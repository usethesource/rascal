module lang::rascalcore::check::Test2 

import ParseTree;
import lang::rascal::\syntax::Rascal;
//import experiments::Compiler::muRascal::AST; 

loc getValues(Assignable a: (Assignable) `<Assignable receiver>@<Name annotation>`) = 
   a@\loc;
    