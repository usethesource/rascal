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

import java.util.List;

import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class Signature extends org.rascalmpl.ast.Signature {

	static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {

		public NoThrows(ISourceLocation __param1, IConstructor tree,FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				 Name __param4, Parameters __param5) {
			super(__param1, tree, __param3, __param2, __param4, __param5);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Parameters parameters = getParameters();
			Type kwParams = TF.tupleEmpty();

			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				List<KeywordFormal> kwd = parameters.getKeywordFormals().getKeywordFormalList();
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, eval);
			}

			return TF.functionType(getType().typeOf(env, eval, instantiateTypeParameters), parameters.typeOf(env, eval, instantiateTypeParameters), kwParams);
		}
	}

	static public class WithThrows extends org.rascalmpl.ast.Signature.WithThrows {
		public WithThrows(ISourceLocation __param1, IConstructor tree, FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				Name __param4, Parameters __param5,
				List<org.rascalmpl.ast.Type> __param6) {
			super(__param1, tree, __param3, __param2, __param4, __param5, __param6);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type kwParams = TF.tupleEmpty();

			Parameters parameters = getParameters();

			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				List<KeywordFormal> kwd = parameters.getKeywordFormals().getKeywordFormalList();
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, eval);
			}

			return TF.functionType(getType().typeOf(env, eval, instantiateTypeParameters), getParameters()
					.typeOf(env, eval, instantiateTypeParameters), kwParams);
		}

	}

	public Signature(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
