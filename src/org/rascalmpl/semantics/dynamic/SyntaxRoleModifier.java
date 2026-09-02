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

import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.NonWellformedType;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public abstract class SyntaxRoleModifier extends org.rascalmpl.ast.SyntaxRoleModifier {

	static public class Syntax extends org.rascalmpl.ast.SyntaxRoleModifier.Syntax {
		public Syntax(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.TypeArg __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			try {
				return RTF.modifyToSyntax(getArg().typeOf(__eval, eval, instantiateTypeParameters));
			}
			catch (IllegalArgumentException e) {
				throw new NonWellformedType(e.getMessage(), eval.getCurrentAST());
			}
		}
	}

	static public class Lexical extends org.rascalmpl.ast.SyntaxRoleModifier.Lexical {
		public Lexical(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.TypeArg __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			try {
				return RTF.modifyToLexical(getArg().typeOf(__eval, eval, instantiateTypeParameters));
			}
			catch (IllegalArgumentException e) {
				throw new NonWellformedType(e.getMessage(), eval.getCurrentAST());
			}
		}
	}

	static public class Layout extends org.rascalmpl.ast.SyntaxRoleModifier.Layout {
		public Layout(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.TypeArg __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			try {
				return RTF.modifyToLayout(getArg().typeOf(__eval, eval, instantiateTypeParameters));
			}
			catch (IllegalArgumentException e) {
				throw new NonWellformedType(e.getMessage(), eval.getCurrentAST());
			}
		}
	}

	static public class Keyword extends org.rascalmpl.ast.SyntaxRoleModifier.Keyword {
		public Keyword(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.TypeArg __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			try {
				return RTF.modifyToKeyword(getArg().typeOf(__eval, eval, instantiateTypeParameters));
			}
			catch (IllegalArgumentException e) {
				throw new NonWellformedType(e.getMessage(), eval.getCurrentAST());
			}
		}
	}

	static public class Data extends org.rascalmpl.ast.SyntaxRoleModifier.Data {
		public Data(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.TypeArg __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			try {
				return RTF.modifyToData(getArg().typeOf(__eval, eval, instantiateTypeParameters));
			}
			catch (IllegalArgumentException e) {
				throw new NonWellformedType(e.getMessage(), eval.getCurrentAST());
			}
		}
	}

	
		
	public SyntaxRoleModifier(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
