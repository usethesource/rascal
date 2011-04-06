@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::Check

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;
import String;

import lang::rascal::checker::ListUtils;
import lang::rascal::types::Types;
import lang::rascal::types::SubTypes;
import lang::rascal::types::TypeSignatures;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::TypeRules;
import lang::rascal::scoping::ResolveNames;
import lang::rascal::checker::TreeUtils;

import lang::rascal::syntax::RascalRascal;

// TODO: the type checker should:
//   -- DONE: annotate expressions and statements with their type
//   -- DONE: infer types for untyped local variables
//   -- DONE: annotate patterns with their type
//   -- check type consistency for:
//           -- DONE: function calls
//           -- DONE: case patterns (i.e. for switch it should be the same as the expression that is switched on)
//           -- DONE: case rules (left-hand side equal to right-hand side
//           -- DONE: rewrite rules (idem)
//   -- filter ambiguous Rascal due to concrete syntax, by:
//          -- type-checking each alternative and filtering type incorrect ones
//          -- counting the size of concrete fragments and minimizing the number of characters in concrete fragments
//          -- comparing the chain rules at the top of fragments, and minimizing those
//          -- balancing left and right-hand side of rules and patterns with replacement by type equality
//   -- check additional static constraints
//          -- DONE: across ||, --> and <--> conditional composition, all pattern matches introduce the same set of variables 
//          -- PARTIAL: all variables have been both declared and initialized in all control flow paths UPDATE: currently
//			   checks to ensure declared, not yet checking to ensure initialized
//          -- switch either implements a default case, or deals with all declared alternatives
//
// More TODOs
//
// 1. [DONE: YES] Do we want to go back and assign the inferred types to names?
//
// 2. For statement types and (in general) blocks, how should we handle types assigned to the blocks? Currently, if
//     you have x = block, and block throws, x is undefined. If we want to continue allowing this we need to decide
//     on a type for x that is safe (i.e., would int x = 3; x = { throw "Help!"; 5; } be a type error or not?
//
// 3. Add solve for the reducer to determine the type -- uses a conservative value now, but can make this more precise
//
// 4. [DONE] Handle polymorphic type variables
//
// 5. Need to set up a large number of test cases!
//
// 6. Add checking for tags
//
// 7. Handle embedded TODOs below and in other files; these are generally less common type checking cases that we
//    still need to handle.
//
// 8. Do we allow interpolation in pattern strings? If so, what does this mean?

private str getTypeString(Name n) {
	if ( hasRType(globalSTBuilder, n@\loc) )
		return "TYPE: " + prettyPrintType(getTypeForName(globalSTBuilder, convertName(n), n@\loc));
	else
		return "TYPE unavailable";
}
 
public Name setUpName(Name n) {
	if ( hasRType(globalSTBuilder, n@\loc) ) {
		n = n[@rtype = getTypeForName(globalSTBuilder, convertName(n), n@\loc)]; 
	}
	n = n[@doc = getTypeString(n)];
	return n;
}

//
// Annotate the nodes of a parse tree with the calculated types.
// 
// NOTE: The extra code for expressions is to bind "it" to a type, based on the type of
// the initializing expression. "it" is created as an inferrence variable with scope just
// inside the middle part (the reduction) of the reducer expression.
//
public Tree updateOverloadedCall(Tree t, RType ty) {
	// TODO: Handle qualified names as well	
	Tree updateCallName(Tree t, RType ty) {
		return top-down-break visit(t) {
			case  `<Name n>` : {
				if ( (ty@at)? )
					insert n[@rtype = ty][@link = ty@at];
				else
					insert n[@rtype = ty]; 
			}
		};
	}
	
	if ((Expression) `<Name n1> ( <{Expression ","}* el> )` := t) return updateCallName(t,ty);
	return t;
}

public Tree check(Tree t) {
	return visit(t) {
		case (Expression)`<Expression e>` : {
			// Now, check the expression, using the updated value above
			RType expType = checkExpression(e); 

			if ((Expression)`<Expression e1> ( <{Expression ","}* el> )` := e && !isFailType(expType)) {
				e = updateOverloadedCall(e,expType);
			}

			// Tag the type of it expressions
			if (e@\loc in globalSTBuilder.itBinder) 
				updateInferredTypeMappings(globalSTBuilder.itBinder[e@\loc], expType);

			// Handle types for functions and constructors, which are the function or constructor
			// type; we need to extract the return/result type and save the function or constructor
			// type for later use
			if ((Expression)`<Expression e1bbb> ( <{Expression ","}* elbbb> )` := e) {
				if (isConstructorType(expType)) {
				    println("Found a constructor on call or tree, type <expType>");
					insert e[@rtype = getConstructorResultType(expType)][@fctype = expType];
				} else if (isFunctionType(expType)) { 
                    println("Found a function on call or tree, type <expType>");
					insert e[@rtype = getFunctionReturnType(expType)][@fctype = expType];
				} else {
                    println("Found something else on call or tree, type <expType>");
					insert e[@rtype = expType]; // Probably a failure type
				}
			} else {
				insert e[@rtype = expType];
			} 
		}
		case (Pattern)`<Pattern p>` : {
			RType patType = checkPattern(p);

			if ((Pattern)`<Pattern p1> ( <{Pattern ","}* pl> )` := p && !isFailType(patType)) {
				p = updateOverloadedCall(p,patType);
			}

			if ((Pattern)`<Pattern p1> ( <{Pattern ","}* pl> )` := p) {
				if (isConstructorType(patType)) {
					insert p[@rtype = getConstructorResultType(patType)][@fctype = patType];
				} else {
					insert p[@rtype = patType]; // Probably a failure type
				}
			} else {
				insert(p[@rtype = patType]);
			}
		}
		case (Statement)`<Statement s>` => s[@rtype = checkStatement(s)]
		case (Assignable)`<Assignable a>` => a[@rtype = checkAssignable(a)]
		case (Catch)`<Catch c>` => c[@rtype = checkCatch(c)]
		case (DataTarget)`<DataTarget dt>` => dt[@rtype = checkDataTarget(dt)]
		case (Target)`<Target t>` => t[@rtype = checkTarget(t)]
		case (PatternWithAction)`<PatternWithAction pwa>` => pwa[@rtype = checkPatternWithAction(pwa)]
		case (Visit)`<Visit v>` => v[@rtype = checkVisit(v)]
		case (Label)`<Label l>` => l[@rtype = checkLabel(l)]
		case (Variable)`<Variable v>` => v[@rtype = checkVariable(v)]
		case (FunctionBody)`<FunctionBody fb>` => fb[@rtype = checkFunctionBody(fb)]
		case (Toplevel)`<Toplevel t>` => t[@rtype = checkToplevel(t)]
		case (Body)`<Body b>` => b[@rtype = checkModuleBody(b)]
		case (Module)`<Module m>` => m[@rtype = checkModule(m)]
		case (Case)`<Case c>` => c[@rtype = checkCase(c)]
		case (StringTemplate)`<StringTemplate s>` => s[@rtype = checkStringTemplate(s)]
	} 
}

//
// Check the names in the tree, "fixing" any inferred types with the
// type calculated during type checking.
//
public Tree retagNames(Tree t) {
	return visit(t) {
		case (Expression)`<Name n>` : { 
			insert(setUpName(n));
		}
	} 
}

private bool debug = true;

private set[RType] gatherFailTypes(set[RType] checkTypes) {
	return { ct | ct <- checkTypes, isFailType(ct) };
}

private bool checkForFail(set[RType] checkTypes) {
	return size(gatherFailTypes(checkTypes)) > 0;
}

private RType propagateFailOr(set[RType] checkTypes, RType newType) {
	set[RType] ts = gatherFailTypes(checkTypes);
	if (size(ts) > 0) 
		return collapseFailTypes(ts);
	else
		return newType;
}

//
// To check a module, we propagate up any errors in the module body, as well as adding any errors
// determined during the building of the symbol table.
//
public RType checkModule(Module m) {
	if ((Module) `<Header h> <Body b>` := m) {
		set[str] scopeErrors = { m | m:error(_,_) <- stBuilder.messages[m@\loc] };
	 	if (size(scopeErrors) > 0) {
	 		return collapseFailTypes({ makeFailType(s,m@\loc) | s <- scopeErrors } + b@rtype);
	 	} else {
	 		return b@rtype;
	 	}
	}
	throw "checkModule: unexpected module syntax";
}

//
// Since checking is a bottom-up process, checking the module body just consists of propagating 
// any errors that have occured inside the module body up.
//
public RType checkModuleBody(Body b) {
	set[RType] modItemTypes = { };
	if ((Body)`<Toplevel* ts>` := b) modItemTypes = { t@rtype | t <- ts, ( (t@rtype)?) };
	if (size(modItemTypes) > 0 && checkForFail(modItemTypes)) return collapseFailTypes(modItemTypes);
	return makeVoidType();
}

//
// Checking the toplevel items involves propagating up any failures detected in the items.
//
public RType checkToplevel(Toplevel t) {
	switch(t) {
		// Variable declaration
		case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
			return checkVarItems(tgs, v, typ, vs);
		}

		// Abstract (i.e., without a body) function declaration
		case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
			return checkAbstractFunction(tgs, v, s);
		}
 
		// Concrete (i.e., with a body) function declaration
		case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
			return checkFunction(tgs, v, s, fb);
		}
			
		// Annotation declaration
		case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
			return checkAnnotationDeclaration(tgs, v, typ, otyp, n);
		}
								
		// Tag declaration
		case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
			return checkTagDeclaration(tgs, v, k, n, typs);
		}
			
		// Rule declaration
		case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
			return checkRuleDeclaration(tgs, n, pwa);
		}
			
		// Test
		case (Toplevel) `<Test tst> ;` : {
			return checkTestDeclaration(tst);
		}
							
		// ADT without variants
		case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
			return checkAbstractADT(tgs, v, typ);
		}
			
		// ADT with variants
		case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
			return checkADT(tgs, v, typ, vars);
		}

		// Alias
		case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
			return checkAlias(tgs, v, typ, btyp);
		}
							
		// View
		case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
			return checkView(tgs, v, n, sn, alts);
		}
	}
	throw "checkToplevel: Unhandled toplevel item <t>";
}

//
// checkVarItems checks for failure types assigned to the variables, either returning
// these or a void type. Failures would come from duplicate use of a variable name
// or other possible scoping errors as well as from errors in the init expression.
//
public RType checkVarItems(Tags ts, Visibility vis, Type t, {Variable ","}+ vs) {
    set[RType] varTypes = { v@rtype | v <- vs };
    if (checkForFail( varTypes )) return collapseFailTypes( varTypes );
    return makeVoidType();
}

//
// checkVariable checks the correctness of the assignment where the variable is of the
// form n = e and also returns either a failure type of the type assigned to the name.
//
public RType checkVariable(Variable v) {
    switch(v) {
        case (Variable) `<Name n>` : {
            return getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        }
        
        case (Variable) `<Name n> = <Expression e>` : {
                // NOTE: The only variable declarations are typed variable declarations. Declarations
            // of the form x = 5 are assignables. So, here we want to make sure the assignment
            // doesn't cause a failure, but beyond that we just return the type of the name,
            // which should be the same as, or a supertype of, the expression.
            RType nType = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
            if (checkForFail( { nType, e@rtype })) return collapseFailTypes({ nType, e@rtype });
            if (subtypeOf(e@rtype, nType)) return nType;
            return makeFailType("Type of <e>, <prettyPrintType(e@rtype)>, must be a subtype of the type of <n>, <prettyPrintType(nType)>", v@\loc);
        }
    }
    throw "checkVariable: unhandled variable case <v>";
}

//
// The type of a function is fail if the parameters have fail types, else it is based on the
// return and parameter types assigned to function name n.
//
// TODO: Add checking of throws, if needed (for instance, to make sure type names exist -- this
// may already be done in Namespace when building the symbol table)
//
public RType checkAbstractFunction(Tags ts, Visibility v, Signature s) {
    switch(s) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
            return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
            return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    }
    throw "checkAbstractFunction: unhandled signature <s>";
}

//
// The type of a function is fail if the body or parameters have fail types, else it is
// based on the return and parameter types (and is already assigned to n, the function name).
//
// TODO: Add checking of throws, if needed (for instance, to make sure type names exist -- this
// may already be done in Namespace when building the symbol table)
//
public RType checkFunction(Tags ts, Visibility v, Signature s, FunctionBody b) {
    switch(s) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
            return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
            return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    }
    throw "checkFunction: unhandled signature <s>";
}

//
// The type of the function body is a failure if any of the statements is a failure type,
// else it is just void. Function bodies don't have types based on the computed results.
//
public RType checkFunctionBody(FunctionBody fb) {
    if ((FunctionBody)`{ <Statement* ss> }` := fb) {
        set[RType] bodyTypes = { getInternalStatementType(s@rtype) | s <- ss };
        if (checkForFail(bodyTypes)) return collapseFailTypes(bodyTypes);
        return makeVoidType();
    }
    throw "checkFunctionBody: Unexpected syntax for function body <fb>";
}

//
// If the name has a type annotation, return it, else just return a void type. A type
// on the name would most likely indicate a scope error.
//
public RType checkAnnotationDeclaration(Tags t, Visibility v, Type t, Type ot, Name n) {
    if ( hasRType(globalSTBuilder,n@\loc) ) return getTypeForName(globalSTBuilder, convertName(n), n@\loc); else return makeVoidType();
}

//
// If the name has a type annotation, return it, else just return a void type. A type
// on the name would most likely indicate a scope error.
//
public RType checkTagDeclaration(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts) {
    if ( hasRType(globalSTBuilder,n@\loc) ) return getTypeForName(globalSTBuilder, convertName(n), n@\loc); else return makeVoidType();
}
    
//
// The type of the rule is the failure type on the name of pattern if either is a
// failure type, else it is the type of the pattern (i.e., the type of the term
// rewritten by the rule).
//                          
public RType checkRuleDeclaration(Tags t, Name n, PatternWithAction p) {
    if ( hasRType(globalSTBuilder, n@\loc )) {
        if (checkForFail({ getTypeForName(globalSTBuilder, convertName(n), n@\loc), p@rtype })) 
                        return collapseFailTypes({getTypeForName(globalSTBuilder, convertName(n), n@\loc), p@rtype});
    } 
    return p@rtype;
}

//
// The type of the test is either a failure type, if the expression has a failure type, or void.
//
public RType checkTestDeclaration(Test t) {
        if ((Test)`<Tags tgs> test <Expression exp>` := t || (Test)`<Tags tgs> test <Expression exp> : <StringLiteral sl>` := t) {
            if (isFailType(exp@rtype)) return exp@rtype; else return makeVoidType();
        }
        throw "Unexpected syntax for test: <t>";
}

//
// The only possible error is on the ADT name itself, so check that for failures.
//
public RType checkAbstractADT(Tags ts, Visibility v, UserType adtType) {
    Name adtn = getUserTypeRawName(adtType);
    if (hasRType(globalSTBuilder, adtn@\loc))
        return getTypeForName(globalSTBuilder, convertName(adtn), adtn@\loc);
    return makeVoidType();
}

//
// Propagate upwards any errors registered on the ADT name or on the variants.
//
public RType checkADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars) {
    set[RType] adtTypes = { };

    Name adtn = getUserTypeRawName(adtType);
    if (hasRType(globalSTBuilder,adtn@\loc))
        adtTypes = adtTypes + getTypeForName(globalSTBuilder, convertName(adtn), adtn@\loc);

    for ((Variant)`<Name n> ( <{TypeArg ","}* args> )` <- vars) {
        if (hasRType(globalSTBuilder, n@\loc))
            adtTypes = adtTypes + getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    }

    if (checkForFail(adtTypes)) return collapseFailTypes(adtTypes);
    return makeVoidType();
}

//
// Return any type registered on the alias name, else return a void type. Types on the name
// most likely represent a scoping error.
//
public RType checkAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType) {
    Name aliasRawName = getUserTypeRawName(aliasType);
    if (hasRType(globalSTBuilder, aliasRawName@\loc)) {
        return getTypeForName(globalSTBuilder, convertName(aliasRawName), aliasRawName@\loc);
    }
    return makeVoidType();
}

//
// TODO: Implement once views are available in Rascal
//
public STBuilder checkView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts) {
    throw "checkView not yet implemented";
}

//
// START OF STATEMENT CHECKING LOGIC
//
public RType checkSolveStatement(Statement sp, {QualifiedName ","}+ vars, Bound b, Statement body) {
    RType boundType = makeIntType();
    if ((Bound)`; <Expression e>` := b) boundType = e@rtype;

    RType bodyType = getInternalStatementType(body@rtype);

    if (checkForFail( { getTypeForName(globalSTBuilder,convertName(v),v@\loc) | v <- vars } + boundType + bodyType))
        return makeStatementType(collapseFailTypes({ getTypeForName(globalSTBuilder,convertName(v),v@\loc) | v <- vars } + boundType + bodyType));

    if (! isIntType(boundType))
                return makeStatementType(makeFailType("The bound type must be type <prettyPrintType(makeIntType())>, not type <prettyPrintType(boundType)>",b@\loc));

    return body@rtype;
}

//
// TODO: This would be a good target for static analysis to come up with a better
// type. The only safe type is list[void], since we don't know if the loop will
// actually execute. This applies to the while loop as well.
//
public RType checkForStatement(Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    set[Expression] expSet = { e | e <- exps };
    RType bodyType = getInternalStatementType(body@rtype);

    if (checkForFail( { e@rtype | e <-expSet } + l@rtype + bodyType))
        return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + bodyType));

    set[RType] failures = { };
    for (e <- exps) {
            if (!isBoolType(e@rtype)) {
                failures += makeFailType("Expression <e> should be of type bool, not <prettyPrintType(e@rtype)>",e@\loc);
        }
    }
    if (size(failures) > 0) return makeStatementType(collapseFailTypes(failures));

    return makeStatementType(makeListType(makeVoidType()));  
}  

//
// TODO: See checkForStatement above.
//
public RType checkWhileStatement(Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    set[Expression] expSet = { e | e <- exps };
    RType bodyType = getInternalStatementType(body@rtype);

    if (checkForFail( { e@rtype | e <-expSet } + l@rtype + bodyType))
        return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + bodyType));

    set[RType] failures = { };
    for (e <- exps) {
            if (!isBoolType(e@rtype)) {
                failures += makeFailType("Expression <e> should be of type bool, not <prettyPrintType(e@rtype)>",e@\loc);
        }
    }
    if (size(failures) > 0) return makeStatementType(collapseFailTypes(failures));

    return makeStatementType(makeListType(makeVoidType()));  
}

//
// TODO: Setting to list[void] for now. If we have an append inside the body that is executed
// on every path we can set it to list[value] instead, which would also be safe. It may be good 
// to analyze the loop body to find a more accurate type.
//
public RType checkDoWhileStatement(Statement sp, Label l, Statement body, Expression e) {
    RType bodyType = getInternalStatementType(body@rtype);

    if (checkForFail( { l@rtype, bodyType, e@rtype }))
        return makeStatementType(collapseFailTypes({ l@rtype, bodyType, e@rtype }));

    if (!isBoolType(e@rtype))
            return makeStatementType(makeFailType("Type of expression <e> should be bool, not <prettyPrintType(e@rtype)>",e@\loc));

    return makeStatementType(makeListType(makeVoidType()));  
}

