module util::Highlight

import ParseTree;
import String;

// A comment

public map[str, str] htmlEscapes = (
	"\<": "&lt;",
	"\>": "&gt;",
	"&" : "&amp;"
);


str highlight2html(Tree t) 
  = "\<pre\>\<code\><trim(highlight2htmlRec(t))>\</code\>\</pre\>";

str highlight2htmlRec(appl(prod(lit(str l), _, _), _)) = span("Keyword", l)
  when /^[a-zA-Z0-9_\-]*$/ := l;

str highlight2htmlRec(appl(prod(_, _, {_*, \tag("category"(str cat))}), list[Tree] as))
  = span(cat, ( "" | it + highlight2htmlRec(a) | a <- as ));

str highlight2htmlRec(appl(prod(_, _, set[Attr] attrs), list[Tree] as))
  = ( "" | it + highlight2htmlRec(a) | a <- as )
  when {_*, \tag("category"(str _))} !:= attrs;

str highlight2htmlRec(appl(regular(_), list[Tree] as))
  = ( "" | it + highlight2htmlRec(a) | a <- as );

str highlight2htmlRec(amb({k, _*})) = highlight2htmlRec(k);

default str highlight2htmlRec(Tree t) = escape(unparse(t), htmlEscapes);

str span(str class, str src) = "\<span class=\"<class>\"\><src>\</span\>";

// Latex

public map[str, str] texEscapes = (
	"\\": "\\textbackslash{}",
	"\<": "\\textless{};",
	"\>": "\\textgreater{};",
	"%": "\\%{};",
	"&" : "\\&{}",
	"_" : "\\_{}",
	"^" : "\\^{}",
	"{" : "\\{{}",
	"}" : "\\}{}",
	"$" : "\\${}"
);

str highlight2latex(appl(prod(lit(str l), _, _), _)) = catCmd("Keyword", l)
  when /^[a-zA-Z0-9_\-]*$/ := l;

str highlight2latex(appl(prod(_, _, {_*, \tag("category"(str cat))}), list[Tree] as))
  = catCmd(cat, ( "" | it + highlight2latex(a) | a <- as ));

str highlight2latex(appl(prod(_, _, set[Attr] attrs), list[Tree] as))
  = ( "" | it + highlight2latex(a) | a <- as )
  when {_*, \tag("category"(str _))} !:= attrs;

str highlight2latex(appl(regular(_), list[Tree] as))
  = ( "" | it + highlight2latex(a) | a <- as );

str highlight2latex(amb({k, _*})) = highlight2latex(k);

default str highlight2latex(Tree t) = escape(unparse(t), texEscapes);

str catCmd(str class, str src) = "\\CAT{<class>}{<src>}";
