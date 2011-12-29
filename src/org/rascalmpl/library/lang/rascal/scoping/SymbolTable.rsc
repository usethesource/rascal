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
module lang::rascal::scoping::SymbolTable

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
import lang::rascal::checker::ListUtils;
import lang::rascal::syntax::RascalRascal;

//
// Rascal Namespaces. Rascal has separate namespaces for modules, labels,
// FCVs (functions, constructors, and variables), types, type variables,
// annotations, rules, and tags.
//
data Namespace = Modules() | Labels() | FCVs() | Types() | TypeVars() | Annotations() | Rules() | Tags() ;

//
// Each item in the symbol table is given a unique ID.
//
public alias ItemId = int;

//
// The symbol table is made up of a mix of different items, some of
// which represent scopes (BlockScope), some of which represent named
// items in the table (ADT), and some of which represent both (Function).
//
data Item =
      BlockScope(ItemId parentId, loc definedAt)
    | BooleanExpScope(ItemId parentId, loc definedAt)
    | OrScope(ItemId parentId, loc definedAt)
	| PatternMatchScope(ItemId parentId, loc definedAt)
    | TopScope()

    | Function(RName functionName, RType returnType, list[tuple[RType,Pattern]] params, list[RType] throwsTypes, bool isPublic, bool isVarArgs, ItemId parentId, loc definedAt)
    | Module(RName moduleName, ItemId parentId, loc definedAt)

    | ADT(RType adtType, bool isPublic, ItemId parentId, loc definedAt) 
    | Alias(RType aliasType, bool isPublic, ItemId parentId, loc definedAt)
    | Annotation(RName annotationName, RType annoType, RType onType, bool isPublic, ItemId parentId, loc definedAt) 
    | Constructor(RName constructorName, list[RNamedType] constructorArgs, ItemId adtParentId, ItemId parentId, loc definedAt)
    | FormalParameter(RName parameterName, RType parameterType, ItemId parentId, loc definedAT)
    | Label(RName labelName, ItemId parentId, loc definedAt)
    | Rule(RName ruleName, ItemId parentId, loc definedAt)
    | TypeVariable(RType typeVar, ItemId parentId, loc definedAt)
	| Variable(RName variableName, RType variableType, ItemId parentId, loc definedAt)
	| Tag(ItemId parentId, loc definedAt)
;

//
// Identify which scope items are included in which namespaces
//
private rel[Namespace, str] namespaceItems = { <Modules(), "Module">, <Labels(), "Label">, <FCVs(), "Constructor">,
                                               <FCVs(), "Function">, <FCVs(), "FormalParameter">, <FCVs(), "Variable">,
                                               <Types(), "ADT">, <Types(), "Alias">, <TypeVars(), "TypeVariable">,
                                               <Annotations(), "Annotation">, <Rules(), "Rule">, <Tags(), "Tag"> };
//
// Named scope items.
//
private set[str] namedItems = { "Module", "Function", "Variable", "FormalParameter", "Label", "Alias", 
                                "Constructor", "ADT", "Annotation", "Rule", "TypeVariable" };

//
// Indicate if the item has a name (like a variable)
//
public bool itemHasName(Item item) {
    return getName(item) in namedItems;
}

//
// If the item has a syntactical name, retrieve it.
//
public RName getItemName(Item item) {
    switch(getName(item)) {
        case "Module" : return item.moduleName;
        case "Function" : return item.functionName;
        case "Variable" : return item.variableName;
        case "FormalParameter" : return item.parameterName;
        case "Label" : return item.labelName;
        case "Alias" : return item.aliasType.aliasName;
        case "Constructor" : return item.constructorName;
        case "ADT" : return item.adtType.adtName;
        case "Annotation" : return item.annotationName;
        case "Rule" : return item.ruleName;
        case "TypeVariable" : return item.typeVar.varName;
		default : throw "Item does not have a name, use itemHasHame(Item item) to check first to ensure the item has a name: <item>";
	}
}

//
// Does item represent a scope?
//
public bool isScope(Item si) {
    return getName(si) in { "TopScope", "Module", "Function", "PatternMatchScope", "BooleanExpScope", "OrScope", "BlockScope" };
}

//
// Does item reprsent a named entity, not just a scope construct?
//
public bool isItem(Item si) {
	return getName(si) in { "ADT", "Alias", "Annotation", "Constructor", "Function", "FormalParameter", "Label", "Module", "Rule", "TypedVariable", "Variable", "Tag" };  
}

