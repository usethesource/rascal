@bootstrapParser
module lang::rascalcore::check::Import

extend lang::rascalcore::check::CheckerCommon;

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
    //println("getDerivedReadLoc: <qualifiedModuleName> =\> |error:///|");
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

tuple[Module, ModuleStatus] getModuleParseTree(str qualifiedModuleName, ModuleStatus ms, PathConfig pcfg){
    if(ms.parseTrees[qualifiedModuleName]?){
        if(traceCaches) println("*** using cached parse tree for <qualifiedModuleName>");
        return <ms.parseTrees[qualifiedModuleName], ms>;
    } else {
        if(size(ms.parseTreeLIFO) >= parseTreeCacheSize){
            ms.parseTrees = delete(ms.parseTrees, ms.parseTreeLIFO[-1]);
            if(traceCaches) println("*** deleting parse tree <ms.parseTreeLIFO[-1]>");
            ms.parseTreeLIFO = ms.parseTreeLIFO[..-1];
        }
        ms.parseTreeLIFO = [qualifiedModuleName, *ms.parseTreeLIFO];
        mloc = getModuleLocation(qualifiedModuleName, pcfg);                    
        if(traceCaches) println("*** parsing <qualifiedModuleName> from <mloc>");
        pt = parseModuleWithSpaces(mloc).top;
        ms.parseTrees[qualifiedModuleName] = pt;
        ms.moduleLocs[qualifiedModuleName] = getLoc(pt);
        ms.status[qualifiedModuleName] += parsed();
        return <pt, ms>;
   }
}

set[str] hardwired = {};

int maxHardwired = 10;

int tmodelCacheSize = 15;

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
    if(traceCaches){
        println("analyzeTModels: <nmodules> modules, importd/extends: <total>, cutoff: <cutoff>");
      iprintln(hardwire);
    }
    hardwired = toSet(domain(hardwire));
}

ModuleStatus  addTModel (str qualifiedModuleName, TModel tm, ModuleStatus ms){
    ms.tmodels[qualifiedModuleName] = tm;
    if(qualifiedModuleName notin hardwired){
        if(qualifiedModuleName notin ms.tmodelLIFO){
            ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
        }
        while(size(ms.tmodels) >= tmodelCacheSize){
            ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
            if(traceCaches) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
            ms.tmodelLIFO = ms.tmodelLIFO[..-1];
        }
    }
    return ms;
}

tuple[bool, TModel, ModuleStatus] getTmodelForModule(str qualifiedModuleName, ModuleStatus ms, PathConfig pcfg){
    if(ms.tmodels[qualifiedModuleName]?){
        return <true, ms.tmodels[qualifiedModuleName], ms>;
    }
    while(size(ms.tmodels) >= tmodelCacheSize){
        ms.tmodels = delete(ms.tmodels, ms.tmodelLIFO[-1]);
        if(traceCaches) println("*** deleting tmodel <ms.tmodelLIFO[-1]>, tmodels: <size(ms.tmodels)>, lifo: <size(ms.tmodelLIFO)>");
        ms.tmodelLIFO = ms.tmodelLIFO[..-1];
    }
    if(qualifiedModuleName notin hardwired){
        ms.tmodelLIFO = [qualifiedModuleName, *ms.tmodelLIFO];
    }
    
    <found, tplLoc> = getTPLReadLoc(qualifiedModuleName, pcfg);
    if(found){
        if(traceTPL) println("*** reading tmodel <tplLoc>");
        try {
            tpl = readBinaryValueFile(#TModel, tplLoc);
            ms.tmodels[qualifiedModuleName] = tpl;
            return <true, tpl, ms>;
        } catch e:
            throw IO("Cannot read tpl for <qualifiedModuleName>: <e>");
    }
    if(qualifiedModuleName notin hardwired){
        ms.tmodelLIFO = ms.tmodelLIFO[1..];
    }
    return <false, tmodel(), ms>;
   // throw IO("Cannot read tpl for <qualifiedModuleName>");
}

data MStatus =
      parsed()
    | module_dependencies_extracted()
    | checked()
    | code_generated()
    | tpl_exists()
    | tpl_invalid()
    | tpl_valid()
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
      map[str, set[MStatus]] status
   );
                              
