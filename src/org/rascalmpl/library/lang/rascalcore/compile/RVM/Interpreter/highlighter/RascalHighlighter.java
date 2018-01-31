package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.highlighter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RascalHighlighter implements IHighlighter {
	private static String keywordPattern;
	
	private String openKeyword = "<strong>";
	private String closeKeyword = "</strong>";
	private String openComment = "<emphasis>";
	private String closeComment = "</emphasis>";

	static {
		String[] keywords = { "o", "syntax", "keyword", "lexical", "int", "break", "continue", "rat", "true", "bag",
				"num", "node", "finally", "private", "real", "list", "fail", "filter", "if", "tag", "extend", "append",
				"rel", "lrel", "void", "non-assoc", "assoc", "test", "anno", "layout", "data", "join", "it", "bracket",
				"in", "import", "false", "all", "dynamic", "solve", "type", "try", "catch", "notin", "else", "insert",
				"switch", "return", "case", "while", "str", "throws", "visit", "tuple", "for", "assert", "loc",
				"default", "map", "alias", "any", "module", "mod", "bool", "public", "one", "throw", "set", "start",
				"datetime", "value", "loc", "node", "num", "type", "bag", "int", "rat", "rel", "lrel", "real", "tuple",
				"str", "bool", "void", "datetime", "set", "map", "list" };
		StringBuilder sb = new StringBuilder();
		sb.append("([/][/][^\n\r]*)"); // single line comment
		sb.append("|(?s)(/\\*.*?\\*/)");	 // multi-line comment
		sb.append("|(?s)(\"(?:\\\\[^\"]|\\\\\"|.)*?\")"); // string literal
		
		for (String kw : keywords) {
			sb.append("|");
			sb.append("(").append(kw).append(")");
		}
		keywordPattern = sb.toString();
	}
	
	public RascalHighlighter setKeywordMarkup(String open, String close){
		openKeyword = open;
		closeKeyword = close;
		return this;
	}
	
	public RascalHighlighter setCommentMarkup(String open, String close){
		openComment = open;
		closeComment = close;
		return this;
	}

	public String highlight(String code) {
		Pattern pat = Pattern.compile(keywordPattern);
		Matcher matcher = pat.matcher(code);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			String matched = matcher.group();
			if (matched.startsWith("//")) {
				matched = matched.replaceAll("[\n\r]", "");
				matcher.appendReplacement(sb, openComment + matched + closeComment);
			} else if (matched.startsWith("/*")) {
				matcher.appendReplacement(sb, openComment + matched + closeComment);
			} else {
				matcher.appendReplacement(sb, openKeyword + matched + closeKeyword);
			}
		}
		matcher.appendTail(sb);

		String highlightedCode = sb.toString();
		return highlightedCode;
	}
}
