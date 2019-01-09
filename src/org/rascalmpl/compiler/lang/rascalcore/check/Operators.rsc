@bootstrapParser
module lang::rascalcore::check::Operators

extend analysis::typepal::TypePal;
 
extend lang::rascalcore::check::AType;

extend lang::rascalcore::check::ConvertType;
extend lang::rascalcore::check::Expression;
extend lang::rascalcore::check::Pattern;
extend lang::rascalcore::check::Statement;

import lang::rascalcore::check::ATypeExceptions;
import lang::rascalcore::check::ATypeInstantiation;
import lang::rascalcore::check::ATypeUtils;
import lang::rascalcore::check::TypePalConfig;

import lang::rascal::\syntax::Rascal;
import Set;
import Node;

AType unaryOp(str op, AType(Tree, AType, Solver) computeType, Tree current, AType t1, Solver s){
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
    
    return computeType(current, t1, s);
}

AType binaryOp(str op, AType(Tree, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, Solver s){
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
    return computeType(current, t1, t2, s);
}

AType ternaryOp(str op, AType(Tree, AType, AType, AType, Solver) computeType, Tree current, AType t1, AType t2, AType t3, Solver s){
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
    return computeType(current, t1, t2, t3, s);
}

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

// ---- is

void collect(current: (Expression) `<Expression e> is <Name n>`, Collector c){
    scope = c.getScope();
    c.calculate("is", current, [e], AType(Solver s) { return unaryOp("is", _computeIsType, current, s.getType(e), s);  });
    collect(e, c); 
}

private AType _computeIsType(Tree current, AType t1, Solver s){               // TODO: check that name exists
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        for(<key, idrole, tp> <- overloads){
           try return _computeIsType(current, tp, s);
           catch checkFailed(_): /* ignore, try next */;
           catch NoBinding(): /* ignore, try next */;
        }
    } else if(isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) return abool();
    s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
}

// ---- has

void collect(current: (Expression) `<Expression e> has <Name n>`, Collector c){
    c.calculate("has", current, [e], AType(Solver s) { return unaryOp("has", _computeHasType, current, s.getType(e), s); });
    collect(e, c); 
} 

private AType _computeHasType(Tree current, AType t1, Solver s){
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        for(<key, idrole, tp> <- overloads){
           try return _computeHasType(current, tp, s);
           catch checkFailed(_): /* ignore, try next */;
           catch NoBinding(): /* ignore, try next */;
        }
    } else if (isRelType(t1) || isListRelType(t1) || isTupleType(t1) || isADTType(t1) || isNonTerminalType(t1) || isNodeType(t1)) return abool();
    
    s.report(error(current, "Invalid type: expected relation, tuple, node or ADT types, found %t", t1));
}

// ---- transitive closure
 
void collect(current: (Expression) `<Expression arg> +`, Collector c){
    c.calculate("transitive closure", current, [arg],
        AType(Solver s) { return unaryOp("transitive closure", _computeTransClosureType, current, s.getType(arg), s); });
    
    collect(arg, c); 
} 

private AType _computeTransClosureType(Tree current, AType t1, Solver s){
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
            s.report(error(current, "Invalid type: expected a binary relation over equivalent types, found %t", t1));
        }
    } else {
        s.report(error(current, "Invalid type: expected a binary relation, found %t", t1));
    }
}
// ---- reflexive transitive closure

void collect(current: (Expression) `<Expression arg> *`, Collector c){
    c.calculate("reflexive transitive closure", current, [arg],
        AType(Solver s) { return unaryOp("reflexive transitive closure", _computeTransClosureType, current, s.getType(arg), s); });
    
    collect(arg, c); 
} 

// ---- isDefined

bool isValidIsDefinedArg(Expression arg) // TODO maybe add call?
    = arg is subscript || arg is fieldAccess || arg is fieldProject || arg is getAnnotation || (arg is \bracket && isValidIsDefinedArg(arg.expression));

