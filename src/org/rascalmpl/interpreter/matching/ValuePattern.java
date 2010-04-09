package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

/**
 * For matching a closed pattern (i.e. a value that simply needs to be checked for equality.
 * This pattern can be used to short-cut pattern matching in several cases.
 */
public class ValuePattern extends AbstractMatchingResult {
	private Result<?> val;

	public ValuePattern(IEvaluatorContext ctx, Result<?> val) {
		super(ctx);
		this.val = val;
	}

	@Override
	public Type getType(Environment env) {
		return val.getType();
	}
	
	@Override
	public void initMatch(Result<IValue> subject) {
		super.initMatch(subject);
		hasNext = ((IBool) subject.equals(val).getValue()).getValue();
	}

	@Override
	public boolean next() {
		boolean result = hasNext;
		hasNext = false;
		return result;
	}

	@Override
	public IValue toIValue(Environment env) {
		return val.getValue();
	}
}
