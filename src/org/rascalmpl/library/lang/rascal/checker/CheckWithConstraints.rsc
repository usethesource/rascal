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
module lang::rascal::checker::CheckWithConstraints

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;
import String;
import DateTime;
import Node;
import Graph;

import lang::rascal::checker::ListUtils;
import lang::rascal::types::Types;
import lang::rascal::types::SubTypes;
import lang::rascal::types::Lubs;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::types::TypeSignatures;
import lang::rascal::scoping::ResolveNames;
import lang::rascal::scoping::ScopedTypes;
import lang::rascal::checker::TreeUtils;

import constraints::Constraint;

import lang::rascal::checker::constraints::Constraints;
import lang::rascal::checker::constraints::Statement;
import lang::rascal::checker::constraints::Expression;
import lang::rascal::checker::constraints::Variable;
import lang::rascal::checker::constraints::BuiltIns;
import lang::rascal::checker::constraints::Fields;
import lang::rascal::checker::constraints::PatternWithAction;
import lang::rascal::checker::constraints::Visit;
import lang::rascal::checker::constraints::Case;
import lang::rascal::checker::constraints::StringTemplate;
import lang::rascal::checker::constraints::Catch;
import lang::rascal::checker::constraints::Assignable;

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
//
// 9. For constraints, need to define a "within" constraint for insert, append, etc -- i.e., constructs that
//    must be used inside another construct. This should allow us to link up the constraint to the surrounding
//    construct to get the type information we need to check it correctly.
//

public str getTopLevelItemDesc(Toplevel t) {
    switch(t) {
        // Variable declaration
        case (Toplevel) `<Tags tgs> <Visibility v> <Type typ> <{Variable ","}+ vs> ;` : { 
            return "Variable (location <t@\loc>)";
        }

        // Abstract (i.e., without a body) function declaration
        case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> ;` : { 
            return "Abstract Function (location <t@\loc>)";
        }
 
        // Concrete (i.e., with a body) function declaration
        case (Toplevel) `<Tags tgs> <Visibility v> <Signature s> <FunctionBody fb>` : {
            return "Function (location <t@\loc>)";
        }
            
        // Annotation declaration
        case (Toplevel) `<Tags tgs> <Visibility v> anno <Type typ> <Type otyp> @ <Name n> ;` : {
            return "Annotation (location <t@\loc>)";
        }
                                
        // Tag declaration
        case (Toplevel) `<Tags tgs> <Visibility v> tag <Kind k> <Name n> on <{Type ","}+ typs> ;` : {
            return "Tag (location <t@\loc>)";
        }
            
        // Rule declaration
        case (Toplevel) `<Tags tgs> rule <Name n> <PatternWithAction pwa> ;` : {
            return "Rule (location <t@\loc>)";
        }
            
        // Test
        case (Toplevel) `<Test tst> ;` : {
            return "Test (location <t@\loc>)";
        }
                            
        // ADT without variants
        case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> ;` : {
            return "Abstract ADT (location <t@\loc>)";
        }
            
        // ADT with variants
        case (Toplevel) `<Tags tgs> <Visibility v> data <UserType typ> = <{Variant "|"}+ vars> ;` : {
            return "ADT (location <t@\loc>)";
        }

        // Alias
        case (Toplevel) `<Tags tgs> <Visibility v> alias <UserType typ> = <Type btyp> ;` : {
            return "Alias (location <t@\loc>)";
        }
    }
    throw "checkToplevel: Unhandled toplevel item <t>";
}

public ConstraintBase gatherConstraints(STBuilder st, Tree t) {
    ConstraintBase cb = makeNewConstraintBase(st.inferredTypeMap, st.nextScopeId);
//    map[loc,Constraints] constraintsByLoc = ( );
    
    visit(t) {
        case `<Assignable a>` : cb = gatherAssignableConstraints(st,cb,a);
        
        case `<Case c>` : cb = gatherCaseConstraints(st,cb,c);
        
        case `<Catch c>` : cb = gatherCatchConstraints(st,cb,c);
        
        case `<Expression exp>` : cb = gatherExpressionConstraints(st,cb,exp);
        
        case `<PatternWithAction pwa>` : cb = gatherPatternWithActionConstraints(st,cb,pwa);
        
        case `<Statement stmt>` : cb = gatherStatementConstraints(st,cb,stmt);
        
        case `<StringTemplate s>` : cb = gatherStringTemplateConstraints(st,cb,s);
        
        case `<Variable v>` : cb = gatherVariableConstraints(st,cb,v);
        
        case `<Visit v>` : cb = gatherVisitConstraints(st,cb,v);
        
//        case `<Toplevel t>` : {
//            if (size(cb.constraints) > 0) {
//                println("Encountered top level item <getTopLevelItemDesc(t)>, partially reducing constraints, current set size <size(cb.constraints)>");
//                <cbp,whys> = multiStepReduction(st,cb);
//                println("Finished reducing constraints, new constraint set has size <size(cbp.constraints)>");
//                constraintsByLoc[t@\loc] = cbp.constraints;
//                cbp.constraints = { };
//                cb = cbp;
//            }
 //       }  
    }
    
    // Now, take out all the sets from the constraint set map and put them into the resulting constraint base
//    Constraints res = { };
//    for (l <- domain(constraintsByLoc)) res += constraintsByLoc[l];
//    cb.constraints = res;
    
    return cb;
}

private STBuilder globalSTBuilder = createNewSTBuilder();

