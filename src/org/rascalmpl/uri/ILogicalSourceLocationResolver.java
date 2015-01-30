package org.rascalmpl.uri;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface ILogicalSourceLocationResolver {
	ISourceLocation resolve(ISourceLocation input);
	String scheme();
	String authority();
}
