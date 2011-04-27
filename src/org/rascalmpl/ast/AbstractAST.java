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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.BooleanEvaluator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected final IConstructor node;
	protected ASTStatistics stats = new ASTStatistics();
	protected Type _type = null;
	protected final TypeFactory TF = TypeFactory.getInstance();
	protected final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	protected final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	AbstractAST(IConstructor node) {
		this.node = node;
	}
	
	public Type _getType() {
	  return _type;
	}
	
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return ResultFactory.makeResult(declaredType, value, ctx);
	}
	
	public static Result<IValue> nothing() {
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	}
	
	public void _setType(Type nonterminalType) {
//	  if (_type != null) {
//	    throw new ImplementationError("why set a type twice?");
//	  }
		if (_type != null && (! _type.equals(nonterminalType))) {
			// For debugging purposes
			System.err.println("In _setType, found two unequal types: " + _type.toString() + " and " + nonterminalType.toString());
		}
		this._type = nonterminalType;
	}
	
//	abstract public <T> T accept(IASTVisitor<T> v);
	public <T> T accept(IASTVisitor<T> v) {
		return null;
	}

	public ISourceLocation getLocation() {
		return TreeAdapter.getLocation((IConstructor) node);
	}

	public ASTStatistics getStats() {
		return stats;
	}
	
	public void setStats(ASTStatistics stats) {
		this.stats = stats;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
			if (obj == this) {
				return true;
			}

			AbstractAST other = (AbstractAST) obj;

			if (other.node == node) {
				return true;
			}

			if (other.node.equals(node)) {
				return other.node.getAnnotation("loc").isEqual(
						node.getAnnotation("loc"));
			}
		}
		return false;
	}

	public IConstructor getTree() {
		return node;
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	/**
	 * For debugging purposes
	 */
	public String toString() {
		return TreeAdapter.yield((IConstructor) node);
	}

	public Result<IValue> interpret(Evaluator eval) {
		throw new NotYetImplemented(this);
	}

	public Result<IValue> assignment(AssignableEvaluator eval) {
		throw new NotYetImplemented(this);
	}
	
	public String declareSyntax(Evaluator eval, boolean withImports) {
		throw new NotYetImplemented(this);
	}

	/**
	 * Computes internal type representations for type literals and patterns. 
	 */
	public Type typeOf(Environment env) {
		throw new NotYetImplemented(this);
	}

	public Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator eval) {
		throw new NotYetImplemented(this);
	}

	public IMatchingResult buildMatcher(PatternEvaluator eval) {
		throw new UnsupportedPatternError(toString(), this);
	}

	public IBooleanResult buildBooleanBacktracker(BooleanEvaluator eval) {
		throw new NotYetImplemented(this);
	}
	
}
