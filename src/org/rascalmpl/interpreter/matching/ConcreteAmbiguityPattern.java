package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;

class ConcreteAmbiguityPattern extends AbstractMatchingResult {

	public ConcreteAmbiguityPattern(
			IEvaluatorContext ctx, CallOrTree x,
			java.util.List<AbstractBooleanResult> args) {
		super(ctx);
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
