package org.rascalmpl.parser.gtd.result.error;

/**
 * A helper class for error parse result builders. It provides access to
 * specific information about productions.
 */
public interface IErrorBuilderHelper<P, N>{
	/**
	 * Checks if the given production is a list production.
	 */
	boolean isListProduction(P production);
	
	/**
	 * Returns the left-hand-side of the production.
	 */
	N getLHS(P production);
	
	/**
	 * Returns the symbol at the given location in the production.
	 */
	N getSymbol(P production, int dot);
}
