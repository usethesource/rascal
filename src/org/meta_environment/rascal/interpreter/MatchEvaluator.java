package org.meta_environment.rascal.interpreter;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.Expression;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.BoolResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

/*
 * Evaluate match and no match expression
 */

public class MatchEvaluator implements Iterator<Result<IValue>> {
	private boolean positive;
	private boolean hasNext = true;
	private MatchPattern mp;
	private Evaluator evaluator;
	private Environment pushedEnv;
	private Environment oldEnv;
	
	// TODO: remove use of evaluator here! it's not good to have this dependency
	MatchEvaluator(Expression pat, Expression subject, boolean positive, Environment env, Evaluator ev){
    	this.positive = positive;
    	this.evaluator = ev;
    	this.oldEnv = ev.getCurrentEnvt();
    	this.pushedEnv = evaluator.pushEnv();
    	//System.err.println("MatchEvaluator: push " + pat);
     	IValue subjectValue = subject.accept(ev).getValue();
    	mp = ev.evalPattern(pat);
   	    // Type check is done by each pattern
        //	if(!ev.mayMatch(mp.getType(pushedEnv), subjectValue.getType()))
    	
    	//Temporarily disabled while implementing concrete syntax matching   <------
    	
    	//if(!mp.mayMatch(subjectValue, pushedEnv))
    	//	throw new UnexpectedTypeError(mp.getType(pushedEnv), subjectValue.getType(), pat);
    	mp.initMatch(subjectValue, evaluator.getCurrentEnvt());
	}

	public boolean hasNext() {
		if(hasNext){
			boolean hn = mp.hasNext();
			if(!hn){
				hasNext = false;
				//System.err.println("MatchEvaluator: pop");
				evaluator.setCurrentEnvt(oldEnv);
			}
			//System.err.println("MatchEvaluator.hasNext: " + hn);
			return hn;
		}
		//System.err.println("MatchEvaluator.hasNext: false");
		return false;
	}

	@SuppressWarnings("unchecked")
	public Result next() {
		//System.err.println("MatchEvaluator: next");
		if(hasNext()){	
			boolean result = positive ? mp.next() : !mp.next();
			//System.err.println("MatchEvaluator.next: " + result);
			return new BoolResult(result, this, null);
		}
		//System.err.println("MatchEvaluator.next: false");
		return new BoolResult(!positive, this, null);
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for MatchEvaluator");
	}
}