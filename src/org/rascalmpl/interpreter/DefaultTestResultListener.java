/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintWriter;

import org.rascalmpl.repl.ReplTextWriter;

import io.usethesource.vallang.ISourceLocation;

public class DefaultTestResultListener implements ITestResultListener{
	private PrintWriter err;
	private int successes;
	private int failures;
	private int errors;
	private int count;
	private int ignored;
    private String context;
    private final boolean verbose;
	private static char[] roller = new char[] {'|', '/', '-', '\\', '|', '/', '-', '\\', '|'};
	
	public DefaultTestResultListener(PrintWriter errorStream){
	    this(errorStream, true);
	}
	public DefaultTestResultListener(PrintWriter errorStream, boolean verbose){
		super();

		this.err = errorStream;
		this.verbose = verbose;
		reset();
	}

    private void reset() {
        this.successes = 0;
		this.failures = 0;
		this.errors = 0;
		this.ignored = 0;
    }
	
	public void setErrorStream(PrintWriter errorStream) {
		this.err = errorStream;
	}
	
	@Override
	public void ignored(String test, ISourceLocation loc) {
	    this.ignored++;
	}
	
	@Override
	public void start(String context, int count) {
	    this.context = context;
	    reset();
	    if (count != 0 && verbose) {
	        err.println("Running tests for " + context);
	    }
		this.count = count;
		progress();
	}

    private void progress() {
        if (count > 0 && verbose) {
            err.print(String.format("%s testing %d/%d ", 
                    roller[getNumberOfTests() % roller.length], getNumberOfTests(), count));
        }
    }
	
	@Override
	public void done() {
	    progress();
	    if (count > 0) {
	        if (!verbose) {
	            // make sure results are reported on a newline
	            err.println();
	        }
	        err.println("\rTest report for " + context);
	        if (errors + failures == 0) {
	            err.println("\tall " + (count - ignored) + "/" + count + " tests succeeded");
	        }
	        else {
	            err.println("\t" + successes + "/" + count + " tests succeeded");
	            err.println("\t" + failures + "/" + count + " tests failed");
	            err.println("\t" + errors + "/" + count + " tests threw exceptions");
	        }

	        if (ignored != 0) {
	            err.println("\t" + ignored + "/" + count + " tests ignored");
	        }
	    }
	}
	

	@Override
	public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
		progress();
		
		if (successful) {
		    successes++;
		    if (verbose) {
		        err.print("success                                                       \r");
		    }
		}
		else if (t != null) {
		    errors++;
		    if (!verbose) {
		        err.println();
		    }
		    err.println("error: " + test + " @ " + ReplTextWriter.valueToString(loc));
		    err.println(message);
		}
		else {
		    failures++;
		    if (!verbose) {
		        err.println();
		    }
		    err.println("failure: " + test + " @ " + ReplTextWriter.valueToString(loc));
		    err.println(message);
		}
		
	
		
		err.flush();
	}


	public int getNumberOfTests(){
		return successes + failures + errors + ignored;
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
