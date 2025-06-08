package org.rascalmpl.shell;

import java.io.PrintWriter;

import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;

public class RascalPackage extends AbstractCommandlineTool {
    public static void main(String[] args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {
        main("lang::rascalcore::package::Packager", new String[] {"org/rascalmpl/compiler"}, args, term, monitor, err, out);
    }
}