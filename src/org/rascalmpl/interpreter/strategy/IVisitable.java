package org.rascalmpl.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;


public interface IVisitable {
	
	public boolean init(IValue v);
	
	public void mark(IValue v);

	public IValue getChildAt(IValue v, int i) throws IndexOutOfBoundsException;

	public int getChildrenNumber(IValue v);

	public <T extends IValue> T setChildren(T v, List<IValue> newchildren)
		throws IndexOutOfBoundsException;

	public <T extends IValue> T setChildAt(T v, int i, IValue newchild)
		throws IndexOutOfBoundsException;
}
