package org.rascalmpl.value.impl.primitive;

import java.net.URI;

public interface IURI {
	String getScheme();
	String getAuthority();
	String getPath();
	String getFragment();
	String getQuery();
	Boolean hasAuthority();
	Boolean hasPath();
	Boolean hasFragment();
	Boolean hasQuery();
	URI getURI();
}
