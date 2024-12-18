package org.rascalmpl.repl.output;

public interface IAnsiCommandOutput extends ICommandOutput {
    IOutputPrinter asAnsi();
}
