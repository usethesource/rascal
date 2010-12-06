package org.rascalmpl.interpreter;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Assignable;
import org.rascalmpl.ast.Assignment;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Assignable.Annotation;
import org.rascalmpl.ast.Assignable.Constructor;
import org.rascalmpl.ast.Assignable.FieldAccess;
import org.rascalmpl.ast.Assignable.IfDefinedOrDefault;
import org.rascalmpl.ast.Assignable.Subscript;
import org.rascalmpl.ast.Assignable.Tuple;
import org.rascalmpl.ast.Assignable.Variable;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.AssignmentToFinalError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredAnnotationError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFieldError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedSubscriptError;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;


/**
 * Implements assignments in their different shapes, using value lookup
 * implemented by Evaluator.
 * TODO: does not implement type checking completely
 */
public class AssignableEvaluator extends org.rascalmpl.ast.NullASTVisitor<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>>{
	public enum AssignmentOperator {Default, Addition, Subtraction, Product, Division, Intersection, IsDefined}
	private org.rascalmpl.interpreter.AssignableEvaluator.AssignmentOperator operator;
    private org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> value;
    private final org.rascalmpl.interpreter.env.Environment env;
    private final org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> eval;
	private static final org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
    
	public AssignableEvaluator(org.rascalmpl.interpreter.env.Environment env, org.rascalmpl.ast.Assignment operator, org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> value, org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> eval) {
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
				throw new org.rascalmpl.interpreter.asserts.ImplementationError("Unknown assignment operator");
		this.__setValue(value);
		this.env = env;
		this.eval = eval;
	} 
	
	public void __setValue(org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> value) {
		this.value = value;
	}

	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> __getValue() {
		return value;
	}

	public org.rascalmpl.interpreter.env.Environment __getEnv() {
		return env;
	}

	public void __setOperator(org.rascalmpl.interpreter.AssignableEvaluator.AssignmentOperator operator) {
		this.operator = operator;
	}

	public org.rascalmpl.interpreter.AssignableEvaluator.AssignmentOperator __getOperator() {
		return operator;
	}

	public org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue>> __getEval() {
		return eval;
	}

	public static org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	/*
	 * Given an old result and a right-hand side Result, compute a new result.
	 */
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> newResult(org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> oldValue, org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> rhsValue) {
		org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> newValue;
		if(oldValue != null){
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
				throw new org.rascalmpl.interpreter.asserts.ImplementationError("Unknown assignment operator");
			}
		
			if (newValue.getType().isSubtypeOf(oldValue.getType())) {
				newValue = org.rascalmpl.interpreter.result.ResultFactory.makeResult(oldValue.getType(), newValue.getValue(),this.__getEval());
				return newValue;
			} else 	if (oldValue.hasInferredType()) {
				// Be liberal here: if the user has not declared a variable explicitly
				// we use the lub type for the new value.
				newValue = org.rascalmpl.interpreter.result.ResultFactory.makeResult(oldValue.getType().lub(newValue.getType()), newValue.getValue(),this.__getEval());
				newValue.setInferredType(true);
				return newValue;
			}
			
			// we use rhs value here, because the addition (for example) implicitly casts up to value,
			// in which case the error is lost. Since we know that the left hand side of the addition
			// is always the variable we are updating, the cause of the error must always be in the value
			// on the right hand side
			throw new org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError(oldValue.getType(), rhsValue.getType(), this.__getEval().getCurrentAST());
		}
		switch(this.__getOperator()){
			case Default:
			case IsDefined:
				return rhsValue;
			default:
				throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError("assignment operator", this.__getEval().getCurrentAST());
		}
	}
	
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> newResult(org.eclipse.imp.pdb.facts.IValue oldValue, org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> rhsValue){
		if (oldValue != null){
			org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> res = org.rascalmpl.interpreter.result.ResultFactory.makeResult(oldValue.getType().lub(rhsValue.getType()), oldValue, this.__getEval());
			return this.newResult(res, rhsValue);
		}
		switch(this.__getOperator()){
		case Default:
		case IsDefined:
				return rhsValue;
		default:
			throw new org.rascalmpl.interpreter.staticErrors.UninitializedVariableError("assignment operator", this.__getEval().getCurrentAST());
		}
	}
	
	public org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> recur(org.rascalmpl.ast.Assignable x, org.rascalmpl.interpreter.result.Result<org.eclipse.imp.pdb.facts.IValue> result) {
		return x.getReceiver().__evaluate(new org.rascalmpl.interpreter.AssignableEvaluator(this.__getEnv(), null, result, this.__getEval()));
	}
	
	public org.rascalmpl.ast.AbstractAST getCurrentAST() {
		return this.__getEval().getCurrentAST();
	}

	public org.rascalmpl.interpreter.env.Environment getCurrentEnvt() {
		return this.__getEval().getCurrentEnvt();
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this.__getEval().getEvaluator();
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment getHeap() {
		return this.__getEval().getHeap();
	}

	public java.lang.String getStackTrace() {
		return this.__getEval().getStackTrace();
	}

	public void pushEnv() {
		this.__getEval().pushEnv();		
	}

	public void runTests() {
		this.__getEval().runTests();
	}

	public void setCurrentEnvt(org.rascalmpl.interpreter.env.Environment environment) {
		this.__getEval().setCurrentEnvt(environment);
	}

	public void unwind(org.rascalmpl.interpreter.env.Environment old) {
		this.__getEval().unwind(old);
	}

}
