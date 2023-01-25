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
import java.util.Arrays;

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

	private String lastPath = "";
	private ISourceLocation[] roots = new ISourceLocation[0];
	
	@Override
	public ISourceLocation resolve(ISourceLocation input) {
		String thePath = System.getenv("PATH");

		if (thePath == null) {
			return input;
		}
		if (!thePath.equals(lastPath)) {
			synchronized(this) {
				// recalculate roots, path has changed
				roots = Arrays.stream(thePath.split(File.pathSeparator))
					.map(FileURIResolver::constructFileURI)
					.toArray(ISourceLocation[]::new)
					;
				lastPath = thePath;
			}
		}
		
		for (var r :roots) {
			ISourceLocation abs = URIUtil.getChildLocation(r, input.getPath());
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
