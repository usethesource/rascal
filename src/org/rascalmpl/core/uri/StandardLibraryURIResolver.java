package org.rascalmpl.core.uri;

import org.rascalmpl.core.uri.libraries.ClassResourceInput;

public class StandardLibraryURIResolver extends ClassResourceInput {

    public StandardLibraryURIResolver() {
        super("std", StandardLibraryURIResolver.class, "/org/rascalmpl/library");
    }

}
