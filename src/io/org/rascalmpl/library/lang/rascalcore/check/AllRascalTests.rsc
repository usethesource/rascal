module lang::rascalcore::check::AllRascalTests


import IO;
import List;
import Set;
import DateTime;
extend lang::rascalcore::check::Checker;
import String;
import ParseTree;

import util::Reflective;

import util::FileSystem;

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
    "Relations" ,
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
"CommonKeywordParameterImportTests1::DiamondTop",
"CommonKeywordParameterImportTests1::DiamondLeft",
"CommonKeywordParameterImportTests1::DiamondRight",
"CommonKeywordParameterImportTests1::DiamondBottom",

"CommonKeywordParameterImportTests2::DiamondTop",
"CommonKeywordParameterImportTests2::DiamondLeft",
"CommonKeywordParameterImportTests2::DiamondRight",
"CommonKeywordParameterImportTests2::DiamondBottom",

"CommonKeywordParameterImportTests3::A",
"CommonKeywordParameterImportTests3::B",
"CommonKeywordParameterImportTests3::C",
"CommonKeywordParameterImportTests3::D",
"CommonKeywordParameterImportTests3::Tests",

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
"ConcreteSyntaxTests6",
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
"RangeTests",
"ReducerTests",
"RegExpTests",
"ScopeTests",       
"SetMatchTests1", 
"SetMatchTests2",
"SimpleVisitTest",
"StatementTests",
"SubscriptTests",
"TryCatchTests",                
"VisitTests1",
"VisitTests2"
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
"CHECKTYPES",
"COMPILER",
"Extension1",
"Extension2",
"Extension3",
"PARENT",
"PARSETREE",
"TYPE",
"UseExtendBase",
"UseExtendBaseExtended",
"UseImportBase",
"UseImportBaseExtended"
];

