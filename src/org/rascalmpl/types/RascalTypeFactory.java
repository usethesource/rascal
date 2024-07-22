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
package org.rascalmpl.types;

import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

public class RascalTypeFactory {
	private TypeFactory tf = TypeFactory.getInstance();
	
	private static class InstanceHolder {
		public static final RascalTypeFactory sInstance = new RascalTypeFactory();
	}
	
	public static RascalTypeFactory getInstance() {
		return InstanceHolder.sInstance;
	}
	 
	public Type nonTerminalType(IConstructor cons) {
		if (SymbolAdapter.isADT(cons)) {
			// TODO: what if the ADT has parameters?
			return TypeFactory.getInstance().abstractDataType(
				new TypeStore(), 
				SymbolAdapter.getName(cons));
		}
		return tf.externalType(new NonTerminalType(cons));
	}
	
	public Type nonTerminalType(org.rascalmpl.ast.Type symbol, boolean lex, String layout) {
		return tf.externalType(new NonTerminalType(symbol, lex, layout));
	}
	
	public Type reifiedType(Type arg) {
		return tf.externalType(new ReifiedType(arg));
	}
}
