@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::TypeMatching

import List;
import Set;
import IO;
import Node;

import lang::rascal::types::Types;
import lang::rascal::types::TypeExceptions;
import lang::rascal::types::SubTypes;


///////////////////////////////////////////////////////////////////////////////////////////
//
// Individual methods that compute bindings for inferred types in Rascal types
//
///////////////////////////////////////////////////////////////////////////////////////////

private tuple[rel[RType, RType], bool] unifyTypesBase(RType left, RType right, rel[RType, RType] bindings) {
    return < bindings, true >;
}

private tuple[rel[RType, RType], bool] unifyReifiedTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(getReifiedType(left), getReifiedType(right), bindings);
}

private tuple[rel[RType, RType], bool] unifyRelTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(getRelElementType(left), getRelElementType(right), bindings);
}

private tuple[rel[RType, RType], bool] unifyTypeVarTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(getTypeVarBound(left), getTypeVarBound(right), bindings);
}

private tuple[rel[RType, RType], bool] unifyFunctionTypes(RType left, RType right, rel[RType, RType] bindings) {
    < bindings, res > = unifyTypes(getFunctionReturnType(left),getFunctionReturnType(right),bindings);
    return res ? unifyTypes(makeTupleType(getFunctionArgumentTypes(left)),makeTupleType(getFunctionArgumentTypes(right)),bindings) : < bindings, res >;
}

private tuple[rel[RType, RType], bool] unifyTupleTypes(RType left, RType right, rel[RType, RType] bindings) {
    list[RType] leftFields = getTupleFields(left);
    list[RType] rightFields = getTupleFields(right);
    if (size(leftFields) != size(rightFields)) return < bindings, false >;
    if (size(leftFields) == 0) return < bindings, true >;
    bool res = true;
    for (n <- [0..size(leftFields)-1], res) 
        < bindings, res > = unifyTypes(leftFields[n],rightFields[n],bindings);
    return < bindings, res >;
}

private tuple[rel[RType, RType], bool] unifySetTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(getSetElementType(left),getSetElementType(right), bindings);
}

private tuple[rel[RType, RType], bool] unifyMapTypes(RType left, RType right, rel[RType, RType] bindings) {
    < bindings, res > = unifyTypes(getMapDomainType(left),getMapDomainType(right), bindings);
    return res ? unifyTypes(getMapRangeType(left),getMapRangeType(right),bindings) : < bindings, res >;
}

private tuple[rel[RType, RType], bool] unifyConstructorTypes(RType left, RType right, rel[RType, RType] bindings) {
    < bindings, res > = unifyTypes(getConstructorResultType(left),getConstructorResultType(right),bindings);
    return res ? unifyTypes(makeTupleType(getConstructorArgumentTypes(left)),makeTupleType(getConstructorArgumentTypes(right)),bindings) : < bindings, res >;
}

private tuple[rel[RType, RType], bool] unifyListTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(getListElementType(left),getListElementType(right), bindings);
}

private tuple[rel[RType, RType], bool] unifyADTTypes(RType left, RType right, rel[RType, RType] bindings) {
    return unifyTypes(makeTupleType(getADTTypeParameters(left)), makeTupleType(getADTTypeParameters(right)), bindings);
}

//
// Main unify routine
//
private rel[str, str, tuple[rel[RType,RType],bool] (RType,RType,rel[RType,RType])] unifyHandlers = {
    < "RValueType", "RValueType", unifyTypesBase >,
    < "RLocType", "RLocType", unifyTypesBase >,
    < "RNodeType", "RNodeType", unifyTypesBase >,
    < "RNumType", "RNumType", unifyTypesBase >,
    < "RReifiedType", "RReifiedType", unifyReifiedTypes >,
    < "RIntType", "RIntType", unifyTypesBase >,
    < "RRelType", "RRelType", unifyRelTypes >,
    < "RTypeVar", "RTypeVar", unifyTypeVarTypes >,
    < "RRealType", "RRealType", unifyTypesBase >,
    < "RFunctionType", "RFunctionType", unifyFunctionTypes >,
    < "RTupleType", "RTupleType", unifyTupleTypes >,
    < "RStrType", "RStrType", unifyTypesBase >,
    < "RBoolType", "RBoolType", unifyTypesBase >,
    < "RVoidType", "RVoidType", unifyTypesBase >,
    < "RDateTimeType", "RDateTimeType", unifyTypesBase >,
    < "RSetType", "RSetType", unifySetTypes >,
    < "RMapType", "RMapType", unifyMapTypes >,
    < "RConstructorType", "RConstructorType", unifyConstructorTypes >,
    < "RListType", "RListType", unifyListTypes >,
    < "RADTType", "RADTType", unifyADTTypes >
};

//
// TODO: Question, what to do when we have an inferred type or an inference var on the right?
// Should we return that as part of the binding? Leave that out for now, see what comes up
// in practice. These probably come up commonly in cases like inferred = inferred, but not
// sure about cases like int = inferred.
//
public tuple[rel[RType, RType], bool] unifyTypes(RType left, RType right, rel[RType, RType] bindings) {
    if (size(unifyHandlers[getName(left),getName(right)]) == 1) {
        return (getOneFrom(unifyHandlers[getName(left),getName(right)]))(left,right,bindings);
    } else if (RInferredType(_) := left) {
        return < bindings + < left, right >, true >;
    } else if (InferenceVar(_) := left) {
        return < bindings + < left, right >, true >;
    } else if (isAliasType(left)) {
        return unifyTypes(getAliasedType(left), right, bindings);
    } else if (isAliasType(right)) {
        return unifyTypes(left, getAliasedType(right), bindings);
    }
        
    return < bindings, false >;
}

public tuple[rel[RType, RType], bool] unifyTypes(RType left, RType right) {
    return unifyTypes(left, right, { });
}


