@bootstrapParser
module lang::rascalcore::check::ComputeType

/*
    Compute the type of expressions and various other constructs
*/

extend lang::rascalcore::check::ATypeInstantiation;
extend lang::rascalcore::check::BuiltinFields;
extend lang::rascalcore::check::ScopeInfo;

import lang::rascalcore::check::ATypeUtils;
import lang::rascal::\syntax::Rascal;

//import IO;
import Map;
import Set;
import List;
import String;
import Node;

void checkConditions(list[Expression] condList, Solver s){
    for(Expression cond <- condList){
        tcond = s.getType(cond);
        if(!s.isFullyInstantiated(tcond)){
            s.requireUnify(abool(), tcond, error(cond, "Cannot unify %t with `bool`", cond));
            tcond = s.instantiate(tcond); 
        } 
        s.requireSubType(tcond, abool(), error(cond, "Condition should be `bool`, found %t", cond));
    }
}

void requireFullyInstantiated(Solver s, AType ts...){
  for(t <- ts){
    if(!s.isFullyInstantiated(t)) throw TypeUnavailable();
  }
}

void checkNonVoid(Tree e, Collector c, str msg){
    if(isVoidAType(c.getType(e))) c.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, AType t, Collector c, str msg){
    if(isVoidAType(t)) c.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, Solver s, str msg){
    if(isVoidAType(s.getType(e))) s.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, AType t, Solver s, str msg){
    if(isVoidAType(t)) s.report(error(e, msg + " should not have type `void`"));
}

AType(Solver) makeGetSyntaxType(Type varType)
    = AType(Solver s) { Tree t = varType; return getSyntaxType(t, s); };
    
void(Solver) makeVarInitRequirement(Variable var)
    = void(Solver s){
            Bindings bindings = ();
            initialType = s.getType(var.initial);
            varType = s.getType(var.name);
            checkNonVoid(var, varType, s, "Variable declaration");
            isuffix = "i";
            vsuffix = "v";
            initialTypeU = makeUniqueTypeParams(initialType, isuffix);
            varTypeU = makeUniqueTypeParams(varType, vsuffix);
            try   bindings = unifyRascalTypeParams(initialTypeU, varTypeU, bindings);
            catch invalidMatch(str reason):
                  s.report(error(var.initial, reason));
            
            initialTypeU = instantiateRascalTypeParameters(var, initialTypeU, bindings, s);  
            if(s.isFullyInstantiated(initialTypeU)){
                s.requireSubType(initialTypeU, varTypeU, error(var, "Initialization of %q should be subtype of %t, found %t", "<var.name>", var.name, deUnique(initialTypeU)));
            } else if(!s.unify(initialType, varType)){
                s.requireSubType(initialTypeU, varTypeU, error(var, "Initialization of %q should be subtype of %t, found %t", "<var.name>", var.name, deUnique(initialTypeU)));
            }
            checkNonVoid(var.initial, initialTypeU, s, "Variable initialization");
            s.fact(var, deUnique(varType));
       };
       
void(Solver) makeNonVoidRequirement(Tree t, str msg)
    = void(Solver s) { checkNonVoid(t, s, msg ); };

AType unaryOp(str op, AType(Tree, AType, Solver) computeType, Tree current, AType t1, Solver s, bool maybeVoid=false){

    requireFullyInstantiated(s, t1);
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        bin_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                bin_overloads += <key, idr, unaryOp(op, computeType, current, tp, s)>;
             } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
//>>           catch e:  /* continue with next overload */;
        }
        if(isEmpty(bin_overloads)) s.report(error(current, "%q cannot be applied to %t", op, t1));
        return overloadedAType(bin_overloads);
    }
    if(!maybeVoid && isVoidAType(t1)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
    return computeType(current, t1, s);
}

AType binaryOp(str op, AType(Tree, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, Solver s){

    requireFullyInstantiated(s, t1, t2);
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        bin_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                bin_overloads += <key, idr, binaryOp(op, computeType, current, tp, t2, s)>;
            } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
//>>          catch e: /* continue with next overload */;
        }
        if(isEmpty(bin_overloads)) s.report(error(current, "%q cannot be applied to %t and %t", op, t1, t2));
        return overloadedAType(bin_overloads);
    }
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t2){
        bin_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                bin_overloads += < key, idr, binaryOp(op, computeType, current, t1, tp, s)>;
             } catch checkFailed(list[FailMessage] _):/* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
//>>           catch e: /* continue with next overload */;
        }
        if(isEmpty(bin_overloads)) s.report(error(current, "%q cannot be applied to %t and %t", op, t1, t2));
        return overloadedAType(bin_overloads);
    }
    if(isVoidAType(t1) || isVoidAType(t2)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
    return computeType(current, t1, t2, s);
}

AType ternaryOp(str op, AType(Tree, AType, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, AType t3, Solver s){

    requireFullyInstantiated(s, t1, t2, t3);
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        tern_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                tern_overloads += <key, idr, ternaryOp(op, computeType, current, tp, t2, t3, s)>;
             } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
               catch _: /* continue with next overload */;
        }
        if(isEmpty(tern_overloads)) s.report(error(current, "%q cannot be applied to %t, %t, and %t", op, t1, t2, t3));
        return overloadedAType(tern_overloads);
    }
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t2){
        tern_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                tern_overloads += < key, idr, ternaryOp(op, computeType, current, t1, tp, t3, s)>;
             } catch checkFailed(list[FailMessage] _):/* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
 //>>          catch e:/* continue with next overload */;
        }
        if(isEmpty(tern_overloads)) s.report(error(current, "%q cannot be applied to %t, %t, and %t", op, t1, t2, t3));
        return overloadedAType(tern_overloads);
    }
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t3){
        tern_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                tern_overloads += < key, idr, ternaryOp(op, computeType, current, t1, t2, tp, s)>;
             } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
 //>>          catch e: /* continue with next overload */;
        }
        if(isEmpty(tern_overloads)) s.report(error(current, "%q cannot be applied to %t, %t, and %t", op, t1, t2, t3));
        return overloadedAType(tern_overloads);
    }
    if(isVoidAType(t1) || isVoidAType(t2) || isVoidAType(t3)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
    return computeType(current, t1, t2, t3, s);
}

