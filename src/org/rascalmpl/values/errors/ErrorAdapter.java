/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.values.errors;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;

public class ErrorAdapter implements Iterable<SubjectAdapter> {
	private IConstructor error;
	
	public ErrorAdapter(IValue error) {
		this.error = (IConstructor) error;
	}
	
	public IList getSubjects() {
		return (IList) error.get("subjects");
	}
	
	public String getDescription() {
		return ((IString) error.get("description")).getValue();
	}
	
	public boolean isError() {
		return error.getConstructorType() == Factory.Error_Error;
	}
	
	public boolean isWarning() {
		return error.getConstructorType() == Factory.Error_Warning;
	}
	
	public boolean isInfo() {
		return error.getConstructorType() == Factory.Error_Info;
	}
	
	public boolean isFatal() {
		return error.getConstructorType() == Factory.Error_Fatal;
	}

	/**
	 * Iterate over subjects in error
	 */
	public Iterator<SubjectAdapter> iterator() {
		return new Iterator<SubjectAdapter>() {
			Iterator<IValue> subjects = ((IList) error.get("subjects")).iterator();
			
			public boolean hasNext() {
				return subjects.hasNext();
			}

			public SubjectAdapter next() {
				return new SubjectAdapter(subjects.next());
			}

			public void remove() {
				subjects.remove();
			}
		};
	}
}
