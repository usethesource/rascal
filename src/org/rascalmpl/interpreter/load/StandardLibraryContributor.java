package org.rascalmpl.interpreter.load;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.rascalmpl.uri.URIUtil;


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
		l.add(URIUtil.rootScheme("cwd"));
		l.add(URIUtil.rootScheme("std"));
		l.add(URIUtil.rootScheme("testdata"));
		l.add(URIUtil.rootScheme("benchmarks"));
		
		String property = java.lang.System.getProperty("rascal.path");

		if (property != null) {
			for (String path : property.split(":")) {
				try {
					l.add(URIUtil.fixUnicode(new File(path).toURI()));
				} catch (URISyntaxException e) {
				}
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