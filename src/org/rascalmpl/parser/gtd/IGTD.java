/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import org.rascalmpl.parser.gtd.debug.IDebugListener;
import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.INodeConstructorFactory;
import org.rascalmpl.parser.gtd.result.out.INodeFlattener;

/**
 * Parser interface.
 */
public interface IGTD<P, T, S> extends ExpectsProvider<P> {
	/**
	 * Parse the input string, using the given non-terminal as start node. If
	 * the parse process successfully completes a result will be constructed
	 * using the supplied node converter. This parse method does not perform
	 * semantic actions during result construction.
	 */
	Object parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory);
	
	Object parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IDebugListener<P> debugger);
	
	Object parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer);
	
	Object parse(String nonterminal, URI inputURI, char[] input, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugger);
	
	/**
	 * Parse the input string, using the given non-terminal as start node. If
	 * the parse process successfully completes a result will be constructed
	 * using the supplied node converter. During result construction the action
	 * executor will be used to execute semantic actions.
	 */
	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory);
	
	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IDebugListener<P> debugger);

	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer);

	Object parse(String nonterminal, URI inputURI, char[] input, IActionExecutor<T> actionExecutor, INodeFlattener<T, S> converter, INodeConstructorFactory<T, S> nodeConstructorFactory, IRecoverer<P> recoverer, IDebugListener<P> debugger);
}
