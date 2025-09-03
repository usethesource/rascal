@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module GenerateTestSources

import IO;
import String;
import Set;
import List;
import Map;
import util::Reflective;
import lang::rascalcore::compile::Compile;
import util::FileSystem;
import util::Monitor;
import util::Benchmark;
import lang::rascalcore::compile::util::Names;
import util::SystemAPI;

// if cmdLineArgs contains "all", then all files in the rascal project are used (~400 files)
// otherwise only standard library and test files (~200 files) 
void main(list[str] cmdLineArgs) = generateTestSources(cmdLineArgs);

void main() = main([]);

loc REPO = |file:///Users/paulklint/git/|;

list[str] getRascalModules(loc rootFolder, PathConfig pcfg)
  = [ getModuleName(file, pcfg) //replaceAll(file[extension=""].path[1..], "/", "::") 
    | loc file <- find(rootFolder, "rsc") 
    ];        

void generateTestSources(list[str] cmdLineArgs) {
   if ("rascal.generateSources.skip" in getSystemProperties()) {
     println("Skipping the generation of test sources.");
     return;
   }
   
   pcfg = getAllSrcREPOPathConfig(keep=true);
   genCompilerConfig = getAllSrcREPOCompilerConfig(pcfg, keep=true);
   
   map[str,int] durations = ();

   list[str] modulesToCompile = [];
  
   if("all" in cmdLineArgs){
      modulesToCompile = getRascalModules(REPO + "rascal/src/org/rascalmpl/library", pcfg);     
   } else {              
       testFolders = [   REPO + "rascal/src/org/rascalmpl/library/lang/rascal/tests"
                       , REPO + "rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests"
                       , REPO + "rascal/src/org/rascalmpl/library/lang/rascalcore/agrammar/tests"
                      // , REPO + "rascal/src/org/rascalmpl/compiler/lang/rascalcore/check/tests"
                     ];
       
       modulesToCompile = [ *getRascalModules(testFolder, pcfg)
                          | testFolder <- testFolders
                          ];
   }  

   ignored = ["lang::rascal::tests::concrete::Patterns3",
               "lang::rascal::syntax::tests::ExpressionGrammars",
               "lang::sdf2::util::SDF2Grammar",
               "lang::sdf2::util::Importer"
              ];           
   modulesToCompile -= ignored;    
   
   list[str] exceptions = [];
   int n = size(modulesToCompile);
   for (i <- index(modulesToCompile)) {
      m = modulesToCompile[i];
      println("Compiling module <m> [<i>/<n>]");
      e = safeCompile(m, genCompilerConfig, void (int d) { durations[m] = d; });
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Compiled <n> modules");
   println("<size(exceptions)> failed to compile:"); iprintln(exceptions);
   if(!isEmpty(ignored)) { println("Ignored: <ignored>"); }
   secs = isEmpty(durations) ? 0 : sum(range(durations))/1000000000;
   println("Time: <secs/60> minutes");
   //iprintln(sort({ <m, durations[m] / 1000000000> | m <- durations}, bool (<_,int i>, <_, int j>) { return i < j; }));
}

str safeCompile(str \module, RascalCompilerConfig compilerConfig, void (int duration) measure) {
  result = "";
   try {
     measure(cpuTimeOf(() {    
       msgs = compile(\module, compilerConfig);
       if(!isEmpty(msgs)){
            iprintln(msgs);
       }
       errors = [ msg | msg <- msgs, msg is error ];
       result = isEmpty(errors) ? "" : "<errors>";
     }));
     return result;
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return \module; 
   }
}
