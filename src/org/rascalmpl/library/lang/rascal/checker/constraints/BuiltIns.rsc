@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::constraints::BuiltIns

import List;
import Set;
import Node;
import Relation;
import lang::rascal::types::Types;
import lang::rascal::types::SubTypes;
import lang::rascal::types::Lubs;
import lang::rascal::types::TypeEquivalence;
import lang::rascal::scoping::SymbolTable;
import constraints::Constraint;
import lang::rascal::checker::constraints::Constraints;

alias UnaryTypingFun = RType (RType, loc);

alias BinaryTypingFun = RType (RType, RType, loc);

private map[RBuiltInOp,str] opStr = ( Negative() : "-", Plus() : "+", Minus() : "-", NotIn() : "notin", In() : "in",
                                      Lt() : "\<", LtEq() : "\<=", Gt() : "\>", GtEq() : "\>=", Eq() : "==", NEq() : "!=",
                                      Intersect() : "&", Product() : "*", Join() : "join", Div() : "/", Mod() : "%" ); 

public str prettyPrintBuiltInOp(RBuiltInOp bop) {
    if (bop in opStr) return opStr[bop];
    throw "Operator <bop> is not in the operator/string map";
}

private set[RBuiltInOp] unaryOps = { Negative() };

private set[RBuiltInOp] binaryOps = { Plus(), Minus(), NotIn(), In(), Lt(), LtEq(), Gt(), 
                                      GtEq(), Eq(), NEq(), Intersect(), Product(), Join(), 
                                      Div(), Mod() };

public RType getBuiltInResultType(RBuiltInOp op, RType domain, RType range, loc at) {
    list[RType] domainAsList = getTupleFields(domain);
    if (op in unaryOps && size(domainAsList) == 1) {
        if (size(typingRelUnaryOps[op,getName(domainAsList[0])]) == 0) {
            str msg = "Error, <prettyPrintBuiltInOp(op)> is not defined for <prettyPrintType(domainAsList[0])>";
            return makeFailType(msg, at);
        } else {
            UnaryTypingFun tfun = getOneFrom(typingRelUnaryOps[op,getName(domainAsList[0])]); 
            return tfun(domainAsList[0],at);
        }
    }
     else if (op in binaryOps && size(domainAsList) == 2) {
        if (size(typingRelBinOps[op,getName(domainAsList[0]),getName(domainAsList[1])]) == 0) {
            str msg = "Error, <prettyPrintBuiltInOp(op)> is not defined for <prettyPrintType(domainAsList[0])> and <prettyPrintType(domainAsList[1])>";
            return makeFailType(msg, at);            
        } else {
            BinaryTypingFun tfun = getOneFrom(typingRelBinOps[op,getName(domainAsList[0]),getName(domainAsList[1])]); 
            return tfun(domainAsList[0],domainAsList[1],at);
        }
    } else {
        return makeFailType("Error, <prettyPrintBuiltInOp(op)> is not yet supported", at);
    }
}

public RType negateInt(RType left, loc at) {
    return makeIntType();
}

public RType negateReal(RType left, loc at) {
    return makeRealType();
}

public RType negateNum(RType left, loc at) {
    return makeNumType();
}

public rel[RBuiltInOp op, str left, UnaryTypingFun tfun] typingRelUnaryOps = {
    < Negative(), "RIntType", negateInt >,
    < Negative(), "RNumType", negateNum >,
    < Negative(), "RRealType", negateReal >
};

public RType itemPlusList(RType left, RType right, loc at) {
    return makeListType(lub(left,getListElementType(right)));
}

public RType listPlusItem(RType left, RType right, loc at) {
    return makeListType(lub(getListElementType(left), right));
}

public RType listPlusList(RType left, RType right, loc at) {
    return makeListType(lub(getListElementType(left),getListElementType(right)));
}

public RType listTimesItem(RType left, RType right, loc at) {
    return makeListType(makeTupleType([getListElementType(left),right]));
}

public RType listTimesList(RType left, RType right, loc at) {
    return makeListType(makeTupleType([getListElementType(left),getListElementType(right)]));
}

public RType setTimesItem(RType left, RType right, loc at) {
    return makeSetType(makeTupleType([getSetElementType(left),right]));
}

public RType setTimesSet(RType left, RType right, loc at) {
    return makeSetType(makeTupleType([getSetElementType(left),getSetElementType(right)]));
}

public RType setTimesRel(RType left, RType right, loc at) {
    return makeRelTypeFromTuple(makeTupleType([getSetElementType(left),getRelElementType(right)]));
}

public RType itemPlusSet(RType left, RType right, loc at) {
    return makeSetType(lub(left,getSetElementType(right)));
}

public RType setPlusItem(RType left, RType right, loc at) {
    return makeSetType(lub(getSetElementType(left),right));
}

public RType setPlusSet(RType left, RType right, loc at) {
    return makeSetType(lub(getSetElementType(left),getSetElementType(right)));
}

public RType setPlusRel(RType left, RType right, loc at) {
    return makeSetType(lub(getSetElementType(left),getRelElementType(right)));
}

public RType itemPlusBag(RType left, RType right, loc at) {
    return makeBagType(lub(left,getBagElementType(right)));
}

public RType bagPlusItem(RType left, RType right, loc at) {
    return makeBagType(lub(getBagElementType(left),right));
}

public RType bagPlusBag(RType left, RType right, loc at) {
    return makeBagType(lub(getBagElementType(left),getBagElementType(right)));
}

public RType arithYieldsInt(RType left, RType right, loc at) {
    return makeIntType();
}

public RType arithYieldsReal(RType left, RType right, loc at) {
    return makeRealType();
}

public RType arithYieldsNum(RType left, RType right, loc at) {
    return makeNumType();
}

public RType compareYieldsBool(RType left, RType right, loc at) {
    return makeBoolType();
}

public RType strPlusStr(RType left, RType right, loc at) {
    return makeStrType();
}

public RType locPlusStr(RType left, RType right, loc at) {
    return makeLocType();
}

public RType relPlusRel(RType left, RType right, loc at) {
    if (comparable(getRelElementType(left),getRelElementType(right)))
        return makeRelTypeFromTuple(lub(getRelElementType(left),getRelElementType(right)));
    else
        return makeSetType(lub(getRelElementType(left),getRelElementType(right)));
}

public RType relPlusTuple(RType left, RType right, loc at) {
    if (comparable(getRelElementType(left),right))
        return makeRelTypeFromTuple(lub(getRelElementType(left),right));
    else
        return makeSetType(lub(getRelElementType(left),right));
}

public RType relPlusSet(RType left, RType right, loc at) {
    if (RSetType(RTupleType(_)) := right) {
        if (comparable(getRelElementType(left),getSetElementType(right)))
            return makeRelTypeFromTuple(lub(getRelElementType(left),getSetElementType(right)));
        else
            return makeSetType(lub(getRelElementType(left),getSetElementType(right)));
    } else { 
        return makeSetType(lub(getRelElementType(left),getSetElementType(right)));
    }
}

public RType relMinusRel(RType left, RType right, loc at) {
    if (comparable(getRelElementType(left),getRelElementType(right)))
        return left;
    else
        return makeFailType("The type of the right operand, <prettyPrintType(right)>, should be a subtype of the type of the left operand, <prettyPrintType(left)>",at);
}

