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

list[str] getRascalModules(loc rootFolder)
  = [ replaceAll(file[extension=""].path[1..], "/", "::") 
    | loc file <- find(rootFolder, "rsc") 
    ];        

void generateTestSources(list[str] cmdLineArgs) {
   if ("rascal.generateSources.skip" in getSystemProperties()) {
     println("Skipping the generation of test sources.");
     return;
   }
     
   genCompilerConfig = getRascalCoreCompilerConfig()[logPathConfig=false];
   
   map[str,int] durations = ();

   println(readFile(|lib://rascal/META-INF/MANIFEST.MF|));

   modulesToCompile = [];
  
   if("all" in cmdLineArgs){
      modulesToCompile = getRascalModules(|std:///|);     
   } else {              
       testFolders = [ |std:///lang/rascal/tests|,
                       REPO + "/rascal-core/lang/rascalcore/check::tests"
                     ];
       
       modulesToCompile = [ *getRascalModules(testFolder)
                          | testFolder <- testFolders
                          ];
   }  

   ignored = ["lang::rascal::tests::concrete::Patterns3",
              "lang::rascal::syntax::tests::ExpressionGrammars"
             ];           
   modulesToCompile -= ignored;    
   
   list[str] exceptions = [];
   int n = size(modulesToCompile);
   for (i <- index(modulesToCompile)) {
      m = modulesToCompile[i];
      println("Compiling module <m> [<i>/<n>]");
      e = safeCompile(m, genCompilerConfig, (int d) { durations[m] = d; });
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Compiled <n> modules");
   println("<size(exceptions)> failed to compile: <exceptions>");
   if(!isEmpty(ignored)) { println("Ignored: <ignored>"); }
   secs = isEmpty(durations) ? 0 : sum(range(durations))/1000000000;
   println("Time: <secs/60> minutes");
   //iprintln(sort({ <m, durations[m] / 1000000000> | m <- durations}, bool (<_,int i>, <_, int j>) { return i < j; }));
}

str safeCompile(str \module, RascalCompilerConfig compilerConfig, void (int duration) measure) {
   try {
     measure(cpuTimeOf(() {    
       msgs = compile(\module, compilerConfig);
       if(!isEmpty(msgs)){
            iprintln(msgs);
       }
     }));
     return "";
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return \module; 
   }
}
