module tests::Booleans

import Boolean;
import Exception;
// Operators

public test bool sanity() = true != false;

/*TODO:COMP*/ //public test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }  
  
public test bool and(bool b) { if ((false && b) == false, (b && false) == false, (true && true) == true) return true; else return false; }

public test bool not(bool b) = !!b == b;

public test bool not() = (!true == false) && (!false == true);

/*TODO:COMP*/ //public test bool equiv(bool b1, bool b2) = (b1 <==> b2) <==> (!b1 && !b2 || b1 && b2);

/*TODO:COMP*/ //public test bool impl(bool b1, bool b2) = (b1 ==> b2) <==> !(b1 && !b2);

// Library functions

public test bool tstArbBool() { b = arbBool() ; return b == true || b == false; }

public test bool fromString1() = fromString("true") == true && fromString("false") == false;

@expected{IllegalArgument}
public test bool fromString1(str s) = fromString(s); // will fail in there rare situtaion that "true" or "false" are passed as argument.

public test bool tstToInt() = toInt(false) == 0 && toInt(true) == 1;

public test bool tstToReal() = toReal(false) == 0.0 && toInt(true) == 1.0;

public test bool tstToString() = toString(false) == "false" && toString(true) == "true";

public test bool shortCircuiting() { 
	try { return false ==> (1/0 == 0) && true || (1/0 == 0) && !(false && (1/0 == 0)); }
	catch ArithmeticException(str _): { return false; }
	}
