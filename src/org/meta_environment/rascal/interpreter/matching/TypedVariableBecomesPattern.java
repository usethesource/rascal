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
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

class TypedVariableBecomesPattern extends AbstractPattern implements MatchPattern {
	
	private String name;
	private Type declaredType;
	private Environment env;
	private MatchPattern pat;
	private boolean debug = false;

	TypedVariableBecomesPattern(IValueFactory vf, Environment env, EvaluatorContext ctx,
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name aname, MatchPattern pat){
		super(vf, ctx);
		this.name = Names.name(aname);
		this.declaredType = type;
		this.env = env;
		this.pat = pat;
		
		if(debug) System.err.println("AbstractPatternTypedVariableBecomes: " + type + " " + name);
		Result<IValue> innerRes = env.getInnermostVariable(name);
		if(innerRes != null){
			throw new RedeclaredVariableError(this.name, aname);
		}
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			System.err.println("localRes != null");
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}

		Result<IValue> globalRes = env.getVariable(null,name);
		if(globalRes != null){
			System.err.println("globalRes != null");
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
		env.storeInnermostVariable(name, makeResult(type, null, ctx));
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		if(!mayMatch(pat.getType(env), declaredType))
			throw new UnexpectedTypeError(pat.getType(env), declaredType, ctx.getCurrentAST());
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public boolean hasNext(){
		return pat.hasNext();
	}

	@Override
	public boolean next() {
		if(debug) System.err.println("AbstractPatternTypedVariableBecomes:  next");
		if(pat.next()){
			Result<IValue> r = ResultFactory.makeResult(declaredType, subject, ctx);
			env.storeVariable(name, r);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}