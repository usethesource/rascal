/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Anya Helene Bagge - UiB
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.rascalmpl.value.ISourceLocation;


public class StackTrace implements Iterable<StackTraceEntry> {
	public static final StackTrace EMPTY_STACK_TRACE = new StackTrace().freeze();
	protected List<StackTraceEntry> trace = new ArrayList<StackTraceEntry>();

	/**
	 * Add a new entry to the stack trace.
	 * 
	 * Later added entries are shown below earlier entries, with the
	 * first/top entry being the location where the exception was
	 * thrown.
	 * 
	 * @param loc A source code location, or null
	 * @param funName The name of the containing function, or null
	 * @return this
	 */
	public StackTrace add(ISourceLocation loc, String funName) {
		trace.add(new StackTraceEntry(loc, funName));
		return this;
	}
	
	/**
	 * Add all entries of another stack trace to this stack trace.
	 * 
	 * The entries are added at the end of this trace.
	 * 
	 * @param stackTrace Another stack trace
	 * @return this
	 */
	public StackTrace addAll(Iterable<StackTraceEntry> stackTrace) {
		if(stackTrace != null) {
			for(StackTraceEntry e : stackTrace) {
				trace.add(e);
			}
		}
		return this;
	}

	/**
	 * Format this stack trace using embedded links, suitable
	 * for printing on a Rascal console.
	 * 
	 * @return The stack trace as a string
	 */
	public String toLinkedString() {
		StringBuilder b = new StringBuilder(4096);
		for(StackTraceEntry e : trace) {
			e.format(b, true);
		}
		return b.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder(4096);
		for(StackTraceEntry e : trace) {
			e.format(b, false);
		}
		return b.toString();
	}
	
	/**
	 * Make the stack trace unmodifiable.
	 * 
	 * The current stack trace is destroyed in the process, avoid using
	 * it after this method returns. Use the return stack trace instead.
	 * 
	 * @return an unmodifiable stack trace
	 */
	public StackTrace freeze() {
		UnmodifiableStackTrace result = new UnmodifiableStackTrace(this);
		trace = null;
		return result;
	}
	
	@Override
	public Iterator<StackTraceEntry> iterator() {
		return trace.iterator();
	}

	/**
	 * @return Current entries in the stack trace
	 */
	public int size() {
		return trace.size();
	}
	
	static class UnmodifiableStackTrace extends StackTrace {
		private UnmodifiableStackTrace(StackTrace original) {
			super.trace = Collections.unmodifiableList(original.trace);
		}
		
		@Override
		public StackTrace add(ISourceLocation loc, String funName) {
			throw new UnsupportedOperationException("This stack trace is unmodifiable");
		}
		
		@Override
		public StackTrace addAll(Iterable<StackTraceEntry> stackTrace) {
			throw new UnsupportedOperationException("This stack trace is unmodifiable");
		}
		
		@Override
		public StackTrace freeze() {
			return this;
		}
		
	}
}

