module lang::rascal::checker::TTL::generated::TmpOperators
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import util::Eval;
import lang::rascal::types::TestChecker;
// Testing infix BooleanOperators && for bool x bool -> bool 
test bool tst1(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) && (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "BooleanOperators && for bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators || for bool x bool -> bool 
test bool tst2(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) || (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "BooleanOperators || for bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators \<==\> for bool x bool -> bool 
test bool tst3(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) \<==\> (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "BooleanOperators \\\<==\\\> for bool x bool -\> bool ");
  }
  return false;
}
// Testing infix BooleanOperators ==\> for bool x bool -> bool 
test bool tst4(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  <rmatches, rbindings> = bind(\bool(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) ==\> (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "BooleanOperators ==\\\> for bool x bool -\> bool ");
  }
  return false;
}
// Testing infix Comparison \< for &T x &T -> bool 
test bool tst5(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) \< (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Comparison \\\< for &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \<= for &T x &T -> bool 
test bool tst6(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) \<= (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Comparison \\\<= for &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison == for &T x &T -> bool 
test bool tst7(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) == (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Comparison == for &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \>= for &T x &T -> bool 
test bool tst8(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) \>= (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Comparison \\\>= for &T x &T -\> bool ");
  }
  return false;
}
// Testing infix Comparison \> for &T x &T -> bool 
test bool tst9(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) \> (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Comparison \\\> for &T x &T -\> bool ");
  }
  return false;
}
// Testing prefix Negation ! for bool -> bool 
test bool tst10(bool arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("! (<escape(arg1)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\bool(), lbindings);
     return validate(actualType, expectedType, arg1, "Negation ! for bool -\> bool ");
  }
  return false;
}
// Testing infix Addition + for &L <: num x &R <: num               -> LUB(&L, &R)
test bool tst11(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for &L \<: num x &R \<: num               -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Addition + for list[&L] x list[&R]                 -> list[LUB(&L,&R)]
test bool tst12(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for list[&L] x list[&R]                 -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Addition + for list[&L] x &R              		  -> list[LUB(&L,&R)] when &R is not a list
test bool tst13(list[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for list[&L] x &R              		  -\> list[LUB(&L,&R)] when &R is not a list");
  }
  return false;
}
// Testing infix Addition + for &L x list[&R <: &L]                 -> list[LUB(&L,&R)] when &L is not a list
test bool tst14(&L arg1, list[&R <: &L] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \parameter("L", \value()))), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for &L x list[&R \<: &L]                 -\> list[LUB(&L,&R)] when &L is not a list");
  }
  return false;
}
// Testing infix Addition + for set[&L] x set[&R]                   -> set[LUB(&L,&R)]
test bool tst15(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for set[&L] x set[&R]                   -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Addition + for set[&L] x &R                        -> set[LUB(&L,&R)] when &R is not a list
test bool tst16(set[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for set[&L] x &R                        -\> set[LUB(&L,&R)] when &R is not a list");
  }
  return false;
}
// Testing infix Addition + for &L x set[&R]                        -> set[LUB(&L,&R)] when &L is not a list
test bool tst17(&L arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for &L x set[&R]                        -\> set[LUB(&L,&R)] when &L is not a list");
  }
  return false;
}
// Testing infix Addition + for map[&K1,&V1] x map[&K2,&V2]         -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
test bool tst18(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for map[&K1,&V1] x map[&K2,&V2]         -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]");
  }
  return false;
}
// Testing infix Addition + for str x str                           -> str
test bool tst19(str arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\str(), ltype);
  
  <rmatches, rbindings> = bind(\str(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\str(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for str x str                           -\> str");
  }
  return false;
}
// Testing infix Addition + for loc x str                           -> loc
test bool tst20(loc arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\loc(), ltype);
  
  <rmatches, rbindings> = bind(\str(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\loc(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for loc x str                           -\> loc");
  }
  return false;
}
// Testing infix Addition + for tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -> tuple[&L1,&L2,&R1,&R2,&R3]
	
test bool tst21(tuple[&L1,&L2] arg1, tuple[&R1,&R2,&R3] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\tuple([\parameter("L1", \value()),\parameter("L2", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\tuple([\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) + (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\tuple([\parameter("L1", \value()),\parameter("L2", \value()),\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Addition + for tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -\> tuple[&L1,&L2,&R1,&R2,&R3]
                                                           	");
  }
  return false;
}
// Testing infix Difference - for &L <: num x &R <: num                -> LUB(&L, &R)
test bool tst22(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) - (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Difference - for &L \<: num x &R \<: num                -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Difference - for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool tst23(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) - (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Difference - for list[&L] x list[&R]                  -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Difference - for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool tst24(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) - (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Difference - for set[&L] x set[&R]                    -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Difference - for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool tst25(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) - (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Difference - for map[&K1,&V1] x map[&K2,&V2]          -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]
                                                           ");
  }
  return false;
}
// Testing infix Product * for &L <: num x &R <: num                -> LUB(&L, &R)
test bool tst26(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) * (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Product * for &L \<: num x &R \<: num                -\> LUB(&L, &R)");
  }
  return false;
}
// Testing infix Product * for list[&L] x list[&R]                  -> lrel[&L,&R]
test bool tst27(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) * (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Product * for list[&L] x list[&R]                  -\> lrel[&L,&R]");
  }
  return false;
}
// Testing infix Product * for set[&L] x set[&R]                    -> rel[&L,&R]

test bool tst28(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) * (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Product * for set[&L] x set[&R]                    -\> rel[&L,&R]
                                                           ");
  }
  return false;
}
// Testing infix Intersection & for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool tst29(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) & (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Intersection & for list[&L] x list[&R]                  -\> list[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Intersection & for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool tst30(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) & (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Intersection & for set[&L] x set[&R]                    -\> set[LUB(&L,&R)]");
  }
  return false;
}
// Testing infix Intersection & for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool tst31(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) & (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Intersection & for map[&K1,&V1] x map[&K2,&V2]          -\> map[LUB(&K1,&K2), LUB(&V1,&V2)]
                                                           ");
  }
  return false;
}
// Testing prefix UnaryMinus - for &L <: num -> &L 
test bool tst32(&L <: num arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("- (<escape(arg1)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\parameter("L", \value()), lbindings);
     return validate(actualType, expectedType, arg1, "UnaryMinus - for &L \<: num -\> &L ");
  }
  return false;
}
// Testing infix Modulo % for int x int -> int 
test bool tst33(int arg1, int arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\int(), ltype);
  
  <rmatches, rbindings> = bind(\int(), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) % (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\int(), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Modulo % for int x int -\> int ");
  }
  return false;
}
// Testing infix Division / for &L <: num x &R <: num        -> LUB(&L, &R) 
test bool tst34(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) / (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Division / for &L \<: num x &R \<: num        -\> LUB(&L, &R) ");
  }
  return false;
}
// Testing postfix Closure + for lrel[&L,&L]			-> lrel[&L,&L]
test bool tst35(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("(<escape(arg1)>) +;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(actualType, expectedType, arg1, "Closure + for lrel[&L,&L]			-\> lrel[&L,&L]");
  }
  return false;
}
// Testing postfix Closure + for rel[&L,&L]  			-> rel[&L,&L]

