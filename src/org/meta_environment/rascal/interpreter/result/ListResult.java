package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class ListResult extends CollectionResult {

	private IList list;
	
	public ListResult(IList list) {
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
		return new ListResult(l.list.concat(l.list));
	}

	@Override
	public ListResult subtractList(ListResult l) {
		throw new ImplementationError("NYI");
		// Note the reverse 
		//return new ListResult(l.list.(l.list));
	}

	
	ListResult insertElement(ValueResult n) {
		return new ListResult(list.insert(n.getValue()));
	}
	
	ListResult appendElement(ValueResult n) {
		// this is called by addLists in element types.
		return new ListResult(list.append(n.getValue()));
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
