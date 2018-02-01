package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.BaseREPL;

import jline.Terminal;

public class DebugREPLFrameObserver implements IFrameObserver {
    protected final PathConfig pcfg;
	private final InputStream stdin;
	private final OutputStream stdout;
	private final File historyFile;
	private final Terminal terminal;
	protected final BreakPointManager breakPointManager;
	
	private RVMCore rvm;
	
	public DebugREPLFrameObserver(PathConfig pcfg, InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal, IDEServices ideServices) throws IOException{
		this.pcfg = pcfg;
		this.stdin = stdin;
		this.stdout = stdout;
		this.historyFile = file;
		this.terminal = terminal;
		this.breakPointManager = new BreakPointManager(pcfg, new PrintWriter(stdout, true), ideServices);
	}
	
	void reset(){
		if(breakPointManager != null){
			breakPointManager.reset();
		}
	}
	
	public BreakPointManager getBreakPointManager(){
		return breakPointManager;
	}
	
	public DebugREPLFrameObserver getObserverWhenActiveBreakpoints(){
		breakPointManager.reset();
		//return this;
		return breakPointManager.hasEnabledBreakPoints() ? this : null;
	}
	
	@Override public void setRVM(RVMCore rvm){
		this.rvm = rvm;
	}
	
	@Override public RVMCore getRVM(){
		return rvm;
	}
	
	@Override
	public boolean observe(Frame frame) {
		try {
			if(breakPointManager.matchOnObserve(frame)){
				makeDebugRepl(frame);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
	@Override
	public boolean enter(Frame frame)  {
		try {
			if(breakPointManager.matchOnEnter(frame)){
				makeDebugRepl(frame);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
	@Override
	public boolean leave(Frame frame, Object rval) {
		try {
			if(breakPointManager.matchOnLeave(frame, rval)){
				makeDebugRepl(frame);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}

    private void makeDebugRepl(Frame frame) throws IOException, URISyntaxException {
        new BaseREPL(new DebugREPL(rvm, frame, breakPointManager), pcfg, stdin, stdout, true, true, historyFile, terminal, null).run();
    }
	
	@Override
	public boolean exception(Frame frame, Thrown thrown){
		try {
			if(breakPointManager.matchOnException(frame, thrown)){
			    makeDebugRepl(frame);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
}
