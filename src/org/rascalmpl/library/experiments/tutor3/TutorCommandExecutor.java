package org.rascalmpl.library.experiments.tutor3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ideservices.IDEServices;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.repl.RascalShellExecutionException;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;
import org.rascalmpl.values.uptr.IRascalValueFactory;

import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class TutorCommandExecutor {
    private final RascalInterpreterREPL repl;
    private final IValueFactory vf;
	private ByteArrayOutputStream shellStringWriter;
	public  PrintWriter err;

	public TutorCommandExecutor(PathConfig pcfg, ByteArrayOutputStream err, IDEServices ideServices) throws IOException, NoSuchRascalFunction, URISyntaxException{
        shellStringWriter = new ByteArrayOutputStream();
        this.err = new PrintWriter(new OutputStreamWriter(err, "utf8"));
        
        repl = new RascalInterpreterREPL(null, shellStringWriter, false, false, false, null) {
            private PrintWriter stdout;
            private PrintWriter stderr;

            @Override
            protected Evaluator constructEvaluator(Writer stdout, Writer stderr) {
                this.stdout = new PrintWriter(stdout, true);
                this.stderr = new PrintWriter(stderr, true);
                return ShellEvaluatorFactory.getDefaultEvaluator(this.stdout, this.stderr);
            }

            @Override
            public void handleInput(String line, Map<String, String> output, Map<String, String> metadata)
                throws InterruptedException {
                super.handleInput(line, output, metadata);
                stdout.flush();
                stderr.flush();
            }
        };
	    
	    repl.initialize(new OutputStreamWriter(shellStringWriter, "utf8"), this.err);
	    repl.setMeasureCommandTime(false);
	    
	    vf = IRascalValueFactory.getInstance();
	}
	
	void flush(){
	    try {
	        repl.getOutput().flush();
	    }
	    catch (IOException e) {
	        // nothing
	    }
	}
	
	void resetOutput(){
		shellStringWriter.reset();
	}
	
	void reset(){
	    repl.cleanEnvironment();
	}
	
	IValue eval(String line) throws RascalShellExecutionException, IOException{
	    Map<String,String> output = new HashMap<>();

	    try {
	        repl.handleInput(line, output, new HashMap<>());
	        String out = output.get("text/plain");

	        if (out != null) {
	            return vf.string(out);
	        }
	        else {
	            return vf.string("");
	        }
	    }
	    catch (InterruptedException e) {
	        e.printStackTrace(err);
	        return vf.string("");
	    }
	}
	
	String evalPrint(String line) throws IOException, RascalShellExecutionException{
	    return ((IString) eval(line)).getValue();
	}
	
	String getMessages() throws UnsupportedEncodingException{
		flush();
		return shellStringWriter.toString("utf8");
	}
	
	void error(String msg){
		err.println(msg);
	}
	
	boolean isStatementComplete(String line){
		return repl.isStatementComplete(line);
	}
}
