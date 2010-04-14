package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class ListStackNode extends StackNode{
	private final String production;

	private final StackNode child;
	private final boolean isPlusList;
	
	private boolean marked;
	
	private final INode result;
	
	public ListStackNode(int id, String production, StackNode child, boolean isPlusList){
		super(id);
		
		this.production = production;
		
		this.child = child;
		this.isPlusList = isPlusList;
		
		this.result = null;
	}
	
	public ListStackNode(int id, String production, StackNode child, boolean isPlusList, INode result){
		super(id);
		
		this.production = production;
		
		this.child = child;
		this.isPlusList = isPlusList;
		
		this.result = result;
	}
	
	private ListStackNode(ListStackNode listParseStackNode){
		super(listParseStackNode);
		
		production = listParseStackNode.production;

		child = listParseStackNode.child;
		isPlusList = listParseStackNode.isPlusList;
		
		result = new ContainerNode(production);
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
		return new ListStackNode(this);
	}
	
	public StackNode getCleanCopyWithPrefix(){
		ListStackNode lpsn = new ListStackNode(this);
		lpsn.prefixes = prefixes;
		lpsn.prefixStartLocations = prefixStartLocations;
		return lpsn;
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
		ListStackNode lpsn = new ListStackNode((id | IGLL.LIST_LIST_FLAG), production, child, true, new ContainerNode(production));
		
		lpsn.addNext(psn);
		psn.addEdge(lpsn);
		psn.addEdge(this);
		
		cpsn.addEdge(lpsn);
		cpsn.addEdge(this);
		
		psn.setStartLocation(-1);
		lpsn.setStartLocation(startLocation);
		cpsn.setStartLocation(startLocation);
		
		if(isPlusList){
			return new StackNode[]{cpsn};
		}
		
		EpsilonStackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		epsn.addEdge(this);
		
		return new StackNode[]{cpsn, epsn};
	}
	
	public void addResult(INode[] children){
		result.addAlternative(children);
	}
	
	public INode getResult(){
		return result;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(production);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