void printModuleStatus(ModuleStatus ms){
    println("strPaths:"); iprintln(ms.strPaths);
    println("paths:"); iprintln(ms.paths);
    println("parseTrees: <domain(ms.parseTrees)>");
    println("moduleLocs for: <domain(ms.moduleLocs)>");
    println("status:"); iprintln(ms.status);
}

ModuleStatus newModuleStatus() = moduleStatus({}, {}, (), [], (), [], (), (), (), ());

str getModuleName(loc mloc, map[loc,str] moduleStrs, PathConfig pcfg){
    return moduleStrs[mloc]? ? moduleStrs[mloc] : getModuleName(mloc, pcfg);
}

// Complete a ModuleStatus by adding a contains relation that adds transitive edges for extend
ModuleStatus complete(ModuleStatus ms, PathConfig pcfg){
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
            ms.messages[mname] =  (ms.messages[mname] ? []) + error("Mixed import/extend cycle not allowed: {<intercalate(", ", toList(cycle))>}", mloc);
        }
    }
   
    paths += { <c, importPath(), a> | < c, importPath(), b> <- paths,  <b , extendPath(), a> <- paths};
    ms.paths = paths;
   
    ms.strPaths = { < getModuleName(from, moduleStrs, pcfg), r, getModuleName(to, moduleStrs, pcfg) > | <loc from, PathRole r, loc to> <- paths};
    analyzeTModels(ms);  
    return ms;
}

ModuleStatus getImportAndExtendGraph(set[str] qualifiedModuleNames, PathConfig pcfg, bool logImports){
    return complete((newModuleStatus() | getImportAndExtendGraph(qualifiedModuleName, pcfg, it, logImports) | qualifiedModuleName <- qualifiedModuleNames), pcfg);
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, bool logImports){
    return complete(getImportAndExtendGraph(qualifiedModuleName, pcfg, newModuleStatus(), logImports), pcfg);
}

