@bootstrapParser
module lang::rascalcore::check::Import

extend lang::rascalcore::check::CheckerCommon;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ADTandGrammar;

import DateTime;
import Exception;
import IO;
import List;
import Location;
import Map;
import Set;
import String;
import ValueIO;

import analysis::graphs::Graph;
import util::Reflective;
import lang::rascalcore::compile::util::Names; // TODO: refactor, this is an undesired dependency on compile

bool traceTPL = false;

tuple[bool,loc] getTPLReadLoc(str qualifiedModuleName, PathConfig pcfg){
    fileName = makeFileName(qualifiedModuleName, extension="tpl");
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
    //fileName = makeFileName(qualifiedModuleName, extension="tpl");
    fileName = "<getBaseClass(qualifiedModuleName)>.tpl";
    tplLoc = getDerivedResourcesDir(qualifiedModuleName, pcfg) + fileName;
    return <exists(tplLoc), tplLoc>;
    //classesDir = getDerivedClassesDir(qualifiedModuleName, pcfg);
    //tplLoc = classesDir + "<getBaseClass(qualifiedModuleName)>.tpl";
    //if(traceTPL) println("getTPLWriteLoc: <qualifiedModuleName> =\> \<<exists(tplLoc)>, <tplLoc>\>");
    //return <exists(tplLoc), tplLoc>;
}

datetime getLastModified(str qualifiedModuleName, map[str, datetime] moduleLastModified, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        return moduleLastModified[qualifiedModuleName];
   } catch NoSuchKey(_): {
        try {
            mloc = getModuleLocation(qualifiedModuleName, pcfg);
            return lastModified(mloc);
        } catch value _: {
            return $2000-01-01T00:00:00.000+00:00$;
        }
    }
}

alias ModuleStructure = tuple[rel[str, PathRole, str] strPaths, 
                              rel[loc, PathRole, loc] paths, 
                              map[str,TModel] tmodels, 
                              map[str,loc] moduleLocs, 
                              map[str,datetime] moduleLastModified,
                              map[str,Module] modules,
                              set[str] valid,
                              set[str] invalid,
                              map[str, list[Message]] messages,
                              set[str] visited
                              ];
void printModuleStructure(ModuleStructure ms){
    println("strPaths:"); iprintln(ms.strPaths);
    println("paths:"); iprintln(ms.paths);
    println("tmodels for: <domain(ms.tmodels)>");
    println("moduleLocs for: <domain(ms.moduleLocs)>");
    println("modules for: <domain(ms.modules)>");
    println("valid: <ms.valid>");
    println("visited: <ms.visited>");
}

ModuleStructure newModuleStructure() = <{}, {}, (), (), (), (), {}, {}, (), {}>;

str getModuleName(loc mloc, map[loc,str] moduleStrs, PathConfig pcfg){
    return moduleStrs[mloc]? ? moduleStrs[mloc] : getModuleName(mloc, pcfg);
}

// Complete a ModuleStructure by adding a contains relation that adds transitive edges for extend
ModuleStructure complete(ModuleStructure ms, PathConfig pcfg){
    moduleStrs = invertUnique(ms.moduleLocs);
    paths = ms.paths + { <ms.moduleLocs[a], r, ms.moduleLocs[b]> | <str a, PathRole r, str b> <- ms.strPaths, ms.moduleLocs[a]?, ms.moduleLocs[b]? };
    extendPlus = {<from, to> | <from, extendPath(), to> <- paths}+;
  
    paths += { <from, extendPath(), to> | <from, to> <- extendPlus };
    
    pathsPlus = {<from, to> | <from, _, to> <- paths}+;
    
    //cyclicExtend = {mloc1, mloc2 | <mloc1, mloc2> <- extendPlus, mloc1 != mloc2,
    //                                  <mloc2, extendPath(), mloc1> in paths
    //                               || <mloc1, extendPath(), mloc2> in paths };
    //if(cyclicExtend <= carrier(extendPlus)){
    //    for(mloc <- cyclicExtend){
    //        mname = getModuleName(mloc, moduleStrs, pcfg);
    //        set[str] cycle = { getModuleName(mloc2, moduleStrs, pcfg) |  <mloc1, mloc2> <- extendPlus, mloc1 == mloc, mloc2 in cyclicExtend } +
    //                         { getModuleName(mloc1, moduleStrs, pcfg) |  <mloc1, mloc2> <- extendPlus, mloc2 == mloc , mloc1 in cyclicExtend };
    //        if(size(cycle) > 1){
    //            ms.messages[mname] =  (ms.messages[mname] ? []) + error("Extend cycle not allowed: {<intercalate(", ", toList(cycle))>}", mloc);
    //        }
    //    }
    //}
    
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
    return ms;
}

