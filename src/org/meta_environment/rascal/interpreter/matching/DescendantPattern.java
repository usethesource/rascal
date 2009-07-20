package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

class DescendantPattern extends AbstractPattern implements MatchPattern {

	private MatchPattern pat;
	private Evaluator eval;
	private EnumerateAndMatch enumAndMatch;

	public DescendantPattern(IValueFactory vf, EvaluatorContext ctx, MatchPattern pat) {
		super(vf, ctx);
		this.eval = ctx.getEvaluator();
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return evaluator.mayOccurIn(getType(env), subjectType);
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		enumAndMatch = new EnumerateAndMatch(pat, makeResult(subject.getType(), subject, ctx), eval);
	}
	
	@Override
	public boolean hasNext(){
		boolean r =  initialized &&  enumAndMatch.hasNext();
		return r;
	}

	@Override
	public boolean next() {
		if(enumAndMatch.next().isTrue()){
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