public RType relMinusTuple(RType left, RType right, loc at) {
    if (comparable(getRelElementType(left),right))
        return left;
    else
        return makeFailType("The type of the right operand, <prettyPrintType(right)>, should be a subtype of the type of the left operand, <prettyPrintType(left)>",at);
}

public RType relMinusSet(RType left, RType right, loc at) {
    if (RSetType(RTupleType(_)) := right) {
        if (comparable(getRelElementType(left),getSetElementType(right)))
            return left;
        else
            return makeFailType("The type of the right operand, <prettyPrintType(right)>, should be a subtype of the type of the left operand, <prettyPrintType(left)>",at);
    } else { 
        return makeFailType("The type of the right operand, <prettyPrintType(right)>, should be a subtype of the type of the left operand, <prettyPrintType(left)>",at);
    }
}

public RType relTimesRel(RType left, RType right, loc at) {
    return makeRelTypeFromTuple(makeTupleType([getRelElementType(left),getRelElementType(right)]));
}

public RType relTimesSet(RType left, RType right, loc at) {
    return makeRelTypeFromTuple(makeTupleType([getRelElementType(left),getSetElementType(right)]));
}

public RType tuplePlusTuple(RType left, RType right, loc at) {
    list[RNamedType] tFields1 = getTupleFieldsWithNames(left);
    list[RNamedType] tFields2 = getTupleFieldsWithNames(right);
    if (tupleHasFieldNames(left) && tupleHasFieldNames(right)) {
        // Need to check for consistency to see if we should use the field names
        if (size({ nm | nm <- (getTupleFieldNames(left) + getTupleFieldNames(right)) }) != size(tFields1 + tFields2)) {
            // If we don't have the same number of field names as we have fields, we must have some duplicates,
            // so we strip the field names off.
            return makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]);
        } else {
            return makeTupleTypeWithNames(tFields1 + tFields2);
        }
    } else {
        return makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]);
    }
}

public RType mapPlusMap(RType left, RType right, loc at) {
    return makeMapTypeFromTuple(lub(getMapFieldsAsTuple(left),getMapFieldsAsTuple(right)));
}

public RType inList(RType left, RType right, loc at) {
    if (subtypeOf(left, getListElementType(right)))
        return makeBoolType();
    else
        return makeFailType("The type of the left operand, <prettyPrintType(left)>, must be a subtype of the list element type, <prettyPrintType(getListElementType(right))>",at);
}

public RType inSet(RType left, RType right, loc at) {
    if (subtypeOf(left, getSetElementType(right)))
        return makeBoolType();
    else
        return makeFailType("The type of the left operand, <prettyPrintType(left)>, must be a subtype of the set element type, <prettyPrintType(getSetElementType(right))>",at);
}

public RType inBag(RType left, RType right, loc at) {
    if (subtypeOf(left, getBagElementType(right)))
        return makeBoolType();
    else
        return makeFailType("The type of the left operand, <prettyPrintType(left)>, must be a subtype of the bag element type, <prettyPrintType(getBagElementType(right))>",at);
}

public RType inRel(RType left, RType right, loc at) {
    if (subtypeOf(left, getRelElementType(right)))
        return makeBoolType();
    else
        return makeFailType("The type of the left operand, <prettyPrintType(left)>, must be a subtype of the tuple formed from the relation fields, <prettyPrintType(getRelElementType(right))>",at);
}

public RType inMap(RType left, RType right, loc at) {
    if (subtypeOf(left,getMapDomainType(right)))
        return makeBoolType();
    else
        return makeFailType("The type of the left operand, <prettyPrintType(left)>, must be a subtype of the map domain type, <prettyPrintType(getMapDomainType(right))>",at);
}

public RType listMinusList(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the list on the right, <prettyPrintType(right)>, must be a subtype of the type of the list on the left, <prettyPrintType(left)>",at);
}

public RType listMinusItem(RType left, RType right, loc at) {
    if (subtypeOf(right,getListElementType(left)))
        return left;
    else
        return makeFailType("The type of the item on the right, <prettyPrintType(right)>, must be a subtype of the element type of the list on the left, <prettyPrintType(getListElementType(left))>",at);
}

public RType setMinusSet(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the list on the right, <prettyPrintType(right)>, must be a subtype of the type of the list on the left, <prettyPrintType(left)>",at);
}

public RType setMinusItem(RType left, RType right, loc at) {
    if (subtypeOf(right,getSetElementType(left)))
        return left;
    else
        return makeFailType("The type of the item on the right, <prettyPrintType(right)>, must be a subtype of the element type of the set on the left, <prettyPrintType(getSetElementType(left))>",at);
}

public RType mapMinusMap(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the map on the right, <prettyPrintType(right)>, must be a subtype of the type of the map on the left, <prettyPrintType(getSetElementType(left))>",at);
}

public RType relIntersectRel(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the relation on the right, <prettyPrintType(right)>, must be a subtype of the relation on the left, <prettyPrintType(left)>",at);
}

public RType relIntersectSet(RType left, RType right, loc at) {
    if (isTupleType(getSetElementType(right)) && subtypeOf(getSetElementType(right),getRelElementType(left)))
        return left;
    else
        return makeFailType("The type of the set elements on the right, <prettyPrintType(getSetElementType(right))>, must be a subtype of the relation element type on the left, <prettyPrintType(left)>",at);
}

public RType setIntersectRel(RType left, RType right, loc at) {
    if (subtypeOf(getSetElementType(right),getRelElementType(left)))
        return left;
    else
        return makeFailType("The tuple type of the relation on the right, <prettyPrintType(getRelElementType(right))>, must be a subtype of the element type of the set on the left, <prettyPrintType(getSetElementType(left))>",at);
}

public RType setIntersectSet(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the set on the right, <prettyPrintType(right)>, must be a subtype of the type of the set on the left, <prettyPrintType(left)>",at);
}

public RType mapIntersectMap(RType left, RType right, loc at) {
    if (subtypeOf(right,left))
        return left;
    else
        return makeFailType("The type of the relation on the right, <prettyPrintType(right)>, must be a subtype of the relation on the left, <prettyPrintType(left)>",at);
}

public RType relJoinRel(RType left, RType right, loc at) {
    RType lTuple = getRelElementType(left);
    RType rTuple = getRelElementType(right);
    list[RNamedType] tFields1 = getTupleFieldsWithNames(lTuple);
    list[RNamedType] tFields2 = getTupleFieldsWithNames(rTuple);
    if (tupleHasFieldNames(lTuple) && tupleHasFieldNames(rTuple)) {
        // Need to check for consistency to see if we should use the field names
        if (size({ nm | nm <- (getTupleFieldNames(lTuple)+getTupleFieldNames(rTuple)) }) != size(tFields1 + tFields2)) {
            // If we don't have the same number of field names as we have fields, we must have some duplicates,
            // so we strip the field names off.
            return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
        } else {
            return makeRelTypeFromTuple(makeTupleTypeWithNames(tFields1 + tFields2));
        }
    } else {
        return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
    }
}

