module org::rascalmpl::checker::Namespace

import org::rascalmpl::checker::Types;
import org::rascalmpl::checker::ListUtils;

import List;
import Graph;
import IO;

import languages::rascal::syntax::Rascal;

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
					list[RType] throwsTypes, bool isVarArgs, ScopeItem parent)
	| BlockLayer(ScopeItem parent)
	| VariableItem(RName variableName, RType variableType, ScopeItem parent)
	| FormalParameterItem(RName parameterName, RType parameterType, ScopeItem parent)
	| LabelItem(RName labelName, ScopeItem parent)
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

private ScopeItemMap scopeItemMap = ( );

// This is a hack -- this ensures the empty list is of type list[RType], not list[Void] or list[Value]
list[RType] mkEmptyList() { return tail([makeVoidType()]); }

//
// Pretty printers for scope information
//
public str prettyPrintSI(ScopeItem si) {
	str sres = "";
	switch(si) {
		case ModuleLayer(n) : {
			return "ModuleLayer: " + prettyPrintName(n);
		}
		case FunctionLayer(n,t,ags,_,_,_) : {
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
			
			case FunctionLayer(n,_,_,_,_,_) : foundItems += sitem;
			
			case LabelItem(n,_) : foundItems += sitem;
			
			//default: { print("No match: "); println(sitem); }
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
			
			case FunctionLayer(_,_,_,_,_,_,sip) : foundItems = getItemsForName(sr,sip,n);
			
			case LabelItem(_,sip) : foundItems = getItemsForName(sr,sip,n);
		}
	}
	
	return foundItems;
}

public RType getTypeForItem(ScopeItem si) {
	switch(si) {
		case FormalParameterItem(_,t,_) : return t;
		
		case VariableItem(_,t,_) : return t;
		
		case FunctionLayer(_,t,pts,_,_,_) : return makeFunctionType(t,[getTypeForItem(sip) | sip <- pts]);
		
		case LabelItem(_,_) : return makeVoidType(); // TODO: Should this be an exception instead?
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
		return ROverloadedType([getTypeForItem(sii) | sii <- items]);
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
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };
	
	scopeRel = handleModuleBodyPass1(b,si);	
	ScopeAndItems sni = handleModuleBodyPass3(b,si,scopeRel);
	
	return <scopeRel+sni.scopeRel, sni.itemUses>;
}

// FIRST PASS: Gather the names of variables and functions. These are visible throughout
// the module.
public ScopeRel handleModuleBodyPass1(Body b, ScopeItem si) {
	ScopeRel scopeRel = { };
	
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
					list[ScopeItem] lsi = addVarItems(tgs,v,typ,vs,t@\loc,si);
					for (vi <- lsi) scopeRel += { <si,vi> } ;				
				}
	
				// Abstract (i.e., without a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
					ScopeItem sfi = handleAbstractFunction(tgs,v,s,t@\loc,si);
					scopeItemMap += ( t@\loc : sfi );
					scopeRel += { <si, sfi> };
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					ScopeItem sfi = handleFunctionNoDescent(tgs,v,s,fb,t@\loc,si);
					scopeItemMap += ( t@\loc : sfi );
					scopeRel += { <si, sfi> };
				}
				
				// TODO: Handle other top level declarations
				
				default: println("No match for item");
			}
		}
	}
	
	return scopeRel;
}

// Replace the scope item parent on a parameter with a new scope item.
// This is needed because the parameter is included in the function scope,
// but the function scope is the parent of the parameter, making the relationship
// recursive.
public ScopeItem addSIToParam(ScopeItem si, ScopeItem sip) {
	if (FormalParameterItem(n,t,_) := si) {
		return FormalParameterItem(n,t,sip);
	} else {
		return si;
	}
}

// Do the above replacement for all parameters in a parameter list
public ScopeItem addSIToParams(ScopeItem si, ScopeItem sip) {
	if (FunctionLayer(n,t,ps,ts,v,p) := si) {
		return FunctionLayer(n,t,[addSIToParam(psi,sip) | psi <- ps],ts,v,p);
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
// Handle abstract function declarations (i.e., function declarations without bodies)
//
public ScopeItem handleAbstractFunction(Tags ts, Visibility v, Signature s, loc l, ScopeItem sip) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			tuple[list[ScopeItem] scopeItems, bool isVarArg] sil = handleParameters(ps,DummyItem());
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), sil.scopeItems, mkEmptyList(), sil.isVarArg, sip);
			si = addSIToParams(si,si);
			return si[@at = l];
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : {
			if (debug) println("Found abstract function " + prettyPrintName(convertName(n)));
			tuple[list[ScopeItem] scopeItems, bool isVarArg] sil = handleParameters(ps,DummyItem());
			ScopeItem si = FunctionLayer(convertName(n), convertType(t), sil.scopeItems, 
				[convertType(thrsi) | thrsi <- thrs], sil.isVarArg, sip);
			si = addSIToParams(si,si);
			return si[@at = l];
		}
		
		default: if (debug) println("Warning, no match found!"); // Need to throw here!
	}
}

