module experiments::Compiler::RVM::Inspector

import IO;
import ValueIO;
import String;
import List;
import Map;
import Set;
import Relation;
import Node;
import Type;
import Message;
import util::FileSystem;
import util::Reflective;
import experiments::Compiler::RVM::AST;

import experiments::Compiler::Compile;

import lang::rascal::types::CheckerConfig;
import lang::rascal::types::CheckTypes;
import lang::rascal::types::AbstractName;


/*
 * A mini-query language to query .rvm and .tc files
 */

data Query = none()
           | q(str select)
           | and(str select1, str select2)
           | and(list[str] selects)
           | and(Query query1, Query query2)
           | and(list[Query] queries)
           | or(str select1, str select2)
           | or(list[str] selects)
           | or(list[Query] queries)
           | not(str select)
           | not(Query query)
           ;

Query and(Query query1, Query query2) = and([query1, query2]);
Query and(str select1, str select2) = and([q(select1), q(select2)]);
Query and(list[str] selects) = and([q(select)| select <- selects]);

Query or(Query query1, Query query2) = or([query1, query2]);
Query or(str select1, str select2) = or([q(select1), q(select2)]);
Query or(list[str] selects) = or([q(select)| select <- selects]);

Query not(str select) = not(q(select));

bool evalQuery(value subject, none()) = true;

bool evalQuery(value subject, q(str select)) = containsSelected(subject, select);

bool evalQuery(value subject, and(list[Query] queries)) = all(query <- queries, evalQuery(subject, query));

bool evalQuery(value subject, or(list[Query] queries)) = any(query <- queries, evalQuery(subject, query));

bool evalQuery(value subject, not(Query query)) = !evalQuery(subject, query);

bool containsSelected(value v, str select){
	visit(v){
		case str s:		if(startsWith(s, select)) return true;
		case node n: 	if(startsWith(getName(n), select)) return true;
	};
	return false;
}

bool containsSelected(value v, Query query) = evalQuery(v, query);

void printSelected(value v, Query query){
	if(evalQuery(v, query)){
		println("\t<v>");
	}
}
void printSelected(value v1, value v2, Query query){
	if(evalQuery(v1, query) || evalQuery(v2, query)){
		println("\t<v1>: <v2>");
	}
}

bool hasMatches(set[&T] s, Query query) = !isEmpty(s) && evalQuery(s, query);
bool hasMatches(map[&K,&KV] m, Query query) = !isEmpty(m) && evalQuery(m, query);


/*
 *  Viewer for compiled RVM programs
 *
 * TODO: zillions of options could be added
 * - show a foldable vizialization.
 */
 
