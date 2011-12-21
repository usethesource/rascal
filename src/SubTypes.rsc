@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::SubTypes

import List;
import Set;
import IO;
import Node;

import lang::rascal::types::Types;
import lang::rascal::types::TypeExceptions;

///////////////////////////////////////////////////////////////////////////////////
//
// Individual methods that compute subtyping for each type available in Rascal
//
///////////////////////////////////////////////////////////////////////////////////

//
// The base routine called in many cases when we don't have another match...
//
private bool subtypeOfBase(RType t1, RType t2) {
    if (isValueType(t2)) return true;
    if (t1 == t2) return true;
    if (isAliasType(t2)) return subtypeOf(t1, getAliasedType(t2));
    if (isTypeVar(t2)) return subtypeOf(t2, getTypeVarBound(t2));
    return false;
}

//
// Based on implementation of isSubtypeOf in ValueType
//
private bool subtypeOfValueType(RType t1, RType t2) {
    if (t1 == t2) return true;
    if (isVoidType(t2)) return false;
    if (isAliasType(t2)) return subtypeOf(t1, getAliasedType(t2));
    if (isTypeVar(t2)) return subtypeOf(t2, getTypeVarBound(t2));
    return false;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfLocType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfNodeType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfNumType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in ReifiedType
//
private bool subtypeOfReifiedType(RType t1, RType t2) {
    if (isReifiedType(t2)) return subtypeOf(getReifiedType(t1),getReifiedType(t2));
    if (isNodeType(t2)) return true;
    return subtypeOfBase(t1,t2);
}

//
// This type does not yet exist, so marking it as an unexpected case
//
// TODO: Add implementation when we have support for this in Rascal
//
private bool subtypeOfBagType(RType t1, RType t2) {
    throw UnimplementedRType(t1);
}

//
// Based on implementation of isSubtypeOf in IntegerType
//
private bool subtypeOfIntType(RType t1, RType t2) {
    if (t1 == t2 || isNumType(t2)) return true;
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in RelationType
//
private bool subtypeOfRelType(RType t1, RType t2) {
    if (isRelType(t2)) return subtypeOf(getRelElementType(t1),getRelElementType(t2));
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in ParameterType
//
private bool subtypeOfTypeVarType(RType t1, RType t2) {
    if (t1 == t2) return true;
    return subtypeOf(getTypeVarBound(t1),t2);
}

//
// Based on implementation of isSubtypeOf in RealType
//
private bool subtypeOfRealType(RType t1, RType t2) {
    if (t1 == t2 || isNumType(t2)) return true;
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in FunctionType
//
// TODO: We need to match t2 against t1 to find bindings for type parameters
// in t2, and then use the bindings to instantiate t2 for another subtype
// test. This is currently done in Rascal.
//
// TODO: Experiment with this, since it seems wrong. For instance, if
// we have a function with type int(&T <: bool, int), a function with
// type int(int,int) should not be a subtype -- in fact, the two are
// not comparable. This seems to be allowed now, though, just based on
// my (MAH) reading of the code.  
//
private bool subtypeOfFunctionType(RType t1, RType t2) {
    if (t1 == t2) return true;

    if (isFunctionType(t2)) {
        if (subtypeOf(getFunctionReturnType(t1),getFunctionReturnType(t2))) {
            if (subtypeOf(makeTupleType(getFunctionArgumentTypes(t1)),makeTupleType(getFunctionArgumentTypes(t2)))) {
                return true;            
            }
        }
        
        return false;
    }

    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in TupleType
//
private bool subtypeOfTupleType(RType t1, RType t2) {
    if (t1 == t2) return true;
    if (isTupleType(t2)) {
        list[RType] te1 = getTupleFields(t1);
        list[RType] te2 = getTupleFields(t2);
        
        if (size(te1) == size(te2)) {
            if (size(te1) > 0) {
                for (n <- [0..size(te1)-1]) {
                    if (! subtypeOf(te1[n],te2[n])) return false;
                }
            }
            return true;
        }
    }
    return subtypeOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfStrType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfBoolType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// TODO: reified reified types don't appear to be implemented yet; we will
// need to add support here once we figure out what this support should
// look like.
//
private bool subtypeOfReifiedReifiedType(RType t1, RType t2) {
    throw UnimplementedRType(t1);
}

//
// Based on implementation of isSubtypeOf in VoidType
//
private bool subtypeOfVoidType(RType t1, RType t2) {
    return true;
}

//
// TODO: We check here to see if t2 is of type Tree, but, what if
// we haven't loaded ParseTree but have instead defined our own
// ADT?
//
private bool subtypeOfNonTerminalType(RType t1, RType t2) {
    if (t1 == t2) return true;
    
    if (isADTType(t2) && getADTName(t2) == RSimpleName("Tree") && !adtHasTypeParameters(t2)) 
        return true;
        
    // TODO: Finish. We may need to change the representation
    // of this type to do so...
    return subtypeOfBase(t1,t2);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private bool subtypeOfDateTimeType(RType t1, RType t2) {
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in SetType
//
private bool subtypeOfSetType(RType t1, RType t2) {
    if (isSetType(t2)) return subtypeOf(getSetElementType(t1),getSetElementType(t2));
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in MapType
//
private bool subtypeOfMapType(RType t1, RType t2) {
    if (isMapType(t2)) 
        return subtypeOf(getMapDomainType(t1),getMapDomainType(t2)) && subtypeOf(getMapRangeType(t1),getMapRangeType(t2));
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in ConstructorType
//
private bool subtypeOfConstructorType(RType t1, RType t2) {
    if (t1 == t2) return true;
    if (isADTType(t2), getConstructorResultType(t1) == t2) return true; 
    return subtypeOf(getConstructorResultType(t1),t2);
}

//
// Based on implementation of isSubtypeOf in ListType
//
private bool subtypeOfListType(RType t1, RType t2) {
    if (isListType(t2)) return subtypeOf(getListElementType(t1), getListElementType(t2));
    return subtypeOfBase(t1,t2);
}

//
// Based on implementation of isSubtypeOf in AbstractDataType
//
private bool subtypeOfADTType(RType t1, RType t2) {
    if (t1 == t2) return true;
    if (isValueType(t2)) return true;
    if (isADTType(t2) && getADTName(t1) == getADTName(t2))
        return subtypeOf(makeTupleType(getADTTypeParameters(t1)),makeTupleType(getADTTypeParameters(t2)));
    if (isAliasType(t2)) return subtypeOf(t1, getAliasedType(t2));
    return subtypeOf(makeNodeType(), t2);
}

//
// lex is not yet a supported type from what I can tell (MAH)
//
// TODO: Add support if/when this is a supported type
//
private bool subtypeOfLexType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter a user type in a subtype comparison,
// we should have always resolved it to an actual type (ADT, alias)
// at this point.
//
private bool subtypeOfUserType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of isSubtypeOf in AliasType
//
private bool subtypeOfAliasType(RType t1, RType t2) {
    if (t1 == t2) return true;
    if (isAliasType(t2) && getAliasName(t1) == getAliasName(t2))
        return subtypeOf(getAliasedType(t1),t2) && subtypeOf(makeTupleType(getAliasTypeParameters(t1)),makeTupleType(getAliasTypeParameters(t2)));
    return subtypeOf(getAliasedType(t1),t2);
}

//
// We should never encounter this type in a subtype comparision
//
private bool subtypeOfDataTypeSelectorType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a subtype comparision
//
private bool subtypeOfFailType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a subtype comparision,
// it should always be instantiated first
//
private bool subtypeOfInferredType(RType t1, RType t2) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of isSubtypeOf in OverloadedFunctionType
//
private bool subtypeOfOverloadedType(RType t1, RType t2) {
    if (t1 == t2) return true;
    
    if (isOverloadedType(t2)) {
        set[RType] t2Options = getOverloadOptions(t2);
        if (size({ t | t <- t2Options, subtypeOf(t1,t) }) > 0) return true;
        return false;
    }
    
    if (isFunctionType(t2)) {
        set[RType] t1Options = getOverloadOptions(t1);
        if (size({ t | t <- t1Options, subtypeOf(t,t2) }) > 0) return true;
        return false;    
    }

    return subtypeOfBase(t1,t2);
}

//
// Main subtypeOf routine
//
private map[str,bool(RType,RType)] subtypeHandlers = (
    "RValueType" : subtypeOfValueType,
    "RLocType" : subtypeOfLocType,
    "RNodeType" : subtypeOfNodeType,
    "RNumType" : subtypeOfNumType,
    "RReifiedType" : subtypeOfReifiedType,
    "RBagType" : subtypeOfBagType,
    "RIntType" : subtypeOfIntType,
    "RRelType" : subtypeOfRelType,
    "RTypeVar" : subtypeOfTypeVarType,
    "RRealType" : subtypeOfRealType,
    "RFunctionType" : subtypeOfFunctionType,
    "RTupleType" : subtypeOfTupleType,
    "RStrType" : subtypeOfStrType,
    "RBoolType" : subtypeOfBoolType,
    "RReifiedReifiedType" : subtypeOfReifiedReifiedType,
    "RVoidType" : subtypeOfVoidType,
    "RNonTerminalType" : subtypeOfNonTerminalType,
    "RDateTimeType" : subtypeOfDateTimeType,
    "RSetType" : subtypeOfSetType,
    "RMapType" : subtypeOfMapType,
    "RConstructorType" : subtypeOfConstructorType,
    "RListType" : subtypeOfListType,
    "RADTType" : subtypeOfADTType,
    "RLexType" : subtypeOfLexType,
    "RUserType" : subtypeOfUserType,
    "RAliasType" : subtypeOfAliasType,
    "RDataTypeSelector" : subtypeOfDataTypeSelectorType,
    "RFailType" : subtypeOfFailType,
    "RInferredType" : subtypeOfInferredType,
    "ROverloadedType" : subtypeOfOverloadedType
);

public bool subtypeOf(RType t1, RType t2) {
    if (getName(t1) in subtypeHandlers) 
        return (subtypeHandlers[getName(t1)])(t1,t2);
    else
        throw UnexpectedRType(t1);
}

//
// TODO: Add LOTS of tests!
//
