module experiments::Compiler::Tests::AllCompilerTests

/*
 * A suite of tests for the Rascal compiler:
 * - import this module in a RascalShell
 * - Type :test at the command line.
 * - Go and drink some latte ;-)
 */
 
import experiments::Compiler::Compile;
import experiments::Compiler::Execute;

//import util::ShellExec;
import IO;

import util::Reflective;

// Note: Type commented out since it uses a definition of datatype D that is incompatible with TestUtils
// extend experiments::Compiler::Tests::Types;
extend experiments::Compiler::Tests::Booleans;
extend experiments::Compiler::Tests::Expressions;
extend experiments::Compiler::Tests::Statements;
extend experiments::Compiler::Tests::Patterns;
extend experiments::Compiler::Tests::StringTemplates;

extend experiments::Compiler::Examples::Run;

list[str] compilerTests = [
"Booleans",
"Expressions",
"Patterns",
"Statements",
"StringTemplates"
];

list[str] examplesTests = [
"Run"
];


lrel[str,str] crashes = [];
lrel[str,str] partial_results = [];

lrel[loc,int,str] runTests(list[str] names, str base){
 pcfg = pathConfig(srcPath=[|test-modules:///|, |std:///|], binDir=|home:///c1bin|, libPath=[|home:///c1bin|]);
 all_test_results = [];
 for(str tst <- names){
      //prog = base + (tst + ".rsc");
      prog = base + "::" + tst;
      for(str ext <- [/*"sig", "sigs", "tc"*/ "rvm.gz", "rvm.ser.gz"]){
       if(<true, l> := getDerivedReadLoc(prog, ext, pcfg)){
          remove(l);
       }
      }
      try {
	      if(lrel[loc src,int n,str msgs] test_results := execute(prog, pcfg, recompile=true, testsuite=true, debug=false)){
	         s = makeTestSummary(test_results);
	         println("TESTING <prog>: <s>");
	         partial_results += <prog, s>;
	         all_test_results += test_results;
	         
	          for(msg <- test_results.msgs){
                if(msg != "" && msg != "FALSE" && findFirst(msg, "test fails for arguments:") < 0){
                    crashes += <prog, msg>;
                } 
              }
	      } else {
	         println("testsuite did not return a list of test results");
	      }
      } catch e: {
        crashes += <prog, "<e>">;
      }
  }
 
  return all_test_results;
}
  
value main(){
  timestamp = now();
  crashes = [];
  partial_results = [];
  all_results = [];
   
  all_results += runTests(compilerTests, "experiments::Compiler::Tests");
  all_results += runTests(examplesTests, "experiments::Compiler::Examples");
 
   
  println("TESTS RUN AT <timestamp>");
  println("\nRESULTS PER FILE:");
  for(<prog, s> <- partial_results)
      println("<prog>: <s>");
  
  println("\nFailed/IGNORED TESTS:");
  printTestReport(all_results, []);
  
  if(size(crashes) > 0){
     println("\nCRASHED TESTS:");
     for(<prog, e> <- crashes)
         println("<prog>: <e>");
  }
  
  return size(all_results[_,0]) == 0;
}

