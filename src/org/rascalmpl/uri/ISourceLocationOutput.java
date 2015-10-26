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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.compress.utils.Charsets;
import org.rascalmpl.value.ISourceLocation;

public interface ISourceLocationOutput {
	OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException;
	String scheme();
	boolean supportsHost();
	void mkDirectory(ISourceLocation uri) throws IOException;
	void remove(ISourceLocation uri) throws IOException;
	default Charset getCharset(ISourceLocation uri) throws IOException {
		return Charsets.UTF_8;
	}
}
