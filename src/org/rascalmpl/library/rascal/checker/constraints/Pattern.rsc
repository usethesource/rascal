@bootstrapParser
module rascal::checker::constraints::Pattern

import List;
import ParseTree;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::syntax::RascalRascal;

//
// Check the type of a reified type pattern.
//
// TODO: Should add additional checks, including an arity check, since you can only have
// one type inside the pattern (even though the syntax allows more).
//
// TODO: Need to expand type so we know that ADTs, etc are marked.
//
// TODO: pl should all have type type
//
public RType checkReifiedTypePattern(Pattern pp, Type t, {Pattern ","}* pl) {
    if (checkForFail({ p@rtype | p <- pl })) return collapseFailTypes({ p@rtype | p <- pl });
    return makeReifiedType(convertType(t), [ p@rtype | p <- pl ]);
}

//
// TODO: this takes a strict view of what a static type error is for patterns. We
// may want a more relaxed version, where if someone uses a pattern that could never
// match we just let it go, since this won't cause a runtime error (but it may be
// useful for the user to know)
//
public RType checkCallOrTreePattern(Pattern pp, Pattern pc, {Pattern ","}* ps) {
    list[RType] matches = getCallOrTreePatternType(pp, pc, ps);
    if (size(matches) > 1) { 
        return makeFailType("There are multiple possible matches for this constructor pattern. Please add additional type information. Matches: <prettyPrintTypeListWLoc(matches)>");
    } else if (size(matches) == 1 && rt := head(matches) && isFailType(rt)) {
        return rt;
    } else if (size(matches) == 1 && rt := head(matches) && isConstructorType(rt)) {
        RType boundType = bindInferredTypesToPattern(rt, pp[@rtype=getConstructorResultType(rt)][@fctype=rt]);
        return rt;
    } else {
        throw "Unexpected situation, checkCallOrTreePattern, found the following matches: <matches>";
    }
}

//
// Find the type of a call or tree pattern. This has to be the use of a constructor -- functions
// invocations can't be used in patterns. So, this function needs to figure out which constructor
// is being used. Note that this is a local determination, i.e., we don't currently allow
// information from the surrounding context to help. So, we have to be able to determine the type
// just from looking at the constructor name and its pattern.
//
// TODO: See if we need to allow contextual information. We may need this in cases where (for instance)
// we have two constructors C of two different ADTs, and we want to be able to use matches such
// as C(_) :=.
//
// TODO: See how much error information we can gather. Currently, we just return if pc or ps
// contains any failures. However, in some situations we could get more error info, for instance
// if pc has a normal type but there are no constructors with that name that take the given
// number of parameters.
//
public list[RType] getCallOrTreePatternType(Pattern pp, Pattern pc, {Pattern ","}* ps) {
    // First, if we have any failures, just propagate those upwards, don't bother to
    // check the rest of the call. 
    if (checkForFail({ pc@rtype } + { p@rtype | p <- ps }))
        return [ collapseFailTypes({ pc@rtype } + { p@rtype | p <- ps }) ];
            
    // Set up the possible alternatives. We will treat the case of no overloads as a trivial
    // case of overloading with only one alternative.
    set[ROverloadedType] alternatives = isOverloadedType(pc@rtype) ? getOverloadOptions(pc@rtype) : { ( (pc@rtype@at)? ) ? ROverloadedTypeWithLoc(pc@rtype,pc@rtype@at) :  ROverloadedType(pc@rtype) };
    
    // Now, try each alternative, seeing if one matches. Note: we could have multiple matches (for
    // instance, if we have inference vars in a constructor), even if the instances themselves
    // did not overlap. e.g., S(int,bool) and S(str,loc) would not overlap, but both would
    // be acceptable alternatives for S(x,y) := e. At this point, we can just return both; the caller
    // can decide if this is acceptable or not.
    list[RType] matches = [ ];
    set[RType] failures = { };
    list[Pattern] actuals = [ p | p <- ps ];
        
    for (a <- alternatives) {
            bool typeHasLoc = ROverloadedTypeWithLoc(_,_) := a;
        RType fcType = typeHasLoc ?  a.overloadType[@at=a.overloadLoc] : a.overloadType;
        
        if (isConstructorType(fcType)) {
            list[RType] formals = getConstructorArgumentTypes(fcType);

            // NOTE: We do not currently support varargs constructors.
            if (size(formals) == size(actuals)) {
                set[RType] localFailures = { };
                for (idx <- domain(actuals)) {
                    RType actualType = actuals[idx]@rtype;
                    RType formalType = formals[idx];
                    if (! subtypeOf(actualType, formalType)) {
                        localFailures = localFailures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: pattern type for pattern argument <actuals[idx]> is <prettyPrintType(actuals[idx]@rtype)> but argument type is <prettyPrintType(formalType)>",actuals[idx]@\loc);
                    }
                }
                if (size(localFailures) > 0) {
                    failures = failures + localFailures;
                } else {
                    matches = matches + ( typeHasLoc ? fcType[@at=a.overloadLoc ] : fcType ); 
                }
            } else {
                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: constructor accepts <size(formals)> arguments while pattern <pp> has arity <size(actuals)>", pp@\loc);
            }
        } else {
            failures = failures + makeFailType("Type <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : ""> is not a constructor",pp@\loc);
        }
    }

    // If we found a match, use that. If not, send back the failures instead. The matches take precedence
    // since failures can result from trying all possible constructors in an effort to find the matching
    // constructor, which is the constructor we will actually use.  
    if (size(matches) > 0)
        return matches;
    else
        return [ collapseFailTypes(failures) ];
}