//
// Symbol table items can have an associated location or locations
//
data STBuilder = STBuilder(
    rel[ItemId scopeId, ItemId itemId] scopeRel, // Basic symbol table: which items are nested in which items
    rel[ItemId scopeId, RName itemName, ItemId itemId] scopeNames, // Same as scopeRel, but just for named items
    rel[loc useLoc, ItemId usedItem] itemUses, // relates locations in the tree to the items that are used at those locations
    rel[loc, Message] messages, 
    list[ItemId] scopeStack, 
    map[ItemId,Item] scopeItemMap, 
    map[int, RType] inferredTypeMap,
    map[loc, ItemId] returnMap,
    map[loc, RType] itBinder, 
    map[RName adtName,tuple[set[ItemId] adtItems,set[ItemId] consItems] adtInfo] adtMap,
    ItemId nextScopeId, 
    int freshType
);

//
// These types are used in functions that add/update the scope information,
// since we need to return multiple items -- the new table, plus information
// about what was added/updated.
//
alias AddedItemPair = tuple[STBuilder stBuilder, ItemId addedId];
alias ScopeUpdatePair = tuple[STBuilder stBuilder, ItemId oldScopeId];
  
//                      
// Create an empty symbol table
//                        
public STBuilder createNewSTBuilder() {
	return STBuilder( { }, { }, { }, { }, [ ], ( ), ( ), ( ), ( ), ( ), 0, 0 );
}                    

//
// Given a number of different OR scope layers in the symbol table, find the subset of
// variables declared in all the layers. Note that, at this point, we just pick one of
// the identical variable as a representative, since they are all considered the same.
// TODO: Should we do anything here for parameterized types? For instance, if one or
// branch introduced one set of bindings, and the other introduced (somehow) a different
// set of bindings?
// TODO: Add location information so we can track back to all points of definition
//
public STBuilder mergeOrScopes(STBuilder stBuilder, list[ItemId] orLayers, ItemId intoLayer) {
    // First, get back the items introduced in the first or layer in the list
    set[ItemId] introducedItems = { vi | vi <- stBuilder.scopeRel[head(orLayers)], Variable(_,_,_,_) := stBuilder.scopeItemMap[vi] };
	
    // Now, go through the rest of the layers -- we only keep vars found in all the layers with the same name.
    // Note that we don't restrict this based on type here, since we may not know the type yet -- this
    // is instead a decision for the type checker.
    for (oritem <- tail(orLayers)) {
        set[ItemId] sharedItems = { };
        for (li <- introducedItems, Variable(vn,_,_,_) := stBuilder.scopeItemMap[li],
            ri <- stBuilder.scopeRel[oritem], Variable(vn,_,_,_) := stBuilder.scopeItemMap[ri]) {
            sharedItems = sharedItems + li;
        }
        introducedItems = sharedItems;
    }

	// Finally, inject them into the intoLayer
    stBuilder = pushScope(intoLayer, stBuilder);
    for (oritem <- introducedItems, Variable(vn,vt,_,_) := stBuilder.scopeItemMap[oritem])
        stBuilder = justSTBuilder(addVariableToScope(vn, vt, false, stBuilder.scopeItemMap[oritem].definedAt, stBuilder));
    stBuilder = popScope(stBuilder);

    return stBuilder;
}

//
// Indicate that scopeItem is used at location l
//
public STBuilder addItemUse(STBuilder stBuilder, ItemId scopeItem, loc l) {
	t = < l, scopeItem >;
    return stBuilder[itemUses = stBuilder.itemUses + t];
}

//
// Indicate that scopeItems are used at location l
//
public STBuilder addItemUses(STBuilder stBuilder, set[ItemId] scopeItems, loc l) {
    return stBuilder[itemUses = stBuilder.itemUses + ({l} * scopeItems)];
}

//
// Add a scope error with message msg at location l
//
public STBuilder addScopeError(STBuilder stBuilder, loc l, str msg) {
	t = < l, error(msg,l) >;
	stBuilder.messages = stBuilder.messages + t;
    return stBuilder;
}

public STBuilder addScopeWarning(STBuilder stBuilder, loc l, str msg) {
	t = < l, warning(msg,l) >;
	stBuilder.messages = stBuilder.messages + t;
    return stBuilder;
}

