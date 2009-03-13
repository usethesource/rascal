package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class ListResult extends CollectionResult<IList> {
	public ListResult(Type type, IList list) {
		super(type, list);
	}
		
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addList(this);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> AbstractResult<U> subtract(AbstractResult<V> result) {
		return result.subtractList(this);
	}

	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> multiply(AbstractResult<V> result) {
		return result.multiplyList(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> in(AbstractResult<V> result) {
		return result.inList(this);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> notIn(AbstractResult<V> result) {
		return result.notInList(this);
	}	
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareList(this);
	}
	
	/////
	
	@Override
	protected <U extends IValue> AbstractResult<U> addList(ListResult l) {
		// Note the reverse concat
		return makeResult(type.lub(l.type), l.value.concat(l.value));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> subtractList(ListResult l) {
		throw new ImplementationError("NYI");
		// Note the reverse 
		//return new ListResult(l.list.(l.list));
	}

	@Override
	protected <U extends IValue> AbstractResult<U> multiplyList(ListResult that) {
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
		return makeResult(type, w.done());	
	}
	
	
	@Override
	<U extends IValue, V extends IValue> AbstractResult<U> insertElement(ValueResult<V> that) {
		return makeResult(resultTypeWhenAddingElement(that), value.insert(that.getValue()));
	}
	
	<U extends IValue, V extends IValue> AbstractResult<U> appendElement(ValueResult<V> that) {
		// this is called by addLists in element types.
		return makeResult(resultTypeWhenAddingElement(that), value.append(that.getValue()));
	}

	<U extends IValue, V extends IValue> AbstractResult<U> removeElement(ValueResult<V> value) {
		throw new ImplementationError("NYI: pdb has no remove on list");
		//return new ListResult(list.remove(value.getValue())
	}

	<U extends IValue, V extends IValue> AbstractResult<U> elementOf(ValueResult<V> elementResult) {
		throw new ImplementationError("NYI: pdb has no contains on lists");
		//return new BoolResult(getValue().contains(elementResult.getValue());
	}

	<U extends IValue, V extends IValue> AbstractResult<U> notElementOf(ValueResult<V> elementResult) {
		throw new ImplementationError("NYI: pdb has no contains on lists");
		//return new BoolResult(!getValue().contains(elementResult.getValue());
	}
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareList(ListResult that) {
		// Note reversed args
		IList left = that.getValue();
		IList right = this.getValue();
		int compare = new Integer(left.length()).compareTo(right.length());
		if (compare != 0) {
			return makeIntegerResult(compare);
		}
		for (int i = 0; i <= left.length(); i++) {
			compare = compareValues(left.get(i), right.get(i));
			if (compare != 0) {
				return makeIntegerResult(compare);
			}
		}
		return makeIntegerResult(0);
	}

	
}
