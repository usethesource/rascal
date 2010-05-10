module rascal::checker::Namespace

import rascal::checker::Types;
import rascal::checker::ListUtils;
import rascal::checker::SubTypes;
import rascal::checker::Signature;
import rascal::checker::ScopeInfo;

import List;
import Graph;
import IO;
import Set;
import Map;

// NOTE: The code in ExampleGraph appears to be out of date, so this doesn't
// appear to work. Look at other ways to visualize.
//import viz::Figure::Core;
//import viz::Figure::Render;

import rascal::\old-syntax::Rascal;

// 
// TODOs
//
// 1. Tags can include expressions, which thus need to be typechecked. Add checking for
//    tags. UPDATE: This is actually wrong, tags don't allow expressions. However, they
//    do introduce a new namespace, so we need to store them and update the scope information.
//    For now they are not enabled in Rascal so just ignore them.
//
// 2. DONE: For each module we should create a module "signature" that includes all externally
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
// 6. DEFER: Would it be good to wrap each scope item with functions for accessors? This would
//    shield it a bit from changes to the arity of the item. Or it could be useful to
//    use the . field accessors and as much as possible eliminate the use of pattern
//    matching over these structures. UPDATE: This may be nice, but defer for now, things are
//    working as they are.
//
// 7. DONE: Would it be good to split the FunctionLayer into a layer and an item? It may be
//    nice to come up with a more general way to think about scoping, made up of layers,
//    namespaces, visibilities, and items, that we could apply to any language, including
//    Rascal.
//
// 8. DONE: Add checks to ensure non-overlaps on overloaded functions and constructors
// 8a. EXCEPT: We would still have an issue with f(int...) and f(list[int]), so we need
//    to add handling specifically for this case. See #15 below.
//
// 9. DONE: Add checks to ensure names are not repeated at the same scoping level (important:
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
// 12. DONE: Need to enforce name availability on all paths from a boolean expression. For
//       instance, in something like (a := p1 && b := p2) || (a := p1 && c := p3), only
//       a should be visible (i.e., in scope) outside of the || expression, since b and c
//       are only bound along one of the paths UPDATE: Done for boolean OR expressions, see
//       what other expressions need this as well. UPDATE: Also added for implication and
//		 equivalence.
//
// 13. DONE: Add support for the Reducer expression
//
// 14. Add alias expansion (since we have all the name information here) -- maybe add
//       as an Alias type, so we can keep track of the original type name used as well,
//       which would make more sense to the user than the expanded form.
//
// 15. Special case, one function ends with list[T], another with T...
//
// 16. DONE: As a follow-up from above, need to introduce the ... var into scope, so it can be
//       checked inside the checker. UPDATE: This is just the last var in the parameter list,
//	   and the handling is already added to properly return the type for the parameter as a
//	   varargs type. The checker needs to handle this properly to equate it with a list of the
//     underlying type.
//
// 17. Should resolve aliases during module imports, since we could have different aliases in
//     different imported modules, but with the same name. We don't want to inadvertently change
//     the type of an imported item.
//
// 18. DONE: Properly handle lookups of qualified names, these are in modules and are resolved from the
//     top level.
//
// 19. Add support for tags.
//
// 20. DONE: Add support for rules.
//
// 21. Add support for tests.
//
// 22. Add checking to ensure that insert, append, fail, break, and continue are all used in the
//     correct contexts.
//

// Set flag to true to issue debug messages
private bool debug = true;

// This is a hack -- this ensures the empty list is of type list[RType], not list[Void] or list[Value]
list[RType] mkEmptyList() { return tail([makeVoidType()]); }

// Same hack -- this ensures the empty list is of type list[ScopeItemId], not list[Void] or list[Value]
list[ScopeItemId] mkEmptySIList() { return tail([3]); }

public list[Import] getImports(Tree t) {
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case `<Tags t> module <QualifiedName n> <Import* i>` : return [il | il <- i];
			case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : return [il | il <- i];
			default : throw "Unexpected module format";
		}
	}
}

public RName getModuleName(Tree t) {
	if ((Module) `<Tags t> module <QualifiedName n> <Import* i> <Body b>` := t) {
		return convertName(n);
	} else if ((Module) `<Tags t> module <QualifiedName n> <Import* i> <Body b>` := t) {
		return convertName(n);
	}
	throw "getModuleName, unexpected module syntax, cannot find module name";
}

alias SignatureMap = map[Import importedModule, RSignature moduleSignature];

//
// Given a tree representing a module, build the namespace. Note that
// we only process one module at a time, although this code can
// trigger the processing of other modules that are imported.
//
public ScopeInfo buildNamespace(Tree t, SignatureMap signatures) {
	ScopeInfo scopeInfo = createNewScopeInfo();
	scopeInfo = justScopeInfo(pushNewTopScope(t@\loc, scopeInfo));

	// Add the module present in this tree. This is nested under TopLayer, so we can, in
	// theory, load multiple modules into the same scope information structure.
	if ((Module) `<Header h> <Body b>` := t) {
		switch(h) {
			case `<Tags t> module <QualifiedName n> <Import* i>` : {
				scopeInfo = handleModuleImports(i, signatures, scopeInfo);
				scopeInfo = justScopeInfo(pushNewModuleScope(convertName(n), t@\loc, scopeInfo));
				scopeInfo = handleModuleBodyFull(b, handleModuleBodyNamesOnly(b, scopeInfo));
				scopeInfo = popScope(scopeInfo);
			}

			case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
				scopeInfo = handleModuleImports(i, signatures, scopeInfo);
				scopeInfo = justScopeInfo(pushNewModuleScope(convertName(n), t@\loc, scopeInfo));
				scopeInfo = handleModuleBodyFull(b, handleModuleBodyNamesOnly(b, scopeInfo));
				scopeInfo = popScope(scopeInfo);
			}

			default : throw "buildNamespace: unexpected syntax for module";
		}
	} else {
        throw "buildNamespace: missed a case for <t.prod>";
	}

	return scopeInfo;
}		

