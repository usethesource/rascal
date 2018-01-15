package org.rascalmpl.shell.compiled;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CommandExecutor;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.CompiledRascalREPL;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.debug.DebugREPLFrameObserver;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.shell.ShellRunner;

import jline.Terminal;

public class CompiledREPLRunner extends BaseREPL  implements ShellRunner {
	
	public CompiledREPLRunner(PathConfig pcfg, InputStream stdin, OutputStream stdout, IDEServices ideServices, Terminal term) throws IOException, URISyntaxException {
		super(makeCompiledRascalREPL(pcfg, ideServices, term, 
		    new DebugREPLFrameObserver(pcfg, stdin, stdout, true, term.isAnsiSupported(), getHistoryFile(), term, ideServices)), 
		    pcfg, stdin, stdout, true, term.isAnsiSupported(), getHistoryFile(), term, ideServices);
	}

    private static CompiledRascalREPL makeCompiledRascalREPL(PathConfig pcfg, IDEServices ideServices, Terminal term, final DebugREPLFrameObserver observer) throws IOException, URISyntaxException {
        return new CompiledRascalREPL(pcfg, true, term.isAnsiSupported(), false, getHistoryFile(), ideServices) {
            @Override
            public void stop() {
                
            }
            
            @Override
            protected CommandExecutor constructCommandExecutor(PathConfig pcfg, PrintWriter stdout, PrintWriter stderr, IDEServices ideServices) throws IOException, NoSuchRascalFunction, URISyntaxException {
                CommandExecutor exec = new CommandExecutor(pcfg, stdout, stderr, ideServices, this, observer);
                setMeasureCommandTime(true);
                return exec;
            }
        };
    }

	private static File getHistoryFile() throws IOException {
		File home = new File(System.getProperty("user.home"));
		File rascal = new File(home, ".rascal");
		if (!rascal.exists()) {
			rascal.mkdirs();
		}
		File historyFile = new File(rascal, ".repl-history-rascal-terminal");
		if (!historyFile.exists()) {
			historyFile.createNewFile();
		}
		return historyFile;
	}
	
	@Override
	public void run(String[] args) throws IOException {
		// there are no args for now
		run();
	}
}