package org.rascalmpl.uri;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class HttpURIResolver implements IURIInputStreamResolver {

	public InputStream getInputStream(URI uri) throws IOException {
		return new BufferedInputStream(uri.toURL().openStream());
	}

	public String scheme() {
		return "http";
	}

	public boolean exists(URI uri) {
		try {
			uri.toURL().openConnection();
			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	public boolean isDirectory(URI uri) {
		return false;
	}

	public boolean isFile(URI uri) {
		return exists(uri);
	}

	public long lastModified(URI uri) {
		try {
			return uri.toURL().openConnection().getLastModified();
		}
		catch (IOException e) {
			return 0L;
		}
	}

	public String[] listEntries(URI uri) {
		String [] ls = {};
		return ls;
	}

	public String absolutePath(URI uri) {
		return uri.getPath();
	}

}
