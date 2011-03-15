module lang::box::util::Highlight

import lang::box::util::Box;

import ParseTree;
import String;
import IO;

anno str Tree@math;

public Tree annotateMathOps(Tree tree, map[str, str] subst) {
	return visit (tree) {
		case a:appl(prod(_, lit(str s), _), _) => a[@math=subst[s]] when subst[s]?
	}
}

public list[Box] highlight(Tree t) {
	switch (t) {
		case a:appl(prod(_, lit(str l), _), _): {
			if ((a@math)?) {
				return [MATH(L(a@math))];
			}
			if (/^[a-zA-Z0-9_\-]*$/ := l) { 
				return [KW(L(l))];
			}
			return [L(l)];
		} 

		case appl(prod(_, layouts(_), _), as): 
			return [ highlightLayout(a) | a <- as ];
			
		case a:appl(prod(_, _, attrs([_*, term(category("Constant")), _*])), _):
			return [STRING(L(unparse(a)))];

		case a:appl(prod(_, _, attrs([_*, term(category("Identifier")), _*])), _):
			return [VAR(L(unparse(a)))];
			
		case a:appl(prod(_, _, attrs([_*, \lex(), _*])), _):
			return [L(unparse(a))];
			
		case appl(_, as):
			return [ highlight(a) | a <- as ];

		case amb(_):
			throw "Ambiguous tree: <t>";
			
		default: 
			throw "Unhandled tree <t>";
	}
}

private list[Box] highlightLayout(Tree t) {
	switch (t) {
		case a:appl(prod(_, _, attrs([_*, term(category("Comment")), _*])), _):
			return [COMM(L(unparse(a)))];
			
		case appl(_, as):
			return [ highlightLayout(a) | a <- as ];
			
		case char(n):
			return [L(stringChar(n))];
			
		default:
			throw "Unhandled tree: <t>";
	}
}

