module lang::rascal::tests::loading::LoadingErrorModules

import IO;
import util::Eval;
import util::PathConfig;

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