module lang::rascalcore::check::ComputeType

extend lang::rascalcore::check::ATypeInstantiation;
extend lang::rascalcore::check::BuiltinFields;

import lang::rascal::\syntax::Rascal;

import IO;
import Map;
import Set;
import String;

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
    if(isVoidType(c.getType(e))) c.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, AType t, Collector c, str msg){
    if(isVoidType(t)) c.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, Solver s, str msg){
    if(isVoidType(s.getType(e))) s.report(error(e, msg + " should not have type `void`"));
}

void checkNonVoid(Tree e, AType t, Solver s, str msg){
    if(isVoidType(t)) s.report(error(e, msg + " should not have type `void`"));
}

AType(Solver) makeGetSyntaxType(Tree varType)
    = AType(Solver s) { return getSyntaxType(varType, s); };
    
void(Solver) makeVarInitRequirement(Variable var)
    = void(Solver s){
            Bindings bindings = ();
            initialType = s.getType(var.initial);
            varType = s.getType(var.name);
            checkNonVoid(var, varType, s, "Variable declaration");
            try   bindings = matchRascalTypeParams(initialType, varType, bindings);
            catch invalidMatch(str reason):
                  s.report(error(var.initial, reason));
            
            initialType = xxInstantiateRascalTypeParameters(var, initialType, bindings, s);  
            if(s.isFullyInstantiated(initialType)){
                s.requireSubType(initialType, varType, error(var, "Initialization of %q should be subtype of %t, found %t", "<var.name>", var.name, initialType));
            } else if(!s.unify(initialType, varType)){
                s.requireSubType(initialType, varType, error(var, "Initialization of %q should be subtype of %t, found %t", "<var.name>", var.name, initialType));
            }
            checkNonVoid(var.initial, initialType, s, "Variable initialization");
            s.fact(var, varType);
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
             } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
//>>           catch e:  /* continue with next overload */;
        }
        if(isEmpty(bin_overloads)) s.report(error(current, "%q cannot be applied to %t", op, t1));
        return overloadedAType(bin_overloads);
    }
    if(!maybeVoid && isVoidType(t1)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
    return computeType(current, t1, s);
}

AType binaryOp(str op, AType(Tree, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, Solver s){

    requireFullyInstantiated(s, t1, t2);
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        bin_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                bin_overloads += <key, idr, binaryOp(op, computeType, current, tp, t2, s)>;
            } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
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
             } catch checkFailed(list[FailMessage] fms):/* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
//>>           catch e: /* continue with next overload */;
        }
        if(isEmpty(bin_overloads)) s.report(error(current, "%q cannot be applied to %t and %t", op, t1, t2));
        return overloadedAType(bin_overloads);
    }
    if(isVoidType(t1) || isVoidType(t2)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
    return computeType(current, t1, t2, s);
}

AType ternaryOp(str op, AType(Tree, AType, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, AType t3, Solver s){

    requireFullyInstantiated(s, t1, t2, t3);
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        tern_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                tern_overloads += <key, idr, ternaryOp(op, computeType, current, tp, t2, t3, s)>;
             } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
               catch e: /* continue with next overload */;
        }
        if(isEmpty(tern_overloads)) s.report(error(current, "%q cannot be applied to %t, %t, and %t", op, t1, t2, t3));
        return overloadedAType(tern_overloads);
    }
    
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t2){
        tern_overloads = {};
        for(<key, idr, tp> <- overloads){
            try {
                tern_overloads += < key, idr, ternaryOp(op, computeType, current, t1, tp, t3, s)>;
             } catch checkFailed(list[FailMessage] fms):/* continue with next overload */;
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
             } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
               catch NoBinding(): /* continue with next overload */;
 //>>          catch e: /* continue with next overload */;
        }
        if(isEmpty(tern_overloads)) s.report(error(current, "%q cannot be applied to %t, %t, and %t", op, t1, t2, t3));
        return overloadedAType(tern_overloads);
    }
    if(isVoidType(t1) || isVoidType(t2) || isVoidType(t3)) s.report(error(current, "%q cannot be applied to argument of type `void`", op));
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
                dontCare = [ false | i <- index(expList) ];
                actualTypes = [ s.getType(expList[i]) | i <- index(expList) ];
                //print("expList: [ "); for(i <- index(expList)) print("<expList[i]> "); println(" ]");
            }
        case list[Pattern] patList: {
                dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
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
            for(ovl: <key, idr, tp> <- overloads){   
                try {
                    actualTypes[i] = tp;
                    returnTypeForOverloadedActuals += <key, idr, computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s)>;
                    //println("succeeds: <ovl>");
                } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
                  catch NoBinding(): /* continue with next overload */;
             }
             if(isEmpty(returnTypeForOverloadedActuals)) { s.report(error(current, "Constructor for %q with arguments %v cannot be resolved", adtName, actuals));}
             else return overloadedAType(returnTypeForOverloadedActuals);
        }
    }
    
    return computeADTReturnType(current, adtName, scope, retType, formalTypes, actuals, actualTypes, kwFormals, keywordArguments, identicalFormals, dontCare, isExpression, s);
}

