module rascal::checker::ScopeInfo

import rascal::checker::Types;
import rascal::checker::Signature;
import rascal::\old-syntax::Rascal;

import List;
import IO;
import Set;
import Relation;
import Map;

// Unique identifiers for scope items
alias ScopeItemId = int;

// Items representing identifiable parts of scope, including layers (modules, functions,
// blocks) and items (functions, variables, formal parameters, labels).
data ScopeItem =
      TopLayer()
	| ModuleLayer(ScopeItemId itemId, ScopeItemId parentId)
	| FunctionLayer(ScopeItemId itemId, ScopeItemId parentId)
	| PatternMatchLayer(ScopeItemId parentId)
	| BooleanExpLayer(ScopeItemId parentId)
	| OrLayer(ScopeItemId parentId)
	| ClosureLayer(ScopeItemId itemId, ScopeItemId parentId)
	| VoidClosureLayer(ScopeItemId itemId, ScopeItemId parentId)
	| BlockLayer(ScopeItemId parentId)

	| ModuleItem(RName moduleName, ScopeItemId parentId)
	| FunctionItem(RName functionName, RType returnType, list[ScopeItemId] parameters, list[RType] throwsTypes, bool isPublic, ScopeItemId parentId)
	| ClosureItem(RType returnType, list[ScopeItemId] parameters, ScopeItemId parentId)
	| VoidClosureItem(list[ScopeItemId] parameters, ScopeItemId parentId)
	| VariableItem(RName variableName, RType variableType, ScopeItemId parentId)
	| FormalParameterItem(RName parameterName, RType parameterType, ScopeItemId parentId)
	| LabelItem(RName labelName, ScopeItemId parentId)
	| AliasItem(RType aliasType, RType aliasedType, bool isPublic, ScopeItemId parentId)
	| ConstructorItem(RName constructorName, list[RNamedType] constructorArgs, ScopeItemId adtParentId, ScopeItemId parentId)
	| ADTItem(RType adtType, set[ScopeItemId] variants, bool isPublic, ScopeItemId parentId) 
	| AnnotationItem(RName annotationName, RType annoType, RType onType, bool isPublic, ScopeItemId parentId) 
	| RuleItem(RName ruleName, ScopeItemId parentId)
;

public bool isLayer(ScopeItem si) {
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

public bool isItem(ScopeItem si) {
	return !isLayer(si);
}
				
anno loc ScopeItem@at;

data Namespace =
	  ModuleName()
	| LabelName()
	| FCVName()
	| TypeName()
	| AnnotationName()
	| RuleName()
	| TagName()
;

alias ScopeRel = rel[ScopeItemId scopeId, ScopeItemId itemId];
alias ItemUses = map[loc useLoc, set[ScopeItemId] usedItems];
alias ScopeItemMap = map[ScopeItemId,ScopeItem];
alias ItemLocationRel = rel[loc,ScopeItemId];

// TODO: Should be able to use ScopeItemMap here, but if I try it doesn't work, something must be
// wrong with the current alias expansion algorithm; this is the same with ItemLocationMap as well
// for itemLocations...
alias ScopeInfo = tuple[ScopeItemId topScopeItemId, rel[ScopeItemId scopeId, ScopeItemId itemId] scopeRel, 
						ItemUses itemUses, ScopeItemId nextScopeId, map[ScopeItemId, ScopeItem] scopeItemMap, 
                        rel[loc, ScopeItemId] itemLocations, ScopeItemId currentScope, int freshType,
                        map[loc, set[str]] scopeErrorMap, map[int, RType] inferredTypeMap, map[loc, RType] returnTypeMap,
						map[loc, RType] itBinder, list[ScopeItemId] scopeStack, 
						map[RName adtName, tuple[set[ScopeItemId] adtItems, set[ScopeItemId] consItems] adtInfo] adtMap];

alias AddedItemPair = tuple[ScopeInfo scopeInfo, ScopeItemId addedId];
alias ScopeUpdatePair = tuple[ScopeInfo scopeInfo, ScopeItemId oldScopeId];
                        
public ScopeInfo createNewScopeInfo() {
	return < -1, { }, ( ), 0, ( ), { }, 0, 0, (), (), (), (), [ ], ( )>;
}                    

//
// Find the intersection of the variables introduced in all the "or layers"
// TODO: How strict should we be with the types here? It is probably wise to "join" any
// inferred types somehow, otherwise we can leave them for the typechecker to sort out, but
// if they are inferred this could lead to some odd circumstances.
public ScopeInfo mergeOrLayers(ScopeInfo scopeInfo, list[ScopeItemId] orLayers, ScopeItemId intoLayer) {
	set[ScopeItemId] introducedItems = { vi | vi <- scopeInfo.scopeRel[head(orLayers)], VariableItem(vn,vt,_) := scopeInfo.scopeItemMap[vi] };
	println("Introduced items numbered <introducedItems>");
	for (oritem <- tail(orLayers)) {
		set[ScopeItemId] sharedItems = { };
		for (li <- introducedItems, ri <- scopeInfo.scopeRel[oritem], 
			 VariableItem(vn,_,_) := scopeInfo.scopeItemMap[li], VariableItem(vn,_,_) := scopeInfo.scopeItemMap[ri]) {
			sharedItems += li;
		}
		println("Found shared items <sharedItems>");
		introducedItems = sharedItems;
	}

	// Finally, inject them into the intoLayer
	println("Pushing layer <scopeInfo.scopeItemMap[intoLayer]>");
	scopeInfo = pushScope(intoLayer, scopeInfo);
	for (oritem <- introducedItems)
		if(VariableItem(vn,vt,_) := scopeInfo.scopeItemMap[oritem])
			scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(vn, vt, false, scopeInfo.scopeItemMap[oritem]@at, scopeInfo),[<true,scopeInfo.scopeItemMap[oritem]@at>]));
	println("Popping layer <scopeInfo.scopeItemMap[intoLayer]>");				
	scopeInfo = popScope(scopeInfo);
	println("Back to layer <scopeInfo.scopeItemMap[scopeInfo.currentScope]>");

	return scopeInfo;
}

