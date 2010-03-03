module org::rascalmpl::checker::Check

import IO;
import List;

import org::rascalmpl::checker::Types;
import org::rascalmpl::checker::SubTypes;

import languages::rascal::syntax::Rascal;

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
 
public Tree check(Tree t) {
	return visit(t) {
		case Expression e => e[@rtype = checkExpression(e)]
	}
}

private bool debug = true;

private RType propagateFailOr(RType checkType, RType newType) {
	if (isFailType(checkType))
		return checkType;
	else
		return newType;
}

// Should we merge these somehow?
private RType propagateFailOr(RType checkType, RType checkType2, RType newType) {
	if (isFailType(checkType))
		return checkType;
	else if (isFailType(checkType2))
		return checkType2;
	else
		return newType;
}

private RType propagateFailOr(RType checkType, RType checkType2, RType checkType3, RType newType) {
	if (isFailType(checkType))
		return checkType;
	else if (isFailType(checkType2))
		return checkType2;
	else if (isFailType(checkType3))
		return checkType3;
	else
		return newType;
}

private RType checkNegativeExpression(Expression ep, Expression e) {
	if (isIntType(e@rtype)) {
		return makeIntType();
	} else if (isRealType(e@rtype)) {
		return makeRealType();
	} else {
		return propagateFailOr(e@rtype,makeFailType("Error in negation operation: <e> should have a numeric type " + 
			"but instead has type " + prettyPrintType(e@rtype),ep@\loc));
	}
}

