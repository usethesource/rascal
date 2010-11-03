@bootstrapParser
module rascal::checker::SymbolTable

import rascal::checker::Types;
import rascal::checker::Signature;
import rascal::checker::SubTypes;
import rascal::syntax::RascalRascal;

import List;
import IO;
import Set;
import Relation;
import Map;
import ParseTree;

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
	| ADTItem(RType adtType, bool isPublic, STItemId parentId) 
	| AnnotationItem(RName annotationName, RType annoType, RType onType, bool isPublic, STItemId parentId) 
	| RuleItem(RName ruleName, STItemId parentId)
	| TypeVariableItem(RType typeVar, STItemId parentId)
	| FieldItem(RName fieldName, STItemId parentId)
;

public bool itemHasName(STItem item) {
        return ( ModuleItem(_,_) := item || FunctionItem(_,_,_,_,_,_) := item || VariableItem(_,_,_) := item || FormalParameterItem(_,_,_) := item ||
       	         LabelItem(_,_) := item || AliasItem(_,_,_,_) := item || ConstructorItem(_,_,_,_) := item || ADTItem(_,_,_) := item ||
		 AnnotationItem(_,_,_,_,_) := item || RuleItem(_,_) := item || TypeVariableItem(_,_) := item || FieldItem(_,_) := item );
}

public RName getItemName(STItem item) {
        switch(item) {
		case ModuleItem(n,_) : return n;
		case FunctionItem(n,_,_,_,_,_) : return n;
		case VariableItem(n,_,_) : return n;
		case FormalParameterItem(n,_,_) : return n;
		case LabelItem(n,_) : return n;
		case AliasItem(tn,_,_,_) : return getUserTypeName(tn);
		case ConstructorItem(n,_,_,_) : return n;
		case ADTItem(tn,_,_) : return getUserTypeName(tn);
		case AnnotationItem(n,_,_,_,_) : return n;
		case RuleItem(n,_) : return n;
		case TypeVariableItem(RTypeVar(RFreeTypeVar(n)),_) : return n;
		case TypeVariableItem(RTypeVar(RBoundTypeVar(n,_)),_) : return n;
		case FieldItem(n,_) : return n;
		default : throw "Item does not have a name, use itemHasHame(STItem item) to check first to ensure the item has a name";
	}
}

//
// Is this a symbol table layer (true) or an actual symbol table entry (false)?
//
public bool isLayer(STItem si) {
       return (TopLayer() := si || ModuleLayer(_,_) := si || FunctionLayer(_,_) := si || PatternMatchLayer(_) := si ||
               BooleanExpLayer(_) := si || OrLayer(_) := si || ClosureLayer(_,_) := si || VoidClosureLayer(_,_) := si ||
               BlockLayer(_) := si);
}

// Is this a symbol table item (true) or a scoping layer (false)?
public bool isItem(STItem si) {
	return !isLayer(si);
}
				
public bool isFunctionItem(STItem si) {
	return (FunctionItem(_,_,_,_,_,_) := si);
}

public bool isConstructorItem(STItem si) {
	return (ConstructorItem(_,_,_,_) := si);
}

