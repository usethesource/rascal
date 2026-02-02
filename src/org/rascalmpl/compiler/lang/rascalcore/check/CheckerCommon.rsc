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
module lang::rascalcore::check::CheckerCommon

/*
    A convenience module that helps to break cycles in the import structure
    Also introduces ModuleStatus that represents all the relevant information about currently processed modules.
*/

extend lang::rascalcore::check::CollectType;
extend lang::rascalcore::check::ComputeType;
extend lang::rascalcore::check::SyntaxGetters;
extend analysis::typepal::FailMessage;

extend lang::rascalcore::check::BasicRascalConfig;
extend lang::rascalcore::check::ModuleLocations;
extend lang::rascalcore::CompilerPathConfig;
extend lang::rascalcore::check::LogicalLocations;

extend analysis::typepal::Collector;

extend lang::rascal::\syntax::Rascal;
import DateTime;
import Exception;
import IO;
import List;
import ListRelation;
import Location;
import Map;
import Set;
import String;
import ValueIO;
import util::Reflective;

import lang::rascalcore::compile::util::Names; // TODO: refactor, this is an undesired dependency on compile
import lang::rascalcore::compile::CompileTimeError;

void checkSupportedByParserGenerator(Tree t, Collector c){
    c.require("implemented by parsergenerator", t, [t], void(Solver s){
        tp = s.getType(t);
        if(isNonParameterizedNonTerminalType(tp)) return;
        s.report(info(t, "%t is possibly not yet supported by parsergenerator", tp));
    });
 }



data MStatus =
      rsc_not_found()
    | tpl_not_found()
    | rsc_changed()
    | parsed()
    | parse_error()
    | module_dependencies_extracted()
    | checked()
    | check_error()
    | code_generated()
    | code_generation_error()
    | tpl_uptodate()
    | tpl_saved()
    | ignored()
    | bom_update_needed()
    ;

data ModuleStatus =
    moduleStatus(
      rel[MODID, PathRole, MODID] paths,
      map[MODID, Module] parseTrees,
      list[MODID] parseTreeLIFO,
      map[MODID, TModel] tmodels,
      list[MODID] tmodelLIFO,
      set[MODID] changedModules,
      map[MODID,loc] moduleLocs,
      map[MODID,datetime] moduleLastModified,
      map[MODID, set[Message]] messages,
      map[MODID, set[MStatus]] status,
      PathConfig pathConfig,
      RascalCompilerConfig compilerConfig
   );

ModuleStatus moduleStatus(          // TEMPORARY FOR COMPATIBILITY BETWEEN VERSIONS
      rel[str, PathRole, str] strPaths,
      rel[MODID, PathRole, MODID] paths,
      map[MODID, Module] parseTrees,
      list[MODID] parseTreeLIFO,
      map[MODID, TModel] tmodels,
      list[MODID] tmodelLIFO,
      set[MODID] changedModules,
      map[MODID,loc] moduleLocs,
      map[MODID,datetime] moduleLastModified,
      map[MODID, set[Message]] messages,
      map[MODID, set[MStatus]] status,
      PathConfig pathConfig,
      RascalCompilerConfig compilerConfig)
      = newModuleStatus(pathConfig, compilerConfig);

ModuleStatus newModuleStatus(PathConfig pcfg, TypePalConfig tcfg)
    = moduleStatus({}, (), [], (), [], {}, (), (), (), (), pcfg, tcfg);

ModuleStatus newModuleStatus() 
    = newModuleStatus(pathConfig(), tconfig());

ModuleStatus newModuleStatus(RascalCompilerConfig ccfg) 
    = newModuleStatus(ccfg.typepalPathConfig, ccfg);

bool isModuleLocationInLibs(loc l, PathConfig pcfg){
    assert !isModuleId(l);
    res = l.extension == "tpl" || !isEmpty(pcfg.libs) && any(lib <- pcfg.libs, l.scheme == lib.scheme && l.path == lib.path);
    return res;
}

bool traceTPL = false;
bool traceParseTreeCache = false;
bool traceTModelCache = false;

tuple[bool,loc] getTPLReadLoc(str qualifiedModuleName, PathConfig pcfg)
    =  getTPLReadLoc(moduleName2moduleId(qualifiedModuleName), pcfg);

