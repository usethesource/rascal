package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class MultiVariablePattern extends QualifiedNamePattern {

	public MultiVariablePattern(IValueFactory vf,
			IEvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name) {
		super(vf, ctx, name);
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