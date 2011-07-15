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
import lang::rascal::types::TypeInstantiation;
import lang::rascal::types::TypeEquivalence;
import lang::rascal::scoping::ResolveNames;
import lang::rascal::scoping::ScopedTypes;
import lang::rascal::checker::TreeUtils;
import lang::rascal::checker::TypeMatching;

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
import lang::rascal::checker::constraints::Pattern;

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
        
        case Statement stmt : cb = gatherStatementConstraints(st,cb,stmt);
        
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
//        // is a subType of the type where the annotation is defined.
//        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subTypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
//        
//        // Third, pick one. If we have multiple available choices here, this is
//        // itself an error (unless all the choices have the same type). If we have
//        // no choices, this is also an error, since that means the annotation is not
//        // defined for this type. Note that the first error should be caught during
//        // symbol table construction, but we handle it here just in case.
//        if (size(annTypes) == 1) {
//            RType annType = getOneFrom(annTypes);
//            // Now, check to see that the rvalue type can be assigned into the annotation type.
//            if (subTypeOf(rvalue,annType)) {
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
//        // is a subType of the type where the annotation is defined.
//        set[RType] annTypes = { markUserTypes(annItem.annoType,st,st.currentModule) | annItem <- annItems, subTypeOf(inType,markUserTypes(annItem.onType,st,st.currentModule)) };
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
//                if (subTypeOf(rightRet,leftParam)) {
//                    cb.constraints = cb.constraints + TypesAreEqual(result, makeFunctionType(getFunctionReturnType(left),getFunctionArgumentTypes(right)), at);
//                } else {
//                    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, at), makeFailType("Cannot compose function types <prettyPrintType(left)> and <prettyPrintType(right)>",at),at);                
//                }            
//            }
//        } else if (isMapType(left) && isMapType(right)) {
//            RType j1 = getMapRangeType(left); 
//            RType j2 = getMapDomainType(right);
//            if (! subTypeOf(j1,j2)) 
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
//                if (! subTypeOf(j1,j2)) { 
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
// TODO: Make sure the subType rules are set up to properly handle
// overloaded types, the rule should be that ot1 <: t2 when at least one
// of the overloads in ot1 <: t2.
//
// TODO: Also need to make sure other functions handle overloaded types. Should
// just look at these again and make sure we handle them consistently. Note that
// there is no restriction here that these all be function or constructor types, 
// but in practice that is what will happen, since other cases are scope errors.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, DefinedBy(RType lvalue, set[ItemId] definingIds, loc at)) {
    RType itemType = makeVoidType();
    
    if (size(definingIds) > 1) {
        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
    } else if (size(definingIds) == 1) {
        itemType = getTypeForItem(st, getOneFrom(definingIds));
    } else {
        itemType = makeFailType("No type has been defined for this item", at);
    }
    
    return DefinedBy(itemType,definingIds,at);
}

//
// CONSTRAINT: ConstrainType(RType constrainedType, RType typeConstraint, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, ConstrainType(RType constrainedType, RType typeConstraint, loc at)) {
    return ConstrainType(typeConstraint, typeConstraint, at);
}

//
// CONSTRAINT: BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, BuiltInAppliable(RBuiltInOp op, RType domain, RType range, loc at)) {
    RType result = range;
    if (isTupleType(domain)) {
        // TODO: Do we need to do the unwind here? doesn't hurt, but may not help, 
        // in which case it just slows it down
        unwound = makeTupleType([ unwindAliases(tf) | tf <- getTupleFields(domain)]);
        result = getBuiltInResultType(op,unwound,range,at);
    }
    return BuiltInAppliable(op,domain,result,at);
}

