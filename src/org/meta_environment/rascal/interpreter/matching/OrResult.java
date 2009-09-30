package org.meta_environment.rascal.interpreter.matching;

import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;

/**
 * The or boolean operator backtracks for both the lhs and the rhs. This means
 * that if the lhs or rhs have multiple ways of assigning a value to a variable,
 * this and operator will be evaluated as many times.
 * 
 * Note that variables introduced in the left hand side will NOT be visible in the
 * right hand side. The right hand side is only evaluated if the left hand side is false,
 * which means that no variables have been bound. Also note that both sides of a
 * disjunction are required to introduce exactly the same variables of exactly the same
 * type
 * 
 * @author jurgenv
 *
 */
public class OrResult extends AbstractBooleanResult {
	private final IBooleanResult left;
	private final IBooleanResult right;

	public OrResult(IEvaluatorContext ctx, IBooleanResult left, IBooleanResult right) {
		super(ctx);
		this.left = left;
		this.right = right;
	}

	@Override
	public void init() {
		super.init();
		left.init();
		right.init();
		hasNext = true;
	}

	@Override
	public boolean hasNext() {
		return hasNext && (left.hasNext() || right.hasNext());
	}
	
	@Override
	public boolean next() {
		// note how we clean up introduced variables, but only if one of the branches fails
		if (left.hasNext()) {
			Environment old = ctx.getCurrentEnvt();
			ctx.pushEnv();
			if (left.next()) {
				hasNext = false;
				return true;
			}
			ctx.unwind(old);
		}
		
		if (right.hasNext()) {
			Environment old = ctx.getCurrentEnvt();
			ctx.pushEnv();
			if (right.next()) {
				hasNext = false;
				return true;
			}
			ctx.unwind(old);
		}
		
		hasNext = false;
		return false;
	}
}
