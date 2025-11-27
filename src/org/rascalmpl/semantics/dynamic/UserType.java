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
import java.util.UUID;
import java.util.Map.Entry;

import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.MissingTypeParameters;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;
import org.rascalmpl.interpreter.utils.Names;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class UserType extends org.rascalmpl.ast.UserType {

	static public class Name extends org.rascalmpl.ast.UserType.Name {

		public Name(ISourceLocation __param1, IConstructor tree, QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
		    Environment theEnv = __eval.getHeap().getEnvironmentForName(getName(), __eval);
		    String name = Names.typeName(this.getName());

		    if (theEnv != null) {
		        Type type = theEnv.lookupAlias(name);

		        if (type != null) {
		            if (type.isParameterized()) {
		                throw new MissingTypeParameters(type, this);
		            }

		            return type.getAliased();
		        }

		        Type tree = theEnv.lookupAbstractDataType(name);

		        if (tree != null) {

		            if (tree.isParameterized()) {
		                throw new MissingTypeParameters(tree, this);
		            }

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

		public Parametric(ISourceLocation __param1, IConstructor tree, QualifiedName __param2,
				List<org.rascalmpl.ast.Type> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			String name;
			Type type = null;
			Environment theEnv = __eval.getHeap().getEnvironmentForName(this.getName(), __eval);

			name = Names.typeName(this.getName());

			if (theEnv != null) {
				type = theEnv.lookupAlias(name);

				if (type == null) {
					type = theEnv.lookupAbstractDataType(name);
				}
			}

			if (type != null) {
				Type[] params = new Type[this.getParameters().size()];

				int i = 0;
				for (org.rascalmpl.ast.Type param : this.getParameters()) {
					params[i++] = param.typeOf(__eval, eval, instantiateTypeParameters);
				}
				
				Type tuple = TF.tupleType(params);
			
				// if names of the formal type parameters of the alias or ADT overlap with the currently
				// instantiated types (could be type parameters themselves) we have a non-hygenic type parameter environment.
				// so first we do some renaming:
				if (tuple.isOpen()) {
				    Map<Type, Type> renamings = new HashMap<Type, Type>();
				    
				    tuple.match(TF.voidType(), renamings);
				    // rename all the bound type parameters
			        for (Entry<Type,Type> entry : renamings.entrySet()) {
			            Type key = entry.getKey();
			            renamings.put(key, TF.parameterType(key.getName() + ":" + UUID.randomUUID().toString(), key.getBound()));
			        }
			        
			        type = type.instantiate(renamings);
			        
			        // now the declared type has unique type parameters
				}
				
				// then we bind the formals to the actuals:
                Map<Type, Type> bindings = new HashMap<Type, Type>();
                type.getTypeParameters().match(tuple, bindings);
                return type.instantiate(bindings);
			}

			throw new UndeclaredType(name, this);

		}

	}

	public UserType(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
