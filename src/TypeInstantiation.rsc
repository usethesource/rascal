@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::types::TypeInstantiation

import List;
import Set;
import IO;
import Node;

import lang::rascal::types::Types;
import lang::rascal::types::TypeExceptions;
import lang::rascal::types::SubTypes;
import lang::rascal::types::Lubs;

///////////////////////////////////////////////////////////////////////////////////////////
//
// Individual methods that compute bindings for type parameters in Rascal types
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// The base routine called in many cases when we don't have another match...
//

private map[RName varName, RType varType] getTVBindingsBase(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    if (!subtypeOf(actual, formal)) throw CannotCalculateBindings(formal, actual, l);
    return bindings;    
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsValueType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsLocType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsNodeType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsNumType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// Based on implementation of match in ReifiedType
//
private map[RName varName, RType varType] getTVBindingsReifiedType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(getReifiedType(formal),getReifiedType(actual), bindings, l);
}

//
// This type does not yet exist, so marking it as an unexpected case
//
// TODO: Add implementation when we have support for this in Rascal
//
private map[RName varName, RType varType] getTVBindingsBagType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnimplementedRType(t1);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsIntType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// Based on implementation of match in RelationType (actually SetType, which R.T. inherits)
//
private map[RName varName, RType varType] getTVBindingsRelType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(getRelElementType(formal),getRelElementType(actual), bindings, l);
}

//
// Based on implementation of match in ParameterType
//
private map[RName varName, RType varType] getTVBindingsTypeVarType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    RName currentVarName = getTypeVarName(formal);
    if (currentVarName in bindings) {
        RType newLub = lub(bindings[currentVarName],actual);
        if (! subtypeOf(newLub,getTypeVarBound(formal))) {
            throw CannotCalculateBindings(formal, actual, l);
        }
        bindings[currentVarName] = newLub;
    } else {
        bindings[currentVarName] = actual;
    }

    return bindings;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsRealType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// Based on implementation of match in FunctionType
//
private map[RName varName, RType varType] getTVBindingsFunctionType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    if (isVoidType(actual)) return getTVBindings(getFunctionReturnType(formal),actual,bindings,l);
    
    if (isOverloadedType(actual)) {
        set[RType] overloads = getOverloadOptions(actual);
        for (overload <- overloads) {
            if (subtypeOf(overload, formal) && isFunctionType(overload)) {
                return getTVBindings(formal,overload,bindings,l);   
            }
        }    
    } else if (isFunctionType(actual)) {
        bindings = getTVBindings(getFunctionReturnType(formal),getFunctionReturnType(actual),bindings,l);
        return getTVBindings(makeTupleType(getFunctionArgumentTypes(formal)),makeTupleType(getFunctionArgumentTypes(actual)),bindings,l);
    }

    throw CannotCalculateBindings(formal, actual, l);
}

//
// Based on implementation of match in TupleType
//
private map[RName varName, RType varType] getTVBindingsTupleType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    list[RType] formalFields = getTupleFields(formal);
    list[RType] actualFields = getTupleFields(actual);
    if (size(formalFields) == 0) return bindings;
    for (n <- [0..size(formalFields)-1]) 
        bindings = getTVBindings(formalFields[n],actualFields[n],bindings,l);
    return bindings;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsStrType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsBoolType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// TODO: reified reified types don't appear to be implemented yet; we will
// need to add support here once we figure out what this support should
// look like.
//
private map[RName varName, RType varType] getTVBindingsReifiedReifiedType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnimplementedRType(t1);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsVoidType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsNonTerminalType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsDateTimeType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// Based on implementation of match in SetType
//
private map[RName varName, RType varType] getTVBindingsSetType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(getSetElementType(formal),getSetElementType(actual), bindings, l);
}

//
// Based on implementation of match in MapType
//
private map[RName varName, RType varType] getTVBindingsMapType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    bindings = getTVBindings(getMapDomainType(formal),getMapDomainType(actual), bindings, l);
    return getTVBindings(getMapRangeType(formal),getMapRangeType(actual),bindings,l);
}

