package org.rascalmpl.shell;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.repl.streams.StreamUtil;

public class RascalTutorCompile extends AbstractCommandlineTool {
    public static void main(String[] args) throws IOException {     
        RascalShell.setupJavaProcessForREPL();
        
        var term = RascalShell.connectToTerminal();
        var monitor = IRascalMonitor.buildConsoleMonitor(term);
        var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
        var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);
        
        System.exit(main("lang::rascal::tutor::Compiler", new String[] {"org/rascalmpl/tutor"}, args, term, monitor, err, out));
    }
}