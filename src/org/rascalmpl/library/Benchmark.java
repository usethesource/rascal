package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Benchmark {
	private final IValueFactory values;
	
	public Benchmark(IValueFactory values){
		super();
		
		this.values = values;
	}

	public IValue currentTimeMillis()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		double ctm = System.currentTimeMillis();
		return values.real(ctm);
	}
}
