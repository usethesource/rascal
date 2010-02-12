package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public class NotPattern extends AbstractMatchingResult {
	private final IMatchingResult arg;

	public NotPattern(IEvaluatorContext ctx, IMatchingResult arg) {
		super(ctx);
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
		ctx.pushEnv();
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
