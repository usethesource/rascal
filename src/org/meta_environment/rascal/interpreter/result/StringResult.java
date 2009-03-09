package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class StringResult extends ValueResult<IString> {

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
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addString(this);
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareString(this);
	}
	
	//////////////////////
	
	@Override
	public <U extends IValue> AbstractResult<U> addString(StringResult s) {
		// Note the reverse concat.
		return makeResult(type, s.getValue().concat(getValue()));
	}	
	
	@Override
	public <U extends IValue> AbstractResult<U> compareString(StringResult that) {
		// note reversed args
		IString left = that.getValue();
		IString right = this.getValue();
		int result = left.compare(right);
		return makeIntegerResult(result);
	}
	
	
}
