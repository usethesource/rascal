package org.rascalmpl.repl;

import java.util.Locale;

import org.fusesource.jansi.internal.CLibrary;
import org.fusesource.jansi.internal.Kernel32;

public class IsTTY {

    /**
     * Until we switch to jline 3 we will need this
     * @return
     */
    public static boolean isTTY() {
        // TODO definitely does not work for CYGWIN
        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win")) {
            long console = Kernel32.GetStdHandle(Kernel32.STD_OUTPUT_HANDLE);
            return Kernel32.GetConsoleMode(console, new int[1]) != 0;
        } 
        else {
            int fd = CLibrary.STDOUT_FILENO;
            return CLibrary.isatty(fd) != 0;
        }
    }
}
