package org.rascalmpl.parser.sgll.util.specific;

public class PositionStore{
	private final static char END_LINE_CHAR = '\n';
	private final static char CARRIAGE_RETURN_CHAR = '\r';
	
	private final static int DEFAULT_SIZE = 8;
	
	private int[] offsets;
	
	private int size;
	
	private int cursor;
	
	public PositionStore(){
		super();
		
		offsets = new int[DEFAULT_SIZE];
	}
	
	public void enlarge(){
		int[] oldOffsets = offsets;
		offsets = new int[size << 1];
		System.arraycopy(oldOffsets, 0, offsets, 0, size);
	}
	
	public void index(char[] input){
		add(0);
		
		boolean encounteredCarriageReturn = false;
		for(int i = 0; i < input.length; ++i){
			char character = input[i];
			if(character == CARRIAGE_RETURN_CHAR){
				encounteredCarriageReturn = true;
			}else if(character == END_LINE_CHAR){
				add(i + 1);
				encounteredCarriageReturn = false;
			}else if(encounteredCarriageReturn){
				add(i);
				encounteredCarriageReturn = false;
			}
		}
		
		add(input.length); // EOF marker.
	}
	
	private void add(int offset){
		if(size == offsets.length){
			enlarge();
		}
		
		offsets[size++] = offset;
	}
	
	public void resetCursor(){
		cursor = 0;
	}
	
	public void setCursorTo(int index){
		cursor = index;
	}
	
	public int findLine(int offset){
		int line = cursor;
		
		if(offsets[line] <= offset){
			++line;
			while(line < size && offsets[line] <= offset){
				++line;
			}
			cursor = line - 1;
		}else{
			while(line > 0 && offsets[line] > offset){
				--line;
			}
			cursor = line;
		}
		
		return cursor;
	}
	
	public int getColumn(int offset, int line){
		return (offset - offsets[line]);
	}
	
	public boolean startsLine(int offset){
		return (isAtColumn(offset, 0));
	}
	
	public boolean endsLine(int offset){
		return (isAtColumn(offset, 0));
	}
	
	public boolean isAtColumn(int offset, int column){
		return (offset - offsets[findLine(offset)]) == column;
	}
	
	public void clear(){
		int length = offsets.length;
		offsets = new int[length];
		size = 0;
	}
	
	public void dirtyClear(){
		size = 0;
	}
}
