/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd;

import java.net.URI;

import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.INodeConverter;

/**
 * Parser interface.
 */
public interface IGTD{
	/**
	 * Parse the input string, using the given non-terminal as start node. If
	 * the parse process succesfully completes a result will be constructed
	 * using the supplied node converter. During result construction the action
	 * executor will be used to execute semantic actions.
	 */
	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor, INodeConverter converter);

	/**
	 * Experimental!
	 * @param nonterminal
	 * @param inputURI
	 * @param input
	 * @param actionExecutor
	 * @param converter
	 * @param robustNodes
	 * @param continuationCharacters
	 * @return
	 */
	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor actionExecutor, INodeConverter converter, Object[] robustNodes, int[][] continuationCharacters);
	
	/**
	 * Parse the input string, using the given non-terminal as start node. If
	 * the parse process succesfully completes a result will be constructed
	 * using the supplied node converter. This parse method does not perform
	 * semantic actions during result construction.
	 */
	Object parse(String nonterminal, URI inputURI, char[] input, INodeConverter converter);
	
	boolean parseErrorHasOccurred();
}