// Check to see if the cases given cover the possible matches of the expected type.
// If a default is present this is automatically true, else we need to look at the
// patterns given in the various cases. 
public bool checkCaseCoverage(RType expectedType, Case+ options, STBuilder table) {
	set[Case] defaultCases = { cs | cs <- options, `default: <Statement b>` := cs };
	if (size(defaultCases) > 0) return true;	
	
	set[Pattern] casePatterns = { p | cs <- options, `case <Pattern p> => <Replacement r>` := cs || `case <Pattern p> : <Statement b>` := cs };
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
	bool checkEmptyMatch({Pattern ","}* pl, RType expectedType, STBuilder table) {
		return size([p | p <- pl]) == 0;
	}

	// Check to see if a 0 or more element pattern matches an arbitrary sequence of zero or more items;
	// this means that all the internal patterns have to be of the form x*, like [ xs* ys* ], since this
	// still allows 0 items total
	bool checkTotalMatchZeroOrMore({Pattern ","}* pl, RType expectedType, STBuilder table) {
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
	bool checkTotalMatchOneOrMore({Pattern ","}* pl, RType expectedType, STBuilder table) {
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

//public CT oneStepReduce(STBuilder st, ConstraintBase cb, Pipeline cfp) {
//    for (cfpi <- cfp) {
//        <cbp,why,b> = cfpi(st,cb); if (b) return <cbp,why,b>;
//    }
//    return <cb,emptyWhy("Reducer"),false>;
//}

//
// START OF CONSTRAINT SOLVING ROUTINES
//

//alias WhyTuple = tuple[str rulename, set[Constraint] matched, set[Constraint] added, set[Constraint] removed, map[int,RType] oldVars, map[int,RType] newVars];
//alias WhySteps = list[WhyTuple];
//public WhyTuple emptyWhy(str rulename) = <rulename, {},{},{},(),()>;
//public WhyTuple makeWhy(str rulename, set[Constraint] matched, set[Constraint] added, set[Constraint] removed, map[int,RType] oldVars, map[int,RType] newVars) = < rulename, matched, added, removed, oldVars, newVars > ;
//public WhyTuple makeWhy(str rulename, set[Constraint] matched, set[Constraint] added, set[Constraint] removed) = < rulename, matched, added, removed, ( ), ( ) > ;

//alias CT = tuple[ConstraintBase newCB, WhyTuple why, bool stepTaken];
//alias ConstraintFun = CT(STBuilder, ConstraintBase, Constraint);
//alias Pipeline = list[ConstraintFun];
 
//
// CONSTRAINT: TreeIsType(Tree t, loc at, RType treeType)
//
// TreeIsType constraints are gathered during the constraint gathering phase,
// associating each tree / location with either a concrete or an inferred
// type. Two functions are given below for TreeIsType constraints. In the
// first, we have two constraints over the same tree, which introduces a
// type equality constraint: if T1 : t1 and T1 : t2, then t1 = t2. The
// second function is used for error propagation, and isn't constraint
// solving per se, it just provides a nice method to propagate errors.
// This says that if T1 : t1 and T1 : t2, and either t1 and t2 (or both)
// are fail types, then T1 : t3, where t3 is the merged failure information
// from t1 and t2.
//
//public CT unifyTreeIsType(STBuilder st, ConstraintBase cb) {
//    if ({_*,ti1:TreeIsType(_,l1,rt1),ti2:TreeIsType(_,l1,rt2)} := cb.constraints, 
//        !isFailType(rt1), 
//        !isFailType(rt2), 
//        <cs,true> := unifyTypes(cb,rt1,rt2,l1)) 
//    {
//        cb.constraints = cb.constraints - ti1 + cs;
//        return < cb, makeWhy("Tree is Type", {ti1,ti2}, cs, {ti1}), true >;
//    } 
//
//    return < cb, emptyWhy("Tree is Type"), false >;
//}
//
//public CT unifyTreeIsTypeWithFailures(STBuilder st, ConstraintBase cb) {
//    if ({_*,ti1:TreeIsType(_,l1,rt1),ti2:TreeIsType(_,l1,rt2)} := cb.constraints, 
//        isFailType(rt1) || isFailType(rt2)) 
//    {
//        if (isFailType(rt1) && isFailType(rt2)) {
//            Constraint c = TreeIsType(t1,l1,collapseFailTypes({rt1,rt2}));
//            cb.constraints = cb.constraints - ti1 - ti2 + c;
//            return < cb, makeWhy("Tree is Type with Failures", { ti1, ti2 }, { c }, { ti1, ti2 }), true >; 
//        } else if (isFailType(rt1)) {
//            cb.constraints = cb.constraints - ti2;
//            return < cb, makeWhy("Tree is Type with Failures", { ti1, ti2 }, { }, { ti2 }), true >; 
//        } else if (isFailType(rt2)) {
//            cb.constraints = cb.constraints - ti1;
//            return < cb, makeWhy("Tree is Type with Failures", { ti1, ti2 }, { }, { ti1 }), true >; 
//        } else {
//            throw "Something odd is going on, at least one of the types must be a fail type to get here!";
//        }
//    } 
//
//    return <cb, emptyWhy("Tree is Type with Failures"), false>;
//}

//
// CONSTRAINT: TypesAreEqual(RType left, RType right, loc at)
//
// TypesAreEqual constraints indicate that two types, t1 and t2, are equal.
// We handle three cases here. In the first two cases, we have a direct assignment
// from an inference var to a concrete type, e.g., t0 = int, or bool = t1.
// In this case we add this equality information to the inference var map
// unless it conflicts with information already present. The third case unifies
// both types, which may each have type variables. This creates new TypesAreEqual
// constraints. If we can unify the types.
//
// TODO: We will have trouble here if we have circular constraints, i.e., t(1) = list[t(1)], where
// we would have an infinite expansion. We need to add a check to avoid this case.
//
// TODO: I think we handle type inference vars, generated during the name resolution
// phase, elsewhere, but we may need handling here as well.
//
//public CT unifyTypesAreEqual(STBuilder st, ConstraintBase cb) {
//    RType oldn;
//    bool hasOldn = false;
//
//    // In this case, we have an equality like InferenceVar(3) = int, so we save
//    // that into the map. This way, future references to InferenceVar(3) will get
//    // the correct type. 
//    if ({_*,ti1:TypesAreEqual(rt1:InferenceVar(n),rt2,at)} := cb.constraints, 
//        isConcreteType(st,cb,rt2)) 
//    {
//        if (n in cb.inferredTypeMap, !isFailType(cb.inferredTypeMap[n]), !equivalent(rt2,cb.inferredTypeMap[n])) {
//            oldn = cb.inferredTypeMap[n]; hasOldn = true;
//            cb.inferredTypeMap[n] = makeFailType("Attempt to constrain type to <prettyPrintType(cb.inferredTypeMap[n])> and <prettyPrintType(rt2)>",at);
//        } else {
//            cb.inferredTypeMap[n] = instantiateAllInferenceVars(st,cb,rt2);
//        }
//        cb.constraints = cb.constraints - ti1;
//        return <cb, makeWhy("Types are Equal", { ti1 }, { }, { ti1 }, hasOldn ? (n : oldn) : ( ), ( n : cb.inferredTypeMap[n] ) ), true>;
//    }
//    
//    if ({_*,ti1:TypesAreEqual(rt1,rt2:InferenceVar(n),at)} := cb.constraints, 
//        isConcreteType(st,cb,rt1)) 
//    {
//        if (n in cb.inferredTypeMap, !isFailType(cb.inferredTypeMap[n]), !equivalent(rt1,cb.inferredTypeMap[n])) {
//            oldn = cb.inferredTypeMap[n]; hasOldn = true;
//            cb.inferredTypeMap[n] = makeFailType("Attempt to constrain type to <prettyPrintType(cb.inferredTypeMap[n])> and <prettyPrintType(rt1)>",at);
//        } else {
//            cb.inferredTypeMap[n] = instantiateAllInferenceVars(st,cb,rt1);
//        }
//        cb.constraints = cb.constraints - ti1;
//        return <cb, makeWhy("Types are Equal", { ti1 }, { }, { ti1 },  hasOldn ? (n : oldn) : ( ), ( n : cb.inferredTypeMap[n] ) ), true>;
//    }
//    
//    if ({_*,ti1:TypesAreEqual(rt1,rt2,at)} := cb.constraints, 
//        <cs,true> := unifyTypes(cb,rt1,rt2,at)) 
//    {
//        cb.constraints = cb.constraints - ti1 + cs;
//        return <cb, makeWhy("Types are Equal", { ti1 }, cs, { ti1 }), true>;
//    }
//    
//    if ({_*,ti1:TypesAreEqual(rt1,rt2,at)} := cb.constraints, RInferredType(tnum) := rt1, isInferenceType(st,cb,rt1), isConcreteType(st,cb,rt2)) {
//        set[int] typesNums = { tnum };
//        solve(typeNums) typeNums = typeNums + { tnn | tn <- typeNums, RInferredType(tnn) := cb.inferredTypeMap[tn] || InferenceVar(tnn) := cb.inferredTypeMap[tn] };
//           
//        ;
//    }
//
//    if ({_*,ti1:TypesAreEqual(rt1,rt2,at)} := cb.constraints, RInferredType(tnum) := rt2, isInferenceType(st,cb,rt2), isConcreteType(st,cb,rt1)) {
//        ;
//    }
//    
//    return <cb, emptyWhy("Types are Equal"), false>;
//}

//
// CONSTRAINT: BindableToCase(RType subject, RType caseType, SolveResult sr, loc at)
//
// TODO: IMPLEMENT
//
//public CT unifyBindableToCase(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Bindable to Case"), false>;
//}

//
// CONSTRAINT: Assignable(RType rvalue, RType lvalue, RType result, loc at)
//
//public CT unifyAssignable(STBuilder st, ConstraintBase cb) {
//    if ({_*,abl:Assignable(tp,at,lhs,rhs,rv,lv,res)} := cb.constraints, isConcreteType(st,cb,lv), isConcreteType(st,cb,rv)) {
//        cb.constraints = cb.constraints - abl;
//        
//        while (InferenceVar(n) := lv) lv = cb.inferredTypeMap[n];
//        while (InferenceVar(n) := rv) rv = cb.inferredTypeMap[n];
//        
//        if (!subtypeOf(rv,lv)) {
//            Constraint c = TreeIsType(tp,tp@\loc,makeFailType("Cannot assign <rhs>, of type <prettyPrintType(rv)>, to <lhs>, which expects type <prettyPrintType(lv)>",tp@\loc));
//            cb.constraints = cb.constraints + c;
//            return  < cb, makeWhy("Assignable with Result", { abl }, { c }, { abl }), true >;
//        }
//
//        // TODO: What if we have inferred types (RInferredType)?
//        // If so, we need to collect all the possible instantiations of that type
//        // But also, isConcreteType will have failed, so this won't match above.
//        // So, we need code that handles this case explicitly.
//        return  < cb, makeWhy("Assignable with Result", { abl }, { }, { abl }), true >;
//    }
//    
//    return < cb, emptyWhy("Assignable with Result"), false>;
//}
//
////
//// CONSTRAINT: Assignable(RType rvalue, RType lvalue, loc at)
////
//public CT unifyAssignableWithValue(STBuilder st, ConstraintBase cb) {
//    if ({_*,abl:Assignable(tp,at,lhs,rhs,rv,lv)} := cb.constraints, isConcreteType(st,cb,lv), isConcreteType(st,cb,rv)) {
//        cb.constraints = cb.constraints - abl;
//        
//        while (InferenceVar(n) := lv) lv = cb.inferredTypeMap[n];
//        while (InferenceVar(n) := rv) rv = cb.inferredTypeMap[n];
//        
//        if (!subtypeOf(rv,lv)) {
//            Constraint c = TreeIsType(tp,tp@\loc,makeFailType("Cannot assign <rhs>, of type <prettyPrintType(rv)>, to <lhs>, which expects type <prettyPrintType(lv)>",tp@\loc));
//            cb.constraints = cb.constraints + c;
//            return  < cb, makeWhy("Assignable", { abl }, { c }, { abl }), true >;
//        }     
//
//        return  < cb, makeWhy("Assignable", { abl }, { }, { abl }), true >;
//    }
//    
//    return < cb, emptyWhy("Assignable"), false>;
//}
//
////
//// NOTE: We can only introduce a new type in certain assignable scenarios. These are:
//// * a standard assignment to a name, e.g., x = 3
//// * a tuple assignment, e.g. < x > = 4 or < x, y > = < 5, 6>
//// * a bracket assignment, e.g. ( x ) = 3 (NOT YET SUPPORTED)
//// * an if defined or default assignment, e.g. x?3 = 4
////
//// Subscript, field access, and annotation assignment assignables cannot introduce
//// a new name, as it wouldn't make sense to start out with something like x@y = 4
//// if x has never been defined.
////
//// These rules are used here, in order to gather the assignment constraints which can
//// introduce new names. Once introduced, these names can be used in any assignable, but
//// they need to be assigned into first.
////
//// TODO: The constraints are unordered. The name resolution stage is not, though. So,
//// we probably want to introduce an ordering constraint there that we cannot here.
//// This code will treat the case where we have x.f = 3, then later x = v (where v has
//// field f) as being the same as the correct case, where we have x = v then x.f = 3.
//// The name resolver should verify that names introduced in assignables are introduced
//// using the above rules, flagging incorrect orders as scope errors.
////
//
////
//// CONSTRAINT: ComboAssignable(Tree assignable, loc at)
////
//public CT unifyComboAssignable(STBuilder st, ConstraintBase cb) {
//    if ({_*,ca:ComboAssignable(t,at)} := cb.constraints) {
//        cb.constraints = cb.constraints - ca;
//        if ((Assignable)`< <Assignable ai> >` := t || 
//            (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` := t ||
//            (Assignable)`(<Assignable ac>)` := t)
//        {
//            Constraint c = ConstrainType(typeForLoc(cb, at),makeFailType("Cannot use assignable as part of combo operator"),at);
//            cb.constraints = cb.constraints + c;
//            return < cb, makeWhy("Combo Assignable", { ca }, { c }, { ca }), true >;
//        }
//        return < cb, makeWhy("Combo Assignable", { ca }, { }, { ca }), true >;
//    }
//    return <cb, emptyWhy("Combo Assignable"), false>;
//}
//
//
////
//// CONSTRAINT: BindsRuntimeException(Tree pat, SolveResult sr, loc at)
////
//// TODO: IMPLEMENT
////
//public CT unifyBindsRuntimeException(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Binds Runtime Exception"), false>;
//}
//
////
//// CONSTRAINT: CaseIsReachable(RType caseType, RType expType, SolveResult sr, loc at)
////
//// TODO: IMPLEMENT
////
//public CT unifyCaseIsReachable(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Case is Reachable"), false>;
//}
//
////
//// CONSTRAINT: SubtypeOf(RType left, RType right, SolveResult sr, loc at)
////
//public CT unifySubtypeOf(STBuilder st, ConstraintBase cb) {
//    if ({_*,sof:SubtypeOf(lt,rt,at)} := cb.constraints, 
//        isConcreteType(st,cb,lt), 
//        isConcreteType(st,cb,rt)) 
//    {
//        cb.constraints = cb.constraints - sof;
//        lt = instantiateAllInferenceVars(st,cb,lt);
//        rt = instantiateAllInferenceVars(st,cb,rt);
//        Constraint c = ConstrainType(typeForLoc(cb, at),makeFailType("Type <prettyPrintType(lt)> is not a subtype of <prettyPrintType(rt)>",at),at);
//        bool addedC = false;
//        if (!subtypeOf(lt,rt)) {
//            cb.constraints = cb.constraints + c;
//            addedC = true;
//        } 
//        return <cb, makeWhy("Subtype Of", { sof }, addedC ? { c } : { }, { sof }), true>;
//    }
//    
//    return <cb, emptyWhy("Subtype Of"), false>;
//}
//
////
//// CONSTRAINT: PWAResultType(RType left, RType right, loc at)
////
//// TODO: IMPLEMENT
////
//public CT unifyPWAResultType(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Pattern with Action Result Type"), false>;
//}
//
////
//// CONSTRAINT: IsReifiedType(RType outer, list[RType] params, loc at, RType result)
////
//// TODO: IMPLEMENT
////
//public CT unifyIsReifiedType(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Is Reified Type"), false>;
//}
//

////
//// CONSTRAINT: FieldOf(Tree t, RType inType, RType fieldType, loc at)
////
//public CT unifyFieldOf(STBuilder st, ConstraintBase cb) {
//    if ({_*,fof:FieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
//        cb.constraints = cb.constraints - fof;
//        inType = instantiateAllInferenceVars(st,cb,inType);
//        if (`<Name n>` := t) {
//            RType resType = getFieldType(inType,convertName(n),st,at);
//            if (isFailType(resType)) {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),resType,at);
//            } else {
//                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
//            }
//            return <cb, emptyWhy("Field Of"), true>;                                    
//        } else {
//            throw "Error, FieldOf tree component should be a name (location <at>)";
//        }    
//    }
//    
//    return <cb, emptyWhy("Field Of"), false>;
//}
//
////
//// CONSTRAINT: NamedFieldOf(Tree t, RType inType, RType fieldType, loc at)
////
//// Constrain the fieldType to be the result of projecting field t out of inType.
////
//public CT unifyNamedFieldOf(STBuilder st, ConstraintBase cb) {
//    if ({_*,fof:NamedFieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
//        cb.constraints = cb.constraints - fof;
//        inType = instantiateAllInferenceVars(st,cb,inType);
//        if ((Field)`<Name n>` := t) {
//            RType resType = getFieldType(inType,convertName(n),st,at);
//            if (!isFailType(resType)) {
//                resType = TypeWithName(RNamedType(resType,convertName(n)));
//                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
//            } else {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),resType,at);
//            }
//            return <cb, emptyWhy("Named Field Of"), true>;                                    
//        } else {
//            throw "Error, FieldOf tree component should be a name (location <at>)";
//        }    
//    }
//    
//    return <cb, emptyWhy("Named Field Of"), false>;
//}
//
////
//// CONSTRAINT: IndexedFieldOf(Tree t, RType inType, RType fieldType, loc at)
////
//// Constrain the fieldType to be the result of projecting field t out of inType.
////
//public CT unifyIndexedFieldOf(STBuilder st, ConstraintBase cb) {
//    if ({_*,iof:IndexedFieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
//        cb.constraints = cb.constraints - iof;
//        inType = instantiateAllInferenceVars(st,cb,inType);
//        if ((Field)`<IntegerLiteral il>` := t) {
//            int ilval = toInt("<il>"); 
//            RType resType = getFieldType(inType,ilval,st,at);            
//            if (!isFailType(resType)) {
//                if (typeHasFieldNames(inType))
//                    resType = TypeWithName(RNamedType(resType,getTypeFieldName(inType,ilval)));
//                else
//                    resType = TypeWithName(RUnnamedType(resType));
//                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
//            } else {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),resType,at);
//            }
//            return <cb, emptyWhy("Indexed Field Of"), true>;                                    
//        } else {
//            throw "Error, FieldOf tree component should be a name (location <at>)";
//        }    
//    }
//    
//    return <cb, emptyWhy("Indexed Field Of"), false>;
//}
//
////
//// CONSTRAINT: FieldProjection(RType inType, list[RType] fieldTypes, RType result, loc at)
////
//// Constrains the resulting type to be the result of projecting the fields out of a tuple,
//// map, or relation. Note that we need to maintain field names if possible. This is done by
//// a simple check below: we create a set of the field names, and if we are projecting the same
//// number of fields as are in the set then we know that we are not duplicating field names.
//// This also handles the case where we have no field names.
////
//public CT unifyFieldProjection(STBuilder st, ConstraintBase cb) {
//    if ({_*,fpr:FieldProjection(inType,fieldTypes,result,at)} := cb.constraints, isConcreteType(st,cb,inType), isConcreteType(st,cb,fieldTypes)) {
//        cb.constraints = cb.constraints - frp;
//
//        // NOTE: We expect all the fieldTypes to be of type TypeWithName
//        list[RNamedType] ftl = [ t | TypeWithName(t) <- fieldTypes];
//        list[RType] ftlnn = [ getElementType(t) | t <- ftl];
//        set[RName] fieldNames = { n | RNamedType(_,n) <- ftl };
//        
//        if (size(ftl) != size(fieldTypes)) throw "Error, fieldTypes contains invalid types: <fieldTypes>";
//        
//        if (isMapType(inType) || isRelType(inType)) {
//            if (size(ftl) == 1)
//                cb.constraints = cb.constraints + TypesAreEqual(result, makeSetType(getElementType(ftl[0])), at);
//            else {
//                if (size(ftl) == size(fieldNames))
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeRelTypeFromTuple(makeTupleTypeWithNames(ftl)), at);
//                else
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeRelType(ftlnn), at);
//            }
//            return <cb, emptyWhy("Field Projection"), true>;
//        }
//    
//        if (isTupleType(inType)) {
//            if (size(ftl) == 1)
//                cb.constraints = cb.constraints + TypesAreEqual(result, getElementType(ftl[0]), at);
//            else {
//                if (size(ftl) == size(fieldNames))
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeTupleTypeWithNames(ftl), at);
//                else
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeTupleType(ftlnn), at);
//            }
//            return <cb, emptyWhy("Field Projection"), true>;
//        } 
//        
//        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Cannot use field projection on type <prettyPrintType(inType)>", at),at);
//        return <cb, emptyWhy("Field Projection"), true>;
//    }
//    
//    return <cb, emptyWhy("Field Projection"), false>;
//}
//
////
//// CONSTRAINT: Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at)
////
//public CT unifySubscript(STBuilder st, ConstraintBase cb) {
//    if ({_*,s:Subscript(inType,indices,indexTypes,result,at)} := cb.constraints, isConcreteType(st,cb,inType), isConcreteType(st,cb,indexTypes)) {
//        cb.constraints = cb.constraints - s;
//        if (isTupleType(inType)) {
//            if (size(indexTypes) != 1) 
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on tuples must contain exactly one element", at),at);
//            else {
//                // A bit of "peephole analysis" -- if we can determine the index, use that info here, but if we
//                // cannot do so, just take the lub of all the tuple elements
//                if (`<IntegerLiteral il>` := indices[0]) {
//                    int ilval = toInt("<il>"); 
//                    cb.constraints = cb.constraints + TypesAreEqual(result, getTupleFieldType(inType, ilval), at);                 
//                } else {
//                    if (isIntType(indexTypes[0])) {
//                        cb.constraints = cb.constraints + TypesAreEqual(result, lubList(getTupleFields(inType)), at);
//                    } else {
//                        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexTypes[0])>",at),at);
//                    }
//                }                
//            }
//            return <cb, emptyWhy("Subscript"), true>;
//        } else if (isRelType(inType)) {
//            list[RType] relTypes = getRelFields(inType);
//            if (size(indexTypes) >= size (relTypes))
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on relations must have an arity less than that of the relation",at),at);
//            else {
//                bool canFormSub = true;
//                for (n <- [0..size(indices)-1]) {
//                    if ((Expression)`_` !:= indices[n]) {
//                        RType indexType = indexTypes[n];
//                        if (isSetType(indexType)) indexType = getSetElementType(indexType);
//                        if (!comparable(indexType,relTypes[n])) {
//                            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscript <n>, with type <prettyPrintType(indexType)>, is not comparable to relation type <n>, <prettyPrintType(relTypes[n])>",at),at);
//                            canFormSub = false;
//                        }
//                    }
//                }
//                if (canFormSub) {
//                    list[RType] subTypes = slice(relTypes,size(indexTypes),size(relTypes)-size(indexTypes));
//                    if (subTypes == 1)
//                        cb.constraints = cb.constraints + TypesAreEqual(result, makeSetType(subTypes[0]), at);
//                    else
//                        cb.constraints = cb.constraints + TypesAreEqual(result, makeRelType(subTypes), at);
//                }
//            }
//            return <cb, emptyWhy("Subscript"), true>;     
//        } else if (isMapType(inType)) {
//            if (size(indexTypes) != 1) 
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on maps must contain exactly one element", at),at);
//            else {
//                if (!comparable(getMapDomainType(inType),indexTypes[0])) {
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("The subscript type <prettyPrintType(indexTypes[0])> must be comparable to the map domain type <prettyPrintType(getMapDomainType(inType))>", at),at);
//                } else {
//                    cb.constraints = cb.constraints + TypesAreEqual(result, getMapRangeType(inType), at);
//                }                 
//            }
//            return <cb, emptyWhy("Subscript"), true>;
//        }  else if (isNodeType(inType)) {
//            if (size(indexTypes) != 1) 
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on nodes must contain exactly one element", at),at);
//            else {
//                if (isIntType(indexTypes[0])) {
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeValueType(), at);                 
//                } else {
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on nodes must be of type int, not type <prettyPrintType(indexTypes[0])>",at),at);
//                }
//            }
//            return <cb, emptyWhy("Subscript"), true>;
//        } else if (isListType(inType)) {
//            if (size(indexTypes) != 1) 
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on lists must contain exactly one element", at),at);
//            else {
//                if (isIntType(indexTypes[0])) {
//                    cb.constraints = cb.constraints + TypesAreEqual(result, getListElementType(inType), at);                 
//                } else {
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(indexTypes[0])>",at),at);
//                }
//            }
//            return <cb, emptyWhy("Subscript"), true>;
//        } else {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Subscript not supported on type <prettyPrintType(inType)>", at),at);
//            return <cb, emptyWhy("Subscript"), true>;
//        }
//    }
//    
//    return <cb, emptyWhy("Subscript"), false>;
//}
//
////
//// CONSTRAINT: Comparable(RType left, RType right, SolveResult sr, loc at)
////
//public CT unifyComparable(STBuilder st, ConstraintBase cb) {
//    if ({_*,cmp:Comparable(lt,rt,at)} := cb.constraints, isConcreteType(st,cb,lt), isConcreteType(st,cb,rt)) {
//        cb.constraints = cb.constraints - cmp;
//        lt = instantiateAllInferenceVars(st,cb,lt);
//        rt = instantiateAllInferenceVars(st,cb,rt);
//        if (!comparable(lt,rt))
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Types <prettyPrintType(lt)> and <prettyPrintType(rt)> are not comparable.",at),at);
//        return <cb, emptyWhy("Comparable"), true>;
//    }
//    
//    return <cb, emptyWhy("Comparable"), false>;
//}
//
////
//// CONSTRAINT: AnnotationAssignable(Tree parent, loc at, Tree lhs, Tree ann, Tree rhs, RType rvalue, RType lvalue, RType result)
////
//// TODO: Size check could fail in the case of aliases. We need to actually use an equivalence
//// check here as well.
////
//public CT unifyAnnotationAssignable(STBuilder st, ConstraintBase cb) {
//    if ({_*, aa:AnnotationAssignable(parent,at,lhs,ann,rhs,rvalue,lvalue,result) } := cb.constraints, `<Name n>` := ann, isConcreteType(st,cb,lvalue), isConcreteType(st,cb,rvalue)) {
//        cb.constraints = cb.constraints - aa;
//        
//        // First, get back the annotations with the given name n
//        RName rn = convertName(n);
//        set[Item] annItems = { st.scopeItemMap[annId] | annId <- getAnnotationItemsForName(st, st.currentModule, rn) };
//        
//        // Second, narrow down this list so that we only keep those where inType
//        // is a subtype of the type where the annotation is defined.
//        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subtypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
//        
//        // Third, pick one. If we have multiple available choices here, this is
//        // itself an error (unless all the choices have the same type). If we have
//        // no choices, this is also an error, since that means the annotation is not
//        // defined for this type. Note that the first error should be caught during
//        // symbol table construction, but we handle it here just in case.
//        if (size(annTypes) == 1) {
//            RType annType = getOneFrom(annTypes);
//            // Now, check to see that the rvalue type can be assigned into the annotation type.
//            if (subtypeOf(rvalue,annType)) {
//                cb.constraints = cb.constraints + TypesAreEqual(resultType,lvalue,at);
//            } else {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Can not assign value of type <prettyPrintType(rvalue)> to annotation of type <prettyPrintType(annType)>.",at),at);
//            }
//        } else if (size(annTypes) == 0) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Annotation <prettyPrintName(rn)> is not defined on type <prettyPrintType(inType)>",at),at);
//        } else {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Annotation <prettyPrintName(rn)> has multiple possible types on type <prettyPrintType(inType)>",at),at);
//        }
//        
//        return <cb, emptyWhy("Annotation Assignable"), true>;
//    }
//    
//    return <cb, emptyWhy("Annotation Assignable"), false>;
//}
//
////
//// CONSTRAINT: AnnotationOf(Tree t, RType inType, RType annType, loc at)
////
//// TODO: Size check could fail in the case of aliases. We need to actually use an equivalence
//// check here as well.
////
//public CT unifyAnnotationOf(STBuilder st, ConstraintBase cb) {
//    if ({_*, aof:AnnotationOf(t,inType,annType,at) } := cb.constraints, `<Name n>` := t, isConcreteType(st,cb,lvalue), isConcreteType(st,cb,rvalue)) {
//        cb.constraints = cb.constraints - aof;
//        
//        // First, get back the annotations with the given name n
//        RName rn = convertName(n);
//        set[Item] annItems = { st.scopeItemMap[annId] | annId <- getAnnotationItemsForName(st, st.currentModule, rn) };
//        
//        // Second, narrow down this list so that we only keep those where inType
//        // is a subtype of the type where the annotation is defined.
//        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subtypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
//        
//        // Third, pick one. If we have multiple available choices here, this is
//        // itself an error (unless all the choices have the same type). If we have
//        // no choices, this is also an error, since that means the annotation is not
//        // defined for this type. Note that the first error should be caught during
//        // symbol table construction, but we handle it here just in case.
//        if (size(annTypes) == 1) {
//            cb.constraints = cb.constraints + TypesAreEqual(annType,getOneFrom(annTypes),at);
//        } else if (size(annTypes) == 0) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Annotation <prettyPrintName(rn)> is not defined on type <prettyPrintType(inType)>",at),at);
//        } else {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("Annotation <prettyPrintName(rn)> has multiple possible types on type <prettyPrintType(inType)>",at),at);
//        }
//        
//        return <cb, emptyWhy("Annotation Of"), true>;
//    }
//    
//    return <cb, emptyWhy("Annotation Of"), false>;
//}
//
////
//// CONSTRAINT: Composable(RType left, RType right, RType result, loc at)
////
//// TODO: Add support for overloaded types and for overloaded type/function type combos
////
//public CT unifyComposable(STBuilder st, ConstraintBase cb) {
//    if ({_*,cmp:Composable(left, right, result, at)} := cb.constraints, isConcreteType(st,cb,left), isConcreteType(st,cb,right)) {
//        cb.constraints = cb.constraints - cmp;
//        
//        if (isOverloadedType(left) && isOverloadedType(right)) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Not yet supported (overlaoded function composition)!",at),at);
//        } else if (isOverloadedType(left) && isFunctionType(right)) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Not yet supported (overlaoded function composition)!",at),at);
//        } else if (isFunctionType(left) && isOverloadedType(right)) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Not yet supported (overlaoded function composition)!",at),at);
//        } else if (isFunType(left) && isFunType(right)) {
//            RType rightRet = getFunctionReturnType(right);
//            list[RType] leftParams = getFunctionArgumentTypes(left);
//            
//            if (size(leftParams) != 1) {
//                cb.constraints = cb.constarints + ConstrainType(typeForLoc(cb, at), makeFailType("Type <prettyPrintType(left)> must have exactly one formal parameter",at),at);            
//            } else {
//                RType leftParam = leftParams[0];
//                if (subtypeOf(rightRet,leftParam)) {
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeFunctionType(getFunctionReturnType(left),getFunctionArgumentTypes(right)), at);
//                } else {
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Cannot compose function types <prettyPrintType(left)> and <prettyPrintType(right)>",at),at);                
//                }            
//            }
//        } else if (isMapType(left) && isMapType(right)) {
//            RType j1 = getMapRangeType(left); 
//            RType j2 = getMapDomainType(right);
//            if (! subtypeOf(j1,j2)) 
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Incompatible types in map composition: domain <prettyPrintType(j1)> and range <prettyPrintType(j2)>", at),at);
//            else    
//                cb.constraints = cb.constraints + TypesAreEqual(result,RMapType(getMapDomainType(left), getMapRangeType(right)),at);
//        } else if (isRelType(left) && isRelType(right)) {
//            list[RNamedType] leftFields = getRelFieldsWithNames(left); 
//            list[RNamedType] rightFields = getRelFieldsWithNames(right);
//    
//            if (size(leftFields) != 2) {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Error in composition: <prettyPrintType(left)> should have arity 2", at),at);
//            }
//            
//            if (size(rightFields) != 2) {
//                cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Error in composition: <prettyPrintType(right)> should have arity 2", at),at);
//            }
//            
//            if (size(leftFields) == 2 && size(rightFields) == 2) {
//                // Check to make sure the fields are of the right type to compose
//                RType j1 = getElementType(leftFields[1]); 
//                RType j2 = getElementType(rightFields[0]);
//                if (! subtypeOf(j1,j2)) { 
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Incompatible types of fields in relation composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", at),at);
//                } else {
//                    // Check to see if we need to drop the field names, then return the proper type
//                    RNamedType r1 = leftFields[1]; 
//                    RNamedType r2 = rightFields[0];
//                    if (RNamedType(t1,n) := r1, RNamedType(t2,m) := r2, n != m)
//                        cb.constraints = cb.constraints + TypesAreEqual(result,RRelType([r1,r2]),at); 
//                    else 
//                        cb.constraints = cb.constraints + TypesAreEqual(result,RRelType([RUnnamedType(getElementType(r1)),RUnnamedType(getElementType(r2))]),at); 
//                }
//            }
//        } else {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Composition is not supported on types <prettyPrintType(left)> and <prettyPrintType(right)>", at),at);
//        }
//        
//        return <cb, emptyWhy("Composable"), true>;
//    }
//    return <cb, emptyWhy("Composable"), false>;
//}

