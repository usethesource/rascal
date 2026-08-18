module lang::rascal::tests::loading::LoadingErrorModules

import IO;
import util::Eval;
import util::PathConfig;
import Message;

PathConfig init() = pathConfig(srcs=[|memory://LoadingErrorModules/|]);

loc moduleFile(str name) = |memory://LoadingErrorModules/| + "<name>.rsc";

test bool moduleWithParseError() {
    exec = createRascalRuntime(pcfg=init());

    writeFile(moduleFile("A"), "modle A");

    try {
        exec.eval(#void, "import A;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("A"), "module A");

    return exec.eval(#void, "import A;") == ok();
}

test bool moduleWithTransientParseError() {
    exec = createRascalRuntime(pcfg=init());

    writeFile(moduleFile("A"), "module A");
    assert exec.eval(#void, "import A;") == ok();
    writeFile(moduleFile("A"), "modle A");

    try {
        exec.eval(#void, "import A;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("A"), "module A");

    return exec.eval(#void, "import A;") == ok();
}

test bool moduleWithTransitiveParseError() {
    exec = createRascalRuntime(pcfg=init());

    writeFile(moduleFile("A"), "modle A");
    writeFile(moduleFile("B"), "module B import A;");

    try {
        exec.eval(#void, "import B;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("A"), "module A");

    return exec.eval(#void, "import A;") == ok()
        && exec.eval(#void, "import B;") == ok();
}

test bool moduleWithStaticError() {
    exec = createRascalRuntime(pcfg=init());

    writeFile(moduleFile("A"), "module A str aap = 42;");

    try {
        exec.eval(#void, "import A;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("A"), "module A str aap = \"42\";");

    return exec.eval(#void, "import A;") == ok();
}

test bool importNonExistingModule() {
    exec = createRascalRuntime(pcfg=init());
    
    // clean slate
    remove(moduleFile("ZZ"));

    writeFile(moduleFile("A"), 
        "module A 
        'import ZZ; 
        'str func() = foo();
        '");
 
    try {
        exec.eval(#void, "import A;");
        return false;
    }
    catch ModuleLoadMessages([error(_m, _l)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("ZZ"), 
        "module ZZ 
        'str foo() = \"bar\";
        '");

    try {
        res = exec.eval(#void, "import A;");
        println("res: <res>");
        res = exec.eval(#str, "func()");
        println("res2: <res>");
        return res == ok() && result("bar") == res;
    }
    catch ModuleLoadMessages(msgs): {
        iprintln("unexpected messages: <msgs>");
        return false;
    }
    catch StaticError(str message, loc location): {
        println("unexpected static error: <message> @ <location>");
        return false;
    }
    catch value v: {
        println("some exception <v>");
        return false;
    }
}


test bool importBrokenModuleName() {
    exec = createRascalRuntime(pcfg=init());

    writeFile(moduleFile("AAA"), "module AA public str aap = \"aap\";");

    try {
        exec.eval(#void, "import AAA;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("AAA"), "module AAA public str aap = \"aap\";");

    return exec.eval(#void, "import AAA;") == ok()
        && result("aap") == exec.eval(#str, "aap");
}

