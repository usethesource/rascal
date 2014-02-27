/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.interpreter.IEvaluatorContext;

public abstract class ResourceResult extends Result<IValue> implements IExternalValue {

	protected ISourceLocation fullURI;
	protected String displayURI;
	
	protected ResourceResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx);
		this.fullURI = fullURI;
		this.displayURI = displayURI;
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEqual(IValue other) {
		if (other instanceof ResourceResult) {
			return fullURI.equals(((ResourceResult) other).fullURI);
		}
		return false;
	}
	
	@Override
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}
	
	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
	  throw new IllegalOperationException(
        "Cannot be viewed as with keyword parameters.", getType());
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
	  return false;
	}
}
