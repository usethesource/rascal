package org.meta_environment.rascal.interpreter;

import java.io.PrintWriter;

public class DefaultTestResultListener implements ITestResultListener{
	private final PrintWriter err;
	private int successes;
	private int failures;
	private int errors;
	
	public DefaultTestResultListener(PrintWriter errorStream){
		super();

		this.err = errorStream;
		this.successes = 0;
		this.failures = 0;
		this.errors = 0;
	}
	
	public void report(boolean successful, String test){
		synchronized(err){
			
			err.print(successful ? "success : " : "failed  : ");
			if(successful)
				successes++;
			else
				failures++;
		    
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
			if(successful)
				successes++;
			else
				errors++;
			if(test.length() <= 50){
				err.println(test);
			}else{
				err.print(test.substring(0, 47));
				err.println("...");
			}
			t.printStackTrace(err);
		}
	}
	
	public int getNumberOfTests(){
		return successes + failures + errors;
	}
	
	public int getSuccesses(){
		return successes;
	}
	
	public int getFailures(){
		return failures;
	}
	
	public int getErrors(){
		return errors;
	}
	
	public boolean allOk(){
		return failures == 0 && errors == 0;
	}
}