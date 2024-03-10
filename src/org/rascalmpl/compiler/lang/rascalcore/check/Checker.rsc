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
    Top level driver for the checker (rascalTModelForLocsNote). 
    Note that the checker calls the code generator (given as parameter) when there are no type errors.
*/
              
/*
 * TODO:
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
import Message;
import Node;
import Relation;
import Set;
import String;

import util::Reflective;
import util::FileSystem;
import util::Monitor;
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
}

//TODO: renact this when we will generate parsers
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

// ----  Various PathConfigs  ---------------------------------------------

private int npc = 0;
@synopsis{PathConfig for testing generated modules in |memory://test-modules/| in memory file system.}
@description{
* gets source files exclusively from |memory://test-modules/|
* generates bin files in the in-memory file system 
* depends only on the pre-compiled standard library from the rascal project 
}
public PathConfig getDefaultTestingPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(   
        srcs = [ |memory:///test-modules/|  ],
        bin = |memory:///test-modules/rascal-core-tests-bin-<snpc>|, 
        generatedSources = |memory:///test-modules/generated-test-sources-<snpc>|,
        resources = |memory:///test-modules/generated-test-resources-<snpc>|,
        libs = [ |std:///| ]
    );
}

@synopsis{PathConfig for type-checking test modules in the rascal-core project; such as lang::rascalcore::check::Test1}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
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
    
@synopsis{a path config for testing type-checking of the standard library in the rascal project}    
public PathConfig getRascalProjectPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(   
        srcs = [|project://rascal/src/org/rascalmpl/library|], 
        bin = |memory:///test-modules/rascal-lib-bin-<snpc>|, 
        libs = []
    );  
}  

CompilerConfig getRascalCompilerConfig()
    = cconfig();

list[Message] validatePathConfigForCompiler(PathConfig pcfg, loc mloc){
    msgs = [];
    
    if(isEmpty(pcfg.srcs)) msgs += error("PathConfig: `srcs` is empty", mloc);
    for(src <- pcfg.srcs){
        if(!exists(src)) msgs += error("PathConfig `srcs`: <src> does not exist", src);
    }
    for(lb <- pcfg.libs){
        if(!exists(lb)) msgs += error("PathConfig `libs`: <lb> does not exist", lb);
    }
    
    if(!exists(pcfg.bin)) {
        try {
            mkDirectory(pcfg.resources);
        } catch _: {
            msgs += error("PathConfig `bin`: <pcfg.bin> does not exist", pcfg.bin);
        }
    }
    
    if(!exists(pcfg.resources)) {
        try {
            mkDirectory(pcfg.resources);
        } catch _: {
            msgs += error("PathConfig `resources`: <pcfg.resources> does not exist", pcfg.resources);
        }
    }
    
    if(!exists(pcfg.generatedSources)) 
        try {
            mkDirectory(pcfg.generatedSources);
        } catch _: {
            msgs += error("PathConfig `generatedSources`: <pcfg.generatedSources> does not exist", pcfg.generatedSources);
        }
        
    return msgs;
}

// ----  Various check functions  ---------------------------------------------

// rascalTModelForLocs is the basic work horse
 
ModuleStatus rascalTModelForLocs(
    list[loc] mlocs,  
    TypePalConfig config, 
    CompilerConfig compilerConfig,
    list[Message](str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, ModuleStatus ms, CompilerConfig compilerConfig) codgen
){     
    bool forceCompilationTopModule = false; /***** for convenience, set to true during development of type checker *****/
    
    pcfg = config.typepalPathConfig;
    if(compilerConfig.verbose) iprintln(pcfg);
    
    msgs = validatePathConfigForCompiler(pcfg, mlocs[0]);
    if(!isEmpty(msgs)){
        throw msgs;
    }

    ModuleStatus ms = newModuleStatus(pcfg); 
    topModuleNames = {};
    
    for(mloc <- mlocs){
        //try {
            m = getModuleName(mloc, pcfg);
            topModuleNames += {m};
        //} catch str e: {
        //    ms.messages += [ error("Cannot get module name for <mloc>, reason: <e>", mloc) ];
        //    return ms;
        //}
    }
    
    try {
        ms = getImportAndExtendGraph(topModuleNames, pcfg);
       
        if(forceCompilationTopModule){
            for(str nm <- topModuleNames){
                ms.status[nm] = {};
            }
        }
       
        imports_and_extends = ms.strPaths<0,2>;
        
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        map[str, set[str]] module2component = (m : c | c <- components, m <- c);
        
        list[str] ordered = [];
        
        if(isEmpty(sorted)){
            ordered = toList(topModuleNames);
            for(str topModuleName <- topModuleNames){
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
            recheck = !all(m <- component, (tpl_uptodate() in ms.status[m] || checked() in ms.status[m]));
            for(m <- component){
                mi += 1;
                if(!recheck){
                    if(tpl_uptodate() notin ms.status[m]){
                        <found, tm, ms> = getTModelForModule(m, ms);
                        if(found){   
                            ms.status[m] += {tpl_uptodate(), checked()};
                        }
                    }
               }
            }
            if(!all(m <- component, tpl_uptodate() in ms.status[m] || checked() in ms.status[m])){
                <tm, ms> = rascalTModelComponent(component, ms, config);
                moduleScopes += getModuleScopes(tm);
                map[str,TModel] tmodels_for_component = ();
                map[str,set[str]] m_imports = ();
                map[str,set[str]] m_extends = ();
                for(m <- component){
                    imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == m };
                    m_imports[m] =  imports;
                    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == m };
                    m_extends[m] = extends;
                    invertedExtends = ms.strPaths<2,0>;
                    if(config.warnUnused){
                        // Look for unused imports or exports
                        usedModules = {path2module[l.path] | loc l <- range(tm.useDef), tm.definitions[l].idRole != moduleId(), path2module[l.path]?};
                        usedModules += {*invertedExtends[um] | um <- usedModules}; // use of an extended module via import
                        msgs = [];
                        <success, pt, ms> = getModuleParseTree(m, ms);
                        if(success){
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
                                   if(checked() in (ms.status[iname] ? {})){
                                       if(imod is \default){
                                         msgs += warning("Unused import of `<iname>`", imod@\loc);
                                       } else {
                                         msgs += info("Extended module `<iname>` is unused in the current module", imod@\loc);
                                       }
                                   }
                                }
                            }
                            tm.messages += msgs;
                        } else {
                            tm.messages += [ error("Cannot get parse tree for module `<m>`", ms.moduleLocs[m]) ];
                        }
                    }
                    if(ms.messages[m]?){
                        tm.messages = ms.messages[m];
                    } else {
                        ms.messages[m] = [];
                    }
                    ms.status[m] += {tpl_uptodate(), checked()};
                    if(!isEmpty([ e | e:error(_,_) <- ms.messages[m] ])){
                        ms.status[m]  += {check_error()};
                    }
                }
                // presave the TModels of the modules in the component
                
                ms = preSaveModule(component, m_imports, m_extends, ms, moduleScopes, tm);
                
                // generate code for the modules in the component
                
                for(str m <- component){
                    <success, pt, ms> = getModuleParseTree(m, ms);
                    if(success){
                        msgs = codgen(m, pt, ms, compilerConfig);
                        ms.messages[m] = msgs;
                        ms.status[m] += {code_generated()};
                    } else {
                        ms.messages[m] += [ error("Cannot get parse tree for module `<m>`", ms.moduleLocs[m]) ];
                    }
                }
                ms = doSaveModule(component, m_imports, m_extends, ms, moduleScopes, compilerConfig);
            }
        }
    } catch ParseError(loc src): {
        for(str mname <- topModuleNames){
            ms.messages[mname] = [ error("Parse error", src) ];
        }
    } catch Message msg: {
        for(str mname <- topModuleNames){
            ms.messages[mname] = [error("During type checking: <msg>", msg.at)];
        }
    }

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
    usedADTs = { unset(tm.facts[l], "alabel") | loc l <- tm.facts, l.path == modulePath, aadt(_,_,_) := tm.facts[l] };
    definedADTs = { unset(the_adt, "alabel") | Define d <- tm.defines, d.defined.path == modulePath, defType(the_adt:aadt(_,_,_)) := d.defInfo };
    usedOrDefinedADTs = usedADTs + definedADTs;
    res = any(loc l <- tm.facts, l.path == importPath, the_adt:aadt(_,_,_) := tm.facts[l], unset(the_adt, "alabel") in usedOrDefinedADTs);
    return res;
}

