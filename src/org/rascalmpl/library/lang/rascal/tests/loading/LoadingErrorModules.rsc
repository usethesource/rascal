module lang::rascal::tests::loading::LoadingErrorModules

import IO;
import util::Eval;
import util::PathConfig;
import Message;

PathConfig init() = pathConfig(projectRoot=|cwd:///|, srcs=[|memory://LoadingErrorModules/|]);

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
    catch ModuleLoadMessages([error(m, l)]): {
        // that's ok
        println("expected message: <m> @<l>");
    }

    writeFile(moduleFile("ZZ"), 
        "module ZZ 
        'str foo() = \"bar\";
        '");

    try {
        res0 = exec.eval(#void, "import ZZ;");
        res1 = exec.eval(#void, "import A;");
        res2 = exec.eval(#str, "func()");
        return res0 == ok() && res1 == ok() && result("bar") == res2;
    }
    catch ModuleLoadMessages(msgs): {
        println("unexpected messages:");
        iprintln(msgs);
        return false;
    }
    catch StaticError(str message, loc location): {
        println("unexpected static error: <message> @ <location>");
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

