module lang::rascalcore::compile::Examples::Tst5

//module lang::rascal::tests::functionality::KeywordParameterImport2::Tests

import lang::rascal::tests::functionality::KeywordParameterImport2::Import1;  
import lang::rascal::tests::functionality::KeywordParameterImport2::Import2;

// this requires keyword parameters attached to the adt
// to be distributed over all constructors in a module:
test bool sameModuleDef1() = l().a == m().a;
test bool sameModuleDef2() = l().d == m().d;

// this requires keyword parameters attached to the adt
// to be distributed over all constructors visible in the current
// module, also the imported ones:
test bool crossModuleDef1() = l().a == n().a;
test bool crossModuleDef3() = m().d == n().d;
test bool crossModuleDef4() = n().f == l().f;
test bool crossModuleDef5() = n().h == m().h;

// except for field access, also assignment should work:
test bool crossModuleAssignAndFieldRef() {
  a = l();
  b = n();
  
  a.e = "hello";
  b.b = 42;
  
  return a.e == "hello" && b.b == 42;
}

// this requires the compiler/interpreter to inject code
// to resolve the default values dynamically
test bool allocatedElseWhereUsedWithNewExtension1() 
   = createL1().f == l().f;

test bool allocatedElseWhereUsedWithNewExtension2() 
   = createN2().a == n().a;

// this requires default values to not be set in the run-time
// such that values remain structurally equal to future extended
// values:   
test bool defaultEquality() = createL1() == l();

// we see that the user has overridden a default, even though
// it is equal to the original. This is apparent in the serialized
// form as well, necessarily. 
// `"l(a=0)" != "l()"` even though `l.a == 0` through the default mechanism
test bool observableOverrides() {
  x = l();
  assert x.a == 1;
  y = x;
  assert x == y && "<x>" == "<y>";
  x.a = 1;
  return x != y && "<x>" != "<y>";
}
