@bootstrapParser
module rascal::checker::constraints::Assignable

import List;
import ParseTree;
import rascal::checker::Types;
import rascal::checker::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Check assignables.
//
// NOTE: This system uses a pair of types, referred to below as the "part type" and the
// "whole type". This is because, in cases like x.f = 3, we need to know the entire
// type of the resulting value, here the type of x, as well as the type of the part of
// x being assigned into, here the type of field f on x. In this example, the type of x
// is the whole type, while the type of the field f is the part type.
//

//
// TODO: Not sure what to return here, since, for a tuple, this could be any of the
// types of the tuple fields. So, for tuples, just return the lub right now, which will
// let the test pass. Returning void would be more conservative, but then this would
// never work for tuples.
//
// NOTE: Doing this for relations with arity > 2 doesn't seem to work right now in the
// interpreter. I'm not sure if this is by design or by accident.
//
// TODO: Review this code, it's complex and could have hidden bugs...
//
public RType checkSubscriptAssignable(Assignable ap, Assignable a, Expression e) {
    if (checkForFail({a@rtype, e@rtype})) return collapseFailTypes({a@rtype, e@rtype});

    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);

    if (isTupleType(partType)) {
        return makeAssignableType(wholeType, lubList(getTupleFields(partType)));        
    } else if (isRelType(partType)) {
        list[RType] relTypes = getRelFields(partType);
        RType relLeftType = relTypes[0];
        list[RType] resultTypes = tail(relTypes);
        if (! (subtypeOf(e@rtype, relLeftType))) return makeFailType("The subscript type <prettyPrintType(e@rtype)> must be a subtype of the first project of the relation type, <prettyPrintType(relLeftType)>", ap@\loc);
        if (size(resultTypes) == 1)
            return makeAssignableType(wholeType, makeSetType(resultTypes[0]));
        else
            return makeAssignableType(wholeType, makeRelType(resultTypes));     
    } else if (isMapType(partType)) {
        RType domainType = getMapDomainType(partType);
        if (! subtypeOf(e@rtype, domainType)) return makeFailType("The subscript type <prettyPrintType(e@rtype)> must be a subtype of to the domain type <prettyPrintType(domainType)>", ap@\loc);
        return makeAssignableType(wholeType, getMapRangeType(partType));
    }  else if (isNodeType(partType)) {
        return makeAssignableType(wholeType, makeValueType());
    } else if (isListType(partType)) {
        if (! isIntType(e@rtype) ) 
                        return makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(e@rtype)>", ap@\loc);
        return makeAssignableType(wholeType, getListElementType(partType));     
    } else {
        return makeFailType("Subscript not supported on type <prettyPrintType(partType)>", ap@\loc);
    }
}

//
// A field access assignable is of the form a.f, where a is another assignable. The whole
// type is just the type of a, since f is a field of a and ultimately we will return an
// a as the final value. The part type is the type of f, since this is the "part" being
// assigned into. We check for the field on the part type of the assignable, since the
// assignable could be of the form a.f1.f2.f3, or a[n].f, etc, and the type with the field 
// is not a as a whole, but a.f1.f2, or a[n], etc.
//
public RType checkFieldAccessAssignable(Assignable ap, Assignable a, Name n) {
    if (checkForFail({a@rtype})) return collapseFailTypes({a@rtype});
    RType partType = getPartType(a@rtype); // The "part" of a which contains the field
    RType wholeType = getWholeType(a@rtype); // The overall type of all of a
    RType fieldType = getFieldType(partType, convertName(n), globalSymbolTable, ap@\loc);
    if (isFailType(fieldType)) return fieldType;
    return makeAssignableType(wholeType, fieldType); 
}

