package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

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
	private boolean leftResult;

	public AndResult(IValueFactory vf, IEvaluatorContext ctx, IBooleanResult left, IBooleanResult right) {
		super(vf, ctx);
		this.left = left;
		this.right = right;
	}

	public void init() {
		left.init();
		// do not right.init() yet since it may use variables introduced by the first left.next();
		firstMatch = true;
	}

	public boolean hasNext() {
		if (firstMatch) {
			return left.hasNext();
		}
		
		return left.hasNext() || (leftResult == true && right.hasNext());
	}
	
	@Override
	public boolean next() {
		if (firstMatch) {
			firstMatch = false;
			ctx.goodPushEnv();
			leftResult = left.next();
			
			if (leftResult) {
				right.init();
				return right.next();
			}
			return false;
		}
		
		if (leftResult && right.hasNext()) {
			// first do the right.next because && would short cut it which leads to an infinite loop 
			// because right will always have a true hasNext() then.
			boolean rightResult = right.next();
			return leftResult && rightResult;
		}

		ctx.goodPushEnv();
		leftResult = left.next();

		if (leftResult) {
			right.init();
			return right.next();
		}
		return false;
	}
}
