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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;

import org.rascalmpl.library.Prelude;
import org.rascalmpl.uri.FileTree;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

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

public class MemoryResolver implements ISourceLocationInputOutput, IClassloaderLocationResolver {
    
    private final class InMemoryFileTree extends FileTree { 
        public ConcurrentNavigableMap<String, FSEntry> getFileSystem() {
            return fs;
        }
    }
    
    private final ConcurrentMap<String, InMemoryFileTree> fileSystems = new ConcurrentHashMap<>();
    
	public MemoryResolver() {
		
    }

    @Override
	public String scheme() {
		return "memory";
	}
	
	private static final class File extends FileTree.FSEntry {
		byte[] contents;
		public File() {
		    super(System.currentTimeMillis());
			contents = null;
		}
		public void newContent(byte[] byteArray) {
			long newTimestamp = System.currentTimeMillis();
			if (newTimestamp <= lastModified) {
				newTimestamp =  lastModified + 1;
			}
			lastModified = newTimestamp;
			contents = byteArray;
		}
		
		public String toString(){
		    return String.valueOf(lastModified) ;//+ ":\n" +new String(contents, StandardCharsets.UTF_8);
		}
	}

	private InMemoryFileTree getFS(String authority) {
		return fileSystems.computeIfAbsent(authority, a -> new InMemoryFileTree());
	}

	private File get(ISourceLocation uri) {
	    return (File) getFS(uri.getAuthority()).getFileSystem().get(uri.getPath());
	}
	
	@Override
	public InputStream getInputStream(ISourceLocation uri)
			throws IOException {
		File file = get(uri);
		if (file == null) {
			throw new FileNotFoundException();
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
				File file = get(uri);
				byte[] content = this.toByteArray();
				if (file == null) {
				    file = new File();
				    getFS(uri.getAuthority()).getFileSystem().put(uri.getPath(), file);
				}
				file.newContent(content);
				super.close();
			}
		};
	    if (append) {
			File file = get(uri);
			if (file == null) {
			    throw new FileNotFoundException();
			}
			// load data to write, makes the closing code simpler
			result.write(file.contents);
	    }
	    return result;
	}
	
	@Override
	public long lastModified(ISourceLocation uri) throws IOException {
		File file = get(uri);
		if (file == null) {
			throw new FileNotFoundException();
		}
		return file.lastModified;
	}
	
	@Override
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
	    File file = get(uri);
	    
	    if (file == null) {
	        throw new FileNotFoundException(uri.toString());
	    }
	    
	    file.lastModified = timestamp;
	}
	
	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return null;
	}

	@Override
	public boolean exists(ISourceLocation uri) {
		return getFS(uri.getAuthority()).exists(uri.getPath());
	}

	@Override
	public boolean isDirectory(ISourceLocation uri) {
	    return getFS(uri.getAuthority()).isDirectory(uri.getPath());
	}

	@Override
	public boolean isFile(ISourceLocation uri) {
		return getFS(uri.getAuthority()).isFile(uri.getPath());
	}

	@Override
	public String[] list(ISourceLocation uri) throws IOException {
	    return getFS(uri.getAuthority()).directChildren(uri.getPath());
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
	}

	@Override
	public void remove(ISourceLocation uri) throws IOException {
	    if (uri.getScheme().endsWith("+readonly")) {
            throw new AccessDeniedException(uri.toString());
        }
	  
		var ft = getFS(uri.getAuthority()).getFileSystem();

		// remove the specific file
	    ft.remove(uri.getPath());

		// clean up the entire map if this was the last entry in the filesystem
		if (ft.isEmpty()) {
			fileSystems.remove(uri.getAuthority());
		}
	}

	@Override
	public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
		return new FromMemoryClassLoader(loc, parent);
	}

	private static class FromMemoryClassLoader extends ClassLoader {
		private final ISourceLocation root;
		private final URIResolverRegistry reg = URIResolverRegistry.getInstance();

		public FromMemoryClassLoader(ISourceLocation root, ClassLoader parent) {
			super(parent);
			this.root = root;
		}

		@Override
		protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
			var file = URIUtil.getChildLocation(root, qualifiedClassName.replaceAll("\\.", "/") + ".class");

			// TODO this code is surprisingly generic. It might work for _any_ loc scheme.
			if (reg.exists(file)) {
				try {
					byte[] bytes = Prelude.consumeInputStream(reg.getInputStream(file));
					return defineClass(qualifiedClassName, bytes, 0, bytes.length);
				}
				catch (IOException e) {
					// fall through
				}
			}
			// Workaround for "feature" in Java 6
			// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
			try {
				Class<?> c = Class.forName(qualifiedClassName);
				return c;
			} catch (ClassNotFoundException nf) {
				// Ignore and fall through
			}

			return super.findClass(qualifiedClassName);
		}
	}
}
