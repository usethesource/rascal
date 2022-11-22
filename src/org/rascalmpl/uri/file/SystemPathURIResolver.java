/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.uri.file;

import java.io.File;

import org.rascalmpl.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

/**
 * For reading and writing files relative the OS's search PATH stored in the
 * PATH environment variable.
 */
public class SystemPathURIResolver implements ILogicalSourceLocationResolver {

	@Override
	public String scheme() {
		return "PATH";
	}
	
	@Override
	public ISourceLocation resolve(ISourceLocation input) {
		String thePath = System.getenv("PATH");

		if (thePath == null) {
			return input;
		}
		
		String[] elems = thePath.split(File.pathSeparator);

		for (String e : elems) {
			ISourceLocation abs = URIUtil.getChildLocation(URIUtil.correctLocation("file", "", e), input.getPath());
			if (URIResolverRegistry.getInstance().exists(abs)) {
				return abs;
			}
		}

		return input;
	}

	@Override
	public String authority() {
		return "";
	}
}
