package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class MapResult extends ValueResult<IMap> {
	
	public MapResult(Type type, IMap map) {
		super(type, map);
	}
	
	@Override 
	public <U extends IValue, V extends IValue> AbstractResult<U> add(AbstractResult<V> result) {
		return result.addMap(this);
		
	}
	
	////
	
	@Override
	protected <U extends IValue> AbstractResult<U> addMap(MapResult m) {
		// Note the reverse
		return makeResult(type, m.value.join(value));
	}
	
}
