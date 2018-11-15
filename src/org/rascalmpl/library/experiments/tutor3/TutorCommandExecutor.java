package org.rascalmpl.library.experiments.tutor3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;

public class TutorCommandExecutor {
    private final RascalInterpreterREPL repl;
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
	
	String eval(String line) {
	    Map<String, InputStream> output = new HashMap<>();

	    try {
	        repl.handleInput(line, output, new HashMap<>());
	   
	        InputStream out = output.get("text/plain");
	        if (out != null) {
	            return Prelude.consumeInputStream(new InputStreamReader(out));
	        }
	        
	        out = output.get("text/html");
	        if (out != null) {
	            // let the html code pass through the asciidoctor file unscathed:
	            return "\n++++\n" 
	                + Prelude.consumeInputStream(new InputStreamReader(out)) 
	                + "\n++++\n";
	        }
	        
	        return "";
	    }
	    catch (InterruptedException e) {
	        return "[error]#eval was interrupted: " + e.getMessage() + "#";
	    }
	    catch (Throwable e) {
	        return "[error]#" + e.getMessage() + "#";
	    }
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
