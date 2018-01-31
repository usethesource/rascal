module lang::rascalcore::compile::Tests::BinaryDependencies

import lang::rascalcore::compile::Compile;
import lang::rascalcore::compile::Execute;
import util::SystemAPI;
import IO;
import util::Reflective;
import util::FileSystem;
import lang::rascalcore::compile::RVM::AST;

private void clean(loc t) {
  for (/file(loc f) := crawl(t))
    remove(f);
}
 
@doc{check if dependency on a binary module for which no source module is available works}
test bool simpleBinaryDependency() {
   top = |test-modules:///simpleBinaryDependency|;
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
   
   // then we compile A which uses B, but only on the library path available as binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinB", top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
      
   // see if it works
   return execute("A", pcfgA, recompile=false) == 42; 
}

test bool simpleBinaryDependencyNoJunk() {
   top = |test-modules:///simpleBinaryDependency|;
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
   
   // then we compile A which uses B, but only on the library path available as binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinB", top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
      
   // see if it works
   return !any(loc f <- (top + "BinA").ls, /B\..*/ := f.file, bprintln("module output of binary dependency written to current bin: <f>"));
}

test bool simpleBinaryDependencyWithSourceDeploymentNoJunk() {
   top = |test-modules:///simpleBinaryDependencyWithSourceDeploymentNoJunk|;
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
   
   // "distribute" source code
   copyFile(top  + "b/B.rsc", (top + "BinB") + "B.rsc");
   
   // then we compile A which uses B, but only on the library path available as binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinB", top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
      
   // "distribute" source code   
   copyFile(top  + "a/A.rsc", (top + "BinA") + "A.rsc");
   
      
   // see if it works
   return execute("A", pcfgA, recompile=false) == 42
       && !any(loc f <- (top + "BinA").ls, /B\..*/ := f.file, bprintln("module output of binary dependency written to current bin: <f>"));
}

@doc{check if dependency on a binary module for which no source module is available works}
test bool simpleBinaryDependencyReadonly() {
   top = |test-modules:///simpleBinaryDependencyReadonly|;
   readonlyTop = top[scheme=top.scheme + "+readonly"];
   
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
   
   // remove the source file entirely, just to be sure of non-interference
   remove(top + "b/B.rsc");
   
   // then we compile A which uses B, but only on the library path available as (readonly) binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[readonlyTop + "BinB", top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // see if it works
   return execute("A", pcfgA, recompile=false) == 42; 
}


@doc{check if dependency on a binary module for which no source module is available works}
test bool simpleBinaryDependencyIncremental() {
   top = |test-modules:///simpleBinaryDependencyIncremental|;
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     'int main1() = 1; // dummy last declaration that is always removed by compile1Incremental
     ");
      
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     'int main2() = 2;  // dummy last declaration that is always removed by compile1Incremental
     ");
     
   // first we compile (standard, non-incremental) module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   rvmProgramB = compileAndLink("B", pcfgB, jvm=true, verbose=false);
   
   // then we compile A which uses B, but only on the library path available as binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinB", top + "BinA"]);
   rvmProgramA = compileAndMergeProgramIncremental("A", true, pcfgA, jvm=true, verbose=false); 
   
   // see if it works by executing (without recompiling!)
   return execute(rvmProgramA, pcfgA, verbose=false) == 42; 
}

@doc{check if dependency on a binary module for which no source module is available works}
test bool simpleBinaryDependencyIncrementalReadonly() {
   top = |test-modules:///simpleBinaryDependencyIncremental|;
   readonlyTop = top[scheme=top.scheme + "+readonly"];
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     'int main1() = 1; // dummy last declaration that is always removed by compile1Incremental
     ");
      
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     'int main2() = 2;  // dummy last declaration that is always removed by compile1Incremental
     ");
     
   // first we compile (standard, non-incremental) module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   rvmProgramB = compileAndLink("B", pcfgB, jvm=true, verbose=false);
   
   // remove the source file entirely
   remove(top + "b/B.rsc");
   
   // then we compile A which uses B, but only on the library path available as binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[readonlyTop + "BinB", top + "BinA"]);
   rvmProgramA = compileAndMergeProgramIncremental("A", true, pcfgA, jvm=true, verbose=false); 
   
   // see if it works by executing (without recompiling!)
   return execute(rvmProgramA, pcfgA, verbose=false) == 42; 
}

