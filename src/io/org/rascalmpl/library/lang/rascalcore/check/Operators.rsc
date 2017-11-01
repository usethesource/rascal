module lang::rascalcore::check::Operators

extend analysis::typepal::TypePal;

import lang::rascalcore::check::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::Scope;
import lang::rascalcore::check::ATypeInstantiation;

import lang::rascal::\syntax::Rascal;

// ---- is

void collect(current: (Expression) `<Expression e> is <Name n>`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("is", current, [e],
        () { t1 = getType(e);
             if(isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) return;
             reportError(current, "Invalid type: expected node, ADT, or concrete syntax types, found <fmt(t1)>");
           });
    
    collectParts(current, tb); 
} 

// ---- has

void collect(current: (Expression) `<Expression e> has <Name n>`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("has", current, [e],
        () { t1 = getType(e);
             if (isRelType(t1) || isListRelType(t1) || isTupleType(t1) || isADTType(t1) || isNonTerminalType(t1) || isNodeType(t1)) return;
             reportError(current, "Invalid type: expected relation, tuple, node or ADT types, found <fmt(t1)>");
           });
    
    collectParts(current, tb); 
} 

// ---- transitive closure
 
void collect(current: (Expression) `<Expression arg> +`, TBuilder tb){
    tb.calculate("transitive closure", current, [arg],
        AType() { return computeTransClosureType(current, getType(arg)); });
    
    collectParts(current, tb); 
} 

AType computeTransClosureType(Expression current, AType t1){
    // Special case: if we have list[void] or set[void], these become lrel[void,void] and rel[void,void]
    if (isListType(t1) && isVoidType(getListElementType(t1)))
        return makeListRelType([makeVoidType(),makeVoidType()]);
    if (isSetType(t1) && isVoidType(getSetElementType(t1)))
        return makeRelType([makeVoidType(),makeVoidType()]);
        
    // Normal case: we have an actual list or relation
    if (isRelType(t1) || isListRelType(t1)) {
        list[AType] flds = isRelType(t1) ? getRelFields(t1) : getListRelFields(t1);
        if (size(flds) == 0) {
            return t1;
        } else if (size(flds) == 2 && equivalent(flds[0],flds[1])) {    
            return t1;
        } else {
            reportError(current, "Invalid type: expected a binary relation over equivalent types, found <fmt(t1)>");
        }
    } else {
        reportError(current, "Invalid type: expected a binary relation, found <fmt(t1)>");
    }
}
// ---- reflexive transitive closure

void collect(current: (Expression) `<Expression arg> *`, TBuilder tb){
    tb.calculate("transitive clsoure", current, [arg],
        AType() { return computeReflexiveTransClosureType(current, getType(arg)); });
    
    collectParts(current, tb); 
} 

AType computeReflexiveTransClosureType(Expression current, AType t1){
    // Special case: if we have list[void] or set[void], these become lrel[void,void] and rel[void,void]
    if (isListType(t1) && isVoidType(getListElementType(t1)))
        return makeListRelType(atypeList([makeVoidType(),makeVoidType()]));
    if (isSetType(t1) && isVoidType(getSetElementType(t1)))
        return makeRelType(atypeList([makeVoidType(),makeVoidType()]));
        
    // Normal case: we have an actual list or relation
    if (isRelType(t1) || isListRelType(t1)) {
        list[AType] flds = isRelType(t1) ? getRelFields(t1) : getListRelFields(t1);
        if (size(flds) == 0) {
            return t1;
        } else if (size(flds) == 2 && equivalent(flds[0],flds[1])) {    
            return t1;
        } else {
            reportError(current, "Invalid type: expected a binary relation over equivalent types, found <fmt(t1)>");
        }
    } else {
        reportError(current, "Invalid type: expected a binary relation, found <fmt(t1)>");
    }
}

// ---- isDefined

void collect(current: (Expression) `<Expression arg> ?`, TBuilder tb){
    tb.fact(current, abool());
    collectParts(current, tb); 
}

// ---- negation

void collect(current: (Expression) `! <Expression arg>`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("negation", current, [arg],
        (){ t1 = getType(arg);
            if(!isBoolType(t1)) reportError(current, "Negation not defined on <fmt(t1)>");
        });
    collectParts(current, tb); 
}

// ---- negative

void collect(current: (Expression) `- <Expression arg>`, TBuilder tb){
    tb.calculate("negative", current, [arg],
        AType(){ t1 = getType(arg);
                 if(isNumericType(t1)) return t1;
                 reportError(current, "Negative not defined on <fmt(t1)>");
        });
    collectParts(current, tb); 
}
// ---- splice

