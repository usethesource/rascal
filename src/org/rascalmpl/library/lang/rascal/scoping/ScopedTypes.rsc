@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::scoping::ScopedTypes

import List;
import IO;
import Set;
import Relation;
import Map;
import ParseTree;
import Node;
import Message;

import lang::rascal::types::Types;
import lang::rascal::types::TypeSignatures;
import lang::rascal::types::SubTypes;
import lang::rascal::types::TypeEquivalence;
import lang::rascal::types::TypeExceptions;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::syntax::RascalRascal;

///////////////////////////////////////////////////////////////////////////////////////////
//
// Aliases and ADTs are given just as names, but should represent other types; this code
// expands these named types into their actual types.
//
///////////////////////////////////////////////////////////////////////////////////////////

public RType expandValueType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandLocType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandNodeType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandNumType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandReifiedType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeReifiedType(expandUserTypes(getReifiedType(rt),stBuilder,currentScope));
}

public RType expandBagType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeBagType(expandUserTypes(getBagElementType(rt),stBuilder,currentScope));
}

public RType expandIntType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandRelType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeRelTypeFromTuple(expandUserTypes(getRelElementType(rt),stBuilder,currentScope));
}

public RType expandTypeVarType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeTypeVarWithBound(getTypeVarName(rt),expandUserTypes(getTypeVarBound(rt),stBuilder,currentScope));
}

public RType expandRealType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandFunctionType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeFunctionTypeFromTuple(expandUserTypes(getFunctionReturnType(rt),stBuilder,currentScope), 
        expandUserTypes(getFunctionArgumentTypesAsTuple(rt),stBuilder,currentScope),
        isVarArgsFun(rt));
}

public RType expandTupleType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeTupleTypeWithNames( [ expandUserTypesForNamedType(p,stBuilder,currentScope) | p <- getTupleFieldsWithNames(rt) ]);
}

public RType expandStrType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandBoolType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandReifiedReifiedType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    throw UnimplementedRType(t1);
}

public RType expandVoidType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandNonTerminalType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    // TODO: Add implementation, we may need to change the representation of this type
    return rt;
}

public RType expandDateTimeType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandSetType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeSetType(expandUserTypes(getSetElementType(rt),stBuilder,currentScope));
}

public RType expandMapType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeMapTypeFromTuple(expandUserTypes(getMapFieldsAsTuple(rt),stBuilder,currentScope));
}

public RType expandConstructorType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeConstructorTypeFromTuple(getConstructorName(rt),
        expandUserTypes(getConstructorResultType(rt),stBuilder,currentScope),
        expandUserTypes(getConstructorArgumentTypesAsTuple(rt),stBuilder,currentScope));
}

public RType expandListType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeListType(expandUserTypes(getListElementType(rt),stBuilder,currentScope));
}

public RType expandADTType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeParameterizedADTType(getADTName(rt), [ expandUserTypes(p,stBuilder,currentScope) | p <- getADTTypeParameters(rt)]);
}

public RType expandLexType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    throw UnexpectedRType(rt);
}

