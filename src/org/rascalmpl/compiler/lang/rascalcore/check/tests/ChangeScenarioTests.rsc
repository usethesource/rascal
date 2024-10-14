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