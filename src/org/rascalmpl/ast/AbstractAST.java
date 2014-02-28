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
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.ast;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPattern;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.values.ValueFactoryFactory;

public abstract class AbstractAST implements IVisitable {
	protected ISourceLocation src;
	protected Map<String, IValue> annotations;
	protected Type _type = null;
	protected final TypeFactory TF = TypeFactory.getInstance();
	protected final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	protected final IValueFactory VF = ValueFactoryFactory.getValueFactory();
	protected IMatchingResult matcher;
	
	AbstractAST() {
	
	}
	
	AbstractAST(IConstructor node) {
		
	}
	
	public Type _getType() {
	  return _type;
	}
	
	public AbstractAST findNode(int offset) {
		if (src.getOffset() <= offset
				&& offset < src.getOffset() + src.getLength()) {
			return this;
		}
		
		return null;
	}
	
	public void setSourceLocation(ISourceLocation src) {
		this.src = src;
	}
	
	public void setAnnotations(Map<String, IValue> annotations) {
		this.annotations = annotations;
	}
	
	public Map<String, IValue> getAnnotations() {
		return annotations;
	}
	
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return ResultFactory.makeResult(declaredType, value, ctx);
	}
	
	public static Result<IValue> nothing() {
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	}
	
	protected static void interpretKeywordParameters(java.util.List<KeywordFormal> parameters, java.util.Map<String, Type> types, java.util.Map<String, IValue> defaults, Environment env, IEvaluator<Result<IValue>> __eval) {
    for(KeywordFormal kwf : parameters){
      Type declaredType = kwf.getType().typeOf(env, true, __eval);
      Result<IValue> r = kwf.getExpression().interpret(__eval);
      String name = Names.name(kwf.getName());
  
      if(r.getType().isSubtypeOf(declaredType)) {
        types.put(name, declaredType);
        defaults.put(name, r.getValue());
      }
      else {
        throw new UnexpectedType(declaredType, r.getType(), kwf);
      }
    }
  }

  public void _setType(Type nonterminalType) {
		if (_type != null && (! _type.equals(nonterminalType))) {
			// For debugging purposes
			System.err.println("In _setType, found two unequal types: " + _type.toString() + " and " + nonterminalType.toString());
		}
		this._type = nonterminalType;
	}
	
	public <T> T accept(IASTVisitor<T> v) {
		return null;
	}

	public ISourceLocation getLocation() {
		return src;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if (getClass() == obj.getClass()) {
			if (obj == this) {
				return true;
			}

			AbstractAST other = (AbstractAST) obj;

			if (other.src == src) {
				return true;
			}

			return other.src.isEqual(src);
		}
		return false;
	}

	@Deprecated
	public IConstructor getTree() {
		throw new NotYetImplemented(this);
	}

	@Override
	public int hashCode() {
		return src.hashCode();
	}

	@Override
	@Deprecated
	/**
	 * @deprecated YOU SHOULD NOT USE THIS METHOD for user information. Use {@link Names}.
	 */
	public String toString() {
		return "AST debug info: " + getClass().getName() + " at " + src;
	}

	public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
		throw new NotYetImplemented(this);
	}

	public Result<IValue> assignment(AssignableEvaluator eval) {
		throw new NotYetImplemented(this);
	}
	
	/**
	 * Computes internal type representations for type literals and patterns. 
	 * @param instantiateTypeParameters TODO
	 * @param eval TODO
	 */
	public Type typeOf(Environment env, boolean instantiateTypeParameters, IEvaluator<Result<IValue>> eval) {
		throw new NotYetImplemented(this);
	}

	public Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator eval) {
		throw new NotYetImplemented(this);
	}

	/**
	 * Recursively build a matching data-structure, use getMatcher if you are just a client of IMatchingResult.
	 */
	public IMatchingResult buildMatcher(IEvaluatorContext eval) {
		throw new UnsupportedPattern(toString(), this);
	}
	
	public IMatchingResult getMatcher(IEvaluatorContext eval) {
			return buildMatcher(eval);
	}

	/**
	 * Recursively build a back-tracking data-structure, use getBacktracker if you are just a client of IBooleanResult
	 */
	public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
	  System.err.println("ambiguity at " + getLocation());
		throw new NotYetImplemented(this);
	}
	
	public IBooleanResult getBacktracker(IEvaluatorContext ctx) {
		return buildBacktracker(ctx);
	}

	/**
	 * If the debugger can suspend (i.e. break) before interpretation. 
	 * @return <code>true</code> if suspension is supported, otherwise <code>false</code>
	 */
	public boolean isBreakable() {
		return annotations != null
				&& annotations.containsKey("breakable") 
				&& annotations.get("breakable").equals(VF.bool(true));
	}
	
}
