@bootstrapParser
module rascal::scoping::ScopedTypes

import List;
import IO;
import Set;
import Relation;
import Map;
import ParseTree;
import Node;

import rascal::types::Types;
import rascal::types::TypeSignatures;
import rascal::types::SubTypes;
import rascal::types::TypeEquivalence;
import rascal::scoping::SymbolTable;
import rascal::syntax::RascalRascal;

///////////////////////////////////////////////////////////////////////////////////////////
//
// Aliases and ADTs are given just as names, but should represent other types; this code
// expands these named types into their actual types.
//
///////////////////////////////////////////////////////////////////////////////////////////

public RType expandValueType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandLocType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandNodeType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandNumType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandReifiedType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeReifiedType(expandUserTypes(getReifiedType(rt),symbolTable,currentScope));
}

public RType expandBagType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeBagType(expandUserTypes(getBagElementType(rt),symbolTable,currentScope));
}

public RType expandIntType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandRelType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeRelTypeFromTuple(expandUserTypes(getRelElementType(rt),symbolTable,currentScope));
}

public RType expandTypeVarType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeTypeVarWithBound(getTypeVarName(rt),expandUserTypes(getTypeVarBound(rt),symbolTable,currentScope));
}

public RType expandRealType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandFunctionType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeFunctionTypeFromTuple(expandUserTypes(getFunctionReturnType(rt),symbolTable,currentScope), 
        expandUserTypes(getFunctionArgumentTypesAsTuple(rt),symbolTable,currentScope),
        isVarArgsFun(rt));
}

public RType expandTupleType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeTupleTypeWithNames( [ expandUserTypesForNamedType(p,symbolTable,currentScope) | p <- getTupleFieldsWithNames(rt) ]);
}

public RType expandStrType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandBoolType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandReifiedReifiedType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnimplementedRType(t1);
}

public RType expandVoidType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandNonTerminalType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    // TODO: Add implementation, we may need to change the representation of this type
    return rt;
}

public RType expandDateTimeType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return rt;
}

public RType expandSetType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeSetType(expandUserTypes(getSetElementType(rt),symbolTable,currentScope));
}

public RType expandMapType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeMapTypeFromTuple(expandUserTypes(getMapFieldsAsTuple(rt),symbolTable,currentScope));
}

public RType expandConstructorType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeConstructorTypeFromTuple(getConstructorName(rt),
        expandUserTypes(getConstructorResultType(rt),symbolTable,currentScope),
        expandUserTypes(getConstructorArgumentTypesAsTuple(rt),symbolTable,currentScope));
}

public RType expandListType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeListType(expandUserTypes(getListElementType(rt),symbolTable,currentScope));
}

public RType expandADTType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeParameterizedADTType(getADTName(rt), [ expandUserType(p,symbolTable,currentScope) | p <- getADTTypeParameters(rt)]);
}

public RType expandLexType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandUserType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    RName tn = getUserTypeName(rt);
    list[RType] params = [ expandUserTypes(tp,symbolTable,currentScope) | tp <- getUserTypeParameters(rt)];
    
    set[STItemId] userTypes = getItems(symbolTable,currentScope,tn,Types());
    set[STItemId] aliasItems = { pi | pi <- userTypes, AliasItem(_,_,_) := symbolTable.scopeItemMap[pi] };
    set[STItemId] adtItems = { pi | pi <- userTypes, ADTItem(_,_,_) := symbolTable.scopeItemMap[pi] };
    
    if (size(userTypes - aliasItems - adtItems) > 0) 
        throw "Unexpected case, got a user type that is not an alias or an adt, example: <symbolTable.scopeItemMap[getOneFrom(userTypes-aliasItems-adtItems)]>";

    if (size(aliasItems) > 0 && size(adtItems) > 0)
        throw "Unexpected case, got a user type that is both an alias and an adt, examples: <symbolTable.scopeItemMap[getOneFrom(aliasItems)]> and <symbolTable.scopeItemMap[getOneFrom(adtItems)]>";
    
    // TODO: Maybe throw an exception here, and catch it in the RUnknownType case?
    if (size(aliasItems) == 0 && size(adtItems) == 0)
        return rt; // Could not expand, may be an unknown type...
        
    // TODO: It should be fine to have more than 1 here, but verify (they should all be the same).        
    if (size(adtItems) > 0) {
        RType resultType = getTypeForItem(symbolTable, getOneFrom(adtItems));
        list[RType] adtParameters = getADTTypeParameters(resultType);
        if (size(params) > 0) {
            if (size(params) != size(adtParameters)) {
                throw "Unexpected case, we have a different number of type parameters in the adt use and adt definition: <prettyPrintType(rt)> versus <prettyPrintType(resultType)>";
            } else {
                resultType = makeParameterizedADTType(getADTName(resultType),params);            
            }
        }
                
        return resultType;
    }
    
    // TODO: Can we ever have more than 1 here? That would be a name clash.
    if (size(aliasItems) > 0) {
        RType resultType = getTypeForItem(symbolTable, getOneFrom(aliasItems));
        list[RType] aliasParameters = getAliasTypeParameters(resultType);
        if (size(params) > 0) {
            if (size(params) != size(aliasParameters)) {
                throw "Unexpected case, we have a different number of type parameters in the alias use and aliased type definition: <prettyPrintType(rt)> versus <prettyPrintType(resultType)>";            
            } else {
                map[RName varName, RType varType] bindings = ( );
                for (n <- [0..size(params)-1]) bindings = bindings + ( aliasParameters[n] : params[n] );
                resultType = makeParameterizedAliasType(getAliasName(resultType), instantiateVars(getAliasedType(resultType),bindings), params);
            }
        }
        
        return resultType;
    }
}

