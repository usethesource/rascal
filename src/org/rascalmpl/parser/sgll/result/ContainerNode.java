package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.DoubleArrayList;
import org.rascalmpl.parser.sgll.util.IndexedStack;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class ContainerNode implements INode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	private Link firstAlternative;
	private IConstructor firstProduction;
	private ArrayList<Link> alternatives;
	private ArrayList<IConstructor> productions;
	
	private boolean rejected;
	
	public ContainerNode(){
		super();
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
	
	private void gatherAlternatives(Link child, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<INode> stack, int depth){
		INode resultNode = child.node;
		
		if(!(resultNode.isEpsilon() && child.prefixes == null)){
			IValue result = resultNode.toTerm(stack, depth);
			if(result == null) return; // Rejected.
			
			IValue[] postFix = new IValue[]{result};
			gatherProduction(child, postFix, gatheredAlternatives, production, stack, depth);
		}else{
			gatheredAlternatives.add(new IValue[]{}, production);
		}
	}
	
	private void gatherProduction(Link child, IValue[] postFix, DoubleArrayList<IValue[], IConstructor> gatheredAlternatives, IConstructor production, IndexedStack<INode> stack, int depth){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; i--){
			Link prefix = prefixes.get(i);
			
			INode resultNode = prefix.node;
			if(!resultNode.isRejected()){
				IValue result = resultNode.toTerm(stack, depth);
				if(result == null) return; // Rejected.
				
				int length = postFix.length;
				IValue[] newPostFix = new IValue[length + 1];
				System.arraycopy(postFix, 0, newPostFix, 1, length);
				newPostFix[0] = result;
				gatherProduction(prefix, newPostFix, gatheredAlternatives, production, stack, depth);
			}
		}
	}
	
	private IValue buildAlternative(IConstructor production, IValue[] children){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; i--){
			childrenListWriter.insert(children[i]);
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IValue toTerm(IndexedStack<INode> stack, int depth){
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			return vf.constructor(Factory.Tree_Cycle, firstProduction.get("rhs"), vf.integer(depth - index));
		}
		
		int childDepth = depth + 1;
		
		stack.push(this, depth); // Push.
		
		// Gather
		DoubleArrayList<IValue[], IConstructor> gatheredAlternatives = new DoubleArrayList<IValue[], IConstructor>();
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction, stack, childDepth);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; i--){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i), stack, childDepth);
			}
		}
		
		// Output.
		IValue result;
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			IValue[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative);
		}else if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else{ // Ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; i--){
				IConstructor production = gatheredAlternatives.getSecond(i);
				IValue[] alternative = gatheredAlternatives.getFirst(i);
				ambSetWriter.insert(buildAlternative(production, alternative));
			}
			
			result = vf.constructor(Factory.Tree_Amb, ambSetWriter.done());
		}
		
		stack.purge(); // Pop.
		
		return result;
	}
}
