package org.meta_environment;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class ValueFactoryFactory{
	private final static IValueFactory valueFactory = ValueFactory.getInstance();
	
	public static IValueFactory getValueFactory(){
		return valueFactory;
	}
}