//
// Get back the layer defined at location l. Note: this operation is VERY expensive,
// and should be used sparingly.
//
public ItemId getLayerAtLocation(loc l, STBuilder stBuilder) {
    set[ItemId] layers = { si | si <- stBuilder.scopeItemMap<0>, isScope(stBuilder.scopeItemMap[si]), TopScope() !:= stBuilder.scopeItemMap[si], stBuilder.scopeItemMap[si].definedAt == l };
    if (size(layers) == 1)
        return getOneFrom(layers);
    else if (size(layers) == 0)
        throw "getLayerAtLocation: Error, trying to retrieve layer item from location <l> with no associated layer.";  		
    else {
        throw "getLayerAtLocation: Error, trying to retrieve layer item from location <l> with more than 1 associated layer.";
    }	
}

//
// Update the item stored at ID idToUpdate
//
public STBuilder updateItem(Item si, ItemId idToUpdate, STBuilder stBuilder) {
    return stBuilder[scopeItemMap = stBuilder.scopeItemMap + (idToUpdate : si)];				
}

//
// Get the item stored at ID id
//
public Item getItem(ItemId id, STBuilder stBuilder) {
    return stBuilder.scopeItemMap[id];
}
	
//
// Pretty printers for scope information
//
public str prettyPrintSI(STBuilder st, Item si) {
    str prettyPrintParam(tuple[RType,Pattern] param) {
        return prettyPrintType(param[0]);
    }
    
    str prettyPrintParams(list[tuple[RType,Pattern]] params) {
        return joinList(params, prettyPrintParam, ", ", "");
    }

    str prettyPrintSIAux(Item si) {
        switch(si) {
            case TopScope() : return "TopScope";
            case PatternMatchScope(_,_) : return "PatternMatchScope";
            case BooleanExpScope(_,_) : return "BooleanExpScope";
            case OrScope(_,_) : return "OrScope";
            case BlockScope(_,_) : return "BlockScope";
            case Module(x,_,_) : return "Module <prettyPrintName(x)>";
            case Function(x,t,ags,_,_,_,_,_) : return "Function <prettyPrintType(t)> <prettyPrintName(x)>(<prettyPrintParams(ags)>)";
            case Variable(x,t,_,_) : return "Variable <prettyPrintType(t)> <prettyPrintName(x)>";
            case TypeVariable(t,_,_) : return "Type Variable <prettyPrintType(t)>";
            case FormalParameter(x,t,_,_) : return "Formal Parameter <prettyPrintType(t)> <prettyPrintName(x)>";
            case Label(x,_,_) : return "Label <prettyPrintName(x)>";
            case Alias(atype,_,_,_) : return "Alias <prettyPrintType(atype)>";
            case Constructor(cn,tas,_,_,_) : 	return "Constructor <prettyPrintName(cn)>(<prettyPrintNamedTypeList(tas)>)";
            case ADT(ut,_,_,_) : return "ADT <prettyPrintType(ut)>";
            case Annotation(x,atyp,otyp,_,_,_) : return "Annotation <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)>";
            case Rule(x,_,_) : return "Rule <prettyPrintName(x)>";
            case Tag(_,_) : return "Tag";
        }
    }
    
    return prettyPrintSIAux(si);
}

public str prettyPrintSIWLoc(STBuilder st, Item si) {
    if (TopScope() := si) return prettyPrintSI(st,si);
    return prettyPrintSI(st,si) + " defined at <si.definedAt>";
//    return prettyPrintSI(st,si) + ( TopScope() := si ? "" : " defined at <si.definedAt>" );
}

//
// Filter a set of items to only include those in the given namespace
//
private set[ItemId] filterNamesForNamespace(STBuilder stBuilder, set[ItemId] scopeItems, Namespace namespace) {
    return { i | i <- scopeItems, getName(stBuilder.scopeItemMap[i]) in namespaceItems[namespace] };
}

//
// Get the ID for the surrounding module layer, if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inModule, ItemId moduleId] getSurroundingModule(STBuilder stBuilder, ItemId currentScopeId) {
    if (Module(_,_,_) := stBuilder.scopeItemMap[currentScopeId]) {
        return < true, currentScopeId >;
    } else if (TopScope() := stBuilder.scopeItemMap[currentScopeId]) {
        return < false, -1 >;
    } else {
        return getSurroundingModule(stBuilder,stBuilder.scopeItemMap[currentScopeId].parentId);
    }
}

