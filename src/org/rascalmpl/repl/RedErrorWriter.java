package org.rascalmpl.repl;

import java.io.Writer;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;

public class RedErrorWriter extends WrappedFilterWriter {
    public RedErrorWriter(Writer out) {
        super(out, Ansi.ansi().fg(Color.RED).toString().toCharArray(), Ansi.ansi().a(Attribute.RESET).toString().toCharArray());
    }
}
