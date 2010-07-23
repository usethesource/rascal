module rascal::checker::SymbolTable

import rascal::checker::Types;
import rascal::checker::Signature;
import rascal::checker::SubTypes;
import rascal::\old-syntax::Rascal;

import List;
import IO;
import Set;
import Relation;
import Map;

// Unique identifiers for scope items
alias STItemId = int;

// Items representing identifiable parts of scope, including scope layers (modules, 
// functions, blocks) and scope items (functions, variables, formal parameters, labels),
// that will go into the symbol table.
data STItem =
      TopLayer()
	| ModuleLayer(STItemId itemId, STItemId parentId)
	| FunctionLayer(STItemId itemId, STItemId parentId)
	| PatternMatchLayer(STItemId parentId)
	| BooleanExpLayer(STItemId parentId)
	| OrLayer(STItemId parentId)
	| ClosureLayer(STItemId itemId, STItemId parentId)
	| VoidClosureLayer(STItemId itemId, STItemId parentId)
	| BlockLayer(STItemId parentId)

	| ModuleItem(RName moduleName, STItemId parentId)
	| FunctionItem(RName functionName, RType returnType, list[STItemId] parameters, list[RType] throwsTypes, bool isPublic, STItemId parentId)
	| ClosureItem(RType returnType, list[STItemId] parameters, STItemId parentId)
	| VoidClosureItem(list[STItemId] parameters, STItemId parentId)
	| VariableItem(RName variableName, RType variableType, STItemId parentId)
	| FormalParameterItem(RName parameterName, RType parameterType, STItemId parentId)
	| LabelItem(RName labelName, STItemId parentId)
	| AliasItem(RType aliasType, RType aliasedType, bool isPublic, STItemId parentId)
	| ConstructorItem(RName constructorName, list[RNamedType] constructorArgs, STItemId adtParentId, STItemId parentId)
	| ADTItem(RType adtType, set[STItemId] variants, bool isPublic, STItemId parentId) 
	| AnnotationItem(RName annotationName, RType annoType, RType onType, bool isPublic, STItemId parentId) 
	| RuleItem(RName ruleName, STItemId parentId)
;

// Is this a symbol table layer (true) or an actual symbol table entry (false)?
public bool isLayer(STItem si) {
	switch(si) {
		case TopLayer() : return true;
		case ModuleLayer(_,_) : return true;
		case FunctionLayer(_,_) : return true;
		case PatternMatchLayer(_) : return true;
		case BooleanExpLayer(_) : return true;
		case OrLayer(_) : return true;
		case ClosureLayer(_,_) : return true;
		case VoidClosureLayer(_,_) : return true;
		case BlockLayer(_) : return true;
		default : return false;
	}
}

// Is this a symbol table item (true) or a scoping layer (false)?
public bool isItem(STItem si) {
	return !isLayer(si);
}
				
// Symbol table items can have an associated location
anno loc STItem@at;

// The various namespaces available in Rascal.
data Namespace =
	  ModuleName()
	| LabelName()
	| FCVName()
	| TypeName()
	| AnnotationName()
	| RuleName()
	| TagName()
;

// TODO: Should be able to use STItemMap here, but if I try it doesn't work, something must be
// wrong with the current alias expansion algorithm; this is the same with ItemLocationMap as well
// for itemLocations...
alias ScopeRel = rel[STItemId scopeId, STItemId itemId];
alias ItemUses = map[loc useLoc, set[STItemId] usedItems];
alias STItemMap = map[STItemId,STItem];
alias ItemLocationRel = rel[loc,STItemId];

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
// returnTypeMap: map of return locations to the type of data that they expect on return
// itBinder: used for typing of the "it" construct in reducers: keeps track of the value of "it"
//           inside the proper scope
// scopeStack: a stack of scope layers, allows entering and leaving scopes by pushing and popping
// adtItems: map from the ADT name to the related ADT and constructor symbol table items
alias SymbolTable = tuple[STItemId topSTItemId, rel[STItemId scopeId, STItemId itemId] scopeRel, 
						ItemUses itemUses, STItemId nextScopeId, map[STItemId, STItem] scopeItemMap, 
                        rel[loc, STItemId] itemLocations, STItemId currentScope, int freshType,
                        map[loc, set[str]] scopeErrorMap, map[int, RType] inferredTypeMap, map[loc, RType] returnTypeMap,
						map[loc, RType] itBinder, list[STItemId] scopeStack, 
						map[RName adtName, tuple[set[STItemId] adtItems, set[STItemId] consItems] adtInfo] adtMap];

alias AddedItemPair = tuple[SymbolTable symbolTable, STItemId addedId];
alias ScopeUpdatePair = tuple[SymbolTable symbolTable, STItemId oldScopeId];
                        
// Create an empty symbol table                        
public SymbolTable createNewSymbolTable() {
	return < -1, { }, ( ), 0, ( ), { }, 0, 0, (), (), (), (), [ ], ( )>;
}                    

