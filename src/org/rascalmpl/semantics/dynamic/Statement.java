package org.rascalmpl.semantics.dynamic;

import java.lang.String;
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
import org.rascalmpl.ast.NoElseMayFollow;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.PatternWithAction;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Target;
import org.rascalmpl.ast.Type;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.AppendWithoutLoop;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;

public abstract class Statement extends org.rascalmpl.ast.Statement {

	public Statement(INode __param1) {
		super(__param1);
	}

	static public class Solve extends org.rascalmpl.ast.Statement.Solve {

		public Solve(INode __param1, List<QualifiedName> __param2, Bound __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

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
						throw new UninitializedVariableError(var.toString(), var);
					}
					currentValue[i] = __eval.getCurrentEnvt().getVariable(var).getValue();
				}

				__eval.pushEnv();
				org.rascalmpl.ast.Statement body = this.getBody();

				int max = -1;

				Bound bound = this.getBound();
				if (bound.isDefault()) {
					Result<IValue> res = bound.getExpression().__evaluate(__eval);
					if (!res.getType().isIntegerType()) {
						throw new UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().integerType(), res.getType(), this);
					}
					max = ((IInteger) res.getValue()).intValue();
					if (max <= 0) {
						throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.indexOutOfBounds((IInteger) res.getValue(), __eval.getCurrentAST(), __eval.getStackTrace());
					}
				}

				Result<IValue> bodyResult = null;

				boolean change = true;
				int iterations = 0;

				while (change && (max == -1 || iterations < max)) {
					change = false;
					iterations++;
					if (__eval.__getInterrupt())
						throw new InterruptException(__eval.getStackTrace());
					bodyResult = body.__evaluate(__eval);
					for (int i = 0; i < size; i++) {
						QualifiedName var = vars[i];
						Result<IValue> v = __eval.getCurrentEnvt().getVariable(var);
						if (currentValue[i] == null || !v.getValue().isEqual(currentValue[i])) {
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

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Break extends org.rascalmpl.ast.Statement.Break {

		public Break(INode __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class VariableDeclaration extends org.rascalmpl.ast.Statement.VariableDeclaration {

		public VariableDeclaration(INode __param1, LocalVariableDeclaration __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return this.getDeclaration().__evaluate(__eval);

		}

	}

	static public class Fail extends org.rascalmpl.ast.Statement.Fail {

		public Fail(INode __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			if (!this.getTarget().isEmpty()) {
				throw new Failure(this.getTarget().getName().toString());
			}

			throw new Failure();

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Insert extends org.rascalmpl.ast.Statement.Insert {

		public Insert(INode __param1, DataTarget __param2, org.rascalmpl.ast.Statement __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Insert(this.getStatement().__evaluate(__eval));

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Expression extends org.rascalmpl.ast.Statement.Expression {

		public Expression(INode __param1, org.rascalmpl.ast.Expression __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Environment old = __eval.getCurrentEnvt();

			try {
				__eval.pushEnv();
				return this.getExpression().__evaluate(__eval);
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Try extends org.rascalmpl.ast.Statement.Try {

		public Try(INode __param1, org.rascalmpl.ast.Statement __param2, List<Catch> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return __eval.evalStatementTry(this.getBody(), this.getHandlers(), null);

		}

	}

	static public class AssertWithMessage extends org.rascalmpl.ast.Statement.AssertWithMessage {

		public AssertWithMessage(INode __param1, org.rascalmpl.ast.Expression __param2, org.rascalmpl.ast.Expression __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> r = this.getExpression().__evaluate(__eval);
			if (!r.getType().equals(org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), r.getType(), this);
			}
			if (r.getValue().isEqual(__eval.__getVf().bool(false))) {
				Result<IValue> msgValue = this.getMessage().__evaluate(__eval);
				IString msg = __eval.__getVf().string(org.rascalmpl.interpreter.utils.Utils.unescape(msgValue.getValue().toString(), this, __eval.getCurrentEnvt()));
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.assertionFailed(msg, __eval.getCurrentAST(), __eval.getStackTrace());
			}
			return r;

		}

	}

	static public class Append extends org.rascalmpl.ast.Statement.Append {

		public Append(INode __param1, DataTarget __param2, org.rascalmpl.ast.Statement __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Accumulator target = null;
			if (__eval.__getAccumulators().empty()) {
				throw new AppendWithoutLoop(this);
			}
			if (!this.getDataTarget().isEmpty()) {
				String label = org.rascalmpl.interpreter.utils.Names.name(this.getDataTarget().getLabel());
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
			Result<IValue> result = this.getStatement().__evaluate(__eval);
			target.append(result);
			return result;

		}

	}

	static public class IfThen extends org.rascalmpl.ast.Statement.IfThen {

		public IfThen(INode __param1, Label __param2, List<org.rascalmpl.ast.Expression> __param3, org.rascalmpl.ast.Statement __param4, NoElseMayFollow __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			int i = 0;
			try {
				gens[0] = __eval.makeBooleanResult(generators.get(0));
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();

				while (i >= 0 && i < size) {
					if (__eval.__getInterrupt())
						throw new InterruptException(__eval.getStackTrace());
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							return body.__evaluate(__eval);
						}

						i++;
						gens[i] = __eval.makeBooleanResult(generators.get(i));
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

	static public class Switch extends org.rascalmpl.ast.Statement.Switch {

		public Switch(INode __param1, Label __param2, org.rascalmpl.ast.Expression __param3, List<Case> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> subject = this.getExpression().__evaluate(__eval);

			for (Case cs : this.getCases()) {
				if (cs.isDefault()) {
					// TODO: what if the default statement uses a fail
					// statement?
					return cs.getStatement().__evaluate(__eval);
				}
				PatternWithAction rule = cs.getPatternWithAction();
				if (rule.isArbitrary() && __eval.matchAndEval(subject, rule.getPattern(), rule.getStatement())) {
					return org.rascalmpl.interpreter.result.ResultFactory.nothing();
					/*
					 * } else if(rule.isGuarded()) {
					 * org.meta_environment.rascal.ast.Type tp = rule.getType();
					 * Type t = evalType(tp);
					 * if(subject.getType().isSubtypeOf(t) &&
					 * matchAndEval(subject.getValue(), rule.getPattern(),
					 * rule.getStatement())){ return ResultFactory.nothing(); }
					 */
				} else if (rule.isReplacing()) {
					throw new NotYetImplemented(rule);
				}
			}
			return ResultFactory.nothing();

		}

	}

	static public class While extends org.rascalmpl.ast.Statement.While {

		public While(INode __param1, Label __param2, List<org.rascalmpl.ast.Expression> __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getBody();
			List<org.rascalmpl.ast.Expression> generators = this.getConditions();

			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
			}
			__eval.__getAccumulators().push(new Accumulator(__eval.__getVf(), label));

			// a while statement is different from a for statement, the body of
			// the while can influence the
			// variables that are used to test the condition of the loop
			// while does not iterate over all possible matches, rather it
			// produces every time the first match
			// that makes the condition true

			loop: while (true) {
				int i = 0;
				try {
					gens[0] = __eval.makeBooleanResult(generators.get(0));
					gens[0].init();
					olds[0] = __eval.getCurrentEnvt();
					__eval.pushEnv();

					while (i >= 0 && i < size) {
						if (__eval.__getInterrupt())
							throw new InterruptException(__eval.getStackTrace());
						if (gens[i].hasNext() && gens[i].next()) {
							if (i == size - 1) {
								body.__evaluate(__eval);
								continue loop;
							}

							i++;
							gens[i] = __eval.makeBooleanResult(generators.get(i));
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
				IValue value = __eval.__getAccumulators().pop().done();
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
			}

		}

	}

	static public class Return extends org.rascalmpl.ast.Statement.Return {

		public Return(INode __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Return(this.getStatement().__evaluate(__eval), this.getStatement().getLocation());

		}

	}

	static public class IfThenElse extends org.rascalmpl.ast.Statement.IfThenElse {

		public IfThenElse(INode __param1, Label __param2, List<org.rascalmpl.ast.Expression> __param3, org.rascalmpl.ast.Statement __param4, org.rascalmpl.ast.Statement __param5) {
			super(__param1, __param2, __param3, __param4, __param5);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getThenStatement();
			List<org.rascalmpl.ast.Expression> generators = this.getConditions();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();

			int i = 0;
			try {
				gens[0] = __eval.makeBooleanResult(generators.get(0));
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				while (i >= 0 && i < size) {
					if (__eval.__getInterrupt())
						throw new InterruptException(__eval.getStackTrace());
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.setCurrentAST(body);
							return body.__evaluate(__eval);
						}

						i++;
						gens[i] = __eval.makeBooleanResult(generators.get(i));
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
			return elsePart.__evaluate(__eval);

		}

	}

	static public class Assignment extends org.rascalmpl.ast.Statement.Assignment {

		public Assignment(INode __param1, Assignable __param2, org.rascalmpl.ast.Assignment __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> right = this.getStatement().__evaluate(__eval);
			return this.getAssignable().__evaluate(new AssignableEvaluator(__eval.getCurrentEnvt(), this.getOperator(), right, __eval));

		}

	}

	static public class Ambiguity extends org.rascalmpl.ast.Statement.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Statement> __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new Ambiguous((IConstructor) this.getTree());

		}

	}

	static public class NonEmptyBlock extends org.rascalmpl.ast.Statement.NonEmptyBlock {

		public NonEmptyBlock(INode __param1, Label __param2, List<org.rascalmpl.ast.Statement> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> r = org.rascalmpl.interpreter.result.ResultFactory.nothing();
			Environment old = __eval.getCurrentEnvt();

			__eval.pushEnv(this);
			try {
				for (org.rascalmpl.ast.Statement stat : this.getStatements()) {
					__eval.setCurrentAST(stat);
					r = stat.__evaluate(__eval);
				}
			} finally {
				__eval.unwind(old);
			}
			return r;

		}

	}

	static public class GlobalDirective extends org.rascalmpl.ast.Statement.GlobalDirective {

		public GlobalDirective(INode __param1, Type __param2, List<QualifiedName> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class For extends org.rascalmpl.ast.Statement.For {

		public For(INode __param1, Label __param2, List<org.rascalmpl.ast.Expression> __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getBody();
			List<org.rascalmpl.ast.Expression> generators = this.getGenerators();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment old = __eval.getCurrentEnvt();
			Environment[] olds = new Environment[size];

			Result<IValue> result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(org.rascalmpl.interpreter.Evaluator.__getTf().voidType(), __eval.__getVf().list(), __eval);

			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
			}
			__eval.__getAccumulators().push(new Accumulator(__eval.__getVf(), label));

			// TODO: does __eval prohibit that the body influences the behavior
			// of the generators??

			int i = 0;
			boolean normalCflow = false;
			try {
				gens[0] = __eval.makeBooleanResult(generators.get(0));
				gens[0].init();
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();

				while (i >= 0 && i < size) {
					if (__eval.__getInterrupt())
						throw new InterruptException(__eval.getStackTrace());
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							// NB: no result handling here.
							body.__evaluate(__eval);
						} else {
							i++;
							gens[i] = __eval.makeBooleanResult(generators.get(i));
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						i--;
						__eval.pushEnv();
					}
				}
				// TODO: __eval is not enough, we must also detect
				// break and return a list result then.
				normalCflow = true;
			} finally {
				IValue value = __eval.__getAccumulators().pop().done();
				if (normalCflow) {
					result = org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
				}
				__eval.unwind(old);
			}
			return result;

		}

	}

	static public class TryFinally extends org.rascalmpl.ast.Statement.TryFinally {

		public TryFinally(INode __param1, org.rascalmpl.ast.Statement __param2, List<Catch> __param3, org.rascalmpl.ast.Statement __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return __eval.evalStatementTry(this.getBody(), this.getHandlers(), this.getFinallyBody());

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Visit extends org.rascalmpl.ast.Statement.Visit {

		public Visit(INode __param1, Label __param2, org.rascalmpl.ast.Visit __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return this.getVisit().__evaluate(__eval);

		}

	}

	static public class Continue extends org.rascalmpl.ast.Statement.Continue {

		public Continue(INode __param1, Target __param2) {
			super(__param1, __param2);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new NotYetImplemented(this.toString()); // TODO

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class Throw extends org.rascalmpl.ast.Statement.Throw {

		public Throw(INode __param1, org.rascalmpl.ast.Statement __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			throw new org.rascalmpl.interpreter.control_exceptions.Throw(this.getStatement().__evaluate(__eval).getValue(), __eval.getCurrentAST(), __eval.getStackTrace());

		}

	}

	static public class DoWhile extends org.rascalmpl.ast.Statement.DoWhile {

		public DoWhile(INode __param1, Label __param2, org.rascalmpl.ast.Statement __param3, org.rascalmpl.ast.Expression __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			org.rascalmpl.ast.Statement body = this.getBody();
			org.rascalmpl.ast.Expression generator = this.getCondition();
			IBooleanResult gen;
			Environment old = __eval.getCurrentEnvt();
			String label = null;
			if (!this.getLabel().isEmpty()) {
				label = org.rascalmpl.interpreter.utils.Names.name(this.getLabel().getName());
			}
			__eval.__getAccumulators().push(new Accumulator(__eval.__getVf(), label));

			while (true) {
				try {
					body.__evaluate(__eval);

					gen = __eval.makeBooleanResult(generator);
					gen.init();
					if (__eval.__getInterrupt())
						throw new InterruptException(__eval.getStackTrace());
					if (!(gen.hasNext() && gen.next())) {
						IValue value = __eval.__getAccumulators().pop().done();
						return org.rascalmpl.interpreter.result.ResultFactory.makeResult(value.getType(), value, __eval);
					}
				} finally {
					__eval.unwind(old);
				}
			}

		}

	}

	static public class Assert extends org.rascalmpl.ast.Statement.Assert {

		public Assert(INode __param1, org.rascalmpl.ast.Expression __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			Result<IValue> r = this.getExpression().__evaluate(__eval);
			if (!r.getType().equals(org.rascalmpl.interpreter.Evaluator.__getTf().boolType())) {
				throw new UnexpectedTypeError(org.rascalmpl.interpreter.Evaluator.__getTf().boolType(), r.getType(), this);
			}

			if (r.getValue().isEqual(__eval.__getVf().bool(false))) {
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.assertionFailed(this, __eval.getStackTrace());
			}
			return r;

		}

	}

	static public class EmptyStatement extends org.rascalmpl.ast.Statement.EmptyStatement {

		public EmptyStatement(INode __param1) {
			super(__param1);
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return org.rascalmpl.interpreter.result.ResultFactory.nothing();

		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

	}

	static public class FunctionDeclaration extends org.rascalmpl.ast.Statement.FunctionDeclaration {

		public FunctionDeclaration(INode __param1, org.rascalmpl.ast.FunctionDeclaration __param2) {
			super(__param1, __param2);
		}

		@Override
		public <T> T __evaluate(NullASTVisitor<T> __eval) {
			return null;
		}

		@Override
		public Result<IValue> __evaluate(Evaluator __eval) {

			return this.getFunctionDeclaration().__evaluate(__eval);

		}

	}
}