////
//// CONSTRAINT: Bindable(Tree pattern, RType subject, loc at)
////
//// TODO: IMPLEMENT
////
//public CT unifyBindable(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Bindable"), false>;
//}
//
////
//// CONSTRAINT: Enumerable(Tree pattern, RType subject, loc at)
////
//// TODO: IMPLEMENT
////
//public CT unifyEnumerable(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Enumerable"), false>;
//}
//
////
//// CONSTRAINT: StepItType(Tree reducer, loc at, RType inType, RType outType, RType result)
////
//// TODO: IMPLEMENT
////
//public CT unifyStepItType(STBuilder st, ConstraintBase cb) {
//    return <cb, emptyWhy("Step It Type"), false>;
//}

//
// CONSTRAINT: DefinedBy(RType lvalue, set[ItemId] definingIds, loc at)
//
// The DefinedBy constraint provides the link between the constraint solver
// and the symbol table. Saying that t1 is defined by a set of IDs constrains
// t1 to be the type of one of the items that the IDs define. If there are multiple
// matching types, an overloaded type, with location information (if possible),
// is returned instead. This is then used for overloading for functions and
// constructors, and also needs to be taken account of during (for instance)
// function calls, since an overlaoded function can be passed to another function.
//
// TODO: Make sure the subtype rules are set up to properly handle
// overloaded types, the rule should be that ot1 <: t2 when at least one
// of the overloads in ot1 <: t2.
//
// TODO: Also need to make sure other functions handle overloaded types. Should
// just look at these again and make sure we handle them consistently. Note that
// there is no restriction here that these all be function or constructor types, 
// but in practice that is what will happen, since other cases are scope errors.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, DefinedBy(rt,itemIds,at)) {
    RType itemType = makeVoidType();
    
    if (size(itemIds) > 1) {
        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- itemIds });
    } else if (size(itemIds) == 1) {
        itemType = getTypeForItem(st, getOneFrom(itemIds));
    } else {
        itemType = makeFailType("No type has been defined for this item", at);
    }
    
    return DefinedBy(itemType,itemIds,at);
}

