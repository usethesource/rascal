package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class TypedVariablePattern extends AbstractMatchingResult {
	private String name;
	org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean anonymous = false;
	private boolean debug = false;
	private boolean iDeclaredItMyself;

	
	// TODO: merge code of the following two constructors.
	
	public TypedVariablePattern(IValueFactory vf, EvaluatorContext ctx, org.eclipse.imp.pdb.facts.type.Type type,
			org.meta_environment.rascal.ast.QualifiedName qname) {
		super(vf, ctx);
		this.name = Names.name(Names.lastName(qname));
		this.declaredType = type;
		this.anonymous = name.equals("_");
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
	}
	
	public TypedVariablePattern(IValueFactory vf, EvaluatorContext ctx, 
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name name) {
		super(vf, ctx);
		this.name = Names.name(name);
		this.declaredType = type;
		this.anonymous = name.toString().equals("_");
		this.iDeclaredItMyself = false;
		if(debug) System.err.println("AbstractPatternTypedVariabe: " + name);
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

		if (!anonymous && !iDeclaredItMyself && !ctx.getCurrentEnvt().declareVariable(declaredType, name)) {
			throw new RedeclaredVariableError(name, ctx.getCurrentAST());
		}
		
		iDeclaredItMyself = true;
		
		if (subject.getValue().getType().isSubtypeOf(declaredType)) {
			if(debug)System.err.println("matches");
			
			if(anonymous) {
				return true;
			}
			
			ctx.getCurrentEnvt().storeVariable(name, ResultFactory.makeResult(declaredType, subject.getValue(), ctx));
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