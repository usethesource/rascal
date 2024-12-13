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
package org.rascalmpl.parser.gtd.result.out;

import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.RecoveredNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * Converter for parse trees that produces trees in UPTR format.
 */
public class DefaultNodeFlattener<P, T, S> implements INodeFlattener<T, S>{
	public static boolean nodeMemoization = true;
	public static boolean linkMemoization = false;
	
	private final CharNodeFlattener<T, S> charNodeConverter;
	private final LiteralNodeFlattener<T, S> literalNodeConverter;
	private final SortContainerNodeFlattener<P, T, S> sortContainerNodeConverter;
	private final ListContainerNodeFlattener<P, T, S> listContainerNodeConverter;
	private final SkippedNodeFlattener<T, S> skippedNodeConverter;
	
	public DefaultNodeFlattener(){
		super();
		
		charNodeConverter = new CharNodeFlattener<T, S>();
		literalNodeConverter = new LiteralNodeFlattener<T, S>();
		sortContainerNodeConverter = new SortContainerNodeFlattener<P, T, S>();
		listContainerNodeConverter = new ListContainerNodeFlattener<P, T, S>();
		skippedNodeConverter = new SkippedNodeFlattener<T, S>();
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
	@SuppressWarnings("unchecked")
	public T convert(INodeConstructorFactory<T, S> nodeConstructorFactory, AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment){
		switch(node.getTypeIdentifier()){
			case CharNode.ID:
				return charNodeConverter.convertToUPTR(nodeConstructorFactory, (CharNode) node);
			case LiteralNode.ID:
				return literalNodeConverter.convertToUPTR(nodeConstructorFactory, (LiteralNode) node);
			case SortContainerNode.ID:
				return sortContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (SortContainerNode<P>) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case ExpandableContainerNode.ID:
				return listContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (ExpandableContainerNode<P>) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case RecoveredNode.ID:
				return convert(nodeConstructorFactory, ((SortContainerNode<S>) node).getFirstAlternative().getNode(), stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case SkippedNode.ID:
				return skippedNodeConverter.convertToUPTR(nodeConstructorFactory, (SkippedNode) node, positionStore); 
			default:
				throw new RuntimeException("Incorrect result node id: "+node.getTypeIdentifier());
		}
	}
	
	/**
	 * Converts the given parse tree to a tree in UPTR format.
	 */
	public T convert(INodeConstructorFactory<T, S> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object rootEnvironment){
		return convert(nodeConstructorFactory, parseTree, new IndexedStack<>(), 0, new CycleMark(), positionStore, filteringTracker, actionExecutor, rootEnvironment);
	}
}
