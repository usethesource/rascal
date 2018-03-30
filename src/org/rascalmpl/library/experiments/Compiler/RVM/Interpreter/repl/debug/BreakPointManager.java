package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalRuntimeException;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class BreakPointManager {
	
	enum DEBUG_MODE { STEP, SKIP, NEXT, BREAK, RETURN };
	
	private List<BreakPoint> breakpoints;
	int uid;
	
	private boolean shouldQuit= false;
	
	DEBUG_MODE mode = DEBUG_MODE.BREAK;
	
	private final String FAINT_ON = Ansi.ansi().a(Attribute.INTENSITY_FAINT).toString();
	private final String FAINT_OFF = Ansi.ansi().a(Attribute.INTENSITY_BOLD_OFF).toString();
	private final String RED_ON = Ansi.ansi().fg(Ansi.Color.RED).toString();
	private final String RED_OFF = Ansi.ansi().fg(Ansi.Color.BLACK).toString();
	
	Frame currentFrame = null;  // next mode, only break in current function
	Frame returnFrame = null;	// only break on return from this frame

	private final PathConfig pcfg;
	private PrintWriter stdout;
	private IDEServices ideServices;

	private final String listingIndent = "\t";
	private boolean autoList = true;
	private final int defaultListingDelta = 5;
	
	//RascalHighlighter highlighter;
	
	Cache<String, IValue> parsedModuleCache;
	IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	BreakPointManager(PathConfig pcfg, PrintWriter stdout, IDEServices ideServices){
		this.stdout = stdout;
		this.pcfg = pcfg;
		this.ideServices = ideServices;
		breakpoints = new ArrayList<>();
		uid = 1;
//		highlighter = new RascalHighlighter()
//				.setKeywordMarkup(Ansi.ansi().bold().toString(), 
//							      Ansi.ansi().boldOff().toString())
//				.setCommentMarkup(Ansi.ansi().fg(Ansi.Color.GREEN).toString(), 
//						          Ansi.ansi().fg(Ansi.Color.BLACK).toString());
		parsedModuleCache = Caffeine.newBuilder()
			    .weakValues()
				.maximumSize(5)
				.build();
	}
	
	void reset(){
		mode =  DEBUG_MODE.BREAK;
		currentFrame = null;
		returnFrame = null;
	}
	
	void edit(ISourceLocation file){
	  ideServices.edit(file);
	}
	
	private boolean isBlackListed(Frame frame){
		return frame == null || frame.src.getPath().equals(CommandExecutor.consoleInputPath) ||
			   frame.function.getPrintableName().endsWith("_init") ||
			   frame.src.getPath().endsWith(".mu");
	}
	
	void setStdOut(PrintWriter stdout){
		this.stdout = stdout;
	}
	
	void setAutoList(boolean autoList){
		this.autoList = autoList;
	}
	
	void setBreakMode(Frame frame){
		if(mode == DEBUG_MODE.RETURN){
			return;
		}
		mode = DEBUG_MODE.BREAK;
		this.currentFrame = null;
	}
	
	void setStepMode(Frame frame){
		mode = isBlackListed(frame) ?  DEBUG_MODE.SKIP : DEBUG_MODE.STEP;
		this.currentFrame = null;
	}
	
	void setNextMode(Frame currentFrame){
		mode = DEBUG_MODE.NEXT;
		this.currentFrame = currentFrame;
	}
	
	void setReturnMode(Frame currentFrame){
		mode = DEBUG_MODE.RETURN;
		this.returnFrame = currentFrame;
	}
	
	boolean hasEnabledBreakPoints(){
		for(BreakPoint breakpoint : breakpoints){
			if(breakpoint.enabled){
				return true;
			}
		}
		return false;
	}
	
	boolean doAutoList(Frame frame){
		if(autoList){
			listingDirective(frame, defaultListingDelta);
		}
		return true;
	}
	
	BreakPoint getBreakPoint(int reqId){
		for(BreakPoint breakpoint : breakpoints){
			if(breakpoint.id == reqId){
				return breakpoint;
			}
		}
		return null;
	}
	
	void requestQuit(){
		shouldQuit = true;
	}
	
	public boolean shouldContinue(){
		return !shouldQuit;
	}
	
	/****************************************************************/
	/*				Handle all frame events							*/
	/****************************************************************/
	
	boolean matchOnObserve(Frame frame){
		if(!frame.src.hasLineColumn()){
			return false;
		}
		switch(mode){
		
		case STEP:
			if(isBlackListed(frame)){
				mode = DEBUG_MODE.SKIP;
				return false;
			}
			return doAutoList(frame);
			
		case SKIP:
			return false;
		
		case NEXT:
			if(frame != currentFrame){
				return false;
			}
			return doAutoList(frame);
			
		case RETURN:
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnObserve(frame)){
					return doAutoList(frame);
				}	
			}
		}
		return false;
	}
	
	boolean matchOnEnter(Frame frame){
		if(!frame.src.hasLineColumn()){
			return false;
		}
		switch(mode){
		
		case STEP:
			if(isBlackListed(frame)){
				mode = DEBUG_MODE.SKIP;
				return false;
			}
			//return doAutoList(frame);
			return true;
			
		case SKIP:
			if(!isBlackListed(frame)){
				mode = DEBUG_MODE.STEP;
				//return doAutoList(frame);
				return true;
			}
			return false;
	
		case NEXT:
			return false;
			
		case RETURN:
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnEnter(frame)){
					mode = DEBUG_MODE.STEP;
					//return doAutoList(frame);
					return true;
				}	
			}
		}
		return false;
	}
	
	boolean matchOnLeave(Frame frame, Object rval){
		if(!frame.src.hasLineColumn()){
			return false;
		}
		switch(mode){
		
		case STEP:
			if(isBlackListed(frame.previousCallFrame)){
				mode = DEBUG_MODE.SKIP;
			}
			return doAutoList(frame);
			
		case SKIP:
			if(!isBlackListed(frame.previousCallFrame)){
				mode = DEBUG_MODE.STEP;
			}
			return false;
		
		case NEXT:
			return false;
			
		case RETURN:
			if(returnFrame == frame){
				returnFrame = null;
				mode = DEBUG_MODE.BREAK;
				if(autoList){
					listingDirective(frame, defaultListingDelta);
				}
				stdout.println(BaseREPL.PRETTY_PROMPT_PREFIX  + "Function " + frame.function.getPrintableName() + " will return: " + rval +  BaseREPL.PRETTY_PROMPT_POSTFIX);
				stdout.flush();
				return true;
			}
			return false;
			
		case BREAK:
			for(BreakPoint bp : breakpoints){
				if(bp.matchOnLeave(frame)){
					return doAutoList(frame);
				}	
			}
		}
		return false;
	}
	
	boolean matchOnException(Frame frame, Thrown thrown){
		stdout.print(RED_ON);
		thrown.printStackTrace(stdout);
		stdout.print(RED_OFF);
		return doAutoList(frame);
	}
	
	/****************************************************************/
	/*				Handle all debug directives						*/
	/****************************************************************/
	
	// break directive during debug mode
	void breakDirective(Frame frame, String[] args) throws NumberFormatException {
		if(args.length == 2 && args[1].matches("[0-9]+")){	// break <lino>
			int lino = Integer.parseInt(args[1]);
			String path = frame.src.getPath();
			add(new LineBreakpoint(uid++, path, lino));
		} else {
			breakDirective(args);
		}
	}
	
	// break directive outside debug mode
	public void breakDirective(String[] args) throws NumberFormatException {
		if(args.length == 1){								// break
			printBreakPoints(stdout);
			return;
		}
		if(args.length == 2){								// break <functionName>
			add(new FunctionEnterBreakpoint(uid++, args[1]));
		}
		if(args.length == 3){								// break <moduleName> <lino>
			ISourceLocation modSrc = pcfg.resolveModule(args[1]);
			if(modSrc != null){
				add(new LineBreakpoint(uid++, modSrc.getPath(), Integer.parseInt(args[2])));
			} else {
				stdout.println("Module " + args[1] + " not found");
				stdout.flush();
			}
		}
	}
	
	public void ignoreDirective(String[] args)  throws NumberFormatException {
		int ignoreCnt = 0;
		if(args.length == 3){
			ignoreCnt = Integer.parseInt(args[2]);
		}
		if(args.length == 2 || args.length == 3){
			int bkpt = Integer.parseInt(args[1]);
			BreakPoint breakpoint = getBreakPoint(bkpt);
			if(breakpoint != null){
				breakpoint.setIgnore(ignoreCnt);
				return;
			}
			stdout.println("Breakpoint #" + bkpt + " is not defined");
			return;
		}
		stdout.println("ignore requires 1 or 2 arguments");
	}
	
	void returnDirective(Frame frame){
		setReturnMode(frame);
	}
	
	public void clearDirective(String[] args) throws NumberFormatException {
		if(args.length == 1){
			breakpoints = new ArrayList<BreakPoint>();
			return;
		}
		ArrayList<BreakPoint> newBreakpoints = new ArrayList<BreakPoint>();

		next:
			for(BreakPoint breakpoint : breakpoints){
				for(int i = 1; i < args.length; i++){
					int bpno = Integer.parseInt(args[i]);
					if(breakpoint.getId() == bpno){
						continue next;
					}
				}
				newBreakpoints.add(breakpoint);
			}
		breakpoints = newBreakpoints;
	}
	
	public void enableDirective(String[] args) throws NumberFormatException {
		setEnabled(args, true);
	}
	
	public void disableDirective(String[] args) throws NumberFormatException {
		setEnabled(args, false);
	}
	
	private void setEnabled(String[] args, boolean enabled) throws NumberFormatException {
		if(args.length == 1){
			for(BreakPoint breakpoint : breakpoints){
				breakpoint.setEnabled(enabled);
			}
			return;
		}
		for(BreakPoint breakpoint : breakpoints){
			for(int i = 1; i < args.length; i++){
				int bpno = Integer.parseInt(args[i]);
				if(breakpoint.getId() == bpno){
					breakpoint.setEnabled(enabled);
				}
			}
		}
	}
	
	void listingDirective(Frame frame, String[] args){
		int delta = defaultListingDelta;
		if(args.length > 1){
			try {
				delta = Integer.parseInt(args[1]);
			} catch(NumberFormatException e){
				// use the default value
			}
		}
		listingDirective(frame, delta);
	}
	
	// private helper functions

	private void add(BreakPoint breakpoint){
		breakpoints.add(breakpoint);
	}
	
	protected void printBreakPointsHeader(PrintWriter stdout){
		stdout.println("Id\tEnabled\tKind\tIgnore\tDetails");
	}
	
	private void printBreakPoints(PrintWriter stdout){
		if(breakpoints.isEmpty()){
			stdout.println("No breakpoints");
		} else {
			printBreakPointsHeader(stdout);
			for(BreakPoint breakpoint : breakpoints){
				breakpoint.println(stdout);
			}
		}
		stdout.flush();
	}
	
	private void listingDirective(Frame frame, int delta){
		ISourceLocation breakpointSrc = frame.src;
		int breakpointSrcBegin = breakpointSrc.getBeginLine();
		int breakpointSrcEnd = breakpointSrc.getEndLine();
		
		int breakpointDelta = breakpointSrcEnd - breakpointSrcBegin;
		
		int windowBegin;
		int windowEnd;
		int windowSize = 2 * delta + 1;
		
		ISourceLocation functionSrc = frame.function.src;
		if(functionSrc.getEndLine() - functionSrc.getBeginLine() <= windowSize){
			windowBegin = functionSrc.getBeginLine();
			windowEnd = functionSrc.getEndLine();
		} else if(breakpointDelta <=  windowSize){
			windowBegin = Math.max(1, breakpointSrcBegin - delta);
			windowEnd = breakpointSrcEnd + delta;
		} else {
			windowBegin = breakpointSrcBegin - 2;
			windowEnd = breakpointSrcBegin + windowSize - 2;
		}
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		try {
			String[] lines;
			ISourceLocation srcFile = vf.sourceLocation(breakpointSrc.getScheme(), "", breakpointSrc.getPath());
			
			if (URIResolverRegistry.getInstance().exists(srcFile)) {
			    if(breakpointSrc.getPath().endsWith(".rsc")){
			        //A Rascal source file, parse and highlight
			        ITree parseTree = getParsedModule(srcFile);
			        StringWriter sw = new StringWriter();
			        TreeAdapter.unparseWithFocus(parseTree, sw, breakpointSrc);
			        lines = sw.toString().split("\n");
			    } else {
			        // Something else (muRascal), no highlighting
			        lines = getResourceContentLines(srcFile);
			    }
			    
			    for(int lino = windowBegin; lino <= windowEnd; lino++){
	                stdout.println(FAINT_ON + String.format("%4d", lino) + FAINT_OFF + listingIndent + lines[lino - 1]);
	            }
			} 
			else {
			    stdout.println("Source file does not exist: " + breakpointSrc);
			}
		} catch (URISyntaxException e){
			stdout.println("Cannot create URI for source file: " + breakpointSrc);
		} catch (IOException e) {
			stdout.println("Cannot read source file");
		}
		stdout.flush();
	}
	
	private IValue lastModified(ISourceLocation sloc) {
		try {
			return vf.datetime(URIResolverRegistry.getInstance().lastModified(sloc));
		} catch(FileNotFoundException e){
			throw RuntimeExceptionFactory.pathNotFound(sloc, null, null);
		}
		catch (IOException e) {
			throw RuntimeExceptionFactory.io(vf.string(e.getMessage()), null, null);
		}
	}
	
	private ITree getParsedModule(ISourceLocation loc){
		String key = loc.toString() + lastModified(loc).toString();
		return (ITree) parsedModuleCache.get(key, k -> {

			try {
				ITree tree = new RascalParser().parse(Parser.START_MODULE, loc.getURI(), getResourceContent(loc), new NoActionExecutor() , new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
				return tree;
			} catch (IOException e) {
				throw RascalRuntimeException.io(vf.string(e.getMessage()), null);
			}
		});
	}
	
	private char[] getResourceContent(ISourceLocation location) throws IOException{
        try (Reader stream = URIResolverRegistry.getInstance().getCharacterReader(location)) {
			return InputConverter.toChar(stream);
		}
	}
	
	private String[] getResourceContentLines(ISourceLocation location) throws IOException {
	    List<String> result = new ArrayList<>();
        try (BufferedReader stream = new BufferedReader(URIResolverRegistry.getInstance().getCharacterReader(location))) {
            String line;
            while ((line = stream.readLine()) != null) {
                result.add(line);
            }
            return result.toArray(new String[result.size()]);
		}
	}

}
