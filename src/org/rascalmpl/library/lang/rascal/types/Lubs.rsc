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

///////////////////////////////////////////////////////////////////////////////////////////
//
// Individual methods that compute least upper bounds for each type available in Rascal
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// The base routine called in many cases when we don't have another match...
//
private RType lubOfBase(RType t1, RType t2) {
    if (t1 == t2) return t1;
    if (isVoidType(t2)) return t1;
    if (isAliasType(t2)) return lub(t1, getAliasedType(t2));
    return makeValueType();
}

//
// Based on implementation of lub in ValueType
//
private RType lubOfValueType(RType t1, RType t2) {
    return t1;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType lubOfLocType(RType t1, RType t2) {
    return lubOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType lubOfNodeType(RType t1, RType t2) {
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in NumberType
//
private RType lubOfNumType(RType t1, RType t2) {
    if (isNumType(t2) || isIntType(t2) || isRealType(t2)) return t1;
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in ReifiedType
//
private RType lubOfReifiedType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    if (isReifiedType(t2)) return makeReifiedType(lub(getReifiedType(t1),getReifiedType(t2)));
    return lubOfBase(t1,t2);
}

//
// This type does not yet exist, so marking it as an unexpected case
//
// TODO: Add implementation when we have support for this in Rascal
//
private RType lubOfBagType(RType t1, RType t2) {
    throw UnimplementedRType(t1);
}

//
// Based on implementation of lub in IntegerType
//
private RType lubOfIntType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    if (isRealType(t2) || isNumType(t2)) return makeNumType();
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in RelationType
//
private RType lubOfRelType(RType t1, RType t2) {
    if (isRelType(t2)) {
        RType lubbedType = lub(getRelElementType(t1),getRelElementType(t2));
        if (isTupleType(lubbedType))
            return makeRelTypeFromTuple(lubbedType);
        else
            return makeSetType(lubbedType);
    }
    
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in ParameterType
//
private RType lubOfTypeVarType(RType t1, RType t2) {
    if (t1 == t2) return true;
    return lub(getTypeVarBound(t1),t2);
}

//
// Based on implementation of lub in RealType
//
private RType lubOfRealType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    if (isIntType(t2) || isNumType(t2)) return makeNumType();
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in FunctionType
//
private RType lubOfFunctionType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    
    // TODO! The rules for lubs of functions make no sense to me...
        
    if (isOverloadedType(t2)) {
        return lub(t2, t1);
    }


    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in TupleType
//
private RType lubOfTupleType(RType t1, RType t2) {
    if (isTupleType(t2) && getTupleFieldCount(t1) == getTupleFieldCount(t2)) {
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
            return RTupleType([ RNamedType(fieldLubs[n],names1[n]) | n <- [0..size(names1)-1] ]);
        } else if (tupleHasFieldNames(t2)) {
            return RTupleType([ RNamedType(fieldLubs[n],names2[n]) | n <- [0..size(names2)-1] ]);
        } else {
            return RTupleType([ RUnnamedType(rt) | rt <- fieldLubs]);
        }
    }
    
    return lubOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType lubOfStrType(RType t1, RType t2) {
    return lubOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType lubOfBoolType(RType t1, RType t2) {
    return lubOfBase(t1,t2);
}

//
// TODO: reified reified types don't appear to be implemented yet; we will
// need to add support here once we figure out what this support should
// look like.
//
private RType lubOfReifiedReifiedType(RType t1, RType t2) {
    throw UnimplementedRType(t1);
}

//
// Based on implementation of lub in VoidType
//
private RType lubOfVoidType(RType t1, RType t2) {
    return t2;
}

//
// Based on implementation of lub in NonTerminalType
//
private RType lubOfNonTerminalType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    
    if (subtypeOf(t2,makeADTType(RSimpleName("Tree")))) 
        return makeADTType(RSimpleName("Tree"));
        
    return lubOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType lubOfDateTimeType(RType t1, RType t2) {
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in SetType
//
private RType lubOfSetType(RType t1, RType t2) {
    if (isSetType(t2)) return makeSetType(lub(getSetElementType(t1),getSetElementType(t2)));
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in MapType
//
private RType lubOfMapType(RType t1, RType t2) {
    if (isMapType(t2))
        return makeMapTypeFromTuple(lub(getMapFieldsAsTuple(t1),getMapFieldsAsTuple(t2)));
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in ConstructorType
//
private RType lubOfConstructorType(RType t1, RType t2) {
    if (isConstructorType(t2)) return lub(getConstructorResultType(t1),getConstructorResultType(t2));
    
    if (isADTType(t2)) return lub(getConstructorResultType(t1), t2);
    
    if (isNodeType(t2)) return t2;
    
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in ListType
//
private RType lubOfListType(RType t1, RType t2) {
    if (isListType(t2)) return makeListType(lub(getListElementType(t1), getListElementType(t2)));
    return lubOfBase(t1,t2);
}

//
// Based on implementation of lub in AbstractDataType
//
private RType lubOfADTType(RType t1, RType t2) {
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
// lex is not yet a supported type from what I can tell (MAH)
//
// TODO: Add support if/when this is a supported type
//
private RType lubOfLexType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter a user type in a lub computation,
// we should have always resolved it to an actual type (ADT, alias)
// at this point.
//
private RType lubOfUserType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of lub in AliasType
//
private RType lubOfAliasType(RType t1, RType t2) {
    if (t1 == t2) return t1;
    
    if (isAliasType(t2) && getAliasName(t1) == getAliasName(t2)) {
        RType aliased = lub(getAliasedType(t1), getAliasedType(t2));
        RType paramLubs = lub(makeTupleType(getAliasTypeParameters(t1)),makeTupleType(getAliasTypeParameters(t2)));
        return makeParameterizedAliasType(getAliasName(t1),aliased,paramLubs);
    }
    
    return lub(getAliasedType(t1),t2);
}

//
// We should never encounter this type in a lub computation
//
private RType lubOfDataTypeSelectorType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a lub computation
//
private RType lubOfFailType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a lub computation,
// it should always be instantiated first
//
private RType lubOfInferredType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of lub in OverloadedFunctionType
//
private RType lubOfOverloadedType(RType t1, RType t2) {
    if (t1 == t2) return t1;

    // TODO: The lub code for OverloadedFunctionType doesn't make sense    
    return lubOfBase(t1,t2);
}

//
// We should never encounter this type in a lub computation
//
private RType lubOfStatementType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// Main lub routine
//
private map[str,RType(RType,RType)] lubHandlers = (
    "RValueType" : lubOfValueType,
    "RLocType" : lubOfLocType,
    "RNodeType" : lubOfNodeType,
    "RNumType" : lubOfNumType,
    "RReifiedType" : lubOfReifiedType,
    "RBagType" : lubOfBagType,
    "RIntType" : lubOfIntType,
    "RRelType" : lubOfRelType,
    "RTypeVar" : lubOfTypeVarType,
    "RRealType" : lubOfRealType,
    "RFunctionType" : lubOfFunctionType,
    "RTupleType" : lubOfTupleType,
    "RStrType" : lubOfStrType,
    "RBoolType" : lubOfBoolType,
    "RReifiedReifiedType" : lubOfReifiedReifiedType,
    "RVoidType" : lubOfVoidType,
    "RNonTerminalType" : lubOfNonTerminalType,
    "RDateTimeType" : lubOfDateTimeType,
    "RSetType" : lubOfSetType,
    "RMapType" : lubOfMapType,
    "RConstructorType" : lubOfConstructorType,
    "RListType" : lubOfListType,
    "RADTType" : lubOfADTType,
    "RLexType" : lubOfLexType,
    "RUserType" : lubOfUserType,
    "RAliasType" : lubOfAliasType,
    "RDataTypeSelector" : lubOfDataTypeSelectorType,
    "RFailType" : lubOfFailType,
    "RInferredType" : lubOfInferredType,
    "ROverloadedType" : lubOfOverloadedType,
    "RStatementType" : lubOfStatementType
);

public RType lub(RType t1, RType t2) {
    if (getName(t1) in lubHandlers) 
        return (lubHandlers[getName(t1)])(t1,t2);
    else
        throw UnexpectedRType(t1);
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

//
// TODO: Add LOTS of tests!
//

// Tests to make sure a lub a == a.
test lub(makeIntType(), makeIntType()) == makeIntType();
test lub(makeRealType(), makeRealType()) == makeRealType();
test lub(makeNumType(), makeNumType()) == makeNumType();
test lub(makeDateTimeType(), makeDateTimeType()) == makeDateTimeType();
test lub(makeStrType(), makeStrType()) == makeStrType();

// Num tests
test lub(makeRealType(), makeIntType()) == makeNumType();
test lub(makeIntType(), makeRealType()) == makeNumType();
test lub(makeIntType(), makeNumType()) == makeNumType();
test lub(makeNumType(), makeIntType()) == makeNumType();
test lub(makeRealType(), makeNumType()) == makeNumType();
test lub(makeNumType(), makeRealType()) == makeNumType();

// Value tests
test lub(makeNumType(), makeStrType()) == makeValueType();

// Check aliases
test lub(makeAliasType(RSimpleName("A"),makeIntType()), makeIntType()) == makeIntType();
test lub(makeIntType(), makeAliasType(RSimpleName("A"),makeIntType())) == makeIntType();
test lub(makeAliasType(RSimpleName("A"),makeIntType()), makeAliasType(RSimpleName("B"),makeIntType())) == makeIntType();