AType computeADTReturnType(Tree current, str adtName, loc scope, AType retType, list[AType] formalTypes, actuals, list[AType] actualTypes, list[Keyword] kwFormals, keywordArguments, list[bool] identicalFormals, list[bool] dontCare, bool isExpression, Solver s){
    Bindings bindings = ();
    index_formals = index(formalTypes);
    for(int i <- index_formals, !dontCare[i]){
        try   bindings = matchRascalTypeParams(formalTypes[i], actualTypes[i], bindings);
        catch invalidMatch(str reason): 
              s.report(error(current, reason));   
    }
    iformals = formalTypes;
    if(!isEmpty(bindings)){
        try   iformals = [instantiateRascalTypeParams(formalTypes[i], bindings) | int i <- index_formals];
        catch invalidInstantiation(str msg):
              s.report(error(current, msg));
    }
    for(int i <- index_formals, !dontCare[i]){
        ai = actualTypes[i];
       //println("<i>: <ai>");
        if(!s.isFullyInstantiated(ai) && isExpression){
            if(identicalFormals[i]){
               s.requireUnify(ai, iformals[i], error(current, "Cannot unify %t with %t", ai, iformals[i]));
               ai = s.instantiate(ai);
               //println("instantiated <actuals[i]>: <ai>");
            } else
                continue;
        }
        //println("comparable?: <ai>, <iformals[i]>");
        //actuals_i = (list[Expression] actualsExp := actuals) ? actualsExp[i] : ((list[Pattern] actualsPat := actuals) ? actualsPat[i] : current);
        s.requireComparable(ai, iformals[i], error(current, "Argument %v should have type %t, found %t", i, formalTypes[i], ai));
    }
    adtType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
    
    switch(keywordArguments){
    case (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`:
        checkExpressionKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsExp, bindings, scope, s);
    case (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`:
        checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArguments, bindings, scope, s);
    case []: ;
    default:
        throw rascalCheckerInternalError("computeADTReturnType: illegal keywordArguments: <keywordArguments>");
    }
    
    list[AType] parameters = [];
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := adtType){
       params = {};
       for(<key, idr, tp> <- overloads){  
        if(isADTType(tp)) params += toSet(getADTTypeParameters(tp));
       }
       parameters = toList(params);
    } else {
        parameters = getADTTypeParameters(adtType);
    }
    for(p <- parameters){
        if(!bindings[p.pname]?) bindings[p.pname] = avoid();
    }
    if(!isEmpty(bindings)){
        try    return instantiateRascalTypeParams(s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles), bindings);
        catch invalidInstantiation(str msg):
               s.report(error(current, msg));
    }
   
    return adtType;
}

void checkExpressionKwArgs(list[Keyword] kwFormals, (KeywordArguments[Expression]) `<KeywordArguments[Expression] keywordArgumentsExp>`, Bindings bindings, loc scope, Solver s){
    if(keywordArgumentsExp is none) return;
 
    next_arg:
    for(kwa <- keywordArgumentsExp.keywordArgumentList){ 
        kwName = prettyPrintName(kwa.name);
        
        for(<ft, de> <- kwFormals){
           fn = ft.label;
           if(kwName == fn){
              ift = ft;
              if(!isEmpty(bindings)){
                  try   ift = instantiateRascalTypeParams(ft, bindings);
                  catch invalidInstantiation(str msg):
                        s.report(error(kwa, msg));
              }
              kwType = s.getType(kwa.expression);
              s.requireComparable(kwType, ift, error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, ift));
              continue next_arg;
           } 
        }
        availableKws = intercalateOr(["`<prettyAType(ft)> <ft.label>`" | < AType ft, Expression de> <- kwFormals]);
        switch(size(kwFormals)){
        case 0: availableKws ="; no other keyword parameters available";
        case 1: availableKws = "; available keyword parameter: <availableKws>";
        default:
            availableKws = "; available keyword parameters: <availableKws>";
        }
        
       s.report(error(kwa, "Undefined keyword argument %q%v", kwName, availableKws));
    }
} 

