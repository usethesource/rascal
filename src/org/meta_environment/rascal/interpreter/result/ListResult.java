package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.RuntimeExceptionFactory;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.ast.AbstractAST;

public class ListResult extends CollectionResult<IList> {
	
	public ListResult(Type type, IList list, AbstractAST ast) {
		super(type, list, ast);
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, AbstractAST ast) {
		return result.addList(this, ast);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, AbstractAST ast) {
		return result.subtractList(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, AbstractAST ast) {
		return result.multiplyList(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, AbstractAST ast) {
		return result.inList(this, ast);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, AbstractAST ast) {
		return result.notInList(this, ast);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, AbstractAST ast) {
		return result.compareList(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, AbstractAST ast) {
		return that.equalToList(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, AbstractAST ast) {
		return that.nonEqualToList(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, AbstractAST ast) {
		return that.lessThanList(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.lessThanOrEqualList(this, ast);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, AbstractAST ast) {
		return that.greaterThanList(this, ast);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, AbstractAST ast) {
		return that.greaterThanOrEqualList(this, ast);
	}


	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, AbstractAST ast) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ast);
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getType().isIntegerType()) {
			throw new UnexpectedTypeError(TypeFactory.getInstance().integerType(), key.getType(), ast);
		}
		IInteger index = ((IInteger)key.getValue());
		if (index.intValue() >= getValue().length()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ast, null);
		}
		return makeResult(getType().getElementType(), getValue().get(index.intValue()), ast);
	}

	/////
	
	@Override
	protected <U extends IValue> Result<U> addList(ListResult l, AbstractAST ast) {
		// Note the reverse concat
		return makeResult(getType().lub(l.getType()), l.getValue().concat(getValue()), ast);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult l, AbstractAST ast) {
		// Note the reversal of args
		IList list = l.getValue();
		for (IValue v: getValue()) {
			while (list.contains(v)) {
				list = list.delete(v);
			}
		}
		return makeResult(l.getType(), list, ast);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyList(ListResult that, AbstractAST ast) {
		Type t1 = that.type.getElementType();
		Type t2 = type.getElementType();
		// Note: reverse
		Type type = getTypeFactory().listType(getTypeFactory().tupleType(t1, t2));
		IListWriter w = type.writer(getValueFactory());
		for (IValue v1 : that.getValue()) {
			for (IValue v2 : getValue()) {
				w.append(getValueFactory().tuple(v1, v2));	
			}
		}
		return makeResult(type, w.done(), ast);	
	}
	
	
	@Override
	<U extends IValue, V extends IValue> Result<U> insertElement(ElementResult<V> that, AbstractAST ast) {
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.insert(that.getValue()), ast);
	}
	
	<U extends IValue, V extends IValue> Result<U> appendElement(ElementResult<V> that, AbstractAST ast) {
		// this is called by addLists in element types.
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.append(that.getValue()), ast);
	}

	<U extends IValue, V extends IValue> Result<U> removeElement(ElementResult<V> value, AbstractAST ast) {
		throw new ImplementationError("NYI: pdb has no remove on list");
		//return new ListResult(list.remove(value.getValue())
	}

	<U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return bool(getValue().contains(elementResult.getValue()));
	}

	<U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, AbstractAST ast) {
		return bool(!getValue().contains(elementResult.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that, AbstractAST ast) {
		return that.equalityBoolean(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, AbstractAST ast) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanList(ListResult that, AbstractAST ast) {
		// note reverse of arguments: we need that < this
		// TODO: move to PDB:
		if (that.getValue().isEqual(getValue())) {
			return bool(false);
		}
		return lessThanOrEqualList(that, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that, AbstractAST ast) {
		for (IValue value: that.getValue()) {
			if (!getValue().contains(value)) {
				return bool(false);
			}
		}
		return bool(true);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanList(ListResult that, AbstractAST ast) {
		// note double reversal of arguments: that >  this
		return that.lessThanList(this, ast);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that, AbstractAST ast) {
		// note double reversal of arguments: that >=  this
		return that.lessThanOrEqualList(this, ast);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> compareList(ListResult that, AbstractAST ast) {
		// Note reversed args
		IList left = that.getValue();
		IList right = this.getValue();
		int compare = new Integer(left.length()).compareTo(right.length());
		if (compare != 0) {
			return makeIntegerResult(compare, ast);
		}
		for (int i = 0; i < left.length(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ast);
			if (compare != 0) {
				return makeIntegerResult(compare, ast);
			}
		}
		return makeIntegerResult(0, ast);
	}

	
}
