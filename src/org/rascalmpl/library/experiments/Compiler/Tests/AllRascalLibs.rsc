module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;

/*
 * Results of compiling Rascal library modules.
 */

list[str] libs = [


"Boolean", 								// OK
"DateTime",								// OK
"Exception", 							// OK
"Grammar", 								// OK
"IO",									// OK
"List", 								// OK
"ListRelation",							// OK
"Map", 									// OK
"Message", 								// OK
"Node",									// OK
"Origins",								// OK
"ParseTree", 							// OK		
"Prelude",								// OK	
"Relation",								// OK
"Set",									// OK
"String",								// OK
"Time", 								// OK
"Type", 								// OK
"ToString", 							// OK
"Traversal",							// OK
"Tuple", 								// OK
"ValueIO", 								// OK
"util::Benchmark",						// OK
"util::Eval",							// OK
"util::FileSystem", 					// OK
"util::Highlight",						// ERROR
"util::Math",							// OK
"util::Maybe",							// OK
"util::Monitor",						// OK
"util::PriorityQueue",					// OK
"util::Reflective", 					// ERROR
"util::ShellExec",						// OK
"util::Webserver"						// OK
/*
// Not yet OK

"Ambiguity",			// #483
						//|rascal://lang::rascal::types::CheckTypes|(31671,1,<634,13>,<634,14>): Expected map[RName, int], but got map[RName, value]     	


"APIGen", 			 	// #482
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3641,16,<89,62>,<89,78>))
						//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3281,33,<80,24>,<80,57>))
						//error("Name cs is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7837,2,<207,39>,<207,41>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7726,25,<206,46>,<206,71>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2134,15,<61,11>,<61,26>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7738,13,<206,58>,<206,71>))
						//error("Name t2 is not in scope",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3093,2,<73,87>,<73,89>))
						//error("Function of type fun str(str) cannot be called with argument types (inferred(11))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(2159,18,<61,36>,<61,54>))
						//error("Multiple constructors and/or productions match this pattern, add additional type annotations",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (inferred(41))",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3603,21,<89,24>,<89,45>))
						//error("Function of type fun str(Symbol) cannot be called with argument types (value)",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3468,22,<85,26>,<85,48>))
						//error("Field definitions does not exist on type type",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7769,13,<206,89>,<206,102>))
						//error("Expected type str, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7795,48,<206,115>,<207,45>))
						//error("Type of pattern could not be computed",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(6028,12,<166,11>,<166,23>))
						//error("Expected type bool, found fail",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(7753,39,<206,73>,<206,112>))
						//error("Invalid return type str, expected return type void",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3323,61,<81,5>,<81,66>))
						//error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/APIGen.rsc|(3259,56,<80,2>,<80,58>))

// "Number"				// DEPRECATED: TC gives errors


"util::LOC"			// #394
				
				// error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(11024,119,<278,7>,<278,126>))
				// error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/ParseTree.rsc|(11066,53,<278,49>,<278,102>))					

"analysis::formalconcepts::FCA",
//error("Name object is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2841,6,<75,13>,<75,19>))
//error("Name attribute is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2949,9,<76,41>,<76,50>))
//error("Alias Object2Attributes declares 2 type parameters, but given 0 instantiating types",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2576,17,<69,58>,<69,75>))
//error("Name p is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(5607,1,<154,26>,<154,27>))
//error("Name attribute is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2849,9,<75,21>,<75,30>))
//error("Alias Attribute2Objects declares 2 type parameters, but given 0 instantiating types",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2796,17,<74,58>,<74,75>))
//error("Name attribute is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2626,9,<70,21>,<70,30>))
//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2796,28,<74,58>,<74,86>))
//error("Type of pattern could not be computed, please add additional type annotations",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2576,25,<69,58>,<69,83>))
//error("Initializer type rel[set[&Object \<: value] objects, set[&Attribute \<: value] attributes] not assignable to variable of type fail",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(5547,32,<153,20>,<153,52>))
//error("Name object is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2618,6,<70,13>,<70,19>))
//error("Name object is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2720,6,<71,44>,<71,50>))
//error("Type concept_t not declared",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(5532,14,<153,5>,<153,19>))
//error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2745,80,<74,7>,<74,87>))
//error("Could not calculate function type because of errors calculating the parameter types",|project://rascal/src/org/rascalmpl/library/analysis/formalconcepts/FCA.rsc|(2525,77,<69,7>,<69,84>))

"analysis::graphs::Graph",					// OK		
"analysis::graphs::LabeledGraph",			// OK
"analysis::linearprogramming::LinearProgramming",
//error("Constructor overlaps existing constructors in the same datatype : 265, {19}",|project://rascal/src/org/rascalmpl/library/analysis/linearprogramming/LinearProgramming.rsc|(792,4,<26,30>,<26,34>))
//error("Constructor overlaps existing constructors in the same datatype : 262, {18}",|project://rascal/src/org/rascalmpl/library/analysis/linearprogramming/LinearProgramming.rsc|(784,5,<26,22>,<26,27>))
//error("Name varVals is not in scope",|project://rascal/src/org/rascalmpl/library/analysis/linearprogramming/LinearProgramming.rsc|(1878,7,<57,27>,<57,34>))
//error("Function of type fun Solution(LLSolution, list[str]) cannot be called with argument types (value,list[str])",|project://rascal/src/org/rascalmpl/library/analysis/linearprogramming/LinearProgramming.rsc|(2813,30,<88,33>,<88,63>))
//error("Constructor overlaps existing constructors in the same datatype : 268, {20}",|project://rascal/src/org/rascalmpl/library/analysis/linearprogramming/LinearProgramming.rsc|(799,5,<26,37>,<26,42>))

"analysis::m3::AST",						// OK
"analysis::m3::Core",						// OK
"analysis::m3::Registry",
//error("Unable to bind subject type str to assignable",|project://rascal/src/org/rascalmpl/library/analysis/m3/Registry.rsc|(2765,19,<92,4>,<92,23>))
//error("Unable to bind subject type str to assignable",|project://rascal/src/org/rascalmpl/library/analysis/m3/Registry.rsc|(2434,19,<81,4>,<81,23>))

"analysis::m3::TypeSymbol",					// OK
"analysis::statistics::Correlation",		// OK
"analysis::statistics::Descriptive",		// OK
"analysis::statistics::Frequency",			// OK
"analysis::statistics::Inference",			// OK
"analysis::statistics::SimpleRegression"	// OK

// DEMO

"demo::basic::Ackermann",					// OK
"demo::basic::Bottles",						// OK
"demo::basic::Bubble",						// OK
"demo::basic::BubbleTest",					// OK
"demo::basic::Factorial",					// OK
"demo::basic::FactorialTest",				// OK

"demo::basic::FizzBuzz",					// OK
"demo::basic::Hello",						// OK
"demo::basic::Quine",
//Java("FactParseError","Expected : but got  ")
"demo::basic::Squares",
"demo::common::WordCount::CountInLine1",	// OK
"demo::common::WordCount::CountInLine2",	// OK
"demo::common::WordCount::CountInLine3",	// OK
"demo::common::WordCount::WordCount",		// OK
"demo::common::Calls",						// OK
"demo::common::ColoredTrees",				// OK
"demo::common::ColoredTreesTest",			// OK
"demo::common::CountConstructors",			// OK
"demo::common::Crawl",						// OK
"demo::common::Cycles",						// OK
"demo::common::Derivative",					// OK
"demo::common::Lift",						// OK
"demo::common::LiftTest",					// OK
"demo::common::StringTemplate",				// OK
"demo::common::StringTemplateTest",			// OK
"demo::common::Trans",						// OK
"demo::common::WordReplacement",			// OK
"demo::common::WordReplacementTest",		// OK

"demo::lang::Exp::Abstract::Eval",			// OK

"demo::lang::Exp::Combined::Automatic::Load",	// OK
"demo::lang::Exp::Combined::Automatic::Parse",	// OK
"demo::lang::Exp::Combined::Automatic::Eval",	// OK

"demo::lang::Exp::Combined::Manual::Load",		// ERROR
"demo::lang::Exp::Combined::Manual::Parse",		// OK
"demo::lang::Exp::Combined::Manual::Eval",		// ERROR

"demo::lang::Exp::Concrete::NoLayout::Eval",	// OK
"demo::lang::Exp::Concrete::WithLayout::Eval"	// OK

"demo::lang::Func::AST",
"demo::lang::Func::Eval0",
"demo::lang::Func::Eval1",
"demo::lang::Func::Eval2",
"demo::lang::Func::Eval3",
"demo::lang::Func::Func",
"demo::lang::Func::Parse",
"demo::lang::Func::Test",

"demo::lang::Lisra::Eval",
"demo::lang::Lisra::Parse",
"demo::lang::Lisra::Pretty",
"demo::lang::Lisra::Runtime",
"demo::lang::Lisra::Syntax",
"demo::lang::Lisra::Test",

"demo::lang::MissGrant::AST",
"demo::lang::MissGrant::CheckController",
"demo::lang::MissGrant::DesugarResetEvents",
"demo::lang::MissGrant::Implode",
"demo::lang::MissGrant::MissGrant",
"demo::lang::MissGrant::Outline",
"demo::lang::MissGrant::ParallelMerge",
"demo::lang::MissGrant::Parse",
"demo::lang::MissGrant::Step",
"demo::lang::MissGrant::ToDot",
"demo::lang::MissGrant::ToMethods",
"demo::lang::MissGrant::ToObjects",
"demo::lang::MissGrant::ToRelation",
"demo::lang::MissGrant::ToSwitch",


"demo::lang::Pico::Compile",
"demo::lang::Pico::ControlFlow",
"demo::lang::Pico::Eval",
"demo::lang::Pico::Typecheck",
"demo::lang::Pico::Uninit",
"demo::lang::Pico::UseDef",
"demo::lang::Pico::Visualize",


"demo::lang::turing::l1::ast::Load",
"demo::lang::turing::l1::ast::Turing",
"demo::lang::turing::l1::cst::Parse",
"demo::lang::turing::l1::cst::Syntax",
"demo::lang::turing::l1::interpreter::Interpreter",
"demo::lang::turing::l2::ast::Load",
"demo::lang::turing::l2::ast::Turing",
"demo::lang::turing::l2::check::Check",
"demo::lang::turing::l2::cst::Parse",
"demo::lang::turing::l2::cst::Syntax",
"demo::lang::turing::l2::desugar::Desugar",
"demo::lang::turing::l2::format::Format",

"demo::vis::Higher",
"demo::vis::Logo",
"demo::vis::VisADT",


// COMPILER

// RVM
"experiments::Compiler::RVM::AST",							// OK
"experiments::Compiler::RVM::Syntax",						// OK
"experiments::Compiler::RVM::Load",							// OK
"experiments::Compiler::RVM::Parse"							// OK
*/
/*
// MuRascal
"experiments::Compiler::muRascal::AST",						// OK
"experiments::Compiler::muRascal::Parse",					// ERROR was OK
"experiments::Compiler::muRascal::Load",					// OK
"experiments::Compiler::muRascal::Implode",					// ERROR
"experiments::Compiler::muRascal::MuAllMuOr",				// ERROR
"experiments::Compiler::muRascal::Syntax",					// OK
"experiments::Compiler::muRascal::Run"						// OK
*/
/*
// muRascal2RVM
"experiments::Compiler::muRascal2RVM::PeepHole",			// was OK
"experiments::Compiler::muRascal2RVM::RascalReifiedTypes",	// OK
"experiments::Compiler::muRascal2RVM::ReifiedTypes",		// ERROR
"experiments::Compiler::muRascal2RVM::StackSize",			// OK
"experiments::Compiler::muRascal2RVM::ToplevelType",		// OK
"experiments::Compiler::muRascal2RVM::mu2rvm"				// ERROR

// Rascal2muRascal
"experiments::Compiler::Rascal2muRascal::RascalExpression",	// ERROR
"experiments::Compiler::Rascal2muRascal::RascalModule",		// ERROR
"experiments::Compiler::Rascal2muRascal::RascalPattern",	// ERROR
"experiments::Compiler::Rascal2muRascal::RascalStatement",	// ERROR
"experiments::Compiler::Rascal2muRascal::RascalType",		// ERROR
"experiments::Compiler::Rascal2muRascal::TmpAndLabel",		// OK
"experiments::Compiler::Rascal2muRascal::TypeReifier",		// ERROR
"experiments::Compiler::Rascal2muRascal::TypeUtils",		// ERROR

// Typechecker
"lang::rascal::types::AbstractKind",						// ERROR was OK
"lang::rascal::types::AbstractName",						// ERROR
"lang::rascal::types::AbstractType",						// ERROR
"lang::rascal::types::CheckTypes",							// ERROR
"lang::rascal::types::ConvertType",							// ERROR
"lang::rascal::types::TestChecker",							// ERROR
"lang::rascal::types::TypeExceptions",						// ERROR
"lang::rascal::types::TypeInstantiation",					// ERROR
"lang::rascal::types::TypeSignature",						// ERROR

// Parser generator
"lang::rascal::grammar::analyze::DefUse",					// was OK
"lang::rascal::grammar::analyze::Dependency",				// ERROR

"lang::rascal::grammar::definition::Attributes",			// ERROR
"lang::rascal::grammar::definition::Characters",			// ERROR
"lang::rascal::grammar::definition::Keywords",				// ERROR
"lang::rascal::grammar::definition::Layout",				// ERROR
"lang::rascal::grammar::definition::Literals",				// ERROR
"lang::rascal::grammar::definition::Modules",				// ERROR
"lang::rascal::grammar::definition::Names",					// OK
"lang::rascal::grammar::definition::Parameters",			// OK
"lang::rascal::grammar::definition::Priorities",			// ERROR
"lang::rascal::grammar::definition::Productions",			// ERROR
"lang::rascal::grammar::definition::Regular",				// ERROR
"lang::rascal::grammar::definition::Symbols",				// ERROR

"lang::rascal::grammar::Bootstrap",							// ERROR
"lang::rascal::grammar::ConcreteSyntax",					// ERROR
"lang::rascal::grammar::Lookahead",							// ERROR
"lang::rascal::grammar::ParserGenerator",					// ERROR
"lang::rascal::grammar::SyntaxTreeGenerator",				// ERROR
*/


];

