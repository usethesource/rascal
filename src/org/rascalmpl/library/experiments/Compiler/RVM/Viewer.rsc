module experiments::Compiler::RVM::Viewer

import Prelude;
import ValueIO;
import util::FileSystem;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Compile;

/*
 *  Viewer for compiled RVM programs
 *
 * TODO: zillions of options could be added
 * - show a foldable vizialization.
 */
 
void view(loc srcLoc,                   // location of Rascal source file
          loc bindir = |home:///bin|,   // location where binaries are stored
          list[str] select = [],     	// select unction names to be shown
          bool listing = false          // show instruction listing
          ){

    rvmLoc = RVMProgramLocation(srcLoc, bindir);
    try {
        p = readTextValueFile(#RVMProgram, rvmLoc);
        
        if(size(select) > 0){
            listDecls(p, select, listing);
            return;
        }
        println("RVM PROGRAM: <p.name>");
        msgs = p.messages;
        if(size(msgs) > 0){
            println("MESSAGES:");
            for(msg <- msgs){
                println("\t<msg>");
            }
        }
        imps = p.imports;
        if(size(imps)> 0){
            println("IMPORTS:");
            for(imp <- imps){
                println("\t<imp>");
            }
        }
        sym_defs = p.symbol_definitions;
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
       
        println("DECLARATIONS:");
        for(dname <- p.declarations){
            printDecl(p.declarations[dname]);
        }
        init = p.initialization;
        if(size(init) > 0){
            println("INITIALIZATION:");
            iprintln(init);
        }
        res = p.resolver;
        if(size(res) > 0){
            println("RESOLVER:");
            for(f <- res){
                println("\t<f>: <res[f]>");
            }        
        }
        overloaded = p.overloaded_functions;
        if(size(overloaded) > 0){
          println("OVERLOADED FUNCTIONS:");
            for(t <- overloaded){
                println("\t<t>");
            }
        }
        return;
    } catch e: {
        println("Reading: <rvmLoc>: <e>");
    }
}

void printDecl(Declaration d){
    if(d is FUNCTION){
        println("\tFUNCTION <d.uqname>, <d.qname>, <d.ftype>");
        println("\t\tnformals=<d.nformals>, nlocals=<d.nlocals>, maxStack=<d.maxStack>, instructions=<size(d.instructions)>, exceptions=<size(d.exceptions)>");
        println("\t\tscopeIn=<d.scopeIn>,\n\t\tsrc=<d.src>");
    } else {
        println("\tCOROUTINE <d.uqname>, <d.qname>");
        println("\t\tnformals=<d.nformals>, nlocals=<d.nlocals>, maxStack=<d.maxStack>, instructions=<size(d.instructions)>");
        println("\t\tscopeIn=<d.scopeIn>,\n\t\tsrc=<d.src>");
    }
}

void listDecls(RVMProgram p, list[str] select, bool listing){
    select = [toLowerCase(sel) | sel <- select];
    for(dname <- p.declarations){
        uqname = p.declarations[dname].uqname;
        for(sel <- select){
            if(findFirst(toLowerCase(uqname), sel) == 0){
                printDecl(p.declarations[dname]);
                if(listing){
                    for(ins <- p.declarations[dname].instructions){
                        println("\t\t<ins>");
                    
                    }
                }
            }
        
        }
    }

}

void statistics(loc root = |rascal:///|,
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