// Given a number of different OR scope layers in the symbol table, find the subset of
// variables declared in all the layers. Note that, at this point, we just pick one of
// the identical variable as a representative, since they are all considered the same.
// TODO: Is this true? Should also check declared types
public SymbolTable mergeOrLayers(SymbolTable symbolTable, list[STItemId] orLayers, STItemId intoLayer) {
	set[STItemId] introducedItems = { vi | vi <- symbolTable.scopeRel[head(orLayers)], VariableItem(vn,vt,_) := symbolTable.scopeItemMap[vi] };
	for (oritem <- tail(orLayers)) {
		set[STItemId] sharedItems = { };
		for (li <- introducedItems, ri <- symbolTable.scopeRel[oritem], 
			 VariableItem(vn,_,_) := symbolTable.scopeItemMap[li], VariableItem(vn,_,_) := symbolTable.scopeItemMap[ri]) {
			sharedItems += li;
		}
		introducedItems = sharedItems;
	}

	// Finally, inject them into the intoLayer
	println("Pushing layer <symbolTable.scopeItemMap[intoLayer]>");
	symbolTable = pushScope(intoLayer, symbolTable);
	for (oritem <- introducedItems)
		if(VariableItem(vn,vt,_) := symbolTable.scopeItemMap[oritem])
			symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(vn, vt, false, symbolTable.scopeItemMap[oritem]@at, symbolTable),[<true,symbolTable.scopeItemMap[oritem]@at>]));
	// println("Popping layer <symbolTable.scopeItemMap[intoLayer]>");				
	symbolTable = popScope(symbolTable);
	// println("Back to layer <symbolTable.scopeItemMap[symbolTable.currentScope]>");

	return symbolTable;
}

public SymbolTable addItemUse(SymbolTable symbolTable, STItemId scopeItem, loc l) {
	if (l in symbolTable.itemUses)
		symbolTable.itemUses[l] = symbolTable.itemUses[l] + scopeItem;
	else
		symbolTable.itemUses += (l : { scopeItem });
	return symbolTable;
}

public SymbolTable addItemUses(SymbolTable symbolTable, set[STItemId] scopeItems, loc l) {
	if (l in symbolTable.itemUses)
		symbolTable.itemUses[l] += scopeItems;
	else
		symbolTable.itemUses += (l : scopeItems );
	return symbolTable;
}

public SymbolTable addScopeError(SymbolTable symbolTable, loc l, str msg) {
	if (l in symbolTable.scopeErrorMap)
		symbolTable.scopeErrorMap[l] = symbolTable.scopeErrorMap[l] + msg;
	else
		symbolTable.scopeErrorMap += (l : { msg } );
	return symbolTable;
}

public AddedItemPair addScopeLayer(STItem si, loc l, SymbolTable symbolTable) {
	int newItemId = symbolTable.nextScopeId;
	STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
	symbolTable = ((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel];
	return <symbolTable,newItemId>;				
}

public AddedItemPair addScopeLayerWithParent(STItem si, STItemId parentId, loc l, SymbolTable symbolTable) {
	int newItemId = symbolTable.nextScopeId;
	ScopeRel newScopeRel = symbolTable.scopeRel + <parentId, symbolTable.nextScopeId>;
	STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
	symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
	return <symbolTable,newItemId>;				
}

public AddedItemPair addSTItem(STItem si, loc l, SymbolTable symbolTable) {
	int newItemId = symbolTable.nextScopeId;
	STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
	symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel]);
	return <symbolTable,newItemId>;				
}

public AddedItemPair addSTItemWithParent(STItem si, STItemId parentId, loc l, SymbolTable symbolTable) {
	int newItemId = symbolTable.nextScopeId;
	ScopeRel newScopeRel = symbolTable.scopeRel + <parentId, symbolTable.nextScopeId>;
	STItemMap newSIMap = symbolTable.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = symbolTable.itemLocations + <l,symbolTable.nextScopeId>;
	symbolTable = (((symbolTable[nextScopeId = symbolTable.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
	return <symbolTable,newItemId>;				
}

public STItemId getLayerAtLocation(loc l, SymbolTable symbolTable) {
	if (l in domain(symbolTable.itemLocations)) {
		set[STItemId] layers = { si | si <- symbolTable.itemLocations[l], isLayer(symbolTable.scopeItemMap[si]) };
		if (size(layers) == 1)
			return getOneFrom(layers);
		else 
			throw "getLayerAtLocation: Error, trying to retrieve layer item from location with either 0 or more than 1 associated layer.";	
	} else {
		throw "getLayerAtLocation: Error, trying to retrieve item from unassociated location.";
	}
}

public SymbolTable updateSTItem(STItem si, STItemId idToUpdate, SymbolTable symbolTable) {
	return symbolTable[scopeItemMap = symbolTable.scopeItemMap + (idToUpdate : si)];				
}

public STItem getSTItem(STItemId id, SymbolTable symbolTable) {
	return symbolTable.scopeItemMap[id];
}
	
// DEPRECATED...
public ScopeUpdatePair changeCurrentScope(STItemId newScopeId, SymbolTable symbolTable) {
	int oldScopeId = symbolTable.currentScope;
	return < symbolTable[currentScope = newScopeId], oldScopeId >;
}
              
//
// Pretty printers for scope information
//
public str prettyPrintSI(STItem si) {
	switch(si) {
		case TopLayer() : return "TopLayer";

		case ModuleLayer(_,_) : return "ModuleLayer";
		
		case FunctionLayer(_,_) : return "FunctionLayer";
		
		case PatternMatchLayer(_) : return "PatternMatchLayer";
		
		case BooleanExpLayer(_) : return "BooleanExpLayer";

		case OrLayer(_) : return "OrLayer";

		case ClosureLayer(_,_) : return "ClosureLayer";
		
		case VoidClosureLayer(_,_) : return "VoidClosureLayer";
		
		case BlockLayer(_) : return "BlockLayer";

		case ModuleItem(x,_) : return "ModuleItem: " + prettyPrintName(x);
		
		case FunctionItem(x,t,ags,_,_,_) : return "FunctionItem: " + prettyPrintType(t) + " " + prettyPrintName(x) + "(" + joinList(ags,prettyPrintSI,",","") + ")";

		case ClosureItem(t,ags,_,_) : return "ClosureItem: " + prettyPrintType(t) + " (" + joinList(ags,prettyPrintSI,",","") + ")";
		
		case VoidClosureItem(ags,_,_) : return "VoidClosureItem: (" + joinList(ags,prettyPrintSI,",","") + ")";

		case VariableItem(x,t,_) : return "VariableItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case FormalParameterItem(x,t,_) : return "FormalParameterItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case LabelItem(x,_) : return "LabelItem: " + prettyPrintName(x);

		case AliasItem(tn,ta,_,_) : return "AliasItem: " + prettyPrintType(tn) + " = " + prettyPrintType(ta);
			
		case ConstructorItem(cn,tas,_,_) : 	return "Constructor: " + prettyPrintName(cn) + "(" + prettyPrintNamedTypeList(tas) + ")";
		
		case ADTItem(ut, vs, _, _) : return "ADT: " + prettyPrintType(ut) + " = " + joinList(vs,prettyPrintSI," | ","");
		 			
		case AnnotationItem(x,atyp,otyp,_,_) : return "Annotation: <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)>";
		
		case RuleItem(x,_) : return "Rule: " + prettyPrintName(x);
	}
}

public set[STItemId] filterNamesForNamespace(SymbolTable symbolTable, set[STItemId] scopeItems, Namespace namespace) {
	set[STItemId] filteredItems = { };
	for (itemId <- scopeItems) {
		switch(namespace) {
			case ModuleName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case ModuleItem(_,_) : filteredItems += itemId;
				}	
			}
			
			case LabelName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case LabelItem(_,_) : filteredItems += itemId;
				}	
			}
			
			case FCVName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case FunctionItem(_,_,_,_,_,_) : filteredItems += itemId;
					case VariableItem(_,_,_) : filteredItems += itemId;
					case FormalParameterItem(_,_,_) : filteredItems += itemId;
					case ConstructorItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
					
			case TypeName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case ADTItem(_,_,_,_) : filteredItems += itemId;
					case AliasItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
			
			case AnnotationName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case AnnotationItem(_,_,_,_) : filteredItems += itemId;
				}
			}
			
			case RuleName() : {
				switch(symbolTable.scopeItemMap[itemId]) {
					case RuleItem(_,_) : filteredItems += itemId;
				}
			}
		}
	}
	return filteredItems;
}