AType computeADTType(Tree current, str adtName, loc scope, AType retType, list[AType] formals, list[Keyword] kwFormals, actuals, keywordArguments, list[bool] identicalFormals, Solver s){                     
    //println("---- <current>, identicalFormals: <identicalFormals>");
    requireFullyInstantiated(s, retType);
    nactuals = size(actuals); nformals = size(formals);
    if(nactuals != nformals){
        s.report(error(current, "Expected %v argument(s), found %v", nformals, nactuals));
    }
    formalTypes = formals; // [ expandUserTypes(formals[i], scope, s) | i <- index(formals) ];
    index_formals = index(formals);
    //println("formals: <formals>");
    list[AType] actualTypes = [];
    list[bool] dontCare = [];
    bool isExpression = true;
    switch(actuals){
        case list[Expression] expList: {
                dontCare = [ false | _ <- index(expList) ];
                actualTypes = [ s.getType(expList[i]) | i <- index(expList) ];
                //print("expList: [ "); for(i <- index(expList)) print("<expList[i]> "); println(" ]");
            }
        case list[Pattern] patList: {
                dontCare = [ isWildCard("<patList[i]>") | i <- index(patList) ];
                actualTypes = [ dontCare[i] ? formals[i] : getPatternType(patList[i], formals[i], scope, s) | i <- index(patList) ];
                //print("patList: [ "); for(i <- index(patList)) print("<patList[i]> "); println(" ]");
                isExpression = false;
            }
        default:
            throw rascalCheckerInternalError(getLoc(current), "Illegal argument `actuals`");
    }
    
    for(int i <- index_formals){
        if(overloadedAType(rel[loc, IdRole, AType] overloads) := actualTypes[i]){   // TODO only handles a single overloaded actual
            //println("computeADTType: <current>");
            //iprintln(overloads);
            returnTypeForOverloadedActuals = {};
            for(<key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeADTReturnType(current, adtName, scope, formalTypes, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
                  catch NoBinding(): /* continue with next overload */;
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { s.report(error(current, "Constructor for %q with arguments %v cannot be resolved", adtName, actuals));}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    
    return computeADTReturnType(current, adtName, scope, formalTypes, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s);
}

AType computeADTReturnType(Tree current, str adtName, loc scope, list[AType] formalTypes, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, list[bool] dontCare, bool isExpression, Solver s){
    Bindings bindings = ();
    fsuffix = "f";
    asuffix = "a";
    index_formals = index(formalTypes);
    formalTypesU = makeUniqueTypeParams(formalTypes, fsuffix);
    actualTypesU = makeUniqueTypeParams(actualTypes, asuffix);
    for(int i <- index_formals, !dontCare[i]){
        try   bindings = matchRascalTypeParams(formalTypesU[i], actualTypesU[i], bindings);
        catch invalidMatch(str reason): 
              s.report(error(current, reason));   
    }
    iformalsU = formalTypesU;
    if(!isEmpty(bindings)){
        try   iformalsU = [instantiateRascalTypeParameters(current, formalTypesU[i], bindings, s) | int i <- index_formals]; // changed
        catch invalidInstantiation(str msg):
              s.report(error(current, msg));
    }
    for(int i <- index_formals, !dontCare[i]){
        aiU = actualTypesU[i];
        if(!s.isFullyInstantiated(aiU) && isExpression){
            if(identicalFormals[i]){
               s.requireUnify(aiU, iformalsU[i], error(current, "Cannot unify %t with %t", aiU, iformalsU[i]));
               aiU = s.instantiate(aiU);
            } else
                continue;
        }
        s.requireComparable(aiU, iformalsU[i], error(current, "Argument %v should have type %t, found %t", i, formalTypesU[i], aiU));
    }
    adtType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
    
    switch(keywordArguments){
    case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`:
        checkExpressionKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsExp, bindings, s);
    case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] _>`:
        checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArguments, bindings, scope, s);
    case []: ;
    default:
        throw rascalCheckerInternalError("computeADTReturnType: illegal keywordArguments: <keywordArguments>");
    }
    
    list[AType] parameters = [];
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := adtType){
       params = {};
       for(<_, _, tp> <- overloads){  
        if(isADTAType(tp)) params += toSet(getADTTypeParameters(tp));
       }
       parameters = toList(params);
    } else {
        parameters = getADTTypeParameters(adtType);
    }
    for(p <- parameters){
        if(!bindings[p.pname]?){
            bindings[p.pname] = avalue(); // was: avoid()
        }
    }
    if(!isEmpty(bindings)){
        try {
            ctype_old = makeUniqueTypeParams(s.getType(current), fsuffix);
            ctype_new = instantiateRascalTypeParameters(current, ctype_old, bindings, s); // changed
            if(ctype_new != ctype_old){
                s.specializedFact(current, ctype_new);
            }
        } catch TypeUnavailable(): /* ignore */ ;
          catch invalidInstantiation(str _msg): /* nothing to instantiate */ ;
        try {
            res = deUnique(instantiateRascalTypeParameters(current, makeUniqueTypeParams(s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles), fsuffix), bindings, s));
            res = visit(res){ case ap:aparameter(_,_) => unset(ap, "closed") };
            return res;
        } catch invalidInstantiation(str msg):
               s.report(error(current, msg));
    }
    try {
        return deUnique(instantiateRascalTypeParameters(current, makeUniqueTypeParams(adtType, fsuffix), bindings, s));
    } catch invalidInstantiation(str msg):
        s.report(error(current, msg));
    return adtType;
}

void checkExpressionKwArgs(list[Keyword] kwFormals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Bindings bindings, Solver s){
    if(keywordArgumentsExp is none) return;
 
    msgs = [];
    next_arg:
    for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
        kwName = prettyPrintName(kwa.name);
        
        for(Keyword k <- kwFormals){
           ft = k.fieldType;
           fn = ft.alabel;
           if(kwName == fn){
              ift = makeUniqueTypeParams(ft, "f");
              if(!isEmpty(bindings)){
                  try   ift = instantiateRascalTypeParameters(kwa, ift, bindings, s); // changed
                  catch invalidInstantiation(str msg):
                        s.report(error(kwa, msg));
              }
              kwType = s.getType(kwa.expression);
              s.requireComparable(kwType, deUnique(ift), error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, deUnique(ift)));
              continue next_arg;
           } 
        }
        availableKws = intercalateOr(["`<prettyAType(kw.fieldType)> <kw.fieldName>`" | Keyword kw <- kwFormals]);
        switch(size(kwFormals)){
        case 0: availableKws ="; no other keyword parameters available";
        case 1: availableKws = "; available keyword parameter: <availableKws>";
        default:
            availableKws = "; available keyword parameters: <availableKws>";
        }
        
       msgs += error(kwa, "Undefined keyword argument %q%v", kwName, availableKws);
    }
    s.reports(msgs);
} 

