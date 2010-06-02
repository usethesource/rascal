package org.rascalmpl.parser.sgll.stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.INode;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.parser.sgll.util.IntegerList;

public final class NonTerminalStackNode extends AbstractStackNode{
	private final String nonTerminal;
	
	private ContainerNode result;
	
	public NonTerminalStackNode(int id, String nonTerminal){
		super(id);
		
		this.nonTerminal = nonTerminal;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original){
		super(original);
		
		nonTerminal = original.nonTerminal;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original, ArrayList<INode[]> prefixes, IntegerList prefixStartLocations){
		super(original, prefixes, prefixStartLocations);
		
		nonTerminal = original.nonTerminal;
	}
	
	public String getMethodName(){
		return nonTerminal;
	}
	
	public boolean reduce(char[] input){
		throw new UnsupportedOperationException();
	}
	
	public boolean isClean(){
		return (result == null);
	}
	
	public AbstractStackNode getCleanCopy(){
		return new NonTerminalStackNode(this);
	}
	
	public AbstractStackNode getCleanCopyWithPrefix(){
		return new NonTerminalStackNode(this, prefixes, prefixStartLocations);
	}
	
	public void initializeResultStore(){
		result = new ContainerNode();
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
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
		sb.append(nonTerminal);
		sb.append(getId());
		sb.append('(');
		sb.append(startLocation);
		sb.append(',');
		sb.append('?');
		sb.append(')');
		
		return sb.toString();
	}
}
