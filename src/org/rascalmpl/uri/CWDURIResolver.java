package org.rascalmpl.uri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * For reading and writing files relative to the current working directory.
 */
public class CWDURIResolver implements IURIInputStreamResolver,
		IURIOutputStreamResolver {

	public InputStream getInputStream(URI uri) throws IOException {
		return new FileInputStream(getAbsolutePath(uri));
	}

	private File getAbsolutePath(URI uri) {
		return new File(System.getProperty("user.dir"), uri.getPath());
	}

	public String scheme() {
		return "cwd";
	}

	public OutputStream getOutputStream(URI uri, boolean append) throws IOException {
		return new FileOutputStream(getAbsolutePath(uri), append);
	}

}
