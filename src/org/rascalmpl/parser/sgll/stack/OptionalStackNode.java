package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.AbstractContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public final class OptionalStackNode extends AbstractStackNode implements IListStackNode{
	private final static EpsilonStackNode EMPTY = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
	
	private final IConstructor production;
	private final String name;
	
	private final AbstractStackNode optional;
	
	private AbstractContainerNode result;
	
	public OptionalStackNode(int id, IConstructor production, AbstractStackNode optional){
		super(id);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.optional = optional;
	}
	
	public OptionalStackNode(int id, IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode optional){
		super(id, followRestrictions);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.optional = optional;
	}
	
	private OptionalStackNode(OptionalStackNode original){
		super(original);
		
		production = original.production;
		name = original.name;
		
		optional = original.optional;
	}
	
	private OptionalStackNode(OptionalStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		production = original.production;
		name = original.name;
		
		optional = original.optional;
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
		return new OptionalStackNode(this);
	}

	public AbstractStackNode getCleanCopyWithPrefix(){
		return new OptionalStackNode(this, prefixesMap);
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
		AbstractStackNode child = optional.getCleanCopy();
		child.markAsEndNode();
		child.setStartLocation(startLocation);
		child.setParentProduction(production);
		child.initEdges();
		child.addEdge(this);

		AbstractStackNode empty = EMPTY.getCleanCopy();
		empty.markAsEndNode();
		empty.setStartLocation(startLocation);
		empty.setParentProduction(production);
		empty.initEdges();
		empty.addEdge(this);
		
		return new AbstractStackNode[]{child, empty};
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
