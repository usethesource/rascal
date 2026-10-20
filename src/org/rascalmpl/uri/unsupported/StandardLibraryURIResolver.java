package org.rascalmpl.uri.unsupported;

/**
 * Used to provide transparant access to the source code of the one and only standard library,
 * but is now defunct. One should use a direct URI, like `mvn://`, `project://`, `jar+file://`, etc.
 */
public class StandardLibraryURIResolver extends UnsupportedURIResolver {

    public StandardLibraryURIResolver() {
        super("std", "The std scheme has been removed; please rewrite to mvn scheme, jar+file or similar.");
    }

}