//
// Based on implementation of match in ConstructorType
//
private map[RName varName, RType varType] getTVBindingsConstructorType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    if (isConstructorType(actual)) {
        bindings = getTVBindings(getConstructorResultType(formal),getConstructorResultType(actual),bindings,l);
        return getTVBindings(makeTupleType(getConstructorArgumentTypes(formal)),makeTupleType(getConstructorArgumentTypes(actual)),bindings,l);
    } else {
        throw CannotCalculateBindings(formal, actual, l);
    }
}

//
// Based on implementation of match in ListType
//
private map[RName varName, RType varType] getTVBindingsListType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(getListElementType(formal),getListElementType(actual), bindings, l);
}

//
// Based on implementation of match in AbstractDataType
//
private map[RName varName, RType varType] getTVBindingsADTType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(makeTupleType(getADTTypeParameters(formal)), makeTupleType(getADTTypeParameters(actual)), bindings, l);
}

//
// lex is not yet a supported type from what I can tell (MAH)
//
// TODO: Add support if/when this is a supported type
//
private map[RName varName, RType varType] getTVBindingsLexType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter a user type in a match computation,
// we should have always resolved it to an actual type (ADT, alias)
// at this point.
//
private map[RName varName, RType varType] getTVBindingsUserType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of match in AliasType
//
private map[RName varName, RType varType] getTVBindingsAliasType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    bindings = getTVBindingsBase(formal, actual, bindings, l);
    return getTVBindings(getAliasedType(formal),actual,bindings,l);
}

//
// We should never encounter this type in a match computation
//
private map[RName varName, RType varType] getTVBindingsDataTypeSelectorType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a match computation
//
private map[RName varName, RType varType] getTVBindingsFailType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in a match computation,
// it should always be instantiated first
//
private map[RName varName, RType varType] getTVBindingsInferredType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    throw UnexpectedRType(t1);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private map[RName varName, RType varType] getTVBindingsOverloadedType(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    return getTVBindingsBase(formal, actual, bindings, l);
}

//
// Main match routine
//
private map[str, map[RName,RType] (RType,RType,map[RName,RType],loc)] matchHandlers = (
    "RValueType" : getTVBindingsValueType,
    "RLocType" : getTVBindingsLocType,
    "RNodeType" : getTVBindingsNodeType,
    "RNumType" : getTVBindingsNumType,
    "RReifiedType" : getTVBindingsReifiedType,
    "RBagType" : getTVBindingsBagType,
    "RIntType" : getTVBindingsIntType,
    "RRelType" : getTVBindingsRelType,
    "RTypeVar" : getTVBindingsTypeVarType,
    "RRealType" : getTVBindingsRealType,
    "RFunctionType" : getTVBindingsFunctionType,
    "RTupleType" : getTVBindingsTupleType,
    "RStrType" : getTVBindingsStrType,
    "RBoolType" : getTVBindingsBoolType,
    "RReifiedReifiedType" : getTVBindingsReifiedReifiedType,
    "RVoidType" : getTVBindingsVoidType,
    "RNonTerminalType" : getTVBindingsNonTerminalType,
    "RDateTimeType" : getTVBindingsDateTimeType,
    "RSetType" : getTVBindingsSetType,
    "RMapType" : getTVBindingsMapType,
    "RConstructorType" : getTVBindingsConstructorType,
    "RListType" : getTVBindingsListType,
    "RADTType" : getTVBindingsADTType,
    "RLexType" : getTVBindingsLexType,
    "RUserType" : getTVBindingsUserType,
    "RAliasType" : getTVBindingsAliasType,
    "RDataTypeSelector" : getTVBindingsDataTypeSelectorType,
    "RFailType" : getTVBindingsFailType,
    "RInferredType" : getTVBindingsInferredType,
    "ROverloadedType" : getTVBindingsOverloadedType
);

