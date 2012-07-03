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
package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * Converter for parse trees that produces trees in UPTR format.
 */
public class DefaultNodeFlattener<T, P> implements INodeFlattener<T, P>{
	private final CharNodeFlattener<T, P> charNodeConverter;
	private final LiteralNodeFlattener<T, P> literalNodeConverter;
	private final SortContainerNodeFlattener<T, P> sortContainerNodeConverter;
	private final ListContainerNodeFlattener<T, P> listContainerNodeConverter;
	private final RecoveryNodeFlattener<T, P> recoveryNodeConverter;
	
	public DefaultNodeFlattener(){
		super();
		
		charNodeConverter = new CharNodeFlattener<T, P>();
		literalNodeConverter = new LiteralNodeFlattener<T, P>();
		sortContainerNodeConverter = new SortContainerNodeFlattener<T, P>();
		listContainerNodeConverter = new ListContainerNodeFlattener<T, P>();
		recoveryNodeConverter = new RecoveryNodeFlattener<T, P>();
	}
	
	/**
	 * Internal helper structure for error tracking.
	 */
	protected static class IsInError{
		public boolean inError;
	}
	
	/**
	 * Convert the given node.
	 */
	public T convert(INodeConstructorFactory<T, P> nodeConstructorFactory, AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		switch(node.getTypeIdentifier()){
			case CharNode.ID:
				return charNodeConverter.convertToUPTR(nodeConstructorFactory, (CharNode) node);
			case LiteralNode.ID:
				return literalNodeConverter.convertToUPTR(nodeConstructorFactory, (LiteralNode) node);
			case SortContainerNode.ID:
				return sortContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (SortContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case ExpandableContainerNode.ID:
				return listContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (ExpandableContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case SkippedNode.ID:
				return recoveryNodeConverter.convertToUPTR(nodeConstructorFactory, (SkippedNode) node);
			default:
				throw new RuntimeException("Incorrect result node id: "+node.getTypeIdentifier());
		}
	}
	
	/**
	 * Converts the given parse tree to a tree in UPTR format.
	 */
	public T convert(INodeConstructorFactory<T, P> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object rootEnvironment){
		return convert(nodeConstructorFactory, parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, filteringTracker, actionExecutor, rootEnvironment);
	}
}
