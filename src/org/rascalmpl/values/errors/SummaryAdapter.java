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
import org.eclipse.imp.pdb.facts.IValue;

public class SummaryAdapter implements Iterable<ErrorAdapter> {
	private IConstructor summary;
	
	public SummaryAdapter(IValue summary) {
		this.summary = (IConstructor) summary;
	}
	
	public IValue getProducer() {
		return summary.get("producer");
	}
	
	public IValue getId() {
		return summary.get("id");
	}

	/**
	 * Iterates over errors in summary
	 */
	public Iterator<ErrorAdapter> iterator() {
		return new Iterator<ErrorAdapter>() {
			Iterator<IValue> errors = ((IList) summary.get("errors")).iterator();

			public boolean hasNext() {
				return errors.hasNext();
			}

			public ErrorAdapter next() {
				return new ErrorAdapter(errors.next());
			}

			public void remove() {
				errors.remove();
			}
		};
	}
	
	public ErrorAdapter getInitialError() {
		return this.iterator().next();
	}
	
	public SubjectAdapter getInitialSubject() {
		for (ErrorAdapter error : this) {
			for (SubjectAdapter subject : error) {
				if (subject.isLocalized()) {
					return subject;
				}
			}
		}
		
		return null;
	}
}