ModuleStructure getImportAndExtendGraph(set[str] qualifiedModuleNames, PathConfig pcfg, bool logImports){
    return complete((newModuleStructure() | getImportAndExtendGraph(qualifiedModuleName, pcfg, it, logImports) | qualifiedModuleName <- qualifiedModuleNames), pcfg);
}

ModuleStructure getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, bool logImports){
    return complete(getImportAndExtendGraph(qualifiedModuleName, pcfg, newModuleStructure(), logImports), pcfg);
}

ModuleStructure getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, ModuleStructure ms, bool logImports){
    qualifiedModuleName = unescape(qualifiedModuleName);
   
    //println("getImportAndExtendGraph: <qualifiedModuleName>, <domain(ms.modules)>, <domain(ms.tmodels)>, <ms.visited>");
    if(ms.modules[qualifiedModuleName]? || ms.tmodels[qualifiedModuleName]?){
        return ms;
    }
    <found, tplLoc> = getTPLReadLoc(qualifiedModuleName, pcfg);
    if(found){
        try {
            allImportsAndExtendsValid = true;
            rel[str, PathRole] localImportsAndExtends = {};
            tm = readBinaryValueFile(#TModel, tplLoc);
            if(tm.store[key_bom]? && rel[str,datetime,PathRole] bom := tm.store[key_bom]){
               //println("BOM:"); iprintln(bom);
               for(<str m, timestampInBom, pathRole> <- bom){
                   if(m != qualifiedModuleName){
                        localImportsAndExtends += <m, pathRole>;
                   }
                   if(decrementMilliseconds(getLastModified(m, ms.moduleLastModified, pcfg), 500) > timestampInBom) {
                        allImportsAndExtendsValid = false;
                        if(m notin ms.invalid){
                            println("--- using <getLastModified(m, ms.moduleLastModified, pcfg)> (most recent) version of <m>, 
                                    '    older <timestampInBom> version was used in previous check of <qualifiedModuleName>");
                            ms.invalid = ms.invalid + {m};
                        }
                   }
               }
            } else {
                throw "No bill-of-materials found in <tplLoc>";
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
                if(logImports) println("*** importing <qualifiedModuleName> from <tplLoc> (ts=<lastModified(tplLoc)>)");
          
                ms.valid += {qualifiedModuleName};
                ms.tmodels[qualifiedModuleName] = tm;
                ms.moduleLocs += tm.moduleLocs;
                ms.paths += tm.paths;
                ms.strPaths += {<qualifiedModuleName, pathRole, imp> | <str imp, PathRole pathRole> <- localImportsAndExtends };
                ms.visited += {qualifiedModuleName};
                for(imp <- localImportsAndExtends<0>, imp notin ms.visited){
                    ms = getImportAndExtendGraph(imp, pcfg, ms, logImports);
                }
                return ms;
             }
        } catch IO(str msg): {
            ms.tmodels[qualifiedModuleName] = tmodel()[messages = [ error("During import of module `<qualifiedModuleName>`: IO error <msg>", getModuleLocation(qualifiedModuleName, pcfg)) ]];
        }
    }
    try {
        Module pt = [Module] "module xxx";
        if(ms.modules[qualifiedModuleName]?){
            pt = ms.modules[qualifiedModuleName];
        } else {
            mloc = getModuleLocation(qualifiedModuleName, pcfg);                    
            if(logImports) println("*** parsing <qualifiedModuleName> from <mloc>");
            pt = parseModuleWithSpaces(mloc).top;
            ms.modules[qualifiedModuleName] = pt;
            ms.moduleLocs[qualifiedModuleName] = getLoc(pt);
        }
        imports_and_extends = getModulePathsAsStr(pt);
        ms.strPaths += imports_and_extends;
        ms.visited += {qualifiedModuleName};
        for(imp <- imports_and_extends<2>){
            ms = getImportAndExtendGraph(imp, pcfg, ms, logImports);
      }
    } catch value _: {
        ms.tmodels[qualifiedModuleName] = tmodel()[messages = [ error("Parse error in module `<qualifiedModuleName>`", getModuleLocation(qualifiedModuleName, pcfg)) ]];
    }
    return ms;
}

ModuleStructure getInlineImportAndExtendGraph(Tree pt, PathConfig pcfg){
    ms = newModuleStructure();
    visit(pt){
        case  m: (Module) `<Header header> <Body _>`: {
            qualifiedModuleName = prettyPrintName(header.name);
            ms.modules[qualifiedModuleName] = m;
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

ModuleStructure saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, ModuleStructure ms, map[str,loc] moduleScopes, PathConfig pcfg){
    tm = addGrammar(qualifiedModuleName, imports, extends, ms.tmodels);
    ms.tmodels[qualifiedModuleName] = tm;
    ms.messages[qualifiedModuleName] = tm.messages;
    ms.tmodels[qualifiedModuleName] = saveModule(qualifiedModuleName, imports, extends, moduleScopes, ms.moduleLastModified, pcfg, tm);
    return ms;
}

private TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, map[str,datetime] moduleLastModified, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes, pcfg);
        <found, tplLoc> = getTPLWriteLoc(qualifiedModuleName, pcfg);
        //classesDir = getDerivedClassesDir(qualifiedModuleName, pcfg);
        //tplLoc = classesDir + "<getBaseClass(qualifiedModuleName)>.tpl";
        //println("saveModule(<qualifiedModuleName>) =\> <tplLoc>");
        //tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
        
        bom = { < m, getLastModified(m, moduleLastModified, pcfg), importPath() > | m <- imports }
            + { < m, getLastModified(m, moduleLastModified, pcfg), extendPath() > | m <- extends }
            + { <qualifiedModuleName, getLastModified(qualifiedModuleName, moduleLastModified, pcfg), importPath() > };
            
        //bom = (m : getLastModified(m, pcfg) | m <- imports + extends);
        //bom[qualifiedModuleName] = getLastModified(qualifiedModuleName, pcfg);
        
        //println("=== BOM <qualifiedModuleName>"); 
        //for(m <- bom) println("<bom[m]>: <m>");
        //println("=== BOM END"); 
        
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
        //println("m1.specializedFacts:"); iprintln(m1.specializedFacts);
        
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
        //m1.paths = tm.paths; //{ tup | tuple[loc from, PathRole pathRole, loc to] tup <- tm.paths, tup.from == mscope };
        //iprintln(m1.paths);
        
        
        //m1.uses = [u | u <- tm.uses, isContainedIn(u.occ, mscope) ];
        m1.useDef = { <u, d> | <u, d> <- tm.useDef, tm.definitions[d].idRole in saveModuleRoles || isContainedIn(u, mscope) };
        
        //roles = dataOrSyntaxRoles + {constructorId(), functionId(), fieldId(), keywordFieldId(), keywordDormal() + annoId()} + anyVariableRoles;
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <loc scope, str _, str _, IdRole idRole, loc defined, DefInfo defInfo> <- tm.defines){
                   if(scope == |global-scope:///| && defined.path in filteredModuleScopePaths || 
                      scope in filteredModuleScopes || 
                      (scope.path == mscope.path && idRole in saveModuleRoles)
                      //(idRole == functionId() && isContainedIn(scope.path == mscope.path))
                      ){
                          
                      if(scope in extendedModuleScopes){
                         if(defType(_) !:= defInfo){
                            throw "Suspicious define in TModel: <tup>";
                         }
                         //tup.scope = mscope;
                      }         
                      append tup;
                  }
               };
        
        //if(tm.config.logImports) println("defines: <size(tm.defines)> ==\> <size(defs)>");
        m1.defines = toSet(defs);
        m1 = visit(m1) {case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""]; };
        m1.definitions = ( def.defined : def | Define def <- m1.defines);  // TODO this is derived info, can we derive it later?
        
        //calcs = (key : tm.calculators[key] | loc key <- tm.calculators, key.path == mscope.path, bprintln("<key>: <tm.calculators[key]>"));
        //
        //reqs  = {r | r <- tm.openReqs, r@\loc.path == mscope.path, bprintln(r)};
        //
        //println("left: <size(calcs)> calculators, <size(reqs)> requirements");
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