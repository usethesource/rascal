package org.rascalmpl.uri;

import java.io.IOException;

public class UnsupportedSchemeException extends IOException {
	private static final long serialVersionUID = -6623574531009224681L;
	
	public UnsupportedSchemeException(String scheme) {
		super("Unsupported scheme " + scheme);
	}
}
