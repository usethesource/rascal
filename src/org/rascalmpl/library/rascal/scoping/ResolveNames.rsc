@bootstrapParser
module rascal::scoping::ResolveNames

import List;
import Graph;
import IO;
import Set;
import Map;
import ParseTree;
import Message;

import rascal::checker::ListUtils;
import rascal::checker::TreeUtils;
import rascal::types::Types;
import rascal::types::SubTypes;
import rascal::types::TypeSignatures;
import rascal::checker::Annotations;
import rascal::scoping::SymbolTable;
import rascal::scoping::ScopedTypes;

import rascal::syntax::RascalRascal;

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
// 14. DONE: Add alias expansion (since we have all the name information here) -- maybe add
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
// 21. DONE: Add support for tests.
//
// 22. Add checking to ensure that insert, append, fail, break, and continue are all used in the
//     correct contexts.
//

//
// This is a hack -- this ensures the empty list is of type list[RType], not list[Void] or list[Value]
//
list[RType] mkEmptyList() { return tail([makeVoidType()]); }

//
// Same hack -- this ensures the empty list is of type list[STItemId], not list[Void] or list[Value]
//
list[STItemId] mkEmptySIList() { return tail([3]); }

//
// Run the name resolution pass over a tree.
//
public Tree resolveTree(Tree t) {
    println("NAME RESOLVER: Getting Imports for Module");
    list[Import] imports = getImports(t);
    println("NAME RESOLVER: Got Imports");
    
    println("NAME RESOLVER: Generating Signature Map");
    SignatureMap sigMap = populateSignatureMap(imports);
    println("NAME RESOLVER: Generated Signature Map");
    
    println("NAME RESOLVER: Generating Symbol Table"); 
    SymbolTable st = buildTable(t, sigMap);
    println("NAME RESOLVER: Generated Symbol Table");
    
    println("NAME RESOLVER: Associating Scope Information with Names");
    Tree t2 = addNamesToTree(st,t);
    println("NAME RESOLVER: Associated Scope Information with Names");

    println("NAME RESOLVER: Adding Information for Scope Errors");
    if (size(st.scopeErrorMap) > 0)
        t2 = t2[@messages = { error(l,s) | l <- st.scopeErrorMap<0>, s <- st.scopeErrorMap[l] }]; 
    println("NAME RESOLVER: Added Information for Scope Errors");

    return t2;     
}

public tuple[Tree,SymbolTable] resolveTreeAux(Tree t) {
    println("NAME RESOLVER: Getting Imports for Module");
    list[Import] imports = getImports(t);
    println("NAME RESOLVER: Got Imports");
    
    println("NAME RESOLVER: Generating Signature Map");
    SignatureMap sigMap = populateSignatureMap(imports);
    println("NAME RESOLVER: Generated Signature Map");
    
    println("NAME RESOLVER: Generating Symbol Table"); 
    SymbolTable st = buildTable(t, sigMap);
    println("NAME RESOLVER: Generated Symbol Table");
    
    println("NAME RESOLVER: Associating Scope Information with Names");
    Tree t2 = addNamesToTree(st,t);
    println("NAME RESOLVER: Associated Scope Information with Names");

    println("NAME RESOLVER: Adding Information for Scope Errors");
    if (size(st.scopeErrorMap) > 0)
        t2 = t2[@messages = { error(l,s) | l <- st.scopeErrorMap<0>, s <- st.scopeErrorMap[l] }]; 
    println("NAME RESOLVER: Added Information for Scope Errors");

    return <t2,st>;     
}

