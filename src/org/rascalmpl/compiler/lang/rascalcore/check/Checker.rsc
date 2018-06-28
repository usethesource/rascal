@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}

module lang::rascalcore::check::Checker
              
/*
 * TODO:
 * - Integrate with parser generator
 * - Fix reference in grammar rules (it seems that production label get lost somewhere and reference cannot be found)
 * - Support for reified types
 *
 * Potential additions:
 * - Unused imports/extends
 * - Unused private functions
 * - Non-void functions have a return along every control path
 * - Unreachable code
 */
 
import IO;
import ValueIO;
import String;
import Map;
import util::Benchmark;

import lang::rascal::\syntax::Rascal;
   
extend analysis::typepal::TypePal;

extend lang::rascalcore::check::ADTSummary;
extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::Declaration;
extend lang::rascalcore::check::Expression;
extend lang::rascalcore::check::Import;
extend lang::rascalcore::check::Operators;
extend lang::rascalcore::check::Pattern;
extend lang::rascalcore::check::Statement;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::TypePalConfig;

import lang::rascalcore::grammar::ParserGenerator;
import lang::rascalcore::grammar::definition::Symbols;
import lang::rascalcore::grammar::definition::Characters;
import lang::rascalcore::grammar::definition::Literals;

import Set;
import Relation;
import util::Reflective;
import util::FileSystem;
import analysis::graphs::Graph;

start syntax Modules
    = Module+ modules;
    
//str parserPackage = "org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers";
//str parserPackage = "org.rascalmpl.core.java.parser.object";

Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int i <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(map[str, Tree] namedTrees, Collector c){
    for(tree <- range(namedTrees)){
        c.enterScope(tree);      
            // Tree type, field "top"
            TreeType = aadt("Tree", [], dataSyntax());
            treeScope = mkTree(1);
            c.define("Tree", dataId(), treeScope, defType(TreeType));
            c.enterScope(treeScope);
                c.define("top", fieldId(), mkTree(2), defType(TreeType));
            c.leaveScope(treeScope);
               
            // Reified type
            if(c.getConfig().classicReifier){
                //data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
                typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
                SymbolType = aadt("Symbol", [], dataSyntax());
                ProductionType = aadt("Production", [], dataSyntax());
                symbolField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
                definitionsField = amap(SymbolType, ProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, ProductionType)>;
                c.define("type", constructorId(), mkTree(3), defType(acons(typeType, /*"type",*/ [symbolField, definitionsField], [], label="type")));
                // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
            } else {
                //data type[&T] = type(AType symbol, map[AType,AProduction] definitions);
                typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
                SymbolType = aadt("AType", [], dataSyntax());
                AProductionType = aadt("AProduction", [], dataSyntax());
                atypeField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
                definitionsField = amap(SymbolType, AProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, AProductionType)>;
                c.define("type", constructorId(), mkTree(3), defType(acons(typeType, /*"type",*/ [atypeField, definitionsField], [], label="type")));
                // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
            }
        c.leaveScope(tree);
    }
}

// Enhance TModel before running Solver
TModel rascalPreSolver(map[str,Tree] namedTrees, TModel m){
    // add transitive edges for extend
    extendPlus = {<from, to> | <loc from, extendPath(), loc to> <- m.paths}+;
    m.paths += { <from, extendPath(), to> | <loc from, loc to> <- extendPlus};
    m.paths += { <c, importPath(), a> | < loc c, importPath(), loc b> <- m.paths,  <b , extendPath(), loc a> <- m.paths};
    //println("rascalPreValidation");
    //iprintln(m.paths);
    return m;
}

void rascalPostSolver(map[str,Tree] namedTrees, Solver s){
    //messages = s.getTModel().messages;
   // if(isEmpty(messages)){
             ;//<tm, adtSummaries> = getADTSummaries(getLoc(pt), tm, s);
             //g = getGrammar(getLoc(pt), s);
            
 //           pname = "DefaultParser";
 //           if(Module m := pt) { 
 //               moduleName = "<m.header.name>";
 //               pname = parserName(moduleName);
 //           }
 //
 //           <msgs, parserClass> = newGenerate(parserPackage, pname, g); 
 //           tm.messages += msgs;
 //           msgs = saveParser(pname, parserClass, |project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers|);
 //           tm.messages += msgs;
  // }

}

// ----  Examples & Tests --------------------------------

public PathConfig getDefaultPathConfig() {
    return pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|
               ]
               );
}

data ProfileData = profile(str file = "unknown", int collector = 0, int solver = 0, int save = 0);
 