//
// This handles returning the correct type for a pattern in a list. There are several cases:
//
// 1. A name that represents a list. This can be treated like an element of the list, since [1,x,2], where x
//    is [3,4], just expands to [1,3,4,2]. More formally, in these cases, if list(te) := t, we return te.
//
// 2. A pattern that is explicitly given a name or typed name (name becomes patterns) or guarded pattern. Here
//    we look at the next level of pattern and treat it according to these rules.
//
// 3. All other patterns. Here we just return the type of the pattern.
//  
public RType getPatternTypeForList(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isListType(patType)) return getListElementType(patType);
        if (isContainerType(patType)) return getContainerElementType(patType);    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return getPatternTypeForList(p);
    }
    return pat@rtype;
}

//
// Indicates if a variable is a list container variable. Uses the same rules as the above.
// 
public bool isListContainerVar(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isListType(patType)) return true;
        if (isContainerType(patType)) return true;    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return isListContainerVar(p);
    }
    return false;
}

//
// Determine the type of a list pattern. This is based on the types of its components.
// It may not be possible to determine an exact type, in which case we delay the
// computation of the lub by returning a list of lub type.
//
public RType checkListPattern(Pattern pp, {Pattern ","}* ps) {
    if (checkForFail({ p@rtype | p <- ps })) return collapseFailTypes({ p@rtype | p <- ps });
    
    // Get the types in the list, we need to watch for inferrence types since we need
    // to handle those separately. We also need to match for lub types that are
    // propagating up from nested patterns.
    list[RType] patTypes = [ getPatternTypeForList(p) | p <- ps ];
    list[RType] patTypesI = [ t | t <- patTypes, hasDeferredTypes(t) ];
    
    if (size(patTypesI) > 0) {
        return makeListType(makeLubType(patTypes));
    } else {
        return makeListType(lubList(patTypes));
    }
}

//
// This handles returning the correct type for a pattern in a set. This uses the same rules
// given above for getPatternTypeForList, so refer to that for more details.
//  
public RType getPatternTypeForSet(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isSetType(patType)) return getSetElementType(patType);
        if (isContainerType(patType)) return getContainerElementType(patType);    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return getPatternTypeForSet(p);
    }
    return pat@rtype;
}