//
// Retrieve the list of imports from the module
//
public list[Import] getImports(Tree t) {
    if ((Module) `<Header h> <Body b>` := t) {
        switch(h) {
            case (Header)`<Tags t> module <QualifiedName n> <Import* i>` : 
                return [il | il <- i];
            case (Header)`<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : 
                return [il | il <- i];
            default : 
                throw "Unexpected module format";
        }
    }
    throw "getModuleName, unexpected module syntax, cannot find module name";
}

//
// Get the name of the module
//
public RName getModuleName(Tree t) {
    if ((Module) `<Tags t> module <QualifiedName qn> <Import* i> <Body b>` := t)
        return convertName(qn);
    else if ((Module) `<Tags t> module <QualifiedName qn> <ModuleParameters p> <Import* i> <Body b>` := t)
        return convertName(qn);
    throw "getModuleName, unexpected module syntax, cannot find module name";
}

//
// Given a tree representing a module, build the namespace. Note that
// we only process one module at a time, although this code can
// trigger the processing of other modules that are imported.
//
public SymbolTable buildTable(Tree t, SignatureMap signatures) {
    // Create the new symbol table, including pushing the top layer
    SymbolTable symbolTable = justSymbolTable(pushNewTopScope(t@\loc, createNewSymbolTable()));

    // Add the module present in this tree. This also handles loading the
    // modules imported by this module. Each module is attached under the
    // top layer.
    if ((Module) `<Header h> <Body b>` := t) {
        if ((Header)`<Tags tg> module <QualifiedName qn> <Import* i>` := h || (Header)`<Tags tg> module <QualifiedName qn> <ModuleParameters p> <Import* i>` := h) {
            symbolTable = handleModuleImports(i, signatures, symbolTable);
            symbolTable = justSymbolTable(setCurrentModule(pushNewModuleScope(convertName(qn), t@\loc, symbolTable)));
            symbolTable = handleModuleBodyFull(b, handleModuleBodyNamesOnly(b, symbolTable));
            symbolTable = popScope(symbolTable);
        } else {
            throw "buildTable: failed to match module syntax";
        }
    } else {
        throw "buildTable: failed to match module syntax";
    }

    // NOTE: We remain inside the top scope, we don't pop that when we are done.
    return symbolTable;
}		

//
// Using the information gathered in the symbol table, add IDs to each name indicating which
// symbol table item(s) the name points to
//
// TODO: Removing qualifiers causes an error during filtering. This needs to be fixed (I'm
// looking at you, Jurgen!)
//
public Tree addNamesToTree(SymbolTable symbolTable, Tree t) {
    str generateDocString(set[STItemId] ids) {
        str result = "";
        if (size(ids) == 1) {
            result = prettyPrintSIWLoc(symbolTable, symbolTable.scopeItemMap[getOneFrom(ids)]);
        } else {
            result = "Multiple Options:\n";
// TODO: File bug report, this should work            
//            result += joinList(toList({ symbolTable.scopeItemMap[id] | id <- ids }), prettyPrintSIWLoc, "\n", "");
            list[str] pp = [ prettyPrintSIWLoc(symbolTable, symbolTable.scopeItemMap[id]) | id <- ids ];
            result += joinList(pp, str(str s) { return s; }, "\n", "");             
        }
        
        return result;
    }
    
    Tree annotateNode(Tree tn, loc l) {
        set[STItemId] ids = symbolTable.itemUses[l];
        return tn[@nameIds = ids][@doc = generateDocString(ids)];
    }
    
    return visit(t) {
        case (Expression)`<Name n>` :
            if (n@\loc in symbolTable.itemUses)
                insert(annotateNode(t,n@\loc));

        case (Expression)`<QualifiedName qn>` :
            if (qn@\loc in symbolTable.itemUses)
                insert(annotateNode(t,qn@\loc));

        case (Pattern)`<Name n>` :
            if (n@\loc in symbolTable.itemUses)
                insert(annotateNode(t,n@\loc));
    
        case (Pattern)`<QualifiedName qn>` :
            if (qn@\loc in symbolTable.itemUses)
                insert(annotateNode(t,qn@\loc));
                
        case (UserType)`<QualifiedName qn>` :
            if (qn@\loc in symbolTable.itemUses)
                insert(annotateNode(t,qn@\loc));

        case (Assignable)`<QualifiedName qn>` :
            if (qn@\loc in symbolTable.itemUses)
                insert(annotateNode(t,qn@\loc));
    };
}

//
// Load information from the imported modules. This also checks for conflicts in the loaded information,
// generating scope errors where appropriate.
//
public SymbolTable handleModuleImports(Import* il, SignatureMap signatures, SymbolTable symbolTable) {
    list[Import] impList = [imp | imp <- il];

    for (imp <- impList) {
        if ((Import)`import <ImportedModule im> ;` := imp || (Import)`extend <ImportedModule im> ;` := imp) {
            if (imp in signatures)
                symbolTable = importModuleTypes(im, signatures[imp], imp@\loc, symbolTable);
            else
                symbolTable = addScopeError(symbolTable, imp@\loc, "Could not build signature for imported module <getNameOfImportedModule(im)>");
        }
    }

    for (imp <- impList) {
        if ((Import)`import <ImportedModule im> ;` := imp || (Import)`extend <ImportedModule im> ;` := imp) {
            if (imp in signatures)
                symbolTable = importModuleItems(im, signatures[imp], imp@\loc, symbolTable);
        }
    }

	return symbolTable;
}

public SymbolTable importModuleTypes(ImportedModule im, RSignature signature, loc l, SymbolTable symbolTable) {
    if ((ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` := im ||
        (ImportedModule)`<QualifiedName qn> <ModuleActuals ma>` := im ||
        (ImportedModule)`<QualifiedName qn> <Renamings rn>` := im ||
        (ImportedModule)`<QualifiedName qn>` := im) {
        return addImportedTypesToScope(qn, signature, l, symbolTable);
    } else {
        throw "Error in importModuleTypes, case not handled: <im>";
    }
}

public SymbolTable importModuleItems(ImportedModule im, RSignature signature, loc l, SymbolTable symbolTable) {
    if ((ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` := im ||
        (ImportedModule)`<QualifiedName qn> <ModuleActuals ma>` := im ||
        (ImportedModule)`<QualifiedName qn> <Renamings rn>` := im ||
        (ImportedModule)`<QualifiedName qn>` := im) {
        return addImportedItemsToScope(qn, signature, l, symbolTable);
    } else {
        throw "Error in importModuleItems, case not handled: <im>";
    }
}

//
// Add imported types into the scope, based on the given signature.
//
public SymbolTable addImportedTypesToScope(QualifiedName qn, RSignature signature, loc l, SymbolTable symbolTable) {
    symbolTable = justSymbolTable(pushNewModuleScope(convertName(qn), l, symbolTable));

    for (AliasSigItem(aliasName,aliasType,at) <- signature.signatureItems) {
        symbolTable = justSymbolTable(addAliasToScope(aliasType, true, at, symbolTable));
        // TODO: Add duplicate checking again later
        // symbolTable = justSymbolTable(checkForDuplicateAliases(addAliasToTopScope(aliasType, true, at, symbolTable), at));
        symbolTable = justSymbolTable(addAliasToTopScope(aliasType, true, at, symbolTable));
    }

    for (ADTSigItem(adtName,adtType,at) <- signature.signatureItems) {
        symbolTable = justSymbolTable(addADTToScope(adtType, true, at, symbolTable));
        symbolTable = justSymbolTable(addADTToTopScope(adtType, true, at, symbolTable));
    }

    return popScope(symbolTable);
}

//
// Add non-type imported items into scope, based on the given signature.
//
public SymbolTable addImportedItemsToScope(QualifiedName qn, RSignature signature, loc l, SymbolTable symbolTable) {
    symbolTable = pushScope(getOneFrom(symbolTable.scopeNames[symbolTable.currentScope,convertName(qn)]), symbolTable);

    for (item <- signature.signatureItems) {
        switch(item) {
            case FunctionSigItem(fn,ps,rt,at) : {
                symbolTable = justSymbolTable(pushNewFunctionScope(fn, rt, ps, [ ], true, isVarArgsParameters(ps), at, symbolTable));
                symbolTable = handleParametersNamesOnly(ps, symbolTable);
                symbolTable = popScope(symbolTable);
                // TODO: Add back in check for overlap
//                if (!willFunctionOverlap(fn,st,symbolTable,symbolTable.topSTItemId)) {
                    symbolTable = justSymbolTable(pushNewFunctionScopeAtTop(fn, rt, ps, [ ], true, isVarArgsParameters(ps), at, symbolTable));
                    symbolTable = handleParametersNamesOnly(ps, symbolTable);
                    symbolTable = popScope(symbolTable);
//                } 
                // TODO: Issue a warning if the function would overlap
                // TODO: Maybe remove other overlaps from the top-level environment
			}

            case VariableSigItem(vn,st,at) : {
                symbolTable = justSymbolTable(addVariableToScope(vn, st, true, at, symbolTable));
                if (! (size(getItems(symbolTable, symbolTable.topSTItemId, vn, FCVs())) > 0)) {
                    symbolTable = justSymbolTable(addVariableToTopScope(vn, st, true, at, symbolTable));
                } 
            }

            case ConstructorSigItem(constructorName,constructorType,at) : {
                RName adtName = getADTName(constructorType);
                list[RNamedType] constructorTypes = getConstructorArgumentTypesWithNames(constructorType);
                
                set[STItemId] possibleADTs = getItems(symbolTable, symbolTable.currentScope, adtName, Types());
                possibleADTs = { t | t <- possibleADTs, ADTItem(_,_,_) := symbolTable.scopeItemMap[t] };
                if (size(possibleADTs) == 0) throw "Error: Cannot find ADT <prettyPrintName(adtName)> to associate with constructor: <item>";
                STItemId adtItemId = getOneFrom(possibleADTs);
                symbolTable = justSymbolTable(addConstructorToScope(constructorName, constructorTypes, adtItemId, true, at, symbolTable));

                possibleADTs = getItems(symbolTable, symbolTable.topSTItemId, adtName, Types());
                possibleADTs = { t | t <- possibleADTs, ADTItem(_,_,_) := symbolTable.scopeItemMap[t] };
                if (size(possibleADTs) == 0) throw "Error: Cannot find ADT <prettyPrintName(adtName)> to associate with constructor: <item>";
                adtItemId = getOneFrom(possibleADTs);
                // TODO: Check for overlap here; if we find an overlap, this will trigger an error, since we should not have
                // overlapping constructors and, unlike functions, we can't just take a "last in wins" approach.
                //symbolTable = justSymbolTable(checkConstructorOverlap(addConstructorToTopScope(constructorName, constructorTypes, adtItemId, true, at, symbolTable),at));
                symbolTable = justSymbolTable(addConstructorToTopScope(constructorName, constructorTypes, adtItemId, true, at, symbolTable));
            }

            case AnnotationSigItem(an,st,ot,at) : {
                symbolTable = justSymbolTable(addAnnotationToScope(an, st, ot, true, at, symbolTable)); 
                // TODO: Need to actually make this check on types, we could have the same name appear
                // multiple times, so, for now, take this check out...				
//				if (size(getAnnotationItemsForName(symbolTable, symbolTable.topSTItemId, an)) == 0) {
                // TODO: Add back in duplicates check 
//                    symbolTable = justSymbolTable(checkForDuplicateAnnotations(addAnnotationToTopScope(an, st, ot, true, at, symbolTable),at));
                    symbolTable = justSymbolTable(addAnnotationToTopScope(an, st, ot, true, at, symbolTable));
//				} 
            }

            // TODO
            // TagSigItem(RName tagName, list[RType] tagTypes, loc at)
            // case TagSigItem(tn,tt,at) : 3;
        }
    }

    return popScope(symbolTable);
}

//
// Process the individual items contained at the top level of the module.
//
public SymbolTable handleModuleBody(Body b, SymbolTable symbolTable) {
    return handleModuleBodyFull(b, handleModuleBodyNamesOnly(b, symbolTable));
}

//
// Gather the names of variables and functions. These are visible throughout the module (a variable 
// can be used in a function declared higher up in the file, for instance) so just the top-level 
// names are gathered first (i.e., we don't descend into function bodies, etc). We process the
// names in a specific order: first aliases, then ADTs, then everything else. The first two
// could potentially be merged, but doing aliases and ADTs first ensures all type names are
// visible when we start to process functions, variables, etc.
//
// TODO: See if it makes sense to merge the first two loops.
//
public SymbolTable handleModuleBodyNamesOnly(Body b, SymbolTable symbolTable) {
    if ((Body)`<Toplevel* ts>` := b) {
        for (Toplevel t <- ts) {
            switch(t) {
                // Alias
                case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` :
                    symbolTable = handleAliasNamesOnly(tgs,v,typ,btyp,t@\loc,symbolTable);
            }
        }

        for (Toplevel t <- ts) {
            switch(t) {
                // ADT without variants
                case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` :
                    symbolTable = handleAbstractADTNamesOnly(tgs,v,typ,t@\loc,symbolTable);

                // ADT with variants
                case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` :
                    symbolTable = handleADTNamesOnly(tgs,v,typ,vars,t@\loc,symbolTable);
            }
        }

        for (Toplevel t <- ts) {
            switch(t) {
                // Variable declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` :
                    symbolTable = handleVarItemsNamesOnly(tgs, v, typ, vs, symbolTable);

                // Abstract (i.e., without a body) function declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` :
                    symbolTable = handleAbstractFunctionNamesOnly(tgs,v,s,t@\loc,symbolTable);

                // Concrete (i.e., with a body) function declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` :
                    symbolTable = handleFunctionNamesOnly(tgs,v,s,fb,t@\loc,symbolTable);

                // Concrete (i.e., with a body) function declaration, in expression form
// TODO: Add this back in when this case is supported by the parser                
//                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> = <Expression e>;` :
//                    symbolTable = handleFunctionExpNamesOnly(tgs,v,s,e,t@\loc,symbolTable);

                // Annotation declaration
                case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` :
                    symbolTable = handleAnnotationDeclarationNamesOnly(tgs,v,typ,otyp,n,t@\loc,symbolTable);

                // Tag declaration
                case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` :
                    symbolTable = handleTagDeclarationNamesOnly(tgs,v,k,n,typs,t@\loc,symbolTable);

                // Rule declaration
                case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` :
                    symbolTable = handleRuleDeclarationNamesOnly(tgs,n,pwa,t@\loc,symbolTable);

                // Test
                case (Toplevel) `<Test tst> ;` :
                    symbolTable = handleTestNamesOnly(tst,t@\loc,symbolTable);

                // View
                case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` :
                    symbolTable = handleViewNamesOnly(tgs,v,n,sn,alts,t@\loc,symbolTable);
            }
        }
    }

    // Now, consolidate ADT definitions and look for errors
    symbolTable = consolidateADTDefinitionsForLayer(symbolTable, symbolTable.currentScope, true);
    symbolTable = checkADTDefinitionsForConsistency(symbolTable);

    return symbolTable;
}

