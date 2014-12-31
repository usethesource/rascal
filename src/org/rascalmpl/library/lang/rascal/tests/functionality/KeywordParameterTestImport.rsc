module lang::rascal::tests::functionality::KeywordParameterTestImport

import lang::rascal::tests::imports::KeywordParameterTest1;
import lang::rascal::tests::imports::KeywordParameterTest2;

data L(int a = 0, int b = 2 * a) = l(int c = 2 * b);

data L(int d = -1) = m();

data L(str e = "e", str f = e + e) = n(str g = f + f);

data L(str h = "") = p();

test bool sameModuleDef1() = l().a == m().a;
test bool sameModuleDef2() = l().d == m().d;

test bool crossModuleDef1() = l().a == n().a;
test bool crossModuleDef2() = l().c == n().c;
test bool crossModuleDef3() = m().d == n().d;
test bool crossModuleDef4() = n().g == l().g;
test bool crossModuleDef5() = n().h == m().h;

test bool crossModuleAssignAndFieldRef() {
  a = l();
  b = n();
  
  a.e = "hello";
  b.c = 42;
  
  return a.e == "hello" && b.c == 42;
}