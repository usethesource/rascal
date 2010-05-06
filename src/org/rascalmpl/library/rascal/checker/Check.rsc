module rascal::checker::Check

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;

import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::Namespace;
import rascal::checker::TypeRules;
import rascal::checker::ScopeInfo;
import rascal::checker::Signature;

import rascal::\old-syntax::Rascal;

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
// 4. Handle polymorphic type variables
//
// 5. Need to set up a large number of test cases!
//
// 6. Add checking for tags
//
// 7. Handle embedded TODOs below and in other files; these are generally less common type checking cases that we
//    still need to handle.
//

private str getTypeString(Name n) {
	if ( (n@rtype)? )
		return "TYPE: " + prettyPrintType(n@rtype);
	else
		return "TYPE unavailable";
}
 
public Name setUpName(Name n) {
	if ( (n@rtype) ?) {
		RType t = checkName(n);
		n = n[@rtype = t];
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
public Tree check(Tree t, RType adtErrors) {
	return visit(t) {
		case Name n => setUpName(n)
		case Expression e : { 
			RType expType = checkExpression(e); 
			if (e@\loc in globalScopeInfo.itBinder) 
				updateInferredTypeMappings(globalScopeInfo.itBinder[e@\loc], expType); 
				insert e[@rtype = expType]; 
		}
		case Pattern p => p[@rtype = checkPattern(p)]
		case Statement s => s[@rtype = checkStatement(s)]
		case Assignable a => a[@rtype = checkAssignable(a)]
		case Catch c => c[@rtype = checkCatch(c)]
		case DataTarget dt => dt[@rtype = checkDataTarget(dt)]
		case PatternWithAction pwa => pwa[@rtype = checkPatternWithAction(pwa)]
		case Visit v => v[@rtype = checkVisit(v)]
		case Label l => l[@rtype = checkLabel(l)]
		case Variable v => v[@rtype = checkVariable(v)]
		case FunctionBody fb => fb[@rtype = checkFunctionBody(fb)]
		case Toplevel t => t[@rtype = checkToplevel(t)]
		case Body b => b[@rtype = checkModuleBody(b)]
		case Module m : {
			RType modType = checkModule(m);
			if (isVoidType(adtErrors))
				insert m[@rtype = modType];
			else
				insert m[@rtype = collapseFailTypes(adtErrors,modType)];
		}
		case Case c => c[@rtype = checkCase(c)]
	} 
}

//
// Check the names in the tree, "fixing" any inferred types with the
// type calculated during type checking.
//
public Tree retagNames(Tree t) {
	return visit(t) {
		case Name n => setUpName(n)
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

// Names can have inferred types, which may have been assigned actual non-inferred
// types. checkName wraps this logic, looking up the inferred type in the scope
// information map.
public RType checkName(Name n) {
	if (isInferredType(n@rtype)) {
		return globalScopeInfo.inferredTypeMap[getInferredTypeIndex(n@rtype)];
	}
	return n@rtype;
}

// To check a module, currently we just propagate up errors on the body. In the future,
// we may need to make use of the module header as well (a possible TODO).
public RType checkModule(Module m) {
	if ((Module) `<Header h> <Body b>` := m) {
		return b@rtype;
	}
	throw "checkModule: unexpected module syntax";
}

// Since checking is a bottom-up process, checking the module body just consists of bubbling any errors
// that have occurred in nested declarations up to the top level.
public RType checkModuleBody(Body b) {
	set[RType] modItemTypes = { };
	if (`<Toplevel* ts>` := b) modItemTypes = { t@rtype | t <- ts, ( (t@rtype)?) };
	if (size(modItemTypes) > 0 && checkForFail(modItemTypes)) return collapseFailTypes(modItemTypes);
	return makeVoidType();
}

// Checking the toplevel items involves propagating up any failures detected in the items.
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
			return checkTest(tst);
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

// checkVarItems checks for failure types assigned to the variables, either returning
// these or a void type. Failures would come from duplicate use of a variable name
// or other possibel scoping errors.
public RType checkVarItems(Tags ts, Visibility vis, Type t, {Variable ","}+ vs) {
	if (debug) println("CHECKER: In checkVarItems for variables <vs>");
	set[RType] varTypes = { v@rtype | v <- vs };
	if (checkForFail( varTypes )) return collapseFailTypes( varTypes );
	return makeVoidType();
}

// checkVariable checks the correctness of the assignment where the variable is of the
// form n = e and also returns either a failure type of the type assigned to the name.
public RType checkVariable(Variable v) {
	switch(v) {
		case (Variable) `<Name n>` : 	return n@rtype;

		case (Variable) `<Name n> = <Expression e>` : {
			if (checkForFail( { n@rtype, e@rtype })) return collapseFailTypes({ n@rtype, e@rtype });

			if (isInferredType(n@rtype)) {
				RType mappedType = globalScopeInfo.inferredTypeMap[getInferredTypeIndex(n@rtype)];
				if (isInferredType(mappedType)) {
					if (debug) println("CHECKER: Updating binding of inferred type <prettyPrintType(mappedType)> to <prettyPrintType(e@rtype)> on name <n>");
					updateInferredTypeMappings(mappedType,e@rtype);
					return e@rtype;
				} else {
					if (mappedType != e@rtype) {
						if (debug) println("CHECKER: Found type clash on inferred variable, trying to assign <prettyPrintType(e@rtype)> and <prettyPrintType(mappedType)> to name <n>");
						return makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: <prettyPrintType(e@rtype)>, <prettyPrintType(mappedType)>", v@\loc);
					} else {
						return e@rtype;
					}
				}
			} else {
				return getAssignmentType( n@rtype, e@rtype, RADefault(), v@\loc );
			}
		}
	}

	throw "checkVariable: unhandled variable case <v>";
}

// The type of a function is fail if the parameters have fail types, else it is based on the
// return and parameter types (and is already assigned to n, the function name).
public RType checkAbstractFunction(Tags ts, Visibility v, Signature s) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
			return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : n@rtype;
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
			return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : n@rtype;
	}
	throw "checkAbstractFunction: unhandled signature <s>";
}

// The type of a function is fail if the body or parameters have fail types, else it is
// based on the return and parameter types (and is already assigned to n, the function name).
public RType checkFunction(Tags ts, Visibility v, Signature s, FunctionBody b) {
	switch(s) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
			return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : n@rtype;
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
			return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : n@rtype;
	}
	throw "checkFunction: unhandled signature <s>";
}

// The type of the function body is a failure if any of the statements is a failure type,
// else it is just void. Function bodies don't have types based on the computed results.
public RType checkFunctionBody(FunctionBody fb) {
	if (`{ <Statement* ss> }` := fb) {
		set[RType] bodyTypes = { getInternalStatementType(s@rtype) | s <- ss };
		if (checkForFail(bodyTypes)) return collapseFailTypes(bodyTypes);
		return makeVoidType();
	}
	throw "checkFunctionBody: Unexpected syntax for function body <fb>";
}

// If the name has a type annotation, return it, else just return a void type. A type
// on the name would most likely indicate a scope error.
public RType checkAnnotationDeclaration(Tags t, Visibility v, Type t, Type ot, Name n) {
	if ( (n@rtype)? ) return n@rtype; else return makeVoidType();
}

// If the name has a type annotation, return it, else just return a void type. A type
// on the name would most likely indicate a scope error.
public RType checkTagDeclaration(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts) {
	if ( (n@rtype)? ) return n@rtype; else return makeVoidType();
}
	
// The type of the rule is the failure type on the name of pattern if either is a
// failure type, else it is the type of the pattern (i.e., type type of the term
// rewritten by the rule).							
public RType checkRuleDeclaration(Tags t, Name n, PatternWithAction p) {
	if ( (n@rtype)? ) {
		if (checkForFail({ n@rtype, p@rtype })) return collapseFailTypes({n@rtype, p@rtype});
	} 
	return p@rtype;
}

// TODO: Implement
public RType checkTestDeclaration(Test t) {
	throw "checkTestDeclaration not yet implemented";
}

//
// The only possible error is on the ADT name itself, so check that for failures.
//
public RType checkAbstractADT(Tags ts, Visibility v, UserType adtType) {
	Name adtn = getUserTypeRawName(adtType);
	if ( (adtn@rtype)? ) return adtn@rtype;
	return makeVoidType();
}

//
// Propagate upwards any errors registered on the ADT name or on the variants.
//
public RType checkADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars) {
	if (debug) println("CHECKER: Checking adt <prettyPrintType(convertType(adtType))>");
	set[RType] adtTypes = { };

	Name adtn = getUserTypeRawName(adtType);
	if ( (adtn@rtype)? ) adtTypes += adtn@rtype;

	for (`<Name n> ( <{TypeArg ","}* args> )` <- vars) {
		if ( (n@rtype)? ) adtTypes += n@rtype;
	}

	if (checkForFail(adtTypes)) return collapseFailTypes(adtTypes);
	return makeVoidType();
}

// Return any type registered on the alias name, else return a void type. Types on the name
// most likely represent a scoping error.
public RType checkAlias(Tags ts, Visibility v, UserType aliasType, Type aliasedType) {
	if (debug) println("CHECKER: Checking alias <prettyPrintType(convertType(aliasType))>");
	Name aliasRawName = getUserTypeRawName(aliasType);
	if ( (aliasRawName@rtype)? ) return aliasRawName@rtype;
	return makeVoidType();
}

// TODO: Implement
// public ScopeInfo checkView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts) {
//	throw "checkView not yet implemented";
// }

//
// START OF STATEMENT CHECKING LOGIC
//

public RType checkSolveStatement(Statement sp, {QualifiedName ","}+ vars, Bound b, Statement body) {
	RType boundType = makeVoidType();
	if (`; <Expression e>` := b) boundType = e@rtype;

	RType bodyType = getInternalStatementType(body@rtype);

	if (checkForFail( { v@rtype | v <- vars } + boundType + bodyType))
		return makeStatementType(collapseFailTypes({ v@rtype | v <- vars } + boundType + bodyType));

	return body@rtype;
}

// TODO: This would be a good target for static analysis to come up with a better
// type. The only safe type is list[void], since we don't know if the loop will
// actually execute. This applies to the while loop as well.
public RType checkForStatement(Statement sp, Label l, {Expression ","}+ exps, Statement body) {
	set[Expression] expSet = { e | e <- exps };
	RType bodyType = getInternalStatementType(body@rtype);

	if (checkForFail( { e@rtype | e <-expSet } + l@rtype + bodyType))
		return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + bodyType));

	return makeStatementType(makeListType(makeVoidType()));  
}  

// TODO: See checkForStatement above.
public RType checkWhileStatement(Statement sp, Label l, {Expression ","}+ exps, Statement body) {
	set[Expression] expSet = { e | e <- exps };
	RType bodyType = getInternalStatementType(body@rtype);

	if (checkForFail( { e@rtype | e <-expSet } + l@rtype + bodyType))
		return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + bodyType));

	return makeStatementType(makeListType(makeVoidType()));  
}

// TODO: Setting to list[void] for now. If we have an append inside the body, we can set it
// to list[value], which would be safe. It may be good to analyze the loop body to find
// a more accurate type.
public RType checkDoWhileStatement(Statement sp, Label l, Statement body, Expression e) {
	RType bodyType = getInternalStatementType(body@rtype);

	if (checkForFail( { l@rtype, bodyType, e@rtype }))
		return makeStatementType(collapseFailTypes({ l@rtype, bodyType, e@rtype }));

	return makeStatementType(makeListType(makeVoidType()));  
}

