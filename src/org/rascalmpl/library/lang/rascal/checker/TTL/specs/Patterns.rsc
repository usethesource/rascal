module lang::rascal::checker::TTL::specs::Patterns

import Type;
import IO;
import lang::rascal::checker::TTL::Library;
import lang::rascal::types::TestChecker;

test bool tst(&T arg1){
    ltype = typeOf(arg1);   
    checkResult = checkStatementsString("X:(<escape(arg1)>) := (<escape(arg1)>);");
    actualType = getTypeForName(checkResult.conf, "X");
    expectedType = ltype;
    return validate(actualType, expectedType, arg1, "XXX");
}
