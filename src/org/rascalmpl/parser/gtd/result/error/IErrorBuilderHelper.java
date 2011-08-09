package org.rascalmpl.parser.gtd.result.error;

/**
 * A helper class for error parse result builders. It provides access to
 * specific information about productions.
 */
public interface IErrorBuilderHelper{
	/**
	 * Checks if the given production is a list production.
	 */
	boolean isListProduction(Object production);
	
	/**
	 * Returns the left-hand-side of the production.
	 */
	Object getLHS(Object production);
	
	/**
	 * Returns the symbol at the given location in the production.
	 */
	Object getSymbol(Object production, int dot);
}