//
// Indicates if a variable is a set container variable. Uses the same rules as the above.
// 
public bool isSetContainerVar(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isSetType(patType)) return true;
        if (isContainerType(patType)) return true;    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return isSetContainerVar(p);
    }
    return false;
}       

//
// Determine the type of a set pattern. This is based on the types of its components.
// It may not be possible to determine an exact type, in which case we delay the
// computation of the lub by returning a set of lub type.
//
public RType checkSetPattern(Pattern pp, {Pattern ","}* ps) {
    if (checkForFail({ p@rtype | p <- ps })) return collapseFailTypes({ p@rtype | p <- ps });

    // Get the types in the list, we need to watch for inferrence types since we need
    // to handle those separately.  We also need to match for lub types that are
    // propagating up from nested patterns.
    list[RType] patTypes = [ getPatternTypeForSet(p) | p <- ps ];
    list[RType] patTypesI = [ t | t <- patTypes, hasDeferredTypes(t)];
    
    if (size(patTypesI) > 0) {
        return makeSetType(makeLubType(patTypes));
    } else {
        return makeSetType(lubList(patTypes));
    }
}

//
// Check the type of a trivial (one element) tuple pattern, which is either
// tuple[t] when pi : t or fail when pi has a fail type.
//
public RType checkTrivialTuplePattern(Pattern pp, Pattern pi) {
    set[Pattern] pset = {pi};
    if (checkForFail({p@rtype | p <- pset})) return collapseFailTypes({p@rtype | p <- pset});
    return makeTupleType([ p@rtype | p <- pset]);
}

//
// Check the type of a non-trivial (multiple element) tuple pattern.
//
public RType checkTuplePattern(Pattern pp, Pattern pi, {Pattern ","}* ps) {
    list[Pattern] plist = [pi] + [ p | p <- ps ];
    if (checkForFail({p@rtype | p <- plist})) return collapseFailTypes({p@rtype | p <- plist});
    return makeTupleType([ p@rtype | p <- plist]);
}

//
// Check the variable becomes pattern. Note that we don't bind the pattern type to
// the name here, since we don't actually have a real type yet for the pattern -- it
// itself could contain inference vars, etc. We wait until the bind function is
// called to do this.
//
public RType checkVariableBecomesPattern(Pattern pp, Name n, Pattern p) {
    RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
    if (checkForFail({ rt, p@rtype })) return collapseFailTypes({ rt, p@rtype });
    return p@rtype;
}

//
// Check the typed variable becomes pattern. We require that the pattern type is
// a subtype of the name type, since otherwise we cannot assign it. Note: we ignore
// the type t here since the process of building the symbol table already assigned
// this type to n.
//
public RType checkTypedVariableBecomesPattern(Pattern pp, Type t, Name n, Pattern p) {
    RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
    if (checkForFail({ rt, p@rtype })) return collapseFailTypes({ rt, p@rtype });
    if (! subtypeOf(p@rtype, rt)) return makeFailType("Type of pattern, <prettyPrintType(p)>, must be a subtype of the type of <n>, <prettyPrintType(rt)>",pp@\loc);
    return rt;
}

//
// Check the guarded pattern type. The result will be of that type, since it must be to match
// (else the match would fail). We return a failure if the pattern can never match the guard. 
//
// TODO: Need to expand type so we know that ADTs, etc are marked.
//
public RType checkGuardedPattern(Pattern pp, Type t, Pattern p) {
    if (isFailType(p@rtype)) return p@rtype;
    RType rt = convertType(t);
    if (! subtypeOf(p@rtype, rt)) return makeFailType("Type of pattern, <prettyPrintType(p)>, must be a subtype of the type of the guard, <prettyPrintType(rt)>",pp@\loc);
    return rt;
}

//
// For the antipattern we will return the type of the pattern, since we still want
// to make sure the pattern can be used to form a valid match. For instance, we
// want to allow !n := 3, where n is an int, but not !n := true, even though, in
// some sense, this is true -- it indicates a (potential) misunderstanding of what
// is being done in the code.
//
public RType checkAntiPattern(Pattern pp, Pattern p) {
    return p@rtype;
}

