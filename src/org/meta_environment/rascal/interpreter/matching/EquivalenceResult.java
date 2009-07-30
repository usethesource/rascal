package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;

/**
 * The equivalence boolean operator backtracks for both the lhs and the rhs. This means
 * that if the lhs or rhs have multiple ways of assigning a value to a variable,
 * this and operator will be evaluated as many times.
 */
public class EquivalenceResult extends AbstractBooleanResult {
	private final IBooleanResult left;
	private final IBooleanResult right;
	private boolean firstMatch = true;
	private boolean leftResult;

	public EquivalenceResult(IValueFactory vf, IEvaluatorContext ctx, IBooleanResult left, IBooleanResult right) {
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
			Environment old = ctx.getCurrentEnvt();
			boolean rightResult;
			try {
				ctx.goodPushEnv();
				leftResult = left.next();
			}
			finally {
				ctx.unwind(old);
			}
			try {
				ctx.goodPushEnv();
				rightResult = right.next();
			}
			finally {
				ctx.unwind(old);
			}
			
			return leftResult == rightResult;
		}
		
		if (right.hasNext()) {
			Environment old = ctx.getCurrentEnvt();
			try {
				ctx.goodPushEnv();
				return leftResult == right.next();
			}
			finally {
				ctx.unwind(old);
			}
		}
		
		Environment old = ctx.getCurrentEnvt();
		try {
			ctx.goodPushEnv();
			leftResult = left.next();
		}
		finally {
			ctx.unwind(old);
		}
		
		try {
			right.init();
			return leftResult == right.next();
		}
		finally {
			ctx.unwind(old);
		}
	}
}
