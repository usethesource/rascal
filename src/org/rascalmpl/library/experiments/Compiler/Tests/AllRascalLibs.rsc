@bootstrapParser
module experiments::Compiler::Tests::AllRascalLibs

import Prelude;
import experiments::Compiler::Compile;
import experiments::Compiler::RVM::AST;

import util::FileSystem;
import util::Benchmark;

import Set;

import lang::rascal::\syntax::Rascal;
import Message;

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
"util::Reflective", 					// OK
"util::ShellExec",						// OK
"util::Webserver",						// OK


"Ambiguity",			

"APIGen", 			 	
// "Number"				// DEPRECATED: TC gives errors


"util::LOC",			// #394
				
				
"analysis::formalconcepts::FCA",

"analysis::graphs::Graph",					// OK		
"analysis::graphs::LabeledGraph",			// OK
"analysis::linearprogramming::LinearProgramming",

"analysis::m3::AST",						// OK
"analysis::m3::Core",						// OK
"analysis::m3::Registry",

"analysis::m3::TypeSymbol",					// OK
"analysis::statistics::Correlation",		// OK
"analysis::statistics::Descriptive",		// OK
"analysis::statistics::Frequency",			// OK
"analysis::statistics::Inference",			// OK
"analysis::statistics::SimpleRegression"	// OK

/*
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
"experiments::Compiler::muRascal::Load",					// ERROR was OK
"experiments::Compiler::muRascal::Implode",					// ERROR
"experiments::Compiler::muRascal::MuAllMuOr",				// ERROR
"experiments::Compiler::muRascal::Syntax",					// OK
"experiments::Compiler::muRascal::Run"						// ERROR was OK
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
"lang::rascal::types::AbstractKind",						// OK
"lang::rascal::types::AbstractName",						// ERROR
"lang::rascal::types::AbstractType",						// ERROR
"lang::rascal::types::CheckTypes",							// ERROR
"lang::rascal::types::ConvertType",							// ERROR
"lang::rascal::types::TestChecker",							// ERROR
"lang::rascal::types::TypeExceptions",						// ERROR
"lang::rascal::types::TypeInstantiation",					// ERROR
"lang::rascal::types::TypeSignature",						// ERROR

// Parser generator
"lang::rascal::grammar::analyze::DefUse",					// OK
"lang::rascal::grammar::analyze::Dependency",				// OK

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
	    
    	rvm_lib = compile(|project://rascal-eclipse/src/org/rascalmpl/eclipse/library/| + (replaceAll(lib, "::", "/") + ".rsc"), recompile=true);
        for(msg <- rvm_lib.messages){
            if(msg is error){
                if(findFirst(msg.msg, "Fatal compilation error") >= 0){
                     crashes += <lib, "<e>">;
                     break;
                }
            }
        }
 
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

int tosec(int t1, int t2) =(t2 - t1)/1000;

set[loc] exclude = { 
};

set[loc] failures = {
 
};


int countErrors(map[loc,int] counts) = size( {msg | msg <- counts, counts[msg] > 0} );

tuple[set[loc],set[loc]] compileAll(loc root = |rascal:///|){
	allFiles = find(root, "rsc") - exclude;
	nfiles = size(allFiles);
	static_error_count = ();
	compiler_errors = {};
	set[Message] all_static_errors = {};
	t1 = realTime();
	i = 0;
	while(!isEmpty(allFiles)){
		<f, allFiles> = takeOneFrom(allFiles);
		i += 1;
		println("**** Compiling <i> of <nfiles> files (static_errors: <countErrors(static_error_count)>, compiler_errors: <size(compiler_errors)>), time sofar <tosec(t1, realTime())> sec. ****");
		try {
			f_rvm = compile(f);
			f_errors = { e | e:error(_,_) <- f_rvm.messages, e.at.path == f.path};
			all_static_errors += f_errors;
            static_error_count[f] = size(f_errors);
            
            if(size(f_errors) > 0){
                for(msg <-f_errors){
                    if(msg is error){
                        if(findFirst(msg.msg, "Fatal compilation error") >= 0){
                            compiler_errors += f;
                        } 
                        println(msg);
                    }
                }
            }
		} catch e: {
			compiler_errors += f;
		}
	}
	
  	nstatic = countErrors(static_error_count);
  	
  	ncompiler = size(compiler_errors);
  	ndone = nfiles - nstatic - ncompiler;
	println("Compiled: <nfiles> files");
	println("Success: <ndone>");
	println("Type checker: <nstatic> files with errors");
	
	nstatic_errors = size(all_static_errors);
	
	println("Type checker: <nstatic_errors> error messages");
	
	println("Compiler errors: <ncompiler> crashes");
	println("Time: <tosec(t1, realTime())> sec.");
	
	writeFile(|rascal:///experiments/Compiler/Tests/static_errors|, 
	   "<for(loc f <- sort([ msg | msg <- static_error_count, static_error_count[msg] > 0])){><f>\n<}>");
	 
	perfile = sort(toList(static_error_count), bool(tuple[loc,int] a, tuple[loc,int] b) {return a[1] > b[1]; });
    writeFile(|rascal:///experiments/Compiler/Tests/static_error_count_per_file|, 
       "<for(tp <- perfile){><tp>\n<}>");
       
	writeFile(|rascal:///experiments/Compiler/Tests/compiler_errors|, 
	   "<for(loc f <- sort(toList(compiler_errors))){><f>\n<}>");
	
	return <domain(static_error_count), compiler_errors>;
}