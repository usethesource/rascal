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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.env.Environment;

public abstract class Sym extends org.rascalmpl.ast.Sym {

	static public class Nonterminal extends org.rascalmpl.ast.Sym.Nonterminal {

		public Nonterminal(IConstructor __param1,
				org.rascalmpl.ast.Nonterminal __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters) {
			return getNonterminal().typeOf(env, instantiateTypeParameters);
		}
	}

	public Sym(IConstructor __param1) {
		super(__param1);
	}
}
