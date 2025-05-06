/*******************************************************************************
 * Copyright (c) 2009-2025 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.result.out;

import java.util.HashMap;
import java.util.Map;

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
	private final CharNodeFlattener<T, S> charNodeConverter;
	private final LiteralNodeFlattener<T, S> literalNodeConverter;
	private final SortContainerNodeFlattener<P, T, S> sortContainerNodeConverter;
	private final ListContainerNodeFlattener<P, T, S> listContainerNodeConverter;
	private final SkippedNodeFlattener<T, S> skippedNodeConverter;

	private final Map<T, T> treeCache;
	
	public DefaultNodeFlattener(){
		super();
		
		charNodeConverter = new CharNodeFlattener<>();
		literalNodeConverter = new LiteralNodeFlattener<>();
		sortContainerNodeConverter = new SortContainerNodeFlattener<>();
		listContainerNodeConverter = new ListContainerNodeFlattener<>();
		skippedNodeConverter = new SkippedNodeFlattener<>();

		treeCache = new HashMap<>();
	}
	
	/**
	 * Convert the given node.
	 */
	@SuppressWarnings("unchecked")
	public T convert(INodeConstructorFactory<T, S> nodeConstructorFactory, AbstractNode node, IndexedStack<AbstractNode> stack, int depth, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object environment, CacheMode cacheMode, int maxAmbDepth){
		T result = node.getTree();

		if (result != null) {
			return result;
		}

		switch(node.getTypeIdentifier()){
			case CharNode.ID:
				result = charNodeConverter.convertToUPTR(nodeConstructorFactory, (CharNode) node);
				break;
			case LiteralNode.ID:
				result = literalNodeConverter.convertToUPTR(nodeConstructorFactory, (LiteralNode) node);
				break;
			case SortContainerNode.ID:
				result = sortContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (SortContainerNode<P>) node, stack, depth, positionStore, filteringTracker, actionExecutor, environment, maxAmbDepth);
				break;
			case ExpandableContainerNode.ID:
				result = listContainerNodeConverter.convertToUPTR(this, nodeConstructorFactory, (ExpandableContainerNode<P>) node, stack, depth, positionStore, filteringTracker, actionExecutor, environment, maxAmbDepth);
				break;
			case RecoveredNode.ID:
				result = convert(nodeConstructorFactory, ((SortContainerNode<S>) node).getFirstAlternative().getNode(), stack, depth, positionStore, filteringTracker, actionExecutor, environment, cacheMode, maxAmbDepth);
				break;
			case SkippedNode.ID:
				result = skippedNodeConverter.convertToUPTR(nodeConstructorFactory, (SkippedNode) node, positionStore); 
				break;
			default:
				throw new IllegalArgumentException("Incorrect result node id: "+node.getTypeIdentifier());
		}

		if (cacheMode == CacheMode.CACHE_MODE_FULL) {
			node.setTree(result);
		} else if (cacheMode == CacheMode.CACHE_MODE_SHARING_ONLY) {
			T existing = treeCache.get(result);
			if (existing == null) {
				treeCache.put(result, result);
			} else {
				result = existing;
			}
		}

		return result;
	}
	
	/**
	 * Converts the given parse tree to a tree in UPTR format.
	 */
	public T convert(INodeConstructorFactory<T, S> nodeConstructorFactory, AbstractNode parseTree, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor<T> actionExecutor, Object rootEnvironment, int maxAmbDepth){
		return convert(nodeConstructorFactory, parseTree, new IndexedStack<>(), 0, positionStore, filteringTracker, actionExecutor, rootEnvironment, CacheMode.CACHE_MODE_NONE,
			maxAmbDepth == -1 ? UNLIMITED_MAX_AMB_DEPTH : maxAmbDepth);
	}
}
