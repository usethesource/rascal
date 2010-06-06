package org.rascalmpl.interpreter;

import java.io.PrintWriter;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class DefaultTestResultListener implements ITestResultListener{
	private PrintWriter err;
	private int successes;
	private int failures;
	private int errors;
	private int count;
	
	public DefaultTestResultListener(PrintWriter errorStream){
		super();

		this.err = errorStream;
		this.successes = 0;
		this.failures = 0;
		this.errors = 0;
	}
	
	public void setErrorStream(PrintWriter errorStream) {
		this.err = errorStream;
	}
	
	public void start(int count) {
		this.count = count;
	}
	
	public void done() {
		err.print(successes + " of " + count + " tests succeeded");
		err.print(failures + " of " + count + " tests failed");
	}
	
	public void report(boolean successful, String test, ISourceLocation loc){
		synchronized(err){
			err.print(loc.getURI());
			err.print(":");
			err.print(loc.getBeginLine());
			err.print(",");
			err.print(loc.getBeginColumn());
			err.print(":");
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
			
			err.flush();
		}
	}
	
	public void report(boolean successful, String test, ISourceLocation loc, Throwable t){
		synchronized(err){
			err.print(loc.getURI());
			err.print(":");
			err.print(loc.getBeginLine());
			err.print(",");
			err.print(loc.getBeginColumn());
			err.print(":");
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
			err.print("\t" + t.getMessage() + "\n");
//			t.printStackTrace(err);
			err.flush();
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