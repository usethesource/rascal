module rascal::checker::Check

import IO;
import List;
import Set;

import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::Namespace;

import rascal::\old-syntax::Rascal;

// TODO: the type checker should:
//   -- annotate expressions and statements with their type
//   -- infer types for untyped local variables
//   -- annotate patterns with their type
//   -- check type consistency for:
//           -- function calls
//           -- case patterns (i.e. for switch it should be the same as the expression that is switched on)
//           -- case rules (left-hand side equal to right-hand side
//           -- rewrite rules (idem)
//   -- filter ambiguous Rascal due to concrete syntax, by:
//          -- type-checking each alternative and filtering type incorrect ones
//          -- counting the size of concrete fragments and minimizing the number of characters in concrete fragments
//          -- comparing the chain rules at the top of fragments, and minimizing those
//          -- balancing left and right-hand side of rules and patterns with replacement by type equality
//   -- check additional static constraints
//          -- across ||, --> and <--> conditional composition, all pattern matches introduce the same set of variables 
//          -- all variables have been both declared and initialized in all control flow paths
//          -- switch either implements a default case, or deals with all declared alternatives

private str getTypeString(Name n) {
	if ( (n@rtype)? )
		return "TYPE: " + prettyPrintType(n@rtype);
	else
		return "TYPE unavailable";
}
 
