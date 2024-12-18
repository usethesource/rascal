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
import java.nio.charset.StandardCharsets;

import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.OSUtils;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.load.StandardLibraryContributor;
import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.repl.BaseREPL2;
import org.rascalmpl.repl.RascalReplServices;
import org.rascalmpl.repl.TerminalProgressBarMonitor;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IValueFactory;


public class RascalShell2  {

    private static void printVersionNumber(){
        System.err.println("Version: " + RascalManifest.getRascalVersionNumber());
    }
    
    public static void main(String[] args) throws IOException {
        System.setProperty("apple.awt.UIElement", "true"); // turns off the annoying desktop icon
        printVersionNumber();

        //try {
            var termBuilder = TerminalBuilder.builder();
            if (OSUtils.IS_WINDOWS) {
                termBuilder.encoding(StandardCharsets.UTF_8);
            }
            termBuilder.dumb(true); // fallback to dumb terminal if detected terminal is not supported
            var term = termBuilder.build();


            var repl = new BaseREPL2(new RascalReplServices((t) -> {
                var monitor = new TerminalProgressBarMonitor(term);
                IDEServices services = new BasicIDEServices(term.writer(), monitor, () -> term.puts(Capability.clear_screen));

                GlobalEnvironment heap = new GlobalEnvironment();
                ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
                IValueFactory vf = ValueFactoryFactory.getValueFactory();
                Evaluator evaluator = new Evaluator(vf, term.reader(), RascalReplServices.generateErrorStream(t, monitor), monitor, root, heap, services);
                evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
                return evaluator;
            }), term);
            repl.run();
    }
}