public RType expandAliasType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    return makeParameterizedAliasType(getAliasName(rt), expandUserTypes(getAliasedType(rt), symbolTable, currentScope), 
        [expandUserTypes(rti, symbolTable, currentScope) | rti <- getAliasTypeParameters(rt)]);
}

public RType expandDataTypeSelectorType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandFailType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandInferredType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandOverloadedType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandStatementType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    throw UnexpectedRType(t1);
}

public RType expandUnknownType(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    RType utRes = expandUserTypes(getUnknownType(rt), symbolTable, currentScope);
    return (utRes != getUnknownType(rt)) ? utRes : rt; // If the type changed, we at least partially resolved it, return that
}

//
// Main lub routine
//
private map[str,RType(RType,SymbolTable,STItemId)] expandHandlers = (
    "RValueType" : expandValueType,
    "RLocType" : expandLocType,
    "RNodeType" : expandNodeType,
    "RNumType" : expandNumType,
    "RReifiedType" : expandReifiedType,
    "RBagType" : expandBagType,
    "RIntType" : expandIntType,
    "RRelType" : expandRelType,
    "RTypeVar" : expandTypeVarType,
    "RRealType" : expandRealType,
    "RFunctionType" : expandFunctionType,
    "RTupleType" : expandTupleType,
    "RStrType" : expandStrType,
    "RBoolType" : expandBoolType,
    "RReifiedReifiedType" : expandReifiedReifiedType,
    "RVoidType" : expandVoidType,
    "RNonTerminalType" : expandNonTerminalType,
    "RDateTimeType" : expandDateTimeType,
    "RSetType" : expandSetType,
    "RMapType" : expandMapType,
    "RConstructorType" : expandConstructorType,
    "RListType" : expandListType,
    "RADTType" : expandADTType,
    "RLexType" : expandLexType,
    "RUserType" : expandUserType,
    "RAliasType" : expandAliasType,
    "RDataTypeSelector" : expandDataTypeSelectorType,
    "RFailType" : expandFailType,
    "RInferredType" : expandInferredType,
    "ROverloadedType" : expandOverloadedType,
    "RStatementType" : expandStatementType,
    "RUnknownType" : expandUnknownType
);

public RNamedType expandUserTypesForNamedType(RNamedType nt, SymbolTable symbolTable, STItemId currentScope) {
    if (RUnnamedType(rt) := nt) {
        return RUnnamedType(expandUserTypes(rt,symbolTable,currentScope));
    } else if (RNamedType(rt,tn) := nt) {
        return RNamedType(expandUserTypes(rt, symbolTable, currentScope), tn);
    } else {
        throw "expandUserTypesForNamedType given unexpected type <nt>";
    }
}

