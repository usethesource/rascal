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
		":=": 		"\\cong",
		"!:=": 		"\\not\\cong"

);

public str rascalDoc2Latex(str s, loc l) {
	return expand(parse(#Document, s), l, formatBlock, formatInline);
}

public str rascalDoc2Latex(loc l) {
	return expand(parse(#Document, l), l, formatBlock, formatInline);
}

private str formatBlock(Tree t, loc l) {
	snip = unquote("<t>", "\\begin{rascal}", "\\end{rascal}"); 
	return "\\begin{rascalf}<rascalToLatex(snip, l)>\\end{rascalf}";
}		

private str formatInline(Tree t, loc l) {
	snip = unquote("<t>", "\\rascal{", "}");
	return "\\rascalf{<rascalToLatex(snip, l)>}";
}

private str rascalToLatex(str snip, loc l) {
	pt = annotateTuples(parseCommand(snip, l));
	pt = annotateMathOps(pt, mathLiterals);
	return highlight2latex(highlight(pt));
}

private Tree annotateTuples(Tree pt) {
	return visit (pt) {
		case appl(p:prod([lit("\<"), _*, lit("\>")], _, _), [lt, a*, gt]) =>
			appl(p, [lt[@math="\\langle"], a, gt[@math="\\rangle"]]) 
	}
}

private str unquote(str src, str bq, str eq) {
	return substring(src, size(bq), size(src) - size(eq));
}