// Load information from the imported modules. Note that the import list is reversed before processing;
// this is because the last module loaded "wins" in conflicts, but it's easier to model this by starting
// with the last first then handling duplicate definitions as they arise.
public ScopeInfo handleModuleImports(Import* il, SignatureMap signatures, ScopeInfo scopeInfo) {
	list[Import] impList = [imp | imp <- il]; 
	impList = reverse(impList);
	for (imp <- impList) {
		switch(imp) {
			case `import <ImportedModule im> ;` : {
				if (debug) println("NAMESPACE: Processing module import <imp>");
				if (imp in signatures)
					scopeInfo = handleImportedModule(im, signatures[imp], imp@\loc, scopeInfo);
				else
					throw "No signature found for imported module <imp>";
			}
			case `extend <ImportedModule im> ;` : {
				if (debug) println("NAMESPACE: Processing module import <imp>");
				if (imp in signatures)
					scopeInfo = handleImportedModule(im, signatures[imp], imp@\loc, scopeInfo);
				else
					throw "No signature found for imported module <imp>";
			}
		}
	}
	return scopeInfo;
}

//
// TODO: Need to handle actuals, renamings -- for now, just handle the basic import scenario
//
public ScopeInfo handleImportedModule(ImportedModule im, RSignature signature, loc l, ScopeInfo scopeInfo) {
	switch(im) {
		case `<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` : {
			if (debug) println("NAMESPACE: Processing import of module <qn>"); 
			scopeInfo = addImportsToScope(qn, signature, l, scopeInfo);
		}
		case `<QualifiedName qn> <ModuleActuals ma>` : {
			if (debug) println("NAMESPACE: Processing import of module <qn>"); 
			scopeInfo = addImportsToScope(qn, signature, l, scopeInfo);
		}
		case `<QualifiedName qn> <Renamings rn>` : {
			if (debug) println("NAMESPACE: Processing import of module <qn>"); 
			scopeInfo = addImportsToScope(qn, signature, l, scopeInfo);
		}
		case (ImportedModule)`<QualifiedName qn>` : {
			if (debug) println("NAMESPACE: Processing import of module <qn>"); 
			scopeInfo = addImportsToScope(qn, signature, l, scopeInfo);
		}
		default : {
			if (debug) println("NAMESPACE: Missed a case for module import <im>"); println(im);
			throw "Error in handleImportedModule, case not handled: <im>";
		}
	}
	return scopeInfo;
}

