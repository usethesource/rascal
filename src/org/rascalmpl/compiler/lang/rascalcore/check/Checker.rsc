@license{
Copyright (c) 2017, Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::Checker
              
/*
 * TODO:
 * - Integrate with parser generator
 * - Rechecking of circular structures crashes sometimes
 * - Fix reference in grammar rules (it seems that production label get lost somewhere and reference cannot be found)
 * - Support for reified types
 *
 * Potential additions/improvements
 * - Unused imports/extends
 * - Unused private functions
 * - Non-void functions have a return along every control path
 * - Unreachable code
 * - Warn for closures inside loops
 * - Reduce rechecking by comparing old and new tpl file
 */
 
import IO;
import ValueIO;
import String;
import List;
import Map;
import util::Benchmark;
import Message;

import lang::rascal::\syntax::Rascal;
   
extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::Declaration;
extend lang::rascalcore::check::Expression;
extend lang::rascalcore::check::GetGrammar;
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
import lang::rascalcore::grammar::definition::Grammar;

import Set;
import Relation;
import util::Reflective;
import util::FileSystem;
import analysis::graphs::Graph;

//start syntax Modules
//    = Module+ modules;
    
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
    //tm = s.getTModel();
    //messages = tm.messages;
    if(!s.reportedErrors()){
        for(mname <- namedTrees){
            pt = namedTrees[mname];
            g = getGrammar(getLoc(pt), s);
            if(!isEmpty(g.rules)){ 
                pname = "DefaultParser";
                if(Module m := pt) { 
                        moduleName = "<m.header.name>";
                        pname = parserName(moduleName);
                }
                <msgs, parserClass> = newGenerate(parserPackage, pname, g); 
                s.addMessages(msgs);
                //TODO: generates too long file names
                //msgs = saveParser(pname, parserClass, |project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers|, s.getConfig().verbose);
            s.addMessages(msgs);
            }
        }
   }
}

// ----  Various check functions  ---------------------------------------------

public PathConfig getDefaultPathConfig() {
    return pathConfig(   
        srcs = [|project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://typepal/src|,
                |project://rascal/src/org/rascalmpl/library|,
                |project://typepal-examples/src|
               ]
               );
}

// Profiling

data ProfileData = profile(str file = "unknown", int collector = 0, int solver = 0, int save = 0);
 
void report(ProfileData pd){
    text = "<pd.collector? ? "collector: <pd.collector> ms;" : ""> <pd.solver? ? "solver: <pd.solver> ms;" : ""> <pd.save? ? "save: <pd.save> ms;" : ""> total: <pd.collector + pd.solver + pd.save> ms";
    if(pd.file != "unknown") text += " (<pd.file>)";
    println(text);
}


int M = 1000000;

// rascalTModelForLoc is the basic work horse