public Tree check(Tree t) {
	return visit(t) {
		 
		case Name n => n[@doc = getTypeString(n)]

		case Expression e => e[@rtype = checkExpression(e)]
		
		case Pattern p => p[@rtype = checkPattern(p)]
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
// Literals are checked directly in checkExpression
//

//
// Qualified names are checked directly in checkExpression
//

private RType checkReifiedTypeExpression(Expression ep, Type t, {Expression ","}* el) {
	if (checkForFail({ e@rtype | e <- el }))
		return collapseFailTypes({ e@rtype | e <- el });
	else
		return makeReifiedType(convertType(t), [ e@rtype | e <- el ]);
}

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
	if (checkForFail({ e@rtype | e <- es }))
		return collapseFailTypes({ e@rtype | e <- es });
	else
		return makeListType(lubList([e@rtype | e <- es]));
}

public RType checkSetExpression(Expression ep, {Expression ","}* es) {
	if (checkForFail({ e@rtype | e <- es }))
		return collapseFailTypes({ e@rtype | e <- es });
	else
		return makeSetType(lubList([e@rtype | e <- es]));
}

public RType checkTupleExpression(Expression ep, Expression ei, {Expression ","}* es) {
	set[Expression] eset = {ei} + {e | e <- es};
	if (checkForFail({e@rtype | e <- eset}))
		return collapseFailTypes({e@rtype | e <- eset});
	else
		return makeTupleType([ e@rtype | e <- eset]);
}

// TODO: Implement this...
public RType checkMapExpression(Expression ep) {
	return makeVoidType();
}

public RType checkClosureExpression(Expression ep, Type t, Parameters p, Statement+ ss) {
	return makeVoidType();
}

public RType checkVoidClosureExpression(Expression ep, Type t, Parameters p, Statement+ ss) {
	return makeVoidType();
}
 
public RType checkNonEmptyBlockExpression(Expression ep, Statement+ ss) {
	return makeVoidType();
}

public RType checkVisitExpression(Expression ep, Label l, Visit v) {
	return makeVoidType();
}

//
// Paren expressions are handled below in checkExpression
//

public RType checkRangeExpression(Expression ep, Expression e1, Expression e2) {
	if (isIntType(e1@rtype) && isIntType(e2@rtype)) {
		return RTypeStructured(RStructuredType(RListType(), [ RTypeArg(RTypeBasic(RIntType())) ]));
	} else {
		return propagateFailOr({ e1@rtype, e2@rtype },makeFailType("Error in range operation: operation is not defined on the types " +
			prettyPrintType(e1@rtype) + " and " + prettyPrintType(e2@rtype),ep@\loc));
	}
}

public RType checkStepRangeExpression(Expression ep, Expression e1, Expression e2, Expression e3) {
	if (isIntType(e1@rtype) && isIntType(e2@rtype) && isIntType(e3@rtype)) {
		return RTypeStructured(RStructuredType(RListType(), [ RTypeArg(RTypeBasic(RIntType())) ]));
	} else {
		return propagateFailOr({e1@rtype, e2@rtype, e3@rtype },makeFailType("Error in step range operation: operation is not defined on the types " +
			prettyPrintType(e1@rtype) + ", " + prettyPrintType(e2@rtype) + " and " + prettyPrintType(e3@rtype),ep@\loc));
	}
}

//
// Reify type expressions are handled below in checkExpression
//

public RType checkFieldUpdateExpression(Expression ep, Expression el, Name n, Expression er) {
	return makeVoidType();
}

// TODO: Add a table with the built-in types and fields
public RType checkFieldAccessExpression(Expression ep, Expression el, Name n) {
	RName fn = convertName(n);
	if (isTupleType(el@rtype) && tupleHasField(el@rtype,fn))
		return getTupleFieldType(el@rtype,fn);
	else if (isTupleType(el@rtype)) {
		return makeFailType("Tuple <prettyPrintType(el@rtype)> does not include field <prettyPrintName(fn)>",ep@\loc);
	} else if (isFailType(el@rtype)) {
		return el@rtype;
	}	
	// else ADT
	// else built-in
	return makeVoidType();
}

public RType checkFieldProjectExpression(Expression ep, Expression e1, {Field ","}+ fl) {
	return makeVoidType();
}

public RType checkSubscriptExpression(Expression ep, Expression el, {Expression ","}+ es) {
	return makeVoidType();
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
	if (isIntType(e@rtype)) {
		return makeIntType();
	} else if (isRealType(e@rtype)) {
		return makeRealType();
	} else {
		return propagateFailOr({ e@rtype },makeFailType("Error in negation operation: <e> should have a numeric type " + 
			"but instead has type " + prettyPrintType(e@rtype),ep@\loc));
	}
}

public RType checkTransitiveReflexiveClosureExpression(Expression ep, Expression e) {
	return makeVoidType();
}

public RType checkTransitiveClosureExpression(Expression ep, Expression e) {
	return makeVoidType();
}

public RType checkGetAnnotationExpression(Expression ep, Expression e, Name n) {
	return makeVoidType();
}

public RType checkSetAnnotationExpression(Expression ep, Expression el, Name n, Expression er) {
	return makeVoidType();
}

public RType checkCompositionExpression(Expression ep, Expression el, Expression er) {
	return makeVoidType();
}

public RType checkProductExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeIntType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeRealType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype)) {
		return makeSetType(makeTupleType([getSetElementType(el@rtype),getSetElementType(er@rtype)]));
	} else if (isListType(el@rtype) && isListType(er@rtype)) {
		return makeListType(makeTupleType([getListElementType(el@rtype),getListElementType(er@rtype)]));
	} else {
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in product operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkJoinExpression(Expression ep, Expression el, Expression er) {
	return makeVoidType();
}

public RType checkDivExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeIntType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeRealType();
	} else {
		return propagateFailOr({ el@rtype, er@rtype },
			makeFailType("Error in division operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkModExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeIntType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeRealType();
	} else {
		return propagateFailOr({ el@rtype, er@rtype },
			makeFailType("Error in mod operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkIntersectionExpression(Expression ep, Expression el, Expression er) {
	return makeVoidType();
}

public RType checkPlusExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeIntType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeRealType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeStrType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype)) {
		return makeSetType(lub(getSetElementType(el@rtype),getSetElementType(er@rtype)));
	} else if (isListType(el@rtype) && isListType(er@rtype)) {
		return makeListType(lub(getListElementType(el@rtype),getListElementType(er@rtype)));
	} else {
		// TODO: Handle Map, Tuple cases
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in sum operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

//
// TODO:
// Question: why should - change the type of the result? Shouldn't set[a] - set[b] or
// list[a] - list[b] always result in a set or list of type a?
//
public RType checkMinusExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeIntType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeRealType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeRealType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeStrType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype)) {
		return makeSetType(lub(getSetElementType(el@rtype),getSetElementType(er@rtype)));
	} else if (isListType(el@rtype) && isListType(er@rtype)) {
		return makeListType(lub(getListElementType(el@rtype),getListElementType(er@rtype)));
	} else {
		// TODO: Handle Map case
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in difference operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkNotInExpression(Expression ep, Expression el, Expression er) {
	if (isSetType(er@rtype) && getSetElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else if (isListType(er@rtype) && getListElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else {
		// TODO: Handle Map type, see what is needed for boolean operations
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in notin operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkInExpression(Expression ep, Expression el, Expression er) {
	if (isSetType(er@rtype) && getSetElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else if (isListType(er@rtype) && getListElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else {
		// TODO: Handle Map type, see what is needed for boolean operations
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in in operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkLessThanExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in less than operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkLessThanOrEqualExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in less than or equal to operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkGreaterThanExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in greater than operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkGreaterThanOrEqualExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in greater than or equal to operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkEqualsExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in equals operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkNotEqualsExpression(Expression ep, Expression el, Expression er) {
	if (isIntType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isIntType(el@rtype) && isRealType(er@rtype)) {
		return makeBoolType();
	} else if (isRealType(el@rtype) && isIntType(er@rtype)) {
		return makeBoolType();
	} else if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else if (isStrType(el@rtype) && isStrType(er@rtype)) {
		return makeBoolType();
	} else if (isListType(el@rtype) && isListType(er@rtype) && getListElementType(el@rtype) == getListElementType(er@rtype)) {
		return makeBoolType();
	} else if (isSetType(el@rtype) && isSetType(er@rtype) && getSetElementType(el@rtype) == getSetElementType(er@rtype)) {
		return makeBoolType();
	} else {
		// TODO: Handle Location, Map, Node, Tuple, Value types
		return propagateFailOr({ el@rtype, er@rtype },makeFailType("Error in not equals operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
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

public RType checkIfDefinedOtherwiseExpression(Expression ep, Expression ed, Expression eo) {
	return makeVoidType();
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

// TODO: Do we want to enforce that it is possible to the two sides to match? Not a static error, but could
// prevent confusion (if developer THINKS this can match, but it never can...)
public RType checkMatchExpression(Expression ep, Pattern p, Expression e) {
	if (checkForFail({ p@rtype, e@rtype })) 
		return collapseFailTypes({ p@rtype, e@rtype });
	else if (subtypeOf(p@rtype,e@rtype) || subtypeOf(e@rtype,p@rtype)) // TODO: Is this right?
		return makeBoolType();
	else
		return makeFailType("Types of <p> and <e>, <prettyPrintType(p@rtype)> and <prettyPrintType(e@rtype)>, should be in a subtype relation",ep@\loc);
}

// TODO: See checkMatchExpression above
public RType checkNoMatchExpression(Expression ep, Pattern p, Expression e) {
	if (checkForFail({ p@rtype, e@rtype })) 
		return collapseFailTypes({ p@rtype, e@rtype });
	else if (subtypeOf(p@rtype,e@rtype) || subtypeOf(e@rtype,p@rtype)) // TODO: Is this right?
		return makeBoolType();
	else
		return makeFailType("Types of <p> and <e>, <prettyPrintType(p@rtype)> and <prettyPrintType(e@rtype)>, should be in a subtype relation",ep@\loc);
}

// TODO: See checkMatchExpression above
public RType checkEnumeratorExpression(Expression ep, Pattern p, Expression e) {
	if (checkForFail({ p@rtype, e@rtype })) 
		return collapseFailTypes({ p@rtype, e@rtype });
	else if (isListType(e@rtype) && subtypeOf(getListElementType(e@rtype), p@rtype))
		return makeBoolType();
	else if (isSetType(e@rtype) && subtypeOf(getSetElementType(e@rtype), p@rtype))
		return makeBoolType();
	else
		return makeFailType("Types of <p> and <e>, <prettyPrintType(p@rtype)> and <prettyPrintType(e@rtype)>, should be in a subtype relation",ep@\loc);
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
			return makeSetType(lubSet({ e@rtype | e <- els }));
		} else {
			return collapseFailType(genFailures);
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
			return makeListType(lubSet({ e@rtype | e <- els }));
		} else {
			return collapseFailType(genFailures);
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
			return makeMapType(ef@rtype, et@rtype);
		} else {
			return collapseFailType(genFailures);
		}
	}
}

public RType checkReducerExpression(Expression ep, Expression ei, Expression er, {Expression ","}+ ers) {
	return makeVoidType();
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
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RBoolType())));
			return makeBoolType();
		}

		case (Expression)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: DecimalIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<OctalIntegerLiteral il>`  : {
			if (debug) println("CHECKER: OctalIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<HexIntegerLiteral il>`  : {
			if (debug) println("CHECKER: HexIntegerLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<RealLiteral rl>`  : {
			if (debug) println("CHECKER: RealLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RRealType())));
			return makeRealType();
		}

		// TODO: Interpolation
		case (Expression)`<StringLiteral sl>`  : {
			if (debug) println("CHECKER: StringLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RStrType())));
			return makeStrType();
		}

		// TODO: Interpolation
		case (Expression)`<LocationLiteral ll>`  : {
			if (debug) println("CHECKER: LocationLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RLocType())));
			return RTypeBasic(RLocType());
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			if (debug) println("CHECKER: DateTimeLiteral: <exp>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(RTypeBasic(RDateTimeType())));
			return RTypeBasic(RDateTimeType());
		}

		// TODO: See if we ever have this; a qualified name, not a name, is an expression
		case (Expression)`<Name n>`: {
			if (debug) println("CHECKER: Name: <exp>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(n@rtype));
			return n@rtype; // TODO: Should throw an exception if the name has no type information
		}
		
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			if (debug) println("CHECKER: QualifiedName: <exp>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return qn@rtype; // TODO: Should throw an exception if the name has no type information
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
			if (debug) println("CHECKER: Call or Tree: <e1>(<el>)");
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

		// Tuple
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

		// Subscript (currently broken)
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

public RType checkReifiedTypePattern(Pattern pp, Type t, {Pattern ","}* pl) {
	if (checkForFail({ p@rtype | p <- pl }))
		return collapseFailTypes({ p@rtype | p <- pl });
	else {
		return makeReifiedType(convertType(t), [ p@rtype | p <- pl ]);
	}
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
	if (checkForFail({ p@rtype | p <- ps }))
		return collapseFailTypes({ p@rtype | p <- ps });
	else
		return makeListType(lubList([p@rtype | p <- ps]));
}

public RType checkSetPattern(Pattern pp, {Pattern ","}* ps) {
	if (checkForFail({ p@rtype | p <- ps }))
		return collapseFailTypes({ p@rtype | p <- ps });
	else
		return makeSetType(lubList([p@rtype | p <- ps]));
}

public RType checkTuplePattern(Pattern pp, Pattern pi, {Pattern ","}* ps) {
	set[Pattern] pset = {pi} + {p | p <- ps};
	if (checkForFail({p@rtype | p <- pset}))
		return collapseFailTypes({p@rtype | p <- pset});
	else
		return makeTupleType([ p@rtype | p <- pset]);
}

// TODO: Implement this once we can match maps
public RType checkMapPattern(Pattern pp) {
	return makeVoidType();
}

public RType checkDescendantPattern(Pattern pp, Pattern p) {
	return p@rtype;
}

// TODO: Look in current interpreter for typing rules
public RType checkVariableBecomesPattern(Pattern pp, Name n, Pattern p) {
	return makeVoidType();
}

// TODO: Look in current interpreter for typing rules
public RType checkTypedVariableBecomesPattern(Pattern pp, Type t, Name n, Pattern p) {
	return makeVoidType();
}

// TODO: Look in current interpreter for typing rules
public RType checkGuardedPattern(Pattern pp, Type t, Pattern p) {
	return makeVoidType();
}

// TODO: Look in current interpreter for typing rules
public RType checkAntiPattern(Pattern pp, Pattern p) {
	return makeVoidType();
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
			return n@rtype; // TODO: Should throw an exception if the name has no type information
		}
		
		// QualifiedName
		case (Pattern)`<QualifiedName qn>`: {
			if (debug) println("CHECKER: QualifiedNamePattern: <pat>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return qn@rtype; // TODO: Should throw an exception if the name has no type information
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
			RType t = checkCallOrTreePattern(pat,p1,pl);
			if(debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// List
		case `[<{Pattern ","}* pl>]` : {
			if (debug) println("CHECKER: ListPattern: <pat>");
			RType t = checkListPattern(pat,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Set
		case `{<{Pattern ","}* pl>}` : {
			if (debug) println("CHECKER: SetPattern: <pat>");
			RType t = checkSetPattern(pat,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Tuple
		case `<<Pattern pi>, <{Pattern ","}* pl>>` : {
			if (debug) println("CHECKER: TuplePattern: <pat>");
			RType t = checkTuplePattern(pat,pi,pl);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TODO: Map: Need to figure out a syntax that works for matching this
//		case `<<Pattern ei>, <{Pattern ","}* el>>` : {
//			// TODO: This is not yet working
//			if (debug) println("CHECKER: Tuple <pat>");
//			RType t = checkTuplePattern(exp,ei,el);
//			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
//			return t;
//		}

		// Descendant
		case `/ <Pattern p>` : {
			if (debug) println("CHECKER: DescendantPattern: <pat>");
			RType t = checkDescendantPattern(pat,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Variable Becomes
		case `<Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: VariableBecomesPattern: <pat>");
			RType t = checkVariableBecomesPattern(pat,n,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Typed Variable Becomes
		case `<Type t> <Name n> : <Pattern p>` : {
			if (debug) println("CHECKER: TypedVariableBecomesPattern: <pat>");
			RType t = checkTypedVariableBecomesPattern(pat,t,n,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Guarded
		case `[ <Type t> ] <Pattern p>` : {
			if (debug) println("CHECKER: GuardedPattern: <pat>");
			RType t = checkGuardedPattern(pat,t,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}			
		
		// Anti
		case `! <Pattern p>` : {
			if (debug) println("CHECKER: AntiPattern: <pat>");
			RType t = checkAntiPattern(pat,p);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
	}
}

//
// Check Pattern with Action productions
//
public RType checkPatternWithAction(PatternWithAction p) {
	switch(p) {
		case `<Pattern p> => <Expression e>` : {
			3;
		}
		
		case `<Pattern p> => <Expression er> when <{Expression ","}+ es>` : {
			3;
		}
		
		case `<Pattern p> : <Statement s>` : {
			3;
		}
	}
	
	return makeVoidType();
}

//
// Check visits
//
public RType checkVisit(Visit v) {
	return makeVoidType();
}

//
// Check individual cases
//
public RType checkCase(Case c) {
	switch(c) {
		case `case <PatternWithAction p>` : {
			if (debug) println("CHECKER: Case: <c>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(p@rtype));
			return p@rtype;
		}
		
		case `default : <Statement b>` : {
			if (debug) println("CHECKER: Case: <c>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(b@rtype));
			return b@rtype;
		}
	}
}

// TODO: Implement
public RType checkSubscriptAssignable(Assignable ap, Assignable a, Expression e) {
	return makeVoidType();
}

// TODO: Implement
public RType checkFieldAccessAssignable(Assignable ap, Assignable a, Name n) {
	return makeVoidType();
}
		
public RType checkIfDefinedOrDefaultAssignable(Assignable ap, Assignable a, Expression e) {
	if (isFailType(a@rtype) || isFailType(e@rtype)) {
		return collapseFailTypes({ a@rtype, e@rtype });
	} else {
		if (!subtypeOf(e@rtype,a@rtype)) {
			return makeFailType("The type of <e>, <prettyPrintType(e@rtype)>, is not a subtype of the type of <a>, <prettyPrintType(a@rtype)>",ap@\loc);
		} else {
			return lub(e@rtype,a@rtype);		
		}
	}
}

// TODO: Implement
public RType checkAnnotationAssignable(Assignable ap, Assignable a, Name n) {
	return makeVoidType();
}
		
public RType checkTupleAssignable(Assignable ap, {Assignable ","}+ al) {
	if (checkForFail({ a@rtype | a <- al }))
		return collapseFailTypes({ a@rtype | a <- al });
	else
		return makeTupleType([ a@rtype | a <- al]);
}

public RType checkConstructorAssignable(Assignable ap, Name n, {Assignable ","}+ al) {
	if (checkForFail({ a@rtype | a <- al }))
		return collapseFailTypes({ a@rtype | a <- al });

	set[RType] alternatives = isOverloadedType(n@rtype) ? getOverloadOptions(n@rtype) : { n@rtype };
	
	// Now, try each alternative, seeing if one matches.
	for (a <- alternatives) {
		list[Assignable] args = [ a | a <- al ];
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
			for (ai <- args) {
				RType argType = head(argTypes); argTypes = tail(argTypes);
				if (argType != ai@rtype) {
					potentialResultType = makeFailType("Bad function invocation or constructor usage, argument type mismatch",ap@\loc); // TODO: Improve error message
				}
			}			
		} else {
			potentialResultType = makeFailType("Arity mismatch", ap@\loc); // TODO: Improve error message
		}
		
		// This will cause us to keep the last error in cases where we cannot find a valid function
		// or constructor to use.
		if (isFailType(resultType)) resultType = potentialResultType;
	}

	return resultType;	
}

//
// Check assignables
//
public RType checkAssignable(Assignable a) {
	switch(a) {
		// Variable
		case (Assignable)`<QualifiedName qn>` : {
			if (debug) println("CHECKER: VariableAssignable: <pat>");
			if (debug) println("CHECKER: Assigned type: " + prettyPrintType(qn@rtype));
			return qn@rtype;
		}
		
		// Subscript
		case `<Assignable al> [ <Expression e> ]` : {
			if (debug) println("CHECKER: SubscriptAssignable: <pat>");
			RType t = checkSubscriptAssignable(a,al,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Field Access
		case `<Assignable al> . <Name n>` : {
			if (debug) println("CHECKER: FieldAccessAssignable: <pat>");
			RType t = checkFieldAccessAssignable(a,al,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// If Defined or Default
		case `<Assignable al> ? <Expression e>` : {
			if (debug) println("CHECKER: IfDefinedOrDefaultAssignable: <pat>");
			RType t = checkIfDefinedOrDefaultAssignable(a,al,e);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Annotation
		case `<Assignable al> @ <Name n>` : {
			if (debug) println("CHECKER: AnnotationAssignable: <pat>");
			RType t = checkAnnotationAssignable(a,al,n);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Tuple
		case `< <{Assignable ","}+ al> >` : {
			if (debug) println("CHECKER: TupleAssignable: <pat>");
			RType t = checkTupleAssignable(a,al);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
		
		// Constructor
		case `<Name n> ( <{Assignable ","}+ al> )` : {
			if (debug) println("CHECKER: ConstructorAssignable: <pat>");
			RType t = checkConstructorAssignable(a,n,al);
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(t));
			return t;
		}
	}
}

//
// Handle local variable declarations, with or without initializers
//
// TODO: Need to check type of e to ensure assignment is correct!
//
public RType checkLocalVarItems(Type t, {Variable ","}+ vs) {
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				return convertType(t);
			}
				
			case `<Name n> = <Expression e>` : {
				return convertType(t);
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
			if (debug) println("CHECKER: Catch: <c>");
			if (debug) println("CHECKER: Assigning type: " + prettyPrintType(b@rtype));
			return b@rtype;
		}
		
		// TODO: Pull out into own function for consistency
		case `catch <Pattern p> : <Statement b>` : {
			if (debug) println("CHECKER: Catch: <c>");
			
			if (checkForFail({ p@rtype, b@rtype }))
				return collapseFailTypes({ p@rtype, b@rtype });
			else
				return b@rtype;
		}
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
	ScopeInfo si = buildNamespace(t);
	Tree td = decorateNames(t,si);
	Tree tc = check(td);
	return tc;
}
