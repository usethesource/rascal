/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Davy Landman -davy.landman@gmail.com - CWI
*******************************************************************************/

package org.rascalmpl.uri.libraries;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.fs.FSEntry;
import org.rascalmpl.uri.fs.FileSystemTree;

import io.usethesource.vallang.ISourceLocation;

/**
 * This resolver implements the scheme "memory" scheme, which implements
 * in-memory file systems for use during testing.
 * 
 * The in-memory "file system" that we use to implement this feature guarantees
 * that "lastModified" is monotone increasing, i.e. after a write to a file lastModified
 * is ALWAYS larger than for the previous version of the same file.
 * When files are written at high speeed (e.g. with 10-30 ms intervals), this property is, 
 * unfortunately, not guaranteed on all operating systems, in all situations
 * So if you are writing temporary files very frequently and use lastModified to mark the fields 
 * as dirty, use an instance of this resolver to guarantee the dirty marking.
 * 
 * The locations should not use the autority field, as that is ignored.
 * 
 * BE AWARE that the information in this in-memory file system is volatile and does not survive:
 * - program execution
 * - replacement by another in-memory filesystem for the same scheme
 * 
 * ALSO BE AWARE that during the existence of the JVM files stored in this scheme
 * are never garbage collected. Use `rm` to clean up after yourselves.
 *
 * The resolver supports the following trick to simulate readonly files, used for testing purposes:
 * If the scheme of a URI ends with `+readonly` the writing part of the resolved throws exceptions. 
 */

public class MemoryResolver implements ISourceLocationInputOutput {
    
	private static class MemoryEntry extends FSEntry {
		private final @Nullable byte[] contents;

		public MemoryEntry() {
			this(null);
		}

		public MemoryEntry(@Nullable byte[] contents) {
			this(System.currentTimeMillis(), System.currentTimeMillis(), contents);
		}

		public MemoryEntry(long created, long lastModified) {
			this(created, lastModified, null);
		}
		public MemoryEntry(long created, long lastModified, @Nullable byte[] contents) {
			super(created, lastModified);
			this.contents = contents;
		}

		public MemoryEntry newContents(byte[] newContents) {
			long newTimestamp = System.currentTimeMillis();
			if (newTimestamp <= getLastModified()) {
				newTimestamp = getLastModified() + 1;
			}
			return new MemoryEntry(getCreated(), newTimestamp, newContents);
		}
	}

    
    private final ConcurrentMap<String, FileSystemTree<MemoryEntry>> fileSystems = new ConcurrentHashMap<>();
    
	public MemoryResolver() { }

    @Override
	public String scheme() {
		return "memory";
	}

	private FileSystemTree<MemoryEntry> getFS(ISourceLocation loc) {
		return getFS(loc.getAuthority());
	}
	private FileSystemTree<MemoryEntry> getFS(String authority) {
		return fileSystems.computeIfAbsent(authority, a -> new FileSystemTree<MemoryEntry>(new MemoryEntry()));
	}

	private static String getPath(ISourceLocation loc) {
		return loc.getPath().substring(1); // remove the leading '/'
	}

	private MemoryEntry get(ISourceLocation uri) throws IOException {
	    return getFS(uri).getEntry(getPath(uri));
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri)
			throws IOException {
		var file = get(uri);
		if (file == null) {
			throw new FileNotFoundException();
		}
		if (file.contents == null) {
			throw new NoSuchFileException(uri.getPath(), null, "not a file, but a directory");
		}
		return new ByteArrayInputStream(file.contents);
	}

	@Override
	public OutputStream getOutputStream(ISourceLocation uri, boolean append)
			throws IOException {
	    if (uri.getScheme().endsWith("+readonly")) {
	        throw new AccessDeniedException(uri.toString());
	    }
	    
		ByteArrayOutputStream result = new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				MemoryEntry entry = null;
				try {
					entry = get(uri);
				} catch (IOException ignored) {
				}
				byte[] content = this.toByteArray();
				var fs = getFS(uri);
				if (entry == null) {
					fs.addFile(getPath(uri), new MemoryEntry(content), MemoryEntry::new);
				}
				else {
					fs.replaceFile(getPath(uri), old -> old.newContents(content));
				}
				super.close();
			}
		};
	    if (append) {
			var file = get(uri);
			if (file == null) {
			    throw new FileNotFoundException();
			}
			if (file.contents == null) {
				throw new NoSuchFileException(getPath(uri), null, "not a file, but a directory");
			}
			// load data to write, makes the closing code simpler
			result.write(file.contents);
	    }
	    return result;
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		return getFS(uri).lastModified(getPath(uri));
	}
	
	@Override
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
		getFS(uri).touch(getPath(uri), timestamp);
	}
	
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		return getFS(uri).exists(getPath(uri));
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
	    return getFS(uri).isDirectory(getPath(uri));
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return getFS(uri).isFile(getPath(uri));
	}

	@Override
	public String[] list(ISourceLocation uri) throws IOException {
	    return getFS(uri).directChildren(getPath(uri));
	}

	@Override
	public boolean supportsHost() {
		return false;
	}

	@Override
	public void mkDirectory(ISourceLocation uri) throws IOException {
	    if (uri.getScheme().endsWith("+readonly")) {
            throw new AccessDeniedException(uri.toString());
        }
		getFS(uri).addDirectory(getPath(uri), new MemoryEntry(), MemoryEntry::new);
	}

	@Override
	public void remove(ISourceLocation uri) throws IOException {
	    if (uri.getScheme().endsWith("+readonly")) {
            throw new AccessDeniedException(uri.toString());
        }
	  
		var ft = getFS(uri.getAuthority());

		var path = getPath(uri);

		if (!path.isEmpty()) {
			// remove the specific file
			ft.remove(path);
		}

		// clean up the entire map if this was the last entry in the filesystem
		if (ft.isEmpty()) {
			fileSystems.remove(uri.getAuthority());
		}
	}
}
