/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class StandardOutputURIResolver implements IURIOutputStreamResolver {

	public OutputStream getOutputStream(URI uri, boolean append)
			throws IOException {
		return System.out;
	}

	public String scheme() {
		return "stdout";
	}

	public void mkDirectory(URI uri) {
		throw new UnsupportedOperationException("not supported by stdout");
	}

	public URI getResourceURI(URI uri) {
		return URIUtil.invalidURI();
	}

	public boolean supportsHost() {
		return false;
	}
}
