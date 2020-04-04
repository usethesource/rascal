module lang::rascalcore::check::tests::ParameterizedTypes

import lang::rascalcore::check::tests::StaticTestingUtils;


test bool issue1300a() =
    unexpectedType("&T f(&T param) {
                   '  Wrap[&T] x = wrap(param);
                   '
                   ' return id(x);
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);
                                   
test bool issue1300b() =
    unexpectedType("&S f(&S param) {
                   '  Wrap[&S] x = wrap(param);
                   '
                   ' return id(x);
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);                                   
                                   
test bool issue1300c() =
    unexpectedType("&T f(&T param) {
                   '  Wrap[&T] x = wrap(param);
                   '
                   ' return x;
                   '}",
                   initialDecls = ["data Wrap[&T] = wrap(&T val);
                                   '&T id(&T arg) = arg;"]);
                                   
test bool issue1386a() = checkOK("bool f(type(symbol,definitions)) = true;");
                                   
test bool issue1386b() = checkOK("bool f(type[&T] _: type(symbol,definitions)) = true;");

test bool issue1386c() = checkOK("bool f(type[&T] x: type(symbol,definitions)) = true;");
                                   
