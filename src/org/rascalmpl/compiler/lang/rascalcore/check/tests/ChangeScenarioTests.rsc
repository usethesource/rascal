@synopsis{Tests for common change scenarios}
module lang::rascalcore::check::tests::ChangeScenarioTests

import lang::rascalcore::check::tests::StaticTestingUtils;

test bool fixMissingImport(){
    clearMemory();
    assert missingModuleInModule("module B import A;");
    writeModule("module A");
    return checkModuleOK("module C import B;");
}

test bool fixMissingExtend(){
    clearMemory();
    assert missingModuleInModule("module B extend A;");
    writeModule("module A");
    return checkModuleOK("module C extend B;");
}

test bool fixErrorInImport(){
    clearMemory();
    assert checkModuleOK("module A public bool b = false;");
    moduleB = "module B import A; int n = b + 1;";
    assert unexpectedTypeInModule(moduleB);
    assert checkModuleOK("module A public int b = 0;"); // change b to type int
    return checkModuleOK(moduleB);
}

test bool fixErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A bool b = false;");
    moduleB = "module B extend A; int n = b + 1;";
    assert unexpectedTypeInModule(moduleB);
    assert checkModuleOK("module A int b = 0;"); // change b to type int
    return checkModuleOK(moduleB);
}

test bool introduceErrorInImport(){
    clearMemory();
    assert checkModuleOK("module A public int b = 0;");
    moduleB = "module B import A; int n = b + 1;";
    assert checkModuleOK(moduleB);
    assert checkModuleOK("module A public bool b = false;");
    return unexpectedTypeInModule(moduleB);
}

test bool introduceErrorInExtend(){
    clearMemory();
    assert checkModuleOK("module A int b = 0;");
    moduleB = "module B extend A; int n = b + 1;";
    assert checkModuleOK(moduleB);
    assert checkModuleOK("module A bool b = false;");
    return unexpectedTypeInModule(moduleB);
}

test bool removeImportedModuleAndRestoreIt1(){
    clearMemory();
    assert checkModuleOK("module A");
    moduleB = "module B import A;";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK("module A");
    return checkModuleOK(moduleB);
}

test bool removeImportedModuleAndRestoreIt2(){
    clearMemory();
    moduleA = "module A int twice(int n) = n * n;";
    assert checkModuleOK(moduleA);
    moduleB = "module B import A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeExtendedModuleAndRestoreIt1(){
    clearMemory();
    moduleA = "module A";
    assert checkModuleOK(moduleA);
    moduleB = "module B extend A;";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeExtendedModuleAndRestoreIt2(){
    clearMemory();
    moduleA = "module A int twice(int n) = n * n;";
    assert checkModuleOK(moduleA);
    moduleB = "module B extend A; int quad(int n) = twice(twice(n));";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);
    assert checkModuleOK(moduleA);
    return checkModuleOK(moduleB);
}

test bool removeOverloadAndRestoreIt(){
    clearMemory();
    moduleA1 = "module A
                int dup(int n) = n + n;
                str dup(str s) = s + s;";
    moduleA2 = "module A
                int dup(int n) = n + n;";
    assert checkModuleOK(moduleA1);
    moduleB = "module B import A;  str f(str s) = dup(s);";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);

    assert checkModuleOK(moduleA2);
    assert argumentMismatchInModule(moduleB);
    assert checkModuleOK(moduleA1);
    return checkModuleOK(moduleB);
}

test bool removeConstructorAndRestoreIt(){
    clearMemory();
    moduleA1 = "module A
                data D = d(int n) | d(str s);";
    moduleA2 = "module A
                data D = d(int n);";
    assert checkModuleOK(moduleA1);
    moduleB = "module B import A;  D f(str s) = d(s);";
    assert checkModuleOK(moduleB);
    removeModule("A");
    assert missingModuleInModule(moduleB);

    assert checkModuleOK(moduleA2);
    assert argumentMismatchInModule(moduleB);
    assert checkModuleOK(moduleA1);
    return checkModuleOK(moduleB);
}

// ---- incremental type checking ---------------------------------------------
// Legend:
//      X ==> Y: replace X by Y
//      X!     : X is checked
// Scenarios:
//      A1!     A1  ==> A2!     A2  ==> A3!     A3
//              |       |       |       |       |
//              B1!     B1      B1      B1 ==>  B2!
//                              |       |       |
//                              C1!     C1      C1

test bool nobreakingChange1(){
    clearMemory();
    moduleA1 = "module A";
    moduleA2 = "module A
                    public int n = 3;";
    moduleA3 = "module A
                    public int n = 3;
                    data D = d1();";

    assert checkedInModule(moduleA1, ["A"]);

    moduleB1 = "module B
                    import A;";
    moduleB2 = "module B
                    import A;
                    public int m = n + 1;";
    assert checkedInModule(moduleB1, ["B"]);

    writeModule(moduleA2);
    assert checkedInModule(moduleB1, ["A"]);

    moduleC1 = "module C
                    import B;
                    int f() = 2;";
    assert checkedInModule(moduleC1, ["C"]);

    writeModule(moduleA3);

    assert checkedInModule(moduleC1, ["A"]);

    writeModule(moduleB2);
    return checkedInModule(moduleC1, ["B"]);
}

//      A1!------+          A1--------+
//      |        |          |         |
//      B1!--+   |    ==>   B2!--+    |
//      |    |   |          |    |    |
//      C1!  +---D1!        C1   +----D1
//      |        |          |         |
//      +---E!---+          +----E----+

test bool nobreakingChange2(){
    clearMemory();
    moduleA1 = "module A";
    moduleB1 = "module B import A;";
    moduleC1 = "module C import B;";
    moduleD1 = "module D import A; import B;";
    moduleE1 = "module E import C; import D;";

    moduleB2 = "module B import A; int n = 0;";

    writeModule(moduleA1);
    writeModule(moduleB1);
    writeModule(moduleC1);
    writeModule(moduleD1);
    writeModule(moduleE1);

    assert checkedInModule(moduleE1, ["A", "B", "C", "D", "E"]);

    writeModule(moduleB2);
    return checkedInModule(moduleC1, ["B"]);
}

//      A1!------+          A1--------+
//      |        |          |         |
//      B1!--+   |    ==>   B2!--+    |
//      |    |   |          |    |    |
//      C1!  +---D1!        C1!  +----D1
//      |        |          |         |
//      +---E!---+          +----E----+

test bool breakingChange1(){
    clearMemory();
    moduleA1 = "module A";
    moduleB1 = "module B import A; int b() = 1;";
    moduleC1 = "module C import B; int c() = b();";
    moduleD1 = "module D import A; import B;";
    moduleE1 = "module E import C; import D;";

    moduleB2 = "module B import A; int b(int n) = n;";

    writeModule(moduleA1);
    writeModule(moduleB1);
    writeModule(moduleC1);
    writeModule(moduleD1);
    writeModule(moduleE1);

    assert checkedInModule(moduleE1, ["A", "B", "C", "D", "E"]);

    writeModule(moduleB2);
    return checkedInModule(moduleC1, ["B", "C"]);
}