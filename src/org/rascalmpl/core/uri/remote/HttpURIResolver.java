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
package org.rascalmpl.core.uri.remote;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.rascalmpl.core.uri.ISourceLocationInput;
import io.usethesource.vallang.ISourceLocation;

public class HttpURIResolver implements ISourceLocationInput {

	@Override
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		return new BufferedInputStream(uri.getURI().toURL().openStream());
	}

	@Override
	public String scheme() {
		return "http";
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		try {
			uri.getURI().toURL().openConnection();
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		return false;
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return true;
	}

	@Override
	public long lastModified(ISourceLocation uri) {
		try {
			return uri.getURI().toURL().openConnection().getLastModified();
		}
		catch (IOException e) {
			return 0L;
		}
	}

	@Override
	public String[] list(ISourceLocation uri) {
		return new String[] { };
	}

	@Override
	public boolean supportsHost() {
		return true;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		try {
			String encoding = uri.getURI().toURL().openConnection().getContentEncoding();
			if (encoding != null && Charset.isSupported(encoding)) {
				return Charset.forName(encoding);
			}
			return null;
		}
		catch (IOException e) {
			return null;
		}
	}
}
