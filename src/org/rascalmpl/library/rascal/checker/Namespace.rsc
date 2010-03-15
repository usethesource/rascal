module rascal::checker::Namespace

import rascal::checker::Types;
import rascal::checker::ListUtils;
import rascal::checker::SubTypes;

import List;
import Graph;
import IO;
import Set;

import rascal::\old-syntax::Rascal;

// 
// TODOs
//
// 1. Tags can include expressions, which thus need to be typechecked. Add checking for
//    tags.
//
// 2. For each module we should create a module "signature" that includes all externally
//    visible functions, datatypes, etc. For now, we are restricted to the information
//    in just the current module.
//
// 3. DONE: This should be fully functional -- we currently maintain a small amount of state,
//    but it would be better to remove this.
//
// 4. DONE: Variables introduced in comprehensions and patterns should not have scope outside
//    of the related structures; for comprehensions this is outside of the comprehension,
//    for patterns this is outside of the action or surrounding code (used in an if,
//    it has scope inside the if). It may be easiest to just always introduce a new
//    scoping layer when entering one of these constructs, since that would then ensure
//    scope is cleaned up properly upon exiting.
//
// 5. DONE: Give each scope item a unique ID. This will make it easier to keep them unique,
//    since it is (remotely) possible that two items, such as two blocks, could each have
//    parents that looked the same.
//
// 6. Would it be good to wrap each scope item with functions for accessors? This would
//    shield it a bit from changes to the arity of the item. Or it could be useful to
//    use the . field accessors and as much as possible eliminate the use of pattern
//    matching over these structures.
//
// 7. Would it be good to split the FunctionLayer into a layer and an item? It may be
//    nice to come up with a more general way to think about scoping, made up of layers,
//    namespaces, visibilities, and items, that we could apply to any language, including
//    Rascal.
//
// 8. Add checks to ensure non-overlaps on overloaded functions and constructors
//
// 9. Add checks to ensure names are not repeated at the same scoping level (important:
//    this includes within a function, since names cannot be shadowed by new names defined
//    inside blocks)
//    NOTE: Support has now been added to stop lookup at function/closure boundaries; this
//          makes it easier to tell if we are violating this rule or not. This is also useful
//          in cases where we should not cross function boundaries looking for a valid name,
//          for instance with labels on statements.
//
// 10. DONE: Need to enforce proper scoping for boolean expressions; for instance,
//     A && B && C creates a scope, with names introduced in A visible in B and C
//
// 11. DONE: Introduce a new boolean expression scope inside a function argument list
//
// 12. Need to enforce name availability on all paths from a boolean expression. For
//     instance, in something like (a := p1 && b := p2) || (a := p1 && c := p3), only
//     a should be visible (i.e., in scope) outside of the || expression
//
// 13. DONE: Add support for the Reducer expression
//
// 14. Add alias expansion (since we have all the name information here) -- maybe add
//     as an Alias type, so we can keep track of the original type name used as well,
//     which would make more sense to the user than the expanded form.
//
// 15. Special case, one function ends with list[Value], another with Value...
//

// Set flag to true to issue debug messages
private bool debug = true;

//
// Items representing identifiable parts of scope, including layers (modules, functions,
// blocks) and items (functions, variables, formal parameters, labels). Note that functions 
// are both, since they are an item present in a scope and also introduce new scope.
//
alias ScopeItemId = int;

data ScopeItem =
	  ModuleLayer(RName moduleName)
	| FunctionLayer(RName functionName, RType returnType, list[ScopeItemId] parameters, list[RType] throwsTypes, bool isVarArgs, bool isPublic, ScopeItemId parentId)
	| ClosureLayer(RType returnType, list[ScopeItemId] parameters, bool isVarArgs, ScopeItemId parentId)
	| VoidClosureLayer(list[ScopeItemId] parameters, bool isVarArgs, ScopeItemId parentId)
	| BlockLayer(ScopeItemId parentId)
	| VariableItem(RName variableName, RType variableType, ScopeItemId parentId)
	| FormalParameterItem(RName parameterName, RType parameterType, ScopeItemId parentId)
	| LabelItem(RName labelName, ScopeItemId parentId)
	| AliasItem(RUserType aliasType, RType aliasedType, bool isPublic, ScopeItemId parentId)
	| ConstructorItem(RName constructorName, list[RTypeArg] constructorArgs, ScopeItemId adtParentId, ScopeItemId parentId)
	| ADTItem(RUserType adtType, set[ScopeItemId] variants, bool isPublic, ScopeItemId parentId) 
	| DummyItem()
	| PatternMatchLayer(ScopeItemId parentId)
	| BooleanExpLayer(ScopeItemId parentId)
	| AnnotationItem(RName annotationName, RType annoType, RType onType, bool isPublic, ScopeItemId parentId) 
;

data Namespace =
	  ModuleName()
	| LabelName()
	| FCVName()
	| TypeName()
	| AnnotationName()
;

anno loc ScopeItem@at;

alias ScopeRel = rel[ScopeItemId scopeId, ScopeItemId itemId];
alias ItemUses = map[loc useLoc, set[ScopeItemId] usedItems];
alias ScopeItemMap = map[ScopeItemId,ScopeItem];
alias ItemLocationMap = map[loc,ScopeItemId];

// TODO: Should be able to use ScopeItemMap here, but if I try it doesn't work, something must be
// wrong with the current alias expansion algorithm; this is the same with ItemLocationMap as well
// for itemLocations...
alias ScopeInfo = tuple[ScopeItemId topScopeItemId, rel[ScopeItemId scopeId, ScopeItemId itemId] scopeRel, ItemUses itemUses, 
                        ScopeItemId nextScopeId, map[ScopeItemId, ScopeItem] scopeItemMap, 
                        map[loc, ScopeItemId] itemLocations, ScopeItemId currentScope, int freshType,
                        map[loc, set[str]] scopeErrorMap, map[int, RType] inferredTypeMap];

alias AddedItemPair = tuple[ScopeInfo scopeInfo, ScopeItemId addedId];
alias ScopeUpdatePair = tuple[ScopeInfo scopeInfo, ScopeItemId oldScopeId];
                        
public ScopeInfo createNewScopeInfo() {
	return < -1, { }, ( ), 0, ( ), ( ), 0, 0, (), () >;
}                    

public ScopeInfo addItemUse(ScopeInfo scopeInfo, ScopeItemId scopeItem, loc l) {
	if (l in scopeInfo.itemUses)
		scopeInfo.itemUses[l] += scopeItem;
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
		scopeInfo.scopeErrorMap[l] += msg;
	else
		scopeInfo.scopeErrorMap += (l : { msg } );
	return scopeInfo;
}

