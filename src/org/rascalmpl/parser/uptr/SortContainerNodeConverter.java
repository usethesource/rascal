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
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.FilteringTracker;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.ForwardLink;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class SortContainerNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	private final static ForwardLink<AbstractNode> NO_NODES = ForwardLink.TERMINATOR;
	
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor, IConstructor>> preCache;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>> cache;
	
	public SortContainerNodeConverter(){
		super();
		
		preCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor,IConstructor>>();
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>>();
	}
	
	private void gatherAlternatives(NodeToUPTR converter, Link child, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		AbstractNode resultNode = child.getNode();
		
		if(!(resultNode.isEpsilon() && child.getPrefixes() == null)){
			gatherProduction(converter, child, new ForwardLink(NO_NODES, resultNode), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}else{
			buildAlternative(converter, NO_NODES, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}
	}
	
	private void gatherProduction(NodeToUPTR converter, Link child, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		ArrayList<Link> prefixes = child.getPrefixes();
		if(prefixes == null){
			buildAlternative(converter, postFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			gatherProduction(converter, prefix, new ForwardLink<AbstractNode>(postFix, prefix.getNode()), gatheredAlternatives, production, stack, depth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		}
	}
	
	private void buildAlternative(NodeToUPTR converter, ForwardLink<AbstractNode> postFix, ArrayList<IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ISourceLocation sourceLocation, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		Object newEnvironment = actionExecutor.enteringProduction(production, environment);
		
		int postFixLength = postFix.length;
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix.element;
			postFix = postFix.next;
			
			newEnvironment = actionExecutor.enteringNode(production, i, newEnvironment);
			
			IConstructor constructedNode = converter.convert(node, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			if(constructedNode == null){
				actionExecutor.exitedProduction(production, true, newEnvironment);
				return;
			}
			childrenListWriter.append(constructedNode);
		}
		
		IConstructor result = VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
		
		result = actionExecutor.filterProduction(result, environment);
		if(result == null){
			actionExecutor.exitedProduction(production, true, environment);
			return;
		}
		
		if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		
		gatheredAlternatives.add(result);
		actionExecutor.exitedProduction(production, false, environment);
	}
	
	public IConstructor convertToUPTR(NodeToUPTR converter, SortContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, Object environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();

		IConstructor rhs = ProductionAdapter.getType(node.getFirstProduction());
		boolean hasSideEffects = actionExecutor.isImpure(rhs);
		
		if(depth <= cycleMark.depth){
			if(!hasSideEffects){
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
		if(!(node.isLayout() || input == null)){
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = VF.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(node);
		if(index != -1){ // Cycle found.
			IConstructor cycle = VF.constructor(Factory.Tree_Cycle, rhs, VF.integer(depth - index));
			cycle = actionExecutor.filterCycle(cycle, environment);
			if(cycle != null && sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(node, depth); // Push.
		
		// Gather
		ArrayList<IConstructor> gatheredAlternatives = new ArrayList<IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, node.getFirstProduction(), stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<IConstructor> productions = node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, positionStore, sourceLocation, filteringTracker, actionExecutor, environment);
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
			if(result != null && sourceLocation != null){
				result = result.setAnnotation(Factory.Location, sourceLocation);
			}
		}
		
		stack.dirtyPurge(); // Pop.
		
		if(result != null && depth < cycleMark.depth){
			if(!hasSideEffects){
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
			}else{
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
