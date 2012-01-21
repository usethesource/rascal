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
import org.rascalmpl.parser.gtd.result.SortContainerNode;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.uptr.NodeToUPTR.CycleMark;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class SortContainerNodeInErrorConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	@SuppressWarnings("unchecked")
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	
	private SortContainerNodeInErrorConverter(){
		super();
	}
	
	protected static void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, IActionExecutor actionExecutor, Object environment){
		AbstractNode resultNode = child.getNode();
		
		if(!(resultNode.isEpsilon() && child.getPrefixes() == null)){
			gatherProduction(converter, child, new ForwardLink<AbstractNode>(NO_NODES, resultNode), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
		}else{
			buildAlternative(converter, NO_NODES, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
		}
	}
	
	private static void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, IActionExecutor actionExecutor, Object environment){
		ArrayList<Link> prefixes = child.getPrefixes();
		if(prefixes == null){
			buildAlternative(converter, postFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(postFix, prefix.getNode()), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
		}
	}
	
	private static void buildAlternative(NodeToUPTR converter, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringProduction(production, environment);
		
		int postFixLength = postFix.length;
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			newEnvironment = actionExecutor.enteringNode(production, i, newEnvironment);
			
			IConstructor constructedNode = converter.convertWithErrors(node, stack, depth, cycleMark, positionStore, actionExecutor, newEnvironment);
			if(constructedNode == null){
				actionExecutor.exitedProduction(production, true, newEnvironment);
				return;
			}
			childrenListWriter.append(constructedNode);
		}
		
		IConstructor originalResult = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		IConstructor result = actionExecutor.filterProduction(originalResult, environment);
		if(result == null){
			actionExecutor.exitedProduction(production, true, environment); // Filtered.
			result = originalResult;
		}else{
			actionExecutor.exitedProduction(production, false, environment); // Successful construction.
		}
		
		if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		
		gatheredAlternatives.add(result);
	}
	
	@SuppressWarnings("unchecked")
	public static IConstructor convertToUPTR(NodeToUPTR converter, SortContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, Object environment){
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
			IConstructor rhsSymbol = ProductionAdapter.getType((IConstructor) node.getFirstProduction());
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, rhsSymbol, VF.integer(depth - index));
			cycle = actionExecutor.filterCycle(cycle, environment);
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
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, (IConstructor) node.getFirstProduction(), stack, childDepth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<IConstructor> productions = (ArrayList<IConstructor>) node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, positionStore, sourceLocation, actionExecutor, environment);
			}
		}
		
		// Output.
		IConstructor result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			result = gatheredAlternatives.get(0);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			ISetWriter ambSetWriter = VF.setWriter(Factory.Tree);
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				ambSetWriter.insert(gatheredAlternatives.get(i));
			}
			
			result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
			result = actionExecutor.filterAmbiguity(result, environment);
			if(result == null){
				// Build error amb.
				result = VF.constructor(Factory.Tree_Error_Amb, ambSetWriter.done());
			}
			
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop.
		
		return result;
	}
}
