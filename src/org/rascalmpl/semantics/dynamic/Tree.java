/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.BasicBooleanResult;
import org.rascalmpl.interpreter.matching.ConcreteApplicationPattern;
import org.rascalmpl.interpreter.matching.ConcreteListPattern;
import org.rascalmpl.interpreter.matching.ConcreteListVariablePattern;
import org.rascalmpl.interpreter.matching.ConcreteOptPattern;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.matching.SetPattern;
import org.rascalmpl.interpreter.matching.TypedVariablePattern;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * These classes special case Expression.CallOrTree for concrete syntax patterns
 */
public abstract class Tree  extends org.rascalmpl.ast.Expression {

	public Tree(IConstructor node) {
		super(node);
	}

	public boolean isLayout() {
		return false;
	}

	static public class MetaVariable extends Tree {
		private final String name;
		private final Type type;

		public MetaVariable(IConstructor node, IConstructor symbol, String name) {
			super(node);
			this.name = name;
			this.type = RTF.nonTerminalType(symbol);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return type;
		}

		@Override
		public Object clone() {
			return new MetaVariable(null, ((NonTerminalType) type).getSymbol(), name);
		}

		public boolean equals(Object o) {
			if (!(o instanceof MetaVariable)) {
				return false;
			}
			MetaVariable other = (MetaVariable) o;

			return name.equals(other.name) && type.equals(other.type);
		}

		public int hashCode() {
			return 13333331 + 37 * name.hashCode() + 61 * type.hashCode();
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			Result<IValue> variable = eval.getCurrentEnvt().getVariable(name);

			if (variable == null) {
				throw new UndeclaredVariable(name, this);
			}

			if (variable.getValue() == null) {
				throw new UninitializedVariable(name, this);
			}

			return variable;
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext ctx) {
			IConstructor symbol = ((NonTerminalType) type).getSymbol();
			if (SymbolAdapter.isStarList(symbol) || SymbolAdapter.isPlusList(symbol)) {
				return new ConcreteListVariablePattern(ctx, this, type, name);
			}
			else {
				return new TypedVariablePattern(ctx, this, type, name);
			}
		}

	}

	static public class Appl extends Tree{
		protected final IConstructor production;
		protected final java.util.List<org.rascalmpl.ast.Expression> args;
		protected final Type type;
		protected final boolean constant;

		public Appl(IConstructor prod, ISourceLocation src, java.util.List<org.rascalmpl.ast.Expression> args) {
			super(null);
			this.production = prod;
			this.type = RascalTypeFactory.getInstance().nonTerminalType(production);
			this.args = args;
			this.constant = false; // TODO! isConstant(args);
			if (src != null) {
				this.setSourceLocation(src);
			}
		}

		@Override
		public Object clone() {
			return new Appl(production, src, clone(args));
		}

		@Override
		public boolean isLayout() {
			return ProductionAdapter.isLayout(production);
		}

		public boolean equals(Object o) {
			if (!(o instanceof Appl)) {
				return false;
			}
			Appl other = (Appl) o;

			return production.equals(other.production) && args.equals(other.args);
		}

		public int hashCode() {
			return 101 + 23 * production.hashCode() + 131 * args.hashCode();
		}

		public IConstructor getProduction() {
			return production;
		}

		@Override
		public Type _getType() {
			return type;
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return type;
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			// TODO add function calling
			IListWriter w = eval.getValueFactory().listWriter();
			for (org.rascalmpl.ast.Expression arg : args) {
				w.append(arg.interpret(eval).getValue());
			}

			ISourceLocation location = getLocation();

			if (location != null) {
				java.util.Map<String,IValue> annos = new HashMap<String,IValue>();
				annos.put("loc", location);
				return makeResult(type, VF.appl(annos, production, w.done()), eval);
			}
			else {
				return makeResult(type, VF.appl(production, w.done()), eval);
			}
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(args.size());
			for (Expression kid : args) { 
				if (!((Tree) kid).isLayout()) {
					kids.add(kid.buildMatcher(eval));
				}
			}
			return new ConcreteApplicationPattern(eval, this,  kids);
		}
	}

	static public class Optional extends Appl {
		public Optional(IConstructor production, ISourceLocation src, java.util.List<org.rascalmpl.ast.Expression> args) {
			super(production, src, args);
		}

		@Override
		public Object clone() {
			return new Optional(production, src, clone(args));
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			java.util.List<IMatchingResult> kids = new ArrayList<IMatchingResult>(args.size());
			if (args.size() == 1) {
				kids.add(args.get(0).buildMatcher(eval));
			}
			return new ConcreteOptPattern(eval, this,  kids);
		}
	}

	static public class List extends Appl {
		private final int delta;

		public List(IConstructor prod, ISourceLocation src, java.util.List<org.rascalmpl.ast.Expression> args) {
			super(prod, src, args);
			this.delta = getDelta(production);
		}

		@Override
		public Object clone() {
			return new List(production, src, clone(args));
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(args.size());
			for (org.rascalmpl.ast.Expression arg : args) {
				kids.add(arg.buildMatcher(eval));
			}
			return new ConcreteListPattern(eval, this,  kids);
		}

		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			// TODO add function calling
			IListWriter w = eval.getValueFactory().listWriter();
			for (org.rascalmpl.ast.Expression arg : args) {
				w.append(arg.interpret(eval).getValue());
			}