//
// Load the imported signatures into scope. This function assumes that we are currently at the
// top level scope -- i.e., that we are not inside a module scope. A new module scope is added
// for the imported module to allow both a top-level version of the name and a module-specific
// version of the name, useful for resolving qualified names.
//
public ScopeInfo addImportsToScope(QualifiedName qn, RSignature signature, loc l, ScopeInfo scopeInfo) {
	if (debug) println("Adding scope item for module <prettyPrintName(convertName(qn))>");
	scopeInfo = justScopeInfo(pushNewModuleScope(convertName(qn), l, scopeInfo));
	
	// Load in ADTs first, just in case they are used in the signatures of functions, constructors,
	// aliases, etc. There is no checking for duplicates at this point; any duplicates will be merged
	// later, so we can still maintain information about the various locations of definitions if needed.
	for (ADTSigItem(an,st,at) <- signature.signatureItems) {
		if (debug) println("NAMESPACE: Adding module level scope item for ADT signature item <prettyPrintType(an)>");
		scopeInfo = justScopeInfo(addADTToScope(an, true, at, scopeInfo));
		if (debug) println("NAMESPACE: Adding top level scope item for ADT signature item <prettyPrintType(an)>");
		scopeInfo = justScopeInfo(addADTToTopScope(an, true, at, scopeInfo));
	}

	// Second, load in aliases. These may use the ADT definitions, and they may be used in definitions
	// for variables, constructors, etc. Check for duplicates, since we do not allow distinct aliases
	// to be loaded more than once (two declarations of the form alias A = int are fine). We assume
	// the signature itself is fine, though -- any errors with duplicates within a module are not
	// handled here, but should instead be caught when type checking the module being loaded. So, we
	// only do a top-level check (which would still happen to catch these), not an in-module-level check.
	for (AliasSigItem(an,st,at) <- signature.signatureItems) {
		if (debug) println("NAMESPACE: Adding module level scope item for Alias signature item <prettyPrintType(an)>");
		scopeInfo = justScopeInfo(addAliasToScope(an, st, true, at, scopeInfo));
		if (debug) println("NAMESPACE: Adding top level scope item for Alias signature item <prettyPrintType(an)>");
		scopeInfo = justScopeInfo(checkForDuplicateAliases(addAliasToTopScope(an, st, true, at, scopeInfo), at));
	}
	
	// Third, load up the other items in the signature. For named items, such as functions, we assume the
	// signature is fine, but we will have to check to see if the names are duplicates of already-defined
	// items when we load them into the top level. If we find an overlap, we ignore it -- we don't
	// register an error, we just don't add the item.
	//
	// TODO: This is potentially confusing. For instance, say we import modules M1 and M2. M1 has f(str)
	// and f(int), while M2 has f(int) and f(bool). So, there is an overlap between f(int) in M1 and M2.
	// Under the current policy, where the last wins, this means we get M1.f(str), M2.f(int), and
	// M2.f(bool). This could be confusing, though, since the user may not realize there is a clash, and
	// may expect that he is calling M1.f(int) instead. Worse, M1.f(str) may call m1.f(int), so different
	// public functions would be called depending on the caller. We should at least issue a warning here,
	// but we may want to do something more rigorous.
	for (item <- signature.signatureItems) {
		switch(item) {
			// Add a function scope layer and associated item for a function in the imported signature. Note that
			// we pop the scope after each add, since we don't want to stay inside the function scope.
			case FunctionSigItem(fn,st,at) : {
				if (debug) println("NAMESPACE: Adding module level scope item for Function signature item <prettyPrintName(fn)>");
				scopeInfo = justScopeInfo(pushNewFunctionScope(fn, getFunctionReturnType(st), [ <RSimpleName(""),t,at,at> | t <- getFunctionArgumentTypes(st) ], [ ], true, at, scopeInfo));
				scopeInfo = popScope(scopeInfo);

				// This is where we check for overlap, since (like above) we assume the loaded module 
				// has already been type checked, so we don't look for overload conflicts within the import.
				// If we find an overlap, just don't import the function into the top level scope; it is
				// still available using a fully qualified name.
				if (!willFunctionOverlap(fn,st,scopeInfo,scopeInfo.topScopeItemId)) {
					if (debug) println("NAMESPACE: Adding top level scope item for Function signature item <prettyPrintName(fn)>");
					scopeInfo = justScopeInfo(pushNewFunctionScopeAtTop(fn, getFunctionReturnType(st), [ <RSimpleName(""),t,at,at> | t <- getFunctionArgumentTypes(st) ], [ ], true, at, scopeInfo));
					scopeInfo = popScope(scopeInfo);
				} else {
					if (debug) println("NAMESPACE: Function <prettyPrintName(fn)> would overlap, not adding to top scope");
				}
			}

			// Add a variable item to the top and module-level scopes. If the name already appears in the
			// top level scope, we do not add it. 
			case VariableSigItem(vn,st,at) : {
				if (debug) println("NAMESPACE: Adding module level scope item for Variable signature item <prettyPrintName(vn)>");
				scopeInfo = justScopeInfo(addVariableToScope(vn, st, true, at, scopeInfo));

				if (! (size(getItemsForName(scopeInfo, scopeInfo.topScopeItemId, vn)) > 0)) {
					if (debug) println("NAMESPACE: Adding top level scope item for Variable signature item <prettyPrintName(vn)>"); 
					scopeInfo = justScopeInfo(addVariableToTopScope(vn, st, true, at, scopeInfo));
				} 
			}

			// Add a constructor to the top and module-level scopes. We look up the ADT in the same
			// scope so we can tie the constructor to the appropriate ADT. In cases where the ADT appears
			// more than once, we just use an arbitrary item ID, since we will later consolidate them.
			case ConstructorSigItem(cn,RConstructorType(_,st,adttyp),at) : {
				RName adtName = getADTName(adttyp);

				set[ScopeItemId] possibleADTs = getTypeItemsForNameMB(scopeInfo, scopeInfo.currentScope, adtName);
				possibleADTs = { t | t <- possibleADTs, ADTItem(_,_,_,_) := scopeInfo.scopeItemMap[t] };
				if (size(possibleADTs) == 0) throw "Error: Cannot find ADT <prettyPrintName(adtName)> to associate with constructor: <item>";
				ScopeItemId adtItemId = getOneFrom(possibleADTs);
				if (debug) println("NAMESPACE: Adding module level scope item for Constructor signature item <prettyPrintName(cn)>");
				scopeInfo = justScopeInfo(addConstructorToScope(cn, st, adtItemId, true, at, scopeInfo));

				possibleADTs = getTypeItemsForName(scopeInfo, scopeInfo.topScopeItemId, adtName);
				possibleADTs = { t | t <- possibleADTs, ADTItem(_,_,_,_) := scopeInfo.scopeItemMap[t] };
				if (size(possibleADTs) == 0) throw "Error: Cannot find ADT <prettyPrintName(adtName)> to associate with constructor: <item>";
				adtItemId = getOneFrom(possibleADTs);
				// Check for overlap here; if we find an overlap, this will trigger an error, since we should not have
				// overlapping constructors and, unlike functions, we can't just take a "last in wins" approach.
				if (debug) println("NAMESPACE: Trying to add top level scope item for Constructor signature item <prettyPrintName(cn)>");
				scopeInfo = justScopeInfo(checkConstructorOverlap(addConstructorToTopScope(cn, st, adtItemId, true, at, scopeInfo),at));
			}

			// Add an annotation item to the top and module-level scopes. If an annotation of the same name 
			// already appears in the top level scope, we consider this to be an error. This is handled using
			// checkForDuplicateAnnotations. 
			case AnnotationSigItem(an,st,ot,at) : {
				scopeInfo = justScopeInfo(addAnnotationToScope(an, st, ot, true, at, scopeInfo)); 
				
				if (! size(getAnnotationItemsForName(scopeInfo, scopeInfo.topScopeItemId, an)) > 0) { 
					scopeInfo = justScopeInfo(checkForDuplicateAnnotations(addAnnotationToTopScope(an, st, ot, true, at, scopeInfo),at));
				} 
			}

			// NOTE: We do not import rule signature items; they are used by the interpreter, but do not
			// constitute part of the module signature that we must be aware of for type checking.
			// RuleSigItem(RName ruleName, loc at)

			// TODO
			// TagSigItem(RName tagName, list[RType] tagTypes, loc at)
			// case TagSigItem(tn,tt,at) : 3;
		}
	}

	scopeInfo = popScope(scopeInfo);
	return scopeInfo;
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
					scopeInfo = handleAnnotationDeclarationNamesOnly(tgs,v,typ,otyp,n,t@\loc,scopeInfo);
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
								
				default: throw "handleModuleBodyNamesOnly: No match for item <t>";
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
					scopeInfo = handleAbstractFunction(tgs, v, s, t@\loc, scopeInfo);
				}
	 
	 			// Concrete (i.e., with a body) function declaration
				case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
					scopeInfo = handleFunction(tgs, v, s, fb, t@\loc, scopeInfo);
				}
				
				// Annotation declaration
				case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
					scopeInfo = handleAnnotationDeclaration(tgs, v, typ, otyp, n, t@\loc, scopeInfo);
				}
									
				// Tag declaration
				case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
					scopeInfo = handleTagDeclaration(tgs, v, k, n, typs, t@\loc, scopeInfo);
				}
				
				// Rule declaration
				case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
					scopeInfo = handleRuleDeclaration(tgs, n, pwa, t@\loc, scopeInfo);
				}
				
				// Test
				case (Toplevel) `<Test tst> ;` : {
					scopeInfo = handleTest(tst, t@\loc, scopeInfo);
				}
								
				// ADT without variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
					scopeInfo = handleAbstractADT(tgs, v, typ, t@\loc, scopeInfo);
				}
				
				// ADT with variants
				case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
					scopeInfo = handleADT(tgs, v, typ, vars, t@\loc, scopeInfo);
				}

				// Alias
				case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
					scopeInfo = handleAlias(tgs, v, typ, btyp, t@\loc, scopeInfo);
				}
								
				// View
				case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
					scopeInfo = handleView(tgs, v, n, sn, alts, t@\loc, scopeInfo);
				}
				
								
				default: throw "handleModuleBodyFull: No match for item <t>";
			}
		}
	}
	
	return scopeInfo;
}