public map[RName varName, RType varType] getTVBindings(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    if (getName(formal) in matchHandlers) 
        return (matchHandlers[getName(formal)])(formal,actual,bindings,l);
    else
        throw UnexpectedRType(t1);
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Individual methods that instantiate type parameters in Rascal types
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// The base routine called in many cases when we don't have specific instantiate logic
//
private RType instantiateVarsBase(RType rt, map[RName varName, RType varType] bindings) {
    return rt;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsValueType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsLocType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsNodeType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsNumType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// Based on implementation of instantiate in ReifiedType
//
private RType instantiateVarsReifiedType(RType rt, map[RName varName, RType varType] bindings) {
    return makeReifiedType(instantiateVars(getReifiedType(rt),bindings));
}

//
// This type does not yet exist, so marking it as an unexpected case
//
// TODO: Add implementation when we have support for this in Rascal
//
private RType instantiateVarsBagType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnimplementedRType(t1);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsIntType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// Based on implementation of instantiate in RelationType (actually SetType, which R.T. inherits)
//
private RType instantiateVarsRelType(RType rt, map[RName varName, RType varType] bindings) {
    return makeRelTypeFromTuple(instantiateVars(getRelElementType(rt),bindings));
}

//
// Based on implementation of instantiate in ParameterType
//
private RType instantiateVarsTypeVarType(RType rt, map[RName varName, RType varType] bindings) {
    RName currentVarName = getTypeVarName(rt);
    if (currentVarName in bindings) {
        return bindings[currentVarName];
    }
    return rt;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsRealType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// Based on implementation of instantiate in FunctionType
//
private RType instantiateVarsFunctionType(RType rt, map[RName varName, RType varType] bindings) {
    return makeFunctionTypeFromTuple(instantiateVars(getFunctionReturnType(rt),bindings),
                                     instantiateVars(getFunctionArgumentTypesAsTuple(rt),bindings),
                                     isVarArgsFun(rt));
}

//
// Based on implementation of instantiate in TupleType
//
private RType instantiateVarsTupleType(RType rt, map[RName varName, RType varType] bindings) {
    list[RNamedType] tupleFields = getTupleFieldsWithNames(rt);
    return RTupleType([instantiateVarsNamedType(tf,bindings) | tf <- tupleFields]);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsStrType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsBoolType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// TODO: reified reified types don't appear to be implemented yet; we will
// need to add support here once we figure out what this support should
// look like.
//
private RType instantiateVarsReifiedReifiedType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnimplementedRType(t1);
}

//
// Based on implementation of instantiate in VoidType
//
private RType instantiateVarsVoidType(RType rt, map[RName varName, RType varType] bindings) {
    return rt;
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsNonTerminalType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsDateTimeType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// Based on implementation of instantiate in SetType
//
private RType instantiateVarsSetType(RType rt, map[RName varName, RType varType] bindings) {
    return makeSetType(instantiateVars(getSetElementType(rt),bindings));
}

//
// Based on implementation of instantiate in MapType
//
private RType instantiateVarsMapType(RType rt, map[RName varName, RType varType] bindings) {
    return makeMapTypeFromTuple(instantiateVars(getMapFieldsAsTuple(rt),bindings));
}

//
// Based on implementation of instantiate in ConstructorType
//
private RType instantiateVarsConstructorType(RType rt, map[RName varName, RType varType] bindings) {
    return makeConstructorTypeFromTuple(
        getConstructorName(rt), 
        instantiateVars(getConstructorResultType(rt),bindings), 
        instantiateVars(getConstructorArgumentTypesAsTuple(rt),bindings));
}

//
// Based on implementation of instantiate in ListType
//
private RType instantiateVarsListType(RType rt, map[RName varName, RType varType] bindings) {
    return makeListType(instantiateVars(getListElementType(rt),bindings));
}

//
// Based on implementation of instantiate in AbstractDataType
//
private RType instantiateVarsADTType(RType rt, map[RName varName, RType varType] bindings) {
    list[RType] params = getADTTypeParameters(rt);
    list[RType] instParams = [ instantiateVars(p,bindings) | p <- params ];
    return makeParameterizedADTType(getADTName(rt),instParams);
}

//
// lex is not yet a supported type from what I can tell (MAH)
//
// TODO: Add support if/when this is a supported type
//
private RType instantiateVarsLexType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter a user type in an instantiate computation,
// we should have always resolved it to an actual type (ADT, alias)
// at this point.
//
private RType instantiateVarsUserType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnexpectedRType(t1);
}

//
// Based on implementation of instantiate in AliasType
//
private RType instantiateVarsAliasType(RType rt, map[RName varName, RType varType] bindings) {
    list[RType] params = getAliasTypeParameters(rt);
    list[RType] instParams = [ instantiateVars(p,bindings) | p <- params ];
    return makeParameterizedAliasType(getAliasName(rt),instantiateVars(getAliasedType(rt),bindings),instParams);
}

//
// We should never encounter this type in an instantiate computation
//
private RType instantiateVarsDataTypeSelectorType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in an instantiate computation
//
private RType instantiateVarsFailType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnexpectedRType(t1);
}

//
// We should never encounter this type in an instantiate computation,
// it should always be resolved to an actual (ADT/alias) type first
//
private RType instantiateVarsInferredType(RType rt, map[RName varName, RType varType] bindings) {
    throw UnexpectedRType(t1);
}

//
// No implementation in the corresponding PDB type, using definition from base class
//
private RType instantiateVarsOverloadedType(RType rt, map[RName varName, RType varType] bindings) {
    return instantiateVarsBase(rt, bindings);
}

//
// Handle named types
//
private RNamedType instantiateVarsNamedType(RNamedType rt, map[RName varName, RType varType] bindings) {
    if (RUnnamedType(t) := rt) return RUnnamedType(instantiateVars(t,bindings));
    if (RNamedType(t,tn) := rt) return RNamedType(instantiateVars(t,bindings),tn);
}

//
// Main instantiate routine
//
private map[str, RType (RType,map[RName,RType])] instHandlers = (
    "RValueType" : instantiateVarsValueType,
    "RLocType" : instantiateVarsLocType,
    "RNodeType" : instantiateVarsNodeType,
    "RNumType" : instantiateVarsNumType,
    "RReifiedType" : instantiateVarsReifiedType,
    "RBagType" : instantiateVarsBagType,
    "RIntType" : instantiateVarsIntType,
    "RRelType" : instantiateVarsRelType,
    "RTypeVar" : instantiateVarsTypeVarType,
    "RRealType" : instantiateVarsRealType,
    "RFunctionType" : instantiateVarsFunctionType,
    "RTupleType" : instantiateVarsTupleType,
    "RStrType" : instantiateVarsStrType,
    "RBoolType" : instantiateVarsBoolType,
    "RReifiedReifiedType" : instantiateVarsReifiedReifiedType,
    "RVoidType" : instantiateVarsVoidType,
    "RNonTerminalType" : instantiateVarsNonTerminalType,
    "RDateTimeType" : instantiateVarsDateTimeType,
    "RSetType" : instantiateVarsSetType,
    "RMapType" : instantiateVarsMapType,
    "RConstructorType" : instantiateVarsConstructorType,
    "RListType" : instantiateVarsListType,
    "RADTType" : instantiateVarsADTType,
    "RLexType" : instantiateVarsLexType,
    "RUserType" : instantiateVarsUserType,
    "RAliasType" : instantiateVarsAliasType,
    "RDataTypeSelector" : instantiateVarsDataTypeSelectorType,
    "RFailType" : instantiateVarsFailType,
    "RInferredType" : instantiateVarsInferredType,
    "ROverloadedType" : instantiateVarsOverloadedType
);

public RType instantiateVars(RType rt, map[RName varName, RType varType] bindings) {
    if (getName(rt) in instHandlers) 
        return (instHandlers[getName(rt)])(rt,bindings);
    else
        throw UnexpectedRType(rt);
}

//
// TODO: Add LOTS of tests!
//
