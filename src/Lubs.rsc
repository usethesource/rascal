@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::Lubs

import List;
import Set;
import IO;
import Node;

import lang::rascal::types::Types;
import lang::rascal::types::TypeExceptions;
import lang::rascal::types::SubTypes;

private RType defaultLub(RType t1, RType t2) {
    if (t1 == t2) return t1;

    if (isVoidType(t2)) return t1;

    if (isAliasType(t1)) return lub(getAliasedType(t1),t2);
    if (isAliasType(t2)) return lub(t1, getAliasedType(t2));
    if (isTypeVar(t2)) return lub(t1, getTypeVarBound(t2));

    return makeValueType();
}

// Types: value, loc, node, str, bool, reified, datetime
public default RType lub(RType t1, RType t2) = defaultLub(t1, t2);

// Type: num
public RType lub(RNumType(), RNumType()) = RNumType();
public RType lub(RNumType(), RIntType()) = RNumType();
public RType lub(RNumType(), RRealType()) = RNumType();
public RType lub(RNumType(), RRatType()) = RNumType();

// Type: type
public RType lub(RReifiedType(RType t1), RReifiedType(RType t2)) = makeReifiedType(lub(t1,t2));

// Type: bag
public RType lub(RBagType(RType t1), RBagType(RType t2)) = RBagType(lub(t1,t2));

// Type: int
public RType lub(RIntType(), RIntType()) = RIntType();
public RType lub(RIntType(), RRealType()) = RNumType();
public RType lub(RIntType(), RNumType()) = RNumType();
public RType lub(RIntType(), RRatType()) = RNumType();

// Type: rat
public RType lub(RRatType(), RRatType()) = RRatType();
public RType lub(RRatType(), RIntType()) = RNumType();
public RType lub(RRatType(), RRealType()) = RNumType();
public RType lub(RRatType(), RNumType()) = RNumType();

// Type: rel
public RType lub(RType t1:RRelType(_), RType t2:RRelType(_)) {
    RType lubbedType = lub(getRelElementType(t1),getRelElementType(t2));
    if (isTupleType(lubbedType)) {
        return makeRelTypeFromTuple(lubbedType);
    } else {
        return makeSetType(lubbedType);
	}
}
    
// Type: parameter
public RType lub(RType t1:RTypeVar(_,RType tb), RType t2) = (t1 == t2) ? t1 : lub(tb, t2);

// Type: real
public RType lub(RRealType(), RRealType()) = RRealType();
public RType lub(RRealType(), RIntType()) = RNumType();
public RType lub(RRealType(), RNumType()) = RNumType();
public RType lub(RRealType(), RRatType()) = RNumType();

// Type: fun
public RType lub(RType t1:RFunctionType(_,_,_), RType t2:RFunctionType(_,_,_)) {
    if (t1 == t2) return t1;
    
    // TODO! The rules for lubs of functions make no sense to me...
        
    if (isOverloadedType(t2)) {
        return lub(t2, t1);
    }

    return defaultLub(t1, t2);
}

// Type: tuple
public RType lub(RType t1:RTupleType(_), RType t2:RTupleType(_)) {
    if (getTupleFieldCount(t1) == getTupleFieldCount(t2)) {
        if (getTupleFieldCount(t1) == 0) return RTupleType([ ]);

        list[RType] tFields1 = getTupleFields(t1);
        list[RType] tFields2 = getTupleFields(t2);
        list[RType] fieldLubs = [lub(tFields1[n], tFields2[n]) | n <- [0..size(tFields1)-1]];
        
        if (tupleHasFieldNames(t1) && tupleHasFieldNames(t2)) {
            list[RName] names1 = getTupleFieldNames(t1);
            list[RName] names2 = getTupleFieldNames(t2);
            if (size([n | n <- [0..size(names1)-1], names1[n] != names2[n]]) > 0) {
                return RTupleType([ RUnnamedType(rt) | rt <- fieldLubs]);
            } else {
                return RTupleType([ RNamedType(fieldLubs[n],names1[n]) | n <- [0..size(names1)-1] ]);
            }
        } else if (tupleHasFieldNames(t1)) {
            list[RName] names1 = getTupleFieldNames(t1);
            return RTupleType([ RNamedType(fieldLubs[n],names1[n]) | n <- [0..size(names1)-1] ]);
        } else if (tupleHasFieldNames(t2)) {
            list[RName] names2 = getTupleFieldNames(t2);
            return RTupleType([ RNamedType(fieldLubs[n],names2[n]) | n <- [0..size(names2)-1] ]);
        } else {
            return RTupleType([ RUnnamedType(rt) | rt <- fieldLubs]);
        }
    }
    
    return defaultLub(t1, t2);
}

// Type: void
public RType lub(RVoidType(), RType t2) = t2;

