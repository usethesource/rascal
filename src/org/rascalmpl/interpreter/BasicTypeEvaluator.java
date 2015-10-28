/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class BasicTypeEvaluator {
	private final static TypeFactory tf = org.rascalmpl.value.type.TypeFactory.getInstance();
	private final Type typeArgument;
	private final IValue[] valueArguments; // for adt, constructor and
											// non-terminal representations
	private final Environment env;

	public BasicTypeEvaluator(Environment env, Type argumentTypes, IValue[] valueArguments) {
		this.env = env;
		this.typeArgument = argumentTypes;
		this.valueArguments = valueArguments;
	}

	public Type __getTypeArgument() {
		return typeArgument;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	public Environment __getEnv() {
		return env;
	}

	public IValue[] __getValueArguments() {
		return valueArguments;
	}
}
