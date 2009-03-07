package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class ListResult extends CollectionResult<IList> {
	public ListResult(Type type, IList list) {
		super(type, list);
	}
		
	@Override
	public AbstractResult add(AbstractResult result) {
		return result.addList(this);
	}
	
	@Override 
	public AbstractResult subtract(AbstractResult result) {
		return result.subtractList(this);
	}

	@Override
	public AbstractResult multiply(AbstractResult result) {
		return result.multiplyList(this);
	}
	
	@Override
	public AbstractResult in(AbstractResult result) {
		return result.inList(this);
	}	
	
	@Override
	public AbstractResult notIn(AbstractResult result) {
		return result.notInList(this);
	}	
	
	/////
	
	@Override
	public ListResult addList(ListResult l) {
		// Note the reverse concat
		return makeResult(type.lub(l.type), l.value.concat(l.value));
	}

	@Override
	public ListResult subtractList(ListResult l) {
		throw new ImplementationException("NYI");
		// Note the reverse 
		//return new ListResult(l.list.(l.list));
	}

	protected ListResult multiplyList(ListResult that) {
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
	
	
	ListResult insertElement(ValueResult that) {
		return makeResult(resultTypeWhenAddingElement(that), value.insert(that.getValue()));
	}
	
	ListResult appendElement(ValueResult that) {
		// this is called by addLists in element types.
		return makeResult(resultTypeWhenAddingElement(that), value.append(that.getValue()));
	}

	ListResult removeElement(ValueResult value) {
		throw new ImplementationException("NYI: pdb has no remove on list");
		//return new ListResult(list.remove(value.getValue())
	}

	BoolResult elementOf(ValueResult elementResult) {
		throw new ImplementationException("NYI: pdb has no contains on lists");
		//return new BoolResult(getValue().contains(elementResult.getValue());
	}

	BoolResult notElementOf(ValueResult elementResult) {
		throw new ImplementationException("NYI: pdb has no contains on lists");
		//return new BoolResult(!getValue().contains(elementResult.getValue());
	}

	
}
