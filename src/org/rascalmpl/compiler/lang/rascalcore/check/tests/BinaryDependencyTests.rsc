@synopsis{Tests for binary imports and binary compatibility:

- Can we typecheck Rascal modules that import and use other modules that
are only available as binary (e.g., as.tpl files on the libs path).
- Are modules binary compatible after certain changes?}

module lang::rascalcore::check::tests::BinaryDependencyTests

import lang::rascalcore::check::Checker;
import lang::rascalcore::check::TestConfigs;
import util::Reflective;
import IO;
import lang::rascalcore::check::Import;
import Map;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import String;


// ---- Utilities for test setup ----------------------------------------------

bool verbose = false;

data PathConfig(loc resources=|unknown:///|, loc generatedSources=|unknown:///|);

data Project
    = project(str name, map[str moduleName, str moduleText] modules, PathConfig pcfg);

void clearMemory() { remove(|memory:///| recursive = true); }

loc projectDir(str pname)
    = |memory://<pname>/|;

loc src(str pname)
    = projectDir(pname) + "src/";

loc bin(str pname)
    = projectDir(pname) + "bin/";

loc generatedSources(str pname)
    = projectDir(pname) + "generated/";

loc resources(str pname)
    = projectDir(pname) + "resources/";

Project createProject(str pname, map[str mname, str mtext] modules, PathConfig pcfg){
    remove(projectDir(pname), recursive=true);

    mkDirectory(src(pname));
    for(mname <- domain(modules)){
        writeFile(src(pname) + "<mname>.rsc", writeModule(mname, modules[mname]));
     }
    return project(pname, modules, pcfg);
}

void removeProject(Project pd){
    remove(projectDir(pd.name), recursive=true);
}

str writeModule(str mname, str mtext){
    msrc = "module <mname>
           '<trim(mtext)>";
    if(verbose) println(msrc);
    try {
        parse(#start[Module], msrc);
        return msrc;
    } catch _:
        throw "Parse error in <msrc>";
}

PathConfig createPathConfig(str pname){
    return pathConfig(
        srcs=[src(pname)],
        bin=bin(pname),
        generatedSources=generatedSources(pname),
        resources=resources(pname),
        libs=[]
    );
}

Project addModule(str mname, str mtext, Project pd){
    pd.modules[mname] = writeModule(mname, mtext);
    writeFile(src(pd.name) + "<mname>.rsc", pd.modules[mname]);
    return pd;
}

Project changeModule(str mname, str mtext, Project pd){
    if(!pd.modules[mname]?) throw "Module <mname> does not exist in <pd.name>";

    pd.modules[mname] = writeModule(mname, mtext);
    writeFile(src(pd.name) + "<mname>.rsc", pd.modules[mname]);
    return pd;
}

Project removeSourceOfModule(str mname, Project pd){
    if(!pd.modules[mname]?) throw "Cannot remove non-existing module <mname>";
    pd.modules = delete(pd.modules, mname);
    remove(src(pd.name) + "<mname>.rsc", recursive=true);
    return pd;
}

bool expectNoErrors(map[str, list[Message]] msgsMap){
    present = (/error(_,_) := msgsMap);
    if(present){
            iprintln(msgsMap);
    }
    return !present;
}

bool checkExpectNoErrors(str mname, PathConfig pcfg, list[Project] remove = []){
    cfg = rascalCompilerConfig(pcfg)[verbose=verbose][logWrittenFiles=verbose];
    if(verbose) ("checkExpectNoErrors: <mname>");
    try {
        res = expectNoErrors(checkModules([mname], cfg));
        for(p <- remove) removeProject(p);
        return res;
    } catch e:{
        println("ERROR: <e>");
        return false;
    }
}

bool expectErrors(map[str, list[Message]] msgsMap, list[str] expected){
    errors = {e | /e:error(_,_) := msgsMap};

    for(e <- errors){
        if(any(ex <- expected, findFirst(e.msg, ex)>= 0)){
                return true;
        }
    }
    if(verbose) println("expectErrors, found: <errors>");
    return false;
}

bool checkExpectErrors(str mname, list[str] expected, PathConfig pcfg, list[Project] remove = []){
    cfg = rascalCompilerConfig(pcfg)[verbose=verbose][logWrittenFiles=verbose];
    if(verbose) println("checkExpectErrors: <mname>, <expected>");
    try {
        res = expectErrors(checkModules([mname], cfg), expected);
        for(p <- remove) removeProject(p);
        return res;
    } catch e:{
        println("ERROR: <e>");
        return false;
    }
}

TModel check(str mname, RascalCompilerConfig cfg){
    ModuleStatus ms = rascalTModelForNames([mname], cfg, dummy_compile1);
    <found, tm, ms> = getTModelForModule(mname, ms);
    if(found) return tm;
    throw "check: no TModel found for <mname>";
}

// ---  Tests for source libraries --------------------------------------------

test bool importSimpleSourceModuleWithRascalAsLib(){
    libName = "test-lib";
    lib =
        createProject(
                libName,
                ("Lib": "int fib(int n) {
                        '   if (n \< 2) {
                        '        return 1;
                        '    }
                        '   return fib(n - 1) + fib(n -2);
                        '}"),
                createPathConfig(libName)
                );

    assert checkExpectNoErrors("Lib", lib.pcfg);

    rascalPCFG = getRascalPathConfig();
    clientName = "test-project";
    client =
        createProject(
            clientName,
            ("LibCall": "import Lib;
                        'import IO;
                        '
                        'int main() {
                        '  println(fib(4));
                        '  return 0;
                        '}"),
            createPathConfig(clientName)
                    [libs=[rascalPCFG.bin]]
                    [srcs=[src(clientName), src(libName)]]
                );
    return checkExpectNoErrors("LibCall", client.pcfg, remove = [lib, client]);
}

// ---- Tests for binary libraries --------------------------------------------

test bool importSimpleBinaryModule(){
    libName = "lib";
    lib =
        createProject(
                libName,
                ("A": "int aFunction() = 1;"),
                createPathConfig(libName)
                );

    assert checkExpectNoErrors("A", lib.pcfg);

    // to avoid source code leaks, we remove the entire source file A from existence
    removeSourceOfModule("A", lib);

    clientName = "client";
    client =
        createProject(
            clientName,
            ("B": "import A; // library import
                  'int bFunction() = aFunction(); // library usage
                  "),
            createPathConfig(clientName)
                    [libs=[resources(libName)]] // library dependency on where the .tpl files are
                );
    return checkExpectNoErrors("B", client.pcfg, remove = [lib, client]);
}

test bool extendTransitiveBinaryModule() {
    // create and check library
    libName = "lib";
    lib =
        createProject(libName,
                      ("A": "int aFunction() = 1;"),
                      createPathConfig(libName));

    assert checkExpectNoErrors("A", lib.pcfg);
    lib = removeSourceOfModule("A", lib);

    // create and check library client
    clientName = "client";
    client =
        createProject(clientName,
                      ("B": "extend A;                       // extension of binary library
                            'int bFunction() = aFunction();  // library usage
                            "),
                       createPathConfig(clientName)
                        [libs=[resources(libName)]]);        // dependency on library lib

    assert checkExpectNoErrors("B", client.pcfg);
    client = removeSourceOfModule("B", client);

    client =
        addModule("C",
                  "extend B; // library extension that itself depends on another library
                  'int cFunction() = bFunction() + aFunction(); // library usage from two levels of extend
                  ",
                  client);

    return  checkExpectNoErrors("C",
            client.pcfg
                [libs=[resources(libName) ,       // library dependencies for both projects we depend on
                       resources(clientName)]]
            remove = [lib, client]);
}

test bool incompatibleWithBinaryLibrary(){
    // Create project "lib" and module "M1" and then compile "M1"
    libName = "lib";
    lib = createProject(libName,
                     ("M1": "int f(int n) = n;"),
                     createPathConfig(libName)
         );
    assert checkExpectNoErrors("M1", lib.pcfg);

    // then remove M1 completely, to be sure
    lib = removeSourceOfModule("M1", lib);

    // Create project "client" and module "M2" and then compile "M2"
    // "client" uses "lib" as binary library
    clientName = "client";
    client = createProject(clientName,
                     ("M2": "import M1;        // binary import
                      'int main() = f(42, 43); // incompatible call fo f
                    "),
                     createPathConfig(clientName)
                        [libs = [resources(libName)] ]
         );
    return checkExpectErrors("M2", ["Expected 1 argument(s), found 2"], client.pcfg, remove = [lib, client]);
}

test bool incompatibleWithBinaryLibraryAfterChange(){
    // Create project "lib" and module M1 and then compile M1
    libName = "lib";
    lib = createProject(libName,
                     ("M1": "int f(int n) = n;"),
                     createPathConfig(libName)
         );
    assert checkExpectNoErrors("M1", lib.pcfg);

    // then remove M1 completely, to be sure
    lib = removeSourceOfModule("M1", lib);

    // Create project ""client" and module M2 and then compile M2
    // "client" uses "lib" as binary library
    clientName = "client";
    client = createProject(clientName,
                     ("M2": "import M1; // binary dependency
                      'int main() = f(42);
                    "),
                     createPathConfig(clientName)
                        [libs = [resources(libName)] ]
         );
    assert checkExpectNoErrors("M2", client.pcfg);

    // Change declaration of "f" in lib and recompile "M1"
    lib = addModule("M1", "int f(int n, int m) = n+m;", lib);
    assert checkExpectNoErrors("M1", lib.pcfg);

    // Call of "f" in M2 no longer complies with "f"'s signature in M1
    return checkExpectErrors("M2", ["Expected 2 argument(s), found 1"], client.pcfg, remove = [lib, client]);
}

/*
 *   rascal:  IO -+        rascal: IO'
 *     extend |   |                |
 *            v   |                |
 *   typepal: TP  |                |
 *     import |   | import         |
 *            v   v                |
 *   core:    Check <--------------+
 *
 *   TP extends IO en is used as a binary library for core.
 *   After (incompatible) modification and recompilation of IO', recompilation of Check
 *   should discover that the old version of IO (as used via the binary extend in TP) is
 *   not compatible with the new binary version of IO as imported directly in Check.
 */

test bool incompatibleVersionsOfBinaryLibrary(){
    clearMemory() ;
    rascalName = "rascal";
    rascal =
        createProject(rascalName,
                      ("IO" : "int f(int n) = n;"),
                      createPathConfig(rascalName)
        );
    assert checkExpectNoErrors("IO", rascal.pcfg);

    rascal = removeSourceOfModule("IO", rascal);

    typepalName = "typepal";
    typepal =
        createProject(typepalName,
                      ("TP": "extend IO;"),
                      createPathConfig(typepalName)
                        [libs = [resources(rascalName)]] // binary dependency on rascal
        );
    assert checkExpectNoErrors("TP", typepal.pcfg);

    coreName = "core";
    core =
        createProject(coreName,
                    ("Check": "import TP;   // binary import
                              'import IO;   // binary import
                              'value main() = f(3);
                              "),
                    createPathConfig(coreName)
                        [libs = [ resources(rascalName),
                                  resources(typepalName)] ] // binary dependency on rascal and typepal
        );
    assert checkExpectNoErrors("Check", core.pcfg);

    // Make incompatible change to IO and recheck
    rascal = addModule("IO", "int f(int n, int m) = n+m;", rascal);
    assert checkExpectNoErrors("IO", rascal.pcfg);

    // Important: we do not recompile TP (and thus it will contain the outdated version of IO)

    // Recompile Check and discover the error
    return checkExpectErrors("Check", ["Recompilation or reconfiguration needed: binary module `TP` uses incompatible module(s)"], core.pcfg, remove = [rascal, typepal, core]);
}

// ---- Binary compatibility of two TModels -----------------------------------

// ---- Utilities -------------------------------------------------------------

AGrammar getGrammar(TModel tm){
    if(!tm.store[key_grammar]?){
        throw "`grammar` not found in store";
    } else if([*AGrammar gs] := tm.store[key_grammar]){
        return gs[0];
    } else {
        throw "`grammar` has incorrect format in store";
    }
}
// The binary compatibility test for TModels

bool binaryCompatible(tuple[TModel old, TModel new] tms){
    if(verbose) iprintln(domain(tms.old.logical2physical));
    if(verbose) iprintln(domain(tms.new.logical2physical));
    return getGrammar(tms.old) == getGrammar(tms.new)
           && domain(tms.old.logical2physical) <= domain(tms.new.logical2physical);
}

tuple[TModel, TModel] createVersions(str v1, str v2){
    pname = "a";
    mname = "M1";
    p = createProject(pname,
                     (mname: v1),
                     createPathConfig(pname)
         );
    tm1 = check(mname, rascalCompilerConfig(p.pcfg));

    p = changeModule(mname, v2, p);
    tm2 = check(mname, rascalCompilerConfig(p.pcfg));
    removeProject(p);
    return <tm1, tm2>;
}

// ---- Tests for module (in)compatibility ------------------------------------

test bool compatibleAfterAddingFunction()
    = binaryCompatible(createVersions("int f(int n) = n;", "int f(int n) = n;
                                                           'int g() = 42;"));

test bool notCompatibleAfterRemovingFunction()
    = !binaryCompatible(createVersions("int f(int n) = n;
                                       'int g() = 42;", "int f(int n) = n;"));

test bool notCompatibleAfterChangingFunction()
    = !binaryCompatible(createVersions("int f(int n) = n;", "int f(int n, int m) = n+m;"));

test bool notCompatibleAfterChangingFunctionArgument()
    = !binaryCompatible(createVersions("int f(int n) = n;", "int f(int m) = m;"));

test bool notCompatibleAfterChangingFunctionReturnType()
    = !binaryCompatible(createVersions("int f(int n) = n;", "num f(int m) = m;"));

test bool compatibleAfterChangingFunctionBody()
    = binaryCompatible(createVersions("int f(int n) = n;", "int f(int n) = n + 1;"));

test bool compatibleAfterAddingConstructor()
    = binaryCompatible(createVersions("data D = d1();", "data D = d1() | d2(int n);"));

test bool notCompatibleAfterRemovingConstructor()
    = !binaryCompatible(createVersions("data D = d1() | d2(int n);", "data D = d1();"));

test bool notCompatibleAfterAddingAlternative()
    = !binaryCompatible(createVersions("syntax A = \"a\";", "syntax A = \"a\" | \"b\";"));