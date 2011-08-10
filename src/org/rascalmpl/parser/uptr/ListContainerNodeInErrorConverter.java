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
import org.rascalmpl.parser.gtd.result.ExpandableContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.uptr.ListContainerNodeConverter.CycleNode;
import org.rascalmpl.parser.uptr.ListContainerNodeConverter.SharedPrefix;
import org.rascalmpl.parser.uptr.NodeToUPTR.CycleMark;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class ListContainerNodeInErrorConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	private final static IConstructor[] NO_CHILDREN = new IConstructor[]{};
	
	private ListContainerNodeInErrorConverter(){
		super();
	}
	
	private static Object buildAlternative(NodeToUPTR converter, IConstructor[] prefix, ForwardLink<AbstractNode> postFix, IConstructor production, ArrayList<IConstructor> gatheredAlternatives, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment);
		
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < prefix.length; ++i){
			childrenListWriter.append(prefix[i]);
		}
		
		int index = prefix.length - 1;
		
		int postFixLength = postFix.length;
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			newEnvironment = actionExecutor.enteringListNode(production, index++, newEnvironment);
			
			if(!(node instanceof CycleNode)){
				childrenListWriter.append(converter.convertWithErrors(node, stack, depth, cycleMark, positionStore, actionExecutor, environment));
			}else{
				CycleNode cycleNode = (CycleNode) node;
				IConstructor[] constructedCycle = constructCycle(converter, production, cycleNode, stack, depth, cycleMark, positionStore, actionExecutor, environment);
				
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
		
		IConstructor originalResult = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		IConstructor result = actionExecutor.filterListProduction(originalResult, newEnvironment);
		if(result == null){
			actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
			result = originalResult;
		}else{
			actionExecutor.exitedListProduction(production, false, newEnvironment); // Not filtered.
		}
		
		gatheredAlternatives.add(result);
		
		return newEnvironment;
	}
	
	private static IConstructor[] constructCycle(NodeToUPTR converter, IConstructor production, CycleNode cycleNode, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment);
		
		AbstractNode[] cycleElements = cycleNode.cycle;
		
		int nrOfCycleElements = cycleElements.length;
		IConstructor[] convertedCycle;
		if(nrOfCycleElements == 1){
			convertedCycle = new IConstructor[1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment);
			convertedCycle[0] = converter.convertWithErrors(cycleElements[0], stack, depth, cycleMark, positionStore, actionExecutor, environment);
		}else{
			convertedCycle = new IConstructor[nrOfCycleElements + 1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment);
			convertedCycle[0] = converter.convertWithErrors(cycleElements[nrOfCycleElements - 1], stack, depth, cycleMark, positionStore, actionExecutor, environment);
			for(int i = 0; i < nrOfCycleElements; ++i){
				newEnvironment = actionExecutor.enteringListNode(production, i + 1, newEnvironment);
				convertedCycle[i + 1] = converter.convertWithErrors(cycleElements[i], stack, depth, cycleMark, positionStore, actionExecutor, environment);
			}
		}
		
		IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getType(production), VF.integer(1));
		cycle = actionExecutor.filterListCycle(cycle, environment);
		if(cycle == null){
			cycle = VF.constructor(Factory.Tree_Error_Cycle, ProductionAdapter.getType(production), VF.integer(1));
		}
		
		IConstructor elements = VF.constructor(Factory.Tree_Appl, production, VF.list(convertedCycle));
		elements = actionExecutor.filterListProduction(elements, newEnvironment);
		if(elements == null){
			actionExecutor.exitedListProduction(production, true, newEnvironment);
			elements = VF.constructor(Factory.Tree_Error, production, VF.list(convertedCycle));
		}else{
			actionExecutor.exitedListProduction(production, false, newEnvironment);
		}
		
		IConstructor constructedCycle = VF.constructor(Factory.Tree_Amb, VF.set(elements, cycle));
		constructedCycle = actionExecutor.filterListAmbiguity(constructedCycle, newEnvironment);
		if(constructedCycle == null){
			constructedCycle = VF.constructor(Factory.Tree_Error_Amb, VF.set(elements, cycle));
		}
		
		return new IConstructor[]{constructedCycle};
	}
	
	protected static void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
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
	
	private static void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, IActionExecutor actionExecutor, Object environment){
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
	
	private static void gatherAmbiguousProduction(NodeToUPTR converter, ArrayList<Link> prefixes, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, IActionExecutor actionExecutor, Object environment){
		SharedPrefix sharedPrefix = sharedPrefixCache.get(prefixes);
		if(sharedPrefix != null){
			IConstructor[] cachedPrefix = sharedPrefix.prefix;
			if(cachedPrefix != null){
				buildAlternative(converter, cachedPrefix, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, sharedPrefix.environment);
			}
			
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
			
			Object newEnvironment = buildAlternative(converter, prefixAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? prefixAlternativeChildren : null, newEnvironment));
		}else if(nrOfGatheredPrefixes > 0){
			ISetWriter ambSublist = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor prefixAlternative = gatheredPrefixes.get(i);
				IList prefixAlternativeChildrenList = TreeAdapter.getArgs(prefixAlternative);
				IConstructor alternativeSubList = VF.constructor(Factory.Tree_Appl, production, prefixAlternativeChildrenList);
				ambSublist.insert(alternativeSubList);
			}
			
			IConstructor prefixResult = VF.constructor(Factory.Tree_Amb, ambSublist.done());
			prefixResult = actionExecutor.filterListAmbiguity(prefixResult, environment);
			if(prefixResult == null){
				sharedPrefixCache.put(prefixes, new SharedPrefix(null, null));
				return;
			}
			
			// Splice the elements into the list if the ambiguity cluster got filtered properly.
			if(TreeAdapter.isAppl(prefixResult)){
				if(ProductionAdapter.getType(TreeAdapter.getProduction(prefixResult)).equals(ProductionAdapter.getType(production))){
					IConstructor filteredAlternative = gatheredPrefixes.get(0);
					IList filteredAlternativeChildrenList = TreeAdapter.getArgs(filteredAlternative);
					
					int prefixLength = filteredAlternativeChildrenList.length();
					IConstructor[] filteredAlternativeChildren = new IConstructor[prefixLength];
					for(int i = prefixLength - 1; i >= 0; --i){
						filteredAlternativeChildren[i] = (IConstructor) filteredAlternativeChildrenList.get(i);
					}
					
					Object newEnvironment = buildAlternative(converter, filteredAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
					
					sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? filteredAlternativeChildren : null, newEnvironment));
					return;
				}
			}
			
			IConstructor[] prefixNodes = new IConstructor[]{prefixResult};
			
			Object newEnvironment = buildAlternative(converter, prefixNodes, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? prefixNodes : null, newEnvironment));
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
	
	public static IConstructor convertToUPTR(NodeToUPTR converter, ExpandableContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
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
			IConstructor rhsSymbol = ProductionAdapter.getType((IConstructor) node.getFirstProduction());
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, rhsSymbol, VF.integer(depth - index));
			cycle = actionExecutor.filterListCycle(cycle, environment);
			if(cycle == null){
				cycle = VF.constructor(Factory.Tree_Error_Cycle, rhsSymbol, VF.integer(depth - index));
			}
			
			if(sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push.
		
		// Gather
		HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache = new HashMap<ArrayList<Link>, SharedPrefix>();
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
			
			result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
			result = actionExecutor.filterListAmbiguity(result, environment);
			if(result == null){
				result = VF.constructor(Factory.Tree_Error_Amb, ambSetWriter.done());
			}
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop.
		
		return result;
	}
}
