module experiments::Compiler::Examples::Tst6

import Boolean;
//import Exception;
// Operators

test bool sanity() = true != false;

//@ignoreCompiler{Issue in the compiler with handling the booleanScope for b1 and b2}
//test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }  
//  
//test bool and(bool b) { if ((false && b) == false, (b && false) == false, (true && true) == true) return true; else return false; }
//
//test bool not(bool b) = !!b == b;
//
//test bool not() = (!true == false) && (!false == true);
//
//@ignoreCompiler{Issue in the compiler with handling the booleanScope for b1 and b2}
//test bool equiv(bool b1, bool b2) = (b1 <==> b2) <==> (!b1 && !b2 || b1 && b2);
//
//@ignoreCompiler{Issue in the compiler with handling the booleanScope for b1 and b2}
//test bool impl(bool b1, bool b2) = (b1 ==> b2) <==> !(b1 && !b2);

// Library functions

test bool tstArbBool() { b = arbBool() ; return b == true || b == false; }
