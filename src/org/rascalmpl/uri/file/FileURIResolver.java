/*******************************************************************************
 * Copyright (c) 2009-2017 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.ISourceLocationInputOutput;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.classloaders.IClassloaderLocationResolver;

import io.usethesource.vallang.ISourceLocation;

public class FileURIResolver implements ISourceLocationInputOutput, IClassloaderLocationResolver {
	public FileURIResolver(){
		super();
	}
	
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return new FileInputStream(path);
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	/**
	 * Returns a URL classloader for the jar file or directory pointed to by the loc parameter.
	 */
	public ClassLoader getClassLoader(ISourceLocation loc, ClassLoader parent) throws IOException {
	    assert loc.getScheme().equals(scheme());
	    String path = loc.getPath();
	    
	    if (isDirectory(loc) && !path.endsWith("/")) {
	        path += "/"; // the URL class loader assumes directories end with a /
	    }
	    
	    if (!isDirectory(loc) && !path.endsWith(".jar")) {
			// dictated by URLClassLoader semantics
			throw new IOException("Can only provide classloaders for directories or jar files, not for " + loc);
		} 
	    
	    return new URLClassLoader(new URL[] { new File(path).toURI().toURL() }, parent);
	}
	
	@Override
	public void setLastModified(ISourceLocation uri, long timestamp) throws IOException {
	    new File(getPath(uri)).setLastModified(timestamp);
	}
	 
	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return new BufferedOutputStream(new FileOutputStream(getPath(uri), append));
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
		new File(getPath(uri)).delete();
	} 
	
	public String scheme() {
		return "file";
	}

	public boolean exists(ISourceLocation uri) {
		return new File(getPath(uri)).exists();
	}

	/**
	 * To override to build resolvers to specific locations using a prefix for example.
	 */
	protected String getPath(ISourceLocation uri) {
		return uri.getPath();
	}

	public boolean isDirectory(ISourceLocation uri) {
		return new File(getPath(uri)).isDirectory();
	}

	public boolean isFile(ISourceLocation uri) {
		return new File(getPath(uri)).isFile();
	}

	public long lastModified(ISourceLocation uri) {
		return new File(getPath(uri)).lastModified();
	}

	@Override
	public String[] list(ISourceLocation uri) {
		return new File(getPath(uri)).list();
	}

	public void mkDirectory(ISourceLocation uri) {
		new File(getPath(uri)).mkdirs();
	}

	/**
	 * Utility function to create a URI from an absolute path.
	 * 
	 * @param path a platform-dependent string representation of this path
	 * @return a file schema URI
	 */
	public static ISourceLocation constructFileURI(String path) {
		try{
			return URIUtil.createFileLocation(path);
		}catch(URISyntaxException usex){
			throw new BadURIException(usex);
		}
	}
	
	public boolean supportsHost() {
		return false;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		return null;
	}
	
	@Override
	public boolean supportsReadableFileChannel() {
	    return true;
	}
	
	@Override
	public FileChannel getReadableFileChannel(ISourceLocation uri) throws IOException {
	    String path = getPath(uri);
	    if (path != null) {
	        return FileChannel.open(new File(path).toPath(), StandardOpenOption.READ);
	    }
	    throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public boolean supportsWritableFileChannel() {
	    return true;
	}
	
	@Override
	public FileChannel getWritableOutputStream(ISourceLocation uri, boolean append) throws IOException {
	    String path = getPath(uri);
	    if (path != null) {
	        OpenOption[] options;
	        if (append) {
	            options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.APPEND };
	        }
	        else {
	            options = new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ };
	        }
	        return FileChannel.open(new File(path).toPath(), options);
	    }
	    throw new IOException("uri has no path: " + uri);
	}
	

}