public RType relJoinSet(RType left, RType right, loc at) {
    if (isTupleType(getSetElementType(right))) {
        RType lTuple = getRelElementType(left);
        RType rTuple = getRelElementType(right);
        list[RNamedType] tFields1 = getTupleFieldsWithNames(lTuple);
        list[RNamedType] tFields2 = getTupleFieldsWithNames(rTuple);
        if (tupleHasFieldNames(lTuple) && tupleHasFieldNames(rTuple)) {
            // Need to check for consistency to see if we should use the field names
            if (size({ nm | nm <- (getTupleFieldNames(lTuple)+getTupleFieldNames(rTuple)) }) != size(tFields1 + tFields2)) {
                // If we don't have the same number of field names as we have fields, we must have some duplicates,
                // so we strip the field names off.
                return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
            } else {
                return makeRelTypeFromTuple(makeTupleTypeWithNames(tFields1 + tFields2));
            }
        } else {
            return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
        }
    } else {
        return makeFailType("The set on the right must have a tuple type for the element type, not <prettyPrintType(getSetElementType(right))>",at);
    }
}

public RType setJoinRel(RType left, RType right, loc at) {
    return makeRelTypeFromTuple(makeTupleType([getSetElementType(left),getRelElementType(right)]));
}

public RType setJoinSet(RType left, RType right, loc at) {
    return makeRelTypeFromTuple(makeTupleType([getSetElementType(left),getSetElementType(right)]));
}

