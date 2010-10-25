package org.rascalmpl.parser.sgll.stack;

import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.util.specific.PositionStore;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.SymbolAdapter;

public final class SeparatedListStackNode extends AbstractStackNode implements IListStackNode{
	private final static EpsilonStackNode EMPTY = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID, 0);
	
	private final IConstructor production;
	private final String name;

	private final AbstractStackNode[] children;
	
	public SeparatedListStackNode(int id, int dot, IConstructor production, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList){
		super(id, dot);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(child, separators, isPlusList);
	}
	
	public SeparatedListStackNode(int id, int dot, IConstructor production, IMatchableStackNode[] followRestrictions, AbstractStackNode child, AbstractStackNode[] separators, boolean isPlusList){
		super(id, dot, followRestrictions);
		
		this.production = production;
		this.name = SymbolAdapter.toString(ProductionAdapter.getRhs(production))+id; // Add the id to make it unique.
		
		this.children = generateChildren(child, separators, isPlusList);
	}
	
	private SeparatedListStackNode(SeparatedListStackNode original){
		super(original);
		
		production = original.production;
		name = original.name;
		
		children = original.children;
	}
	
	private AbstractStackNode[] generateChildren(AbstractStackNode child,  AbstractStackNode[] separators, boolean isPlusList){
		AbstractStackNode listNode = child.getCleanCopy();
		listNode.markAsEndNode();
		listNode.setParentProduction(production);
		
		int numberOfSeparators = separators.length;
		AbstractStackNode[] prod = new AbstractStackNode[numberOfSeparators + 2];
		
		listNode.setNext(prod);
		prod[0] = listNode; // Start
		for(int i = numberOfSeparators - 1; i >= 0; --i){
			AbstractStackNode separator = separators[i];
			separator.setNext(prod);
			separator.markAsSeparator();
			prod[i + 1] = separator;
		}
		prod[numberOfSeparators + 1] = listNode; // End
		
		if(isPlusList){
			return new AbstractStackNode[]{listNode};
		}

		AbstractStackNode empty = EMPTY.getCleanCopy();
		empty.markAsEndNode();
		empty.setParentProduction(production);
		
		return new AbstractStackNode[]{listNode, empty};
	}
	
	public String getName(){
		return name;
	}
	
	public void setPositionStore(PositionStore positionStore){
		throw new UnsupportedOperationException();
	}
	
	public boolean match(URI inputURI, char[] input){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(){
		return new SeparatedListStackNode(this);
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		return children;
	}
	
	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
