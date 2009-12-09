package org.meta_environment.rascal.library;

import java.util.Random;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class Boolean {
	private final IValueFactory values;
	private final Random random;
	
	public Boolean(IValueFactory values){
		super();
		
		this.values = values;
		random = new Random();
	}
	
	public IValue arbBool()
	//@doc{arbBool -- get an arbitrary boolean value.}
	{
	  return values.bool(random.nextInt(2) == 1);
	}
}