//
// CONSTRAINT: ConstrainType(RType constrainedType, RType typeConstraint, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, ConstrainType(ct,tc,at)) {
    return ConstrainType(tc, tc, at);
}

//
// CONSTRAINT: BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, BuiltInAppliable(op,d,r,at)) {
    RType result = r;
    if (isTupleType(d)) {
        result = getBuiltInResultType(op,d,r,at);
    }
    return BuiltInAppliable(op,d,result,at);
}

//
// CONSTRAINT: Assignable(RType rvalue, RType lvalue, RType result, SolveResult sr, loc at)
//
// TODO: Need to "lock" certain parts of the resulting type, since (for instance)
// if we have a name defined with a non-inferred type we cannot just assign and lub it.
// (NOTE: This is also commented as such in Assignable.rsc)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Assignable(rv,lv,res,sr,at)) {
    // For now, just state that all assignments are fine, yielding a value that is the lub
    // of the current type and the type being assigned. Later, tighten this up to take account
    // of when we are dealing with inferred types and when we are dealing with non-inferred types.
    return Assignable(rv,lv,lub(rv,lv),T(),at);
}

//
// CONSTRAINT: Assignable(RType rvalue, RType lvalue, SolveResult sr, loc at)
//
// TODO: Need to "lock" certain parts of the resulting type, since (for instance)
// if we have a name defined with a non-inferred type we cannot just assign and lub it.
// (NOTE: This is also commented as such in Assignable.rsc)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Assignable(rv,lv,sr,at)) {
    // For now, just state that all assignments are fine, yielding a value that is the lub
    // of the current type and the type being assigned. Later, tighten this up to take account
    // of when we are dealing with inferred types and when we are dealing with non-inferred types.
    return Assignable(rv,lv,T(),at);
}

