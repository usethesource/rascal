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
	
	void defArg(int i){
		Result argResult = expr[i].accept(evaluator);
		if(!argResult.getType().isBoolType()){
			throw new UnexpectedTypeError(TypeFactory.getInstance().boolType(), argResult.getType(), expr[i]);
		}
		result[i] = argResult;
	};
	
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

	@Override
	public Result next() {
		if(is(LEFT, false)){
			if(!getNextResult(LEFT,true)){
				return new BoolResult(false, this);
			}
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT,true)){
				return new BoolResult(true, this);
			}
			if(getNextResult(LEFT,true)){
				redef(RIGHT);
				return next();
			}
		}
		return new BoolResult(false, this);
	}
}

/*
 * Evaluate or expression
 */

class OrEvaluator extends BooleanEvaluator {
	
	OrEvaluator(Or x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public Result next() {	
		if(getNextResult(LEFT, true)){
			return new BoolResult(true, this);
		}
		if(getNextResult(RIGHT,true)){
			return new BoolResult(true, this);
		}
		return new BoolResult(false, this);
			
	}
}

/*
 * Evaluate negation expression
 */

class NegationEvaluator extends BooleanEvaluator {
	
	NegationEvaluator(Negation x, Evaluator ev){
		super(x.getArgument(), null, ev);
	}

	@Override
	public Result next() {		
		if(getNextResult(LEFT)){
			return new BoolResult(!((IBool)result[LEFT].getValue()).getValue(), this);
		}
		return new BoolResult(false, this);
	}
}

/*
 * Evaluate implication expression
 */
class ImplicationEvaluator extends BooleanEvaluator {
	
	ImplicationEvaluator(Implication x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public Result next() {
		if(is(LEFT,false)){
			if(getNextResult(RIGHT)){
				return new BoolResult(true, this);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this);
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT, true)){
				return new BoolResult(true, this);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this);
		}
	
		return new BoolResult(false, this);
	}
}

/*
 * Evaluate equivalence operator
 */

class EquivalenceEvaluator extends BooleanEvaluator {
	
	EquivalenceEvaluator(Equivalence x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public Result next() {
		if(is(LEFT,false)){
			if(getNextResult(RIGHT,false)){
				return new BoolResult(true, this);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this);
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT, true)){
				return new BoolResult(true, this);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BoolResult(false, this);
		}
	
		return  new BoolResult(false, this);
	}
}

/*
 * Evaluate match and no match expression
 */

class MatchEvaluator implements Iterator<Result<IValue>> {
	private boolean positive;
	private boolean hasNext = true;
	private MatchPattern mp;
	private Evaluator evaluator;
	private Environment pushedEnv;
	
	// TODO: remove use of evaluator here! it's not good to have this dependency and the use
	// of the "global" variable lastPattern complicates things a lot.
	MatchEvaluator(Expression pat, Expression subject, boolean positive, Environment env, Evaluator ev){
    	this.positive = positive;
    	this.evaluator = ev;
    	this.pushedEnv = evaluator.pushEnv();
    	//System.err.println("MatchEvaluator: push " + pat);
    	mp = ev.evalPattern(pat);
    	ev.lastPattern = mp;
    	mp.initMatch(subject.accept(ev).getValue(), evaluator.peek());
	}

	public boolean hasNext() {
		//System.err.println("MatchEvaluator: hasNext");
		if(hasNext){
			boolean hn = mp.hasNext();
			if(!hn){
				hasNext = false;
				//System.err.println("MatchEvaluator: pop");
				evaluator.popUntil(pushedEnv);
			}
			return hn;
		}
		return false;
	}

	public Result next() {
		//System.err.println("MatchEvaluator: next");
		if(hasNext()){	
			boolean result = positive ? mp.next() : !mp.next();
			return new BoolResult(result, this);
		} else
			return new BoolResult(!positive, this);
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for MatchEvaluator");
	}
}