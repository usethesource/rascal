package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public final class ListStackNode extends AbstractStackNode implements IListStackNode{
	private final static EpsilonStackNode EMPTY = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
	
	private final IConstructor production;
	private final String name;

	private final AbstractStackNode child;
	private final boolean isPlusList;
	
	private AbstractContainerNode result;
	
	public ListStackNode(int id, IConstructor production, AbstractStackNode child, boolean isPlusList){
		super(id);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.child = child;
		this.isPlusList = isPlusList;
	}
	
	public ListStackNode(int id, IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode child, boolean isPlusList){
		super(id, followRestrictions);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.child = child;
		this.isPlusList = isPlusList;
	}
	
	private ListStackNode(ListStackNode original){
		super(original);
		
		production = original.production;
		name = original.name;

		child = original.child;
		isPlusList = original.isPlusList;
	}
	
	private ListStackNode(ListStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		production = original.production;
		name = original.name;

		child = original.child;
		isPlusList = original.isPlusList;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public boolean isClean(){
		return (result == null);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new ListStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new ListStackNode(this, prefixesMap);
	}
	
	public void setResultStore(AbstractContainerNode resultStore){
		result = resultStore;
	}
	
	public AbstractContainerNode getResultStore(){
		return result;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		AbstractStackNode listNode = child.getCleanCopy();
		listNode.markAsEndNode();
		listNode.setStartLocation(startLocation);
		listNode.setParentProduction(production);
		listNode.setNext(listNode);
		listNode.initEdges();
		listNode.addEdgeWithPrefix(this, null, startLocation);
		
		if(isPlusList){
			return new AbstractStackNode[]{listNode};
		}
		
		AbstractStackNode empty = EMPTY.getCleanCopy();
		empty.markAsEndNode();
		empty.setStartLocation(startLocation);
		empty.setParentProduction(production);
		empty.initEdges();
		empty.addEdge(this);
		
		return new AbstractStackNode[]{listNode, empty};
	}
	
	public AbstractNode getResult(){
		return result;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
