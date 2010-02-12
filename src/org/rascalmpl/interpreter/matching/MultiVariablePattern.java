package org.rascalmpl.interpreter.matching;

import org.rascalmpl.interpreter.IEvaluatorContext;

public class MultiVariablePattern extends QualifiedNamePattern {

	public MultiVariablePattern(
			IEvaluatorContext ctx, org.rascalmpl.ast.QualifiedName name) {
		super(ctx, name);
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		
		// If not anonymous, store the value.
		if(!anonymous) {
			ctx.getCurrentEnvt().storeVariable(name.toString(), subject);
		}
		return true;
	}
	
}