tuple[set[str], ModuleStatus] loadImportsAndExtends(str moduleName, ModuleStatus ms, Collector c, set[str] added){
    pcfg = ms.pathConfig;
    rel[str,str] contains = ms.strPaths<0,2>;
    for(imp <- contains[moduleName]){
        if(imp notin added){
            if(tpl_uptodate() in ms.status[imp]){
                added += imp;
                <found, tm, ms> = getTModelForModule(imp, ms);
                c.addTModel(tm);
            }
        }
    }
    return <added, ms>;
}

tuple[TModel, ModuleStatus] rascalTModelComponent(set[str] moduleNames, ModuleStatus ms, TypePalConfig config){
                                                        
    pcfg = ms.pathConfig;
    modelName = intercalate(" + ", toList(moduleNames));    
    map[str, Module] namedTrees = ();
    for(str nm <- moduleNames){
        <success, pt, ms> = getModuleParseTree(nm, ms);
        if(success){
            namedTrees[nm] = pt;
        } else {
            ms.messages[nm] += error("Cannot get parse tree for module `<nm>`", ms.moduleLocs[nm]);
        }
    }
    jobStart("RascalCompiler");
    jobStep("RascalCompiler", "Type checking <modelName>"); // TODO: monitor
    if(config.verbose) println("Type checking <modelName>");
    
    c = newCollector(modelName, namedTrees, config);
    c.push(key_pathconfig, pcfg);
    
    rascalPreCollectInitialization(namedTrees, c);
    
    added = {};
    for(str nm <- moduleNames){
        <a, ms> = loadImportsAndExtends(nm, ms, c, added);
        added += a;
    }
  
    for(str nm <- namedTrees){
        collect(namedTrees[nm], c);
    }
    tm = c.run();
    
    tm.paths =  ms.paths;
    
    s = newSolver(namedTrees, tm);
    tm = s.run();
    
    return <tm, ms>;
}

