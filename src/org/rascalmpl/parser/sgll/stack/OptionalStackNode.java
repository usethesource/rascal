package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IntegerList;

public final class OptionalStackNode extends AbstractStackNode implements IListStackNode{
	private final IConstructor symbol;
	
	private final AbstractStackNode optional;
	
	private ContainerNode result;
	
	public OptionalStackNode(int id, IConstructor symbol, AbstractStackNode optional){
		super(id);
		
		this.symbol = symbol;
		
		this.optional = optional;
	}
	
	private OptionalStackNode(OptionalStackNode original){
		super(original);
		
		symbol = original.symbol;
		
		optional = original.optional;
	}
	
	private OptionalStackNode(OptionalStackNode original, ArrayList<INode[]> prefixes, IntegerList prefixStartLocations){
		super(original, prefixes, prefixStartLocations);
		
		symbol = original.symbol;
		
		optional = original.optional;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public boolean isClean(){
		return (result == null);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new OptionalStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		return new OptionalStackNode(this, prefixes, prefixStartLocations);
	}
	
	public void initializeResultStore(){
		result = new ContainerNode();
	}
	
	public AbstractStackNode[] getChildren(){
		AbstractStackNode copy = optional.getCleanCopy();
		copy.setParentProduction(symbol);
		copy.setStartLocation(-1); // Reset.
		
		AbstractStackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		copy.addEdge(this);
		epsn.addEdge(this);
		epsn.setStartLocation(startLocation);
		epsn.setParentProduction(symbol);
		
		return new AbstractStackNode[]{copy, epsn};
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
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
