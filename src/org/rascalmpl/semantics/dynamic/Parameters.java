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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Formals;
import org.rascalmpl.ast.KeywordFormals;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Parameters extends org.rascalmpl.ast.Parameters {

	static public class Default extends org.rascalmpl.ast.Parameters.Default {
		public Default(ISourceLocation __param1, IConstructor tree, Formals __param2, KeywordFormals __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return this.getFormals().typeOf(env, instantiateTypeParameters, eval);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return null;
		}

	}

	static public class VarArgs extends org.rascalmpl.ast.Parameters.VarArgs {

		public VarArgs(ISourceLocation __param1, IConstructor tree, Formals __param2, KeywordFormals __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			Type formals = getFormals().typeOf(env, instantiateTypeParameters, eval);
			int arity = formals.getArity();

			if (arity == 0) {
				return TF.tupleType(TF.listType(TF.valueType()));
			}

			Type[] types = new Type[arity];
			int i;

			for (i = 0; i < arity - 1; i++) {
				types[i] = formals.getFieldType(i);
			}

			types[i] = TF.listType(formals.getFieldType(i));

			return TF.tupleType(types);
		}

	}

	public Parameters(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
