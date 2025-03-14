@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::Checker

/*
    Top level driver for the checker (rascalTModelForLocsNote).
    Note that the checker calls the code generator (given as parameter) when there are no type errors.
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

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::compile::CompileTimeError;
import lang::rascalcore::check::ModuleLocations;
extend lang::rascalcore::check::TestConfigs;

import analysis::typepal::Exception;

import IO;
import List;
import Map;
import Message;
import Node;
import Relation;
import Set;
import String;

import util::Reflective;
import util::FileSystem;
import util::Monitor;
import util::Benchmark;
import analysis::graphs::Graph;

// Duplicate in lang::rascalcore::compile::util::Names, factor out
data PathConfig(
    loc generatedSources=|unknown:///|,
    loc generatedTestSources=|unknown:///|,
    loc resources = |unknown:///|,
    loc testResources =|unknown:///|
);

void rascalPreCollectInitialization(map[str, Tree] _namedTrees, Collector c){

    c.push(patternContainer, "toplevel");
    c.push(key_allow_use_before_def, <|none:///|(0,0,<0,0>,<0,0>), |none:///|(0,0,<0,0>,<0,0>)>);
}

list[Message] validatePathConfigForChecker(PathConfig pcfg, loc mloc) {
    msgs = [];

    if(isEmpty(pcfg.srcs)) msgs += error("PathConfig: `srcs` is empty", mloc);
    for(src <- pcfg.srcs){
        if(!exists(src)) msgs += error("PathConfig `srcs`: <src> does not exist", src);
    }
    for(lb <- pcfg.libs){
        if(!exists(lb)) msgs += warning("PathConfig `libs`: <lb> does not exist (yet)", lb);
    }

    if(!exists(pcfg.resources)) {
        try {
            mkDirectory(pcfg.resources);
        } catch e: {
            msgs += error("PathConfig `resources`: <e>", pcfg.resources);
        }
    }

    return msgs;
}

list[Message] validatePathConfigForCompiler(PathConfig pcfg, loc mloc) {
    msgs = validatePathConfigForChecker(pcfg, mloc);
    if(!exists(pcfg.bin)){
        try {
            mkDirectory(pcfg.bin);
        } catch e: {
            msgs += error("PathConfig `bin`: <e>", pcfg.bin);
        }
     }
     return msgs;
}

// ----  Various check functions  ---------------------------------------------

// Dummy compile function (used when running only the checker)

list[Message] dummy_compile1(str _qualifiedModuleName, lang::rascal::\syntax::Rascal::Module _M, map[str,TModel] _transient_tms, ModuleStatus _ms, RascalCompilerConfig _compilerConfig)
    = [];

// rascalTModelForLocs is the basic work horse

ModuleStatus rascalTModelForLocs(
    list[loc] mlocs,
    RascalCompilerConfig compilerConfig,
    list[Message](str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, map[str,TModel] transient_tms, ModuleStatus ms, RascalCompilerConfig compilerConfig) codgen
){
    pcfg = compilerConfig.typepalPathConfig;
   
    if(compilerConfig.logPathConfig) { iprintln(pcfg); }

    msgs = validatePathConfigForChecker(pcfg, mlocs[0]);

    ModuleStatus ms = newModuleStatus(compilerConfig);
    
    mnames = 
        for(mloc <- mlocs){
            if(!exists(mloc)){
                msgs += error("<mloc> does not exist", mloc);
                append  "LocationDoesNotExist: <mloc>";
            } else {
                try {
                    append getRascalModuleName(mloc, pcfg);
                } catch e: {
                    append "NoModuleNameFound: <mloc>";
                    msgs += error("No module name found for <mloc>", mloc);
                }
            }
        };

    if(allModulesHaveValidTpls(mlocs, pcfg)){
        <compatibleLibs, ms> = libraryDependenciesAreCompatible(mlocs, ms);

        if(compatibleLibs && uptodateTPls(mlocs, mnames, pcfg)){
            for (i <- index(mlocs)) {
                <found, tm, ms> = getTModelForModule(mnames[i], ms, convert=true);
                if(!found){
                    throw "TModel for <mnames[i]> not found (no changes)";
                }
            }
         return ms;
        }
    }

    for (int i <- index(mlocs)) {
        mloc = mlocs[i];
        mname = mnames[i];
        if(isModuleLocationInLibs(mname, mloc, pcfg)){
            ms.status[mname] ? {} += {rsc_not_found()};
        }
       
        ms.moduleLocs[mname] = mloc;
        msgs += toList(ms.messages[mname] ? {});
    }

    str jobName = "";

    topModuleNames = toSet(mnames);
    try {
        ms = getImportAndExtendGraph(topModuleNames, ms);

        // if(/error(_,_) := ms.messages){

        //     return clearTModelCache(ms);
        // }

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
            for(str singleton <- singletons){
                module2component[singleton] = {singleton};
            }
        }

        map[str, loc] moduleScopes = ();
        map[str,str] path2module = (ms.moduleLocs[mname].path : mname | mname <- ms.moduleLocs);
        mi = 0;
        nmodules = size(ordered);

        jobName = "Compiling <intercalate(" ", [*topModuleNames])>";
        jobStart(jobName, totalWork=nmodules);

        while(mi < nmodules) {
          
            component = module2component[ordered[mi]];
            //println("while: <component>,  Ambiguity: <ms.status["analysis::grammars::Ambiguity"] ? {}>");
            jobStep(jobName, intercalate(" + ", [*component]), work=size(component));

            recheck = !all(m <- component, ms.status[m]?, (tpl_uptodate() in ms.status[m] || checked() in ms.status[m]));
            for(m <- component){
                mi += 1;
                if(!recheck){
                    if(tpl_uptodate() notin ms.status[m]){
                        <found, tm, ms> = getTModelForModule(m, ms, convert=false);
                        if(found){
                            ms.status[m] += {tpl_uptodate(), checked()};
                        }
                    }
               }
            }

            compatible_with_all_imports = true;
            any_tpl_outdated = any(m <- component, tplOutdated(m, pcfg));
            if(any_tpl_outdated){
                for(m <- component){
                    ms.status[m] -= {tpl_uptodate(), checked()};
                    ms.status[m] += {rsc_changed()};
                }
            } else {
                for(m <- component){
                    m_compatible = false;
                    <found, tm, ms> = getTModelForModule(m, ms, convert=false);
                    if(found && !tplOutdated(m, pcfg)){
                        imports_extends_m = imports_and_extends[m];
                   
                        <m_compatible, ms> = importsAndExtendsAreBinaryCompatible(tm, imports_extends_m, ms);
                        if(m_compatible){
                            ms.status[m] += {tpl_uptodate(), checked(), bom_update_needed()};
                        }
                    }
                    compatible_with_all_imports = compatible_with_all_imports && m_compatible;
                }
            }

            any_rsc_changed = any(m <- component, rsc_changed() in ms.status[m]);
            any_from_lib = any(m <- component, rsc_not_found() in ms.status[m]);
            all_tmodels_uptodate = true;
            for(m <- component){
                if(tpl_uptodate() notin ms.status[m] && checked() notin ms.status[m])
                    all_tmodels_uptodate = false;
            }
            recheckCond = !any_from_lib && (!compatible_with_all_imports || any_rsc_changed || !all_tmodels_uptodate);

             if(recheckCond){
                if(ms.compilerConfig.verbose){
                    println("recheck <component>: compatible_with_all_imports: <compatible_with_all_imports>, any_rsc_changed: <any_rsc_changed>, all_tmodels_uptodate: <all_tmodels_uptodate>");
                }
                
                <tm, ms> = rascalTModelComponent(component, ms);
                moduleScopes += getModuleScopes(tm);
                map[str,TModel] tmodels_for_component = ();
                map[str,set[str]] m_imports = ();
                map[str,set[str]] m_extends = ();
                for(m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
                    imports =  { imp | <m1, importPath(), imp> <- ms.strPaths, m1 == m };
                    m_imports[m] =  imports;
                    extends = { ext | <m1, extendPath(), ext > <- ms.strPaths, m1 == m };
                    m_extends[m] = extends;
                    invertedExtends = ms.strPaths<2,0>;
                    if(compilerConfig.warnUnused){
                        // Look for unused imports or exports
                        usedModules = {path2module[l.path] | loc l <- range(tm.useDef), tm.definitions[l].idRole != moduleId(), path2module[l.path]?};
                        usedModules += {*invertedExtends[um] | um <- usedModules}; // use of an extended module via import
                        list[Message] imsgs = [];
                        <success, pt, ms> = getModuleParseTree(m, ms);
                        if(success){
                            if(compilerConfig.infoModuleChecked){
                                imsgs += [info("Checked <m>", pt.header.name@\loc)];
                            }
                            check_imports:
                            for(imod <- pt.header.imports, imod has \module){
                                iname = unescape("<imod.\module.name>");
                                if(!ms.status[iname]?){
                                    ms.status[iname] = {};
                                }
                                if(iname notin usedModules){
                                   if(iname == "ParseTree" && implicitlyUsesParseTree(ms.moduleLocs[m].path, tm)){
                                     continue check_imports;
                                   }
                                   if(ms.moduleLocs[iname]? && ms.moduleLocs[m]? && implicitlyUsesLayoutOrLexical(ms.moduleLocs[m].path, ms.moduleLocs[iname].path, tm)){
                                    continue check_imports;
                                   }
                                   if(ms.moduleLocs[iname]? && ms.moduleLocs[m]? && usesOrExtendsADT(ms.moduleLocs[m].path, ms.moduleLocs[iname].path, tm)){
                                    continue check_imports;
                                   }
                                   if((iname in component || checked() in ms.status[iname]) && rsc_not_found() notin ms.status[iname]){
                                       if(imod is \default){
                                         imsgs += warning("Unused import of `<iname>`", imod@\loc);
                                       } //else { //TODO: maybe add option to turn off info messages?
                                         //imsgs += info("Extended module `<iname>` is unused in the current module", imod@\loc);
                                       //}
                                   }
                                }
                            }
                            tm.messages += imsgs;
                        }
                    }
                    if(ms.messages[m]?){
                        tm.messages += toList(ms.messages[m]);
                    }
                    ms.messages[m] ? {} += toSet(tm.messages);

                    ms.status[m] += {tpl_uptodate(), checked()};
                    if(!isEmpty([ e | e:error(_,_) <- ms.messages[m] ])){
                        ms.status[m]  += {check_error()};
                    }
                }
                // prepare the TModels of the modules in this component for compilation

                <transient_tms, ms> = prepareForCompilation(component, m_imports, m_extends, ms, moduleScopes, tm);

                // generate code for the modules in this component

                for(str m <- component, MStatus::ignored() notin ms.status[m]){
                    <success, pt, ms> = getModuleParseTree(m, ms);
                    if(success){
                        msgs = codgen(m, pt, transient_tms, ms, compilerConfig);
                        ms.messages[m] += toSet(msgs);
                        ms.status[m] += {code_generated()};
                    }
                }
                ms = doSaveModule(component, m_imports, m_extends, ms, moduleScopes, transient_tms, compilerConfig);
                for(m <- component){
                    ms.status[m] -= {rsc_changed()};
                    ms.status[m] += {tpl_uptodate()};
                }
            } else {
                 for(m <- component){  
                    ms.status[m] += bom_update_needed();
                 }
            }
        }
    } catch ParseError(loc src): {
        for(str mname <- topModuleNames){
            ms.messages[mname] = { error("Parse error", src) };
        }
    } catch rascalTplVersionError(str txt):{
        for(str mname <- topModuleNames){
            ms.messages[mname] = { error("<txt>", ms.moduleLocs[mname] ? |unknown:///|) };
        }
    } catch Message msg: {
        for(str mname <- topModuleNames){
            ms.messages[mname] = { error("During type checking: <msg>", msg.at) };
        }
    } catch rascalBinaryNeedsRecompilation(str moduleName, Message msg): {
        ms.messages[moduleName] = { msg };
    }

    jobEnd(jobName);
    return clearTModelCache(ms);
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
                <found, tm, ms> = getTModelForModule(imp, ms, convert=true);
                try {
                    c.addTModel(tm);
                } catch wrongTplVersion(str reason): {
                    ms.messages[imp] ? {} += { Message::error(reason, ms.moduleLocs[imp]) };
                }
            }
        }
    }
    return <added, ms>;
}

tuple[TModel, ModuleStatus] rascalTModelComponent(set[str] moduleNames, ModuleStatus ms){

    pcfg = ms.pathConfig;
    compilerConfig = ms.compilerConfig;
    modelName = intercalate(" + ", toList(moduleNames));
    map[str, Module] namedTrees = ();
    for(str nm <- moduleNames){
        //ms.status[nm] = {};
        ms.messages[nm] = {};
        mloc = |unknown:///|(0,0,<0,0>,<0,0>);
        try {
            mloc = getRascalModuleLocation(nm, pcfg);
        } catch e: {
            err = error("Cannot get location for <nm>: <e>", mloc);
            ms.messages[nm] = { err };
            ms.status[nm] += { rsc_not_found() };
            tm = tmodel(modelName=nm, messages=[ err ]);
            ms = addTModel(nm, tm, ms);
            return <tm, ms>;
        }
        if(mloc.extension != "rsc" || isModuleLocationInLibs(nm, mloc, pcfg)){
            continue;
        }
        <success, pt, ms> = getModuleParseTree(nm, ms);
        if(success){
            tagsMap = getTags(pt.header.tags);

            if(ignoreCompiler(tagsMap)) {
                    ms.messages[nm] ? {} += { Message::info("Ignoring module <nm>", pt@\loc) };
                    ms.status[nm] += MStatus::ignored();
            } else {
                namedTrees[nm] = pt;
            }
        }
        //else {
        //    ms.messages[nm] += error("Cannot get parse tree for module `<nm>`", ms.moduleLocs[nm]);
        //}
    }
    if(!isEmpty(namedTrees)){
        if(compilerConfig.verbose) { println("Checking ... <modelName>"); }

        start_check = cpuTime();
        resetClosureCounter();
        c = newCollector(modelName, namedTrees, compilerConfig);
        c.push(key_pathconfig, pcfg);

        rascalPreCollectInitialization(namedTrees, c);

        added = {};
        for(str nm <- domain(namedTrees)){
            <a, ms> = loadImportsAndExtends(nm, ms, c, added);
            added += a;
        }

        for(str nm <- namedTrees){
            collect(namedTrees[nm], c);
        }
        tm = c.run();

        tm.paths =  ms.paths;

        if(!isEmpty(namedTrees)){
            s = newSolver(namedTrees, tm);
            tm = s.run();
        }
        tm.usesPhysicalLocs = true;
        // for(mname <- moduleNames){
        //     ms.messages[mname] = tm.messages;
        // }
        //iprintln(tm.messages);

        check_time = (cpuTime() - start_check)/1000000;

        if(compilerConfig.verbose) { println("Checked .... <modelName> in <check_time> ms"); }
        return <tm, ms>;
    } else {
        ms.status[modelName]? {} += { tpl_saved() };
        <found, tm, ms> = getTModelForModule(modelName, ms, convert=false);
        return <tm, ms>;
    }
}

// ---- rascalTModelForName a checker version that works on module names

ModuleStatus rascalTModelForNames(list[str] moduleNames,
                                  RascalCompilerConfig compilerConfig,
                                  list[Message] (str qualifiedModuleName, lang::rascal::\syntax::Rascal::Module M, map[str,TModel] transient_tms, ModuleStatus ms, RascalCompilerConfig compilerConfig) codgen){

    pcfg = compilerConfig.typepalPathConfig;
    mlocs = [];
    for(moduleName <- moduleNames){
        try {
            mlocs += [ getRascalModuleLocation(moduleName, pcfg) ];
        } catch value e: {
            mloc = |unknown:///|(0,0,<0,0>,<0,0>);
            err = error("Cannot get location for <moduleName>: <e>", mloc);
            ms = newModuleStatus(compilerConfig);
            ms.messages[moduleName] = { err };
            return ms;
        }
    }
    return rascalTModelForLocs(mlocs, compilerConfig, codgen);
}

bool uptodateTPls(list[loc] candidates, list[str] mnames, PathConfig pcfg){
    for(int i <- index(candidates)){
        mloc = candidates[i];
        <found, tpl> = getTPLReadLoc(mnames[i], pcfg);
        if(!found || lastModified(mloc) > lastModified(tpl)){
            return false;
        }
    }
    return true;
}

bool allModulesHaveValidTpls(list[loc] candidates, PathConfig pcfg){
    for(srcdir <- pcfg.srcs){
        for(loc mloc <- find(srcdir, "rsc")){
            try {
                mname = getRascalModuleName(mloc, pcfg);
                <found, tpl> = getTPLReadLoc(mname, pcfg);
                if(found && (mloc notin candidates) && (lastModified(mloc) > lastModified(tpl))){
                    return false;
                }
            } catch e:{
                return false;
            }
        }
    }
    return true;
}

tuple[bool, ModuleStatus] libraryDependenciesAreCompatible(list[loc] candidates, ModuleStatus ms){
    pcfg = ms.pathConfig;
    for(candidate <- candidates){
        mname = getRascalModuleName(candidate, pcfg);
        <found, tm, ms> = getTModelForModule(mname, ms, convert=false);
        imports_and_extends = ms.strPaths<0,2>[mname];
        <compatible, ms> = importsAndExtendsAreBinaryCompatible(tm, imports_and_extends, ms);
        if(!compatible){
            return <false, ms>;
        }
    }
    return <true, ms>;
}

// ---- checker functions for IDE

// name  of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

list[ModuleMessages] check(list[loc] moduleLocs, RascalCompilerConfig compilerConfig){
    pcfg1 = compilerConfig.typepalPathConfig;
    compilerConfig.typepalPathConfig = pcfg1;
    ms = rascalTModelForLocs(moduleLocs, compilerConfig, dummy_compile1);
    messagesNoModule = {};
    for(mname <- ms.messages, !ms.moduleLocs[mname]?){
        messagesNoModule += ms.messages[mname];
    }   
    return [ program(ms.moduleLocs[mname], ms.messages[mname] + messagesNoModule) | mname <- ms.messages, ms.moduleLocs[mname] ?  ];
}

list[ModuleMessages] checkAll(loc root, RascalCompilerConfig compilerConfig){
    return check(toList(find(root, "rsc")), compilerConfig);
}

// ---- Convenience check function during development -------------------------

map[str, list[Message]] checkModules(list[str] moduleNames, RascalCompilerConfig compilerConfig) {
    ModuleStatus ms = rascalTModelForNames(moduleNames, compilerConfig, dummy_compile1);
    return  (mname : toList(msgs) | mname <- ms.messages, msgs := ms.messages[mname], !isEmpty(msgs));
}

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
