/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.List;
import java.util.Stack;

import org.rascalmpl.ast.Assignable;
import org.rascalmpl.ast.Bound;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Catch;
import org.rascalmpl.ast.DataTarget;
import org.rascalmpl.ast.Label;
import org.rascalmpl.ast.LocalVariableDeclaration;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Target;
import org.rascalmpl.ast.Type;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.control_exceptions.BreakException;
import org.rascalmpl.interpreter.control_exceptions.ContinueException;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnguardedAppend;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;
import org.rascalmpl.interpreter.utils.Cases;
import org.rascalmpl.interpreter.utils.Cases.CaseBlock;
import org.rascalmpl.interpreter.utils.Names;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;

public abstract class Statement extends org.rascalmpl.ast.Statement {

	static public class Append extends org.rascalmpl.ast.Statement.Append {
		public Append(ISourceLocation src, IConstructor node, DataTarget __param2,
				org.rascalmpl.ast.Statement __param3) {
			super(src, node, __param2, __param3);
		}
		
		protected Accumulator getTarget(IEvaluator<Result<IValue>> __eval) {
			if (__eval.__getAccumulators().empty()) { 
				throw new UnguardedAppend(this);
			}
			if (!this.getDataTarget().isEmpty()) {
				String label = org.rascalmpl.interpreter.utils.Names.name(this.getDataTarget().getLabel());
				Accumulator accu = findDataTarget(__eval, label);
				if (accu == null) {
					throw new UnguardedAppend(this); // TODO: better error
				}
				return accu;
			}
			return __eval.__getAccumulators().peek();
		}

		private Accumulator findDataTarget(IEvaluator<Result<IValue>> __eval,
				String label) {
			Stack<Accumulator> accus = __eval.__getAccumulators();
			
			// Search backwards, to allow nested fors with same label.
			for (int i = accus.size() - 1; i >= 0; i--) {
				Accumulator accu = accus.get(i);
				if (accu.hasLabel(label)) {
					return accu;
				}
			}
			return null;
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			Accumulator target = null;
			if (__eval.__getAccumulators().empty()) {
				throw new UnguardedAppend(this);
			}
			if (!this.getDataTarget().isEmpty()) {
				String label = org.rascalmpl.interpreter.utils.Names.name(this
						.getDataTarget().getLabel());
				target = findDataTarget(__eval, label);
				if (target == null) {
					throw new UnguardedAppend(this); // TODO: better error
					// message
				}
			} else {
				target = __eval.__getAccumulators().peek();
			}
			Result<IValue> result = this.getStatement().interpret(__eval);
			target.append(result);
			return result;

		}

	}

	static public class Assert extends org.rascalmpl.ast.Statement.Assert {

		public Assert(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			Result<IValue> r = this.getExpression().interpret(__eval);
			if (!r.getStaticType().equals(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedType(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.boolType(), r.getStaticType(), this);
			}

			if (r.getValue().equals(__eval.__getVf().bool(false))) {
				throw org.rascalmpl.exceptions.RuntimeExceptionFactory
						.assertionFailed(this, __eval.getStackTrace());
			}
			return r;

		}

	}