//
// Get the ID for the surrounding function layer, if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inFunction, ItemId functionId] getSurroundingFunction(STBuilder stBuilder, ItemId currentScopeId) {
    if (Function(_,_,_,_,_,_,_,_) := stBuilder.scopeItemMap[currentScopeId]) {
        return < true, currentScopeId >;
    } else if (TopScope() := stBuilder.scopeItemMap[currentScopeId]) {
        return < false, -1 >;
    } else {
        return getSurroundingFunction(stBuilder,stBuilder.scopeItemMap[currentScopeId].parentId);
    }
}

//
// Get the ID for the outermost surrounding function layer (think nested functions), if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inFunction, ItemId functionId] getOutermostFunction(STBuilder stBuilder, ItemId currentScopeId) {
    <inFunction, functionId> = getSurroundingFunction(stBuilder, currentScopeId);
    if (inFunction) {
        <inFunction2,functionId2> = getSurroundingFunction(stBuilder,stBuilder.scopeItemMap[functionId].parentId);
        while(inFunction2) {
            inFunction = inFunction2;
            functionId = functionId2;
            <inFunction2,functionId2> = getSurroundingFunction(stBuilder,stBuilder.scopeItemMap[functionId].parentId);
        }
    }
    return <inFunction, functionId>;
}

//
// Get items of name x defined in the the current module or, if no names are found in the module, in
// the top layer.
//
private set[ItemId] getModuleAndTopItems(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    set[ItemId] foundItems = { };
    < inModule, moduleId > = getSurroundingModule(stBuilder,currentScopeId);
    if (inModule)
        foundItems = filterNamesForNamespace(stBuilder, stBuilder.scopeNames[moduleId,x], ns);
    if (size(foundItems) == 0)
        foundItems = filterNamesForNamespace(stBuilder, stBuilder.scopeNames[last(stBuilder.scopeStack),x], ns);
    return foundItems;    
}

//
// The same as above, except this only checks in the current module.
//
private set[ItemId] getModules(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    set[ItemId] foundItems = { };
    < inModule, moduleId > = getSurroundingModule(stBuilder,currentScopeId);
    if (inModule)
        foundItems = filterNamesForNamespace(stBuilder, stBuilder.scopeNames[currentScopeId,x], ns);
    return foundItems;    
}

//
// The same as above, except this only checks in the top layer.
//
private set[ItemId] getTopItems(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    return filterNamesForNamespace(stBuilder, stBuilder.scopeNames[last(stBuilder.scopeStack),x], ns);
}

//
// Get items, stopping at the given bound. Note that this assumes that the bound is above the current
// scope ID, we do not verify that here.
//
// TODO: Put in logic to stop at the top
//
private set[ItemId] getBoundedItems(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns, ItemId boundingId) {
    set[ItemId] foundItems = filterNamesForNamespace(stBuilder, stBuilder.scopeNames[currentScopeId,x], ns);
    if (size(foundItems) == 0) {
        if (currentScopeId != boundingId) {
            foundItems = getBoundedItems(stBuilder, stBuilder.scopeItemMap[currentScopeId].parentId, x, ns, boundingId);
        }
    }
    return foundItems;
}

//
// Get items of name x defined in the current function, starting at the current scope layer and working
// up to the function layer the current scope is nested within.
//
private set[ItemId] getCurrentFunctions(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    set[ItemId] foundItems = { };
    < inFunction, functionId > = getSurroundingFunction(stBuilder,currentScopeId);
    if (inFunction) {
        foundItems = getBoundedItems(stBuilder, currentScopeId, x, ns, functionId);
    }
    return foundItems;    
}

//
// Get items of name x defined in the current function, starting at the current scope layer and working
// up to the outermost function layer the current scope is nested within.
//
private set[ItemId] getNestedFunctions(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    set[ItemId] foundItems = { };
    < inFunction, functionId > = getOutermostFunction(stBuilder,currentScopeId);
    if (inFunction) {
        foundItems = getBoundedItems(stBuilder, currentScopeId, x, ns, functionId);
    }
    return foundItems;    
}

//
// Get items of name x defined between the current scope and the top.
//
private set[ItemId] getAllItems(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    return getBoundedItems(stBuilder, currentScopeId, x, ns, last(stBuilder.scopeStack));
}

//
// Look up names associated with modules. Modules are always created at the top layer, since
// we do not (currently) support nested modules.
//
// TODO: If we ever support nested modules, this will need to be changed.
//
private set[ItemId] getModules(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getTopItems(stBuilder,currentScopeId,x,Modules());
}

