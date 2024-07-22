@synopsis{Tests if we can typecheck Rascal modules that import and use other modules that
are only available as .tpl files on the libs path.}
module lang::rascalcore::check::tests::BinaryDependencyTest

import lang::rascalcore::check::Checker;
import util::Reflective;
import IO;
import lang::rascalcore::check::Import;
import Map;

data PathConfig(loc resources=|unknown:///|, loc generatedSources=|unknown:///|);

data Project 
    = project(str name, map[str moduleName, str moduleText] modules, PathConfig pcfg);
    
str makeModule(str mname, str mtext)
    = "module <mname>
      '<mtext>
      ";

Project createProject(str pname, map[str mname, str mtext] modules, PathConfig pcfg){
    remove(|memory://<pname>/|, recursive=true);
    
    mkDirectory(|memory://<pname>/src|);
    for(mname <- domain(modules)){
        writeFile(|memory://<pname>/src/<mname>.rsc|,
                  makeModule(mname, modules[mname]));
     }
    return project(pname, modules, pcfg);
}

PathConfig createPathConfig(str pname){
    return pathConfig(
        srcs=[|memory://<pname>/src|],
        bin=|memory://<pname>/bin|,
        generatedSources=|memory://<pname>/generated|,
        resources=|memory://<pname>/resources|,
        libs=[]
    );
}

Project addModule(str mname, str mtext, Project pd){
    pd.modules[mname] = makeModule(mname, mtext);
    writeFile(|memory://<pd.name>/src/<mname>.rsc|, pd.modules[mname]);
    return pd;
}

Project changeModule(str mname, str mtext, Project pd){
    if(!pd.modules[mname]?) throw "Module <mname> does not exist in <pd.name>";

    pd.modules[mname] = makeModule(mname, mtext);
    writeFile(|memory://<pd.name>/src/<mname>.rsc|, pd.modules[mname]);
    return pd;
}

Project removeModule(str mname, Project pd){
    if(!pd.modules[mname]?) throw "Cannot remove non-existing module <mname>";
    pd.modules = delete(pd.modules, mname);
    remove(|memory://<pd.name>/src/<mname>.rsc|, recursive=true);
    return pd;
}

void removeProject(Project pd){
    remove(|memory://<pd.name>/|, recursive=true);
}

bool expectNoErrors(map[str, list[Message]] msgsMap){
    present = (/error(_,_) := msgsMap);
    if(present){
            iprintln(msgsMap);
    }
    return !present;
}

// check module mname (and store tmodel in cfg.pcfg.resources/rascal/$<mname>.tpl), expect no errors
bool checkExpectNoErrors(str mname, RascalCompilerConfig cfg){
    println("checkExpectNoErrors: <mname>");
    try {
        return expectNoErrors(checkModules([mname], cfg[verbose=true][logWrittenFiles=true]));
    } catch e:{
        println("ERROR: <e>");
        return false;
    }
}

test bool importSimpleBinaryModule(){
    myTestLibraryName = "myTestLibrary";
    myTestLibrary =
        createProject(
                myTestLibraryName,
                ("A": "int aFunction() = 1;"),
                createPathConfig(myTestLibraryName)
                );

    assert checkExpectNoErrors("A", rascalCompilerConfig(myTestLibrary.pcfg));
    
    // to avoid source code leaks, we remove the entire source file A from existence
    removeModule("A", myTestLibrary);
    
    myTestLibraryClientName = "myTestLibraryClient";
    myTestLibraryClient =
        createProject(
            myTestLibraryClientName,
            ("B": "import A; // library import
                  'int bFunction() = aFunction(); // library usage
                  "),
            createPathConfig(myTestLibraryClientName)
                    [libs=[|memory://<myTestLibraryName>/resources|]] // library dependency on where the .tpl files are
                );
    return checkExpectNoErrors("B", rascalCompilerConfig(myTestLibraryClient.pcfg));
}

test bool extendTransitiveBinaryModule() {
    // create and check library
    myTestLibraryName = "myTestLibrary";
    myTestLibrary =
        createProject(myTestLibraryName,
                      ("A": "int aFunction() = 1;"),
                      createPathConfig(myTestLibraryName));

    assert checkExpectNoErrors("A", rascalCompilerConfig(myTestLibrary.pcfg));
println("STEP 1");
    myTestLibrary = removeModule("A", myTestLibrary);

    // create and check library client
    myTestLibraryClientName = "myTestLibraryClient";

    myTestLibraryClient = 
        createProject(myTestLibraryClientName,
                      ("B": "extend A; // library extension
                            'int bFunction() = aFunction(); // library usage
                            "),
                       createPathConfig(myTestLibraryClientName)
                        [libs=[|memory://myTestLibrary/resources|]]); // library dependency on where the .tpl files are;
    
    assert checkExpectNoErrors("B", rascalCompilerConfig(myTestLibraryClient.pcfg));
    myTestLibraryClient = removeModule("B", myTestLibraryClient);

    myTestLibraryClient = 
        addModule("C",
                  "extend B; // library extension that itself depends on another library
                  'int cFunction() = bFunction() + aFunction(); // library usage from two levels of extend
                  ",
                  myTestLibraryClient);
        
    return checkExpectNoErrors("C", rascalCompilerConfig(
            myTestLibraryClient.pcfg
                [libs=[|memory://myTestLibrary/resources| ,         // library dependency on where the .tpl files are
                       |memory://myTestLibraryClient/resources|]] // for both projects we depend on
            ));
}

test bool incompatibelBinaryOnlyImport(){
    aName = "a";
    a = createProject(aName,
                     ("M1": "int f(int n) = n;"),
                     createPathConfig(aName)
         );
    assert checkExpectNoErrors("M1", rascalCompilerConfig(a.pcfg));

    bName = "b";
    b = createProject(bName,
                     ("M2": "import M1;
                      'int main() = f(42),
                    "),
                     createPathConfig(bName)
                        [libs = [|memory://a/resources|] ]
         );
    assert checkExpectNoErrors("M2", rascalCompilerConfig(b.pcfg));
    a = changeModule("M1", "int f(int n) = n;", a);
    return checkExpectNoErrors("M2", rascalCompilerConfig(b.pcfg));
}
test bool incompatibleExtendsionOfBinaryLibrary(){
    rascalName = "rascal";
    rascal =
        createProject(rascalName,
                      ("IO" : "int f(int n) = n;"),
                      createPathConfig(rascalName)
        );
    assert checkExpectNoErrors("IO", rascalCompilerConfig(rascal.pcfg));

    typepalName = "typepal";
    typepal =
        createProject(typepalName,
                      ("TP": "extend IO;"),
                      createPathConfig(typepalName)
                        [libs = [|memory://<rascalName>/resources|]] // binary dependency on rascal
        );
    assert checkExpectNoErrors("TP", rascalCompilerConfig(typepal.pcfg));

    coreName = "core";
    core = 
        createProject(coreName,
                    ("Check": "import TP;
                              'import IO;
                              'value main() = f(3);
                              "),
                    createPathConfig(coreName)
                        [libs = [ |memory://<rascalName>/resources|,
                                  |memory://<typepalName>/resources|] ] // binary dependency on rascal and typepal
        );
    assert checkExpectNoErrors("Check", rascalCompilerConfig(core.pcfg));

    // Incompatible change to IO causes clash between binary use and source use of IO
    rascal = changeModule("IO", "int f(int n, int m) = n+m;", rascal);
    assert checkExpectNoErrors("IO", rascalCompilerConfig(rascal.pcfg));
    // core.pcfg[libs = [  |memory://<typepalName>/resources|] ] ;

    return checkExpectNoErrors("Check", rascalCompilerConfig(core.pcfg));
}
