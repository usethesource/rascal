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
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ListContainerNode extends AbstractNode{
	private final URI input;
	private final int offset;
	private final int endOffset;
	
	private boolean rejected;
	
	private final boolean isNullable;
	private final boolean isSeparator;
	
	private Link firstAlternative;
	private IConstructor firstProduction;
	private ArrayList<Link> alternatives;
	private ArrayList<IConstructor> productions;
	
	private IConstructor cachedResult;
	
	public ListContainerNode(URI input, int offset, int endOffset, boolean isNullable, boolean isSeparator){
		super();
		
		this.input = input;
		this.offset = offset;
		this.endOffset = endOffset;
		
		this.isNullable = isNullable;
		this.isSeparator = isSeparator;
	}
	
	public void addAlternative(IConstructor production, Link children){
		if(firstAlternative == null){
			firstAlternative = children;
			firstProduction = production;
		}else{
			if(alternatives == null){
				alternatives = new ArrayList<Link>(1);
				productions = new ArrayList<IConstructor>(1);
			}
			alternatives.add(children);
			productions.add(production);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public boolean isNullable(){
		return isNullable;
	}
	
	public boolean isSeparator(){
		return isSeparator;
	}
	
	public void setRejected(){
		rejected = true;
		
		// Clean up.
		firstAlternative = null;
		alternatives = null;
		productions = null;
	}
	
	public boolean isRejected(){
		return rejected;
	}
	
	private void gatherAlternatives(Link child, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore){
		AbstractNode childNode = child.node;
		
		if(!(childNode.isEpsilon() && child.prefixes == null)){
		
			IConstructor result = childNode.toTerm(stack, depth, cycleMark, positionStore);
			if(result == null) return; // Rejected.
			
			ArrayList<AbstractNode> blackList = new ArrayList<AbstractNode>();
			if(childNode.isNullable()){
				IConstructor[] cycle = gatherCycle(child, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList);
				if(cycle != null){
					StringBuilder buffer = new StringBuilder();
					buffer.append("repeat(");
					buffer.append(cycle[0]);
					int cycleLength = cycle.length;
					for(int j = 1; j < cycleLength; ++j){
						buffer.append(',');
						buffer.append(cycle[j]);
					}
					buffer.append(')');
					
					if(cycleLength == 1){
						gatherProduction(child, new IConstructor[]{buildCycle(cycle, production)}, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, blackList);
					}else{
						gatherProduction(child, new IConstructor[]{result, buildCycle(cycle, production)}, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, blackList);
					}
					return;
				}
			}
			gatherProduction(child, new IConstructor[]{result}, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, blackList);
		}else{
			gatheredAlternatives.add(new IConstructor[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, IConstructor[] postFix, DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, PositionStore positionStore, ArrayList<AbstractNode> blackList){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		int nrOfPrefixes = prefixes.size();
		
		for(int i = nrOfPrefixes - 1; i >= 0; --i){
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
				
				if(prefixNode.isNullable() && !prefixNode.isSeparator()){ // Possibly a cycle.
					IConstructor[] cycle = gatherCycle(prefix, new IConstructor[]{result}, stack, depth, cycleMark, positionStore, blackList);
					if(cycle != null){
						StringBuilder buffer = new StringBuilder();
						buffer.append("repeat(");
						buffer.append(cycle[0]);
						int cycleLength = cycle.length;
						for(int j = 1; j < cycleLength; ++j){
							buffer.append(',');
							buffer.append(cycle[j]);
						}
						buffer.append(')');

						int length = postFix.length;
						IConstructor[] newPostFix;
						if(cycleLength == 1){
							newPostFix = new IConstructor[length + 1];
							System.arraycopy(postFix, 0, newPostFix, 1, length);
							newPostFix[0] = buildCycle(cycle, production);
						}else{
							newPostFix = new IConstructor[length + 2];
							System.arraycopy(postFix, 0, newPostFix, 2, length);
							newPostFix[1] = buildCycle(cycle, production);
							newPostFix[0] = result;
						}
						
						gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, blackList);
						continue;
					}
				}
				
				int length = postFix.length;
				IConstructor[] newPostFix = new IConstructor[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = result;
				gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, positionStore, blackList);
			}
		}
	}
	
	private IConstructor buildCycle(IConstructor[] cycleElements, IConstructor production){
		IConstructor elements = vf.constructor(Factory.Tree_Appl, production, vf.list(cycleElements));
		IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(production), vf.integer(1));
		return vf.constructor(Factory.Tree_Amb, vf.set(elements, cycle));
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
				AbstractNode prefixNode = prefix.node;
				
				if(prefixNode == originNode){
					return postFix;
				}
				
				if(prefixNode.isNullable()){
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
		if(input != null){
			int beginLine = positionStore.findLine(offset);
			int endLine = positionStore.findLine(endOffset);
			sourceLocation = vf.sourceLocation(input, offset, endOffset - offset, beginLine, endLine, positionStore.getColumn(offset, beginLine), positionStore.getColumn(endOffset, endLine));
		}
		
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(firstProduction), vf.integer(depth - index));
			if(input != null) cycle = cycle.setAnnotation(Factory.Location, sourceLocation);
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(this, depth); // Push.
		
		// Gather
		DoubleArrayList<IConstructor[], IConstructor> gatheredAlternatives = new DoubleArrayList<IConstructor[], IConstructor>();
		
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth, cycleMark, positionStore);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; --i){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark, positionStore);
			}
		}
		
		// Output.
		IConstructor result;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative);
			if(input != null) result = result.setAnnotation(Factory.Location, sourceLocation);
		}else if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else{ // Ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				
				IConstructor alt = buildAlternative(production, alternative);
				if(input != null) alt = alt.setAnnotation(Factory.Location, sourceLocation);
				ambSetWriter.insert(alt);
			}
			
			result = vf.constructor(Factory.Tree_Amb, ambSetWriter.done());
		}
		
		stack.dirtyPurge(); // Pop.
		
		return (depth <= cycleMark.depth) ? (cachedResult = result) : result;
	}
}
