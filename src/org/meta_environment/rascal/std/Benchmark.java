package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class Benchmark {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();

	public static IValue currentTimeMillis()
	// @doc{currentTimeMillis -- current time in milliseconds since January 1, 1970 GMT.}
	{
		double ctm = System.currentTimeMillis();
		return values.real(ctm);
	}
}
