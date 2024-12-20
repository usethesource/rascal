package org.rascalmpl.repl.streams;

import java.io.PrintWriter;
import java.io.Writer;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class StreamUtil {

    public static PrintWriter generateErrorStream(Terminal tm, Writer out) {
        // previously we would alway write errors to System.err, but that tends to mess up terminals
        // and also our own error print
        // so now we try to not write to System.err
        if (supportsColors(tm)) {
            return new PrintWriter(new RedErrorWriter(out), true);
        }
        if (supportsItalic(tm)) {
            return new PrintWriter(new ItalicErrorWriter(out), true);
        }
        return new PrintWriter(System.err, true);
    
    }

    public static boolean supportsColors(Terminal tm) {
        Integer cols = tm.getNumericCapability(Capability.max_colors);
        return cols != null && cols >= 8;
    }

    public static boolean supportsItalic(Terminal tm) {
        String ital = tm.getStringCapability(Capability.enter_italics_mode);
        return ital != null && !ital.equals("");
    }
    
}
