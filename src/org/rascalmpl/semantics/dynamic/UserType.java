/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;

public abstract class UserType extends org.rascalmpl.ast.UserType {

	static public class Name extends org.rascalmpl.ast.UserType.Name {

		public Name(IConstructor __param1, QualifiedName __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			Environment theEnv = __eval.getHeap().getEnvironmentForName(
					getName(), __eval);
			String name = org.rascalmpl.interpreter.utils.Names.typeName(this
					.getName());

			if (theEnv != null) {
				Type type = theEnv.lookupAlias(name);

				if (type != null) {
					return type;
				}

				Type tree = theEnv.lookupAbstractDataType(name);

				if (tree != null) {
					return tree;
				}

				Type symbol = theEnv.lookupConcreteSyntaxType(name);

				if (symbol != null) {
					return symbol;
				}
			}

			throw new UndeclaredType(name, this);

		}

	}

	static public class Parametric extends
			org.rascalmpl.ast.UserType.Parametric {

		public Parametric(IConstructor __param1, QualifiedName __param2,
				List<org.rascalmpl.ast.Type> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			String name;
			Type type = null;
			Environment theEnv = __eval.getHeap().getEnvironmentForName(
					this.getName(), __eval);

			name = org.rascalmpl.interpreter.utils.Names.typeName(this
					.getName());

			if (theEnv != null) {
				type = theEnv.lookupAlias(name);

				if (type == null) {
					type = theEnv.lookupAbstractDataType(name);
				}
			}

			if (type != null) {
				Map<Type, Type> bindings = new HashMap<Type, Type>();
				Type[] params = new Type[this.getParameters().size()];

				int i = 0;
				for (org.rascalmpl.ast.Type param : this.getParameters()) {
					params[i++] = param.typeOf(__eval, instantiateTypeParameters, eval);
				}

				// __eval has side-effects that we might need?
				type.getTypeParameters().match(TF.tupleType(params), bindings);

				// Instantiate first using the type actually given in the parameter,
				// e.g., for T[str], with data T[&U] = ..., instantate the binding
				// of &U = str, and then instantate using bindings from the current
				// environment (generally meaning we are inside a function with
				// type parameters, and those parameters are now bound to real types)
				return type.instantiate(bindings).instantiate(__eval.getTypeBindings());
			}

			throw new UndeclaredType(name, this);

		}

	}

	public UserType(IConstructor __param1) {
		super(__param1);
	}
}
