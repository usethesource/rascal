module lang::rascal::tests::Booleans

public test bool sanity() = true != false;

public test bool or(bool b) { if (true || b == true, b || true == true, false || false == false) return true; else return false; }  
  
public test bool and(bool b) { if ((false && b) == false, (b && false) == false, (true && true) == true) return true; else return false; }

public test bool not(bool b) = !!b == b;

public test bool not() = (!true == false) && (!false == true);

