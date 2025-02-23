@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
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
module CheckTestSources

import IO;
import String;
import Set;
import List;
import util::Reflective;
import lang::rascalcore::check::Checker;
import util::FileSystem;
import util::Benchmark;
import lang::rascalcore::compile::util::Names;
import lang::rascalcore::check::RascalConfig;


void main() = checkTestSources([]);

// if cmdLineArgs contains "all", then all files in the rascal project are used (~400 files)
// otherwise only standard library and test files (~200 files) 
void main(list[str] cmdLineArgs) = checkTestSources(cmdLineArgs);

loc REPO = |file:///Users/paulklint/git/|;

list[str] getRascalModules(loc rootFolder, PathConfig pcfg)
  = [ getModuleName(file, pcfg) 
    | loc file <- find(rootFolder, "rsc") 
    ];  

void checkTestSources(list[str] cmdLineArgs) {
   testConfig = getRascalPathConfig();
     
   println("PathConfig for type checking test sources:\n");
   iprintln(testConfig);
   
   genCompilerConfig = getAllSrcCompilerConfig()[logPathConfig=false];
   total = 0;
   
   list[str] modulesToCheck = [];
   
   if("all" in cmdLineArgs){
      modulesToCheck = getRascalModules(|std:///|, genCompilerConfig.typepalPathConfig);               
   } else {         
      testFolders = [ //|std:///lang/rascal/tests|,
                       //REPO + "/rascal-core/lang/rascalcore/check::tests",
                       REPO + "/typepal/src/"
                    ];
      modulesToCheck = [ *getRascalModules(testFolder, genCompilerConfig.typepalPathConfig)
                       | testFolder <- testFolders
                      ];
    }
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3" // takes too long
             ];           
   modulesToCheck -= ignored; 
   
   list[str] exceptions = [];
   int n = size(modulesToCheck);
   for (i <- index(modulesToCheck)) {
      m = modulesToCheck[i];
      println("Checking test module <m> [<i>/<n>]");
      <e, d> = safeCheck(m, genCompilerConfig);
      total += d;
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Checked <n> test modules");
   println("<size(exceptions)> failed to check: <exceptions>");
   if(!isEmpty(ignored)) { println("Ignored: <ignored>"); }
   secs = total/1000000000;
   println("Time: <secs> seconds");
}

tuple[str, int]  safeCheck(str \module, RascalCompilerConfig compilerConfig) {
    start_time = cpuTime();
    
    try {
       ModuleStatus result = rascalTModelForNames([\module], 
                                                  compilerConfig,
                                                  dummy_compile1);
       //iprintln(result.tmodels[\module].facts);
       <found, tm, result> = getTModelForModule(\module, result);
       if(found && !isEmpty(tm.messages)){
        if(/error(_,_) := tm.messages){
          println("*** ERRORS ***");
        }
            iprintln(tm.messages);
       }
       return <"", cpuTime()-start_time>;
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return <\module, 0>; 
   }
}
