package org.rascalmpl.repl.output;

public interface INotebookOutput extends IHtmlCommandOutput {
    IOutputPrinter asNotebook();
}