//
// CONSTRAINT: LubOf(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOf(ttl,lr,at)) {
    return LubOf(ttl,lubList(ttl),at);
}

//
// CONSTRAINT: LubOfList(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub. This includes special 
// handling for list splicing, which occurs when we have a variable of list 
// type in a list, such as a = [1,2,3], l = [0,a,4], which is expected to 
// yield l = [0,1,2,3,4]. If we have a splicable element that is a list, 
// treat it instead as the list element type.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOfList(ttl,lr,at)) {
    lubs =
        for (rt <- ttl) {
            if (SpliceableElement(sty) := rt) {
                if (isListType(sty)) {
                    append(getListElementType(sty));
                } else {
                    append(sty);
                } 
            } else { 
                append(rt);
            } 
        };
    return LubOfList(ttl,lubList(lubs),at);
}

//
// CONSTRAINT: LubOfSet(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub. This includes special 
// handling for set splicing, which occurs when we have a variable of set 
// type in a set, such as a = {1,2,3}, s = {0,a,4}, which is expected to 
// yield s = {0,1,2,3,4}. If we have a splicable element that is a set, 
// treat it instead as the set element type.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOfSet(ttl,lr,at)) {
    lubs =
        for (rt <- ttl) {
            if (SpliceableElement(sty) := rt) {
                if (isSetType(sty)) {
                    append(getSetElementType(sty));
                } else {
                    append(sty);
                } 
            } else { 
                append(rt);
            } 
        };
    return LubOfSet(ttl,lubList(lubs),at);
}

