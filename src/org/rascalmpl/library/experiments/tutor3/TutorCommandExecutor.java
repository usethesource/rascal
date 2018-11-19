package org.rascalmpl.library.experiments.tutor3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.Prelude;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.shell.ShellEvaluatorFactory;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

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
                Evaluator eval = ShellEvaluatorFactory.getDefaultEvaluator(new PrintWriter(stdout), new PrintWriter(stderr));
                eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
                return eval;
            }
        };
	    
	    repl.initialize(new OutputStreamWriter(shellStandardOutput, "utf8"), new OutputStreamWriter(shellErrorOutput, "utf8"));
	    repl.setMeasureCommandTime(false); 
	}
	
	private String javaCompilerPathAsString(IList javaCompilerPath) {
        StringBuilder b = new StringBuilder();
        
        for (IValue elem : javaCompilerPath) {
            ISourceLocation loc = (ISourceLocation) elem;
            
            if (b.length() != 0) {
                b.append(File.pathSeparatorChar);
            }
           
            assert loc.getScheme().equals("file");
            String path = loc.getPath();
            if (path.startsWith("/") && path.contains(":\\")) {
                // a windows path should drop the leading /
                path = path.substring(1);
            }
            b.append(path);
        }
        
        return b.toString();
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
	
	String eval(String line, String conceptFolder) {
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
	                + Prelude.consumeInputStream(new InputStreamReader(out, StandardCharsets.UTF_8)) 
	                + "\n++++\n";
	        }
	        
	        for (String mimeType : output.keySet()) {
	            if (mimeType.equals("text/html") || mimeType.equals("text/plain")) {
	                continue;
	            }
	            
	            InputStream content = output.get(mimeType);
	            
                if (mimeType.startsWith("image/")) {
	                String imageFile = dumpMimetypeFile(conceptFolder, content, mimeType);
	                
	                return "++++\n" 
	                     + "<img src=\"" + imageFile + "\">"
	                     + "++++\n";
	            }
	            else if (mimeType.startsWith("audio/")) {
	                String audioFile = dumpMimetypeFile(conceptFolder, content, mimeType);
	                
	                return "++++\n" 
                    + "<audio controls>\n"
                    + "<source src=\""+ audioFile + "\" type=\""+ mimeType + "\">"
                    + "Your browser does not support the audio element.\n"
                    + "</audio>\n"
                    + "++++\n";
	            }
	            else if (mimeType.startsWith("video/")) {
                    String videoFile = dumpMimetypeFile(conceptFolder, content, mimeType);
                    
                    return "++++\n" 
                    + "<video width=\"100%\" height=250 controls>\n"
                    + "<source src=\""+ videoFile + "\" type=\""+ mimeType + "\">"
                    + "Your browser does not support the video element.\n"
                    + "</audio>\n"
                    + "++++\n";
                }
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

    private String dumpMimetypeFile(String imagePath, InputStream input, String mimeType)
        throws IOException, FileNotFoundException {
        String imageFile = imagePath + "/" + UUID.randomUUID() + "." + mimeType.split("/")[1];
        try (OutputStream file = new FileOutputStream(imageFile)) {
            byte[] buf = new byte[512];
            int read = -1;
            while ((read = input.read(buf, 0, 512)) != -1) {
                file.write(buf, 0, read);
            }
        }
        return imageFile;
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
