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
// 3. This should be fully functional -- we currently maintain a small amount of state,
//    but it would be better to remove this.
//
// 4. Variables introduced in comprehensions and patterns should not have scope outside
//    of the related structures; for comprehensions this is outside of the comprehension,
//    for patterns this is outside of the action or surrounding code (used in an if,
//    it has scope inside the if). It may be easiest to just always introduce a new
//    scoping layer when entering one of these constructs, since that would then ensure
//    scope is cleaned up properly upon exiting.
//

// Set flag to true to issue debug messages
private bool debug = true;

//
// Items representing identifiable parts of scope, including layers (modules, functions,
// blocks) and items (functions, variables, formal parameters, labels). Note that functions 
// are both, since they are an item present in a scope and also introduce new scope.
//
data ScopeItem =
	  ModuleLayer(RName moduleName)
	| FunctionLayer(RName functionName, RType returnType, list[ScopeItem] parameters, 
					list[RType] throwsTypes, bool isVarArgs, bool isPublic, ScopeItem parent)
	| BlockLayer(ScopeItem parent)
	| VariableItem(RName variableName, RType variableType, ScopeItem parent)
	| FormalParameterItem(RName parameterName, RType parameterType, ScopeItem parent)
	| LabelItem(RName labelName, ScopeItem parent)
	| AliasItem(RUserType aliasType, RType aliasedType, bool isPublic, ScopeItem parent)
	| ConstructorItem(RName constructorName, list[RTypeArg] constructorArgs, ScopeItem adtParent, ScopeItem parent)
	| ADTItem(RUserType adtType, set[ScopeItem] variants, bool isPublic, ScopeItem parent) 
	| DummyItem() // placeholder, used when we need to provide something and fill it in later
;

alias ScopeRel = rel[ScopeItem scope, ScopeItem item];
alias ScopePair = tuple[ScopeItem scopeItem, ScopeRel scopeRel];
alias ScopeTriple = tuple[ScopeItem scopeItem, ScopeRel scopeRel, ItemUses itemUses];
alias ItemUsePair = tuple[loc useLoc, list[ScopeItem] usedItems];
alias ItemUses = set[ItemUsePair];
alias ScopeAndItems = tuple[ScopeRel scopeRel, ItemUses itemUses];
alias ScopeItemMap = map[loc itemLoc, ScopeItem item];

anno loc ScopeItem@at;

// TODO: Remove these, we don't want global state items (or, we need to clear them out later)

private ScopeItemMap scopeItemMap = ( );

private set[tuple[loc errorLoc, RType errorType]] scopeErrors = { };

// This is a hack -- this ensures the empty list is of type list[RType], not list[Void] or list[Value]
list[RType] mkEmptyList() { return tail([makeVoidType()]); }

list[ScopeItem] mkEmptySIList() { return tail([DummyItem()]); }

//
// Pretty printers for scope information
//
public str prettyPrintSI(ScopeItem si) {
	str sres = "";
	switch(si) {
		case ModuleLayer(n) : {
			return "ModuleLayer: " + prettyPrintName(n);
		}
		case FunctionLayer(n,t,ags,_,_,_,_) : {
			return "FunctionLayer: " + prettyPrintType(t) + " " + prettyPrintName(n) + "(" + joinList(ags,prettyPrintSI,",","") + ")";
		}
		case VariableItem(n,t,_) : {
			return "VariableItem: " + prettyPrintType(t) + " " + prettyPrintName(n);
		}
		
		case FormalParameterItem(n,t,_) : {
			return "FormalParameterItem: " + prettyPrintType(t) + " " + prettyPrintName(n);
		}
		
		case LabelItem(n,_) : {
			return "LabelItem: " + prettyPrintName(n);
		}

		case AliasItem(tn,ta,_,_) : {
			return "AliasItem: " + prettyPrintUserType(tn) + " = " + prettyPrintType(ta);
		}
			
		case ConstructorItem(cn,tas,_,_) : {
			return prettyPrintName(cn) + "(" + prettyPrintTAList(tas) + ")";
		}
		
		case ADTItem(ut, vs, _, _) : {
			return prettyPrintUserType(ut) + " = " + joinList(vs,prettyPrintSI," | ","");
		}
		 			
		case BlockLayer(_) : {
			return "BlockLayer";
		}
	}
}