	static public class AssertWithMessage extends
			org.rascalmpl.ast.Statement.AssertWithMessage {

		public AssertWithMessage(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			Result<IValue> r = this.getExpression().interpret(__eval);
			if (!r.getStaticType().equals(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedType(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.boolType(), r.getStaticType(), this);
			}
			if (r.getValue().equals(__eval.__getVf().bool(false))) {
				Result<IValue> msgValue = this.getMessage().interpret(__eval);
//				IString msg = __eval.__getVf().string(
//						org.rascalmpl.interpreter.utils.StringUtils.unescapeBase(msgValue.getValue().toString());
				throw org.rascalmpl.exceptions.RuntimeExceptionFactory
						.assertionFailed((IString) msgValue.getValue(), __eval.getCurrentAST(), __eval
								.getStackTrace());
			}
			return r;

		}

	}

	static public class Assignment extends
			org.rascalmpl.ast.Statement.Assignment {

		public Assignment(ISourceLocation __param1, IConstructor tree, Assignable __param2,
				org.rascalmpl.ast.Assignment __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			
			Result<IValue> right = this.getStatement().interpret(__eval);
//			if(this.getAssignable().isSlice() && !this.getOperator().isDefault()){
//				throw new UnsupportedOperation("Slicing assignment only implemented for simple assignment operator (=)", __eval.getCurrentAST());
//			}
			return this.getAssignable().assignment(
					new AssignableEvaluator(__eval.getCurrentEnvt(), this
							.getOperator(), right, __eval));

		}

	}

	static public class Break extends org.rascalmpl.ast.Statement.Break {

		public Break(ISourceLocation __param1, IConstructor tree, Target __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			if (getTarget().isEmpty()) {
				throw new BreakException();
			}
			else {
				throw new BreakException(Names.name(getTarget().getName()));
			}
			
		}

	}

	static public class Continue extends org.rascalmpl.ast.Statement.Continue {

		public Continue(ISourceLocation __param1, IConstructor tree, Target __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			if (getTarget().isEmpty()) {
				throw new ContinueException();
			}
			else {
				throw new ContinueException(Names.name(getTarget().getName()));
			}

		}

	}

	static public class DoWhile extends org.rascalmpl.ast.Statement.DoWhile {

		public DoWhile(ISourceLocation __param1, IConstructor tree, Label __param2,
				org.rascalmpl.ast.Statement __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			org.rascalmpl.ast.Statement body = this.getBody();
			org.rascalmpl.ast.Expression generator = this.getCondition();
			IBooleanResult gen;
			Environment old = __eval.getCurrentEnvt();
			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this
						.getLabel().getName());
			}
			__eval.__getAccumulators().push(
					new Accumulator(__eval.__getVf(), label));
			IValue value = null;
			try {
				while (true) {
					try {
						try {
							body.interpret(__eval);
						}
						catch (BreakException e) {
							value = __eval.__getAccumulators().pop().done();
							return org.rascalmpl.interpreter.result.ResultFactory
									.makeResult(value.getType(), value, __eval);
						}
						catch (ContinueException e) {
							// just continue;
						}
	
						gen = generator.getBacktracker(__eval);
						gen.init();
						if (__eval.isInterrupted()) {
							throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
						}
						if (!(gen.hasNext() && gen.next())) {
							value = __eval.__getAccumulators().pop().done();
							return org.rascalmpl.interpreter.result.ResultFactory
									.makeResult(value.getType(), value, __eval);
						}
					} finally {
						__eval.unwind(old);
					}
				}
			}
			finally {
				if (value == null) {
					// make sure to pop the accumulators even in the case of exceptions
					__eval.__getAccumulators().pop().done();
				}
			}
		}

	}

