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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWriter;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;

public abstract class Comprehension extends org.rascalmpl.ast.Comprehension {

	static public class List extends org.rascalmpl.ast.Comprehension.List {

		public List(INode __param1, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new ListComprehensionWriter(this.getResults(), __eval));
		}

	}

	static public class Map extends org.rascalmpl.ast.Comprehension.Map {

		public Map(INode __param1, Expression __param2, Expression __param3,
				java.util.List<Expression> __param4) {
			super(__param1, __param2, __param3, __param4);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			java.util.List<Expression> resultExprs = new ArrayList<Expression>();
			resultExprs.add(this.getFrom());
			resultExprs.add(this.getTo());
			return evalComprehension(__eval, this.getGenerators(),
					new MapComprehensionWriter(resultExprs, __eval));

		}
	}

	static public class Set extends org.rascalmpl.ast.Comprehension.Set {
		public Set(INode __param1, java.util.List<Expression> __param2,
				java.util.List<Expression> __param3) {
			super(__param1, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(Evaluator __eval) {
			return evalComprehension(__eval, this.getGenerators(),
					new SetComprehensionWriter(this.getResults(), __eval));
		}
	}

	public static Result<IValue> evalComprehension(Evaluator eval,
			java.util.List<Expression> generators, ComprehensionWriter w) {
		int size = generators.size();
		IBooleanResult[] gens = new IBooleanResult[size];
		Environment[] olds = new Environment[size];
		Environment old = eval.getCurrentEnvt();
		int i = 0;

		try {
			gens[0] = eval.makeBooleanResult(generators.get(0));
			gens[0].init();
			olds[0] = eval.getCurrentEnvt();
			eval.pushEnv();

			while (i >= 0 && i < size) {
				if (eval.__getInterrupt())
					throw new InterruptException(eval.getStackTrace());
				if (gens[i].hasNext() && gens[i].next()) {
					if (i == size - 1) {
						w.append();
						eval.unwind(olds[i]);
						eval.pushEnv();
					} else {
						i++;
						gens[i] = eval.makeBooleanResult(generators.get(i));
						gens[i].init();
						olds[i] = eval.getCurrentEnvt();
						eval.pushEnv();
					}
				} else {
					eval.unwind(olds[i]);
					i--;
				}
			}
		} finally {
			eval.unwind(old);
		}
		return w.done();
	}

	public Comprehension(INode __param1) {
		super(__param1);
	}

	private static abstract class ComprehensionWriter {
		protected org.eclipse.imp.pdb.facts.type.Type elementType1;
		protected org.eclipse.imp.pdb.facts.type.Type elementType2;
		protected org.eclipse.imp.pdb.facts.type.Type resultType;
		protected final java.util.List<Expression> resultExprs;
		protected IWriter writer;
		protected final org.rascalmpl.interpreter.Evaluator ev;
		protected final TypeFactory TF;
		protected final IValueFactory VF;

		ComprehensionWriter(java.util.List<Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			this.ev = ev;
			this.resultExprs = resultExprs;
			this.writer = null;
			this.TF = Evaluator.__getTf();
			this.VF = ev.__getVf();
		}

		public void check(Result<IValue> r,
				org.eclipse.imp.pdb.facts.type.Type t, String kind,
				Expression expr) {
			if (!r.getType().isSubtypeOf(t)) {
				throw new UnexpectedTypeError(t, r.getType(), expr);
			}
		}

		public IEvaluatorContext getContext(AbstractAST ast) {
			ev.setCurrentAST(ast);
			return ev;
		}

		public abstract void append();

		public abstract Result<IValue> done();
	}

	public static class ListComprehensionWriter extends ComprehensionWriter {

		private boolean splicing[];
		private Result<IValue> rawElements[];

		@SuppressWarnings("unchecked")
		public ListComprehensionWriter(java.util.List<Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			this.splicing = new boolean[resultExprs.size()];
			this.rawElements = new Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements
			// first, and whether or not to splice them, and evaluate them
			if (this.writer == null) {
				int k = 0;
				this.elementType1 = TF.voidType();

				for (Expression resExpr : this.resultExprs) {
					this.rawElements[k] = resExpr.interpret(this.ev);
					org.eclipse.imp.pdb.facts.type.Type elementType = this.rawElements[k]
							.getType();

					if (elementType.isListType() && !resExpr.isList()) {
						elementType = elementType.getElementType();
						this.splicing[k] = true;
					} else {
						this.splicing[k] = false;
					}
					this.elementType1 = this.elementType1.lub(elementType);
					k++;
				}

				this.resultType = TF.listType(this.elementType1);
				this.writer = this.resultType.writer(VF);
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (Expression resExpr : this.resultExprs) {
					this.rawElements[k++] = resExpr.interpret(this.ev);
				}
			}

			// here we finally add the elements
			int k = 0;
			for (Expression resExpr : this.resultExprs) {
				if (this.splicing[k]) {
					/*
					 * Splice elements of the value of the result expression in
					 * the result list
					 */
					if (!this.rawElements[k].getType().getElementType()
							.isSubtypeOf(this.elementType1)) {
						throw new UnexpectedTypeError(this.elementType1,
								this.rawElements[k].getType().getElementType(),
								resExpr);
					}

					for (IValue val : ((IList) this.rawElements[k].getValue())) {
						((IListWriter) this.writer).append(val);
					}
				} else {
					this.check(this.rawElements[k], this.elementType1, "list",
							resExpr);
					((IListWriter) this.writer).append(this.rawElements[k]
							.getValue());
				}
				k++;
			}
		}

		@Override
		public Result<IValue> done() {
			return (this.writer == null) ? makeResult(TF
					.listType(TF.voidType()), VF.list(), this
					.getContext(this.resultExprs.get(0))) : makeResult(TF
					.listType(this.elementType1), this.writer.done(), this
					.getContext(this.resultExprs.get(0)));
		}
	}

	public static class MapComprehensionWriter extends ComprehensionWriter {

		public MapComprehensionWriter(java.util.List<Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			if (resultExprs.size() != 2)
				throw new ImplementationError(
						"Map comprehensions needs two result expressions");
		}

		@Override
		public void append() {
			Result<IValue> r1 = this.resultExprs.get(0).interpret(this.ev);
			Result<IValue> r2 = this.resultExprs.get(1).interpret(this.ev);
			if (this.writer == null) {
				this.elementType1 = r1.getType();
				this.elementType2 = r2.getType();
				this.resultType = TF.mapType(this.elementType1,
						this.elementType2);
				this.writer = this.resultType.writer(VF);
			}
			this.check(r1, this.elementType1, "map", this.resultExprs.get(0));
			this.check(r2, this.elementType2, "map", this.resultExprs.get(1));
			((IMapWriter) this.writer).put(r1.getValue(), r2.getValue());
		}

		@Override
		public Result<IValue> done() {
			return (this.writer == null) ? makeResult(TF.mapType(TF.voidType(),
					TF.voidType()), VF.map(TF.voidType(), TF.voidType()), this
					.getContext(this.resultExprs.get(0))) : makeResult(TF
					.mapType(this.elementType1, this.elementType2), this.writer
					.done(), this.getContext(this.resultExprs.get(0)));
		}
	}

	public static class SetComprehensionWriter extends ComprehensionWriter {
		private boolean splicing[];
		private Result<IValue> rawElements[];

		@SuppressWarnings("unchecked")
		public SetComprehensionWriter(java.util.List<Expression> resultExprs,
				org.rascalmpl.interpreter.Evaluator ev) {
			super(resultExprs, ev);
			this.splicing = new boolean[resultExprs.size()];
			this.rawElements = new Result[resultExprs.size()];
		}

		@Override
		public void append() {
			// the first time we need to find out the type of the elements
			// first, and whether or not to splice them, and evaluate them
			if (this.writer == null) {
				int k = 0;
				this.elementType1 = TF.voidType();

				for (Expression resExpr : this.resultExprs) {
					this.rawElements[k] = resExpr.interpret(this.ev);
					org.eclipse.imp.pdb.facts.type.Type elementType = this.rawElements[k]
							.getType();

					if (elementType.isSetType() && !resExpr.isSet()) {
						elementType = elementType.getElementType();
						this.splicing[k] = true;
					} else {
						this.splicing[k] = false;
					}
					this.elementType1 = this.elementType1.lub(elementType);
					k++;
				}

				this.resultType = TF.setType(this.elementType1);
				this.writer = this.resultType.writer(VF);
			}
			// the second time we only need to evaluate and add the elements
			else {
				int k = 0;
				for (Expression resExpr : this.resultExprs) {
					this.rawElements[k++] = resExpr.interpret(this.ev);
				}
			}

			// here we finally add the elements
			int k = 0;
			for (Expression resExpr : this.resultExprs) {
				if (this.splicing[k]) {
					/*
					 * Splice elements of the value of the result expression in
					 * the result list
					 */
					if (!this.rawElements[k].getType().getElementType()
							.isSubtypeOf(this.elementType1)) {
						throw new UnexpectedTypeError(this.elementType1,
								this.rawElements[k].getType().getElementType(),
								resExpr);
					}

					for (IValue val : ((ISet) this.rawElements[k].getValue())) {
						((ISetWriter) this.writer).insert(val);
					}
				} else {
					this.check(this.rawElements[k], this.elementType1, "list",
							resExpr);
					((ISetWriter) this.writer).insert(this.rawElements[k]
							.getValue());
				}
				k++;
			}
		}

		@Override
		public Result<IValue> done() {
			return (this.writer == null) ? makeResult(
					TF.setType(TF.voidType()), VF.set(), this
							.getContext(this.resultExprs.get(0))) : makeResult(
					TF.setType(this.elementType1), this.writer.done(), this
							.getContext(this.resultExprs.get(0)));
		}
	}
}
