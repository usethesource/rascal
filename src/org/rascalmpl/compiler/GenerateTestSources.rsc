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

void generateTestSources(list[str] cmdLineArgs) {
   if ("rascal.generateSources.skip" in getSystemProperties()) {
     println("Skipping the generation of test sources.");
     return;
   }

   testConfig = pathConfig(
     bin=|project://generated-sources/classes/|,
     generatedSources=|project://generated-sources/target/generated-sources/src/main/java/|,
     generatedTestSources = |project://generated-sources/target/generated-sources/src/main/java/|,
     //generatedSources=|project://rascal-core/target/generated-test-sources|,
     resources = |project://generated-sources/target/generated-resources/src/main/java/|, //|project://rascal-core/target/generated-test-resources|,
     srcs=[ |project://rascal/src/org/rascalmpl/library|,
            |project://rascal-core/src/org/rascalmpl/core/library| ],
     libs = [ ]
     );
     
   testCompilerConfig = getRascalCoreCompilerConfig(testConfig)[logPathConfig=false];
   
   map[str,int] durations = ();
     
   println("PathConfig for generating test sources:\n");
   iprintln(testConfig);

   println(readFile(|lib://rascal/META-INF/MANIFEST.MF|));

   testModules = [];
  
   if("all" in cmdLineArgs){
        rootFolder = |std:///|;
   
        testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                      | loc file <- find(rootFolder, "rsc") 
                      ];           
   } else {         
   
       libraryModules = ["Boolean", 
                         "DateTime", 
                         "Exception", 
                         "Grammar", 
                         "IO", 
                         "List", 
                         "ListRelation", 
                         "Location", 
                         "Map", 
                         "Message", 
                         "Node", 
                         "ParseTree", 
                         "Prelude", 
                         "Relation", 
                         "Set", "String", 
                         /*"Traversal",*/
                         "Type", 
                         "ValueIO",
                         "analysis::graphs::Graph", 
                         "analysis::statistics::Correlation",
                         "analysis::statistics::Descriptive",
                         "analysis::statistics::Frequency",
                         "analysis::statistics::Inference",
                         "analysis::statistics::SimpleRegression",
                         "lang::csv::IO",
                         "lang::json::IO",
                         "lang::manifest::IO",
                         "lang::rascal::syntax::Rascal",
                         "lang::xml::DOM",
                         "lang::xml::IO",
                         "util::FileSystem", 
                         "util::Math",
                         "util::Maybe",
                         "util::Memo",
                         "util::PriorityQueue",
                         "util::Reflective",
                         "util::SemVer",
                         "util::UUID",
                         
                         //"demo::lang::Pico::Syntax",
                        
                         "analysis::m3::AST", 
                         "analysis::m3::Core", 
                         "analysis::m3::FlowGraph", 
                         "analysis::m3::Registry",
                         "analysis::m3::TypeSymbol"];  
                     
       for (m <- libraryModules) {
         safeCompile(m, testCompilerConfig, (int d) { durations[m] = d; });
       }
   
     
       testFolders = [|std:///lang/rascal/tests| ];
       
       testModules = [ *[ replaceAll(file[extension=""].path[1..], "/", "::") | loc file <- find(testFolder, "rsc") ]
                     | testFolder <- testFolders
                     ];
   }  

   ignored = ["lang::rascal::tests::concrete::Patterns3",
              "lang::rascal::syntax::tests::ExpressionGrammars"
             ];           
   testModules -= ignored;    
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Compiling test module <m> [<i>/<n>]");
      e = safeCompile(m, testCompilerConfig, (int d) { durations[m] = d; });
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Compiled <n> test modules");
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
