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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.fusesource.jansi.internal.Kernel32;
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
        setupWindowsCodepage();
        enableWindowsAnsiEscapesIfPossible();
        System.setProperty("apple.awt.UIElement", "true"); // turns off the annoying desktop icon
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
                    var monitor = IRascalMonitor.buildConsoleMonitor(System.in, System.out);
                    runner = new ModuleRunner(System.in, monitor instanceof OutputStream ? (OutputStream) monitor : System.out, System.err, monitor);
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

                IRascalMonitor monitor = IRascalMonitor.buildConsoleMonitor(System.in, System.out, ansiEnabled);

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

    private static boolean IS_WINDOWS = System.getProperty("os.name", "?").toLowerCase().contains("windows");

    private static int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 0x0004;

    public static void enableWindowsAnsiEscapesIfPossible() {
        if (IS_WINDOWS) {
            // Since Windows 10, it's possible to setup ANSI escapes for the terminal process
            // but jline2 doesn't do this itself. Some terminals on windows take care of it, some don't.
            // so we get the handle, and set it up
            long stdOut = Kernel32.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
            if (stdOut == Kernel32.INVALID_HANDLE_VALUE) {
                return; // there is no output stream allocated for this process
            }
            int[] mode = new int[1];
            if (Kernel32.GetConsoleMode(stdOut, mode) == 0) {
                // not a console, but a file/stream output, no need to turn ANSI on.
                return; 
            }
            int newMode = mode[0] | ENABLE_VIRTUAL_TERMINAL_PROCESSING;
            int errno;
            if ((errno = Kernel32.SetConsoleMode(stdOut, newMode)) != 0) {
                // will only fail on older versions of Windows
                System.err.println("Windows console mode could not be enabled, errno=" + errno);
            }; 
        }
    }

    private static final int WINDOWS_UTF8_CODE_PAGE = 65001;
    public static void setupWindowsCodepage() {
        if (IS_WINDOWS) {
            // On windows, we rarely get started as utf8, but lots of places in the repl assume utf8
            // both conhost & terminal on windows support utf8, as long as we setup the correct
            // codepage. so we do that.
            // after that we update the properties and continue as if all is wll.
            try {
                // this enables unicode output, but unicode input is not working, we need jline3 for that
                // as the has both `SetConsoleCP` but more importantly: the `readConsoleInput` function actually deals with unicode chars
                if (Kernel32.SetConsoleOutputCP(WINDOWS_UTF8_CODE_PAGE) == 0) {
                    throw new Exception("SetConsoleOutputCP failed with: " + Kernel32.GetLastError());
                }
                System.setOut(new PrintStream(System.out, false, StandardCharsets.UTF_8));
                System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
                System.setProperty("file.encoding", StandardCharsets.UTF_8.name());
                System.setProperty("input.encoding", StandardCharsets.UTF_8.name());
            } catch (Exception e) {
                System.err.println("Error setting console code point to UTF8: " + e.getMessage());
                System.err.println("Most likely, non-ascii characters will not print correctly, please report this on our github.");
            }
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
