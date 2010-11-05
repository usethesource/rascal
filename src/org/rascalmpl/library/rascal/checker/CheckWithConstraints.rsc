@bootstrapParser
module rascal::checker::CheckWithConstraints

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;
import String;

import rascal::checker::ListUtils;
import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::SymbolTable;
import rascal::checker::Signature;
import rascal::checker::TypeRules;
import rascal::checker::Namespace;
import rascal::checker::TreeUtils;

import rascal::checker::constraints::Statements;

import rascal::syntax::RascalRascal;

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
//
// 9. For constraints, need to define a "within" constraint for insert, append, etc -- i.e., constructs that
//    must be used inside another construct. This should allow us to link up the constraint to the surrounding
//    construct to get the type information we need to check it correctly.
//

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
			return getTypeForName(globalSymbolTable, convertName(n), n@\loc);
		}
		
		case (Variable) `<Name n> = <Expression e>` : {
		        // NOTE: The only variable declarations are typed variable declarations. Declarations
			// of the form x = 5 are assignables. So, here we want to make sure the assignment
			// doesn't cause a failure, but beyond that we just return the type of the name,
			// which should be the same as, or a supertype of, the expression.
			RType nType = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
			return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : getTypeForName(globalSymbolTable, convertName(n), n@\loc);
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
			return checkForFail(toSet(getParameterTypes(ps))) ? collapseFailTypes(toSet(getParameterTypes(ps))) : getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps>` : 
			return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : getTypeForName(globalSymbolTable, convertName(n), n@\loc);
		case `<Type t> <FunctionModifiers ns> <Name n> <Parameters ps> throws <{Type ","}+ thrs> ` : 
			return checkForFail(toSet(getParameterTypes(ps)) + b@rtype) ? collapseFailTypes(toSet(getParameterTypes(ps)) + b@rtype) : getTypeForName(globalSymbolTable, convertName(n), n@\loc);
	}
	throw "checkFunction: unhandled signature <s>";
}

//
// The type of the function body is a failure if any of the statements is a failure type,
// else it is just void. Function bodies don't have types based on the computed results.
//
public RType checkFunctionBody(FunctionBody fb) {
	if (`{ <Statement* ss> }` := fb) {
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
	if ( hasRType(globalSymbolTable,n@\loc) ) return getTypeForName(globalSymbolTable, convertName(n), n@\loc); else return makeVoidType();
}

//
// If the name has a type annotation, return it, else just return a void type. A type
// on the name would most likely indicate a scope error.
//
public RType checkTagDeclaration(Tags t, Visibility v, Kind k, Name n, {Type ","}+ ts) {
	if ( hasRType(globalSymbolTable,n@\loc) ) return getTypeForName(globalSymbolTable, convertName(n), n@\loc); else return makeVoidType();
}
	
//
// The type of the rule is the failure type on the name of pattern if either is a
// failure type, else it is the type of the pattern (i.e., the type of the term
// rewritten by the rule).
//							
public RType checkRuleDeclaration(Tags t, Name n, PatternWithAction p) {
	if ( hasRType(globalSymbolTable, n@\loc )) {
		if (checkForFail({ getTypeForName(globalSymbolTable, convertName(n), n@\loc), p@rtype })) 
                        return collapseFailTypes({getTypeForName(globalSymbolTable, convertName(n), n@\loc), p@rtype});
	} 
	return p@rtype;
}

//
// The type of the test is either a failure type, if the expression has a failure type, or void.
//
public RType checkTestDeclaration(Test t) {
        if (`<Tags tgs> test <Expression exp>` := t || `<Tags tgs> test <Expression exp> : <StringLiteral sl>` := t) {
	        if (isFailType(exp@rtype)) return exp@rtype; else return makeVoidType();
        }
        throw "Unexpected syntax for test: <t>";
}

//
// The only possible error is on the ADT name itself, so check that for failures.
//
public RType checkAbstractADT(Tags ts, Visibility v, UserType adtType) {
	Name adtn = getUserTypeRawName(adtType);
	if (hasRType(globalSymbolTable, adtn@\loc))
		return getTypeForName(globalSymbolTable, convertName(adtn), adtn@\loc);
	return makeVoidType();
}

//
// Propagate upwards any errors registered on the ADT name or on the variants.
//
public RType checkADT(Tags ts, Visibility v, UserType adtType, {Variant "|"}+ vars) {
	set[RType] adtTypes = { };

	Name adtn = getUserTypeRawName(adtType);
	if (hasRType(globalSymbolTable,adtn@\loc))
		adtTypes = adtTypes + getTypeForName(globalSymbolTable, convertName(adtn), adtn@\loc);

	for (`<Name n> ( <{TypeArg ","}* args> )` <- vars) {
		if (hasRType(globalSymbolTable, n@\loc))
			adtTypes = adtTypes + getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
	if (hasRType(globalSymbolTable, aliasRawName@\loc)) {
		return getTypeForName(globalSymbolTable, convertName(aliasRawName), aliasRawName@\loc);
	}
	return makeVoidType();
}

//
// TODO: Implement once views are available in Rascal
//
public SymbolTable checkView(Tags ts, Visibility v, Name n, Name sn, {Alternative "|"}+ alts) {
	throw "checkView not yet implemented";
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
// Check the call or tree expression, which can be either a function or constructor, a node
// constructor, or a location
//
// (FUNCTION OR CONSTRUCTOR)
//
//      f : tf1 x ... x tfn -> tr, e1 : t1, ... , en : tn, t1 <: tf1, ..., tn <: tfn 
// ----------------------------------------------------------------------------------------------
//               f (e1, ..., en) : tr
//
// (NODE)
//
//      f : str, e1 : t1, ... , en : tn, isValidNodeName(f)
// ----------------------------------------------------------------------------------------------
//               f (e1, ..., en) : node
//
// (LOCATION)
//
//      f : loc, e1 : int, e2 : int, e3 : tuple[int,int], e4 : tuple[int,int]
// ----------------------------------------------------------------------------------------------
//               f (e1, e2, e3, e4) : loc
//
public Constraints checkCallOrTreeExpression(Constraints cs, Expression ep, Expression ec, {Expression ","}* es) {
        <cs, l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1];
	Constraint c1 = IsType(ep,t1); // t1 is the overall type of the expression: call result, node, or loc
	Constraint c2 = IsType(ec,t2); // t2 is the type of the function, node string, or existing loc
	cs.constraints = cs.constraints + { c1, c2 };
	
	list[RType] params = [ ];
	for (e <- es) {
	        <cs, l2> = makeFreshTypes(cs,1); t3 = l2[0];
		Constraint c3 = IsType(e,t3); // t3 is the type of each parameter
		params += t3;
		cs.constraints = cs.constraints + { c3 };
	}

	<cs, l3> = makeFreshTypes(cs,1); t4 = l3[0]; 
	Constraint c4 = IsType(t2,CallableType(params,t4)); // t4 is the result type of the call; this should unify with t2's type
	Constraint c5 = IsType(t1,t4); // t4 should also be the ultimate type of executing the expression
	cs.constraints = cs.constraints + { c4, c5 };

	return cs;	
}

//
// Check the list expression
//
//      e1 : t1, ... , en : tn, tl = lub(t1, ..., tn)
// ------------------------------------------------------
//               [ e1, ..., en ] : list[tl]
//
// NOTE: This rule is simplified a bit, below we also need to handle splicing
//
public Constraints checkListExpression(Constraints cs, Expression ep, {Expression ","}* es) {
	<cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1]; 
	Constraint c1 = IsType(ep,t1);
	Constraint c2 = IsType(t2,makeVoidType());
	cs.constraints = cs.constraints + { c1, c2 };

	list[RType] elements = [ t2 ]; 
	for (e <- es) { 
                <cs,l2> = makeFreshTypes(cs,1); t3 = l2[0]; 
		Constraint c3 = IsType(e,t3);
		cs.constraints = cs.constraints + { c3 };

		if (`[<{Expression ","}* el>]` !:= e) {
			Constraint c3a = SplicedListElement(t3);
			cs.constraints = cs.constraints + { c3a };
		}

		elements += t3;
        }

	<cs,l3> = makeFreshTypes(cs,1); t4 = l3[0]; 
	Constraint c4 = LubOf(elements,t4);
	Constraint c5 = IsType(t1, makeListType(t4));
	cs.constraints = cs.constraints + { c4, c5 };

	return cs;
}

//
// Check the set expression
//
//      e1 : t1, ... , en : tn, tl = lub(t1, ..., tn)
// ------------------------------------------------------
//               { e1, ..., en } : set[tl]
//
// NOTE: This rule is simplified a bit, below we also need to handle splicing
//
public Constraints checkSetExpression(Constraints cs, Expression ep, {Expression ","}* es) {
	<cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1]; 
	Constraint c1 = IsType(ep,t1);
	Constraint c2 = IsType(t2,makeVoidType());
	cs.constraints = cs.constraints + { c1, c2 };

	list[RType] elements = [ t2 ]; 
	for (e <- es) { 
                <cs,l2> = makeFreshTypes(cs,1); t3 = l2[0]; 
		Constraint c3 = IsType(e,t3);
		cs.constraints = cs.constraints + { c3 };

		if (`{<{Expression ","}* el>}` !:= e) {
			Constraint c3a = SplicedSetElement(t3);
			cs.constraints = cs.constraints + { c3a };
		}

		elements += t3;
        }

	<cs,l3> = makeFreshTypes(cs,1); t4 = l3[0]; 
	Constraint c4 = LubOf(elements,t4);
	Constraint c5 = IsType(t1, makeSetType(t4));
	cs.constraints = cs.constraints + { c4, c5 };

	return cs;
}

//
// Check the trivial tuple expression
//
//      e1 : t1
// ----------------------
//   < e1 > : tuple[t1]
//
public Constraints checkTrivialTupleExpression(Constraints cs, Expression ep, Expression ei) {
	<cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1]; 
	Constraint c1 = IsType(ep,t1);
	Constraint c2 = IsType(ei,t2);
	Constraint c3 = IsType(t1,makeTupleType([t2]));
	cs.constraints = cs.constraints + { c1, c2, c3 };
	return cs;
}

//
// Check the tuple expression
//
//      e1 : t1, ..., en : tn
// ------------------------------------------
//   < e1, ..., en > : tuple[t1, ..., tn]
//
public Constraints checkTupleExpression(Constraints cs, Expression ep, Expression ei, {Expression ","}* el) {
	<cs,l1> = makeFreshTypes(cs,2); t1 = l1[0]; t2 = l1[1];
	Constraint c1 = IsType(ep,t1);
	Constraint c2 = IsType(ei,t2);
	cs.constraints = cs.constraints + { c1, c2 };

	list[RType] elements = [ t2 ]; 

	for (e <- el) { 
                <cs,l2> = makeFreshTypes(cs,1); t3 = l2[0]; 
		Constraint c3 = IsType(e,t3);
		cs.constraints = cs.constraints + { c3 };
		elements += t3;
        }

	Constraint c4 = IsType(t1, makeTupleType(elements));
	cs.constraints = cs.constraints + { c4 };

	return cs;
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

tuple[Constraints,list[RType]] makeFreshTypes(Constraints cs, int n) {
        list[RType] ftlist = [FreshType(c) | c <- [cs.freshCounter .. (cs.freshCounter+n-1)] ];
	cs.freshCounter = cs.freshCounter + n;
	return < cs, ftlist >;
}

//
// Handle string templates
//
public RType checkStringTemplate(StringTemplate s) {
	switch(s) {
		case `for (<{Expression ","}+ gens>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
			set[RType] res = { e@rtype | e <- gens } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
		        list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
				        res = res + ipet@rtype;
			}
			if (checkForFail(res)) return collapseFailTypes(res);
			return makeStrType();
		}

		case `if (<{Expression ","}+ conds>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
			set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post };
		        list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
				        res = res + ipet@rtype;
			}
			if (checkForFail(res)) return collapseFailTypes(res);
			return makeStrType();
		}

		case `if (<{Expression ","}+ conds>) { <Statement* preThen> <StringMiddle bodyThen> <Statement* postThen> } else { <Statement* preElse> <StringMiddle bodyElse> <Statement* postElse> }` : {
			set[RType] res = { e@rtype | e <- conds } + { getInternalStatementType(st@rtype) | st <- preThen } + 
                                         { getInternalStatementType(st@rtype) | st <- postThen } +
                                         { getInternalStatementType(st@rtype) | st <- preElse } + { getInternalStatementType(st@rtype) | st <- postElse };
		        list[Tree] ipl = prodFilter(bodyThen, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
				        res = res + ipet@rtype;
			}
		        ipl = prodFilter(bodyElse, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
				        res = res + ipet@rtype;
			}
			if (checkForFail(res)) return collapseFailTypes(res);
			return makeStrType();
		}

		case `while (<Expression cond>) { <Statement* pre> <StringMiddle body> <Statement* post> }` : {
			set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
		        list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
				        res = res + ipet@rtype;
			}
			if (checkForFail(res)) return collapseFailTypes(res);
			return makeStrType();
		}

		case `do { <Statement* pre> <StringMiddle body> <Statement* post> } while (<Expression cond>)` : {
			set[RType] res = { getInternalStatementType(st@rtype) | st <- pre } + { getInternalStatementType(st@rtype) | st <- post } + cond@rtype;
		        list[Tree] ipl = prodFilter(body, 
                                bool(Production prd) { return prod(_,\cf(sort("Expression")),_) := prd || prod(_,\cf(sort("StringTemplate")),_) := prd; });
			for (ipe <- ipl) {
			        if (`<Expression ipee>` := ipe)
			                res = res + ipee@rtype;
				else if (`<StringTemplate ipet>` := ipe)
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
		case `case <PatternWithAction p>` : {
			
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
	RType fieldType = getFieldType(partType, convertName(n), globalSymbolTable, ap@\loc);
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
	RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
			RType rt = getTypeForName(globalSymbolTable, RSimpleName("_"), a@\loc);
			return makeAssignableType(rt,rt); 
		}

		// Variable with an actual name
		case (Assignable)`<QualifiedName qn>` : {
			RType rt = getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
			return makeAssignableType(rt,rt); 
		}
		
		// Subscript
		case `<Assignable al> [ <Expression e> ]` : {
			return checkSubscriptAssignable(a,al,e);
		}
		
		// Field Access
		case `<Assignable al> . <Name n>` : {
			return checkFieldAccessAssignable(a,al,n);
		}
		
		// If Defined or Default
		case `<Assignable al> ? <Expression e>` : {
			return checkIfDefinedOrDefaultAssignable(a,al,e);
		}
		
		// Annotation
		case `<Assignable al> @ <Name n>` : {
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
		        RType varType = getTypeForNameLI(globalSymbolTable, RSimpleName("_"), a@\loc);
		        if (isInferredType(varType)) {
			        RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(varType)];
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
		        RType varType = getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc);
			if (isInferredType(varType)) {
				RType t = globalSymbolTable.inferredTypeMap[getInferredTypeIndex(varType)];
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
		case `<Assignable al> [ <Expression e> ]` : {
		        RType partType = getPartType(a@rtype);
			if (! subtypeOf(rt, partType))
			        return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to subscript with type <prettyPrintType(partType)>", a@\loc);
			return getWholeType(a@rtype);
		}
		
		// Field Access
		// Check to see if the part type of the assignable matches the binding type. It doesn't make
		// sense to push this any further down, since the type we have to compare against is just the
		// part type, not the whole type.
		case `<Assignable al> . <Name n>` : {
		        RType partType = getPartType(a@rtype);
			if (! subtypeOf(rt, partType))
			        return makeFailType("Error, cannot assign expression of type <prettyPrintType(rt)> to field with type <prettyPrintType(partType)>", 
                                                    a@\loc);
			return getWholeType(a@rtype);
		}
		
		// If Defined or Default
		// This just pushes the binding down into the assignable on the left-hand
		// side of the ?, the default expression has no impact on the binding.
		case `<Assignable al> ? <Expression e>` : {
			return bindInferredTypesToAssignable(rt, al);
		}
		
		// Annotation
		// Check to see if the part type of the assignable matches the binding type. It doesn't make
		// sense to push this any further down, since the type we have to compare against is just the
		// part type, not the whole type.
		case `<Assignable al> @ <Name n>` : {
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
			else {
				RType boundType = bindInferredTypesToPattern(p@rtype, p);
				if (isFailType(boundType)) return makeStatementType(boundType);
				return b@rtype;
			}
		}
	}
}

public RType checkLabel(Label l) {
	if ((Label)`<Name n> :` := l && hasRType(globalSymbolTable, n@\loc)) {
		RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
		case `visit (<Expression se>) { <Case+ cs> }` : {
			set[RType] caseTypes = { c@rtype | c <- cs };
			if (checkForFail( caseTypes + se@rtype )) return collapseFailTypes(caseTypes + se@rtype);
			RType caseLubType = lubSet(caseTypes);
			//if (subtypeOf(caseLubType, se@rtype)) 
				return se@rtype;
			//else
				//return makeFailType("Visit cases must all be subtypes of the type of the visited expression",v@\loc); 
		}
		
		case `<Strategy st> visit (<Expression se>) { <Case+ cs> }` : {
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
	RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
	RType rt = getTypeForName(globalSymbolTable, convertName(n), n@\loc);
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
			list[RType] retTypes = [ getTypeForName(globalSymbolTable, RSimpleName("<n>"), n@\loc) | n <- names ];
			if (checkForFail(toSet(retTypes))) return collapseFailTypes(toSet(retTypes));
			return makeStrType();
		}

		case (Pattern)`_` : {
		        RType patType = getTypeForName(globalSymbolTable, RSimpleName("_"), pat@\loc);
			//println("For pattern _ at location <pat@\loc> found type(s) <patType>");
			return patType;
		}
		
		case (Pattern)`<Name n>`: {
			return getTypeForName(globalSymbolTable, convertName(n), n@\loc);
		}
		
		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			return getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
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
			return getTypeForName(globalSymbolTable, convertName(n), n@\loc);
		}

		// Multi Variable
		case (Pattern) `_ *` : {
			return getTypeForName(globalSymbolTable, RSimpleName("_"), pat@\loc);
		}
		
		case (Pattern) `<QualifiedName qn> *` : {
			return getTypeForName(globalSymbolTable, convertName(qn), qn@\loc);
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
 			        globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getContainerElementType(pt))] :
				getContainerElementType(pt);
		} else if (isListType(pt)) {
			elementIsInferred = (isInferredType(getListElementType(pt))) ? true : false;
			elementType = (isInferredType(getListElementType(pt))) ?
 			        globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getListElementType(pt))] : 
			        getListElementType(pt);
		} else if (isSetType(pt)) {
			elementIsInferred = (isInferredType(getSetElementType(pt))) ? true : false;
			elementType = (isInferredType(getSetElementType(pt))) ?
 			        globalSymbolTable.inferredTypeMap[getInferredTypeIndex(getSetElementType(pt))] : 
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
			    RType pt = getTypeForName(globalSymbolTable, RSimpleName("<n>"), n@\loc);
			    RType t = (isInferredType(pt)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
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
			RType t = (isInferredType(pt)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(pt)] : pt;
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
			RType nType = getTypeForNameLI(globalSymbolTable,convertName(n),n@\loc);
			RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
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
			RType nType = getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc);
			RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
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
			return bindInferredTypesToMV(rt, getTypeForNameLI(globalSymbolTable,RSimpleName("_"),pat@\loc), pat);
		}
		
		case (Pattern) `<QualifiedName qn> *` : {
			return bindInferredTypesToMV(rt, getTypeForNameLI(globalSymbolTable,convertName(qn),qn@\loc), pat);
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
			        set[RType] rts = reachableTypes(globalSymbolTable, rt);
				RType bt = bindInferredTypesToPattern(lubSet(rts), p);
				return isFailType(bt) ? bt : rt;
			} else if ( (! isInferredType(p@rtype)) && (hasDeferredTypes(p@rtype))) {
			        set[RType] rts = reachableTypes(globalSymbolTable, rt);
				rts = { rtsi | rtsi <- rts, subtypeOf(rtsi, p@rtype)};
				RType bt = bindInferredTypesToPattern(lubSet(rts), p);
				return isFailType(bt) ? bt : rt;
			} else {
			        set[RType] rts = reachableTypes(globalSymbolTable, rt);
				if (p@rtype in rts) return rt;
				return makeFailType("Pattern type <prettyPrintType(p@rtype)> cannot appear in type <prettyPrintType(rt)>", pat@\loc);
			}
		}

		// Variable Becomes
		case (Pattern) `<Name n> : <Pattern p>` : {
			RType boundType = bindInferredTypesToPattern(rt, p);
			if (! isFailType(boundType)) {
        			RType nType = getTypeForNameLI(globalSymbolTable,convertName(n),n@\loc);
	        		RType t = (isInferredType(nType)) ? globalSymbolTable.inferredTypeMap[getInferredTypeIndex(nType)] : nType;
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
		case `<Pattern p> => <Expression e>` : {
			if (checkForFail( { p@rtype, e@rtype } )) return collapseFailTypes( { p@rtype, e@rtype } );
			RType boundType = bindInferredTypesToPattern(p@rtype, p);
			if (isFailType(boundType)) return boundType;
			if (!subtypeOf(e@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(e@rtype)>, must be identical.", pat@\loc); 
			return p@rtype; 
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			set[RType] whenTypes = { e@rtype | e <- es };
			if (checkForFail( whenTypes + p@rtype + er@rtype )) return collapseFailTypes( whenTypes + p@rtype + er@rtype );
			RType boundType = bindInferredTypesToPattern(p@rtype, p);
			if (isFailType(boundType)) return boundType;
			if (!subtypeOf(er@rtype,boundType)) return makeFailType("Type of pattern, <prettyPrintType(boundType)>, and action expression, <prettyPrintType(er@rtype)>, must be comparable.", pat@\loc); 
			return p@rtype; 
		}
		
		case `<Pattern p> : <Statement s>` : {
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
	if ((DataTarget)`<Name n> :` := dt && isFailType(getTypeForName(globalSymbolTable,convertName(n),n@\loc))) 
		return getTypeForName(globalSymbolTable,convertName(n),n@\loc);		
	return makeVoidType();
}

//
// Check the type of the target. This just propagates failures (for instance, from using a target
// name that is not defined), otherwise assigning a void type.
//
public RType checkTarget(Target t) {
	if ((Target)`<Name n>` := t && isFailType(getTypeForName(globalSymbolTable,convertName(n),n@\loc))) 
		return getTypeForName(globalSymbolTable,convertName(n),n@\loc);		
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
				pTypes += getTypeForName(globalSymbolTable,convertName(n),n@\loc);
		}
	} else if (`( <Formals f> ... )` := p && (Formals)`<{Formal ","}* fs>` := f) {
		for ((Formal)`<Type t> <Name n>` <- fs) {
				pTypes += getTypeForName(globalSymbolTable,convertName(n),n@\loc);
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
// TODO: Need to expand support for matching over reified types	
public bool checkPatternCoverage(RType expectedType, set[Pattern] options, SymbolTable table) {

	// Check to see if a given use of a name is the same use that defines it. A use is the
	// defining use if, at the location of the name, there is a use of the name, and that use
	// is also the location of the definition of a new item.
	bool isDefiningUse(Name n, SymbolTable table) {
		loc nloc = n@\loc;
		if (nloc in table.itemUses) {
			if (size(table.itemUses[nloc]) == 1) {
				if (nloc in domain(table.itemLocations)) {
					set[STItemId] items = { si | si <- table.itemLocations[nloc], isItem(table.scopeItemMap[si]) };
					if (size(items) == 1) {
						return (VariableItem(_,_,_) := table.scopeItemMap[getOneFrom(items)]);
					} else if (size(items) > 1) {
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
	globalSymbolTable = buildNamespace(t, sigMap);
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


public SymbolTable justGenerateTable(Tree t) {
	println("TYPE CHECKER: Getting Imports for Module");
	list[Import] imports = getImports(t);
	println("TYPE CHECKER: Got Imports");
	
	println("TYPE CHECKER: Generating Signature Map");
	SignatureMap sigMap = populateSignatureMap(imports);
	println("TYPE CHECKER: Generated Signature Map");
	
	println("TYPE CHECKER: Generating Symbol Table"); 
	symbolTable = buildNamespace(t, sigMap);
	println("TYPE CHECKER: Generated Symbol Table");
	
	return symbolTable;
}

public Tree typecheckTreeWithExistingTable(SymbolTable symbolTable, Tree t) {
	globalSymbolTable = symbolTable;
	
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