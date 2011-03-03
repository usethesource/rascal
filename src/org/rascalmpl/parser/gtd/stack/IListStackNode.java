package org.rascalmpl.parser.gtd.stack;

public interface IListStackNode{
	final static int DEFAULT_LIST_EPSILON_ID = -2; // (0xeffffffe | 0x80000000)
	
	AbstractStackNode[] getChildren();
}
