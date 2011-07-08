package org.rascalmpl.parser.gtd.grammar;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;

public class Grammar{
	public final static int LOOK_AHEAD_TABLE_SIZE = 0x10000;
	public final static int MAX__LOOKAHEAD_CODE_POINT = 0xfffe;
	public final static int LOOKAHEAD_OVERFLOW_SLOT = 0xffff;
	
	private final AbstractStackNode[][][] expectMatrix;
	private final int[] lookAheadTable;
	
	public Grammar(AbstractStackNode[][][] expectMatrix, int[] lookAheadTable){
		super();
		
		this.expectMatrix = expectMatrix;
		this.lookAheadTable = lookAheadTable;
	}
	
	public AbstractStackNode[] getAlternatives(int sortIdentifier, char lookAhead){
		if(lookAhead > MAX__LOOKAHEAD_CODE_POINT){
			return expectMatrix[sortIdentifier][LOOKAHEAD_OVERFLOW_SLOT];
		}
		return expectMatrix[sortIdentifier][lookAheadTable[lookAhead]];
	}
	
	// TODO Restrictions.
}
