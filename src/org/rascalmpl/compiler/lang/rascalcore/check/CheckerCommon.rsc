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

import analysis::typepal::Collector;

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
      not_found()
    | parsed()
    | parse_error()
    | module_dependencies_extracted()
    | checked()
    | check_error()
    | code_generated()
    | tpl_uptodate()
    | tpl_saved()
    | ignored()
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
      map[str, list[Message]] messages,
      map[str, set[MStatus]] status,
      PathConfig pathConfig
   );

ModuleStatus newModuleStatus(PathConfig pcfg) = moduleStatus({}, {}, (), [], (), [], (), (), (), (), pcfg);

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
        fileLoc = dir + "<compiled_rascal_package>" + fileName;
        if(exists(fileLoc)){
           if(traceTPL) println("getTPLReadLoc: <qualifiedModuleName> =\> <fileLoc>");
           return <true, fileLoc>;
        } else {
           if(traceTPL) 
            println("getTPLReadLoc: DOES NOT EXIST: <fileLoc>");
        }
    }
    return <false, |error:///|>;
}

tuple[bool,loc] getTPLWriteLoc(str qualifiedModuleName, PathConfig pcfg){
    fileName = "<asBaseClassName(qualifiedModuleName)>.tpl";
    tplLoc = getDerivedResourcesDir(qualifiedModuleName, pcfg) + fileName;
    return <exists(tplLoc), tplLoc>;
}

datetime startOfEpoch = $2000-01-01T00:00:00.000+00:00$;

datetime getLastModified(str qualifiedModuleName, map[str, datetime] moduleLastModified, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        res = moduleLastModified[qualifiedModuleName];
        //println("getLastModified <qualifiedModuleName> from map: <res>");
        return res;
   } catch NoSuchKey(_): {
        try {
            mloc = getModuleLocation(qualifiedModuleName, pcfg);
            res = lastModified(mloc);
            //println("getLastModified <mloc> via lastModified: <res>");
            return res;
        } catch value _: {
            return startOfEpoch;
        }
    }
}

int parseTreeCacheSize = 10;

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
                mloc = getModuleLocation(qualifiedModuleName, pcfg);      
            } catch _: {
                //ms.messages[qualifiedModuleName] ? [] = [error("Module <qualifiedModuleName> not found", mloc)];
                //ms.status[qualifiedModuleName] += {not_found()};
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
                ms.messages[qualifiedModuleName] ? [] = [error("Parse error in <qualifiedModuleName>", mloc)];
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

set[str] hardwired = {};

int tmodelCacheSize = 4; // should be > 0

int maxHardwired = tmodelCacheSize/4;

void analyzeTModels(ModuleStatus ms){
    freq = ();
    total = 0;
    for(<_, _, m2> <- ms.strPaths){
        freq[m2] ? 0 += 1;
        total += 1;
    }
    sorted_freq = sort(toList(freq), bool(tuple[str s,int n] a, tuple[str s, int n] b){ return a.n > b.n; });
    nmodules = size(freq);
    cutoff = 2 * total/(nmodules + 1);
    hardwire = [tp | tuple[str s, int n] tp <- sorted_freq , tp.n > cutoff][0..maxHardwired];
    if(traceTModelCache){
        println("analyzeTModels: <nmodules> modules, imports/extends: <total>, cutoff: <cutoff>");
      iprintln(hardwire);
    }
    hardwired = toSet(domain(hardwire));
}

ModuleStatus  addTModel (str qualifiedModuleName, TModel tm, ModuleStatus ms){
    if(traceTModelCache) println("addTModel: <qualifiedModuleName>");
    if(tmodelCacheSize > 0){
        if(tpl_saved() notin ms.status[qualifiedModuleName]){
            iprintln(ms.status);
            throw "Cannot add module <qualifiedModuleName> with unsaved tpl";
        }
        ms.tmodels[qualifiedModuleName] = tm;
        if(qualifiedModuleName notin hardwired){
            if(qualifiedModuleName notin ms.tmodelLIFO){
                ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
                while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != qualifiedModuleName){
                    if(tpl_saved() notin ms.status[ms.tmodelLIFO[-1]]){
                        iprintln(ms.status);
                        throw "Cannot remove unsaved tpl <ms.tmodelLIFO[-1]>, <ms.status[ms.tmodelLIFO[-1]]>";
                    }
                    ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
                    if(traceTModelCache) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
                    ms.tmodelLIFO = ms.tmodelLIFO[..-1];
                }
            }
        }
    }
    return ms;
}

tuple[bool, TModel, ModuleStatus] getTModelForModule(str qualifiedModuleName, ModuleStatus ms){
    if(traceTModelCache) println("getTModelForModule: <qualifiedModuleName>");
    pcfg = ms.pathConfig;
    if(ms.tmodels[qualifiedModuleName]?){
        if(tpl_saved() notin ms.status[qualifiedModuleName]){
            throw "Unsaved tmodel for <qualifiedModuleName> in cache";
        }
        return <true, ms.tmodels[qualifiedModuleName], ms>;
    }
    while(size(ms.tmodels) >= tmodelCacheSize && size(ms.tmodelLIFO) > 0 && ms.tmodelLIFO[-1] != qualifiedModuleName){
        ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
        if(traceTModelCache) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
        ms.tmodelLIFO = ms.tmodelLIFO[..-1];
    }
   
    <found, tplLoc> = getTPLReadLoc(qualifiedModuleName, pcfg);
    if(found){
        if(traceTPL) println("*** reading tmodel <tplLoc>");
        try {
            tpl = readBinaryValueFile(#TModel, tplLoc);
            if(tpl.rascalTplVersion? && isValidRascalTplVersion(tpl.rascalTplVersion)){
                tpl = convertTModel2PhysicalLocs(tpl);
                
                ms.tmodels[qualifiedModuleName] = tpl;
                ms.status[qualifiedModuleName] += {tpl_uptodate(), tpl_saved()};
                if(qualifiedModuleName notin hardwired){
                    ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
                }
                return <true, tpl, ms>;
             } 
             //else {
             //   msg = "<tplLoc> has outdated or missing Rascal TPL version (required: <getCurrentRascalTplVersion()>)";
             //   println("INFO: <msg>)");
             //   throw rascalTplVersionError(msg);
             //   //ms.tmodels[qualifiedModuleName] = 
             //   //    tmodel(modelName=qualifiedModuleName, 
             //   //           messages=[msg]);
             //   //return <true, tpl, ms>; 
             //}
        } catch e: {
            //ms.status[qualifiedModuleName] ? {} += not_found();
            return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>: <e>", tplLoc)]), ms>; 
            //throw IO("Cannot read tpl for <qualifiedModuleName>: <e>");
        }
        msg = "<tplLoc> has outdated or missing Rascal TPL version (required: <getCurrentRascalTplVersion()>)";
        println("INFO: <msg>)");
        throw rascalTplVersionError(msg);
    }
    //if(qualifiedModuleName notin hardwired){
    //    ms.tmodelLIFO = ms.tmodelLIFO[1..];
    //}
    //ms.status[qualifiedModuleName] ? {} += not_found();
    return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>", |unknown:///<qualifiedModuleName>|)]), ms>;
   // throw IO("Cannot read tpl for <qualifiedModuleName>");
}