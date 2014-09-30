@license{
  Copyright (c) 2009-2014 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::CheckerConfig

import analysis::graphs::Graph;
import IO;
import Set;
import Map;
import Message;
import Node;
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

// TODO: Clean up imports

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
    | function(RName name, Symbol rtype, KeywordParamMap keywordParams, bool isVarArgs, int containedIn, list[Symbol] throwsTypes, bool isDeferred, loc at)
    | closure(Symbol rtype, KeywordParamMap keywordParams, int containedIn, loc at)
    | \module(RName name, loc at)
    | overload(set[int] items, Symbol rtype)
    | datatype(RName name, Symbol rtype, int containedIn, set[loc] ats)
    | sorttype(RName name, Symbol rtype, int containedIn, set[loc] ats)
    | constructor(RName name, Symbol rtype, KeywordParamMap keywordParams, int containedIn, loc at)
    | production(RName name, Symbol rtype, int containedIn, Production p, loc at)
    | annotation(RName name, Symbol rtype, Symbol onType, int containedIn, loc at)
    | \tag(RName name, TagKind tkind, set[Symbol] onTypes, int containedIn, loc at)
    | \alias(RName name, Symbol rtype, int containedIn, loc at)
    | booleanScope(int containedIn, loc at)
    | blockScope(int containedIn, loc at)
    ;

data LabelStackItem = labelStackItem(RName labelName, LabelSource labelSource, Symbol labelType);
data Timing = timing(str tmsg,datetime tstart,datetime tend);

private ModuleInfo createModInfo(Configuration c, RName modName) {
	return modInfo(modName, c.labelEnv, c.fcvEnv, c.typeEnv, c.annotationEnv, c.tagEnv);
}

data ModuleInfo = modInfo(RName modName,
						  map[RName,int] labelEnv,
                          map[RName,int] fcvEnv,
                          map[RName,int] typeEnv,
                          map[RName,int] annotationEnv,
                          map[RName,int] tagEnv);

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
                            map[int,value] keywordDefaults,
                            rel[int,RName,value] dataKeywordDefaults,
                            map[str,Symbol] tvarBounds,
                            map[RName,ModuleInfo] moduleInfo,
                            map[RName,int] globalAdtMap,
                            map[RName,int] globalSortMap,
                            map[int,value] deferredSignatures,
                            set[RName] unimportedNames,
                            bool importing
                           );

public Configuration newConfiguration() = config({},(),\void(),(),(),(),(),(),(),(),(),(),{},(),(),{},{},{},(),{},{},[],[],[],0,0,(),{ },(),(),(),(),(),{},false);

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
    cNew.tvarBounds = cOld.tvarBounds;
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

@doc{Get the container in which the given item is defined.}
public int definingContainer(Configuration c, int i) {
	if (c.store[i] is overload) return definingContainer(c, getOneFrom(c.store[i].items));
    cid = c.store[i].containedIn;
    if (c.store[cid] is \module || c.store[cid] is function || c.store[cid] is closure) return cid;
    return definingContainer(c,cid);
}

@doc{Starting with the current context, get the container in which it is being defined.}
public list[int] upToContainer(Configuration c, int i) {
	// NOTE: The reason we do not need to check for overload here is because the current context can
	// never be an overload -- it has to be some actual scope item that could be put on the stack.
    if (c.store[i] is \module || c.store[i] is function || c.store[i] is closure) return [i];
    return [i] + upToContainer(c,c.store[i].containedIn);
}

@doc{Get the container for a given abstract value.}
private int getContainedIn(Configuration c, AbstractValue av) {
	if (av has containedIn) return av.containedIn;
	// NOTE: This assumes that overloads are all defined at the same level (all at the top
	// of a module, for instance), or this won't work properly at the calling site.
	if (av is overload) return getContainedIn(c, c.store[getOneFrom(av.items)]);
	return head(c.stack);
}

