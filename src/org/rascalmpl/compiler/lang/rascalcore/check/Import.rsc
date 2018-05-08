@bootstrapParser
module lang::rascalcore::check::Import

import ValueIO;
import IO;
import List;
import Map;
import Set;
import Exception;
import String;
import util::Reflective;
import analysis::graphs::Graph;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::TypePalConfig;


import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascalcore::check::Checker;
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
        if(<true, tplLoc> := TPLReadLoc(qualifiedModuleName, pcfg)){
           return lastModified(tplLoc);
        }
        throw "No source or tpl loc found for <qualifiedModuleName>";
    }
}

alias ImportState = tuple[rel[str,str] imports, rel[str,str] extends, rel[str,str] contains, map[str,TModel] tmodels, map[str,Module] modules];

ImportState newImportState() = <{}, {}, {}, (), ()>;

ImportState getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg){
    istate = getImportAndExtendGraph(qualifiedModuleName, pcfg, {}, newImportState());
    // add a contains relation that adss transitive edges for extend
    extendPlus = istate.extends+;
    istate.contains = istate.imports + extendPlus + { <c, a> | < str c, str b> <- istate.imports,  <b , str a> <- extendPlus};
    return istate;
}

ImportState getImportAndExtendGraph(str qualifiedModuleName, PathConfig pcfg, set[str] visited, ImportState state){
    qualifiedModuleName = unescape(qualifiedModuleName);
   
    //println("getImportAndExtendGraph: <qualifiedModuleName>, <domain(state.modules)>, <domain(state.tmodels)>");
    if(state.modules[qualifiedModuleName]? || state.tmodels[qualifiedModuleName]?){
        return state;
    }
    <found, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
    if(found){
        try {
            allImportsValid = true;
            localImports = {};
            tm = readBinaryValueFile(#TModel, tplLoc);
            if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
               for(str m <- bom){
                   if(m != qualifiedModuleName){
                        localImports += m;
                   }
                   if(bom[m] < getLastModified(m, pcfg)) {
                        allImportsValid = false;
                        println("--- <m> is no longer valid");
                   }
               }
            }
            if(allImportsValid){
                println("*** importing <qualifiedModuleName> from <tplLoc> (ts=<lastModified(tplLoc)>)");
          
                state.tmodels[qualifiedModuleName] = tm;
                //state.imports += {<qualifiedModuleName, imp> | imp <- localImports };
                visited += qualifiedModuleName;
                //for(imp <- localImports, imp notin visited){
                //    state = getImportAndExtendGraph(imp, pcfg, visited, state);
                //}
             }
        } catch IO(str msg): {
            //c.report(warning(importStatement, "During import of %q: %v", qualifiedModuleName, msg));
            println("IO Error during import of <qualifiedModuleName>: <msg>");
        }
    } else {
         try {
            Module pt = [Module] "module xxx";
            if(state.modules[qualifiedModuleName]?){
                pt = state.modules[qualifiedModuleName];
            } else {
                mloc = getModuleLocation(qualifiedModuleName, pcfg);                    
                println("*** parsing <qualifiedModuleName> from <mloc>");
                pt = parseModuleWithSpaces(mloc).top;
                state.modules[qualifiedModuleName] = pt;
            }
            localImports = getImports(pt);
            localExtends = getExtends(pt);
            state.imports += { <qualifiedModuleName, imp> | imp <- localImports };
            state.extends += { <qualifiedModuleName, imp> | imp <- localExtends };
            visited += qualifiedModuleName;
            for(imp <- localImports + localExtends){
                state = getImportAndExtendGraph(imp, pcfg, visited, state);
          }
        } catch value e: {
            //c.report(error(importStatement, "Error during import of %v: %v", mname, e));
            println("Error during import of <qualifiedModuleName>: <e>");
        }
    }
    return state;
}

set[str] getImportsAndExtends(Module m)   
    = { unescape("<imod.\module.name>") | imod <- m.header.imports, imod has \module };

set[str] getImports(Module m)   
    = { unescape("<imod.\module.name>") | imod <- m.header.imports, imod has \module, imod is \default};

set[str] getExtends(Module m)   
    = { unescape("<imod.\module.name>") | imod <- m.header.imports, imod has \module, imod is \extend};

TModel emptyModel = tmodel();

tuple[bool, TModel] getIfValid(str qualifiedModuleName, PathConfig pcfg){
    //println("getIfValid: <qualifiedModuleName>");
    
    <existsTpl, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
    if(!existsTpl) { 
        //println("getIfValid, !existsTpl: false");
        return <false, emptyModel>;
    }
    lastModTpl = lastModified(tplLoc);
    
    existsSrc = false;
    lastModSrc = lastModTpl;
    try {
        mloc = getModuleLocation(qualifiedModuleName, pcfg);
        existsSrc = true;
        lastModSrc = getLastModified(qualifiedModuleName, pcfg);
    } catch value e:{
    ;
    }

    if(lastModSrc > lastModTpl) {
        //println("getIfValid, lastModSrc \> lastModTpl: false"); 
        return <false, emptyModel>;
    }
    
    try {
        tm = readBinaryValueFile(#TModel, tplLoc);
        if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
       
           for(str m <- bom){
               if(bom[m] < getLastModified(m, pcfg)) {
                  if(existsSrc){
                     if(m != qualifiedModuleName){
                        println("<m> out of date: in BOM <bom[m]> vs current <getLastModified(m, pcfg)>");
                        return <false, emptyModel>;
                     }
                  } else {
                   if(m != qualifiedModuleName)
                        println("reusing outdated <m> (source not accessible)");
                  }
               }
           }
        }
        return <true, tm>;
    } catch IO(str msg): {
        // c.reportWarning()
        return <false, emptyModel>;
    }
}

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

