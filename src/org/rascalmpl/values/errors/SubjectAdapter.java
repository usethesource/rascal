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
*******************************************************************************/
package org.rascalmpl.values.errors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.values.ValueFactoryFactory;

public class SubjectAdapter {
	private IConstructor subject;
	
	public SubjectAdapter(IValue subject) {
		this.subject = (IConstructor) subject;
	}
	
	public boolean isLocalized() {
		return subject.getConstructorType() == Factory.Subject_Localized;
	}

	public ISourceLocation getLocation() {
		if (isLocalized()) {
			return org.rascalmpl.values.locations.Factory.getInstance().toSourceLocation(ValueFactoryFactory.getValueFactory(), (IConstructor) subject.get("location"));
		}
		return null;
	}
	
	public String getPath() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getURI().getPath();
		}
		return null;
	}
	
	public int getBeginColumn() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getBeginColumn();
		}
		return 0;
	}
	
	public int getEndColumn() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getEndColumn();
		}
		return 0;
	}
	
	public int getBeginLine() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getBeginLine();
		}
		return 1;
	}
	
	public int getEndLine() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getEndLine();
		}
		return 1;
	}
	
	public int getOffset() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getOffset();
		}
		return 0;
	}
	
	public int getLength() {
		ISourceLocation loc = getLocation();
		if (loc != null) {
			return loc.getLength();
		}
		return 0;
	}
	
	public String getDescription() {
		return ((IString) subject.get("description")).getValue();
	}
}
