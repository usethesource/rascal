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
	
	private IConstructor firstProduction;
	private Link firstAlternative;
	private ArrayList<IConstructor> productions;
	private ArrayList<Link> alternatives;
	
	private boolean rejected;
	
	public ContainerNode(){
		super();
	}
	
	public void addAlternative(IConstructor production, Link children){
		if(firstAlternative == null){
			firstProduction = production;
			firstAlternative = children;
		}else{
			if(alternatives == null){
				productions = new ArrayList<IConstructor>(1);
				alternatives = new ArrayList<Link>(1);
			}
			productions.add(production);
			alternatives.add(children);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	public void setRejected(){
		rejected = true;
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
		
		int nrOfPrefixes = prefixes.size();
		if(nrOfPrefixes == 1){
			Link prefix = prefixes.get(0);
			
			int length = postFix.length;
			INode[] newPostFix = new INode[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = prefix.node;
			gatherProduction(prefix, newPostFix, gatheredAlternatives, production);
			return;
		}
		
		for(int i = nrOfPrefixes - 1; i >= 0; i--){
			Link prefix = prefixes.get(i);
			
			int length = postFix.length;
			INode[] newPostFix = new INode[length + 1];
			System.arraycopy(postFix, 0, newPostFix, 1, length);
			newPostFix[0] = prefix.node;
			gatherProduction(child.prefixes.get(i), newPostFix, gatheredAlternatives, production);
		}
	}
	
	private IValue buildAlternative(IConstructor production, INode[] children, IndexedStack<INode> stack, int depth){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; i--){
			childrenListWriter.insert(children[i].toTerm(stack, depth));
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IValue toTerm(IndexedStack<INode> stack, int depth){
		if(rejected){
			return vf.constructor(Factory.Tree_Rejected);
		}
		
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
		if(nrOfAlternatives == 1){
			IConstructor production = gatheredAlternatives.getSecond(0);
			INode[] alternative = gatheredAlternatives.getFirst(0);
			result = buildAlternative(production, alternative, stack, childDepth);
		}else{
			ISetWriter ambListWriter = vf.setWriter(Factory.Tree);
			
			for(int i = nrOfAlternatives - 1; i >= 0; i--){
				IConstructor production = gatheredAlternatives.getSecond(0);
				INode[] alternative = gatheredAlternatives.getFirst(0);
				ambListWriter.insert(buildAlternative(production, alternative, stack, childDepth));
			}
			
			result = vf.constructor(Factory.Tree_Amb, ambListWriter.done());
		}
		
		stack.purge(); // Pop.
		
		return result;
	}
}
