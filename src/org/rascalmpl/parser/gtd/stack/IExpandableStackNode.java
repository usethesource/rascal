package org.rascalmpl.parser.gtd.stack;

public interface IExpandableStackNode{
	final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	final static EpsilonStackNode EMPTY = new EpsilonStackNode(DEFAULT_LIST_EPSILON_ID, 0);
	
	AbstractStackNode[] getChildren();
	
	boolean canBeEmpty();
	
	AbstractStackNode getEmptyChild();
}