public RType expandUserType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    RName tn = getUserTypeName(rt);

    list[RType] params = [ expandUserTypes(tp,stBuilder,currentScope) | tp <- getUserTypeParameters(rt)];
    
    set[ItemId] userTypes = getItems(stBuilder,currentScope,tn,Types());
    set[ItemId] aliasItems = { pi | pi <- userTypes, Alias(_,_,_,_) := stBuilder.scopeItemMap[pi] };
    set[ItemId] adtItems = { pi | pi <- userTypes, ADT(_,_,_,_) := stBuilder.scopeItemMap[pi] };
    
    if (size(userTypes - aliasItems - adtItems) > 0) 
        throw "Unexpected case, got a user type that is not an alias or an adt, example: <stBuilder.scopeItemMap[getOneFrom(userTypes-aliasItems-adtItems)]>";

    if (size(aliasItems) > 0 && size(adtItems) > 0)
        throw "Unexpected case, got a user type that is both an alias and an adt, examples: <stBuilder.scopeItemMap[getOneFrom(aliasItems)]> and <stBuilder.scopeItemMap[getOneFrom(adtItems)]>";
    
    // TODO: Maybe throw an exception here, and catch it in the RUnknownType case?
    if (size(aliasItems) == 0 && size(adtItems) == 0)
        return rt; // Could not expand, may be an unknown type...
        
    // TODO: It should be fine to have more than 1 here, but verify (they should all be the same).
    // TODO: Instantiate here? Any use of a user type with params that is not the declaration
    // site should probably be instantiated to get back the proper type.        
    if (size(adtItems) > 0) {
        RType resultType = getTypeForItem(stBuilder, getOneFrom(adtItems));
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
        RType resultType = getTypeForItem(stBuilder, getOneFrom(aliasItems));
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

public RType expandAliasType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return makeParameterizedAliasType(getAliasName(rt), expandUserTypes(getAliasedType(rt), stBuilder, currentScope), 
        [expandUserTypes(rti, stBuilder, currentScope) | rti <- getAliasTypeParameters(rt)]);
}

public RType expandDataTypeSelectorType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    throw UnexpectedRType(rt);
}

public RType expandFailType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    throw UnexpectedRType(rt);
}

public RType expandInferredType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandOverloadedType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    return rt;
}

public RType expandStatementType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    throw UnexpectedRType(rt);
}

public RType expandUnknownType(RType rt, STBuilder stBuilder, ItemId currentScope) {
    RType utRes = expandUserTypes(getUnknownType(rt), stBuilder, currentScope);
    return (utRes != getUnknownType(rt)) ? utRes : rt; // If the type changed, we at least partially resolved it, return that
}

