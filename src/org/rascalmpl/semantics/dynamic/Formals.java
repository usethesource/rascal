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
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UndeclaredType;

public abstract class Formals extends org.rascalmpl.ast.Formals {

	static public class Default extends org.rascalmpl.ast.Formals.Default {

		public Default(IConstructor __param1,
				List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env) {
			List<org.rascalmpl.ast.Expression> list = this.getFormals();
			Type[] types = new Type[list.size()];

			for (int index = 0; index < list.size(); index++) {
				org.rascalmpl.ast.Expression f = list.get(index);
				Type type = f.typeOf(env);

				if (type == null) {
					// TODO: can this actually happen?
					throw new UndeclaredType(f.getType().toString(), f);
				}
				types[index] = type;
			}

			return TypeFactory.getInstance().tupleType(types);
		}

	}

	public Formals(IConstructor __param1) {
		super(__param1);
	}
}
