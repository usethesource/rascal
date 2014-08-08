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
TodoComment			TBD
Translation			TBD
UncommentedMain		DONE
UpperEll			TBD
ArrayTypeStyle		TBD
FinalParameters		TBD
DescendantToken		TBD
Indentation			TBD
TrailingComment		TBD
Regexp				TBD
OuterTypeFilename	TBD
UniqueProperties	TBD
*/

list[Message] miscellaneousChecks(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) {
	return
		  unCommentedMain(ast, model, classDeclarations, methodDeclarations)
		+ outerTypeFilename(ast, model, classDeclarations, methodDeclarations)
		;
}

list[Message] unCommentedMain(node ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) =
	[miscellaneous("UnCommentedMain", m@src)  | m <- methodDeclarations, m.name == "main"];
	
// model@containment<1,0>)[ast@decl] is ambiguous!

list[Message] outerTypeFilename(Declaration ast, M3 model, list[Declaration] classDeclarations, list[Declaration] methodDeclarations) =
    [miscellaneous("outerTypeFilename", innerDecl)  | innerDecl <- (model@containment)[ast@decl] , 
    											  \public() in model@modifiers[innerDecl],
												  !endsWith(ast@decl.path[..-5], innerDecl.path) ];

