package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.interpreter.env.EvalResult;

class BooleanEvalResult extends EvalResult {
	BooleanEvaluator beval;
	
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
	Expression leftExpr;
	Expression rightExpr;
	EvalResult left;
	EvalResult right;
	boolean firstTime;
	Evaluator ev;
	
	BooleanEvaluator(Expression leftExpr, Expression rightExpr, Evaluator ev){
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
		this.ev = ev;
		firstTime = true;	
	}
	
	EvalResult getArg(Expression expr){
		EvalResult argResult = expr.accept(ev);
		if(!argResult.type.isBoolType()){
			throw new RascalTypeError("Argument of || should be of type bool and not " + argResult.type);
		}
		return argResult;
	};
	
	void defLeft(){
		if(firstTime){
			left = getArg(leftExpr);
		}
	}
	
	void defRight(){
		if(firstTime){
			right = getArg(rightExpr);
		}
	}
	
	public boolean hasNext(){
		return firstTime || left.hasNext() || (right != null && right.hasNext());
	}
	
	public EvalResult next(){
		return null;
	}

}

class AndEvaluator extends BooleanEvaluator {
	
	AndEvaluator(And x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {
		
		defLeft();
		while(left.hasNext() && !((IBool)left.value).getValue()){
			left = left.next();
		}
		if(!left.hasNext()){
			return new BooleanEvalResult(this, false);
		}
		defRight();	
		while(right.hasNext() && !((IBool)right.value).getValue()){
			right = right.next();
		}
		if(!left.hasNext()){
			return new BooleanEvalResult(this, false);
		}
		return new BooleanEvalResult(this, true);
	}
}

class OrEvaluator extends BooleanEvaluator {
	
	OrEvaluator(Or x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {
		
		defLeft();
		while(left.hasNext() && !((IBool)left.value).getValue()){
			left = left.next();
		}
		if(left.hasNext()){
			return new BooleanEvalResult(this, true);
		}
		defRight();	
		while(right.hasNext() && !((IBool)right.value).getValue()){
			right = right.next();
		}
		if(left.hasNext()){
			return new BooleanEvalResult(this, true);
		}
		return new BooleanEvalResult(this, false);
	}
}
