module lang::rascalcore::check::tests::FunctionTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

 test bool returnFOK() = checkOK("int f() = 3;");
 test bool returnFNotOK() = unexpectedType("int f() = false;");

 test bool returnSumOK() = checkOK("(&T \<:num) sum([(&T \<: num) hd, *(&T \<: num) tl]) = (hd | it + i | i \<- tl);");

 test bool returnDomainOK() = checkOK("set[&T0] domain (rel[&T0,&T1] R){ return R\<0\>; }");

 test bool returnGOK()    = checkOK("&T g(Maybe[&T] _, &T x) = x;", initialDecls= ["import util::Maybe;"]);
 test bool returnGNotOK() = unexpectedType("&T g(Maybe[&T] _, value x) = x;", initialDecls= ["import util::Maybe;"]);

 test bool returnGetNotOK() = unexpectedType("&T get(list[&T] _) = 1;");

 test bool returnHeadTailNotOK() = unexpectedType("
 tuple[&T, list[&T]] headTail(list[&T] l) {
      if ([&T h, *&T t] := l) {
        return \<h, t\>;
      }
      return \<0,[]\>; 
   }");

 test bool returnHeadTailOK() = checkOK("
   tuple[&T, list[&T]] headTail(list[&T] l) {
      if ([&T h, *&T t] := l) {
        return \<h, t\>;
      }
      fail; // we can not handle the empty case. could also be `throw EmptyList()`
   }");

 test bool returnEmptyMapNotOK() = unexpectedType("map[&K, &V] emptyMap(type[map[&K,&V]] _) = ();");
 test bool returnEmptyMapOK()    = checkOK("map[&K, &V] emptyMap(type[map[&K,&V]] _) { map[&K,&V] e = (); return e;};");

 test bool returnEmptyListNotOK() = unexpectedType("list[&T] emptyList(type[&T] _) = [];");
 test bool returnEmptyListOK()    = checkOK("list[&T] emptyList(type[&T] _) = { list[&T] e = []; return e;};");

 test bool makeSmallerNotOk() = unexpectedType("
 &T \<: num makeSmallerThan(&T \<: num n) {
     if (int i := n) {
         return i;    
     }
     return n;
 }");

 test bool makeSmallerOk() = checkOK("
 &T \<: num makeSmallerThan(&T \<: num n) {
     if (int i := n) {
         &T \<: num x = i;
         return x;    
     }
     return n;
 }");

test bool ambFilterNotOK() = unexpectedType("
&T \<:Tree ambFilter(amb(set[&T \<:Tree] alternatives)) {
  result = {a | Aas a \<- alternatives, !(a is nil)};
  if ({oneTree} := result) {
    return oneTree;
  }
  return ParseTree::amb(result);
}", initialDecls= ["
import ParseTree;
syntax Aas
  = nil: [a]*
  | a:   [a][a]*
  | aas: [a][a][a]*
  ;
"]);

// // TODO: Running this test produces not yet explained errors such as:
// error(
//     "isListRelType: |rascal+function:///Type/isListRelType$b6641| has identical definitions at two locations: |std:///Type.rsc|(38799,52,\<628,0\>,\<628,52\>) and |std:///Type.rsc|(39988,52,\<650,0\>,\<650,52\>)",
//     |rascal+function:///Type/isListRelType$b6641|)

// test bool ambFilterOK() = checkOK("
// &T \<:Tree ambFilter(amb(set[&T \<:Tree] alternatives)) {
//   set[&T \<: Tree] result = {a | Aas a \<- alternatives, !(a is nil)};
//   if ({&T \<: Tree oneTree} := result) {
//     return oneTree;
//   }
//   return ParseTree::amb(result);
// }", initialDecls= ["
// import ParseTree;
// syntax Aas
//   = nil: [a]*
//   | a:   [a][a]*
//   | aas: [a][a][a]*
//   ;
// "]);

