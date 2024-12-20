package org.rascalmpl.repl.output;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationCommandOutput extends ICommandOutput {
    ISourceLocation asLocation();
    String locationMimeType();
}