//
// Identify names used inside functions or in static initializers, noting type information. This pass 
// actually descends into functions, building the scope information within them as well.
//
public SymbolTable handleModuleBodyFull(Body b, SymbolTable symbolTable) {
    if ((Body)`<Toplevel* ts>` := b) {
        for (Toplevel t <- ts) {
            switch(t) {
                // Variable declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` :
                    symbolTable = handleVarItems(tgs, v, typ, vs, symbolTable);

                // Abstract (i.e., without a body) function declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : 
                    symbolTable = handleAbstractFunction(tgs, v, s, t@\loc, symbolTable);

                // Concrete (i.e., with a body) function declaration
                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` :
                    symbolTable = handleFunction(tgs, v, s, fb, t@\loc, symbolTable);

                // Concrete (i.e., with a body) function declaration, in expression form
// TODO: Add this back in when this case is supported by the parser                
//                case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> = <Expression e>;` :
//                    symbolTable = handleFunctionExp(tgs,v,s,e,t@\loc,symbolTable);

                // Annotation declaration
                case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` :
                    symbolTable = handleAnnotationDeclaration(tgs, v, typ, otyp, n, t@\loc, symbolTable);

                // Tag declaration
                case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` :
                    symbolTable = handleTagDeclaration(tgs, v, k, n, typs, t@\loc, symbolTable);

                // Rule declaration
                case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` :
                    symbolTable = handleRuleDeclaration(tgs, n, pwa, t@\loc, symbolTable);

                // Test
                case (Toplevel) `<Test tst> ;` :
                    symbolTable = handleTest(tst, t@\loc, symbolTable);

                // ADT without variants
                case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` :
                    symbolTable = handleAbstractADT(tgs, v, typ, t@\loc, symbolTable);

                // ADT with variants
                case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` :
                    symbolTable = handleADT(tgs, v, typ, vars, t@\loc, symbolTable);

                // Alias
                case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` :
                    symbolTable = handleAlias(tgs, v, typ, btyp, t@\loc, symbolTable);

                // View
                case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` :
                    symbolTable = handleView(tgs, v, n, sn, alts, t@\loc, symbolTable);

                default: throw "handleModuleBodyFull: No match for item <t>";
            }
        }
    }

    return symbolTable;
}

//
// Handle variable declarations, with or without initializers. We don't allow duplicate top-level names, but we do
// allow this name to shadow a name from an imported module. This is why the duplicate check is module bounded.
//
public SymbolTable handleVarItemsNamesOnly(Tags ts, Visibility v, Type t, {Variable ","}+ vs, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(ts, symbolTable);

    ConvertTuple ct = convertRascalType(symbolTable, t);
    RType varType = ct.rtype; symbolTable = ct.symbolTable;

    for (vb <- vs) {
        if ((Variable)`<Name n>` := vb || (Variable)`<Name n> = <Expression e>` := vb) {
            if (size(getItems(symbolTable, symbolTable.currentScope, convertName(n), FCVs())) > 0) {		
                symbolTable = addScopeError(symbolTable, n@\loc, "Duplicate declaration of name <n>");
            } 
            symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(convertName(n), varType, isPublic(v), vb@\loc, symbolTable),[<true,n@\loc>]));
        }
    }
    return symbolTable;
}

//
// Process the initializer expressions given inside the variable declaration.
//
public SymbolTable handleVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs, SymbolTable symbolTable) {
    symbolTable = handleTags(ts, symbolTable);
    for ((Variable)`<Name n> = <Expression e>` <- vs) symbolTable = handleExpression(e, symbolTable);
    return symbolTable;
}

//
// Handle standard function declarations (i.e., function declarations with bodies), but
// do NOT descend into the bodies
//
public SymbolTable handleFunctionNamesOnly(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, SymbolTable symbolTable) {
    return handleAbstractFunctionNamesOnly(ts,v,s,l,symbolTable);		
}

//
// Handle standard function declarations with expressions for bodies, but
// do NOT descend into the bodies
//
public SymbolTable handleFunctionExpNamesOnly(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, SymbolTable symbolTable) {
    return handleAbstractFunctionNamesOnly(ts,v,s,l,symbolTable);       
}

//
// Handle abstract function declarations (i.e., function declarations without bodies)
//
public SymbolTable handleAbstractFunctionNamesOnly(Tags ts, Visibility v, Signature s, loc l, SymbolTable symbolTable) {
    // Add the new function into the scope and process any parameters.
    SymbolTable addFunction(Name n, RType retType, loc rloc, Parameters ps, list[RType] thrsTypes, bool isPublic, SymbolTable symbolTable) {
        symbolTable = justSymbolTable(addSTItemUses(pushNewFunctionScope(convertName(n),retType,ps,thrsTypes,isPublic,isVarArgsParameters(ps),l,symbolTable),[<false,l>, <true,n@\loc>]));
        symbolTable = handleParametersNamesOnly(ps, symbolTable);
        
        // Check if the return type has any type variables; if so, make sure they are in scope
        for (tvv <- collectTypeVars(retType)) {
            set[STItemId] tvItems = getItems(symbolTable, symbolTable.currentScope, getTypeVarName(tvv), TypeVars());
            if (size(tvItems) == 0) {
                symbolTable = addScopeError(symbolTable, rloc, "Type variable <prettyPrintName(tvv.varName)> used in return type not previously declared.");        
            } else {
               // TODO: We should just have one, check to see if we have more
               RType tvType = symbolTable.scopeItemMap[getOneFrom(tvItems)].typeVar;
               if (tvType.varTypeBound != tvv.varTypeBound) {
                    symbolTable = addScopeError(symbolTable, rloc, "Illegal redefinition of bound on type variable <prettyPrintName(tvv.varName)> with existing bound <prettyPrintType(tvType.varTypeBound)>.");        
               }
            }
        }
        
        return popScope(symbolTable);
    }

    symbolTable = handleTagsNamesOnly(ts, symbolTable);

    switch(s) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType retType = ct.rtype; symbolTable = ct.symbolTable;
            symbolTable = addFunction(n, retType, t@\loc, ps, mkEmptyList(), isPublic(v), symbolTable);
        }

        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : {
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType retType = ct.rtype; symbolTable = ct.symbolTable;
            list[RType] throwsTypes = [ ];
            for (thrsi <- thrs) { ct = convertRascalType(symbolTable, thrsi); throwsTypes = throwsTypes + ct.rtype; symbolTable = ct.symbolTable; }
            symbolTable = addFunction(n, retType, t@\loc, ps, throwsTypes, isPublic(v), symbolTable);
        }
    }
    return symbolTable;
}

//
// This function has no body, and the function header was processed already, so just process the tags.
//
public SymbolTable handleAbstractFunction(Tags ts, Visibility v, Signature s, loc l, SymbolTable symbolTable) {
    return handleTags(ts, symbolTable);
}

//
// Handle parameter declarations. Parameters currently have no defaults, etc, so there is no other
// version of this function (no non "NamesOnly" version).
//
// TODO: A current requirement is that, for varargs functions, the last parameter is just
// a type variable pattern. Enforce that here.
//
public SymbolTable handleParametersNamesOnly(Parameters p, SymbolTable symbolTable) {
    if ((Parameters)`( <Formals f> )` := p || (Parameters)`( <Formals f> ... )` := p) {
        if ((Formals)`<{Pattern ","}* fs>` := f) {
            for (fp <- fs) {
                symbolTable = handlePattern(fp, symbolTable);
            }
        }
    }
    
    return symbolTable;
}

//
// Handle standard function declarations (i.e., function declarations with bodies). The header has
// already been processed, so this just enters the scope of the header and then processes the
// function body.
//
public SymbolTable handleFunction(Tags ts, Visibility v, Signature s, FunctionBody b, loc l, SymbolTable symbolTable) {
    symbolTable = handleTags(ts, symbolTable);

    // First, get back the scope item at location l so we can switch into the proper function scope
    symbolTable = pushScope(getLayerAtLocation(l, symbolTable), symbolTable);

    // Now, process the function body
    switch(s) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
            symbolTable = handleFunctionBody(b,symbolTable);
        }

        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
            symbolTable = handleFunctionBody(b,symbolTable);
        }
    }

    return popScope(symbolTable);	
}

//
// Handle standard function declarations with expressions for bodies. The header has
// already been processed, so this just enters the scope of the header and then processes the
// function body.
//
public SymbolTable handleFunctionExp(Tags ts, Visibility v, Signature s, Expression e, loc l, SymbolTable symbolTable) {
    symbolTable = handleTags(ts, symbolTable);

    // First, get back the scope item at location l so we can switch into the proper function scope
    symbolTable = pushScope(getLayerAtLocation(l, symbolTable), symbolTable);

    // Now, process the function body
    switch(s) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : {
            symbolTable = handleExpression(e,symbolTable);
        }

        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ tts> ` : {
            symbolTable = handleExpression(e,symbolTable);
        }
    }

    return popScope(symbolTable);   
}

//
// Handle function bodies
//
public SymbolTable handleFunctionBody(FunctionBody fb, SymbolTable symbolTable) {
    if ((FunctionBody)`{ <Statement* ss> }` := fb) {
        for (s <- ss) symbolTable = handleStatement(s, symbolTable);
    } else {
        throw "handleFunctionBody, unexpected syntax for body <fb>";
    }
    return symbolTable;
}

//
// Check is visibility represents public or private
//
private bool isPublic(Visibility v) {
    return ((Visibility)`public` := v);
}

//
// Introduce the annotation name into the current scope. Duplicates are not allowed, so we check for them
// here and tag the name with a scope error if we find one.
//
// TODO: We should probably put these into a table, like the ADTs, so we can figure out more easily
// during checking which values have which annotations.
//
// TODO: Make sure the duplicate check only checks for duplicates on the same type, we can have multiple
// declarations for the same annotation name as long as they are on different types.
//
public SymbolTable handleAnnotationDeclarationNamesOnly(Tags t, Visibility v, Type ty, Type ot, Name n, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(t, symbolTable);
    ConvertTuple ct = convertRascalType(symbolTable, ty);
    RType annoType = ct.rtype; symbolTable = ct.symbolTable;
    ct = convertRascalType(symbolTable, ot);
    RType onType = ct.rtype; symbolTable = ct.symbolTable;
    // TODO: Add back in duplicate check, if needed
//    symbolTable = justSymbolTable(checkForDuplicateAnnotations(addAnnotationToScope(convertName(n),annoType,onType,isPublic(v),l,symbolTable), n@\loc));
    symbolTable = justSymbolTable(addAnnotationToScope(convertName(n),annoType,onType,isPublic(v),l,symbolTable));
    return symbolTable;
}

//
// All checks were done above specifically for annotations, so just handle the tags here.
//
public SymbolTable handleAnnotationDeclaration(Tags t, Visibility v, Type ty, Type ot, Name n, loc l, SymbolTable symbolTable) {
    return handleTags(t, symbolTable);
}

//
// TODO: Implement
//
public SymbolTable handleTagDeclaration(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts, loc l, SymbolTable symbolTable) {
    return handleTags(t, symbolTable);
}

//
// TODO: Implement
//
public SymbolTable handleTagDeclarationNamesOnly(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts, loc l, SymbolTable symbolTable) {
    return handleTagsNamesOnly(t, symbolTable);
}

//
// In this first pass we just worry about the name of the rule, we don't yet descend into the pattern.
//
public SymbolTable handleRuleDeclarationNamesOnly(Tags t, Name n, PatternWithAction p, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(t, symbolTable);
    return justSymbolTable(addRuleToScope(convertName(n), l, handleTagsNamesOnly(t, symbolTable)));
}

//
// For the second pass, descend into the rule pattern with action.
//							
public SymbolTable handleRuleDeclaration(Tags t, Name n, PatternWithAction p, loc l, SymbolTable symbolTable) {
    return handlePatternWithAction(p, handleTags(t, symbolTable));
}

//
// Tests don't introduce any top-level names, so we only need to handle the test tag on this first pass.
//
public SymbolTable handleTestNamesOnly(Test t, loc l, SymbolTable symbolTable) {
    if ((Test)`<Tags tgs> test <Expression exp>` := t || (Test)`<Tags tgs> test <Expression exp> : <StringLiteral sl>` := t) {
        return handleTagsNamesOnly(tgs, symbolTable);
    }
    throw "Unexpected syntax for test: <t>";
}

//
// Tests can use names in the test expression, so we need to descend into the expression of the test
// on the second pass.
//
public SymbolTable handleTest(Test t, loc l, SymbolTable symbolTable) {
    if ((Test)`<Tags tgs> test <Expression exp>` := t || (Test)`<Tags tgs> test <Expression exp> : <StringLiteral sl>` := t) {
        return handleExpression(exp,handleTags(tgs, symbolTable));
    }
    throw "Unexpected syntax for test: <t>";
}

//
// Handle abstract ADT declarations (ADT's without variants). This introduces the ADT name into scope. Note
// that duplicate ADT names are not an error; the constructors of all ADTs sharing the same name will be
// merged together, allowing them to be introduced piecemeal.
//
public SymbolTable handleAbstractADTNamesOnly(Tags ts, Visibility v, UserType adtType, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(ts, symbolTable);
    RType adtBase = convertUserType(adtType);
    RType adtRType = makeParameterizedADTType(getUserTypeName(adtBase),getUserTypeParameters(adtBase));
    return justSymbolTable(addSTItemUses(addADTToScope(adtRType, isPublic(v), l, symbolTable),[<true,getUserTypeRawName(adtType)@\loc>]));
}

//
// This just handles the tags; the ADT name was introduced into scope in handleAbstractADTNamesOnly, so
// there is nothing left to process at this point.
//
public SymbolTable handleAbstractADT(Tags ts, Visibility v, UserType adtType, loc l, SymbolTable symbolTable) {
    return handleTags(ts, symbolTable);
}

//
// Handle ADT declarations (ADT's with variants). This will introduce the ADT and constructor names into
// scope. It will also check for overlaps with the constructor names to ensure references to introduced
// constructors can be unambiguous.
//
public SymbolTable handleADTNamesOnly(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(ts, symbolTable);
    RType adtBase = convertUserType(adtType);
    RType adtRType = makeParameterizedADTType(getUserTypeName(adtBase),getUserTypeParameters(adtBase));
    ResultTuple rt = addSTItemUses(addADTToScope(adtRType, isPublic(v), l, symbolTable),[<true,getUserTypeRawName(adtType)@\loc>]);
    STItemId adtId = head(rt.addedItems);
    symbolTable = justSymbolTable(rt);

    // Process each given variant, adding it into scope	
    for (var <- vars) {
        if ((Variant)`<Name n> ( <{TypeArg ","}* args> )` := var) {
            list[RNamedType] cparams = [ ];
            for (targ <- args) { ConvertTupleN ct = convertRascalTypeArg(symbolTable, targ); cparams = cparams + ct.rtype; symbolTable = ct.symbolTable; }
            //symbolTable = justSymbolTable(checkConstructorOverlap(addSTItemUses(addConstructorToScope(convertName(n), cparams, adtId, true, l, symbolTable),[<true,n@\loc>]),n@\loc));
            // TODO: Re-add overlap check
            symbolTable = justSymbolTable(addSTItemUses(addConstructorToScope(convertName(n), cparams, adtId, true, l, symbolTable),[<true,n@\loc>]));
        }
    }

    return symbolTable;
}

//
// The ADT declaration is brought into scope with the last function, therefore this just
// checks the tags to make sure they are sensible but doesn't further process the
// ADT.
//
public SymbolTable handleADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars, loc l, SymbolTable symbolTable) {
    return handleTags(ts, symbolTable);
}

//
// Handle alias declarations. Note that we don't check to see if the type being pointed to exists, since it may
// be another alias, ADT, etc that is also being processed in this first step.
//
public SymbolTable handleAliasNamesOnly(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(ts, symbolTable);
    Name aliasRawName = getUserTypeRawName(aliasType);
    RName aliasName = convertName(aliasRawName);

    ConvertTuple ct = convertRascalUserType(symbolTable, aliasType);
    RType aType = ct.rtype; symbolTable = ct.symbolTable;
    ct = convertRascalType(symbolTable, aliasedType);
    RType tType = ct.rtype; symbolTable = ct.symbolTable;
    RType aliasRType = makeParameterizedAliasType(getUserTypeName(aType), tType, getUserTypeParameters(aType));
    
    //symbolTable = justSymbolTable(checkForDuplicateAliases(addSTItemUses(addAliasToScope(aliasRType, isPublic(v), l, symbolTable),[<true,aliasRawName@\loc>]),aliasRawName@\loc));
    // TODO: Add checking for duplicates
    symbolTable = justSymbolTable(addSTItemUses(addAliasToScope(aliasRType, isPublic(v), l, symbolTable), [<true,aliasRawName@\loc>]));

    return symbolTable;
}

//
// Handle the alias declaration in the second pass.
//
// TODO: This may be a good time to verify that the aliased type actually exists.
//
public SymbolTable handleAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType, loc l, SymbolTable symbolTable) {
    return handleTags(ts, symbolTable); 
}

