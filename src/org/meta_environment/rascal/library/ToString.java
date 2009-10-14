package org.meta_environment.rascal.library;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;
import org.meta_environment.values.ValueFactoryFactory;

public class ToString {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IString toString(IValue value)
	{
		if (value.getType() == Factory.Tree) {
			return values.string(TreeAdapter.yield((IConstructor) value));
		}
		if (value.getType().isStringType()) {
			return (IString) value;
		}
		return values.string(value.toString());
	}
}
