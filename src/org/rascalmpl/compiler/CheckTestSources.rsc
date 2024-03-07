module CheckTestSources

import IO;
import String;
import Set;
import List;
import Map;
import util::Reflective;
import lang::rascalcore::check::Checker;
import util::FileSystem;
import util::Monitor;
import util::Benchmark;
import lang::rascalcore::compile::util::Names;

PathConfig manualTestConfig= pathConfig(bin=|project://rascal-core/target/test-classes2|,
                                        generatedSources = |project://rascal-core/target/generated-test-sources2|,
                                        resources = |project://rascal-core/target/generated-test-resources2|
                                       );

void main() = checkTestSources(manualTestConfig);

void checkTestSources(PathConfig pcfg) {
     testConfig = pathConfig(
     bin=pcfg.bin,
     generatedSources=|project://rascal-core/target/generated-test-sources2|,
     resources = |project://rascal-core/target/generated-test-resources2|,
     srcs=[ |project://rascal/src/org/rascalmpl/library|, |std:///|, |project://rascal-core/src/org/rascalmpl/core/library| ],
     libs = [ ]
     );
     
   println("PathConfig for type checking test sources:\n");
   iprintln(testConfig);
   
   testCompilerConfig = getRascalCompilerConfig();
   total = 0;

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
                    
                     "analysis::m3::AST", 
                     "analysis::m3::Core", 
                     "analysis::m3::FlowGraph", 
                     "analysis::m3::Registry",
                     "analysis::m3::TypeSymbol"];  

   for (m <- libraryModules) {
     <e,d> = safeCompile(m, testConfig, testCompilerConfig);
     total += d;
   }
     
   testFolder = |std:///lang/rascal/tests|;
   
   testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                 | loc file <- find(testFolder, "rsc")     // all Rascal source files
                 ];
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3" // takes too long
             ];           
   testModules -= ignored; 
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Checking test module <m> [<i>/<n>]");
      <e, d> = safeCompile(m, testConfig, testCompilerConfig);
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

tuple[str, int]  safeCompile(str \module, PathConfig pcfg, CompilerConfig compilerConfig) {
    start_time = cpuTime();
    
    try {
       println("checking <\module>");
       ModuleStatus result = rascalTModelForNames([\module], 
                                                  rascalTypePalConfig(rascalPathConfig=pcfg), 
                                                  compilerConfig,
                                                  dummy_compile1);
       iprintln(result.tmodels[\module].messages);
     return <"", cpuTime()-start_time>;
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return <\module, 0>; 
   }
}