public str prettyPrintSR(ScopeRel sr) {
	str res = "";
	for ( <sil,sir> <- sr ) {
		res += prettyPrintSI(sil) + "\n   to\n" + prettyPrintSI(sir) + "\n\n";
	}
	return res;
}

public str prettyPrintSP(ScopePair sp) {
	return "Top Item: " + prettyPrintSI(sp.scopeItem) + "\n\n" + prettyPrintSR(sp.scopeRel);
}

public void prettyPrintSPNoStr(ScopePair sp) {
	print(prettyPrintSP(sp));
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
public list[ScopeItem] getItemsForName(ScopeRel sr, ScopeItem si, RName n) {
	list[ScopeItem] foundItems = [];
	
	// First, find all the scope items at the current level of scope that match
	// the name we are looking for.
	for (sitem <- sr[si]) {
		switch(sitem) {
			case FormalParameterItem(n,_,_) : foundItems += sitem;
			
			case VariableItem(n,_,_) : foundItems += sitem;
			
			case FunctionLayer(n,_,_,_,_,_,_) : foundItems += sitem;
			
			case LabelItem(n,_) : foundItems += sitem;

			case ConstructorItem(n,_,_,_) : foundItems += sitem;			
		}
	}

	// If no names were found at this level, step back up one level to find them
	// in the parent scope. This will recurse until either the names are found
	// or the top level, Module, is reached (note there is no match for finding
	// the parent of a Module below, since it doesn't exist).
	if (size(foundItems) == 0) {
		switch(si) {
			case FormalParameterItem(_,_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case VariableItem(_,_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case FunctionLayer(_,_,_,_,_,_,_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case LabelItem(_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case ConstructorItem(_,_,_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case BlockLayer(sip) : foundItems = getItemsForName(sr,sip,n);
		}
	}
	
	if (size(foundItems) == 0) {
		println("Warning: looking up " + prettyPrintName(n) + " but could not find it!");
	}
	return foundItems;	
}

public RType getTypeForItem(ScopeItem si) {
	switch(si) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionLayer(_,t,pts,_,_,_,_) : return makeFunctionType(t,[getTypeForItem(sip) | sip <- pts]);
		
		case LabelItem(_,_) : return makeVoidType(); // TODO: Should this be an exception instead?
		
		case ConstructorItem(n,tas,sip,_) : return makeConstructorType(n,tas,getTypeForItem(sip));
		
		case ADTItem(ut,_,_,_) : return RTypeUser(ut); // TODO: Should also extract type parameters
		
		default : { if (debug) println("Requesting type for item : " + prettyPrintSI(si)); return makeVoidType(); }
	}
}

public bool isNameInScope(ScopeRel sr, ScopeItem si, RName n) {
	return size(getItemsForName(sr,si,n) > 0);
}

public RType getTypeForName(ScopeRel sr, ScopeItem si, RName n) {
	list[ScopeItem] items = getItemsForName(sr,si,n);
	if (size(items) == 0) {
		// TODO: Should be an exception
		return makeVoidType();
	} else if (size(items) == 1) {
		return getTypeForItem(head(items));
	} else {
		return RTypeOverloaded([getTypeForItem(sii) | sii <- items]);
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
public ScopeTriple buildNamespace(Tree t) {
	if ((Module) `<Header h> <Body b>` := t) {
		ScopeItem moduleItem = handleModuleHeader(h,t@\loc);
		scopeItemMap += ( t@\loc : moduleItem );
		
		ScopeAndItems sni = handleModuleBody(b,moduleItem);
		
		return < moduleItem, sni.scopeRel, sni.itemUses >;
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
public ScopeItem handleModuleHeader(Header h, loc l) {
	switch(h) {
		case `<Tags t> module <QualifiedName n> <Import* i>` : {
			return ModuleLayer(convertName(n))[@at=l];
		}		

		case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
			return ModuleLayer(convertName(n))[@at=l];
		}		
	}
}

//
// Process the individual items contained at the top level of the module.
//
public ScopeAndItems handleModuleBody(Body b, ScopeItem si) {
	scopeItemMap = ( );
	scopeErrors = { };
	
	ScopeAndItems sni = handleModuleBodyPass1(b, si, { }, { });
	sni = handleModuleBodyPass3(b, si, sni.scopeRel, sni.itemUses);
	
	return sni;
}

// FIRST PASS: Gather the names of variables and functions. These are visible throughout
// the module.
public ScopeAndItems handleModuleBodyPass1(Body b, ScopeItem currentScope, ScopeRel sr, ItemUses itu) {
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
					list[ScopeItem] lsi = handleVarItemsPass1(tgs,v,typ,vs,currentScope);
					for (vi <- lsi) sr += <currentScope,vi> ;				
				}
	
				// Abstract (i.e., without a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
					ScopeItem sfi = handleAbstractFunction(tgs,v,s,t@\loc,currentScope);
					scopeItemMap += ( t@\loc : sfi );
					sr += <currentScope, sfi> ;
					for (p <- sfi.parameters) sr += <sfi, p>;
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					ScopeItem sfi = handleFunctionNoDescent(tgs,v,s,fb,t@\loc,currentScope);
					scopeItemMap += ( t@\loc : sfi );
					sr += <currentScope, sfi> ;
					for (p <- sfi.parameters) sr += <sfi, p>;
				}
				
				// Annotation declaration
				case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					return 3;
				}
									
				// Tag declaration
				case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					return 3;
				
				}
				
				// Rule declaration
				case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
					return 3;
				
				}
				
				// Test
				case (Toplevel) `<Test tst> ;` : {
					return 3;
				
				}
								
				// ADT without variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
					ScopeAndItems sni = handleAbstractADT(tgs,v,typ,t@\loc,currentScope,sr,itu);
					sr = sni.scopeRel; itu = sni.itemUses;
				}
				
				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					ScopeAndItems sni = handleADT(tgs,v,typ,vars,t@\loc,currentScope,sr,itu);
					sr = sni.scopeRel; itu = sni.itemUses;
				}

				// Alias
				case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
					ScopeAndItems sni = handleAlias(tgs,v,typ,btyp,t@\loc,currentScope,sr,itu);
					sr = sni.scopeRel; itu = sni.itemUses;
				}
								
				// View
				case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
					return 3;
				
				}
				
								
				default: println("No match for item");
			}
		}
	}
	
	return <sr,itu>;
}