void collect(current: (Expression) `* <Expression arg>`, TBuilder tb){
    tb.calculate("splice", current, [arg], AType(){ return computeSpliceType(getType(arg)); });
    collectParts(current, tb); 
}

AType computeSpliceType(AType t1){
    if (isListType(t1)) return getListElementType(t1);
    if (isSetType(t1)) return getSetElementType(t1);
    if (isBagType(t1)) return getBagElementType(t1);
    if (isRelType(t1)) return getRelElementType(t1);
    if (isListRelType(t1)) return getListRelElementType(t1);
    return t1;
}

// ---- asType

void collect(current: (Expression)`[ <Type t> ] <Expression e>`, TBuilder tb){
    scope = tb.getScope();
    reqType = convertType(t, tb);
    
    tb.calculate("asType", current, [e],
        AType() { subtype(getType(e), astr(), onError(e, "Expected str, instead found <fmt(getType(e))>"));
                  return expandUserTypes(reqType, scope);
                });
    collectParts(current, tb);
}


// ---- composition  TODO

void collect(current: (Expression) `<Expression lhs> o <Expression rhs>`, TBuilder tb){
    tb.calculate("composition", current, [lhs, rhs],  AType(){ return computeCompositionType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

AType computeCompositionType(Expression current, AType t1, AType t2){  

//    // Special handling for list[void] and set[void], these should be treated as lrel[void,void]
//    // and rel[void,void], respectively
//    if (isListType(t1) && isVoidType(getListElementType(t1))) t1 = makeListRelType(makeVoidType(),makeVoidType());
//    if (isListType(t2) && isVoidType(getListElementType(t2))) t2 = makeListRelType(makeVoidType(),makeVoidType());
//    if (isSetType(t1) && isVoidType(getSetElementType(t1))) t1 = makeRelType(makeVoidType(),makeVoidType());
//    if (isSetType(t2) && isVoidType(getSetElementType(t2))) t2 = makeRelType(makeVoidType(),makeVoidType());
//    
//    
//    if (isMapType(t1) && isMapType(t2)) {
//        if (subtype(getMapRangeType(t1),getMapDomainType(t2))) {
//            return markLocationType(c, exp@\loc, makeMapType(stripLabel(getMapDomainType(t1)),stripLabel(getMapRangeType(t2))));
//        } else {
//            return markLocationFailed(c, exp@\loc, makeFailType("<prettyPrintType(getMapRangeType(t1))> must be a subtype of <prettyPrintType(getMapDomainType(t2))>", exp@\loc));
//        }
//    }
//    
//    if (isRelType(t1) && isRelType(t2)) {
//        list[Symbol] lflds = getRelFields(t1);
//        list[Symbol] rflds = getRelFields(t2);
//        set[Symbol] failures = { };
//        if (size(lflds) != 0 && size(lflds) != 2)
//            failures += makeFailType("Relation <prettyPrintType(t1)> should have arity of 0 or 2", e1@\loc); 
//        if (size(rflds) != 0 && size(rflds) != 2)
//            failures += makeFailType("Relation <prettyPrintType(t2)> should have arity of 0 or 2", e2@\loc);
//        if (!comparable(lflds[1],rflds[0]))
//            failures += makeFailType("Range of relation <prettyPrintType(t1)> must be comparable to domain of relation <prettyPrintType(t1)>", exp@\loc);
//        if (size(failures) > 0) return markLocationFailed(c, exp@\loc, failures);
//        if (size(lflds) == 0 || size(rflds) == 0)
//            return markLocationType(c, exp@\loc, \rel([]));
//        else
//            return markLocationType(c, exp@\loc, \rel([lflds[0],rflds[1]])); 
//    }
//
//    if (isListRelType(t1) && isListRelType(t2)) {
//        list[Symbol] lflds = getListRelFields(t1);
//        list[Symbol] rflds = getListRelFields(t2);
//        set[Symbol] failures = { };
//        if (size(lflds) != 0 && size(lflds) != 2)
//            failures += makeFailType("List relation <prettyPrintType(t1)> should have arity of 0 or 2", e1@\loc); 
//        if (size(rflds) != 0 && size(rflds) != 2)
//            failures += makeFailType("List relation <prettyPrintType(t2)> should have arity of 0 or 2", e2@\loc);
//        if (!comparable(lflds[1],rflds[0]))
//            failures += makeFailType("Range of list relation <prettyPrintType(t1)> must be comparable to domain of list relation <prettyPrintType(t1)>", exp@\loc);
//        if (size(failures) > 0) return markLocationFailed(c, exp@\loc, failures);
//        if (size(lflds) == 0 || size(rflds) == 0)
//            return markLocationType(c, exp@\loc, \lrel([]));
//        else
//            return markLocationType(c, exp@\loc, \lrel([lflds[0],rflds[1]])); 
//    }
//
//    if (isFunctionType(t1) && isFunctionType(t2)) {
//        compositeArgs = getFunctionArgumentTypes(t2);
//        compositeRet = getFunctionReturnType(t1);
//        linkingArgs = getFunctionArgumentTypes(t1);
//        
//        // For f o g, f should have exactly one formal parameter
//        if (size(linkingArgs) != 1) {
//            ft = makeFailType("In a composition of two functions the leftmost function must have exactly one formal parameter.", exp@\loc);
//            return markLocationFailed(c, exp@\loc, ft);
//        }
//        
//        // and, that parameter must be of a type that a call with the return type of g would succeed
//        linkingArg = linkingArgs[0];
//        rightReturn = getFunctionReturnType(t2);
//        if (!subtype(rightReturn, linkingArg)) {
//            ft = makeFailType("The return type of the right-hand function, <prettyPrintType(rightReturn)>, cannot be passed to the left-hand function, which expects type <prettyPrintType(linkingArg)>", exp@\loc);
//            return markLocationFailed(c, exp@\loc, ft);          
//        }
//        
//        // If both of those pass, the result type is a function with the args of t2 and the return type of t1
//        rt = Symbol::\func(compositeRet, compositeArgs,[]);
//        return markLocationType(c, exp@\loc, rt);         
//    }
//    
//    // Here, one or both types are overloaded functions, with at most one a normal function.
//    if ((isOverloadedType(t1) || isFunctionType(t1)) && (isOverloadedType(t2) || isFunctionType(t2))) {
//        // Step 1: get back all the type possibilities on the left and right
//        leftFuns = (isFunctionType(t1)) ? { t1 } : (getNonDefaultOverloadOptions(t1) + getDefaultOverloadOptions(t1));
//        rightFuns = (isFunctionType(t2)) ? { t2 } : (getNonDefaultOverloadOptions(t2) + getDefaultOverloadOptions(t2));
//        
//        // Step 2: filter out leftmost functions that cannot be used in compositions
//        leftFuns = { f | f <- leftFuns, size(getFunctionArgumentTypes(f)) == 1 };
//        
//        // Step 3: combine the ones we can -- the return of the rightmost type has to be allowed
//        // as the parameter for the leftmost type
//        newFunTypes = { Symbol::\func(getFunctionReturnType(lf), getFunctionArgumentTypes(rf), []) |
//            rf <- rightFuns, lf <- leftFuns, subtype(getFunctionReturnType(rf),getFunctionArgumentTypes(lf)[0]) };
//            
//        // Step 4: If we get an empty set, fail; if we get just 1, return that; if we get multiple possibilities,
//        // return an overloaded type
//        if (size(newFunTypes) == 0) {
//            ft = makeFailType("The functions cannot be composed", exp@\loc);
//            return markLocationFailed(c, exp@\loc, ft);
//        } else if (size(newFunTypes) == 1) {
//            return markLocationType(c, exp@\loc, getFirstFrom(newFunTypes));
//        } else {
//            // TODO: Do we need to keep track of defaults through all this? If so, do we compose default
//            // and non-default functions?
//            return markLocationType(c, exp@\loc, \overloaded(newFunTypes,{}));
//        }
//    }
//
//    return markLocationFailed(c, exp@\loc, makeFailType("Composition not defined for <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));

    return avalue();

}

// ---- product

void collect(current: (Expression) `<Expression lhs> * <Expression rhs>`, TBuilder tb){
    tb.calculate("product", current, [lhs, rhs],  AType(){ return computeProductType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

AType computeProductType(Tree current, AType t1, AType t2){    
    if(subtype(t1, anum()) && subtype(t2, anum())) return lub(t1, t2);
    
    if (isListType(t1) && isListType(t2))
        return makeListType(atuple(atypeList([getListElementType(t1),getListElementType(t2)])));
    if (isRelType(t1) && isRelType(t2))
        return arel(atypeList([getRelElementType(t1),getRelElementType(t2)]));
    if (isListRelType(t1) && isListRelType(t2))
        return alrel(atypeList([getListRelElementType(t1),getListRelElementType(t2)]));
    if (isSetType(t1) && isSetType(t2))
        return arel(atypeList([getSetElementType(t1),getSetElementType(t2)]));
    
    reportError(current, "Product not defined on <fmt(t1)> and <fmt(t2)>");
}

// ---- join

void collect(current: (Expression) `<Expression lhs> join <Expression rhs>`, TBuilder tb){
    tb.calculate("join", current, [lhs, rhs], AType(){ return computeJoinType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

AType computeJoinType(Expression current, AType t1, AType t2){     
    if ((isRelType(t1) && isRelType(t2)) || (isListRelType(t1) && isListRelType(t2))) {
       bool isRel = isRelType(t1);
        list[AType] lflds = isRel ? getRelFields(t1) : getListRelFields(t1);
        list[AType] rflds = isRel ? getRelFields(t2) : getListRelFields(t2);
       
        // If possible, we want to maintain the field names; check here to see if that
        // is possible. We can when 1) both relations use field names, and 2) the names
        // used are distinct.
        list[str] llabels = isRel ? getRelFieldNames(t1) : getListRelFieldNames(t1);
        list[str] rlabels = isRel ? getRelFieldNames(t2) : getListRelFieldNames(t2);
        
        set[str] labelSet = toSet(llabels) + toSet(rlabels);
        flds = atypeList(lflds+rflds);
        if (size(llabels) == size(lflds) && size(rlabels) == size(rflds) && 
            size(labelSet) == size(llabels) + size(rlabels)) {  
            return (isRel ? arel(flds) : alrel(flds));
        } else {
            return isRel ? arel(flds) : alrel(flds); 
        }
    }

    if (isRelType(t1) && isSetType(t2))
        return arel( atypeList(getRelFields(t1) + getSetElementType(t2)) );
    
    if (isSetType(t1) && isRelType(t2))
        return arel( atypeList(getSetElementType(t1) + getRelFields(t2)) );
    
    if (isListRelType(t1) && isListType(t2))
        return alrel( atypeList(getListRelFields(t1) + getListElementType(t2)) );
    
    if (isListType(t1) && isListRelType(t2))
        return alrel( atypeList(getListElementType(t1) +getListRelFields(t2)) );
    
    if (isListType(t1) && isListType(t2))
        return alrel( atypeList(getListElementType(t1), getListElementType(t2))) ;
    
    if (isSetType(t1) && isSetType(t2))
        return arel( atypeList([getSetElementType(t1), getSetElementType(t2)]) );
    
    reportError(current, "Join not defined for <fmt(t1)> and <fmt(t2)>");
} 

// ---- remainder

void collect(current: (Expression) `<Expression lhs> % <Expression rhs>`, TBuilder tb){
    tb.calculate("remainder", current, [lhs, rhs],
        AType(){ t1 = getType(lhs); t2 = getType(rhs);
                 if(isIntType(t1) && isIntType(t2)) return lub(t1, t2);
                 reportError(current, "Remainder not defined on <fmt(t1)> and <fmt(t2)>");
        });
    collectParts(current, tb); 
}

// ---- division

void collect(current: (Expression) `<Expression lhs> / <Expression rhs>`, TBuilder tb){
    tb.calculate("division", current, [lhs, rhs], AType(){ return computeDivisionType(current, getType(lhs), getType(rhs));  });
    collectParts(current, tb); 
}

AType computeDivisionType(Tree current, AType t1, AType t2){
    if(isNumericType(t1) && isNumericType(t2)) return lub(t1, t2);
    reportError(current, "Division not defined on <fmt(t1)> and <fmt(t2)>");
}

// ---- intersection

void collect(current: (Expression) `<Expression lhs> & <Expression rhs>`, TBuilder tb){
    tb.calculate("intersection", current, [lhs, rhs], AType() { return computeIntersectionType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb);
}

AType computeIntersectionType(Tree current, AType t1, AType t2){
    if ( ( isListRelType(t1) && isListRelType(t2) ) || 
         ( isListType(t1) && isListType(t2) ) || 
         ( isRelType(t1) && isRelType(t2) ) || 
         ( isSetType(t1) && isSetType(t2) ) || 
         ( isMapType(t1) && isMapType(t2) ) )
    {
        if (!comparable(t1,t2))
            reportError(current, "Types <fmt(t1)> and <fmt(t2)> are not comparable");
            
        if (subtype(t2, t1))
            return t2;
            
        if (subtype(t1, t2))
            return t1;
            
        if (isListRelType(t1)) return makeListRelType(makeVoidType(),makeVoidType());
        if (isListType(t1)) return makeListType(makeVoidType());
        if (isRelType(t1)) return makeRelType(makeVoidType(), makeVoidType());
        if (isSetType(t1)) return makeSetType(makeVoidType());
        if (isMapType(t1)) return makeMapType(makeVoidType(),makeVoidType());
    }
    reportError(current, "Intersection not defined on <fmt(t1)> and <fmt(t2)>");
}

// ---- addition

void collect(current: (Expression) `<Expression lhs> + <Expression rhs>`, TBuilder tb){
    tb.calculate("addition", current, [lhs, rhs], AType() { return computeAdditionType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

default AType computeAdditionType(Tree current, AType t1, AType t2) {
    
    if(subtype(t1, anum()) && subtype(t2, anum())) return lub(t1, t2);
    
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
        return lub(t1,t2);
    if (isSetType(t1) && isSetType(t2))
        return lub(t1,t2);
    if (isMapType(t1) && isMapType(t2))
        return lub(t1,t2);
        
    if (isListType(t1) && !isContainerType(t2))
        return makeListType(lub(getListElementType(t1),t2));
    if (isSetType(t1) && !isContainerType(t2)) // Covers relations too
        return makeSetType(lub(getSetElementType(t1),t2));
    if (isBagType(t1) && !isContainerType(t2))
        return abag(lub(getBagElementType(t1),t2));
        
    if (isListType(t2) && !isContainerType(t1))
        return makeListType(lub(t1,getListElementType(t2)));
    if (isSetType(t2) && !isContainerType(t1)) // Covers relations too
        return makeSetType(lub(t1,getSetElementType(t2)));
    if (isBagType(t2) && !isContainerType(t1))
        return abag(lub(t1,getBagElementType(t2)));
        
    if (isListType(t1))
        return makeListType(lub(getListElementType(t1),t2));
    if (isSetType(t1)) // Covers relations too
        return makeSetType(lub(getSetElementType(t1),t2));
    if (isBagType(t1))
        return abag(lub(getBagElementType(t1),t2));
        
    //// If we are adding together two functions, this creates an overloaded
    //// type with the two items as non-defaults.
    //// TODO: If we need to track default status here as well, we will need
    //// to special case plus to handle f + g, where f and g are both function
    //// names, and catch this before evaluating them both and retrieving their
    //// types.
    //// TODO: Can we also add together constructor types?
    //if (isFunctionType(t1) && isFunctionType(t2))
    //    return \overloaded({t1,t2},{});
    //else if (\overloaded(nd1,d1) := t1 && \overloaded(nd2,d2) := t2)
    //    return \overloaded(nd1+nd2,d1+d2);
    //else if (\overloaded(nd1,d1) := t1 && isFunctionType(t2))
    //    return \overloaded(nd1+t2,d1);
    //else if (isFunctionType(t1) && \overloaded(nd2,d2) := t2)
    //    return \overloaded(nd2+t1,d2);
    
    reportError(current, "Addition not defined on <fmt(t1)> and <fmt(t2)>");
}

// ---- subtraction

void collect(current: (Expression) `<Expression lhs> - <Expression rhs>`, TBuilder tb){
    tb.calculate("subtraction", current, [lhs, rhs], AType() { return computeSubtractionType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

AType computeSubtractionType(Tree current, AType t1, AType t2) { 
    if(isNumericType(t1) && isNumericType(t2)){
        return lub(t1, t2);
    }
    if(isListType(t1) && isListType(t2)){
        if(comparable(getListElementType(t1),getListElementType(t2))) return t1;
        reportError(current, "<isListRelType(t1) ? "List Relation" : "List"> of type <fmt(t1)> could never contain elements of second <isListRelType(t2) ? "List Relation" : "List"> type <fmt(t2)>");
    }
    
    if(isListType(t1)){
        if(comparable(getListElementType(t1),t2)) return t1;
        reportError(current, "<isListRelType(t1) ? "List Relation" : "List"> of type <fmt(t1)> could never contain elements of type <fmt(t2)>");
    }
    if(isSetType(t1) && isSetType(t2)){
        if(comparable(getSetElementType(t1),getSetElementType(t2))) return t1;
        reportError(current, "<isRelType(t1) ? "Relation" : "Set"> of type <fmt(t1)> could never contain elements of second <isListRelType(t2) ? "Relation" : "Set"> type <fmt(t2)>");
    }
    if(isSetType(t1)){
        if(comparable(getSetElementType(t1),t2)) return t1;
        reportError(current, "<isRelType(t1) ? "Relation" : "Set"> of type <fmt(t1)> could never contain elements of type <fmt(t2)>");
    }

    if(isMapType(t1)){
        if(comparable(t1, t2)) return t1;
        reportError(current, "Map of type <fmt(t1)> could never contain a sub-map of type <fmt(t2)>");
    }
    
    reportError(current, "Subtraction not defined on <fmt(t1)> and <fmt(t2)>");
}

//AType computeSubtractionType(Expression exp, t1:alist(_), AType t2) { 
//    if(isListType(t2)){
//        if(comparable(getListElementType(t1),getListElementType(t2))) return t1;
//        reportError(exp, "<isListRelType(t1) ? "List Relation" : "List"> of type <fmt(t1)> could never contain elements of second <isListRelType(t2) ? "List Relation" : "List"> type <fmt(t2)>");
//    }
//    if(comparable(getListElementType(t1),t2)) return t1;
//    reportError(exp, "<isListRelType(t1) ? "List Relation" : "List"> of type <fmt(t1)> could never contain elements of type <fmt(t2)>");
//}
//
//AType computeSubtractionType(t1:aset(_), AType t2) { 
//    if(issetType(t2)){
//        if(comparable(getSetElementType(t1),getSetElementType(t2))) return t1;
//        reportError(exp, "<isRelType(t1) ? "Relation" : "Set"> of type <fmt(t1)> could never contain elements of second <isListRelType(t2) ? "Relation" : "Set"> type <fmt(t2)>");
//    }
//    if(comparable(getSetElementType(t1),t2)) return t1;
//    reportError(exp, "<isRelType(t1) ? "Relation" : "Set"> of type <fmt(t1)> could never contain elements of type <fmt(t2)>");
//}
//
//AType computeSubtractionType(t1:amap(_,_), t2:amap(_,_)) { 
//    if(comparable(t1, t2)) return t1;
//    reportError(exp, "Map of type <fmt(t1)> could never contain a sub-map of type <fmt(t2)>");
//}
//
//default AType computeSubtractionType(Expression exp, AType t1, AType t2){
//    if(isNumericType(t1) && isNumericType(t2)){
//        return lub(t1, t2);
//    }
//    reportError(exp, "Subtraction not defined on <fmt(t1)> and <fmt(t2)>");
//
//}

// ---- appendAfter

void collect(current: (Expression) `<Expression lhs> \<\< <Expression rhs>`, TBuilder tb){
    tb.calculate("append after", current, [lhs, rhs],
        AType(){ t1 = getType(lhs); t2 = getType(rhs);
                 if (isListType(t1)) {
                    return makeListType(lub(getListElementType(t1),t2));
                 }
                reportError("Expected a list type, not type <fmt(t1)>");
        });
    collectParts(current, tb); 
}

// ---- insertBefore

void collect(current: (Expression) `<Expression lhs> \>\> <Expression rhs>`, TBuilder tb){
    tb.calculate("insert before", current, [lhs, rhs],
        AType(){ t1 = getType(lhs); t2 = getType(rhs);
                 if (isListType(t2)) {
                    return makeListType(lub(getListElementType(t2),t1));
                 }

                reportError("Expected a list type, not type <fmt(t2)>");
        });
    collectParts(current, tb); 
}

// ---- modulo

void collect(current: (Expression) `<Expression lhs> mod <Expression rhs>`, TBuilder tb){
    tb.calculate("modulo", current, [lhs, rhs],
        AType(){ t1 = getType(lhs); t2 = getType(rhs);
                 if (isIntType(t1) && isIntType(t2)) {
                    return aint();
                 }
                reportError("Modulo not defined on <fmt(t1)> and <fmt(t2)>");
        });
    collectParts(current, tb); 
}

// ---- notin

void collect(current: (Expression) `<Expression lhs> notin <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("notin", current, [lhs, rhs], () { checkInType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

void checkInType(Expression current, AType t1, AType t2){

    if (isRelType(t2)) {
        et = getRelElementType(t2);
        if (!comparable(t1,et))
            reportError(current, "Cannot compare <fmt(t1)> with element type of <fmt(t2)>");
    } else if (isSetType(t2)) {
        et = getSetElementType(t2);
        if (!comparable(t1,et))
            reportError(current, "Cannot compare <fmt(t1)> with element type of <fmt(t2)>");
    } else if (isMapType(t2)) {
        et = getMapDomainType(t2);
        if (!comparable(t1,et))
            reportError(current, "Cannot compare <fmt(t1)> with domain type of <fmt(t2)>");
    } else if (isListRelType(t2)) {
        et = getListRelElementType(t2);
        if (!comparable(t1,et))
            reportError(current, "Cannot compare <fmt(t1)> with element type of <fmt(t2)>");
    } else if (isListType(t2)) {
        et = getListElementType(t2);
        if (!comparable(t1,et))
            reportError(current, "Cannot compare <fmt(t1)> with element type of <fmt(t2)>");
    } else {
        reportError(current, "in or notin not defined for <fmt(t1)> and <fmt(t2)>");
    }
}

// ---- in

void collect(current: (Expression) `<Expression lhs> in <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
    tb.require("in", current, [lhs, rhs], () { checkInType(current, getType(lhs), getType(rhs)); });
    collectParts(current, tb); 
}

// ---- comparisons >=, <=, <, >, ==, !=

void collect(current: (Expression) `<Expression lhs> \>= <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("\>=", current, tb);
    
void collect(current: (Expression) `<Expression lhs> \<= <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("\<=", current, tb);
    
void collect(current: (Expression) `<Expression lhs> \> <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("\>", current, tb);
    
void collect(current: (Expression) `<Expression lhs> \< <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("\<", current, tb);

void collect(current: (Expression) `<Expression lhs> == <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("==", current, tb);

void collect(current: (Expression) `<Expression lhs> != <Expression rhs>`, TBuilder tb)
    = checkComparisonOp("!=", current, tb);

void checkComparisonOp(str op, Expression current, TBuilder tb){
    tb.fact(current, abool());
    tb.require("comparison <fmt(op)>", current, [current.lhs, current.rhs],
        (){ if(!checkComparisonArgs(getType(current.lhs), getType(current.rhs))) 
               reportError(current, "Comparison <fmt(op)> not defined on <fmt(current.lhs)> and <fmt(current.rhs)>");
          });
    collectParts(current, tb);
}

default bool checkComparisonArgs(AType t1, AType t2){
     if(t1.label?) t1 = unset(t1, "label");
     if(t2.label?) t2 = unset(t2, "label");
     if(t1 == t2 || (subtype(t1, anum()) && subtype(t2, anum())))
        return true;
        
     if(t1 == avoid() || t2 == avoid())
        return false;
        
    if (isListRelType(t1) && isListRelType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return true;
    if (isListType(t1) && isListType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return true;
    if (isMapType(t1) && isMapType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return true;
    if (isRelType(t1) && isRelType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return true;
    if (isSetType(t1) && isSetType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return true;
    if (isTupleType(t1) && isTupleType(t2))
        return true;
        
    if (isValueType(t1) || isValueType(t2))
        return true;
    
    return false;  
}
        
// ---- ifDefined

void collect(current: (Expression) `<Expression e1> ? <Expression e2>`, TBuilder tb) {    
    tb.calculate("if defined", current, [e1, e2], AType(){ return lub(getType(e1), getType(e2)); });
    collectParts(current, tb);
}

// ---- noMatch

void collect(current: (Expression) `<Pattern pat> !:= <Expression expression>`, TBuilder tb){
    computeMatchPattern(current, pat, "!:=", expression, tb);
}
// ---- match

void collect(current: (Expression) `<Pattern pat> := <Expression expression>`, TBuilder tb){
    computeMatchPattern(current, pat, ":=", expression, tb);
}

void computeMatchPattern(Expression current, Pattern pat, str operator, Expression expression, TBuilder tb){
    tb.fact(current, abool());
    if(pat is  qualifiedName){
        name = pat.qualifiedName;
        tau = tb.newTypeVar();
        tb.define(unescape("<name>"), variableId(), name, defLub([expression], AType(){ return getType(expression); }));
        tb.requireEager("match (<operator>) variable `<name>`", current, [expression],
                () { etype = getType(expression);
                     unify(etype, getType(name), onError(pat, "Type of pattern could not be computed"));
                     comparable(etype, getType(name), onError(current, "Incompatible type in match (<operator>) using `<name>`")); 
                   });  
    } else {
        tb.requireEager("match", current, [expression],
           () {  ptype = getType(pat); etype = getType(expression);
                 if(isFullyInstantiated(ptype) && isFullyInstantiated(etype)){
                    comparable(ptype, etype, onError(current, "Cannot match (<operator>) <fmt(pat)> pattern with <fmt(expression)> subject"));
                 } else {
                    println("match: <ptype>, <etype>");
                    unify(ptype, etype, onError(pat, "Type of <operator> pattern could not be computed"));
                    ptype = instantiate(ptype);
                    etype = instantiate(etype);
                    println("ptype becomes: <ptype>");
                    println("etype becomes: <etype>");
                    // add comparable?
                    fact(pat, ptype);
                    fact(expression, etype);
                 }
                
               });
    }
    collectParts(current, tb);
}

// ---- enumerator

void collect(current: (Expression) `<Pattern pat> \<- <Expression expression>`, TBuilder tb){

    tb.fact(current, abool());
    
    if(pat is  qualifiedName){
        name = pat.qualifiedName;
        tau = tb.newTypeVar();
        tb.define(unescape("<name>"), variableId(), name, defLub([], AType(){ return getType(tau); }));
        
        tb.calculateEager("enumeration variable `<name>`", name, [expression],
           AType () { 
                     ptype = getType(expression); 
                      etype = getType(expression);
                     try {
                        //if(/*!isFullyInstantiated(ptype) ||*/ !isFullyInstantiated(etype)){
                        //   unify(ptype, etype, onError(pat, "Type of pattern could not be computed"));
                        //   ptype = instantiate(ptype);
                        //   etype = instantiate(etype);
                        //}   
                        elmType = computeEnumeratorElementType(current, etype);
                        unify(tau, elmType, onError(name, "XXX"));
                        return elmType;
                        //subtype(getType(name), elmType, onError(pat, "Type of pattern could not be computed"));
                     } catch checkFailed(set[str] msgs): {
                             reportErrors(current, msgs);
                     }
                  });  
    } else {
        tb.requireEager("enumeration", current, [expression],
            () { ptype = getType(pat); etype = getType(expression);
                 elmType = avalue();
                 elmType = computeEnumeratorElementType(current, etype);   
                 
                 if(!isFullyInstantiated(ptype)){
                    unify(ptype, elmType, onError(pat, "Type of pattern could not be computed"));
                 }      
                 subtype(instantiate(ptype), elmType, onError(pat, "Pattern of <fmt(getType(pat))> cannot be used to enumerate over <fmt(getType(expression))>"));
               });
    }
    collectParts(current, tb);
}

@doc{Check the types of Rascal expressions: Enumerator}
AType computeEnumeratorElementType(Expression current, AType etype) {
    // TODO: For concrete lists, what should we use as the type?
    // TODO: For nodes, ADTs, and tuples, would it be better to use the lub of all the possible types?

//println("computeEnumeratorElementType: <etype>");
     if(!isFullyInstantiated(etype)) throw TypeUnavailable();
     etype = instantiate(etype);
    
    if (isSetType(etype)) {
        return getSetElementType(etype);
    } else if (isListType(etype)) {
        return getListElementType(etype);
    } else if (isMapType(etype)) {
        return getMapDomainType(etype);
    } else if (isADTType(etype) || isTupleType(etype) || isNodeType(etype)) {
        return avalue();
    } else if (isNonTerminalIterType(etype)) {
        return getNonTerminalIterElement(etype);
    } else if (isNonTerminalOptType(etype)) {
        return getNonTerminalOptType(etype);
    } else {
        //println(etype);
        reportError(current, "Type <fmt(etype)> is not enumerable");
    }
}

// TODO scoping rules in Boolean operators!
// ---- implication

void collect(current: (Expression) `<Expression lhs> ==\> <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
   
    tb.requireEager("implication", current, [lhs, rhs],
        (){ if(!unify(abool(), getType(lhs))) reportError(lhs, "Argument of ==\> should be `bool`, found <fmt(lhs)>");
            if(!unify(abool(), getType(rhs))) reportError(rhs, "Argument of ==\> should be `bool`, found <fmt(rhs)>");
          });
    collectParts(current, tb);
}

// ---- equivalence

void collect(current: (Expression) `<Expression lhs> \<==\> <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
   
    tb.requireEager("equivalence", current, [lhs, rhs],
        (){ if(!unify(abool(), getType(lhs))) reportError(lhs, "Argument of \<==\> should be `bool`, found <fmt(lhs)>");
            if(!unify(abool(), getType(rhs))) reportError(rhs, "Argument of \<==\> should be `bool`, found <fmt(rhs)>");
          });
    collectParts(current, tb);
}

// ---- and

void collect(current: (Expression) `<Expression lhs> && <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
   
    tb.requireEager("and", current, [lhs, rhs],
        (){ if(!unify(abool(), getType(lhs))) reportError(lhs, "Argument of && should be `bool`, found <fmt(lhs)>");
            if(!unify(abool(), getType(rhs))) reportError(rhs, "Argument of && should be `bool`, found <fmt(rhs)>");
          });
    collectParts(current, tb);
}

// ---- or

void collect(current: (Expression) `<Expression lhs> || <Expression rhs>`, TBuilder tb){
    tb.fact(current, abool());
      
    tb.requireEager("or", current, [lhs, rhs],
        (){ if(!unify(abool(), getType(lhs))) reportError(lhs, "Argument of || should be `bool`, found <fmt(lhs)>");
            if(!unify(abool(), getType(rhs))) reportError(rhs, "Argument of || should be `bool`, found <fmt(rhs)>");
            // TODO: check that lhs and rhs introduce the same set of variables
          });
    collectParts(current, tb);
}

// ---- if expression

void collect(current: (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`,  TBuilder tb){
    tb.enterScope(condition);   // thenExp may refer to variables defined in conditions; elseExp may not
        storeExcludeUse(condition, elseExp, tb);            // variable occurrences in elseExp may not refer to variables defined in condition
        
        tb.calculateEager("if expression", current, [condition, thenExp, thenExp],
            AType (){
                checkConditions([condition]);
                return lub(getType(thenExp), getType(thenExp));
            });
        collectParts(current, tb);
    tb.leaveScope(condition); 
}
