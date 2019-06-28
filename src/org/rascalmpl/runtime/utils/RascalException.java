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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.io.IOException;

import org.rascalmpl.interpreter.StackTrace;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.io.StandardTextWriter;

/**
 * This class is for representing all run-time exceptions in Rascal.
 * These exceptions can be caught by Rascal code. 
 * These exceptions can be thrown by Rascal code - 
 * in case of a run-time error, or by Rascal code - using the throw statement,
 * or by Java "built-in" methods, or by Java "embedded" methods.
 * <br>
 * Note that this class is <code>final</code> to emphasize the fact that
 * different kinds of Rascal exceptions need to be modeled by different kind
 * of exception values, not different kind of Java classes.
 */
public final class RascalException extends RuntimeException {
	private static final long serialVersionUID = -7290501865940548332L;
	private final IValue exception_value;
	private volatile ISourceLocation loc;
	private volatile StackTrace trace;
	
	
	private static String toString(IValue value, int length){
		StandardTextWriter stw = new StandardTextWriter(true);
		LimitedResultWriter lros = new LimitedResultWriter(length);
		
		try {
			stw.write(value, lros);
		}
		catch (IOLimitReachedException iolrex){
			// This is fine, ignore.
		}
		catch (IOException ioex) {
			// This can never happen.
		}
		
		return lros.toString();
	}
	
	/**
	 * Make a new Rascal exception.
	 * 
	 * @param value The Rascal exception value
	 * @param loc A source location, or null if unavailable
	 * @param trace A stack trace, or null
	 */
	public RascalException(IValue value, ISourceLocation loc, StackTrace trace) {
		super(toString(value, 4096));
		this.exception_value = value;
		this.loc = loc;
		if(trace == null) {
			trace = StackTrace.EMPTY_STACK_TRACE;
		}
		this.trace = trace;
	}
	
	/**
	 * Make a new Rascal exception.
	 * 
	 * @param value The Rascal exception value
	 */
	public RascalException(IValue value) {
		super(toString(value, 4096));
		this.exception_value = value;
		this.loc = null;
		if(trace == null) {
			trace = StackTrace.EMPTY_STACK_TRACE;
		}
		this.trace = trace;
	}
	
	/**
	 * @return The value provided by the exception
	 */
	
	public IValue getValue() {
		return exception_value;
	}
	
	/**
	 * @return The Rascal stack trace, guaranteed to never be null
	 */
	public StackTrace getTrace() {
		return trace;
	}
	
	/**
	 * Update the Rascal stack trace.
	 * 
	 * @param trace The new trace, or null for an empty trace
	 */
	public void setTrace(StackTrace trace) {
		if(trace == null) {
			trace = StackTrace.EMPTY_STACK_TRACE;
		}
		this.trace = trace;
	}
	
	/**
	 * @return The Rascal exception value
	 */
	public IValue getException() {
		return exception_value;
	}
	
	/**
	 * @return The source location where this exception occurred, or null
	 */
	public ISourceLocation getLocation() {
		return loc;
	}
	
	/**
	 * @param loc The source location, or null
	 */
	public void setLocation(ISourceLocation loc) {
		this.loc = loc;
	}

	@Override
	public Throwable fillInStackTrace() {
		return trace == StackTrace.EMPTY_STACK_TRACE ? fillInStackTrace() // ensure that we have proper Java traces as well
												     : null;
	}
	
	@Override
	public String getMessage() {
		if (loc != null) {
			return (loc.getScheme().equals("file") ? (loc.getAuthority() + loc.getPath()) : loc.top()) 
					+ ":" + loc.getBeginLine() 
					+ "," + loc.getBeginColumn() 
					+ ": " + super.getMessage();
		}
		
		// TODO remove once all errors have locations
		return super.getMessage();
	}
	
}
