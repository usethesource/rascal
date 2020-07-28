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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class Sym extends org.rascalmpl.ast.Sym {

	static public class Nonterminal extends org.rascalmpl.ast.Sym.Nonterminal {

		public Nonterminal(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Nonterminal __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getNonterminal().typeOf(env, eval, instantiateTypeParameters);
		}
	}

	public Sym(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
