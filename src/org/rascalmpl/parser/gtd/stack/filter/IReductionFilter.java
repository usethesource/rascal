package org.rascalmpl.parser.gtd.stack.filter;

public interface IReductionFilter{
	boolean isFiltered(char[] input, int start, int end);
}
