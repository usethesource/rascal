@bootstrapParser
module lang::rascalcore::check::Import

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::Checker;

import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::NameUtils;
import Location;

import lang::rascal::\syntax::Rascal;

import Exception;
import IO;
import List;
import Map;
import Set;
import String;
import ValueIO;

import analysis::graphs::Graph;
import util::Reflective;

//public str key_bom = "bill_of_materials";
//public str key_current_module = "current_module";

tuple[bool,loc] TPLReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);

datetime getLastModified(str qualifiedModuleName, map[str, datetime] moduleLastModified, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        return moduleLastModified[qualifiedModuleName];
   } catch NoSuchKey(_): {
        try {
            mloc = getModuleLocation(qualifiedModuleName, pcfg);
            return lastModified(mloc);
        } catch value e: {
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

// Complete an ModuleStructure by adding a contains relation that adds transitive edges for extend
ModuleStructure complete(ModuleStructure ms, PathConfig pcfg){
    moduleStrs = invertUnique(ms.moduleLocs);
    paths = ms.paths + { <ms.moduleLocs[a], r, ms.moduleLocs[b]> | <str a, PathRole r, str b> <- ms.strPaths, ms.moduleLocs[a]?, ms.moduleLocs[b]? };
    extendPlus = {<from, to> | <from, extendPath(), to> <- paths}+;
  
    paths += { <from, extendPath(), to> | <from, to> <- extendPlus };
    
    pathsPlus = {<from, to> | <from, r, to> <- paths}+;
    
    cyclic = {mloc1, mloc2 | <mloc1, mloc2> <- pathsPlus, mloc1 != mloc2,
                             <mloc1, importPath(), mloc2> in paths && <mloc2, extendPath(), mloc1> in paths
                             || <mloc1, extendPath(), mloc2> in paths && <mloc2, importPath(), mloc1> in paths };
    for(mloc <- cyclic){
        mname = getModuleName(mloc, moduleStrs, pcfg);
        set[str] cycle = { getModuleName(mloc2, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc1 == mloc, mloc2 in cyclic } +
                         { getModuleName(mloc1, moduleStrs, pcfg) |  <mloc1, mloc2> <- pathsPlus, mloc2 == mloc , mloc1 in cyclic };
        ms.messages[mname] =  (ms.messages[mname] ? []) + error("Mixed import/extend cycle not allowed: {<intercalate(", ", toList(cycle))>}", mloc);
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
    <found, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
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
                   if(getLastModified(m, ms.moduleLastModified, pcfg) > timestampInBom) {
                        allImportsAndExtendsValid = false;
                        if(m notin ms.invalid){
                            println("--- <m> is no longer valid (latest <getLastModified(m, ms.moduleLastModified, pcfg)>, previous check used <timestampInBom>)");
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
                } catch value e:{
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
    } catch value e: {
        ms.tmodels[qualifiedModuleName] = tmodel()[messages = [ error("Parse error in module `<qualifiedModuleName>`", getModuleLocation(qualifiedModuleName, pcfg)) ]];
    }
    return ms;
}

ModuleStructure getInlineImportAndExtendGraph(Tree pt, PathConfig pcfg){
    ms = newModuleStructure();
    visit(pt){
        case  m: (Module) `<Header header> <Body body>`: {
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
    = (id: defined | <loc scope, str id, moduleId(), loc defined, DefInfo defInfo> <- tm.defines);

loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes){
    try {
        return moduleScopes[qualifiedModuleName];
    } catch NoSuchKey(_): {
        throw "No module scope found for <qualifiedModuleName>";
    }
}

TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, map[str,datetime] moduleLastModified, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes);
        tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
        
        bom = { < m, getLastModified(m, moduleLastModified, pcfg), importPath() > | m <- imports }
            + { < m, getLastModified(m, moduleLastModified, pcfg), extendPath() > | m <- extends }
            + { <qualifiedModuleName, getLastModified(qualifiedModuleName, moduleLastModified, pcfg), importPath() > };
            
        //bom = (m : getLastModified(m, pcfg) | m <- imports + extends);
        //bom[qualifiedModuleName] = getLastModified(qualifiedModuleName, pcfg);
        
        //println("=== BOM <qualifiedModuleName>"); 
        //for(m <- bom) println("<bom[m]>: <m>");
        //println("=== BOM END"); 
        
        extendedModuleScopes = {getModuleScope(m, moduleScopes) | str m <- extends};
        filteredModuleScopes = {getModuleScope(m, moduleScopes) | str m <- (qualifiedModuleName + imports)} + extendedModuleScopes /*+ |global-scope:///|*/;
       
        TModel m1 = tmodel();
        
        m1.modelName = qualifiedModuleName;
        m1.moduleLocs = (qualifiedModuleName : mscope);
        
        m1.facts = (key : tm.facts[key] | key <- tm.facts, any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        //if(tm.config.logImports) println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
        //println("tm.specializedFacts:"); iprintln(tm.specializedFacts);
        m1.specializedFacts = (key : tm.specializedFacts[key] | key <- tm.specializedFacts, any(fms <- filteredModuleScopes, isContainedIn(key, fms)));
        //println("m1.specializedFacts:"); iprintln(m1.specializedFacts);
        m1.messages = tm.messages; //[msg | msg <- tm.messages, msg.at.path == mscope.path];
        
        no_errors = isEmpty(m1.messages) || !any(msg <- m1.messages, error(_,_) := msg);
        
        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        
        m1.scopes = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths);
        
        m1.store = (key_bom : bom);
        m1.store["grammar"] = tm.store["grammar"] ? grammar({}, ());
        m1.store["ADTs"]    = tm.store["ADTs"] ? {};
        m1.store["CommonKeywordFields"]    = tm.store["CommonKeywordFields"] ? [];
        
        m1.paths = { tup | tuple[loc from, PathRole pathRole, loc to] tup <- m1.paths, tup.from == mscope };
        //m1.paths = domainR(tm.paths, {mscope});
        
        //m1.uses = [u | u <- tm.uses, isContainedIn(u.occ, mscope) ];
        m1.useDef = { <u, d> | <u, d> <- tm.useDef, isContainedIn(u, mscope) };
        
        //roles = dataOrSyntaxRoles + {constructorId(), functionId(), fieldId(), keywordFieldId(), annoId()} + anyVariableRoles;
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <loc scope, str id, IdRole idRole, loc defined, DefInfo defInfo> <- tm.defines){
                   if(scope == |global-scope:///| && defined.path in filteredModuleScopePaths || 
                      scope in filteredModuleScopes || 
                      (scope.path == mscope.path && idRole in saveModuleRoles)){
                          
                      if(scope in extendedModuleScopes){
                         tup.scope = mscope;
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
        //reqs  = {r | r <- tm.openReqs, r.src.path == mscope.path, bprintln(r)};
        //
        //println("left: <size(calcs)> calculators, <size(reqs)> requirements");
        
        writeBinaryValueFile(tplLoc, m1);
        if(tm.config.logImports) println("WRITTEN to <tplLoc> (ts=<lastModified(tplLoc)>)");
        return m1;
    } catch value e: {
        return tmodel()[messages=[error("Could not save .tpl file for `<qualifiedModuleName>`: <e>", |unknown:///|(0,0,<0,0>,<0,0>))]];
    }
}