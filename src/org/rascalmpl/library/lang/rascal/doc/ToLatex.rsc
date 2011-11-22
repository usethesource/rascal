@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::doc::ToLatex

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
		"notin": 	"\\not\\in",
		"*": 		"\\times",
		"&": 		"\\cap",
		"&&": 		"\\wedge",
		"||": 		"\\vee",
		"!": 		"\\neg",
		"any": 		"\\exists",
		"all": 		"\\forall",
		"==": 		"\\equiv",
		"!=": 		"\\neq",
		"==\>": 	"\\Longrightarrow",
		"\<=\>": 	"\\Leftrightarrow",
		"=\>": 		"\\Rightarrow",
		"!:=":		"\\neg{}:="
//		":=": 		"\\cong",
//		"!:=": 		"\\not\\cong"

);

public str rascalDoc2Latex(loc l) {
	return rascalDoc2Latex(readFile(l), l);
}

public str rascalDoc2Latex(str s, loc l) {
	return expand(myParse(s), l);
}

private str escapeRascalString(str s) {
	// escape backslashes and curlies
	preEscapes = ("\\": "\\textbackslash{}", "{": "\\{", "}": "\\}");
	s = escape(s, preEscapes);
	s = visit (s) {
		// introduce backslashes and curlies
		case /^<ws:\s*>'/ => escape("<ws>\'")
	}
	// escape the rest
	return escape(s, stringEscapes - preEscapes);
}

private str rascalToLatex(str snip, loc l) {
	// we take out the backticks before parsing and readd them afterwards
	int snipCount = 0;
	map[int, str] snips = ();
	newSnip = visit(snip) {
		case /`<w:[^`]*>`/ : {
			snipCount += 1;
			snips[snipCount] = w;
			insert "\":!:<snipCount>!:!\"";
		}
	}
	try {
		pt = parseCommands(newSnip, l);
		//println("Annotating specials...");
		//pt = annotateSpecials(pt);
		//println("Annotating math ops...");
		//pt = annotateMathOps(pt, mathLiterals);
		highlighted = highlight2latex(highlight(pt), escapeRascalString);
		
		// revert backtick wraps etc
		return visit(highlighted) {
			case /STR\{\":!:<si:\d+>!:!\"\}/ => "BACKTICK{<snips[toInt(si)]>}"
		}
	}
	catch value err: {
		println("Parse error at <err>");
		return "\\begin{verbatim}PARSE ERROR at <err>  <newSnip> original: <snip>\\end{verbatim}";
	}
}


private Tree annotateSpecials(Tree pt) {
	return top-down-break visit (pt) {
		
		// tuples
		case appl(p:prod(label("Tuple",_), [lit("\<"), _*, lit("\>")], _), [lt, a*, gt]) =>
			appl(p, [lt[@math="\\langle"], a, gt[@math="\\rangle"]])

		// multi variables			
		case appl(p:prod(label("MultiVariable",_), [_*, lit("*")], _), [a*, star]) =>
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
			case water(str s):  result += s;
		}
	}
	return result;
}


public list[Chunk] parseInlines(str s) {
	// does not support \n in s
	result = [];
	int i = 0;
	while (s != "", /^<pre:.*>\\irascal\{/ := s) {
		off = size(pre) + 9;
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
		if (!inIsland, /^\s*\\begin\{rascal\}$/ := line) {
			inIsland = true;
			ile.s = "";
		}
		else if (inIsland, /^\s*\\end\{rascal\}$/ := line) {
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





