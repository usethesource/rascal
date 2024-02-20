module lang::rascalcore::check::AllRascalTests

/*
    Run all Rascal tests
*/
import IO;
import List;
import Set;
import Map;
import DateTime;
extend lang::rascalcore::check::Checker;
import lang::rascalcore::check::RascalConfig;
import String;
import ParseTree;

import util::Reflective;

import util::FileSystem;
import ValueIO;

//import lang::rascalcore::compile::Compile;

// Percentage of succeeded tests, see spreadsheet TestOverview.ods


private list[str] basicTests = [
    //"Booleans",
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

private list[str] concreteTests = [
"Call",
"Character",
"Field",
"isDefined",
"Layout",
"Matching",
"OtherSyntax",
"Parsing",
"Patterns1",
"Patterns2",
"Patterns3",
"PostParseFilter",
"SubscriptAndSlice",
"Syntax1",
"Syntax2",
"Syntax3", 
"Syntax4",
"Syntax5",
"Syntax6",
"SyntaxKeywordFields",
"Terms"
];

private list[str] functionalityTests = [

"Accumulating",
"Alias",
"Annotation",
"Assignment",
"Backtracking",
"Call",
"CallAux",
"CommonKeywordParameterImport1::DiamondTop",
"CommonKeywordParameterImport1::DiamondLeft",
"CommonKeywordParameterImport1::DiamondRight",
"CommonKeywordParameterImport1::DiamondBottom",

"CommonKeywordParameterImport2::DiamondTop",
"CommonKeywordParameterImport2::DiamondLeft",
"CommonKeywordParameterImport2::DiamondRight",
"CommonKeywordParameterImport2::DiamondBottom",

"CommonKeywordParameterImport3::A",
"CommonKeywordParameterImport3::B",
"CommonKeywordParameterImport3::C",
"CommonKeywordParameterImport3::D",
"CommonKeywordParameterImport3::Tests",

"Comprehension",

"DataDeclaration",
"DataType",
"Declaration",
"FunctionComposition",
"Interpolation",
"KeywordParameterImport1::DiamondTop",
"KeywordParameterImport1::DiamondLeft",
"KeywordParameterImport1::DiamondRight",
"KeywordParameterImport1::DiamondBottom",
"KeywordParameterImport2::Tests",
"KeywordParameter",

"Pattern",
"PatternDescendant",
"PatternList1",
"PatternList2",
"PatternSet1",
"PatternSet2",
"PatternSet3",
"Projection",
"Range",
"Reducer",
"RegExp",
"Reification",
"Scoping",       
"SimpleVisit",
"Statement",
"Subscription",
"TryCatch",                
"Visit1",
"Visit2"
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

alias TestResults = tuple[list[value] crashes, set[map[str,list[Message]] msgs] msgsPerModule];

TestResults runTests(list[str] names, str base, PathConfig pcfg){
 all_test_msgs = {};
 list[value] crashes = [];
 for(tst <- names){
      try {
          prog = base == "" ? tst : (base + "::" + tst);
          println("TYPECHECKING <prog>");
          mname2msgs = filterErrors(checkModules([prog], rascalTypePalConfig(), pcfg));
          iprintln(mname2msgs);
          all_test_msgs += mname2msgs;
      } catch value e:
        crashes += e;
  }
  return <crashes, all_test_msgs>;
}
  
//value main() = allRascalTests();
  
set[map[str, list[Message]]] allRascalTests(PathConfig pcfg= pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|
               ])){ //loc bin=|home:///bin-tests-intp|, loc boot=|boot:///|, bool jvm=true){

  println("Using <pcfg>");
  set[map[str, list[Message]]] all_msgs = {};
  list[value] all_crashes = [];
  //pcfg = pathConfig(srcs=[|std:///|], bin=bin, boot=boot, libs=[bin]);
  
  TestResults res = runTests(basicTests, "lang::rascal::tests::basic", pcfg);
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  res = runTests(functionalityTests, "lang::rascal::tests::functionality", pcfg);
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  res = runTests(libraryTests, "lang::rascal::tests::library", pcfg);
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  res = runTests(importTests, "lang::rascal::tests::imports", pcfg);
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  res = runTests(extendTests, "lang::rascal::tests::extends", pcfg); 
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  res = runTests(files_with_tests, "", pcfg);
  all_crashes += res.crashes; all_msgs += res.msgsPerModule;
  
  //res = runTests(typeTests, "lang::rascalcore::check::tests");
  //all_crashes += res.crashes; all_msgs += res.msgsPerModule;
   
   for(msg <- all_msgs)
      println(msg);
   for(v <- all_crashes)
      println(v);
   return all_msgs;
}

