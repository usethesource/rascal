package org.rascalmpl.library.vis.util;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.properties.Properties;

public interface Key<T> {
	
	void registerValue(IValue val);
	
	IValue scaleValue(IValue val);
	
	

}
