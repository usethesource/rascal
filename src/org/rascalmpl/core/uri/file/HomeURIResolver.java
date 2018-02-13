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

import org.rascalmpl.core.uri.ILogicalSourceLocationResolver;
import org.rascalmpl.core.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

public class HomeURIResolver implements ILogicalSourceLocationResolver {

	@Override
	public String scheme() {
		return "home";
	}
	
	@Override
	public ISourceLocation resolve(ISourceLocation input) {
		return URIUtil.getChildLocation(FileURIResolver.constructFileURI(System.getProperty("user.home")), input.getPath());
	}

	@Override
	public String authority() {
		return "";
	}
}