ImportState saveModuleAndImports(str qualifiedModuleName, PathConfig pcfg, TModel tm, ImportState istate){     
    qualifiedModuleName = unescape(qualifiedModuleName); 
    
    rel[str,str] import_graph = istate.imports;  
    rel[str,str] extend_graph = istate.extends;
    
    // Replace all getAType functions by their value
    defs = for(tup: <loc scope, str id, IdRole idRole, loc defined, DefInfo defInfo> <- tm.defines){
         if(id == "type" && idRole == constructorId()){  
            continue; // exclude builtin constructor for "type"
         } else {
             if((defInfo has getAType || defInfo has getATypes)){
               try {                   
                   dt = defType(tm.facts[defined]);
                   if(defInfo.vis?) dt.vis = defInfo.vis;
                   if(defInfo.tags?) dt.tags = defInfo.tags;
                   tup.defInfo = dt;
                   //println("Changed <defInfo> ==\> <dt>");
               } catch NoSuchKey(k): {
                //println("ignore: <tup>");
                continue;
               }
           } else if(defInfo has atype){
              if(tvar(l) := defInfo.atype) {
                 try {
                    tup.defInfo.atype = tm.facts[l];
                 } catch NoSuchKey(v):{
                    println("*** <v> is undefined");
                    tup.defInfo.atype = avalue();
                 }
               }
           }
           append tup;
       }
    }
   
    tm.defines = toSet(defs);
    moduleScopes = getModuleScopes(tm);
    
    toBeSaved = {imp  | imp <- istate.imports[qualifiedModuleName], !istate.tmodels[imp]? };
    
    for(m <- qualifiedModuleName + toBeSaved){
        tms = saveModule(m, import_graph[m] ? {}, (extend_graph+)[m] ? {}, moduleScopes, pcfg, tm);
        istate.tmodels[m] = tms;
    }
    istate.modules = domainX(istate.modules, {qualifiedModuleName + toBeSaved});
    return istate;
}

TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes);
        tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
        
        bom = (m : getLastModified(m, pcfg) | m <- imports + extends);
        bom[qualifiedModuleName] = getLastModified(qualifiedModuleName, pcfg);
        
        //println("=== BOM <qualifiedModuleName>"); 
        //for(m <- bom) println("<bom[m]>: <m>");
        //println("=== BOM END"); 
        
        extendedModuleScopes = {getModuleScope(m, moduleScopes) | str m <- extends};
        filteredModuleScopes = {getModuleScope(m, moduleScopes) | str m <- (qualifiedModuleName + imports)} + extendedModuleScopes /*+ |global-scope:///|*/;
       
        TModel m1 = tmodel();
        
        m1.facts = (key : tm.facts[key] | key <- tm.facts, any(fms <- filteredModuleScopes, containedIn(key, fms)));
        println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
     
        m1.messages = [msg | msg <- tm.messages, msg.at.path == mscope.path];
        
        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        
        m1.scopes = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths);
               
        m1.store = (key_bom : bom);
        m1.paths = tm.paths;
        
        //m1.uses = [u | u <- tm.uses, containedIn(u.occ, mscope) ];
        
        roles = dataOrSyntaxIds + {constructorId(), functionId(), fieldId(), annoId() /*, variableId()*/};
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <loc scope, str id, IdRole idRole, loc defined, DefInfo defInfo> <- tm.defines){
                   if(scope == |global-scope:///| && defined.path in filteredModuleScopePaths || 
                      scope in filteredModuleScopes || 
                      (scope.path == mscope.path && idRole in roles)){
                          
                      if(scope in extendedModuleScopes){
                         tup.scope = mscope;
                      }
                      append tup;
                  }
               };
        
        println("defines: <size(tm.defines)> ==\> <size(defs)>");
        m1.defines = toSet(defs);
        m1 = visit(m1) {case loc l : if(!isEmpty(l.fragment)) insert l[fragment=""]; };
        
        //calcs = (key : tm.calculators[key] | loc key <- tm.calculators, key.path == mscope.path, bprintln("<key>: <tm.calculators[key]>"));
        //
        //reqs  = {r | r <- tm.openReqs, r.src.path == mscope.path, bprintln(r)};
        //
        //println("left: <size(calcs)> calculators, <size(reqs)> requirements");
        
        writeBinaryValueFile(tplLoc, m1);
        println("WRITTEN to <tplLoc> (ts=<lastModified(tplLoc)>)");
        return m1;
    } catch value e: {
        return tmodel()[messages=[error("Could not save .tpl file for `<qualifiedModuleName>`: <e>", |unknown:///|(0,0,<0,0>,<0,0>))]];
    }
}