module lang::java::style::Miscellaneous

import analysis::m3::Core;
import lang::java::m3::Core;
import lang::java::m3::AST;
import Message;
import String;
import List;

import lang::java::jdt::m3::Core;		// Java specific modules
import lang::java::jdt::m3::AST;

import lang::java::style::Utils;

import IO;

data Message = miscellaneous(str category, loc pos);

/*
NewlineAtEndOfFile	TBD
TodoComment			TBD		// Note comments only partially available
Translation			TBD
UncommentedMain		DONE
UpperEll			TBD
ArrayTypeStyle		TBD
FinalParameters		TBD
DescendantToken		TBD
Indentation			TBD
TrailingComment		TBD
Regexp				TBD
OuterTypeFilename	DONE
UniqueProperties	TBD
*/

/* --- unCommentedMain ------------------------------------------------------*/

list[Message] unCommentedMain(Declaration m: \method(_,str name,_,_,_),  list[Declaration] parents, M3 model) =
	name == "main" ? [miscellaneous("UnCommentedMain", m@src)] : [];
	
list[Message] unCommentedMain(Declaration m: \method(_,str name,_,_),  list[Declaration] parents, M3 model) =
	name == "main" ? [miscellaneous("UnCommentedMain", m@src)] : [];

default list[Message] unCommentedMain(Declaration d,  list[Declaration] parents, M3 model) =	[];

/* --- outerTypeFilename ----------------------------------------------------*/

str getPath(loc l){
	p = l.path;
	if(endsWith(p, ".java")){
		p = p[..-5];
	}
	return p;
}

bool isOuterType(list[Declaration] parents) =
size(parents) == 0 || compilationUnit(_,_) := head(parents) || compilationUnit(_,_,_) := head(parents);

list[Message] outerTypeFilename(Declaration d,  list[Declaration] parents, M3 model) =
	isOuterType(parents) && \public() in (model@modifiers[d@decl]) && !endsWith(getPath(parents[0]@decl), getPath(d@decl))
	? [miscellaneous("OuterTypeFilename", d@decl) ] : [];

//// TODO: model@containment<1,0>)[ast@decl] is ambiguous!

