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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.load;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.uri.BadURIException;
import org.rascalmpl.uri.IURIInputOutputResolver;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.UnsupportedSchemeException;

/**
 * This class implements the rascal:// scheme. If the path component of a given URI represents a module name, then
 * this resolver will look through the Rascal search path and find the proper input stream if it exists.
 */
public class RascalURIResolver implements IURIInputOutputResolver {
	private final ArrayList<IRascalSearchPathContributor> contributors;
	private final URIResolverRegistry reg;
	
	public RascalURIResolver(URIResolverRegistry ctx) {
		this.contributors = new ArrayList<IRascalSearchPathContributor>();
		this.reg = ctx;
	}
	
	public void addPathContributor(IRascalSearchPathContributor contrib) {
		if(!contributors.contains(contrib)){
			contributors.add(0, contrib);
		}
	}
	
	/**
	 * Returns a URI that does not have the rascal scheme, or null if the URI is not found.
	 */
	public URI resolve(URI uri) {
		try {
			for (URI dir : collect()) {
				URI full = getFullURI(getPath(uri), dir);
				if (reg.exists(full)) {
					return full;
				}
			}
			
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public boolean exists(URI uri) {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (reg.exists(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}

	public URI getRootForModule(URI uri) {
		try {
			for (URI dir : collect()) {
				URI full = getFullURI(getPath(uri), dir);
				if (reg.exists(full)) {
					return dir;
				}
			}

			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	private List<URI> collect() {
		// collect should run the contributors in reverse order
		List<URI> paths = new LinkedList<URI>();
//		List<IRascalSearchPathContributor> reversed = (List<IRascalSearchPathContributor>) contributors.clone();
//		Collections.reverse(reversed);
		for (IRascalSearchPathContributor c : contributors) {
			c.contributePaths(paths);
		}
		
		for (URI uri : paths) {
			if (uri.getScheme().equals(scheme())) {
				throw new IllegalArgumentException("The rascal scheme can not be contributed to the Rascal path:" + uri);
			}
		}
		return paths;
	}

	private URI getFullURI(String path, URI dir) throws URISyntaxException {
		String dirPath = dir.getPath() != null ? dir.getPath() : "/";
		if (dirPath.length() > 0 && !dirPath.startsWith("/")) {
			dirPath = dirPath + "/";
		}
		while (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (!dirPath.endsWith("/")) {
			path = "/" + path;
		}
		return new URI(dir.getScheme(), dir.getHost() != null ? dir.getHost() : "", dirPath + path, null);
	}

	private String getPath(URI uri) {
		String path = uri.getPath();
		
		if (path != null && path.length() != 0) {
			return path;
		}
		else {
			String host = uri.getAuthority();
			
			if (host != null) {
				if (host.endsWith("/")) {
					host = host.substring(0, host.length() - 2);
				}
				if (!host.endsWith(Configuration.RASCAL_FILE_EXT)) {
					host = host.concat(Configuration.RASCAL_FILE_EXT);
				}
				host = host.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
			}
			return host;
		}
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);

				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (reg.exists(full)) {
						return reg.getInputStream(full);
					}
				}
			}
			
			if (uri.getPath() == null || uri.getPath().length() == 0) {
				throw new IOException("File " + uri + " not found.");
			}
			else {
				throw new IOException("Module " + uri + " not found.");
			}
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public String scheme() {
		return "rascal";
	}

	public OutputStream getOutputStream(URI uri, boolean append)
			throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				URI parent = URIResolverRegistry.getParentURI(uri);

				for (URI dir : collect()) {
					URI fullParent = parent != null ? getFullURI(getPath(parent), dir) : null;
					if (fullParent == null || reg.exists(fullParent)) {
						URI full = getFullURI(path, dir);
						try {
							return reg.getOutputStream(full, append);
						}
						catch (UnsupportedSchemeException e) {
							// it happens
						}
					}
				}
			}
			
			if (uri.getPath() == null || uri.getPath().length() == 0) {
				throw new IOException("Module " + uri.getPath() + " not found");
			}
			else {
				throw new IOException("Could not write to file " + uri);
			}
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
	public boolean isDirectory(URI uri) {
		System.err.println("isDirectory: " + uri.getPath());
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					System.err.println("full = " + full.getPath());
					if (reg.exists(full) &&
						reg.isDirectory(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}
	
	public boolean isFile(URI uri) {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (reg.exists(full) &&
					    reg.isFile(full)) {
						return true;
					}
				}
			}
			return false;
		} 
		catch (URISyntaxException e) {
			return false;
		}
	}
	
	public long lastModified(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (reg.exists(full)) {
						return reg.lastModified(full);
					}
				}
			}
			throw new UnsupportedSchemeException(uri.toString());
		} 
		catch (URISyntaxException e) {
			throw new BadURIException(e);
		}
	}
	
	public String[] listEntries(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					if (reg.exists(full)) {
						return reg.listEntries(full);
					}
				}
			}
			throw new UnsupportedSchemeException(uri.toString());
		} 
		catch (URISyntaxException e) {
			throw new BadURIException(e);
		}
	}

	public void mkDirectory(URI uri) throws IOException {
		try {
			if (uri.getScheme().equals(scheme())) {
				String path = getPath(uri);
				
				for (URI dir : collect()) {
					URI full = getFullURI(path, dir);
					try {
						reg.mkDirectory(full);
					}
					catch (UnsupportedSchemeException e) {
						// it happens
						continue;
					}
					return;
				}
			}
			
			
			throw new FileNotFoundException("Parent of " + uri + " could not be found");
		} 
		catch (URISyntaxException e) {
			throw new IOException(e.getMessage(), e);
		}
	}

	public URI getResourceURI(URI uri) {
		try {
			return reg.getResourceURI(uri);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public URIResolverRegistry getRegistry() {
		return reg;
	}

}
