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
module CompileTestSources

// Compile:
// - selected standard libraries
// - all tests in the lang/rascal/tests folder
// - all type checker tests in "lang::rascalcore::check::tests (for now disabled)

import IO;
import String;
import Set;
import List;
import util::Reflective;
import lang::rascalcore::compile::Compile;
import util::FileSystem;
import util::Benchmark;
import lang::rascalcore::compile::util::Names;

loc REPO = |file:///Users/paulklint/git/|;

PathConfig manualTestConfig= pathConfig(bin= REPO + "generated-sources/target/classes",
                                        generatedSources = REPO + "generated-sources/target/generated-sources",
                                        resources = REPO + "generated-sources/target/classes" //|project://rascal-core/target/generated-test-resources|
                                       );
void main() = compileTestSources(manualTestConfig);

void compileTestSources(PathConfig pcfg) {
   testCompilerConfig = getAllSrcCompilerConfig()[logPathConfig=false];
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
                     
  //   list[str] checkerTestModules = [
  //       "lang::rascalcore::check::tests::AccumulatingTCTests",
  //       "lang::rascalcore::check::tests::AliasTests",
  //       "lang::rascalcore::check::tests::AllStaticIussues",
  //       "lang::rascalcore::check::tests::AllStaticIssuesUsingStdLib", 
  //       "lang::rascalcore::check::tests::AnnotationTCTests",
  //       "lang::rascalcore::check::tests::AnnotationUsingStdLibTCTests",
  //       "lang::rascalcore::check::tests::AssignmentTCTests",
  //       "lang::rascalcore::check::tests::ATypeInstantiationTests",
  //       "lang::rascalcore::check::tests::ATypeTests",
  //       "lang::rascalcore::check::tests::BooleanTCTests",
  //       "lang::rascalcore::check::tests::BooleanUsingStdLibTCTests",
  //       "lang::rascalcore::check::tests::CallTCTests",
  //       "lang::rascalcore::check::tests::CallUsingStdLibTCTests",
  //       "lang::rascalcore::check::tests::ComprehensionTCTests",
  //       "lang::rascalcore::check::tests::DataDeclarationTCTests",
  //       "lang::rascalcore::check::tests::DataTypeTCTests",
  //       "lang::rascalcore::check::tests::DeclarationTCTests",
  //       "lang::rascalcore::check::tests::ImportTCTests",
  //       "lang::rascalcore::check::tests::InferenceTCTests",
  //       "lang::rascalcore::check::tests::ParameterizedTCTests",
  //       "lang::rascalcore::check::tests::PatternTCTests",
  //       "lang::rascalcore::check::tests::PatternUsingStdLibTCTests",
  //       "lang::rascalcore::check::tests::ProjectTCTests",
  //       "lang::rascalcore::check::tests::RegExpTCTests",
  //       "lang::rascalcore::check::tests::ScopeTCTests",
  //       "lang::rascalcore::check::tests::StatementTCTests",
  //       "lang::rascalcore::check::tests::StaticTestsingUtilsTests",
  //       "lang::rascalcore::check::tests::StaticTestingUsingStdLibTests",
  //       "lang::rascalcore::check::tests::SubscriptTCTests",
  //       "lang::rascalcore::check::tests::VisitTCTests"
  //  ];
   

  //  for (m <- libraryModules) {
  //    <e, d> = safeCompile(m, testCompilerConfig);
  //    total += d;
  //  }
   
   //for (m <- checkerTestModules) {
   //  <e, d> = safeCompile(m, testConfig, testCompilerConfig);
   //  total += d;
   //}
     
   testFolder = |std:///lang/rascal/|;
   
   testModules = [ replaceAll(file[extension=""].path[1..], "/", "::") 
                 | loc file <- find(testFolder, "rsc")     // all Rascal source files
                 ];  
                 
   ignored = ["lang::rascal::tests::concrete::Patterns3" // takes too long
             ];           
   testModules -= ignored;
   println("Compiling test modules:");
   println(testModules);   
   
   list[str] exceptions = [];
   int n = size(testModules);
   for (i <- index(testModules)) {
      m = testModules[i];
      println("Compiling test module <m> [<i>/<n>]");
      <e, d> = safeCompile(m, testCompilerConfig);
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
}

tuple[str, int] safeCompile(str \module, RascalCompilerConfig compilerConfig) {
   start_time = cpuTime();
   try { 
       msgs = compile(\module, compilerConfig);
       if(!isEmpty(msgs)){
            iprintln(msgs);
       }
       return <"",cpuTime()-start_time>;
   }
   catch value exception: {
     println("Something unexpected went wrong during test source generation for <\module>:
             '    <exception>"); 
     return <\module, 0>;
   }
}