//
// Typecheck an if/then/else statement. The result type is the lub of the types of each 
// branch (since at least one of the branches will have executed)
//
public RType checkIfThenElseStatement(Statement s, Label l, {Expression ","}+ exps, Statement trueBody, Statement falseBody) {
    set[Expression] expSet = { e | e <- exps };
    RType trueBodyType = getInternalStatementType(trueBody@rtype);
    RType falseBodyType = getInternalStatementType(falseBody@rtype);
    if (checkForFail( { e@rtype | e <-expSet } + l@rtype + trueBodyType + falseBodyType))
        return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + trueBodyType + falseBodyType));

    set[RType] failures = { };
    for (e <- exps) {
            if (!isBoolType(e@rtype)) {
                failures += makeFailType("Expression <e> should be of type bool, not <prettyPrintType(e@rtype)>",e@\loc);
        }
    }
    if (size(failures) > 0) return makeStatementType(collapseFailTypes(failures));

    return makeStatementType(lub(trueBodyType, falseBodyType));
}

//
// Typecheck an if/then statement without an else. The result type is always void (since the body
// of the if may not execute).
//
public RType checkIfThenStatement(Statement s, Label l, {Expression ","}+ exps, Statement trueBody) {
    set[Expression] expSet = { e | e <- exps };
    RType trueBodyType = getInternalStatementType(trueBody@rtype);
    if (checkForFail( { e@rtype | e <-expSet } + l@rtype + trueBodyType))
        return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + trueBodyType));

    set[RType] failures = { };
    for (e <- exps) {
            if (!isBoolType(e@rtype)) {
                failures += makeFailType("Expression <e> should be of type bool, not <prettyPrintType(e@rtype)>",e@\loc);
        }
    }
    if (size(failures) > 0) return makeStatementType(collapseFailTypes(failures));

    return makeStatementType(makeVoidType());
}

//
// Calculate the type of a switch statement and check for failures. The result type of the switch is the
// lub of the types of the various cases.
//
// TODO: How should subtypes factor in here?
//
// TODO: Would it make sense to distinguish between warnings and errors? For instance, here we do the
// coverage check to see if we can return the lub type. The coverage check is conservative, so we may
// actually cover all cases but not be able to detect that here. We should have a warning in these
// cases, to let the user know that they may not have full coverage, but a type failure seems to be
// too severe.
//
public RType checkSwitchStatement (Statement sp, Label l, Expression e, Case+ cases) {
    set[RType] caseTypes = { c@rtype | c <- cases };
    if (checkForFail( caseTypes + e@rtype + l@rtype))
        return makeStatementType(collapseFailTypes(caseTypes + e@rtype + l@rtype));

    // Check to ensure that all the case pattern types are the same as or supertypes of the switch expression type
    set[RType] caseFailures = { };
    for (cs <- cases) {
        if (! isDefaultCase(cs)) {
            RType casePatternType = getCasePatternType(cs);
            if (! subtypeOf(e@rtype,casePatternType)) 
                caseFailures += makeFailType("Type of case pattern, <prettyPrintType(casePatternType)>, must match type of switch expression, <prettyPrintType(e@rtype)>", cs@\loc);
        }
    }
    
    if (size(caseFailures) == 0) {
            // TODO: Add this back in. This needs to be improved first.
        //if (checkCaseCoverage(e@rtype, cases, globalSTBuilder)) {           
            return makeStatementType(makeVoidType());
        //} else {
            //return makeStatementType(makeFailType("The switch does not include a default case and the cases may not cover all possibilities.", sp@\loc));
        //}
    } else {
        return makeStatementType(collapseFailTypes(caseFailures));
    }
} 

//
// Is this case the default case?
//
public bool isDefaultCase(Case c) {
    return (Case)`default : <Statement b>` := c;
}

//
// Given a non-default case, get the type assigned to the case pattern P, with P => or P :
//
public RType getCasePatternType(Case c) {
    switch(c) {
        case (Case)`case <Pattern p> => <Expression e>` : return p@rtype;
        case (Case)`case <Pattern p> => <Expression er> when <{Expression ","}+ es>` : return p@rtype;
        case (Case)`case <Pattern p> : <Statement s>` : return p@rtype;
        case (Case)`default : <Statement b>` : throw "getCaseExpType should not be called on default case";
    }    
}

//
// The type of the visit is calculated elsewhere; the statement simply returns that
// type unless the label is a fail type (which can happen when the same label name
// is reused).
//
public RType checkVisitStatement(Statement sp, Label l, Visit v) {
    if (checkForFail({ l@rtype, v@rtype}))
        return makeStatementType(collapseFailTypes({ l@rtype, v@rtype }));
    return makeStatementType(v@rtype);
}           

//
// Type checks the various cases for assignment statements. Note that this assumes that the
// assignable and the statement have themselves already been checked and assigned types.
//
public RType checkAssignmentStatement(Statement sp, Assignable a, Assignment op, Statement s) {
    RType stmtType = getInternalStatementType(s@rtype);

    if (checkForFail({ a@rtype, stmtType })) {
        return makeStatementType(collapseFailTypes({ a@rtype, getInternalStatementType(s@rtype) }));
    }

    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);

    // Special case: name += value, where name is a list/set/map of void and value is a list/set/map or
    // an element.
    // TODO: This is a terrible hack, and needs to be removed, but at least it lets things pass the checker
    // that otherwise would generate noise (or require explicit type declarations).
    if (isListType(wholeType) && isVoidType(getListElementType(wholeType)) && RAAddition() := convertAssignmentOp(op)) {
            if (isListType(stmtType)) {
                return makeStatementType(bindInferredTypesToAssignable(stmtType, a));
        } else {
                return makeStatementType(bindInferredTypesToAssignable(makeListType(stmtType), a));
        }
    } else if (isSetType(wholeType) && isVoidType(getSetElementType(wholeType)) && RAAddition() := convertAssignmentOp(op)) {
            if (isSetType(stmtType)) {
                return makeStatementType(bindInferredTypesToAssignable(stmtType, a));
        } else {
                return makeStatementType(bindInferredTypesToAssignable(makeSetType(stmtType), a));
        }
    } else if (isMapType(wholeType) && isVoidType(getMapDomainType(wholeType)) && isVoidType(getMapRangeType(wholeType)) && RAAddition() := convertAssignmentOp(op)) {
            if (isMapType(stmtType)) {
                return makeStatementType(bindInferredTypesToAssignable(stmtType, a));
        }
    }

    // This works over two cases. For both = and ?=, the variable(s) on the left can be inference vars.
    // Otherwise, they cannot, since we are doing some kind of calculation using them and, therefore,
    // they must have been initialized (and assigned types) earlier.
    if (!aOpHasOp(convertAssignmentOp(op))) {
            return makeStatementType(bindInferredTypesToAssignable(stmtType, a));
    } else {
        RType actualAssignedType = getAssignmentType(partType, stmtType, convertAssignmentOp(op), sp@\loc);
        if (isFailType(actualAssignedType)) {
            return makeStatementType(actualAssignedType);
        } else {
            if (subtypeOf(actualAssignedType, partType)) {
                return makeStatementType(wholeType);
            } else {
                return makeStatementType(makeFailType("Invalid assignment, the type being assigned, <prettyPrintType(stmtType)>, must be a subtype of the type being assigned into, <prettyPrintType(partType)>",sp@\loc));
            }
        }
    }
}

//
// An assert without a message should have expression type bool.
//
public RType checkAssertStatement(Statement sp, Expression e) {
    if (isBoolType(e@rtype)) {
        return makeStatementType(makeVoidType());
    } else if (isFailType(e@rtype)) {
        return makeStatementType(e@rtype);
    } else {
        return makeStatementType(makeFailType("Expression in assert should have type bool, but instead has type <prettyPrintType(e@rtype)>", sp@\loc));
    }
}

//
// An assert with a message should have expression types bool : str .
//
public RType checkAssertWithMessageStatement(Statement sp, Expression e, Expression em) {
        RType eType = e@rtype;
        RType emType = em@rtype;

    if (isBoolType(eType) && isStrType(emType)) {
        return makeStatementType(makeVoidType());
    } else if (checkForFail({e@rtype, em@rtype})) {
        return makeStatementType(collapseFailTypes({e@rtype,em@rtype}));
    } else if (isBoolType(eType) && !isStrType(emType)) {
        return makeStatementType(makeFailType("Right-hand expression in assert should have type str, but instead has type <prettyPrintType(em@rtype)>", sp@\loc));
    } else if (!isBoolType(eType) && isStrType(emType)) {
        return makeStatementType(makeFailType("Left-hand expression in assert should have type bool, but instead has type <prettyPrintType(e@rtype)>", sp@\loc));
    } else {
        return makeStatementType(makeFailType("Assert should have types bool : str, but instead has types <prettyPrintType(e@rtype)> : <prettyPrintType(em@rtype)>", sp@\loc));
    }
}

//
// Checking the return statement requires checking to ensure that the returned type is the same
// as the type of the function. We could do that at the function level, using a visitor (like we
// do to check part of the visit), but do it this way instead since it should be faster, at
// the expense of maintaining additional data structures.
//
public RType checkReturnStatement(Statement sp, Statement b) {
    RType stmtType = getInternalStatementType(b@rtype);
    RType retType = getFunctionReturnType(globalSTBuilder.returnTypeMap[sp@\loc]);
    if (isFailType(stmtType)) {
        return makeStatementType(stmtType);
    } else if (subtypeOf(stmtType,retType)) {
        return makeStatementType(retType);
    } else {
        return makeStatementType(makeFailType("Type of return, <prettyPrintType(stmtType)>, must be a subtype of function return type, <prettyPrintType(retType)>", sp@\loc));
    } 
}

