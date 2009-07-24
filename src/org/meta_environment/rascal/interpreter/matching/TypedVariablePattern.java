package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.Names;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

/* package */class TypedVariablePattern extends AbstractMatchingResult {
	private String name;
	org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean anonymous = false;
	private boolean debug = false;

	
	// TODO: merge code of the following two constructors.
	
	TypedVariablePattern(IValueFactory vf, EvaluatorContext ctx, org.eclipse.imp.pdb.facts.type.Type type,
			org.meta_environment.rascal.ast.QualifiedName qname) {
		super(vf, ctx);
		this.name = Names.name(Names.lastName(qname));
		this.declaredType = type;
		this.anonymous = name.equals("_");
		Environment env = ctx.getCurrentEnvt();
		
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
		
		Result<IValue> localRes = env.getLocalVariable(qname);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, qname);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, qname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(qname, makeResult(localRes.getType(), null, ctx));
			return;
		}
		Result<IValue> globalRes = env.getVariable(qname);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, qname);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, qname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(qname, makeResult(globalRes.getType(), null, ctx));
			return;
		}
		
		
	}
	
	TypedVariablePattern(IValueFactory vf, EvaluatorContext ctx, 
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name name) {
		super(vf, ctx);
		this.name = Names.name(name);
		this.declaredType = type;
		this.anonymous = name.toString().equals("_");
		Environment env = ctx.getCurrentEnvt();
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
		
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, name);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, name);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}
	
		Result<IValue> globalRes = env.getVariable(name, this.name);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, name);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, name);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name);
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}

	@Override
	public boolean next() {
		if(debug)System.err.println("AbstractTypedVariable.next");
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug) {
			System.err.println("Subject: " + subject + " name: " + name + " getType: ");
			System.err.println("AbstractTypedVariable.next: " + subject + "(type=" + subject.getType() + ") with " + declaredType + " " + name);
		}
		
		if (subject.getType().isSubtypeOf(declaredType)) {
			if(!anonymous)
				ctx.getCurrentEnvt().storeInnermostVariable(name, subject);
			if(debug)System.err.println("matches");
			return true;
		}
		if(debug)System.err.println("no match");
		return false;
	}
	
	@Override
	public String toString(){
		return declaredType + " " + name + "==" + subject;
	}
}