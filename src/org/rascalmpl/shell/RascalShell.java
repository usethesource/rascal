/*
Copyright (c) 2024, Swat.engineering
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

package org.rascalmpl.shell;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.OSUtils;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.repl.streams.StreamUtil;


public class RascalShell  {

    public static void main(String[] args) throws IOException {
        checkIfHelp(args);

        var term = connectToTerminal();

        ShellRunner runner; 
        if (args.length > 0) {
            var monitor = IRascalMonitor.buildConsoleMonitor(term);
            var err = (monitor instanceof Writer) ?  StreamUtil.generateErrorStream(term, (Writer)monitor) : new PrintWriter(System.err, true);
            var out = (monitor instanceof PrintWriter) ? (PrintWriter) monitor : new PrintWriter(System.out, false);

            runner = new ModuleRunner(term.reader(), out, err, monitor);
        } 
        else {
            runner = new REPLRunner(term);
        }
        runner.run(args);
    }

    public static Terminal connectToTerminal() throws IOException {
        setupJavaProcessForREPL();
      
        var termBuilder = TerminalBuilder.builder();
        if (OSUtils.IS_WINDOWS) {
            termBuilder.encoding(StandardCharsets.UTF_8);
        }
        termBuilder.dumb(true); // fallback to dumb terminal if detected terminal is not supported
        var term = termBuilder.build();
        return term;
    }

    private static void checkIfHelp(String[] args) {
        if (args.length > 0 && "--help".equals(args[0])) {
            System.err.println("Usage: java -jar rascal-version.jar [Module]");
            System.err.println("\ttry also the --help options of the respective commands.");
            System.err.println("\tjava -jar rascal-version.jar [Module]: runs the main function of the module using the interpreter");
            System.exit(0);
        }
    }

    public static void setupJavaProcessForREPL() {
        // configure jline3 to avoid reflective access warnings printed by jdk
        System.setProperty("org.jline.terminal.exec.redirectPipeCreationMode", "native");
        // avoid getting a separate icon in OSX
        System.setProperty("apple.awt.UIElement", "true");
    }

}
