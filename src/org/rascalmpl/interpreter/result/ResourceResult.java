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

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.AbstractExternalValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IValueVisitor;

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
	
	@Override
	public IConstructor encodeAsConstructor() {
		return AbstractExternalValue.encodeAsConstructor(this);
	}
	
}
