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
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.Manifest;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.library.experiments.Compiler.Commands.Rascal;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalC;
import org.rascalmpl.library.experiments.Compiler.Commands.RascalTests;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.NoSuchRascalFunction;
import org.rascalmpl.shell.compiled.CompiledRascalShell;

import jline.Terminal;
import jline.TerminalFactory;
import jline.TerminalSupport;


public class RascalShell  {

    public static final String ECLIPSE_TERMINAL_CONNECTION_REPL_KEY = "__ECLIPSE_CONNECTION";

    private static void printVersionNumber(){
        System.err.println("Version: " + getVersionNumber());
    }
    
    public static String getVersionNumber() {
        try {
            Enumeration<URL> resources = RascalShell.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                String bundleName = manifest.getMainAttributes().getValue("Name");
                if (bundleName != null && bundleName.equals("rascal")) {
                    String result = manifest.getMainAttributes().getValue("Specification-Version");
                    if (result != null) {
                        return result;
                    }
                }
            }
            
            return "not specified in META-INF/MANIFEST.MF";
        } catch (IOException e) {
            return "unknown (due to " + e.getMessage();
        }
    }


    public static void main(String[] args) throws IOException {
        printVersionNumber();
        RascalManifest mf = new RascalManifest();

        try {
            ShellRunner runner; 
            if (mf.hasManifest(RascalShell.class) && mf.hasMainModule(RascalShell.class)) {
                runner = new ManifestRunner(mf, new PrintWriter(System.out), new PrintWriter(System.err, true));
            } 
            else if (args.length > 0) {            	
            	if (args[0].equals("--help")) {
                    System.err.println("Usage: java -jar rascal-version.jar [{--rascalc, --rascal, --rascalTests, --compiledREPL}] [Module]");
                    System.err.println("\ttry also the --help options of the respective commands.");
                    System.err.println("\tjava -jar rascal-version.jar [Module]: runs the main function of the module using the interpreter");
                    return;
                }
            	else if (args[0].equals("--rascalc")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) {
                            RascalC.main(Arrays.copyOfRange(args, 1, args.length));
                        }
                    };
                }
                else if (args[0].equals("--rascal")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) throws IOException {
                            Rascal.main(Arrays.copyOfRange(args, 1, args.length));
                        }
                    };
                }
                else if (args[0].equals("--rascalTests")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) throws IOException {
                            try {
                                RascalTests.main(Arrays.copyOfRange(args, 1, args.length));
                            } catch (NoSuchRascalFunction | URISyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
                }
                else if (args[0].equals("--compiledREPL")) {
                    runner = new ShellRunner() {
                        @Override
                        public void run(String[] args) throws IOException {
                            CompiledRascalShell.main(Arrays.copyOfRange(args, 1, args.length));
                        }
                    };
                    
                }
                else {
                    runner = new ModuleRunner(new PrintWriter(System.out), new PrintWriter(System.err, true));
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
                runner = new REPLRunner(System.in, System.out, term);
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
