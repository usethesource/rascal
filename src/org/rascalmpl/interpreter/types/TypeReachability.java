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
package org.rascalmpl.interpreter.types;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;

public class TypeReachability {
	public static boolean mayOccurIn(Type small, Type large, Environment env) {
		return mayOccurIn(small, large, new HashSet<Type>(), env);
	}

	static private boolean mayOccurIn(Type small, Type large, Set<Type> seen, Environment env) {
		// TODO: this should probably be a visitor as well

		if (small.isVoidType())
			return true;
		if (large.isVoidType())
			return false;
		if (small.isValueType())
			return true;
		if (small.isSubtypeOf(large))
			return true;
		if (large.isListType() || large.isSetType())
			return mayOccurIn(small, large.getElementType(), seen, env);
		if (large.isMapType())
			return mayOccurIn(small, large.getKeyType(), seen, env) || mayOccurIn(small, large.getValueType(), seen, env);
		if (large.isTupleType()) {
			for (int i = 0; i < large.getArity(); i++) {
				if (mayOccurIn(small, large.getFieldType(i), seen, env))
					return true;
			}
			return false;
		}

		if (large instanceof NonTerminalType && small instanceof NonTerminalType) {
			// TODO: Until we have more precise info about the types in the
			// concrete syntax
			// we just return true here.
			return true;
		}

		if (large.isConstructorType()) {
			for (int i = 0; i < large.getArity(); i++) {
				if (mayOccurIn(small, large.getFieldType(i), seen, env)) {
					return true;
				}
			}
			return false;
		}
		if (large.isAbstractDataType()) {
			if (small.isNodeType() && !small.isAbstractDataType()) {
				return true;
			}
			if (small.isConstructorType() && small.getAbstractDataType().equivalent(large.getAbstractDataType())) {
				return true;
			}
			seen.add(large);
			for (Type alt : env.lookupAlternatives(large)) {
				if (alt.isConstructorType()) {
					for (int i = 0; i < alt.getArity(); i++) {
						Type fType = alt.getFieldType(i);
						if (seen.add(fType) && mayOccurIn(small, fType, seen, env))
							return true;
					}
				} else {
					throw new ImplementationError("ADT");
				}
			}
			return false;
		}
		return small.isSubtypeOf(large);
	}

}
