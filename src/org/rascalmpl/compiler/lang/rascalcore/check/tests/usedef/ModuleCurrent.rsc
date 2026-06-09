module lang::rascalcore::check::tests::usedef::ModuleCurrent

import lang::rascalcore::check::tests::usedef::Util;

test bool defInModuleCurrent() = testUseDef(
    "int i = 5;
    'int f() {
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1)
    ]
);

test bool defInModuleCurrentAndConstructorParameterPositional() = testUseDef(
    "int i = 5;
    'data D = c(int i, int j = i);
    ",
    [
        useDef("i", 3, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndConstructorParameterKeyword() = testUseDef(
    "int i = 5;
    'data D = c(int i = i, int j = i);
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndConstructorParameterCommon() = testUseDef(
    "int i = 5;
    'data D(int i = i) = c(int j = i);
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

test bool defInModuleCurrentAndFunctionParameterPositional() = testUseDef(
    "int i = 5;
    'int f(int i) {
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndFunctionParameterKeyword() = testUseDef(
    "int i = 5;
    'int f(int i = i) {
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

test bool defInModuleCurrentAndFunctionParameterPatternLabeled() = testUseDef(
    "int i = 5;
    'int f(int i: i) {
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndFunctionParameterPatternRegularExpression() = testUseDef(
    "str s = \"foo\";
    'str f(/\<s:\<s\>\>/) {
    '   return s;
    '}
    ",
    [
        useDef("s", 3, 1),
        useDef("s", 4, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndFunctionParameterPatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree t = char(53);
    'Tree f((Parent) `\<Child t\>`) {
    '   return t;
    '}
    ",
    [
        useDef("t", 3, 1),
        useDef("t", 4, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndFunctionBody() = testUseDef(
    "int i = 5;
    'int f() {
    '   int i = i;
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfMatchPatternLabeled() = testUseDef(
    "int i = 5;
    'int f() {
    '   if (int i: i := i) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 2),
        useDef("i", 6, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfMatchPatternRegularExpression() = testUseDef(
    "str s = \"foo\";
    'str f() {
    '   if (/\<s:\<s\>\>/ := s) {
    '       return s;
    '   }
    '   return s;
    '}
    ",
    [
        useDef("s", 3, 1),
        useDef("s", 4, 2),
        useDef("s", 5, 2),
        useDef("s", 6, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfMatchPatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree t = char(53);
    'Tree f() {
    '   if ((Parent) `\<Child t\>` := t) {
    '       return t;
    '   } 
    '   return t;
    '}
    ",
    [
        useDef("t", 3, 1),
        useDef("t", 4, 2),
        useDef("t", 5, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfEnumeratePatternLabeled() = testUseDef(
    "int i = 5;
    'int f() {
    '   if (int i: i \<- [i]) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 2),
        useDef("i", 6, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfEnumeratePatternRegularExpression() = testUseDef(
    "str s = \"foo\";
    'str f() {
    '   if (/\<s:\<s\>\>/ \<- [s]) {
    '       return s;
    '   }
    '   return s;
    '}
    ",
    [
        useDef("s", 3, 1),
        useDef("s", 4, 2),
        useDef("s", 5, 2),
        useDef("s", 6, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfEnumeratePatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree t = char(53);
    'Tree f() {
    '   if ((Parent) `\<Child t\>` \<- [t]) {
    '       return t;
    '   } 
    '   return t;
    '}
    ",
    [
        useDef("t", 3, 1),
        useDef("t", 4, 2),
        useDef("t", 5, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfVisitPatternLabeled() = testUseDef(
    "int i = 5;
    'int f() {
    '   if (i == visit(i) { case int i: i =\> i }) {
    '       return i;
    '   }
    '   return i;
    '}
    ",
    [
        useDef("i", 2, 1),
        useDef("i", 3, 1),
        useDef("i", 5, 1),
        useDef("i", 6, 4),
        useDef("i", 7, 1),
        useDef("i", 8, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfVisitPatternRegularExpression() = testUseDef(
    "str s = \"foo\";
    'str f() {
    '   if (s == visit(s) { case /\<s:\<s\>\>/ =\> s }) {
    '       return s;
    '   }
    '   return s;
    '}
    ",
    [
        useDef("s", 2, 1),
        useDef("s", 3, 1),
        useDef("s", 5, 1),
        useDef("s", 6, 4),
        useDef("s", 7, 1),
        useDef("s", 8, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndIfVisitPatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree t = char(53);
    'Tree f() {
    '   if (t == visit(t) { case (Parent) `\<Child t\>` =\> (Parent) `\<Child t\>` }) {
    '       return t;
    '   } 
    '   return t;
    '}
    ",
    [
        useDef("t", 2, 1),
        useDef("t", 3, 1),
        useDef("t", 5, 4),
        useDef("t", 6, 1),
        useDef("t", 7, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndCatchPatternLabeled() = testUseDef(
    "int i = 5;
    'int f() {
    '   try {;} catch (int i: i): {
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
test bool defInModuleCurrentAndCatchPatternRegularExpression() = testUseDef(
    "str s = \"foo\";
    'str f() {
    '   try {;} catch /\<s:\<s\>\>/: {
    '       return s;
    '   }
    '   return s;
    '}
    ",
    [
        useDef("i", 3, 1),
        useDef("i", 4, 2),
        useDef("i", 5, 1)
    ]
);

// @ignore
test bool defInModuleCurrentAndCatchPatternConcrete() = testUseDef(
    "import ParseTree;
    'syntax Parent = Child;
    'syntax Child = [0-9];
    'Tree t = char(53);
    'Tree f() {
    '   try {;} catch (Parent) `\<Child t\>`: {
    '       return t;
    '   } 
    '   return t;
    '}
    ",
    [
        useDef("t", 3, 2),
        useDef("t", 4, 1)
    ]
);
