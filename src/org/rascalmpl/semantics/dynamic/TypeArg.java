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

import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;

public abstract class TypeArg extends org.rascalmpl.ast.TypeArg {

	static public class Default extends org.rascalmpl.ast.TypeArg.Default {

		public Default(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return this.getType().typeOf(__eval, instantiateTypeParameters, eval);
		}

	}

	static public class Named extends org.rascalmpl.ast.TypeArg.Named {

		public Named(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2,
				Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return this.getType().typeOf(__eval, instantiateTypeParameters, eval);
		}

	}

	public TypeArg(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