//
// Handle variable declarations, with or without initializers
//
public list[ScopeItem] handleVarItemsPass1(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeItem sip) {
	if (debug) println("Adding variables in declaration...");
	list[ScopeItem] siList = [];
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),sip)[@at = vb@\loc]);
				scopeItemMap += (vb@\loc : vi);
				siList += vi;
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),sip)[@at = vb@\loc]);
				scopeItemMap += (vb@\loc : vi);
				siList += vi;
			}
		}
	}
	return siList;
}

// Replace the scope item parent on a parameter with a new scope item.
// This is needed because the parameter is included in the function scope,
// but the function scope is the parent of the parameter, making the relationship
// recursive.
public ScopeItem addSIToParam(ScopeItem si, ScopeItem sip) {
	if (FormalParameterItem(_,_,_) := si) {
		return si[parent = sip];
	} else {
		return si;
	}
}

// Do the above replacement for all parameters in a parameter list
public ScopeItem addSIToParams(ScopeItem si, ScopeItem sip) {
	if (FunctionLayer(_,_,ps,_,_,_,_) := si) {
		return si[parameters = [addSIToParam(psi,sip) | psi <- ps]];
	} else {
		return si;
	}
}
		
//
// Handle parameter declarations
//
public tuple[list[ScopeItem] scopeItems, bool isVarArg] handleParameters(Parameters p, ScopeItem sip) {
	list[ScopeItem] siList = [];
	if (`( <Formals f> )` := p) {
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if((Formal)`<Type t> <Name n>` := fp) {
					if (debug) println("Adding parameter <n>");
					ScopeItem pitem = (FormalParameterItem(convertName(n),convertType(t),sip)[@at = fp@\loc]);
					scopeItemMap += (fp@\loc : pitem);
					siList += pitem;
				}
			}
		}
		return <siList,false>;
	} else if (`( <Formals f> ... )` := p) {
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if((Formal)`<Type t> <Name n>` := fp) {
					if (debug) println("Adding parameter <n>");
					ScopeItem pitem = (FormalParameterItem(convertName(n),convertType(t),sip)[@at = fp@\loc]);
					scopeItemMap += (fp@\loc : pitem);
					siList += pitem;
				}
			}
		}
		return <siList,true>;
	} else {
		if (debug) println("Warning: failed to match formals!"); // TODO: Should throw exception here
		return <siList,false>;
	}
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
// Handle abstract function declarations (i.e., function declarations without bodies)
//
public ScopeItem handleAbstractFunction(Tags ts, Visibility v, Signature s, loc l, ScopeItem sip) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			tuple[list[ScopeItem] scopeItems, bool isVarArg] sil = handleParameters(ps,DummyItem());
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), sil.scopeItems, mkEmptyList(), sil.isVarArg, isPublic(v), sip);
			si = addSIToParams(si,si);
			return si[@at = l];
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			tuple[list[ScopeItem] scopeItems, bool isVarArg] sil = handleParameters(ps,DummyItem());
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), sil.scopeItems, 
				[convertType(thrsi) | thrsi <- thrs], sil.isVarArg, isPublic(v), sip);
			si = addSIToParams(si,si);
			return si[@at = l];
		}
		
		default: if (debug) println("Warning, no match found!"); // Need to throw here!
	}
}