//
// Functions for finding names in the current scope
//

//
// Find the names visible at the current level of scoping. This allows shadowing
// of names, but does not allow matches across multiple levels of scoping.
// For instance, declaring a function at one level hides all functions with
// the same name at higher levels -- i.e., overloading cannot be partially
// extended.
//
public set[STItemId] getItemsForNameWBound(SymbolTable symbolTable, STItemId currentScopeId, RName x, set[Namespace] containingNamespaces, bool funBounded, bool modBounded) {
	set[STItemId] foundItems = { };

	// Handle qualified names. If this is not a module name, then we try to switch into the scope
	// of the module named in the qualified name. If we are looking for modules, handle this
	// below, since modules do have qualified names, so we don't want to deconstruct the name
	// first.	
	if (ModuleName() notin containingNamespaces) {
		if (RCompoundName(nl) := x) {
			RName moduleName = (size(tail(nl)) == 1) ? RSimpleName(head(tail(reverse(nl)))) : RCompoundName(reverse(tail(reverse(nl)))); // There should be a better way to do this
			x = RSimpleName(head(tail(nl,1)));
			set[STItemId] mods = getModuleItemsForName(symbolTable, moduleName);
			if (size(mods) == 1) {
				// Redirect to module scope
				return getItemsForNameWBound(symbolTable, symbolTable.scopeItemMap[getOneFrom(mods)].parentId, x, containingNamespaces, funBounded, modBounded);					
			} else if (size(mods) > 1) {
				// TODO: Error, should be caught processing imports -- this means multiple modules have the same name
				// BUT: When we have parameterized modules, this could happen -- how will we distinguish?
				return { }; 
			} else {
				return { };
			}
		}
	} else if (ModuleName() in containingNamespaces && size(containingNamespaces) > 1) {
		return getItemsForNameWBound(symbolTable, currentScopeId, x, { ModuleName() }, funBounded, modBounded) +
				getItemsForNameWBound(symbolTable, currentScopeId, x, containingNamespaces - ModuleName(), funBounded, modBounded);
	}

	// We fall through to here IF a) the name is not a qualified name, or b) the name IS a qualified name but is
	// the name of a module. Otherwise, it is taken care of above.

	// Find all the scope items at the current level of scope that match the name we are looking for. Note that we need
	// special handling for functions and modules, since the function and module items are actually store inside the function
	// and module layers. The other layers are not named, so could never lead to a match.
	for (itemId <- symbolTable.scopeRel[currentScopeId]) {
		switch(symbolTable.scopeItemMap[itemId]) {
			case ModuleItem(x,_) : foundItems += itemId;
			case FormalParameterItem(x,_,_) : foundItems += itemId;
			case VariableItem(x,_,_) : foundItems += itemId;
			case FunctionItem(x,_,_,_,_,_) : foundItems += itemId;
			case LabelItem(x,_) : foundItems += itemId;
			case ConstructorItem(x,_,_,_) : foundItems += itemId;
			case AnnotationItem(x,_,_,_) : foundItems += itemId;
			case RuleItem(x,_) : foundItems += itemId;
			case AliasItem(RUserType(x),_,_,_) : foundItems += itemId; 
			case AliasItem(RParameterizedUserType(x,_),_,_,_) : foundItems += itemId; 
			case ADTItem(RUserType(x),_,_,_) : foundItems += itemId; 
			case ADTItem(RParameterizedUserType(x,_),_,_,_) : foundItems += itemId; 
			case FunctionLayer(funItemId,_) : if (FunctionItem(x,_,_,_,_,_) := symbolTable.scopeItemMap[funItemId]) foundItems += funItemId;
			case ModuleLayer(modItemId,_) : if (ModuleItem(x,_) := symbolTable.scopeItemMap[modItemId]) foundItems += modItemId; 			
		}
	}

	// Now, filter it down based on the namespaces we are looking for
	foundItems = { f | ns <- containingNamespaces, f <- filterNamesForNamespace(symbolTable, foundItems, ns) };
		
	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level is reached. If this is a bounded search, don't pass through 
	// function boundaries.
	if (size(foundItems) == 0) {
		switch(symbolTable.scopeItemMap[currentScopeId]) {
			case ModuleLayer(_,parentScopeId) : if (!modBounded) foundItems = getItemsForNameWBound(symbolTable, parentScopeId, x, containingNamespaces, funBounded,modBounded);
			case FunctionLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case PatternMatchLayer(parentScopeId) : foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case BooleanExpLayer(parentScopeId) : foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case OrLayer(parentScopeId) : foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case ClosureLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case VoidClosureLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case BlockLayer(parentScopeId) : foundItems = getItemsForNameWBound(symbolTable,parentScopeId,x,containingNamespaces,funBounded,modBounded);
		}
	}

	// NOTE: This can be empty (for instance, when looking up a declaration of a variable that is not explicitly declared)	
	return foundItems;	
}