public RType expandUserTypes(RType rt, SymbolTable symbolTable, STItemId currentScope) {
    if (getName(rt) in expandHandlers) { 
        RType expandedType = (expandHandlers[getName(rt)])(rt, symbolTable, currentScope);
        if ( (rt@at)? ) expandedType = expandedType[@at=rt@at];
        return expandedType;        
    } else {
        throw UnexpectedRType(rt);
    }
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Get types back for individual scope items / source locations
//
///////////////////////////////////////////////////////////////////////////////////////////

public RType getTypeForItem(SymbolTable symbolTable, STItemId itemId) {
    if (itemId notin symbolTable.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";
    
    STItem si = symbolTable.scopeItemMap[itemId];
    RType rt = makeVoidType();
    
    // NOTE: For those items that introduce a new type, we bring over the location information
    switch(si) {
        case ADTItem(ut,_,_) : { 
            rt = expandUserTypes(ut,symbolTable,si.parentId);
            if ( (si@at)? ) rt = rt[@at = si@at];
        }
        
        case AliasItem(ut,_,_) : { 
            rt = expandUserTypes(ut,symbolTable,si.parentId); 
            if ( (si@at)? ) rt = rt[@at = si@at];
        }
        
        case ConstructorItem(n,tas,adtParentId,_) : {
            rt = expandUserTypes(makeConstructorType(n,getTypeForItem(symbolTable,adtParentId),tas),symbolTable,si.parentId);
            if ( (si@at)? ) rt = rt[@at = si@at];
        }
        
        case FunctionItem(_,t,params,_,_,isVarArgs,_) : {
            // TODO: Make this really work! (Pattern Dispatch)
            // NOTE: This should only be called during name resolution by the functionality that
            // tags function return statements with their return types. It would be nicer if we could
            // just tag the return with the function scope we are inside.
            //rt = expandUserTypes(makeFunctionType(t,[getTypeForItem(symbolTable, paramId) | paramId <- paramIds],isVarArgs),symbolTable,si.parentId);
            rt = expandUserTypes(makeFunctionType(t,[makeValueType()],isVarArgs),symbolTable,si.parentId);
            if ( (si@at)? ) rt = rt[@at = si@at];
        }
        
        case FormalParameterItem(_,t,_) : 
            rt = expandUserTypes(t,symbolTable,si.parentId);
        
        case TypeVariableItem(t,_) : 
            rt = expandUserTypes(t,symbolTable,si.parentId);
        
        case VariableItem(_,t,_) : 
            rt = expandUserTypes(t,symbolTable,si.parentId);
        
        default : 
            throw "No Match!!! <si>"; 
    }
            
    return rt;
}

public bool hasRType(SymbolTable symbolTable, loc l) {
    if (l in symbolTable.itemUses || l in symbolTable.scopeErrorMap) return true;
    return false;
}

public RType getRType(SymbolTable symbolTable, loc l) {
    set[STItemId] itemIds = (l in symbolTable.itemUses) ? symbolTable.itemUses[l] : { };
    set[str] scopeErrors = (l in symbolTable.scopeErrorMap) ? symbolTable.scopeErrorMap[l] : { };
    
    if (size(scopeErrors) == 0) {
        if (size(itemIds) == 0) {
            return makeFailType("Error, attemting to find type of item at location <l>, but not item found",l);
        } else {
            if (size({ getTypeForItem(symbolTable, itemId) | itemId <- itemIds }) > 1) {
                // TODO: Should we verify this is a function or constructor type?
                return ROverloadedType({ getTypeForItem(symbolTable,itemId) | itemId <- itemIds });
            } else {
                return getTypeForItem(symbolTable, getOneFrom(itemIds));
            }
        }
    } else {
        return collapseFailTypes({ makeFailType(s,l) | s <- scopeErrors });
    }
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Scope-aware type conversion; this makes sure that any errors found during conversion
// are noted as scope errors.
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// Provide consistency checking and expansion on type conversion.
//
// TODO: Add checks to ensure that the correct number of type parameters are
// given. For example, T[&U] should not be used as T[int,bool].
//
// TODO: Ensure that reified types are given correctly. We need to do this
// here, since this can depend on scope information, such as which ADTs
// are in scope at the time (unless we just force use of the pre-existing
// constructors, which could work, but in that case we need to enforce that
// in Types).
//

alias ConvertTuple = tuple[SymbolTable symbolTable, RType rtype];
alias ConvertTupleN = tuple[SymbolTable symbolTable, RNamedType rtype];

public ConvertTuple convertRascalType(SymbolTable symbolTable, Type t) {
    // Step 1: convert the type
    RType rt = convertType(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    list[tuple[str msg, loc at]] conversionWarnings = [ ];
    
    visit(rt) { 
        case RType ct : {
            if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo;
            if ( (ct@warninfo)? ) conversionWarnings = conversionWarnings + ct@warninfo;
        }
    }

    // Step 3: if we found errors, add them as scope errors
    if (size(conversionErrors) > 0)
        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);
    if (size(conversionWarnings) > 0)
        for (<cmsg,cloc> <- conversionWarnings) symbolTable = addScopeWarning(symbolTable, cloc, cmsg);

    // Step 4: finally return the type
    return <symbolTable, rt>;   
}

public ConvertTuple convertRascalUserType(SymbolTable symbolTable, UserType t) {
    // Step 1: convert the type
    RType rt = convertUserType(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    visit(rt) { case RType ct : if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo; }

    // Step 3: if we found errors, add them as scope errors
    if (size(conversionErrors) > 0)
        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);

    // Step 4: finally return the type
    return <symbolTable, rt>;   
}

public ConvertTupleN convertRascalTypeArg(SymbolTable symbolTable, TypeArg t) {
    // Step 1: convert the type
    RNamedType rt = convertTypeArg(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    visit(rt) { case RType ct : if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo; }

    // Step 3: if we found errors, add them as scope errors
    if (size(conversionErrors) > 0)
        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);

    // Step 4: finally return the type
    return <symbolTable, rt>;   
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Consolidate definitions of ADTs, since ADTs are defined as all the constructors
// defined across multiple ADT definitions of the same name
//
///////////////////////////////////////////////////////////////////////////////////////////

public SymbolTable consolidateADTDefinitions(SymbolTable symbolTable, RName moduleName) {
    // Get back the ID for the name of the module being checked -- there should be only one matching
    // item. TODO: We may want to verify that here.
    STItemId moduleItemId = getOneFrom(getModuleItemsForName(symbolTable, moduleName));
    STItemId moduleLayerId = moduleItemId.parentId;
    return consolidateADTDefinitionsForLayer(symbolTable, moduleLayerId, true);
}

public SymbolTable consolidateADTDefinitionsForLayer(SymbolTable symbolTable, STItemId layerId, bool includeTopLayer) {
    // Step 1: Pick out all ADT definitions in the loaded scope information (i.e., all ADTs defined
    // in either the loaded module or its direct imports)
    set[STItemId] adtIDs = { sid | sid <- symbolTable.scopeRel[layerId], ADTItem(_,_,_) := symbolTable.scopeItemMap[sid] };
    if (includeTopLayer) {
        adtIDs = adtIDs + { sid | sid <- symbolTable.scopeRel[symbolTable.topSTItemId], ADTItem(_,_,_) := symbolTable.scopeItemMap[sid] };
    }
                              
    // Step 2: Group these based on the name of the ADT
    rel[RName adtName, STItemId adtItemId] nameXADTItem = { < getADTName(n), sid > | sid <- adtIDs, ADTItem(n,_,_) := symbolTable.scopeItemMap[sid] };
    
    // Step 3: Gather together all the constructors for the ADTs
    rel[STItemId adtItemId, STItemId consItemId] adtItemXConsItem = { < sid, cid > | sid <- range(nameXADTItem), cid <- domain(symbolTable.scopeItemMap), ConstructorItem(_,_,sid,_) := symbolTable.scopeItemMap[cid] };
     
    // Step 4: Now, directly relate the ADT names to the available constructors
    rel[RName adtName, STItemId consItemId] nameXConsItem = nameXADTItem o adtItemXConsItem;
    
    // Step 5: Put these into the needed form for the internal ADT map
    for (n <- domain(nameXADTItem))
        symbolTable.adtMap[n] = < { sid | sid <- nameXADTItem[n] }, { cid | cid <- nameXConsItem[n] } >;
        
    // Finally, return the scopeinfo with the consolidated ADT information
    return symbolTable;
}

public SymbolTable checkADTDefinitionsForConsistency(SymbolTable symbolTable) {
    <inModule, moduleLayerId> = getSurroundingModule(symbolTable, symbolTable.currentScope); 
    loc moduleLoc = symbolTable.scopeItemMap[moduleLayerId]@at;
    
    // Check each ADT individually for field type consistency
    for (n <- domain(symbolTable.adtMap)) {
        map[RName fieldName, RType fieldType] fieldMap = ( );

        // First check imported constructors. If we get errors, we would rather have them on the constructors
        // defined in the current module, since they are easier to fix -- checking them later preferences the
        // types assigned to field in imported types.
        for (ci <- symbolTable.adtMap[n].consItems, ci in symbolTable.scopeRel[symbolTable.topSTItemId]) {
            RType consType = getTypeForItem(symbolTable, ci);
            if (isConstructorType(consType)) {
                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
                for (RNamedType(nt,nn) <- argTypes) {
                    if (nn notin fieldMap) {
                        fieldMap[nn] = nt;
                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
                        symbolTable = addScopeError(symbolTable, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
                    }
                }
            } else {
                throw "checkADTDefinitionsForConsistency, unexpected constructor item <symbolTable.scopeItemMap[ci]>";
            }
        }
        
        // Now check the fields on the ADTs defined in the current module.
        // TODO: May be good to refactor out identical checking code
        for (ci <- symbolTable.adtMap[n].consItems, ci in symbolTable.scopeRel[symbolTable.topSTItemId]) {
            RType consType = getTypeForItem(symbolTable, ci);
            if (isConstructorType(consType)) {
                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
                for (RNamedType(nt,nn) <- argTypes) {
                    if (nn notin fieldMap) {
                        fieldMap[nn] = nt;
                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
                        symbolTable = addScopeError(symbolTable, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
                    }
                }               
            } else {
                throw "checkADTDefinitionsForConsistency, unexpected constructor item <symbolTable.scopeItemMap[ci]>";
            }
        }
    }
    
    return symbolTable; 
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Scoping convenience functions that deal with types
//
// TODO: See which of these should be moved into SymbolTable directly.
//
// TODO: Field update syntax is causing problems with type derivation in the interpreter.
//
///////////////////////////////////////////////////////////////////////////////////////////
