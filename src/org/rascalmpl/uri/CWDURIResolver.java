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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * For reading and writing files relative to the current working directory.
 */
public class CWDURIResolver implements IURIInputOutputResolver {

	public InputStream getInputStream(URI uri) throws IOException {
		return new FileInputStream(getAbsolutePath(uri));
	}

	public URI getResourceURI(URI uri) {
		return new File(System.getProperty("user.dir") + uri.getPath()).toURI();
	}
	
	private File getAbsolutePath(URI uri) {
		return new File(getResourceURI(uri));
	}

	public String scheme() {
		return "cwd";
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		return new FileOutputStream(getAbsolutePath(uri), append);
	}

	public boolean exists(URI uri) {
		return getAbsolutePath(uri).exists();
	}

	public boolean isDirectory(URI uri) {
		return getAbsolutePath(uri).isDirectory();
	}

	public boolean isFile(URI uri) {
		return getAbsolutePath(uri).isFile();
	}

	public long lastModified(URI uri) {
		return getAbsolutePath(uri).lastModified();
	}

	public String[] listEntries(URI uri) {
		return getAbsolutePath(uri).list();
	}
	
	public void mkDirectory(URI uri) {
		getAbsolutePath(uri).mkdir();
	}

	public boolean supportsHost() {
		return false;
	}
}
