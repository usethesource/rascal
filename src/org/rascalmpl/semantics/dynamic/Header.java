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

import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.ModuleParameters;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;

public abstract class Header extends org.rascalmpl.ast.Header {

	static public class Default extends org.rascalmpl.ast.Header.Default {

		public Default(ISourceLocation __param1, IConstructor tree, Tags __param2, QualifiedName __param3,
				List<Import> __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			visitImports(__eval, this.getImports());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}
		
		public static void visitImports(IEvaluator<Result<IValue>> eval, List<Import> imports) {
			for (Import i : imports) {
				i.interpret(eval);
			}
		}

	}

	static public class Parameters extends org.rascalmpl.ast.Header.Parameters {

		public Parameters(ISourceLocation __param1, IConstructor tree, Tags __param2,
				QualifiedName __param3, ModuleParameters __param4,
				List<Import> __param5) {
			super(__param1, tree, __param2, __param3, __param4, __param5);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			org.rascalmpl.semantics.dynamic.Header.Default.visitImports(__eval, this.getImports());
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();
		}

	}

	public Header(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
