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

extend analysis::typepal::Collector;

import lang::rascal::\syntax::Rascal;
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
    | tpl_uptodate()
    | tpl_saved()
    | ignored()
    | bom_update_needed()
    ;

data ModuleStatus =
    moduleStatus(
      rel[str, PathRole, str] strPaths,
      rel[loc, PathRole, loc] paths,
      map[str, Module] parseTrees,
      list[str] parseTreeLIFO,
      map[str, TModel] tmodels,
      list[str] tmodelLIFO,
      map[str,loc] moduleLocs,
      map[str,datetime] moduleLastModified,
      map[str, set[Message]] messages,
      map[str, set[MStatus]] status,
      PathConfig pathConfig,
      RascalCompilerConfig compilerConfig
   );

ModuleStatus newModuleStatus(RascalCompilerConfig ccfg) = moduleStatus({}, {}, (), [], (), [], (), (), (), (), ccfg.typepalPathConfig, ccfg);

bool isModuleLocationInLibs(str mname, loc l, PathConfig pcfg){
    res = l.extension == "tpl" || !isEmpty(pcfg.libs) && any(lib <- pcfg.libs, l.scheme == lib.scheme && l.path == lib.path);
    //println("isModuleLocationInLibs: <mname>, <l> ==\> <res>");
    return res;
}

bool traceTPL = false;
bool traceParseTreeCache = false;
bool traceTModelCache = false;

tuple[bool,loc] getTPLReadLoc(str qualifiedModuleName, PathConfig pcfg){
    parts = split("::", qualifiedModuleName);
    parts = parts[0 .. size(parts)-1] + "$<parts[-1]>";
    res = intercalate("/", parts);
    fileName = intercalate("/", parts) + ".tpl";
    dirName = makeDirName(qualifiedModuleName);

    for(loc dir <- [pcfg.resources, pcfg.bin] + pcfg.libs){   // In a bin or lib directory?
        fileLoc = dir + "<getCompiledPackage(qualifiedModuleName, pcfg)>" + fileName;
        if(exists(fileLoc)){
           if(traceTPL) println("getTPLReadLoc: <qualifiedModuleName> =\> <fileLoc>");
           return <true, fileLoc>;
        } else {
        ;//    if(traceTPL)
        //     println("getTPLReadLoc: DOES NOT EXIST: <fileLoc>");
        }
    }
    return <false, |error:///|>;
}

tuple[bool,loc] getTPLWriteLoc(str qualifiedModuleName, PathConfig pcfg){
    fileName = "<asBaseClassName(qualifiedModuleName)>.tpl";
    tplLoc = getGeneratedResourcesDir(qualifiedModuleName, pcfg) + fileName;
    return <exists(tplLoc), tplLoc>;
}

datetime startOfEpoch = $2000-01-01T00:00:00.000+00:00$;

datetime getLastModified(str qualifiedModuleName, map[str, datetime] moduleLastModified, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        return moduleLastModified[qualifiedModuleName];
   } catch NoSuchKey(_): {
        try {
            mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
            return lastModified(mloc);
        } catch value _: {
            return startOfEpoch;
        }
    }
}

// Check if a module is modified compared to a given timestamp
bool isModuleModified(str qualifiedModuleName, datetime timestamp, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
        return lastModified(mloc) != timestamp;
    } catch value _: {
        return false;
    }
}

bool tplOutdated(str qualifiedModuleName, PathConfig pcfg){
    try {
        mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
        <found, tpl> = getTPLReadLoc(qualifiedModuleName, pcfg);
        lmMloc = lastModified(mloc);
        lmTpl = lastModified(tpl);
        res = !found || lmMloc > lmTpl;
        //println("tplOutdated <qualifiedModuleName>: <res>; mloc: <lmMloc> \> tpl: <lmTpl>: <lmMloc > lmTpl>, (<mloc>, <tpl>)");
        return res;
    } catch e: {
        return false;
    }
}

int parseTreeCacheSize = 20;

