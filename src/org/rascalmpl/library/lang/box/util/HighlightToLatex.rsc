module lang::box::util::HighlightToLatex

import lang::box::util::Box;
import String;

// TODO this depends on specific LaTex commands like \VAR etc.

public map[str, str] latexEscapes = (
	"\t": "\\TAB{}",
	"#": "\\#",
	"_": "\\_",
	"$": "\\$",
	" ": "\\space{}", // space gets eaten sometimes, \  eats up newlines. \quad?
	"{": "\\{",
	"}": "\\}",
	"\\": "\\textbackslash{}"
);

public map[str, str] stringEscapes = 
	latexEscapes + ( "\t": "    ", " ": "\\textvisiblespace{}");


public str highlight2latex(list[Box] bs) {
	res = "";
	for (b <- bs) {
		switch (b) {
			case KW(L(s)): res += "\\KW{<s>}";
			case STRING(L(s)): res += "\\STR{<escapeString(s)>}";
			case COMM(L(s)): res += "\\COMM{<escape(s)>}";
			case VAR(L(s)): res += "\\VAR{<escape(s)>}";
			case MATH(L(s)): res += "\\MATH{<s>}";
			case L(s): res += escape(s);
			default: throw "Unhandled box: <b>"; // todo NUM, REF etc. 
		}
	}
	return res;
}

public str escapeString(str s) {
	return escape(s, stringEscapes);
}

public str escape(str s) {
	return escape(s, latexEscapes);
}