//
// Handle alias declarations
//
public ScopeAndItems handleAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (debug) println("Found alias: <aliasType> = <aliasedType>");
	
	ScopeItem aliasItem = (AliasItem(convertUserType(aliasType), convertType(aliasedType), isPublic(v), si))[@at = l];
	sr += <si, aliasItem>;
	return <sr, itu>;
}

//
// Handle abstract ADT declarations (ADT's without variants)
//
public ScopeAndItems handleAbstractADT(Tags ts, Visibility v, UserType adtType, loc l, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (debug) println("Found Abstract ADT: <adtType>");

	ScopeItem adtItem = (ADTItem(convertUserType(adtType), { }, isPublic(v), si))[@at = l];
	sr += <si, adtItem>;
	return <sr, itu>;
}

//
// Handle abstract ADT declarations (ADT's without variants)
//
public ScopeAndItems handleADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (debug) println("Found Abstract ADT: <adtType>");

	ScopeItem adtItem = (ADTItem(convertUserType(adtType), { }, isPublic(v), si))[@at = l];
	sr += <si, adtItem>;

	for (var <- vars) {
		if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
			ScopeItem constructorItem = ConstructorItem(convertName(n), [ convertTypeArg(targ) | targ <- args ], adtItem, si)[@at = l];
			adtItem.variants += { constructorItem };
			sr += <si, constructorItem>;			
		}
	}
	
	return <sr, itu>;
}


//
// Handle standard function declarations (i.e., function declarations with bodies), but
// do NOT descend into the bodies
//
public ScopeItem handleFunctionNoDescent(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeItem sip) {
	return handleAbstractFunction(ts,v,s,l,sip);		
}

// SECOND PASS: Get information from imported modules; this will be needed to resolve
// the names inside the functions or used in static initializers
// TODO: Make this actually work
public ScopeRel handleModuleBodyPass2(Body b, ScopeItem si) {
	ScopeRel scopeRel = { };
						
	return scopeRel;
}

// THIRD PASS: Identify names used inside functions or in static initializers, noting type
// information. This pass actually descends into functions, building the scope information
// within them as well.
public ScopeAndItems handleModuleBodyPass3(Body b, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : {
					ScopeAndItems sni = handleVarItems(tgs,v,typ,vs,si,sr,itu);
					sr = sni.scopeRel; itu = sni.itemUses; 
				}
	
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					ScopeItem funScope = scopeItemMap[t@\loc];
					if (debug) {
						println("Entering scope: " + prettyPrintSI(funScope));
						for (sit <- sr[funScope]) {
							println("\tIncludes Item: " + prettyPrintSI(sit));
						}
					}
					ScopeAndItems sni = handleFunction(tgs,v,s,fb,funScope,sr,itu);
					sr = sni.scopeRel; itu = sni.itemUses; 
				}
				
				// TODO: Handle other top level declarations
				
				//default: println("No match for item");
			}
		}
	}
	
	return < sr, itu >;
}