public tuple[Configuration,Symbol] checkTVarBound(Configuration c, loc l, Symbol rt) {
	// Get all the type vars out of the type rt
	tvars = collectTypeVars(rt);
	
    if (size(tvars) > 0) {
    	// Get back a relation from names to bounds to the var
    	tvrel = { < getTypeVarName(tv), getTypeVarBound(tv), tv > | tv <- tvars };
    	
    	for (n <- tvrel<0>) {
    		tvn = tvrel[n];
    		
    		// Filter the relation down to just those vars where the bound was actually given
    		wBounds = { < b, tv > | < b, tv > <- tvn, (tv@boundGiven)? && tv@boundGiven };
    		
    		if (size(wBounds) > 0 && n in c.tvarBounds) {
    			// If bounds were given and the type var was already in the config, make sure the new
    			// bounds are equivalent to the old bounds
    			for (bnd <- wBounds<0>) {
		    		if (!equivalent(bnd,c.tvarBounds[n])) {
		    			c = addScopeError(c, "The bound given for the type, <prettyPrintType(bnd)>, does not match the bound declared earlier for &<n>, <prettyPrintType(c.tvarBounds[n])>", l);
		    		}
		    	}
    		} else if (size(wBounds) > 0) {
    			// If bounds were given but this type var isn't in the config yet, make sure the bounds
    			// are internally consistent
    			nonequiv = { < bnd1, bnd2 > | bnd1 <- wBounds<0>, bnd2 <- wBounds<0>, !equivalent(bnd1,bnd2) };
    			if (size(nonequiv) > 0) {
    				< bnd1, bnd2 > = getOneFrom(nonequiv);
    				c = addScopeError(c, "Non-equivalent bounds are given for &<n>, e.g. <prettyPrintType(bnd1)> and <prettyPrintType(bnd2)>", l);
    				
    				// We had non-equivalent bounds; we just lub them all and set that as the bound to use going forward, which
    				// hopefully will cut down on extra error reports because of this
    				tolub = toList(wBounds<0>);
    				lubbed = ( tolub[0] | lub(it,elem) | elem <- tolub[1..] );
    				c.tvarBounds[n] = lubbed;
	    		} else {
	    			// All the bounds were equivalent; we just pick one at random and save it
	    			c.tvarBounds[n] = getOneFrom(wBounds<0>);
	    		}
    		}
    	}
    	
    	// Now that we have bounds, make sure they are consistent in the actual type
    	rt = bottom-up visit(rt) {
    		case tp:\parameter(tvn,tvb) => tp[bound=c.tvarBounds[tvn]][@boundGiven=true] when tvn in c.tvarBounds
    	}
    }
    
    return < c, rt >;
}

@doc{Add a new top-level variable into the configuration.}
public Configuration addTopLevelVariable(Configuration c, RName n, bool inf, Vis visibility, loc l, Symbol rt) {
	moduleId = head([i | i <- c.stack, c.store[i] is \module]);
	moduleName = c.store[moduleId].name;

	< c, rt > = checkTVarBound(c, l, rt);

	int insertVariable() {
		varId = c.nextLoc;

		existingDefs = invert(c.definitions)[l];
		if (!isEmpty(existingDefs)) {
			varId = getOneFrom(existingDefs);
		} else {
			c.nextLoc = varId + 1;
			c.store[varId] = variable(n,rt,inf,head(c.stack),l);
			c.definitions = c.definitions + < varId, l >;
			if (defaultVis() !:= visibility) {
				c.visibilities[varId] = visibility;
			}
		}

		return varId;
	}
		
	varId = insertVariable();
		
	if (n notin c.fcvEnv) {
		c.fcvEnv[n] = varId;
		c.fcvEnv[appendName(moduleName,n)] = varId;
    } else {
		c = addScopeError(c, "Attempted redeclaration of name <prettyPrintName(n)> in module <prettyPrintName(moduleName)>", l);
    }
        
    return c;
}

@doc{Add a new top-level variable without an explicit visibility into the configuration.}
public Configuration addTopLevelVariable(Configuration c, RName n, bool inf, loc l, Symbol rt) {
    return addTopLevelVariable(c, n, inf, defaultVis(), l, rt);
}

@doc{Add an imported top-level variable into the configuration.}
public Configuration addImportedVariable(Configuration c, RName n, int varId, bool addFullName=false) {
	if (n in c.fcvEnv && c.fcvEnv[n] == varId) return c;

	moduleId = head([i | i <- c.stack, c.store[i] is \module]);
	moduleName = c.store[moduleId].name;
	if (n notin c.fcvEnv) {
		c.fcvEnv[n] = varId;
		if (n is RSimpleName && addFullName) {
			c.fcvEnv[appendName(moduleName,n)] = varId;
		}
    } else {
		c = addScopeError(c, "Attempted redeclaration of imported name <prettyPrintName(n)> in module <prettyPrintName(moduleName)>", c.store[varId].at);
    }
    return c;
}

@doc{Add a new local variable into the configuration.}
public Configuration addLocalVariable(Configuration c, RName n, bool inf, loc l, Symbol rt) {
	moduleId = head([i | i <- c.stack, c.store[i] is \module]);
	moduleName = c.store[moduleId].name;

	< c, rt > = checkTVarBound(c, l, rt);

	int insertVariable() {
		varId = c.nextLoc;
		c.store[varId] = variable(n,rt,inf,head(c.stack),l);
		c.definitions = c.definitions + < varId, l >;
		c.nextLoc = varId + 1;
		return varId;
	}

	varId = insertVariable();
	
	if (n notin c.fcvEnv) {
		c.fcvEnv[n] = varId;
	} else {
		// The name is already defined: what items are defined by this name?
		conflictIds = (overload(ids,_) := c.store[c.fcvEnv[n]]) ? ids : { c.fcvEnv[n] };
		// Also, what "container" (e.g., function) is each of these definitions in?
		containingIds = { definingContainer(c,i) | i <- conflictIds };
		// What scopes are we currently within?
		containingScopes = upToContainer(c,head(c.stack));

		// If we have a redeclaration, this is fine if it follows the correct rules, but we
		// cannot redeclare a name within the same container -- e.g., we cannot declare the
		// same name twice in the same function.
		if (size(toSet(containingScopes) & containingIds) > 0) {
			c = addScopeError(c, "Cannot re-declare name that is already declared in the current function or closure: <prettyPrintName(n)>", l);
		} else {
			c.fcvEnv[n] = varId;
		}
	}

	return c;
}