//
// Handle variable declarations, with or without initializers
//
public ScopeInfo handleVarItemsNamesOnly(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeInfo scopeInfo) {
	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
	if (debug) println("NAMESPACE: Adding variables in declaration...");
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameMB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Duplicate declaration of name <n>");
				} 
				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), isPublic(v), vb@\loc, scopeInfo),[<true,n@\loc>])); 
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameMB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Duplicate declaration of name <n>"); 
				}
				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), isPublic(v), vb@\loc, scopeInfo),[<true,n@\loc>])); 
			}
		}
	}
	return scopeInfo;
}

//
// Identify any names used inside variable declarations
//
public ScopeInfo handleVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, ScopeInfo scopeInfo) {
	scopeInfo = handleTags(ts, scopeInfo);
	for (`<Name n> = <Expression e>` <- vs) scopeInfo = handleExpression(e, scopeInfo);
	return scopeInfo;
}

//
// Handle standard function declarations (i.e., function declarations with bodies), but
// do NOT descend into the bodies
//
public ScopeInfo handleFunctionNamesOnly(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeInfo scopeInfo) {
	return handleAbstractFunctionNamesOnly(ts,v,s,l,scopeInfo);		
}

//
// Handle abstract function declarations (i.e., function declarations without bodies)
//
public ScopeInfo handleAbstractFunctionNamesOnly(Tags ts, Visibility v, Signature s, loc l, ScopeInfo scopeInfo) {
	// Add the new function into the scope and process any parameters.
	ScopeInfo addFunction(Name n, RType retType, Parameters ps, list[RType] thrsTypes, bool isPublic, ScopeInfo scopeInfo) {
		// Get back a list of tuples representing the parameters; these will actually be added into the scope
		// in the next step
		list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params = handleParametersNamesOnly(ps, scopeInfo);

		// Add a new function scope, getting back the updated scope and a list of added scope IDs
		ResultTuple rt = pushNewFunctionScope(convertName(n), retType, params, thrsTypes, isPublic, l, scopeInfo);

		// Add uses and get back the final scope info, checking for overlaps
		scopeInfo = justScopeInfo(checkFunctionOverlap(addScopeItemUses(rt,([<false,l>, <true,n@\loc>] + [<true,p.nloc> | tuple[RName pname, RType ptype, loc ploc, loc nloc] p <- params])),n@\loc));

		// Pop the new scope and exit
		return popScope(scopeInfo);
	}

	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
	
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
// Just process the tags; this function has no body, and the function header was processed already.
//
public ScopeInfo handleAbstractFunction(Tags ts, Visibility v, Signature s, loc l, ScopeInfo scopeInfo) {
	return handleTags(ts, scopeInfo);
}

//
// Handle parameter declarations. Parameters currently have no defaults, etc, so there is no other
// version of this function.
//
public list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] handleParametersNamesOnly(Parameters p, ScopeInfo scopeInfo) {
	list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params = [];

	// Add each parameter into the scope; the current scope is the function that
	// is being processed.	
	if (`( <Formals f> )` := p) {
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if ((Formal)`<Type t> <Name n>` := fp) params += < convertName(n), convertType(t), fp@\loc, n@\loc >; 					
			}
		}
	} else if (`( <Formals f> ... )` := p) {
		if (`<{Formal ","}* fs>` := f) {
			for (fp <- fs) {
				if ((Formal)`<Type t> <Name n>` := fp) params += < convertName(n), convertType(t), fp@\loc, n@\loc>; 					
			}
			params[size(params)-1].ptype = makeVarArgsType(params[size(params)-1].ptype);
		}
	}

	return params;
}

//
// Handle standard function declarations (i.e., function declarations with bodies). The header has
// already been processed, so this just enters the scope of the header and then processes the
// function body.
//
public ScopeInfo handleFunction(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, ScopeInfo scopeInfo) {
	scopeInfo = handleTags(ts, scopeInfo);

	// First, get back the scope item at location l so we can switch into the proper function scope
	if (debug) println("NAMESPACE: Switching to function with signature <s>");
	scopeInfo = pushScope(getLayerAtLocation(l, scopeInfo), scopeInfo);
	
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
			scopeInfo = handleFunctionBody(b,scopeInfo);
		}

		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
			scopeInfo = handleFunctionBody(b,scopeInfo);
		}
	}
	
	return popScope(scopeInfo);	
}

//
// Handle function bodies
//
public ScopeInfo handleFunctionBody(FunctionBody fb, ScopeInfo scopeInfo) {
	if (`{ <Statement* ss> }` := fb) {
		for (s <- ss) scopeInfo = handleStatement(s, scopeInfo);
	} else {
		throw "handleFunctionBody, unexpected syntax for body <fb>";
	}
	return scopeInfo;
}

//
// Check is visibility represents public or private
//
private bool isPublic(Visibility v) {
	return (`public` := v);
}

//
// Introduce the annotation name into the current scope. Duplicates are not allowed, so we check for them
// here and tag the name with a scope error if we find one.
//
public ScopeInfo handleAnnotationDeclarationNamesOnly(Tags t, Visibility v, Type t, Type ot, Name n, loc l, ScopeInfo scopeInfo) {
	scopeInfo = handleTagsNamesOnly(t, scopeInfo);
	scopeInfo = justScopeInfo(checkForDuplicationAnnotations(addAnnotationToScope(convertName(n),convertType(t),convertType(ot),isPublic(v),l,scopeInfo), n@\loc));
	return scopeInfo;
}

// TODO: The annotation name was handled above, here we just check to make sure the types used are actually
// in scope.
public ScopeInfo handleAnnotationDeclaration(Tags t, Visibility v, Type t, Type ot, Name n, loc l, ScopeInfo scopeInfo) {
	return handleTags(t, scopeInfo);
}

public ScopeInfo handleTagDeclaration(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts, loc l, ScopeInfo scopeInfo) {
	scopeInfo = handleTags(t, scopeInfo);
	throw "handleTagDeclaration not yet implemented";
}

