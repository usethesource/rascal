module lang::rascalcore::check::Import

import ValueIO;
import util::Reflective;
extend analysis::typepal::TypePal;
import lang::rascalcore::check::AType;
import lang::rascalcore::check::Scope;

tuple[bool, loc] cachedTModelReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "tc", pcfg);
loc cachedTModelWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "tc", pcfg);

tuple[bool,loc] cachedDateReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "sig", pcfg);
loc cachedDateWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "sig", pcfg);

tuple[bool,loc] cachedDateMapReadLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedReadLoc(qualifiedModuleName, "sigs", pcfg);
loc cachedDateMapWriteLoc(str qualifiedModuleName, PathConfig pcfg) = getDerivedWriteLoc(qualifiedModuleName, "sigs", pcfg);

tuple[bool, datetime] getCachedDate(str qualifiedModuleName, PathConfig pcfg) {
    if(<true, l> := cachedDateReadLoc(qualifiedModuleName,pcfg)){
       return <true, readBinaryValueFile(#datetime, l)>;
    }
    return <false, $2017-03-10T18:56:53.386+00:00$>;
}

tuple[bool, TModel] getCachedTModel(str qualifiedModuleName, PathConfig pcfg) {
    if(<true, l> := cachedTModelReadLoc(qualifiedModuleName,pcfg)){
       return <true, readBinaryValueFile(#TModel, l)>;
    }
    return <false,  newConfiguration(pcfg)>;
}    

tuple[bool, map[str,datetime]] getCachedDateMap(str qualifiedModuleName, PathConfig pcfg){
    if(<true, l> := cachedDateMapReadLoc(qualifiedModuleName,pcfg)){
       return <true, readBinaryValueFile(#map[str,datetime], l)>;
    }
    return <false, ()>;
} 

void writeCachedDate(str qualifiedModuleName, PathConfig pcfg, datetime dateval) {
    l = cachedDateWriteLoc(qualifiedModuleName,pcfg);
    if (!exists(l.parent)) mkDirectory(l.parent);
    writeBinaryValueFile(l, dateval); 
}
void writeCachedTModel(str qualifiedModuleName, PathConfig pcfg, TModel tm) {
    l = cachedTModelWriteLoc(qualifiedModuleName, pcfg); 
    if (!exists(l.parent)) mkDirectory(l.parent);
    writeBinaryValueFile(l, tm); 
}
void writeCachedDateMap(str qualifiedModuleName, PathConfig pcfg, map[str,datetime] m) {
    l = cachedDateMapWriteLoc(qualifiedModuleName, pcfg); 
    if (!exists(l.parent)) mkDirectory(l.parent);
    writeBinaryValueFile(l, m); 
}

bool addImport(str qualifiedModuleName, PathConfig pcfg, TBuilder tb){
    return false;
    <found, tplLoc> = getDerivedReadLoc(qualifiedModuleName, "tpl", pcfg);
    if(found){
        try {
            tm = readBinaryValueFile(#TModel, tplLoc);
            println("read <tplLoc>");
            tb.addTModel(tm);
            return true;
        } catch IO(str msg): {
            // tb.reportWarning()
            return false;
        }
    } else 
        return false;
   
}

void saveModule(str qualifiedModuleName, PathConfig pcfg, TModel tm){
    mpath = getModuleLocation(qualifiedModuleName, pcfg).path;
    tplLoc = getDerivedWriteLoc(qualifiedModuleName, "tpl", pcfg);
    m1 = tm;
    
    //println("MODEL <qualifiedModuleName> BEFORE REDUCTION");
    //iprintln(m1);
    
    m1.calculators = ();
    // keep m1.facts
    m1.openFacts = {};
    m1. openReqs = {};
    m1.tvScopes = ();
    // keep m1.messages
    
    // replace functions in defType by the defined type
    defs = for(tup: <Key scope, str id, IdRole idRole, Key defined, DefInfo defInfo> <- m1.defines){
       if(scope.path == mpath || scope.path == "/"){
           //println("consider: <tup>");
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
           }
           append tup;
       } //else println("remove: <tup>");
    };
    println("defines from <size(m1.defines)> to <size(defs)>");
   
    m1.defines = toSet(defs);
    m1.definitions = ();
    m1.definesMap = ();
    //println("MODEL <qualifiedModuleName> AFTER REDUCTION");
    //iprintln(m1);
    println("write to <tplLoc>");
    writeBinaryValueFile(tplLoc, m1);
}