list[str] eclipse_libs =
[
/*
// Eclipse library
"util::Clipboard",						// OK				
"util::ContentCompletion",				// ERROR
"util::Editors",						// ERROR
"util::FastPrint",						// OK
"util::HtmlDisplay",					// OK
"util::IDE",							// ERROR
"util::NonRascalMenuContributionItem",	// ERROR
"util::ParseTreeUI",					// ERROR
"util::Prompt",							// OK
"util::ResourceMarkers",				// OK
"util::Resources",						// ERROR
"util::SyntaxHighligthingTemplates",	// ERROR
"util::ValueUI"							// ERROR
*/
];

value main(list[value] args){
  crashes = [];
  for(lib <- libs){
    println("**** Compiling <lib> ****");
    try {
	    //compile("module TMP  extend Exception; extend <lib>;", recompile=true);
	    
    	compile(|project://rascal/src/org/rascalmpl/library/| + (replaceAll(lib, "::", "/") + ".rsc"), recompile=true);
    } catch e: {
      crashes += <lib, "<e>">;
    }
  }
  
   for(lib <- eclipse_libs){
    println("**** Compiling <lib> ****");
    try {
	    //compile("module TMP  extend Exception; extend <lib>;", recompile=true);
	    
    	compile(|project://rascal-eclipse/src/org/rascalmpl/eclipse/library/| + (replaceAll(lib, "::", "/") + ".rsc"), recompile=true);
    } catch e: {
      crashes += <lib, "<e>">;
    }
  }
  

  if(size(crashes) > 0){
    println("\nERRORS:\n");
     for(<lib, msg> <- crashes){
       println("<lib>: <msg>");
    }
  }
  return true;
}