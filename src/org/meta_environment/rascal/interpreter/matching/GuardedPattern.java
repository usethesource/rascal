package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

class GuardedPattern extends AbstractPattern implements MatchPattern {
	private Type type;
	private MatchPattern pat;
	
	GuardedPattern(IValueFactory vf, EvaluatorContext ctx, Type type, MatchPattern pat){
		super(vf, ctx);
		this.type = type;
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		if(!mayMatch(pat.getType(env), type))
			throw new UnexpectedTypeError(pat.getType(env), type, ctx.getCurrentAST());
		this.hasNext = pat.getType(env).equivalent(type);
	}

	@Override
	public boolean next() {
		return pat.next();
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}