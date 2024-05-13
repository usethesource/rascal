@synopsis{Tests if we can typecheck Rascal modules that import and use other modules that
are only available as .tpl files on the libs path.}
module lang::rascalcore::check::tests::BinaryDependencyTest

import lang::rascalcore::check::Checker;
import util::Reflective;
import IO;
import lang::rascalcore::check::Import;

data PathConfig(loc resources=|unknown:///|, loc generatedSources=|unknown:///|);

test bool importSimpleBinaryModule() {
    lang::rascalcore::check::Import::traceTPL = true;

    // First we compile a library
    writeFile(|memory://myTestLibrary/src/A.rsc|,
        "module A
        '
        'int aFunction() = 1;
        ");
    
    

    pcfg = pathConfig(
        srcs=[|memory://myTestLibrary/src|],
        bin=|memory://myTestLibrary/bin|,
        generatedSources=|memory://myTestLibrary/generated|,
        resources=|memory://myTestLibrary/resources|,
        libs=[]
    );
      
    // this transitively compiles A and stores in pcfg.bin/rascal/$A.tpl
    msgs = check([|memory://myTestLibrary/src/A.rsc|], rascalCompilerConfig(pcfg));

    // no issues expected
    assert all(program(_,{}) <- msgs);

    // just to make sure no source code leaks into the following, 
    // we remove the entire source file from existence
    remove(|memory://myTestLibrary/src|, recursive=true);

    // Now we compile a client
    writeFile(|memory://myTestLibraryClient/src/B.rsc|,
        "module B
        '
        'import A; // library import
        'int bFunction() = aFunction(); // library usage
        ");
    
    pcfg = pathConfig(
        srcs=[|memory://myTestLibraryClient/src|],
        bin=|memory://myTestLibraryClient/bin|,
        generatedSources=|memory://myTestLibraryClient/generated|,
        resources=|memory://myTestLibraryClient/resources|,
        libs=[|memory://myTestLibrary/resources|] // library dependency on where the .tpl files are
    );
    
    msgs = check([|memory://myTestLibraryClient/src/B.rsc|], rascalCompilerConfig(pcfg));

    // again no issues expected
    assert all(program(_,{}) <- msgs) : "<msgs>";

    return true;
}
