package org.rascalmpl.test.infrastructure;

import java.net.URI;
import java.net.URISyntaxException;

import org.rascalmpl.uri.ClassResourceInput;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;

public class RascalProjectInput extends ClassResourceInput {
	private static String projectPrefix = "/src/org/rascalmpl/library";

	public RascalProjectInput(Class<?> clazz) {
		super("project", clazz, "/org/rascalmpl/library");
	}
	
	@Override
	protected String getPath(URI uri) {
		if (uri.getAuthority() != null && uri.getAuthority().equals("rascal")) {
			String path = uri.getPath();
			if (path.startsWith(projectPrefix)) {
				// we have a special kind or location for level 1 development
				// we just have to rewrite it.
				try {
					return super.getPath(URIUtil.changePath(uri, path.substring(projectPrefix.length())));
				} catch (URISyntaxException e) {
					throw new RuntimeException("Somethin went wrong with the path (" + path + ")", e);
				}
			}
		}
		return super.getPath(uri);
	}
}
