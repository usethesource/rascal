@bootstrapParser
module rascal::scoping::SymbolTable

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
import rascal::checker::ListUtils;
import rascal::syntax::RascalRascal;

//
// Rascal Namespaces. Rascal has separate namespaces for modules, labels,
// FCVs (functions, constructors, and variables), types, type variables,
// annotations, rules, and tags.
//
data Namespace = Modules() | Labels() | FCVs() | Types() | TypeVars() | Annotations() | Rules() | Tags() ;

//
// Each item in the symbol table is given a unique ID.
//
alias STItemId = int;

//
// The symbol table is made up of a mix of layers and items. The layers
// represent scopes in the symbol table, while the items represent actual
// named entities (functions, variables, etc).
//
// Why IDs? This mainly simplifies the computation -- there is no need
// to form an explicit n-ary tree, but instead the tree can be encoded
// using IDs. Storing these IDs then provides a direct link back into
// that location in the scope tree.
//
// TODO: Add something for TagItem...
//
data STItem =
      BlockLayer(STItemId parentId)
    | BooleanExpLayer(STItemId parentId)
    | FunctionLayer(STItemId itemId, STItemId parentId)
	| ModuleLayer(STItemId itemId, STItemId parentId)
    | OrLayer(STItemId parentId)
	| PatternMatchLayer(STItemId parentId)
    | TopLayer()

    | ADTItem(RType adtType, bool isPublic, STItemId parentId) 
    | AliasItem(RType aliasType, bool isPublic, STItemId parentId)
    | AnnotationItem(RName annotationName, RType annoType, RType onType, bool isPublic, STItemId parentId) 
    | ConstructorItem(RName constructorName, list[RNamedType] constructorArgs, STItemId adtParentId, STItemId parentId)
    | FunctionItem(RName functionName, RType returnType, Parameters params, list[RType] throwsTypes, bool isPublic, bool isVarArgs, STItemId parentId)
    | FormalParameterItem(RName parameterName, RType parameterType, STItemId parentId)
    | LabelItem(RName labelName, STItemId parentId)
    | ModuleItem(RName moduleName, STItemId parentId)
    | RuleItem(RName ruleName, STItemId parentId)
    | TypeVariableItem(RType typeVar, STItemId parentId)
	| VariableItem(RName variableName, RType variableType, STItemId parentId)
	| TagItem(STItemId parentId)
;

//
// Identify which scope items are included in which namespaces
//
private rel[Namespace, str] namespaceItems = { <Modules(), "ModuleItem">, <Labels(), "LabelItem">, <FCVs(), "ConstructorItem">,
                                               <FCVs(), "FunctionItem">, <FCVs(), "FormalParameterItem">, <FCVs(), "VariableItem">,
                                               <Types(), "ADTItem">, <Types(), "AliasItem">, <TypeVars(), "TypeVariableItem">,
                                               <Annotations(), "AnnotationItem">, <Rules(), "RuleItem">, <Tags(), "TagItem"> };
//
// Named scope items.
//
private set[str] namedItems = { "ModuleItem", "FunctionItem", "VariableItem", "FormalParameterItem", "LabelItem", "AliasItem", 
                                "ConstructorItem", "ADTItem", "AnnotationItem", "RuleItem", "TypeVariableItem" };

//
// Indicate if the item has a name (like a variable)
//
public bool itemHasName(STItem item) {
    return getName(item) in namedItems;
}

//
// If the item has a syntactical name, retrieve it.
//
public RName getItemName(STItem item) {
    switch(getName(item)) {
        case "ModuleItem" : return item.moduleName;
        case "FunctionItem" : return item.functionName;
        case "VariableItem" : return item.variableName;
        case "FormalParameterItem" : return item.parameterName;
        case "LabelItem" : return item.labelName;
        case "AliasItem" : return item.aliasType.aliasName;
        case "ConstructorItem" : return item.constructorName;
        case "ADTItem" : return item.adtType.adtName;
        case "AnnotationItem" : return item.annotationName;
        case "RuleItem" : return item.ruleName;
        case "TypeVariableItem" : return item.typeVar.varName;
		default : throw "Item does not have a name, use itemHasHame(STItem item) to check first to ensure the item has a name";
	}
}

//
// Is this a symbol table layer (true) or an actual symbol table entry (false)?
//
// NOTE: We could instead do a regexp against Layer, but that seems like too much
// of a hack...
//
public bool isLayer(STItem si) {
    return getName(si) in { "TopLayer", "ModuleLayer", "FunctionLayer", "PatternMatchLayer", "BooleanExpLayer", "OrLayer", "BlockLayer" };
}