void checkPatternKwArgs(list[Keyword] kwFormals, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Bindings bindings, loc scope, Solver s){
    if(keywordArgumentsPat is none) return;
    next_arg:
    for(kwa <- keywordArgumentsPat.keywordArgumentList){ 
        kwName = prettyPrintName(kwa.name);
        
        for(<ft, de> <- kwFormals){
           fn = ft.label;
           if(kwName == fn){
              ift = ft;
              if(!isEmpty(bindings)){
                  try   ift = instantiateRascalTypeParams(ft, bindings);
                  catch invalidInstantiation(str msg):
                        s.report(error(kwa, msg));
              }
              kwType = getPatternType(kwa.expression, ift, scope, s);
            s.requireComparable(kwType, ift, error(kwa, "Keyword argument %q has type %t, expected %t", kwName, kwType, ift));
              continue next_arg;
           } 
        }
        availableKws = intercalateOr(["`<prettyAType(ft)> <ft.label>`" | </*str fn,*/ AType ft, Expression de> <- kwFormals]);
        switch(size(kwFormals)){
        case 0: availableKws ="; no other keyword parameters available";
        case 1: availableKws = "; available keyword parameter: <availableKws>";
        default:
            availableKws = "; available keyword parameters: <availableKws>";
        }
        
        s.report(error(kwa, "Undefined keyword argument %q%v", kwName, availableKws));
    }
}

list[Keyword] computeKwFormals(list[KeywordFormal] kwFormals, Solver s){
    return [<s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | kwf <- kwFormals];
}

list[Keyword] getCommonKeywords(aadt(str adtName, list[AType] parameters, _), loc scope, Solver s) =
     [ <s.getType(kwf.\type)[label=prettyPrintName(kwf.name)], kwf.expression> | d <- s.getDefinitions(adtName, scope, dataOrSyntaxRoles), kwf <- d.defInfo.commonKeywordFields ];
     
list[Keyword] getCommonKeywords(overloadedAType(rel[loc, IdRole, AType] overloads), loc scope, Solver s) = [ *getCommonKeywords(adt, scope, s) | <def, idr, adt> <- overloads ];
default list[Keyword] getCommonKeywords(AType atype, loc scope, Solver s) = [];
    
public AType computeFieldTypeWithADT(AType containerType, Tree field, loc scope, Solver s) {
    //println("computeFieldTypeWithADT: <containerType>, <field>");
    requireFullyInstantiated(s, containerType);
    fieldName = unescape("<field>");
    if(isNonTerminalType(containerType) && fieldName == "top"){
        return isStartNonTerminalType(containerType) ? getStartNonTerminalType(containerType) : containerType;
    }
    return s.getTypeInType(containerType, field, {fieldId(), keywordFieldId(), annoId()}, scope); // DURING TRANSITION: allow annoIds
}
    
