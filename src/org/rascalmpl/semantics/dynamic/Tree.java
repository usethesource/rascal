/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.BooleanEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.matching.BasicBooleanResult;
import org.rascalmpl.interpreter.matching.ConcreteApplicationPattern;
import org.rascalmpl.interpreter.matching.ConcreteListPattern;
import org.rascalmpl.interpreter.matching.ConcreteOptPattern;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.semantics.dynamic.Expression.CallOrTree;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * These classes special case Expression.CallOrTree for concrete syntax patterns
 */
public abstract class Tree {
  static public class Appl extends org.rascalmpl.ast.Expression {
	protected final IConstructor production;
	protected java.util.List<Expression> args;
	protected Type type;

	public Appl(IConstructor node, java.util.List<Expression> args) {
		super(node);
		this.production = TreeAdapter.getProduction(node);
		this.type = RascalTypeFactory.getInstance().nonTerminalType(production);
		this.args = args;
	}
	
	@Override
	public Type _getType() {
		return type;
	}
	
	@Override
	public Result<IValue> interpret(Evaluator eval) {
		// TODO add constant caching and function calling
		IListWriter w = eval.getValueFactory().listWriter(Factory.Tree);
		for (Expression arg : args) {
			w.append(arg.interpret(eval).getValue());
		}
		
		return makeResult(type, Factory.Tree_Appl.make(eval.getValueFactory(), production, w.done()), eval);
	}
	
	@Override
	public IBooleanResult buildBooleanBacktracker(BooleanEvaluator eval) {
		return new BasicBooleanResult(eval.__getCtx(), this);
	}
	
	@Override
	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(args.size());
		for (int i = 0; i < args.size(); i+=2) { // skip layout elements for efficiency
			kids.add(args.get(i).buildMatcher(eval)); 
		}
		return new ConcreteApplicationPattern(eval.__getCtx(), this, kids);
	}
  }
  
  static public class Lexical extends Appl {
	public Lexical(IConstructor node, java.util.List<Expression> args) {
		super(node, args);
	}
	
	@Override
	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(args.size());
		for (Expression arg : args) {
			kids.add(arg.buildMatcher(eval));
		}
		return new ConcreteApplicationPattern(eval.__getCtx(), this, kids);
	}
  }
  
  static public class Optional extends Appl {
	public Optional(IConstructor node, java.util.List<Expression> args) {
		super(node, args);
	}
	

	@Override
	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		java.util.List<IMatchingResult> kids = new ArrayList<IMatchingResult>(args.size());
		if (args.size() == 1) {
			kids.add(args.get(0).buildMatcher(eval));
		}
		return new ConcreteOptPattern(eval.__getCtx(), this, kids);
	}
  }
  
  static public class List extends Appl {
	public List(IConstructor node, java.util.List<Expression> args) {
		super(node, args);
	}
	
	@Override
	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		java.util.List<IMatchingResult> kids = new java.util.ArrayList<IMatchingResult>(args.size());
		for (Expression arg : args) {
			kids.add(arg.buildMatcher(eval));
		}
		return new ConcreteListPattern(eval.__getCtx(), this, kids);
	}
  }
  
  static public class Char extends CallOrTree {
	  // TODO: special case for efficiency?
	public Char(IConstructor __param1, org.rascalmpl.ast.Expression __param2,
			java.util.List<org.rascalmpl.ast.Expression> __param3) {
		super(__param1, __param2, __param3);
	}
  }
}
