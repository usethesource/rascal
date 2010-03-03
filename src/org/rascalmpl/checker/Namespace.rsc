module org::rascalmpl::checker::Namespace

import org::rascalmpl::checker::Types;
import org::rascalmpl::checker::ListUtils;

import List;
import Graph;
import IO;
import Set;

import languages::rascal::syntax::Rascal;

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
// 4. Variables introduced in comprehensions and patterns should not have scope outside
//    of the related structures; for comprehensions this is outside of the comprehension,
//    for patterns this is outside of the action or surrounding code (used in an if,
//    it has scope inside the if). It may be easiest to just always introduce a new
//    scoping layer when entering one of these constructs, since that would then ensure
//    scope is cleaned up properly upon exiting.
//
// 5. DONE Give each scope item a unique ID. This will make it easier to keep them unique,
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
	| BlockLayer(ScopeItemId parentId)
	| VariableItem(RName variableName, RType variableType, ScopeItemId parentId)
	| FormalParameterItem(RName parameterName, RType parameterType, ScopeItemId parentId)
	| LabelItem(RName labelName, ScopeItemId parentId)
	| AliasItem(RUserType aliasType, RType aliasedType, bool isPublic, ScopeItemId parentId)
	| ConstructorItem(RName constructorName, list[RTypeArg] constructorArgs, ScopeItemId adtParentId, ScopeItemId parentId)
	| ADTItem(RUserType adtType, set[ScopeItemId] variants, bool isPublic, ScopeItemId parentId) 
	| DummyItem()
	| PatternMatchLayer(ScopeItemId parentId)
;

data Namespace =
	  ModuleName()
	| LabelName()
	| FCVName()
	| TypeName()
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
                        ScopeItemId nextScopeId, map[ScopeItemId,ScopeItem] scopeItemMap, 
                        map[loc,ScopeItemId] itemLocations, ScopeItemId currentScope];

alias AddedItemPair = tuple[ScopeInfo scopeInfo, ScopeItemId addedId];
alias ScopeUpdatePair = tuple[ScopeInfo scopeInfo, ScopeItemId oldScopeId];
                        
public ScopeInfo createNewScopeInfo() {
	return < -1, { }, ( ), 0, ( ), ( ), 0 >;
}                    

public ScopeInfo addItemUse(ScopeInfo scopeInfo, ScopeItemId scopeItem, loc l) {
	if (l in scopeInfo.itemUses)
		scopeInfo = scopeInfo[itemUses = (scopeInfo.itemUses + ( l : (scopeInfo.itemUses[l] + scopeItem)))];
	else
		scopeInfo = scopeInfo[itemUses = (scopeInfo.itemUses + ( l : { scopeItem } ))];
	return scopeInfo;
}

public ScopeInfo addItemUses(ScopeInfo scopeInfo, set[ScopeItemId] scopeItems, loc l) {
	if (l in scopeInfo.itemUses)
		scopeInfo = scopeInfo[itemUses = (scopeInfo.itemUses + ( l : (scopeInfo.itemUses[l] + scopeItems)))];
	else
		scopeInfo = scopeInfo[itemUses = (scopeInfo.itemUses + ( l : scopeItems ))];
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
		if (debug) println("Error, trying to retrieve item from unassociated location!");
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
		
		case VariableItem(x,t,_) : return "VariableItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case FormalParameterItem(x,t,_) : return "FormalParameterItem: " + prettyPrintType(t) + " " + prettyPrintName(x);
		
		case LabelItem(x,_) : return "LabelItem: " + prettyPrintName(x);

		case AliasItem(tn,ta,_,_) : return "AliasItem: " + prettyPrintUserType(tn) + " = " + prettyPrintType(ta);
			
		case ConstructorItem(cn,tas,_,_) : 	return prettyPrintName(cn) + "(" + prettyPrintTAList(tas) + ")";
		
		case ADTItem(ut, vs, _, _) : return prettyPrintUserType(ut) + " = " + joinList(vs,prettyPrintSI," | ","");
		 			
		case BlockLayer(_) : return "BlockLayer";
		
		case PatternMatchLayer(_) : return "PatternMatchLayer";
	}
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
public set[ScopeItemId] getItemsForName(ScopeInfo scopeInfo, ScopeItemId currentScopeId, RName x) {
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
		}
	}

	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level, Module, is reached (note there is no match for finding
	// the parent of a Module below, since modules do not have parents).
	if (size(foundItems) == 0) {
		switch(scopeInfo.scopeItemMap[currentScopeId]) {
			case FunctionLayer(_,_,_,_,_,_,_,parentScopeId) : foundItems = getItemsForName(scopeInfo,parentScopeId,x);
			
			case BlockLayer(parentScopeId) : foundItems = getItemsForName(scopeInfo,parentScopeId,x);
			
			case PatternMatchLayer(parentScopeId) : foundItems = getItemsForName(scopeInfo,parentScopeId,x);
		}
	}

	// NOTE: This can be empty (for instance, when looking up a declaration of a variable that is not explicitly declared)	
	return foundItems;	
}