test bool tst36(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("(<escape(arg1)>) +;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(actualType, expectedType, arg1, "Closure + for rel[&L,&L]  			-\> rel[&L,&L]
                                                     ");
  }
  return false;
}
// Testing postfix Closure * for lrel[&L,&L]			-> lrel[&L,&L]
test bool tst37(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("(<escape(arg1)>) *;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(actualType, expectedType, arg1, "Closure * for lrel[&L,&L]			-\> lrel[&L,&L]");
  }
  return false;
}
// Testing postfix Closure * for rel[&L,&L]  			-> rel[&L,&L]

test bool tst38(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  
  if(lmatches){
     
     
	    checkResult = checkStatementsString("(<escape(arg1)>) *;", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
     return validate(actualType, expectedType, arg1, "Closure * for rel[&L,&L]  			-\> rel[&L,&L]
                                                     ");
  }
  return false;
}
// Testing infix Composition o for lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C]
test bool tst39(lrel[&A,&B] arg1, lrel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\lrel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) o (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\lrel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Composition o for lrel[&A,&B] x lrel[&B,&C] -\> lrel[&A,&C]");
  }
  return false;
}
// Testing infix Composition o for rel[&A,&B] x rel[&B,&C] -> rel[&A,&C]
test bool tst40(rel[&A,&B] arg1, rel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  
  <rmatches, rbindings> = bind(\rel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) o (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\rel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Composition o for rel[&A,&B] x rel[&B,&C] -\> rel[&A,&C]");
  }
  return false;
}
// Testing infix Composition o for map[&A,&B] x map[&B,&C] -> map[&A,&C]

test bool tst41(map[&A,&B] arg1, map[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("A", \value()),\parameter("B", \value())), ltype);
  
  <rmatches, rbindings> = bind(\map(\parameter("B", \value()),\parameter("C", \value())), rtype);
  
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     checkResult = checkStatementsString("(<escape(arg1)>) o (<escape(arg2)>);", importedModules=[], initialDecls = []); // apply the operator to its arguments
     actualType = checkResult.res; 
     expectedType = normalize(\map(\parameter("A", \value()),\parameter("C", \value())), bindings);
     return validate(actualType, expectedType, arg1, arg2, "Composition o for map[&A,&B] x map[&B,&C] -\> map[&A,&C]
                                                           ");
  }
  return false;
}

