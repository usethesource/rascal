module lang::rascal::tests::basic::IsDefined

import Exception;
import util::Math;
import List;
// Strings

test bool isDefinedStr1() = ("abc"[0])?;

test bool isDefinedStr2() = !("abc"[5])?;

test bool isDefinedStr3() = ("abc"[-3])?;

test bool isDefinedStr4() = !("abc"[-4])?;

// Locations

test bool isDefinedLoc1() = |project://x/y.txt|(5,4,<1,5>,<1,9>).begin?;
test bool isDefinedLoc2() = |project://x/y.txt|(5,4,<1,5>,<1,9>).end?;
test bool isDefinedLoc3() = !(|project://x/y.txt|(5,4,<1,5>,<1,9>).host?);

test bool isDefinedLoc4() = !(|std:///List.rsc|.ls?);
test bool isDefinedLoc5() = |std:///util|.ls?;

// Lists

test bool isDefinedList1(list[int] L) = L == [] || L[arbInt(size(L))]?;

test bool isDefinedList2(list[int] L) = !L[size(L) + 1]?;

test bool isDefinedList3() = [0,1,2][-3]?;

test bool isDefinedList4() = !([0,1,2][-4])?;


test bool isDefinedList5() {
    lst = [0,1,2];
    try {
      lst[3];
      return false;
    } catch IndexOutOfBounds(_): {
      return true;
    }
}

test bool isDefinedList6() {
    lst = [0,1,2];
    return !lst[3]?;
}

test bool isDefinedList7() {
   int x = -1;
   try {
     x = [0,1,2,3][2] ? 100;
   } catch IndexOutOfBounds(_) : {
     x = 200;
   }
   return x == 2;
}

test bool isDefinedList8() {
   int x = -1;
   try {
     x = [0,1,2,3][5] ? 100;
   } catch IndexOutOfBounds(_) : {
     x = 200;
   }
   return x == 100;
}

test bool isDefinedList9() {
   list[int] lst = [0,1,2];
   lst[2] ? 0 += 1;
   return lst[2] == 3;
}

// Maps

test bool isDefinedMap1() = (0 : "0", 1 : "1", 2 : "2")[2]?;

test bool isDefinedMap2() = !((0 : "0", 1 : "1", 2 : "2")[3])?;

test bool isDefinedMap3() {
    map[int,str] m = (0 : "0", 1 : "1", 2 : "2");
    try {
      m[3];
      return false;
    } catch NoSuchKey(_): {
      return true;
    }
}

test bool isDefinedMap4(){
   map[int,str] m = (0 : "0", 1 : "1", 2 : "2");

   return m[2]?;
}

test bool isDefinedMap5(){
   map[int,str] m = (0 : "0", 1 : "1", 2 : "2");

   return !m[3]?;
}

test bool isDefinedMap6(){
    map[int,int] m = (0: 10, 1: 20);
    m[0] ? 0 += 1;
    return m[0] == 11;
}

test bool isDefinedMap7(){
    map[int,int] m = (0: 10, 1: 20);
    m[3] ? 0 += 1;
    return m[3] == 1;
}

test bool isDefinedMap8(){
    map[int,int] m = (0: 10, 1: 20);
    m[0] ?= 100;
    return m[0] == 10;
}

test bool isDefinedMap9(){
    map[int,int] m = (0: 10, 1: 20);
    m[5] ?= 100;
    return m[5] == 100;
}

// Tuples

test bool isDefinedTuple1(){
    return (<0,1,2>[1])?;
}

test bool isDefinedTuple2(){
    return (<0,1,2><1>)?;
}

test bool isDefinedTuple3(){
    tuple[int n, str s] tup = <0, "a">;
    return tup.n?;
}

@ignoreCompiler{Remove-after-transtion-to-compiler: Already detected by type checker: Field x does not exist on type tuple[int n, str s]}
@expected{UndeclaredField}
test bool isDefinedTuple4(){
    tuple[int n, str s] tup = <0, "a">;
    return !tup.x?;
}

test bool hasTuple1(){
    tuple[int n, str s] tup = <0, "a">;
    return tup has n;
}    

test bool hasTuple2(){
    tuple[int n, str s] tup = <0, "a">;
    return !(tup has x);
}   

// Relation

test bool isDefinedRel1(){
    return ({<1, "a">, <2, "b">}[0])?;
}

@ignoreCompiler{Remove-after-transtion-to-compiler: Already detected by type checker}
@expected{UnsupportedSubscriptArity}
test bool isDefinedRel2(){
    return !({<1, "a">, <2, "b">}[1,2,3])?;
}

test bool hasRel1(){
    rel[int n, str s] r = {<0, "a">};
    return r has n;
}

test bool hasRel2(){
    rel[int n, str s] r = {<0, "a">};
    return !(r has x);
}

// ListRelation

test bool isDefinedLRel1(){
    return ([<1, "a">, <2, "b">][0])?;
}

@ignoreCompiler{Remove-after-transtion-to-compiler: Already detected by type checker}
@expected{UnsupportedSubscriptArity}
test bool isDefinedLRel2(){
    return !([<1, "a">, <2, "b">][1,2,3])?;
}

test bool hasLRel1(){
    lrel[int n, str s] r = [<0, "a">];
    return r has n;
}

test bool hasLRel2(){
    lrel[int n, str s] r = [<0, "a">];
    return !(r has x);
}

// ADT

data A = a(int x, int y, str s);

test bool isDefinedADT1(){
    return (a(1,2,"abc")[0])?;
}

test bool isDefinedADT2(){
    return !(a(1,2,"abc")[5])?;
}

// node

test bool isDefinedNode1(){
    return ("f"(0,1,2)[0])?;
}

test bool isDefinedNode2(){
    return !("f"(0,1,2)[5])?;
}

test bool isDefinedNode3() = "aap"(noot=1).noot?;

test bool isDefinedNode4() = !("aap"(boot=1).noot?);

test bool hasNode1() = "aap"(noot=1) has noot;

test bool hasNode2() = !("aap"(boot=1) has noot);

test bool hasNode3() = !("aap"() has noot);

@ignoreCompiler{INCOMPATIBILITY: ? has been restricted and works no longer on undefined variables}
test bool tst() { int x = 10; y = x ? 1; return y == 10; }

// The status of unitialized variables is in transit
//test bool tst() { int x; y = x ? 1; return x == 1; }

// Annotations

data F = f3() | f3(int n) | g(int n) | deep(F f);
 
data F(int pos = 0);

test bool isDefinedAnno1() = (f3()[pos=1]).pos?;

test bool isDefinedAnno2() = !(f3().pos)?;

test bool isDefinedAnno3() = ((f3()[pos=1]).pos) == 1;

test bool isDefinedAnno4() = ((f3()).pos) == 10;

test bool isDefinedAnno5(){
    X = f3(); 
    X.pos ? 0 += 1;
    return X.pos == 1;
}

test bool isDefinedAnno6(){
    X = f3()[pos=1];
    X.pos ? 0 += 1;
    return X.pos == 2;
}

test bool isDefinedAnno7(){
    X = f3(); 
    X.pos ?= 3;
    return X.pos == 3;
}

test bool isDefinedAnno8(){
    X = f3()[pos=1]; 
    X.pos ?= 3;
    return X.pos == 1;
}

test bool isDefinedAnno9() = f3()[pos=1] has pos;

// TODO we can not tell this anymore since annotations are now simulated using keyword parameters.
// the keyword parameter "is" always there due to their semantics of having defaults..
// test bool isDefinedAnno10() = !(f3() has pos);

// e has f : e is of an ADT type and its constructor has a positional or keyword field f.
// e[k]?   : list or map contains given index k
// e.f?    : e is a node or constructor that has a keyword field f with an explicitly set value
// e?      : debatable whether we allow the general case

data F = z(int l = 2) | u();

test bool isDefined8() = !(u() has l);
test bool isDefined9() = !(u().l?);
test bool isDefined10() = !(z().l?);
test bool isDefined11() = z() has l;
test bool isDefined12() = z(l=1).l?;

test bool isDefined13() {
  e = z();
  e.l?=3; // set l to 3 if the field is not set, otherwise leave it
  return e.l == 3;
}

data D = d1() | d2(int n) | d3(int n, str s = "abc");

@expected{NoSuchField}
test bool getADTField1() = d1().n == 0;

@expected{NoSuchField}
test bool getADTField2() = d1().s == "abc";

test bool getADTField3() = d2(10).n == 10;

@expected{NoSuchField}
test bool getADTField4() = d2(10).s == "abc";

test bool getADTField5() = d3(20).n == 20;
test bool getADTField6() = d3(20).s == "abc";
test bool getADTField7() = d3(20, s = "def").n == 20;
test bool getADTField8() = d3(20, s = "def").s == "def";
test bool getADTField9() = d3(20, s = "abc").n == 20;
test bool getADTField10() = d3(20, s = "abc").s == "abc";

test bool hasADT1() = !(d1() has n);
test bool hasADT2() = !(d1() has s);
test bool hasADT3() = d2(10) has n;
test bool hasADT4() = !(d2(10) has s);
test bool hasADT5() = d3(20) has n;
test bool hasADT6() = d3(20) has s;
test bool hasADT7() = d3(20, s="def") has n;
test bool hasADT8() = d3(20, s="def") has s;
test bool hasADT9() = d3(20, s="abc") has n;
test bool hasADT10() = d3(20, s="abc") has s;

test bool isDefADTField1() = !d1().n?;
test bool isDefADTField2() = !d1().s?;
test bool isDefADTField3() = d2(10).n?;
test bool isDefADTField4() = !d2(10).s?;
test bool isDefADTField5() = d3(20).n?;
test bool isDefADTField6() = !d3(20).s?;
test bool isDefADTField7() = d3(20, s = "def").n?;
test bool isDefADTField8() = d3(20, s = "def").s?;
test bool isDefADTField9() = d3(20, s = "abc").n?;
test bool isDefADTField10() = d3(20, s = "abc").s?;

test bool ifDefADTFieldOtherwise1() = 13 == (d1().n ? 13);
test bool ifDefADTFieldOtherwise2() = "xyz" == (d1().s ? "xyz");
test bool ifDefADTFieldOtherwise3() = 10 == (d2(10).n ? 13);
test bool ifDefADTFieldOtherwise4() = "xyz" == (d2(10).s ? "xyz");
test bool ifDefADTFieldOtherwise5() = 20 == (d3(20).n ? 13);
test bool ifDefADTFieldOtherwise6() = "xyz" == (d3(20).s ? "xyz");
test bool ifDefADTFieldOtherwise7() = 20 == (d3(20, s = "def").n ? 13);
test bool ifDefADTFieldOtherwise8() = "def" == (d3(20, s = "def").s ? "xyz");
test bool ifDefADTFieldOtherwise9() = 20 == (d3(20, s = "abc").n ? 13);
test bool ifDefADTFieldOtherwise10() = "abc" == (d3(20, s = "abc").s ? "xyz");

@ignoreCompiler{INCOMPATIBILITY: ? has been restricted and works no longer on undefined variables}
test bool undefinedVariable() = !undefined?;

@ignoreCompiler{INCOMPATIBILITY: ? has been restricted and works no longer on undefined variables}
test bool definedVariable() {
  int defined = 42;
  return defined?;
}

// Keyword parameters

bool fWithKwN1(int n = 1) = n?;

test bool unsetKwParameterIsUndefined1() = !fWithKwN1();
test bool setKwParameterIsDefined1() = fWithKwN1(n=2);

int fWithKwN2(int n = 1) = n? ? 10 : -10;

test bool unsetKwParameterIsUndefined2() = fWithKwN2() == -10;
test bool setKwParameterIsDefined2() = fWithKwN2(n=2) == 10;

// Potential generic rules to check:
// e has f => e.f is well defined
// !(e has f) => e.f. gives error
// e.f? => e has f
// !(e.f?) && !(e has f) => e.f gives error
// !(e.f?) && (e has field) => e.f. gives default value
