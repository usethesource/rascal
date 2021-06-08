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
package org.rascalmpl.uri;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationInput {
	InputStream getInputStream(ISourceLocation uri) throws IOException;
	default FileChannel getReadableFileChannel(ISourceLocation uri) throws IOException {
	    throw new UnsupportedOperationException("File channels not supported for: " + scheme());
	}
	Charset getCharset(ISourceLocation uri) throws IOException;
	boolean exists(ISourceLocation uri);
	long lastModified(ISourceLocation uri)  throws IOException; 
	default long created(ISourceLocation uri) throws IOException {
		return lastModified(uri);
	}
	boolean isDirectory(ISourceLocation uri);  
	boolean isFile(ISourceLocation uri) ;
	String[] list(ISourceLocation uri)  throws IOException;
	String scheme();
	boolean supportsHost();
	default boolean supportsReadableFileChannel() {
	    return false;
	}
}
