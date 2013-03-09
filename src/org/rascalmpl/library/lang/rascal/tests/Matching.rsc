module lang::rascal::tests::Matching

import List;
import IO;

syntax A = a: "a";

syntax As = as: A+ alist;

syntax C = c: A a "x" As as;

test bool tstA(){
    pt = parse(#A, "a");
    return a() := pt && pt is a;
}

test bool tstAs(){
    pt = parse(#As, "aaa");
    return as(al) := pt && pt is as && pt.alist == al;
}

test bool tstC(){
    pt = parse(#C, "axaaa");
    return c(A a, As as) := pt && pt.a == a && pt.as == as && size([x | x <- as.alist]) == 3;
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

