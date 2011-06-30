package org.rascalmpl.library.vis.util;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.properties.Properties;

public interface Key {
	
	void registerValue(Properties prop,IValue val);
	
	void registerOffset(double offset);
	
	IValue scaleValue(IValue val);
	
	String getId();
	

}
