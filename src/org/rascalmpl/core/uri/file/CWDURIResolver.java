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
package org.rascalmpl.core.uri.file;

import org.rascalmpl.core.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.core.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

/**
 * For reading and writing files relative to the current working directory.
 */
public class CWDURIResolver implements ILogicalSourceLocationResolver {

	@Override
	public String scheme() {
		return "cwd";
	}
	
	@Override
	public ISourceLocation resolve(ISourceLocation input) {
		return URIUtil.getChildLocation(FileURIResolver.constructFileURI(System.getProperty("user.dir")), input.getPath());
	}

	@Override
	public String authority() {
		return "";
	}
}
