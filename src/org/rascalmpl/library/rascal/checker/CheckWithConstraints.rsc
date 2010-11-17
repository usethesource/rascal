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
import rascal::checker::constraints::BuiltIns;
import rascal::checker::constraints::Fields;

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

public ConstraintBase gatherConstraints(SymbolTable st, Tree t) {
    ConstraintBase cb = makeNewConstraintBase();
    
    visit(t) {
        case `<Statement stmt>` : cb = gatherStatementConstraints(st,cb,stmt);
        
        case `<Expression exp>` : cb = gatherExpressionConstraints(st,cb,exp);
    }
    
    return cb;
}

//
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

public tuple[ConstraintBase,bool] oneStepReduce(SymbolTable st, ConstraintBase cb) {
    <cbp, b> = unifyDefinedBy(st,cb); if (b) return <cbp, b>;

    <cbp, b> = unifyTypesAreEqual(st,cb); if (b) return <cbp,b>;
    
    <cbp, b> = unifyTreeIsType(st,cb); if (b) return <cbp,b>;

    <cbp, b> = unifyBuiltInAppliable(st,cb); if (b) return <cbp,b>;
    
    <cbp, b> = unifyLubOfSet(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifyLubOfList(st,cb); if (b) return <cbp, b>;
    
    <cbp, b> = unifySubtypeOf(st,cb); if (b) return <cbp, b>;
    
    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyTreeIsType(SymbolTable st, ConstraintBase cb) {
    if ({_*,ti1:TreeIsType(t1,l1,rt1),ti2:TreeIsType(t1,l1,rt2)} := cb.constraints) {
        <cbp,b> = unifyTypes(cb,rt1,rt2);
        if (b) {
            println("Unified constraints <prettyPrintConstraint(ti1)>, <prettyPrintConstraint(ti2)>");
            cbp.constraints = cbp.constraints - (standardType(rt1) ? ti2 : ti1);
            return <cbp,true>;
        }
    } 

    return <cb, false>;
}

//
// TODO: We will have trouble here if we have circular constraints, i.e., t(1) = list[t(1)], where
// we would have an infinite expansion. We need to add a check to avoid this case.
//
public tuple[ConstraintBase,bool] unifyTypesAreEqual(SymbolTable st, ConstraintBase cb) {
    //
    // The first two checks are for top-level assignments to inference vars, i.e., t(1) = list[t(2)],
    // or map[int] = t(3). The third is where we have two types that are equated that contain nested
    // variables, i.e., list[t(1)] = list[int], or map[int,t(1)] = map[t(2),bool].
    //
    if ({_*,ti1:TypesAreEqual(rt1:InferenceVar(_),rt2)} := cb.constraints) {
        println("Replacing type <rt1> with type <rt2>");
        cb.constraints = replaceTypes(cb.constraints - ti1, rt1, rt2);
        return <cb,true>;
    } else if ({_*,ti1:TypesAreEqual(rt1,rt2:InferenceVar(_))} := cb.constraints) {
        println("Replacing type <rt2> with type <rt1>");
        cb.constraints = replaceTypes(cb.constraints - ti1, rt2, rt1);
        return <cb,true>;
    } else if ({_*,til:TypesAreEqual(rt1,rt2)} := cb.constraints) {
        println("Expanding equality constraint <prettyPrintConstraint(til)> to account for nested constraints");
        <cbp,b> = unifyTypes(cb,rt1,rt2);
        if (b) {
            cbp.constraints = cbp.constraints - til;
            return <cbp,true>;
        }
    }
    
    return <cb, false>;
}

//
// Add constraints for built-in operations. Note that we will always add a constraint if we call addConstraintsForBuiltIn,
// but this may constraint a type to be fail in a case where the operands are not compatible with the operation being
// used. If we return false, that means we did not have any built in appliable constraints that we could use.
//
public tuple[ConstraintBase,bool] unifyBuiltInAppliable(SymbolTable st, ConstraintBase cb) {
    // The operands are required to be concrete, since we aren't using the operator signatures to derive anything
    // except for equality constraints. In other words, if we see t(1) + t(2), we do not constrain t(1) to be a type
    // that can be on the left of a +, t(2) to be a type that can be on the right.
    if ({c*,biac:BuiltInAppliable(op,d,r,at)} := cb.constraints, RTupleType(_) := d, concreteType(getTupleFields(d)), inferenceType(r)) {
        println("Adding constraints for BuiltInAppliable <prettyPrintConstraint(biac)>");
        cb.constraints = addConstraintsForBuiltIn(c,op,d,r,at);    
        return <cb,true>;
    }
    
    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyLubOfSet(SymbolTable st, ConstraintBase cb) {
    if ({_*,los:LubOfSet(lubs,r)} := cb.constraints, concreteType(lubs), inferenceType(r)) {
        lubs2 =
            for (rt <- lubs) { 
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
        cb.constraints = cb.constraints + TypesAreEqual(r,lubList(lubs2)) - los;
        return <cb,true>;
    }
    
    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyLubOfList(SymbolTable st, ConstraintBase cb) {
    if ({_*,los:LubOfList(lubs,r)} := cb.constraints, concreteType(lubs), inferenceType(r)) {
        lubs2 =
            for (rt <- lubs) { 
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
        cb.constraints = cb.constraints + TypesAreEqual(r,lubList(lubs2)) - los;
        return <cb,true>;
    }
    
    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifyDefinedBy(SymbolTable st, ConstraintBase cb) {
    if ({_*,db:DefinedBy(rt,itemIds)} := cb.constraints, inferenceType(rt)) {
        if (size(itemIds) > 1) {
            set[ROverloadedType] overloads = { };
            for (itemId <- itemIds) {
                STItem item = getSTItem(itemId, st);
                if ( (item@at) ?)
                    overloads += ROverloadedTypeWithLoc(getTypeForItem(st, itemId), item@at);
                else
                    overloads = ROverloadedType(getTypeForItem(st, itemId));
            }
            cb.constraints = cb.constraints + TypesAreEqual(rt,ROverloadedType(overloads)) - db;
            return <cb, true>;
        } else if (size(itemIds) == 1) {
            RType itemType = getTypeForItem(st, getOneFrom(itemIds));
            cb.constraints = cb.constraints + TypesAreEqual(rt,itemType) - db;
            return <cb, true>;
        } else {
            throw "We should not have a defined by with no items!";
        }    
    }
    
    return <cb, false>;
}

public tuple[ConstraintBase,bool] unifySubtypeOf(SymbolTable st, ConstraintBase cb) {
    if ({_*,sof:SubtypeOf(lt,rt)} := cb.constraints, concreteType(lt), concreteType(rt), subtypeOf(lt,rt)) {
        cb.constraints = cb.constraints - sof;
        return <cb, true>;
    }
    
    return <cb, false>;
}

public Constraints replaceTypes(Constraints cs, RType t1, RType t2) {
    return visit(cs) {
        case t1 => t2
    };
}

public ConstraintBase multiStepReduction(SymbolTable st, ConstraintBase cb) {
    bool keepReducing = false;
    do {
        <cb,keepReducing> = oneStepReduce(st,cb);
    } while (keepReducing == true);
    return cb;
}

public bool inferenceType(RType rt) {
    return /InferenceVar(_) := rt;
}

public bool concreteType(RType rt) {
    return !inferenceType(rt);
}

public bool inferenceType(list[RType] rts) {
    return size([rt | rt <- rts, inferenceType(rt)]) > 0;
}

public bool concreteType(list[RType] rts) {
    return !inferenceType(rts);
}

public bool standardType(RType rt) {
    return ! (/InferenceVar(_) := rt || /SpliceableElement(_) := rt);
}

public tuple[ConstraintBase,bool] unifyTypes(ConstraintBase cb, RType t1, RType t2) {
    // First, if we have aliases or type vars, unroll them so we can try to unify actual types
    if (isAliasType(t1)) return unifyTypes(cb,getAliasedType(t1),t2);
    if (isAliasType(t2)) return unifyTypes(cb,t1,getAliasedType(t2));
    if (isTypeVar(t1)) return unifyTypes(cb,getTypeVarBound(t1),t2);
    if (isTypeVar(t2)) return unifyTypes(cb,t1,getTypeVarBound(t2));
    
    if (SpliceableElement(se1) := t1) return unifyTypes(cb,se1,t2);
    if (SpliceableElement(se2) := t2) return unifyTypes(cb,t1,se2);

    if (RStatementType(st1) := t1 && RStatementType(st2) := t2) return unifyTypes(cb,st1,st2);
        
    // Now, start going through the types on a case by case basis. First we see if one is
    // an inference var -- if so, the other is a match by default, and we can unify them.
    if (InferenceVar(_) := t1) {
        cb.constraints = cb.constraints + TypesAreEqual(t1,t2);
        return <cb,true>;
    }
    if (InferenceVar(_) := t2) {
        cb.constraints = cb.constraints + TypesAreEqual(t2,t1);
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
    if (RSetType(et1) := t1 && RSetType(et2) := t2) return unifyTypes(cb,et1,et2);
    if (RSetType(et1) := t1 && RRelType(_) := t2) return unifyTypes(cb,et1,getRelElementType(t2));
    if (RRelType(_) := t1 && RSetType(et2) := t2) return unifyTypes(cb,getRelElementType(t1),et2);
    if (RRelType(_) := t1 && RRelType(_) := t2) return unifyTypes(cb,getRelElementType(t1),getRelElementType(t2));
    if (RListType(et1) := t1 && RListType(et2) := t2) return unifyTypes(cb,et1,et2);
    if (RBagType(et1) := t1 && RBagType(et2) := t2) return unifyTypes(cb,et1,et2);
    if (RMapType(_,_) := t1 && RMapType(_,_) := t2) {
        <cb,b> = unifyTypes(cb,getMapDomainType(t1),getMapDomainType(t2));
        return b ? unifyTypes(cb,getMapRangeType(t1),getMapRangeType(t2)) : <cb,b>;
    }
                                                           
    // To check a tuple, we need to check the arity and all the elements
    if (RTupleType(_) := t1 && RTupleType(_) := t2) {
        list[RType] tf1 = getTupleFields(t1);
        list[RType] tf2 = getTupleFields(t2);
        if (size(tf1) != size(tf2)) return <cb,false>;
        if (size(tf1) == 0) return <cb,true>;
        for (n <- [0..size(tf1)-1]) {
            <cb, b> = unifyTypes(tf1[n],tf2[n]);
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
        <cb,b> = unifyTypes(cb,ct1,ct2);
        if (!b) return <cb,b>;
        
        // Unify the fields
        for (n <- [0..size(cf1)-1]) {
            <cb, b> = unifyTypes(cf1[n],cf2[n]);
            if (!b) return <cb,b>;
        }
        return <cb,true>;
    }
    
    // TODO: Add function types
    
    return <cb,false>;
}
