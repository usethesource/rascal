package org.rascalmpl.parser.gtd.result.out;

public class FilteringTracker{
	private int offset;
	private int endOffset;
	
	public FilteringTracker(){
		super();
	}
	
	public void setLastFilered(int offset, int endOffset){
		this.offset = offset;
		this.endOffset = endOffset;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public int getEndOffset(){
		return endOffset;
	}
}
