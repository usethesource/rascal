/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.shell;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.ideservices.BasicIDEServices;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.utils.RascalManifest;
import jline.Terminal;
import jline.TerminalFactory;
import jline.TerminalSupport;


public class RascalShell  {

    public static final String ECLIPSE_TERMINAL_CONNECTION_REPL_KEY = "__ECLIPSE_CONNECTION";

    private static void printVersionNumber(){
        System.err.println("Version: " + RascalManifest.getRascalVersionNumber());
    }
    
    public static void main(String[] args) throws IOException {
        printVersionNumber();

        try {
            ShellRunner runner; 
            if (args.length > 0) {            	
            	if (args[0].equals("--help")) {
                    System.err.println("Usage: java -jar rascal-version.jar [Module]");
                    System.err.println("\ttry also the --help options of the respective commands.");
                    System.err.println("\tjava -jar rascal-version.jar [Module]: runs the main function of the module using the interpreter");
                    return;
                }
                else {
                    runner = new ModuleRunner(System.in, System.out, System.err);
                }
            } 
            else {
                Terminal term = TerminalFactory.get();
                String sneakyRepl = System.getProperty(ECLIPSE_TERMINAL_CONNECTION_REPL_KEY);
                if (sneakyRepl != null) {
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        // we are inside TM terminal in Windows, we need a special jline terminal that
                        // doesn't try to convert TTY/vt100 stuff, as TM Terminal is already doing this.
                        // having them both try to emulate windows and linux at the same time is causing a whole
                        // bunch of problems
                        term = new TMSimpleTerminal();
                    }
                    term = new EclipseTerminalConnection(term, Integer.parseInt(sneakyRepl));
                }

                IRascalMonitor monitor = IRascalMonitor.buildConsoleMonitor(System.in, System.out);

                IDEServices services = new BasicIDEServices(new PrintWriter(System.err), monitor);
                runner = new REPLRunner(System.in, System.err, monitor instanceof OutputStream ? (OutputStream) monitor : System.out, term, services);
            }
            runner.run(args);

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
    }
    private static final class TMSimpleTerminal extends TerminalSupport {
    	public TMSimpleTerminal() {
    		super(true);
    		setAnsiSupported(true);
		}
    	@Override
    	public void restore() throws Exception {
    		super.restore();
    		System.out.println(); // this is what unix terminal does after a restore
    	}
    }
}
