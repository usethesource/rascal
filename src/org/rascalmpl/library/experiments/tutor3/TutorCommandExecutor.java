package org.rascalmpl.library.experiments.tutor3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CompiledRascalREPL;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.RascalShellExecutionException;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import jline.Terminal;

public class TutorCommandExecutor {
	ISourceLocation screenInputLocation;
	CommandExecutor executor;
	StringWriter shellStringWriter;
	PrintWriter shellPrintWriter;
	PrintWriter err;
	String consoleInputPath = "/ConsoleInput.rsc";

	private static class TutorTerminalEmulator implements Terminal {
        @Override
        public void init() throws Exception {
            
        }

        @Override
        public void restore() throws Exception {
        }

        @Override
        public void reset() throws Exception {
        }

        @Override
        public boolean isSupported() {
            return true;
        }

        @Override
        public int getWidth() {
            return 72;
        }

        @Override
        public int getHeight() {
            return 100;
        }

        @Override
        public boolean isAnsiSupported() {
            return false;
        }

        @Override
        public OutputStream wrapOutIfNeeded(OutputStream out) {
            return out;
        }

        @Override
        public InputStream wrapInIfNeeded(InputStream in) throws IOException {
            return in;
        }

        @Override
        public boolean hasWeirdWrap() {
            return false;
        }

        @Override
        public boolean isEchoEnabled() {
            return false;
        }

        @Override
        public void setEchoEnabled(boolean enabled) {
            
        }

        @Override
        public void disableInterruptCharacter() {
            
        }

        @Override
        public void enableInterruptCharacter() {
            
        }

        @Override
        public String getOutputEncoding() {
            return "UTF8";
        }
	}
	
	public TutorCommandExecutor(PathConfig pcfg, PrintWriter err, IDEServices ideServices) throws IOException, NoSuchRascalFunction, URISyntaxException{
		try {
			IValueFactory vf = ValueFactoryFactory.getValueFactory();
			screenInputLocation = vf.sourceLocation("home", "", consoleInputPath);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot initialize: " + e.getMessage());
		}
		this.err = err;
		shellStringWriter = new StringWriter();
		shellPrintWriter = new PrintWriter(shellStringWriter);
		executor = new CommandExecutor(pcfg, shellPrintWriter, shellPrintWriter, ideServices, null);
		executor.setDebugObserver(new DebugREPLFrameObserver(null, System.in, System.out, true, true, null, new TutorTerminalEmulator(), ideServices));
	}
	
	void flush(){
		shellPrintWriter.flush();
	}
	
	void resetOutput(){
		shellStringWriter.getBuffer().setLength(0);
	}
	
	void reset(){
		executor.reset();
	}
	
	IValue eval(String line) throws RascalShellExecutionException, IOException{
	  String[] words = line.split(" ");
	  if(words.length > 0 && CompiledRascalREPL.SHELL_VERBS.contains(words[0])){
	    return executor.evalShellCommand(words);
	  } else {
	    return executor.eval(line, screenInputLocation);
	  }
	}
	
	String evalPrint(String line) throws IOException, RascalShellExecutionException{
	  String[] words = line.trim().split(" ");
	  IValue result;
      if(words.length > 0 && CompiledRascalREPL.SHELL_VERBS.contains(words[0])){
        result = executor.evalShellCommand(words);
      } else {
        result = executor.eval(line, screenInputLocation);
      }
		return executor.resultToString(result);
	}
	
	String getMessages(){
		flush();
		return shellStringWriter.toString();
	}
	
	void error(String msg){
		err.println(msg);
	}
	
	boolean isStatementComplete(String line){
		return executor.isStatementComplete(line);
	}
}
