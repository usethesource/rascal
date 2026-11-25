package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.repl.streams.StreamUtil;

public class RascalGenerateSources extends AbstractCommandlineTool {
    public static void main(String[] args) throws IOException {
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
		var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
		var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);

        System.exit(main("RascalGenerateSources", new String[] { "org/rascalmpl/compiler", "org/rascalmpl/typepal" }, args, term, monitor, err, out));
    }
}
