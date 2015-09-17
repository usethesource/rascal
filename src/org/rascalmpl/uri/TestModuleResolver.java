/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Davy Landman -davy.landman@gmail.com - CWI
*******************************************************************************/

package org.rascalmpl.uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.uri.ISourceLocationInputOutput;

/**
 * The scheme "test-modules" is used, amongst others, to generate modules during tests.
 * These modules are implemented via an in-memory "file system" that guarantees
 * that "lastModified" is monotone increasing, i.e. after a write to a file lastModified
 * is ALWAYS larger than for the previous version of the same file.
 * When files are written at high speeed (e.g. with 10-30 ms intervals ), this property is, 
 * unfortunately, not guaranteed on all operating systems.
 *
 */

public class TestModuleResolver implements ISourceLocationInputOutput {
	
	@Override
	public String scheme() {
		return "test-modules";
	}
	
	private static final class File {
		byte[] contents;
		long timestamp;
		public File() {
			contents = new byte[0];
			timestamp = System.currentTimeMillis();
		}
		public void newContent(byte[] byteArray) {
			long newTimestamp = System.currentTimeMillis();
			if (newTimestamp <= timestamp) {
				newTimestamp =  timestamp +1;
			}
			timestamp = newTimestamp;
			contents = byteArray;
		}
	}
	
	private final Map<ISourceLocation, File> files = new HashMap<>();

	@Override
	public InputStream getInputStream(ISourceLocation uri)
			throws IOException {
		File file = files.get(uri);
		if (file == null) {
			throw new IOException();
		}
		return new ByteArrayInputStream(file.contents);
	}

	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
		File file = files.get(uri);
		final File result = file == null ? new File() : file; 
		return new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				super.close();
				result.newContent(this.toByteArray());
				files.put(uri, result);
			}
		};
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		File file = files.get(uri);
		if (file == null) {
			throw new IOException();
		}
		return file.timestamp;
	}
	
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		return files.containsKey(uri);
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		return false;
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return files.containsKey(uri);
	}

	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		return null;
	}

	@Override
	public boolean supportsHost() {
		return false;
	}

	@Override
	public void mkDirectory(ISourceLocation uri) throws IOException {
	}

	@Override
	public void remove(ISourceLocation uri) throws IOException {
		files.remove(uri);
	}
}
