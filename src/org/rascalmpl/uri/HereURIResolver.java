package org.rascalmpl.uri;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

public class HereURIResolver implements IURIInputStreamResolver {

	public static URI makeHere(String input) throws IOException {
		try {
			return URIUtil.create("here", "", input);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
	}

	@Override
	public InputStream getInputStream(URI uri) throws IOException {
		return new ByteArrayInputStream(uri.getPath().substring(1).getBytes(getCharset(uri)));
	}

	@Override
	public Charset getCharset(URI uri) throws IOException {
		String authority = uri.getAuthority();
		
		if (authority != null) {
			return Charset.forName(authority);
		}
		
		return Charset.forName("UTF-8");
	}

	@Override
	public boolean exists(URI uri) {
		return true;
	}

	@Override
	public long lastModified(URI uri) throws IOException {
		return 0L;
	}

	@Override
	public boolean isDirectory(URI uri) {
		return false;
	}

	@Override
	public boolean isFile(URI uri) {
		return true;
	}

	@Override
	public String[] listEntries(URI uri) throws IOException {
		return new String[0];
	}

	@Override
	public String scheme() {
		return "here";
	}

	@Override
	public boolean supportsHost() {
		return false;
	}

}