//
// Type check a map pattern. 
//
public RType checkMapPattern(Pattern pat) {
        list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
    if (size(mapContents) == 0) return makeMapType(makeVoidType(), makeVoidType());

    list[RType] domains; list[RType] ranges;
    for (<md,mr> <- mapContents) { domains += md@rtype; ranges += mr@rtype; }

    if (checkForFail(toSet(domains+ranges))) return collapseFailTypes(toSet(domains+ranges));
    return makeMapType(lubList(domains),lubList(ranges));   
}

//
// Driver code to check patterns. This code, except for literals and names, mainly just 
// dispatches to the various functions defined above.
//
// TODO: This is still insufficient to deal with descendant patterns, since we really
// need to know the type of the subject before we can truly check it. This isn't an
// issue with patterns like / x := B, but it is with patterns like [_*,/x,_*] := B,
// where B is a list with (for instance) ADTs inside. So, think about how we
// want to handle this, we may need another type that is treated specially in patterns,
// like RUnderspecified(t), where t is the type information we have (e.g., list of
// something inferred, etc)
//
public RType checkPattern(Pattern pat) {
    switch(pat) {
        case (Pattern)`<BooleanLiteral bl>` : {
            return makeBoolType();
        }

        case (Pattern)`<DecimalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<OctalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<HexIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<RealLiteral rl>`  : {
            return makeRealType();
        }

        case (Pattern)`<StringLiteral sl>`  : {
            return makeStrType();
        }

        case (Pattern)`<LocationLiteral ll>`  : {
            return makeLocType();
        }

        case (Pattern)`<DateTimeLiteral dtl>`  : {
            return makeDateTimeType();
        }

        // Regular Expression literal
        case (Pattern)`<RegExpLiteral rl>` : {
                // NOTE: The only possible source of errors here is the situation where one of the variables in the
            // regular expression pattern is not a string. We usually can't detect this until the bind, though,
            // so save that check for bindInferredTypesToPattern.
                list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
            list[RType] retTypes = [ getTypeForName(globalSymbolTable, RSimpleName("<n>"), n@\loc) | n <- names ];
            if (checkForFail(toSet(retTypes))) return collapseFailTypes(toSet(retTypes));
            return makeStrType();
        }

        case (Pattern)`_` : {
                RType patType = getTypeForName(globalSymbolTable, RSimpleName("_"), pat@\loc);
            //println("For pattern _ at location <pat@\loc> found type(s) <patType>");
            return patType;
        }
        
        case (Pattern)`<Name n>`: {
            return getTypeForName(globalSymbolTable, convertName(n), n@\loc);
        }
        
        // QualifiedName
        case (Pattern)`<QualifiedName qn>`: {
            return getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
        }

        // ReifiedType
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            return checkReifiedTypePattern(pat,t,pl);
        }

        // CallOrTree
        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            return checkCallOrTreePattern(pat,p1,pl);
        }

        // List
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            return checkListPattern(pat,pl);
        }

        // Set
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            return checkSetPattern(pat,pl);
        }

        // Tuple
        case (Pattern) `<<Pattern pi>>` : {
            return checkTrivialTuplePattern(pat,pi);
        }

        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
            return checkTuplePattern(pat,pi,pl);
        }

        // Typed Variable
        case (Pattern) `<Type t> <Name n>` : {
            return getTypeForName(globalSymbolTable, convertName(n), n@\loc);
        }

        // Multi Variable
        case (Pattern) `_ *` : {
            return getTypeForName(globalSymbolTable, RSimpleName("_"), pat@\loc);
        }
        
        case (Pattern) `<QualifiedName qn> *` : {
            return getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
        }

        // Descendant
        case (Pattern) `/ <Pattern p>` : {
            return p@rtype;
        }

        // Variable Becomes
        case (Pattern) `<Name n> : <Pattern p>` : {
            return checkVariableBecomesPattern(pat,n,p);
        }
        
        // Typed Variable Becomes
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            return checkTypedVariableBecomesPattern(pat,t,n,p);
        }
        
        // Guarded
        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            return checkGuardedPattern(pat,t,p);
        }           
        
        // Anti
        case (Pattern) `! <Pattern p>` : {
            return checkAntiPattern(pat,p);
        }
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
        // pat[0] is the production used, pat[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := pat[0]) {
            RType t = checkMapPattern(pat);
                return t;
    }
    throw "Missing case on checkPattern for pattern <pat> at location <pat@\loc>";
}

