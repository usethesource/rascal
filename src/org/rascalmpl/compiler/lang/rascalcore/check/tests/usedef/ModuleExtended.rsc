module lang::rascalcore::check::tests::usedef::ModuleExtended

import lang::rascalcore::check::tests::usedef::Util;

test bool defInModuleExtended() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'int f() {
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 1>, <"X", 1>)
    ]
);

test bool defInModuleExtendedAndConstructorParameterPositional() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'data D = c(int i, int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleExtendedAndConstructorParameterKeyword() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'data D = c(int i = i, int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleExtendedAndConstructorParameterCommon() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'data D(int i = i) = c(int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

test bool defInModuleExtendedAndFunctionParameterPositional() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'int f(int i) {
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 2>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleExtendedAndFunctionParameterKeyword() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'int f(int i = i) {
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleExtendedAndFunctionParameterPattern() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'int f(int i: i) {
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleExtendedAndFunctionBody() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "extend X;
        'int f() {
        '   int i = i;
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);