//
// Is this a symbol table item (true) or a scoping layer (false)?
//
public bool isItem(STItem si) {
	return !isLayer(si);
}

//
// Several functions to check the type of item
//				
public bool isFunctionItem(STItem si) {
	return getName(si) == "FunctionItem";
}

public bool isConstructorItem(STItem si) {
	return getName(si) == "ConstructorItem";
}

public bool isFunctionOrConstructorItem(STItem si) {
	return isFunctionItem(si) || isConstructorItem(si);
}

//
// Symbol table items can have an associated location
//
anno loc STItem@at;

anno set[loc] STItem@ats;

// The data structure used to represent the symbol table.
//
// topSTItemId: the symbol table item ID for the top of the symbol table tree
// scopeRel: relates the scope layer (scopeId) with the individual ST items (itemId)
// itemUses: given a location, give the symbol table items that may be used there
// scopeItemMap: maps the symbol table item identifier to the actual ST item
// itemLocations: maps a location to the item defined in that location
// currentScope: the identifier of the current scope layer
// freshType: a counter which allows generation of "fresh" types, used by the local inferencer
// scopeErrorMap: maps locations to errors detected at tht location
// inferredTypeMap: map the id used in the fresh type to the type determined via inference
// returnMap: map of return locations to the function item this return is associated with
// itBinder: used for typing of the "it" construct in reducers: keeps track of the value of "it"
//           inside the proper scope
// scopeStack: a stack of scope layers, allows entering and leaving scopes by pushing and popping
// adtItems: map from the ADT name to the related ADT and constructor symbol table items

alias ScopeRel = rel[STItemId scopeId, STItemId itemId];
alias ScopeNamesRel = rel[STItemId scopeId, RName itemName, STItemId itemId];
alias ItemUses = map[loc useLoc, set[STItemId] usedItems];
alias STItemMap = map[STItemId,STItem];
alias ItemLocationRel = rel[loc,STItemId];
alias ItemTypeMap = map[int, RType];

alias SymbolTable = tuple[
    STItemId topSTItemId,
    STItemId currentModule,	
    ScopeRel scopeRel,
    ScopeNamesRel scopeNames,
    ItemUses itemUses, 
    STItemId nextScopeId, 
    STItemMap scopeItemMap, 
    ItemLocationRel itemLocations, 
    STItemId currentScope, 
    int freshType,
    map[loc, set[str]] scopeErrorMap, 
    ItemTypeMap inferredTypeMap,
    ItemTypeMap typeVarMap, 
    map[loc, STItemId] returnMap,
    map[loc, RType] itBinder, 
    list[STItemId] scopeStack, 
    map[RName adtName,tuple[set[STItemId] adtItems,set[STItemId] consItems] adtInfo] adtMap,
    list[RType] functionReturnStack,
    list[Tree] visitSubjectStack,
    list[Tree] switchSubjectStack
];

//
// These types are used in functions that add/update the scope information,
// since we need to return multiple items -- the new table, plus information
// about what was added/updated.
//
alias AddedItemPair = tuple[SymbolTable symbolTable, STItemId addedId];
alias ScopeUpdatePair = tuple[SymbolTable symbolTable, STItemId oldScopeId];
  
//                      
// Create an empty symbol table
//                        
public SymbolTable createNewSymbolTable() {
	return < -1, -1, { }, { }, ( ), 0, ( ), { }, 0, 0, (), (), (), (), (), [ ], ( ), [], [], []>;
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
public SymbolTable mergeOrLayers(SymbolTable symbolTable, list[STItemId] orLayers, STItemId intoLayer) {
    // First, get back the items introduced in the first or layer in the list
    set[STItemId] introducedItems = { vi | vi <- symbolTable.scopeRel[head(orLayers)], VariableItem(_,_,_) := symbolTable.scopeItemMap[vi] };
	
    // Now, go through the rest of the layers -- we only keep vars found in all the layers with the same name.
    // Note that we don't restrict this based on type here, since we may not know the type yet -- this
    // is instead a decision for the type checker.
    for (oritem <- tail(orLayers)) {
        set[STItemId] sharedItems = { };
        for (li <- introducedItems, VariableItem(vn,_,_) := symbolTable.scopeItemMap[li],
            ri <- symbolTable.scopeRel[oritem], VariableItem(vn,_,_) := symbolTable.scopeItemMap[ri]) {
            sharedItems += li;
        }
        introducedItems = sharedItems;
    }

	// Finally, inject them into the intoLayer
    symbolTable = pushScope(intoLayer, symbolTable);
    for (oritem <- introducedItems, VariableItem(vn,vt,_) := symbolTable.scopeItemMap[oritem]) {
        symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(vn, vt, false, symbolTable.scopeItemMap[oritem]@at, symbolTable),[<true,symbolTable.scopeItemMap[oritem]@at>]));
    }
    symbolTable = popScope(symbolTable);

    return symbolTable;
}

