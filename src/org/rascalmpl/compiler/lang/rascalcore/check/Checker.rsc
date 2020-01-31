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
 * - Unused modules not detected correctly
 * - Fix reference in grammar rules (it seems that production label get lost somewhere and reference cannot be found)
 * - Support for reified types
 *
 * Potential additions/improvements
 * - Reduce rechecking by comparing old and new tpl file
 */
 
extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;

extend lang::rascalcore::check::ADTandGrammar;
extend lang::rascalcore::check::CollectDeclaration;
extend lang::rascalcore::check::CollectExpression;
extend lang::rascalcore::check::CollectOperators;
extend lang::rascalcore::check::CollectPattern;
extend lang::rascalcore::check::CollectStatement;
extend lang::rascalcore::check::CollectType;
extend lang::rascalcore::check::Import;

import lang::rascalcore::check::ScopeInfo;

import lang::rascalcore::check::RascalConfig;

import IO;
import List;
import Map;
import Message;
import Relation;
import Set;
import String;
import ValueIO;

import util::Benchmark;
import util::Reflective;
import util::FileSystem;
import analysis::graphs::Graph;
    
//str parserPackage = "org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers";
//str parserPackage = "org.rascalmpl.core.java.parser.object";

Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int i <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(map[str, Tree] namedTrees, Collector c){

    c.push(patternContainer, "toplevel");
    c.push(key_allow_use_before_def, <|none:///|(0,0,<0,0>,<0,0>), |none:///|(0,0,<0,0>,<0,0>)>);
    
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
                c.define("type", constructorId(), mkTree(3), defType(acons(typeType, [symbolField, definitionsField], [], label="type")));
                // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
            } else {
                //data type[&T] = type(AType symbol, map[AType,AProduction] definitions);
                typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
                SymbolType = aadt("AType", [], dataSyntax());
                AProductionType = aadt("AProduction", [], dataSyntax());
                atypeField = SymbolType[label="symbol"]; //<"symbol", SymbolType>;
                definitionsField = amap(SymbolType, AProductionType)[label="definitions"]; //< "definitions", amap(SymbolType, AProductionType)>;
                c.define("atype", constructorId(), mkTree(3), defType(acons(typeType, [atypeField, definitionsField], [], label="type")));
                // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
            }
        c.leaveScope(tree);
    }
}
//
//// Enhance TModel before running Solver
//TModel rascalPreSolver(map[str,Tree] namedTrees, TModel m){
//    // add transitive edges for extend
//    extendPlus = {<from, to> | <loc from, extendPath(), loc to> <- m.paths}+;
//    m.paths += { <from, extendPath(), to> | <loc from, loc to> <- extendPlus};
//    m.paths += { <c, importPath(), a> | < loc c, importPath(), loc b> <- m.paths,  <b , extendPath(), loc a> <- m.paths};
//    return m;
//}
//
//void rascalPostSolver(map[str,Tree] namedTrees, Solver s){
//    if(!s.reportedErrors()){
//        for(mname <- namedTrees){
//            pt = namedTrees[mname];
//            g = addGrammar(getLoc(pt), s);
//            if(!isEmpty(g.rules)){ 
//                pname = "DefaultParser";
//                if(Module m := pt) { 
//                        moduleName = "<m.header.name>";
//                        pname = parserName(moduleName);
//                }
//                //<msgs, parserClass> = newGenerate(parserPackage, pname, g); 
//                //s.addMessages(msgs);
//                //TODO: generates too long file names
//                //msgs = saveParser(pname, parserClass, |project://rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers|, s.getConfig().verbose);
//            //s.addMessages(msgs);
//            }
//            addADTsAndCommonKeywordFields(s);
//        }
//   }
//}

// ----  Various check functions  ---------------------------------------------

