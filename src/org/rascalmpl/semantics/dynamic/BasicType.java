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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.BasicTypeEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.NonWellformedTypeError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredTypeError;
import org.rascalmpl.values.uptr.Factory;

public abstract class BasicType extends org.rascalmpl.ast.BasicType {

	static public class Bag extends org.rascalmpl.ast.BasicType.Bag {

		public Bag(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			throw new NotYetImplemented(this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"bag should have one type argument, like bag[value].", this);

		}

	}

	static public class Bool extends org.rascalmpl.ast.BasicType.Bool {

		public Bool(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.boolType();
			}
			throw new NonWellformedTypeError(
					"bool cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.boolType();

		}

	}

	static public class DateTime extends org.rascalmpl.ast.BasicType.DateTime {

		public DateTime(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.dateTimeType();
			}
			throw new NonWellformedTypeError(
					"datetime cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			return TF.dateTimeType();
		}

	}

	static public class Int extends org.rascalmpl.ast.BasicType.Int {

		public Int(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.integerType();
			}
			throw new NonWellformedTypeError("int cannot have type arguments.",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.integerType();

		}

	}


	static public class Rational extends org.rascalmpl.ast.BasicType.Rational {

		public Rational(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.integerType();
			}
			throw new NonWellformedTypeError("rat cannot have type arguments.",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.rationalType();

		}

	}


	static public class List extends org.rascalmpl.ast.BasicType.List {

		public List(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.listType(__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedTypeError(
					"list should have exactly one type argument, like list[value]",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			throw new NonWellformedTypeError(
					"list should have one type argument, like list[value].",
					this);
		}

	}

	static public class Loc extends org.rascalmpl.ast.BasicType.Loc {

		public Loc(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.sourceLocationType();
			}
			throw new NonWellformedTypeError("loc cannot have type arguments.",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.sourceLocationType();

		}

	}

	static public class Map extends org.rascalmpl.ast.BasicType.Map {

		public Map(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 2) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.mapTypeFromTuple(__eval.__getTypeArgument());
			}
			throw new NonWellformedTypeError(
					"map should have exactly two type arguments, like map[value,value]",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"map should have at two type arguments, like map[value,value].",
					this);

		}

	}

	static public class Node extends org.rascalmpl.ast.BasicType.Node {

		public Node(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.nodeType();
			}
			throw new NonWellformedTypeError(
					"node cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.nodeType();

		}

	}

	static public class Num extends org.rascalmpl.ast.BasicType.Num {

		public Num(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.numberType();
			}
			throw new NonWellformedTypeError("num cannot have type arguments.",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			return TF.numberType();

		}

	}

	static public class Real extends org.rascalmpl.ast.BasicType.Real {

		public Real(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.realType();
			}
			throw new NonWellformedTypeError(
					"real cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			return TF.realType();
		}

	}

	static public class ReifiedAdt extends
			org.rascalmpl.ast.BasicType.ReifiedAdt {

		public ReifiedAdt(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			java.lang.String name;

			if (__eval.__getValueArguments() == null) {
				throw new ImplementationError(
						"missing value arguments to construct adt type");
			}

			if (__eval.__getValueArguments().length >= 1) {
				if (__eval.__getValueArguments()[0].getType().isStringType()) {
					name = ((IString) __eval.__getValueArguments()[0])
							.getValue();
					Type adt = __eval.__getEnv().lookupAbstractDataType(name);

					if (adt == null) {
						// TODO __eval should be a dynamic error, not a static
						// one
						throw new UndeclaredTypeError(name, this);
					}
				} else {
					throw new NonWellformedTypeError(
							"a reified adt should have a name as first argument, like adt(str name)",
							this);
				}

				if (__eval.__getValueArguments().length == 1) {
					return org.rascalmpl.interpreter.BasicTypeEvaluator
							.__getTf().abstractDataType(
									__eval.__getEnv().getStore(), name);
				}

				if (__eval.__getValueArguments().length == 2) {
					if (__eval.__getValueArguments()[1].getType().isListType()) {
						IList list = (IList) __eval.__getValueArguments()[1];
						Type[] args = new Type[list.length()];
						int i = 0;
						for (IValue arg : list) {
							Type argType = arg.getType();

							if (argType instanceof org.rascalmpl.interpreter.types.ReifiedType) {
								args[i++] = argType.getTypeParameters()
										.getFieldType(0);
							} else {
								throw new NonWellformedTypeError(
										"type parameters of an adt should be reified types, as in adt(str name, list[type[value]] parameters",
										this);
							}
						}

						return org.rascalmpl.interpreter.BasicTypeEvaluator
								.__getTf().abstractDataType(
										__eval.__getEnv().getStore(), name,
										args);
					}
				}
			}

			throw new NonWellformedTypeError(
					"a reified adt should be one of adt(str name), adt(str name, list[type[value]] parameters).",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			throw new NonWellformedTypeError(
					"a reified adt should be one of adt(str name), adt(str name, list[type[&T]] parameters), adt(str name, list[Constructor] constructors).",
					this);
		}

	}

	static public class ReifiedConstructor extends
			org.rascalmpl.ast.BasicType.ReifiedConstructor {

		public ReifiedConstructor(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			throw new ImplementationError(
					"Did not expect to handle constructors in type evaluator");

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"a reified constructor declaration should look like contructor(str name, type[&T1] arg1, ...)",
					this);

		}

	}

