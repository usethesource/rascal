module lang::box::util::HighlightToHTML

import lang::box::util::Box;
import String;

public map[str, str] htmlEscapes = (
	"\<": "&lt;",
	"\>": "&gt;",
	"&" : "&amp;",
	" " : "&nbsp;"
);


// &#9251 = open box 
public map[str, str] stringEscapes = htmlEscapes + (" ": "&middot;");

public str highlight2html(list[Box] bs) {
	res = "";
	for (b <- bs) {
		switch (b) {
			case KW(L(s)): 		res += span("keyword", s);
			case STRING(L(s)): 	res += span("string", escapeString(s));
			case COMM(L(s)): 	res += span("comment", escape(s));
			case VAR(L(s)): 	res += span("variable", escape(s));
			case MATH(L(s)): 	res += span("math", s);
			case L(s): 			res += escape(s);
			default: throw "Unhandled box: <b>"; // todo NUM, REF etc. 
		}
	}
	return res;
}


private str span(str class, str src) {
	return "\<span class=\"<class>\"\><src>\</span\>";
}

private str escapeString(str s) {
	return escape(s, stringEscapes);
}

private str escape(str s) {
	return escape(s, htmlEscapes);
}

