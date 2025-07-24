package org.rascalmpl.shell;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jline.terminal.Terminal;
import org.rascalmpl.repl.BaseREPL;
import org.rascalmpl.repl.rascal.RascalInterpreterREPL;
import org.rascalmpl.repl.rascal.RascalReplServices;

public class REPLRunner implements ShellRunner {

    private final Terminal term;
    
    public REPLRunner(Terminal term) {
        this.term = term;
    }

    @Override
    public void run(String[] args) throws IOException {
        var repl = new BaseREPL(new RascalReplServices(new RascalInterpreterREPL(), getHistoryFile()), term);
        repl.run();
    }

    public static Path getHistoryFile() throws IOException {
        var home = FileSystems.getDefault().getPath(System.getProperty("user.home"));
        var rascalDir = home.resolve(".rascal");
        if (!Files.isDirectory(rascalDir)) {
            Files.createDirectories(rascalDir);
        }
        return rascalDir.resolve(".repl-history-rascal-terminal.jline3");
    }

    
}
