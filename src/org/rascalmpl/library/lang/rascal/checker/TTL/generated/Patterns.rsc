module lang::rascal::checker::TTL::generated::Patterns
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import List;
import Set;
import Message;
import util::Eval;
import lang::rascal::types::AbstractName;
import lang::rascal::types::TestChecker;
import lang::rascal::checker::TTL::PatternGenerator;
public bool verbose = true;
// Testing _P0 := _V0
test bool Patterns42(&T0 arg0){
     ptypes = [type(typeOf(arg0), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 := _V0;", penv);
     if(verbose) println("[Patterns42] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns42] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns42", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns42] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}
// Testing _P0 !:= _V0
test bool Patterns43(&T0 arg0){
     ptypes = [type(typeOf(arg0), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 !:= _V0;", penv);
     if(verbose) println("[Patterns43] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns43] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns43", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns43] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}
// Testing _P0 := V0 && _P1 := V1
test bool Patterns44(&T0 arg0, &T1 arg1){
     ptypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 := V0 && _P1 := V1;", penv);
     if(verbose) println("[Patterns44] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns44] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns44", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns44] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}
// Testing _P0 := _V0 || _P1 := _V1
test bool Patterns45(&T0 arg0, &T1 arg1){
     ptypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 := _V0 || _P1 := _V1;", penv);
     if(verbose) println("[Patterns45] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns45] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns45", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns45] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}
// Testing _P0 := _V0 ==> _P1 := _V1
test bool Patterns46(&T0 arg0, &T1 arg1){
     ptypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 := _V0 ==\> _P1 := _V1;", penv);
     if(verbose) println("[Patterns46] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns46] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns46", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns46] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}
// Testing _P0 := _V0 <==> _P1 := _V1
test bool Patterns47(&T0 arg0, &T1 arg1){
     ptypes = [type(typeOf(arg0), ()), type(typeOf(arg1), ())]; 
     <penv, expected_pvars> = generatePatterns(ptypes); 
     exp = buildExpr("_P0 := _V0 \<==\> _P1 := _V1;", penv);
     if(verbose) println("[Patterns47] exp = <exp>");
     checkResult = checkStatementsString(exp);

     pvars = getPatternVariables(checkResult.conf);

     for(v <- expected_pvars){
         try {
             expectedType = expected_pvars[v].symbol;
             actualType = pvars[RSimpleName(v)];
             if(inferred(_) := actualType){
                 println("[Patterns47] *** No type found for variable <v>; expected type <expectedType>; exp = <exp>");
             } else {
             if(!validate("Patterns47", exp, actualType, expectedType, "variable <v>"))
                return false;
             }
         } catch: {
           println("[Patterns47] *** Variable <v> unbound; expected type <expected_pvars[v].symbol>; exp = <exp>");
           return false;
         }
     }
     return true;
}

