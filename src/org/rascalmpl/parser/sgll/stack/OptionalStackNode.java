package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class OptionalStackNode extends AbstractStackNode implements IListStackNode{
	private final IConstructor symbol;
	
	private final AbstractStackNode optional;
	
	private boolean marked;
	
	private final INode result;
	
	public OptionalStackNode(int id, IConstructor symbol, AbstractStackNode optional){
		super(id);
		
		this.symbol = symbol;
		
		this.optional = optional;
		
		this.result = null;
	}
	
	private OptionalStackNode(OptionalStackNode optionalParseStackNode){
		super(optionalParseStackNode);
		
		symbol = optionalParseStackNode.symbol;
		
		optional = optionalParseStackNode.optional;
		
		result = new ContainerNode();
	}
	
	public void mark(){
		marked = true;
	}
	
	public boolean isMarked(){
		return marked;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public boolean reduce(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode getCleanCopy(){
		return new OptionalStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		OptionalStackNode opsn = new OptionalStackNode(this);
		opsn.prefixes = prefixes;
		opsn.prefixStartLocations = prefixStartLocations;
		return opsn;
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
}
