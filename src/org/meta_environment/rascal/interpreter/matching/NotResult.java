package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;

/**
 * The not operator backtracks over its argument just like the other operator.
 * Notice however that any variables introduced below a not will never be available after it.
 * 
 * @author jurgenv
 *
 */
public class NotResult extends AbstractBooleanResult {
	private final IBooleanResult arg;

	public NotResult(IValueFactory vf, EvaluatorContext ctx, IBooleanResult arg) {
		super(vf, ctx);
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
		ctx.goodPushEnv();
		try {
			return !arg.next();
		}
		finally {
			ctx.unwind(old);
		}
	}
}
