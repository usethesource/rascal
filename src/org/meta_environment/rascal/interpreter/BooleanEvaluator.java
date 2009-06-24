package org.meta_environment.rascal.interpreter;

/*
 * Implementation of all operators that provide backtracking:
 * - Boolean operators and (&&), or (||), not (!), implies (==>), equivalence (<===>).
 * - Match (~=) and NoMatch (~!)
 */

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.BoolResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;


/*
 * Base class for iterating over the values of the arguments of the Boolean operators.
 */

public abstract class BooleanEvaluator implements Iterator<Result<IValue>> {
	static final int LEFT = 0;
	static final int RIGHT = 1;
	Expression expr[];
	Result<IValue> result[];
	Evaluator evaluator;
	
	@SuppressWarnings("unchecked")
	BooleanEvaluator(Expression leftExpr, Expression rightExpr, Evaluator evaluator){
		expr = new Expression[] { leftExpr, rightExpr };
		this.evaluator = evaluator;
		result = new Result[] { null, null };
	}
	
	@SuppressWarnings("unchecked")
	void defArg(int i){
		Result<IValue> argResult = expr[i].accept(evaluator);
		if(!argResult.getType().isBoolType()){
			throw new UnexpectedTypeError(TypeFactory.getInstance().boolType(), argResult.getType(), expr[i]);
		}
		result[i] = argResult;
	}
	
	void def(int i){
		if(result[i] == null){
			defArg(i);
		}
	}
	
	void redef(int i){
		result[i] = null;
	}
	
	public boolean hasNext(){
		return (result[LEFT] != null && result[LEFT].hasNext()) || 
		        (result[RIGHT] != null && result[RIGHT].hasNext());
	}
	
	public Result<IValue>  next(){
		throw new ImplementationError("next on empty BooleanEvaluator");
	}
	
	public void remove(){
		throw new ImplementationError("remove() in BooleanEvaluator not implemented");
	}
	
	public boolean getNextResult(int i){
		if(result[i] == null){
			defArg(i);
			return true;
		}
		while(result[i].hasNext()){
			result[i] = result[i].next();
			return true;
		}
		return false;
	}
	
	public boolean getNextResult(int i, boolean expected){
		if(result[i] == null){
			defArg(i);
			if(((IBool)result[i].getValue()).getValue() == expected){
				return true;
			}
		}
		while(result[i].hasNext()){
			result[i] = result[i].next();
			if(((IBool)result[i].getValue()).getValue() == expected){
				return true;
			}
		}
		return false;
	}
	
	public boolean is(int i, boolean expected){
		if(result[i] == null){
			defArg(i);
		}
		return ((IBool)result[i].getValue()).getValue() == expected;
	}
}

/*
 * Evaluate and expression
 */

class AndEvaluator extends BooleanEvaluator {
	
	AndEvaluator(And x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result next() {
		if(is(LEFT, false)){
			if(!getNextResult(LEFT,true)){
				return new BoolResult(false, this, null);
			}
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT,true)){
				return new BoolResult(true, this, null);
			}
			if(getNextResult(LEFT,true)){
				redef(RIGHT);
				return next();
			}
		}
		return new BoolResult(false, this, null);
	}
}

/*
 * Evaluate or expression
 */

class OrEvaluator extends BooleanEvaluator {
	
	OrEvaluator(Or x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result next() {	
		if(getNextResult(LEFT, true)){
			return new BoolResult(true, this, null);
		}
		if(getNextResult(RIGHT,true)){
			return new BoolResult(true, this, null);
		}
		return new BoolResult(false, this, null);
			
	}
}

/*
 * Evaluate negation expression
 */

class NegationEvaluator extends BooleanEvaluator {
	
	NegationEvaluator(Negation x, Evaluator ev){
		super(x.getArgument(), null, ev);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result next() {		
		if(getNextResult(LEFT)){
			return new BoolResult(!((IBool)result[LEFT].getValue()).getValue(), this, null);
		}
		return new BoolResult(false, this, null);
	}
}

/*
 * Evaluate implication expression
 */
class ImplicationEvaluator extends BooleanEvaluator {
	
	ImplicationEvaluator(Implication x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result next() {
		if(is(LEFT,false)){
			if(getNextResult(RIGHT)){
				return new BoolResult(true, this, null);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this, null);
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT, true)){
				return new BoolResult(true, this, null);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this, null);
		}
	
		return new BoolResult(false, this, null);
	}
}

/*
 * Evaluate equivalence operator
 */

class EquivalenceEvaluator extends BooleanEvaluator {
	
	EquivalenceEvaluator(Equivalence x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result next() {
		if(is(LEFT,false)){
			if(getNextResult(RIGHT,false)){
				return new BoolResult(true, this, null);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this, null);
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT, true)){
				return new BoolResult(true, this, null);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this, null);
		}
	
		return  new BoolResult(false, this, null);
	}
}