void report(ProfileData pd){
    text = "<pd.collector? ? "collector: <pd.collector> ms;" : ""> <pd.solver? ? "solver: <pd.solver> ms;" : ""> <pd.save? ? "save: <pd.save> ms;" : ""> total: <pd.collector + pd.solver + pd.save> ms";
    if(pd.file != "unknown") text += " (<pd.file>)";
    println(text);
}

TModel rascalTModelForTestModules(Tree pt, bool debug=false){
    ms = getInlineImportAndExtendGraph(pt, getDefaultPathConfig());
   
    if(start[Modules] mds := pt){
        return rascalTModelComponent( (unescape("<md.header.name>") : md | md <- mds.top.modules ), ms, inline=true);
    } else if(Modules mds := pt){
        return rascalTModelComponent( (unescape("<md.header.name>") : md | md <- mds.modules ), ms, inline=true)[1];
    } else
        throw "Cannot handle Modules";
}

TModel rascalTModelForName(str moduleName, PathConfig pcfg, bool debug=true){
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        mloc = getModuleLocation(moduleName, pcfg);
        return rascalTModelForLoc(mloc, pcfg, debug=debug)[1];
    } catch value e: {
        return tmodel()[messages = [ error("During validation: <e>", mloc) ]];
    }
}

int M = 1000000;

tuple[ProfileData, TModel] rascalTModelForLoc(loc mloc, PathConfig pcfg, bool debug=false, bool verbose = false){     
    bool forceCompilationTopModule = false; /***** set to true during development of type checker *****/
    try {
        beginTime = cpuTime();   
        topModuleName = getModuleName(mloc, pcfg);
        
        if(!forceCompilationTopModule){
            <valid, tm> = getIfValid(topModuleName, pcfg);
            if(valid) {
                stime = (cpuTime() - beginTime)/1000000;
                prof = profile(file=topModuleName,solver=stime);
                if(verbose){
                    println("*** reusing up-to-date TModel of <topModuleName>");
                    report(prof);
                }
                return <prof, tm>;
            }
        }
        
        before = cpuTime();
        ms = getImportAndExtendGraph(topModuleName, pcfg);
        if(forceCompilationTopModule){
            ms.valid -= {topModuleName};
            ms.tmodels = delete(ms.tmodels, topModuleName);
        }
        graphTime = cpuTime() - before;
       
        map[str, ProfileData] profs = ();
        imports_and_extends = ms.strPaths<0,2>;
        
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        module2component = (m : c | c <- components, m <- c);
        
        ordered = [];
        
        if(isEmpty(sorted)){
            ordered = [topModuleName];
            module2component[topModuleName] = {topModuleName};
        } else {
            ordered = reverse(sorted);
        }
        
        //println("imports_and_extends"); iprintln(imports_and_extends);
        //println("ordered modules: <ordered>");
        //println("valid: <ms.valid>");
        //println("cycles: <{c | c <- components, size(c) > 1}>");
        map[str, loc] moduleScopes = ();
        mi = 0;
        nmodules = size(ordered);
        while(mi < nmodules){
            component = module2component[ordered[mi]];
            for(m <- component){
                mi += 1;
                if(!ms.tmodels[m]?){
                    if(m in ms.valid){
                        <found, tplLoc> = getDerivedReadLoc(m, "tpl", pcfg);
                        if(found){   
                            println("*** reading <m> from <tplLoc>");       
                            ms.tmodels[m] = readBinaryValueFile(#TModel, tplLoc);
                        }
                    } else {
                        mloc = getModuleLocation(m, pcfg);            
                        println("*** parsing <m> from <mloc>");
                        pt = parseModuleWithSpaces(mloc).top;
                        ms.modules[m] = pt;
                    }
                }
            }
            if(!all(m <- component, ms.tmodels[m]?, m in ms.valid)){
                <prof, tm> = rascalTModelComponent((m: ms.modules[m] |  m <- component), ms, pcfg = pcfg, debug=debug);
                profs[intercalate("/", toList(component))] = prof;
                moduleScopes += getModuleScopes(tm);
                for(m <- component){
                    imports =  { imp | <m, importPath(), imp> <- ms.strPaths };
                    extends = { ext | <m, extendPath(), ext > <- ms.strPaths };
                    ms.tmodels[m] = saveModule(m, imports, extends, moduleScopes, pcfg, tm);
                    ms.modules = delete(ms.modules, m);
                }
            }
        }
       
        tcollector = 0; tsolver = 0; tsave = 0;
        for(m <- profs){
            p = profs[m];
            tcollector += p.collector;
            tsolver += p.solver;
            tsave += p.save;
            report(p);
        }
        
        println("<topModuleName>, import graph <graphTime/M> ms");
        println("<topModuleName> <tcollector+tsolver> ms [ collector: <tcollector> ms; solver: <tsolver> ms; save: <tsave> ms ]");
        println("<topModuleName>, measured total time: <(cpuTime() - beginTime)/M> ms");
      
        return <profile(), ms.tmodels[topModuleName]>;
    } catch ParseError(loc src): {
        return <profile(), tmodel()[messages = [ error("Parse error", src)  ]]>;
    } catch Message msg: {
     return <profile(), tmodel()[messages = [ error("During validation: <msg>", msg.at) ]]>;
    } catch value e: {
        return <profile(), tmodel()[messages = [ error("During validation: <e>", mloc) ]]>;
    }    
}

void loadImportsAndExtends(str moduleName, ModuleStructure ms, Collector c, set[str] seen){
    if(moduleName in seen) return;
    
    rel[str,str] contains = ms.strPaths<0,2>;
    //println("<moduleName>: contains: <contains>");
    for(imp <- contains[moduleName]){
        if(ms.tmodels[imp]?){
            //println("+++ adding TModel <imp>");
            c.addTModel(ms.tmodels[imp]);
        } else {
            seen += imp;
            loadImportsAndExtends(imp, ms, c, seen);
            collect(ms.modules[imp], c);
            //println("+++ collecting  <imp>");
        }
    }
}

tuple[ProfileData, TModel] rascalTModelComponent(map[str, Tree] namedTrees, ModuleStructure ms, PathConfig pcfg = getDefaultPathConfig(), bool debug=false, bool verbose=false, bool inline=false){
    modelName = intercalate(" + ", toList(domain(namedTrees)));
    
    //if(verbose) 
        //println("\<\<\< checking <modelName>");
    c = newCollector(modelName, namedTrees, config=rascalTypePalConfig(classicReifier=true), debug=debug, verbose=verbose);
    c.push(patternContainer, "toplevel");
    
    rascalPreCollectInitialization(namedTrees, c);
    startTime = cpuTime();
     
    for(nm <- namedTrees){
        loadImportsAndExtends(nm, ms, c, {});
    }
    startTime = cpuTime();
    for(nm <- namedTrees){
        collect(namedTrees[nm], c);
    }
    tm = c.run();
    collectTime = cpuTime() - startTime; 
    startTime = cpuTime(); 
    
    tm.paths =  ms.paths;
 //   tm = rascalPreSolver(pt, tm);
    
    s = newSolver(namedTrees, tm);
    tm = s.run();
    
    solveTime = cpuTime() - startTime;
    
    ProfileData prof = profile(file=modelName);
    
    if(!inline){
        prof.collector = collectTime/1000000;
        prof.solver = solveTime/1000000;
    }
    //if(verbose) println("\>\>\> checking <modelName> complete");
    return <prof, tm>;
}

// name of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

ModuleMessages check(str moduleName, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <moduleName>"); iprintln(pcfg1);
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        tm = rascalTModelForName(moduleName, pcfg);
        mloc = getModuleLocation(moduleName, pcfg);
        return program(mloc, toSet(tm.messages));
    } catch value e: {
        return program(mloc, {error("During validation: <e>", mloc)});
    }
}

