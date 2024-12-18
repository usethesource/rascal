package org.rascalmpl.repl.output;

import java.net.URI;

public interface IWebContentOutput extends ICommandOutput {
    URI webUri();
    String webTitle();
    int webviewColumn();
}
