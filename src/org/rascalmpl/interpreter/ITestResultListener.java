package org.rascalmpl.interpreter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public interface ITestResultListener{
	void start(int count);
	void report(boolean successful, String test, ISourceLocation loc);
	void report(boolean successful, String test, ISourceLocation loc, Throwable t);
	void done();
}
