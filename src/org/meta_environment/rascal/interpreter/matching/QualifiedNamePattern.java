package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

/* package */ class QualifiedNamePattern extends AbstractPattern implements MatchPattern {
	protected org.meta_environment.rascal.ast.QualifiedName name;
	private Type type;
	protected boolean anonymous = false;
	private boolean debug = false;
	protected Environment env; 
	
	QualifiedNamePattern(IValueFactory vf, Environment env, EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name){
		super(vf, ctx);
		this.name = name;
		this.anonymous = getName().equals("_");
		this.env = env;
		// Look for this variable while we are constructing this pattern
		if(anonymous){
			type = TypeFactory.getInstance().valueType();
		} else {
			Result<IValue> varRes = env.getVariable(name);
			if(varRes == null || varRes.getValue() == null){
				type = TypeFactory.getInstance().valueType();
			} else {
				type = varRes.getType();
			}
		}
	}
	
	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(getName());
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return Names.name(Names.lastName(name));
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name);
		
		// Anonymous variables matches always
		if(anonymous) {
			return true;
		}
	
		Result<IValue> varRes = env.getVariable(name);
		if((varRes == null) || (varRes.getValue() == null)){
			// Is the variable still undefined?
			if(debug)System.err.println("name= " + name + ", subject=" + subject + ",");
			type = subject.getType();
			env.storeInnermostVariable(getName(), makeResult(type, subject, ctx));
			return true;
		}
		
		// ... or has it already received a value during matching?
		IValue varVal = varRes.getValue();
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name + ", subject=" + subject + ", value=" + varVal);
		if (subject.getType().isSubtypeOf(varRes.getType())) {
			if(debug) {
				System.err.println("returns " + makeResult(subject.getType(),subject, ctx).equals(varRes));
			}
			return makeResult(subject.getType(),subject, ctx).equals(varRes, ctx).isTrue();
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return name + "==" + subject;
	}
}
