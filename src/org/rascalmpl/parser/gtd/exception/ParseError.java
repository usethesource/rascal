package org.rascalmpl.parser.gtd.exception;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public class ParseError extends RuntimeException{
	private final static long serialVersionUID = -7745764421591174025L;
	
	private final String message;
	private final ISourceLocation location;
	
	private final Stack<AbstractStackNode> unexpandableNodes;
	private final Stack<AbstractStackNode> unmatchableNodes;
	private final DoubleStack<AbstractStackNode, AbstractNode> filteredNodes;
	
	public ParseError(String message, ISourceLocation location, Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, DoubleStack<AbstractStackNode, AbstractNode> filteredNodes){
		super();
		
		this.message = message;
		this.location = location;
		
		this.unexpandableNodes = unexpandableNodes;
		this.unmatchableNodes = unmatchableNodes;
		this.filteredNodes = filteredNodes;
	}
	
	public ParseError(String message, ISourceLocation location){
		super();

		this.message = message;
		this.location = location;
		
		this.unexpandableNodes = null;
		this.unmatchableNodes = null;
		this.filteredNodes = null;
	}
	
	public String getMessage(){
		return message;
	}
	
	public ISourceLocation getLocation(){
		return location;
	}
	
	public Stack<AbstractStackNode> getUnexpandableNodes(){
		return unexpandableNodes;
	}
	
	public Stack<AbstractStackNode> getUnmatchableNodes(){
		return unmatchableNodes;
	}
	
	public DoubleStack<AbstractStackNode, AbstractNode> getFilteredNodes(){
		return filteredNodes;
	}
}
