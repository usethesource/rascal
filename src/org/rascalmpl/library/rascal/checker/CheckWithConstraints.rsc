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
import DateTime;

import rascal::checker::ListUtils;
import rascal::types::Types;
import rascal::types::SubTypes;
import rascal::scoping::SymbolTable;
import rascal::types::TypeSignatures;
import rascal::scoping::ResolveNames;
import rascal::checker::TreeUtils;

import constraints::Constraint;

import rascal::checker::constraints::Constraints;
import rascal::checker::constraints::Statement;
import rascal::checker::constraints::Expression;
import rascal::checker::constraints::Variable;
import rascal::checker::constraints::BuiltIns;
import rascal::checker::constraints::Fields;
import rascal::checker::constraints::PatternWithAction;
import rascal::checker::constraints::Visit;
import rascal::checker::constraints::Case;
import rascal::checker::constraints::StringTemplate;
import rascal::checker::constraints::Catch;

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
                            
        // View
        case (Toplevel) `<Tags tgs> <Visibility v> view <Name n> <: <Name sn> = <{Alternative "|"}+ alts> ;` : {
            return "View (location <t@\loc>)";
        }
    }
    throw "checkToplevel: Unhandled toplevel item <t>";
}

public ConstraintBase gatherConstraints(SymbolTable st, Tree t) {
    ConstraintBase cb = makeNewConstraintBase();
    map[loc,Constraints] constraintsByLoc = ( );
    
    visit(t) {
        case `<Statement stmt>` : cb = gatherStatementConstraints(st,cb,stmt);
        
        case `<Expression exp>` : cb = gatherExpressionConstraints(st,cb,exp);
        
        case `<Variable v>` : cb = gatherVariableConstraints(st,cb,v);
        
        case `<PatternWithAction pwa>` : cb = gatherPatternWithActionConstraints(st,cb,pwa);
        
        case `<Visit v>` : cb = gatherVisitConstraints(st,cb,v);
        
        case `<Case c>` : cb = gatherCaseConstraints(st,cb,c);
        
        case `<StringTemplate s>` : cb = gatherStringTemplateConstraints(st,cb,s);
        
        case `<Catch c>` : cb = gatherCatchConstraints(st,cb,c);
        
        case `<Toplevel t>` : {
            if (size(cb.constraints) > 0) {
                println("Encountered top level item <getTopLevelItemDesc(t)>, partially reducing constraints, current set size <size(cb.constraints)>");
                ConstraintBase cbp = multiStepReduction(st,cb);
                println("Finished reducing constraints, new constraint set has size <size(cbp.constraints)>");
                constraintsByLoc[t@\loc] = cbp.constraints;
                cbp.constraints = { };
                cb = cbp;
            }
        }  
    }
    
    // Now, take out all the sets from the constraint set map and put them into the resulting constraint base
    Constraints res = { };
    for (l <- domain(constraintsByLoc)) res += constraintsByLoc[l];
    cb.constraints = res;
    
    return cb;
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

public tuple[ConstraintBase,bool] oneStepReduce(SymbolTable st, ConstraintBase cb) {

    <cbp, b> = unifyDefinedBy(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyTypesAreEqual(st,cb); if (b) return <cbp,b>;
    
    <cbp, b> = unifyTreeIsType(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyTreeIsTypeWithFailures(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyLocIsType(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyLocIsTypeWithFailures(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyBuiltInAppliable(st,cb); if (b) return <cbp,b>;
    

    <cbp, b> = unifyIsRuntimeException(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyLubOf(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyLubOfSet(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyLubOfList(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifySubtypeOf(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyComparable(st,cb); if (b) return <cbp, b>;
        

    <cbp, b> = unifyReturnable(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyAssignable(st,cb); if (b) return <cbp, b>;
    

    <cbp, b> = unifyCallOrTree(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyFieldOf(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyNamedFieldOf(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyIndexedFieldOf(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyFieldProjection(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifySubtypeOf(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyAnnotationOf(st, cb); if (b) return <cbp, b>;

    <cbp, b> = unifyAnnotationAssignable(st, cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyComposable(st, cb); if (b) return <cbp, b>;
    
    return <cb, false>;
}

//
// START OF CONSTRAINT SOLVING ROUTINES
//

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
// are fail types, then T1 : t3, there t3 is the merged failure information
// from t1 and t2.
//
public tuple[ConstraintBase,bool] unifyTreeIsType(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:TreeIsType(_,l1,rt1),ti2:TreeIsType(_,l1,rt2)} := cb.constraints, !isFailType(rt1), !isFailType(rt2), <cbp,b> := unifyTypes(cb,rt1,rt2,l1), b) {
        cbp.constraints = cbp.constraints - ti1;
        return <cbp,true>;
    } 

    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyTreeIsTypeWithFailures(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:TreeIsType(_,l1,rt1),ti2:TreeIsType(_,l1,rt2)} := cb.constraints, isFailType(rt1) || isFailType(rt2)) {
        if (isFailType(rt1) && isFailType(rt2)) {
            cb.constraints = cb.constraints - ti1 - ti2 + TreeIsType(t1,l1,collapseFailTypes({rt1,rt2}));
        } else if (isFailType(rt1)) {
            cb.constraints = cb.constraints - ti2;
        } else if (isFailType(rt2)) {
            cb.constraints = cb.constraints - ti1;
        } else {
            throw "Something odd is going on, at least one of the types must be a fail type to get here!";
        }
        
        return <cb,true>;
    } 

    return <cb, false>;
}

//
// CONSTRAINT: LocIsType(loc at, RType treeType)
//
// LocIsType constraints are like TreeIsType constraints but do not include
// an actual tree.
//
// TODO: It may make sense to remove the TreeIsType constraints, if we do not
// ever use the trees.
//
public tuple[ConstraintBase,bool] unifyLocIsType(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:LocIsType(l1,rt1),ti2:LocIsType(l1,rt2)} := cb.constraints, !isFailType(rt1), !isFailType(rt2), <cbp,b> := unifyTypes(cb,rt1,rt2,l1), b) {
        cbp.constraints = cbp.constraints - ti1;
        return <cbp,true>;
    } 

    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyLocIsTypeWithFailures(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:LocIsType(l1,rt1),ti2:LocIsType(l1,rt2)} := cb.constraints, isFailType(rt1) || isFailType(rt2)) {
        if (isFailType(rt1) && isFailType(rt2)) {
            cb.constraints = cb.constraints - ti1 - ti2 + LocIsType(l1,collapseFailTypes({rt1,rt2}));
        } else if (isFailType(rt1)) {
            cb.constraints = cb.constraints - ti2;
        } else if (isFailType(rt2)) {
            cb.constraints = cb.constraints - ti1;
        } else {
            throw "Something odd is going on, at least one of the types must be a fail type to get here!";
        }
        
        return <cb,true>;
    } 

    return <cb, false>;
}

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
public tuple[ConstraintBase,bool] unifyTypesAreEqual(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:TypesAreEqual(rt1:InferenceVar(n),rt2,at)} := cb.constraints, isConcreteType(st,cb,rt2)) {
        if (n in cb.inferenceVarMap) {
            if (!isFailType(cb.inferenceVarMap[n])) {
                if (!equivalent(rt2,cb.inferenceVarMap[n])) {
                    cb.inferenceVarMap[n] = makeFailType("Attempt to constrain type to <prettyPrintType(cb.inferenceVarMap[n])> and <prettyPrintType(rt2)>",at);
                }
            }
        } else {
            cb.inferenceVarMap[n] = instantiateAllInferenceVars(st,cb,rt2);
        }
        cb.constraints = cb.constraints - ti1;
        return <cb,true>;
    } else if ({_*,ti1:TypesAreEqual(rt1,rt2:InferenceVar(n),at)} := cb.constraints, isConcreteType(st,cb,rt1)) {
        if (n in cb.inferenceVarMap) {
            if (!isFailType(cb.inferenceVarMap[n])) {
                if (!equivalent(rt1,cb.inferenceVarMap[n])) {
                    cb.inferenceVarMap[n] = makeFailType("Attempt to constrain type to <prettyPrintType(cb.inferenceVarMap[n])> and <prettyPrintType(rt1)>",at);
                }
            }
        } else {
            cb.inferenceVarMap[n] = instantiateAllInferenceVars(st,cb,rt1);
        }
        cb.constraints = cb.constraints - ti1;
        return <cb,true>;
    } else if ({_*,til:TypesAreEqual(rt1,rt2,at)} := cb.constraints, <cbp,b> := unifyTypes(cb,rt1,rt2,at), b) {
        cbp.constraints = cbp.constraints - til;
        return <cbp,true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: LubOf(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub. Only fires when all the types
// in typesToLub are concrete types.
//
public tuple[ConstraintBase,bool] unifyLubOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,los:LubOf(lubs,r,at)} := cb.constraints, isConcreteType(st,cb,lubs), isInferenceType(st,cb,r)) {
        lubs2 =
            for (rt <- lubs) { 
                append(instantiateAllInferenceVars(st,cb,rt));
            };
        cb.constraints = cb.constraints + TypesAreEqual(r,lubList(lubs2),at) - los;
        return <cb,true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: LubOfList(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub. Only fires when all the types
// in typesToLub are concrete types. This includes special handling for list splicing,
// which occurs when we have a variable of list type in a list, such as a = [1,2,3],
// l = [0,a,4], which is expected to yield l = [0,1,2,3,4]. If we have a splicable
// element that is a list, treat it instead as the list element type.
//
public tuple[ConstraintBase,bool] unifyLubOfList(SymbolTable st, ConstraintBase cb) {
    if ({_*,los:LubOfList(lubs,r,at)} := cb.constraints, isConcreteType(st,cb,lubs), isInferenceType(st,cb,r)) {
        lubs2 =
            for (rt <- lubs) {
               rt = instantiateAllInferenceVars(st,cb,rt);
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
        cb.constraints = cb.constraints + TypesAreEqual(r,lubList(lubs2),at) - los;
        return <cb,true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: LubOfSet(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub. Only fires when all the types
// in typesToLub are concrete types. This includes special handling for set splicing,
// which occurs when we have a variable of set type in a set, such as a = {1,2,3},
// s = {0,a,4}, which is expected to yield s = {0,1,2,3,4}. If we have a splicable
// element that is a set, treat it instead as the set element type.
//
public tuple[ConstraintBase,bool] unifyLubOfSet(SymbolTable st, ConstraintBase cb) {
    if ({c*,los:LubOfSet(lubs,r,at)} := cb.constraints, isConcreteType(st,cb,lubs), isInferenceType(st,cb,r)) {
        lubs2 =
            for (rt <- lubs) { 
                rt = instantiateAllInferenceVars(st,cb,rt);
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
        cb.constraints = cb.constraints + TypesAreEqual(r,lubList(lubs2),at) - los;
        return <cb,true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: BindableToCase(RType subject, RType caseType, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyBindableToCase(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: Assignable(Tree parent, loc at, Tree lhs, Tree rhs, RType rvalue, RType lvalue, RType result)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyAssignable(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: Assignable(Tree parent, loc at, Tree lhs, Tree rhs, RType rvalue, RType lvalue)
//
// TODO: This code is not yet complete.
//
public tuple[ConstraintBase,bool] unifyAssignableWithValue(SymbolTable st, ConstraintBase cb) {
    if ({_*,abl:Assignable(tp,at,lhs,rhs,rv,lv)} := cb.constraints, isConcreteType(st,cb,lv), isConcreteType(st,cb,rv)) {
        cb.constraints = cb.constraints - abl;
        
        while (InferenceVar(n) := lv) lv = cb.inferenceVarMap[n];
        while (InferenceVar(n) := rv) rv = cb.inferenceVarMap[n];
        
        if (!subtypeOf(rv,lv))
            cb.constraints = cb.constraints + TreeIsType(tp,tp@\loc,makeFailType("Cannot assign <rhs>, of type <prettyPrintType(rv)>, to <lhs>, which expects type <prettyPrintType(lv)>",tp@\loc));
        return <cb, true>;
    }
    
    return < cb, false>;
}
 
//
// CONSTRAINT: IsNameAssignable(RType assignableType, RType resultType, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyIsNameAssignable(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: Returnable(Tree parent, loc at, Tree returned, RType given, RType expected)
//
public tuple[ConstraintBase,bool] unifyReturnable(SymbolTable st, ConstraintBase cb) {
    if ({_*,rtb:Returnable(tp,at,r,gt,et)} := cb.constraints, isConcreteType(st,cb,gt), isConcreteType(st,cb,et)) {
        cb.constraints = cb.constraints - rtb;
        gt = instantiateAllInferenceVars(st,cb,gt);
        et = instantiateAllInferenceVars(st,cb,et);
        if (!subtypeOf(gt,et))
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Cannot return value of type <prettyPrintType(gt)>, type <prettyPrintType(et)> was expected",at));
        return <cb, true>;
    }
    
    return <cb, false>;
}


//
// CONSTRAINT: IsRuntimeException(RType expType, loc at)
//
public tuple[ConstraintBase,bool] unifyIsRuntimeException(SymbolTable st, ConstraintBase cb) {
    if ({_*,ire:IsRuntimeException(expType,at)} := cb.constraints, isConcreteType(st,cb,expType)) {
        cb.constraints = cb.constraints - ire;
        
        if (! (isADTType(expType) && getADTName(expType) in { RSimpleName("RuntimeException"),RCompoundName(["Exception","RuntimeException"]) })) {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("The type of the thrown expression must be RuntimeException, not <prettyPrintType(expType)>",at));
        }
        
        return <cb, true>;
    }
    return <cb, false>;
}

//
// CONSTRAINT: BindsRuntimeException(Tree pat, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyBindsRuntimeException(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: CaseIsReachable(RType caseType, RType expType, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyCaseIsReachable(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: SubtypeOf(RType left, RType right, loc at)
//
public tuple[ConstraintBase,bool] unifySubtypeOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,sof:SubtypeOf(lt,rt,at)} := cb.constraints, isConcreteType(st,cb,lt), isConcreteType(st,cb,rt)) {
        cb.constraints = cb.constraints - sof;
        lt = instantiateAllInferenceVars(st,cb,lt);
        rt = instantiateAllInferenceVars(st,cb,rt);
        if (!subtypeOf(lt,rt))
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Type <prettyPrintType(lt)> is not a subtype of <prettyPrintType(rt)>",at));
        return <cb, true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: PWAResultType(RType left, RType right, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyPWAResultType(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: IsReifiedType(RType outer, list[RType] params, loc at, RType result)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyIsReifiedType(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: CallOrTree(RType source, list[RType] params, RType result, loc at)
//
//
// TODO: Is there anything in particular we need to do for type variables here? The
// call will instantiate them, so we need to use them to determine the return type,
// but beyond that it doesn't seem like we need to do anything with them.
//
// TODO: Make sure type variables are distinct. This should be handled by the scope
// rules, but verify that (for instance) two &T vars won't get tangled.
//
public tuple[ConstraintBase,bool] unifyCallOrTree(SymbolTable st, ConstraintBase cb) {
    if ({_*,cot:CallOrTree(source,params,result,at)} := cb.constraints, isConcreteType(st,cb,source), isConcreteType(st,cb,params)) {

        set[RType] failures = { }; list[RType] matches = [ ];

        // Initialize the set of alternatives. Then, check each one, trying to find a match.
        set[ROverloadedType] alternatives = isOverloadedType(source) ? getOverloadOptions(source) : { ( (source@at)? ) ? ROverloadedTypeWithLoc(source,source@at) :  ROverloadedType(source) };
        for (a <- alternatives) {
            bool typeHasLoc = ROverloadedTypeWithLoc(_,_) := a;
            RType fcType = typeHasLoc ? a.overloadType[@at=a.overloadLoc] : a.overloadType;
    
            if (isFunctionType(fcType)) {
                list[RType] formalTypes = getFunctionArgumentTypes(fcType);
                list[RType] actualTypes = params;
    
                if ( (isVarArgsFun(fcType) && size(formalTypes) <= size(actualTypes)) || (!isVarArgsFun(fcType) && size(formalTypes) == size(actualTypes)) ) {
                    bool matched = true;
                    if (size(actualTypes) > 0) {
                        for (n <- [0..size(actualTypes)-1] && matched) {
                            RType formalType = (n < size(formalTypes)) ? formalTypes[n] : formalTypes[size(formalTypes)-1];
                            RType actualType = actualTypes[n];
                            if (! subtypeOf(actualType, formalType)) {
                                failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",at);
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
                        // TODO: Need to find embedded failures in the map
                        map[RName varName, RType varType] varMapping = getTVBindings(makeTupleType(getFunctionArgumentTypes(fcType)), funcType, ( ), at);
                        fcType = instantiateVars(varMapping, fcType);
                    }
    
                    if (matched) matches = matches + fcType;
                } else {
                    str orMore = isVarArgsFun(fcType) ? "or more " : "";
                    failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: function accepts <size(formalTypes)> <orMore>arguments but was given <size(actuals)>", at);
                }
            } else if (isConstructorType(fcType)) {
                list[RType] formalTypes = getConstructorArgumentTypes(fcType);
                list[RType] actualTypes = params;
                if (size(formalTypes) == size(actuals)) {
                    bool matched = true;
                    for (n <- domain(actuals) && matched) {
                        RType formalType = formalTypes[n];
                        RType actualType = actualTypes[n];
                        if (! subtypeOf(actualType, formalType)) {
                            failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: type of argument <n>(<actuals[n]>), <prettyPrintType(actualType)>, must be a subtype of <prettyPrintType(formalType)>",at);
                            matched = false;
                        }
                    }
                    // If we have matched so far, try to find mappings for any type variables
                    if (matched && typeContainsTypeVars(fcType)) {
                        // First, we "mock up" the actual constructor type, based on the types of the actual parameters
                        RType consType = makeTupleType([RUnnamedType(acty) | acty <- actualTypes]);
                        // Now, find the bindings
                        map[RName varName, RType varType] varMapping = getTVBindings(getConstructorArgumentTypes(fcType), consType, ( ), at);
                        fcType = instantiateVars(varMapping, fcType);
                    }
                    if (matched) matches = matches + fcType;
                } else {
                    failures = failures + makeFailType("Could not use alternative <prettyPrintType(fcType)><typeHasLoc ? " defined at <fcType@at>" : "">: constructor accepts <size(formalTypes)> arguments but was given <size(actuals)>", at);
                }
            } else if (isStrType(fcType)) {
                // node, need to verify that the string is a valid identifier
                // TODO: Add identifier checking
                // TODO: I don't believe we need to do anything with the params, since they
                // are all of type value, but check to make sure. Not sure how this could
                // fail typing then as long as the identifier given is a proper identifier
                // name.
                matches = matches + makeNodeType();
            } else if (isLocType(fcType)) {
                // parameters should be (int, int, tuple[int,int], tuple[int,int])
                list[RType] actualTypes = params;
                if (! (size(actualTypes) == 4 && isIntType(actualTypes[0]) && isIntType(actualTypes[1]) && isTupleType(actualTypes[2]) && size(getTupleFields(actualTypes[2])) == 2 && isIntType(getTupleFields(actualTypes[2])[0]) && isIntType(getTupleFields(actualTypes[2])[1]) && isTupleType(actualTypes[3])) && isIntType(getTupleFields(actualTypes[3])[0]) && isIntType(getTupleFields(actualTypes[3])[1]) ) {
                    failures = failures + makeFailType("The location offset information must be given as (int,int,\<int,int\>,\<int,int\>)",at);
                } else {
                    matches = matches + makeLocType();
                }
            } else {
                failures = failures + makeFailType("Type <prettyPrintType(fcType)> is not a function or constructor type.",at);
            }
        }
        
        cb.constraints = cb.constraints - cot;
        if (size(matches) == 1) {
            cb.constraints = cb.constraints + TypesAreEqual(result,getOneFrom(matches),at);
        } else if (size(matches) > 1) {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("There are multiple possible matches for this function or constructor expression. Please add additional type information. Matches: <prettyPrintTypeListWLoc(matches)>",at));
        } else {
            cb.constraints = cb.constraints + LocIsType(at,collapseFailTypes(failures));        
        }
        
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: FieldOf(Tree t, RType inType, RType fieldType, loc at)
//
public tuple[ConstraintBase,bool] unifyFieldOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,fof:FieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
        cb.constraints = cb.constraints - fof;
        inType = instantiateAllInferenceVars(st,cb,inType);
        if (`<Name n>` := t) {
            RType resType = getFieldType(inType,convertName(n),st,at);
            if (isFailType(resType)) {
                cb.constraints = cb.constraints + LocIsType(at,resType);
            } else {
                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
            }
            return <cb, true>;                                    
        } else {
            throw "Error, FieldOf tree component should be a name (location <at>)";
        }    
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: NamedFieldOf(Tree t, RType inType, RType fieldType, loc at)
//
// Constrain the fieldType to be the result of projecting field t out of inType.
//
public tuple[ConstraintBase,bool] unifyNamedFieldOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,fof:NamedFieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
        cb.constraints = cb.constraints - fof;
        inType = instantiateAllInferenceVars(st,cb,inType);
        if ((Field)`<Name n>` := t) {
            RType resType = getFieldType(inType,convertName(n),st,at);
            if (!isFailType(resType)) {
                resType = TypeWithName(RNamedType(resType,convertName(n)));
                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
            } else {
                cb.constraints = cb.constraints + LocIsType(at,resType);
            }
            return <cb, true>;                                    
        } else {
            throw "Error, FieldOf tree component should be a name (location <at>)";
        }    
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: IndexedFieldOf(Tree t, RType inType, RType fieldType, loc at)
//
// Constrain the fieldType to be the result of projecting field t out of inType.
//
public tuple[ConstraintBase,bool] unifyIndexedFieldOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,iof:IndexedFieldOf(t,inType,fieldType,at)} := cb.constraints, isConcreteType(st,cb,inType)) {
        cb.constraints = cb.constraints - iof;
        inType = instantiateAllInferenceVars(st,cb,inType);
        if ((Field)`<IntegerLiteral il>` := t) {
            int ilval = toInt("<il>"); 
            RType resType = getFieldType(inType,ilval,st,at);            
            if (!isFailType(resType)) {
                if (typeHasFieldNames(inType))
                    resType = TypeWithName(RNamedType(resType,getTypeFieldName(inType,ilval)));
                else
                    resType = TypeWithName(RUnnamedType(resType));
                cb.constraints = cb.constraints + TypesAreEqual(fieldType,resType,at);
            } else {
                cb.constraints = cb.constraints + LocIsType(at,resType);
            }
            return <cb, true>;                                    
        } else {
            throw "Error, FieldOf tree component should be a name (location <at>)";
        }    
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: FieldProjection(RType inType, list[RType] fieldTypes, RType result, loc at)
//
// Constrains the resulting type to be the result of projecting the fields out of a tuple,
// map, or relation. Note that we need to maintain field names if possible. This is done by
// a simple check below: we create a set of the field names, and if we are projecting the same
// number of fields as are in the set then we know that we are not duplicating field names.
// This also handles the case where we have no field names.
//
public tuple[ConstraintBase,bool] unifyFieldProjection(SymbolTable st, ConstraintBase cb) {
    if ({_*,fpr:FieldProjection(inType,fieldTypes,result,at)} := cb.constraints, isConcreteType(st,cb,inType), isConcreteType(st,cb,fieldTypes)) {
        cb.constraints = cb.constraints - frp;

        // NOTE: We expect all the fieldTypes to be of type TypeWithName
        list[RNamedType] ftl = [ t | TypeWithName(t) <- fieldTypes];
        list[RType] ftlnn = [ getElementType(t) | t <- ftl];
        set[RName] fieldNames = { n | RNamedType(_,n) <- ftl };
        
        if (size(ftl) != size(fieldTypes)) throw "Error, fieldTypes contains invalid types: <fieldTypes>";
        
        if (isMapType(inType) || isRelType(inType)) {
            if (size(ftl) == 1)
                cb.constraints = cb.constraints + TypesAreEqual(result, makeSetType(getElementType(ftl[0])), at);
            else {
                if (size(ftl) == size(fieldNames))
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeRelTypeFromTuple(makeTupleTypeWithNames(ftl)), at);
                else
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeRelType(ftlnn), at);
            }
            return <cb, true>;
        }
    
        if (isTupleType(inType)) {
            if (size(ftl) == 1)
                cb.constraints = cb.constraints + TypesAreEqual(result, getElementType(ftl[0]), at);
            else {
                if (size(ftl) == size(fieldNames))
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeTupleTypeWithNames(ftl), at);
                else
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeTupleType(ftlnn), at);
            }
            return <cb, true>;
        } 
        
        cb.constraints = cb.constraints + LocIsType(at, makeFailType("Cannot use field projection on type <prettyPrintType(inType)>", at));
        return <cb, true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at)
//
public tuple[ConstraintBase,bool] unifySubscript(SymbolTable st, ConstraintBase cb) {
    if ({_*,s:Subscript(inType,indices,indexTypes,result,at)} := cb.constraints, isConcreteType(st,cb,inType), isConcreteType(st,cb,indexTypes)) {
        cb.constraints = cb.constraints - s;
        if (isTupleType(inType)) {
            if (size(indexTypes) != 1) 
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on tuples must contain exactly one element", at));
            else {
                // A bit of "peephole analysis" -- if we can determine the index, use that info here, but if we
                // cannot do so, just take the lub of all the tuple elements
                if (`<IntegerLiteral il>` := indices[0]) {
                    int ilval = toInt("<il>"); 
                    cb.constraints = cb.constraints + TypesAreEqual(result, getTupleFieldType(inType, ilval), at);                 
                } else {
                    if (isIntType(indexTypes[0])) {
                        cb.constraints = cb.constraints + TypesAreEqual(result, lubList(getTupleFields(inType)), at);
                    } else {
                        cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexTypes[0])>",at));
                    }
                }                
            }
            return <cb, true>;
        } else if (isRelType(inType)) {
            list[RType] relTypes = getRelFields(inType);
            if (size(indexTypes) >= size (relTypes))
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on relations must have an arity less than that of the relation",at));
            else {
                bool canFormSub = true;
                for (n <- [0..size(indices)-1]) {
                    if ((Expression)`_` !:= indices[n]) {
                        RType indexType = indexTypes[n];
                        if (isSetType(indexType)) indexType = getSetElementType(indexType);
                        if (!comparable(indexType,relTypes[n])) {
                            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscript <n>, with type <prettyPrintType(indexType)>, is not comparable to relation type <n>, <prettyPrintType(relTypes[n])>",at));
                            canFormSub = false;
                        }
                    }
                }
                if (canFormSub) {
                    list[RType] subTypes = slice(relTypes,size(indexTypes),size(relTypes)-size(indexTypes));
                    if (subTypes == 1)
                        cb.constraints = cb.constraints + TypesAreEqual(result, makeSetType(subTypes[0]), at);
                    else
                        cb.constraints = cb.constraints + TypesAreEqual(result, makeRelType(subTypes), at);
                }
            }
            return <cb, true>;     
        } else if (isMapType(inType)) {
            if (size(indexTypes) != 1) 
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on maps must contain exactly one element", at));
            else {
                if (!comparable(getMapDomainType(inType),indexTypes[0])) {
                    cb.constraints = cb.constraints + LocIsType(at, makeFailType("The subscript type <prettyPrintType(indexTypes[0])> must be comparable to the map domain type <prettyPrintType(getMapDomainType(inType))>", at));
                } else {
                    cb.constraints = cb.constraints + TypesAreEqual(result, getMapRangeType(inType), at);
                }                 
            }
            return <cb, true>;
        }  else if (isNodeType(inType)) {
            if (size(indexTypes) != 1) 
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on nodes must contain exactly one element", at));
            else {
                if (isIntType(indexTypes[0])) {
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeValueType(), at);                 
                } else {
                    cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on nodes must be of type int, not type <prettyPrintType(indexTypes[0])>",at));
                }
            }
            return <cb, true>;
        } else if (isListType(inType)) {
            if (size(indexTypes) != 1) 
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on lists must contain exactly one element", at));
            else {
                if (isIntType(indexTypes[0])) {
                    cb.constraints = cb.constraints + TypesAreEqual(result, getListElementType(inType), at);                 
                } else {
                    cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(indexTypes[0])>",at));
                }
            }
            return <cb, true>;
        } else {
            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Subscript not supported on type <prettyPrintType(inType)>", at));
            return <cb, true>;
        }
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: Comparable(RType left, RType right, loc at)
//
public tuple[ConstraintBase,bool] unifyComparable(SymbolTable st, ConstraintBase cb) {
    if ({_*,cmp:Comparable(lt,rt,at)} := cb.constraints, isConcreteType(st,cb,lt), isConcreteType(st,cb,rt)) {
        cb.constraints = cb.constraints - cmp;
        lt = instantiateAllInferenceVars(st,cb,lt);
        rt = instantiateAllInferenceVars(st,cb,rt);
        if (!comparable(lt,rt))
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Types <prettyPrintType(lt)> and <prettyPrintType(rt)> are not comparable.",at));
        return <cb, true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: AnnotationAssignable(Tree parent, loc at, Tree lhs, Tree ann, Tree rhs, RType rvalue, RType lvalue, RType result)
//
// TODO: Size check could fail in the case of aliases. We need to actually use an equivalence
// check here as well.
//
public tuple[ConstraintBase,bool] unifyAnnotationAssignable(SymbolTable st, ConstraintBase cb) {
    if ({_*, aa:AnnotationAssignable(parent,at,lhs,ann,rhs,rvalue,lvalue,result) } := cb.constraints, `<Name n>` := ann, isConcreteType(st,cb,lvalue), isConcreteType(st,cb,rvalue)) {
        cb.constraints = cb.constraints - aa;
        
        // First, get back the annotations with the given name n
        RName rn = convertName(n);
        set[STItem] annItems = { st.scopeItemMap[annId] | annId <- getAnnotationItemsForName(st, st.currentModule, rn) };
        
        // Second, narrow down this list so that we only keep those where inType
        // is a subtype of the type where the annotation is defined.
        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subtypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
        
        // Third, pick one. If we have multiple available choices here, this is
        // itself an error (unless all the choices have the same type). If we have
        // no choices, this is also an error, since that means the annotation is not
        // defined for this type. Note that the first error should be caught during
        // symbol table construction, but we handle it here just in case.
        if (size(annTypes) == 1) {
            RType annType = getOneFrom(annTypes);
            // Now, check to see that the rvalue type can be assigned into the annotation type.
            if (subtypeOf(rvalue,annType)) {
                cb.constraints = cb.constraints + TypesAreEqual(resultType,lvalue,at);
            } else {
                cb.constraints = cb.constraints + LocIsType(at,makeFailType("Can not assign value of type <prettyPrintType(rvalue)> to annotation of type <prettyPrintType(annType)>.",at));
            }
        } else if (size(annTypes) == 0) {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Annotation <prettyPrintName(rn)> is not defined on type <prettyPrintType(inType)>",at));
        } else {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Annotation <prettyPrintName(rn)> has multiple possible types on type <prettyPrintType(inType)>",at));
        }
        
        return <cb, true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: AnnotationOf(Tree t, RType inType, RType annType, loc at)
//
// TODO: Size check could fail in the case of aliases. We need to actually use an equivalence
// check here as well.
//
public tuple[ConstraintBase,bool] unifyAnnotationOf(SymbolTable st, ConstraintBase cb) {
    if ({_*, aof:AnnotationOf(t,inType,annType,at) } := cb.constraints, `<Name n>` := t, isConcreteType(st,cb,lvalue), isConcreteType(st,cb,rvalue)) {
        cb.constraints = cb.constraints - aof;
        
        // First, get back the annotations with the given name n
        RName rn = convertName(n);
        set[STItem] annItems = { st.scopeItemMap[annId] | annId <- getAnnotationItemsForName(st, st.currentModule, rn) };
        
        // Second, narrow down this list so that we only keep those where inType
        // is a subtype of the type where the annotation is defined.
        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subtypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
        
        // Third, pick one. If we have multiple available choices here, this is
        // itself an error (unless all the choices have the same type). If we have
        // no choices, this is also an error, since that means the annotation is not
        // defined for this type. Note that the first error should be caught during
        // symbol table construction, but we handle it here just in case.
        if (size(annTypes) == 1) {
            cb.constraints = cb.constraints + TypesAreEqual(annType,getOneFrom(annTypes),at);
        } else if (size(annTypes) == 0) {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Annotation <prettyPrintName(rn)> is not defined on type <prettyPrintType(inType)>",at));
        } else {
            cb.constraints = cb.constraints + LocIsType(at,makeFailType("Annotation <prettyPrintName(rn)> has multiple possible types on type <prettyPrintType(inType)>",at));
        }
        
        return <cb, true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: Composable(RType left, RType right, RType result, loc at)
//
// TODO: Add support for overloaded types and for overloaded type/function type combos
//
public tuple[ConstraintBase,bool] unifyComposable(SymbolTable st, ConstraintBase cb) {
    if ({_*,cmp:Composable(left, right, result, at)} := cb.constraints, isConcreteType(st,cb,left), isConcreteType(st,cb,right)) {
        cb.constraints = cb.constraints - cmp;
        
        if (isOverloadedType(left) && isOverloadedType(right)) {
            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Not yet supported (overlaoded function composition)!",at));
        } else if (isOverloadedType(left) && isFunctionType(right)) {
            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Not yet supported (overlaoded function composition)!",at));
        } else if (isFunctionType(left) && isOverloadedType(right)) {
            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Not yet supported (overlaoded function composition)!",at));
        } else if (isFunType(left) && isFunType(right)) {
            RType rightRet = getFunctionReturnType(right);
            list[RType] leftParams = getFunctionArgumentTypes(left);
            
            if (size(leftParams) != 1) {
                cb.constraints = cb.constarints + LocIsType(at, makeFailType("Type <prettyPrintType(left)> must have exactly one formal parameter",at));            
            } else {
                RType leftParam = leftParams[0];
                if (subtypeOf(rightRet,leftParam)) {
                    cb.constraints = cb.constraints + TypesAreEqual(result, makeFunctionType(getFunctionReturnType(left),getFunctionArgumentTypes(right)), at);
                } else {
                    cb.constraints = cb.constraints + LocIsType(at, makeFailType("Cannot compose function types <prettyPrintType(left)> and <prettyPrintType(right)>",at));                
                }            
            }
        } else if (isMapType(left) && isMapType(right)) {
            RType j1 = getMapRangeType(left); 
            RType j2 = getMapDomainType(right);
            if (! subtypeOf(j1,j2)) 
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Incompatible types in map composition: domain <prettyPrintType(j1)> and range <prettyPrintType(j2)>", at));
            else    
                cb.constraints = cb.constraints + TypesAreEqual(result,RMapType(getMapDomainType(left), getMapRangeType(right)),at);
        } else if (isRelType(left) && isRelType(right)) {
            list[RNamedType] leftFields = getRelFieldsWithNames(left); 
            list[RNamedType] rightFields = getRelFieldsWithNames(right);
    
            if (size(leftFields) != 2) {
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Error in composition: <prettyPrintType(left)> should have arity 2", at));
            }
            
            if (size(rightFields) != 2) {
                cb.constraints = cb.constraints + LocIsType(at, makeFailType("Error in composition: <prettyPrintType(right)> should have arity 2", at));
            }
            
            if (size(leftFields) == 2 && size(rightFields) == 2) {
                // Check to make sure the fields are of the right type to compose
                RType j1 = getElementType(leftFields[1]); 
                RType j2 = getElementType(rightFields[0]);
                if (! subtypeOf(j1,j2)) { 
                    cb.constraints = cb.constraints + LocIsType(at, makeFailType("Incompatible types of fields in relation composition: <prettyPrintType(j1)> and <prettyPrintType(j2)>", at));
                } else {
                    // Check to see if we need to drop the field names, then return the proper type
                    RNamedType r1 = leftFields[1]; 
                    RNamedType r2 = rightFields[0];
                    if (RNamedType(t1,n) := r1, RNamedType(t2,m) := r2, n != m)
                        cb.constraints = cb.constraints + TypesAreEqual(result,RRelType([r1,r2]),at); 
                    else 
                        cb.constraints = cb.constraints + TypesAreEqual(result,RRelType([RUnnamedType(getElementType(r1)),RUnnamedType(getElementType(r2))]),at); 
                }
            }
        } else {
            cb.constraints = cb.constraints + LocIsType(at, makeFailType("Composition is not supported on types <prettyPrintType(left)> and <prettyPrintType(right)>", at));
        }
        
        return <cb, true>;
    }
    return <cb, false>;
}

//
// CONSTRAINT: BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
//
// If we have a built-in operation, such as + : int real -> t0, this will derive the type
// for t0, adding a constraint saying t0 is equal to the result type (for instance, here
// the constraint t0 = real will be added). This will always add a constraint if the
// condition matches, although it may be a failure, for those cases where an operation 
// is not defined.
//
public tuple[ConstraintBase,bool] unifyBuiltInAppliable(SymbolTable st, ConstraintBase cb) {
    if ({c*,biac:BuiltInAppliable(op,d,r,at)} := cb.constraints, RTupleType(_) := d, isConcreteType(st,cb,d), isInferenceType(st,cb,r)) {
        cb.constraints = addConstraintsForBuiltIn(c,op,instantiateAllInferenceVars(st,cb,d),r,at);    
        return <cb,true>;
    }
    
    return <cb, false>;
}

//
// CONSTRAINT: Bindable(Tree pattern, RType subject, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyBindable(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: Enumerable(Tree pattern, RType subject, loc at)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyEnumerable(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: StepItType(Tree reducer, loc at, RType inType, RType outType, RType result)
//
// TODO: IMPLEMENT
//
public tuple[ConstraintBase,bool] unifyStepItType(SymbolTable st, ConstraintBase cb) {
    return <cb, false>;
}

//
// CONSTRAINT: DefinedBy(RType lvalue, set[STItemId] definingIds, loc at)
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
public tuple[ConstraintBase,bool] unifyDefinedBy(SymbolTable st, ConstraintBase cb) {
    if ({_*,db:DefinedBy(rt,itemIds,at)} := cb.constraints, isInferenceType(st,cb,rt)) {
        if (size(itemIds) > 1) {
            set[ROverloadedType] overloads = { };
            for (itemId <- itemIds) {
                STItem item = getSTItem(itemId, st);
                if ( (item@at) ?)
                    overloads += ROverloadedTypeWithLoc(getTypeForItem(st, itemId), item@at);
                else
                    overloads = ROverloadedType(getTypeForItem(st, itemId));
            }
            cb.constraints = cb.constraints + TypesAreEqual(rt,ROverloadedType(overloads),at) - db;
            return <cb, true>;
        } else if (size(itemIds) == 1) {
            RType itemType = getTypeForItem(st, getOneFrom(itemIds));
            cb.constraints = cb.constraints + TypesAreEqual(rt,itemType,at) - db;
            return <cb, true>;
        } else {
            throw "We should not have a defined by with no items!";
        }    
    }
    
    return <cb, false>;
}






public Constraints replaceTypes(Constraints cs, RType t1, RType t2) {
    return { instantiateInferenceVar(c,t1,t2) | c <- cs };
}

public ConstraintBase multiStepReduction(SymbolTable st, ConstraintBase cb) {
    bool keepReducing = false;
    dt1 = now();
    int steps = 0;
    do {
        <cb,keepReducing> = oneStepReduce(st,cb);
        steps += 1;
    } while (keepReducing);
    dt2 = now();
    dur = createDuration(dt1,dt2);
    println("Reduced <steps> steps in time <dur>");
    return cb;
}

//
// The type is concrete IF it does not contain an inference var OR
// IF it only contains inference vars that are in the inference
// var map, which maps inference vars to conrete types.
//
public bool concreteType(SymbolTable st, ConstraintBase cb, RType rt) {
    if (/InferenceVar(n) := rt, n notin cb.inferenceVarMap) return false;
    return true;
}

public bool concreteType(SymbolTable st, ConstraintBase cb, list[RType] rts) {
    return size([rt | rt <- rts, concreteType(st,cb,rt)]) == size(rts);
}

//
// The type is an inference type IF it is not concrete.
//
public bool inferenceType(SymbolTable st, ConstraintBase cb, RType rt) {
    return !concreteType(st,cb,rt);
}

public bool inferenceType(SymbolTable st, ConstraintBase cb, list[RType] rts) {
    return !concreteType(st,cb,rts);
}

//
// The type is a standard type if it is not one of the types added
// for the inferencer.
//
public bool standardType(RType rt) {    
    return ! (/InferenceVar(_) := rt || /SpliceableElement(_) := rt);
}

public tuple[ConstraintBase,bool] unifyTypes(ConstraintBase cb, RType t1, RType t2, loc at) {
    // First, if we have aliases or type vars, unroll them so we can try to unify actual types
    if (isAliasType(t1)) return unifyTypes(cb,getAliasedType(t1),t2,at);
    if (isAliasType(t2)) return unifyTypes(cb,t1,getAliasedType(t2),at);
    if (isTypeVar(t1)) return unifyTypes(cb,getTypeVarBound(t1),t2,at);
    if (isTypeVar(t2)) return unifyTypes(cb,t1,getTypeVarBound(t2),at);
    
    if (SpliceableElement(se1) := t1) return unifyTypes(cb,se1,t2,at);
    if (SpliceableElement(se2) := t2) return unifyTypes(cb,t1,se2,at);
    
    if (InferenceVar(n) := t1 && n in cb.inferenceVarMap) return unifyTypes(cb,cb.inferenceVarMap[n],t2,at);
    if (InferenceVar(n) := t2 && n in cb.inferenceVarMap) return unifyTypes(cb,t1,cb.inferenceVarMap[n],at);

    if (RStatementType(st1) := t1 && RStatementType(st2) := t2) return unifyTypes(cb,st1,st2,at);
        
    // Now, start going through the types on a case by case basis. First we see if one is
    // an inference var -- if so, the other is a match by default, and we can unify them.
    if (InferenceVar(_) := t1) {
        cb.constraints = cb.constraints + TypesAreEqual(t1,t2,at);
        return <cb,true>;
    }
    if (InferenceVar(_) := t2) {
        cb.constraints = cb.constraints + TypesAreEqual(t2,t1,at);
        return <cb,true>;
    }
    
    // Next, if both are scalars, we have a match if they are the same type of scalar
    if (RBoolType() := t1 && RBoolType() := t2) return <cb,true>;
    if (RIntType() := t1 && RIntType() := t2) return <cb,true>;
    if (RRealType() := t1 && RRealType() := t2) return <cb,true>;
    if (RNumType() := t1 && RNumType() := t2) return <cb,true>;
    if (RStrType() := t1 && RStrType() := t2) return <cb,true>;
    if (RValueType() := t1 && RValueType() := t2) return <cb,true>;
    if (RNodeType() := t1 && RNodeType() := t2) return <cb,true>;
    if (RVoidType() := t1 && RVoidType() := t2) return <cb,true>;
    if (RLocType() := t1 && RLocType() := t2) return <cb,true>;
    if (RDateTimeType() := t1 && RDateTimeType() := t2) return <cb,true>;
    
    // Next, check containers. We need to check both at the top level of the
    // container and descend inside.
    if (RSetType(et1) := t1 && RSetType(et2) := t2) return unifyTypes(cb,et1,et2,at);
    if (RSetType(et1) := t1 && RRelType(_) := t2) return unifyTypes(cb,et1,getRelElementType(t2),at);
    if (RRelType(_) := t1 && RSetType(et2) := t2) return unifyTypes(cb,getRelElementType(t1),et2,at);
    if (RRelType(_) := t1 && RRelType(_) := t2) return unifyTypes(cb,getRelElementType(t1),getRelElementType(t2),at);
    if (RListType(et1) := t1 && RListType(et2) := t2) return unifyTypes(cb,et1,et2,at);
    if (RBagType(et1) := t1 && RBagType(et2) := t2) return unifyTypes(cb,et1,et2,at);
    if (RMapType(_,_) := t1 && RMapType(_,_) := t2) {
        <cb,b> = unifyTypes(cb,getMapDomainType(t1),getMapDomainType(t2),at);
        return b ? unifyTypes(cb,getMapRangeType(t1),getMapRangeType(t2),at) : <cb,b>;
    }
                                                           
    // To check a tuple, we need to check the arity and all the elements
    if (RTupleType(_) := t1 && RTupleType(_) := t2) {
        list[RType] tf1 = getTupleFields(t1);
        list[RType] tf2 = getTupleFields(t2);
        if (size(tf1) != size(tf2)) return <cb,false>;
        if (size(tf1) == 0) return <cb,true>;
        for (n <- [0..size(tf1)-1]) {
            <cb, b> = unifyTypes(cb,tf1[n],tf2[n],at);
            if (!b) return <cb,b>;
        }
        return <cb,true>;
    }
    
    // To check a constructor, we need to check the constructor name, all the fields, and the ADT type
    if (RConstructorType(cn1,ct1,cnf1) := t1 && RConstructorType(cn2,ct2,cnf2) := t2) {
        list[RType] cf1 = getConstructorArgumentTypes(t1);
        list[RType] cf2 = getConstructorArgumentTypes(t2);
        
        // Check arity
        if (size(cf1) != size(cf2)) return <cb,false>;
        
        // Make sure they are both the same constructor
        if (cn1 != cn2) return <cb,false>; // TODO: Is this a problem with qualified names?
        
        // Unify the ADTs
        <cb,b> = unifyTypes(cb,ct1,ct2,at);
        if (!b) return <cb,b>;
        
        // Unify the fields
        for (n <- [0..size(cf1)-1]) {
            <cb, b> = unifyTypes(cf1[n],cf2[n],at);
            if (!b) return <cb,b>;
        }
        return <cb,true>;
    }
    
    // TODO: Add function types
    
    return <cb,false>;
}

//
// Recursively instantiate inference var infv with concrete type conc, starting
// at the constraint level and recursing down into the types.
//
public Constraint instantiateInferenceVar(Constraint c, RType infv, RType conc) {
    switch(c) {
        case TreeIsType(t,at,tt) : return TreeIsType(t,at,instantiateInferenceVar(tt,infv,conc));
        case LocIsType(at,tt) : return LocIsType(at,instantiateInferenceVar(tt,infv,conc));
        case TypesAreEqual(lt,rt,at) : return TypesAreEqual(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case LubOf(tl,tr,at) : return LubOf(instantiateInferenceVar(tl,infv,conc),instantiateInferenceVar(tr,infv,conc),at);
        case LubOfList(tl,tr,at) : return LubOfList(instantiateInferenceVar(tl,infv,conc),instantiateInferenceVar(tr,infv,conc),at);
        case LubOfSet(tl,tr,at) : return LubOfSet(instantiateInferenceVar(tl,infv,conc),instantiateInferenceVar(tr,infv,conc),at);
        case BindableToCase(tl,tr,at) : return BindableToCase(instantiateInferenceVar(tl,infv,conc),instantiateInferenceVar(tr,infv,conc),at);
        case Assignable(p,at,lhs,rhs,rv,lv,res) : return Assignable(p,at,lhs,rhs,instantiateInferenceVar(rv,infv,conc),instantiateInferenceVar(lv,infv,conc),instantiateInferenceVar(res,infv,conc));
        case Assignable(p,at,lhs,rhs,rv,lv) : return Assignable(p,at,lhs,rhs,instantiateInferenceVar(rv,infv,conc),instantiateInferenceVar(lv,infv,conc));
        case IsNameAssignable(lt,rt,at) : return IsNameAssignable(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case Returnable(t,at,r,gt,et) : return Returnable(t,at,r,instantiateInferenceVar(gt,infv,conc),instantiateInferenceVar(et,infv,conc));
        case IsRuntimeException(et,at) : return IsRuntimeException(instantiateInferenceVar(et,infv,conc),at);
        case BindsRuntimeException(_,_) : return c;
        case CaseIsReachable(ct,et,at) : return CaseIsReachable(instantiateInferenceVar(ct,infv,conc),instantiateInferenceVar(et,infv,conc),at);
        case SubtypeOf(lt,rt,at) : return SubtypeOf(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case PWAResultType(lt,rt,at) : return PWAResultType(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case IsReifiedType(rt,ptl,at,res) : return IsReifiedType(instantiateInferenceVar(rt,infv,conc),instantiateInferenceVar(ptl,infv,conc),at,instantiateInferenceVar(res,infv,conc));
        case CallOrTree(lt,ptl,res,at) : return CallOrTree(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(ptl,infv,conc),instantiateInferenceVar(res,infv,conc),at);
        case FieldOf(t,lt,rt,at) : return FieldOf(t,instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case FieldAssignable(p,at,lt,ft,rt,rv,lv) : return FieldAssignable(p,at,lt,ft,rt,instantiateInferenceVar(rv,infv,conc),instantiateInferenceVar(lv,infv,conc));
        case NamedFieldOf(t,lt,rt,at) : return NamedFieldOf(t,instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case IndexedFieldOf(t,lt,rt,at) : return IndexedFieldOf(t,instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case FieldProjection(lt,ftl,res,at) : return FieldProjection(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(ftl,infv,conc),instantiateInferenceVar(res,infv,conc),at);
        case Subscript(lt,tl,rtl,res,at) :return Subscript(instantiateInferenceVar(lt,infv,conc),tl,instantiateInferenceVar(rtl,infv,conc),instantiateInferenceVar(res,infv,conc),at);        
        case Comparable(lt,rt,at) : return Comparable(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case AnnotationAssignable(p,at,lt,ant,rt,rv,lv,res) : return AnnotationAssignable(p,at,lt,ant,rt,instantiateInferenceVar(rv,infv,conc),instantiateInferenceVar(lv,infv,conc),instantiateInferenceVar(res,infv,conc));
        case AnnotationOf(t,lt,rt,at) : return AnnotationOf(t,instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),at);
        case Composable(lt,rt,res,at) : return Composable(instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(rt,infv,conc),instantiateInferenceVar(res,infv,conc),at);
        case BuiltInAppliable(op,d,r,at) : return BuiltInAppliable(op,instantiateInferenceVar(d,infv,conc),instantiateInferenceVar(r,infv,conc),at);
        case Bindable(p,st,at) : return Bindable(p,instantiateInferenceVar(st,infv,conc),at);
        case Enumerable(p,st,at) : return Enumerable(p,instantiateInferenceVar(st,infv,conc),at);
        case StepItType(p,at,lt,st,rt) : return Enumerable(p,at,instantiateInferenceVar(lt,infv,conc),instantiateInferenceVar(st,infv,conc),instantiateInferenceVar(rt,infv,conc));
        case DefinedBy(lt,ids,at) : return DefinedBy(instantiateInferenceVar(lt,infv,conc),ids,at);
    }
}

public RNamedType instantiateInferenceVar(RNamedType nt, RType infv, RType conc) {
    if (RUnnamedType(t) := nt) 
        return RUnnamedType(instantiateInferenceVar(t,infv,conc));
    else if (RNamedType(t,tn) := nt)
        return RNamedType(instantiateInferenceVar(t,infv,conc),tn);
}        

public ROverloadedType instantiateInferenceVar(ROverloadedType olt, RType infv, RType conc) {
    if (ROverloadedType(ot) := olt)
        return ROverloadedType(instantiateInferenceVar(ot,infv,conc));
    else if (ROverloadedTypeWithLoc(ot,l) := olt)
        return ROverloadedTypeWithLoc(instantiateInferenceVar(ot,infv,conc),l);
}

public RTypeVar instantiateInferenceVar(RTypeVar tv, RType infv, RType conc) {
    if (RFreeTypeVar(vn) := tv)
        return tv;
    else if (RBoundTypeVar(vn,vb) := tv)
        return RBoundTypeVar(vn,instantiateInferenceVar(vb,infv,conc));
}

public list[RNamedType] instantiateInferenceVar(list[RNamedType] ntl, RType infv, RType conc) {
    return [ instantiateInferenceVar(nt,infv,conc) | nt <- ntl ];
}

public list[RType] instantiateInferenceVar(list[RType] tl, RType infv, RType conc) {
    return [ instantiateInferenceVar(t,infv,conc) | t <- tl ];
}

public set[ROverloadedType] instantiateInferenceVar(set[ROverloadedType] ots, RType infv, RType conc) {
    return { instantiateInferenceVar(ot,infv,conc) | ot <- ots };
}
    
public RType instantiateInferenceVar(RType t1, RType infv, RType conc) {
    switch(t1) {
        case infv : return conc;
        
        case RBoolType() : return t1;
        case RIntType() : return t1;
        case RRealType() : return t1;
        case RNumType() : return t1;
        case RStrType() : return t1;
        case RValueType() : return t1;
        case RNodeType() : return t1;
        case RVoidType() : return t1;
        case RLocType() : return t1;
        case RLexType() : return t1;
        case RNonTerminalType : return t1;
        case RDateTimeType : return t1;
        case RListType(et) : return RListType(instantiateInferenceVar(et,infv,conc));
        case RSetType(et) : return RSetType(instantiateInferenceVar(et,infv,conc));
        case RListOrSetType(et) : return RListOrSetType(instantiateInferenceVar(et,infv,conc));
        case RBagType(et) : return RBagType(instantiateInferenceVar(et,infv,conc));
        case RMapType(dt,rt) : return RMapType(instantiateInferenceVar(dt,infv,conc),instantiateInferenceVar(rt,infv,conc));
        case RRelType(lnt) : return RRelType(instantiateInferenceVar(lnt,infv,conc));         
        case RTupleType(lnt) : return RTupleType(instantiateInferenceVar(lnt,infv,conc));
        case RADTType(et) : return RADTType(instantiateInferenceVar(et,infv,conc));
        case RConstructorType(cn,at,etl) : return RConstructorType(cn,instantiateInferenceVar(at,infv,conc),instantiateInferenceVar(etl,infv,conc));
        case RFunctionType(rt,ptl) : return RFunctionType(instantiateInferenceVar(rt,infv,conc),instantiateInferenceVar(ptl,infv,conc));
        case RReifiedType(et) : return RReifiedType(instantiateInferenceVar(et,infv,conc));
        case RReifiedReifiedType(et) : return RReifiedReifiedType(instantiateInferenceVar(et,infv,conc));
        case RFailType(_) : return t1;
        case RInferredType(_) : return t1;
        case ROverloadedType(ots) : return ROverloadedType(instantiateInferenceVar(ots,infv,conc));
        case RVarArgsType(vt) : return RVarArgsType(instantiateInferenceVar(vt,infv,conc));
        case RStatementType(st) : return RStatementType(instantiateInferenceVar(st,infv,conc));
        case RAliasType(an,at) : return RAliasType(instantiateInferenceVar(an,infv,conc),instantiateInferenceVar(at,infv,conc));
        case RUserType(_) : return t1;
        case RParameterizedUserType(tn,tpl) : return RParameterizedUserType(tn,instantiateInferenceVar(tpl,infv,conc));
        case RTypeVar(tv) : return RTypeVar(instantiateInferenceVar(tv,infv,conc));
        case InferenceVar(_) : return t1;
        case TupleProjection(atl) : return TupleProjection(instantiateInferenceVar(atl,infv,conc));
        case SingleProjection(res) : return SingleProjection(instantiateInferenceVar(res,infv,conc));
        case RelationProjection(atl) : return RelationProjection(instantiateInferenceVar(atl,infv,conc));
        case SetProjection(res) : return SetProjection(instantiateInferenceVar(res,infv,conc));
        case CaseType(rp,ct) : return CaseType(instantiateInferenceVar(rp,infv,conc),instantiateInferenceVar(ct,infv,conc));
        case DefaultCaseType(ct) : return DefaultCaseType(instantiateInferenceVar(ct,infv,conc));
        case AssignableType(_) : return t1;
        case TypeWithName(nt) : return TypeWithName(instantiateInferenceVar(nt,infv,conc));
        case SpliceableElement(et) : return SpliceableElement(instantiateInferenceVar(et,infv,conc));
        case ReplacementType(p,rt) : return ReplacementType(p,instantiateInferenceVar(rt,infv,conc));
        case NoReplacementType(p,rt) : return NoReplacementType(p,instantiateInferenceVar(rt,infv,conc));
    }

    throw "Unmatched type <t1>";    
}

//
// Recursively determine if the given type is a concrete type. A type is concrete if
// it does not contain any unresolved inference variables. The duals of these,
// isInferenceType, are given for each isConcreteType function. The dual just
// checks !isConcreteType.
//
public bool isConcreteType(SymbolTable st, ConstraintBase cb, RNamedType nt) {
    if (RUnnamedType(t) := nt) 
        return isConcreteType(st, cb, t);
    else if (RNamedType(t,tn) := nt)
        return isConcreteType(st, cb, t);
}        

public bool isInferenceType(SymbolTable st, ConstraintBase cb, RNamedType nt) {
    return !isConcreteType(st, cb, nt);
}
    
public bool isConcreteType(SymbolTable st, ConstraintBase cb, ROverloadedType olt) {
    if (ROverloadedType(ot) := olt)
        return isConcreteType(st, cb, ot);
    else if (ROverloadedTypeWithLoc(ot,l) := olt)
        return isConcreteType(st, cb, ot);
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, ROverloadedType olt) {
    return !isConcreteType(st, cb, olt);
}

public bool isConcreteType(SymbolTable st, ConstraintBase cb, RTypeVar tv) {
    if (RFreeTypeVar(vn) := tv)
        return true;
    else if (RBoundTypeVar(vn,vb) := tv)
        return isConcreteType(st, cb, vb);
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, RTypeVar tv) {
    return !isConcreteType(st,cb,tv);
}

public bool isConcreteType(SymbolTable st, ConstraintBase cb, list[RNamedType] ntl) {
    return size([ nt | nt <- ntl, !isConcreteType(st, cb, nt) ]) == 0;
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, list[RNamedType] ntl) {
    return !isConcreteType(st, cb, ntl);
}

public bool isConcreteType(SymbolTable st, ConstraintBase cb, list[RType] tl) {
    return size([ t | t <- tl, !isConcreteType(st, cb, t) ]) == 0;
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, list[RType] tl) {
    return !isConcreteType(st, cb, tl);
}

public bool isConcreteType(SymbolTable st, ConstraintBase cb, set[ROverloadedType] ots) {
    return size({ ot | ot <- ots, isConcreteType(st, cb, ot) }) == 0;
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, set[ROverloadedType] ots) {
    return !isConcreteType(st, cb, ots);
}

public bool isConcreteType(SymbolTable st, ConstraintBase cb, RType t1) {
    switch(t1) {
        case RBoolType() : return true;
        case RIntType() : return true;
        case RRealType() : return true;
        case RNumType() : return true;
        case RStrType() : return true;
        case RValueType() : return true;
        case RNodeType() : return true;
        case RVoidType() : return true;
        case RLocType() : return true;
        case RLexType() : return true;
        case RNonTerminalType : return true;
        case RDateTimeType : return true;
        case RListType(et) : return isConcreteType(st, cb, et);
        case RSetType(et) : return isConcreteType(st, cb, et);
        case RListOrSetType(et) : return isConcreteType(st, cb, et);
        case RBagType(et) : return isConcreteType(st, cb, et);
        case RMapType(dt,rt) : return isConcreteType(st, cb, dt) && isConcreteType(st, cb, rt);
        case RRelType(lnt) : return isConcreteType(st, cb, lnt);         
        case RTupleType(lnt) : return isConcreteType(st, cb, lnt);
        case RADTType(et) : return isConcreteType(st, cb, et);
        case RConstructorType(cn,at,etl) : return isConcreteType(st, cb, at) && isConcreteType(st, cb, etl);
        case RFunctionType(rt,ptl) : return isConcreteType(st, cb, rt) && isConcreteType(st, cb, ptl);
        case RReifiedType(et) : return isConcreteType(st, cb, et);
        case RReifiedReifiedType(et) : return isConcreteType(st, cb, et);
        case RFailType(_) : return false;
        case RInferredType(_) : return false;
        case ROverloadedType(ots) : return isConcreteType(st, cb, ots);
        case RVarArgsType(vt) : return isConcreteType(st, cb, vt);
        case RStatementType(st) : return isConcreteType(st, cb, st);
        case RAliasType(an,at) : return isConcreteType(st, cb, an) && isConcreteType(st, cb, at);
        case RUserType(_) : return true;
        case RParameterizedUserType(tn,tpl) : return isConcreteType(st, cb, tpl);
        case RTypeVar(tv) : return isConcreteType(st, cb, tv);
        case InferenceVar(n) : if (n in cb.inferenceVarMap) return isConcreteType(st, cb, cb.inferenceVarMap[n]);
        case SpliceableElement(se) : return isConcreteType(st, cb, se);
        case TypeWithName(nt) : return isConcreteType(st, cb, nt);
    }

    return false;    
}

public bool isInferenceType(SymbolTable st, ConstraintBase cb, RType t1) {
    return !isConcreteType(st,cb,t1);
}

//
// Instantiate all inference variables, based on the current mapping given in the constraint
// base. This starts at the level of a constraint, recursing down through the types.
//
public Constraint instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, Constraint c) {
    switch(c) {
        case TreeIsType(t,at,tt) : return TreeIsType(t,at,instantiateAllInferenceVars(st,cb,tt));
        case LocIsType(at,tt) : return LocIsType(at,instantiateAllInferenceVars(st,cb,tt));
        case TypesAreEqual(lt,rt,at) : return TypesAreEqual(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case LubOf(tl,tr,at) : return LubOf(instantiateAllInferenceVars(st,cb,tl),instantiateAllInferenceVars(st,cb,tr),at);
        case LubOfList(tl,tr,at) : return LubOfList(instantiateAllInferenceVars(st,cb,tl),instantiateAllInferenceVars(st,cb,tr),at);
        case LubOfSet(tl,tr,at) : return LubOfSet(instantiateAllInferenceVars(st,cb,tl),instantiateAllInferenceVars(st,cb,tr),at);
        case BindableToCase(tl,tr,at) : return BindableToCase(instantiateAllInferenceVars(st,cb,tl),instantiateAllInferenceVars(st,cb,tr),at);
        case Assignable(p,at,lhs,rhs,rv,lv,res) : return Assignable(p,at,lhs,rhs,instantiateAllInferenceVars(st,cb,rv),instantiateAllInferenceVars(st,cb,lv),instantiateAllInferenceVars(st,cb,res));
        case Assignable(p,at,lhs,rhs,rv,lv) : return Assignable(p,at,lhs,rhs,instantiateAllInferenceVars(st,cb,rv),instantiateAllInferenceVars(st,cb,lv));
        case IsNameAssignable(lt,rt,at) : return IsNameAssignable(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case Returnable(t,at,r,gt,et) : return Returnable(t,at,r,instantiateAllInferenceVars(st,cb,gt),instantiateAllInferenceVars(st,cb,et));
        case IsRuntimeException(et,at) : return IsRuntimeException(instantiateAllInferenceVars(st,cb,et),at);
        case BindsRuntimeException(_,_) : return c;
        case CaseIsReachable(ct,et,at) : return CaseIsReachable(instantiateAllInferenceVars(st,cb,ct),instantiateAllInferenceVars(st,cb,et),at);
        case SubtypeOf(lt,rt,at) : return SubtypeOf(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case PWAResultType(lt,rt,at) : return PWAResultType(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case IsReifiedType(rt,ptl,at,res) : return IsReifiedType(instantiateAllInferenceVars(st,cb,rt),instantiateAllInferenceVars(st,cb,ptl),at,instantiateAllInferenceVars(st,cb,res));
        case CallOrTree(lt,ptl,res,at) : return CallOrTree(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,ptl),instantiateAllInferenceVars(st,cb,res),at);
        case FieldOf(t,lt,rt,at) : return FieldOf(t,instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case FieldAssignable(p,at,lt,ft,rt,rv,lv) : return FieldAssignable(p,at,lt,ft,rt,instantiateAllInferenceVars(st,cb,rv),instantiateAllInferenceVars(st,cb,lv));
        case NamedFieldOf(t,lt,rt,at) : return NamedFieldOf(t,instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case IndexedFieldOf(t,lt,rt,at) : return IndexedFieldOf(t,instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case FieldProjection(lt,ftl,res,at) : return FieldProjection(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,ftl),instantiateAllInferenceVars(st,cb,res),at);
        case Subscript(lt,tl,rtl,res,at) :return Subscript(instantiateAllInferenceVars(st,cb,lt),tl,instantiateAllInferenceVars(rtl,cb,lt),instantiateAllInferenceVars(st,cb,res),at);        
        case Comparable(lt,rt,at) : return Comparable(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case AnnotationAssignable(p,at,lt,ant,rt,rv,lv,res) : return AnnotationAssignable(p,at,lt,ant,rt,instantiateAllInferenceVars(st,cb,rv),instantiateAllInferenceVars(st,cb,lv),instantiateAllInferenceVars(st,cb,res));
        case AnnotationOf(t,lt,rt,at) : return AnnotationOf(t,instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),at);
        case Composable(lt,rt,res,at) : return Composable(instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,rt),instantiateAllInferenceVars(st,cb,res),at);
        case BuiltInAppliable(op,d,r,at) : return BuiltInAppliable(op,instantiateAllInferenceVars(st,cb,d),instantiateAllInferenceVars(st,cb,r),at);
        case Bindable(p,stt,at) : return Bindable(p,instantiateAllInferenceVars(st,cb,stt),at);
        case Enumerable(p,stt,at) : return Enumerable(p,instantiateAllInferenceVars(st,cb,stt),at);
        case StepItType(p,at,lt,stt,rt) : return StepItType(p,at,instantiateAllInferenceVars(st,cb,lt),instantiateAllInferenceVars(st,cb,stt),instantiateAllInferenceVars(st,cb,rt));
        case DefinedBy(lt,ids,at) : return DefinedBy(instantiateAllInferenceVars(st,cb,lt),ids,at);
    }
}

public RNamedType instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, RNamedType nt) {
    if (RUnnamedType(t) := nt) 
        return RUnnamedType(instantiateAllInferenceVars(st,cb,t));
    else if (RNamedType(t,tn) := nt)
        return RNamedType(instantiateAllInferenceVars(st,cb,t),tn);
}        

public ROverloadedType instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, ROverloadedType olt) {
    if (ROverloadedType(ot) := olt)
        return ROverloadedType(instantiateAllInferenceVars(st,cb,ot));
    else if (ROverloadedTypeWithLoc(ot,l) := olt)
        return ROverloadedTypeWithLoc(instantiateAllInferenceVars(st,cb,ot),l);
}

public RTypeVar instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, RTypeVar tv) {
    if (RFreeTypeVar(vn) := tv)
        return tv;
    else if (RBoundTypeVar(vn,vb) := tv)
        return RBoundTypeVar(vn,instantiateAllInferenceVars(st,cb,vb));
}

public list[RNamedType] instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, list[RNamedType] ntl) {
    return [ instantiateAllInferenceVars(st,cb,nt) | nt <- ntl ];
}

public list[RType] instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, list[RType] tl) {
    return [ instantiateAllInferenceVars(st,cb,t) | t <- tl ];
}

public set[ROverloadedType] instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, set[ROverloadedType] ots) {
    return { instantiateAllInferenceVars(st,cb,ot) | ot <- ots };
}
    
public RType instantiateAllInferenceVars(SymbolTable st, ConstraintBase cb, RType t1) {
    switch(t1) {
        case InferenceVar(vnum) : return (vnum in cb.inferenceVarMap ? instantiateAllInferenceVars(st,cb,cb.inferenceVarMap[vnum]) : t1);
        
        case RBoolType() : return t1;
        case RIntType() : return t1;
        case RRealType() : return t1;
        case RNumType() : return t1;
        case RStrType() : return t1;
        case RValueType() : return t1;
        case RNodeType() : return t1;
        case RVoidType() : return t1;
        case RLocType() : return t1;
        case RLexType() : return t1;
        case RNonTerminalType : return t1;
        case RDateTimeType : return t1;
        case RListType(et) : return RListType(instantiateAllInferenceVars(st,cb,et));
        case RSetType(et) : return RSetType(instantiateAllInferenceVars(st,cb,et));
        case RListOrSetType(et) : return RListOrSetType(instantiateAllInferenceVars(st,cb,et));
        case RBagType(et) : return RBagType(instantiateAllInferenceVars(st,cb,et));
        case RMapType(dt,rt) : return RMapType(instantiateAllInferenceVars(st,cb,dt),instantiateAllInferenceVars(st,cb,rt));
        case RRelType(lnt) : return RRelType(instantiateAllInferenceVars(st,cb,lnt));         
        case RTupleType(lnt) : return RTupleType(instantiateAllInferenceVars(st,cb,lnt));
        case RADTType(et) : return RADTType(instantiateAllInferenceVars(st,cb,et));
        case RConstructorType(cn,at,etl) : return RConstructorType(cn,instantiateAllInferenceVars(st,cb,at),instantiateAllInferenceVars(st,cb,etl));
        case RFunctionType(rt,ptl) : return RFunctionType(instantiateAllInferenceVars(st,cb,rt),instantiateAllInferenceVars(st,cb,ptl));
        case RReifiedType(et) : return RReifiedType(instantiateAllInferenceVars(st,cb,et));
        case RReifiedReifiedType(et) : return RReifiedReifiedType(instantiateAllInferenceVars(st,cb,et));
        case RFailType(_) : return t1;
        case RInferredType(_) : return t1;
        case ROverloadedType(ots) : return ROverloadedType(instantiateAllInferenceVars(st,cb,ots));
        case RVarArgsType(vt) : return RVarArgsType(instantiateAllInferenceVars(st,cb,vt));
        case RStatementType(st) : return RStatementType(instantiateAllInferenceVars(st,cb,st));
        case RAliasType(an,at) : return RAliasType(instantiateAllInferenceVars(st,cb,an),instantiateAllInferenceVars(st,cb,at));
        case RUserType(_) : return t1;
        case RParameterizedUserType(tn,tpl) : return RParameterizedUserType(tn,instantiateAllInferenceVars(st,cb,tpl));
        case RTypeVar(tv) : return RTypeVar(instantiateAllInferenceVars(st,cb,tv));
        case TupleProjection(atl) : return TupleProjection(instantiateAllInferenceVars(st,cb,atl));
        case SingleProjection(res) : return SingleProjection(instantiateAllInferenceVars(st,cb,res));
        case RelationProjection(atl) : return RelationProjection(instantiateAllInferenceVars(st,cb,atl));
        case SetProjection(res) : return SetProjection(instantiateAllInferenceVars(st,cb,res));
        case CaseType(rp,ct) : return CaseType(instantiateAllInferenceVars(st,cb,rp),instantiateAllInferenceVars(st,cb,ct));
        case DefaultCaseType(ct) : return DefaultCaseType(instantiateAllInferenceVars(st,cb,ct));
        case AssignableType(_) : return t1;
        case TypeWithName(nt) : return TypeWithName(instantiateAllInferenceVars(st,cb,nt));
        case SpliceableElement(et) : return SpliceableElement(instantiateAllInferenceVars(st,cb,et));
        case ReplacementType(p,rt) : return ReplacementType(p,instantiateAllInferenceVars(st,cb,rt));
        case NoReplacementType(p,rt) : return NoReplacementType(p,instantiateAllInferenceVars(st,cb,rt));
    }

    throw "Unmatched type <t1>";    
}