// ---- rascalTModelForName a checker version that works on module names

ModuleStatus rascalTModelForNames(list[str] moduleNames, 
                                  TypePalConfig config, 
                                  CompilerConfig compilerConfig, 
                                  list[Message] (str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, ModuleStatus ms, CompilerConfig compilerConfig) codgen){

   
    pcfg = config.typepalPathConfig;
    mlocs = [];
    for(moduleName <- moduleNames){
        try {
            mlocs += [ getModuleLocation(moduleName, pcfg) ];
        } catch value e: {
            mloc = |unknown:///|(0,0,<0,0>,<0,0>);
            ms = newModuleStatus(pcfg);
            ms.messages[moduleName] = [ error("<e>", mloc) ];
            return ms;
        }
    }
    return rascalTModelForLocs(mlocs, config, compilerConfig, codgen);
}

// ---- checker functions for IDE

// name  of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

list[Message] dummy_compile1(str _qualifiedModuleName, lang::rascal::\syntax::Rascal::Module _M, ModuleStatus _ms, CompilerConfig _compilerConfig)
    = [];
    
list[ModuleMessages] check(list[loc] moduleLocs, PathConfig pcfg, CompilerConfig compilerConfig){
    pcfg1 = pcfg; pcfg1.classloaders = []; pcfg1.javaCompilerPath = [];
    //println("=== check: <moduleLocs>"); iprintln(pcfg1);
    ms = rascalTModelForLocs(moduleLocs, rascalTypePalConfig(pcfg), compilerConfig, dummy_compile1);
    return [ program(ms.moduleLocs[mname], toSet(ms.messages[mname])) | mname <- ms.messages ];
}

list[ModuleMessages] checkAll(loc root, PathConfig pcfg, CompilerConfig compilerConfig){
    return check(toList(find(root, "rsc")), pcfg, compilerConfig);
}

// ---- Convenience check function during development -------------------------
      
map[str, list[Message]] checkModules(list[str] moduleNames, TypePalConfig config, PathConfig pcfg, bool verbose=true) {
    ModuleStatus ms = rascalTModelForNames(moduleNames, config, getRascalCompilerConfig()[verbose=verbose], dummy_compile1);
    tmodels = ms.tmodels;
    return (mname : tmodels[mname].messages | mname <- tmodels, !isEmpty(tmodels[mname].messages));
}

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
