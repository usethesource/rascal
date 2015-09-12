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
import java.net.URI;
import java.nio.charset.Charset;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface ISourceLocationInput {
	InputStream getInputStream(ISourceLocation uri) throws IOException;  
	Charset getCharset(ISourceLocation uri) throws IOException;
	boolean exists(ISourceLocation uri);
	long lastModified(ISourceLocation uri)  throws IOException; 
	boolean isDirectory(ISourceLocation uri);  
	boolean isFile(ISourceLocation uri) ;
	String[] list(ISourceLocation uri)  throws IOException;
	String scheme();
	boolean supportsHost();
	boolean supportsToFileURI();
	URI toFileURI(ISourceLocation uri);
}