//
// Indicate that scopeItem is used at location l
//
public SymbolTable addItemUse(SymbolTable symbolTable, STItemId scopeItem, loc l) {
    if (l in symbolTable.itemUses)
        symbolTable.itemUses[l] = symbolTable.itemUses[l] + scopeItem;
    else
        symbolTable.itemUses += (l : { scopeItem });
    return symbolTable;
}

//
// Indicate that scopeItems are used at location l
//
public SymbolTable addItemUses(SymbolTable symbolTable, set[STItemId] scopeItems, loc l) {
    if (l in symbolTable.itemUses)
        symbolTable.itemUses[l] += scopeItems;
    else
        symbolTable.itemUses += (l : scopeItems );
    return symbolTable;
}

//
// Add a scope error with message msg at location l
//
public SymbolTable addScopeError(SymbolTable symbolTable, loc l, str msg) {
    if (l in symbolTable.scopeErrorMap)
        symbolTable.scopeErrorMap[l] = symbolTable.scopeErrorMap[l] + msg;
    else
        symbolTable.scopeErrorMap += (l : { msg } );
    return symbolTable;
}

//
// Add a new layer WITHOUT a parent into the scope. These are layers that are not
// nested inside other layers, such as the top layer.
//
public AddedItemPair addScopeLayer(STItem si, loc l, SymbolTable symbolTable) {
    int newItemId = symbolTable.nextScopeId;
    STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
    ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
    symbolTable = ((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel];
    return <symbolTable,newItemId>;				
}

//
// Add a new layer WITH a parent into the scope. These are layers that are nested
// inside other layers, such as the module layers (inside the top layer), function
// layers (inside modules or other functions), etc. Note that layers do not
// have names, only normal items, so we do not need to update the scope names
// relation here.
//
public AddedItemPair addScopeLayerWithParent(STItem si, STItemId parentId, loc l, SymbolTable symbolTable) {
    int newItemId = symbolTable.nextScopeId;
    ScopeRel newScopeRel = symbolTable.scopeRel + <parentId, symbolTable.nextScopeId>;
    STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
    ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
    symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
    return <symbolTable,newItemId>;				
}

//
// Add a new symbol table item within a scope layer.
//
public AddedItemPair addSTItemWithParent(STItem si, STItemId parentId, loc l, SymbolTable symbolTable) {
    int newItemId = symbolTable.nextScopeId;
    ScopeRel newScopeRel = symbolTable.scopeRel + <parentId, symbolTable.nextScopeId>;
    STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
    ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
    if (itemHasName(si)) {
        ScopeNamesRel newScopeNamesRel = { };
        // For functions and modules, we add the name to the parent layer, since (at least in the case of functions)
        // we want to make sure that the name is only visible at the actual defining level of scope. If we define
        // it inside the function layer we block calls to overloads with the same name, since we will always find
        // this one first and won't continue up the symbol table hierarchy looking for others.
        if (FunctionItem(n,_,_,_,_,_,_) := si || ModuleItem(n,_) := si) {
            newScopeNamesRel = symbolTable.scopeNames + < symbolTable.scopeItemMap[parentId].parentId, n, symbolTable.nextScopeId>;
        } else {
            newScopeNamesRel = symbolTable.scopeNames + <parentId, getItemName(si), symbolTable.nextScopeId>;
        }
        symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel][scopeNames = newScopeNamesRel];
    } else {
        symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
    }
    return <symbolTable,newItemId>;				
}

//
// Get back the layer defined at location l
//
public STItemId getLayerAtLocation(loc l, SymbolTable symbolTable) {
    if (l in domain(symbolTable.itemLocations)) {
        set[STItemId] layers = { si | si <- symbolTable.itemLocations[l], isLayer(symbolTable.scopeItemMap[si]) };
        if (size(layers) == 1)
            return getOneFrom(layers);
        else if (size(layers) == 0)
            throw "getLayerAtLocation: Error, trying to retrieve layer item from location <l> with no associated layer.";  		
        else {
            throw "getLayerAtLocation: Error, trying to retrieve layer item from location <l> with more than 1 associated layer.";
        }	
    } else {
        throw "getLayerAtLocation: Error, trying to retrieve item from unassociated location <l>.";
    }
}

//
// Update the item stored at ID idToUpdate
//
public SymbolTable updateSTItem(STItem si, STItemId idToUpdate, SymbolTable symbolTable) {
    return symbolTable[scopeItemMap = symbolTable.scopeItemMap + (idToUpdate : si)];				
}

