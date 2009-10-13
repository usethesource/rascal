package org.meta_environment.rascal.interpreter;

import java.io.PrintWriter;

public class DefaultTestResultListener implements ITestResultListener{
	private final PrintWriter err;
	
	public DefaultTestResultListener(PrintWriter errorStream){
		super();

		this.err = errorStream;
	}
	
	public void report(boolean successful, String test){
		synchronized(err){
			err.print(successful ? "success : " : "failed  : ");
			if(test.length() <= 50){
				err.println(test);
			}else{
				err.print(test.substring(0, 47));
				err.println("...");
			}
		}
	}
	
	public void report(boolean successful, String test, Throwable t){
		synchronized(err){
			err.print(successful ? "success : " : "failed  : ");
			if(test.length() <= 50){
				err.println(test);
			}else{
				err.print(test.substring(0, 47));
				err.println("...");
			}
			t.printStackTrace(err);
		}
	}
}