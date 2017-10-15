module lang::rascal::check::Scope

extend typepal::TypePal;
extend typepal::TestFramework;

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
    | visitScope()
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

// Add visibility information to definitions
data DefInfo(Vis vis = publicVis());

// Maintain excluded use in parts of a scope
private str key_exclude_use = "exclude_use";

void storeExcludeUse(Tree cond, Tree excludedPart, FRBuilder frb){
    frb.store(key_exclude_use, <getLoc(cond), getLoc(excludedPart)>);
}

// Maintain allow before use: where variables may be used left (before) their definition
private str key_allow_use_before_def = "allow_use_before_def";

void storeAllowUseBeforeDef(Tree container, Tree allowedPart, FRBuilder frb){
    frb.store(key_allow_use_before_def, <getLoc(container), getLoc(allowedPart)>);
}

// Define the name overloading that is allowed
bool myMayOverload(set[Key] defs, map[Key, Define] defines){
    //println("myMayOverload: <defs>");
    idRoles = {defines[def].idRole | def <- defs};
    //println("idRoles: <idRoles>");
    return    idRoles <= {functionId(), constructorId(), fieldId()}
           || idRoles <= {dataId(), moduleId()} 
           || idRoles <= {fieldId()}
           ;
}

// Name resolution filters

Accept isAcceptableSimple(FRModel frm, Key def, Use use){
    //println("isAcceptableSimple: <use.id> def=<def>, use=<use>");
    res = acceptBinding();

    if(variableId() in use.idRoles){
       // enforce definition before use
       if(def.path == use.scope.path && def < use.scope){
          if(use.occ.offset < def.offset){
             // allow when inside explicitly use before def parts
             if(rel[Key,Key] allowedParts := frm.store[key_allow_use_before_def]){
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
                throw "Inconsistent value stored for <key_allow_use_before_def>: <frm.store[key_allow_use_before_def]>";
             }
          }
          // restrict when in excluded parts of a scope
          if(rel[Key,Key] excludedParts := frm.store[key_exclude_use]){
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
             throw "Inconsistent value stored for <key_allow_use_before_def>: <frm.store[key_allow_use_before_def]>";
          }
       }
    }
    //println("isAcceptableSimple =\> <res>");
    return res;
}

Accept isAcceptablePath(FRModel frm, Key defScope, Key def, Use use, PathRole pathRole) {
    //println("isAcceptablePath <use.id>, candidate <def>, <pathRole>, <use>");
    //iprintln(frm.definitions[def]);
    res = acceptBinding();
    vis = frm.definitions[def].defInfo.vis;
    //println("vis: <vis>");
    if(pathRole == importPath()){
        defIdRole = frm.definitions[def].idRole;
        //println("defIfRole: <defIdRole>");
        //iprintln(frm.paths);
        //println("TEST: <<use.scope, importPath(), defScope> in frm.paths>");
        res = (defIdRole == dataId() || defIdRole == constructorId()) // data declarations and constructors are globally visible
              || //(<use.scope, importPath(), defScope> in frm.paths // one step import only
                  //&& 
                  vis == publicVis()
              ? acceptBinding() 
              : ignoreContinue();
    }
    if(pathRole == extendPath()){
        res = acceptBinding();
    }
    //println("isAcceptablePath =\> <res>");
    return res;
}
