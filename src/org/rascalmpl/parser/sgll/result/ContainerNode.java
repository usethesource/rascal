package org.rascalmpl.parser.sgll.result;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleArrayList;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.parser.sgll.util.Stack;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;

public class ContainerNode extends AbstractNode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private final URI input;
	private final int offset;
	private final int length;
	
	private final boolean isListContainer;
	
	private boolean rejected;

	private Link firstAlternative;
	private IConstructor firstProduction;
	private ArrayList<Link> alternatives;
	private ArrayList<IConstructor> productions;
	
	private IConstructor cachedResult;
	
	public ContainerNode(URI input, int offset, int length, boolean isListContainer){
		super();
		
		this.input = input;
		this.offset = offset;
		this.length = length;
		
		this.isListContainer = isListContainer;
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
	
	private void gatherAlternatives(Link child, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark){
		AbstractNode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			IValue result = resultNode.toTerm(stack, depth, cycleMark);
			if(result == null) return; // Rejected.
			
			IValue[] postFix = new IValue[]{result};
			gatherProduction(child, postFix, gatheredAlternatives, production, stack, depth, cycleMark);
		}else{
			gatheredAlternatives.add(new IValue[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, IValue[] postFix, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			AbstractNode resultNode = prefix.node;
			if(!resultNode.isRejected()){
				IValue result = resultNode.toTerm(stack, depth, cycleMark);
				if(result == null) return; // Rejected.
				
				int length = postFix.length;
				IValue[] newPostFix = new IValue[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = result;
				gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark);
			}
		}
	}
	
	private void gatherListAlternatives(Link child, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark){
		AbstractNode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			IValue result = resultNode.toTerm(stack, depth, cycleMark);
			if(result == null) return; // Rejected.
			
			IndexedStack<AbstractNode> listElementStack = new IndexedStack<AbstractNode>();
			
			if(resultNode.isContainer()) listElementStack.push(resultNode, 0);
			gatherList(child, new IValue[]{result}, gatheredAlternatives, production, stack, depth, cycleMark, listElementStack, 1, new Stack<AbstractNode>());
			if(resultNode.isContainer()) listElementStack.dirtyPurge();
		}else{
			gatheredAlternatives.add(new IValue[]{}, production);
		}
	}
	
	private void gatherList(Link child, IValue[] postFix, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark, IndexedStack<AbstractNode> listElementStack, int elementNr, Stack<AbstractNode> blackList){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; --i){
			Link prefix = prefixes.get(i);
			
			if(prefix == null){
				gatheredAlternatives.add(postFix, production);
				continue;
			}
			
			AbstractNode prefixNode = prefix.node;
			
			if(!prefixNode.isRejected()){
				if(blackList.contains(prefixNode)) continue;
				
				int index = listElementStack.contains(prefixNode);
				if(index != -1){
					int length = postFix.length;
					int repeatLength = elementNr - index;
					
					IValue[] newPostFix = new IValue[length - repeatLength + 1];
					System.arraycopy(postFix, repeatLength, newPostFix, 1, length - repeatLength);
					
					IListWriter subList = vf.listWriter(Factory.Tree);
					for(int j = repeatLength - 1; j >= 0; --j){
						subList.insert(postFix[j]);
					}
					
					ISetWriter cycleChildren = vf.setWriter(Factory.Tree);
					IConstructor subListNode = vf.constructor(Factory.Tree_Appl, production, subList.done());
					subListNode = subListNode.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, length, -1, -1, -1, -1));
					cycleChildren.insert(subListNode);
					IConstructor cycleNode = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(production), vf.integer(1));
					cycleNode = cycleNode.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, length, -1, -1, -1, -1));
					cycleChildren.insert(cycleNode);
					IConstructor ambSubListNode = vf.constructor(Factory.Tree_Amb, cycleChildren.done());
					newPostFix[0] = ambSubListNode;
					
					blackList.push(prefixNode);
					gatherList(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, listElementStack, elementNr + 1, blackList);
					blackList.pop();
				}else{
					int length = postFix.length;
					IValue[] newPostFix = new IValue[length + 1];
					System.arraycopy(postFix, 0, newPostFix, 1, length);
					
					if(prefixNode.isContainer()) listElementStack.push(prefixNode, elementNr);
					
					IValue result = prefixNode.toTerm(stack, depth, cycleMark);
					if(result == null) return; // Rejected.
					
					newPostFix[0] = result;
					gatherList(prefix, newPostFix, gatheredAlternatives, production, stack, depth, cycleMark, listElementStack, elementNr + 1, blackList);
					
					if(prefixNode.isContainer()) listElementStack.dirtyPurge();
				}
			}
		}
	}
	
	private IConstructor buildAlternative(IConstructor production, IValue[] children){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; --i){
			childrenListWriter.insert(children[i]);
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IValue toTerm(IndexedStack<AbstractNode> stack, int depth, CycleMark cycleMark){
		if(cachedResult != null && (depth <= cycleMark.depth)){
			if(depth == cycleMark.depth){
				cycleMark.reset();
			}
			return cachedResult;
		}
		
		if(rejected) return null;
		
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			IConstructor cycle = vf.constructor(Factory.Tree_Cycle, ProductionAdapter.getRhs(firstProduction), vf.integer(depth - index));
			if(input != null) cycle = cycle.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, length, -1, -1, -1, -1));
			
			cycleMark.setMark(index);
			
			return cycle;
		}
		
		int childDepth = depth + 1;
		
		stack.push(this, depth); // Push.
		
		// Gather
		DoubleArrayList<IValue[], IConstructor> gatheredAlternatives = new DoubleArrayList<IValue[], IConstructor>();
		
		if(!isListContainer){
			gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth, cycleMark);
			if(alternatives != null){
				for(int i = alternatives.size() - 1; i >= 0; --i){
					gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark);
				}
			}
		}else{
			gatherListAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth, cycleMark);
			if(alternatives != null){
				for(int i = alternatives.size() - 1; i >= 0; --i){
					gatherListAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth, cycleMark);
				}
			}
		}
		
		// Output.
		IConstructor result;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative);
			if(input != null) result = result.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, length, -1, -1, -1, -1));
		}else if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else{ // Ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; --i){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				
				IConstructor alt = buildAlternative(production, alternative);
				if(input != null) alt = alt.setAnnotation(Factory.Location, vf.sourceLocation(input, offset, length, -1, -1, -1, -1));
				ambSetWriter.insert(alt);
			}
			
			result = vf.constructor(Factory.Tree_Amb, ambSetWriter.done());
		}
		
		stack.dirtyPurge(); // Pop.
		
		return (depth <= cycleMark.depth) ? (cachedResult = result) : result;
	}
}
