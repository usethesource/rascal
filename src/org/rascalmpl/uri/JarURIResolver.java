/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class JarURIResolver implements ISourceLocationInput{
	
	public JarURIResolver() {
		super();
	}
	
	private String getJar(ISourceLocation uri) throws IOException {
		String path = uri.getPath();
		if (path == null) {
			path = uri.toString();
		}
		int bang = path.indexOf('!');
		if (bang != -1) {
		  return path.substring(path.indexOf("/"), bang);
		}
		else {
			throw new IOException("The jar and the internal path should be seperated with a !");
		}
	}
	
	private String getPath(ISourceLocation uri) {
		String path = uri.getPath();
		if (path == null) {
			path = uri.toString();
		}
		int bang = path.indexOf('!');
		
		if (bang != -1) {
		  path = path.substring(bang + 1);
		  while (path.startsWith("/")) {
		    path = path.substring(1);
		  }
		  return path;
		}
		else {
		  return "";
		}
	}
	
	public InputStream getInputStream(ISourceLocation uri) throws IOException {
	  String jar = getJar(uri);
    String path = getPath(uri);
    
    @SuppressWarnings("resource")
    // jarFile's are closed the moment when there are no more references to it.
    JarFile jarFile = new JarFile(jar);
    JarEntry jarEntry = jarFile.getJarEntry(path);
    return jarFile.getInputStream(jarEntry);
	}
	
	public boolean exists(ISourceLocation uri) {
		try {
			String jar = getJar(uri);
			String path = getPath(uri);
			
			
			
			JarFile jarFile = new JarFile(jar);
			JarEntry jarEntry = jarFile.getJarEntry(path);
			jarFile.close();
			return(jarEntry != null);
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean isDirectory(ISourceLocation uri){
		try {
			if (uri.getPath() != null && uri.getPath().endsWith(".jar!")) {
				// if the uri is the root of a jar, and it ends with a !, it should be considered a directory
				return true;
			}
			String jar = getJar(uri);
			String path = getPath(uri);
			
			if (!path.endsWith("/")) {
				path = path + "/";
			}
			
			JarFile jarFile = new JarFile(jar);
			try {
				JarEntry jarEntry = jarFile.getJarEntry(path);
				if (jarEntry != null && jarEntry.isDirectory()) {
					return true;
				}
				// maybe the path is not in the jar as a seperate entry, but there are files in the path
				Enumeration<JarEntry> entries =  jarFile.entries();
				while (entries.hasMoreElements()) {
					if (entries.nextElement().getName().startsWith(path)) {
						return true;
					}
				}
				return false;
			}
			finally {
				jarFile.close();
			}
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean isFile(ISourceLocation uri){
		try {
			String jar = getJar(uri);
			String path = getPath(uri);
			
			JarFile jarFile = new JarFile(jar);
			JarEntry jarEntry = jarFile.getJarEntry(path);
			jarFile.close();
			return(jarEntry != null && !jarEntry.isDirectory());
		} catch (IOException e) {
			return false;
		}
	}
	
	public long lastModified(ISourceLocation uri) throws IOException{
		String jar = getJar(uri);
		String path = getPath(uri);
		
		JarFile jarFile = new JarFile(jar);
		JarEntry jarEntry = jarFile.getJarEntry(path);
		jarFile.close();
		
		if (jarEntry == null) {
			throw new FileNotFoundException(uri.toString());
		}
		
		return jarEntry.getTime();
	}
	
	@Override
	public String[] list(ISourceLocation uri) throws IOException {
		String jar = getJar(uri);
		String path = getPath(uri);
		
		if (!path.endsWith("/") && !path.isEmpty()) {
			path = path + "/";
		}
		
		JarFile jarFile = new JarFile(jar);
		
		Enumeration<JarEntry> entries =  jarFile.entries();
		ArrayList<String> matchedEntries = new ArrayList<>();
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			String name = je.getName();
			
			if (name.equals(path)) {
				continue;
			}
			int index = name.indexOf(path);
			
			if (index == 0) {
				String result = name.substring(path.length());
				
				index = result.indexOf("/");
				
				if (index == -1) {
					matchedEntries.add(result);
				} else {
					result = result.substring(0, index);
					boolean entryPresent = false;
					for (Iterator<String> it = matchedEntries.iterator(); it.hasNext(); ) {
						if (result.equals(it.next())) {
							entryPresent = true;
							break;
						}
					}
					if (!entryPresent) {
						matchedEntries.add(result);
					}
				}
			}
		}
		jarFile.close();
		
		String[] listedEntries = new String[matchedEntries.size()];
		return matchedEntries.toArray(listedEntries);
	}
	
	public String scheme() {
		return "jar";
	}

	public boolean supportsHost() {
		return false;
	}

	@Override
	public Charset getCharset(ISourceLocation uri) throws IOException {
		// TODO need to see if we can detect the charset inside a jar
		return null;
	}
}
