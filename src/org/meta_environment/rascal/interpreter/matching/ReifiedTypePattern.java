package org.meta_environment.rascal.interpreter.matching;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.BasicType;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;

public class ReifiedTypePattern extends AbstractMatchingResult {
	private final BasicType basic;
	private final NodePattern nodePattern;

	public ReifiedTypePattern(IEvaluatorContext ctx, BasicType type, java.util.List<IMatchingResult> arguments) {
		super(ctx);
		this.basic = type;
        this.nodePattern = new NodePattern(ctx, new LiteralPattern(ctx, ctx.getValueFactory().string(basic.toString())), null, arguments);
	}

	@Override
	public Type getType(Environment env) {
		// TODO: check if this would do it
		return RascalTypeFactory.getInstance().reifiedType(tf.valueType());
	}

	@Override
	public void initMatch(final Result<IValue> subject) {
		super.initMatch(subject);
		nodePattern.initMatch(subject);
		hasNext = nodePattern.hasNext();
	}
	
	@Override
	public boolean next() {
		if (hasNext()) {
			boolean result = nodePattern.next();
			hasNext = nodePattern.hasNext();
			return result;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO: check if this would do it
		return nodePattern.toIValue(env);
	}

}
