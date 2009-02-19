package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class ListResult extends AbstractResult {

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


	/////
	
	@Override
	public ListResult addList(ListResult l) {
		// Note the reverse concat
		return new ListResult(l.list.concat(l.list));
	}

	// TODO: make a single add for atomic types adding.
	// TODO: introduce a intermediate result class for it?
	
	@Override
	protected ListResult addReal(RealResult n) {
		// Note: insert here, not append
		return new ListResult(getList().insert(n.getReal()));
	}
	
	@Override
	protected ListResult addInteger(IntegerResult n) {
		// Note: insert here, not append
		return new ListResult(getList().insert(n.getInteger()));
	}

	@Override
	protected ListResult addString(StringResult n) {
		// Note: insert here, not append
		return new ListResult(getList().insert(n.getString()));
	}
	
	@Override
	protected ListResult addBool(BoolResult n) {
		// Note: insert here, not append
		return new ListResult(getList().insert(n.getBool()));
	}

	public IList getList() {
		return list;
	}

	public ListResult appendResult(AbstractResult n) {
		// this is called by addLists in element types.
		return new ListResult(list.append(n.getValue()));
	}

	
}
