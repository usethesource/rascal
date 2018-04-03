module lang::rascalcore::check::TypePalConfig
 
extend analysis::typepal::TypePal;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
extend lang::rascalcore::check::Expression;

import lang::rascal::\syntax::Rascal;
import List;
import Set;
import String;
import Map;

data IdRole
    = moduleId()
    | functionId()
    | labelId()
    | constructorId()
    | fieldId()
    | dataId()
    | aliasId()
    | annoId()
    | nonterminalId()
    | lexicalId()
    | layoutId()
    | keywordId()
    | typeVarId()
    ;

public set[IdRole] syntaxIds = {aliasId(), nonterminalId(), lexicalId(), layoutId(), keywordId()};
public set[IdRole] dataOrSyntaxIds = {dataId()} + syntaxIds;
public set[IdRole] dataIds = {aliasId(), dataId()}; 

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

data DefInfo(map[str,str] tags = ());

// Productions and Constructor fields; common Keyword fields
data DefInfo(//set[AProduction] productions = {}, 
             bool isStart = false,
             //set[AType] constructorFields = {},
             //set[AType] constructors = {},
             list[KeywordFormal] commonKeywordFields = []
             );

// Maintain excluded use in parts of a scope
private str key_exclude_use = "exclude_use";

void storeExcludeUse(Tree cond, Tree excludedPart, Collector c){
    c.push(key_exclude_use, <getLoc(cond), getLoc(excludedPart)>);
}

// Maintain allow before use: where variables may be used left (before) their definition
private str key_allow_use_before_def = "allow_use_before_def";

void storeAllowUseBeforeDef(Tree container, Tree allowedPart, Collector c){
    c.push(key_allow_use_before_def, <getLoc(container), getLoc(allowedPart)>);
}

// Define the name overloading that is allowed
bool rascalMayOverload(set[loc] defs, map[loc, Define] defines){
    bool seenVAR = false;
    bool seenNT  = false;
    bool seenLEX = false;
    bool seenLAY = false;
    bool seenKEY = false;
    
    for(def <- defs){
        // Forbid:
        // - overloading of variables
        // - overloading of incompatible syntax definitions
        switch(defines[def].idRole){
        case variableId(): 
            { if(seenVAR) return false;  seenVAR = true;}
        case nonterminalId():
            { if(seenLEX || seenLAY || seenKEY){  return false; } seenNT = true; }
        case lexicalId():
            { if(seenNT || seenLAY || seenKEY) {  return false; }  seenLEX= true; }
        case layoutId():
            { if(seenNT || seenLEX || seenKEY) {  return false; }  seenLAY = true; }
        case keywordId():
            { if(seenNT || seenLAY || seenLEX) {  return false; }  seenKEY = true; }
        }
    }
    return true;
}

// Name resolution filters
@memo
Accept rascalIsAcceptableSimple(TModel tm, loc def, Use use){
    //println("rascalIsAcceptableSimple: <use.id> def=<def>, use=<use>");
 
    if(variableId() in use.idRoles){
       // enforce definition before use
       if(def.path == use.occ.path && /*def.path == use.scope.path &&*/ def < use.scope){
          if(use.occ.offset < def.offset){
             // allow when inside explicitly use before def parts
             if(lrel[loc,loc] allowedParts := tm.store[key_allow_use_before_def] ? []){
                 list[loc] parts = allowedParts[use.scope];
                 if(!isEmpty(parts)){
                    if(any(part <- parts, use.occ < part)){
                       return acceptBinding();
                    }
                  } else {
                   //println("rascalIsAcceptableSimple =\> <ignoreContinue()>");
                   return ignoreContinue();
                 }
             } else {
                throw "Inconsistent value stored for <key_allow_use_before_def>: <tm.store[key_allow_use_before_def]>";
             }
          }
          // restrict when in excluded parts of a scope
          if(lrel[loc,loc] excludedParts := tm.store[key_exclude_use] ? []){
              list[loc] parts = excludedParts[use.scope];
              //println("parts = <parts>, <any(part <- parts, use.occ < part)>");
              if(!isEmpty(parts)){
                 if(any(part <- parts, use.occ < part)){
                    //println("rascalIsAcceptableSimple =\> <ignoreContinue()>");
                    return ignoreContinue();
                 }
              } 
          } else {
             throw "Inconsistent value stored for <key_allow_use_before_def>: <tm.store[key_allow_use_before_def]>";
          }
       }
    }
    //println("rascalIsAcceptableSimple =\> < acceptBinding()>");
    return  acceptBinding();
}

