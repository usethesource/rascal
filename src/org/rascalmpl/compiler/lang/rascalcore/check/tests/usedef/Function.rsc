module lang::rascalcore::check::tests::usedef::Function


// -- Function positional parameter --

test bool defInFunctionParameterPositional() = testUseDef(
    "int f(int i) {
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInFunctionParameterPositionalAndFunctionParameterPositional() = testUseDef(
    "int f(int i) {
    '   int g(int i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 2),
        useDef("i", 4, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPositionalAndFunctionParameterKeyword() = testUseDef(
    "int f(int i) {
    '   int g(int i = i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPositionalAndFunctionParameterPattern() = testUseDef(
    "int f(int i) {
    '   int g(int i: i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPositionalAndFunctionBody() = testUseDef(
    "int f(int i) {
    '   int g() {
    '       int i = i;
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// -- Function keyword parameter --

// @ignore
test bool defInFunctionParameterKeyword() = testUseDef(
    "int f(int i = 5) {
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1)
    ]
);

// @ignore
test bool defInFunctionParameterKeywordAndFunctionParameterPositional() = testUseDef(
    "int f(int i = 5) {
    '   int g(int i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 2),
        useDef("i", 4, 1)
    ]
);

// @ignore
test bool defInFunctionParameterKeywordAndFunctionParameterKeyword() = testUseDef(
    "int f(int i) {
    '   int g(int i = i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterKeywordAndFunctionParameterPattern() = testUseDef(
    "int f(int i) {
    '   int g(int i: i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterKeywordAndFunctionBody() = testUseDef(
    "int f(int i) {
    '   int g() {
    '       int i = i;
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// -- Function pattern parameter --

test bool defInFunctionParameterPattern() = testUseDef(
    "int f(int i: 5) {
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInFunctionParameterPatternAndFunctionParameterPositional() = testUseDef(
    "int f(int i: 5) {
    '   int g(int i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 2),
        useDef("i", 4, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPatternAndFunctionParameterKeyword() = testUseDef(
    "int f(int i: 5) {
    '   int g(int i = i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPatternAndFunctionParameterPattern() = testUseDef(
    "int f(int i: 5) {
    '   int g(int i: i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionParameterPatternAndFunctionBody() = testUseDef(
    "int f(int i: 5) {
    '   int g() {
    '       int i = i;
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// -- Function body --

test bool defInFunctionBody() = testUseDef(
    "int f() {
    '   int i = 5;
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInFunctionBodyAndFunctionParameterPositional() = testUseDef(
    "int f() {
    '   int i = 5;
    '   int g(int i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 2),
        useDef("i", 4, 1)
    ]
);

// @ignore
test bool defInFunctionBodyAndFunctionParameterKeyword() = testUseDef(
    "int f() {
    '   int i = 5;
    '   int g(int i = i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

test bool defInFunctionBodyAndFunctionParameterPatternUnlabeled() = testUseDef(
    "int f() {
    '   int i = 5;
    '   int g(i: int i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1),
        useDef("i", 4, 3),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInFunctionBodyAndFunctionParameterPatternLabeled() = testUseDef(
    "int f() {
    '   int i = 5;
    '   int g(i: int i: i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1),
        useDef("i", 4, 1),
        useDef("i", 5, 3),
        useDef("i", 6, 1)
    ]
);

// @ignore
test bool defInFunctionBodyAndFunctionParameterPatternRegularExpression() = testUseDef(
    "str f() {
    '   str s = \"foo\";
    '   str g(s: /\<s:\<s\>\>/) {
    '       return s;
    '   }
    '   return s;
    '}
    ",
    [
        useDef("s", 2, 1),
        useDef("s", 4, 1),
        useDef("s", 5, 3),
        useDef("s", 6, 1)
    ]
);

// @ignore
test bool defInFunctionBodyAndFunctionParameterPatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree f() {
    '   Tree t = char(53);
    '   Tree g(t: (Parent) `\<Child t\>`) {
    '       return t;
    '   }
    '   return t;
    '}
    ",
    [
        useDef("t", 2, 1),
        useDef("t", 4, 1),
        useDef("t", 5, 1)
    ]
);

// @ignore
test bool defInFunctionBodyAndFunctionBody() = testUseDef(
    "int f() {
    '   int i = 5;
    '   int g() {
    '       int i = i;
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);
