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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public abstract class Signature extends org.rascalmpl.ast.Signature {

	static public class NoThrows extends org.rascalmpl.ast.Signature.NoThrows {

		public NoThrows(IConstructor __param1,FunctionModifiers __param3, org.rascalmpl.ast.Type __param2,
				 Name __param4, Parameters __param5) {
			super(__param1, __param3, __param2, __param4, __param5);
		}

		@Override
		public Type typeOf(Environment env) {
			RascalTypeFactory RTF = org.rascalmpl.interpreter.types.RascalTypeFactory
					.getInstance();
			return RTF.functionType(getType().typeOf(env), getParameters()
					.typeOf(env));
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
		public Type typeOf(Environment env) {
			RascalTypeFactory RTF = RascalTypeFactory.getInstance();
			return RTF.functionType(getType().typeOf(env), getParameters()
					.typeOf(env));
		}

	}

	public Signature(IConstructor __param1) {
		super(__param1);
	}
}