//
// Bind any variables used in the map pattern to the types present in type rt.
//
public RType bindInferredTypesToMapPattern(RType rt, Pattern pat) {
        // Get the domain and range types for rt
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

//
// Recursively bind the types from an expression to any inferred types in a pattern. To make subtyping easier,
// we do the binding before we do the subtyping. This allows us to find specific errors in cases where the
// subject and the pattern do not match -- for instance, we can find that a constructor is given two
// arguments, but expects three. If we do subtyping checks first, we get less information -- only that the
// pattern and the subject are not comparable.
//
// TODO: In certain odd cases Lub types could be assigned to names; make sure those are resolved
// correctly here... 
//
public RType bindInferredTypesToPattern(RType rt, Pattern pat) {
    RType pt = pat@rtype; // Just save some typing below...
    
    // If either the type we are binding against (rt) or the current pattern type are fail
    // types, don't try to bind, just fail, we had something wrong in either the pattern
    // or the subject that may yield lots of spurious errors here.
    if (isFailType(rt) || isFailType(pat@rtype)) return collapseFailTypes({ rt, pat@rtype });
    
    // Now, compare the pattern and binding (subject) types, binding actual types to lub and
    // inference types if possible. 
    switch(pat) {
        case (Pattern)`<BooleanLiteral bl>` : {
            if (isBoolType(rt) && isBoolType(pt)) {
                return pt;
            } else {
                return makeFailType("Boolean literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<DecimalIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<OctalIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<HexIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<RealLiteral rl>`  : {
            if (isRealType(rt) && isRealType(pt)) {
                return pt;
            } else {
                return makeFailType("Real literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<StringLiteral sl>`  : {
            if (isStrType(rt) && isStrType(pt)) {
                return pt;
            } else {
                return makeFailType("String literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<LocationLiteral ll>`  : {
            if (isLocType(rt) && isLocType(pt)) {
                return pt;
            } else {
                return makeFailType("Location literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<DateTimeLiteral dtl>`  : {
            if (isDateTimeType(rt) && isDateTimeType(pt)) {
                return pt;
            } else {
                return makeFailType("DateTime literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // Regular Expression literal
        // TODO: We need to check for this pattern in the main visitor, so we can mark the types on the names.
        case (Pattern)`<RegExpLiteral rl>` : {
            if (isStrType(rt) && isStrType(pt)) {
                list[tuple[RType,RName]] resTypes = [ ];
                list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
            for (n <- names) {
                RType pt = getTypeForName(globalSymbolTable, RSimpleName("<n>"), n@\loc);
                RType t = (isInferredType(pt)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
                if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                resTypes += <rt,RSimpleName("<n>")>;
                } else if (! equivalent(t,rt)) {
                resTypes += <makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name <n> in pattern <pat>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc),RSimpleName("<n>")>;
                }           
                        }
            if (checkForFail({t | <t,_> <- resTypes})) return collapseFailTypes({t | <t,_> <- resTypes});
            if (size(resTypes) == 0 || (size(resTypes) > 0 && isStrType(lubList([t | <t,_> <- resTypes])))) return rt;
            return makeFailType("The following names in the pattern are not of type string: <[n | <t,n> <- resTypes, !isStrType(t)]>",pat@\loc);
                   } else {
               return makeFailType("Regular Expression  pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
                   }
        }

        // Anonymous name
        // TODO: Add LubType support, just in case
        case (Pattern)`_` : {
            RType retType;
            RType t = (isInferredType(pt)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name <pat>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", pat@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;
        }
        
        // Name
        // TODO: Add LubType support, just in case
        case (Pattern)`<Name n>`: {
            RType retType;
            RType nType = getTypeForNameLI(globalSymbolTable,convertName(n),n@\loc);
            RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;         
        }

        // QualifiedName
        // TODO: Add LubType support, just in case
        case (Pattern)`<QualifiedName qn>`: {
            RType retType;
            RType nType = getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc);
            RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <qn>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;         
        }

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

        // List
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            if (isListType(rt) && isListType(pt)) {
                RType plt = getListElementType(pt);
                RType rlt = getListElementType(rt);
                
                list[RType] elementTypes = [ ];
                for (p <- pl) {
                    if (isListContainerVar(p))          
                        elementTypes += bindInferredTypesToPattern(rt,p);
                    else
                        elementTypes += bindInferredTypesToPattern(rlt,p);
                }
                
                if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                
                // Get the types in the list, we need to watch for inferrence types since we need
                // to handle those separately. We also need to watch for lub types, since we could
                // propagate these up, although we should be able to resolve them at some point (maybe
                // just not yet). For instance, if we have C([ [x,_*], _* ]), when we type [x,_*] this
                // will generate a lub type, then [ [x,_*], _* ] will also generate a lub type, and it
                // will not be resolved until we reach C([ [x,_*], _*]), where we should be able to
                // determine the actual type.
                list[RType] patTypesI = [ t | t <- elementTypes, isInferredType(t) || isLubType(t) ];
                
                if (size(patTypesI) > 0) {
                    return makeListType(makeLubType(elementTypes));
                } else {
                    RType lubType = lubList(elementTypes);
                    if (subtypeOf(rlt,lubType)) {
                        return makeListType(lubType);
                    } else {
                        return makeFailType("The list element type of the subject, <prettyPrintType(rlt)>, must be a subtype of the list element type in the pattern, <prettyPrintType(lubType)>", pat@\loc);
                    }
                }
            } else {
                return makeFailType("List pattern has pattern type of <prettyPrintType(pt)> but subject type of <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // Set
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            if (isSetType(rt) && isSetType(pt)) {
                RType pst = getSetElementType(pt);
                RType rst = getSetElementType(rt);
                
                list[RType] elementTypes = [ ];
                for (p <- pl) {
                    if (isSetContainerVar(p))           
                        elementTypes += bindInferredTypesToPattern(rt,p);
                    else
                        elementTypes += bindInferredTypesToPattern(rst,p);
                }
                
                if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                
                // Get the types in the set, we need to watch for inferrence types since we need
                // to handle those separately. We also need to watch for lub types, since we could
                // propagate these up, although we should be able to resolve them at some point (maybe
                // just not yet). For instance, if we have C({ {x,_*}, _* }), when we type {x,_*} this
                // will generate a lub type, then { {x,_*}, _* } will also generate a lub type, and it
                // will not be resolved until we reach C({ {x,_*}, _*}), where we should be able to
                // determine the actual type.
                list[RType] patTypesI = [ t | t <- elementTypes, hasDeferredTypes(t) ];
                
                if (size(patTypesI) > 0) {
                    return makeListType(makeLubType(elementTypes));
                } else {
                    RType lubType = lubList(elementTypes);
                    if (subtypeOf(rst,lubType)) {
                        return makeSetType(lubType);
                    } else {
                        return makeFailType("The set element type of the subject, <prettyPrintType(rst)>, must be a subtype of the set element type in the pattern, <prettyPrintType(lubType)>", pat@\loc);
                    }
                }
            } else {
                return makeFailType("Set pattern has pattern type of <prettyPrintType(pt)> but subject type of <prettyPrintType(rt)>",pat@\loc);
            }
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