//
// Get the item stored at ID id
//
public STItem getSTItem(STItemId id, SymbolTable symbolTable) {
    return symbolTable.scopeItemMap[id];
}
	
//
// Pretty printers for scope information
//
public str prettyPrintSI(SymbolTable st, STItem si) {
    str prettyPrintSIAux(STItem si) {
        switch(si) {
            case TopLayer() : return "TopLayer";
            case ModuleLayer(_,_) : return "ModuleLayer";
            case FunctionLayer(_,_) : return "FunctionLayer";
            case PatternMatchLayer(_) : return "PatternMatchLayer";
            case BooleanExpLayer(_) : return "BooleanExpLayer";
            case OrLayer(_) : return "OrLayer";
            case BlockLayer(_) : return "BlockLayer";
            case ModuleItem(x,_) : return "Module <prettyPrintName(x)>";
            case FunctionItem(x,t,ags,_,_,_,_) : return "Function <prettyPrintType(t)> <prettyPrintName(x)>(<ags>)";
            case VariableItem(x,t,_) : return "Variable <prettyPrintType(t)> <prettyPrintName(x)>";
            case TypeVariableItem(t,_) : return "Type Variable <prettyPrintType(t)>";
            case FormalParameterItem(x,t,_) : return "Formal Parameter <prettyPrintType(t)> <prettyPrintName(x)>";
            case LabelItem(x,_) : return "Label <prettyPrintName(x)>";
            case AliasItem(atype,_,_) : return "Alias <prettyPrintType(atype)>";
            case ConstructorItem(cn,tas,_,_) : 	return "Constructor <prettyPrintName(cn)>(<prettyPrintNamedTypeList(tas)>)";
            case ADTItem(ut,_,_) : return "ADT <prettyPrintType(ut)>";
            case AnnotationItem(x,atyp,otyp,_,_) : return "Annotation <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)>";
            case RuleItem(x,_) : return "Rule <prettyPrintName(x)>";
        }
    }
    
    return prettyPrintSIAux(si);
}

public str prettyPrintSIWLoc(SymbolTable st, STItem si) {
    str prettyPrintSIWLocAux(STItem si) {
        if ( (si@at)? ) {
            switch(si) {
                case TopLayer() : return "TopLayer at <si@at>";
                case ModuleLayer(_,_) : return "ModuleLayer at <si@at>";
                case FunctionLayer(_,_) : return "FunctionLayer at <si@at>";
                case PatternMatchLayer(_) : return "PatternMatchLayer at <si@at>";
                case BooleanExpLayer(_) : return "BooleanExpLayer at <si@at>";
                case OrLayer(_) : return "OrLayer at <si@at>";
                case BlockLayer(_) : return "BlockLayer at <si@at>";
                case ModuleItem(x,_) : return "Module <prettyPrintName(x)> at <si@at>";
                case FunctionItem(x,t,ags,_,_,_,_) : return "Function <prettyPrintType(t)> <prettyPrintName(x)>(<ags>)";
                case VariableItem(x,t,_) : return "Variable <prettyPrintType(t)> <prettyPrintName(x)> at <si@at>";
                case TypeVariableItem(t,_) : return "Type Variable <prettyPrintType(t)> at <si@at>";
                case FormalParameterItem(x,t,_) : return "Formal Parameter <prettyPrintType(t)> <prettyPrintName(x)> at <si@at>";
                case LabelItem(x,_) : return "Label <prettyPrintName(x)> at <si@at>";
                case AliasItem(atype,_,_) : return "Alias <prettyPrintType(atype)> at <si@at>";
                case ConstructorItem(cn,tas,_,_) :  return "Constructor <prettyPrintName(cn)>(<prettyPrintNamedTypeList(tas)>) at <si@at>";
                case ADTItem(ut,_,_) : return "ADT <prettyPrintType(ut)> at <si@at>";
                case AnnotationItem(x,atyp,otyp,_,_) : return "Annotation <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)> at <si@at>";
                case RuleItem(x,_) : return "Rule <prettyPrintName(x)> at <si@at>";
            }
        } else {
            println(si);
            return prettyPrintSI(st,si);
        }
    }
    
    return prettyPrintSIWLocAux(si);
}

//
// Filter a set of items to only include those in the given namespace
//
private set[STItemId] filterNamesForNamespace(SymbolTable symbolTable, set[STItemId] scopeItems, Namespace namespace) {
    return { i | i <- scopeItems, getName(symbolTable.scopeItemMap[i]) in namespaceItems[namespace] };
}

