package org.rascalmpl.uri.unsupported;

public class LibraryURIResolver extends UnsupportedURIResolver {
    public LibraryURIResolver() {
        super("lib", "The lib scheme has been removed, please rewrite to mvn scheme or use getResource");
    }
}