	static public class ReifiedFunction extends
			org.rascalmpl.ast.BasicType.ReifiedFunction {

		public ReifiedFunction(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() < 1) {
				throw new ImplementationError(
						"a reified function type has at least a return type, as in fun(int).");
			}

			throw new NotYetImplemented(this);

		}

	}

	static public class ReifiedNonTerminal extends
			org.rascalmpl.ast.BasicType.ReifiedNonTerminal {

		public ReifiedNonTerminal(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getValueArguments() == null) {
				throw new ImplementationError(
						"missing value arguments to construct non-terminal type");
			}

			if (__eval.__getValueArguments().length == 1) {
				if (__eval.__getValueArguments()[0].getType() == Factory.Symbol) {
					return org.rascalmpl.interpreter.types.RascalTypeFactory
							.getInstance()
							.reifiedType(
									org.rascalmpl.interpreter.types.RascalTypeFactory
											.getInstance()
											.nonTerminalType(
													(IConstructor) __eval
															.__getValueArguments()[0]));
				}
			}

			throw new NonWellformedTypeError(
					"a reified non-terminal type should look like non-terminal(Symbol symbol, this)",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			throw new NonWellformedTypeError(
					"a reified non-terminal type should look like non-terminal(Symbol symbol, this)",
					this);
		}

	}

	static public class ReifiedReifiedType extends
			org.rascalmpl.ast.BasicType.ReifiedReifiedType {

		public ReifiedReifiedType(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getValueArguments() == null) {
				throw new ImplementationError(
						"missing value arguments to construct non-terminal type");
			}

			if (__eval.__getValueArguments().length == 1) {
				if (__eval.__getValueArguments()[0].getType() instanceof org.rascalmpl.interpreter.types.ReifiedType) {
					return org.rascalmpl.interpreter.types.RascalTypeFactory
							.getInstance().reifiedType(
									new org.rascalmpl.interpreter.TypeReifier(VF)
											.valueToType((IConstructor) __eval
													.__getValueArguments()[0]));
				}
			}

			throw new NonWellformedTypeError(
					"a reified reified type should look like reified(type[&T] arg)",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			throw new NonWellformedTypeError(
					"a reified reified type should look like reified(type[&T] arg)",
					this);
		}

	}

	static public class ReifiedType extends
			org.rascalmpl.ast.BasicType.ReifiedType {

		public ReifiedType(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.types.RascalTypeFactory
						.getInstance().reifiedType(
								__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedTypeError(
					"type should have exactly one type argument, like type[value]",
					this);

		}

		@Override
		public Type typeOf(Environment env) {
			throw new NonWellformedTypeError(
					"type should have at one type argument, like type[value].",
					this);
		}

	}

	static public class ReifiedTypeParameter extends
			org.rascalmpl.ast.BasicType.ReifiedTypeParameter {

		public ReifiedTypeParameter(IConstructor __param1) {
			super(__param1);
		}

	}

	static public class Relation extends org.rascalmpl.ast.BasicType.Relation {

		public Relation(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
					.relTypeFromTuple(__eval.__getTypeArgument());

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"rel should have at least one type argument, like rel[value,value].",
					this);

		}

	}

	static public class Set extends org.rascalmpl.ast.BasicType.Set {

		public Set(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 1) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.setType(__eval.__getTypeArgument().getFieldType(0));
			}
			throw new NonWellformedTypeError(
					"set should have exactly one type argument, like set[value]",
					this);

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"set should have one type argument, like set[value].", this);

		}

	}

	static public class String extends org.rascalmpl.ast.BasicType.String {

		public String(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.stringType();
			}
			throw new NonWellformedTypeError(
					"string cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			return TF.stringType();

		}

	}

	static public class Tuple extends org.rascalmpl.ast.BasicType.Tuple {

		public Tuple(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			return __eval.__getTypeArgument();

		}

		@Override
		public Type typeOf(Environment __eval) {

			throw new NonWellformedTypeError(
					"tuple should have type arguments, like tuple[value,value].",
					this);

		}

	}

	static public class Value extends org.rascalmpl.ast.BasicType.Value {

		public Value(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {

			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.valueType();
			}
			throw new NonWellformedTypeError(
					"value cannot have type arguments.", this);

		}

		@Override
		public Type typeOf(Environment __eval) {
			return TF.valueType();

		}

	}

	static public class Void extends org.rascalmpl.ast.BasicType.Void {

		public Void(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Type __evaluate(BasicTypeEvaluator __eval) {
			if (__eval.__getTypeArgument().getArity() == 0) {
				return org.rascalmpl.interpreter.BasicTypeEvaluator.__getTf()
						.voidType();
			}
			throw new NonWellformedTypeError(
					"void cannot have type arguments.", this);
		}

		@Override
		public Type typeOf(Environment __eval) {
			return TF.voidType();
		}

	}

	public BasicType(IConstructor __param1) {
		super(__param1);
	}
}
