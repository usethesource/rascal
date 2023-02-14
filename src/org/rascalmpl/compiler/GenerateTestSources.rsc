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

PathConfig manualTestConfig= pathConfig(bin=|project://rascal-core/target/test-classes|);

void main(list[str] args) = generateTestSources(manualTestConfig);

void main() = main([]);

void generateTestSources(PathConfig pcfg) {
   testConfig = pathConfig(
     bin=pcfg.bin,
     generatedSources=|project://rascal-core/target/generated-test-sources|,
     resources = |project://rascal-core/target/generated-test-resources|,
     srcs=[ |project://rascal/src/org/rascalmpl/library|, |std:///| ],
     libs = [ ]
     );
   map[str,int] durations = ();
     
   println("PathConfig for generating test sources:\n");
   iprintln(testConfig);

   println(readFile(|lib://rascal/META-INF/MANIFEST.MF|));

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
                     
                     "demo::lang::Pico::Syntax",
                    
                     "analysis::m3::AST", 
                     "analysis::m3::Core", 
                     "analysis::m3::FlowGraph", 
                     "analysis::m3::Registry",
                     "analysis::m3::TypeSymbol"];  

   for (m <- libraryModules) {
     safeCompile(m, testConfig, (int d) { durations[m] = d; });
   }
     
   testFolder = |std:///lang/rascal/tests|;
   
   testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                 | loc file <- find(testFolder, "rsc")     // all Rascal source files
                 ];  
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3"
             ];           
   testModules -= ignored;    
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Compiling test module <m> [<i>/<n>]");
      e = safeCompile(m, testConfig, (int d) { durations[m] = d; });
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Compiled <n> test modules");
   println("<size(exceptions)> failed to compile: <exceptions>");
   if(!isEmpty(ignored)) { println("Ignored: <ignored>"); }
   secs = sum(range(durations))/1000000000;
   println("Time: <secs/60> minutes");
   //iprintln(sort({ <m, durations[m] / 1000000000> | m <- durations}, bool (<_,int i>, <_, int j>) { return i < j; }));
}

void testCompile(str \module) {
  int duration = 0;
  safeCompile(\module, manualTestConfig, (int d) { duration = d; return; });
  println("compile of <\module> lasted <duration / (1000*1000*1000.0)> seconds");
}

str safeCompile(str \module, PathConfig pcfg, void (int duration) measure) {
   try {
     measure(cpuTimeOf(() {    
       compile(\module, pcfg);
     }));
     return "";
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return \module; 
   }
}
