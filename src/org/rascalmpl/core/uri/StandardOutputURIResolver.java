/*******************************************************************************
 * Copyright (c) 2009-2014 CWI
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
package org.rascalmpl.core.uri;

import java.io.IOException;
import java.io.OutputStream;

import io.usethesource.vallang.ISourceLocation;

public class StandardOutputURIResolver implements ISourceLocationOutput {

	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		return System.out;
	}

	@Override
	public String scheme() {
		return "stdout";
	}

	@Override
	public void mkDirectory(ISourceLocation uri) {
		throw new UnsupportedOperationException("not supported by stdout");
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
	  throw new UnsupportedOperationException("not supported by stdout");
	}

	@Override
	public boolean supportsHost() {
		return false;
	}
}
