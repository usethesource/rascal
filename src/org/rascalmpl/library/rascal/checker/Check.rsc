module rascal::checker::Check

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;

import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::TypeRules;
import rascal::checker::SymbolTable;
import rascal::checker::Namespace;
import rascal::checker::Signature;
import rascal::checker::ListUtils;

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
public Tree updateOverloadedCall(Tree t, RType ty) {
	// TODO: Handle qualified names as well	
	Tree updateCallName(Tree t, RType ty) {
		return top-down-break visit(t) {
			case `<Name n>` : {
				if ( (ty@at)? )
					insert t[@rtype = ty][@link = ty@at];
				else
					insert t[@rtype = ty]; 
			}
		};
	}
	
	if ((Expression) `<Name n1> ( <{Expression ","}* el> )` := t) return updateCallName(t,ty);
	return t;
}

public Tree check(Tree t, RType adtErrors) {
	return visit(t) {
		case `<Expression e>` : {
			if (`<Expression e1> ( <{Expression ","}* el> )` := e) {
				if (isOverloadedType(e1@rtype)) {
					RType resolvedType = resolveOverloadedCallOrTreeExpression(e,e1,el);
					if (! typeEquality(resolvedType,e1@rtype)) {
						e = updateOverloadedCall(e,resolvedType);						
					} 
				}
			}
			RType expType = checkExpression(e); 
			if (e@\loc in globalSymbolTable.itBinder) 
				updateInferredTypeMappings(globalSymbolTable.itBinder[e@\loc], expType);
			else 
				insert e[@rtype = expType]; 
		}
		case `<Pattern p>` => p[@rtype = checkPattern(p)]
		case `<Statement s>` => s[@rtype = checkStatement(s)]
		case `<Assignable a>` => a[@rtype = checkAssignable(a)]
		case `<Catch c>` => c[@rtype = checkCatch(c)]
		case `<DataTarget dt>` => dt[@rtype = checkDataTarget(dt)]
		case `<PatternWithAction pwa>` => pwa[@rtype = checkPatternWithAction(pwa)]
		case `<Visit v>` => v[@rtype = checkVisit(v)]
		case `<Label l>` => l[@rtype = checkLabel(l)]
		case `<Variable v>` => v[@rtype = checkVariable(v)]
		case `<FunctionBody fb>` => fb[@rtype = checkFunctionBody(fb)]
		case `<Toplevel t>` => t[@rtype = checkToplevel(t)]
		case `<Body b>` => b[@rtype = checkModuleBody(b)]
		case `<Module m>` : {
			RType modType = checkModule(m);
			if (isVoidType(adtErrors))
				insert m[@rtype = modType];
			else
				insert m[@rtype = collapseFailTypes(adtErrors,modType)];
		}
		case `<Case c>` => c[@rtype = checkCase(c)]
		case `<Name n>` => setUpName(n)
	} 
}

//
// Check the names in the tree, "fixing" any inferred types with the
// type calculated during type checking.
//
public Tree retagNames(Tree t) {
	return visit(t) {
		case `<Name n>` => setUpName(n)
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
		return globalSymbolTable.inferredTypeMap[getInferredTypeIndex(n@rtype)];
	}
	return n@rtype;
}

// To check a module, currently we just propagate up errors on the body. In the future,
// we may need to make use of the module header as well (a possible TODO).
public RType checkModule(Module m) {
	if ((Module) `<Header h> <Body b>` := m) 
		return b@rtype;
	throw "checkModule: unexpected module syntax";
}

