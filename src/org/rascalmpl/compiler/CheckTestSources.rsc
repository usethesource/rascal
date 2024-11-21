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


void main() = checkTestSources([]);

// if cmdLineArgs contains "all", then all files in the rascal project are used (~400 files)
// otherwise only standard library and test files (~200 files) 
void main(list[str] cmdLineArgs) = checkTestSources(cmdLineArgs);

void checkTestSources(list[str] cmdLineArgs) {
   testConfig = getRascalPathConfig();
     
   println("PathConfig for type checking test sources:\n");
   iprintln(testConfig);
   
   testCompilerConfig = rascalCompilerConfig(testConfig)[logPathConfig=false];
   total = 0;

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
                        
                         "analysis::m3::AST", 
                         "analysis::m3::Core", 
                         "analysis::m3::FlowGraph", 
                         "analysis::m3::Registry",
                         "analysis::m3::TypeSymbol"];  
    
       for (m <- libraryModules) {
         <e,d> = safeCheck(m, testCompilerConfig);
         total += d;
       }
         
       testFolder = |std:///|;
       //testFolder = |std:///lang/rascal/tests|;
       
       testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                     | loc file <- find(testFolder, "rsc")     // all Rascal source files
                     ];
   }
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3" // takes too long
             ];           
   testModules -= ignored; 
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Checking test module <m> [<i>/<n>]");
      <e, d> = safeCheck(m, testCompilerConfig);
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
