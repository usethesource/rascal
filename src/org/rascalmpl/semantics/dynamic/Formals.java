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

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public abstract class Formals extends org.rascalmpl.ast.Formals {

	static public class Default extends org.rascalmpl.ast.Formals.Default {

		public Default(ISourceLocation __param1, IConstructor tree,
				List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			List<org.rascalmpl.ast.Expression> list = this.getFormals();
			Type[] types = new Type[list.size()];

			for (int index = 0; index < list.size(); index++) {
				types[index] = list.get(index).typeOf(env, instantiateTypeParameters, eval);
			}

			return TypeFactory.getInstance().tupleType(types);
		}

	}

	public Formals(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
