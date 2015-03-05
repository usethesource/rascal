package org.rascalmpl.uri;

import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;

public class LogicalMapResolver implements ILogicalSourceLocationResolver {
	private final IMap map;
	private final String scheme;
	private String authority;
	
	public LogicalMapResolver(String scheme, String authority, IMap map) {
		this.scheme = scheme;
		this.authority = authority;
		this.map = map;
	}
	
	@Override
	public String scheme() {
		return scheme;
	}
	
	@Override
	public String authority() {
		return authority;
	}
	
	public ISourceLocation resolve(ISourceLocation uri) {
		return (ISourceLocation) map.get(uri);
	}
}