@doc{Compute the type of field fn on type containerType. A checkFailed is thrown if the field is not defined on the given type.}
public AType computeFieldType(AType containerType, Tree field, loc scope, Solver s) {
    //println("computeFieldType: <containerType>, <field>");
    requireFullyInstantiated(s, containerType);
    if(!s.isFullyInstantiated(containerType)) throw TypeUnavailable();
    fieldName = unescape("<field>");
    
   if (isReifiedType(containerType)) {
        if(s.getConfig().classicReifier){
            if (fieldName == "symbol") {
                return s.getTypeInScopeFromName("Symbol", scope, {dataId()});
            } else if (fieldName == "definitions") {
               s.getTypeInScopeFromName("Symbol", scope, {dataId()});
                      
               s.getTypeInScopeFromName("Production", scope, {dataId()});
               return makeMapType(makeADTType("Symbol"), makeADTType("Production"));
                    
            } else {
               s.report(error(field, "Field %q does not exist on type `type` (classic reifier)", fieldName));
            }
         } else {
            if (fieldName == "symbol") { // TODO: symbol => atype
                return s.getTypeInScopeFromName("AType", scope, {dataId()});
            } else if (fieldName == "definitions") { // TODO definitions => ?
               s.getTypeInScopeFromName("AType", scope, {dataId()});
               s.getTypeInScopeFromName("AProduction", scope, {dataId()});
               return makeMapType(makeADTType("AType"), makeADTType("AProduction"));
                    
            } else {
               s.report(error(field, "Field %q does not exist on type `type`  (non-classic reifier)", fieldName));
            }
         }
    } else if(isNonTerminalType(containerType)){
        if(isStartNonTerminalType(containerType)){
           return computeFieldTypeWithADT(getStartNonTerminalType(containerType), field, scope, s);
        } else if(isNonTerminalIterType(containerType)){
            if(containerType.label == fieldName){
                return makeListType(getNonTerminalIterElement(containerType));
            }
        } else if(isNonTerminalSeqType(containerType)){
            for(tp <- getNonTerminalSeqTypes(containerType)){
                if(tp.label == fieldName){
                    return tp;
                }
            }
        } else if(isNonTerminalAltType(containerType)){
            for(tp <- getNonTerminalAltTypes(containerType)){
                if(tp.label == fieldName){
                    return tp;
                }
            }
        } 
     } else if(isAnyCharType(containerType)){
            return computeFieldTypeWithADT(treeType, field, scope, s);
    } else if (isTupleType(containerType)) {
        if(tupleHasFieldNames(containerType)){
            idx = indexOf(getTupleFieldNames(containerType), fieldName);
            if(idx >= 0)
                return getTupleFieldTypes(containerType)[idx];
            else
                s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        } else {
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
        }
    } else if (isLocType(containerType)) {
        if (fieldName in getBuiltinFieldMap()[aloc()])
            return getBuiltinFieldMap()[aloc()][fieldName];
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isDateTimeType(containerType)) {
        if (fieldName in getBuiltinFieldMap()[adatetime()])
            return getBuiltinFieldMap()[adatetime()][fieldName];
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isRelType(containerType)) {
        idx = indexOf(getRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeSetType(getRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isListRelType(containerType)) {
        idx = indexOf(getListRelFieldNames(containerType), fieldName);
        if(idx >= 0){
            return makeListType(getListRelFields(containerType)[idx]); 
        }
        else
           s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    } else if (isMapType(containerType)) {
           idx = indexOf(getMapFieldNames(containerType), fieldName);
        if(idx >= 0){
            return idx == 0 ? makeSetType(getMapDomainType(containerType)) : makeSetType(getMapRangeType(containerType));
        }
        else
            s.report(error(field, "Field %q does not exist on type %t", fieldName, containerType));
    
    } else if (isNodeType(containerType)) {
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
            } catch checkFailed(list[FailMessage] fms):/* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>>        catch e: /* continue with next overload */;
        }
        //println("computeSubscriptionType: <current>, <t1>, <tl> ==\> <overloadedAType(subscript_overloads)>");
        if(isEmpty(subscript_overloads)) s.report(error(current, "Expressions of type %t cannot be subscripted", t1));
        return overloadedAType(subscript_overloads);
    } else if (isListType(t1) && (!isListRelType(t1) || (isListRelType(t1) && size(tl) == 1 && isIntType(tl[0])))) {
        // TODO: At some point we should have separate notation for this, but this final condition treats list
        // relations indexed by one int value as lists, making this an index versus a projection
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a list expression, found %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type int, found %t", tl[0]));
        else
            return getListElementType(t1);
            //return makeListType(getListElementType(t1));
    } else if (isRelType(t1)) {
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
    } else if (isListRelType(t1)) {
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
    } else if (isMapType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a map expression, found %v", size(tl)));
        else if (!comparable(tl[0],getMapDomainType(t1)))
            s.report(error(current, "Expected subscript of type %t, found %t", getMapDomainType(t1),tl[0]));
        else
            return getMapRangeType(t1);
    } else if (isNodeType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a node expression, found %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else
            return avalue();
    } else if (isTupleType(t1)) {
        if (size(tl) != 1) {
            s.report(error(current, "Expected only 1 subscript for a tuple expression, found %v", size(tl)));
        } else if (!isIntType(tl[0])) {
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
    } else if (isStrType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a string expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else
            return astr();
    } else if (isNonTerminalType(t1)) {
        if (size(tl) != 1)
            s.report(error(current, "Expected only 1 subscript for a nonterminal subscript expression, not %v", size(tl)));
        else if (!isIntType(tl[0]))
            s.report(error(current, "Expected subscript of type `int`, found %t", tl[0]));
        else if (isNonTerminalIterType(t1))
            return getNonTerminalIterElement(t1);
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
            } catch checkFailed(list[FailMessage] fms): /* continue with next overload */;
              catch NoBinding(): /* continue with next overload */;
 //>>         catch e: /* continue with next overload */; 
        }
        if(isEmpty(slice_overloads)) s.report(error(current, "Slice cannot be computed for %t", base));
        return overloadedAType(slice_overloads);
    }
    
    failures = [];
    if(!isIntType(first)) failures += error(current, "The first slice index must be of type `int`, found %t", first);
    if(!isIntType(step)) failures  += error(current, "The slice step must be of type `int`, found %t", step);
    if(!isIntType(last)) failures  += error(current, "The last slice index must be of type `int`, found %t", last);
    
    if(!isEmpty(failures)) throw s.reports(failures);
    
    if (isListType(base) || isStrType(base) || isNonTerminalIterType(base)) {
        return base;
    } else if (isNodeType(base)) {
        return makeListType(avalue());
    }
    
    s.reports(failures + error(current, "Slices can only be used on (concrete) lists, strings, and nodes, found %t", base));
    return avalue();
}

