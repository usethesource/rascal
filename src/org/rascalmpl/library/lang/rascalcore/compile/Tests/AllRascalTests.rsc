module lang::rascalcore::compile::Tests::AllRascalTests

import IO;
import List;
import DateTime;
import lang::rascalcore::compile::Execute;
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
	"TestsForTests",
	"Tuples"					
];


private list[str] functionalityTests = [

"AccumulatingTests",
"AliasTests",
"AnnotationTests",
"AssignmentTests",
"BacktrackingTests",
"CallTests",
"CallTestsAux",
//"CommonKeywordParameterImportTests1::DiamondTop",
//"CommonKeywordParameterImportTests1::DiamondLeft",
//"CommonKeywordParameterImportTests1::DiamondRight",
//"CommonKeywordParameterImportTests1::DiamondBottom",
//
//"CommonKeywordParameterImportTests2::DiamondTop",
//"CommonKeywordParameterImportTests2::DiamondLeft",
//"CommonKeywordParameterImportTests2::DiamondRight",
//"CommonKeywordParameterImportTests2::DiamondBottom",
//
//"CommonKeywordParameterImportTests3::A",
//"CommonKeywordParameterImportTests3::B",
//"CommonKeywordParameterImportTests3::C",
//"CommonKeywordParameterImportTests3::D",
//"CommonKeywordParameterImportTests3::Tests",

"ComprehensionTests",
"ConcretePatternTests1",
"ConcretePatternTests2",
"ConcretePatternTests3",
"ConcreteSubscriptAndSliceTests",
"ConcreteSyntaxKeywordFields",
"ConcreteSyntaxTests1",
"ConcreteSyntaxTests2",
"ConcreteSyntaxTests3", 
"ConcreteSyntaxTests4",
"ConcreteSyntaxTests5",
"ConcreteTerms",
"DataDeclarationTests",
"DataTypeTests",
"DeclarationTests",
"FunctionCompositionTests",
"InterpolationTests",
"KeywordParameterImportTests1::DiamondTop",
"KeywordParameterImportTests1::DiamondLeft",
"KeywordParameterImportTests1::DiamondRight",
"KeywordParameterImportTests1::DiamondBottom",
"KeywordParameterImportTests2::Tests",
"KeywordParameterTests",
"LayoutTests",
"ParsingTests",
"PatternTests",
"PatternDescendantTests",
"PatternList3Tests",
"ProjectionTests",
"RangeTests",				// OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
"ReducerTests",
"RegExpTests",
//"ScopeTests",				// OK but OutOfMemory????
"SetMatchTests1", 
"SetMatchTests2",
"SimpleVisitTest",
"StatementTests",
"SubscriptTests",
"TryCatchTests",  				
"VisitTests"
];

private list[str] importTests = [
"ImportTests1", 
"ImportTests2",
"ImportTests3",
"ImportTests4",
"ImportTests5",
"ImportTests6",
"ImportTests7",
"ImportTests8", 
"ImportTests9", 
"ModuleInitRange"
];


private list[str] libraryTests = [
"BooleanTests",
"DateTimeTests",
"IntegerTests",
"ListRelationTests",
"ListTests",
"MapTests",
"MathTests",
"NodeTests",
"NumberTests",
"RelationTests",
"SetTests",
"StringTests",
"TypeTests",
"ValueIOTests",
//"analysis::formalconcepts::FCATest",
"analysis::graphs::GraphTests",
"analysis::statistics::DescriptiveTests",
"analysis::statistics::RangeUtils",
"lang::csv::CSVIOTests",
"lang::json::JSONIOTests",
"util::SemVerTests"
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
"StaticTestingUtilsTests",
"AccumulatingTCTests",
//"AliasTCTests",			// C & I: Overflow/LOOP?
"AllStaticIssues",
"AnnotationTCTests",
"AssignmentTCTests",
"CallTCTests",				// C == I : 1 fails callError6
"ComprehensionTCTests",
			
"DataDeclarationTCTests",
"DataTypeTCTests",
"DeclarationTCTests",
"ImportTCTests",
"PatternTCTests",
"ProjectionTCTests",
"RegExpTCTests",
"ScopeTCTests",
"StatementTCTests",
"SubscriptTCTests",
"VisitTCTests"
];


