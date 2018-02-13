package org.rascalmpl.core.uri;

import org.rascalmpl.core.uri.libraries.ClassResourceInput;

public class BinaryStandardLibraryURIResolver extends ClassResourceInput {

    public BinaryStandardLibraryURIResolver() {
        super("stdlib", BinaryStandardLibraryURIResolver.class, "/std");
    }

}
