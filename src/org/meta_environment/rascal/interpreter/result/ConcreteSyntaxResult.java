package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;

public class ConcreteSyntaxResult extends ConstructorResult {

	public ConcreteSyntaxResult(Type type, IConstructor cons,
			EvaluatorContext ctx) {
		super(type, cons, ctx);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, EvaluatorContext ctx) {
		return that.equalToConcreteSyntax(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, EvaluatorContext ctx) {
		return that.nonEqualToConcreteSyntax(this, ctx);
	}
	
	private boolean isLayout(IValue v){
		if(v instanceof IConstructor){
			IConstructor cons = (IConstructor) v;
			return cons.getName().equals("layout");
		}
		return false;
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToConcreteSyntax(
			ConcreteSyntaxResult that, EvaluatorContext ctx) {
		IConstructor left = this.getValue();
		IConstructor right = this.getValue();
		if(!left.getName().equals(right.getName())){
			throw new ImplementationError("names unequal");
		}
		int i = 0;
		for (IValue leftKid: left.getChildren()) {
			IValue rightKid = right.get(i);
			i++;
			if(isLayout(leftKid) && isLayout(rightKid))
				continue;
			if(!leftKid.equals(rightKid))
				return (Result<U>) new BoolResult(false, ctx);
		}
		return (Result<U>) new BoolResult(true, ctx);
	}

}
