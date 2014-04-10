/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.result.Result;

public class Accumulator {

	private String label = null;
	private IListWriter writer = null;
	private StringBuilder builder = null;
	private IValueFactory factory;
	
	public Accumulator(IValueFactory factory, String label) {
		this.factory = factory;
		this.label = label;
	}
	
	public Accumulator(IValueFactory factory) {
		this(factory, null);
	}
	
	public boolean hasLabel(String label) {
		if (this.label == null) {
			return false;
		}
		return this.label.equals(label);
	}
	
	public void append(Result<IValue> value) {
		if (writer == null) {
			writer = factory.listWriter(); 
		}
		writer.append(value.getValue());
	}

	// For string templates.
	public void appendString(IString s) {
		if (builder == null) {
			builder = new StringBuilder(); 
		}
		builder.append(s.getValue());
	}

	
	public IList done() {
		if (builder != null) {
			return factory.list(factory.string(builder.toString()));
		}
		if (writer == null) {
			return factory.list();
		}
		return writer.done();
	}
	
	protected IValueFactory getFactory() {
		return factory;
	}
	
	
}
