package org.rascalmpl.repl.output.impl;

import org.rascalmpl.repl.output.ICommandOutput;
import org.rascalmpl.repl.output.IErrorCommandOutput;
import org.rascalmpl.repl.output.IOutputPrinter;

/**
 * An IErrorCommandOutput implementation that takes IOutputPrinter(s) as constructor parameters.
 * If the single-argument constructor is used, the printer will be used for both plain and error printing.
 */

public class PrinterErrorCommandOutput implements IErrorCommandOutput {
    private IOutputPrinter plainPrinter;
    private IOutputPrinter errorPrinter;


    public static IErrorCommandOutput errorMessage(String message) {
        return new PrinterErrorCommandOutput(new AsciiStringOutputPrinter(message));
    }

    public PrinterErrorCommandOutput(IOutputPrinter printer) {
        this(printer, printer);
    }

    public PrinterErrorCommandOutput(IOutputPrinter errorPrinter, IOutputPrinter plainPrinter) {
        this.errorPrinter = errorPrinter;
        this.plainPrinter = plainPrinter;
    }

    @Override
    public IOutputPrinter asPlain() {
        return plainPrinter;
    }

    @Override
    public ICommandOutput getError() {
        return () -> errorPrinter;
    }
}
