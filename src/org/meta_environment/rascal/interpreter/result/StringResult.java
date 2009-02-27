package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;

public class StringResult extends ValueResult {

	private IString string;
	
	public StringResult(IString string) {
		this.string = string;
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
		return new StringResult(s.getValue().concat(getValue()));
	}	
	
	
}
