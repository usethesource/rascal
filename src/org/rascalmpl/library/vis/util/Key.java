package org.rascalmpl.library.vis.util;

import org.eclipse.imp.pdb.facts.IValue;

public interface Key<T> {
	
	void registerValue(IValue val);
	
	void registerOffset(double offset);
	
	T scaleValue(IValue val);
	
	String getId();
	

}
