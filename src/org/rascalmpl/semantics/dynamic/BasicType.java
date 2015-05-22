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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.BasicTypeEvaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.NonWellformedType;

public abstract class BasicType extends org.rascalmpl.ast.BasicType {

	static public class Bag extends org.rascalmpl.ast.BasicType.Bag {

		public Bag(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			throw new NotYetImplemented(this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"bag should have one type argument, like bag[value].", this);

		}

	}

	static public class Bool extends org.rascalmpl.ast.BasicType.Bool {

		public Bool(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.boolType();
			}
			throw new NonWellformedType(
					"bool cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.boolType();

		}

	}

	static public class DateTime extends org.rascalmpl.ast.BasicType.DateTime {

		public DateTime(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.dateTimeType();
			}
			throw new NonWellformedType(
					"datetime cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return TF.dateTimeType();
		}

	}

	static public class Int extends org.rascalmpl.ast.BasicType.Int {

		public Int(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.integerType();
			}
			throw new NonWellformedType("int cannot have type arguments.",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.integerType();

		}

	}


	static public class Rational extends org.rascalmpl.ast.BasicType.Rational {

		public Rational(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.integerType();
			}
			throw new NonWellformedType("rat cannot have type arguments.",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.rationalType();

		}

	}


	static public class List extends org.rascalmpl.ast.BasicType.List {

		public List(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.listType(__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedType(
					"list should have exactly one type argument, like list[value]",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			throw new NonWellformedType(
					"list should have one type argument, like list[value].",
					this);
		}

	}

	static public class Loc extends org.rascalmpl.ast.BasicType.Loc {

		public Loc(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.sourceLocationType();
			}
			throw new NonWellformedType("loc cannot have type arguments.",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.sourceLocationType();

		}

	}

	static public class Map extends org.rascalmpl.ast.BasicType.Map {

		public Map(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 2) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.mapTypeFromTuple(__eval.__getTypeArgument());
			}
			throw new NonWellformedType(
					"map should have exactly two type arguments, like map[value,value]",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"map should have at two type arguments, like map[value,value].",
					this);

		}

	}

	static public class Node extends org.rascalmpl.ast.BasicType.Node {

		public Node(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.nodeType();
			}
			throw new NonWellformedType(
					"node cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.nodeType();

		}

	}

	static public class Num extends org.rascalmpl.ast.BasicType.Num {

		public Num(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.numberType();
			}
			throw new NonWellformedType("num cannot have type arguments.",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			return TF.numberType();

		}

	}

	static public class Real extends org.rascalmpl.ast.BasicType.Real {

		public Real(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.realType();
			}
			throw new NonWellformedType(
					"real cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return TF.realType();
		}

	}

	static public class Type extends
			org.rascalmpl.ast.BasicType.Type {

		public Type(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.types.RascalTypeFactory
						.getInstance().reifiedType(
								__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedType(
					"type should have exactly one type argument, like type[value]",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			throw new NonWellformedType(
					"type should have at one type argument, like type[value].",
					this);
		}

	}

	static public class Relation extends org.rascalmpl.ast.BasicType.Relation {

		public Relation(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
					.relTypeFromTuple(__eval.__getTypeArgument());

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"rel should have at least one type argument, like rel[value,value].",
					this);

		}

	}
	
	static public class ListRelation extends org.rascalmpl.ast.BasicType.ListRelation {

		public ListRelation(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
					.lrelTypeFromTuple(__eval.__getTypeArgument());

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"lrel should have at least one type argument, like lrel[value,value].",
					this);

		}

	}

	static public class Set extends org.rascalmpl.ast.BasicType.Set {

		public Set(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.setType(__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedType(
					"set should have exactly one type argument, like set[value]",
					this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"set should have one type argument, like set[value].", this);

		}

	}

	static public class String extends org.rascalmpl.ast.BasicType.String {

		public String(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.stringType();
			}
			throw new NonWellformedType(
					"string cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return TF.stringType();

		}

	}

	static public class Tuple extends org.rascalmpl.ast.BasicType.Tuple {

		public Tuple(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			return __eval.__getTypeArgument();

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {

			throw new NonWellformedType(
					"tuple should have type arguments, like tuple[value,value].",
					this);

		}

	}

	static public class Value extends org.rascalmpl.ast.BasicType.Value {

		public Value(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.valueType();
			}
			throw new NonWellformedType(
					"value cannot have type arguments.", this);

		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return TF.valueType();

		}

	}

	static public class Void extends org.rascalmpl.ast.BasicType.Void {

		public Void(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type __evaluate(BasicTypeEvaluator __eval) {
			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.voidType();
			}
			throw new NonWellformedType(
					"void cannot have type arguments.", this);
		}

		@Override
		public org.eclipse.imp.pdb.facts.type.Type typeOf(Environment __eval, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return TF.voidType();
		}

	}

	public BasicType(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
