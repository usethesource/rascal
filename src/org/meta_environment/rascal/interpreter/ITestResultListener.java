package org.meta_environment.rascal.interpreter;

public interface ITestResultListener{
	void report(boolean successful, String test);
	void report(boolean successful, String test, Throwable t);
}
