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

    try {
        exec.eval(#void, "import Z;");
        return false;
    }
    catch ModuleLoadMessages([error(_,_)]): {
        // that's ok
        ;
    }

    writeFile(moduleFile("Z"), "module Z public str aap = \"aap\";");

    return exec.eval(#void, "import Z;") == ok()
        && result("aap") == exec.eval(#str, "aap");
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

