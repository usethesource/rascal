package org.rascalmpl.shell;

import org.rascalmpl.uri.libraries.ClassResourceInput;

public class ManifestURIResolver extends ClassResourceInput {
    public ManifestURIResolver() {
        super("manifest", ManifestURIResolver.class, "/");
    }    
}
