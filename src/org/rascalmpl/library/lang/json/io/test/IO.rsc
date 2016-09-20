module lang::json::io::\test::IO

import lang::json::IO;
import IO;

data Nat = suc(Nat next) | zero() | time(datetime d);

private loc file = |test-temp:///jsonTest.json|;

test bool writeRead(Nat n) {
  println("testing <n>");
  writeJSON(file, n, dateTimeAsInt=true);
  return n == readJSON(#Nat, file);
}

test bool writeReadArray(Nat n) {
  println("testing <n>");
  writeJSON(file, n, implicitConstructors=false,  dateTimeAsInt=true);
  return n == readJSON(#Nat, file, implicitConstructors=false);
}