@doc{Add an anonymous variable into the configuration.}
public Configuration addUnnamedVariable(Configuration c, loc l, Symbol rt) {
	< c, rt > = checkTVarBound(c, l, rt);
	c.store[c.nextLoc] = variable(RSimpleName("_"),rt,true,head(c.stack),l);
	c.definitions = c.definitions + < c.nextLoc, l >;
	c.nextLoc = c.nextLoc + 1;

	return c;
}

@doc{Add an annotation into the configuration}
public Configuration addAnnotation(Configuration c, RName n, Symbol rt, Symbol rtOn, Vis visibility, loc l) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

	int insertAnnotation() {
		existingDefs = invert(c.definitions)[l];
		if (!isEmpty(existingDefs)) return getOneFrom(existingDefs);

		annId = c.nextLoc;
		c.store[annId] = annotation(n,rt,rtOn,moduleId,l);
		c.definitions = c.definitions + < annId, l >;
		c.nextLoc = annId + 1;
		return annId;
	}

	void updateAnnotation(int id, int newid) {
		if (c.store[id] is overload) {
			c.store[id].items = c.store[id].items + newid;
			c.store[id].rtype = lub(c.store[id].rtype, rt);
		} else {
			oitem = overload({ id, newid }, lub(c.store[id].rtype, rt));
			oid = c.nextLoc;
			c.store[oid] = oitem;
			c.nextLoc = oid + 1;
			c.annotationEnv[n] = oid;
		}
	}

	if (n notin c.annotationEnv) {
		annId = insertAnnotation();
		c.annotationEnv[n] = annId;
	} else {
		// If an annotation of this name has already been declared, we need to make sure that either it has not been
		// declared on this (or a comparable type) or, if it has, that the types are equivalent
		annIds = (c.store[c.annotationEnv[n]] is overload) ? c.store[c.annotationEnv[n]].items : { c.annotationEnv[n] };
		onComparableTypes = { aId | aId <- annIds, comparable(rtOn, c.store[aId].onType) };
		
		if (size(onComparableTypes) == 0) {
			// This annotation has not been declared before on any comparable types.
			annId = insertAnnotation();
			updateAnnotation(c.annotationEnv[n], annId);			
		} else {
			// This annotation has been declared before on at least one comparable type.
			bool firstTimeThrough = true;
			for (ct <- onComparableTypes) {
				if (!equivalent(rt,c.store[ct].rtype)) {
					c = addScopeError(c, "Annotation <prettyPrintName(n)> has already been declared with type <c.store[ct].rtype> in module <prettyPrintName(c.store[c.store[ct].containedIn].name)>", l);
					if (firstTimeThrough) {
						// If we don't add an item regardless, this can lead to errors later, but we don't want to add it repeatedly
						annId = insertAnnotation();
						firstTimeThrough = false;
					}
				} else {
					c.definitions = c.definitions + < getOneFrom(onComparableTypes), l >;
				}
			}
		}
	}
	
	return c;
}

@doc{Add an imported annotation into the configuration}
public Configuration addImportedAnnotation(Configuration c, RName n, int annId) {
	if (n in c.annotationEnv && c.annotationEnv[n] == annId) return c;

	void updateAnnotation(int id) {
		if (c.store[id] is overload) {
			c.store[id].items = c.store[id].items + ( (c.store[annId] is overload) ? c.store[annId].items : { annId } );
			c.store[id].rtype = lub(c.store[id].rtype, c.store[annId].rtype);
		} else {
			oitem = overload(( (c.store[annId] is overload) ? c.store[annId].items : { annId } ) + id, lub(c.store[id].rtype, c.store[annId].rtype));
			oid = c.nextLoc;
			c.store[oid] = oitem;
			c.nextLoc = oid + 1;
			c.annotationEnv[n] = oid;
		}
	}

	if (n notin c.annotationEnv) {
		c.annotationEnv[n] = annId;
	} else {
		// If an annotation of this name has already been declared, we need to make sure that either it has not been
		// declared on this (or a comparable type) or, if it has, that the types are equivalent
		annIds = (c.store[c.annotationEnv[n]] is overload) ? c.store[c.annotationEnv[n]].items : { c.annotationEnv[n] };
		mergeIds = (c.store[annId] is overload) ? c.store[annId].items : { annId };
		onComparableTypes = { < aId, mId > | aId <- annIds, mId <- mergeIds, comparable(c.store[mId].onType, c.store[aId].onType) };
		
		if (size(onComparableTypes) == 0) {
			// This annotation has not been declared before on any comparable types.
			updateAnnotation(c.annotationEnv[n]);			
		} else {
			// This annotation has been declared before on at least one comparable type.
			for (< ct, mId > <- onComparableTypes, !equivalent(c.store[mId].rtype,c.store[ct].rtype)) {
				c = addScopeError(c, "Import of annotation <prettyPrintName(n)> in module <prettyPrintName(c.store[c.store[annId].containedIn].name)> conflicts with existing annotation in module <prettyPrintName(c.store[c.store[ct].containedIn].name)>", c.store[mid].at);
			}
		}
	}
	
	return c;
}

