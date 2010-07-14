package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class ComposedFunctionResult extends AbstractFunction{
	private final AbstractFunction left;
	private final AbstractFunction right;

	public ComposedFunctionResult(AbstractFunction left, AbstractFunction right, IEvaluatorContext ctx) {
		super(ctx.getCurrentAST(), 
				ctx.getEvaluator(), 
				(FunctionType) RascalTypeFactory.getInstance().functionType(
						right.getFunctionType().getReturnType(), left.getFunctionType().getArgumentTypes()), 
						false, 
						ctx.getCurrentEnvt(), false);
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues) {
		Result<IValue> rightResult = right.call(argTypes, argValues);
		return left.call(new Type[] { rightResult.getType() }, new IValue[] { rightResult.getValue() });
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeFunction(this);
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
