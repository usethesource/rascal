package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;

public final class OptionalStackNode extends StackNode{
	private final StackNode optional;
	
	private final String nodeName;
	
	private boolean marked;
	
	private final INode result;
	
	public OptionalStackNode(int id, StackNode optional, String nodeName){
		super(id);
		
		this.optional = optional;
		
		this.nodeName = nodeName;
		
		this.result = null;
	}
	
	private OptionalStackNode(OptionalStackNode optionalParseStackNode){
		super(optionalParseStackNode);
		
		optional = optionalParseStackNode.optional;
		
		nodeName = optionalParseStackNode.nodeName;
		
		result = new ContainerNode(nodeName);
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
		StackNode epsn = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID);
		copy.addEdge(this);
		epsn.addEdge(this);
		
		copy.setStartLocation(-1); // Reset.

		StackNode[] children = new StackNode[2];
		children[0] = copy;
		children[1] = epsn;
		return children;
	}
	
	public String getMethodName(){
		throw new UnsupportedOperationException();
	}
	
	public void addResult(INode[] children){
		result.addAlternative(children);
	}
	
	public INode getResult(){
		return result;
	}
}
