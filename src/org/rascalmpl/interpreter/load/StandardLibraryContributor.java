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

import java.util.List;

import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;


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
		l.add(URIUtil.rootLocation("std"));
	}

	@Override
	public String toString() {
		return "|std:///|";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
}
