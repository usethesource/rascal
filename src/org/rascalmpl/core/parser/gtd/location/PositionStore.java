/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.parser.gtd.location;

public class PositionStore{
	private final static int LINE_FEED_CHAR = '\n';
	private final static int CARRIAGE_RETURN_CHAR = '\r';
	
	private final static int DEFAULT_SIZE = 8;
	
	private int[] offsets;
	private int endOfFile;
	
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
	
	public void index(int[] input){
		add(0);
		
		endOfFile = input.length;
		
		boolean encounteredCarriageReturn = false;
		for(int i = 0; i < endOfFile; ++i){
			int character = input[i];
			if(character == CARRIAGE_RETURN_CHAR){
				encounteredCarriageReturn = true;
			}else if(character == LINE_FEED_CHAR){
				add(i + 1);
				encounteredCarriageReturn = false;
			}else if(encounteredCarriageReturn){
				add(i);
				encounteredCarriageReturn = false;
			}
		}
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
		return (offset == endOfFile) || (isAtColumn(offset+1, 0));
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
