module lang::rascal::tests::basic::Matching

import List;
import IO;

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

data IG = ig(int x = 1);

test bool ignoreKeywordParameter1() = ig() := ig(x=1);
test bool ignoreKeywordParameter2() = ig(x=1) := ig();
test bool ignoreKeywordParameter3() = "bla"() := "bla"(y=1);
test bool ignoreKeywordParameter4() = {ig()} := {ig(x=1)};
test bool ignoreKeywordParameter5() = {{ig()}} := {{ig(x=1)}};
test bool ignoreKeywordParameter6() = <ig(),_> := <ig(x=1),2>;
test bool ignoreKeywordParameter7() = [ig(),_] := [ig(x=1),2];
test bool ignoreKeywordParameter8() = "fiets"(ig()) := "fiets"(ig(x=1));

@ignore{Not yet operational}
test bool ignoreKeywordParameter9() { A = ig(x = 1); return A := ig(); }

@ignore{Not yet operational}
test bool ignoreKeywordParameter10() { L = [ ig(x = 1) ]; return L := [ ig() ]; }

@ignore{Not yet operational}
test bool ignoreKeywordParameter11() { S = { ig(x = 1) }; return S := { ig() }; }

@ignore{Not yet operational}
test bool ignoreKeywordParameter12() { M = (ig(x = 1): ig(x = 1)); return M := (ig(): ig()); }

@ignore{Not yet operational}
test bool ignoreKeywordParameter13() { M = ([ig(x = 1)]: ig(x = 1)); return M := ([ig()]: ig()); }

@ignore{Not yet operational}
test bool ignoreKeywordParameter14() { T = <ig(x = 1), ig(x = 1)>; return T := <ig(), ig()>; }
