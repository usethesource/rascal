module experiments::Compiler::Examples::Tst3

import IO;
import Type;
import List;
import DateTime;
import experiments::Compiler::Execute;
import String;
import ParseTree;

import util::Reflective;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods


private list[str] basicTests = [
    //"Booleans",
    //"Equality",
    //"Functions",
    //"Integers",
    //"IO",
//  "IsDefined",         // TEMP
    //"ListRelations",
    //"Lists",
    //"Locations",
//    "Maps",
    "Matching"
    //"Memoization",
    //"Nodes",
    //"Overloading",
    //"Relations" ,
    //"Sets",
    //"SolvedIssues",
    //"Strings",
    //"Tuples"                    
];


private lrel[str,str] crashes = [];
private lrel[str,str] partial_results = [];

lrel[loc,int,str] runTests(list[str] names, str base, PathConfig pcfg, bool jvm=false){
 all_test_results = [];
 for(tst <- names){
      prog = base == "" ? tst : (base + "::" + tst);
      //for(str ext <- ["sig", "sigs", "tc", "rvm.gz", "rvm.ser.gz"]){
      // if(<true, l> := getDerivedReadLoc(prog, ext, pcfg)){
      //    remove(l);
      // }
      //}
      try {
          if(lrel[loc src,int n,str msgs] test_results := execute(prog, pcfg, recompile=true, testsuite=true, jvm=jvm)){
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
  
value main(bool jvm=false) = allRascalTests(binDir=|home:///bin-tests-comp|, jvm=jvm);
  
value allRascalTests(loc binDir=|home:///bin-tests-intp|, bool jvm=false){
  
  println("Using binDir = <binDir>");
  timestamp = now();
  crashes = [];
  partial_results = [];
  lrel[loc,int,str] all_results = [];
  
  pcfg = pathConfig(binDir=binDir, libPath=[binDir]);
  
  all_results += runTests(basicTests, "lang::rascal::tests::basic", pcfg, jvm=jvm);
   
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
