package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;

public class Benchmark {

	private static final ValueFactory values = ValueFactory.getInstance();

	public static IValue currentTimeMillis()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		double ctm = System.currentTimeMillis();
		return values.dubble(ctm);
	}
}
