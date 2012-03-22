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
package org.rascalmpl.parser.uptr;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.CharNode;
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.LiteralNode;
import org.rascalmpl.parser.gtd.result.SkippedNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.out.INodeConverter;
import org.rascalmpl.parser.gtd.util.IndexedStack;

/**
 * Converter for parse trees that produces trees in UPTR format.
 */
public class NodeToUPTR implements INodeConverter{
	private final LiteralNodeConverter literalNodeConverter;
	private final SortContainerNodeConverter sortContainerNodeConverter;
	private final ListContainerNodeConverter listContainerNodeConverter;
	private final RecoveryNodeConverter recoveryNodeConverter;
	
	public NodeToUPTR(){
		super();
		
		literalNodeConverter = new LiteralNodeConverter();
		sortContainerNodeConverter = new SortContainerNodeConverter();
		listContainerNodeConverter = new ListContainerNodeConverter();
		recoveryNodeConverter = new RecoveryNodeConverter();
	}
	
	/**
	 * Internal helper structure for cycle detection and handling.
	 */
	protected static class CycleMark{
		public int depth = Integer.MAX_VALUE;
		
		public CycleMark(){
			super();
		}
		
		/**
		 * Marks the depth at which a cycle was detected.
		 */
		public void setMark(int depth){
			if(depth < this.depth){
				this.depth = depth;
			}
		}
		
		/**
		 * Resets the mark.
		 */
		public void reset(){
			depth = Integer.MAX_VALUE;
		}
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
	protected IConstructor convert(AbstractNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		switch(node.getTypeIdentifier()){
			case CharNode.ID:
				return CharNodeConverter.convertToUPTR((CharNode) node);
			case LiteralNode.ID:
				return literalNodeConverter.convertToUPTR((LiteralNode) node);
			case SortContainerNode.ID:
				return sortContainerNodeConverter.convertToUPTR(this, (SortContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case ExpandableContainerNode.ID:
				return listContainerNodeConverter.convertToUPTR(this, (ExpandableContainerNode) node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			case SkippedNode.ID:
				return recoveryNodeConverter.convertToUPTR((SkippedNode) node);
			default:
				throw new RuntimeException("Incorrect result node id: "+node.getTypeIdentifier());
		}
	}
	
	/**
	 * Converts the given parse tree to a tree in UPTR format.
	 */
	public IConstructor convert(AbstractNode parseTree, PositionStore positionStore, IActionExecutor actionExecutor, Object rootEnvironment, FilteringTracker filteringTracker){
		return convert(parseTree, new IndexedStack<AbstractNode>(), 0, new CycleMark(), positionStore, filteringTracker, actionExecutor, rootEnvironment);
	}
}
