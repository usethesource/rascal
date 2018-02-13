package org.rascalmpl.core.uri;

import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;

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
