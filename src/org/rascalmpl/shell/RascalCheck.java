package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.repl.streams.StreamUtil;
import org.rascalmpl.uri.URIUtil;

public class RascalCheck {
    private static final String MODULENAME = "lang::rascalcore::check::Checker";

    public static void main(String[] args) {
        try {
            var term = RascalShell.connectToTerminal();
            var monitor = IRascalMonitor.buildConsoleMonitor(term);
            var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
            var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);

            var eval = ShellEvaluatorFactory.getDefaultEvaluator(term.reader(), out, err, monitor);
            var rascalJar = PathConfig.resolveCurrentRascalRuntimeJar();

            eval.addRascalSearchPath(URIUtil.getChildLocation(rascalJar, "org/rascalmpl/compiler"));
            eval.addRascalSearchPath(URIUtil.getChildLocation(rascalJar, "org/rascalmpl/library"));

            eval.doImport(monitor, MODULENAME);
            
            eval.main(null, MODULENAME, "main", args);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}