public AddedItemPair addScopeItem(ScopeItem si, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationMap newILMap = scopeInfo.itemLocations + (l : scopeInfo.nextScopeId);
	scopeInfo = ((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILMap];
	return <scopeInfo,newItemId>;				
}

public AddedItemPair addScopeItemWithParent(ScopeItem si, ScopeItemId parentId, loc l, ScopeInfo scopeInfo) {
	int newItemId = scopeInfo.nextScopeId;
	ScopeRel newScopeRel = scopeInfo.scopeRel + <parentId, scopeInfo.nextScopeId>;
	ScopeItemMap newSIMap = scopeInfo.scopeItemMap + (newItemId : si);
	ItemLocationMap newILMap = scopeInfo.itemLocations + (l : scopeInfo.nextScopeId);
	scopeInfo = (((scopeInfo[nextScopeId = scopeInfo.nextScopeId+1])[scopeItemMap=newSIMap])[itemLocations=newILMap])[scopeRel = newScopeRel];
	return <scopeInfo,newItemId>;				
}

public ScopeItemId getItemAtLocation(loc l, ScopeInfo scopeInfo) {
	if (l in scopeInfo.itemLocations) {
		return scopeInfo.itemLocations[l];	
	} else {
		if (debug) println("NAMESPACE: Error, trying to retrieve item from unassociated location!");
		// TODO: Should instead throw an exception
	}
}

public ScopeInfo updateScopeItem(ScopeItem si, ScopeItemId idToUpdate, ScopeInfo scopeInfo) {
	return scopeInfo[scopeItemMap = scopeInfo.scopeItemMap + (idToUpdate : si)];				
}

public ScopeItem getScopeItem(ScopeItemId id, ScopeInfo scopeInfo) {
	return scopeInfo.scopeItemMap[id];
}
	
public ScopeUpdatePair changeCurrentScope(ScopeItemId newScopeId, ScopeInfo scopeInfo) {
	int oldScopeId = scopeInfo.currentScope;
	return < scopeInfo[currentScope = newScopeId], oldScopeId >;
}
              
// This is a hack -- this ensures the empty list is of type list[RType], not list[Void] or list[Value]
list[RType] mkEmptyList() { return tail([makeVoidType()]); }

// Same hack -- this ensures the empty list is of type list[ScopeItemId], not list[Void] or list[Value]
list[ScopeItemId] mkEmptySIList() { return tail([3]); }

//
// Pretty printers for scope information
//
public str prettyPrintSI(ScopeItem si) {
	switch(si) {
		case ModuleLayer(x) : return "ModuleLayer: " + prettyPrintName(x);
		
		case FunctionLayer(x,t,ags,_,_,_,_) : return "FunctionLayer: " + prettyPrintType(t) + " " + prettyPrintName(x) + "(" + joinList(ags,prettyPrintSI,",","") + ")";
		
		case ClosureLayer(t,ags,_,_) : return "ClosureLayer: " + prettyPrintType(t) + " (" + joinList(ags,prettyPrintSI,",","") + ")";
		
		case VoidClosureLayer(ags,_,_) : return "VoidClosureLayer: (" + joinList(ags,prettyPrintSI,",","") + ")";
		
		case VariableItem(x,t,_) : return "VariableItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case FormalParameterItem(x,t,_) : return "FormalParameterItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case LabelItem(x,_) : return "LabelItem: " + prettyPrintName(x);

		case AliasItem(tn,ta,_,_) : return "AliasItem: " + prettyPrintUserType(tn) + " = " + prettyPrintType(ta);
			
		case ConstructorItem(cn,tas,_,_) : 	return "Constructor: " + prettyPrintName(cn) + "(" + prettyPrintTAList(tas) + ")";
		
		case ADTItem(ut, vs, _, _) : return "ADT: " + prettyPrintUserType(ut) + " = " + joinList(vs,prettyPrintSI," | ","");
		 			
		case BlockLayer(_) : return "BlockLayer";
		
		case PatternMatchLayer(_) : return "PatternMatchLayer";
		
		case BooleanExpLayer(_) : return "BooleanExpLayer";
		
		case AnnotationItem(x,atyp,otyp,_,_) : return "Annotation: <prettyPrintType(atyp)> <prettyPrintType(otyp)>@<prettyPrintName(x)>";
	}
}

public set[ScopeItemId] filterNamesForNamespace(ScopeInfo scopeInfo, set[ScopeItemId] scopeItems, Namespace namespace) {
	set[ScopeItemId] filteredItems = { };
	for (itemId <- scopeItems) {
		switch(namespace) {
			case ModuleName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case ModuleLayer(_) : filteredItems += itemId;
				}	
			}
			
			case LabelName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case LabelItem(_,_) : filteredItems += itemId;
				}	
			}
			
			case FCVName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case FunctionLayer(_,_,_,_,_,_,_) : filteredItems += itemId;
					case VariableItem(_,_,_) : filteredItems += itemId;
					case FormalParameterItem(_,_,_) : filteredItems += itemId;
					case ConstructorItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
					
			case TypeName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case ADTItem(_,_,_,_) : filteredItems += itemId;
				}	
			}
			
			case AnnotationName() : {
				switch(scopeInfo.scopeItemMap[itemId]) {
					case AnnotationItem(_,_,_,_) : filteredItems += itemId;
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
public set[ScopeItemId] getItemsForNameWBound(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x, set[Namespace] containingNamespaces, bool bounded) {
	set[ScopeItemId] foundItems = { };
	
	// First, find all the scope items at the current level of scope that match
	// the name we are looking for.
	for (itemId <- scopeInfo.scopeRel[currentScopeId]) {
		switch(scopeInfo.scopeItemMap[itemId]) {
			case FormalParameterItem(x,_,_) : foundItems += itemId;
			
			case VariableItem(x,_,_) : foundItems += itemId;
			
			case FunctionLayer(x,_,_,_,_,_,_) : foundItems += itemId;
			
			case LabelItem(x,_) : foundItems += itemId;

			case ConstructorItem(x,_,_,_) : foundItems += itemId;
			
			case ModuleLayer(x) : foundItems += itemId;
			
			case AnnotationItem(x,_,_,_) : foundItems += itemId;			
		}
	}

	// Now, filter it down based on the namespaces we are looking for
	foundItems = { f | ns <- containingNamespaces, f <- filterNamesForNamespace(scopeInfo, foundItems, ns) };
		
	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level, Module, is reached (note there is no match for finding
	// the parent of a Module below, since modules do not have parents).
	// If this is a bounded search, don't pass through function boundaries.
	if (size(foundItems) == 0) {
		switch(scopeInfo.scopeItemMap[currentScopeId]) {
			case FunctionLayer(_,_,_,_,_,_,parentScopeId) : if (!bounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);

			case ClosureLayer(_,_,_,parentScopeId) : if (!bounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);

			case VoidClosureLayer(_,_,parentScopeId) : if (!bounded) foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);
			
			case BlockLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);
			
			case PatternMatchLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);
			
			case BooleanExpLayer(parentScopeId) : foundItems = getItemsForNameWBound(scopeInfo,parentScopeId,x,containingNamespaces,bounded);
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
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { FCVName() }, false);
}

public set[ScopeItemId] getItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { FCVName() }, true);
}

public set[ScopeItemId] getAnnotationItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { AnnotationName() }, false);
}

public set[ScopeItemId] getAnnotationItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { AnnotationName() }, true);
}

public set[ScopeItemId] getLabelItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { LabelName() }, false);
}

public set[ScopeItemId] getLabelItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { LabelName() }, true);
}

public set[ScopeItemId] getTypeItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { TypeName() }, false);
}

public set[ScopeItemId] getTypeItemsForNameFB(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
	return getItemsForNameWBound(scopeInfo, currentScopeId, x, { TypeName() }, true);
}

//
// TODO: This should throw an exception when the type of an untyped name (e.g., a label) is requested
//
public RType getTypeForItem(ScopeInfo scopeInfo, ScopeItemId itemId) {
	switch(scopeInfo.scopeItemMap[itemId]) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionLayer(_,t,paramIds,_,false,_,_) : 
			return makeFunctionType(t,[getTypeForItem(scopeInfo, paramId) | paramId <- paramIds]);
		
		case FunctionLayer(_,t,paramIds,_,true,_,_) : {
			list[RType] paramTypes = [getTypeForItem(scopeInfo, paramId) | paramId <- paramIds];
			if (size(paramTypes) == 0)
				paramTypes = [ makeVarArgsType(makeValueType()) ];
			else
				paramTypes += makeVarArgsType(head(tail(paramTypes,1))); 
			return makeFunctionType(t,paramTypes);
		}
			
		case ConstructorItem(n,tas,adtParentId,_) : return makeConstructorType(n,tas,getTypeForItem(scopeInfo,adtParentId));
		
		case ADTItem(ut,_,_,_) : return RTypeUser(ut); // TODO: Should also extract type parameters
		
		default : { 
			if (debug) println("NAMESPACE: Requesting type for item : " + prettyPrintSI(scopeInfo.scopeItemMap[itemId])); 
			return makeVoidType(); 
		}
	}
}

//
// Given a tree representing a module, build the namespace. Note that
// we only process one module at a time, although this code can
// trigger the processing of other modules that are imported.
//
// TODO: This currently only handles one module, we should also
// process imported modules. See also handleModuleHeader below.
//
public ScopeInfo buildNamespace(Tree t) {
	ScopeInfo theInfo = createNewScopeInfo();

	// Overly detailed comments follow...
	// This happens in four main steps. First, the module header is processed, creating a new
	// scope for the module. The second step switches us into this scope. The third step then
	// processes the module body in this module scope. The fourth step switches back to the
	// scope from entry. Finally, the resulting scopeInfo after all this is returned, with the
	// top scope id set to the scope id of the module.
	if ((Module) `<Header h> <Body b>` := t) {
		AddedItemPair sip = handleModuleHeaderNamesOnly(h, t@\loc, theInfo);
		ScopeUpdatePair sup = changeCurrentScope(sip.addedId, sip.scopeInfo);
		theInfo = handleModuleBody(b, sup.scopeInfo);
		sup = changeCurrentScope(sup.oldScopeId, theInfo);
		return sup.scopeInfo[topScopeItemId = sip.addedId];
	}
 
        throw "missed a case for <t.prod>";
}		