tuple[bool,loc] getTPLReadLoc(MODID moduleId, PathConfig pcfg){
    assert isModuleId(moduleId) : "getTPLReadLoc: <moduleId>";
    parts = split("/", moduleId.path);
    parts = parts[0 .. size(parts)-1] + "$<parts[-1]>";
    fileName = intercalate("/", parts) + ".tpl";
    dirName = makeDirName(parts);

    for(loc dir <- [pcfg.generatedResources, pcfg.bin] + pcfg.libs){   // In a bin or lib directory?
        fileLoc = dir + "<getCompiledPackage()>" + fileName;
        if(exists(fileLoc)){
           if(traceTPL) println("getTPLReadLoc: <moduleId> =\> <fileLoc>");
           return <true, fileLoc>;
        } else {
        ;//    if(traceTPL)
        //     println("getTPLReadLoc: DOES NOT EXIST: <fileLoc>");
        }
    }
    return <false, |error:///|>;
}

tuple[bool,loc] getTPLWriteLoc(MODID moduleId, PathConfig pcfg){
    assert isModuleId(moduleId) : "getTPLWriteLoc: <moduleId>";
    qualifiedModuleName = moduleId2moduleName(moduleId);
    fileName = "<asBaseClassName(qualifiedModuleName)>.tpl";
    tplLoc = getGeneratedResourcesDir(qualifiedModuleName, pcfg) + fileName;
    return <exists(tplLoc), tplLoc>;
}

datetime startOfEpoch = $2000-01-01T00:00:00.000+00:00$;

datetime getLastModified(MODID moduleId, ModuleStatus ms){
    assert isModuleId(moduleId) : "getLastModified: <moduleId>";
    qualifiedModuleName = moduleId2moduleName(moduleId);
    if(moduleId in ms.moduleLastModified){
        return ms.moduleLastModified[moduleId];
   } else {
        try {
            mloc = getRascalModuleLocation(moduleId, ms);
            return lastModified(mloc);
        } catch value _: {
            return startOfEpoch;
        }
    }
}

// Check if a module is modified compared to a given timestamp in BOM
tuple[bool,ModuleStatus] isModuleModified(MODID moduleId, datetime timestamp, PathRole pathRole, ModuleStatus ms){
    pcfg = ms.pathConfig;
    try {
        mloc = getRascalModuleLocation(moduleId, ms);
        bool modifiedChanged = lastModified(mloc) != timestamp;
        if(pathRole == importPath()){
            return <modifiedChanged, ms>;
        } else {    
            // extendPath
            // TODO: in the case of deep extend chains this might become inefficient since we are
            // yoyo-ing up and down through the extend chains.
            // Potential solution: maintain set of changed modules in ModuleStatus
            <found, tm, ms> = getTModelForModule(moduleId, ms);
            if(found && tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
                 for(<str m, datetime timestampInBom, PathRole pathRole> <- bom){
                    <mchanged, ms> = isModuleModified(moduleName2moduleId(m), timestampInBom, pathRole, ms);
                    if(mchanged){
                        return <true, ms>;
                    }
                 }
                 return <false, ms>;
            }
            return <false, ms>;
        }
    } catch value _: {
        return <false, ms>;
    }
}

bool tplOutdated(MODID moduleId, PathConfig pcfg){
    try {
        qualifiedModuleName = moduleId2moduleName(moduleId);
        mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
        <found, tpl> = getTPLReadLoc(qualifiedModuleName, pcfg);
        lmMloc = lastModified(mloc);
        lmTpl = lastModified(tpl);
        res = !found || lmMloc > lmTpl;
        //println("tplOutdated <qualifiedModuleName>: <res>; mloc: <lmMloc> \> tpl: <lmTpl>: <lmMloc > lmTpl>, (<mloc>, <tpl>)");
        return res;
    } catch _: {
        return false;
    }
}

int parseTreeCacheSize = 20;