public set[ScopeItemId] filterNamesForNamespace(ScopeInfo scopeInfo, list[ScopeItemId] scopeItems, Namespace namespace) {
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
		}
	}
	return filteredItems;
}
//
// TODO: This should throw an exception when the type of an untyped name (e.g., a label) is requested
//
public RType getTypeForItem(ScopeInfo scopeInfo, ScopeItemId itemId) {
	switch(scopeInfo.scopeItemMap[itemId]) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionLayer(_,t,paramIds,_,_,_,_) : 
			return makeFunctionType(t,[getTypeForItem(scopeInfo, paramId) | paramId <- paramIds]);
		
		case ConstructorItem(n,tas,adtParentId,_) : return makeConstructorType(n,tas,getTypeForItem(scopeInfo,adtParentId));
		
		case ADTItem(ut,_,_,_) : return RTypeUser(ut); // TODO: Should also extract type parameters
		
		default : { 
			if (debug) println("Requesting type for item : " + prettyPrintSI(scopeInfo.scopeItemMap[itemId])); 
			return makeVoidType(); 
		}
	}
}

public bool isNameInScope(ScopeInfo scopeInfo, ScopeItemId itemId, RName x) {
	return size(getItemsForName(scopeInfo, itemId, x) > 0);
}

public RType getTypeForName(ScopeInfo scopeInfo, ScopeItemId itemId, RName x) {
	set[ScopeItemId] items = getItemsForName(scopeInfo, itemId, x);
	if (size(items) == 0) {
		// TODO: Should be an exception
		return makeVoidType();
	} else if (size(items) == 1) {
		return getTypeForItem(scopeInfo, head(items));
	} else {
		return RTypeOverloaded({ getTypeForItem(scopeInfo, scopeItemId) | scopeItemId <- items });
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
								
				default: println("No match for item");
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
				
								
				default: println("No match for item");
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
	if (debug) println("Adding variables in declaration...");
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
				AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
				scopeInfo = aip.scopeInfo;
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
				AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
				scopeInfo = aip.scopeInfo;
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
	for (vb <- vs) {
		switch(vb) {
			case `<Name n> = <Expression e>` : {
				scopeInfo = handleExpression(e, scopeInfo);
			}
		}
	}
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
// Handle abstract function declarations (i.e., function declarations without bodies)
//
// TODO: Should handle tags
//
public ScopeInfo handleAbstractFunctionNamesOnly(Tags ts, Visibility v, Signature s, loc l, ScopeInfo scopeInfo) {
	// Both cases below do essentially the same thing. First, a new FunctionLayer is created. This is not
	// complete, since it holds information about its parameters. This item is added, and the scope is switched
	// to the scope of this added function. The parameters are then added, with the scope then switched
	// back to the scope at function entry.
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), mkEmptySIList(), mkEmptyList(), false, isPublic(v), scopeInfo.currentScope)[@at=l];
			AddedItemPair aip = addScopeItemWithParent(si, scopeInfo.currentScope, l, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);			 
			scopeInfo = handleParametersNamesOnly(ps, sup.scopeInfo);
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), mkEmptySIList(), 
				[convertType(thrsi) | thrsi <- thrs], false, isPublic(v), scopeInfo.currentScope)[@at=l];
			AddedItemPair aip = addScopeItemWithParent(si, scopeInfo.currentScope, l, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);			 
			scopeInfo = handleParametersNamesOnly(ps, sup.scopeInfo);
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
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
					if (debug) println("Adding parameter <n>");
					ScopeItem pitem = (FormalParameterItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = fp@\loc]);
					AddedItemPair aip = addScopeItemWithParent(pitem, scopeInfo.currentScope, fp@\loc, scopeInfo);
					scopeInfo = aip.scopeInfo;
					siList += aip.addedId;
				}
			}
		}
	} else if (`( <Formals f> ... )` := p) {
		varArgs = true;
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if((Formal)`<Type t> <Name n>` := fp) {
					if (debug) println("Adding parameter <n>");
					ScopeItem pitem = FormalParameterItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = fp@\loc];
					AddedItemPair aip = addScopeItemWithParent(pitem, scopeInfo.currentScope, fp@\loc, scopeInfo);
					scopeInfo = aip.scopeInfo;
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
	if (debug) println("Switching to function with signature <s>");
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
	if (debug) println("Found alias: <aliasType> = <aliasedType>");
	
	ScopeItem aliasItem = AliasItem(convertUserType(aliasType), convertType(aliasedType), isPublic(v), scopeInfo.currentScope)[@at = l];
	AddedItemPair aip = addScopeItemWithParent(aliasItem, scopeInfo.currentScope, l, scopeInfo);
	return aip.scopeInfo;
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
	if (debug) println("Found Abstract ADT: <adtType>");

	ScopeItem adtItem = ADTItem(convertUserType(adtType), { }, isPublic(v), scopeInfo.currentScope)[@at = l];

	// See if this is already declared
	set[ScopeItemId] items = getItemsForName(scopeInfo,scopeInfo.currentScope,getUserTypeName(convertUserType(adtType)));
	items = filterNamesForNamespace(scopeInfo,items,TypeName());
	// TODO: Check here to see if size > 1, that would be an error and should not happen
	if (size(items) == 0) {
		AddedItemPair aip = addScopeItemWithParent(adtItem, scopeInfo.currentScope, l, scopeInfo);
		scopeInfo = aip.scopeInfo;
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
	if (debug) println("Found ADT: <adtType>");

	ScopeItem adtItem = ADTItem(convertUserType(adtType), { }, isPublic(v), scopeInfo.currentScope)[@at = l];
	ScopeItemId itemId = -1;
	
	// See if this is already declared
	set[ScopeItemId] items = getItemsForName(scopeInfo,scopeInfo.currentScope,getUserTypeName(convertUserType(adtType)));
	items = filterNamesForNamespace(scopeInfo,items,TypeName());
	// TODO: Check here to see if size > 1, that would be an error and should not happen
	if (size(items) == 0) {
		AddedItemPair aip = addScopeItemWithParent(adtItem, scopeInfo.currentScope, l, scopeInfo);
		scopeInfo = aip.scopeInfo;
		itemId = aip.addedId;
	} else if (size(items) == 1) {
		adtItem = getScopeItem(getOneFrom(items),scopeInfo);
		itemId = getOneFrom(items);
	}
	
	set[ScopeItemId] variantSet = { };
	
	for (var <- vars) {
		if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
			ScopeItem constructorItem = ConstructorItem(convertName(n), [ convertTypeArg(targ) | targ <- args ], aip.addedId, scopeInfo.currentScope)[@at = l];
			AddedItemPair aip2 = addScopeItemWithParent(constructorItem, scopeInfo.currentScope, l, scopeInfo);
			scopeInfo = aip2.scopeInfo;
			variantSet += aip2.addedId; 			
		}
	}
	
	adtItem = adtItem[variants = variantSet];
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
	if (debug) println("Inside statement <s>");
	switch(s) {
		case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
			for (v <- vs) {
				if (debug) println("Adding use for <v>");
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(v)), v@\loc);
			}
			
			if (`; <Expression e>` := b) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(sb, scopeInfo);		
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			scopeInfo = handleLabel(l,scopeInfo);			
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(b, scopeInfo); 
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			scopeInfo = handleLabel(l,scopeInfo);			
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(b, scopeInfo); 
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			scopeInfo = handleLabel(l,scopeInfo);			
			scopeInfo = handleStatement(b, scopeInfo); 
			scopeInfo = handleExpression(e, scopeInfo);			
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			scopeInfo = handleLabel(l,scopeInfo);			
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(bt, scopeInfo); 
			scopeInfo = handleStatement(bf, scopeInfo); 
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			scopeInfo = handleLabel(l,scopeInfo);			
			
			for (e <- es) {
				scopeInfo = handleExpression(e, scopeInfo);
			}
			
			scopeInfo = handleStatement(bt, scopeInfo); 
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			scopeInfo = handleLabel(l,scopeInfo);						
			scopeInfo = handleExpression(e, scopeInfo);
			for (c <- cs) {
				scopeInfo = handleCase(c, scopeInfo);
			}
		}

		case (Statement)`<Label l> <Visit v>` : {
			scopeInfo = handleLabel(l,scopeInfo);						
			scopeInfo = handleVisit(v, scopeInfo);
		}
			
		case `<Expression e> ;` : {
			scopeInfo = handleExpression(e, scopeInfo);
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			scopeInfo = handleAssignable(a, scopeInfo);
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `assert <Expression e> ;` : {
			scopeInfo = handleExpression(e, scopeInfo);
		}

		case `assert <Expression e> : <Expression em> ;` : {
			scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleExpression(em, scopeInfo);			
		}
		
		case `return <Statement b>` : {
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `throw <Statement b>` : {
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `insert <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well, ensuring it is in scope
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well, ensuring it is in scope
			scopeInfo = handleStatement(b, scopeInfo);
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			// First get back the function signature information, creating the scope item
			scopeInfo = handleFunctionNamesOnly(ts,v,sig,fb,s@\loc,scopeInfo);
					
			// Now, descend into the function, processing the body
			scopeInfo = handleFunction(ts,v,sig,fb,s@\loc,scopeInfo);
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			scopeInfo = handleLocalVarItems(t,vs,scopeInfo);
		}
		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			// TODO: Handle scoping of dynamics properly
			scopeInfo = handleLocalVarItems(t,vs,scopeInfo);
		}
		
		//case `break <Target t> ;` : {
		//	// TODO: Check target!
		//}
		
		//case `fail <Target t> ;` : {
		//	// TODO: Check target!
		//}
		
		//case `continue <Target t> ;` : {
		//	// TODO: Check target!
		//}
		
		case `try <Statement b> <Catch+ cs>` : {
			scopeInfo = handleStatement(b, scopeInfo);
			
			for (ct <- cs)  {
				scopeInfo = handleCatch(ct, scopeInfo);
			}
		}
		
		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			scopeInfo = handleStatement(b, scopeInfo);
			
			for (ct <- cs)  {
				scopeInfo = handleCatch(ct, scopeInfo);
			}

			scopeInfo = handleStatement(bf, scopeInfo);
		}
		
		case `<Label l> { <Statement+ bs> }` : {
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
		
		//case `global <Type t> <{QualifiedName ","}+ xs> ;` : {
		//	// TODO: Define how this works; this is not yet implemented, and I need to find out
		//	// what it is for.
		//	return sr;
		//}
	}
	
	return scopeInfo;
}

//
// Handle individual expressions (which could contain closures, for instance)
//
public ScopeInfo handleExpression(Expression e, ScopeInfo scopeInfo) {
	switch(e) {
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			if (debug) println("Adding use for <qn>");		
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// List
		case `[<{Expression ","}* el>]` : {
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			for (ei <- el) {
				scopeInfo = handleExpression(ei, scopeInfo);
			}
		}

		// Tuple
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			scopeInfo = handleExpression(ei, scopeInfo);
			for (eli <- el) {
				scopeInfo = handleExpression(eli, scopeInfo);
			}
		}

		// TODO: Map

		// Closure
		case `<Type t> <Parameters p> { <Statement+ ss> }` : {
			// First, create a new abstract function without a name
			
			// Now, descend into the body
			
			3;
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			// First, create a new abstract function without a name
			
			// Now, descend into the body
			
			3;
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {			
			ScopeItem blockItem = BlockLayer(scopeInfo.currentScope)[@at=e@\loc];
			AddedItemPair aip = addScopeItemWithParent(blockItem, scopeInfo.currentScope, e@\loc, scopeInfo);
			ScopeUpdatePair sup = changeCurrentScope(aip.addedId, aip.scopeInfo);

			for (s <- ss) {
				scopeInfo = handleStatement(s,scopeInfo);
			}
	
			sup = changeCurrentScope(sup.oldScopeId, scopeInfo);
			scopeInfo = sup.scopeInfo;
		}
		
		// Visit
		case (Expression) `<Label l> <Visit v>` : {
			scopeInfo = handleLabel(l,scopeInfo);						
			scopeInfo = handleVisit(v, scopeInfo);
		}
		
		// ParenExp
		case `(<Expression e1>)` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
			scopeInfo = handleExpression(e3, scopeInfo);
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// TODO: Add code to deal with fields: FieldProject
//		case `<Expression e1> < <{Field ","}+ fl> >` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression rct = RFieldProjectExp(convertExpression(e1),mapper(getSDFExpListItems(el),convertExpression));
//			return rct[@at = exp@\loc];
//		}

		// TODO: Subscript (currently broken)
//		case `<Expression e1> [ <{Expression ","}+ el> ]` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression rct = RSubscriptExp(convertExpression(e1),mapper(getSDFExpListItems(el),convertExpression));
//			return rct[@at = exp@\loc];
//		}

		// IsDefined
		case `<Expression e1> ?` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// Negation
		case `! <Expression e1>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// Negative
		case `- <Expression e1> ` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// TransitiveClosure
		case `<Expression e1> + ` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// TransitiveReflexiveClosure
		case `<Expression e1> * ` : {
			scopeInfo = handleExpression(e1, scopeInfo);
		}

		// GetAnnotation
		case `<Expression e1> @ <Name n>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Times
		case `<Expression e1> * <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Plus
		case `<Expression e1> + <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
			scopeInfo = handleExpression(e3, scopeInfo);
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			scopeInfo = handleExpression(e1, scopeInfo);
			scopeInfo = handleExpression(e2, scopeInfo);
		}	
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
	switch(a) {
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("Adding use for <qn>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `<Assignable al> [ <Expression e> ]` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);			
		}
		
		case `<Assignable al> . <Name n>` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `<Assignable al> @ <Name n>` : {
			scopeInfo = handleAssignable(al, scopeInfo);
			if (debug) println("Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `< <{Assignable ","}+ al> >` : {
			for (ali <- al) {
				scopeInfo = handleAssignable(ali, scopeInfo);
			}
		}
		
		case `<Name n> ( <{Assignable ","}+ al> )` : {
			if (debug) println("Adding use for <qn>");
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
				if (debug) println("Adding variable <n>");
				ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
				AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
				scopeInfo = aip.scopeInfo;
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = VariableItem(convertName(n), convertType(t), scopeInfo.currentScope)[@at = vb@\loc];
				AddedItemPair aip = addScopeItemWithParent(vi, scopeInfo.currentScope, vb@\loc, scopeInfo);
				scopeInfo = handleExpression(e,aip.scopeInfo);
				
				
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
			scopeInfo = handlePattern(p, scopeInfo);
			scopeInfo = handleStatement(b, scopeInfo);
		}
	}
	
	return scopeInfo;
}		

//
// Handle labels
//
public ScopeInfo handleLabel(Label l, ScopeInfo scopeInfo) {
	if ((Label)`<Name n> :` := l) {
		ScopeItem li = LabelItem(convertName(n), scopeInfo.currentScope)[@at = l@\loc];
		AddedItemPair aip = addScopeItemWithParent(li, scopeInfo.currentScope, l@\loc, scopeInfo);
		scopeInfo = aip.scopeInfo;
	}
	return scopeInfo;
}

//
// Handle visits
//
// TODO: Add needed code
//
public ScopeInfo handleVisit(Visit v, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle patterns
//
// TODO: Add needed code
//
public ScopeInfo handlePattern(Pattern p, ScopeInfo scopeInfo) {
	return scopeInfo;
}

//
// Handle Pattern with Action productions
//
public ScopeInfo handlePatternWithAction(PatternWithAction p, ScopeInfo scopeInfo) {
	switch(p) {
		case `<Pattern p> => <Expression e>` : {
			scopeInfo = handlePattern(p, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			scopeInfo = handlePattern(p, scopeInfo);
			scopeInfo = handleExpression(er, scopeInfo);
			for (e <- es) { 
				scopeInfo = handleExpression(e, scopeInfo);
			}
		}
		
		case `<Pattern p> : <Statement s>` : {
			scopeInfo = handlePattern(p, scopeInfo);
			scopeInfo = handleStatement(s, scopeInfo);
		}
	}
	
	return scopeInfo;
}

public bool hasRType(ScopeInfo scopeInfo, loc l) {
	if (l in scopeInfo.itemUses)
		return true;
	return false;
}

public RType getRType(ScopeInfo scopeInfo, loc l) {
	set[ScopeItemId] items = scopeInfo.itemUses[l];
	if (size(items) == 0) {
		// TODO: Should be an exception
		return makeVoidType();
	} else if (size(items) == 1) {
		return getTypeForItem(scopeInfo, getOneFrom(items));
	} else {
		return RTypeOverloaded([getTypeForItem(scopeInfo, sii) | sii <- items]);
	}
}

public Tree decorateNames(Tree t, ScopeInfo scopeInfo) {
	return visit(t) {
		case `<Name n>` => hasRType(scopeInfo,n@\loc) ? n[@rtype = getRType(scopeInfo,n@\loc)] : n
		
		case `<QualifiedName n>` => hasRType(scopeInfo,n@\loc) ? n[@rtype = getRType(scopeInfo,n@\loc)] : n
	}
}