ModuleMessages check(loc file, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <file>"); iprintln(pcfg1);
    <prof, tm> = rascalTModelForLoc(file, pcfg);
    report(prof);
    return program(file, toSet(tm.messages));
}

list[ModuleMessages] check(list[str] moduleNames, PathConfig pcfg){
    return [ check(moduleName, pcfg) | moduleName <- moduleNames ];
}

list[ModuleMessages] check(list[loc] files, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <files>"); iprintln(pcfg1);
    e = v = s = 0;
    mms = [];
    pds = [];
    for(file <- files){
        <pd, tm> = rascalTModelForLoc(file, pcfg);
        report(pd);
        pds += pd;
        mms += program(file, toSet(tm.messages));
        e += pd.collector; v += pd.solver; s += pd.save;
    }
    println("SUMMARY");
    for(pd <- pds) report(pd);
    println("TOTAL");
    report(profile(collector=e, solver=v,save=s));
    return mms;
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg){
     return [ check(moduleLoc, pcfg) | moduleLoc <- find(root, "rsc") ]; 
}

list[Message] validateModules(str moduleName, bool debug=false) {
    return rascalTModelForName(moduleName, getDefaultPathConfig(), debug=debug).messages;
}

void testModules(str names...) {
    if(isEmpty(names)) names = allTests;
    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests/<name>.ttl| | str name <- names], #Modules, rascalTModelForTestModules, verbose=false);
}

list[str] allTests = ["adt", "adtparam", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
                     "imports", "operators", "pat", "scope", "splicepats", "stats"/*,"syntax1", "syntax2", "syntax3"*/];