public PathConfig getDefaultPathConfig() {
    println("checker path config");
    return pathConfig(   
        srcs = [|test-modules:///|, /* test-modules is an in-memory file-system */ 
                //Jurgen: Commented out, because wouldn't it be weird if modules under test depend secretly on the implementation of the checker?
                //Paul: we need this to run in Eclipse console
                |project://rascal-core/src/org/rascalmpl/core/library/|,
                |project://rascal/src/org/rascalmpl/library/|
                //, |project://typepal/src/|
               ],
        bin = |project://rascal-core/bin|, 
        libs = [|std:///|, |lib://rascal/|, |lib://typepal/|]
               );
}

// Profiling

data ProfileData = profile(str file = "unknown", int collector = 0, int solver = 0, int save = 0);
 
void report(ProfileData pd){
    text = "<pd.collector? ? "collector: <pd.collector> ms;" : ""> <pd.solver? ? "solver: <pd.solver> ms;" : ""> <pd.save? ? "save: <pd.save> ms;" : ""> total: <pd.collector + pd.solver + pd.save> ms";
    if(pd.file != "unknown") text += " (<pd.file>)";
    println(text);
}

alias CheckerResult = tuple[map[str,TModel] tmodels, map[str,loc] moduleLocs, map[str,Module] modules];

// rascalTModelForLoc is the basic work horse
 
CheckerResult rascalTModelForLocs(list[loc] mlocs, PathConfig pcfg, TypePalConfig config){     
    bool forceCompilationTopModule = true; /***** set to true during development of type checker *****/
    try {
        beginTime = cpuTime();   
        topModuleNames = { getModuleName(mloc, pcfg) | mloc <- mlocs };
        
        before = cpuTime();
        ms = getImportAndExtendGraph(topModuleNames, pcfg, config.logImports);
        
        //println("rascalTModelForLocs: <size(mlocs)> mlocs, <size(ms.tmodels)> tmodels, <size(ms.modules)> modules, <size(ms.moduleLocs)> moduleLocs, <size(ms.moduleLastModified)> lastModified, <size(ms.valid)> valid, <size(ms.invalid)> invalid");
        
        if(forceCompilationTopModule){
            ms.valid -= topModuleNames;
            for(topModuleName <- topModuleNames){
                ms.tmodels = delete(ms.tmodels, topModuleName);
            }
        }
        
        graphTime = cpuTime() - before;
       
        map[str, ProfileData] profs = ();
        imports_and_extends = ms.strPaths<0,2>;
        
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        map[str, set[str]] module2component = (m : c | c <- components, m <- c);
        
        list[str] ordered = [];
        
        //println("topModuleNames: <topModuleNames>");
        
        if(isEmpty(sorted)){
            ordered = toList(topModuleNames);
            for(topModuleName <- topModuleNames){
                module2component[topModuleName] = {topModuleName};
            }
        } else {
            ordered = reverse(sorted);
            singletons = toList(topModuleNames - toSet(ordered));
            //println("ordered: <ordered>");
            //println("found singletons: <singletons>");
            ordered += singletons;
            for(singleton <- singletons){
                module2component[singleton] = {singleton};
            }
        }
        
        map[str, loc] moduleScopes = ();
        map[str,str] path2module = (ms.moduleLocs[mname].path : mname | mname <- ms.moduleLocs);
        mi = 0;
        nmodules = size(ordered);
        
        //println("rascalTModelForLocs: <size(mlocs)> mlocs, <nmodules> after ordering");
       
        while(mi < nmodules){
            component = module2component[ordered[mi]];
            recheck = !all(m <- component, m in ms.valid);
            
            for(m <- component){
                mi += 1;
                if(recheck){
                    if(!ms.modules[m]?){
                        mloc = getModuleLocation(m, pcfg);  
                        //ms.moduleLocs[m] = mloc;  
                        path2module[mloc.path] = m;        
                        if(config.verbose) println("*** parsing <m> from <mloc>");
                        ms.moduleLastModified[m] = lastModified(mloc);
                        try {
                            Module pt = parseModuleWithSpaces(mloc).top;
                            ms.modules[m] = pt;
                            ms.moduleLocs[m] = pt@\loc; 
                        } catch Java("ParseError","Parse error"): {
                            ms.tmodels[m] = tmodel()[messages = [ error("Parse error in module `<m>`", getModuleLocation(m, pcfg)) ]];
                            ms.valid += {m};
                        }
                       
                    }
                } else if(!ms.tmodels[m]?){
                    <found, tplLoc> = getDerivedReadLoc(m, "tpl", pcfg);
                    if(found){   
                        try {
                            if(config.verbose) println("*** reading <m> from <tplLoc>");  
                            ms.moduleLastModified[m] = lastModified(tplLoc);     
                            ms.tmodels[m] = readBinaryValueFile(#TModel, tplLoc);
                        } catch IO(str msg): {
                            ms.tmodels[m] = tmodel()[messages = [ error("Error while loading .tpl file for module `<m>`: <msg>", getModuleLocation(m, pcfg)) ]];
                            ms.valid += {m};
                        }
                    }
                }
            }
            if(!all(m <- component, ms.tmodels[m]?, m in ms.valid)){
                ms.tmodels = domainX(ms.tmodels, component);
                <prof, tm> = rascalTModelComponent((m: ms.modules[m] |  m <- component), ms, pcfg, config=config);
                profs[intercalate("/", toList(component))] = prof;
                moduleScopes += getModuleScopes(tm);
                for(m <- component){
                    imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == m };
                    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == m };
                    if(config.warnUnused){
                        // Look for unused imports or exports
                        usedModules = {path2module[l.path] | loc l <- range(tm.useDef), tm.definitions[l].idRole != moduleId(), path2module[l.path]?};
                        msgs = [];
                        for(imod <- ms.modules[m].header.imports, imod has \module){
                            iname = unescape("<imod.\module.name>");
                            if(iname notin usedModules){ 
                               if(imod is \default){
                                 msgs += warning("Unused import of `<iname>`", imod@\loc);
                               } else {
                                 msgs += info("Extended module `<iname>` is unused in the current module", imod@\loc);
                               }
                            }
                        }
                        tm.messages += msgs;
                    }
                    if(ms.messages[m]?){
                        tm.messages += ms.messages[m];
                    }
                    ms.tmodels[m] = saveModule(m, imports, extends, moduleScopes, ms.moduleLastModified, pcfg, tm);
                    //ms.modules = delete(ms.modules, m);
                }
            }
        }
 
        if(config.logTime){
            tcollector = 0; tsolver = 0; tsave = 0;
            for(m <- profs){
                p = profs[m];
                tcollector += p.collector;
                tsolver += p.solver;
                tsave += p.save;
                report(p);
            }
            
            int toMilli(int time_in_ns) = time_in_ns/1000000;
            
            println("<topModuleNames>, import graph <toMilli(graphTime)> ms");
            println("<topModuleNames> <tcollector+tsolver> ms [ collector: <tcollector> ms; solver: <tsolver> ms; save: <tsave> ms ]");
            println("<topModuleNames>, measured total time: <toMilli(cpuTime() - beginTime)> ms");
        }
              
        return <ms.tmodels, ms.moduleLocs, ms.modules>;
    } catch ParseError(loc src): {
        return <("<mloc>" : tmodel()[messages = [ error("Parse error", src)  ]] | mloc <- mlocs), (), ()>;
    } catch Message msg: {
     return  <("<mloc>" : tmodel()[messages = [ error("During type checking: <msg>", msg.at) ]] | mloc <- mlocs), (), ()>;
    } 
    //catch value e: {
    //    return <("<mloc>" : tmodel()[messages = [ error("During validation: <e>", mloc) ]]), (), ()>;
    //}    
}

