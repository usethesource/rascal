@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::CollectOperators
 
extend lang::rascalcore::check::CheckerCommon;
import lang::rascalcore::check::BacktrackFree;

import lang::rascalcore::check::CollectPattern;
import lang::rascalcore::check::CollectExpression;

import lang::rascal::\syntax::Rascal;

import Node;
import Set;

// ---- is

void collect(current: (Expression) `<Expression e> is <Name n>`, Collector c){
    scope = c.getScope();
    c.calculate("is", current, [e], AType(Solver s) { return unaryOp("is", do_computeIsType, current, s.getType(e), s);  });
    c.use(n, {constructorId()});
    collect(e, c); 
}

private AType do_computeIsType(Tree current, AType t1, Solver s){               // TODO: check that name exists
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        for(<_, _, tp> <- overloads){
           try return do_computeIsType(current, tp, s);
           catch checkFailed(_): /* ignore, try next */;
           catch NoBinding(): /* ignore, try next */;
        }
    } else if(isNodeAType(t1) || isADTAType(t1) || isSyntaxType(t1)) return abool();
    s.report(error(current, "Invalid type: expected node, ADT, or concrete syntax types, found %t", t1));
    return avalue();
}

// ---- has

void collect(current: (Expression) `<Expression e> has <Name n>`, Collector c){
    c.calculate("has", current, [e], AType(Solver s) { return unaryOp("has", _computeHasType, current, s.getType(e), s); });
    collect(e, c); 
} 

private AType _computeHasType(Tree current, AType t1, Solver s){
    if(overloadedAType(rel[loc, IdRole, AType] overloads) := t1){
        for(<_, _, tp> <- overloads){
           try return _computeHasType(current, tp, s);
           catch checkFailed(_): /* ignore, try next */;
           catch NoBinding(): /* ignore, try next */;
        }
    } else if (isRelAType(t1) || isListRelAType(t1) || isTupleAType(t1) || isADTAType(t1) || isSyntaxType(t1) || isNodeAType(t1)) return abool();
    
    s.report(error(current, "Invalid type: expected relation, tuple, node or ADT types, found %t", t1));
    return avalue();
}

// ---- transitive closure
 
void collect(current: (Expression) `<Expression arg> +`, Collector c){
    c.calculate("transitive closure", current, [arg],
        AType(Solver s) { return unaryOp("transitive closure", _computeTransClosureType, current, s.getType(arg), s); });
    
    collect(arg, c); 
} 

