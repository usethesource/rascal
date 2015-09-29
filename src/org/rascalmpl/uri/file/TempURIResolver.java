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

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class TempURIResolver extends FileURIResolver {
	@Override
	public String scheme() {
		return "tmp";
	}
	
	@Override
	protected String getPath(ISourceLocation uri) {
		String path = super.getPath(uri);
		return System.getProperty("java.io.tmpdir") + (path.startsWith("/") ? path : ("/" + path));
	}
}
