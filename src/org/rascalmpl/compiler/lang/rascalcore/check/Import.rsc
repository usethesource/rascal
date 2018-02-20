module lang::rascalcore::check::Import

import ValueIO;
import IO;
import List;
import Map;
import Set;
import Exception;
import util::Reflective;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::TypePalConfig;


import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

public str key_imported = "imported";
public str key_extended = "extended";
public str key_expanding_imports = "expanding_imports";
public str key_bom = "bill_of_materials";
public str key_current_module = "current_module";
public str key_import_graph = "import_graph";
public str key_extend_graph = "extend_graph";
public str key_processed_modules = "processed_modules";

//AType subsitute(AType atype, map[loc, AType] facts){
//    return 
//        visit(atype){
//            case tv: tvar(loc src) => substitute(facts[src], facts)
//        };
//}

private map[str, datetime] lastModifiedModules = ();
private set[str] toBeSaved = {};

void init_Import(){
    toBeSaved = {};
    lastModifiedModules = ();
}

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <Key scope, str id, moduleId(), Key defined, DefInfo defInfo> <- tm.defines);

loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes){
    try {
        return moduleScopes[qualifiedModuleName];
    } catch NoSuchKey(_): {
        throw "No module scope found for <qualifiedModuleName>";
    }
}

tuple[bool,loc] TPLReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);

datetime getLastModified(str qualifiedModuleName, PathConfig pcfg, bool fresh = false){
    qualifiedModuleName = unescape(qualifiedModuleName);
    if(!fresh && lastModifiedModules[qualifiedModuleName]?){
        return lastModifiedModules[qualifiedModuleName];
    }
    try {
        mloc = getModuleLocation(qualifiedModuleName, pcfg);
        lm = lastModified(mloc);
        lastModifiedModules[qualifiedModuleName] = lm;
        return lm;
    } catch value e: {
        if(<true, tplLoc> := TPLReadLoc(qualifiedModuleName, pcfg)){
           lm = lastModified(tplLoc);
           lastModifiedModules[qualifiedModuleName] = lm;
            return lm;
        }
        throw "No source or tpl loc found for <qualifiedModuleName>";
    }
}

TModel emptyModel = tmodel();

tuple[bool, TModel] getIfValid(str qualifiedModuleName, PathConfig pcfg){
    lastModSrc = getLastModified(qualifiedModuleName, pcfg, fresh = true);
    
    <existsTpl, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
    if(!existsTpl) return <false, emptyModel>;
    
    lastModTpl = lastModified(tplLoc);
    if(lastModSrc > lastModTpl) return <false, emptyModel>;
    
    try {
        tm = readBinaryValueFile(#TModel, tplLoc);
        if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
           for(str m <- bom){
               if(bom[m] < getLastModified(m, pcfg)) {
                  println("<m> out of date: <bom[m]> vs <getLastModified(m, pcfg)>");
                  return <false, emptyModel>;
               }
           }
        }
        return <true, tm>;
    } catch IO(str msg): {
        // tb.reportWarning()
        return <false, emptyModel>;
    }
}

bool addImport(str qualifiedModuleName, PathConfig pcfg, TBuilder tb){
    qualifiedModuleName = unescape(qualifiedModuleName);

    //return false;
    <found, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
    if(found){
        try {
            println("addImport: <qualifiedModuleName> from <tplLoc>");
            tm = readBinaryValueFile(#TModel, tplLoc);
            if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
               println("=== BOM");
               for(str m <- bom){ println("<bom[m]>: <m> (lm: <getLastModified(m, pcfg)>)"); }
               println("=== BOM");
               for(str m <- bom, m != qualifiedModuleName){
                   if(bom[m] < getLastModified(m, pcfg)) {
                        toBeSaved += qualifiedModuleName;
                        println("--- <m> is no longer valid, toBeSaved: <toBeSaved>");
                        return false;
                   }
               }
            }
            println("*** importing <qualifiedModuleName> from <tplLoc>");
            //iprintln(tm);
            tb.addTModel(tm);
            if(list[str] imps := tm.store[key_imported] ? []){
                for(imp <- toSet(imps)) tb.push(key_imported, imp);
            }
            return true;
        } catch IO(str msg): {
            // tb.reportWarning()
            return false;
        }
    } else {
        toBeSaved += qualifiedModuleName;
        return false;
    }
}

