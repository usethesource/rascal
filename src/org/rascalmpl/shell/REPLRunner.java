package org.rascalmpl.shell;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jline.terminal.Terminal;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.output.IWebContentOutput;
import org.rascalmpl.repl.rascal.RascalInterpreterREPL;
import org.rascalmpl.repl.rascal.RascalReplServices;

public class REPLRunner implements ShellRunner {

    private final Terminal term;
    
    public REPLRunner(Terminal term) {
        this.term = term;
    }

    @Override
    public void run(String[] args) throws IOException {
        var repl = new BaseREPL(new RascalReplServices(new RascalInterpreterREPL() {
            @Override
            protected Evaluator buildEvaluator(Reader input, PrintWriter stdout, PrintWriter stderr,
                IDEServices services) {
                    return ShellEvaluatorFactory.getDefaultEvaluator(input, stdout, stderr, services);
            }

            @Override
            protected void openWebContent(IWebContentOutput webContent) {
                try {
                    // Note that Desktop.isDesktopSupported can not be factored into a class constant because
                    // it may throw exceptions on headless machines which are ignored below.
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(webContent.webUri());
                        }
                        catch (IOException e) {
                            eval.getStdErr().println("failed to display content: " + e.getMessage());
                        }
                    }
                }
                catch (Throwable e) {
                    // we fail silently in order to support headless machines
                }

            }
        }, getHistoryFile()), term);
        repl.run();
    }

    private static Path getHistoryFile() throws IOException {
        var home = FileSystems.getDefault().getPath(System.getProperty("user.home"));
        var rascalDir = home.resolve(".rascal");
        if (!Files.isDirectory(rascalDir)) {
            Files.createDirectories(rascalDir);
        }
        return rascalDir.resolve(".repl-history-rascal-terminal-v2");
    }

    
}