//
// Get the ID for the surrounding module layer, if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inModule, STItemId moduleId] getSurroundingModule(SymbolTable symbolTable, STItemId currentScopeId) {
    if (ModuleLayer(_,_) := symbolTable.scopeItemMap[currentScopeId]) {
        return < true, currentScopeId >;
    } else if (TopLayer() := symbolTable.scopeItemMap[currentScopeId]) {
        return < false, -1 >;
    } else {
        return getSurroundingModule(symbolTable,symbolTable.scopeItemMap[currentScopeId].parentId);
    }
}

//
// Get the ID for the surrounding function layer, if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inFunction, STItemId functionId] getSurroundingFunction(SymbolTable symbolTable, STItemId currentScopeId) {
    if (FunctionLayer(_,_) := symbolTable.scopeItemMap[currentScopeId]) {
        return < true, currentScopeId >;
    } else if (TopLayer() := symbolTable.scopeItemMap[currentScopeId]) {
        return < false, -1 >;
    } else {
        return getSurroundingFunction(symbolTable,symbolTable.scopeItemMap[currentScopeId].parentId);
    }
}

//
// Get the ID for the outermost surrounding function layer (think nested functions), if one is present.
//
// TODO: May want to provide a nicer interface to this...
//
public tuple[bool inFunction, STItemId functionId] getOutermostFunction(SymbolTable symbolTable, STItemId currentScopeId) {
    <inFunction, functionId> = getSurroundingFunction(symbolTable, currentScopeId);
    if (inFunction) {
        <inFunction2,functionId2> = getSurroundingFunction(symbolTable,symbolTable.scopeItemMap[functionId].parentId);
        while(inFunction2) {
            inFunction = inFunction2;
            functionId = functionId2;
            <inFunction2,functionId2> = getSurroundingFunction(symbolTable,symbolTable.scopeItemMap[functionId].parentId);
        }
    }
    return <inFunction, functionId>;
}

//
// Get items of name x defined in the the current module or, if no names are found in the module, in
// the top layer.
//
private set[STItemId] getModuleAndTopItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    set[STItemId] foundItems = { };
    < inModule, moduleId > = getSurroundingModule(symbolTable,currentScopeId);
    if (inModule)
        foundItems = filterNamesForNamespace(symbolTable, symbolTable.scopeNames[currentScopeId,x], ns);
    if (size(foundItems) == 0)
        foundItems = filterNamesForNamespace(symbolTable, symbolTable.scopeNames[symbolTable.topSTItemId,x], ns);
    return foundItems;    
}

//
// The same as above, except this only checks in the current module.
//
private set[STItemId] getModuleItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    set[STItemId] foundItems = { };
    < inModule, moduleId > = getSurroundingModule(symbolTable,currentScopeId);
    if (inModule)
        foundItems = filterNamesForNamespace(symbolTable, symbolTable.scopeNames[currentScopeId,x], ns);
    return foundItems;    
}

//
// The same as above, except this only checks in the top layer.
//
private set[STItemId] getTopItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    return filterNamesForNamespace(symbolTable, symbolTable.scopeNames[symbolTable.topSTItemId,x], ns);
}

//
// Get items, stopping at the given bound. Note that this assumes that the bound is above the current
// scope ID, we do not verify that here.
//
// TODO: Put in logic to stop at the top
//
private set[STItemId] getBoundedItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns, STItemId boundingId) {
    set[STItemId] foundItems = filterNamesForNamespace(symbolTable, symbolTable.scopeNames[currentScopeId,x], ns);
    if (size(foundItems) == 0) {
        if (currentScopeId != boundingId) {
            foundItems = getBoundedItems(symbolTable, symbolTable.scopeItemMap[currentScopeId].parentId, x, ns, boundingId);
        }
    }
    return foundItems;
}

//
// Get items of name x defined in the current function, starting at the current scope layer and working
// up to the function layer the current scope is nested within.
//
private set[STItemId] getCurrentFunctionItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    set[STItemId] foundItems = { };
    < inFunction, functionId > = getSurroundingFunction(symbolTable,currentScopeId);
    if (inFunction) {
        foundItems = getBoundedItems(symbolTable, currentScopeId, x, ns, functionId);
    }
    return foundItems;    
}

//
// Get items of name x defined in the current function, starting at the current scope layer and working
// up to the outermost function layer the current scope is nested within.
//
private set[STItemId] getNestedFunctionItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    set[STItemId] foundItems = { };
    < inFunction, functionId > = getOutermostFunction(symbolTable,currentScopeId);
    if (inFunction) {
        foundItems = getBoundedItems(symbolTable, currentScopeId, x, ns, functionId);
    }
    return foundItems;    
}

