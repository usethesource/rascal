package org.rascalmpl.library.vis.util;

import org.rascalmpl.library.vis.properties.Properties;

public interface Key<To> {
	
	void registerValue(Properties prop,Object val);
	
	void registerOffset(double offset);
	
	To scaleValue(Object val);
	
	String getId();
	

}
