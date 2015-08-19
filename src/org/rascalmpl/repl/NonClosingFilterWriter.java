package org.rascalmpl.repl;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;


public abstract class NonClosingFilterWriter extends FilterWriter {

  protected NonClosingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void close() throws IOException {
  }
  

}
