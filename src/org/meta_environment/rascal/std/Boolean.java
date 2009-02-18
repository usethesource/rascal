package org.meta_environment.rascal.std;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;

public class Boolean {

	private static final ValueFactory values = ValueFactory.getInstance();
	private static final Random random = new Random();
	
	public static IValue arbBool()
	//@doc{arbBool -- get an arbitrary boolean value.}
	{
	  return values.bool(random.nextInt(2) == 1);
	}
}
