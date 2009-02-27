package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.type.Type;

public class MapResult extends ValueResult {
	private IMap map;
	
	public MapResult(Type type, IMap map) {
		super(type, map);
		this.map = map;
	}
	
	@Override 
	public AbstractResult add(AbstractResult result) {
		return result.addMap(this);
		
	}
	
	////
	
	@Override
	protected MapResult addMap(MapResult m) {
		return new MapResult(type, m.getMap().join(getMap()));
	}
	
	public IMap getMap() {
		return map;
	}

	@Override
	public IMap getValue() {
		return map;
	}
	
}
