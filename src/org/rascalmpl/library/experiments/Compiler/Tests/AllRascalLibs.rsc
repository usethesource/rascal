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


];

list[str] eclipse_libs =
[

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
                     crashes += <lib, "<msg>">;
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

tuple[set[loc],set[loc]] compileAll(list[loc] roots = [|project://rascal/|, |project://rascal/src/org/rascalmpl/tutor|]){
	allFiles = [ *find(root, "rsc") | root <- roots];

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