//
// Process the header of a module, returning a scope item representing
// the module scope.
//
// TODO: This currently just processes the name of the module. We will
// need to also handle the imports, potentially building a graph that
// we can use to "solve" the name-related dependencies, and we may also
// need the module parameters.
//
// TODO: Should handle tags
//
public AddedItemPair handleModuleHeaderNamesOnly(Header h, loc l, ScopeInfo scopeInfo) {
	switch(h) {
		case `<Tags t> module <QualifiedName n> <Import* i>` : {
			return addScopeItem(ModuleLayer(convertName(n))[@at=l], l, scopeInfo);
		}		

		case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
			return addScopeItem(ModuleLayer(convertName(n))[@at=l], l, scopeInfo);
		}		
	}
}

//
// Process the individual items contained at the top level of the module.
//
public ScopeInfo handleModuleBody(Body b, ScopeInfo scopeInfo) {
	scopeInfo = handleModuleBodyNamesOnly(b, scopeInfo);
	scopeInfo = handleModuleBodyFull(b, scopeInfo);
	
	return scopeInfo;
}

//
// Gather the names of variables and functions. These are visible throughout the module (a variable 
// can be used in a function declared higher up in the file, for instance) so just the top-level 
// names are gathered first (i.e., we don't descend into function bodies, etc).
//
public ScopeInfo handleModuleBodyNamesOnly(Body b, ScopeInfo scopeInfo) {
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
					scopeInfo = handleVarItemsNamesOnly(tgs, v, typ, vs, scopeInfo);
				}
	
				// Abstract (i.e., without a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
					scopeInfo = handleAbstractFunctionNamesOnly(tgs,v,s,t@\loc,scopeInfo);
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					scopeInfo = handleFunctionNamesOnly(tgs,v,s,fb,t@\loc,scopeInfo);
				}
				
				// Annotation declaration
				case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					scopeInfo = handleAnnotationDeclarationNamesOnly(tgs,v,typ,otyp,t@\loc,scopeInfo);
				}
									
				// Tag declaration
				case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					scopeInfo = handleTagDeclarationNamesOnly(tgs,v,k,n,typs,t@\loc,scopeInfo);
				}
				
				// Rule declaration
				case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
					scopeInfo = handleRuleDeclarationNamesOnly(tgs,n,pwa,t@\loc,scopeInfo);
				}
				
				// Test
				case (Toplevel) `<Test tst> ;` : {
					scopeInfo = handleTestNamesOnly(tst,t@\loc,scopeInfo);
				}
								
				// ADT without variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
					scopeInfo = handleAbstractADTNamesOnly(tgs,v,typ,t@\loc,scopeInfo);
				}
				
				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					scopeInfo = handleADTNamesOnly(tgs,v,typ,vars,t@\loc,scopeInfo);
				}

				// Alias
				case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
					scopeInfo = handleAliasNamesOnly(tgs,v,typ,btyp,t@\loc,scopeInfo);
				}
								
				// View
				case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
					scopeInfo = handleViewNamesOnly(tgs,v,n,sn,alts,t@\loc,scopeInfo);
				}
								
				default: println("NAMESPACE: No match for item");
			}
		}
	}
	
	return scopeInfo;
}

//
// Identify names used inside functions or in static initializers, noting type information. This pass 
// actually descends into functions, building the scope information within them as well.
//
public ScopeInfo handleModuleBodyFull(Body b, ScopeInfo scopeInfo) {
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
					scopeInfo = handleVarItems(tgs, v, typ, vs, scopeInfo);
				}
	
				// Abstract (i.e., without a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
					scopeInfo = handleAbstractFunction(tgs,v,s,t@\loc,scopeInfo);
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					scopeInfo = handleFunction(tgs,v,s,fb,t@\loc,scopeInfo);
				}
				
				// Annotation declaration
				case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					scopeInfo = handleAnnotationDeclaration(tgs,v,typ,otyp,t@\loc,scopeInfo);
				}
									
				// Tag declaration
				case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					scopeInfo = handleTagDeclaration(tgs,v,k,n,typs,t@\loc,scopeInfo);
				}
				
				// Rule declaration
				case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
					scopeInfo = handleRuleDeclaration(tgs,n,pwa,t@\loc,scopeInfo);
				}
				
				// Test
				case (Toplevel) `<Test tst> ;` : {
					scopeInfo = handleTest(tst,t@\loc,scopeInfo);
				}
								
				// ADT without variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
					scopeInfo = handleAbstractADT(tgs,v,typ,t@\loc,scopeInfo);
				}
				
				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					scopeInfo = handleADT(tgs,v,typ,vars,t@\loc,scopeInfo);
				}

				// Alias
				case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
					scopeInfo = handleAlias(tgs,v,typ,btyp,t@\loc,scopeInfo);
				}
								
				// View
				case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
					scopeInfo = handleView(tgs,v,n,sn,alts,t@\loc,scopeInfo);
				}
				
								
				default: println("NAMESPACE: No match for item");
			}
		}
	}
	
	return scopeInfo;
}

//
// Handle variable declarations, with or without initializers
//
// TODO: Should handle tags
//
public ScopeInfo handleVarItemsNamesOnly(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Adding variables in declaration...");
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Duplicate declaration of variable <n>"); 
					if (debug) println("NAMESPACE: Duplicate declaration of variable <n>");
				} else {
					ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
					AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo,  { aip.addedId }, n@\loc);
				}
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Duplicate declaration of variable <n>"); 
					if (debug) println("NAMESPACE: Duplicate declaration of variable <n>");
				} else {
					ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
					AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo,  { aip.addedId }, n@\loc);
				}
			}
		}
	}
	return scopeInfo;
}

//
// Identify any names used inside variable declarations
//
// TODO: Should handle tags
//
public ScopeInfo handleVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeInfo scopeInfo) {
	for (`<Name n> = <Expression e>` <- vs)
		scopeInfo = handleExpression(e, scopeInfo);
	return scopeInfo;
}

//
// Handle standard function declarations (i.e., function declarations with bodies), but
// do NOT descend into the bodies
//
// TODO: Should handle tags (if handled in handleAbstractFunctionNamesOnly, no need to do it here)
//
public ScopeInfo handleFunctionNamesOnly(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeInfo scopeInfo) {
	return handleAbstractFunctionNamesOnly(ts,v,s,l,scopeInfo);		
}

//
// TODO: It may be nice to indicate WHICH function the new function overlaps with. Right now,
// this is just a binary true or false check.
//
public bool checkFunctionOverlap(ScopeItemId newFun, set[ScopeItemId] funs, ScopeInfo scopeInfo) {
	// Get back information for the function we are trying to add
	ScopeItem newFunItem = getScopeItem(newFun, scopeInfo);
	RType newFunItemType = getTypeForItem(scopeInfo, newFun);
	list[RType] newFunItemArgs = getFunctionArgumentTypes(newFunItemType);

	for (fid <- funs) {
		// Get back information for each of the existing functions
		ScopeItem funItem = getScopeItem(fid, scopeInfo);
		RType funItemType = getTypeForItem(scopeInfo, fid);
		list[RType] funItemArgs = getFunctionArgumentTypes(funItemType);

		// Handle extending the argument lists for varargs functions so that both
		// functions are the same arity. This is needed to detect overlap in cases
		// where two functions could overlap in practice, even if not in the syntax
		// of their definitions, such as f(int, str, str) and f(int, str...)
		if (funItem.isVarArgs || newFunItem.isVarArgs) {
			if (funItem.isVarArgs && (size(funItemArgs) < size(newFunItemArgs))) {
				if (debug) println("NAMESPACE: Extending vararg function for comparison");
				RType et = (size(funItemArgs) > 0) ? tail(funItemArgs,1) : makeValueType();
				funItemArgs += [ et | n <- [1 .. (size(newFunItemArgs) - size(funItemArgs))] ];
			} else if (newFunItem.isVarArgs && (size(newFunItemArgs) < size(funItemArgs))) {
				if (debug) println("NAMESPACE: Extending vararg function for comparison");
				RType et = (size(newFunItemArgs) > 0) ? tail(newFunItemArgs,1) : makeValueType();
				newFunItemArgs += [ et | n <- [1 .. (size(funItemArgs) - size(newFunItemArgs))] ];
			}
		}

		// Check to see if both lists of args are the same length; if not, we cannot have an
		// overlap between the two functions.
		if (size(funItemArgs) == size(newFunItemArgs)) {
			if (debug) println("NAMESPACE: Checking functions of matching arity for overlap");
			bool foundIncomparable = false;

			for (n <- domain(funItemArgs)) {
				RType t1 = funItemArgs[n]; RType t2 = newFunItemArgs[n];
				if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
			}
			
			if (!foundIncomparable) {
				if (debug) println("NAMESPACE: Functions have comparable signatures, overlap detected."); 
				return true;
			}
		}
	}

	if (debug) println("NAMESPACE: No overlap found.");
	return false;
}

