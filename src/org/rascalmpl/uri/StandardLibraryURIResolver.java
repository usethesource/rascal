package org.rascalmpl.uri;

import org.rascalmpl.uri.libraries.ClassResourceInput;

public class StandardLibraryURIResolver extends ClassResourceInput {

    public StandardLibraryURIResolver() {
        super("std", StandardLibraryURIResolver.class, "/org/rascalmpl/library");
    }

}
