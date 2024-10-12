module lang::rascalcore::check::tests::FunctionTCTests

import lang::rascalcore::check::tests::StaticTestingUtils;

// Without type parameters
test bool returnOK1() = checkOK("int f() = 3;");
test bool returnOK2() = checkOK("list[int] f() = [1,2,3];");

test bool returnNotOK1() = unexpectedType("int f() = false;");
test bool returnNotOK2() = unexpectedType("list[int] f() = {1,2,3};");

// Type parameters without bounds
test bool returnSumOK()       = checkOK("(&T \<:num) sum([(&T \<: num) hd, *(&T \<: num) tl]) = (hd | it + i | i \<- tl);");
test bool returnDomainOK()    = checkOK("set[&T0] domain (rel[&T0,&T1] R){ return R\<0\>; }");
test bool returnGOK()         = checkOK("&T g(Maybe[&T] _, value x) = x;", initialDecls= ["import util::Maybe;"]);
test bool returnEmptyListOK() = checkOK("list[&T] emptyList(type[&T] _) = [];");
test bool returnEmptyMapOK()  = checkOK("map[&K, &V] emptyMap(type[map[&K,&V]] _) = ();");
test bool typeParamsOK1()     = checkOK("&T add(&T x, &T y) = y;");
test bool typeParamsOK2()     = checkOK("num sub(num x, num y) = x - y;");

test bool typeParamNotBound1() = unexpectedType("list[&T] f(int _) = [];");
test bool typeParamNotBound2() = unexpectedType("rel[&T, &V] f(&T x) = {\<x,x\>};");

test bool returnNotOK3()       = unexpectedType("&T get(list[&T] _) = 1;");

// Type parameters with bounds

test bool BoundOK1() = checkOK("&T \<: int f(&T \<: num _) = 1;");
test bool BoundOK2() = checkOK("&T \<: num sub(&T \<:num x, &T y) = x - y;");
test bool BoundOK3() = checkOK("&T sub(&T \<:num x, &T y) = x - y;");

test bool matchOK1() = checkOK("bool f(&A a, &B b) = &A _ := b;");

test bool BoundNotOK1() = unexpectedType("&T \<: real f(&T \<: str x) = 1.5;");
test bool returnNotOK4()= unexpectedType("list[&T] emptyList(list[&T] _) = [1];");
test bool assignmentNotOK1() = unexpectedType("void f(&T x) { &T y = 1; }");
test bool matchNotOK1() = unexpectedType("bool f(&A \<: str a, &B \<:int b) = &A _ := b;");

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

 test bool makeSmallerNotOK() = unexpectedType("
 &T \<: num makeSmallerThan(&T \<: num n) {
      if (int i := n) {
         return i;    
      }
      return n;
  }");
