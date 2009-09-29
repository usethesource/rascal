package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.PatternEvaluator;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

public class MatchResult extends AbstractBooleanResult {
	private boolean positive;
	private IMatchingResult mp;
	private Expression expression;
	private boolean firstTime;
	private Expression pattern;
	
	public MatchResult(IEvaluatorContext ctx, Expression pattern, boolean positive, Expression expression) {
		super(ctx);
    	this.positive = positive;
    	this.pattern = pattern;
    	this.mp = null;
    	this.expression = expression;
	}

    @Override
    public void init() {
    	super.init();
    	
    	// because the right hand side may introduce types that are needed
    	// in the left-hand side, we first need to evaluate the expression
    	// before we construct a pattern.
		Result<IValue> result = expression.accept(ctx.getEvaluator());
		Type subjectType = result.getType();

		if (mp == null) {
			mp = pattern.accept(new PatternEvaluator(ctx));
		}
		
    	mp.initMatch(expression.accept(ctx.getEvaluator()));

    	if(!mp.mayMatch(subjectType, ctx.getCurrentEnvt())) {
    		throw new UnexpectedTypeError(mp.getType(ctx.getCurrentEnvt()), subjectType, ctx.getCurrentAST());
    	}
    	
    	firstTime = true;
    }

    @Override
	public boolean hasNext() {
    	if (positive) {
    		return mp.hasNext();
    	}
    	
		if (firstTime) {
			return true;
		}
		return mp.hasNext();
	}

    @Override
	public boolean next() {
    	firstTime = false;
		// TODO: should manage escape variable from negative matches!!!
		if(hasNext()){	
			return positive ? mp.next() : !mp.next();
		}
		
		return !positive;
	}
}