//
// Look up names associated with labels. Labels are only visible within the current function.
//
// TODO: What if we are not inside a function, but are instead at the module level when we
// encounter the label? We probably want to limit scope to just the current statement instead.
//
private set[ItemId] getLabels(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getCurrentFunctions(stBuilder,currentScopeId,x,Labels());
}

//
// Look up names associated with variables, constructors, and functions. Since these names can
// shadow one another, start the lookup at the current level and continue to the top scope.
//
private set[ItemId] getFCVItems(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getAllItems(stBuilder, currentScopeId, x, FCVs());
}

//
// The same as above, but only lookup to the top of the current function scope (assuming
// we are inside a function...)
//
private set[ItemId] getFCVItemsForConflicts(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getNestedFunctions(stBuilder, currentScopeId, x, FCVs());
}

//
// Look up names associated with types (aliases, ADTs). These are always visible either in the
// current module or at the top level (for imported names).
//
private set[ItemId] getTypeItems(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getModuleAndTopItems(stBuilder,currentScopeId,x,Types());
}

//
// Look up names associated with type variables. If one is inside a function, we find the first
// definition inside a function (either the current function or a parent function).
//
// TODO: What if we are not inside a function, but are instead at the module level when we
// encounter the label? We probably want to limit scope to just the current statement instead.
//
private set[ItemId] getTypeVarItems(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getNestedFunctions(stBuilder,currentScopeId,x,TypeVars());
}

//
// Look up names associated with annotations. These are always visible either in the
// current module or at the top level (for imported names).
//
// NOTE: Node defines getAnnotations, so we leave this as getAnnotationItems to make it unique
//
private set[ItemId] getAnnotationItems(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getModuleAndTopItems(stBuilder,currentScopeId,x,Annotations());
}

//
// Look up names associated with rules. These are always visible either in the
// current module or at the top level (for imported names).
//
private set[ItemId] getRules(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getModuleAndTopItems(stBuilder,currentScopeId,x,Rules());
}

//
// Look up names associated with tags. These are always visible either in the
// current module or at the top level (for imported names).
//
private set[ItemId] getTags(STBuilder stBuilder, ItemId currentScopeId, RName x) {
    return getModuleAndTopItems(stBuilder,currentScopeId,x,Tags());
}

private map[Namespace, set[ItemId] (STBuilder, ItemId, RName)] lookupFunctions = (
    Modules() : getModules,
    Labels() : getLabels,
    FCVs() : getFCVItems,
    Types() : getTypeItems,
    TypeVars() : getTypeVarItems,
    Annotations() : getAnnotationItems,
    Rules() : getRules,
    Tags() : getTags );

private map[Namespace, set[ItemId] (STBuilder, ItemId, RName)] lookupFunctionsForConflicts = (
    Modules() : getModules,
    Labels() : getLabels,
    FCVs() : getFCVItemsForConflicts,
    Types() : getTypeItems,
    TypeVars() : getTypeVarItems,
    Annotations() : getAnnotationItems,
    Rules() : getRules,
    Tags() : getTags );
    
//
// Get back items of a given name from the symbol table. This handles dispatch to the correct
// lookup function (encoded in the map above), plus it handles lookups in qualified names.
//
private set[ItemId] getItemsGeneral(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns, map[Namespace, set[ItemId] (STBuilder, ItemId, RName)] lookupFuns) {
    // We look up modules by their full name. So, if we aren't looking up modules, and we have a compound
    // name, switch to the correct context, else just do a normal lookup.
    if (Modules() !:= ns && RCompoundName(nl) := x) {
        // The module name is the qualified name, minus the last part of the name. 
        RName moduleName = (size(nl) == 2) ? RSimpleName(nl[0]) : RCompoundName(head(nl,size(nl)-1)); 
		x = RSimpleName(nl[size(nl)-1]);
		
		// Get the module item representing the given module name, it should be in scope.
		set[ItemId] mods = getModules(stBuilder, currentScopeId, moduleName);
		
		// If we found the module, do the lookup in that context. It should be the case that
		// we have just one match; if we do not, then we have an error, which will show up
		// as a scope error saying the name cannot be found.
		//
		// TODO: Once we have parameterized modules working, we need to decide how to do this,
		// a simple lookup based on the module name may no longer by valid.
		if (size(mods) == 1) {
			return getItemsGeneral(stBuilder, stBuilder.scopeItemMap[getOneFrom(mods)].parentId, x, ns, lookupFuns);
		} else if (size(mods) > 1) {
			// TODO: Error, should be caught processing imports -- this means multiple modules have the same name
			// TODO: Throw an exception here!
			return { }; 
		} else {
			return { };
		}
	}

	// We fall through to here IF a) the name is not a qualified name, or b) the name IS a qualified 
	// name but is the name of a module. Otherwise, it is taken care of above.
    return (lookupFuns[ns])(stBuilder,currentScopeId,x);
}

