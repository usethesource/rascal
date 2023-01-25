package org.rascalmpl.uri.file;

import java.io.IOException;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;

public abstract class AliasedFileResolver implements ILogicalSourceLocationResolver {

    private final String scheme;

    AliasedFileResolver(String scheme) {
        this.scheme = scheme;
    }

    abstract ISourceLocation getRoot();

    @Override
    public ISourceLocation resolve(ISourceLocation input) throws IOException {
        return URIUtil.getChildLocation(getRoot(), input.getPath());
    }

    @Override
    public String scheme() {
        return scheme;
    }

    @Override
    public String authority() {
        return "";
    }
    
}
