package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

public class MapResult extends ValueResult<IMap> {
	
	public MapResult(Type type, IMap map) {
		super(type, map);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addMap(this);
		
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareMap(this);
	}
	
	
	////
	
	@Override
	protected <U extends IValue> AbstractResult<U> addMap(MapResult m) {
		// Note the reverse
		return makeResult(type, m.value.join(value));
	}
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareMap(MapResult that) {
		// Note reversed args
		IMap left = that.getValue();
		IMap right = this.getValue();
		if (left.isEqual(right)) {
			return makeIntegerResult(0);
		}
		if (left.isSubMap(left)) {
			return makeIntegerResult(-1);
		}
		return makeIntegerResult(1);
	}
	
}