//
// CONSTRAINT: IsRuntimeException(RType expType, SolveResult sr, loc at)
//
// TODO: This allows anything to be a runtime exception -- we always just say
// the check succeeded. If we want to really require use of a runtime exception
// constructed value, we should modify this.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, IsRuntimeException(et,sr,at)) {
    return IsRuntimeException(et,T(),at);
}

//
// CONSTRAINT: Returnable(RType given, RType expected, SolveResult sr, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Returnable(gt,et,sr,at)) {
    if (subtypeOf(gt,et))
        return Returnable(gt,et,T(),at);
    return Returnable(gt,et,F(),at);
}

//
// CONSTRAINT: CallOrTree(RType source, list[RType] params, RType result, loc at)
//
// TODO: Is there anything in particular we need to do for type variables here? The
// call will instantiate them, so we need to use them to determine the return type,
// but beyond that it doesn't seem like we need to do anything with them.
//
// TODO: Make sure type variables are distinct. This should be handled by the scope
// rules, but verify that (for instance) two &T vars won't get tangled.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, CallOrTree(st,pts,rt,at)) {
    set[RType] failures = { }; set[RType] matches = [ ];
    return CallOrTree(st, pts, rt, at);
}
    
//    if ({_*,cot:CallOrTree(source,params,result,at)} := cb.constraints, isConcreteType(st,cb,source), isConcreteType(st,cb,params)) {
//
//        set[RType] failures = { }; list[RType] matches = [ ];
//
//        // Initialize the set of alternatives. Then, check each one, trying to find a match.
//        set[ROverloadedType] alternatives = isOverloadedType(source) ? getOverloadOptions(source) : { ( (source@at)? ) ? ROverloadedTypeWithLoc(source,source@at) :  ROverloadedType(source) };
//        for (a <- alternatives) {
//            bool typeHasLoc = ROverloadedTypeWithLoc(_,_) := a;
//            RType fcType = typeHasLoc ? a.overloadType[@at=a.overloadLoc] : a.overloadType;
//    
//            if (isFunctionType(fcType)) {
//                list[RType] formalTypes = getFunctionArgumentTypes(fcType);
//                list[RType] actualTypes = params;
//    
//                if ( (isVarArgsFun(fcType) && size(formalTypes) <= size(actualTypes)) || (!isVarArgsFun(fcType) && size(formalTypes) == size(actualTypes)) ) {
//                    bool matched = true;
//                    if (size(actualTypes) > 0) {
//                        for (n <- [0..size(actualTypes)-1] && matched) {
//                            RType formalType = (n < size(formalTypes)) ? formalTypes[n] : formalTypes[size(formalTypes)-1];
//                            RType actualType = actualTypes[n];
//                            if (! subtypeOf(actualType, formalType)) {
//                                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",at);
//                                matched = false;
//                            }                  
//                        }
//                    }
//    
//                    // If we have matched so far, try to find mappings for any type variables
//                    if (matched && typeContainsTypeVars(fcType)) {
//                        // First, create a tuple type holding the actual function call parameters. We can use this
//                        // to get the mappings. Note that we don't use a function here, since we are trying to find
//                        // the bindings, and the return type (if it is parameterized) will be based on this mapping,
//                        // but cannot contribute to it.
//                        RType funcType = makeVoidType();
//                        if (isVarArgsFun(fcType)) {
//                            // Calculate the var args type based on the lub's of the elements passed in
//                            RType vat = makeVarArgsType(lubList(tail(actualTypes,size(actualTypes)-size(formalTypes)+1)));
//                            // Create the function type using the formal return type and the actuals + the varargs
//                            funcType = makeTupleType(head(actualTypes,size(formalTypes)-1) + vat);
//                        } else {
//                            funcType = makeTupleType(actualTypes);
//                        }
//    
//                        // Now, find the bindings
//                        // TODO: Need to find embedded failures in the map
//                        map[RName varName, RType varType] varMapping = getTVBindings(makeTupleType(getFunctionArgumentTypes(fcType)), funcType, ( ), at);
//                        fcType = instantiateVars(varMapping, fcType);
//                    }
//    
//                    if (matched) matches = matches + fcType;
//                } else {
//                    str orMore = isVarArgsFun(fcType) ? "or more " : "";
//                    failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: function accepts <size(formalTypes)> <orMore>arguments but was given <size(actuals)>", at);
//                }
//            } else if (isConstructorType(fcType)) {
//                list[RType] formalTypes = getConstructorArgumentTypes(fcType);
//                list[RType] actualTypes = params;
//                if (size(formalTypes) == size(actuals)) {
//                    bool matched = true;
//                    for (n <- domain(actuals) && matched) {
//                        RType formalType = formalTypes[n];
//                        RType actualType = actualTypes[n];
//                        if (! subtypeOf(actualType, formalType)) {
//                            failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",at);
//                            matched = false;
//                        }
//                    }
//                    // If we have matched so far, try to find mappings for any type variables
//                    if (matched && typeContainsTypeVars(fcType)) {
//                        // First, we "mock up" the actual constructor type, based on the types of the actual parameters
//                        RType consType = makeTupleType([RUnnamedType(acty) | acty <- actualTypes]);
//                        // Now, find the bindings
//                        map[RName varName, RType varType] varMapping = getTVBindings(getConstructorArgumentTypes(fcType), consType, ( ), at);
//                        fcType = instantiateVars(varMapping, fcType);
//                    }
//                    if (matched) matches = matches + fcType;
//                } else {
//                    failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: constructor accepts <size(formalTypes)> arguments but was given <size(actuals)>", at);
//                }
//            } else if (isStrType(fcType)) {
//                // node, need to verify that the string is a valid identifier
//                // TODO: Add identifier checking
//                // TODO: I don't believe we need to do anything with the params, since they
//                // are all of type value, but check to make sure. Not sure how this could
//                // fail typing then as long as the identifier given is a proper identifier
//                // name.
//                matches = matches + makeNodeType();
//            } else if (isLocType(fcType)) {
//                // parameters should be (int, int, tuple[int,int], tuple[int,int])
//                list[RType] actualTypes = params;
//                if (! (size(actualTypes) == 4 && isIntType(actualTypes[0]) && isIntType(actualTypes[1]) && isTupleType(actualTypes[2]) && size(getTupleFields(actualTypes[2])) == 2 && isIntType(getTupleFields(actualTypes[2])[0]) && isIntType(getTupleFields(actualTypes[2])[1]) && isTupleType(actualTypes[3])) && isIntType(getTupleFields(actualTypes[3])[0]) && isIntType(getTupleFields(actualTypes[3])[1]) ) {
//                    failures = failures + makeFailType("The location offset information must be given as (int,int,\<int,int\>,\<int,int\>)",at);
//                } else {
//                    matches = matches + makeLocType();
//                }
//            } else {
//                failures = failures + makeFailType("Type <prettyPrintType(fcType)> is not a function or constructor type.",at);
//            }
//        }
//        
//        cb.constraints = cb.constraints - cot;
//        if (size(matches) == 1) {
//            cb.constraints = cb.constraints + TypesAreEqual(result,getOneFrom(matches),at);
//        } else if (size(matches) > 1) {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),makeFailType("There are multiple possible matches for this function or constructor expression. Please add additional type information. Matches: <prettyPrintTypeListWLoc(matches)>",at),at);
//        } else {
//            cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at),collapseFailTypes(failures),at);        
//        }
//        
//    }
//    
//    return <cb, emptyWhy("Call or Tree"), false>;
//}
//

