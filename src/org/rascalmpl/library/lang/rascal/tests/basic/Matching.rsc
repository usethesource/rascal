module lang::rascal::tests::basic::Matching

data T1 = \int() | \void() | string(str s);
data T2 = \int() | \void() | string(str s);

@ignoreCompiler{FIXME}
test bool incomparableTypesButNonEmptyIntersectionCanMatch() {
   tuple[int, num] a = <1,1>;
   return tuple[num, int] _ := a;
}

test bool tstQNameInPatternInt(){
    T1 t1 = T1::\int();
    T2 t2 = T2::\int();
    return T1::\int() := t1 && T2::\int() := t2;
}

@expect{
UnexpectedType
}
test bool prefixShouldHaveEffect() {
  value term = T2::string("x");
  
  return T1::string(_) !:= term;
}

test bool tstQNameInPatternVoid(){
    T1 t1 = T1::\void();
    T2 t2 = T2::\void();
    return T1::\void() := t1 && T2::\void() := t2;
}

test bool tstQNameInSwitchInt(){
    T1 t1 = T1::\int();
    T2 t2 = T2::\int();
    bool tstSwitch = true;
    switch(t1) { case T1::\int(): ; default: tstSwitch = false; }
    switch(t2) { case T2::\int(): ; default: tstSwitch = false; }
    return tstSwitch;
}

test bool tstQNameInSwitchVoid(){
    T1 t1 = T1::\void();
    T2 t2 = T2::\void();
    bool tstSwitch = true;
    switch(t1) { case T1::\void(): ; default: tstSwitch = false; }
    switch(t2) { case T2::\void(): ; default: tstSwitch = false; }
    return tstSwitch;
}

test bool tstQNameInSwitchString(){
    T1 t1 = T1::string("t1");
    T2 t2 = T2::string("t2");
    bool tstSwitch = true;
   
    switch(t1) { case T1::string(str _): ; default: tstSwitch = false; }
    switch(t2) { case T2::string(str _): ; default: tstSwitch = false; }
    return tstSwitch;
}

bool fT1(T1::\int()) = true;
bool fT1(T1::\void()) = true;
bool fT1(T1::string(str _)) = true;
default bool fT1(value _) = false;

bool fT2(T2::\int()) = true;
bool fT2(T2::\void()) = true;
bool fT2(T2::string(str _)) = true;
default bool fT2(value _) = false;

test bool tstQNameinFun1Int(){
    T1 t1 = T1::\int();
    return fT1(t1);
}

test bool tstQNameinFun2Int(){
    T2 t2 = T2::\int();
    return fT2(t2);
}

test bool tstQNameinFun1Void(){
    T1 t1 = T1::\void();
    return fT1(t1);
}

test bool tstQNameinFun2Void(){
    T2 t2 = T2::\void();
    return fT2(t2);
}

test bool tstQNameinFun1String(){
    T1 t1 = T1::string("t1");
    return fT1(t1);
}

test bool tstQNameinFun2String(){
    T2 t2 = T2::string("t2");
    return fT2(t2);
}

test bool deepMatchKeywordParameter() = /int _ := "f"("f"(x=[1]));

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
