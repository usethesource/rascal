package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class GuardedPattern extends AbstractMatchingResult {
	private Type type;
	private IMatchingResult pat;
	
	public GuardedPattern(IEvaluatorContext ctx, Expression.Guarded x, Type type, IMatchingResult pat){
		super(ctx, x);
		this.type = type;
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public boolean hasNext() {
		return pat.hasNext();
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		
		Environment env = ctx.getCurrentEnvt();
		
		if (type instanceof NonTerminalType && pat.getType(env).isSubtypeOf(tf.stringType()) && subject.getValue().getType().isSubtypeOf(Factory.Tree)) {
			if (subject.getValue().getType().isSubtypeOf(type)) {
				subject = ResultFactory.makeResult(tf.stringType(), ctx.getValueFactory().string(TreeAdapter.yield((IConstructor) subject.getValue())), ctx);
				pat.initMatch(subject);
				this.hasNext = pat.hasNext();
			}
			else {
				this.hasNext = false;
			}
		}
		else {
			pat.initMatch(subject);
			// this code triggers during a visit which might encounter other stuff that would never match
//			if (!mayMatch(pat.getType(env), type)) {
//				throw new UnexpectedTypeError(pat.getType(env), type, ctx.getCurrentAST());
//			}
			this.hasNext = pat.getType(env).equivalent(type);
		}
		
	}

	@Override
	public boolean next() {
		return pat.next();
	}
}