void checkPatternKwArgs(list[Keyword] kwFormals, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Bindings bindings, loc scope, Solver s){
    if(keywordArgumentsPat is none) return;
    
    msgs = [];
    next_arg:
    for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
        kwName = prettyPrintName(kwa.name);
        
        for(Keyword k <- kwFormals){
           ft = k.fieldType;
           fn = ft.alabel;
           if(kwName == fn){
              ift = ft;
              if(!isEmpty(bindings)){
                  try   ift = instantiateRascalTypeParameters(kwa, ft, bindings, s); // changed
                  catch invalidInstantiation(str msg):
                        s.report(error(kwa, msg));
              }
              kwType = getPatternType(kwa.expression, ift, scope, s);
            s.requireComparable(kwType, ift, error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, ift));
              continue next_arg;
           } 
        }
        availableKws = intercalateOr(["`<prettyAType(kw.fieldType)> <kw.fieldName>`" | Keyword kw <- kwFormals]);
        switch(size(kwFormals)){
        case 0: availableKws ="; no other keyword parameters available";
        case 1: availableKws = "; available keyword parameter: <availableKws>";
        default:
            availableKws = "; available keyword parameters: <availableKws>";
        }
        
        msgs += error(kwa, "Undefined keyword argument %q%v", kwName, availableKws);
    }
    s.reports(msgs);
}

list[Keyword] computeKwFormals(list[KeywordFormal] kwFormals, Solver s){
    if(str currentModuleName := s.top(key_current_module)){
        return [kwField(s.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], prettyPrintName(kwf.name), currentModuleName, kwf.expression) | kwf <- kwFormals];
    } else {
        throw "computeKwFormals: key_current_module not found";
    }
}

list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters, _), loc scope, Solver s) {
    if(str currentModuleName := s.top(key_current_module)){
        return [ kwField(s.getType(kwf.\type)[alabel=prettyPrintName(kwf.name)], prettyPrintName(kwf.name), currentModuleName, kwf.expression) | d <- s.getDefinitions(adtName, scope, dataOrSyntaxRoles), kwf <- d.defInfo.commonKeywordFields ];
    } else {
        throw "getCommonKeywords: key_current_module not found";
    }

}
     
list[Keyword] getCommonKeywords(overloadedAType(rel[loc, IdRole, AType] overloads), loc scope, Solver s) = [ *getCommonKeywords(adt, scope, s) | <_, _, adt> <- overloads ];
default list[Keyword] getCommonKeywords(AType atype, loc scope, Solver s) = [];
    
public AType computeFieldTypeWithADT(AType containerType, Tree field, loc scope, Solver s) {
    //println("computeFieldTypeWithADT: <containerType>, <field>");
    containerType = unwrapAType(containerType);
    requireFullyInstantiated(s, containerType);
    fieldName = unescape("<field>");
    if(isNonTerminalAType(containerType) && fieldName == "top"){
        return isStartNonTerminalType(containerType) ? getStartNonTerminalType(containerType) : containerType;
    }
    return s.getTypeInType(containerType, field, {fieldId(), keywordFieldId(), annoId()}, scope); // DURING TRANSITION: allow annoIds
}
    