@doc{single module recompilation after edit should have an effect}
test bool simpleRecompile() {
   top = |test-modules:///simpleRecompile|;
   clean(top);
   
   // create a module
   writeFile(top + "a/A.rsc",
     "module A
     'int main() = 42;
     ");
     
   // compile the module  
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // run the module
   first = execute("A", pcfgA, recompile=false);
   
   // edit the module
   writeFile(top + "a/A.rsc",
     "module A
     'int main() = 43;
     ");
  
   // recompile
   compileAndLink("A", pcfgA, jvm=true); 
     
   // expect change  
   second = execute("A", pcfgA, recompile=false);
  
   return first != second && second == 43;
}

@doc{both files are in the same source directory, and the imported one receives an update before the importer is recompiled}
test bool sourceDependencyRecompile() {
   top = |test-modules:///sourceDependencyRecompile|;
   clean(top);
   
   writeFile(top + "A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   pcfgA = pathConfig(srcs=[top, |std:///|], bin=top + "Bin", libs=[top + "Bin"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   first = execute("A", pcfgA, recompile=false); 
   
   writeFile(top + "B.rsc",
     "module B
     'int testb() = 43;
     ");
     
   // notice the top module is recompiled, not the changed module  
   compileAndLink("A", pcfgA, jvm=true);
   
   second = execute("A", pcfgA, recompile=false);
   
   return first != second && second == 43; 
}

@doc{the imported module is only on the library path in binary form, and this imported library receives an update before the importer is recompiled}
test bool binaryDependencyRecompile() {
   top = |test-modules:///binaryDependencyRecompile|;
   readonlyTop = top[scheme=top.scheme + "+readonly"];
   clean(top);  
   
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
    // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
     
   // then in another bin we compile A  
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinA", readonlyTop + "BinB"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // see what comes out
   first = execute("A", pcfgA, recompile=false); 
   
   // change module B
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 43;
     ");
     
   // recompile B 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
     
   // recompile A
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinA",readonlyTop + "BinB"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // see what comes out
   second = execute("A", pcfgA, recompile=false);
   
   return first != second && second == 43; 
}

@doc{binary dependencies do not trigger a transitive recompile}
test bool binaryDependencyNoTransitiveRecompile() {
   top = |test-modules:///binaryDependencyNoTransitiveRecompile|;
   clean(top);
   
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
    // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
     
   // then in another bin we compile A  
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinA", top + "BinB"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // see what comes out
   first = execute("A", pcfgA, recompile=false); 
   
   // change module B
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 43;
     ");
     
   // note: no recompilation for B 
     
   // recompile A, even with "recompile=true"
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "BinA", top + "BinB"]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   // see what comes out
   second = execute("A", pcfgA, recompile=true);
   
   // no change expected
   return first == second; 
}

test bool binaryStandardLibraryTest() {
   top = |test-modules:///binaryStandardLibraryTest|;
   clean(top);
   
   writeFile(top + "a/A.rsc",
     "module A
     'import IO;
     'int testa() = bprintln(\"Hello binaryStandardLibraryTest\") ? 1 : 0;
     'int main() = testa();
     ");
     
   // then in another bin we compile A  
   pcfgA = pathConfig(srcs=[top + "a"], bin=top + "BinA", libs=[top + "BinA", |stdlib:///|]);
   compileAndLink("A", pcfgA, jvm=true); 
   
   result = execute("A", pcfgA, recompile=true);
   
   return result == 1;
}

test bool simpleBinaryDependencyRelocation() {
   top = |test-modules:///simpleBinaryDependencyRelocation|;
   clean(top);
   
   // write two modules in different source folders, A and B
   writeFile(top + "a/A.rsc",
     "module A
     'import B;
     'int testa() = testb();
     'int main() = testa();
     ");
     
   writeFile(top + "b/B.rsc",
     "module B
     'int testb() = 42;
     ");
     
   // first we compile module B to a B binary 
   pcfgB = pathConfig(srcs=[top + "b", |std:///|], bin=top + "BinB", libs=[top + "BinB"]);
   compileAndLink("B", pcfgB, jvm=true);
   
   // then we move the binary modules to a different location
   copyDirectory(top + "BinB", top + "RelocB");
   remove(top + "BinB");
   
   // then we compile A which uses B, but only on the library path available as relocated binary
   pcfgA = pathConfig(srcs=[top + "a", |std:///|], bin=top + "BinA", libs=[top + "RelocB", top + "BinA"]);
   compileAndLink("A", pcfgA, jvm=true); 
      
   // see if it works
   return !any(loc f <- (top + "BinA").ls, /B\..*/ := f.file, bprintln("module output of binary dependency written to current bin: <f>"));
}