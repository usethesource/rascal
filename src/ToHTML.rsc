@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
module lang::rascal::doc::ToHTML

import lang::rascal::doc::Document;
import lang::rascal::doc::HTMLIsland;
import ParseTree;
import String;
import lang::box::util::Highlight;
import lang::box::util::HighlightToHTML;
import Reflective;

public map[str,str] mathLiterals = (
		"o": 		"&circ;",
//		"\>": 		"&gt;",
//		"\<": 		"&lt;",
		"\>=": 		"&ge;",
		"\<=": 		"&le;",
		"\<-": 		"&larr;",
		"in": 		"&isin;",
		"*": 		"&times;",
		"&": 		"&cap;",
		"&&": 		"&and;",
		"||": 		"&or;",
		"!": 		"&not;",
		"any": 		"&exist;",
		"all": 		"&forall;",
		"==": 		"&equiv;",
		"!=": 		"&nequiv;",
		"==\>": 	"&rArr;",
		"\<=\>": 	"&hArr.",
		"=\>": 		"&#21A6;", // mapsto
		":=": 		"&cong;",
		"!:=": 		"&#2246;", // not congruent
		"join":		"&#22C8;"

);

public str rascalDoc2HTML(str s, loc l) {
	return expand(parse(#Document, s), l, formatBlock, formatInline);
}

public str rascalDoc2HTML(loc l) {
	return expand(parse(#Document, l), l, formatBlock, formatInline);
}

private str formatBlock(Tree t, loc l) {
	pre = "\<code class=\"rascal\"\>";
	post = "\</code\>";
	snip = unquote("<t>", pre, post); 
	return "<pre><rascalToHTML(snip, l)><post>";
}		

private str formatInline(Tree t, loc l) {
	pre = "\<span class=\"rascal\"\>";
	post = "\</span\>";
	snip = unquote("<t>", pre, post);
	return "<pre><rascalToHTML(snip, l)><post>";
}

private str rascalToHTML(str snip, loc l) {
	pt = annotateMathOps(parseCommand(snip, l), mathLiterals);
	return highlight2html(highlight(pt));
}

private str unquote(str src, str bq, str eq) {
	return substring(src, size(bq), size(src) - size(eq));
}