//
// TODO: Implement later, views aren't currently supported
//
public SymbolTable handleViewNamesOnly(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts, loc l, SymbolTable symbolTable) {
    symbolTable = handleTagsNamesOnly(ts, symbolTable);
    //throw "handleViewNamesOnly not yet implemented";
    return symbolTable;
}

//
// TODO: Implement later
//
public SymbolTable handleView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts, loc l, SymbolTable symbolTable) {
    return handleTags(ts, symbolTable);
}

//
// Handle individual statements
//
public SymbolTable handleStatement(Statement s, SymbolTable symbolTable) {
    switch(s) {
        // solve statement; note that the names are not binders, they should already be in scope
        case (Statement)`solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
            for (v <- vs)
                symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, convertName(v), FCVs()), v@\loc);

            if ((Bound)`; <Expression e>` := b)
                symbolTable = handleExpression(e, symbolTable);

            symbolTable = handleStatement(sb, symbolTable);		
        }

        // for statement; this opens a boolean scope, ensuring bindings in the for expression are visible just in the body
        case (Statement)`<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
            symbolTable = handleLabel(l,symbolTable);			
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- es) symbolTable = handleExpression(e, symbolTable);
            symbolTable = handleStatement(b, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // while statement; this opens a boolean scope, ensuring bindings in the while expression are visible just in the body
        case (Statement)`<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
            symbolTable = handleLabel(l,symbolTable);			
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- es) symbolTable = handleExpression(e, symbolTable);
            symbolTable = handleStatement(b, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // do statement; in this case the expression is not a binder, since it comes after the first iteration
        case (Statement)`<Label l> do <Statement b> while (<Expression e>);` :
            symbolTable = handleExpression(e, handleStatement(b, handleLabel(l,symbolTable)));			

        // if statement; this opens a boolean scope, ensuring bindings in the if guard expression are visible just in the body		
        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
            symbolTable = handleLabel(l,symbolTable);			
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- es) symbolTable = handleExpression(e, symbolTable);
            symbolTable = handleStatement(bf, handleStatement(bt, symbolTable));
            symbolTable = popScope(symbolTable);
        }

        // if statement with no else; this opens a boolean scope, ensuring bindings in the if guard expression are visible just in the body
        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> <NoElseMayFollow _>` : {
            symbolTable = handleLabel(l,symbolTable);			
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- es) symbolTable = handleExpression(e, symbolTable);
            symbolTable = handleStatement(bt, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // switch statement
        case (Statement)`<Label l> switch (<Expression e>) { <Case+ cs> }` : {
            symbolTable = handleExpression(e,handleLabel(l,symbolTable));						
            for (c <- cs) symbolTable = handleCase(c, symbolTable);
        }

        // visit statement
        case (Statement)`<Label l> <Visit v>` :
            symbolTable = handleVisit(v, handleLabel(l,symbolTable));						

        // expression statement
        case (Statement)`<Expression e> ;` :
            symbolTable = handleExpression(e, symbolTable);

        // assignment statement
        case (Statement)`<Assignable a> <Assignment op> <Statement b>` :
            symbolTable = handleStatement(b, handleAssignable(a, symbolTable));

        // assert statement
        case (Statement)`assert <Expression e> ;` :
            symbolTable = handleExpression(e, symbolTable);

        // assert statement with guard
        case (Statement)`assert <Expression e> : <Expression em> ;` :
            symbolTable = handleExpression(em, handleExpression(e, symbolTable));

        // return statement -- we also save the ID of the associated function item, since this makes
        // it easier for the type checker (or other anayses) to know which function this return is
        // associated with 
        case (Statement)`return <Statement b>` : {
            symbolTable = handleStatement(b, symbolTable);
            <inFunction, functionLayerId> = getSurroundingFunction(symbolTable, symbolTable.currentScope);
            if (inFunction) {
                symbolTable = markReturnFunction(symbolTable.scopeItemMap[functionLayerId].itemId, s@\loc, symbolTable);
            } else {
                symbolTable = addScopeError(symbolTable, s@\loc, "Return statement must be given inside a function");
            }
        }

        // throw statement
        case (Statement)`throw <Statement b>` :
            symbolTable = handleStatement(b, symbolTable);

        // insert statement
        case (Statement)`insert <DataTarget dt> <Statement b>` :
            symbolTable = handleStatement(b, handleDataTarget(dt, symbolTable));

        // append statement
        case (Statement)`append <DataTarget dt> <Statement b>` :
            symbolTable = handleStatement(b, handleDataTarget(dt, symbolTable));

        // local function declaration; the called functions handle the scoping so we don't have to here
        case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
            // First get back the function signature information, creating the scope item
            symbolTable = handleFunctionNamesOnly(ts,v,sig,fb,s@\loc,handleTagsNamesOnly(ts, symbolTable));

            // Now, descend into the function, processing the body
            symbolTable = handleFunction(ts,v,sig,fb,s@\loc,handleTags(ts, symbolTable));
        }

        // local function declaration; the called functions handle the scoping so we don't have to here
// TODO: Add this back in when this case is supported by the parser                
//        case (Statement) `<Tags ts> <Visibility v> <Signature sig> = <Expression e>;` : {
//            // First get back the function signature information, creating the scope item
//            symbolTable = handleFunctionExpNamesOnly(ts,v,sig,e,s@\loc,handleTagsNamesOnly(ts, symbolTable));
//
//            // Now, descend into the function, processing the body
//            symbolTable = handleFunctionExp(ts,v,sig,e,s@\loc,handleTags(ts, symbolTable));
//        }

        // local variable declaration
        case (Statement) `<Type t> <{Variable ","}+ vs> ;` :
            symbolTable = handleLocalVarItems(t,vs,symbolTable);

        // dynamic variable declaration; TODO this is not implemented yet by Rascal
        case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` :
            symbolTable = handleLocalVarItems(t,vs,symbolTable);

        // break statement		
        case (Statement)`break <Target t> ;` :
            symbolTable = handleTarget(t, symbolTable);

        // fail statement
        case (Statement)`fail <Target t> ;` :
            symbolTable = handleTarget(t, symbolTable);

        // continue statement
        case (Statement)`continue <Target t> ;` :
            symbolTable = handleTarget(t, symbolTable);

        // try/catch statement
        case (Statement)`try <Statement b> <Catch+ cs>` : {
            symbolTable = handleStatement(b, symbolTable);
            for (ct <- cs) symbolTable = handleCatch(ct, symbolTable);
        }

        // try/catch/finally statement
        case (Statement)`try <Statement b> <Catch+ cs> finally <Statement bf>` : {
            symbolTable = handleStatement(b, symbolTable);
            for (ct <- cs) symbolTable = handleCatch(ct, symbolTable);
            symbolTable = handleStatement(bf, symbolTable);
        }

        // labeled statement block
        case (Statement)`<Label l> { <Statement+ bs> }` : {
            symbolTable = handleLabel(l,symbolTable);			
            symbolTable = justSymbolTable(pushNewBlockScope(s@\loc, symbolTable));
            for (b <- bs) symbolTable = handleStatement(b,symbolTable);
            symbolTable = popScope(symbolTable);
        }
    }

    return symbolTable;
}

//
// Pick apart a map to properly introduce its names into scope
//
public list[Tree] getMapMappings(Tree t) {
    list[Tree] mapParts = [ ];

    // t[1] holds the parse tree contents for the map
    if (list[Tree] mapTop := t[1]) {
        // mapTop[0] = (, mapTop[1] = layout, mapTop[2] = map contents, mapTop[3] = layout, mapTop[4] = ), so we get out 2
        if (appl(_,list[Tree] mapItems) := mapTop[2]) {
            if (size(mapItems) > 0) {
                // The map items include layout and commas, so we use a mod 4 to account for this: we have
                // item layout comma layout item layout comma layout etc
                list[Tree] mapMappings = [ mapItems[n] | n <- [0..size(mapItems)-1], n % 4 == 0];

                // Each item should have the domain and range inside. It is organized as pat layout : layout pat
                for (n <- [0..size(mapMappings)-1]) {
                    if (appl(_,list[Tree] mapContents) := mapMappings[n]) {
                        if (size(mapContents) == 5 && `<Tree tl>` := mapContents[0] && `<Tree tr>` := mapContents[4]) {
                            mapParts = mapParts + [ tl, tr ]; 
                        }
                    } 
                }
            }
        }
    }

    return mapParts;
}

//
// Return domain : range expression pairs as a list of tuples for a map expression
//
public list[tuple[Expression mapDomain, Expression mapRange]] getMapExpressionContents(Expression exp) {
    list[Tree] mm = getMapMappings(exp); // What comes back is in the form [domain,range,domain,range,...]

    if (size(mm) > 0)
        return [ <el, er> | n <- [0..size(mm)-1], n % 2 == 0, `<Expression el>` := mm[n], `<Expression er>` := mm[n+1] ];
    else
        return [ ];
}

//
// Return domain : range pattern pairs as a list of tuples for a map pattern
//
public list[tuple[Pattern mapDomain, Pattern mapRange]] getMapPatternContents(Pattern pat) {
    list[Tree] mm = getMapMappings(pat); // What comes back is in the form [domain,range,domain,range,...]

    if (size(mm) > 0)
        return [ <pl, pr> | n <- [0..size(mm)-1], n % 2 == 0, `<Pattern pl>` := mm[n], `<Pattern pr>` := mm[n+1] ];
    else
        return [ ];
}

//
// Scope handling for map expressions -- this is done separately since we cannot use matching to get back
// the parts of the map.
//
public SymbolTable handleMapExpression(Expression exp, SymbolTable symbolTable) {
    list[tuple[Expression mapDomain, Expression mapRange]] mapContents = getMapExpressionContents(exp);
    for (<md,mr> <- mapContents) symbolTable = handleExpression(mr, handleExpression(md, symbolTable));
    return symbolTable;
}

//
// TODO: We still need to add support for concrete syntax, both here and in patterns (below).
//
public SymbolTable handleExpression(Expression exp, SymbolTable symbolTable) {
    SymbolTable handleExpName(RName n, loc l, SymbolTable symbolTable) {
        if (size(getItems(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {
            symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, n, FCVs()), l);
        } else {
            symbolTable = addScopeError(symbolTable, l, "<prettyPrintName(n)> not defined before use");
        }
        return symbolTable;
    }

    switch(exp) {
        // Strings (in case of interpolation)
        case (Expression)`<StringLiteral sl>`: {
            list[Tree] ipl = prodFilter(sl,bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
        }

        // Locations (in case of interpolation)
        case (Expression)`<LocationLiteral ll>`: {
            list[Expression] ipl = prodFilter(ll, bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd; });
            for (ipe <- ipl) symbolTable = handleExpression(ipe, symbolTable);
        }

        // Name _
        // TODO: This is not really true, since _ can appear in a relation subscript. Handle this there, though,
        // as _ cannot appear elsewhere...
        case (Expression)`_`: 
            symbolTable = addScopeError(symbolTable, exp@\loc, "_ cannot be used as a variable name in an expression.");

		// Name (other than _)
        case (Expression)`<Name n>`: 
            symbolTable = handleExpName(convertName(n),n@\loc,symbolTable);

        // QualifiedName
        case (Expression)`<QualifiedName qn>`: 
            symbolTable = handleExpName(convertName(qn),qn@\loc,symbolTable);

        // ReifiedType
        case (Expression)`<BasicType t> ( <{Expression ","}* el> )` : {
            // NOTE: We don't ensure t is well-formed here, because it need not be; for instance, to
            // give the reified type form of list[int], we would specify list(int()), but this means
            // that list, the basic type, is not a valid type, since it must take an element type if
            // used for a variable type, function parameter type, etc. So,
            // TODO: Make sure el is a well-formed type expression, like list(int())
            for (ei <- el) symbolTable = handleExpression(ei, symbolTable);
        }

        // CallOrTree
        case (Expression)`<Expression e1> ( <{Expression ","}* el> )` : {
            symbolTable = handleExpression(e1, symbolTable);

            // Parameters maintain their own scope for backtracking purposes
            symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
            for (ei <- el) symbolTable = handleExpression(ei, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // List
        case (Expression)`[<{Expression ","}* el>]` :
            for (ei <- el) symbolTable = handleExpression(ei, symbolTable);

        // Set
        case (Expression)`{<{Expression ","}* el>}` :
            for (ei <- el) symbolTable = handleExpression(ei, symbolTable);

        // Tuple, just one expression
        case (Expression) `<<Expression ei>>` :
            symbolTable = handleExpression(ei, symbolTable);

        // Tuple, more than one expression
        case (Expression)`<<Expression ei>, <{Expression ","}* el>>` : {
            symbolTable = handleExpression(ei,symbolTable);
            for (eli <- el) symbolTable = handleExpression(eli, symbolTable);
        }

        // Closure
        // TODO: Should we verify that p is not varargs here?
        case (Expression)`<Type t> <Parameters p> { <Statement+ ss> }` : {
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType retType = ct.rtype; symbolTable = ct.symbolTable;
            symbolTable = justSymbolTable(pushNewClosureScope(retType,p,exp@\loc,symbolTable));
            symbolTable = handleParametersNamesOnly(p, symbolTable);
            
            // Check if the return type has any type variables; if so, make sure they are in scope
            for (tvv <- collectTypeVars(retType)) {
                set[STItemId] tvItems = getItems(symbolTable, symbolTable.currentScope, getTypeVarName(tvv), TypeVars());
                if (size(tvItems) == 0) {
                    symbolTable = addScopeError(symbolTable, t@\loc, "Type variable <prettyPrintName(tvv.varName)> used in return type not previously declared.");        
                } else {
                   // TODO: We should just have one, check to see if we have more
                   RType tvType = symbolTable.scopeItemMap[getOneFrom(tvItems)].typeVar;
                   if (tvType.varTypeBound != tvv.varTypeBound) {
                        symbolTable = addScopeError(symbolTable, t@\loc, "Illegal redefinition of bound on type variable <prettyPrintName(tvv.varName)> with existing bound <prettyPrintType(tvType.varTypeBound)>.");        
                   }
                }
            }
            
            for (s <- ss) symbolTable = handleStatement(s, symbolTable);
            symbolTable = popScope(symbolTable);
        }

		// VoidClosure
        case (Expression)`<Parameters p> { <Statement* ss> }` : {
            symbolTable = justSymbolTable(pushNewVoidClosureScope(p,exp@\loc,symbolTable));
            symbolTable = handleParametersNamesOnly(p, symbolTable);
            for (s <- ss) symbolTable = handleStatement(s, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // NonEmptyBlock
        case (Expression)`{ <Statement+ ss> }` : {
            symbolTable = justSymbolTable(pushNewBlockScope(s@\loc, symbolTable));
            for (s <- ss) symbolTable = handleStatement(s, symbolTable);
            symbolTable = popScope(symbolTable);
        }

        // Visit
        case (Expression) `<Label l> <Visit v>` :
            symbolTable = handleVisit(v, handleLabel(l,symbolTable));						

        // ParenExp
        case (Expression)`(<Expression e>)` :
            symbolTable = handleExpression(e, symbolTable);

        // Range
        case (Expression)`[ <Expression e1> .. <Expression e2> ]` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // StepRange
        case (Expression)`[ <Expression e1>, <Expression e2> .. <Expression e3> ]` :
            symbolTable = handleExpression(e3, handleExpression(e2, handleExpression(e1, symbolTable)));

        // FieldUpdate
        // NOTE: We don't add this name into the symbol table or try to look it up
        // since we don't actually even know if this name is valid. We need to know
        // the type of e1 first, so this is just handled by the type checker.
        case (Expression)`<Expression e1> [<Name n> = <Expression e2>]` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // FieldAccess
        // NOTE: We don't add this name into the symbol table or try to look it up
        // since we don't actually even know if this name is valid. We need to know
        // the type of e1 first, so this is just handled by the type checker.
        case (Expression)`<Expression e1> . <Name n>` :
            symbolTable = handleExpression(e1, symbolTable);

        // FieldProject
        // NOTE: We don't add this name into the symbol table or try to look it up
        // since we don't actually even know if this name is valid. We need to know
        // the type of e1 first, so this is just handled by the type checker.
        case (Expression)`<Expression e1> < <{Field ","}+ fl> >` :
            symbolTable = handleExpression(e1, symbolTable);

        // Subscript
        // NOTE: We explicitly handle _ here as a possible expression in the subscript.
        // If we find it, we just skip it. That way, above we can explicitly mark the
        // name _ as an error if we find it in another context. The type checker needs
        // to handle the case of whether _ is a valid expression in this context -- it
        // is for relations, but not for lists, for instance.
        case (Expression)`<Expression e1> [ <{Expression ","}+ el> ]` : {
            symbolTable = handleExpression(e1, symbolTable);
            for (e <- el) {
                if ((Expression)`_` := e) symbolTable = handleExpression(e, symbolTable);
            }
		}

        // IsDefined
        case (Expression)`<Expression e> ?` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e, symbolTable);
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Negation
        case (Expression)`! <Expression e>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e, symbolTable);
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Negative
        case (Expression)`- <Expression e> ` :
            symbolTable = handleExpression(e, symbolTable);

        // TransitiveReflexiveClosure
        case (Expression)`<Expression e> * ` :
            symbolTable = handleExpression(e, symbolTable);

        // TransitiveClosure
        case (Expression)`<Expression e> + ` :
            symbolTable = handleExpression(e, symbolTable);

        // GetAnnotation
        case (Expression)`<Expression e> @ <Name n>` : {
            symbolTable = handleExpression(e, symbolTable);
            symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, convertName(n), Annotations()), n@\loc);
        }

        // SetAnnotation
        case (Expression)`<Expression e1> [@ <Name n> = <Expression e2>]` : {
            symbolTable = handleExpression(e2,handleExpression(e1, symbolTable));
            symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, convertName(n), Annotations()), n@\loc);
        }

        // Composition
        case (Expression)`<Expression e1> o <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Product
        case (Expression)`<Expression e1> * <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Join
        case (Expression)`<Expression e1> join <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Div
        case (Expression)`<Expression e1> / <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Mod
        case (Expression)`<Expression e1> % <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Intersection
        case (Expression)`<Expression e1> & <Expression e2>` :
            symbolTable = handleExpression(e2, handleExpression(e1, symbolTable));

        // Plus
        case (Expression)`<Expression e1> + <Expression e2>` :
            symbolTable = handleExpression(e2,handleExpression(e1, symbolTable));

        // Minus
        case (Expression)`<Expression e1> - <Expression e2>` :
            symbolTable = handleExpression(e2,handleExpression(e1, symbolTable));

        // NotIn
        case (Expression)`<Expression e1> notin <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

		// In
        case (Expression)`<Expression e1> in <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // LessThan
        case (Expression)`<Expression e1> < <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // LessThanOrEq
        case (Expression)`<Expression e1> <= <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // GreaterThan
        case (Expression)`<Expression e1> > <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // GreaterThanOrEq
        case (Expression)`<Expression e1> >= <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Equals
        case (Expression)`<Expression e1> == <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // NotEquals
        case (Expression)`<Expression e1> != <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // IfThenElse (Ternary)
        case (Expression)`<Expression e1> ? <Expression e2> : <Expression e3>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e3, handleExpression(e2, handleExpression(e1, symbolTable)));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // IfDefinedOtherwise
        case (Expression)`<Expression e1> ? <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Implication
        case (Expression)`<Expression e1> ==> <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }

            // First, push a scope for the left-hand side of the or and evaluate
            // the expression there
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope1 = symbolTable.currentScope;
            symbolTable = handleExpression(e1, symbolTable);
            symbolTable = popScope(symbolTable);

            // Now, do the same for the right-hand side.
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope2 = symbolTable.currentScope;
            symbolTable = handleExpression(e2, symbolTable);
            symbolTable = popScope(symbolTable);

            // Merge the names shared by both branches of the or into the current scope
            symbolTable = mergeOrLayers(symbolTable, [orScope1, orScope2], symbolTable.currentScope);

            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Equivalence
        case (Expression)`<Expression e1> <==> <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }

            // First, push a scope for the left-hand side of the or and evaluate
            // the expression there
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope1 = symbolTable.currentScope;
            symbolTable = handleExpression(e1, symbolTable);
            symbolTable = popScope(symbolTable);

            // Now, do the same for the right-hand side.
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope2 = symbolTable.currentScope;
            symbolTable = handleExpression(e2, symbolTable);
            symbolTable = popScope(symbolTable);

            // Merge the names shared by both branches of the or into the current scope
            symbolTable = mergeOrLayers(symbolTable, [orScope1, orScope2], symbolTable.currentScope);

            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

		// And
        case (Expression)`<Expression e1> && <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handleExpression(e2, handleExpression(e1,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Or
        case (Expression)`<Expression e1> || <Expression e2>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }

            // First, push a scope for the left-hand side of the or and evaluate
            // the expression there
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope1 = symbolTable.currentScope;
            symbolTable = handleExpression(e1, symbolTable);
            symbolTable = popScope(symbolTable);

            // Now, do the same for the right-hand side.
            symbolTable = justSymbolTable(pushNewOrScope(exp@\loc, symbolTable));
            STItemId orScope2 = symbolTable.currentScope;
            symbolTable = handleExpression(e2, symbolTable);
            symbolTable = popScope(symbolTable);

            // Merge the names shared by both branches of the or into the current scope
            symbolTable = mergeOrLayers(symbolTable, [orScope1, orScope2], symbolTable.currentScope);

            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Match
        case (Expression)`<Pattern p> := <Expression e>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handlePattern(p, handleExpression(e,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // NoMatch
        case (Expression)`<Pattern p> !:= <Expression e>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handlePattern(p, handleExpression(e,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Enumerator
        case (Expression)`<Pattern p> <- <Expression e>` : {
            bool popAtTheEnd = false;
            if (! inBoolLayer (symbolTable)) {
                symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));
                popAtTheEnd = true;
            }
            symbolTable = handlePattern(p, handleExpression(e,symbolTable));
            if (popAtTheEnd) symbolTable = popScope(symbolTable);
        }

        // Set Comprehension
        case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` : {
            // Open a new boolean scope for the generators, this makes them available on the left
            symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));

            for (e <- er) symbolTable = handleExpression(e, symbolTable);
            for (e <- el) symbolTable = handleExpression(e, symbolTable);

            // Now pop the scope to take the names out of scope
            symbolTable = popScope(symbolTable);
        }

        // List Comprehension
        case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` : {
            // Open a new boolean scope for the generators, this makes them available on the left
            symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));

            for (e <- er) symbolTable = handleExpression(e, symbolTable);
            for (e <- el) symbolTable = handleExpression(e, symbolTable);

            // Now pop the scope to take the names out of scope
            symbolTable = popScope(symbolTable);
        }

        // Map Comprehension
        case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` : {
            // Open a new boolean scope for the generators, this makes them available on the left
            symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));

            for (e <- er) symbolTable = handleExpression(e, symbolTable);
            symbolTable = handleExpression(et, handleExpression(ef, symbolTable));

            // Now pop the scope to take the names out of scope
            symbolTable = popScope(symbolTable);
        }

        // Reducer
        case (Expression)`( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
            symbolTable = handleExpression(ei, symbolTable);

            // Open a new boolean scope for the generators, this makes them available in the reducer
            symbolTable = justSymbolTable(pushNewBooleanScope(exp@\loc, symbolTable));

            // Calculate the scope info for the generators and expressors; we add "it" as a variable automatically
            for (e <- egs) symbolTable = handleExpression(e, symbolTable);
            symbolTable = addFreshVariable(RSimpleName("it"), ei@\loc, symbolTable);
            symbolTable = handleExpression(er, symbolTable);

            // Switch back to the prior scope to take expression bound names and "it" out of scope
            symbolTable = popScope(symbolTable);			
        }

        // It
        case (Expression)`it` :
            symbolTable = handleExpName(RSimpleName("it"),exp@\loc,symbolTable);

        // All 
        case (Expression)`all ( <{Expression ","}+ egs> )` :
            for (e <- egs) symbolTable = handleExpression(e, symbolTable);

        // Any 
        case (Expression)`all ( <{Expression ","}+ egs> )` :
            for (e <- egs) symbolTable = handleExpression(e, symbolTable);
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // exp[0] is the production used, exp[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := exp[0]) {
        symbolTable = handleMapExpression(exp, symbolTable);
    }

    return symbolTable;
}

public SymbolTable handleStringTemplate(StringTemplate s, SymbolTable symbolTable) {
    switch(s) {
        case (StringTemplate)`for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- gens) symbolTable = handleExpression(e, symbolTable);
            for (st <- pre) symbolTable = handleStatement(st, symbolTable);
            list[Tree] ipl = prodFilter(body, 
                             bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- post) symbolTable = handleStatement(st, symbolTable);
            symbolTable = popScope(symbolTable);		
		}

        case (StringTemplate)`if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- conds) symbolTable = handleExpression(e, symbolTable);
            for (st <- pre) symbolTable = handleStatement(st, symbolTable);
            list[Tree] ipl = prodFilter(body, 
                             bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- post) symbolTable = handleStatement(st, symbolTable);
            symbolTable = popScope(symbolTable);		
        }

        case (StringTemplate)`if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (e <- conds) symbolTable = handleExpression(e, symbolTable);
            for (st <- preThen) symbolTable = handleStatement(st, symbolTable);
            list[Tree] ipl = prodFilter(bodyThen, 
                             bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- postThen) symbolTable = handleStatement(st, symbolTable);
            for (st <- preElse) symbolTable = handleStatement(st, symbolTable);
            ipl = prodFilter(bodyElse, 
                  bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- postElse) symbolTable = handleStatement(st, symbolTable);
            symbolTable = popScope(symbolTable);		
        }

        case (StringTemplate)`while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            symbolTable = handleExpression(cond, symbolTable);
            for (st <- pre) symbolTable = handleStatement(st, symbolTable);
            list[Tree] ipl = prodFilter(body, 
                             bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- post) symbolTable = handleStatement(st, symbolTable);
            symbolTable = popScope(symbolTable);		
        }

        case (StringTemplate)`do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(s@\loc, symbolTable));
            for (st <- pre) symbolTable = handleStatement(st, symbolTable);
            list[Tree] ipl = prodFilter(body, 
                             bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                if (`<Expression ipee>` := ipe)
                    symbolTable = handleExpression(ipee, symbolTable);
                else if (`<StringTemplate ipet>` := ipe)
                    symbolTable = handleStringTemplate(ipet, symbolTable);
            }
            for (st <- post) symbolTable = handleStatement(st, symbolTable);
            symbolTable = handleExpression(cond, symbolTable);
            symbolTable = popScope(symbolTable);		
        }
    }

    return symbolTable;
}

