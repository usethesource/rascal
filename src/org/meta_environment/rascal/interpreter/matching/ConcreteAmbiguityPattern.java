package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;

class ConcreteAmbiguityPattern extends AbstractMatchingResult {

	public ConcreteAmbiguityPattern(IValueFactory vf,
			IEvaluatorContext ctx, CallOrTree x,
			java.util.List<AbstractBooleanResult> args) {
		super(vf, ctx);
	}

	@Override
	public Type getType(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean next() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
