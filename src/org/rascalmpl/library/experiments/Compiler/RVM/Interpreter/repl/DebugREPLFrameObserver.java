package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

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
	
	public DebugREPLFrameObserver(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, File file, Terminal terminal) throws IOException{
		this.stdin = stdin;
		this.stdout = stdout;
		this.historyFile = file;
		this.terminal = terminal;
		this.breakPointManager = new BreakPointManager();
	}
	
	@Override
	public void observe(Frame frame) {
		try {
			new DebugREPL(frame, breakPointManager, stdin, stdout, true, true, historyFile, terminal).run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