//
// These are specialized versions of the above function to make it easier to get back the
// right names.
//
public set[STItemId] getItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { FCVName() }, false, false);
}

public set[STItemId] getItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { FCVName() }, true, false);
}

public set[STItemId] getItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { FCVName() }, false, true);
}

public set[STItemId] getAnnotationItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { AnnotationName() }, false, false);
}

public set[STItemId] getAnnotationItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { AnnotationName() }, true, false);
}

public set[STItemId] getAnnotationItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { AnnotationName() }, false, true);
}

public set[STItemId] getRuleItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { RuleName() }, false, false);
}

public set[STItemId] getRuleItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { RuleName() }, true, false);
}

public set[STItemId] getRuleItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { RuleName() }, false, true);
}

public set[STItemId] getLabelItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { LabelName() }, false, false);
}

public set[STItemId] getLabelItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { LabelName() }, true, false);
}

public set[STItemId] getLabelItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { LabelName() }, false, true);
}

public set[STItemId] getTypeItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeName() }, false, false);
}

public set[STItemId] getTypeItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeName() }, true, false);
}

public set[STItemId] getTypeItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeName() }, false, true);
}

public set[STItemId] getModuleItemsForName(SymbolTable symbolTable, RName x) {
	return getItemsForNameWBound(symbolTable, symbolTable.topSTItemId, x, { ModuleName() }, false, false);
}

// Get the function item that encloses a point in the code. This will navigate up to the function layer
// and then return the item id for the function item that is associated with this layer.
public STItemId getEnclosingFunctionAux(SymbolTable symbolTable, STItemId currentScope) {
	STItem si = getSTItem(currentScope, symbolTable);
	if (FunctionLayer(itemId,_) := si) return itemId;
	if (TopLayer() := si) throw "Cannot get enclosing function at top level";
	return getEnclosingFunctionAux(symbolTable, si.parentId);
}

public STItemId getEnclosingFunction(SymbolTable symbolTable) {
	return getEnclosingFunctionAux(symbolTable, symbolTable.currentScope);
}

public RType getEnclosingFunctionType(SymbolTable symbolTable) {
	return getTypeForItem(symbolTable, getEnclosingFunction(symbolTable)); 
}

//
// TODO: This should throw an exception when the type of an untyped name (e.g., a label) is requested
//
public RType getTypeForItem(SymbolTable symbolTable, STItemId itemId) {
	if (itemId notin symbolTable.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";

	switch(symbolTable.scopeItemMap[itemId]) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionItem(_,t,paramIds,_,_,_) : 
			return makeFunctionType(t,[getTypeForItem(symbolTable, paramId) | paramId <- paramIds]);
		
		case ConstructorItem(n,tas,adtParentId,_) : return makeConstructorType(n,tas,getTypeForItem(symbolTable,adtParentId));
		
		case ADTItem(ut,_,_,_) : return ut; // TODO: Should also extract type parameters, if needed

		case AliasItem(ut,_,_,_) : return ut; // TODO: Should also extract type parameters, if needed
		
		default : { 
			return makeVoidType(); 
		}
	}
}

alias ResultTuple = tuple[SymbolTable symbolTable, list[STItemId] addedItems];

//
// Functions to push new scope layers and add scope items; also performs
// some error checking.
//

public ResultTuple pushNewTopScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aipTop = addScopeLayer(TopLayer()[@at=l], l, symbolTable);
	symbolTable = aipTop.symbolTable[topSTItemId = aipTop.addedId];
	symbolTable.scopeStack = [ aipTop.addedId ] + symbolTable.scopeStack;
	symbolTable.currentScope = aipTop.addedId;
	return <symbolTable,[aipTop.addedId]>; 	
}