//
// Get items of name x defined between the current scope and the top.
//
private set[STItemId] getAllItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    return getBoundedItems(symbolTable, currentScopeId, x, ns, symbolTable.topSTItemId);
}

//
// Look up names associated with modules. Modules are always created at the top layer, since
// we do not (currently) support nested modules.
//
// TODO: If we ever support nested modules, this will need to be changed.
//
private set[STItemId] getModuleItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getTopItems(symbolTable,currentScopeId,x,Modules());
}

//
// Look up names associated with labels. Labels are only visible within the current function.
//
// TODO: What if we are not inside a function, but are instead at the module level when we
// encounter the label? We probably want to limit scope to just the current statement instead.
//
private set[STItemId] getLabelItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getCurrentFunctionItems(symbolTable,currentScopeId,x,Labels());
}

//
// Look up names associated with variables, constructors, and functions. Since these names can
// shadow one another, start the lookup at the current level and continue to the top scope.
//
private set[STItemId] getFCVItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getAllItems(symbolTable, currentScopeId, x, FCVs());
}

//
// The same as above, but only lookup to the top of the current function scope (assuming
// we are inside a function...)
//
private set[STItemId] getFCVItemsForConflicts(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getNestedFunctionItems(symbolTable, currentScopeId, x, FCVs());
}

//
// Look up names associated with types (aliases, ADTs). These are always visible either in the
// current module or at the top level (for imported names).
//
private set[STItemId] getTypeItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getModuleAndTopItems(symbolTable,currentScopeId,x,Types());
}

//
// Look up names associated with type variables. If one is inside a function, we find the first
// definition inside a function (either the current function or a parent function).
//
// TODO: What if we are not inside a function, but are instead at the module level when we
// encounter the label? We probably want to limit scope to just the current statement instead.
//
private set[STItemId] getTypeVarItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getNestedFunctionItems(symbolTable,currentScopeId,x,TypeVars());
}

//
// Look up names associated with annotations. These are always visible either in the
// current module or at the top level (for imported names).
//
private set[STItemId] getAnnotationItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getModuleAndTopItems(symbolTable,currentScopeId,x,Annotations());
}

//
// Look up names associated with rules. These are always visible either in the
// current module or at the top level (for imported names).
//
private set[STItemId] getRuleItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getModuleAndTopItems(symbolTable,currentScopeId,x,Rules());
}

//
// Look up names associated with tags. These are always visible either in the
// current module or at the top level (for imported names).
//
private set[STItemId] getTagItems(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
    return getModuleAndTopItems(symbolTable,currentScopeId,x,Tags());
}

private map[Namespace, set[STItemId] (SymbolTable, STItemId, RName)] lookupFunctions = (
    Modules() : getModuleItems,
    Labels() : getLabelItems,
    FCVs() : getFCVItems,
    Types() : getTypeItems,
    TypeVars() : getTypeVarItems,
    Annotations() : getAnnotationItems,
    Rules() : getRuleItems,
    Tags() : getTagItems );

private map[Namespace, set[STItemId] (SymbolTable, STItemId, RName)] lookupFunctionsForConflicts = (
    Modules() : getModuleItems,
    Labels() : getLabelItems,
    FCVs() : getFCVItemsForConflicts,
    Types() : getTypeItems,
    TypeVars() : getTypeVarItems,
    Annotations() : getAnnotationItems,
    Rules() : getRuleItems,
    Tags() : getTagItems );
    
