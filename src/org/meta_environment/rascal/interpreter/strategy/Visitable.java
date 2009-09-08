package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IValue;

public interface Visitable {

	/**
	 * Get a child
	 * 
	 * @param i
	 *            the zero based index of the child
	 * @return a value
	 */
	public Visitable get(int i) throws IndexOutOfBoundsException;

	/**
	 * Set a different child at a certain position.
	 * 
	 * @param i
	 *            the zero based index of the child to be replaced
	 * @param newChild
	 *            the new value for the child
	 * @return a new visitable with the new child at position i
	 */
	public Visitable set(int i, Visitable newChild)
			throws IndexOutOfBoundsException;

	/**
	 * @return the (fixed) number of children
	 */
	public int arity();

	/**
	 * @return the concrete value
	 */
	public IValue getValue();

}
