
@license{
  Copyright (c) 2013-2024 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs.van.der.Storm@cwi.nl}
@contributor{Jurgen.Vinju@cwi.nl}
@synopsis{Maps parse trees to highlighting markup in ANSI, HTML or LaTeX format.}
module util::Highlight

import ParseTree;
import String;
import IO;

@synopsis{Yields the characters of a parse tree as the original input sentence in a <code>...</code> block, but with spans for highlighted segments in HTML}
public str ToHTML(Tree t) {
  htmlEscapes = (
	  "\<": "&lt;",
	  "\>": "&gt;",
	  "&" : "&amp;"
  );

  str rec(t:appl(prod(lit(str l), _, _), _)) 
    = span("Keyword", l) when isKeyword(l);

  str rec(t:appl(prod(cilit(str l), _, _), _)) 
    = span("Keyword", l) when isKeyword(l);

  str rec(t:appl(prod(_, _, {*_, \tag("category"(str cat))}), list[Tree] as))
    = span(cat, "<for (a <- as) {><rec(a)><}>");

  default str rec(appl(_, list[Tree] as))
    = "<for (a <- as) {><rec(a)><}>";

  str rec(amb({k, *_})) = rec(k);

  default str rec(Tree t:char(_)) = escape("<t>", htmlEscapes);

  str span(str class, str src) = "\<span class=\"<class>\"\><src>\</span\>";

  return "\<pre class=\"rascal\"\>\<code\><trim(rec(t))>\</code\>\</pre\>";
}

@synopsis{Yields the characters of a parse tree as the original input sentence but using macros to wrap to-be-highlighted areas.}
public str toLaTeX(Tree t) {
  texEscapes = (
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

  str rec(appl(prod(lit(str l), _, _), _)) = cat("Keyword", l)
    when isKeyword(l);

  str rec(appl(prod(cilit(str l), _, _), _)) = cat("Keyword", l)
    when isKeyword(l);

  str rec(appl(prod(_, _, {*_, \tag("category"(str category))}), list[Tree] as))
    = cat(category, "<for (a <- as) {><rec(a)><}>");

  default str rec(appl(_, list[Tree] as)) 
    = "<for (a <- as) {><rec(a)><}>";
    
  str rec(amb({k, *_})) = rec(k);

  default str rec(Tree t:char(_)) = escape("<t>", texEscapes);

  str cat(str class, str src) = "\\CAT{<class>}{<src>}";

  return rec(t);
} 

@synopsis{Yields the characters of a parse tree as the original input sentence in a <code>...</code> block, but with spans for highlighted segments in HTML}
public str toHTML(Tree t) {
  htmlEscapes = (
	  "\<": "&lt;",
	  "\>": "&gt;",
	  "&" : "&amp;"
  );

  str rec(t:appl(prod(lit(str l), _, _), _)) 
    = wrapLink(span("Keyword", l), t)
    when isKeyword(l);

  str rec(t:appl(prod(cilit(str l), _, _), _)) 
    = wrapLink(span("Keyword", l), t)
    when isKeyword(l);

  str rec(t:appl(prod(_, _, {*_, \tag("category"(str cat))}), list[Tree] as))
    = wrapLink(span(cat, ( "" | it + rec(a) | a <- as )), t);

  str rec(appl(prod(_, _, set[Attr] attrs), list[Tree] as))
    = ( "" | it + rec(a) | a <- as )
    when {*_, \tag("category"(str _))} !:= attrs;

  str rec(appl(regular(_), list[Tree] as))
    = ( "" | it + rec(a) | a <- as );

  str rec(amb({k, *_})) = rec(k);

  default str rec(Tree t) 
    = wrapLink(escape(unparse(t), htmlEscapes), t);

  str span(str class, str src) = "\<span class=\"<class>\"\><src>\</span\>";

  default str wrapLink(str text, Tree _) = text;

  return "\<pre class=\"rascal\"\>\<code\><trim(rec(t))>\</code\>\</pre\>";
}

@synopsis{Unparse a parse tree to unicode characters, wrapping certain substrings with ANSI codes for highlighting.}
public str toANSI(Tree t, bool underlineAmbiguity=false, int tabSize=4) {
  str rec(Tree x:appl(prod(lit(str l), _, _), _))   = isKeyword(l) ? bold("<x>") :  "<x>";
  str rec(Tree x:appl(prod(cilit(str l), _, _), _)) = isKeyword(l) ? bold("<x>") :  "<x>";

  str rec(Tree x:appl(prod(_, _, {*_, \tag("category"(str cat))}), list[Tree] as))
    = \map(cat, "<x>");

  default str rec(x:appl(_, list[Tree] as))
    = "<for (a <- as) {><rec(a)><}>";

  str rec(amb({k, *_})) = underlineAmbiguity ? underline(rec(k)) : rec(k);

  str rec (char(9)) = right("", tabSize);
  default str rec(Tree t:char(_)) = "<t>";

  str ESC               = "\a1b[";
  str Bold              = "<ESC>1m";
  str Underline         = "<ESC>4m";
  str Normal            = "<ESC>0m";
  str Comment           = "<ESC>3m<ESC>2m";
  str bold(str s)       = "<Bold><s><Normal>";
  str underline(str s)  = "<Underline><s><Normal>";
  str comment(str s)    = "<Comment><s><Normal>";

  str \map("Comment", text)         = comment(text);
  str \map("Keyword", text)         = bold(text);
  default str \map(str _, str text) = text;

  return rec(t);
} 

@synopsis{Encodes when to highlight a literal as a keyword category}
private bool isKeyword(str s) = /^[a-zA-Z0-9_\-]*$/ := s;