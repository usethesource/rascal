package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

public class MatchResult extends AbstractBooleanResult {
	private boolean positive;
	private IMatchingResult mp;
	private Expression expression;
	
	public MatchResult(IValueFactory vf, EvaluatorContext ctx, IMatchingResult pat, boolean positive, Expression expression) {
		super(vf, ctx);
    	this.positive = positive;
    	this.mp = pat;
    	this.expression = expression;
	}

    @Override
    public void init() {
    	super.init();
    	
		Result<IValue> result = expression.accept(ctx.getEvaluator());
		Type subjectType = result.getType();
    	
    	mp.initMatch(expression.accept(ctx.getEvaluator()));

    	if(!mp.mayMatch(subjectType, ctx.getCurrentEnvt())) {
    		throw new UnexpectedTypeError(mp.getType(ctx.getCurrentEnvt()), subjectType, ctx.getCurrentAST());
    	}
    }

    @Override
	public boolean hasNext() {
    	return mp.hasNext();
	}

    @Override
	public boolean next() {
		// TODO: should manage escape variable from negative matches!!!
		if(hasNext()){	
			return positive ? mp.next() : !mp.next();
		}
		
		return !positive;
	}
}