//
// CONSTRAINT: Assignable(RType lvalue, RType rvalue, RType result, loc at)
//
// TODO: Need to "lock" certain parts of the resulting type, since (for instance)
// if we have a name defined with a non-inferred type we cannot just assign and lub it.
// (NOTE: This is also commented as such in Assignable.rsc)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Assignable(RType lvalue, RType rvalue, RType result, loc at)) {
    set[RType] itypes = getInferredTypes(lvalue);
    RType lval = lvalue;
    RType res = result;

    if (size(itypes) > 0) {
        // We have inferred types on the left-hand side (i.e., the piece being assigned into). So,
        // to see if the assignment is valid, we need to see if we can bind the subject type to
        // the assignable type.
        tuple[rel[RType,RType],bool] bindings = unifyTypes(lvalue,rvalue);
        if (bindings[1]) {
            // we were able to unify, which means the result type should be the same as the
            // rvalue type (or we wouldn't have unified against the vars in lvalue); rvalue
            // had to be concrete, so the result is as well
            lval = instantiate(lval, bindings[0]);
            res = lval;
        } else {
            res = makeFailType("Cannot unify current lvalue type of <prettyPrintType(lvalue)> and rvalue type of <prettyPrintType(rvalue)>", at);
        }
    } else {
        // lvalue is concrete, as is rvalue -- the result is the lub; code in the main solving routine needs
        // to ensure that this is correct, in the sense that the item with type lvalue can actually have
        // its type changed (assuming lub(lvalue,rvalue) != lvalue)
        lval = lvalue;
        res = lub(lvalue, rvalue);    
    }
    
    return Assignable(lval, rvalue, res, at);
}

//
// CONSTRAINT: ComboAssignable(RType lvalue, RType rvalue, RType result, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, ComboAssignable(RType lvalue, RType rvalue, RType result, loc at)) {
    // lvalue is concrete, as is rvalue -- the result is the lub; code in the main solving routine needs
    // to ensure that this is correct, in the sense that the item with type lvalue can actually have
    // its type changed (assuming lub(lvalue,rvalue) != lvalue)
    return ComboAssignable(lvalue, rvalue, lub(lvalue, rvalue), at);
}

