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

import lang::rascal::\syntax::Rascal;

extend lang::rascalcore::check::ADTandGrammar;
extend lang::rascalcore::check::CollectDeclaration;
extend lang::rascalcore::check::CollectExpression;
extend lang::rascalcore::check::CollectOperators;
extend lang::rascalcore::check::CollectPattern;
extend lang::rascalcore::check::CollectStatement;
extend lang::rascalcore::check::Import;

extend lang::rascalcore::check::RascalConfig;

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

// Duplicate in lang::rascalcore::compile::util::Names, factor out
data PathConfig(
    loc generatedSources=|unknown:///|,
    loc resources = |unknown:///|,
    loc testResources =|unknown:///|
);


Tree mkTree(int n) = [DecimalIntegerLiteral] "<for(int _ <- [0 .. n]){>6<}>"; // Create a unique tree to identify predefined names
 
void rascalPreCollectInitialization(map[str, Tree] namedTrees, Collector c){

    c.push(patternContainer, "toplevel");
    c.push(key_allow_use_before_def, <|none:///|(0,0,<0,0>,<0,0>), |none:///|(0,0,<0,0>,<0,0>)>);
    
    for(tree <- range(namedTrees)){
        c.enterScope(tree);      
            // Tree type, fields "top" and "src"
            TreeType = aadt("Tree", [], dataSyntax());
            treeScope = mkTree(1);
            c.define("Tree", dataId(), treeScope, defType(TreeType));
            c.enterScope(treeScope);
                c.define("top", fieldId(), mkTree(2), defType(TreeType));
                c.define("src", keywordFieldId(), mkTree(3), defType(aloc())); // TODO: remove when @\loc is gone
            c.leaveScope(treeScope); 
            
            //DefaultLayoutType = aadt("$default$", [], layoutSyntax());
            //c.define("$default$", layoutId(), mkTree(4), defType(DefaultLayoutType));
            
            // Reified type
            if(c.getConfig().classicReifier){
                ;////data type[&T] = type(Symbol symbol, map[Symbol,Production] definitions);
                //typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
                //SymbolType = aadt("Symbol", [], dataSyntax());
                //ProductionType = aadt("Production", [], dataSyntax());
                //symbolField = SymbolType[alabel="symbol"]; //<"symbol", SymbolType>;
                //definitionsField = amap(SymbolType, ProductionType)[alabel="definitions"]; //< "definitions", amap(SymbolType, ProductionType)>;
                //c.define("type", constructorId(), mkTree(3), defType(acons(typeType, [symbolField, definitionsField], [], alabel="type")));
                // NB: this definition does not persist to avoid duplicate definitions in different modules, see lang::rascalcore::check::Import::saveModule
            } else {
                //data type[&T] = type(AType symbol, map[AType,AProduction] definitions);
                //typeType = aadt("Type", [aparameter("T", avalue())], dataSyntax());
                SymbolType = aadt("AType", [], dataSyntax());
                AProductionType = aadt("AProduction", [], dataSyntax());
                atypeField = SymbolType[alabel="symbol"]; //<"symbol", SymbolType>;
                definitionsField = amap(SymbolType, AProductionType)[alabel="definitions"]; //< "definitions", amap(SymbolType, AProductionType)>;
                //c.define("atype", constructorId(), mkTree(3), defType(acons(typeType, [atypeField, definitionsField], [], alabel="type")));
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

private int npc = 0;
@doc{
  PathConfig for testing generated modules in |test-modules:///| in memory file system.
  
  * gets source files exclusively from |test-modules:///|
  * generates bin files in the in-memory file system 
  * depends only on the pre-compiled standard library from the rascal project 
}
public PathConfig getDefaultPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(   
        srcs = [|test-modules:///| /* test-modules is an in-memory file-system */], 
        bin = |test-modules:///rascal-core-tests-bin-<snpc>|, 
        libs = [|lib://rascal/|]
    );
}

@doc{ 
  for type-checking test modules in the rascal-core project; such as "lang::rascalcore::check::Test1" 
  
  * sources have to be in |project://rascal-core/src/org/rascalmpl/core/library|
  * binaries will be stored in-memory only
  * has the standard library and typepal on the library path, in case you accidentally want to test
    a module in rascal-core which depends on typepal.
}
public PathConfig getRascalCorePathConfig() {
   return pathConfig(   
        srcs = [
                |project://rascal/src/org/rascalmpl/library|, 
                |std:///|,
                |project://rascal-core/src/org/rascalmpl/core/library|,
                //|project://rascal_eclipse/src/org/rascalmpl/eclipse/library|,
                |project://typepal/src|
                //|project://salix/src|
                ],
        bin = |project://rascal-core/target/test-classes|,
        generatedSources = |project://rascal-core/target/generated-test-sources|,
        resources = |project://rascal-core/target/generated-test-resources|,
        libs = []
    );
}
    
@doc{
  a path config for testing type-checking of the standard library in the rascal project
}    
public PathConfig getRascalProjectPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(   
        srcs = [|project://rascal/src/org/rascalmpl/library|], 
        bin = |test-modules:///rascal-lib-bin-<snpc>|, 
        libs = []
    );  
}  

// Profiling

data ProfileData = profile(str file = "unknown", int collector = 0, int solver = 0, int save = 0);
 
void report(ProfileData pd){
    text = "<pd.collector? ? "collector: <pd.collector> ms;" : ""> <pd.solver? ? "solver: <pd.solver> ms;" : ""> <pd.save? ? "save: <pd.save> ms;" : ""> total: <pd.collector + pd.solver + pd.save> ms";
    if(pd.file != "unknown") text += " (<pd.file>)";
    println(text);
}

// rascalTModelForLoc is the basic work horse
 
ModuleStatus rascalTModelForLocs(list[loc] mlocs, PathConfig pcfg, TypePalConfig config, list[Message](str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, TModel tm, ModuleStatus ms, PathConfig pcfg) codgen){     
    bool forceCompilationTopModule = true; /***** for convenience, set to true during development of type checker *****/
    ModuleStatus ms = newModuleStatus();
    beginTime = cpuTime();   
    topModuleNames = { getModuleName(mloc, pcfg) | mloc <- mlocs };
        
    before = cpuTime();
    try {
        ms = getImportAndExtendGraph(topModuleNames, pcfg, config.logImports);
       
        if(forceCompilationTopModule){
            for(nm <- topModuleNames){
                ms.status[nm] -= {tpl_valid(), checked(), code_generated()};
                ms.status[nm] += {tpl_invalid()};
            }
        }
        
        graphTime = cpuTime() - before;
       
        map[str, ProfileData] profs = ();
        imports_and_extends = ms.strPaths<0,2>;
        
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        map[str, set[str]] module2component = (m : c | c <- components, m <- c);
        
        list[str] ordered = [];
        
        if(isEmpty(sorted)){
            ordered = toList(topModuleNames);
            for(topModuleName <- topModuleNames){
                module2component[topModuleName] = {topModuleName};
            }
        } else {
            ordered = reverse(sorted);
            singletons = toList(topModuleNames - toSet(ordered));
            ordered += singletons;
            for(singleton <- singletons){
                module2component[singleton] = {singleton};
            }
        }
        
        map[str, loc] moduleScopes = ();
        map[str,str] path2module = (ms.moduleLocs[mname].path : mname | mname <- ms.moduleLocs);
        mi = 0;
        nmodules = size(ordered);
       
        while(mi < nmodules){
            component = module2component[ordered[mi]];
            recheck = !all(m <- component, (tpl_valid() in ms.status[m] || checked() in ms.status[m]));
            
            for(m <- component){
                mi += 1;
                if(!recheck){
                    if(tpl_valid() notin ms.status[m]){
                        <found, tm, ms> = getTmodelForModule(m, ms, pcfg);
                        if(found){   
                            ms.status[m] += {tpl_valid(), checked()};
                        }
                    }
               }
            }
            if(!all(m <- component, tpl_valid() in ms.status[m] || checked() in ms.status[m])){
                <prof, tm, ms> = rascalTModelComponent(component, ms, pcfg, config=config);
                profs[intercalate("/", toList(component))] = prof;
                moduleScopes += getModuleScopes(tm);
                for(m <- component){
                    imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == m };
                    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == m };
                    invertedExtends = ms.strPaths<2,0>;
                    if(config.warnUnused){
                        // Look for unused imports or exports
                        usedModules = {path2module[l.path] | loc l <- range(tm.useDef), tm.definitions[l].idRole != moduleId(), path2module[l.path]?};
                        usedModules += {*invertedExtends[um] | um <- usedModules}; // use of an extended module via import
                        msgs = [];
                        <pt, ms> = getModuleParseTree(m, ms, pcfg);
                        check_imports:
                        for(imod <- pt.header.imports, imod has \module){
                            iname = unescape("<imod.\module.name>");
                            if(iname notin usedModules){ 
                               if(iname == "ParseTree" && implicitlyUsesParseTree(ms.moduleLocs[m].path, tm)){      
                                 continue check_imports;
                               }
                               if(ms.moduleLocs[iname]? && implicitlyUsesLayoutOrLexical(ms.moduleLocs[m].path, ms.moduleLocs[iname].path, tm)){
                                continue check_imports;
                               }
                               if(ms.moduleLocs[iname]? && usesOrExtendsADT(ms.moduleLocs[m].path, ms.moduleLocs[iname].path, tm)){
                                continue check_imports;
                               }
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
                    } else {
                        ms.messages[m] = [];
                    }
                    <tm1, ms> = saveModule(m, imports, extends, ms, moduleScopes, pcfg, tm);
                    ms = addTModel(m, tm1, ms);
                    ms.status[m] += {tpl_valid(), checked()};
                    <pt, ms> = getModuleParseTree(m, ms, pcfg);
                    msgs = codgen(m, pt, tm1, ms, pcfg);
                    ms.messages[m] = msgs;
                    ms.status[m] += {code_generated()};
                    //ms.parseTrees = delete(ms.parseTrees, m);
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
        //return ms; //<ms.tmodels, ms.moduleLocs, ()>;;
    } catch ParseError(loc src): {
        for(mname <- topModuleNames){
            ms.messages[mname] = [ error("Parse error", src) ];
        }
       // return ms;
    } catch Message msg: {
        for(mname <- topModuleNames){
            ms.messages[mname] = [error("During type checking: <msg>", msg.at)];
        }
        //return ms;
    }
    //catch e:{
    //    println("**** Exception: <e> ****");
    //    //return ms;  
    //}
    //for(nm <- topModuleNames){
    //    ms.parseTrees = delete(ms.parseTrees, nm);
    //}
    return ms;
}

bool implicitlyUsesParseTree(str modulePath, TModel tm){
    return any(loc l <- tm.facts, l.path == modulePath, areified(_) <- tm.facts[l]);
}

bool implicitlyUsesLayoutOrLexical(str modulePath, str importPath, TModel tm){
    return    any(loc l <- tm.facts, l.path == importPath, aadt(_,_,sr) := tm.facts[l], sr in {layoutSyntax(), lexicalSyntax()})
           && any(loc l <- tm.facts, l.path == modulePath, aadt(_,_,contextFreeSyntax()) := tm.facts[l]);
}

bool usesOrExtendsADT(str modulePath, str importPath, TModel tm){
    usedADTs = { tm.facts[l] | loc l <- tm.facts, l.path == modulePath, aadt(_,_,_) := tm.facts[l] };
    definedADTs = { the_adt | Define d <- tm.defines, d.defined.path == modulePath, defType(the_adt:aadt(_,_,_)) := d.defInfo };
    usedOrDefinedADTs = usedADTs + definedADTs;
    return any(loc l <- tm.facts, l.path == importPath, the_adt:aadt(_,_,_) := tm.facts[l], the_adt in usedOrDefinedADTs);
}

tuple[set[str], ModuleStatus] loadImportsAndExtends(str moduleName, ModuleStatus ms, Collector c, set[str] added, PathConfig pcfg){
    rel[str,str] contains = ms.strPaths<0,2>;
    for(imp <- contains[moduleName]){
        if(imp notin added){
            if(tpl_valid() in ms.status[imp]){ //ms.tmodels[imp]?){
                added += imp;
                <found, tm, ms> = getTmodelForModule(imp, ms, pcfg);
                c.addTModel(tm); //ms.tmodels[imp]);
            }
        }
    }
    return <added, ms>;
}

tuple[ProfileData, TModel, ModuleStatus] rascalTModelComponent(set[str] moduleNames, ModuleStatus ms, PathConfig pcfg,
                                                 TypePalConfig config=rascalTypePalConfig(classicReifier=true), bool inline=false){
    modelName = intercalate(" + ", toList(moduleNames));    
    map[str, Module] namedTrees = ();
    for(nm <- moduleNames){
        <pt, ms> = getModuleParseTree(nm, ms, pcfg);
        namedTrees[nm] = pt;
    }
    if(config.verbose) println("\<\<\< checking <modelName>");
    c = newCollector(modelName, namedTrees, config);
    c.push(key_pathconfig, pcfg);
    
    rascalPreCollectInitialization(namedTrees, c);
    startTime = cpuTime();
    
    added = {};
    for(nm <- moduleNames){
        <a, ms> = loadImportsAndExtends(nm, ms, c, added, pcfg);
        added += a;
    }
    //println("<modelName>: added before collect <added>");
    startTime = cpuTime();
    for(str nm <- namedTrees){
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
    //for(nm <- moduleNames){
    //    delete(ms.parseTrees, nm);
    //}
    return <prof, tm, ms>;
}

// ---- rascalTModelForName a checker version that works on module names

ModuleStatus rascalTModelForNames(list[str] moduleNames, PathConfig pcfg, TypePalConfig config, list[Message] (str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, TModel tm, ModuleStatus ms, PathConfig pcfg) codgen){
    mloc = |unknown:///|(0,0,<0,0>,<0,0>);
    iprintln(pcfg);
    try {
        mlocs = [ getModuleLocation(moduleName, pcfg) | moduleName <- moduleNames ];
        return rascalTModelForLocs(mlocs, pcfg, config, codgen);
    } catch value e: {
        ms = newModuleStatus();
        for( moduleName <- moduleNames){
            ms.messages[moduleName] = [ error("<e>", mloc) ];
        }
        return ms;
        //return <(moduleName : tmodel()[messages = [ error("<e>", mloc) ]] | moduleName <- moduleNames), (), ()>;
    }
}

// ---- checker functions for IDE

// name  of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

list[Message] dummy_compile1(str _qualifiedModuleName, lang::rascal::\syntax::Rascal::Module _M, TModel _tm, ModuleStatus _ms, PathConfig _pcfg/*, loc reloc = |noreloc:///|, bool verbose = true, bool optimize=true, bool enableAsserts=true*/)
    = [];
    
list[ModuleMessages] check(list[loc] moduleLocs, PathConfig pcfg){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    //println("=== check: <moduleLocs>"); iprintln(pcfg1);
    ms = rascalTModelForLocs(moduleLocs, pcfg, rascalTypePalConfig(classicReifier=true,logImports=false), dummy_compile1);
    return [ program(ms.moduleLocs[mname], toSet(ms.messages[mname])) | mname <- ms.messages ];
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg){
    return check(toList(find(root, "rsc")), pcfg);
}

//// ---- Convenience check function during development -------------------------
//      
//map[str, list[Message]] checkModules(list[str] moduleNames, TypePalConfig config, PathConfig pcfg) {
//    <tmodels, moduleLocs, modules> = rascalTModelForNames(moduleNames, pcfg, config);
//    return (mname : tmodels[mname].messages | mname <- tmodels, !isEmpty(tmodels[mname].messages));
//}

// ---- Convenience check function during development -------------------------



// -- calculate rename changes
// a change request happens at a symbol location that points to the lexical the cursor position, not necessarily a full symbol present in the TModel
// returns a list of changes (that can cross multiple files)
// operations:
//    - insert = loc start and end range should be the same (a zero length location)
//    - replace = loc range defines replacement
//    - delete = empty string value
lrel[loc, str] rename(loc _symbol, str _newName, PathConfig _currentProject, rel[loc, PathConfig] _otherProjects) {
    return [];
}
