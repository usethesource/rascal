/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri.file;

import java.net.URISyntaxException;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

public class TempURIResolver implements ILogicalSourceLocationResolver {
    
    private final ISourceLocation root;

    public TempURIResolver() {
        try {
            root = URIUtil.createFileLocation(System.getProperty("java.io.tmpdir"));
        }
        catch (URISyntaxException e) {
            throw new RuntimeException("Error loading temporary location");
        }
    }

    @Override
    public String scheme() {
        return "tmp";
    }

    @Override
    public ISourceLocation resolve(ISourceLocation input) {
        return URIUtil.getChildLocation(root, input.getPath());
    }

    @Override
    public String authority() {
        return "";
    }
}
