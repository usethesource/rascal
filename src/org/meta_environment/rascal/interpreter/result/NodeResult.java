package org.meta_environment.rascal.interpreter.result;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class NodeResult extends ElementResult<INode> {

	public NodeResult(Type type, INode node, IEvaluatorContext ctx) {
		super(type, node, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, IEvaluatorContext ctx) {
		return that.equalToNode(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, IEvaluatorContext ctx) {
		return that.nonEqualToNode(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanNode(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.lessThanOrEqualNode(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanNode(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, IEvaluatorContext ctx) {
		return result.greaterThanOrEqualNode(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that, IEvaluatorContext ctx) {
		return that.compareNode(this, ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, IEvaluatorContext ctx) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		if (!((Result<IValue>)subscripts[0]).getType().isIntegerType()) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), 
					((Result<IValue>)subscripts[0]).getType(), ctx.getCurrentAST());
		}
		IInteger index = ((IntegerResult)subscripts[0]).getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), ctx.getStackTrace());
		}
		Type elementType = getTypeFactory().valueType();
		return makeResult(elementType, getValue().get(index.intValue()), ctx);
	}
	
	//////
	
	@Override
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ctx) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ctx) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ctx) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that, IEvaluatorContext ctx) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ctx) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, IEvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, IEvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareNode(NodeResult that,
			IEvaluatorContext ctx) {
		// Note reversed args
		INode left = that.getValue();
		INode right = this.getValue();
		
		if (left.isEqual(right)) {
			return makeIntegerResult(0, ctx);
		}
		
		int str = left.getName().compareTo(right.getName());
		
		if (str == 0) {
			int leftArity = left.arity();
			int rightArity = right.arity();
			
			if (leftArity == rightArity) {
				Type valueType = getTypeFactory().valueType();
				
				for (int i = 0; i < leftArity; i++) {
					Result<IInteger> comp = makeResult(valueType, left.get(i), ctx).compare(makeResult(valueType, right.get(i), ctx), ctx);
					int val = comp.getValue().intValue();
					
					if (val != 0) {
						return makeIntegerResult(val, ctx);
					}
				}
				
				throw new ImplementationError("This should not happen, all children are equal");
			}
			
			if (left.arity() < right.arity()) {
				return makeIntegerResult(-1, ctx);
			}
			
			return makeIntegerResult(1, ctx);
		}
		
		return makeIntegerResult(str, ctx);
	}
	
}