@doc{Compute the type of field fn on type containerType. A checkFailed is thrown if the field is not defined on the given type.}
public AType computeFieldType(AType containerType, Tree field, loc scope, Solver s) {
   //println("computeFieldType: <containerType>, <field>");
    containerType = unwrapAType(containerType);
    requireFullyInstantiated(s, containerType);
    if(!s.isFullyInstantiated(containerType)) throw TypeUnavailable();
    fieldName = unescape("<field>");
    
   if (isReifiedAType(containerType)) {
        if (fieldName == "symbol") {
            return s.getTypeInScopeFromName("Symbol", scope, {dataId()});
        } else if (fieldName == "definitions") {
           s.getTypeInScopeFromName("Symbol", scope, {dataId()});
                  
           s.getTypeInScopeFromName("Production", scope, {dataId()});
           return makeMapType(makeADTType("Symbol"), makeADTType("Production"));
                
        } else {
           s.report(error(field, "Field %q does not exist on type `type` (classic reifier)", fieldName));
        }
    } else if(isSyntaxType(containerType)){
       
        if(isStartNonTerminalType(containerType)){
           return computeFieldTypeWithADT(getStartNonTerminalType(containerType), field, scope, s);
        } else if(isIterType(containerType)){
            if(containerType.alabel == fieldName){
                return makeListType(getIterElementType(containerType));
            }
        } else if(isSeqType(containerType)){
            for(tp <- getSeqTypes(containerType)){
                if(tp.alabel == fieldName){
                    return tp;
                }
            }
        } else if(isAltType(containerType)){
            for(tp <- getAltTypes(containerType)){
                if(tp.alabel == fieldName){
                    return tp;
                }
            }
        } else if(isNonTerminalAType(containerType)){
           return computeFieldTypeWithADT(containerType, field, scope, s);
        }
        return computeFieldTypeWithADT(treeType, field, scope, s);
     } else if(asubtype(containerType, treeType)){
            return computeFieldTypeWithADT(treeType, field, scope, s);
    } else if (isTupleAType(containerType)) {
        if(tupleHasFieldNames(containerType)){
            idx = indexOf(getTupleFieldNames(containerType), fieldName);
            if(idx >= 0)
                return getTupleFieldTypes(containerType)[idx];
            else
                s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        } else {
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        }
    } else if (isLocAType(containerType)) {
        if (fieldName in getBuiltinFieldMap()[aloc()])
            return getBuiltinFieldMap()[aloc()][fieldName];
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isDateTimeAType(containerType)) {
        if (fieldName in getBuiltinFieldMap()[adatetime()])
            return getBuiltinFieldMap()[adatetime()][fieldName];
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isRelAType(containerType)) {
        idx = indexOf(getRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeSetType(getRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isListRelAType(containerType)) {
        idx = indexOf(getListRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeListType(getListRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isMapAType(containerType)) {
           idx = indexOf(getMapFieldNames(containerType), fieldName);
        if(idx >= 0){
            return idx == 0 ? makeSetType(getMapDomainType(containerType)) : makeSetType(getMapRangeType(containerType));
        }
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    
    } else if (isNodeAType(containerType)) {
        return avalue();
    } 
    s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    return avalue();
}

AType computeSubscriptionType(Tree current, AType t1, list[AType] tl, list[Expression] indexList, Solver s){
    if(!s.isFullyInstantiated(t1)) throw TypeUnavailable();
    for(tp <- tl){
        if(!s.isFullyInstantiated(tp)) throw TypeUnavailable();
    }

    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        //println("computeSubscriptionType: <current>, <t1>");
        subscript_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                subscript_overloads += <key, role, computeSubscriptionType(current, tp, tl, indexList, s)>;
            } catch checkFailed(list[FailMessage] _):/* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>>        catch e: /* continue with next overload */;
        }
        //println("computeSubscriptionType: <current>, <t1>, <tl> ==\> <overloadedAType(subscript_overloads)>");
        if(isEmpty(subscript_overloads)) s.report(error(current, "Expressions of type %t cannot be subscripted", t1));
        return overloadedAType(subscript_overloads);
    } else if (isListAType(t1) && (!isListRelAType(t1) || (isListRelAType(t1) && size(tl) == 1 && isIntAType(tl[0])))) {
        // TODO: At some point we should have separate notation for this, but this final condition treats list
        // relations indexed by one int value as lists, making this an index versus a projection
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a list expression, found %v", size(tl)));
        else if (!isIntAType(tl[0]))
            s.report(error(current, "Expected subscript of type int, found %t", tl[0]));
        else
            return getListElementType(t1);
            //return makeListType(getListElementType(t1));
    } else if (isRelAType(t1)) {
        if (size(tl) >= size(getRelFields(t1)))
            s.report(error(current, "For a relation with arity %v you can have at most %v subscripts", size(getRelFields(t1)), size(getRelFields(t1))-1));
        else {
            relFields = getRelFields(t1);
            failures = [ error(indexList[idx], "At subscript %v, subscript type %t must be comparable to relation field type %t",  idx+1, tl[idx], relFields[idx])
                       | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) 
                       ];
            if (size(failures) > 0) {
                s.reports(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                //if (alabel(_,rft) := rftype) rftype = rft; 
                return makeSetType(rftype);
            } else {
                return arel(atypeList(tail(relFields,size(relFields)-size(tl))));
            }
        }
    } else if (isListRelAType(t1)) {
        if (size(tl) >= size(getListRelFields(t1)))
            s.report(error(current, "For a list relation with arity %v you can have at most %v subscripts", size(getListRelFields(t1)), size(getListRelFields(t1))-1));
        else {
            relFields = getListRelFields(t1);
            failures = [ error(indexList[idx], "At subscript %v, subscript type %t must be comparable to relation field type %t", idx+1, tl[idx], relFields[idx]) 
                       | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],makeSetType(relFields[idx]))) 
                       ];
            if (size(failures) > 0) {
                s.reports(failures);
            } else if ((size(relFields) - size(tl)) == 1) {
                rftype = last(relFields);
                //if (alabel(_,rft) := rftype) rftype = rft; 
                return makeListType(rftype);
            } else {
                return alrel(atypeList(tail(relFields,size(relFields)-size(tl))));
            }
        }
    } else if (isMapAType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a map expression, found %v", size(tl)));
        else if (!comparable(tl[0],getMapDomainType(t1)))
            s.report(error(current, "Expected subscript of type %t, found %t", getMapDomainType(t1),tl[0]));
        else
            return getMapRangeType(t1);
    } else if (isNodeAType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a node expression, found %v", size(tl)));
        else if (!isIntAType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else
            return avalue();
    } else if (isTupleAType(t1)) {
        if (size(tl) != 1) {
            s.report(error(current, "Expected only 1 subscript for a tuple expression, found %v", size(tl)));
        } else if (!isIntAType(tl[0])) {
            s.report(error(current, "Expected subscript of type `int`, found %v", tl[0]));
        } else if ((Expression)`<DecimalIntegerLiteral dil>` := head(indexList)) {
            tupleIndex = toInt("<dil>");
            if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(t1))) {
               s.report(error(current, "Tuple index must be between 0 and %v", size(getTupleFields(t1))-1));
            } else {
                return getTupleFields(t1)[tupleIndex];
            }
        } else {
            return lubList(getTupleFields(t1));
        }
    } else if (isStrAType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a string expression, not %v", size(tl)));
        else if (!isIntAType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else
            return astr();
    } else if (isSyntaxType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a nonterminal subscript expression, not %v", size(tl)));
        else if (!isIntAType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else if (isIterType(t1))
            return getIterElementType(t1);
        else
            return makeADTType("Tree");    
    } else {
        s.report(error(current, "Expressions of type %t cannot be subscripted", t1));
    }
    return avalue();
}

AType computeSliceType(Tree current, AType base, AType first, AType step, AType last, Solver s){
    requireFullyInstantiated(s, base, first, step, last);
    
     if(overloadedAType(rel[loc, IdRole, AType] overloads) := base){
        slice_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                slice_overloads += <key, role, computeSliceType(current, tp, first, step, last, s)>;
            } catch checkFailed(list[FailMessage] _): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>         catch e: /* continue with next overload */; 
        }
        if(isEmpty(slice_overloads)) s.report(error(current, "Slice cannot be computed for %t", base));
        return overloadedAType(slice_overloads);
    }
    
    failures = [];
    if(!isIntAType(first)) failures += error(current, "The first slice index must be of type `int`, found %t", first);
    if(!isIntAType(step)) failures  += error(current, "The slice step must be of type `int`, found %t", step);
    if(!isIntAType(last)) failures  += error(current, "The last slice index must be of type `int`, found %t", last);
    
    if(!isEmpty(failures)) throw s.reports(failures);
    
    if (isListAType(base) || isStrAType(base) || isIterType(base)) {
        return base;
    } else if (isNodeAType(base)) {
        return makeListType(avalue());
    }
    
    s.reports(failures + error(current, "Slices can only be used on (concrete) lists, strings, and nodes, found %t", base));
    return avalue();
}

bool isSameTypeParameter(aparameter(str n1, AType b1), aparameter(str n2, AType b2))
    = n1 == n2 && b1 == b2;
    
default bool isSameTypeParameter(AType t1, AType t2) = false;

@synopsis{Coerce acts like alub but for arithmetic opererand pairs that feature coercions}
@description{
The coercions in this dispatch table make it so that:
* int escalates to rat, in presence of a rat
* rat escalates to real, in the presence of a real
* int also escalates to real, in the presence of a real

Furthermore all arithmetic operators `op` in {`*`, `+`, `-`, `/`} are "type preserving":
* int op int   => int
* rat op rat   => rat
* real op real => real
* &T op &T     => &T, given that &T <: num
* And so unequal type parameters, default to `num`: &T <: num op &Y <: num => num
    
Finally, the escalation mechanism through coercions also works in the bounds of type parameters:
* &T <: rat op &U <: int => rat
That is by forwarding the coercion to the bounds of type parameters if the type names are unequal.
}
AType coerce(aint(), arat())     = arat();  // int becomes rat
AType coerce(arat(), aint())     = arat();  // int becomes rat
AType coerce(aint(), areal())    = areal(); // int becomes real
AType coerce(areal(), aint())    = areal(); // int becomes real
AType coerce(arat(), areal())    = areal(); // rat becomes real
AType coerce(areal(), arat())    = areal(); // rat becomes real
AType coerce(anum(), AType r)    = anum();  // everything becomes anum
AType coerce(AType l, anum())    = anum();  // everything becomes anum
default AType coerce(AType t, t) = t;       // type preservation = closed algebras for int, rat and real