tuple[bool, Module, ModuleStatus] getModuleParseTree(str qualifiedModuleName, ModuleStatus ms){
    pcfg = ms.pathConfig;
    if(ms.parseTrees[qualifiedModuleName]?){
        if(traceParseTreeCache) println("*** using cached parse tree for <qualifiedModuleName>");
        return <true, ms.parseTrees[qualifiedModuleName], ms>;
    } else {
        if(!ms.status[qualifiedModuleName]?){
            ms.status[qualifiedModuleName] = {};
        }
        if(parse_error() notin ms.status[qualifiedModuleName]){
            if(size(ms.parseTreeLIFO) >= parseTreeCacheSize){
                ms.parseTrees = delete(ms.parseTrees, ms.parseTreeLIFO[-1]);
                if(traceParseTreeCache) println("*** deleting parse tree <ms.parseTreeLIFO[-1]>");
                ms.parseTreeLIFO = ms.parseTreeLIFO[..-1];
            }
            ms.parseTreeLIFO = [qualifiedModuleName, *ms.parseTreeLIFO];
            mloc = |unknown:///<qualifiedModuleName>|;
            try {
                mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
                // Make sure we found a real source module (as opposed to a tpl module in a library
                if(isModuleLocationInLibs(qualifiedModuleName, mloc, pcfg)) {
                    ms.status[qualifiedModuleName] += {rsc_not_found()};
                    throw "No src or library module";
                }
            } catch _: {
                ms.messages[qualifiedModuleName] ? {} += {error("Module <qualifiedModuleName> not found", mloc)};
                mpt = [Module] "module <qualifiedModuleName>";
                //ms.parseTrees[qualifiedModuleName] = mpt;
                ms.moduleLocs[qualifiedModuleName] = mloc;
                return <false, mpt, ms>;
            }
            if(traceParseTreeCache) println("*** parsing <qualifiedModuleName> from <mloc>");
            try {
                pt = parseModuleWithSpaces(mloc).top;
                ms.parseTrees[qualifiedModuleName] = pt;
                ms.moduleLocs[qualifiedModuleName] = getLoc(pt);
                ms.status[qualifiedModuleName] += parsed();
                return <true, pt, ms>;
            } catch _: {//ParseError(loc src): {
                ms.messages[qualifiedModuleName] ? {} = {error("Parse error in <qualifiedModuleName>", mloc)};
                ms.moduleLocs[qualifiedModuleName] = mloc;
                ms.status[qualifiedModuleName] += parse_error();
                return <false, [Module] "module <qualifiedModuleName>", ms>;
            }
        }
        mpt = [Module] "module <qualifiedModuleName>";
        ms.parseTrees[qualifiedModuleName] = mpt;
        return <false, mpt, ms>;
   }
}

/*
 * We implement a caching mechanism for TModels with the following properties:
 * - tmodelCacheSize tmodels are cached.
 * - TModels on file (.tpl) physical locations have been replaced by logical locations where possible.
 * - When a TModel is read in, physical locations are NOT YET converted by logical logical locations
 *   and only do that when absolutely needed
 * - The policy is to keep TModels in the cache in this unconverted logical form as long as possible.
 * - During its presence in the cache, the BOM of a TModel may get updated.
 * - When a TModel has to be removed from the cache, it is converted back to the logical form (if needed) and written back to file.
 */

int tmodelCacheSize = 30; // should be > 0

ModuleStatus clearTModelCache(ModuleStatus ms){
    todo = { mname | mname <- ms.status, bom_update_needed() in ms.status[mname]};
    for(candidate <- ms.tmodelLIFO){
        ms = removeOldestTModelFromCache(ms, updateBOMneeded=true);
        todo -= candidate;
    }
    for(candidate <- todo){
        ms = removeTModel(candidate, ms, updateBOMneeded=true);
        ms.status[candidate] -= bom_update_needed();
    }
    return ms;
}

rel[str,datetime,PathRole] makeBom(str qualifiedModuleName, ModuleStatus ms){
    map[str,datetime] moduleLastModified = ms.moduleLastModified;
    pcfg = ms.pathConfig;
    imports = ms.strPaths[qualifiedModuleName,importPath()];
    extends = ms.strPaths[qualifiedModuleName, extendPath()];
    return   { < m, getLastModified(m, moduleLastModified, pcfg), importPath() > | m <- imports }
           + { < m, getLastModified(m, moduleLastModified, pcfg), extendPath() > | m <- extends }
           + { <qualifiedModuleName, getLastModified(qualifiedModuleName, moduleLastModified, pcfg), importPath() > };
}

