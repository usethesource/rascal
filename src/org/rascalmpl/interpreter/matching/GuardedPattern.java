package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.TreeAdapter;

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