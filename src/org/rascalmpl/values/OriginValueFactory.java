package org.rascalmpl.values;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.impl.primitive.Chunk;
import org.eclipse.imp.pdb.facts.impl.primitive.NoOrg;

public class OriginValueFactory extends RascalValueFactory {

	private static class InstanceKeeper{
		public final static OriginValueFactory instance = new OriginValueFactory();
	}

	public static OriginValueFactory getInstance(){
		return InstanceKeeper.instance;
	}
	
	@Override
	public IString string(ISourceLocation origin, String s) {
		return new Chunk(origin, s);
	}

	@Override
	public IString string(ISourceLocation origin, int ch) throws IllegalArgumentException {
		return new Chunk(origin, string(ch).getValue());
	}
	
	@Override
	public IString string(ISourceLocation origin, int[] chars) throws IllegalArgumentException {
		return new Chunk(origin, string(chars).getValue());
	}
	
	@Override
	public IString string(String s) {
		return new NoOrg(s);
	}

	@Override
	public IString string(int ch) {
		return new NoOrg(super.string(ch).getValue());
	}

	@Override
	public IString string(int[] chars) {
		return new NoOrg(super.string(chars).getValue());
	}

}
