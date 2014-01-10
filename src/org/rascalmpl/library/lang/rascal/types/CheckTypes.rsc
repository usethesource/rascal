@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::CheckTypes

import List;
import analysis::graphs::Graph;
import IO;
import Set;
import Map;
import ParseTree;
import Message;
import Node;
import Type;
import Relation;
import util::Reflective;
import DateTime;
import String;

import lang::rascal::checker::ListUtils;
import lang::rascal::checker::TreeUtils;
import lang::rascal::types::AbstractKind;
import lang::rascal::types::AbstractName;
import lang::rascal::types::AbstractType;
import lang::rascal::types::ConvertType;
import lang::rascal::types::TypeSignature;
import lang::rascal::types::TypeInstantiation;
import lang::rascal::checker::ParserHelper;
import lang::rascal::grammar::definition::Symbols;

import lang::rascal::\syntax::Rascal;

//
// TODOs
// 1. Add scoping information for boolean operations (and, or, etc) and surrounding
//    constructs (comprehensions, matches, conditionals, etc) DONE
//
// 1a. Make sure that names propagate correctly with boolean operators. For instance,
//     in a boolean or, a name needs to occur along both branches for it to be
//     visible outside the or, but for and the name can occur along only one
//     branch (since both need to be true, meaning both have been processed).
//
// 2. Add support for _ in subscripts DONE: This is only allowed in subscripts
//    in expressions, not in assignables.
//
// 3. Add support for _ in patterns, to ensure we don't accidentally bind _ as a name DONE
//
// 4. Filter out bad assignables? For instance, we cannot have <x,y>.f, but it does
//    parse, so we may be handed an example such as this.
//
// 5. Expand out uses of user types, including those with parameters DONE
//
// 6. Check tags to make sure they are declared
//
// 7. Make sure add errors are encoded as exceptions. This way we don't need
//    to always encode checks to see if we can add the given item, we can just
//    try and catch any errors. REMOVED: We now generate scope errors in the add
//    routines, which allows us to handle the add logic (including whether add
//    is allowed in some cases) in one place. Throws are too disruptive, since
//    they can stop the checker from running if not handled properly, while
//    registering errors provides a better UI experience.
//
// 8. Make sure that, when we have a declaration (even one with a type), we
//    check shadowing! This is done in statements, but is not always done
//    now in patterns. DONE: This is handled by now having the logic to mark
//    scope errors in the functions used to add variables.
//
// 9. Make sure that varargs parameters are properly given list types. Also make sure
//    they are just names (not varargs of a tuple pattern, for instance) DONE
//
// 10. Make sure we always instantiate type parameters when we use a constructor. NOTE: This
//     is partially done -- it has been done for call or tree expressions, but not yet for
//     call or tree patterns.
//
// 11. For ADT types, the user type could have other types for bounds which have
//     not yet been introduced (although this would be odd), so we need to
//     ignore those when we are not descending
//
// 12. Remember to pop the function when we exit!!! DONE: We already were doing this
//     by just resetting to the old stack.
//
// 13. Check values created by append NOTE: This is partially done, in that we are gathering
//     the types. Additional checking may still be needed.
//
// 14. Instantiate types in parametric function calls. DONE.
//
// 15. Make sure type var bounds are consistent.
//
// 16. Change shadowing rules to allow shadowing of imported items by items
//     in the current module. Note that functions do not shadow, but are
//     instead added into the overloaded cases. DONE.
//
// 17. Support _ in assignables DONE
//
// 18. Provide support for insert DONE
//
// 19. Refactor out code in statements with labels. DONE
//
// 20. Ensure environments are proper for nested functions and for closures.
//     For instance, we should not see labels from outside a closure while
//     we are inside the closure (especially since we could pass or return
//     the closure!) DONE
//
// 21. Ensure that inferred types are handled appropriately in situations
//     where we have control flow iteration: loops, visits, calls, solve
//
// 22. Ensure field names are propagated correctly in subscripting and projection
//     operations.
//
// 23. Typing for loops -- should this always be void? We never know if the loop actually evaluates.
//
// 24. Remember to keep track of throws declarations, right now these are just discarded.
//     PARTIALLY DONE: We are now keeping track of these, but still need to check
//     them to enforce they are constructors for runtime exception.
//
// 25. Need to account for default functions and constructors which have the same signature,
//     this is allowed, and we give precedence to the function DONE
//
// 25a. We should mark a function with the same signature and name as a constructor but with
//      a different return type as an error. This is currently being done at the point of use,
//      but should be done at the point of definition.
//
// 26. Need to pre-populate the type and name environments with the constructors for
//     reified types, since we get these back if someone uses #type.
//
// 27. Finish the binders for assignables, we still need to modify these for more complex
//     binders that push information back through multiple levels of field accesses, etc
//     DONE
//
// 28. In statement blocks, segregate out function defs, these see the scope of the
//     entire block, not just what came before; however, closures are still just
//     expressions, so we still need the ability to capture an environment for them
//     and smartly check for changes to inferred types
//
// 29. Make sure that, in a function, all paths return.
//
// 30. Make sure we don't allow changes to the types of variables bound in pattern matches.
//     These do not follow the same rules as other inferred vars.
//
// 31. addition on functions
//
@doc{The source of a label (visit, block, etc).}
data LabelSource = visitLabel() | blockLabel() | forLabel() | whileLabel() | doWhileLabel() | ifLabel() | switchLabel() | caseLabel() | functionLabel() ;

@doc{Function modifiers.}
data Modifier = javaModifier() | testModifier() | defaultModifier();

@doc{Convert from the concrete to the abstract representation of modifiers.}
Modifier getModifier(FunctionModifier fmod:(FunctionModifier)`java`) = javaModifier();
Modifier getModifier(FunctionModifier fmod:(FunctionModifier)`test`) = testModifier();
Modifier getModifier(FunctionModifier fmod:(FunctionModifier)`default`) = defaultModifier();

@doc{Visibility of declarations.}
data Vis = publicVis() | privateVis() | defaultVis();

@doc{Convert from the concrete to the abstract representation of visibilities.}
Vis getVis(Visibility v:(Visibility)`private`) = privateVis();
Vis getVis(Visibility v:(Visibility)`public`) = publicVis();
default Vis getVis(Visibility v) = defaultVis();

alias KeywordParamMap = map[RName kpName, Symbol kpType];
alias KeywordParamRel = lrel[RName pname, Symbol ptype, Expression pinit];

@doc{Abstract values manipulated by the semantics. We include special scopes here as well, such as the
     scopes used for blocks and boolean expressions.}
data AbstractValue 
    = label(RName name, LabelSource source, int containedIn, loc at) 
    | variable(RName name, Symbol rtype, bool inferred, int containedIn, loc at)
    | function(RName name, Symbol rtype, KeywordParamMap keywordParams, bool isVarArgs, int containedIn, list[Symbol] throwsTypes, loc at)
    | closure(Symbol rtype, KeywordParamMap keywordParams, int containedIn, loc at)
    | \module(RName name, loc at)
    | overload(set[int] items, Symbol rtype)
    | datatype(RName name, Symbol rtype, int containedIn, set[loc] ats)
    | sorttype(RName name, Symbol rtype, int containedIn, set[loc] ats)
    | constructor(RName name, Symbol rtype, KeywordParamMap keywordParams, int containedIn, loc at)
    | production(RName name, Symbol rtype, int containedIn, loc at)
    | annotation(RName name, Symbol rtype, set[Symbol] onTypes, int containedIn, loc at)
    | \tag(RName name, TagKind tkind, set[Symbol] onTypes, int containedIn, loc at)
    | \alias(RName name, Symbol rtype, int containedIn, loc at)
    | booleanScope(int containedIn, loc at)
    | blockScope(int containedIn, loc at)
    | conflict(set[int] items) 
    ;

data LabelStackItem = labelStackItem(RName labelName, LabelSource labelSource, Symbol labelType);
data Timing = timing(str tmsg,datetime tstart,datetime tend);

@doc{Configurations provide the state used during evaluation.}
data Configuration = config(set[Message] messages, 
                            map[loc,Symbol] locationTypes, 
                            Symbol expectedReturnType, 
                            map[RName,int] labelEnv,
                            map[RName,int] fcvEnv,
                            map[RName,int] typeEnv,
                            map[RName,int] modEnv,
                            map[RName,int] annotationEnv,
                            map[RName,int] tagEnv,
                            map[int,Vis] visibilities,
                            map[int,AbstractValue] store,
                            map[int,Production] grammar,
                            set[int] starts,
                            map[tuple[int,str],Symbol] adtFields,
                            map[tuple[int,str],Symbol] nonterminalFields,
                            rel[int,Modifier] functionModifiers,
                            rel[int,loc] definitions,
                            rel[int,loc] uses,
                            map[loc,int] usedIn,
                            rel[int,int] adtConstructors,
                            rel[int,int] nonterminalConstructors,
                            list[int] stack,
                            list[LabelStackItem] labelStack,
                            list[Timing] timings,
                            int nextLoc,
                            int uniqueify,
                            map[int,Expression] keywordDefaults,
                            rel[int,RName,Expression] dataKeywordDefaults
                           );

public Configuration newConfiguration() = config({},(),\void(),(),(),(),(),(),(),(),(),(),{},(),(),{},{},{},(),{},{},[],[],[],0,0,(),{ });

public Configuration pushTiming(Configuration c, str m, datetime s, datetime e) = c[timings = c.timings + timing(m,s,e)];

public set[&T] filterSet(set[&T] xs, bool (&T) f) = { x | x <- xs, f(x) };

@doc{Add a new location type.}
public CheckResult markLocationType(Configuration c, loc l, Symbol t) {
    if (isFailType(t)) return markLocationFailed(c, l, t);
    c.locationTypes[l] = t;
    return < c, t >;
}

@doc{Mark that a location has type fail.}
public CheckResult markLocationFailed(Configuration c, loc l, set[Symbol] ts) {
    res = collapseFailTypes(ts);
    c.locationTypes[l] = res;
    c.messages += getFailures(res);
    return < c, res >;
}

public CheckResult markLocationFailed(Configuration c, loc l, Symbol t) = markLocationFailed(c,l,{t});

public Configuration recoverEnvironments(Configuration cNew, Configuration cOld) {
    cNew.labelEnv = cOld.labelEnv;
    cNew.fcvEnv = cOld.fcvEnv;
    cNew.typeEnv = cOld.typeEnv;
    cNew.modEnv = cOld.modEnv;
    cNew.annotationEnv = cOld.annotationEnv;
    cNew.tagEnv = cOld.tagEnv;
    return cNew;
}

public Configuration recoverEnvironmentsAfterCall(Configuration cNew, Configuration cOld) {
    cNew = recoverEnvironments(cNew,cOld);
    cNew.expectedReturnType = cOld.expectedReturnType;
    cNew.stack = cOld.stack;
    cNew.labelStack = cOld.labelStack;
    return cNew;
}

public Configuration setExpectedReturn(Configuration c, Symbol t) {
    return c[expectedReturnType = t];
}

public bool labelExists(Configuration c, RName n) = n in c.labelEnv;

public Configuration addLabel(Configuration c, RName n, loc l, LabelSource ls) {
    c.labelEnv[n] = c.nextLoc;
    c.store[c.nextLoc] = label(n,ls,head(c.stack),l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

public bool fcvExists(Configuration c, RName n) = n in c.fcvEnv;

public int definingContainer(Configuration c, int i) {
	if (c.store[i] is overload) return definingContainer(c, getOneFrom(c.store[i].items));
    cid = c.store[i].containedIn;
    if (\module(_,_) := c.store[cid]) return cid;
    if (\function(_,_,_,_,_,_,_) := c.store[cid]) return cid;
    if (\closure(_,_,_,_) := c.store[cid]) return cid;
    return definingContainer(c,cid);
}

public list[int] upToContainer(Configuration c, int i) {
    if (\module(_,_) := c.store[i]) return [i];
    if (\function(_,_,_,_,_,_,_) := c.store[i]) return [i];
    if (\closure(_,_,_,_) := c.store[i]) return [i];
    return [i] + upToContainer(c,c.store[i].containedIn);
}

private int getContainedIn(Configuration c, AbstractValue av) {
	if (av has containedIn) return av.containedIn;
	// NOTE: This assumes that overloads are all defined at the same level (all at the top
	// of a module, for instance), or this won't work properly at the calling site.
	if (av is overload) return getContainedIn(c, c.store[getOneFrom(av.items)]);
	return head(c.stack);
}

public Configuration addVariable(Configuration c, RName n, bool inf, loc l, Symbol rt) {
    moduleName = head([m | i <- c.stack, m:\module(_,_) := c.store[i]]).name;
    moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
    atRootOfModule = \module(_,_) := c.store[head(c.stack)];
    if (n notin c.fcvEnv) {
    	// Case 1: This is the first appearance of the name. If we are at the module global
    	// level, we also add a module-qualified version of the name into the environment.
        c.fcvEnv[n] = c.nextLoc;
        if (atRootOfModule) c.fcvEnv[appendName(moduleName,n)] = c.nextLoc;
        c.store[c.nextLoc] = variable(n,rt,inf,head(c.stack),l);
        c.definitions = c.definitions + < c.nextLoc, l >;
        c.nextLoc = c.nextLoc + 1;
    } else {
        if (atRootOfModule && \module(_,_) := c.store[getContainedIn(c,c.store[c.fcvEnv[n]])] && getContainedIn(c,c.store[c.fcvEnv[n]]) != moduleId) {
            // Case 2: We are adding a new global name, and this name has already been defined inside another module that
            // we are importing. This is allowed, but we issue an informational message since it may be accidental.
            c.fcvEnv[n] = c.nextLoc;
            c.fcvEnv[appendName(moduleName,n)] = c.nextLoc;
            c.store[c.nextLoc] = variable(n,rt,inf,head(c.stack),l);
            c.definitions = c.definitions + < c.nextLoc, l >;
            c.nextLoc = c.nextLoc + 1;
            c = addScopeInfo(c, "Declaration of variable <prettyPrintName(n)> shadows an imported name", l);
        } else if (atRootOfModule && \module(_,_) := c.store[getContainedIn(c,c.store[c.fcvEnv[n]])] && getContainedIn(c,c.store[c.fcvEnv[n]]) == moduleId) {
            // Case 3: We are adding a new global name, but this name has already been declared inside this module.
            // This is a scope error, so issue an error message and don't add the new variable.
            c = addScopeError(c, "Cannot re-declare global name: <prettyPrintName(n)>", l);
            c.uses = c.uses + < c.fcvEnv[n], l >;
            c.usedIn[l] = head(c.stack);
        } else {
            containingScopes = upToContainer(c,head(c.stack));
            conflictIds = (overload(ids,_) := c.store[c.fcvEnv[n]]) ? ids : { c.fcvEnv[n] };
            containingIds = { definingContainer(c,i) | i <- conflictIds };
            if (size(toSet(containingScopes) & containingIds) > 0) {
            	// Case 4: We are adding a new local name, but it is already declared inside this function or
            	// closure. We do not allow redeclarations of names inside a function, so issue an error
            	// message and don't add the new variable.
                c = addScopeError(c, "Cannot re-declare name that is already declared in the current function or closure: <prettyPrintName(n)>", l);
                c.uses = c.uses + < c.fcvEnv[n], l >;
                c.usedIn[l] = head(c.stack);
            } else {
            	// Case 5: We are adding a new local name which will shadow an existing declaration of the
            	// name. This is allowed, so we add the new variable declaration here.
                c.fcvEnv[n] = c.nextLoc;
                c.store[c.nextLoc] = variable(n,rt,inf,head(c.stack),l);
                c.definitions = c.definitions + < c.nextLoc, l >;
                c.nextLoc = c.nextLoc + 1;
            }
        }
    }
    return c;
}

public Configuration addUnnamedVariable(Configuration c, loc l, Symbol rt) {
	// We can always add unnamed variables, and they are always distinct, so here we just add
	// it into the store without all the checking done for a normal variable addition.
    c.store[c.nextLoc] = variable(RSimpleName("_"),rt,true,head(c.stack),l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

public Configuration addVariable(Configuration c, RName n, bool inf, Vis visibility, loc l, Symbol rt) {
	// Here we are adding a variable with a visibility marker (like a global that is public or
	// private). Since addVariable may not actually add a new variable item, this checks to see if
	// the nextLoc was increased by 1, which would happen if a new variable is added. If so, add
	// the visibility info for the variable. If not, don't -- we don't want to tag whatever happened
	// to be added last with erroneous visibility information.
	expectedLoc = c.nextLoc + 1;
    c = addVariable(c,n,inf,l,rt);
    if (c.nextLoc == expectedLoc)
    	c.visibilities[expectedLoc-1] = visibility;
    return c;
}

public Configuration addAnnotation(Configuration c, RName n, Symbol rt, Symbol rtOn, Vis visibility, loc l) {
	// Add an annotation. We track the type of the annotation and the type being annotated. We enforce that
	// all annotations of the same name must be declared to be of an equivalent type (e.g., an annotation
	// of type int and an annotation of myint, an alias of int, would be allowed). There is no way to
	// qualify annotation names.
	// TODO: We currently always treat annotation declarations as public, so we just
	// ignore the visibility here. If we decide to allow private annotation declarations,
	// revisit this.
	if (n notin c.annotationEnv) {
		c.annotationEnv[n] = c.nextLoc;
		c.store[c.nextLoc] = annotation(n,rt,{rtOn},head([i | i <- c.stack, \module(_,_) := c.store[i]]),l);
		c.definitions = c.definitions + < c.nextLoc, l >;
		c.nextLoc = c.nextLoc + 1;
	} else {
		if (!equivalent(rt,c.store[c.annotationEnv[n]].rtype)){
			c = addScopeError(c, "Annotation <prettyPrintName(n)> has already been declared with type <c.store[c.annotationEnv[n]].rtype>", l);
		}
		// NOTE: Even though this annotation is incorrect, we add the information on the annotated
		// type and the definition site into the configuration. This should help to reduce follow-on
		// errors where attempts are made to use the annotation, although it may trigger type
		// errors instead of missing annotation errors.
		c.store[c.annotationEnv[n]].onTypes = c.store[c.annotationEnv[n]].onTypes + rtOn; 
		c.definitions = c.definitions + < c.annotationEnv[n], l >;
	}
	return c;
}

public Configuration addADT(Configuration c, RName n, Vis visibility, loc l, Symbol rt) {
	// TODO: We currently always treat datatype declarations as public, so we just
	// ignore the visibility here. If we decide to allow private datatype declarations,
	// revisit this.
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);
	
	int addDataType() {
		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = datatype(n,rt,moduleId,{ l });
		c.definitions = c.definitions + < itemId, l >;
		return itemId;
	}

	Configuration extendDataType(Configuration c, int existingId) {
		c.store[existingId].ats = c.store[existingId].ats + l;
		c.typeEnv[fullName] = existingId;
		c.definitions = c.definitions + < existingId, l >;		
		return c; 	
	}
		    
	if (n notin c.typeEnv) {
		// Case 1: No type of this name already exists. Add a new data type item, and link it in
		// using both the qualified and unqualified names.
		itemId = addDataType();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (\datatype(_,_,_,_) := c.store[c.typeEnv[n]]) {
		// Case 2: A datatype of this name already exists. Use this existing data type item, adding
		// a link to it using the qualified name. NOTE: This means that the same type may be available
		// using multiple qualified names, but all will point to the same instance.
		existingId = c.typeEnv[n];
		c = extendDataType(c, existingId);
	} else if ((c.store[c.typeEnv[n]] is sorttype || c.store[c.typeEnv[n]] is \alias) && c.store[c.typeEnv[n]].containedIn != moduleId) {
		// Case 3: A sort or alias already exists with the given name, imported from a different
		// module. If this ADT is being added to the main module (the one we are actually checking),
		// this takes precedence over the others. If not, we require that all the types be accessed
		// just with qualified names, which we track by adding in a conflict item holding the IDs of
		// the items that cause the conflict.
		itemId = addDataType();
		c.typeEnv[fullName] = itemId;
		if (moduleId == mainModuleId) {
			c.typeEnv[n] = itemId;
		} else {
			c.store[c.nextLoc] = conflict({c.typeEnv[n], itemId});
			c.typeEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		}
		c = addScopeInfo(c, "The definition of type <prettyPrintName(n)> masks an existing imported nonterminal or alias definition", l);
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId notin { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 4: The unqualified name was removed because of a name conflict. We may be adding a new
		// item to the conflict set, or this may be a valid item for an unqualified name if we are adding
		// the name to the module being checked. NOTE: We check specially for data types in the conflict set;
		// if one exists, we can extend it instead of adding a new one.
		dtids = { itemid | itemid <- c.store[c.typeEnv[n]].items, c.store[itemid] is datatype };
		if (size(dtids) == 0) {				
			itemId = addDataType();
			c.typeEnv[fullName] = itemId;
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = itemId;
			} else {
				c.store[c.typeEnv[n]].items += itemId;
			}
		} else {
			existingId = getOneFrom(dtids);
			c = extendDataType(c, existingId);
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = existingId;
			}
		}
	} else if ((c.store[c.typeEnv[n]] is sorttype || c.store[c.typeEnv[n]] is \alias) && c.store[c.typeEnv[n]].containedIn == moduleId) {
		// Case 5: A sort or alias with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them.
		c = addScoreError(c, "An alias or nonterminal named <prettyPrintName(n)> has already been declared in this module", l);
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId in { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 6: We have a conflict item which contains at least one item declared in the current module. If this is a datatype,
		// we extend it, else this is an error just like in Case 5.
		dtids = { itemid | itemid <- c.store[c.typeEnv[n]].items, c.store[itemid] is datatype, c.store[itemid].containedIn == moduleId };
		if (size(dtids) == 0) {				
			c = addScoreError(c, "An alias or nonterminal named <prettyPrintName(n)> has already been declared in this module", l);
		} else {
			existingId = getOneFrom(dtids);
			c = extendDataType(c, existingId);
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = existingId;
			}
		}
	}
	
	return c;
}

public Configuration addNonterminal(Configuration c, RName n, loc l, Symbol sort) {
	// TODO: We currently always treat nonterminal declarations as public, so we just
	// ignore the visibility here. If we decide to allow private nonterminal declarations,
	// revisit this.
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);
	
	int addNonTerminal() {
		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = sorttype(n,sort,moduleId,{ l });
		c.definitions = c.definitions + < itemId, l >;
		return itemId;
	}

	Configuration extendNonTerminal(Configuration c, int existingId) {
		c.store[existingId].ats = c.store[existingId].ats + l;
		c.typeEnv[fullName] = existingId;
		c.definitions = c.definitions + < existingId, l >;
		
		return c; 	
	}
		    
	if (n notin c.typeEnv) {
		// Case 1: No type of this name already exists. Add a new nonterminal item, and link it in
		// using both the qualified and unqualified names.
		itemId = addNonTerminal();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (\sorttype(_,_,_,_) := c.store[c.typeEnv[n]]) {
		// Case 2: A nonterminal of this name already exists. Use this existing nonterminal item, adding
		// a link to it using the qualified name. NOTE: This means that the same type may be available
		// using multiple qualified names, but all will point to the same instance.
		existingId = c.typeEnv[n];
		c = extendNonTerminal(c, existingId);
	} else if ((c.store[c.typeEnv[n]] is datatype || c.store[c.typeEnv[n]] is \alias) && c.store[c.typeEnv[n]].containedIn != moduleId) {
		// Case 3: A adt or alias already exists with the given name, imported from a different
		// module. If this nonterminal is being added to the main module (the one we are actually checking),
		// this takes precedence over the others. If not, we require that all the types be accessed
		// just with qualified names, which we track by adding in a conflict item holding the IDs of
		// the items that cause the conflict.
		itemId = addNonTerminal();
		c.typeEnv[fullName] = itemId;
		if (moduleId == mainModuleId) {
			c.typeEnv[n] = itemId;
		} else {
			c.store[c.nextLoc] = conflict({c.typeEnv[n], itemId});
			c.typeEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		}
		c = addScopeInfo(c, "The definition of nonterminal <prettyPrintName(n)> masks an existing imported adt or alias definition", l);
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId notin { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 4: The unqualified name was removed because of a name conflict. We may be adding a new
		// item to the conflict set, or this may be a valid item for an unqualified name if we are adding
		// the name to the module being checked. NOTE: We check specially for nonterminals in the conflict set;
		// if one exists, we can extend it instead of adding a new one.
		dtids = { itemid | itemid <- c.store[c.typeEnv[n]].items, c.store[itemid] is sorttype };
		if (size(dtids) == 0) {				
			itemId = addNonTerminal();
			c.typeEnv[fullName] = itemId;
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = itemId;
			} else {
				c.store[c.typeEnv[n]].items += itemId;
			}
		} else {
			existingId = getOneFrom(dtids);
			c = extendNonTerminal(c, existingId);
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = existingId;
			}
		}
	} else if ((c.store[c.typeEnv[n]] is datatype || c.store[c.typeEnv[n]] is \alias) && c.store[c.typeEnv[n]].containedIn == moduleId) {
		// Case 5: A adt or alias with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them.
		c = addScoreError(c, "An alias or adt named <prettyPrintName(n)> has already been declared in this module", l);
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId in { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 6: We have a conflict item which contains at least one item declared in the current module. If this is a datatype,
		// we extend it, else this is an error just like in Case 5.
		dtids = { itemid | itemid <- c.store[c.typeEnv[n]].items, c.store[itemid] is sorttype, c.store[itemid].containedIn == moduleId };
		if (size(dtids) == 0) {				
			c = addScoreError(c, "An alias or adt named <prettyPrintName(n)> has already been declared in this module", l);
		} else {
			existingId = getOneFrom(dtids);
			c = extendNonTerminal(c, existingId);
			if (moduleId == mainModuleId) {
				c.typeEnv[n] = existingId;
			}
		}
	}
	
	return c;
}

public Configuration addAlias(Configuration c, RName n, Vis vis, loc l, Symbol rt) {
	// TODO: We currently always treat alias declarations as public, so we just
	// ignore the visibility here. If we decide to allow private alias declarations,
	// revisit this.
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);
	
	int addAlias() {
		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = \alias(n,rt,moduleId,l);
		c.definitions = c.definitions + < itemId, l >;
		return itemId;
	}

	// NOTE: A working assumption of this code is that the names in the main module are
	// processed LAST. If this changes, the code for determining how to use unqualified
	// names in case of conflicts will need to be reworked.
	if (n notin c.typeEnv) {
		// Case 1: No type of this name already exists. Add a new alias item, and link it in
		// using both the qualified and unqualified names.
		itemId = addAlias();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (c.store[c.typeEnv[n]].containedIn != moduleId) {
		// Case 2: A type already exists with the given name, imported from a different module.
		// If this alias is being added to the main module (the one we are actually checking),
		// the unqualified version of the name will point to this. If not, we require that all
		// the types be accessed just with qualified names, which we track by adding in a conflict
		// item holding the IDs of the items that cause the conflict.
		itemId = addAlias();
		c.typeEnv[fullName] = itemId;
		if (moduleId == mainModuleId) {
			c.typeEnv[n] = itemId;
		} else {
			c.store[c.nextLoc] = conflict({c.typeEnv[n], itemId});
			c.typeEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		}
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId notin { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 3: The unqualified name was removed because of a name conflict. We may be adding a new
		// item to the conflict set, or this may be a valid item for an unqualified name if we are adding
		// the name to the module being checked. This name does not conflict with another name in the same
		// module.
		itemId = addAlias();
		c.typeEnv[fullName] = itemId;
		if (moduleId == mainModuleId) {
			c.typeEnv[n] = itemId;
		} else {
			c.store[c.typeEnv[n]].items += itemId;
		}
	} else if ((c.store[c.typeEnv[n]] is datatype || c.store[c.typeEnv[n]] is sorttype || c.store[c.typeEnv[n]] is \alias) && c.store[c.typeEnv[n]].containedIn == moduleId) {
		// Case 4: A type with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them. NOTE: We don't even allow this if the repeated definition is also an equivalent alias.
		c = addScoreError(c, "An adt or nonterminal named <prettyPrintName(n)> has already been declared in this module", l);
	} else if (c.store[c.typeEnv[n]] is conflict && moduleId in { c.store[itemid].containedIn | itemid <- c.store[c.typeEnv[n]].items }) {
		// Case 5: We have a conflict item which contains at least one item declared in the current module. This is an error,
		// even if it is another alias.
		c = addScoreError(c, "An adt, alias, or nonterminal named <prettyPrintName(n)> has already been declared in this module", l);
	}
	
	return c;
}

// TODO: Enhance scoping as was done with ADTs, etc to allow for name clashes and qualified names
public Configuration addConstructor(Configuration c, RName n, loc l, Symbol rt, KeywordParamRel commonParams, KeywordParamRel keywordParams) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

    adtName = RSimpleName(rt.\adt.name);
	fullAdtName = appendName(moduleName, adtName);
    if (fullAdtName notin c.typeEnv) {
    	c = addScopeError(c, "Could not add constructor, associated ADT is not in scope", l);
    	return c;
    }
    
    adtId = c.typeEnv[fullAdtName];
	keywordParamMap = ( pn : pt | <pn,pt,_> <- keywordParams);
    
    // Now, process the arguments. This performs several consistency checks, namely:
    // * either all fields must have labels, or none should have labels
    // * labels should not be repeated in the same constructor
    // * labels shared between constructors should have matching types
    args = getConstructorArgumentTypes(rt);
	set[str] seenAlready = { };
    if (size(args) > 0) {
        labeledArgs = [ arg | arg <- args, \label(_,_) := arg ];
        if (size(labeledArgs) > 0) {
            if (size(labeledArgs) != size(args)) {
                c = addScopeError(c,"On constructor definitions, either all fields should be labeled or no fields should be labeled", l);
            } else {
                for (\label(fn,ft) <- args) {
                    if (fn in seenAlready) {
                        c = addScopeError(c,"Field name <fn> cannot be repeated in the same constructor", l);
                    } else {
                        seenAlready = seenAlready + fn;
                        if (<adtId,fn> in c.adtFields) {
                            if (!equivalent(ft, c.adtFields[<adtId,fn>])) {
                                c = addScopeError(c,"Field <fn> already defined as type <prettyPrintType(c.adtFields[<adtId,fn>])> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(ft)>",l);
                            }
                        } else {
                            c.adtFields[<adtId,fn>] = ft;
                        }
                    }
                }
            }
        }
    }

	rel[RName pname, Symbol ptype, Expression pinit] consolidatedParams = { };
	
	set[str] paramsSeen = { };
	for (kp:<pn,pt,pe> <- commonParams) {
		pnAsString = prettyPrintName(pn);
		if (pnAsString in seenAlready) {
			c = addScopeError(c,"Common keyword parameter <pnAsString> has the same name as a regular field in the current constructor", l);
		} else if (pnAsString in paramsSeen) {
			c = addScopeError(c,"Common keyword parameter <pnAsString> occurs more than once in the same ADT definition", l); 
		} else {
			paramsSeen = paramsSeen + pnAsString;
			consolidatedParams += kp;
			if (<adtId,pnAsString> in c.adtFields) {
				if (!equivalent(pt, c.adtFields[<adtId,pnAsString>])) {
					c = addScopeError(c,"Field <pnAsString> already defined as type <prettyPrintType(c.adtFields[<adtId,pnAsString>])> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(pt)>",l);
				}
			} else {
				c.adtFields[<adtId,pnAsString>] = pt;
			}
		}
	}    
	
	paramsSeen = { };
	for (kp:<pn,pt,pe> <- keywordParams) {
		pnAsString = prettyPrintName(pn);
		if (pnAsString in seenAlready) {
			c = addScopeError(c,"Keyword parameter <pnAsString> has the same name as a regular field in the current constructor", l);
		} else if (pnAsString in paramsSeen) {
			c = addScopeError(c,"Keyword parameter <pnAsString> occurs more than once in the same ADT definition", l); 
		} else {
			paramsSeen = paramsSeen + pnAsString;
			consolidatedParams = { kp2 | kp2:<pn2,pt2,pe2> <- consolidatedParams, pn2 != pn } + kp;
			if (<adtId,pnAsString> in c.adtFields) {
				if (!equivalent(pt, c.adtFields[<adtId,pnAsString>])) {
					c = addScopeError(c,"Field <pnAsString> already defined as type <prettyPrintType(c.adtFields[<adtId,pnAsString>])> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(pt)>",l);
				}
			} else {
				c.adtFields[<adtId,pnAsString>] = pt;
			}
		}
	}    
	
    // Add the constructor. This also performs an overlap check if this is not the first
    // constructor with this name to ensure the constructor is distinguishable within
    // the same ADT (we can add the ADT name to distinguish constructors from different
    // ADTs).
    void addConstructorItem(RName n, int constructorItemId) {
	    if (n notin c.fcvEnv) {
	    	// Case 1: This is the first occurrence of this name.
	        c.fcvEnv[n] = constructorItemId;
	    } else if (overload(items,overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
	    	// Case 2: The name is already overloaded. Add this as one more overload.
	    	// TODO: If we are annotating overload items, we need to copy annotations here
            c.store[c.fcvEnv[n]] = overload(items + constructorItemId, overloaded(itemTypes,defaults + rt));
	    } else if (constructor(_,_,_,_,_) := c.store[c.fcvEnv[n]] || function(_,_,_,_,_,_,_) := c.store[c.fcvEnv[n]]) {
            nonDefaults = {};
            defaults = { rt };
            if(isConstructorType(c.store[c.fcvEnv[n]].rtype)) {
            	defaults += c.store[c.fcvEnv[n]].rtype; 
            } else {
            	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
            		defaults += c.store[c.fcvEnv[n]].rtype;
            	} else {
            		nonDefaults += c.store[c.fcvEnv[n]].rtype;
            	}
            }
            c.store[c.nextLoc] = overload({ c.fcvEnv[n], constructorItemId }, overloaded(nonDefaults,defaults));
            c.fcvEnv[n] = c.nextLoc;
            c.nextLoc = c.nextLoc + 1;
	    } else {
	        throw "Invalid addition: cannot add constructor into scope, it clashes with non-constructor variable or function names";
	    }
	}

    existsAlready = size({ i | i <- c.adtConstructors[adtId], c.store[i].at == l}) > 0;
    if (!existsAlready) {
	    nameWithAdt = appendName(adtName,n);
	    nameWithModule = appendName(moduleName,n);
    
	    constructorItemId = c.nextLoc;
	    c.nextLoc = c.nextLoc + 1;

        overlaps = { i | i <- c.adtConstructors[adtId], c.store[i].name == n, comparable(c.store[i].rtype,rt)}; //, !equivalent(c.store[i].rtype,rt)};
        if (size(overlaps) > 0)
            c = addScopeError(c,"Constructor overlaps existing constructors in the same datatype : <constructorItemId>, <overlaps>",l);

	    constructorItem = constructor(n,rt,keywordParamMap,head([i | i <- c.stack, \module(_,_) := c.store[i]]),l);
	    c.store[constructorItemId] = constructorItem;
	    c.definitions = c.definitions + < constructorItemId, l >;
	    c.adtConstructors = c.adtConstructors + < adtId, constructorItemId >;
	    
	    c.dataKeywordDefaults = c.dataKeywordDefaults + { < constructorItemId, cp, pe > | <cp,pt,pe> <- consolidatedParams };
	    
	    addConstructorItem(n, constructorItemId);
	    addConstructorItem(nameWithAdt, constructorItemId);
	    addConstructorItem(nameWithModule, constructorItemId);
	}    
	
    return c;
}

// TODO: We need to catch the case where we are trying to create productions and constructors
// with the same name, we should only allow this if the name is qualified to prevent conflicts.
public Configuration addProduction(Configuration c, RName n, loc l, Production prod) {
	assert ( (prod.def is label && prod.def.symbol has name) 
				|| ( !(prod.def is label) && prod.def has name ) || prod.def is \start);
     
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

    sortId = -1;
    sortName = RSimpleName("");
    
    if (unwrapType(prod.def) is \start) {
      sortName = RSimpleName("start[<getNonTerminalName(prod.def)>]");
      sortId = c.typeEnv[sortName];
      fullSortName = sortName;
    }
    else {
      sortName = RSimpleName( (prod.def is label) ? prod.def.symbol.name : prod.def.name );
	  fullSortName = appendName(moduleName, sortName);
      if (fullSortName notin c.typeEnv) {
    	c = addScopeError(c, "Could not add production, associated nonterminal is not in scope", l);
    	return c;
      }
      sortId = c.typeEnv[fullSortName];
    }
    
    
    args = prod.symbols;
    moduleName = head([m | i <- c.stack, m:\module(_,_) := c.store[i]]).name;
    // TODO: think about production overload when we start to create ability to construct concrete trees from abstract names
    Symbol rtype = Symbol::\prod( (prod.def is label) ? prod.def.symbol : prod.def, getSimpleName(n), prod.symbols, prod.attributes );
    
    void addProductionItem(RName n, int productionItemId) {
	    if (n notin c.fcvEnv) {
	    	// Case 1: This is the first occurrence of this name.
	        c.fcvEnv[n] = productionItemId;
	    } else if (overload(items,overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
	    	// Case 2: The name is already overloaded. Add this as one more overload.
	    	// TODO: If we are annotating overload items, we need to copy annotations here
            c.store[c.fcvEnv[n]] = overload(items + productionItemId, overloaded(itemTypes,defaults + rtype));
	    } else if (production(_,_,_,_) := c.store[c.fcvEnv[n]] || function(_,_,_,_,_,_,_) := c.store[c.fcvEnv[n]]) {
            nonDefaults = {};
            defaults = { rtype };
            if(isProductionType(c.store[c.fcvEnv[n]].rtype)) {
            	defaults += c.store[c.fcvEnv[n]].rtype; 
            } else {
            	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
            		defaults += c.store[c.fcvEnv[n]].rtype;
            	} else {
            		nonDefaults += c.store[c.fcvEnv[n]].rtype;
            	}
            }
            c.store[c.nextLoc] = overload({ c.fcvEnv[n], productionItemId }, overloaded(nonDefaults,defaults));
            c.fcvEnv[n] = c.nextLoc;
            c.nextLoc = c.nextLoc + 1;
	    } else {
	        throw "Invalid addition: cannot add production into scope, it clashes with non-constructor variable or function names";
	    }
	}
    
    existsAlready = size({ i | i <- c.nonterminalConstructors[sortId], c.store[i].at == l}) > 0;
    if (!existsAlready) {
	    nameWithSort = appendName(sortName,n);
	    nameWithModule = appendName(moduleName,n);
    
  	    productionItemId = c.nextLoc;
	    c.nextLoc = c.nextLoc + 1;
	    
		overlaps = { i | i <- c.nonterminalConstructors[sortId], c.store[i].name == n, comparable(c.store[i].rtype,rtype)}; //, !equivalent(c.store[i].rtype,rt)};
        if (size(overlaps) > 0)
            c = addScopeError(c,"Production overlaps existing productions in the same nonterminal : <productionItemId>, <overlaps>",l);

	    productionItem = production(n, rtype, head([i | i <- c.stack, \module(_,_) := c.store[i]]), l);
	    c.store[productionItemId] = productionItem;
	    c.definitions = c.definitions + < productionItemId, l >;
	    c.nonterminalConstructors = c.nonterminalConstructors + < sortId, productionItemId >;
	    
	    addProductionItem(n, productionItemId);
	    addProductionItem(nameWithSort, productionItemId);
	    addProductionItem(nameWithModule, productionItemId);
	}    
    
    // Add non-terminal fields
    alreadySeen = {};
    for(\label(str fn, Symbol ft) <- prod.symbols) {
    	if(fn notin alreadySeen) {
    		if(c.nonterminalFields[<sortId,fn>]?) {
    			t = c.nonterminalFields[<sortId,fn>];
    			// TODO: respective functions, e.g., equivalent etc., need to be defined on non-terminal and regular symbols
    			if(!equivalent( (Symbol::\conditional(_,_) := ft) ? ft.symbol : ft, 
    							(Symbol::\conditional(_,_) := t)  ? t.symbol  : t  )) {
    				c = addScopeError(c,"Field <fn> already defined as type <prettyPrintType(c.nonterminalFields[<sortId,fn>])> on non-terminal type <prettyPrintName(sortName)>, cannot redefine to type <prettyPrintType(ft)>",l);
    			}
    		} else {
    			c.nonterminalFields[<sortId,fn>] = ft;
    		}
    	} else {
    		c = addScopeError(c,"Field name <fn> cannot be repeated in the same production", l);
    	}
    	alreadySeen += fn;
    }
    
    return c;
}

public Configuration addSyntaxDefinition(Configuration c, RName rn, loc l, Production prod, bool isStart) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	mainModuleId = last([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

 	fullSortName = appendName(moduleName, rn);
    if (fullSortName notin c.typeEnv) {
    	c = addScopeError(c, "Could not add syntax definition, associated nonterminal is not in scope", l);
    	return c;
    }
    sortId = c.typeEnv[fullSortName];

    if(isStart) {
    	c.starts = c.starts + sortId;
    	c = addNonterminal(c,RSimpleName("start[<getNonTerminalName(prod.def)>]"), l, prod.def);
    	return c;
    }
    
    if(c.grammar[sortId]?) {
    	c.grammar[sortId] = choice(c.store[sortId].rtype, { c.grammar[sortId], prod });
    } else {
    	c.grammar[sortId] = choice(c.store[sortId].rtype, { prod });
    }
    return c;
}

public Configuration addModule(Configuration c, RName n, loc l) {
    c.modEnv[n] = c.nextLoc;
    c.store[c.nextLoc] = \module(n,l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

public Configuration popModule(Configuration c) {
    if (\module(_,_) !:= c.store[head(c.stack)]) throw "Error, can only be called when a module is the top item on the stack";
    c.stack = tail(c.stack);
    return c;
}

public Configuration addClosure(Configuration c, Symbol rt, KeywordParamMap keywordParams, loc l) {
    c.store[c.nextLoc] = closure(rt,keywordParams, head(c.stack),l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    c.expectedReturnType = getFunctionReturnType(rt);
    return c;
}

public Configuration addFunction(Configuration c, RName n, Symbol rt, KeywordParamMap keywordParams, set[Modifier] modifiers, bool isVarArgs, Vis visibility, list[Symbol] throwsTypes, loc l) {
    // TODO: Handle the visibility properly. The main point is that we should not have variants
    // for the same function that are given different visibilities.
    // TODO: Verify the scoping is working properly for the second and third cases. It should be the
    // case that, if we cannot shadow, this means the name exists within the same function or
    // module. We may want to be stricter, though, and say that functions can only be defined within
    // a module or function scope, not (for instance) inside control flow.
    // TODO: Check for overlaps. But, we need to figure out if this is even possible anymore, we naturally
    // have overlaps because of the pattern-based dispatch.
    // TODO: Along with the overlaps, see if we are bringing the same function in along multiple paths
    // and, if so, don't add a new entry for it. For now, we just get back multiple entries, which
    // is fine.
    rt@isVarArgs = isVarArgs;
    currentModuleId = head([i | i <- c.stack, \module(_,_) := c.store[i]]);

	// Create the new function item and insert it into the store; also keep track of
	// the item Id. This also handles other bookkeeping information, such as the
	// information on definitions and visibilities.
	functionItem = function(n,rt,keywordParams,isVarArgs,head(c.stack),throwsTypes,l);
	functionId = c.nextLoc;
	c.nextLoc = c.nextLoc + 1;
	c.store[functionId] = functionItem;
    c.definitions = c.definitions + < functionId, l >;
    c.visibilities[functionId] = visibility;
    for(Modifier modifier <- modifiers) c.functionModifiers = c.functionModifiers + <functionId,modifier>;

	// This actually links in the function item with a name in the proper manner. This is handled name by
	// name so we can keep separate overload sets for different versions of a name (if we qualify the name,
	// it should not refer to other names with different qualifiers).
	void addFunctionItem(RName n, int functionId) {	
	    if (n notin c.fcvEnv) {
	    	// Case 1: The name does not appear at all, so insert it and link it to the function item.
	        c.fcvEnv[n] = functionId;
	    } else if (overload(items, overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
	    	// Case 2: The name is already overloaded, so link in the Id as one of the overloads.
	        if(hasDefaultModifier(modifiers)) {
	        	defaults += rt;
	        } else {
	        	itemTypes += rt;
	        }
	        c.store[c.fcvEnv[n]] = overload(items + functionId, overloaded(itemTypes,defaults));
	    } else if (function(_,_,_,_,_,_,_) := c.store[c.fcvEnv[n]] || constructor(_,_,_,_,_) := c.store[c.fcvEnv[n]] || production(_,_,_,_) := c.store[c.fcvEnv[n]]) {
	    	// Case 3: The name is not overloaded yet, but this will make it overloaded. So, create the
	    	// overloading entry. We also then point the current name to this overload item, which will
	    	// then point (using the overload set) to the item currently referenced by the name.
	        itemTypes = {};
	        defaults = {};
	        if(isConstructorType(c.store[c.fcvEnv[n]].rtype)) {
	        	defaults += c.store[c.fcvEnv[n]].rtype;
	        } else if (isProductionType(c.store[c.fcvEnv[n]].rtype)) {
	        	defaults += c.store[c.fcvEnv[n]].rtype;
	        } else {
	        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
	        		defaults += c.store[c.fcvEnv[n]].rtype;
	        	} else {
	        		itemTypes += c.store[c.fcvEnv[n]].rtype;
	        	}
	        }
	        if(hasDefaultModifier(modifiers)) {
	        	defaults += rt;
	        } else {
	        	itemTypes += rt;
	        }
	        c.store[c.nextLoc] = overload({ c.fcvEnv[n], functionId }, overloaded(itemTypes,defaults));
			c.fcvEnv[n] = c.nextLoc;
	        c.nextLoc = c.nextLoc + 1;
	    } else if ((\module(_,_) := c.store[c.store[c.fcvEnv[n]].containedIn] && c.store[c.fcvEnv[n]].containedIn != currentModuleId)) {
	    	// Case 4: This function has the same name as a variable defined in another module. We still add
	    	// it, but we also issue a warning, since reuse of the name may be accidental.
	        c = addScopeWarning(c, "Function declaration masks imported variable definition", l);
	        c.fcvEnv[n] = functionId;
	    } else {
	    	// Case 5: This function has the same name as a variable defined in the same module. We don't allow
	    	// functions to shadow variables in this case.
	    	// TODO: Verify that we don't want a looser rule.
	        c = addScopeError(c, "Cannot add function <prettyPrintName(n)>, a variable of the same name has already been defined in the current scope",l);
	    }
	}

	// Now, link up the names. We always link up the unqualified name. If we are at the top of the module,
	// we also link up a version of the name qualified with the module name.
	addFunctionItem(n, functionId);
    if (\module(_,_) := c.store[head(c.stack)]) {
        // If this function is module-level, also make it referenceable through
        // the qualified name module::function.
        moduleName = head([m | i <- c.stack, m:\module(_,_) := c.store[i]]).name;
        addFunctionItem(appendName(moduleName,n), functionId);
    }
	
    return c;
}

public Configuration addTag(Configuration c, TagKind tk, RName n, set[Symbol] onTypes, Vis visibility, loc l) {
    // TODO: We currently always treat datatype declarations as public, so we just
    // ignore the visibility here. If we decide to allow private datatype declarations,
    // revisit this.
    if (rn in c.tagEnv) {
        currentVal = c.store[c.tagEnv[n]];
        if (tk != currentVal.tkind) throw "Cannot add tag with same name but different kind into environment!";
        c.store[c.tagEnv[n]].onTypes = c.store[c.tagEnv[n]].onTypes + onTypes;
        c.definitions[c.tagEnv[n]] = c.definitions + < c.tagEnv[n], l >; 
    } else {
        c.tagEnv[n] = c.nextLoc;
        c.store[c.nextLoc] = \tag(n, tk, onTypes, head([i | i <- c.stack, \module(_,_) := c.store[i]]), l);
        c.definitions = c.definitions + < c.nextLoc, l >;
        c.nextLoc = c.nextLoc + 1;
    }
    
    return c;
}

public Configuration addScopeMessage(Configuration c, Message m) = c[messages = c.messages + m];

public Configuration addScopeError(Configuration c, str s, loc l) = addScopeMessage(c,error(s,l));
public Configuration addScopeWarning(Configuration c, str s, loc l) = addScopeMessage(c,warning(s,l));
public Configuration addScopeInfo(Configuration c, str s, loc l) = addScopeMessage(c,info(s,l));

@doc{Represents the result of checking an expression.}
alias CheckResult = tuple[Configuration conf, Symbol res];

@doc{Marks if a function is a var-args function.}
public anno bool Symbol@isVarArgs;

@doc{Marks the location(s) where a defined type (function, constructor, etc) is defined.}
public anno set[loc] Symbol@definedAt;

@doc{Strip the label off a symbol, if it has one at the top.}
private Symbol stripLabel(Symbol::\label(str s, Symbol t)) = stripLabel(t);
private default Symbol stripLabel(Symbol t) = t;

public Configuration enterBlock(Configuration c, loc l) {
    c.store[c.nextLoc] = blockScope(head(c.stack), l);
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

public Configuration exitBlock(Configuration c, Configuration cOrig) {
    c.stack = tail(c.stack);
    return recoverEnvironments(c,cOrig);
}

public Configuration enterBooleanScope(Configuration c, loc l) {
    c.store[c.nextLoc] = booleanScope(head(c.stack), l);
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

public Configuration exitBooleanScope(Configuration c, Configuration cOrig) {
    c.stack = tail(c.stack);
    return recoverEnvironments(c,cOrig);
}

public CheckResult checkStatementSequence(list[Statement] ss, Configuration c) {
	// Introduce any functions in the statement list into the current scope, but
	// don't process the bodies, just the signatures. This way we can use functions
	// in the bodies of other functions inside the block before the declaring statement
	// is reached.
    fundecls = [ fd | Statement fds:(Statement)`<FunctionDeclaration fd>` <- ss ];
	for (fundecl <- fundecls) {
		c = checkFunctionDeclaration(fundecl, false, c);
	}
	t1 = Symbol::\void();
	for (s <- ss) < c, t1 > = checkStmt(s, c);
	return < c, t1 >;
}

@doc{Check the types of Rascal expressions: NonEmptyBlock (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`{ <Statement+ ss> }`, Configuration c) {
    cBlock = enterBlock(c,exp@\loc);

	< cBlock, t1 > = checkStatementSequence([ssi | ssi <- ss], cBlock);

    c = exitBlock(cBlock,c);
    
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    return markLocationType(c,exp@\loc,t1);
}

@doc{Check the types of Rascal expressions: Bracket (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`( <Expression e> )`, Configuration c) {
    < c, t1 > = checkExp(e,c);
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    return markLocationType(c,exp@\loc,t1);
}

@doc{Check the types of Rascal expressions: Closure (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Type t> <Parameters ps> { <Statement+ ss> }`, Configuration c) {
    // Add an empty closure -- this ensures that the parameters, processed
    // when building the function type, are created in the closure environment
    // instead of in the surrounding environment.   
    < cFun, rt > = convertAndExpandType(t,c);
    Symbol funType = Symbol::\func(rt,[]);
    cFun = addClosure(cFun, funType, ( ), exp@\loc);
    
    // Calculate the parameter types. This returns the parameters as a tuple. As
    // a side effect, names defined in the parameters are added to the environment.
    < cFun, ptTuple > = checkParameters(ps, cFun);
    list[Symbol] parameterTypes = getTupleFields(ptTuple);

	< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(ps), cFun);
    
    // Check each of the parameters for failures. If we have any failures, we do
    // not build a function type.
    paramFailures = { pt | pt <- (parameterTypes+toList(keywordParams<1>)), isFailType(pt) };
    if (size(paramFailures) > 0) {
        funType = collapseFailTypes(paramFailures + makeFailType("Could not calculate function type because of errors calculating the parameter types", exp@\loc));     
    } else {
        funType = makeFunctionTypeFromTuple(rt, false, \tuple(parameterTypes));
    }
    
    // Update the closure with the computed function type.
    cFun.store[head(cFun.stack)].rtype = funType;
	cFun.store[head(cFun.stack)].keywordParams = keywordParams;
	    
    // In the environment with the parameters, check the body of the closure.
	< cFun, st > = checkStatementSequence([ssi | ssi <- ss], cFun);
    
    // Now, recover the environment active before the call, removing any names
    // added by the closure (e.g., for parameters) from the environment. This
    // also cleans up any parts of the configuration altered to invoke a
    // function or closure.
    c = recoverEnvironmentsAfterCall(cFun,c);

    if (isFailType(funType))
        return markLocationFailed(c, exp@\loc, funType); 
    else
        return markLocationType(c,exp@\loc, funType);
}

@doc{Check the types of Rascal expressions: StepRange (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`[ <Expression ef> , <Expression es> .. <Expression el> ]`, Configuration c) {
    < c, t1 > = checkExp(ef, c);
    < c, t2 > = checkExp(es, c);
    < c, t3 > = checkExp(el, c);

    if (!isFailType(t1) && !isFailType(t2) && !isFailType(t3) && subtype(t1,\num()) && subtype(t2,\num()) && subtype(t3,\num())) {
        return markLocationType(c,exp@\loc,\list(lubList([t1,t2,t3])));
    } else {
        if (!isFailType(t1) && !subtype(t1,\num())) t1 = makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t1)>", ef@\loc);
        if (!isFailType(t2) && !subtype(t2,\num())) t2 = makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t2)>", es@\loc);
        if (!isFailType(t3) && !subtype(t3,\num())) t3 = makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t3)>", el@\loc);
        return markLocationFailed(c,exp@\loc,{t1,t2,t3});
    }
}

@doc{Check the types of Rascal expressions: VoidClosure (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Parameters ps> { <Statement* ss> }`, Configuration c) {
    // Add an empty closure -- this ensures that the parameters, processed
    // when building the function type, are created in the closure environment
    // instead of in the surrounding environment.   
    rt = \void();
    Symbol funType = Symbol::\func(rt,[]);
    cFun = addClosure(c, funType, ( ), exp@\loc);
    
    // Calculate the parameter types. This returns the parameters as a tuple. As
    // a side effect, names defined in the parameters are added to the environment.
    < cFun, ptTuple > = checkParameters(ps, cFun);
    < cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(ps), cFun);
    list[Symbol] parameterTypes = getTupleFields(ptTuple);
    
    // Check each of the parameters for failures. If we have any failures, we do
    // not build a function type.
    paramFailures = { pt | pt <- (parameterTypes+toList(keywordParams<1>)), isFailType(pt) };
    if (size(paramFailures) > 0) {
        funType = collapseFailTypes(paramFailures + makeFailType("Could not calculate function type because of errors calculating the parameter types", exp@\loc));     
    } else {
        funType = makeFunctionTypeFromTuple(rt, false, \tuple(parameterTypes));
    }
    
    // Update the closure with the computed function type.
    cFun.store[head(cFun.stack)].rtype = funType;
	cFun.store[head(cFun.stack)].keywordParams = keywordParams;
    
    // In the environment with the parameters, check the body of the closure.
	< cFun, t1 > = checkStatementSequence([ssi | ssi <- ss], cFun);
    
    // Now, recover the environment active before the call, removing any names
    // added by the closure (e.g., for parameters) from the environment. This
    // also cleans up any parts of the configuration altered to invoke a
    // function or closure.
    c = recoverEnvironmentsAfterCall(cFun,c);

    if (isFailType(funType))
        return markLocationFailed(c, exp@\loc, funType); 
    else
        return markLocationType(c,exp@\loc, funType);
}

@doc{Check the types of Rascal expressions: Visit (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Label l> <Visit v>`, Configuration c) {
    // Treat the visit as a block, since the label has a defined scope from the start to
    // the end of the visit, but not outside of it.
    cVisit = enterBlock(c,exp@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := l) {
        labelName = convertName(n);
        if (labelExists(cVisit,labelName)) cVisit = addMessage(cVisit,error("Cannot reuse label names: <n>", l@\loc));
        cVisit = addLabel(cVisit,labelName,l@\loc,visitLabel());
        cVisit.labelStack = labelStackItem(labelName, visitLabel(), \void()) + cVisit.labelStack;
    } else {
        cVisit.labelStack = labelStackItem(RSimpleName(""), visitLabel(), \void()) + cVisit.labelStack;
    }
    
    < cVisit, vt > = checkVisit(v,cVisit);

    // Remove the added item from the label stack and then exit the block we created above,
    // which will clear up the added label name, removing it from scope.
    cVisit.labelStack = tail(cVisit.labelStack);
    c = exitBlock(cVisit,c);

    if (isFailType(vt)) return markLocationFailed(c,exp@\loc,vt);
    return markLocationType(c,exp@\loc,vt);
}

@doc{Check the types of Rascal expressions: Reducer (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )`, Configuration c) {
    // Check the initializer first, which runs outside of a scope with "it" defined.
    < c, t1 > = checkExp(ei, c);
    
    // Enter a boolean expression scope, since we could bind new variables in
    // the generators (egs) that should not be visible outside the reducer.
    // NOTE: "it" is also not in scope here.
    // TODO: The scope actually starts at er and goes to the end of the
    // reducer. Modify the loc to account for this.
    cRed = enterBooleanScope(c,exp@\loc);
    list[Symbol] ts = [];
    for (eg <- egs) { < cRed, t2 > = checkExp(eg,cRed); ts += t2; }
    
    // If the initializer isn't fail, introduce the variable "it" into scope; it is 
    // available in er (the result), but not in the rest, and we need it to check er.
    // Note that this means we cannot check er if we cannot assign an initial type to
    // "it", since we have no information on which to base a reasonable assumption. 
    Symbol erType = t1;
    if (!isFailType(t1)) {
        cRed = addVariable(cRed, RSimpleName("it"), true, exp@\loc, erType);
        < cRed, t3 > = checkExp(er, cRed);
        if (!isFailType(t3)) {
            if (!equivalent(erType,t3) && lub(erType,t3) == t3) {
                // If this is true, this means that "it" now has a different type, and
                // that the type is growing towards value. We run the body again to
                // see if the type changes again. This covers many standard cases
                // such as assigning it the value [] and then adding items to the
                // list, while failing in cases where the type is dependent on
                // the number of iterations.
                erType = t3;
                cRed.store[cRed.fcvEnv[RSimpleName("it")]].rtype = erType;
                < cRed, t3 > = checkExp(er, cRed);
                if (!isFailType(t3)) {
                    if (!equivalent(erType,t3)) {
                        erType = makeFailType("Type of it does not stabilize", exp@\loc);
                    }
                } else {
                    erType = t3;
                }
            } else if (!equivalent(erType,t3)) {
                erType = makeFailType("Type changes in non-monotonic fashion", exp@\loc);
            }
        } else {
            erType = t3;
        }
        cRed.store[cRed.fcvEnv[RSimpleName("it")]].rtype = erType;
    }

    // Leave the boolean scope, which will remove all names added in the generators and
    // also will remove "it".
    c = exitBooleanScope(cRed, c);
    
    // Calculate the final type. If we had failures, it is a failure, else it
    // is the type of the reducer step.
    failTypes = { t | t <- (ts + t1 + erType), isFailType(t) };
    if (size(failTypes) > 0) {
        return markLocationFailed(c,exp@\loc,failTypes);
    } else {
        return markLocationType(c,exp@\loc,erType);
    }
}

@doc{Check the types of Rascal expressions: ReifiedType (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`type ( <Expression es> , <Expression ed> )`, Configuration c) {
    // TODO: Is there anything we can do statically to make the result type more accurate?
    < c, t1 > = checkExp(es, c);
    < c, t2 > = checkExp(ed, c);
    if (!isFailType(t1) && !subtype(t1,\adt("Symbol",[])))
        t1 = makeFailType("Expected subtype of Symbol, instead found <prettyPrintType(t1)>",es@\loc);
    if (!isFailType(t1) && !subtype(t2,\map(\adt("Symbol",[]),\adt("Production",[]))))
        t2 = makeFailType("Expected subtype of map[Symbol,Production], instead found <prettyPrintType(t2)>",ed@\loc);
    if (isFailType(t1) || isFailType(t2))
        return markLocationFailed(c,exp@\loc,collapseFailTypes({t1,t2}));
    else
        return markLocationType(c,exp@\loc,\reified(\value()));
}

@doc{Check the types of Rascal expressions: Concete Syntax Fragments (TODO)}
public CheckResult checkExp(Expression exp: (Expression) `<Concrete concrete>`, Configuration c) {
  set[Symbol] failures = { };
  
  for (hole((ConcreteHole) `\<<Sym s> <Name n>\>`) <- concrete.parts) {
    <c, rt> = convertAndExpandSymbol(s, c);
    if(isFailType(rt)) { 
        failures += rt; 
    }  
    
    name = convertName(n)[@at = n@\loc];
    
    if (fcvExists(c, name)) {
        c.uses = c.uses + < c.fcvEnv[name], n@\loc >;
        c.usedIn[n@\loc] = head(c.stack);
        <c, rt> = markLocationType(c, n@\loc, c.store[c.fcvEnv[name]].rtype);
    } else {
        <c, rt> = markLocationFailed(c, n@\loc, makeFailType("Name <prettyPrintName(name)> is not in scope", n@\loc));
        failures += rt;
    }
  }
  
  if(size(failures) > 0) {
    return markLocationFailed(c, exp@\loc, failures);
  }
  
  <c, rt> = convertAndExpandSymbol(concrete.symbol, c);
  
  return markLocationType(c, exp@\loc, rt);
}

@doc{Check the types of Rascal expressions: CallOrTree}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> ( <{Expression ","}* eps> <KeywordArguments keywordArguments> )`, Configuration c) {
    // check for failures
    set[Symbol] failures = { };
    
    list[Expression] epsList = [ epsi | epsi <- eps ];
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) failures += t1;
    list[Symbol] tl = [];
    for (ep <- eps) { 
        < c, t2 > = checkExp(ep, c); 
        tl += t2; 
        if (isFailType(t2)) failures += t2; 
    }
    
    // If we have any failures, either in the head or in the arguments,
    // we aren't going to be able to match, so filter these cases out
    // here
    if (size(failures) > 0)
        return markLocationFailed(c, exp@\loc, failures);
    
 	tuple[Symbol, bool, Configuration] instantiateFunctionTypeArgs(Configuration c, Symbol targetType) {
		// If the function is parametric, we need to calculate the actual types of the
    	// parameters and make sure they fall within the proper bounds.
    	formalArgs = getFunctionArgumentTypes(targetType);
		bool varArgs = ( ((targetType@isVarArgs)?) ? targetType@isVarArgs : false );
		set[Symbol] typeVars = { *collectTypeVars(fa) | fa <- formalArgs };
		map[str,Symbol] bindings = ( getTypeVarName(tv) : \void() | tv <- typeVars );
    	bool canInstantiate = true;            
		if (!varArgs) {
			// First try to get the bindings between the type vars and the actual types for each of the
			// function parameters. Here this is not a varargs function, so there are the same number of
			// formals as actuals.
			for (idx <- index(tl)) {
				try {
					bindings = match(formalArgs[idx],tl[idx],bindings);
				} catch : {
					// c = addScopeError(c,"Cannot instantiate parameter <idx+1>, parameter type <prettyPrintType(tl[idx])> violates bound of type parameter in formal argument with type <prettyPrintType(formalArgs[idx])>", epsList[idx]@\loc);
					canInstantiate = false;  
				}
			}
		} else {
			// Get the bindings between the type vars and the actual types for each function parameter. Since
			// this is a var-args function, we need to take that into account. The first for loop takes care
			// of the fixes parameters, while the second takes care of those that are mapped to the var-args
			// parameter.
			for (idx <- index(tl), idx < size(formalArgs)) {
				try {
					bindings = match(formalArgs[idx],tl[idx],bindings);
				} catch : {
					// c = addScopeError(c,"Cannot instantiate parameter <idx+1>, parameter type <prettyPrintType(tl[idx])> violates bound of type parameter in formal argument with type <prettyPrintType(formalArgs[idx])>", epsList[idx]@\loc);
					canInstantiate = false;  
				}
			}
			for (idx <- index(tl), idx >= size(formalArgs)) {
				try {
					bindings = match(getListElementType(formalArgs[size(formalArgs)-1]),tl[idx],bindings);
				} catch : {
					// c = addScopeError(c,"Cannot instantiate parameter <idx+1>, parameter type <prettyPrintType(tl[idx])> violates bound of type parameter in formal argument with type <prettyPrintType(getListElementType(formalArgs[size(formalArgs)-1]))>", epsList[idx]@\loc);
					canInstantiate = false;  
				}
			}
    	}
    	// Based on the above, either give an error message (if we could not match the function's parameter types) or
    	// try to instantiate the entire function type. The instantiation should only fail if we cannot instantiate
    	// the return type correctly, for instance if the instantiation would violate the bounds.
    	// NOTE: We may instantiate and still have type parameters, since we may be calling this from inside
    	// a function, using a value with a type parameter as its type.
    	if (canInstantiate) {
        	try {
            	targetType = instantiate(targetType, bindings);
        	} catch : {
            	canInstantiate = false;
        	}
    	}
    	return < targetType, canInstantiate, c >;	
	}
	
	// Special handling for overloads -- if we have an overload, at least one of the overload options
	// should be a subtype of the other type, but some of them may not be.
	bool subtypeOrOverload(Symbol t1, Symbol t2) {
		if (!isOverloadedType(t1)) {
			return subtype(t1,t2);
		} else {
			overloads = getNonDefaultOverloadOptions(t1) + getDefaultOverloadOptions(t1);
			return (true in { subtype(overload,t2) | overload <- overloads });
		}		
	}
	
	tuple[Configuration c, set[Symbol] matches, set[str] failures] matchFunctionAlts(Configuration c, set[Symbol] alts) {
        set[Symbol] matches = { };
        set[str] failureReasons = { };
        for (a <- alts, isFunctionType(a)) {
            list[Symbol] args = getFunctionArgumentTypes(a);
            // NOTE: We cannot assume the annotation is set, since we only set it when we add a
            // function (and have the info available); we don't have the information when we only
            // have a function type, such as with a function parameter.
            bool varArgs = ( ((a@isVarArgs)?) ? a@isVarArgs : false );
            if (!varArgs) {
                if (size(epsList) == size(args) && size(epsList) == 0) {
                    matches += a;
                } else if (size(epsList) == size(args)) {
					if (typeContainsTypeVars(a)) {
        				< instantiated, b, c > = instantiateFunctionTypeArgs(c, a);
        				if (!b) {
        					failureReasons += "Could not instantiate type variables in type <prettyPrintType(a)> with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
        					continue;
        				}
        				args = getFunctionArgumentTypes(instantiated);
        			}
                 	if (false notin { subtypeOrOverload(tl[idx],args[idx]) | (idx <- index(epsList)) })
                    	matches += a;
                    else
                    	failureReasons += "Function of type <prettyPrintType(a)> cannot be called with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
                } else {
                    failureReasons += "Function of type <prettyPrintType(a)> cannot be called with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
                }
            } else {
                if (size(epsList) >= size(args)-1) {
                    if (size(epsList) == 0) {
                        matches += a;
                    } else {
						if (typeContainsTypeVars(a) && size(args)-1 <= size(tl)) {
    	    				< instantiated, b, c > = instantiateFunctionTypeArgs(c, a);
	        				if (!b) {
	        					failureReasons += "Could not instantiate type variables in type <prettyPrintType(a)> with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
	        					continue;
	        				}
        					args = getFunctionArgumentTypes(instantiated);
        				}
        				// TODO: It may be good to put another check here to make sure we don't
        				// continue if the size is wrong; we will still get the proper error, but
        				// we could potentially give a better message here
                        list[Symbol] fixedPart = head(tl,size(args)-1);
                        list[Symbol] varPart = tail(tl,size(tl)-size(args)+1);
                        list[Symbol] fixedArgs = head(args,size(args)-1);
                        Symbol varArgsType = getListElementType(last(args));
                        if (size(fixedPart) == 0 || all(idx <- index(fixedPart), subtypeOrOverload(fixedPart[idx],fixedArgs[idx]))) {
                            if (size(varPart) == 0) {
                                matches += a;
                            } else if (size(varPart) == 1 && subtypeOrOverload(varPart[0],last(args))) {
                                matches += a;
                            } else if (all(idx2 <- index(varPart),subtypeOrOverload(varPart[idx2],varArgsType))) {
                                matches += a;
                            } else {
                                failureReasons += "Function of type <prettyPrintType(a)> cannot be called with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
                            }
                        } else {
                            failureReasons += "Function of type <prettyPrintType(a)> cannot be called with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
                        }
                    }
                }
            }
        }
        // TODO: Here would be a good place to filter out constructors that are "masked" by functions with the
        // same name and signature. We already naturally mask function declarations by using a set, but we do
        // need to keep track there of possible matching IDs so we can link things up correctly.
        return < c, matches, failureReasons >;
    }
    
   tuple[Configuration c, set[Symbol] matches, set[str] failures] matchConstructorAlts(Configuration c, set[Symbol] alts) {
        set[Symbol] matches = { };
        set[str] failureReasons = { };
        for (a <- alts, isConstructorType(a)) {
            list[Symbol] args = getConstructorArgumentTypes(a);
            if (size(epsList) == size(args) && size(epsList) == 0) {
                matches += a;
            } else if (size(epsList) == size(args) && false notin { subtype(tl[idx],args[idx]) | idx <- index(epsList) }) {
                matches += a;
            } else {
                failureReasons += "Constructor of type <prettyPrintType(a)> cannot be built with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
            }
        }
        // TODO: Here would be a good place to filter out constructors that are "masked" by functions with the
        // same name and signature. We already naturally mask function declarations by using a set, but we do
        // need to keep track there of possible matching IDs so we can link things up correctly.
        return < c, matches, failureReasons >;
    }

   tuple[Configuration c, set[Symbol] matches, set[str] failures] matchProductionAlts(Configuration c, set[Symbol] alts) {
        set[Symbol] matches = { };
        set[str] failureReasons = { };
        for (a <- alts, isProductionType(a)) {
            list[Symbol] args = getProductionArgumentTypes(a);
            if (size(epsList) == size(args) && size(epsList) == 0) {
                matches += a;
            } else if (size(epsList) == size(args) && false notin { subtype(tl[idx],args[idx]) | idx <- index(epsList) }) {
                matches += a;
            } else {
                failureReasons += "Production of type <prettyPrintType(a)> cannot be built with argument types (<intercalate(",",[prettyPrintType(tli)|tli<-tl])>)";
            }
        }
        // TODO: Here would be a good place to filter out productions that are "masked" by functions with the
        // same name and signature. We already naturally mask function declarations by using a set, but we do
        // need to keep track there of possible matching IDs so we can link things up correctly.
        return < c, matches, failureReasons >;
    }
        
    // e was either a name or an expression that evaluated to a function, a constructor, a production,
    // a source location, or a string
    if (isFunctionType(t1) || isConstructorType(t1) || isOverloadedType(t1) || isProductionType(t1)) {
        set[Symbol] alts     = isFunctionType(t1) ? {t1} : ( (isConstructorType(t1) || isProductionType(t1)) ? {  } : getNonDefaultOverloadOptions(t1) );
        set[Symbol] defaults = isFunctionType(t1) ? {  } : ( (isConstructorType(t1) || isProductionType(t1)) ? {t1} : getDefaultOverloadOptions(t1) );
        
        < c, nonDefaultFunctionMatches, nonDefaultFunctionFailureReasons > = matchFunctionAlts(c, alts);
        < c, defaultFunctionMatches, defaultFunctionFailureReasons > = matchFunctionAlts(c, defaults);
        < c, constructorMatches, constructorFailureReasons > = matchConstructorAlts(c, defaults);
        < c, productionMatches, productionFailureReasons > = matchProductionAlts(c, defaults);
        
        if (size(nonDefaultFunctionMatches + defaultFunctionMatches + constructorMatches + productionMatches) == 0) {
            return markLocationFailed(c,exp@\loc,{makeFailType(reason,exp@\loc) | reason <- (nonDefaultFunctionFailureReasons + defaultFunctionFailureReasons + constructorFailureReasons + productionFailureReasons)});
        } else if ( (size(nonDefaultFunctionMatches) > 1 || size(defaultFunctionMatches) > 1) && size(constructorMatches) > 1 && size(productionMatches) > 1) {
            return markLocationFailed(c,exp@\loc,makeFailType("Multiple functions, constructors, and productions found which could be applied",exp@\loc));
        } else if ( (size(nonDefaultFunctionMatches) > 1 || size(defaultFunctionMatches) > 1) && size(constructorMatches) > 1) {
            return markLocationFailed(c,exp@\loc,makeFailType("Multiple functions and constructors found which could be applied",exp@\loc));
        } else if ( (size(nonDefaultFunctionMatches) > 1 || size(defaultFunctionMatches) > 1) && size(productionMatches) > 1) {
            return markLocationFailed(c,exp@\loc,makeFailType("Multiple functions and productions found which could be applied",exp@\loc));
        } else if (size(nonDefaultFunctionMatches) > 1 || size(defaultFunctionMatches) > 1) {
            return markLocationFailed(c,exp@\loc,makeFailType("Multiple functions found which could be applied",exp@\loc));
        } else if (size(constructorMatches) > 1) {
            return markLocationFailed(c,exp@\loc,makeFailType("Multiple constructors found which could be applied",exp@\loc));
        } else if (size(productionMatches) > 1) {
        	return markLocationFailed(c,exp@\loc,makeFailType("Multiple productions found which could be applied",exp@\loc));
        } else if (size(productionMatches) > 1 && size(constructorMatches) > 1)
        	return markLocationFailed(c,exp@\loc,makeFailType("Both a constructor and a concrete syntax production could be applied",exp@\loc));
        
        set[Symbol] finalNonDefaultMatches = {};
        set[Symbol] finalDefaultMatches = {};
        bool cannotInstantiateFunction = false;
        bool cannotInstantiateConstructor = false;
        
        if (size(nonDefaultFunctionMatches + defaultFunctionMatches) > 0) {
            rts = nonDefaultFunctionMatches + defaultFunctionMatches;
            for(rt <- rts) {
            	isInDefaults = rt in defaultFunctionMatches;
            	isInNonDefaults = rt in nonDefaultFunctionMatches;
            	
            	if (typeContainsTypeVars(rt)) {
					< rt, canInstantiate, c > = instantiateFunctionTypeArgs(c, rt);
					cannotInstantiateFunction = !canInstantiate;
					if(isInDefaults) {
						finalDefaultMatches += rt;
					}
					if(isInNonDefaults) {
						finalNonDefaultMatches += rt;
					}
            	} else {
            		if(isInDefaults) {
            			finalDefaultMatches += rt;
            		}
            		if(isInNonDefaults) {
            			finalNonDefaultMatches += rt;
            		}
            	}
            }
		} 
		
		if (size(constructorMatches) == 1) {
            rt = getOneFrom(constructorMatches);
            if (typeContainsTypeVars(rt)) {
                // If the constructor is parametric, we need to calculate the actual types of the
                // parameters and make sure they fall within the proper bounds.
                formalArgs = getConstructorArgumentTypes(rt);
                set[Symbol] typeVars = { *collectTypeVars(fa) | fa <- (formalArgs+rt) };
                map[str,Symbol] bindings = ( getTypeVarName(tv) : \void() | tv <- typeVars );
                for (idx <- index(tl)) {
                    try {
                        bindings = match(formalArgs[idx],tl[idx],bindings);
                    } catch : {
                        c = addScopeError(c,"Cannot instantiate parameter <idx+1>, parameter type <prettyPrintType(tl[idx])> violates bound of type parameter in formal argument with type <prettyPrintType(formalArgs[idx])>", epsList[idx]@\loc);
                        cannotInstantiateConstructor = true;  
                    }
                }
                if (!cannotInstantiateConstructor) {
                    try {
                        rt = instantiate(rt, bindings);
                        finalDefaultMatches += rt;
                    } catch : {
                        cannotInstantiateConstructor = true;
                    }
                }
            } else {
            	finalDefaultMatches += rt;
            }
        }

		if (size(productionMatches) == 1) {
            finalDefaultMatches += getOneFrom(productionMatches);
        }
        
        if (cannotInstantiateFunction && cannotInstantiateConstructor) {
        	return markLocationFailed(c,exp@\loc,makeFailType("Cannot instantiate type parameters in function invocation and constructor", exp@\loc));
        } else if (cannotInstantiateFunction) {
        	return markLocationFailed(c,exp@\loc,makeFailType("Cannot instantiate type parameters in function invocation", exp@\loc));
        } else if (cannotInstantiateConstructor) {
        	return markLocationFailed(c,exp@\loc,makeFailType("Cannot instantiate type parameters in constructor", exp@\loc));
        } else {
        	if ( (size(finalNonDefaultMatches) + size(finalDefaultMatches)) == 1 ) {
        		finalMatch = getOneFrom(finalNonDefaultMatches + finalDefaultMatches);
        		if (isFunctionType(finalMatch)) {
				    < c, rtp > = markLocationType(c,e@\loc,finalMatch);
				    return markLocationType(c,exp@\loc,getFunctionReturnType(finalMatch));
				} else if (isConstructorType(finalMatch)) {
			        < c, rtp > = markLocationType(c,e@\loc,finalMatch);
			        return markLocationType(c,exp@\loc,getConstructorResultType(finalMatch));
				} else if (isProductionType(finalMatch)) {
					< c, rtp > = markLocationType(c,e@\loc,finalMatch);
					return markLocationType(c,exp@\loc,getProductionSortType(finalMatch));
				}
			} else if (size(finalNonDefaultMatches) == 0 && size(finalDefaultMatches) == 2) {
				// Make sure the defaults function, constructor, and production variants have the same return type, else we
				// have a conflict.
				functionVariant = getOneFrom(filterSet(finalDefaultMatches, isFunctionType));
				constructorMatches = filterSet(finalDefaultMatches, isConstructorType);
				productionMatches = filterSet(finalDefaultMatches, isProductionType);
				nonFunctionResult = (size(constructorMatches) > 0) ? getConstructorResultType(getOneFrom(constructorMatches)) : getProductionSortType(getOneFrom(productionMatches));
				
				if (!equivalent(getFunctionReturnType(functionVariant),nonFunctionResult)) {
					// TODO: This should also result in an error on the function
					// declaration, since we should not have a function with the same name
					// and parameters but a different return type
					println("WARNING: call at <e@\loc> uses a function with a bad return type");    
				}
				< c, rtp > = markLocationType(c,e@\loc,functionVariant);
				return markLocationType(c,exp@\loc,getFunctionReturnType(functionVariant));
			} else if (size(finalNonDefaultMatches) == 1 && size(finalDefaultMatches) == 1) {
				// Make sure the function and the default function or constructor variants have the same return type, else we
				// have a conflict.
				functionVariant = getOneFrom(filterSet(finalNonDefaultMatches, isFunctionType));
				defaultVariant = getOneFrom(finalDefaultMatches);
				defaultResultType = isConstructorType(defaultVariant) ? getConstructorResultType(defaultVariant) : (isFunctionType(defaultVariant) ? getFunctionReturnType(defaultVariant) : getProductionSortType(defaultVariant));
				
				if (equivalent(getFunctionReturnType(functionVariant),defaultResultType)) {
					finalType = makeOverloadedType(finalNonDefaultMatches,finalDefaultMatches);
				    < c, rtp > = markLocationType(c,e@\loc,finalType);
				    return markLocationType(c,exp@\loc,getFunctionReturnType(functionVariant));
				} else {
					// TODO: This should also result in an error on the function
					// declaration, since we should not have a function with the same name
					// and parameters but a different return type
					println("WARNING: call at <e@\loc> uses a function with a bad return type");
				    < c, rtp > = markLocationType(c,e@\loc,defaultVariant);
				    return markLocationType(c,exp@\loc,defaultResultType);
				}
			} else {
				// Make sure the function, the default function and constructor variants have the same return type, else we
				// have a conflict.
				functionVariant = getOneFrom(filterSet(finalNonDefaultMatches, isFunctionType));
				defaultVariant = getOneFrom(filterSet(finalDefaultMatches, isFunctionType));
				constructorMatches = filterSet(finalDefaultMatches, isConstructorType);
				productionMatches = filterSet(finalDefaultMatches, isProductionType);
				nonFunctionResult = (size(constructorMatches) > 0) ? getConstructorResultType(getOneFrom(constructorMatches)) : getProductionSortType(getOneFrom(productionMatches));
				
				if ( equivalent(getFunctionReturnType(functionVariant),getFunctionReturnType(defaultVariant))
						&& equivalent(getFunctionReturnType(functionVariant),nonFunctionResult) ) {
					finalType = makeOverloadedType(finalNonDefaultMatches,{ defaultVariant });
				    < c, rtp > = markLocationType(c,e@\loc,finalType);
				    return markLocationType(c,exp@\loc,getFunctionReturnType(functionVariant));
				} else {
					// TODO: This should also result in an error on the function
					// declaration, since we should not have a function with the same name
					// and parameters but a different return type
					println("WARNING: call at <e@\loc> uses a function with a bad return type");
				    < c, rtp > = markLocationType(c,e@\loc,defaultVariant);
				    return markLocationType(c,exp@\loc,getFunctionReturnType(defaultVariant));
				}
			}
        }
        
    } else if (isLocType(t1)) {
        if (size(tl) == 4) {
            // We are expecting a signature of int, int, tuple[int,int], tuple[int,int], make sure we got it
            if (!isIntType(tl[0])) 
                failures += makeFailType("Expected int, found <prettyPrintType(tl[0])>", epsList[0]@\loc);  
                        
            if (!isIntType(tl[1])) 
                failures += makeFailType("Expected int, found <prettyPrintType(tl[1])>", epsList[1]@\loc);
                            
            if (!isTupleType(tl[2])) {
                failures += makeFailType("Expected tuple[int,int], found <prettyPrintType(tl[2])>", epsList[2]@\loc);
            } else {
                tf1 = getTupleFields(tl[2]);
                if (!(size(tf1) == 2 && isIntType(tf1[0]) && isIntType(tf1[1])))
                    failures += makeFailType("Expected tuple[int,int], found <prettyPrintType(tl[2])>", epsList[2]@\loc);
            }   
                
            if (!isTupleType(tl[3])) { 
                failures += makeFailType("Expected tuple[int,int], found <prettyPrintType(tl[3])>", epsList[3]@\loc);
            } else {
                tf2 = getTupleFields(tl[3]);
                if (!(size(tf2) == 2 && isIntType(tf2[0]) && isIntType(tf2[1])))
                    failures += makeFailType("Expected tuple[int,int], found <prettyPrintType(tl[2])>", epsList[2]@\loc);
            }           
        } else {
            failures += makeFailType("Expected 4 arguments: int, int, tuple[int,int], and tuple[int,int]", exp@\loc); 
        }
        
        if (size(failures) > 0)
            return markLocationFailed(c,exp@\loc,failures);
        else
            return markLocationType(c,exp@\loc,\loc());
    } else if (isStrType(t1)) {
        return markLocationType(c,exp@\loc,\node());
    }
    
    return markLocationFailed(c,exp@\loc,makeFailType("Cannot use type <prettyPrintType(t1)> in calls", exp@\loc)); 
}

@doc{Check the types of Rascal expressions: Literal (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Literal l>`, Configuration c) {
    return checkLiteral(l, c);
}

public bool inBooleanScope(Configuration c) = ((size(c.stack) > 0) && (booleanScope(_,_) := c.store[c.stack[0]]));

@doc{Check the types of Rascal expressions: Any (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`any ( <{Expression ","}+ egs> )`, Configuration c) {
    // Start a new boolean scope. Names should not leak out of an any, even if
    // this is embedded inside a boolean scope already. If nothing else, we may
    // never have a valid match in the any, in which case the vars would not
    // be bound anyway.
    cAny = enterBooleanScope(c, exp@\loc);
    
    // Now, check the type of each of the generators. They should all evaluate to
    // a value of type bool.
    set[Symbol] failures = { };
    for (eg <- egs) { 
        < cAny, t1 > = checkExp(eg,cAny);
        if (isFailType(t1)) {
            failures += t1;
        } else if (!isBoolType(t1)) {
            failures += makeFailType("Expected type bool, found <prettyPrintType(t1)>", eg@\loc);
        } 
    }
    
    // Then, exit the boolean scope, which discards any of the names bound inside.
    c = exitBooleanScope(cAny, c);
    
    if (size(failures) > 0) return markLocationFailed(c, exp@\loc, collapseFailTypes(failures));
    return markLocationType(c, exp@\loc, \bool());
}

@doc{Check the types of Rascal expressions: All (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`all ( <{Expression ","}+ egs> )`, Configuration c) {
    // Start a new boolean scope. Names should not leak out of an all, even if
    // this is embedded inside a boolean scope already. If nothing else, we may
    // never have a valid match in the all, in which case the vars would not
    // be bound anyway.
    cAll = enterBooleanScope(c, exp@\loc);
    
    // Now, check the type of each of the generators. They should all evaluate to
    // a value of type bool.
    set[Symbol] failures = { };
    for (eg <- egs) { 
        < cAll, t1 > = checkExp(eg,cAll);
        if (isFailType(t1)) {
            failures += t1;
        } else if (!isBoolType(t1)) {
            failures += makeFailType("Expected type bool, found <prettyPrintType(t1)>", eg@\loc);
        } 
    }
    
    // Then, exit the boolean scope, which discards any of the names
    // bound inside.
    c = exitBooleanScope(cAll, c);
    
    if (size(failures) > 0) return markLocationFailed(c, exp@\loc, collapseFailTypes(failures));
    return markLocationType(c, exp@\loc, \bool());
}

@doc{Check the types of Rascal expressions: Comprehension (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Comprehension cp>`, Configuration c) {
    return checkComprehension(cp, c);
}

@doc{Check the types of Rascal expressions: Set (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`{ <{Expression ","}* es> }`, Configuration c) {
    list[Symbol] tl = [ \void() ];
    for (e <- es) { < c, t1 > = checkExp(e,c); tl += t1; }
    if (all(t <- tl, !isFailType(t))) {
        return markLocationType(c, exp@\loc, \set(lubList(tl)));
    } else {
        return markLocationFailed(c, exp@\loc, {t|t<-tl});
    }
}

@doc{Check the types of Rascal expressions: List (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`[ <{Expression ","}* es> ]`, Configuration c) {
    list[Symbol] tl = [ \void() ];
    for (e <- es) { < c, t1 > = checkExp(e,c); tl += t1; }
    if (all(t <- tl, !isFailType(t))) {
        return markLocationType(c, exp@\loc, \list(lubList(tl)));
    } else {
        return markLocationFailed(c, exp@\loc, {t|t<-tl});
    }
}

@doc{Check the types of Rascal expressions: ReifyType (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`# <Type t>`, Configuration c) {
    < c, rt > = convertAndExpandType(t,c);
    return markLocationType(c, exp@\loc, \reified(rt));
}

@doc{Check the types of Rascal expressions: Range (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`[ <Expression ef> .. <Expression el> ]`, Configuration c) {
    < c, t1 > = checkExp(ef, c);
    < c, t2 > = checkExp(el, c);
    
    if (!isFailType(t1) && !isFailType(t2) && subtype(t1,\num()) && subtype(t2,\num())) {
        return markLocationType(c,exp@\loc,\list(lubList([t1,t2])));
    } else {
        if (!subtype(t1,\num())) t1 = makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t1)>", ef@\loc);
        if (!subtype(t2,\num())) t2 = makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t2)>", el@\loc);
        return markLocationFailed(c,exp@\loc,{t1,t2});
    }
}

@doc{Check the types of Rascal expressions: Tuple (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`\< <Expression e1>, <{Expression ","}* es> \>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    list[Symbol] tl = [ t1 ];
    for (e <- es) { < c, t2 > = checkExp(e,c); tl += t2; }
    if (all(t <- tl, !isFailType(t))) {
        return markLocationType(c, exp@\loc, \tuple(tl));
    } else {
        return markLocationFailed(c, exp@\loc, {t|t<-tl});
    }
}

@doc{Check the types of Rascal expressions: Map (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`( <{Mapping[Expression] ","}* mes> )`, Configuration c) {
    list[Symbol] td = [ \void() ];
    list[Symbol] tr = [ \void() ];
    set[Symbol] failures = { };
    
    for ((Mapping[Expression])`<Expression ed> : <Expression er>` <- mes) {
        < c, t1 > = checkExp(ed, c);
        < c, t2 > = checkExp(er, c);

        if (isFailType(t1)) 
            failures += t1;
        else
            td += t1;

        if (isFailType(t2)) 
            failures += t2;
        else
            tr += t2;
    }
    
    if (size(failures) > 0)
        return markLocationFailed(c, exp@\loc, failures);
    else
        return markLocationType(c, exp@\loc, \map(lubList(td),lubList(tr)));
}

@doc{Check the types of Rascal expressions: it (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`it`, Configuration c) {
    if (fcvExists(c, RSimpleName("it"))) {
        c.uses = c.uses + < c.fcvEnv[RSimpleName("it")], exp@\loc >;
        c.usedIn[exp@\loc] = head(c.stack);
        return markLocationType(c, exp@\loc, c.store[c.fcvEnv[RSimpleName("it")]].rtype);
    } else {
        return markLocationFailed(c, exp@\loc, makeFailType("Name it not in scope", exp@\loc));
    }
}

@doc{Check the types of Rascal expressions: QualifiedName}
public CheckResult checkExp(Expression exp:(Expression)`<QualifiedName qn>`, Configuration c) {
    n = convertName(qn);
    if (fcvExists(c, n)) {
        c.uses = c.uses + < c.fcvEnv[n], exp@\loc >;
        c.usedIn[exp@\loc] = head(c.stack); 
        return markLocationType(c, exp@\loc, c.store[c.fcvEnv[n]].rtype);
    } else {
        return markLocationFailed(c, exp@\loc, makeFailType("Name <prettyPrintName(n)> is not in scope", exp@\loc));
    }
}

@doc{Check the types of Rascal expressions: Subscript (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> [ <{Expression ","}+ es> ]`, Configuration c) {
    list[Symbol] tl = [ ];
    set[Symbol] failures = { };
    < c, t1 > = checkExp(e, c);
    eslist = [ esi | esi <- es ];
    if (isFailType(t1)) failures = failures + t1;
    for (esi <- es) {
        // Subscripts can also use the "_" character, which means to ignore that position; we do
        // that here by treating it as \value(), which is comparable to all other types and will
        // thus work when calculating the type below.
        if ((Expression)`_` := esi) {
            tl += \value();
        } else { 
            < c, t2 > = checkExp(esi,c); 
            tl += t2; 
            if (isFailType(t2)) failures = failures + t2;
        } 
    }
    if (size(failures) > 0) {
        // If we do not have valid types for e or the subscripting expressions, we cannot compute
        // the type properly, so return right away with the failures. 
        return markLocationFailed(c, exp@\loc, failures);
    }
    if (isListType(t1) && !isListRelType(t1)) {
        if (size(tl) != 1)
            return markLocationFailed(c,exp@\loc,makeFailType("Expected only 1 subscript for a list expression, not <size(tl)>",exp@\loc));
        else if (!isIntType(tl[0]))
            return markLocationFailed(c,exp@\loc,makeFailType("Expected subscript of type int, not <prettyPrintType(tl[0])>",exp@\loc));
        else
            return markLocationType(c,exp@\loc,getListElementType(t1));
    } else if (isRelType(t1)) {
        if (size(tl) >= size(getRelFields(t1)))
            return markLocationFailed(c,exp@\loc,makeFailType("For a relation with arity <size(getRelFields(t1))> you can have at most <size(getRelFields(t1))-1> subscripts",exp@\loc));
        else {
            relFields = getRelFields(t1);
            failures = { makeFailType("At subscript <idx+1>, subscript type <prettyPrintType(tl[idx])> must be comparable to relation field type <prettyPrintType(relFields[idx])>", exp@\loc) | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],\set(relFields[idx]))) };
            if (size(failures) > 0)
                return markLocationFailed(c,exp@\loc,failures);
            else if ((size(relFields) - size(tl)) == 1)
                return markLocationType(c,exp@\loc,\set(last(relFields)));
            else
                return markLocationType(c,exp@\loc,\rel(tail(relFields,size(relFields)-size(tl))));
        }
    } else if (isListRelType(t1)) {
        if (size(tl) >= size(getListRelFields(t1)))
            return markLocationFailed(c,exp@\loc,makeFailType("For a list relation with arity <size(getListRelFields(t1))> you can have at most <size(getListRelFields(t1))-1> subscripts",exp@\loc));
        else {
            relFields = getListRelFields(t1);
            failures = { makeFailType("At subscript <idx+1>, subscript type <prettyPrintType(tl[idx])> must be comparable to relation field type <prettyPrintType(relFields[idx])>", exp@\loc) | idx <- index(tl), ! (comparable(tl[idx],relFields[idx]) || comparable(tl[idx],\set(relFields[idx]))) };
            if (size(failures) > 0)
                return markLocationFailed(c,exp@\loc,failures);
            else if ((size(relFields) - size(tl)) == 1)
                return markLocationType(c,exp@\loc,\list(last(relFields)));
            else
                return markLocationType(c,exp@\loc,\lrel(tail(relFields,size(relFields)-size(tl))));
        }
    } else if (isMapType(t1)) {
        if (size(tl) != 1)
            return markLocationFailed(c,exp@\loc,makeFailType("Expected only 1 subscript for a map expression, not <size(tl)>",exp@\loc));
        else if (!comparable(tl[0],getMapDomainType(t1)))
            return markLocationFailed(c,exp@\loc,makeFailType("Expected subscript of type <prettyPrintType(getMapDomainType(t1))>, not <prettyPrintType(tl[0])>",exp@\loc));
        else
            return markLocationType(c,exp@\loc,getMapRangeType(t1));
    } else if (isNodeType(t1)) {
        if (size(tl) != 1)
            return markLocationFailed(c,exp@\loc,makeFailType("Expected only 1 subscript for a node expression, not <size(tl)>",exp@\loc));
        else if (!isIntType(tl[0]))
            return markLocationFailed(c,exp@\loc,makeFailType("Expected subscript of type int, not <prettyPrintType(tl[0])>",exp@\loc));
        else
            return markLocationType(c,exp@\loc,\value());
    } else if (isTupleType(t1)) {
        if (size(tl) != 1)
            return markLocationFailed(c,exp@\loc,makeFailType("Expected only 1 subscript for a tuple expression, not <size(tl)>",exp@\loc));
        else if (!isIntType(tl[0]))
            return markLocationFailed(c,exp@\loc,makeFailType("Expected subscript of type int, not <prettyPrintType(tl[0])>",exp@\loc));
        else if ((Expression)`<DecimalIntegerLiteral dil>` := head(eslist)) {
        	tupleIndex = toInt("<dil>");
        	if (tupleIndex < 0 || tupleIndex >= size(getTupleFields(t1)))
        		return markLocationFailed(c,exp@\loc,makeFailType("Tuple index must be between 0 and <size(getTupleFields(t1))-1>",exp@\loc));
        	else
        		return markLocationType(c,exp@\loc,getTupleFields(t1)[tupleIndex]);
        } else
            return markLocationType(c,exp@\loc,lubList(getTupleFields(t1)));
    } else if (isStrType(t1)) {
        if (size(tl) != 1)
            return markLocationFailed(c,exp@\loc,makeFailType("Expected only 1 subscript for a string expression, not <size(tl)>",exp@\loc));
        else if (!isIntType(tl[0]))
            return markLocationFailed(c,exp@\loc,makeFailType("Expected subscript of type int, not <prettyPrintType(tl[0])>",exp@\loc));
        else
            return markLocationType(c,exp@\loc,\str());
    } else {
        return markLocationFailed(c,exp@\loc,makeFailType("Expressions of type <prettyPrintType(t1)> cannot be subscripted", exp@\loc));
    }
}

@doc{Check the types of Rascal expressions: Slice (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> [ <OptionalExpression ofirst> .. <OptionalExpression olast> ]`, Configuration c) {
    set[Symbol] failures = { };

    < c, t1 > = checkExp(e, c);
    
    if ((OptionalExpression)`<Expression efirst>` := ofirst) {
    	< c, t2 > = checkExp(efirst, c);
    	if (isFailType(t2)) failures += t2;
    	if (!isIntType(t2)) failures += makeFailType("The first slice index must be of type int", efirst@\loc);
    }
    
    if ((OptionalExpression)`<Expression elast>` := olast) {
    	< c, t3 > = checkExp(elast, c);
    	if (isFailType(t3)) failures += t3;
    	if (!isIntType(t3)) failures += makeFailType("The last slice index must be of type int", elast@\loc);
    }
    
    res = makeFailType("Slices can only be used on lists, strings, and nodes", exp@\loc);
    
	if (isListType(t1) || isStrType(t1)) {
		res = t1;	
	} else if (isNodeType(t1)) {
		res = \list(\value());
	}
	
	if (isFailType(res) || size(failures) > 0)
		return markLocationFailed(c, exp@\loc, failures + res);
	else
		return markLocationType(c, exp@\loc, res);
}

@doc{Check the types of Rascal expressions: Slice Step (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> [ <OptionalExpression ofirst>, <Expression second> .. <OptionalExpression olast> ]`, Configuration c) {
    set[Symbol] failures = { };

    < c, t1 > = checkExp(e, c);
	    
    if ((OptionalExpression)`<Expression efirst>` := ofirst) {
    	< c, t2 > = checkExp(efirst, c);
    	if (isFailType(t2)) failures += t2;
    	if (!isIntType(t2)) failures += makeFailType("The first slice index must be of type int", efirst@\loc);
    }
    
	< c, t3 > = checkExp(second, c);
	if (!isIntType(t3)) failures += makeFailType("The slice step must be of type int", second@\loc);
	    
    if ((OptionalExpression)`<Expression elast>` := olast) {
    	< c, t4 > = checkExp(elast, c);
    	if (isFailType(t4)) failures += t4;
    	if (!isIntType(t4)) failures += makeFailType("The last slice index must be of type int", elast@\loc);
    }

    res = makeFailType("Slices can only be used on lists, strings, and nodes", exp@\loc);
    
	if (isListType(t1) || isStrType(t1)) {
		res = t1;	
	} else if (isNodeType(t1)) {
		res = \list(\value());
	}
	
	if (isFailType(res) || size(failures) > 0)
		return markLocationFailed(c, exp@\loc, failures + res);
	else
		return markLocationType(c, exp@\loc, res);
}


@doc{Field names and types for built-ins}
private map[Symbol,map[str,Symbol]] fieldMap =
    ( \loc() :
        ( "scheme" : \str(), "authority" : \str(), "host" : \str(), "path" : \str(), "parent" : \str(),
          "file" : \str(), "ls" : \list(\loc()), "extension" : \str(), "fragment" : \str(), 
          "query" : \str(), "user" : \str(), "port" : \int(), "length" : \int(), "offset" : \int(), 
          "begin" : \tuple([\label("line",\int()),\label("column",\int())]), 
          "end" : \tuple([\label("line",\int()),\label("column",\int())]), "uri" : \str(), "top" : \loc()
        ),
      \datetime() :
        ( "year" : \int(), "month" : \int(), "day" : \int(), "hour" : \int(), "minute" : \int(), 
          "second" : \int(), "millisecond" : \int(), "timezoneOffsetHours" : \int(), 
          "timezoneOffsetMinutes" : \int(), "century" : \int(), "isDate" : \bool(), 
          "isTime" : \bool(), "isDateTime" : \bool(), "justDate" : \datetime(), "justTime" : \datetime()
        )
    );

private rel[Symbol,str] writableFields = ({ \loc() } * { "uri","scheme","authority","host","path","file","parent","extension","top","fragment","query","user","port","length","offset","begin","end" })
                                       + ({ \datetime() } * { "year", "month", "day", "hour", "minute", "second", "millisecond","timezoneOffsetHours", "timezoneOffsetMinutes" });
                                       
@doc{Check the types of Rascal expressions: Field Access (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> . <Name f>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    
    // If the type is a failure, we don't know how to look up the field name, so just return
    // right away.
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);

    return markLocationType(c, exp@\loc, computeFieldType(t1, convertName(f), exp@\loc, c));
}

@doc{Compute the type of field fn on type t1. A fail type is returned if the field is not defined on the given type.}
public Symbol computeFieldType(Symbol t1, RName fn, loc l, Configuration c) {
    fAsString = prettyPrintName(fn);
    if (isLocType(t1)) {
        if (fAsString in fieldMap[\loc()])
            return fieldMap[\loc()][fAsString];
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
    } else if (isDateTimeType(t1)) {
        if (fAsString in fieldMap[\datetime()])
            return fieldMap[\datetime()][fAsString];
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
    } else if (isRelType(t1)) {
        rt = getRelElementType(t1);
        if (tupleHasField(rt, fAsString))
            return \set(getTupleFieldType(rt, fAsString));
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
    } else if (isListRelType(t1)) {
        rt = getListRelElementType(t1);
        if (tupleHasField(rt, fAsString))
            return \list(getTupleFieldType(rt, fAsString));
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
    } else if (isMapType(t1)) {
        rt = getMapFieldsAsTuple(t1);
        if (tupleHasField(rt, fAsString))
            return getTupleFieldType(rt, fAsString);
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
	} else if (isReifiedType(t1)) {
		if (fAsString == "symbol") {
			typeName = RSimpleName("Symbol");
			if (typeName in c.typeEnv && c.store[c.typeEnv[typeName]] is datatype && isADTType(c.store[c.typeEnv[typeName]].rtype)) {
				return c.store[c.typeEnv[typeName]].rtype;			
			} else {
				return makeFailType("The type of field <fAsString>, <prettyPrintName(typeName)>, is not in scope", l);
			}
		} else {
			return makeFailType("Field <fAsString> does not exist on type type", l);
		}
    } else if (isADTType(t1)) {
        adtName = RSimpleName(getADTName(t1));
        if (adtName in c.typeEnv && c.store[c.typeEnv[adtName]] is datatype) {
	        if (<c.typeEnv[adtName],fAsString> notin c.adtFields)
	            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
	        else {
				adtId = c.typeEnv[adtName];
				originalType = c.store[adtId].rtype;
				originalParams = getADTTypeParameters(originalType);
				fieldType = c.adtFields[<c.typeEnv[adtName],fAsString>];
				if (size(originalParams) > 0) {
					actualParams = getADTTypeParameters(t1);
					if (size(originalParams) != size(actualParams)) {
						return makeFailType("Invalid ADT type, the number of type parameters is inconsistent", l);
					} else {
						bindings = ( getTypeVarName(originalParams[idx]) : actualParams[idx] | idx <- index(originalParams));
	                    try {
	                        fieldType = instantiate(fieldType, bindings);
	                    } catch : {
	                        return makeFailType("Failed to instantiate type parameters in field type", l);
	                    }						
					}
				}									        	
	            return fieldType;
			}
	    } else {
	    	return makeFailType("Cannot compute type of field <fAsString>, user type <prettyPrintType(t1)> has not been declared or is out of scope", l); 
	    }  
    } else if (isNonTerminalType(t1)) {
        nonterminalName = RSimpleName(getNonTerminalName(t1));
        if (nonterminalName in c.typeEnv && c.store[c.typeEnv[nonterminalName]] is sorttype) {
	        if (<c.typeEnv[nonterminalName],fAsString> notin c.nonterminalFields)
	            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
	        else
	            return c.nonterminalFields[<c.typeEnv[nonterminalName],fAsString>];
	    } else {
	    	return makeFailType("Cannot compute type of field <fAsString>, nonterminal type <prettyPrintType(t1)> has not been declared", l); 
	    }  
    } 
    else if (isStartNonTerminalType(t1)) {
        nonterminalName = RSimpleName("start[<getNonTerminalName(t1)>]");
         if (nonterminalName in c.typeEnv && c.store[c.typeEnv[nonterminalName]] is sorttype) {
        if (<c.typeEnv[nonterminalName],fAsString> notin c.nonterminalFields)
                return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
            else
                return c.nonterminalFields[<c.typeEnv[nonterminalName],fAsString>];
        }
        else {
            return makeFailType("Cannot compute type of field <fAsString>, nonterminal type <prettyPrintType(t1)> has not been declared", l);
        } 
    }else if (isTupleType(t1)) {
        if (tupleHasField(t1, fAsString))
            return getTupleFieldType(t1, fAsString);
        else
            return makeFailType("Field <fAsString> does not exist on type <prettyPrintType(t1)>", l);
    } 

    return makeFailType("Cannot access fields on type <prettyPrintType(t1)>", l);
}

@doc{Check the types of Rascal expressions: Field Update (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> [ <Name n> = <Expression er> ]`, Configuration c) {
    // TODO: Need to properly handle field updates for relations, which don't appear to work
    < c, t1 > = checkExp(e, c);
    < c, t2 > = checkExp(er, c);
    
    // If the type of e is a failure, we don't know how to look up the field name, so just return
    // right away. t2 may have failures as well, so include that in the failure marking.
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,{t1,t2});
    
    // Now get the field type. If this fails, return right away as well.
    ft = computeFieldType(t1, convertName(n), exp@\loc, c);
    if (isFailType(t2) || isFailType(ft)) return markLocationFailed(c,exp@\loc,{t2,ft});
    if ((isLocType(t1) || isDateTimeType(t1)) && getSimpleName(convertName(n)) notin writableFields[t1])
        return markLocationFailed(c,exp@\loc,makeFailType("Cannot update field <n> on type <prettyPrintType(t1)>",exp@\loc)); 

    // To assign, the type of er (t2) must be a subtype of the type of the field (ft)   
    if (!subtype(t2,ft)) return markLocationFailed(c,exp@\loc,makeFailType("Cannot assign type <prettyPrintType(t2)> into field of type <prettyPrintType(ft)>",exp@\loc));

    return markLocationType(c, exp@\loc, t1);
}

@doc{Check the types of Rascal expressions: Field Project (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> \< <{Field ","}+ fs> \>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    
    // If the type is a failure, we don't know how to look up the field name, so just return
    // right away.
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);

    // Get back the fields as a tuple, if this is one of the allowed subscripting types.
    Symbol rt = \void();
    if (isRelType(t1)) {
        rt = getRelElementType(t1);
    } else if (isListRelType(t1)) {
    	rt = getListRelElementType(t1);
    } else if (isMapType(t1)) {
        rt = getMapFieldsAsTuple(t1);
    } else if (isTupleType(t1)) {
        rt = t1;
    } else {
        return markLocationFailed(c, exp@\loc, makeFailType("Type <prettyPrintType(t1)> does not allow fields", exp@\loc));
    }
    
    // Find the field type and name for each index
    set[Symbol] failures = { };
    list[Symbol] subscripts = [ ];
    list[str] fieldNames = [ ];
    bool maintainFieldNames = tupleHasFieldNames(rt);
    
    for (f <- fs) {
        if ((Field)`<IntegerLiteral il>` := f) {
            int offset = toInt("<il>");
            if (!tupleHasField(rt, offset))
                failures += makeFailType("Field subscript <il> out of range", f@\loc);
            else {
                subscripts += getTupleFieldType(rt, offset);
                if (maintainFieldNames) fieldNames += getTupleFieldName(rt, offset);
            }
        } else if ((Field)`<Name fn>` := f) {
            fnAsString = "<fn>";
            if (!tupleHasField(rt, fnAsString)) {
                failures += makeFailType("Field <fn> does not exist", f@\loc);   // PK: was prettyPrintName(fn)
            } else {
                subscripts += getTupleFieldType(rt, fnAsString);
                if (maintainFieldNames) fieldNames += fnAsString;
            }
        } else {
            throw "Unhandled field case: <f>";
        }
    }
    
    if (size(failures) > 0) return markLocationFailed(c, exp@\loc, failures);

	// Keep the field names if all fields are named and if we have unique names
	if (size(subscripts) > 1 && size(subscripts) == size(fieldNames) && size(fieldNames) == size(toSet(fieldNames))) {
		subscripts = [ \label(fieldNames[idx],subscripts[idx]) | idx <- index(subscripts) ];
	}
	
    if (isRelType(t1)) {
        if (size(subscripts) > 1) return markLocationType(c, exp@\loc, \rel(subscripts));
        return markLocationType(c, exp@\loc, \set(head(subscripts)));
    } else if (isListRelType(t1)) {
        if (size(subscripts) > 1) return markLocationType(c, exp@\loc, \lrel(subscripts));
        return markLocationType(c, exp@\loc, \list(head(subscripts)));
    } else if (isMapType(t1)) {
        if (size(subscripts) > 1) return markLocationType(c, exp@\loc, \rel(subscripts));
        return markLocationType(c, exp@\loc, \set(head(subscripts)));
    } else if (isTupleType(t1)) {
        if (size(subscripts) > 1) return markLocationType(c, exp@\loc, \tuple(subscripts));
        return markLocationType(c, exp@\loc, head(subscripts));
    }   
}

@doc{Check the types of Rascal expressions: Set Annotation (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> [ @ <Name n> = <Expression er> ]`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    < c, t2 > = checkExp(er, c);

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isNodeType(t1) || isADTType(t1)) {
        aname = convertName(n);
        if (aname in c.annotationEnv, true in { subtype(t1,ot) | ot <- c.store[c.annotationEnv[aname]].onTypes }) {
            aType = c.store[c.annotationEnv[aname]].rtype;
            if (isFailType(aType)) {
                return markLocationFailed(c,exp@\loc,aType);
            } else {
                if (subtype(t2,aType)) {
                    return markLocationType(c,exp@\loc,t1);
                } else {
                    return markLocationFailed(c,exp@\loc,makeFailType("Cannot assign value of type <prettyPrintType(t2)> to annotation of type <prettyPrintType(aType)>", exp@\loc));
                }
            }
        } else {
            return markLocationFailed(c,exp@\loc,makeFailType("Annotation <n> not declared on <prettyPrintType(t1)> or its supertypes",exp@\loc));
        }
    } else {
        return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected node or ADT types, found <prettyPrintType(t1)>", e@\loc));
    }
}

@doc{Check the types of Rascal expressions: Get Annotation (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> @ <Name n>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    if (isNodeType(t1) || isADTType(t1)) {
        aname = convertName(n);
        if (aname in c.annotationEnv, true in { subtype(t1,ot) | ot <- c.store[c.annotationEnv[aname]].onTypes }) {
            aType = c.store[c.annotationEnv[aname]].rtype;
            if (isFailType(aType))
                return markLocationFailed(c,exp@\loc,aType);
            else
                return markLocationType(c,exp@\loc,aType);
        } else {
            return markLocationFailed(c,exp@\loc,makeFailType("Annotation <n> not declared on <prettyPrintType(t1)> or its supertypes",exp@\loc));
        }
    } else {
        return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected node or ADT types, found <prettyPrintType(t1)>", e@\loc));
    }
}

    @doc{Check the types of Rascal expressions: Is (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> is <Name n>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cIs = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cIs, t1 > = checkExp(e, cIs);
    c = needNewScope ? exitBooleanScope(cIs, c) : cIs;
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    if (isNodeType(t1) || isADTType(t1) || isNonTerminalType(t1)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected node or ADT types, found <prettyPrintType(t1)>", e@\loc));
}

@doc{Check the types of Rascal expressions: Has (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> has <Name n>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cHas = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cHas, t1 > = checkExp(e, cHas);
    c = needNewScope ? exitBooleanScope(cHas, c) : cHas;
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    if (isRelType(t1) || isListRelType(t1) || isTupleType(t1) || isADTType(t1)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected relation, tuple, or ADT types, found <prettyPrintType(t1)>", e@\loc));
}

@doc{Check the types of Rascal expressions: Transitive Closure (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> +`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);

	// Special case: if we have list[void] or set[void], these become lrel[void,void] and rel[void,void]
	if (isListType(t1) && isVoidType(getListElementType(t1)))
		return markLocationType(c,exp@\loc,makeListRelType([makeVoidType(),makeVoidType()]));
	if (isSetType(t1) && isVoidType(getSetElementType(t1)))
		return markLocationType(c,exp@\loc,makeRelType([makeVoidType(),makeVoidType()]));
		
	// Normal case: we have an actual list or relation
    if (isRelType(t1) || isListRelType(t1)) {
        list[Symbol] flds = isRelType(t1) ? getRelFields(t1) : getListRelFields(t1);
        if (size(flds) == 0) {
            return markLocationType(c,exp@\loc,t1);
        } else if (size(flds) == 2 && equivalent(flds[0],flds[1])) {    
            return markLocationType(c,exp@\loc,t1);
        } else {
            t1 = makeFailType("Invalid type: expected a binary relation over equivalent types, found <prettyPrintType(t1)>", e@\loc);
            return markLocationFailed(c,exp@\loc,t1);
        }
    } else {
        t1 = makeFailType("Invalid type: expected a binary relation, found <prettyPrintType(t1)>", e@\loc);
        return markLocationFailed(c,exp@\loc,t1);
    }
}

@doc{Check the types of Rascal expressions: Transitive Reflexive Closure (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> *`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);

	// Special case: if we have list[void] or set[void], these become lrel[void,void] and rel[void,void]
	if (isListType(t1) && isVoidType(getListElementType(t1)))
		return markLocationType(c,exp@\loc,makeListRelType([makeVoidType(),makeVoidType()]));
	if (isSetType(t1) && isVoidType(getSetElementType(t1)))
		return markLocationType(c,exp@\loc,makeRelType([makeVoidType(),makeVoidType()]));
		
	// Normal case: we have an actual list or relation
    if (isRelType(t1) || isListRelType(t1)) {
        list[Symbol] flds = isRelType(t1) ? getRelFields(t1) : getListRelFields(t1);
        if (size(flds) == 0) {
            return markLocationType(c,exp@\loc,t1);
        } else if (size(flds) == 2 && equivalent(flds[0],flds[1])) {    
            return markLocationType(c,exp@\loc,t1);
        } else {
            t1 = makeFailType("Invalid type: expected a binary relation over equivalent types, found <prettyPrintType(t1)>", e@\loc);
            return markLocationFailed(c,exp@\loc,t1);
        }
    } else {
        t1 = makeFailType("Invalid type: expected a binary relation, found <prettyPrintType(t1)>", e@\loc);
        return markLocationFailed(c,exp@\loc,t1);
    }
}

@doc{Check the types of Rascal expressions: Is Defined (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e> ?`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cIsDef = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cIsDef, t1 > = checkExp(e, cIsDef);
    c = needNewScope ? exitBooleanScope(cIsDef,c) : cIsDef;
    if (isFailType(t1)) return markLocationFailed(c,exp@\loc,t1);
    return markLocationType(c,exp@\loc,\bool());
}

@doc{Check the types of Rascal expressions: Negation (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`! <Expression e>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cNeg = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cNeg, t1 > = checkExp(e, cNeg);
    c = needNewScope ? exitBooleanScope(cNeg,c) : cNeg;
    if (isFailType(t1)) return markLocationFailed(c, exp@\loc, t1);
    if (isBoolType(t1)) return markLocationType(c,exp@\loc,t1);
    return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected bool, found <prettyPrintType(t1)>", e@\loc));
}

@doc{Check the types of Rascal expressions: Negative (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`- <Expression e>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) return markLocationFailed(c, exp@\loc, t1);
    if (isIntType(t1) || isRealType(t1) || isRatType(t1) || isNumType(t1)) return markLocationType(c,exp@\loc,t1);
    return markLocationFailed(c,exp@\loc,makeFailType("Invalid type: expected numeric type, found <prettyPrintType(t1)>", e@\loc));
}

@doc{Check the types of Rascal expressions: Splice (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`* <Expression e>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1)) return markLocationFailed(c, exp@\loc, t1);
    if (isListType(t1)) return markLocationType(c, exp@\loc, getListElementType(t1));
    if (isSetType(t1)) return markLocationType(c, exp@\loc, getSetElementType(t1));
    if (isBagType(t1)) return markLocationType(c, exp@\loc, getBagElementType(t1));
    if (isRelType(t1)) return markLocationType(c, exp@\loc, getRelElementType(t1));
    if (isListRelType(t1)) return markLocationType(c, exp@\loc, getListRelElementType(t1));
    return markLocationType(c, exp@\loc, t1);
}

@doc{Check the types of Rascal expressions: AsType (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`[ <Type t> ] <Expression e>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    
    // TODO: Currently the interpreter verifies this is a non-terminal type, but this just
    // shows up in the type system as being another ADT. Should we keep a separate non-terminal
    // type, or somehow mark the ADT to indicate it is produced from a non-terminal? This could
    // also be done by making an entry in the symbol table, but leaving the type alone...
    < c, rt > = convertAndExpandType(t,c);
    
    set[Symbol] failures = { };
    if (\sort(_) !:= rt) failures += makeFailType("Expected non-terminal type, instead found <prettyPrintType(rt)>", t@\loc);
    if (!isFailType(t1) && !isStrType(t1)) failures += makeFailType("Expected str, instead found <prettyPrintType(t1)>", e@\loc);
    if (isFailType(t1)) failures += t1;

    if (size(failures) > 0) return markLocationFailed(c, exp@\loc, collapseFailTypes(failures));
    return markLocationType(c, exp@\loc, rt);   
}

@doc{Check the types of Rascal expressions: Composition (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> o <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});

	// Special handling for list[void] and set[void], these should be treated as lrel[void,void]
	// and rel[void,void], respectively
	if (isListType(t1) && isVoidType(getListElementType(t1))) t1 = makeListRelType(makeVoidType(),makeVoidType());
	if (isListType(t2) && isVoidType(getListElementType(t2))) t2 = makeListRelType(makeVoidType(),makeVoidType());
	if (isSetType(t1) && isVoidType(getSetElementType(t1))) t1 = makeRelType(makeVoidType(),makeVoidType());
	if (isSetType(t2) && isVoidType(getSetElementType(t2))) t2 = makeRelType(makeVoidType(),makeVoidType());
	
	
    if (isMapType(t1) && isMapType(t2)) {
        if (subtype(getMapRangeType(t1),getMapDomainType(t2))) {
            return markLocationType(c, exp@\loc, makeMapType(stripLabel(getMapDomainType(t1)),stripLabel(getMapRangeType(t2))));
        } else {
            return markLocationFailed(c, exp@\loc, makeFailType("<prettyPrintType(getMapRangeType(t1))> must be a subtype of <prettyPrintType(getMapDomainType(t2))>", exp@\loc));
        }
    }
    
    if (isRelType(t1) && isRelType(t2)) {
        list[Symbol] lflds = getRelFields(t1);
        list[Symbol] rflds = getRelFields(t2);
        set[Symbol] failures = { };
        if (size(lflds) != 0 && size(lflds) != 2)
            failures += makeFailType("Relation <prettyPrintType(t1)> should have arity of 0 or 2", e1@\loc); 
        if (size(rflds) != 0 && size(rflds) != 2)
            failures += makeFailType("Relation <prettyPrintType(t2)> should have arity of 0 or 2", e2@\loc);
        if (!comparable(lflds[1],rflds[0]))
            failures += makeFailType("Range of relation <prettyPrintType(t1)> must be comparable to domain of relation <prettyPrintType(t1)>", exp@\loc);
        if (size(failures) > 0) return markLocationFailed(c, exp@\loc, failures);
        if (size(lflds) == 0 || size(rflds) == 0)
            return markLocationType(c, exp@\loc, \rel([]));
        else
            return markLocationType(c, exp@\loc, \rel([lflds[0],rflds[1]])); 
    }

    if (isListRelType(t1) && isListRelType(t2)) {
        list[Symbol] lflds = getListRelFields(t1);
        list[Symbol] rflds = getListRelFields(t2);
        set[Symbol] failures = { };
        if (size(lflds) != 0 && size(lflds) != 2)
            failures += makeFailType("List relation <prettyPrintType(t1)> should have arity of 0 or 2", e1@\loc); 
        if (size(rflds) != 0 && size(rflds) != 2)
            failures += makeFailType("List relation <prettyPrintType(t2)> should have arity of 0 or 2", e2@\loc);
        if (!comparable(lflds[1],rflds[0]))
            failures += makeFailType("Range of list relation <prettyPrintType(t1)> must be comparable to domain of list relation <prettyPrintType(t1)>", exp@\loc);
        if (size(failures) > 0) return markLocationFailed(c, exp@\loc, failures);
        if (size(lflds) == 0 || size(rflds) == 0)
            return markLocationType(c, exp@\loc, \lrel([]));
        else
            return markLocationType(c, exp@\loc, \lrel([lflds[0],rflds[1]])); 
    }

    if (isFunctionType(t1) && isFunctionType(t2)) {
        compositeArgs = getFunctionArgumentTypes(t2);
        compositeRet = getFunctionReturnType(t1);
        linkingArgs = getFunctionArgumentTypes(t1);
        
        // For f o g, f should have exactly one formal parameter
        if (size(linkingArgs) != 1) {
        	ft = makeFailType("In a composition of two functions the leftmost function must have exactly one formal parameter.", exp@\loc);
        	return markLocationFailed(c, exp@\loc, ft);
        }
        
        // and, that parameter must be of a type that a call with the return type of g would succeed
        linkingArg = linkingArgs[0];
        rightReturn = getFunctionReturnType(t2);
        if (!subtype(rightReturn, linkingArg)) {
        	ft = makeFailType("The return type of the right-hand function, <prettyPrintType(rightReturn)>, cannot be passed to the left-hand function, which expects type <prettyPrintType(linkingArg)>", exp@\loc);
			return markLocationFailed(c, exp@\loc, ft);        	 
        }
        
        // If both of those pass, the result type is a function with the args of t2 and the return type of t1
		rt = Symbol::\func(compositeRet, compositeArgs);
		return markLocationType(c, exp@\loc, rt);         
    }
    
    // Here, one or both types are overloaded functions, with at most one a normal function.
    if ((isOverloadedType(t1) || isFunctionType(t1)) && (isOverloadedType(t2) || isFunctionType(t2))) {
    	// Step 1: get back all the type possibilities on the left and right
    	leftFuns = (isFunctionType(t1)) ? { t1 } : (getNonDefaultOverloadOptions(t1) + getDefaultOverloadOptions(t1));
    	rightFuns = (isFunctionType(t2)) ? { t2 } : (getNonDefaultOverloadOptions(t2) + getDefaultOverloadOptions(t2));
    	
    	// Step 2: filter out leftmost functions that cannot be used in compositions
    	leftFuns = { f | f <- leftFuns, size(getFunctionArgumentTypes(f)) == 1 };
    	
    	// Step 3: combine the ones we can -- the return of the rightmost type has to be allowed
    	// as the parameter for the leftmost type
    	newFunTypes = { Symbol::\func(getFunctionReturnType(lf), getFunctionArgumentTypes(rf)) |
    		rf <- rightFuns, lf <- leftFuns, subtype(getFunctionReturnType(rf),getFunctionArgumentTypes(lf)[0]) };
    		
    	// Step 4: If we get an empty set, fail; if we get just 1, return that; if we get multiple possibilities,
    	// return an overloaded type
    	if (size(newFunTypes) == 0) {
    		ft = makeFailType("The functions cannot be composed", exp@\loc);
    		return markLocationFailed(c, exp@\loc, ft);
    	} else if (size(newFunTypes) == 1) {
    		return markLocationType(c, exp@\loc, getOneFrom(newFunTypes));
    	} else {
    		// TODO: Do we need to keep track of defaults through all this? If so, do we compose default
    		// and non-default functions?
    		return markLocationType(c, exp@\loc, \overloaded(newFunTypes,{}));
    	}
    }

    return markLocationFailed(c, exp@\loc, makeFailType("Composition not defined for <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: Product (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> * <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    return markLocationType(c,exp@\loc,computeProductType(t1,t2,exp@\loc));
}

Symbol computeProductType(Symbol t1, Symbol t2, loc l) {
    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2))
        return numericArithTypes(t1, t2);
    
    if (isListType(t1) && isListType(t2))
        return \list(\tuple([getListElementType(t1),getListElementType(t2)]));
    if (isRelType(t1) && isRelType(t2))
        return \rel([getRelElementType(t1),getRelElementType(t2)]);
    if (isListRelType(t1) && isListRelType(t2))
        return \lrel([getListRelElementType(t1),getListRelElementType(t2)]);
    if (isSetType(t1) && isSetType(t2))
        return \rel([getSetElementType(t1),getSetElementType(t2)]);
    
    return makeFailType("Product not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l);
}

@doc{Check the types of Rascal expressions: Join}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> join <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
	
	    Symbol stripLabel(Symbol t) = (\label(s,lt) := t) ? stripLabel(lt) : t;
	
    if ((isRelType(t1) && isRelType(t2)) || (isListRelType(t1) && isListRelType(t2))) {
        list[Symbol] lflds = isRelType(t1) ? getRelFields(t1) : getListRelFields(t1);
        list[Symbol] rflds = isRelType(t2) ? getRelFields(t2) : getListRelFields(t2);
        
        // If possible, we want to maintain the field names; check here to see if that
        // is possible. We can when 1) both relations use field names, and 2) the names
        // used are distinct.
        list[str] llabels = [ s | \label(s,_) <- lflds ];
        list[str] rlabels = [ s | \label(s,_) <- rflds ];
        set[str] labelSet = toSet(llabels) + toSet(rlabels);
        if (size(llabels) == size(lflds) && size(rlabels) == size(rflds) && size(labelSet) == size(llabels) + size(rlabels)) {
        	rt = isRelType(t1) ? \rel(lflds+rflds) : \lrel(lflds+rflds);
        	return markLocationType(c, exp@\loc, rt);
        } else {
        	rt = isRelType(t1) ? \rel([ stripLabel(t) | t <- (lflds+rflds) ]) : \lrel([ stripLabel(t) | t <- (lflds+rflds) ]); 
        	return markLocationType(c, exp@\loc, rt);
        }
    }

	if (isRelType(t1) && isSetType(t2))
		return markLocationType(c, exp@\loc, \rel( [ stripLabel(t) | t <- getRelFields(t1) ] + getSetElementType(t2) ));
	
	if (isSetType(t1) && isRelType(t2))
		return markLocationType(c, exp@\loc, \rel( getSetElementType(t1) + [ stripLabel(t) | t <- getRelFields(t2) ] ));
	
	if (isListRelType(t1) && isListType(t2))
		return markLocationType(c, exp@\loc, \lrel( [ stripLabel(t) | t <- getListRelFields(t1) ] + getListElementType(t2) ));
	
	if (isListType(t1) && isListRelType(t2))
		return markLocationType(c, exp@\loc, \lrel( getListElementType(t1) + [ stripLabel(t) | t <- getListRelFields(t2) ] ));
	
	if (isListType(t1) && isListType(t2))
		return markLocationType(c, exp@\loc, \lrel([ getListElementType(t1), getListElementType(t2) ]));
	
	if (isSetType(t1) && isSetType(t2))
		return markLocationType(c, exp@\loc, \rel([ getSetElementType(t1), getSetElementType(t2) ]));
	
    return markLocationFailed(c, exp@\loc, makeFailType("Join not defined for <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: Remainder (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> % <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isIntType(t1) && isIntType(t2)) return markLocationType(c,exp@\loc,\int());
    return markLocationFailed(c,exp@\loc,makeFailType("Remainder not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>",exp@\loc));
}

@doc{Check the types of Rascal expressions: Division (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> / <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    return markLocationType(c,exp@\loc,computeDivisionType(t1,t2,exp@\loc));
}

Symbol computeDivisionType(Symbol t1, Symbol t2, loc l) {
    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2))
        return numericArithTypes(t1, t2);
    return makeFailType("Division not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l);
}

@doc{Check the types of Rascal expressions: Intersection (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> & <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    < c, itype > = computeIntersectionType(c,t1,t2,exp@\loc);
    return markLocationType(c,exp@\loc,itype);
}

CheckResult computeIntersectionType(Configuration c, Symbol t1, Symbol t2, loc l) {
    if ( ( isListRelType(t1) && isListRelType(t2) ) || 
         ( isListType(t1) && isListType(t2) ) || 
         ( isRelType(t1) && isRelType(t2) ) || 
         ( isSetType(t1) && isSetType(t2) ) || 
         ( isMapType(t1) && isMapType(t2) ) )
	{
    	if (!comparable(t1,t2))
    		c = addScopeWarning(c, "Types <prettyPrintType(t1)> and <prettyPrintType(t2)> are not comparable", l);
    		
    	if (subtype(t2, t1))
    		return < c, t2 >;
    		
    	if (subtype(t1, t2))
    		return < c, t1 >;
    		
    	if (isListRelType(t1)) return < c, makeListRelType(makeVoidType(),makeVoidType()) >;
    	if (isListType(t1)) return < c, makeListType(makeVoidType()) >;
    	if (isRelType(t1)) return < c, makeRelType(makeVoidType(), makeVoidType()) >;
    	if (isSetType(t1)) return < c, makeSetType(makeVoidType()) >;
    	if (isMapType(t1)) return < c, makeMapType(makeVoidType(),makeVoidType()) >;
    }
    return < c, makeFailType("Intersection not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l) >;
}

@doc{Check the types of Rascal expressions: Addition (DONE)}
// TODO: Currently, this isn't parsing right: 1 + [2] doesn't match this
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> + <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    return markLocationType(c,exp@\loc,computeAdditionType(t1,t2,exp@\loc));
}

@doc{General function to calculate the type of an addition.}
Symbol computeAdditionType(Symbol t1, Symbol t2, loc l) {
    // Numbers
    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2))
        return numericArithTypes(t1, t2);
    
    // Other non-containers
    if (isStrType(t1) && isStrType(t2))
        return \str();
    if (isBoolType(t1) && isBoolType(t2))
        return \bool();
    if (isLocType(t1) && isLocType(t2))
        return \loc();
    if (isLocType(t1) && isStrType(t2))
        return \loc();
        
    if (isTupleType(t1) && isTupleType(t2)) {
    	if (tupleHasFieldNames(t1) && tupleHasFieldNames(t2)) {
	    	tflds1 = getTupleFields(t1);
	    	tflds2 = getTupleFields(t2);
	    	if (size(toSet(tflds1)+toSet(tflds2)) == size(tflds1+tflds2)) {
	    		return \tuple(tflds1+tflds2);
	    	} else {
	    		return \tuple(getTupleFieldTypes(t1) + getTupleFieldTypes(t2));
	    	}
		} else {    	
        	return \tuple(getTupleFieldTypes(t1) + getTupleFieldTypes(t2));
		}
    }
                
    if (isListType(t1) && isListType(t2))
        return lub(t1,t2);
    if (isSetType(t1) && isSetType(t2))
        return lub(t1,t2);
    if (isMapType(t1) && isMapType(t2))
        return lub(t1,t2);
    
    if (isListType(t1) && !isContainerType(t2))
        return \list(lub(getListElementType(t1),t2));
    if (isSetType(t1) && !isContainerType(t2)) // Covers relations too
        return \set(lub(getSetElementType(t1),t2));
    if (isBagType(t1) && !isContainerType(t2))
        return \bag(lub(getBagElementType(t1),t2));
        
    if (isListType(t2) && !isContainerType(t1))
        return \list(lub(t1,getListElementType(t2)));
    if (isSetType(t2) && !isContainerType(t1)) // Covers relations too
        return \set(lub(t1,getSetElementType(t2)));
    if (isBagType(t2) && !isContainerType(t1))
        return \bag(lub(t1,getBagElementType(t2)));
        
    if (isListType(t1))
        return \list(lub(getListElementType(t1),t2));
    if (isSetType(t1)) // Covers relations too
        return \set(lub(getSetElementType(t1),t2));
    if (isBagType(t1))
        return \bag(lub(getBagElementType(t1),t2));
        
	// If we are adding together two functions, this creates an overloaded
	// type with the two items as non-defaults.
	// TODO: If we need to track default status here as well, we will need
	// to special case plus to handle f + g, where f and g are both function
	// names, and catch this before evaluating them both and retrieving their
	// types.
	// TODO: Can we also add together constructor types?
	if (isFunctionType(t1) && isFunctionType(t2))
		return \overloaded({t1,t2},{});
	else if (\overloaded(nd1,d1) := t1 && \overloaded(nd2,d2) := t2)
		return \overloaded(nd1+nd2,d1+d2);
	else if (\overloaded(nd1,d1) := t1 && isFunctionType(t2))
		return \overloaded(nd1+t2,d1);
	else if (isFunctionType(t1) && \overloaded(nd2,d2) := t2)
		return \overloaded(nd2+t1,d2);
		
    return makeFailType("Addition not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l);
}

@doc{Check the types of Rascal expressions: Subtraction (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> - <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    < c, stype > = computeSubtractionType(c, t1, t2, exp@\loc);
    return markLocationType(c,exp@\loc,stype);
}

public CheckResult computeSubtractionType(Configuration c, Symbol t1, Symbol t2, loc l) {
    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2))
        return <c, numericArithTypes(t1, t2)>;

    if (isListType(t1) && isListType(t2)) {
		if (!comparable(getListElementType(t1),getListElementType(t2)))
			c = addScopeWarning(c, "<isListRelType(t1) ? "List Relation" : "List"> of type <prettyPrintType(t1)> could never contain elements of second <isListRelType(t2) ? "List Relation" : "List"> type <prettyPrintType(t2)>", l); 
		return < c, t1 >;
    }
    
    if (isListType(t1)) {
        if(!comparable(getListElementType(t1),t2))
		   c = addScopeWarning(c, "<isListRelType(t1) ? "List Relation" : "List"> of type <prettyPrintType(t1)> could never contain elements of type <prettyPrintType(t2)>", l); 
		return < c, t1 >;
    }
    
    if (isSetType(t1) && isSetType(t2)) {
		if (!comparable(getSetElementType(t1),getSetElementType(t2)))
			c = addScopeWarning(c, "<isRelType(t1) ? "Relation" : "Set"> of type <prettyPrintType(t1)> could never contain elements of second <isRelType(t2) ? "Relation" : "Set"> type <prettyPrintType(t2)>", l); 
        return < c, t1 >;
    }
    
    if (isSetType(t1)) {
        if(!comparable(getSetElementType(t1),t2))
		   c = addScopeWarning(c, "<isRelType(t1) ? "Relation" : "Set"> of type <prettyPrintType(t1)> could never contain elements of type <prettyPrintType(t2)>", l); 
        return < c, t1 >;
    }
    
    if (isBagType(t1) && isBagType(t2)) {
		if (!comparable(getBagElementType(t1),getBagElementType(t2)))
			c = addScopeWarning(c, "Bag of type <prettyPrintType(t1)> could never contain elements of second bag type <prettyPrintType(t2)>", l); 
        return < c, t1 >;
    }
    
    if (isBagType(t1)) {
        if(!comparable(getBagElementType(t1),t2))
		   c = addScopeWarning(c, "Bag of type <prettyPrintType(t1)> could never contain elements of type <prettyPrintType(t2)>", l); 
        return < c, t1 >;
    }

    if (isMapType(t1)) {
        if (!comparable(t1,t2))
            c = addScopeWarning(c, "Map of type <prettyPrintType(t1)> could never contain a sub-map of type <prettyPrintType(t2)>", l); 
        return < c, t1 >;
    }

    return < c, makeFailType("Subtraction not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l) >;
}

@doc{Check the types of Rascal expressions: AppendAfter (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \<\< <Expression e2>`, Configuration c) {
    // TODO: Revisit once this feature has been implemented
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    throw "Not implemented";
}

@doc{Check the types of Rascal expressions: InsertBefore (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \>\> <Expression e2>`, Configuration c) {
    // TODO: Revisit once this feature has been implemented
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    throw "Not implemented";
}

@doc{Check the types of Rascal expressions: Modulo (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> mod <Expression e2>`, Configuration c) {
    < c, t1 > = checkExp(e1, c);
    < c, t2 > = checkExp(e2, c);
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isIntType(t1) && isIntType(t2)) return markLocationType(c,exp@\loc,\int());
    return markLocationFailed(c,exp@\loc,makeFailType("Modulo not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>",exp@\loc));
}

@doc{Check the types of Rascal expressions: Not In (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> notin <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cNotIn = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cNotIn, t1 > = checkExp(e1, cNotIn);
    < cNotIn, t2 > = checkExp(e2, cNotIn);
    c = needNewScope ? exitBooleanScope(cNotIn,c) : cNotIn;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isRelType(t2)) {
        et = getRelElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isSetType(t2)) {
        et = getSetElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isMapType(t2)) {
        et = getMapDomainType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with domain type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isListRelType(t2)) {
        et = getListRelElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isListType(t2)) {
        et = getListElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    }
    return markLocationFailed(c,exp@\loc,makeFailType("notin not defined for <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: In (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> in <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cIn = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cIn, t1 > = checkExp(e1, cIn);
    < cIn, t2 > = checkExp(e2, cIn);
    c = needNewScope ? exitBooleanScope(cIn,c) : cIn;
    
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isRelType(t2)) {
        et = getRelElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isSetType(t2)) {
        et = getSetElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    } else if (isMapType(t2)) {
        et = getMapDomainType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with domain type of <prettyPrintType(t2)>",exp@\loc));
   } else if (isListRelType(t2)) {
        et = getListRelElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
     } else if (isListType(t2)) {
        et = getListElementType(t2);
        if (comparable(t1,et))
            return markLocationType(c,exp@\loc,\bool());
        else
            return markLocationFailed(c,exp@\loc,makeFailType("Cannot compare <prettyPrintType(t1)> with element type of <prettyPrintType(t2)>",exp@\loc));
    }
    return markLocationFailed(c,exp@\loc,makeFailType("in not defined for <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: Greater Than or Equal (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \>= <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cGtEq = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cGtEq, t1 > = checkExp(e1, cGtEq);
    < cGtEq, t2 > = checkExp(e2, cGtEq);
    c = needNewScope ? exitBooleanScope(cGtEq,c) : cGtEq;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});

    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2)) {
        return markLocationType(c,exp@\loc,\bool());
    }
    
    if (isDateTimeType(t1) && isDateTimeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isBoolType(t1) && isBoolType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isListRelType(t1) && isListRelType(t2) && comparable(getListRelElementType(t1),getListRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isListType(t1) && isListType(t2) && comparable(getListElementType(t1),getListElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isMapType(t1) && isMapType(t2) && comparable(getMapDomainType(t1),getMapDomainType(t2)) && comparable(getMapRangeType(t1),getMapRangeType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isNodeType(t1) && isNodeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isRelType(t1) && isRelType(t2) && comparable(getRelElementType(t1),getRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isSetType(t1) && isSetType(t2) && comparable(getSetElementType(t1),getSetElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isStrType(t1) && isStrType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isTupleType(t1) && isTupleType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isLocType(t1) && isLocType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isValueType(t1) || isValueType(t2))
        return markLocationType(c,exp@\loc,\bool());
        
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

@doc{Check the types of Rascal expressions: Less Than or Equal (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \<= <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cLtEq = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cLtEq, t1 > = checkExp(e1, cLtEq);
    < cLtEq, t2 > = checkExp(e2, cLtEq);
    c = needNewScope ? exitBooleanScope(cLtEq,c) : cLtEq;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});

    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2)) {
        return markLocationType(c,exp@\loc,\bool());
    }
    
    if (isDateTimeType(t1) && isDateTimeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isBoolType(t1) && isBoolType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isListRelType(t1) && isListRelType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isListType(t1) && isListType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isMapType(t1) && isMapType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isNodeType(t1) && isNodeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isRelType(t1) && isRelType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isSetType(t1) && isSetType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isStrType(t1) && isStrType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isTupleType(t1) && isTupleType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isLocType(t1) && isLocType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isValueType(t1) || isValueType(t2))
        return markLocationType(c,exp@\loc,\bool());
        
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

@doc{Check the types of Rascal expressions: Less Than (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \< <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cLt = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cLt, t1 > = checkExp(e1, cLt);
    < cLt, t2 > = checkExp(e2, cLt);
    c = needNewScope ? exitBooleanScope(cLt,c) : cLt;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});

    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2)) {
        return markLocationType(c,exp@\loc,\bool());
    }
    
    if (isDateTimeType(t1) && isDateTimeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isBoolType(t1) && isBoolType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isListRelType(t1) && isListRelType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isListType(t1) && isListType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isMapType(t1) && isMapType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isNodeType(t1) && isNodeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isRelType(t1) && isRelType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isSetType(t1) && isSetType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isStrType(t1) && isStrType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isTupleType(t1) && isTupleType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isLocType(t1) && isLocType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isValueType(t1) || isValueType(t2))
        return markLocationType(c,exp@\loc,\bool());
        
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

@doc{Check the types of Rascal expressions: Greater Than (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \> <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cGt = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cGt, t1 > = checkExp(e1, cGt);
    < cGt, t2 > = checkExp(e2, cGt);
    c = needNewScope ? exitBooleanScope(cGt,c) : cGt;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});

    if (subtype(t1, \num()) && subtype(t2, \num()) && !isVoidType(t1) && !isVoidType(t2)) {
        return markLocationType(c,exp@\loc,\bool());
    }
    
    if (isDateTimeType(t1) && isDateTimeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isBoolType(t1) && isBoolType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isListRelType(t1) && isListRelType(t2) && comparableOrNum(getListRelElementType(t1),getListRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isListType(t1) && isListType(t2) && comparableOrNum(getListElementType(t1),getListElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isMapType(t1) && isMapType(t2) && comparableOrNum(getMapDomainType(t1),getMapDomainType(t2)) && comparableOrNum(getMapRangeType(t1),getMapRangeType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isNodeType(t1) && isNodeType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isRelType(t1) && isRelType(t2) && comparableOrNum(getRelElementType(t1),getRelElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isSetType(t1) && isSetType(t2) && comparableOrNum(getSetElementType(t1),getSetElementType(t2)))
        return markLocationType(c,exp@\loc,\bool());
    if (isStrType(t1) && isStrType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isTupleType(t1) && isTupleType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isLocType(t1) && isLocType(t2))
        return markLocationType(c,exp@\loc,\bool());
    if (isValueType(t1) || isValueType(t2))
        return markLocationType(c,exp@\loc,\bool());
        
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

private bool isNumericType(Symbol t) = isIntType(t) || isRealType(t) || isRatType(t) || isNumType(t);

@doc{Check the types of Rascal expressions: Equals (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> == <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cEq = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cEq, t1 > = checkExp(e1, cEq);
    < cEq, t2 > = checkExp(e2, cEq);
    c = needNewScope ? exitBooleanScope(cEq,c) : cEq;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (comparable(t1,t2)) return markLocationType(c,exp@\loc,\bool());
    if (isNumericType(t1) && isNumericType(t2)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

@doc{Check the types of Rascal expressions: Non Equals (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> != <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cNeq = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cNeq, t1 > = checkExp(e1, cNeq);
    < cNeq, t2 > = checkExp(e2, cNeq);
    c = needNewScope ? exitBooleanScope(cNeq,c) : cNeq;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (comparable(t1,t2)) return markLocationType(c,exp@\loc,\bool());
    if (isNumericType(t1) && isNumericType(t2)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("<prettyPrintType(t1)> and <prettyPrintType(t2)> incomparable", exp@\loc));
}

@doc{Check the types of Rascal expressions: If Defined Otherwise (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> ? <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cIfDef = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cIfDef, t1 > = checkExp(e1, cIfDef);
    < cIfDef, t2 > = checkExp(e2, cIfDef);
    c = needNewScope ? exitBooleanScope(cIfDef,c) : cIfDef;

    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    return markLocationType(c,exp@\loc,lub(t1,t2));
}

@doc{Check the types of Rascal expressions: No Match (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Pattern p> !:= <Expression e>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cNoMatch = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cNoMatch, t1 > = checkExp(e, cNoMatch);
    if (isFailType(t1)) {
        c = needNewScope ? exitBooleanScope(cNoMatch,c) : cNoMatch;
        return markLocationFailed(c, exp@\loc, t1);
    }

    < cNoMatch, t2 > = calculatePatternType(p, cNoMatch, t1);
    c = needNewScope ? exitBooleanScope(cNoMatch,c) : cNoMatch;
    
    if (isFailType(t2)) return markLocationFailed(c, exp@\loc, t2);
    return markLocationType(c, exp@\loc, \bool());
}

@doc{Check the types of Rascal expressions: Match (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Pattern p> := <Expression e>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cMatch = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cMatch, t1 > = checkExp(e, cMatch);
    if (isFailType(t1)) {
        c = needNewScope ? exitBooleanScope(cMatch,c) : cMatch;
        return markLocationFailed(c, exp@\loc, t1);
    }

    < cMatch, t2 > = calculatePatternType(p, cMatch, t1);
    c = needNewScope ? exitBooleanScope(cMatch,c) : cMatch;
    
    if (isFailType(t2)) return markLocationFailed(c, exp@\loc, t2);
    return markLocationType(c, exp@\loc, \bool());
}

@doc{Check the types of Rascal expressions: Enumerator}
public CheckResult checkExp(Expression exp:(Expression)`<Pattern p> \<- <Expression e>`, Configuration c) {
    // TODO: For concrete lists, what should we use as the type?
    // TODO: For nodes, ADTs, and tuples, would it be better to use the lub of all the possible types?
    needNewScope = !inBooleanScope(c);
    cEnum = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cEnum, t1 > = checkExp(e, cEnum);
    if (isFailType(t1)) {
        c = needNewScope ? exitBooleanScope(cEnum, c) : cEnum;
        return markLocationFailed(c, exp@\loc, t1);
    }
    Symbol t2 = \void();
    if (isSetType(t1))
        < cEnum, t2 > = calculatePatternType(p, cEnum, getSetElementType(t1));
    else if (isListType(t1))
        < cEnum, t2 > = calculatePatternType(p, cEnum, getListElementType(t1));
    else if (isMapType(t1))
        < cEnum, t2 > = calculatePatternType(p, cEnum, getMapDomainType(t1));
    else if (isADTType(t1) || isTupleType(t1) || isNodeType(t1))
        < cEnum, t2 > = calculatePatternType(p, cEnum, \value());
    else if (\iter(st:\sort(_)) := t1)
    	< cEnum, t2 > = calculatePatternType(p, cEnum, st);
    else {
        t2 = makeFailType("Type <prettyPrintType(t1)> is not enumerable", exp@\loc);
    }
    c = needNewScope ? exitBooleanScope(cEnum, c) : cEnum;
    
    if (isFailType(t2)) return markLocationFailed(c, exp@\loc, t2);
    return markLocationType(c, exp@\loc, \bool());
}

@doc{Check the types of Rascal expressions: Implication (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> ==\> <Expression e2>`, Configuration c) {
	return checkBooleanOpsWithMerging(exp, e1, e2, "Logical implication", c);
}

@doc{Check the types of Rascal expressions: Equivalence (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> \<==\> <Expression e2>`, Configuration c) {
	return checkBooleanOpsWithMerging(exp, e1, e2, "Logical equivalence", c);
}

@doc{Check the types of Rascal expressions: And (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> && <Expression e2>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cAnd = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cAnd, t1 > = checkExp(e1, cAnd);
    < cAnd, t2 > = checkExp(e2, cAnd);
    c = needNewScope ? exitBooleanScope(cAnd,c) : cAnd;
    if (isFailType(t1) || isFailType(t2)) return markLocationFailed(c,exp@\loc,{t1,t2});
    if (isBoolType(t1) && isBoolType(t2)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("Logical and not defined for types <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: Or (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> || <Expression e2>`, Configuration c) {
	return checkBooleanOpsWithMerging(exp, e1, e2, "Logical or", c);
}

@doc{Handle merging logic for checking boolean operations}
public CheckResult checkBooleanOpsWithMerging(Expression exp, Expression e1, Expression e2, str opname, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cOr = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    failures = { };

    // The left and right branches are evaluated in their own environments,
    // since we want to make sure to control the propagation of names created
    // in the branches. This is because the or short circuits and backtracks,
    // so the only names visible after evaluation are names added on both branches.
    cOrLeft = enterBooleanScope(cOr, e1@\loc);
    leftStartRange = cOrLeft.nextLoc;
    < cOrLeft, t1 > = checkExp(e1, cOrLeft);
    if (isFailType(t1)) failures += t1;
    leftEndRange = cOrLeft.nextLoc - 1; // Since nextLoc holds the next, the last allocated is at -1
    
    // This finds vars added in the left-hand branch. To save time, we just look at new items added
    // into the abstract store between the location we started with and the current location. We
    // also verify that the variable corresponds to the name that is currently in scope, since
    // we don't want to "resurrect" names that have already fallen out of scope in a nested
    // expression, e.g., (x := p1 || y := p2) || x := p3, we don't want x to suddenly "come back"
    // when it would not have been visible outside of the nested or.
    leftVars = ( );
    if (leftEndRange >= leftStartRange) {
       leftVars = ( vn : v | idx <- [leftStartRange .. leftEndRange+1], idx in cOrLeft.store, sh := head(cOrLeft.stack), v:variable(vn,_,_,sh,_) := cOrLeft.store[idx], RSimpleName("_") != vn, idx := cOrLeft.fcvEnv[vn]);
    }

    cOr = exitBooleanScope(cOrLeft, cOr);
    
    // As above, do the same for the right branch
    cOrRight = enterBooleanScope(cOr, e2@\loc);
    rightStartRange = cOrRight.nextLoc;
    < cOrRight, t2 > = checkExp(e2, cOrRight);
    if (isFailType(t2)) failures += t2;
    rightEndRange = cOrRight.nextLoc - 1;
    
    // Find vars added on the right branch, see above for details of how this works.
    rightVars = ( );
    if (rightEndRange >= rightStartRange) {
       rightVars = ( vn : v | idx <- [rightStartRange .. rightEndRange+1], idx in cOrRight.store, sh := head(cOrRight.stack), v:variable(vn,_,_,sh,_) := cOrRight.store[idx], RSimpleName("_") != vn, idx := cOrRight.fcvEnv[vn]);
    }
    
    cOr = exitBooleanScope(cOrRight, cOr);
    
    // Now, which variables are on both branches? We want to add those into the current scope, and we
    // also need to ensure that the type information is consistent. We also want to merge them in
    // the store and in bookkeeping info like uses, ensuring we only have one copy of each of
    // the variables.
    for (vn <- leftVars, vn in rightVars, variable(vn,lt,linf,lin,lloc) := leftVars[vn], variable(vn,rt,rinf,rin,rloc) := rightVars[vn]) {
        // NOTE: It should be the case that lt and rt, the types assigned to the vars, are not
        // inferred types -- they should have been bound to actual types already. Check here
        // just in case (since this will have been marked elsewhere as an error, don't add another 
        // error here, just leave the name out of the scope). We also make sure they are not
        // failure types, in which case we don't want to introduce the variable.
        if (! (isInferredType(lt) || isInferredType(rt) || isFailType(lt) || isFailType(rt)) ) {
        	// If the variable is available on both sides, we hoist it up into this level,
        	// merging all references to the two independent variables into just one
			cOr.store[cOrLeft.fcvEnv[vn]].containedIn = head(cOr.stack);; // Move the definition from the left-hand side to this level
			oldDefinitions = domainR(cOr.definitions, { cOrRight.fcvEnv[vn] }); // Find the old right-hand side definition(s) of the variable
			cOr.definitions = domainX(cOr.definitions, { cOrRight.fcvEnv[vn] }); // Remove the definition from the right-hand side 
			oldUses = domainR(cOr.uses, { cOrRight.fcvEnv[vn] }); // Find uses of the right-hand definition
			cOr.uses = cOr.uses - oldUses + ( { cOrLeft.fcvEnv[vn] } * oldUses<1> ) + ( { cOrLeft.fcvEnv[vn] } * oldDefinitions<1> ); // Switch these to uses of the left-hand definition, plus make right-hand defs into uses
			cOr.store = domainX(cOr.store, { cOrRight.fcvEnv[vn] }); // Finally, remove the right-hand definition from the store
			cOr.fcvEnv[vn] = cOrLeft.fcvEnv[vn]; // Make sure the name is in the top environment
            
            if (!equivalent(lt,rt)) {
                // We added the variable anyway just to prevent spurious errors, but we just assume the first type
                // is the correct one. If not, we will get errors based on that (i.e., the user meant for the
                // second type, from the right-hand branch, to be the correct one).
                failures += makeFailType("Variable <prettyPrintName(vn)> given inconsistent types <prettyPrintType(lt)> and <prettyPrintType(rt)>", exp@\loc); 
            }
        }
    }
    
    c = needNewScope ? exitBooleanScope(cOr,c) : cOr;
    if (size(failures) > 0) return markLocationFailed(c,exp@\loc,failures);
    if (isBoolType(t1) && isBoolType(t2)) return markLocationType(c,exp@\loc,\bool());
    return markLocationFailed(c,exp@\loc,makeFailType("<opname> not defined for types <prettyPrintType(t1)> and <prettyPrintType(t2)>", exp@\loc));
}

@doc{Check the types of Rascal expressions: If Then Else (DONE)}
public CheckResult checkExp(Expression exp:(Expression)`<Expression e1> ? <Expression e2> : <Expression e3>`, Configuration c) {
    needNewScope = !inBooleanScope(c);
    cTern = needNewScope ? enterBooleanScope(c, exp@\loc) : c;
    < cTern, t1 > = checkExp(e1, cTern);
    < cTern, t2 > = checkExp(e2, cTern);
    < cTern, t3 > = checkExp(e3, cTern);
    c = needNewScope ? exitBooleanScope(cTern,c) : cTern;
    if (isFailType(t1) || isFailType(t2) || isFailType(t3)) return markLocationFailed(c,exp@\loc,{t1,t2,t3});
    if (!isBoolType(t1)) return markLocationFailed(c,exp@\loc,makeFailType("Expected bool, found <prettyPrintType(t1)>",e1@\loc));
    return markLocationType(c,exp@\loc,lub(t2,t3));
}

@doc{Calculate the arith type for the numeric types, taking account of coercions.}
public Symbol numericArithTypes(Symbol l, Symbol r) {
    if (isIntType(l) && isIntType(r)) return \int();
    if (isIntType(l) && isRatType(r)) return \rat();
    if (isIntType(l) && isRealType(r)) return \real();
    if (isIntType(l) && isNumType(r)) return \num();

    if (isRatType(l) && isIntType(r)) return \rat();
    if (isRatType(l) && isRatType(r)) return \rat();
    if (isRatType(l) && isRealType(r)) return \real();
    if (isRatType(l) && isNumType(r)) return \num();

    if (isRealType(l) && isIntType(r)) return \real();
    if (isRealType(l) && isRatType(r)) return \real();
    if (isRealType(l) && isRealType(r)) return \real();
    if (isRealType(l) && isNumType(r)) return \num();

    if (isNumType(l) && isIntType(r)) return \num();
    if (isNumType(l) && isRatType(r)) return \num();
    if (isNumType(l) && isRealType(r)) return \num();
    if (isNumType(l) && isNumType(r)) return \num();

    throw "Only callable for numeric types, given <prettyPrintType(l)> and <prettyPrintType(r)>";
}

@doc{Check the types of Rascal literals: IntegerLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<IntegerLiteral il>`, Configuration c) = markLocationType(c, l@\loc, \int());

@doc{Check the types of Rascal literals: RealLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<RealLiteral rl>`, Configuration c) = markLocationType(c, l@\loc, \real());

@doc{Check the types of Rascal literals: BooleanLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<BooleanLiteral bl>`, Configuration c) = markLocationType(c, l@\loc, \bool());

@doc{Check the types of Rascal literals: DateTimeLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<DateTimeLiteral dtl>`, Configuration c) = markLocationType(c, l@\loc, \datetime());

@doc{Check the types of Rascal literals: RationalLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<RationalLiteral rl>`, Configuration c) = markLocationType(c, l@\loc, \rat());

@doc{Check the types of Rascal literals: RegExpLiteral (DONE)}
public CheckResult checkLiteral(Literal l:(Literal)`<RegExpLiteral rl>`, Configuration c) {
    // Extract all the names used in the regular expression.
    //
    // NOTE: We cannot use concrete syntax matching here, because it confuses the parser. NamedRegExp is defined
    // as Name:RegExp, but the : is interpreted as defining a variable becomes pattern instead, which causes an
    // exception to be thrown in the interpreter.
    //
    list[Tree] nameUses = [];
    list[Tree] nameDefs = [];
    rel[Tree,Tree] defUses = { };

	// NOTE: Using a top-down visit should enforce the correct order, ensuring
	// that uses follow declarations. Just to be sure, we sort them below.    
    top-down visit(rl) {
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : nameUses += prds[1];
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : nameDefs += prds[1];
        case \appl(\prod(lex("NamedRegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : defUses += < last(nameDefs), prds[1] >;
    }

    // Come up with a consolidated, ordered list. All the items in nameUses and nameDefs are at the top level, so we don't have
    // to worry about nesting here. All the nested names are inside defUses.
    list[Tree] consolidated = sort(nameUses + nameDefs, bool(Tree l, Tree r) { return l.begin.line < r.begin.line || (l.begin.line <= r.begin.line && l.begin.column < r.begin.column); });
    
    // Process the names in the regexp, making sure they are defined or adding them into scope as needed.
    if (size(consolidated) > 0) {
        for (n <- consolidated) {
            RName rn = convertName(n);
            if (n in nameUses) {
                // If this is just a use, it should be defined already. It can be of any type -- it will just be
                // converted to a string before being used.
                if (!fcvExists(c, rn)) {
                    c = addScopeMessage(c, error("Name is undefined", n@\loc));
                } else {
                    c.uses += < c.fcvEnv[rn], n@\loc >;
                    c.usedIn[n@\loc] = head(c.stack);
                }
            } else {
                // If this is a definition, add it into scope.
                c = addVariable(c, rn, false, n@\loc, \str());
                
                // Then process names used in the def part.
                for (cn <- defUses[n]) {
                    if (!fcvExists(c,convertName(cn))) {
                        c = addScopeMessage(c, error("Name is undefined", cn@\loc));
                    } else {
                        c.uses += < c.fcvEnv[convertName(cn)], cn@\loc >;
                        c.usedIn[cn@\loc] = head(c.stack);
                    }
                }
            }
        }
    }
    
    // This always appears in a pattern, so we don't need to either add a scope or back out the vars we added (that
    // will be taken care of in the pattern checking logic). We return str here just to match against the intended
    // type of the subject.
    return markLocationType(c, l@\loc, \str());
}

@doc{Check the types of Rascal literals: StringLiteral}
public CheckResult checkLiteral(Literal l:(Literal)`<StringLiteral sl>`, Configuration c) {
    < c, t1 > = checkStringLiteral(sl,c);
    return markLocationType(c, l@\loc, t1);
}

@doc{Check the types of Rascal literals: LocationLiteral}
public CheckResult checkLiteral(Literal l:(Literal)`<LocationLiteral ll>`, Configuration c) {
	< c, t1 > = checkLocationLiteral(ll,c);
	return markLocationType(c, l@\loc, t1);
}

@doc{Check the types of Rascal parameters: Default (DONE) }
public CheckResult checkParameters((Parameters)`( <Formals fs> <KeywordFormals kfs>)`, Configuration c) = checkFormals(fs, false, c);

@doc{Check the types of Rascal parameters: VarArgs (DONE) }
public CheckResult checkParameters((Parameters)`( <Formals fs> ... <KeywordFormals kfs>)`, Configuration c) = checkFormals(fs, true, c);

@doc{Retrieves the parameters from a signature}
public Parameters getFunctionParameters(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps> throws <{Type ","}+ exs>`) = ps;
public Parameters getFunctionParameters(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps>`) = ps;

@doc{Retrieve the keyword formals from a parameter list}
public KeywordFormals getKeywordFormals((Parameters)`( <Formals fs> <KeywordFormals kfs>)`) = kfs;
public KeywordFormals getKeywordFormals((Parameters)`( <Formals fs> ... <KeywordFormals kfs>)`) = kfs;

@doc{Check the types of Rascal formals: Default}
public CheckResult checkFormals((Formals)`<{Pattern ","}* ps>`, bool isVarArgs, Configuration c) {
    list[Symbol] formals = [ ];
    list[Pattern] patterns = [ p | p <- ps ];
    for (idx <- index(patterns)) {
        < c, t > = calculatePatternType(patterns[idx], c);
        if (size(patterns) == (idx + 1) && isVarArgs && !isFailType(t)) {
        	if ((Pattern)`<Type pt> <Name pn>` := patterns[idx]) {
        		c.store[c.fcvEnv[convertName(pn)]].rtype = \list(t);
        	} else if (!isFailType(t)) {
        		t = makeFailType("A var-args parameter must be a type name followed by a variable name", patterns[idx]@\loc);
        	}
        	formals += \list(t);
        } else {
        	formals += t;
        }
    }
    return < c, \tuple(formals) >;
}

@doc{Check the types of Rascal keyword formals}
public tuple[Configuration,KeywordParamMap] checkKeywordFormals((KeywordFormals)`<OptionalComma oc> <{KeywordFormal ","}+ kfl>`, Configuration c) {
	KeywordParamMap kpm = ( );
	for (kfi <- kfl) {
		< c, rn, rt > = checkKeywordFormal(kfi, c);
		kpm[rn] = rt;
	}
	return < c, kpm >;
}

// This is for the case when the keyword formals production derives empty
public default tuple[Configuration,KeywordParamMap] checkKeywordFormals(KeywordFormals kwf, Configuration c) = < c, ( ) >;

@doc{Check the type of a single Rascal keyword formal}
public tuple[Configuration,RName,Symbol] checkKeywordFormal(KeywordFormal kf: (KeywordFormal)`<Type t> <Name n> = <Expression e>`, Configuration c) {
	// Note: We check the default expression first, since the name should NOT be visible inside it
	< c, et > = checkExp(e, c);

    < c, rt > = convertAndExpandType(t,c);
	currentNextLoc = c.nextLoc;
	rn = convertName(n);
	c = addVariable(c, rn, false, n@\loc, rt);
	
	if (!subtype(et, rt))
		rt = makeFailType("The default is not compatible with the parameter type", kf@\loc);  
	if (c.nextLoc > currentNextLoc)
		c.keywordDefaults[currentNextLoc] = e;	  	
	
	return < c, rn, rt >;
}

@doc{Defs and uses of names; allows marking them while still keeping them in the same list or set.}
data DefOrUse = def(RName name) | use(RName name);

data LiteralNodeInfo = literalNodeInfo(DefOrUse,loc);
data MapNodeInfo = mapNodeInfo(PatternTree,PatternTree);

@doc{A compact representation of patterns}
data PatternTree 
    = setNode(list[PatternTree] children)
    | listNode(list[PatternTree] children)
    | nameNode(RName name)
    | multiNameNode(RName name)
    | spliceNodePlus(RName name)
    | spliceNodePlus(RName name, loc at, Symbol rtype)
    | spliceNodeStar(RName name)
    | spliceNodeStar(RName name, loc at, Symbol rtype)
    | negativeNode(PatternTree child)
    | literalNode(Symbol rtype)
    | literalNode(list[LiteralNodeInfo] names)
    | tupleNode(list[PatternTree] children)
    | typedNameNode(RName name, loc at, Symbol rtype)
    | mapNode(list[MapNodeInfo] mapChildren)
    | reifiedTypeNode(PatternTree s, PatternTree d)
    | callOrTreeNode(PatternTree head, list[PatternTree] args)
    | concreteSyntaxNode(Symbol rtype, list[PatternTree] args)
    | varBecomesNode(RName name, loc at, PatternTree child)
    | asTypeNode(Symbol rtype, PatternTree child)
    | deepNode(PatternTree child)
    | antiNode(PatternTree child)
    | tvarBecomesNode(Symbol rtype, RName name, loc at, PatternTree child)
    ;
    
@doc{Mark pattern trees with the source location of the pattern}
public anno loc PatternTree@at;

@doc{A shorthand for the results to expect from binding -- an updated configuration and an updated pattern tree.}
public alias BindResult = tuple[Configuration,PatternTree];

@doc{Extract a tree representation of the pattern.}
public BindResult extractPatternTree(Pattern pat:(Pattern)`{ <{Pattern ","}* ps> }`, Configuration c) {
    list[PatternTree] tpList = [ ];
    for (p <- ps) { < c, pti > = extractPatternTree(p,c); tpList = tpList + pti; }
    return < c, setNode(tpList)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`[ <{Pattern ","}* ps> ]`, Configuration c) {
    list[PatternTree] tpList = [ ];
    for (p <- ps) { < c, pti > = extractPatternTree(p,c); tpList = tpList + pti; }
    return < c, listNode(tpList)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<QualifiedName qn>`, Configuration c) {
    return < c, nameNode(convertName(qn))[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<QualifiedName qn>*`, Configuration c) {
    return < c, multiNameNode(convertName(qn))[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`* <QualifiedName qn>`, Configuration c) {
    return < c, spliceNodeStar(convertName(qn))[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`* <Type t> <Name n>`, Configuration c) {
    < c, rt > = convertAndExpandType(t,c);
    return < c, spliceNodeStar(convertName(n), n@\loc, rt)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`+ <QualifiedName qn>`, Configuration c) {
    return < c, spliceNodePlus(convertName(qn))[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`+ <Type t> <Name n>`, Configuration c) {
    < c, rt > = convertAndExpandType(t,c);
    return < c, spliceNodePlus(convertName(n), n@\loc, rt)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`- <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    return < c, negativeNode(pti)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<IntegerLiteral il>`, Configuration c) {
    return < c, literalNode(\int())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<RealLiteral rl>`, Configuration c) {
    return < c, literalNode(\real())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<BooleanLiteral bl>`, Configuration c) {
    return < c, literalNode(\bool())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<DateTimeLiteral dtl>`, Configuration c) {
    return < c, literalNode(\datetime())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<RationalLiteral rl>`, Configuration c) {
    return < c, literalNode(\rat())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<RegExpLiteral rl>`, Configuration c) {
    list[LiteralNodeInfo] names = [ ];
        
    top-down visit(rl) {
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : 
        	names += literalNodeInfo(use(convertName(prds[1])), prds[1]@\loc );
        case \appl(\prod(lex("RegExp"),[_,\lex("Name"),_,_,_],_),list[Tree] prds) : 
        	names += literalNodeInfo(def(convertName(prds[1])), prds[1]@\loc);
        case \appl(\prod(lex("NamedRegExp"),[_,\lex("Name"),_],_),list[Tree] prds) : 
        	names += literalNodeInfo(use(convertName(prds[1])), prds[1]@\loc);
    }
    
    return < c, literalNode(names)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<StringLiteral sl>`, Configuration c) {
	< c, t1 > = checkStringLiteral(sl,c);
    return < c, literalNode(\str())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<LocationLiteral ll>`, Configuration c) {
	< c, t1 > = checkLocationLiteral(ll,c);
    return < c, literalNode(\loc())[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`\< <Pattern p1>, <{Pattern ","}* ps> \>`, Configuration c) {
    < c, pt1 > = extractPatternTree(p1, c);
    list[PatternTree] ptlist = [ pt1 ];
    for (p <- ps) { < c, pti > = extractPatternTree(p,c); ptlist = ptlist + pti; }
    return < c, tupleNode(ptlist)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<Type t> <Name n>`, Configuration c) {
    < c, rt > = convertAndExpandType(t,c);
    return < c, typedNameNode(convertName(n), n@\loc, rt)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`( <{Mapping[Pattern] ","}* mps> )`, Configuration c) {
    list[MapNodeInfo] res = [ ];
    for ((Mapping[Pattern])`<Pattern pd> : <Pattern pr>` <- mps) {
        < c, pdt > = extractPatternTree(pd,c);
        < c, prt > = extractPatternTree(pr,c);
        res += mapNodeInfo(pdt, prt);
    }
    return < c, mapNode(res)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`type ( <Pattern s>, <Pattern d> )`, Configuration c) {
    < c, pti1 > = extractPatternTree(s,c);
    < c, pti2 > = extractPatternTree(d,c);
    return < c, reifiedTypeNode(pti1,pti2)[@at = pat@\loc] >;
}

public BindResult extractPatternTree(Pattern pat:(Pattern)`<Concrete concrete>`, Configuration c) {
  psList = for (hole((ConcreteHole) `\<<Sym sym> <Name n>\>`) <- concrete.parts) {
    <c, rt> = resolveSorts(sym2symbol(sym),sym@\loc,c);
    append typedNameNode(convertName(n), n@\loc, rt)[@at = n@\loc];
  }
  
  <c, sym> = resolveSorts(sym2symbol(concrete.symbol),concrete.symbol@\loc, c);
  return <c, concreteSyntaxNode(sym,psList)[@at = pat@\loc]>;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<Pattern p> ( <{Pattern ","}* ps> <KeywordArguments keywordArguments>)`, Configuration c) { 
    < c, pti > = extractPatternTree(p,c);
    list[PatternTree] psList = [ ];
    for (psi <- ps) { < c, psit > = extractPatternTree(psi,c); psList = psList + psit; }
    return < c, callOrTreeNode(pti[@headPosition=true],psList)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<Name n> : <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    return < c, varBecomesNode(convertName(n), n@\loc, pti)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`[ <Type t> ] <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    < c, rt > = convertAndExpandType(t,c);
    return < c, asTypeNode(rt, pti)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`/ <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    return < c, deepNode(pti)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`! <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    return < c, antiNode(pti)[@at = pat@\loc] >;
}
public BindResult extractPatternTree(Pattern pat:(Pattern)`<Type t> <Name n> : <Pattern p>`, Configuration c) {
    < c, pti > = extractPatternTree(p,c);
    < c, rt > = convertAndExpandType(t,c);
    return < c, tvarBecomesNode(rt,convertName(n),n@\loc,pti)[@at = pat@\loc] >;
}

@doc{Allows PatternTree nodes to be annotated with types.}
public anno Symbol PatternTree@rtype;

@doc{Allows PatternTree nodes to keep track of which ids they define.}
public anno set[int] PatternTree@defs;

@doc{Is this node in head position in a call or tree node?}
public anno bool PatternTree@headPosition;

@doc{Do we have possible constructors here that do not match arity?}
public anno set[Symbol] PatternTree@arityMismatches;

@doc{Do we have too many matching constructors here?}
public anno set[Symbol] PatternTree@tooManyMatches;

@doc{A quick predicate to say whether we can use the type in a type calculation}
public bool concreteType(Symbol t) = size({ ti | /Symbol ti := t, \failure(_) := ti || \inferred(_) := ti }) == 0; 

@doc{Calculate the type of pattern. If a subject is given, this is used as part of the type calculation to ensure the subject can be bound to the pattern.}
public CheckResult calculatePatternType(Pattern pat, Configuration c, Symbol subjects...) {
    if (size(subjects) > 1) throw "Invalid invocation, only one subject allowed, not <size(subjects)>";
    
    // Init: extract the pattern tree, which gives us an abstract representation of the pattern
    < c, pt > = extractPatternTree(pat,c);
    Configuration cbak = c;
    set[Symbol] failures = { };
    
    // Step 1: Do an initial assignment of types to the names present
    // in the tree and to nodes with invariant types (such as int
    // literals and guarded patterns).
    pt = bottom-up visit(pt) {
        case ptn:setNode(ptns) : {
            for (idx <- index(ptns), spliceNodePlus(n) := ptns[idx] || spliceNodeStar(n) := ptns[idx] || 
                                     spliceNodePlus(n,_,_) := ptns[idx] || spliceNodeStar(n,_,_) := ptns[idx] ||
                                     multiNameNode(n) := ptns[idx]) {
                
            	if (spliceNodePlus(_,_,rt) := ptns[idx] || spliceNodeStar(_,_,rt) := ptns[idx]) {
	                if (RSimpleName("_") == n) {
                        c = addUnnamedVariable(c, ptns[idx]@at, \set(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt][@defs = { c.nextLoc - 1 }];
	                } else {
	                	// TODO: Do we want to issue a warning here if the same name is used multiple times? Probably, although a pass
	                	// over the pattern tree may be a better way to do this (this would only catch cases at the same level of
	                	// a set pattern or, below, a list pattern)
	                    c = addVariable(c, n, false, ptns[idx]@at, \set(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt];
	                } 
            	} else {
	                if (RSimpleName("_") == n) {
	                    rt = \inferred(c.uniqueify);
	                    c.uniqueify = c.uniqueify + 1;
	                    c = addUnnamedVariable(c, ptns[idx]@at, \set(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt][@defs = { c.nextLoc - 1 }];
	                } else if (!fcvExists(c, n)) {
	                    rt = \inferred(c.uniqueify);
	                    c.uniqueify = c.uniqueify + 1;
	                    c = addVariable(c, n, true, ptns[idx]@at, \set(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt];
	                } else {
	                    c.uses = c.uses + < c.fcvEnv[n], ptns[idx]@at >;
	                    c.usedIn[ptn@at] = head(c.stack);
	                    Symbol rt = c.store[c.fcvEnv[n]].rtype;
	                    // TODO: Keep this now that we have splicing?
	                    if (isSetType(rt))
	                        ptns[idx] = ptns[idx][@rtype = getSetElementType(rt)];
	                    else
	                        failures += makeFailType("Expected type set, not <prettyPrintType(rt)>", ptns[idx]@at);
	                    c = addNameWarning(c,n,ptns[idx]@at);
	                }
            	}
            }
            
            insert(ptn[children=ptns]);
        }

        case ptn:listNode(ptns) : {
            for (idx <- index(ptns), spliceNodePlus(n) := ptns[idx] || spliceNodeStar(n) := ptns[idx] || 
                                     spliceNodePlus(n,_,_) := ptns[idx] || spliceNodeStar(n,_,_) := ptns[idx] ||
                                     multiNameNode(n) := ptns[idx]) {
                
            	if (spliceNodePlus(_,_,rt) := ptns[idx] || spliceNodeStar(_,_,rt) := ptns[idx]) {
	                if (RSimpleName("_") == n) {
                        c = addUnnamedVariable(c, ptns[idx]@at, \list(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt][@defs = { c.nextLoc - 1 }];
	                } else {
	                    c = addVariable(c, n, false, ptns[idx]@at, \list(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt];
	                } 
            	} else {
	                if (RSimpleName("_") == n) {
	                    rt = \inferred(c.uniqueify);
	                    c.uniqueify = c.uniqueify + 1;
	                    c = addUnnamedVariable(c, ptns[idx]@at, \list(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt][@defs = { c.nextLoc - 1 }];
	                } else if (!fcvExists(c, n)) {
	                    rt = \inferred(c.uniqueify);
	                    c.uniqueify = c.uniqueify + 1;
	                    c = addVariable(c, n, true, ptns[idx]@at, \list(rt));
	                    ptns[idx] = ptns[idx][@rtype = rt];
	                } else {
	                    c.uses = c.uses + < c.fcvEnv[n], ptns[idx]@at >;
	                    c.usedIn[ptn@at] = head(c.stack);
	                    Symbol rt = c.store[c.fcvEnv[n]].rtype;
	                    // TODO: Keep this now that we have splicing?
	                    if (isListType(rt))
	                        ptns[idx] = ptns[idx][@rtype = getListElementType(rt)];
	                    else
	                        failures += makeFailType("Expected type list, not <prettyPrintType(rt)>", ptns[idx]@at); 
	                    c = addNameWarning(c,n,ptns[idx]@at);
	                }
	            }
            }
            insert(ptn[children=ptns]);
        }

        case ptn:nameNode(n) : { 
            if (RSimpleName("_") == n) {
                rt = \inferred(c.uniqueify);
                c.uniqueify = c.uniqueify + 1;
                c = addUnnamedVariable(c, ptn@at, rt);
                insert(ptn[@rtype = rt][@defs = { c.nextLoc - 1 }]);
            } else if (!fcvExists(c, n)) {
                rt = \inferred(c.uniqueify);
                c.uniqueify = c.uniqueify + 1;
                c = addVariable(c, n, true, ptn@at, rt);
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            } else {
                c.uses = c.uses + < c.fcvEnv[n], ptn@at >;
                c.usedIn[ptn@at] = head(c.stack);
                if ( !((ptn@headPosition)?) || ((ptn@headPosition)? && !ptn@headPosition)) {
                    if (variable(_,_,_,_,_) !:= c.store[c.fcvEnv[n]]) {
                        c = addScopeWarning(c, "<prettyPrintName(n)> is a function, constructor, or production name", ptn@at);
                    } else {
                        c = addNameWarning(c,n,ptn@at);
                    }
                }
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            }
        }
        
        case ptn:literalNode(Symbol rt) => ptn[@rtype = rt]
        
        case ptn:literalNode(list[LiteralNodeInfo] names) : {
            for ( literalNodeInfo(d, l) <- names ) {
                if (def(n) := d) {
                    c = addVariable(c, n, false, l, \str());
                } else if (use(n) := d) {
                    if (!fcvExists(c, n)) {
                        failures += makeFailType("Name <prettyPrintName(n)> not yet defined", ptn@at);
                    } else {
                        c.uses = c.uses + < c.fcvEnv[n], l >; 
                        c.usedIn[l] = head(c.stack);
                    }
                } 
            }
            insert(ptn[@rtype = \str()]);
        }
        
        case ptn:typedNameNode(n, l, rt) : { 
            if (RSimpleName("_") == n) {
                c = addUnnamedVariable(c, l, rt);
                insert(ptn[@rtype = rt][@defs = { c.nextLoc - 1 }]);
            } else {
                c = addVariable(c, n, false, l, rt);
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            }
        }
        
        case ptn:varBecomesNode(n, l, _) : { 
            if (RSimpleName("_") == n) {
                rt = \inferred(c.uniqueify);
                c.uniqueify = c.uniqueify + 1;
                c = addUnnamedVariable(c, l, rt);
                insert(ptn[@rtype = rt][@defs = { c.nextLoc - 1 }]);
            } else if (!fcvExists(c, n)) {
                rt = \inferred(c.uniqueify);
                c.uniqueify = c.uniqueify + 1;
                c = addVariable(c, n, true, l, rt);
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            }  else {
                c.uses = c.uses + < c.fcvEnv[n], ptn@at >;
                c.usedIn[ptn@at] = head(c.stack);
                if (variable(_,_,_,_,_) !:= c.store[c.fcvEnv[n]]) {
                    c = addScopeWarning(c, "Name <prettyPrintName(n)> is a function, constructor, or production name", ptn@at);
                } else {
                    c = addNameWarning(c,n,ptn@at);
                }
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            }
        }

		case ptn:deepNode(_) : {
			rt = \inferred(c.uniqueify);
			c.uniqueify = c.uniqueify + 1;
			insert(ptn[@rtype = rt]);
		}
		
        case ptn:asTypeNode(rt, _) => ptn[@rtype = rt]
        
		case ptn:antiNode(_) : {
			rt = \inferred(c.uniqueify);
			c.uniqueify = c.uniqueify + 1;
			insert(ptn[@rtype = rt]);
		}
		
		case ptn:reifiedTypeNode(tSymbol,pDefs) => 
			ptn[@rtype = makeReifiedType(makeValueType())]
		
        // TODO: Not sure if this is the best choice, but it is the choice
        // the current interpreter makes...
        //case ptn:antiNode(_) => ptn[@rtype = \value()]
        
        case ptn:tvarBecomesNode(rt, n, l, _) : { 
            if (RSimpleName("_") == n) {
                c = addUnnamedVariable(c, l, rt);
                insert(ptn[@rtype = rt][@defs = { c.nextLoc - 1 }]);
            } else {
                c = addVariable(c, n, false, l, rt);
                insert(ptn[@rtype = c.store[c.fcvEnv[n]].rtype]);
            }
        }
        
        case ptn:concreteSyntaxNode(rt,plist) => ptn[@rtype = rt]
    }
    
    if (size(failures) > 0) {
    	// TODO: Allowing the "bad" config to go back, change back to
    	// cbak if this causes chaos...
        return < c, collapseFailTypes(failures) >;
    }
        
    bool modified = true;

    PatternTree updateRT(PatternTree pt, Symbol rt) {
        if ( (pt@rtype)? && (pt@rtype == rt) ) return pt;
        modified = true;
        return pt[@rtype = rt];
    }

    PatternTree updateBindProblems(PatternTree pt, set[Symbol] arityMismatches, set[Symbol] tooManyMatches) {
    	// We intentionally don't set modified here, since these are just error markers.
    	if (size(tooManyMatches) > 0) arityMismatches = { }; // only report arity problems if they are the only ones we have
        return pt[@arityMismatches = arityMismatches][@tooManyMatches = tooManyMatches];
    }
    
    // Step 2: push types up from the leaves to the root, and back down from the root to the leaves,
    // until the type stabilizes
    bool firstTime = true;
    while(modified) {
        modified = false;

        // In this first visit, we try to propagate type information up from the leaves of the
        // pattern tree towards the root. This gives us a way to use the types assigned to
        // names, literals, etc to find the final types of other patterns.
        pt = bottom-up visit(pt) {
            case ptn:setNode([]) => updateRT(ptn, \set(\void()))
            
            case ptn:setNode(ptns) => updateRT(ptn,\set(lubList([pti@rtype | pti <- ptns]))) 
                                      when all(idx <- index(ptns), (ptns[idx]@rtype)?, concreteType(ptns[idx]@rtype))
                                      
            case ptn:listNode([]) => updateRT(ptn, \list(\void()))
            
            case ptn:listNode(ptns) => updateRT(ptn,\list(lubList([pti@rtype | pti <- ptns]))) 
                                       when all(idx <- index(ptns), (ptns[idx]@rtype)?, concreteType(ptns[idx]@rtype))
                                      
            case ptn:negativeNode(cp) => updateRT(ptn, cp@rtype) 
            							 when (cp@rtype)? && concreteType(cp@rtype) && !isVoidType(cp@rtype) && subtype(cp@rtype, \num())
    
            case ptn:negativeNode(cp) :
                if ( (cp@rtype)? && concreteType(cp@rtype))
                    failures += makeFailType("Cannot apply negative pattern to subpattern of type <prettyPrintType(cp@rtype)>", ptn@at);
                    
            case ptn:tupleNode(ptns) => updateRT(ptn,\tuple([pti@rtype|pti <- ptns]))
                                        when all(idx <- index(ptns), (ptns[idx]@rtype)?, concreteType(ptns[idx]@rtype))
                                        
            case ptn:mapNode([]) => updateRT(ptn,\map(\void(),\void()))
                                        
            case ptn:mapNode(ptns) => updateRT(ptn,\map(lubList([d@rtype|mapNodeInfo(d,_) <- ptns]),lubList([r@rtype|mapNodeInfo(_,r)<-ptns])))
                                      when all(idx <- index(ptns), mapNodeInfo(d,r) := ptns[idx], (d@rtype)?, (r@rtype)?, concreteType(d@rtype), concreteType(r@rtype))
                                      
            //case ptn:deepNode(cp) => updateRT(ptn, \void()) when (cp@rtype)? && concreteType(cp@rtype)

            //case ptn:antiNode(cp) => updateRT(ptn, cp@rtype) when (cp@rtype)? && concreteType(cp@rtype)
            
            case ptn:varBecomesNode(n,l,cp) : {
                if ( (cp@rtype)? && concreteType(cp@rtype)) {
                    Symbol rt = (RSimpleName("_") == n) ? ptn@rtype : c.store[c.fcvEnv[n]].rtype;
                    bool isInferred = (RSimpleName("_") == n) ? true : c.store[c.fcvEnv[n]].inferred;
                    if (isInferred) {
                        if (isInferredType(rt)) {
                            if (RSimpleName("_") == n) {
                                c.store[getOneFrom(ptn@defs)].rtype = cp@rtype; 
                            } else {
                                c.store[c.fcvEnv[n]].rtype = cp@rtype;
                            }
                            insert updateRT(ptn, cp@rtype);
                        } else {
                            Symbol rtNew = lub(rt, cp@rtype);
                            if (!equivalent(rtNew,rt)) {
                                if (RSimpleName("_") == n) {
                                    c.store[getOneFrom(ptn@defs)].rtype = rtNew; 
                                } else {
                                    c.store[c.fcvEnv[n]].rtype = rtNew;
                                }
                                insert updateRT(ptn, rtNew);
                            }
                        }
                    } else {
                        if (!comparable(cp@rtype, rt))
                            failures += makeFailType("Cannot assign pattern of type <prettyPrintType(cp@rtype)> to non-inferred variable <prettyPrintName(n)> of type <prettyPrintType(rt)>", ptn@at);
                    }
                }
            }
    
            case ptn:tvarBecomesNode(rt,n,l,cp) : {
                if ( (cp@rtype)? && concreteType(cp@rtype)) {
                    Symbol rt = (RSimpleName("_") == n) ? ptn@rtype : c.store[c.fcvEnv[n]].rtype;
                    if (!comparable(cp@rtype, rt))
                        failures += makeFailType("Cannot assign pattern of type <prettyPrintType(cp@rtype)> to non-inferred variable <prettyPrintName(n)> of type <prettyPrintType(rt)>", ptn@at);
                }
            }
            
            case ptn:reifiedTypeNode(sp,dp) : {
                if ( (sp@rtype)? && concreteType(sp@rtype) && !subtype(sp@rtype,\adt("Symbol",[])) ) {
                	failures += makeFailType("The first pattern parameter in a reified type parameter must be of type Symbol, not <prettyPrintType(sp@rtype)>", ptn@at);
                }
                if ( (dp@rtype)? && concreteType(dp@rtype) && !subtype(dp@rtype,\map(\adt("Symbol",[]), \adt("Production",[]))) ) { 
                	failures += makeFailType("The second pattern parameter in a reified type parameter must be of type map[Symbol,Production], not <prettyPrintType(dp@rtype)>", ptn@at);
                }
			}
                
    
            case ptn:callOrTreeNode(ph,pargs) : {
            	if ( (ph@rtype)? && concreteType(ph@rtype) ) {
                    if (isConstructorType(ph@rtype) || isOverloadedType(ph@rtype) || isProductionType(ph@rtype)) {
                        // default alternatives contain all possible constructors of this name
                        set[Symbol] alts = (isOverloadedType(ph@rtype)) ? (filterSet(getDefaultOverloadOptions(ph@rtype), isConstructorType) + filterSet(getDefaultOverloadOptions(ph@rtype), isProductionType)) : {ph@rtype};
                        // matches holds all the constructors that match the arity and types in the pattern
                        set[Symbol] matches = { };
                        set[Symbol] nonMatches = { };
                        ptn@arityMismatches = { };
                        ptn@tooManyMatches = { };
                        
                        //if (size(pargs) == 0) {
                        //    // if we have no arguments, then all the alternatives could match
                        //    // TODO: Is this true? It seems that we can only match if the arity matches, so, disabling for now...
                        //    matches = alts;
                        //} else {
                            // filter first based on the arity of the constructor
                            for (a <- alts) {
                            	if (isConstructorType(a) && size(getConstructorArgumentTypes(a)) == size(pargs)) {
	                                // next, find the bad matches, which are those argument positions where we have concrete
	                                // type information and that information does not match the alternative
	                                badMatches = { };
	                                for (idx <- index(pargs)) {
	                                	bool pseudoMatch = false;
	                                	argType = getConstructorArgumentTypes(a)[idx];
	                                	if ((pargs[idx]@rtype)?) {
	                                		if (concreteType(pargs[idx]@rtype)) {
	                                			if (!subtype(pargs[idx]@rtype, argType)) {
	                                				badMatches = badMatches + idx;
	                                			}
	                                		} else {
	                                			pseudoMatch = true;
	                                		}
	                                	} else {
	                                		pseudoMatch = true;
	                                	}
	                                	
	                                	if (pseudoMatch) {
	                                		if (! ( (isListType(argType) && pargs[idx] is listNode) ||
	                                			    (isSetType(argType) && pargs[idx] is setNode) ||
	                                			    (isMapType(argType) && pargs[idx] is mapNode) ||
	                                			    ( !(pargs[idx] is listNode || pargs[idx] is setNode || pargs[idx] is mapNode) && (!((pargs[idx]@rtype)?) || !(concreteType(pargs[idx]@rtype)))))) {
	                                			badMatches = badMatches + idx;
	                                		}
	                                	}
	                                }
	                                if (size(badMatches) == 0) 
	                                    // if we had no bad matches, this is a valid alternative
	                                    matches += a;
                            	} else if (isProductionType(a) && size(getProductionArgumentTypes(a)) == size(pargs)) {
	                                // next, find the bad matches, which are those argument positions where we have concrete
	                                // type information and that information does not match the alternative
	                                badMatches = { idx | idx <- index(pargs), (pargs[idx]@rtype)?, concreteType(pargs[idx]@rtype), !subtype(pargs[idx]@rtype, getProductionArgumentTypes(a)[idx]) };
	                                if (size(badMatches) == 0) 
	                                    // if we had no bad matches, this is a valid alternative
	                                    matches += a;
                                } else {
                                    nonMatches += a;
                                }
                            }
                        //}
                        
                        if (size(matches) == 1) {
                            // Push the binding back down the tree with the information in the constructor type; if
                            // this doesn't cause errors, save the updated children back into the tree, along with
                            // the match type
                            Symbol matchType = getOneFrom(matches);
                            bool cannotInstantiate = false;

                            // TODO: Find a better place for this huge chunk of code!
                            if (concreteType(matchType) && typeContainsTypeVars(matchType) && ( size(pargs) == 0 || all(idx <- index(pargs), (pargs[idx])?, concreteType(pargs[idx]@rtype)))) {
                                // If the constructor is parametric, we need to calculate the actual types of the
                                // parameters and make sure they fall within the proper bounds. Note that we can only
                                // do this when the match type is concrete and when we either have no pargs or we have
                                // pargs that all have concrete types associated with them.
                                formalArgs = isConstructorType(matchType) ? getConstructorArgumentTypes(matchType) : getProductionArgumentTypes(matchType);
                                set[Symbol] typeVars = { *collectTypeVars(fa) | fa <- formalArgs };
                                map[str,Symbol] bindings = ( getTypeVarName(tv) : \void() | tv <- typeVars );
                                unlabeledArgs = [ (\label(_,v) := li) ? v : li | li <- formalArgs ];
                                for (idx <- index(formalArgs)) {
                                    try {
                                        bindings = match(unlabeledArgs[idx],pargs[idx]@rtype,bindings);
                                    } catch : {
                                        insert updateRT(ptn[head=ph[@rtype=matchType]], makeFailType("Cannot instantiate parameter <idx+1>, parameter type <prettyPrintType(pargs[idx]@rtype)> violates bound of type parameter in formal argument with type <prettyPrintType(formalArgs[idx])>", pargs[idx]@at));
                                        cannotInstantiate = true;  
                                    }
                                }
                                if (!cannotInstantiate) {
                                    try {
                                        matchType = instantiate(matchType, bindings);
                                    } catch : {
                                        insert updateRT(ptn[head=ph[@rtype=matchType]], makeFailType("Cannot instantiate type parameters in constructor", ptn@at));
                                        cannotInstantiate = true;
                                    }
                                }
                            }
                            
                            if (!cannotInstantiate) {
                                list[PatternTree] newChildren = [ ];
                                formalArgs = isConstructorType(matchType) ? getConstructorArgumentTypes(matchType) : getProductionArgumentTypes(matchType);
                                unlabeledArgs = [ (\label(_,v) := li) ? v : li | li <- formalArgs ];                                
                                try {
                                    for (idx <- index(pargs)) {
                                        //println("<ptn@at>: pushing down <getConstructorArgumentTypes(matchType)[idx]> for arg <pargs[idx]>");  
                                        < c, newarg > = bind(pargs[idx],unlabeledArgs[idx],c);
                                        newChildren += newarg;
                                    }
                                } catch v : {
                                    newChildren = pargs;
                                }
                                insert updateRT(ptn[head=ph[@rtype=matchType]][args=newChildren], isConstructorType(matchType)?getConstructorResultType(matchType):getProductionSortType(matchType));
                            }
                        } else {
                        	insert updateBindProblems(ptn, nonMatches, matches);
                        }
                    } else if (isStrType(ph@rtype)) {
                        list[PatternTree] newChildren = [];
                        try {
                            for(int idx <- index(pargs)) {
                                <c, newarg> = bind(pargs[idx],Symbol::\value(),c);
                            }
                        } catch v : {
                            newChildren = pargs;
                        }
                        insert updateRT(ptn[args=newChildren], \node());
                    }
                }
            }       
        }
        
        if (size(failures) > 0) {
	    	// TODO: Allowing the "bad" config to go back, change back to
	    	// cbak if this causes chaos...
            return < c, collapseFailTypes(failures) >;
        }
        
        if (size(subjects) == 1) {
            try {
                < c, pt > = bind(pt, getOneFrom(subjects), c);
                // Why do this? Because we want to bind at least once, and the first bind could
                // modify the tree, but we don't have a good, cheap way of telling. After that, we
                // can assume that, if we didn't change anything above, we won't change anything if
                // we bind again.
                if (firstTime) {
                    modified = true;
                    firstTime = false;
                }
            } catch v : {
                //println("Bind attempt failed, now have <pt>");
                if(pt@rtype? && !hasInferredType(pt@rtype)) {
                	failures += makeFailType("Cannot match an expression of type: <getOneFrom(subjects)> against a pattern of type <pt@rtype>", pt@at);
               	}
            }
        }
    }
    
    if (size(failures) > 0) {
    	// TODO: Allowing the "bad" config to go back, change back to
    	// cbak if this causes chaos...
        return < c, collapseFailTypes(failures) >;
    }

    set[PatternTree] unknownConstructorFailures(PatternTree pt) {
        return { ptih | /PatternTree pti:callOrTreeNode(PatternTree ptih,_) := pt, (ptih@rtype)?, isInferredType(ptih@rtype) };
    }

    set[PatternTree] arityFailures(PatternTree pt) {
        return { pti | /PatternTree pti:callOrTreeNode(_,_) := pt, (pti@arityMismatches)?, size(pti@arityMismatches) > 0 };
    }

    set[PatternTree] tooManyMatchesFailures(PatternTree pt) {
        return { pti | /PatternTree pti:callOrTreeNode(_,_) := pt, (pti@tooManyMatches)?, size(pti@tooManyMatches) > 0 };
    }

	set[PatternTree] unresolved = { };
    if ( (pt@rtype)? ) {
        unresolved = { pti | /PatternTree pti := pt, !((pti@rtype)?) || ((pti@rtype)? && !concreteType(pti@rtype)) };
    }
    
    if ( (pt@rtype)? == false || size(unresolved) > 0) {
        unknowns = unknownConstructorFailures(pt);
        arityProblems = arityFailures(pt);
        tooManyMatches = tooManyMatchesFailures(pt);
        if (size(unknowns) == 0 && size(arityProblems) == 0 && size(tooManyMatches) == 0) {
            //println("<pt@at>: Pattern tree is <pt>, with subjects <subjects>");
            return < c, makeFailType("Type of pattern could not be computed, please add additional type annotations", pat@\loc) >;
        } else {
    		for (PatternTree pt <- tooManyMatches)
    			failures += makeFailType("Multiple constructors and/or productions match this pattern, add additional type annotations", pt@at);
        	
    		for (PatternTree pt <- arityProblems)
    			failures += makeFailType("Only constructors or productions with a different arity are available", pt@at);

            for (unk <- unknowns)
            	failures += makeFailType("Constructor or production name is not in scope", unk@at);
        	
        	failures += makeFailType("Type of pattern could not be computed", pat@\loc);
            return < c, collapseFailTypes(failures) >;
        }
    } else {
		c.locationTypes = c.locationTypes + ( ptnode@at : ptnode@rtype | /PatternTree ptnode := pt, (ptnode@rtype)? );  
		return < c, pt@rtype >;
    }
}

@doc{Bind a subject type to a pattern tree.}
public BindResult bind(PatternTree pt, Symbol rt, Configuration c) {
    // NOTE: We assume the bind triggers an error at the point of bind failure.
    // So, if we are looking at a set node, we just have to make sure that the
    // type we are binding to it is a set of something.
    //
    // TODO: Add more checks here. If we push information through a node that will
    // cause a failure on the push back up, we will still catch it. However, if we
    // are using bind as a proxy for which overload to use, we will have better
    // luck if we catch more errors here. Examples: negation should check for numerics,
    // and typed name becomes should make sure the result is of a compatible type.
    //
    // TODO: Anything for * variables?

    switch(pt) {
        case setNode(cs) : {
            if (isSetType(rt)) {
                list[PatternTree] res = [ ];
                for (csi <- cs) { 
                    < c, pti > = bind(csi, getSetElementType(rt), c); 
                    res += pti; 
                }
                return < c, pt[children = res] >; 
            } else if (isValueType(rt)) {
                return < c, pt >;
            }
        }

        case listNode(cs) : {
            if (isListType(rt)) {
                list[PatternTree] res = [ ];
                for (csi <- cs) { 
                    //println("<csi@at>: Binding <csi> to type <prettyPrintType(getListElementType(rt))>");
                    < c, pti > = bind(csi, getListElementType(rt), c); 
                    //println("<csi@at>: Binding result is <pti>");
                    res += pti; 
                }
                return < c, pt[children = res] >; 
            } else if (isValueType(rt)) {
                return < c, pt >;
            }
        }
        
        case nameNode(RSimpleName("_")) : {
            Symbol currentType = pt@rtype;
            if (isInferredType(currentType)) {
                c.store[getOneFrom(pt@defs)].rtype = rt;
                return < c, pt[@rtype = rt] >;
            } else {
                c.store[getOneFrom(pt@defs)].rtype = lub(currentType, rt);
                return < c, pt[@rtype = lub(currentType, rt)] >;
            }
        }
        
        case nameNode(rn) : {
            Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (c.store[c.fcvEnv[rn]].inferred) {
                if (isInferredType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = rt;
                } else {
                    c.store[c.fcvEnv[rn]].rtype = lub(currentType, rt);
                }
                return < c, pt[@rtype = c.store[c.fcvEnv[rn]].rtype] >;
            } else {
                if (comparable(currentType, rt))
                    return < c, pt >;
                else
                    throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
        case multiNameNode(RSimpleName("_")) : {
            Symbol currentType = pt@rtype;
            if (isInferredType(currentType)) {
                c.store[getOneFrom(pt@defs)].rtype = rt;
                return < c, pt[@rtype = rt] >;
            } else {
                c.store[getOneFrom(pt@defs)].rtype = lub(currentType, rt);
                return < c, pt[@rtype = lub(currentType, rt)] >;
            }
        }

        case multiNameNode(rn) : {
            Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (c.store[c.fcvEnv[rn]].inferred) {
                if (isSetType(currentType) && isInferredType(getSetElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \set(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isListType(currentType) && isInferredType(getListElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \list(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isSetType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \set(lub(getSetElementType(currentType), rt));
                    return < c, pt[@rtype = getSetElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                } else if (isListType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \list(lub(getListElementType(currentType), rt));
                    return < c, pt[@rtype = getListElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                }
            } else {
                if (isSetType(currentType) && comparable(getSetElementType(currentType), rt))
                    return < c, pt >;
                else if (isListType(currentType) && comparable(getListElementType(currentType), rt))
                    return < c, pt >;
                else
                    throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
       case spliceNodeStar(RSimpleName("_")) : {
            Symbol currentType = pt@rtype;
            if (isInferredType(currentType)) {
                return < c, pt[@rtype = rt] >;
            } else {
                return < c, pt[@rtype = lub(currentType, rt)] >;
            }
        }
        
        case spliceNodeStar(rn) : { 
        	Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (c.store[c.fcvEnv[rn]].inferred) {
                if (isSetType(currentType) && isInferredType(getSetElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \set(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isListType(currentType) && isInferredType(getListElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \list(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isSetType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \set(lub(getSetElementType(currentType), rt));
                    return < c, pt[@rtype = getSetElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                } else if (isListType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \list(lub(getListElementType(currentType), rt));
                    return < c, pt[@rtype = getListElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                }
            } else {
                if (isSetType(currentType) && comparable(getSetElementType(currentType), rt)) {
                    return < c, pt >;
                } else if (isListType(currentType) && comparable(getListElementType(currentType), rt)) {
                    return < c, pt >;
                } else {
                    throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
                }
            }
        }
        
        case spliceNodeStar(RSimpleName("_"),_,nt) : {
            Symbol currentType = pt@rtype;
            if (comparable(currentType, rt)) {
                return < c, pt >;
            } else {
                throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
        case spliceNodeStar(rn,_,nt) : { 
        	Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (isSetType(currentType) && comparable(getSetElementType(currentType), rt)) {
                return < c, pt >;
            } else if (isListType(currentType) && comparable(getListElementType(currentType), rt)) {
                return < c, pt >;
            } else {
            	throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
        case spliceNodePlus(RSimpleName("_")) : {
        	Symbol currentType = pt@rtype;
            if (isInferredType(currentType)) {
                return < c, pt[@rtype = rt] >;
            } else {
                return < c, pt[@rtype = lub(currentType, rt)] >;
            } 
        }
        
        case spliceNodePlus(rn) : { 
        	Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (c.store[c.fcvEnv[rn]].inferred) {
                if (isSetType(currentType) && isInferredType(getSetElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \set(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isListType(currentType) && isInferredType(getListElementType(currentType))) {
                    c.store[c.fcvEnv[rn]].rtype = \list(rt);
                    return < c, pt[@rtype = rt] >;
                } else if (isSetType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \set(lub(getSetElementType(currentType), rt));
                    return < c, pt[@rtype = getSetElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                } else if (isListType(currentType)) {
                    c.store[c.fcvEnv[rn]].rtype = \list(lub(getListElementType(currentType), rt));
                    return < c, pt[@rtype = getListElementType(c.store[c.fcvEnv[rn]].rtype)] >;
                }
            } else {
                if (isSetType(currentType) && comparable(getSetElementType(currentType), rt)) {
                    return < c, pt >;
                } else if (isListType(currentType) && comparable(getListElementType(currentType), rt)) {
                    return < c, pt >;
                } else {
                    throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
                }
            }
        }
        
        case spliceNodePlus(RSimpleName("_"),_,nt) : {
            Symbol currentType = pt@rtype;
            if (comparable(currentType, rt)) {
                return < c, pt >;
            } else {
                throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
        case spliceNodePlus(rn,_,nt) : { 
        	Symbol currentType = c.store[c.fcvEnv[rn]].rtype;
            if (isSetType(currentType) && comparable(getSetElementType(currentType), rt)) {
                return < c, pt >;
            } else if (isListType(currentType) && comparable(getListElementType(currentType), rt)) {
                return < c, pt >;
            } else {
            	throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            }
        }
        
        case negativeNode(cp) : {
            < c, cpNew > = bind(cp, rt, c);
            return < c, pt[child = cpNew] >;
        }
        
        case literalNode(nt) : {
        	if (\sort(_) := rt && isStrType(pt@rtype)) {
        		return < c, pt >;
        	} else if (!isInferredType(rt) && !comparable(pt@rtype,rt)) {
                throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            } else {
                return < c, pt >;
			}
        }
        
        case literalNode(list[LiteralNodeInfo] names) : {
            if (!isInferredType(rt) && !comparable(pt@rtype,rt))
                throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
            else
                return < c, pt >;
        }
        
        case tupleNode(cs) : {
            if (isTupleType(rt)) {
                list[Symbol] tfields = getTupleFields(rt);
                if (size(tfields) == size(cs)) {
                    list[PatternTree] res = [ ];
                    for (idx <- index(tfields)) { < c, pti > = bind(cs[idx], tfields[idx], c); res += pti; }
                    return < c, pt[children = res] >; 
                } else {
                    throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
                }
            } else if (isValueType(rt)) {
                return < c, pt >;
            }
        }
        
        case typedNameNode(n, l, nt) : {
            Symbol currentType = (RSimpleName("_") == n) ? pt@rtype : c.store[c.fcvEnv[n]].rtype;
            if (comparable(currentType, rt))
                return < c, pt >;
            else
                throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
        }
        
        case mapNode(list[MapNodeInfo] mapChildren) : {
            if (isMapType(rt)) {
                list[MapNodeInfo] res = [ ];
                for (mapNodeInfo(d1,r1) <- mapChildren) { 
                    < c, pt1 > = bind(d1, getMapDomainType(rt), c); 
                    < c, pt2 > = bind(r1, getMapRangeType(rt), c); 
                    res += mapNodeInfo(pt1, pt2); 
                }
                return < c, pt[mapChildren = res] >; 
            } else if (isValueType(rt)) {
                return < c, pt >;
            }
        }
        
        case reifiedTypeNode(ps,pd) : {
        	// The subject type has no influence on the types of the children of a reified type
        	// node, so we can't push a type down through the node, we instead always insist
        	// that the types are Symbol and map[Symbol,Production]
        	< c, psnew > = bind(ps, \adt("Symbol",[]), c);
        	< c, pdnew > = bind(pd, \map(\adt("Symbol",[]),\adt("Production",[])), c);
        	return < c, pt[s=psnew][d=pdnew] >; 
        }
        
        case callOrTreeNode(ph, cs) : {
            // TODO: Should we implement this? We already always push back down when we hit call or tree nodes,
            // so this may just be redundant. So, for now, just return the default.
            return < c, pt >;
        }
        
        case varBecomesNode(n, l, cp) : {
            Symbol currentType = pt@rtype;
            < c, cpnew > = bind(cp, rt, c);
            return < c, pt[child=cpnew] >;
        }
        
        case asTypeNode(nt, cp) : {
            < c, cpNew > = bind(cp, rt, c);
            return < c, pt[child = cpNew] >;
        }
        
        case deepNode(cp) : {
            Symbol currentType = pt@rtype;
            < c, cpNew > = bind(cp, \value(), c);
            return < c, pt[child = cpNew][@rtype=rt] >;
        }

		// TODO: Is this right? Technically, the type of the antinode
		// can be anything, since we are saying this isn't the thing we
		// are matching, but we may still want a sharper check to give
		// good warnings when things cannot happen                
        case antiNode(cp) : {
            < c, cpNew > = bind(cp, rt, c);
            return < c, pt[child = cpNew][@rtype=rt] >;
        }
        
        case tvarBecomesNode(nt, n, l, cp) : {
            < c, cpNew > = bind(cp, rt, c);
            return < c, pt[child = cpNew] >;
        }
        
        case concreteSyntaxNode(nt,plist) : {
            if (comparable(pt@rtype, rt)) {
                return < c, pt >;
            }
            throw "Bind error, cannot bind subject of type <prettyPrintType(rt)> to pattern of type <prettyPrintType(pt@rtype)>";
        }
    }
    
    throw "Bind Error: Cannot bind pattern tree <pt> to type <prettyPrintType(rt)>";
}

@doc{Check the type of Rascal statements: Assert (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`assert <Expression e>;`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    if (isFailType(t1))
        return markLocationFailed(c, stmt@\loc, t1);
    else if (!isBoolType(t1))
        return markLocationFailed(c, stmt@\loc, makeFailType("Invalid type <prettyPrintType(t1)>, expected expression of type bool", e@\loc));
    return markLocationType(c, stmt@\loc, \bool());
}

@doc{Check the type of Rascal statements: AssertWithMessage (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`assert <Expression e> : <Expression em>;`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    < c, t2 > = checkExp(em, c);
    set[Symbol] failures = { };
    
    if (isFailType(t1)) failures += t1;
    if (!isFailType(t1) && !isBoolType(t1))
        failures += makeFailType("Invalid type <prettyPrintType(t1)>, expected expression of type bool", e@\loc);
        
    if (isFailType(t2)) failures += t2;
    if (!isFailType(t2) && !isStrType(t2))
        failures += makeFailType("Invalid type <prettyPrintType(t2)>, expected expression of type str", em@\loc);
        
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \bool());
}

@doc{Check the type of Rascal statements: Expression (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<Expression e>;`, Configuration c) {
    < c, t1 > = checkExp(e,c);
    if (isFailType(t1))
        return markLocationFailed(c, stmt@\loc, t1);
    else
        return markLocationType(c, stmt@\loc, t1);
}

@doc{Check the type of Rascal statements: Visit}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> <Visit v>`, Configuration c) {
    // Treat the visit as a block, since the label has a defined scope from the start to
    // the end of the visit, but not outside of it.
    cVisit = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cVisit,labelName)) cVisit = addMessage(cVisit,error("Cannot reuse label names: <n>", lbl@\loc));
        cVisit = addLabel(cVisit,labelName,lbl@\loc,visitLabel());
        cVisit.labelStack = labelStackItem(labelName, visitLabel(), \void()) + cVisit.labelStack;
    } else {
        cVisit.labelStack = labelStackItem(RSimpleName(""), visitLabel(), \void()) + cVisit.labelStack;
    }
    
    < cVisit, vt > = checkVisit(v,cVisit);

    // Remove the added item from the label stack and then exit the block we created above,
    // which will clear up the added label name, removing it from scope.
    cVisit.labelStack = tail(cVisit.labelStack);
    c = exitBlock(cVisit,c);

    if (isFailType(vt)) return markLocationFailed(c,stmt@\loc,vt);
    return markLocationType(c,stmt@\loc,vt);
}

@doc{Check the type of Rascal statements: While}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> while ( <{Expression ","}+ conds> ) <Statement bdy>`, Configuration c) {
    set[Symbol] failures = { };

    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cWhile = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cWhile,labelName)) cWhile = addMessage(cWhile,error("Cannot reuse label names: <n>", lbl@\loc));
        cWhile = addLabel(cWhile,labelName,lbl@\loc,whileLabel());
        cWhile.labelStack = labelStackItem(labelName, whileLabel(), \void()) + cWhile.labelStack;
    } else {
        cWhile.labelStack = labelStackItem(RSimpleName(""), whileLabel(), \void()) + cWhile.labelStack;
    }

    // Enter a boolean scope, for both the conditionals and the statement body.
    // TODO: Technically, this scope does not include the label.
    cWhileBool = enterBooleanScope(cWhile, stmt@\loc);

    // Process all the conditions; these can add names into the scope   
    for (cond <- conds) { 
        < cWhileBool, t1 > = checkExp(cond, cWhileBool);
        if (isFailType(t1)) 
            failures += t1;
        else if (!isBoolType(t1))
            failures += makeFailType("Unexpected type <prettyPrintType(t1)>, expected type bool", cond@\loc);
    }

    // Check the body of the loop               
    cWhileBody = enterBlock(cWhileBool, bdy@\loc);
    < cWhileBody, t2 > = checkStmt(bdy, cWhileBody);
    
    // See if the loop changed the type of any vars declared outside of the loop.
    modifiedVars = { vl | vl <- (cWhileBody.fcvEnv<1> & cWhile.fcvEnv<1>), variable(_,rt1,true,_,_) := cWhileBody.store[vl], variable(_,rt2,true,_,_) := cWhile.store[vl], !equivalent(rt1,rt2) };
    modifiedVarValues = ( vl : cWhileBody.store[vl].rtype | vl <- modifiedVars );
    
    cWhileBool = exitBlock(cWhileBody, cWhileBool);
    if (isFailType(t2)) failures += t2;
    
    // If the loop did change the type of any of these vars, iterate again and see if the type keeps changing. If so,
    // the loop does not cause the type to stabilize, in which case we want to issue a warning and set the type of
    // the var in question to value.
    if (size(modifiedVars) > 0) {
        cWhileBody = enterBlock(cWhileBool, bdy@\loc);
        < cWhileBody, t2 > = checkStmt(bdy, cWhileBody);
        modifiedVars2 = { vl | vl <- modifiedVars, !equivalent(cWhileBody.store[vl].rtype,modifiedVarValues[vl]) };
        cWhileBool = exitBlock(cWhileBody, cWhileBool);
        if (isFailType(t2)) failures += t2;
        
        for (vl <- modifiedVars2) {
            cWhileBool.store[vl].rtype = \value();
            cWhileBool = addMessage(cWhileBool, error("Type of variable <prettyPrintName(cWhileBool.store[vl].rname)> does not stabilize in loop", bdy@\loc));
        }               
    }

    // Exit back to the block scope
    cWhile = exitBooleanScope(cWhileBool, cWhile);

    // Get out any append info, which is used to calculate the loop type, and then
    // pop the label stack.         
    loopElementType  = head(cWhile.labelStack).labelType;
    cWhile.labelStack = tail(cWhile.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cWhile, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \list(loopElementType)); 
}

@doc{Check the type of Rascal statements: DoWhile}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> do <Statement bdy> while (<Expression cond>);`, Configuration c) {
    set[Symbol] failures = { };

    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cDoWhile = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cDoWhile,labelName)) cDoWhile = addMessage(cDoWhile,error("Cannot reuse label names: <n>", lbl@\loc));
        cDoWhile = addLabel(cDoWhile,labelName,lbl@\loc,doWhileLabel());
        cDoWhile.labelStack = labelStackItem(labelName, doWhileLabel(), \void()) + cDoWhile.labelStack;
    } else {
        cDoWhile.labelStack = labelStackItem(RSimpleName(""), doWhileLabel(), \void()) + cDoWhile.labelStack;
    }

    // Check the body of the loop               
    cDoWhileBody = enterBlock(cDoWhile, bdy@\loc);
    < cDoWhileBody, t2 > = checkStmt(bdy, cDoWhileBody);
    cDoWhile = exitBlock(cDoWhileBody, cDoWhile);
    if (isFailType(t2)) failures += t2;

    // Check the loop condition 
    cDoWhileBool = enterBooleanScope(cDoWhile,cond@\loc);
    < cDoWhileBool, t1 > = checkExp(cond, cDoWhileBool);
    cDoWhile = exitBooleanScope(cDoWhileBool, cDoWhile);
    
    if (isFailType(t1)) 
        failures += t1;
    else if (!isBoolType(t1))
        failures += makeFailType("Unexpected type <prettyPrintType(t1)>, expected type bool", cond@\loc);

    // Get out any append info, which is used to calculate the loop type, and then
    // pop the label stack.         
    loopElementType = head(cDoWhile.labelStack).labelType;
    cDoWhile.labelStack = tail(cDoWhile.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cDoWhile, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \list(loopElementType)); 
}

@doc{Check the type of Rascal statements: For}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> for ( <{Expression ","}+ gens> ) <Statement bdy>`, Configuration c) {
    set[Symbol] failures = { };

    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cFor = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cFor,labelName)) cFor = addMessage(cFor,error("Cannot reuse label names: <n>", lbl@\loc));
        cFor = addLabel(cFor,labelName,lbl@\loc,forLabel());
        cFor.labelStack = labelStackItem(labelName, forLabel(), \void()) + cFor.labelStack;
    } else {
        cFor.labelStack = labelStackItem(RSimpleName(""), forLabel(), \void()) + cFor.labelStack;
    }

    // Enter a boolean scope, for both the conditionals and the statement body.
    // TODO: Technically, this scope does not include the label.
    cForBool = enterBooleanScope(cFor, stmt@\loc);

    // Process all the generators; these can add names into the scope   
    for (gen <- gens) { 
        < cForBool, t1 > = checkExp(gen, cForBool);
        if (isFailType(t1)) 
            failures += t1;
        else if (!isBoolType(t1))
            failures += makeFailType("Unexpected type <prettyPrintType(t1)>, expected type bool", gen@\loc);
    }

    // Check the body of the loop       
    cForBody = enterBlock(cForBool, bdy@\loc);      
    < cForBody, t2 > = checkStmt(bdy, cForBody);
    cForBool = exitBlock(cForBody, cForBool);
    if (isFailType(t2)) failures += t2;

    // Exit back to the block scope
    cFor = exitBooleanScope(cForBool, cFor);

    // Get out any append info, which is used to calculate the loop type, and then
    // pop the label stack.         
    loopElementType = head(cFor.labelStack).labelType;
    cFor.labelStack = tail(cFor.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cFor, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \list(loopElementType)); 
}

@doc{Check the type of Rascal statements: IfThen (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> if ( <{Expression ","}+ conds> ) <Statement bdy>`, Configuration c) {
    set[Symbol] failures = { };

    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cIf = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cIf,labelName)) cIf = addMessage(cIf,error("Cannot reuse label names: <n>", lbl@\loc));
        cIf = addLabel(cIf,labelName,lbl@\loc,ifLabel());
        cIf.labelStack = labelStackItem(labelName, ifLabel(), \void()) + cIf.labelStack;
    } else {
        cIf.labelStack = labelStackItem(RSimpleName(""), ifLabel(), \void()) + cIf.labelStack;
    }

    // Enter a boolean scope, for both the conditionals and the statement body.
    // TODO: Technically, this scope does not include the label.
    cIfBool = enterBooleanScope(cIf, stmt@\loc);

    // Process all the conditions; these can add names into the scope   
    for (cond <- conds) { 
        < cIfBool, t1 > = checkExp(cond, cIfBool);
        if (isFailType(t1)) 
            failures += t1;
        else if (!isBoolType(t1))
            failures += makeFailType("Unexpected type <prettyPrintType(t1)>, expected type bool", cond@\loc);
    }

    // Check the body of the conditional.
    cIfThen = enterBlock(cIfBool, bdy@\loc);                
    < cIfThen, t2 > = checkStmt(bdy, cIfThen);
    cIfBool = exitBlock(cIfThen, cIfBool);
    if (isFailType(t2)) failures += t2;

    // Exit back to the block scope
    cIf = exitBooleanScope(cIfBool, cIf);

    // and, pop the label stack...
    cIf.labelStack = tail(cIf.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cIf, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \value());    
}

@doc{Check the type of Rascal statements: IfThenElse (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> if ( <{Expression ","}+ conds> ) <Statement thenBody> else <Statement elseBody>`, Configuration c) {
    set[Symbol] failures = { };

    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cIf = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cIf,labelName)) cIf = addMessage(cIf,error("Cannot reuse label names: <n>", lbl@\loc));
        cIf = addLabel(cIf,labelName,lbl@\loc,ifLabel());
        cIf.labelStack = labelStackItem(labelName, ifLabel(), \void()) + cIf.labelStack;
    } else {
        cIf.labelStack = labelStackItem(RSimpleName(""), ifLabel(), \void()) + cIf.labelStack;
    }

    // Enter a boolean scope, for both the conditionals and the statement body.
    // TODO: Technically, this scope does not include the label.
    cIfBool = enterBooleanScope(cIf, stmt@\loc);

    // Process all the conditions; these can add names into the scope   
    for (cond <- conds) { 
        < cIfBool, t1 > = checkExp(cond, cIfBool);
        if (isFailType(t1)) 
            failures += t1;
        else if (!isBoolType(t1))
            failures += makeFailType("Unexpected type <prettyPrintType(t1)>, expected type bool", cond@\loc);
    }

    // Check the then body of the conditional. We enter a new block to make sure
    // that we remove any declarations (for instance, if the body is just a
    // variable declaration).
    cIfThen = enterBlock(cIfBool, thenBody@\loc);               
    < cIfThen, t2 > = checkStmt(thenBody, cIfThen);
    cIfBool = exitBlock(cIfThen, cIfBool);
    if (isFailType(t2)) failures += t2;

    // Do the same for the else body.
    cIfElse = enterBlock(cIfBool, elseBody@\loc);
    < cIfElse, t3 > = checkStmt(elseBody, cIfElse);
    cIfBool = exitBlock(cIfElse, cIfBool);
    if (isFailType(t3)) failures += t3;

    // Exit back to the block scope
    cIf = exitBooleanScope(cIfBool, cIf);

    // and, pop the label stack...
    cIf.labelStack = tail(cIf.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cIf, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, lub(t2,t3));  
}

@doc{Check the type of Rascal statements: Switch (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> switch ( <Expression e> ) { <Case+ cases> }`, Configuration c) {
    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cSwitch = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cSwitch,labelName)) cSwitch = addMessage(cSwitch,error("Cannot reuse label names: <n>", lbl@\loc));
        cSwitch = addLabel(cSwitch,labelName,lbl@\loc,switchLabel());
        cSwitch.labelStack = labelStackItem(labelName, switchLabel(), \void()) + cSwitch.labelStack;
    } else {
        cSwitch.labelStack = labelStackItem(RSimpleName(""), switchLabel(), \void()) + cSwitch.labelStack;
    }

    // Enter a boolean scope, for both the conditionals and the statement body.
    // TODO: Technically, this scope does not include the label.
    cSwitchBool = enterBooleanScope(cSwitch, stmt@\loc);

    // Now, check the expression and the various cases. If the expression is a failure, just pass
    // in value as the expected type so we don't cascade even more errors.
    < cSwitchBool, t1 > = checkExp(e,cSwitchBool);
    for (cItem <- cases) {
        cSwitchBody = enterBlock(cSwitchBool, cItem@\loc);
        cSwitchBody = checkCase(cItem, isFailType(t1) ? \value() : t1, cSwitchBody);
        cSwitchBool = exitBlock(cSwitchBody, cSwitchBool);
    }
    
    // Exit back to the block scope
    cSwitch = exitBooleanScope(cSwitchBool, cSwitch);

    // and, pop the label stack...
    cSwitch.labelStack = tail(cSwitch.labelStack);

    // Now, return to the scope on entry, removing the label
    c = exitBlock(cSwitch, c);
    
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Fail (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`fail <Target target>;`, Configuration c) {
    if ((Target)`<Name n>` := target) {
        rn = convertName(n);
        // TODO: Check to see what category the label is in?
        if (rn notin c.labelEnv) return markLocationFailed(c, stmt@\loc, makeFailType("Target label not defined", stmt@\loc));
    }   
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Break (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`break <Target target>;`, Configuration c) {
    if ((Target)`<Name n>` := target) {
        rn = convertName(n);
        // TODO: Check to see what category the label is in?
        if (rn notin c.labelEnv) return markLocationFailed(c, stmt@\loc, makeFailType("Target label not defined", stmt@\loc));
    }   
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Continue (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`continue <Target target>;`, Configuration c) {
    if ((Target)`<Name n>` := target) {
        rn = convertName(n);
        // TODO: Check to see what category the label is in?
        if (rn notin c.labelEnv) return markLocationFailed(c, stmt@\loc, makeFailType("Target label not defined", stmt@\loc));
    }   
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Filter (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`filter;`, Configuration c) {
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Solve (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`solve ( <{QualifiedName ","}+ vars> <Bound bound> ) <Statement body>`, Configuration c) {
    set[Symbol] failures = { };
    
    // First, check the names. Note: the names must exist already, we do not bind them here. Instead,
    // we make sure they all exist.
    for (qn <- vars) {
        n = convertName(qn);
        if (fcvExists(c, n)) {
            c.uses = c.uses + < c.fcvEnv[n], qn@\loc >;
            c.usedIn[qn@\loc] = head(c.stack);
        } else {
            failures = failures + makeFailType("Name <prettyPrintName(n)> is not in scope", qn@\loc);
        }
    }
    
    // Next, check the bound. It can be empty, but, if not, it should be something
    // that evaluates to an int.
    if (Bound bnd:(Bound)`; <Expression be>` := bound) {
        < c, tb > = checkExp(be, c);
        if (isFailType(tb))
            failures = failures + tb;
        else if (!isIntType(tb))
            failures = failures + makeFailType("Type of bound should be int, not <prettyPrintType(tb)>", bound@\loc);
    }
    
    // Finally, check the body.
    cBody = enterBlock(c, body@\loc);
    < cBody, tbody > = checkStmt(body, cBody);
    c = exitBlock(cBody, c);
    if (isFailType(tbody)) failures = failures + tbody;
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, tbody);
}

@doc{Check the type of Rascal statements: Try (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`try <Statement body> <Catch+ handlers>`, Configuration c) {
    // TODO: For now, returning void -- should we instead lub the results of the body and all
    // the catch blocks?
    cBody = enterBlock(c, body@\loc);
    < cBody, t1 > = checkStmt(body, cBody);
    c = exitBlock(cBody, c);
    for (handler <- handlers) c = checkCatch(handler, c);
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: TryFinally (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`try <Statement body> <Catch+ handlers> finally <Statement fbody>`, Configuration c) {
    cBody = enterBlock(c, body@\loc);
    < cBody, t1 > = checkStmt(body, cBody);
    c = exitBlock(cBody, c);
    for (handler <- handlers) c = checkCatch(handler, c);
    < c, tf > = checkStmt(fbody, c);
    return markLocationType(c, stmt@\loc, tf);
}

@doc{Check the type of Rascal statements: NonEmptyBlock (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<Label lbl> { <Statement+ stmts> }`, Configuration c) {
    // Treat this construct as a block, since the label has a defined scope from the start to
    // the end of the construct, but not outside of it.
    cBlock = enterBlock(c,stmt@\loc);

    // Add the appropriate label into the label stack and label environment. If we have a blank
    // label we still add it to the stack, but not to the environment, since we cannot access it
    // using a name.
    if ((Label)`<Name n> :` := lbl) {
        labelName = convertName(n);
        if (labelExists(cBlock,labelName)) cBlock = addMessage(cBlock,error("Cannot reuse label names: <n>", lbl@\loc));
        cBlock = addLabel(cBlock,labelName,lbl@\loc,blockLabel());
        cBlock.labelStack = labelStackItem(labelName, blockLabel(), \void()) + cBlock.labelStack;
    } else {
        cBlock.labelStack = labelStackItem(RSimpleName(""), blockLabel(), \void()) + cBlock.labelStack;
    }

	< cBlock, st > = checkStatementSequence([ssi | ssi <- stmts], cBlock);

    // Pop the label stack...
    cBlock.labelStack = tail(cBlock.labelStack);

    // ... and return to the scope on entry, removing the label
    c = exitBlock(cBlock, c);

    if (isFailType(st))
        return markLocationFailed(c, stmt@\loc, st);
    else
        return markLocationType(c, stmt@\loc, st); 
}

@doc{Check the type of Rascal statements: EmptyStatement (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`;`, Configuration c) {
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: GlobalDirective (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`global <Type t> <{QualifiedName ","}+ names>;`, Configuration c) {
    throw "Not Implemented";
}

@doc{Check the type of Rascal statements: Assignment}
public CheckResult checkStmt(Statement stmt:(Statement)`<Assignable a> <Assignment op> <Statement s>`, Configuration c) {
    // First, evaluate the statement, which gives us the type that we will assign into the assignable. If this is a
    // failure, we cannot figure out the type of the assignable, so just return right away.
    < c, t1 > = checkStmt(s, c);
    if (isFailType(t1)) return markLocationFailed(c, stmt@\loc, t1);
    < c, t2 > = checkAssignment(op, a, t1, stmt@\loc, c);
    if (isFailType(t2)) return markLocationFailed(c, stmt@\loc, t2);
    return markLocationType(c, stmt@\loc, t2);
}

@doc{Check the type of Rascal statements: Return (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`return <Statement s>`, Configuration c) {
    < c, t1 > = checkStmt(s, c);
    if (!isFailType(t1) && !subtype(t1, c.expectedReturnType))
        return markLocationFailed(c, stmt@\loc, makeFailType("Invalid return type <prettyPrintType(t1)>, expected return type <prettyPrintType(c.expectedReturnType)>", stmt@\loc)); 
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Throw (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`throw <Statement s>`, Configuration c) {
    < c, t1 > = checkStmt(s, c);
    return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Insert (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`insert <DataTarget dt> <Statement s>`, Configuration c) {
    set[Symbol] failures = { };

    < c, t1 > = checkStmt(s, c);
    if (isFailType(t1)) failures += t1;

    labelName = RSimpleName("");
    if ((DataTarget)`<Name n>:` := dt) {
        labelName = convertName(n);
        // TODO: Check to see what category the label is in?
        if (labelName notin c.labelEnv) {
            failures += makeFailType("Target label not defined", dt@\loc);
        } else if (visitLabel() !:= c.labelEnv[labelName]) {
            failures += makeFailType("Target label must refer to a visit statement or expression", dt@\loc);
        }
    }
    
    if (labelTypeInStack(c, {visitLabel()})) {
        if (labelTypeInStack(c, {caseLabel()})) {
            expectedType = getFirstLabeledType(c,{caseLabel()});
            if (!isFailType(t1) && !subtype(t1,expectedType)) {
                failures += makeFailType("Inserted type <prettyPrintType(t1)> must be a subtype of case type <prettyPrintType(expectedType)>", stmt@\loc);
            } 
        } else {
            failures += makeFailType("Cannot insert outside the scope of a non-replacement case action", stmt@\loc);
        }
    } else {
        failures += makeFailType("Cannot insert outside the scope of a visit", stmt@\loc);
    }
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: Append (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`append <DataTarget dt> <Statement s>`, Configuration c) {
    set[Symbol] failures = { };

    < c, t1 > = checkStmt(s, c);
    if (isFailType(t1)) failures += t1;
    if ((DataTarget)`<Name n>:` := dt) {
        rn = convertName(n);
        // TODO: Check to see what category the label is in?
        if (rn notin c.labelEnv) 
            failures += makeFailType("Target label not defined", dt@\loc);
        else
            c = addAppendTypeInfo(c, t1, rn, { forLabel(), whileLabel(), doWhileLabel() }, stmt@\loc);
    } else {
        c = addAppendTypeInfo(c, t1, { forLabel(), whileLabel(), doWhileLabel() }, stmt@\loc);
    }
    
    if (size(failures) > 0)
        return markLocationFailed(c, stmt@\loc, failures);
    else
        return markLocationType(c, stmt@\loc, \void());
}

@doc{Check the type of Rascal statements: FunctionDeclaration (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<FunctionDeclaration fd>`, Configuration c) {
    c = checkFunctionDeclaration(fd, true, c);
    return < c, \void() >;
}

@doc{Check the type of Rascal statements: LocalVariableDeclaration (DONE)}
public CheckResult checkStmt(Statement stmt:(Statement)`<LocalVariableDeclaration vd>;`, Configuration c) {
    if ((LocalVariableDeclaration)`<Declarator d>` := vd || (LocalVariableDeclaration)`dynamic <Declarator d>` := vd) {
        if ((Declarator)`<Type t> <{Variable ","}+ vars>` := d) {
            < c, rt > = convertAndExpandType(t,c);
            
            for (v <- vars) {
                if ((Variable)`<Name n> = <Expression init>` := v || (Variable)`<Name n>` := v) {
                    if ((Variable)`<Name n> = <Expression init>` := v) {
                        < c, t1 > = checkExp(init, c);
                        if (!isFailType(t1) && !subtype(t1,rt)) 
                            c = addScopeMessage(c, error("Initializer type <prettyPrintType(t1)> not assignable to variable of type <prettyPrintType(rt)>", v@\loc));                       
                    }
                                        
                    RName rn = convertName(n);
                    c = addVariable(c, rn, false, n@\loc, rt);
                } 
            }
        }
    }
    
    return < c, \void() >;
}

public test bool callOrTreeExp1() = < _, \node()> := checkExp(parseExpression("\"mynode\"()"), newConfiguration());
public test bool callOrTreeExp2() = < _, \node()> := checkExp(parseExpression("\"mynode\"(1,2,false,5.3)"), newConfiguration());
public test bool callOrTreeExp3() = < _, \loc()> := checkExp(parseExpression("|project://Test/Project|(1,2,\<3,4\>,\<5,6\>)"), newConfiguration());
public test bool callOrTreeExp4() = < _, t> := checkExp(parseExpression("|project://Test/Project|(1.2,2,\<3,4\>,\<5,6\>)"), newConfiguration()) && isFailType(t);
public test bool callOrTreeExp5() = < _, t> := checkExp(parseExpression("|project://Test/Project|(1,2.3,\<3,4\>,\<5,6\>)"), newConfiguration()) && isFailType(t);
public test bool callOrTreeExp6() = < _, t> := checkExp(parseExpression("|project://Test/Project|(1,2,\<3.4,4\>,\<5,6\>)"), newConfiguration()) && isFailType(t);
public test bool callOrTreeExp7() = < _, t> := checkExp(parseExpression("|project://Test/Project|(1,2,\<3,4\>,\<5.5,6\>)"), newConfiguration()) && isFailType(t);
public test bool callOrTreeExp8() = < _, t> := checkExp(parseExpression("|project://Test/Project|(1,2)"), newConfiguration()) && isFailType(t);

public test bool reducerExp1() = < _, \int() > := checkExp(parseExpression("( 3 | it + x | x \<- [1..10] )"), addModule(newConfiguration(),RSimpleName("Tests"), |file:///tmp|));
public test bool reducerExp2() = < _, \real() > := checkExp(parseExpression("( 3.4 | it + x | x \<- [1..10] )"), addModule(newConfiguration(),RSimpleName("Tests"), |file:///tmp|));
public test bool reducerExp3() = < _, \list(\int()) > := checkExp(parseExpression("( [3] | it + x | x \<- [1..10] )"), addModule(newConfiguration(),RSimpleName("Tests"), |file:///tmp|));
public test bool reducerExp4() = < _, t > := checkExp(parseExpression("( [] | [it] | x \<- [1..10] )"), addModule(newConfiguration(),RSimpleName("Tests"), |file:///tmp|)) && isFailType(t);
public test bool reducerExp5() = < _, \list(\int()) > := checkExp(parseExpression("( [] | it + x | x \<- [1..10] )"), addModule(newConfiguration(),RSimpleName("Tests"), |file:///tmp|));

public test bool literalExp1() = < _, \int()> := checkExp(parseExpression("1"), newConfiguration());
public test bool literalExp2() = < _, \real()> := checkExp(parseExpression("1.1"), newConfiguration());
public test bool literalExp3() = < _, \rat()> := checkExp(parseExpression("1r2"), newConfiguration());
public test bool literalExp4() = < _, \bool()> := checkExp(parseExpression("true"), newConfiguration());
public test bool literalExp5() = < _, \bool()> := checkExp(parseExpression("false"), newConfiguration());
public test bool literalExp6() = < _, \datetime()> := checkExp(parseExpression("$2012-01-27$"), newConfiguration());
public test bool literalExp7() = < _, \str()> := checkExp(parseExpression("\"hello world!\""), newConfiguration());
public test bool literalExp8() = < _, \loc()> := checkExp(parseExpression("|project://MyLang/src/myfile.rsc|"), newConfiguration());

public test bool assertStmt1() = < _, \bool()> := checkStmt(parseStatement("assert true;"), newConfiguration());
public test bool assertStmt2() = < _, t> := checkStmt(parseStatement("assert 5;"), newConfiguration()) && isFailType(t);
public test bool assertStmt3() = < _, \bool()> := checkStmt(parseStatement("assert true : \"or else!\";"), newConfiguration());
public test bool assertStmt4() = < _, t> := checkStmt(parseStatement("assert 5 : \"or else!\";"), newConfiguration()) && isFailType(t);
public test bool assertStmt5() = < _, t> := checkStmt(parseStatement("assert true : 10;"), newConfiguration()) && isFailType(t);
public test bool assertStmt6() = < _, t> := checkStmt(parseStatement("assert 5 : 10;"), newConfiguration()) && isFailType(t);
 
public test bool listExp1() = < _, \list(\void()) > := checkExp(parseExpression("[]"), newConfiguration());

public test bool setExp1() = < _, \set(\void()) > := checkExp(parseExpression("{}"), newConfiguration());

public test bool mapExp1() = < _, \map(\void(),\void()) > := checkExp(parseExpression("( )"), newConfiguration());








@doc{A compact representation of assignables}
data AssignableTree 
    = bracketNode(AssignableTree child)
    | variableNode(RName name)
    | subscriptNode(AssignableTree receiver, Symbol subscriptType)
    | sliceNode(AssignableTree receiver, Symbol firstType, Symbol lastType)
    | sliceStepNode(AssignableTree receiver, Symbol firstType, Symbol secondType, Symbol lastType)
    | fieldAccessNode(AssignableTree receiver, RName name)
    | ifDefinedOrDefaultNode(AssignableTree receiver, Symbol defaultType)
    | constructorNode(RName name, list[AssignableTree] children)
    | tupleNodeAT(list[AssignableTree] children)
    | annotationNode(AssignableTree receiver, RName name)
    ;
    
@doc{Mark assignable trees with the source location of the assignable}
public anno loc AssignableTree@at;

@doc{Allows AssignableTree nodes to keep track of which ids they define.}
public anno set[int] AssignableTree@defs;

@doc{Allows AssignableTree nodes to be annotated with types.}
public anno Symbol AssignableTree@otype;
public anno Symbol AssignableTree@atype;

@doc{Result of building the assignable tree.}
alias ATResult = tuple[Configuration, AssignableTree];

@doc{Extract a tree representation of the assignable and perform basic checks: Bracket (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`(<Assignable ar>)`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, top, c);
    return < c, bracketNode(atree)[@atype=atree@atype][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the assignable and perform basic checks: Variable (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<QualifiedName qn>`, bool top, Configuration c) {
    n = convertName(qn);
    if (RSimpleName("_") == n) {
        rt = \inferred(c.uniqueify);
        c.uniqueify = c.uniqueify + 1;  
        c = addUnnamedVariable(c, qn@\loc, rt);
        return < c, variableNode(n)[@atype=rt][@at=assn@\loc][@defs={c.nextLoc-1}] >;
    } else if (fcvExists(c, n)) {
        if (variable(_,_,_,_,_) := c.store[c.fcvEnv[n]]) {
            c.uses = c.uses + < c.fcvEnv[n], assn@\loc >;
            c.usedIn[assn@\loc] = head(c.stack);
            rt = c.store[c.fcvEnv[n]].rtype;
            c = addNameWarning(c,n,assn@\loc);
            return < c, variableNode(n)[@atype=rt][@at=assn@\loc] >;
        } else {
            c.uses = c.uses + < c.fcvEnv[n], assn@\loc >;
            c.usedIn[assn@\loc] = head(c.stack);
            return < c, variableNode(n)[@atype=makeFailType("Cannot assign to an existing constructor, production, or function name",assn@\loc)][@at=assn@\loc] >;
        }
    } else {
        rt = \inferred(c.uniqueify);
        c.uniqueify = c.uniqueify + 1;  
        c = addVariable(c, n, true, qn@\loc, rt);
        return < c, variableNode(n)[@atype=rt][@at=assn@\loc] >;
    }
}

@doc{Extract a tree representation of the assignable and perform basic checks: Subscript (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> [ <Expression sub> ]`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, false, c);
    < c, tsub > = checkExp(sub, c);
    
    if (isFailType(atree@atype) || isFailType(tsub))
        return < c, subscriptNode(atree,tsub)[@atype=collapseFailTypes({atree@atype,tsub})][@at=assn@\loc] >;

    if (!concreteType(atree@atype)) {
        failtype = makeFailType("Assignable <ar> must have an actual type before subscripting", assn@\loc);
        return < c, subscriptNode(atree,tsub)[@atype=failtype][@at=assn@\loc] >;
    }

    if (isListType(atree@atype) && isIntType(tsub))
        return < c, subscriptNode(atree,tsub)[@atype=getListElementType(atree@atype)][@at=assn@\loc] >;

    if (isNodeType(atree@atype) && isIntType(tsub))
        return < c, subscriptNode(atree,tsub)[@atype=\value()][@at=assn@\loc] >;

    if (isTupleType(atree@atype) && isIntType(tsub))
        return < c, subscriptNode(atree,tsub)[@atype=\value()][@at=assn@\loc] >;

    if (isMapType(atree@atype)) {
        if (avar:variableNode(vname) := atree) {
            if (!equivalent(getMapDomainType(atree@atype), tsub)) {
                if (top) {
                    if (c.store[c.fcvEnv[vname]].inferred) {
                        Symbol newMapType = \map(lub(getMapDomainType(c.store[c.fcvEnv[vname]].rtype),tsub),getMapRangeType(c.store[c.fcvEnv[vname]].rtype));
                        c.store[c.fcvEnv[vname]].rtype = newMapType;
                        atree@atype=newMapType;
                    }
                }
            }
        }
        if (!comparable(getMapDomainType(atree@atype), tsub))
            atree@atype = makeFailType("Cannot subscript map of type <prettyPrintType(atree@atype)> using subscript of type <prettyPrintType(tsub)>", assn@\loc);
        return < c, subscriptNode(atree,tsub)[@atype=getMapRangeType(atree@atype)][@at=assn@\loc] >;
    }

    if (isRelType(atree@atype) && size(getRelFields(atree@atype)) == 2 && subtype(tsub,getRelFields(atree@atype)[0]))
        return < c, subscriptNode(atree,tsub)[@atype=getRelFields(atree@atype)[1]][@at=assn@\loc] >;

    return < c, subscriptNode(atree,tsub)[@atype=makeFailType("Cannot subscript assignable of type <prettyPrintType(atree@atype)>",assn@\loc)][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the assignable and perform basic checks: Slice (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> [ <OptionalExpression optFirst> .. <OptionalExpression optLast> ]`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, false, c);
    
    tFirst = makeIntType();
    tLast = makeIntType();
    
    if ((OptionalExpression)`<Expression eFirst>` := optFirst)
    	< c, tFirst > = checkExp(eFirst, c);
    
    if ((OptionalExpression)`<Expression eLast>` := optLast)
    	< c, tLast > = checkExp(eLast, c);
    
    if (isFailType(atree@atype) || isFailType(tFirst) || isFailType(tLast))
        return < c, sliceNode(atree,tFirst,tLast)[@atype=collapseFailTypes({atree@atype,tFirst,tLast})][@at=assn@\loc] >;

    if (!concreteType(atree@atype)) {
        failtype = makeFailType("Assignable <ar> must have an actual type before subscripting", assn@\loc);
        return < c, sliceNode(atree,tFirst,tLast)[@atype=failtype][@at=assn@\loc] >;
    }

    if (isListType(atree@atype) && isIntType(tFirst) && isIntType(tLast))
        return < c, sliceNode(atree,tFirst,tLast)[@atype=atree@atype][@at=assn@\loc] >;

    if (isNodeType(atree@atype) && isIntType(tFirst) && isIntType(tLast))
        return < c, sliceNode(atree,tFirst,tLast)[@atype=atree@atype][@at=assn@\loc] >;

    if (isStrType(atree@atype) && isIntType(tFirst) && isIntType(tLast))
        return < c, sliceNode(atree,tFirst,tLast)[@atype=atree@atype][@at=assn@\loc] >;

	if (!isIntType(tFirst) || !isIntType(tLast))
		return < c, sliceNode(atree,tFirst,tLast)[@atype=makeFailType("Indexes must be of type int, given: <prettyPrintType(tFirst)>, <prettyPrintType(tLast)>",assn@\loc)][@at=assn@\loc] >;
		
    return < c, sliceNode(atree,tFirst,tLast)[@atype=makeFailType("Cannot use slicing to assign into type <prettyPrintType(atree@atype)>",assn@\loc)][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the assignable and perform basic checks: Slice Step (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> [ <OptionalExpression optFirst>, <Expression second> .. <OptionalExpression optLast> ]`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, false, c);
    
    tFirst = makeIntType();
    if ((OptionalExpression)`<Expression eFirst>` := optFirst)
    	< c, tFirst > = checkExp(eFirst, c);
    
    < c, tSecond > = checkExp(second, c);
        
    tLast = makeIntType();
    if ((OptionalExpression)`<Expression eLast>` := optLast)
    	< c, tLast > = checkExp(eLast, c);
    
    if (isFailType(atree@atype) || isFailType(tFirst) || isFailType(tSecond) || isFailType(tLast))
        return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=collapseFailTypes({atree@atype,tFirst,tSecond,tLast})][@at=assn@\loc] >;

    if (!concreteType(atree@atype)) {
        failtype = makeFailType("Assignable <ar> must have an actual type before subscripting", assn@\loc);
        return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=failtype][@at=assn@\loc] >;
    }

    if (isListType(atree@atype) && isIntType(tFirst) && isIntType(tSecond) && isIntType(tLast))
        return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=atree@atype][@at=assn@\loc] >;

    if (isNodeType(atree@atype) && isIntType(tFirst) && isIntType(tSecond) && isIntType(tLast))
        return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=atree@atype][@at=assn@\loc] >;

    if (isStrType(atree@atype) && isIntType(tFirst) && isIntType(tSecond) && isIntType(tLast))
        return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=atree@atype][@at=assn@\loc] >;

	if (!isIntType(tFirst) || !isIntType(tSecond) || !isIntType(tLast))
		return < c, sliceNode(atree,tFirst,tLast)[@atype=makeFailType("Indexes must be of type int, given: <prettyPrintType(tFirst)>, <prettyPrintType(tSecond)>, <prettyPrintType(tLast)>",assn@\loc)][@at=assn@\loc] >;
		
    return < c, sliceStepNode(atree,tFirst,tSecond,tLast)[@atype=makeFailType("Cannot use slicing to assign into type <prettyPrintType(atree@atype)>",assn@\loc)][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the pattern and perform basic checks: FieldAccess (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> . <Name fld>`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, false, c);
    fldName = convertName(fld);
    
    if (!isFailType(atree@atype) && !concreteType(atree@atype)) {
        failtype = makeFailType("Assignable <ar> must have an actual type before assigning to a field", assn@\loc);
        return < c, fieldAccessNode(atree, fldName)[@atype=failtype][@at=assn@\loc] >;
    }
    
    if (!isFailType(atree@atype)) {
        tfield = computeFieldType(atree@atype, fldName, assn@\loc, c);
    
        if (!isFailType(tfield)) {
            if ((isLocType(atree@atype) || isDateTimeType(atree@atype)) && "<fld>" notin writableFields[atree@atype]) {
                tfield = makeFailType("Cannot update field <fld> on type <prettyPrintType(atree@atype)>",assn@\loc);
            }
        } 
        
        return < c, fieldAccessNode(atree, fldName)[@atype=tfield][@at=assn@\loc] >;
    }
    
    return < c, fieldAccessNode(atree,fldName)[@atype=atree@atype][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the pattern and perform basic checks: IfDefinedOrDefault (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> ? <Expression dflt>`, bool top, Configuration c) {
    < c, atree > = buildAssignableTree(ar, top, c);
    < c, tdef > = checkExp(dflt, c);
    return < c, ifDefinedOrDefaultNode(atree,tdef)[@atype=lub(atree@atype,tdef)][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the pattern and perform basic checks: Constructor (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Name n> ( <{Assignable ","}+ args> )`, bool top, Configuration c) {
    throw "Not implemented";
}

@doc{Extract a tree representation of the pattern and perform basic checks: Tuple (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`\< <{Assignable ","}+ as> \>`, bool top, Configuration c) {
    list[AssignableTree] trees = [ ];

    for (ai <- as) {
        < c, atree > = buildAssignableTree(ai, true, c);
        trees = trees + atree;
    }
    
    failures = { t@atype | t <- trees, isFailType(t@atype) };
    
    if (size(failures) > 0)
        return < c, tupleNodeAT(trees)[@atype=collapseFailTypes(failures)][@at=assn@\loc] >;
    else
        return < c, tupleNodeAT(trees)[@atype=\tuple([t@atype|t<-trees])][@at=assn@\loc] >;
}

@doc{Extract a tree representation of the pattern and perform basic checks: Annotation (DONE)}
public ATResult buildAssignableTree(Assignable assn:(Assignable)`<Assignable ar> @ <Name an>`, bool top, Configuration c) {
    // First, build the tree for the receiver and convert the annotation name into something
    // we can use below.
    < c, atree > = buildAssignableTree(ar, false, c);
    aname = convertName(an);

    // Then, check the assignment type of the receiver -- for us to proceed, it cannot be fail and
    // it has to be concrete, since we need to know the type before we can look up the annotation.
    if (!isFailType(atree@atype) && !concreteType(atree@atype)) {
        failtype = makeFailType("Assignable <ar> must have an actual type before assigning to an annotation", assn@\loc);
        return < c, annotationNode(atree,aname)[@atype=failtype][@at=assn@\loc] >;
    }
    
    // Now, check the assignment type to make sure it is a type that can carry an annotation.
    if (isNodeType(atree@atype) || isADTType(atree@atype)) {
        // Check to make sure that the annotation is actually declared on the receiver type. We do this
        // by grabbing back all the types on which this annotation is defined and making sure that
        // the current type is a subtype of one of these.
        // TODO: Make sure all annotations of the same name are given equivalent types. This
        // requirement is implicit in the code below, but I'm not sure it's being checked.
        if (aname in c.annotationEnv, true in { subtype(atree@atype,ot) | ot <- c.store[c.annotationEnv[aname]].onTypes }) {
            aType = c.store[c.annotationEnv[aname]].rtype;
            return < c, annotationNode(atree,aname)[@atype=aType][@at=assn@\loc] >;
        } else {
            rt = makeFailType("Annotation <an> not declared on <prettyPrintType(atree@atype)> or its supertypes",assn@\loc);
            return < c, annotationNode(atree,aname)[@atype=rt][@at=assn@\loc] >;
        }
    } else {
        rt = makeFailType("Invalid type: expected node or ADT types, found <prettyPrintType(atree@atype)>", assn@\loc);
        return < c, annotationNode(atree,aname)[@atype=rt][@at=assn@\loc] >;
    }
}

@doc{Check the type of Rascal assignments: IfDefined (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`?=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // Now, using the subject type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, st, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind subject type <prettyPrintType(st)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Division (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`/=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a += operation", a@\loc));
    
    // Check to ensure the division is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    rt = computeDivisionType(atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the resulting type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Product (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`*=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a += operation", a@\loc));
    
    // Check to ensure the product is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    rt = computeProductType(atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the result type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Intersection (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`&=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a += operation", a@\loc));
    
    // Check to ensure the intersection is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    < c, rt > = computeIntersectionType(c, atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the subject type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Subtraction (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`-=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a += operation", a@\loc));
    
    // Check to ensure the subtraction is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    < c, rt > = computeSubtractionType(c, atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the result type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Default (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // Now, using the subject type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, st, c);
    } catch msg : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind subject type <prettyPrintType(st)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Check the type of Rascal assignments: Addition (DONE)}
public CheckResult checkAssignment(Assignment assn:(Assignment)`+=`, Assignable a, Symbol st, loc l, Configuration c) {
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a += operation", a@\loc));
    
    // Check to ensure the addition is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    rt = computeAdditionType(atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the result type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{General function to calculate the type of an append.}
Symbol computeAppendType(Symbol t1, Symbol t2, loc l) {
    if (isListType(t1)) return \list(lub(getListElementType(t1),t2));
    return makeFailType("Append not defined on <prettyPrintType(t1)> and <prettyPrintType(t2)>", l);
}

@doc{Check the type of Rascal assignments: Append}
public CheckResult checkAssignment(Assignment assn:(Assignment)`\<\<=`, Assignable a, Symbol st, Configuration c) {
	// TODO: This isn't implemented yet, so we need to verify this is actually the correct type.
    cbak = c;
    < c, atree > = buildAssignableTree(a, true, c);
    if (isFailType(atree@atype)) return markLocationFailed(cbak, a@\loc, atree@atype);

    // If the assignment point is not concrete, we cannot do the assignment -- 
    // the subject type cannot influence the type here.
    if (!concreteType(atree@atype)) return markLocationFailed(cbak, a@\loc, makeFailType("Cannot initialize variables using a \<\< operation", a@\loc));
    
    // Check to ensure the append is valid. If so, the resulting type is the overall
    // type of the assignable, else it is the failure type generated by the operation.
    rt = computeAppendType(atree@atype, st, l);
    if (isFailType(rt)) return markLocationType(c, l, rt);

    // Now, using the result type, try to bind it to the assignable tree
    try {
        < c, atree > = bindAssignable(atree, rt, c);
    } catch : {
        return markLocationFailed(cbak, l, makeFailType("Unable to bind result type <prettyPrintType(rt)> to assignable", l));
    }

    unresolved = { ati | /AssignableTree ati := atree, !((ati@otype)?) || !concreteType(ati@otype) };
    if (size(unresolved) > 0)
        return markLocationFailed(cbak, l, makeFailType("Type of assignable could not be computed", l));
    else {
        c.locationTypes = c.locationTypes + ( atnode@at : atnode@atype | /AssignableTree atnode := atree, (atnode@atype)? );
        return markLocationType(c, l, atree@otype);
    }
}

@doc{Bind variable types to variables in assignables: Bracket}
public ATResult bindAssignable(AssignableTree atree:bracketNode(AssignableTree child), Symbol st, Configuration c) {
    // Since bracketing does not impact anything, binding just passes the type
    // information through to the bracketed assignable node.
    < c, newChild > = bindAssignable(child, st, c);
    return < c, atree[@otype=newChild@otype][@atype=newChild@atype] >;
}

@doc{Bind variable types to variables in assignables: Variable}
public ATResult bindAssignable(AssignableTree atree:variableNode(RName name), Symbol st, Configuration c) {
    // Binding the name involves assigning the binding type. In the case of names with
    // inferred types, we may be able to assign the type directly, or may have to compute
    // the lub of the type. If the name has a defined type, we ensure that the
    // type of the value being assigned is a subtype of the current type.
    
    // TODO: A sensible restriction would be that a name can occur at most once in
    // an assignable IF it is assigned into. We should add that, although it makes
    // more sense to do this when building the assignable tree, not here. This will
    // also prevent odd errors that could occur if a name changes types, such as from
    // an ADT type (with fields) to a node type (without fields).
    
    if (RSimpleName("_") == name) {
        varId = getOneFrom(atree@defs);
        Symbol currentType = c.store[varId].rtype;
        if (isInferredType(currentType)) {
            c.store[varId].rtype = st;
        } else {
            c.store[varId].rtype = lub(currentType, st);
        }
        return < c, atree[@otype=c.store[varId].rtype][@atype=c.store[varId].rtype] >;
    } else {
        Symbol currentType = c.store[c.fcvEnv[name]].rtype;
        if (c.store[c.fcvEnv[name]].inferred) {
            if (isInferredType(currentType)) {
                c.store[c.fcvEnv[name]].rtype = st;
            } else {
                c.store[c.fcvEnv[name]].rtype = lub(currentType, st);
            }
        } else if (!subtype(st, currentType)) {
            throw "Cannot assign value of type <prettyPrintType(st)> to assignable of type <prettyPrintType(currentType)>";
        }
        return < c, atree[@otype=c.store[c.fcvEnv[name]].rtype][@atype=c.store[c.fcvEnv[name]].rtype] >;
    }
}

@doc{Bind variable types to variables in assignables: Subscript}
public ATResult bindAssignable(AssignableTree atree:subscriptNode(AssignableTree receiver, Symbol stype), Symbol st, Configuration c) {
    
    if (isListType(receiver@atype)) { 
        < c, receiver > = bindAssignable(receiver, \list(lub(st,getListElementType(receiver@atype))), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=getListElementType(receiver@atype)] >;
    } else if (isNodeType(receiver@atype)) {
        < c, receiver > = bindAssignable(receiver, \node(), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\value()] >;
    } else if (isTupleType(receiver@atype)) {
        tupleFields = getTupleFields(receiver@atype);
        // This type is as exact as we can get. Assuming the subscript is
        // in range, all we can infer about the resulting type is that, since
        // we could assign to each field, each field could have a type based
        // on the lub of the existing field type and the subject type.
        < c, receiver > = bindAssignable(receiver, \tuple([lub(tupleFields[idx],st) | idx <- index(tupleFields)]), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\value()] >;
    } else if (isMapType(receiver@atype)) {
        < c, receiver > = bindAssignable(receiver, \map(getMapDomainType(receiver@atype), lub(st,getMapRangeType(receiver@atype))), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=getMapRangeType(receiver@atype)] >;
    } else if (isRelType(receiver@atype)) {
        relFields = getRelFields(receiver@atype);
        < c, receiver > = bindAssignable(receiver, \rel([relFields[0],lub(relFields[1],st)]), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=getRelFields(receiver@atype)[1]] >;
    } else {
    	throw "Cannot assign value of type <prettyPrintType(st)> to assignable of type <prettyPrintType(receiver@atype)>";
    }
}

public default ATResult bindAssignable(AssignableTree atree, Symbol st, Configuration c) {
	throw "Missing assignable!";
	return < c, atree >;
}

@doc{Bind variable types to variables in assignables: Slice}
public ATResult bindAssignable(AssignableTree atree:sliceNode(AssignableTree receiver, Symbol firstType, Symbol lastType), Symbol st, Configuration c) {    
    if (isListType(receiver@atype) && isListType(st)) {
        < c, receiver > = bindAssignable(receiver, lub(st,receiver@atype), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=receiver@atype] >;
    } else if (isNodeType(receiver@atype) && isListType(st)) {
        < c, receiver > = bindAssignable(receiver, \node(), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\node()] >;
    } else if (isStrType(receiver@atype) && isStrType(st)) {
        < c, receiver > = bindAssignable(receiver, \str(), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\str()] >;
    } else {
    	throw "Cannot assign value of type <prettyPrintType(st)> to assignable of type <prettyPrintType(receiver@atype)>";
    }
}

@doc{Bind variable types to variables in assignables: Slice Step}
public ATResult bindAssignable(AssignableTree atree:sliceStepNode(AssignableTree receiver, Symbol firstType, Symbol secondType, Symbol lastType), Symbol st, Configuration c) {    
    if (isListType(receiver@atype) && isListType(st)) {
        < c, receiver > = bindAssignable(receiver, lub(st,receiver@atype), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=receiver@atype] >;
    } else if (isNodeType(receiver@atype) && isListType(st)) {
        < c, receiver > = bindAssignable(receiver, \node(), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\node()] >;
    } else if (isStrType(receiver@atype) && isStrType(st)) {
        < c, receiver > = bindAssignable(receiver, \str(), c);
        return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=\str()] >;
    } else {
    	throw "Cannot assign value of type <prettyPrintType(st)> to assignable of type <prettyPrintType(receiver@atype)>";
    }
}

@doc{Bind variable types to variables in assignables: FieldAccess}
public ATResult bindAssignable(AssignableTree atree:fieldAccessNode(AssignableTree receiver, RName name), Symbol st, Configuration c) {
    // Note that, for field access, we have to know the receiver type, since
    // it holds the field information. However, unlike with subscripts, writing
    // to a field cannot change the type of the item holding the field -- all fields
    // have non-inferred types. So, we don't need to push back anything other than
    // the current receiver type. We do, however, need to make sure the value being
    // assigned is of the correct type.
    
    if (subtype(st, atree@atype)) {
        < c, receiver > = bindAssignable(receiver, receiver@atype, c);
        return < c, atree[receiver=receiver][@otype=receiver@otype] >;
    } else {
        throw "Bind error, cannot assign value of type <prettyPrintType(st)> to assignable expecting type <prettyPrintType(atree@atype)>";
    }
}

@doc{Bind variable types to variables in assignables: IfDefinedOrDefault}
public ATResult bindAssignable(AssignableTree atree:ifDefinedOrDefaultNode(AssignableTree receiver, Symbol dtype), Symbol st, Configuration c) {
    // For and If Defined or Default assignable, we just push the type through, much
    // like with a bracket. It will be checked by the receiver to ensure it is
    // correct, using the logic behind the proper receiver (subscript, etc).
    
    < c, receiver > = bindAssignable(receiver, st, c);
    return < c, atree[receiver=receiver][@otype=receiver@otype][@atype=receiver@atype] >;
}

@doc{Check the type of Rascal assignables: Constructor}
public ATResult bindAssignable(AssignableTree atree:constructorNode(RName name,list[AssignableTree] children), Symbol st, Configuration c) {
    throw "Not implemented";
}

@doc{Bind variable types to variables in assignables: Tuple}
public ATResult bindAssignable(AssignableTree atree:tupleNodeAT(list[AssignableTree] children), Symbol st, Configuration c) {
    // For tuple assignables, we make sure that the subject is also a tuple with the
    // same number of fields. We then push the tuple type of the subject through each
    // of the children of the tuple assignable, in order. The final type is then based
    // on the final types of the children.
    if (isTupleType(st)) {
        list[Symbol] tflds = getTupleFields(st);
        if (size(tflds) == size(children)) {
            list[AssignableTree] newChildren = [ ];
            for (idx <- index(children)) {
                < c, newTree > = bindAssignable(children[idx], tflds[idx], c);
                newChildren = newChildren + newTree;
            }
            return < c, atree[children = newChildren][@otype=\tuple([child@otype|child <- newChildren])][@atype=\tuple([child@atype|child <- newChildren])] >; 
        } else {
            throw "Cannot bind tuple assignable with arity <size(children)> to value of tuple type <prettyPrintType(st)> with arity <size(tflds)>";
        }
    } else {
        throw "Cannot bind tuple assignable to non-tuple type <prettyPrintType(st)>";
    }
}

@doc{Check the type of Rascal assignables: Annotation}
public ATResult bindAssignable(AssignableTree atree:annotationNode(AssignableTree receiver, RName name), Symbol st, Configuration c) {
    // Note that, for annotations, we have to know the receiver type, since the
    // annotation type is based on this. However, unlike with subscripts, writing
    // to an annotation cannot change the type of the annotated item. So, we don't 
    // need to push back anything other than the current receiver type. We do, 
    // however, need to make sure the value being assigned is of the correct type.
    
    if (subtype(st, atree@atype)) {
        < c, receiver > = bindAssignable(receiver, receiver@atype, c);
        return < c, atree[receiver=receiver][@otype=receiver@otype] >;
    } else {
        throw "Bind error, cannot assign value of type <prettyPrintType(st)> to assignable expecting type <prettyPrintType(atree@atype)>";
    }
}

@doc{Check the type of the components of a declaration: Variable}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> <Type t> <{Variable ","}+ vars>;`, bool descend, Configuration c) {
    < c, rt > = convertAndExpandType(t,c);

    for (v <- vars, v@\loc notin c.definitions<1>, v@\loc notin {l | error(_,l) <- c.messages}) {
        // If v@\loc is not in c.definitions and not in scope errors, we haven't processed
        // it yet, so process it now. We process the expression now too, even if not descending,
        // since we want to process the expressions in name order. 
        if ((Variable)`<Name n> = <Expression init>` := v || (Variable)`<Name n>` := v) {
            if ((Variable)`<Name n> = <Expression init>` := v) {
                < c, t1 > = checkExp(init, c);
                if (!isFailType(t1) && !subtype(t1,rt)) 
                    c = addScopeMessage(c, error("Initializer type <prettyPrintType(t1)> not assignable to variable of type <prettyPrintType(rt)>", v@\loc));                       
            }
                                
            RName rn = convertName(n);
            c = addVariable(c, rn, false, getVis(vis), v@\loc, rt);
        } 
    }
    
    return c;
}

@doc{Check the type of the components of a declaration: Annotation}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> anno <Type annoType> <Type onType> @ <Name n>;`, bool descend, Configuration c) {
    // NOTE: We ignore descend here. There is nothing that is done here that should be deferred until
    // later in declaration processing.
    
    if (decl@\loc notin c.definitions<1>) {
        // TODO: Check for conversion errors
        < c, at > = convertAndExpandType(annoType,c);
        < c, ot > = convertAndExpandType(onType,c);
        if(isFailType(at)) {
        	c.messages = c.messages + getFailures(at);
        }
        if(isFailType(ot)) {
        	c.messages = c.messages + getFailures(ot);
        }
        rn = convertName(n);
        c = addAnnotation(c,rn,at,ot,getVis(vis),decl@\loc);
    }
    return c;   
}

@doc{Check the type of the components of a declaration: Alias}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> alias <UserType ut> = <Type t>;`, bool descend, Configuration c) {
    // Add the alias, but only if it isn't already defined. If it is defined, the location
    // will be in definitions
    if (decl@\loc notin c.definitions<1>) { 
        // TODO: Check for convert errors
        < c, utype > = convertAndExpandUserType(ut,c);
        
        // Extract the name and parameters
        utypeName = getUserTypeName(utype);
        utypeParams = getUserTypeParameters(utype);
        
        // Add the alias into the type environment
        // TODO: Check to make sure this is possible
        c = addAlias(c,RSimpleName(utypeName),getVis(vis),decl@\loc,\alias(utypeName,utypeParams,\void()));
    }

    // If we can descend, process the aliased type as well, assigning it into
    // the alias.
    if (descend) {
        // If we descend, we also want to add the constructors; if not, we are just
        // adding the ADT into the type environment. We get the adt type out of
        // the store by looking up the definition from this location.
        aliasId = getOneFrom(invert(c.definitions)[decl@\loc]);
        aliasType = c.store[aliasId].rtype;
        // TODO: Check for convert errors
        < c, aliasedType > = convertAndExpandType(t,c);
        c.store[aliasId].rtype = \alias(aliasType.name, aliasType.parameters, aliasedType);
    }
    
    return c;
}

@doc{Check the type of the components of a declaration: Tag}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> tag <Kind k> <Name n> on <{Type ","}+ ts>;`, bool descend, Configuration c) {
    // TODO: Add descend code here; we should introduce the name, but not descend into the type.

    if (decl@\loc notin c.definitions<1>) {
        tk = convertKind(k);
        rn = convertName(n);
        set[Symbol] typeset = { };
        for (t <- ts) {
            < c, rt > = convertAndExpandType(t, c);
            typeset = typeset + rt;
        }
        // TODO: Make sure the add if safe first...
        c = addTag(c, tk, rn, typeset, getVis(vis), decl@\loc);
    }
    
    return c;
}

@doc{Check the type of the components of a declaration: DataAbstract}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> data <UserType ut>;`, bool descend, Configuration c) {
    // NOTE: We ignore descend here. There is nothing that is done here that should be deferred until
    // later in declaration processing.
    if (decl@\loc notin c.definitions<1>) {
        // TODO: Check for convert errors
        < c, utype > = convertAndExpandUserType(ut,c);
        
        // Extract the name and parameters
        utypeName = getUserTypeName(utype);
        utypeParams = getUserTypeParameters(utype);
        
        // Add the ADT into the type environment
        c = addADT(c,RSimpleName(utypeName),getVis(vis),decl@\loc,\adt(utypeName,utypeParams));
    }

	// TODO: We may need to descend here to properly handle parameters, although this may not
	// be necessary since ADTs other than Tree cannot be used as bounds    
    return c;
}

public tuple[Configuration, KeywordParamRel] calculateKeywordParamRel(Configuration c, list[KeywordFormal] kfl) {
	KeywordParamRel kprel = [ ];
	for (KeywordFormal kf: (KeywordFormal)`<Type kt> <Name kn> = <Expression ke>` <- kfl) {
		kfName = convertName(kn);
		< c, kfType > = convertAndExpandType(kt,c);
		< c, defType > = checkExp(ke, c);
		if (!subtype(defType, kfType))
			c = addScopeError(c, "The default for keyword parameter <prettyPrintName(kfName)> is of an invalid type", kf@\loc);
		kprel += < kfName, kfType, ke >;
	}
	return < c, kprel >;
}

@doc{Check the type of the components of a declaration: Data}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<Tags tags> <Visibility vis> data <UserType ut> <CommonKeywordParameters commonParams> = <{Variant "|"}+ vs>;`, bool descend, Configuration c) {
	// Add the ADT definition, but only if we haven't already added the definition
	// at this location. If we have, we can just use it if we need it.
	if (decl@\loc notin c.definitions<1>) { 
		// TODO: Check for convert errors
		< c, utype > = convertAndExpandUserType(ut,c);
		if (\user(_,_) !:= utype) throw "Conversion error: type for user type <ut> should be user type, not <prettyPrintType(utype)>";

		// Extract the name and parameters
		utypeName = getUserTypeName(utype);
		
		// TODO: We may need to descend instead to properly handle parameters, although this may not
		// be necessary since ADTs other than Tree cannot be used as bounds    
		utypeParams = getUserTypeParameters(utype);

		// Add the ADT into the type environment
		c = addADT(c,RSimpleName(utypeName),getVis(vis),decl@\loc,\adt(utypeName,utypeParams));
	}

	// If we descend, we also want to add the constructors; if not, we are just adding the ADT into 
	// the type environment. We get the adt type out of the store by looking up the definition from
	// this location. Check to make sure it is there -- if there was an error adding the ADT, there
	// may not be a datatype definition at this location.
	if (descend && size(invert(c.definitions)[decl@\loc]) > 0 && c.store[getOneFrom(invert(c.definitions)[decl@\loc])] is datatype) {
		adtId = getOneFrom(invert(c.definitions)[decl@\loc]);
		adtType = c.store[adtId].rtype;

		// Get back information on the common keyword parameters
		commonParamList = [ ];
		if ((CommonKeywordParameters)`( <{KeywordFormal ","}+ kfs> )` := commonParams) commonParamList = [ kfi | kfi <- kfs ];
						
		// Now add all the constructors
		// TODO: Check here for overlap problems
		for (Variant vr:(Variant)`<Name vn> ( < {TypeArg ","}* vargs > <KeywordFormals keywordArgs>)` <- vs) {
			// TODO: Check for convert errors
			list[Symbol] targs = [ ];
			for (varg <- vargs) { < c, vargT > = convertAndExpandTypeArg(varg, c); targs = targs + vargT; } 
			cn = convertName(vn);
			kfl = [ ];
			if ((KeywordFormals)`<OptionalComma _> <{KeywordFormal ","}+ keywordFormalList>` := keywordArgs)
				kfl = [ ka | ka <- keywordFormalList ];
			< c, ckfrel > = calculateKeywordParamRel(c, commonParamList);
			< c, kfrel > = calculateKeywordParamRel(c, kfl);
			c = addConstructor(c, cn, vr@\loc, Symbol::\cons(adtType,getSimpleName(cn),targs), ckfrel, kfrel);       
		}
	}

	return c;
}

@doc{Check the type of the components of a declaration: Function}
public Configuration checkDeclaration(Declaration decl:(Declaration)`<FunctionDeclaration fd>`, bool descend, Configuration c) {
    return checkFunctionDeclaration(fd,descend,c);
}

@doc{Prepare the name environment for checking the function signature.}
private Configuration prepareSignatureEnv(Configuration c) {
    // Strip other functions and variables out of the environment. We do 
    // this so we have an appropriate environment for typing the patterns 
    // in the function signature. Names used in these patterns cannot be 
    // existing variables and/or functions that are live in the current 
    // environment. Also, this way we can just get the type and drop all 
    // the changes that would be made to the environment.
    return c[fcvEnv = ( ename : c.fcvEnv[ename] | ename <- c.fcvEnv<0>, constructor(_,_,_,_,_) := c.store[c.fcvEnv[ename]] || (overload(ids,_) := c.store[c.fcvEnv[ename]] && size({consid | consid <- ids, constructor(_,_,_,_,_) := c.store[consid]})>0) )];
}

@doc{Prepare the various environments for checking the function body.}
private Configuration prepareFunctionBodyEnv(Configuration c) {
    // At this point, all we have to really do is make sure the labels
    // are cleared out. We should not be able to break from inside a function
    // out to a loop that surrounds it, for instance.
    return c[labelEnv = ( )][labelStack = [ ]];
}


@doc{Check function declarations: Abstract}
public Configuration checkFunctionDeclaration(FunctionDeclaration fd:(FunctionDeclaration)`<Tags tags> <Visibility vis> <Signature sig>;`, bool descend, Configuration c) {
    // TODO: Enforce that this is a java function?
    rn = getFunctionName(sig);
    throwsTypes = [ ];
    for ( ttype <- getFunctionThrows(sig)) { 
        < c, ttypeC > = convertAndExpandThrowType(ttype, c); 
        if(isFailType(ttypeC)) {
        	c.messages = c.messages + getFailures(ttypeC);
        }
        throwsTypes += ttypeC; 
    }
    
    println("Checking function <prettyPrintName(rn)>");

    // First, check to see if we have processed this declaration before. If we have, just get back the
    // id for the function, we don't want to create a new entry for it.
    if (fd@\loc notin c.definitions<1>) { 
    	set[Modifier] modifiers = getModifiers(sig);
        cFun = prepareSignatureEnv(c);
            
        // Put the function in, so we can enter the correct scope. This also puts the function name into the
        // scope -- we don't want to inadvertently use the function name as the name of a pattern variable,
        // and this makes sure we find it when checking the patterns in the signature.
        cFun = addFunction(cFun, rn, Symbol::\func(\void(),[]), ( ), modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
        < cFun, tFun > = processSignature(sig, cFun);
        if (isFailType(tFun)) c.messages = c.messages + getFailures(tFun);

		// Check the keyword formals. This will compute the types, check for redeclarations of the param
		// names, and also make sure the default is the correct type.        
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
		for (kpt <- keywordParams<1>, isFailType(kpt)) c.messages = c.messages + getFailures(kpt);
		  
        // We now have the function type. So, we can throw cFun away, and add this as a proper function
        // into the scope. NOTE: This can be a failure type.
        c = addFunction(c, rn, tFun, keywordParams, modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
    }
    //else {
    funId = getOneFrom(invert(c.definitions)[fd@\loc]);
    c.stack = funId + c.stack;
    //}   
    
    // Normally we would now descend into the body. Here we don't have one.
    // However, we still process the signature, e.g., to add the formal parameters to the store,
    // So that this static information could be still used by a compiler.
    if(descend) {
        funId = head(c.stack);
        funType = c.store[funId].rtype;
        < cFun, tFun > = processSignature(sig, c);
        // Checking the keyword formals here adds the names into the store and also adds
        // entries mapping each name to its default
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
        c = recoverEnvironmentsAfterCall(cFun, c);
    }
    
    c.stack = tail(c.stack);
    return c;
}

@doc{Check function declarations: Expression}
public Configuration checkFunctionDeclaration(FunctionDeclaration fd:(FunctionDeclaration)`<Tags tags> <Visibility vis> <Signature sig> = <Expression exp>;`, bool descend, Configuration c) {
    rn = getFunctionName(sig);
    throwsTypes = [ ];
    for ( ttype <- getFunctionThrows(sig)) { 
        < c, ttypeC > = convertAndExpandThrowType(ttype, c); 
        if(isFailType(ttypeC)) {
        	c.messages = c.messages + getFailures(ttypeC);
        }
        throwsTypes += ttypeC; 
    }

    println("Checking function <prettyPrintName(rn)>");

    if (fd@\loc notin c.definitions<1>) { 
    	set[Modifier] modifiers = getModifiers(sig);
        cFun = prepareSignatureEnv(c);
        cFun = addFunction(cFun, rn, Symbol::\func(\void(),[]), ( ), modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
        < cFun, tFun > = processSignature(sig, cFun);
        if (isFailType(tFun)) c.messages = c.messages + getFailures(tFun);

		// Check the keyword formals. This will compute the types, check for redeclarations of the param
		// names, and also make sure the default is the correct type.        
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
		for (kpt <- keywordParams<1>, isFailType(kpt)) c.messages = c.messages + getFailures(kpt);

        c = addFunction(c, rn, tFun, keywordParams, modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
    }
    //else {
    funId = getOneFrom(invert(c.definitions)[fd@\loc]);
    c.stack = funId + c.stack;
    //}   
    
    if (descend) {
        // Process the signature, but this time in a copy of the current environment
        // without the names stripped out (since these names will be visible in the
        // body of the function).
        funId = head(c.stack);
        funType = c.store[funId].rtype;
        cFun = prepareFunctionBodyEnv(c);
        if (!isFailType(funType)) {
            cFun = setExpectedReturn(c, getFunctionReturnType(funType));
        } else {
            // If we couldn't calculate the function type, use value here so we don't also
            // get errors on the return type not matching.
            cFun = setExpectedReturn(c, \value());
        }
        < cFun, tFun > = processSignature(sig, cFun);
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
        cFun = addLabel(cFun,rn,fd@\loc,functionLabel());
        cFun.labelStack = labelStackItem(rn, functionLabel(), \void()) + cFun.labelStack;
        < cFun, tExp > = checkExp(exp, cFun);
        cFun.labelStack = tail(cFun.labelStack);
        if (!isFailType(tExp) && !subtype(tExp, cFun.expectedReturnType))
            cFun = addScopeMessage(cFun,error("Unexpected type: type of body expression, <prettyPrintType(tExp)>, must be a subtype of the function return type, <prettyPrintType(cFun.expectedReturnType)>", exp@\loc));
        c = recoverEnvironmentsAfterCall(cFun, c);
    }

    c.stack = tail(c.stack);
    return c;
}

@doc{Check function declarations: Conditional}
public Configuration checkFunctionDeclaration(FunctionDeclaration fd:(FunctionDeclaration)`<Tags tags> <Visibility vis> <Signature sig> = <Expression exp> when <{Expression ","}+ conds>;`, bool descend, Configuration c) {
    rn = getFunctionName(sig);
    throwsTypes = [ ];
    for ( ttype <- getFunctionThrows(sig)) { 
        < c, ttypeC > = convertAndExpandThrowType(ttype, c); 
        if(isFailType(ttypeC)) {
        	c.messages = c.messages + getFailures(ttypeC);
        }
        throwsTypes += ttypeC; 
    }

    println("Checking function <prettyPrintName(rn)>");

    if (fd@\loc notin c.definitions<1>) {
    	set[Modifier] modifiers = getModifiers(sig); 
        cFun = prepareSignatureEnv(c);
        cFun = addFunction(cFun, rn, Symbol::\func(\void(),[]), ( ), modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
        < cFun, tFun > = processSignature(sig, cFun);
        if (isFailType(tFun)) c.messages = c.messages + getFailures(tFun);

		// Check the keyword formals. This will compute the types, check for redeclarations of the param
		// names, and also make sure the default is the correct type.        
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
		for (kpt <- keywordParams<1>, isFailType(kpt)) c.messages = c.messages + getFailures(kpt);

        c = addFunction(c, rn, tFun, keywordParams, modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
    }
    //else {
    funId = getOneFrom(invert(c.definitions)[fd@\loc]);
    c.stack = funId + c.stack;
    //}   
    
    if (descend) {
        // Process the signature, but this time in a copy of the current environment
        // without the names stripped out (since these names will be visible in the
        // body of the function).
        funId = head(c.stack);
        funType = c.store[funId].rtype;
        cFun = prepareFunctionBodyEnv(c);
        if (!isFailType(funType)) {
            cFun = setExpectedReturn(c, getFunctionReturnType(funType));
        } else {
            cFun = setExpectedReturn(c, \void());
        }
        < cFun, tFun > = processSignature(sig, cFun);
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
	
		// Any variables bound in the when clause should be visible inside the function body,
		// so we enter a new boolean scope to make sure the bindings are handled properly.
		condList = [ cond | cond <- conds ];
		cWhen = enterBooleanScope(cFun, condList[0]@\loc);    
        for (cond <- conds) {
            < cWhen, tCond > = checkExp(cond, cWhen);
            if (!isFailType(tCond) && !isBoolType(tCond))
                cWhen = addScopeMessage(cWhen,error("Unexpected type: condition should be of type bool, not type <prettyPrintType(tCond)>", cond@\loc));
        }
        
        cWhen = addLabel(cWhen,rn,fd@\loc,functionLabel());
        cWhen.labelStack = labelStackItem(rn, functionLabel(), \void()) + cWhen.labelStack;
        < cWhen, tExp > = checkExp(exp, cWhen);
        cWhen.labelStack = tail(cWhen.labelStack);

        if (!isFailType(tExp) && !subtype(tExp, cWhen.expectedReturnType))
            cWhen = addScopeMessage(cWhen,error("Unexpected type: type of body expression, <prettyPrintType(tExp)>, must be a subtype of the function return type, <prettyPrintType(cFun.expectedReturnType)>", exp@\loc));
            
        cFun = exitBooleanScope(cWhen, cFun);
        c = recoverEnvironmentsAfterCall(cFun, c);
    }

    c.stack = tail(c.stack);
    return c;
}

@doc{Check function declarations: Default}
public Configuration checkFunctionDeclaration(FunctionDeclaration fd:(FunctionDeclaration)`<Tags tags> <Visibility vis> <Signature sig> <FunctionBody body>`, bool descend, Configuration c) {
    rn = getFunctionName(sig);
    throwsTypes = [ ];
    for ( ttype <- getFunctionThrows(sig)) { 
        < c, ttypeC > = convertAndExpandThrowType(ttype, c); 
        if(isFailType(ttypeC)) {
        	c.messages = c.messages + getFailures(ttypeC);
        }
        throwsTypes += ttypeC; 
    }

    println("Checking function <prettyPrintName(rn)>");
    
    if (fd@\loc notin c.definitions<1>) { 
    	set[Modifier] modifiers = getModifiers(sig);
        cFun = prepareSignatureEnv(c);
        cFun = addFunction(cFun, rn, Symbol::\func(\void(),[]), ( ), modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
        < cFun, tFun > = processSignature(sig, cFun);
        if (isFailType(tFun)) c.messages = c.messages + getFailures(tFun);

		// Check the keyword formals. This will compute the types, check for redeclarations of the param
		// names, and also make sure the default is the correct type.        
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
		for (kpt <- keywordParams<1>, isFailType(kpt)) c.messages = c.messages + getFailures(kpt);

        c = addFunction(c, rn, tFun, keywordParams, modifiers, isVarArgs(sig), getVis(vis), throwsTypes, fd@\loc);
    }
    //else {
    funId = getOneFrom(invert(c.definitions)[fd@\loc]);
    c.stack = funId + c.stack;
    //}   
    
    if (descend) {
        // Process the signature, but this time in a copy of the current environment
        // without the names stripped out (since these names will be visible in the
        // body of the function).
        funId = head(c.stack);
        funType = c.store[funId].rtype;
        cFun = prepareFunctionBodyEnv(c);
        if (!isFailType(funType)) {
            cFun = setExpectedReturn(c, getFunctionReturnType(funType));
        } else {
            cFun = setExpectedReturn(c, \void());
        }
        < cFun, tFun > = processSignature(sig, cFun);
		< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);        
        cFun = addLabel(cFun,rn,fd@\loc,functionLabel());
        cFun.labelStack = labelStackItem(rn, functionLabel(), \void()) + cFun.labelStack;

        if ((FunctionBody)`{ <Statement* ss> }` := body) {
			< cFun, tStmt > = checkStatementSequence([ssi | ssi <- ss], cFun);
        }

        cFun.labelStack = tail(cFun.labelStack);
        c = recoverEnvironmentsAfterCall(cFun, c);
    }

    c.stack = tail(c.stack);
    return c;
}

@doc{Process function signatures: WithThrows}
public CheckResult processSignature(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps> throws <{Type ","}+ exs>`, Configuration c) {
    // TODO: Do something with the exception information
    < c, rType > = convertAndExpandType(t,c);
    < c, ptTuple > = checkParameters(ps, c);
    list[Symbol] parameterTypes = getTupleFields(ptTuple);
    paramFailures = { pt | pt <- parameterTypes, isFailType(pt) };
    funType = \void();
    if (size(paramFailures) > 0) {
        funType = collapseFailTypes(paramFailures + makeFailType("Could not calculate function type because of errors calculating the parameter types", sig@\loc));     
    } else {
        funType = makeFunctionTypeFromTuple(rType, isVarArgs(sig), \tuple(parameterTypes));
    }

    return < c, funType >;
}

@doc{Process function signatures: NoThrows}
public CheckResult processSignature(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps>`, Configuration c) {
    < c, rType > = convertAndExpandType(t,c);
    < c, ptTuple > = checkParameters(ps, c);
    list[Symbol] parameterTypes = getTupleFields(ptTuple);
    paramFailures = { pt | pt <- parameterTypes, isFailType(pt) };
    funType = \void();
    if (size(paramFailures) > 0) {
        funType = collapseFailTypes(paramFailures + makeFailType("Could not calculate function type because of errors calculating the parameter types", sig@\loc));     
    } else {
        funType = makeFunctionTypeFromTuple(rType, isVarArgs(sig), \tuple(parameterTypes));
    }

    return < c, funType >;
}

@doc{Extract the function modifiers from the signature.}
public set[Modifier] getModifiers(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps> throws <{Type ","}+ exs>`) = getModifiers(mds);
public set[Modifier] getModifiers(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps>`) = getModifiers(mds);

@doc{Extract the function modifiers from the list of modifiers.}
set[Modifier] getModifiers(FunctionModifiers fmods:(FunctionModifiers)`<FunctionModifier* fms>`) {
    return { getModifier(m) | m <- fms };
}

@doc{Check if a set of function modifiers has the default modifier}
public bool hasDefaultModifier(set[Modifier] modifiers) = defaultModifier() in modifiers;

@doc{Extract the function name from the signature.}
public RName getFunctionName(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps> throws <{Type ","}+ exs>`) = convertName(n);
public RName getFunctionName(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps>`) = convertName(n);

@doc{Extract the throws information from the signature.}
public list[Type] getFunctionThrows(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps> throws <{Type ","}+ exs>`) = [ exsi | exsi <- exs ];
public list[Type] getFunctionThrows(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> <Parameters ps>`) = [ ];

@doc{Check to see if the function is a varargs function.}
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> ) throws <{Type ","}+ exs>`) = false;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> <KeywordFormals kfmls>) throws <{Type ","}+ exs>`) = false;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> ... ) throws <{Type ","}+ exs>`) = true;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> ... <KeywordFormals kfmls>) throws <{Type ","}+ exs>`) = true;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> )`) = false;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> <KeywordFormals kfmls>)`) = false;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> ... )`) = true;
public bool isVarArgs(Signature sig:(Signature)`<FunctionModifiers mds> <Type t> <Name n> ( <Formals fmls> ... <KeywordFormals kfmls>)`) = true;

//
// TODO: The code for importing signature items duplicates a fair amount of the declaration
// code. Refactor this to prevent duplication.
//
@doc{Import a signature item: Alias}
public Configuration importAlias(RName aliasName, UserType aliasType, Type aliasedType, loc at, Vis vis, bool descend, Configuration c) {
    // If we are not descending, we just record the name in the type environment. If
    // we are descending, we also process the type parameters and the aliased type.
    if (!descend) {
        c = addAlias(c,aliasName,vis,at,\alias(prettyPrintName(aliasName),[],\void()));
    } else {
        aliasId = getOneFrom(invert(c.definitions)[at]);
        < c, utype > = convertAndExpandUserType(aliasType, c);
        utypeParams = getUserTypeParameters(utype);
        < c, rt > = convertAndExpandType(aliasedType,c);
        c.store[aliasId].rtype = \alias(prettyPrintName(aliasName), utypeParams, rt);
    }
    return c;
}

@doc{Import a signature item: Function}
public Configuration importFunction(RName functionName, Signature sig, loc at, Vis vis, Configuration c) {
    // This looks confusing, but is actually fairly simple. We build a new configuration with a modified
    // name environment that only includes constructors. This is done because the function parameters can
    // refer to existing constructors, but not to existing functions or variables. We then add the function
    // into the environment and process the function signature, calculating the type. Finally, we add the
    // function using the original environment, ensuring we don't actually lose the name info and/or add
    // parameter names into scope.
    // NOTE: There is no descend option here. Instead we process imports of function and variable names
    // from top to bottom, and don't descend into them at any point.
    throwsTypes = [ ];
    for ( ttype <- getFunctionThrows(sig)) { 
        < c, ttypeC > = convertAndExpandThrowType(ttype, c); 
        if(isFailType(ttypeC)) {
        	c.messages = c.messages + getFailures(ttypeC);
        }
        throwsTypes += ttypeC; 
    }
    set[Modifier] modifiers = getModifiers(sig);
    cFun = c[fcvEnv = ( ename : c.fcvEnv[ename] | ename <- c.fcvEnv<0>, constructor(_,_,_,_,_) := c.store[c.fcvEnv[ename]]
    																	|| production(_,_,_,_) := c.store[c.fcvEnv[ename]]
    																	// constructor names may be overloaded 
    																	|| overload(_,_) := c.store[c.fcvEnv[ename]] )];
    cFun = addFunction(cFun, functionName, Symbol::\func(\void(),[]), ( ), modifiers, isVarArgs(sig), vis, throwsTypes, at);
    < cFun, tFun > = processSignature(sig, cFun);
    if(isFailType(tFun)) c.messages = c.messages + getFailures(tFun);
	< cFun, keywordParams > = checkKeywordFormals(getKeywordFormals(getFunctionParameters(sig)), cFun);
	for (kpt <- keywordParams<1>, isFailType(kpt)) c.messages = c.messages + getFailures(kpt);
    c = addFunction(c, functionName, tFun, keywordParams, modifiers, isVarArgs(sig), vis, throwsTypes, at);
    return c;
}

@doc{Import a signature item: Variable}
public Configuration importVariable(RName variableName, Type variableType, loc at, Vis vis, Configuration c) {
    < c, rt > = convertAndExpandType(variableType,c);
    return addVariable(c, variableName, false, vis, at, rt);                        
}

@doc{Import a signature item: ADT}
public Configuration importADT(RName adtName, UserType adtType, loc at, Vis vis, bool descend, Configuration c) {
    // If we are not descending, we just record the name in the type environment. If
    // we are descending, we also process the type and keyword parameters.
    if (!descend) {
        c = addADT(c,adtName,vis,at,\adt(prettyPrintName(adtName),[]));
    } else if (size(invert(c.definitions)[at]) > 0 && c.store[getOneFrom(invert(c.definitions)[at])] is datatype) {
        adtId = getOneFrom(invert(c.definitions)[at]);
        < c, utype > = convertAndExpandUserType(adtType,c);
        utypeParams = getUserTypeParameters(utype);
        c.store[adtId].rtype = \adt(prettyPrintName(adtName),utypeParams);
    }
    return c;
}

public Configuration importNonterminal(RName sort, Symbol sym, loc at, Configuration c) {
  c = addNonterminal(c, sort, at, sym); // TODO: something with descend?
  //id = getOneFrom(invert(c.definitions)[at]); // TODO: ??
  //c.store[id].rtype = sym;
  return c;
}

@doc{Import a signature item: Constructor}
public Configuration importConstructor(RName conName, UserType adtType, list[TypeArg] argTypes, list[KeywordFormal] commonParams, list[KeywordFormal] keywordParams, loc adtAt, loc at, Vis vis, Configuration c) {
    // NOTE: We do not have a separate descend stage. Instead, we just add these after the types (aliases
    // and ADTs) have already been added. These are added before functions, though, since they may be
    // used in the function parameters.
    rt = c.store[getOneFrom(invert(c.definitions)[adtAt])].rtype;
    list[Symbol] targs = [ ];
    for (varg <- argTypes) { < c, vargT > = convertAndExpandTypeArg(varg, c); targs = targs + vargT; } 
	< c, ckfrel > = calculateKeywordParamRel(c, commonParams);	    
	< c, kfrel > = calculateKeywordParamRel(c, keywordParams);	    
    return addConstructor(c, conName, at, Symbol::\cons(rt,getSimpleName(conName),targs), ckfrel, kfrel);         
}

@doc{Import a signature item: Production}
public Configuration importProduction(RSignatureItem item, Configuration c) {
	// Signature item contains a syntax definition production
	Production prod = item.prod;
	if( (prod.def is label && prod.def.symbol has name) 
			|| (!(prod.def is label) && prod.def has name) 
			|| (prod.def is \start && prod.def.symbol has name)) {
		str sortName = (prod.def is \start || prod.def is label) ? prod.def.symbol.name : prod.def.name;
		c = addSyntaxDefinition(c, RSimpleName(sortName), item.at, prod, prod.def is \start);
	}
	// Productions that end up in the store
	for(/Production p:prod(_,_,_) := prod) {
		if(label(str l, Symbol _) := prod.def) {
    		c = addProduction(c, RSimpleName(l), item.at, p);
    	} else {
    		c = addProduction(c, RSimpleName(""), item.at, p);
    	} 
    }
    return c;
}

@doc{Import a signature item: Annotation}
public Configuration importAnnotation(RName annName, Type annType, Type onType, loc at, Vis vis, Configuration c) {
    // NOTE: We also do not descend here. We just process these once, after all the types have been added.
    < c, atype > = convertAndExpandType(annType,c);
    < c, otype > = convertAndExpandType(onType,c);
    if(isFailType(atype)) {
        	c.messages = c.messages + getFailures(atype);
        }
    if(isFailType(otype)) {
        c.messages = c.messages + getFailures(otype);
    }
    return addAnnotation(c,annName,atype,otype,vis,at);
}

@doc{Import a signature item: Tag}
public Configuration importTag(RName tagName, TagKind tagKind, list[Symbol] taggedTypes, loc at, Vis vis, bool descend, Configuration c) {
    // If we are not descending, we just add the tag name into the environment. If we
    // are descending, we process all the types in the tag (the "tagged types") as well.
    if (!descend) {
        c = addTag(c, tagKind, tagName, { }, vis, at);
    } else {
        tagId = getOneFrom(invert(c.definitions)[at]);
        set[Symbol] typeset = { };
        for (t <- taggedTypes) {
            < c, rt > = convertAndExpandType(t, c);
            typeset = typeset + rt;
        }
        c.store[tagId].onTypes = typeset;
    }
    return c;
}

@doc{Check a given module, including loading the imports and extends items for the module.}
public Configuration checkModule(Module md:(Module)`<Header header> <Body body>`, Configuration c) {
    moduleName = getHeaderName(header);
    importList = getHeaderImports(header);
    map[RName,RSignature] sigMap = ( );
    map[RName,bool] isExtends = ( );
    map[RName,int] moduleIds = ( );
    map[RName,loc] moduleLocs = ( );
    lrel[RName,bool] defaultImports = [ ]; // [ < RSimpleName("Exception"), false > ];
    list[RName] importOrder = [ ];
    
    c = addModule(c, moduleName, md@\loc);
    currentModuleId = head(c.stack);
            
    for (< modName, defaultExtends > <- defaultImports) {
        try {
            dt1 = now();
            modTree = getModuleParseTree(prettyPrintName(modName));
            sigMap[modName] = getModuleSignature(modTree);
            moduleLocs[modName] = modTree@\loc;
            importOrder = importOrder + modName;
            c = addModule(c,modName,modTree@\loc);
            moduleIds[modName] = head(c.stack);
            c = popModule(c);
            isExtends[modName] = defaultExtends;
            c = pushTiming(c, "Generate signature for <prettyPrintName(modName)>", dt1, now());
        } catch perror : {
            c = addScopeError(c, "Cannot calculate signature for default module <prettyPrintName(modName)>", md@\loc);
        }
    }

    // Get the information about each import, including the module signature
    for (importItem <- importList) {
        if ((Import)`import <ImportedModule im>;` := importItem || (Import)`extend <ImportedModule im>;` := importItem) {
            try {
                dt1 = now();
                modName = getNameOfImportedModule(im);
                modTree = getModuleParseTree(prettyPrintName(modName));
                sigMap[modName] = getModuleSignature(modTree);
                moduleLocs[modName] = modTree@\loc;
                importOrder = importOrder + modName;
                c = addModule(c,modName,modTree@\loc);
                moduleIds[modName] = head(c.stack);
                c = popModule(c);
                isExtends[modName] = (Import)`extend <ImportedModule im>;` := importItem;
                c = pushTiming(c, "Generate signature for <prettyPrintName(modName)>", dt1, now());
            } catch perror : {
                c = addScopeError(c, "Cannot calculate signature for imported module", importItem@\loc);
            }
        } 
    }
    
    
    
    // Add all the aliases and ADTs from each module without descending. Do tags here to, although
    // (when they are really used) we need to add them in a reasonable order. Right now we just
    // ignore them. So, TODO: Handle tags appropriately.
    dt1 = now();
    for (modName <- importOrder) {
        sig = sigMap[modName];
        c.stack = ( isExtends[modName] ? currentModuleId : moduleIds[modName] ) + c.stack;
        
        for (item <- sig.datatypes) 
          c = importADT(item.adtName, item.adtType, item.at, publicVis(), false, c);
        for (item <- sig.aliases) 
          c = importAlias(item.aliasName, item.aliasType, item.aliasedType, item.at, publicVis(), false, c);
        for (item <- sig.tags) 
          c = importTag(item.tagName, item.tagKind, item.taggedTypes, item.at, publicVis(), false, c);
        for (item <- sig.lexicalNonterminals + sig.contextfreeNonterminals + sig.layoutNonterminals + sig.keywordNonterminals)
          c = importNonterminal(item.sortName, item.sort, item.at, c);
          
        c.stack = tail(c.stack);
    }

    // Now, descend into each alias and ADT, ensuring all parameters are correctly added and the
    // aliased type is handled correctly. As above, we do tags here as well.
    for (modName <- importOrder) {
        sig = sigMap[modName];
        c.stack = ( isExtends[modName] ? currentModuleId : moduleIds[modName] ) + c.stack;
        for (item <- sig.datatypes) c = importADT(item.adtName, item.adtType, item.at, publicVis(), true, c);
        bool modified = true;
        definitions = invert(c.definitions);
        while(modified) {
            modified = false;
            for(item <- sig.aliases) {
                int aliasId = getOneFrom(definitions[item.at]);
                Symbol t = c.store[aliasId].rtype;
                c = importAlias(item.aliasName, item.aliasType, item.aliasedType, item.at, publicVis(), true, c);
                if(t != c.store[aliasId].rtype) {
                    modified = true;
                }
            }
        }
        for (item <- sig.tags) c = importTag(item.tagName, item.tagKind, item.taggedTypes, item.at, publicVis(), true, c);
        c.stack = tail(c.stack);
    }

    // Add constructors next, ensuring they are visible for the imported functions.
    // NOTE: This is one area where we could have problems. Once the checker is working
    // correctly, TODO: calculate the types in the signature, so we don't risk clashes
    // over constructor names (or inadvertent visibility of constructor names) that would
    // not have been an issue before, when we did not have parameters with patterns.
    for (modName <- importOrder) {
        sig = sigMap[modName];
        c.stack = ( isExtends[modName] ? currentModuleId : moduleIds[modName] ) + c.stack;
        for (item <- sig.publicConstructors) 
          c = importConstructor(item.conName, item.adtType, item.argTypes, item.commonParams, item.keywordParams, item.adtAt, item.at, publicVis(), c);
        for (item <- sig.publicProductions) {
          // Firts, resolve names in the productions
          <p,c> = resolveProduction(item.prod, item.at, c, true);
          item.prod = p;
          c = importProduction(item, c);
        }
        c.stack = tail(c.stack);
    }
    
    // Now, bring in all public names, including annotations, public vars, and public functions.
    for (modName <- importOrder) {
        sig = sigMap[modName];
        c.stack = ( isExtends[modName] ? currentModuleId : moduleIds[modName] ) + c.stack;
        for (item <- sig.publicVariables) c = importVariable(item.variableName, item.variableType, item.at, publicVis(), c);
        for (item <- sig.publicFunctions) c = importFunction(item.functionName, item.sig, item.at, publicVis(), c);
        for (item <- sig.annotations) c = importAnnotation(item.annName, item.annType, item.onType, item.at, publicVis(), c);
        c.stack = tail(c.stack);
    }
    
    // Now, bring in the private names, but only for modules that are imported using extends
    for (modName <- importOrder, isExtends[modName]) {
        sig = sigMap[modName];
        c.stack = currentModuleId + c.stack;
        for (item <- sig.privateVariables) c = importVariable(item.variableName, item.variableType, item.at, privateVis(), c);
        for (item <- sig.privateFunctions) c = importFunction(item.functionName, item.sig, item.at, privateVis(), c);
        c.stack = tail(c.stack);
    }
    c = pushTiming(c, "Imported module signatures", dt1, now());
            
    // Process the current module
    syntaxConfig = processSyntax(moduleName, importList);
    for (item <- syntaxConfig.lexicalNonterminals + syntaxConfig.contextfreeNonterminals + syntaxConfig.layoutNonterminals + syntaxConfig.keywordNonterminals)
      c = importNonterminal(item.sortName, item.sort, item.at, c);
    for (prodItem <- syntaxConfig.publicProductions) {
      // First, resolve names in the productions
      <p,c> = resolveProduction(prodItem.prod, prodItem.at, c, false);
      prodItem.prod = p;
      c = importProduction(prodItem, c);
    }
    
    c = checkSyntax(importList, c);  
  
    if ((Body)`<Toplevel* tls>` := body) {
        dt1 = now();
        list[Declaration] typesAndTags = [ ];
        list[Declaration] aliases = [ ];
        list[Declaration] annotations = [ ];
        list[Declaration] names = [ ];
        
        c.stack = currentModuleId + c.stack;
        
        for ((Toplevel)`<Declaration decl>` <- tls) {
            switch(decl) {
                case (Declaration)`<Tags _> <Visibility _> <Type _> <{Variable ","}+ _> ;` : names = names + decl;
                case (Declaration)`<Tags _> <Visibility _> anno <Type _> <Type _> @ <Name _>;` : annotations = annotations + decl;
                case (Declaration)`<Tags _> <Visibility _> alias <UserType _> = <Type _> ;` : aliases = aliases + decl;
                case (Declaration)`<Tags _> <Visibility _> tag <Kind _> <Name _> on <{Type ","}+ _> ;` : typesAndTags = typesAndTags + decl;
                case (Declaration)`<Tags _> <Visibility _> data <UserType _> ;` : typesAndTags = typesAndTags + decl;
                case (Declaration)`<Tags _> <Visibility _> data <UserType _> <CommonKeywordParameters commonKeywordParameters> = <{Variant "|"}+ _> ;` : typesAndTags = typesAndTags + decl;
                case (Declaration)`<FunctionDeclaration _>` : names = names + decl;
            }
        }

        // Introduce the type names into the environment
        for (t <- typesAndTags) c = checkDeclaration(t,false,c);
        for (t <- aliases) c = checkDeclaration(t,false,c);
        
        // Now, actually process the aliases
        bool modified = true;
        definitions = invert(c.definitions);
        while(modified) {
        	    modified = false;
            for(t <- aliases) {
                int aliasId = getOneFrom(definitions[t@\loc]);
                Symbol aliasedType = c.store[aliasId].rtype;
                c = checkDeclaration(t,true,c);
                if(aliasedType != c.store[aliasId].rtype) {
                    modified = true;
                }
            }
        }
        
        // Now, actually process the type names
        for (t <- typesAndTags) c = checkDeclaration(t,true,c);
        
        // Next, process the annotations
        for (t <- annotations) c = checkDeclaration(t,true,c);
        
        // Next, introduce names into the environment
        for (t <- names) c = checkDeclaration(t,false,c);
        
        // Finally, process the names
        for (t <- names) c = checkDeclaration(t,true,c);
        
        c.stack = tail(c.stack);
        
        c = pushTiming(c, "Checked current module", dt1, now());
    }

    // TODO: We currently leave the environment "dirty" by not removing the items
    // added in this scope. If we ever want to call this as part of a multi-module
    // checker we need to do so.    
    return c;
}

public Configuration checkSyntax(list[Import] defs, Configuration c) {
  for ((Import) `<SyntaxDefinition syn>` <- defs, /Nonterminal t := syn.production, t notin getParameters(syn.defined)) {
    <c,rt> = resolveSorts(sort("<t>"), t@\loc, c);
  }
  
  return c;
}

list[Nonterminal] getParameters((Sym) `<Nonterminal _>[<{Sym ","}+ params>]`) = [ t | (Sym) `&<Nonterminal t>` <- params];
default list[Nonterminal] getParameters(Sym _) = []; 

@doc{Get the module name from the header.}
public RName getHeaderName((Header)`<Tags tags> module <QualifiedName qn> <ModuleParameters mps> <Import* imports>`) = convertName(qn);
public RName getHeaderName((Header)`<Tags tags> module <QualifiedName qn> <Import* imports>`) = convertName(qn);

@doc{Get the list of imports from the header.}
public list[Import] getHeaderImports((Header)`<Tags tags> module <QualifiedName qn> <ModuleParameters mps> <Import* imports>`) = [i | i<-imports];
public list[Import] getHeaderImports((Header)`<Tags tags> module <QualifiedName qn> <Import* imports>`) = [i | i<-imports];

public CheckResult convertAndExpandSymbol(Sym t, Configuration c) {
    <c,rt> = resolveSorts(convertSymbol(t), t@\loc, c);
    return expandType(rt, t@\loc, c);
}

public CheckResult convertAndExpandType(Type t, Configuration c) {
    rt = convertType(t);
    if ( (rt@errinfo)? && size(rt@errinfo) > 0) {
        for (m <- rt@errinfo) {
            c = addScopeMessage(c,m);
        }
    }
    return expandType(rt, t@\loc, c);
}

//  We allow constructor names (constructor types) to be used in the 'throws' clauses of Rascal functions
public CheckResult convertAndExpandThrowType(Type t, Configuration c) {
    rt = convertType(t);
    if( utc:\user(rn,pl) := rt && isEmpty(pl) && c.fcvEnv[rn]? && !(c.typeEnv[rn]?) ) {
        // Check if there is a value constructor with this name in the current environment
        if(constructor(_,_,_,_,_) := c.store[c.fcvEnv[rn]] || ( overload(_,overloaded(_,defaults)) := c.store[c.fcvEnv[rn]] && !isEmpty(filterSet(defaults, isConstructorType)) )) {
            // TODO: More precise resolution requires a new overloaded function to be used, which contains only value contructors;
            c.uses = c.uses + <c.fcvEnv[rn], utc@at>;
            c.usedIn[utc@at] = head(c.stack);
            return <c, rt>;   
        }
    } else if (\func(utc:\user(rn,pl), ps) := rt && isEmpty(pl) && c.fcvEnv[rn]? && !(c.typeEnv[rn]?) ) {
        // Check if there is a value constructor with this name in the current environment
        if(constructor(_,_,_,_,_) := c.store[c.fcvEnv[rn]] || ( overload(_,overloaded(_,defaults)) := c.store[c.fcvEnv[rn]] && !isEmpty(filterSet(defaults, isConstructorType)) )) {
            // TODO: More precise resolution requires a new overloaded function to be used, which contains only value contructors;
            c.uses = c.uses + <c.fcvEnv[rn], utc@at>;
            c.usedIn[utc@at] = head(c.stack);
            return <c, rt>;   
        }
	}
    
    if ( (rt@errinfo)? && size(rt@errinfo) > 0 ) {
        for (m <- rt@errinfo) {
            c = addScopeMessage(c,m);
        }
    }
    return expandType(rt, t@\loc, c);
}

public CheckResult convertAndExpandTypeArg(TypeArg t, Configuration c) {
    rt = convertTypeArg(t);
    if ( (rt@errinfo)? && size(rt@errinfo) > 0) {
        for (m <- rt@errinfo) {
            c = addScopeMessage(c,m);
        }
    }
    return expandType(rt, t@\loc, c);
}

public CheckResult convertAndExpandUserType(UserType t, Configuration c) {
    rt = convertUserType(t);
    if ( (rt@errinfo)? && size(rt@errinfo) > 0) {
        for (m <- rt@errinfo) {
            c = addScopeMessage(c,m);
        }
    }
    
    // Why don't we just expand the entire type? Because we want to keep this as a user type,
    // allowing us to get access to the type name. We do want to expand the parameters, though,
    // to make sure they have properly marked types (i.e., names are expanded into actual
    // types).
    // TODO: What if we create something like this?
    // alias T[&A <: T] = ...
    // This should probably be identified as something incorrect.
    //
    if (\user(utn,utps) := rt) {
        etlist = [ ];
        for (utpi <- utps) { < c, et > = expandType(utpi, t@\loc, c); etlist = etlist + et; }
        return < c, \user(utn, etlist) >;
    } else {
        throw "Conversion error: type for user type <t> should be user type, not <prettyPrintType(rt)>";
    }
}

public tuple[Configuration,Symbol] expandType(Symbol rt, loc l, Configuration c) {
    rt = bottom-up visit(rt) {
        case utc:\user(rn,pl) : {
            if (rn in c.typeEnv && !(c.store[c.typeEnv[rn]] is conflict)) {
                ut = c.store[c.typeEnv[rn]].rtype;
                if ((utc@at)?) {
                    c.uses = c.uses + < c.typeEnv[rn], utc@at >;
                    c.usedIn[utc@at] = head(c.stack);
                } 
                if (isAliasType(ut)) {
                    atps = getAliasTypeParameters(ut);
                    if (size(pl) == size(atps)) {
                        failures = { };
                        for (idx <- index(pl), !subtype(pl[idx],getTypeVarBound(atps[idx]))) 
                            failures = failures + makeFailType("Cannot instantiate parameter <idx> with type <prettyPrintType(pl[idx])>, parameter has bound <prettyPrintType(getTypeVarBound(atps[idx]))>", l);
                        if (size(failures) == 0) {
                            if (size(pl) > 0) {
                                bindings = ( getTypeVarName(atps[idx]) : pl[idx] | idx <- index(pl) );
                                insert(instantiate(getAliasedType(ut),bindings));
                            } else {
                                insert(getAliasedType(ut));
                            }
                        } else {
                            return < c, collapseFailTypes(failures) >;
                        } 
                    } else {
                        return < c, makeFailType("Alias <prettyPrintName(rn)> declares <size(atps)> type parameters, but given <size(pl)> instantiating types", l) >;
                    }
                } else if (isADTType(ut)) {
                    atps = getADTTypeParameters(ut);
                    if (size(pl) == size(atps)) {
                        failures = { };
                        for (idx <- index(pl), !subtype(pl[idx],getTypeVarBound(atps[idx]))) 
                            failures = failures + makeFailType("Cannot instantiate parameter <idx> with type <prettyPrintType(pl[idx])>, parameter has bound <prettyPrintType(getTypeVarBound(atps[idx]))>", l);
                        if (size(failures) == 0) {
                            if (size(pl) > 0) {
                                insert(\adt(getADTName(ut),pl));
                            } else {
                                insert(ut);
                            }
                        } else {
                            return < c, collapseFailTypes(failures) >;
                        } 
                    } else {
                        return < c, makeFailType("Data type <prettyPrintName(rn)> declares <size(atps)> type parameters, but given <size(pl)> instantiating types", l) >;
                    }
                } else if (ut is \lex || ut is \sort || ut is \keyword || ut is \layout) {
                  return < c, ut >;
                } else {
                    throw "User type should not refer to type <prettyPrintType(ut)>";
                }
            } else {
                return < c, makeFailType("Type <prettyPrintName(rn)> not declared", l) >;
            }
        }
    }
    return < c, rt >;
}

@doc{Check the types of Rascal comprehensions: Set (DONE)}
public CheckResult checkComprehension(Comprehension cmp:(Comprehension)`{ <{Expression ","}+ results> | <{Expression ","}+ generators> }`, Configuration c) {
    set[Symbol] failures = { };
    // We enter a new scope here since the names bound in the generators
    // are available inside the comprehension, but not outside, even if this
    // is part of a larger pattern.
    cComp = enterBooleanScope(c, cmp@\loc);

    for (gen <- generators) {
        < cComp, gt > = checkExp(gen,cComp);
        if (isFailType(gt)) {
            failures = failures + gt;
        } else if (!isBoolType(gt)) {
            failures = failures + makeFailType("Unexpected type <prettyPrintType(gt)>, generator should be an expression of type bool", gen@\loc);
        }
    }
    list[Symbol] elementTypes = [ \void() ];
    for (res <- results) {
        < cComp, rt > = checkExp(res,cComp);
        if (isFailType(rt)) {
            failures = failures + rt;
        } else {
            elementTypes = elementTypes + rt;
        }
    }
    
    // Leave the boolean scope to remove the added names from scope
    c = exitBooleanScope(cComp, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, cmp@\loc, failures);
    else
        return markLocationType(c, cmp@\loc, \set(lubList(elementTypes)));
}

@doc{Check the types of Rascal comprehensions: Map (DONE)}
public CheckResult checkComprehension(Comprehension cmp:(Comprehension)`( <Expression from> : <Expression to> | <{Expression ","}+ generators> )`, Configuration c) {
    set[Symbol] failures = { };

    // We enter a new scope here since the names bound in the generators
    // are available inside the comprehension, but not outside, even if this
    // is part of a larger pattern.
    cComp = enterBooleanScope(c, cmp@\loc);

    for (gen <- generators) {
        < cComp, gt > = checkExp(gen,cComp);
        if (isFailType(gt)) {
            failures = failures + gt;
        } else if (!isBoolType(gt)) {
            failures = failures + makeFailType("Unexpected type <prettyPrintType(gt)>, generator should be an expression of type bool", gen@\loc);
        }
    }

    < cComp, fromType > = checkExp(from,cComp);
    if (isFailType(fromType)) failures = failures + fromType;
    < cComp, toType > = checkExp(to,cComp);
    if (isFailType(toType)) failures = failures + toType;

    // Leave the boolean scope to remove the added names from scope
    c = exitBooleanScope(cComp, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, cmp@\loc, failures);
    else
        return markLocationType(c, cmp@\loc, \map(fromType,toType));
}

@doc{Check the types of Rascal comprehensions: List (DONE)}
public CheckResult checkComprehension(Comprehension cmp:(Comprehension)`[ <{Expression ","}+ results> | <{Expression ","}+ generators> ]`, Configuration c) {
    set[Symbol] failures = { };

    // We enter a new scope here since the names bound in the generators
    // are available inside the comprehension, but not outside, even if this
    // is part of a larger pattern.
    cComp = enterBooleanScope(c, cmp@\loc);

    for (gen <- generators) {
        < cComp, gt > = checkExp(gen,cComp);
        if (isFailType(gt)) {
            failures = failures + gt;
        } else if (!isBoolType(gt)) {
            failures = failures + makeFailType("Unexpected type <prettyPrintType(gt)>, generator should be an expression of type bool", gen@\loc);
        }
    }
    list[Symbol] elementTypes = [ \void() ];
    for (res <- results) {
        < cComp, rt > = checkExp(res,cComp);
        if (isFailType(rt)) {
            failures = failures + rt;
        } else {
            elementTypes = elementTypes + rt;
        }
    }

    // Leave the boolean scope to remove the added names from scope
    c = exitBooleanScope(cComp, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, cmp@\loc, failures);
    else
        return markLocationType(c, cmp@\loc, \list(lubList(elementTypes)));
}

@doc{Check the type of Rascal cases: PatternWithAction (DONE)}
public Configuration checkCase(Case cs:(Case)`case <PatternWithAction pwa>`, Symbol expected, Configuration c) {
    return checkPatternWithAction(pwa, expected, c);
}

@doc{Check the type of Rascal cases: Default (DONE)}
public Configuration checkCase(Case cs:(Case)`default : <Statement stmt>`, Symbol expected, Configuration c) {
    < c, t1 > = checkStmt(stmt, c);
    return c;   
}

@doc{Check the type of Rascal pattern with action constructs: Replacing (DONE)}
public Configuration checkPatternWithAction(PatternWithAction pwa:(PatternWithAction)`<Pattern p> =\> <Replacement r>`, Symbol expected, Configuration c) {
    // We need to enter a boolean scope here since we will be adding pattern vars in both
    // the case and potentially in a when clause
	cVisit = enterBooleanScope(c, pwa@\loc);
	    
    // First, calculate the pattern type. The expected type, which is the type of the item being
    // matched (in a switch, for instance), acts as the subject type. If we cannot calculate the
    // pattern type, assume it is value so we can continue checking, but report the error.
    < cVisit, pt > = calculatePatternType(p, cVisit, expected);
    if (isFailType(pt)) {
    	<cVisit, pt> = markLocationFailed(cVisit, p@\loc, pt);
        pt = \value();
    }
        
    // Now, calculate the replacement type. This should be a subtype of the pattern type, since it
    // should be substitutable for the matched term.
    < cVisit, rt > = checkReplacement(r, cVisit);
    if (!isFailType(rt) && !subtype(rt, pt))
        cVisit = addScopeError(cVisit,"Type of replacement, <prettyPrintType(rt)>, not substitutable for type of pattern, <prettyPrintType(pt)>",pwa@\loc);
    
    // Now, return in the environment, restoring the visible names to what they were on entry.
    return exitBooleanScope(cVisit, c);
}

@doc{Check the type of Rascal pattern with action constructs: Arbitrary (DONE)}
public Configuration checkPatternWithAction(PatternWithAction pwa:(PatternWithAction)`<Pattern p> : <Statement stmt>`, Symbol expected, Configuration c) {
    // We need to enter a boolean scope here since we will be adding pattern vars in
    // the case
	cVisit = enterBooleanScope(c, pwa@\loc);

    // First, calculate the pattern type. The expected type, which is the type of the item being
    // matched (in a switch, for instance), acts as the subject type. If we cannot calculate the
    // pattern type, assume it is value so we can continue checking, but report the error.
    < cVisit, pt > = calculatePatternType(p, cVisit, expected);
    if (isFailType(pt)) {
        <cVisit, pt> = markLocationFailed(cVisit, p@\loc, pt);
        pt = \value();
    }

    // We slightly abuse the label stack by putting cases in there as well. This allows us to  
    // keep track of inserted types without needing to invent a new mechanism for doing so.
    if (labelTypeInStack(cVisit,{visitLabel()})) {
        cVisit.labelStack = labelStackItem(getFirstLabeledName(cVisit,{visitLabel()}), caseLabel(), pt) + cVisit.labelStack;
    }

    // Second, calculate the statement type. This is done in the same environment, so the names
    // from the pattern persist.
    < cVisit, st > = checkStmt(stmt, cVisit);

    if (labelTypeInStack(cVisit,{visitLabel()})) {
        cVisit.labelStack = tail(cVisit.labelStack);
    }

    // Now, return in the environment, restoring the visible names to what they were on entry.
    return exitBooleanScope(cVisit, c);
}

@doc{Check the type of a Rascal replacement: Unconditional (DONE)}
public CheckResult checkReplacement(Replacement r:(Replacement)`<Expression e>`, Configuration c) {
    return checkExp(e, c);
}

@doc{Check the type of a Rascal replacement: Conditional  (DONE)}
public CheckResult checkReplacement(Replacement r:(Replacement)`<Expression e> when <{Expression ","}+ conds>`, Configuration c) {
    set[Symbol] failures = { };
    
    // Check the conditions, which are checked in the environment that includes bindings created by the
    // pattern. This creates no new bindings of its own.
    for (cnd <- conds) {
        < c, t1 > = checkExp(cnd, c);
        if (isFailType(t1)) failures = failures + t1;
        if (!isBoolType(t1)) failures = failures + makeFailType("Expected type bool, not <prettyPrintType(t1)>", cnd@\loc);
    }

    // Now check the main expression itself.    
    < c, t2 > = checkExp(e, c);
    if (isFailType(t2)) failures = failures + t2;

    // Don't mark the type with this location, this construct doesn't have a type. Instead
    // just return the type of e so we can use it in the caller.
    return < c, (size(failures) > 0) ? collapseFailTypes(failures) : t2 >;
}

@doc{Check the type of a Rascal visit: GivenStrategy (DONE)}
public CheckResult checkVisit(Visit v:(Visit)`<Strategy strat> visit ( <Expression sub> ) { < Case+ cases > }`, Configuration c) {
    // TODO: For now, we are just ignoring the strategy. Should we do anything with it here?
    < c, t1 > = checkExp(sub, c);
    // TODO: We need to compute what is reachable from t1. For now, we always use
    // value, allowing the case to have any type at all.
    for (cItem <- cases) c = checkCase(cItem, \value(), c);
    return markLocationType(c,v@\loc,t1);
}

@doc{Check the type of a Rascal visit: DefaultStrategy (DONE)}
public CheckResult checkVisit(Visit v:(Visit)`visit ( <Expression sub> ) { < Case+ cases > }`, Configuration c) {
    < c, t1 > = checkExp(sub, c);
    // TODO: We need to compute what is reachable from t1. For now, we always use
    // value, allowing the case to have any type at all.
    for (cItem <- cases) c = checkCase(cItem, \value(), c);
    return markLocationType(c,v@\loc,t1);
}

public Configuration addAppendTypeInfo(Configuration c, Symbol t, RName rn, set[LabelSource] ls, loc l) {
    possibleIndexes = [ idx | idx <- index(c.labelStack), c.labelStack[idx].labelSource in ls, c.labelStack[idx].labelName == rn ];
    if (size(possibleIndexes) == 0) {
        c = addScopeError(c, "Cannot add append information, no valid surrounding context found", l);
    } else {
        c.labelStack[possibleIndexes[0]].labelType = lub(c.labelStack[possibleIndexes[0]].labelType, t);
    }
    return c;
}

public Configuration addAppendTypeInfo(Configuration c, Symbol t, set[LabelSource] ls, loc l) {
    return addAppendTypeInfo(c,t,RSimpleName(""),ls,l);
}

public bool labelTypeInStack(Configuration c, set[LabelSource] ls) {
    possibleIndexes = [ idx | idx <- index(c.labelStack), c.labelStack[idx].labelSource in ls ];
    return size(possibleIndexes) > 0;
}

public Symbol getFirstLabeledType(Configuration c, set[LabelSource] ls) {
    possibleIndexes = [ idx | idx <- index(c.labelStack), c.labelStack[idx].labelSource in ls ];
    if (size(possibleIndexes) == 0) {
        throw "No matching labels in the label stack, you should call labelTypeInStack first to verify this!";
    } else {
        return c.labelStack[possibleIndexes[0]].labelType;
    }
}

public RName getFirstLabeledName(Configuration c, set[LabelSource] ls) {
    possibleIndexes = [ idx | idx <- index(c.labelStack), c.labelStack[idx].labelSource in ls ];
    if (size(possibleIndexes) == 0) {
        throw "No matching labels in the label stack, you should call labelTypeInStack first to verify this!";
    } else {
        return c.labelStack[possibleIndexes[0]].labelName;
    }
}

@doc{Check the type of a Rascal location literal}
public CheckResult checkLocationLiteral(LocationLiteral ll, Configuration c) {
    set[Symbol] failures = { };
    list[Expression] ipl = prodFilter(ll, bool(Production prd) { return prod(\label(_,\sort("Expression")),_,_) := prd; });
    for (ipe <- ipl) {
        if ((Expression)`<Expression ipee>` := ipe) {
            < c, t1 > = checkExp(ipee, c);
            if (isFailType(t1)) failures = failures + t1;
        }
    }
    if (size(failures) > 0)
        return markLocationFailed(c, ll@\loc, failures);
    else
        return markLocationType(c, ll@\loc, \loc());
}

@doc{Check the type of a Rascal string literal: Template}
public CheckResult checkStringLiteral(StringLiteral sl:(StringLiteral)`<PreStringChars pre> <StringTemplate st> <StringTail tl>`, Configuration c) {
    < c, t1 > = checkStringTemplate(st, c);
    < c, t2 > = checkStringTail(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return markLocationFailed(c, sl@\loc, {t1,t2});
    else
        return markLocationType(c,sl@\loc,\str());
}

@doc{Check the type of a Rascal string literal: Interpolated}
public CheckResult checkStringLiteral(StringLiteral sl:(StringLiteral)`<PreStringChars pre> <Expression exp> <StringTail tl>`, Configuration c) {
    < c, t1 > = checkExp(exp, c);
    < c, t2 > = checkStringTail(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return markLocationFailed(c, sl@\loc, {t1,t2});
    else
        return markLocationType(c,sl@\loc,\str());
}

@doc{Check the type of a Rascal string literal: NonInterpolated}
public CheckResult checkStringLiteral(StringLiteral sl:(StringLiteral)`<StringConstant sc>`, Configuration c) {
    return markLocationType(c,sl@\loc,\str());
}

@doc{Check the type of a Rascal string template: IfThen (DONE)}
public CheckResult checkStringTemplate(StringTemplate st:(StringTemplate)`if (<{Expression ","}+ conds>) {<Statement* pre> <StringMiddle body> <Statement* post>}`, Configuration c) {
    set[Symbol] failures = { };
    
    // We can bind variables in the condition that can be used in the body,
    // so enter a new scope here. NOTE: We always enter a new scope, since we
    // want to remove any introduced names at the end of the construct.
    cIf = enterBooleanScope(c, st@\loc);
    
    // Make sure each of the conditions evaluates to bool.
    for (cond <- conds) {
        < cIf, tc > = checkExp(cond, cIf);
        if (isFailType(tc)) failures = failures + tc;
        if (!isBoolType(tc)) failures = failures + makeFailType("Expected type bool, found <prettyPrintType(tc)>", cond@\loc);
    }
    
    // Now, check the body. The StringMiddle may have other comprehensions
    // embedded inside. We enter a new scope here as well; it is probably
    // redundant, but technically the body is a new scoping construct.
    cIfThen = enterBlock(cIf, st@\loc);

    for (preItem <- pre) {
        < cIfThen, tPre > = checkStmt(preItem, cIfThen);
        if (isFailType(tPre)) failures = failures + tPre;
    }   
    < cIfThen, tMid > = checkStringMiddle(body, cIfThen);
    if (isFailType(tMid)) failures = failures + tMid;
    if (!isStrType(tMid)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid)>", body@\loc);
    
    for (postItem <- post) {
        < cIfThen, tPost > = checkStmt(postItem, cIfThen);
        if (isFailType(tPost)) failures = failures + tPost;
    }   
    cIf = exitBlock(cIfThen, cIf);
    
    // Finally, recover the initial scope to remove any added names.
    c = exitBooleanScope(cIf, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, st@\loc, failures);
    else
        return markLocationType(c, st@\loc, \str());
}

@doc{Check the type of a Rascal string template: IfThenElse (DONE)}
public CheckResult checkStringTemplate(StringTemplate st:(StringTemplate)`if (<{Expression ","}+ conds>) {<Statement* thenpre> <StringMiddle thenbody> <Statement* thenpost>} else {<Statement* elsepre> <StringMiddle elsebody> <Statement* elsepost>}`, Configuration c) {
    set[Symbol] failures = { };
    
    // We can bind variables in the condition that can be used in the body,
    // so enter a new scope here. NOTE: We always enter a new scope, since we
    // want to remove any introduced names at the end of the construct.
    cIf = enterBooleanScope(c, st@\loc);
    
    // Make sure each of the conditions evaluates to bool.
    for (cond <- conds) {
        < cIf, tc > = checkExp(cond, cIf);
        if (isFailType(tc)) failures = failures + tc;
        if (!isBoolType(tc)) failures = failures + makeFailType("Expected type bool, found <prettyPrintType(tc)>", cond@\loc);
    }
    
    // Now, check the then body. The StringMiddle may have other comprehensions
    // embedded inside.
    cIfThen = enterBlock(cIf, st@\loc);

    for (preItem <- thenpre) {
        < cIfThen, tPre > = checkStmt(preItem, cIfThen);
        if (isFailType(tPre)) failures = failures + tPre;
    }   
    < cIfThen, tMid > = checkStringMiddle(thenbody, cIfThen);
    if (isFailType(tMid)) failures = failures + tMid;
    if (!isStrType(tMid)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid)>", thenbody@\loc);
    
    for (postItem <- thenpost) {
        < cIfThen, tPost > = checkStmt(postItem, cIfThen);
        if (isFailType(tPost)) failures = failures + tPost;
    }   
    cIf = exitBlock(cIfThen, cIf);
    
    // Then, check the else body. The StringMiddle may have other comprehensions
    // embedded inside.
    cIfElse = enterBlock(cIf, st@\loc);
    
    for (preItem <- elsepre) {
        < cIfElse, tPre2 > = checkStmt(preItem, cIfElse);
        if (isFailType(tPre2)) failures = failures + tPre2;
    }   
    < cIfElse, tMid2 > = checkStringMiddle(elsebody, cIfElse);
    if (isFailType(tMid2)) failures = failures + tMid2;
    if (!isStrType(tMid2)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid2)>", elsebody@\loc);

    for (postItem <- elsepost) {    
        < cIfElse, tPost2 > = checkStmt(postItem, cIfElse);
        if (isFailType(tPost2)) failures = failures + tPost2;
    }
    cIf = exitBlock(cIfElse, cIf);
    
    // Finally, recover the initial scope to remove any added names.
    c = exitBooleanScope(cIf, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, st@\loc, failures);
    else
        return markLocationType(c, st@\loc, \str());
}

@doc{Check the type of a Rascal string template: For (DONE)}
public CheckResult checkStringTemplate(StringTemplate st:(StringTemplate)`for (<{Expression ","}+ gens>) {<Statement* pre> <StringMiddle body> <Statement* post>}`, Configuration c) {
    set[Symbol] failures = { };
    
    // We can bind variables in the condition that can be used in the body,
    // so enter a new scope here. NOTE: We always enter a new scope, since we
    // want to remove any introduced names at the end of the construct.
    cFor = enterBooleanScope(c, st@\loc);
    
    // Make sure each of the generators evaluates to bool.
    for (gen <- gens) {
        < cFor, tg > = checkExp(gen, cFor);
        if (isFailType(tg)) failures = failures + tg;
        if (!isBoolType(tg)) failures = failures + makeFailType("Expected type bool, found <prettyPrintType(tg)>", gen@\loc);
    }
    
    // Now, check the body. The StringMiddle may have other comprehensions
    // embedded inside. We enter a new scope here as well; it is probably
    // redundant, but technically the body is a new scoping construct.
    cForBody = enterBlock(cFor, st@\loc);

    for (preItem <- pre) {
        < cForBody, tPre > = checkStmt(preItem, cForBody);
        if (isFailType(tPre)) failures = failures + tPre;
    }   
    < cForBody, tMid > = checkStringMiddle(body, cForBody);
    if (isFailType(tMid)) failures = failures + tMid;
    if (!isStrType(tMid)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid)>", body@\loc);

    for (postItem <- post) {    
        < cForBody, tPost > = checkStmt(postItem, cForBody);
        if (isFailType(tPost)) failures = failures + tPost;
    }   
    cFor = exitBlock(cForBody, cFor);
    
    // Finally, recover the initial scope to remove any added names.
    c = exitBooleanScope(cFor, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, st@\loc, failures);
    else
        return markLocationType(c, st@\loc, \str());
}

@doc{Check the type of a Rascal string template: DoWhile (DONE)}
public CheckResult checkStringTemplate(StringTemplate st:(StringTemplate)`do {<Statement* pre> <StringMiddle body> <Statement* post>} while (<Expression cond>)`, Configuration c) {
    set[Symbol] failures = { };
    
    // Check the body. The StringMiddle may have other comprehensions
    // embedded inside. We enter a new scope here as well; it is probably
    // redundant, but technically the body is a new scoping construct.
    cDoBody = enterBlock(c, st@\loc);

    for (preItem <- pre) {
        < cDoBody, tPre > = checkStmt(preItem, cDoBody);
        if (isFailType(tPre)) failures = failures + tPre;
    }   
    < cDoBody, tMid > = checkStringMiddle(body, cDoBody);
    if (isFailType(tMid)) failures = failures + tMid;
    if (!isStrType(tMid)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid)>", body@\loc);
    
    for (postItem <- post) {
        < cDoBody, tPost > = checkStmt(postItem, cDoBody);
        if (isFailType(tPost)) failures = failures + tPost;
    }
        
    c = exitBlock(cDoBody, c);
    
    // Unlike in a while loop, variables bound in the condition cannot be
    // used in the body, since the body runs before the condition is evaluated
    // for the first time. So, we enter a separate block to check the condition,
    // and then leave it once the condition is checked.
    cDo = enterBooleanScope(c, st@\loc);
    
    // Make sure the condition evaluates to bool.
    < cDo, tc > = checkExp(cond, cDo);
    if (isFailType(tc)) failures = failures + tc;
    if (!isBoolType(tc)) failures = failures + makeFailType("Expected type bool, found <prettyPrintType(tc)>", cond@\loc);
    
    c = exitBooleanScope(cDo, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, st@\loc, failures);
    else
        return markLocationType(c, st@\loc, \str());
}

@doc{Check the type of a Rascal string template: While (DONE)}
public CheckResult checkStringTemplate(StringTemplate st:(StringTemplate)`while (<Expression cond>) {<Statement* pre> <StringMiddle body> <Statement* post>}`, Configuration c) {
    set[Symbol] failures = { };
    
    // We can bind variables in the condition that can be used in the body,
    // so enter a new scope here. NOTE: We always enter a new scope, since we
    // want to remove any introduced names at the end of the construct.
    cWhile = enterBooleanScope(c, st@\loc);
    
    // Make sure the condition evaluates to bool.
    < cWhile, tc > = checkExp(cond, cWhile);
    if (isFailType(tc)) failures = failures + tc;
    if (!isBoolType(tc)) failures = failures + makeFailType("Expected type bool, found <prettyPrintType(tc)>", cond@\loc);
    
    // Now, check the body. The StringMiddle may have other comprehensions
    // embedded inside. We enter a new scope here as well; it is probably
    // redundant, but technically the body is a new scoping construct.
    cWhileBody = enterBlock(cWhile, st@\loc);

    for (preItem <- pre) {
        < cWhileBody, tPre > = checkStmt(preItem, cWhileBody);
        if (isFailType(tPre)) failures = failures + tPre;
    }
    
    < cWhileBody, tMid > = checkStringMiddle(body, cWhileBody);
    if (isFailType(tMid)) failures = failures + tMid;
    if (!isStrType(tMid)) failures = failures + makeFailType("Expected type str, found <prettyPrintType(tMid)>", body@\loc);
    
    for (postItem <- post) {
        < cWhileBody, tPost > = checkStmt(postItem, cWhileBody);
        if (isFailType(tPost)) failures = failures + tPost;
    }
        
    cWhile = exitBlock(cWhileBody, cWhile);
    
    // Finally, recover the initial scope to remove any added names.
    c = exitBooleanScope(cWhile, c);
    
    if (size(failures) > 0)
        return markLocationFailed(c, st@\loc, failures);
    else
        return markLocationType(c, st@\loc, \str());
}

@doc{Check the type of a Rascal string tail: MidInterpolated (DONE)}
public CheckResult checkStringTail(StringTail st:(StringTail)`<MidStringChars msc> <Expression exp> <StringTail tl>`, Configuration c) {
    < c, t1 > = checkExp(exp, c);
    < c, t2 > = checkStringTail(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return < c, collapseFailTypes({t1,t2}) >;
    else
        return < c, \str() >;   
}

@doc{Check the type of a Rascal string tail: Post (DONE)}
public CheckResult checkStringTail(StringTail st:(StringTail)`<PostStringChars post>`, Configuration c) {
    return < c, \str() >;
}

@doc{Check the type of a Rascal string tail: MidTemplate (DONE)}
public CheckResult checkStringTail(StringTail st:(StringTail)`<MidStringChars msc> <StringTemplate t> <StringTail tl>`, Configuration c) {
    < c, t1 > = checkStringTemplate(t, c);
    < c, t2 > = checkStringTail(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return < c, collapseFailTypes({t1,t2}) >;
    else
        return < c, \str() >;   
}

@doc{Check the type of a Rascal string middle: Mid (DONE)}
public CheckResult checkStringMiddle(StringMiddle sm:(StringMiddle)`<MidStringChars msc>`, Configuration c) {
    return < c, \str() >;
}

@doc{Check the type of a Rascal string middle: Template (DONE)}
public CheckResult checkStringMiddle(StringMiddle sm:(StringMiddle)`<MidStringChars msc> <StringTemplate st> <StringMiddle tl>`, Configuration c) {
    < c, t1 > = checkStringTemplate(st, c);
    < c, t2 > = checkStringMiddle(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return < c, collapseFailTypes({t1,t2}) >;
    else
        return < c, \str() >;   
}

@doc{Check the type of a Rascal string middle: Interpolated (DONE)}
public CheckResult checkStringMiddle(StringMiddle sm:(StringMiddle)`<MidStringChars msc> <Expression e> <StringMiddle tl>`, Configuration c) {
    < c, t1 > = checkExp(e, c);
    < c, t2 > = checkStringMiddle(tl, c);
    if (isFailType(t1) || isFailType(t2))
        return < c, collapseFailTypes({t1,t2}) >;
    else
        return < c, \str() >;   
} 

@doc{Check catch blocks: Default (DONE)}
public Configuration checkCatch(Catch ctch:(Catch)`catch : <Statement body>`, Configuration c) {
    // We check the statement, which will mark any type errors in the catch block. However,
    // the block itself does not yield a type.
    < c, tb > = checkStmt(body, c);
    return c;
}

@doc{Check catch blocks: Binding (DONE)}
public Configuration checkCatch(Catch ctch:(Catch)`catch <Pattern p> : <Statement body>`, Configuration c) {
    // We enter a block scope because that allows us to set the correct
    // scope for the pattern -- variables bound in the pattern are
    // available in the catch body. NOTE: Calculating the pattern type
    // has the side effect of introducing the variables into scope.
    cCatch = enterBlock(c, ctch@\loc);
    tp = \void();
    if ((Pattern)`<QualifiedName qn>` := p) {
        < cCatch, tp > = calculatePatternType(p, cCatch, \adt("RuntimeException",[]));
    } else {
        < cCatch, tp > = calculatePatternType(p, cCatch);
    }
    if (isFailType(tp)) cCatch.messages = getFailures(tp);
        
    // Attempt to check the body regardless of whether the pattern is typable. NOTE: We could
    // also avoid this -- the tradeoff is that, if we check it anyway, we can possibly catch
    // more errors, but we may also get a number of spurious errors about names not being
    // in scope.
    < cCatch, tb > = checkStmt(body, cCatch);
    
    // Exit the block to remove the bound variables from the scope.
    c = exitBlock(cCatch, c);
    
    return c;
}

public Configuration addNameWarning(Configuration c, RName n, loc l) {
    currentModuleLoc = head([c.store[i].at | i <- c.stack, \module(_,_) := c.store[i]]);
    if (c.store[c.fcvEnv[n]] has at && currentModuleLoc.path != c.store[c.fcvEnv[n]].at.path)
        c = addScopeWarning(c, "Name defined outside of current module", l);
    return c;
}

public anno map[loc,str] Tree@docStrings;
public anno map[loc,set[loc]] Tree@docLinks;

public Configuration checkAndReturnConfig(str mpath) {
    c = newConfiguration();
	t = getModuleParseTree(mpath);    
    try {
		if (t has top && Module m := t.top)
			c = checkModule(m, c);
	} catch : {
		c.messages = {error("Encountered error checking module <mpath>", t@\loc)};
	}
	return c;
}

public Module check(Module m) {
    c = newConfiguration();
    c = checkModule(m, c);

    //| overload(set[int] items, Symbol rtype)
    //| datatype(RName name, Symbol rtype, int containedIn, set[loc] ats)

    dt1 = now();
    map[loc,set[loc]] docLinks = ( );
    for (<l,i> <- invert(c.uses)) {
        set[loc] toAdd = { };
        if (overload(items,_) := c.store[i]) {
            toAdd = { c.store[itm].at | itm <- items };
        } else if (datatype(_,_,_,ats) := c.store[i]) {
            toAdd = ats;
        } else if (sorttype(_,_,_,ats) := c.store[i]) {
           toAdd = ats;
        } else {
            toAdd = { c.store[i].at };
        }   
        if (l in docLinks) {
            docLinks[l] = docLinks[l] + toAdd;
        } else {
            docLinks[l] = toAdd;
        }
    }
    c = pushTiming(c,"Annotating", dt1, now());
    for (t <- c.timings) println("<t.tmsg>:<createDuration(t.tstart,t.tend)>");
    return m[@messages = c.messages]
            [@docStrings = ( l : "TYPE: <prettyPrintType(c.locationTypes[l])>" | l <- c.locationTypes<0>)]
            [@docLinks = docLinks];
}

public default Module check(Tree t) {
	if (t has top && Module m := t.top)
		return check(m);
	else
		throw "Cannot check arbitrary trees";
}

CheckResult resolveSorts(Symbol sym, loc l, Configuration c) {
  sym = visit(sym) {
   case sort(str name) : {
     sname = RSimpleName(name);
     if (sname notin c.typeEnv || !(c.store[c.typeEnv[sname]] is sorttype)) {
       c = addScopeMessage(c,error("Syntax type <name> is not defined", l));
     }
     else {
       c.uses = c.uses + < c.typeEnv[sname], l >;
       c.usedIn[l] = head(c.stack);
       insert c.store[c.typeEnv[sname]].rtype;
     } // TODO finish
   }
  }
  
  return <c, sym>;
}

tuple[Production,Configuration] resolveProduction(Production prod, loc l, Configuration c, bool imported) {
	// Resolve names in the production given a type environment
	typeEnv = c.typeEnv;
	prod = visit(prod) {
		case \sort(n): {
			name = RSimpleName(n);
			if(typeEnv[name]? && c.store[typeEnv[name]] is sorttype) {
				sym = c.store[typeEnv[name]].rtype;
				if(\lex(n) := sym || \layouts(n) := sym || \keywords(n) := sym) {
					insert sym;
				}
			} else {
				if(!imported) {
					c = addScopeMessage(c, error("Syntax type <n> is not defined", l));
				} else {
					c = addScopeMessage(c, warning("Leaking syntax type <n>", l));
				}
			}
			fail;
		}
		case \parameterized-sort(n,ps): {
			name = RSimpleName(n);
			if(typeEnv[name]? && c.store[typeEnv[name]] is sorttype) {
				sym = c.store[typeEnv[name]].rtype;
				if(\parameterized-lex(n,_) := sym) {
					insert \parameterized-lex(n,ps);
				}
			} else {
				if(!imported) {
					c = addScopeMessage(c, error("Syntax type <n> is not defined", l));
				} else {
					c = addScopeMessage(c, warning("Leaking syntax type <n>", l));
				}
			}
			fail;
		}
		case \lex(n): {
			name = RSimpleName(n);
			if(typeEnv[name]? && c.store[typeEnv[name]] is sorttype) {
				sym = c.store[typeEnv[name]].rtype;
				if(\sort(n) := sym || \layouts(n) := sym || \keywords(n) := sym) {
					insert sym;
				}
			} else {
				if(!imported) {
					c = addScopeMessage(c, error("Syntax type <n> is not defined", l));
				} else {
					c = addScopeMessage(c, warning("Leaking syntax type <n>", l));
				}
			}
			fail;
		 }
		case \parameterized-lex(n,ps): {
			name = RSimpleName(n);
			if(typeEnv[name]? && c.store[typeEnv[name]] is sorttype) {
				sym = c.store[typeEnv[name]].rtype;
				if(\parameterized-sort(n,_) := sym) {
					insert \parameterized-sort(n,ps);
				}
			} else {
				if(!imported) {
					c = addScopeMessage(c, error("Syntax type <n> is not defined", l));
				} else {
					c = addScopeMessage(c, warning("Leaking syntax type <n>", l));
				}
			}
			fail;
		}
	}
	return <prod,c>;
}

public bool comparableOrNum(Symbol l, Symbol r) {
	leftAsNum = visit(l) {
		case \int() => \num()
		case \real() => \num()
		case \rat() => \num()
	};
	
	rightAsNum = visit(r) {
		case \int() => \num()
		case \real() => \num()
		case \rat() => \num()
	};
	
	return comparable(l, r) || comparable(leftAsNum,rightAsNum);
}