//tuple[TModel org, map[str,TModel] differences] sameTPL(str qualifiedModuleName, PathConfig pcfg= pathConfig(   
//        srcs = [|project://rascal-core/src/org/rascalmpl/library/|,
//                |project://typepal/src|,
//                |project://rascal/src/org/rascalmpl/library|
//               ])){
//    
//    <_, msgs> = checkModules([qualifiedModuleName], rascalTypePalConfig(), pcfg);
//    iprintln(msgs);       
//    mTplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
//    mOrgModel = readBinaryValueFile(#TModel, mTplLoc);
//    
//    differences = ();
//   
//    base = "lang::rascal::tests::basic";
//    names = basicTests;
//    for(tst <- names){
//      try {
//        if(exists(mTplLoc)){
//            remove(mTplLoc);
//        }
//      } catch e: println("@@@@ while removing <mTplLoc>: <e> @@@@");
//      try {
//          prog = base == "" ? tst : (base + "::" + tst);
//          println("TYPECHECKING <prog>");
//          <_, msgs> = checkModules([prog], rascalTypePalConfig(), pcfg);
//          iprintln(msgs);
//          if(exists(mTplLoc)){
//              mNewModel = readBinaryValueFile(#TModel, mTplLoc);
//              df = diff(mOrgModel, mNewModel);
//              if(!isEmpty(df)){
//                println("**** diff for <tst>");
//                println(df);
//                differences[prog] = mNewModel;
//              }
//          }
//      } catch e:
//        println("@@@@ EXCEPTION: <e> @@@@@@");
//  }
//  println("<size(names)> modules; <size(differences)> differences");
//  return <mOrgModel, differences>;
//}

bool blacklisted(str qualifiedModuleName){
    //for(s <- {//"lang::rascal::types", "experiments::Compiler", "lang::rascal::boot", "lang::rascal::tests::types" , "experiments::tutor3", "lang::java::patterns", "lang::sdf2", "lang::box", "Sudoku
    //          "lang::java::flow::JavaToObjectFlow", "lang::java::patterns::JavaToMicroPatterns", "lang::sdf2::util::SDF2Grammar", "lang::sdf2::util::Importer",
    //          "lang::rascal::tests::library::analysis::formalconcepts::FCATest", "experiments::tutor3::LegacyExamManager", "lang::rascalcore::compile::Benchmarks::JavaMetrics",
    //          "lang::rascalcore::compile::Benchmarks::SudokuEq", "experiments::Compiler::Benchmarks::SudokuEq", "lang::rascal::checker::TTL::TTLGen"
    //          // "experiments", "tests", "types", "boot", "Compiler"
    //         }
    //){
    //    if(contains(qualifiedModuleName, s)) return true;
    //}
    return contains(qualifiedModuleName, "Java18");
}

bool whitelisted(str qualifiedModuleName){
    return true;
    //for(s <- {"lang::rascal"}){
    //   if(contains(qualifiedModuleName, s)) return true;
    //}
    //return false;
}

list[Message] filterErrors(list[Message] msgs){
    return [msg | msg <- msgs, error(_,_) := msg];
}
map[str, list[Message]] filterErrors(map[str, list[Message]] modsAndMsgs){
    return (mname : msgs | mname <- modsAndMsgs, msgs := filterErrors(modsAndMsgs[mname]), !isEmpty(msgs));
}

void main() = allFiles();

void allFiles(PathConfig pcfg = pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://rascal_eclipse/src/org/rascalmpl/eclipse/library|,
                |project://salix/src|
                //|std:///|
               ],
         bin = |test-modules:///rascal-core-bin|,
         libs = [/*|lib://rascal/|, |lib://typepal/|*/])){
    modulePaths =  find(|project://rascal/src/org/rascalmpl/library|, bool(loc l) { return endsWith(l.path, ".rsc"); });
                   //find(|std:///lang/rascal/tests|, bool(loc l) { return endsWith(l.path, ".rsc"); });
                   // find(|project://salix/src|, bool(loc l) { return endsWith(l.path, ".rsc"); });
                  //find(|project://rascal-core/src/org/rascalmpl/core/library|, bool(loc l) { return endsWith(l.path, ".rsc"); });
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
        if(!whitelisted(qualifiedModuleName)){
            nskipped += 1;
            continue;
        }
        println("\>\>\> <ncount>: CHECKING <qualifiedModuleName> (N:<size(modulePaths)>/E:<size(problems)>/C:<size(crashed)>/S:<nskipped>)");
        try {
            modulesAndmsgs = filterErrors(checkModules([qualifiedModuleName], rascalTypePalConfig(), pcfg));
            //modulesAndmsgs = (qualifiedModuleName : filterErrors(compile(qualifiedModuleName, rascalTypePalConfig(),pcfg)));
            if(modulesAndmsgs[qualifiedModuleName]?) iprintln(modulesAndmsgs);
            problems += modulesAndmsgs;
            //if(modulesAndmsgs[qualifiedModuleName]?) {
            //    mpath = replaceAll(qualifiedModuleName, "::", "/");
            //    if(any(msg <- msgs, contains(msg.at.path, mpath))){
            //        problems[qualifiedModuleName] = msgs;
            //    }
            //}
        } catch value e: {
            crashed[qualifiedModuleName] = e;
        }
    }
    iprintln(problems);
    iprintln(crashed);
    println("<size(modulePaths)> files, <size(problems)> with a problem, <size(crashed)> crashed, <nskipped> skipped");
    println("\nWith errors:"); iprintln(sort(domain(problems)));
    println("\nCrashed:"); iprintln(crashed);
}
