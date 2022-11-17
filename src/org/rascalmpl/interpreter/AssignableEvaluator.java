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
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Assignable;
import org.rascalmpl.ast.Assignment;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;

import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.TypeFactory;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
public class AssignableEvaluator {
	public enum AssignmentOperator {Default, Addition, Subtraction, Product, Division, Intersection, IsDefined}
	private AssignmentOperator operator;
    private Result<IValue> value;
    private final Environment env;
    private final IEvaluator<Result<IValue>> eval;
	private static final TypeFactory tf = io.usethesource.vallang.type.TypeFactory.getInstance();
    
	public AssignableEvaluator(Environment env, Assignment operator, Result<IValue> value, IEvaluator<Result<IValue>> eval) {
		if(operator == null || operator.isDefault())
			this.__setOperator(AssignmentOperator.Default);
		else if(operator.isAddition())
			this.__setOperator(AssignmentOperator.Addition);
		else if(operator.isSubtraction())
			this.__setOperator(AssignmentOperator.Subtraction);
		else if(operator.isProduct())
			this.__setOperator(AssignmentOperator.Product);
		else if(operator.isDivision())
			this.__setOperator(AssignmentOperator.Division);
		else if(operator.isIntersection())
			this.__setOperator(AssignmentOperator.Intersection);
		else if(operator.isIfDefined())
			this.__setOperator(AssignmentOperator.IsDefined);
		else
				throw new ImplementationError("Unknown assignment operator");
		this.__setValue(value);
		this.env = env;
		this.eval = eval;
	} 
	
	public void __setValue(Result<IValue> value) {
		this.value = value;
	}

	public Result<IValue> __getValue() {
		return value;
	}

	public Environment __getEnv() {
		return env;
	}

	public void __setOperator(AssignmentOperator operator) {
		this.operator = operator;
	}

	public AssignmentOperator __getOperator() {
		return operator;
	}

	public IEvaluator<Result<IValue>> __getEval() {
		return eval;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	/*
	 * Given an old result and a right-hand side Result, compute a new result.
	 */
	public Result<IValue> newResult(Result<IValue> oldValue, Result<IValue> rhsValue) {
	    if (rhsValue.getStaticType().isBottom()) {
	        throw new UnexpectedType(oldValue.getStaticType(), tf.voidType(), getCurrentAST());
	    }
	    
		Result<IValue> newValue;
		if(oldValue != null) {
			switch(this.__getOperator()){
			case Default:
				newValue = rhsValue; break;
			case Addition:
				newValue = oldValue.add(rhsValue); break;
			case Subtraction:
				newValue = oldValue.subtract(rhsValue); break;
			case Product:
				newValue = oldValue.multiply(rhsValue); break;
			case Division:
				newValue = oldValue.divide(rhsValue); break;
			case Intersection:
				newValue = oldValue.intersect(rhsValue); break;
			case IsDefined:
				return oldValue;
			default:
				throw new ImplementationError("Unknown assignment operator");
			}
		
			if (newValue.getValue().getType().isSubtypeOf(oldValue.getStaticType())) {
				newValue = ResultFactory.makeResult(oldValue.getStaticType(), newValue.getValue(), this.__getEval());
				return newValue;
			} else 	if (oldValue.hasInferredType()) {
				// Be liberal here: if the user has not declared a variable explicitly
				// we use the lub type for the new value.
				newValue = ResultFactory.makeResult(oldValue.getStaticType().lub(newValue.getStaticType()), newValue.getValue(),this.__getEval());
				newValue.setInferredType(true);
				return newValue;
			}
			
			// we use rhs value here, because the addition (for example) implicitly casts up to value,
			// in which case the error is lost. Since we know that the left hand side of the addition
			// is always the variable we are updating, the cause of the error must always be in the value
			// on the right hand side
			throw new UnexpectedType(oldValue.getStaticType(), rhsValue.getStaticType(), this.__getEval().getCurrentAST());
		}
		
		switch(this.__getOperator()){
			case Default:
			case IsDefined:
				return rhsValue;
			default:
				throw new UninitializedVariable("assignment operator", this.__getEval().getCurrentAST());
		}
	}
	
	public Result<IValue> newResult(IValue oldValue, Result<IValue> rhsValue){
		if (oldValue != null){
			Result<IValue> res = org.rascalmpl.interpreter.result.ResultFactory.makeResult(oldValue.getType().lub(rhsValue.getStaticType()), oldValue, this.__getEval());
			return this.newResult(res, rhsValue);
		}
		switch(this.__getOperator()){
		case Default:
		case IsDefined:
				return rhsValue;
		default:
			throw new UninitializedVariable("assignment operator", this.__getEval().getCurrentAST());
		}
	}
	
	public Result<IValue> recur(Assignable x, Result<IValue> result) {
		return x.getReceiver().assignment(new org.rascalmpl.interpreter.AssignableEvaluator(this.__getEnv(), null, result, this.__getEval()));
	}
	
	public AbstractAST getCurrentAST() {
		return this.__getEval().getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return this.__getEval().getCurrentEnvt();
	}

	public IEvaluator<Result<IValue>> getEvaluator() {
		return this.__getEval().getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return this.__getEval().getHeap();
	}

	public StackTrace getStackTrace() {
		return this.__getEval().getStackTrace();
	}

	public void pushEnv() {
		this.__getEval().pushEnv();		
	}

	public void runTests(IRascalMonitor monitor) {
		this.__getEval().runTests(monitor);
	}

	public void setCurrentEnvt(Environment environment) {
		this.__getEval().setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		this.__getEval().unwind(old);
	}

}
