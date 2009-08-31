package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;

public class ComposedFunctionResult extends AbstractFunction implements IExternalValue {
	private final AbstractFunction left;
	private final AbstractFunction right;

	public ComposedFunctionResult(AbstractFunction left, AbstractFunction right, IEvaluatorContext ctx) {
		super(ctx.getCurrentAST(), 
				ctx.getEvaluator(), 
				(FunctionType) RascalTypeFactory.getInstance().functionType(
						right.getFunctionType().getReturnType(), left.getFunctionType().getArgumentTypes()), 
						false, 
						ctx.getCurrentEnvt());
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Result<?> call(Type[] argTypes, IValue[] argValues,
			IEvaluatorContext ctx) {
		Result<?> rightResult = right.call(argTypes, argValues, ctx);
		return left.call(new Type[] { rightResult.getType() }, new IValue[] { rightResult.getValue() }, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(
			Result<V> right, IEvaluatorContext ctx) {
		return right.composeFunction(this, ctx);
	}
	
	public <T> T accept(IValueVisitor<T> v) throws VisitorException {
		return v.visitExternal(this);
	}

	public boolean isEqual(IValue other) {
		return other == this;
	}
	
	@Override
	public boolean isIdentical(IValue other) throws FactTypeUseException {
		return other == this;
	}

}
