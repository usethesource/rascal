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
    Top level driver for the checker (rascalTModelForLocs).
    Note that the checker calls the code generator (given as parameter) when there are no type errors.
*/

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
import lang::rascalcore::CompilerPathConfig;

void rascalPreCollectInitialization(map[str, Tree] _namedTrees, Collector c){

    c.push(patternContainer, "toplevel");
    c.push(key_allow_use_before_def, <|none:///|(0,0,<0,0>,<0,0>), |none:///|(0,0,<0,0>,<0,0>)>);
}

set[Message] validatePathConfigForChecker(PathConfig pcfg, loc mloc) {
    msgs = {};

    if(isEmpty(pcfg.srcs)) msgs += error("PathConfig: `srcs` is empty", mloc);
    for(src <- pcfg.srcs){
        if(!exists(src)) msgs += error("PathConfig `srcs`: <src> does not exist", src);
    }
    for(lb <- pcfg.libs){
        if(!exists(lb)) msgs += warning("PathConfig `libs`: <lb> does not exist (yet)", lb);
    }

    if(pcfg.generatedResources != |unknown:///| && !exists(pcfg.generatedResources)) {
        try {
            mkDirectory(pcfg.generatedResources);
        } catch e: {
            msgs += error("PathConfig `generatedResources`: <e>", pcfg.generatedResources);
        }
    }
    return msgs;
}

set[Message] validatePathConfigForCompiler(PathConfig pcfg, loc mloc) {
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

bool errorsPresent(TModel tmodel) = errorsPresent(tmodel.messages);
bool errorsPresent(set[Message] msgs) = errorsPresent(toList(msgs));
bool errorsPresent(list[Message] msgs) = !isEmpty([ e | e:error(_,_) <- msgs ]);

// ----  Various check functions  ---------------------------------------------

// Dummy compile function (used when running only the checker)

list[Message] dummy_compile1(MODID _moduleId, lang::rascal::\syntax::Rascal::Module _M, map[MODID,TModel] _transient_tms, ModuleStatus _ms, RascalCompilerConfig _compilerConfig)
    = [];

// rascalTModelForLocs is the basic work horse
// Essential assumption: all changed modules are included in mlocs.
// If this is not the case, changed modules not in mlocs will not be checked.

ModuleStatus rascalTModelForLocs(
    list[loc] mlocs,
    RascalCompilerConfig compilerConfig,
    list[Message](MODID moduleId, lang::rascal::\syntax::Rascal::Module M, map[MODID,TModel] transient_tms, ModuleStatus ms, RascalCompilerConfig compilerConfig) codgen
){
    pcfg = compilerConfig.typepalPathConfig;
    
    if(compilerConfig.logPathConfig) { iprintln(pcfg); }

    ModuleStatus ms = newModuleStatus(compilerConfig);

    if(isEmpty(mlocs)) return ms;

    set[Message] msgs = validatePathConfigForChecker(pcfg, mlocs[0]);

    list[str] mnames = [];
    mids = 
        for(mloc <- mlocs){
            if(isRascalLogicalLoc(mloc)){
                mnames += moduleId2moduleName(mloc);
                append mloc;
            } else 
            if(exists(mloc)){
                try {
                    mname = getRascalModuleName(mloc, pcfg);
                    mnames += mname;
                    append moduleName2moduleId(mname);
                } catch _: {
                    msgs += error("No module name found for <mloc>", mloc);
                }
            } else {
                msgs += error("<mloc> does not exist", mloc);
            }
        };

    if(size(mlocs) != size(mnames)){ // not all mlocs could be mapped to a module
        for(mid <- mids){
             ms.messages[mid] = msgs;
        }
        if(errorsPresent(msgs)){
            pcfg.messages += toList(msgs);
            ms.pathConfig = pcfg;
            ms.compilerConfig.typepalPathConfig.messages += pcfg.messages;
        }
        return ms;
    }
 
    <compatibleLibs, ms> = libraryDependenciesAreCompatible(mids, ms);

    if(compatibleLibs && uptodateTPls(mlocs, mnames, pcfg)){
        for (i <- index(mids)) {
            <found, tm, ms> = getTModelForModule(mids[i], ms);
            if(!found){
                throw "TModel for <mnames[i]> not found (no changes)";
            }
        }
        return ms;
    }

    for (int i <- index(mlocs)) {
        mid = mids[i];
        mloc = mlocs[i];
        mname = mnames[i];
        if(isModuleLocationInLibs(mloc, pcfg)){
            ms.status[mloc] ? {} += {rsc_not_found()};
        }
       
        ms.moduleLocs[mid] = mloc;
        msgs += (ms.messages[mid] ? {});
    }

    str jobName = "";

    ms.changedModules = topModuleIds = toSet(mids);
    topModuleNames = mnames;
    try {
        ms = getImportAndExtendGraph(topModuleIds, ms);

        // if(/error(_,_) := ms.messages){
        //     return clearTModelCache(ms);
        // }

        imports_and_extends = ms.paths<0,2>;
        <components, sorted> = stronglyConnectedComponentsAndTopSort(imports_and_extends);
        map[MODID, set[MODID]] module2component = (m : c | c <- components, m <- c);

        list[MODID] ordered = [];

        if(isEmpty(sorted)){
            ordered = toList(topModuleIds);
            for(MODID topModuleId <- topModuleIds){
                module2component[topModuleId] = {topModuleId};
            }
        } else {
            ordered = reverse(sorted);
            singletons = toList(topModuleIds - toSet(ordered));
            ordered += singletons;
            for(MODID singleton <- singletons){
                module2component[singleton] = {singleton};
            }
        }

        // map[loc, loc] moduleScopes = ();
        //map[str,loc] path2module = (ms.moduleLocs[mname].path : mname | mname <- ms.moduleLocs);
        mi = 0;
        nmodules = size(ordered);

        jobName = "Compiling <intercalate(" ", [*topModuleNames])>";
        jobStart(jobName, totalWork=nmodules);

        while(mi < nmodules) {
          
            component = module2component[ordered[mi]];
            sizeComponent = size(component);
            componentNames =
                for(c <- component){
                    cstr = "<c.path>";
                    append replaceAll(cstr[0] == "/" ? cstr[1..] : cstr, "/", "::");         
                };
            
            jobStep(jobName, intercalate(" + ", [*componentNames]), work=size(componentNames));

            recheck = !all(m <- component, m in ms.status, (tpl_uptodate() in ms.status[m] || checked() in ms.status[m]));
            for(m <- component){
               
                if(m notin ms.status){
                    ms.status[m] = {};
                }
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
                    <found, tm, ms> = getTModelForModule(m, ms);
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
                // if(ms.compilerConfig.verbose){
                //     println("recheck <component>: compatible_with_all_imports: <compatible_with_all_imports>, any_rsc_changed: <any_rsc_changed>, all_tmodels_uptodate: <all_tmodels_uptodate>");
                // }
                
                <tm, ms> = rascalTModelComponent(component, ms);
                // moduleScopes += getModuleScopes(tm);
                map[str,TModel] tmodels_for_component = ();
                map[MODID,set[MODID]] m_imports = ();
                map[MODID,set[MODID]] m_extends = ();
                for(m <- component, rsc_not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
                    imports =  { imp | <m1, importPath(), imp> <- ms.paths, m1 == m, MStatus::ignored() notin ms.status[imp]};
                    m_imports[m] =  imports;
                    extends = { ext | <m1, extendPath(), ext > <- ms.paths, m1 == m, MStatus::ignored() notin ms.status[ext] };
                    m_extends[m] = extends;
                    invertedExtends = ms.paths<2,0>;
                    if(compilerConfig.warnUnused){
                        // Look for unused imports or extends
                        //usedModules = {path2module[l.path] | loc l <- range(tm.useDef), tm.definitions[l].idRole == moduleId(), path2module[l.path]?};
                        usedModules = {l | loc l <- range(tm.useDef), tm.definitions[l].idRole == moduleId()};
                        usedModules += {*invertedExtends[um] | um <- usedModules}; // use of an extended module via import
                        list[Message] imsgs = [];
                        <success, pt, ms> = getModuleParseTree(m, ms);
                        if(success){
                            if(compilerConfig.infoModuleChecked){
                                imsgs += [info("Checked <moduleId2moduleName(m)>", pt.header.name@\loc)];
                            }
                            check_imports:
                            for(imod <- pt.header.imports, imod has \module){
                                iname = unescape("<imod.\module.name>");
                                inameId = moduleName2moduleId(iname);
                                if(!ms.status[inameId]?){
                                    ms.status[inameId] = {};
                                }
                                if(inameId notin usedModules){
                                   if(iname == "ParseTree" && implicitlyUsesParseTree(ms.moduleLocs[m].path, tm)){
                                     continue check_imports;
                                   }
                                   if(ms.moduleLocs[inameId]? && ms.moduleLocs[m]? && implicitlyUsesLayoutOrLexical(ms.moduleLocs[m].path, ms.moduleLocs[inameId].path, tm)){
                                    continue check_imports;
                                   }
                                   if(ms.moduleLocs[inameId]? && ms.moduleLocs[m]? && usesOrExtendsADT(ms.moduleLocs[m].path, ms.moduleLocs[inameId].path, tm)){
                                    continue check_imports;
                                   }
                                   if((inameId in component || checked() in ms.status[inameId]) && rsc_not_found() notin ms.status[inameId]){
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
                    if(errorsPresent(ms.messages[m])){
                        ms.status[m]  += {check_error()};
                    }
                }
                // prepare the TModels of the modules in this component for compilation

                <transient_tms, ms> = prepareForCompilation(component, m_imports, m_extends, ms, tm);

                // generate code for the modules in this component

                for(MODID m <- component, MStatus::ignored() notin ms.status[m]){
                    <success, pt, ms> = getModuleParseTree(m, ms);
                    if(success){
                        lmsgs = codgen(m, pt, transient_tms, ms, compilerConfig);
                        ms.messages[m] += toSet(lmsgs);
                        ms.status[m] += errorsPresent(lmsgs) ? {code_generation_error()} : {code_generated()};
                    }
                }
                ms = doSaveModule(component, m_imports, m_extends, ms, transient_tms, compilerConfig);
                for(m <- component){
                    ms.status[m] -= {rsc_changed()};
                    ms.status[m] += {tpl_uptodate()};
                }
            } else {
                ;//  for(m <- component){  
                //     ms.status[m] += bom_update_needed();
                //  }
            }
        }
    } catch ParseError(loc src): {
        for(MODID mid <- topModuleIds){
            ms.messages[mid] = { error("Parse error", src) };
        }
    } catch rascalTplVersionError(str txt):{
        for(MODID mid <- topModuleIds){
            ms.messages[mid] = { error("<txt>", ms.moduleLocs[mid] ? |unknown:///|) };
        }
    } catch Message msg: {
        for(MODID mid <- topModuleIds){
            ms.messages[mid] = { error("During type checking: <msg>", msg.at) };
        }
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

tuple[set[MODID], ModuleStatus] loadImportsAndExtends(set[MODID] moduleIds, ModuleStatus ms, Collector c, set[MODID] added){
    pcfg = ms.pathConfig;
    for(<from, pathRole, imp> <- ms.paths, from in moduleIds){
        if(imp notin added, imp notin moduleIds){
            if(tpl_uptodate() in ms.status[imp]){
                added += imp;
                <found, tm, ms> = getTModelForModule(imp, ms);
                try {
                    if(pathRole == importPath()){
                        tm.defines = {d | d <- tm.defines, d.idRole == moduleVariableId() ==> d.defInfo.vis == publicVis() };
                    }
                    c.addTModel(tm);
                } catch wrongTplVersion(str reason): {
                    ms.messages[imp] ? {} += { Message::error(reason, ms.moduleLocs[imp]) };
                }
            }
        }
    }
    return <added, ms>;
}

tuple[TModel, ModuleStatus] rascalTModelComponent(set[MODID] moduleIds, ModuleStatus ms){
    pcfg = ms.pathConfig;
    compilerConfig = ms.compilerConfig;
    modelNames = [moduleId2moduleName(moduleId) | moduleId <- moduleIds];
    modelName = intercalate(" + ", modelNames);
    map[MODID, Module] idTrees = ();
    for(MODID mid <- moduleIds){
        mname = moduleId2moduleName(mid);
        //ms.status[mid] = {};
        //ms.messages[mid] = {};
        ms = removeTModel(mid, ms);
        mloc = |unknown:///|(0,0,<0,0>,<0,0>);
        try {
            mloc = getRascalModuleLocation(mid, ms);
        } catch Message err: {
            ms.messages[mid] = { err };
            ms.status[mid] += { rsc_not_found() };
            tm = tmodel(modelName=mname, messages=[ err ]);
            ms = addTModel(mid, tm, ms);
            return <tm, ms>;
        }
        if(!isRascalLogicalLoc(mloc) && (mloc.extension != "rsc" || isModuleLocationInLibs(mloc, pcfg))){
            continue;
        }
        <success, pt, ms> = getModuleParseTree(mid, ms);
        if(success){
            tagsMap = getTags(pt.header.tags);

            if(ignoreCompiler(tagsMap)) {
                    ms.messages[mid] ? {} += { Message::info("Ignoring module <mid>", pt@\loc) };
                    ms.status[mid] += MStatus::ignored();
            }
            idTrees[mid] = pt;
        }
        //else {
        //    ms.messages[mid] += error("Cannot get parse tree for module `<mid>`", ms.moduleLocs[mid]);
        //}
    }
    if(!isEmpty(idTrees)){
        if(compilerConfig.verbose) { println("Checking ... <modelName>"); }

        start_check = cpuTime();
        resetClosureCounter();
        namedTrees = (moduleId2moduleName(mid) : idTrees[mid] | mid <- idTrees);
        c = newCollector(modelName, namedTrees, compilerConfig);
        c.push(key_pathconfig, pcfg);

        rascalPreCollectInitialization(namedTrees, c);
        <added, ms> = loadImportsAndExtends(domain(idTrees), ms, c, {});
        for(str nm <- namedTrees){
            collect(namedTrees[nm], c);
        }
        tm = c.run();

        tm.paths =  ms.paths;

        if(!isEmpty(namedTrees)){
            s = newSolver(namedTrees, tm);
            tm = s.run();
        }

        for(mid <- moduleIds){
            ms.messages[mid] ? {} += toSet(tm.messages);
        }
        //iprintln(tm.messages);

        check_time = (cpuTime() - start_check)/1000000;

        if(compilerConfig.verbose) { println("Checked .... <modelName> in <check_time> ms"); }
        return <tm, ms>;
    } else {
        oneOfComponent = getOneFrom(moduleIds);
        ms.status[oneOfComponent]? {} += { tpl_saved() };            // TODO check this, when is this executed?
        <found, tm, ms> = getTModelForModule(oneOfComponent, ms);
        return <tm, ms>;
    }
}

// ---- rascalTModelForName a checker version that works on module names

ModuleStatus rascalTModelForNames(list[str] moduleNames,
                                  RascalCompilerConfig compilerConfig,
                                  list[Message] (MODID moduleId, lang::rascal::\syntax::Rascal::Module M, map[MODID,TModel] transient_tms, ModuleStatus ms, RascalCompilerConfig compilerConfig) codgen){

    pcfg = compilerConfig.typepalPathConfig;
    mlocs = [];
    for(moduleName <- moduleNames){
        try {
            mlocs += [ getRascalModuleLocation(moduleName, pcfg) ];
        } catch Message err: {
            ms = newModuleStatus(compilerConfig);
            ms.messages[moduleName2moduleId(moduleName)] = { err };
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

tuple[bool, ModuleStatus] libraryDependenciesAreCompatible(list[MODID] candidates, ModuleStatus ms){
    pcfg = ms.pathConfig;
    for(candidate <- candidates){
        <found, tm, ms> = getTModelForModule(candidate, ms);
        if(found){ // TODO: needed?
            imports_and_extends = ms.paths<0,2>[candidate];
            <compatible, ms> = importsAndExtendsAreBinaryCompatible(tm, imports_and_extends, ms);
            if(!compatible){
                return <false, ms>;
            }
        }
    }
    return <true, ms>;
}

// ---- checker functions for IDE

// name  of the production has to mirror the Kernel compile result
data ModuleMessages = program(loc src, set[Message] messages);

// Essential assumption: all changed modules are included in moduleLocs.
// If this is not the case, changed modules not in mlocs will not be checked.

list[ModuleMessages] check(list[loc] moduleLocs, RascalCompilerConfig compilerConfig){
    pcfg1 = compilerConfig.typepalPathConfig;
    compilerConfig.typepalPathConfig = pcfg1;
    ms = rascalTModelForLocs(moduleLocs, compilerConfig, dummy_compile1);

    return reportModuleMessages(ms);
}

list[ModuleMessages] reportModuleMessages(ModuleStatus ms){
    moduleIds = domain(ms.moduleLocs);
    messagesNoModule = {*ms.messages[mid] | MODID mid <- ms.messages, (mid notin moduleIds || mid notin ms.moduleLocs)} + toSet(ms.pathConfig.messages);
    msgs = [ program(ms.moduleLocs[mid], (ms.messages[mid] ? {}) + messagesNoModule) | MODID mid <- moduleIds ];
    if(isEmpty(msgs) && !isEmpty(messagesNoModule)){
        msgs = [ program(|unknown:///|, messagesNoModule) ];
    }
    return msgs;
}

list[ModuleMessages] checkAll(loc root, RascalCompilerConfig compilerConfig){
    return check(toList(find(root, "rsc")), compilerConfig);
}

@synopsis{General commandline interface to the Rascal static checker}
@benefits{
* The keyword fields of the main function generated commandline options, like `-libs` and `-srcs`
}
@pitfalls{
* This interface is a strong stable contract between the checker and the rascal-maven-plugin. If 
we remove or rename keyword fields here, then they the client code must be adapted as well.
}
int main(
    list[loc] modules             = [],  // dirty modules to check 
    PathConfig pcfg               = pathConfig(),
    bool logPathConfig            = false,
    bool logImports               = false,
    bool verbose                  = false,
    bool logWrittenFiles          = false,
    bool warnUnused               = true,
    bool warnUnusedFormals        = true,
    bool warnUnusedVariables      = true,
    bool warnUnusedPatternFormals = true,
    bool infoModuleChecked        = false,
    bool errorsAsWarnings         = false,
    bool warningsAsErrors         = false
) {
    if (verbose) {
        println("PathConfig:");
        iprintln(pcfg);
        println("Dirty modules:");
        iprintln(modules);
    }

    // resources should end up in the target binary folder
    // if we want something intermediary instead, we'll have to copy
    // them there at some point.
    pcfg.generatedResources     = pcfg.bin;
    pcfg.generatedTestResources = pcfg.bin;
    pcfg.generatedSources       = pcfg.projectRoot + "gen/java";
    pcfg.generatedTestSources   = pcfg.projectRoot + "gen/java";

    rascalConfig = rascalCompilerConfig(pcfg,
        logPathConfig            = logPathConfig,
        logImports               = logImports,
        verbose                  = verbose,
        logWrittenFiles          = logWrittenFiles,
        warnUnused               = warnUnused,
        warnUnusedFormals        = warnUnusedFormals,
        warnUnusedVariables      = warnUnusedVariables,
        warnUnusedPatternFormals = warnUnusedPatternFormals,
        infoModuleChecked        = infoModuleChecked
    );

    list[ModuleMessages] messages = [];
    
    if (modules == []) {
        //messages = [info("No modules to check.", |unknown:///|)];
        return 0;
    }
    else {
        messages = check(modules, rascalConfig);
    }

    flatMessages = [*msgs | program(_, msgs) <- messages];

    return mainMessageHandler(flatMessages, projectRoot=pcfg.projectRoot, errorsAsWarnings=errorsAsWarnings, warningsAsErrors=warningsAsErrors);
}

// ---- Convenience check function during development -------------------------

list[ModuleMessages] checkModules(list[str] moduleNames, RascalCompilerConfig compilerConfig) {
    ModuleStatus ms = rascalTModelForNames(moduleNames, compilerConfig, dummy_compile1);
    return reportModuleMessages(ms);
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