public bool isFunctionOrConstructorItem(STItem si) {
	return isFunctionItem(si) || isConstructorItem(si);
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
	| TypeVarName()
	| FieldName()
;

// TODO: Should be able to use STItemMap here, but if I try it doesn't work, something must be
// wrong with the current alias expansion algorithm; this is the same with ItemLocationMap as well
// for itemLocations...
alias ScopeRel = rel[STItemId scopeId, STItemId itemId];
alias ScopeNamesRel = rel[STItemId scopeId, RName itemName, STItemId itemId];
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
alias SymbolTable = 
	tuple[
                STItemId topSTItemId,
		STItemId currentModule,	
                rel[STItemId scopeId, STItemId itemId] scopeRel,
	        rel[STItemId scopeId, RName itemName, STItemId itemId] scopeNames,
		ItemUses itemUses, 
		STItemId nextScopeId, 
		map[STItemId, STItem] scopeItemMap, 
                rel[loc, STItemId] itemLocations, 
                STItemId currentScope, 
                int freshType,
                map[loc, set[str]] scopeErrorMap, 
                map[int, RType] inferredTypeMap,
                map[int, RType] typeVarMap, 
                map[loc, RType] returnTypeMap,
		map[loc, RType] itBinder, 
		list[STItemId] scopeStack, 
		map[RName adtName,tuple[set[STItemId] adtItems,set[STItemId] consItems] adtInfo] adtMap,
		rel[RName annName, RType annType, RType annOn] annRel
        ];

alias AddedItemPair = tuple[SymbolTable symbolTable, STItemId addedId];
alias ScopeUpdatePair = tuple[SymbolTable symbolTable, STItemId oldScopeId];
                        
// Create an empty symbol table                        
public SymbolTable createNewSymbolTable() {
	return < -1, -1, { }, { }, ( ), 0, ( ), { }, 0, 0, (), (), (), (), (), [ ], ( ), { }>;
}                    

// Given a number of different OR scope layers in the symbol table, find the subset of
// variables declared in all the layers. Note that, at this point, we just pick one of
// the identical variable as a representative, since they are all considered the same.
// TODO: Is this true? Should also check declared types
// TODO: Should we do anything here for parameterized types? For instance, if one or
// branch introduced one set of bindings, and the other introduced (somehow) a different
// set of bindings?
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
	symbolTable = pushScope(intoLayer, symbolTable);
	for (oritem <- introducedItems)
		if(VariableItem(vn,vt,_) := symbolTable.scopeItemMap[oritem])
			symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(vn, vt, false, symbolTable.scopeItemMap[oritem]@at, symbolTable),[<true,symbolTable.scopeItemMap[oritem]@at>]));
	symbolTable = popScope(symbolTable);

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
		if (FunctionItem(n,_,_,_,_,_) := si || ModuleItem(n,_) := si) {
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

		case ModuleItem(x,_) : return "ModuleItem: <prettyPrintName(x)>";
		
		case FunctionItem(x,t,ags,_,_,_) : return "FunctionItem: <prettyPrintType(t)> <prettyPrintName(x)>(<joinList(ags,prettyPrintSI,",","")>)";

		case ClosureItem(t,ags,_,_) : return "ClosureItem: <prettyPrintType(t)> (<joinList(ags,prettyPrintSI,",","")>)";
		
		case VoidClosureItem(ags,_,_) : return "VoidClosureItem: (<joinList(ags,prettyPrintSI,",","")>)";

		case VariableItem(x,t,_) : return "VariableItem: <prettyPrintType(t)> <prettyPrintName(x)>";

		case TypeVariableItem(t,_) : return "TypeVariableItem: <prettyPrintType(t)>";
		
		case FormalParameterItem(x,t,_) : return "FormalParameterItem: <prettyPrintType(t)> <prettyPrintName(x)>";
		
		case LabelItem(x,_) : return "LabelItem: <prettyPrintName(x)>";

		case AliasItem(tn,ta,_,_) : return "AliasItem: <prettyPrintType(tn)> = <prettyPrintType(ta)>";
			
		case ConstructorItem(cn,tas,_,_) : 	return "Constructor: <prettyPrintName(cn)>(<prettyPrintNamedTypeList(tas)>)";
		
		case ADTItem(ut,_,_) : return "ADT: <prettyPrintType(ut)>";
		 			
		case AnnotationItem(x,atyp,otyp,_,_) : return "Annotation: <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)>";
		
		case RuleItem(x,_) : return "Rule: <prettyPrintName(x)>";

		case FieldItem(x,_) : return "Field: <prettyPrintName(x)>";
	}
}

public set[STItemId] filterNamesForNamespace(SymbolTable symbolTable, set[STItemId] scopeItems, Namespace namespace) {
       switch(namespace) {
          case ModuleName() : return { i | i <- scopeItems, ModuleItem(_,_) := symbolTable.scopeItemMap[i] };
	  
	  case LabelName() : return { i | i <- scopeItems, LabelItem(_,_) := symbolTable.scopeItemMap[i] };
	         			       
          case FCVName() : return { i | i <- scopeItems, si := symbolTable.scopeItemMap[i], FunctionItem(_,_,_,_,_,_) := si || VariableItem(_,_,_) := si || 
	       		   	        FormalParameterItem(_,_,_) := si || ConstructorItem(_,_,_,_) := si };
					
	  case TypeName() : return { i | i <- scopeItems, si := symbolTable.scopeItemMap[i], ADTItem(_,_,_) := si || AliasItem(_,_,_,_) := si };

	  case TypeVarName() : return { i | i <- scopeItems, TypeVariableItem(_,_) := symbolTable.scopeItemMap[i] };
			
	  case AnnotationName() : return { i | i <- scopeItems, AnnotationItem(_,_,_,_) := symbolTable.scopeItemMap[i] };

	  case RuleName() : return { i | i <- scopeItems, RuleItem(_,_) := symbolTable.scopeItemMap[i] };

	  case FieldName() : return { i | i <- scopeItems, FieldItem(_,_) := symbolTable.scopeItemMap[i] };
	}

	throw "Unmatched namespace in filterNamesForNamespace: <namespace>";
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

	// First, if we are looking up module names (and other names) split this off into two lookups, one for module names, one for
	// the other names.
	if (ModuleName() in containingNamespaces && size(containingNamespaces) > 1)
		return getItemsForNameWBound(symbolTable, currentScopeId, x, { ModuleName() }, funBounded, modBounded) +
				getItemsForNameWBound(symbolTable, currentScopeId, x, containingNamespaces - ModuleName(), funBounded, modBounded);

	// Now, handle qualified names. Module names can be qualified names, and are stored as such, but other names aren't, so if we
	// encounter a qualified name and are not looking up a module name change into the module scope and just lookup the simple name.
	if (ModuleName() notin containingNamespaces && RCompoundName(nl) := x) {
		RName moduleName = (size(nl) == 2) ? RSimpleName(nl[0]) : RCompoundName(head(nl,size(nl)-1)); 
		x = RSimpleName(nl[size(nl)-1]);
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

	// We fall through to here IF a) the name is not a qualified name, or b) the name IS a qualified name but is
	// the name of a module. Otherwise, it is taken care of above.

	// Get back the items at this level with the given name
	foundItems += symbolTable.scopeNames[currentScopeId,x];

	// Now, filter it down based on the namespaces we are looking for
	foundItems = { f | ns <- containingNamespaces, f <- filterNamesForNamespace(symbolTable, foundItems, ns) };

	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level is reached. If this is a bounded search, don't pass through 
	// function and/or module boundaries.

	if (size(foundItems) == 0) {
	   STItem cl = symbolTable.scopeItemMap[currentScopeId];
	   if (ModuleLayer(_,pid) := cl) {
              if (!modBounded) {
	         foundItems = getItemsForNameWBound(symbolTable,pid,x,containingNamespaces,funBounded,modBounded);
              }
	   } else if (FunctionLayer(_,pid) := cl || ClosureLayer(_,pid) := cl || VoidClosureLayer(_,pid) := cl) {
              if (!funBounded) {
	         foundItems = getItemsForNameWBound(symbolTable,pid,x,containingNamespaces,funBounded,modBounded);
              }
	   } else if (TopLayer() !:= cl) {
	      foundItems = getItemsForNameWBound(symbolTable,cl.parentId,x,containingNamespaces,funBounded,modBounded);
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

public set[STItemId] getTypeVarItemsForName(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeVarName() }, false, false);
}

public set[STItemId] getTypeVarItemsForNameFB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeVarName() }, true, false);
}