//AType computeGetAnnotationType(Tree current, AType t1, AType t2, Solver s)
//    = binaryOp("get annotation", _computeGetAnnotationType, current, t1, t2, s);
//    
//private AType _computeGetAnnotationType(Tree current, AType t1, AType tn, Solver s){
//    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) {
//        if(aanno(_, onType, annoType) := tn){
//           return annoType;
//        } else
//            s.report(error(current, "Invalid annotation type: %t", tn));
//    } else {
//        s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
//    }
//    return avalue();
//}

@doc{Calculate the arith type for the numeric types, taking account of coercions.}
public AType numericArithTypes(AType l, AType r) {
    if (isIntType(l) && isIntType(r)) return aint();
    if (isIntType(l) && isRatType(r)) return arat();
    if (isIntType(l) && isRealType(r)) return areal();
    if (isIntType(l) && isNumType(r)) return anum();

    if (isRatType(l) && isIntType(r)) return arat();
    if (isRatType(l) && isRatType(r)) return arat();
    if (isRatType(l) && isRealType(r)) return areal();
    if (isRatType(l) && isNumType(r)) return anum();

    if (isRealType(l) && isIntType(r)) return areal();
    if (isRealType(l) && isRatType(r)) return areal();
    if (isRealType(l) && isRealType(r)) return areal();
    if (isRealType(l) && isNumType(r)) return anum();

    if (isNumType(l) && isIntType(r)) return anum();
    if (isNumType(l) && isRatType(r)) return anum();
    if (isNumType(l) && isRealType(r)) return anum();
    if (isNumType(l) && isNumType(r)) return anum();

    throw rascalCheckerInternalError("Only callable for numeric types, given <l> and <r>");
}


AType computeAdditionType(Tree current, AType t1, AType t2, Solver s) 
    = binaryOp("addition", _computeAdditionType, current, t1, t2, s);

