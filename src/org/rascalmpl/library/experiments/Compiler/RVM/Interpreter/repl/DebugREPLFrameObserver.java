package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.rascalmpl.library.experiments.Compiler.Commands.PathConfig;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.observers.IFrameObserver;

import jline.Terminal;
import jline.console.ConsoleReader;

public class DebugREPLFrameObserver implements IFrameObserver {

	private final InputStream stdin;
	private final OutputStream stdout;
	private final File historyFile;
	private final Terminal terminal;
	private final BreakPointManager breakPointManager;
	
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
	
	BreakPointManager getBreakPointManager(){
		return breakPointManager;
	}
	
	DebugREPLFrameObserver getObserverWhenActiveBreakpoints(){
		breakPointManager.reset();
		return breakPointManager.hasEnabledBreakPoints() ? this : null;
	}
	
	@Override
	public void observe(Frame frame) {
		try {
			if(breakPointManager.matchOnObserve(frame)){
				new DebugREPL(frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void enter(Frame frame) {
		try {
			if(breakPointManager.matchOnEnter(frame)){
				new DebugREPL(frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(Frame frame, Object rval) {
		try {
			if(breakPointManager.matchOnLeave(frame, rval)){
				new DebugREPL(frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
