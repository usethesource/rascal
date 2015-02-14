module lang::rascal::tests::basic::Matching

import List;
import IO;
import ParseTree;

syntax A = a: "a";

syntax As = as: A+ alist;

syntax C = c: A a "x" As as;

syntax D = d: "d" | e: "e" D d;

syntax Ds = ds: {D ","}+ dlist;

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
    return c(A a, As as) := pt;
}

test bool testFieldSelectC(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && pt.a == a;
}
test bool testFieldSelectC2(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && pt.as == as;
}

@ignoreInterpreter{Feature is not implemented}
test bool testConcreteListC1(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && as.alist[0] == [A]"a";
}

test bool testConcreteListC2(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && size([x | x <- as.alist]) == 3;
}

@ignoreInterpreter{Feature is not implemented}
test bool testConcreteListD1(){
    pt = parse(#Ds, "d,d");
    return Ds ds := pt && ds.dlist[0] == [D]"d";
}

test bool testConcreteListD2(){
    pt = parse(#Ds, "d,d");
    return Ds ds := pt && size([x | x <- ds.dlist]) == 2;
}

data T1 = \int() | \void() | string(str s);
data T2 = \int() | \void() | string(str s);

test bool tstQNameInPatterns() {
	T1 t1 = T1::\int();
	T2 t2 = T2::\int();
	
	bool fT1(T1::\int()) = true;
	bool fT1(T1::\void()) = true;
	bool fT1(T1::string(str _)) = true;
	default bool fT1(value _) = false;
	
	bool fT2(T2::\int()) = true;
	bool fT2(T2::\void()) = true;
	bool fT2(T2::string(str _)) = true;
	default bool fT2(value _) = false;
	
	bool tst = true;
	bool tstSwitch = true;
	bool tstFuncCalls = true;
	
	tst = tst && T1::\int() := t1 && T2::\int() := t2;
	switch(t1) { case T1::\int(): ; default: tstSwitch = false; }
	switch(t2) { case T2::\int(): ; default: tstSwitch = false; }
	
	tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
	
	t1 = T1::\void();
	t2 = T2::\void();
	
	tst = tst && T1::\void() := t1 && T2::\void() := t2;
	switch(t1) { case T1::\void(): ; default: tstSwitch = false; }
	switch(t2) { case T2::\void(): ; default: tstSwitch = false; }
	tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
	
	t1 = T1::string("t1");
	t2 = T2::string("t2");
	
	tst = tst && T1::string(str _) := t1 && T2::string(str _) := t2;
	switch(t1) { case T1::string(str _): ; default: tstSwitch = false; }
	switch(t2) { case T2::string(str _): ; default: tstSwitch = false; }
	tstFuncCalls = tstFuncCalls && fT1(t1) && fT2(t2);
	
	return tst && tstSwitch && tstFuncCalls;
}

test bool deepMatchKeywordParameter() = /int i := "f"("f"(x=[1]));

bool dispatch(e(D _)) = true;
bool dispatch(d()) = false;

test bool dispatch() = D _ := (D) `ed`;
