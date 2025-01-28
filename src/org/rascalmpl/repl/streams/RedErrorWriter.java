package org.rascalmpl.repl.streams;

import java.io.Writer;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Attribute;
import org.jline.jansi.Ansi.Color;


public class RedErrorWriter extends WrappedFilterWriter {
    public RedErrorWriter(Writer out) {
        super(out, Ansi.ansi().fg(Color.RED).toString().toCharArray(), Ansi.ansi().a(Attribute.RESET).toString().toCharArray());
    }
}
