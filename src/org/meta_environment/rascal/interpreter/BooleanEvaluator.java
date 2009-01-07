package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.interpreter.env.EvalResult;

class BooleanEvalResult extends EvalResult {
	BooleanEvaluator beval;
	
	BooleanEvalResult(BooleanEvaluator beval){
		super(TypeFactory.getInstance().boolType(), null);
		this.beval = beval;
	}
	
	BooleanEvalResult(BooleanEvaluator beval, boolean b){
		super(TypeFactory.getInstance().boolType(), ValueFactory.getInstance().bool(b));
		this.beval = beval;
	}
	
	public boolean hasNext(){
		return beval.hasNext();
	}
	
	public EvalResult next(){
		return beval.next();
	}
}

public abstract class BooleanEvaluator {
	static final int LEFT = 0;
	static final int RIGHT = 1;
	Expression expr[];
	EvalResult result[];
	Evaluator ev;
	
	BooleanEvaluator(Expression leftExpr, Expression rightExpr, Evaluator ev){
		expr = new Expression[] { leftExpr, rightExpr };
		this.ev = ev;
		result = new EvalResult[] { null, null };
	}
	
	void defArg(int i){
		System.err.println("defArg: " + i + ", "+ expr[i]);
		EvalResult argResult = expr[i].accept(ev);
		if(!argResult.type.isBoolType()){
			throw new RascalTypeError("Argument of boolean operator should be of type bool and not " + argResult.type);
		}
		result[i] = argResult;
	};
	
	void def(int i){
		if(result[i] == null){
			defArg(i);
		}
	}
	
	void redef(int i){
		defArg(i);
	}
	
	public boolean hasNext(){
		return (result[LEFT] != null && result[LEFT].hasNext()) || (result[RIGHT] != null && result[RIGHT].hasNext());
	}
	
	public EvalResult next(){
		return null;
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
			if(((IBool)result[i].value).getValue() == expected){
				return true;
			}
		}
		while(result[i].hasNext()){
			result[i] = result[i].next();
			if(((IBool)result[i].value).getValue() == expected){
				return true;
			}
		}
		return false;
	}
	
	public boolean is(int i, boolean expected){
		if(result[i] == null){
			defArg(i);
		}
		return ((IBool)result[i].value).getValue() == expected;
	}

}

class AndEvaluator extends BooleanEvaluator {
	
	AndEvaluator(And x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {
		System.err.println("AndEvaluator");
		if(is(LEFT, false)){
			if(!getNextResult(LEFT,true)){
				return new BooleanEvalResult(this, false);
			}
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT,true)){
				return new BooleanEvalResult(this, true);
			}
			if(getNextResult(LEFT,true)){
				redef(RIGHT);
				return next();
			}
		}
		return new BooleanEvalResult(this, false);
	}
}

class OrEvaluator extends BooleanEvaluator {
	
	OrEvaluator(Or x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {	
		if(getNextResult(LEFT, true)){
			return new BooleanEvalResult(this, true);
		}
		if(getNextResult(RIGHT,true)){
			return new BooleanEvalResult(this, true);
		}
		return new BooleanEvalResult(this, false);
			
	}
}

class NegationEvaluator extends BooleanEvaluator {
	
	NegationEvaluator(Negation x, Evaluator ev){
		super(x.getArgument(), null, ev);
	}

	@Override
	public EvalResult next() {		
		if(getNextResult(LEFT)){
			return new BooleanEvalResult(this, !((IBool)result[LEFT].value).getValue());
		}
		return new BooleanEvalResult(this, false);
	}
}

class ImplicationEvaluator extends BooleanEvaluator {
	
	ImplicationEvaluator(Implication x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {
		if(is(LEFT,false)){
			if(getNextResult(RIGHT)){
				return new BooleanEvalResult(this, true);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BooleanEvalResult(this, false);
		}
		if(is(LEFT, true)){
			if(getNextResult(RIGHT, true)){
				return new BooleanEvalResult(this, true);
			}
			if(getNextResult(LEFT)){
				redef(RIGHT);
				return next();
			} 
			return new BooleanEvalResult(this, false);
		}
	
		return  new BooleanEvalResult(this, false);
	}
}
