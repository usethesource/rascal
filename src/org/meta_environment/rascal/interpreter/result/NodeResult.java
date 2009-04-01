package org.meta_environment.rascal.interpreter.result;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;

import org.meta_environment.rascal.ast.AbstractAST;

public class NodeResult extends ElementResult<INode> {

	public NodeResult(Type type, INode node) {
		super(type, node);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToNode(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToNode(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> result, AbstractAST ast) {
		return result.lessThanNode(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.lessThanOrEqualNode(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> result, AbstractAST ast) {
		return result.greaterThanNode(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> result, AbstractAST ast) {
		return result.greaterThanOrEqualNode(this, ast);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ast);
		}
		if (!((Result<IValue>)subscripts[0]).getType().isIntegerType()) {
			throw new UnexpectedTypeError(getTypeFactory().integerType(), 
					((Result<IValue>)subscripts[0]).getType(), ast);
		}
		IInteger index = ((IntegerResult)subscripts[0]).getValue();
		if (index.intValue() >= getValue().arity()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ast);
		}
		Type elementType = getTypeFactory().valueType();
		return makeResult(elementType, getValue().get(index.intValue()));
	}
	
	//////
	
	@Override
	protected <U extends IValue> Result<U> lessThanNode(NodeResult that, AbstractAST ast) {
		// note reversed args: we need that < this
		return bool(that.comparisonInts(this, ast) < 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualNode(NodeResult that, AbstractAST ast) {
		// note reversed args: we need that <= this
		return bool(that.comparisonInts(this, ast) <= 0);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanNode(NodeResult that, AbstractAST ast) {
		// note reversed args: we need that > this
		return bool(that.comparisonInts(this, ast) > 0);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualNode(NodeResult that, AbstractAST ast) {
		// note reversed args: we need that >= this
		return bool(that.comparisonInts(this, ast) >= 0);
	}

	@Override
	protected <U extends IValue> Result<U> equalToNode(NodeResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToNode(NodeResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
}
