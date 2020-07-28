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

import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class ResourceResult extends Result<IValue> implements IExternalValue {

	protected ISourceLocation fullURI;
	protected String displayURI;
	
	protected ResourceResult(Type type, IValue value, IEvaluatorContext ctx, ISourceLocation fullURI, String displayURI) {
		super(type, value, ctx);
		this.fullURI = fullURI;
		this.displayURI = displayURI;
	}

	@Override
	public boolean equals(Object other) {
	    if (other == null) {
	        return false;
	    }
	    
		if (other instanceof ResourceResult) {
			return fullURI.equals(((ResourceResult) other).fullURI);
		}
		return false;
	}
	
	@Override
    public boolean match(IValue other) {
        if (other instanceof ResourceResult) {
            return fullURI.equals(((ResourceResult) other).fullURI);
        }
        return false;
    }
}
