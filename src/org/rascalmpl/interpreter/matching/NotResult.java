package org.rascalmpl.interpreter.matching;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;

/**
 * The not operator backtracks over its argument just like the other operator.
 * Notice however that any variables introduced below a not will never be available after it.
 * 
 * @author jurgenv
 *
 */
public class NotResult extends AbstractBooleanResult {
	private final IBooleanResult arg;

	public NotResult(IEvaluatorContext ctx, IBooleanResult arg) {
		super(ctx);
		this.arg = arg;
	}

	@Override
	public void init() {
		super.init();
		arg.init();
	}

	@Override
	public boolean hasNext() {
		return arg.hasNext();
	}
	
	@Override
	public boolean next() {
		Environment old = ctx.getCurrentEnvt();
		ctx.pushEnv();
		try {
			return !arg.next();
		}
		finally {
			ctx.unwind(old);
		}
	}
}