public ScopeInfo handleTagDeclarationNamesOnly(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts, loc l, ScopeInfo scopeInfo) {
	scopeInfo = handleTagsNamesOnly(t, scopeInfo);
	throw "handleTagDeclarationNamesOnly not yet implemented";
}

public ScopeInfo handleRuleDeclarationNamesOnly(Tags t, Name n, PatternWithAction p, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Handling rule declaration names for rule <n> = <p>");
	scopeInfo = handleTagsNamesOnly(t, scopeInfo);
	scopeInfo = justScopeInfo(checkForDuplicateRules(addRuleToScope(convertName(n), l, scopeInfo), n@\loc));
	return scopeInfo;
}
								
public ScopeInfo handleRuleDeclaration(Tags t, Name n, PatternWithAction p, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Handling rule declaration for rule <n> = <p>");
	scopeInfo = handleTags(t, scopeInfo);
	scopeInfo = handlePatternWithAction(p, scopeInfo);
	return scopeInfo;
}

public ScopeInfo handleTestNamesOnly(Test t, loc l, ScopeInfo scopeInfo) {
	throw "handleTestNamesOnly not yet implemented";
}

public ScopeInfo handleTest(Test t, loc l, ScopeInfo scopeInfo) {
	throw "handleTest not yet implemented";
}

//
// Handle abstract ADT declarations (ADT's without variants). This introduces the ADT name into scope. Note
// that duplicate ADT names are not an error; the constructors of all ADTs sharing the same name will be
// merged together, allowing them to be introduced piecemeal.
//
public ScopeInfo handleAbstractADTNamesOnly(Tags ts, Visibility v, UserType adtType, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found Abstract ADT: <adtType>");
	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
	scopeInfo = justScopeInfo(addScopeItemUses(addADTToScope(convertUserType(adtType), isPublic(v), l, scopeInfo),[<true,getUserTypeRawName(adtType)@\loc>]));
	return scopeInfo;
}

//
// This just handles the tags; the ADT name was introduced into scope in handleAbstractADTNamesOnly, so
// there is nothing left to process at this point.
//
public ScopeInfo handleAbstractADT(Tags ts, Visibility v, UserType adtType, loc l, ScopeInfo scopeInfo) {
	return handleTags(ts, scopeInfo);
}

//
// Handle ADT declarations (ADT's with variants). This will introduce the ADT and constructor names into
// scope. It will also check for overlaps with the constructor names to ensure references to introduced
// constructors can be unambiguous.
//
public ScopeInfo handleADTNamesOnly(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found ADT: <adtType>");
	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
	ResultTuple rt = addScopeItemUses(addADTToScope(convertUserType(adtType), isPublic(v), l, scopeInfo),[<true,getUserTypeRawName(adtType)@\loc>]);
	ScopeItemId adtId = head(rt.addedItems);
	scopeInfo = justScopeInfo(rt);

	// Process each given variant, adding it into scope and saving the generated id	
	set[ScopeItemId] variantSet = { };
	for (var <- vars) {
		if (`<Name n> ( <{TypeArg ","}* args> )` := var) {
			ResultTuple rt2 = checkConstructorOverlap(addScopeItemUses(addConstructorToScope(convertName(n), [ convertTypeArg(targ) | targ <- args ], adtId, true, l, scopeInfo),[<true,n@\loc>]),n@\loc);
			ScopeItemId variantId = head(rt2.addedItems);
			scopeInfo = justScopeInfo(rt2);
			variantSet += variantId; 			
		}
	}
	
	scopeInfo.scopeItemMap[adtId].variants = variantSet;
	return scopeInfo;
}

//
// The ADT declaration is brought into scope with the last function, therefore this just
// checks the tags to make sure they are sensible but doesn't further process the
// ADT.
//
public ScopeInfo handleADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, ScopeInfo scopeInfo) {
	return handleTags(ts, scopeInfo);
}

//
// Handle alias declarations
//
// TODO: Should tag the aliased type with where it comes from
//
public ScopeInfo handleAliasNamesOnly(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, ScopeInfo scopeInfo) {
	if (debug) println("NAMESPACE: Found alias: <aliasType> = <aliasedType>");
	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);

	Name aliasRawName = getUserTypeRawName(aliasType);
	RName aliasName = convertName(aliasRawName);
	scopeInfo = justScopeInfo(checkForDuplicateAliases(addScopeItemUses(addAliasToScope(convertType(aliasType), convertType(aliasedType), isPublic(v), l, scopeInfo),[<true,aliasRawName@\loc>]),aliasRawName@\loc));
	return scopeInfo;
}

public ScopeInfo handleAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, ScopeInfo scopeInfo) {
	return handleTags(ts, scopeInfo); 
}

public ScopeInfo handleViewNamesOnly(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts, loc l, ScopeInfo scopeInfo) {
	scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
	throw "handleViewNamesOnly not yet implemented";
}

public ScopeInfo handleView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts, loc l, ScopeInfo scopeInfo) {
	return handleTags(ts, scopeInfo);
}

