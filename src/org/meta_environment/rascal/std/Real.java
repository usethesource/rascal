package org.meta_environment.rascal.std;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.ValueFactoryFactory;

public class Real {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final Random random = new Random();
	

	public static IValue arbReal()
	//@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
	{
	  return values.real(random.nextDouble());
	}

	public static IValue toInteger(IReal d)
	//@doc{toInteger -- convert a real to integer.}
	{
	  return d.toInteger();
	}

	public static IValue toString(IReal d)
	//@doc{toString -- convert a real to a string.}
	{
	  return values.string(d.toString());
	}

}
