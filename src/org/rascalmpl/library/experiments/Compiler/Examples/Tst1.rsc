module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;	

value main(list[value] args) {
	return
	checkOK("13;",
		//initialDecls = 
		//["data Tree = char(int character);",
		// "public data TreeSearchResult[&T\<:Tree] = treeFound(&T tree) | treeNotFound();",
		// "public default TreeSearchResult[&T\<:Tree] treeAt(type[&T\<:Tree] t, loc l, Tree root) = treeNotFound();",
		//  "public bool sameType(label(_,Symbol s),Symbol t) = sameType(s,t);"				 
		//				 
		//				 
		//				 ]
		importedModules=["ParseTree"]
			);
}		