//
// Identify any names used inside variable declarations
//
public ScopeAndItems handleVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeItem sip, ScopeRel sr, ItemUses itu) {
	for (vb <- vs) {
		switch(vb) {
			case `<Name n> = <Expression e>` : {
				ScopeAndItems sni = handleExpression(e, sip, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;			
			}
		}
	}
	return <sr, itu>;
}

//
// Handle standard function declarations (i.e., function declarations with bodies)
//
public ScopeAndItems handleFunction(Tags ts, Visibility v, Signature s, FunctionBody b, ScopeItem si, ScopeRel sr, ItemUses itu) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			return handleFunctionBody(b,si,sr,itu);
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
			return handleFunctionBody(b,si,sr,itu);
		}
	}
}

//
// Handle function bodies
//
public ScopeAndItems handleFunctionBody(FunctionBody fb, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (`{ <Statement* ss> }` := fb) {
		for (s <- ss) {
			ScopeAndItems sni = handleStatement(s, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
	}
	return <sr, itu>;
}

//
// Handle individual statements
//
public ScopeAndItems handleStatement(Statement s, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (debug) println("Inside statement <s>");
	switch(s) {
		case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
			// TODO: Verify that vs cannot introduce new names into scope (i.e., is not a binder)
			for (v <- vs) {
				itu += <v@\loc,getItemsForName(sr,si,convertName(v))>;
			}
			
			if (`; <Expression e>` := b) {
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(sb, si, sr, itu); 
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?
			
			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(b, si, sr, itu); 
			sr = sni2.scopeRel; itu = sni2.itemUses;
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(b, si, sr, itu); 
			sr = sni2.scopeRel; itu = sni2.itemUses;
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleStatement(b, si, sr, itu); 
			sr = sni.scopeRel; itu = sni.itemUses;
			
			ScopeAndItems sni2 = handleExpression(e, si, sr, itu);
			sr = sni2.scopeRel; itu = sni2.itemUses;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(bt, si, sr, itu); 
			sr = sni2.scopeRel; itu = sni2.itemUses;
			
			ScopeAndItems sni3 = handleStatement(bf, si, sr, itu); 
			sr = sni3.scopeRel; itu = sni3.itemUses;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(bt, si, sr, itu); 
			sr = sni2.scopeRel; itu = sni2.itemUses;
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleExpression(e, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			for (c <- cs) {
				ScopeAndItems sni2 = handleCase(c, si, sr, itu);
				sr = sni2.scopeRel; itu = sni2.itemUses;
			}
		}

		case (Statement)`<Label l> <Visit v>` : {
			// TODO: Assuming labels are in a separate namespace from functions and
			// variables, should we check to see if we are reusing a label already
			// introduced in the same scope?

			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleVisit(v, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
			
		case `<Expression e> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			ScopeAndItems sni = handleAssignable(a, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			ScopeAndItems sni2 = handleStatement(b, si, sr, itu); 
			sr = sni2.scopeRel; itu = sni2.itemUses;
		}
		
		case `assert <Expression e> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		case `assert <Expression e> : <Expression em> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			sni = handleExpression(em, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `return <Statement b>` : {
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `throw <Statement b>` : {
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `insert <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well, ensuring it is in scope
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well, ensuring it is in scope
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		//case (Statement) `<Tags ts> <Visibility v> <Signature sig> ;` : {
			// TODO: Fix this case (nested abstract function -- not even sure that makes sense...) 
			//ScopePair sp = handleAbstractFunction(ts,v,sig,s@\loc);
			//return sp.scopeRel + { <si,sp.scopeItem> };
		//}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			// First get back the function signature information, creating the scope item
			ScopeItem sfi = handleFunctionNoDescent(ts,v,sig,fb,s@\loc,si);
			scopeItemMap += ( s@\loc : sfi );
			sr += { <si, sfi> };
					
			// Now, descend into the function, processing the body
			ScopeAndItems sni = handleFunction(ts,v,sig,fb,sfi,sr,itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			ScopeAndItems sni = handleLocalVarItems(t,vs,si,sr,itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			// TODO: Flag this dynamic somehow?
			ScopeAndItems sni = handleLocalVarItems(t,vs,si,sr,itu);
			sr = sni.scopeRel; itu = sni.itemUses;
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
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			for (ct <- cs)  {
				ScopeAndItems sni2 = handleCatch(ct, si, sr, itu);
				sr = sni2.scopeRel; itu = sni2.itemUses;
			}
		}
		
		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			for (ct <- cs)  {
				sni = handleCatch(ct, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
			
			sni = handleStatement(bf, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			list[ScopeItem] ll = handlelabel(l,si);
			sr += { <si,lli> | lli <- ll };
			
			for (b <- bs) {
				ScopeItem sib = BlockLayer(si);
				sr += <si,sib> ;
				
				ScopeAndItems sni = handleStatement(b, sib, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}
		
		//case `global <Type t> <{QualifiedName ","}+ xs> ;` : {
		//	// TODO: Define how this works; this is not yet implemented, and I need to find out
		//	// what it is for.
		//	return sr;
		//}
	}
	
	return <sr, itu>;
}

//
// Handle individual expressions (which could contain closures, for instance)
//
public ScopeAndItems handleExpression(Expression e, ScopeItem si, ScopeRel sr, ItemUses itu) {
	switch(e) {
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			itu += <qn@\loc,getItemsForName(sr, si, convertName(qn))>;
		}

		// ReifiedType
//		case `<BasicType t> ( <{Expression ","}* el> )` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RBasicType bt = convertBasicType(t);
//			RType rt = RTypeBasic(bt); rt = rt[@at = t@\loc];
//			RExpression re = RReifiedTypeExp(rt, mapper(getSDFExpListItems(el),convertExpression));
//			return re[@at = exp@\loc];			
//		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			for (ei <- el) {
				sni = handleExpression(ei, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}

		// List
		case `[<{Expression ","}* el>]` : {
			for (ei <- el) {
				ScopeAndItems sni = handleExpression(ei, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			for (ei <- el) {
				ScopeAndItems sni = handleExpression(ei, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}

		// Tuple
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			ScopeAndItems sni = handleExpression(ei, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			for (eli <- el) {
				sni = handleExpression(eli, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}

		// TODO: Map

		// TODO: Closure

		// TODO: VoidClosure

		// TODO: NonEmptyBlock

		// TODO: Visit
		
		// ParenExp
		case `(<Expression e1>)` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e3, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;			
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;
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
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// Negation
		case `! <Expression e1>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// Negative
		case `- <Expression e1> ` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// TransitiveClosure
		case `<Expression e1> + ` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// TransitiveReflexiveClosure
		case `<Expression e1> * ` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}

		// GetAnnotation
		case `<Expression e1> @ <Name n>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Times
		case `<Expression e1> * <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Plus
		case `<Expression e1> + <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
			sni = handleExpression(e3, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			ScopeAndItems sni = handleExpression(e1, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e2, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;	
		}	
	}
	
	return <sr, itu>;
}

//
// Handle individual cases
//
public ScopeAndItems handleCase(Case c, ScopeItem si, ScopeRel sr, ItemUses itu) {
	switch(c) {
		case `case <PatternWithAction p>` : {
			return handlePatternWithAction(p, si, sr, itu);
		}
		
		case `default : <Statement b>` : {
			return handleStatement(b, si, sr, itu);
		}
	}
	
	return sr;
}

//
// Handle assignables
//
public ScopeAndItems handleAssignable(Assignable a, ScopeItem si, ScopeRel sr, ItemUses itu) {
	if (debug) println("Inside assignable <a>");
	
	switch(a) {
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("Going to get items for name <qn>");
			itu += <qn@\loc,getItemsForName(sr,si,convertName(qn))>;
			if (debug) println("Got items for name <qn>");
		}
		
		case `<Assignable al> [ <Expression e> ]` : {
			ScopeAndItems sni = handleAssignable(al, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			sni = handleExpression(e, si, sr, itu);			
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `<Assignable al> . <Name n>` : {
			ScopeAndItems sni = handleAssignable(al, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;			
		}
		
		case `<Assignable al> @ <Name n>` : {
			ScopeAndItems sni = handleAssignable(al, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;			
		}
		
		case `< <{Assignable ","}+ al> >` : {
			for (ali <- al) {
				ScopeAndItems sni = handleAssignable(ali, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}
		
		case `<Name n> ( <{Assignable ","}+ al> )` : {
			itu += <n@\loc,getItemsForName(sr,si,convertName(n))>;			
			for (ali <- al) {
				ScopeAndItems sni = handleAssignable(ali, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}
	}
	
	return <sr, itu>;
}

//
// Handle local variable declarations, with or without initializers
//
public ScopeAndItems handleLocalVarItems(Type t, {Variable ","}+ vs, ScopeItem si, ScopeRel sr, ItemUses itu) {
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),si)[@at = vb@\loc]);
				scopeItemMap += (vb@\loc : vi);
				sr += <si, vi>;
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),si)[@at = vb@\loc]);
				scopeItemMap += (vb@\loc : vi);
				sr += <si, vi>;
				
				ScopeAndItems sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}
	}
	return <sr,itu>;
}

//
// Handle the "catch" part of a try/catch statement
//
public ScopeAndItems handleCatch(Catch c, ScopeItem si, ScopeRel sr, ItemUses itu) {
	
	switch(c) {
		case `catch : <Statement b>` : {
			ScopeAndItems sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `catch <Pattern p> : <Statement b>` : {
			ScopeAndItems sni = handlePattern(p, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleStatement(b, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
	}
	
	return <sr, itu>;
}		

//
// Handle labels
//
public list[ScopeItem] handleLabel(Label l) {
	if ((Label)`<Name n> :` := l) {
		return [ (LabelItem(convertName(n)))[@at = n@\loc] ];
	}
	return [];
}

//
// Handle visits
//
public ScopeAndItems handleVisit(Visit v, ScopeItem si, ScopeRel sr, ItemUses itu) {
	return <sr,itu>;
}

//
// Handle patterns
//
public ScopeAndItems handlePattern(Pattern p, ScopeItem si, ScopeRel sr, ItemUses itu) {
	return <sr,itu>;
}

//
// Handle Pattern with Action productions
//
public ScopeAndItems handlePatternWithAction(PatternWithAction p, ScopeItem si, ScopeRel sr, ItemUses itu) {
	switch(p) {
		case `<Pattern p> => <Expression e>` : {
			ScopeAndItems sni = handlePattern(p, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(e, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			ScopeAndItems sni = handlePattern(p, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleExpression(er, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			for (e <- es) { 
				sni = handleExpression(e, si, sr, itu);
				sr = sni.scopeRel; itu = sni.itemUses;
			}
		}
		
		case `<Pattern p> : <Statement s>` : {
			ScopeAndItems sni = handlePattern(p, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;
			sni = handleStatement(s, si, sr, itu);
			sr = sni.scopeRel; itu = sni.itemUses;			
		}
	}
	
	return <sr, itu>;
}

public bool hasRType(ItemUses itu, loc l) {
	return size(itu[l]) == 1;
}

public RType getRType(ItemUses itu, loc l) {
	list[ScopeItem] items = getOneFrom(itu[l]);
	if (size(items) == 0) {
		// TODO: Should be an exception
		return makeVoidType();
	} else if (size(items) == 1) {
		return getTypeForItem(head(items));
	} else {
		return RTypeOverloaded([getTypeForItem(sii) | sii <- items]);
	}
}

public Tree decorateNames(Tree t, ItemUses itu) {
	return visit(t) {
		case `<Name n>` => hasRType(itu,n@\loc) ? n[@rtype = getRType(itu,n@\loc)] : n
		
		case `<QualifiedName n>` => hasRType(itu,n@\loc) ? n[@rtype = getRType(itu,n@\loc)] : n
	}
}