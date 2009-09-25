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
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToNode(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that) {
		return that.nonEqualToNode(this);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result) {
		return result.lessThanNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result) {
		return result.lessThanOrEqualNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result) {
		return result.greaterThanNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result) {
		return result.greaterThanOrEqualNode(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(
			Result<V> that) {
		return that.compareNode(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts) {
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
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that) {
		return that.equalityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> compareNode(NodeResult that) {
		// Note reversed args
		INode left = that.getValue();
		INode right = this.getValue();
		
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		
		int str = left.getName().compareTo(right.getName());
		
		if (str == 0) {
			int leftArity = left.arity();
			int rightArity = right.arity();
			
			if (leftArity == rightArity) {
				Type valueType = getTypeFactory().valueType();
				
				for (int i = 0; i < leftArity; i++) {
					Result<IInteger> comp = makeResult(valueType, left.get(i), ctx).compare(makeResult(valueType, right.get(i), ctx));
					int val = comp.getValue().intValue();
					
					if (val != 0) {
						return makeIntegerResult(val);
					}
				}
				
				throw new ImplementationError("This should not happen, all children are equal");
			}
			
			if (left.arity() < right.arity()) {
				return makeIntegerResult(-1);
			}
			
			return makeIntegerResult(1);
		}
		
		return makeIntegerResult(str);
	}
	
}