// Since checking is a bottom-up process, checking the module body just consists of bubbling any errors
// that have occurred in nested declarations up to the top level.
public RType checkModuleBody(Body b) {
	set[RType] modItemTypes = { };
	if ((Body)`<Toplevel* ts>` := b) modItemTypes = { t@rtype | t <- ts, ( (t@rtype)?) };
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
				RType mappedType = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(n@rtype)];
				if (isInferredType(mappedType)) {
					updateInferredTypeMappings(mappedType,e@rtype);
					return e@rtype;
				} else {
					if (! typeEquality(mappedType, e@rtype)) {
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
	//throw "checkTestDeclaration not yet implemented";
	return makeVoidType();
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
	Name aliasRawName = getUserTypeRawName(aliasType);
	if ( (aliasRawName@rtype)? ) return aliasRawName@rtype;
	return makeVoidType();
}

// TODO: Implement
// public SymbolTable checkView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts) {
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
		if (checkCaseCoverage(e@rtype, cases, globalSymbolTable)) {			
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
	RType retType = getFunctionReturnType(globalSymbolTable.returnTypeMap[sp@\loc]);
	if (isFailType(stmtType)) {
		return makeStatementType(stmtType);
	} else if (subtypeOf(stmtType,retType)) {
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
			RType rt = checkSolveStatement(s,vs,b,sb);
			return rt;
		}

		case `<Label l> for (<{Expression ","}+ es>) <Statement b>` : {
			RType rt = checkForStatement(s,l,es,b);
			return rt;
		}

		case `<Label l> while (<{Expression ","}+ es>) <Statement b>` : {
			RType rt = checkWhileStatement(s,l,es,b);
			return rt;
		}

		case `<Label l> do <Statement b> while (<Expression e>);` : {
			RType rt = checkDoWhileStatement(s,l,b,e);
			return rt;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` : {
			RType rt = checkIfThenElseStatement(s,l,es,bt,bf);
			return rt;
		}

		case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` : {
			RType rt = checkIfThenStatement(s,l,es,bt);
			return rt;
		}

		case `<Label l> switch (<Expression e>) { <Case+ cs> }` : {
			RType rt = checkSwitchStatement(s,l,e,cs);
			return rt;
		}

		case (Statement)`<Label l> <Visit v>` : {
			RType rt = checkVisitStatement(s,l,v);
			return rt;
		}

		case `<Expression e> ;` : {
			RType rt = makeStatementType(e@rtype);
			return rt;
		}

		case `<Assignable a> <Assignment op> <Statement b>` : {
			RType rt = checkAssignmentStatement(s,a,op,b);
			return rt;
		}
		
		case `assert <Expression e> ;` : {
			RType rt = checkAssertStatement(s, e);
			return rt;
		}

		case `assert <Expression e> : <Expression em> ;` : {
			RType rt = checkAssertWithMessageStatement(s, e, em);
			return rt;
		}
		
		// This uses a special lookup table which cached the type of the function in scope at the
		// time of the return.
		// TODO: Extract this out into a separate function.
		case `return <Statement b>` : {
			RType rt = checkReturnStatement(s, b);
			return rt;
		}
		
		// TODO: Need to add RuntimeException to a default "type store" so we can use it
		// TODO: Modify to properly check the type of b; should be a subtype of RuntimeException
		case `throw <Statement b>` : {
			RType rt = b@rtype;
			return rt;
		}

		// TODO: Need to verify that statement has same type as current subject in visit or rewrite rule
		case `insert <DataTarget dt> <Statement b>` : {
			RType st = getInternalStatementType(b@rtype);
			RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
			return rt;
		}
		
		case `append <DataTarget dt> <Statement b>` : {
			RType st = getInternalStatementType(b@rtype);
			RType rt = checkForFail({ dt@rtype, st }) ? makeStatementType(collapseFailTypes({ dt@rtype, st })) : b@rtype;
			return rt;
		}
		
		case (Statement) `<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` : {
			RType rt = checkLocalFunctionStatement(s,ts,v,sig,fb);
			return rt;
		}
		
		case (Statement) `<Type t> <{Variable ","}+ vs> ;` : {
			RType rt = checkLocalVarItems(s, vs);
			return rt;
		}
		
		// TODO: Handle the dynamic part of dynamic vars		
		case (Statement) `dynamic <Type t> <{Variable ","}+ vs> ;` : {
			RType rt = checkLocalVarItems(s, vs);
			return rt;
		}
		
		case `break <Target t> ;` : {
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			return rt;
		}
		
		case `fail <Target t> ;` : {
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			return rt;
		}
		
		case `continue <Target t> ;` : {
			RType rt = (checkForFail({ t@rtype })) ? makeStatementType(collapseFailTypes({ t@rtype })) : makeStatementType(makeVoidType());
			return rt;
		}
		
		case `try <Statement b> <Catch+ cs>` : {
			RType rt = checkTryCatchStatement(s,b,cs);
			return rt;
		}

		case `try <Statement b> <Catch+ cs> finally <Statement bf>` : {
			RType rt = checkTryCatchFinallyStatement(s,b,cs,bf);
			return rt;
		}
		
		case `<Label l> { <Statement+ bs> }` : {
			RType rt = checkBlockStatement(s, l, bs);
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

//
// This function actually resolves the overloading, giving the new type which will be assigned to
// ec; the call or tree function then just needs to get the result type of ec and check for failures.
//
public RType resolveOverloadedCallOrTreeExpression(Expression ep, Expression ec, {Expression ","}* es) {
	RType resultType = makeFailType("We assume bad, bad things!",ep@\loc);

	// First, if we have any failures, just leave the same type -- we cannot resolve the overloading.	
	if (checkForFail({ ec@rtype } + { e@rtype | e <- es })) return ec@rtype;
			
	// We can have overloaded functions and overloaded data constructors. If the type
	// is overloaded, we need to check each overloading to find the one that works.
	// TODO: We should codify the rules for resolving overloaded functions, since
	// we need those rules here. It should be the case that the most specific function
	// wins, but we probably also need errors to indicate when definitions make this
	// impossible, like f(int,value) versus f(value,int).
	
	// Set up the possible alternatives. We will treat the case of no overloads as a trivial
	// case of overloading with only one alternative.
	set[ROverloadedType] alternatives = isOverloadedType(ec@rtype) ? getOverloadOptions(ec@rtype) : { ROverloadedType(ec@rtype) };
	
	// Now, try each alternative, seeing if one matches.
	list[Expression] args = [ e | e <- es ];
	for (a <- alternatives) {
		RType potentialResultType;
		list[RType] argTypes = [];
		RType altType = a.overloadType;
		bool altHasLoc = (ROverloadedTypeWithLoc(_,_) := a);
		
		if (isFunctionType(altType)) {
			argTypes = getFunctionArgumentTypes(altType);
			potentialResultType = altType;
			if (altHasLoc) potentialResultType = potentialResultType[@at=a.overloadLoc];
		} else if (isConstructorType(altType)) {
			argTypes = getConstructorArgumentTypes(altType);
			potentialResultType = altType;
			if (altHasLoc) potentialResultType = potentialResultType[@at=a.overloadLoc];
		} else {
			potentialResultType = makeFailType("Type <prettyPrintType(altType)> is not a function or constructor type.",ep@\loc);
		}
		
		if (isFunctionType(altType) || isConstructorType(altType)) {
			if (size(argTypes) == size(args)) {
				for (e <- args) {
					RType argType = head(argTypes); argTypes = tail(argTypes);
					if (! subtypeOf(e@rtype, e@rtype)) {
						potentialResultType = makeFailType("Bad function invocation or constructor usage, type of <e>, <prettyPrintType(e@rtype)>, should be a subtype of <prettyPrintType(argType)>",ep@\loc); // TODO: Improve error message
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
		
	return isFailType(resultType) ? ec@rtype : resultType;	
}

public rel[RName,RType] getMappings(RType source, RType target) {
	if (RTypeVar(tv) := source) {
		RName tvn = getTypeVarNames(source);
		return { < tvn, target > };
	} else {
		return { getMappings(se,te) | <se,te> <- zip(getElementTypes(source),getElementTypes(target)) }; 
	}
}

public rel[RName,RType] getTypeVarMappings(list[tuple[Expression,RType]] argAndTypes) {
	return { getMappings(e@rtype,rt) | <e,rt> <- argAndTypes };
}

public map[RName,RType] consolidateMappings(rel[RName,RType] varMappings) {
	map[RName,RType] mt = ( );
	for(n <- domain(varMappings)) {
		mt[n] = lubSet(varMappings[n]);
	}
	return mt;
}

// TODO: Should streamline this logic now, since we winnow down the results above; this is correct,
// but quite redundant.
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
	set[ROverloadedType] alternatives = isOverloadedType(ec@rtype) ? getOverloadOptions(ec@rtype) : { ROverloadedType(ec@rtype) };
	
	// Now, try each alternative, seeing if one matches.
	list[Expression] args = [ e | e <- es ];
	for (a <- alternatives) {
		RType potentialResultType;
		list[RType] argTypes = [];
		RType altType = a.overloadType;
		
		if (isFunctionType(altType)) {
			argTypes = getFunctionArgumentTypes(altType);
			potentialResultType = getFunctionReturnType(altType);
		} else if (isConstructorType(altType)) {
			argTypes = getConstructorArgumentTypes(altType);
			potentialResultType = getConstructorResultType(altType);
		} else {
			potentialResultType = makeFailType("Type <prettyPrintType(altType)> is not a function or constructor type.",ep@\loc);
		}
		
		// TODO: Add consistency check for type variables here, plus add information
		// about the return type based on the input types (assuming the return type
		// then is also based on a parametric type)
		if (isFunctionType(altType) || isConstructorType(altType)) {
			if (size(argTypes) == size(args)) {
				argAndTypes = zip(args,argTypes);
				for (e <- args) {
					RType argType = head(argTypes); argTypes = tail(argTypes);
					if (! subtypeOf(e@rtype, argType)) {
						potentialResultType = makeFailType("Bad function invocation or constructor usage, type of <e>, <prettyPrintType(e@rtype)>, should be a subtype of <prettyPrintType(argType)>",ep@\loc); // TODO: Improve error message
					}
				}
//				if (! isFailType(potentialResultType) && containsTypeVar(potentialResultType)) {
//					map[RName,RType] varMappings = consolidateMappings(getTypeVarMappings(argAndTypes));
//					potentialResultType = instantiateVars(varMappings, potentialResultType);
//				}
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
			if (!typeEquality(et@rtype,ef@rtype) && !subtypeOf(et@rtype,ef@rtype) && !subtypeOf(ef@rtype,et@rtype)) {
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
		RType elementType = getListElementType(e@rtype);
		RType boundType = bindInferredTypesToPattern(elementType, p);
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

		// TODO: Interpolation
		case (Expression)`<StringLiteral sl>`  : {
			return makeStrType();
		}

		// TODO: Interpolation
		case (Expression)`<LocationLiteral ll>`  : {
			return makeLocType();
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			return makeDateTimeType();
		}

		// TODO: See if we ever have this; a qualified name, not a name, is an expression
		case (Expression)`<Name n>`: {
			return isInferredType(n@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(n@rtype)] : n@rtype; 
		}
		
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			return isInferredType(qn@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			RType rt = checkReifiedTypeExpression(exp,t,el);
			return rt;
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			RType t = checkCallOrTreeExpression(exp,e1,el);
			return t;
		}

		// List
		case `[<{Expression ","}* el>]` : {
			RType t = checkListExpression(exp,el);
			return t;
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			RType t = checkSetExpression(exp,el);
			return t;
		}

		// Tuple, with just one element
		case (Expression)`<<Expression ei>>` : {
			RType t = checkTrivialTupleExpression(exp,ei);
			return t;
		}

		// Tuple, with multiple elements
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			RType t = checkTupleExpression(exp,ei,el);
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
			RType t = checkClosureExpression(exp,t,p,ss);
			return t;
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			RType t = checkVoidClosureExpression(exp,p,ss);
			return t;
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {
			RType t = checkNonEmptyBlockExpression(exp,ss);
			return t;		
		}
		
		// Visit
		case (Expression) `<Label l> <Visit v>` : {
			RType t = checkVisitExpression(exp,l,v);
			return t;			
		}
		
		// ParenExp
		case `(<Expression e>)` : {
			RType t = e@rtype;
			return t;
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			RType t = checkRangeExpression(exp,e1,e2);
			return t;
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			RType t = checkStepRangeExpression(exp,e1,e2,e3);
			return t;
		}

		// ReifyType
		case (Expression)`#<Type t>` : {
			RType t = RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))]));
			return t;
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			RType t = checkFieldUpdateExpression(exp,e1,n,e2);
			return t;
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			RType t = checkFieldAccessExpression(exp,e1,n);
			return t;
		}

		// FieldProject
		case `<Expression e1> < <{Field ","}+ fl> >` : {
			RType t = checkFieldProjectExpression(exp,e1,fl);
			return t;
		}

		// Subscript 
		case `<Expression e1> [ <{Expression ","}+ el> ]` : {
			RType t = checkSubscriptExpression(exp,e1,el);
			return t;
		}

		// IsDefined
		case `<Expression e> ?` : {
			RType t = checkIsDefinedExpression(exp,e);
			return t;
		}

		// Negation
		case `! <Expression e>` : {
			RType t = checkNegationExpression(exp,e);
			return t;
		}

		// Negative
		case `- <Expression e> ` : {
			RType t = checkNegativeExpression(exp,e);
			return t;
		}

		// TransitiveReflexiveClosure
		case `<Expression e> * ` : {
			RType t = checkTransitiveReflexiveClosureExpression(exp,e);
			return t;
		}

		// TransitiveClosure
		case `<Expression e> + ` : {
			RType t = checkTransitiveClosureExpression(exp,e);
			return t;
		}

		// GetAnnotation
		case `<Expression e> @ <Name n>` : {
			RType t = checkGetAnnotationExpression(exp,e,n);
			return t;
		}

		// SetAnnotation
		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
			RType t = checkSetAnnotationExpression(exp,e1,n,e2);
			return t;
		}

		// Composition
		case `<Expression e1> o <Expression e2>` : {
			RType t = checkCompositionExpression(exp,e1,e2);
			return t;
		}

		// Product
		case `<Expression e1> * <Expression e2>` : {
			RType t = checkProductExpression(exp,e1,e2);
			return t;
		}

		// Join
		case `<Expression e1> join <Expression e2>` : {
			RType t = checkJoinExpression(exp,e1,e2);
			return t;
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			RType t = checkDivExpression(exp,e1,e2);
			return t;
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			RType t = checkModExpression(exp,e1,e2);
			return t;
		}

		// Intersection
		case `<Expression e1> & <Expression e2>` : {
			RType t = checkIntersectionExpression(exp,e1,e2);
			return t;
		}
		
		// Plus
		case `<Expression e1> + <Expression e2>` : {
			RType t = checkPlusExpression(exp,e1,e2);
			return t;
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			RType t = checkMinusExpression(exp,e1,e2);
			return t;
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			RType t = checkNotInExpression(exp,e1,e2);
			return t;
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			RType t = checkInExpression(exp,e1,e2);
			return t;
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			RType t = checkLessThanExpression(exp,e1,e2);
			return t;
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			RType t = checkLessThanOrEqualExpression(exp,e1,e2);
			return t;
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			RType t = checkGreaterThanExpression(exp,e1,e2);
			return t;
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			RType t = checkGreaterThanOrEqualExpression(exp,e1,e2);
			return t;
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			RType t = checkEqualsExpression(exp,e1,e2);
			return t;
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			RType t = checkNotEqualsExpression(exp,e1,e2);
			return t;
		}

		// IfThenElse (Ternary)
		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
			RType t = checkIfThenElseExpression(exp,e1,e2,e3);
			return t;	
		}

		// IfDefinedOtherwise
		case `<Expression e1> ? <Expression e2>` : {
			RType t = checkIfDefinedOtherwiseExpression(exp,e1,e2);
			return t;		
		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			RType t = checkImplicationExpression(exp,e1,e2);
			return t;
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			RType t = checkEquivalenceExpression(exp,e1,e2);
			return t;
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			RType t = checkAndExpression(exp,e1,e2);
			return t;
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			RType t = checkOrExpression(exp,e1,e2);
			return t;
		}
		
		// Match
		case `<Pattern p> := <Expression e>` : {
			RType t = checkMatchExpression(exp,p,e);
			return t;
		}

		// NoMatch
		case `<Pattern p> !:= <Expression e>` : {
			RType t = checkNoMatchExpression(exp,p,e);
			return t;
		}

		// Enumerator
		case `<Pattern p> <- <Expression e>` : {
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
		case `( <Expression ei> | <Expression er> | <{Expression ","}+ egs> )` : {
			RType t = checkReducerExpression(exp,ei,er,egs);
			return t;
		}
		
		// It
		case `it` : {
			return exp@rtype; 
		}
			
		// All 
		case `all ( <{Expression ","}+ egs> )` : {
			RType t = checkAllExpression(exp,egs);
			return t;
		}

		// Any 
		case `all ( <{Expression ","}+ egs> )` : {
			RType t = checkAnyExpression(exp,egs);
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
			
			// If insert is used anywhere in this case pattern, find the type being inserted and
			// check to see if it is correct.
			RType caseType = getCasePatternType(c);
			set[RType] failures = { };
			top-down-break visit(p) {
				case (Expression) `<Label l> <Visit v>` : 0; // no-op
				
				case (Statement) `<Label l> <Visit v>` : 0; // no-op
				
				case Statement ins : `insert <DataTarget dt> <Statement s>` : {
					RType stmtType = getInternalStatementType(s@rtype);
					if (! subtypeOf(stmtType, caseType)) {
						failures += makeFailType("Type of insert, <prettyPrintType(stmtType)>, does not match type of case, <prettyPrintType(caseType)>", s@\loc);
					}
				} 
			}
			RType retType = (size(failures) == 0) ? p@rtype : collapseFailTypes(failures);
			return retType;
		}
		
		case `default : <Statement b>` : {
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
	//throw "Function checkAnnotationAssignable not implemented!";
	return makeAssignableType(getWholeType(a@rtype),makeVoidType());
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
			RType rt = isInferredType(qn@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype;
			return makeAssignableType(rt,rt); 
		}
		
		// Subscript
		case `<Assignable al> [ <Expression e> ]` : {
			RType t = checkSubscriptAssignable(a,al,e);
			return t;
		}
		
		// Field Access
		case `<Assignable al> . <Name n>` : {
			RType t = checkFieldAccessAssignable(a,al,n);
			return t;
		}
		
		// If Defined or Default
		case `<Assignable al> ? <Expression e>` : {
			RType t = checkIfDefinedOrDefaultAssignable(a,al,e);
			return t;
		}
		
		// Annotation
		case `<Assignable al> @ <Name n>` : {
			RType t = checkAnnotationAssignable(a,al,n);
			return t;
		}
		
		// Tuple, with just one element
		case (Assignable)`< <Assignable ai> >` : {
			RType t = checkTupleAssignable(a, ai);
			return t;
		}

		// Tuple, with multiple elements
		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
			RType t = checkTupleAssignable(a, ai, al);
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
			if (isInferredType(qn@rtype)) {
				RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)];
				if (isInferredType(t)) {
					updateInferredTypeMappings(t,rt);
					return rt;
				} else {
					if (!subtypeOf(rt,t)) {
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
			return a@rtype;
		}
		
		// Field Access
		// NOTE: If we are assigning into a field, the assignable needs to already have a
		// type for this to make sense. So, don't propagate the bind any further, and
		// just return the existing type of the assignable.
		case `<Assignable al> . <Name n>` : {
			return a@rtype;
		}
		
		// If Defined or Default
		// This just pushes the binding down into the assignable on the left-hand
		// side of the ?, the default expression has no impact on the binding.
		case `<Assignable al> ? <Expression e>` : {
			return bindInferredTypesToAssignable(rt, al);
		}
		
		// Annotation
		// NOTE: If we are assigning into an annotation, this assignable needs to already
		// have a type for this to make sense. So, don't propagate the bind any further,
		// and just return the existing type of the assignable.
		case `<Assignable al> @ <Name n>` : {
			return a@rtype;
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
				return makeTupleType([bindInferredTypesToAssignable(tft,ali) | n <- [0..(getTupleFieldCount(rt)-1)], tft := tupleFieldTypes[n], ali := alist[n]]);  				
			} else if (!isTupleType(rt)) {
				return makeFailType("Type mismatch: this error should have already been caught!", a@\loc);
			} else {
				return makeFailType("Arity mismatch: this error should have already been caught!", a@\loc);
			}
		}

		case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
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
			return b@rtype;
		}
		
		// TODO: Pull out into own function for consistency
		case `catch <Pattern p> : <Statement b>` : {
			
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
	set[ROverloadedType] alternatives = isOverloadedType(pc@rtype) ? getOverloadOptions(pc@rtype) : { ROverloadedType(pc@rtype) };
	
	// Now, try each alternative, seeing if one matches.
	for (a <- alternatives) {
		list[Pattern] args = [ p | p <- ps ];
		list[RType] argTypes = [];
		RType potentialResultType;
		RType altType = a.overloadType;
		bool altHasLoc = (ROverloadedTypeWithLoc(_,_) := a);
		
		if (isFunctionType(altType)) {
			argTypes = getFunctionArgumentTypes(altType);
			potentialResultType = getFunctionReturnType(altType);
			if (altHasLoc) potentialResultType = potentialResultType[@at=a.overloadLoc];
		} else if (isConstructorType(altType)) {
			argTypes = getConstructorArgumentTypes(altType);
			potentialResultType = getConstructorResultType(altType);
			if (altHasLoc) potentialResultType = potentialResultType[@at=a.overloadLoc];
		}
		
		if (size(argTypes) == size(args)) {
			for (p <- args) {
				RType argType = head(argTypes); argTypes = tail(argTypes);
				if (! subtypeOf(p@rtype,argType)) {
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

		// TODO: Interpolation
		case (Pattern)`<StringLiteral sl>`  : {
			return makeStrType();
		}

		// TODO: Interpolation
		case (Pattern)`<LocationLiteral ll>`  : {
			return makeLocType();
		}

		case (Pattern)`<DateTimeLiteral dtl>`  : {
			return makeDateTimeType();
		}

		// TODO: See if we ever have this; a qualified name, not a name, is a Pattern
		case (Pattern)`<Name n>`: {
			return isInferredType(n@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(n@rtype)] : n@rtype; 
		}
		
		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			return isInferredType(qn@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// ReifiedType
		case `<BasicType t> ( <{Pattern ","}* pl> )` : {
			RType rt = checkReifiedTypePattern(pat,t,pl);
			return rt;
		}

		// CallOrTree
		case `<Pattern p1> ( <{Pattern ","}* pl> )` : {
			RType rt = checkCallOrTreePattern(pat,p1,pl);
			return rt;
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			RType rt = checkListPattern(pat,pl);
			return rt;
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			RType rt = checkSetPattern(pat,pl);
			return rt;
		}

		// Tuple
		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			RType rt = checkTrivialTuplePattern(pat,pi);
			return rt;
		}

		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			RType rt = checkTuplePattern(pat,pi,pl);
			return rt;
		}

		// TODO: Map: Need to figure out a syntax that works for matching maps

		// Typed Variable
		case (Pattern) `<Type t> <Name n>` : {
			return n@rtype;
		}

		// Multi Variable
		case `<QualifiedName qn> *` : {
			return isInferredType(qn@rtype) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)] : qn@rtype; 
		}

		// Descendant
		case `/ <Pattern p>` : {
			return p@rtype;
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			RType rt = checkVariableBecomesPattern(pat,n,p);
			return rt;
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			RType rt = checkTypedVariableBecomesPattern(pat,t,n,p);
			return rt;
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			RType rt = checkGuardedPattern(pat,t,p);
			return rt;
		}			
		
		// Anti
		case `! <Pattern p>` : {
			RType t = checkAntiPattern(pat,p);
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
	switch(pat) {
		case (Pattern)`<BooleanLiteral bl>` : {
			return pat@rtype;
		}

		case (Pattern)`<DecimalIntegerLiteral il>`  : {
			return pat@rtype;
		}

		case (Pattern)`<OctalIntegerLiteral il>`  : {
			return pat@rtype;
		}

		case (Pattern)`<HexIntegerLiteral il>`  : {
			return pat@rtype;
		}

		case (Pattern)`<RealLiteral rl>`  : {
			return pat@rtype;
		}

		// TODO: Interpolation
		case (Pattern)`<StringLiteral sl>`  : {
			return pat@rtype;
		}

		// TODO: Interpolation
		case (Pattern)`<LocationLiteral ll>`  : {
			return pat@rtype;
		}

		case (Pattern)`<DateTimeLiteral dtl>`  : {
			return pat@rtype;
		}

		case (Pattern)`<Name n>`: {
			if (isInferredType(n@rtype)) {
				RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(n@rtype)];
				if (isInferredType(t)) {
					updateInferredTypeMappings(t,rt);
					return rt;
				} else {
					if (! typeEquality(t,rt)) {
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
			if (isInferredType(qn@rtype)) {
				RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(qn@rtype)];
				if (isInferredType(t)) {
					updateInferredTypeMappings(globalSymbolTable,t,rt);
					return rt;
				} else {
					if (! typeEquality(t,rt)) {
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
			return pat@rtype;
		}

		// CallOrTree
		case `<Pattern p1> ( <{Pattern ","}* pl> )` : {
			return pat@rtype;
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			return pat@rtype;
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			return pat@rtype;
		}

		// Tuple
		case `<<Pattern pi>>` : {
			return pat@rtype;
		}

		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			return pat@rtype;
		}

		// TODO: Map: Need to figure out a syntax that works for matching maps

		// Typed Variable
		case (Pattern) `<Type t> <Name n>` : {
			return pat@rtype;
		}

		// Multi Variable
		case `<QualifiedName qn> *` : {
			return pat@rtype;
		}

		// Descendant
		// TODO: If Descendant is an inferred type, bind to value here -- this means that no type was given, so
		// it could actually match anything.
		case `/ <Pattern p>` : {
			return bindInferredTypesToPattern(rt, p);
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			return bindInferredTypesToPattern(rt, p);
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			return bindInferredTypesToPattern(rt, p);
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			return bindInferredTypesToPattern(rt, p);
		}			
		
		// Anti -- TODO see if this makes sense, check the interpreter
		case `! <Pattern p>` : {
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
			if (!subtypeOf(e@rtype,p@rtype)) return makeFailType("Type of pattern, <prettyPrintType(p@rtype)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
			return e@rtype; // TODO: Or p@rtype?
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			set[RType] whenTypes = { e@rtype | e <- es };
			if (checkForFail( whenTypes + p@rtype + e@rtype )) return collapseFailTypes( whenTypes + p@rtype + e@rtype );
			if (!subtypeOf(e@rtype,p@rtype)) return makeFailType("Type of pattern, <prettyPrintType(p@rtype)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
			return e@rtype; // TODO: Or p@rtype?
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
	globalSymbolTable.inferredTypeMap[getInferredTypeIndex(t)] = rt;
}

// Replace inferred with concrete types
public RType replaceInferredTypes(RType rt) {
	return visit(rt) { case RInferredType(n) => globalSymbolTable.inferredTypeMap[n] };
}

//
// Calculate the list of types assigned to a list of parameters
//
public list[RType] getParameterTypes(Parameters p) {
	list[RType] pTypes = [];

	if (`( <Formals f> )` := p && (Formals)`<{Formal ","}* fs>` := f) {
		for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += n@rtype;
		}
	} else if (`( <Formals f> ... )` := p && (Formals)`<{Formal ","}* fs>` := f) {
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
	globalSymbolTable = consolidateADTDefinitions(buildNamespace(t, sigMap),getModuleName(t));
	Tree td = decorateNames(t,globalSymbolTable);
	RType adtErrors = checkADTDefinitionsForConsistency(globalSymbolTable, getModuleName(t));
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

	str getNameOfImportedModule(ImportedModule im) {
		switch(im) {
			case `<QualifiedName qn> <ModuleActuals ma> <Renamings rn>` : {
				return prettyPrintName(convertName(qn));
			}
			case `<QualifiedName qn> <ModuleActuals ma>` : {
				return prettyPrintName(convertName(qn));
			}
			case `<QualifiedName qn> <Renamings rn>` : {
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
		if (`import <ImportedModule im> ;` := i || `extend <ImportedModule im> ;` := i) {
			Tree importTree = getModuleParseTree(getNameOfImportedModule(im));
			sigMap[i] = getModuleSignature(importTree);
		} 
	}

	return sigMap;
}

public RType checkADTDefinitionsForConsistency(SymbolTable table, RName moduleName) {
	set[RType] consistencyFailures = { };

	// Get back the ID for the name of the module being checked -- there should be only one matching
	// item. TODO: We may want to verify that here.
	STItemId moduleLayerId = getOneFrom(getModuleItemsForName(table, moduleName));
	
	// Check each ADT individually	
	for (n <- domain(table.adtMap)) {
		map[RName fieldName, RType fieldType] fieldMap = ( );

		// First check imported constructors. If we get errors, we would rather have them on the constructors
		// defined in the current module, since they are easier to fix -- checking them later preferences the
		// types assigned to field in imported types.
		for (ci <- table.adtMap[n].consItems, ci in table.scopeRel[table.topSTItemId]) {
			if (ConstructorItem(cn,params,_,_) := table.scopeItemMap[ci]) {
				for (RNamedType(nt,nn) <- params) {
					if (nn notin fieldMap) {
						fieldMap[nn] = nt;
					} else if (nn in fieldMap && !typeEquality(fieldMap[nn],nt)) {
						consistencyFailures += makeFailType("Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>",table.scopeItemMap[ci]@at);
					}
				}				
			} else {
				throw "checkADTDefinitionsForConsistency, unexpected constructor item <table.scopeItemMap[ci]>";
			}
		}
		
		// TODO: May be good to refactor out identical checking code
		for (ci <- table.adtMap[n].consItems, ci in table.scopeRel[moduleLayerId]) {
			if (ConstructorItem(cn,params,_,_) := table.scopeItemMap[ci]) {
				for (RNamedType(nt,nn) <- params) {
					if (nn notin fieldMap) {
						fieldMap[nn] = nt;
					} else if (nn in fieldMap && !typeEquality(fieldMap[nn],nt)) {
						consistencyFailures += makeFailType("Constructor <prettyPrintName(cn)> of ADT <prettyPrintName(n)> redefines the type of field <prettyPrintName(nn)> from <prettyPrintType(fieldMap[nn])> to <prettyPrintType(nt)>",table.scopeItemMap[ci]@at);
					}
				}				
			} else {
				throw "checkADTDefinitionsForConsistency, unexpected constructor item <table.scopeItemMap[ci]>";
			}
		}
	}
	
	if (size(consistencyFailures) > 0) 
		return collapseFailTypes(consistencyFailures);
	else
		return makeVoidType();
}

private SymbolTable globalSymbolTable = createNewSymbolTable();

// Check to see if the cases given cover the possible matches of the expected type.
// If a default is present this is automatically true, else we need to look at the
// patterns given in the various cases. 
public bool checkCaseCoverage(RType expectedType, Case+ options, SymbolTable table) {
	set[Case] defaultCases = { cs | cs <- options, `default: <Statement b>` := cs };
	if (size(defaultCases) > 0) return true;	
	
	set[Pattern] casePatterns = { p | cs <- options, `case <Pattern p> => <Replacement r>` := cs || `case <Pattern p> : <Statement b>` := cs };
	return checkPatternCoverage(expectedType, casePatterns, table);		
}

// Check to see if the patterns in the options set cover the possible matches of the
// expected type. This can be recursive, for instance with ADT types.
// TODO: Interpolation
// TODO: Need to expand support for matching over reified types	
public bool checkPatternCoverage(RType expectedType, set[Pattern] options, SymbolTable table) {

	// Check to see if a given use of a name is the same use that defines it. A use is the
	// defining use if, at the location of the name, there is a use of the name, and that use
	// is also the location of the definition of a new item.
	bool isDefiningUse(Name n, SymbolTable table) {
		loc nloc = n@\loc;
		println("CHECKING FOR DEFINING USE, name <n>, location <n@\loc>");
		if (nloc in table.itemUses) {
			println("location in itemUses");
			if (size(table.itemUses[nloc]) == 1) {
				println("only 1 item used, not overloaded");
				if (nloc in domain(table.itemLocations)) {
					println("location in itemLocations");
					set[STItemId] items = { si | si <- table.itemLocations[nloc], isItem(table.scopeItemMap[si]) };
					if (size(items) == 1) {
						println("location items = <items>");
						return (VariableItem(_,_,_) := table.scopeItemMap[getOneFrom(items)]);
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
	bool isDefiningPattern(Pattern p, RType expectedType, SymbolTable table) {
		if ((Pattern)`_` := p) {
			return true;
		} else if ((Pattern)`<Name n>` := p && isDefiningUse(n, table)) {
			return true;
		} else if ((Pattern)`<Type t> _` := p && convertType(t) == expectedType) {
			return true;
		} else if ((Pattern)`<Type t> <Name n>` := p && isDefiningUse(n, table) && convertType(t) == expectedType) {
			return true;
		} else if (`<Name n> : <Pattern pd>` := p) {
			return isDefiningPattern(pd, expectedType, table);
		} else if (`<Type t> <Name n> : <Pattern pd>` := p && convertType(t) == expectedType) {
			return isDefiningPattern(pd, expectedType, table);
		} else if (`[ <Type t> ] <Pattern pd>` := p && convertType(t) == expectedType) {
			return isDefiningPattern(pd, expectedType, table);
		}
		
		return false;
	}
	
	// Check to see if a 0 or more element pattern is empty (i.e., contains no elements)
	bool checkEmptyMatch({Pattern ","}* pl, RType expectedType, SymbolTable table) {
		return size([p | p <- pl]) == 0;
	}

	// Check to see if a 0 or more element pattern matches an arbitrary sequence of zero or more items;
	// this means that all the internal patterns have to be of the form x*, like [ xs* ys* ], since this
	// still allows 0 items total
	bool checkTotalMatchZeroOrMore({Pattern ","}* pl, RType expectedType, SymbolTable table) {
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
	bool checkTotalMatchOneOrMore({Pattern ","}* pl, RType expectedType, SymbolTable table) {
		list[Pattern] plst = [p | p <- pl];
		set[int] nonStarMatch = { n | n <- domain(plst), ! `<QualifiedName qn>*` := plst[n] };
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
			if (`[<{Pattern ","}* pl>]` := p) {
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
			if (`{<{Pattern ","}* pl>}` := p) {
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
		for (p <- options) {
			if (isDefiningPattern(p, expectedType, table)) return true;
		}
		return false;				
	}

	return false;	
}
