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

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.utils.Timing;
import org.rascalmpl.values.IRascalValueFactory;

public class Benchmark {
	private final IRascalValueFactory values;
	
	public Benchmark(IRascalValueFactory values){
		super();
		
		this.values = values;
	}

	public IValue userTime()
	// @doc{userTime -- User time spend by this thread in nanoseconds.}
	{
		return values.integer(Timing.getUserTime());
	}
	
	public IValue systemTime()
	// @doc{systemTime -- System time spend by this thread in nanoseconds.}
	{
		return values.integer(Timing.getSystemTime());
	}
	
	public IValue cpuTime()
	// @doc{cpuTime -- Cpu time spend by this thread in nanoseconds.}
	{
		return values.integer(Timing.getCpuTime());
	}
	
	public IValue realTime()
	// @doc{realTime -- current time in milliseconds since January 1, 1970 GMT.}
	{
		return values.integer(System.currentTimeMillis());
	}
	
	public IValue getNanoTime(){
		return values.integer(System.nanoTime());
	}
	
	public IValue getMilliTime(){
		return values.integer(System.currentTimeMillis());
	}
}
