module lang::rascal::tests::concrete::ParameterizedNonTerminals

layout Whitespace = [\ \t\n]*;
syntax A = "a";
syntax B = "b";

syntax P[&T] = "{" &T ppar "}";

syntax Q[&T] = "[" P[&T] qpar "]";

syntax R[&T] = "(" Q[&T] rpar ")";

syntax PA = P[A] papar;

syntax QA = Q[A] qapar;

syntax RA = R[A] rapar;

test bool PA1() = "<(PA) `{a}`>" == "{a}";

test bool PA2() = "<[PA] "{a}">" == "{a}";

test bool PA3(){
    PA pa =  (PA) `{a}`;
    return "<pa>" == "{a}";
}

test bool PA4(){
    PA pa1 = (PA) `{a}`;
    PA pa2 = [PA] "{a}";
    return "<pa1>" == "<pa2>";
}

test bool PA5(){
    PA pa1 = (PA) `{a}`;
    PA pa2 = [PA] "{a}";
    return pa1 := pa2 && pa2 := pa1;
}

test bool PA5a() = "<((PA) `{a}`).papar.ppar>" == "a";

test bool PA6() = "<(P[A]) `{a}`>" == "{a}";

test bool PA7() = "<(P[A]) `{a}`>" == "<(PA) `{a}`>";

test bool PA8() = "<(P[A]) `{a}`>" == "<[PA] "{a}">";

@ignoreInterpreter{
Gives: Undeclared type: P
}
test bool PA9() = "<(P[A]) `{a}`>" == "<[P[A]] "{a}">";

@ignoreInterpreter{
Gives: Syntax error: concrete syntax fragment
}
test bool PB1() = "<(P[B]) `{b}`>" == "{b}";

@ignoreInterpreter{
Gives: Syntax error: concrete syntax fragment
}
test bool PB2() = "<[P[B]] "{b}">" == "{b}";

test bool QA1() = "<(QA) `[{a}]`>" == "[{a}]";
test bool QA2() = "<[QA] "[{a}]">" == "[{a}]";
test bool QA3() = "<((QA) `[{a}]`).qapar.qpar.ppar>" == "a";

test bool RA1() = "<(RA) `([{a}])`>" == "([{a}])";
test bool RA2() = "<[RA] "([{a}])">" == "([{a}])";
test bool RA3() = "<((RA) `([{a}])`).rapar.rpar.qpar.ppar>" == "a";

