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

import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.DataTypeSelector;
import org.rascalmpl.ast.FunctionType;
import org.rascalmpl.ast.StructuredType;
import org.rascalmpl.ast.Sym;
import org.rascalmpl.ast.SyntaxRoleModifier;
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.UserType;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;

public abstract class Type extends org.rascalmpl.ast.Type {
	static public class Basic extends org.rascalmpl.ast.Type.Basic {

		public Basic(ISourceLocation __param1, IConstructor tree, BasicType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return this.getBasic().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Bracket extends org.rascalmpl.ast.Type.Bracket {

		public Bracket(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return this.getType().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Function extends org.rascalmpl.ast.Type.Function {

		public Function(ISourceLocation __param1, IConstructor tree, FunctionType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return this.getFunction().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Selector extends org.rascalmpl.ast.Type.Selector {

		public Selector(ISourceLocation __param1, IConstructor tree, DataTypeSelector __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return this.getSelector().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Structured extends org.rascalmpl.ast.Type.Structured {

		public Structured(ISourceLocation __param1, IConstructor tree, StructuredType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getStructured().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Symbol extends org.rascalmpl.ast.Type.Symbol {

		public Symbol(ISourceLocation __param1, IConstructor tree, Sym __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			// TODO: !!! where to get the right layout name for this non-terminal? It depends on where it was/is used when parsing whatever
			// is being analyzed here...
			// TODO AND: we always assume this non-terminal is not a lexical one here for some reason.
			// This will produce issue.
			return RTF.nonTerminalType(this, false, "LAYOUTNAME");
		}

	}
	
	static public class User extends org.rascalmpl.ast.Type.User {
		public User(ISourceLocation __param1, IConstructor tree, UserType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment __eval, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return this.getUser().typeOf(__eval, eval, instantiateTypeParameters);
		}
	}

	static public class Variable extends org.rascalmpl.ast.Type.Variable {
		public Variable(ISourceLocation __param1, IConstructor tree, TypeVar __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			TypeVar var = this.getTypeVar();
			io.usethesource.vallang.type.Type param;

			if (var.isBounded()) {
				param = TF.parameterType(Names.name(var.getName()), var.getBound().typeOf(env, eval, instantiateTypeParameters));
			} else {
				param = TF.parameterType(Names.name(var.getName()));
			}

			return param;
		}
	}

	static public class Modifier extends org.rascalmpl.ast.Type.Modifier {
		public Modifier(ISourceLocation src, IConstructor node, SyntaxRoleModifier modifier) {
			super(src, node, modifier);
		}

		@Override
		public io.usethesource.vallang.type.Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getModifier().typeOf(env, eval, instantiateTypeParameters);
		}
	}

	public Type(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