void checkIsDefinedArg(Expression arg, Collector c){
    if(!isValidIsDefinedArg(arg)){
        c.report(error(arg, "Only subscript, field access, field project or get annotation allowed"));
    }
}

void collect(current: (Expression) `<Expression arg> ?`, Collector c){
    c.fact(current, abool());
    checkIsDefinedArg(arg, c);
    collect(arg, c); 
}

// ---- negation

void collect(current: (Expression) `! <Expression arg>`, Collector c){
    c.calculate("negation", current, [arg],
       AType(Solver s){ return unaryOp("negation", computeNegation, current, s.getType(arg), s); });
    collect(arg, c); 
}

AType computeNegation(Tree current, AType t1, Solver s){    
    if(isBoolType(t1)) return abool();
    s.report(error(current, "Negation not defined on %t", t1));
}

// ---- negative

void collect(current: (Expression) `- <Expression arg>`, Collector c){
    c.calculate("negative", current, [arg],
       AType(Solver s){ return unaryOp("negative", computeNegative, current, s.getType(arg), s); });
    collect(arg, c); 
}

AType computeNegative(Tree current, AType t1, Solver s){    
    if(isNumericType(t1)) return t1;
    s.report(error(current, "Negative not defined on %t", t1));
}
// ---- splice

void collect(current: (Expression) `* <Expression arg>`, Collector c){
    c.calculate("splice", current, [arg], 
       AType(Solver s){ return unaryOp("splice", _computeSpliceType, current, s.getType(arg), s); });
    collect(arg, c); 
}

private AType _computeSpliceType(Tree current, AType t1, Solver s){    
    if (isListType(t1)) return getListElementType(t1);
    if (isSetType(t1)) return getSetElementType(t1);
    if (isBagType(t1)) return getBagElementType(t1);
    if (isRelType(t1)) return getRelElementType(t1);
    if (isListRelType(t1)) return getListRelElementType(t1);
    return t1;
}

// ---- asType

void collect(current: (Expression)`[ <Type t> ] <Expression e>`, Collector c){
    c.calculate("asType", current, [t, e],
        AType(Solver s) { 
            if(!(s.subtype(e, astr()) || s.subtype(e, aloc()))) s.report(error(e, "Expected `str` or `loc`, instead found %t", e));
            return s.getType(t);
        });
    collect(t, e, c);
}

// ---- composition