public ScopeInfo addItemUse(ScopeInfo scopeInfo, ScopeItemId scopeItem, loc l) {
	if (l in scopeInfo.itemUses)
		scopeInfo.itemUses[l] = scopeInfo.itemUses[l] + scopeItem;
	else
		scopeInfo.itemUses += (l : { scopeItem });
	return scopeInfo;
}

public ScopeInfo addItemUses(ScopeInfo scopeInfo, set[ScopeItemId] scopeItems, loc l) {
	if (l in scopeInfo.itemUses)
		scopeInfo.itemUses[l] += scopeItems;
	else
		scopeInfo.itemUses += (l : scopeItems );
	return scopeInfo;
}

public ScopeInfo addScopeError(ScopeInfo scopeInfo, loc l, str msg) {
	if (l in scopeInfo.scopeErrorMap)
		scopeInfo.scopeErrorMap[l] = scopeInfo.scopeErrorMap[l] + msg;
	else
		scopeInfo.scopeErrorMap += (l : { msg } );
	return scopeInfo;
}

public AddedItemPair addScopeLayer(ScopeItem si, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = scopeInfo.itemLocations + <l,scopeInfo.nextScopeId>;
	scopeInfo = ((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel];
	return <scopeInfo,newItemId>;				
}

public AddedItemPair addScopeLayerWithParent(ScopeItem si, ScopeItemId parentId, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeRel newScopeRel = scopeInfo.scopeRel + <parentId, scopeInfo.nextScopeId>;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = scopeInfo.itemLocations + <l,scopeInfo.nextScopeId>;
	scopeInfo = (((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
	return <scopeInfo,newItemId>;				
}

public AddedItemPair addScopeItem(ScopeItem si, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = scopeInfo.itemLocations + <l,scopeInfo.nextScopeId>;
	scopeInfo = (((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel]);
	return <scopeInfo,newItemId>;				
}

public AddedItemPair addScopeItemWithParent(ScopeItem si, ScopeItemId parentId, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeRel newScopeRel = scopeInfo.scopeRel + <parentId, scopeInfo.nextScopeId>;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationRel newILRel = scopeInfo.itemLocations + <l,scopeInfo.nextScopeId>;
	scopeInfo = (((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILRel])[scopeRel = newScopeRel];
	return <scopeInfo,newItemId>;				
}

public ScopeItemId getLayerAtLocation(loc l, ScopeInfo scopeInfo) {
	if (l in domain(scopeInfo.itemLocations)) {
		set[ScopeItemId] layers = { si | si <- scopeInfo.itemLocations[l], isLayer(scopeInfo.scopeItemMap[si]) };
		if (size(layers) == 1)
			return getOneFrom(layers);
		else 
			throw "getLayerAtLocation: Error, trying to retrieve layer item from location with either 0 or more than 1 associated layer.";	
	} else {
		throw "getLayerAtLocation: Error, trying to retrieve item from unassociated location.";
	}
}

public ScopeInfo updateScopeItem(ScopeItem si, ScopeItemId idToUpdate, ScopeInfo scopeInfo) {
	return scopeInfo[scopeItemMap = scopeInfo.scopeItemMap + (idToUpdate : si)];				
}

public ScopeItem getScopeItem(ScopeItemId id, ScopeInfo scopeInfo) {
	return scopeInfo.scopeItemMap[id];
}
	
// DEPRECATED...
public ScopeUpdatePair changeCurrentScope(ScopeItemId newScopeId, ScopeInfo scopeInfo) {
	int oldScopeId = scopeInfo.currentScope;
	return < scopeInfo[currentScope = newScopeId], oldScopeId >;
}
              
//
// Pretty printers for scope information
//
public str prettyPrintSI(ScopeItem si) {
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

public set[ScopeItemId] filterNamesForNamespace(ScopeInfo scopeInfo, set[ScopeItemId] scopeItems, Namespace namespace) {
	set[ScopeItemId] filteredItems = { };
	for (itemId <- scopeItems) {
		switch(namespace) {
			case ModuleName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case ModuleItem(_,_) : filteredItems += itemId;
				}	
			}
			
			case LabelName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case LabelItem(_,_) : filteredItems += itemId;
				}	
			}
			
			case FCVName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case FunctionItem(_,_,_,_,_,_) : filteredItems += itemId;
					case VariableItem(_,_,_) : filteredItems += itemId;
					case FormalParameterItem(_,_,_) : filteredItems += itemId;
					case ConstructorItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
					
			case TypeName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case ADTItem(_,_,_,_) : filteredItems += itemId;
					case AliasItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
			
			case AnnotationName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case AnnotationItem(_,_,_,_) : filteredItems += itemId;
				}
			}
			
			case RuleName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
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
public set[ScopeItemId] getItemsForNameWBound(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x, set[Namespace] containingNamespaces, bool funBounded, bool modBounded) {
	set[ScopeItemId] foundItems = { };

	// Handle qualified names. If this is not a module name, then we try to switch into the scope
	// of the module named in the qualified name. If we are looking for modules, handle this
	// below, since modules do have qualified names, so we don't want to deconstruct the name
	// first.	
	if (ModuleName() notin containingNamespaces) {
		if (RCompoundName(nl) := x) {
			RName moduleName = (size(tail(nl)) == 1) ? RSimpleName(head(tail(reverse(nl)))) : RCompoundName(reverse(tail(reverse(nl)))); // There should be a better way to do this
			x = RSimpleName(head(tail(nl,1)));
			set[ScopeItemId] mods = getModuleItemsForName(scopeInfo, moduleName);
			if (size(mods) == 1) {
				// Redirect to module scope
				return getItemsForNameWBound(scopeInfo, scopeInfo.scopeItemMap[getOneFrom(mods)].parentId, x, containingNamespaces, funBounded, modBounded);					
			} else if (size(mods) > 1) {
				// TODO: Error, should be caught processing imports -- this means multiple modules have the same name
				// BUT: When we have parameterized modules, this could happen -- how will we distinguish?
				return { }; 
			} else {
				return { };
			}
		}
	} else if (ModuleName() in containingNamespaces && size(containingNamespaces) > 1) {
		return getItemsForNameWBound(scopeInfo, currentScopeId, x, { ModuleName() }, funBounded, modBounded) +
				getItemsForNameWBound(scopeInfo, currentScopeId, x, containingNamespaces - ModuleName(), funBounded, modBounded);
	}

	// We fall through to here IF a) the name is not a qualified name, or b) the name IS a qualified name but is
	// the name of a module. Otherwise, it is taken care of above.

	// Find all the scope items at the current level of scope that match the name we are looking for. Note that we need
	// special handling for functions and modules, since the function and module items are actually store inside the function
	// and module layers. The other layers are not named, so could never lead to a match.
	for (itemId <- scopeInfo.scopeRel[currentScopeId]) {
		switch(scopeInfo.scopeItemMap[itemId]) {
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
			case FunctionLayer(funItemId,_) : if (FunctionItem(x,_,_,_,_,_) := scopeInfo.scopeItemMap[funItemId]) foundItems += funItemId;
			case ModuleLayer(modItemId,_) : if (ModuleItem(x,_) := scopeInfo.scopeItemMap[modItemId]) foundItems += modItemId; 			
		}
	}

	// Now, filter it down based on the namespaces we are looking for
	foundItems = { f | ns <- containingNamespaces, f <- filterNamesForNamespace(scopeInfo, foundItems, ns) };
		
	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level is reached. If this is a bounded search, don't pass through 
	// function boundaries.
	if (size(foundItems) == 0) {
		switch(scopeInfo.scopeItemMap[currentScopeId]) {
			case ModuleLayer(_,parentScopeId) : if (!modBounded) foundItems = getItemsForNameWBound(scopeInfo, parentScopeId, x, containingNamespaces, funBounded,modBounded);
			case FunctionLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case PatternMatchLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case BooleanExpLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case OrLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case ClosureLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case VoidClosureLayer(_,parentScopeId) : if (!funBounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
			case BlockLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,funBounded,modBounded);
		}
	}

	// NOTE: This can be empty (for instance, when looking up a declaration of a variable that is not explicitly declared)	
	return foundItems;	
}

//
// These are specialized versions of the above function to make it easier to get back the
// right names.
//
public set[ScopeItemId] getItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { FCVName() }, false, false);
}

public set[ScopeItemId] getItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { FCVName() }, true, false);
}