public SymbolTable handleCase(Case c, SymbolTable symbolTable) {
    switch(c) {
        case (Case)`case <PatternWithAction p>` :
            symbolTable = handlePatternWithAction(p, symbolTable);

        case (Case)`default : <Statement b>` :
            symbolTable = handleStatement(b, symbolTable);
    }

    return symbolTable;
}

public SymbolTable handleAssignable(Assignable a, SymbolTable symbolTable) {
	switch(a) {
		// Name _
		case (Assignable)`_` :
			symbolTable = addFreshAnonymousVariable(a@\loc, symbolTable);
	
		// Assignment to a variable
		case (Assignable)`<QualifiedName qn>` : {
			if (size(getItems(symbolTable, symbolTable.currentScope, convertName(qn), FCVs())) > 0) {		
				symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, convertName(qn), FCVs()), qn@\loc);
			} else {
				symbolTable = addFreshVariable(convertName(qn), qn@\loc, symbolTable);			
			}
		}
		
		// Subscript assignment
		case (Assignable)`<Assignable al> [ <Expression e> ]` :
			symbolTable = handleExpression(e, handleAssignable(al, symbolTable));			

		// Field assignment, since the field name is part of the type, not a declared variable, we don't mark it here
		case (Assignable)`<Assignable al> . <Name n>` :
			symbolTable = handleAssignable(al, symbolTable);
		
		// If-defined assignment
		case (Assignable)`<Assignable al> ? <Expression e>` :
			symbolTable = handleExpression(e, handleAssignable(al, symbolTable));			
		
		// Annotation assignment
		case (Assignable)`<Assignable al> @ <Name n>` : {
			symbolTable = handleAssignable(al, symbolTable);
			symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, convertName(n), Annotations()), n@\loc);
		}

		// Tuple assignable, with just one tuple element		
		case (Assignable)`< <Assignable ai> >` :
			symbolTable = handleAssignable(ai, symbolTable);

		// Tuple assignable, with multiple elements in the tuple
		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
			symbolTable = handleAssignable(ai, symbolTable);
			for (ali <- al) symbolTable = handleAssignable(ali, symbolTable);
		}
		
		default : 
			throw "Found unhandled assignable case during namespace construction: <a>";
	}
	
	return symbolTable;
}

