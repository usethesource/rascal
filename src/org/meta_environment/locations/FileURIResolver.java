package org.meta_environment.locations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileURIResolver implements IURIResolver {
	public InputStream resolve(URI uri) throws IOException {
		return new FileInputStream(uri.getPath());
	}

	public String scheme() {
		return "file";
	}
}
