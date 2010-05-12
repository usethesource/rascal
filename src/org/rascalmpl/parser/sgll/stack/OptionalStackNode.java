package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class OptionalStackNode extends StackNode{
	private final IConstructor symbol;
	
	private final StackNode optional;
	
	private boolean marked;
	
	private final INode result;
	
	public OptionalStackNode(int id, IConstructor symbol, StackNode optional){
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
	
	public boolean isReducable(){
		return false;
	}
	
	public boolean isList(){
		return true;
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
	
	public StackNode getCleanCopy(){
		return new OptionalStackNode(this);
	}
	
	public StackNode getCleanCopyWithPrefix(){
		OptionalStackNode opsn = new OptionalStackNode(this);
		opsn.prefixes = prefixes;
		opsn.prefixStartLocations = prefixStartLocations;
		return opsn;
	}
	
	public StackNode[] getChildren(){
		StackNode copy = optional.getCleanCopy();
		copy.setParentProduction(symbol);
		copy.setStartLocation(-1); // Reset.
		
		StackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		copy.addEdge(this);
		epsn.addEdge(this);
		epsn.setStartLocation(startLocation);
		epsn.setParentProduction(symbol);

		StackNode[] children = new StackNode[2];
		children[0] = copy;
		children[1] = epsn;
		return children;
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
