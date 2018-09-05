@bootstrapParser
module lang::rascalcore::check::Import


extend analysis::typepal::TypePal;

extend lang::rascalcore::check::AType;
extend lang::rascalcore::check::Checker;

import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::TypePalConfig;

import ValueIO;
import IO;
import List;
import Map;
import Set;
import Exception;
import String;
import util::Reflective;
import analysis::graphs::Graph;

import lang::rascal::\syntax::Rascal;

public str key_bom = "bill_of_materials";
public str key_current_module = "current_module";

tuple[bool,loc] TPLReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);

datetime getLastModified(str qualifiedModuleName, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    try {
        mloc = getModuleLocation(qualifiedModuleName, pcfg);
        return lastModified(mloc);
    } catch value e: {
        return $2000-01-01T00:00:00.000+00:00$;
        //if(<true, tplLoc> := TPLReadLoc(qualifiedModuleName, pcfg)){
        //   return lastModified(tplLoc);
        //}
        //throw "No source or tpl loc found for <qualifiedModuleName>";
    }
}

alias ModuleStructure = tuple[rel[str, PathRole, str] strPaths, 
                              rel[loc, PathRole, loc] paths, 
                              map[str,TModel] tmodels, 
                              map[str,loc] moduleLocs, 
                              map[str,Module] modules,
                              set[str] valid,
                              set[str] invalid
                              ];
void printModuleStructure(ModuleStructure ms){
    println("strPaths:"); iprintln(ms.strPaths);
    println("paths:"); iprintln(ms.paths);
    println("tmodels for: <domain(ms.tmodels)>");
    println("moduleLocs for: <domain(ms.moduleLocs)>");
    println("modules for: <domain(ms.modules)>");
    println("valid: <ms.valid>");
}

ModuleStructure newModuleStructure() = <{}, {}, (), (), (), {}, {}>;

str getModuleName(loc mloc, map[loc,str] moduleStrs, PathConfig pcfg){
    return moduleStrs[mloc]? ? moduleStrs[mloc] : getModuleName(mloc, pcfg);
}

// Complete an ModuleStructure by adding a contains relation that adds transitive edges for extend
ModuleStructure complete(ModuleStructure ms, PathConfig pcfg){
    //println("complete, paths:");
    //iprintln(ms.paths);
    paths = ms.paths + { <ms.moduleLocs[a], r, ms.moduleLocs[b]> | <str a, PathRole r, str b> <- ms.strPaths, ms.moduleLocs[a]?, ms.moduleLocs[b]? };
    extendPlus = {<from, to> | <from, extendPath(), to> <- paths}+;
    paths += { <from, extendPath(), to> | <from, to> <- extendPlus };
    paths += { <c, importPath(), a> | < c, importPath(), b> <- paths,  <b , extendPath(), a> <- paths};
    ms.paths = paths;
    moduleStrs = invertUnique(ms.moduleLocs);
    //ms.strPaths = { < moduleStrs[from], r, moduleStrs[to] > | <loc from, PathRole r, loc to> <- paths, moduleStrs[from]?, moduleStrs[to]?};  
    ms.strPaths = { < getModuleName(from, moduleStrs, pcfg), r, getModuleName(to, moduleStrs, pcfg) > | <loc from, PathRole r, loc to> <- paths};  
    return ms;
}

ModuleStructure getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, bool showImports){
    return complete(getImportAndExtendGraph(qualifiedModuleName, pcfg, {}, newModuleStructure(), showImports), pcfg);
}

ModuleStructure getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, set[str] visited, ModuleStructure ms, bool showImports){
    qualifiedModuleName = unescape(qualifiedModuleName);
   
    //println("getImportAndExtendGraph: <qualifiedModuleName>, <domain(ms.modules)>, <domain(ms.tmodels)>");
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
                   if(getLastModified(m, pcfg) > timestampInBom) {
                        allImportsAndExtendsValid = false;
                        if(m notin ms.invalid){
                            println("--- <m> is no longer valid (latest <getLastModified(m, pcfg)>, previous check used <timestampInBom>)");
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
                    println("--- reusing outdated <qualifiedModuleName> (source not accessible)");
                }
            }
            if(allImportsAndExtendsValid){
                if(showImports) println("*** importing <qualifiedModuleName> from <tplLoc> (ts=<lastModified(tplLoc)>)");
          
                ms.valid += {qualifiedModuleName};
                ms.tmodels[qualifiedModuleName] = tm;
                ms.moduleLocs += tm.moduleLocs;
                ms.paths += tm.paths;
                ms.strPaths += {<qualifiedModuleName, pathRole, imp> | <str imp, PathRole pathRole> <- localImportsAndExtends };
                visited += qualifiedModuleName;
                for(imp <- localImportsAndExtends<0>, imp notin visited){
                    ms = getImportAndExtendGraph(imp, pcfg, visited, ms, showImports);
                }
                //println("allImportsAndExtendsValid, return:");
                //printModuleStructure(ms);
                return ms;
             }
        } catch IO(str msg): {
            //c.report(warning(ModuleStructurement, "During import of %q: %v", qualifiedModuleName, msg));
            println("IO Error during import of <qualifiedModuleName>: <msg>");
        }
    }
    try {
        Module pt = [Module] "module xxx";
        if(ms.modules[qualifiedModuleName]?){
            pt = ms.modules[qualifiedModuleName];
        } else {
            mloc = getModuleLocation(qualifiedModuleName, pcfg);                    
            if(showImports) println("*** parsing <qualifiedModuleName> from <mloc>");
            pt = parseModuleWithSpaces(mloc).top;
            ms.modules[qualifiedModuleName] = pt;
            ms.moduleLocs[qualifiedModuleName] = getLoc(pt);
        }
        imports_and_extends = getModulePathsAsStr(pt);
        ms.strPaths += imports_and_extends;
        visited += qualifiedModuleName;
        for(imp <- imports_and_extends<2>){
            ms = getImportAndExtendGraph(imp, pcfg, visited, ms, showImports);
      }
    } catch value e: {
        //c.report(error(importStatement, "Error during import of %v: %v", mname, e));
        println("Error during import of <qualifiedModuleName>: <e>");
    }
    //println("After reparse, return:");
    //printModuleStructure(ms);
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

