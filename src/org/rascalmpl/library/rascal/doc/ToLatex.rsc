module rascal::doc::ToLatex

import lang::box::util::Highlight;
import lang::box::util::HighlightToLatex;
import Reflective;
import ParseTree;
import String;
import IO;

data Chunk
	= block(str s)
	| inline(str s)
	| water(str s);

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

public str rascalDoc2Latex(loc l) {
	return rascalDoc2Latex(readFile(l), l);
}

public str rascalDoc2Latex(str s, loc l) {
	return expand(myParse(s), l);
}


private str rascalToLatex(str snip, loc l) {
	pt = annotateSpecials(parseCommand(snip, l));
	pt = annotateMathOps(pt, mathLiterals);
	return highlight2latex(highlight(pt));
}


private Tree annotateSpecials(Tree pt) {
	println("Annotating specials...");
	return top-down-break visit (pt) {
		
		// tuples
		case appl(p:prod([lit("\<"), _*, lit("\>")], _, _), [lt, a*, gt]) =>
			appl(p, [lt[@math="\\langle"], a, gt[@math="\\rangle"]])

		// multi variables			
		case appl(p:prod([_*, lit("*")], _, _), [a*, star]) =>
			appl(p, [a, star[@math="^{*}"]])  
	}
}

private str unquote(str src, str bq, str eq) {
	return substring(src, size(bq), size(src) - size(eq));
}

public str expand(list[Chunk] doc, loc l) {
	result = "";
	for (c <- doc) {
		switch (c) {
			case inline(str s): result += "\\irascaldoc{<rascalToLatex(s, l)>}";
			case block(str s):  result += "\\begin{rascaldoc}<rascalToLatex(s, l)>\\end{rascaldoc}";
			case water(str s): 	result += s;
		}
	}
	return result;
}


public list[Chunk] parseInlines(str s) {
	// does not support \n in s
	result = [];
	int i = 0;
	while (s != "", /^<pre:.*>\\rascal\{/ := s) {
		off = size(pre) + 8;
		s = (off < size(s)) ? substring(s, off) : "";
		result += [water(pre)];
		i = 0;
		nest = 1;
		ile = inline("");
		while (nest > 0, i < size(s)) {
			if (stringChar(charAt(s, i)) == "{") {
				nest += 1;
			}
			if (stringChar(charAt(s, i)) == "}") {
				nest -= 1;
			}
			if (nest > 0) {
				ile.s += stringChar(charAt(s, i));
			}
			i += 1;
		}
		result += [ile];
	}
	result += [water(substring(s, i))];
	return result;
}
	
public list[Chunk] myParse(str s) {
	println("Parsing...");
	ile = block("");
	eau = water("");
	result = [];
	inIsland = false;
	begin = true;
	while (s != "", /^<line:.*>/ := s) {
		off = size(line);
		s = (off < size(s)) ? substring(s, off + 1) : "";
		if (!inIsland, /^\\begin\{rascal\}$/ := line) {
			inIsland = true;
			ile.s = "";
		}
		else if (inIsland, /^\\end\{rascal\}$/ := line) {
			inIsland = false;
			result += [ile];
		}
		else if (inIsland) {
			ile.s += "<line>\n";
		}
		else {
			result += parseInlines("<line>\n");
		}
	}
	println("Done parsing.");
	return result;
} 



