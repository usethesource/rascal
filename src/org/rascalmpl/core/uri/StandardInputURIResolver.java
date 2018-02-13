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
*******************************************************************************/
package org.rascalmpl.core.uri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public class StandardInputURIResolver implements ISourceLocationInput {
	public boolean exists(ISourceLocation uri) {
		return true;
	}

	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		return System.in;
	}

	public String scheme() {
		return "stdin";
	}

	public boolean isDirectory(ISourceLocation uri) {
		return false;
	}

	public boolean isFile(ISourceLocation uri) {
		return false;
	}

	public long lastModified(ISourceLocation uri) {
		return 0L;
	}

	public String[] list(ISourceLocation uri) {
		return new String[] { };
	}

	public String absolutePath(ISourceLocation uri) {
		return "stdin";
	}
	
	public boolean supportsHost() {
		return false;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return null;
	}
}
