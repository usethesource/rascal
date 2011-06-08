package org.rascalmpl.parser.gtd.exception;

import java.net.URI;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public class ParseError extends RuntimeException{
	private static final long serialVersionUID = 3755880608516802997L;

	private final String message;
	
	private final URI location;
	private final int offset;
	private final int length;
	private final int beginLine;
	private final int endLine;
	private final int beginColumn;
	private final int endColumn;
	
	private final Stack<AbstractStackNode> unexpandableNodes;
	private final Stack<AbstractStackNode> unmatchableNodes;
	private final DoubleStack<AbstractStackNode, AbstractNode> filteredNodes;
	
	public ParseError(String message, URI location, int offset, int length, int beginLine, int endLine, int beginColumn, int endColumn, Stack<AbstractStackNode> unexpandableNodes, Stack<AbstractStackNode> unmatchableNodes, DoubleStack<AbstractStackNode, AbstractNode> filteredNodes){
		super();
		
		this.message = message;
		
		this.location = location;
		this.offset = offset;
		this.length = length;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.beginColumn = beginColumn;
		this.endColumn = endColumn;
		
		this.unexpandableNodes = unexpandableNodes;
		this.unmatchableNodes = unmatchableNodes;
		this.filteredNodes = filteredNodes;
	}
	
	public ParseError(String message, URI location, int offset, int length, int beginLine, int endLine, int beginColumn, int endColumn){
		super();

		this.message = message;

		this.location = location;
		this.offset = offset;
		this.length = length;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.beginColumn = beginColumn;
		this.endColumn = endColumn;
		
		this.unexpandableNodes = null;
		this.unmatchableNodes = null;
		this.filteredNodes = null;
	}
	
	public String getMessage(){
		return message;
	}
	
	public URI getLocation(){
		return location;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getBeginLine(){
		return beginLine;
	}
	
	public int getEndLine(){
		return endLine;
	}
	
	public int getBeginColumn(){
		return beginColumn;
	}
	
	public int getEndColumn(){
		return endColumn;
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
