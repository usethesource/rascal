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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import io.usethesource.vallang.ISourceLocation;

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
	private String moduleToDir(String module) {
		return module.replaceAll(Configuration.RASCAL_MODULE_SEP, Configuration.RASCAL_PATH_SEP);
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
	
	public List<String> listModuleEntries(String moduleRoot) {
	    assert !moduleRoot.endsWith("::");
		try {
			    String modulePath = moduleToDir(moduleRoot);
		    List<String> result = new ArrayList<>();
			for (ISourceLocation dir : collect()) {
				ISourceLocation full = getFullURI(modulePath, dir);
				if (reg.exists(full)) {
				    try {
				        String[] entries = reg.listEntries(full);
				        if (entries == null) {
				            continue;
				        }
                        for (String module: entries ) {
                            if (module.endsWith(Configuration.RASCAL_FILE_EXT)) {
                                result.add(module.substring(0, module.length() - Configuration.RASCAL_FILE_EXT.length()));
                            }
                            else if (module.indexOf('.') == -1 && reg.isDirectory(getFullURI(module, full))) {
                                // a sub folder path
                                result.add(module + "::");
                            }
                        }
                    }
                    catch (IOException e) {
                    }
				}
			}
			return result;
		} catch (URISyntaxException e) {
			return Collections.emptyList();
		}
	    
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
	
	public String toString(){
		StringBuffer sb = new StringBuffer("RascalSearchPath[ ");
		
		for(IRascalSearchPathContributor contrib : contributors){
			sb.append(contrib.getName()).append(" ");
		}
		sb.append("])");
		return sb.toString();
	}
}
