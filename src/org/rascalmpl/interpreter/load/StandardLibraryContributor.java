package org.rascalmpl.interpreter.load;

import java.io.File;
import java.net.URI;
import java.util.List;


public class StandardLibraryContributor implements
		IRascalSearchPathContributor {
	
	private StandardLibraryContributor() { }
	
	private static class InstanceHolder {
		public static StandardLibraryContributor sInstance = new StandardLibraryContributor();
	}
	
	public static StandardLibraryContributor getInstance() {
		return InstanceHolder.sInstance;
	}
	
	public void contributePaths(List<URI> l) {
		l.add(java.net.URI.create("cwd:///"));
		l.add(java.net.URI.create("std:///"));
		l.add(java.net.URI.create("testdata:///"));
		l.add(java.net.URI.create("benchmarks:///"));
		
		String property = java.lang.System.getProperty("rascal.path");

		if (property != null) {
			for (String path : property.split(":")) {
				l.add(new File(path).toURI());
			}
		}
	}

	@Override
	public String toString() {
		return "[current wd and stdlib]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this;
	}
}