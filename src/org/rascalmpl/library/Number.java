package org.rascalmpl.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Number {
	private final IValueFactory values;
	private final Random random;
	
	public Number(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}

	public IValue arbInt()
	//@doc{arbInt -- return an arbitrary integer value}
	{
	   return values.integer(random.nextInt());
	}

	public IValue arbInt(IInteger limit)
	//@doc{arbInt -- return an arbitrary integer value in the interval [0, limit).}
	{
		// TODO allow big ints
	   return values.integer(random.nextInt(limit.intValue()));
	}
	
	public IValue arbReal()
	//@doc{arbReal -- returns an arbitrary real value in the interval [0.0,1.0).}
	{
	  return values.real(random.nextDouble());
	}
	
	public IValue toInt(INumber d)
	//@doc{toInteger -- convert a number to integer.}
	{
	  return d.toInteger();
	}
	

	public IValue toReal(INumber n)
	//@doc{toReal -- convert a number value to a real value.}
	{
	  return n.toReal();
	}

	public IValue toString(INumber n)
	//@doc{toString -- convert a number value to a string.}
	{
	  return values.string(n.toString());
	}
}