public rel[RBuiltInOp op, str left, str right, BinaryTypingFun tfun] typingRelBinOps = {
    < Plus(), "RBoolType", "RListType", itemPlusList >,
    < Plus(), "RBoolType", "RSetType", itemPlusSet >,
    < Plus(), "RBoolType", "RBagType", itemPlusBag >,

    < Plus(), "RIntType", "RIntType", arithYieldsInt >,
    < Plus(), "RIntType", "RRealType", arithYieldsReal >,
    < Plus(), "RIntType", "RNumType", arithYieldsNum >,
    < Plus(), "RIntType", "RListType", itemPlusList >,
    < Plus(), "RIntType", "RSetType", itemPlusSet >,
    < Plus(), "RIntType", "RBagType", itemPlusBag >,

    < Plus(), "RRealType", "RIntType", arithYieldsReal >,
    < Plus(), "RRealType", "RRealType", arithYieldsReal >,
    < Plus(), "RRealType", "RNumType", arithYieldsNum >,
    < Plus(), "RRealType", "RListType", itemPlusList >,
    < Plus(), "RRealType", "RSetType", itemPlusSet >,
    < Plus(), "RRealType", "RBagType", itemPlusBag >,

    < Plus(), "RNumType", "RIntType", arithYieldsNum >,
    < Plus(), "RNumType", "RRealType", arithYieldsNum >,
    < Plus(), "RNumType", "RNumType", arithYieldsNum >,
    < Plus(), "RNumType", "RListType", itemPlusList >,
    < Plus(), "RNumType", "RSetType", itemPlusSet >,
    < Plus(), "RNumType", "RBagType", itemPlusBag >,

    < Plus(), "RStrType", "RStrType", strPlusStr >,
    < Plus(), "RStrType", "RListType", itemPlusList >,
    < Plus(), "RStrType", "RSetType", itemPlusSet >,
    < Plus(), "RStrType", "RBagType", itemPlusBag >,

    < Plus(), "RValueType", "RListType", itemPlusList >,
    < Plus(), "RValueType", "RSetType", itemPlusSet >,
    < Plus(), "RValueType", "RBagType", itemPlusBag >,

    < Plus(), "RNodeType", "RListType", itemPlusList >,
    < Plus(), "RNodeType", "RSetType", itemPlusSet >,
    < Plus(), "RNodeType", "RBagType", itemPlusBag >,

    < Plus(), "RLocType", "RStrType", locPlusStr >,
    < Plus(), "RLocType", "RListType", itemPlusList >,
    < Plus(), "RLocType", "RSetType", itemPlusSet >,
    < Plus(), "RLocType", "RBagType", itemPlusBag >,

    < Plus(), "RDateTimeType", "RListType", itemPlusList >,
    < Plus(), "RDateTimeType", "RSetType", itemPlusSet >,
    < Plus(), "RDateTimeType", "RBagType", itemPlusBag >,

    < Plus(), "RTupleType", "RTupleType", tuplePlusTuple >,
    < Plus(), "RTupleType", "RListType", itemPlusList >,
    < Plus(), "RTupleType", "RSetType", itemPlusSet >,
    < Plus(), "RTupleType", "RBagType", itemPlusBag >,

    < Plus(), "RADTType", "RListType", itemPlusList >,
    < Plus(), "RADTType", "RSetType", itemPlusSet >,
    < Plus(), "RADTType", "RBagType", itemPlusBag >,

    < Plus(), "RConstructorType", "RListType", itemPlusList >,
    < Plus(), "RConstructorType", "RSetType", itemPlusSet >,
    < Plus(), "RConstructorType", "RBagType", itemPlusBag >,
    
    < Plus(), "RListType", "RListType", listPlusList >,
    < Plus(), "RListType", "RBoolType", listPlusItem >,
    < Plus(), "RListType", "RIntType", listPlusItem >,
    < Plus(), "RListType", "RRealType", listPlusItem >,
    < Plus(), "RListType", "RNumType", listPlusItem >,
    < Plus(), "RListType", "RStrType", listPlusItem >,
    < Plus(), "RListType", "RValueType", listPlusItem >,
    < Plus(), "RListType", "RNodeType", listPlusItem >,
    < Plus(), "RListType", "RLocType", listPlusItem >,
    < Plus(), "RListType", "RDateTimeType", listPlusItem >,
    < Plus(), "RListType", "RTupleType", listPlusItem >,
    < Plus(), "RListType", "RADTType", listPlusItem >,
    < Plus(), "RListType", "RConstructorType", listPlusItem >,
    < Plus(), "RListType", "RSetType", listPlusItem >,
    < Plus(), "RListType", "RBagType", listPlusItem >,
    < Plus(), "RListType", "RRelType", listPlusItem >,
    < Plus(), "RListType", "RMapType", listPlusItem >,

    < Plus(), "RSetType", "RSetType", setPlusSet >,
    < Plus(), "RSetType", "RRelType", setPlusRel >,
    < Plus(), "RSetType", "RBoolType", setPlusItem >,
    < Plus(), "RSetType", "RIntType", setPlusItem >,
    < Plus(), "RSetType", "RRealType", setPlusItem >,
    < Plus(), "RSetType", "RNumType", setPlusItem >,
    < Plus(), "RSetType", "RStrType", setPlusItem >,
    < Plus(), "RSetType", "RValueType", setPlusItem >,
    < Plus(), "RSetType", "RNodeType", setPlusItem >,
    < Plus(), "RSetType", "RLocType", setPlusItem >,
    < Plus(), "RSetType", "RDateTimeType", setPlusItem >,
    < Plus(), "RSetType", "RTupleType", setPlusItem >,
    < Plus(), "RSetType", "RADTType", setPlusItem >,
    < Plus(), "RSetType", "RConstructorType", setPlusItem >,
    < Plus(), "RSetType", "RListType", setPlusItem >,
    < Plus(), "RSetType", "RBagType", setPlusItem >,
    < Plus(), "RSetType", "RRelType", setPlusItem >,
    < Plus(), "RSetType", "RMapType", setPlusItem >,

    < Plus(), "RBagType", "RBagType", bagPlusBag >,
    < Plus(), "RBagType", "RBoolType", bagPlusItem >,
    < Plus(), "RBagType", "RIntType", bagPlusItem >,
    < Plus(), "RBagType", "RRealType", bagPlusItem >,
    < Plus(), "RBagType", "RNumType", bagPlusItem >,
    < Plus(), "RBagType", "RStrType", bagPlusItem >,
    < Plus(), "RBagType", "RValueType", bagPlusItem >,
    < Plus(), "RBagType", "RNodeType", bagPlusItem >,
    < Plus(), "RBagType", "RLocType", bagPlusItem >,
    < Plus(), "RBagType", "RDateTimeType", bagPlusItem >,
    < Plus(), "RBagType", "RTupleType", bagPlusItem >,
    < Plus(), "RBagType", "RADTType", bagPlusItem >,
    < Plus(), "RBagType", "RConstructorType", bagPlusItem >,
    < Plus(), "RBagType", "RListType", bagPlusItem >,
    < Plus(), "RBagType", "RSetType", bagPlusItem >,
    < Plus(), "RBagType", "RRelType", bagPlusItem >,
    < Plus(), "RBagType", "RMapType", bagPlusItem >,

    < Plus(), "RRelType", "RRelType", relPlusRel >,
    < Plus(), "RRelType", "RTupleType", relPlusTuple >,
    < Plus(), "RRelType", "RSetType", relPlusSet >,
    
    < Plus(), "RMapType", "RMapType", mapPlusMap >,
    
    < Minus(), "RIntType", "RIntType", arithYieldsInt >,
    < Minus(), "RIntType", "RRealType", arithYieldsReal >,
    < Minus(), "RIntType", "RNumType", arithYieldsNum >,

    < Minus(), "RRealType", "RIntType", arithYieldsReal >,
    < Minus(), "RRealType", "RRealType", arithYieldsReal >,
    < Minus(), "RRealType", "RNumType", arithYieldsNum >,

    < Minus(), "RNumType", "RIntType", arithYieldsNum >,
    < Minus(), "RNumType", "RRealType", arithYieldsNum >,
    < Minus(), "RNumType", "RNumType", arithYieldsNum >,

    < Minus(), "RListType", "RListType", listMinusList >,
    < Minus(), "RListType", "RBoolType", listMinusItem >,
    < Minus(), "RListType", "RIntType", listMinusItem >,
    < Minus(), "RListType", "RRealType", listMinusItem >,
    < Minus(), "RListType", "RNumType", listMinusItem >,
    < Minus(), "RListType", "RStrType", listMinusItem >,
    < Minus(), "RListType", "RValueType", listMinusItem >,
    < Minus(), "RListType", "RNodeType", listMinusItem >,
    < Minus(), "RListType", "RLocType", listMinusItem >,
    < Minus(), "RListType", "RDateTimeType", listMinusItem >,
    < Minus(), "RListType", "RTupleType", listMinusItem >,
    < Minus(), "RListType", "RADTType", listMinusItem >,
    < Minus(), "RListType", "RConstructorType", listMinusItem >,    
    < Minus(), "RListType", "RSetType", listMinusItem >,
    < Minus(), "RListType", "RBagType", listMinusItem >,
    < Minus(), "RListType", "RRelType", listMinusItem >,
    < Minus(), "RListType", "RMapType", listMinusItem >,

    < Minus(), "RSetType", "RSetType", setMinusSet >,
    < Minus(), "RSetType", "RBoolType", setMinusItem >,
    < Minus(), "RSetType", "RIntType", setMinusItem >,
    < Minus(), "RSetType", "RRealType", setMinusItem >,
    < Minus(), "RSetType", "RNumType", setMinusItem >,
    < Minus(), "RSetType", "RStrType", setMinusItem >,
    < Minus(), "RSetType", "RValueType", setMinusItem >,
    < Minus(), "RSetType", "RNodeType", setMinusItem >,
    < Minus(), "RSetType", "RLocType", setMinusItem >,
    < Minus(), "RSetType", "RDateTimeType", setMinusItem >,
    < Minus(), "RSetType", "RTupleType", setMinusItem >,
    < Minus(), "RSetType", "RADTType", setMinusItem >,
    < Minus(), "RSetType", "RConstructorType", setMinusItem >,    
    < Minus(), "RSetType", "RListType", setMinusItem >,
    < Minus(), "RSetType", "RBagType", setMinusItem >,
    < Minus(), "RSetType", "RRelType", setMinusItem >,
    < Minus(), "RSetType", "RMapType", setMinusItem >,
    
    < Minus(), "RMapType", "RMapType", mapMinusMap >,
    
    < Minus(), "RRelType", "RRelType", relMinusRel >,
    < Minus(), "RRelType", "RTupleType", relMinusTuple >,
    < Minus(), "RRelType", "RSetType", relMinusSet >,
    
    < Product(), "RIntType", "RIntType", arithYieldsInt >,
    < Product(), "RIntType", "RRealType", arithYieldsReal >,
    < Product(), "RIntType", "RNumType", arithYieldsNum >,

    < Product(), "RRealType", "RIntType", arithYieldsReal >,
    < Product(), "RRealType", "RRealType", arithYieldsReal >,
    < Product(), "RRealType", "RNumType", arithYieldsNum >,

    < Product(), "RNumType", "RIntType", arithYieldsNum >,
    < Product(), "RNumType", "RRealType", arithYieldsNum >,
    < Product(), "RNumType", "RNumType", arithYieldsNum >,

    < Product(), "RListType", "RListType", listTimesList >,
    < Product(), "RListType", "RBoolType", listTimesItem >,
    < Product(), "RListType", "RIntType", listTimesItem >,
    < Product(), "RListType", "RRealType", listTimesItem >,
    < Product(), "RListType", "RNumType", listTimesItem >,
    < Product(), "RListType", "RStrType", listTimesItem >,
    < Product(), "RListType", "RValueType", listTimesItem >,
    < Product(), "RListType", "RNodeType", listTimesItem >,
    < Product(), "RListType", "RLocType", listTimesItem >,
    < Product(), "RListType", "RDateTimeType", listTimesItem >,
    < Product(), "RListType", "RTupleType", listTimesItem >,
    < Product(), "RListType", "RADTType", listTimesItem >,
    < Product(), "RListType", "RConstructorType", listTimesItem >,
    < Product(), "RListType", "RSetType", listTimesItem >,
    < Product(), "RListType", "RBagType", listTimesItem >,
    < Product(), "RListType", "RRelType", listTimesItem >,
    < Product(), "RListType", "RMapType", listTimesItem >,

    < Product(), "RSetType", "RSetType", setTimesSet >,
    < Product(), "RSetType", "RBoolType", setTimesItem >,
    < Product(), "RSetType", "RIntType", setTimesItem >,
    < Product(), "RSetType", "RRealType", setTimesItem >,
    < Product(), "RSetType", "RNumType", setTimesItem >,
    < Product(), "RSetType", "RStrType", setTimesItem >,
    < Product(), "RSetType", "RValueType", setTimesItem >,
    < Product(), "RSetType", "RNodeType", setTimesItem >,
    < Product(), "RSetType", "RLocType", setTimesItem >,
    < Product(), "RSetType", "RDateTimeType", setTimesItem >,
    < Product(), "RSetType", "RTupleType", setTimesItem >,
    < Product(), "RSetType", "RADTType", setTimesItem >,
    < Product(), "RSetType", "RConstructorType", setTimesItem >,
    < Product(), "RSetType", "RSetType", setTimesItem >,
    < Product(), "RSetType", "RBagType", setTimesItem >,
    < Product(), "RSetType", "RRelType", setTimesRel >,
    < Product(), "RSetType", "RMapType", setTimesItem >,

    < Product(), "RRelType", "RRelType", relTimesRel >,
    < Product(), "RRelType", "RSetType", relTimesSet >,

    < Div(), "RIntType", "RIntType", arithYieldsInt >,
    < Div(), "RIntType", "RRealType", arithYieldsReal >,
    < Div(), "RIntType", "RNumType", arithYieldsNum >,

    < Div(), "RRealType", "RIntType", arithYieldsReal >,
    < Div(), "RRealType", "RRealType", arithYieldsReal >,
    < Div(), "RRealType", "RNumType", arithYieldsNum >,

    < Div(), "RNumType", "RIntType", arithYieldsNum >,
    < Div(), "RNumType", "RRealType", arithYieldsNum >,
    < Div(), "RNumType", "RNumType", arithYieldsNum >,
    
    < Mod(), "RIntType", "RIntType", arithYieldsInt >,

    < Lt(), "RBoolType", "RBoolType", compareYieldsBool >,

    < Lt(), "RIntType", "RIntType", compareYieldsBool >,
    < Lt(), "RIntType", "RRealType", compareYieldsBool >,
    < Lt(), "RIntType", "RNumType", compareYieldsBool >,

    < Lt(), "RRealType", "RIntType", compareYieldsBool >,
    < Lt(), "RRealType", "RRealType", compareYieldsBool >,
    < Lt(), "RRealType", "RNumType", compareYieldsBool >,

    < Lt(), "RNumType", "RIntType", compareYieldsBool >,
    < Lt(), "RNumType", "RRealType", compareYieldsBool >,
    < Lt(), "RNumType", "RNumType", compareYieldsBool >,

    < Lt(), "RStrType", "RStrType", compareYieldsBool >,

    < Lt(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < Lt(), "RNodeType", "RNodeType", compareYieldsBool >,
    < Lt(), "RNodeType", "RADTType", compareYieldsBool >,

    < Lt(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < Lt(), "RTupleType", "RTupleType", compareYieldsBool >,

    < Lt(), "RADTType", "RConstructorType", compareYieldsBool >,
    < Lt(), "RADTType", "RNodeType", compareYieldsBool >,
    < Lt(), "RADTType", "RADTType", compareYieldsBool >,

    < Lt(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < Lt(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < Lt(), "RConstructorType", "RADTType", compareYieldsBool >,

    < Lt(), "RListType", "RListType", compareYieldsBool >,

    < Lt(), "RSetType", "RSetType", compareYieldsBool >,
    < Lt(), "RSetType", "RRelType", compareYieldsBool >,
    < Lt(), "RRelType", "RSetType", compareYieldsBool >,
    < Lt(), "RRelType", "RRelType", compareYieldsBool >,

    < Lt(), "RMapType", "RMapType", compareYieldsBool >,

    < LtEq(), "RBoolType", "RBoolType", compareYieldsBool >,

    < LtEq(), "RIntType", "RIntType", compareYieldsBool >,
    < LtEq(), "RIntType", "RRealType", compareYieldsBool >,
    < LtEq(), "RIntType", "RNumType", compareYieldsBool >,
    
    < LtEq(), "RRealType", "RIntType", compareYieldsBool >,
    < LtEq(), "RRealType", "RRealType", compareYieldsBool >,
    < LtEq(), "RRealType", "RNumType", compareYieldsBool >,

    < LtEq(), "RNumType", "RIntType", compareYieldsBool >,
    < LtEq(), "RNumType", "RRealType", compareYieldsBool >,
    < LtEq(), "RNumType", "RNumType", compareYieldsBool >,

    < LtEq(), "RStrType", "RStrType", compareYieldsBool >,

    < LtEq(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < LtEq(), "RNodeType", "RNodeType", compareYieldsBool >,
    < LtEq(), "RNodeType", "RADTType", compareYieldsBool >,

    < LtEq(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < LtEq(), "RTupleType", "RTupleType", compareYieldsBool >,

    < LtEq(), "RADTType", "RConstructorType", compareYieldsBool >,
    < LtEq(), "RADTType", "RNodeType", compareYieldsBool >,
    < LtEq(), "RADTType", "RADTType", compareYieldsBool >,

    < LtEq(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < LtEq(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < LtEq(), "RConstructorType", "RADTType", compareYieldsBool >,

    < LtEq(), "RListType", "RListType", compareYieldsBool >,

    < LtEq(), "RSetType", "RSetType", compareYieldsBool >,
    < LtEq(), "RSetType", "RRelType", compareYieldsBool >,
    < LtEq(), "RRelType", "RSetType", compareYieldsBool >,
    < LtEq(), "RRelType", "RRelType", compareYieldsBool >,

    < LtEq(), "RMapType", "RMapType", compareYieldsBool >,

    < Gt(), "RBoolType", "RBoolType", compareYieldsBool >,

    < Gt(), "RIntType", "RIntType", compareYieldsBool >,
    < Gt(), "RIntType", "RRealType", compareYieldsBool >,
    < Gt(), "RIntType", "RNumType", compareYieldsBool >,
    
    < Gt(), "RRealType", "RIntType", compareYieldsBool >,
    < Gt(), "RRealType", "RRealType", compareYieldsBool >,
    < Gt(), "RRealType", "RNumType", compareYieldsBool >,

    < Gt(), "RNumType", "RIntType", compareYieldsBool >,
    < Gt(), "RNumType", "RRealType", compareYieldsBool >,
    < Gt(), "RNumType", "RNumType", compareYieldsBool >,

    < Gt(), "RStrType", "RStrType", compareYieldsBool >,

    < Gt(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < Gt(), "RNodeType", "RNodeType", compareYieldsBool >,
    < Gt(), "RNodeType", "RADTType", compareYieldsBool >,

    < Gt(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < Gt(), "RTupleType", "RTupleType", compareYieldsBool >,

    < Gt(), "RADTType", "RConstructorType", compareYieldsBool >,
    < Gt(), "RADTType", "RNodeType", compareYieldsBool >,
    < Gt(), "RADTType", "RADTType", compareYieldsBool >,

    < Gt(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < Gt(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < Gt(), "RConstructorType", "RADTType", compareYieldsBool >,

    < Gt(), "RListType", "RListType", compareYieldsBool >,

    < Gt(), "RSetType", "RSetType", compareYieldsBool >,
    < Gt(), "RSetType", "RRelType", compareYieldsBool >,
    < Gt(), "RRelType", "RSetType", compareYieldsBool >,
    < Gt(), "RRelType", "RRelType", compareYieldsBool >,

    < Gt(), "RMapType", "RMapType", compareYieldsBool >,

    < GtEq(), "RBoolType", "RBoolType", compareYieldsBool >,

    < GtEq(), "RIntType", "RIntType", compareYieldsBool >,
    < GtEq(), "RIntType", "RRealType", compareYieldsBool >,
    < GtEq(), "RIntType", "RNumType", compareYieldsBool >,

    < GtEq(), "RRealType", "RIntType", compareYieldsBool >,
    < GtEq(), "RRealType", "RRealType", compareYieldsBool >,
    < GtEq(), "RRealType", "RNumType", compareYieldsBool >,

    < GtEq(), "RNumType", "RIntType", compareYieldsBool >,
    < GtEq(), "RNumType", "RRealType", compareYieldsBool >,
    < GtEq(), "RNumType", "RNumType", compareYieldsBool >,

    < GtEq(), "RStrType", "RStrType", compareYieldsBool >,

    < GtEq(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < GtEq(), "RNodeType", "RNodeType", compareYieldsBool >,
    < GtEq(), "RNodeType", "RADTType", compareYieldsBool >,

    < GtEq(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < GtEq(), "RTupleType", "RTupleType", compareYieldsBool >,

    < GtEq(), "RADTType", "RConstructorType", compareYieldsBool >,
    < GtEq(), "RADTType", "RNodeType", compareYieldsBool >,
    < GtEq(), "RADTType", "RADTType", compareYieldsBool >,

    < GtEq(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < GtEq(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < GtEq(), "RConstructorType", "RADTType", compareYieldsBool >,

    < GtEq(), "RListType", "RListType", compareYieldsBool >,

    < GtEq(), "RSetType", "RSetType", compareYieldsBool >,
    < GtEq(), "RSetType", "RRelType", compareYieldsBool >,
    < GtEq(), "RRelType", "RSetType", compareYieldsBool >,
    < GtEq(), "RRelType", "RRelType", compareYieldsBool >,

    < GtEq(), "RMapType", "RMapType", compareYieldsBool >,

    < Eq(), "RBoolType", "RBoolType", compareYieldsBool >,

    < Eq(), "RIntType", "RIntType", compareYieldsBool >,
    < Eq(), "RIntType", "RRealType", compareYieldsBool >,
    < Eq(), "RIntType", "RNumType", compareYieldsBool >,

    < Eq(), "RRealType", "RIntType", compareYieldsBool >,
    < Eq(), "RRealType", "RRealType", compareYieldsBool >,
    < Eq(), "RRealType", "RNumType", compareYieldsBool >,

    < Eq(), "RNumType", "RIntType", compareYieldsBool >,
    < Eq(), "RNumType", "RRealType", compareYieldsBool >,
    < Eq(), "RNumType", "RNumType", compareYieldsBool >,

    < Eq(), "RStrType", "RStrType", compareYieldsBool >,

    < Eq(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < Eq(), "RNodeType", "RNodeType", compareYieldsBool >,
    < Eq(), "RNodeType", "RADTType", compareYieldsBool >,

    < Eq(), "RLocType", "RLocType", compareYieldsBool >,

    < Eq(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < Eq(), "RTupleType", "RTupleType", compareYieldsBool >,

    < Eq(), "RADTType", "RConstructorType", compareYieldsBool >,
    < Eq(), "RADTType", "RNodeType", compareYieldsBool >,
    < Eq(), "RADTType", "RADTType", compareYieldsBool >,

    < Eq(), "RFunctionType", "RFunctionType", compareYieldsBool >,

    < Eq(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < Eq(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < Eq(), "RConstructorType", "RADTType", compareYieldsBool >,

    < Eq(), "RListType", "RListType", compareYieldsBool >,

    < Eq(), "RSetType", "RSetType", compareYieldsBool >,
    < Eq(), "RSetType", "RRelType", compareYieldsBool >,
    < Eq(), "RRelType", "RSetType", compareYieldsBool >,
    < Eq(), "RRelType", "RRelType", compareYieldsBool >,

    < Eq(), "RMapType", "RMapType", compareYieldsBool >,

    < Eq(), "RValueType", "RBoolType", compareYieldsBool >,
    < Eq(), "RValueType", "RIntType", compareYieldsBool >,
    < Eq(), "RValueType", "RRealType", compareYieldsBool >,
    < Eq(), "RValueType", "RNumType", compareYieldsBool >,
    < Eq(), "RValueType", "RStrType", compareYieldsBool >,
    < Eq(), "RValueType", "RValueType", compareYieldsBool >,
    < Eq(), "RValueType", "RNodeType", compareYieldsBool >,
    < Eq(), "RValueType", "RLocType", compareYieldsBool >,
    < Eq(), "RValueType", "RDateTimeType", compareYieldsBool >,
    < Eq(), "RValueType", "RTupleType", compareYieldsBool >,
    < Eq(), "RValueType", "RADTType", compareYieldsBool >,
    < Eq(), "RValueType", "RConstructorType", compareYieldsBool >,
    < Eq(), "RValueType", "RListType", compareYieldsBool >,
    < Eq(), "RValueType", "RSetType", compareYieldsBool >,
    < Eq(), "RValueType", "RBagType", compareYieldsBool >,
    < Eq(), "RValueType", "RRelType", compareYieldsBool >,
    < Eq(), "RValueType", "RMapType", compareYieldsBool >,

    < Eq(), "RBoolType", "RValueType", compareYieldsBool >,
    < Eq(), "RIntType", "RValueType", compareYieldsBool >,
    < Eq(), "RRealType", "RValueType", compareYieldsBool >,
    < Eq(), "RNumType", "RValueType", compareYieldsBool >,
    < Eq(), "RStrType", "RValueType", compareYieldsBool >,
    < Eq(), "RNodeType", "RValueType", compareYieldsBool >,
    < Eq(), "RLocType", "RValueType", compareYieldsBool >,
    < Eq(), "RDateTimeType", "RValueType", compareYieldsBool >,
    < Eq(), "RTupleType", "RValueType", compareYieldsBool >,
    < Eq(), "RADTType", "RValueType", compareYieldsBool >,
    < Eq(), "RConstructorType", "RValueType", compareYieldsBool >,
    < Eq(), "RListType", "RValueType", compareYieldsBool >,
    < Eq(), "RSetType", "RValueType", compareYieldsBool >,
    < Eq(), "RBagType", "RValueType", compareYieldsBool >,
    < Eq(), "RRelType", "RValueType", compareYieldsBool >,
    < Eq(), "RMapType", "RValueType", compareYieldsBool >,

    < NEq(), "RBoolType", "RBoolType", compareYieldsBool >,

    < NEq(), "RIntType", "RIntType", compareYieldsBool >,
    < NEq(), "RIntType", "RRealType", compareYieldsBool >,
    < NEq(), "RIntType", "RNumType", compareYieldsBool >,

    < NEq(), "RRealType", "RIntType", compareYieldsBool >,
    < NEq(), "RRealType", "RRealType", compareYieldsBool >,
    < NEq(), "RRealType", "RNumType", compareYieldsBool >,

    < NEq(), "RNumType", "RIntType", compareYieldsBool >,
    < NEq(), "RNumType", "RRealType", compareYieldsBool >,
    < NEq(), "RNumType", "RNumType", compareYieldsBool >,

    < NEq(), "RStrType", "RStrType", compareYieldsBool >,

    < NEq(), "RNodeType", "RConstructorType", compareYieldsBool >,
    < NEq(), "RNodeType", "RNodeType", compareYieldsBool >,
    < NEq(), "RNodeType", "RADTType", compareYieldsBool >,

    < NEq(), "RLocType", "RLocType", compareYieldsBool >,

    < NEq(), "RDateTimeType", "RDateTimeType", compareYieldsBool >,

    < NEq(), "RTupleType", "RTupleType", compareYieldsBool >,

    < NEq(), "RADTType", "RConstructorType", compareYieldsBool >,
    < NEq(), "RADTType", "RNodeType", compareYieldsBool >,
    < NEq(), "RADTType", "RADTType", compareYieldsBool >,

    < NEq(), "RConstructorType", "RConstructorType", compareYieldsBool >,
    < NEq(), "RConstructorType", "RNodeType", compareYieldsBool >,
    < NEq(), "RConstructorType", "RADTType", compareYieldsBool >,

    < NEq(), "RListType", "RListType", compareYieldsBool >,

    < NEq(), "RSetType", "RSetType", compareYieldsBool >,
    < NEq(), "RSetType", "RRelType", compareYieldsBool >,
    < NEq(), "RRelType", "RSetType", compareYieldsBool >,
    < NEq(), "RRelType", "RRelType", compareYieldsBool >,

    < NEq(), "RMapType", "RMapType", compareYieldsBool >,
    
    < NEq(), "RValueType", "RBoolType", compareYieldsBool >,
    < NEq(), "RValueType", "RIntType", compareYieldsBool >,
    < NEq(), "RValueType", "RRealType", compareYieldsBool >,
    < NEq(), "RValueType", "RNumType", compareYieldsBool >,
    < NEq(), "RValueType", "RStrType", compareYieldsBool >,
    < NEq(), "RValueType", "RValueType", compareYieldsBool >,
    < NEq(), "RValueType", "RNodeType", compareYieldsBool >,
    < NEq(), "RValueType", "RLocType", compareYieldsBool >,
    < NEq(), "RValueType", "RDateTimeType", compareYieldsBool >,
    < NEq(), "RValueType", "RTupleType", compareYieldsBool >,
    < NEq(), "RValueType", "RADTType", compareYieldsBool >,
    < NEq(), "RValueType", "RConstructorType", compareYieldsBool >,
    < NEq(), "RValueType", "RListType", compareYieldsBool >,
    < NEq(), "RValueType", "RSetType", compareYieldsBool >,
    < NEq(), "RValueType", "RBagType", compareYieldsBool >,
    < NEq(), "RValueType", "RRelType", compareYieldsBool >,
    < NEq(), "RValueType", "RMapType", compareYieldsBool >,

    < NEq(), "RBoolType", "RValueType", compareYieldsBool >,
    < NEq(), "RIntType", "RValueType", compareYieldsBool >,
    < NEq(), "RRealType", "RValueType", compareYieldsBool >,
    < NEq(), "RNumType", "RValueType", compareYieldsBool >,
    < NEq(), "RStrType", "RValueType", compareYieldsBool >,
    < NEq(), "RNodeType", "RValueType", compareYieldsBool >,
    < NEq(), "RLocType", "RValueType", compareYieldsBool >,
    < NEq(), "RDateTimeType", "RValueType", compareYieldsBool >,
    < NEq(), "RTupleType", "RValueType", compareYieldsBool >,
    < NEq(), "RADTType", "RValueType", compareYieldsBool >,
    < NEq(), "RConstructorType", "RValueType", compareYieldsBool >,
    < NEq(), "RListType", "RValueType", compareYieldsBool >,
    < NEq(), "RSetType", "RValueType", compareYieldsBool >,
    < NEq(), "RBagType", "RValueType", compareYieldsBool >,
    < NEq(), "RRelType", "RValueType", compareYieldsBool >,
    < NEq(), "RMapType", "RValueType", compareYieldsBool >,

    < In(), "RBoolType", "RListType", inList >,
    < In(), "RBoolType", "RSetType", inSet >,
    < In(), "RBoolType", "RRelType", inRel >,
    < In(), "RBoolType", "RMapType", inMap >,
    < In(), "RBoolType", "RBagType", inBag >,
    
    < In(), "RIntType", "RListType", inList >,
    < In(), "RIntType", "RSetType", inSet >,
    < In(), "RIntType", "RRelType", inRel >,
    < In(), "RIntType", "RMapType", inMap >,
    < In(), "RIntType", "RBagType", inBag >,

    < In(), "RRealType", "RListType", inList >,
    < In(), "RRealType", "RSetType", inSet >,
    < In(), "RRealType", "RRelType", inRel >,
    < In(), "RRealType", "RMapType", inMap >,
    < In(), "RRealType", "RBagType", inBag >,

    < In(), "RNumType", "RListType", inList >,
    < In(), "RNumType", "RSetType", inSet >,
    < In(), "RNumType", "RRelType", inRel >,
    < In(), "RNumType", "RMapType", inMap >,
    < In(), "RNumType", "RBagType", inBag >,

    < In(), "RStrType", "RListType", inList >,
    < In(), "RStrType", "RSetType", inSet >,
    < In(), "RStrType", "RRelType", inRel >,
    < In(), "RStrType", "RMapType", inMap >,
    < In(), "RStrType", "RBagType", inBag >,

    < In(), "RValueType", "RListType", inList >,
    < In(), "RValueType", "RSetType", inSet >,
    < In(), "RValueType", "RRelType", inRel >,
    < In(), "RValueType", "RMapType", inMap >,
    < In(), "RValueType", "RBagType", inBag >,

    < In(), "RNodeType", "RListType", inList >,
    < In(), "RNodeType", "RSetType", inSet >,
    < In(), "RNodeType", "RRelType", inRel >,
    < In(), "RNodeType", "RMapType", inMap >,
    < In(), "RNodeType", "RBagType", inBag >,

    < In(), "RLocType", "RListType", inList >,
    < In(), "RLocType", "RSetType", inSet >,
    < In(), "RLocType", "RRelType", inRel >,
    < In(), "RLocType", "RMapType", inMap >,
    < In(), "RLocType", "RBagType", inBag >,

    < In(), "RDateTimeType", "RListType", inList >,
    < In(), "RDateTimeType", "RSetType", inSet >,
    < In(), "RDateTimeType", "RRelType", inRel >,
    < In(), "RDateTimeType", "RMapType", inMap >,
    < In(), "RDateTimeType", "RBagType", inBag >,

    < In(), "RTupleType", "RListType", inList >,
    < In(), "RTupleType", "RSetType", inSet >,
    < In(), "RTupleType", "RRelType", inRel >,
    < In(), "RTupleType", "RMapType", inMap >,
    < In(), "RTupleType", "RBagType", inBag >,

    < In(), "RADTType", "RListType", inList >,
    < In(), "RADTType", "RSetType", inSet >,
    < In(), "RADTType", "RRelType", inRel >,
    < In(), "RADTType", "RMapType", inMap >,
    < In(), "RADTType", "RBagType", inBag >,

    < In(), "RConstructorType", "RListType", inList >,
    < In(), "RConstructorType", "RSetType", inSet >,
    < In(), "RConstructorType", "RRelType", inRel >,
    < In(), "RConstructorType", "RMapType", inMap >,
    < In(), "RConstructorType", "RBagType", inBag >,

    < In(), "RListType", "RListType", inList >,
    < In(), "RListType", "RSetType", inSet >,
    < In(), "RListType", "RRelType", inRel >,
    < In(), "RListType", "RMapType", inMap >,
    < In(), "RListType", "RBagType", inBag >,

    < In(), "RSetType", "RListType", inList >,
    < In(), "RSetType", "RSetType", inSet >,
    < In(), "RSetType", "RRelType", inRel >,
    < In(), "RSetType", "RMapType", inMap >,
    < In(), "RSetType", "RBagType", inBag >,

    < In(), "RBagType", "RListType", inList >,
    < In(), "RBagType", "RSetType", inSet >,
    < In(), "RBagType", "RRelType", inRel >,
    < In(), "RBagType", "RMapType", inMap >,
    < In(), "RBagType", "RBagType", inBag >,

    < In(), "RRelType", "RListType", inList >,
    < In(), "RRelType", "RSetType", inSet >,
    < In(), "RRelType", "RRelType", inRel >,
    < In(), "RRelType", "RMapType", inMap >,
    < In(), "RRelType", "RBagType", inBag >,

    < In(), "RMapType", "RListType", inList >,
    < In(), "RMapType", "RSetType", inSet >,
    < In(), "RMapType", "RRelType", inRel >,
    < In(), "RMapType", "RMapType", inMap >,
    < In(), "RMapType", "RBagType", inBag >,

    < NotIn(), "RBoolType", "RListType", inList >,
    < NotIn(), "RBoolType", "RSetType", inSet >,
    < NotIn(), "RBoolType", "RRelType", inRel >,
    < NotIn(), "RBoolType", "RMapType", inMap >,
    < NotIn(), "RBoolType", "RBagType", inBag >,

    < NotIn(), "RIntType", "RListType", inList >,
    < NotIn(), "RIntType", "RSetType", inSet >,
    < NotIn(), "RIntType", "RRelType", inRel >,
    < NotIn(), "RIntType", "RMapType", inMap >,
    < NotIn(), "RIntType", "RBagType", inBag >,

    < NotIn(), "RRealType", "RListType", inList >,
    < NotIn(), "RRealType", "RSetType", inSet >,
    < NotIn(), "RRealType", "RRelType", inRel >,
    < NotIn(), "RRealType", "RMapType", inMap >,
    < NotIn(), "RRealType", "RBagType", inBag >,

    < NotIn(), "RNumType", "RListType", inList >,
    < NotIn(), "RNumType", "RSetType", inSet >,
    < NotIn(), "RNumType", "RRelType", inRel >,
    < NotIn(), "RNumType", "RMapType", inMap >,
    < NotIn(), "RNumType", "RBagType", inBag >,

    < NotIn(), "RStrType", "RListType", inList >,
    < NotIn(), "RStrType", "RSetType", inSet >,
    < NotIn(), "RStrType", "RRelType", inRel >,
    < NotIn(), "RStrType", "RMapType", inMap >,
    < NotIn(), "RStrType", "RBagType", inBag >,

    < NotIn(), "RValueType", "RListType", inList >,
    < NotIn(), "RValueType", "RSetType", inSet >,
    < NotIn(), "RValueType", "RRelType", inRel >,
    < NotIn(), "RValueType", "RMapType", inMap >,
    < NotIn(), "RValueType", "RBagType", inBag >,

    < NotIn(), "RNodeType", "RListType", inList >,
    < NotIn(), "RNodeType", "RSetType", inSet >,
    < NotIn(), "RNodeType", "RRelType", inRel >,
    < NotIn(), "RNodeType", "RMapType", inMap >,
    < NotIn(), "RNodeType", "RBagType", inBag >,

    < NotIn(), "RLocType", "RListType", inList >,
    < NotIn(), "RLocType", "RSetType", inSet >,
    < NotIn(), "RLocType", "RRelType", inRel >,
    < NotIn(), "RLocType", "RMapType", inMap >,
    < NotIn(), "RLocType", "RBagType", inBag >,

    < NotIn(), "RDateTimeType", "RListType", inList >,
    < NotIn(), "RDateTimeType", "RSetType", inSet >,
    < NotIn(), "RDateTimeType", "RRelType", inRel >,
    < NotIn(), "RDateTimeType", "RMapType", inMap >,
    < NotIn(), "RDateTimeType", "RBagType", inBag >,
    
    < NotIn(), "RTupleType", "RListType", inList >,
    < NotIn(), "RTupleType", "RSetType", inSet >,
    < NotIn(), "RTupleType", "RRelType", inRel >,
    < NotIn(), "RTupleType", "RMapType", inMap >,
    < NotIn(), "RTupleType", "RBagType", inBag >,

    < NotIn(), "RADTType", "RListType", inList >,
    < NotIn(), "RADTType", "RSetType", inSet >,
    < NotIn(), "RADTType", "RRelType", inRel >,
    < NotIn(), "RADTType", "RMapType", inMap >,
    < NotIn(), "RADTType", "RBagType", inBag >,

    < NotIn(), "RConstructorType", "RListType", inList >,
    < NotIn(), "RConstructorType", "RSetType", inSet >,
    < NotIn(), "RConstructorType", "RRelType", inRel >,
    < NotIn(), "RConstructorType", "RMapType", inMap >,
    < NotIn(), "RConstructorType", "RBagType", inBag >,
    
    < NotIn(), "RListType", "RListType", inList >,
    < NotIn(), "RListType", "RSetType", inSet >,
    < NotIn(), "RListType", "RRelType", inRel >,
    < NotIn(), "RListType", "RMapType", inMap >,
    < NotIn(), "RListType", "RBagType", inBag >,

    < NotIn(), "RSetType", "RListType", inList >,
    < NotIn(), "RSetType", "RSetType", inSet >,
    < NotIn(), "RSetType", "RRelType", inRel >,
    < NotIn(), "RSetType", "RMapType", inMap >,
    < NotIn(), "RSetType", "RBagType", inBag >,

    < NotIn(), "RBagType", "RListType", inList >,
    < NotIn(), "RBagType", "RSetType", inSet >,
    < NotIn(), "RBagType", "RRelType", inRel >,
    < NotIn(), "RBagType", "RMapType", inMap >,
    < NotIn(), "RBagType", "RBagType", inBag >,

    < NotIn(), "RRelType", "RListType", inList >,
    < NotIn(), "RRelType", "RSetType", inSet >,
    < NotIn(), "RRelType", "RRelType", inRel >,
    < NotIn(), "RRelType", "RMapType", inMap >,
    < NotIn(), "RRelType", "RBagType", inBag >,

    < NotIn(), "RMapType", "RListType", inList >,
    < NotIn(), "RMapType", "RSetType", inSet >,
    < NotIn(), "RMapType", "RRelType", inRel >,
    < NotIn(), "RMapType", "RMapType", inMap >,
    < NotIn(), "RMapType", "RBagType", inBag >,

    < Intersect(), "RRelationType", "RRelationType", relIntersectRel >,
    < Intersect(), "RRelationType", "RSetType", relIntersectSet >,

    < Intersect(), "RSetType", "RRelationType", setIntersectRel >,
    < Intersect(), "RSetType", "RSetType", setIntersectSet >,

    < Intersect(), "RMapType", "RMapType", mapIntersectMap >,

    < Join(), "RRelType", "RRelType", relJoinRel >,
    < Join(), "RRelType", "RSetType", relJoinSet >,
    < Join(), "RSetType", "RRelType", setJoinRel >,            
    < Join(), "RSetType", "RSetType", setJoinSet >            
    
};
