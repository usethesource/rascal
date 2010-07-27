package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public class NotPattern extends AbstractMatchingResult {
	private final IMatchingResult arg;

	public NotPattern(IEvaluatorContext ctx, Expression x, IMatchingResult arg) {
		super(ctx, x);
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
}
