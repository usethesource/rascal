package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.types.NonTerminalType;
import org.meta_environment.uptr.TreeAdapter;

public class GuardedPattern extends AbstractMatchingResult {
	private Type type;
	private IMatchingResult pat;
	
	public GuardedPattern(IEvaluatorContext ctx, Type type, IMatchingResult pat){
		super(ctx);
		this.type = type;
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		
		Environment env = ctx.getCurrentEnvt();
		
		if (type instanceof NonTerminalType && pat.getType(env).isSubtypeOf(tf.stringType())) {
			subject = ResultFactory.makeResult(tf.stringType(), ctx.getValueFactory().string(TreeAdapter.yield((IConstructor) subject.getValue())), ctx);
			pat.initMatch(subject);
		}
		else {
			pat.initMatch(subject);
			if (!mayMatch(pat.getType(env), type)) {
				throw new UnexpectedTypeError(pat.getType(env), type, ctx.getCurrentAST());
			}
		}
		this.hasNext = pat.getType(env).equivalent(type);
	}

	@Override
	public boolean next() {
		return pat.next();
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}