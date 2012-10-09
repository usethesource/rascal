package org.rascalmpl.parser.gtd.stack;

import org.rascalmpl.parser.gtd.result.AbstractNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;

/**
 * Indicates that the stack node is expandable.
 * Lists and optionals are examples of expandable nodes.
 */
public abstract class AbstractExpandableStackNode<P> extends AbstractStackNode<P>{
	public final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	public final static EpsilonStackNode<?> EMPTY = new EpsilonStackNode<Object>(DEFAULT_LIST_EPSILON_ID, 0);
	
	protected AbstractExpandableStackNode(int id, int dot){
		super(id, dot);
	}
	
	protected AbstractExpandableStackNode(int id, int dot, IEnterFilter[] enterFilters, ICompletionFilter[] completionFilters){
		super(id, dot, enterFilters, completionFilters);
	}
	
	protected AbstractExpandableStackNode(AbstractExpandableStackNode<P> original, int startLocation){
		super(original, startLocation);
	}
	
	/**
	 * Retrieves all the alternatives of the expandable.
	 */
	public abstract AbstractStackNode<P>[] getChildren();
	
	/**
	 * Check whether or not this node is nullable.
	 */
	public abstract boolean canBeEmpty();
	
	/**
	 * Retrieves the empty child (in case this node is nullable).
	 */
	public abstract AbstractStackNode<P> getEmptyChild();
	
	public boolean isEmptyLeafNode(){
		return false;
	}
	
	public AbstractNode match(int[] input, int location){
		throw new UnsupportedOperationException();
	}
	
	public AbstractStackNode<P> getCleanCopyWithResult(int startLocation, AbstractNode result){
		throw new UnsupportedOperationException();
	}
	
	public int getLength(){
		throw new UnsupportedOperationException();
	}
	
	public AbstractNode getResult(){
		throw new UnsupportedOperationException();
	}
	
	public final boolean isExpandable(){
		return true;
	}
		
}
