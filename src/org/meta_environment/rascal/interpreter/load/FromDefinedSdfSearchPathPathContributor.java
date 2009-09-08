package org.meta_environment.rascal.interpreter.load;

import java.util.LinkedList;
import java.util.List;

public class FromDefinedSdfSearchPathPathContributor implements ISdfSearchPathContributor {
	public List<String> contributePaths() {
		String path = System.getProperty("rascal.sdf.path");
		List<String> paths = new LinkedList<String>();
		
		for (String p : path.split(":")) {
			paths.add(p);
		}
		
		return paths;
	}
}
