package org.rascalmpl.interpreter.load;

import java.net.URI;
import java.util.List;


public class URIContributor implements IRascalSearchPathContributor {
	private final URI uri;

	public URIContributor(URI uri) {
		this.uri = uri;
	}

	@Override
	public String getName() {
	  return uri.toString();
	}
	
	public void contributePaths(List<URI> path) {
		path.add(0, uri);
	}

	@Override
	public String toString() {
		return uri.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof URIContributor)) {
			return false;
		}
		URIContributor other = (URIContributor) obj;
		return uri.equals(other.uri);
	}
}