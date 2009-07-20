package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

class VariableBecomesPattern extends AbstractPattern implements MatchPattern {
	
	private String name;
	private Environment env;
	private MatchPattern pat;

	VariableBecomesPattern(IValueFactory vf, Environment env, EvaluatorContext ctx, 
			org.meta_environment.rascal.ast.Name aname, MatchPattern pat){
		super(vf, ctx);
		this.name = Names.name(aname);
		this.env = env;
		this.pat = pat;
		
		Result<IValue> innerRes = env.getInnermostVariable(name);
		if(innerRes != null){
			throw new RedeclaredVariableError(this.name, aname);
		}
		
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}
		Result<IValue> globalRes = env.getVariable(null,name);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
		env.storeInnermostVariable(name, makeResult(tf.valueType(), null, ctx));
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
	}
	
	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean hasNext(){
		return pat.hasNext();
	}

	@Override
	public boolean next() {
		if(pat.next()){	
			Result<IValue> r = ResultFactory.makeResult(subject.getType(), subject, ctx);
			env.storeInnermostVariable(name, r);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}