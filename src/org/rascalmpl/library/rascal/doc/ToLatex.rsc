module rascal::doc::ToLatex

import rascal::doc::Document;
import rascal::doc::LatexIsland;
import ParseTree;
import String;
import lang::box::util::Highlight;
import lang::box::util::HighlightToLatex;
import Reflective;

public map[str,str] mathLiterals = (
		"o": 		"\\circ",
		"\>": 		"\>",
		"\<": 		"\<",
		"\>=": 		"\\geq",
		"\<=": 		"\\leq",
		"\<-": 		"\\leftarrow",
		"in": 		"\\in",
		"*": 		"\\times",
		"&": 		"\\cap",
		"&&": 		"\\wedge",
		"||": 		"\\vee",
		"!": 		"\\neg",
		"any": 		"\\exists",
		"all": 		"\\forall",
		"==": 		"\\equiv",
		"!=": 		"\\neq",
		"==\>": 	"\\Rightarrow",
		"\<=\>": 	"\\Leftrightarrow",
		"=\>": 		"\\mapsto",
		":=": 		"\\simeq",
		"!:=": 		"\\not\\simeq"

);

public str rascalDoc2Latex(str s, loc l) {
	return expand(parse(#Document, s), l, formatBlock, formatInline);
}

public str rascalDoc2Latex(loc l) {
	return expand(parse(#Document, l), l, formatBlock, formatInline);
}

private str formatBlock(Tree t, loc l) {
	snip = unquote("<t>", "\\begin{rascal}", "\\end{rascal}"); 
	return "\\begin{alltt}<rascalToLatex(snip, l)>\\end{alltt}";
}		

private str formatInline(Tree t, loc l) {
	snip = unquote("<t>", "\\rascal{", "}");
	return "{<rascalToLatex(snip, l)>}";
}

private str rascalToLatex(str snip, loc l) {
	pt = annotateMathOps(parseCommand(snip, l), mathLiterals);
	return highlight2latex(highlight(pt));
}

private str unquote(str src, str bq, str eq) {
	return substring(src, size(bq), size(src) - size(eq));
}