private AType _computeTransClosureType(Tree current, AType t1, Solver s){
    // Special case: if we have list[void] or set[void], these become lrel[void,void] and rel[void,void]
    if (isListAType(t1) && isVoidAType(getListElementType(t1)))
        return makeListRelType([makeVoidType(),makeVoidType()]);
    if (isSetAType(t1) && isVoidAType(getSetElementType(t1)))
        return makeRelType([makeVoidType(),makeVoidType()]);
        
    // Normal case: we have an actual list or relation
    if (isRelAType(t1) || isListRelAType(t1)) {
        list[AType] flds = isRelAType(t1) ? getRelFields(t1) : getListRelFields(t1);
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
    return avalue();
}
// ---- reflexive transitive closure

void collect(current: (Expression) `<Expression arg> *`, Collector c){
    c.calculate("reflexive transitive closure", current, [arg],
        AType(Solver s) { return unaryOp("reflexive transitive closure", _computeTransClosureType, current, s.getType(arg), s); });
    
    collect(arg, c); 
} 

// ---- isDefined

// syntactic check on argument
bool isValidIsDefinedArg(Expression arg)
    =  arg is subscript 
    || arg is fieldAccess 
    || arg is fieldProject 
    || arg is getAnnotation // TODO remove when annotations are gone
//    || arg is callOrTree
    || arg is qualifiedName
    || (arg is \bracket && isValidIsDefinedArg(arg.expression));

void checkIsDefinedArg(Expression arg, Collector c){
    if(arg is qualifiedName){
        scope = c.getScope();
        c.require("is defined on keyword parameter", arg, [],
            void(Solver s){
                if(isEmpty(s.getDefinitions("<arg>", scope, {keywordFormalId()}))){
                    s.report(error(arg,"Is defined operator `?` can only be applied to a keyword parameter"));
                }
            });
    
    } else if(!isValidIsDefinedArg(arg)){
        c.report(error(arg, "Is defined operator `?` can only be applied to subscript, keyword parameter, field access, field project or get annotation"));
    }
  
}

void collect(current: (Expression) `<Expression arg> ?`, Collector c){
    c.fact(current, abool());
    checkIsDefinedArg(arg, c);
    collect(arg, c); 
    c.require("non void", arg, [], makeNonVoidRequirement(arg, "Argument of is-defined operator _ ?)"));
}

// ---- negation

void collect(current: (Expression) `! <Expression arg>`, Collector c){
    c.calculate("negation", current, [arg],
       AType(Solver s){ return unaryOp("negation", computeNegation, current, s.getType(arg), s); });
    collect(arg, c); 
}

AType computeNegation(Tree current, AType t1, Solver s){    
    if(!isBoolAType(t1)){
        s.report(error(current, "Negation not defined on %t", t1));
    }
    return abool();
}

// ---- negative

void collect(current: (Expression) `- <Expression arg>`, Collector c){
    c.calculate("negative", current, [arg],
       AType(Solver s){ return unaryOp("negative", computeNegative, current, s.getType(arg), s); });
    collect(arg, c); 
}

AType computeNegative(Tree current, AType t1, Solver s){    
    if(!isNumericType(t1)){
        s.report(error(current, "Negative not defined on %t", t1));
    }
    return t1;
}
// ---- splice

void collect(current: (Expression) `* <Expression arg>`, Collector c){
    c.calculate("splice", current, [arg], 
       AType(Solver s){ return unaryOp("splice", do_computeSpliceType, current, s.getType(arg), s, maybeVoid=true); });
    collect(arg, c); 
}

private AType do_computeSpliceType(Tree current, AType t1, Solver s){    
    if (isListAType(t1)) return getListElementType(t1);
    if (isSetAType(t1)) return getSetElementType(t1);
    if (isBagAType(t1)) return getBagElementType(t1);
    if (isRelAType(t1)) return getRelElementType(t1);
    if (isListRelAType(t1)) return getListRelElementType(t1);
    s.report(error(current, "Splice not defined on expression of type %t", t1));
    return avalue();
}

// ---- asType

void collect(current: (Expression)`[ <Type t> ] <Expression e>`, Collector c){
    c.calculate("asType", current, [t, e],
        AType(Solver s) { 
            if(!(s.subtype(e, astr()) || s.subtype(e, aloc()))) s.report(error(e, "Expected `str` or `loc`, instead found %t", e));
            return s.getType(t);
        });
        
    checkSupportedByParserGenerator(t, c);
    collect(t, e, c);
}

// ---- composition

void collect(current: (Expression) `<Expression lhs> o <Expression rhs>`, Collector c){
    c.calculate("composition", current, [lhs, rhs],  
       AType(Solver s){ 
            lhsType = s.getType(lhs);
            rhsType = s.getType(rhs);
            if(isOverloadedAType(lhsType) && isOverloadedAType(rhsType)){
                return do_computeCompositionType(current, lhsType, rhsType, s);
            }
            return binaryOp("composition", do_computeCompositionType, current, lhsType, rhsType, s); 
       });
    collect(lhs, rhs, c); 
}

private AType do_computeCompositionType(Tree current, AType t1, AType t2, Solver s){  

    // Special handling for list[void] and set[void], these should be treated as lrel[void,void]
    // and rel[void,void], respectively
    if (isListAType(t1) && isVoidAType(getListElementType(t1))) t1 = makeListRelType(makeVoidType(),makeVoidType());
    if (isListAType(t2) && isVoidAType(getListElementType(t2))) t2 = makeListRelType(makeVoidType(),makeVoidType());
    if (isSetAType(t1) && isVoidAType(getSetElementType(t1))) t1 = makeRelType(makeVoidType(),makeVoidType());
    if (isSetAType(t2) && isVoidAType(getSetElementType(t2))) t2 = makeRelType(makeVoidType(),makeVoidType());
    
    if (isMapAType(t1) && isMapAType(t2)) {
        if (asubtype(getMapRangeType(t1),getMapDomainType(t2))) {
            return makeMapType(getMapDomainType(t1),getMapRangeType(t2));
        } else {
            s.report(error(current, "%t must be a subtype of %t", getMapRangeType(t1), getMapDomainType(t2)));
        }
    }
    
    if (isRelAType(t1) && isRelAType(t2)) {
        list[AType] lflds = getRelFields(t1);
        list[AType] rflds = getRelFields(t2);
        if (size(lflds) == 0 || size(rflds) == 0)
            return arel(atypeList([]));
        else {
            s.requireComparable(lflds[-1], rflds[0], error(current, "Type of last element of relation %t must be comparable to type of first element of relation %t", t1, t2));
            return arel(atypeList([*lflds[..-1],*rflds[1..]])); 
         }
    }

    if (isListRelAType(t1) && isListRelAType(t2)) {
        list[AType] lflds = getListRelFields(t1);
        list[AType] rflds = getListRelFields(t2);    
        
        if (size(lflds) == 0 || size(rflds) == 0)
            return alrel(atypeList([]));
        else {
            s.requireComparable(lflds[-1], rflds[0], error(current, "Type of last element of listrelation %t must be comparable to type of first element of listrelation %t", t1, t2));
            return alrel(atypeList([*lflds[..-1], *rflds[1..]])); 
        }
    }
    
    if ((isFunctionAType(t1) || isOverloadedAType(t1)) && (isFunctionAType(t2) || isOverloadedAType(t2))) {
        compositeArgs = getFunctionOrConstructorArgumentTypes(t2);
        compositeRet = getFunctionReturnType(t1);
        linkingArgs = getFunctionOrConstructorArgumentTypes(t1);
        
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
        return rt;         
    }

   s.report(error(current, "Composition not defined on %t and %t", t1, t2));
   return avalue();
}

// ---- product

void collect(current: (Expression) `<Expression lhs> * <Expression rhs>`, Collector c){
    c.calculate("product", current, [lhs, rhs],  
       AType(Solver s){ 
            return computeProductType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

// ---- join

void collect(current: (Expression) `<Expression lhs> join <Expression rhs>`, Collector c){
    c.calculate("join", current, [lhs, rhs], 
       AType(Solver s){ return binaryOp("join", do_computeJoinType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType do_computeJoinType(Tree current, AType t1, AType t2, Solver s){ 
    if ((isRelAType(t1) && isRelAType(t2)) || (isListRelAType(t1) && isListRelAType(t2))) {
       bool isRel = isRelAType(t1);
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

    if (isRelAType(t1) && isSetAType(t2))
        return arel( atypeList(getRelFields(t1) + getSetElementType(t2)) );
    
    if (isSetAType(t1) && isRelAType(t2))
        return arel( atypeList(getSetElementType(t1) + getRelFields(t2)) );
    
    if (isListRelAType(t1) && isListAType(t2))
        return alrel( atypeList(getListRelFields(t1) + getListElementType(t2)) );
    
    if (isListAType(t1) && isListRelAType(t2))
        return alrel( atypeList(getListElementType(t1) + getListRelFields(t2)) );
    
    if (isListAType(t1) && isListAType(t2))
        return alrel( atypeList([getListElementType(t1), getListElementType(t2)])) ;
    
    if (isSetAType(t1) && isSetAType(t2))
        return arel( atypeList([getSetElementType(t1), getSetElementType(t2)]) );
    
    s.report(error(current, "Join not defined on %t and %t", t1, t2));
    return avalue();
} 

// ---- remainder

void collect(current: (Expression) `<Expression lhs> % <Expression rhs>`, Collector c){
    c.calculate("remainder", current, [lhs, rhs],
        AType(Solver s){ return binaryOp("remainder", do_computeRemainderType, current, s.getType(lhs), s.getType(rhs), s);
                //t1 = getType(lhs); t2 = getType(rhs);
                // if(isIntAType(t1) && isIntAType(t2)) return lub(t1, t2);
                // report(error(current, "Remainder not defined on <fmt(t1)> and <fmt(t2)>");
        });
    collect(lhs, rhs, c); 
}

private AType do_computeRemainderType(Tree current, AType t1, AType t2, Solver s){
    if(!(isIntAType(t1) && isIntAType(t2))){
        s.report(error(current, "Remainder not defined on %t and %t", t1, t2));
    }
    return aint();
}

// ---- division

void collect(current: (Expression) `<Expression lhs> / <Expression rhs>`, Collector c){
    c.calculate("division", current, [lhs, rhs], 
       AType(Solver s){ return computeDivisionType(current, s.getType(lhs), s.getType(rhs), s);  });
    collect(lhs, rhs, c); 
}

// ---- intersection

void collect(current: (Expression) `<Expression lhs> & <Expression rhs>`, Collector c){
    c.calculate("intersection", current, [lhs, rhs], 
       AType(Solver s) { return computeIntersectionType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c);
}

// ---- addition

void collect(current: (Expression) `<Expression lhs> + <Expression rhs>`, Collector c){
    c.calculate("addition", current, [lhs, rhs], 
        AType(Solver s) { return computeAdditionType(current, s.getType(lhs), s.getType(rhs), s);  });
    collect(lhs, rhs, c); 
}

// ---- subtraction

void collect(current: (Expression) `<Expression lhs> - <Expression rhs>`, Collector c){
    c.calculate("subtraction", current, [lhs, rhs], 
       AType(Solver s) { return computeSubtractionType(current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

// ---- appendAfter

void collect(current: (Expression) `<Expression lhs> \<\< <Expression rhs>`, Collector c){
    c.calculate("append after", current, [lhs, rhs],
        AType(Solver s){ return binaryOp("append after", do_computeAppendAfterType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType do_computeAppendAfterType(Tree current, AType t1, AType t2, Solver s) { 
    if (isListAType(t1)) {
       return makeListType(s.lub(getListElementType(t1),t2));
    }
    s.report(error(current, "Append after not defined on %t and %t", t1, t2));
    return avalue();
}

// ---- insertBefore

void collect(current: (Expression) `<Expression lhs> \>\> <Expression rhs>`, Collector c){
    c.calculate("insert before", current, [lhs, rhs],
       AType(Solver s){ return binaryOp("insert before", do_computeInsertBeforeType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType do_computeInsertBeforeType(Tree current, AType t1, AType t2, Solver s) { 
    if (isListAType(t2)) {
        return makeListType(s.lub(getListElementType(t2),t1));
    }
    s.report(error(current, "Insert before not defined on %t and %t", t1, t2));
    return avalue();
}

// ---- modulo

void collect(current: (Expression) `<Expression lhs> mod <Expression rhs>`, Collector c){
    c.calculate("modulo", current, [lhs, rhs],
       AType(Solver s){ return binaryOp("modulo", do_computeModuloType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType do_computeModuloType(Tree current, AType t1, AType t2, Solver s) { 
    if(!(isIntAType(t1) && isIntAType(t2)) ){
        s.report(error(current, "Modulo not defined on %t and %t", t1, t2));
    }
    return aint();
}

// ---- notin

void collect(current: (Expression) `<Expression lhs> notin <Expression rhs>`, Collector c){
    c.calculate("notin", current, [lhs, rhs], 
       AType(Solver s) { return binaryOp("notin", do_computeInType, current, s.getType(lhs), s.getType(rhs), s); });
    collect(lhs, rhs, c); 
}

private AType do_computeInType(Tree current, AType t1, AType t2, Solver s){
    if (isRelAType(t2)) {
        et = getRelElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isSetAType(t2)) {
        et = getSetElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isMapAType(t2)) {
        et = getMapDomainType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with domain type of %t", t1, t2));
        return abool();
    } else if (isListRelAType(t2)) {
        et = getListRelElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else if (isListAType(t2)) {
        et = getListElementType(t2);
        s.requireComparable(t1, et, error(current, "Cannot compare %t with element type of %t", t1, t2));
        return abool();
    } else {
        s.report(error(current, "`in` or `notin` not defined on %t and %t", t1, t2));
    }
    return avalue();
}

// ---- in

void collect(current: (Expression) `<Expression lhs> in <Expression rhs>`, Collector c){
    c.calculate("in", current, [lhs, rhs], 
       AType(Solver s) { return binaryOp("in", do_computeInType, current, s.getType(lhs), s.getType(rhs), s); });
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
       void(Solver s){ binaryOp(op, do_computeComparisonType, current, s.getType(current.lhs), s.getType(current.rhs), s); });
    c.fact(current, abool());
    collect([current.lhs, current.rhs], c);
}

private AType do_computeComparisonType(Tree current, AType t1, AType t2, Solver s){
    if(t1.alabel?) t1 = unset(t1, "alabel");      // TODO: do this for all operators?
    if(t2.alabel?) t2 = unset(t2, "alabel");
    if(comparable(t1, t2) || (isNumericType(t1) && isNumericType(t2)))
       return abool();
        
    if(t1 == avoid() || t2 == avoid())
       s.report(error(current, "Comparison not defined on %t and %t", t1, t2));
        
    if (isListRelAType(t1) && isListRelAType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return abool();
    if (isListAType(t1) && isListAType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return abool();
    if (isMapAType(t1) && isMapAType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return abool();
    if (isRelAType(t1) && isRelAType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return abool();
    if (isSetAType(t1) && isSetAType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return abool();
    if (isTupleAType(t1) && isTupleAType(t2))
        return abool();
        
    if (isValueAType(t1) || isValueAType(t2))
        return abool();
    
    s.report(error(current, "Comparison not defined on %t and %t", t1, t2));
    return abool();
}
    
// ---- ifDefined

void collect(current: (Expression) `<Expression e1> ? <Expression e2>`, Collector c) { 
    checkIsDefinedArg(e1, c);   
    c.calculate("if defined", current, [e1, e2], AType(Solver s){ return s.lub(s.getType(e1), s.getType(e2)); });
    collect(e1, e2, c);
    c.require("non void", e1, [], makeNonVoidRequirement(e1, "First argument of if-defined operator _ ? _"));
    c.require("non void", e2, [], makeNonVoidRequirement(e2, "Second argument of if-defined operator _ ? _"));
    
}

// ---- noMatch

void collect(current: (Expression) `<Pattern pat> !:= <Expression expression>`, Collector c){
    //c.enterScope(current);  // wrap in extra scope to avoid that variables in pattern leak to surroundings
    computeMatchPattern(current, pat, "!:=", expression, c);
    //c.leaveScope(current);
}
// ---- match

void collect(current: (Expression) `<Pattern pat> := <Expression expression>`, Collector c){
    computeMatchPattern(current, pat, ":=", expression, c);
}

void computeMatchPattern(Expression current, Pattern pat, str operator, Expression expression, Collector c){
    scope = c.getScope();
    c.calculate("match", current, [expression],
        AType(Solver s) {
            subjectType = s.getType(expression);
            checkNonVoid(expression, subjectType, s, "Second argument of match operator _ := _");
            if(isStartNonTerminalType(subjectType)){
                subjectType = getStartNonTerminalType(subjectType);
            }
            patType = getPatternType(pat, subjectType, scope, s);
            s.instantiate(subjectType);
            instantiateAndCompare(current, patType, subjectType, s);
            return abool();
        });
    c.push(patternContainer, "match");
    if(operator == "!:=") c.enterScope(pat);  // wrap in extra scope to avoid that variables in pattern leak to surroundings
    collect(pat, c);
    if(operator == "!:=") c.leaveScope(pat);
    c.pop(patternContainer);
    collect(expression, c);
}

// ---- enumerator

void collect(current: (Expression) `<Pattern pat> \<- <Expression expression>`, Collector c){
    scope = c.getScope();
    collect(expression, c); // first collect expression, its type may help find pat's type early on
    if((Pattern) `<QualifiedName name>` := pat){    // Optimize a very common case
        try {
            exprType = c.getType(expression);
            patType = avalue();
            if (isSetAType(exprType)) {
             patType = getSetElementType(exprType);
            } else if (isListAType(exprType)) {
                patType = getListElementType(exprType);
            } else if (isMapAType(exprType)) {
                patType = getMapDomainType(exprType);
            } else if (isADTAType(exprType) || isTupleAType(exprType) || isNodeAType(exprType)) {
                patType = avalue();
            } else if (isIterType(exprType)) {
                  patType = getIterElementType(exprType);
            } else if (isOptType(exprType)) {
                  patType = getOptType(exprType);
            } else  
                throw TypeUnavailable();
          
            c.define("<name>", formalOrPatternFormal(c), name, defType(patType));
            c.fact(pat, patType);
            c.fact(current, abool());
            return;  
        } catch TypeUnavailable(): ;
    }
    
    collect(pat, c); // collect pat in standard fashion
    
    c.calculate("enumeration", current, [expression],
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
                for(<_, _, tp> <- overloads){
                    if(comparable(patType, tp)) return abool();
                }
                s.report(error(pat, "Pattern of type %t cannot be used to enumerate over %t", patType, exprType));
            } 
            s.requireComparable(patType, elmType, error(pat, "Pattern of type %t cannot be used to enumerate over %t", patType, exprType));
            return abool();
           });
    
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
    
    if (isSetAType(etype)) {
        return getSetElementType(etype);
    } else if (isListAType(etype)) {
        return getListElementType(etype);
    } else if (isMapAType(etype)) {
        return getMapDomainType(etype);
    } else if (isADTAType(etype) || isTupleAType(etype) || isNodeAType(etype)) {
        return avalue();
    } else if (isIterType(etype)) {
        return getIterElementType(etype);
    } else if (isOptType(etype)) {
        return getOptType(etype);
    } else if(overloadedAType(rel[loc, IdRole, AType] overloads) := etype){
        for(<_, _, tp> <- overloads, isEnumeratorType(tp)){
            try {
                return computeEnumeratorElementType(current, tp, s);
            } catch checkFailed(list[FailMessage] _): /* do nothing and try next overload*/;
              catch NoBinding():  /* do nothing and try next overload*/;
        }
    } 
    s.report(error(current, "Type %t is not enumerable", etype));
    return avalue();
}

// TODO scoping rules in Boolean operators!
// ---- implication

void collect(current: (Expression) `<Expression lhs> ==\> <Expression rhs>`, Collector c){
    c.fact(current, abool());
   
    c.require("implication", current, [lhs, rhs],
        void (Solver s){ 
            s.requireUnify(abool(), s.getType(lhs), error(lhs, "Argument of ==\> should be `bool`, found %t", lhs));
            s.requireUnify(abool(), rhs, error(rhs, "Argument of ==\> should be `bool`, found %t", rhs));
            s.requireTrue(backtrackFree(lhs) && backtrackFree(rhs), error(current, "No backtracking allowed in arguments of ==\>"));
            //s.requireTrue(backtrackFree(rhs), error(rhs, "No backtracking allowed in argument of ==\>"));
        });
    collect(lhs, rhs, c);
}

// ---- equivalence

void collect(current: (Expression) `<Expression lhs> \<==\> <Expression rhs>`, Collector c){
    //c.fact(current, abool());
   
    c.calculate("equivalence", current, [lhs, rhs],
        AType(Solver s){ 
            s.requireUnify(abool(), lhs, error(lhs, "Argument of \<==\> should be `bool`, found %t", lhs));
            s.requireUnify(abool(), rhs, error(rhs, "Argument of \<==\> should be `bool`, found %t", rhs));
            s.requireTrue(backtrackFree(lhs) && backtrackFree(rhs), error(current, "No backtracking allowed in arguments of \<==\>"));
            //s.requireTrue(backtrackFree(rhs), error(rhs, "No backtracking allowed in argument of \<==\>"));
            return abool();
        });
    collect(lhs, rhs, c);
}

// ---- and

void collect(current: (Expression) `<Expression lhs> && <Expression rhs>`, Collector c){
    c.fact(current, abool());
   
    c.require("and", current, [lhs, rhs],
        void (Solver s){ 
            s.requireUnify(abool(), lhs, error(lhs, "Argument of && should be `bool`, found %t", lhs));
            //clearBindings();
            s.requireUnify(abool(), rhs, error(rhs, "Argument of && should be `bool`, found %t", rhs));
            //clearBindings();
          });
    collect(lhs, rhs, c);
}

private set[str] introducedVars(Expression exp, Collector c){
    if(exp is \bracket){
        return introducedVars(exp.expression, c);
    } else if (exp is match){
        return introducedVars(exp.pattern, c);
    } else if (exp is and || exp is implication || exp is equivalence){
        return introducedVars(exp.lhs, c) + introducedVars(exp.rhs, c);
    } else {
        return {};
    }
}

private set[str] introducedVars(Pattern e, Collector c){
    vars = {};
    top-down-break visit(e){
        case (Pattern) `<QualifiedName qualifiedName>`: {
             nm = prettyPrintName(qualifiedName);
             if(nm[0] != "_" && !c.isAlreadyDefined(nm, qualifiedName)) vars += nm;
        }
        case (Pattern) `<Type _> <Name name>` : { 
             nm = prettyPrintName(name); 
             if(nm[0] != "_") vars += nm;
        }
        case (Pattern) `<Name name> : <Pattern pattern>`: {
             nm = prettyPrintName(name); 
             if(nm[0] != "_" && !c.isAlreadyDefined(nm, name)) vars += nm;
             vars += introducedVars(pattern, c);
        }
        case (Pattern) `<Type _> <Name name> : <Pattern pattern>`: {
             nm = prettyPrintName(name); 
             if(nm[0] != "_") vars += nm;
             vars += introducedVars(pattern, c);
        }
        case (Pattern) `<Pattern expression> ( <{Pattern ","}* arguments> <KeywordArguments[Pattern] keywordArguments> )`: {
            if(!(expression is qualifiedName)) vars += introducedVars(expression, c);
            vars += {*introducedVars(argument, c) | argument <- arguments};
            if(keywordArguments is \default){
                vars += { *introducedVars(kwa.expression, c) | kwa <- keywordArguments.keywordArgumentList };
            }
        }
    }
    return vars;
}

// ---- or
 
void collect(current: (Expression) `<Expression lhs> || <Expression rhs>`, Collector c){
    c.fact(current, abool());
      
    c.require("or", current, [lhs, rhs],
        void (Solver s){ 
            s.requireUnify(abool(), lhs, error(lhs, "Argument of || should be `bool`, found %t", lhs));
            s.requireUnify(abool(), rhs, error(rhs, "Argument of || should be `bool`, found %t", rhs));
          });
          
    introLhs = introducedVars(lhs, c);
    introRhs = introducedVars(rhs, c);  
      
    collect(lhs, c);
    
    // make common variables available when collecting rhs;
    // variables in rhs will use definition from lhs (see CollectPattern: typed variable pattern, qualifiedName pattern)
    c.setScopeInfo(c.getScope(), orScope(), orInfo(introLhs));
    collect(rhs, c);
    
    // Check that the names introduced in lhs and rhs are the same  
    
    common = introLhs & introRhs;
    missing = (introLhs - common) + (introRhs - common);
    
    if(!isEmpty(missing)){
        c.report(error(current, "Variable(s) %v should be introduced on both sides of `||` operator", missing));
    }
}

// ---- if expression

void checkNoAssignable(Expression e, Solver s, str msg){
    if(/Assignable _ := e){
        s.report(error(e, msg + " should not contain assignment"));
    }
}

void collect(current: (Expression) `<Expression condition> ? <Expression thenExp> : <Expression elseExp>`, Collector c){
    c.enterCompositeScope([condition, thenExp]);   // thenExp may refer to variables defined in conditions; elseExp may not
        
        c.calculate("if expression", current, [condition, thenExp, elseExp],
            AType(Solver s){
                s.requireComparable(abool(), condition, error(condition, "Condition should be `bool`, found %t", condition));
                checkNonVoid(thenExp, s, "Then part in conditional expression");
                checkNonVoid(elseExp, s, "Else part in conditional expression");
                checkNoAssignable(thenExp, s, "Then part in conditional expression");
                checkNoAssignable(elseExp, s, "Else part in conditional expression");
                return s.lub(thenExp, elseExp);
            });
        beginPatternScope("conditions", c);
            collect(condition, c);
        endPatternScope(c);
        collect(thenExp, c);
     c.leaveCompositeScope([condition, thenExp]); 
     collect(elseExp, c);
    
}