	static public class EmptyStatement extends
			org.rascalmpl.ast.Statement.EmptyStatement {

		public EmptyStatement(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Expression extends
			org.rascalmpl.ast.Statement.Expression {

		public Expression(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			Environment old = __eval.getCurrentEnvt();

			try {
				__eval.pushEnv();
				return this.getExpression().interpret(__eval);
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Fail extends org.rascalmpl.ast.Statement.Fail {

		public Fail(ISourceLocation __param1, IConstructor tree, Target __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			if (!this.getTarget().isEmpty()) {
				throw new Failure(Names.name(this.getTarget().getName()));
			}

			throw new Failure();

		}

	}

	static public class Filter extends org.rascalmpl.ast.Statement.Filter {

		public Filter(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}
		
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			throw new Filtered();
		
		}

	}

	static public class For extends org.rascalmpl.ast.Statement.For {

		public For(ISourceLocation __param1, IConstructor tree, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			org.rascalmpl.ast.Statement body = this.getBody();
			List<org.rascalmpl.ast.Expression> generators = this
					.getGenerators();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment old = __eval.getCurrentEnvt();
			Environment[] olds = new Environment[size];

			Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory
					.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf()
							.voidType(), __eval.__getVf().list(), __eval);

			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this
						.getLabel().getName());
			}
			__eval.__getAccumulators().push(
					new Accumulator(__eval.__getVf(), label));

			// TODO: does this prohibit that the body influences the behavior
			// of the generators??

			int i = 0;
			boolean normalCflow = false;
			try {
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();

				while (i >= 0 && i < size) {
					if (__eval.isInterrupted()) {
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							// NB: no result handling here.
							__eval.setCurrentAST(body);
							
							try {
								body.interpret(__eval);
							}
							catch (Failure e) {
								// TODO: failure should undo variable assignments outside the scope of the for!
								if (!e.hasLabel()) { 
									continue;
								}
								else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
									continue;
								}

								throw e;
							}
							catch (ContinueException e) {
								if (!e.hasLabel() && getLabel().isEmpty()) { 
									continue;
								}
								else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
									continue;
								}

								throw e;
							}
							catch (BreakException e) {
								if (!e.hasLabel() && getLabel().isEmpty()) { 
									break;
									
								}
								else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
									break;
								}

								throw e;
							}
						} else {
							i++;
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
							gens[i] = generators.get(i).getBacktracker(__eval);
							gens[i].init();
						}
					} else {
						__eval.unwind(olds[i]);
						i--;
					}
				}
				// TODO: this is not enough, we must also detect
				// break and return a list result then.
				normalCflow = true;
			} finally {
				IValue value = __eval.__getAccumulators().pop().done();
				if (normalCflow) {
					result = org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(value.getType(), value, __eval);
				}
				__eval.unwind(old);
			}
			return result;

		}

	}

	static public class FunctionDeclaration extends
			org.rascalmpl.ast.Statement.FunctionDeclaration {

		public FunctionDeclaration(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.FunctionDeclaration __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			return this.getFunctionDeclaration().interpret(__eval);

		}

	}

	static public class GlobalDirective extends
			org.rascalmpl.ast.Statement.GlobalDirective {

		public GlobalDirective(ISourceLocation __param1, IConstructor tree, Type __param2,
				List<QualifiedName> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			throw new NotYetImplemented(this); // TODO
		}

	}

	static public class IfThen extends org.rascalmpl.ast.Statement.IfThen {

		public IfThen(ISourceLocation __param1, IConstructor tree, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this
					.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();
			
			int i = 0;
			try {
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();

				while (i >= 0 && i < size) {

					if (__eval.isInterrupted()) {
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							try {
								return body.interpret(__eval);
							}
							catch (Failure e) {
								if (!e.hasLabel()) { 
									continue;
								}
								else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
									continue;
								}

								throw e;
							}
						}
						else {
							i++;
							gens[i] = generators.get(i).getBacktracker(__eval);
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						__eval.pushEnv();
						i--;
					}
				}
			} finally {
				__eval.unwind(old);
			}
			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class IfThenElse extends
			org.rascalmpl.ast.Statement.IfThenElse {

		public IfThenElse(ISourceLocation __param1, IConstructor tree, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4,
				org.rascalmpl.ast.Statement __param5) {
			super(__param1, tree, __param2, __param3, __param4, __param5);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this
					.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			int i = 0;
			try {
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();

				while (i >= 0 && i < size) {

					if (__eval.isInterrupted()) {
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							try {
								return body.interpret(__eval);
							}
							catch (Failure e) {
								if (!e.hasLabel()) { 
									continue;
								}
								else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
									continue;
								}

								throw e;
							}
						}
						else {
							i++;
							gens[i] = generators.get(i).getBacktracker(__eval);
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						__eval.pushEnv();
						i--;
					}
				}
			} finally {
				__eval.unwind(old);
			}

			org.rascalmpl.ast.Statement elsePart = this.getElseStatement();
			__eval.setCurrentAST(elsePart);
			return elsePart.interpret(__eval);

		}

	}

	static public class Insert extends org.rascalmpl.ast.Statement.Insert {

		public Insert(ISourceLocation __param1, IConstructor tree, DataTarget __param2,
				org.rascalmpl.ast.Statement __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			throw new org.rascalmpl.interpreter.control_exceptions.Insert(this
					.getStatement().interpret(__eval));

		}

	}

	static public class NonEmptyBlock extends
			org.rascalmpl.ast.Statement.NonEmptyBlock {

		public NonEmptyBlock(ISourceLocation __param1, IConstructor tree, Label __param2,
				List<org.rascalmpl.ast.Statement> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			Result<IValue> r = org.rascalmpl.interpreter.result.ResultFactory
					.nothing();
			Environment old = __eval.getCurrentEnvt();

			__eval.pushEnv(this);
			try {
				for (org.rascalmpl.ast.Statement stat : this.getStatements()) {
					__eval.setCurrentAST(stat);
					r = stat.interpret(__eval);
				}
			} finally {
				__eval.unwind(old);
			}
			return r;

		}

	}

	static public class Return extends org.rascalmpl.ast.Statement.Return {

		public Return(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Statement __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
            throw new org.rascalmpl.interpreter.control_exceptions.Return(getStatement().interpret(__eval), this.getStatement().getLocation());

		}

	}

	static public class Solve extends org.rascalmpl.ast.Statement.Solve {

		public Solve(ISourceLocation __param1, IConstructor tree, List<QualifiedName> __param2,
				Bound __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			int size = this.getVariables().size();
			QualifiedName vars[] = new QualifiedName[size];
			IValue currentValue[] = new IValue[size];

			Environment old = __eval.getCurrentEnvt();

			try {
				List<QualifiedName> varList = this.getVariables();

				for (int i = 0; i < size; i++) {
					QualifiedName var = varList.get(i);
					vars[i] = var;
					Result<IValue> tmp = __eval.getCurrentEnvt().getSimpleVariable(var);
					
					if (tmp == null) {
						throw new UndeclaredVariable(Names.fullName(var), var);
					}
					if (tmp.getValue() == null) {
						throw new UninitializedVariable(Names.fullName(var),
								var);
					}
					currentValue[i] = tmp.getValue();
				}

				__eval.pushEnv();
				org.rascalmpl.ast.Statement body = this.getBody();

				int max = -1;

				Bound bound = this.getBound();
				if (bound.isDefault()) {
					Result<IValue> res = bound.getExpression()
							.interpret(__eval);
					if (!res.getStaticType().isInteger()) {
						throw new UnexpectedType(
								org.rascalmpl.interpreter.Evaluator.__getTf()
										.integerType(), res.getStaticType(), this);
					}
					max = ((IInteger) res.getValue()).intValue();
					if (max <= 0) {
						throw org.rascalmpl.exceptions.RuntimeExceptionFactory
								.indexOutOfBounds((IInteger) res.getValue(),
										__eval.getCurrentAST(), __eval
												.getStackTrace());
					}
				}

				Result<IValue> bodyResult = null;

				boolean change = true;
				int iterations = 0;

				while (change && (max == -1 || iterations < max)) {
					change = false;
					iterations++;
					if (__eval.isInterrupted()) {
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					}
					bodyResult = body.interpret(__eval);
					for (int i = 0; i < size; i++) {
						QualifiedName var = vars[i];
						Result<IValue> v = __eval.getCurrentEnvt().getVariable(
								var);
						if (currentValue[i] == null
								|| !v.getValue().equals(currentValue[i])) {
							change = true;
							currentValue[i] = v.getValue();
						}
					}
				}
				return bodyResult;
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Switch extends org.rascalmpl.ast.Statement.Switch {
		private List<CaseBlock> blocks;

		public Switch(ISourceLocation __param1, IConstructor tree, Label __param2,
				org.rascalmpl.ast.Expression __param3, List<Case> cases) {
			super(__param1, tree, __param2, __param3, cases);
			blocks = Cases.precompute(cases, false);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			Result<IValue> subject = this.getExpression().interpret(__eval);

			for (CaseBlock cs : blocks) {
				if (cs.matchAndEval(__eval, subject)) {
					return org.rascalmpl.interpreter.result.ResultFactory
							.nothing();
				}
			}

			return ResultFactory.nothing();
		}
	}

	static public class Throw extends org.rascalmpl.ast.Statement.Throw {

		public Throw(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Statement __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			throw new org.rascalmpl.exceptions.Throw(this
					.getStatement().interpret(__eval).getValue(), __eval
					.getCurrentAST().getLocation(), __eval.getStackTrace());
		}

	}

	static public class Try extends org.rascalmpl.ast.Statement.Try {

		public Try(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Statement __param2,
				List<Catch> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			return evalStatementTry(__eval, this.getBody(), this.getHandlers(), null);
		}
		
		static public Result<IValue> evalStatementTry(IEvaluator<Result<IValue>> eval, org.rascalmpl.ast.Statement body, java.util.List<Catch> handlers, org.rascalmpl.ast.Statement finallyBody) {
			Result<IValue> res = org.rascalmpl.interpreter.result.ResultFactory.nothing();

			try {
				res = body.interpret(eval);
			} catch (org.rascalmpl.exceptions.Throw e) {
				IValue eValue = e.getException();

				boolean handled = false;

				for (Catch c : handlers) {
					if (c.isDefault()) {
						res = c.getBody().interpret(eval);
						handled = true;
						break;
					}
 
					if (Cases.matchAndEval(makeResult(eValue.getType(), eValue, eval), c.getPattern().buildMatcher(eval, false), c.getBody(), eval)) {
						handled = true;
						break;
					}
				}

				if (!handled)
					throw e;
			} 
			catch (RascalStackOverflowError e) {
				// and now we pretend as if a real Stackoverflow() value has been thrown, such that 
				// it can be caugt in this catch block if necessary:
				boolean handled = false;

				for (Catch c : handlers) {
					if (c.hasPattern() && isCatchStackOverflow(c.getPattern())) {
					    IValue pseudo = e.makeThrow().getException();

						if (Cases.matchAndEval(makeResult(pseudo.getType(), pseudo, eval), c.getPattern().buildMatcher(eval, false), c.getBody(), eval)) {
							handled = true;
							break;
						}
					}
				}

				if (!handled) {
					// we rethrow because higher up the stack may be another catch block
					throw e;
				}	
			}
			finally {
				if (finallyBody != null) {
					finallyBody.interpret(eval);
				}
			}
			return res;
		}

		private static boolean isCatchStackOverflow(org.rascalmpl.ast.Expression pattern) {
			if (pattern.isVariableBecomes() || pattern.isTypedVariableBecomes()) {
				return isCatchStackOverflow(pattern.getPattern());
			}
			else if (pattern.isCallOrTree()) {
				var called = pattern.getExpression();
				if (called.isQualifiedName()) {
					var qname = called.getQualifiedName();
					return pattern.getArguments().isEmpty() && "StackOverflow".equals(Names.consName(qname));
				}
			}
			
			return false;
		}
	}

	static public class TryFinally extends
			org.rascalmpl.ast.Statement.TryFinally {

		public TryFinally(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Statement __param2,
				List<org.rascalmpl.ast.Catch> __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			return org.rascalmpl.semantics.dynamic.Statement.Try.evalStatementTry(__eval, this.getBody(), this.getHandlers(),
					this.getFinallyBody());
		
		}
	}

	static public class VariableDeclaration extends
			org.rascalmpl.ast.Statement.VariableDeclaration {

		public VariableDeclaration(ISourceLocation __param1, IConstructor tree,
				LocalVariableDeclaration __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			return this.getDeclaration().interpret(__eval);

		}

	}

	static public class Visit extends org.rascalmpl.ast.Statement.Visit {

		public Visit(ISourceLocation __param1, IConstructor tree, Label __param2,
				org.rascalmpl.ast.Visit __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);
			
			return this.getVisit().interpret(__eval);

		}

	}

	static public class While extends org.rascalmpl.ast.Statement.While {

		public While(ISourceLocation __param1, IConstructor tree, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			IValue value = null;
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);

			org.rascalmpl.ast.Statement body = this.getBody();
			List<org.rascalmpl.ast.Expression> generators = this
					.getConditions();

			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this
						.getLabel().getName());
			}
			__eval.__getAccumulators().push(
					new Accumulator(__eval.__getVf(), label));
			try {
				// a while statement is different from a for statement, the body of
				// the while can influence the
				// variables that are used to test the condition of the loop
				// while does not iterate over all possible matches, rather it
				// produces every time the first match
				// that makes the condition true
				loop: while (true) {
					int i = 0;
					try {
						if (__eval.isInterrupted()) {
							throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
						}
						
						olds[0] = __eval.getCurrentEnvt();
						gens[0] = generators.get(0).getBacktracker(__eval);
						gens[0].init();
	
						conditions:while (i >= 0 && i < size) {
							__eval.unwind(olds[i]);
							__eval.pushEnv();
	
							if (__eval.isInterrupted()) {
								throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
							}
							if (gens[i].hasNext() && gens[i].next()) {
								if (i == size - 1) {
									__eval.setCurrentAST(body);
									
									try {
										body.interpret(__eval);
										continue loop;
									}
									catch (Failure e) {
										// try next assignment of generators!
										// TODO: failure should undo assignment
										if (!e.hasLabel() && getLabel().isEmpty()) { 
											continue conditions;
										}
										else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
											continue conditions;
										}
	
										throw e;
									}
									catch (ContinueException e) {
										// try next assignment of generators!
										if (!e.hasLabel() && getLabel().isEmpty()) { 
											continue loop;
										}
										else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
											continue loop;
										}
	
										throw e;
									}
									catch (BreakException e) {
										if (!e.hasLabel() && getLabel().isEmpty()) { 
											value = __eval.__getAccumulators().pop().done();
											return org.rascalmpl.interpreter.result.ResultFactory
													.makeResult(value.getType(), value, __eval);
										}
										else if (!getLabel().isEmpty() && e.getLabel().equals(Names.name(getLabel().getName()))) {
											value = __eval.__getAccumulators().pop().done();
											return org.rascalmpl.interpreter.result.ResultFactory
													.makeResult(value.getType(), value, __eval);
										}
	
										throw e;
									}
								}
								else {
									i++;
									gens[i] = generators
											.get(i).getBacktracker(__eval);
									gens[i].init();
									olds[i] = __eval.getCurrentEnvt();
								}
							} else {
								i--;
							}
						}
					} finally {
						__eval.unwind(old);
					}
					value = __eval.__getAccumulators().pop().done();
					return org.rascalmpl.interpreter.result.ResultFactory
							.makeResult(value.getType(), value, __eval);
				}
			}
			finally	 {
				if (value == null) {
					// make sure to always pop the accumulator when it hasn't been popped yet
					__eval.__getAccumulators().pop().done();
				}
			}

		}

	}

	public Statement(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
}
