package org.rascalmpl.repl.streams;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;


abstract class NonClosingFilterWriter extends FilterWriter {

    protected NonClosingFilterWriter(Writer out) {
        super(out);
    }

    @Override
    public void close() throws IOException {
    }


}