public set[ScopeItemId] getItemsForNameMB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { FCVName() }, false, true);
}

public set[ScopeItemId] getAnnotationItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { AnnotationName() }, false, false);
}

public set[ScopeItemId] getAnnotationItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { AnnotationName() }, true, false);
}

public set[ScopeItemId] getAnnotationItemsForNameMB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { AnnotationName() }, false, true);
}

public set[ScopeItemId] getRuleItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { RuleName() }, false, false);
}

public set[ScopeItemId] getRuleItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { RuleName() }, true, false);
}

public set[ScopeItemId] getRuleItemsForNameMB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { RuleName() }, false, true);
}

public set[ScopeItemId] getLabelItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { LabelName() }, false, false);
}

public set[ScopeItemId] getLabelItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { LabelName() }, true, false);
}

public set[ScopeItemId] getLabelItemsForNameMB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { LabelName() }, false, true);
}

public set[ScopeItemId] getTypeItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { TypeName() }, false, false);
}

public set[ScopeItemId] getTypeItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { TypeName() }, true, false);
}

public set[ScopeItemId] getTypeItemsForNameMB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { TypeName() }, false, true);
}

public set[ScopeItemId] getModuleItemsForName(ScopeInfo scopeInfo, RName x) {
	return getItemsForNameWBound(scopeInfo, scopeInfo.topScopeItemId, x, { ModuleName() }, false, false);
}

