module lang::rascal::checker::TTL::generated::Operators
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
// Testing infix BooleanOperators && for bool x bool -> bool 
test bool Operators1(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) && (<escape(arg2)>);";
     if(verbose) println("[Operators1] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators1, expression, actualType, expectedType, arg1, arg2, "signature bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators || for bool x bool -> bool 
test bool Operators2(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) || (<escape(arg2)>);";
     if(verbose) println("[Operators2] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators2, expression, actualType, expectedType, arg1, arg2, "signature bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators \<==\> for bool x bool -> bool 
test bool Operators3(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) \<==\> (<escape(arg2)>);";
     if(verbose) println("[Operators3] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators3, expression, actualType, expectedType, arg1, arg2, "signature bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators ==\> for bool x bool -> bool 
test bool Operators4(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) ==\> (<escape(arg2)>);";
     if(verbose) println("[Operators4] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators4, expression, actualType, expectedType, arg1, arg2, "signature bool x bool -\> bool ");
  }
  return false;
}
// Testing infix Comparison \< for &T x &T -> bool 
test bool Operators5(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) \< (<escape(arg2)>);";
     if(verbose) println("[Operators5] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators5, expression, actualType, expectedType, arg1, arg2, "signature &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \<= for &T x &T -> bool 
test bool Operators6(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) \<= (<escape(arg2)>);";
     if(verbose) println("[Operators6] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators6, expression, actualType, expectedType, arg1, arg2, "signature &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison == for &T x &T -> bool 
test bool Operators7(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) == (<escape(arg2)>);";
     if(verbose) println("[Operators7] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators7, expression, actualType, expectedType, arg1, arg2, "signature &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \>= for &T x &T -> bool 
test bool Operators8(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) \>= (<escape(arg2)>);";
     if(verbose) println("[Operators8] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators8, expression, actualType, expectedType, arg1, arg2, "signature &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \> for &T x &T -> bool 
test bool Operators9(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) \> (<escape(arg2)>);";
     if(verbose) println("[Operators9] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(Operators9, expression, actualType, expectedType, arg1, arg2, "signature &T x &T -\> bool ");
  }
  return false;
}
// Testing prefix Negation ! for bool -> bool 
test bool Operators10(bool arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators10] exp: " + expression);
	    checkResult = checkStatementsString("! (<escape(arg1)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), lbindings);
     return validate(Operators10, "! (<escape(arg1)>);", actualType, expectedType, arg1, "signature bool -\> bool ");
  }
  return false;
}
// Testing infix Addition + for &L <: num x &R <: num               -> LUB(&L, &R)
test bool Operators11(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators11] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(Operators11, expression, actualType, expectedType, arg1, arg2, "signature &L \<: num x &R \<: num               -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Addition + for list[&L] x list[&R]                 -> list[LUB(&L,&R)]
test bool Operators12(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators12] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators12, expression, actualType, expectedType, arg1, arg2, "signature list[&L] x list[&R]                 -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Addition + for list[&L] x &R              		  -> list[LUB(&L,&R)] when &R is not a list
test bool Operators13(list[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators13] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators13, expression, actualType, expectedType, arg1, arg2, "signature list[&L] x &R              		  -\> list[LUB(&L,&R)] when &R is not a list");
  }
  return false;
}
// Testing infix Addition + for &L x list[&R <: &L]                 -> list[LUB(&L,&R)] when &L is not a list
test bool Operators14(&L arg1, list[&R <: &L] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \parameter("L", \value()))), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators14] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators14, expression, actualType, expectedType, arg1, arg2, "signature &L x list[&R \<: &L]                 -\> list[LUB(&L,&R)] when &L is not a list");
  }
  return false;
}
// Testing infix Addition + for set[&L] x set[&R]                   -> set[LUB(&L,&R)]
test bool Operators15(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators15] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators15, expression, actualType, expectedType, arg1, arg2, "signature set[&L] x set[&R]                   -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Addition + for set[&L] x &R                        -> set[LUB(&L,&R)] when &R is not a list
test bool Operators16(set[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators16] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators16, expression, actualType, expectedType, arg1, arg2, "signature set[&L] x &R                        -\> set[LUB(&L,&R)] when &R is not a list");
  }
  return false;
}
// Testing infix Addition + for &L x set[&R]                        -> set[LUB(&L,&R)] when &L is not a list
test bool Operators17(&L arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators17] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators17, expression, actualType, expectedType, arg1, arg2, "signature &L x set[&R]                        -\> set[LUB(&L,&R)] when &L is not a list");
  }
  return false;
}
// Testing infix Addition + for map[&K1,&V1] x map[&K2,&V2]         -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
test bool Operators18(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators18] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(Operators18, expression, actualType, expectedType, arg1, arg2, "signature map[&K1,&V1] x map[&K2,&V2]         -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]");
  }
  return false;
}
// Testing infix Addition + for str x str                           -> str
test bool Operators19(str arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\str(), ltype);
  
  <rmatches, rbindings> = bind(\str(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators19] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\str(), bindings);
     return validate(Operators19, expression, actualType, expectedType, arg1, arg2, "signature str x str                           -\> str");
  }
  return false;
}
// Testing infix Addition + for loc x str                           -> loc
test bool Operators20(loc arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\loc(), ltype);
  
  <rmatches, rbindings> = bind(\str(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators20] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\loc(), bindings);
     return validate(Operators20, expression, actualType, expectedType, arg1, arg2, "signature loc x str                           -\> loc");
  }
  return false;
}
// Testing infix Addition + for tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -> tuple[&L1,&L2,&R1,&R2,&R3]
	
