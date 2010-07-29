package org.rascalmpl.interpreter.matching;

import java.util.Arrays;
import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;

public class AntiPattern extends AbstractMatchingResult {
	private IMatchingResult pat;
	private boolean stop;

	public AntiPattern(IEvaluatorContext ctx, Expression.Anti anti, IMatchingResult pat) {
		super(ctx, anti);
		this.pat = pat;
	}

	@Override 
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		pat.initMatch(subject);
		stop = false;
	}
	
	@Override
	public boolean mayMatch(Type subjectType, Environment env){
		return pat.mayMatch(subjectType, env);
	}
	
	@Override
	public boolean hasNext() {
		return !stop && pat.hasNext();
	}
	
	@Override
	public boolean next() {
		Environment old = ctx.getCurrentEnvt();

		while (pat.hasNext()) {
			try {
				ctx.pushEnv();
				if (pat.next()) {
					stop = true;
					return false;
				}
			}
			finally {
				ctx.unwind(old);
			}
		}
		
		return true;
	}
}
