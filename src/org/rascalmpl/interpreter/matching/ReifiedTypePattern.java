package org.rascalmpl.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class ReifiedTypePattern extends AbstractMatchingResult {
	private final BasicType basic;
	private final NodePattern nodePattern;

	public ReifiedTypePattern(IEvaluatorContext ctx, Expression x, BasicType type, java.util.List<IMatchingResult> arguments) {
		super(ctx, x);
		this.basic = type;
        this.nodePattern = new NodePattern(ctx, x, new LiteralPattern(ctx, type, ctx.getValueFactory().string(basic.toString())), null, arguments);
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
	public List<String> getVariables() {
		return nodePattern.getVariables();
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
}
