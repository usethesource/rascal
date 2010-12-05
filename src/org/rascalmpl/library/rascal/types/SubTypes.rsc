module rascal::types::SubTypes

import List;
import Set;
import IO;

import rascal::types::Types;

//
// TODO: Support is provided for the RListOrSet type, but only for lub and subtype
// calculations, not for type vars. Add these if needed, but we try to resolve
// the RListOrSet type to either an RList or an RSet type as quickly as possible,
// so hopefully we don't need code for this.
//

//
// TODO: Add support for RReifiedReifiedType
//

//
// Encode the subtyping relation t1 <: t2
//
// NOTE: For performance reasons we use matching to check the constructor used to
// construct the type t1. The isType functions are used for t2, which will automatically
// handle unrolling aliases and using the bounds of type variables.
//
public bool subtypeOf(RType t1, RType t2) {
    if (RAliasType(_,_) := t1) {
        // First check: equality. We will say that t1 and t2 are both equal if they are both aliases 
        // with the same names and equivalent type parameters.
        if (RAliasType(_,_) := t2 && (getAliasName(t1) == getAliasName(t2))) {
            RType params1 = aliasHasTypeParameters(t1) ? makeTupleType(getAliasTypeParameters(t1)) : makeVoidType();
            RType params2 = aliasHasTypeParameters(t2) ? makeTupleType(getAliasTypeParameters(t2)) : makeVoidType();
            if (equivalent(params1, params2)) return true;
        }

        // Second check: t2 is alias, subtype relation between parameters holds
        if (RAliasType(_,_) := t2 && (getAliasName(t1) == getAliasName(t2))) {
            RType params1 = aliasHasTypeParameters(t1) ? makeTupleType(getAliasTypeParameters(t1)) : makeVoidType();
            RType params2 = aliasHasTypeParameters(t2) ? makeTupleType(getAliasTypeParameters(t2)) : makeVoidType();
            return subtypeOf(getAliasedType(t1),t2) && subtypeOf(params1, params2);
        }

        // Third check: see if the aliased type of t1 is a subtype of t2
        return subtypeOf(getAliasedType(t1), t2);

    } else if (RTypeVar(_) := t1) {
        // First check: equality of the type variables. They are equal if they have the same name and equivalent bounds.
        if (RTypeVar(_) := t2 && (getTypeVarName(t1) == getTypeVarName(t2)) && (equivalent(getTypeVarBound(t1),getTypeVarBound(t2)))) return true;

        // Second check: the bound of t1 is a subtype of t2.
        return subtypeOf(getTypeVarBound(t1),t2);

    } else if (RRelType(_) := t1 || RSetType(RTupleType(_)) := t1) {
        // First check: t1 subtypeOf t2 if the element types are also subtypes
        if (isRelType(t2)) return subtypeOf(getRelElementType(t1),getRelElementType(t2));

        // Second check: the super call would first go to set, so put set checking
        // code here as well as below.
        if (isSetType(t2)) return subtypeOf(getRelElementType(t1), getSetElementType(t2));

        // Third check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RADTType(_) := t1) {
        // First check: equality. We will say t1 and t2 are equal if they are both ADTs with the same names and equivalent type parameters.
        if (isADTType(t2) && (getADTName(t1) == getADTName(t2))) {
            RType params1 = adtHasTypeParameters(t1) ? makeTupleType(getADTTypeParameters(t1)) : makeVoidType();
            RType params2 = adtHasTypeParameters(t2) ? makeTupleType(getADTTypeParameters(t2)) : makeVoidType();
            if (equivalent(params1, params2)) return true;
        }

        // Second check: t2 is value
        if (isValueType(t2)) return true;

        // Third check: t2 is ADT, subtype relation between parameters holds
        if (isADTType(t2) && (getADTName(t1) == getADTName(t2))) {
            RType params1 = adtHasTypeParameters(t1) ? makeTupleType(getADTTypeParameters(t1)) : makeVoidType();
            RType params2 = adtHasTypeParameters(t2) ? makeTupleType(getADTTypeParameters(t2)) : makeVoidType();
            return subtypeOf(params1, params2);
        }
        
        // Fourth check: aliases
        if (isAliasType(t2) && !isVoidType(getAliasedType(t2))) return subtypeOf(t1, getAliasedType(t2));

        // Fifth check: try with node
        return subtypeOf(makeNodeType(), t2);

    } else if (RBoolType() := t1) {
        // No bool-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RConstructorType(_,_,_) := t1) {
        // First check: equality. We will say that t1 and t2 are both equal if they are both constructors 
        // with the same names and equivalent parameters and return types.
        if (isConstructorType(t2) && (getADTName(t1) == getADTName(t2))) {
            if(equivalent(getConstructorResultType(t1),getConstructorResultType(t2))) {
                RType params1 = (size(getConstructorArgumentTypes(t1)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getConstructorArgumentTypes(t1));
                RType params2 = (size(getConstructorArgumentTypes(t2)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getConstructorArgumentTypes(t2));
                if (equivalent(params1,params2)) return true;
            }
        }

        // Second check: equality of the underlying ADTs.
        if ( (isADTType(t2) || isConstructorType(t2)) && (equivalent(getConstructorResultType(t1), isADTType(t2) ? t2 : getConstructorResultType(t2)))) return true;

        // Third check: underlying ADT is a subtype of t2
        return subtypeOf(getConstructorResultType(t1),t2);

    } else if (RDateTimeType() := t1) {
        // No datetime-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RIntType() := t1) {
        // First check: is t2 of type int  
        if (isIntType(t2)) return true;

        // Second check: is t2 of type num  
        if (isNumType(t2)) return true;

        // Third check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RListType(_) := t1) {
        // First check: t1 subtypeOf t2 if both are lists and the element type of t1 is a subtypeOf the element type of t2
        if (isListType(t2)) return subtypeOf(getListElementType(t1),getListElementType(t2));

        // Similar check for ListOrSet
        if (isListOrSetType(t2)) return subtypeOf(getListElementType(t1),getListOrSetElementType(t2));
        
        // Second check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RListOrSetType(_) := t1) {
        if (isListType(t2)) return subtypeOf(getListOrSetElementType(t1),getListElementType(t2));
        if (isSetType(t2)) return subtypeOf(getListOrSetElementType(t1),getSetElementType(t2));
        if (isListOrSetType(t2)) return subtypeOf(getListOrSetElementType(t1),getListOrSetElementType(t2));
        return subtypeOfDefault(t1, t2);

    } else if (RMapType(_,_) := t1) {
        if (isMapType(t2)) return subtypeOf(getMapDomainType(t1),getMapDomainType(t2)) && subtypeOf(getMapRangeType(t1),getMapRangeType(t2));

        // Second check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RNodeType() := t1) {
        // No node-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RNumType() := t1) {
        // No number-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RRealType() := t1) {
        // First check: is t2 of type real  
        if (isRealType(t2)) return true;

        // Second check: is t2 of type num  
        if (isNumType(t2)) return true;

        // Third check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RSetType(_) := t1) { // no need to check for RRelType(_) here, already checked above
        // First check: t1 subtypeOf t2 if the element types are also subtypes
        if (isSetType(t2)) return subtypeOf(getSetElementType(t1),getSetElementType(t2));

        // Similar check for ListOrSet
        if (isListOrSetType(t2)) return subtypeOf(getSetElementType(t1),getListOrSetElementType(t2));
        
        // Second check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RLocType() := t1) {
        // No source location-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RStrType() := t1) {
        // No str-specific implementation, use default check
        return subtypeOfDefault(t1, t2);

    } else if (RTupleType(_) := t1) {
        // First check: two tuples are subtypes if the elements have the same arity and are also in a subtype relationship
        if (isTupleType(t2)) {
            list[RType] tflds1 = getTupleFields(t1);
            list[RType] tflds2 = getTupleFields(t2);
            if (size(tflds1) == size(tflds2)) {
                if (size(tflds1) == 0) return true;
                return (size(tflds1) == 0) ? true : (size([n | n <- [0..size(tflds1)-1], !subtypeOf(tflds1[n],tflds2[n])]) == 0);
            }
        } 

        // Second check: continue on up hierarchy to default checks  
        return subtypeOfDefault(t1, t2);

    } else if (RValueType() := t1) {
        if (isValueType(t2)) return true;
        if (isVoidType(t2)) return false;
        if (isAliasType(t2)) return subtypeOf(t1, getAliasedType(t2));
        if (isTypeVar(t2)) return subtypeOf(t1, getTypeVarBound(t2));
        return false;

    } else if (RVoidType() := t1) {
        return true;

    } else if (RFunctionType(_,_) := t1) {
        // First check: t2 is equal to t1, defined as having equivalent return and parameter types
        if (isFunctionType(t2) && equivalent(getFunctionReturnType(t1),getFunctionReturnType(t2))) {
            RType params1 = (size(getFunctionArgumentTypes(t1)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getFunctionArgumentTypes(t1));
            RType params2 = (size(getFunctionArgumentTypes(t2)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getFunctionArgumentTypes(t2));
            if (equivalent(params1, params2)) return true;
        }

        // Second check: t2 is a subtype of t1. Functions have contra-variant parameters and co-variant return types.
        if (isFunctionType(t2) && subtypeOf(getFunctionReturnType(t1), getFunctionReturnType(t2))) {
            RType params1 = (size(getFunctionArgumentTypes(t1)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getFunctionArgumentTypes(t1));
            RType params2 = (size(getFunctionArgumentTypes(t2)) == 0) ? makeTupleType([makeVoidType()]) : makeTupleType(getFunctionArgumentTypes(t2));
            if (subtypeOf(params1, params2)) return true;

            // TODO: Instantiate any type parameters for a second check
        }

        // Third check: if t2 is a function type, and we haven't matched yet, return false
        if (isFunctionType(t2)) return false;

        // Fourth check: switch to default
        return subtypeOfDefault(t1, t2);

    } else if (RReifiedType(_) := t1) {
        // First check: other type is a reified type too, compare type args
        if (isReifiedType(t2)) return subtypeOf(getReifiedType(t1),getReifiedType(t2));

        // Second check: just try default
        return subtypeOfDefault(t1, t2);
    
    } else {
        throw "subtypeOf given an invalid type combination, <prettyPrintType(t1)> AND <prettyPrintType(t2)>";
    }
}

//
// This function handles the default "super" case for subtypeOf. We don't need
// to check everything here: for instance, ADTs will never be checked in this
// function, since they never call the equivalent code in the Rascal implementation.
//
public bool subtypeOfDefault(RType t1, RType t2) {
    // Short circuit -- basic equality test, won't handle alias or type
    // var comparisons to regular types, but will handle all the int to int
    // or bool to bool type comparisons (below is redundant then, but provides
    // documentation, we don't have singletons or object equality in Rascal)
    if (t1 == t2) return true;

    // Short circuit -- moving up comparisons that always just immediately
    // return true so we can skip other comparisons below.

    // Added case: var args types
    // TODO: See if we need to keep this case, or if we can remove it somehow
    // TODO: If we need to keep it, do we need one for t1 as well?
    if (RVarArgsType(_) := t2) {
        RType vt = getVarArgsType(t2);
        return subtypeOf(t1, vt) || subtypeOf(t1, makeListType(vt));
    }

    // Now, back to the regular flow of control...

    // First, see if the second type is value
    if (RValueType() := t2) return true;

        // Second, handle simple equality tests. 
    if (isBoolType(t1) && isBoolType(t2)) return true;
    if (isDateTimeType(t1) && isDateTimeType(t2)) return true;
    if (isIntType(t1) && isIntType(t2)) return;
    if (isLocType(t1) && isLocType(t2)) return true;
    if (isNodeType(t1) && isNodeType(t2)) return true;
    if (isNumType(t1) && isNumType(t2)) return true;
    if (isRealType(t1) && isRealType(t2)) return true;
    if (isStrType(t1) && isStrType(t2)) return true;

    // Third, handle aliases
    if (isAliasType(t2) && !isVoidType(getAliasedType(t2))) return subtypeOf(t1, getAliasedType(t2));
    
    // Fourth, handle type parameters
    if (isTypeVar(t2)) return subtypeOf(t1, getTypeVarBound(t2));

    // When all else fails, return false
    return false;
}

//
// Calculate the least upper bound of t1 and t2
//
public RType lub(RType t1, RType t2) {
    // NOTE: The ordering here is intentional. isADTType, isIntType, etc will automatically unroll aliases and type parameters.
    // Also, relations are always sets. So, we put the functionality for aliases, type vars, and then relations first, so we
    // make sure to handle those in the way they should be handled.  
    if (RAliasType(_,_) := t1) {
        // First check: equality. We will say that t1 and t2 are both equal if they are both aliases with the same names and
        // equivalent type parameters.
        if (isAliasType(t2) && (getAliasName(t1) == getAliasName(t2))) {
            RType params1 = aliasHasTypeParameters(t1) ? makeTupleType(getAliasTypeParameters(t1)) : makeVoidType();
            RType params2 = aliasHasTypeParameters(t2) ? makeTupleType(getAliasTypeParameters(t2)) : makeVoidType();
            if (equivalent(params1, params2)) return t1;
        }

        // Second check: t2 is alias, subtype relation between parameters holds
        if (isAliasType(t2) && (getAliasName(t1) == getAliasName(t2))) {
            RType params1 = aliasHasTypeParameters(t1) ? makeTupleType(getAliasTypeParameters(t1)) : makeVoidType();
            RType params2 = aliasHasTypeParameters(t2) ? makeTupleType(getAliasTypeParameters(t2)) : makeVoidType();
            RType lubAliased = lub(getAliasedType(t1), getAliasedType(t2));
            RType lubParams = lub(params1, params2);
            if (RTupleType(tps) := lubParams)
                return RAliasType(RParameterizedUserType(getAliasName(t1),tps), lubAliased);
            else
                return RAliasType(RUserType(getAliasName(t1)), lubAliased);
        }

        // Third check: try the lub with the aliased type and t2
        return lub(getAliasedType(t1), t2);

    } else if (RTypeVar(_) := t1) {
        // First check: equality of the type variables. They are equal if they have the same name and equivalent bounds.
        if (isTypeVar(t2) && (getTypeVarName(t1) == getTypeVarName(t2)) && (equivalent(getTypeVarBound(t1),getTypeVarBound(t2)))) return t1;

        // Second check: take the lub of the bound of t1 and t2
        return lub(getTypeVarBound(t1),t2);

    } else if (RRelType(_) := t1) {
        // First check: if both types are relations, we take the lub of the rel element tuples. If this is also a tuple,
        // the result is a new relation with this tuple. If instead the result is not a tuple, the end result is a set
        // using the new (non-tuple) lub.
        if (isRelType(t2)) {
            RType res = lub(getRelElementType(t1), getRelElementType(t2));
            if (RTupleType(tels) := res)
                return RRelType(tels);
            else
                return RSetType(res);
        }

        // Second check: if the second type is a set, not a relation, the lub is a set with element the lub of the rel and set elements
        if (isSetType(t2)) return makeSetType(lub(getRelElementType(t1),getSetElementType(t2)));

        // Third check: just call default
        return lubDefault(t1, t2);
    
    } else if (RADTType(_) := t1) {
        // First check: equality. We will say t1 and t2 are equal if they are both ADTs with the same names and equivalent type parameters.
        if (isADTType(t2) && (getADTName(t1) == getADTName(t2))) {
            RType params1 = adtHasTypeParameters(t1) ? makeTupleType(getADTTypeParameters(t1)) : makeVoidType();
            RType params2 = adtHasTypeParameters(t2) ? makeTupleType(getADTTypeParameters(t2)) : makeVoidType();
            if (equivalent(params1, params2)) return t1;
        }

        // Second check: t2 is void
        if (isVoidType(t2)) return t1;

        // Third check: t2 is ADT, return a new ADT type using the lub of the type parameters
        if (isADTType(t2) && (getADTName(t1) == getADTName(t2))) {
            RType params1 = adtHasTypeParameters(t1) ? makeTupleType(getADTTypeParameters(t1)) : makeVoidType();
            RType params2 = adtHasTypeParameters(t2) ? makeTupleType(getADTTypeParameters(t2)) : makeVoidType();
            RType lubParams = lub(params1, params2);
            if (RTupleType(tpl) := lubParams)
                return RADTType(RParameterizedUserType(getADTName(t1),tpl));
            else
            return RADTType(RUserType(getADTName(t1)));
        }
        
        // Fourth check: t2 is a constructor, so using its ADT, return a new ADT type using the lub of the type parameters
        if (isConstructorType(t2) && (getADTName(t1) == getADTName(t2))) {
            RType params1 = adtHasTypeParameters(t1) ? makeTupleType(getADTTypeParameters(t1)) : makeVoidType();
            RType params2 = adtHasTypeParameters(t2) ? makeTupleType(getADTTypeParameters(t2)) : makeVoidType();
            RType lubParams = lub(params1, params2);
            if (RTupleType(tpl) := lubParams)
                return RADTType(RParameterizedUserType(getADTName(t1),tpl));
            else
                return RADTType(RUserType(getADTName(t1)));
        }

        // Fifth check: try with node
        return lub(makeNodeType(), t2);

    } else if (RBoolType() := t1) {
        // No explicit lub rule, just use default
        return lubDefault(t1, t2);

    } else if (RConstructorType(_,_,_) := t1) {
        // First check: t2 is also a constructor
        if (isConstructorType(t2)) return lub(getConstructorResultType(t1), getConstructorResultType(t2));

        // Second check: t2 is an ADT
        if (isADTType(t2)) return lub(getConstructorResultType(t1), t2);

        // Third check: t2 is a node
        if (isNodeType(t2)) return t2;

        // Fourth check: just try the default
        return lubDefault(t1, t2);

    } else if (RDateTimeType() := t1) {
        // No explicit lub rule, just use default
        return lubDefault(t1, t2);

    } else if (RIntType() := t1) {
        // First check: lub(int,int) = int
        if (isIntType(t2)) return t1;

        // Second check: lub(int,num) and lub(int,real) = num
        if (isNumType(t2) || isRealType(t2)) return makeNumType();

        // Final check: just call default
        return lubDefault(t1, t2);

    } else if (RListType(_) := t1) {
        // First check: if both types are lists, the lub is a list with element the lub of the two list elements
        if (isListType(t2)) return makeListType(lub(getListElementType(t1),getListElementType(t2)));

        if (isListOrSetType(t2)) return makeListType(lub(getListElementType(t1),getListOrSetElementType(t2)));
        
        // Final check: just call default
        return lubDefault(t1, t2);

    } else if (RListOrSetType(_) := t1) {
        if (isListType(t2)) return makeListType(lub(getListOrSetElementType(t1),getListElementType(t2)));
        if (isSetType(t2)) return makeSetType(lub(getListOrSetElementType(t1),getSetElementType(t2)));
        if (isListOrSetType(t2)) return makeListOrSetType(lub(getListOrSetElementType(t1),getListOrSetElementType(t2)));
        
        // Final check: just call default
        return lubDefault(t1, t2);

    } else if (RMapType(_,_) := t1) {
        // First check: if both types are maps, the lub is a map with lub'ed domain and range
        if (isMapType(t2)) {
            list[RNamedType] m1 = getMapFieldsWithNames(t1);
            list[RNamedType] m2 = getMapFieldsWithNames(t2);
            RType res = lub(RTupleType([m1[0],m1[1]]), RTupleType([m2[0],m2[1]]));
            if (RTupleType([nd,nr]) := res) return RMapType(nd,nr);
            throw "lub: unexpected result while building map: received lub result on components of <prettyPrintType(res)>";
        }
        
        // Second check: just call default
        return lubDefault(t1, t2);

    } else if (RNodeType() := t1) {
        // No explicit lub rule, just use default
        return lubDefault(t1, t2);

    } else if (RNumType() := t1) {
        // First check: lub(num,num) = num
        if (isNumType(t2)) return t1;

        // Second check: lub(num,int) and lub(num,real) = num
        if (isIntType(t2) || isRealType(t2)) return t1;

        // Final check: just call default
        return lubDefault(t1, t2);

    } else if (RRealType() := t1) {
        // First check: lub(real,real) = real
        if (isRealType(t2)) return t1;

        // Second check: lub(real,num) and lub(real,int) = num
        if (isNumType(t2) || isIntType(t2)) return makeNumType();

        // Final check: just call default
        return lubDefault(t1, t2);
        
    } else if (RSetType(_) := t1) {
        // First check: if both types are sets, the lub is a set with element the lub of the two set elements. Note: here we go ahead
        // and turn the set into a relation automatically if we can.
        if (isSetType(t2)) {
            RType res = lub(getSetElementType(t1), getSetElementType(t2));
            if (RTupleType(tels) := res)
                return RRelType(tels);
            else
                return RSetType(res);
        }

        if (isListOrSetType(t2)) return makeSetType(lub(getSetElementType(t1),getListOrSetElementType(t2)));

        // Final check: just call default
        return lubDefault(t1, t2);

    } else if (RLocType() := t1) {
        // No explicit lub rule, just use default
        return lubDefault(t1, t2);

    } else if (RStrType() := t1) {
        // No explicit lub rule, just use default
        return lubDefault(t1, t2);

    } else if (RTupleType(_) := t1) {
        // If the other type is also a tuple, and both tuples have the same number of fields, the lub is based on the lubs of the
        // individual fields. Note that we also copy over field names.
        if (isTupleType(t2)) {
            list[RNamedType] tFields1 = getTupleFieldsWithNames(t1);
            list[RNamedType] tFields2 = getTupleFieldsWithNames(t2);

            // Check to make sure the arity of both lists is the same. If so, lub each of the fields.
            if (size(tFields1) == size(tFields2) && size(tFields1) > 0) {
                list[RType] fieldLubs = [lub(getElementType(tFields1[n]),getElementType(tFields2[n])) | n <- [0..size(tFields1)-1]];

                // Determine if we should copy over field names. If both t1 and t2 have field names, we check to see if they are
                // consistent (i.e., the same names are used in the same positions). If not, we just drop the names. If only one
                // of the two tuples uses field names, we just use the names from that tuple.
                if (tupleHasFieldNames(t1) && tupleHasFieldNames(t2)) {
                    list[RName] names1 = getTupleFieldNames(t1);
                    list[RName] names2 = getTupleFieldNames(t2);
                    if (size([n | n <- [0..size(names1)-1], names1[n] != names2[n]]) > 0) {
                        return RTupleType([ RUnnamedType(rt) | rt <- fieldLubs]);
                    } else {
                        return RTupleType([ RNamedType(rt,rn) | n <- [0..size(names1)-1], rt := fieldLubs[n], rn := names1[n]]);
                    }
                } else if (tupleHasFieldNames(t1)) {
                    list[RName] names = getTupleFieldNames(t1);
                    return RTupleType([ RNamedType(fieldLubs[n],names[n]) | n <- [0..size(names)-1] ]);
                } else if (tupleHasFieldNames(t2)) {
                    list[RName] names = getTupleFieldNames(t2);
                    return RTupleType([ RNamedType(fieldLubs[n],names[n]) | n <- [0..size(names)-1] ]);
                } else {
                    return RTupleType([ RUnnamedType(rt) | rt <- fieldLubs ]);
                }
            } else if (size(tFields1) == size(tFields2)) {
                println("Found empty tuple(s), <t1>,<t2>");
                if ( (t1@at)?) println("This was at location <t1@at>");
                    return RTupleType([]);
            }
        }

        // If the other type was not a tuple, just find the default lub.
        return lubDefault(t1, t2);

    } else if (RValueType() := t1) {
        // First check: The lub of value and another type is always value
        return t1;

    } else if (RVoidType() := t1) {
        // First check: The lub of void and another type is always the other type
        return t2;

    } else if (RFunctionType(_,_) := t1) {
        // First check: t2 is equal to t1, defined as having equivalent return and parameter types
        if (isFunctionType(t2) && equivalent(getFunctionReturnType(t1),getFunctionReturnType(t2))) {
            RType params1 = (size(getFunctionArgumentTypes(t1)) == 0) ? makeVoidType() : makeTupleType(getFunctionArgumentTypes(t1));
            RType params2 = (size(getFunctionArgumentTypes(t2)) == 0) ? makeVoidType() : makeTupleType(getFunctionArgumentTypes(t2));
            if (equivalent(params1, params2)) return t1;
        }

        // TODO: See how much of lub logic for functions is needed, looks like we should use the overloaded type here (ours is more
        // general, it doesn't just hold functions)
 
        // Final check: switch to default
        return lubDefault(t1, t2);

    } else if (RReifiedType(_) := t1) {
        // First check: other type is equal
        if (isReifiedType(t2) && equivalent(getReifiedType(t1),getReifiedType(t2))) return t1;

        // Second check: other type is a reified type too, make a new reified type using the lub of the reified types
        if (isReifiedType(t2)) return makeReifiedType(lub(getReifiedType(t1),getReifiedType(t2)));

        // Final check: just try default
        return lubDefault(t1, t2);
    
    } else {
        // Log a message, we should have a check here
        println("lub, unmatched type combination <prettyPrintType(t1)>, <prettyPrintType(t2)>; internal form <t1>, <t2>");
        return makeValueType();
    }
}

//
// This function handles the default "super" case for lub. We don't need to check
// everything here, for the same reason we didn't need to in subtypeOfDefault -- not
// all types call into this code as a backup.
//
public RType lubDefault(RType t1, RType t2) {
    // Short circuit -- basic equality test, won't handle alias or type
    // var comparisons to regular types, but will handle all the int to int
    // or bool to bool type comparisons (below is redundant then, but provides
    // documentation, we don't have singletons or object equality in Rascal)
    if (t1 == t2) return t1;

    // First, handle simple equality tests. 
    if (isBoolType(t1) && isBoolType(t2)) return t1;
    if (isDateTimeType(t1) && isDateTimeType(t2)) return t1;
    if (isIntType(t1) && isIntType(t2)) return;
    if (isLocType(t1) && isLocType(t2)) return t1;
    if (isNodeType(t1) && isNodeType(t2)) return t1;
    if (isNumType(t1) && isNumType(t2)) return t1;
    if (isRealType(t1) && isRealType(t2)) return t1;
    if (isStrType(t1) && isStrType(t2)) return t1;

    // Second, see if the second type is void
    if (isVoidType(t2)) return t1;

    // Third, handle aliases
    if (isAliasType(t2)) return lub(t1, getAliasedType(t2));
    
    // When all else fails, return value
    return makeValueType();
}

//
// Returns true when two types are comparable. Two types are comparable if they are
// the same type, or if one is a subtype of the other. The equality check is built
// in to the subtype check, if t1 == t2 then t1 <: t2. This matches the definition
// of comparable given in PDB.
//
public bool comparable(RType t1, RType t2) {
    return subtypeOf(t1,t2) || subtypeOf(t2,t1);
}

//
// Check to see if two types are equivalent. Two types are equivalent if they are both
// subtypes of the other. This matches the definition of equivalent given in PDB.
//
public bool equivalent(RType t1, RType t2) {
    return subtypeOf(t1,t2) && subtypeOf(t2,t1);
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
// Find mappings between the formal parameter type, which includes any type variables we
// are binding, and the actual parameter type. These are returned as a map from type variable
// names to instantiated types.
//
public map[RName varName, RType varType] getTVBindings(RType formal, RType actual, map[RName varName, RType varType] bindings, loc l) {
    // Verify that actual <: formal, this should be the case, and it makes the code below easier since we don't need to worry
    // about cases where this does not hold.
    if (!subtypeOf(actual,formal)) throw "getTVBindings: type <actual> must be a subtype of type <formal>";

    if (isAliasType(formal)) {
        // For aliases, continue the match down through the aliased type
        bindings = getTVBindings(getAliasedType(formal), actual, bindings, l);

    } else if (isTypeVar(formal)) {
        // For type variables, we map the variable to the type. We also check the lub to make sure
        // it is a subtype of the bound, issuing an error if this is not the case.
        RName tvn = getTypeVarName(formal);
        RType tvType = tvn in bindings ? lub(bindings[tvn],actual) : actual;
        if (! subtypeOf(tvType,getTypeVarBound(formal))) {
            tvType = makeFailType("Bound of type variable <prettyPrintName(tvn)> is <prettyPrintType(getTypeVarBound(formal))>, but attempting to bind <prettyPrintType(tvType)>", l);
        }
        bindings[tvn] = tvType;

    } else if (isADTType(formal)) {
        // TODO: Should we verify that actual is an ADT or a constructor?
        // For ADTs, we also match down into the type parameters.
        if (adtHasTypeParameters(formal) && adtHasTypeParameters(actual)) {
            list[RType] tpf = getADTTypeParameters(formal);
            list[RType] tpa = getADTTypeParameters(actual);
            if (size(tpf) == size(tpa) && size(tpf) > 0) {
                for (n <- [0..size(tpf)-1]) bindings = getTVBindings(tpf[n],tpa[n],bindings,l);
            } else {
                throw "getTVBindings: ADT types have different number of type parameters, <prettyPrintType(formal)>, <prettyPrintType(actual)>";
            }
        }

	} else if (isConstructorType(formal)) {
        // TODO: Should we verify that actual is a constructor?
        // For constructors, we first match into the associated ADTs.
        bindings = getTVBindings(getConstructorResultType(formal), getConstructorResultType(actual), bindings, l);

        // Then, we match across the constructor parameters
        list[RType] ctf = getConstructorArgumentTypes(formal);
        list[RType] cta = getConstructorArgumentTypes(actual);
        if (size(ctf) == size(cta) && size(ctf) > 0) {
            for (n <- [0..size(ctf)-1]) bindings = getTVBindings(ctf[n],cta[n],bindings,l);
        } else {
            throw "getTVBindings: Constructor types have different number of arguments, <prettyPrintType(formal)>, <prettyPrintType(actual)>";
        }
		
	} else if (isListType(formal)) {
        bindings = getTVBindings(getListElementType(formal), ( isContainerType(actual) ? getContainerElementType(actual) : getListElementType(actual) ), bindings, l);

	} else if (isMapType(formal)) {
        bindings = getTVBindings(getMapDomainType(formal), getMapDomainType(actual), bindings, l);
        bindings = getTVBindings(getMapRangeType(formal), getMapRangeType(actual), bindings, l);

	} else if (isSetType(formal)) {
        bindings = getTVBindings(getSetElementType(formal), ( isContainerType(actual) ? getContainerElementType(actual) : getSetElementType(actual) ), bindings, l);

	} else if (isBagType(formal)) {
        bindings = getTVBindings(getBagElementType(formal), ( isContainerType(actual) ? getContainerElementType(actual) : getBagElementType(actual) ), bindings, l);

	} else if (isTupleType(formal)) {
        list[RType] tflds1 = getTupleFields(formal);
        list[RType] tflds2 = getTupleFields(actual);
        if (size(tflds1) == size(tflds2) && size(tflds1) > 0) {
            for (n <- [0..size(tflds1)-1]) bindings = getTVBindings(tflds1[n],tflds2[n],bindings,l);
        } else {
            throw "getTVBindings: Tuple types have different number of arguments, <prettyPrintType(formal)>, <prettyPrintType(actual)>";
        }

	} else if (isFunctionType(formal)) {
        // TODO: Add support for overloads

        // First, match across the function parameters
        list[RType] ftf = getFunctionArgumentTypes(formal);
        list[RType] fta = getFunctionArgumentTypes(actual);
        if (size(ftf) == size(fta) && size(ftf) > 0) {
            for (n <- [0..size(ftf)-1]) bindings = getTVBindings(ftf[n],fta[n],bindings,l);
        } else {
            throw "getTVBindings: Function types have different number of arguments, <prettyPrintType(formal)>, <prettyPrintType(actual)>";
        }

        // Second, match across the return types
        bindings = getTVBindings(getFunctionReturnType(formal), getFunctionReturnType(actual), bindings, l);
	
	} else if (isReifiedType(formal)) {
        bindings = getTVBindings(getReifiedType(formal), getReifiedType(actual), bindings, l);
	
	} else if (isVarArgsType(formal)) {
        bindings = getTVBindings(getTypeArgsType(formal), actual, bindings, l);
	}

    return bindings;
}

//
// Actually instantiate all type variables in the type
//
// TODO: Revisit this, this code will accidentally drop field names.
//
public RType instantiateVars(map[RName,RType] bindings, RType rt) {
    RType res = rt;

    if (isAliasType(rt)) {
        if (aliasHasTypeParameters(rt)) {
            res = makeParameterizedAliasType(getAliasName(rt), instantiateVars(bindings, getAliasedType(rt)), 
                  [instantiateVars(bindings, rti) | rti <- getAliasTypeParameters(rt)]);
		} else {
            res = makeAliasType(getAliasName(rt), instantiateVars(bindings, getAliasedType(rt)));
		}
		
	} else if (isTypeVar(rt)) {
        RName tvn = getTypeVarName(rt);
        res = tvn in bindings ? bindings[tvn] : rt;

	} else if (isRelType(rt)) {
        res = makeRelTypeFromTuple(instantiateVars(bindings, getRelElementType(rt)));

    } else if (isADTType(rt) && adtHasTypeParameters(rt)) {
        res = makeParameterizedADTType(getADTName(rt), [instantiateVars(bindings, rti) | rti <- getADTTypeParameters(rt)]);

	} else if (isConstructorType(rt)) {
        res = makeConstructorType(getConstructorName(rt), instantiateVars(bindings, getConstructorResultType(rt)), 
              instantiateVarsForNamedTypeList(bindings, getConstructorArgumentTypesWithNames(rt)));
		
	} else if (isListType(rt)) {
        res = makeListType(instantiateVars(bindings, getListElementType(rt)));

	} else if (isMapType(rt)) {
        res = makeMapTypeWithNames(instantiateVarsForNamedType(bindings, getMapFieldsWithNames(rt)[0]), 
              instantiateVarsForNamedType(bindings, getMapFieldsWithNames(rt)[1]));

	} else if (isSetType(rt)) {
        res = makeSetType(instantiateVars(bindings, getSetElementType(rt)));

	} else if (isBagType(rt)) {
        res = makeBagType(instantiateVars(bindings, getBagElementType(rt)));

	} else if (isTupleType(rt)) {
        res = makeTupleTypeWithNames(instantiateVarsForNamedTypeList(bindings, getTupleFieldsWithNames(rt)));

	} else if (isFunctionType(rt)) {
        // TODO: Add support for overloads
        res = makeFunctionTypeWithNames(instantiateVars(bindings, getFunctionReturnType(rt)),
              instantiateVarsForNamedTypeList(bindings, getFunctionArgumentTypesWithNames(rt)));

	} else if (isReifiedType(rt)) {
        res = makeReifiedType(instantiateVars(bindings, getReifiedType(rt)));
	
	} else if (isVarArgsType(rt)) {
        res = makeVarArgsType(instantiateVars(bindings, getVarArgsType(rt)));
	}

	return res;
}

private RNamedType instantiateVarsForNamedType(map[RName,RType] bindings, RNamedType rt) {
    if (RUnnamedType(t) := rt) return RUnnamedType(instantiateVars(bindings, t));
    if (RNamedType(t,tn) := rt) return RNamedType(instantiateVars(bindings, t), tn);
}

private list[RNamedType] instantiateVarsForNamedTypeList(map[RName,RType] bindings, list[RNamedType] rtl) {
    return [ instantiateVarsForNamedType(bindings, rt) | rt <- rtl ];
}

