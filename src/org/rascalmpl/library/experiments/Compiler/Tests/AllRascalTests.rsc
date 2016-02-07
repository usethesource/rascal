module experiments::Compiler::Tests::AllRascalTests

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
	"Booleans",
	"Equality",
	"Functions",
	"Integers",
	"IO",
	"IsDefined",
	"ListRelations",
	"Lists",
	"Locations",
	"Maps",
	"Matching",
	"Memoization",
	"Nodes",
	"Overloading",
	"Relations"	,
	"Sets",
	"SolvedIssues",
	"Strings",
	"Tuples"					
];


private list[str] functionalityTests = [

"AccumulatingTests",		// OK
"AliasTests",				// OK
"AnnotationTests",			// OK
"AssignmentTests",			// OK
"BacktrackingTests",		// OK
"CallTests",				// OK
"CallTestsAux",
"ComprehensionTests",		// OK, 3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
"ConcretePatternTests1",	// OK
"ConcretePatternTests2",	// OK
"ConcretePatternTests3",	// OK
"ConcreteSubscriptAndSliceTests",   // OK
"ConcreteSyntaxTests1",     // OK
"ConcreteSyntaxTests2",     // OK
"ConcreteSyntaxTests3",     // OK
"ConcreteSyntaxTests4",     // OK
"ConcreteTerms",			// OK
"DataDeclarationTests",		// OK
"DataTypeTests",			// OK
"DeclarationTests",			// OK
"FunctionCompositionTests",	// OK
"InterpolationTests",
"KeywordParameterImportTests",
"KeywordParameterTests",
"ParsingTests",
"PatternTests",				// OK
"PatternDescendantTests",
"PatternList3Tests",
"ProjectionTests", 			// OK
"RangeTests",				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
"ReducerTests",				// OK
"RegExpTests",				// OK
//"ScopeTests",				// OK OutOfMemory????
"SetMatchTests1",           // OK
"SetMatchTests2",           // OK
"StatementTests",			// OK
"SubscriptTests",			// OK
"TryCatchTests",			// OK    				
"VisitTests"				// OK
];


private list[str] libraryTests = [

// OK


"BooleanTests",			    // OK
"IntegerTests",			    // OK
"ListRelationTests",
"ListTests" ,			    // OK
"MapTests",				    // OK
"MathTests"	,			    // OK
"NodeTests",                // OK
"NumberTests",			    // OK
"RelationTests",		    // OK
"SetTests",				    // OK
"StringTests",			    // OK
"TypeTests",
"ValueIOTests",
"analysis::graphs::GraphTests",
"analysis::statistics::DescriptiveTests",
"analysis::statistics::RangeUtils",
"lang::csv::CSVIOTests",      // OK
"lang::json::JSONIOTests"    // OK
];

private list[str] importTests = [
"ImportTests1",             // OK
"ImportTests2",             // OK
"ImportTests3",             // OK
"ImportTests4",             // OK
"ImportTests5",             // OK
"ImportTests6",             // OK
"ImportTests7",              // OK
"ImportTests8",              // OK
"ModuleInitRange"
];

private list[str] extendTests  = [
"ABSTRACTTYPE",
"A1",
"A2",
"B1",
"B2",
"B3",
"PARSETREE",
"TYPE",
"UseImportBase",
"UseImportBaseExtended",
"UseExtendBase",
"UseExtendBaseExtended"
];

private list[str] typeTests = [
"StaticTestingUtilsTests",	// OK
"AccumulatingTCTests",		// OK
//"AliasTCTests",			// C & I: Overflow/LOOP?
"AllStaticIssues",			// C == I : 1 fail : Issue504
"AnnotationTCTests",		// OK
"AssignmentTCTests",		// OK
"CallTCTests",				// C == I : 1 fails callError6
"ComprehensionTCTests",		// C == I:  2 fail: emptyTupleGeneratorError[34]
			
"DataDeclarationTCTests",	// OK
"DataTypeTCTests",			// OK
"DeclarationTCTests",		// OK
"ImportTCTests",			// OK
"PatternTCTests",			// OK
"ProjectionTCTests",		// OK
"RegExpTCTests",			// OK
"ScopeTCTests",				// OK
"StatementTCTests",			// OK
"SubscriptTCTests",			// OK
"VisitTCTests"				// OK
];