void collect(current: (Expression) `<Expression lhs> o <Expression rhs>`, Collector c){
    c.calculate("composition", current, [lhs, rhs],  
       AType(Solver s){ return binaryOp("composition", _computeCompositionType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeCompositionType(Tree current, AType t1, AType t2, Solver s){  

    // Special handling for list[void] and set[void], these should be treated as lrel[void,void]
    // and rel[void,void], respectively
    if (isListType(t1) && isVoidType(getListElementType(t1))) t1 = makeListRelType(makeVoidType(),makeVoidType());
    if (isListType(t2) && isVoidType(getListElementType(t2))) t2 = makeListRelType(makeVoidType(),makeVoidType());
    if (isSetType(t1) && isVoidType(getSetElementType(t1))) t1 = makeRelType(makeVoidType(),makeVoidType());
    if (isSetType(t2) && isVoidType(getSetElementType(t2))) t2 = makeRelType(makeVoidType(),makeVoidType());
    
    if (isMapType(t1) && isMapType(t2)) {
        if (asubtype(getMapRangeType(t1),getMapDomainType(t2))) {
            return makeMapType(getMapDomainType(t1),getMapRangeType(t2));
        } else {
            s.report(error(current, "%t must be a subtype of %t", getMapRangeType(t1), getMapDomainType(t2)));
        }
    }
    
    if (isRelType(t1) && isRelType(t2)) {
        list[AType] lflds = getRelFields(t1);
        list[AType] rflds = getRelFields(t2);
        failures = [];
        if (size(lflds) != 0 && size(lflds) != 2)
            failures += error(current, "Relation %t should have arity of 0 or 2", t1); 
        if (size(rflds) != 0 && size(rflds) != 2)
            failures += error(current, "Relation %t should have arity of 0 or 2", t2);
        if (!comparable(lflds[1],rflds[0]))
            failures += error(current, "Range of relation %t must be comparable to domain of relation %t", t1, t2);
        if (size(failures) > 0) {
            s.reports(failures);
        }
        if (size(lflds) == 0 || size(rflds) == 0)
            return arel(atypeList([]));
        else {
            return arel(atypeList([lflds[0],rflds[1]])); 
         }
    }

    if (isListRelType(t1) && isListRelType(t2)) {
        list[AType] lflds = getListRelFields(t1);
        list[AType] rflds = getListRelFields(t2);
        list[FailMessage] failures = [];
        if (size(lflds) != 0 && size(lflds) != 2)
            failures += error(current, "List relation %t should have arity of 0 or 2", t1); 
        if (size(rflds) != 0 && size(rflds) != 2)
            failures += error(current, "List relation %t should have arity of 0 or 2", t2);
        if (!comparable(lflds[1],rflds[0]))
            failures += error(current, "Range of list relation %t must be comparable to domain of list relation %t", t1, t2);
        if (size(failures) > 0) {
            s.reports(failures);
        }
        if (size(lflds) == 0 || size(rflds) == 0)
            return alrel(atypeList([]));
        else {
            return alrel(atypeList([lflds[0], rflds[1]])); 
        }
    }
    
    if (isFunctionType(t1) && isFunctionType(t2)) {
        compositeArgs = getFunctionArgumentTypes(t2);
        compositeRet = getFunctionReturnType(t1);
        linkingArgs = getFunctionArgumentTypes(t1);
        
        // For f o g, f should have exactly one formal parameter
        if (size(linkingArgs) != 1) {
            s.report(error(current, "In a composition of two functions the leftmost function must have exactly one formal parameter."));
        }
        
        // and, that parameter must be of a type that a call with the return type of g would succeed
        linkingArg = linkingArgs[0];
        rightReturn = getFunctionReturnType(t2);
        s.requireSubType(rightReturn, linkingArg, error(current, "The return type of the right-hand function, %t, cannot be passed to the left-hand function, which expects type %t", rightReturn, linkingArg));          
        
        // If both of those pass, the result type is a function with the args of t2 and the return type of t1
        rt = afunc(compositeRet, compositeArgs,[]);
        return return rt;         
    }

   s.report(error(current, "Composition not defined for %t and %t", t1, t2));
}

// ---- product

void collect(current: (Expression) `<Expression lhs> * <Expression rhs>`, Collector c){
    c.calculate("product", current, [lhs, rhs],  
       AType(Solver s){ return computeProductType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
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
}

// ---- join

void collect(current: (Expression) `<Expression lhs> join <Expression rhs>`, Collector c){
    c.calculate("join", current, [lhs, rhs], 
       AType(Solver s){ return binaryOp("join", _computeJoinType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeJoinType(Tree current, AType t1, AType t2, Solver s){ 
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
        return alrel( atypeList(getListElementType(t1) + getListRelFields(t2)) );
    
    if (isListType(t1) && isListType(t2))
        return alrel( atypeList([getListElementType(t1), getListElementType(t2)])) ;
    
    if (isSetType(t1) && isSetType(t2))
        return arel( atypeList([getSetElementType(t1), getSetElementType(t2)]) );
    
    s.report(error(current, "Join not defined for %t and %t", t1, t2));
} 

// ---- remainder

void collect(current: (Expression) `<Expression lhs> % <Expression rhs>`, Collector c){
    c.calculate("remainder", current, [lhs, rhs],
        AType(Solver s){ return binaryOp("remainder", _computeRemainderType, current, s.getType(lhs), s.getType(rhs), s);
                //t1 = getType(lhs); t2 = getType(rhs);
                // if(isIntType(t1) && isIntType(t2)) return lub(t1, t2);
                // report(error(current, "Remainder not defined on <fmt(t1)> and <fmt(t2)>");
        });
    collect(lhs, rhs, c); 
}

private AType _computeRemainderType(Tree current, AType t1, AType t2, Solver s){
    if(isIntType(t1) && isIntType(t2)) return aint();
    s.report(error(current, "Remainder not defined on %t and %t", t1, t2));
}

// ---- division

void collect(current: (Expression) `<Expression lhs> / <Expression rhs>`, Collector c){
    c.calculate("division", current, [lhs, rhs], 
       AType(Solver s){ return computeDivisionType(current, s.getType(lhs), s.getType(rhs), s);  });
    collect(lhs, rhs, c); 
}

AType computeDivisionType(Tree current, AType t1, AType t2, Solver s)
    = binaryOp("division", _computeDivisionType, current, t1, t2, s);

private AType _computeDivisionType(Tree current, AType t1, AType t2, Solver s){
    if(isNumericType(t1) && isNumericType(t2)) return numericArithTypes(t1, t2);
    s.report(error(current, "Division not defined on %t and %t", t1, t2));
}

// ---- intersection

void collect(current: (Expression) `<Expression lhs> & <Expression rhs>`, Collector c){
    c.calculate("intersection", current, [lhs, rhs], 
       AType(Solver s) { return computeIntersectionType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c);
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
}

// ---- addition

void collect(current: (Expression) `<Expression lhs> + <Expression rhs>`, Collector c){
    c.calculate("addition", current, [lhs, rhs], 
        AType(Solver s) { return computeAdditionType(current, s.getType(lhs), s.getType(rhs), s);  });
    collect(lhs, rhs, c); 
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
}

// ---- subtraction

void collect(current: (Expression) `<Expression lhs> - <Expression rhs>`, Collector c){
    c.calculate("subtraction", current, [lhs, rhs], 
       AType(Solver s) { return computeSubtractionType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
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
}

// ---- appendAfter

void collect(current: (Expression) `<Expression lhs> \<\< <Expression rhs>`, Collector c){
    c.calculate("append after", current, [lhs, rhs],
        AType(Solver s){ return binaryOp("append after", _computeAppendAfterType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeAppendAfterType(Tree current, AType t1, AType t2, Solver s) { 
    if (isListType(t1)) {
       return makeListType(s.lub(getListElementType(t1),t2));
    }
    s.report(error(current, "Expected a list type, not type %t", t1));
}

// ---- insertBefore

void collect(current: (Expression) `<Expression lhs> \>\> <Expression rhs>`, Collector c){
    c.calculate("insert before", current, [lhs, rhs],
       AType(Solver s){ return binaryOp("insert before", _computeInsertBeforeType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeInsertBeforeType(Tree current, AType t1, AType t2, Solver s) { 
    if (isListType(t2)) {
        return makeListType(s.lub(getListElementType(t2),t1));
    }
    s.report(error(current, "Expected a list type, not type %t", t2));
}

// ---- modulo

void collect(current: (Expression) `<Expression lhs> mod <Expression rhs>`, Collector c){
    c.calculate("modulo", current, [lhs, rhs],
       AType(Solver s){ return binaryOp("modulo", _computeModuloType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeModuloType(Tree current, AType t1, AType t2, Solver s) { 
    if (isIntType(t1) && isIntType(t2)) {
        return aint();
    }
    s.report(error(current, "Modulo not defined on %t and %t", t1, t2));
}

// ---- notin

void collect(current: (Expression) `<Expression lhs> notin <Expression rhs>`, Collector c){
    c.calculate("notin", current, [lhs, rhs], 
       AType(Solver s) { return binaryOp("notin", _computeInType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType _computeInType(Tree current, AType t1, AType t2, Solver s){
    if (isRelType(t2)) {
        et = getRelElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isSetType(t2)) {
        et = getSetElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isMapType(t2)) {
        et = getMapDomainType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with domain type of %t", t1, t2));
        return abool();
    } else if (isListRelType(t2)) {
        et = getListRelElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isListType(t2)) {
        et = getListElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else {
        s.report(error(current, "`in` or `notin` not defined for %t and %t", t1, t2));
    }
}

// ---- in

void collect(current: (Expression) `<Expression lhs> in <Expression rhs>`, Collector c){
    c.calculate("in", current, [lhs, rhs], 
       AType(Solver s) { return binaryOp("in", _computeInType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

// ---- comparisons >=, <=, <, >, ==, !=

void collect(current: (Expression) `<Expression lhs> \>= <Expression rhs>`, Collector c)
    = checkComparisonOp("\>=", current, c);
    
void collect(current: (Expression) `<Expression lhs> \<= <Expression rhs>`, Collector c)
    = checkComparisonOp("\<=", current, c);
    
void collect(current: (Expression) `<Expression lhs> \> <Expression rhs>`, Collector c)
    = checkComparisonOp("\>", current, c);
    
void collect(current: (Expression) `<Expression lhs> \< <Expression rhs>`, Collector c)
    = checkComparisonOp("\<", current, c);

void collect(current: (Expression) `<Expression lhs> == <Expression rhs>`, Collector c)
    = checkComparisonOp("==", current, c);

void collect(current: (Expression) `<Expression lhs> != <Expression rhs>`, Collector c)
    = checkComparisonOp("!=", current, c);

void checkComparisonOp(str op, Expression current, Collector c){
    c.require("comparison `<op>`", current, [current.lhs, current.rhs],
       void(Solver s){ binaryOp(op, _computeComparisonType, current, s.getType(current.lhs), s.getType(current.rhs), s); });
    c.fact(current, abool());
    collect([current.lhs, current.rhs], c);
}

private AType _computeComparisonType(Tree current, AType t1, AType t2, Solver s){
    if(t1.label?) t1 = unset(t1, "label");      // TODO: do this for all operators?
    if(t2.label?) t2 = unset(t2, "label");
    if(comparable(t1, t2) || (isNumericType(t1) && isNumericType(t2)))
       return abool();
        
    if(t1 == avoid() || t2 == avoid())
       s.report(error(current, "Comparison not defined on %t and %t", t1, t2));
        
    if (isListRelType(t1) && isListRelType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return abool();
    if (isListType(t1) && isListType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return abool();
    if (isMapType(t1) && isMapType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return abool();
    if (isRelType(t1) && isRelType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return abool();
    if (isSetType(t1) && isSetType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return abool();
    if (isTupleType(t1) && isTupleType(t2))
        return abool();
        
    if (isValueType(t1) || isValueType(t2))
        return abool();
    
    s.report(error(current, "Comparison not defined on %t and %t", t1, t2));
}
    
// ---- ifDefined

void collect(current: (Expression) `<Expression e1> ? <Expression e2>`, Collector c) { 
    checkIsDefinedArg(e1, c);   
    c.calculate("if defined", current, [e1, e2], AType(Solver s){ return s.lub(s.getType(e1), s.getType(e2)); });
    collect(e1, e2, c);
}

// ---- noMatch

void collect(current: (Expression) `<Pattern pat> !:= <Expression expression>`, Collector c){
    computeMatchPattern(current, pat, "!:=", expression, c);
}
// ---- match

void collect(current: (Expression) `<Pattern pat> := <Expression expression>`, Collector c){
    computeMatchPattern(current, pat, ":=", expression, c);
}

void computeMatchPattern(Expression current, Pattern pat, str operator, Expression expression, Collector c){
    scope = c.getScope();
    c.calculateEager("match", current, [expression],
        AType(Solver s) {
            subjectType = s.getType(expression);
            if(isStartNonTerminalType(subjectType)){
                subjectType = getStartNonTerminalType(subjectType);
            }
            patType = getPatternType(pat, subjectType, scope, s);
            s.instantiate(subjectType);
            if(!s.isFullyInstantiated(patType) || !s.isFullyInstantiated(subjectType)){
                s.requireUnify(patType, subjectType, error(pat, "Type of pattern could not be computed"));
                ipatType = s.instantiate(patType);
                if(tvar(src) := patType) s.fact(src, ipatType);
                patType = ipatType;
                isubjectType = s.instantiate(subjectType);
                //if(tvar(src) := subjectType) fact(src, isubjectType);
                subjectType = isubjectType;
                //s.keepBindings(getLoc(pat)); // <===
            }
            s.requireComparable(patType, subjectType, error(current, "Pattern should be comparable with %t, found %t", subjectType, patType));
            return abool();
        });
    c.push(patternContainer, "match");
    collect(pat, c);
    c.pop(patternContainer);
    collect(expression, c);
}

// ---- enumerator

void collect(current: (Expression) `<Pattern pat> \<- <Expression expression>`, Collector c){
    scope = c.getScope();
    c.calculateEager("enumeration", current, [expression],
       AType(Solver s) { 
             exprType = s.getType(expression);
             elmType = avalue();
             elmType = computeEnumeratorElementType(current, exprType, s); 
             patType = getPatternType(pat, elmType, scope, s);   
             
             if(!s.isFullyInstantiated(patType) || !s.isFullyInstantiated(elmType)){
                s.requireUnify(patType, elmType, error(pat, "Type of pattern could not be computed"));
                ipatType = s.instantiate(patType);
                if(tvar(src) := patType) s.fact(src, ipatType);
                patType = ipatType;
                ielmType = s.instantiate(elmType);
                if(tvar(src) := elmType) s.fact(src, ielmType);
                elmType = ielmType;
                //clearBindings(); // <===
             }  else {
                    s.fact(pat, patType);
             }
             if(overloadedAType(rel[loc, IdRole, AType] overloads) := elmType){
                for(<key, role, tp> <- overloads){
                    if(comparable(patType, tp)) return abool();
                }
                s.report(error(pat, "Pattern of type %t cannot be used to enumerate over %t", patType, exprType));
            } 
            s.requireComparable(patType, elmType, error(pat, "Pattern of type %t cannot be used to enumerate over %t", patType, exprType));
            return abool();
           });
    collect(pat, expression, c);
}

@doc{Check the types of Rascal expressions: Enumerator}
AType computeEnumeratorElementType(Expression current, AType etype, Solver s) {
    // TODO: For concrete lists, what should we use as the type?
    // TODO: For nodes, ADTs, and tuples, would it be better to use the lub of all the possible types?

//println("computeEnumeratorElementType: <etype>");
     if(!s.isFullyInstantiated(etype)) throw TypeUnavailable();
     
     etype = s.instantiate(etype);
     
     if(overloadedAType(rel[loc, IdRole, AType] overloads) := etype){
        filtered_overloads = {};
        for(<key, role, tp> <- overloads){
            try {
                filtered_overloads += <key, role, computeEnumeratorElementType(current, tp, s)>;
            } catch checkFailed(_): /* ignore, try next */;
              catch NoBinding(): /* ignore, try next */;
  //>>        catch e: /* ignore, try next */;
        }
        if(!isEmpty(filtered_overloads)) return overloadedAType(filtered_overloads);
        s.report(error(current, "Type %t is not enumerable", etype));
      }
    
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
    } else if(overloadedAType(rel[loc, IdRole, AType] overloads) := etype){
        for(<key, role, tp> <- overloads, isEnumeratorType(tp)){
            try {
                return computeEnumeratorElementType(current, tp, s);
            } catch checkFailed(list[FailMessage] fms): /* do nothing and try next overload*/;
              catch NoBinding():  /* do nothing and try next overload*/;
        }
    } 
    s.report(error(current, "Type %t is not enumerable", etype));
}

// TODO scoping rules in Boolean operators!
// ---- implication

void collect(current: (Expression) `<Expression lhs> ==\> <Expression rhs>`, Collector c){
    c.fact(current, abool());
   
    c.requireEager("implication", current, [lhs, rhs],
        void (Solver s){ s.requireUnify(abool(), s.getType(lhs), error(lhs, "Argument of ==\> should be `bool`, found %t", lhs));
            //clearBindings();
            s.requireUnify(abool(), rhs, error(rhs, "Argument of ==\> should be `bool`, found %t", rhs));
            //clearBindings();
          });
    collect(lhs, rhs, c);
}

// ---- equivalence

void collect(current: (Expression) `<Expression lhs> \<==\> <Expression rhs>`, Collector c){
    //c.fact(current, abool());
   
    c.calculateEager("equivalence", current, [lhs, rhs],
        AType(Solver s){ s.requireUnify(abool(), lhs, error(lhs, "Argument of \<==\> should be `bool`, found %t", lhs));
                  //clearBindings();
                  s.requireUnify(abool(), rhs, error(rhs, "Argument of \<==\> should be `bool`, found %t", rhs));
                  //clearBindings();
                  return abool();
                });
    collect(lhs, rhs, c);
}

// ---- and

void collect(current: (Expression) `<Expression lhs> && <Expression rhs>`, Collector c){
    c.fact(current, abool());
   
    c.requireEager("and", current, [lhs, rhs],
        void (Solver s){ 
            s.requireUnify(abool(), lhs, error(lhs, "Argument of && should be `bool`, found %t", lhs));
            //clearBindings();
            s.requireUnify(abool(), rhs, error(rhs, "Argument of && should be `bool`, found %t", rhs));
            //clearBindings();
          });
    collect(lhs, rhs, c);
}

// ---- or

void collect(current: (Expression) `<Expression lhs> || <Expression rhs>`, Collector c){
    c.fact(current, abool());
      
    c.requireEager("or", current, [lhs, rhs],
        void (Solver s){ 
            s.requireUnify(abool(), lhs, error(lhs, "Argument of || should be `bool`, found %t", lhs));
            s.requireUnify(abool(), rhs, error(rhs, "Argument of || should be `bool`, found %t", rhs));
          });
          
    // Check that the names introduced in lhs and rhs are the same    
    
    namesBeforeOr = c.getStack(patternNames);
    collect(lhs, c);
    namesAfterLhs = c.getStack(patternNames);
    
    // Restore patternNames
    c.clearStack(patternNames);
    for(nm <- reverse(namesBeforeOr)) c.push(patternNames, nm);
    
    // Trick 1: wrap rhs in a separate scope to avoid double declarations with names introduced in lhs
    // Trick 2: use "current" as scope (to avoid clash with scope created by rhs)
    c.enterScope(lhs);
        collect(rhs, c);
    c.leaveScope(lhs);
    namesAfterRhs = c.getStack(patternNames);
  
    missingInLhs = namesAfterRhs - namesAfterLhs;
    missingInRhs = namesAfterLhs - namesAfterRhs;
    //if(!isEmpty(missingInLhs)) c.report(error(lhs, "Left argument of `||` should also introduce <fmt(missingInLhs)>");
    //if(!isEmpty(missingInRhs)) c.report(error(rhs, "Right argument of `||` should also introduce <fmt(missingInRhs)>");
}

// ---- if expression

void collect(current: (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`, Collector c){
    c.enterScope(condition);   // thenExp may refer to variables defined in conditions; elseExp may not
        storeExcludeUse(condition, elseExp, c);            // variable occurrences in elseExp may not refer to variables defined in condition
        
        c.calculate("if expression", current, [condition, thenExp, elseExp],
            AType(Solver s){
                s.requireComparable(abool(), condition, error(condition, "Condition should be `bool`, found %t", condition));
                //clearBindings();
                //checkConditions([condition]);
                return s.lub(thenExp, elseExp);
            });
        beginPatternScope("conditions", c);
        collect(condition, c);
        endPatternScope(c);
        collect(thenExp, elseExp, c);
    c.leaveScope(condition); 
}
