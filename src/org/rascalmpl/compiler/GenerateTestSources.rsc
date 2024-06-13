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

PathConfig manualTestConfig= pathConfig(bin=|project://rascal-core/target/test-classes|);

void main(list[str] args) = generateTestSources(manualTestConfig);

void main() = main([]);

void generateTestSources(PathConfig pcfg) {
   if (getSystemProperty("-Drascal.generateSources.skip") != "") {
     println("Skipping the generation of test sources.");
     return;
   }

   testConfig = pathConfig(
     bin=pcfg.bin,
     generatedSources=|project://rascal-core/target/generated-test-sources|,
     resources = |project://rascal-core/target/generated-test-resources|,
     srcs=[ |std:///|, |project://rascal-core/src/org/rascalmpl/core/library| ],
     libs = [ ]
     );
     
   testCompilerConfig = getRascalCoreCompilerConfig(testConfig)[logPathConfig=false][forceCompilationTopModule=false];
   
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
                     
                     //"demo::lang::Pico::Syntax",
                    
                     "analysis::m3::AST", 
                     "analysis::m3::Core", 
                     "analysis::m3::FlowGraph", 
                     "analysis::m3::Registry",
                     "analysis::m3::TypeSymbol"];  
                     
   list[str] checkerTestModules = [
        "lang::rascalcore::check::tests::AccumulatingTCTests",
        "lang::rascalcore::check::tests::AliasTests",
        "lang::rascalcore::check::tests::AllStaticIussues",
        "lang::rascalcore::check::tests::AllStaticIssuesUsingStdLib", 
        "lang::rascalcore::check::tests::AnnotationTCTests",
        "lang::rascalcore::check::tests::AnnotationUsingStdLibTCTests",
        "lang::rascalcore::check::tests::AssignmentTCTests",
        "lang::rascalcore::check::tests::ATypeInstantiationTests",
        "lang::rascalcore::check::tests::ATypeTests",
        "lang::rascalcore::check::tests::BooleanTCTests",
        "lang::rascalcore::check::tests::BooleanUsingStdLibTCTests",
        "lang::rascalcore::check::tests::CallTCTests",
        "lang::rascalcore::check::tests::CallUsingStdLibTCTests",
        "lang::rascalcore::check::tests::ComprehensionTCTests",
        "lang::rascalcore::check::tests::DataDeclarationTCTests",
        "lang::rascalcore::check::tests::DataTypeTCTests",
        "lang::rascalcore::check::tests::DeclarationTCTests",
        "lang::rascalcore::check::tests::ImportTCTests",
        "lang::rascalcore::check::tests::InferenceTCTests",
        "lang::rascalcore::check::tests::ParameterizedTCTests",
        "lang::rascalcore::check::tests::PatternTCTests",
        "lang::rascalcore::check::tests::PatternUsingStdLibTCTests",
        "lang::rascalcore::check::tests::ProjectTCTests",
        "lang::rascalcore::check::tests::RegExpTCTests",
        "lang::rascalcore::check::tests::ScopeTCTests",
        "lang::rascalcore::check::tests::StatementTCTests",
        "lang::rascalcore::check::tests::StaticTestsingUtilsTests",
        "lang::rascalcore::check::tests::StaticTestingUsingStdLibTests",
        "lang::rascalcore::check::tests::SubscriptTCTests",
        "lang::rascalcore::check::tests::VisitTCTests"
   ];
   

   for (m <- libraryModules) {
     safeCompile(m, testCompilerConfig, (int d) { durations[m] = d; });
   }
   
   //for (m <- checkerTestModules) {
   //  safeCompile(m, testConfig, testCompilerConfig, (int d) { durations[m] = d; });
   //}
     
   testFolders = [|std:///lang/rascal/tests| ];
   
   testModules = [ *[ replaceAll(file[extension=""].path[1..], "/", "::") | loc file <- find(testFolder, "rsc") ]
                 | testFolder <- testFolders
                 ];  

   ignored = ["lang::rascal::tests::concrete::Patterns3"
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
