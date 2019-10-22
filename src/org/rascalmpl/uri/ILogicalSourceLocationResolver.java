package org.rascalmpl.uri;

import java.io.IOException;

import io.usethesource.vallang.ISourceLocation;

public interface ILogicalSourceLocationResolver {
	ISourceLocation resolve(ISourceLocation input) throws IOException;
	String scheme();
	String authority();
}
