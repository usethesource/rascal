/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Atze van der Ploeg - A.J.van.der.Ploeg@cwi.nl - CWI
*******************************************************************************/
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
	
	@Override
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