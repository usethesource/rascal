package org.meta_environment.rascal.interpreter.matching;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.Names;

class TypedVariableBecomesPattern extends AbstractMatchingResult {
	
	private String name;
	private Type declaredType;
	private IMatchingResult pat;
	private boolean debug = false;

	TypedVariableBecomesPattern(IValueFactory vf, EvaluatorContext ctx,
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name aname, IMatchingResult pat){
		super(vf, ctx);
		this.name = Names.name(aname);
		this.declaredType = type;
		this.pat = pat;
		Environment env = ctx.getCurrentEnvt();
		
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
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject);
		Environment env = ctx.getCurrentEnvt();
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
			ctx.getCurrentEnvt().storeVariable(name, subject);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}