//
// Handle individual statements
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
			scopeInfo = justScopeInfo(pushNewBooleanScope(s@\loc, scopeInfo));
			for (e <- es) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleStatement(b, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			if (debug) println("NAMESPACE: Inside while statement <s>");
			scopeInfo = handleLabel(l,scopeInfo);			
			scopeInfo = justScopeInfo(pushNewBooleanScope(s@\loc, scopeInfo));
			for (e <- es) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleStatement(b, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			if (debug) println("NAMESPACE: Inside do statement <s>");
			scopeInfo = handleExpression(e, handleStatement(b, handleLabel(l,scopeInfo)));			
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			if (debug) println("NAMESPACE: Inside if with else statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			
			scopeInfo = justScopeInfo(pushNewBooleanScope(s@\loc, scopeInfo));
			for (e <- es) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleStatement(bf, handleStatement(bt, scopeInfo));
			scopeInfo = popScope(scopeInfo);
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			if (debug) println("NAMESPACE: Inside if statement <s>");
			
			scopeInfo = handleLabel(l,scopeInfo);			
			scopeInfo = justScopeInfo(pushNewBooleanScope(s@\loc, scopeInfo));
			for (e <- es) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleStatement(bt, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			if (debug) println("NAMESPACE: Inside switch statement <s>");			
			scopeInfo = handleExpression(e,handleLabel(l,scopeInfo));						
			for (c <- cs) scopeInfo = handleCase(c, scopeInfo);
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
			scopeInfo = markReturnType(getEnclosingFunctionType(scopeInfo), s, scopeInfo);
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
			scopeInfo = handleTagsNamesOnly(ts, scopeInfo);
			scopeInfo = handleFunctionNamesOnly(ts,v,sig,fb,s@\loc,scopeInfo);
					
			// Now, descend into the function, processing the body
			scopeInfo = handleTags(ts, scopeInfo);
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
			for (ct <- cs) scopeInfo = handleCatch(ct, scopeInfo);
		}
		
		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			if (debug) println("NAMESPACE: Inside try with finally statement <s>");

			scopeInfo = handleStatement(b, scopeInfo);
			for (ct <- cs) scopeInfo = handleCatch(ct, scopeInfo);
			scopeInfo = handleStatement(bf, scopeInfo);
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			if (debug) println("NAMESPACE: Inside block statement <s>");

			scopeInfo = handleLabel(l,scopeInfo);			
			scopeInfo = justScopeInfo(pushNewBlockScope(s@\loc, scopeInfo));
			for (b <- bs) scopeInfo = handleStatement(b,scopeInfo);
			scopeInfo = popScope(scopeInfo);
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

		// Name _
		case (Expression)`_`: {
			scopeInfo = addScopeError(scopeInfo, exp@\loc, "_ cannot be used as a variable name in an expression.");
		}
		
		// Name
		case (Expression)`<Name n>`: {
			if (debug) println("NAMESPACE: Name: <exp>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
				if (debug) println("NAMESPACE: Adding use for <n> of items <getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))>");
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
				scopeInfo = addScopeError(scopeInfo, qn@\loc, "<qn> not defined before use (scope: <scopeInfo.currentScope>).");
				if (debug) println("NAMESPACE: Found undefined use of <qn>");
			}
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			if (debug) println("NAMESPACE: ReifiedType: <exp>");
			for (ei <- el) scopeInfo = handleExpression(ei, scopeInfo);
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			if (debug) println("NAMESPACE: Call or Tree: <exp>");
			scopeInfo = handleExpression(e1, scopeInfo);

			// Parameters maintain their own scope for backtracking purposes
			scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
			for (ei <- el) scopeInfo = handleExpression(ei, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		// List
		case `[<{Expression ","}* el>]` : {
			if (debug) println("NAMESPACE: List: <exp>");
			for (ei <- el) scopeInfo = handleExpression(ei, scopeInfo);
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			if (debug) println("NAMESPACE: Set: <exp>");
			for (ei <- el) scopeInfo = handleExpression(ei, scopeInfo);
		}

		// Tuple, just one expression
		case (Expression) `<<Expression ei>>` : {
			if (debug) println("NAMESPACE: Tuple: <exp>");
			scopeInfo = handleExpression(ei, scopeInfo);
		}

		// Tuple, more than one expression
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			if (debug) println("NAMESPACE: Tuple: <exp>");
			scopeInfo = handleExpression(ei, scopeInfo);
			for (eli <- el)	scopeInfo = handleExpression(eli, scopeInfo);
		}

		// TODO: Map: Need to figure out a syntax that works for matching this
		// case ...
		//case appl(prod([],cf(sort("Expression")),no-attrs),[...]) :

		// Closure
		case `<Type t> <Parameters p> { <Statement+ ss> }` : {
			if (debug) println("NAMESPACE: Closure: <exp>");
			list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params = handleParametersNamesOnly(p, scopeInfo);
			scopeInfo = justScopeInfo(pushNewClosureScope(convertType(t), params, exp@\loc, scopeInfo));
			for (s <- ss) scopeInfo = handleStatement(s, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			if (debug) println("NAMESPACE: VoidClosure: <exp>");
			list[tuple[RName pname, RType ptype, loc ploc, loc nloc]] params = handleParametersNamesOnly(p, scopeInfo);
			scopeInfo = justScopeInfo(pushNewVoidClosureScope(params, exp@\loc, scopeInfo));
			for (s <- ss) scopeInfo = handleStatement(s, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {
			if (debug) println("NAMESPACE: NonEmptyBlock: <exp>");
			scopeInfo = justScopeInfo(pushNewBlockScope(s@\loc, scopeInfo));
			for (s <- ss) scopeInfo = handleStatement(s,scopeInfo);
			scopeInfo = popScope(scopeInfo);
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
			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e, scopeInfo);
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e, scopeInfo);
			}	
		}

		// Negation
		case `! <Expression e>` : {
			if (debug) println("NAMESPACE: Negation: <exp>");
			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e, scopeInfo);
				scopeInfo = popScope(scopeInfo);
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

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			if (debug) println("NAMESPACE: In: <exp>");
			
			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			if (debug) println("NAMESPACE: LessThan: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			if (debug) println("NAMESPACE: LessThanOrEq: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			if (debug) println("NAMESPACE: GreaterThan: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			if (debug) println("NAMESPACE: GreaterThanOrEq: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			if (debug) println("NAMESPACE: Equals: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			if (debug) println("NAMESPACE: NotEquals: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			if (debug) println("NAMESPACE: IfThenElse: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e3,handleExpression(e2,handleExpression(e1, scopeInfo)));
			}	
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			if (debug) println("NAMESPACE: IfDefinedOtherwise: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			if (debug) println("NAMESPACE: Implication: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));

				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);

				scopeInfo = popScope(scopeInfo);
			} else {
				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);
			}	
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			if (debug) println("NAMESPACE: Equivalence: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));

				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);

				scopeInfo = popScope(scopeInfo);
			} else {
				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);
			}	
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			if (debug) println("NAMESPACE: And: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handleExpression(e2, handleExpression(e1,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handleExpression(e2,handleExpression(e1, scopeInfo));
			}	
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			if (debug) println("NAMESPACE: Or: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));

				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);

				scopeInfo = popScope(scopeInfo);
			} else {
				// First, push a scope for the left-hand side of the or and evaluate
				// the expression there
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope1 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e1, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Now, do the same for the right-hand side.
				scopeInfo = justScopeInfo(pushNewOrScope(exp@\loc, scopeInfo));
				ScopeItemId orScope2 = scopeInfo.currentScope;
				scopeInfo = handleExpression(e2, scopeInfo);
				scopeInfo = popScope(scopeInfo);

				// Merge the names shared by both branches of the or into the current scope
				scopeInfo = mergeOrLayers(scopeInfo, [orScope1, orScope2], scopeInfo.currentScope);
			}	
		}
		
		// Match
		case `<Pattern p> := <Expression e>` : {
			if (debug) println("NAMESPACE: Match: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handlePattern(p, handleExpression(e,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handlePattern(p, handleExpression(e, scopeInfo));
			}	
		}

		// NoMatch
		case `<Pattern p> !:= <Expression e>` : {
			if (debug) println("NAMESPACE: NoMatch: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handlePattern(p, handleExpression(e,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handlePattern(p, handleExpression(e, scopeInfo));
			}	
		}

		// Enumerator
		case `<Pattern p> <- <Expression e>` : {
			if (debug) println("NAMESPACE: Enumerator: <exp>");

			if (! inBoolLayer (scopeInfo)) {
				scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
				scopeInfo = handlePattern(p, handleExpression(e,scopeInfo));
				scopeInfo = popScope(scopeInfo);
			} else {
				scopeInfo = handlePattern(p, handleExpression(e, scopeInfo));
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
		case `( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
			if (debug) println("NAMESPACE: Reducer: <exp>");
			
			scopeInfo = handleExpression(e1, scopeInfo);
			
			// Open a new boolean scope for the generators, this makes them available in the reducer
			scopeInfo = justScopeInfo(pushNewBooleanScope(exp@\loc, scopeInfo));
			
			// Calculate the scope info for the generators and expressors; we add "it" as a variable automatically
			for (e <- egs) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = addFreshVariable(RSimpleName("it"), ei@\loc, scopeInfo);
			scopeInfo = handleExpression(er, scopeInfo);
			
			// Switch back to the prior scope to take expression bound names and "it" out of scope
			scopeInfo = popScope(scopeInfo);			
		}
		
		// It
		case `it` : {
			if (debug) println("NAMESPACE: It: <exp>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, RSimpleName("it"))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, RSimpleName("it")), exp@\loc);
				if (debug) println("NAMESPACE: Adding use for <exp>");
			} else {
				scopeInfo = addScopeError(scopeInfo, exp@\loc, "<exp> not currently in scope.");
				if (debug) println("NAMESPACE: Found undefined use of <exp>");
			}
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
		// Name _
		case (Assignable)`_`: {
			scopeInfo = addScopeError(scopeInfo, a@\loc, "_ cannot be used as a variable name in an assignable.");
		}
	
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("NAMESPACE: QualifiedName Assignable: <a>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
				if (debug) println("NAMESPACE: Adding use for <qn>");
			} else {
				scopeInfo = addFreshVariable(convertName(qn), qn@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh type for <qn>");
			}
		}
		
		case `<Assignable al> [ <Expression e> ]` : {
			if (debug) println("NAMESPACE: Subscripted Assignable: <a>");
			scopeInfo = handleAssignable(al, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);			
		}
		
		case `<Assignable al> . <Name n>` : {
			if (debug) println("NAMESPACE: Field Assignable: <a>");
			scopeInfo = handleAssignable(al, scopeInfo);
			// TODO: Most likely we don't need the code below, since the names are based on the type, not on
			// names defined earlier in the current scope. Verify this.
			//if (debug) println("NAMESPACE: Adding use for <n>");
			//scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
		}
		
		case `<Assignable al> ? <Expression e>` : {
			if (debug) println("NAMESPACE: IfDefined Assignable: <a>");
			scopeInfo = handleAssignable(al, scopeInfo);
			scopeInfo = handleExpression(e, scopeInfo);			
		}
		
		case `<Assignable al> @ <Name n>` : {
			if (debug) println("NAMESPACE: Attribute Assignable: <a>");
			scopeInfo = handleAssignable(al, scopeInfo);
			if (debug) println("NAMESPACE: Adding use for <n>");
			scopeInfo = addItemUses(scopeInfo, getAnnotationItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
		}

		// Tuple assignable, with just one tuple element		
		case (Assignable)`< <Assignable ai> >` : {
			if (debug) println("NAMESPACE: Tuple Assignable: <a>");
			scopeInfo = handleAssignable(ai, scopeInfo);
		}

		// Tuple assignable, with multiple elements in the tuple
		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
			if (debug) println("NAMESPACE: Tuple Assignable: <a>");
			scopeInfo = handleAssignable(ai, scopeInfo);
			for (ali <- al) {
				scopeInfo = handleAssignable(ali, scopeInfo);
			}
		}
		
		// NOTE: We are not currently supporting this case, as we are considering removing it from
		// the language as an unsafe operation.
//		case `<Name n> ( <{Assignable ","}+ al> )` : {
//			if (debug) println("NAMESPACE: Constructor Assignable: <a>");
//			if (debug) println("NAMESPACE: Adding use for <qn>");
//			scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
//			for (ali <- al) {
//				scopeInfo = handleAssignable(ali, scopeInfo);
//			}
//		}

		default : {
			if (debug) println("NAMESPACE: Unhandled assignable case! <a>");
			throw "Found unhandled assignable case during namespace construction: <a>";
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
			case (Variable) `<Name n>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Illegal redefinition of <n>.");
					if (debug) println("NAMESPACE: Illegal redefinition of <n>");
				} 

				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), true, vb@\loc, scopeInfo), [<true,n@\loc>])); 
				if (debug) println("NAMESPACE: Added variable <n> with type <prettyPrintType(convertType(t))>");
			}
				
			case (Variable) `<Name n> = <Expression e>` : {
				if (debug) println("NAMESPACE: Adding variable <n>");
				if (size(getItemsForNameFB(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {
					scopeInfo = addScopeError(scopeInfo, n@\loc, "Illegal redefinition of <n>.");
					if (debug) println("NAMESPACE: Illegal redefinition of <n>");
				} 

				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), true, vb@\loc, scopeInfo), [<true,n@\loc>])); 
				scopeInfo = handleExpression(e, scopeInfo);
				if (debug) println("NAMESPACE: Added variable <n> with type <prettyPrintType(convertType(t))>");
			}

			default : throw "Unexpected local var declaration syntax, <vb>";
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
		if (size(ls) > 0) {		
			scopeInfo = addScopeError(scopeInfo, n@\loc, "Label <n> has already been defined.");
		}
		scopeInfo = justScopeInfo(addLabelToScope(convertName(n), l@\loc, scopeInfo));					
	} // label can be empty... 
	return scopeInfo;
}

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
	if (RSimpleName("it") := n) scopeInfo.itBinder[nloc] = freshType;
	scopeInfo.freshType = scopeInfo.freshType + 1;
	scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(n, freshType, false, nloc, scopeInfo), [<true,nloc>]));
	return scopeInfo;
}

