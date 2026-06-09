module lang::rascalcore::check::tests::usedef::ModuleImported

import lang::rascalcore::check::tests::usedef::Util;

test bool defInModuleImported() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
        'int f() {
        '   return i;
        '}
        "
    ),
    [
        useDef("i", <"Y", 1>, <"X", 1>)
    ]
);

test bool defInModuleImportedAndConstructorParameterPositional() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
        'data D = c(int i, int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleImportedAndConstructorParameterKeyword() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
        'data D = c(int i = i, int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

// @ignore
test bool defInModuleImportedAndConstructorParameterCommon() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
        'data D(int i = i) = c(int j = i);
        "
    ),
    [
        useDef("i", <"Y", 2>, <"X", 1>),
        useDef("i", <"Y", 3>, <"Y", 1>)
    ]
);

test bool defInModuleImportedAndFunctionParameterPositional() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
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
test bool defInModuleImportedAndFunctionParameterKeyword() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
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
test bool defInModuleImportedAndFunctionParameterPattern() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
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
test bool defInModuleImportedAndFunctionBody() = testUseDef(
    (
        "X":
        "public int i = 5;
        ",
        "Y":
        "import X;
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