public map[str,RType(RType,STBuilder,ItemId)] expandHandlers = (
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

public RNamedType expandUserTypesForNamedType(RNamedType nt, STBuilder stBuilder, ItemId currentScope) {
    if (RUnnamedType(rt) := nt) {
        return RUnnamedType(expandUserTypes(rt,stBuilder,currentScope));
    } else if (RNamedType(rt,tn) := nt) {
        return RNamedType(expandUserTypes(rt, stBuilder, currentScope), tn);
    } else {
        throw "expandUserTypesForNamedType given unexpected type <nt>";
    }
}

public RType expandUserTypes(RType rt, STBuilder stBuilder, ItemId currentScope) {
    if (getName(rt) in expandHandlers) {
    	//h = expandHandlers[getName(rt)];
    	//println(rt); println(h);
    	//RType expandedType = h(rt, stBuilder, currentScope); 
        RType expandedType = (expandHandlers[getName(rt)])(rt, stBuilder, currentScope);
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

public RType getTypeForItem(STBuilder stBuilder, ItemId itemId) {
    if (itemId notin stBuilder.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";
    
    Item si = stBuilder.scopeItemMap[itemId];
    RType rt = makeVoidType();
    
    // NOTE: For those items that introduce a new type, we bring over the location information
    switch(si) {
        case ADT(ut,_,_,_) : { 
            rt = expandUserTypes(ut,stBuilder,si.parentId)[@at = si.definedAt];
        }
        
        case Alias(ut,_,_,_) : { 
            rt = expandUserTypes(ut,stBuilder,si.parentId)[@at = si.definedAt]; 
        }
        
        case Constructor(n,tas,adtParentId,_,_) : {
            rt = expandUserTypes(makeConstructorType(n,getTypeForItem(stBuilder,adtParentId),tas),stBuilder,si.parentId)[@at = si.definedAt];
        }
        
        case Function(_,t,params,_,_,isVarArgs,_,_) : {
            // TODO: Make this really work! (Pattern Dispatch)
            // NOTE: This should only be called during name resolution by the functionality that
            // tags function return statements with their return types. It would be nicer if we could
            // just tag the return with the function scope we are inside.
            //rt = expandUserTypes(makeFunctionType(t,[getTypeForItem(stBuilder, paramId) | paramId <- paramIds],isVarArgs),stBuilder,si.parentId);
            rt = expandUserTypes(makeFunctionType(t,[ ptype | < ptype, _ > <- params ],isVarArgs),stBuilder,si.parentId)[@at = si.definedAt];
        }
        
        case FormalParameter(_,t,_,_) : 
            rt = expandUserTypes(t,stBuilder,si.parentId);
        
        case TypeVariable(t,_,_) : 
            rt = expandUserTypes(t,stBuilder,si.parentId);
        
        case Variable(_,t,_,_) : 
            rt = expandUserTypes(t,stBuilder,si.parentId);
        
        default : 
            throw "No Match!!! <si>"; 
    }
            
    return rt;
}

public bool hasRType(STBuilder stBuilder, loc l) {
	if (l in stBuilder.itemUses<0>) return true;
	if (size({ m | m:error(_,_) <- stBuilder.messages[l] }) > 0) return true;
    return false;
}

public RType getRType(STBuilder stBuilder, loc l) {
    set[ItemId] itemIds = stBuilder.itemUses[l];
    set[str] scopeErrors = { s | error(s,_) <- stBuilder.messages[l] };
    
    if (size(scopeErrors) == 0) {
        if (size(itemIds) == 0) {
            return makeFailType("Error, attemting to find type of item at location <l>, but not item found",l);
        } else {
            if (size({ getTypeForItem(stBuilder, itemId) | itemId <- itemIds }) > 1) {
                // TODO: Should we verify this is a function or constructor type?
                return ROverloadedType({ getTypeForItem(stBuilder,itemId) | itemId <- itemIds });
            } else {
                return getTypeForItem(stBuilder, getOneFrom(itemIds));
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

alias ConvertTuple = tuple[STBuilder stBuilder, RType rtype];
alias ConvertTupleN = tuple[STBuilder stBuilder, RNamedType rtype];

public ConvertTuple convertRascalType(STBuilder stBuilder, Type t) {
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
        for (<cmsg,cloc> <- conversionErrors) stBuilder = addScopeError(stBuilder, cloc, cmsg);
    if (size(conversionWarnings) > 0)
        for (<cmsg,cloc> <- conversionWarnings) stBuilder = addScopeWarning(stBuilder, cloc, cmsg);

    // Step 4: finally return the type
    return <stBuilder, rt>;   
}

public ConvertTuple convertRascalUserType(STBuilder stBuilder, UserType t) {
    // Step 1: convert the type
    RType rt = convertUserType(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    visit(rt) { case RType ct : if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo; }

    // Step 3: if we found errors, add them as scope errors
    if (size(conversionErrors) > 0)
        for (<cmsg,cloc> <- conversionErrors) stBuilder = addScopeError(stBuilder, cloc, cmsg);

    // Step 4: finally return the type
    return <stBuilder, rt>;   
}

public ConvertTupleN convertRascalTypeArg(STBuilder stBuilder, TypeArg t) {
    // Step 1: convert the type
    RNamedType rt = convertTypeArg(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    visit(rt) { case RType ct : if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo; }

    // Step 3: if we found errors, add them as scope errors
    if (size(conversionErrors) > 0)
        for (<cmsg,cloc> <- conversionErrors) stBuilder = addScopeError(stBuilder, cloc, cmsg);

    // Step 4: finally return the type
    return <stBuilder, rt>;   
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Consolidate definitions of ADTs, since ADTs are defined as all the constructors
// defined across multiple ADT definitions of the same name
//
///////////////////////////////////////////////////////////////////////////////////////////

public STBuilder consolidateADTDefinitions(STBuilder stBuilder, RName moduleName) {
    // Get back the ID for the name of the module being checked -- there should be only one matching
    // item. TODO: We may want to verify that here.
    ItemId moduleItemId = getOneFrom(getModuleItemsForName(stBuilder, moduleName));
    ItemId moduleLayerId = moduleItemId.parentId;
    return consolidateADTDefinitionsForLayer(stBuilder, moduleLayerId, true);
}

public STBuilder consolidateADTDefinitionsForLayer(STBuilder stBuilder, ItemId layerId, bool includeTopScope) {
    // Step 1: Pick out all ADT definitions in the loaded scope information (i.e., all ADTs defined
    // in either the loaded module or its direct imports)
    set[ItemId] adtIDs = { sid | sid <- stBuilder.scopeRel[layerId], ADT(_,_,_,_) := stBuilder.scopeItemMap[sid] };
    if (includeTopScope) {
        adtIDs = adtIDs + { sid | sid <- stBuilder.scopeRel[last(stBuilder.scopeStack)], ADT(_,_,_,_) := stBuilder.scopeItemMap[sid] };
    }
                              
    // Step 2: Group these based on the name of the ADT
    rel[RName adtName, ItemId adtItemId] nameXADTItem = { < getADTName(n), sid > | sid <- adtIDs, ADT(n,_,_,_) := stBuilder.scopeItemMap[sid] };
    
    // Step 3: Gather together all the constructors for the ADTs
    rel[ItemId adtItemId, ItemId consItemId] adtItemXConsItem = { < sid, cid > | sid <- range(nameXADTItem), cid <- domain(stBuilder.scopeItemMap), Constructor(_,_,sid,_,_) := stBuilder.scopeItemMap[cid] };
     
    // Step 4: Now, directly relate the ADT names to the available constructors
    rel[RName adtName, ItemId consItemId] nameXConsItem = nameXADTItem o adtItemXConsItem;
    
    // Step 5: Put these into the needed form for the internal ADT map
    for (n <- domain(nameXADTItem))
        stBuilder.adtMap[n] = < { sid | sid <- nameXADTItem[n] }, { cid | cid <- nameXConsItem[n] } >;
        
    // Finally, return the scopeinfo with the consolidated ADT information
    return stBuilder;
}

public STBuilder checkADTDefinitionsForConsistency(STBuilder stBuilder) {
    <inModule, moduleLayerId> = getSurroundingModule(stBuilder, head(stBuilder.scopeStack)); 
    loc moduleLoc = stBuilder.scopeItemMap[moduleLayerId].definedAt;
    
    // Check each ADT individually for field type consistency
    for (n <- domain(stBuilder.adtMap)) {
        map[RName fieldName, RType fieldType] fieldMap = ( );

        // First check imported constructors. If we get errors, we would rather have them on the constructors
        // defined in the current module, since they are easier to fix -- checking them later preferences the
        // types assigned to field in imported types.
        for (ci <- stBuilder.adtMap[n].consItems, ci in stBuilder.scopeRel[last(stBuilder.scopeStack)]) {
            RType consType = getTypeForItem(stBuilder, ci);
            if (isConstructorType(consType)) {
                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
                for (RNamedType(nt,nn) <- argTypes) {
                    if (nn notin fieldMap) {
                        fieldMap[nn] = nt;
                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
                        stBuilder = addScopeError(stBuilder, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
                    }
                }
            } else {
                throw "checkADTDefinitionsForConsistency, unexpected constructor item <stBuilder.scopeItemMap[ci]>";
            }
        }
        
        // Now check the fields on the ADTs defined in the current module.
        // TODO: May be good to refactor out identical checking code
        for (ci <- stBuilder.adtMap[n].consItems, ci in stBuilder.scopeRel[last(stBuilder.scopeStack)]) {
            RType consType = getTypeForItem(stBuilder, ci);
            if (isConstructorType(consType)) {
                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
                for (RNamedType(nt,nn) <- argTypes) {
                    if (nn notin fieldMap) {
                        fieldMap[nn] = nt;
                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
                        stBuilder = addScopeError(stBuilder, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
                    }
                }               
            } else {
                throw "checkADTDefinitionsForConsistency, unexpected constructor item <stBuilder.scopeItemMap[ci]>";
            }
        }
    }
    
    return stBuilder; 
}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Scoping convenience functions that deal with types
//
// TODO: See which of these should be moved into STBuilder directly.
//
// TODO: Field update syntax is causing problems with type derivation in the interpreter.
//
///////////////////////////////////////////////////////////////////////////////////////////
