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
package org.rascalmpl.library;

import java.util.Collections;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Type {
	private final IValueFactory vf;

	public Type(IValueFactory vf) {
		this.vf = vf;
		
	}
	
	public IValue typeOf(IValue v, IEvaluatorContext ctx) {
		return ((IConstructor) new TypeReifier(vf).typeToValue(v.getType(), ctx).getValue()).get("symbol");
	}
	
	public IBool eq(IValue x, IValue y) {
	  return vf.bool(x.isEqual(y));
	}
	
	public IValue make(IValue type, IString name, IList args) {
		TypeStore store = new TypeStore();
		org.eclipse.imp.pdb.facts.type.Type t = new TypeReifier(vf).valueToType((IConstructor) type, store);
		
		IValue[] children = new IValue[args.length()];
		org.eclipse.imp.pdb.facts.type.Type[] argsTypes = new org.eclipse.imp.pdb.facts.type.Type[args.length()];

		for (int i = 0; i < args.length(); i++) {
			children[i] = args.get(i);
			argsTypes[i] = children[i].getType();
		}
		
		try {
			org.eclipse.imp.pdb.facts.type.Type constructor 
			= store.lookupConstructor(t, name.getValue(), TypeFactory.getInstance().tupleType(argsTypes));
			
			if (constructor == null) {
				// TODO: improve error messaging, using specialized exception
				throw RuntimeExceptionFactory.illegalArgument(type, null, null);
			}
			return vf.constructor(constructor, children, Collections.<String,IValue>emptyMap());
		}
		catch (FactTypeUseException e) {
			// TODO: improve error messaging, using specialized exception
			throw RuntimeExceptionFactory.illegalArgument(type, null, null);
		}
		
		
	}
	
}
