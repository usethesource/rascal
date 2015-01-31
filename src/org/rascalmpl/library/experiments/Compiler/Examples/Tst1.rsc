module experiments::Compiler::Examples::Tst1

import lang::rascal::tests::types::StaticTestingUtils;
//import lang::rascal::\syntax::Rascal;
//import ParseTree;
import IO;


value main(list[value] args)  = declarationError("A x = 0;", initialDecls=["alias A = str;", "alias A = int;"]);
//value main(list[value] args) { 
//	pt = parse(#start[Module],"module M\nimport List;");
//	//iprintln(pt);
//	println("pt has top: <pt has top>");
//	println("Module m := pt.top: <Module m := pt.top>");
//	return true;
//}
	

//checkOK("x;", initialDecls=["int x = 5;"]);

//#start[A];

//parse(#start[Module],"module M\nimport List;");


//checkOK("x;", initialDecls=["int x = 5;"]);
