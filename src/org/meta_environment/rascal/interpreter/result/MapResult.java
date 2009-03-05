package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.type.Type;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class MapResult extends ValueResult<IMap> {
	
	public MapResult(Type type, IMap map) {
		super(type, map);
	}
	
	@Override 
	public AbstractResult add(AbstractResult result) {
		return result.addMap(this);
		
	}
	
	////
	
	@Override
	protected MapResult addMap(MapResult m) {
		// Note the reverse
		return makeResult(type, m.value.join(value));
	}
	
}
