package org.rascalmpl.shell;

import java.io.PrintWriter;
import org.jline.terminal.Terminal;
import org.rascalmpl.debug.IRascalMonitor;

public class RascalTutorCompile extends AbstractCommandlineTool {
    public static void main(String[] args, Terminal term, IRascalMonitor monitor, PrintWriter err, PrintWriter out) {           
        System.exit(main("lang::rascal::tutor::Compiler", new String[] {"org/rascalmpl/tutor"}, args, term, monitor, err, out));
    }
}