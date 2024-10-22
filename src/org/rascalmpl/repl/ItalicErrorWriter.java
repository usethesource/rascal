package org.rascalmpl.repl;

import java.io.Writer;

import org.jline.jansi.Ansi;
import org.jline.jansi.Ansi.Attribute;


public class ItalicErrorWriter extends WrappedFilterWriter {
    public ItalicErrorWriter(Writer out) {
        super(out, Ansi.ansi().a(Attribute.ITALIC).toString().toCharArray(), Ansi.ansi().a(Attribute.ITALIC_OFF).toString().toCharArray());
    }
}
