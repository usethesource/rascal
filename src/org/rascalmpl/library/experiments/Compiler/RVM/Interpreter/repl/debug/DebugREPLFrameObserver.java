package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVM;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Thrown;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;
import org.rascalmpl.library.util.PathConfig;

import jline.Terminal;

public class DebugREPLFrameObserver implements IFrameObserver {

	private final InputStream stdin;
	private final OutputStream stdout;
	private final File historyFile;
	private final Terminal terminal;
	private final BreakPointManager breakPointManager;
	
	private RVM rvm;
	
	public DebugREPLFrameObserver(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal, PathConfig pcfg) throws IOException{
		this.stdin = stdin;
		this.stdout = stdout;
		this.historyFile = file;
		this.terminal = terminal;
		this.breakPointManager = new BreakPointManager(new PrintWriter(stdout), pcfg);
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
		return this;
		//return breakPointManager.hasEnabledBreakPoints() ? this : null;
	}
	
	@Override public void setRVM(RVM rvm){
		this.rvm = rvm;
	}
	
	@Override public RVM getRVM(){
		return rvm;
	}
	
	@Override
	public boolean observe(Frame frame) {
		try {
			if(breakPointManager.matchOnObserve(frame)){
				new DebugREPL(rvm, frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
	@Override
	public boolean enter(Frame frame) {
		try {
			if(breakPointManager.matchOnEnter(frame)){
				new DebugREPL(rvm, frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
	@Override
	public boolean leave(Frame frame, Object rval) {
		try {
			if(breakPointManager.matchOnLeave(frame, rval)){
				new DebugREPL(rvm, frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
	@Override
	public boolean exception(Frame frame, Thrown thrown){
		try {
			if(breakPointManager.matchOnException(frame, thrown)){
				new DebugREPL(rvm, frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return breakPointManager.shouldContinue();
	}
	
}