// Typecheck an if/then/else statement. The result type is the lub of the types of each 
// branch (since at least one of the branches will have executed)
public RType checkIfThenElseStatement(Statement s, Label l, {Expression ","}+ exps, Statement trueBody, Statement falseBody) {
	set[Expression] expSet = { e | e <- exps };
	RType trueBodyType = getInternalStatementType(trueBody@rtype);
	RType falseBodyType = getInternalStatementType(falseBody@rtype);
	if (checkForFail( { e@rtype | e <-expSet } + l@rtype + trueBodyType + falseBodyType))
		return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + trueBodyType + falseBodyType));
	return makeStatementType(lub(trueBodyType, falseBodyType));
}

// Typecheck an if/then statement without an else. The result type is always void (since the body
// of the if may not execute).
public RType checkIfThenStatement(Statement s, Label l, {Expression ","}+ exps, Statement trueBody) {
	set[Expression] expSet = { e | e <- exps };
	RType trueBodyType = getInternalStatementType(trueBody@rtype);
	if (checkForFail( { e@rtype | e <-expSet } + l@rtype + trueBodyType))
		return makeStatementType(collapseFailTypes({ e@rtype | e <-expSet } + l@rtype + trueBodyType));
	return makeStatementType(makeVoidType());
}

// Calculate the type of a switch statement and check for failures. The result type of the switch is the
// lub of the types of the various cases.
//
// TODO: How should subtypes factor in here?
//
// TODO: Need to check for a default case or case coverage. If we could have
// a switch element with no matching case, we really should return a void
// type here. So, returning one for now until we can do a better analysis.
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
		if (checkCaseCoverage(e@rtype, cases, globalScopeInfo)) {			
			return makeStatementType(makeVoidType());
		} else {
			return makeStatementType(makeFailType("The switch does not include a default case and the cases may not cover all possibilities.", sp@\loc));
		}
	} else {
		return makeStatementType(collapseFailTypes(caseFailures));
	}
} 

public bool isDefaultCase(Case c) {
	return `default : <Statement b>` := c;
}

// Given a non-default case, get the type assigned to the case pattern P, with P => or P :
public RType getCasePatternType(Case c) {
	switch(c) {
		case `case <Pattern p> => <Expression e>` : return p@rtype;
		case `case <Pattern p> => <Expression er> when <{Expression ","}+ es>` : return p@rtype;
		case `case <Pattern p> : <Statement s>` : return p@rtype;
		case `default : <Statement b>` : throw "getCaseExpType should not be called on default case";
	}	 
}

// The type of the visit is calculated elsewhere; the statement simply returns that
// type unless the label is a fail type (which can happen when the same label name
// is reused).
public RType checkVisitStatement(Statement sp, Label l, Visit v) {
	if (checkForFail({ l@rtype, v@rtype}))
		return makeStatementType(collapseFailTypes({ l@rtype, v@rtype }));
	return makeStatementType(v@rtype);
}			

// Type checks the various cases for assignment statements. Note that this assumes that the
// assignable and the statement have themselves already been checked and assigned types.
public RType checkAssignmentStatement(Statement sp, Assignable a, Assignment op, Statement s) {
	RType stmtType = getInternalStatementType(s@rtype);
	if (checkForFail({ a@rtype, stmtType })) return makeStatementType(collapseFailTypes({ a@rtype, getInternalStatementType(s@rtype) }));
	RType partType = getPartType(a@rtype);
	RType wholeType = getWholeType(a@rtype);

	// This works over two cases. For both = and ?=, the variable(s) on the left can be inference vars.
	// Otherwise, they cannot, since we are doing some kind of calculation using them and, therefore,
	// they must have been initialized (and assigned types) earlier.
	// NOTE: In these cases, where the assignable is either a name or a tuple, the partType and wholeType
	// are the same. In the other cases, where the assignable is a subscript, etc, they differ, but in those
	// cases we cannot bind into inference variables, since we assume that the assignable already has
	// a non-inferred type (or we could not look up the field, annotation, subscript, etc). So, we do
	// not have to worry about binding the inferrence vars and then "reinserting" the part into the whole.
	if (!aOpHasOp(convertAssignmentOp(op))) {
		if (subtypeOf(stmtType, partType)) {
			return makeStatementType(bindInferredTypesToAssignable(stmtType, a));
		} else {
			return makeStatementType(makeFailType("Invalid assignment, the type being assigned, <prettyPrintType(stmtType)>, must be a subtype of the allowed assignment type, <prettyPrintType(partType)>",sp@\loc));
		}
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

// An assert without a message should have expression type bool.
public RType checkAssertStatement(Statement sp, Expression e) {
	if (isBoolType(e@rtype)) {
		return makeStatementType(makeVoidType());
	} else if (isFailType(e@rtype)) {
		return makeStatementType(e@rtype);
	} else {
		return makeStatementType(makeFailType("Expression in assert should have type bool, but instead has type <prettyPrintType(e@rtype)>", sp@\loc));
	}
}

// An assert with a message should have expression types bool : str .
public RType checkAssertWithMessageStatement(Statement sp, Expression e, Expression em) {
	if (isBoolType(e@rtype) && isStrType(em@rtype)) {
		return makeStatementType(rt);
	} else if (checkForFail({e@rtype, em@rtype})) {
		return makeStatementType(collapseFailTypes({e@rtype,em@rtype}));
	} else if (isBoolType(e@rtype) && !isStrType(em@rtype)) {
		return makeStatementType(makeFailType("Right-hand expression in assert should have type str, but instead has type <prettyPrintType(em@rtype)>", sp@\loc));
	} else if (!isBoolType(e@rtype) && isStrType(em@rtype)) {
		return makeStatementType(makeFailType("Left-hand expression in assert should have type bool, but instead has type <prettyPrintType(e@rtype)>", sp@\loc));
	} else {
		return makeStatementType(makeFailType("Assert should have types bool : str, but instead has types <prettyPrintType(e@rtype)> : <prettyPrintType(em@rtype)>", sp@\loc));
	}
}

// Checking the return statement requires checking to ensure that the returned type is the same
// as the type of the function. We could do that at the function level, using a visitor (like we
// do to check part of the visit), but do it this way instead since it should be faster, at
// the expense of maintaining additional data structures.
public RType checkReturnStatement(Statement sp, Statement b) {
	RType stmtType = getInternalStatementType(b@rtype);
	RType retType = getFunctionReturnType(globalScopeInfo.returnTypeMap[sp@\loc]);
	if (debug) println("CHECKER: Verifying return type <prettyPrintType(retType)>");
	if (isFailType(stmtType)) {
		return makeStatementType(stmtType);
	} else if (subtypeOf(stmtType,retType)) {
		if (debug) println("CHECKER: Returning type <prettyPrintType(retType)>");
		return makeStatementType(retType);
	} else {
		return makeStatementType(makeFailType("Type of return, <prettyPrintType(stmtType)>, must be a subtype of function return type, <prettyPrintType(retType)>", sp@\loc));
	} 
}

// Checking a local function statement just involves propagating any failures or, if there are no failures,
// returning the type already assigned (in the scope generation) to the name.
public RType checkLocalFunctionStatement(Statement sp, Tags ts, Visibility v, Signature sig, FunctionBody fb) {
	switch(sig) {
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
			return checkForFail(toSet(getParameterTypes(ps)) + fb@rtype) ? makeStatementType(collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype)) : makeStatementType(n@rtype);
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
			return checkForFail(toSet(getParameterTypes(ps)) + fb@rtype) ? makeStatementType(collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype)) : makeStatementType(n@rtype);
	}
	throw "checkFunction: unhandled signature <sig>";
}

// Typecheck a try/catch statement. Currently the behavior of the interpreter returns the value of the body if 
// the body exits correctly, or an undefined value if a throw occurs in the body. For now, type this as void,
// but TODO: check to see if we want to maintain this behavior.
public RType checkTryCatchStatement(Statement sp, Statement body, Catch+ catches) {
		set[Catch] catchSet = { c | c <- catches };
		set[RType] catchTypes = { getInternalStatementType(c) | c <- catchSet };
		RType bodyType = getInternalStatementType(body@rtype);
		if (checkForFail( catchTypes + bodyType))
			return makeStatementType(collapseFailType( catchTypes + bodyType ));

		return makeStatementType(makeVoidType());
}		

