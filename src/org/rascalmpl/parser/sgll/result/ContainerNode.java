package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
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
	
	private void gatherAlternatives(Link child, DoubleArrayList<INode[], IConstructor> gatheredAlternatives, IConstructor production){
		gatherProduction(child, new INode[]{child.node}, gatheredAlternatives, production);
	}
	
	private void gatherProduction(Link child, INode[] postFix, DoubleArrayList<INode[], IConstructor> gatheredAlternatives, IConstructor production){
		ArrayList<Link> prefixes = child.prefixes;
		if(prefixes == null){
			gatheredAlternatives.add(postFix, production);
			return;
		}
		
		for(int i = prefixes.size() - 1; i >= 0; i--){
			Link prefix = prefixes.get(i);
			
			int length = postFix.length;
			INode[] newPostFix = new INode[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			INode resultNode = prefix.node;
			if(!resultNode.isRejected()){
				newPostFix[0] = resultNode;
				gatherProduction(prefix, newPostFix, gatheredAlternatives, production);
			}
		}
	}
	
	private IValue buildAlternative(IConstructor production, INode[] children, IndexedStack<INode> stack, int depth){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; i--){
			IValue childTerm = children[i].toTerm(stack, depth);
			if(childTerm == null) return null;
			
			childrenListWriter.insert(childTerm);
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IValue toTerm(IndexedStack<INode> stack, int depth){
		int index = stack.contains(this);
		if(index != -1){ // Cycle found.
			return vf.constructor(Factory.Tree_Cycle, firstProduction.get("rhs"), vf.integer(depth - index));
		}
		
		int childDepth = depth + 1;
		IValue result;
		
		stack.push(this, depth); // Push.
		
		// Gather
		DoubleArrayList<INode[], IConstructor> gatheredAlternatives = new DoubleArrayList<INode[], IConstructor>();
		gatherAlternatives(firstAlternative, gatheredAlternatives, firstProduction);
		if(alternatives != null){
			for(int i = alternatives.size() - 1; i >= 0; i--){
				gatherAlternatives(alternatives.get(i), gatheredAlternatives, productions.get(i));
			}
		}
		
		// Output.
		
		int nrOfAlternatives = gatheredAlternatives.size();
		if(nrOfAlternatives == 1){ // Not ambiguous.
			IConstructor production = gatheredAlternatives.getSecond(0);
			INode[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative, stack, childDepth);
		}else if(nrOfAlternatives == 0){ // Filtered.
			result = null;
		}else{ // Possibly ambiguous.
			ISetWriter ambSetWriter = vf.setWriter(Factory.Tree);
			
			// Construct the alternatives.
			for(int i = nrOfAlternatives - 1; i >= 0; i--){
				IConstructor production = gatheredAlternatives.getSecond(0);
				INode[] alternative = gatheredAlternatives.getFirst(0);
				IValue alternativeTerm = buildAlternative(production, alternative, stack, childDepth);
				if(alternativeTerm == null) continue;
				
				ambSetWriter.insert(alternativeTerm);
			}
			
			// Construct the result, based on the number of alternatives.
			ISet ambCluster = ambSetWriter.done();
			int nrOfAmbiguities = ambCluster.size();
			if(nrOfAmbiguities == 1){ // Not ambiguous.
				result = ambCluster.iterator().next();
			}else if(nrOfAmbiguities == 0){ // Filtered.
				result = null;
			}else{ // Ambiguous.
				result = vf.constructor(Factory.Tree_Amb, ambCluster);
			}
		}
		
		stack.purge(); // Pop.
		
		return result;
	}
}
