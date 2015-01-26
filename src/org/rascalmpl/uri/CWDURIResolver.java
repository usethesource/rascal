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
package org.rascalmpl.uri;

import java.io.File;

import org.eclipse.imp.pdb.facts.ISourceLocation;

/**
 * For reading and writing files relative to the current working directory.
 */
public class CWDURIResolver extends FileURIResolver {

	@Override
	public String scheme() {
		return "cwd";
	}
	
	@Override
	protected String getPath(ISourceLocation uri) {
		return new File(new File(System.getProperty("user.dir")), uri.getPath()).getAbsolutePath();
	}
}