//
// Checking a local function statement just involves propagating any failures or, if there are no failures,
// returning the type already assigned (in the scope generation) to the name.
//
public RType checkLocalFunctionStatement(Statement sp, Tags ts, Visibility v, Signature sig, FunctionBody fb) {
    switch(sig) {
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
            return checkForFail(toSet(getParameterTypes(ps)) + fb@rtype) ? makeStatementType(collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype)) : makeStatementType(getTypeForName(globalSTBuilder,convertName(n),n@\loc));
        case (Signature)`<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
            return checkForFail(toSet(getParameterTypes(ps)) + fb@rtype) ? makeStatementType(collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype)) : makeStatementType(getTypeForName(globalSTBuilder,convertName(n),n@\loc));
    }
    throw "checkFunction: unhandled signature <sig>";
}

//
// Typecheck a try/catch statement. Currently the behavior of the interpreter returns the value of the body if 
// the body exits correctly, or an undefined value if a throw occurs in the body. For now, type this as void,
// but TODO: check to see if we want to maintain this behavior.
//
public RType checkTryCatchStatement(Statement sp, Statement body, Catch+ catches) {
        set[Catch] catchSet = { c | c <- catches };
        set[RType] catchTypes = { getInternalStatementType(c@rtype) | c <- catchSet };
        RType bodyType = getInternalStatementType(body@rtype);
        if (checkForFail( catchTypes + bodyType))
            return makeStatementType(collapseFailTypes( catchTypes + bodyType ));
        return makeStatementType(makeVoidType());
}       

//
// Typecheck a try/catch/finally statement. See the comments for the try/catch statement for added details.
//
public RType checkTryCatchFinallyStatement(Statement sp, Statement body, Catch+ catches, Statement fBody) {
        set[Catch] catchSet = { c | c <- catches };
        set[RType] catchTypes = { getInternalStatementType(c@rtype) | c <- catchSet };
        RType bodyType = getInternalStatementType(body@rtype);
        RType finallyType = getInternalStatementType(fBody@rtype);
        if (checkForFail( catchTypes + bodyType + finallyType ))
            return makeStatementType(collapseFailTypes( catchTypes + bodyType + finallyType ));
        return makeStatementType(makeVoidType());
}       

// Type check a block of statements. The result is either statement type containing a failure type, in cases where
// the label or one of the statements is a failure, or a statement type containing the internal type of the last
// statement in the block. For instance, if the last statement in the block is 3; the block would have type int.
public RType checkBlockStatement(Statement sp, Label l, Statement+ bs) {
    list[RType] statementTypes = [ getInternalStatementType(b@rtype) | b <- bs];

    if (checkForFail({ l@rtype} +  toSet(statementTypes) ))
        return makeStatementType(collapseFailTypes({ l@rtype } + toSet(statementTypes) ));
    
    return makeStatementType(head(tail(statementTypes,1)));
} 

// Typecheck all statements. This is a large switch/case over all the statement syntax, calling out to smaller functions
// where needed. The resulting type is an RStatementType containing the type of the computation
public RType checkStatement(Statement s) {
    switch(s) {
        case (Statement)`solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
            return checkSolveStatement(s,vs,b,sb);
        }

        case (Statement)`<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
            return checkForStatement(s,l,es,b);
        }

        case (Statement)`<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
            return checkWhileStatement(s,l,es,b);
        }
        
        case (Statement)`<Label l> do <Statement b> while (<Expression e>);` : {
            return checkDoWhileStatement(s,l,b,e);
        }

        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
            return checkIfThenElseStatement(s,l,es,bt,bf);
        }

        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> <NoElseMayFollow _>` : {
            return checkIfThenStatement(s,l,es,bt);
        }

        case (Statement)`<Label l> switch (<Expression e>) { <Case+ cs> }` : {
            return checkSwitchStatement(s,l,e,cs);
        }

        case (Statement)`<Label l> <Visit v>` : {
            return checkVisitStatement(s,l,v);
        }

        case (Statement)`<Expression e> ;` : {
                return makeStatementType(e@rtype);
        }

        case (Statement)`<Assignable a> <Assignment op> <Statement b>` : {
            return checkAssignmentStatement(s,a,op,b);
        }
        
        case (Statement)`assert <Expression e> ;` : {
            return checkAssertStatement(s, e);
        }

        case (Statement)`assert <Expression e> : <Expression em> ;` : {
            return checkAssertWithMessageStatement(s, e, em);
        }
        
        case (Statement)`return <Statement b>` : {
            return checkReturnStatement(s, b);
        }
        
        // TODO: Need to add RuntimeException to a default "type store" so we can use it
        // TODO: Modify to properly check the type of b; should be a subtype of RuntimeException
        case (Statement)`throw <Statement b>` : {
            RType rt = b@rtype;
            return rt;
        }

        // TODO: Need to verify that statement has same type as current subject in visit or rewrite rule
        case (Statement)`insert <DataTarget dt> <Statement b>` : {
            RType st = getInternalStatementType(b@rtype);
            RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
            return rt;
        }
        
        case (Statement)`append <DataTarget dt> <Statement b>` : {
            RType st = getInternalStatementType(b@rtype);
            RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
            return rt;
        }
        
        case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
            return checkLocalFunctionStatement(s,ts,v,sig,fb);
        }
        
        case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
            return checkLocalVarItems(s, vs);
        }
        
        // TODO: Handle the dynamic part of dynamic vars        
        case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
            return checkLocalVarItems(s, vs);
        }
        
        case (Statement)`break <Target t> ;` : {
            return (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
        }
        
        case (Statement)`fail <Target t> ;` : {
            return  (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
        }
        
        case (Statement)`continue <Target t> ;` : {
            return (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
        }
        
        case (Statement)`try <Statement b> <Catch+ cs>` : {
            return checkTryCatchStatement(s,b,cs);
        }

        case (Statement)`try <Statement b> <Catch+ cs> finally <Statement bf>` : {
            return checkTryCatchFinallyStatement(s,b,cs,bf);
        }
        
        case (Statement)`<Label l> { <Statement+ bs> }` : {
            return checkBlockStatement(s, l, bs);
        }
        
        case (Statement)`;` : {
            return makeStatementType(makeVoidType());
        }
    }
    
    throw "Unhandled type checking case in checkStatement for statement <s>";
}

//
// TODO: The expressions should all be of type type
//
private RType checkReifiedTypeExpression(Expression ep, Type t, {Expression ","}* el) {
    if (checkForFail({ e@rtype | e <- el }))
        return collapseFailTypes({ e@rtype | e <- el });
    else
        return makeReifiedType(convertType(t), [ e@rtype | e <- el ]);
}

//
// TODO: May need to handle source location "calls" here as well (based on the code
// in the SourceLocation result class)
//
public RType checkCallOrTreeExpression(Expression ep, Expression ec, {Expression ","}* es) {
    list[RType] matches = getCallOrTreeExpressionType(ep, ec, es);
    if (size(matches) > 1) { 
        return makeFailType("There are multiple possible matches for this function or constructor expression. Please add additional type information. Matches: <prettyPrintTypeListWLoc(matches)>",ep@\loc);
    } else if (size(matches) == 1 && rt := head(matches) && isFailType(rt)) {
        return rt;
    } else if (size(matches) == 1 && rt := head(matches) && isConstructorType(rt)) {
        return rt;
    } else if (size(matches) == 1 && rt := head(matches) && isFunctionType(rt)) {
        return rt;
    } else {
        throw "Unexpected situation, checkCallOrTreeExpression, found the following matches: <matches>";
    }
}

public list[RType] getCallOrTreeExpressionType(Expression ep, Expression ec, {Expression ","}* es) {
    // First, if we have any failures, just propagate those upwards, don't bother to
    // check the rest of the call.
    // TODO: We may want to check arity, etc anyway, since we could catch errors
    // where no function or constructor could possibly match.   
    if (checkForFail({ ec@rtype } + { e@rtype | e <- es }))
        return [collapseFailTypes({ ec@rtype } + { e@rtype | e <- es })];
            
    // We can have overloaded functions and overloaded data constructors. If the type
    // is overloaded, we need to check each overloading to find the one that works.
    // TODO: We should codify the rules for resolving overloaded functions, since
    // we need those rules here. It should be the case that the most specific function
    // wins, but we probably also need errors to indicate when definitions make this
    // impossible, like f(int,value) versus f(value,int).
    
    // Set up the possible alternatives. We will treat the case of no overloads as a trivial
    // case of overloading with only one alternative.
    set[ROverloadedType] alternatives = isOverloadedType(ec@rtype) ? getOverloadOptions(ec@rtype) : { ( (ec@rtype@at)? ) ? ROverloadedTypeWithLoc(ec@rtype,ec@rtype@at) :  ROverloadedType(ec@rtype) };
    
    // Now, try each alternative, seeing if one matches.
    // TODO: Add consistency check for type variables here, plus add information
    // about the return type based on the input types (assuming the return type
    // then is also based on a parametric type)

    set[RType] failures = { };
    list[RType] matches = [ ];
    
        list[Expression] actuals = [ e | e <- es ];
    
    for (a <- alternatives) {
            bool typeHasLoc = ROverloadedTypeWithLoc(_,_) := a;
        RType fcType = typeHasLoc ? a.overloadType[@at=a.overloadLoc] : a.overloadType;

        if (isFunctionType(fcType)) {
            list[RType] formalTypes = getFunctionArgumentTypes(fcType);
            list[RType] actualTypes = [ actual@rtype | actual <- actuals ];

            if ( (isVarArgsFun(fcType) && size(formalTypes) <= size(actualTypes)) || (!isVarArgsFun(fcType) && size(formalTypes) == size(actualTypes)) ) {
                    bool matched = true;
                if (size(actualTypes) > 0) {
                            for (n <- [0..size(actualTypes)-1] && matched) {
                                    RType formalType = (n < size(formalTypes)) ? formalTypes[n] : formalTypes[size(formalTypes)-1];
                                    RType actualType = actualTypes[n];
                                    if (! subtypeOf(actualType, formalType)) {
                                            failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",ep@\loc);
                                        matched = false;
                                    }                  
                            }
                }

                    // If we have matched so far, try to find mappings for any type variables
                    if (matched && typeContainsTypeVars(fcType)) {
                            // First, create a tuple type holding the actual function call parameters. We can use this
                    // to get the mappings. Note that we don't use a function here, since we are trying to find
                    // the bindings, and the return type (if it is parameterized) will be based on this mapping,
                    // but cannot contribute to it.
                            RType funcType = makeVoidType();
                            if (isVarArgsFun(fcType)) {
                            // Calculate the var args type based on the lub's of the elements passed in
                                    RType vat = makeVarArgsType(lubList(tail(actualTypes,size(actualTypes)-size(formalTypes)+1)));
                        // Create the function type using the formal return type and the actuals + the varargs
                        funcType = makeTupleType(head(actualTypes,size(formalTypes)-1) + vat);
                            } else {
                            funcType = makeTupleType(actualTypes);
                            }

                    // Now, find the bindings
                    if (!hasDeferredTypes(funcType)) {
                                    map[RName varName, RType varType] varMapping = ( );
                            varMapping = getTVBindings(makeTupleType(getFunctionArgumentTypes(fcType)), funcType, ( ), ep@\loc);
                            fcType = instantiateVars(varMapping, fcType);
                    } else {
                            println("WARNING: Location <ep@\loc>, Cannot bind formals <fcType> to actuals <funcType>, actuals contains deferred types.");
                    }
                     }

                     if (matched) matches = matches + fcType;
            } else {
                str orMore = isVarArgsFun(fcType) ? "or more " : "";
                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: function accepts <size(formalTypes)> <orMore>arguments but was given <size(actuals)>", ep@\loc);
            }
        } else if (isConstructorType(fcType)) {
                list[RType] formalTypes = getConstructorArgumentTypes(fcType);
                list[RType] actualTypes = [ actual@rtype | actual <- actuals ];
                if (size(formalTypes) == size(actuals)) {
                        bool matched = true;
                    for (n <- domain(actuals) && matched) {
                            RType formalType = formalTypes[n];
                            RType actualType = actualTypes[n];
                            if (! subtypeOf(actualType, formalType)) {
                                    failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",ep@\loc);
                                matched = false;
                            }
                    }
                    // If we have matched so far, try to find mappings for any type variables
                    if (matched && typeContainsTypeVars(fcType)) {
                            // First, we "mock up" the actual constructor type, based on the types of the actual parameters
                            RType consType = makeTupleType([RUnnamedType(acty) | acty <- actualTypes]);
                    // Now, find the bindings
                    if (!hasDeferredTypes(funcType)) {
                                    map[RName varName, RType varType] varMapping = ( );
                            varMapping = getTVBindings(getConstructorArgumentTypes(fcType), consType, ( ), ep@\loc);
                            fcType = instantiateVars(varMapping, fcType);
                    } else {
                            println("WARNING: Location <ep@\loc>, Cannot bind formals <fcType> to actuals <funcType>, actuals contains deferred types.");
                    }
                     }
                    if (matched) matches = matches + fcType;
               } else {
                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: constructor accepts <size(formalTypes)> arguments but was given <size(actuals)>", ep@\loc);
               }
        } else {
            failures = failures + makeFailType("Type <prettyPrintType(fcType)> is not a function or constructor type.",ep@\loc);
        }
    }

    if (size(matches) > 0)
        return matches;
    else
        return [ collapseFailTypes(failures) ];
}

public RType checkListExpression(Expression ep, {Expression ","}* es) {
    if (checkForFail({ e@rtype | e <- es })) return collapseFailTypes({ e@rtype | e <- es });
    list[RType] listTypes = [ ];
    // This handles list splicing -- we splice if we have a var l of list type and an exp like [ l ],
    // but we do not splice if we explicitly include the brackets, i.e. if we say [ [ l ] ].
    for (e <- es) {
            RType eType = e@rtype;
        if (isListType(eType) && (Expression)`[<{Expression ","}* el>]` := e) {
            listTypes = listTypes + [ e@rtype ];        
        } else if (isListType(eType)) {
            listTypes = listTypes + [ getListElementType(eType) ];
        } else {
            listTypes = listTypes + [ e@rtype ];
        }
    }
    return makeListType(lubList(listTypes));
}

public RType checkSetExpression(Expression ep, {Expression ","}* es) {
    if (checkForFail({ e@rtype | e <- es })) return collapseFailTypes({ e@rtype | e <- es });
    list[RType] setTypes = [ ];
    // This handles set splicing -- we splice if we have a var s of set type and an exp like { s },
    // but we do not splice if we explicitly include the brackets, i.e. if we say { { s } }.
    for (e <- es) {
            RType eType = e@rtype;
        if (isSetType(eType) && (Expression)`{<{Expression ","}* el>}` := e) {
            setTypes = setTypes + [ e@rtype ];
        } else if (isSetType(eType)) {
            setTypes = setTypes + [ getSetElementType(eType) ];
        } else {
            setTypes = setTypes + [ e@rtype ];
        }
    }
    return makeSetType(lubList(setTypes));
}

public RType checkTrivialTupleExpression(Expression ep, Expression ei) {
        if (isFailType(ei@rtype)) return ei@rtype;
    return makeTupleType([ ei@rtype ]);
}

public RType checkTupleExpression(Expression ep, Expression ei, {Expression ","}* el) {
    list[Expression] elist = [ei] + [ eli | eli <- el ];
    if (checkForFail({eli@rtype | eli <- elist})) return collapseFailTypes({eli@rtype | eli <- elist});
    return makeTupleType([ eli@rtype | eli <- elist]);
}

//
// Typecheck a closure. The type of the closure is a function type, based on the parameter types
// and the return type. This mainly then propagages any failures in the parameters or the
// closure body.
//
public RType checkClosureExpression(Expression ep, Type t, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    bool isVarArgs = size(pTypes) > 0 ? isVarArgsType(pTypes[size(pTypes)-1]) : false;
    set[RType] stmtTypes = { getInternalStatementType(s@rtype) | s <- ss };
    
    if (checkForFail(toSet(pTypes) + stmtTypes)) return collapseFailTypes(toSet(pTypes) + stmtTypes);

    return makeFunctionType(convertType(t), pTypes);
}

//
// Typecheck a void closure. The type of the closure is a function type, based on the parameter types
// and the void return type. This mainly then propagages any failures in the parameters or the
// closure body.
//
public RType checkVoidClosureExpression(Expression ep, Parameters p, Statement+ ss) {
    list[RType] pTypes = getParameterTypes(p);
    bool isVarArgs = size(pTypes) > 0 ? isVarArgsType(pTypes[size(pTypes)-1]) : false;
    set[RType] stmtTypes = { getInternalStatementType(s@rtype) | s <- ss };
    
    if (checkForFail(toSet(pTypes) + stmtTypes)) return collapseFailTypes(toSet(pTypes) + stmtTypes);

    return makeFunctionType(makeVoidType(), pTypes);
}
 
//
// The type of a block of expressions is the type generated by the last statement in the block.
//
public RType checkNonEmptyBlockExpression(Expression ep, Statement+ ss) {
    list[Statement] sl = [ s | s <- ss ];
    list[RType] slTypes = [ getInternalStatementType(s@rtype) | s <- sl ];

    if (checkForFail(toSet(slTypes))) {
        return collapseFailTypes(toSet(slTypes));
    } else {
        return slTypes[size(slTypes)-1];
    }
}

public RType checkVisitExpression(Expression ep, Label l, Visit v) {
    if (checkForFail({l@rtype, v@rtype})) return collapseFailTypes({l@rtype, v@rtype});
    return v@rtype;
}

//
// Paren expressions are handled below in checkExpression
//

public RType checkRangeExpression(Expression ep, Expression e1, Expression e2) {
    if (isIntType(e1@rtype) && isIntType(e2@rtype)) {
        return makeListType(makeIntType());
    } else {
        return propagateFailOr({ e1@rtype, e2@rtype }, makeFailType("Error in range operation: operation is not defined on the types " +
            prettyPrintType(e1@rtype) + " and " + prettyPrintType(e2@rtype), ep@\loc));
    }
}

public RType checkStepRangeExpression(Expression ep, Expression e1, Expression e2, Expression e3) {
    if (isIntType(e1@rtype) && isIntType(e2@rtype) && isIntType(e3@rtype)) {
        return makeListType(makeIntType());
    } else {
        return propagateFailOr({e1@rtype, e2@rtype, e3@rtype }, makeFailType("Error in step range operation: operation is not defined on the types " +
            prettyPrintType(e1@rtype) + ", " + prettyPrintType(e2@rtype) + " and " + prettyPrintType(e3@rtype),ep@\loc));
    }
}

public RType checkFieldUpdateExpression(Expression ep, Expression el, Name n, Expression er) {
    if (checkForFail({el@rtype,er@rtype})) return collapseFailTypes({el@rtype,er@rtype});
    RType ft = getFieldType(el@rtype, convertName(n), globalSTBuilder, ep@\loc);
    if (isFailType(ft)) return ft;
    if (subtypeOf(er@rtype,ft))
        return el@rtype;
    else
        return makeFailType("Type <prettyPrintType(er@rtype)> must be a subtype of type <prettyPrintType(ft)>", ep@\loc); 
}

//
// Fields can be included on ADTs (defined in constructors), 
public RType checkFieldAccessExpression(Expression ep, Expression el, Name n) {
    if (checkForFail({el@rtype})) return collapseFailTypes({el@rtype});
    return getFieldType(el@rtype, convertName(n), globalSTBuilder, ep@\loc);
}

private list[tuple[Field field, int offset]] getFieldOffsets(list[RNamedType] fieldTypes, list[Field] fields) {
    list[tuple[Field field, int offset]] result = [ ];
    map[RName, int] namedFields = size(fieldTypes) > 0 ? ( n : i | i <-  [0..size(fieldTypes)-1], RNamedType(_,n) := fieldTypes[i]) : ( );
    
    for (f <- fields) {
        switch(f) {
            case (Field) `<Name n>` : {
                RName fName = convertName(n);
                if (fName in namedFields) 
                    result += < f, namedFields[fName] >;
                else
                    result += < f, -1 >;                
            }

            case (Field) `<IntegerLiteral il>` : {
                int ival = toInt("<il>");
                if (ival >= size(fieldTypes))
                    result += <f, -1 >;
                else
                    result += <f, ival >;
            } 
        }
    }

    return result;
}

//
// Field projection is defined over maps, tuples, and relations.
//
// TODO: Improve pretty printing of error messages
// 
// TODO: Factor out common code
//
// TODO: Why does <f,-1> cause a parsing error when used in a pattern match?
//
public RType checkFieldProjectExpression(Expression ep, Expression e, {Field ","}+ fl) {
    if (checkForFail({e@rtype})) return collapseFailTypes({e@rtype});

    list[Field] fieldList = [ f | f <- fl ];

    RType expType = e@rtype;

    if (isMapType(expType)) {
        list[RNamedType] fields = getMapFieldsWithNames(expType);
        list[tuple[Field field, int offset]] fieldOffsets = getFieldOffsets(fields, fieldList);

        list[Field] badFields = [ f | <f,n> <- fieldOffsets, n == -1 ];
        if (size(badFields) > 0) return makeFailType("Map <prettyPrintType(expType)> does not contain fields <badFields>");

        list[int] fieldNumbers = [ n | <_,n> <- fieldOffsets ];
        bool keepFieldNames = size(fieldNumbers) == size(toSet(fieldNumbers));
                
        if (size(fieldNumbers) == 1)
            return makeSetType(getElementType(fields[fieldNumbers[0]]));
        else
            return makeRelType([ keepFieldNames ? fields[fieldNumbers[n]] : getElementType(fields[fieldNumbers[n]]) | n <- fieldNumbers ]);

    } else if (isRelType(expType)) {
        list[RNamedType] fields = getRelFieldsWithNames(expType);
        list[tuple[Field field, int offset]] fieldOffsets = getFieldOffsets(fields, fieldList);

        list[Field] badFields = [ f | <f,n> <- fieldOffsets, n == -1 ];
        if (size(badFields) > 0) return makeFailType("Relation <prettyPrintType(expType)> does not contain fields <badFields>");

        list[int] fieldNumbers = [ n | <_,n> <- fieldOffsets ];
        bool keepFieldNames = size(fieldNumbers) == size(toSet(fieldNumbers));              

        if (size(fieldNumbers) == 1)
            return makeSetType(getElementType(fields[fieldNumbers[0]]));
        else
            return makeRelType([ keepFieldNames ? fields[fieldNumbers[n]] : getElementType(fields[fieldNumbers[n]]) | n <- fieldNumbers ]);

    } else if (isTupleType(expType)) {
        list[RNamedType] fields = getTupleFieldsWithNames(expType);
        list[tuple[Field field, int offset]] fieldOffsets = getFieldOffsets(fields, fieldList);

        list[Field] badFields = [ f | <f,n> <- fieldOffsets, n == -1 ];
        if (size(badFields) > 0) return makeFailType("Tuple <prettyPrintType(expType)> does not contain fields <badFields>");

        list[int] fieldNumbers = [ n | <_,n> <- fieldOffsets ];
        bool keepFieldNames = size(fieldNumbers) == size(toSet(fieldNumbers));              

        if (size(fieldNumbers) == 1)
            return getElementType(fields[fieldNumbers[0]]);
        else
            return makeTupleType([ keepFieldNames ? fields[fieldNumbers[n]] : getElementType(fields[fieldNumbers[n]]) | n <- fieldNumbers ]);

    } else {
        return makeFailType("Cannot use field projection on type <prettyPrintType(expType)>", ep@\loc);
    }
}

public RType checkSubscriptExpression(Expression ep, Expression el, {Expression ","}+ es) {
    list[Expression] indexList = [ e | e <- es ];
    if (checkForFail({ e@rtype | e <- es } + el@rtype)) return collapseFailTypes({ e@rtype | e <- es } + el@rtype);

    RType expType = el@rtype;
    
    if (isTupleType(expType)) {
        if (size(indexList) > 1) return makeFailType("Subscripts on tuples must contain exactly one element", ep@\loc);
        if (! isIntType(indexList[0]@rtype) ) 
                        return makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
        return lubList(getTupleFields(expType));        
    } else if (isRelType(expType)) {
        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
        RType relLeftType = getRelFields(expType)[0];
        RType indexType = lubSet({ e@rtype | e <- indexList});
        if (! (subtypeOf(relLeftType,indexType) || subtypeOf(indexType,relLeftType))) { 
            return makeFailType("The subscript type <prettyPrintType(indexType)> must be comparable to the type of the first projection of the relation, <prettyPrintType(relLeftType)>", ep@\loc);
        }
        list[RType] resultTypes = tail(getRelFields(expType));
        if (size(resultTypes) == 1)
            return makeSetType(resultTypes[0]);
        else
            return makeRelType(resultTypes);        
    } else if (isMapType(expType)) {
        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
        RType domainType = getMapDomainType(expType);
        RType indexType = indexList[0]@rtype;
        if (! (subtypeOf(domainType,indexType) || subtypeOf(indexType,domainType))) 
            return makeFailType("The subscript type <prettyPrintType(indexType)> must be comparable to the domain type <prettyPrintType(domainType)>", ep@\loc);
        return getMapRangeType(expType);
    }  else if (isNodeType(expType)) {
        if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
        if (! isIntType(indexList[0]@rtype) ) return makeFailType("Subscripts on nodes must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
        return makeValueType();
    } else if (isListType(expType)) {
        if (size(indexList) > 1) return makeFailType("Subscripts on lists must contain exactly one element", ep@\loc);
        if (! isIntType(indexList[0]@rtype) ) return makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
        return getListElementType(expType);     
    } else {
        return makeFailType("Subscript not supported on type <prettyPrintType(expType)>", ep@\loc);
    }
}

public RType checkIsDefinedExpression(Expression ep, Expression e) {
    if (isFailType(e@rtype)) {
        return e@rtype;
    } else {
        return makeBoolType();
    }
}

private RType checkNegationExpression(Expression ep, Expression e) {
    if (isBoolType(e@rtype)) {
        return makeBoolType();
    } else {
        return propagateFailOr({ e@rtype },makeFailType("Error in negation operation: <e> should have type " + 
            prettyPrintType(makeBoolType()) + " but instead has type " + prettyPrintType(e@rtype),ep@\loc));
    }
}

private RType checkNegativeExpression(Expression ep, Expression e) {
    if (isIntType(e@rtype) || isRealType(e@rtype)) {
        return e@rtype;
    } else {
        return propagateFailOr({ e@rtype },makeFailType("Error in negation operation: <e> should have a numeric type " + 
            "but instead has type " + prettyPrintType(e@rtype),ep@\loc));
    }
}

public RType checkTransitiveReflexiveClosureExpression(Expression ep, Expression e) {
    RType expType = e@rtype;
    if (isFailType(expType)) return expType;
    if (! isRelType(expType)) return makeFailType("Error in transitive reflexive closure operation: <e> should be a relation, but instead is <prettyPrintType(expType)>", ep@\loc);
    list[RNamedType] relFields = getRelFieldsWithNames(expType);
    if (size(relFields) != 2) return makeFailType("Error in transitive reflexive closure operation: <e> should be a relation of arity 2, but instead is <prettyPrintType(expType)>", ep@\loc);
    return expType; 
}

public RType checkTransitiveClosureExpression(Expression ep, Expression e) {
    RType expType = e@rtype;
    if (isFailType(expType)) return expType;
    if (! isRelType(expType)) return makeFailType("Error in transitive closure operation: <e> should be a relation, but instead is <prettyPrintType(expType)>", ep@\loc);
    list[RNamedType] relFields = getRelFieldsWithNames(expType);
    if (size(relFields) != 2) return makeFailType("Error in transitive closure operation: <e> should be a relation of arity 2, but instead is <prettyPrintType(expType)>", ep@\loc);
    return expType; 
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
public RType checkGetAnnotationExpression(Expression ep, Expression e, Name n) {
    RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    if (checkForFail({ e@rtype, rt })) return collapseFailTypes({ e@rtype, rt });
    return rt;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
public RType checkSetAnnotationExpression(Expression ep, Expression el, Name n, Expression er) {
    RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    if (checkForFail({ el@rtype, rt, er@rtype })) return collapseFailTypes({ el@rtype, rt, er@rtype });
    if (! subtypeOf(er@rtype, rt)) return makeFailType("The type of <er>, <prettyPrintType(er@rtype)>, must be a subtype of the type of <n>, <prettyPrintType(rt)>", ep@\loc);
    return rt;
}

//
// Composition is defined for functions, maps, and relations.
//
// TODO: Question on order: currently the order is "backwards" from the standard mathematical
// order, i.e., r1 o r2 is r1, then r2, versus r2 first, then r1. Is this the desired behavior, or was
// this accidental? For functions the order appears to be correct, even though the implementation
// doesn't actually work. For maps the order is the same "backwards" order as it is for relations.
//
// NOTE: map composition does not maintain field names. Is this intentional?
//
public RType checkCompositionExpression(Expression ep, Expression el, Expression er) {
    if (checkForFail({ el@rtype, er@rtype })) return collapseFailTypes({ el@rtype, er@rtype });
    RType leftType = el@rtype; RType rightType = er@rtype;
    if (isFunType(leftType) && isFunType(rightType)) {
        return makeFailType("Type checking this feature is not yet supported!", ep@\loc); // TODO: Implement this, including support for overloading
    } else if (isMapType(leftType) && isMapType(rightType)) {
        // Check to make sure the fields are of the right type to compose
        RType j1 = getMapRangeType(leftType); RType j2 = getMapDomainType(rightType);
        if (! subtypeOf(j1,j2)) return makeFailType("Incompatible types in composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", ep@\loc);

        return RMapType(getMapDomainType(leftType), getMapRangeType(rightType));
    } else if (isRelType(leftType) && isRelType(rightType)) {
        list[RNamedType] leftFields = getRelFieldsWithNames(leftType); 
        list[RNamedType] rightFields = getRelFieldsWithNames(rightType);

        // Check to make sure each relation is just arity 2
        if (size(leftFields) != 2) return makeFailType("Error in composition: <el> should be a relation of arity 2, but instead is <prettyPrintType(leftType)>", ep@\loc);
        if (size(rightFields) != 2) return makeFailType("Error in composition: <er> should be a relation of arity 2, but instead is <prettyPrintType(rightType)>", ep@\loc);

        // Check to make sure the fields are of the right type to compose
        RType j1 = getElementType(head(tail(leftFields,1))); RType j2 = getElementType(head(rightFields));
        if (! subtypeOf(j1,j2)) return makeFailType("Incompatible types in composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", ep@\loc);

        // Check to see if we need to drop the field names, then return the proper type
        RNamedType r1 = head(leftFields); RNamedType r2 = head(tail(rightFields,1));
        if (RNamedType(t1,n) := r1 && RNamedType(t2,n) := r2)
            return RRelType([RUnnamedType(t1),RUnnamedType(t2)]); // Both fields had the same name, so just keep the type and make unnamed fields
        else
            return RRelType([r1,r2]); // Maintain the field names, they differ
    }
    return makeFailType("Composition is not supported on types <prettyPrintType(leftType)> and <prettyPrintType(rightType)>", ep@\loc);
}

public RType checkProductExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RProduct(), ep@\loc));
}

//
// TODO: Note that, in the interpreter, a java exception is thrown when you join a set with
// a relation.
//
public RType checkJoinExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RJoin(), ep@\loc));
}

public RType checkDivExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RDiv(), ep@\loc));
}

public RType checkModExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RMod(), ep@\loc));
}

public RType checkIntersectionExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RInter(), ep@\loc));
}

public RType checkPlusExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RPlus(), ep@\loc));
}

//
// TODO:
// Question: why should - change the type of the result? Shouldn't set[a] - set[b] or
// list[a] - list[b] always result in a set or list of type a?
//
public RType checkMinusExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RMinus(), ep@\loc));
}

public RType checkNotInExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RNotIn(), ep@\loc));
}

public RType checkInExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RIn(), ep@\loc));
}

public RType checkLessThanExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RLt(), ep@\loc));
}

public RType checkLessThanOrEqualExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RLtEq(), ep@\loc));
}

public RType checkGreaterThanExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RGt(), ep@\loc));
}

public RType checkGreaterThanOrEqualExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RGtEq(), ep@\loc));
}

public RType checkEqualsExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, REq(), ep@\loc));
}

public RType checkNotEqualsExpression(Expression ep, Expression el, Expression er) {
    return propagateFailOr({ el@rtype, er@rtype}, expressionType(el@rtype, er@rtype, RNEq(), ep@\loc));
}

// TODO: Add additional checking in case of failure or "branches" to better detect errors (but this is valid)
public RType checkIfThenElseExpression(Expression ep, Expression eb, Expression et, Expression ef) {
    if (checkForFail({ eb@rtype, et@rtype, ef@rtype }))
        return collapseFailTypes({ eb@rtype, et@rtype, ef@rtype });
    else {
        if (!isBoolType(eb@rtype)) {
            return makeFailType("Expression <eb> should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(eb@rtype)>",ep@\loc);
        } else {
            if (!equivalent(et@rtype,ef@rtype) && !subtypeOf(et@rtype,ef@rtype) && !subtypeOf(ef@rtype,et@rtype)) {
                return makeFailType("Expressions <et> and <ef> should have matching types or be in a subtype relation, but instead have types <prettyPrintType(et@rtype)> and <prettyPrintType(ef@rtype)>",ep@\loc);
            } else {
                return lub(et@rtype,ef@rtype);
            }
        }
    }
}

// TODO: In the interpreter this currently can return two types: either the type of ed, or type type
// of eo. Do we want to require these to be the same, or return the lub as the static type? Do the
// latter for now...
public RType checkIfDefinedOtherwiseExpression(Expression ep, Expression ed, Expression eo) {
    if (checkForFail({ ed@rtype, eo@rtype })) return collapseFailTypes({ ed@rtype, eo@rtype });
    return lub(ed@rtype,eo@rtype);
}

public RType checkImplicationExpression(Expression ep, Expression el, Expression er) {
    if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
        return makeBoolType();
    } else {
        return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in implication operation: operation is not defined on the types " +
            prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
    }
}

public RType checkEquivalenceExpression(Expression ep, Expression el, Expression er) {
    if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
        return makeBoolType();
    } else {
        return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in equivalence operation: operation is not defined on the types " +
            prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
    }
}

public RType checkAndExpression(Expression ep, Expression el, Expression er) {
    if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
        return makeBoolType();
    } else {
        return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in and operation: operation is not defined on the types " +
            prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
    }
}

public RType checkOrExpression(Expression ep, Expression el, Expression er) {
    if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
        return makeBoolType();
    } else {
        return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in or operation: operation is not defined on the types " +
            prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
    }
}

public RType checkMatchExpression(Expression ep, Pattern p, Expression e) {
    if (checkForFail({ p@rtype, e@rtype })) return collapseFailTypes({ p@rtype, e@rtype });
    RType boundType = bindInferredTypesToPattern(e@rtype, p);
    if (isFailType(boundType)) return boundType;
    if ( (! subtypeOf(e@rtype, boundType)) && (! subtypeOf(boundType, e@rtype))) 
        return makeFailType("The type of the expression, <prettyPrintType(e@rtype)>, must be comparable to that of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
    return makeBoolType();
}

public RType checkNoMatchExpression(Expression ep, Pattern p, Expression e) {
    if (checkForFail({ p@rtype, e@rtype })) return collapseFailTypes({ p@rtype, e@rtype });
    RType boundType = bindInferredTypesToPattern(e@rtype, p);
    if (isFailType(boundType)) return boundType;
    if ( (! subtypeOf(e@rtype, boundType)) && (! subtypeOf(boundType, e@rtype))) 
        return makeFailType("The type of the expression, <prettyPrintType(e@rtype)>, must be comparable to that of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
    return makeBoolType();
}

//
// Enumerators act like a match, i.e., like :=, except for containers, like lists,
// sets, etc, where they "strip off" the outer layer of the subject. For instance,
// n <- 1 acts just like n := 1, while n <- [1..10] acts like [_*,n,_*] := [1..10].
//
public RType checkEnumeratorExpression(Expression ep, Pattern p, Expression e) {
    if (checkForFail({ p@rtype, e@rtype })) { 
        return collapseFailTypes({ p@rtype, e@rtype });
    } 
    
    RType expType = e@rtype;

    // TODO: Nodes
    // TODO: ADTs
    // TODO: Any other special cases?   
    if (isListType(expType)) {
            RType et = getListElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The list element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isSetType(expType)) {
            RType et = getSetElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The set element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isBagType(expType)) {
            RType et = getBagElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The bag element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isContainerType(expType)) {
            RType et = getContainerElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The container element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isRelType(expType)) {
            RType et = getRelElementType(expType);
        RType boundType = bindInferredTypesToPattern(et, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(et, boundType)) return makeFailType("The relation element type of the subject, <prettyPrintType(et)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isMapType(expType)) {
            RType dt = getMapDomainType(expType);
        RType boundType = bindInferredTypesToPattern(dt, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(dt, boundType)) return makeFailType("The domain type of the map, <prettyPrintType(dt)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else if (isTupleType(expType)) {
            RType tt = lubList(getTupleFields(expType));
        RType boundType = bindInferredTypesToPattern(tt, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(tt, boundType)) return makeFailType("The least upper bound of the tuple element types, <prettyPrintType(tt)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    } else {
        RType boundType = bindInferredTypesToPattern(expType, p);
        if (isFailType(boundType)) return boundType;
        if (! subtypeOf(expType, boundType)) return makeFailType("The type of the subject, <prettyPrintType(expType)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
        return makeBoolType();
    }
    
    println("Unhandled enumerator case, <p> \<- <e>");
    return makeBoolType();
}

public RType checkSetComprehensionExpression(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    set[Expression] allExps = { e | e <- els } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps })) {
        return collapseFailTypes({ e@rtype | e <- allExps });
    } else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            list[RType] setTypes = [ ];
            for (e <- els) {
                    RType eType = e@rtype;
                if (isSetType(replaceInferredTypes(eType)) && (Expression)`{<{Expression ","}* el>}` := e) {
                    setTypes = setTypes + [ replaceInferredTypes(eType) ];
                } else if (isSetType(replaceInferredTypes(eType))) {
                    setTypes = setTypes + [ getSetElementType(replaceInferredTypes(eType)) ];
                } else {
                    setTypes = setTypes + [ replaceInferredTypes(eType) ];
                }
            }
            return makeSetType(lubList(setTypes));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

public RType checkListComprehensionExpression(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
    set[Expression] allExps = { e | e <- els } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps }))
        return collapseFailTypes({ e@rtype | e <- allExps });
    else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            list[RType] listTypes = [ ];
            for (e <- els) {
                    RType eType = e@rtype;
                if (isListType(replaceInferredTypes(eType)) && (Expression)`[<{Expression ","}* el>]` := e) {
                    listTypes = listTypes + [ replaceInferredTypes(eType) ];
                } else if (isListType(replaceInferredTypes(eType))) {
                    listTypes = listTypes + [ getListElementType(replaceInferredTypes(eType)) ];
                } else {
                    listTypes = listTypes + [ replaceInferredTypes(eType) ];
                }
            }
            return makeListType(lubList(listTypes));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

public RType checkMapComprehensionExpression(Expression ep, Expression ef, Expression et, {Expression ","}+ ers) {
    set[Expression] allExps = { ef } + { et } + { e | e <- ers };
    if (checkForFail({ e@rtype | e <- allExps }))
        return collapseFailTypes({ e@rtype | e <- ers });
    else {
        set[RType] genFailures = { 
            makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
                e <- ers, !isBoolType(e@rtype)
        };
        if (size(genFailures) == 0) {
            return makeMapType(replaceInferredTypes(ef@rtype), replaceInferredTypes(et@rtype));
        } else {
            return collapseFailTypes(genFailures);
        }
    }
}

//
// NOTE: We cannot actually type this statically, since the type of the "it" expression is implicit and the type of
// the result is based only indirectly on the type of er. If we could type this, we could probably solve the halting
// problem ;)
public RType checkReducerExpression(Expression ep, Expression ei, Expression er, {Expression ","}+ ers) {
    list[RType] genTypes = [ e@rtype | e <- ers ];
    if (checkForFail(toSet(genTypes + ei@rtype + er@rtype))) return collapseFailTypes(toSet(genTypes + ei@rtype + er@rtype));

    return makeValueType(); // for now, since it could be anything
}

public RType checkAllExpression(Expression ep, {Expression ","}+ ers) {
    if (checkForFail({ e@rtype | e <- ers }))
        return collapseFailTypes({ e@rtype | e <- ers });
    else {
        for (e <- ers) {
            if (! isBoolType(e@rtype)) {
                return makeFailType("All expressions used in expression all must have type <prettyPrintType(makeBoolType())>, but expression <e> has type <prettyPrintType(e@rtype)>",ep@\loc);         
            } 
        }
    }
    return makeBoolType();
}
        
public RType checkAnyExpression(Expression ep, {Expression ","}+ ers) {
    if (checkForFail({ e@rtype | e <- ers }))
        return collapseFailTypes({ e@rtype | e <- ers });
    else {
        for (e <- ers) {
            if (! isBoolType(e@rtype)) {
                return makeFailType("All expressions used in expression any must have type <prettyPrintType(makeBoolType())>, but expression <e> has type <prettyPrintType(e@rtype)>",ep@\loc);         
            } 
        }
    }
    return makeBoolType();
}

public RType checkMapExpression(Expression exp) {
        list[tuple[Expression mapDomain, Expression mapRange]] mapContents = getMapExpressionContents(exp);
    if (size(mapContents) == 0) return makeMapType(makeVoidType(), makeVoidType());

    list[RType] domains; list[RType] ranges;
    for (<md,mr> <- mapContents) { domains += md@rtype; ranges += mr@rtype; }

    if (checkForFail(toSet(domains+ranges))) return collapseFailTypes(toSet(domains+ranges));
    return makeMapType(lubList(domains),lubList(ranges));   
}

public RType checkExpression(Expression exp) {
    switch(exp) {
        case (Expression)`<BooleanLiteral bl>` : {
            return makeBoolType();
        }

        case (Expression)`<DecimalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Expression)`<OctalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Expression)`<HexIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Expression)`<RealLiteral rl>`  : {
            return makeRealType();
        }

        case (Expression)`<StringLiteral sl>`  : {
                list[Tree] ipl = prodFilter(sl, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            set[RType] failures = { ipe@rtype | ipe <- ipl, isFailType(ipe@rtype) };
            if (size(failures) > 0) return collapseFailTypes(failures);
            return makeStrType();
        }

        case (Expression)`<LocationLiteral ll>`  : {
                list[Expression] ipl = prodFilter(ll, bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd; });
            set[RType] failures = { ipe@rtype | ipe <- ipl, isFailType(ipe@rtype) };
            if (size(failures) > 0) return collapseFailTypes(failures);
            return makeLocType();
        }

        case (Expression)`<DateTimeLiteral dtl>`  : {
            return makeDateTimeType();
        }

        // _ as a name, should only be in patterns, but include just in case...
        case (Expression)`_`: {
            RType rt = getTypeForName(globalSTBuilder, RSimpleName("_"), exp@\loc);
            return rt;
        }

        // Name
        case (Expression)`<Name n>`: {
            RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
            return rt;
        }
        
        // QualifiedName
        case (Expression)`<QualifiedName qn>`: {
            RType rt = getTypeForName(globalSTBuilder, convertName(qn), qn@\loc);
            return rt;
        }

        // ReifiedType
        case (Expression)`<BasicType t> ( <{Expression ","}* el> )` : {
            RType rt = checkReifiedTypeExpression(exp,t,el);
            return rt;
        }

        // CallOrTree
        case (Expression)`<Expression e1> ( <{Expression ","}* el> )` : {
            RType t = checkCallOrTreeExpression(exp,e1,el);
            return t;
        }

        // List
        case (Expression)`[<{Expression ","}* el>]` : {
            RType t = checkListExpression(exp,el);
            return t;
        }

        // Set
        case (Expression)`{<{Expression ","}* el>}` : {
            RType t = checkSetExpression(exp,el);
            return t;
        }

        // Tuple, with just one element
        case (Expression)`<<Expression ei>>` : {
            RType t = checkTrivialTupleExpression(exp,ei);
            return t;
        }

        // Tuple, with multiple elements
        case (Expression)`<<Expression ei>, <{Expression ","}* el>>` : {
            RType t = checkTupleExpression(exp,ei,el);
            return t;
        }

        // Closure
        case (Expression)`<Type t> <Parameters p> { <Statement+ ss> }` : {
            RType t = checkClosureExpression(exp,t,p,ss);
            return t;
        }

        // VoidClosure
        case (Expression)`<Parameters p> { <Statement* ss> }` : {
            RType t = checkVoidClosureExpression(exp,p,ss);
            return t;
        }

        // NonEmptyBlock
        case (Expression)`{ <Statement+ ss> }` : {
            RType t = checkNonEmptyBlockExpression(exp,ss);
            return t;       
        }
        
        // Visit
        case (Expression) `<Label l> <Visit v>` : {
            RType t = checkVisitExpression(exp,l,v);
            return t;           
        }
        
        // ParenExp
        case (Expression)`(<Expression e>)` : {
            RType t = e@rtype;
            return t;
        }

        // Range
        case (Expression)`[ <Expression e1> .. <Expression e2> ]` : {
            RType t = checkRangeExpression(exp,e1,e2);
            return t;
        }

        // StepRange
        case (Expression)`[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
            RType t = checkStepRangeExpression(exp,e1,e2,e3);
            return t;
        }

        // ReifyType
        case (Expression)`#<Type t>` : {
            RType t = RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))]));
            return t;
        }

        // FieldUpdate
        case (Expression)`<Expression e1> [<Name n> = <Expression e2>]` : {
            RType t = checkFieldUpdateExpression(exp,e1,n,e2);
            return t;
        }

        // FieldAccess
        case (Expression)`<Expression e1> . <Name n>` : {
            RType t = checkFieldAccessExpression(exp,e1,n);
            return t;
        }

        // FieldProject
        case (Expression)`<Expression e1> < <{Field ","}+ fl> >` : {
            RType t = checkFieldProjectExpression(exp,e1,fl);
            return t;
        }

        // Subscript 
        case (Expression)`<Expression e1> [ <{Expression ","}+ el> ]` : {
            RType t = checkSubscriptExpression(exp,e1,el);
            return t;
        }

        // IsDefined
        case (Expression)`<Expression e> ?` : {
            RType t = checkIsDefinedExpression(exp,e);
            return t;
        }

        // Negation
        case (Expression)`! <Expression e>` : {
            RType t = checkNegationExpression(exp,e);
            return t;
        }

        // Negative
        case (Expression)`- <Expression e> ` : {
            RType t = checkNegativeExpression(exp,e);
            return t;
        }

        // TransitiveReflexiveClosure
        case (Expression)`<Expression e> * ` : {
            RType t = checkTransitiveReflexiveClosureExpression(exp,e);
            return t;
        }

        // TransitiveClosure
        case (Expression)`<Expression e> + ` : {
            RType t = checkTransitiveClosureExpression(exp,e);
            return t;
        }

        // GetAnnotation
        case (Expression)`<Expression e> @ <Name n>` : {
            RType t = checkGetAnnotationExpression(exp,e,n);
            return t;
        }

        // SetAnnotation
        case (Expression)`<Expression e1> [@ <Name n> = <Expression e2>]` : {
            RType t = checkSetAnnotationExpression(exp,e1,n,e2);
            return t;
        }

        // Composition
        case (Expression)`<Expression e1> o <Expression e2>` : {
            RType t = checkCompositionExpression(exp,e1,e2);
            return t;
        }

        // Product
        case (Expression)`<Expression e1> * <Expression e2>` : {
            RType t = checkProductExpression(exp,e1,e2);
            return t;
        }

        // Join
        case (Expression)`<Expression e1> join <Expression e2>` : {
            RType t = checkJoinExpression(exp,e1,e2);
            return t;
        }

        // Div
        case (Expression)`<Expression e1> / <Expression e2>` : {
            RType t = checkDivExpression(exp,e1,e2);
            return t;
        }

        // Mod
        case (Expression)`<Expression e1> % <Expression e2>` : {
            RType t = checkModExpression(exp,e1,e2);
            return t;
        }

        // Intersection
        case (Expression)`<Expression e1> & <Expression e2>` : {
            RType t = checkIntersectionExpression(exp,e1,e2);
            return t;
        }
        
        // Plus
        case (Expression)`<Expression e1> + <Expression e2>` : {
            RType t = checkPlusExpression(exp,e1,e2);
            return t;
        }

        // Minus
        case (Expression)`<Expression e1> - <Expression e2>` : {
            RType t = checkMinusExpression(exp,e1,e2);
            return t;
        }

        // NotIn
        case (Expression)`<Expression e1> notin <Expression e2>` : {
            RType t = checkNotInExpression(exp,e1,e2);
            return t;
        }

        // In
        case (Expression)`<Expression e1> in <Expression e2>` : {
            RType t = checkInExpression(exp,e1,e2);
            return t;
        }

        // LessThan
        case (Expression)`<Expression e1> < <Expression e2>` : {
            RType t = checkLessThanExpression(exp,e1,e2);
            return t;
        }

        // LessThanOrEq
        case (Expression)`<Expression e1> <= <Expression e2>` : {
            RType t = checkLessThanOrEqualExpression(exp,e1,e2);
            return t;
        }

        // GreaterThan
        case (Expression)`<Expression e1> > <Expression e2>` : {
            RType t = checkGreaterThanExpression(exp,e1,e2);
            return t;
        }

        // GreaterThanOrEq
        case (Expression)`<Expression e1> >= <Expression e2>` : {
            RType t = checkGreaterThanOrEqualExpression(exp,e1,e2);
            return t;
        }

        // Equals
        case (Expression)`<Expression e1> == <Expression e2>` : {
            RType t = checkEqualsExpression(exp,e1,e2);
            return t;
        }

        // NotEquals
        case (Expression)`<Expression e1> != <Expression e2>` : {
            RType t = checkNotEqualsExpression(exp,e1,e2);
            return t;
        }

        // IfThenElse (Ternary)
        case (Expression)`<Expression e1> ? <Expression e2> : <Expression e3>` : {
            RType t = checkIfThenElseExpression(exp,e1,e2,e3);
            return t;   
        }

        // IfDefinedOtherwise
        case (Expression)`<Expression e1> ? <Expression e2>` : {
            RType t = checkIfDefinedOtherwiseExpression(exp,e1,e2);
            return t;       
        }

        // Implication
        case (Expression)`<Expression e1> ==> <Expression e2>` : {
            RType t = checkImplicationExpression(exp,e1,e2);
            return t;
        }

        // Equivalence
        case (Expression)`<Expression e1> <==> <Expression e2>` : {
            RType t = checkEquivalenceExpression(exp,e1,e2);
            return t;
        }

        // And
        case (Expression)`<Expression e1> && <Expression e2>` : {
            RType t = checkAndExpression(exp,e1,e2);
            return t;
        }

        // Or
        case (Expression)`<Expression e1> || <Expression e2>` : {
            RType t = checkOrExpression(exp,e1,e2);
            return t;
        }
        
        // Match
        case (Expression)`<Pattern p> := <Expression e>` : {
            RType t = checkMatchExpression(exp,p,e);
            return t;
        }

        // NoMatch
        case (Expression)`<Pattern p> !:= <Expression e>` : {
            RType t = checkNoMatchExpression(exp,p,e);
            return t;
        }

        // Enumerator
        case (Expression)`<Pattern p> <- <Expression e>` : {
            RType t = checkEnumeratorExpression(exp,p,e);
            return t;
        }
        
        // Set Comprehension
        case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` : {
            RType t = checkSetComprehensionExpression(exp,el,er);
            return t;
        }

        // List Comprehension
        case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` : {
            RType t = checkListComprehensionExpression(exp,el,er);
            return t;
        }
        
        // Map Comprehension
        case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` : {
            RType t = checkMapComprehensionExpression(exp,ef,et,er);
            return t;
        }
        
        // Reducer 
        case (Expression)`( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
            RType t = checkReducerExpression(exp,ei,er,egs);
            return t;
        }
        
        // It
        case (Expression)`it` : {
            RType rt = getTypeForName(globalSTBuilder, RSimpleName("it"), exp@\loc);
            return rt; 
        }
            
        // All 
        case (Expression)`all ( <{Expression ","}+ egs> )` : {
            RType t = checkAllExpression(exp,egs);
            return t;
        }

        // Any 
        case (Expression)`any ( <{Expression ","}+ egs> )` : {
            RType t = checkAnyExpression(exp,egs);
            return t;
        }
        
        // TODO: Look in embedding.sdf for more expression productions
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
    // exp[0] is the production used, exp[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := exp[0]) {
        RType t = checkMapExpression(exp);
        return t;
    }
    
    throw "Unmatched expression <exp> at location <exp@\loc>";
}

