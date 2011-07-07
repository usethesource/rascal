package org.rascalmpl.parser.gtd.grammar;

import org.rascalmpl.parser.gtd.stack.AbstractStackNode;

public class Grammar{
	private final AbstractStackNode[][][] expectMatrix;
	private final char[] lookAheadTable;
	
	public Grammar(AbstractStackNode[][][] expectMatrix, char[] lookAheadTable){
		super();
		
		this.expectMatrix = expectMatrix;
		this.lookAheadTable = lookAheadTable;
	}
	
	public AbstractStackNode[] getAlternatives(int sortIdentifier, char lookAhead){
		return expectMatrix[sortIdentifier][lookAheadTable[lookAhead]];
	}
	
	// TODO Restrictions.
}