@doc{Add a user-defined ADT into the configuration}
public Configuration addADT(Configuration c, RName n, Vis visibility, loc l, Symbol rt) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);

	int addDataType() {
		if (n in c.globalAdtMap) {
			return c.globalAdtMap[n];
		}
		
		existingDefs = invert(c.definitions)[l];
		if (!isEmpty(existingDefs)) return getOneFrom(existingDefs);
		
		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = datatype(n,rt,moduleId,{ l });
		c.definitions = c.definitions + < itemId, l >;
		c.globalAdtMap[n] = itemId;
		return itemId;
	}

	Configuration extendDataType(Configuration c, int existingId) {
		c.store[existingId].ats = c.store[existingId].ats + l;
		c.typeEnv[fullName] = existingId;
		c.definitions = c.definitions + < existingId, l >;		
		return c; 	
	}

	if (n notin c.typeEnv && n notin c.globalAdtMap) {
		// No type of this name already exists. Add a new data type item, and link it in
		// using both the qualified and unqualified names.
		itemId = addDataType();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (n notin c.typeEnv && n in c.globalAdtMap) {
		existingId = c.globalAdtMap[n];
		c = extendDataType(c, existingId);
	} else if (c.store[c.typeEnv[n]] is datatype) {
		// A datatype of this name already exists. Use this existing data type item, adding
		// a link to it using the qualified name. NOTE: This means that the same type may be available
		// using multiple qualified names, but all will point to the same instance.
		existingId = c.typeEnv[n];
		c = extendDataType(c, existingId);
	} else if (c.store[c.typeEnv[n]] is sorttype || c.store[c.typeEnv[n]] is \alias) {
		// A sort or alias with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them.
		itemId = addDataType();
		c = addScopeError(c, "An alias or nonterminal named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", l);
	}
	
	return c;
}

@doc{Add an imported user-defined ADT into the configuration}
public Configuration addImportedADT(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.typeEnv && c.typeEnv[n] == itemId) return c;
	
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

	if (n notin c.typeEnv) {
		c.typeEnv[n] = itemId;
		if (n is RSimpleName && addFullName) {
			c.typeEnv[appendName(moduleName,n)] = itemId;
		}
	} else if (c.store[c.typeEnv[n]] is sorttype || c.store[c.typeEnv[n]] is \alias) {
		c = addScopeError(c, "An alias or nonterminal named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", c.store[itemId].at);
	}
	
	return c;
}

@doc{Add a user-defined non-terminal type into the configuration}
public Configuration addNonterminal(Configuration c, RName n, loc l, Symbol sort) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);

	int addNonTerminal() {
		if (n in c.globalSortMap) {
			return c.globalSortMap[n];
		}
		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = sorttype(n,sort,moduleId,{ l });
		c.definitions = c.definitions + < itemId, l >;
		c.globalSortMap[n] = itemId;
		return itemId;
	}

	Configuration extendNonTerminal(Configuration c, int existingId) {
		c.store[existingId].ats = c.store[existingId].ats + l;
		if (n notin c.typeEnv) c.typeEnv[n] = existingId;
		c.typeEnv[fullName] = existingId;
		c.definitions = c.definitions + < existingId, l >;
		return c; 	
	}
		    
	if (n notin c.typeEnv && n notin c.globalSortMap) {
		// No type of this name already exists. Add a new nonterminal item, and link it in
		// using both the qualified and unqualified names.
		itemId = addNonTerminal();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (n notin c.typeEnv && n in c.globalSortMap) {
		existingId = c.globalSortMap[n];
		c = extendNonTerminal(c, existingId);
	} else if (c.store[c.typeEnv[n]] is sorttype) {
		// A nonterminal of this name already exists. Use this existing nonterminal item, adding
		// a link to it using the qualified name. NOTE: This means that the same type may be available
		// using multiple qualified names, but all will point to the same instance.
		existingId = c.typeEnv[n];
		c = extendNonTerminal(c, existingId);
	} else if (c.store[c.typeEnv[n]] is datatype || c.store[c.typeEnv[n]] is \alias) {
		// An adt or alias with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them.
		itemId = addNonTerminal();
		c = addScopeError(c, "An alias or adt named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", l);
	}
	
	return c;
}

@doc{Add an imported non-terminal type into the configuration}
public Configuration addImportedNonterminal(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.typeEnv && c.typeEnv[n] == itemId) return c;

	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
		    
	if (n notin c.typeEnv) {
		c.typeEnv[n] = itemId;
		if (n is RSimpleName && addFullName) {
			c.typeEnv[appendName(moduleName, n)] = itemId;
		}
	} else if (c.store[c.typeEnv[n]] is datatype || c.store[c.typeEnv[n]] is \alias) {
		c = addScopeError(c, "An alias or adt named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", c.store[itemId].at);
	}
	
	return c;
}

