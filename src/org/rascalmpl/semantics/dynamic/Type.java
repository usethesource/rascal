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
import org.rascalmpl.ast.TypeVar;
import org.rascalmpl.ast.UserType;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IValue;

public abstract class Type extends org.rascalmpl.ast.Type {
	static public class Basic extends org.rascalmpl.ast.Type.Basic {

		public Basic(ISourceLocation __param1, IConstructor tree, BasicType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return this.getBasic().typeOf(__eval, instantiateTypeParameters, eval);
		}

	}

	static public class Bracket extends org.rascalmpl.ast.Type.Bracket {

		public Bracket(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return this.getType().typeOf(__eval, instantiateTypeParameters, eval);

		}

	}

	static public class Function extends org.rascalmpl.ast.Type.Function {

		public Function(ISourceLocation __param1, IConstructor tree, FunctionType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return this.getFunction().typeOf(__eval, instantiateTypeParameters, eval);

		}

	}

	static public class Selector extends org.rascalmpl.ast.Type.Selector {

		public Selector(ISourceLocation __param1, IConstructor tree, DataTypeSelector __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return this.getSelector().typeOf(__eval, instantiateTypeParameters, eval);

		}

	}

	static public class Structured extends org.rascalmpl.ast.Type.Structured {

		public Structured(ISourceLocation __param1, IConstructor tree, StructuredType __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return getStructured().typeOf(__eval, instantiateTypeParameters, eval);
		}

	}

	static public class Symbol extends org.rascalmpl.ast.Type.Symbol {

		public Symbol(ISourceLocation __param1, IConstructor tree, Sym __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
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
		public org.rascalmpl.value.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return this.getUser().typeOf(__eval, instantiateTypeParameters, eval);

		}

	}

	static public class Variable extends org.rascalmpl.ast.Type.Variable {

		public Variable(ISourceLocation __param1, IConstructor tree, TypeVar __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public org.rascalmpl.value.type.Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			TypeVar var = this.getTypeVar();
			org.rascalmpl.value.type.Type param;

			if (var.isBounded()) {
				param = TF.parameterType(Names.name(var.getName()), var
						.getBound().typeOf(env, instantiateTypeParameters, eval));
			} else {
				param = TF.parameterType(Names.name(var.getName()));
			}

			if (instantiateTypeParameters) {
				return param.instantiate(env.getTypeBindings());
			}
			return param;

		}

	}

	public Type(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
