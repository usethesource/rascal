module CompileTestSources

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

PathConfig manualTestConfig= pathConfig(bin=|project://rascal-core/target/test-classes2|,
                                        generatedSources = |project://rascal-core/target/generated-test-sources2|,
                                        resources = |project://rascal-core/target/generated-test-resources2|
                                       );
//void main(list[str] args) = compileTestSources(manualTestConfig);

void main() = compileTestSources(manualTestConfig);

void compileTestSources(PathConfig pcfg) {
   testConfig = pathConfig(
     bin=pcfg.bin,
     generatedSources=|project://rascal-core/target/generated-test-sources2|,
     resources = |project://rascal-core/target/generated-test-resources2|,
     srcs=[ |project://rascal/src/org/rascalmpl/library|, |std:///| , |project://rascal-core/src/org/rascalmpl/core/library|],
     libs = [ ]
     );
     
   testCompilerConfig = getRascalCompilerConfig();
   map[str,int] durations = ();
   total = 0;
     
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
     <e, d> = safeCompile(m, testConfig, testCompilerConfig);
     total += d;
   }
   
   //for (m <- checkerTestModules) {
   //  <e, d> = safeCompile(m, testConfig, testCompilerConfig);
   //  total += d;
   //}
     
   testFolder = |std:///lang/rascal/tests|;
   
   testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                 | loc file <- find(testFolder, "rsc")     // all Rascal source files
                 ];  
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3"
              //"lang::rascal::tests::extend_function1::M3"
              //"lang::rascal::tests::concrete::Matching // class cast empty set
              //"lang::rascal::tests::concrete::Parsing" // class cast empty set
              //"lang::rascal::tests::concrete::Syntax1",  //class cast empty set
              //"lang::rascal::tests::concrete::Syntax4" // parser generator
              //"lang::rascal::tests::concrete::Syntax5" // parser generator
              //"lang::rascal::tests::concrete::FieldProjectionBug"// parser generator
              //"lang::rascal::tests::concrete::Patterns1" // class cast empty set
              //"lang::rascal::tests::functionality::Range",
              //"lang::rascal::tests::concrete::ParameterizedNonTerminals",
              //"lang::rascal::tests::functionality::Interpolation", // check on muCon arg
              //"lang::rascal::tests::library::String"
             ];           
   testModules -= ignored;    
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Compiling test module <m> [<i>/<n>]");
      <e, d> = safeCompile(m, testConfig, testCompilerConfig);
      total += d;
      if(!isEmpty(e)){
        exceptions += e;
      }
   }
   println("Compiled <n> test modules");
   println("<size(exceptions)> failed to compile: <exceptions>");
   if(!isEmpty(ignored)) { println("Ignored: <ignored>"); }
   secs = total/1000000000;
   println("Time: <secs> seconds");
   //iprintln(sort({ <m, durations[m] / 1000000000> | m <- durations}, bool (<_,int i>, <_, int j>) { return i < j; }));
}

tuple[str, int] safeCompile(str \module, PathConfig pcfg, CompilerConfig compilerConfig) {
   start_time = cpuTime();
   try {
       println("compiling <\module>");   
       compile(\module, pcfg, compilerConfig);
       return <"",cpuTime()-start_time>;
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return <\module, 0>;
   }
}
