module lang::rascalcore::check::tests::usedef::Constructor

import lang::rascalcore::check::tests::usedef::Util;

// -- Constructor positional parameters --

test bool defInConstructorParameterPositional() = testUseDef(
    "data D = c(int i, int() f = int() { return i; });
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInConstructorParameterPositionalAndFunctionParameterPositional() = testUseDef(
    "data D = c(int i, int(int) f = int(int i) { return i; });
    ",
    [
        useDef("i", 3, 2)
    ]
);

// @ignore
test bool defInConstructorParameterPositionalAndFunctionParameterPattern() = testUseDef(
    "data D = c(int i, int(int) f = int(int i: i) { return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInConstructorParameterPositionalAndFunctionBody() = testUseDef(
    "data D = c(int i, int() f = int() { int i = i; return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// -- Constructor keyword parameter --

test bool defInConstructorParameterKeyword() = testUseDef(
    "data D = c(int i = 5, int() f = int() { return i; });
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInConstructorParameterKeywordAndFunctionParameterPositional() = testUseDef(
    "data D = c(int i = 5, int(int) f = int(int i) { return i; });
    ",
    [
        useDef("i", 3, 2)
    ]
);

// @ignore
test bool defInConstructorParameterKeywordAndFunctionParameterPattern() = testUseDef(
    "data D = c(int i = 5, int(int) f = int(int i: i) { return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInConstructorParameterKeywordAndFunctionBody() = testUseDef(
    "data D = c(int i = 5, int() f = int() { int i = i; return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// -- Constructor common parameter --

// @ignore
test bool defInConstructorParameterCommon() = testUseDef(
    "data D(int i = 5) = c(int() f = int() { return i; });
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInConstructorParameterCommonAndFunctionParameterPositional() = testUseDef(
    "data D(int i = 5) = c(int(int) f = int(int i) { return i; });
    ",
    [
        useDef("i", 3, 2)
    ]
);

// @ignore
test bool defInConstructorParameterCommonAndFunctionParameterPattern() = testUseDef(
    "data D(int i = 5) = c(int(int) f = int(int i: i) { return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInConstructorParameterCommonAndFunctionBody() = testUseDef(
    "data D(int i = 5) = c(int() f = int() { int i = i; return i; });
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);
