package org.meta_environment.rascal.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;


public interface IVisitable extends IValue {

	/**
	 * Get a child
	 * 
	 * @param i
	 *            the zero based index of the child
	 * @return a value
	 */
	public IVisitable getChildAt(int i) throws IndexOutOfBoundsException;

	/**
	 * Set a different child at a certain position.
	 * 
	 * @param i
	 *            the zero based index of the child to be replaced
	 * @param newChild
	 *            the new value for the child
	 */
	public void setChildAt(int i, IVisitable newChild)
			throws IndexOutOfBoundsException;

	/**
	 * @return the (fixed) number of children
	 */
	public int getChildrenNumber();

	/**
	 * @return the concrete value
	 */
	public IValue getValue();
	
	public IVisitable setValue(IValue value);

	public void setChildren(List<IVisitable> newchildren)
			throws IndexOutOfBoundsException;

	public void update(IValue oldvalue, IValue newvalue);

}