// Get the function item that encloses a point in the code. This will navigate up to the function layer
// and then return the item id for the function item that is associated with this layer.
public ScopeItemId getEnclosingFunctionAux(ScopeInfo scopeInfo, ScopeItemId currentScope) {
	ScopeItem si = getScopeItem(currentScope, scopeInfo);
	if (FunctionLayer(itemId,_) := si) return itemId;
	if (TopLayer() := si) throw "Cannot get enclosing function at top level";
	return getEnclosingFunctionAux(scopeInfo, si.parentId);
}

public ScopeItemId getEnclosingFunction(ScopeInfo scopeInfo) {
	return getEnclosingFunctionAux(scopeInfo, scopeInfo.currentScope);
}

public RType getEnclosingFunctionType(ScopeInfo scopeInfo) {
	return getTypeForItem(scopeInfo, getEnclosingFunction(scopeInfo)); 
}

//
// TODO: This should throw an exception when the type of an untyped name (e.g., a label) is requested
//
public RType getTypeForItem(ScopeInfo scopeInfo, ScopeItemId itemId) {
	if (itemId notin scopeInfo.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";

	switch(scopeInfo.scopeItemMap[itemId]) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionItem(_,t,paramIds,_,_,_) : 
			return makeFunctionType(t,[getTypeForItem(scopeInfo, paramId) | paramId <- paramIds]);
		
		case ConstructorItem(n,tas,adtParentId,_) : return makeConstructorType(n,tas,getTypeForItem(scopeInfo,adtParentId));
		
		case ADTItem(ut,_,_,_) : return ut; // TODO: Should also extract type parameters, if needed

		case AliasItem(ut,_,_,_) : return ut; // TODO: Should also extract type parameters, if needed
		
		default : { 
			return makeVoidType(); 
		}
	}
}

alias ResultTuple = tuple[ScopeInfo scopeInfo, list[ScopeItemId] addedItems];

//
// Functions to push new scope layers and add scope items; also performs
// some error checking.
//

public ResultTuple pushNewTopScope(loc l, ScopeInfo scopeInfo) {
	AddedItemPair aipTop = addScopeLayer(TopLayer()[@at=l], l, scopeInfo);
	scopeInfo = aipTop.scopeInfo[topScopeItemId = aipTop.addedId];
	scopeInfo.scopeStack = [ aipTop.addedId ] + scopeInfo.scopeStack;
	scopeInfo.currentScope = aipTop.addedId;
	return <scopeInfo,[aipTop.addedId]>; 	
}

public ResultTuple pushNewModuleScope(RName moduleName, loc l, ScopeInfo scopeInfo) {
	println("Adding module layer to parent <scopeInfo.scopeItemMap[scopeInfo.currentScope]>");
	AddedItemPair aip = addScopeLayerWithParent(ModuleLayer(-1, scopeInfo.currentScope)[@at=l], scopeInfo.currentScope, l, scopeInfo);
	aip.scopeInfo.scopeStack = [ aip.addedId ] + aip.scopeInfo.scopeStack;
	aip.scopeInfo.currentScope = aip.addedId;
	AddedItemPair aip2 = addScopeItemWithParent(ModuleItem(moduleName, aip.scopeInfo.currentScope)[@at=l], aip.scopeInfo.currentScope, l, aip.scopeInfo);
	aip2.scopeInfo.scopeItemMap[aip.addedId].itemId = aip2.addedId;
	return <aip2.scopeInfo,[aip.addedId,aip2.addedId]>; 	
}

public ResultTuple pushNewBooleanScope(loc l, ScopeInfo scopeInfo) {
	AddedItemPair aip = addScopeLayer(BooleanExpLayer(scopeInfo.currentScope)[@at=l], l, scopeInfo);
	aip.scopeInfo.scopeStack = [ aip.addedId ] + aip.scopeInfo.scopeStack;
	aip.scopeInfo.currentScope = aip.addedId;
	return <aip.scopeInfo,[aip.addedId]>; 	
}

public ResultTuple pushNewOrScope(loc l, ScopeInfo scopeInfo) {
	AddedItemPair aip = addScopeLayer(OrLayer(scopeInfo.currentScope)[@at=l], l, scopeInfo);
	aip.scopeInfo.scopeStack = [ aip.addedId ] + aip.scopeInfo.scopeStack;
	aip.scopeInfo.currentScope = aip.addedId;
	return <aip.scopeInfo,[aip.addedId]>; 	
}

public ResultTuple pushNewBlockScope(loc l, ScopeInfo scopeInfo) {
	AddedItemPair aip = addScopeLayer(BlockLayer(scopeInfo.currentScope)[@at=l], l, scopeInfo);
	aip.scopeInfo.scopeStack = [ aip.addedId ] + aip.scopeInfo.scopeStack;
	aip.scopeInfo.currentScope = aip.addedId;
	return <aip.scopeInfo,[aip.addedId]>; 	
}