//
// Build symbol table information for local variable declarations. We do allow shadowing of names declared
// outside the function, but we do not allow shadowing inside the function, so our duplicates check is
// function bounded (see SymbolTable for the implementation).
//
public SymbolTable handleLocalVarItems(Type t, {Variable ","}+ vs, SymbolTable symbolTable) {
    for (vb <- vs) {
        if ((Variable)`<Name n>` := vb || (Variable)`<Name n> = <Expression e>` := vb) {
            if (size(getItemsForConflicts(symbolTable, symbolTable.currentScope, convertName(n), FCVs())) > 0) {
                symbolTable = addScopeError(symbolTable, n@\loc, "Illegal redefinition of <n>.");
            } else {
                ConvertTuple ct = convertRascalType(symbolTable, t);
                RType varType = ct.rtype; symbolTable = ct.symbolTable;
                symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(convertName(n), varType, true, vb@\loc, symbolTable), [<true,n@\loc>]));
            } 
        }
		
        if ((Variable)`<Name n> = <Expression e>` := vb) {		
            symbolTable = handleExpression(e, symbolTable);
        }
    }
    return symbolTable;
}

public SymbolTable handleCatch(Catch c, SymbolTable symbolTable) {
    switch(c) {
        case (Catch)`catch : <Statement b>` :
            symbolTable = handleStatement(b, symbolTable);
		
        case (Catch)`catch <Pattern p> : <Statement b>` : {
            symbolTable = justSymbolTable(pushNewBooleanScope(c@\loc, symbolTable));
            symbolTable = handleStatement(b, handlePattern(p, symbolTable));
            symbolTable = popScope(symbolTable);
        }
    }

    return symbolTable;
}		