//
// An if-defined-or-default assignable is of the form a ? e, where a is another assignable
// and e is the default value. We propagate up both the whole and part types, since this
// impacts neither. For instance, if we say a.f1.f2 ? [ ], we are still going to assign into
// f2, so we need that information. However, we need to check to make sure that [ ] could be
// assigned into f2, since it will actually be the default value given for it if none exists.
//      
public RType checkIfDefinedOrDefaultAssignable(Assignable ap, Assignable a, Expression e) {
    if (isFailType(a@rtype) || isFailType(e@rtype)) return collapseFailTypes({ a@rtype, e@rtype });
    RType partType = getPartType(a@rtype); // The "part" being checked for definedness
    RType wholeType = getWholeType(a@rtype); // The "whole" being assigned into
    if (!subtypeOf(e@rtype,partType)) 
                return makeFailType("The type of <e>, <prettyPrintType(e@rtype)>, is not a subtype of the type of <a>, <prettyPrintType(partType)>",ap@\loc);
    return makeAssignableType(wholeType, partType); // Propagate up the current part and whole once we've made sure the default is valid
}

//
// An annotation assignable is of the form a @ n, where a is another assignable and
// n is the annotation name on this assignable. The whole type for a is propagated
// up, with the new part type being the type of this annotation, which should be a valid
// annotation on the current part type.
//
// TODO: Ensure that annotation n is a valid annotation on the part type of a
//
public RType checkAnnotationAssignable(Assignable ap, Assignable a, Name n) {
    if (isFailType(a@rtype)) return collapseFailTypes({ a@rtype });
    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);
    RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
    if (isFailType(rt)) return rt;
    return makeAssignableType(wholeType, rt);
}

//
// A tuple assignable is of the form < a_1, ..., a_n >, where a_1 ... a_n are
// other assignables. For tuple assignables, the part type is a tuple of the
// part types of the constituent assignables, while the whole type is the tuple
// of the whole types of the constituent assignables. This is because we will
// ultimately return a tuple made up of the various assignables, but we will
// also assign into the part types of each of the assignables.
//      
public RType checkTrivialTupleAssignable(Assignable ap, Assignable a) {
    list[Assignable] alist = [ a ];
    if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
    RType wholeType = makeTupleType([ getWholeType(ai@rtype) | ai <- alist]);
    RType partType = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
    return makeAssignableType(wholeType,partType);
}

public RType checkTupleAssignable(Assignable ap, Assignable a, {Assignable ","}* al) {
    list[Assignable] alist = [ a ] + [ ai | ai <- al];
    if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
    RType wholeType = makeTupleType([ getWholeType(ai@rtype) | ai <- alist]);
    RType partType = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
    return makeAssignableType(wholeType,partType);
}

//
// Check assignables.
//
public RType checkAssignable(Assignable a) {
    switch(a) {
        // Variable _
        case (Assignable)`_` : {
            RType rt = getTypeForName(globalSymbolTable, RSimpleName("_"), a@\loc);
            return makeAssignableType(rt,rt); 
        }

        // Variable with an actual name
        case (Assignable)`<QualifiedName qn>` : {
            RType rt = getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
            return makeAssignableType(rt,rt); 
        }
        
        // Subscript
        case `<Assignable al> [ <Expression e> ]` : {
            return checkSubscriptAssignable(a,al,e);
        }
        
        // Field Access
        case `<Assignable al> . <Name n>` : {
            return checkFieldAccessAssignable(a,al,n);
        }
        
        // If Defined or Default
        case `<Assignable al> ? <Expression e>` : {
            return checkIfDefinedOrDefaultAssignable(a,al,e);
        }
        
        // Annotation
        case `<Assignable al> @ <Name n>` : {
            return checkAnnotationAssignable(a,al,n);
        }
        
        // Tuple, with just one element
        case (Assignable)`< <Assignable ai> >` : {
            return checkTupleAssignable(a, ai);
        }

        // Tuple, with multiple elements
        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            return checkTupleAssignable(a, ai, al);
        }
    }
}

