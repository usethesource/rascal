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
	private final ByteArrayOutputStream shellStandardOutput;
	private final ByteArrayOutputStream shellErrorOutput;
	private final PrintWriter err;

	public TutorCommandExecutor(PathConfig pcfg) throws IOException, NoSuchRascalFunction, URISyntaxException{
        shellStandardOutput = new ByteArrayOutputStream();
        shellErrorOutput = new ByteArrayOutputStream();
        err = new PrintWriter(new OutputStreamWriter(shellErrorOutput));
        
        repl = new RascalInterpreterREPL(null, shellStandardOutput, false, false, false, null) {
            @Override
            protected Evaluator constructEvaluator(Writer stdout, Writer stderr) {
                return ShellEvaluatorFactory.getDefaultEvaluator(new PrintWriter(stdout), new PrintWriter(stderr));
            }
        };
	    
	    repl.initialize(new OutputStreamWriter(shellStandardOutput, "utf8"), new OutputStreamWriter(shellErrorOutput, "utf8"));
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
		shellStandardOutput.reset();
		err.flush();
		shellErrorOutput.reset();
	}
	
	void reset(){
	    repl.cleanEnvironment();
	}
	
	String getPrompt() {
	    return repl.getPrompt();
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
	        return vf.string("");
	    }
	}
	
	String evalPrint(String line) throws IOException, RascalShellExecutionException{
	    return ((IString) eval(line)).getValue();
	}
	
	String getMessages() throws UnsupportedEncodingException{
		flush();
		String errors = shellErrorOutput.toString("utf8");
		String output = shellStandardOutput.toString("utf8");
		
		if (!errors.trim().isEmpty()) {
		    return errors + "\n" + output;
		}
		else {
		    return output;
		}
	}
	
	void error(String msg){
		err.println(msg);
	}
	
	void log(Throwable e) {
	    e.printStackTrace(err);
	}
	
	boolean isStatementComplete(String line){
		return repl.isStatementComplete(line);
	}
}
