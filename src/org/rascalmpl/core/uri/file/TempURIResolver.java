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
package org.rascalmpl.core.uri.file;

import java.net.URISyntaxException;

import org.rascalmpl.core.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.core.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.core.values.ValueFactoryFactory;

public class TempURIResolver implements ILogicalSourceLocationResolver {
    
    private final IValueFactory VF;
    private final ISourceLocation root;

    public TempURIResolver() {
        VF = ValueFactoryFactory.getValueFactory();
        ISourceLocation rootFinalHack = null;
        try {
            rootFinalHack = VF.sourceLocation("file", "", System.getProperty("java.io.tmpdir") + "/");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        root = rootFinalHack;
    }
    
	@Override
	public String scheme() {
		return "tmp";
	}

    @Override
    public ISourceLocation resolve(ISourceLocation input) {
        String path = input.getPath();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return URIUtil.getChildLocation(root, path);
    }

    @Override
    public String authority() {
        return "";
    }
}
