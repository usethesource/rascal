package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.MatchPattern;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;

class AntiPattern extends AbstractPattern implements MatchPattern {

	private MatchPattern pat;
	private java.util.List<String> patVars;

	public AntiPattern(IValueFactory vf, EvaluatorContext ctx, MatchPattern pat) {
		super(vf, ctx);
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		
		java.util.List<String> vars = pat.getVariables();
		patVars = new java.util.ArrayList<String>(vars.size());
		for(String name : vars){
			Result<IValue> vr = env.getVariable(null, name);
			if(vr == null || vr.getValue() == null)
				patVars.add(name);
		}
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return pat.mayMatch(subjectType, env);
	}

	@Override
	public boolean next() {
		boolean res = pat.next();
		// Remove any bindings
		for(String var : patVars){
			ctx.getCurrentEnvt().storeVariable(var,  ResultFactory.nothing());
		}
		return !res;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}
