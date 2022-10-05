/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.util;

import java.io.IOException;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.util.HeapDumper;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Benchmark {
	private final IValueFactory values;
	
	public Benchmark(IValueFactory values){
		super();
		
		this.values = values;
	}

	public IInteger getFreeMemory() {
		return values.integer(Runtime.getRuntime().freeMemory());
	}

	public IInteger getTotalMemory() {
		return values.integer(Runtime.getRuntime().totalMemory());
	}

	public IInteger getMaxMemory() {
		return values.integer(Runtime.getRuntime().maxMemory());
	}
	
	public void heapDump(ISourceLocation loc, IBool live) {
	    try {
	        loc = URIResolverRegistry.getInstance().logicalToPhysical(loc);
	        
	        if (!"file".equals(loc.getScheme())) {
	            throw RuntimeExceptionFactory.illegalArgument(loc, null, null);
	        }
	        
	        HeapDumper.dumpHeap(loc.getPath(), live.getValue());
	    }
	    catch (IOException e) {
	        throw RuntimeExceptionFactory.io(values.string(e.getMessage()), null, null);
	    }
	}
	
	public IValue userTimeNow()
	// @doc{userTime -- User time spent by this thread in nanoseconds.}
	{
		return values.integer(Timing.getUserTime());
	}
	
	public IValue systemTimeNow()
	// @doc{systemTime -- System time spent by this thread in nanoseconds.}
	{
		return values.integer(Timing.getSystemTime());
	}
	
	public IValue cpuTimeNow()
	// @doc{cpuTime -- Cpu time (= system time + user time) spent by this thread in nanoseconds.}
	{
		return values.integer(Timing.getCpuTime());
	}
	
	public IValue realTimeNow()
	// @doc{realTime -- current time in milliseconds since January 1, 1970 GMT.}
	{
		return values.integer(System.currentTimeMillis());
	}
	
	public IValue getNanoTimeNow(){
		return values.integer(System.nanoTime());
	}
	
	public IValue getMilliTimeNow(){
		return values.integer(System.currentTimeMillis());
	}
	
	public void gc() {
		System.gc();
	}
}
