package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.type.Type;

public class StringResult extends ValueResult {

	private IString string;
	
	public StringResult(Type type, IString string) {
		super(type, string);
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
		return new StringResult(type, s.getValue().concat(getValue()));
	}	
	
	
}
