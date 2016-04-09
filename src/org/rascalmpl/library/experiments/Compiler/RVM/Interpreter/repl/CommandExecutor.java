package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ExecutionTools;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NameCompleter;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMExecutable;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalExecutionContextBuilder;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.help.HelpManager;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.library.lang.rascal.boot.Kernel;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IMapWriter;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class CommandExecutor {
	
	private PrintWriter stdout;
	private PrintWriter stderr;
	private final IValueFactory vf;
	private String consoleInputName = "ConsoleInput";
	public static String consoleInputPath = "/ConsoleInput.rsc";
	//public static String muLibraryPath = "/experiments/Compiler/muRascal2RVM/MuLibrary.mu";
	private ISourceLocation consoleInputLocation;
	private RVMExecutable rvmConsoleExecutable;
	private RVMExecutable lastRvmConsoleExecutable;
	private final Prelude prelude;
	
	boolean semiColonAdded = false;
	
	private DebugREPLFrameObserver debugObserver;
	
	private HelpManager helpManager;
	
	boolean debug;
	boolean debugRVM;
	boolean testsuite;
	boolean profile;
	boolean trace;
	boolean coverage;
	boolean jvm;
	boolean verbose;
	
	private ArrayList<String> imports;
	private HashMap<String,String> syntaxDefinitions;
	private ArrayList<String> declarations;	
	
	private HashMap<String, Variable> variables = new HashMap<String, Variable>();
	
	private final String shellModuleName = "CompiledRascalShell";
	
	private boolean forceRecompilation = true;
	private IMap moduleTags;
	private Kernel kernel;
	
	public CommandExecutor(PrintWriter stdout, PrintWriter stderr) {
		this.stdout = stdout;
		this.stderr = stderr; 
		vf = ValueFactoryFactory.getValueFactory();
		prelude = new Prelude(vf);
		try {
			consoleInputLocation = vf.sourceLocation("test-modules", "", consoleInputName + ".rsc");
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot initialize: " + e.getMessage());
		}
		debug = false;							// options per executed command
		debugRVM = false;
		testsuite = false;
		profile = false;
		trace = false;
		coverage = false;
		jvm = true;
		verbose = false;
		
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("bootstrapParser"), vf.string(""));
		IMap CompiledRascalShellModuleTags = w.done();
		
		w = vf.mapWriter();
		w.put(vf.string(shellModuleName), CompiledRascalShellModuleTags);
		w.put(vf.string(consoleInputName), CompiledRascalShellModuleTags);
		moduleTags = w.done();
		
		RascalExecutionContext rex = 
				RascalExecutionContextBuilder.normalContext(vf, this.stdout, this.stderr)
					.withModuleTags(moduleTags)
					.forModule(shellModuleName)
					.setJVM(true)					// options for complete repl
					//.setProfiling(true)
					.build();
		
		kernel = new Kernel(vf, rex);
		
		imports = new ArrayList<String>();
		syntaxDefinitions = new HashMap<>();
		declarations = new ArrayList<String>();
		
		helpManager = new HelpManager(stdout, stderr);
		stderr.println("Type 'help' for information or 'quit' to leave");
	}
	
	public void reset(){
		imports = new ArrayList<String>();
		syntaxDefinitions = new HashMap<>();
		declarations = new ArrayList<String>();
		forceRecompilation = true;
	}
	
	public void setDebugObserver(DebugREPLFrameObserver observer){
		this.debugObserver = observer;
	}
	
	private IMap makeCompileKwParamsAsIMap(){
		IMapWriter w = vf.mapWriter();
		w.put(vf.string("verbose"), vf.bool(false));
		return w.done();
	}
	
	private boolean noErrors(String modString, RVMExecutable exec){
		ISet errors = exec.getErrors();
		if(errors.size() == 0){
			return true;
		}
		for(IValue m : errors){
			IConstructor msg = (IConstructor) m;
			String msgKind = msg.getName();
			String txt = ((IString)msg.get("msg")).getValue();
			ISourceLocation src = (ISourceLocation)msg.get("at");
			String hint = " AT " + src.toString();
			if(src.getPath().equals(consoleInputPath)){
				int offset = src.getOffset();
				String subarea = modString.substring(offset, offset + src.getLength());
				hint = subarea.matches("[a-zA-Z0-9]+") ? "" : " IN '" + subarea + "'";
			}
			(msgKind.equals("error") ? stderr : stdout).println("[" + msgKind + "] " + txt + hint);
		}
		return false;
	}
	
	private IValue executeModule(String main, boolean onlyMainChanged){
		StringWriter w = new StringWriter();
		w.append("@bootstrapParser module ConsoleInput\n");
		for(String imp : imports){
			w.append("import ").append(imp).append(";\n");
		}
		for(String syn : syntaxDefinitions.keySet()){
			w.append(syntaxDefinitions.get(syn));
		}
		for(String decl : declarations){
			w.append(decl);
		}
//		for(String name : variables.keySet()){
//			Variable var = variables.get(name);
//			w.append(var.type).append(" ").append(name).append(" = ").append(var.value.toString()).append(";\n");
//		}
		w.append(main);
		String modString = w.toString();
//		System.err.println(modString);
		try {
			prelude.writeFile(consoleInputLocation, vf.list(vf.string(modString)));
			IBool reuseConfig = vf.bool(onlyMainChanged && !forceRecompilation);
			
			rvmConsoleExecutable = kernel.compileAndLinkIncremental(vf.string(consoleInputName), reuseConfig, makeCompileKwParamsAsIMap());
			
			if(noErrors(modString, rvmConsoleExecutable)){
				RascalExecutionContext rex = RascalExecutionContextBuilder.normalContext(vf, stdout, stderr)
						.forModule(shellModuleName)
						.withModuleTags(moduleTags)
						.setDebug(debug)
						.setDebugRVM(debugRVM)
						.setTestsuite(testsuite)
						.setProfile(profile)
						.setTrace(trace)
						.setCoverage(coverage)
						.setJVM(jvm)
						.observedBy(debugObserver.getObserverWhenActiveBreakpoints())
						.build();
						
				IValue val = ExecutionTools.executeProgram(rvmConsoleExecutable, vf.mapWriter().done(), rex);
				lastRvmConsoleExecutable = rvmConsoleExecutable;
				forceRecompilation = false;
				return val;
			} else {
				return null;
			}
		} catch (Thrown e){
			stderr.println(e.getMessage());
			e.printStackTrace(stderr);
			return null;
		} catch (IOException e) {
			stderr.println(e.getMessage());
			e.printStackTrace(stderr);
			return null;
		}
	}
	
	private boolean is(ITree tree, String consName){
		return TreeAdapter.getConstructorName(tree).equals(consName);
	}
	
	private ITree get(ITree tree, String fieldName){
		return TreeAdapter.getArg(tree, fieldName);
	}
	
	private boolean has(ITree tree, String fieldName){
		try {
			TreeAdapter.getArg(tree, fieldName);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	public IValue eval(String statement, ISourceLocation rootLocation) {
		ITree cmd = parseCommand(statement, rootLocation);
		
		cmd = TreeAdapter.getStartTop(cmd);
		
		if(is(cmd, "expression")){
			return evalExpression(statement, get(cmd, "expression"));
		}
		if(is(cmd, "statement")){
			try {
				return evalStatement(statement, get(cmd, "statement"));
			} catch (FactTypeUseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		if(is(cmd, "import")){
			try {
				return evalImport(statement, get(cmd, "imported"));
			} catch (FactTypeUseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		if(is(cmd, "declaration")){
			try {
				return evalDeclaration(statement, get(cmd, "declaration"));
			} catch (FactTypeUseException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//		if(is(cmd, "shell")){
//			try {
//				return evalShellCommand(statement, get(cmd, "command"));
//			} catch (FactTypeUseException | IOException e) {
//			}
//		}
		return null;
	}
	
	private IValue evalExpression(String src, ITree exp){
		try {
			return executeModule("\nvalue main() = " + src + ";\n", true);
		} catch (Thrown e){
			return null;
		}
	}
	
	private void declareVar(String type, String name, String val){
		variables.put(name,  new Variable(type, name, val));
		StringWriter w = new StringWriter();
		w.append(type).append(" ").append(name).append(" = ").append(val).append(";\n");
		declarations.add(w.toString());
	}
	
	private IValue report(String msg){
		stdout.println(msg);
		stdout.flush();
		return null;
	}
	
	private String getBaseVar(ITree assignable) throws FactTypeUseException, IOException{
		if(is(assignable, "variable")){
			return unparse(get(assignable, "qualifiedName"));
		}
		if(has(assignable, "receiver")){
			return getBaseVar(get(assignable, "receiver"));
		}
		return null;
	}
	
	private IValue evalStatement(String src, ITree stat) throws FactTypeUseException, IOException{
		
		String consName = TreeAdapter.getConstructorName(stat);
		switch(consName){
		
		case "expression":
			String innerExp = unparse(get(stat, "expression"));
			try {
				return executeModule("\nvalue main() = " + innerExp + ";\n", true);
			} catch (Exception e){
				forceRecompilation = true;
				return null;
			}
			
		case "assignment":
			//assignment: Assignable assignable Assignment operator Statement!functionDeclaration!variableDeclaration statement
			
			ITree assignable = get(stat, "assignable");
			String name = getBaseVar(assignable);
			
			if(name != null){
				Variable var = variables.get(name);
				if(var != null){
					IValue val = executeModule("\nvalue main() { " + src + "}\n", true);
					if(val != null){
						var.value = val.toString();
						return val;
					}
					return null;
				} else {
					IValue val = executeModule("\nvalue main() { " + unparse(get(stat, "statement")) + " }\n", true);
					
					if(val == null){
						return null;
					}
					declareVar(val.getType().toString(), name, val.toString());
					forceRecompilation = true;
					return val;
				}
			}
			return report("Assignable is not supported supported");
			
		case "variableDeclaration":
			ITree declarator = get(get(stat, "variableDeclaration"), "declarator");
			ITree type = get(declarator, "type");
			ITree variables = get(declarator, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);
			if(vars.length() != 1){
				return report("Multiple names in variable declaration are not supported");
			}
			ITree var = (ITree) vars.get(0);
			if(is(var, "initialized")){
				String name1 = unparse(get(var, "name"));
				String initial = unparse(get(var, "initial"));
				if(variables.get(name1) != null){
					report("Redeclaring variable " + name1);
				}
				declareVar(unparse(type), name1, initial);
				return executeModule("\nvalue main() = <name1>;\n", false);
			} else {
				return report("Initialization required in variable declaration");
			}
		default:
			return executeModule("\nvalue main() { " + unparse(stat) + " }\n", true);
		}
	}
	
	private IValue evalImport(String src, ITree imp) throws FactTypeUseException, IOException{
		IValue result;
		if(is(imp, "default")){
			String impName = unparse(get(get(imp, "module"), "name"));
			if(imports.contains(impName)){
				return null;
			}
			imports.add(impName);
			try {
				forceRecompilation = true;
				result = executeModule("\nvalue main() = \"ok\";\n", false);
				if(result == null){
					imports.remove(impName);
					forceRecompilation = true;
				}
				
				return null;
			} catch (Exception e){
				imports.remove(impName);
				forceRecompilation = true;
				return null;
			}
		}
		if(is(imp, "syntax")){
			StringWriter w = new StringWriter();
			TreeAdapter.unparse(get(get(imp, "syntax"), "defined"), w);
			String name = w.toString();
			syntaxDefinitions.put(name, src);
			try {
				result = executeModule("\nvalue main() = \"ok\";\n", false);
				if(result == null){
					syntaxDefinitions.remove(name);
					forceRecompilation = true;
				}
				return null;
			} catch (Exception e){
				syntaxDefinitions.remove(name);
				forceRecompilation = true;
				return null;
			}
		}
		
		return null;
	}
	
	private IValue evalDeclaration(String src, ITree cmd) throws FactTypeUseException, IOException{
		IValue result;
		if(is(cmd, "variable")){
			ITree type = get(cmd, "type");
			ITree variables = get(cmd, "variables");
			IList vars = TreeAdapter.getListASTArgs(variables);
			if(vars.length() != 1){
				return report("Multiple names in variable declaration not supported");
			}
			ITree var = (ITree) vars.get(0);
			if(is(var, "initialized")){
				String name = unparse(get(var, "name"));
				String initial = unparse(get(var, "initial"));
				declareVar(unparse(type), name, initial);
				try {
					forceRecompilation = true;
					result = executeModule("\nvalue main() = " + name + ";\n", false);
					if(result == null){
						this.variables.remove(name);
						forceRecompilation = true;
					}
					return result;
				} catch (Exception e){
					this.variables.remove(name);
					forceRecompilation = true;
					return null;
				}
			} else {
				return report("Initialization required in variable declaration");
			}
		}
		
		// TODO handle here data, dataAbstract and function
		declarations.add(src);
	
		try {
			result =  executeModule("\nvalue main() = true;\n", false);
			if(result == null){
				declarations.remove(src);
				forceRecompilation = true;
			}
			return null;
		} catch (Exception e){
			declarations.remove(src);
			forceRecompilation = true;
			return null;
		}
	}
	
	private boolean getBooleanValue(String val){
		switch(val){
		case "true": return true;
		case "false": return false;
		default:
			stdout.println("'" + val + "' is not a boolean value");
			return false;
		}
	}
	
	private String unparse(ITree tree) throws FactTypeUseException, IOException{
		StringWriter w = new StringWriter();
		TreeAdapter.unparse(tree, w);
		return w.toString();
	}
	
	private IValue showOptions(){
		StringBuilder sb = new StringBuilder();
		return report(
				sb.append("profile:  ").append(profile).append("\n")
				  .append("trace:    ").append(trace).append("\n")
				  .append("coverage: ").append(coverage).toString());
	}
	
	public IValue evalShellCommand(String[] words) {
		switch(words[0]){
		
		case "set":
			if(words.length == 1){
				return showOptions();
			}
			if(words.length != 3){
				return report("set requires two arguments");
			}
			String name = words[1];
			String val = words[2];
			
			switch(name){
			case "profile":
				profile =  getBooleanValue(val);
				if(profile && (trace || coverage)){
					trace = coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + profile);
				
			case "trace":
				trace = getBooleanValue(val);
				if(trace && (profile || coverage)){
					profile = coverage = false;
					return showOptions();
				}
				return report(name + " set to "  + trace);
				
			case "coverage":
				coverage = getBooleanValue(val);
				if(coverage && (profile || trace)){
					profile = trace = false;
					return showOptions();
				}
				return report(name + " set to "  + coverage);
								
			default:
				return report("Unrecognized option : " + name);
			}
	
		case "help":
			helpManager.printHelp(words);
			break;
			
		case "declarations":
			if(syntaxDefinitions.isEmpty() && declarations.isEmpty() && variables.isEmpty()){
				System.err.println("No declarations");
			} else {
				for(String synName : syntaxDefinitions.keySet()){
					System.err.println(syntaxDefinitions.get(synName));
				}

				for(String decl : declarations){
					System.err.println(decl);
				}

				for(String varName : variables.keySet()){
					Variable var = variables.get(varName);
					System.err.println(var.type + " " + varName + ";");
				}
			}
			
			break;
			
		case "modules":
			if(imports.isEmpty()){
				System.err.println("No imported modules");
			} else {
				for(String imp : imports){
					System.err.println(imp);
				}
			}
			break;
			
		case "unimport":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					imports.remove(words[i]);
				}
			} else {
				imports = new ArrayList<String>();
			}
			break;
			
		case "undeclare":
			if(words.length > 1){
				for(int i = 1; i <words.length; i++){
					variables.remove(words[i]);
					syntaxDefinitions.remove(words[i]);
				}
			} else {
				variables =  new HashMap<>();
				declarations = new ArrayList<>();
				syntaxDefinitions = new HashMap<>();
			}
			break;
		
		case "break":
			debugObserver.getBreakPointManager().breakDirective(words);
			break;
		
		case "clear":
			debugObserver.getBreakPointManager().clearDirective(words);
			break;
		
		case "ignore":
			debugObserver.getBreakPointManager().ignoreDirective(words);
			break;
			
		case "enable":
			debugObserver.getBreakPointManager().enableDirective(words);
			break;
		
		case "disable":
			debugObserver.getBreakPointManager().disableDirective(words);
			break;
		}
		stdout.flush();
		return null;
	}	
	
	  public boolean isStatementComplete(String command) {
		  
		  String[] words = command.split(" ");
		  if(words.length > 0 && CompiledRascalREPL.SHELL_VERBS.contains(words[0])){
			  return true;
		  }
		  try {
			  ITree res = parseCommand(command, URIUtil.rootLocation("prompt"));
		  }
		  catch (ParseError pe) {
			  String[] commandLines = command.split("\n");
			  int lastLine = commandLines.length;
			  int lastColumn = commandLines[lastLine - 1].length();

			  if (pe.getEndLine() + 1 == lastLine && lastColumn < pe.getEndColumn()) { 
				  semiColonAdded = false;
				  return false;
			  }
			  if (!semiColonAdded && pe.getEndLine() + 1 == lastLine && lastColumn == pe.getEndColumn()) { 
				  semiColonAdded = true;
				  boolean isComplete = isStatementComplete(command + ";");
				  semiColonAdded &= isComplete;
				  return isComplete;
			  }
			  return false;
		  }
		  return true;
	  }
	
	public ITree parseCommand(String command, ISourceLocation location) {
		//__setInterrupt(false);
		IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
		ITree tree =  new RascalParser().parse(Parser.START_COMMAND, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));

		//		if (!noBacktickOutsideStringConstant(command)) {
		//		  tree = org.rascalmpl.semantics.dynamic.Import.parseFragments(this, tree, location, getCurrentModuleEnvironment());
		//		}

		return tree;
	}
	
	public PrintWriter getStdErr() {
		return stderr;
	}
	
	public PrintWriter getStdOut() {
		return stdout;
	}
	
	public Collection<String> completePartialIdentifier(String qualifier, String term) {
		if(lastRvmConsoleExecutable != null){
			return lastRvmConsoleExecutable.completePartialIdentifier(new NameCompleter(), term).getResult();
		}
		return null;
	}
	
	public Collection<String> completeDeclaredIdentifier(String term) {
		TreeSet<String> result = null;
		for(String var : variables.keySet()){
			if(var.startsWith(term)){
				if(result == null){
			    	 result = new TreeSet<String>();
			     }
				result.add(var);
			}
		}
		return result;
	}
	
	public Collection<String> completeImportedIdentifier(String term) {
		TreeSet<String> result = null;
		for(String mod : imports){
			if(mod.startsWith(term)){
				if(result == null){
			    	 result = new TreeSet<String>();
			     }
				result.add(mod);
			}
		}
		return result;
	}
}

class Variable {
	String name;
	String type;
	String value;
	
	Variable(String type, String name, String value){
		this.name = name;
		this.type = type;
		this.value = value;
	}
}