//
// CONSTRAINT: LubOf(list[RType] typesToLub, RType lubResult, loc at)
//
// Constrains lubResult to be the lub of typesToLub.
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOf(list[RType] typesToLub, RType lubResult, loc at)) {
    return LubOf(typesToLub,lubList(typesToLub),at);
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
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOfList(list[RType] typesToLub, RType lubResult, loc at)) {
    lubs =
        for (rt <- typesToLub) {
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
    return LubOfList(typesToLub,lubList(lubs),at);
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
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, LubOfSet(list[RType] typesToLub, RType lubResult, loc at)) {
    lubs =
        for (rt <- typesToLub) {
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
    return LubOfSet(typesToLub,lubList(lubs),at);
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
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, CallOrTree(RType lt, list[RType] pts, RType rt, loc at)) {
    set[RType] failures = { }; set[tuple[RType,RType]] matches = { }; set[RType] targets = { };
    targets = isOverloadedType(lt) ? getOverloadOptions(lt) : { lt };
    
    for ( tt <- targets ) {
        if (isFunctionType(tt)) {
            list[RType] ptypes = getFunctionArgumentTypes(tt);
            list[str] failMessages = [ ];
            if (size(ptypes) == size(pts)) {
                for (idx <- index(pts),!subTypeOf(pts[idx],ptypes[idx]))
                    failMessages += "Type mismatch at position <idx+1>, <prettyPrintType(pts[idx])> must be a subType of <prettyPrintType(ptypes[idx])>";
            } else if (isVarArgsFun(tt), size(pts) >= (size(ptypes)-1)) {
                // why size(ptypes)-1? because if we have a var args fun, we could give 0 arguments
                // TODO: corner case: we could pass in a list instead of individual elements, add logic for this
                RType varArgsElement = getListElementType(last(ptypes));
                for (idx <- index(pts),!subTypeOf(pts[idx],(idx == size(ptypes)-1) ? varArgsElement : ptypes[idx]))
                    failMessages += "Type mismatch at position <idx+1>, <prettyPrintType(pts[idx])> must be a subType of <prettyPrintType((idx == size(ptypes)-1) ? varArgsElement : ptypes[idx])>";
            } else {
                failMessages += "Arity mismatch, too few arguments given";
            }
            if (size(failMessages) == 0) {
                // If we have matched so far, try to find mappings for any type variables
                if (typeContainsTypeVars(tt)) {
                    RType actualTypes = makeTupleType(pts);
                    RType paramTypes = makeTupleType([ (isVarArgsFun(tt) && idx == (size(ptypes)-1)) ? getListElementType(ptypes[idx]) : ptypes[idx] | idx <- index(ptypes)]);
                    map[RName varName, RType varType] varMapping = getTVBindings(paramTypes, actualTypes, initializeTypeVarMap(tt), at);
                    matches += < instantiateVars(getFunctionReturnType(tt), varMapping), instantiateVars(tt, varMapping) >;
                } else {
                    matches += < getFunctionReturnType(tt), tt >;
                }
            } else {
                failures += makeFailType("Cannot apply function with type <prettyPrintType(tt)>: " + intercalate(",", failMessages), at);
            }
        } else if (isConstructorType(tt)) {
            list[RType] ptypes = getConstructorArgumentTypes(tt);
            list[str] failMessages = [ ];
            if (size(ptypes) == size(pts)) {
                for (idx <- index(ptypes),!subTypeOf(pts[idx],ptypes[idx]))
                    failMessages += "Type mismatch at position <idx+1>, <prettyPrintType(pts[idx])> must be a subType of <prettyPrintType(ptypes[idx])>";
            } else {
                failMessages += "Arity mismatch";
            }            
            if (size(failMessages) == 0)
                // If we have matched so far, try to find mappings for any type variables
                if (typeContainsTypeVars(tt)) {
                    RType actualTypes = makeTupleType(pts);
                    RType paramTypes = makeTupleType(ptypes);
                    map[RName varName, RType varType] varMapping = getTVBindings(paramTypes, actualTypes, initializeTypeVarMap(tt), at);
                    matches += < instantiateVars(getConstructorResultType(tt), varMapping), instantiateVars(tt, varMapping) >;
                } else {
                    matches += < getConstructorResultType(tt), tt >;
                }
            else
                failures += makeFailType("Cannot apply constructor with type <prettyPrintType(tt)>: " + intercalate(",", failMessages), at);
        } else if (isStrType(tt)) {
            // If the target is a string, the result is a node, regardless of the
            // types given in the parameters (these are not reflected in the node
            // type at all).
            matches += < makeNodeType(), tt >;
        } else if (isLocType(tt)) {
            // If the target is a location, the parameters must be the extra details
            // for the location offset information. This should be an int, an int,
            // a tuple[int,int], and a tuple[int,int].
            list[str] failMessages = [ ];
            if (size(pts) == 4) {
                if (! isIntType(pts[0]))
                    failMessages += "The first argument must be of type int";
                if (! isIntType(pts[1]))
                    failMessages += "The second argument must be of type int";
                if (! (isTupleType(pts[2]) && size(getTupleFields(pts[2])) == 2 && isIntType(getTupleFields(pts[2])[0]) && isIntType(getTupleFields(pts[2])[1])))
                    failMessages += "The third argument must be of type tuple[int,int]";
                if (! (isTupleType(pts[3]) && size(getTupleFields(pts[3])) == 2 && isIntType(getTupleFields(pts[3])[0]) && isIntType(getTupleFields(pts[3])[1])))
                    failMessages += "The fourth argument must be of type tuple[int,int]";
            } else {
                failMessages += "There must be four offset parameters";
            }
            if (size(failMessages) == 0)
                matches += < makeLocType(), tt >;
            else
                failures += makeFailType("Unable to construct location: " + intercalate(",", failMessages), at);
        } else {
            failures += makeFailType("Unexpected type <prettyPrintType(tt)>: expected a function, constructor, str, or loc", at);
        }
    }

    // If we have a match, use that -- if we have none, or too many, we have an error
    if (size(matches) == 1) {
        return CallOrTree(getOneFrom(matches)[1], pts, getOneFrom(matches)[0], at);
    } else if (size(matches) > 1) {
        RType res = makeFailType("Invalid call or construction: multiple possible matches found", at);
        return CallOrTree(lt, pts, res, at);
    } else if (size(failures) > 0){
        RType res = collapseFailTypes(failures);
        return CallOrTree(lt, pts, res, at);
    }
    
    return CallOrTree(lt,pts,rt,at);
}
    
//
// CONSTRAINT: Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Subscript(RType inType, list[Tree] indices, list[RType] indexTypes, RType result, loc at)) {
    RType res = result;
    
    if (isTupleType(inType)) {
        if (size(indexTypes) != 1) {
            res = makeFailType("The index on a value of type tuple must consist of only 1 element", at);
        } else {
            tupleFields = getTupleFields(inType);
            if (`<IntegerLiteral il>` := indices[0]) {
                int ilval = toInt("<il>");
                if (ilval < size(tupleFields))
                    res = getTupleFieldType(inType, ilval);
                else
                    res = makeFailType("The index <il> should be in the range 0 .. <size(tupleFields)-1>", at);                 
            } else {
                if (isIntType(indexTypes[0]))
                    res = lubList(tupleFields);
                else
                    res = makeFailType("Subscripts on tuples must be of type int, not type <prettyPrintType(indexTypes[0])>",at);
            }           
        }    
    } else if (isRelType(inType)) {
        list[RType] relTypes = getRelFields(inType);
        if (size(indexTypes) >= size (relTypes))
            res = makeFailType("Subscripts on relations must have an arity less than that of the relation",at);
        else {
            list[str] msgs = [ ]; 
            for (n <- [0..size(indices)-1]) {
                if ((Expression)`_` !:= indices[n]) {
                    RType indexType = indexTypes[n];
                    if (isSetType(indexType)) indexType = getSetElementType(indexType);
                    if (!comparable(indexType,relTypes[n]))
                        msgs += "Subscript <n>, with index type <prettyPrintType(indexType)>, is not comparable to relation type <n>, with indexing type <prettyPrintType(relTypes[n])>";
                }
            }
            if (size(msgs) == 0) {
                list[RType] subTypes = slice(relTypes,size(indexTypes),size(relTypes)-size(indexTypes));
                if (subTypes == 1)
                    res = makeSetType(subTypes[0]);
                else
                    res = makeRelType(subTypes);
            } else {
                res = makeFailType(intercalate(",", msgs), at);
            }
        }    
    } else if (isMapType(inType)) {
        if (size(indexTypes) != 1) 
            res = makeFailType("Subscripts on maps must contain exactly one element", at);
        else {
            if (!comparable(getMapDomainType(inType),indexTypes[0])) {
                res = makeFailType("The subscript type <prettyPrintType(indexTypes[0])> must be comparable to the map domain type <prettyPrintType(getMapDomainType(inType))>", at);
            } else {
                res = getMapRangeType(inType);
            }                 
        }
    } else if (isNodeType(inType)) {
        if (size(indexTypes) != 1) 
            res = makeFailType("Subscripts on nodes must contain exactly one element", at);
        else {
            if (isIntType(indexTypes[0]))
                res = makeValueType();                 
            else
                res = makeFailType("Subscripts on nodes must be of type int, not type <prettyPrintType(indexTypes[0])>",at);
        }
    } else if (isListType(inType)) {
        if (size(indexTypes) != 1) 
            res = makeFailType("Subscripts on lists must contain exactly one element", at);
        else {
            if (isIntType(indexTypes[0]))
                res = getListElementType(inType);                 
            else
                re = makeFailType("Subscripts on lists must be of type int, not type <prettyPrintType(indexTypes[0])>",at);
        }
    } else {
        res = makeFailType("Subscript not supported on type <prettyPrintType(inType)>", at);    
    }
    
    return Subscript(inType,indices,indexTypes,res,at);
}

//
// CONSTRAINT: FieldOf(Tree t, RType inType, RType fieldType, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, FieldOf(Tree t, RType inType, RType fieldType, loc at)) {
    RType res = fieldType;
    if (`<Name n>` := t) {
        res = getFieldType(inType,convertName(n),st,at);
    } else {
        res = makeFailType("Error, field should be a name, not <t>",at);
    }    
    return FieldOf(t,inType,res,at);
}

