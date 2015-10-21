/*******************************************************************************
 * Copyright (c) 2013-2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *    * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 ******************************************************************************/
package org.rascalmpl.value.impl;

import org.rascalmpl.value.IAnnotatable;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.value.IWithKeywordParameters;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.impl.func.NodeFunctions;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.visitors.IValueVisitor;

import io.usethesource.capsule.AbstractSpecialisedImmutableMap;
import io.usethesource.capsule.ImmutableMap;

public abstract class AbstractNode extends AbstractValue implements INode {

	protected static TypeFactory getTypeFactory() {
		return TypeFactory.getInstance();
	}

	protected abstract IValueFactory getValueFactory();


	@Override
	public INode replace(int first, int second, int end, IList repl) throws FactTypeUseException, IndexOutOfBoundsException {
		return NodeFunctions.replace(getValueFactory(), this, first, second, end, repl);
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T, E> v) throws E {
		return v.visitNode(this);
	}
	
	@Override
	public boolean isAnnotatable() {
		return true;
	}
	
	@Override
	public IAnnotatable<? extends INode> asAnnotatable() {
		return new AbstractDefaultAnnotatable<INode>(this) {
			@Override
			protected INode wrap(INode content, ImmutableMap<String, IValue> annotations) {
				return new AnnotatedNodeFacade(content, annotations);
			}
		};
	}
	
	@Override
	public boolean mayHaveKeywordParameters() {
	  return true;
	}
	
	@Override
	public IWithKeywordParameters<? extends INode> asWithKeywordParameters() {
	  return new AbstractDefaultWithKeywordParameters<INode>(this, AbstractSpecialisedImmutableMap.<String, IValue>mapOf()) {
	    @Override
	    protected INode wrap(INode content, ImmutableMap<String, IValue> parameters) {
	      return new NodeWithKeywordParametersFacade(content, parameters);
	    }
    };
	}

}
