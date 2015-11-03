@bootstrapParser
@doc{
In this test module we collect test cases that are associated with bugs from the past.
This is just to make sure the bugs are not re-introduced accidentally.
}
module lang::rascal::tests::basic::SolvedIssues

import List;

test bool emptySetEquals(set[value] x, set[value] y) = x - x == y - y;

test bool emptySetEquals(map[value,value] x, map[value,value] y) = x - x == y - y;

data X = n(set[node] nn);

test bool infiniteMatcher() {
  bool firstTime = true;
  for (n({"a"(),_*}) <- { n({"a"()}) }) {
    if (!firstTime) {
      return false;
    }
    firstTime = false;
  }
  return true;
}

data Exp = a(int x, int y = 5);

test bool inferKWparamType() {
  // a regression test
  
  if (a(x, y = q) := a(0,y=3)) { 
    int z = q; // used to throw an exception "Expected int, but got value true;"
    return true; 
  } 
  else {
    return false;
  }
}  

// https://github.com/cwi-swat/rascal/issues/885
test bool checkComparableOnElementsBreak() = [<[],_,_>,_] := [<[],false,1>,<[3],false,3>];