//
// CONSTRAINT: Comparable(RType left, RType right, SolveResult sr, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Comparable(RType left, RType right, SolveResult sr, loc at)) {
    return Comparable(left, right, comparable(left,right) ? T() : F(), at);
}

//
// CONSTRAINT: subTypeOf(RType left, RType right, SolveResult sr, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, SubtypeOf(RType left, RType right, SolveResult sr, loc at)) {
    return SubtypeOf(left, right, SubtypeOf(left,right) ? T() : F(), at);
}

//
// CONSTRAINT: StepItType(Tree reducer, RType inType, RType outType, RType result, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, StepItType(Tree reducer, RType inType, RType outType, RType result, loc at)) {
    return StepItType(reducer, inType, outType, result, at); // NO CHANGES, TODO: ACTUALLY SOLVE
}

//
// CONSTRAINT: CaseIsReachable(RType caseType, RType expType, SolveResult sr, loc at)
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, CaseIsReachable(RType caseType, RType expType, SolveResult sr, loc at)) {
    return CaseIsReachable(caseType, expType, sr, at); // TODO: ACTUALLY SOLVE
}

//
// CONSTRAINT: BindableToCase(RType subject, RType caseType, SolveResult sr, loc at)
//
// TODO: The logic here is the same as that for Comparable. It is informative having
// a separate constraint name, but should we just use Comparable instead to avoid
// duplication?
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, BindableToCase(RType subject, RType caseType, SolveResult sr, loc at)) {
    return BindableToCase(subject, caseType, comparable(subject,caseType) ? T() : F(), at);
}