public set[ItemId] getItems(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    return getItemsGeneral(stBuilder, currentScopeId, x, ns, lookupFunctions);
}    

public set[ItemId] getItemsForConflicts(STBuilder stBuilder, ItemId currentScopeId, RName x, Namespace ns) {
    return getItemsGeneral(stBuilder, currentScopeId, x, ns, lookupFunctionsForConflicts);
}    

///////////////////////////////////////////////////////////////////////////////////////////
//
// Functions to push new scope layers and add scope items; also performs
// some error checking.
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// Add a new item into the symbol table.
//
public AddedItemPair addItem(Item si, ItemId parentId, loc l, STBuilder stBuilder) {
	return addItem(si, parentId, l, stBuilder, false);
}

//
// Add a new item into the symbol table. Also handle marking of uses of items, if desired.
//
public AddedItemPair addItem(Item si, ItemId parentId, loc l, STBuilder stBuilder, bool flagUse) {
    int newItemId = stBuilder.nextScopeId;
    stBuilder.nextScopeId += 1;

	// insert the new item
    stBuilder.scopeItemMap = stBuilder.scopeItemMap + (newItemId : si);

	// add it to the symbol table
	t1 = < parentId, newItemId >;
    stBuilder.scopeRel = stBuilder.scopeRel + t1;

	// add it to the symbol table names relation, if it has a name
    if (itemHasName(si)) {
		t2 = < parentId, getItemName(si), newItemId >;
    	stBuilder.scopeNames = stBuilder.scopeNames + t2;
    }

	// flag this as a use of the item being added
	// TODO: We should really add this as a use at the location of the name!
    if (flagUse) 
    	if (itemHasName(si) && (getItemName(si)@at)?) {
    		stBuilder = addItemUse(stBuilder, newItemId, getItemName(si)@at);
    	} else {
    		stBuilder = addItemUse(stBuilder, newItemId, l);
    	}

    return < stBuilder, newItemId >;				
}

//
// Push an existing item onto the scope stack, setting this to be the current scope
//
public STBuilder pushScope(ItemId newScope, STBuilder stBuilder) {
	return stBuilder[scopeStack = [newScope] + stBuilder.scopeStack];
}

//
// Pop the current head of the scope stack, making the new head of the stack the current scope
//
public STBuilder popScope(STBuilder stBuilder) {
	if (size(stBuilder.scopeStack) == 0) throw "popScope: Scope Stack is empty, cannot pop!";
	return stBuilder[scopeStack = tail(stBuilder.scopeStack)];
}

//
// Add a new scope into the symbol table and make it the current scope
//
public AddedItemPair pushNewScope(Item scopeItem, ItemId parentId, loc l, STBuilder stBuilder) {
	return pushNewScope(scopeItem, parentId, l, stBuilder, false);
}

//
// Add a new scope into the symbol table and make it the current scope. Also handle marking
// uses of items, if desired.
//
// TODO: Since we now allow the parent Id, the scope stack is not really accurate. But, we don't
// want to have to completely redo this. Look into whether we need a way to make this accurate,
// though, instead of just making sure the head and last are accurate (which they still are).
//
public AddedItemPair pushNewScope(Item scopeItem, ItemId parentId, loc l, STBuilder stBuilder, bool markUse) {
	< stBuilder, addedId > = addItem(scopeItem, parentId, l, stBuilder);
	stBuilder = pushScope(addedId, stBuilder);
	if (markUse) stBuilder = addItemUse(stBuilder, addedId, l);
	return < stBuilder, addedId >;
}

//
// Add the top scope into the symbol table. This should only be done in an empty symbol
// table.
// TODO: Verify that this constraint holds.
//
public AddedItemPair pushNewTopScope(STBuilder stBuilder) {
    int newItemId = stBuilder.nextScopeId;
    stBuilder.nextScopeId += 1;

    Item item = TopScope();
    stBuilder.scopeItemMap = stBuilder.scopeItemMap + ( newItemId : item );

	stBuilder = pushScope(newItemId, stBuilder);

	return < stBuilder, newItemId >; 	
}