map[str,TModel] rascalTModelForLoc(loc mloc, PathConfig pcfg, TypePalConfig config){     
    bool forceCompilationTopModule = false; /***** set to true during development of type checker *****/
    try {
        beginTime = cpuTime();   
        topModuleName = getModuleName(mloc, pcfg);
        
        before = cpuTime();
        ms = getImportAndExtendGraph(topModuleName, pcfg, config.showImports);
        if(forceCompilationTopModule){
            ms.valid -= {topModuleName};
            ms.tmodels = delete(ms.tmodels, topModuleName);
        }
        graphTime = cpuTime() - before;
       
        map[str, ProfileData] profs = ();
        imports_and_extends = ms.strPaths<0,2>;
        
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        map[str, set[str]] module2component = (m : c | c <- components, m <- c);
        
        list[str] ordered = [];
        
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
            recheck = !all(m <- component, m in ms.valid);
            
            for(m <- component){
                mi += 1;
                if(recheck){
                    if(!ms.modules[m]?){
                        mloc = getModuleLocation(m, pcfg);            
                        if(config.verbose) println("*** parsing <m> from <mloc>");
                        pt = parseModuleWithSpaces(mloc).top;
                        ms.modules[m] = pt;
                    }
                } else if(!ms.tmodels[m]?){
                    <found, tplLoc> = getDerivedReadLoc(m, "tpl", pcfg);
                    if(found){   
                        if(config.verbose) println("*** reading <m> from <tplLoc>");       
                        ms.tmodels[m] = readBinaryValueFile(#TModel, tplLoc);
                    }
                }
            }
            if(!all(m <- component, ms.tmodels[m]?, m in ms.valid)){
                ms.tmodels = domainX(ms.tmodels, component);
                <prof, tm> = rascalTModelComponent((m: ms.modules[m] |  m <- component), ms, pcfg = pcfg, config=config);
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
 
        if(config.showTimes){
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
        }
      
        return ms.tmodels;
    } catch ParseError(loc src): {
        return ("<mloc>" : tmodel()[messages = [ error("Parse error", src)  ]]);
    } catch Message msg: {
     return  ("<mloc>" : tmodel()[messages = [ error("During validation: <msg>", msg.at) ]]);
    } catch value e: {
        return ("<mloc>" : tmodel()[messages = [ error("During validation: <e>", mloc) ]]);
    }    
}

set[str] loadImportsAndExtends(str moduleName, ModuleStructure ms, Collector c, set[str] added){
    //println("loadImportsAndExtends for <moduleName>");
    
    rel[str,str] contains = ms.strPaths<0,2>;
    for(imp <- contains[moduleName]){
        if(imp notin added){
            if(ms.tmodels[imp]?){
                added += imp;
                //println("+++ adding <imp>");
                c.addTModel(ms.tmodels[imp]);
            }
        }
    }
    return added;
}

tuple[ProfileData, TModel] rascalTModelComponent(map[str, Tree] namedTrees, ModuleStructure ms, 
                                                 PathConfig pcfg = getDefaultPathConfig(), 
                                                 TypePalConfig config=rascalTypePalConfig(classicReifier=true), bool inline=false){
    modelName = intercalate(" + ", toList(domain(namedTrees)));
    
    if(config.verbose) println("\<\<\< checking <modelName>");
    c = newCollector(modelName, namedTrees, config=config);
    c.push(patternContainer, "toplevel");
    
    rascalPreCollectInitialization(namedTrees, c);
    startTime = cpuTime();
    
    added = {};
    for(nm <- namedTrees){
        added += loadImportsAndExtends(nm, ms, c, added);
    }
    //println("<modelName>: added before collect <added>");
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
    return <prof, tm>;
}

// ---- rascalTModelForName a checker version that works on module names

map[str,TModel] rascalTModelForName(str moduleName, PathConfig pcfg, TypePalConfig config){
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        mloc = getModuleLocation(moduleName, pcfg);
        return rascalTModelForLoc(mloc, pcfg, config);
    } catch value e: {
        return (moduleName : tmodel()[messages = [ error("During validation: <e>", mloc) ]]);
    }
}

// ---- checker functions for IDE

// name of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

ModuleMessages check(str moduleName, PathConfig pcfg){        // TODO change from ModuleMessages to list[ModuleMessages]
    try {
        moduleLoc = getModuleLocation(moduleName, pcfg);
        return check(moduleLoc, pcfg);
    } catch value e: {
        return program(|unkown:///|, {error("During validation: <e>", |unkown:///|)});
    }
}

ModuleMessages check(loc moduleLoc, PathConfig pcfg){          // TODO: change from ModuleMessages to list[ModuleMessages]
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <moduleLoc>"); iprintln(pcfg1);
    module2tmodel = rascalTModelForLoc(moduleLoc, pcfg, rascalTypePalConfig(classicReifier=true));
    moduleName = getModuleName(moduleLoc, pcfg);
    tm = module2tmodel[moduleName];
    return program(moduleLoc, toSet(tm.messages));
    //return [ program(getModuleLocation(m, pcfg), toSet(module2tmodel[m].messages)) | m <- module2tmodel ];
}

list[ModuleMessages] check(list[str] moduleNames, PathConfig pcfg){
    return [ *check(moduleName, pcfg) | moduleName <- moduleNames ];
}

list[ModuleMessages] check(list[loc] moduleLocs, PathConfig pcfg){
    return [ *check(moduleLoc, pcfg) | moduleLoc <- moduleLocs ];
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg){
     return [ *check(moduleLoc, pcfg) | moduleLoc <- find(root, "rsc") ]; 
}

// ---- Convenience check function during development -------------------------

map[str, list[Message]] checkModule(str moduleName,  
                              bool verbose                       = false,
                              bool debug                         = false,
                              bool showTimes                     = false,
                              bool showSolverIterations          = false,
                              bool showSolverSteps               = false,
                              bool showAttempts               = false,
                              bool showTModel                    = false,
                              bool showImports                   = false,
                              bool validateConstraints           = true) {
                              
    if(verbose) {
        showSolverIterations = showImports = true;
    }
    if(debug){
        showSolverIterations = showSolverSteps = showTModel = showImports = true;
    }
    config = rascalTypePalConfig(classicReifier=true);
    config.verbose = verbose;
    config.showTimes = showTimes;
    config.showSolverIterations = showSolverIterations;
    config.showSolverSteps = showSolverSteps;
    config.showAttempts = showAttempts;
    config.showTModel = showTModel;
    config.showImports = showImports;
    config.validateConstraints = validateConstraints;
    
    mname2tm = rascalTModelForName(moduleName, getDefaultPathConfig(), config);
    return (mname : mname2tm[mname].messages | mname <- mname2tm, !isEmpty(mname2tm[mname].messages));
}

//// ---- Testing ---------------------------------------------------------------
//
//TModel rascalTModelForTestModules(Tree pt, bool debug=false){
//    ms = getInlineImportAndExtendGraph(pt, getDefaultPathConfig());
//   TypePalConfig config=rascalTypePalConfig(classicReifier=true);
//   if(debug){
//        config = config[showSolverIterations = true][showImports = true];
//   }
//    if(start[Modules] mds := pt){
//        return rascalTModelComponent( (unescape("<md.header.name>") : md | md <- mds.top.modules ), ms, config=config, inline=true)[1];
//    } else if(Modules mds := pt){
//        return rascalTModelComponent( (unescape("<md.header.name>") : md | md <- mds.modules ), ms, config=config, inline=true)[1];
//    } else
//        throw "Cannot handle Modules";
//}
//
//void testModules(str names...) {
//    if(isEmpty(names)) names = allTests;
//    runTests([|project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/check/tests/<name>.ttl| | str name <- names], #Modules, rascalTModelForTestModules, verbose=false);
//}
//
//list[str] allTests = ["adt", "adtparam", "alias", "assignment", "datadecl", "exp", "fields", "fundecl", 
//                     "imports", "operators", "pat", "scope", "splicepats", "stats"/*,"syntax1", "syntax2", "syntax3"*/];