package org.rascalmpl.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import org.rascalmpl.library.lang.rascal.tutor.repl.TutorCommandExecutor;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.library.util.PathConfig.RascalConfigMode;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

/**
 * EXPERIMENTAL TOOL FOR USE IN DOCUSAURUS
 * 
 * This translates a markdown code block by executing the Rascal
 * commands within it and capturing the output. The input is read
 * from stdin and the output (Markdown/HTML
 * code to be included in a documentation page) is written to stdout.
 */
public class DocRunner {
    // this executes Rascal code and collects outputs
    private final TutorCommandExecutor repl;
    // this is where generate images are supposed to end up in
    private final String folder;

    public DocRunner(ISourceLocation cwd, String folder) throws IOException, URISyntaxException {
        this.repl = new TutorCommandExecutor(PathConfig.fromSourceProjectRascalManifest(cwd, RascalConfigMode.INTERPETER));
        this.folder = folder;
    }

    public static void main(String[] args) {
        try {
            ISourceLocation cwd = args.length > 0 ? URIUtil.createFileLocation(args[0]) : URIUtil.rootLocation("cwd");
            String folder = args.length > 1 ? args[1] : "./";
            System.exit(new DocRunner(cwd, folder).execute() ? 0 : 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public boolean execute() throws IOException {
		OutputStreamWriter preprocessOut = new OutputStreamWriter(System.out);
        
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line = reader.readLine();
            return executeRascalShellScript(repl, reader, preprocessOut, line);
        }
        finally {
            preprocessOut.flush();
        }
	}

    private boolean executeRascalShellScript(TutorCommandExecutor repl, BufferedReader reader, OutputStreamWriter preprocessOut, String line) throws IOException {
        boolean isContinue = line.contains("continue");
        boolean mayHaveErrors = line.contains("error");
        boolean hasErrors = false;
        
        if (line.startsWith("[")){
            line = reader.readLine();   // skip ----
            if(line == null) {
                // missing ---- ? what to do?
                return true;
            }
        }
        
        if(!isContinue){
            repl.reset();
        }

        startREPL(preprocessOut, mayHaveErrors);
        boolean printWarning = false;
        
        OUTER:while ((line = reader.readLine()) != null ) {
            if (line.equals("```") || line.equals("----")){
                break;
            }
            
            if (line.trim().startsWith("//")) {
                endREPL(preprocessOut);
                preprocessOut.append(line.trim().substring(2).trim() + "\n");
                while ((line = reader.readLine()) != null && line.trim().startsWith("//")) {
                    preprocessOut.append(line.trim().substring(2).trim() + "\n");
                    if (line.equals("```") || line.equals("----")) {
                        break OUTER;
                    }
                }
                startREPL(preprocessOut, mayHaveErrors);
            }
            
            preprocessOut.append(repl.getPrompt()).append(line).append("\n");
        
            String resultOutput = repl.eval(line, folder);
            String htmlOutput = repl.getHTMLOutput();
            String errorOutput = repl.getErrorOutput();
            String printedOutput = repl.getPrintedOutput();
            
            if (!printedOutput.isEmpty()){
                preprocessOut.append(printedOutput);
            } 
            
            if (!errorOutput.isEmpty()) {
                hasErrors = true;
                if (!mayHaveErrors) {
                    printWarning = true;
                }
                preprocessOut.append(mayHaveErrors ? makeRed(errorOutput) : errorOutput);
            }
            
            if (!htmlOutput.isEmpty()) {
                endREPL(preprocessOut);
                preprocessOut.append("<div class=\"todo-html-output\">");
                preprocessOut.append(htmlOutput);
                preprocessOut.append("</div>");
                startREPL(preprocessOut, mayHaveErrors);
            }
            
            if (!resultOutput.isEmpty()) {
                preprocessOut.append(resultOutput);
            }
        }
        
        endREPL(preprocessOut);
        
        if (printWarning) {
            // note that the trailing space after the second # is important for the ADOC parser.
            preprocessOut.append(makeRed("WARNING: unexpected errors in the above SHELL example. Documentation author please fix!"));
        }

        return mayHaveErrors || !hasErrors;
    }

    private String makeRed(String result) {
        // this is tricky since # syntax is parsed using a line-by-line tokenizer.
        // there are many many many corner cases where this might go wrong.
        // the nbsp is there to normalize these case a bit, such that # is never directly
        // after a \r or \n character to break the asciidoctor parser.
        return "<span class=\"error\">" + result + "</span>";
    }

    private void endREPL(Writer preprocessOut) throws IOException {
        preprocessOut.write("```\n");
    }

    private void startREPL(Writer preprocessOut, boolean mayHaveErrors) throws IOException {
        preprocessOut.write("```rascal-shell\n");
    }
}