//
// Insert a new module scope. The additional code also adds the module name into the scope relation
// for the current module, since it is visible within itself.
//
public AddedItemPair pushNewModuleScope(RName moduleName, loc l, STBuilder stBuilder) {
	< stBuilder, addedId > = pushNewScope(Module(moduleName, head(stBuilder.scopeStack),l), head(stBuilder.scopeStack), l, stBuilder);
	t = < addedId, moduleName, addedId >;
	stBuilder.scopeNames = stBuilder.scopeNames + t;
	return < stBuilder, addedId >;
}

//
// Insert a new boolean scope, used to enforce scoping of names introduced in boolean expressions.
//
public AddedItemPair pushNewBooleanScope(loc l, STBuilder stBuilder) {
	return pushNewScope(BooleanExpScope(head(stBuilder.scopeStack),l), head(stBuilder.scopeStack), l, stBuilder);
}

//
// Insert a new OR scope, used to enforce the scoping rule for names in || or similar expressions
// (that rule being that names need to be introduced on all branches to be visible)
//
public AddedItemPair pushNewOrScope(loc l, STBuilder stBuilder) {
	return pushNewScope(OrScope(head(stBuilder.scopeStack),l), head(stBuilder.scopeStack), l, stBuilder);
}

//
// Insert a new block scope
//
public AddedItemPair pushNewBlockScope(loc l, STBuilder stBuilder) {
	return pushNewScope(BlockScope(head(stBuilder.scopeStack),l), head(stBuilder.scopeStack), l, stBuilder);
}

//
// Insert a new pattern match scope, used in match operations inside cases, etc
//
public AddedItemPair pushNewPatternMatchScope(loc l, STBuilder stBuilder) {
	return pushNewScope(PatternMatchScope(head(stBuilder.scopeStack),l), head(stBuilder.scopeStack), l, stBuilder);
}

//
// Insert a new function scope. Like with the module scope, the function scope adds the function name
// into the scope for the function itself, since the function name is visible inside the function.
//
public AddedItemPair pushNewFunctionScopeAt(bool hasAnonymousName, RName functionName, RType retType, list[tuple[RType,Pattern]] ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, STBuilder stBuilder, ItemId scopeToUse) {
    if (hasAnonymousName) functionName = RSimpleName("@ANONYMOUS_FUNCTION_<stBuilder.nextScopeId>");
    < stBuilder, addedId > = pushNewScope(Function(functionName, retType, ps, throwsTypes, isPublic, isVarArgs, scopeToUse, l), scopeToUse, l, stBuilder);
    t = < addedId, functionName, addedId >;
	stBuilder.scopeNames = stBuilder.scopeNames + t;
	return < stBuilder, addedId >;
}

//
// Insert the function scope inside the current scope
//
public AddedItemPair pushNewFunctionScope(RName functionName, RType retType, list[tuple[RType,Pattern]] ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, STBuilder stBuilder) {
	return pushNewFunctionScopeAt(false, functionName, retType, ps, throwsTypes, isPublic, isVarArgs, l, stBuilder, head(stBuilder.scopeStack));
}

//
// Insert the function scope inside the top scope, used for imported functions
//
public AddedItemPair pushNewFunctionScopeAtTop(RName functionName, RType retType, list[tuple[RType,Pattern]] ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, STBuilder stBuilder) {
	return pushNewFunctionScopeAt(false, functionName, retType, ps, throwsTypes, isPublic, isVarArgs, l, stBuilder, last(stBuilder.scopeStack));
} 

//
// Insert a new closure scope, which is just a function scope for an anonymously-named function
//
public AddedItemPair pushNewClosureScope(RType retType, list[tuple[RType,Pattern]] ps, loc l, STBuilder stBuilder) {
    return pushNewFunctionScopeAt(true, RSimpleName(""), retType, ps, [], false, false, l, stBuilder, head(stBuilder.scopeStack));
}

//
// Insert a new void closure scope, which is just a function scope for an anonymously-named function
//
public AddedItemPair pushNewVoidClosureScope(list[tuple[RType,Pattern]] ps, loc l, STBuilder stBuilder) {
    return pushNewFunctionScopeAt(true, RSimpleName(""), makeVoidType(), ps, [], false, false, l, stBuilder, head(stBuilder.scopeStack));
}

