package org.meta_environment.rascal.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.values.ValueFactoryFactory;

public class Boolean {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final Random random = new Random();
	
	public static IValue arbBool()
	//@doc{arbBool -- get an arbitrary boolean value.}
	{
	  return values.bool(random.nextInt(2) == 1);
	}
}
