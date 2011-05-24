package org.rascalmpl.parser.gtd.result.uptr;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.gtd.location.PositionStore;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.result.ListContainerNode;
import org.rascalmpl.parser.gtd.result.AbstractNode.CycleMark;
import org.rascalmpl.parser.gtd.result.AbstractNode.FilteringTracker;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.action.IEnvironment;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.ObjectIntegerKeyedHashSet;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ListContainerNodeConverter{
	private final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor, IConstructor>> preCache;
	private final IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>> cache;
	
	public ListContainerNodeConverter(){
		super();

		preCache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashMap<IConstructor,IConstructor>>();
		cache = new IntegerKeyedHashMap<ObjectIntegerKeyedHashSet<IConstructor>>();
	}
	
	protected static class CycleNode extends AbstractNode{
		public final AbstractNode[] cycle;
		
		public CycleNode(AbstractNode[] cycle){
			super();
			
			this.cycle = cycle;
		}
		
		public int getID(){
			throw new UnsupportedOperationException("CycleNode does not have an ID, it's for internal use only.");
		}
		
		public void addAlternative(IConstructor production, Link children){
			throw new UnsupportedOperationException();
		}
		
		public boolean isEmpty(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isRejected(){
			throw new UnsupportedOperationException();
		}
		
		public boolean isSeparator(){
			throw new UnsupportedOperationException();
		}
		
		public void setRejected(){
			throw new UnsupportedOperationException();
		}
	}
	
	private IConstructor[] constructPostFix(NodeToUPTR converter, AbstractNode[] postFix, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		int postFixLength = postFix.length;
		int offset = 0;
		IConstructor[] constructedPostFix = new IConstructor[postFixLength];
		for(int i = 0; i < postFixLength; ++i){
			AbstractNode node = postFix[i];
			if(!(node instanceof CycleNode)){
				IConstructor constructedNode = converter.convert(postFix[i], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(constructedNode == null) return null;
				constructedPostFix[offset + i] = constructedNode;
			}else{
				CycleNode cycleNode = (CycleNode) node;
				IConstructor[] convertedCycle = convertCycle(converter, cycleNode, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(convertedCycle == null) return null;
				
				IConstructor[] constructedCycle = constructCycle(convertedCycle, production, actionExecutor, environment);
				if(constructedCycle == null) return null;
				
				int constructedCycleLength = constructedCycle.length;
				if(constructedCycleLength == 1){
					constructedPostFix[offset + i] = constructedCycle[0];
				}else{
					offset += constructedCycleLength;
					int currentLength = constructedPostFix.length;
					IConstructor[] newConstructedPostFix = new IConstructor[currentLength + constructedCycleLength];
					System.arraycopy(constructedPostFix, 0, newConstructedPostFix, 0, i);
					System.arraycopy(constructedCycle, 0, newConstructedPostFix, i, constructedCycleLength);
				}
			}
		}
		
		return constructedPostFix;
	}
	
	private IConstructor[] convertCycle(NodeToUPTR converter, CycleNode cycleNode, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		AbstractNode[] cycleElements = cycleNode.cycle;
		
		int nrOfCycleElements = cycleElements.length;
		IConstructor[] convertedCycle;
		if(nrOfCycleElements == 1){
			convertedCycle = new IConstructor[1];
			IConstructor element = converter.convert(cycleElements[0], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			if(element == null) return null;
			convertedCycle[0] = element;
		}else{
			convertedCycle = new IConstructor[nrOfCycleElements + 1];
			for(int i = 0; i < nrOfCycleElements; ++i){
				IConstructor element = converter.convert(cycleElements[i], stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(element == null) return null;
				convertedCycle[i + 1] = element;
			}
			convertedCycle[0] = convertedCycle[nrOfCycleElements];
		}
		
		return convertedCycle;
	}
	
	private IConstructor[] constructCycle(IConstructor[] convertedCycle, IConstructor production, IActionExecutor actionExecutor, IEnvironment environment){
		IConstructor cycle = VF.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(production), VF.integer(1));
		cycle = actionExecutor.filterCycle(cycle, environment);
		if(cycle == null){
			return convertedCycle;
		}
		
		IConstructor elements = VF.constructor(Factory.Tree_Appl, production, VF.list(convertedCycle));
		
		IConstructor constructedCycle = VF.constructor(Factory.Tree_Amb, VF.set(elements, cycle));
		
		return new IConstructor[]{constructedCycle};
	}
	
	protected void gatherAlternatives(NodeToUPTR converter, Link child, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		AbstractNode childNode = child.getNode();
		
		if(!(childNode.isEpsilon() && child.getPrefixes() == null)){
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){
				CycleNode cycle = gatherCycle(child, new AbstractNode[]{childNode}, blackList);
				if(cycle != null){
					if(cycle.cycle.length == 1){
						gatherProduction(converter, child, new AbstractNode[]{cycle}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
					}else{
						gatherProduction(converter, child, new AbstractNode[]{childNode, cycle}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
					}
					return;
				}
			}
			gatherProduction(converter, child, new AbstractNode[]{childNode}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
		}else{
			gatheredAlternatives.add(new IConstructor[]{}, production);
		}
	}
	
	private void gatherProduction(NodeToUPTR converter, Link child, AbstractNode[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		do{
			ArrayList<Link> prefixes = child.getPrefixes();
			if(prefixes == null){
				IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(constructedPostFix == null) return;
				
				gatheredAlternatives.add(constructedPostFix, production);
				return;
			}
			
			if(prefixes.size() == 1){
				Link prefix = prefixes.get(0);
				
				if(prefix == null){
					IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
					if(constructedPostFix == null) return;
					
					gatheredAlternatives.add(constructedPostFix, production);
					return;
				}
				
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)){
					return;
				}
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){
						prefixNode = cycle;
					}
				}
				
				int length = postFix.length;
				AbstractNode[] newPostFix = new AbstractNode[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = prefixNode;
				
				child = prefix;
				postFix = newPostFix;
				continue;
			}
			
			gatherAmbiguousProduction(converter, prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
			
			break;
		}while(true);
	}
	
	private void gatherAmbiguousProduction(NodeToUPTR converter, ArrayList<Link> prefixes, AbstractNode[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		IConstructor[] cachedPrefixResult = sharedPrefixCache.get(prefixes);
		if(cachedPrefixResult != null){
			int prefixResultLength = cachedPrefixResult.length;
			IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			int length = constructedPostFix.length;
			
			IConstructor[] newPostFix = new IConstructor[prefixResultLength + length];
			System.arraycopy(cachedPrefixResult, 0, newPostFix, 0, prefixResultLength);
			System.arraycopy(constructedPostFix, 0, newPostFix, prefixResultLength, length);
			
			gatheredAlternatives.add(newPostFix, production);
			return;
		}
		
		DoubleArrayList<IConstructor[], IConstructor> gatheredPrefixes = new DoubleArrayList<IConstructor[], IConstructor>();
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){
				IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
				if(constructedPostFix == null) return;
				
				gatheredAlternatives.add(constructedPostFix, production);
			}else{
				AbstractNode prefixNode = prefix.getNode();
				if(blackList.contains(prefixNode)){
					continue;
				}
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					CycleNode cycle = gatherCycle(prefix, new AbstractNode[]{prefixNode}, blackList);
					if(cycle != null){
						gatherProduction(converter, prefix, new AbstractNode[]{cycle}, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
						continue;
					}
				}
				
				gatherProduction(converter, prefix, new AbstractNode[]{prefixNode}, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor, environment);
			}
		}
		
		int nrOfGatheredPrefixes = gatheredPrefixes.size();
		
		if(nrOfGatheredPrefixes == 1){
			IConstructor[] prefixAlternative = gatheredPrefixes.getFirst(0);
			
			IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			if(constructedPostFix == null) return;
			
			int length = constructedPostFix.length;
			int prefixLength = prefixAlternative.length;
			IConstructor[] newPostFix = new IConstructor[length + prefixLength];
			System.arraycopy(prefixAlternative, 0, newPostFix, 0, prefixLength);
			System.arraycopy(constructedPostFix, 0, newPostFix, prefixLength, length);
			
			gatheredAlternatives.add(newPostFix, production);
		}else if(nrOfGatheredPrefixes > 0){
			ISetWriter ambSublist = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor alternativeSubList = VF.constructor(Factory.Tree_Appl, production, VF.list(gatheredPrefixes.getFirst(i)));
				ambSublist.insert(alternativeSubList);
			}
			
			IConstructor prefixResult = VF.constructor(Factory.Tree_Amb, ambSublist.done());
			
			IConstructor[] constructedPostFix = constructPostFix(converter, postFix, production, stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor, environment);
			if(constructedPostFix == null) return;
			
			int length = constructedPostFix.length;
			IConstructor[] newPostFix = new IConstructor[length + 1];
			newPostFix[0] = prefixResult;
			System.arraycopy(constructedPostFix, 0, newPostFix, 1, length);
			
			gatheredAlternatives.add(newPostFix, production);
			
			sharedPrefixCache.put(prefixes, new IConstructor[]{prefixResult});
		}
	}
	
	private CycleNode gatherCycle(Link child, AbstractNode[] postFix, ArrayList<AbstractNode> blackList){
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
	
	private IConstructor buildAlternative(IConstructor production, IValue[] children){
		IListWriter childrenListWriter = VF.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; --i){
			childrenListWriter.insert(children[i]);
		}
		
		return VF.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IConstructor convertToUPTR(NodeToUPTR converter, ListContainerNode node, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor, IEnvironment environment){
		int offset = node.getOffset();
		int endOffset = node.getEndOffset();
		
		IConstructor rhs = ProductionAdapter.getRhs(node.getFirstProduction());
		boolean hasSideEffects = actionExecutor.mayHaveSideEffects(rhs);
		
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
		
		if(node.isRejected()){
			filteringTracker.setLastFilered(node.getOffset(), node.getEndOffset());
			return null;
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
		HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache = new HashMap<ArrayList<Link>, IConstructor[]>();
		DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives = new DoubleArrayList<IConstructor[], IConstructor>();
		gatherAlternatives(converter, node.getFirstAlternative(), gatheredAlternatives, node.getFirstProduction(), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, filteringTracker, actionExecutor, environment);
		ArrayList<Link> alternatives = node.getAdditionalAlternatives();
		ArrayList<IConstructor> productions = node.getAdditionalProductions();
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(converter, alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, sharedPrefixCache, positionStore, filteringTracker, actionExecutor, environment);
			}
		}
		
		// Output.
		IConstructor result = null;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative);
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}else if(nrOfAlternatives > 0){ // Ambiguous.
			ISetWriter ambSetWriter = VF.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				
				IConstructor alt = buildAlternative(production, alternative);
				if(sourceLocation != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
				ambSetWriter.insert(alt);
			}
			
			result = VF.constructor(Factory.Tree_Amb, ambSetWriter.done());
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
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