// Type preservation also holds if we don't know which type it is. This rule makes
// sure the parameter is propagated over the operator application expression.
// Since the operator always implements coercion at run-time, the bounds do not 
// immediately shrink to the GLB, but rather to the coerced bounds. 
AType coerce(aparameter(str name, AType boundL, closed=false), aparameter(name, AType boundR, closed=false)) 
    = aparameter(name, coerce(boundL,boundR)/*, closed=false*/); 

AType coerce(aparameter(str name, AType boundL, closed=true), aparameter(name, AType boundR, closed=true)) 
    = aparameter(name, coerce(boundL, boundR), closed=true); 

// Coercion also applies to type parameter bounds, but we do not get to keep
// open parameters if the names are different.
// must be `default` to avoid overlap with the equal-type-names case.
default AType coerce(aparameter(str _, AType bound, closed=false), AType r) = coerce(bound, r);
default AType coerce(AType l, aparameter(str _, AType bound, closed=false)) = coerce(l, bound);

// Here we have a closed parameter type, we do not know what it is but it is anything below the bound, 
// We can defer to the coercion of the bounds again, because the run-time will guarantee such
// coercion always. The cases for open parameters are the same, but we leave this here
// for the sake of clarity and completeness.
default AType coerce(aparameter(str _, AType bound, closed=true), AType r) = coerce(bound, r);
default AType coerce(AType l, aparameter(str _, AType bound, closed=true)) = coerce(l, bound);

@synopsis{Calculate the arith type for the numeric types, taking account of coercions.}
public AType numericArithTypes(AType l, AType r) {
    return coerce(l, r);
}

AType computeAdditionType(Tree current, AType t1, AType t2, Solver s) 
    = binaryOp("addition", do_computeAdditionType, current, t1, t2, s);

