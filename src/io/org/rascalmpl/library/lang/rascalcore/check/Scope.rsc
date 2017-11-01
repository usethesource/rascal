module lang::rascalcore::check::Scope

extend analysis::typepal::TypePal;
extend analysis::typepal::TestFramework;

import lang::rascalcore::check::AType;

data IdRole
    = moduleId()
    | functionId()
    | variableId()
    | formalId()
    | labelId()
    | constructorId()
    | fieldId()
    | dataId()
    | aliasId()
    | annoId()
    | nonterminalId()
    | lexicalId()
    ;

data PathRole
    = importPath()
    | extendPath()
    ;
    
data ScopeRole
    = moduleScope()
    | functionScope()
    | conditionalScope()
    | replacementScope()
    | visitOrSwitchScope()
    | boolScope()
    | loopScope()
    ;

data Vis
    = publicVis()
    | privateVis()
    | defaultVis()
    ;

data Modifier
    = javaModifier()
    | testModifier()
    | defaultModifier()
    ;

// Visibility information
data DefInfo(Vis vis = publicVis());

// Productions and Constructor fields; common Keyword fields
data DefInfo(set[AProduction] productions = {}, 
             rel[str fieldName, AType fieldType] constructorFields = {},
             list[Keyword] commonKeywordFields = []
             );

// Maintain excluded use in parts of a scope
private str key_exclude_use = "exclude_use";

void storeExcludeUse(Tree cond, Tree excludedPart, TBuilder tb){
    tb.store(key_exclude_use, <getLoc(cond), getLoc(excludedPart)>);
}

// Maintain allow before use: where variables may be used left (before) their definition
private str key_allow_use_before_def = "allow_use_before_def";

void storeAllowUseBeforeDef(Tree container, Tree allowedPart, TBuilder tb){
    tb.store(key_allow_use_before_def, <getLoc(container), getLoc(allowedPart)>);
}

// Define the name overloading that is allowed
bool myMayOverload(set[Key] defs, map[Key, Define] defines){
    //println("myMayOverload: <defs>");
    idRoles = {defines[def].idRole | def <- defs};
    //println("idRoles: <idRoles>");
    res =    idRoles <= {functionId(), constructorId(), fieldId()}
           || idRoles <= {dataId(), moduleId(), nonterminalId()} 
           || idRoles <= {fieldId()}
           || idRoles <= {annoId()}
           ;
    
    //println("myMayOverload ==\> <res>");
    return res;
}

// Name resolution filters

Accept isAcceptableSimple(TModel tm, Key def, Use use){
    //println("isAcceptableSimple: <use.id> def=<def>, use=<use>");
    res = acceptBinding();

    if(variableId() in use.idRoles){
       // enforce definition before use
       if(def.path == use.scope.path && def < use.scope){
          if(use.occ.offset < def.offset){
             // allow when inside explicitly use before def parts
             if(rel[Key,Key] allowedParts := tm.store[key_allow_use_before_def]){
                 set[Key] parts = allowedParts[use.scope];
                 if(!isEmpty(parts)){
                    if(any(part <- parts, use.occ < part)){
                       return res;
                    }
                  } else {
                    res = ignoreContinue();
                    //println("isAcceptableSimple =\> <res>");
                    return res;
                 }
             } else {
                throw "Inconsistent value stored for <key_allow_use_before_def>: <tm.store[key_allow_use_before_def]>";
             }
          }
          // restrict when in excluded parts of a scope
          if(rel[Key,Key] excludedParts := tm.store[key_exclude_use]){
              set[Key] parts = excludedParts[use.scope];
              //println("parts = <parts>, <any(part <- parts, use.occ < part)>");
              if(!isEmpty(parts)){
                 if(any(part <- parts, use.occ < part)){
                    res = ignoreContinue();
                   //println("isAcceptableSimple =\> <res>");
                   return res;
                 }
              } 
          } else {
             throw "Inconsistent value stored for <key_allow_use_before_def>: <tm.store[key_allow_use_before_def]>";
          }
       }
    }
    //println("isAcceptableSimple =\> <res>");
    return res;
}

Accept isAcceptablePath(TModel tm, Key defScope, Key def, Use use, PathRole pathRole) {
    //println("isAcceptablePath <use.id>, candidate <def>, <pathRole>, <use>");
    //iprintln(tm.definitions[def]);
    res = acceptBinding();
    vis = tm.definitions[def].defInfo.vis;
    //println("vis: <vis>");
    if(pathRole == importPath()){
        defIdRole = tm.definitions[def].idRole;
        //println("defIfRole: <defIdRole>");
        //iprintln(tm.paths);
        //println("TEST: <<use.scope, importPath(), defScope> in tm.paths>");
        res = (defIdRole == dataId() || defIdRole == constructorId()) // data declarations and constructors are globally visible
              || //(<use.scope, importPath(), defScope> in tm.paths // one step import only
                  //&& 
                  vis == publicVis()
              ? acceptBinding() 
              : ignoreContinue();
    } else
    if(pathRole == extendPath()){
        res = acceptBinding();
    }
    //println("isAcceptablePath =\> <res>");
    return res;
}
