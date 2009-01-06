package org.meta_environment.rascal.interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.impl.hash.ValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.OperatorAsValue.Not;
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
	
	EvalResult getArg(Expression expr){
		EvalResult argResult = expr.accept(ev);
		if(!argResult.type.isBoolType()){
			throw new RascalTypeError("Argument of || should be of type bool and not " + argResult.type);
		}
		return argResult;
	};
	
	void def(int i){
		if(result[i] == null){
			result[i] = getArg(expr[i]);
		}
	}
	
	void redef(int i){
		result[i] = getArg(expr[i]);
	}
	
	public boolean hasNext(){
		return (result[LEFT] != null && result[LEFT].hasNext()) || (result[RIGHT] != null && result[RIGHT].hasNext());
	}
	
	public EvalResult next(){
		return null;
	}
	
	public boolean nextResult(int i){
		def(i);
		if(result[i].hasNext()){
			result[i] = result[i].next();
			return true;
		}
		return false;
	}
	
	public boolean nextResult(int i, boolean expected){
		def(i);
		while(result[i].hasNext()){
			result[i] = result[i].next();
			if(((IBool)result[i].value).getValue() == expected){
				return true;
			}
		}
		return false;
	}
	
	public boolean nextTrueLeft(){
		return nextResult(LEFT, true);
	}
	
	public boolean nextTrueRight(){
		return nextResult(RIGHT, true);
	}
	
	public boolean nextFalseLeft(){
		return nextResult(LEFT, false);
	}
	
	public boolean nextLeft(){
		return nextResult(LEFT);
	}
	
	public boolean nextRight(){
		return nextResult(RIGHT);
	}
}

class AndEvaluator extends BooleanEvaluator {
	
	AndEvaluator(And x, Evaluator ev){
		super(x.getLhs(), x.getRhs(), ev);
	}

	@Override
	public EvalResult next() {
		if(isTrueLeft()){
			if(nextTrueRight()){
				return new BooleanEvalResult(this, true);
			}
			
		if(!nextTrueLeft()){
			return new BooleanEvalResult(this, false);
		}
		if(!nextTrueRight()){
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
		if(nextTrueLeft()){
			return new BooleanEvalResult(this, true);
		}
		if(nextTrueRight()){
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
		if(nextFalseLeft()){
			return new BooleanEvalResult(this, true);
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
		if(nextLeft()){
			if(!(((IBool)result[LEFT].value).getValue())){
				return new BooleanEvalResult(this, true);
			}
			
		}
		if(((IBool)left.value).getValue()){
			return new BooleanEvalResult(this, true);
		}
		nextTrueRight();
		if(((IBool)right.value).getValue()){
			return new BooleanEvalResult(this, true);
		}
		return new BooleanEvalResult(this, false);
	}
}
