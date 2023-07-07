module lang::rascal::tests::concrete::Matching

import List;
import Node;
import ParseTree;

syntax A = a: "a";

syntax As = as: A+ alist;

syntax C = c: A a "x" As as;

syntax D = d: "d" | e: "e" D d;

syntax Ds = ds: {D ","}+ dlist;

syntax X = "plus" {A ","}+ l1 | "star" {A ","}* l2;

layout W = [\ ]*;

int f({A ","}* l) = size([x | A x <- l]);

test bool plusToStar() = f(([X] "plus a,a").l1) == 2;

test bool plusToStarIndirect() {
  {A ","}+ x = ([X] "plus a,a").l1;
  
  return f(x) == 2;
}

test bool testIs(){
    pt = parse(#A, "a");
    return a() := pt && pt is a;
}

test bool testAs(){
    pt = parse(#As, "aaa");
    return as(al) := pt && pt is as && pt.alist == al;
}

test bool testMatchC(){
    pt = parse(#C, "axaaa");
    return c(A _, As _) := pt;
}

test bool testFieldSelectC(){
    pt = parse(#C, "axaaa");
    return c(A a, As _) := pt && pt.a == a;
}
test bool testFieldSelectC2(){
    pt = parse(#C, "axaaa");
    return c(A _, As as) := pt && pt.as == as;
}

@ignoreInterpreter{
Feature is not implemented
}
test bool testConcreteListC1(){
    pt = parse(#C, "axaaa");
    return c(A _, As as) := pt && unsetRec(as.alist[0]) == unsetRec([A]"a"); // unset src keywords
}

test bool testConcreteListC2(){
    pt = parse(#C, "axaaa");
    return c(A _, As as) := pt && size([x | x <- as.alist]) == 3;
}

@ignoreInterpreter{
Feature is not implemented
}
test bool testConcreteListD1(){
    pt = parse(#Ds, "d,d");
    return Ds ds := pt && (unsetRec(ds.dlist[0]) == unsetRec([D]"d"));  // unset src keywords
}

test bool testConcreteListD2(){
    pt = parse(#Ds, "d,d");
    return Ds ds := pt && size([x | x <- ds.dlist]) == 2;
}

bool dispatch(e(D _)) = true;
bool dispatch(d()) = false;

test bool dispatchTest() = dispatch((D) `ed`) && !dispatch((D) `d`);