ModuleStatus updateBOM(str qualifiedModuleName, ModuleStatus ms){
    if(rsc_not_found() in ms.status[qualifiedModuleName]){
        return ms;
    }
    <found, tm, ms> = getTModelForModule(qualifiedModuleName, ms, convert=false);
    if(found){
        
        newBom = makeBom(qualifiedModuleName, 
        ms);
        if(newBom != tm.store[key_bom]){
            tm.store[key_bom] = newBom;
            ms.status[qualifiedModuleName] -= tpl_saved();
            ms = addTModel(qualifiedModuleName, tm, ms);

            if(ms.compilerConfig.logWrittenFiles) println("Updated BOM: <qualifiedModuleName>");
        }
    } else{
        println("Could not update BOM of <qualifiedModuleName>");
    }
    return ms;
}

ModuleStatus removeTModel(str candidate, ModuleStatus ms, bool updateBOMneeded = false){
    if(ms.status[candidate]? && tpl_saved() notin ms.status[candidate] && rsc_not_found() notin ms.status[candidate]){
        pcfg = ms.pathConfig;
        if(updateBOMneeded){
            ms = updateBOM(candidate, ms);
         } else {
            ms.status[candidate] += bom_update_needed();
         }
        <found, tplLoc> = getTPLWriteLoc(candidate, pcfg);
        tm = ms.tmodels[candidate];
        tm = convertTModel2LogicalLocs(tm, ms.tmodels);
        ms.status[candidate] += tpl_saved();
        if(ms.compilerConfig.verbose) println("Save <candidate> before removing from cache <ms.status[candidate]>");
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

ModuleStatus  addTModel (str qualifiedModuleName, TModel tm, ModuleStatus ms){
    if(traceTModelCache) println("addTModel: <qualifiedModuleName>");
    if(tmodelCacheSize > 0){
        ms.tmodels[qualifiedModuleName] = tm;
        if(qualifiedModuleName notin ms.tmodelLIFO){
            ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
            while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != qualifiedModuleName){
                ms = removeOldestTModelFromCache(ms);
            }
        }
    }
    return ms;
}

private type[TModel] ReifiedTModel = #TModel;  // precomputed for efficiency

tuple[bool, TModel, ModuleStatus] getTModelForModule(str qualifiedModuleName, ModuleStatus ms, bool convert = true){
    if(traceTModelCache) println("getTModelForModule: <qualifiedModuleName>");
    pcfg = ms.pathConfig;
    if(qualifiedModuleName in ms.tmodels){
        tm = ms.tmodels[qualifiedModuleName];
        if(convert && !tm.usesPhysicalLocs){
            tm = convertTModel2PhysicalLocs(tm);
            ms.tmodels[qualifiedModuleName] = tm;
        }
        return <true, tm, ms>;
    }
    while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != qualifiedModuleName){
        ms = removeOldestTModelFromCache(ms);
    }

    <found, tplLoc> = getTPLReadLoc(qualifiedModuleName, pcfg);
    if(found){
        if(traceTPL) println("*** reading tmodel <tplLoc>");
        try {
            tm = readBinaryValueFile(ReifiedTModel, tplLoc);
            if(tm.rascalTplVersion? && isValidRascalTplVersion(tm.rascalTplVersion)){
                tm.usesPhysicalLocs = false; // temporary
                if(convert){
                    tm = convertTModel2PhysicalLocs(tm);
                }
                ms.tmodels[qualifiedModuleName] = tm;
                mloc = getRascalModuleLocation(qualifiedModuleName, pcfg);
                if(isModuleLocationInLibs(qualifiedModuleName, mloc, pcfg)){
                    ms.status[qualifiedModuleName] ? {} += {rsc_not_found()};
                }
                ms.status[qualifiedModuleName] ? {} += {tpl_uptodate(), tpl_saved()};
                ms.messages[qualifiedModuleName] = toSet(tm.messages);
                ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
                return <true, tm, ms>;
             }
        } catch e: {
            return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>: <e>", tplLoc)]), ms>;
        }
        msg = "<tplLoc> has outdated or missing Rascal TPL version (required: <getCurrentRascalTplVersion()>)";
        println("INFO: <msg>)");
        throw rascalTplVersionError(msg);
    }
    return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>", |unknown:///<qualifiedModuleName>|)]), ms>;
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