tuple[bool, Module, ModuleStatus] getModuleParseTree(MODID moduleId, ModuleStatus ms){
    assert isModuleId(moduleId) : "getModuleParseTree: <moduleId>";
    pcfg = ms.pathConfig;
    qualifiedModuleName = moduleId2moduleName(moduleId);
    if(ms.parseTrees[moduleId]?){
        if(traceParseTreeCache) println("*** using cached parse tree for <moduleId>");
        return <true, ms.parseTrees[moduleId], ms>;
    } else {
        if(!ms.status[moduleId]?){
            ms.status[moduleId] = {};
        }
        if(parse_error() notin ms.status[moduleId]){
            if(size(ms.parseTreeLIFO) >= parseTreeCacheSize){
                ms.parseTrees = delete(ms.parseTrees, ms.parseTreeLIFO[-1]);
                if(traceParseTreeCache) println("*** deleting parse tree <ms.parseTreeLIFO[-1]>");
                ms.parseTreeLIFO = ms.parseTreeLIFO[..-1];
            }
            ms.parseTreeLIFO = [moduleId, *ms.parseTreeLIFO];
            mloc = moduleId;
            try {
                mloc = getRascalModuleLocation(moduleId, ms);
                // Make sure we found a real source module (as opposed to a tpl module in a library
                if(isModuleLocationInLibs(mloc, pcfg)) {
                    ms.status[moduleId] += {rsc_not_found()};
                    throw "No src or library module";
                }
            } catch e: {
                println(e);
                ms.messages[moduleId] ? {} += {error("Module <qualifiedModuleName> not found", mloc)};
                mpt = [Module] "module <qualifiedModuleName>";
                ms.moduleLocs[moduleId] = mloc;
                return <false, mpt, ms>;
            }
            if(traceParseTreeCache) println("*** parsing <moduleId> from <mloc>");
            try {
                pt = parseModuleWithSpaces(mloc.top).top;
                ms.parseTrees[moduleId] = pt;
                newLoc = getLoc(pt);
                ms.moduleLocs[moduleId] = newLoc;
                ms.status[moduleId] ? {} += {parsed()};
                return <true, pt, ms>;
            } catch ParseError(loc src): {
                ms.messages[moduleId] ? {} = {error("Parse error in <moduleId>", src)};
                ms.moduleLocs[moduleId] = mloc;
                ms.status[moduleId] += parse_error();
                return <false, [Module] "module <qualifiedModuleName>", ms>;
            }
        }
        mpt = [Module] "module <qualifiedModuleName>";
        ms.parseTrees[moduleId] = mpt;
        return <false, mpt, ms>;
   }
}

loc getRascalModuleLocation(MODID moduleId, ModuleStatus ms){
    if(moduleId in ms.moduleLocs){
        loc l = ms.moduleLocs[moduleId];
        if(!isRascalLogicalLoc(l)) return l;
    }
    return getRascalModuleLocation(moduleId2moduleName(moduleId), ms.pathConfig);
}

/*
 * We implement a caching mechanism for TModels with the following properties:
 * - tmodelCacheSize tmodels are cached.
 * - In TModels on file (.tpl) physical locations have been replaced by logical locations where possible.
 * - The policy is to always keep TModels in the cache in this logical form.
 * - During its presence in the cache, the BOM of a TModel may get updated.
 * - When a TModel has to be removed from the cache, it is written back to file (if needed).
 */

int tmodelCacheSize = 30; // should be > 0

ModuleStatus clearTModelCache(ModuleStatus ms){
    todo = { mname | mname <- ms.status, bom_update_needed() in ms.status[mname]};
    for(candidate <- ms.tmodelLIFO){
        ms = removeOldestTModelFromCache(ms/*, updateBOMneeded=true*/);
        todo -= candidate;
    }
    for(candidate <- todo){
        ms = removeTModel(candidate, ms/*, updateBOMneeded=true*/);
        ms.status[candidate] -= bom_update_needed();
    }
    return ms;
}

rel[str,datetime,PathRole] makeBom(MODID moduleId, ModuleStatus ms){
    pcfg = ms.pathConfig;
    qualifiedModuleName = moduleId2moduleName(moduleId);
    imports = ms.paths[moduleId,importPath()];
    extends = ms.paths[moduleId, extendPath()];
    return   { < mname, getLastModified(m, ms), importPath() > | m <- imports, mname := moduleId2moduleName(m) }
           + { < mname, getLastModified(m, ms), extendPath() > | m <- extends, mname := moduleId2moduleName(m) }
           + { <qualifiedModuleName, getLastModified(moduleId, ms), importPath() > };
}

ModuleStatus updateBOM(MODID moduleId, ModuleStatus ms){
    if(rsc_not_found() in ms.status[moduleId]){
        return ms;
    }
    <found, tm, ms> = getTModelForModule(moduleId, ms);
    if(found){
        
        newBom = makeBom(moduleId, ms);
        if(newBom != tm.store[key_bom]){
            tm.store[key_bom] = newBom;
            ms.status[moduleId] -= {tpl_saved(), bom_update_needed()};
            ms = addTModel(moduleId, tm, ms);

            if(ms.compilerConfig.logWrittenFiles) println("Updated BOM: <moduleId>");
        }
    } else{
        println("Could not update BOM of <moduleId>");
    }
    return ms;
}

