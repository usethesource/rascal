module lang::rascalcore::check::tests::InferenceTests

import lang::rascalcore::check::tests::StaticTestingUtils;
    
test bool nestedLocalVarDoesNotLeak1() = undeclaredVariable("int f(){ { x = true; } int n = x + 1;}");

test bool nestedLocalVarDoesNotLeak2() = undeclaredVariable("void f(){ { x = 1; } x += 1;}");

test bool parallelLocalVarsDoNotLeak() = checkOK("void f(){ { x = true; bool b = x;} { x = 1;  int n = x; } }");

test bool switchCasesDoNotLeak1()  = checkOK("void f(value v){ switch(v) { case str x: {str s = x;} case int x: {int n = x;}} }");

test bool switchCasesDoNotLeak2()  = undeclaredVariable("void f(value v){ switch(v) { case str x: {str s = x;} case int x: {int n = x;}} x;}");

test bool parallelIfDoesNotLeak() = checkOK("void f(value v){ if(str x := v) {str s = x;} if(int x := v) {int n = x;} }");

test bool parallelIfElseDoesNotLeak() = checkOK("void f(value v){ if(str x := v) {str s = x;} else {;} if(int x := v) {int n = x;} else {;} }");