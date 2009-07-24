package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;

public class DescendantPattern extends AbstractMatchingResult  {
	private IMatchingResult pat;
	private IBooleanResult enumAndMatch;

	public DescendantPattern(IValueFactory vf, EvaluatorContext ctx, IMatchingResult pat) {
		super(vf, ctx);
		this.pat = pat;
		// nice trick, we reuse the enumerator here because that does traversal, 
		// in the future, we may put the traversal in this class and remove it from enumerator
		this.enumAndMatch = new EnumeratorResult(vf, ctx, pat, null, null);
	}

	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return ctx.getEvaluator().mayOccurIn(getType(env), subjectType);
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		enumAndMatch.init();
	}
	
	@Override
	public boolean hasNext(){
		boolean r =  initialized &&  enumAndMatch.hasNext();
		return r;
	}

	@Override
	public boolean next() {
		if(enumAndMatch.next()){
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return subject.getValue();
	}
}
