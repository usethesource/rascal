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

	public TutorCommandExecutor(PathConfig pcfg) throws IOException, NoSuchRascalFunction, URISyntaxException{
        shellStandardOutput = new ByteArrayOutputStream();
        shellErrorOutput = new ByteArrayOutputStream();
        
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
	
	private void flushOutput(){
	    repl.getOutputWriter().flush();
	}
	
	private void flushErrors(){
	    repl.getErrorWriter().flush();
    }
	
	private void resetOutput(){
		shellStandardOutput.reset();
	}
	
	private void resetErrors(){
        shellErrorOutput.reset();
    }
	
	void reset() {
	    repl.cleanEnvironment();
	    resetOutput();
	    resetErrors();
	}
	
	String getPrompt() {
	    return repl.getPrompt();
	}
	
	IValue eval(String line) {
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
	    catch (Throwable e) {
	        return vf.string(e.getMessage());
	    }
	}
	
	public String evalPrint(String line) throws IOException {
	    return ((IString) eval(line)).getValue();
	}
	

    public boolean isStatementComplete(String line){
        return repl.isStatementComplete(line);
    }
	
	public String getPrintedOutput() throws UnsupportedEncodingException{
	    try {
	        flushOutput();
	        String result = shellStandardOutput.toString("utf8");
	        resetOutput();
	        return result;
	    }
	    catch (UnsupportedEncodingException e) {
	        return "";
	    }
	}
	
	public String getErrorOutput() {
	    try {
	        flushErrors();
	        String result = shellErrorOutput.toString("utf8");
	        resetErrors();
	        return result;
	    }
        catch (UnsupportedEncodingException e) {
            return "";
        }
	}
	
}
