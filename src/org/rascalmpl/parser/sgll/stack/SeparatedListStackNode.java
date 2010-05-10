package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class SeparatedListStackNode extends StackNode{
	private final IConstructor symbol;

	private final StackNode child;
	private final StackNode[] separators;
	private final boolean isPlusList;
	
	private boolean marked;
	
	private final INode result;
	
	public SeparatedListStackNode(int id, IConstructor symbol, StackNode child, StackNode[] separators, boolean isPlusList){
		super(id);
		
		this.symbol = symbol;
		
		this.child = child;
		this.separators = separators;
		this.isPlusList = isPlusList;
		
		this.result = null;
	}
	
	public SeparatedListStackNode(int id, IConstructor symbol, StackNode child, StackNode[] separators, boolean isPlusList, INode result){
		super(id);
		
		this.symbol = symbol;
		
		this.child = child;
		this.separators = separators;
		this.isPlusList = isPlusList;
		
		this.result = result;
	}
	
	public SeparatedListStackNode(SeparatedListStackNode separatedListStackNode){
		super(separatedListStackNode);
		
		symbol = separatedListStackNode.symbol;

		child = separatedListStackNode.child;
		separators = separatedListStackNode.separators;
		isPlusList = separatedListStackNode.isPlusList;
		
		result = new ContainerNode();
	}
	
	public boolean isReducable(){
		return false;
	}
	
	public boolean isList(){
		return true;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public StackNode getCleanCopy(){
		return new SeparatedListStackNode(this);
	}
	
	public StackNode getCleanCopyWithPrefix(){
		SeparatedListStackNode slpsn = new SeparatedListStackNode(this);
		slpsn.prefixes = prefixes;
		slpsn.prefixStartLocations = prefixStartLocations;
		return slpsn;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public void mark(){
		marked = true;
	}
	
	public boolean isMarked(){
		return marked;
	}
	
	public StackNode[] getChildren(){
		StackNode psn = child.getCleanCopy();
		StackNode cpsn = child.getCleanCopy();
		SeparatedListStackNode slpsn = new SeparatedListStackNode((id | IGLL.LIST_LIST_FLAG), symbol, child, separators, true, new ContainerNode());
		
		StackNode from = slpsn;
		for(int i = 0; i < separators.length; i++){
			StackNode to = separators[i];
			from.addNext(to);
			from = to;
		}
		from.addNext(psn);
		psn.addEdge(slpsn);
		psn.addEdge(this);
		
		cpsn.addEdge(slpsn);
		cpsn.addEdge(this);
		
		psn.setStartLocation(-1); // Reset.
		slpsn.setStartLocation(startLocation);
		cpsn.setStartLocation(startLocation);
		
		if(isPlusList){
			return new StackNode[]{cpsn};
		}
		
		EpsilonStackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		epsn.addEdge(this);
		epsn.setParentProduction(symbol);
		
		return new StackNode[]{cpsn, epsn};
	}
	
	public void addResult(IConstructor production, INode[] children){
		result.addAlternative(production, children);
	}
	
	public INode getResult(){
		return result;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(symbol);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
