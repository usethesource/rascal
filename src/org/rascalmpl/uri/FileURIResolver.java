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
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class FileURIResolver implements ISourceLocationInputOutput {
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
	
	public OutputStream getOutputStream(ISourceLocation uri, boolean append) throws IOException {
		String path = getPath(uri);
		if (path != null) {
			return new BufferedOutputStream(new FileOutputStream(getPath(uri), append));
		}
		throw new IOException("uri has no path: " + uri);
	}
	
	@Override
	public void remove(ISourceLocation uri) throws IOException {
	  String path = getPath(uri);
	  File file = new File(path);
	  
	  if (file.isDirectory()) {
		  for (ISourceLocation element : list(uri)) {
			  remove(element);
		  }
	  }
	  
	  file.delete();
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
	public ISourceLocation[] list(ISourceLocation uri) {
		String[] list = new File(getPath(uri)).list();
		
		if (list == null) {
			return new ISourceLocation[] { };
		} else {
			ISourceLocation[] res = new ISourceLocation[list.length];
			int i = 0;
			for (String element : list) {
				res[i++] = URIUtil.getChildLocation(uri, element);
			}
			
			return res;
		}
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
}
