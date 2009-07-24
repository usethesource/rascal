package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;

/**
 * The and boolean operator backtracks for both the lhs and the rhs. This means
 * that if the lhs or rhs have multiple ways of assigning a value to a variable,
 * this and operator will be evaluated as many times.
 * 
 * Also note that variables introduced in the lhs of the and can be used directly
 * in the rhs of the and, but not vice versa.
 * 
 * @author jurgenv
 *
 */
public class AndResult extends AbstractBooleanResult {
	private final IBooleanResult left;
	private final IBooleanResult right;
	private boolean firstMatch = true;

	public AndResult(IValueFactory vf, EvaluatorContext ctx, IBooleanResult left, IBooleanResult right) {
		super(vf, ctx);
		this.left = left;
		this.right = right;
	}

	public void init() {
		left.init();
		right.init();
		firstMatch = true;
	}

	public boolean hasNext() {
		if (firstMatch) {
			return left.hasNext() && right.hasNext();
		}
		
		return right.hasNext() || left.hasNext();
	}
	
	@Override
	public boolean next() {
		if (firstMatch) {
			firstMatch = false;
			ctx.goodPushEnv();
			return left.next() && right.next();
		}
		
		if (right.hasNext()) {
			return right.next();
		}
		
		ctx.goodPushEnv();
		right.init();
		return left.next() && right.next();
	}
}
