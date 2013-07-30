@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::box::util::Highlight

import lang::box::util::Category;

import Ambiguity;
import ParseTree;
import String;
import IO;

anno str Tree@math;

public Tree annotateMathOps(Tree tree, map[str, str] subst) {
	return top-down-break visit (tree) {
		case a:appl(prod(lit(str s), _, _), _) => a[@math=subst[s]] when subst[s]? && !((a@math)?)
	}
}

public list[Box] highlight(Tree t) {
	switch (t) {
		case a:appl(prod(lit(str l), _, _), _): {
			if ((a@math)?) {
				return [MATH(L(a@math))];
			}
			if (/^[a-zA-Z0-9_\-]*$/ := l) { 
				return [KW(L(l))];
			}
			return [L(l)];
		} 

		case appl(prod(\layouts(_), _, _), as): 
			return [ *highlightLayout(a) | a <- as ];
			
		case a:appl(prod(_, _, {_*, \tag("category"("Constant"))}), _):
			return [STRING(L(unparse(a)))];

		case a:appl(prod(_, _, {_*, \tag("category"("Identifier"))}), _):
			return [VAR(L(unparse(a)))];

		case a:appl(prod(_, _, {_*, \tag("category"(str cat))}), as):
			return [CAT(cat, [ *highlight(a) | a <- as ])];

		case a:appl(prod(\lex(_), _, _), _):
			return [L(unparse(a))];
			
		case appl(_, as):
			return [ *highlight(a) | a <- as ];

		case amb({k, _*}): {
			// this triggers a bug in stringtemplates??? 
			//throw "Ambiguous tree: <report(t)>";
			// pick one
			println("Warning: ambiguity: <t>");
			return highlight(k);
		}
			
		default: 
			throw "Unhandled tree <t>";
	}
}

private list[Box] highlightLayout(Tree t) {
	switch (t) {
		case a:appl(prod(_, _, {_*, \tag("category"("Comment"))}), _):
			return [COMM(L(unparse(a)))];
			
		case appl(_, as):
			return [ *highlightLayout(a) | a <- as ];
			
		case char(n):
			return [L(stringChar(n))];
			
		default:
			throw "Unhandled tree: <t>";
	}
}