Accept rascalIsAcceptableQualified(TModel tm, loc def, Use use){
   // println("rascalIsAcceptableQualified: <def>, <use>");
    if(defType(AType atype) := tm.definitions[def].defInfo){
       
        defPath = def.path;
        qualAsPath = replaceAll(use.ids[0], "::", "/") + ".rsc";
        
        // qualifier and proposed definition are the same?
        if(endsWith(defPath, qualAsPath)){
           return acceptBinding();
        }
        
         // Qualifier is a ADT name?
        //if(acons(ret:aadt(adtName, list[AType] parameters, _), str consName, list[AType/*NamedField*/] fields, list[Keyword] kwFields) := atype, use.ids[0] == adtName){
        //    return acceptBinding();
        //} 
        
        if(acons(ret:aadt(adtName, list[AType] parameters, _), /*str consName,*/ list[AType/*NamedField*/] fields, list[Keyword] kwFields) := atype){
           return  use.ids[0] == adtName ? acceptBinding() : ignoreContinue();
        } 
        
        // Is there another acceptable qualifier via an extend?
        
        extendedStarBy = {<to.path, from.path> | <loc from, extendPath(), loc to> <- tm.paths}*;
 
        if(!isEmpty(extendedStarBy) && any(p <- extendedStarBy[defPath]?{}, endsWith(p, defPath))){
           return acceptBinding();
        }
       
        return ignoreContinue();
    }
    return acceptBinding();
}

Accept rascalIsAcceptablePath(TModel tm, loc defScope, loc def, Use use, PathRole pathRole) {
    //println("rascalIsAcceptablePath <use.id>, candidate <def>, <pathRole>, <use>");
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
    //println("rascalIsAcceptablePath =\> <res>");
    return res;
}

alias Bindings = map[str varName, AType varType];

AType rascalInstantiateTypeParameters(Tree selector,
                                      def:aadt(str adtName1, list[AType] formals, SyntaxRole syntaxRole1),
                                      ins:aadt(str adtName2, list[AType] actuals, SyntaxRole syntaxRole2),
                                      AType act,
                                      Solver s){ 
    if(adtName1 != adtName2) throw TypePalUsage("rascalInstantiateTypeParameters: <adtName1> versus <adtName2>");
    if(size(formals) != size(actuals)) s.report(error(selector, "Expected %v type parameters for %q, found %v", size(formals), adtName1, size(actuals)));
    bindings = (formals[i].pname : actuals [i] | int i <- index(formals));
    return xxInstantiateRascalTypeParameters(act, bindings, s);
    //return visit(act) { case aparameter(str pname, AType bound):
    //                        if(asubtype(bindings[pname], bound)) insert bindings[pname]; else s.report(error(selector, "Type parameter %q should be less than %t, found %t", pname, bound, bindings[pname]));
    //                  };
}

default AType rascalInstantiateTypeParameters(Tree selector, AType formalType, AType actualType, AType toBeInstantiated, Solver s)
    = toBeInstantiated;
    
    
AType xxInstantiateRascalTypeParameters(AType t, Bindings bindings, Solver s){
    if(isEmpty(bindings))
        return t;
    else
        return visit(t) { case aparameter(str pname, AType bound):
                                if(asubtype(bindings[pname], bound))
                                    insert bindings[pname]; 
                                else 
                                    s.report(error(selector, "Type parameter %q should be less than %t, found %t", pname, bound, bindings[pname]));
                        };
}

default AType rascalInstantiateTypeParameters(Tree selector, AType def, AType ins, AType act, Solver s) = act;

tuple[bool isNamedType, str typeName, set[IdRole] idRoles] rascalGetTypeNameAndRole(aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)){
    return <true, adtName, {dataId(), nonterminalId(), lexicalId(), layoutId(), keywordId()}>;
}

default tuple[bool isNamedType, str typeName, set[IdRole] idRoles] rascalGetTypeNameAndRole(AType t){
    return <false, "", {}>;
}

AType rascalGetTypeInNamelessType(AType containerType, Tree selector, loc scope, Solver s){
    return computeFieldType(containerType, selector, scope, s);
}

data TypePalConfig(
    bool classicReifier = false
);

TypePalConfig rascalTypePalConfig(bool classicReifier = false)
    = tconfig(
        getMinAType                   = AType(){ return avoid(); },
        getMaxAType                   = AType(){ return avalue(); },
        isSubType                     = lang::rascalcore::check::AType::asubtype,
        getLub                        = lang::rascalcore::check::AType::alub,
        
        lookup                        = lookupWide,
       
        isAcceptableSimple            = rascalIsAcceptableSimple,
        isAcceptableQualified         = rascalIsAcceptableQualified,
        isAcceptablePath              = rascalIsAcceptablePath,
        
        mayOverload                   = rascalMayOverload,       
        classicReifier                = classicReifier,
      
        getTypeNameAndRole            = rascalGetTypeNameAndRole,
        getTypeInNamelessType         = rascalGetTypeInNamelessType,
        instantiateTypeParameters     = rascalInstantiateTypeParameters
    );
