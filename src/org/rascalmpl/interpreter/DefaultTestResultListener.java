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
	
	@Override
	public void start(int count) {
		this.count = count;
	}
	
	@Override
	public void done() {
		err.print(successes + " of " + count + " tests succeeded\n");
		err.println(failures + " of " + count + " tests failed\n");
	}
	

	@Override
	public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
		err.print(loc.getURI());
		err.print(":");
		err.print(loc.getBeginLine());
		err.print(",");
		err.print(loc.getBeginColumn());
		err.print(":");
		err.print(successful ? "success : " : "failed  : ");
		if (successful) {
			successes++;
		}
		else {
			failures++;
		}
		
		if (test.length() <= 50) {
			err.println(test);
		} else {
			err.print(test.substring(0, 47));
			err.println("...");
		}
		err.print("\t" + message + "\n");
		if (t != null) {
		  t.printStackTrace(err);
		}
		err.flush();
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