//
// Given an actual type rt and an assignable a, recurse the structure of a, assigning the correct parts of
// rt to any named parts of a. For instance, in an assignment like x = 5, if x has an inference type it will
// be assigned type int, while in an assignment like <a,b> = <true,4>, a would be assigned type bool
// while b would be assigned type int (again, assuming they are both inferrence variables). The return
// type is the newly-computed type of the assignable, with inference vars bound to concrete types.
//
// NOTE: This functionality now does much more checking, similarly to the bind logic in patterns,
// since we also now do all the subtype checking here as well.
//
public RType bindInferredTypesToAssignable(RType rt, Assignable a) {
    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);
    
    switch(a) {
        // Anonymous name (variable name _)
            // When assigning into _, we make sure that either the type assigned to _ is still open or that the type we are
        // assigning is a subtype. Realistically, it should always be open, since each instance of _ is distinct.
        case (Assignable)`_` : {
                RType varType = getTypeForNameLI(globalSymbolTable, RSimpleName("_"), a@\loc);
                if (isInferredType(varType)) {
                    RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(varType)];
                if (isInferredType(t)) {
                        updateInferredTypeMappings(t,rt);
                    return rt;
                } else if (! equivalent(t,rt)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name: already bound <prettyPrintType(t)>, attempting to bind <prettyPrintType(rt)>", a@\loc);
                } else {
                        return rt;
                }
            } else {
                    if (subtypeOf(rt, varType)) {
                        return varType;
                } else {
                        return makeFailType("Type <prettyPrintType(rt)> must be a subtype of the type of <a>, <prettyPrintType(varType)>",a@\loc);
                }
            }
        }

        // Qualified Name (variable name)
        // When assigning into a name, we make sure that either the type assigned to the name is still open or that the
        // type we are assigning is a subtype.
        // NOTE: This includes a terrible hack to handle situations such as x = { }; x = { 1 } which don't work right now.
        // This allows container (set/list/map) types to be bumped up from void to non-void element types. However, this
        // is not sound, so we need to instead divise a better way to handle this, for instance by using constraint systems.
        // so, TODO: Fix this!
        case (Assignable)`<QualifiedName qn>` : {
                RType varType = getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc);
            if (isInferredType(varType)) {
                RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(varType)];
                if (isInferredType(t)) {
                    updateInferredTypeMappings(t,rt);
                    return rt;
                } else if ( (isListType(t) && isVoidType(getListElementType(t)) && isListType(rt)) || 
                                            (isSetType(t) && isVoidType(getSetElementType(t)) && isSetType(rt)) ||
                                            (isMapType(t) && isVoidType(getMapDomainType(t)) && isVoidType(getMapRangeType(t)) && isMapType(rt))) {
                        updateInferredTypeMappings(varType,rt);
                    return rt;
                } else if ( (isListType(t) && isListType(rt) && isVoidType(getListElementType(rt))) ||
                            (isSetType(t) && isSetType(rt) && isVoidType(getSetElementType(rt))) ||
                        (isMapType(t) && isMapType(rt) && isVoidType(getMapDomainType(rt)) && isVoidType(getMapRangeType(rt)))) {
                    return t;
                    } else if (! equivalent(t,rt)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed name <qn>: already bound <prettyPrintType(t)>, attempting to bind <prettyPrintType(rt)>", qn@\loc);
                    } else {
                        return rt; 
                    }
            } else {
                    if (subtypeOf(rt, varType)) {
                        return varType;
                } else {
                        return makeFailType("Type <prettyPrintType(rt)> must be a subtype of the type of <a>, <prettyPrintType(varType)>",a@\loc);
                }
            }
        }
        
        // Subscript
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type. Checking the assignable already checked the structure of
        // the whole type.
        case `<Assignable al> [ <Expression e> ]` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to subscript with type <prettyPrintType(partType)>", a@\loc);
            return getWholeType(a@rtype);
        }
        
        // Field Access
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type.
        case `<Assignable al> . <Name n>` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to field with type <prettyPrintType(partType)>", 
                                                    a@\loc);
            return getWholeType(a@rtype);
        }
        
        // If Defined or Default
        // This just pushes the binding down into the assignable on the left-hand
        // side of the ?, the default expression has no impact on the binding.
        case `<Assignable al> ? <Expression e>` : {
            return bindInferredTypesToAssignable(rt, al);
        }
        
        // Annotation
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type.
        case `<Assignable al> @ <Name n>` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to field with type <prettyPrintType(partType)>", 
                                                    a@\loc);
            return getWholeType(a@rtype);
        }
        
        // Tuple
        // To be correct, the type being bound into the assignable also needs to be a tuple
        // of the same length. If this is true, the bind recurses on each tuple element.
        // If not, a failure type, indicating the type of failure (arity mismatch, or type of
        // assignable not a tuple) has occurred.
        case (Assignable)`< <Assignable ai> >` : {
            list[Assignable] alist = [ai];
            if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
                list[RType] tupleFieldTypes = getTupleFields(rt);
                results = [bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]];
                failures = { result | result <- results, isFailType(result) };
                if (size(failures) > 0) return collapseFailTypes(failures);
                return makeTupleType(results);
            } else if (!isTupleType(rt)) {
                return makeFailType("Type mismatch: cannot assign non-tuple type <prettyPrintType(rt)> to <a>", a@\loc);
            } else {
                return makeFailType("Arity mismatch: cannot assign tuple of length <getTupleFieldCount(rt)> to <a>", a@\loc);
            }
        }

        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            list[Assignable] alist = [ai] + [ ali | ali <- al ];
            if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
                list[RType] tupleFieldTypes = getTupleFields(rt);
                results = [bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]];
                failures = { result | result <- results, isFailType(result) };
                if (size(failures) > 0) return collapseFailTypes(failures);
                return makeTupleType(results);
            } else if (!isTupleType(rt)) {
                return makeFailType("Type mismatch: cannot assign non-tuple type <prettyPrintType(rt)> to <a>", a@\loc);
            } else {
                return makeFailType("Arity mismatch: cannot assign tuple of length <getTupleFieldCount(rt)> to <a>", a@\loc);
            }
        }
        
    }
}