public ResultTuple pushNewModuleScope(RName moduleName, loc l, SymbolTable symbolTable) {
	println("Adding module layer to parent <symbolTable.scopeItemMap[symbolTable.currentScope]>");
	AddedItemPair aip = addScopeLayerWithParent(ModuleLayer(-1, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	AddedItemPair aip2 = addSTItemWithParent(ModuleItem(moduleName, aip.symbolTable.currentScope)[@at=l], aip.symbolTable.currentScope, l, aip.symbolTable);
	aip2.symbolTable.scopeItemMap[aip.addedId].itemId = aip2.addedId;
	return <aip2.symbolTable,[aip.addedId,aip2.addedId]>; 	
}

public ResultTuple pushNewBooleanScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayer(BooleanExpLayer(symbolTable.currentScope)[@at=l], l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewOrScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayer(OrLayer(symbolTable.currentScope)[@at=l], l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewBlockScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayer(BlockLayer(symbolTable.currentScope)[@at=l], l, symbolTable);
	aip.symbolTable.scopeStack = [ aip.addedId ] + aip.symbolTable.scopeStack;
	aip.symbolTable.currentScope = aip.addedId;
	return <aip.symbolTable,[aip.addedId]>; 	
}

public ResultTuple pushNewPatternMatchScope(loc l, SymbolTable symbolTable) {
	AddedItemPair aip = addScopeLayer(PatternMatchLayer(symbolTable.currentScope)[@at=l], l, symbolTable);
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

public ResultTuple pushNewFunctionScopeAt(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(FunctionLayer(-1, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	aipLayer.symbolTable.scopeStack = [ aipLayer.addedId ] + aipLayer.symbolTable.scopeStack;
	aipLayer.symbolTable.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	symbolTable = aipLayer.symbolTable;
	list[STItemId] paramIds = [ ];
	set[RName] namesSeen = { functionName };
	for (tuple[RName pname, RType ptype, loc ploc, loc nloc] pt <- params) {
		if (pt.pname != RSimpleName("") && pt.pname in namesSeen) {
			symbolTable = addScopeError(symbolTable, pt.nloc, "Illegal redefinition of <prettyPrintName(pt.pname)>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addSTItemWithParent(FormalParameterItem(pt.pname, pt.ptype, symbolTable.currentScope)[@at=pt.ploc], symbolTable.currentScope, pt.ploc, symbolTable);
		paramIds += aipParam.addedId; symbolTable = aipParam.symbolTable;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(FunctionItem(functionName, retType, paramIds, throwsTypes, isPublic, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;
	
	return <aipItem.symbolTable,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewFunctionScope(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, SymbolTable symbolTable) {
	return pushNewFunctionScopeAt(functionName, retType, params, throwsTypes, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple pushNewFunctionScopeAtTop(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, SymbolTable symbolTable) {
	return pushNewFunctionScopeAt(functionName, retType, params, throwsTypes, isPublic, l, symbolTable, symbolTable.topSTItemId);
} 

public ResultTuple pushNewClosureScopeAt(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(ClosureLayer(-1, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	aipLayer.symbolTable.scopeStack = [ aipLayer.addedId ] + aipLayer.symbolTable.scopeStack;
	aipLayer.symbolTable.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	symbolTable = aipLayer.symbolTable;
	list[STItemId] paramIds = [ ];
	for (pt <- params) {
		if (pt.pname in namesSeen) {
			symbolTable = addScopeError(symbolTable, pt.nloc, "Illegal redefinition of <n>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addSTItemWithParent(FormalParameterItem(pt.pname, pt.ptype, symbolTable.currentScope)[@at=pt.ploc], symbolTable.currentScope, pt.ploc, symbolTable);
		paramIds += aip.addedId; symbolTable = aip.symbolTable;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(ClosureItem(retType, paramIds, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	return <aipItem.symbolTable,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewClosureScope(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable) {
	return pushNewClosureScopeAt(retType, params, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple pushNewClosureScopeAtTop(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable) {
	return pushNewClosureScopeAt(retType, params, l, symbolTable, symbolTable.topSTItemId);
} 

public ResultTuple pushNewVoidClosureScopeAt(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(VoidClosureLayer(-1, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	aipLayer.symbolTable.scopeStack = [ aipLayer.addedId ] + aipLayer.symbolTable.scopeStack;
	aipLayer.symbolTable.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	symbolTable = aipLayer.symbolTable;
	list[STItemId] paramIds = [ ];
	for (pt <- params) {
		if (pt.pname in namesSeen) {
			symbolTable = addScopeError(symbolTable, pt.nloc, "Illegal redefinition of <n>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addSTItemWithParent(FormalParameterItem(pt.pname, pt.ptype, symbolTable.currentScope)[@at=pt.ploc], symbolTable.currentScope, pt.ploc, symbolTable);
		paramIds += aip.addedId; symbolTable = aip.symbolTable;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(VoidClosureItem(paramIds, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	return <aipItem.symbolTable,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewVoidClosureScope(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable) {
	return pushNewVoidClosureScopeAt(params, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple pushNewVoidClosureScopeAtTop(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, SymbolTable symbolTable) {
	return pushNewVoidClosureScopeAt(params, l, symbolTable, symbolTable.topSTItemId);
} 

public ResultTuple addAliasToScopeAt(RType aliasType, RType aliasedType, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(AliasItem(aliasType, aliasedType, isPublic, scopeToUse), scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addAliasToScope(RType aliasType, RType aliasedType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAliasToScopeAt(aliasType, aliasedType, isPublic, l, symbolTable, symbolTable.currentScope);
}

public ResultTuple addAliasToTopScope(RType aliasType, RType aliasedType, bool isPublic, loc l, SymbolTable symbolTable) {
	return addAliasToScopeAt(aliasType, aliasedType, isPublic, l, symbolTable, symbolTable.topSTItemId);
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

public ResultTuple addADTToScopeAt(RType adtName, bool isPublic, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(ADTItem(adtName, { }, isPublic, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
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

public ResultTuple addSTItemUses(ResultTuple result, list[tuple[bool flagUse, loc useloc]] useLocs) {
	SymbolTable symbolTable = result.symbolTable;
	for (n <- [0..size(useLocs)-1]) if (useLocs[n].flagUse) symbolTable = addItemUse(symbolTable, result.addedItems[n], useLocs[n].useloc);
	return <symbolTable, result.addedItems>;
}

// TODO: Extend this to also account for type parameters
// TODO: Add locations of conflicting types
public ResultTuple checkForDuplicateAliasesBounded(ResultTuple result, loc nloc, bool modBounded) {
	SymbolTable symbolTable = result.symbolTable;
	STItemId aliasId = result.addedItems[0];
	RName aliasName = getUserTypeName(symbolTable.scopeItemMap[aliasId].aliasType);
	set[STItemId] otherItems = { };
	if (modBounded) {
		otherItems = getTypeItemsForNameMB(symbolTable, symbolTable.currentScope, aliasName) - aliasId;
	} else {
		otherItems = getTypeItemsForName(symbolTable, symbolTable.currentScope, aliasName) - aliasId;
	}
	for (oi <- otherItems) {
		switch(symbolTable.scopeItemMap[oi]) {
			case AliasItem(aname,atype,_,_) : {
				if (unrollAlias(atype,symbolTable) != unrollAlias(symbolTable.scopeItemMap[aliasId].aliasedType,symbolTable)) {
					symbolTable = addScopeError(symbolTable, nloc, "Scope Error: Definition of alias <prettyPrintName(aliasName)> conflicts with another alias of the same name");	
				}
			}
			case ADTItem(_,_,_,_) : {
				symbolTable = addScopeError(symbolTable, nloc, "Scope Error: Definition of alias <prettyPrintName(aliasName)> conflicts with an ADT of the same name");
			} 
		}
	}
	return <symbolTable, result.addedItems>;
}

public ResultTuple checkForDuplicateAliases(ResultTuple result, loc nloc) {
	return checkForDuplicateAliasesBounded(result, nloc, false);
}

public ResultTuple checkForDuplicateAliasesInModule(ResultTuple result, loc nloc) {
	return checkForDuplicateAliasesBounded(result, nloc, true);
}

//
// TODO: It may be nice to indicate WHICH function the new function overlaps with. Right now,
// this is just a binary true or false check.
//
// TODO: Also need to handle the overlap case where we have a varargs function, which is converted into
// a function that takes a list of parameters (for the varargs part) and a function that already takes
// this list
//
public ResultTuple checkFunctionOverlap(ResultTuple result, loc nloc) {
	SymbolTable symbolTable = result.symbolTable;
	STItemId newFun = result.addedItems[1];
	STItem newFunItem = getSTItem(newFun, symbolTable);
	RType newFunItemType = getTypeForItem(symbolTable, newFun);
	list[RType] newFunItemArgs = getFunctionArgumentTypes(newFunItemType);

	set[STItemId] potentialOverlaps = getItemsForName(symbolTable, symbolTable.currentScope, newFunItem.functionName) - newFun;

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		STItem overlapItem = getSTItem(oid, symbolTable);
		RType overlapItemType = getTypeForItem(symbolTable, oid);
		if (isFunctionType(overlapItemType)) {
			list[RType] funItemArgs = getFunctionArgumentTypes(overlapItemType);
	
			// Special handling of varargs functions -- to check overlap, we extend a shorter varargs
			// function to have the same size as the longer function and remove varargs notation. So,
			// f(int...) and f(int,int,str) would become f(int,int,int) and f(int,int,str). This allows
			// a simpler check below.
			if (isVarArgsFun(overlapItemType) || isVarArgsFun(newFunItemType)) {
				if (isVarArgsFun(overlapItemType) && (size(funItemArgs) < size(newFunItemArgs))) {
					RType et = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
					funItemArgs[size(funItemArgs)-1] = et;
					funItemArgs += [ et | n <- [1 .. (size(newFunItemArgs) - size(funItemArgs))] ];
					if (isVarArgsFun(newFunItemType)) newFunItemArgs[size(newFunItemArgs)-1] = getVarArgsType(getElementType(head(tail(newFunItemArgs,1))));
				} else if (isVarArgsFun(newFunItemType) && (size(newFunItemArgs) < size(funItemArgs))) {
					RType et = getVarArgsType(getElementType(head(tail(newFunItemArgs,1))));
					newFunItemArgs[size(newFunItemArgs)-1] = et;
					newFunItemArgs += [ et | n <- [1 .. (size(funItemArgs) - size(newFunItemArgs))] ];
					if (isVarArgsFun(overlapItemType)) funItemArgs[size(funItemArgs)-1] = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
				}
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(funItemArgs) == size(newFunItemArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(funItemArgs)) {
					RType t1 = funItemArgs[n]; RType t2 = newFunItemArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					symbolTable = addScopeError(symbolTable, nloc, "Overlapping overload of function <newFunItem.functionName> declared");
				}
			}
		} else if (isConstructorType(overlapItemType)) {
			list[RType] consItemArgs = getConstructorArgumentTypes(overlapItemType);

			// Similar to the varargs handling above, but only for the function	
			if (isVarArgsFun(newFunItemType) && (size(newFunItemArgs) < size(consItemArgs))) {
				RType et = getVarArgsType(getElementType(head(tail(newFunItemArgs,1))));
				newFunItemArgs[size(newFunItemArgs)-1] = et;
				newFunItemArgs += [ et | n <- [1 .. (size(consItemArgs) - size(newFunItemArgs))] ];
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(consItemArgs) == size(newFunItemArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(consItemArgs)) {
					RType t1 = consItemArgs[n]; RType t2 = newFunItemArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					symbolTable = addScopeError(symbolTable, nloc, "Overlapping overload of function <newFunItem.functionName> declared");
				}
			}
		} else {
			symbolTable = addScopeError(symbolTable, nloc, "Function name clashes with another declaration: <prettyPrintSI(overlapItem)>");
		}
	}
	return <symbolTable, result.addedItems>;
}

// TODO: This is a copy of the above function, with the addition of scope errors replaced
// with a return of true, and the final return replaced with a return of false. Factor out
// the common code somehow. This is needed because we have situations where we need to check
// for overlap before the scope item is addded, along with situations where we want to check
// for overlap after the scope item is added.
public bool willFunctionOverlap(RName funName, RType funType, SymbolTable symbolTable, STItemId scopeToCheck) {
	list[RType] funArgs = getFunctionArgumentTypes(funType);

	set[STItemId] potentialOverlaps = getItemsForName(symbolTable, scopeToCheck, funName);

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		STItem overlapItem = getSTItem(oid, symbolTable);
		RType overlapItemType = getTypeForItem(symbolTable, oid);
		if (isFunctionType(overlapItemType)) {
			list[RType] funItemArgs = getFunctionArgumentTypes(overlapItemType);
	
			// Special handling of varargs functions -- to check overlap, we extend a shorter varargs
			// function to have the same size as the longer function and remove varargs notation. So,
			// f(int...) and f(int,int,str) would become f(int,int,int) and f(int,int,str). This allows
			// a simpler check below.
			if (isVarArgsFun(overlapItemType) || isVarArgsFun(funType)) {
				if (isVarArgsFun(overlapItemType) && (size(funItemArgs) < size(funArgs))) {
					RType et = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
					funItemArgs[size(funItemArgs)-1] = et;
					funItemArgs += [ et | n <- [1 .. (size(funArgs) - size(funItemArgs))] ];
					if (isVarArgsFun(funType)) newFunItemArgs[size(funArgs)-1] = getVarArgsType(getElementType(head(tail(funArgs,1))));
				} else if (isVarArgsFun(funType) && (size(funArgs) < size(funItemArgs))) {
					RType et = getVarArgsType(getElementType(head(tail(funArgs,1))));
					funArgs[size(funArgs)-1] = et;
					funArgs += [ et | n <- [1 .. (size(funItemArgs) - size(funArgs))] ];
					if (isVarArgsFun(overlapItemType)) funItemArgs[size(funItemArgs)-1] = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
				}
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(funItemArgs) == size(funArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(funItemArgs)) {
					RType t1 = funItemArgs[n]; RType t2 = funArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					return true; 
				}
			}
		} else if (isConstructorType(overlapItemType)) {
			list[RType] consItemArgs = getConstructorArgumentTypes(overlapItemType);

			// Similar to the varargs handling above, but only for the function	
			if (isVarArgsFun(funType) && (size(funArgs) < size(consItemArgs))) {
				RType et = getVarArgsType(getElementType(head(tail(funArgs,1))));
				funArgs[size(funArgs)-1] = et;
				funArgs += [ et | n <- [1 .. (size(consItemArgs) - size(funArgs))] ];
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(consItemArgs) == size(funArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(consItemArgs)) {
					RType t1 = consItemArgs[n]; RType t2 = funArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					return true; 
				}
			}
		} else {
			return true;
		}
	}
	return false;
}

public ResultTuple checkConstructorOverlap(ResultTuple result, loc nloc) {
	SymbolTable symbolTable = result.symbolTable;
	STItemId newCons = result.addedItems[0];
	STItem newConsItem = getSTItem(newCons, symbolTable);
	RType newConsItemType = getTypeForItem(symbolTable, newCons);
	list[RType] newConsItemArgs = getConstructorArgumentTypes(newConsItemType);

	set[STItemId] potentialOverlaps = getItemsForName(symbolTable, symbolTable.currentScope, newConsItem.constructorName) - newCons;

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		STItem overlapItem = getSTItem(oid, symbolTable);
		RType overlapItemType = getTypeForItem(symbolTable, oid);
		if (isConstructorType(overlapItemType)) {
			list[RType] consItemArgs = getConstructorArgumentTypes(overlapItemType);
			if (size(consItemArgs) == size(newConsItemArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(consItemArgs)) {
					RType t1 = consItemArgs[n]; RType t2 = newConsItemArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					symbolTable = addScopeError(symbolTable, nloc, "Overlapping overload of constructor <newConsItem.constructorName> declared");
				}
			}
		} else if (isFunctionType(overlapItemType)) {
			list[RType] funItemArgs = getFunctionArgumentTypes(overlapItemType);

			// Similar to the varargs handling above, but only for the function	
			if (isVarArgsFun(overlapItemType) && (size(funItemArgs) < size(newConsItemArgs))) {
				RType et = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
				funItemArgs[size(funItemArgs)-1] = et;
				funItemArgs += [ et | n <- [1 .. (size(newConsItemArgs) - size(funItemArgs))] ];
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(funItemArgs) == size(newConsItemArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(funItemArgs)) {
					RType t1 = funItemArgs[n]; RType t2 = newConsItemArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					symbolTable = addScopeError(symbolTable, nloc, "Overlapping overload of constructor <newConsItem.constructorName> declared");
				}
			}
		} else {
			symbolTable = addScopeError(symbolTable, nloc, "Constructor name clashes with another declaration: <prettyPrintSI(overlapItem)>");
		}
	}
	return <symbolTable, result.addedItems>;
}

// TODO: This is a copy of the above function, with the addition of scope errors replaced
// with a return of true, and the final return replaced with a return of false. Factor out
// the common code somehow. This is needed because we have situations where we need to check
// for overlap before the scope item is addded, along with situations where we want to check
// for overlap after the scope item is added.
public bool willConstructorOverlap(RName conName, RType conType, SymbolTable symbolTable, STItemId scopeToCheck) {
	list[RType] consArgs = getConstructorArgumentTypes(conType);
	set[STItemId] potentialOverlaps = getItemsForName(symbolTable, scopeToCheck, conName);

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		STItem overlapItem = getSTItem(oid, symbolTable);
		RType overlapItemType = getTypeForItem(symbolTable, oid);
		if (isConstructorType(overlapItemType)) {
			list[RType] consItemArgs = getConstructorArgumentTypes(overlapItemType);
			if (size(consItemArgs) == size(consArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(consItemArgs)) {
					RType t1 = consItemArgs[n]; RType t2 = consArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					return true; 
				}
			}
		} else if (isFunctionType(overlapItemType)) {
			list[RType] funItemArgs = getFunctionArgumentTypes(overlapItemType);

			// Similar to the varargs handling above, but only for the function	
			if (isVarArgsFun(overlapItemType) && (size(funItemArgs) < size(consArgs))) {
				RType et = getVarArgsType(getElementType(head(tail(funItemArgs,1))));
				funItemArgs[size(funItemArgs)-1] = et;
				funItemArgs += [ et | n <- [1 .. (size(consArgs) - size(funItemArgs))] ];
			}
	
			// Check to see if both lists of args are the same length; if not, we cannot have an
			// overlap between the two functions.
			if (size(funItemArgs) == size(consArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(funItemArgs)) {
					RType t1 = funItemArgs[n]; RType t2 = consArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					return true; 
				}
			}
		} else {
			return true;
		}
	}
	return false;
}
	
// TODO: Need to actually unroll the type here, this currently just works if the aliased type
// is not, itself, an alias
// TODO: Need to account for different types defined with the same name
RType unrollAlias(RType aliasedType, SymbolTable symbolTable) {
	return aliasedType;
}

public SymbolTable markReturnType(RType t, Statement s, SymbolTable symbolTable) {
	return symbolTable[returnTypeMap = symbolTable.returnTypeMap + ( s@\loc : t )];
}

// TODO: We may want to allow repeated annotations of the same name and type
public ResultTuple checkForDuplicateAnnotationsBounded(ResultTuple result, loc nloc, bool modBounded) {
	SymbolTable symbolTable = result.symbolTable;
	STItemId annotationId = result.addedItems[0];
	RName annotationName = symbolTable.scopeItemMap[annotationId].annotationName;
	set[STItemId] otherItems = { };
	if (modBounded) {
		otherItems = getAnnotationItemsForNameMB(symbolTable, symbolTable.currentScope, annotationName) - annotationId;
	} else {
		otherItems = getAnnotationItemsForName(symbolTable, symbolTable.currentScope, annotationName) - annotationId;
	}
	if (size(otherItems) > 0) {
		symbolTable = addScopeError(symbolTable, nloc, "Scope Error: Definition of annotation <prettyPrintName(annotationName)> conflicts with another annotation of the same name");	
	}
	return <symbolTable, result.addedItems>;
}

public ResultTuple checkForDuplicateAnnotations(ResultTuple result, loc nloc) {
	return checkForDuplicateAnnotationsBounded(result, nloc, false);
}

public ResultTuple checkForDuplicateAnnotationsInModule(ResultTuple result, loc nloc) {
	return checkForDuplicateAnnotationsBounded(result, nloc, true);
}

public ResultTuple checkForDuplicateRulesBounded(ResultTuple result, loc nloc, bool modBounded) {
	SymbolTable symbolTable = result.symbolTable;
	STItemId ruleId = result.addedItems[0];
	RName ruleName = symbolTable.scopeItemMap[ruleId].ruleName;
	set[STItemId] otherItems = { };
	if (modBounded) {
		otherItems = getRuleItemsForNameMB(symbolTable, symbolTable.currentScope, ruleName) - ruleId;
	} else {
		otherItems = getRuleItemsForName(symbolTable, symbolTable.currentScope, ruleName) - ruleId;
	}
	if (size(otherItems) > 0) {
		symbolTable = addScopeError(symbolTable, nloc, "Scope Error: Definition of rule <prettyPrintName(ruleName)> conflicts with another rule of the same name");	
	}
	return <symbolTable, result.addedItems>;
}

public ResultTuple checkForDuplicateRules(ResultTuple result, loc nloc) {
	return checkForDuplicateRulesBounded(result, nloc, false);
}

public ResultTuple checkForDuplicateRulesInModule(ResultTuple result, loc nloc) {
	return checkForDuplicateRulesBounded(result, nloc, true);
}

public bool inBoolLayer(SymbolTable symbolTable) {
	if (BooleanExpLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope] || OrLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope])
		return true;
	return false;
}

public SymbolTable consolidateADTDefinitions(SymbolTable symbolTable, RName moduleName) {
	// Get back the ID for the name of the module being checked -- there should be only one matching
	// item. TODO: We may want to verify that here.
	STItemId moduleLayerId = getOneFrom(getModuleItemsForName(symbolTable, moduleName));
	
	// Step 1: Pick out all ADT definitions in the loaded scope information (i.e., all ADTs defined
	// in either the loaded module or its direct imports)
	set[STItemId] adtIDs = { sid | sid <- symbolTable.scopeRel[symbolTable.topSTItemId], ADTItem(_,_,_,_) := symbolTable.scopeItemMap[sid] } +
							  { sid | sid <- symbolTable.scopeRel[symbolTable.scopeItemMap[moduleLayerId].parentId], ADTItem(_,_,_,_) := symbolTable.scopeItemMap[sid] };
							  
	// Step 2: Group these based on the name of the ADT
	rel[RName adtName, STItemId adtItemId] nameXADTItem = { < getUserTypeName(n), sid > | sid <- adtIDs, ADTItem(n,_,_,_) := symbolTable.scopeItemMap[sid] };
	
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

