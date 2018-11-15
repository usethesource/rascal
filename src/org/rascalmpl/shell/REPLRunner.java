package org.rascalmpl.shell;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;

import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.ILanguageProtocol;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.uri.URIUtil;

import jline.Terminal;

public class REPLRunner extends BaseREPL  implements ShellRunner {
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

  public REPLRunner(InputStream stdin, OutputStream stdout, Terminal term)  throws IOException, URISyntaxException{
    super(makeInterpreter(stdin, stdout, true, term.isAnsiSupported(), false, getHistoryFile(), term), null, stdin, stdout, true, term.isAnsiSupported(), getHistoryFile(), term, null);
  }
  
  public REPLRunner(ILanguageProtocol language)  throws IOException, URISyntaxException{
      super(language, null, null, null, true, true, new File(""), null, null);
  }

  private static ILanguageProtocol makeInterpreter(InputStream stdin, OutputStream stdout, boolean prettyPrompt, boolean allowColors, boolean htmlOutput, File persistentHistory, Terminal terminal) throws IOException, URISyntaxException {
    RascalInterpreterREPL repl = new RascalInterpreterREPL(stdin, stdout, prettyPrompt, allowColors, htmlOutput, getHistoryFile()) {
        private OnePageServer server;
        
        @Override
        protected Evaluator constructEvaluator(Writer stdout, Writer stderr) {
            try {
                server = OnePageServer.getInstance();
                
            }
            catch (IOException e ) {
                // no problem
            }
                    
            return ShellEvaluatorFactory.getDefaultEvaluator(new PrintWriter(stdout), new PrintWriter(stderr));
        }

        @Override
        public void handleInput(String line, Map<String, String> output, Map<String, String> metadata)
            throws InterruptedException {
            super.handleInput(line, output, metadata);
            
            if (Desktop.isDesktopSupported()) {
                String html = output.get("text/html");
                
                if (server != null && html != null) {
                    try {
                        server.setHTML(html);
                        Desktop.getDesktop().browse(URIUtil.assumeCorrect("http", "localhost:" + server.getListeningPort(), "/"));
                    }
                    catch (IOException e) {
                        getErrorWriter().println("failed to display HTML content: " + e.getMessage());
                    }
                }
            }
        }
    };
    
    repl.setMeasureCommandTime(false);
    
    return repl;
  }

  @Override
  public void run(String[] args) throws IOException {
    // there are no args for now
    run();
  }

}