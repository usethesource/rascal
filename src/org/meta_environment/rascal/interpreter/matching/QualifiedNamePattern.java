package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class QualifiedNamePattern extends AbstractMatchingResult {
	protected org.meta_environment.rascal.ast.QualifiedName name;
	private Type declaredType;
	protected boolean anonymous = false;
	private boolean debug = false;
	private boolean iWroteItMySelf;
	
	public QualifiedNamePattern(IValueFactory vf, IEvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name){
		super(vf, ctx);
		this.name = name;
		this.anonymous = getName().equals("_");
		Environment env = ctx.getCurrentEnvt();
		
		// Look for this variable while we are constructing this pattern
		if(anonymous){
			declaredType = TypeFactory.getInstance().valueType();
		} else {
			Result<IValue> varRes = env.getVariable(name);
			if (varRes == null || varRes.getType() == null) {
				declaredType = TypeFactory.getInstance().valueType();
			} else {
				declaredType = varRes.getType();
			}
		}
		
		iWroteItMySelf = false;
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		// do not reinit iWroteItMyself!
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
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
		if(!hasNext) {
			return false;
		}
		hasNext = false;
		
		if (debug) System.err.println("AbstractPatternQualifiedName.match: " + name);
		
		// Anonymous variables matches always
		if (anonymous) {
			return true;
		}
	
		if (iWroteItMySelf) {
			// overwrite a previous binding
			ctx.getCurrentEnvt().storeVariable(name, subject);
			return true;
		}
		// either bind the variable or check for equality
		
		Result<IValue> varRes = ctx.getCurrentEnvt().getVariable(name);
		if (varRes == null) {
			// inferred declaration
			declaredType = subject.getType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariableError(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
			iWroteItMySelf = true;
			return true;
		}
		else if (varRes.getValue() == null) {
			declaredType = varRes.getType();
			if (!ctx.getCurrentEnvt().declareVariable(declaredType, getName())) {
				throw new RedeclaredVariableError(getName(), ctx.getCurrentAST());
			}
			ctx.getCurrentEnvt().storeVariable(name, subject);
			iWroteItMySelf = true;
			return true;
		}
		else {
			// equality check
			if (subject.getValue().getType().isSubtypeOf(varRes.getType())) {
				if(debug) {
					System.err.println("returns " + subject.equals(varRes));
				}
//					iWroteItMySelf = false;
				return subject.equals(varRes).isTrue();
			}
			return false;
		}
	}
	
	@Override
	public String toString(){
		return name + "==" + subject;
	}
}