public SymbolTable handleLabel(Label l, SymbolTable symbolTable) {
    if ((Label)`<Name n> :` := l) {
        // First, check to see if this label already exists
        set[STItemId] ls = getItemsForConflicts(symbolTable, symbolTable.currentScope, convertName(n), Labels());
        if (size(ls) > 0) {
            symbolTable = addScopeError(symbolTable, n@\loc, "Label <n> has already been defined.");
        } else {
            symbolTable = justSymbolTable(addLabelToScope(convertName(n), l@\loc, symbolTable));
        }					
    } 
    return symbolTable;
}

public SymbolTable handleVisit(Visit v, SymbolTable symbolTable) {
	if ((Visit)`visit (<Expression se>) { <Case+ cs> }` := v || (Visit)`<Strategy st> visit (<Expression se>) { <Case+ cs> }` := v) {
		symbolTable = handleExpression(se, symbolTable);
		for (c <- cs) symbolTable = handleCase(c, symbolTable);
	}
	return symbolTable;
}

public SymbolTable handleMapPattern(Pattern pat, SymbolTable symbolTable) {
    list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
    for (<md,mr> <- mapContents) symbolTable = handlePattern(mr, handlePattern(md, symbolTable));
    return symbolTable;
}

//
// TODO: We don't handle interpolation here. Does it make sense to allow this inside
// either string or location patterns? (for instance, to create the string to match against?)
//
public SymbolTable handlePattern(Pattern pat, SymbolTable symbolTable) {
	SymbolTable handlePatternName(RName n, loc l, SymbolTable symbolTable) {
		if (size(getItems(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {		
			symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, n, FCVs()), l);
		} else {
			symbolTable = addFreshVariable(n, l, symbolTable);
		}
		return symbolTable;
	}
	
	SymbolTable handleListMultiPatternName(RName n, loc l, SymbolTable symbolTable) {
		if (size(getItems(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {		
			symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, n, FCVs()), l);
		} else {
			symbolTable = addFreshListVariable(n, l, symbolTable);
		}
		return symbolTable;
	}
		
    SymbolTable handleSetMultiPatternName(RName n, loc l, SymbolTable symbolTable) {
        if (size(getItems(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {     
            symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, n, FCVs()), l);
        } else {
            symbolTable = addFreshSetVariable(n, l, symbolTable);
        }
        return symbolTable;
    }

	SymbolTable handleTypedPatternName(RName n, RType t, loc l, loc pl, SymbolTable symbolTable) {
		if (size(getItemsForConflicts(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {
			set[STItemId] conflictItems = getItemsForConflicts(symbolTable, symbolTable.currentScope, n, FCVs());
			set[loc] conflictLocations = { symbolTable.scopeItemMap[si]@at | si <- conflictItems, ( (symbolTable.scopeItemMap[si]@at)?) };		
			symbolTable = addScopeError(symbolTable, l, "Illegal shadowing of already declared name <prettyPrintName(n)>; other declarations at <conflictLocations>");
		} else {
			symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, t, false, pl, symbolTable), [<true,l>]));
		}
		
        // Handle any type variables in the type of the parameter IF this is in a function scope
        // (i.e., if this is a parameter declaration)
        if (FunctionLayer(_,_) := symbolTable.scopeItemMap[symbolTable.currentScope]) {
            for(tvv <- collectTypeVars(t)) {
                set[STItemId] tvItems = getItems(symbolTable, symbolTable.currentScope, getTypeVarName(tvv), TypeVars());
                if (size(tvItems) == 0) {
                    symbolTable = justSymbolTable(addTypeVariableToScope(tvv, pl, symbolTable));
                } else {
                   // TODO: We should just have one, check to see if we have more
                   RType tvType = symbolTable.scopeItemMap[getOneFrom(tvItems)].typeVar;
                   if (tvType.varTypeBound != tvv.varTypeBound) {
                        symbolTable = addScopeError(symbolTable, pl, "Illegal redefinition of bound on type variable <prettyPrintName(tvv.varName)> with existing bound <prettyPrintType(tvType.varTypeBound)>.");        
                   }
                }
            }
        }
        
		return symbolTable;
	}	

	switch(pat) {
        // Regular Expression literal
        case (Pattern)`<RegExpLiteral rl>` : {
            list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,sort("Name"),_) := prd; });
            // For each name, either introduce it into scope, or tag the use of an existing name; we can
            // assume that names are of type string, since they will hold parts of strings (but will check
            // this during type checking in case names we don't introduce aren't actually strings)
            for (n <- names) {
                RName rn = RSimpleName("<n>");
                if (size(getItems(symbolTable, symbolTable.currentScope, rn, FCVs())) > 0) {
                    symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, rn, FCVs()), n@\loc);
                } else {
                    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(rn, makeStrType(), false, n@\loc, symbolTable), [<true,n@\loc>]));
                }
            }
        }

        // Name _
        case (Pattern)`_` : {
            symbolTable = addFreshAnonymousVariable(pat@\loc, symbolTable);
        }			

        // Name other than _
        case (Pattern)`<Name n>` : {
            symbolTable = handlePatternName(convertName(n), n@\loc, symbolTable);
        }
		
		// QualifiedName
        case (Pattern)`<QualifiedName qn>` : {
            symbolTable = handlePatternName(convertName(qn), qn@\loc, symbolTable);
        }

        // ReifiedType
        // TODO: How much should we enforce that pl specifies types? Or, should
        // this all be deferred to the checker? (For instance, can this ever
        // be size(pl) > 1?)
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            for (p <- pl) symbolTable = handlePattern(p, symbolTable);
        }

        // CallOrTree
        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            symbolTable = handlePatternConstructorName(p1, symbolTable);
            for (p <- pl) symbolTable = handlePattern(p, symbolTable);
        }

        // List
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            for (p <- pl) {
                if ((Pattern)`_*` := p) {
                    symbolTable = addFreshAnonymousListVariable(pat@\loc, symbolTable);
                } else if ((Pattern)`<QualifiedName qn> *` := p) {
                    symbolTable = handleListMultiPatternName(convertName(qn), qn@\loc, symbolTable);
                } else {
                    symbolTable = handlePattern(p, symbolTable);
                }
            }
        }

        // Set
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            for (p <- pl) {
                if ((Pattern)`_*` := p) {
                    symbolTable = addFreshAnonymousSetVariable(pat@\loc, symbolTable);
                } else if ((Pattern)`<QualifiedName qn> *` := p) {
                    symbolTable = handleSetMultiPatternName(convertName(qn), qn@\loc, symbolTable);
                } else {
                    symbolTable = handlePattern(p, symbolTable);
                }
            }
        }

        // Tuple, with just one element
        case (Pattern) `<<Pattern pi>>` : {
            // println("NAMESPACE: Handling tuple pattern <pat>");
            symbolTable = handlePattern(pi, symbolTable);
        }

        // Tuple, with multiple elements
        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
            // println("NAMESPACE: Handling tuple pattern <pat>");
            symbolTable = handlePattern(pi, symbolTable);
            for (pli <- pl) symbolTable = handlePattern(pli, symbolTable);
        }

        // Typed Variable
        case (Pattern) `<Type t> <Name n>` : {
            // println("NAMESPACE: Handling typed variable pattern <pat>");
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType varType = ct.rtype; symbolTable = ct.symbolTable;
            symbolTable = handleTypedPatternName(convertName(n),varType,n@\loc,pat@\loc,symbolTable);
        }

