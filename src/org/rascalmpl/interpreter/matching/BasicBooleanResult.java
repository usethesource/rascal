package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;


public class BasicBooleanResult extends AbstractBooleanResult {
	private Result<IValue> result;
	private org.rascalmpl.ast.Expression expr;
	private boolean firstTime = true;

	public BasicBooleanResult(IEvaluatorContext ctx, Expression expr) {
		super(ctx);
		this.expr = expr;
	}

	@Override
	public void init() {
		super.init();
		firstTime = true;
	}
	
	@Override
	public boolean hasNext() {
		return firstTime;
	}

	@Override
	public boolean next() {
		if (firstTime) {
			/* Evaluate expression only once */
			firstTime = false;
			result = expr.accept(ctx.getEvaluator());
			if (result.getType().isBoolType() && result.getValue() != null) {
				if (result.getValue().isEqual(ctx.getValueFactory().bool(true))) {
					return true;
				}
				
				
				return false;
			}

			throw new UnexpectedTypeError(tf.boolType(), result.getType(), expr);
		}
		
		return false;
	}
}