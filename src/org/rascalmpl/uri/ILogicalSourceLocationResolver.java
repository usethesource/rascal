package org.rascalmpl.uri;

import io.usethesource.vallang.ISourceLocation;

public interface ILogicalSourceLocationResolver {
	ISourceLocation resolve(ISourceLocation input);
	String scheme();
	String authority();
}