//
// CONSTRAINT: Bindable(Pattern pat, RType subjectType, RType result, loc at)
//
// TODO: IMPLEMENT
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Bindable(Pattern pat, RType subjectType, RType result, loc at)) {
    < bindings, failures > = bindPattern(cb, pat, subjectType);
    if (size(failures) == 0) {
        result = instantiate(result, bindings);
    } else {
        result = makeFailType("Binding failure, more info to come!", at);
    }
    
    return Bindable(pat, subjectType, result, at);
}

//
// CONSTRAINT: Enumerable(Tree pattern, RType subject, SolveResult sr, loc at)
//
// TODO: IMPLEMENT
//
public Constraint solveConstraint(STBuilder st, ConstraintBase cb, Enumerable(Tree pattern, RType subject, SolveResult sr, loc at)) {
    return Enumerable(pattern, subject, sr, at);
}

//
// Default version of the solve function -- this does nothing beyond return the current
// constraint, making no changes.
//
public default Constraint solveConstraint(STBuilder st, ConstraintBase cb, Constraint c) = c;

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

public tuple[ConstraintBase,ConstraintGraph,bool,map[RType,RType]] solveConstraints(STBuilder st, ConstraintBase cb) {
    ConstraintGraph updateGraph(ConstraintGraph cg, Constraint old, Constraint new) {
        ConstraintGraph oldl = { < CN(old), cr > | < CN(old), cr > <- cg };
        ConstraintGraph oldr = { < cl, CN(old) > | < cl, CN(old) > <- cg };
        ConstraintGraph newl = { < CN(new), cr > | cr <- oldl<1> };
        ConstraintGraph newr = { < cl, CN(new) > | cl <- oldr<0> };
        
        return cg - oldl - oldr + newl + newr;
    }
    
    int solvedCount = 0;
    
    // First, using the current constraint base, generate a graph representation
    // of the dependencies inherent in the constraints.
    println("Generating Constraint Graph...");
    ConstraintGraph cg = generateConstraintGraph(cb);
    println("Constraint Graph Generated");
    set[ConstraintNode] frontier = { cn | cn:CN(c) <- top(cg), solvable(c) };
    
    // Maintain a structure to keep track of the history of the constraints
    rel[Constraint,Constraint] history = { };
    
    // and a structure to keep track of the assignments to type variables
    map[RType,RType] typeAssignment = ( t : t | TN(t) <- carrier(cg) );
    
    // If we don't have a frontier, this means that all the nodes depend on
    // some other node. If this is the case, it means that we cannot
    // solve the constraints.
    if (size(frontier) == 0) {
        println("ERROR: Irreducible constraint system.");
        return < cb, cg, false, typeAssignment >;
    }

    // Using the frontier, start solving the constraints in the graph, pushing
    // forward using a "frontier" of unsolved nodes. This frontier represents
    // the propagation of information discovered during the solving process.
    bool keepGoing = true;
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
                //println("SOLVING: <c>");
                
                Constraint cp = solveConstraint(st, cb, c);
                if (c != cp) {
                    keepGoing = true;

                    // Put the modified constraint back into the constraint graph
                    cg = updateGraph(cg, c, cp);
                    solvedCount += 1;
                    if (solvedCount % 100 == 0) println("Constraints Solved: <solvedCount>");
                    //println("SOLVED: <c> --\> <cp>");
                    
                    // Save the modification as part of the history for this constraint
                    history += < c, cp >;
                    
                    // Find the information to update any bindings of inference vars
                    < bindings, res > = mappings(c, cp);
                    
                    // If we could derive the mappings, use these to update the constraints
                    // that depend on these types, and put the modified constraints into
                    // the next frontier and back into the graph.
                    if (res) {
                        // For each inferred type on the left-hand side of the bindings, find all the inferred
                        // types on the right-hand side, and tie these in to the graph so we ensure future
                        // type updates are correctly propagated.
                        rel[RType,RType] updateTargets = { };
                        for (tl <- bindings<0>, isInferenceType(tl), tr <- { tii | ti <- bindings[tl], tii <- getInferredTypes(ti)}) {
                            if (tr notin typeAssignment) typeAssignment[tr] = tr;
                            if (typeAssignment[tr] != tr) updateTargets = updateTargets + < tr, typeAssignment[tr] >;
                            for ( c2c <- cg[TN(tl)] ) cg = cg + < TN(tr), c2c >;
                            for ( c2c <- (invert(cg))[TN(tl)] ) cg = cg + < c2c, TN(tr) >;
                        }
                        if (size(updateTargets) > 0) {
                            cpold = cp;
                            cp = instantiate(cpold, updateTargets);
                            cg = updateGraph(cg, cpold, cp);
                             < bindings, res > = mappings(c, cp);
                             if (!res) throw "Error, re-mapping should not fail after succeeding!";
                        }

                        // Handle any failures in the bindings -- fail always "overrides" any other types                        
                        failures = { < tf, tt > | tf <- bindings<0>, tt <- bindings[tf], isFailType(tt) };
                        for (tf <- failures<0>) typeAssignment[tf] = collapseFailTypes(failures[tf] + { typeAssignment[tf] });

                        // Handle any successes -- we need to ensure we don't override existing failure information
                        // TODO: need to handle type merges along different paths here, we currently include a rough version
                        // of this here...
                        successes = { < tf, tt > | tf <- bindings<0>, tf notin failures<0>, tt <- bindings[tf], !isFailType(tt), !isInferenceType(tt) };
                        for (tf <- successes<0>, !isFailType(typeAssignment[tf])) {
                            setToLub = successes[tf] + (isInferenceType(typeAssignment[tf]) ? { } : { typeAssignment[tf] });
                            if (size(setToLub) > 0)
                                typeAssignment[tf] = lubSet(setToLub);
                        } 

                        // Get all the constraints that we should update with the new type mapping
                        set[Constraint] toUpdate = { cu |  < tf, tt > <- bindings, tf != tt, < TN(tf), CN(cu) > <- cg };
                        
                        // Update each of these constraints with the new type information
                        rel[Constraint,Constraint] updated = { < cu, instantiate(cu, bindings) > | cu <- toUpdate };
                        //for ( <c1,c2> <- updated ) println("PROPAGATED: <c1> --\> <c2>");
                        
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
    
    println("Total Constraints Solved: <solvedCount>");
    return < cb, cg, true, typeAssignment >;
        
}

public Tree decorateTree(Tree t, ConstraintBase cb, map[RType,RType] tmap) {
    set[Message] messages = ( (t@messages)? ) ? t@messages : { }; 
    messages = messages + { error(s,l) | t <- tmap<1>, RFailType(etup) := t, <s,l> <- etup };
    if (size(messages) > 0) t@messages = messages;
    return visit(t) {
        case Expression e : if (e@\loc in cb.locTypes) insert(e[@rtype=tmap[cb.locTypes[e@\loc]]][@doc="TYPE:<prettyPrintType(tmap[cb.locTypes[e@\loc]])>"]); 
    }
}

//
// Check a tree
//
public Tree checkTree(Tree t) {
    println("TYPE CHECKER: Resolving Tree");
    < t, st > = resolveTreeAux(t, true);
    println("TYPE CHECKER: Resolved Tree");

    println("TYPE CHECKER: Gathering Constraints");
    cb = gatherConstraints(st, t);
    println("TYPE CHECKER: Gathered Constraints");
    
    println("TYPE CHECKER: Solving Constraints");
    < cb, cg, res, tmap > = solveConstraints(st, cb);
    println("TYPE CHECKER: Solved Constraints");
    
    println("TYPE CHECKER: Decorating Tree");
    t = decorateTree(t, cb, tmap);
    println("TYPE CHECKER: Decorated Tree");
    
    return t;
}

public tuple[rel[RType,RType] bindings, rel[Pattern,RType,RType] failures] bindPattern(ConstraintBase cb, Pattern pat, RType subjectType) {
    rel[RType,RType] bindings = { };
    rel[Pattern,RType,RType] failures = { };

    void bindPattern(Pattern pat, RType subjectType) {
        switch(pat) {
            case (Pattern)`<BooleanLiteral _>` :
                if (isBoolType(subjectType) || isValueType(subjectType)) return;
            
            case (Pattern)`<DecimalIntegerLiteral il>` :
                if (isIntType(subjectType) || isNumType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<OctalIntegerLiteral il>` :
                if (isIntType(subjectType) || isNumType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<HexIntegerLiteral il>` :
                if (isIntType(subjectType) || isNumType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<RealLiteral rl>` :
                if (isRealType(subjectType) || isNumType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<StringLiteral sl>` :
                if (isStrType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<LocationLiteral ll>` :
                if (isLocType(subjectType) || isValueType(subjectType)) return;
    
            case (Pattern)`<DateTimeLiteral dtl>` :
                if (isDateTimeType(subjectType) || isValueType(subjectType)) return;
            
            case (Pattern)`<RegExpLiteral rl>` :
                if (isStrType(subjectType) || isValueType(subjectType)) return;
            
            case (Pattern)`_` : {
                bindings = bindings + < typeForLoc(cb, pat@\loc), subjectType >;
                return;
            }
            
            case (Pattern)`<Name n>` :  {
                bindings = bindings + < typeForLoc(cb, pat@\loc), subjectType >;
                return;
            }
            
            case (Pattern)`<QualifiedName qn>` :  {
                bindings = bindings + < typeForLoc(cb, pat@\loc), subjectType >;
                return;
            }
            
            case (Pattern) `<Type t> <Name n>` : {
                bindings = bindings + < typeForLoc(cb, pat@\loc), subjectType >;
                return;
            }
            
            case (Pattern) `[<{Pattern ","}* pl>]` : {
                if (isListType(subjectType)) {
                    for (pli <- pl) {
                        if ((Pattern)`_*` := pli) {
                            bindings = bindings + < typeForLoc(cb, pli@\loc), subjectType >;
                        } else if ((Pattern)`<QualifiedName qn> *` := pli) {
                            bindings = bindings + < typeForLoc(cb, pli@\loc), subjectType >;
                        } else {
                            bindPattern(pli, getListElementType(subjectType)); 
                        }
                    }
                    return;
                }
            }
    
            case (Pattern) `{<{Pattern ","}* pl>}` : {
                if (isSetType(subjectType)) {
                    for (pli <- pl) {
                        if ((Pattern)`_*` := pli) {
                            bindings = bindings + < typeForLoc(cb, pli@\loc), subjectType >;
                        } else if ((Pattern)`<QualifiedName qn> *` := pli) {
                            bindings = bindings + < typeForLoc(cb, pli@\loc), subjectType >;
                        } else {
                            bindPattern(pli, getSetElementType(subjectType)); 
                        }
                    }
                    return;
                }
            }
            
            case (Pattern) `<BasicType t> ( <{Pattern ","}* pl> )` : {
                // TODO: Add code here
                return;
            }
    
            case (Pattern) `<Pattern p1> ( <{Pattern ","}* pl> )` : {
                // TODO: Add code here
                return;
            }
            
            case (Pattern) `/ <Pattern p>` : {
                // TODO: Add code here
                // We may need to bind p without respect to type, we have no idea what it will match against
                return;
            }
    
            case (Pattern) `<Name n> : <Pattern p>` : {
                bindPattern(p, subjectType); // The bound type will flow through to the type for n based on existing constraints
                return;
            }
            
            case (Pattern) `<Type t> <Name n> : <Pattern p>` : {
                bindPattern(p, subjectType); // The bound type will flow through to the type for n based on existing constraints
                return;
            }
    
            case (Pattern) `[ <Type t> ] <Pattern p>` : {
                bindPattern(p, subjectType);
                return;
            }
            
            case (Pattern) `! <Pattern p>` : {
                bindPattern(p, subjectType);
                return;
            }
        }
    
        if (prod(_,_,attrs([_*,term(cons("Tuple")),_*])) := pat[0]) {
            if (isTupleType(subjectType)) {
                list[Pattern] tupleContents = getTuplePatternContents(pat);
                list[RType] subjectTypes = getTupleFields(subjectType);
                
                if (size(tupleContents) == size(subjectTypes)) {
                    for (idx <- index(tupleContents))
                        bindPattern(tupleContents[idx], subjectTypes[idx]);
                    return;
                }
            }
        }

        failures = failures + < pat, patType, subjectType >;
        return;
    }

    bindPattern(pat, subjectType);    
    return < bindings, failures >;
}