//
// Calculate the list of types assigned to a list of parameters
//
public list[RType] getParameterTypes(Parameters p) {
    list[RType] pTypes = [];

    if (`( <Formals f> )` := p && (Formals)`<{Formal ","}* fs>` := f) {
        for ((Formal)`<Type t> <Name n>` <- fs) {
                pTypes += getTypeForName(globalSymbolTable,convertName(n),n@\loc);
        }
    } else if (`( <Formals f> ... )` := p && (Formals)`<{Formal ","}* fs>` := f) {
        for ((Formal)`<Type t> <Name n>` <- fs) {
                pTypes += getTypeForName(globalSymbolTable,convertName(n),n@\loc);
        }
        // For varargs, mark the last parameter as the variable size parameter; if we have no
        // parameters, then we add one, a varargs which accepts anything
        if (size(pTypes) > 0)
            pTypes[size(pTypes)-1] = RVarArgsType(pTypes[size(pTypes)-1]);
        else
            pTypes = [ RVarArgsType(makeValueType()) ];
    }

    return pTypes;
}

//
// Figure the type of value that would be assigned, based on the assignment statement
// being used. This returns a fail type if the assignment is invalid. 
//
public RType getAssignmentType(RType t1, RType t2, RAssignmentOp raOp, loc l) {
    if (aOpHasOp(raOp)) {
        RType expType = expressionType(t1,t2,opForAOp(raOp),l);
        if (isFailType(expType)) return expType;
        if (subtypeOf(expType, t1)) return t1;
        return makeFailType("Invalid assignment of type <prettyPrintType(expType)> into variable of type <prettyPrintType(t1)>",l);
    } else if (raOp in { RADefault(), RAIfDefined() }) {
        if (subtypeOf(t2,t1)) {
            return t1;
        } else {
            return makeFailType("Invalid assignment of type <prettyPrintType(t2)> into variable of type <prettyPrintType(t1)>",l);
        }
    } else {
        throw "Invalid assignment operation: <raOp>";
    }
}