//
// Default version of the solve function -- this does nothing beyond return the current
// constraint, making no changes.
//
public Constraint default solveConstraint(STBuilder st, ConstraintBase cb, Constraint c) = c;

//
// Driver function to reduce as far as we can -- this starts with the top of the graph,
// meaning the nodes that appear only on the left of a tuple, and iteratively
// tries to solve them.
//
// TODO: Implement history
//
// TODO: Implement sub-graphs -- this would help by allowing us to break off
// solved connected components, making the remaining problem smaller
//
//public ConstraintGraph updateGraph(ConstraintGraph cg, Constraint old, Constraint new) {
//    ConstraintGraph oldl = { < CN(old), cr > | < CN(old), cr > <- cg };
//    ConstraintGraph oldr = { < cl, CN(old) > | < cl, CN(old) > <- cg };
//    ConstraintGraph newl = { < CN(new), cr > | cr <- oldl<1> };
//    ConstraintGraph newr = { < cl, CN(new) > | cl <- oldr<0> };
//    
//    return cg - oldl - oldr + newl + newr;
//}

public tuple[ConstraintBase,ConstraintGraph,bool] solveConstraints(STBuilder st, ConstraintBase cb) {
    ConstraintGraph updateGraph(ConstraintGraph cg, Constraint old, Constraint new) {
        ConstraintGraph oldl = { < CN(old), cr > | < CN(old), cr > <- cg };
        ConstraintGraph oldr = { < cl, CN(old) > | < cl, CN(old) > <- cg };
        ConstraintGraph newl = { < CN(new), cr > | cr <- oldl<1> };
        ConstraintGraph newr = { < cl, CN(new) > | cl <- oldr<0> };
        
        return cg - oldl - oldr + newl + newr;
    }
    
    // First, using the current constraint base, generate a graph representation
    // of the dependencies inherent in the constraints.
    ConstraintGraph cg = generateConstraintGraph(cb);
    set[ConstraintNode] frontier = { cn | cn:CN(c) <- top(cg), solvable(c) };
    
    // If we don't have a frontier, this means that all the nodes depend on
    // some other node. If this is the case, it means that we cannot
    // solve the constraints.
    if (size(frontier) == 0) {
        println("ERROR: Irreducible constraint system.");
        return < cb, cg, false >;
    }

    // Using the frontier, start solving the constraints in the graph, pushing
    // forward using a "frontier" of unsolved nodes. This frontier represents
    // the propagation of information discovered during the solving process.
    bool keepGoing = true;
    rel[Constraint,Constraint] history = { };
    while (keepGoing) {
        // The next frontier includes those nodes that we will process in the
        // next pass, based on having solved at least one of their dependencies
        // in this pass.
        set[ConstraintNode] nextFrontier = { };
        
        // Assume we stop -- we only continue if we solve at least one constraint
        keepGoing = false;
        
        // Attempt to solve each node in the frontier. For those nodes that we
        // change, find the mappings for the inferred types and propagate those
        // to the nodes depending on those types.
        for (ConstraintNode cn <- frontier) {
            if (CN(c) := cn) {
                println("SOLVING: <c>");
                
                Constraint cp = solveConstraint(st, cb, c);
                if (c != cp) {
                    keepGoing = true;

                    // Put the modified constraint back into the constraint graph
                    cg = updateGraph(cg, c, cp);
                    println("SOLVED: <c> --\> <cp>");
                    
                    // Save the modification as part of the history for this constraint
                    history += < c, cp >;
                    
                    // Find the information to update any bindings of inference vars
                    < bindings, res > = mappings(c, cp);
                    
                    // If we could derive the mappings, use these to update the constraints
                    // that depend on these types, and put the modified constraints into
                    // the next frontier and back into the graph.
                    if (res) {
                        // Get all the constraints that we should update with the new type mapping
                        set[Constraint] toUpdate = { cu |  < tf, tt > <- bindings, tf != tt, < TN(tf), CN(cu) > <- cg };
                        
                        // Update each of these constraints with the new type information
                        rel[Constraint,Constraint] updated = { < cu, instantiate(cu, bindings) > | cu <- toUpdate };
                        for ( <c1,c2> <- updated ) println("PROPAGATED: <c1> --\> <c2>");
                        
                        // Add these updated constraints to the next frontier, if they are now solvable (else
                        // we cannot reduce them anyway)
                        nextFrontier += { CN(cu) | cu <- updated<1>, solvable(cu) };
                        
                        // And, put the changes back into the graph
                        for (< cu, cv > <- updated) cg = updateGraph(cg, cu, cv);
                    }            
                }            
            }
        }
        
        frontier = nextFrontier;    
    }
    
    return < cb, cg, true >;
        
}
