module experiments::Compiler::RVM::Inspector

import IO;
import ValueIO;
import String;
import List;
import Map;
import Set;
import Type;
import Message;
import util::FileSystem;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Compile;

/*
 *  Viewer for compiled RVM programs
 *
 * TODO: zillions of options could be added
 * - show a foldable vizialization.
 */
 
void inspect(loc srcLoc,                // location of Rascal source file
          loc bindir = |home:///bin|,   // location where binaries are stored
          list[str] select = [],     	// select function names to be shown
          int line = -1,				// select line of function to be shown
          bool listing = false          // show instruction listing
          ){
    rvmLoc = RVMProgramLocation(srcLoc, bindir);
    RVMProgram p;
    try {
    	if(rvmLoc == bindir + "/src/org/rascalmpl/library/experiments/Compiler/muRascal2RVM/LibraryGamma.rvm"){
    		decls = readTextValueFile(#list[Declaration], rvmLoc);
    		p = rvm("LibraryGamma",
		      {},
			  [],
              (), 
              (),
              (d.qname : d | d <- decls),
              [], 
              (), 
              [],
              rvmLoc);
    	} else {
        	p = readTextValueFile(#RVMProgram, rvmLoc);
        }	
        
        println("RVM PROGRAM: <p.name>");
         
        if(line >= 0){
         	listDecls(p, select, line, listing);
         	return;
         }	
         
        if(size(select) > 0){
            listDecls(p, select, line, listing);
            printOverloaded(p.overloaded_functions, select, line);
            printResolver(p.resolver, select, line);
            return;
        }
       
        printMessages(p.messages);
       
        printImports(p.imports);
       
        printSymbolDefinitions(p.symbol_definitions);
       
        println("DECLARATIONS:");
        for(dname <- p.declarations){
            printDecl(p.declarations[dname]);
        }
        
        init = p.initialization;
        if(size(init) > 0){
            println("INITIALIZATION:");
            iprintln(init);
        }
        
        printResolver(p.resolver, select, line);
        
        printOverloaded(p.overloaded_functions, select, line);
        
        return;
    } catch e: {
        println("Reading: <rvmLoc>: <e>");
    }
}

void printSymbolDefinitions(map[Symbol, Production] sym_defs){
	if(size(sym_defs) > 0){
    	println("SYMBOL DEFINITIONS:");
		for(sym <- sym_defs){
        	if(choice(s, choices) := sym_defs[sym]){
            	println("\t<s>:");
                for(c <- choices){
                	println("\t\t<c>");
                }
            } else {
            	println("\t<sym>: <sym_defs[sym]>");
            }
		}
	}
}

void printMessages(set[Message] messages){
	if(size(messages) > 0){
    	println("MESSAGES:");
        for(msg <- messages){
        	println("\t<msg>");
        }
    }
}

void printImports(list[loc] imports){
	if(size(imports)> 0){
    	println("IMPORTS:");
       	for(imp <- imports){
        	println("\t<imp>");
        }
    }
}

void printResolver(map[str, int] resolver, list[str] select, int line){
	if(size(resolver) > 0){
		println("RESOLVER:");
		for(f <- resolver){
			if(size(select) == 0 || matchesSelection(f, select, atStart=false)){
					println("\t<f>: <resolver[f]>");
			}
		}
    }
}

void printOverloaded(lrel[str,list[str],list[str]] overloaded, list[str] select, int line){
	if(size(overloaded) > 0){
    	println("OVERLOADED FUNCTIONS:");
        for(int i <- index(overloaded)){
        	t = overloaded[i];
        	if(size(select) == 0 || any(/str s :=  t, matchesSelection(s, select, atStart=false)) || containsLine(p.declarations[dname].src, line)){
            	println("\t<right("<i>", 6)>: <t>");
            }
        }
	}
}

void printDecl(Declaration d){
    if(d is FUNCTION){
        println("\tFUNCTION <d.uqname>, <d.qname>, <d.ftype>");
    } else {
        println("\tCOROUTINE <d.uqname>, <d.qname>");
    }
    println("\t\tnformals=<d.nformals>, nlocals=<d.nlocals>, maxStack=<d.maxStack>, instructions=<size(d.instructions)>");
    println("\t\tscopeIn=<d.scopeIn>,\n\t\tsrc=<d.src>");
    if(size(d.exceptions) > 0){
    	for(<str from, str to, Symbol \type, str target> <- d.exceptions){
    		println("\t\texception: from=<from>, to=<to>, type=<\type>, target=<target>");
    	}
    }
}

bool matchesSelection(str info, list[str] select, bool atStart = false){
	select = [toLowerCase(sel) | sel <- select];
	return any(sel <- select, int i := findFirst(toLowerCase(info), sel), atStart ? i == 0 : i >= 0);
}

bool containsLine(loc src, int line) =
	line >= 0 && line >= src.begin.line && line <= src.end.line;

void listDecls(RVMProgram p, list[str] select, int line, bool listing){
    select = [toLowerCase(sel) | sel <- select];
    for(dname <- p.declarations){
        uqname = p.declarations[dname].uqname;
        if(matchesSelection(uqname, select, atStart = true) || containsLine(p.declarations[dname].src, line)){
        	printDecl(p.declarations[dname]);
            if(listing){
 				for(ins <- p.declarations[dname].instructions){
					println("\t\t<ins>");                
				}
            }
        }
    }
}

void statistics(loc root = |project://rascal/src/|,
                loc bindir = |home:///bin|
                ){
    allFiles = find(root, "rsc");
    
    nfunctions = 0;
    ncoroutines = 0;
    ninstructions = 0;
  
    messages = {};
    missing = {};
    nsuccess = 0;
    for(f <- allFiles){
        rvmLoc = RVMProgramLocation(f, bindir);
        try {
            p = readTextValueFile(#RVMProgram, rvmLoc);
            if(size(p.messages) == 0 || all(msg <- p.messages, msg is warning)){
                nsuccess += 1;
            }
            messages += p.messages;
           
            for(dname <- p.declarations){
                decl = p.declarations[dname];
                if(decl is FUNCTION)
                    nfunctions += 1;
                else {
                    ncoroutines += 1;
                }
                ninstructions += size(decl.instructions);
            }
        } catch: 
            missing += f;
    }
    
    nfatal = 0;
    nerrors = 0;
    nwarnings = 0;
    
    fatal = {};
    
    for(msg <- messages){
        if(msg is error){
            if(findFirst(msg.msg, "Fatal compilation error") >= 0){
                fatal += msg.at;
            } else {
                nerrors += 1;
            }
         } else {
            nwarnings += 1;
         }
    }
    
    println("files:        <size(allFiles)>
            'functions:    <nfunctions>
            'coroutines:   <ncoroutines>
            'instructions: <ninstructions>
            'errors:       <nerrors>
            'warnings:     <nwarnings>
            'missing:      <size(missing)>, <missing>
            'success:      <nsuccess>
            'fatal:        <size(fatal)>, <fatal>
            '");
}

set[loc] getFunctionLocations(
						   loc srcLoc,                  // location of Rascal source file
   loc bindir = |home:///bin|   // location where binaries are stored
){
   rvmLoc = RVMProgramLocation(srcLoc, bindir);
   try {
        p = readTextValueFile(#RVMProgram, rvmLoc);
        
        return {p.declarations[dname].src | dname <- p.declarations};
   } catch e: {
        println("Reading: <rvmLoc>: <e>");
   }
} 