//
// Get back items of a given name from the symbol table. This handles dispatch to the correct
// lookup function (encoded in the map above), plus it handles lookups in qualified names.
//
private set[STItemId] getItemsGeneral(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns, map[Namespace, set[STItemId] (SymbolTable, STItemId, RName)] lookupFuns) {
    // We look up modules by their full name. So, if we aren't looking up modules, and we have a compound
    // name, switch to the correct context, else just do a normal lookup.
    if (Modules() !:= ns && RCompoundName(nl) := x) {
        // The module name is the qualified name, minus the last part of the name. 
        RName moduleName = (size(nl) == 2) ? RSimpleName(nl[0]) : RCompoundName(head(nl,size(nl)-1)); 
		x = RSimpleName(nl[size(nl)-1]);
		
		// Get the module item representing the given module name, it should be in scope.
		set[STItemId] mods = getModuleItems(symbolTable, currentScopeId, moduleName);
		
		// If we found the module, do the lookup in that context. It should be the case that
		// we have just one match; if we do not, then we have an error, which will show up
		// as a scope error saying the name cannot be found.
		//
		// TODO: Once we have parameterized modules working, we need to decide how to do this,
		// a simple lookup based on the module name may no longer by valid.
		if (size(mods) == 1) {
			return getItemsGeneral(symbolTable, symbolTable.scopeItemMap[getOneFrom(mods)].parentId, x, ns, lookupFuns);
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
    return (lookupFuns[ns])(symbolTable,currentScopeId,x);
}

public set[STItemId] getItems(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    return getItemsGeneral(symbolTable, currentScopeId, x, ns, lookupFunctions);
}    

public set[STItemId] getItemsForConflicts(SymbolTable symbolTable, STItemId currentScopeId, RName x, Namespace ns) {
    return getItemsGeneral(symbolTable, currentScopeId, x, ns, lookupFunctionsForConflicts);
}    

///////////////////////////////////////////////////////////////////////////////////////////
//
// Functions to push new scope layers and add scope items; also performs
// some error checking.
//
///////////////////////////////////////////////////////////////////////////////////////////

//
// A word of explanation: the functions that add new items into the scope
// return a ResultTuple, which includes both the updated symbol table and
// a list of the items that were added. This allows additional information
// to be added using these items. Note that the receiver of this information
// either knows the order in which items are returned (for instance, knows
// the first is a function layer, the second is the function item, etc) or
// has to find this out.
//

alias ResultTuple = tuple[SymbolTable symbolTable, list[STItemId] addedItems];

public ResultTuple pushNewTopScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aipTop = addScopeLayer(TopLayer()[@at=l], l, symbolTable);
	symbolTable = aipTop.symbolTable[topSTItemId = aipTop.addedId];
	symbolTable.scopeStack = [ aipTop.addedId ] + symbolTable.scopeStack;
	symbolTable.currentScope = aipTop.addedId;
	return <symbolTable,[aipTop.addedId]>; 	
}

public ResultTuple pushNewModuleScope(RName moduleName, loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayerWithParent(ModuleLayer(-1, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	AddedItemPair aip2 = addSTItemWithParent(ModuleItem(moduleName, aip.symbolTable.currentScope)[@at=l], aip.symbolTable.currentScope, l, aip.symbolTable);
	aip2.symbolTable.scopeItemMap[aip.addedId].itemId = aip2.addedId;
	return <aip2.symbolTable,[aip.addedId,aip2.addedId]>; 	
}

public ResultTuple pushNewBooleanScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayerWithParent(BooleanExpLayer(symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewOrScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayerWithParent(OrLayer(symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewBlockScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayerWithParent(BlockLayer(symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewPatternMatchScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayerWithParent(PatternMatchLayer(symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public SymbolTable popScope(SymbolTable symbolTable) {
	if (size(symbolTable.scopeStack) == 0) throw "popScope: Scope Stack is empty, cannot pop!";
	symbolTable.scopeStack = tail(symbolTable.scopeStack);
	symbolTable.currentScope = head(symbolTable.scopeStack);
	return symbolTable;
}

public SymbolTable pushScope(STItemId newScope, SymbolTable symbolTable) {
	symbolTable.scopeStack = [ newScope ] + symbolTable.scopeStack;
	symbolTable.currentScope = newScope;
	return symbolTable;
}

public ResultTuple pushNewFunctionScopeAt(bool hasAnonymousName, RName functionName, RType retType, Parameters ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(FunctionLayer(-1, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	symbolTable = aipLayer.symbolTable;
	symbolTable.scopeStack = [ aipLayer.addedId ] + symbolTable.scopeStack;
	symbolTable.currentScope = aipLayer.addedId;
    
    // Add the actual function item associated with the scope layer
    AddedItemPair aipItem = addSTItemWithParent(FunctionItem(functionName, retType, ps, throwsTypes, isPublic, isVarArgs, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
    aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;
    if (hasAnonymousName) aipItem.symbolTable.scopeItemMap[aipItem.addedId].functionName = "@ANONYMOUS_FUNCTION_<aipItem.addedId>";
    
    return <aipItem.symbolTable,[aipLayer.addedId, aipItem.addedId]>;
}

public ResultTuple pushNewFunctionScope(RName functionName, RType retType, Parameters ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, SymbolTable symbolTable) {
	return pushNewFunctionScopeAt(false, functionName, retType, ps, throwsTypes, isPublic, isVarArgs, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple pushNewFunctionScopeAtTop(RName functionName, RType retType, Parameters ps, list[RType] throwsTypes, bool isPublic, bool isVarArgs, loc l, SymbolTable symbolTable) {
	return pushNewFunctionScopeAt(false, functionName, retType, ps, throwsTypes, isPublic, isVarArgs, l, symbolTable, symbolTable.topSTItemId);
} 

public ResultTuple pushNewClosureScope(RType retType, Parameters ps, loc l, SymbolTable symbolTable) {
    return pushNewFunctionScopeAt(true, "", retType, ps, [], false, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple pushNewVoidClosureScope(Parameters ps, loc l, SymbolTable symbolTable) {
    return pushNewFunctionScopeAt(true, "", makeVoidType(), ps, [], false, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addAliasToScopeAt(RType aliasType, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(AliasItem(aliasType, isPublic, scopeToUse), scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addAliasToScope(RType aliasType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAliasToScopeAt(aliasType, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addAliasToTopScope(RType aliasType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAliasToScopeAt(aliasType, isPublic, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addVariableToScopeAt(RName varName, RType varType, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(VariableItem(varName, varType, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addVariableToScope(RName varName, RType varType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addVariableToScopeAt(varName, varType, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addVariableToTopScope(RName varName, RType varType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addVariableToScopeAt(varName, varType, isPublic, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addTypeVariableToScopeAt(RType varType, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(TypeVariableItem(varType, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addTypeVariableToScope(RType varType, loc l, SymbolTable symbolTable) {
	return addTypeVariableToScopeAt(varType, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addTypeVariableToTopScope(RType varType, loc l, SymbolTable symbolTable) {
	return addTypeVariableToScopeAt(varType, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addADTToScopeAt(RType adtName, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(ADTItem(adtName, isPublic, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
	return <symbolTable, []>;
}

public ResultTuple addADTToScope(RType adtName, bool isPublic, loc l, SymbolTable symbolTable) {
	return addADTToScopeAt(adtName, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addADTToTopScope(RType adtName, bool isPublic, loc l, SymbolTable symbolTable) {
	return addADTToScopeAt(adtName, isPublic, l, symbolTable, symbolTable.topSTItemId);
}

// TODO: Need to get ADT that goes with the adtType and associate this constructor with it, as well as adding this constructor
// ID to the list maintained as part of the ADT.
public ResultTuple addConstructorToScopeAt(RName constructorName, list[RNamedType] constructorArgs, STItemId adtItem, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(ConstructorItem(constructorName, constructorArgs, adtItem, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addConstructorToScope(RName constructorName, list[RNamedType] constructorArgs, STItemId adtItem, bool isPublic, loc l, SymbolTable symbolTable) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addConstructorToTopScope(RName constructorName, list[RNamedType] constructorArgs, STItemId adtItem, bool isPublic, loc l, SymbolTable symbolTable) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addAnnotationToScopeAt(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(AnnotationItem(annotationName, annotationType, onType, isPublic, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addAnnotationToScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addAnnotationToTopScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addRuleToScopeAt(RName ruleName, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(RuleItem(ruleName, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addRuleToScope(RName ruleName, loc l, SymbolTable symbolTable) {
	return addRuleToScopeAt(ruleName, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addRuleToTopScope(RName ruleName, loc l, SymbolTable symbolTable) {
	return addRuleToScopeAt(ruleName, l, symbolTable, symbolTable.topSTItemId);
}

public ResultTuple addLabelToScopeAt(RName labelName, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(LabelItem(labelName, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addLabelToScope(RName labelName, loc l, SymbolTable symbolTable) {
	return addLabelToScopeAt(labelName, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addLabelToTopScope(RName labelName, loc l, SymbolTable symbolTable) {
    return addLabelToScopeAt(labelName, l, symbolTable, symbolTable.topSTItemId);
}

// Projectors/combinators to work with result tuples
public SymbolTable justSymbolTable(ResultTuple result) {
	return result.symbolTable;
}

public ResultTuple setCurrentModule(ResultTuple result) {
    SymbolTable symbolTable = result.symbolTable;
	symbolTable.currentModule = result.addedItems[0];
	return <symbolTable, result.addedItems>;
}

public ResultTuple addSTItemUses(ResultTuple result, list[tuple[bool flagUse, loc useloc]] useLocs) {
	SymbolTable symbolTable = result.symbolTable;
	for (n <- [0..size(useLocs)-1]) if (useLocs[n].flagUse) symbolTable = addItemUse(symbolTable, result.addedItems[n], useLocs[n].useloc);
	return <symbolTable, result.addedItems>;
}

public bool inBoolLayer(SymbolTable symbolTable) {
	if (BooleanExpLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope] || OrLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope])
		return true;
	return false;
}



public SymbolTable markReturnFunction(STItemId id, loc l, SymbolTable symbolTable) {
    return symbolTable[returnMap = symbolTable.returnMap + ( l : id )];
}