public bool checkConstructorOverlap(ScopeItemId newCon, set[ScopeItemId] cons, ScopeInfo scopeInfo) {
	// Get back information for the constructor we are trying to add
	ScopeItem newConItem = getScopeItem(newCon, scopeInfo);
	RType newConItemType = getTypeForItem(scopeInfo, newCon);
	list[RType] newConItemArgs = getConstructorArgumentTypes(newConItemType);

	for (cid <- cons) {
		// Get back information for each of the existing constructors
		ScopeItem conItem = getScopeItem(cid, scopeInfo);
		RType conItemType = getTypeForItem(scopeInfo, cid);
		list[RType] conItemArgs = getConstructorArgumentTypes(conItemType);

		// Check to see if both lists of args are the same length; if not, we cannot have an
		// overlap between the two constructors.
		if (size(conItemArgs) == size(newConItemArgs)) {
			if (debug) println("NAMESPACE: Checking constructor of matching arity for overlap");
			bool foundIncomparable = false;

			for (n <- domain(conItemArgs)) {
				RType t1 = conItemArgs[n]; RType t2 = newConItemArgs[n];
				if ( ! ( (t1 == t2) || (subtypeOf(t1,t2)) || (subtypeOf(t2,t1)) ) ) foundIncomparable = true;
			}
			
			if (!foundIncomparable) {
				if (debug) println("NAMESPACE: Constructors have comparable signatures, overlap detected."); 
				return true;
			}
		}
	}

	if (debug) println("NAMESPACE: No overlap found.");
	return false;
}

//
// Handle abstract function declarations (i.e., function declarations without bodies)
//
// TODO: Should handle tags
//
public ScopeInfo handleAbstractFunctionNamesOnly(Tags ts, Visibility v, Signature s, loc l, ScopeInfo scopeInfo) {
	// Add the new function into the scope and process any parameters.
	ScopeInfo addFunction(Name n, RType retType, Parameters ps, list[RType] thrsTypes, bool isPublic, ScopeInfo scopeInfo) {
		set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n));
		ScopeItem si = FunctionLayer(convertName(n), retType, mkEmptySIList(), thrsTypes, false, isPublic, scopeInfo.currentScope)[@at=l];
		AddedItemPair aip = addScopeItemWithParent(si, scopeInfo.currentScope, l, scopeInfo);
		ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);			 
		scopeInfo = handleParametersNamesOnly(ps, sup.scopeInfo);
		sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
		scopeInfo = addItemUses(sup.scopeInfo, { aip.addedId }, n@\loc);

		if (checkFunctionOverlap(aip.addedId, potentialOverlaps, scopeInfo)) {	
			scopeInfo = addScopeError(scopeInfo, n@\loc, "Overlapping overload of function <n> declared");
		}

		return scopeInfo;	
	}
	
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			if (debug) println("NAMESPACE: Found abstract function " + prettyPrintName(convertName(n)));
			scopeInfo = addFunction(n, convertType(t), ps, mkEmptyList(), isPublic(v), scopeInfo);
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : {
			if (debug) println("NAMESPACE: Found abstract function " + prettyPrintName(convertName(n)));
			scopeInfo = addFunction(n, convertType(t), ps, [convertType(thrsi) | thrsi <- thrs], 
									isPublic(v), scopeInfo);
		}
	}
	return scopeInfo;
}

//
// TODO: Should handle tags
//
public ScopeInfo handleAbstractFunction(Tags ts, Visibility v, Signature s, loc l, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle parameter declarations
//
public ScopeInfo handleParametersNamesOnly(Parameters p, ScopeInfo scopeInfo) {
	list[ScopeItemId] siList = [];
	bool varArgs = false;

	// Add each parameter into the scope; the current scope is the function that
	// is being processed.	
	if (`( <Formals f> )` := p) {
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if((Formal)`<Type t> <Name n>` := fp) {
					if (debug) println("NAMESPACE: Adding parameter <n>");
					ScopeItem pitem = (FormalParameterItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = fp@\loc]);
					AddedItemPair aip = addScopeItemWithParent(pitem, scopeInfo.currentScope, fp@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, n@\loc);
					siList += aip.addedId;
				}
			}
		}
	} else if (`( <Formals f> ... )` := p) {
		varArgs = true;
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if((Formal)`<Type t> <Name n>` := fp) {
					if (debug) println("NAMESPACE: Adding parameter <n>");
					ScopeItem pitem = FormalParameterItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = fp@\loc];
					AddedItemPair aip = addScopeItemWithParent(pitem, scopeInfo.currentScope, fp@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, n@\loc);
					siList += aip.addedId;
				}
			}
		}
	}
	
	// Update the function with information on its parameters
	ScopeItem functionItem = getScopeItem(scopeInfo.currentScope, scopeInfo);
	functionItem = (functionItem[parameters = siList])[isVarArgs = varArgs];
	scopeInfo = updateScopeItem(functionItem, scopeInfo.currentScope, scopeInfo);
	
	return scopeInfo;
}

//
// Handle standard function declarations (i.e., function declarations with bodies)
//
// TODO: Should handle tags
//
public ScopeInfo handleFunction(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeInfo scopeInfo) {
	// First, get back the scope item at location l so we can switch into the proper function scope
	if (debug) println("NAMESPACE: Switching to function with signature <s>");
	ScopeItemId functionScope = getItemAtLocation(l, scopeInfo);
	ScopeUpdatePair sup = changeCurrentScope(functionScope,scopeInfo);
	
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			scopeInfo = handleFunctionBody(b,sup.scopeInfo);
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
			scopeInfo = handleFunctionBody(b,sup.scopeInfo);
		}
	}
	
	sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
	return sup.scopeInfo;
}

//
// Handle function bodies
//
public ScopeInfo handleFunctionBody(FunctionBody fb, ScopeInfo scopeInfo) {
	if (`{ <Statement* ss> }` := fb) {
		for (s <- ss) {
			scopeInfo = handleStatement(s, scopeInfo);
		}
	}
	return scopeInfo;
}

//
// Check is visibility represents public or private
//
private bool isPublic(Visibility v) {
	if (`public` := v)
		return true;
	else
		return false;
}

//
// Handle alias declarations
//
// TODO: Should handle tags
//
public ScopeInfo handleAliasNamesOnly(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found alias: <aliasType> = <aliasedType>");
	
	ScopeItem aliasItem = AliasItem(convertUserType(aliasType), convertType(aliasedType), isPublic(v), scopeInfo.currentScope)[@at = l];
	AddedItemPair aip = addScopeItemWithParent(aliasItem, scopeInfo.currentScope, l, scopeInfo);
	scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, getUserTypeRawName(aliasType)@\loc);
	return scopeInfo;
}

