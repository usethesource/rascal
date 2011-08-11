/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Assignable;
import org.rascalmpl.ast.Bound;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Catch;
import org.rascalmpl.ast.DataTarget;
import org.rascalmpl.ast.Label;
import org.rascalmpl.ast.LocalVariableDeclaration;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.StringConstant;
import org.rascalmpl.ast.Target;
import org.rascalmpl.ast.Type;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class Statement extends org.rascalmpl.ast.Statement {

	static public class Append extends org.rascalmpl.ast.Statement.Append {

		public Append(IConstructor __param1, DataTarget __param2,
				org.rascalmpl.ast.Statement __param3) {
			super(__param1, __param2, __param3);
		}

		protected Accumulator getTarget(Evaluator __eval) {
			if (__eval.__getAccumulators().empty()) { 
				throw new AppendWithoutLoop(this);
			}
			if (!this.getDataTarget().isEmpty()) {
				String label = org.rascalmpl.interpreter.utils.Names.name(this.getDataTarget().getLabel());
				for (Accumulator accu : __eval.__getAccumulators()) {
					if (accu.hasLabel(label)) {
						return accu;
					}
				}
				throw new AppendWithoutLoop(this); // TODO: better error
			}
			return __eval.__getAccumulators().peek();
		}
		
		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Accumulator target = null;
			if (__eval.__getAccumulators().empty()) {
				throw new AppendWithoutLoop(this);
			}
			if (!this.getDataTarget().isEmpty()) {
				String label = org.rascalmpl.interpreter.utils.Names.name(this
						.getDataTarget().getLabel());
				for (Accumulator accu : __eval.__getAccumulators()) {
					if (accu.hasLabel(label)) {
						target = accu;
						break;
					}
				}
				if (target == null) {
					throw new AppendWithoutLoop(this); // TODO: better error
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

		public Assert(IConstructor __param1, org.rascalmpl.ast.Expression __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> r = this.getExpression().interpret(__eval);
			if (!r.getType().equals(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedTypeError(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.boolType(), r.getType(), this);
			}

			if (r.getValue().isEqual(__eval.__getVf().bool(false))) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.assertionFailed(this, __eval.getStackTrace());
			}
			return r;

		}

	}

	static public class AssertWithMessage extends
			org.rascalmpl.ast.Statement.AssertWithMessage {

		public AssertWithMessage(IConstructor __param1,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> r = this.getExpression().interpret(__eval);
			if (!r.getType().equals(
					org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedTypeError(
						org.rascalmpl.interpreter.Evaluator.__getTf()
								.boolType(), r.getType(), this);
			}
			if (r.getValue().isEqual(__eval.__getVf().bool(false))) {
				Result<IValue> msgValue = this.getMessage().interpret(__eval);
				IString msg = __eval.__getVf().string(
						org.rascalmpl.interpreter.utils.StringUtils.unescape(
								msgValue.getValue().toString(), this, __eval
										.getCurrentEnvt()));
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
						.assertionFailed(msg, __eval.getCurrentAST(), __eval
								.getStackTrace());
			}
			return r;

		}

	}

	static public class Assignment extends
			org.rascalmpl.ast.Statement.Assignment {

		public Assignment(IConstructor __param1, Assignable __param2,
				org.rascalmpl.ast.Assignment __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			Result<IValue> right = this.getStatement().interpret(__eval);
			return this.getAssignable().assignment(
					new AssignableEvaluator(__eval.getCurrentEnvt(), this
							.getOperator(), right, __eval));

		}

	}

	static public class Break extends org.rascalmpl.ast.Statement.Break {

		public Break(IConstructor __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

	}

	static public class Continue extends org.rascalmpl.ast.Statement.Continue {

		public Continue(IConstructor __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

	}

	static public class DoWhile extends org.rascalmpl.ast.Statement.DoWhile {

		public DoWhile(IConstructor __param1, Label __param2,
				org.rascalmpl.ast.Statement __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

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

			while (true) {
				try {
					body.interpret(__eval);

					gen = generator.getBacktracker(__eval);
					gen.init();
					if (__eval.__getInterrupt()) {
						throw new InterruptException(__eval.getStackTrace());
					}
					if (!(gen.hasNext() && gen.next())) {
						IValue value = __eval.__getAccumulators().pop().done();
						return org.rascalmpl.interpreter.result.ResultFactory
								.makeResult(value.getType(), value, __eval);
					}
				} finally {
					__eval.unwind(old);
				}
			}

		}

	}

	static public class EmptyStatement extends
			org.rascalmpl.ast.Statement.EmptyStatement {

		public EmptyStatement(IConstructor __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

	}

	static public class Expression extends
			org.rascalmpl.ast.Statement.Expression {

		public Expression(IConstructor __param1, org.rascalmpl.ast.Expression __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

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

		public Fail(IConstructor __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			if (!this.getTarget().isEmpty()) {
				throw new Failure(this.getTarget().getName().toString());
			}

			throw new Failure();

		}

	}

	static public class Filter extends org.rascalmpl.ast.Statement.Filter {

		public Filter(IConstructor __param1) {
			super(__param1);
		}
		
		public Result<IValue> interpret(Evaluator __eval) {
			throw new Filtered();
		}

	}

	static public class For extends org.rascalmpl.ast.Statement.For {

		public For(IConstructor __param1, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

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
				gens[0] = generators.get(0).getBacktracker(__eval);
				olds[0] = __eval.getCurrentEnvt();
				gens[0].init();
				__eval.pushEnv();

				while (i >= 0 && i < size) {
					if (__eval.__getInterrupt()) {
						throw new InterruptException(__eval.getStackTrace());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							// NB: no result handling here.
							__eval.setCurrentAST(body);
							body.interpret(__eval);
							// __eval.unwind(olds[i]);
							// __eval.pushEnv();
						} else {
							i++;
							gens[i] = generators
							.get(i).getBacktracker(__eval);
							olds[i] = __eval.getCurrentEnvt();
							gens[i].init();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						i--;
						__eval.pushEnv();
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

		public FunctionDeclaration(IConstructor __param1,
				org.rascalmpl.ast.FunctionDeclaration __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getFunctionDeclaration().interpret(__eval);

		}

	}

	static public class GlobalDirective extends
			org.rascalmpl.ast.Statement.GlobalDirective {

		public GlobalDirective(IConstructor __param1, Type __param2,
				List<QualifiedName> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

	}

	static public class IfThen extends org.rascalmpl.ast.Statement.IfThen {

		public IfThen(IConstructor __param1, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this
					.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			int i = 0;
			try {
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();

				while (i >= 0 && i < size) {

					if (__eval.__getInterrupt()) {
						throw new InterruptException(__eval.getStackTrace());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							return body.interpret(__eval);
						}

						i++;
						gens[i] = generators.get(i).getBacktracker(__eval);
						gens[i].init();
						olds[i] = __eval.getCurrentEnvt();
						__eval.pushEnv();
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

		public IfThenElse(IConstructor __param1, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4,
				org.rascalmpl.ast.Statement __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this
					.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			int i = 0;
			try {
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();

				while (i >= 0 && i < size) {

					if (__eval.__getInterrupt()) {
						throw new InterruptException(__eval.getStackTrace());
					}
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							return body.interpret(__eval);
						}

						i++;
						gens[i] = generators.get(i).getBacktracker(__eval);
						gens[i].init();
						olds[i] = __eval.getCurrentEnvt();
						__eval.pushEnv();
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

		public Insert(IConstructor __param1, DataTarget __param2,
				org.rascalmpl.ast.Statement __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Insert(this
					.getStatement().interpret(__eval));

		}

	}

	static public class NonEmptyBlock extends
			org.rascalmpl.ast.Statement.NonEmptyBlock {

		public NonEmptyBlock(IConstructor __param1, Label __param2,
				List<org.rascalmpl.ast.Statement> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

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

		public Return(IConstructor __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Return(this
					.getStatement().interpret(__eval), this.getStatement()
					.getLocation());

		}

	}

	static public class Solve extends org.rascalmpl.ast.Statement.Solve {

		public Solve(IConstructor __param1, List<QualifiedName> __param2,
				Bound __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			int size = this.getVariables().size();
			QualifiedName vars[] = new QualifiedName[size];
			IValue currentValue[] = new IValue[size];

			Environment old = __eval.getCurrentEnvt();

			try {
				List<QualifiedName> varList = this.getVariables();

				for (int i = 0; i < size; i++) {
					QualifiedName var = varList.get(i);
					vars[i] = var;
					if (__eval.getCurrentEnvt().getVariable(var) == null) {
						throw new UndeclaredVariableError(var.toString(), var);
					}
					if (__eval.getCurrentEnvt().getVariable(var).getValue() == null) {
						throw new UninitializedVariableError(var.toString(),
								var);
					}
					currentValue[i] = __eval.getCurrentEnvt().getVariable(var)
							.getValue();
				}

				__eval.pushEnv();
				org.rascalmpl.ast.Statement body = this.getBody();

				int max = -1;

				Bound bound = this.getBound();
				if (bound.isDefault()) {
					Result<IValue> res = bound.getExpression()
							.interpret(__eval);
					if (!res.getType().isIntegerType()) {
						throw new UnexpectedTypeError(
								org.rascalmpl.interpreter.Evaluator.__getTf()
										.integerType(), res.getType(), this);
					}
					max = ((IInteger) res.getValue()).intValue();
					if (max <= 0) {
						throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory
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
					if (__eval.__getInterrupt()) {
						throw new InterruptException(__eval.getStackTrace());
					}
					bodyResult = body.interpret(__eval);
					for (int i = 0; i < size; i++) {
						QualifiedName var = vars[i];
						Result<IValue> v = __eval.getCurrentEnvt().getVariable(
								var);
						if (currentValue[i] == null
								|| !v.getValue().isEqual(currentValue[i])) {
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
		private abstract class CaseBlock {
			public abstract boolean matchAndEval(Evaluator eval,
					Result<IValue> subject);
		}

		private class ConcreteBlock extends CaseBlock {
			private final Hashtable<IConstructor, List<Case>> table = new Hashtable<IConstructor, List<Case>>();

			void add(Case c) {
				IConstructor key = TreeAdapter.getProduction(c
						.getPatternWithAction().getPattern().getTree());
				List<Case> same = table.get(key);
				if (same == null) {
					same = new LinkedList<Case>();
					table.put(key, same);
				}
				same.add(c);
			}

			@Override
			public boolean matchAndEval(Evaluator eval, Result<IValue> subject) {
				IValue value = subject.getValue();
				org.eclipse.imp.pdb.facts.type.Type subjectType = value
						.getType();

				if (subjectType.isSubtypeOf(Factory.Tree)) {
					List<Case> alts = table.get(TreeAdapter
							.getProduction((IConstructor) value));
					if (alts != null) {
						for (Case c : alts) {
							PatternWithAction rule = c.getPatternWithAction();
							if (eval.matchAndEval(subject, rule.getPattern(),
									rule.getStatement())) {
								return true;
							}
						}
					}
				}

				return false;
			}
		}

		private class DefaultBlock extends CaseBlock {
			private final Case theCase;

			public DefaultBlock(Case c) {
				this.theCase = c;
			}

			@Override
			public boolean matchAndEval(Evaluator __eval, Result<IValue> subject) {
				if (theCase.isDefault()) {
					// TODO: what if the default statement uses a fail
					// statement?
					theCase.getStatement().interpret(__eval);
					return true;
				}
				
				PatternWithAction rule = theCase.getPatternWithAction();
				return __eval.matchAndEval(subject, rule.getPattern(), rule.getStatement());
			}
		}

		private class NodeCaseBlock extends CaseBlock {
			private final Hashtable<String, List<Case>> table = new Hashtable<String, List<Case>>();

			void add(Case c) {
				org.rascalmpl.ast.Expression name = c.getPatternWithAction()
						.getPattern().getExpression();
				String key = null;

				if (name.isQualifiedName()) {
					key = Names.name(Names.lastName(name.getQualifiedName()));
				} else if (name.isLiteral()) {
					key = ((StringConstant.Lexical) name.getLiteral()
							.getStringLiteral().getConstant()).getString();
				}

				List<Case> same = table.get(key);
				if (same == null) {
					same = new LinkedList<Case>();
					table.put(key, same);
				}
				same.add(c);
			}

			@Override
			public boolean matchAndEval(Evaluator eval, Result<IValue> subject) {
				IValue value = subject.getValue();
				org.eclipse.imp.pdb.facts.type.Type subjectType = value
						.getType();

				if (subjectType.isSubtypeOf(TF.nodeType())) {
					List<Case> alts = table.get(((INode) value).getName());
					if (alts != null) {
						for (Case c : alts) {
							PatternWithAction rule = c.getPatternWithAction();
							if (eval.matchAndEval(subject, rule.getPattern(),
									rule.getStatement())) {
								return true;
							}
						}
					}
				}

				return false;
			}
		}

		private final List<CaseBlock> blocks;

		public Switch(IConstructor __param1, Label __param2,
				org.rascalmpl.ast.Expression __param3, List<Case> __param4) {
			super(__param1, __param2, __param3, __param4);
			blocks = new ArrayList<CaseBlock>(__param4.size());
			precompute(__param4);
//			System.err.println("switched optimized from + " + __param4.size()
//					+ " to " + blocks.size() + " alts at " + getLocation());
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			Result<IValue> subject = this.getExpression().interpret(__eval);

			for (CaseBlock cs : blocks) {
				if (cs.matchAndEval(__eval, subject)) {
					return org.rascalmpl.interpreter.result.ResultFactory
							.nothing();
				}
			}

			return ResultFactory.nothing();
		}

		private boolean isConcreteSyntaxPattern(Case d) {
			if (d.isDefault()) {
				return false;
			}

			org.rascalmpl.ast.Expression pattern = d.getPatternWithAction()
					.getPattern();
			if (pattern._getType() != null
					&& pattern._getType() instanceof NonTerminalType) {
				return true;
			}

			return false;
		}

		private boolean isConstantTreePattern(Case c) {
			if (c.isDefault()) {
				return false;
			}
			org.rascalmpl.ast.Expression pattern = c.getPatternWithAction()
					.getPattern();
			if (pattern.isCallOrTree()) {
				if (pattern.getExpression().isQualifiedName()) {
					return true;
				}
				if (pattern.getExpression().isLiteral()) {
					return true;
				}
			}

			return false;
		}

		private void precompute(List<Case> cases) {
			for (int i = 0; i < cases.size(); i++) {
				Case c = cases.get(i);
				if (isConcreteSyntaxPattern(c)) {
					ConcreteBlock b = new ConcreteBlock();
					b.add(c);
					for (int j = i + 1; j < cases.size(); j++) {
						Case d = cases.get(j);
						if (isConcreteSyntaxPattern(d)) {
							b.add(d);
							i++;
						} else {
							break;
						}
					}
					blocks.add(b);
				} else if (isConstantTreePattern(c)) {
					NodeCaseBlock b = new NodeCaseBlock();
					b.add(c);
					for (int j = i + 1; j < cases.size(); j++) {
						Case d = cases.get(j);
						if (isConstantTreePattern(d)) {
							b.add(d);
							i++;
						} else {
							break;
						}
					}
					blocks.add(b);
				} else {
					blocks.add(new DefaultBlock(c));
				}
			}
		}

	}

	static public class Throw extends org.rascalmpl.ast.Statement.Throw {

		public Throw(IConstructor __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Throw(this
					.getStatement().interpret(__eval).getValue(), __eval
					.getCurrentAST(), __eval.getStackTrace());

		}

	}

	static public class Try extends org.rascalmpl.ast.Statement.Try {

		public Try(IConstructor __param1, org.rascalmpl.ast.Statement __param2,
				List<Catch> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return evalStatementTry(__eval, this.getBody(), this.getHandlers(), null);
		}
		
		static public Result<IValue> evalStatementTry(Evaluator eval, org.rascalmpl.ast.Statement body, java.util.List<Catch> handlers, org.rascalmpl.ast.Statement finallyBody) {
			Result<IValue> res = org.rascalmpl.interpreter.result.ResultFactory.nothing();

			try {
				res = body.interpret(eval);
			} catch (org.rascalmpl.interpreter.control_exceptions.Throw e) {
				IValue eValue = e.getException();

				boolean handled = false;

				for (Catch c : handlers) {
					if (c.isDefault()) {
						res = c.getBody().interpret(eval);
						handled = true;
						break;
					}

					// TODO: Throw should contain Result<IValue> instead of IValue
					if (eval.matchAndEval(makeResult(eValue.getType(), eValue, eval), c.getPattern(), c.getBody())) {
						handled = true;
						break;
					}
				}

				if (!handled)
					throw e;
			} finally {
				if (finallyBody != null) {
					finallyBody.interpret(eval);
				}
			}
			return res;
		}
	}

	static public class TryFinally extends
			org.rascalmpl.ast.Statement.TryFinally {

		public TryFinally(IConstructor __param1, org.rascalmpl.ast.Statement __param2,
				List<org.rascalmpl.ast.Catch> __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return org.rascalmpl.semantics.dynamic.Statement.Try.evalStatementTry(__eval, this.getBody(), this.getHandlers(),
					this.getFinallyBody());
		}
	}

	static public class VariableDeclaration extends
			org.rascalmpl.ast.Statement.VariableDeclaration {

		public VariableDeclaration(IConstructor __param1,
				LocalVariableDeclaration __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getDeclaration().interpret(__eval);

		}

	}

	static public class Visit extends org.rascalmpl.ast.Statement.Visit {

		public Visit(IConstructor __param1, Label __param2,
				org.rascalmpl.ast.Visit __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

			return this.getVisit().interpret(__eval);

		}

	}

	static public class While extends org.rascalmpl.ast.Statement.While {

		public While(IConstructor __param1, Label __param2,
				List<org.rascalmpl.ast.Expression> __param3,
				org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {

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

			// a while statement is different from a for statement, the body of
			// the while can influence the
			// variables that are used to test the condition of the loop
			// while does not iterate over all possible matches, rather it
			// produces every time the first match
			// that makes the condition true

			loop: while (true) {
				int i = 0;
				try {
					gens[0] = generators.get(0).getBacktracker(__eval);
					gens[0].init();
					olds[0] = __eval.getCurrentEnvt();

					while (i >= 0 && i < size) {
						__eval.unwind(olds[i]);
						__eval.pushEnv();

						if (__eval.__getInterrupt()) {
							throw new InterruptException(__eval.getStackTrace());
						}
						if (gens[i].hasNext() && gens[i].next()) {
							if (i == size - 1) {
								__eval.setCurrentAST(body);
								body.interpret(__eval);
								continue loop;
							}

							i++;
							gens[i] = generators
							.get(i).getBacktracker(__eval);
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
						} else {
							i--;
						}
					}
				} finally {
					__eval.unwind(old);
				}
				IValue value = __eval.__getAccumulators().pop().done();
				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(value.getType(), value, __eval);
			}

		}

	}

	public Statement(IConstructor __param1) {
		super(__param1);
	}
}
