module lang::rascal::checker::TTL::specs::Patterns

import Type;
import IO;
import String;
import lang::rascal::checker::TTL::Library;
import lang::rascal::types::AbstractName;
import lang::rascal::types::TestChecker;
import lang::rascal::checker::TTL::PatternGenerator;

/*
test bool tst(&T arg1){
    ltype = typeOf(arg1); 
    println("tst: arg1 = <arg1>; ltype = <ltype>");
    exp = "X:<escape(arg1)> := <escape(arg1)>;";
    println("tst: exp = <exp>");
    checkResult = checkStatementsString(exp);
    pvars = getPatternVariables(checkResult.conf);
    println("tst: pvars = <pvars>");
    actualType = pvars[RSimpleName("X")];
    expectedType = ltype;
    return validate(actualType, expectedType, arg1, "exp = <exp>");
}
*/

test bool tst6(&T arg1){
    ptype = typeOf(arg1); 
    println("tst6: arg1 = <arg1>; ptype = <ptype>");
    pv = generatePattern(type(ptype, ())); 
    println("tst6: pv = <pv>"); 
    expected_pvars = pv.env;
    exp = "<pv.pat> := <escape(pv.val)>;";
    println("tst6: exp = <exp>");
    checkResult = checkStatementsString(exp);
    println("checkResult = <checkResult>");
    pvars = getPatternVariables(checkResult.conf);
    println("pvars = <pvars>");
    for(v <- expected_pvars, contains(pv.pat, v)){
        try {
            actualType = pvars[RSimpleName(v)];
            if(inferred(_) := actualType){
               println("No type found; exp = <exp>");
            } else {
              expectedType = typeOf(expected_pvars[v]);
              if(!validate(actualType, expectedType, arg1, "variable <v>; exp = <exp>"))
                 return false;
            }
        } catch: {
            println("Variable <v> unbound; exp = <exp>");
            return false;
        }
    }
    return true;
}