TModel emptyModel = tmodel();

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

TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes);
        tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
        
        bom = { < m, getLastModified(m, pcfg), importPath() > | m <- imports }
            + { < m, getLastModified(m, pcfg), extendPath() > | m <- extends }
            + { <qualifiedModuleName, getLastModified(qualifiedModuleName, pcfg), importPath() > };
            
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
        
        m1.facts = (key : tm.facts[key] | key <- tm.facts, any(fms <- filteredModuleScopes, containedIn(key, fms)));
        if(tm.config.showImports) println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
     
        m1.messages = [msg | msg <- tm.messages, msg.at.path == mscope.path];
        
        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        
        m1.scopes = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths);
               
        m1.store = (key_bom : bom);
        m1.paths = { tup | tuple[loc from, PathRole pathRole, loc to] tup <- m1.paths, tup.from == mscope };
        //m1.paths = domainR(tm.paths, {mscope});
        
        //m1.uses = [u | u <- tm.uses, containedIn(u.occ, mscope) ];
        m1.useDef = { <u, d> | <u, d> <- tm.useDef, containedIn(u, mscope) };
        
        roles = dataOrSyntaxIds + {constructorId(), functionId(), fieldId(), annoId(), variableId()};
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <loc scope, str id, IdRole idRole, loc defined, DefInfo defInfo> <- tm.defines){
                   if(scope == |global-scope:///| && defined.path in filteredModuleScopePaths || 
                      scope in filteredModuleScopes || 
                      (scope.path == mscope.path && idRole in roles)){
                          
                      if(scope in extendedModuleScopes){
                         tup.scope = mscope;
                      }
                       if((defInfo has getAType || defInfo has getATypes)){
                       try {                   
                           dt = defType(tm.facts[defined]);
                           if(defInfo.vis?) dt.vis = defInfo.vis;
                            if(defInfo.nestedParameters?) dt.nestedParameters = defInfo.nestedParameters;
                           if(defInfo.tags?) dt.tags = defInfo.tags;
                           tup.defInfo = dt;
                           //println("Changed <defInfo> ==\> <dt>");
                       } catch NoSuchKey(k): {
                        //println("ignore: <tup>");
                        continue;
                       }
                    } 
                    //else if(defInfo has atype){
                    //  if(tvar(l) := defInfo.atype) {
                    //     try {
                    //        tup.defInfo.atype = tm.facts[l];
                    //     } catch NoSuchKey(v):{
                    //        println("*** <v> is undefined");
                    //        tup.defInfo.atype = avalue();
                    //     }
                    //   }
                    //}
                    append tup;
                  }
               };
        
        if(tm.config.showImports) println("defines: <size(tm.defines)> ==\> <size(defs)>");
        m1.defines = toSet(defs);
        m1 = visit(m1) {case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""]; };
        
        //calcs = (key : tm.calculators[key] | loc key <- tm.calculators, key.path == mscope.path, bprintln("<key>: <tm.calculators[key]>"));
        //
        //reqs  = {r | r <- tm.openReqs, r.src.path == mscope.path, bprintln(r)};
        //
        //println("left: <size(calcs)> calculators, <size(reqs)> requirements");
        
        writeBinaryValueFile(tplLoc, m1);
        if(tm.config.showImports) println("WRITTEN to <tplLoc> (ts=<lastModified(tplLoc)>)");
        return m1;
    } catch value e: {
        return tmodel()[messages=[error("Could not save .tpl file for `<qualifiedModuleName>`: <e>", |unknown:///|(0,0,<0,0>,<0,0>))]];
    }
}