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

import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.TypeUtils;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class FunctionType extends org.rascalmpl.ast.FunctionType {

	static public class TypeArguments extends
			org.rascalmpl.ast.FunctionType.TypeArguments {

		public TypeArguments(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2,
				List<TypeArg> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type returnType = this.getType().typeOf(__eval, eval, instantiateTypeParameters);
			Type argTypes = TypeUtils.typeOf(this.getArguments(), __eval);
			return TF.functionType(returnType, argTypes, TF.tupleEmpty());
		}
	}

	public FunctionType(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
