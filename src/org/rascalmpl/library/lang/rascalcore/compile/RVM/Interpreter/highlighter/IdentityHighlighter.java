package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.highlighter;

public class IdentityHighlighter implements IHighlighter {

	@Override
	public IHighlighter setKeywordMarkup(String open, String close) {
		return this;
	}

	@Override
	public IHighlighter setCommentMarkup(String open, String close) {
		return this;
	}

	@Override
	public String highlight(String code) {
		return code;
	}

}
