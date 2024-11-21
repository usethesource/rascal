package org.rascalmpl.shell;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Map;

import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.ILanguageProtocol;
import org.rascalmpl.repl.RascalInterpreterREPL;
import org.rascalmpl.uri.URIUtil;

import jline.Terminal;

public class REPLRunner extends BaseREPL implements ShellRunner {
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

    public REPLRunner(InputStream stdin, OutputStream stderr, OutputStream stdout, Terminal term, IDEServices services)
        throws IOException, URISyntaxException {
        super(makeInterpreter(stdin, stderr, stdout, true, term.isAnsiSupported(), getHistoryFile(), term), null,
            stdin, stderr, stdout, true, term.isAnsiSupported(), getHistoryFile(), term, services);
    }

    public REPLRunner(ILanguageProtocol language, IDEServices services) throws IOException, URISyntaxException {
        super(language, null, null, null, null, true, true, new File(""), null, services);
    }

    private static ILanguageProtocol makeInterpreter(InputStream stdin, OutputStream stderr, OutputStream stdout,
        boolean prettyPrompt, boolean allowColors, File persistentHistory, Terminal terminal)
        throws IOException, URISyntaxException {
        RascalInterpreterREPL repl =
            new RascalInterpreterREPL(prettyPrompt, allowColors, getHistoryFile()) {
                @Override
                protected Evaluator constructEvaluator(InputStream input, OutputStream stdout, OutputStream stderr, IDEServices services) {
                    return ShellEvaluatorFactory.getDefaultEvaluator(input, stdout, stderr, services);
                }

                @Override
                public void handleInput(String line, Map<String, InputStream> output, Map<String, String> metadata)
                    throws InterruptedException {
                    super.handleInput(line, output, metadata);

                    try {
                        // Note that Desktop.isDesktopSupported can not be factored into a class constant because
                        // it may throw exceptions on headless machines which are ignored below.
                        if (Desktop.isDesktopSupported()) {
                            for (String mimetype : output.keySet()) {
                                if (!mimetype.contains("html") && !mimetype.startsWith("image/")) {
                                    continue;
                                }

                                try {
                                    Desktop.getDesktop().browse(URIUtil.assumeCorrect(metadata.get("url")));
                                }
                                catch (IOException e) {
                                    getErrorWriter().println("failed to display content: " + e.getMessage());
                                }
                            }
                        }
                    }
                    catch (Throwable e) {
                        // we fail silently in order to support headless machines
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