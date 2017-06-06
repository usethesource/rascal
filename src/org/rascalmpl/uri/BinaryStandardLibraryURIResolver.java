package org.rascalmpl.uri;

import org.rascalmpl.uri.libraries.ClassResourceInput;

public class BinaryStandardLibraryURIResolver extends ClassResourceInput {

    public BinaryStandardLibraryURIResolver() {
        super("stdlib", BinaryStandardLibraryURIResolver.class, "/std");
    }

}