// TODO: These two should not appear outside of a list or set pattern. Verify
// this, since we are only supporting them above for now!
        // Anonymous Multi Variable
//        case (Pattern) `_ *` : {
//            // println("NAMESPACE: Handling multivariable pattern <pat>");
//            symbolTable = addFreshAnonymousContainerVariable(pat@\loc, symbolTable);
//        }			

        // Multi Variable
//        case (Pattern) `<QualifiedName qn> *` : {
//            // println("NAMESPACE: Handling multivariable pattern <pat>");
//            symbolTable = handleMultiPatternName(convertName(qn), qn@\loc, symbolTable);
//        }

        // Descendant
        case (Pattern) `/ <Pattern p>` : {
            // println("NAMESPACE: Handling descendant pattern <pat>");
            symbolTable = handlePattern(p, symbolTable);
        }

        // Variable Becomes
        case (Pattern) `<Name n> : <Pattern p>` : {
            // println("NAMESPACE: Handling variable becomes pattern <pat>");
            symbolTable = handlePattern(p, handlePatternName(convertName(n), n@\loc, symbolTable));
        }
		
        // Typed Variable Becomes
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            // println("NAMESPACE: Handling typed variable becomes pattern <pat>");
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType varType = ct.rtype; symbolTable = ct.symbolTable;
            symbolTable = handlePattern(p, handleTypedPatternName(convertName(n),varType,n@\loc,pat@\loc,symbolTable));
        }
		
        // Guarded
        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            // println("NAMESPACE: Handling guarded pattern <pat>");
            ConvertTuple ct = convertRascalType(symbolTable, t); // Just to check the type, we don't use it here
            symbolTable = handlePattern(p, ct.symbolTable);
        }
		
        // Anti
        case (Pattern) `! <Pattern p>` : {
            // println("NAMESPACE: Handling anti pattern <pat>");
            symbolTable = handlePattern(p, symbolTable);
        }
	}
	
    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // pat[0] is the production used, pat[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := pat[0]) {
        symbolTable = handleMapPattern(pat, symbolTable);
    }

    return symbolTable;
}

//
// We have separate logic here since we don't allow general patterns to be used for the constructor
// position in a call or tree pattern. These patterns can be 1) constructors, or 2) nodes. We cannot
// use the other two options here, 3) functions, and 4) locations -- these cannot be used in pattern
// matches. For the constructor position, this means that we either need a name or qualified name for
// constructor matches, and either a string literal or a name of some sort (including a typed
// variable name) for node patterns.
//
// TODO: We may be able to just call back to handlePattern here, instead of adding our own
// logic.
//
public SymbolTable handlePatternConstructorName(Pattern pat, SymbolTable symbolTable) {
	SymbolTable handlePatternName(RName n, loc l, SymbolTable symbolTable) {
		if (size(getItems(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {
            // TODO: Should we check here to verify that we have an actual constructor name? Or just let
            // this go until type checking? Probably the latter, we have more info then...
            symbolTable = addItemUses(symbolTable, getItems(symbolTable, symbolTable.currentScope, n, FCVs()), l);
		} else {
			symbolTable = addScopeError(symbolTable, l, "Constructor name <prettyPrintName(n)> must be declared, in scope <symbolTable.currentScope>");
		}
		return symbolTable;
	}
	
    SymbolTable handleTypedPatternName(RName n, RType t, loc l, loc pl, SymbolTable symbolTable) {
        if (size(getItemsForConflicts(symbolTable, symbolTable.currentScope, n, FCVs())) > 0) {
            set[STItemId] conflictItems = getItemsForConflicts(symbolTable, symbolTable.currentScope, n, FCVs());
            set[loc] conflictLocations = { symbolTable.scopeItemMap[si]@at | si <- conflictItems, ( (symbolTable.scopeItemMap[si]@at)?) };      
            symbolTable = addScopeError(symbolTable, l, "Illegal shadowing of already declared name <prettyPrintName(n)>; other declarations at <conflictLocations>");
        } else {
            symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, t, false, pl, symbolTable), [<true,l>]));
        }
        return symbolTable;
    }   
	
	switch(pat) {
        case (Pattern)`_` :
            symbolTable = addScopeError(symbolTable, pat@\loc, "Illegal pattern for constructor or node name");

		case (Pattern)`<Name n>` :
			symbolTable = handlePatternName(convertName(n), n@\loc, symbolTable);
		
		case (Pattern)`<QualifiedName qn>` :
			symbolTable = handlePatternName(convertName(qn), qn@\loc, symbolTable);

		case (Pattern)`<StringLiteral sl>` :
		        symbolTable = symbolTable; // no-op, we get no new names, just avoid the default below

        // Typed Variable
        // TODO: We need to make sure the type checker has logic to 1) allow this case, and 2) verify the type
        // is string.
        case (Pattern) `<Type t> <Name n>` : {
            ConvertTuple ct = convertRascalType(symbolTable, t);
            RType varType = ct.rtype; symbolTable = ct.symbolTable;
            symbolTable = handleTypedPatternName(convertName(n),varType,n@\loc,pat@\loc,symbolTable);
        }

		default :
			symbolTable = addScopeError(symbolTable, pat@\loc, "Illegal pattern for constructor or node name");
	}
	
	return symbolTable;
}

//
// Extract scope information from PatternWithAction nodes. Note that this opens a
// new scope, since we can bind variables in the pattern which should then be available
// in the right hand side expression(s) and/or statements. The new scope is closed (popped)
// on the way out of the function.
//
public SymbolTable handlePatternWithAction(PatternWithAction pwa, SymbolTable symbolTable) {
    symbolTable = justSymbolTable(pushNewPatternMatchScope(pwa@\loc, symbolTable));

	switch(pwa) {
		case (PatternWithAction)`<Pattern p> => <Expression e>` :
			symbolTable = handleExpression(e, handlePattern(p, symbolTable));
		
		case (PatternWithAction)`<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			symbolTable = handlePattern(p, symbolTable);
			for (e <- es) symbolTable = handleExpression(e, symbolTable);
			symbolTable = handleExpression(er, symbolTable);
		}
		
		case (PatternWithAction)`<Pattern p> : <Statement s>` :
			symbolTable = handleStatement(s, handlePattern(p, symbolTable));			
		
        default : throw "Unexpected Pattern With Action syntax, <pwa>";
	}
	
	return popScope(symbolTable);
}

public SymbolTable handleDataTarget(DataTarget dt, SymbolTable symbolTable) {
	if ((DataTarget)`<Name n> :` := dt) {
		set[STItemId] items = getItems(symbolTable, symbolTable.currentScope, convertName(n), Labels());
		if (size(items) == 1) {
			symbolTable = addItemUses(symbolTable, items, n@\loc);
		} else if (size(items) == 0) {
			symbolTable = addScopeError(symbolTable, n@\loc, "Label <n> has not been defined.");			
		} else {
			symbolTable = addScopeError(symbolTable, n@\loc, "Label <n> has multiple definitions.");
		}
	}
	return symbolTable;
}

public SymbolTable handleTarget(Target t, SymbolTable symbolTable) {
	if ((Target)`<Name n>` := t) {
		set[STItemId] items = getItems(symbolTable, symbolTable.currentScope, convertName(n), Labels());
		if (size(items) == 1) {
			symbolTable = addItemUses(symbolTable, items, n@\loc);
		} else if (size(items) == 0) {
			symbolTable = addScopeError(symbolTable, n@\loc, "Label <n> has not been defined.");			
		} else {
			symbolTable = addScopeError(symbolTable, n@\loc, "Label <n> has multiple definitions.");
		}
	}
	return symbolTable;
}

// TODO: Add tag handling here
public SymbolTable handleTagsNamesOnly(Tags ts, SymbolTable symbolTable) {
	return symbolTable;
}

// TODO: Add tag handling here
public SymbolTable handleTags(Tags ts, SymbolTable symbolTable) {
	return symbolTable;
}


//
// Routines to add inference vars of various types
//

public SymbolTable addFreshVariable(RName n, loc nloc, SymbolTable symbolTable) {
    RType freshType = makeInferredType(symbolTable.freshType);
    symbolTable.inferredTypeMap[symbolTable.freshType] = freshType;
    if (RSimpleName("it") := n) symbolTable.itBinder[nloc] = freshType;
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshAnonymousVariable(loc nloc, SymbolTable symbolTable) {
    RType freshType = makeInferredType(symbolTable.freshType);
    symbolTable.inferredTypeMap[symbolTable.freshType] = freshType;
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(RSimpleName("_"), freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshListVariable(RName n, loc nloc, SymbolTable symbolTable) {
    RType freshType = makeListType(makeInferredType(symbolTable.freshType));
    symbolTable.inferredTypeMap[symbolTable.freshType] = getListElementType(freshType);
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshSetVariable(RName n, loc nloc, SymbolTable symbolTable) {
    RType freshType = makeSetType(makeInferredType(symbolTable.freshType));
    symbolTable.inferredTypeMap[symbolTable.freshType] = getSetElementType(freshType);
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshAnonymousListVariable(loc nloc, SymbolTable symbolTable) {
    RType freshType = makeListType(makeInferredType(symbolTable.freshType));
    symbolTable.inferredTypeMap[symbolTable.freshType] = getListElementType(freshType);
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(RSimpleName("_"), freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshAnonymousSetVariable(loc nloc, SymbolTable symbolTable) {
    RType freshType = makeSetType(makeInferredType(symbolTable.freshType));
    symbolTable.inferredTypeMap[symbolTable.freshType] = getSetElementType(freshType);
    symbolTable.freshType = symbolTable.freshType + 1;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(RSimpleName("_"), freshType, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshVariableWithType(RName n, loc nloc, RType rt, SymbolTable symbolTable) {
    if (RSimpleName("it") := n) symbolTable.itBinder[nloc] = rt;
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(n, rt, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}

public SymbolTable addFreshAnonymousVariableWithType(loc nloc, RType rt, SymbolTable symbolTable) {
    symbolTable = justSymbolTable(addSTItemUses(addVariableToScope(RSimpleName("_"), rt, false, nloc, symbolTable), [<true,nloc>]));
    return symbolTable;
}
