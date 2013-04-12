module lang::rascal::checker::TTL::generated::Operators
import lang::rascal::checker::TTL::Library;
import Type;
import IO;
import util::Eval;
import lang::rascal::types::TestChecker;
// Testing infix BooleanOperators && for bool x bool -> bool 
test bool tst(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  // tp = \bool(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\bool(), rtype);
  // tp = \bool(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) && (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix BooleanOperators || for bool x bool -> bool 
test bool tst(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  // tp = \bool(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\bool(), rtype);
  // tp = \bool(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) || (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix BooleanOperators \<==\> for bool x bool -> bool 
test bool tst(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  // tp = \bool(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\bool(), rtype);
  // tp = \bool(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) \<==\> (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix BooleanOperators ==\> for bool x bool -> bool 
test bool tst(bool arg1, bool arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  // tp = \bool(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\bool(), rtype);
  // tp = \bool(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) ==\> (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Comparison \< for &T x &T -> bool 
test bool tst(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  // tp = \parameter("T", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  // tp = \parameter("T", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) \< (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Comparison \<= for &T x &T -> bool 
test bool tst(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  // tp = \parameter("T", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  // tp = \parameter("T", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) \<= (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Comparison == for &T x &T -> bool 
test bool tst(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  // tp = \parameter("T", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  // tp = \parameter("T", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) == (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Comparison \>= for &T x &T -> bool 
test bool tst(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  // tp = \parameter("T", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  // tp = \parameter("T", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) \>= (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Comparison \> for &T x &T -> bool 
test bool tst(&T arg1, &T arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("T", \value()), ltype);
  // tp = \parameter("T", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("T", \value()), rtype);
  // tp = \parameter("T", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) \> (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\bool(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing prefix Negation !
test bool tst(bool arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\bool(), ltype);
  // tp = \bool(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("! (<escape(arg1)>);")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\bool(), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing infix Addition + for &L <: num x &R <: num               -> LUB(&L, &R)
test bool tst(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  // tp = \parameter("L", \num()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  // tp = \parameter("R", \num()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for list[&L] x list[&R]                 -> list[LUB(&L,&R)]
test bool tst(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  // tp = \list(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  // tp = \list(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for list[&L] x &R              		  -> list[LUB(&L,&R)] when &R is not a list
test bool tst(list[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  // tp = \list(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  // tp = \parameter("R", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for &L x list[&R <: &L]                 -> list[LUB(&L,&R)] when &L is not a list
test bool tst(&L arg1, list[&R <: &L] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  // tp = \parameter("L", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\list(\parameter("R", \parameter("L", \value()))), rtype);
  // tp = \list(\parameter("R", \parameter("L", \value()))); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for set[&L] x set[&R]                   -> set[LUB(&L,&R)]
test bool tst(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  // tp = \set(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  // tp = \set(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for set[&L] x &R                        -> set[LUB(&L,&R)] when &R is not a list
test bool tst(set[&L] arg1, &R arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  // tp = \set(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \value()), rtype);
  // tp = \parameter("R", \value()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["R"])) return true;
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for &L x set[&R]                        -> set[LUB(&L,&R)] when &L is not a list
test bool tst(&L arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \value()), ltype);
  // tp = \parameter("L", \value()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  // tp = \set(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     if(isListType(bindings["L"])) return true;
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for map[&K1,&V1] x map[&K2,&V2]         -> map[LUB(&K1,&K2), LUB(&V1,&V2)]
test bool tst(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  // tp = \map(\parameter("K1", \value()),\parameter("V1", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  // tp = \map(\parameter("K2", \value()),\parameter("V2", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for str x str                           -> str
test bool tst(str arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\str(), ltype);
  // tp = \str(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\str(), rtype);
  // tp = \str(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\str(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for loc x str                           -> loc
test bool tst(loc arg1, str arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\loc(), ltype);
  // tp = \loc(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\str(), rtype);
  // tp = \str(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\loc(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Addition + for tuple[&L1,&L2] x tuple[&R1,&R2,&R3] -> tuple[&L1,&L2,&R1,&R2,&R3]
	
test bool tst(tuple[&L1,&L2] arg1, tuple[&R1,&R2,&R3] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\tuple([\parameter("L1", \value()),\parameter("L2", \value())]), ltype);
  // tp = \tuple([\parameter("L1", \value()),\parameter("L2", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\tuple([\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), rtype);
  // tp = \tuple([\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) + (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\tuple([\parameter("L1", \value()),\parameter("L2", \value()),\parameter("R1", \value()),\parameter("R2", \value()),\parameter("R3", \value())]), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Difference - for &L <: num x &R <: num                -> LUB(&L, &R)
test bool tst(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  // tp = \parameter("L", \num()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  // tp = \parameter("R", \num()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) - (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Difference - for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool tst(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  // tp = \list(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  // tp = \list(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) - (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Difference - for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool tst(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  // tp = \set(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  // tp = \set(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) - (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Difference - for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool tst(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  // tp = \map(\parameter("K1", \value()),\parameter("V1", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  // tp = \map(\parameter("K2", \value()),\parameter("V2", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) - (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Product * for &L <: num x &R <: num                -> LUB(&L, &R)
test bool tst(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  // tp = \parameter("L", \num()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  // tp = \parameter("R", \num()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) * (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Product * for list[&L] x list[&R]                  -> lrel[&L,&R]
test bool tst(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  // tp = \list(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  // tp = \list(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) * (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Product * for set[&L] x set[&R]                    -> rel[&L,&R]

test bool tst(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  // tp = \set(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  // tp = \set(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) * (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\rel([\parameter("L", \value()),\parameter("R", \value())]), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Intersection & for list[&L] x list[&R]                  -> list[LUB(&L,&R)]
test bool tst(list[&L] arg1, list[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\list(\parameter("L", \value())), ltype);
  // tp = \list(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\list(\parameter("R", \value())), rtype);
  // tp = \list(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) & (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\list(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Intersection & for set[&L] x set[&R]                    -> set[LUB(&L,&R)]
test bool tst(set[&L] arg1, set[&R] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\set(\parameter("L", \value())), ltype);
  // tp = \set(\parameter("L", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\set(\parameter("R", \value())), rtype);
  // tp = \set(\parameter("R", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) & (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\set(\LUB(\parameter("L", \value()),\parameter("R", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Intersection & for map[&K1,&V1] x map[&K2,&V2]          -> map[LUB(&K1,&K2), LUB(&V1,&V2)]

test bool tst(map[&K1,&V1] arg1, map[&K2,&V2] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("K1", \value()),\parameter("V1", \value())), ltype);
  // tp = \map(\parameter("K1", \value()),\parameter("V1", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\map(\parameter("K2", \value()),\parameter("V2", \value())), rtype);
  // tp = \map(\parameter("K2", \value()),\parameter("V2", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) & (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\map(\LUB(\parameter("K1", \value()),\parameter("K2", \value())),\LUB(\parameter("V1", \value()),\parameter("V2", \value()))), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing prefix UnaryMinus -
test bool tst(&L <: num arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  // tp = \parameter("L", \num()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("- (<escape(arg1)>);")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\parameter("L", \value()), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing infix Modulo % for int x int -> int 
test bool tst(int arg1, int arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\int(), ltype);
  // tp = \int(); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\int(), rtype);
  // tp = \int(); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) % (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\int(), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Division / for &L <: num x &R <: num        -> LUB(&L, &R) 
test bool tst(&L <: num arg1, &R <: num arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\parameter("L", \num()), ltype);
  // tp = \parameter("L", \num()); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\parameter("R", \num()), rtype);
  // tp = \parameter("R", \num()); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) / (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\LUB(\parameter("L", \value()),\parameter("R", \value())), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing postfix Closure +
test bool tst(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  // tp = \lrel([\parameter("L", \value()),\parameter("L", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("(<escape(arg1)>) +;")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing postfix Closure +
test bool tst(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  // tp = \rel([\parameter("L", \value()),\parameter("L", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("(<escape(arg1)>) +;")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing postfix Closure *
test bool tst(lrel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  // tp = \lrel([\parameter("L", \value()),\parameter("L", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("(<escape(arg1)>) *;")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\lrel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing postfix Closure *
test bool tst(rel[&L,&L] arg1){ 
  ltype = typeOf(arg1);
  if(isDateTimeType(ltype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("L", \value()),\parameter("L", \value())]), ltype);
  // tp = \rel([\parameter("L", \value()),\parameter("L", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  if(lmatches){
     
     
	    if(result(v) := eval("(<escape(arg1)>) *;")){ // apply the operator to its arguments
        actualType = typeOf(v);
        expectedType = normalize(\rel([\parameter("L", \value()),\parameter("L", \value())]), lbindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
		}
  }
  return false;
}
// Testing infix Composition o for lrel[&A,&B] x lrel[&B,&C] -> lrel[&A,&C]
test bool tst(lrel[&A,&B] arg1, lrel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\lrel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  // tp = \lrel([\parameter("A", \value()),\parameter("B", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\lrel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  // tp = \lrel([\parameter("B", \value()),\parameter("C", \value())]); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) o (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\lrel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Composition o for rel[&A,&B] x rel[&B,&C] -> rel[&A,&C]
test bool tst(rel[&A,&B] arg1, rel[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\rel([\parameter("A", \value()),\parameter("B", \value())]), ltype);
  // tp = \rel([\parameter("A", \value()),\parameter("B", \value())]); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\rel([\parameter("B", \value()),\parameter("C", \value())]), rtype);
  // tp = \rel([\parameter("B", \value()),\parameter("C", \value())]); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) o (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\rel([\parameter("A", \value()),\parameter("C", \value())]), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}
// Testing infix Composition o for map[&A,&B] x map[&B,&C] -> map[&A,&C]

test bool tst(map[&A,&B] arg1, map[&B,&C] arg2){ 
  ltype = typeOf(arg1);
  rtype = typeOf(arg2);
  if(isDateTimeType(ltype) || isDateTimeType(rtype))
		return true;
  <lmatches, lbindings> = bind(\map(\parameter("A", \value()),\parameter("B", \value())), ltype);
  // tp = \map(\parameter("A", \value()),\parameter("B", \value())); println("<tp>, ltype = <ltype>, lmatches = <lmatches> <lbindings>");
  <rmatches, rbindings> = bind(\map(\parameter("B", \value()),\parameter("C", \value())), rtype);
  // tp = \map(\parameter("B", \value()),\parameter("C", \value())); println("<tp>, rtype = <rtype>, rmatches = <rmatches> <rbindings>");
  if(lmatches && rmatches){
     bindings = merge(lbindings, rbindings); 
     
     
     if(result(v) := eval("(<escape(arg1)>) o (<escape(arg2)>);")){ // apply the operator to its arguments
        actualType = typeOf(v); 
        expectedType = normalize(\map(\parameter("A", \value()),\parameter("C", \value())), bindings);
//      println("actual = <actualType>, expected = <expectedType>");
        return subtype(actualType, expectedType);
     }
  }
  return false;
}

