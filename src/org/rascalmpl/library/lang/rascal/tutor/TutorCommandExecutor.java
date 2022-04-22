package org.rascalmpl.library.lang.rascal.tutor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.library.Prelude;
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
    private final ByteArrayOutputStream shellHTMLOutput;
    private final ByteArrayInputStream shellInputNotUsed;

    public TutorCommandExecutor(PathConfig pcfg) throws IOException, URISyntaxException{
        shellStandardOutput = new ByteArrayOutputStream();
        shellErrorOutput = new ByteArrayOutputStream();
        shellHTMLOutput = new ByteArrayOutputStream();
        shellInputNotUsed = new ByteArrayInputStream("***this inputstream should not be used***".getBytes());

        repl = new RascalInterpreterREPL(false, false, null) {
            @Override
            protected Evaluator constructEvaluator(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices services) {
                Evaluator eval = ShellEvaluatorFactory.getDefaultEvaluator(input, stdout, stderr);
                eval.getConfiguration().setRascalJavaClassPathProperty(javaCompilerPathAsString(pcfg.getJavaCompilerPath()));
                eval.setMonitor(services);
                return eval;
            }
        };

        repl.initialize(shellInputNotUsed, shellStandardOutput, shellErrorOutput, null);
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
    
    private void resetHTML(){
        shellHTMLOutput.reset();
    }

    private void resetErrors(){
        shellErrorOutput.reset();
    }

    void reset() {
        repl.cleanEnvironment();
        resetOutput();
        resetErrors();
        resetHTML();
    }

    String getPrompt() {
        return repl.getPrompt();
    }

    
    String eval(String line, String conceptFolder) {
        Map<String, InputStream> output = new HashMap<>();
        String result = "";
        
        try {
            repl.handleInput(line, output, new HashMap<>());

            for (String mimeType : output.keySet()) {
                InputStream content = output.get(mimeType);

                if (mimeType.equals("text/plain")) {
                    result = Prelude.consumeInputStream(new InputStreamReader(content));
                }
                else if (mimeType.equals("text/html")) {
                    shellHTMLOutput.write(Prelude.consumeInputStream(new InputStreamReader(content, StandardCharsets.UTF_8)).getBytes());
                }
                else if (mimeType.startsWith("image/")) {
                    String imageFile = dumpMimetypeFile(conceptFolder, content, mimeType);
                    shellHTMLOutput.write(("<img src=\"" + imageFile + "\">").getBytes());
                }
                else if (mimeType.startsWith("audio/")) {
                    String audioFile = dumpMimetypeFile(conceptFolder, content, mimeType);

                    shellHTMLOutput.write(( 
                      "<audio controls>\n"
                    + "<source src=\""+ audioFile + "\" type=\""+ mimeType + "\">"
                    + "Your browser does not support the audio element.\n"
                    + "</audio>\n").getBytes());
                }
                else if (mimeType.startsWith("video/")) {
                    String videoFile = dumpMimetypeFile(conceptFolder, content, mimeType);

                    shellHTMLOutput.write((  
                      "<video width=\"100%\" height=250 controls>\n"
                    + "<source src=\""+ videoFile + "\" type=\""+ mimeType + "\">"
                    + "Your browser does not support the video element.\n"
                    + "</audio>\n").getBytes());
                }
            }

            return result;
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
        File imageFile = new File(imagePath + "/" + UUID.randomUUID() + "." + mimeType.split("/")[1]);
        try (OutputStream file = new FileOutputStream(imageFile)) {
            byte[] buf = new byte[512];
            int read = -1;
            while ((read = input.read(buf, 0, 512)) != -1) {
                file.write(buf, 0, read);
            }
        }
        return imageFile.getName();
    }

    public boolean isStatementComplete(String line){
        return repl.isStatementComplete(line);
    }

    public String getPrintedOutput() throws UnsupportedEncodingException{
        try {
            flushOutput();
            String result = shellStandardOutput.toString(StandardCharsets.UTF_8.name());
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
            String result = shellErrorOutput.toString(StandardCharsets.UTF_8.name());
            resetErrors();
            return result;
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
    }
    
    public String getHTMLOutput() {
        try {
            String result = shellHTMLOutput.toString(StandardCharsets.UTF_8.name());
            resetHTML();
            return result;
        }
        catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