//
// TODO: should handle tags
//
public ScopeInfo handleAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle abstract ADT declarations (ADT's without variants)
//
// TODO: Should handle tags
//
public ScopeInfo handleAbstractADTNamesOnly(Tags ts, Visibility v, UserType adtType, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found Abstract ADT: <adtType>");

	ScopeItem adtItem = ADTItem(convertUserType(adtType), { }, isPublic(v), scopeInfo.currentScope)[@at = l];

	// See if this is already declared
	set[ScopeItemId] items = getTypeItemsForName(scopeInfo,scopeInfo.currentScope,getUserTypeName(convertUserType(adtType)));
	// TODO: Check here to see if size > 1, that would be an error and should not happen
	if (size(items) == 0) {
		AddedItemPair aip = addScopeItemWithParent(adtItem, scopeInfo.currentScope, l, scopeInfo);
		scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, getUserTypeRawName(adtType)@\loc);
	} else {
		scopeInfo = addItemUses(scopeInfo, items, getUserTypeRawName(adtType)@\loc);
	}
	
	return scopeInfo;
}

//
// TODO: Should handle tags
//
public ScopeInfo handleAbstractADT(Tags ts, Visibility v, UserType adtType, loc l, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle abstract ADT declarations (ADT's without variants)
//
// TODO: This should handle cases where constructors are declared over different data
// declarations with the same ADT name; need to verify this as part of tests.
//
public ScopeInfo handleADTNamesOnly(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found ADT: <adtType>");

	ScopeItem adtItem = ADTItem(convertUserType(adtType), { }, isPublic(v), scopeInfo.currentScope)[@at = l];
	ScopeItemId itemId = -1;
	
	// See if this is already declared
	set[ScopeItemId] items = getTypeItemsForName(scopeInfo,scopeInfo.currentScope,getUserTypeName(convertUserType(adtType)));
	// TODO: Check here to see if size > 1, that would be an error and should not happen
	if (size(items) == 0) {
		AddedItemPair aip = addScopeItemWithParent(adtItem, scopeInfo.currentScope, l, scopeInfo);
		scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, getUserTypeRawName(adtType)@\loc);
		itemId = aip.addedId;
	} else if (size(items) == 1) {
		adtItem = getScopeItem(getOneFrom(items),scopeInfo);
		itemId = getOneFrom(items);
		scopeInfo = addItemUses(scopeInfo, items, getUserTypeRawName(adtType)@\loc);
	}

	set[ScopeItemId] variantSet = { };
	
	for (var <- vars) {
		if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
			set[ScopeItemId] potentialOverlaps = getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n));

			ScopeItem constructorItem = ConstructorItem(convertName(n), [ convertTypeArg(targ) | targ <- args ], itemId, scopeInfo.currentScope)[@at = l];
			AddedItemPair aip2 = addScopeItemWithParent(constructorItem, scopeInfo.currentScope, l, scopeInfo);
			scopeInfo = addItemUses(aip2.scopeInfo, { aip2.addedId }, n@\loc);
			variantSet += aip2.addedId; 			

			if (checkConstructorOverlap(aip2.addedId, potentialOverlaps, scopeInfo)) {	
				scopeInfo = addScopeError(scopeInfo, n@\loc, "Overlapping overload of constructor <n> declared");
			}
		}
	}
	
	adtItem.variants += variantSet;
	scopeInfo = updateScopeItem(adtItem, itemId, scopeInfo);
	
	return scopeInfo;
}