ModuleStatus removeTModel(MODID candidate, ModuleStatus ms, bool updateBOMneeded = false){
    assert isModuleId(candidate) : "removeTModel: <candidate>";
    if(   updateBOMneeded
       || (   candidate in ms.tmodels 
           && candidate in ms.status
           && tpl_saved() notin ms.status[candidate] 
           && rsc_not_found() notin ms.status[candidate])
      ){
        pcfg = ms.pathConfig;
        if(updateBOMneeded){
            ms = updateBOM(candidate, ms);
         } 
        ms.status[candidate] -= bom_update_needed();
        <found, tplLoc> = getTPLWriteLoc(candidate, pcfg);
        tm = ms.tmodels[candidate];
        //tm.messages = toList(toSet(tm.messages) + ms.messages[candidate]); // TODO needed ?
        ms.status[candidate] += tpl_saved();
        if(ms.compilerConfig.verbose) println("Saving tmodel for <moduleId2moduleName(candidate)> before removing from cache");
        try {
            writeBinaryValueFile(tplLoc, tm);
            if(traceTPL) println("Written <tplLoc>");
        } catch value e: {
            mloc = ms.moduleLocs[candidate] ? |unknown:///|;
            ms.messages[candidate] += { error("Cannot write TPL file <tplLoc>, reason: <e>",  mloc) };
        }
    }
    ms.tmodels = delete(ms.tmodels, candidate);
    return ms;
}

ModuleStatus removeOldestTModelFromCache(ModuleStatus ms, bool updateBOMneeded = false){
    if(size(ms.tmodelLIFO) > 0){
        candidate = ms.tmodelLIFO[-1];
        ms = removeTModel(candidate, ms, updateBOMneeded=updateBOMneeded);
        if(traceTModelCache) println("*** deleted tmodel <candidate>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
        ms.tmodelLIFO = ms.tmodelLIFO[..-1];
    }
    return ms;
}

ModuleStatus  addTModel (MODID moduleId, TModel tm, ModuleStatus ms){
    if(traceTModelCache) println("addTModel: <moduleId>");
    if(tmodelCacheSize > 0){
        ms.tmodels[moduleId] = tm;
        if(moduleId notin ms.tmodelLIFO){
            ms.tmodelLIFO = [moduleId, *ms.tmodelLIFO];
            while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != moduleId){
                ms = removeOldestTModelFromCache(ms);
            }
        }
    }
    return ms;
}

private type[TModel] ReifiedTModel = #TModel;  // precomputed for efficiency

tuple[bool, TModel, ModuleStatus] getTModelForModule(str moduleName, ModuleStatus ms)
    = getTModelForModule(moduleName2moduleId(moduleName), ms);

tuple[bool, TModel, ModuleStatus] getTModelForModule(MODID moduleId, ModuleStatus ms){
    assert isModuleId(moduleId) : "getTModelForModule: <moduleId>";
    if(traceTModelCache) println("getTModelForModule: <moduleId> <moduleId in ms.status ? ms.status[moduleId] : "{}">");
    pcfg = ms.pathConfig;
    if(moduleId in ms.tmodels){
        tm = ms.tmodels[moduleId];
        return <true, tm, ms>;
    }
    while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != moduleId){
        ms = removeOldestTModelFromCache(ms);
    }

    <found, tplLoc> = getTPLReadLoc(moduleId, pcfg);
    if(found){
        if(traceTPL) println("*** reading tmodel <tplLoc>");
        try {
            tm = readBinaryValueFile(ReifiedTModel, tplLoc);
            if(tm.rascalTplVersion? && isValidRascalTplVersion(tm.rascalTplVersion)){
                ms.tmodels[moduleId] = tm;
                mloc = getRascalModuleLocation(moduleId, ms);
                if(isModuleLocationInLibs(mloc, pcfg)){
                    ms.status[moduleId] ? {} += {rsc_not_found()};
                }
                ms.status[moduleId] ? {} += {tpl_uptodate(), tpl_saved()};
                ms.messages[moduleId] = toSet(tm.messages);
                ms.tmodelLIFO = [moduleId, *ms.tmodelLIFO];
                return <true, tm, ms>;
             }
        } catch e: {
            qualifiedModuleName = moduleId2moduleName(moduleId);
            return <false, tmodel(modelName=moduleId2moduleName(moduleId), messages=[error("Cannot read TPL for <qualifiedModuleName>: <e>", tplLoc)]), ms>;
        }
        msg = "<tplLoc> has outdated or missing Rascal TPL version (required: <getCurrentRascalTplVersion()>)";
        println("INFO: <msg>)");
        throw rascalTplVersionError(msg);
    }
    qualifiedModuleName = moduleId2moduleName(moduleId);
    return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>", tplLoc)]), ms>;
}

rel[loc from, PathRole r, loc to] getPaths(rel[MODID from, PathRole r, MODID to] paths, ModuleStatus ms){
    paths = {};
    for(<MODID from, PathRole r, MODID to> <- paths){
        try {
            mfrom = getRascalModuleLocation(from, ms);
            mto = getRascalModuleLocation(to, ms);
            paths += <mfrom, r, mto>;
        } catch _: ;/* ignore non-existing module */
    }
    return paths;
}

int closureCounter = 0;

int nextClosure(){
    counter = closureCounter;
    closureCounter += 1;
    return counter;
}

void resetClosureCounter(){
    closureCounter = 0;
}