ModuleStatus getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, ModuleStatus ms, bool logImports){
    qualifiedModuleName = unescape(qualifiedModuleName);
    
    if(!ms.status[qualifiedModuleName]?){
        ms.status[qualifiedModuleName] = {};
    }
   
    if(module_dependencies_extracted() in ms.status[qualifiedModuleName]){ 
        return ms;
    }
    ms.status[qualifiedModuleName] += module_dependencies_extracted();
    
    <found, tm, ms> = getTmodelForModule(qualifiedModuleName, ms, pcfg);
    if(found){
        try {
            allImportsAndExtendsValid = true;
            rel[str, PathRole] localImportsAndExtends = {};
            
            if(!ms.moduleLastModified[qualifiedModuleName]?){
                ms.moduleLastModified[qualifiedModuleName] = getLastModified(qualifiedModuleName, ms.moduleLastModified, pcfg);
            }
            ms.status[qualifiedModuleName] += tpl_exists();
            
            if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
               //println("BOM:"); iprintln(bom);
               for(<str m, datetime timestampInBom, PathRole pathRole> <- bom){
                   if(!ms.status[m]?){
                        ms.status[m] = {};
                   }
                   if(m != qualifiedModuleName){
                        localImportsAndExtends += <m, pathRole>;
                   }
                   lm = getLastModified(m, ms.moduleLastModified, pcfg);
                   if(!ms.moduleLastModified[m]?){
                        ms.moduleLastModified[m] = lm;
                   }
                   //TODO: window used to be 500, increase to 1000 when using inside/outside Eclipse runtimes
                   if(decrementMilliseconds(getLastModified(m, ms.moduleLastModified, pcfg), 1000) > timestampInBom) {
                        allImportsAndExtendsValid = false;
                        if(tpl_invalid() notin ms.status[m] ){
                            println("--- using <getLastModified(m, ms.moduleLastModified, pcfg)> (most recent) version of <m>, 
                                    '    older <timestampInBom> version was used in previous check of <qualifiedModuleName>");
                            ms.status[m] += tpl_invalid();
                        }
                   }
               }
             
            } else {
                throw "No bill-of-materials found for <qualifiedModuleName>";
            }
            if(!allImportsAndExtendsValid){
                try {
                    mloc = getModuleLocation(qualifiedModuleName, pcfg);
                } catch value _:{
                    allImportsAndExtendsValid = true;
                    println("--- reusing tmodel of <qualifiedModuleName> (source not accessible)");
                }
            }
            if(allImportsAndExtendsValid){
                if(logImports) println("*** importing <qualifiedModuleName>");
                ms.status[qualifiedModuleName] += {tpl_valid(), checked(), code_generated()}; //TODO: check java files
       
                ms.moduleLocs += tm.moduleLocs;
                ms.paths += tm.paths;
                ms.strPaths += {<qualifiedModuleName, pathRole, imp> | <str imp, PathRole pathRole> <- localImportsAndExtends };
                ms.status[qualifiedModuleName] += module_dependencies_extracted();
                for(imp <- localImportsAndExtends<0>, module_dependencies_extracted() notin ms.status[imp]  ){
                    ms = getImportAndExtendGraph(imp, pcfg, ms, logImports);
                }
                return ms;
             }

        } catch IO(str msg): {
            ms.messages[qualifiedModuleName] = [ error("During import of module `<qualifiedModuleName>`: IO error <msg>", getModuleLocation(qualifiedModuleName, pcfg)) ];
        }
    }
    
    try {
        <pt, ms> = getModuleParseTree(qualifiedModuleName, ms, pcfg);
        imports_and_extends = getModulePathsAsStr(pt);
        ms.strPaths += imports_and_extends;
       
        for(imp <- imports_and_extends<2>){
            ms = getImportAndExtendGraph(imp, pcfg, ms, logImports);
        }
        //ms.parseTrees = delete(ms.parseTrees, qualifiedModuleName);
    } catch value _: {
        ms.messages[qualifiedModuleName] = [ error("Parse error in module `<qualifiedModuleName>`", getModuleLocation(qualifiedModuleName, pcfg)) ];
    }
    return ms;
}

ModuleStatus getInlineImportAndExtendGraph(Tree pt, PathConfig pcfg){
    ms = newModuleStatus();
    visit(pt){
        case  m: (Module) `<Header header> <Body _>`: {
            qualifiedModuleName = prettyPrintName(header.name);
            ms.moduleLocs[qualifiedModuleName] = getLoc(m);
            imports_and_extends = getModulePathsAsStr(m);
            ms.strPaths += imports_and_extends;
        }
    }
    return complete(ms, pcfg);
}

rel[str, PathRole, str] getModulePathsAsStr(Module m){
    moduleName = unescape("<m.header.name>");
    return { <moduleName, imod is \default ? importPath() : extendPath(), unescape("<imod.\module.name>")> |  imod <- m.header.imports, imod has \module};
}

set[loc] getImportLocsOfModule(str qualifiedModuleName, set[Module] modules)
    = { getLoc(imod) | m <- modules, imod <- m.header.imports, imod has \module, "<imod.\module>" == qualifiedModuleName };

// ---- Save modules ----------------------------------------------------------

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <loc _, str id, str _orgId, moduleId(), loc defined, DefInfo _> <- tm.defines);

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

tuple[TModel, ModuleStatus] saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, ModuleStatus ms, map[str,loc] moduleScopes, PathConfig pcfg, TModel tm){
    tmodels = ();
    for(m <- imports + extends, tpl_valid() in ms.status[m]){
        <found, tpl, ms> = getTmodelForModule(m, ms, pcfg);
        ms = addTModel(m, tpl, ms);
    }
    //tmodels = ( m : getTmodelForModule(m, ms, pcfg) | m <- imports + extends, tpl_valid() in ms.status[m] );
    ms = addTModel(qualifiedModuleName, tm, ms);
    tm = addGrammar(qualifiedModuleName, imports, extends, ms.tmodels);
    ms.messages [qualifiedModuleName] = tm.messages;
    tm = saveModule(qualifiedModuleName, imports, extends, moduleScopes, ms.moduleLastModified, pcfg, tm);
    return <tm, ms>;
}

private TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, map[str,datetime] moduleLastModified, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes, pcfg);
        <found, tplLoc> = getTPLWriteLoc(qualifiedModuleName, pcfg);
        
        bom = { < m, getLastModified(m, moduleLastModified, pcfg), importPath() > | m <- imports }
            + { < m, getLastModified(m, moduleLastModified, pcfg), extendPath() > | m <- extends }
            + { <qualifiedModuleName, getLastModified(qualifiedModuleName, moduleLastModified, pcfg), importPath() > };
        
        extendedModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- extends};
        extendedModuleScopes += {*tm.paths[ems,importPath()] | ems <- extendedModuleScopes}; // add imports of extended modules
        filteredModuleScopes = {getModuleScope(m, moduleScopes, pcfg) | str m <- (qualifiedModuleName + imports)} + extendedModuleScopes /*+ |global-scope:///|*/;
       
        TModel m1 = tmodel();
        
        m1.modelName = qualifiedModuleName;
        m1.moduleLocs = (qualifiedModuleName : mscope);
        
        //m1.facts = (key : tm.facts[key] | key <- tm.facts, any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        m1.facts = tm.facts;
        
        
        //if(tm.config.logImports) println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
        //println("tm.specializedFacts:"); iprintln(tm.specializedFacts);
        m1.specializedFacts = (key : tm.specializedFacts[key] | key <- tm.specializedFacts, any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        m1.facts += m1.specializedFacts;
        
        m1.messages = [msg | msg <- tm.messages, msg.at.path == mscope.path];
        
        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        
        m1.scopes
            = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths);
        
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
        m1.useDef = { <u, d> | <u, d> <- tm.useDef, tm.definitions[d].idRole in saveModuleRoles || isContainedIn(u, mscope) };
        
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <loc scope, str _, str _, IdRole idRole, loc defined, DefInfo _> <- tm.defines){ 
                   if(  idRole in saveModuleRoles
                      && (  scope == |global-scope:///| && defined.path in filteredModuleScopePaths 
                         || scope in filteredModuleScopes
                         || scope.path == mscope.path
                         )
                     ){ 
                     //if(scope in extendedModuleScopes){
                     //   if(defType(_) !:= defInfo){
                     //      throw "Suspicious define in TModel: <tup>";
                     //   }
                     //   //tup.scope = mscope;
                     //}         
                     append tup;
                  }
               };
        
        m1.defines = toSet(defs);
        m1 = visit(m1) {case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""]; };
        m1.definitions = ( def.defined : def | Define def <- m1.defines);  // TODO this is derived info, can we derive it later?
        
        try {
            if(!isEmpty(m1.messages)){
                iprintln(m1.messages);
            }
            writeBinaryValueFile(tplLoc, m1);
            println("Written: <tplLoc>");
        } catch _: {
            throw "Corrupt TPL file <tplLoc>";
        }
        if(tm.config.logImports) {
             errors = { msg | msg <- m1.messages, error(_,_) := msg };
             n_errors = size(errors);
             n_other = size(m1.messages) - n_errors;
             notes = n_errors > 0 ? " ERRORS <n_errors>" : "";
             notes += n_errors > 0 ? " WARNINGS/INFO: <n_other>" : "";
             if (n_errors > 0) println("WRITTEN to <tplLoc> (ts=<lastModified(tplLoc)>)<notes>");
             for(e <- errors){
                iprintln(e);
             }
      
        }
        return m1;
    } catch value e: {
        return tmodel()[messages=tm.messages + [error("Could not save .tpl file for `<qualifiedModuleName>`: <e>", |unknown:///|(0,0,<0,0>,<0,0>))]];
    }
}