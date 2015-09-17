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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.uri.ISourceLocationInputOutput;

/**
 * This resolver is used for example for the scheme "test-modules", which amongst others 
 * generates modules during tests.
 * These modules are implemented via an in-memory "file system" that guarantees
 * that "lastModified" is monotone increasing, i.e. after a write to a file lastModified
 * is ALWAYS larger than for the previous version of the same file.
 * When files are written at high speeed (e.g. with 10-30 ms intervals ), this property is, 
 * unfortunately, not guaranteed on all operating systems.
 * 
 * So if you are writing temporary files very frequently and use lastModified to mark the fields 
 * as dirty, use an instance of this of this resolver to guarantee the dirty marking.
 * 
 * The locations should not use the autority field, as that is ignored.
 *
 */

public class InMemoryResolver implements ISourceLocationInputOutput {
	
    private final String scheme;

	public InMemoryResolver(String scheme) {
        this.scheme = scheme;
    }

    @Override
	public String scheme() {
		return scheme;
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
		public String toString(){
		    return String.valueOf(timestamp) + ":\n" +new String(contents, StandardCharsets.UTF_8);
		}
	}

	
	private final Map<String, File> files = new HashMap<>();

	@Override
	public InputStream getInputStream(ISourceLocation uri)
			throws IOException {
		File file = files.get(uri.getPath());
		if (file == null) {
			throw new IOException();
		}
		System.err.println("getInputStream: " + uri + "?" + file.toString());
		return new ByteArrayInputStream(file.contents);
	}

	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
		return new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				super.close();
				File file = files.get(uri.getPath());
				if (file == null) {
				    file = new File();
				    files.put(uri.getPath(), file);
				}
				file.newContent(this.toByteArray());
				System.err.println("getOutputStream.close " + uri + "?" + file.toString());
			}
		};
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		File file = files.get(uri.getPath());
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
		return files.containsKey(uri.getPath());
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
		return false;
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return files.containsKey(uri.getPath());
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
		files.remove(uri.getPath());
	}
}
