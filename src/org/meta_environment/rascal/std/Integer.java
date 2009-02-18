package org.meta_environment.rascal.std;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;

public class Integer {

	private static final ValueFactory values = ValueFactory.getInstance();
	private static final Random random = new Random();

	public static IValue arbInt()
	//@doc{arbInt -- return an arbitrary integer value}
	{
	   return values.integer(random.nextInt());
	}

	public static IValue arbInt(IInteger limit)
	//@doc{arbInt -- return an arbitrary integer value in the interval [0, limit).}
	{
	   return values.integer(random.nextInt(limit.getValue()));
	}

	public static IValue toReal(IInteger n)
	//@doc{toReal -- convert an integer value to a real value.}
	{
	  return n.toDouble();
	}

	public static IValue toString(IInteger n)
	//@doc{toString -- convert an integer value to a string.}
	{
	  return values.string(n.toString());
	}
}
