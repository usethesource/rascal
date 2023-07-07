module lang::rascal::tests::basic::EscapedNames

//import lang::rascal::tests::basic::\Escaped-name-module;
//
//test bool escapedNameModule() = importedFromEscapedNameModule();

int \an-int = 42;

test bool escapedGlobal() = \an-int == 42;

int \twice(int n) = 2 * n;
int \do-twice(int n) = 2 * n;

test bool escapedFunctionName1() = \twice(2) == 4;
test bool escapedFunctionName2() = twice(2) == 4;
test bool escapedFunctionName3() = \do-twice(2) == 4;

int same1(int \a) = a;
int same2(int \a) = \a;
int same3(int \an-a) = \an-a;

test bool escapedPositionalFormal1() = same1(2) == 2;
test bool escapedPositionalFormal2() = same2(2) == 2;
test bool escapedPositionalFormal3() = same3(2) == 2;

int samekw1(int \n = 42) = n;
int samekw2(int \n = 42) = \n;
int samekw3(int \a-n = 42) =\a-n;

test bool escapedKeywordFormal1() = samekw1(n = 3) == 3;
test bool escapedKeywordFormal2() = samekw2(\n = 3) == 3;
test bool escapedKeywordFormal3() = samekw3(\a-n = 3) == 3;

data D1 = d1();
data \D2 = d2();
data \A-D3 = d3();

test bool escapedADTName1() { \D1 x = d1(); return x == d1(); }
test bool escapedADTName2() { \D2 x = d2(); return x == d2(); }
test bool escapedADTName3() { D2 x = d2(); return x == d2(); }
test bool escapedADTName4() { \A-D3 x = d3(); return x == d3(); }

data E1 = e1();
data E2 = \an-e2();

test bool escapedConstructorName1() = \e1() == e1();
test bool escapedConstructorName2() = \an-e2() == \an-e2();

data F1 = f1(int n);
data F2 = f2(int \a-n);

test bool escapedPositionalField1() = f1(3).n == 3;
test bool escapedPositionalField2() = f1(3).\n == 3;
test bool escapedPositionalField3() = f2(3).\a-n == 3;

data G1 = g1(int n = 42);
data G2 = g2(int \n = 42);
data G3 = g3(int \a-n = 42);

test bool escapedkeywordField1a() = g1(n=3).n == 3;
test bool escapedkeywordField1b() = g1(\n=3).n == 3;
test bool escapedkeywordField1c() = g1(n=3).\n == 3;
test bool escapedkeywordField1d() = g1(\n=3).\n == 3;

test bool escapedkeywordField2a() = g2(n=3).n == 3;
test bool escapedkeywordField2b() = g2(\n=3).n == 3;
test bool escapedkeywordField2c() = g2(n=3).\n == 3;
test bool escapedkeywordField2d() = g2(\n=3).\n == 3;

test bool escapedkeywordField3() = g3(\a-n=3).\a-n == 3;

test bool escapedkeywordFieldNode() = "f"(\a-n=3).\a-n == 3;

data H1[&T] = h1(&T n);
data H2[&\T] = h2(&T n);
data H3[&\T] = h3(&\T n);
data H4[&T] = h4(&\T n);

test bool escapedTypeParameter1() { H1[int] x = h1(3); return x.n == 3; }
test bool escapedTypeParameter2() { H2[int] x = h2(3); return x.n == 3; }
test bool escapedTypeParameter3() { H3[int] x = h3(3); return x.n == 3; }
test bool escapedTypeParameter4() { H4[int] x = h4(3); return x.n == 3; }

alias A = int;
alias \B = int;
alias C = B;

test bool escapedAlias1() { A x = 3; return x == 3; }
test bool escapedAlias2() { \A x = 3; return x == 3; }

test bool escapedAlias3() { B x = 3; return x == 3; }
test bool escapedAlias4() { \B x = 3; return x == 3; }

test bool escapedAlias5() { C x = 3; return x == 3; }
test bool escapedAlias6() { \C x = 3; return x == 3; }

test bool escapedPatternName1a() = \x := 3 && x == 3;
test bool escapedPatternName1b() = \a-x := 3 && \a-x == 3;

test bool escapedPatternName2a() = int \x := 3 && x == 3;
test bool escapedPatternName2b() = int \a-x := 3 && \a-x == 3;

test bool escapedPatternName3a() = \y: int \x := 3 && y == 3;
test bool escapedPatternName3b() = \a-y: int \x := 3 && \a-y == 3;

test bool escapedPatternName4a() = [\x] := [3] && x == 3;
test bool escapedPatternName4b() = [\a-x] := [3] && \a-x == 3;

test bool escapedPatternName5a() = [*\x] := [3, 4] && x == [3, 4];
test bool escapedPatternName5b() = [*\a-x] := [3, 4] && \a-x == [3, 4];

test bool escapedPatternName6a() = [*int \x] := [3, 4] && x == [3, 4];
test bool escapedPatternName6b() = [*int \a-x] := [3, 4] && \a-x == [3, 4];

test bool escapedPatternName7a() = {\x} := {3} && x == 3;
test bool escapedPatternName7b() = {\a-x} := {3} && \a-x == 3;

test bool escapedPatternName8a() = {*\x} := {3, 4} && x == {3, 4};
test bool escapedPatternName8b() = {*\a-x} := {3, 4} && \a-x == {3, 4};

test bool escapedPatternName9a() = {*int \x} := {3, 4} && x == {3, 4};
test bool escapedPatternName9b() = {*int \a-x} := {3, 4} && \a-x == {3, 4};

test bool escapedPatternName10a() = <\x> := <3> && x == 3;
test bool escapedPatternName10b() = <\a-x> := <3> && \a-x == 3;

@ignoreInterpreter{
TBD
}
test bool escapedPatternName11a() = /a<\z:[b]>c/ := "abc" && z == "b";

@ignoreInterpreter{
TBD
}
test bool escapedPatternName11b() = /a<\a-z:[b]>c/ := "abc" && \a-z == "b";

test bool escapedWhileLabel() {
    \a-l: while(true){
            break \a-l;
        }
    return true;
}

test bool escapedDoLabel() {
    \a-l: do {
            break \a-l;
        } while(true);
    return true;
}

test bool escapedForLabel() {
    \a-l: for(x <- [1..10]){
            break \a-l;
          }
    return true;
}