public ScopeInfo addFreshAnonymousVariable(loc nloc, ScopeInfo scopeInfo) {
	RType freshType = makeInferredType(scopeInfo.freshType);
	scopeInfo.inferredTypeMap[scopeInfo.freshType] = freshType;
	scopeInfo.freshType = scopeInfo.freshType + 1;
	scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(RSimpleName("_"), freshType, false, nloc, scopeInfo), [<true,nloc>]));
	return scopeInfo;
}

public ScopeInfo addFreshContainerVariable(RName n, loc nloc, ScopeInfo scopeInfo) {
	RType freshType = makeContainerType(makeInferredType(scopeInfo.freshType));
	scopeInfo.inferredTypeMap[scopeInfo.freshType] = freshType;
	scopeInfo.freshType = scopeInfo.freshType + 1;
	scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(n, freshType, false, nloc, scopeInfo), [<true,nloc>]));
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
		case (Pattern)`_`: {
			if (debug) println("NAMESPACE: Anonymous NamePattern: <pat>");
			
			scopeInfo = addFreshAnonymousVariable(pat@\loc, scopeInfo);			
			if (debug) println("NAMESPACE: Adding anonymous variable for <pat>");
		}

		// Name
		case (Pattern)`<Name n>`: {
			if (debug) println("NAMESPACE: NamePattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n)), n@\loc);
				if (debug) println("NAMESPACE: Adding use for <n>");
			} else {
				scopeInfo = addFreshVariable(convertName(n), n@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh type for <n>");
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

		// Tuple, with just one element
		case `<<Pattern pi>>` : {
			if (debug) println("NAMESPACE: TuplePattern: <pat>");
			scopeInfo = handlePattern(pi, scopeInfo);
		}

		// Tuple, with multiple elements
		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("NAMESPACE: TuplePattern: <pat>");
			scopeInfo = handlePattern(pi, scopeInfo);
			for (p <- pl) scopeInfo = handlePattern(p, scopeInfo);
		}

		// TODO: Map: Need to figure out a syntax that works for matching this

		// Typed Variable
		case (Pattern) `<Type t> <Name n>` : {
			if (debug) println("NAMESPACE: TypedVariablePattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(n))) > 0) {		
				scopeInfo = addScopeError(scopeInfo, pat@\loc, "Illegal shadowing of already declared name: <n>");
			} else {
				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), pat@\loc, scopeInfo), [<true,n@\loc>]));
				if (debug) println("NAMESPACE: Adding new declaration for <n>");
			}
		}

		// Multi Variable
		case `<QualifiedName qn> *` : {
			if (debug) println("NAMESPACE: MultiVariablePattern: <pat>");
			if (size(getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn))) > 0) {		
				scopeInfo = addItemUses(scopeInfo, getItemsForName(scopeInfo, scopeInfo.currentScope, convertName(qn)), qn@\loc);
				if (debug) println("NAMESPACE: Adding use for <qn>");
			} else {
				scopeInfo = addFreshContainerVariable(convertName(qn), qn@\loc, scopeInfo);			
				if (debug) println("NAMESPACE: Adding fresh container type for for <qn>");
			}
		}

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
				scopeInfo = justScopeInfo(addScopeItemUses(addVariableToScope(convertName(n), convertType(t), pat@\loc, scopeInfo), [<true,n@\loc>]));
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
			scopeInfo = justScopeInfo(pushNewPatternMatchScope(pwa@\loc, scopeInfo));
			scopeInfo = handleExpression(e, handlePattern(p, scopeInfo));
			scopeInfo = popScope(scopeInfo);
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			scopeInfo = justScopeInfo(pushNewPatternMatchScope(pwa@\loc, scopeInfo));
			scopeInfo = handlePattern(p, scopeInfo);
			for (e <- es) scopeInfo = handleExpression(e, scopeInfo);
			scopeInfo = handleExpression(er, scopeInfo);
			scopeInfo = popScope(scopeInfo);
		}
		
		case `<Pattern p> : <Statement s>` : {
			scopeInfo = justScopeInfo(pushNewPatternMatchScope(pwa@\loc, scopeInfo));
			scopeInfo = handleStatement(s, handlePattern(p, scopeInfo));			
			scopeInfo = popScope(scopeInfo);
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
			ScopeItemId anid = getOneFrom(items);
			return getTypeForItem(scopeInfo, getOneFrom(items));
		} else {
			return ROverloadedType({ getTypeForItem(scopeInfo, sii) | sii <- items });
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

// TODO: Add tag handling here
public ScopeInfo handleTagsNamesOnly(Tags ts, ScopeInfo scopeInfo) {
	return scopeInfo;
}

public ScopeInfo handleTags(Tags ts, ScopeInfo scopeInfo) {
	return scopeInfo;
}

// NOTE: The code in ExampleGraph appears to be out of date, so this doesn't
// appear to work. Look at other ways to visualize.
//public void showScope(ScopeInfo scopeInfo, int w, int h) {
//	// First, create a node for each item in the scope info
//	nodes = [ box([id("<n>"), width(20), height(20), fillColor("lightblue")], text("<si>")) | n <- domain(scopeInfo.scopeItemMap), si := scopeInfo.scopeItemMap[n]];
//
//	// Now, create the edges based on the relation
//	edges = [ edge("<f>", "<t>") | < f, t > <- scopeInfo.scopeRel];
//
//	// Finally, render
//	render(graph([width(w), height(h)], nodes, edges));