private list[str] typeTests = [
"StaticTestingUtilsTests",
"AccumulatingTCTests",
//"AliasTCTests",           // C & I: Overflow/LOOP?
"AllStaticIssues",
"AnnotationTCTests",
"AssignmentTCTests",
"CallTCTests",              // C == I : 1 fails callError6
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
"lang::rascal::tests::types::AbstractKindTests",
"lang::rascal::tests::types::AbstractNameTests",
"lang::rascal::tests::types::TypeInstantiationTests", 
"lang::rascal::tests::types::UtilTests",
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

tuple[list[value] crashes, list[Message] msgs] runTests(list[str] names, str base){
 list[Message] all_test_msgs = [];
 list[value] crashes = [];
 for(tst <- names){
      try {
          prog = base == "" ? tst : (base + "::" + tst);
          println("TYPECHECKING <prog>");
          msgs = validateModules(prog);
          iprintln(msgs);
          all_test_msgs += msgs;
      } catch e:
        crashes += e;
  }
  return <crashes, all_test_msgs>;
}
  
value main(bool jvm=true) = allRascalTests();
  
set[Message] allRascalTests(PathConfig pcfg= pathConfig(   
        srcs = [|project://rascal-core/src/io/org/rascalmpl/library/|,
                |project://TypePal/src|,
                |project://rascal/src/org/rascalmpl/library|
               ])){ //loc bin=|home:///bin-tests-intp|, loc boot=|boot:///|, bool jvm=true){

  println("Using <pcfg>");
  list[Message] all_msgs = [];
  list[value] all_crashes = [];
  tuple[list[value] crashes, list[Message] msgs] res;
  //pcfg = pathConfig(srcs=[|std:///|], bin=bin, boot=boot, libs=[bin]);
  
  res = runTests(basicTests, "lang::rascal::tests::basic");
  all_crashes += res.crashes; all_msgs += res.msgs;
  res = runTests(functionalityTests, "lang::rascal::tests::functionality");
  all_crashes += res.crashes; all_msgs += res.msgs;
  res = runTests(libraryTests, "lang::rascal::tests::library");
  all_crashes += res.crashes; all_msgs += res.msgs;
  res = runTests(importTests, "lang::rascal::tests::imports");
  all_crashes += res.crashes; all_msgs += res.msgs;
  res = runTests(extendTests, "lang::rascal::tests::extends"); 
  all_crashes += res.crashes; all_msgs += res.msgs;
  res = runTests(files_with_tests, "");
  all_crashes += res.crashes; all_msgs += res.msgs;
  
  //res = runTests(typeTests, "lang::rascal::tests::types", pcfg);
  //all_crashes += res.crashes; all_msgs += res.msgs;
   
   set_all_msgs = toSet(all_msgs);
   
   for(msg <- set_all_msgs)
      println(msg);
   for(v <- all_crashes)
      println(v);
   return set_all_msgs;
}

tuple[TModel org, map[str,TModel] differences] sameTPL(str qualifiedModuleName, PathConfig pcfg= pathConfig(   
        srcs = [|project://rascal-core/src/io/org/rascalmpl/library/|,
                |project://TypePal/src|,
                |project://rascal/src/org/rascalmpl/library|
               ])){
    
    msgs = validateModules(qualifiedModuleName);
    iprintln(msgs);       
    mTplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
    mOrgModel = readBinaryValueFile(#TModel, mTplLoc);
    
    differences = ();
   
    base = "lang::rascal::tests::basic";
    names = basicTests;
    for(tst <- names){
      try {
        if(exists(mTplLoc)){
            remove(mTplLoc);
        }
      } catch e: println("@@@@ while removing <mTplLoc>: <e> @@@@");
      try {
          prog = base == "" ? tst : (base + "::" + tst);
          println("TYPECHECKING <prog>");
          msgs = validateModules(prog);
          iprintln(msgs);
          if(exists(mTplLoc)){
              mNewModel = readBinaryValueFile(#TModel, mTplLoc);
              df = diff(mOrgModel, mNewModel);
              if(!isEmpty(df)){
                println("**** diff for <tst>");
                println(df);
                differences[prog] = mNewModel;
              }
          }
      } catch e:
        println("@@@@ EXCEPTION: <e> @@@@@@");
  }
  println("<size(names)> modules; <size(differences)> differences");
  return <mOrgModel, differences>;
}

bool blacklisted(str qualifiedModuleName){
    for(s <- {//"lang::rascal::types", "experiments::Compiler", "lang::rascal::boot", "lang::rascal::tests::types" , "experiments::tutor3", "lang::java::patterns", "lang::sdf2", "lang::box", "Sudoku
              "lang::java::flow::JavaToObjectFlow", "lang::java::patterns::JavaToMicroPatterns", "lang::sdf2::util::SDF2Grammar", "lang::sdf2::util::Importer",
              "lang::rascal::tests::library::analysis::formalconcepts::FCATest", "experiments::tutor3::LegacyExamManager", "experiments::Compiler::Benchmarks::JavaMetrics",
              "experiments::Compiler::Benchmarks::SudokuEq", "lang::rascal::checker::TTL::TTLGen"
             }
    ){
        if(contains(qualifiedModuleName, s)) return true;
    }
    return false;
}

void allFiles(PathConfig pcfg = pathConfig(   
        srcs = [|project://rascal-core/src/io/org/rascalmpl/library/|,
                |project://TypePal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |std:///|
               ])){
    modulePaths = find(|std:///|, bool(loc l) { return endsWith(l.path, ".rsc"); });
    println("<size(modulePaths)> files");
    problems = ();
    crashed = ();
    nskipped = 0;
    ncount = 0;
    for(p <- modulePaths){
        qualifiedModuleName = getModuleName(p, pcfg);
        ncount += 1;
        if(blacklisted(qualifiedModuleName)){
           println("\>\>\> <ncount>: SKIPPING <qualifiedModuleName>");
           nskipped += 1;
           continue;
        }
        println("\>\>\> <ncount>: CHECKING <qualifiedModuleName> (N:<size(modulePaths)>/E:<size(problems)>/C:<size(crashed)>/S:<nskipped>)");
        try {
            msgs = validateModules(qualifiedModuleName);
            iprintln(msgs);
            if(!isEmpty(msgs)) {
                mpath = replaceAll(qualifiedModuleName, "::", "/");
                if(any(msg <- msgs, contains(msg.at.path, mpath))){
                    problems[qualifiedModuleName] = msgs;
                }
            }
        } catch e: {
            crashed[qualifiedModuleName] = e;
        }
    }
    iprintln(problems);
    iprintln(crashed);
    println("<size(modulePaths)> files, <size(problems)> with a problem, <size(crashed)> crashed, <nskipped> skipped");
    println("\nWith errors:"); iprintln(sort(domain(problems)));
    println("\nCrashed:"); iprintln(crashed);
}
