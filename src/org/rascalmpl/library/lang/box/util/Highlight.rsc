module lang::box::util::Highlight

import lang::box::util::Box;

import ParseTree;
import String;
import IO;

public list[Box] toBox(Tree t) {
	switch (t) {
		case appl(prod(_, lit(str l), _), _):
			if (/^[a-zA-Z0-9_\-]*$/ := l) { 
				return [KW(L(l))];
			}
			else {
				return [L(l)];
			} 

		case appl(prod(_, layouts(_), _), as): 
			return [ toBoxLayout(a) | a <- as ];
			
		case a:appl(prod(_, _, attrs([_*, term(category("Constant")), _*])), _):
			return [STRING(L(unparse(a)))];

		case a:appl(prod(_, _, attrs([_*, term(category("Identifier")), _*])), _):
			return [VAR(L(unparse(a)))];
			
		case a:appl(prod(_, _, attrs([_*, \lex(), _*])), _):
			return [L(unparse(a))];
			
		case appl(_, as):
			return [ toBox(a) | a <- as ];
			
		default: 
			throw "Unhandled tree <t>";
	}
}

public list[Box] toBoxLayout(Tree t) {
	switch (t) {
		case a:appl(prod(_, _, attrs([_*, term(category("Comment")), _*])), _):
			return [COMM(L(unparse(a)))];
			
		case appl(_, as):
			return [ toBoxLayout(a) | a <- as ];
			
		case char(n):
			return [L(stringChar(n))];
			
		default:
			throw "Unhandled tree: <t>";
	}
}
			