set[str] loadImportsAndExtends(str moduleName, ModuleStructure ms, Collector c, set[str] added){
    rel[str,str] contains = ms.strPaths<0,2>;
    for(imp <- contains[moduleName]){
        if(imp notin added){
            if(ms.tmodels[imp]?){
                added += imp;
                c.addTModel(ms.tmodels[imp]);
            }
        }
    }
    return added;
}

tuple[ProfileData, TModel] rascalTModelComponent(map[str, Tree] namedTrees, ModuleStructure ms, PathConfig pcfg,
                                                 TypePalConfig config=rascalTypePalConfig(classicReifier=true), bool inline=false){
    modelName = intercalate(" + ", toList(domain(namedTrees)));
    
    if(config.verbose) println("\<\<\< checking <modelName>");
    c = newCollector(modelName, namedTrees, config=config);
    c.push(key_pathconfig, pcfg);
    
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

CheckerResult rascalTModelForNames(list[str] moduleNames, PathConfig pcfg, TypePalConfig config){
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    try {
        mlocs = [ getModuleLocation(moduleName, pcfg) | moduleName <- moduleNames ];
        return rascalTModelForLocs(mlocs, pcfg, config);
    } catch value e: {
        throw e;
        //return <(moduleName : tmodel()[messages = [ error("During type checking: <e>", mloc) ]] | moduleName <- moduleNames), (), ()>;
    }
}

// ---- checker functions for IDE

// name  of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);


list[ModuleMessages] check(list[loc] moduleLocs, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    println("=== check: <moduleLocs>"); iprintln(pcfg1);
    <tmodels, moduleLocs1, modules> = rascalTModelForLocs(moduleLocs, pcfg, rascalTypePalConfig(classicReifier=true,logImports=true));
    nomodels = {moduleName | moduleLoc <- moduleLocs, moduleName := getModuleName(moduleLoc, pcfg), !tmodels[moduleName]?};
    if(!isEmpty(nomodels)) println("<size(nomodels)> tmodels missing for: <nomodels>");
    return [ program(moduleLoc, toSet(tm.messages)) | moduleLoc <- moduleLocs, moduleName := getModuleName(moduleLoc, pcfg), tmodels[moduleName]?, tm:= tmodels[moduleName] ];
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg){
    return check(toList(find(root, "rsc")), pcfg);
}

// ---- Convenience check function during development -------------------------
      
map[str, list[Message]] checkModules(list[str] moduleNames, TypePalConfig config) {
    <tmodels, moduleLocs, modules> = rascalTModelForNames(moduleNames, getDefaultPathConfig(), config);
    return (mname : tmodels[mname].messages | mname <- tmodels, !isEmpty(tmodels[mname].messages));
}

// ---- Convenience check function during development -------------------------

