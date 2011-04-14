@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::box::util::HighlightToLatex

import lang::box::util::Box;
import String;

// TODO this depends on specific LaTex commands like \VAR etc.

public map[str, str] latexEscapes = (
	"\t": "\\TAB{}",
	"#": "\\#",
	"%": "\\%",
	"_": "\\_",
	"$": "\\$",
	" ": "\\SPACE{}", // space gets eaten sometimes, \  eats up newlines. \quad?
	"{": "\\{",
	"}": "\\}",
	"&": "\\&",
	"\\": "\\textbackslash{}"
);

public map[str, str] stringEscapes = 
	// this dangerous: \t first to spaces, then to \textvisiblespace{}
	latexEscapes + ( "\t": "    ", " ": "\\textvisiblespace{}");


public str highlight2latex(list[Box] bs, str(str) myEscapeString) {
	res = "";
	for (b <- bs) {
		switch (b) {
			case KW(L(s)): res += "\\KW{<s>}";
			case STRING(L(s)): res += "\\STR{<myEscapeString(s)>}";
			case COMM(L(s)): res += "\\COMM{<escape(s)>}";
			case VAR(L(s)): res += "\\VAR{<escape(s)>}";
			case MATH(L(s)): res += "\\MATH{<s>}";
			case L(s): res += escape(s);
			default: throw "Unhandled box: <b>"; // todo NUM, REF etc. 
		}
	}
	return res;	
}

public str highlight2latex(list[Box] bs) {
	return highlight2latex(bs, escapeString);
}

public str escapeString(str s) {
	return escape(s, stringEscapes);
}

public str escape(str s) {
	return escape(s, latexEscapes);
}

