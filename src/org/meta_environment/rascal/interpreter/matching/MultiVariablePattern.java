package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

/* package */ class MultiVariablePattern extends QualifiedNamePattern {

	MultiVariablePattern(IValueFactory vf, Environment env,
			EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name) {
		super(vf, env, ctx, name);
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		
		// If not anonymous, store the value.
		if(!anonymous) {
			Type type = subject.getType();
			env.storeInnermostVariable(name.toString(), makeResult(type, subject, ctx));
		}
		return true;
	}
	
}