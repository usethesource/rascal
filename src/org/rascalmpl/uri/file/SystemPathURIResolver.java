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
import java.io.IOException;
import java.util.stream.Stream;
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

	private Stream<ISourceLocation> pathStream() {
		String thePath = System.getenv("PATH");
		if (thePath == null) {
			return Stream.<ISourceLocation>empty();
		}

		return Arrays.stream(thePath.split(File.pathSeparator))
			.map(FileURIResolver::constructFileURI)
			.filter(URIResolverRegistry.getInstance()::exists);
	}

	@Override
	public ISourceLocation resolve(ISourceLocation input) {
		return pathStream()
			.map(r -> URIUtil.getChildLocation(r, input.getPath()))
			.filter(URIResolverRegistry.getInstance()::exists)
			.findFirst()
			.orElse(input)
			;
	}

	@Override
	public String[] resolveList(ISourceLocation input) {
		return pathStream()
			.map(r -> URIUtil.getChildLocation(r, input.getPath()))
			.filter(URIResolverRegistry.getInstance()::exists)
			.flatMap(r -> {
				// here we concatenate the `.list()` for all folders in the path
				try {
					if (URIResolverRegistry.getInstance().isDirectory(r)) {
						return Arrays.stream(URIResolverRegistry.getInstance().listEntries(r));
					}
					else {
						return Stream.empty();
					}
				}
				catch (IOException e) {
					return null;
				}
			})
			.filter(o -> o != null)
			.toArray(String[]::new);
	}

	@Override
	public String authority() {
		return "";
	}
}
