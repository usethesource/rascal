@bootstrapParser
module lang::rascalcore::check::Import

/*
    Check imports, and read/write TPL files.
    Introduces ModuleStatus that represents all the relevant information about currently processed modules.
*/

extend lang::rascalcore::check::CheckerCommon;

import lang::rascalcore::check::RascalConfig;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ADTandGrammar;

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

import analysis::graphs::Graph;
import util::Reflective;
import lang::rascalcore::compile::util::Names; // TODO: refactor, this is an undesired dependency on compile

bool traceTPL = false;
bool traceCaches = false;

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
           if(traceTPL) println("getTPLReadLoc: DOES NOT EXIST: <fileLoc>");
        }
    }
    return <false, |error:///|>;
}

tuple[bool,loc] getTPLWriteLoc(str qualifiedModuleName, PathConfig pcfg){
    fileName = "<asBaseClassName(qualifiedModuleName)>.tpl";
    tplLoc = getDerivedResourcesDir(qualifiedModuleName, pcfg) + fileName;
    return <exists(tplLoc), tplLoc>;
}

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
            return $2000-01-01T00:00:00.000+00:00$;
        }
    }
}

int parseTreeCacheSize = 10;

tuple[bool, Module, ModuleStatus] getModuleParseTree(str qualifiedModuleName, ModuleStatus ms){
    pcfg = ms.pathConfig;
    if(ms.parseTrees[qualifiedModuleName]?){
        if(traceCaches) println("*** using cached parse tree for <qualifiedModuleName>");
        return <true, ms.parseTrees[qualifiedModuleName], ms>;
    } else {
        if(!ms.status[qualifiedModuleName]?){
            ms.status[qualifiedModuleName] = {};
        }
        if(parse_error() notin ms.status[qualifiedModuleName]){
            if(size(ms.parseTreeLIFO) >= parseTreeCacheSize){
                ms.parseTrees = delete(ms.parseTrees, ms.parseTreeLIFO[-1]);
                if(traceCaches) println("*** deleting parse tree <ms.parseTreeLIFO[-1]>");
                ms.parseTreeLIFO = ms.parseTreeLIFO[..-1];
            }
            ms.parseTreeLIFO = [qualifiedModuleName, *ms.parseTreeLIFO];
            mloc = |unknown:///|;
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
            if(traceCaches) println("*** parsing <qualifiedModuleName> from <mloc>");
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

//set[str] hardwired = {};
//
//int maxHardwired = 10;
//
//int tmodelCacheSize = 200;
//
//void analyzeTModels(ModuleStatus ms){
//    freq = ();
//    total = 0;
//    for(<_, _, m2> <- ms.strPaths){
//        freq[m2] ? 0 += 1;
//        total += 1;
//    }
//    sorted_freq = sort(toList(freq), bool(tuple[str s,int n] a, tuple[str s, int n] b){ return a.n > b.n; });
//    nmodules = size(freq);
//    cutoff = 2 * total/(nmodules + 1);
//    hardwire = [tp | tuple[str s, int n] tp <- sorted_freq , tp.n > cutoff][0..maxHardwired];
//    if(traceCaches){
//        println("analyzeTModels: <nmodules> modules, imports/extends: <total>, cutoff: <cutoff>");
//      iprintln(hardwire);
//    }
//    hardwired = toSet(domain(hardwire));
//}
//
//ModuleStatus  addTModel (str qualifiedModuleName, TModel tm, ModuleStatus ms){
//    if(traceCaches) println("addTModel: <qualifiedModuleName>");
//    ms.tmodels[qualifiedModuleName] = tm;
//    if(qualifiedModuleName notin hardwired){
//        if(qualifiedModuleName notin ms.tmodelLIFO){
//            ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
//        }
//        while(size(ms.tmodels) >= tmodelCacheSize){
//            if(tpl_saved() notin ms.status[ms.tmodelLIFO[-1]]){
//                throw "Cannot remove unsaved tpl <ms.tmodelLIFO[-1]>, <ms.status[ms.tmodelLIFO[-1]]>";
//            }
//            ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
//            if(traceCaches) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
//            ms.tmodelLIFO = ms.tmodelLIFO[..-1];
//        }
//    }
//    return ms;
//}

tuple[bool, TModel, ModuleStatus] getTModelForModule(str qualifiedModuleName, ModuleStatus ms){
    //if(traceCaches) println("getTModelForModule: <qualifiedModuleName>");
    pcfg = ms.pathConfig;
    if(ms.tmodels[qualifiedModuleName]?){
        return <true, ms.tmodels[qualifiedModuleName], ms>;
    }
    //while(size(ms.tmodels) >= tmodelCacheSize){
    //    ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
    //    if(traceCaches) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
    //    ms.tmodelLIFO = ms.tmodelLIFO[..-1];
    //}
    //if(qualifiedModuleName notin hardwired){
    //    ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
    //}
    
    <found, tplLoc> = getTPLReadLoc(qualifiedModuleName, pcfg);
    if(found){
        if(traceTPL) println("*** reading tmodel <tplLoc>");
        try {
            tpl = readBinaryValueFile(#TModel, tplLoc);
            if(isValidRascalTplVersion(tpl.rascalTplVersion)){
                tpl = convertTModel2PhysicalLocs(tpl);
                
                ms.tmodels[qualifiedModuleName] = tpl;
                ms.status[qualifiedModuleName] += {tpl_uptodate(), tpl_saved()};
                return <true, tpl, ms>;
             } else {
                println("INFO: <tplLoc> has outdated Rascal TPL version <tpl.rascalTplVersion> (required: <getCurrentRascalTplVersion()>)");
                return <false, tmodel(modelName=qualifiedModuleName, 
                        messages=[error("<tplLoc> has outdated Rascal TPL version <tpl.rascalTplVersion>", tplLoc)]), ms>; 
             }
        } catch e: {
            //ms.status[qualifiedModuleName] ? {} += not_found();
            return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL for <qualifiedModuleName>: <e>", tplLoc)]), ms>; 
            //throw IO("Cannot read tpl for <qualifiedModuleName>: <e>");
        }
    }
    //if(qualifiedModuleName notin hardwired){
    //    ms.tmodelLIFO = ms.tmodelLIFO[1..];
    //}
    //ms.status[qualifiedModuleName] ? {} += not_found();
    return <false, tmodel(modelName=qualifiedModuleName, messages=[error("Cannot read TPL", |unknown:///|)]), ms>;
   // throw IO("Cannot read tpl for <qualifiedModuleName>");
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
      //list[str] tmodelLIFO,
      map[str,loc] moduleLocs, 
      map[str,datetime] moduleLastModified,
      map[str, list[Message]] messages,
      map[str, set[MStatus]] status,
      PathConfig pathConfig
   );

ModuleStatus newModuleStatus(PathConfig pcfg) = moduleStatus({}, {}, (), [], (), (), (), (), (), pcfg);

str getModuleName(loc mloc, map[loc,str] moduleStrs, PathConfig pcfg){
    if(moduleStrs[mloc]? ){
        return moduleStrs[mloc];
    }
    return getModuleName(mloc, pcfg);
}

// Complete a ModuleStatus by adding a contains relation that adds transitive edges for extend
ModuleStatus complete(ModuleStatus ms){
    pcfg = ms.pathConfig;
    moduleStrs = invertUnique(ms.moduleLocs);
    paths = ms.paths + { <ms.moduleLocs[a], r, ms.moduleLocs[b]> | <str a, PathRole r, str b> <- ms.strPaths, ms.moduleLocs[a]?, ms.moduleLocs[b]? };
    extendPlus = {<from, to> | <from, extendPath(), to> <- paths}+;
  
    paths += { <from, extendPath(), to> | <from, to> <- extendPlus };
    
    pathsPlus = {<from, to> | <from, _, to> <- paths}+;
    
    cyclicMixed = {mloc1, mloc2 | <mloc1, mloc2> <- pathsPlus, mloc1 != mloc2,
                             <mloc1, importPath(), mloc2> in paths && <mloc2, extendPath(), mloc1> in paths
                             || <mloc1, extendPath(), mloc2> in paths && <mloc2, importPath(), mloc1> in paths };
                             
    for(mloc <- cyclicMixed){
        mname = getModuleName(mloc, moduleStrs, pcfg);
        set[str] cycle = { getModuleName(mloc2, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc1 == mloc, mloc2 in cyclicMixed } +
                         { getModuleName(mloc1, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc2 == mloc , mloc1 in cyclicMixed };
        if(size(cycle) > 1){
            ms.messages[mname] = (ms.messages[mname] ? []) + error("Mixed import/extend cycle not allowed: {<intercalate(", ", toList(cycle))>}", mloc);
        }
    }
   
    paths += { <c, importPath(), a> | < c, importPath(), b> <- paths,  <b , extendPath(), a> <- paths};
    ms.paths = paths;
   
   strPaths = {};
    for(<loc from, PathRole r, loc to> <- paths){
        try {
            mfrom = getModuleName(from, moduleStrs, pcfg);
            mto = getModuleName(to, moduleStrs, pcfg);
            strPaths += <mfrom, r, mto >;
        } catch _: ;/* ignore non-existing module */
    }
    ms.strPaths = strPaths;  
    return ms;
}

ModuleStatus getImportAndExtendGraph(set[str] qualifiedModuleNames, PathConfig pcfg){
    return complete((newModuleStatus(pcfg) | getImportAndExtendGraph(qualifiedModuleName, it) | qualifiedModuleName <- qualifiedModuleNames));
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg){
    return complete(getImportAndExtendGraph(qualifiedModuleName, newModuleStatus(pcfg)));
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, ModuleStatus ms){
    pcfg = ms.pathConfig;
    qualifiedModuleName = unescape(qualifiedModuleName);
    
    if(!ms.status[qualifiedModuleName]?){
        ms.status[qualifiedModuleName] = {};
    }
   
    if(module_dependencies_extracted() in ms.status[qualifiedModuleName]){ 
        return ms;
    }
    ms.status[qualifiedModuleName] += module_dependencies_extracted();
    
    <found, tm, ms> = getTModelForModule(qualifiedModuleName, ms);
    if(found){
        allImportsAndExtendsValid = true;
        rel[str, PathRole] localImportsAndExtends = {};
        
        if(!ms.moduleLastModified[qualifiedModuleName]?){
            ms.moduleLastModified[qualifiedModuleName] = getLastModified(qualifiedModuleName, ms.moduleLastModified, pcfg);
        }
        imported_messages = [];
        
        if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
           //println("BOM <qualifiedModuleName>:"); iprintln(bom);
           for(<str m, datetime timestampInBom, PathRole pathRole> <- bom){
               if(!ms.status[m]?){
                    ms.status[m] = {};
               }
               if(m != qualifiedModuleName){
                    localImportsAndExtends += <m, pathRole>;
               }
               lm = getLastModified(m, ms.moduleLastModified, pcfg);
               //if(!ms.moduleLastModified[m]?){
                    ms.moduleLastModified[m] = lm;
               //}
               //TODO: window used to be 500, increase to 1000 when using inside/outside Eclipse runtimes
               if(decrementMilliseconds(/*getLastModified(m, ms.moduleLastModified, pcfg)*/ lm, 1000) > timestampInBom) {
                    allImportsAndExtendsValid = false;
                    if(tpl_uptodate() notin ms.status[m] ){
                        println("--- using <lm /*getLastModified(m, ms.moduleLastModified, pcfg)*/> (most recent) version of <m>, 
                                '    older <timestampInBom> version was used in previous check of <qualifiedModuleName>");
                        //ms.status[m] += tpl_invalid();
                    }
               }
           }
         
        } else {
            throw "No bill-of-materials found for <qualifiedModuleName>";
        }
        if(!allImportsAndExtendsValid){ // Check that the source code of qualifiedModuleName is available
            try {
                mloc = getModuleLocation(qualifiedModuleName, pcfg);
            } catch value _:{
                allImportsAndExtendsValid = true;
                println("--- reusing tmodel of <qualifiedModuleName> (source not accessible)");
            }
        }
        if(allImportsAndExtendsValid){
            ms.status[qualifiedModuleName] += {tpl_uptodate(), checked()}; //TODO: maybe check existence of generated java files
   
            ms.moduleLocs += tm.moduleLocs;
            ms.paths += tm.paths;
            ms.strPaths += {<qualifiedModuleName, pathRole, imp> | <str imp, PathRole pathRole> <- localImportsAndExtends };
            ms.status[qualifiedModuleName] += module_dependencies_extracted();
            for(imp <- localImportsAndExtends<0>, module_dependencies_extracted() notin ms.status[imp]  ){
                ms = getImportAndExtendGraph(imp, ms);
            }
            return ms;
         }
    } 
    
    else {
        allImportsAndExtendsValid = false;
        //ms.status[qualifiedModuleName] += not_found();
    }
    
    <success, pt, ms> = getModuleParseTree(qualifiedModuleName, ms);
    if(success){
        imports_and_extends = getModulePathsAsStr(pt);
        ms.strPaths += imports_and_extends;
   
        for(<_, kind, imp> <- imports_and_extends){
            ms.strPaths += {<qualifiedModuleName, kind, imp>};
            ms = getImportAndExtendGraph(imp, ms);
        }
    } else {
        if(not_found() notin ms.status[qualifiedModuleName]){
            //ms.messages[qualifiedModuleName] ? [] += [ error("Cannot get parse tree for module `<qualifiedModuleName>`", ms.moduleLocs[qualifiedModuleName]) ];
            ms.status[qualifiedModuleName] += not_found();
        }
    }
    
    return ms;
}

ModuleStatus getInlineImportAndExtendGraph(Tree pt, PathConfig pcfg){
    ms = newModuleStatus(pcfg);
    visit(pt){
        case  m: (Module) `<Header header> <Body _>`: {
            qualifiedModuleName = prettyPrintName(header.name);
            ms.moduleLocs[qualifiedModuleName] = getLoc(m);
            imports_and_extends = getModulePathsAsStr(m);
            ms.strPaths += imports_and_extends;
        }
    }
    return complete(ms);
}

rel[str, PathRole, str] getModulePathsAsStr(Module m){
    moduleName = unescape("<m.header.name>");
    return { <moduleName, imod is \default ? importPath() : extendPath(), unescape("<imod.\module.name>")> |  imod <- m.header.imports, imod has \module};
}

set[loc] getImportLocsOfModule(str qualifiedModuleName, set[Module] modules)
    = { getLoc(imod) | m <- modules, imod <- m.header.imports, imod has \module, "<imod.\module>" == qualifiedModuleName };

// ---- Save modules ----------------------------------------------------------

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <loc _, str id, str _orgId, moduleId(),  loc defined, DefInfo _> <- tm.defines);

loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes, PathConfig pcfg){
    if(moduleScopes[qualifiedModuleName]?){
        return moduleScopes[qualifiedModuleName];
    }
    for(l <- range(moduleScopes)){
        if(getModuleName(l, pcfg) == qualifiedModuleName){
            return l;
        }
    }
    throw "No module scope found for <qualifiedModuleName>";
}

ModuleStatus prepareForCompilation(set[str] component, map[str,set[str]] m_imports, map[str,set[str]] m_extends, ModuleStatus ms, map[str,loc] moduleScopes, TModel tm){
    map[str,TModel] tmodels = ms.tmodels;
    pcfg = ms.pathConfig;
    
    dependencies_ok = true;
    for(m <- component, not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        if(parse_error() in ms.status[m]){
            ms.tmodels[m] = tmodel(modelName=m,messages=ms.messages[m]);
            return ms;
        }
        for(imp <- m_imports[m] + m_extends[m], not_found() notin ms.status[imp], MStatus::ignored() notin ms.status[m]){
            imp_status = ms.status[imp];
            if(parse_error() in imp_status || checked() notin imp_status){
                dependencies_ok = false;
                cause = (not_found() in imp_status) ? "module not found" : "due to syntax error";
                ms.messages[m] = (ms.messages[imp] ? []) + error("<imp in m_imports[imp] ? "Imported" : "Extended"> module <imp> could not be checked (<cause>)", moduleScopes[m]); 
            }
        }
        if(!dependencies_ok){
            ms.tmodels[m] = tmodel(modelName=m,messages=ms.messages[m]);
            return ms;
        }
    }
    for(m <- component, not_found() notin ms.status[m], MStatus::ignored() notin ms.status[m]){
        tm.modelName = m;
        mScope = getModuleScope(m, moduleScopes, pcfg);
        tm.moduleLocs = (m : mScope);
        
        tm.definitions = ( def.defined : def | Define def <- tm.defines);
       
        ms.tmodels[m] = tm;
        tm = addGrammar(m, m_imports[m], m_extends[m], ms.tmodels);
        ms.messages[m] = tm.messages;
        ms.tmodels[m] = tm;
    }
    return ms;
}

ModuleStatus doSaveModule(set[str] component, map[str,set[str]] m_imports, map[str,set[str]] m_extends, ModuleStatus ms, map[str,loc] moduleScopes, RascalCompilerConfig compilerConfig){
    map[str,datetime] moduleLastModified = ms.moduleLastModified;
    pcfg = ms.pathConfig;
    
    if(any(c <- component, !isEmpty({parse_error(), not_found(), MStatus::ignored()} & ms.status[c]))){
        return ms;
    }
    //println("doSaveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    component_scopes = { getModuleScope(qualifiedModuleName, moduleScopes, pcfg) | qualifiedModuleName <- component };
    loc2moduleName = invertUnique(ms.moduleLocs);
    
    bool isContainedInComponentScopes(loc inner){
        return any(cs <- component_scopes, isContainedIn(inner, cs));
    };
        
    for(qualifiedModuleName <- component){
        tm = ms.tmodels[qualifiedModuleName];
        try {
            mscope = getModuleScope(qualifiedModuleName, moduleScopes, pcfg);
            <found, tplLoc> = getTPLWriteLoc(qualifiedModuleName, pcfg);
            imports = m_imports[qualifiedModuleName];
            extends = m_extends[qualifiedModuleName];
            
            bom = { < m, getLastModified(m, moduleLastModified, pcfg), importPath() > | m <- imports }
                + { < m, getLastModified(m, moduleLastModified, pcfg), extendPath() > | m <- extends }
                + { <qualifiedModuleName, getLastModified(qualifiedModuleName, moduleLastModified, pcfg), importPath() > };
            
            extendedModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- extends, checked() in ms.status[m]};
            extendedModuleScopes += {*tm.paths[ems,importPath()] | ems <- extendedModuleScopes}; // add imports of extended modules
            filteredModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- (qualifiedModuleName + imports), checked() in ms.status[m]} + extendedModuleScopes;
            
            TModel m1 = tmodel();
            m1.rascalTplVersion = getCurrentRascalTplVersion();
            m1.modelName = qualifiedModuleName;
            m1.moduleLocs = (qualifiedModuleName : mscope);
            
            m1.facts = (key : tm.facts[key] | key <- tm.facts, isContainedInComponentScopes(key));
            
            m1.specializedFacts = (key : tm.specializedFacts[key] | key <- tm.specializedFacts, isContainedInComponentScopes(key), any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
            m1.facts += m1.specializedFacts;
            
            m1.messages = sort( { msg | msg <- tm.messages, /*msg is error || */msg.at.path == mscope.path}, bool(Message a, Message b){ return a.at.begin.line < b.at.begin.line; });
            ms.messages[qualifiedModuleName] = m1.messages;
            
            filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
            
            //println("tm.scopes:"); iprintln(tm.scopes);
            //m1.scopes = tm.scopes;
            m1.scopes
                = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths, isContainedInComponentScopes(inner));
            
            m1.store 
                = (key_bom : bom);
            m1.store[key_grammar] 
                = tm.store[key_grammar] ? grammar({}, ());
            
            m1.store[key_ADTs]    
                = tm.store[key_ADTs] ? {};
            m1.store[key_common_keyword_fields]   
                = tm.store[key_common_keyword_fields] ? [];
            
            m1.paths = { tup | tuple[loc from, PathRole pathRole, loc to] tup <- tm.paths, tup.from == mscope || tup.from in filteredModuleScopes /*|| tup.from in filteredModuleScopePaths*/ };
            
            //m1.uses = [u | u <- tm.uses, isContainedIn(u.occ, mscope) ];
            m1.useDef = { <u, d> | <u, d> <- tm.useDef, tm.definitions[d].idRole in keepInTModelRoles || isContainedIn(u, mscope) };
            
            // Filter model for current module and replace functions in defType by their defined type
            
            defs = for(tup:<loc scope, str _id, str _orgId, IdRole idRole, loc defined, DefInfo _defInfo> <- tm.defines){ 
                       if( idRole in keepInTModelRoles
                          && isContainedInComponentScopes(defined)
                          && (  scope == |global-scope:///| && defined.path in filteredModuleScopePaths 
                             || scope in filteredModuleScopes
                             || scope.path == mscope.path
                             )
                         ){
                         append tup;
                      } else {
                         ;
                      }
                   };
            
            m1.defines = toSet(defs);
            
            m1.definitions = ( def.defined : def | Define def <- m1.defines);  // TODO this is derived info, can we derive it later?
            // Remove default expressions and fragments
            m1 = visit(m1) {
                    case kwField(AType atype, str fieldName, str definingModule, Expression _defaultExp) => kwField(atype, fieldName, definingModule)
                    case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""];
                 };
            m1.logical2physical = tm.logical2physical;
                            
            m1 = convertTModel2LogicalLocs(m1, ms.tmodels);
            
            ////TODO temporary check: are external locations present in the TModel? If so, throw exception
            //
            //result = "";
            //
            //void checkPhysical(value v, str label){ 
            //    visit(v){
            //        case loc l:
            //                if(!(isContainedInComponentScopes(l) || l == |global-scope:///| || contains(l.scheme, "rascal+")))
            //                    result += "<label>, outside <qualifiedModuleName>: <l>\n";
            //    }
            //}
            //checkPhysical(m1.moduleLocs, "moduleLocs");
            //checkPhysical(m1.facts, "facts");
            //checkPhysical(m1.specializedFacts, "specializedFacts");
            //checkPhysical(m1.defines, "defines");
            //checkPhysical(m1.definitions, "definitions");
            //checkPhysical(m1.scopes, "scopes");
            //checkPhysical(m1.store, "store");
            //checkPhysical(m1.paths, "paths");
            //checkPhysical(m1.useDef, "useDef");
            //
            //if(!isEmpty(result)){
            //    println("------------- <qualifiedModuleName>:
            //            '<result>");
            //    iprintln(m1, lineLimit=10000);
            //    throw "checkPhysical failed, see above";
            //}
            
            ms.status[qualifiedModuleName] += tpl_saved();
            try {
                writeBinaryValueFile(tplLoc, m1);
                if(compilerConfig.logWrittenFiles) println("Written: <tplLoc>");
            } catch value e: {
                throw "Cannot write TPL file <tplLoc>, reason: <e>";
            }
            
            ms.tmodels[qualifiedModuleName] = m1;
            
        } catch value e: {
            ms.tmodels[qualifiedModuleName] = tmodel(modelName=qualifiedModuleName, messages=tm.messages + [error("Could not save .tpl file for `<qualifiedModuleName>`, reason: <e>", |unknown:///|(0,0,<0,0>,<0,0>))]);
            return ms;
        }
    }
    return ms;
}