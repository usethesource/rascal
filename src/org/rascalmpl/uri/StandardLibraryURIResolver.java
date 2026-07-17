package org.rascalmpl.uri;

import java.io.IOException;

import io.usethesource.vallang.ISourceLocation;

/**
 * Used to provide transparant access to the source code of the one and only standard library,
 * but is now defunct. One should use a direct URI, like `mvn://`, `project://`, `jar+file://`, etc.
 */
public class StandardLibraryURIResolver implements ILogicalSourceLocationResolver {

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        throw new IOException("std:/// scheme does not exist anymore; use a physical URI instead");
    }

    @Override
    public String scheme() {
        return "std";
    }

    @Override
    public String authority() {
        return "";
    }

}
