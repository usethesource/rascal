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
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.RecoveryNode;
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.parser.uptr.NodeToUPTR.CycleMark;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

/**
 * A converter for sort container result nodes.
 */
public class SortContainerNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	@SuppressWarnings("unchecked")
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor, IConstructor>> preCache;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>> cache;
	
	public SortContainerNodeConverter(){
		super();
		
		preCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor,IConstructor>>();
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>>();
	}
	
	/**
	 * Gather all the alternatives ending with the given child.
	 */
	private void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		AbstractNode resultNode = child.getNode();
		
		if(!(resultNode.isEpsilon() && child.getPrefixes() == null)){ // Has non-epsilon results.
			gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, resultNode), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}else{ // Has a single epsilon result.
			buildAlternative(converter, NO_NODES, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}
	}
	
	/**
	 * Gathers all alternatives for the given production related to the given child and postfix.
	 */
	private void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		ArrayList<Link> prefixes = child.getPrefixes();
		if(prefixes == null){ // Reached the start of the production.
			buildAlternative(converter, postFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){ // Traverse all the prefixes (can be more then one in case of ambiguity).
			Link prefix = prefixes.get(i);
			gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(postFix, prefix.getNode()), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}
	}
	
	/**
	 * Construct the UPTR representation for the given production.
	 * Additionally, it handles all semantic actions related 'events' associated with it.
	 */
	private void buildAlternative(NodeToUPTR converter, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringProduction(production, environment); // Fire a 'entering production' event to enable environment handling.
		
		int postFixLength = postFix.length;
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			if (node instanceof RecoveryNode) {
				System.err.println("hallo, hier is een recovery node");
			}
			newEnvironment = actionExecutor.enteringNode(production, i, newEnvironment); // Fire a 'entering node' event when converting a child to enable environment handling.
			
			IConstructor constructedNode = converter.convert(node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			if(constructedNode == null){
				actionExecutor.exitedProduction(production, true, newEnvironment); // Filtered.
				return;
			}
			childrenListWriter.append(constructedNode);
		}
		
		IConstructor result = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		
		result = actionExecutor.filterProduction(result, environment); // Execute the semantic actions associated with this node.
		if(result == null){
			actionExecutor.exitedProduction(production, true, environment); // Filtered.
			return;
		}
		
		if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation); // Add location information (if available).
		
		gatheredAlternatives.add(result);
		actionExecutor.exitedProduction(production, false, environment); // Successful construction.
	}
	
	/**
	 * Converts the given sort container result node to the UPTR format.
	 */
	public IConstructor convertToUPTR(NodeToUPTR converter, SortContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();

		Object firstProduction = node.getFirstProduction();
		if (firstProduction == null) {
			System.err.println("this is weird");
		}
		IConstructor rhs = ProductionAdapter.getType((IConstructor) firstProduction);
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
			cycle = actionExecutor.filterCycle(cycle, environment);
			if(cycle != null && sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push this node on the stack.
		
		// Gather the alternatives.
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, (IConstructor) firstProduction, stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		@SuppressWarnings("unchecked")
		ArrayList<IConstructor> productions = (ArrayList<IConstructor>) node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
			}
		}
		
		// Construct the resulting tree containing all gathered alternatives.
		IConstructor result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			result = gatheredAlternatives.get(0);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			ISetWriter ambSetWriter = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor alt = gatheredAlternatives.get(i);
				ambSetWriter.insert(alt);
			}
			
			result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
			result = actionExecutor.filterAmbiguity(result, environment);
			if(result != null && sourceLocation != null){
				result = result.setAnnotation(Factory.Location, sourceLocation);
			}
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