@doc{Add a new alias into the configuration}
public Configuration addAlias(Configuration c, RName n, Vis vis, loc l, Symbol rt) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);
	
	int addAlias() {
		existingDefs = invert(c.definitions)[l];
		if (!isEmpty(existingDefs)) return getOneFrom(existingDefs);

		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = \alias(n,rt,moduleId,l);
		c.definitions = c.definitions + < itemId, l >;
		return itemId;
	}

	if (n notin c.typeEnv) {
		itemId = addAlias();
		c.typeEnv[n] = itemId;
		c.typeEnv[fullName] = itemId;
	} else if (n in c.typeEnv) {
		// An adt, alias, or sort with this name already exists in the same module. We cannot perform this
		// type of redefinition, so this is an error. This is because there is no way we can qualify the names
		// to distinguish them.
		itemId = addAlias();
		c = addScopeError(c, "An adt, alias, or nonterminal named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", l);
	}
	
	return c;
}

@doc{Add an imported alias into the configuration}
public Configuration addImportedAlias(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.typeEnv && c.typeEnv[n] == itemId) return c;

	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

	if (n notin c.typeEnv) {
		c.typeEnv[n] = itemId;
		if (n is RSimpleName && addFullName) {
			c.typeEnv[appendName(moduleName, n)] = itemId;
		}
	} else if (n in c.typeEnv) {
		c = addScopeError(c, "An adt, alias, or nonterminal named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", c.store[itemId].at);
	}
	
	return c;
}

