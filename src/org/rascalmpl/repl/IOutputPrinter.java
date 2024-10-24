package org.rascalmpl.repl;

import java.io.PrintWriter;
import java.io.Reader;

public interface IOutputPrinter {
    void write(PrintWriter target);
    Reader asReader();
}
