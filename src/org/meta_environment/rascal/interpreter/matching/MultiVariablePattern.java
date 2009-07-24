package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;

/* package */ class MultiVariablePattern extends QualifiedNamePattern {

	MultiVariablePattern(IValueFactory vf,
			EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name) {
		super(vf, ctx, name);
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		
		// If not anonymous, store the value.
		if(!anonymous) {
			ctx.getCurrentEnvt().storeInnermostVariable(name.toString(), subject);
		}
		return true;
	}
	
}