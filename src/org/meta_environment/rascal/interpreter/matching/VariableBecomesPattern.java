package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class VariableBecomesPattern extends AbstractMatchingResult {
	private String name;
	private IMatchingResult pat;

	public VariableBecomesPattern(IValueFactory vf, IEvaluatorContext ctx, 
			org.meta_environment.rascal.ast.Name aname, IMatchingResult pat){
		super(vf, ctx);
		this.name = Names.name(aname);
		this.pat = pat;
		Environment env = ctx.getCurrentEnvt();
		
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
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject);
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
			ctx.getCurrentEnvt().storeInnermostVariable(name, subject);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}