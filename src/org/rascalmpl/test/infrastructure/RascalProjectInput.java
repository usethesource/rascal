package org.rascalmpl.test.infrastructure;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.uri.libraries.ClassResourceInput;

public class RascalProjectInput extends ClassResourceInput {
	private static String projectPrefix = "/src/org/rascalmpl/library";

	public RascalProjectInput(Class<?> clazz) {
		super("project", clazz, "/org/rascalmpl/library");
	}
	
	@Override
	protected String getPath(ISourceLocation uri) {
		if (uri.getAuthority() != null && uri.getAuthority().equals("rascal")) {
			String path = uri.getPath();
			if (path.startsWith(projectPrefix)) {
				// we have a special kind or location for level 1 development
				// we just have to rewrite it.
				return super.getPath(URIUtil.getChildLocation(uri, path.substring(projectPrefix.length())));
			}
		}
		return super.getPath(uri);
	}
}
