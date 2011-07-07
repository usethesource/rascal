package org.rascalmpl.parser.gtd.grammar;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;

public class Grammar{
	public final static int LOOK_AHEAD_TABLE_SIZE = 128;
	public final static int MAX_CODE_POINT = 126;
	public final static int UNICODE_OVERFLOW_SLOT = 127;
	
	private final AbstractStackNode[][][] expectMatrix;
	private final int[] lookAheadTable;
	
	public Grammar(AbstractStackNode[][][] expectMatrix, int[] lookAheadTable){
		super();
		
		this.expectMatrix = expectMatrix;
		this.lookAheadTable = lookAheadTable;
	}
	
	public AbstractStackNode[] getAlternatives(int sortIdentifier, char lookAhead){
		if(lookAhead > MAX_CODE_POINT){
			return expectMatrix[sortIdentifier][UNICODE_OVERFLOW_SLOT];
		}
		return expectMatrix[sortIdentifier][lookAheadTable[lookAhead]];
	}
	
	// TODO Restrictions.
}
