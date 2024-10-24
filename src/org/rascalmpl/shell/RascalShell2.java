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
import java.io.PrintWriter;

import org.jline.terminal.TerminalBuilder;
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
            var term = TerminalBuilder.builder()
                .color(true)
                .build();


            var repl = new BaseREPL2(new RascalReplServices((t) -> {
                var monitor = new TerminalProgressBarMonitor(term);
                IDEServices services = new BasicIDEServices(term.writer(), monitor);


                GlobalEnvironment heap = new GlobalEnvironment();
                ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
                IValueFactory vf = ValueFactoryFactory.getValueFactory();
                Evaluator evaluator = new Evaluator(vf, term.reader(), new PrintWriter(System.err, true), term.writer(), root, heap, monitor);
                evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());
                return evaluator;
            }), term);
            repl.run();
            /*


            //IRascalMonitor monitor = IRascalMonitor.buildConsoleMonitor(System.in, System.out, true);
            var monitor = new TerminalProgressBarMonitor(term);

            // var monitor = new NullRascalMonitor() {
            //     @Override
            //     public void warning(String message, ISourceLocation src) {
            //         reader.printAbove("[WARN] " + message);
            //     }
            // };

            IDEServices services = new BasicIDEServices(term.writer(), monitor);


            GlobalEnvironment heap = new GlobalEnvironment();
            ModuleEnvironment root = heap.addModule(new ModuleEnvironment(ModuleEnvironment.SHELL_MODULE, heap));
            IValueFactory vf = ValueFactoryFactory.getValueFactory();
            Evaluator evaluator = new Evaluator(vf, term.reader(), new PrintWriter(System.err, true), term.writer(), root, heap, monitor);
            evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());

            URIResolverRegistry reg = URIResolverRegistry.getInstance();

            var indentedPrettyPrinter = new ReplTextWriter(true);

            while (true) {
                String line = reader.readLine(Ansi.ansi().reset().bold().toString() + "rascal> " + Ansi.ansi().boldOff().toString());
                try {

                    Result<IValue> result;

                    synchronized(evaluator) {
                        result = evaluator.eval(monitor, line, URIUtil.rootLocation("prompt"));
                        evaluator.endAllJobs();
                    }

                    if (result.isVoid()) {
                        monitor.println("ok");
                    }
                    else {
                        IValue value = result.getValue();
                        Type type = result.getStaticType();
                        
                        if (type.isAbstractData() && type.isStrictSubtypeOf(RascalValueFactory.Tree) && !type.isBottom()) {
                            monitor.write("(" + type.toString() +") `");
                            TreeAdapter.yield((IConstructor)value, true, monitor);
                            monitor.write("`");
                        }
                        else {
                            indentedPrettyPrinter.write(value, monitor);
                        }
                        monitor.println();
                    }
                }
                catch (InterruptException ie) {
                    reader.printAbove("Interrupted");
                    try {
                        ie.getRascalStackTrace().prettyPrintedString(evaluator.getStdErr(), indentedPrettyPrinter);
                    }
                    catch (IOException e) {
                    }
                }
                catch (ParseError pe) {
                    parseErrorMessage(evaluator.getStdErr(), line, "prompt", pe, indentedPrettyPrinter);
                }
                catch (StaticError e) {
                    staticErrorMessage(evaluator.getStdErr(),e, indentedPrettyPrinter);
                }
                catch (Throw e) {
                    throwMessage(evaluator.getStdErr(),e, indentedPrettyPrinter);
                }
                catch (QuitException q) {
                    reader.printAbove("Quiting REPL");
                    break;
                }
                catch (Throwable e) {
                    throwableMessage(evaluator.getStdErr(), e, evaluator.getStackTrace(), indentedPrettyPrinter);
                }
            }
            System.exit(0);
        }
        catch (Throwable e) {
            System.err.println("\n\nunexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        finally {
            System.out.flush();
            System.err.flush();
        }
        */
    }




}
