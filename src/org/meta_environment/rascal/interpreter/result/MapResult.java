package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IMap;

public class MapResult extends AbstractResult {
	private IMap map;
	
	public MapResult(IMap map) {
		this.map = map;
	}
	
	@Override 
	public AbstractResult add(AbstractResult result) {
		return result.addMap(this);
		
	}
	
	////
	
	@Override
	protected MapResult addMap(MapResult m) {
		return new MapResult(m.getMap().join(getMap()));
	}
	
	public IMap getMap() {
		return map;
	}

	@Override
	public IMap getValue() {
		return map;
	}
	
}
