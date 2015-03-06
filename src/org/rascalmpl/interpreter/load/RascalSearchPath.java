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
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.load;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

/**
 * This class implements a search path for Rascal. It can be used to look up modules
 * by name or files by path relative to the roots of all path contributors.
 * It will return the first match found.
 */
public class RascalSearchPath {
	private final ArrayList<IRascalSearchPathContributor> contributors;
	private final URIResolverRegistry reg;
	
	public RascalSearchPath() {
		this.contributors = new ArrayList<IRascalSearchPathContributor>();
		this.reg = URIResolverRegistry.getInstance();
	}
	
	public void addPathContributor(IRascalSearchPathContributor contrib) {
		if(!contributors.contains(contrib)){
			contributors.add(0, contrib);
		}
	}
	
	public ISourceLocation resolveModule(String module) {
		module = moduleToFile(module);
				
		try {
			for (ISourceLocation dir : collect()) {
				ISourceLocation full = getFullURI(module, dir);
				if (reg.exists(full)) {
					return full;
				}
			}
			
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private String moduleToFile(String module) {
		if (!module.endsWith(Configuration.RASCAL_FILE_EXT)) {
			module = module.concat(Configuration.RASCAL_FILE_EXT);
		}
		return module.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
	}
	
	public ISourceLocation resolvePath(String path) {
		try {
			for (ISourceLocation dir : collect()) {
				ISourceLocation full = getFullURI(path, dir);
				if (reg.exists(full)) {
					return full;
				}
			}
			
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public ISourceLocation getRootForModule(String module) {
		try {
			for (ISourceLocation dir : collect()) {
				ISourceLocation full = getFullURI(moduleToFile(module), dir);
				if (reg.exists(full)) {
					return dir;
				}
			}

			return null;
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public List<ISourceLocation> collect() { 
		List<ISourceLocation> paths = new LinkedList<ISourceLocation>();
		for (IRascalSearchPathContributor c : contributors) {
			c.contributePaths(paths);
		}
		
		return paths;
	}

	private ISourceLocation getFullURI(String path, ISourceLocation dir) throws URISyntaxException {
		return URIUtil.getChildLocation(dir, path);
	}

	public URIResolverRegistry getRegistry() {
		return reg;
	}

	public void remove(IRascalSearchPathContributor contrib) {
		contributors.remove(contrib);
	}
}
