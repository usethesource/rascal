package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.utils.Timing;

public class Benchmark {
	private final IValueFactory values;
	
	public Benchmark(IValueFactory values){
		super();
		
		this.values = values;
	}

	public IValue userTime()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		return values.integer(Timing.getUserTime());
	}
	
	public IValue systemTime()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		return values.integer(Timing.getSystemTime());
	}
	
	public IValue cpuTime()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		return values.integer(Timing.getCpuTime());
	}
}