//
// Handle standard function declarations (i.e., function declarations with bodies), but
// do NOT descend into the bodies
//
public ScopeItem handleFunctionNoDescent(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeItem sip) {
	return handleAbstractFunction(ts,v,s,l,sip);		
}

//
// Handle variable declarations, with or without initializers
//
public list[ScopeItem] addVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, loc l, ScopeItem sip) {
	if (debug) println("Adding variables in declaration...");
	list[ScopeItem] siList = [];
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),sip)[@at = l]);
				scopeItemMap += (vb@\loc : vi);
				siList += vi;
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				ScopeItem vi = (VariableItem(convertName(n),convertType(t),sip)[@at = l]);
				scopeItemMap += (vb@\loc : vi);
				siList += vi;
			}
		}
	}
	return siList;
}

// SECOND PASS: Get information from imported modules; this will be needed to resolve
// the names inside the functions or used in static initializers
// TODO: Make this actually work
public ScopeRel handleModuleBodyPass2(Body b, ScopeItem si) {
	ScopeRel scopeRel = { };
						
	return scopeRel;
}

//
// Identify any names used inside variable declarations
//
public ScopeAndItems annotateVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeItem sip, ScopeRel sr) {
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };
	
	for (vb <- vs) {
		switch(vb) {
			case `<Name n> = <Expression e>` : {
				ScopeAndItems sni = handleExpression(e,sip,sr);
				scopeRel += sni.scopeRel; itemUses += sni.itemUses;			
			}
		}
	}
	return <scopeRel, itemUses>;
}

// THIRD PASS: Identify names used inside functions or in static initializers, noting type
// information. This pass actually descends into functions, building the scope information
// within them as well.
public ScopeAndItems handleModuleBodyPass3(Body b, ScopeItem si, ScopeRel sr) {
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };
	
	if (`<Toplevel* ts>` := b) {
		for (Toplevel t <- ts) {
			switch(t) {
				// Variable declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : {
					ScopeAndItems sni = annotateVarItems(tgs,v,typ,vs,si,sr);
					scopeRel += sni.scopeRel;
					itemUses += sni.itemUses; 
				}
	
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					ScopeItem funScope = scopeItemMap[t@\loc];
					ScopeAndItems sni = handleFunction(tgs,v,s,fb,funScope,sr);
					scopeRel += sni.scopeRel;
					itemUses += sni.itemUses; 
				}
				
				// TODO: Handle other top level declarations
				
				//default: println("No match for item");
			}
		}
	}
	
	return < scopeRel, itemUses >;
}

//
// Handle standard function declarations (i.e., function declarations with bodies)
//
public ScopeAndItems handleFunction(Tags ts, Visibility v, Signature s, FunctionBody b, ScopeItem si, ScopeRel sr) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			return handleFunctionBody(b,si,sr);
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
			return handleFunctionBody(b,si,sr);
		}
	}
}

//
// Handle function bodies
//
public ScopeAndItems  handleFunctionBody(FunctionBody fb, ScopeItem si, ScopeRel sr) {
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };

	if (`{ <Statement* ss> }` := fb) {
		for (s <- ss) {
			ScopeAndItems sni = handleStatement(s, si, sr);
			scopeRel += sni.scopeRel; itemUses += sni.itemUses;
		}
	}
	return <scopeRel, itemUses>;
}

