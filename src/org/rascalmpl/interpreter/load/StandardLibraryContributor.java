/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Atze van der Ploeg - A.J.van.der.Ploeg@cwi.nl - CWI
 *   * Davy Landman  - Davy.Landman@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.load;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.rascalmpl.interpreter.utils.RascalManifest;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;


public class StandardLibraryContributor implements
		IRascalSearchPathContributor {
	
	private StandardLibraryContributor() { }
	
	private static class InstanceHolder {
		public static StandardLibraryContributor sInstance = new StandardLibraryContributor();
	}
	
	@Override
	public String getName() {
	  return "std";
	}
	
	public static StandardLibraryContributor getInstance() {
		return InstanceHolder.sInstance;
	}
	
	public void contributePaths(List<ISourceLocation> l) {
		String property = java.lang.System.getProperty("rascal.path");
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
				
		if (property != null) {
			for (String path : property.split(":")) {
				try {
					if (path.endsWith(".jar")) {
						for (String root: new RascalManifest().getSourceRoots(new File(path))) {
							l.add(vf.sourceLocation("jar","", path + "!" + root));
						}
					}
					else {
						l.add(URIUtil.createFileLocation(path));
					}
				} catch (URISyntaxException e) {
				}
			}
		}
		
		try {
			l.add(vf.sourceLocation("cwd","",""));
			l.add(vf.sourceLocation("std","",""));
			l.add(vf.sourceLocation("testdata","",""));
		    l.add(vf.sourceLocation("test-modules","",""));
			l.add(vf.sourceLocation("benchmarks","",""));
		}
		catch (URISyntaxException e) {
			assert false;
		}
	}

	@Override
	public String toString() {
		return "[current wd and stdlib]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
}