//
// Handle string templates
//
public RType checkStringTemplate(StringTemplate s) {
    switch(s) {
        case (StringTemplate)`for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { e@rtype | e <- gens } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case (StringTemplate)`if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case (StringTemplate)`if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` : {
            set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- preThen } + 
                                         { getInternalStatementType(st@rtype) | st <- postThen } +
                                         { getInternalStatementType(st@rtype) | st <- preElse } + { getInternalStatementType(st@rtype) | st <- postElse };
                list[Tree] ipl = prodFilter(bodyThen, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
                ipl = prodFilter(bodyElse, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case (StringTemplate)`while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
            set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }

        case (StringTemplate)`do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` : {
            set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
                list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
            for (ipe <- ipl) {
                    if ((Expression)`<Expression ipee>` := ipe)
                            res = res + ipee@rtype;
                else if ((StringTemplate)`<StringTemplate ipet>` := ipe)
                        res = res + ipet@rtype;
            }
            if (checkForFail(res)) return collapseFailTypes(res);
            return makeStrType();
        }
    }

    throw "Unexpected string template syntax at location <s@\loc>, no match";
}

//
// Check individual cases
//
public RType checkCase(Case c) {
    switch(c) {
        case (Case)`case <PatternWithAction p>` : {
            
            // If insert is used anywhere in this case pattern, find the type being inserted and
            // check to see if it is correct.
            // TODO: This will only check the first insert. Need to modify logic to handle all
            // insert statements that are in this visit, but NOT in a nested visit. It may be
            // easiest to mark visit boundaries during the symbol table construction, since that
            // is done in a top-down manner.
            RType caseType = getCasePatternType(c);
            set[RType] failures = { };
            top-down-break visit(p) {
                case (Expression) `<Label l> <Visit v>` : 0; // no-op
                
                case (Statement) `<Label l> <Visit v>` : 0; // no-op
                
                case Statement ins : (Statement) `insert <DataTarget dt> <Statement s>` : {
                    RType stmtType = getInternalStatementType(s@rtype);
                    if (! subtypeOf(stmtType, caseType)) {
                        failures += makeFailType("Type of insert, <prettyPrintType(stmtType)>, does not match type of case, <prettyPrintType(caseType)>", s@\loc);
                    }
                } 
            }
            RType retType = (size(failures) == 0) ? p@rtype : collapseFailTypes(failures);
            return retType;
        }
        
        case (Case)`default : <Statement b>` : {
            return getInternalStatementType(b@rtype);
        }
    }
}

//
// Check assignables.
//
// NOTE: This system uses a pair of types, referred to below as the "part type" and the
// "whole type". This is because, in cases like x.f = 3, we need to know the entire
// type of the resulting value, here the type of x, as well as the type of the part of
// x being assigned into, here the type of field f on x. In this example, the type of x
// is the whole type, while the type of the field f is the part type.
//

//
// TODO: Not sure what to return here, since, for a tuple, this could be any of the
// types of the tuple fields. So, for tuples, just return the lub right now, which will
// let the test pass. Returning void would be more conservative, but then this would
// never work for tuples.
//
// NOTE: Doing this for relations with arity > 2 doesn't seem to work right now in the
// interpreter. I'm not sure if this is by design or by accident.
//
// TODO: Review this code, it's complex and could have hidden bugs...
//
public RType checkSubscriptAssignable(Assignable ap, Assignable a, Expression e) {
    if (checkForFail({a@rtype, e@rtype})) return collapseFailTypes({a@rtype, e@rtype});

    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);

    if (isTupleType(partType)) {
        return makeAssignableType(wholeType, lubList(getTupleFields(partType)));        
    } else if (isRelType(partType)) {
        list[RType] relTypes = getRelFields(partType);
        RType relLeftType = relTypes[0];
        list[RType] resultTypes = tail(relTypes);
        if (! (subtypeOf(e@rtype, relLeftType))) return makeFailType("The subscript type <prettyPrintType(e@rtype)> must be a subtype of the first project of the relation type, <prettyPrintType(relLeftType)>", ap@\loc);
        if (size(resultTypes) == 1)
            return makeAssignableType(wholeType, makeSetType(resultTypes[0]));
        else
            return makeAssignableType(wholeType, makeRelType(resultTypes));     
    } else if (isMapType(partType)) {
        RType domainType = getMapDomainType(partType);
        if (! subtypeOf(e@rtype, domainType)) return makeFailType("The subscript type <prettyPrintType(e@rtype)> must be a subtype of to the domain type <prettyPrintType(domainType)>", ap@\loc);
        return makeAssignableType(wholeType, getMapRangeType(partType));
    }  else if (isNodeType(partType)) {
        return makeAssignableType(wholeType, makeValueType());
    } else if (isListType(partType)) {
        if (! isIntType(e@rtype) ) 
                        return makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(e@rtype)>", ap@\loc);
        return makeAssignableType(wholeType, getListElementType(partType));     
    } else {
        return makeFailType("Subscript not supported on type <prettyPrintType(partType)>", ap@\loc);
    }
}

//
// A field access assignable is of the form a.f, where a is another assignable. The whole
// type is just the type of a, since f is a field of a and ultimately we will return an
// a as the final value. The part type is the type of f, since this is the "part" being
// assigned into. We check for the field on the part type of the assignable, since the
// assignable could be of the form a.f1.f2.f3, or a[n].f, etc, and the type with the field 
// is not a as a whole, but a.f1.f2, or a[n], etc.
//
public RType checkFieldAccessAssignable(Assignable ap, Assignable a, Name n) {
    if (checkForFail({a@rtype})) return collapseFailTypes({a@rtype});
    RType partType = getPartType(a@rtype); // The "part" of a which contains the field
    RType wholeType = getWholeType(a@rtype); // The overall type of all of a
    RType fieldType = getFieldType(partType, convertName(n), globalSTBuilder, ap@\loc);
    if (isFailType(fieldType)) return fieldType;
    return makeAssignableType(wholeType, fieldType); 
}

//
// An if-defined-or-default assignable is of the form a ? e, where a is another assignable
// and e is the default value. We propagate up both the whole and part types, since this
// impacts neither. For instance, if we say a.f1.f2 ? [ ], we are still going to assign into
// f2, so we need that information. However, we need to check to make sure that [ ] could be
// assigned into f2, since it will actually be the default value given for it if none exists.
//      
public RType checkIfDefinedOrDefaultAssignable(Assignable ap, Assignable a, Expression e) {
    if (isFailType(a@rtype) || isFailType(e@rtype)) return collapseFailTypes({ a@rtype, e@rtype });
    RType partType = getPartType(a@rtype); // The "part" being checked for definedness
    RType wholeType = getWholeType(a@rtype); // The "whole" being assigned into
    if (!subtypeOf(e@rtype,partType)) 
                return makeFailType("The type of <e>, <prettyPrintType(e@rtype)>, is not a subtype of the type of <a>, <prettyPrintType(partType)>",ap@\loc);
    return makeAssignableType(wholeType, partType); // Propagate up the current part and whole once we've made sure the default is valid
}

//
// An annotation assignable is of the form a @ n, where a is another assignable and
// n is the annotation name on this assignable. The whole type for a is propagated
// up, with the new part type being the type of this annotation, which should be a valid
// annotation on the current part type.
//
// TODO: Ensure that annotation n is a valid annotation on the part type of a
//
public RType checkAnnotationAssignable(Assignable ap, Assignable a, Name n) {
    if (isFailType(a@rtype)) return collapseFailTypes({ a@rtype });
    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);
    RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    if (isFailType(rt)) return rt;
    return makeAssignableType(wholeType, rt);
}

//
// A tuple assignable is of the form < a_1, ..., a_n >, where a_1 ... a_n are
// other assignables. For tuple assignables, the part type is a tuple of the
// part types of the constituent assignables, while the whole type is the tuple
// of the whole types of the constituent assignables. This is because we will
// ultimately return a tuple made up of the various assignables, but we will
// also assign into the part types of each of the assignables.
//      
public RType checkTrivialTupleAssignable(Assignable ap, Assignable a) {
    list[Assignable] alist = [ a ];
    if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
    RType wholeType = makeTupleType([ getWholeType(ai@rtype) | ai <- alist]);
    RType partType = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
    return makeAssignableType(wholeType,partType);
}

public RType checkTupleAssignable(Assignable ap, Assignable a, {Assignable ","}* al) {
    list[Assignable] alist = [ a ] + [ ai | ai <- al];
    if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
    RType wholeType = makeTupleType([ getWholeType(ai@rtype) | ai <- alist]);
    RType partType = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
    return makeAssignableType(wholeType,partType);
}

//
// Check assignables.
//
public RType checkAssignable(Assignable a) {
    switch(a) {
        // Variable _
        case (Assignable)`_` : {
            RType rt = getTypeForName(globalSTBuilder, RSimpleName("_"), a@\loc);
            return makeAssignableType(rt,rt); 
        }

        // Variable with an actual name
        case (Assignable)`<QualifiedName qn>` : {
            RType rt = getTypeForName(globalSTBuilder, convertName(qn), qn@\loc);
            return makeAssignableType(rt,rt); 
        }
        
        // Subscript
        case (Assignable)`<Assignable al> [ <Expression e> ]` : {
            return checkSubscriptAssignable(a,al,e);
        }
        
        // Field Access
        case (Assignable)`<Assignable al> . <Name n>` : {
            return checkFieldAccessAssignable(a,al,n);
        }
        
        // If Defined or Default
        case (Assignable)`<Assignable al> ? <Expression e>` : {
            return checkIfDefinedOrDefaultAssignable(a,al,e);
        }
        
        // Annotation
        case (Assignable)`<Assignable al> @ <Name n>` : {
            return checkAnnotationAssignable(a,al,n);
        }
        
        // Tuple, with just one element
        case (Assignable)`< <Assignable ai> >` : {
            return checkTupleAssignable(a, ai);
        }

        // Tuple, with multiple elements
        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            return checkTupleAssignable(a, ai, al);
        }
    }
}

//
// Given an actual type rt and an assignable a, recurse the structure of a, assigning the correct parts of
// rt to any named parts of a. For instance, in an assignment like x = 5, if x has an inference type it will
// be assigned type int, while in an assignment like <a,b> = <true,4>, a would be assigned type bool
// while b would be assigned type int (again, assuming they are both inferrence variables). The return
// type is the newly-computed type of the assignable, with inference vars bound to concrete types.
//
// NOTE: This functionality now does much more checking, similarly to the bind logic in patterns,
// since we also now do all the subtype checking here as well.
//
public RType bindInferredTypesToAssignable(RType rt, Assignable a) {
    RType partType = getPartType(a@rtype);
    RType wholeType = getWholeType(a@rtype);
    
    switch(a) {
        // Anonymous name (variable name _)
            // When assigning into _, we make sure that either the type assigned to _ is still open or that the type we are
        // assigning is a subtype. Realistically, it should always be open, since each instance of _ is distinct.
        case (Assignable)`_` : {
                RType varType = getTypeForNameLI(globalSTBuilder, RSimpleName("_"), a@\loc);
                if (isInferredType(varType)) {
                    RType t = globalSTBuilder.inferredTypeMap[getInferredTypeIndex(varType)];
                if (isInferredType(t)) {
                        updateInferredTypeMappings(t,rt);
                    return rt;
                } else if (! equivalent(t,rt)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name: already bound <prettyPrintType(t)>, attempting to bind <prettyPrintType(rt)>", a@\loc);
                } else {
                        return rt;
                }
            } else {
                    if (subtypeOf(rt, varType)) {
                        return varType;
                } else {
                        return makeFailType("Type <prettyPrintType(rt)> must be a subtype of the type of <a>, <prettyPrintType(varType)>",a@\loc);
                }
            }
        }

        // Qualified Name (variable name)
        // When assigning into a name, we make sure that either the type assigned to the name is still open or that the
        // type we are assigning is a subtype.
        // NOTE: This includes a terrible hack to handle situations such as x = { }; x = { 1 } which don't work right now.
        // This allows container (set/list/map) types to be bumped up from void to non-void element types. However, this
        // is not sound, so we need to instead divise a better way to handle this, for instance by using constraint systems.
        // so, TODO: Fix this!
        case (Assignable)`<QualifiedName qn>` : {
                RType varType = getTypeForNameLI(globalSTBuilder,convertName(qn),qn@\loc);
            if (isInferredType(varType)) {
                RType t = globalSTBuilder.inferredTypeMap[getInferredTypeIndex(varType)];
                if (isInferredType(t)) {
                    updateInferredTypeMappings(t,rt);
                    return rt;
                } else if ( (isListType(t) && isVoidType(getListElementType(t)) && isListType(rt)) || 
                                            (isSetType(t) && isVoidType(getSetElementType(t)) && isSetType(rt)) ||
                                            (isMapType(t) && isVoidType(getMapDomainType(t)) && isVoidType(getMapRangeType(t)) && isMapType(rt))) {
                        updateInferredTypeMappings(varType,rt);
                    return rt;
                } else if ( (isListType(t) && isListType(rt) && isVoidType(getListElementType(rt))) ||
                            (isSetType(t) && isSetType(rt) && isVoidType(getSetElementType(rt))) ||
                        (isMapType(t) && isMapType(rt) && isVoidType(getMapDomainType(rt)) && isVoidType(getMapRangeType(rt)))) {
                    return t;
                    } else if (! equivalent(t,rt)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed name <qn>: already bound <prettyPrintType(t)>, attempting to bind <prettyPrintType(rt)>", qn@\loc);
                    } else {
                        return rt; 
                    }
            } else {
                    if (subtypeOf(rt, varType)) {
                        return varType;
                } else {
                        return makeFailType("Type <prettyPrintType(rt)> must be a subtype of the type of <a>, <prettyPrintType(varType)>",a@\loc);
                }
            }
        }
        
        // Subscript
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type. Checking the assignable already checked the structure of
        // the whole type.
        case (Assignable)`<Assignable al> [ <Expression e> ]` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to subscript with type <prettyPrintType(partType)>", a@\loc);
            return getWholeType(a@rtype);
        }
        
        // Field Access
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type.
        case (Assignable)`<Assignable al> . <Name n>` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to field with type <prettyPrintType(partType)>", 
                                                    a@\loc);
            return getWholeType(a@rtype);
        }
        
        // If Defined or Default
        // This just pushes the binding down into the assignable on the left-hand
        // side of the ?, the default expression has no impact on the binding.
        case (Assignable)`<Assignable al> ? <Expression e>` : {
            return bindInferredTypesToAssignable(rt, al);
        }
        
        // Annotation
        // Check to see if the part type of the assignable matches the binding type. It doesn't make
        // sense to push this any further down, since the type we have to compare against is just the
        // part type, not the whole type.
        case (Assignable)`<Assignable al> @ <Name n>` : {
                RType partType = getPartType(a@rtype);
            if (! subtypeOf(rt, partType))
                    return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to field with type <prettyPrintType(partType)>", 
                                                    a@\loc);
            return getWholeType(a@rtype);
        }
        
        // Tuple
        // To be correct, the type being bound into the assignable also needs to be a tuple
        // of the same length. If this is true, the bind recurses on each tuple element.
        // If not, a failure type, indicating the type of failure (arity mismatch, or type of
        // assignable not a tuple) has occurred.
        case (Assignable)`< <Assignable ai> >` : {
            list[Assignable] alist = [ai];
            if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
                list[RType] tupleFieldTypes = getTupleFields(rt);
                results = [bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]];
                failures = { result | result <- results, isFailType(result) };
                if (size(failures) > 0) return collapseFailTypes(failures);
                return makeTupleType(results);
            } else if (!isTupleType(rt)) {
                return makeFailType("Type mismatch: cannot assign non-tuple type <prettyPrintType(rt)> to <a>", a@\loc);
            } else {
                return makeFailType("Arity mismatch: cannot assign tuple of length <getTupleFieldCount(rt)> to <a>", a@\loc);
            }
        }

        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            list[Assignable] alist = [ai] + [ ali | ali <- al ];
            if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
                list[RType] tupleFieldTypes = getTupleFields(rt);
                results = [bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]];
                failures = { result | result <- results, isFailType(result) };
                if (size(failures) > 0) return collapseFailTypes(failures);
                return makeTupleType(results);
            } else if (!isTupleType(rt)) {
                return makeFailType("Type mismatch: cannot assign non-tuple type <prettyPrintType(rt)> to <a>", a@\loc);
            } else {
                return makeFailType("Arity mismatch: cannot assign tuple of length <getTupleFieldCount(rt)> to <a>", a@\loc);
            }
        }
        
    }
}

//
// Check local variable declarations. The variables themselves have already been checked, so we just
// need to collect any possible failures here.
//
public RType checkLocalVarItems(Statement sp, {Variable ","}+ vs) {
    set[RType] localTypes = { v@rtype | v <- vs };
    return makeStatementType(checkForFail(localTypes) ? collapseFailTypes(localTypes) : makeVoidType());
}

//
// Check catch clauses in exception handlers
//
public RType checkCatch(Catch c) {
    switch(c) {
        case (Catch)`catch : <Statement b>` : {
            return b@rtype;
        }
        
        // TODO: Pull out into own function for consistency
        case (Catch)`catch <Pattern p> : <Statement b>` : {
            
            if (checkForFail({ p@rtype, getInternalStatementType(b@rtype) }))
                return makeStatementType(collapseFailTypes({ p@rtype, getInternalStatementType(b@rtype) }));
            else {
                RType boundType = bindInferredTypesToPattern(p@rtype, p);
                if (isFailType(boundType)) return makeStatementType(boundType);
                return b@rtype;
            }
        }
    }
}

public RType checkLabel(Label l) {
    if ((Label)`<Name n> :` := l && hasRType(globalSTBuilder, n@\loc)) {
        RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        return rt;
    }
    return makeVoidType();
}

//
// TODO: Extract common code in each case into another function
//
// TODO: Any way to verify that types of visited sub-parts can properly be types
// of subparts of the visited expression?
//
// TODO: Put checks back in, taken out for now since they are adding useless "noise"
//
public RType checkVisit(Visit v) {
    switch(v) {
        case (Visit)`visit (<Expression se>) { <Case+ cs> }` : {
            set[RType] caseTypes = { c@rtype | c <- cs };
            if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
            RType caseLubType = lubSet(caseTypes);
            //if (subtypeOf(caseLubType, se@rtype)) 
                return se@rtype;
            //else
                //return makeFailType("Visit cases must all be subtypes of the type of the visited expression",v@\loc); 
        }
        
        case (Visit)`<Strategy st> visit (<Expression se>) { <Case+ cs> }` : {
            set[RType] caseTypes = { c@rtype | c <- cs };
            if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
            RType caseLubType = lubSet(caseTypes);
            //if (subtypeOf(caseLubType, se@rtype))
                return se@rtype;
            //else
                //return makeFailType("Visit cases must all be subtypes of the type of the visited expression",v@\loc); 
        }       
    }
}

//
// Check the type of a reified type pattern.
//
// TODO: Should add additional checks, including an arity check, since you can only have
// one type inside the pattern (even though the syntax allows more).
//
// TODO: Need to expand type so we know that ADTs, etc are marked.
//
// TODO: pl should all have type type
//
public RType checkReifiedTypePattern(Pattern pp, Type t, {Pattern ","}* pl) {
    if (checkForFail({ p@rtype | p <- pl })) return collapseFailTypes({ p@rtype | p <- pl });
    return makeReifiedType(convertType(t), [ p@rtype | p <- pl ]);
}

//
// TODO: this takes a strict view of what a static type error is for patterns. We
// may want a more relaxed version, where if someone uses a pattern that could never
// match we just let it go, since this won't cause a runtime error (but it may be
// useful for the user to know)
//
public RType checkCallOrTreePattern(Pattern pp, Pattern pc, {Pattern ","}* ps) {
    list[RType] matches = getCallOrTreePatternType(pp, pc, ps);
    if (size(matches) > 1) { 
        return makeFailType("There are multiple possible matches for this constructor pattern. Please add additional type information. Matches: <prettyPrintTypeListWLoc(matches)>");
    } else if (size(matches) == 1 && rt := head(matches) && isFailType(rt)) {
        return rt;
    } else if (size(matches) == 1 && rt := head(matches) && isConstructorType(rt)) {
        RType boundType = bindInferredTypesToPattern(rt, pp[@rtype=getConstructorResultType(rt)][@fctype=rt]);
        return rt;
    } else {
        throw "Unexpected situation, checkCallOrTreePattern, found the following matches: <matches>";
    }
}

//
// Find the type of a call or tree pattern. This has to be the use of a constructor -- functions
// invocations can't be used in patterns. So, this function needs to figure out which constructor
// is being used. Note that this is a local determination, i.e., we don't currently allow
// information from the surrounding context to help. So, we have to be able to determine the type
// just from looking at the constructor name and its pattern.
//
// TODO: See if we need to allow contextual information. We may need this in cases where (for instance)
// we have two constructors C of two different ADTs, and we want to be able to use matches such
// as C(_) :=.
//
// TODO: See how much error information we can gather. Currently, we just return if pc or ps
// contains any failures. However, in some situations we could get more error info, for instance
// if pc has a normal type but there are no constructors with that name that take the given
// number of parameters.
//
public list[RType] getCallOrTreePatternType(Pattern pp, Pattern pc, {Pattern ","}* ps) {
    // First, if we have any failures, just propagate those upwards, don't bother to
    // check the rest of the call. 
    if (checkForFail({ pc@rtype } + { p@rtype | p <- ps }))
        return [ collapseFailTypes({ pc@rtype } + { p@rtype | p <- ps }) ];
            
    // Set up the possible alternatives. We will treat the case of no overloads as a trivial
    // case of overloading with only one alternative.
    set[ROverloadedType] alternatives = isOverloadedType(pc@rtype) ? getOverloadOptions(pc@rtype) : { ( (pc@rtype@at)? ) ? ROverloadedTypeWithLoc(pc@rtype,pc@rtype@at) :  ROverloadedType(pc@rtype) };
    
    // Now, try each alternative, seeing if one matches. Note: we could have multiple matches (for
    // instance, if we have inference vars in a constructor), even if the instances themselves
    // did not overlap. e.g., S(int,bool) and S(str,loc) would not overlap, but both would
    // be acceptable alternatives for S(x,y) := e. At this point, we can just return both; the caller
    // can decide if this is acceptable or not.
    list[RType] matches = [ ];
    set[RType] failures = { };
    list[Pattern] actuals = [ p | p <- ps ];
        
    for (a <- alternatives) {
            bool typeHasLoc = ROverloadedTypeWithLoc(_,_) := a;
        RType fcType = typeHasLoc ?  a.overloadType[@at=a.overloadLoc] : a.overloadType;
        
        if (isConstructorType(fcType)) {
            list[RType] formals = getConstructorArgumentTypes(fcType);

            // NOTE: We do not currently support varargs constructors.
            if (size(formals) == size(actuals)) {
                set[RType] localFailures = { };
                for (idx <- domain(actuals)) {
                    RType actualType = actuals[idx]@rtype;
                    RType formalType = formals[idx];
                    if (! subtypeOf(actualType, formalType)) {
                        localFailures = localFailures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: pattern type for pattern argument <actuals[idx]> is <prettyPrintType(actuals[idx]@rtype)> but argument type is <prettyPrintType(formalType)>",actuals[idx]@\loc);
                    }
                }
                if (size(localFailures) > 0) {
                    failures = failures + localFailures;
                } else {
                    matches = matches + ( typeHasLoc ? fcType[@at=a.overloadLoc ] : fcType ); 
                }
            } else {
                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: constructor accepts <size(formals)> arguments while pattern <pp> has arity <size(actuals)>", pp@\loc);
            }
        } else {
            failures = failures + makeFailType("Type <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : ""> is not a constructor",pp@\loc);
        }
    }

    // If we found a match, use that. If not, send back the failures instead. The matches take precedence
    // since failures can result from trying all possible constructors in an effort to find the matching
    // constructor, which is the constructor we will actually use.  
    if (size(matches) > 0)
        return matches;
    else
        return [ collapseFailTypes(failures) ];
}

//
// This handles returning the correct type for a pattern in a list. There are several cases:
//
// 1. A name that represents a list. This can be treated like an element of the list, since [1,x,2], where x
//    is [3,4], just expands to [1,3,4,2]. More formally, in these cases, if list(te) := t, we return te.
//
// 2. A pattern that is explicitly given a name or typed name (name becomes patterns) or guarded pattern. Here
//    we look at the next level of pattern and treat it according to these rules.
//
// 3. All other patterns. Here we just return the type of the pattern.
//  
public RType getPatternTypeForList(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isListType(patType)) return getListElementType(patType);
        if (isContainerType(patType)) return getContainerElementType(patType);    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return getPatternTypeForList(p);
    }
    return pat@rtype;
}

//
// Indicates if a variable is a list container variable. Uses the same rules as the above.
// 
public bool isListContainerVar(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isListType(patType)) return true;
        if (isContainerType(patType)) return true;    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return isListContainerVar(p);
    }
    return false;
}

//
// Determine the type of a list pattern. This is based on the types of its components.
// It may not be possible to determine an exact type, in which case we delay the
// computation of the lub by returning a list of lub type.
//
public RType checkListPattern(Pattern pp, {Pattern ","}* ps) {
    if (checkForFail({ p@rtype | p <- ps })) return collapseFailTypes({ p@rtype | p <- ps });
    
    // Get the types in the list, we need to watch for inferrence types since we need
    // to handle those separately. We also need to match for lub types that are
    // propagating up from nested patterns.
    list[RType] patTypes = [ getPatternTypeForList(p) | p <- ps ];
    list[RType] patTypesI = [ t | t <- patTypes, hasDeferredTypes(t) ];
    
    if (size(patTypesI) > 0) {
        return makeListType(makeLubType(patTypes));
    } else {
        return makeListType(lubList(patTypes));
    }
}

//
// This handles returning the correct type for a pattern in a set. This uses the same rules
// given above for getPatternTypeForList, so refer to that for more details.
//  
public RType getPatternTypeForSet(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isSetType(patType)) return getSetElementType(patType);
        if (isContainerType(patType)) return getContainerElementType(patType);    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return getPatternTypeForSet(p);
    }
    return pat@rtype;
}

//
// Indicates if a variable is a set container variable. Uses the same rules as the above.
// 
public bool isSetContainerVar(Pattern pat) {
    if ((Pattern)`<Name n>` := pat || (Pattern)`<QualifiedName qn>` := pat || (Pattern)`<Type t><Name n>` := pat || (Pattern)`<QualifiedName qn> *` := pat) {
            RType patType = pat@rtype;
        if (isSetType(patType)) return true;
        if (isContainerType(patType)) return true;    
    } else if ((Pattern)`<Name n> : <Pattern p>` := pat || (Pattern)`<Type t> <Name n> : <Pattern p>` := pat || (Pattern)`[ <Type t> ] <Pattern p>` := pat) {
        return isSetContainerVar(p);
    }
    return false;
}       

//
// Determine the type of a set pattern. This is based on the types of its components.
// It may not be possible to determine an exact type, in which case we delay the
// computation of the lub by returning a set of lub type.
//
public RType checkSetPattern(Pattern pp, {Pattern ","}* ps) {
    if (checkForFail({ p@rtype | p <- ps })) return collapseFailTypes({ p@rtype | p <- ps });

    // Get the types in the list, we need to watch for inferrence types since we need
    // to handle those separately.  We also need to match for lub types that are
    // propagating up from nested patterns.
    list[RType] patTypes = [ getPatternTypeForSet(p) | p <- ps ];
    list[RType] patTypesI = [ t | t <- patTypes, hasDeferredTypes(t)];
    
    if (size(patTypesI) > 0) {
        return makeSetType(makeLubType(patTypes));
    } else {
        return makeSetType(lubList(patTypes));
    }
}

//
// Check the type of a trivial (one element) tuple pattern, which is either
// tuple[t] when pi : t or fail when pi has a fail type.
//
public RType checkTrivialTuplePattern(Pattern pp, Pattern pi) {
    set[Pattern] pset = {pi};
    if (checkForFail({p@rtype | p <- pset})) return collapseFailTypes({p@rtype | p <- pset});
    return makeTupleType([ p@rtype | p <- pset]);
}

//
// Check the type of a non-trivial (multiple element) tuple pattern.
//
public RType checkTuplePattern(Pattern pp, Pattern pi, {Pattern ","}* ps) {
    list[Pattern] plist = [pi] + [ p | p <- ps ];
    if (checkForFail({p@rtype | p <- plist})) return collapseFailTypes({p@rtype | p <- plist});
    return makeTupleType([ p@rtype | p <- plist]);
}

//
// Check the variable becomes pattern. Note that we don't bind the pattern type to
// the name here, since we don't actually have a real type yet for the pattern -- it
// itself could contain inference vars, etc. We wait until the bind function is
// called to do this.
//
public RType checkVariableBecomesPattern(Pattern pp, Name n, Pattern p) {
    RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    if (checkForFail({ rt, p@rtype })) return collapseFailTypes({ rt, p@rtype });
    return p@rtype;
}

//
// Check the typed variable becomes pattern. We require that the pattern type is
// a subtype of the name type, since otherwise we cannot assign it. Note: we ignore
// the type t here since the process of building the symbol table already assigned
// this type to n.
//
public RType checkTypedVariableBecomesPattern(Pattern pp, Type t, Name n, Pattern p) {
    RType rt = getTypeForName(globalSTBuilder, convertName(n), n@\loc);
    if (checkForFail({ rt, p@rtype })) return collapseFailTypes({ rt, p@rtype });
    if (! subtypeOf(p@rtype, rt)) return makeFailType("Type of pattern, <prettyPrintType(p)>, must be a subtype of the type of <n>, <prettyPrintType(rt)>",pp@\loc);
    return rt;
}

//
// Check the guarded pattern type. The result will be of that type, since it must be to match
// (else the match would fail). We return a failure if the pattern can never match the guard. 
//
// TODO: Need to expand type so we know that ADTs, etc are marked.
//
public RType checkGuardedPattern(Pattern pp, Type t, Pattern p) {
    if (isFailType(p@rtype)) return p@rtype;
    RType rt = convertType(t);
    if (! subtypeOf(p@rtype, rt)) return makeFailType("Type of pattern, <prettyPrintType(p)>, must be a subtype of the type of the guard, <prettyPrintType(rt)>",pp@\loc);
    return rt;
}

//
// For the antipattern we will return the type of the pattern, since we still want
// to make sure the pattern can be used to form a valid match. For instance, we
// want to allow !n := 3, where n is an int, but not !n := true, even though, in
// some sense, this is true -- it indicates a (potential) misunderstanding of what
// is being done in the code.
//
public RType checkAntiPattern(Pattern pp, Pattern p) {
    return p@rtype;
}

//
// Type check a map pattern. 
//
public RType checkMapPattern(Pattern pat) {
        list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
    if (size(mapContents) == 0) return makeMapType(makeVoidType(), makeVoidType());

    list[RType] domains; list[RType] ranges;
    for (<md,mr> <- mapContents) { domains += md@rtype; ranges += mr@rtype; }

    if (checkForFail(toSet(domains+ranges))) return collapseFailTypes(toSet(domains+ranges));
    return makeMapType(lubList(domains),lubList(ranges));   
}

//
// Driver code to check patterns. This code, except for literals and names, mainly just 
// dispatches to the various functions defined above.
//
// TODO: This is still insufficient to deal with descendant patterns, since we really
// need to know the type of the subject before we can truly check it. This isn't an
// issue with patterns like / x := B, but it is with patterns like [_*,/x,_*] := B,
// where B is a list with (for instance) ADTs inside. So, think about how we
// want to handle this, we may need another type that is treated specially in patterns,
// like RUnderspecified(t), where t is the type information we have (e.g., list of
// something inferred, etc)
//
public RType checkPattern(Pattern pat) {
    switch(pat) {
        case (Pattern)`<BooleanLiteral bl>` : {
            return makeBoolType();
        }

        case (Pattern)`<DecimalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<OctalIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<HexIntegerLiteral il>`  : {
            return makeIntType();
        }

        case (Pattern)`<RealLiteral rl>`  : {
            return makeRealType();
        }

        case (Pattern)`<StringLiteral sl>`  : {
            return makeStrType();
        }

        case (Pattern)`<LocationLiteral ll>`  : {
            return makeLocType();
        }

        case (Pattern)`<DateTimeLiteral dtl>`  : {
            return makeDateTimeType();
        }

        // Regular Expression literal
        case (Pattern)`<RegExpLiteral rl>` : {
                // NOTE: The only possible source of errors here is the situation where one of the variables in the
            // regular expression pattern is not a string. We usually can't detect this until the bind, though,
            // so save that check for bindInferredTypesToPattern.
                list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
            list[RType] retTypes = [ getTypeForName(globalSTBuilder, RSimpleName("<n>"), n@\loc) | n <- names ];
            if (checkForFail(toSet(retTypes))) return collapseFailTypes(toSet(retTypes));
            return makeStrType();
        }

        case (Pattern)`_` : {
                RType patType = getTypeForName(globalSTBuilder, RSimpleName("_"), pat@\loc);
            //println("For pattern _ at location <pat@\loc> found type(s) <patType>");
            return patType;
        }
        
        case (Pattern)`<Name n>`: {
            return getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        }
        
        // QualifiedName
        case (Pattern)`<QualifiedName qn>`: {
            return getTypeForName(globalSTBuilder, convertName(qn), qn@\loc);
        }

        // ReifiedType
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            return checkReifiedTypePattern(pat,t,pl);
        }

        // CallOrTree
        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            return checkCallOrTreePattern(pat,p1,pl);
        }

        // List
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            return checkListPattern(pat,pl);
        }

        // Set
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            return checkSetPattern(pat,pl);
        }

        // Tuple
        case (Pattern) `<<Pattern pi>>` : {
            return checkTrivialTuplePattern(pat,pi);
        }

        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
            return checkTuplePattern(pat,pi,pl);
        }

        // Typed Variable
        case (Pattern) `<Type t> <Name n>` : {
            return getTypeForName(globalSTBuilder, convertName(n), n@\loc);
        }

        // Multi Variable
        case (Pattern) `_ *` : {
            return getTypeForName(globalSTBuilder, RSimpleName("_"), pat@\loc);
        }
        
        case (Pattern) `<QualifiedName qn> *` : {
            return getTypeForName(globalSTBuilder, convertName(qn), qn@\loc);
        }

        // Descendant
        case (Pattern) `/ <Pattern p>` : {
            return p@rtype;
        }

        // Variable Becomes
        case (Pattern) `<Name n> : <Pattern p>` : {
            return checkVariableBecomesPattern(pat,n,p);
        }
        
        // Typed Variable Becomes
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            return checkTypedVariableBecomesPattern(pat,t,n,p);
        }
        
        // Guarded
        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            return checkGuardedPattern(pat,t,p);
        }           
        
        // Anti
        case (Pattern) `! <Pattern p>` : {
            return checkAntiPattern(pat,p);
        }
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
        // pat[0] is the production used, pat[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := pat[0]) {
            RType t = checkMapPattern(pat);
                return t;
    }
    throw "Missing case on checkPattern for pattern <pat> at location <pat@\loc>";
}

//
// Bind any variables used in the map pattern to the types present in type rt.
//
public RType bindInferredTypesToMapPattern(RType rt, Pattern pat) {
        // Get the domain and range types for rt
        RType mapDomain = getMapDomainType(rt);
        RType mapRange = getMapRangeType(rt);

        list[tuple[Pattern mapDomain, Pattern mapRange]] mapContents = getMapPatternContents(pat);
    if (size(mapContents) == 0) return makeMapType(makeVoidType(), makeVoidType());

    list[RType] domains; list[RType] ranges;
    for (<md,mr> <- mapContents) { 
            domains += bindInferredTypesToPattern(mapDomain, pl);
        ranges += bindInferredTypesToPattern(mapRange, pr);
        }

    if (checkForFail(toSet(domains+ranges))) return collapseFailTypes(toSet(domains+ranges));
    return makeMapType(lubList(domains),lubList(ranges));   
}

//
// Bind inferred types to multivar names: _* and QualifiedName*
//
public RType bindInferredTypesToMV(RType rt, RType pt, Pattern pat) {
        RType retType;

    // Make sure the type we are given is actually one that can contain elements
    if (! (isListType(rt) || isSetType(rt) || isContainerType(rt))) {
            return makeFailType("Attempting to bind type <prettyPrintType(rt)> to a multivariable <pat>",pat@\loc);
    }

    // Make sure that the type we are given is compatible with the type of the container variable
    if ( ! ( (isListType(rt) && (isListType(pt) || isContainerType(pt))) ||
                 (isSetType(rt) && (isSetType(pt) || isContainerType(pt))) ||
                 (isContainerType(rt) && isContainerType(pt)))) {
            return makeFailType("Attempting to bind type <prettyPrintType(rt)> to an incompatible container type <prettyPrintType(pt)>",pat@\loc);
        }

        // This should be structured as RContainerType(RInferredType(#)) unless we have assigned a more specific
    // type between creation and now. It should always be a container (list, set, or container) of some sort.
    if (isContainerType(pt) || isListType(pt) || isSetType(pt)) {
                RType elementType;
        bool elementIsInferred = false;
            if (isContainerType(pt)) {
                    elementIsInferred = (isInferredType(getContainerElementType(pt))) ? true : false;
            elementType = (isInferredType(getContainerElementType(pt))) ?
                    globalSTBuilder.inferredTypeMap[getInferredTypeIndex(getContainerElementType(pt))] :
                getContainerElementType(pt);
        } else if (isListType(pt)) {
            elementIsInferred = (isInferredType(getListElementType(pt))) ? true : false;
            elementType = (isInferredType(getListElementType(pt))) ?
                    globalSTBuilder.inferredTypeMap[getInferredTypeIndex(getListElementType(pt))] : 
                    getListElementType(pt);
        } else if (isSetType(pt)) {
            elementIsInferred = (isInferredType(getSetElementType(pt))) ? true : false;
            elementType = (isInferredType(getSetElementType(pt))) ?
                    globalSTBuilder.inferredTypeMap[getInferredTypeIndex(getSetElementType(pt))] : 
                getSetElementType(pt);
        }

        // Get the type of element inside the type being bound
        RType relementType = isContainerType(rt) ? getContainerElementType(rt) : (isListType(rt) ? getListElementType(rt) : getSetElementType(rt));

        if (elementIsInferred) {
                // The element type is inferred. See if it still is open -- if it still points to an inferred type.
            if (isInferredType(elementType)) {
                        // Type still open, update mapping
                updateInferredTypeMappings(elementType,relementType);
                retType = rt;
                    } else if (! equivalent(elementType, relementType)) {
                        // Already assigned a type, issue a failure, attempting to bind multiple types to the same var
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <pat>: already bound element type as <prettyPrintType(elementType)>, attempting to bind new element type <prettyPrintType(relementType)>", pat@\loc);
            } else {
                // Trying to assign the same type again, which is fine, just return it.
                retType = rt;
                }
        } else {
                // The element type is NOT an inferred type. The type of rt must match exactly.
                if (! equivalent(elementType, relementType)) {
                    retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <pat>: already bound element type as <prettyPrintType(elementType)>, attempting to bind new element type <prettyPrintType(relementType)>", pat@\loc);
            } else {
                retType = rt;
            }  
        }
    } else {
            throw "Unexpected type assigned to container var at location <pat@\loc>: <prettyPrintType(pt)>";
    }
    
        return retType;
}

//
// Recursively bind the types from an expression to any inferred types in a pattern. To make subtyping easier,
// we do the binding before we do the subtyping. This allows us to find specific errors in cases where the
// subject and the pattern do not match -- for instance, we can find that a constructor is given two
// arguments, but expects three. If we do subtyping checks first, we get less information -- only that the
// pattern and the subject are not comparable.
//
// TODO: In certain odd cases Lub types could be assigned to names; make sure those are resolved
// correctly here... 
//
public RType bindInferredTypesToPattern(RType rt, Pattern pat) {
    RType pt = pat@rtype; // Just save some typing below...
    
    // If either the type we are binding against (rt) or the current pattern type are fail
    // types, don't try to bind, just fail, we had something wrong in either the pattern
    // or the subject that may yield lots of spurious errors here.
    if (isFailType(rt) || isFailType(pat@rtype)) return collapseFailTypes({ rt, pat@rtype });
    
    // Now, compare the pattern and binding (subject) types, binding actual types to lub and
    // inference types if possible. 
    switch(pat) {
        case (Pattern)`<BooleanLiteral bl>` : {
            if (isBoolType(rt) && isBoolType(pt)) {
                return pt;
            } else {
                return makeFailType("Boolean literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<DecimalIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<OctalIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<HexIntegerLiteral il>`  : {
            if (isIntType(rt) && isIntType(pt)) {
                return pt;
            } else {
                return makeFailType("Integer literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<RealLiteral rl>`  : {
            if (isRealType(rt) && isRealType(pt)) {
                return pt;
            } else {
                return makeFailType("Real literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<StringLiteral sl>`  : {
            if (isStrType(rt) && isStrType(pt)) {
                return pt;
            } else {
                return makeFailType("String literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<LocationLiteral ll>`  : {
            if (isLocType(rt) && isLocType(pt)) {
                return pt;
            } else {
                return makeFailType("Location literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        case (Pattern)`<DateTimeLiteral dtl>`  : {
            if (isDateTimeType(rt) && isDateTimeType(pt)) {
                return pt;
            } else {
                return makeFailType("DateTime literal pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // Regular Expression literal
        // TODO: We need to check for this pattern in the main visitor, so we can mark the types on the names.
        case (Pattern)`<RegExpLiteral rl>` : {
            if (isStrType(rt) && isStrType(pt)) {
                list[tuple[RType,RName]] resTypes = [ ];
                list[Tree] names = prodFilter(rl, bool(Production prd) { return prod(_,lex(sort("Name")),_) := prd; });
            for (n <- names) {
                RType pt = getTypeForName(globalSTBuilder, RSimpleName("<n>"), n@\loc);
                RType t = (isInferredType(pt)) ? globalSTBuilder.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
                if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                resTypes += <rt,RSimpleName("<n>")>;
                } else if (! equivalent(t,rt)) {
                resTypes += <makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name <n> in pattern <pat>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc),RSimpleName("<n>")>;
                }           
                        }
            if (checkForFail({t | <t,_> <- resTypes})) return collapseFailTypes({t | <t,_> <- resTypes});
            if (size(resTypes) == 0 || (size(resTypes) > 0 && isStrType(lubList([t | <t,_> <- resTypes])))) return rt;
            return makeFailType("The following names in the pattern are not of type string: <[n | <t,n> <- resTypes, !isStrType(t)]>",pat@\loc);
                   } else {
               return makeFailType("Regular Expression  pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>",pat@\loc);
                   }
        }

        // Anonymous name
        // TODO: Add LubType support, just in case
        case (Pattern)`_` : {
            RType retType;
            RType t = (isInferredType(pt)) ? globalSTBuilder.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed anonymous name <pat>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", pat@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;
        }
        
        // Name
        // TODO: Add LubType support, just in case
        case (Pattern)`<Name n>`: {
            RType retType;
            RType nType = getTypeForNameLI(globalSTBuilder,convertName(n),n@\loc);
            RType t = (isInferredType(nType)) ? globalSTBuilder.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;         
        }

        // QualifiedName
        // TODO: Add LubType support, just in case
        case (Pattern)`<QualifiedName qn>`: {
            RType retType;
            RType nType = getTypeForNameLI(globalSTBuilder,convertName(qn),qn@\loc);
            RType t = (isInferredType(nType)) ? globalSTBuilder.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
            if (isInferredType(t)) {
                updateInferredTypeMappings(t,rt);
                retType = rt;
            } else if (! equivalent(t,rt)) {
                retType = makeFailType("Attempt to bind multiple types to the same implicitly typed name <qn>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(rt)>", n@\loc);
            } else {
                retType = t; // or rt, types are equal
            }
            return retType;         
        }

        // TODO: ReifiedType, see if we need to expand matching for this
        case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
            if (RReifiedType(bt) := rt) {
                return rt; // TODO: Will need to change to really get this working, just return the binder type for now
            } else {
                return makeFailType("Type of pattern, <prettyPrintType(pt)>, is not compatible with the type of the binding expression, <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // CallOrTree
        // This handles two different cases. In the first, the binding code is invoked when we handle
        // a constructor pattern to assign types to the variables. In that case, we actually have the
        // full signature of the constructor, so we have the information for each field in the
        // pattern. In the second, the binding code is invoked during a match or enumeration, so
        // we don't actually have explicit constructor types, just the ADT type. In that case, we
        // can't descend into the pattern, we just have to compare the ADT types of the constructor
        // and the type of the binding type (rt).
        case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
            list[Pattern] patternFields = [p | p <- pl];
            RType patternType = pat@fctype; // Get back the constructor type used, not the ADT types
            if (isConstructorType(patternType) && isConstructorType(rt) && size(getConstructorArgumentTypes(patternType)) == size(patternFields)) {
                set[RType] potentialFailures = { };
                list[RType] rtArgTypes = getConstructorArgumentTypes(rt); 
                for (n <- domain(rtArgTypes))
                    potentialFailures += bindInferredTypesToPattern(rtArgTypes[n],patternFields[n]);
                if (checkForFail(potentialFailures)) return collapseFailTypes(potentialFailures);
                return getConstructorResultType(patternType);
            } else if (isADTType(pt) && isADTType(rt) && subtypeOf(rt,pt)) {
                return pt; // TODO: Firm this up
            } else {
                return makeFailType("Actual type, <prettyPrintType(rt)>, is incompatible with the pattern type, <prettyPrintType(pt)>",pat@\loc);
            }
        }

        // List
        case (Pattern) `[<{Pattern ","}* pl>]` : {
            if (isListType(rt) && isListType(pt)) {
                RType plt = getListElementType(pt);
                RType rlt = getListElementType(rt);
                
                list[RType] elementTypes = [ ];
                for (p <- pl) {
                    if (isListContainerVar(p))          
                        elementTypes += bindInferredTypesToPattern(rt,p);
                    else
                        elementTypes += bindInferredTypesToPattern(rlt,p);
                }
                
                if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                
                // Get the types in the list, we need to watch for inferrence types since we need
                // to handle those separately. We also need to watch for lub types, since we could
                // propagate these up, although we should be able to resolve them at some point (maybe
                // just not yet). For instance, if we have C([ [x,_*], _* ]), when we type [x,_*] this
                // will generate a lub type, then [ [x,_*], _* ] will also generate a lub type, and it
                // will not be resolved until we reach C([ [x,_*], _*]), where we should be able to
                // determine the actual type.
                list[RType] patTypesI = [ t | t <- elementTypes, isInferredType(t) || isLubType(t) ];
                
                if (size(patTypesI) > 0) {
                    return makeListType(makeLubType(elementTypes));
                } else {
                    RType lubType = lubList(elementTypes);
                    if (subtypeOf(rlt,lubType)) {
                        return makeListType(lubType);
                    } else {
                        return makeFailType("The list element type of the subject, <prettyPrintType(rlt)>, must be a subtype of the list element type in the pattern, <prettyPrintType(lubType)>", pat@\loc);
                    }
                }
            } else {
                return makeFailType("List pattern has pattern type of <prettyPrintType(pt)> but subject type of <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // Set
        case (Pattern) `{<{Pattern ","}* pl>}` : {
            if (isSetType(rt) && isSetType(pt)) {
                RType pst = getSetElementType(pt);
                RType rst = getSetElementType(rt);
                
                list[RType] elementTypes = [ ];
                for (p <- pl) {
                    if (isSetContainerVar(p))           
                        elementTypes += bindInferredTypesToPattern(rt,p);
                    else
                        elementTypes += bindInferredTypesToPattern(rst,p);
                }
                
                if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                
                // Get the types in the set, we need to watch for inferrence types since we need
                // to handle those separately. We also need to watch for lub types, since we could
                // propagate these up, although we should be able to resolve them at some point (maybe
                // just not yet). For instance, if we have C({ {x,_*}, _* }), when we type {x,_*} this
                // will generate a lub type, then { {x,_*}, _* } will also generate a lub type, and it
                // will not be resolved until we reach C({ {x,_*}, _*}), where we should be able to
                // determine the actual type.
                list[RType] patTypesI = [ t | t <- elementTypes, hasDeferredTypes(t) ];
                
                if (size(patTypesI) > 0) {
                    return makeListType(makeLubType(elementTypes));
                } else {
                    RType lubType = lubList(elementTypes);
                    if (subtypeOf(rst,lubType)) {
                        return makeSetType(lubType);
                    } else {
                        return makeFailType("The set element type of the subject, <prettyPrintType(rst)>, must be a subtype of the set element type in the pattern, <prettyPrintType(lubType)>", pat@\loc);
                    }
                }
            } else {
                return makeFailType("Set pattern has pattern type of <prettyPrintType(pt)> but subject type of <prettyPrintType(rt)>",pat@\loc);
            }
        }

        // Tuple with just one element
        // TODO: Ensure fields persist in the match, they don't right now
        case (Pattern) `<<Pattern pi>>` : {
            if (isTupleType(rt) && isTupleType(pt)) {
                list[RType] tupleFields = getTupleFields(rt);
                if (size(tupleFields) == 1) {
                    RType resultType = bindInferredTypesToPattern(head(tupleFields),pi);
                    if (isFailType(resultType))
                        return resultType;
                    else
                        return makeTupleType([resultType]);
                } else {
                    return makeFailType("Tuple type in subject <prettyPrintType(rt)> has more fields than tuple type in pattern <pat>, <prettyPrintType(pat@rtype)>",pat@\loc);
                }
            } else {
                return makeFailType("tuple pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
            }
        }

        // Tuple with more than one element
        // TODO: Ensure fields persist in the match, they don't right now
        case (Pattern) `<<Pattern pi>, <{Pattern ","}* pl>>` : {
            if (isTupleType(rt) && isTupleType(pt)) {
                list[RType] tupleFields = getTupleFields(rt);
                list[Pattern] patternFields = [pi] + [p | p <- pl];
                
                if (size(tupleFields) == size(patternFields)) {
                    list[RType] elementTypes = [ ];
                    for (n <- [0..size(tupleFields)-1])
                        elementTypes += bindInferredTypesToPattern(tupleFields[n],patternFields[n]);
                    if (checkForFail(toSet(elementTypes))) return collapseFailTypes(toSet(elementTypes));
                    return makeTupleType(elementTypes);
                } else {
                    return makeFailType("Tuple type in subject <prettyPrintType(rt)> has a different number of fields than tuple type in pattern <pat>, <prettyPrintType(pat@rtype)>",pat@\loc);
                }
            } else {
                return makeFailType("tuple pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
            }
        }

        // Typed Variable: a variable of type t can match a subject of type rt when rt <: t
        // TODO: Special rules for scalars vs nodes/ADTs? May make sense to say they can match
        // when, with allSubtypes being the set of all possible subtypes of t,
        // size(allSubtypes(pt) inter allSubtypes(rt)) > 0, i.e., when the actual type of each,
        // which is a subtype of the static type, could be shared...
        case (Pattern) `<Type t> <Name n>` : {
            if (subtypeOf(rt,pt))
                return pt;
            else
                return makeFailType("not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
        }
        
        // Multi Variable patterns, _* and QualifiedName*
        case (Pattern)`_ *` : {
            return bindInferredTypesToMV(rt, getTypeForNameLI(globalSTBuilder,RSimpleName("_"),pat@\loc), pat);
        }
        
        case (Pattern) `<QualifiedName qn> *` : {
            return bindInferredTypesToMV(rt, getTypeForNameLI(globalSTBuilder,convertName(qn),qn@\loc), pat);
        }

        // Descendant
        // Since this pattern is inside something, we use the subject type (rt) to determine what it is
        // inside. If p is itself just an inferred type (e.g., p = / x) then we use rt to figure
        // out what x can hold, which is: the lub of all the types reachable through rt. If p has
        // a type of some sort at the top level, we check to see if that can be used inside rt.
        // If so, and if it contains inferred or deferred types, we push down a lub of the matching
        // types in rt. If so, and if it has no deferred types, we just use that type, if it can
        // occur inside rt.
        // 
        // NOTE: We actually return rt as the type of / x, not lub(reachable(rt)). This is because
        // pattern / x essentially stands in for rt in this case, if we have [_*,/ x,_*] for instance,
        // and we use this to indicate that the second position actually has an rt which we are
        // picking apart.
        case (Pattern) `/ <Pattern p>` : {
            if ( isInferredType(p@rtype) ) {
                    set[RType] rts = reachableTypes(globalSTBuilder, rt);
                RType bt = bindInferredTypesToPattern(lubSet(rts), p);
                return isFailType(bt) ? bt : rt;
            } else if ( (! isInferredType(p@rtype)) && (hasDeferredTypes(p@rtype))) {
                    set[RType] rts = reachableTypes(globalSTBuilder, rt);
                rts = { rtsi | rtsi <- rts, subtypeOf(rtsi, p@rtype)};
                RType bt = bindInferredTypesToPattern(lubSet(rts), p);
                return isFailType(bt) ? bt : rt;
            } else {
                    set[RType] rts = reachableTypes(globalSTBuilder, rt);
                if (p@rtype in rts) return rt;
                return makeFailType("Pattern type <prettyPrintType(p@rtype)> cannot appear in type <prettyPrintType(rt)>", pat@\loc);
            }
        }

        // Variable Becomes
        case (Pattern) `<Name n> : <Pattern p>` : {
            RType boundType = bindInferredTypesToPattern(rt, p);
            if (! isFailType(boundType)) {
                    RType nType = getTypeForNameLI(globalSTBuilder,convertName(n),n@\loc);
                    RType t = (isInferredType(nType)) ? globalSTBuilder.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
                    if (isInferredType(t)) {
                        updateInferredTypeMappings(t,boundType);
                        return boundType;
                    } else if (! equivalent(t,boundType)) {
                        return makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: already bound type <prettyPrintType(t)>, attempting to bind type <prettyPrintType(boundType)>", n@\loc);
                    } else {
                        return t; // or boundType, types are equal
                    }
            }
            return boundType;
        }
        
        // Typed Variable Becomes
        case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
            if (subtypeOf(rt,pt)) {
                RType resultType = bindInferredTypesToPattern(rt, p);
                if (isFailType(resultType)) return resultType;
                return pt;
            } else {
                return makeFailType("Not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
            }
        }
        
        // Guarded
        case (Pattern) `[ <Type t> ] <Pattern p>` : {
            if (subtypeOf(rt,pt)) {
                RType resultType = bindInferredTypesToPattern(rt, p);
                if (isFailType(resultType)) return resultType;
                return pt;
            } else {
                return makeFailType("Not possible to bind actual type <prettyPrintType(rt)> to pattern type <prettyPrintType(pt)>", pat@\loc);
            }
        }           
        
        // Anti -- TODO see if this makes sense, check the interpreter
        case (Pattern) `! <Pattern p>` : {
            return bindInferredTypesToPattern(rt, p);
        }
    }

    // Logic for handling maps -- we cannot directly match them, so instead we need to pick apart the tree
    // representing the map.
        // pat[0] is the production used, pat[1] is the actual parse tree contents
    if (prod(_,_,attrs([_*,term(cons("Map")),_*])) := pat[0]) {
            if (isMapType(rt) && isMapType(pt)) {
                    RType t = bindInferredTypesToMapPattern(rt, pat);
                        return t;
                } else {
                        return makeFailType("map pattern has unexpected pattern and subject types: <prettyPrintType(pt)>, <prettyPrintType(rt)>", pat@\loc);
                }
    }

    throw "Missing case on checkPattern for pattern <pat>";
}

//
// Check Pattern with Action productions
//
public RType checkPatternWithAction(PatternWithAction pat) {
    switch(pat) {
        case (PatternWithAction)`<Pattern p> => <Expression e>` : {
            if (checkForFail( { p@rtype, e@rtype } )) return collapseFailTypes( { p@rtype, e@rtype } );
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            if (!subtypeOf(e@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
            return p@rtype; 
        }
        
        case (PatternWithAction)`<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
            set[RType] whenTypes = { e@rtype | e <- es };
            if (checkForFail( whenTypes + p@rtype + er@rtype )) return collapseFailTypes( whenTypes + p@rtype + er@rtype );
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            if (!subtypeOf(er@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(er@rtype)>, must be comparable.", pat@\loc); 
            return p@rtype; 
        }
        
        case (PatternWithAction)`<Pattern p> : <Statement s>` : {
            RType stmtType = getInternalStatementType(s@rtype);
            if (checkForFail( { p@rtype, stmtType })) return collapseFailTypes( { p@rtype, stmtType });
            RType boundType = bindInferredTypesToPattern(p@rtype, p);
            if (isFailType(boundType)) return boundType;
            return stmtType;
        }
    }
    
    throw "Unhandled case in checkPatternWithAction, <pat>";    
}

//
// Check the type of the data target. This just propagates failures (for instance, from using a target
// name that is not defined), otherwise assigning a void type.
//
public RType checkDataTarget(DataTarget dt) {
    if ((DataTarget)`<Name n> :` := dt && isFailType(getTypeForName(globalSTBuilder,convertName(n),n@\loc))) 
        return getTypeForName(globalSTBuilder,convertName(n),n@\loc);     
    return makeVoidType();
}

//
// Check the type of the target. This just propagates failures (for instance, from using a target
// name that is not defined), otherwise assigning a void type.
//
public RType checkTarget(Target t) {
    if ((Target)`<Name n>` := t && isFailType(getTypeForName(globalSTBuilder,convertName(n),n@\loc))) 
        return getTypeForName(globalSTBuilder,convertName(n),n@\loc);     
    return makeVoidType();
}

// TODO: For now, just update the exact index. If we need to propagate these changes we need to make this
// code more powerful.
private void updateInferredTypeMappings(RType t, RType rt) {
    globalSTBuilder.inferredTypeMap[getInferredTypeIndex(t)] = rt;
}

// Replace inferred with concrete types
public RType replaceInferredTypes(RType rt) {
    return visit(rt) { case RInferredType(n) => globalSTBuilder.inferredTypeMap[n] };
}

//
// Calculate the list of types assigned to a list of parameters
//
public list[RType] getParameterTypes(Parameters p) {
    list[RType] pTypes = [];

    if ((Parameters)`( <Formals f> )` := p && (Formals)`<{Formal ","}* fs>` := f) {
        for ((Formal)`<Type t> <Name n>` <- fs) {
                pTypes += getTypeForName(globalSTBuilder,convertName(n),n@\loc);
        }
    } else if ((Parameters)`( <Formals f> ... )` := p && (Formals)`<{Formal ","}* fs>` := f) {
        for ((Formal)`<Type t> <Name n>` <- fs) {
                pTypes += getTypeForName(globalSTBuilder,convertName(n),n@\loc);
        }
        // For varargs, mark the last parameter as the variable size parameter; if we have no
        // parameters, then we add one, a varargs which accepts anything
        if (size(pTypes) > 0)
            pTypes[size(pTypes)-1] = RVarArgsType(pTypes[size(pTypes)-1]);
        else
            pTypes = [ RVarArgsType(makeValueType()) ];
    }

    return pTypes;
}

//
// Figure the type of value that would be assigned, based on the assignment statement
// being used. This returns a fail type if the assignment is invalid. 
//
public RType getAssignmentType(RType t1, RType t2, RAssignmentOp raOp, loc l) {
    if (aOpHasOp(raOp)) {
        RType expType = expressionType(t1,t2,opForAOp(raOp),l);
        if (isFailType(expType)) return expType;
        if (subtypeOf(expType, t1)) return t1;
        return makeFailType("Invalid assignment of type <prettyPrintType(expType)> into variable of type <prettyPrintType(t1)>",l);
    } else if (raOp in { RADefault(), RAIfDefined() }) {
        if (subtypeOf(t2,t1)) {
            return t1;
        } else {
            return makeFailType("Invalid assignment of type <prettyPrintType(t2)> into variable of type <prettyPrintType(t1)>",l);
        }
    } else {
        throw "Invalid assignment operation: <raOp>";
    }
}

// TODO: We need logic that caches the signatures on the parse trees for the
// modules. Until then, we load up the signatures here...
public SignatureMap populateSignatureMap(list[Import] imports) {

    str getNameOfImportedModule(ImportedModule im) {
        switch(im) {
            case (ImportedModule)`<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` : {
                return prettyPrintName(convertName(qn));
            }
            case (ImportedModule)`<QualifiedName qn> <ModuleActuals ma>` : {
                return prettyPrintName(convertName(qn));
            }
            case (ImportedModule)`<QualifiedName qn> <Renamings rn>` : {
                return prettyPrintName(convertName(qn));
            }
            case (ImportedModule)`<QualifiedName qn>` : {
                return prettyPrintName(convertName(qn));
            }
        }
        throw "getNameOfImportedModule: invalid syntax for ImportedModule <im>, cannot get name";
    }


    SignatureMap sigMap = ( );
    for (i <- imports) {
        if ((Import)`import <ImportedModule im> ;` := i || (Import)`extend <ImportedModule im> ;` := i) {
            Tree importTree = getModuleParseTree(getNameOfImportedModule(im));
            sigMap[i] = getModuleSignature(importTree);
        } 
    }

    return sigMap;
}

private STBuilder globalSTBuilder = createNewSTBuilder();

// Check to see if the cases given cover the possible matches of the expected type.
// If a default is present this is automatically true, else we need to look at the
// patterns given in the various cases. 
public bool checkCaseCoverage(RType expectedType, Case+ options, STBuilder table) {
    set[Case] defaultCases = { cs | cs <- options, (Case)`default: <Statement b>` := cs };
    if (size(defaultCases) > 0) return true;    
    
    set[Pattern] casePatterns = { p | cs <- options, (Case)`case <Pattern p> => <Replacement r>` := cs || (Case)`case <Pattern p> : <Statement b>` := cs };
    return checkPatternCoverage(expectedType, casePatterns, table);     
}

// Check to see if the patterns in the options set cover the possible matches of the
// expected type. This can be recursive, for instance with ADT types.
// TODO: Need to expand support for matching over reified types 
public bool checkPatternCoverage(RType expectedType, set[Pattern] options, STBuilder table) {

    // Check to see if a given use of a name is the same use that defines it. A use is the
    // defining use if, at the location of the name, there is a use of the name, and that use
    // is also the location of the definition of a new item.
    bool isDefiningUse(Name n, STBuilder table) {
        loc nloc = n@\loc;
        return false;
    }

    // Take a rough approximation of whether a pattern completely covers the given
    // type. This is not complete, since some situations where this is true will return
    // false here, but it is sound, in that any time we return true it should be the
    // case that the pattern actually covers the type.
    bool isDefiningPattern(Pattern p, RType expectedType, STBuilder table) {
        if ((Pattern)`_` := p) {
            return true;
        } else if ((Pattern)`<Name n>` := p && isDefiningUse(n, table)) {
            return true;
        } else if ((Pattern)`<Type t> _` := p && convertType(t) == expectedType) {
            return true;
        } else if ((Pattern)`<Type t> <Name n>` := p && isDefiningUse(n, table) && convertType(t) == expectedType) {
            return true;
        } else if ((Pattern)`<Name n> : <Pattern pd>` := p) {
            return isDefiningPattern(pd, expectedType, table);
        } else if ((Pattern)`<Type t> <Name n> : <Pattern pd>` := p && convertType(t) == expectedType) {
            return isDefiningPattern(pd, expectedType, table);
        } else if ((Pattern)`[ <Type t> ] <Pattern pd>` := p && convertType(t) == expectedType) {
            return isDefiningPattern(pd, expectedType, table);
        }
        
        return false;
    }
    
    // Check to see if a 0 or more element pattern is empty (i.e., contains no elements)
    bool checkEmptyMatch({Pattern ","}* pl, RType expectedType, STBuilder table) {
        return size([p | p <- pl]) == 0;
    }

    // Check to see if a 0 or more element pattern matches an arbitrary sequence of zero or more items;
    // this means that all the internal patterns have to be of the form x*, like [ xs* ys* ], since this
    // still allows 0 items total
    bool checkTotalMatchZeroOrMore({Pattern ","}* pl, RType expectedType, STBuilder table) {
        list[Pattern] plst = [p | p <- pl];
        set[bool] starMatch = { (Pattern)`<QualifiedName qn>*` := p | p <- pl };
        return (! (false in starMatch) );
    }

    // Check to see if a 1 or more element pattern matches an arbitrary sequence of one or more items;
    // this would be something like [xs* x ys*]. If so, recurse to make sure this pattern actually covers
    // the element type being matched. So, [xs* x ys*] := [1..10] would cover (x taking 1..10), but
    // [xs* 3 ys*] would not (even though it matches here, there is no static guarantee it does without
    // checking the values allowed on the right), and [xs* 1000 ys*] does not (again, no static guarantees,
    // and it definitely doesn't cover the example here).
    bool checkTotalMatchOneOrMore({Pattern ","}* pl, RType expectedType, STBuilder table) {
        list[Pattern] plst = [p | p <- pl];
        set[int] nonStarMatch = { n | n <- domain(plst), ! (Pattern)`<QualifiedName qn>*` := plst[n] };
        return (size(nonStarMatch) == 1 && isDefiningPattern(plst[getOneFrom(nonStarMatch)], expectedType, table));
    }
    
    if (isBoolType(expectedType)) {
        // For booleans, check to see if either a) a variable is given which could match either
        // true or false, or b) both the constants true and false are explicitly given in two cases
        bool foundTrue = false; bool foundFalse = false;
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) 
                return true;
            else if ((Pattern)`true` := p) 
                foundTrue = true;
            else if ((Pattern)`false` := p) 
                foundFalse = true;
            if (foundTrue && foundFalse) return true; 
        }
        return false;
    } else if (isIntType(expectedType) || isRealType(expectedType) || isNumType(expectedType) || isStrType(expectedType) || isValueType(expectedType) || isLocType(expectedType) || isLexType(expectedType) || isReifiedType(expectedType) || isDateTimeType(expectedType)) {
        // For int, real, num, str, value, loc, lex, datetime, and reified types, just check to see
        // if a variable if given which could match any value of these types.
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
        }
        return false;           
    } else if (isListType(expectedType)) {
        // For lists, check to see if either a) a variable which could represent the entire list is given, or
        // b) the list is used explicitly, but the patterns given inside the list cover all the cases.
        // TODO: At this point, we do a simple check here for b). Either we have a variable which can represent
        // 0 or more items inside the list, or we have a case with the empty list and a case with 1 item in the
        // list. We don't check anything more advanced, but it would be good to.
        bool foundEmptyMatch = false; bool foundTotalMatch = false; bool foundSingleMatch = false;
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
            if ((Pattern)`[<{Pattern ","}* pl>]` := p) {
                RType listElementType = getListElementType(expectedType);
                if (!foundEmptyMatch) foundEmptyMatch = checkEmptyMatch(pl, listElementType, table);
                if (!foundTotalMatch) foundTotalMatch = checkTotalMatchZeroOrMore(pl, listElementType, table);
                if (!foundSingleMatch) foundSingleMatch = checkTotalMatchOneOrMore(pl, listElementType, table);
            }
            if (foundTotalMatch || (foundEmptyMatch && foundSingleMatch)) return true;
        }
        return false;
    } else if (isSetType(expectedType)) {
        bool foundEmptyMatch = false; bool foundTotalMatch = false; bool foundSingleMatch = false;
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
            if ((Pattern)`{<{Pattern ","}* pl>}` := p) {
                RType setElementType = getSetElementType(expectedType);
                if (!foundEmptyMatch) foundEmptyMatch = checkEmptyMatch(pl, setElementType, table);
                if (!foundTotalMatch) foundTotalMatch = checkTotalMatchZeroOrMore(pl, setElementType, table);
                if (!foundSingleMatch) foundSingleMatch = checkTotalMatchOneOrMore(pl, setElementType, table);
            }
            if (foundTotalMatch || (foundEmptyMatch && foundSingleMatch)) return true;
        }
        return false;
    } else if (isMapType(expectedType)) {
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
        }
        return false;                   
    } else if (isRelType(expectedType)) {
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
        }
        return false;               
    } else if (isTupleType(expectedType)) {
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
        }
        return false;               
    } else if (isADTType(expectedType)) {
            println("Checking case coverage for ADT type <expectedType>");
        for (p <- options) {
            if (isDefiningPattern(p, expectedType, table)) return true;
        }
        return false;               
    }

    return false;   
}

//
// Check a file, given the path to the file
//
public Tree typecheckFile(str filePath) {
    loc l = |file://<filePath>|;
    Tree t = parse(#Module,l);
    return typecheckTree(t);
}

//
// Check a tree
//
public Tree typecheckTree(Tree t) {
    println("TYPE CHECKER: Getting Imports for Module");
    list[Import] imports = getImports(t);
    println("TYPE CHECKER: Got Imports");
    
    println("TYPE CHECKER: Generating Signature Map");
    SignatureMap sigMap = populateSignatureMap(imports);
    println("TYPE CHECKER: Generated Signature Map");
    
    println("TYPE CHECKER: Generating Symbol Table"); 
    globalSTBuilder = buildNamespace(t, sigMap);
    println("TYPE CHECKER: Generated Symbol Table");
    
    println("TYPE CHECKER: Type Checking Module");
    Tree tc = check(t);
    println("TYPE CHECKER: Type Checked Module");
    
    println("TYPE CHECKER: Retagging Names with Type Information");
    tc = retagNames(tc);
    println("TYPE CHECKER: Retagged Names");
    
    if (isFailType(tc@rtype)) tc = tc[@messages = { error(l,s) | RFailType(allFailures) := tc@rtype, <s,l> <- allFailures }];
    if (debug && isFailType(tc@rtype)) {
        println("TYPE CHECKER: Found type checking errors");
        for (RFailType(allFailures) := tc@rtype, <s,l> <- allFailures) println("<l>: <s>");
    }
    return tc;
}


public STBuilder justGenerateTable(Tree t) {
    println("TYPE CHECKER: Getting Imports for Module");
    list[Import] imports = getImports(t);
    println("TYPE CHECKER: Got Imports");
    
    println("TYPE CHECKER: Generating Signature Map");
    SignatureMap sigMap = populateSignatureMap(imports);
    println("TYPE CHECKER: Generated Signature Map");
    
    println("TYPE CHECKER: Generating Symbol Table"); 
    stBuilder = buildNamespace(t, sigMap);
    println("TYPE CHECKER: Generated Symbol Table");
    
    return stBuilder;
}

public Tree typecheckTreeWithExistingTable(STBuilder stBuilder, Tree t) {
    globalSTBuilder = stBuilder;
    
    println("TYPE CHECKER: Type Checking Module");
    Tree tc = check(t);
    println("TYPE CHECKER: Type Checked Module");
    
    println("TYPE CHECKER: Retagging Names with Type Information");
    tc = retagNames(tc);
    println("TYPE CHECKER: Retagged Names");
    
    if (isFailType(tc@rtype)) tc = tc[@messages = { error(l,s) | RFailType(allFailures) := tc@rtype, <s,l> <- allFailures }];
    if (debug && isFailType(tc@rtype)) {
        println("TYPE CHECKER: Found type checking errors");
        for (RFailType(allFailures) := tc@rtype, <s,l> <- allFailures) println("<l>: <s>");
    }
    return tc;
}
