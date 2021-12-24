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
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.usethesource.vallang.ISourceLocation;

public interface ISourceLocationOutput {
	OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException;
	default FileChannel getWritableOutputStream(ISourceLocation uri, boolean append) throws IOException {
	    throw new UnsupportedOperationException("The " + scheme() + " scheme does not support writable output channels.");
	}
	String scheme();
	boolean supportsHost();
	default boolean supportsWritableFileChannel() {
	    return false;
	}
	void mkDirectory(ISourceLocation uri) throws IOException;
	void remove(ISourceLocation uri) throws IOException;
	default void rename(ISourceLocation from, ISourceLocation to, boolean overwrite) throws IOException {
		URIResolverRegistry.getInstance().copy(from, to, true, overwrite);
		URIResolverRegistry.getInstance().remove(from, true);
	}
	default Charset getCharset(ISourceLocation uri) throws IOException {
		return StandardCharsets.UTF_8;
	}
	
	void setLastModified(ISourceLocation uri, long timestamp) throws IOException;
}
