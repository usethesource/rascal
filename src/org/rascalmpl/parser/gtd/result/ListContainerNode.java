package org.rascalmpl.parser.gtd.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.rascalmpl.parser.IActionExecutor;
import org.rascalmpl.parser.gtd.result.struct.Link;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleArrayList;
import org.rascalmpl.parser.gtd.util.HashMap;
import org.rascalmpl.parser.gtd.util.IndexedStack;
import org.rascalmpl.parser.gtd.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ListContainerNode extends AbstractContainerNode{
	
	public ListContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super(input, offset, endOffset, isNullable, isSeparator, isLayout);
	}
	
	protected void gatherAlternatives(Link child, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, FilteringTracker filteringTracker, IActionExecutor actionExecutor){
		HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache = new HashMap<ArrayList<Link>, IConstructor[]>();
		
		AbstractNode childNode = child.node;
		
		if(!(childNode.isEpsilon() && child.prefixes == null)){
			IConstructor result = childNode.toTerm(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor);
			if(result == null) return; // Rejected.
			
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){
				IConstructor[] cycle = gatherCycle(child, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList, filteringTracker, actionExecutor);
				if(cycle != null){
					IConstructor cycleNode = buildCycle(cycle, production, actionExecutor);
					if(cycleNode == null){
						gatherProduction(child, new IConstructor[]{result}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
					}else{
						if(cycle.length == 1){
							gatherProduction(child, new IConstructor[]{cycleNode}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
						}else{
							gatherProduction(child, new IConstructor[]{result, cycleNode}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
						}
					}
					return;
				}
			}
			gatherProduction(child, new IConstructor[]{result}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
		}else{
			gatheredAlternatives.add(new IConstructor[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, IConstructor[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor){
		do{
			ArrayList<Link> prefixes = child.prefixes;
			if(prefixes == null){
				gatheredAlternatives.add(postFix, production);
				return;
			}
			
			if(prefixes.size() == 1){
				Link prefix = prefixes.get(0);
				
				if(prefix == null){
					gatheredAlternatives.add(postFix, production);
					return;
				}
				
				AbstractNode prefixNode = prefix.node;
				if(blackList.contains(prefixNode)){
					return;
				}
				
				IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor);
				if(result == null) return; // Rejected.
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					IConstructor[] cycle = gatherCycle(prefix, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList, filteringTracker, actionExecutor);
					if(cycle != null){
						IConstructor[] newPostFix = buildCycle(cycle, postFix, result, production, actionExecutor);
						
						child = prefix;
						postFix = newPostFix;
						continue;
					}
				}
				
				int length = postFix.length;
				IConstructor[] newPostFix = new IConstructor[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = result;
				
				child = prefix;
				postFix = newPostFix;
				continue;
			}
			
			gatherAmbiguousProduction(prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
			
			break;
		}while(true);
	}
	
	private void gatherAmbiguousProduction(ArrayList<Link> prefixes, IConstructor[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor[]> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor){
		IConstructor[] cachedPrefixResult = sharedPrefixCache.get(prefixes);
		if(cachedPrefixResult != null){
			int prefixResultLength = cachedPrefixResult.length;
			IConstructor[] newPostFix;
			if(prefixResultLength == 1){
				int length = postFix.length;
				newPostFix = new IConstructor[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = cachedPrefixResult[0];
			}else{
				int length = postFix.length;
				newPostFix = new IConstructor[prefixResultLength + length];
				System.arraycopy(cachedPrefixResult, 0, newPostFix, 0, prefixResultLength);
				System.arraycopy(postFix, 0, newPostFix, prefixResultLength, length);
			}
			
			gatheredAlternatives.add(newPostFix, production);
			return;
		}
		
		DoubleArrayList<IConstructor[], IConstructor> gatheredPrefixes = new DoubleArrayList<IConstructor[], IConstructor>();
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){
				gatheredAlternatives.add(postFix, production);
			}else{
				AbstractNode prefixNode = prefix.node;
				if(blackList.contains(prefixNode)){
					continue;
				}
				
				IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor);
				if(result == null) continue; // Rejected.
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					IConstructor[] cycle = gatherCycle(prefix, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList, filteringTracker, actionExecutor);
					if(cycle != null){
						IConstructor[] newPostFix = buildCycle(cycle, postFix, result, production, actionExecutor);
						
						gatherProduction(prefix, newPostFix, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
						continue;
					}
				}
				
				gatherProduction(prefix, new IConstructor[]{result}, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList, filteringTracker, actionExecutor);
			}
		}
		
		int nrOfGatheredPrefixes = gatheredPrefixes.size();
		if(nrOfGatheredPrefixes ==  0) return;
		
		if(nrOfGatheredPrefixes == 1){
			IConstructor[] prefixAlternative = gatheredPrefixes.getFirst(0);
			
			int length = postFix.length;
			int prefixLength = prefixAlternative.length;
			IConstructor[] newPostFix = new IConstructor[length + prefixLength];
			System.arraycopy(postFix, 0, newPostFix, prefixLength, length);
			System.arraycopy(prefixAlternative, 0, newPostFix, 0, prefixLength);
			
			gatheredAlternatives.add(newPostFix, production);
		}else{
			ISetWriter ambSublist = vf.setWriter(Factory.Tree);
			IConstructor lastAlternativeSubList = null;
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor alternativeSubList = vf.constructor(Factory.Tree_Appl, production, vf.list(gatheredPrefixes.getFirst(i)));
				alternativeSubList = actionExecutor.filterAppl(alternativeSubList);
				if(alternativeSubList != null){
					lastAlternativeSubList = alternativeSubList;
					ambSublist.insert(alternativeSubList);
				}
			}
			
			int nrOfAmbSubLists = ambSublist.size();
			if(nrOfAmbSubLists == 1){ // Filtered and no longer ambiguous; flatten it.
				int nrOfChildren = lastAlternativeSubList.arity();
				IConstructor[] children = new IConstructor[nrOfChildren];
				for(int i = nrOfChildren - 1; i >= 0; --i){
					children[i] = (IConstructor) lastAlternativeSubList.get(i);
				}
				
				int length = postFix.length;
				IConstructor[] newPostFix = new IConstructor[nrOfChildren + length];
				System.arraycopy(children, 0, newPostFix, 0, nrOfChildren);
				System.arraycopy(postFix, 0, newPostFix, nrOfChildren, length);
				
				gatheredAlternatives.add(newPostFix, production);
				
				sharedPrefixCache.put(prefixes, children);
			}else if(nrOfAmbSubLists > 1){ // Ambiguous after filtering.
				IConstructor prefixResult = vf.constructor(Factory.Tree_Amb, ambSublist.done());
				
				int length = postFix.length;
				IConstructor[] newPostFix = new IConstructor[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = prefixResult;
				
				gatheredAlternatives.add(newPostFix, production);
				
				sharedPrefixCache.put(prefixes, new IConstructor[]{prefixResult});
			}
			// Filtering caused it to be discarded.
		}
	}
	
	private IConstructor buildCycle(IConstructor[] cycleElements, IConstructor production, IActionExecutor actionExecutor){
		IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(production), vf.integer(1));
		cycle = actionExecutor.filterAppl(cycle);
		if(cycle == null){
			return null;
		}
		
		IConstructor elements = vf.constructor(Factory.Tree_Appl, production, vf.list(cycleElements));
		elements = actionExecutor.filterAppl(elements);
		if(elements == null){
			return null;
		}
		
		return vf.constructor(Factory.Tree_Amb, vf.set(elements, cycle));
	}
	
	private IConstructor[] buildCycle(IConstructor[] cycleElements, IConstructor[] postFix, IConstructor result, IConstructor production, IActionExecutor actionExecutor){
		int length = postFix.length;
		IConstructor[] newPostFix;
		IConstructor cycleNode = buildCycle(cycleElements, production, actionExecutor);
		if(cycleElements.length == 1){
			if(cycleNode != null){
				newPostFix = new IConstructor[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = cycleNode;
			}else{
				int nrOfCycleElements = cycleElements.length;
				newPostFix = new IConstructor[nrOfCycleElements + length];
				System.arraycopy(cycleElements, 0, newPostFix, 0, nrOfCycleElements);
				System.arraycopy(postFix, 0, newPostFix, nrOfCycleElements, length);
			}
		}else{
			if(cycleNode != null){
				newPostFix = new IConstructor[length + 2];
				System.arraycopy(postFix, 0, newPostFix, 2, length);
				newPostFix[1] = cycleNode;
				newPostFix[0] = result;
			}else{
				int nrOfCycleElements = cycleElements.length;
				newPostFix = new IConstructor[nrOfCycleElements + length + 1];
				System.arraycopy(cycleElements, 0, newPostFix, 1, nrOfCycleElements);
				System.arraycopy(postFix, 0, newPostFix, nrOfCycleElements + 1, length);
				newPostFix[0] = result;
			}
		}
		return newPostFix;
	}
	
	private IConstructor[] gatherCycle(Link child, IConstructor[] postFix, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ArrayList<AbstractNode> blackList, FilteringTracker filteringTracker, IActionExecutor actionExecutor){
		AbstractNode originNode = child.node;
		
		blackList.add(originNode);
		
		OUTER : do{
			ArrayList<Link> prefixes = child.prefixes;
			if(prefixes == null){
				return null;
			}
			
			int nrOfPrefixes = prefixes.size();
			
			for(int i = nrOfPrefixes - 1; i >= 0; --i){
				Link prefix = prefixes.get(i);
				if(prefix == null) continue;
				AbstractNode prefixNode = prefix.node;
				
				if(prefixNode == originNode){
					return postFix;
				}
				
				if(prefixNode.isEmpty()){
					int length = postFix.length;
					IConstructor[] newPostFix = new IConstructor[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore, filteringTracker, actionExecutor);
					if(result == null) continue; // Rejected.
					newPostFix[0] = result;
					
					child = prefix;
					postFix = newPostFix;
					continue OUTER;
				}
			}
			break;
		}while(true);
		
		return null;
	}
}