//
// TODO: Should handle tags
//
public ScopeInfo handleADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle individual statements
//
// TODO: Verify that names in solve statement must be declared already (i.e. that solve is not a binder)
//
public ScopeInfo handleStatement(Statement s, ScopeInfo scopeInfo) {
	switch(s) {
		case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
			if (debug) println("NAMESPACE: Inside solve statement <s>");
			
			for (v <- vs) {
				if (debug) println("NAMESPACE: Adding use for <v>");
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(v)), v@\loc);
			}
			
			if (`; <Expression e>` := b) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(sb, scopeInfo);		
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			if (debug) println("NAMESPACE: Inside for statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			

			// Open a new boolean scope for the expression list and the statement body
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(b, scopeInfo);

			// Switch back to the prior scope to take expression bound names out of scope			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			if (debug) println("NAMESPACE: Inside while statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			
			
			// Open a new boolean scope for the expression list and the statement body
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(b, scopeInfo); 

			// Switch back to the prior scope to take expression bound names out of scope			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			if (debug) println("NAMESPACE: Inside do statement <s>");
			scopeInfo = handleExpression(e, handleStatement(b, handleLabel(l,scopeInfo)));			
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			if (debug) println("NAMESPACE: Inside if with else statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			
			
			// Open a new boolean scope for the expression list and the statement body
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(bf,handleStatement(bt, scopeInfo)); 

			// Switch back to the prior scope to take expression bound names out of scope			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			if (debug) println("NAMESPACE: Inside if statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			
			
			// Open a new boolean scope for the expression list and the statement body
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(bt, scopeInfo); 

			// Switch back to the prior scope to take expression bound names out of scope			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			if (debug) println("NAMESPACE: Inside switch statement <s>");
			
			scopeInfo = handleExpression(e,handleLabel(l,scopeInfo));						
			for (c <- cs) {
				scopeInfo = handleCase(c, scopeInfo);
			}
		}

		case (Statement)`<Label l> <Visit v>` : {
			if (debug) println("NAMESPACE: Inside visit statement <s>");
			scopeInfo = handleVisit(v, handleLabel(l,scopeInfo));						
		}
			
		case `<Expression e> ;` : {
			if (debug) println("NAMESPACE: Inside expression statement <s>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			if (debug) println("NAMESPACE: Inside assignment statement <s>");
			scopeInfo = handleStatement(b, handleAssignable(a, scopeInfo));
		}
		
		case `assert <Expression e> ;` : {
			if (debug) println("NAMESPACE: Inside assert statement <s>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		case `assert <Expression e> : <Expression em> ;` : {
			if (debug) println("NAMESPACE: Inside assert with message statement <s>");
			scopeInfo = handleExpression(em, handleExpression(e, scopeInfo));
		}
		
		case `return <Statement b>` : {
			if (debug) println("NAMESPACE: Inside return statement <s>");
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `throw <Statement b>` : {
			if (debug) println("NAMESPACE: Inside throw statement <s>");
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `insert <DataTarget dt> <Statement b>` : {
			if (debug) println("NAMESPACE: Inside insert statement <s>");
			scopeInfo = handleStatement(b, handleTarget(dt, scopeInfo));
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			if (debug) println("NAMESPACE: Inside append statement <s>");
			scopeInfo = handleStatement(b, handleTarget(dt, scopeInfo));
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			if (debug) println("NAMESPACE: Inside local function statement <s>");
			
			// First get back the function signature information, creating the scope item
			scopeInfo = handleFunctionNamesOnly(ts,v,sig,fb,s@\loc,scopeInfo);
					
			// Now, descend into the function, processing the body
			scopeInfo = handleFunction(ts,v,sig,fb,s@\loc,scopeInfo);
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			if (debug) println("NAMESPACE: Inside local variable statement <s>");
			scopeInfo = handleLocalVarItems(t,vs,scopeInfo);
		}
		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			if (debug) println("NAMESPACE: Inside dynamic local variable statement <s>");

			// TODO: Handle scoping of dynamics properly
			scopeInfo = handleLocalVarItems(t,vs,scopeInfo);
		}
		
		case `break <Target t> ;` : {
			if (debug) println("NAMESPACE: Inside break statement <s>");
			scopeInfo = handleTarget(dt, scopeInfo);
		}
		
		case `fail <Target t> ;` : {
			if (debug) println("NAMESPACE: Inside fail statement <s>");
			scopeInfo = handleTarget(dt, scopeInfo);
		}
		
		case `continue <Target t> ;` : {
			if (debug) println("NAMESPACE: Inside continue statement <s>");
			scopeInfo = handleTarget(dt, scopeInfo);
		}
		
		case `try <Statement b> <Catch+ cs>` : {
			if (debug) println("NAMESPACE: Inside try without finally statement <s>");

			scopeInfo = handleStatement(b, scopeInfo);
			
			for (ct <- cs)  {
				scopeInfo = handleCatch(ct, scopeInfo);
			}
		}
		
		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			if (debug) println("NAMESPACE: Inside try with finally statement <s>");

			scopeInfo = handleStatement(b, scopeInfo);
			
			for (ct <- cs)  {
				scopeInfo = handleCatch(ct, scopeInfo);
			}

			scopeInfo = handleStatement(bf, scopeInfo);
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			if (debug) println("NAMESPACE: Inside block statement <s>");

			scopeInfo = handleLabel(l,scopeInfo);						

			// Add a scope layer for the block and switch the scope to it			
			ScopeItem blockItem = BlockLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(blockItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

			for (b <- bs) {
				scopeInfo = handleStatement(b,scopeInfo);
			}
	
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
	}
	
	return scopeInfo;
}

//
// Handle individual expressions (which could contain closures, for instance)
//
public ScopeInfo handleExpression(Expression exp, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Inside expression <exp>");
	switch(exp) {
		case (Expression)`<BooleanLiteral bl>` : {
			if (debug) println("NAMESPACE: BooleanLiteral: <exp>");
		}

		case (Expression)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: DecimalIntegerLiteral: <exp>");
		}

		case (Expression)`<OctalIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: OctalIntegerLiteral: <exp>");
		}

		case (Expression)`<HexIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: HexIntegerLiteral: <exp>");
		}

		case (Expression)`<RealLiteral rl>`  : {
			if (debug) println("NAMESPACE: RealLiteral: <exp>");
		}

		// TODO: Interpolation
		case (Expression)`<StringLiteral sl>`  : {
			if (debug) println("NAMESPACE: StringLiteral: <exp>");
		}

		// TODO: Interpolation
		case (Expression)`<LocationLiteral ll>`  : {
			if (debug) println("NAMESPACE: LocationLiteral: <exp>");
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			if (debug) println("NAMESPACE: DateTimeLiteral: <exp>");
		}

		// Name
		case (Expression)`<Name n>`: {
			if (debug) println("NAMESPACE: Name: <exp>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
				if (debug) println("NAMESPACE: Adding use for <n>");
			} else {
				scopeInfo = addScopeError(scopeInfo, n@\loc, "<n> not defined before use.");
				if (debug) println("NAMESPACE: Found undefined use of <n>");
			}
		}
		
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			if (debug) println("NAMESPACE: QualifiedName: <exp>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
				if (debug) println("NAMESPACE: Adding use for <qn>");
			} else {
				scopeInfo = addScopeError(scopeInfo, n@\loc, "<n> not defined before use.");
				if (debug) println("NAMESPACE: Found undefined use of <qn>");
			}
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			if (debug) println("NAMESPACE: ReifiedType: <exp>");
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			if (debug) println("NAMESPACE: Call or Tree: <exp>");
			scopeInfo = handleExpression(e1, scopeInfo);

			// Parameters maintain their own scope for backtracking purposes
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}

			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		// List
		case `[<{Expression ","}* el>]` : {
			if (debug) println("NAMESPACE: List: <exp>");
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			if (debug) println("NAMESPACE: Set: <exp>");
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// Tuple
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			if (debug) println("NAMESPACE: Tuple: <exp>");
			scopeInfo = handleExpression(ei, scopeInfo);
			for (eli <- el) {
				scopeInfo = handleExpression(eli, scopeInfo);
			}
		}

		// TODO: Map: Need to figure out a syntax that works for matching this
		// case ...
		//case appl(prod([],cf(sort("Expression")),no-attrs),[...]) :

		// Closure
		case `<Type t> <Parameters p> { <Statement+ ss> }` : {
			if (debug) println("NAMESPACE: Closure: <exp>");
			
			// Create and switch scope to the closure layer
			ScopeItem si = ClosureLayer(convertType(t), mkEmptySIList(), false, scopeInfo.currentScope)[@at=exp@\loc];
			AddedItemPair aip = addScopeItemWithParent(si, scopeInfo.currentScope, l, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			// Add parameters to the scope			 
			scopeInfo = handleParametersNamesOnly(p, sup.scopeInfo);
			
			// Handle the closure body
			for (s <- ss) scopeInfo = handleStatement(s, scopeInfo);
			
			// Switch back to the original scope
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			if (debug) println("NAMESPACE: VoidClosure: <exp>");
			
			// Create and switch scope to the closure layer
			ScopeItem si = VoidClosureLayer(mkEmptySIList(), false, scopeInfo.currentScope)[@at=exp@\loc];
			AddedItemPair aip = addScopeItemWithParent(si, scopeInfo.currentScope, l, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			// Add parameters to the scope			 
			scopeInfo = handleParametersNamesOnly(p, sup.scopeInfo);
			
			// Handle the closure body
			for (s <- ss) scopeInfo = handleStatement(s, scopeInfo);
			
			// Switch back to the original scope
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {
			if (debug) println("NAMESPACE: NonEmptyBlock: <exp>");
			
			// Create and switch into a new scope layer for the block
			ScopeItem blockItem = BlockLayer(scopeInfo.currentScope)[@at=exp@\loc];
			AddedItemPair aip = addScopeItemWithParent(blockItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

			for (s <- ss) {
				scopeInfo = handleStatement(s,scopeInfo);
			}
	
			// After processing the scope body, switch back to the surrounding scope
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;		
		}
		
		// Visit
		case (Expression) `<Label l> <Visit v>` : {
			if (debug) println("NAMESPACE: Visit: <exp>");
			scopeInfo = handleVisit(v, handleLabel(l,scopeInfo));						
		}
		
		// ParenExp
		case `(<Expression e>)` : {
			if (debug) println("NAMESPACE: ParenExp: <exp>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			if (debug) println("NAMESPACE: Range: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			if (debug) println("NAMESPACE: StepRange: <exp>");
			scopeInfo = handleExpression(e3, handleExpression(e2, handleExpression(e1, scopeInfo)));
		}

		// ReifyType
		case (Expression)`#<Type t>` : {
			if (debug) println("NAMESPACE: ReifyType: <exp>");
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			if (debug) println("NAMESPACE: FieldUpdate: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
			
			// NOTE: Here we do not add uses for n, since n should be the name of a field
			// on the type e1. We will instead check this inside the type checker, since we
			// need to know the type first before we can do this.
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			if (debug) println("NAMESPACE: FieldAccess: <exp>");
			scopeInfo = handleExpression(e1, scopeInfo);
			
			// NOTE: Here we do not add uses for n, since n should be the name of a field
			// on the type e1. We will instead check this inside the type checker, since we
			// need to know the type first before we can do this.
		}

		// FieldProject
		case `<Expression e1> < <{Field ","}+ fl> >` : {
			if (debug) println("NAMESPACE: FieldProject: <exp>");
			scopeInfo = handleExpression(e1, scopeInfo);
			
			// NOTE: Here we do not add uses for the fields, since we need to know the type of e1
			// to check this (these are not uses of names defined in local scope). We will instead 
			// check this inside the type checker. A field must be either a name or a number.
		}

		// Subscript
		case `<Expression e1> [ <{Expression ","}+ el> ]` : {
			if (debug) println("NAMESPACE: Subscript <exp>");
			scopeInfo = handleExpression(e1, scopeInfo);
			for (e <- el) scopeInfo = handleExpression(e, scopeInfo);
		}

		// IsDefined
		case `<Expression e> ?` : {
			if (debug) println("NAMESPACE: IsDefined: <exp>");
			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e, scopeInfo);

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e, scopeInfo);
			}	
		}

		// Negation
		case `! <Expression e>` : {
			if (debug) println("NAMESPACE: Negation: <exp>");
			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e, scopeInfo);

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e, scopeInfo);
			}	
		}

		// Negative
		case `- <Expression e> ` : {
			if (debug) println("NAMESPACE: Negative: <exp>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		// TransitiveReflexiveClosure
		case `<Expression e> * ` : {
			if (debug) println("NAMESPACE: TransitiveReflexiveClosure: <exp>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		// TransitiveClosure
		case `<Expression e> + ` : {
			if (debug) println("NAMESPACE: TransitiveClosure: <exp>");
			scopeInfo = handleExpression(e, scopeInfo);
		}

		// GetAnnotation
		case `<Expression e> @ <Name n>` : {
			if (debug) println("NAMESPACE: GetAnnotation: <exp>");
			scopeInfo = handleExpression(e, scopeInfo);
			if (debug) println("NAMESPACE: Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getAnnotationItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			if (debug) println("NAMESPACE: SetAnnotation: <exp>");
			scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			if (debug) println("NAMESPACE: Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getAnnotationItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			if (debug) println("NAMESPACE: Composition: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// Product
		case `<Expression e1> * <Expression e2>` : {
			if (debug) println("NAMESPACE: Times: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			if (debug) println("NAMESPACE: Join: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			if (debug) println("NAMESPACE: Div: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			if (debug) println("NAMESPACE: Mod: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}

		// Intersection
		case `<Expression e1> & <Expression e2>` : {
			if (debug) println("NAMESPACE: Intersection: <exp>");
			scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
		}
		
		// Plus
		case `<Expression e1> + <Expression e2>` : {
			if (debug) println("NAMESPACE: Plus: <exp>");
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			if (debug) println("NAMESPACE: Minus: <exp>");
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			if (debug) println("NAMESPACE: NotIn: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			if (debug) println("NAMESPACE: In: <exp>");
			
			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			if (debug) println("NAMESPACE: LessThan: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			if (debug) println("NAMESPACE: LessThanOrEq: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			if (debug) println("NAMESPACE: GreaterThan: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			if (debug) println("NAMESPACE: GreaterThanOrEq: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			if (debug) println("NAMESPACE: Equals: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			if (debug) println("NAMESPACE: NotEquals: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			if (debug) println("NAMESPACE: IfThenElse: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e3,handleExpression(e2,handleExpression(e1, scopeInfo)));
				
				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e3,handleExpression(e2,handleExpression(e1, scopeInfo)));
			}	
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			if (debug) println("NAMESPACE: IfDefinedOtherwise: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			if (debug) println("NAMESPACE: Implication: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			if (debug) println("NAMESPACE: Equivalence: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			if (debug) println("NAMESPACE: And: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			if (debug) println("NAMESPACE: Or: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e2, handleExpression(e1, scopeInfo));
			}	
		}
		
		// Match
		case `<Pattern p> := <Expression e>` : {
			if (debug) println("NAMESPACE: Match: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));
			}	
		}

		// NoMatch
		case `<Pattern p> !:= <Expression e>` : {
			if (debug) println("NAMESPACE: NoMatch: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));
			}	
		}

		// Enumerator
		case `<Pattern p> <- <Expression e>` : {
			if (debug) println("NAMESPACE: Enumerator: <exp>");

			if (BooleanExpLayer(_) !:= scopeInfo.scopeItemMap[scopeInfo.currentScope]) {
				ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=exp@\loc];
				AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, exp@\loc, scopeInfo);
				ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));

				sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
				scopeInfo = sup.scopeInfo;
			} else {
				scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));
			}	
		}
		
		// Set Comprehension
		case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` : {
			if (debug) println("NAMESPACE: SetComprehension: <exp>");
			for (e <- er) scopeInfo = handleExpression(e, scopeInfo);
			for (e <- el) scopeInfo = handleExpression(e, scopeInfo);
		}

		// List Comprehension
		case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` : {
			if (debug) println("NAMESPACE: ListComprehension: <exp>");
			for (e <- er) scopeInfo = handleExpression(e, scopeInfo);
			for (e <- el) scopeInfo = handleExpression(e, scopeInfo);
		}
		
		// Map Comprehension
		case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` : {
			if (debug) println("NAMESPACE: MapComprehension: <exp>");
			for (e <- er) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleExpression(et, handleExpression(ef, scopeInfo));
		}

		// Reducer
		// TODO: I think a boolean scope is fine here, but may want to use a block scope instead 
		case `( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
			if (debug) println("NAMESPACE: Reducer: <exp>");
			
			scopeInfo = handleExpression(e1, scopeInfo);
			
			// Open a new boolean scope for the generators
			ScopeItem booleanExpItem = BooleanExpLayer(scopeInfo.currentScope)[@at=s@\loc];
			AddedItemPair aip = addScopeItemWithParent(booleanExpItem, scopeInfo.currentScope, s@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			for (e <- egs) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleExpression(er, scopeInfo);
			
			// Switch back to the prior scope to take expression bound names out of scope			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
		
		// It
		case `it` : {
			if (debug) println("NAMESPACE: It: <exp>");
		}
			
		// All 
		case `all ( <{Expression ","}+ egs> )` : {
			if (debug) println("NAMESPACE: All: <exp>");
			for (e <- egs) scopeInfo = handleExpression(e, scopeInfo);
		}

		// Any 
		case `all ( <{Expression ","}+ egs> )` : {
			if (debug) println("NAMESPACE: Any: <exp>");
			for (e <- egs) scopeInfo = handleExpression(e, scopeInfo);
		}
		
		// TODO: Look in embedding.sdf for more expression productions
		
		// TODO: Add support for interpolation
	}
	
	return scopeInfo;
}

//
// Handle individual cases
//
public ScopeInfo handleCase(Case c, ScopeInfo scopeInfo) {
	switch(c) {
		case `case <PatternWithAction p>` : {
			scopeInfo = handlePatternWithAction(p, scopeInfo);
		}
		
		case `default : <Statement b>` : {
			scopeInfo = handleStatement(b, scopeInfo);
		}
	}
	
	return scopeInfo;
}

//
// Handle assignables
//
public ScopeInfo handleAssignable(Assignable a, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Inside assignable <a>");
	switch(a) {
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("NAMESPACE: Adding use for <qn>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `<Assignable al> [ <Expression e> ]` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);			
		}
		
		case `<Assignable al> . <Name n>` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			if (debug) println("NAMESPACE: Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `<Assignable al> ? <Expression e>` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);			
		}
		
		case `<Assignable al> @ <Name n>` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			if (debug) println("NAMESPACE: Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getAnnotationItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}
		
		case `< <{Assignable ","}+ al> >` : {
			for (ali <- al) {
				scopeInfo = handleAssignable(ali, scopeInfo);
			}
		}
		
		case `<Name n> ( <{Assignable ","}+ al> )` : {
			if (debug) println("NAMESPACE: Adding use for <qn>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
			for (ali <- al) {
				scopeInfo = handleAssignable(ali, scopeInfo);
			}
		}
	}
	
	return scopeInfo;
}

//
// Handle local variable declarations, with or without initializers
//
public ScopeInfo handleLocalVarItems(Type t, {Variable ","}+ vs, ScopeInfo scopeInfo) {
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Illegal redefinition of <n>.");
					if (debug) println("NAMESPACE: Illegal redefinition of <n>");
				} else {
					ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
					AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo,  { aip.addedId }, n@\loc);
				}
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Illegal redefinition of <n>.");
					if (debug) println("NAMESPACE: Illegal redefinition of <n>");
				} else {
					ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
					AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
					scopeInfo = addItemUses(aip.scopeInfo,  { aip.addedId }, n@\loc);
				}
				
				scopeInfo = handleExpression(e, scopeInfo);
			}
		}
	}
	return scopeInfo;
}

//
// Handle the "catch" part of a try/catch statement
//
public ScopeInfo handleCatch(Catch c, ScopeInfo scopeInfo) {
	switch(c) {
		case `catch : <Statement b>` : {
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `catch <Pattern p> : <Statement b>` : {
			scopeInfo = handleStatement(b, handlePattern(p, scopeInfo));
		}
	}
	
	return scopeInfo;
}		

//
// Handle labels
//
public ScopeInfo handleLabel(Label l, ScopeInfo scopeInfo) {
	if ((Label)`<Name n> :` := l) {
		// First, check to see if this label already exists
		set[ScopeItemId] ls = getLabelItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n));
		if (size(ls) == 0) {		
			ScopeItem li = LabelItem(convertName(n), scopeInfo.currentScope)[@at = l@\loc];
			AddedItemPair aip = addScopeItemWithParent(li, scopeInfo.currentScope, l@\loc, scopeInfo);
			scopeInfo = aip.scopeInfo;
		} else {
			scopeInfo = addScopeError(scopeInfo, n@\loc, "Label <n> has already been defined.");					
		}
	}
	return scopeInfo;
}

//
// Handle visits
//
public ScopeInfo handleVisit(Visit v, ScopeInfo scopeInfo) {
	switch(v) {
		case `visit (<Expression se>) { <Case+ cs> }` : {
			scopeInfo = handleExpression(se, scopeInfo);
			for (c <- cs) scopeInfo = handleCase(c, scopeInfo);
		}
		
		case `<Strategy st> visit (<Expression se>) { <Case+ cs> }` : {
			scopeInfo = handleExpression(se, scopeInfo);
			for (c <- cs) scopeInfo = handleCase(c, scopeInfo);		
		}		
	}
	
	return scopeInfo;
}

public ScopeInfo addFreshVariable(RName n, loc nloc, ScopeInfo scopeInfo) {
	RType freshType = makeInferredType(scopeInfo.freshType);
	scopeInfo.inferredTypeMap[scopeInfo.freshType] = freshType;
	scopeInfo.freshType = scopeInfo.freshType + 1;
	ScopeItem vi = VariableItem(n, freshType, scopeInfo.currentScope)[@at = nloc];
	AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, nloc, scopeInfo);
	scopeInfo = addItemUses(aip.scopeInfo, { aip.addedId }, nloc);
	return scopeInfo;
}

//
// Handle patterns
//
public ScopeInfo handlePattern(Pattern pat, ScopeInfo scopeInfo) {
	switch(pat) {
		case (Pattern)`<BooleanLiteral bl>` : {
			if (debug) println("NAMESPACE: BooleanLiteralPattern: <pat>");
		}

		case (Pattern)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: DecimalIntegerLiteralPattern: <pat>");
		}

		case (Pattern)`<OctalIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: OctalIntegerLiteralPattern: <pat>");
		}

		case (Pattern)`<HexIntegerLiteral il>`  : {
			if (debug) println("NAMESPACE: HexIntegerLiteralPattern: <pat>");
		}

		case (Pattern)`<RealLiteral rl>`  : {
			if (debug) println("NAMESPACE: RealLiteralPattern: <pat>");
		}

		// TODO: Interpolation
		case (Pattern)`<StringLiteral sl>`  : {
			if (debug) println("NAMESPACE: StringLiteralPattern: <pat>");
		}

		// TODO: Interpolation
		case (Pattern)`<LocationLiteral ll>`  : {
			if (debug) println("NAMESPACE: LocationLiteralPattern: <pat>");
		}

		case (Pattern)`<DateTimeLiteral dtl>`  : {
			if (debug) println("NAMESPACE: DateTimeLiteralPattern: <pat>");
		}

		// Name
		case (Pattern)`<Name n>`: {
			if (debug) println("NAMESPACE: NamePattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
				if (debug) println("NAMESPACE: Adding use for <n>");
			} else {
				scopeInfo = addFreshVariable(convertName(n), n@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh type for for <n>");
			}
		}
		
		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			if (debug) println("NAMESPACE: QualifiedNamePattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
				if (debug) println("NAMESPACE: Adding use for <qn>");
			} else {
				scopeInfo = addFreshVariable(convertName(qn), qn@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh type for for <qn>");
			}
		}

		// ReifiedType
		case `<BasicType t> ( <{Pattern ","}* pl> )` : {
			if (debug) println("NAMESPACE: ReifiedTypePattern: <pat>");
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// CallOrTree
		case `<Pattern p1> ( <{Pattern ","}* pl> )` : {
			if (debug) println("NAMESPACE: CallOrTreePattern: <pat>");
			scopeInfo = handlePattern(p1, scopeInfo);
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			if (debug) println("NAMESPACE: ListPattern: <pat>");
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			if (debug) println("NAMESPACE: SetPattern: <pat>");
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// Tuple
		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("NAMESPACE: TuplePattern: <pat>");
			scopeInfo = handlePattern(pi, scopeInfo);
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// TODO: Map: Need to figure out a syntax that works for matching this

		// Descendant
		case `/ <Pattern p>` : {
			if (debug) println("NAMESPACE: DescendantPattern: <pat>");
			scopeInfo = handlePattern(p, scopeInfo);
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			if (debug) println("NAMESPACE: VariableBecomesPattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
				if (debug) println("NAMESPACE: Adding use for <n>");
			} else {
				scopeInfo = addFreshVariable(convertName(qn), qn@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh type for for <n>");
			}
			scopeInfo = handlePattern(p, scopeInfo);
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			if (debug) println("NAMESPACE: TypedVariableBecomesPattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addScopeError(scopeInfo, pat@\loc, "Illegal shadowing of already declared name: <n>");
			} else {
				ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = n@\loc];
				AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, n@\loc, scopeInfo);
				scopeInfo = aip.scopeInfo;			
				if (debug) println("NAMESPACE: Adding new declaration for <n>");
			}
			scopeInfo = handlePattern(p, scopeInfo);
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			if (debug) println("NAMESPACE: GuardedPattern: <pat>");
			scopeInfo = handlePattern(p, scopeInfo);
		}			
		
		// Anti
		case `! <Pattern p>` : {
			if (debug) println("NAMESPACE: AntiPattern: <pat>");
			scopeInfo = handlePattern(p, scopeInfo);
		}
	}
	
	return scopeInfo;
}

//
// Handle Pattern with Action productions
//
public ScopeInfo handlePatternWithAction(PatternWithAction pwa, ScopeInfo scopeInfo) {
	switch(pwa) {
		case `<Pattern p> => <Expression e>` : {
			ScopeItem patternItem = PatternMatchLayer(scopeInfo.currentScope)[@at=pwa@\loc];
			AddedItemPair aip = addScopeItemWithParent(patternItem, scopeInfo.currentScope, pwa@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));

			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			ScopeItem patternItem = PatternMatchLayer(scopeInfo.currentScope)[@at=pwa@\loc];
			AddedItemPair aip = addScopeItemWithParent(patternItem, scopeInfo.currentScope, pwa@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);
			
			scopeInfo = handlePattern(p, scopeInfo);
			for (e <- es) { 
				scopeInfo = handleExpression(e, scopeInfo);
			}
			scopeInfo = handleExpression(er, scopeInfo);
						
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
		
		case `<Pattern p> : <Statement s>` : {
			ScopeItem patternItem = PatternMatchLayer(scopeInfo.currentScope)[@at=pwa@\loc];
			AddedItemPair aip = addScopeItemWithParent(patternItem, scopeInfo.currentScope, pwa@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

			scopeInfo = handleStatement(s, handlePattern(p, scopeInfo));			
			
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
	}
	
	return scopeInfo;
}

public ScopeInfo handleTarget(DataTarget dt, ScopeInfo scopeInfo) {
	if ((DataTarget)`<Name n> :` := dt) {
		set[ScopeItemId] items = getLabelItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n));
		if (size(items) == 1) {
			scopeInfo = addItemUses(scopeInfo, items, n@\loc);
		} else if (size(items) == 0) {
			scopeInfo = addScopeError(scopeInfo, n@\loc, "Label <n> has not been defined.");			
		} else {
			scopeInfo = addScopeError(scopeInfo, n@\loc, "Label <n> has multiple definitions.");
		}
		
	}
	return scopeInfo;
}

public bool hasRType(ScopeInfo scopeInfo, loc l) {
	if (l in scopeInfo.itemUses || l in scopeInfo.scopeErrorMap)
		return true;
	return false;
}

public RType getRType(ScopeInfo scopeInfo, loc l) {
	set[ScopeItemId] items = (l in scopeInfo.itemUses) ? scopeInfo.itemUses[l] : { };
	set[str] scopeErrors = (l in scopeInfo.scopeErrorMap) ? scopeInfo.scopeErrorMap[l] : { };
	
	if (size(scopeErrors) == 0) {
		if (size(items) == 0) {
			// TODO: Should be an exception
			return makeVoidType();
		} else if (size(items) == 1) {
			return getTypeForItem(scopeInfo, getOneFrom(items));
		} else {
			return RTypeOverloaded({ getTypeForItem(scopeInfo, sii) | sii <- items });
		}
	} else {
		return collapseFailTypes({ makeFailType(s,l) | s <- scopeErrors });
	}
}

public Tree decorateNames(Tree t, ScopeInfo scopeInfo) {
	return visit(t) {
		case `<Name n>` => hasRType(scopeInfo, n@\loc) ? n[@rtype = getRType(scopeInfo, n@\loc)] : n
		
		case `<QualifiedName qn>` => hasRType(scopeInfo, qn@\loc) ? qn[@rtype = getRType(scopeInfo, qn@\loc)] : qn
	}
}
