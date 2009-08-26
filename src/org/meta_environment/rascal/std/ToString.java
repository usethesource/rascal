package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.TreeAdapter;

public class ToString {
private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	
	public static IString toString(IValue value)
	{
		if (value.getType() == Factory.Tree) {
			TreeAdapter tree = new TreeAdapter((IConstructor) value);
			return values.string(tree.yield());
		}
		return values.string(value.toString());
	}
}
