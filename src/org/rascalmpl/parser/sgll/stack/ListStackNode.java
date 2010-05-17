package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.IGLL;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class ListStackNode extends AbstractStackNode{
	private final IConstructor symbol;

	private final AbstractStackNode child;
	private final boolean isPlusList;
	
	private boolean marked;
	
	private final INode result;
	
	public ListStackNode(int id, IConstructor symbol, AbstractStackNode child, boolean isPlusList){
		super(id);
		
		this.symbol = symbol;
		
		this.child = child;
		this.isPlusList = isPlusList;
		
		this.result = null;
	}
	
	public ListStackNode(int id, IConstructor symbol, AbstractStackNode child, boolean isPlusList, INode result){
		super(id);
		
		this.symbol = symbol;
		
		this.child = child;
		this.isPlusList = isPlusList;
		
		this.result = result;
	}
	
	private ListStackNode(ListStackNode listParseStackNode){
		super(listParseStackNode);
		
		symbol = listParseStackNode.symbol;

		child = listParseStackNode.child;
		isPlusList = listParseStackNode.isPlusList;
		
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
	
	public AbstractStackNode getCleanCopy(){
		return new ListStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
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
	
	public AbstractStackNode[] getChildren(){
		AbstractStackNode psn = child.getCleanCopy();
		AbstractStackNode cpsn = child.getCleanCopy();
		ListStackNode lpsn = new ListStackNode((id | IGLL.LIST_LIST_FLAG), symbol, child, true, new ContainerNode());
		
		lpsn.addNext(psn);
		psn.addEdge(lpsn);
		psn.addEdge(this);
		psn.setParentProduction(symbol);
		
		cpsn.addEdge(lpsn);
		cpsn.addEdge(this);
		cpsn.setParentProduction(symbol);
		
		psn.setStartLocation(-1); // Reset
		lpsn.setStartLocation(startLocation);
		cpsn.setStartLocation(startLocation);
		
		if(isPlusList){
			return new AbstractStackNode[]{cpsn};
		}
		
		EpsilonStackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		epsn.addEdge(this);
		epsn.setStartLocation(startLocation);
		epsn.setParentProduction(symbol);
		
		return new AbstractStackNode[]{cpsn, epsn};
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