private RType checkNegationExpression(Expression ep, Expression e) {
	if (isBoolType(e@rtype)) {
		return makeBoolType();
	} else {
		return propagateFailOr(e@rtype,makeFailType("Error in negation operation: <e> should have type " + 
			prettyPrintType(makeBoolType()) + " but instead has type " + prettyPrintType(e@rtype),ep@\loc));
	}
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in sum operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in difference operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkTimesExpression(Expression ep, Expression el, Expression er) {
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in product operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in division operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in mod operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in less than operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in less than or equal to operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in greater than operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in greater than or equal to operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in equals operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in not equals operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkImplicationExpression(Expression ep, Expression el, Expression er) {
	if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else {
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in implication operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkEquivalenceExpression(Expression ep, Expression el, Expression er) {
	if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else {
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in equivalence operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkAndExpression(Expression ep, Expression el, Expression er) {
	if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else {
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in and operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkOrExpression(Expression ep, Expression el, Expression er) {
	if (isBoolType(el@rtype) && isBoolType(er@rtype)) {
		return makeBoolType();
	} else {
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in or operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkSetExpression(Expression ep, {Expression ","}* es) {
	// TODO: Properly account for subtypes and failures
	// TODO: Probably want to copy the rtype (see if we need to, maybe useful if we think we will assign annotations to the types)
	list[Expression] l = [ e | e <- es];
	return RTypeStructured(RStructuredType(RSetType(), [ RTypeArg( size(l) > 0 ? getOneFrom(l)@rtype : RTypeBasic(RVoidType) ) ]));
}

public RType checkListExpression(Expression ep, {Expression ","}* es) {
	// TODO: Properly account for subtypes and failures
	// TODO: Probably want to copy the rtype (see if we need to, maybe useful if we think we will assign annotations to the types)
	list[Expression] l = [ e | e <- es ];
	return RTypeStructured(RStructuredType(RListType(), [ RTypeArg( size(l) > 0 ? getOneFrom(l)@rtype : RTypeBasic(RVoidType()) ) ]));
}

public RType checkTupleExpression(Expression ep, Expression ei, {Expression ","}* es) {
	// TODO: Properly account for subtypes and failures
	// TODO: Probably want to copy the rtype (see if we need to, maybe useful if we think we will assign annotations to the types)
	list[Expression] l = [ei];
	l += [ e | e <- es ];
	return RTypeStructured(RStructuredType(RTupleType(), [ RTypeArg(e@rtype) | e <- l]));
}

public RType checkRangeExpression(Expression ep, Expression e1, Expression e2) {
	if (isIntType(e1@rtype) && isIntType(e2@rtype)) {
		return RTypeStructured(RStructuredType(RListType(), [ RTypeArg(RTypeBasic(RIntType())) ]));
	} else {
		return propagateFailOr(e1@rtype,e2@rtype,makeFailType("Error in range operation: operation is not defined on the types " +
			prettyPrintType(e1@rtype) + " and " + prettyPrintType(e2@rtype),ep@\loc));
	}
}

public RType checkIsDefinedExpression(Expression ep, Expression e) {
	if (isFailType(e@rtype)) {
		return e@rtype;
	} else {
		return makeBoolType();
	}
}

public RType checkStepRangeExpression(Expression ep, Expression e1, Expression e2, Expression e3) {
	if (isIntType(e1@rtype) && isIntType(e2@rtype) && isIntType(e3@rtype)) {
		return RTypeStructured(RStructuredType(RListType(), [ RTypeArg(RTypeBasic(RIntType())) ]));
	} else {
		return propagateFailOr(e1@rtype,e2@rtype,e3@rtype,makeFailType("Error in step range operation: operation is not defined on the types " +
			prettyPrintType(e1@rtype) + ", " + prettyPrintType(e2@rtype) + " and " + prettyPrintType(e3@rtype),ep@\loc));
	}
}

public RType checkInExpression(Expression ep, Expression el, Expression er) {
	if (isSetType(er@rtype) && getSetElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else if (isListType(er@rtype) && getListElementType(er@rtype) == el@rtype) {
		return makeBoolType();
	} else {
		// TODO: Handle Map type, see what is needed for boolean operations
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in in operation: operation is not defined on the types " +
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
		return propagateFailOr(el@rtype,er@rtype,makeFailType("Error in notin operation: operation is not defined on the types " +
			prettyPrintType(el@rtype) + " and " + prettyPrintType(er@rtype),ep@\loc));
	}
}

public RType checkCallOrTreeExpression(Expression ep, Expression ec, {Expression ","}* es) {
	RType resultType = makeFailType("We assume bad, bad things!",ep@\loc);
	
	// TODO: Check for failure types for ec and es here; if we have failures, all
	// the following logic is pointless anyway.
	
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
	for (a <- alternatives) {
		list[Expression] args = [ e | e <- es ];
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
			for (e <- args) {
				RType argType = head(argTypes); argTypes = tail(argTypes);
				if (argType != e@rtype) {
					if (!isFailType(potentialResultType)) {
						potentialResultType = makeFailType("Bad function invocation, argument type mismatch",ep@\loc); // TODO: Improve error message
					}
				}
			}			
		} else {
			potentialResultType = makeFailType("Arity mismatch", ep@\loc); // TODO: Improve error message
		}
		
		if (isFailType(resultType)) resultType = potentialResultType;
	}
		
	return resultType;	
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

public RType checkFieldUpdateExpression(Expression ep, Expression el, Name n, Expression er) {
	return makeVoidType();
}

public RType checkFieldProjectExpression(Expression ep, Expression e1, {Field ","}+ fl) {
	return makeVoidType();
}

public RType checkExpression(Expression exp) {
	switch(exp) {
		case (Expression)`<BooleanLiteral bl>` : {
			if (debug) println("BooleanLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RBoolType())));
			return makeBoolType();
		}

		case (Expression)`<DecimalIntegerLiteral il>`  : {
			if (debug) println("DecimalIntegerLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<OctalIntegerLiteral il>`  : {
			if (debug) println("OctalIntegerLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<HexIntegerLiteral il>`  : {
			if (debug) println("HexIntegerLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RIntType())));
			return makeIntType();
		}

		case (Expression)`<RealLiteral rl>`  : {
			if (debug) println("RealLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RRealType())));
			return makeRealType();
		}

		case (Expression)`<StringLiteral sl>`  : {
			if (debug) println("StringLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RStrType())));
			return makeStrType();
		}

		case (Expression)`<LocationLiteral ll>`  : {
			if (debug) println("LocationLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RLocType())));
			return RTypeBasic(RLocType());
		}

		case (Expression)`<DateTimeLiteral dtl>`  : {
			if (debug) println("DateTimeLiteral: <exp>");
			if (debug) println("Assigning type: " + prettyPrintType(RTypeBasic(RDateTimeType())));
			return RTypeBasic(RDateTimeType());
		}

		// Name
		case (Expression)`<Name n>`: {
			if (debug) println("Name: <exp>");
			if (debug) println("Assigned type: " + prettyPrintType(n@rtype));
			return n@rtype; // TODO: Should throw an exception if the name has no type information
		}
		
		// QualifiedName
		case (Expression)`<QualifiedName qn>`: {
			if (debug) println("QualifiedName: <exp>");
			if (debug) println("Assigned type: " + prettyPrintType(qn@rtype));
			return qn@rtype; // TODO: Should throw an exception if the name has no type information
		}

		// ReifiedType
		case `<BasicType t> ( <{Expression ","}* el> )` : {
			if (debug) println("ReifiedType: <exp>");
			RType rt = checkReifiedType(exp,t,el);
			if (debug) println("Assigned type: " + prettyPrintType(rt));
			return rt;
		}

		// CallOrTree
		case `<Expression e1> ( <{Expression ","}* el> )` : {
			if (debug) println("Call or Tree: <e1>(<el>)");
			RType t = checkCallOrTreeExpression(exp,e1,el);
			if(debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// List
		case `[<{Expression ","}* el>]` : {
			if (debug) println("List: <exp>");
			RType t = checkListExpression(exp,el);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Set
		case `{<{Expression ","}* el>}` : {
			if (debug) println("Set: <exp>");
			RType t = checkSetExpression(exp,el);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Tuple
		case `<<Expression ei>, <{Expression ","}* el>>` : {
			if (debug) println("Tuple <exp>");
			RType t = checkTupleExpression(exp,ei,el);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TODO: Map: Need to figure out a syntax that works for matching this
//		case `<<Expression ei>, <{Expression ","}* el>>` : {
//			// TODO: This is not yet working
//			if (debug) println("Tuple <exp>");
//			RType t = checkTupleExpression(exp,ei,el);
//			if (debug) println("Assigning type: " + prettyPrintType(t));
//			return t;
//		}

		// Closure
		case `<Type t> <Parameters p> { <Statement+ ss> }` : {
			if (debug) println("Closure: <exp>");
			RType t = checkClosureExpression(exp,t,p,ss);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// VoidClosure
		case `<Parameters p> { <Statement* ss> }` : {
			if (debug) println("VoidClosure: <exp>");
			RType t = checkVoidClosureExpression(exp,p,ss);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NonEmptyBlock
		case `{ <Statement+ ss> }` : {
			if (debug) println("NonEmptyBlock: <exp>");
			RType t = checkNonEmptyBlockExpression(exp,ss);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;		
		}
		
		// Visit
		case (Expression) `<Label l> <Visit v>` : {
			if (debug) println("Visit: <exp>");
			RType t = checkVisitExpression(exp,l,v);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;			
		}
		
		// ParenExp
		case `(<Expression e>)` : {
			if (debug) println("ParenExp: <exp>");
			RType t = e@rtype;
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Range
		case `[ <Expression e1> .. <Expression e2> ]` : {
			if (debug) println("Range: <exp>");
			RType t = checkRangeExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// StepRange
		case `[ <Expression e1>, <Expression e2> .. <Expression e3> ]` : {
			if (debug) println("StepRange: <exp>");
			RType t = checkStepRangeExpression(exp,e1,e2,e3);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// ReifyType
		case (Expression)`#<Type t>` : {
			if (debug) println("ReifyType: <exp>");
			RType t = RTypeStructured(RStructuredType(RTypeType(),[RTypeArg(convertType(t))]));
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldUpdate
		case `<Expression e1> [<Name n> = <Expression e2>]` : {
			if (debug) println("FieldUpdate: <exp>");
			RType t = checkFieldUpdateExpression(exp,e1,n,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldAccess
		case `<Expression e1> . <Name n>` : {
			if (debug) println("FieldAccess: <exp>");
			RType t = checkFieldAccessExpression(exp,e1,n);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// FieldProject
		case `<Expression e1> < <{Field ","}+ fl> >` : {
			if (debug) println("FieldProject: <exp>");
			RType t = checkFieldProjectExpression(exp,e1,fl);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TODO: Subscript (currently broken)
//		case `<Expression e1> [ <{Expression ","}+ el> ]` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression rct = RSubscriptExp(convertExpression(e1),mapper(getSDFExpListItems(el),convertExpression));
//			return rct[@at = exp@\loc];
//		}

		// IsDefined
		case `<Expression e> ?` : {
			if (debug) println("IsDefined: <exp>");
			RType t = checkIsDefinedExpression(exp,e);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Negation
		case `! <Expression e>` : {
			if (debug) println("Negation: <exp>");
			RType t = checkNegationExpression(exp,e);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Negative
		case `- <Expression e> ` : {
			if (debug) println("Negative: <exp>");
			RType t = checkNegativeExpression(exp,e);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// TransitiveClosure
//		case `<Expression e> + ` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RTransitiveClosureExp(convertExpression(e));
//			return re[@at = exp@\loc] ;
//		}

		// TransitiveReflexiveClosure
//		case `<Expression e> * ` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RTransitiveReflexiveClosureExp(convertExpression(e));
//			return re[@at = exp@\loc] ;
//		}

		// GetAnnotation
//		case `<Expression e> @ <Name n>` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RGetAnnotationExp(convertExpression(e),convertName(n));
//			return re[@at = exp@\loc] ;
//		}

		// SetAnnotation
//		case `<Expression e1> [@ <Name n> = <Expression e2>]` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RSetAnnotationExp(convertExpression(e1),convertName(n),convertExpression(e2));
//			return re[@at = exp@\loc] ;
//		}

		// Composition
//		case `<Expression e1> o <Expression e2>` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RCompositionExp(convertExpression(e1),convertExpression(e2));
//			return re[@at = exp@\loc] ;
//		}

		// Join
//		case `<Expression e1> join <Expression e2>` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RJoinExp(convertExpression(e1),convertExpression(e2));
//			return re[@at = exp@\loc] ;
//		}

		// Times
		case `<Expression e1> * <Expression e2>` : {
			if (debug) println("Times: <exp>");
			RType t = checkTimesExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Plus
		case `<Expression e1> + <Expression e2>` : {
			if (debug) println("Plus: <exp>");
			RType t = checkPlusExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Minus
		case `<Expression e1> - <Expression e2>` : {
			if (debug) println("Minus: <exp>");
			RType t = checkMinusExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Div
		case `<Expression e1> / <Expression e2>` : {
			if (debug) println("Div: <exp>");
			RType t = checkDivExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Mod
		case `<Expression e1> % <Expression e2>` : {
			if (debug) println("Mod: <exp>");
			RType t = checkModExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// In
		case `<Expression e1> in <Expression e2>` : {
			if (debug) println("In: <exp>");
			RType t = checkInExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NotIn
		case `<Expression e1> notin <Expression e2>` : {
			if (debug) println("NotIn: <exp>");
			RType t = checkNotInExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// LessThan
		case `<Expression e1> < <Expression e2>` : {
			if (debug) println("LessThan: <exp>");
			RType t = checkLessThanExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// LessThanOrEq
		case `<Expression e1> <= <Expression e2>` : {
			if (debug) println("LessThanOrEq: <exp>");
			RType t = checkLessThanOrEqualExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// GreaterThanOrEq
		case `<Expression e1> >= <Expression e2>` : {
			if (debug) println("GreaterThanOrEq: <exp>");
			RType t = checkGreaterThanOrEqualExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// GreaterThan
		case `<Expression e1> > <Expression e2>` : {
			if (debug) println("GreaterThan: <exp>");
			RType t = checkGreaterThanExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Equals
		case `<Expression e1> == <Expression e2>` : {
			if (debug) println("Equals: <exp>");
			RType t = checkEqualsExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// NotEquals
		case `<Expression e1> != <Expression e2>` : {
			if (debug) println("NotEquals: <exp>");
			RType t = checkNotEqualsExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// IfDefinedOtherwise
//		case `<Expression e1> ? <Expression e2>` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RIfDefExp(convertExpression(e1),convertExpression(e2));
//			return re[@at = exp@\loc] ;
//		}

		// IfThenElse (Ternary)
//		case `<Expression e1> ? <Expression e2> : <Expression e3>` : {
//			if (debug) println("DateTimeLiteral: <dtl>");
//			RExpression re = RTernaryExp(convertExpression(e1),convertExpression(e2),convertExpression(e3));
//			return re[@at = exp@\loc] ;
//		}

		// Implication
		case `<Expression e1> ==> <Expression e2>` : {
			if (debug) println("Implication: <exp>");
			RType t = checkImplicationExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Equivalence
		case `<Expression e1> <==> <Expression e2>` : {
			if (debug) println("Equivalence: <exp>");
			RType t = checkEquivalenceExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// And
		case `<Expression e1> && <Expression e2>` : {
			if (debug) println("And: <exp>");
			RType t = checkAndExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

		// Or
		case `<Expression e1> || <Expression e2>` : {
			if (debug) println("Or: <exp>");
			RType t = checkOrExpression(exp,e1,e2);
			if (debug) println("Assigning type: " + prettyPrintType(t));
			return t;
		}

	}
}

