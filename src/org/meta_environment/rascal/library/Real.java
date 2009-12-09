package org.meta_environment.rascal.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Real {
	private final IValueFactory values;
	private final Random random;
	
	public Real(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}

	public IValue arbReal()
	//@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
	{
	  return values.real(random.nextDouble());
	}

	public IValue toInt(IReal d)
	//@doc{toInteger -- convert a real to integer.}
	{
	  return d.toInteger();
	}

	public IValue toString(IReal d)
	//@doc{toString -- convert a real to a string.}
	{
	  return values.string(d.toString());
	}
}