private AType do_computeAdditionType(Tree current, AType t1, AType t2, Solver s) {
    if(isNumericType(t1) && isNumericType(t2)) return numericArithTypes(t1, t2);
    
    if (isStrAType(t1) && isStrAType(t2))
        return isSameTypeParameter(t1, t2) ? t1 : astr();

    // TODO: what is `true + true`? probably should not exist here
    if (isBoolAType(t1) && isBoolAType(t2))
        return isSameTypeParameter(t1, t2) ? t1 : abool();

    // TODO: what is |unknown:///| + |unknown:///|? probably should not exist here
    if (isLocAType(t1) && isLocAType(t2))
        return isSameTypeParameter(t1, t2) ? t1 : aloc();

    // TODO: this does not make sense to me. The returning type is
    // always a `loc`, which is more specific than the &T unless it were `void`.
    // why are we propagating the &T here?    
    if (isLocAType(t1) && isStrAType(t2))
        return isTypeParameter(t1) ? t1 : aloc();
        
    // TODO what is `now() + now()`? probably should not exist here.    
    if(isDateTimeAType(t1) && isDateTimeAType(t2))
        return isSameTypeParameter(t1, t2) ? t1 : adatetime();
        
    // TODO: This re-encodes alub all over again, but maybe subtly different. 
    if (isTupleAType(t1) && isTupleAType(t2)) {
         if (tupleHasFieldNames(t1) && tupleHasFieldNames(t2)) {
            tflds1 = getTupleFields(t1);
            tflds2 = getTupleFields(t2);
            tnms1  = getTupleFieldNames(t1);
            tnms2  = getTupleFieldNames(t2);
            
            if (size(toSet(tnms1 + tnms2)) == size(tflds1+tflds2)) {
                return makeTupleType(tflds1+tflds2);
             } else {
                return makeTupleType(getTupleFieldTypes(t1) + getTupleFieldTypes(t2));
             }
         } else {        
            return makeTupleType(getTupleFieldTypes(t1) + getTupleFieldTypes(t2));
         }
     } 

    // TODO: this is what all the cases should look like, IMHO, the addition always
    // produces the lub of the types.   
    if (isListAType(t1) && isListAType(t2))
        return s.lub(t1,t2);
    if (isSetAType(t1) && isSetAType(t2))
        return s.lub(t1,t2);
    if (isMapAType(t1) && isMapAType(t2))
        return s.lub(t1,t2);
        
    if (isListAType(t1) && !isContainerType(t2))
        return makeListType(s.lub(getListElementType(t1),t2));
    if (isSetAType(t1) && !isContainerType(t2)) // Covers relations too
        return makeSetType(s.lub(getSetElementType(t1),t2));
    if (isBagAType(t1) && !isContainerType(t2))
        return abag(s.lub(getBagElementType(t1),t2));
        
    if (isListAType(t2) && !isContainerType(t1))
        return makeListType(s.lub(t1,getListElementType(t2)));
    if (isSetAType(t2) && !isContainerType(t1)) // Covers relations too
        return makeSetType(s.lub(t1,getSetElementType(t2)));
    if (isBagAType(t2) && !isContainerType(t1))
        return abag(s.lub(t1,getBagElementType(t2)));
        
    if (isListAType(t1))
        return makeListType(s.lub(getListElementType(t1),t2));
    if (isSetAType(t1)) // Covers relations too
        return makeSetType(s.lub(getSetElementType(t1),t2));
    if (isBagAType(t1))
        return abag(s.lub(getBagElementType(t1),t2));
    
    // TODO: Can we also add together constructor types? 
    // JV: constructor functions can be added like normal functions can.
    // That's why the interpreter gives function types to constructor functions and
    // not something special. Functions are constructor functions are completely interchangeable
    // in the run-time, so they should not have different type kinds.

    // TODO: cloc is arbitrary, can we do better?
    cloc = getLoc(current);
    // TODO: this is a re-implementation of `alub` don't know if it's exactly the same:
    if (isFunctionAType(t1)){
        if (isFunctionAType(t2))
        // TODO: in the interpreter we simply use the lub of the two function types. What is the use of the
        // overloaded type if it can never be a result of a type computation?
            return overloadedAType({<cloc, functionId(), t1>, <cloc, functionId(), t2>});
        else if(overloadedAType(rel[loc, IdRole, AType] overloads) := t2){
            return overloadedAType(overloads + <cloc, functionId(), t1>);
        }
    } else if(overloadedAType(rel[loc, IdRole, AType] overloads1)  := t1){
        if(isFunctionAType(t2))
           return overloadedAType(overloads1 + <cloc, functionId(), t2>);
        else if(overloadedAType(rel[loc, IdRole, AType] overloads2) := t2){
            return overloadedAType(overloads1 + overloads2);
        }
    }
    
    s.report(error(current, "Addition not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeSubtractionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("subtraction", do_computeSubtractionType, current, t1, t2, s);

private AType do_computeSubtractionType(Tree current, AType t1, AType t2, Solver s) { 
    if(isNumericType(t1) && isNumericType(t2)){
        return numericArithTypes(t1, t2);
    }
    if(isDateTimeAType(t1) && isDateTimeAType(t2)) {
        // TODO JV: why are we promoting the type parameter here?
        // BTW, the difference between two DateTime's is _NOT_ a datetime but an integer or something that represents a Duration.
        return isSameTypeParameter(t1, t2) ? t1 : adatetime();
    }
    if(isListAType(t1) && isListAType(t2)){
        s.requireComparable(getListElementType(t1), getListElementType(t2), error(current, "%v of type %t could never contain elements of second %v type %t", 
                                                                                    isListRelAType(t1) ? "List Relation" : "List", t1, isListRelAType(t2) ? "List Relation" : "List", t2));
       return t1;
    }
    
    // TODO JV: this is weird, what if it's a list of lists and you want to subtract something? Then the previous case already
    // complains about incomparability...
    if(isListAType(t1)){
        s.requireComparable(getListElementType(t1), t2, error(current, "%v of type %t could never contain elements of type %t", isListRelAType(t1) ? "List Relation" : "List", t1, t2));
        return t1;
    }
    if(isSetAType(t1) && isSetAType(t2)){
        s.requireComparable(getSetElementType(t1), getSetElementType(t2), error(current, "%v of type %t could never contain elements of second %v type %t", isRelAType(t1) ? "Relation" : "Set", t1,isListRelAType(t2) ? "Relation" : "Set", t2));
        return t1;
    }

    // TODO JV: same issue as with list
    if(isSetAType(t1)){
        s.requireComparable(getSetElementType(t1), t2, error(current, "%v of type %t could never contain elements of type %t", isRelAType(t1) ? "Relation" : "Set", t1, t2));
        return t1;
    }

    if(isMapAType(t1)){
        s.requireComparable(t1, t2, error(current, "Map of type %t could never contain a sub-map of type %t", t1, t2));
        return t1;
    }
    
    s.report(error(current, "Subtraction not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeProductType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("product", do_computeProductType, current, t1, t2, s);

private AType do_computeProductType(Tree current, AType t1, AType t2, Solver s){ 
    if(isNumericType(t1) && isNumericType(t2)) return numericArithTypes(t1, t2);
    
    if (isListAType(t1) && isListAType(t2))
        return makeListType(atuple(atypeList([getListElementType(t1),getListElementType(t2)])));
    // TODO: JV is a rel not a set of tuples?
    if (isRelAType(t1) && isRelAType(t2))
        return arel(atypeList([getRelElementType(t1),getRelElementType(t2)]));
    if (isListRelAType(t1) && isListRelAType(t2))
        return alrel(atypeList([getListRelElementType(t1),getListRelElementType(t2)]));
    if (isSetAType(t1) && isSetAType(t2))
        return arel(atypeList([getSetElementType(t1),getSetElementType(t2)]));
    
    s.report(error(current, "Product not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeDivisionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("division", do_computeDivisionType, current, t1, t2, s);

private AType do_computeDivisionType(Tree current, AType t1, AType t2, Solver s){
    if(!(isNumericType(t1) && isNumericType(t2))){
        s.report(error(current, "Division not defined on %t and %t", t1, t2));
    }
     return numericArithTypes(t1, t2);
}

AType computeIntersectionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("intersection", do_computeIntersectionType, current, t1, t2, s);
    
private AType do_computeIntersectionType(Tree current, AType t1, AType t2, Solver s){  
    if ( ( isListRelAType(t1) && isListRelAType(t2) ) || 
         ( isListAType(t1) && isListAType(t2) ) || 
         ( isRelAType(t1) && isRelAType(t2) ) || 
         ( isSetAType(t1) && isSetAType(t2) ) || 
         ( isMapAType(t1) && isMapAType(t2) ) )
    {
        // TODO: JV maybe: warning: intersection of incomparable sets always produces the empty set. It's not strictly a static error.
        // Another way is to use the glb and warn if the result is set[void], list[void] or map[void,void].
        if (!comparable(t1,t2)) 
            s.report(error(current, "Types %t and %t are not comparable", t1, t2));

        // TODO JV: this re-encodes what `glb` also does: if t1 <: t2 then glb(t1,t2) == t2,
        // except it does not take care of the bounds of type parameters.
        // Let's rewrite this using glb.
        if (asubtype(t2, t1))
            return t2;
        // TODO See above: 
        if (asubtype(t1, t2))
            return t1;

        // TODO: This is also a partial re-implementation of glb    
        if (isListRelAType(t1)) return makeListRelType(makeVoidType(),makeVoidType());
        if (isListAType(t1)) return makeListType(makeVoidType());
        if (isRelAType(t1)) return makeRelType(makeVoidType(), makeVoidType());
        if (isSetAType(t1)) return makeSetType(makeVoidType());
        if (isMapAType(t1)) return makeMapType(makeVoidType(),makeVoidType());
    }
    s.report(error(current, "Intersection not defined on %t and %t", t1, t2));
    return avalue();
}

// ---- getPatternType --------------------------------------------------------

AType getPatternType(Pattern p, AType subjectType, loc scope, Solver s){
    requireFullyInstantiated(s, subjectType);
    if(isConstructorAType(subjectType)){
        subjectType = getConstructorResultType(subjectType);
    }
    tp = getPatternType0(p, subjectType, scope, s);
    s.fact(p, tp);
    return tp;
}

// ---- bracketed pattern

//private AType getPatternType0(current: (Pattern) `(<Pattern p>)`, AType subjectType, loc scope, Solver s){
//    return getPatternType(p, subjectType, scope, Solver);
//}

// ---- literal patterns

private default AType getPatternType0(Pattern p, AType subjectType, loc scope, Solver s){
    return s.getType(p);
}

// ---- set pattern

private AType getPatternType0(current: (Pattern) `{ <{Pattern ","}* elements0> }`, AType subjectType, loc scope, Solver s){
    elmType = isSetAType(subjectType) ? getSetElementType(subjectType) : avalue();
    setType = aset(s.lubList([getPatternType(p, elmType, scope,s) | p <- elements0]));
    s.fact(current, setType);
    return setType;
}

// ---- list pattern

private AType getPatternType0(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, AType subjectType, loc scope, Solver s){
    elmType = isListAType(subjectType) ? getListElementType(subjectType) : avalue();
    res = alist(s.lubList([getPatternType(p, elmType, scope, s) | p <- elements0]));
    s.fact(current, res);
    return res;
}

// ---- typed variable pattern

private AType getPatternType0(current:( Pattern) `<Type tp> <Name name>`, AType subjectType, loc scope, Solver s){
    return s.getType(tp)[alabel=unescape("<name>")];
}

// ---- qualifiedName pattern: QualifiedName

private AType getPatternType0(current: (Pattern) `<QualifiedName name>`, AType subjectType, loc scope, Solver s){
    base = prettyPrintBaseName(name);
    if(!isWildCard(base)){
       nameType = s.getType(name);
       nameType = instantiateAndCompare(current, nameType, subjectType, s);
       return nameType[alabel=unescape("<name>")];
    } else
       return subjectType[alabel=unescape("<name>")];
}

// ---- multiVariable pattern: QualifiedName*

private AType getPatternType0(current: (Pattern) `<QualifiedName name>*`,  AType subjectType, loc scope, Solver s){
    Pattern pat = (Pattern) `<QualifiedName name>`;
    return getSplicePatternType(current, pat, subjectType, s);
}

// ---- splice pattern: *Pattern

private AType getPatternType0(current: (Pattern) `* <Pattern argument>`, AType subjectType, loc scope, Solver s){
    return  getSplicePatternType(current, argument, subjectType, s);
}

private AType getSplicePatternType(Pattern current, Pattern argument,  AType subjectType, Solver s){
    if(argument is typedVariable){
       uname = unescape("<argument.name>");
       if(isWildCard(uname)){
          return s.getType(argument.\type);
       } else {
          inameType = s.getType(argument.name);
          s.fact(argument, inameType);
          if(isListAType(inameType)) {
                elmType = getListElementType(inameType);
                s.fact(current, elmType);
                return elmType;
          }
          if(isSetAType(inameType)){
                elmType =  getSetElementType(inameType);
                s.fact(current, elmType);
                return elmType;
          }
          s.report(error(current, "Cannot get element type for %t", inameType)); 
       }
    }
    if(argument is qualifiedName){
         argName = argument.qualifiedName;
         base = prettyPrintBaseName(argName);
         if(isWildCard(base)){
            return subjectType;
         } else {
           elmType = subjectType;
           inameType = s.getType(argName);
           elmType = avoid();
           if(isListAType(inameType)){
                elmType = getListElementType(inameType);
           } else if(isSetAType(inameType)){
                elmType = getSetElementType(inameType);
           } else {
                s.report(error(argument, "List or set type expected, found %t", inameType));
           }
             
           return instantiateAndCompare(current, elmType, subjectType, s);
        }
    } else {
        s.report(error(current, "Unsupported construct in splice pattern"));
        return subjectType;
    }
}

AType instantiateAndCompare(Tree current, AType patType, AType subjectType, Solver s){
    if(!s.isFullyInstantiated(patType) || !s.isFullyInstantiated(subjectType)){
      s.requireUnify(patType, subjectType, error(current, "Type of pattern could not be computed"));
      s.fact(current, patType); // <====
      patType = s.instantiate(patType);
      s.fact(current, patType);
      subjectType = s.instantiate(subjectType);
   }
  
   bindings = ();
   lsuffix = "l";
   rsuffix = "r";
   patTypeU = makeUniqueTypeParams(patType, lsuffix);
   subjectTypeU = makeUniqueTypeParams(subjectType, rsuffix);
   try   bindings = unifyRascalTypeParams(patTypeU, subjectTypeU, bindings);
   catch invalidMatch(str reason):
         s.report(error(current, reason));
   if(!isEmpty(bindings)){
    try   <patTypeU, subjectTypeU> = instantiateRascalTypeParameters(current, patTypeU, subjectTypeU, bindings, s);
    catch invalidInstantiation(str msg):
        s.report(error(current, msg));
   }
   patType = deUnique(patTypeU);
   subjectType = deUnique(subjectTypeU);
   s.requireComparable(patType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, patTypeU));
   return patType;
}

// ---- splicePlus pattern: +Pattern ------------------------------------------

private AType getPatternType0(current: (Pattern) `+<Pattern argument>`, AType subjectType, loc scope, Solver s)
    = getSplicePatternType(current, argument, subjectType, s);

// ---- tuple pattern ---------------------------------------------------------

private AType getPatternType0(current: (Pattern) `\< <{Pattern ","}* elements1> \>`, AType subjectType, loc scope, Solver s){
    pats = [ p | Pattern p <- elements1 ];
    if(isTupleAType(subjectType)){
        elmTypes = getTupleFieldTypes(subjectType);
        if(size(pats) == size(elmTypes)){
           if(tupleHasFieldNames(subjectType)){
             res = atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope, s)[alabel= elmTypes[i].alabel] | int i <- index(pats)]));
             return res;
           } else {
             return atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope, s) | int i <- index(pats)]));
           }
        } else {
            s.report(error(current, "Expected tuple pattern with %v elements, found %v",size(elmTypes), size(pats)));
        }
    }
    return atuple(atypeList([getPatternType(pats[i], avalue(), scope, s) | int i <- index(pats)]));
}

