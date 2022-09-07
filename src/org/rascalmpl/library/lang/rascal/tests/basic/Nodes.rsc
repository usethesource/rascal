module lang::rascal::tests::basic::Nodes

import Node;
import List;
import util::Math;
import Exception;

// Operators

// In an ideal world, this should work, but we have to adapt ranges first ...

//public list[int] makeSlice(list[int] L, int b, int s, int e){
//  return
//    for(int i <- [b, s .. e])
//      append L[i];
//}

test bool visitNode() { 
  switch ("x"()) { 
    case "x"() :
      return true; 
  }
  return false;
}

test bool subscription(node N){
  R = getChildren(N);
  for(int i <- [0..arity(N)]){
      if(head(R) != N[i])
         return false;
       R = tail(R);
  }
  return true;  
}

test bool subscriptionWrapped(node N){
  for(int i <- [0 .. arity(N)]){
      if(N[i] != N[i - arity(N)]){
      	 return false;
      }
  }
  return true;
}

public list[value] makeSlice(list[value] L, int f, int s, int e){
  res = [];
  int i = f;
  int delta = s - f;
  if(delta == 0 || f == e)
     return res;
  if(f <= e){
     while(i >= 0 && i < size(L) && i < e ){
        res += L[i];
        i += delta;
     }
  } else {
    while(i >= 0 && i < size(L) && i > e){
        res += L[i];
        i += delta;
     }
  }
  return res;
}

test bool sliceFirst1(node N) {
   L = getChildren(N);
   if(isEmpty(L)) return true;
   f = arbInt(size(L));
   e = f + arbInt(size(L) - f);
   return N[f .. e] == makeSlice(L, f, f + 1, e);
}

test bool sliceFirst2(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  return N[f..] == makeSlice(L, f, f + 1, size(L) );
}


test bool sliceFirstSecond(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  return N[f, f + incr..] == makeSlice(L, f, f + incr, size(L));
}


test bool sliceEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return N[..e] == makeSlice(L, 0, 1, e);
}

test bool sliceSecondEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  incr = 2;
  first = incr > e ? size(L)-1 : 0;
  return N[,incr..e] == makeSlice(L, first, incr, e);
}

public tuple[int,int] arbFirstEnd(list[value] L){
  if(isEmpty(L)) throw "No beging/end indices possible";
  if(size(L) == 1) return <0,0>;
  f = arbInt(size(L));
  e = f + arbInt(size(L) - f);
  return <f, e>;
}
test bool sliceFirstSecondEnd(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  <f, e> = arbFirstEnd(L);
  incr = 2;
  return N[f, f + incr .. e] == makeSlice(L, f, f + incr, e);
}

test bool sliceFirstNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = 1;
  return N[-f..] == makeSlice(L, size(L) - f,  size(L) - f + 1, size(L));
}

test bool sliceEndNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  e = arbInt(size(L));
  return N[..-e] == makeSlice(L, 0, 1, e == 0 ? e : size(L) - e);
}

test bool sliceFirstNegativeSecondNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  f = arbInt(size(L));
  incr = 2;
  if(f == 0)
     return N[0, -incr..] == makeSlice(L, 0, size(L) - incr, size(L));
  else
     return N[-f, -(f + incr)..] == makeSlice(L, size(L) - f, size(L) - (f + incr), -1);
}

test bool sliceSecondNegative(node N) {
  L = getChildren(N);
  if(isEmpty(L)) return true;
  incr = 2;
  return N[, -incr ..] == makeSlice(L, 0, size(L) - incr, size(L));
}

test bool assignSlice1() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[..] = [10,20]; return L == "f"(10,20,10,20,10,20,10,20,10,20);}
test bool assignSlice2() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[2..] = [10,20]; return   L == "f"(0,1,10,20,10,20,10,20,10,20);}
test bool assignSlice3() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[2..6] = [10,20]; return L == "f"(0,1,10,20,10,20,6,7,8,9);}
test bool assignSlice4() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[8..3] = [10,20]; return L == "f"(0,1,2,3,10,20,10,20,10,9);}

test bool assignStep1() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10]; return L == "f"(10,1,10,3,10,5,10,7,10,9);}
test bool assignStep2() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10,20]; return L == "f"(10,1,20,3,10,5,20,7,10,9);}
test bool assignStep3() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10]; return L == "f"(10,1,10,3,10,5,10,7,10,9);}
test bool assignStep4() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10,20]; return L == "f"(10,1,20,3,10,5,20,7,10,9);}
test bool assignStep5() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10,20,30]; return L == "f"(10,1,20,3,30,5,10,7,20,9);}
test bool assignStep6() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,2..] = [10,20,30,40,50,60,70]; return L == "f"(10,1,20,3,30,5,40,7,50,9,60,70);}

test bool assignStep7() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[2,4..] = [10]; return L == "f"(0,1,10,3,10,5,10,7,10,9);}
test bool assignStep8() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[2,4..6] = [10]; return L == "f"(0,1,10,3,10,5,6,7,8,9);}

test bool assignStep9() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[,6..1] = [10]; return L == "f"(0,1,2,10,4,5,10,7,8,10);}
test bool assignStep10() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[8,6..] = [10]; return L == "f"(10,1,10,3,10,5,10,7,10,9);}
test bool assignStep11() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[8,6..3] = [10]; return L == "f"(0,1,2,3,10,5,10,7,10,9);}

test bool assignStep12() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[-1,-2..] = [10,20,30,40,50]; return L == "f"(50,40,30,20,10,50,40,30,20,10);}
test bool assignStep13() { L = "f"(0,1,2,3,4,5,6,7,8,9); L[-1,-3..] = [10,20,30,40,50]; return L == "f"(0,50,2,40,4,30,6,20,8,10);}

// Library functions

test bool tstNode1(node N) = N == makeNode(getName(N), getChildren(N), keywordParameters = getKeywordParameters(N));

test bool tstNode2(str name, list[value] children) = arity(makeNode(name, children)) == size(children) &&
                                                               getName(makeNode(name, children)) == name &&
                                                               getChildren(makeNode(name, children)) == children;
                                                               
data D = d(int i, int j = 0);
node n0 = d(1, j = 2);
node n1 = d(3);

test bool testPositionalFieldOnNode() = n0.i == 1; //generate warning in static checker: "field access on node type may fail at run-time"
test bool testKeywordParameterOnNode() = n0.j == 2;

@expected{NoSuchField}
test bool testUnsetKeywordParameterOnNode() {
  n1.j;
  fail;
}
@expected{NoSuchField}
test bool testNonExistingFieldOnNode() {
  n0.k;
  fail;
}

test bool testNodeHasPositionalParameter() = n0 has i;
test bool testNodeHasKeywordParameter() = n0 has j;
test bool testNodeHasDefaultKeywordParameter() = !(n1 has j);
test bool testNodeIsPositionalParameterDefined() = n0.i?;
test bool testNodeIsKeywordParameterDefined() = n0.j?;
test bool testNodeDefaultKeywordParameterIsNotDefined() = !n1.j?;

test bool testUnset(node n) = getKeywordParameters(unset(n)) == ();

test bool testUnsetRecNode(node v) = testUnsetRecValue(v);

test bool testUnsetRecValue(value v) {
  visit (unsetRec(v)) {
    case node n : {
      if (getKeywordParameters(n) != ()) {
        return false;
      }
    }
  }
  return true;
}

test bool testUnsetRecInsideTuple() = testUnsetRecValue(<1, "a"(x=3)>);