//
// Handle individual statements
//
public ScopeRel handleStatement(Statement s, ScopeItem si, ScopeRel sr) {
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };
	
	switch(s) {
		case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
			// TODO: Verify that vs cannot introduce new names into scope (i.e., is not a binder)
			// TODO: Link names in scope to vs
			
			if (`; <Expression e>` := b) {
				ScopeAndItems sni = handleExpression(e, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(sb, si, sr); 
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(b, si, sr); 
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(b, si, sr); 
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleStatement(b, si, sr); 
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
			
			ScopeAndItems sni2 = handleExpression(e, si, sr);
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(bt, si, sr); 
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
			
			ScopeAndItems sni3 = handleStatement(bf, si, sr); 
			scopeRel += sni3.scopeRel;
			itemUses += sni3.itemUses;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			for (e <- es) {
				ScopeAndItems sni = handleExpression(e, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			ScopeAndItems sni2 = handleStatement(bt, si, sr); 
			scopeRel += sni2.scopeRel;
			itemUses += sni2.itemUses;
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleExpression(e, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
			
			for (c <- cs) {
				ScopeAndItems sni2 = handleCase(c, si, sr);
				scopeRel += sni2.scopeRel;
				itemUses += sni2.itemUses;
			}
		}

		case (Statement)`<Label l> <Visit v>` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			ScopeAndItems sni = handleVisit(v, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
			
		case `<Expression e> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			// TODO: Handle this case!
			sr = handleAssignable(a, si, sr);
			return sr + handleStatement(b, si, sr);	
		}
		
		case `assert <Expression e> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}

		case `assert <Expression e> : <Expression em> ;` : {
			ScopeAndItems sni = handleExpression(e, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
			
			sni = handleExpression(em, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case `return <Statement b>` : {
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case `throw <Statement b>` : {
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case `insert <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			// TODO: Should check target here as well
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> ;` : {
			// TODO: Fix this case (nested abstract function -- not even sure that makes sense...) 
			ScopePair sp = handleAbstractFunction(ts,v,sig,s@\loc);
			return sp.scopeRel + { <si,sp.scopeItem> };
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			// TODO: Fix this case (nested function)
			ScopePair sp = handleFunction(ts,v,sig,fb,s@\loc);
			return sp.scopeRel + { <si, sp.scopeItem> }; 
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			// TODO: Fix this case (local variable declarations)
			list[ScopeItem] siList = handleLocalVarItems(t,vs,s@\loc);	
			return { <si,sii> | sii <- siList };
		}
		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			// TODO: Fix this case (local variable declarations)
			list[ScopeItem] siList = handleLocalVarItems(t,vs,s@\loc);	
			return { <si,sii> | sii <- siList };		
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
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
			
			for (ct <- cs)  {
				ScopeAndItems sni2 = handleCatch(ct,si,sr,s@\loc);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
		}
		
		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			ScopeAndItems sni = handleStatement(b, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
			
			for (ct <- cs)  {
				ScopeAndItems sni2 = handleCatch(ct,si,sr,s@\loc);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
			
			sni = handleStatement(bf, si, sr);
			scopeRel += sni.scopeRel;
			itemUses += sni.itemUses;
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			list[ScopeItem] ll = handlelabel(l,si);
			scopeRel += { <si,lli> | lli <- ll };
			
			for (b <- bs) {
				ScopeAndItems sni = handleStatement(b, si, sr);
				scopeRel += sni.scopeRel;
				itemUses += sni.itemUses;
			}
		}
		
		//case `global <Type t> <{QualifiedName ","}+ xs> ;` : {
		//	// TODO: Define how this works; this is not yet implemented, and I need to find out
		//	// what it is for.
		//	return sr;
		//}
	}
	
	return <scopeRel, itemUses>;
}

//
// Handle individual expressions (which could contain closures, for instance)
//
public ScopeAndItems handleExpression(Expression e, ScopeItem sip, ScopeRel srp) {
	ScopeRel scopeRel = { };
	ItemUses itemUses = { };
	
	switch(e) {
		case (Expression)`<BooleanLiteral bl>` : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<DecimalIntegerLiteral il>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<OctalIntegerLiteral il>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<HexIntegerLiteral il>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<RealLiteral rl>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<StringLiteral sl>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<LocationLiteral ll>`  : {
			return <scopeRel, itemUses>;
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			return <scopeRel, itemUses>;
		}

		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			return <sr, {<qn@\loc,getItemsForName(srp,sip,convertName(qn))>}>;
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
//		case `<Expression e1> ( <{Expression ","}* el> )` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression rct = RCallOrTreeExp(convertExpression(e1),mapper(getSDFExpListItems(el),convertExpression));
//			return rct[@at = exp@\loc];
//		}

		// List
		case `[<{Expression ","}* el>]` : {
			for (<srn,iun> <- { handleExpression(eli,sip,srp) | eli <- el }) {
				sr += srn; iu += iun;
			}
			return <scopeRel, itemUses>;
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			for (<srn,iun> <- { handleExpression(eli,sip,srp) | eli <- el }) {
				sr += srn; iu += iun;
			}
			return <scopeRel, itemUses>;
		}

		// Tuple
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			for (<srn,iun> <- ({ handleExpression(ei,sip,srp) } + { handleExpression(eli,sip,srp) | eli <- el })) {
				sr += srn; iu += iun;
			}
			return <scopeRel, itemUses>;
		}

		// TODO: Map

		// TODO: Closure

		// TODO: VoidClosure

		// TODO: NonEmptyBlock

		// TODO: Visit
		
		// ParenExp
		case `(<Expression e1>)` : {
			return handleExpression(e1,sip,srp);
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)} )) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)} + {handleExpression(e3,sip,srp)} )) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// ReifyType
		case (Expression)`#<Type t>` : {
			return <sr,iu>;
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)} )) {
				sr += srn; iu += iun;
			}
			iu += <n@\loc,getItemsForName(srp,sip,convertName(n))>;
			return <sr,iu>;		
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)})) {
				sr += srn; iu += iun;
			}
			iu += <n@\loc,getItemsForName(srp,sip,convertName(n))>;
			return <sr,iu>;		
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
			return handleExpression(e1,sip,srp);
		}

		// Negation
		case `! <Expression e1>` : {
			return handleExpression(e1,sip,srp);
		}

		// Negative
		case `- <Expression e1> ` : {
			return handleExpression(e1,sip,srp);
		}

		// TransitiveClosure
		case `<Expression e1> + ` : {
			return handleExpression(e1,sip,srp);
		}

		// TransitiveReflexiveClosure
		case `<Expression e1> * ` : {
			return handleExpression(e1,sip,srp);
		}

		// GetAnnotation
		case `<Expression e1> @ <Name n>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)})) {
				sr += srn; iu += iun;
			}
			iu += <n@\loc,getItemsForName(srp,sip,convertName(n))>;
			return <sr,iu>;		
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			iu += <n@\loc,getItemsForName(srp,sip,convertName(n))>;
			return <sr,iu>;		
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Times
		case `<Expression e1> * <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Plus
		case `<Expression e1> + <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)} + {handleExpression(e3,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			for (<srn,iun> <- ( {handleExpression(e1,sip,srp)} + {handleExpression(e2,sip,srp)})) {
				sr += srn; iu += iun;
			}
			return <sr,iu>;		
		}	
	}
	
	return <sr,ui>;
}

//
// Handle individual cases
//
public ScopeRel handleCase(Case c, ScopeItem si, ScopeRel sr) {
	switch(c) {
		case `case <PatternWithAction p>` : {
			return handlePatternWithAction(p,si, sr);
		}
		
		case `default : <Statement b>` : {
			return handleStatement(b,si, sr);
		}
	}
	
	return sr;
}

//
// Handle assignables
//
public ScopeRel handleAssignable(Assignable a, ScopeItem si, ScopeRel sr) {
	if (debug) println("Inside assignable <a>");
	switch(a) {
		case (Assignable)`<QualifiedName qn>` : {
			return sr;	
		}
		
		case `<Assignable al> [ <Expression e> ]` : {
			return handleAssignable(al,si, sr) + handleExpression(e,si, sr);			
		}
		
		case `<Assignable al> . <Name n>` : {
			return handleAssignable(al);			
		}
		
		case `<Assignable al> @ <Name n>` : {
			return handleAssignable(al);			
		}
		
		case `< <{Assignable ","}+ al> >` : {
			return { handleAssignable(ala,si, sr) | ala <- al };			
		}
		
		case `<Name n> ( <{Assignable ","}+ al> )` : {
			return { handleAssignable(ala,si, sr) | ala <- al };			
		}
	}
	
	return sr;
}

//
// Handle local variable declarations, with or without initializers
//
public list[ScopeItem] handleLocalVarItems(Type t, {Variable ","}+ vs, loc l, ScopeItem si, ScopeRel sr) {
	list[ScopeItem] siList = [];
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				siList += (VariableItem(convertName(n),convertType(t))[@at = l]);
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				siList += (VariableItem(convertName(n),convertType(t))[@at = l]);
			}
		}
	}
	return siList;
}

//
// Handle the "catch" part of a try/catch statement
//
public ScopeRel handleCatch(Catch c, ScopeItem si, ScopeRel sr, loc l) {
	
	switch(c) {
		case `catch : <Statement b>` : {
			return handleStatement(b, si, sr);		
		}
		
		case `catch <Pattern p> : <Statement b>` : {
			sr += handlePattern(p,si, sr);
			return sr + handleStatement(b,si, sr);		
		}
	}
	
	return sr;
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
public ScopeRel handleVisit(Visit v, ScopeItem si, ScopeRel sr) {
	ScopeRel sr = { };
	
	return sr;
}

//
// Handle patterns
//
public ScopeRel handlePattern(Pattern p, ScopeItem si, ScopeRel sr) {
	ScopeRel sr = { };
	
	return sr;
}

//
// Handle Pattern with Action productions
//
public ScopeRel handlePatternWithAction(PatternWithAction p, ScopeItem si, ScopeRel sr) {
	ScopeRel sr = { };
	
	switch(p) {
		case `<Pattern p> => <Expression e>` : {
			sr += handlePattern(p,si, sr);
			return sr + handleExpression(e,si, sr);
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			sr += handlePattern(p,si, sr);
			sr += handleExpression(er,si, sr);
			for (e <- es) sr += handleExpression(e,si, sr);
			return sr;
		}
		
		case `<Pattern p> : <Statement s>` : {
			sr += handlePattern(p,si, sr);
			return sr + handleStatement(s,si, sr);	
		}
	}
	
	return sr;
}