private AType getPatternType0((KeywordArgument[Pattern]) `<Name _> = <Pattern expression>`, AType subjectType, loc scope, Solver s){
    return getPatternType(expression, subjectType, scope, s);
}

// ---- call or tree pattern --------------------------------------------------

private AType getPatternType0(current: (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`, AType subjectType, loc scope, Solver s){
   //println("getPatternType: <current>");
    pats = [ p | Pattern p <- arguments ];
    
    texp = getPatternType(expression, subjectType, scope, s);
    //println("bindings: <bindings>");
    //clearBindings();    // <====
    subjectType = s.instantiate(subjectType);
    
    if(isStartNonTerminalType(subjectType)){
        subjectType = getStartNonTerminalType(subjectType);
    }
    
    if(isStrAType(texp)){
        return computePatternNodeType(current, scope, pats, keywordArguments, s, subjectType);
    }       
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := texp){
       notFullyInstantiatedArgs = false;
       bareArgTypes = for(p <- pats){
                        try {
                            pType = s.getType(p);
                            if(!s.isFullyInstantiated(pType)){
                                notFullyInstantiatedArgs = true;
                               
                            }
                            append pType;
                        } catch TypeUnavailable():
                            append avalue();
                      };
       <filteredOverloads, identicalFields> = filterOverloadedConstructors(overloads, bareArgTypes, subjectType, s);
       if({<_, _, tp>} := filteredOverloads){
          texp = tp;
          s.fact(expression, tp);
       } else {
         if(notFullyInstantiatedArgs){
            s.report(error(expression, "Ambiguous pattern type %t", texp));
         }
         overloads = filteredOverloads;
         validReturnTypeOverloads = {};
         validOverloads = {};
         next_cons:
         for(ovl: <key, idr, tp> <- overloads){
            if(acons(adtType:aadt(adtName, list[AType] _, _), list[AType] fields, list[Keyword] kwFields) := tp){
               try {
                     validReturnTypeOverloads += <key, idr, computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, identicalFields, s)>;
                     validOverloads += ovl;
                    } catch _: checkFailed(list[FailMessage] _): {
                            continue next_cons;
                      }
                      catch NoBinding():
                            continue next_cons;
             }
        }
        if({<_, _, tp>} := validOverloads){
           texp = tp; 
           s.fact(expression, tp);
            // TODO check identicalFields to see whether this can make sense
           // unique overload, fall through to non-overloaded case to potentially bind more type variables
        } else if(isEmpty(validReturnTypeOverloads)) s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
        else return overloadedAType(validReturnTypeOverloads);
      }
    }

    if(acons(adtType:aadt(adtName, list[AType] _, _), list[AType] fields, list[Keyword] kwFields) := texp){
       return computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, [true | int _ <- index(fields)], s);
    }
    s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
    return avalue();
}

private bool acceptable(int i, AType a, AType b, list[bool] uninstantiated)
        = uninstantiated[i] || comparable(a, b);
        
tuple[rel[loc, IdRole, AType], list[bool]] filterOverloadedConstructors(rel[loc, IdRole, AType] overloads, list[AType] argTypes, AType subjectType, Solver s){
    int arity = size(argTypes);
    rel[loc, IdRole, AType] filteredOverloads = {};
    list[bool] identicalFields = [true | int _ <- [0 .. arity]];
    
    uninstantiated = [ !s.isFullyInstantiated(argTypes[i]) | int i <- [0 .. arity] ];
    
    for(ovl:<loc _, IdRole _, AType tp> <- overloads){                     
        if(acons(ret:aadt(_, list[AType] _, _), list[AType] fields, list[Keyword] _) := tp, comparable(ret, subjectType)){
           if(size(fields) == arity && (arity == 0 || all(int i <- index(fields), acceptable(i, fields[i], argTypes[i], uninstantiated)))){
            filteredOverloads += ovl;
           }
        }
    }
    return <filteredOverloads, identicalFields>;
}

AType computePatternNodeType(Tree current, loc scope, list[Pattern] patList, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Solver s, AType subjectType){                     
    dontCare = [ isWildCard("<patList[i]>") | i <- index(patList) ];
    actualType = [ dontCare[i] ? avalue() : getPatternType(patList[i], avalue(), scope, s) | i <- index(patList) ];
    
    if(adtType:aadt(adtName, list[AType] _,_) := subjectType){
       declaredInfo = s.getDefinitions(adtName, scope, dataOrSyntaxRoles);
       declaredType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
       checkPatternKwArgs(getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return subjectType;
    } else if(acons(adtType:aadt(_, list[AType] _, _), list[AType] _, list[Keyword] kwFields) := subjectType){
       kwFormals = kwFields;
       checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return anode([]);
    } else if(anode(list[AType] fields) := subjectType){
        return computePatternNodeTypeWithKwArgs(current, keywordArgumentsPat, fields, scope, s);
    } else if(isValueAType(subjectType)){
        return anode([]);
    }
    s.report(error(current, "Node pattern does not match %t", subjectType));
    return avalue();
}

AType computePatternNodeTypeWithKwArgs(Tree current, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, list[AType] fields, loc scope, Solver s){
    if(keywordArgumentsPat is none) return anode([]);
                       
    nodeFieldTypes = [];
    nextKW:
       for(ft <- fields){
           fn = ft.alabel;
           for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
               kwName = prettyPrintName(kwa.name);
               if(kwName == fn){
                  kwType = getPatternType(kwa.expression,ft, scope, s);
                  s.requireUnify(ft, kwType, error(current, "Cannot determine type of field %q", fn));
                  nodeFieldTypes += ft;
                  continue nextKW;
               }
           }    
       }
       return anode(nodeFieldTypes); 
 }

// ---- variable becomes pattern

private AType getPatternType0(current: (Pattern) `<Name name> : <Pattern pattern>`,  AType subjectType, loc scope, Solver s){
    return getPatternType(pattern, subjectType, scope, s)[alabel=unescape("<name>")];
}

// ---- typed variable becomes

private AType getPatternType0(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    uname = unescape("<name>");
    declaredType = s.getType(tp);
    patType = getPatternType(pattern, subjectType, scope, s);
    s.requireComparable(patType, declaredType, error(current, "Incompatible type in assignment to variable %q, expected %t, found %t", tp, declaredType, patType));
    return declaredType[alabel=unescape("<name>")];
}

// ---- descendant pattern

private AType getPatternType0(current: (Pattern) `/ <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    getPatternType(pattern, avalue(), scope, s);
    return subjectType;
}

// ---- negative 

private AType getPatternType0(current: (Pattern) `- <Pattern pattern>`,  AType subjectType, loc scope, Solver s){
    return getPatternType(pattern, subjectType, scope, s);
}

//TODO: map

private AType getPatternType0(current: (Pattern) `( <{Mapping[Pattern] ","}* mps> )`, AType subjectType, loc scope, Solver s){
    return amap(avoid(),avoid()); // TODO
}

// ---- reifiedType

private AType getPatternType0(current: (Pattern) `type ( <Pattern symbol>, <Pattern definitions> )`, AType subjectType, loc scope, Solver s){
    pats = [symbol, definitions];
    return areified(avalue());
}

// ---- asType

private AType getPatternType0(current: (Pattern) `[ <Type tp> ] <Pattern p>`, AType subjectType, loc scope, Solver s){
    return s.getType(tp);
}

// ---- anti

private AType getPatternType0(current: (Pattern) `! <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    getPatternType(pattern, avalue(), scope, s);  // to force nested type calculations
    return avalue();
}