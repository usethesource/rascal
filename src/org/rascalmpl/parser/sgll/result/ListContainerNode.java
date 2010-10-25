package org.rascalmpl.parser.sgll.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleArrayList;
import org.rascalmpl.parser.sgll.util.HashMap;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ListContainerNode extends AbstractContainerNode{
	private IConstructor cachedResult;
	
	public ListContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator, boolean isLayout){
		super(input, offset, endOffset, isNullable, isSeparator, isLayout);
	}
	
	private void gatherAlternatives(Link child, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor> sharedPrefixCache, PositionStore positionStore){
		AbstractNode childNode = child.node;
		
		if(!(childNode.isEpsilon() && child.prefixes == null)){
			IConstructor result = childNode.toTerm(stack, depth, cycleMark, positionStore);
			if(result == null) return; // Rejected.
			
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isEmpty()){
				IConstructor[] cycle = gatherCycle(child, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList);
				if(cycle != null){
					if(cycle.length == 1){
						gatherProduction(child, new IConstructor[]{buildCycle(cycle, production)}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
					}else{
						gatherProduction(child, new IConstructor[]{result, buildCycle(cycle, production)}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
					}
					return;
				}
			}
			gatherProduction(child, new IConstructor[]{result}, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
		}else{
			gatheredAlternatives.add(new IConstructor[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, IConstructor[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList){
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
				
				IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore);
				if(result == null) return; // Rejected.
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					IConstructor[] cycle = gatherCycle(prefix, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList);
					if(cycle != null){
						IConstructor[] newPostFix = buildCycle(cycle, postFix, result, production);
						
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
			
			gatherAmbiguousProduction(prefixes, postFix, gatheredAlternatives, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
			
			break;
		}while(true);
	}
	
	private void gatherAmbiguousProduction(ArrayList<Link> prefixes, IConstructor[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, HashMap<ArrayList<Link>, IConstructor> sharedPrefixCache, PositionStore positionStore, ArrayList<AbstractNode> blackList){
		IConstructor prefixResult = sharedPrefixCache.get(prefixes);
		if(prefixResult != null){
			int length = postFix.length;
			IConstructor[] newPostFix = new IConstructor[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = prefixResult;
			
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
				
				IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore);
				if(result == null) return; // Rejected.
				
				if(prefixNode.isEmpty() && !prefixNode.isSeparator()){ // Possibly a cycle.
					IConstructor[] cycle = gatherCycle(prefix, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList);
					if(cycle != null){
						IConstructor[] newPostFix = buildCycle(cycle, postFix, result, production);
						
						gatherProduction(prefix, newPostFix, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
						continue;
					}
				}
				
				gatherProduction(prefix, new IConstructor[]{result}, gatheredPrefixes, production, stack, depth, cycleMark, sharedPrefixCache, positionStore, blackList);
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
			
			for(int i = nrOfGatheredPrefixes - 1; i >= 0; --i){
				IConstructor alternativeSubList = vf.constructor(Factory.Tree_Appl, production, vf.list(gatheredPrefixes.getFirst(i)));
				ambSublist.insert(alternativeSubList);
			}
			
			prefixResult = vf.constructor(Factory.Tree_Amb, ambSublist.done());
			sharedPrefixCache.put(prefixes, prefixResult);
			
			int length = postFix.length;
			IConstructor[] newPostFix = new IConstructor[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = prefixResult;
			
			gatheredAlternatives.add(newPostFix, production);
		}
	}
	
	private IConstructor buildCycle(IConstructor[] cycleElements, IConstructor production){
		IConstructor elements = vf.constructor(Factory.Tree_Appl, production, vf.list(cycleElements));
		IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(production), vf.integer(1));
		return vf.constructor(Factory.Tree_Amb, vf.set(elements, cycle));
	}
	
	private IConstructor[] buildCycle(IConstructor[] cycle, IConstructor[] postFix, IConstructor result, IConstructor production){
		int length = postFix.length;
		IConstructor[] newPostFix;
		if(cycle.length == 1){
			newPostFix = new IConstructor[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = buildCycle(cycle, production);
		}else{
			newPostFix = new IConstructor[length + 2];
			System.arraycopy(postFix, 0, newPostFix, 2, length);
			newPostFix[1] = buildCycle(cycle, production);
			newPostFix[0] = result;
		}
		return newPostFix;
	}
	
	private IConstructor[] gatherCycle(Link child, IConstructor[] postFix, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ArrayList<AbstractNode> blackList){
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
					IConstructor result = prefixNode.toTerm(stack, depth, cycleMark, positionStore);
					if(result == null) return null; // Rejected.
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
	
	private IConstructor buildAlternative(IConstructor production, IValue[] children){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; --i){
			childrenListWriter.insert(children[i]);
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IConstructor toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		if(cachedResult != null && (depth <= cycleMark.depth)){
			if(depth == cycleMark.depth){
				cycleMark.reset();
			}
			return cachedResult;
		}
		
		if(rejected) return null;
		
		ISourceLocation sourceLocation = null;
		if(!(isLayout || input == null)){
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = vf.sourceLocation(input, offset, endOffset - offset, beginLine + 1, endLine + 1, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(firstProduction), vf.integer(depth - index));
			if(sourceLocation != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(this, depth); // Push.
		
		// Gather
		HashMap<ArrayList<Link>, IConstructor> sharedPrefixCache = new HashMap<ArrayList<Link>, IConstructor>();
		DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives = new DoubleArrayList<IConstructor[], IConstructor>();
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth, cycleMark, sharedPrefixCache, positionStore);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, sharedPrefixCache, positionStore);
			}
		}
		
		// Output.
		IConstructor result;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative);
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}else if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else{ // Ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				
				IConstructor alt = buildAlternative(production, alternative);
				if(sourceLocation != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
				ambSetWriter.insert(alt);
			}
			
			result = vf.constructor(Factory.Tree_Amb, ambSetWriter.done());
			if(sourceLocation != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}
		
		stack.dirtyPurge(); // Pop.
		
		return (depth <= cycleMark.depth) ? (cachedResult = result) : result;
	}
}
