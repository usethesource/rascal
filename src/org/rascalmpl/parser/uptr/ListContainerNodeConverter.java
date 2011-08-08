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
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.uptr.NodeToUPTR.CycleMark;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

/**
 * A converter for 'expandable' container result nodes.
 * In the case of the UPTR format, this implies variations of lists.
 */
public class ListContainerNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	private final static IConstructor[] NO_CHILDREN = new IConstructor[]{};
	
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor, IConstructor>> preCache;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>> cache;
	
	public ListContainerNodeConverter(){
		super();

		preCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor,IConstructor>>();
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>>();
	}
	
	/**
	 * A helper structure for keeping track of cycles inside lists.
	 * These cycles can occur due to nullable elements and separators.
	 */
	protected static class CycleNode extends AbstractNode{
		public final AbstractNode[] cycle;
		
		public CycleNode(AbstractNode[] cycle){
			super();
			
			this.cycle = cycle;
		}
		
		public int getTypeIdentifier(){
			throw new UnsupportedOperationException("CycleNode does not have an ID, it's for internal use only.");
		}
		
		public boolean isEmpty(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isRejected(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isNonterminalSeparator(){
			throw new UnsupportedOperationException();
		}
		
		public void setRejected(){
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * A helper structure for storing shared prefixes of lists.
	 */
	protected static class SharedPrefix{
		public final IConstructor[] prefix;
		public final Object environment;
		
		public SharedPrefix(IConstructor[] prefix, Object environment){
			super();
			
			this.prefix = prefix;
			this.environment = environment;
		}
	}
	
	/**
	 * Construct the UPTR representation for the given production.
	 * Additionally, it handles all semantic actions related 'events' associated with it.
	 */
	private Object buildAlternative(NodeToUPTR converter, IConstructor[] prefix, ForwardLink<AbstractNode> postFix, IConstructor production, ArrayList<IConstructor> gatheredAlternatives, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < prefix.length; ++i){
			childrenListWriter.append(prefix[i]);
		}

		int index = prefix.length - 1;
		
		int postFixLength = postFix.length;
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			newEnvironment = actionExecutor.enteringListNode(production, index++, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			
			if(!(node instanceof CycleNode)){ // Not a cycle.
				IConstructor constructedNode = converter.convert(node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
				if(constructedNode == null){
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				
				childrenListWriter.append(constructedNode);
			}else{ // Cycle.
				CycleNode cycleNode = (CycleNode) node;
				IConstructor[] constructedCycle = constructCycle(converter, production, cycleNode, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
				if(constructedCycle == null){
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				
				int constructedCycleLength = constructedCycle.length;
				if(constructedCycleLength == 1){
					childrenListWriter.append(constructedCycle[0]);
				}else{
					for(int j = 0; j < constructedCycleLength; ++j){
						childrenListWriter.append(constructedCycle[j]);
					}
				}
			}
		}
		
		IConstructor result = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		result = actionExecutor.filterListProduction(result, newEnvironment); // Execute the semantic actions associated with this list.
		if(result == null){
			actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
			return null;
		}

		actionExecutor.exitedListProduction(production, false, newEnvironment); // Successful construction.
		
		gatheredAlternatives.add(result);
		
		return newEnvironment;
	}
	
	/**
	 * Construct the UPTR representation for the given cycle.
	 */
	private IConstructor[] constructCycle(NodeToUPTR converter, IConstructor production, CycleNode cycleNode, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringListProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		AbstractNode[] cycleElements = cycleNode.cycle;
		
		int nrOfCycleElements = cycleElements.length;
		IConstructor[] convertedCycle;
		if(nrOfCycleElements == 1){ // A single element cycle (non-separated list).
			convertedCycle = new IConstructor[1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			IConstructor element = converter.convert(cycleElements[0], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
			if(element == null){
				actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
				return null;
			}
			convertedCycle[0] = element;
		}else{ // A multi element cycle (separated list).
			// The last node in the array is the actual list element.
			// Since cycles aren't allowed to start or end at separators, construct the cycle with the element both at the begin and end.
			convertedCycle = new IConstructor[nrOfCycleElements + 1];
			
			newEnvironment = actionExecutor.enteringListNode(production, 0, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			convertedCycle[0] = converter.convert(cycleElements[nrOfCycleElements - 1], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
			for(int i = 0; i < nrOfCycleElements; ++i){
				newEnvironment = actionExecutor.enteringListNode(production, i + 1, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
				IConstructor element = converter.convert(cycleElements[i], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, newEnvironment);
				if(element == null) {
					actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
					return null;
				}
				convertedCycle[i + 1] = element;
			}
		}
		
		IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getType(production), VF.integer(1));
		cycle = actionExecutor.filterListCycle(cycle, environment); // Execute the semantic actions associated with the list this cycle belongs to.
		if(cycle == null){
			return convertedCycle;
		}
		
		IConstructor elements = VF.constructor(Factory.Tree_Appl, production, VF.list(convertedCycle));
		elements = actionExecutor.filterListProduction(elements, newEnvironment); // Execute the semantic actions associated with this list.
		if(elements == null){
			actionExecutor.exitedListProduction(production, true, newEnvironment); // Filtered.
			return null;
		}
		
		actionExecutor.exitedListProduction(production, false, newEnvironment); // Successful construction.
		
		IConstructor constructedCycle = VF.constructor(Factory.Tree_Amb, VF.set(elements, cycle)); // Execute the semantic actions associated with this ambiguous list.
		constructedCycle = actionExecutor.filterListAmbiguity(constructedCycle, newEnvironment);
		if(constructedCycle == null) return null;
		
		return new IConstructor[]{constructedCycle};
	}
	
	/**
	 * Gather all the alternatives ending with the given child.
	 */
	protected void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		AbstractNode childNode = child.getNode();
		
		if(!(childNode.isEpsilon() && child.getPrefixes() == null)){ // Has non-epsilon results.
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){ // Child starts a cycle.
				CycleNode cycle = gatherCycle(child, new AbstractNode[]{childNode}, blackList);
				if(cycle != null){ // Encountered a cycle.
					if(cycle.cycle.length == 1){
						gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, cycle), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
					}else{
						ForwardLink<AbstractNode> cycleLink = new ForwardLink<AbstractNode>(NO_NODES, cycle);
						gatherProduction(converter, child, new ForwardLink<AbstractNode>(cycleLink, childNode), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
					}
					return;
				}
			}
			// Encountered non-cyclic child.
			gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, childNode), gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
		}else{ // Has a single epsilon result.
			buildAlternative(converter, NO_CHILDREN, NO_NODES, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
		}
	}
	
	/**
	 * Gathers all alternatives for the given production related to the given child and postfix.
	 */
	private void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){ // Start of the production encountered.
				buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				return;
			}
			
			// One prefix, so not ambiguous at this point.
			if(prefixes.size() == 1){
				Link prefix = prefixes.get(0);
				
				if(prefix == null){ // Start of the production encountered.
					buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
					return;
				}
				
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)) return; // Prefix node is not allowed (due to being part of a cycle already gathered cycle).
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle (separators can't start or end cycles, only elements can).
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){ // Encountered cycle, insert it.
						prefixNode = cycle;
					}
				}
				
				child = prefix;
				postFix = new ForwardLink<AbstractNode>(postFix, prefixNode);
				continue; // Reuse the stack frame for the next iteration (part of the conditional tail-recursion optimization; this is required to prevent stack-overflows when flattening long lists).
			}
			
			// Multiple prefixes, so the list is ambiguous at this point.
			gatherAmbiguousProduction(converter, prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
			
			break;
		}while(true);
	}
	
	/**
	 * Gathers all alternatives for the given ambiguous production related to the given child and postfix.
	 */
	private void gatherAmbiguousProduction(NodeToUPTR converter, ArrayList<Link> prefixes, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		// Check if we've been at this node before. If so reuse the cached prefix.
		SharedPrefix sharedPrefix = sharedPrefixCache.get(prefixes);
		if(sharedPrefix != null){
			IConstructor[] cachedPrefix = sharedPrefix.prefix;
			if(cachedPrefix != null){
				buildAlternative(converter, cachedPrefix, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, sharedPrefix.environment);
			}
			
			// Check if there is a null prefix in this node's prefix list (this can happen if this node both start the list and has an empty prefix).
			for(int i = prefixes.size() - 1; i >= 0; --i){
				if(prefixes.get(i) == null){
					buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				}
			}
			
			return;
		}
		
		// Gather all alternative prefixes.
		ArrayList<IConstructor> gatheredPrefixes = new ArrayList<IConstructor>();
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){ // List start node encountered.
				buildAlternative(converter, NO_CHILDREN, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			}else{
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)) continue; // Prefix node is not allowed (due to being part of a cycle already gathered cycle).
				
				if(prefixNode.isEmpty() && !prefixNode.isNonterminalSeparator()){ // Possibly a cycle (separators can't start or end cycles, only elements can).
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){ // Encountered a cycle.
						gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(NO_NODES, cycle), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
						continue;
					}
				}
				
				gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(NO_NODES, prefixNode), gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
			}
		}
		
		int nrOfGatheredPrefixes = gatheredPrefixes.size();
		
		// Non-ambiguous prefix.
		if(nrOfGatheredPrefixes == 1){
			IConstructor prefixAlternative = gatheredPrefixes.get(0);
			IList prefixAlternativeChildrenList = TreeAdapter.getArgs(prefixAlternative);
			
			int prefixLength = prefixAlternativeChildrenList.length();
			IConstructor[] prefixAlternativeChildren = new IConstructor[prefixLength];
			for(int i = prefixLength - 1; i >= 0; --i){
				prefixAlternativeChildren[i] = (IConstructor) prefixAlternativeChildrenList.get(i);
			}
			
			Object newEnvironment = buildAlternative(converter, prefixAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? prefixAlternativeChildren : null, newEnvironment));
		}else if(nrOfGatheredPrefixes > 0){ // Ambiguous prefix.
			ISetWriter ambSublist = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor prefixAlternative = gatheredPrefixes.get(i);
				IList prefixAlternativeChildrenList = TreeAdapter.getArgs(prefixAlternative);
				IConstructor alternativeSubList = VF.constructor(Factory.Tree_Appl, production, prefixAlternativeChildrenList);
				ambSublist.insert(alternativeSubList);
			}
			
			IConstructor prefixResult = VF.constructor(Factory.Tree_Amb, ambSublist.done());
			prefixResult = actionExecutor.filterListAmbiguity(prefixResult, environment);
			if(prefixResult == null){ // Ambiguous list prefix got filtered, remember this.
				sharedPrefixCache.put(prefixes, new SharedPrefix(null, null));
				return;
			}
			
			// Splice the elements into the list if a single alternative remained after filtering the ambiguity cluster.
			if(TreeAdapter.isAppl(prefixResult)){
				if(ProductionAdapter.getType(TreeAdapter.getProduction(prefixResult)).equals(ProductionAdapter.getType(production))){
					IConstructor filteredAlternative = gatheredPrefixes.get(0);
					IList filteredAlternativeChildrenList = TreeAdapter.getArgs(filteredAlternative);
					
					int prefixLength = filteredAlternativeChildrenList.length();
					IConstructor[] filteredAlternativeChildren = new IConstructor[prefixLength];
					for(int i = prefixLength - 1; i >= 0; --i){
						filteredAlternativeChildren[i] = (IConstructor) filteredAlternativeChildrenList.get(i);
					}
					
					Object newEnvironment = buildAlternative(converter, filteredAlternativeChildren, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
					
					sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? filteredAlternativeChildren : null, newEnvironment));
					return;
				}
			}
			
			IConstructor[] prefixNodes = new IConstructor[]{prefixResult};
			
			Object newEnvironment = buildAlternative(converter, prefixNodes, postFix, production, gatheredAlternatives, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			
			sharedPrefixCache.put(prefixes, new SharedPrefix(newEnvironment != null ? prefixNodes : null, newEnvironment));
		}
	}
	
	/**
	 * Gathers the cycle related to the given child.
	 */
	private CycleNode gatherCycle(Link child, AbstractNode[] postFix, ArrayList<AbstractNode> blackList){
		AbstractNode originNode = child.getNode();
		
		blackList.add(originNode); // Prevent the cycle node from being traversed again.
		
		OUTER : do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){ // Encountered the start of the list (so no cycle detected).
				return null;
			}
			
			int nrOfPrefixes = prefixes.size();
			
			for(int i = nrOfPrefixes - 1; i >= 0; --i){
				Link prefix = prefixes.get(i);
				if(prefix == null) continue;
				AbstractNode prefixNode = prefix.getNode();
				
				if(prefixNode == originNode){ // Cycle detected.
					return new CycleNode(postFix);
				}
				
				if(prefixNode.isEmpty()){ // Only empty nodes can be part of a cycle.
					int length = postFix.length;
					AbstractNode[] newPostFix = new AbstractNode[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					newPostFix[0] = prefixNode;
					
					child = prefix;
					postFix = newPostFix;
					continue OUTER;
				}
				// Fall through means no cycle was detected.
			}
			break;
		}while(true);
		
		return null;
	}
	
	/**
	 * Converts the given expandable container result node to the UPTR format.
	 */
	public IConstructor convertToUPTR(NodeToUPTR converter, ExpandableContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();
		
		IConstructor rhs = ProductionAdapter.getType((IConstructor) node.getFirstProduction());
		boolean hasSideEffects = actionExecutor.isImpure(rhs);
		
		if(depth <= cycleMark.depth){ // Only check for sharing if we are not currently inside a cycle.
			if(!hasSideEffects){ // If this sort node and its direct and indirect children do not rely on side-effects from semantic actions, check the cache for existing results.
				ObjectIntegerKeyedHashMap<IConstructor, IConstructor> levelCache = preCache.get(offset);
				if(levelCache != null){
					IConstructor cachedResult = levelCache.get(rhs, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
				}
			}
			
			cycleMark.reset();
		}
		
		ISourceLocation sourceLocation = null;
		URI input = node.getInput();
		if(!(node.isLayout() || input == null)){ // Construct a source location annotation if this sort container does not represent a layout non-terminal and if it's available.
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle detected.
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, rhs, VF.integer(depth - index));
			cycle = actionExecutor.filterListCycle(cycle, environment);
			if(cycle != null && sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push this node on the stack.
		
		// Gather the alternatives.
		HashMap<ArrayList<Link>, SharedPrefix> sharedPrefixCache = new HashMap<ArrayList<Link>, SharedPrefix>();
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, (IConstructor) node.getFirstProduction(), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, filteringTracker, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<IConstructor> productions = (ArrayList<IConstructor>) node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, filteringTracker, actionExecutor, environment);
			}
		}
		
		// Construct the resulting tree containing all gathered alternatives.
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
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop this node off the stack.
		
		if(result != null && depth < cycleMark.depth){ // Only share the constructed tree if we are not in a cycle.
			if(!hasSideEffects){ // Cache side-effect free tree.
				ObjectIntegerKeyedHashMap<IConstructor, IConstructor> levelCache = preCache.get(offset);
				if(levelCache != null){
					IConstructor cachedResult = levelCache.get(rhs, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
					
					levelCache.putUnsafe(rhs, endOffset, result);
					return result;
				}
				
				levelCache = new ObjectIntegerKeyedHashMap<IConstructor, IConstructor>();
				levelCache.putUnsafe(rhs, endOffset, result);
				preCache.put(offset, levelCache);
			}else{ // Cache tree with side-effects.
				ObjectIntegerKeyedHashSet<IConstructor> levelCache = cache.get(offset);
				if(levelCache != null){
					IConstructor cachedResult = levelCache.getEquivalent(result, endOffset);
					if(cachedResult != null){
						return cachedResult;
					}
					
					levelCache.putUnsafe(result, endOffset);
					return result;
				}
				
				levelCache = new ObjectIntegerKeyedHashSet<IConstructor>();
				levelCache.putUnsafe(result, endOffset);
				cache.putUnsafe(offset, levelCache);
			}
		}
		
		return result;
	}
}
