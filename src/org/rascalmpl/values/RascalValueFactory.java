package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.impl.AbstractValueFactoryAdapter;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class RascalValueFactory extends AbstractValueFactoryAdapter implements IRascalValueFactory {

	private static class InstanceKeeper{
		public final static RascalValueFactory instance = new RascalValueFactory();
	}

	public static RascalValueFactory getInstance(){
		return InstanceKeeper.instance;
	}
	
	protected RascalValueFactory() {
		super(ValueFactory.getInstance());
	}

	@Override
	public IString string(ISourceLocation loc, String str) {
		return string(str);
	}
	
	@Override
	public IString string(ISourceLocation loc, int c) {
		return string(c);
	}

	@Override
	public IString string(ISourceLocation loc, int[] chars) {
		return string(chars);
	}

}
