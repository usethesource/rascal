module lang::rascal::tests::functionality::KeywordParameterTestImport

import lang::rascal::tests::imports::KeywordParameterTest1;
import lang::rascal::tests::imports::KeywordParameterTest2;

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

test bool allocatedElseWhereUsedWithNewExtension1() 
   = createL1().g == l().g;

test bool allocatedElseWhereUsedWithNewExtension2() 
   = createN2().a == n().a;
