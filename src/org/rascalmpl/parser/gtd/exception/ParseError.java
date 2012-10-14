package org.rascalmpl.parser.gtd.exception;

import java.net.URI;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.util.ArrayList;
import org.rascalmpl.parser.gtd.util.DoubleStack;
import org.rascalmpl.parser.gtd.util.Stack;

public class ParseError extends RuntimeException{
	private static final long serialVersionUID = 3755880608516802997L;
	
	private final URI location;
	private final int offset;
	private final int length;
	private final int beginLine;
	private final int endLine;
	private final int beginColumn;
	private final int endColumn;
	
	private final Stack<AbstractStackNode<?>> unexpandableNodes;
	private final Stack<AbstractStackNode<?>> unmatchableLeafNodes;
	private final DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>> unmatchableMidProductionNodes;
	private final DoubleStack<AbstractStackNode<?>, AbstractNode> filteredNodes;
	
	public ParseError(String message, URI location, int offset, int length, int beginLine, int endLine, int beginColumn, int endColumn, Stack<AbstractStackNode<?>> unexpandableNodes, Stack<AbstractStackNode<?>> unmatchableLeafNodes, DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>> unmatchableMidProductionNodes, DoubleStack<AbstractStackNode<?>, AbstractNode> filteredNodes){
		super(message);
		
		this.location = location;
		this.offset = offset;
		this.length = length;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.beginColumn = beginColumn;
		this.endColumn = endColumn;
		
		this.unexpandableNodes = unexpandableNodes;
		this.unmatchableLeafNodes = unmatchableLeafNodes;
		this.unmatchableMidProductionNodes = unmatchableMidProductionNodes;
		this.filteredNodes = filteredNodes;
	}
	
	public ParseError(String message, URI location, int offset, int length, int beginLine, int endLine, int beginColumn, int endColumn){
		super(message);
		
		this.location = location;
		this.offset = offset;
		this.length = length;
		this.beginLine = beginLine;
		this.endLine = endLine;
		this.beginColumn = beginColumn;
		this.endColumn = endColumn;
		
		this.unexpandableNodes = null;
		this.unmatchableLeafNodes = null;
		this.unmatchableMidProductionNodes = null;
		this.filteredNodes = null;
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
	
	public Stack<AbstractStackNode<?>> getUnexpandableNodes(){
		return unexpandableNodes;
	}
	
	public Stack<AbstractStackNode<?>> getUnmatchableLeafNodes(){
		return unmatchableLeafNodes;
	}
	
	public DoubleStack<ArrayList<AbstractStackNode<?>>, AbstractStackNode<?>> getUnmatchableMidProductionNodes(){
		return unmatchableMidProductionNodes;
	}
	
	public DoubleStack<AbstractStackNode<?>, AbstractNode> getFilteredNodes(){
		return filteredNodes;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(getMessage());
		
		sb.append("\n");
		sb.append("at ");
		sb.append(location);
		sb.append(" offset=");
		sb.append(offset);
		sb.append(" length=");
		sb.append(length);
		sb.append(" begin=");
		sb.append(beginLine);
		sb.append(":");
		sb.append(beginColumn);
		sb.append(" end=");
		sb.append(endLine);
		sb.append(":");
		sb.append(endColumn);
		
		return sb.toString();
	}
	
	public String toDetailedString(){
		StringBuilder sb = new StringBuilder();
		
		sb.append(getMessage());
		
		sb.append("\n");
		sb.append("at ");
		sb.append(location);
		sb.append(" offset=");
		sb.append(offset);
		sb.append(" length=");
		sb.append(length);
		sb.append(" begin=");
		sb.append(beginLine);
		sb.append(":");
		sb.append(beginColumn);
		sb.append(" end=");
		sb.append(endLine);
		sb.append(":");
		sb.append(endColumn);
		
		sb.append("\n");
		sb.append("Unexpandable nodes: ");
		int nrOfUnexpandableNodes = unexpandableNodes.getSize();
		if(nrOfUnexpandableNodes == 0){
			sb.append("none");
		}else{
			for(int i = nrOfUnexpandableNodes - 1; i >= 0; --i){
				sb.append("\n");
				sb.append(unexpandableNodes.get(i));
			}
		}
		
		sb.append("\n");
		sb.append("Unmatchable nodes: ");
		int nrOfUnmatchableLeafNodes = unmatchableLeafNodes.getSize();
		if(nrOfUnmatchableLeafNodes == 0){
			sb.append("none");
		}else{
			for(int i = nrOfUnmatchableLeafNodes - 1; i >= 0; --i){
				sb.append("\n");
				sb.append(unmatchableLeafNodes.get(i));
			}
		}
		
		sb.append("\n");
		sb.append("Unmatchable nodes: ");
		int nrOfUnmatchableMidProductionNodes = unmatchableMidProductionNodes.getSize();
		if(nrOfUnmatchableMidProductionNodes == 0){
			sb.append("none");
		}else{
			for(int i = nrOfUnmatchableMidProductionNodes - 1; i >= 0; --i){
				sb.append("\n");
				sb.append(unmatchableMidProductionNodes.getSecond(i));
			}
		}
		
		sb.append("\n");
		sb.append("Filtered nodes: ");
		int nrOfFilteredNodes = filteredNodes.getSize();
		if(nrOfFilteredNodes == 0){
			sb.append("none");
		}else{
			for(int i = nrOfFilteredNodes - 1; i >= 0; --i){
				sb.append("\n");
				sb.append(filteredNodes.getFirst(i));
			}
		}
		
		return sb.toString();
	}
}
