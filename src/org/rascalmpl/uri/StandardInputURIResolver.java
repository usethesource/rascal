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
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class StandardInputURIResolver implements IURIInputStreamResolver {
	public boolean exists(URI uri) {
		return true;
	}

	public InputStream getInputStream(URI uri) throws IOException {
		return System.in;
	}

	public String scheme() {
		return "stdin";
	}

	public boolean isDirectory(URI uri) {
		return false;
	}

	public boolean isFile(URI uri) {
		return false;
	}

	public long lastModified(URI uri) {
		return 0L;
	}

	public String[] listEntries(URI uri) {
		String [] ls = {};
		return ls;
	}

	public String absolutePath(URI uri) {
		return "stdin";
	}
	
	public boolean supportsHost() {
		return false;
	}
}
