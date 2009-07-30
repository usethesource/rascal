package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;

public class NotPattern extends AbstractMatchingResult {
	private final IMatchingResult arg;

	public NotPattern(IValueFactory vf, IEvaluatorContext ctx, IMatchingResult arg) {
		super(vf, ctx);
		this.arg = arg;
	}

	@Override
	public void initMatch(Result<IValue> subject) {
		arg.initMatch(subject);
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

	@Override
	public Type getType(Environment env) {
		return arg.getType(env);
	}

	@Override
	public IValue toIValue(Environment env) {
		throw new ImplementationError("did not expect to instantiate a negative pattern");
	}

}