public set[STItemId] getTypeVarItemsForNameMB(SymbolTable symbolTable, STItemId currentScopeId, RName x) {
	return getItemsForNameWBound(symbolTable, currentScopeId, x, { TypeVarName() }, false, true);
}

public set[STItemId] getModuleItemsForName(SymbolTable symbolTable, RName x) {
	return getItemsForNameWBound(symbolTable, symbolTable.topSTItemId, x, { ModuleName() }, false, false);
}

// Get the function item that encloses a point in the code. This will navigate up to the function layer
// and then return the item id for the function item that is associated with this layer.
public STItemId getEnclosingFunctionAux(SymbolTable symbolTable, STItemId currentScope) {
	STItem si = getSTItem(currentScope, symbolTable);
	if (FunctionLayer(itemId,_) := si) return itemId;
	if (ClosureLayer(itemId,_) := si) return itemId;
	if (VoidClosureLayer(itemId,_) := si) return itemId;
	if (TopLayer() := si) throw "Cannot get enclosing function at top level";
	return getEnclosingFunctionAux(symbolTable, si.parentId);
}

public STItemId getEnclosingFunction(SymbolTable symbolTable) {
	return getEnclosingFunctionAux(symbolTable, symbolTable.currentScope);
}

public RType getEnclosingFunctionType(SymbolTable symbolTable) {
	return getTypeForItem(symbolTable, getEnclosingFunction(symbolTable)); 
}

public bool insideEnclosingFunction(SymbolTable symbolTable, STItemId currentScope) {
	STItem si = getSTItem(currentScope, symbolTable);
	if (FunctionLayer(itemId,_) := si) return true;
	if (ClosureLayer(itemId,_) := si) return true;
	if (VoidClosureLayer(itemId,_) := si) return true;
	if (TopLayer() := si) return false;
	return insideEnclosingFunction(symbolTable, si.parentId);
}

public list[RNamedType] markUserTypesForNamedTypeList(list[RNamedType] ntl, SymbolTable symbolTable, STItemId currentScope) {
	return [ markUserTypesForNamedType(nt,symbolTable,currentScope) | nt <- ntl ];
}

public RNamedType markUserTypesForNamedType(RNamedType nt, SymbolTable symbolTable, STItemId currentScope) {
        if (RUnnamedType(rt) := nt) {
	        return RUnnamedType(markUserTypes(rt,symbolTable,currentScope));
	} else if (RNamedType(rt,tn) := nt) {
	        return RNamedType(markUserTypes(rt, symbolTable, currentScope), tn);
	} else {
	        throw "markUserTypesForNamedType given unexpected type <nt>";
	}
}

