/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
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
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.rascalmpl.interpreter.utils.Timing;

public class Benchmark {
	private final IValueFactory values;
	
	public Benchmark(IValueFactory values){
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
		return ValueFactory.getInstance().integer(System.nanoTime());
	}
	
	public IValue getMilliTime(){
		return ValueFactory.getInstance().integer(System.currentTimeMillis());
	}
}