public ResultTuple pushNewPatternMatchScope(loc l, ScopeInfo scopeInfo) {
	AddedItemPair aip = addScopeLayer(PatternMatchLayer(scopeInfo.currentScope)[@at=l], l, scopeInfo);
	aip.scopeInfo.scopeStack = [ aip.addedId ] + aip.scopeInfo.scopeStack;
	aip.scopeInfo.currentScope = aip.addedId;
	return <aip.scopeInfo,[aip.addedId]>; 	
}

public ScopeInfo popScope(ScopeInfo scopeInfo) {
	if (size(scopeInfo.scopeStack) == 0) throw "popScope: Scope Stack is empty, cannot pop!";
	scopeInfo.scopeStack = tail(scopeInfo.scopeStack);
	scopeInfo.currentScope = head(scopeInfo.scopeStack);
	return scopeInfo;
}

public ScopeInfo pushScope(ScopeItemId newScope, ScopeInfo scopeInfo) {
	scopeInfo.scopeStack = [ newScope ] + scopeInfo.scopeStack;
	scopeInfo.currentScope = newScope;
	return scopeInfo;
}

public ResultTuple pushNewFunctionScopeAt(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(FunctionLayer(-1, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	aipLayer.scopeInfo.scopeStack = [ aipLayer.addedId ] + aipLayer.scopeInfo.scopeStack;
	aipLayer.scopeInfo.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	scopeInfo = aipLayer.scopeInfo;
	list[ScopeItemId] paramIds = [ ];
	set[RName] namesSeen = { functionName };
	for (tuple[RName pname, RType ptype, loc ploc, loc nloc] pt <- params) {
		if (pt.pname != RSimpleName("") && pt.pname in namesSeen) {
			scopeInfo = addScopeError(scopeInfo, pt.nloc, "Illegal redefinition of <prettyPrintName(pt.pname)>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addScopeItemWithParent(FormalParameterItem(pt.pname, pt.ptype, scopeInfo.currentScope)[@at=pt.ploc], scopeInfo.currentScope, pt.ploc, scopeInfo);
		paramIds += aipParam.addedId; scopeInfo = aipParam.scopeInfo;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addScopeItemWithParent(FunctionItem(functionName, retType, paramIds, throwsTypes, isPublic, scopeInfo.currentScope)[@at=l], scopeInfo.currentScope, l, scopeInfo);
	aipItem.scopeInfo.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;
	
	return <aipItem.scopeInfo,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewFunctionScope(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return pushNewFunctionScopeAt(functionName, retType, params, throwsTypes, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple pushNewFunctionScopeAtTop(RName functionName, RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, list[RType] throwsTypes, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return pushNewFunctionScopeAt(functionName, retType, params, throwsTypes, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
} 

public ResultTuple pushNewClosureScopeAt(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(ClosureLayer(-1, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	aipLayer.scopeInfo.scopeStack = [ aipLayer.addedId ] + aipLayer.scopeInfo.scopeStack;
	aipLayer.scopeInfo.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	scopeInfo = aipLayer.scopeInfo;
	list[ScopeItemId] paramIds = [ ];
	for (pt <- params) {
		if (pt.pname in namesSeen) {
			scopeInfo = addScopeError(scopeInfo, pt.nloc, "Illegal redefinition of <n>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addScopeItemWithParent(FormalParameterItem(pt.pname, pt.ptype, scopeInfo.currentScope)[@at=pt.ploc], scopeInfo.currentScope, pt.ploc, scopeInfo);
		paramIds += aip.addedId; scopeInfo = aip.scopeInfo;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addScopeItemWithParent(ClosureItem(retType, paramIds, scopeInfo.currentScope)[@at=l], scopeInfo.currentScope, l, scopeInfo);
	aipItem.scopeInfo.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	return <aipItem.scopeInfo,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewClosureScope(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo) {
	return pushNewClosureScopeAt(retType, params, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple pushNewClosureScopeAtTop(RType retType, list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo) {
	return pushNewClosureScopeAt(retType, params, l, scopeInfo, scopeInfo.topScopeItemId);
} 

public ResultTuple pushNewVoidClosureScopeAt(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	// Create the function layer, adding it to the scope stack and making it the current scope
	AddedItemPair aipLayer = addScopeLayerWithParent(VoidClosureLayer(-1, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	aipLayer.scopeInfo.scopeStack = [ aipLayer.addedId ] + aipLayer.scopeInfo.scopeStack;
	aipLayer.scopeInfo.currentScope = aipLayer.addedId;

	// Create scope items for each of the parameters
	scopeInfo = aipLayer.scopeInfo;
	list[ScopeItemId] paramIds = [ ];
	for (pt <- params) {
		if (pt.pname in namesSeen) {
			scopeInfo = addScopeError(scopeInfo, pt.nloc, "Illegal redefinition of <n>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addScopeItemWithParent(FormalParameterItem(pt.pname, pt.ptype, scopeInfo.currentScope)[@at=pt.ploc], scopeInfo.currentScope, pt.ploc, scopeInfo);
		paramIds += aip.addedId; scopeInfo = aip.scopeInfo;
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addScopeItemWithParent(VoidClosureItem(paramIds, scopeInfo.currentScope)[@at=l], scopeInfo.currentScope, l, scopeInfo);
	aipItem.scopeInfo.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	return <aipItem.scopeInfo,[aipLayer.addedId] + [aipItem.addedId] + paramIds>;
}

public ResultTuple pushNewVoidClosureScope(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo) {
	return pushNewVoidClosureScopeAt(params, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple pushNewVoidClosureScopeAtTop(list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params, loc l, ScopeInfo scopeInfo) {
	return pushNewVoidClosureScopeAt(params, l, scopeInfo, scopeInfo.topScopeItemId);
} 

public ResultTuple addAliasToScopeAt(RType aliasType, RType aliasedType, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(AliasItem(aliasType, aliasedType, isPublic, scopeToUse), scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addAliasToScope(RType aliasType, RType aliasedType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addAliasToScopeAt(aliasType, aliasedType, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addAliasToTopScope(RType aliasType, RType aliasedType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addAliasToScopeAt(aliasType, aliasedType, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
}

public ResultTuple addVariableToScopeAt(RName varName, RType varType, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(VariableItem(varName, varType, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addVariableToScope(RName varName, RType varType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addVariableToScopeAt(varName, varType, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addVariableToTopScope(RName varName, RType varType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addVariableToScopeAt(varName, varType, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
}

public ResultTuple addADTToScopeAt(RType adtName, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(ADTItem(adtName, { }, isPublic, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
	return <scopeInfo, []>;
}

public ResultTuple addADTToScope(RType adtName, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addADTToScopeAt(adtName, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addADTToTopScope(RType adtName, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addADTToScopeAt(adtName, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
}

// TODO: Need to get ADT that goes with the adtType and associate this constructor with it, as well as adding this constructor
// ID to the list maintained as part of the ADT.
public ResultTuple addConstructorToScopeAt(RName constructorName, list[RNamedType] constructorArgs, ScopeItemId adtItem, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(ConstructorItem(constructorName, constructorArgs, adtItem, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addConstructorToScope(RName constructorName, list[RNamedType] constructorArgs, ScopeItemId adtItem, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addConstructorToTopScope(RName constructorName, list[RNamedType] constructorArgs, ScopeItemId adtItem, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addConstructorToScopeAt(constructorName, constructorArgs, adtItem, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
}

public ResultTuple addAnnotationToScopeAt(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(AnnotationItem(annotationName, annotationType, onType, isPublic, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addAnnotationToScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addAnnotationToTopScope(RName annotationName, RType annotationType, RType onType, bool isPublic, loc l, ScopeInfo scopeInfo) {
	return addAnnotationToScopeAt(annotationName, annotationType, onType, isPublic, l, scopeInfo, scopeInfo.topScopeItemId);
}

public ResultTuple addRuleToScopeAt(RName ruleName, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(RuleItem(ruleName, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addRuleToScope(RName ruleName, loc l, ScopeInfo scopeInfo) {
	return addRuleToScopeAt(ruleName, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addRuleToTopScope(RName ruleName, loc l, ScopeInfo scopeInfo) {
	return addRuleToScopeAt(ruleName, l, scopeInfo, scopeInfo.topScopeItemId);
}

public ResultTuple addLabelToScopeAt(RName labelName, loc l, ScopeInfo scopeInfo, ScopeItemId scopeToUse) {
	AddedItemPair aip = addScopeItemWithParent(LabelItem(labelName, scopeToUse)[@at=l], scopeToUse, l, scopeInfo);
	return <aip.scopeInfo,[aip.addedId]>;
}

public ResultTuple addLabelToScope(RName labelName, loc l, ScopeInfo scopeInfo) {
	return addLabelToScopeAt(labelName, l, scopeInfo, scopeInfo.currentScope);
}

public ResultTuple addLabelToTopScope(RName labelName, loc l, ScopeInfo scopeInfo) {
	return addLabelToScopeAt(labelName, l, scopeInfo, scopeInfo.topScopeItemId);
}

// Projectors/combinators to work with result tuples
public ScopeInfo justScopeInfo(ResultTuple result) {
	return result.scopeInfo;
}

public ResultTuple addScopeItemUses(ResultTuple result, list[tuple[bool flagUse, loc useloc]] useLocs) {
	ScopeInfo scopeInfo = result.scopeInfo;
	for (n <- [0..size(useLocs)-1]) if (useLocs[n].flagUse) scopeInfo = addItemUse(scopeInfo, result.addedItems[n], useLocs[n].useloc);
	return <scopeInfo, result.addedItems>;
}

// TODO: Extend this to also account for type parameters
// TODO: Add locations of conflicting types
public ResultTuple checkForDuplicateAliasesBounded(ResultTuple result, loc nloc, bool modBounded) {
	ScopeInfo scopeInfo = result.scopeInfo;
	ScopeItemId aliasId = result.addedItems[0];
	RName aliasName = getUserTypeName(scopeInfo.scopeItemMap[aliasId].aliasType);
	set[ScopeItemId] otherItems = { };
	if (modBounded) {
		otherItems = getTypeItemsForNameMB(scopeInfo, scopeInfo.currentScope, aliasName) - aliasId;
	} else {
		otherItems = getTypeItemsForName(scopeInfo, scopeInfo.currentScope, aliasName) - aliasId;
	}
	for (oi <- otherItems) {
		switch(scopeInfo.scopeItemMap[oi]) {
			case AliasItem(aname,atype,_,_) : {
				if (unrollAlias(atype,scopeInfo) != unrollAlias(scopeInfo.scopeItemMap[aliasId].aliasedType,scopeInfo)) {
					scopeInfo = addScopeError(scopeInfo, nloc, "Scope Error: Definition of alias <prettyPrintName(aliasName)> conflicts with another alias of the same name");	
				}
			}
			case ADTItem(_,_,_,_) : {
				scopeInfo = addScopeError(scopeInfo, nloc, "Scope Error: Definition of alias <prettyPrintName(aliasName)> conflicts with an ADT of the same name");
			} 
		}
	}
	return <scopeInfo, result.addedItems>;
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
	ScopeInfo scopeInfo = result.scopeInfo;
	ScopeItemId newFun = result.addedItems[1];
	ScopeItem newFunItem = getScopeItem(newFun, scopeInfo);
	RType newFunItemType = getTypeForItem(scopeInfo, newFun);
	list[RType] newFunItemArgs = getFunctionArgumentTypes(newFunItemType);

	set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeInfo.currentScope, newFunItem.functionName) - newFun;

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		ScopeItem overlapItem = getScopeItem(oid, scopeInfo);
		RType overlapItemType = getTypeForItem(scopeInfo, oid);
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
					scopeInfo = addScopeError(scopeInfo, nloc, "Overlapping overload of function <newFunItem.functionName> declared");
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
					scopeInfo = addScopeError(scopeInfo, nloc, "Overlapping overload of function <newFunItem.functionName> declared");
				}
			}
		} else {
			scopeInfo = addScopeError(scopeInfo, nloc, "Function name clashes with another declaration: <prettyPrintSI(overlapItem)>");
		}
	}
	return <scopeInfo, result.addedItems>;
}

// TODO: This is a copy of the above function, with the addition of scope errors replaced
// with a return of true, and the final return replaced with a return of false. Factor out
// the common code somehow. This is needed because we have situations where we need to check
// for overlap before the scope item is addded, along with situations where we want to check
// for overlap after the scope item is added.
public bool willFunctionOverlap(RName funName, RType funType, ScopeInfo scopeInfo, ScopeItemId scopeToCheck) {
	list[RType] funArgs = getFunctionArgumentTypes(funType);

	set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeToCheck, funName);

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		ScopeItem overlapItem = getScopeItem(oid, scopeInfo);
		RType overlapItemType = getTypeForItem(scopeInfo, oid);
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
	ScopeInfo scopeInfo = result.scopeInfo;
	ScopeItemId newCons = result.addedItems[0];
	ScopeItem newConsItem = getScopeItem(newCons, scopeInfo);
	RType newConsItemType = getTypeForItem(scopeInfo, newCons);
	list[RType] newConsItemArgs = getConstructorArgumentTypes(newConsItemType);

	set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeInfo.currentScope, newConsItem.constructorName) - newCons;

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		ScopeItem overlapItem = getScopeItem(oid, scopeInfo);
		RType overlapItemType = getTypeForItem(scopeInfo, oid);
		if (isConstructorType(overlapItemType)) {
			list[RType] consItemArgs = getConstructorArgumentTypes(overlapItemType);
			if (size(consItemArgs) == size(newConsItemArgs)) {
				bool foundIncomparable = false;
	
				for (n <- domain(consItemArgs)) {
					RType t1 = consItemArgs[n]; RType t2 = newConsItemArgs[n];
					if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
				}
				
				if (!foundIncomparable) {
					scopeInfo = addScopeError(scopeInfo, nloc, "Overlapping overload of constructor <newConsItem.constructorName> declared");
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
					scopeInfo = addScopeError(scopeInfo, nloc, "Overlapping overload of constructor <newConsItem.constructorName> declared");
				}
			}
		} else {
			scopeInfo = addScopeError(scopeInfo, nloc, "Constructor name clashes with another declaration: <prettyPrintSI(overlapItem)>");
		}
	}
	return <scopeInfo, result.addedItems>;
}

// TODO: This is a copy of the above function, with the addition of scope errors replaced
// with a return of true, and the final return replaced with a return of false. Factor out
// the common code somehow. This is needed because we have situations where we need to check
// for overlap before the scope item is addded, along with situations where we want to check
// for overlap after the scope item is added.
public bool willConstructorOverlap(RName conName, RType conType, ScopeInfo scopeInfo, ScopeItemId scopeToCheck) {
	list[RType] consArgs = getConstructorArgumentTypes(conType);
	set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeToCheck, conName);

	for (oid <- potentialOverlaps) {
		// Get back information for each of the existing overlapping items
		ScopeItem overlapItem = getScopeItem(oid, scopeInfo);
		RType overlapItemType = getTypeForItem(scopeInfo, oid);
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
RType unrollAlias(RType aliasedType, ScopeInfo scopeInfo) {
	return aliasedType;
}

public ScopeInfo markReturnType(RType t, Statement s, ScopeInfo scopeInfo) {
	return scopeInfo[returnTypeMap = scopeInfo.returnTypeMap + ( s@\loc : t )];
}

// TODO: We may want to allow repeated annotations of the same name and type
public ResultTuple checkForDuplicateAnnotationsBounded(ResultTuple result, loc nloc, bool modBounded) {
	ScopeInfo scopeInfo = result.scopeInfo;
	ScopeItemId annotationId = result.addedItems[0];
	RName annotationName = scopeInfo.scopeItemMap[annotationId].annotationName;
	set[ScopeItemId] otherItems = { };
	if (modBounded) {
		otherItems = getAnnotationItemsForNameMB(scopeInfo, scopeInfo.currentScope, annotationName) - annotationId;
	} else {
		otherItems = getAnnotationItemsForName(scopeInfo, scopeInfo.currentScope, annotationName) - annotationId;
	}
	if (size(otherItems) > 0) {
		scopeInfo = addScopeError(scopeInfo, nloc, "Scope Error: Definition of annotation <prettyPrintName(annotationName)> conflicts with another annotation of the same name");	
	}
	return <scopeInfo, result.addedItems>;
}

public ResultTuple checkForDuplicateAnnotations(ResultTuple result, loc nloc) {
	return checkForDuplicateAnnotationsBounded(result, nloc, false);
}

public ResultTuple checkForDuplicateAnnotationsInModule(ResultTuple result, loc nloc) {
	return checkForDuplicateAnnotationsBounded(result, nloc, true);
}

public ResultTuple checkForDuplicateRulesBounded(ResultTuple result, loc nloc, bool modBounded) {
	ScopeInfo scopeInfo = result.scopeInfo;
	ScopeItemId ruleId = result.addedItems[0];
	RName ruleName = scopeInfo.scopeItemMap[ruleId].ruleName;
	set[ScopeItemId] otherItems = { };
	if (modBounded) {
		otherItems = getRuleItemsForNameMB(scopeInfo, scopeInfo.currentScope, ruleName) - ruleId;
	} else {
		otherItems = getRuleItemsForName(scopeInfo, scopeInfo.currentScope, ruleName) - ruleId;
	}
	if (size(otherItems) > 0) {
		scopeInfo = addScopeError(scopeInfo, nloc, "Scope Error: Definition of rule <prettyPrintName(ruleName)> conflicts with another rule of the same name");	
	}
	return <scopeInfo, result.addedItems>;
}

public ResultTuple checkForDuplicateRules(ResultTuple result, loc nloc) {
	return checkForDuplicateRulesBounded(result, nloc, false);
}

public ResultTuple checkForDuplicateRulesInModule(ResultTuple result, loc nloc) {
	return checkForDuplicateRulesBounded(result, nloc, true);
}

public bool inBoolLayer(ScopeInfo scopeInfo) {
	if (BooleanExpLayer(_) := scopeInfo.scopeItemMap[scopeInfo.currentScope] || OrLayer(_) := scopeInfo.scopeItemMap[scopeInfo.currentScope])
		return true;
	return false;
}

public ScopeInfo consolidateADTDefinitions(ScopeInfo scopeInfo, RName moduleName) {
	// Get back the ID for the name of the module being checked -- there should be only one matching
	// item. TODO: We may want to verify that here.
	ScopeItemId moduleLayerId = getOneFrom(getModuleItemsForName(scopeInfo, moduleName));
	
	// Step 1: Pick out all ADT definitions in the loaded scope information (i.e., all ADTs defined
	// in either the loaded module or its direct imports)
	set[ScopeItemId] adtIDs = { sid | sid <- scopeInfo.scopeRel[scopeInfo.topScopeItemId], ADTItem(_,_,_,_) := scopeInfo.scopeItemMap[sid] } +
							  { sid | sid <- scopeInfo.scopeRel[scopeInfo.scopeItemMap[moduleLayerId].parentId], ADTItem(_,_,_,_) := scopeInfo.scopeItemMap[sid] };
							  
	// Step 2: Group these based on the name of the ADT
	rel[RName adtName, ScopeItemId adtItemId] nameXADTItem = { < getUserTypeName(n), sid > | sid <- adtIDs, ADTItem(n,_,_,_) := scopeInfo.scopeItemMap[sid] };
	
	// Step 3: Gather together all the constructors for the ADTs
	rel[ScopeItemId adtItemId, ScopeItemId consItemId] adtItemXConsItem = { < sid, cid > | sid <- range(nameXADTItem), cid <- domain(scopeInfo.scopeItemMap), ConstructorItem(_,_,sid,_) := scopeInfo.scopeItemMap[cid] };
	 
	// Step 4: Now, directly relate the ADT names to the available constructors
	rel[RName adtName, ScopeItemId consItemId] nameXConsItem = nameXADTItem o adtItemXConsItem;
	
	// Step 5: Put these into the needed form for the internal ADT map
	for (n <- domain(nameXADTItem))
		scopeInfo.adtMap[n] = < { sid | sid <- nameXADTItem[n] }, { cid | cid <- nameXConsItem[n] } >;
		
	// Finally, return the scopeinfo with the consolidated ADT information
	return scopeInfo;
}

