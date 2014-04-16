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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IKeywordParameterInitializer;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public abstract class Signature extends org.rascalmpl.ast.Signature {

	static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {

		public NoThrows(IConstructor __param1,FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				 Name __param4, Parameters __param5) {
			super(__param1, __param3, __param2, __param4, __param5);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			RascalTypeFactory RTF = org.rascalmpl.interpreter.types.RascalTypeFactory
					.getInstance();
			Parameters parameters = getParameters();
			Type kwParams = TF.voidType();
			java.util.Map<String,IKeywordParameterInitializer> kwDefaults = new HashMap<>();

			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				List<KeywordFormal> kwd = parameters.getKeywordFormals().getKeywordFormalList();
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, eval);
				kwDefaults = TypeDeclarationEvaluator.interpretKeywordParameters(kwd, kwParams, eval);
			}

			return RTF.functionType(getType().typeOf(env, instantiateTypeParameters, eval), parameters
					.typeOf(env, instantiateTypeParameters, eval), kwParams, kwDefaults);
		}
	}

	static public class WithThrows extends
			org.rascalmpl.ast.Signature.WithThrows {
		public WithThrows(IConstructor __param1, FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				 Name __param4, Parameters __param5,
				List<org.rascalmpl.ast.Type> __param6) {
			super(__param1, __param3, __param2, __param4, __param5, __param6);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			RascalTypeFactory RTF = RascalTypeFactory.getInstance();
			
			java.util.Map<String,Type> kwParams = new HashMap<>();
      java.util.Map<String,IValue> kwDefaults = new HashMap<>();
      
      Parameters parameters = getParameters();
      
      if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
        interpretKeywordParameters(parameters.getKeywordFormals().getKeywordFormalList(), kwParams, kwDefaults, env, eval);
      }
      
			return RTF.functionType(getType().typeOf(env, instantiateTypeParameters, eval), getParameters()
					.typeOf(env, instantiateTypeParameters, eval), kwParams, kwDefaults);
		}

	}

	public Signature(IConstructor __param1) {
		super(__param1);
	}
}
