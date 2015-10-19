package org.rascalmpl.uri;

import org.rascalmpl.value.ISourceLocation;

public interface ILogicalSourceLocationResolver {
	ISourceLocation resolve(ISourceLocation input);
	String scheme();
	String authority();
}
