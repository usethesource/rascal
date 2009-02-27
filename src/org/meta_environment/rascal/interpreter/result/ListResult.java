package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class ListResult extends CollectionResult {

	private IList list;
	
	public ListResult(Type type, IList list) {
		super(type, list);
		this.list = list;
	}
	
	@Override
	public IList getValue() {
		return list;
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
		return new ListResult(type, l.list.concat(l.list));
	}

	@Override
	public ListResult subtractList(ListResult l) {
		throw new ImplementationError("NYI");
		// Note the reverse 
		//return new ListResult(l.list.(l.list));
	}

	@Override
	protected ListResult multiplyList(ListResult that) {
		Type t1 = getValueType().getElementType();
		Type t2 = that.getValueType().getElementType();
		// Note: reverse
		Type type = getTypeFactory().listType(getTypeFactory().tupleType(t2, t1));
		IListWriter w = type.writer(getValueFactory());
		for (IValue v1 : that.getValue()) {
			for (IValue v2 : getValue()) {
				w.append(getValueFactory().tuple(v1, v2));	
			}
		}
		return new ListResult(type, w.done());	
	}
	
	ListResult insertElement(ValueResult n) {
		return new ListResult(type, list.insert(n.getValue()));
	}
	
	ListResult appendElement(ValueResult n) {
		// this is called by addLists in element types.
		return new ListResult(type, list.append(n.getValue()));
	}

	ListResult removeElement(ValueResult value) {
		throw new ImplementationError("NYI: pdb has no remove on list");
		//return new ListResult(list.remove(value.getValue())
	}

	BoolResult elementOf(ValueResult elementResult) {
		throw new ImplementationError("NYI: pdb has no contains on lists");
		//return new BoolResult(getValue().contains(elementResult.getValue());
	}

	BoolResult notElementOf(ValueResult elementResult) {
		throw new ImplementationError("NYI: pdb has no contains on lists");
		//return new BoolResult(!getValue().contains(elementResult.getValue());
	}

	
}
