package org.rascalmpl.parser.gtd.result.out;

/**
 * This class can be used to keep track of post-parse filtering errors. These
 * kinds of errors can occur during parse tree conversion, due to semantic
 * actions.
 */
public class FilteringTracker{
	private int offset;
	private int endOffset;
	
	public FilteringTracker(){
		super();
	}
	
	/**
	 * Marks the last filtered location.
	 */
	public void setLastFiltered(int offset, int endOffset){
		this.offset = offset;
		this.endOffset = endOffset;
	}
	
	/**
	 * Returns the begin location in the input string of the last filtered
	 * location.
	 */
	public int getOffset(){
		return offset;
	}
	
	/**
	 * Returns the end location in the input string of the last filtered
	 * location.
	 */
	public int getEndOffset(){
		return endOffset;
	}
}