private AType _computeAdditionType(Tree current, AType t1, AType t2, Solver s) {
    if(isNumericType(t1) && isNumericType(t2)) return numericArithTypes(t1, t2);
    
    if (isStrType(t1) && isStrType(t2))
        return astr();
    if (isBoolType(t1) && isBoolType(t2))
        return abool();
    if (isLocType(t1) && isLocType(t2))
        return aloc();
    if (isLocType(t1) && isStrType(t2))
        return aloc();
        
     if (isTupleType(t1) && isTupleType(t2)) {
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
       
    if (isListType(t1) && isListType(t2))
        return s.lub(t1,t2);
    if (isSetType(t1) && isSetType(t2))
        return s.lub(t1,t2);
    if (isMapType(t1) && isMapType(t2))
        return s.lub(t1,t2);
        
    if (isListType(t1) && !isContainerType(t2))
        return makeListType(s.lub(getListElementType(t1),t2));
    if (isSetType(t1) && !isContainerType(t2)) // Covers relations too
        return makeSetType(s.lub(getSetElementType(t1),t2));
    if (isBagType(t1) && !isContainerType(t2))
        return abag(s.lub(getBagElementType(t1),t2));
        
    if (isListType(t2) && !isContainerType(t1))
        return makeListType(s.lub(t1,getListElementType(t2)));
    if (isSetType(t2) && !isContainerType(t1)) // Covers relations too
        return makeSetType(s.lub(t1,getSetElementType(t2)));
    if (isBagType(t2) && !isContainerType(t1))
        return abag(s.lub(t1,getBagElementType(t2)));
        
    if (isListType(t1))
        return makeListType(s.lub(getListElementType(t1),t2));
    if (isSetType(t1)) // Covers relations too
        return makeSetType(s.lub(getSetElementType(t1),t2));
    if (isBagType(t1))
        return abag(s.lub(getBagElementType(t1),t2));
    
    // TODO: Can we also add together constructor types?
    // TODO: cloc is arbitrary, can we do better?
    cloc = getLoc(current);
    if (isFunctionType(t1)){
        if(isFunctionType(t2))
            return overloadedAType({<cloc, functionId(), t1>, <cloc, functionId(), t2>});
        else if(overloadedAType(rel[loc, IdRole, AType] overloads) := t2){
            return overloadedAType(overloads + <cloc, functionId(), t1>);
        }
    } else if(overloadedAType(rel[loc, IdRole, AType] overloads1)  := t1){
        if(isFunctionType(t2))
           return overloadedAType(overloads1 + <cloc, functionId(), t2>);
        else if(overloadedAType(rel[loc, IdRole, AType] overloads2) := t2){
            return overloadedAType(overloads1 + overloads2);
        }
    }
    
    s.report(error(current, "Addition not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeSubtractionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("subtraction", _computeSubtractionType, current, t1, t2, s);

private AType _computeSubtractionType(Tree current, AType t1, AType t2, Solver s) { 
    if(isNumericType(t1) && isNumericType(t2)){
        return numericArithTypes(t1, t2);
    }
    if(isListType(t1) && isListType(t2)){
        s.requireComparable(getListElementType(t1), getListElementType(t2), error(current, "%v of type %t could never contain elements of second %v type %t", 
                                                                                    isListRelType(t1) ? "List Relation" : "List", t1, isListRelType(t2) ? "List Relation" : "List", t2));
       return t1;
    }
    
    if(isListType(t1)){
        s.requireComparable(getListElementType(t1), t2, error(current, "%v of type %t could never contain elements of type %t", isListRelType(t1) ? "List Relation" : "List", t1, t2));
        return t1;
    }
    if(isSetType(t1) && isSetType(t2)){
        s.requireComparable(getSetElementType(t1), getSetElementType(t2), error(current, "%v of type %t could never contain elements of second %v type %t", isRelType(t1) ? "Relation" : "Set", t1,isListRelType(t2) ? "Relation" : "Set", t2));
        return t1;
    }
    if(isSetType(t1)){
        s.requireComparable(getSetElementType(t1), t2, error(current, "%v of type %t could never contain elements of type %t", isRelType(t1) ? "Relation" : "Set", t1, t2));
        return t1;
    }

    if(isMapType(t1)){
        s.requireComparable(t1, t2, error(current, "Map of type %t could never contain a sub-map of type %t", t1, t2));
        return t1;
    }
    
    s.report(error(current, "Subtraction not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeProductType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("product", _computeProductType, current, t1, t2, s);

private AType _computeProductType(Tree current, AType t1, AType t2, Solver s){ 
    if(isNumericType(t1) && isNumericType(t2)) return numericArithTypes(t1, t2);
    
    if (isListType(t1) && isListType(t2))
        return makeListType(atuple(atypeList([getListElementType(t1),getListElementType(t2)])));
    if (isRelType(t1) && isRelType(t2))
        return arel(atypeList([getRelElementType(t1),getRelElementType(t2)]));
    if (isListRelType(t1) && isListRelType(t2))
        return alrel(atypeList([getListRelElementType(t1),getListRelElementType(t2)]));
    if (isSetType(t1) && isSetType(t2))
        return arel(atypeList([getSetElementType(t1),getSetElementType(t2)]));
    
    s.report(error(current, "Product not defined on %t and %t", t1, t2));
    return avalue();
}

AType computeDivisionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("division", _computeDivisionType, current, t1, t2, s);

private AType _computeDivisionType(Tree current, AType t1, AType t2, Solver s){
    if(!(isNumericType(t1) && isNumericType(t2))){
        s.report(error(current, "Division not defined on %t and %t", t1, t2));
    }
     return numericArithTypes(t1, t2);
}

AType computeIntersectionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("intersection", _computeIntersectionType, current, t1, t2, s);
    
private AType _computeIntersectionType(Tree current, AType t1, AType t2, Solver s){  
    if ( ( isListRelType(t1) && isListRelType(t2) ) || 
         ( isListType(t1) && isListType(t2) ) || 
         ( isRelType(t1) && isRelType(t2) ) || 
         ( isSetType(t1) && isSetType(t2) ) || 
         ( isMapType(t1) && isMapType(t2) ) )
    {
        if (!comparable(t1,t2))
            s.report(error(current, "Types %t and %t are not comparable", t1, t2));
            
        if (asubtype(t2, t1))
            return t2;
            
        if (asubtype(t1, t2))
            return t1;
            
        if (isListRelType(t1)) return makeListRelType(makeVoidType(),makeVoidType());
        if (isListType(t1)) return makeListType(makeVoidType());
        if (isRelType(t1)) return makeRelType(makeVoidType(), makeVoidType());
        if (isSetType(t1)) return makeSetType(makeVoidType());
        if (isMapType(t1)) return makeMapType(makeVoidType(),makeVoidType());
    }
    s.report(error(current, "Intersection not defined on %t and %t", t1, t2));
    return avalue();
}

// ---- getPatternType --------------------------------------------------------

AType getPatternType(Pattern p, AType subjectType, loc scope, Solver s){
    requireFullyInstantiated(s, subjectType);
    if(isConstructorType(subjectType)){
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
    elmType = isSetType(subjectType) ? getSetElementType(subjectType) : avalue();
    setType = aset(s.lubList([getPatternType(p, elmType, scope,s) | p <- elements0]));
    s.fact(current, setType);
    return setType;
}

// ---- list pattern

private AType getPatternType0(current: (Pattern) `[ <{Pattern ","}* elements0> ]`, AType subjectType, loc scope, Solver s){
    elmType = isListType(subjectType) ? getListElementType(subjectType) : avalue();
    res = alist(s.lubList([getPatternType(p, elmType, scope, s) | p <- elements0]));
    s.fact(current, res);
    return res;
}

// ---- typed variable pattern

private AType getPatternType0(current:( Pattern) `<Type tp> <Name name>`, AType subjectType, loc scope, Solver s){
    return s.getType(tp)[label=unescape("<name>")];
}

// ---- qualifiedName pattern: QualifiedName

private AType getPatternType0(current: (Pattern) `<QualifiedName name>`, AType subjectType, loc scope, Solver s){
    base = prettyPrintBaseName(name);
    if(base != "_"){
       nameType = s.getType(name);
       if(!s.isFullyInstantiated(nameType) || !s.isFullyInstantiated(subjectType)){
          s.requireUnify(nameType, subjectType, error(current, "Type of pattern could not be computed"));
          s.fact(name, nameType); // <====
          nameType = s.instantiate(nameType);
          s.fact(name, nameType);
          subjectType = s.instantiate(subjectType);
          //clearBindings();
       }
       s.requireComparable(nameType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, nameType));
       return nameType[label=unescape("<name>")];
    } else
       return subjectType[label=unescape("<name>")];
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
       if(uname == "_"){
          return s.getType(argument.\type);
       } else {
          inameType = s.getType(argument.name);
          s.fact(argument, inameType);
          if(isListType(inameType)) {
                elmType = getListElementType(inameType);
                s.fact(current, elmType);
                return elmType;
          }
          if(isSetType(inameType)){
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
         if(base == "_"){
            return subjectType;
         } else {
           elmType = subjectType;
           inameType = s.getType(argName);
           elmType = avoid();
           if(isListType(inameType)){
                elmType = getListElementType(inameType);
           } else if(isSetType(inameType)){
                elmType = getSetElementType(inameType);
           } else {
            s.report(error(argument, "List or set type expected, found %t", inameType));
           }
             
           if(!s.isFullyInstantiated(elmType) || !s.isFullyInstantiated(subjectType)){
              s.requireUnify(elmType, subjectType, error(current, "Type of pattern could not be computed"));
              elmType = s.instantiate(elmType);
              //s.fact(argName, nameElementType);//<<
              s.fact(current, elmType); // <<
              subjectType = s.instantiate(elmType);
           }
           s.requireComparable(elmType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, elmType));
           return elmType;
        }
    } else {
        s.report(error(current, "Unsupported construct in splice pattern"));
        return subjectType;
    }
}

// ---- splicePlus pattern: +Pattern ------------------------------------------

private AType getPatternType0(current: (Pattern) `+<Pattern argument>`, AType subjectType, loc scope, Solver s)
    = getSplicePatternType(current, argument, subjectType, s);

// ---- tuple pattern ---------------------------------------------------------

private AType getPatternType0(current: (Pattern) `\< <{Pattern ","}* elements1> \>`, AType subjectType, loc scope, Solver s){
    pats = [ p | Pattern p <- elements1 ];
    if(isTupleType(subjectType)){
        elmTypes = getTupleFieldTypes(subjectType);
        if(size(pats) == size(elmTypes)){
           if(tupleHasFieldNames(subjectType)){
             res = atuple(atypeList([getPatternType(pats[i], elmTypes[i], scope, s)[label= elmTypes[i].label] | int i <- index(pats)]));
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

private AType getPatternType0(current: (KeywordArgument[Pattern]) `<Name name> = <Pattern expression>`, AType subjectType, loc scope, Solver s){
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
    
    if(isStrType(texp)){
        return computePatternNodeType(current, scope, pats, keywordArguments, s, subjectType);
    }       
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := texp){
       bareArgTypes = for(p <- pats){
                        try {
                            append s.getType(p);
                        } catch TypeUnavailable():
                            append avalue();
                      };
       <filteredOverloads, identicalFields> = filterOverloadedConstructors(overloads, bareArgTypes, subjectType, s);
       if({<key, idr, tp>} := filteredOverloads){
          texp = tp;
          s.fact(expression, tp);
       } else {
          if(insideFormals(s) && (size(overloads) > 1)){
            if(any(tp <- bareArgTypes, !s.isFullyInstantiated(tp))){
                defs = itemizeLocs(filteredOverloads<0>);
                s.report(error(expression, "Constructor `%v` in formal parameter should be unique, found %t, defined at %v", "<expression>", expression, defs));
                return avalue();
            }
          }
         overloads = filteredOverloads;
         validReturnTypeOverloads = {};
         validOverloads = {};
         next_cons:
         for(ovl: <key, idr, tp> <- overloads){
            if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := tp){
               try {
                     validReturnTypeOverloads += <key, idr, computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, identicalFields, s)>;
                     validOverloads += ovl;
                    } catch checkFailed(list[FailMessage] fms):
                            continue next_cons;
                      catch NoBinding():
                            continue next_cons;
             }
        }
        if({<key, idr, tp>} := validOverloads){
           texp = tp; 
           s.fact(expression, tp);
            // TODO check identicalFields to see whether this can make sense
           // unique overload, fall through to non-overloaded case to potentially bind more type variables
        } else if(isEmpty(validReturnTypeOverloads)) s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
        else return overloadedAType(validReturnTypeOverloads);
      }
    }

    if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := texp){
       return computeADTType(current, adtName, scope, adtType, fields, kwFields, pats, keywordArguments, [true | int i <- index(fields)], s);
    }
    s.report(error(current, "No pattern constructor found for %q of expected type %t", expression, subjectType));
    return avalue();
}

tuple[rel[loc, IdRole, AType], list[bool]] filterOverloadedConstructors(rel[loc, IdRole, AType] overloads, list[AType] argTypes, AType subjectType, Solver s){
    int arity = size(argTypes);
    filteredOverloads = {};
    identicalFields = [true | int i <- [0 .. arity]];
    
    uninstantiated = [ !s.isFullyInstantiated(argTypes[i]) | int i <- [0 .. arity] ];
    bool acceptable(int i, AType a, AType b)
        = uninstantiated[i] || comparable(a, b);
    
    for(ovl:<key, idr, tp> <- overloads){                       
        if(acons(ret:aadt(_, list[AType] _, _), list[AType] fields, list[Keyword] _) := tp, comparable(ret, subjectType)){
           if(size(fields) == arity && (arity == 0 || all(int i <- index(fields), acceptable(i, fields[i], argTypes[i])))){
            filteredOverloads += ovl;
           }
        }
    }
    return <filteredOverloads, identicalFields>;
}

AType computePatternNodeType(Tree current, loc scope, list[Pattern] patList, (KeywordArguments[Pattern]) `<KeywordArguments[Pattern] keywordArgumentsPat>`, Solver s, AType subjectType){                     
    dontCare = [ "<patList[i]>" == "_" | i <- index(patList) ];
    actualType = [ dontCare[i] ? avalue() : getPatternType(patList[i], avalue(), scope, s) | i <- index(patList) ];
    
    if(adtType:aadt(adtName, list[AType] parameters,_) := subjectType){
       declaredInfo = s.getDefinitions(adtName, scope, dataOrSyntaxRoles);
       declaredType = s.getTypeInScopeFromName(adtName, scope, dataOrSyntaxRoles);
       checkPatternKwArgs(getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return subjectType;
    } else if(acons(adtType:aadt(adtName, list[AType] parameters, _), list[AType] fields, list[Keyword] kwFields) := subjectType){
       kwFormals = kwFields;
       checkPatternKwArgs(kwFormals + getCommonKeywords(adtType, scope, s), keywordArgumentsPat, (), scope, s);
       return anode([]);
    } else if(anode(list[AType] fields) := subjectType){
        return computePatternNodeTypeWithKwArgs(current, keywordArgumentsPat, fields, scope, s);
    } else if(isValueType(subjectType)){
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
           fn = ft.label;
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
    return getPatternType(pattern, subjectType, scope, s)[label=unescape("<name>")];
}

// ---- typed variable becomes

private AType getPatternType0(current: (Pattern) `<Type tp> <Name name> : <Pattern pattern>`, AType subjectType, loc scope, Solver s){
    declaredType = s.getType(name);
    patType = getPatternType(pattern, subjectType, scope, s);
    s.requireComparable(patType, declaredType, error(current, "Incompatible type in assignment to variable %q, expected %t, found %t", name, declaredType, patType));
    return declaredType[label=unescape("<name>")];
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
    getPatternType(pattern, avalue(), scope, s);
    return avoid();
}