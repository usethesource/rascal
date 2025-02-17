module lang::rascalcore::compile::Examples::Tst1

/*
    Perform a path analysis on the Rascal source code of a function.
    On the fly it will report dead code.
*/

extend lang::rascalcore::check::CheckerCommon;
 
import lang::rascal::\syntax::Rascal;

// import String;

/********************************************************************/
/*       Return path analysis                                       */
/********************************************************************/


bool returnsViaAllPath((Statement) `<Label label> switch ( <Expression expression> ) { <Case+ cases> }`)
    = true;