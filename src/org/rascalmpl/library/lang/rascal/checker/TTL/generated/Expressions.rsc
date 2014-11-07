module lang::rascal::checker::TTL::generated::Expressions
import Prelude;

import lang::rascal::checker::TTL::TTLsyntax;
import lang::rascal::checker::TTL::generated::Signatures;
import lang::rascal::checker::TTL::ExpressionGenerator;

import lang::rascal::types::AbstractName;
import lang::rascal::types::TestChecker;
import lang::rascal::checker::TTL::Library;

import Type;

bool verbose = true;

test bool ExpressionsOK(&T0 arg0){
     ptype = type(typeOf(arg0), ()); 
      if(/Symbol::\void() := ptype.symbol || /\datetime() := ptype.symbol) return true;
     exp = generateExpression(ptype, signatures1, true); 
     if(verbose) println("[ExpressionsOK] <ptype> exp = <exp>");
     checkResult = checkStatementsString(exp);
     println("checkResult = <checkResult>");
     expectedType = ptype.symbol;
     actualType = checkResult.res;
     if(!validate("ExpressionsOK", exp, actualType, expectedType, "expression is supposed to be type correct")){
        return false;
     }
    
     return true;
}


test bool ExpressionsKO(&T0 arg0){
     ptype = type(typeOf(arg0), ()); 
     if(/Symbol::\void() := ptype.symbol || /\datetime() := ptype.symbol) return true;
     exp = generateExpression(ptype, signatures1, false); 
     if(verbose) println("[ExpressionsKO] <ptype> exp = <exp>");
     checkResult = checkStatementsString(exp);
     println("checkResult = <checkResult>");
     expectedType = ptype.symbol;
     actualType = checkResult.res;
     
     if(size(getAllMessages(checkResult)) > 0){
          return true;
     }
     if(invalid("ExpressionsKO", exp, actualType, expectedType, "expression is supposed to be type incorrect")){
        return true;
     } 
	 println("[ExpressionKO] *** wrong type or no warning/error messages for <exp>");
	 return false;
}