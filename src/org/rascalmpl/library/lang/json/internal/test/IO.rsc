module lang::json::internal::\test::IO

import lang::json::IO;

data Nat = suc(Nat next) | zero() | time(datetime d) | both(Nat n, int x) | twice(Nat n, Nat m);

private loc file = |test-temp:///jsonTest.json|;

test bool writeRead(Nat n) {
  writeJSON(file, n, dateTimeAsInt=true);
  return n == readJSON(#Nat, file);
}

test bool writeReadArray(Nat n) {
  writeJSON(file, n, implicitConstructors=false,  dateTimeAsInt=true);
  return n == readJSON(#Nat, file, implicitConstructors=false);
}


