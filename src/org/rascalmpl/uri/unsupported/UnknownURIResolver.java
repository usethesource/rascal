package org.rascalmpl.uri.unsupported;

import org.rascalmpl.uri.URIUtil;

public class UnknownURIResolver extends UnsupportedURIResolver {
    public UnknownURIResolver() {
        super(URIUtil.unknownLocation().getScheme(), "The unknown scheme cannot be read/written to, it indicates someone didn't know the location");
    }
}
