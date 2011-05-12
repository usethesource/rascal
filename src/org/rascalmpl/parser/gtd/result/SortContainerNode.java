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
package org.rascalmpl.parser.gtd.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.IEnvironment;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class SortContainerNode extends AbstractContainerNode{
	
	public SortContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super(input, offset, endOffset, isNullable, isSeparator, isLayout);
	}
	
	protected void gatherAlternatives(Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		AbstractNode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			AbstractNode[] postFix = new AbstractNode[]{resultNode};
			gatherProduction(child, postFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}else{
			IEnvironment newEnvironment = actionExecutor.enteringProduction(production, environment);
			buildAlternative(production, new IConstructor[]{}, gatheredAlternatives, sourceLocation, filteringTracker, actionExecutor, newEnvironment);
		}
	}
	
	private void gatherProduction(Link child, AbstractNode[] postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			IEnvironment newEnvironment = actionExecutor.enteringProduction(production, environment);
			
			int postFixLength = postFix.length;
			IConstructor[] constructedPostFix = new IConstructor[postFixLength];
			for(int i = 0; i < postFixLength; ++i){
				newEnvironment = actionExecutor.enteringNode(production, i, newEnvironment);
				
				IConstructor node = postFix[i].toTree(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(node == null){
					actionExecutor.exitedProduction(production, true, newEnvironment);
					return;
				}
				constructedPostFix[i] = node;
			}
			
			buildAlternative(production, constructedPostFix, gatheredAlternatives, sourceLocation, filteringTracker, actionExecutor, newEnvironment);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			AbstractNode resultNode = prefix.node;
			if(!resultNode.isRejected()){
				int length = postFix.length;
				AbstractNode[] newPostFix = new AbstractNode[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = resultNode;
				gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
			}
		}
	}
	
	private void buildAlternative(IConstructor production, IValue[] children, ArrayList<IConstructor> gatheredAlternatives, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; --i){
			childrenListWriter.insert(children[i]);
		}
		
		IConstructor result = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		
		result = actionExecutor.filterProduction(result, environment);
		if(result == null){
			filteringTracker.setLastFilered(offset, endOffset);
			actionExecutor.exitedProduction(production, true, environment);
			return;
		}
		
		if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		
		gatheredAlternatives.add(result);
		actionExecutor.exitedProduction(production, false, environment);
	}
	
	public IConstructor toTree(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		if(depth <= cycleMark.depth){
			cycleMark.reset();
		}
		
		if(rejected){
			filteringTracker.setLastFilered(offset, endOffset);
			return null;
		}
		
		ISourceLocation sourceLocation = null;
		if(!(isLayout || input == null)){
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(firstProduction), VF.integer(depth - index));
			cycle = actionExecutor.filterCycle(cycle, environment);
			if(cycle != null && sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(this, depth); // Push.
		
		// Gather
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
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
				IConstructor alt = gatheredAlternatives.get(i);
				ambSetWriter.insert(alt);
			}
			
			result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
			result = actionExecutor.filterAmbiguity(result, environment);
			if(result == null){
				return null;
			}
			
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop.
		
		return result;
	}
	
	/*public IConstructor toErrorTree(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor){
		if(depth <= cycleMark.depth){
			cycleMark.reset();
		}
		
		return ErrorSortBuilder.toErrorSortTree(this, stack, depth, cycleMark, positionStore, actionExecutor);
	}*/
	
	public IConstructor toErrorTree(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, IActionExecutor actionExecutor, IEnvironment environment){
		return toTree(stack, depth, cycleMark, positionStore, new FilteringTracker(), actionExecutor, environment);
	}
}
