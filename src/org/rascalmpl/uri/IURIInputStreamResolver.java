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

public interface IURIInputStreamResolver {
	InputStream getInputStream(URI uri) throws IOException;  
	boolean exists(URI uri);
	long lastModified(URI uri)  throws IOException; 
	boolean isDirectory(URI uri);  
	boolean isFile(URI uri) ;
    String[] listEntries(URI uri)  throws IOException;
	String scheme();
	boolean supportsHost();
}
