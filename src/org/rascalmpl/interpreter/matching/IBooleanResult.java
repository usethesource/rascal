package org.rascalmpl.interpreter.matching;


public interface IBooleanResult {
	public void init();
	
	/**
	 * @returns true iff next will return true, but will not actually advance the iterator or have any side-effects
	 */
	public boolean hasNext();
	
	/**
	 * @return true iff the current boolean value returns true, and advances the iterator to a next assignment
	 */
	public boolean next();
}