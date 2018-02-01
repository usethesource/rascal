/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
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