// Type: non-terminal type
public RType lub(RType t1:RNonTerminalType(), RType t2) {
    if (t1 == t2) return t1;
    
    if (subtypeOf(t2,makeADTType(RSimpleName("Tree")))) 
        return makeADTType(RSimpleName("Tree"));
        
    return defaultLub(t1, t2);
}

// Type: set
public RType lub(RSetType(RType t1), RSetType(RType t2)) = RSetType(lub(t1,t2));

// Type: map
public RType lub(RType t1:RMapType(_,_), RType t2:RMapType(_,_)) = makeMapTypeFromTuple(lub(getMapFieldsAsTuple(t1),getMapFieldsAsTuple(t2)));

// Type: constructor
public RType lub(RType t1:RConstructorType(_,_,_), RType t2) {
    if (isConstructorType(t2)) return lub(getConstructorResultType(t1),getConstructorResultType(t2));
    
    if (isADTType(t2)) return lub(getConstructorResultType(t1), t2);
    
    if (isNodeType(t2)) return t2;
    
    return defaultLub(t1,t2);
}

// Type: list
public RType lub(RListType(RType t1), RListType(RType t2)) = RListType(lub(t1,t2));

// Type: adt
public RType lub(RType t1:RADTType(_,_), RType t2) {
    if (t1 == t2) return t1;
    
    if (isVoidType(t2)) return t1;
    
    if (isADTType(t2) && getADTName(t1) == getADTName(t2)) {
        paramLubs = lub(makeTupleType(getADTTypeParameters(t1)), makeTupleType(getADTTypeParameters(t2)));
        return makeParameterizedADTType(getADTName(t1), getTupleFields(paramLubs));
    }

    if (isConstructorType(t2) && getADTName(t1) == getADTName(t2)) {
        paramLubs = lub(makeTupleType(getADTTypeParameters(t1)), makeTupleType(getADTTypeParameters(t2)));
        return makeParameterizedADTType(getADTName(t1), getTupleFields(paramLubs));
    }
             
    return lub(makeNodeType(), t2);
}

//
// Based on implementation of lub in AliasType
//
public RType lub(RType t1:RAliasType(_,_,_), RType t2) {
    if (t1 == t2) return t1;
    
    if (isAliasType(t2) && getAliasName(t1) == getAliasName(t2)) {
        RType aliased = lub(getAliasedType(t1), getAliasedType(t2));
        RType paramLubs = lub(makeTupleType(getAliasTypeParameters(t1)),makeTupleType(getAliasTypeParameters(t2)));
        return makeParameterizedAliasType(getAliasName(t1),aliased,paramLubs);
    }
    
    return lub(getAliasedType(t1),t2);
}

//
// Based on implementation of lub in OverloadedFunctionType
//
public RType lub(RType t1:ROverloadedType(_), RType t2) {
    if (t1 == t2) return t1;

    // TODO: The lub code for OverloadedFunctionType doesn't make sense    
    return defaultLub(t1,t2);
}

//
// Calculate the least upper bound of an entire list of types
//
public RType lubList(list[RType] tl) {
    RType resultType = makeVoidType();
    for (t <- tl) resultType = lub(resultType,t);
    return resultType; // TODO: Switch to reduce syntax...
}

//
// Calculate the least upper bound of an entire set of types
//
public RType lubSet(set[RType] tl) {
    RType resultType = makeVoidType();
    for (t <- tl) resultType = lub(resultType,t);
    return resultType; 
}

// Tests to make sure a lub a == a.
test bool tl1() = lub(makeIntType(), makeIntType()) == makeIntType();
test bool tl2() = lub(makeRealType(), makeRealType()) == makeRealType();
test bool tl3() = lub(makeNumType(), makeNumType()) == makeNumType();
test bool tl4() = lub(makeDateTimeType(), makeDateTimeType()) == makeDateTimeType();
test bool tl5() = lub(makeStrType(), makeStrType()) == makeStrType();

// Num tests
test bool tl6() = lub(makeRealType(), makeIntType()) == makeNumType();
test bool tl7() = lub(makeIntType(), makeRealType()) == makeNumType();
test bool tl8() = lub(makeIntType(), makeNumType()) == makeNumType();
test bool tl9() = lub(makeNumType(), makeIntType()) == makeNumType();
test bool tl10() = lub(makeRealType(), makeNumType()) == makeNumType();
test bool tl11() = lub(makeNumType(), makeRealType()) == makeNumType();

// Value tests
test bool tl12() = lub(makeNumType(), makeStrType()) == makeValueType();

// Check aliases
test bool tl13() = lub(makeAliasType(RSimpleName("A"),makeIntType()), makeIntType()) == makeIntType();
test bool tl14() = lub(makeIntType(), makeAliasType(RSimpleName("A"),makeIntType())) == makeIntType();
test bool tl15() = lub(makeAliasType(RSimpleName("A"),makeIntType()), makeAliasType(RSimpleName("B"),makeIntType())) == makeIntType();

