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
package org.rascalmpl.core.types;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RascalTypeFactory {
	private TypeFactory tf = TypeFactory.getInstance();
	
	private static class InstanceHolder {
		public static final RascalTypeFactory sInstance = new RascalTypeFactory();
	}
	
	public static RascalTypeFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	 
	public Type functionType(Type returnType, Type argumentTypes, Type keywordParams) {
		return tf.externalType(new FunctionType(returnType, argumentTypes, keywordParams));
	}
	
	public Type nonTerminalType(IConstructor cons) {
		return tf.externalType(new NonTerminalType(cons));
	}
	
	public Type nonTerminalType(org.rascalmpl.ast.Type symbol, boolean lex, String layout) {
		return tf.externalType(new NonTerminalType(symbol, lex, layout));
	}
	
//	public Type overloadedFunctionType(Set<FunctionType> newAlternatives) {
//		return tf.externalType(new OverloadedFunctionType(newAlternatives));
//	}

	public Type reifiedType(Type arg) {
		return tf.externalType(new ReifiedType(arg));
	}
	
//	public Type failureType(ISet messages) {
//	    return tf.externalType(new FailureType(messages));
//	}
}
