module lang::rascalcore::check::Import

import ValueIO;
import List;
import Map;
import util::Reflective;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::Scope;


import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::ATypeInstantiation;

public str key_imported = "imported";
public str key_extended = "extended";
public str key_expanding_imports = "expanding_imports";
public str key_bom = "bill_of_materials";
public str key_current_module = "current_module";
public str key_import_graph = "import_graph";
public str key_extend_graph = "extend_graph";

AType subsitute(AType atype, map[loc, AType] facts){
    return 
        visit(atype){
            case tv: tvar(loc src) => substitute(facts[src], facts)
        };
}

public map[str, datetime] lastModifiedModules = ();

public set[str] toBeSaved = {};

datetime getLastModified(str qualifiedModuleName, PathConfig pcfg){
    qualifiedModuleName = unescape(qualifiedModuleName);
    if(lastModifiedModules[qualifiedModuleName]?){
        return lastModifiedModules[qualifiedModuleName];
    }
    mloc = getModuleLocation(qualifiedModuleName, pcfg);
    lm = lastModified(mloc);
    lastModifiedModules[qualifiedModuleName] = lm;
    return lm;
}

TModel emptyModel = tmodel();

tuple[bool, TModel] getIfValid(str qualifiedModuleName, PathConfig pcfg){
    lastModSrc = getLastModified(qualifiedModuleName, pcfg);
    tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
    lastModTpl = lastModified(tplLoc);
    if(lastModSrc > lastModTpl) return <false, emptyModel>;
    
    try {
        tm = readBinaryValueFile(#TModel, tplLoc);
        if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
           for(str m <- bom){
               if(bom[m] < getLastModified(m, pcfg)) {
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
            tm = readBinaryValueFile(#TModel, tplLoc);
            if(tm.store[key_bom]? && map[str,datetime] bom := tm.store[key_bom]){
               for(str m <- bom){
                   if(bom[m] < getLastModified(m, pcfg)) {
                        toBeSaved += qualifiedModuleName;
                         println("--- <m> is no longer valid, toBeSaved: <toBeSaved>");
                        return false;
                   }
               }
            }
            println("*** importing <qualifiedModuleName> from <tplLoc>");
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

map[str, loc] getModuleScopes(TModel tm)
    = (id: defined | <Key scope, str id, moduleId(), Key defined, DefInfo defInfo> <- tm.defines);

loc getModuleScope(str qualifiedModuleName, map[str, loc] moduleScopes){
    return moduleScopes[qualifiedModuleName] ? { throw "No module scope found for <qualifiedModuleName>"; };
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
        tms = saveModule(m, import_graph[m] ? {}, extend_graph[m] ? {}, moduleScopes, pcfg, tm);
        //if(m == qualifiedModuleName) iprintln(tms);
    }
}

TModel saveModule(str qualifiedModuleName, set[str] imports, set[str] extends, map[str,loc] moduleScopes, PathConfig pcfg, TModel tm){
    //println("saveModule: <qualifiedModuleName>, <imports>, <extends>, <moduleScopes>");
    
    mscope = getModuleScope(qualifiedModuleName, moduleScopes);
    tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
    
    bom = (m : getLastModified(m, pcfg) | m <- imports + extends);
    bom[qualifiedModuleName] = getLastModified(qualifiedModuleName, pcfg);
    
    println("==== BOM <qualifiedModuleName>"); 
    for(m <- bom) println("<bom[m]>: <m>");
    println("==== BOM END"); 
    
    extendedModuleScopes = {getModuleScope(m, moduleScopes) | str m <- extends};
    filteredModuleScopes = {getModuleScope(m, moduleScopes) | str m <- (qualifiedModuleName + imports)} + extendedModuleScopes + |global-scope:///|;
    //println("filtered: <filteredModuleScopes>");
    m1 = tm;
    
    m1.calculators = ();
    // keep m1.facts
    
    m1.openFacts = {};
    m1.openReqs = {};
    m1.tvScopes = ();
    // keep m1.messages
    m1.messages = {msg | msg <- tm.messages, msg.at.path == mscope.path};
    moduleScopeLocs = range(moduleScopes);
    m1.scopes = (inner : outer | inner <- tm.scopes, outer := tm.scopes[inner], outer in moduleScopeLocs);
    m1.uses = [];
    m1.store = ();
    m1.store[key_bom] = bom;
    
    // Filter model for current module and replace functions in defType by their defined type
    defs = for(tup: <Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- m1.defines){
           if(scope in filteredModuleScopes || (scope.path == mscope.path && idRole in {dataId(), constructorId(), functionId()})){
          //if(scope.scheme == "global-scope" || scope.path == mpath || (scope.path in filteredModules && scope.offset <= 2) /*|| idRole in {dataId(), constructorId()}*/){
             if((defInfo has getAType || defInfo has getATypes)){
               try {                   
                   dt = defType(m1.facts[defined]);
                   if(defInfo.vis?) dt.vis = defInfo.vis;
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
                    tup.defInfo.atype = m1.facts[l];
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
    
       } else if(scope.path == mscope.path && idRole != variableId()) println("remove: <scope.path>: <tup>");
    };
    println("defines: <size(m1.defines)> ==\> <size(defs)>");
    m1.defines = toSet(defs);
    m1.definitions = ();
    m1.definesMap = ();
   
    m1.facts = (key : m1.facts[key] | key <- m1.facts, key.scheme != "typevar", key in filteredModuleScopes);
    println("facts: <size(tm.facts)>  ==\> <size(m1.facts)>");
    
    println("write to <tplLoc>");
    writeBinaryValueFile(tplLoc, m1);
    return m1;
}