package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.highlighter;

public interface IHighlighter {

	IHighlighter setKeywordMarkup(String open, String close);	
	IHighlighter setCommentMarkup(String open, String close);
		
	public String highlight(String code);
	
}
