module lang::rascal::tests::extends1::PARENT

import lang::rascal::tests::extends1::PARSETREE;
import lang::rascal::tests::extends1::TYPE;

test bool dontShareANameResolutionCache1() =
   lang::rascal::tests::extends::TYPE::comparable(A(),B()) &&
   lang::rascal::tests::extends::PARSETREE::comparable(A(),C());


test bool dontShareANameResolutionCache2() =
   lang::rascal::tests::extends::PARSETREE::comparable(A(),C()) &&
   lang::rascal::tests::extends::TYPE::comparable(A(),B());
   
   
