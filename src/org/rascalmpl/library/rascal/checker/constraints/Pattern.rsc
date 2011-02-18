@bootstrapParser
module rascal::checker::constraints::Pattern

import List;
import ParseTree;
import IO;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::checker::Annotations;
import rascal::checker::TreeUtils;
import rascal::syntax::RascalRascal;

//
// Given a pattern and a subject type, attempt to bind the subject type to
// the pattern, including creating constraints to represent type assignments
// to pattern variables.
//
public ConstraintBase bindInferredTypesToPattern(ConstraintBase cb, SymbolTable st, RType rt, Pattern pat) {
    //
    // Literals in patterns: we should have a bool type and a bool pattern, int type and int pattern,
    // etc. If so, add a constraint saying the tree is of that pattern type.
    //    
    if ((Pattern)`<BooleanLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeBoolType());

    if ((Pattern)`<DecimalIntegerLiteral _>` := pat || (Pattern)`<OctalIntegerLiteral _>` := pat || (Pattern)`<HexIntegerLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeIntType());
    
    if ((Pattern)`<RealLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeRealType());

    if (isStrType(rt), (Pattern)`<StringLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeStrType());

    if (isLocType(rt), (Pattern)`<LocationLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeLocType());
        
    if (isDateTimeType(rt), (Pattern)`<DateTimeLiteral _>` := pat)
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeDateTimeType());

    //
    // For regular expression patterns, we need to pull the names inside the pattern out
    // and check them against a subject type of str.
    //
    if (isStrType(rt), (Pattern)`<RegExpLiteral rl>` := pat) {
        list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
        for (ntree <- names) {
            cb = bindInferredTypesToPattern(cb,st,makeStrType(),ntree);
        }
        cb.constraints = cb.constraints + TreeIsType(pat,pat@\loc,makeStrType());
    }
    
    //
    // Various name patterns (anonymous, qualified, etc)
    //
    if ((Pattern)`_` := pat) {
        <cs, t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(pat,pat@\loc,t1);
        if (pat@\loc in st.itemUses) {
            cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[pat@\loc],pat@\loc);
        }
        cs.constraints = cs.constraints + SubtypeOf(rt,t1,pat@\loc);   
    }
    
    if ((Pattern)`<Name n>` := pat) {
        <cs, t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(pat,pat@\loc,t1);
        if (n@\loc in st.itemUses) {
            cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[n@\loc],n@\loc);
        }
        cs.constraints = cs.constraints + SubtypeOf(rt,t1,n@\loc);   
    }
        
    if ((Pattern)`<QualifiedName qn>` := pat) {
        <cs, t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(pat,pat@\loc,t1);
        if (qn@\loc in st.itemUses) {
            cs.constraints = cs.constraints + DefinedBy(t1,(st.itemUses)[qn@\loc],qn@\loc);
        }
        cs.constraints = cs.constraints + SubtypeOf(rt,t1,qn@\loc);   
    }

    //    
    // List, set, map, and tuple patterns
    //
    if (isListType(rt), (Pattern) `[<{Pattern ","}* pl>]` := pat) {
        list[RType] lubTypes = [ ];
        for (p <- pl) {
            <cb, t1> = makeFreshType(cb);
            if ((Pattern)`[<{Pattern ","}* pl2>]` !:= p)
                t1 = SpliceableElement(t1);
            lubTypes += t1;
            cb.constraints = cb.constraints + TreeIsType(p,p@\loc,t1);
            cb = bindInferredTypesToPattern(cb,st,element,p);
        }
        <cb, t2> = makeFreshType(cb);
        cb.constraints = cb.constraints + LubOfList(lubTypes,t2,pat@\loc);
        cs.constraints = cs.constraints + TreeIsType(pat,pat@\loc,t2);
    }

    if (isSetType(rt), (Pattern) `{<{Pattern ","}* pl>}` := pat) {
        Set[RType] lubTypes = [ ];
        for (p <- pl) {
            <cb, t1> = makeFreshType(cb);
            if ((Pattern)`{<{Pattern ","}* pl2>}` !:= p)
                t1 = SpliceableElement(t1);
            lubTypes += t1;
            cb.constraints = cb.constraints + TreeIsType(p,p@\loc,t1);
            cb = bindInferredTypesToPattern(cb,st,element,p);
        }
        <cb, t2> = makeFreshType(cb);
        cb.constraints = cb.constraints + LubOfSet(lubTypes,t2,pat@\loc);
        cs.constraints = cs.constraints + TreeIsType(pat,pat@\loc,t2);
    }
    
       // Tuple with just one element
        // TODO: Ensure fields persist in the match, they don't right now
        case (Pattern) `<<Pattern pi>>` : {
            if (isTupleType(rt) && isTupleType(pt)) {
                list[RType] tupleFields = getTupleFields(rt);
                if (size(tupleFields) == 1) {
                    RType resultType = bindInferredTypesToPattern(head(tupleFields),pi);
                    if (isFailType(resultType))
                        return resultType;
                    else
                        return makeTupleType([resultType]);
                } else {
                    return makeFailType("Tuple type in subject <prettyPrintType(rt)> has more fields than tuple type in pattern <pat>, <prettyPrintType(pat@rtype)>",pat@\loc);
                }
            } else {
                return makeFailType("tuple pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
            }
        }

        // Tuple with more than one element
        // TODO: Ensure fields persist in the match, they don't right now
        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
            if (isTupleType(rt) && isTupleType(pt)) {
                list[RType] tupleFields = getTupleFields(rt);
                list[Pattern] patternFields = [pi] + [p | p <- pl];
                
                if (size(tupleFields) == size(patternFields)) {
                    list[RType] elementTypes = [ ];
                    for (n <- [0..size(tupleFields)-1])
                        elementTypes += bindInferredTypesToPattern(tupleFields[n],patternFields[n]);
                    if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                    return makeTupleType(elementTypes);
                } else {
                    return makeFailType("Tuple type in subject <prettyPrintType(rt)> has a different number of fields than tuple type in pattern <pat>, <prettyPrintType(pat@rtype)>",pat@\loc);
                }
            } else {
                return makeFailType("tuple pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
            }
        }
    
    
    switch(pat) {
        // TODO: ReifiedType, see if we need to expand matching for this
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            if (RReifiedType(bt) := rt) {
                return rt; // TODO: Will need to change to really get this working, just return the binder type for now
            } else {
                return makeFailType("Type of pattern, <prettyPrintType(pt)>, is not compatible with the type of the binding expression, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // CallOrTree
        // This handles two different cases. In the first, the binding code is invoked when we handle
        // a constructor pattern to assign types to the variables. In that case, we actually have the
        // full signature of the constructor, so we have the information for each field in the
        // pattern. In the second, the binding code is invoked during a match or enumeration, so
        // we don't actually have explicit constructor types, just the ADT type. In that case, we
        // can't descend into the pattern, we just have to compare the ADT types of the constructor
        // and the type of the binding type (rt).
        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            list[Pattern] patternFields = [p | p <- pl];
            RType patternType = pat@fctype; // Get back the constructor type used, not the ADT types
            if (isConstructorType(patternType) && isConstructorType(rt) && size(getConstructorArgumentTypes(patternType)) == size(patternFields)) {
                set[RType] potentialFailures = { };
                list[RType] rtArgTypes = getConstructorArgumentTypes(rt); 
                for (n <- domain(rtArgTypes))
                    potentialFailures += bindInferredTypesToPattern(rtArgTypes[n],patternFields[n]);
                if (checkForFail(potentialFailures)) return collapseFailTypes(potentialFailures);
                return getConstructorResultType(patternType);
            } else if (isADTType(pt) && isADTType(rt) && subtypeOf(rt,pt)) {
                return pt; // TODO: Firm this up
            } else {
                return makeFailType("Actual type, <prettyPrintType(rt)>, is incompatible with the pattern type, <prettyPrintType(pt)>",pat@\loc);
            }
        }

 
        // Typed Variable: a variable of type t can match a subject of type rt when rt <: t
        // TODO: Special rules for scalars vs nodes/ADTs? May make sense to say they can match
        // when, with allSubtypes being the set of all possible subtypes of t,
        // size(allSubtypes(pt) inter allSubtypes(rt)) > 0, i.e., when the actual type of each,
        // which is a subtype of the static type, could be shared...
        case (Pattern) `<Type t> <Name n>` : {
            if (subtypeOf(rt,pt))
                return pt;
            else
                return makeFailType("not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
        }
        
        // Multi Variable patterns, _* and QualifiedName*
        case (Pattern)`_ *` : {
            return bindInferredTypesToMV(rt, getTypeForNameLI(globalSymbolTable,RSimpleName("_"),pat@\loc), pat);
        }
        
        case (Pattern) `<QualifiedName qn> *` : {
            return bindInferredTypesToMV(rt, getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc), pat);
        }

        // Descendant
        // Since this pattern is inside something, we use the subject type (rt) to determine what it is
        // inside. If p is itself just an inferred type (e.g., p = / x) then we use rt to figure
        // out what x can hold, which is: the lub of all the types reachable through rt. If p has
        // a type of some sort at the top level, we check to see if that can be used inside rt.
        // If so, and if it contains inferred or deferred types, we push down a lub of the matching
        // types in rt. If so, and if it has no deferred types, we just use that type, if it can
        // occur inside rt.
        // 
        // NOTE: We actually return rt as the type of / x, not lub(reachable(rt)). This is because
        // pattern / x essentially stands in for rt in this case, if we have [_*,/ x,_*] for instance,
        // and we use this to indicate that the second position actually has an rt which we are
        // picking apart.
        case (Pattern) `/ <Pattern p>` : {
            if ( isInferredType(p@rtype) ) {
                    set[RType] rts = reachableTypes(globalSymbolTable, rt);
                RType bt = bindInferredTypesToPattern(lubSet(rts), p);
                return isFailType(bt) ? bt : rt;
            } else if ( (! isInferredType(p@rtype)) && (hasDeferredTypes(p@rtype))) {
                    set[RType] rts = reachableTypes(globalSymbolTable, rt);
                rts = { rtsi | rtsi <- rts, subtypeOf(rtsi, p@rtype)};
                RType bt = bindInferredTypesToPattern(lubSet(rts), p);
                return isFailType(bt) ? bt : rt;
            } else {
                    set[RType] rts = reachableTypes(globalSymbolTable, rt);
                if (p@rtype in rts) return rt;
                return makeFailType("Pattern type <prettyPrintType(p@rtype)> cannot appear in type <prettyPrintType(rt)>", pat@\loc);
            }
        }

        // Variable Becomes
        case (Pattern) `<Name n> : <Pattern p>` : {
            RType boundType = bindInferredTypesToPattern(rt, p);
            if (! isFailType(boundType)) {
                    RType nType = getTypeForNameLI(globalSymbolTable,convertName(n),n@\loc);
                    RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
                    if (isInferredType(t)) {
                        updateInferredTypeMappings(t,boundType);
                        return boundType;
                    } else if (! equivalent(t,boundType)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(boundType)>", n@\loc);
                    } else {
                        return t; // or boundType, types are equal
                    }
            }
            return boundType;
        }
        
        // Typed Variable Becomes
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            if (subtypeOf(rt,pt)) {
                RType resultType = bindInferredTypesToPattern(rt, p);
                if (isFailType(resultType)) return resultType;
                return pt;
            } else {
                return makeFailType("Not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
            }
        }
        
        // Guarded
        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            if (subtypeOf(rt,pt)) {
                RType resultType = bindInferredTypesToPattern(rt, p);
                if (isFailType(resultType)) return resultType;
                return pt;
            } else {
                return makeFailType("Not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
            }
        }           
        
        // Anti -- TODO see if this makes sense, check the interpreter
        case (Pattern) `! <Pattern p>` : {
            return bindInferredTypesToPattern(rt, p);
        }
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
        // pat[0] is the production used, pat[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := pat[0]) {
            if (isMapType(rt) && isMapType(pt)) {
                    RType t = bindInferredTypesToMapPattern(rt, pat);
                        return t;
                } else {
                        return makeFailType("map pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
                }
    }

    throw "Missing case on checkPattern for pattern <pat>";
}

//
// Bind any variables used in the map pattern to the types present in type rt.
//
public RType bindInferredTypesToMapPattern(RType rt, Pattern pat) {
    RType mapDomain = getMapDomainType(rt);
    RType mapRange = getMapRangeType(rt);

    list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
    if (size(mapContents) == 0) return makeMapType(makeVoidType(), makeVoidType());

    list[RType] domains; list[RType] ranges;
    for (<md,mr> <- mapContents) { 
        domains += bindInferredTypesToPattern(mapDomain, pl);
        ranges += bindInferredTypesToPattern(mapRange, pr);
    }

    if (checkForFail(toSet(domains+ranges))) return collapseFailTypes(toSet(domains+ranges));
    return makeMapType(lubList(domains),lubList(ranges));   
}

//
// Bind inferred types to multivar names: _* and QualifiedName*
//
public RType bindInferredTypesToMV(RType rt, RType pt, Pattern pat) {
        RType retType;

    // Make sure the type we are given is actually one that can contain elements
    if (! (isListType(rt) || isSetType(rt) || isContainerType(rt))) {
            return makeFailType("Attempting to bind type <prettyPrintType(rt)> to a multivariable <pat>",pat@\loc);
    }

    // Make sure that the type we are given is compatible with the type of the container variable
    if ( ! ( (isListType(rt) && (isListType(pt) || isContainerType(pt))) ||
                 (isSetType(rt) && (isSetType(pt) || isContainerType(pt))) ||
                 (isContainerType(rt) && isContainerType(pt)))) {
            return makeFailType("Attempting to bind type <prettyPrintType(rt)> to an incompatible container type <prettyPrintType(pt)>",pat@\loc);
        }

        // This should be structured as RContainerType(RInferredType(#)) unless we have assigned a more specific
    // type between creation and now. It should always be a container (list, set, or container) of some sort.
    if (isContainerType(pt) || isListType(pt) || isSetType(pt)) {
                RType elementType;
        bool elementIsInferred = false;
            if (isContainerType(pt)) {
                    elementIsInferred = (isInferredType(getContainerElementType(pt))) ? true : false;
            elementType = (isInferredType(getContainerElementType(pt))) ?
                    globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getContainerElementType(pt))] :
                getContainerElementType(pt);
        } else if (isListType(pt)) {
            elementIsInferred = (isInferredType(getListElementType(pt))) ? true : false;
            elementType = (isInferredType(getListElementType(pt))) ?
                    globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getListElementType(pt))] : 
                    getListElementType(pt);
        } else if (isSetType(pt)) {
            elementIsInferred = (isInferredType(getSetElementType(pt))) ? true : false;
            elementType = (isInferredType(getSetElementType(pt))) ?
                    globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getSetElementType(pt))] : 
                getSetElementType(pt);
        }

        // Get the type of element inside the type being bound
        RType relementType = isContainerType(rt) ? getContainerElementType(rt) : (isListType(rt) ? getListElementType(rt) : getSetElementType(rt));

        if (elementIsInferred) {
                // The element type is inferred. See if it still is open -- if it still points to an inferred type.
            if (isInferredType(elementType)) {
                        // Type still open, update mapping
                updateInferredTypeMappings(elementType,relementType);
                retType = rt;
                    } else if (! equivalent(elementType, relementType)) {
                        // Already assigned a type, issue a failure, attempting to bind multiple types to the same var
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <pat>: already bound element type as <prettyPrintType(elementType)>, attempting to bind new element type <prettyPrintType(relementType)>", pat@\loc);
            } else {
                // Trying to assign the same type again, which is fine, just return it.
                retType = rt;
                }
        } else {
                // The element type is NOT an inferred type. The type of rt must match exactly.
                if (! equivalent(elementType, relementType)) {
                    retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <pat>: already bound element type as <prettyPrintType(elementType)>, attempting to bind new element type <prettyPrintType(relementType)>", pat@\loc);
            } else {
                retType = rt;
            }  
        }
    } else {
            throw "Unexpected type assigned to container var at location <pat@\loc>: <prettyPrintType(pt)>";
    }
    
        return retType;
}

