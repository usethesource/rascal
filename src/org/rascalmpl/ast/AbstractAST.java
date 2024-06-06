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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.AssignableEvaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPattern;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public abstract class AbstractAST implements IVisitable, Cloneable {
	protected static final TypeFactory TF = TypeFactory.getInstance();
	protected static final RascalTypeFactory RTF = RascalTypeFactory.getInstance();
	protected static final IRascalValueFactory VF = IRascalValueFactory.getInstance();
	protected ISourceLocation src;
	
	AbstractAST(ISourceLocation src) {
		this.src = src;
	}
	
	/**
	 * @return a non-terminal type for ASTs which represent concrete syntax patterns or null otherwise
	 */
	public Type getConcreteSyntaxType() {
		return null;
	}
	
	@Override
	public abstract Object clone();
	
	@SuppressWarnings("unchecked")
	/**
	 * Used in generated clone methods to avoid case distinctions in the code generator
	 */
	protected <T extends AbstractAST> T clone(T in) {
	    if (in == null) {
	        return null;
	    }
		return (T) in.clone();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Used in generated clone methods to avoid regenerating the same code;
	 */ 
	public <T extends AbstractAST> java.util.List<T> clone(java.util.List<T> in) {
		java.util.List<T> tmp = new ArrayList<T>(in.size());
		for (T elem : in) {
			tmp.add((T) elem.clone());
		}
		return tmp;
	}
	
	/**
	 * Used in clone and AST Builder
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractAST> T newInstance(java.lang.Class<T> clazz, Object... args) {
    	try {
    		Constructor<?> cons = clazz.getConstructors()[0];
    		cons.setAccessible(true);
    		return (T) cons.newInstance(args);
    	}
    	catch (ClassCastException | ArrayIndexOutOfBoundsException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
    		throw new ImplementationError("Can not instantiate AST object for " + clazz.getName(), e);
    	}
    }
	
	public AbstractAST findNode(int offset) {
		if (src.getOffset() <= offset
				&& offset < src.getOffset() + src.getLength()) {
			return this;
		}
		
		return null;
	}
	
	public static <T extends IValue> Result<T> makeResult(Type declaredType, IValue value, IEvaluatorContext ctx) {
		return ResultFactory.makeResult(declaredType, value, ctx);
	}
	
	public static Result<IValue> nothing() {
		return org.rascalmpl.interpreter.result.ResultFactory.nothing();
	}

	public <T> T accept(IASTVisitor<T> v) {
		return null;
	}

	public ISourceLocation getLocation() {
		return src;
	}

	@Override
	public boolean equals(Object obj) {
		throw new ImplementationError("Missing generated hashCode/equals methods");
	}

	@Deprecated
	public IConstructor getTree() {
		throw new NotYetImplemented(this);
	}

	@Override
	public int hashCode() {
		throw new ImplementationError("Missing generated concrete hashCode/equals methods");
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
	 */
	public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
		throw new NotYetImplemented(this);
	}

	public Type __evaluate(org.rascalmpl.interpreter.BasicTypeEvaluator eval) {
		throw new NotYetImplemented(this);
	}

	/**
	 * Recursively build a matching data-structure, use getMatcher if you are just a client of IMatchingResult.
	 * @param bindTypeParameters TODO
	 */
	public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
		throw new UnsupportedPattern(toString(), this);
	}
	
	public IMatchingResult getMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return buildMatcher(eval, bindTypeParameters);
	}

	protected void addForLineNumber(int line, java.util.List<AbstractAST> result) {
		return;
	}
	
	public List<AbstractAST> breakpoints(int line) {
		List<AbstractAST> candidates = new LinkedList<>();
		addForLineNumber(line, candidates);
		return candidates.stream().filter(p -> p.isBreakable()).collect(Collectors.toList());
	}
	
	/**
	 * Recursively build a back-tracking data-structure, use getBacktracker if you are just a client of IBooleanResult
	 */
	public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
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
		return false;
	}
	
	public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
	    __eval.warning("INTERNAL WARNING: generic implementation of isDefined triggered", getLocation());
	    try {
	        interpret(__eval);
	        return ResultFactory.makeResult(TF.boolType(), VF.bool(true), __eval);
	    }
	    catch (Throw e) {
	        return ResultFactory.makeResult(TF.boolType(), VF.bool(false), __eval);
	    }
	}
}