void saveModules(str qualifiedModuleName, PathConfig pcfg, TModel tm){     
    qualifiedModuleName = unescape(qualifiedModuleName); 
    
    rel[str,str] import_graph = {};
    if(tm.store[key_import_graph]? && lrel[str,str] imps := tm.store[key_import_graph]){
        import_graph = {<unescape(f), unescape(t)> | <f, t> <- toSet(imps)};
    }
    
    rel[str,str] extend_graph = {};
    if(tm.store[key_extend_graph]? && lrel[str,str] exts := tm.store[key_extend_graph]){
        extend_graph = {<unescape(f), unescape(t)> | <f, t> <- toSet(exts)};
    }
    moduleScopes = getModuleScopes(tm);
    
    for(m <- qualifiedModuleName + {unescape(tbs) | tbs <- toBeSaved}){
        tms = saveModule(m, import_graph[m] ? {}, (extend_graph+)[m] ? {}, moduleScopes, pcfg, tm);
        //if(m == "lang::rascal::syntax::Rascal") iprintln(tms, lineLimit=15000);
    }
}

TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    try {
        mscope = getModuleScope(qualifiedModuleName, moduleScopes);
        tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
        
        bom = (m : getLastModified(m, pcfg) | m <- imports + extends);
        bom[qualifiedModuleName] = getLastModified(qualifiedModuleName, pcfg);
        
        println("=== BOM <qualifiedModuleName>"); 
        for(m <- bom) println("<bom[m]>: <m>");
        println("=== BOM END"); 
        
        extendedModuleScopes = {getModuleScope(m, moduleScopes) | str m <- extends};
        filteredModuleScopes = {getModuleScope(m, moduleScopes) | str m <- (qualifiedModuleName + imports)} + extendedModuleScopes /*+ |global-scope:///|*/;
        //println("filtered: <filteredModuleScopes>");
        TModel m1 = tmodel();
        
        m1.facts = (key : tm.facts[key] | key <- tm.facts, key in filteredModuleScopes);
        //m1.facts = (key : tm.facts[key] | key <- tm.facts, any(fms <- filteredModuleScopes, containedIn(key, fms)));
        println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
     
        m1.messages = [msg | msg <- tm.messages, msg.at.path == mscope.path];
        
        filteredModuleScopePaths = {ml.path |loc  ml <- filteredModuleScopes};
        //println("filteredModuleScopePaths: <filteredModuleScopePaths>");
        m1.scopes = (inner : tm.scopes[inner] | loc inner <- tm.scopes, inner.path in filteredModuleScopePaths);
        //println("scopes: <size(tm.scopes)> ==\> <size(m1.scopes)>");
       
        m1.store = (key_bom : bom);
        m1.paths = tm.paths;
        //m1.uses = [u | u <- tm.uses, containedIn(u.occ, mscope) ];
        
        roles = dataOrSyntaxIds + {constructorId(), functionId(), fieldId()/*, variableId()*/};
        // Filter model for current module and replace functions in defType by their defined type
        
        defs = for(tup: <Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- tm.defines){
               if(scope == |global-scope:///| && defined.path in filteredModuleScopePaths || 
                  scope in filteredModuleScopes || 
                  (scope.path == mscope.path && idRole in roles)){
                 if(id == "type" && idRole == constructorId()){  
                    continue; // exclude builtin constructor for "type"
                 } else {
                     if((defInfo has getAType || defInfo has getATypes)){
                       try {                   
                           dt = defType(tm.facts[defined]);
                           if(defInfo.vis?) dt.vis = defInfo.vis;
                           if(defInfo.tags?) dt.tags = defInfo.tags;
                           if(defInfo.constructorFields?) dt.constructorFields = defInfo.constructorFields;
                           if(defInfo.productions?) dt.productions = defInfo.productions;
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
               
                   if(scope in extendedModuleScopes){
                    tup.scope = mscope;
                   }
                     
                   append tup;
               }
        
           } //else if(scope.path == mscope.path && idRole != variableId()) println("remove: <scope.path>: <tup>");
        };
        println("defines: <size(tm.defines)> ==\> <size(defs)>");
        m1.defines = toSet(defs);
        
        //calcs = (key : tm.calculators[key] | Key key <- tm.calculators, key.path == mscope.path, bprintln("<key>: <tm.calculators[key]>"));
        //
        //reqs  = {r | r <- tm.openReqs, r.src.path == mscope.path, bprintln(r)};
        //
        //println("left: <size(calcs)> calculators, <size(reqs)> requirements");
        println("write to <tplLoc>");
        writeBinaryValueFile(tplLoc, m1);
        return m1;
    } catch value e: {
        return tmodel()[messages=[error("Could not save .tpl file for <fmt(qualifiedModuleName)>: <fmt(e)>", |unknown:///|(0,0,<0,0>,<0,0>))]];
    }
}

