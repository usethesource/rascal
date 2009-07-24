package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;
import static org.meta_environment.rascal.interpreter.result.ResultFactory.bool;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedSubscriptArityError;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class ListResult extends CollectionResult<IList> {
	
	public ListResult(Type type, IList list, EvaluatorContext ctx) {
		super(type, list, ctx);
	}
		
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> result, EvaluatorContext ctx) {
		return result.addList(this, ctx);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> Result<U> subtract(Result<V> result, EvaluatorContext ctx) {
		return result.subtractList(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> multiply(Result<V> result, EvaluatorContext ctx) {
		return result.multiplyList(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> in(Result<V> result, EvaluatorContext ctx) {
		return result.inList(this, ctx);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> notIn(Result<V> result, EvaluatorContext ctx) {
		return result.notInList(this, ctx);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compare(Result<V> result, EvaluatorContext ctx) {
		return result.compareList(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that, EvaluatorContext ctx) {
		return that.equalToList(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> nonEquals(Result<V> that, EvaluatorContext ctx) {
		return that.nonEqualToList(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThan(Result<V> that, EvaluatorContext ctx) {
		return that.lessThanList(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> lessThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return that.lessThanOrEqualList(this, ctx);
	}

	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThan(Result<V> that, EvaluatorContext ctx) {
		return that.greaterThanList(this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> greaterThanOrEqual(Result<V> that, EvaluatorContext ctx) {
		return that.greaterThanOrEqualList(this, ctx);
	}


	@Override
	@SuppressWarnings("unchecked")
	public <U extends IValue, V extends IValue> Result<U> subscript(Result<?>[] subscripts, EvaluatorContext ctx) {
		if (subscripts.length != 1) {
			throw new UnsupportedSubscriptArityError(getType(), subscripts.length, ctx.getCurrentAST());
		}
		Result<IValue> key = (Result<IValue>) subscripts[0];
		if (!key.getType().isIntegerType()) {
			throw new UnexpectedTypeError(TypeFactory.getInstance().integerType(), key.getType(), ctx.getCurrentAST());
		}
		IInteger index = ((IInteger)key.getValue());
		if (index.intValue() >= getValue().length()) {
			throw RuntimeExceptionFactory.indexOutOfBounds(index, ctx.getCurrentAST(), null);
		}
		return makeResult(getType().getElementType(), getValue().get(index.intValue()), ctx);
	}

	/////
	
	@Override
	protected <U extends IValue> Result<U> addList(ListResult l, EvaluatorContext ctx) {
		// Note the reverse concat
		return makeResult(getType().lub(l.getType()), l.getValue().concat(getValue()), ctx);
	}

	@Override
	protected <U extends IValue> Result<U> subtractList(ListResult l, EvaluatorContext ctx) {
		// Note the reversal of args
		IList list = l.getValue();
		for (IValue v: getValue()) {
			while (list.contains(v)) {
				list = list.delete(v);
			}
		}
		return makeResult(l.getType(), list, ctx);
	}

	@Override
	protected <U extends IValue> Result<U> multiplyList(ListResult that, EvaluatorContext ctx) {
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
		return makeResult(type, w.done(), ctx);	
	}
	
	
	@Override
	<U extends IValue, V extends IValue> Result<U> insertElement(ElementResult<V> that, EvaluatorContext ctx) {
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.insert(that.getValue()), ctx);
	}
	
	<U extends IValue, V extends IValue> Result<U> appendElement(ElementResult<V> that, EvaluatorContext ctx) {
		// this is called by addLists in element types.
		Type newType = getTypeFactory().listType(that.getType().lub(getType().getElementType()));
		return makeResult(newType, value.append(that.getValue()), ctx);
	}

	<U extends IValue, V extends IValue> Result<U> removeElement(ElementResult<V> value, EvaluatorContext ctx) {
		throw new ImplementationError("NYI: pdb has no remove on list");
		//return new ListResult(list.remove(value.getValue())
	}

	<U extends IValue, V extends IValue> Result<U> elementOf(ElementResult<V> elementResult, EvaluatorContext ctx) {
		return bool(getValue().contains(elementResult.getValue()));
	}

	<U extends IValue, V extends IValue> Result<U> notElementOf(ElementResult<V> elementResult, EvaluatorContext ctx) {
		return bool(!getValue().contains(elementResult.getValue()));
	}
	
	@Override
	protected <U extends IValue> Result<U> equalToList(ListResult that, EvaluatorContext ctx) {
		return that.equalityBoolean(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> nonEqualToList(ListResult that, EvaluatorContext ctx) {
		return that.nonEqualityBoolean(this);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanList(ListResult that, EvaluatorContext ctx) {
		// note reverse of arguments: we need that < this
		// TODO: move to PDB:
		if (that.getValue().isEqual(getValue())) {
			return bool(false);
		}
		return lessThanOrEqualList(that, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> lessThanOrEqualList(ListResult that, EvaluatorContext ctx) {
		for (IValue value: that.getValue()) {
			if (!getValue().contains(value)) {
				return bool(false);
			}
		}
		return bool(true);
	}

	@Override
	protected <U extends IValue> Result<U> greaterThanList(ListResult that, EvaluatorContext ctx) {
		// note double reversal of arguments: that >  this
		return that.lessThanList(this, ctx);
	}
	
	@Override
	protected <U extends IValue> Result<U> greaterThanOrEqualList(ListResult that, EvaluatorContext ctx) {
		// note double reversal of arguments: that >=  this
		return that.lessThanOrEqualList(this, ctx);
	}
	
	
	@Override
	protected <U extends IValue> Result<U> compareList(ListResult that, EvaluatorContext ctx) {
		// Note reversed args
		IList left = that.getValue();
		IList right = this.getValue();
		int compare = Integer.valueOf(left.length()).compareTo(Integer.valueOf(right.length()));
		if (compare != 0) {
			return makeIntegerResult(compare, ctx);
		}
		for (int i = 0; i < left.length(); i++) {
			compare = compareIValues(left.get(i), right.get(i), ctx);
			if (compare != 0) {
				return makeIntegerResult(compare, ctx);
			}
		}
		return makeIntegerResult(0, ctx);
	}

	
}