test bool Operators21(tuple[&L1,&L2] arg1, tuple[&R1,&R2,&R3] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\tuple([\parameter("L1", \value()),\parameter("L2", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\tuple([\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) + (<escape(arg2)>);";
     if(verbose) println("[Operators21] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\tuple([\parameter("L1", \value()),\parameter("L2", \value()),\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), bindings);
     return validate(Operators21, expression, actualType, expectedType, arg1, arg2, "signature tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -\> tuple[&L1,&L2,&R1,&R2,&R3]
	");
  }
  return false;
}
// Testing infix Difference - for &L <: num x &R <: num                -> LUB(&L, &R)
test bool Operators22(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) - (<escape(arg2)>);";
     if(verbose) println("[Operators22] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(Operators22, expression, actualType, expectedType, arg1, arg2, "signature &L \<: num x &R \<: num                -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Difference - for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool Operators23(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) - (<escape(arg2)>);";
     if(verbose) println("[Operators23] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators23, expression, actualType, expectedType, arg1, arg2, "signature list[&L] x list[&R]                  -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Difference - for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool Operators24(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) - (<escape(arg2)>);";
     if(verbose) println("[Operators24] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators24, expression, actualType, expectedType, arg1, arg2, "signature set[&L] x set[&R]                    -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Difference - for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool Operators25(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) - (<escape(arg2)>);";
     if(verbose) println("[Operators25] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(Operators25, expression, actualType, expectedType, arg1, arg2, "signature map[&K1,&V1] x map[&K2,&V2]          -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]
");
  }
  return false;
}
// Testing infix Product * for &L <: num x &R <: num                -> LUB(&L, &R)
test bool Operators26(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) * (<escape(arg2)>);";
     if(verbose) println("[Operators26] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(Operators26, expression, actualType, expectedType, arg1, arg2, "signature &L \<: num x &R \<: num                -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Product * for list[&L] x list[&R]                  -> lrel[&L,&R]
test bool Operators27(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) * (<escape(arg2)>);";
     if(verbose) println("[Operators27] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
     return validate(Operators27, expression, actualType, expectedType, arg1, arg2, "signature list[&L] x list[&R]                  -\> lrel[&L,&R]");
  }
  return false;
}
// Testing infix Product * for set[&L] x set[&R]                    -> rel[&L,&R]

test bool Operators28(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) * (<escape(arg2)>);";
     if(verbose) println("[Operators28] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
     return validate(Operators28, expression, actualType, expectedType, arg1, arg2, "signature set[&L] x set[&R]                    -\> rel[&L,&R]
");
  }
  return false;
}
// Testing infix Intersection & for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool Operators29(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) & (<escape(arg2)>);";
     if(verbose) println("[Operators29] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators29, expression, actualType, expectedType, arg1, arg2, "signature list[&L] x list[&R]                  -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Intersection & for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool Operators30(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) & (<escape(arg2)>);";
     if(verbose) println("[Operators30] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(Operators30, expression, actualType, expectedType, arg1, arg2, "signature set[&L] x set[&R]                    -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Intersection & for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool Operators31(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) & (<escape(arg2)>);";
     if(verbose) println("[Operators31] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(Operators31, expression, actualType, expectedType, arg1, arg2, "signature map[&K1,&V1] x map[&K2,&V2]          -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]
");
  }
  return false;
}
// Testing prefix UnaryMinus - for &L <: num -> &L 
test bool Operators32(&L <: num arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators32] exp: " + expression);
	    checkResult = checkStatementsString("- (<escape(arg1)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\parameter("L", \value()), lbindings);
     return validate(Operators32, "- (<escape(arg1)>);", actualType, expectedType, arg1, "signature &L \<: num -\> &L ");
  }
  return false;
}
// Testing infix Modulo % for int x int -> int 
test bool Operators33(int arg1, int arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\int(), ltype);
  
  <rmatches, rbindings> = bind(\int(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) % (<escape(arg2)>);";
     if(verbose) println("[Operators33] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\int(), bindings);
     return validate(Operators33, expression, actualType, expectedType, arg1, arg2, "signature int x int -\> int ");
  }
  return false;
}
// Testing infix Division / for &L <: num x &R <: num        -> LUB(&L, &R) 
test bool Operators34(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) / (<escape(arg2)>);";
     if(verbose) println("[Operators34] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(Operators34, expression, actualType, expectedType, arg1, arg2, "signature &L \<: num x &R \<: num        -\> LUB(&L, &R) ");
  }
  return false;
}
// Testing postfix Closure + for lrel[&L,&L]			-> lrel[&L,&L]
test bool Operators35(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators35] exp: " + expression);
	    checkResult = checkStatementsString("(<escape(arg1)>) +;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(Operators35, "(<escape(arg1)>) +;", actualType, expectedType, arg1, "signature lrel[&L,&L]			-\> lrel[&L,&L]");
  }
  return false;
}
// Testing postfix Closure + for rel[&L,&L]  			-> rel[&L,&L]

test bool Operators36(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators36] exp: " + expression);
	    checkResult = checkStatementsString("(<escape(arg1)>) +;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(Operators36, "(<escape(arg1)>) +;", actualType, expectedType, arg1, "signature rel[&L,&L]  			-\> rel[&L,&L]
");
  }
  return false;
}
// Testing postfix Closure * for lrel[&L,&L]			-> lrel[&L,&L]
test bool Operators37(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators37] exp: " + expression);
	    checkResult = checkStatementsString("(<escape(arg1)>) *;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(Operators37, "(<escape(arg1)>) *;", actualType, expectedType, arg1, "signature lrel[&L,&L]			-\> lrel[&L,&L]");
  }
  return false;
}
// Testing postfix Closure * for rel[&L,&L]  			-> rel[&L,&L]

test bool Operators38(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
     if(verbose) println("[Operators38] exp: " + expression);
	    checkResult = checkStatementsString("(<escape(arg1)>) *;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(Operators38, "(<escape(arg1)>) *;", actualType, expectedType, arg1, "signature rel[&L,&L]  			-\> rel[&L,&L]
");
  }
  return false;
}
// Testing infix Composition o for lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C]
test bool Operators39(lrel[&A,&B] arg1, lrel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\lrel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) o (<escape(arg2)>);";
     if(verbose) println("[Operators39] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
     return validate(Operators39, expression, actualType, expectedType, arg1, arg2, "signature lrel[&A,&B] x lrel[&B,&C] -\> lrel[&A,&C]");
  }
  return false;
}
// Testing infix Composition o for rel[&A,&B] x rel[&B,&C] -> rel[&A,&C]
test bool Operators40(rel[&A,&B] arg1, rel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\rel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) o (<escape(arg2)>);";
     if(verbose) println("[Operators40] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
     return validate(Operators40, expression, actualType, expectedType, arg1, arg2, "signature rel[&A,&B] x rel[&B,&C] -\> rel[&A,&C]");
  }
  return false;
}
// Testing infix Composition o for map[&A,&B] x map[&B,&C] -> map[&A,&C]

test bool Operators41(map[&A,&B] arg1, map[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("A", \value()),\parameter("B", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("B", \value()),\parameter("C", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     expression = "(<escape(arg1)>) o (<escape(arg2)>);";
     if(verbose) println("[Operators41] exp: <expression>");
     checkResult = checkStatementsString(expression, importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\parameter("A", \value()),\parameter("C", \value())), bindings);
     return validate(Operators41, expression, actualType, expectedType, arg1, arg2, "signature map[&A,&B] x map[&B,&C] -\> map[&A,&C]
");
  }
  return false;
}

