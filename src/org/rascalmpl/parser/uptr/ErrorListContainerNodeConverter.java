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

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.error.ErrorListContainerNode;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.uptr.ListContainerNodeConverter.CycleNode;
import org.rascalmpl.parser.uptr.NodeToUPTR.CycleMark;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ErrorListContainerNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	private final static IConstructor[] NO_CHILDREN = new IConstructor[]{};
	private final static IList EMPTY_LIST = VF.list();
	
	private ErrorListContainerNodeConverter(){
		super();
	}
	
	private static void buildAlternative(NodeToUPTR converter, IConstructor[] prefix, ForwardLink<AbstractNode> postFix, IConstructor production, ArrayList<IConstructor> gatheredAlternatives, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < prefix.length; ++i){
			childrenListWriter.append(prefix[i]);
		}
		
		int postFixLength = postFix.length;
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			if(!(node instanceof CycleNode)){
				IConstructor constructedNode = converter.convertWithErrors(node, stack, depth, cycleMark, positionStore, actionExecutor, environment);
				if(constructedNode == null) return;
				childrenListWriter.append(constructedNode);
			}else{
				CycleNode cycleNode = (CycleNode) node;
				IConstructor[] constructedCycle = constructCycle(converter, production, cycleNode, stack, depth, cycleMark, positionStore, actionExecutor, environment);
				if(constructedCycle == null) return;
				
				int constructedCycleLength = constructedCycle.length;
				if(constructedCycleLength == 1){
					childrenListWriter.append(constructedCycle[0]);
				}else{
					for(int j = 0; j < constructedCycle.length; ++j){
						childrenListWriter.append(constructedCycle[j]);
					}
				}
			}
		}
		
		IConstructor result = VF.constructor(Factory.Tree_Error, production, childrenListWriter.done(), EMPTY_LIST);
		gatheredAlternatives.add(result);
	}
	
	private static IConstructor[] constructCycle(NodeToUPTR converter, IConstructor production, CycleNode cycleNode, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		AbstractNode[] cycleElements = cycleNode.cycle;
		
		int nrOfCycleElements = cycleElements.length;
		IConstructor[] convertedCycle;
		if(nrOfCycleElements == 1){
			convertedCycle = new IConstructor[1];
			IConstructor element = converter.convertWithErrors(cycleElements[0], stack, depth, cycleMark, positionStore, actionExecutor, environment);
			if(element == null) return null;
			convertedCycle[0] = element;
		}else{
			convertedCycle = new IConstructor[nrOfCycleElements + 1];
			convertedCycle[0] = converter.convertWithErrors(cycleElements[nrOfCycleElements - 1], stack, depth, cycleMark, positionStore, actionExecutor, environment);
			for(int i = 0; i < nrOfCycleElements; ++i){
				IConstructor element = converter.convertWithErrors(cycleElements[i], stack, depth, cycleMark, positionStore, actionExecutor, environment);
				if(element == null) return null;
				convertedCycle[i + 1] = element;
			}
		}
		
		IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getType(production), VF.integer(1));
		cycle = actionExecutor.filterListCycle(cycle, environment);
		if(cycle == null){
			return convertedCycle;
		}
		
		IConstructor elements = VF.constructor(Factory.Tree_Appl, production, VF.list(convertedCycle));
		
		IConstructor constructedCycle = VF.constructor(Factory.Tree_Amb, VF.set(elements, cycle));
		
		return new IConstructor[]{constructedCycle};
	}
	
	protected static void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		AbstractNode childNode = child.getNode();
		
		if(!(childNode.isEpsilon() && child.getPrefixes() == null)){
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){
				CycleNode cycle = gatherCycle(child, new AbstractNode[]{childNode}, blackList);
				if(cycle != null){
					if(cycle.cycle.length == 1){
						gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, cycle), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
					}else{
						ForwardLink<AbstractNode> cycleLink = new ForwardLink<AbstractNode>(NO_NODES, cycle);
						gatherProduction(converter, child, new ForwardLink<AbstractNode>(cycleLink, childNode), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
					}
					return;
				}
			}
			gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, childNode), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
		}else{
			buildAlternative(converter, NO_CHILDREN, NO_NODES, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
		}
	}
	
	private static void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, IActionExecutor actionExecutor, Object environment){
		do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){
				buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
				return;
			}
			
			if(prefixes.size() == 1){
				Link prefix = prefixes.get(0);
				
				if(prefix == null){
					buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
					return;
				}
				
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)){
					return;
				}
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle.
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){
						prefixNode = cycle;
					}
				}
				
				child = prefix;
				postFix = new ForwardLink<AbstractNode>(postFix, prefixNode);
				continue;
			}
			
			gatherAmbiguousProduction(converter, prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
			
			break;
		}while(true);
	}
	
	private static void gatherAmbiguousProduction(NodeToUPTR converter, ArrayList<Link> prefixes, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, IActionExecutor actionExecutor, Object environment){
		IConstructor[] cachedPrefixResult = sharedPrefixCache.get(prefixes);
		if(cachedPrefixResult != null){
			buildAlternative(converter, cachedPrefixResult, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			
			// Check if there is a null prefix in this node's prefix list; if so handle the 'starts the production' case.
			for(int i = prefixes.size() - 1; i >= 0; --i){
				if(prefixes.get(i) == null){
					buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
				}
			}
			
			return;
		}
		
		ArrayList<IConstructor> gatheredPrefixes = new ArrayList<IConstructor>();
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){
				buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			}else{
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)){
					continue;
				}
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle.
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){
						gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(NO_NODES, cycle), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
						continue;
					}
				}
				
				gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(NO_NODES, prefixNode), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, actionExecutor, environment);
			}
		}
		
		int nrOfGatheredPrefixes = gatheredPrefixes.size();
		
		if(nrOfGatheredPrefixes == 1){
			IConstructor prefixAlternative = gatheredPrefixes.get(0);
			IList prefixAlternativeChildrenList = TreeAdapter.getArgs(prefixAlternative);
			
			int prefixLength = prefixAlternativeChildrenList.length();
			IConstructor[] prefixAlternativeChildren = new IConstructor[prefixLength];
			for(int i = prefixLength - 1; i >= 0; --i){
				prefixAlternativeChildren[i] = (IConstructor) prefixAlternativeChildrenList.get(i);
			}
			
			sharedPrefixCache.put(prefixes, prefixAlternativeChildren);
			
			buildAlternative(converter, prefixAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
		}else if(nrOfGatheredPrefixes > 0){
			ISetWriter ambSublist = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor prefixAlternative = gatheredPrefixes.get(i);
				IList prefixAlternativeChildrenList = TreeAdapter.getArgs(prefixAlternative);
				IConstructor alternativeSubList = VF.constructor(Factory.Tree_Appl, production, prefixAlternativeChildrenList);
				ambSublist.insert(alternativeSubList);
			}
			
			IConstructor prefixResult = VF.constructor(Factory.Tree_Amb, ambSublist.done());
			
			IConstructor[] prefixNodes = new IConstructor[]{prefixResult};
			sharedPrefixCache.put(prefixes, prefixNodes);
			
			buildAlternative(converter, prefixNodes, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
		}
	}
	
	private static CycleNode gatherCycle(Link child, AbstractNode[] postFix, ArrayList<AbstractNode> blackList){
		AbstractNode originNode = child.getNode();
		
		blackList.add(originNode);
		
		OUTER : do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){
				return null;
			}
			
			int nrOfPrefixes = prefixes.size();
			
			for(int i = nrOfPrefixes - 1; i >= 0; --i){
				Link prefix = prefixes.get(i);
				if(prefix == null) continue;
				AbstractNode prefixNode = prefix.getNode();
				
				if(prefixNode == originNode){
					return new CycleNode(postFix);
				}
				
				if(prefixNode.isEmpty()){
					int length = postFix.length;
					AbstractNode[] newPostFix = new AbstractNode[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					newPostFix[0] = prefixNode;
					
					child = prefix;
					postFix = newPostFix;
					continue OUTER;
				}
			}
			break;
		}while(true);
		
		return null;
	}
	
	public static IConstructor convertToUPTR(NodeToUPTR converter, ErrorListContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		if(depth <= cycleMark.depth){
			cycleMark.reset();
		}
		
		ISourceLocation sourceLocation = null;
		URI input = node.getInput();
		if(!(node.isLayout() || input == null)){
			int offset = node.getOffset();
			int endOffset = node.getEndOffset();
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle found.
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getType((IConstructor) node.getFirstProduction()), VF.integer(depth - index));
			cycle = actionExecutor.filterListCycle(cycle, environment);
			if(cycle != null && sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push.
		
		// Gather
		HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache = new HashMap<ArrayList<Link>, IConstructor[]>();
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, (IConstructor) node.getFirstProduction(), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<IConstructor> productions = (ArrayList<IConstructor>) node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, actionExecutor, environment);
			}
		}
		
		// Output.
		IConstructor result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			result = gatheredAlternatives.get(0);
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			ISetWriter ambSetWriter = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor alt = gatheredAlternatives.get(i);
				
				if(sourceLocation != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
				ambSetWriter.insert(alt);
			}
			
			result = VF.constructor(Factory.Tree_Error_Amb, ambSetWriter.done());
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop.
		
		return result;
	}
}