//
// Add an alias into the given scope.
//
public AddedItemPair addAliasToScopeAt(RType aliasType, bool isPublic, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Alias(aliasType, isPublic, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addAliasToScope(RType aliasType, bool isPublic, loc l, STBuilder stBuilder) {
	return addAliasToScopeAt(aliasType, isPublic, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addAliasToTopScope(RType aliasType, bool isPublic, loc l, STBuilder stBuilder) {
	return addAliasToScopeAt(aliasType, isPublic, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add a variable into the given scope.
//
public AddedItemPair addVariableToScopeAt(RName varName, RType varType, bool isPublic, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Variable(varName, varType, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addVariableToScope(RName varName, RType varType, bool isPublic, loc l, STBuilder stBuilder) {
	return addVariableToScopeAt(varName, varType, isPublic, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addVariableToTopScope(RName varName, RType varType, bool isPublic, loc l, STBuilder stBuilder) {
	return addVariableToScopeAt(varName, varType, isPublic, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add a type variable into the given scope.
//
public AddedItemPair addTypeVariableToScopeAt(RType varType, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(TypeVariable(varType, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addTypeVariableToScope(RType varType, loc l, STBuilder stBuilder) {
	return addTypeVariableToScopeAt(varType, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addTypeVariableToTopScope(RType varType, loc l, STBuilder stBuilder) {
	return addTypeVariableToScopeAt(varType, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add an ADT into the given scope.
//
public AddedItemPair addADTToScopeAt(RType adtName, bool isPublic, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(ADT(adtName, isPublic, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addADTToScope(RType adtName, bool isPublic, loc l, STBuilder stBuilder) {
	return addADTToScopeAt(adtName, isPublic, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addADTToTopScope(RType adtName, bool isPublic, loc l, STBuilder stBuilder) {
	return addADTToScopeAt(adtName, isPublic, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add a constructor into the given scope.
//
public AddedItemPair addConstructorToScopeAt(RName constructorName, list[RNamedType] constructorArgs, ItemId adtItem, bool isPublic, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Constructor(constructorName, constructorArgs, adtItem, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addConstructorToScope(RName constructorName, list[RNamedType] constructorArgs, ItemId adtItem, bool isPublic, loc l, STBuilder stBuilder) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addConstructorToTopScope(RName constructorName, list[RNamedType] constructorArgs, ItemId adtItem, bool isPublic, loc l, STBuilder stBuilder) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add an annotation into the given scope
//
public AddedItemPair addAnnotationToScopeAt(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Annotation(annotationName, annotationType, onType, isPublic, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addAnnotationToScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, STBuilder stBuilder) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addAnnotationToTopScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, STBuilder stBuilder) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add a rule into the given scope
//
public AddedItemPair addRuleToScopeAt(RName ruleName, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Rule(ruleName, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addRuleToScope(RName ruleName, loc l, STBuilder stBuilder) {
	return addRuleToScopeAt(ruleName, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addRuleToTopScope(RName ruleName, loc l, STBuilder stBuilder) {
	return addRuleToScopeAt(ruleName, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Add a label into the given scope.
//
public AddedItemPair addLabelToScopeAt(RName labelName, loc l, STBuilder stBuilder, ItemId scopeToUse) {
	return addItem(Label(labelName, scopeToUse, l), scopeToUse, l, stBuilder, true);
}

public AddedItemPair addLabelToScope(RName labelName, loc l, STBuilder stBuilder) {
	return addLabelToScopeAt(labelName, l, stBuilder, head(stBuilder.scopeStack));
}

public AddedItemPair addLabelToTopScope(RName labelName, loc l, STBuilder stBuilder) {
    return addLabelToScopeAt(labelName, l, stBuilder, last(stBuilder.scopeStack));
}

//
// Indicate if the current scope is a boolean scope or an or scope -- used to prevent us from adding
// additional layers of scoping where they are not needed.
// 
public bool inBoolLayer(STBuilder stBuilder) {
	return (BooleanExpScope(_,_) := stBuilder.scopeItemMap[head(stBuilder.scopeStack)] || OrScope(_,_) := stBuilder.scopeItemMap[head(stBuilder.scopeStack)]);
}

//
// Indicate which function is being returned from by the return statement at location l
//
public STBuilder markReturnFunction(ItemId id, loc l, STBuilder stBuilder) {
    return stBuilder[returnMap = stBuilder.returnMap + ( l : id )];
}

//
// Just return the symbol table when we don't care about the ID in the pair
//
public STBuilder justSTBuilder(aip) = aip[0];