@doc{Add a constructor into the configuration}
public Configuration addConstructor(Configuration c, RName n, loc l, Symbol rt, KeywordParamRel commonParams, KeywordParamRel keywordParams) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);

	adtName = RSimpleName(rt.\adt.name);
	fullAdtName = appendName(moduleName, adtName);
	
	adtId = 0;
	if (adtName in c.globalAdtMap) {
		adtId = c.globalAdtMap[adtName];
	} else {
		c = addScopeError(c, "Cannot add constructor <prettyPrintName(n)>, associated ADT <prettyPrintName(adtName)> not found", l);
		return c;
	}

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
							aft = c.adtFields[<adtId,fn>];
							if (!equivalent(ft, aft)) {
								c = addScopeError(c,"Field <fn> already defined as type <prettyPrintType(aft)> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(ft)>",l);
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
				aft = c.adtFields[<adtId,pnAsString>];
				if (!equivalent(pt, aft)) {
					c = addScopeError(c,"Field <pnAsString> already defined as type <prettyPrintType(aft)> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(pt)>",l);
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
				aft = c.adtFields[<adtId,pnAsString>];
				if (!equivalent(pt, aft)) {
					c = addScopeError(c,"Field <pnAsString> already defined as type <prettyPrintType(aft)> on datatype <prettyPrintName(adtName)>, cannot redefine to type <prettyPrintType(pt)>",l);
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
		} else if (c.store[c.fcvEnv[n]] is constructor || c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is production) {
			nonDefaults = {};
			defaults = { rt };
			if(isConstructorType(c.store[c.fcvEnv[n]].rtype) || isProductionType(c.store[c.fcvEnv[n]].rtype)) {
				defaults += c.store[c.fcvEnv[n]].rtype; 
			} else {
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		nonDefaults += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
			}
			c.store[c.nextLoc] = overload({ c.fcvEnv[n], constructorItemId }, overloaded(nonDefaults,defaults));
			c.fcvEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		} else {
			c = addScopeWarning(c, "Invalid declaration: constructor <prettyPrintName(n)> clashes with an existing variable name in the same scope.", l);
		}
	}

	existsAlready = size({ i | i <- c.adtConstructors[adtId], c.store[i].at == l}) > 0;
	if (!existsAlready) {
		nameWithAdt = appendName(adtName,n);
		nameWithModule = appendName(moduleName,n);

		constructorItemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;

		overlaps = { i | i <- c.adtConstructors[adtId], c.store[i].name == n, comparable(c.store[i].rtype,rt), c.store[i].rtype != rt}; //, !equivalent(c.store[i].rtype,rt)};
		if (size(overlaps) > 0)
			c = addScopeError(c,"Constructor overlaps existing constructors in the same datatype : <constructorItemId>, <overlaps>",l);

		// NOTE: This will pick one if we have multiple types for the same name, but we will have already issued
		// a warning above...
		keywordParamMap = ( pn : pt | pn <- consolidatedParams<0>, pt := getOneFrom(consolidatedParams[pn]<0>) );
		
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

@doc{Add an imported constructor into the configuration}
public Configuration addImportedConstructor(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.fcvEnv && c.fcvEnv[n] == itemId) return c;

	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

	void addConstructorItem(RName n, int constructorItemId) {
		if (n notin c.fcvEnv) {
			c.fcvEnv[n] = constructorItemId;
		} else if (overload(items,overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
			c.store[c.fcvEnv[n]] = overload(items + constructorItemId, overloaded(itemTypes,defaults + c.store[constructorItemId].rtype));
		} else if (c.store[c.fcvEnv[n]] is constructor || c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is production) {
			nonDefaults = {};
			defaults = { c.store[constructorItemId].rtype };
			if(isConstructorType(c.store[c.fcvEnv[n]].rtype) || isProductionType(c.store[c.fcvEnv[n]].rtype)) {
				defaults += c.store[c.fcvEnv[n]].rtype; 
			} else {
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		nonDefaults += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
			}
			c.store[c.nextLoc] = overload({ c.fcvEnv[n], constructorItemId }, overloaded(nonDefaults,defaults));
			c.fcvEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		} else {
			c = addScopeWarning(c, "Invalid declaration: constructor <prettyPrintName(n)> clashes with an existing variable, function, or production name in the same scope.", c.store[itemId].at);
		}
	}

	addConstructorItem(n, itemId);
	if (n is RSimpleName && addFullName) {
		addConstructorItem(appendName(moduleName, n), itemId);
	}

	return c;
}

@doc{Add a production into the configuration.}
public Configuration addProduction(Configuration c, RName n, loc l, Production prod) {
	assert ( (prod.def is label && prod.def.symbol has name) || ( !(prod.def is label) && prod.def has name ) || prod.def is \start);
 
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);

	sortId = -1;
	sortName = RSimpleName("");

	if (unwrapType(prod.def) is \start) {
		sortName = RSimpleName("start[<getNonTerminalName(prod.def)>]");
		if (sortName in c.globalSortMap) {
			sortId = c.globalSortMap[sortName];
		} else {
			if (RSimpleName("") !:= n) {
				c = addScopeError(c, "Cannot add production <prettyPrintName(n)>, associated sort <prettyPrintName(sortName)> not found", l);
			} else {
				c = addScopeError(c, "Cannot add production <prettyPrintName(n)>, associated sort <prettyPrintName(sortName)> not found", l);
			}
		}
	} else {
		sortName = RSimpleName( (prod.def is label) ? prod.def.symbol.name : prod.def.name );
		if (sortName in c.globalSortMap) {
			sortId = c.globalSortMap[sortName];
		} else {
			if (RSimpleName("") !:= n) {
				c = addScopeError(c, "Cannot add production <prettyPrintName(n)>, associated sort <prettyPrintName(sortName)> not found", l);
			} else {
				c = addScopeError(c, "Cannot add production <prettyPrintName(n)>, associated sort <prettyPrintName(sortName)> not found", l);
			}
		}
	}   
	
	args = prod.symbols;
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
		} else if (c.store[c.fcvEnv[n]] is production || c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is constructor) {
			nonDefaults = {};
			defaults = { rtype };
			if(isProductionType(c.store[c.fcvEnv[n]].rtype) || isConstructorType(c.store[c.fcvEnv[n]].rtype)) {
				defaults += c.store[c.fcvEnv[n]].rtype; 
			} else {
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		nonDefaults += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
			}
			c.store[c.nextLoc] = overload({ c.fcvEnv[n], productionItemId }, overloaded(nonDefaults,defaults));
			c.fcvEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		} else {
			c = addScopeWarning(c, "Invalid declaration: production <prettyPrintName(n)> clashes with an existing variable name in the same scope.", l);
		}
	}

	Symbol removeAllLabels(Symbol st) { return top-down visit(st) { case label(_,sti) => sti }; }
	
	// We can have unnamed productions; in that case, we still add information into the store, but don't add anything
	// into the name environment, since we cannot look this up by name.
	existsAlready = size({ i | i <- c.nonterminalConstructors[sortId], c.store[i].at == l, c.store[i].name == n}) > 0;
	if (!existsAlready) {
		nameWithSort = appendName(sortName,n);
		nameWithModule = appendName(moduleName,n);

		productionItemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;

		if (RSimpleName("") != n) {
			// If the production is named, another production will overlap if it has the same name and a different type, including
			// labels -- it is only acceptable to repeat a production exactly
			overlaps = { i | i <- c.nonterminalConstructors[sortId], c.store[i].name == n, c.store[i].rtype != rtype}; 
			if (size(overlaps) > 0)
				c = addScopeError(c,"Production overlaps existing productions in the same nonterminal : <productionItemId>, <overlaps>",l);
		} else {
			// If the production isn't named, we have a slightly different rule: the productions don't need to match, but if
			// they match not accounting for labels, they have to match given labels -- so, the production can be different,
			// but if it has the same parts, they have to have the same names
			overlaps = { i | i <- c.nonterminalConstructors[sortId], c.store[i].name == n, removeAllLabels(c.store[i].rtype) == removeAllLabels(rtype), c.store[i].rtype != rtype};
			if (size(overlaps) > 0)
				c = addScopeError(c,"Production overlaps existing productions in the same nonterminal : <productionItemId>, <overlaps>",l);		
		}
		
		productionItem = production(n, rtype, head([i | i <- c.stack, \module(_,_) := c.store[i]]), prod, l);
		c.store[productionItemId] = productionItem;
		c.definitions = c.definitions + < productionItemId, l >;
		c.nonterminalConstructors = c.nonterminalConstructors + < sortId, productionItemId >;

		if (RSimpleName("") != n) {
			addProductionItem(n, productionItemId);
			addProductionItem(nameWithSort, productionItemId);
			addProductionItem(nameWithModule, productionItemId);
		}
	}    
	
	// Add non-terminal fields
	alreadySeen = {};
	for(\label(str fn, Symbol ft) <- prod.symbols) {
		if(fn notin alreadySeen) {
			if(c.nonterminalFields[<sortId,fn>]?) {
				t = c.nonterminalFields[<sortId,fn>];
				// TODO: respective functions, e.g., equivalent etc., need to be defined on non-terminal and regular symbols
				if(!equivalent( (Symbol::\conditional(_,_) := ft) ? ft.symbol : ft, (Symbol::\conditional(_,_) := t)  ? t.symbol  : t  )) {
					c = addScopeError(c,"Field <fn> already defined as type <prettyPrintType(t)> on non-terminal type <prettyPrintName(sortName)>, cannot redefine to type <prettyPrintType(ft)>",l);
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

@doc{Add an imported production into the configuration.}
public Configuration addImportedProduction(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.fcvEnv && c.fcvEnv[n] == itemId) return c;

	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;

	void addProductionItem(RName n, int productionItemId) {
		if (n notin c.fcvEnv) {
			c.fcvEnv[n] = productionItemId;
		} else if (overload(items,overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
			c.store[c.fcvEnv[n]] = overload(items + productionItemId, overloaded(itemTypes,defaults + c.store[productionItemId].rtype));
		} else if (c.store[c.fcvEnv[n]] is production || c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is constructor) {
			nonDefaults = {};
			defaults = { c.store[productionItemId].rtype };
			if(isProductionType(c.store[c.fcvEnv[n]].rtype) || isConstructorType(c.store[c.fcvEnv[n]].rtype)) {
				defaults += c.store[c.fcvEnv[n]].rtype; 
			} else {
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		nonDefaults += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
			}
			c.store[c.nextLoc] = overload({ c.fcvEnv[n], productionItemId }, overloaded(nonDefaults,defaults));
			c.fcvEnv[n] = c.nextLoc;
			c.nextLoc = c.nextLoc + 1;
		} else {
			c = addScopeWarning(c, "Invalid declaration: production <prettyPrintName(n)> clashes with an existing variable name in the same scope.", c.store[itemId].at);
		}
	}

	addProductionItem(n, itemId);
	if (n is RSimpleName && addFullName) {
		addProductionItem(appendName(moduleName, n), itemId);
	}    

	return c;
}

@doc{Add a syntax definition into the configuration.}
public Configuration addSyntaxDefinition(Configuration c, RName rn, loc l, Production prod, bool isStart) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
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

@doc{Add a module into the configuration.}
public Configuration addModule(Configuration c, RName n, loc l) {
    c.modEnv[n] = c.nextLoc;
    c.store[c.nextLoc] = \module(n,l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    return c;
}

@doc{Remove the current module from the top of the stack.}
public Configuration popModule(Configuration c) {
    if (\module(_,_) !:= c.store[head(c.stack)]) throw "Error, can only be called when a module is the top item on the stack";
    c.stack = tail(c.stack);
    return c;
}

@doc{Add a closure into the configuration.}
public Configuration addClosure(Configuration c, Symbol rt, KeywordParamMap keywordParams, loc l) {
    c.store[c.nextLoc] = closure(rt,keywordParams, head(c.stack),l);
    c.definitions = c.definitions + < c.nextLoc, l >;
    c.stack = c.nextLoc + c.stack;
    c.nextLoc = c.nextLoc + 1;
    c.expectedReturnType = getFunctionReturnType(rt);
    return c;
}

@doc{Add a function into the configuration.}
public Configuration addFunction(Configuration c, RName n, Symbol rt, KeywordParamMap keywordParams, set[Modifier] modifiers, bool isVarArgs, Vis visibility, list[Symbol] throwsTypes, loc l, bool isDeferred=false) {
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
	functionItem = function(n,rt,keywordParams,isVarArgs,head(c.stack),throwsTypes,isDeferred,l);
	functionId = c.nextLoc;
	existingDefs = invert(c.definitions)[l];
	if (!isEmpty(existingDefs)) {
		functionId = getOneFrom(existingDefs);
	} else {
		c.nextLoc = c.nextLoc + 1;
		c.store[functionId] = functionItem;
	    c.definitions = c.definitions + < functionId, l >;
	    c.visibilities[functionId] = visibility;
	    for(Modifier modifier <- modifiers) c.functionModifiers = c.functionModifiers + <functionId,modifier>;
	}

	// This actually links in the function item with a name in the proper manner. This is handled name by
	// name so we can keep separate overload sets for different versions of a name (if we qualify the name,
	// it should not refer to other names with different qualifiers).
	void addFunctionItem(RName n, int functionId) {	
	    if (n notin c.fcvEnv) {
	    	// Case 1: The name does not appear at all, so insert it and link it to the function item.
	        c.fcvEnv[n] = functionId;
		} else if (overload(items, overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
			// Case 2: The name is already overloaded, so link in the Id as one of the overloads.
			if (!isDeferred) {
				if(hasDefaultModifier(modifiers)) {
					defaults += rt;
				} else {
					itemTypes += rt;
				}
			}
			c.store[c.fcvEnv[n]] = overload(items + functionId, overloaded(itemTypes,defaults));
	    } else if (c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is constructor || c.store[c.fcvEnv[n]] is production) {
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
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		itemTypes += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
	        }
	        if (!isDeferred) {
		        if(hasDefaultModifier(modifiers)) {
		        	defaults += rt;
		        } else {
		        	itemTypes += rt;
		        }
			}
	        c.store[c.nextLoc] = overload({ c.fcvEnv[n], functionId }, overloaded(itemTypes,defaults));
			c.fcvEnv[n] = c.nextLoc;
	        c.nextLoc = c.nextLoc + 1;
	    } else {
	    	// Case 4: This function has the same name as a variable defined in the same module. We don't allow
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

@doc{Add an imported function into the configuration.}
public Configuration addImportedFunction(Configuration c, RName n, int itemId, bool addFullName=false) {
	if (n in c.fcvEnv && c.fcvEnv[n] == itemId) return c;

	void addFunctionItem(RName n, int functionId) {	
	    if (n notin c.fcvEnv) {
	        c.fcvEnv[n] = functionId;
	    } else if (overload(items, overloaded(set[Symbol] itemTypes, set[Symbol] defaults)) := c.store[c.fcvEnv[n]]) {
        	if (!c.store[functionId].isDeferred) {
		        if(hasDefaultModifier(c.functionModifiers[functionId])) {
		        	defaults += c.store[functionId].rtype;
		        } else {
		        	itemTypes += c.store[functionId].rtype;
		        }
	        }
	        c.store[c.fcvEnv[n]] = overload(items + functionId, overloaded(itemTypes,defaults));
	    } else if (c.store[c.fcvEnv[n]] is function || c.store[c.fcvEnv[n]] is constructor || c.store[c.fcvEnv[n]] is production) {
	        itemTypes = {};
	        defaults = {};
	        if(isConstructorType(c.store[c.fcvEnv[n]].rtype)) {
	        	defaults += c.store[c.fcvEnv[n]].rtype;
	        } else if (isProductionType(c.store[c.fcvEnv[n]].rtype)) {
	        	defaults += c.store[c.fcvEnv[n]].rtype;
	        } else {
	        	if (!c.store[c.fcvEnv[n]].isDeferred) {
		        	if(hasDefaultModifier(c.functionModifiers[c.fcvEnv[n]])) {
		        		defaults += c.store[c.fcvEnv[n]].rtype;
		        	} else {
		        		itemTypes += c.store[c.fcvEnv[n]].rtype;
		        	}
		        }
	        }
			if (!c.store[functionId].isDeferred) {
				if(hasDefaultModifier(c.functionModifiers[functionId])) {
					defaults += c.store[functionId].rtype;
				} else {
					itemTypes += c.store[functionId].rtype;
				}
			}
	        c.store[c.nextLoc] = overload({ c.fcvEnv[n], functionId }, overloaded(itemTypes,defaults));
			c.fcvEnv[n] = c.nextLoc;
	        c.nextLoc = c.nextLoc + 1;
	    } else {
	        c = addScopeError(c, "Cannot add function <prettyPrintName(n)>, a variable of the same name has already been defined in the current scope",c.store[itemId].at);
	    }
	}

	addFunctionItem(n, itemId);
    if (n is RSimpleName && addFullName) {
        moduleName = head([m | i <- c.stack, m:\module(_,_) := c.store[i]]).name;
        addFunctionItem(appendName(moduleName,n), itemId);
    }
	
    return c;
}

public Configuration addTag(Configuration c, TagKind tk, RName n, set[Symbol] onTypes, Vis visibility, loc l) {
    // TODO: We currently always treat datatype declarations as public, so we just
    // ignore the visibility here. If we decide to allow private datatype declarations,
    // revisit this.
    if (n in c.tagEnv) {
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

// TODO: If we ever use tags, add support for importing them...

public Configuration addMessage(Configuration c, Message m) = c[messages = c.messages + m];
public Configuration addScopeMessage(Configuration c, Message m) = c[messages = c.messages + m];

public Configuration addScopeError(Configuration c, str s, loc l) = addScopeMessage(c,error(s,l));
public Configuration addScopeWarning(Configuration c, str s, loc l) = addScopeMessage(c,warning(s,l));
public Configuration addScopeInfo(Configuration c, str s, loc l) = addScopeMessage(c,info(s,l));

@doc{Represents the result of checking an expression.}
alias CheckResult = tuple[Configuration conf, Symbol res];

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

@doc{Check if a set of function modifiers has the default modifier}
public bool hasDefaultModifier(set[Modifier] modifiers) = defaultModifier() in modifiers;
