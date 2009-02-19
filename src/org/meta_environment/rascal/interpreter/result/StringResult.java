package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public class StringResult extends AbstractResult {

	private IString string;
	
	public StringResult(IString string) {
		this.setString(string);
	}
	
	@Override
	public IString getValue() {
		return string;
	}
	
	@Override
	public AbstractResult add(AbstractResult result) {
		return result.addString(this);
	}

	
	//////////////////////
	
	@Override
	public StringResult addString(StringResult s) {
		// Note the reverse concat.
		return new StringResult(s.getString().concat(getString()));
	}

	@Override
	protected AbstractResult subtractSet(SetResult s) {
		throw new ImplementationError("NYI");
		//return new SetResult(s.getSet().delete(this));
	}

	@Override
	protected SetResult addSet(SetResult s) {
		return s.addString(this);
	}

	@Override
	protected ListResult addList(ListResult s) {
		return s.appendResult(this);
	}

	
	
	public void setString(IString string) {
		this.string = string;
	}

	public IString getString() {
		return string;
	}
	
	
	
}