//
// Aliases and ADTs are given just as names, but we want to make sure we mark them as having the proper
// type. So, recurse over the structure of the type, expanding out any user names that we find. We could
// find them anywhere -- e.g., as part of the bound on a type variable, as part of the parameter on an
// alias or ADT, etc, so we need to check through the entire type structure.
//
// Note that we don't need to handle all types here; some of the types, such as statement 
// types, are just used internally, and at the point they are encountered aliases should 
// already be marked. This is really just needed on types that can be created from converting 
// the syntactic types we get in Rascal (i.e., actual types that could be given on variables,
// generated from expressions, etc.
//
// Note also that we use matching here instead of the isAliasType, isIntType, etc functions.
// This is because this is faster. However, we need to maintain the ordering to make sure
// we handle aliases and type vars as aliases and type vars, not as their underlying aliased
// or bound types.
//
public RType markUserTypes(RType rt, SymbolTable symbolTable, STItemId currentScope) {
        RType res = rt;

        // NOTE: Alias type comes before the other types so we can handle it specially here. This is needed because it
	// also responds with true to the is...Type question that applies to the aliased type. For instance, alias l = list[int]
	// responds true to both isAliasType(l) and isListType(l).
        if (RAliasType(_,_) := rt) res = aliasTypeHasParameters(rt) 
                ? makeParameterizedAliasType(getAliasName(rt), markUserTypes(getAliasedType(rt), symbolTable, currentScope), 
                  [markUserTypes(rti, symbolTable, currentScope) | rti <- getAliasTypeParameters(rt)])
	        : makeAliasType(getAliasName(rt), markUserTypes(getAliasedType(rt), symbolTable, currentScope));

	// NOTE: Type vars also come first for the same reason, in this case based on their bound versus on the aliased
	// type.
	else if (RTypeVar(_) := rt) res = typeVarHasBound(rt) ? makeTypeVarWithBound(getTypeVarName(rt),markUserTypes(getTypeVarBound(rt), symbolTable, currentScope)) : rt;

	// NOTE: Rel comes before set because it is also a set. This allows us to handle it individually here.
        else if (RRelType(_) := rt) res = makeRelTypeFromTuple(markUserTypes(getRelElementType(rt), symbolTable, currentScope));

	// Special handling for unknown types. These are types that are imported, but where the module that was imported did not
	// contain the declaration of the type. We see if we can find it here (for instance, a module may use an ADT imported
	// from another module; as long as we imported that other module as well, the ADT will be in scope and we can resolve it)
	else if (RUnknownType(_) := rt) {
	        RType utRes = markUserTypes(getUnknownType(rt), symbolTable, currentScope);
		res = (utRes != getUnknownType(rt)) ? utRes : rt; // If the type changed, we at least partially resolved it, return that
	}

	// ADTs also have type parameters, handle them similarly to aliases
        else if (RADTType(_) := rt) res = aliasHasTypeParameters(rt) ? 
                makeParameterizedADTType(getADTName(rt), [markUserTypes(rti, symbolTable, currentScope) | rti <- getADTTypeParameters(rt)]) : makeADTType(getAliasName(rt));

	// Now, handle the other containers
        else if (RListType(_) := rt) res = makeListType(markUserTypes(getListElementType(rt), symbolTable, currentScope));
        else if (RSetType(_) := rt) res = makeSetType(markUserTypes(getSetElementType(rt), symbolTable, currentScope));
        else if (RBagType(_) := rt) res = makeBagType(markUserTypes(getBagElementType(rt), symbolTable, currentScope));
        else if (RContainerType(_) := rt) res = makeContainerType(markUserTypes(getContainerElementType(rt), symbolTable, currentScope));
        else if (RMapType(_,_) := rt) res = makeMapTypeWithNames(markUserTypesForNamedType(getMapFieldsWithNames(rt)[0], symbolTable, currentScope), 
                                                      markUserTypesForNamedType(getMapFieldsWithNames(rt)[1], symbolTable, currentScope));
        else if (RTupleType(_) := rt) res = makeTupleTypeWithNames(markUserTypesForNamedTypeList(getTupleFieldsWithNames(rt), symbolTable, currentScope));
        else if (RFunctionType(_,_) := rt) res = makeFunctionTypeWithNames(markUserTypes(getFunctionReturnType(rt), symbolTable, currentScope),
                                                                markUserTypesForNamedTypeList(getFunctionArgumentTypesWithNames(rt), symbolTable, currentScope));
        else if (RConstructorType(_,_,_) := rt) res = makeConstructorType(getConstructorName(rt), markUserTypes(getConstructorResultType(rt), symbolTable, currentScope), 
                                                             markUserTypesForNamedTypeList(getConstructorArgumentTypesWithNames(rt), symbolTable, currentScope));
        else if (RReifiedType(_) := rt) res = makeReifiedType(markUserTypes(getReifiedType(rt), symbolTable, currentScope));
	else if (RVarArgsType(_) := rt) res = makeVarArgsType(markUserTypes(getVarArgsType(rt), symbolTable, currentScope));

	// Now, handle the types that do not contain other types.
        else if (RBoolType() := rt) res = rt;
        else if (RIntType() := rt) res = rt;
        else if (RRealType() := rt) res = rt;
        else if (RNumType() := rt) res = rt;
        else if (RStrType() := rt) res = rt;
        else if (RValueType() := rt) res = rt;
        else if (RNodeType() := rt) res = rt;
        else if (RVoidType() := rt) res = rt;
        else if (RLocType() := rt) res = rt;
        else if (RDateTimeType() := rt) res = rt;
        else if (RInferredType(_) := rt) res = rt;

	// Now, handle types that we should not encounter, this helps us to determine if we missed something...
	else if (RLexType() := rt) throw "Should not find this in real life: markUserTypes for type <rt>";
	else if (RNonTerminalType() := rt) throw "Should not find this in real life: markUserTypes for type <rt>";
	else if (RFailType(_) := rt) throw "Should not find this as the type of a name: markUserTypes for type <rt>";
	else if (ROverloadedType(_) := rt) throw "Should not find this as the type of a name during type expansion: markUserTypes for type <rt>";
	else if (RStatementType(_) := rt) throw "Should not find this in real life: markUserTypes for type <rt>";
	else if (RDateTypeSelector(_,_) := rt) throw "Should not find this in real life: markUserTypes for type <rt>";
	else if (RAssignableType(_,_) := rt) throw "Should not find this as the type of a name during type expansion: markUserTypes for <rt>";

	// Now, these are the real expansion cases: the user types. Here, we want to expand any user types 
	// by looking up the actual type and recursing.
	else if (RUserType(tn) := rt) {
		set[STItemId] userTypes = getTypeItemsForName(symbolTable,currentScope,tn);
		set[STItemId] aliasItems = { pi | pi <- userTypes, AliasItem(_,_,_,_) := symbolTable.scopeItemMap[pi] };
		set[STItemId] adtItems = { pi | pi <- userTypes, ADTItem(_,_,_) := symbolTable.scopeItemMap[pi] };

		if (size(userTypes - aliasItems - adtItems) > 0) 
                        throw "Unexpected case, got a user type that is not an alias or an adt, example: <symbolTable.scopeItemMap[getOneFrom(userTypes-aliasItems-adtItems)]>";
		
		// Preference ADTs, although scope rules should keep us from adding both ADTs and aliases with the same name
		if (size(adtItems) > 0) {
			STItemId adtItemId = getOneFrom(adtItems);
			RType resultType = getTypeForItem(symbolTable, adtItemId);
			return resultType; // we return here, we want to keep whatever @at annotation was added
		} else if (size(aliasItems) > 0) {
			STItemId aliasItemId = getOneFrom(aliasItems);
			RType resultType = getTypeForItem(symbolTable, aliasItemId);
			return resultType; // we return here, we want to keep whatever @at annotation was added
		}
	} 
        else if (RParameterizedUserType(tn,tps) := rt) {
		list[RType] params = [ markUserTypes(tp,symbolTable,currentScope) | tp <- tps];

		set[STItemId] userTypes = getTypeItemsForName(symbolTable,currentScope,tn);
		set[STItemId] aliasItems = { pi | pi <- userTypes, AliasItem(_,_,_,_) := symbolTable.scopeItemMap[pi] };
		set[STItemId] adtItems = { pi | pi <- userTypes, ADTItem(_,_,_) := symbolTable.scopeItemMap[pi] };

		if (size(userTypes - aliasItems - adtItems) > 0) 
                        throw "Unexpected case, got a user type that is not an alias or an adt, example: <symbolTable.scopeItemMap[getOneFrom(userTypes-aliasItems-adtItems)]>";

		// Preference ADTs, although scope rules should keep us from adding both ADTs and aliases with the same name
		if (size(adtItems) > 0) {
			STItemId adtItemId = getOneFrom(adtItems);
			RType resultType = getTypeForItem(symbolTable, adtItemId);
			if (size(params) > 0)
			        // TODO: We need to verify that the number of parameters given is correct
			        resultType = makeParameterizedADTType(getADTName(resultType), params);
			return resultType;  
		} else if (size(aliasItems) >= 1) {
			STItemId aliasItemId = getOneFrom(aliasItems);
			RType resultType = getTypeForItem(symbolTable, aliasItemId);
			if (size(params) > 0) {
			        // Instantiate the variables in the aliased type, based on the type parameters
				// TODO: Any need to check bounds here? Or have we done that elsewhere? 
				list[RName] aliasParams = [ getTypeVarName(atp) | atp <- getAliasTypeParameters(resultType), isTypeVar(atp)];

				map[RName, RType] bindings = ( );
				for (n <- [0..size(aliasParams)-1]) bindings = bindings + (aliasParams[n] : params[n]);
				RType instantiatedAliased = instantiateVars(bindings, getAliasedType(resultType));
			        resultType = makeParameterizedAliasType(getAliasName(resultType), instantiatedAliased, params);
			}
			return resultType;
		}
	} else {
	        println("markUserTypes, warning, did not handle type <prettyPrintType(rt)>, internal form <rt>");
	}
	
	if ( (rt@at)? ) res = res[@at = rt@at];
	return res;
}