private list[str] files_with_tests =
[
"demo::basic::Ackermann",                             // OK
"demo::basic::Bubble",                                // OK
"demo::basic::Factorial",                             // OK
"demo::common::Calls",                                // OK
"demo::common::ColoredTrees",                         // OK
"demo::common::CountConstructors",                    // OK
"demo::common::Cycles",                               // OK
"demo::common::Derivative",                           // OK
"demo::common::Lift",                                 // OK
"demo::common::StringTemplate",                       // OK
"demo::common::Trans",                                // OK
"demo::common::WordReplacement",                      // OK
"demo::common::WordCount::CountInLine1",               // OK
"demo::common::WordCount::CountInLine2",               // OK
"demo::common::WordCount::CountInLine3",               // OK
"demo::common::WordCount::WordCount",                  // OK
"demo::Dominators",                                  // OK
"demo::lang::Exp::Abstract::Eval",                      // OK
"demo::lang::Exp::Combined::Automatic::Eval",            // OK
"demo::lang::Exp::Combined::Manual::Eval",               // OK
"demo::lang::Exp::Concrete::NoLayout::Eval",             // OK
"demo::lang::Exp::Concrete::WithLayout::Eval",           // OK
"demo::lang::Func::Test",                              // OK
"demo::lang::Lisra::Test",                             // OK
"demo::McCabe",                                      // OK
"demo::ReachingDefs",                                // OK
"demo::Slicing",                                     // OK
"demo::Uninit",                                      // OK
"lang::rascal::format::Escape",                        // OK
"lang::rascal::format::Grammar",                       // OK
"lang::rascal::grammar::Lookahead",                    // OK
"lang::rascal::grammar::tests::ParserGeneratorTests",   // ok
"lang::rascal::grammar::tests::PicoGrammar",            // ok
"lang::rascal::grammar::tests::CGrammar",            	// ok
"lang::rascal::grammar::tests::CharactersTests", 		// OK
"lang::rascal::grammar::tests::LiteralsTests", 			// 5 fail
"lang::rascal::grammar::tests::LookaheadTests",         // OK
"lang::rascal::grammar::tests::RascalGrammar",          // ok
"lang::rascal::syntax::tests::ConcreteSyntax",          // OK
"lang::rascal::syntax::tests::ExpressionGrammars",      // OK
"lang::rascal::syntax::tests::ImplodeTests",            // 4 fail
"lang::rascal::syntax::tests::KnownIssues",             // OK
//"lang::rascal::syntax::tests::ParsingRegressionTests",  // OK
"lang::rascal::meta::ModuleInfoTests",  
"lang::rascal::syntax::tests::PreBootstrap",            // OK
"lang::rascal::syntax::tests::SolvedIssues",            // OK
"lang::rascal::types::tests::AbstractKindTests",		// OK
"lang::rascal::types::tests::AbstractNameTests",		// OK
//"lang::rascal::types::tests::TypeInstantiationTests",
"lang::rascal::types::tests::UtilTests",				// OK
"lang::yaml::Model",                                  // Error
"util::PriorityQueue",                               // OK
"util::UUID"                                         // OK
];

private list[str] reachability_tests = [
"ConcretePatternTests1",
"ConcretePatternTests2",
"ConcretePatternTests3",
"ConcreteSyntaxTests1",
"ConcreteSyntaxTests2",
"ConcreteSyntaxTests3",
"PatternTests",
"PatternDescendantTests",
"StatementTests",						
"VisitTests"	
];

private lrel[str,str] crashes = [];
private lrel[str,str] partial_results = [];

lrel[loc,int,str] runTests(list[str] names, str base, PathConfig pcfg){
 all_test_results = [];
 for(tst <- names){
      prog = base == "" ? tst : (base + "::" + tst);
      //for(str ext <- ["sig", "sigs", "tc", "rvm.gz", "rvm.ser.gz"]){
      // if(<true, l> := getDerivedReadLoc(prog, ext, pcfg)){
      //    remove(l);
      // }
      //}
      try {
	      if(lrel[loc src,int n,str msgs] test_results := execute(prog, pcfg, recompile=true, testsuite=true, jvm=true)){
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
  
value main() = allRascalTests(binDir=|home:///bin-tests-comp|);
  
value allRascalTests(loc binDir=|home:///bin-tests-intp|){
  
  println("Using binDir = <binDir>");
  timestamp = now();
  crashes = [];
  partial_results = [];
  all_results = [];
  
  pcfg = pathConfig(binDir=binDir, libPath=[binDir]);
  
  all_results += runTests(basicTests, "lang::rascal::tests::basic", pcfg);
  //all_results += runTests(functionalityTests, "lang::rascal::tests::functionality", pcfg);
  //all_results += runTests(libraryTests, "lang::rascal::tests::library", pcfg);
  //all_results += runTests(importTests, "lang::rascal::tests::imports", pcfg);
  //all_results += runTests(extendTests, "lang::rascal::tests::extends", pcfg);  
  //all_results += runTests(files_with_tests, "", pcfg);
  //all_results += runTests(typeTests, "lang::rascal::tests::types", pcfg);
   
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
