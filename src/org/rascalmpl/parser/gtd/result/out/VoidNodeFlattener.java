/*******************************************************************************
 * Copyright (c) 2012-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * A node converter that doesn't do anything.
 * This may be useful for benchmarking and debugging and such.
 */
public class VoidNodeFlattener implements INodeFlattener<AbstractNode, Object>{
	
	public VoidNodeFlattener(){
		super();
	}
	
	/**
	 * Returns the given tree.
	 */
	@Override
	public AbstractNode convert(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment){
		return parseTree;
	}
	
	@Override
	public AbstractNode convert(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment, INodeFlattener.CacheMode cacheMode){
		return parseTree;
	}
	
	/**
	 * Returns the given tree.
	 */
	public AbstractNode convertWithErrors(INodeConstructorFactory<AbstractNode, Object> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, IActionExecutor<AbstractNode> actionExecutor, Object rootEnvironment){
		return parseTree;
	}
}