private list[str] files_with_tests =
[
"demo::basic::Ackermann",
"demo::basic::Bubble",
"demo::basic::Factorial",
"demo::common::Calls",
"demo::common::ColoredTrees",
"demo::common::CountConstructors",
"demo::common::Cycles",
"demo::common::Derivative",
"demo::common::Lift",
"demo::common::StringTemplate",
"demo::common::Trans",
"demo::common::WordReplacement",
"demo::common::WordCount::CountInLine1",
"demo::common::WordCount::CountInLine2",
"demo::common::WordCount::CountInLine3",
"demo::common::WordCount::WordCount",
"demo::Dominators", 
"demo::lang::Exp::Abstract::Eval",
"demo::lang::Exp::Combined::Automatic::Eval",
"demo::lang::Exp::Combined::Manual::Eval",
"demo::lang::Exp::Concrete::NoLayout::Eval",
"demo::lang::Exp::Concrete::WithLayout::Eval",
"demo::lang::Func::Test",
"demo::lang::Lisra::Test",
"demo::lang::Lisra::Parse",
"demo::lang::Lisra::Pretty",
"demo::McCabe",
"demo::ReachingDefs",
"demo::Slicing",
"demo::Uninit",
"lang::rascal::format::Escape",
"lang::rascal::format::Grammar",
"lang::rascal::grammar::Lookahead",
"lang::rascal::grammar::tests::ParserGeneratorTests",
"lang::rascal::grammar::tests::PicoGrammar",
"lang::rascal::grammar::tests::CGrammar",
"lang::rascal::grammar::tests::CharactersTests",
"lang::rascal::grammar::tests::LiteralsTests",
"lang::rascal::grammar::tests::LookaheadTests",
"lang::rascal::grammar::tests::RascalGrammar",
"lang::rascal::syntax::tests::ConcreteSyntax",
"lang::rascal::syntax::tests::ExpressionGrammars",
"lang::rascal::syntax::tests::ImplodeTests",          // 4 fail
"lang::rascal::syntax::tests::KnownIssues",
//"lang::rascal::syntax::tests::ParsingRegressionTests",  // OK, but parses 800 files, takes too long
"lang::rascal::meta::ModuleInfoTests",
"lang::rascal::syntax::tests::PreBootstrap",
"lang::rascal::syntax::tests::SolvedIssues",
"lang::rascal::types::tests::AbstractKindTests",
"lang::rascal::types::tests::AbstractNameTests",
"lang::rascal::types::tests::TypeInstantiationTests", 
"lang::rascal::types::tests::UtilTests",
// "lang::yaml::Model",                               // Error during reading (halts JVM version)
"util::PriorityQueue",
"util::UUID" 
//"lang::rascal::tests::library::lang::java::m3::BasicM3Tests"  // requires jdt and still has ties with interpreter
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
  
value main(bool jvm=true) = allRascalTests(pathConfig());
  
value allRascalTests(PathConfig pcfg){ //loc bin=|home:///bin-tests-intp|, loc boot=|boot:///|, bool jvm=true){

  println("Using <pcfg>");
  jvm = true;
  timestamp = now();
  crashes = [];
  partial_results = [];
  lrel[loc,int,str] all_results = [];
  jvm = true;
  
  //pcfg = pathConfig(srcs=[|std:///|], bin=bin, boot=boot, libs=[bin]);
  
  all_results += runTests(basicTests, "lang::rascal::tests::basic", pcfg, jvm=jvm);
  all_results += runTests(functionalityTests, "lang::rascal::tests::functionality", pcfg, jvm=jvm);
  all_results += runTests(libraryTests, "lang::rascal::tests::library", pcfg, jvm=jvm);
  all_results += runTests(importTests, "lang::rascal::tests::imports", pcfg, jvm=jvm);
  all_results += runTests(extendTests, "lang::rascal::tests::extends", pcfg, jvm=jvm);  
  all_results += runTests(files_with_tests, "", pcfg, jvm=jvm);
  //all_results += runTests(typeTests, "lang::rascal::tests::types", pcfg, jvm=jvm);
   
  println("TESTS RUN AT <timestamp>");
  println("\nRESULTS PER FILE:");
  for(<prog, s> <- partial_results)
      println("<prog>: <s>");
  
  println("\nFailed/IGNORED TESTS:");
  //printTestReport(testResults(all_results, []));
  
  if(size(crashes) > 0){
     println("\nCRASHED TESTS:");
     for(<prog, e> <- crashes)
         println("<prog>: <e>");
  }
  
  return size(all_results[_,0]) == 0;
}
