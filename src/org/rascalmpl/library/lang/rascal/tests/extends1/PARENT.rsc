module lang::rascal::tests::extends::PARENT

import lang::rascal::tests::extends::PARSETREE;
import lang::rascal::tests::extends::TYPE;

test bool dontShareANameResolutionCache1() =
   lang::rascal::tests::extends::TYPE::comparable(A(),B()) &&
   lang::rascal::tests::extends::PARSETREE::comparable(A(),C());


test bool dontShareANameResolutionCache2() =
   lang::rascal::tests::extends::PARSETREE::comparable(A(),C()) &&
   lang::rascal::tests::extends::TYPE::comparable(A(),B());
   
   