void inspect(loc srcLoc,                // location of Rascal source file
          loc bindir = |home:///bin|,   // location where binaries are stored
          Query select = none(),     	// select function names to be shown
          int line = -1,				// select line of function to be shown
          bool listing = false          // show instruction listing
          ){
    rvmLoc = RVMProgramLocation(srcLoc, bindir);
    RVMProgram p;
    try {
    	if(rvmLoc == bindir + "/src/org/rascalmpl/library/experiments/Compiler/muRascal2RVM/Library.rvm.gz"){
    		decls = readBinaryValueFile(#list[Declaration], rvmLoc);
    		p = rvm("Library",
    		  (),
		      {},
			  [],
			  [],
              (), 
              (),
              (d.qname : d | d <- decls),
              [], 
              (), 
              [],
              rvmLoc);
    	} else {
        	p = readBinaryValueFile(#RVMProgram, rvmLoc);
        }	
        
        noSelection = line < 0 && select == none();
        
        if(noSelection){
	        println("RVM PROGRAM: <p.name>");
	        
	        if(p.tags != ()){
	        	println("TAGS: <p.tags>");
	        }
        }
         
        if(line >= 0){
         	listDecls(p, select, line, listing);
         	return;
         }	
         
        if(select != none()){
            listDecls(p, select, line, listing);
            printOverloaded(p.overloaded_functions, select, line);
            printResolver(p.resolver, select, line);
            return;
        }
       
        printMessages(p.messages);
       
        printImports(p.imports);
        
        printExtends(p.extends);
       
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
void printExtends(list[loc] extends){
	if(size(extends)> 0){
    	println("EXTENDS:");
       	for(ext <- extends){
        	println("\t<ext>");
        }
    }
}

void printResolver(map[str, int] resolver, Query select, int line){
	if(size(resolver) > 0){
		println("RESOLVER:");
		for(f <- resolver){
			if(matchesSelection(f, select, atStart=false)){
					println("\t<f>: <resolver[f]>");
			}
		}
    }
}

void printOverloaded(lrel[str name, Symbol funType, str scope, list[str] ofunctions, list[str] oconstructors] overloaded, Query select, int line){
	if(size(overloaded) > 0){
    	println("OVERLOADED FUNCTIONS:");
        for(int i <- index(overloaded)){
        	t = overloaded[i];
        	if(select == none() || any(/str s :=  t, matchesSelection(s, select, atStart=false))){
            	println("\t<right("<i>", 6)>: <t>");
            }
        }
	}
}

void printDecl(Declaration d){
    if(d is FUNCTION){
        println("\tFUNCTION <d.uqname>, <d.qname>, <d.ftype>");
        print("\t\tisPublic=<d.isPublic>, isDefault=<d.isDefault>, ");
    } else {
        println("\tCOROUTINE <d.uqname>, <d.qname>");
        print("\t\t");
    }
    println("nformals=<d.nformals>, nlocals=<d.nlocals>, maxStack=<d.maxStack>, instructions=<size(d.instructions)>, scopeIn=<d.scopeIn>");
    println("\t\tsrc=<d.src>");
    if(size(d.exceptions) > 0){
    	for(<str from, str to, Symbol \type, str target, int fromSP> <- d.exceptions){
    		println("\t\ttry: from=<from>, to=<to>, type=<\type>, target=<target>, fromSP=<fromSP>");
    	}
    }
}

bool matchesSelection(str info, Query select, bool atStart = false){
     return evalQuery(info, select);
	//return any(sel <- select, int i := findFirst(toLowerCase(info), sel), atStart ? i == 0 : i >= 0);
}

bool containsLine(loc src, int line) =
	line >= 0 && line >= src.begin.line && line <= src.end.line;

void listDecls(RVMProgram p, Query select, int line, bool listing){
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



void config(loc src,                // location of Rascal source file
            loc bindir = |home:///bin|,
            Query select = none()){
            
           Configuration c = readBinaryValueFile(#Configuration, cachedConfig(src,bindir));
           
           if(hasMatches(c.messages, select)) { println("messages:"); for(msg <- c.messages) println("\t<msg>"); }
           
           if(hasMatches(c.locationTypes, select)) { println("locationTypes:"); for(l <- c.locationTypes) printSelected(l, c.locationTypes[l], select); }
		   if(containsSelected(c.expectedReturnType, select)) { println("expectedReturnType:"); printSelected(c.expectedReturnType, select); }
           if(hasMatches(c.labelEnv, select)) { println("labelEnv:"); for(nm <- c.labelEnv) printSelected(nm, c.labelEnv[nm], select); }
           
           if(hasMatches(c.fcvEnv, select)) { println("fcvEnv:"); for(nm <- c.fcvEnv)printSelected(prettyPrintName(nm), c.fcvEnv[nm], select); }
           
           if(hasMatches(c.typeEnv, select)) { println("typeEnv:"); for(nm <- c.typeEnv)printSelected(nm, c.typeEnv[nm], select); }
           if(hasMatches(c.modEnv, select)) { println("modEnv:"); for(nm <- c.modEnv)printSelected(nm, c.modEnv[nm], select); }
           if(hasMatches(c.annotationEnv, select)) { println("annotationEnv:"); for(nm <- c.annotationEnv)printSelected(nm, c.annotationEnv[nm], select); }
           if(hasMatches(c.tagEnv, select)) { println("tagEnv:"); for(nm <- c.tagEnv)printSelected(nm, c.tagEnv[nm], select); }
           if(hasMatches(c.visibilities, select)) { println("visibilities:"); for(uid <- c.visibilities) printSelected(uid, c.visibilities[uid], select); }
           if(hasMatches(c.store, select)) { println("store:"); for(uid <- sort(domain(c.store))) printSelected(uid, c.store[uid], select); }
           if(hasMatches(c.grammar, select)) { println("grammar:"); for(uid <- sort(domain(c.grammar))) printSelected(uid, c.grammar[uid], select); }
           if(hasMatches(c.starts, select)) { println("starts:"); printSelected(c.starts, select); }
           if(hasMatches(c.adtFields, select)) { println("adtFields:"); for(is <- sort(domain(c.adtFields))) printSelected(is, c.adtFields[is], select); }
           if(hasMatches(c.nonterminalFields, select)) { println("nonterminalFields:"); for(is <- sort(domain(c.nonterminalFields))) printSelected(is, c.nonterminalFields[is], select); }
           if(hasMatches(c.functionModifiers, select)) { println("functionModifiers:"); for(uid <- sort(domain(c.functionModifiers))) printSelected(uid, c.functionModifiers[uid], select); }
           if(hasMatches(c.definitions, select)) { println("definitions:"); for(uid <- sort(domain(c.definitions))) printSelected(uid, c.definitions[uid], select); }
           if(hasMatches(c.uses, select)) { println("uses:"); for(uid <- sort(domain(c.uses))) printSelected(uid, c.uses[uid], select); }
           if(hasMatches(c.narrowedUses, select)) { println("narrowedUses:"); for(uid <- sort(domain(c.narrowedUses))) printSelected(uid, c.narrowedUses[uid], select); }
           if(hasMatches(c.usedIn, select)) { println("usedIn:"); for(uid <- sort(domain(c.usedIn))) printSelected(uid, c.usedIn[uid], select); }
           if(hasMatches(c.adtConstructors, select)) { println("adtConstructors:"); for(uid <- sort(domain(c.adtConstructors))) printSelected(uid, c.adtConstructors[uid], select); }
           if(hasMatches(c.nonterminalConstructors, select)) { println("nonterminalConstructors:"); for(uid <- sort(domain(c.nonterminalConstructors))) printSelected(uid,c.nonterminalConstructors[uid], select); }
           if(hasMatches(c.keywordDefaults, select)) { println("keywordDefaults:"); for(uid <- sort(domain(c.keywordDefaults))) printSelected(uid, c.keywordDefaults[uid], select); }
           if(hasMatches(c.dataKeywordDefaults, select)) { println("dataKeywordDefaults:"); for(uid <- sort(domain(c.dataKeywordDefaults))) printSelected(uid, c.dataKeywordDefaults[uid], select); }
           if(hasMatches(c.tvarBounds, select)) { println("tvarBounds:"); for(uid <- sort(domain(c.tvarBounds))) printSelected(uid, c.tvarBounds[uid], select); }
           if(hasMatches(c.moduleInfo, select)) { println("moduleInfo:"); for(uid <- sort(domain(c.moduleInfo))) printSelected(uid, c.moduleInfo[uid], select); }
           if(hasMatches(c.globalAdtMap, select)) { println("globalAdtMap:"); for(uid <- sort(domain(c.globalAdtMap))) printSelected(prettyPrintName(uid), c.globalAdtMap[uid], select); }
           
           if(hasMatches(c.globalSortMap, select)) { println("globalSortMap:"); for(uid <- sort(domain(c.globalSortMap))) printSelected(prettyPrintName(uid), c.globalSortMap[uid], select); }
           
           if(hasMatches(c.deferredSignatures, select)) { println("deferredSignatures:"); for(uid <- sort(domain(c.deferredSignatures))) printSelected(uid, c.deferredSignatures[uid], select); }
           if(hasMatches(c.unimportedNames, select)) { println("unimportedNames:"); for(uid <- sort(c.unimportedNames)) printSelected(uid, select); }
           
}