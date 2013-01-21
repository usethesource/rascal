@license{
  Copyright (c) 2009-2013 CWI
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
import Type;

import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::ConvertType;
import lang::rascal::types::TypeSignature;
import lang::rascal::types::TypeExceptions;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::syntax::RascalRascal;

@doc{Replace any user types with the type the name points to (an ADT or alias).}
private default Symbol et(Symbol t, STBuilder stb, ItemId cs) = t;

private Symbol et(\label(str s, Symbol lt), STBuilder stb, ItemId cs) = \label(s,expandUserTypes(lt,stb,cs));
private Symbol et(\parameter(str s, Symbol pt), STBuilder stb, ItemId cs) = \parameter(s,expandUserTypes(pt,stb,cs));

private Symbol et(\set(Symbol et), STBuilder stb, ItemId cs) = \set(expandUserTypes(et,stb,cs));
private Symbol et(\rel(list[Symbol] fts), STBuilder stb, ItemId cs) = \rel([expandUserTypes(ft,stb,cs)|ft<-fts]);
private Symbol et(\tuple(list[Symbol] fs), STBuilder stb, ItemId cs) = \tuple([expandUserTypes(f,stb,cs) | f <- fs]);
private Symbol et(\list(Symbol et), STBuilder stb, ItemId cs) = \list(expandUserTypes(et,stb,cs));
private Symbol et(\map(Symbol md, Symbol mr), STBuilder stb, ItemId cs) = \map(expandUserTypes(md,stb,cs),expandUserTypes(mr,stb,cs));
private Symbol et(\reified(Symbol rt), STBuilder stb, ItemId cs) = \reified(expandUserTypes(rt,stb,cs));
private Symbol et(\bag(Symbol et), STBuilder stb, ItemId cs) = \bag(expandUserTypes(et,stb,cs));
private Symbol et(\adt(str s, list[Symbol] ps), STBuilder stb, ItemId cs) = \adt(s,[expandUserTypes(p,stb,cs)|p <- ps]);
private Symbol et(Symbol::\cons(Symbol a, list[Symbol] ps), STBuilder stb, ItemId cs) = Symbol::\cons(expandUserTypes(a,stb,cs),[expandUserTypes(p,stb,cs)|p<-ps]);
private Symbol et(\alias(str s, list[Symbol] ps, Symbol at), STBuilder stb, ItemId cs) = \alias(s,[expandUserTypes(p,stb,cs)|p<-ps],expandUserTypes(at,stb,cs));
private Symbol et(Symbol::\func(Symbol rt, list[Symbol] ps), STBuilder stb, ItemId cs) = Symbol::\func(expandUserTypes(rt,stb,cs),[expandUserTypes(p,stb,cs)|p <- ps]);

private Symbol et(\overloaded(set[Symbol] os), STBuilder stb, ItemId cs) = \overloaded({expandUserTypes(\o,stb,cs)|\o<-os});
private Symbol et(\user(RName rn, list[Symbol] ps), STBuilder stb, ItemId cs) {
    list[Symbol] params = [ expandUserTypes(tp,stb,cs) | tp <- ps ];
    set[ItemId] userTypes = getItems(stb,cs,rn,Types());
    set[ItemId] aliasItems = { pi | pi <- userTypes, Alias(_,_,_,_) := stb.scopeItemMap[pi] };
    set[ItemId] adtItems = { pi | pi <- userTypes, ADT(_,_,_,_) := stb.scopeItemMap[pi] };
    
    if (size(userTypes - aliasItems - adtItems) > 0) 
        throw "Unexpected case, got a user type that is not an alias or an adt, example: <stb.scopeItemMap[getOneFrom(userTypes-aliasItems-adtItems)]>";

    if (size(aliasItems) > 0 && size(adtItems) > 0)
        throw "Unexpected case, got a user type that is both an alias and an adt, examples: <stb.scopeItemMap[getOneFrom(aliasItems)]> and <stb.scopeItemMap[getOneFrom(adtItems)]>";
    
    // TODO: Maybe throw an exception here, and catch it in the RUnknownType case?
    if (size(aliasItems) == 0 && size(adtItems) == 0)
        return rt; // Could not expand, may be an unknown type...
        
    // TODO: It should be fine to have more than 1 here, but verify (they should all be the same).
    // TODO: Instantiate here? Any use of a user type with params that is not the declaration
    // site should probably be instantiated to get back the proper type.        
    if (size(adtItems) > 0) {
        Symbol resultType = getTypeForItem(stb, getOneFrom(adtItems));
        list[Symbol] adtParameters = getADTTypeParameters(resultType);
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
        Symbol resultType = getTypeForItem(stb, getOneFrom(aliasItems));
        list[Symbol] aliasParameters = getAliasTypeParameters(resultType);
        if (size(params) > 0) {
            if (size(params) != size(aliasParameters)) {
                throw "Unexpected case, we have a different number of type parameters in the alias use and aliased type definition: <prettyPrintType(rt)> versus <prettyPrintType(resultType)>";            
            } else {
                map[RName varName, Symbol varType] bindings = ( );
                for (n <- [0..size(params)]) bindings = bindings + ( aliasParameters[n] : params[n] );
                resultType = makeParameterizedAliasType(getAliasName(resultType), instantiateVars(getAliasedType(resultType),bindings), params);
            }
        }
        
        return resultType;
    }
}

@doc{Expand any user types inside the given type into their actual adt or alias representation.}
public Symbol expandUserTypes(Symbol rt, STBuilder stBuilder, ItemId currentScope) {
	Symbol expandedType = et(rt,stBuilder,currentScope);
    if ( (rt@at)? ) expandedType = expandedType[@at=rt@at];
    return expandedType;        
}

@doc{Get types back for individual scope items / source locations.}
public Symbol getTypeForItem(STBuilder stBuilder, ItemId itemId) {
    if (itemId notin stBuilder.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";
    
    Item si = stBuilder.scopeItemMap[itemId];
    Symbol rt = makeVoidType();
    
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

public bool hasSymbol(STBuilder stBuilder, loc l) {
	if (l in stBuilder.itemUses<0>) return true;
	if (size({ m | m:error(_,_) <- stBuilder.messages[l] }) > 0) return true;
    return false;
}

public Symbol getSymbol(STBuilder stBuilder, loc l) {
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

alias ConvertTuple = tuple[STBuilder stBuilder, Symbol rtype];

public ConvertTuple convertRascalType(STBuilder stBuilder, Type t) {
    // Step 1: convert the type
    Symbol rt = convertType(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    list[tuple[str msg, loc at]] conversionWarnings = [ ];
    
    visit(rt) { 
        case Symbol ct : {
            if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo;
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
    Symbol rt = convertUserType(t);

    // Step 2: look for any errors marked on the converted type
    list[tuple[str msg, loc at]] conversionErrors = [ ];
    visit(rt) { case Symbol ct : if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo; }

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

//public STBuilder checkADTDefinitionsForConsistency(STBuilder stBuilder) {
//    <inModule, moduleLayerId> = getSurroundingModule(stBuilder, head(stBuilder.scopeStack)); 
//    loc moduleLoc = stBuilder.scopeItemMap[moduleLayerId].definedAt;
//    
//    // Check each ADT individually for field type consistency
//    for (n <- domain(stBuilder.adtMap)) {
//        map[RName fieldName, Symbol fieldType] fieldMap = ( );
//
//        // First check imported constructors. If we get errors, we would rather have them on the constructors
//        // defined in the current module, since they are easier to fix -- checking them later preferences the
//        // types assigned to field in imported types.
//        for (ci <- stBuilder.adtMap[n].consItems, ci in stBuilder.scopeRel[last(stBuilder.scopeStack)]) {
//            Symbol consType = getTypeForItem(stBuilder, ci);
//            if (isConstructorType(consType)) {
//                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
//                for (RNamedType(nt,nn) <- argTypes) {
//                    if (nn notin fieldMap) {
//                        fieldMap[nn] = nt;
//                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
//                        stBuilder = addScopeError(stBuilder, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
//                    }
//                }
//            } else {
//                throw "checkADTDefinitionsForConsistency, unexpected constructor item <stBuilder.scopeItemMap[ci]>";
//            }
//        }
//        
//        // Now check the fields on the ADTs defined in the current module.
//        // TODO: May be good to refactor out identical checking code
//        for (ci <- stBuilder.adtMap[n].consItems, ci in stBuilder.scopeRel[last(stBuilder.scopeStack)]) {
//            Symbol consType = getTypeForItem(stBuilder, ci);
//            if (isConstructorType(consType)) {
//                list[RNamedType] argTypes = getConstructorArgumentTypesWithNames(consType);
//                for (RNamedType(nt,nn) <- argTypes) {
//                    if (nn notin fieldMap) {
//                        fieldMap[nn] = nt;
//                    } else if (nn in fieldMap && !equivalent(fieldMap[nn],nt)) {
//                        stBuilder = addScopeError(stBuilder, moduleLoc, "Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>");
//                    }
//                }               
//            } else {
//                throw "checkADTDefinitionsForConsistency, unexpected constructor item <stBuilder.scopeItemMap[ci]>";
//            }
//        }
//    }
//    
//    return stBuilder; 
//}

///////////////////////////////////////////////////////////////////////////////////////////
//
// Scoping convenience functions that deal with types
//
// TODO: See which of these should be moved into STBuilder directly.
//
// TODO: Field update syntax is causing problems with type derivation in the interpreter.
//
///////////////////////////////////////////////////////////////////////////////////////////