//
// TODO: This should throw an exception when the type of an untyped name (e.g., a label) is requested
//
public RType getTypeForItem(SymbolTable symbolTable, STItemId itemId) {
	if (itemId notin symbolTable.scopeItemMap) throw "Error, id <itemId> is not in the scopeItemMap";
	STItem si = symbolTable.scopeItemMap[itemId];
	RType retType = makeVoidType();
	switch(si) {
		case FormalParameterItem(_,t,_) : retType = markUserTypes(t,symbolTable,si.parentId);
		
		case VariableItem(_,t,_) : retType = markUserTypes(t,symbolTable,si.parentId);
		
		case TypeVariableItem(t,_) : retType = markUserTypes(t,symbolTable,si.parentId);
		
		case FunctionItem(_,t,paramIds,_,_,_) : {
			retType = makeFunctionType(markUserTypes(t,symbolTable,si.parentId),[getTypeForItem(symbolTable, paramId) | paramId <- paramIds]);
			if ( (si@at)? ) retType = retType[@at = si@at];
	 	}

		case ClosureItem(t,paramIds,_) :
		        retType = makeFunctionType(markUserTypes(t,symbolTable,si.parentId),[getTypeForItem(symbolTable, paramId) | paramId <- paramIds]);

		case VoidClosureItem(paramIds,_) :
		        retType = makeFunctionType(markUserTypes(RVoidType(),symbolTable,si.parentId),[getTypeForItem(symbolTable, paramId) | paramId <- paramIds]);

		case ConstructorItem(n,tas,adtParentId,_) : {
			retType = makeConstructorType(n,RADTType(symbolTable.scopeItemMap[adtParentId].adtType),[markUserTypesForNamedType(t,symbolTable,si.parentId) | t <- tas]);
			if ( (si@at)? ) retType = retType[@at = si@at];
		}
		
		case ADTItem(ut,_,_) : 
			retType = RADTType(ut);
		
		case AliasItem(ut,ut2,_,_) : 
			retType = RAliasType(ut,markUserTypes(ut2,symbolTable,si.parentId)); 
		
		default : { 
			retType = makeVoidType(); 
		}
	}
	return retType;
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

	// Create scope items for type variables in the parameters; this way these types will be in scope
	// TODO: Here should check to make sure variables with the same name have the same bounds
	// TODO: SEe if logic changes require any changes here!!!! BBBBBBBBBBBBBBBBB
	set[RType] typeVars = { tvv | p <- params, tvv <- collectTypeVars(p[1]) };
	for (tvv <- typeVars) {
	    // See if the name is not yet in scope -- if it is in scope, it must be defined by a surrounding function
	    if (size(getTypeVarItemsForNameMB(symbolTable, symbolTable.currentScope, getTypeVarName(tvv))) == 0) {
	       AddedItemPair aipTV = addSTItemWithParent(TypeVariableItem(tvv, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	       symbolTable = aipTV.symbolTable;
	    }
	}

	// Check if the return type has any type variables
	// TODO: Need to check to make sure all return type variables are present in the function parameters -- the type var can use a type
	// variable defined in the surrounding function context, but only if the function parameters include this var as well
	set[RType] rTypeVars = collectTypeVars(retType);
	for (tvv <- rTypeVars) {
	    // See if the name is not yet in scope -- if it is in scope, it must be defined by a surrounding function
	    if (size(getTypeVarItemsForNameMB(symbolTable, symbolTable.currentScope, getTypeVarName(tvv))) == 0) {
	       symbolTable = addScopeError(symbolTable, l, "Type variable <prettyPrintType(tvv)> given in return type for function not in scope.");
	    }
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(FunctionItem(functionName, retType, paramIds, throwsTypes, isPublic, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;
	
	// TODO: Add the IDs for the type vars?
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
	set[RName] namesSeen = { };
	for (tuple[RName pname, RType ptype, loc ploc, loc nloc] pt <- params) {
		if (pt.pname in namesSeen) {
			symbolTable = addScopeError(symbolTable, pt.nloc, "Illegal redefinition of <prettyPrintName(pt.pname)>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addSTItemWithParent(FormalParameterItem(pt.pname, pt.ptype, symbolTable.currentScope)[@at=pt.ploc], symbolTable.currentScope, pt.ploc, symbolTable);
		paramIds += aipParam.addedId; symbolTable = aipParam.symbolTable;
	}

	// Create scope items for type variables in the parameters; this way these types will be in scope
	// TODO: Here should check to make sure variables with the same name have the same bounds
	set[RType] typeVars = { tvv | p <- params, tvv <- collectTypeVars(p[1]) };
	for (tvv <- typeVars) {
	    // See if the name is not yet in scope -- if it is in scope, it must be defined by a surrounding function
	    if (size(getTypeVarItemsForNameMB(symbolTable, symbolTable.currentScope, getTypeVarName(tvv))) == 0) {
	       AddedItemPair aipTV = addSTItemWithParent(TypeVariableItem(tvv, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	       symbolTable = aipTV.symbolTable;
	    }
	}

	// Check if the return type has any type variables
	// TODO: Need to check to make sure all return type variables are present in the function parameters -- the type var can use a type
	// variable defined in the surrounding function context, but only if the function parameters include this var as well
	set[RType] rTypeVars = collectTypeVars(retType);
	for (tvv <- rTypeVars) {
	    // See if the name is not yet in scope -- if it is in scope, it must be defined by a surrounding function
	    if (size(getTypeVarItemsForNameMB(symbolTable, symbolTable.currentScope, getTypeVarName(tvv))) == 0) {
	       symbolTable = addScopeError(symbolTable, l, "Type variable <prettyPrintType(tvv)> given in return type for function not in scope.");
	    }
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(ClosureItem(retType, paramIds, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	// TODO: Add the IDs for the type vars?
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
	set[RName] namesSeen = { };
	for (pt <- params) {
		if (pt.pname in namesSeen) {
			symbolTable = addScopeError(symbolTable, pt.nloc, "Illegal redefinition of <prettyPrintName(pt.pname)>. Parameter names must be different from other parameter names and from the name of the function.");
		}
		namesSeen += pt.pname;
		AddedItemPair aipParam = addSTItemWithParent(FormalParameterItem(pt.pname, pt.ptype, symbolTable.currentScope)[@at=pt.ploc], symbolTable.currentScope, pt.ploc, symbolTable);
		paramIds += aipParam.addedId; symbolTable = aipParam.symbolTable;
	}

	// Create scope items for type variables in the parameters; this way these types will be in scope
	// TODO: Here should check to make sure variables with the same name have the same bounds
	set[RType] typeVars = { tvv | p <- params, tvv <- collectTypeVars(p[1]) };
	for (tvv <- typeVars) {
	    // See if the name is not yet in scope -- if it is in scope, it must be defined by a surrounding function
	    if (size(getTypeVarItemsForNameMB(symbolTable, symbolTable.currentScope, getTypeVarName(tvv))) == 0) {
	       AddedItemPair aipTV = addSTItemWithParent(TypeVariableItem(tvv, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	       symbolTable = aipTV.symbolTable;
	    }
	}

	// Add the actual function item associated with the scope layer
	AddedItemPair aipItem = addSTItemWithParent(VoidClosureItem(paramIds, symbolTable.currentScope)[@at=l], symbolTable.currentScope, l, symbolTable);
	aipItem.symbolTable.scopeItemMap[aipLayer.addedId].itemId = aipItem.addedId;

	// TODO: Add the IDs for the type vars?
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

public ResultTuple addFieldToScopeAt(RName fieldName, loc l, SymbolTable symbolTable, STItemId scopeToUse) {
	AddedItemPair aip = addSTItemWithParent(FieldItem(fieldName, scopeToUse)[@at=l], scopeToUse, l, symbolTable);
	return <aip.symbolTable,[aip.addedId]>;
}

public ResultTuple addFieldToScope(RName fieldName, loc l, SymbolTable symbolTable) {
	return addFieldToScopeAt(fieldName, l, symbolTable, symbolTable.currentScope);
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
			case ADTItem(_,_,_) : {
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
	funType = markUserTypes(funType, symbolTable, scopeToCheck);

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
       conType = markUserTypes(conType, symbolTable, scopeToCheck);
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
	RType onType = symbolTable.scopeItemMap[annotationId].onType;
	set[STItemId] otherItems = { };
	if (modBounded) {
		otherItems = getAnnotationItemsForNameMB(symbolTable, symbolTable.currentScope, annotationName) - annotationId;
	} else {
		otherItems = getAnnotationItemsForName(symbolTable, symbolTable.currentScope, annotationName) - annotationId;
	}
	if (size(otherItems) > 0) {
	        // See if the other items overlap
		if (size({ oi | oi <- otherItems, comparable(onType,symbolTable.scopeItemMap[oi].onType) }) > 1)
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

public ResultTuple registerAnnotation(ResultTuple result, loc nloc) {
        STItemId annotationId = result.addedItems[0];
	RName annotationName = symbolTable.scopeItemMap[annotationId].annotationName;
	RType annotationType = symbolTable.scopeItemMap[annotationId].annoType;
	RType onType = symbolTable.scopeItemMap[annotationId].onType;
	symbolTable = aip.symbolTable;
	symbolTable.annRel += < annotationName, annotationType, onType >;

}

public bool inBoolLayer(SymbolTable symbolTable) {
	if (BooleanExpLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope] || OrLayer(_) := symbolTable.scopeItemMap[symbolTable.currentScope])
		return true;
	return false;
}

public SymbolTable consolidateADTDefinitions(SymbolTable symbolTable, RName moduleName) {
	// Get back the ID for the name of the module being checked -- there should be only one matching
	// item. TODO: We may want to verify that here.
	STItemId moduleItemId = getOneFrom(getModuleItemsForName(symbolTable, moduleName));
	STItemId moduleLayerId = moduleItemId.parentId;
	return consolidateADTDefinitionsForLayer(symbolTable, moduleLayerId, true);
}

public STItemId getEnclosingModuleAux(SymbolTable symbolTable, STItemId currentScope) {
	STItem si = getSTItem(currentScope, symbolTable);
	if (ModuleLayer(itemId,_) := si) return itemId;
	if (TopLayer() := si) throw "Cannot get enclosing module at top level";
	return getEnclosingModuleAux(symbolTable, si.parentId);
}

public STItemId getEnclosingModule(SymbolTable symbolTable) {
	return getEnclosingModuleAux(symbolTable, symbolTable.currentScope);
}

public SymbolTable consolidateADTDefinitionsForLayer(SymbolTable symbolTable, STItemId layerId, bool includeTopLayer) {
	// Step 1: Pick out all ADT definitions in the loaded scope information (i.e., all ADTs defined
	// in either the loaded module or its direct imports)
	set[STItemId] adtIDs = { sid | sid <- symbolTable.scopeRel[layerId], ADTItem(_,_,_) := symbolTable.scopeItemMap[sid] };
	if (includeTopLayer) {
		adtIDs = adtIDs + { sid | sid <- symbolTable.scopeRel[symbolTable.topSTItemId], ADTItem(_,_,_) := symbolTable.scopeItemMap[sid] };
	}
							  
	// Step 2: Group these based on the name of the ADT
	rel[RName adtName, STItemId adtItemId] nameXADTItem = { < getUserTypeName(n), sid > | sid <- adtIDs, ADTItem(n,_,_) := symbolTable.scopeItemMap[sid] };
	
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

public bool hasRType(SymbolTable symbolTable, loc l) {
	if (l in symbolTable.itemUses || l in symbolTable.scopeErrorMap)
		return true;
	return false;
}

data RType = RLocatedType(RType actualType, loc l);

//
// Get the RType of the item(s) used at location l. We can return multiple types if we have a function
// or constructor with overloads. Note that we could also return multiple items even when these items
// have the same type, for instance when we merge the items created along different or branches into
// the parent layer (we don't go back and remove the old items). This code specifically checks for that
// by ensuring that the types, and not just the items, are different.
//
public RType getRType(SymbolTable symbolTable, loc l) {
	set[STItemId] itemIds = (l in symbolTable.itemUses) ? symbolTable.itemUses[l] : { };
	set[str] scopeErrors = (l in symbolTable.scopeErrorMap) ? symbolTable.scopeErrorMap[l] : { };
	
	if (size(scopeErrors) == 0) {
	        if (size(itemIds) == 0) {
		        return makeFailType("Error, attemting to find type of item at location <l>, but not item found",l);
		} else {
			if (size({ getTypeForItem(symbolTable, itemId) | itemId <- itemIds }) > 1) {
			        // We have multiple items with multiple distinct types, so return an overload type
  			        set[ROverloadedType] overloads = { };
			        for (itemId <- itemIds) {
				        STItem item = getSTItem(itemId, symbolTable);
				        if ( (item@at) ?)
					        overloads += ROverloadedTypeWithLoc(getTypeForItem(symbolTable, itemId), item@at);
				        else
					        overloads = ROverloadedType(getTypeForItem(symbolTable, itemId));
			        }
			        return ROverloadedType(overloads);
			} else {
			        STItemId itemId = getOneFrom(itemIds);
			        STItem item = getSTItem(itemId, symbolTable);
			        if ( isFunctionOrConstructorItem(item) && ((item@at) ?)) {
				        return RLocatedType(getTypeForItem(symbolTable, itemId), item@at);
			        } else {
				        return getTypeForItem(symbolTable, itemId);
			        }
			}
		}
	} else {
		return collapseFailTypes({ makeFailType(s,l) | s <- scopeErrors });
	}
}

public RType getTypeForName(SymbolTable symbolTable, RName theName, loc theLoc) {
	if (hasRType(symbolTable, theLoc)) {
		RType rt = getRType(symbolTable, theLoc);
		if (RLocatedType(rt2,l) := rt) {
			return rt2[@at=l];
		} else {
			return isInferredType(rt) ? symbolTable.inferredTypeMap[getInferredTypeIndex(rt)] : rt;
		}
	} else {
		return makeFailType("No type declared or inferred for <prettyPrintName(theName)>",theLoc);
	}
}

public RType getTypeForNameLI(SymbolTable symbolTable, RName theName, loc theLoc) {
	if (hasRType(symbolTable, theLoc)) {
		RType rt = getRType(symbolTable, theLoc);
		if (RLocatedType(rt2,l) := rt) {
			return rt2[@at=l];
		} else {
			return rt;
		}
	} else {
		return makeFailType("No type declared or inferred for <prettyPrintName(theName)>",theLoc);
	}
}

//
// Provide consistency checking and expansion on type conversion.
//
// TODO: Add checks to ensure that the correct number of type parameters are
// given. For example, T[&U] should not be used as T[int,bool].
//

alias ConvertTuple = tuple[SymbolTable symbolTable, RType rtype];
alias ConvertTupleN = tuple[SymbolTable symbolTable, RNamedType rtype];

public ConvertTuple convertRascalType(SymbolTable symbolTable, Type t) {
        // Step 1: convert the type
	RType rt = convertType(t);

	// Step 2: look for any errors marked on the converted type
	list[tuple[str msg, loc at]] conversionErrors = [ ];
	visit(rt) { 
	        case RType ct : 
		        if ( (ct@errinfo)? ) conversionErrors = conversionErrors + ct@errinfo;
        }

	// Step 3: if we found errors, add them as scope errors
	if (size(conversionErrors) > 0) {
	        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);
	}

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
	if (size(conversionErrors) > 0) {
	        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);
	}

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
	if (size(conversionErrors) > 0) {
	        for (<cmsg,cloc> <- conversionErrors) symbolTable = addScopeError(symbolTable, cloc, cmsg);
	}

	// Step 4: finally return the type
	return <symbolTable, rt>;	
}

//
// TODO: Not sure if this is sufficient for parameterized ADTs, need
// to verify that this works for them as well.
//
public set[RType] reachableTypes(SymbolTable symbolTable, RType t) {
        set[RType] foundSet = {  };
        set[RType] workingSet = { t };
	set[STItemId] foundItems = { };

        do {
                RType working = getOneFrom(workingSet); 
		workingSet = workingSet - working; foundSet = foundSet + working;
		for (/RType found := working) { 
        	        if (found notin foundSet) workingSet += found;
	        	    if (RADTType(_) := found) {
		                set[STItemId] constructors = symbolTable.adtMap[getADTName(found)].consItems;
			        for (ci <- constructors, ci notin foundItems) {
				        foundItems = foundItems + ci;
			                RType ct = getTypeForItem(symbolTable, ci);
				        if (ct notin foundSet) workingSet = workingSet + ct;
			        }
		        }
	        }
        } while (size(workingSet) > 0);
	foundSet = foundSet - { fi | fi <- foundSet, RUserType(_) := fi || RParameterizedUserType(_,_) := fi };
        return foundSet;
}