/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Name;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;

public abstract class QualifiedName extends org.rascalmpl.ast.QualifiedName {

	static public class Default extends org.rascalmpl.ast.QualifiedName.Default {
		private static final TypeFactory TF = TypeFactory.getInstance();

		public Default(IConstructor __param1, List<Name> __param2) {
			super(__param1, __param2);
		}

		@Override
		public Type typeOf(Environment env) {
			if (getNames().size() == 1
					&& Names.name(getNames().get(0)).equals("_")) {
				return TF.valueType();
			} else {
				Result<IValue> varRes = env.getVariable(this);
				if (varRes == null || varRes.getType() == null) {
					return TF.valueType();
				} else {
					return varRes.getType();
				}
			}
		}
	}

	public QualifiedName(IConstructor __param1) {
		super(__param1);
	}
}