			ISourceLocation location = getLocation();

			if (location != null) {
				java.util.Map<String,IValue> annos = new HashMap<String,IValue>();
				annos.put("loc", location);
				return makeResult(type, VF.appl(annos, production, flatten(w.done())), eval);
			}
			else {
				return makeResult(type, VF.appl(production, flatten(w.done())), eval);
			}
		}

		private void appendPreviousSeparators(IList args, IListWriter result, int delta, int i, boolean previousWasEmpty) {
			if (!previousWasEmpty) {
				for (int j = i - delta; j > 0 && j < i; j++) {
					result.append(args.get(j));
				}
			}
		}

		private IList flatten(IList args) {
			IListWriter result = VF.listWriter();
			boolean previousWasEmpty = false;

			for (int i = 0; i < args.length(); i+=(delta+1)) {
				IConstructor tree = (IConstructor) args.get(i);

				if (TreeAdapter.isList(tree) && ProductionAdapter.shouldFlatten(production, TreeAdapter.getProduction(tree))) {
					IList nestedArgs = TreeAdapter.getArgs(tree);
					if (nestedArgs.length() > 0) {
						appendPreviousSeparators(args, result, delta, i, previousWasEmpty);
						result.appendAll(nestedArgs);
					}
					else {
						previousWasEmpty = true;
					}
				}
				else {
					appendPreviousSeparators(args, result, delta, i, previousWasEmpty);
					result.append(tree);
					previousWasEmpty = false;
				}
			}

			return result.done();
		}

		private int getDelta(IConstructor prod) {
			IConstructor rhs = ProductionAdapter.getType(prod);

			if (SymbolAdapter.isIterPlusSeps(rhs) || SymbolAdapter.isIterStarSeps(rhs)) {
				return SymbolAdapter.getSeparators(rhs).length();
			}

			return 0;
		}
	}

	static public class Amb extends Tree {
		private final Type type;
		private final java.util.List<org.rascalmpl.ast.Expression> alts;
		private final boolean constant;
		protected final IConstructor node;

		public Amb(IConstructor node, java.util.List<org.rascalmpl.ast.Expression> alternatives) {
			super(node);
			this.type = RascalTypeFactory.getInstance().nonTerminalType(node);
			this.alts = alternatives;
			this.constant = false; // TODO! isConstant(alternatives);
			this.node = this.constant ? node : null;
		}

		@Override
		public Object clone() {
			return new Amb(node, clone(alts));
		}

		public boolean equals(Object o) {
			if (!(o instanceof Amb)) {
				return false;
			}
			Amb other = (Amb) o;

			return node.equals(other.node);
		}

		public int hashCode() {
			return node.hashCode();
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			if (constant) {
				return makeResult(type, node, eval);
			}

			// TODO: add filtering semantics, function calling
			ISetWriter w = eval.getValueFactory().setWriter();
			for (org.rascalmpl.ast.Expression a : alts) {
				w.insert(a.interpret(eval).getValue());
			}
			return makeResult(type, VF.amb(w.done()), eval);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return type;
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			if (constant) {
				return new LiteralPattern(eval, this,  node);
			}

			java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(alts.size());
			for (org.rascalmpl.ast.Expression arg : alts) {
				kids.add(arg.buildMatcher(eval));
			}

			IMatchingResult setMatcher = new SetPattern(eval, this,  kids);
			java.util.List<IMatchingResult> wrap = new ArrayList<IMatchingResult>(1);
			wrap.add(setMatcher);

			Result<IValue> ambCons = eval.getCurrentEnvt().getVariable("amb");
			return new NodePattern(eval, this, new LiteralPattern(eval, this,  ambCons.getValue()), null, RascalValueFactory.Tree_Amb, wrap, Collections.<String,IMatchingResult>emptyMap());
		} 
	}

	static public class Char extends  Tree {
		private final IConstructor node;

		public Char(IConstructor node) {
			super(node);
			this.node = node;
		}

		@Override
		public Object clone() {
			return new Char(node);
		}

		public boolean equals(Object o) {
			if (!(o instanceof Char)) {
				return false;
			}
			Char other = (Char) o;

			return node.equals(other.node);
		}

		public int hashCode() {
			return 17 + 37 * node.hashCode();
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			return makeResult(RascalValueFactory.Tree, node, eval);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			return new LiteralPattern(eval, this,  node);
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return RascalValueFactory.Tree;
		}
	}

	static public class Cycle extends Tree {
		private final int length;
		private final IConstructor node;

		public Cycle(IConstructor node, int length) {
			super(node);
			this.length = length;
			this.node = node;
		}

		@Override
		public Object clone() {
			return new Cycle(node, length);
		}

		public boolean equals(Object o) {
			if (!(o instanceof Cycle)) {
				return false;
			}
			Cycle other = (Cycle) o;

			return node.equals(other.node);
		}

		public int hashCode() {
			return node.hashCode();
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			return makeResult(RascalValueFactory.Tree, VF.cycle(node, length), eval);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval) {
			return new LiteralPattern(eval, this, VF.cycle(node,length));
		}

		@Override
		public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
			return RascalValueFactory.Tree;
		}
	}
}
