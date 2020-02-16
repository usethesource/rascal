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
  = "\<pre class=\"rascal\"\>\<code\><trim(highlight2htmlRec(t))>\</code\>\</pre\>";

bool isKeyword(str s) = /^[a-zA-Z0-9_\-]*$/ := s;

str highlight2htmlRec(t:appl(prod(lit(str l), _, _), _)) 
  = wrapLink(span("Keyword", l), t)
  when isKeyword(l);

str highlight2htmlRec(t:appl(prod(cilit(str l), _, _), _)) 
  = wrapLink(span("Keyword", l), t)
  when isKeyword(l);

str highlight2htmlRec(t:appl(prod(_, _, {*_, \tag("category"(str cat))}), list[Tree] as))
  = wrapLink(span(cat, ( "" | it + highlight2htmlRec(a) | a <- as )), t);

str highlight2htmlRec(appl(prod(_, _, set[Attr] attrs), list[Tree] as))
  = ( "" | it + highlight2htmlRec(a) | a <- as )
  when {*_, \tag("category"(str _))} !:= attrs;

str highlight2htmlRec(appl(regular(_), list[Tree] as))
  = ( "" | it + highlight2htmlRec(a) | a <- as );

str highlight2htmlRec(amb({k, *_})) = highlight2htmlRec(k);

default str highlight2htmlRec(Tree t) 
  = wrapLink(escape(unparse(t), htmlEscapes), t);

str span(str class, str src) = "\<span class=\"<class>\"\><src>\</span\>";

default str wrapLink(str text, Tree _) = text;

// Latex

public map[str, str] texEscapes = (
	"\\": "\\textbackslash{}",
	"\<": "\\textless{}",
	"\>": "\\textgreater{}",
	"%": "\\%{}",
	"&" : "\\&{}",
	"_" : "\\_{}",
	"^" : "\\^{}",
	"{" : "\\{{}",
	"}" : "\\}{}",
	"$" : "\\${}",
	"[" : "{}[",
	"\t" : "    "
);

str highlight2latex(appl(prod(lit(str l), _, _), _)) = catCmd("Keyword", l)
  when isKeyword(l);

str highlight2latex(appl(prod(cilit(str l), _, _), _)) = catCmd("Keyword", l)
  when isKeyword(l);

str highlight2latex(appl(prod(_, _, {*_, \tag("category"(str cat))}), list[Tree] as))
  = catCmd(cat, ( "" | it + highlight2latex(a) | a <- as ));

str highlight2latex(appl(prod(_, _, set[Attr] attrs), list[Tree] as))
  = ( "" | it + highlight2latex(a) | a <- as )
  when {*_, \tag("category"(str _))} !:= attrs;

str highlight2latex(appl(regular(_), list[Tree] as))
  = ( "" | it + highlight2latex(a) | a <- as );

str highlight2latex(amb({k, *_})) = highlight2latex(k);

default str highlight2latex(Tree t) = escape(unparse(t), texEscapes);

str catCmd(str class, str src) = "\\CAT{<class>}{<src>}";
