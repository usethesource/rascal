package org.rascalmpl.parser.sgll.stack;

import org.rascalmpl.parser.sgll.result.AbstractNode;
import org.rascalmpl.parser.sgll.result.ContainerNode;
import org.rascalmpl.parser.sgll.result.struct.Link;
import org.rascalmpl.parser.sgll.util.ArrayList;

public final class NonTerminalStackNode extends AbstractStackNode{
	private final int levelId;
	
	private final String nonTerminal;
	
	private ContainerNode result;
	
	public NonTerminalStackNode(int id, int levelId, String nonTerminal){
		super(id);
		
		this.levelId = levelId;
		
		this.nonTerminal = nonTerminal;
	}
	
	public NonTerminalStackNode(int id, int levelId, IReducableStackNode[] followRestrictions, String nonTerminal){
		super(id, followRestrictions);
		
		this.levelId = levelId;
		
		this.nonTerminal = nonTerminal;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original){
		super(original);
		
		levelId = original.levelId;
		
		nonTerminal = original.nonTerminal;
	}
	
	private NonTerminalStackNode(NonTerminalStackNode original, ArrayList<Link>[] prefixes){
		super(original, prefixes);
		
		levelId = original.levelId;
		
		nonTerminal = original.nonTerminal;
	}
	
	public int getLevelId(){
		return levelId;
	}
	
	public String getName(){
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
		return new NonTerminalStackNode(this, prefixesMap);
	}
	
	public void setResultStore(ContainerNode resultStore){
		result = resultStore;
	}
	
	public ContainerNode getResultStore(){
		return result;
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode[] getChildren(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
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