// Typecheck a try/catch/finally statement. See the comments for the try/catch statement for added details.
public RType checkTryCatchFinallyStatement(Statement sp, Statement body, Catch+ catches, Statement fBody) {
		set[Catch] catchSet = { c | c <- catches };
		set[RType] catchTypes = { getInternalStatementType(c) | c <- catchSet };
		RType bodyType = getInternalStatementType(body@rtype);
		RType finallyType = getInternalStatementType(fBody@rtype);
		if (checkForFail( catchTypes + bodyType + finallyType ))
			return makeStatementType(collapseFailType( catchTypes + bodyType + finallyType ));

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
		case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` : {
			if (debug) println("CHECKER: Inside solve statement <s>");
			RType rt = checkSolveStatement(s,vs,b,sb);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			if (debug) println("CHECKER: Inside for statement <s>");
			RType rt = checkForStatement(s,l,es,b);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			if (debug) println("CHECKER: Inside while statement <s>");
			RType rt = checkWhileStatement(s,l,es,b);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			if (debug) println("CHECKER: Inside do statement <s>");
			RType rt = checkDoWhileStatement(s,l,b,e);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			if (debug) println("CHECKER: Inside if with else statement <s>");
			RType rt = checkIfThenElseStatement(s,l,es,bt,bf);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			if (debug) println("CHECKER: Inside if statement <s>");
			RType rt = checkIfThenStatement(s,l,es,bt);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			if (debug) println("CHECKER: Inside switch statement <s>");
			RType rt = checkSwitchStatement(s,l,e,cs);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case (Statement)`<Label l> <Visit v>` : {
			if (debug) println("CHECKER: Inside visit statement <s>");
			RType rt = checkVisitStatement(s,l,v);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Expression e> ;` : {
			if (debug) println("CHECKER: Inside expression statement <s>");
			RType rt = makeStatementType(e@rtype);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			if (debug) println("CHECKER: Inside assignment statement <s>");
			RType rt = checkAssignmentStatement(s,a,op,b);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `assert <Expression e> ;` : {
			if (debug) println("CHECKER: Inside assert statement <s>");
			RType rt = checkAssertStatement(s, e);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `assert <Expression e> : <Expression em> ;` : {
			if (debug) println("CHECKER: Inside assert with message statement <s>");
			RType rt = checkAssertWithMessageStatement(s, e, em);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		// This uses a special lookup table which cached the type of the function in scope at the
		// time of the return.
		// TODO: Extract this out into a separate function.
		case `return <Statement b>` : {
			if (debug) println("CHECKER: Inside return statement <s>");
			RType rt = checkReturnStatement(s, b);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		// TODO: Need to add RuntimeException to a default "type store" so we can use it
		// TODO: Modify to properly check the type of b; should be a subtype of RuntimeException
		case `throw <Statement b>` : {
			if (debug) println("CHECKER: Inside throw statement <s>");
			RType rt = b@rtype;
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		// TODO: Need to verify that statement has same type as current subject in visit or rewrite rule
		case `insert <DataTarget dt> <Statement b>` : {
			if (debug) println("CHECKER: Inside insert statement <s>");
			RType st = getInternalStatementType(b@rtype);
			RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			if (debug) println("CHECKER: Inside append statement <s>");
			RType st = getInternalStatementType(b@rtype);
			RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			if (debug) println("CHECKER: Inside local function statement <s>");
			RType rt = checkLocalFunctionStatement(s,ts,v,sig,fb);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			if (debug) println("CHECKER: Inside local variable statement <s>");
			RType rt = checkLocalVarItems(s, vs);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		// TODO: Handle the dynamic part of dynamic vars		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			if (debug) println("CHECKER: Inside dynamic local variable statement <s>");
			RType rt = checkLocalVarItems(s, vs);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `break <Target t> ;` : {
			if (debug) println("CHECKER: Inside break statement <s>");
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `fail <Target t> ;` : {
			if (debug) println("CHECKER: Inside fail statement <s>");
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `continue <Target t> ;` : {
			if (debug) println("CHECKER: Inside continue statement <s>");
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `try <Statement b> <Catch+ cs>` : {
			if (debug) println("CHECKER: Inside try without finally statement <s>");
			RType rt = checkTryCatchStatement(s,b,cs);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}

		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			if (debug) println("CHECKER: Inside try with finally statement <s>");
			RType rt = checkTryCatchFinallyStatement(s,b,cs,bf);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			if (debug) println("CHECKER: Inside block statement <s>");
			RType rt = checkBlockStatement(s, l, bs);
			if (debug) println("CHECKER: Returning type <prettyPrintType(rt)>");
			return rt;
		}
	}
	
	throw "Unhandled type checking case in checkStatement for statement <s>";
}

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
	RType resultType = makeFailType("We assume bad, bad things!",ep@\loc);

	// First, if we have any failures, just propagate those upwards, don't bother to
	// check the rest of the call.
	// TODO: We may want to check arity, etc anyway, since we could catch errors
	// where no function or constructor could possibly match.	
	if (checkForFail({ ec@rtype } + { e@rtype | e <- es }))
		return collapseFailTypes({ ec@rtype } + { e@rtype | e <- es });
			
	// We can have overloaded functions and overloaded data constructors. If the type
	// is overloaded, we need to check each overloading to find the one that works.
	// TODO: We should codify the rules for resolving overloaded functions, since
	// we need those rules here. It should be the case that the most specific function
	// wins, but we probably also need errors to indicate when definitions make this
	// impossible, like f(int,value) versus f(value,int).
	
	// Set up the possible alternatives. We will treat the case of no overloads as a trivial
	// case of overloading with only one alternative.
	set[RType] alternatives = isOverloadedType(ec@rtype) ? getOverloadOptions(ec@rtype) : { ec@rtype };
	
	// Now, try each alternative, seeing if one matches.
	list[Expression] args = [ e | e <- es ];
	for (a <- alternatives) {
		if (debug) println("CHECKER: Checking alternative <prettyPrintType(a)> in call <ep>");
		
		RType potentialResultType;
		list[RType] argTypes = [];
		
		if (isFunctionType(a)) {
			argTypes = getFunctionArgumentTypes(a);
			potentialResultType = getFunctionReturnType(a);
		} else if (isConstructorType(a)) {
			argTypes = getConstructorArgumentTypes(a);
			potentialResultType = getConstructorResultType(a);
		} else {
			potentialResultType = makeFailType("Type <prettyPrintType(a)> is not a function or constructor type.",ep@\loc);
		}
		
		if (isFunctionType(a) || isConstructorType(a)) {
			if (size(argTypes) == size(args)) {
				for (e <- args) {
					RType argType = head(argTypes); argTypes = tail(argTypes);
					if (argType != e@rtype) {
						potentialResultType = makeFailType("Bad function invocation or constructor usage, argument type mismatch",ep@\loc); // TODO: Improve error message
					}
				}			
			} else {
				potentialResultType = makeFailType("Arity mismatch, function accepts <size(argTypes)> arguments but was given <size(args)>", ep@\loc); // TODO: Improve error message
			}
		}
				
		// This will cause us to keep the last error in cases where we cannot find a valid function
		// or constructor to use.
		if (isFailType(resultType)) resultType = potentialResultType;
	}
		
	return resultType;	
}
 
public RType checkListExpression(Expression ep, {Expression ","}* es) {
	if (checkForFail({ e@rtype | e <- es })) return collapseFailTypes({ e@rtype | e <- es });
	return makeListType(lubList([e@rtype | e <- es]));
}

public RType checkSetExpression(Expression ep, {Expression ","}* es) {
	if (checkForFail({ e@rtype | e <- es })) return collapseFailTypes({ e@rtype | e <- es });
	return makeSetType(lubList([e@rtype | e <- es]));
}

public RType checkTrivialTupleExpression(Expression ep, Expression ei) {
	set[Expression] eset = {ei};
	if (checkForFail({e@rtype | e <- eset})) return collapseFailTypes({e@rtype | e <- eset});
	return makeTupleType([ e@rtype | e <- eset]);
}

public RType checkTupleExpression(Expression ep, Expression ei, {Expression ","}* es) {
	set[Expression] eset = {ei} + {e | e <- es};
	if (checkForFail({e@rtype | e <- eset})) return collapseFailTypes({e@rtype | e <- eset});
	return makeTupleType([ e@rtype | e <- eset]);
}

// TODO: Implement this, need to get syntax matching over maps working first
public RType checkMapExpression(Expression ep) {
	return makeVoidType();
}

//
// Typecheck a closure. The type of the closure is a function type, based on the parameter types
// and the return type. This mainly then propagages any failures in the parameters or the
// closure body.
//
public RType checkClosureExpression(Expression ep, Type t, Parameters p, Statement+ ss) {
	list[RType] pTypes = getParameterTypes(p);
	bool isVarArgs = isVarArgsType(tail(pTypes));
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
	bool isVarArgs = isVarArgsType(tail(pTypes));
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
		return head(tail(slTypes,1));
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
	RType ft = getFieldType(el@rtype, convertName(n), ep@\loc);
	if (debug) println("CHECKER: Found field type <prettyPrintType(ft)>");
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
	return getFieldType(el@rtype, convertName(n), ep@\loc);
}

private list[tuple[Field field, int offset]] getFieldOffsets(list[RNamedType] fieldTypes, list[Field] fields) {
	list[tuple[Field field, int offset]] result = [ ];
	map[RName, int] namedFields =( n : i | i <-  [0..size(fieldTypes)-1], RNamedType(_,n) := fieldTypes[i]);
	
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

	RType expType = e@rtype;
	list[Field] fieldList = [ f | f <- fl ];

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
		if (! isIntType(indexList[0]@rtype) ) return makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexList[0]@rtype)>", ep@\loc);
		return lubList(getTupleFields(expType)); 		
	} else if (isRelType(expType)) {
		if (size(indexList) > 1) return makeFailType("Subscripts on nodes must contain exactly one element", ep@\loc);
		RType relLeftType = getRelFields(expType)[0];
		RType indexType = lubSet({ e@rtype | e <- indexList});
		if (! (subtypeOf(relLeftType,indexType) || subtypeOf(indexType,relLeftType))) 
			return makeFailType("The subscript type <prettyPrintType(indexType)> must be comparable to the type of the relation's first projection <prettyPrintType(relLeftType)>", ep@\loc);
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
	if (isIntType(e@rtype) && isRealType(e@rtype)) {
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
	if (checkForFail({ e@rtype, n@rtype })) return collapseFailTypes({ e@rtype, n@rtype });
	return n@rtype;
}

//
// TODO: To properly check this, we need to keep a map of not just the annotation names and types,
// but of which types those annotations apply to!
//
public RType checkSetAnnotationExpression(Expression ep, Expression el, Name n, Expression er) {
	if (checkForFail({ el@rtype, n@rtype, er@rtype })) return collapseFailTypes({ el@rtype, n@rtype, er@rtype });
	if (! subtypeOf(er@rtype, n@rtype)) return makeFailType("The type of <er>, <prettyPrintType(er@rtype)>, must be a subtype of the type of <n>, <prettyPrintType(n@rtype)>", ep@\loc);
	return n@rtype;
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
			if (et@rtype != ef@rtype && !subtypeOf(et@rtype,ef@rtype) & !subtypeOf(ef@rtype,et@rtype)) {
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
	if (! subtypeOf(e@rtype, boundType)) return makeFailType("The type of the expression, <prettyPrintType(e@rtype)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
	return makeBoolType();
}

public RType checkNoMatchExpression(Expression ep, Pattern p, Expression e) {
	if (checkForFail({ p@rtype, e@rtype })) return collapseFailTypes({ p@rtype, e@rtype });
	RType boundType = bindInferredTypesToPattern(e@rtype, p);
	if (isFailType(boundType)) return boundType;
	if (! subtypeOf(e@rtype, boundType)) return makeFailType("The type of the expression, <prettyPrintType(e@rtype)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>");
	return makeBoolType();
}

public RType checkEnumeratorExpression(Expression ep, Pattern p, Expression e) {
	if (checkForFail({ p@rtype, e@rtype })) { 
		return collapseFailTypes({ p@rtype, e@rtype });
	} else if (isListType(e@rtype) && subtypeOf(getListElementType(e@rtype), p@rtype)) {
		if (debug) println("CHECKER: Pattern <p> has type <prettyPrintType(p@rtype)>");
		if (debug) println("CHECKER: Expression <e> has type <prettyPrintType(e@rtype)>");
		RType elementType = getListElementType(e@rtype);
		RType boundType = bindInferredTypesToPattern(elementType, p);
		if (debug) println("CHECKER: Bound type is <prettyPrintType(boundType)>");
		if (isFailType(boundType)) return boundType;
		if (! subtypeOf(elementType, boundType)) return makeFailType("The type of the list elements, <prettyPrintType(elementType)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
		return makeBoolType();
	} else if (isSetType(e@rtype) && subtypeOf(getSetElementType(e@rtype), p@rtype)) {
		RType elementType = getSetElementType(e@rtype);
		RType boundType = bindInferredTypesToPattern(elementType, p);
		if (isFailType(boundType)) return boundType;
		if (! subtypeOf(elementType, boundType)) return makeFailType("The type of the expression, <prettyPrintType(e@rtype)>, must be a subtype of the pattern type, <prettyPrintType(boundType)>", ep@\loc);
		return makeBoolType();
	} else {
		throw "Unhandled enumerator case, <p> \<- <e>";
	}
}

public RType checkSetComprehensionExpression(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
	set[Expression] allExps = { e | e <- els } + { e | e <- ers };
	if (checkForFail({ e@rtype | e <- allExps }))
		return collapseFailTypes({ e@rtype | e <- ers });
	else {
		set[RType] genFailures = { 
			makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
				e <- ers, !isBoolType(e@rtype)
		};
		if (size(genFailures) == 0) {
			return makeSetType(lubSet({ replaceInferredTypes(e@rtype) | e <- els }));
		} else {
			return collapseFailTypes(genFailures);
		}
	}
}

public RType checkListComprehensionExpression(Expression ep, {Expression ","}+ els, {Expression ","}+ ers) {
	set[Expression] allExps = { e | e <- els } + { e | e <- ers };
	if (checkForFail({ e@rtype | e <- allExps }))
		return collapseFailTypes({ e@rtype | e <- ers });
	else {
		set[RType] genFailures = { 
			makeFailType("Expression should have type <prettyPrintType(makeBoolType())>, but instead has type <prettyPrintType(e@rtype)>",e@\loc) |
				e <- ers, !isBoolType(e@rtype)
		};
		if (size(genFailures) == 0) {
			return makeListType(lubSet({ replaceInferredTypes(e@rtype) | e <- els }));
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
			return makeMapType(replaceInferredTyeps(ef@rtype), replaceInferredTyeps(et@rtype));
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
	if (checkForFail(genTypes + ei@rtype + er@rtype)) return collapseFailTypes(genTypes + ei@rtype + er@rtype);
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

public RType checkExpression(Expression exp) {
	switch(exp) {
		case (Expression)`<BooleanLiteral bl>` : {
			if (debug) println("CHECKER: BooleanLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeBoolType())>");
			return makeBoolType();
		}

		case (Expression)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: DecimalIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeIntType())>");
			return makeIntType();
		}

		case (Expression)`<OctalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: OctalIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeIntType())>");
			return makeIntType();
		}

		case (Expression)`<HexIntegerLiteral il>`  : {
			if (debug) println("CHECKER: HexIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeIntType())>");
			return makeIntType();
		}

		case (Expression)`<RealLiteral rl>`  : {
			if (debug) println("CHECKER: RealLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeRealType())>");
			return makeRealType();
		}

		// TODO: Interpolation
		case (Expression)`<StringLiteral sl>`  : {
			if (debug) println("CHECKER: StringLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeStrType())>");
			return makeStrType();
		}

		// TODO: Interpolation
		case (Expression)`<LocationLiteral ll>`  : {
			if (debug) println("CHECKER: LocationLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeLocType())>");
			return makeLocType();
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			if (debug) println("CHECKER: DateTimeLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(makeDateTimeType())>");
			return makeDateTimeType();
		}

		// TODO: See if we ever have this; a qualified name, not a name, is an expression
		case (Expression)`<Name n>`: {
			if (debug) println("CHECKER: Name: <exp>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(n@rtype));
			return isInferredType(n@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(n@rtype)] : n@rtype; 
		}
		
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			if (debug) println("CHECKER: QualifiedName: <exp>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return isInferredType(qn@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			if (debug) println("CHECKER: ReifiedType: <exp>");
			RType rt = checkReifiedTypeExpression(exp,t,el);
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(rt));
			return rt;
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			if (debug) println("CHECKER: Call or Tree: <exp>)");
			RType t = checkCallOrTreeExpression(exp,e1,el);
			if(debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// List
		case `[<{Expression ","}* el>]` : {
			if (debug) println("CHECKER: List: <exp>");
			RType t = checkListExpression(exp,el);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			if (debug) println("CHECKER: Set: <exp>");
			RType t = checkSetExpression(exp,el);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Tuple, with just one element
		case (Expression)`<<Expression ei>>` : {
			if (debug) println("CHECKER: Tuple <exp>");
			RType t = checkTrivialTupleExpression(exp,ei);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Tuple, with multiple elements
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			if (debug) println("CHECKER: Tuple <exp>");
			RType t = checkTupleExpression(exp,ei,el);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TODO: Map: Need to figure out a syntax that works for matching this
//		case `<<Expression ei>, <{Expression ","}* el>>` : {
//			// TODO: This is not yet working
//			if (debug) println("CHECKER: Tuple <exp>");
//			RType t = checkTupleExpression(exp,ei,el);
//			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
//			return t;
//		}

		// Closure
		case `<Type t> <Parameters p> { <Statement+ ss> }` : {
			if (debug) println("CHECKER: Closure: <exp>");
			RType t = checkClosureExpression(exp,t,p,ss);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			if (debug) println("CHECKER: VoidClosure: <exp>");
			RType t = checkVoidClosureExpression(exp,p,ss);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {
			if (debug) println("CHECKER: NonEmptyBlock: <exp>");
			RType t = checkNonEmptyBlockExpression(exp,ss);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;		
		}
		
		// Visit
		case (Expression) `<Label l> <Visit v>` : {
			if (debug) println("CHECKER: Visit: <exp>");
			RType t = checkVisitExpression(exp,l,v);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;			
		}
		
		// ParenExp
		case `(<Expression e>)` : {
			if (debug) println("CHECKER: ParenExp: <exp>");
			RType t = e@rtype;
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			if (debug) println("CHECKER: Range: <exp>");
			RType t = checkRangeExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			if (debug) println("CHECKER: StepRange: <exp>");
			RType t = checkStepRangeExpression(exp,e1,e2,e3);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// ReifyType
		case (Expression)`#<Type t>` : {
			if (debug) println("CHECKER: ReifyType: <exp>");
			RType t = RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))]));
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			if (debug) println("CHECKER: FieldUpdate: <exp>");
			RType t = checkFieldUpdateExpression(exp,e1,n,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			if (debug) println("CHECKER: FieldAccess: <exp>");
			RType t = checkFieldAccessExpression(exp,e1,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldProject
		case `<Expression e1> < <{Field ","}+ fl> >` : {
			if (debug) println("CHECKER: FieldProject: <exp>");
			RType t = checkFieldProjectExpression(exp,e1,fl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Subscript 
		case `<Expression e1> [ <{Expression ","}+ el> ]` : {
			if (debug) println("CHECKER: Subscript <exp>");
			RType t = checkSubscriptExpression(exp,e1,el);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// IsDefined
		case `<Expression e> ?` : {
			if (debug) println("CHECKER: IsDefined: <exp>");
			RType t = checkIsDefinedExpression(exp,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Negation
		case `! <Expression e>` : {
			if (debug) println("CHECKER: Negation: <exp>");
			RType t = checkNegationExpression(exp,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Negative
		case `- <Expression e> ` : {
			if (debug) println("CHECKER: Negative: <exp>");
			RType t = checkNegativeExpression(exp,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TransitiveReflexiveClosure
		case `<Expression e> * ` : {
			if (debug) println("CHECKER: TransitiveReflexiveClosure: <exp>");
			RType t = checkTransitiveReflexiveClosureExpression(exp,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TransitiveClosure
		case `<Expression e> + ` : {
			if (debug) println("CHECKER: TransitiveClosure: <exp>");
			RType t = checkTransitiveClosureExpression(exp,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// GetAnnotation
		case `<Expression e> @ <Name n>` : {
			if (debug) println("CHECKER: GetAnnotation: <exp>");
			RType t = checkGetAnnotationExpression(exp,e,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			if (debug) println("CHECKER: SetAnnotation: <exp>");
			RType t = checkSetAnnotationExpression(exp,e1,n,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			if (debug) println("CHECKER: Composition: <exp>");
			RType t = checkCompositionExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Product
		case `<Expression e1> * <Expression e2>` : {
			if (debug) println("CHECKER: Times: <exp>");
			RType t = checkProductExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			if (debug) println("CHECKER: Join: <exp>");
			RType t = checkJoinExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			if (debug) println("CHECKER: Div: <exp>");
			RType t = checkDivExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			if (debug) println("CHECKER: Mod: <exp>");
			RType t = checkModExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Intersection
		case `<Expression e1> & <Expression e2>` : {
			if (debug) println("CHECKER: Intersection: <exp>");
			RType t = checkIntersectionExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Plus
		case `<Expression e1> + <Expression e2>` : {
			if (debug) println("CHECKER: Plus: <exp>");
			RType t = checkPlusExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			if (debug) println("CHECKER: Minus: <exp>");
			RType t = checkMinusExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			if (debug) println("CHECKER: NotIn: <exp>");
			RType t = checkNotInExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			if (debug) println("CHECKER: In: <exp>");
			RType t = checkInExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			if (debug) println("CHECKER: LessThan: <exp>");
			RType t = checkLessThanExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			if (debug) println("CHECKER: LessThanOrEq: <exp>");
			RType t = checkLessThanOrEqualExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			if (debug) println("CHECKER: GreaterThan: <exp>");
			RType t = checkGreaterThanExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			if (debug) println("CHECKER: GreaterThanOrEq: <exp>");
			RType t = checkGreaterThanOrEqualExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			if (debug) println("CHECKER: Equals: <exp>");
			RType t = checkEqualsExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			if (debug) println("CHECKER: NotEquals: <exp>");
			RType t = checkNotEqualsExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			if (debug) println("CHECKER: IfThenElse: <exp>");
			RType t = checkIfThenElseExpression(exp,e1,e2,e3);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;	
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			if (debug) println("CHECKER: IfDefinedOtherwise: <exp>");
			RType t = checkIfDefinedOtherwiseExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;		
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			if (debug) println("CHECKER: Implication: <exp>");
			RType t = checkImplicationExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			if (debug) println("CHECKER: Equivalence: <exp>");
			RType t = checkEquivalenceExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			if (debug) println("CHECKER: And: <exp>");
			RType t = checkAndExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			if (debug) println("CHECKER: Or: <exp>");
			RType t = checkOrExpression(exp,e1,e2);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Match
		case `<Pattern p> := <Expression e>` : {
			if (debug) println("CHECKER: Match: <exp>");
			RType t = checkMatchExpression(exp,p,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NoMatch
		case `<Pattern p> !:= <Expression e>` : {
			if (debug) println("CHECKER: NoMatch: <exp>");
			RType t = checkNoMatchExpression(exp,p,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Enumerator
		case `<Pattern p> <- <Expression e>` : {
			if (debug) println("CHECKER: Enumerator: <exp>");
			RType t = checkEnumeratorExpression(exp,p,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Set Comprehension
		case (Expression) `{ <{Expression ","}+ el> | <{Expression ","}+ er> }` : {
			if (debug) println("CHECKER: SetComprehension: <exp>");
			RType t = checkSetComprehensionExpression(exp,el,er);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// List Comprehension
		case (Expression) `[ <{Expression ","}+ el> | <{Expression ","}+ er> ]` : {
			if (debug) println("CHECKER: ListComprehension: <exp>");
			RType t = checkListComprehensionExpression(exp,el,er);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Map Comprehension
		case (Expression) `( <Expression ef> : <Expression et> | <{Expression ","}+ er> )` : {
			if (debug) println("CHECKER: MapComprehension: <exp>");
			RType t = checkMapComprehensionExpression(exp,ef,et,er);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Reducer 
		case `( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
			if (debug) println("CHECKER: Reducer: <exp>");
			RType t = checkReducerExpression(exp,ei,er,egs);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// It
		case `it` : {
			if (debug) println("CHECKER: It: <exp>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(exp@rtype));
			return exp@rtype; 
		}
			
		// All 
		case `all ( <{Expression ","}+ egs> )` : {
			if (debug) println("CHECKER: All: <exp>");
			RType t = checkAllExpression(exp,egs);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Any 
		case `all ( <{Expression ","}+ egs> )` : {
			if (debug) println("CHECKER: Any: <exp>");
			RType t = checkAnyExpression(exp,egs);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// TODO: Look in embedding.sdf for more expression productions
		
		// TODO: Add support for interpolation
	}
}

//
// Check individual cases
//
public RType checkCase(Case c) {
	switch(c) {
		case `case <PatternWithAction p>` : {
			if (debug) println("CHECKER: Case: <c>");
			
			// If insert is used anywhere in this case pattern, find the type being inserted and
			// check to see if it is correct.
			RType caseType = getCasePatternType(c);
			set[RType] failures = { };
			top-down-break visit(p) {
				case (Expression) `<Label l> <Visit v>` : 0; // no-op
				
				case (Statement) `<Label l> <Visit v>` : 0; // no-op
				
				case ins : `insert <DataTarget dt> <Statement s>` : {
					RType stmtType = getInternalStatementType(s@rtype);
					if (caseType != stmtType) {
						failures += makeFailType("Type of insert, <prettyPrintType(stmtType)>, does not match type of case, <prettyPrintType(caseType)>", ins@\loc);
					}
				} 
			}
			RType retType = (size(failures) == 0) ? p@rtype : collapseFailTypes(failures);
			if (debug) println("CHECKER: Assigning type: <prettyPrintType(retType)>");
			return retType;
		}
		
		case `default : <Statement b>` : {
			if (debug) println("CHECKER: Case: <c>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(getInternalStatementType(b@rtype)));
			return getInternalStatementType(b@rtype);
		}
	}
}

//
// TODO: Not sure what to return here, since, for a tuple, this could be any of the
// types of the tuple fields. So, for tuples, just return the lub right now, which will
// let the test pass. Returning void would be more conservative, but then this would
// never work for tuples.
//
// NOTE: Doing this for relations with arity > 2 doesn't seem to work right now in the
// interpreter. I'm not sure if this is by design or by accident.
//
public RType checkSubscriptAssignable(Assignable ap, Assignable a, Expression e) {
	if (checkForFail({a@rtype, e@rtype})) return collapseFailTypes({a@rtype, e@rtype});
	if (! isIntType(e@rtype)) return makeFailType("The index expression must be of type int, but is instead of type <prettyPrintType(e@rtype)>", ap@\loc);
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
		if (! isIntType(e@rtype) ) return makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(e@rtype)>", ap@\loc);
		return makeAssignableType(wholeType, getListElementType(partType));		
	} else {
		return makeFailType("Subscript not supported on type <prettyPrintType(partType)>", ap@\loc);
	}
}

//
// For the field access assignable, we return the overall type of the assignable (i.e., the value with
// fields) and the type of the individual field. If the field does not exist, this instead returns
// fail.
//
public RType checkFieldAccessAssignable(Assignable ap, Assignable a, Name n) {
	if (checkForFail({a@rtype})) return collapseFailTypes({a@rtype});
	RType partType = getPartType(a@rtype);
	RType wholeType = getWholeType(a@rtype);
	RType rt = getFieldType(partType, convertName(n), ap@\loc);
	if (isFailType(rt)) return rt;
	return makeAssignableType(wholeType, rt);
}

//
// The type of the if defined assignable is still the overall type of the assignable. We just need to make
// sure that the default value is something that could actually be assigned into the assignable.
//		
public RType checkIfDefinedOrDefaultAssignable(Assignable ap, Assignable a, Expression e) {
	if (isFailType(a@rtype) || isFailType(e@rtype)) return collapseFailTypes({ a@rtype, e@rtype });
	RType partType = getPartType(a@rtype);
	RType wholeType = getWholeType(a@rtype);
	if (!subtypeOf(e@rtype,partType)) return makeFailType("The type of <e>, <prettyPrintType(e@rtype)>, is not a subtype of the type of <a>, <prettyPrintType(partType)>",ap@\loc);
	return makeAssignableType(wholeType, partType);		
}

// TODO: Implement
public RType checkAnnotationAssignable(Assignable ap, Assignable a, Name n) {
	throw "Function checkAnnotationAssignable not implemented!";
}

//
// The type returned is the actual type of the tuple for both the whole and the part of
// the assignable type, since the tuple overall is being assigned into. The first field
// of the tuple is a, while the rest are in al. This is only done because of a matching
// problem over the standard tuple concrete syntax.
//		
public RType checkTrivialTupleAssignable(Assignable ap, Assignable a) {
	list[Assignable] alist = [ a ];
	if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
	RType rt = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
	return makeAssignableType(getWholeType(a@rtype),rt);
}

public RType checkTupleAssignable(Assignable ap, Assignable a, {Assignable ","}* al) {
	list[Assignable] alist = [ a ] + [ ai | ai <- al];
	if (checkForFail({ ai@rtype | ai <- alist })) return collapseFailTypes({ ai@rtype | ai <- alist });
	RType rt = makeTupleType([ getPartType(ai@rtype) | ai <- alist]);
	return makeAssignableType(getWholeType(a@rtype),rt);
}

//
// Check assignables
//
public RType checkAssignable(Assignable a) {
	switch(a) {
		// Variable
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("CHECKER: VariableAssignable: <a>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			RType rt = isInferredType(qn@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype;
			return makeAssignableType(rt,rt); 
		}
		
		// Subscript
		case `<Assignable al> [ <Expression e> ]` : {
			if (debug) println("CHECKER: SubscriptAssignable: <a>");
			RType t = checkSubscriptAssignable(a,al,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Field Access
		case `<Assignable al> . <Name n>` : {
			if (debug) println("CHECKER: FieldAccessAssignable: <a>");
			RType t = checkFieldAccessAssignable(a,al,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// If Defined or Default
		case `<Assignable al> ? <Expression e>` : {
			if (debug) println("CHECKER: IfDefinedOrDefaultAssignable: <a>");
			RType t = checkIfDefinedOrDefaultAssignable(a,al,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Annotation
		case `<Assignable al> @ <Name n>` : {
			if (debug) println("CHECKER: AnnotationAssignable: <a>");
			RType t = checkAnnotationAssignable(a,al,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Tuple, with just one element
		case (Assignable)`< <Assignable ai> >` : {
			if (debug) println("CHECKER: TupleAssignable: <a>");
			RType t = checkTupleAssignable(a, ai);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Tuple, with multiple elements
		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
			if (debug) println("CHECKER: TupleAssignable: <a>");
			RType t = checkTupleAssignable(a, ai, al);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Constructor
		// NOTE: We are not currently supporting this case, as we are considering removing it from
		// the language as an unsafe operation.
//		case `<Name n> ( <{Assignable ","}+ al> )` : {
//			if (debug) println("CHECKER: ConstructorAssignable: <a>");
//			RType t = checkConstructorAssignable(a,n,al);
//			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
//			return t;
//		}
	}
}

//
// Given an actual type rt and an assignable a, recurse the structure of a, assigning the correct parts of
// rt to any named parts of a. For instance, in an assignment like x = 5, if x has an inference type it will
// be assigned type int, while in an assignment like <a,b> = <true,4>, a would be assigned type bool
// while b would be assigned type int (again, assuming they are both inferrence variables). The return
// type is the newly-computed type of the assignable, with inference vars bound to concrete types.
//
public RType bindInferredTypesToAssignable(RType rt, Assignable a) {
	if (debug) println("CHECKER: binding inferred types in <prettyPrintType(rt)> to assignable <a>");

	RType partType = getPartType(a@rtype);
	RType wholeType = getWholeType(a@rtype);

	switch(a) {
		// Variable
		// This is the useful bottom case of the recursion. We should already have checked for some
		// type errors at this point, so this just targets cases where we have variables with inferred types.
		// If no type has been assigned to the inference variable, this will execute correctly. If a type has
		// been assigned, rt, the type being bound, must be a subtype of of the existing type, since rt
		// is being assigned into t. The type will remain at t though, it will not be rebound.
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("CHECKER: Checking on bind of type <prettyPrintType(rt)> to name <qn>");
			if (isInferredType(qn@rtype)) {
				RType t = globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)];
				if (isInferredType(t)) {
					if (debug) println("CHECKER: Updating binding of inferred type <prettyPrintType(t)> to <prettyPrintType(rt)> on name <qn>");
					updateInferredTypeMappings(t,rt);
					return rt;
				} else {
					if (!subtypeOf(rt,t)) {
						if (debug) println("CHECKER: Found type clash on inferred variable, trying to assign <prettyPrintType(rt)> and <prettyPrintType(t)> to name <qn>");
						return makeFailType("In assignment to <qn> type <prettyPrintType(rt)> must be a subtype of type <prettyPrintType(t)>", qn@\loc);
					} else {
						return t;
					}
				}
			} else {
				return qn@rtype;
			}
		}
		
		// Subscript
		// NOTE: If we are assigning into a subscripted assignable, the assignable needs
		// to already have a type for this to make sense. So, don't propagate the bind
		// any further, and just return the existing type of the assignable.
		case `<Assignable al> [ <Expression e> ]` : {
			if (debug) println("CHECKER: SubscriptAssignable: <a>");
			return a@rtype;
		}
		
		// Field Access
		// NOTE: If we are assigning into a field, the assignable needs to already have a
		// type for this to make sense. So, don't propagate the bind any further, and
		// just return the existing type of the assignable.
		case `<Assignable al> . <Name n>` : {
			if (debug) println("CHECKER: FieldAccessAssignable: <a>");
			return a@rtype;
		}
		
		// If Defined or Default
		// This just pushes the binding down into the assignable on the left-hand
		// side of the ?, the default expression has no impact on the binding.
		case `<Assignable al> ? <Expression e>` : {
			if (debug) println("CHECKER: IfDefinedOrDefaultAssignable: <a>");
			return bindInferredTypesToAssignable(rt, al);
		}
		
		// Annotation
		// NOTE: If we are assigning into an annotation, this assignable needs to already
		// have a type for this to make sense. So, don't propagate the bind any further,
		// and just return the existing type of the assignable.
		case `<Assignable al> @ <Name n>` : {
			if (debug) println("CHECKER: AnnotationAssignable: <a>");
			return a@rtype;
		}
		
		// Tuple
		// To be correct, the type being bound into the assignable also needs to be a tuple
		// of the same length. If this is true, the bind recurses on each tuple element.
		// If not, a failure type, indicating the type of failure (arity mismatch, or type of
		// assignable not a tuple) has occurred.
		case (Assignable)`< <Assignable ai> >` : {
			if (debug) println("CHECKER: TupleAssignable: <a>");
			list[Assignable] alist = [ai];
			if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
				list[RType] tupleFieldTypes = getTupleFields(rt);
				return makeTupleType([bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]]);  				
			} else if (!isTupleType(rt)) {
				return makeFailType("Type mismatch: this error should have already been caught!", a@\loc);
			} else {
				return makeFailType("Arity mismatch: this error should have already been caught!", a@\loc);
			}
		}

		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
			if (debug) println("CHECKER: TupleAssignable: <a>");
			list[Assignable] alist = [ai] + [ ali | ali <- al ];
			if (isTupleType(rt) && getTupleFieldCount(rt) == size(alist)) {
				list[RType] tupleFieldTypes = getTupleFields(rt);
				return makeTupleType([bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]]);  				
			} else if (!isTupleType(rt)) {
				return makeFailType("Type mismatch: this error should have already been caught!", a@\loc);
			} else {
				return makeFailType("Arity mismatch: this error should have already been caught!", a@\loc);
			}
		}
		
		// Constructor
		// NOTE: We are not currently supporting this case, as we are considering removing it from
		// the language as an unsafe operation.
//		case `<Name n> ( <{Assignable ","}+ al> )` : {
//			if (debug) println("CHECKER: ConstructorAssignable: <a>");
//			return makeFailType("Unhandled case in bindInferredTypesToAssignable for assignable <a>");
//		}
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
		case `catch : <Statement b>` : {
			if (debug) println("CHECKER: Catch: <c>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(b@rtype));
			return b@rtype;
		}
		
		// TODO: Pull out into own function for consistency
		case `catch <Pattern p> : <Statement b>` : {
			if (debug) println("CHECKER: Catch: <c>");
			
			if (checkForFail({ p@rtype, getInternalStatementType(b@rtype) }))
				return makeStatementType(collapseFailTypes({ p@rtype, getInternalStatementType(b@rtype) }));
			else
				return b@rtype;
		}
	}
}

public RType checkLabel(Label l) {
	if ((Label)`<Name n> :` := l && ( (n@rtype)?)) {
		return n@rtype;
	}
	return makeVoidType();
}

//
// TODO: Extract common code in each case into another function
//
// TODO: Any way to verify that types of visited sub-parts can properly be types
// of subparts of the visited expression?
//
public RType checkVisit(Visit v) {
	switch(v) {
		case `visit (<Expression se>) { <Case+ cs> }` : {
			set[RType] caseTypes = { c@rtype | c <- cs };
			if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
			RType caseLubType = lubSet(caseTypes);
			if (subtypeOf(caseLubType, se@rtype)) 
				return se@rtype;
			else
				return makeFailType("Visit cases must all be subtypes of the type of the visited expression"); 
		}
		
		case `<Strategy st> visit (<Expression se>) { <Case+ cs> }` : {
			set[RType] caseTypes = { c@rtype | c <- cs };
			if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
			RType caseLubType = lubSet(caseTypes);
			if (subtypeOf(caseLubType, se@rtype))
				return se@rtype;
			else
				return makeFailType("Visit cases must all be subtypes of the type of the visited expression"); 
		}		
	}
}

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
	RType resultType = makeFailType("We assume bad, bad things!",pp@\loc);

	// First, if we have any failures, just propagate those upwards, don't bother to
	// check the rest of the call.
	// TODO: We may want to check arity, etc anyway, since we could catch errors
	// where no function or constructor could possibly match.	
	if (checkForFail({ pc@rtype } + { p@rtype | p <- ps }))
		return collapseFailTypes({ pc@rtype } + { p@rtype | p <- ps });
			
	// We can have overloaded functions and overloaded data constructors. If the type
	// is overloaded, we need to check each overloading to find the one that works.
	// TODO: We should codify the rules for resolving overloaded functions, since
	// we need those rules here. It should be the case that the most specific function
	// wins, but we probably also need errors to indicate when definitions make this
	// impossible, like f(int,value) versus f(value,int).
	
	// Set up the possible alternatives. We will treat the case of no overloads as a trivial
	// case of overloading with only one alternative.
	set[RType] alternatives = isOverloadedType(pc@rtype) ? getOverloadOptions(pc@rtype) : { pc@rtype };
	
	// Now, try each alternative, seeing if one matches.
	for (a <- alternatives) {
		list[Pattern] args = [ p | p <- ps ];
		list[RType] argTypes = [];
		RType potentialResultType;
		
		if (isFunctionType(a)) {
			argTypes = getFunctionArgumentTypes(a);
			potentialResultType = getFunctionReturnType(a);
		} else if (isConstructorType(a)) {
			argTypes = getConstructorArgumentTypes(a);
			potentialResultType = getConstructorResultType(a);
		}
		
		if (size(argTypes) == size(args)) {
			for (p <- args) {
				RType argType = head(argTypes); argTypes = tail(argTypes);
				if (argType != p@rtype) {
					potentialResultType = makeFailType("Bad function or constructor pattern, argument type mismatch",pp@\loc); // TODO: Improve error message
				}
			}			
		} else {
			potentialResultType = makeFailType("Arity mismatch", pp@\loc); // TODO: Improve error message
		}
		
		// This will cause us to keep the last error in cases where we cannot find a valid function
		// or constructor to use.
		if (isFailType(resultType)) resultType = potentialResultType;
	}
		
	return resultType;	
}
 
public RType checkListPattern(Pattern pp, {Pattern ","}* ps) {
	if (checkForFail({ p@rtype | p <- ps })) return collapseFailTypes({ p@rtype | p <- ps });
	return makeListType(lubList([p@rtype | p <- ps]));
}

public RType checkSetPattern(Pattern pp, {Pattern ","}* ps) {
	if (checkForFail({ p@rtype | p <- ps })) 	return collapseFailTypes({ p@rtype | p <- ps });
	return makeSetType(lubList([p@rtype | p <- ps]));
}

public RType checkTrivialTuplePattern(Pattern pp, Pattern pi) {
	set[Pattern] pset = {pi};
	if (checkForFail({p@rtype | p <- pset})) 	return collapseFailTypes({p@rtype | p <- pset});
	return makeTupleType([ p@rtype | p <- pset]);
}

public RType checkTuplePattern(Pattern pp, Pattern pi, {Pattern ","}* ps) {
	set[Pattern] pset = {pi} + {p | p <- ps};
	if (checkForFail({p@rtype | p <- pset})) 	return collapseFailTypes({p@rtype | p <- pset});
	return makeTupleType([ p@rtype | p <- pset]);
}

// TODO: Implement this once we can match maps
public RType checkMapPattern(Pattern pp) {
	return makeVoidType();
}

//
// For this pattern, and the next few patterns, check to see if we should enforce any kind of typing
// discipling. In the current interpreter haveing n of type t1, and p of type t2, where t1 || t2,
// does not cause type errors, although it can be confusing.
// Also, TODO check to see if this construct can bind inference vars.
//
public RType checkVariableBecomesPattern(Pattern pp, Name n, Pattern p) {
	if (checkForFail({ n@rtype, p@rtype })) return collapseFailTypes({ n@rtype, p@rtype });
	return n@rtype;
}

//
// Currently this just "casts" the pattern to the given type t for type checking purposes,
// but not for matching purposes. n should already be assigned type t, so just return
// the type of n.
//
public RType checkTypedVariableBecomesPattern(Pattern pp, Type t, Name n, Pattern p) {
	if (checkForFail({ n@rtype, p@rtype })) return collapseFailTypes({ n@rtype, p@rtype });
	return n@rtype;
}

//
// Currently this just "casts" the pattern to the given type t for type checking purposes,
// but not for matching purposes. For instance, [ tuple[int,int] ] <a,b,c> := <1,2> would
// be type correct, but would not match. The type of the pattern is the type t.
//
public RType checkGuardedPattern(Pattern pp, Type t, Pattern p) {
	if (isFailType(p@rtype)) return p@rtype;
	return convertType(t);
}

// TODO: Should we just give this the void type? !p gives us basically anything. Follow up with this!
public RType checkAntiPattern(Pattern pp, Pattern p) {
	return p@rtype;
}

public RType checkPattern(Pattern pat) {
	switch(pat) {
		case (Pattern)`<BooleanLiteral bl>` : {
			if (debug) println("CHECKER: BooleanLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeBoolType()));
			return makeBoolType();
		}

		case (Pattern)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: DecimalIntegerLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeIntType()));
			return makeIntType();
		}

		case (Pattern)`<OctalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: OctalIntegerLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeIntType()));
			return makeIntType();
		}

		case (Pattern)`<HexIntegerLiteral il>`  : {
			if (debug) println("CHECKER: HexIntegerLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeIntType()));
			return makeIntType();
		}

		case (Pattern)`<RealLiteral rl>`  : {
			if (debug) println("CHECKER: RealLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeRealType()));
			return makeRealType();
		}

		// TODO: Interpolation
		case (Pattern)`<StringLiteral sl>`  : {
			if (debug) println("CHECKER: StringLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeStrType()));
			return makeStrType();
		}

		// TODO: Interpolation
		case (Pattern)`<LocationLiteral ll>`  : {
			if (debug) println("CHECKER: LocationLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeLocType()));
			return makeLocType();
		}

		case (Pattern)`<DateTimeLiteral dtl>`  : {
			if (debug) println("CHECKER: DateTimeLiteralPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(makeDateTimeType()));
			return makeDateTimeType();
		}

		// TODO: See if we ever have this; a qualified name, not a name, is a Pattern
		case (Pattern)`<Name n>`: {
			if (debug) println("CHECKER: NamePattern: <pat>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(n@rtype));
			return isInferredType(n@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(n@rtype)] : n@rtype; 
		}
		
		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			if (debug) println("CHECKER: QualifiedNamePattern: <pat>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return isInferredType(qn@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// ReifiedType
		case `<BasicType t> ( <{Pattern ","}* pl> )` : {
			if (debug) println("CHECKER: ReifiedTypePattern: <pat>");
			RType rt = checkReifiedTypePattern(pat,t,pl);
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(rt));
			return rt;
		}

		// CallOrTree
		case `<Pattern p1> ( <{Pattern ","}* pl> )` : {
			if (debug) println("CHECKER: CallOrTreePattern: <pat>");
			RType rt = checkCallOrTreePattern(pat,p1,pl);
			if(debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			if (debug) println("CHECKER: ListPattern: <pat>");
			RType rt = checkListPattern(pat,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			if (debug) println("CHECKER: SetPattern: <pat>");
			RType rt = checkSetPattern(pat,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}

		// Tuple
		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("CHECKER: TuplePattern: <pat>");
			RType rt = checkTrivialTuplePattern(pat,pi);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}

		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("CHECKER: TuplePattern: <pat>");
			RType rt = checkTuplePattern(pat,pi,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}

		// TODO: Map: Need to figure out a syntax that works for matching maps

		// Typed Variable
		case (Pattern) `<Type t> <Name n>` : {
			if (debug) println("CHECKER: TypedVariablePattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(n@rtype));
			return n@rtype;
		}

		// Multi Variable
		case `<QualifiedName qn> *` : {
			if (debug) println("CHECKER: MultiVariablePattern: <pat>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return isInferredType(qn@rtype) ? globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// Descendant
		case `/ <Pattern p>` : {
			if (debug) println("CHECKER: DescendantPattern: <pat>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(p@rtype));
			return p@rtype;
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: VariableBecomesPattern: <pat>");
			RType rt = checkVariableBecomesPattern(pat,n,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: TypedVariableBecomesPattern: <pat>");
			RType rt = checkTypedVariableBecomesPattern(pat,t,n,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			if (debug) println("CHECKER: GuardedPattern: <pat>");
			RType rt = checkGuardedPattern(pat,t,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(rt));
			return rt;
		}			
		
		// Anti
		case `! <Pattern p>` : {
			if (debug) println("CHECKER: AntiPattern: <pat>");
			RType t = checkAntiPattern(pat,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
	}

	throw "Missing case on checkPattern for pattern <p>";
}

//
// Recursively bind the types from an expression to any inferred types in a pattern. Note that we assume at this
// point that the expression and pattern are both type correct except for inference clashes, so we only need to
// look for them here. The return type then is only important if it contains failures. Otherwise, it will be the same
// as the source type rt, excepting for type inference vars.
//
// TODO: Finish bind cases, some cases are just set to return the current pattern type type
//
public RType bindInferredTypesToPattern(RType rt, Pattern pat) {
	if (debug) println("CHECKER: Binding inferred types on pattern <pat>");
	switch(pat) {
		case (Pattern)`<BooleanLiteral bl>` : {
			if (debug) println("CHECKER: Binding BooleanLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: Binding DecimalIntegerLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<OctalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: Binding OctalIntegerLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<HexIntegerLiteral il>`  : {
			if (debug) println("CHECKER: Binding HexIntegerLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<RealLiteral rl>`  : {
			if (debug) println("CHECKER: Binding RealLiteralPattern: <pat>");
			return pat@rtype;
		}

		// TODO: Interpolation
		case (Pattern)`<StringLiteral sl>`  : {
			if (debug) println("CHECKER: Binding StringLiteralPattern: <pat>");
			return pat@rtype;
		}

		// TODO: Interpolation
		case (Pattern)`<LocationLiteral ll>`  : {
			if (debug) println("CHECKER: Binding LocationLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<DateTimeLiteral dtl>`  : {
			if (debug) println("CHECKER: Binding DateTimeLiteralPattern: <pat>");
			return pat@rtype;
		}

		case (Pattern)`<Name n>`: {
			if (debug) println("CHECKER: Checking on bind of type <prettyPrintType(rt)> to name <n>");
			if (isInferredType(n@rtype)) {
				RType t = globalScopeInfo.inferredTypeMap[getInferredTypeIndex(n@rtype)];
				if (isInferredType(t)) {
					if (debug) println("CHECKER: Updating binding of inferred type <prettyPrintType(t)> to <prettyPrintType(rt)> on name <n>");
					updateInferredTypeMappings(t,rt);
					return rt;
				} else {
					if (t != rt) {
						if (debug) println("CHECKER: Found type clash on inferred variable, trying to assign <prettyPrintType(rt)> and <prettyPrintType(t)> to name <n>");
						return makeFailType("Attempt to bind multiple types to the same implicitly typed name <n>: <prettyPrintType(rt)>, <prettyPrintType(t)>", n@\loc);
					} else {
						return t;
					}
				}
			} else {
				return pat@rtype;
			}
		}

		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			if (debug) println("CHECKER: Checking on bind of type <prettyPrintType(rt)> to qualified name <qn>");
			if (isInferredType(qn@rtype)) {
				RType t = globalScopeInfo.inferredTypeMap[getInferredTypeIndex(qn@rtype)];
				if (isInferredType(t)) {
					updateInferredTypeMappings(globalScopeInfo,t,rt);
					return rt;
				} else {
					if (t != rt) {
						return makeFailType("Attempt to bind multiple types to the same implicitly typed name <qn>: <prettyPrintType(rt)>, <prettyPrintType(t)>", qn@\loc);
					} else {
						return t;
					}
				}
			} else {
				return pat@rtype;
			}
		}

		// TODO: ReifiedType
		case `<BasicType t> ( <{Pattern ","}* pl> )` : {
			if (debug) println("CHECKER: BindingReifiedTypePattern: <pat>");
			return pat@rtype;
		}

		// CallOrTree
		case `<Pattern p1> ( <{Pattern ","}* pl> )` : {
			if (debug) println("CHECKER: Binding CallOrTreePattern: <pat>");
			return pat@rtype;
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			if (debug) println("CHECKER: Binding ListPattern: <pat>");
			return pat@rtype;
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			if (debug) println("CHECKER: Binding SetPattern: <pat>");
			return pat@rtype;
		}

		// Tuple
		case `<<Pattern pi>>` : {
			if (debug) println("CHECKER: Binding TuplePattern: <pat>");
			return pat@rtype;
		}

		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("CHECKER: Binding TuplePattern: <pat>");
			return pat@rtype;
		}

		// TODO: Map: Need to figure out a syntax that works for matching maps

		// Typed Variable
		case (Pattern) `<Type t> <Name n>` : {
			if (debug) println("CHECKER: Binding TypedVariablePattern: <pat>");
			return pat@rtype;
		}

		// Multi Variable
		case `<QualifiedName qn> *` : {
			if (debug) println("CHECKER: Binding MultiVariablePattern: <pat>");
			return pat@rtype;
		}

		// Descendant
		// TODO: If Descendant is an inferred type, bind to value here -- this means that no type was given, so
		// it could actually match anything.
		case `/ <Pattern p>` : {
			if (debug) println("CHECKER: Binding DescendantPattern: <pat>");
			return bindInferredTypesToPattern(rt, p);
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: Binding VariableBecomesPattern: <pat>");
			return bindInferredTypesToPattern(rt, p);
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: Binding TypedVariableBecomesPattern: <pat>");
			return bindInferredTypesToPattern(rt, p);
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			if (debug) println("CHECKER: Binding GuardedPattern: <pat>");
			return bindInferredTypesToPattern(rt, p);
		}			
		
		// Anti -- TODO see if this makes sense, check the interpreter
		case `! <Pattern p>` : {
			if (debug) println("CHECKER: Binding AntiPattern: <pat>");
			return bindInferredTypesToPattern(rt, p);
		}
	}

	throw ("CHECKER: Did not match a case for binding inferred types on pattern <pat>!");
}

//
// Check Pattern with Action productions
//
public RType checkPatternWithAction(PatternWithAction pat) {
	switch(pat) {
		case `<Pattern p> => <Expression e>` : {
			if (checkForFail( { p@rtype, e@rtype } )) return collapseFailTypes( { p@rtype, e@rtype } );
			if (p@rtype != e@rtype) return makeFailType("Type of pattern, <prettyPrintType(p@rtype)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
			return e@rtype;
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			set[RType] whenTypes = { e@rtype | e <- es };
			if (checkForFail( whenTypes + p@rtype + e@rtype )) return collapseFailTypes( whenTypes + p@rtype + e@rtype );
			if (p@rtype != e@rtype) return makeFailType("Type of pattern, <prettyPrintType(p@rtype)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
			return e@rtype;
		}
		
		case `<Pattern p> : <Statement s>` : {
			RType stmtType = getInternalStatementType(s@rtype);
			if (checkForFail( { p@rtype, stmtType })) return collapseFailTypes( { p@rtype, stmtType });
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
	if ((DataTarget)`<Name n> :` := dt && isFailType(n@rtype)) return n@rtype;		
	return makeVoidType();
}

// TODO: For now, just update the exact index. If we need to propagate these changes we need to make this
// code more powerful.
private void updateInferredTypeMappings(RType t, RType rt) {
	globalScopeInfo.inferredTypeMap[getInferredTypeIndex(t)] = rt;
}

// Replace inferred with concrete types
public RType replaceInferredTypes(RType rt) {
	return visit(rt) { case RInferredType(n) => globalScopeInfo.inferredTypeMap[n] };
}

//
// Calculate the list of types assigned to a list of parameters
//
public list[RType] getParameterTypes(Parameters p) {
	list[RType] pTypes = [];

	if (`( <Formals f> )` := p && `<{Formal ","}* fs>` := f) {
		for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += n@rtype;
		}
	} else if (`( <Formals f> ... )` := p && `<{Formal ","}* fs>` := f) {
		for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += n@rtype;
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
	SignatureMap sigMap = populateSignatureMap(getImports(t));
	globalScopeInfo = consolidateADTDefinitions(buildNamespace(t, sigMap),getModuleName(t));
	Tree td = decorateNames(t,globalScopeInfo);
	RType adtErrors = checkADTDefinitionsForConsistency(globalScopeInfo, getModuleName(t));
	Tree tc = retagNames(check(td, adtErrors));
	if (isFailType(tc@rtype)) tc = tc[@messages = { error(l,s) | RFailType(allFailures) := tc@rtype, <s,l> <- allFailures }];
	if (debug && isFailType(tc@rtype)) {
		println("CHECKER: Found type checking errors");
		for (RFailType(allFailures) := tc@rtype, <s,l> <- allFailures) println("<l>: <s>");
	}
	return tc;
}

// TODO: We need logic that caches the signatures on the parse trees for the
// modules. Until then, we load up the signatures here...
public SignatureMap populateSignatureMap(list[Import] imports) {

	RName getNameOfImportedModule(ImportedModule im) {
		switch(im) {
			case `<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` : {
				return convertName(qn);
			}
			case `<QualifiedName qn> <ModuleActuals ma>` : {
				return convertName(qn);
			}
			case `<QualifiedName qn> <Renamings rn>` : {
				return convertName(qn);
			}
			case (ImportedModule)`<QualifiedName qn>` : {
				return convertName(qn);
			}
		}
		throw "getNameOfImportedModule: invalid syntax for ImportedModule <im>, cannot get name";
	}

	// A hack to get the paths to the modules; we need a way to ask the environment for these, or do
	// the linking like we planned (but which is currently disabled)
	str rascalLibPath = "/Users/mhills/Projects/rascal/build/rascal/src/org/rascalmpl/library";
	str testPath = "/Users/mhills/Documents/runtime-Rascal/Test/src";
	map[RName moduleName, str modPath] modPathMap = (
		RSimpleName("ATermIO") : "<rascalLibPath>/ATermIO.rsc",
		RSimpleName("AUT") : "<rascalLibPath>/AUT.rsc",
		RSimpleName("Benchmark") : "<rascalLibPath>/Benchmark.rsc",
		RSimpleName("Boolean") : "<rascalLibPath>/Boolean.rsc",
		RSimpleName("DateTime") : "<rascalLibPath>/DateTime.rsc",
		RSimpleName("ECore") : "<rascalLibPath>/ECore.rsc",
		RSimpleName("Exception") : "<rascalLibPath>/Exception.rsc",
		RSimpleName("Graph") : "<rascalLibPath>/Graph.rsc",
		RSimpleName("HTMLIO") : "<rascalLibPath>/HTMLIO.rsc",
		RSimpleName("IO") : "<rascalLibPath>/IO.rsc",
		RSimpleName("Integer") : "<rascalLibPath>/Integer.rsc",
		RSimpleName("LabeledGraph") : "<rascalLibPath>/LabeledGraph.rsc",
		RSimpleName("List") : "<rascalLibPath>/List.rsc",
		RSimpleName("Map") : "<rascalLibPath>/Map.rsc",
		RSimpleName("Message") : "<rascalLibPath>/Message.rsc",
		RSimpleName("Node") : "<rascalLibPath>/Node.rsc",
		RSimpleName("Number") : "<rascalLibPath>/Number.rsc",
		RSimpleName("ParseTree") : "<rascalLibPath>/ParseTree.rsc",
		RSimpleName("PriorityQueue") : "<rascalLibPath>/PriorityQueue.rsc",
		RSimpleName("RSF") : "<rascalLibPath>/RSF.rsc",
		RSimpleName("Real") : "<rascalLibPath>/Real.rsc",
		RSimpleName("Relation") : "<rascalLibPath>/Relation.rsc",
		RSimpleName("Set") : "<rascalLibPath>/Set.rsc",
		RSimpleName("SourceHierarchy") : "<rascalLibPath>/SourceHierarchy.rsc",
		RSimpleName("Strategy") : "<rascalLibPath>/Strategy.rsc",
		RSimpleName("String") : "<rascalLibPath>/String.rsc",
		RSimpleName("ToString") : "<rascalLibPath>/ToString.rsc",
		RSimpleName("TopologicalStrategy") : "<rascalLibPath>/TopologicalStrategy.rsc",
		RSimpleName("UnitTest") : "<rascalLibPath>/UnitTest.rsc",
		RSimpleName("ValueIO") : "<rascalLibPath>/ValueIO.rsc",
		RSimpleName("XMLDOM") : "<rascalLibPath>/XMLDOM.rsc",
		RSimpleName("XMLIO") : "<rascalLibPath>/XMLIO.rsc",
 		RSimpleName("ListExamples") : "<testPath>/ListExamples.rsc",
		RSimpleName("SomeTypes") : "<testPath>/SomeTypes.rsc",
		RSimpleName("Stack") : "<testPath>/Stack.rsc",
		RSimpleName("StackUser") : "<testPath>/StackUser.rsc",
		RSimpleName("TupleExample") : "<testPath>/TupleExample.rsc"
	);

	SignatureMap sigMap = ( );
	for (i <- imports) {
		switch(i) {
			case `import <ImportedModule im> ;` : {
				loc importLoc = |file://<modPathMap[getNameOfImportedModule(im)]>|;
				Tree importTree = parse(#Module,importLoc);
				if (debug) println("CHECKER: Generating signature for module <prettyPrintName(getNameOfImportedModule(im))>");
				sigMap[i] = getModuleSignature(importTree);
			}
			case `extend <ImportedModule im> ;` : {
				loc importLoc = |file://<modPathMap[getNameOfImportedModule(im)]>|;
				Tree importTree = parse(#Module,importLoc);
				if (debug) println("CHECKER: Generating signature for module <prettyPrintName(getNameOfImportedModule(im))>");
				sigMap[i] = getModuleSignature(importTree);
			} 
		}
	}

	return sigMap;
}

public RType checkADTDefinitionsForConsistency(ScopeInfo scopeInfo, RName moduleName) {
	set[RType] consistencyFailures = { };

	// Get back the ID for the name of the module being checked -- there should be only one matching
	// item. TODO: We may want to verify that here.
	ScopeItemId moduleLayerId = getOneFrom(getModuleItemsForName(scopeInfo, moduleName));
	
	// Check each ADT individually	
	for (n <- domain(scopeInfo.adtMap)) {
		map[RName fieldName, RType fieldType] fieldMap = ( );

		// First check imported constructors. If we get errors, we would rather have them on the constructors
		// defined in the current module, since they are easier to fix -- checking them later preferences the
		// types assigned to field in imported types.
		for (ci <- scopeInfo.adtMap[n].consItems, ci in scopeInfo.scopeRel[scopeInfo.topScopeItemId]) {
			if (ConstructorItem(cn,params,_,_) := scopeInfo.scopeItemMap[ci]) {
				for (RNamedType(nt,nn) <- params) {
					if (nn notin fieldMap) {
						fieldMap[nn] = nt;
					} else if (nn in fieldMap && fieldMap[nn] != nt) {
						consistencyFailures += makeFailType("Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>",scopeInfo.scopeItemMap[ci]@at);
					}
				}				
			} else {
				throw "checkADTDefinitionsForConsistency, unexpected constructor item <scopeInfo.scopeItemMap[ci]>";
			}
		}
		
		// TODO: May be good to refactor out identical checking code
		for (ci <- scopeInfo.adtMap[n].consItems, ci in scopeInfo.scopeRel[moduleLayerId]) {
			if (ConstructorItem(cn,params,_,_) := scopeInfo.scopeItemMap[ci]) {
				for (RNamedType(nt,nn) <- params) {
					if (nn notin fieldMap) {
						fieldMap[nn] = nt;
					} else if (nn in fieldMap && fieldMap[nn] != nt) {
						consistencyFailures += makeFailType("Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>",scopeInfo.scopeItemMap[ci]@at);
					}
				}				
			} else {
				throw "checkADTDefinitionsForConsistency, unexpected constructor item <scopeInfo.scopeItemMap[ci]>";
			}
		}
	}
	
	if (size(consistencyFailures) > 0) 
		return collapseFailTypes(consistencyFailures);
	else
		return makeVoidType();
}

private ScopeInfo globalScopeInfo = createNewScopeInfo();

// Check to see if the cases given cover the possible matches of the expected type.
// If a default is present this is automatically true, else we need to look at the
// patterns given in the various cases. 
public bool checkCaseCoverage(RType expectedType, Case+ options, ScopeInfo scopeInfo) {
	set[Case] defaultCases = { cs | cs <- options, `default: <Statement b>` := cs };
	if (size(defaultCases) > 0) return true;	
	
	set[Pattern] casePatterns = { p | cs <- options, `case <Pattern p> => <Replacement r>` := cs || `case <Pattern p> : <Statement b>` := cs };
	return checkPatternCoverage(expectedType, casePatterns, scopeInfo);		
}

// Check to see if the patterns in the options set cover the possible matches of the
// expected type. This can be recursive, for instance with ADT types.
// TODO: Interpolation
// TODO: Need to expand support for matching over reified types	
public bool checkPatternCoverage(RType expectedType, set[Pattern] options, ScopeInfo scopeInfo) {

	// Check to see if a given use of a name is the same use that defines it. A use is the
	// defining use if, at the location of the name, there is a use of the name, and that use
	// is also the location of the definition of a new item.
	bool isDefiningUse(Name n, ScopeInfo scopeInfo) {
		loc nloc = n@\loc;
		println("CHECKING FOR DEFINING USE, name <n>, location <n@\loc>");
		if (nloc in scopeInfo.itemUses) {
			println("location in itemUses");
			if (size(scopeInfo.itemUses[nloc]) == 1) {
				println("only 1 item used, not overloaded");
				if (nloc in domain(scopeInfo.itemLocations)) {
					println("location in itemLocations");
					set[ScopeItemId] items = { si | si <- scopeInfo.itemLocations[nloc], isItem(scopeInfo.scopeItemMap[si]) };
					if (size(items) == 1) {
						println("location items = <items>");
						return (VariableItem(_,_,_) := scopeInfo.scopeItemMap[getOneFrom(items)]);
					} else if (size(items) > 1) {
						println("location items = <items>");
						throw "isDefiningUse: Error, location defines more than one scope item.";
					}				
				}
			}
		}
		return false;
	}

	// Take a rough approximation of whether a pattern completely covers the given
	// type. This is not complete, since some situations where this is true will return
	// false here, but it is sound, in that any time we return true it should be the
	// case that the pattern actually covers the type.
	bool isDefiningPattern(Pattern p, RType expectedType, ScopeInfo scopeInfo) {
		if ((Pattern)`_` := p) {
			return true;
		} else if ((Pattern)`<Name n>` := p && isDefiningUse(n, scopeInfo)) {
			return true;
		} else if ((Pattern)`<Type t> _` := p && convertType(t) == expectedType) {
			return true;
		} else if ((Pattern)`<Type t> <Name n>` := p && isDefiningUse(n, scopeInfo) && convertType(t) == expectedType) {
			return true;
		} else if (`<Name n> : <Pattern pd>` := p) {
			return isDefiningPattern(pd, expectedType, scopeInfo);
		} else if (`<Type t> <Name n> : <Pattern pd>` := p && convertType(t) == expectedType) {
			return isDefiningPattern(pd, expectedType, scopeInfo);
		} else if (`[ <Type t> ] <Pattern pd>` := p && convertType(t) == expectedType) {
			return isDefiningPattern(pd, expectedType, scopeInfo);
		}
		
		return false;
	}
	
	// Check to see if a 0 or more element pattern is empty (i.e., contains no elements)
	bool checkEmptyMatch({Pattern ","}* pl, RType expectedType, ScopeInfo scopeInfo) {
		return size([p | p <- pl]) == 0;
	}

	// Check to see if a 0 or more element pattern matches an arbitrary sequence of zero or more items;
	// this means that all the internal patterns have to be of the form x*, like [ xs* ys* ], since this
	// still allows 0 items total
	bool checkTotalMatchZeroOrMore({Pattern ","}* pl, RType expectedType, ScopeInfo scopeInfo) {
		list[Pattern] plst = [p | p <- pl];
		set[bool] starMatch = { `<QualifiedName qn>*` := p | p <- pl };
		return (! (false in starMatch) );
	}

	// Check to see if a 1 or more element pattern matches an arbitrary sequence of one or more items;
	// this would be something like [xs* x ys*]. If so, recurse to make sure this pattern actually covers
	// the element type being matched. So, [xs* x ys*] := [1..10] would cover (x taking 1..10), but
	// [xs* 3 ys*] would not (even though it matches here, there is no static guarantee it does without
	// checking the values allowed on the right), and [xs* 1000 ys*] does not (again, no static guarantees,
	// and it definitely doesn't cover the example here).
	bool checkTotalMatchOneOrMore({Pattern ","}* pl, RType expectedType, ScopeInfo scopeInfo) {
		list[Pattern] plst = [p | p <- pl];
		set[int] nonStarMatch = { n | n <- domain(plst), ! `<QualifiedName qn>*` := plst[n] };
		return (size(nonStarMatch) == 1 && isDefiningPattern(plst[getOneFrom(nonStarMatch)], expectedType, scopeInfo));
	}
	
	if (isBoolType(expectedType)) {
		// For booleans, check to see if either a) a variable is given which could match either
		// true or false, or b) both the constants true and false are explicitly given in two cases
		bool foundTrue = false; bool foundFalse = false;
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) 
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
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
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
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
			if (`[<{Pattern ","}* pl>]` := p) {
				RType listElementType = getListElementType(expectedType);
				if (!foundEmptyMatch) foundEmptyMatch = checkEmptyMatch(pl, listElementType, scopeInfo);
				if (!foundTotalMatch) foundTotalMatch = checkTotalMatchZeroOrMore(pl, listElementType, scopeInfo);
				if (!foundSingleMatch) foundSingleMatch = checkTotalMatchOneOrMore(pl, listElementType, scopeInfo);
			}
			if (foundTotalMatch || (foundEmptyMatch && foundSingleMatch)) return true;
		}
		return false;
	} else if (isSetType(expectedType)) {
		bool foundEmptyMatch = false; bool foundTotalMatch = false; bool foundSingleMatch = false;
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
			if (`{<{Pattern ","}* pl>}` := p) {
				RType setElementType = getSetElementType(expectedType);
				if (!foundEmptyMatch) foundEmptyMatch = checkEmptyMatch(pl, setElementType, scopeInfo);
				if (!foundTotalMatch) foundTotalMatch = checkTotalMatchZeroOrMore(pl, setElementType, scopeInfo);
				if (!foundSingleMatch) foundSingleMatch = checkTotalMatchOneOrMore(pl, setElementType, scopeInfo);
			}
			if (foundTotalMatch || (foundEmptyMatch && foundSingleMatch)) return true;
		}
		return false;
	} else if (isMapType(expectedType)) {
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
		}
		return false;					
	} else if (isRelType(expectedType)) {
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
		}
		return false;				
	} else if (isTupleType(expectedType)) {
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
		}
		return false;				
	} else if (isADTType(expectedType)) {
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, scopeInfo)) return true;
		}
		return false;				
	}

	return false;	
}
