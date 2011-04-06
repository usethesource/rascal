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
module lang::rascal::checker::TypeRules

import List;
import Set;
import IO;

import lang::rascal::checker::ListUtils;
import lang::rascal::types::Types;
import lang::rascal::types::SubTypes;
import lang::rascal::scoping::SymbolTable;

import lang::rascal::syntax::RascalRascal;

data RAssignmentOp = RADefault() | RAAddition() | RASubtraction() | RAProduct() | RADivision() | RAIntersection() | RAIfDefined() ;

public RAssignmentOp convertAssignmentOp(Assignment a) {
	switch(a) {
		case (Assignment)`=` : return RADefault();
		case (Assignment)`+=` : return RAAddition();
		case (Assignment)`-=` : return RASubtraction();
		case (Assignment)`*=` : return RAProduct();
		case (Assignment)`/=` : return RADivision();
		case (Assignment)`&=` : return RAIntersection();
		case (Assignment)`?=` : return RAIfDefined();
	}
}

public str prettyPrintAOp(RAssignmentOp a) {
	switch(a) {
		case RADefault() : return "=";
		case RAAddition() : return "+=";
		case RASubtraction() : return "-=";
		case RAProduct() : return "*=";
		case RADivision() : return "/=";
		case RAIntersection() : return "&=";
		case RAIfDefined() : return "?=";
	}
}

public bool aOpHasOp(RAssignmentOp a) {
	switch(a) {
		case RADefault() : return false;
		case RAIfDefined() : return false;
		default : return true;
	}
}

public ROp opForAOp(RAssignmentOp a) {
	switch(a) {
		case RAAddition() : return RPlus();
		case RASubtraction() : return RMinus();
		case RAProduct() : return RProduct();
		case RADivision() : return RDiv();
		case RAIntersection() : return RInter();
	}
}

data ROp = RProduct() | RDiv() | RMod() | RPlus() | RMinus() | RNotIn() | RIn() | RLt() | RLtEq() | RGt() | RGtEq() | REq() | RNEq() | RInter() | RJoin();

private map[ROp,str] opStr = ( RProduct() : "*", RDiv() : "/", RMod() : "%", RPlus() : "+", RMinus() : "-", RNotIn() : "notin" , RIn() : "in",
			       RLt() : "\<", RLtEq() : "\<=", RGt() : "\>", RGtEq() : "\>=", REq() : "=", RNEq() : "!=", RInter() : "&", RJoin() : "join"); 

public str prettyPrintOp(ROp ro) {
	if (ro in opStr) return opStr[ro];
	throw "Operator <ro> is not in the operator/string map";
}

private set[ROp] relationalOps = { RLt(), RLtEq(), RGt(), RGtEq(), REq(), RNEq() };
private set[ROp] arithOpsWithMod = { RProduct(), RDiv(), RMod(), RPlus(), RMinus() };	
private set[ROp] arithOps = { RProduct(), RDiv(), RPlus(), RMinus() };	

// TODO: For documentation purposes, I'm not sure if it makes the most sense to organize this
// by types (the way it is now) or by operators. Should decide which is easier to follow.
//
// NOTE: There are no cases for types void, lex, type, adt, non-terminal, reified. This is not an oversight.
//
// TODO: See if we need RContainerType rules...
//
public RType expressionType(RType lType, RType rType, ROp rop, loc l) {
	RType notSupported() {
	        return makeFailType("Operation <prettyPrintOp(rop)> not supported on <prettyPrintType(lType)> and <prettyPrintType(rType)>",l);
	}

	// First, manually unroll aliases until we get to an actual type -- which could still
	// have aliases inside (for instance, in a function's parameters), but that's not a problem
	if (RAliasType(_,at) := lType) return expressionType(at, rType, rop, l);
	if (RAliasType(_,at) := rType) return expressionType(lType, at, rop, l);

	// Second, treat any type variables as their bounds -- we need this in cases where
	// type variables are declared in the scope, since we will actually then get situations
	// like &T <: int x, then later x + y, and the type of x wil be &T <: int.
	if (RTypeVar(_) := lType) return expressionType(getTypeVarBound(lType), rType, rop, l);
	if (RTypeVar(_) := rType) return expressionType(lType, getTypeVarBound(rType), rop, l);

	// Third, we key off the type of the first argument, then the operator, then the second
	// argument. This is the easiest way to tie the results back to the interpreter. First,
	// handle the scalar types. We don't have a case for void, since void can't be used in
	// binary operations anyway.
	if (RBoolType() := lType) {
	        if (rop in relationalOps && RBoolType() := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RIntType() := lType) {
	        if (rop in arithOpsWithMod && RIntType() := rType) return makeIntType();
		if (rop in arithOps && RRealType() := rType) return makeRealType();
		if (rop in arithOps && RNumType() := rType) return makeNumType();
		
		if (rop in relationalOps && RIntType() := rType) return makeBoolType();
		if (rop in relationalOps && RRealType() := rType) return makeBoolType();
		if (rop in relationalOps && RNumType() := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RRealType() := lType) {
	        if (rop in arithOps && RIntType() := rType) return makeRealType();
		if (rop in arithOps && RRealType() := rType) return makeRealType();
		if (rop in arithOps && RNumType() := rType) return makeNumType();
		
		if (rop in relationalOps && RIntType() := rType) return makeBoolType();
		if (rop in relationalOps && RRealType() := rType) return makeBoolType();
		if (rop in relationalOps && RNumType() := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RNumType() := lType) {
	        if (rop in arithOps && RIntType() := rType) return makeNumType();
		if (rop in arithOps && RRealType() := rType) return makeNumType();
		if (rop in arithOps && RNumType() := rType) return makeNumType();
		
		if (rop in relationalOps && RIntType() := rType) return makeBoolType();
		if (rop in relationalOps && RRealType() := rType) return makeBoolType();
		if (rop in relationalOps && RNumType() := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RStrType() := lType) {
	        if (RPlus() := rop && RStrType() := rType) return makeStrType();
		if (rop in relationalOps && RStrType() := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RValueType() := lType) {
	        if (rop in { REq(), RNEq() }) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RNodeType() := lType) {
	        if (rop in relationalOps && RConstructorType(_,_,_) := lType) return makeBoolType();
		if (rop in relationalOps && RNodeType() := lType) return makeBoolType();
		if (rop in relationalOps && RADTType(_) := lType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RLocType() := lType) {
	        if (RPlus() := rop && RStrType() := rType) return makeLocType();
		if (rop in { REq(), RNEq() } && RLocType() := rType) return makeLocType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RDateTimeType() := lType) {
	        if (rop in relationalOps && RDateTimeType() := rType) return makeDateTimeType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	// Tuples and ADTs are "hybrid" types -- they contain other data, but are not general containers
	if (RTupleType(_) := lType) {
	        if (RPlus() := rop && RTupleType(_) := rType) {
	                list[RNamedType] tFields1 = getTupleFieldsWithNames(lType);
	                list[RNamedType] tFields2 = getTupleFieldsWithNames(rType);
			if (tupleHasFieldNames(lType) && tupleHasFieldNames(rType)) {
			        // Need to check for consistency to see if we should use the field names
			        if (size({ nm | nm <- (getTupleFieldNames(lType) + getTupleFieldNames(rType)) }) != size(tFields1 + tFields2)) {
				        // If we don't have the same number of field names as we have fields, we must have some duplicates,
					// so we strip the field names off.
					return makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]);
				} else {
				        return makeTupleTypeWithNames(tFields1 + tFields2);
				}
			} else {
		                return makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]);
			}
		}

		if (rop in relationalOps && RTupleType(_) := rType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}
	
	if (RADTType(_) := lType) {
	        if (rop in relationalOps && RConstructorType(_,_,_) := lType) return makeBoolType();
		if (rop in relationalOps && RNodeType() := lType) return makeBoolType();
		if (rop in relationalOps && RADTType(_) := lType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	// Now functions and constructors
	if (RFunctionType(_,_) := lType) {
	        if (REq() := rop && RFunctionType(_,_) := rType) return makeBoolType();
	}

	if (RConstructorType(_,_,_) := lType) {
	        if (rop in relationalOps && RConstructorType(_,_,_) := lType) return makeBoolType();
		if (rop in relationalOps && RNodeType() := lType) return makeBoolType();
		if (rop in relationalOps && RADTType(_) := lType) return makeBoolType();

		if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(lType,getListElementType(rType)));
		if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(lType, getSetElementType(rType)));
		if (RPlus() := rop && RBagType(_) := rType) return makeBagType(lub(lType, getBagElementType(rType)));

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	// Now containers
	if (RListType(_) := lType) {
	        if (RPlus() := rop && RListType(_) := rType) return makeListType(lub(getListElementType(lType),getListElementType(rType)));
		//if (RPlus() := rop && RContainerType(_) := rType) return makeListType(lub(getListElementType(lType),getContainerElementType(rType)));
		if (RPlus() := rop) return makeListType(lub(getListElementType(lType), rType));

	        if (RMinus() := rop && RListType(_) := rType) return makeListType(getListElementType(lType));
		//if (RMinus() := rop && RContainerType(_) := rType) return makeListType(getListElementType(lType));
		if (RMinus() := rop) return makeListType(getListElementType(lType));

	        if (RProduct() := rop && RListType(_) := rType) return makeListType(makeTupleType([getListElementType(lType),getListElementType(rType)]));
		//if (RProduct() := rop && RContainerType(_) := rType) return makeListType(makeTupleType([getListElementType(lType),getContainerElementType(rType)]));
		if (RProduct() := rop) return makeListType(makeTupleType([getListElementType(lType),rType]));

		if (rop in relationalOps && RListType(_) := rType) return makeBoolType();
		//if (rop in relationalOps && RContainerType(_) := rType) return makeBoolType();

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	if (RRelType(_) := lType) {
		if (RPlus() := rop && RRelType(_) := rType) {
		        if (comparable(getRelElementType(lType),getRelElementType(rType)))
			        return makeRelTypeFromTuple(lub(getRelElementType(lType),getRelElementType(rType)));
			else
				return makeSetType(lub(getRelElementType(lType),getRelElementType(rType)));
		}
		if (RPlus() := rop && RTupleType(_) := rType) {
		        if (comparable(getRelElementType(lType),rType))
			        return makeRelTypeFromTuple(lub(getRelElementType(lType),rType));
			else
				return makeSetType(lub(getRelElementType(lType),rType));

		}
	        if (RPlus() := rop && RSetType(RTupleType(_)) := rType) {
		        if (comparable(getRelElementType(lType),getSetElementType(rType)))
			        return makeRelTypeFromTuple(lub(getRelElementType(lType),getSetElementType(rType)));
			else
				return makeSetType(lub(getRelElementType(lType),getSetElementType(rType)));
		}
	        if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(getRelElementType(lType),getSetElementType(rType)));
		//if (RPlus() := rop && RContainerType(_) := rType) return makeSetType(lub(getRelElementType(lType),getContainerElementType(rType)));

	        if (RMinus() := rop && RRelType(_) := rType && comparable(getRelElementType(lType),getRelElementType(rType))) 
		        return makeRelType(getRelElementType(lType));
		if (RMinus() := rop && RTupleType(_) := rType && comparable(getRelElementType(lType),rType)) 
		        return makeRelType(getRelElementType(lType));
	        if (RMinus() := rop && RSetType(RTupleType(_)) := rType && comparable(getRelElementType(lType),getSetElementType(rType))) 
		        return makeRelType(getRelElementType(lType));

 		if (RProduct() := rop && RRelType(_) := rType) 
		        return makeRelTypeFromTuple(makeTupleType([getRelElementType(lType),getRelElementType(rType)]));
	        if (RProduct() := rop && RSetType(_) := rType) 
		        return makeRelTypeFromTuple(makeTupleType([getRelElementType(lType),getSetElementType(rType)]));
 		//if (RProduct() := rop && RContainerType(_) := rType) 
		 //       return makeRelTypeFromTuple(makeTupleType([getRelElementType(lType),getContainerElementType(rType)]));

		if (RInter() := rop && RRelType(_) := rType)
		        return makeRelTypeFromTuple(lub(getRelElementType(lType),getRelElementType(rType)));
		if (RInter() := rop && RSetType(_) := rType)
		        return makeRelTypeFromTuple(lub(getRelElementType(lType),getSetElementType(rType)));

	        if (RJoin() := rop && (RRelType(_) := rType || RSetType(RTupleType(_)) := rType)) {
		        RType lTuple = getRelElementTypes(lType);
			RType rTuple = getRelElementTypes(rTypes);
	                list[RNamedType] tFields1 = getTupleFieldsWithNames(lTuple);
	                list[RNamedType] tFields2 = getTupleFieldsWithNames(rTuple);
			if (tupleHasFieldNames(lTuple) && tupleHasFieldNames(rTuple)) {
			        // Need to check for consistency to see if we should use the field names
			        if (size({ nm | nm <- (getTupleFieldNames(lTuple)+getTupleFieldNames(rTuple)) }) != size(tFields1 + tFields2)) {
				        // If we don't have the same number of field names as we have fields, we must have some duplicates,
					// so we strip the field names off.
					return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
				} else {
				        return makeRelTypeFromTuple(makeTupleTypeWithNames(tFields1 + tFields2));
				}
			} else {
		                return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(ti)) | ti <- (tFields1 + tFields2)]));
			}
		}

		if (RJoin() := rop && RSetType(_) := rType) {
		        RType lTuple = getRelElementTypes(lType);
	                list[Type] tFields1 = [ getElementType(ti) | ti <- getTupleFieldsWithNames(lTuple)];
			return makeRelTypeFromTuple(makeTupleType(tFields1+getSetElementType(rType)));
		}

		if (rop in relationalOps && RSetType(_) := rType) return makeBoolType();
		//if (rop in relationalOps && RContainerType(_) := rType) return makeBoolType();
		if (rop in relationalOps && RRelType(_) := rType) return makeBoolType();

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();

	}

	if (RSetType(_) := lType) {
	        if (RPlus() := rop && RSetType(_) := rType) return makeSetType(lub(getSetElementType(lType),getSetElementType(rType)));
		//if (RPlus() := rop && RContainerType(_) := rType) return makeSetType(lub(getSetElementType(lType),getContainerElementType(rType)));
		if (RPlus() := rop && RRelType(_) := rType) return makeSetType(lub(getSetElementType(lType),getRelElementType(rType)));
		if (RPlus() := rop) return makeSetType(lub(getSetElementType(lType), rType));

	        if (RMinus() := rop && RSetType(_) := rType) return makeSetType(getSetElementType(lType));
		//if (RMinus() := rop && RContainerType(_) := rType) return makeSetType(getSetElementType(lType));
		if (RMinus() := rop) return makeSetType(getSetElementType(lType));

	        if (RProduct() := rop && RSetType(_) := rType) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),getSetElementType(rType)]));
 		//if (RProduct() := rop && RContainerType(_) := rType) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),getContainerElementType(rType)]));
 		if (RProduct() := rop && RRelType(_) := rType) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),getRelElementType(rType)]));
		if (RProduct() := rop) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),rType]));

		if (RInter() := rop && RSetType(_) := rType && comparable(getSetElementType(lType),getSetElementType(rType))) return makeSetType(getSetElementType(lType));

		if (RJoin() := rop && RSetType(_) := rType) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),getSetElementType(rType)]));
		if (RJoin() := rop && RRelType(_) := rType) return makeRelTypeFromTuple(makeTupleType([getSetElementType(lType),getRelElementType(rType)]));

		if (rop in relationalOps && RSetType(_) := rType) return makeBoolType();
		//if (rop in relationalOps && RContainerType(_) := rType) return makeBoolType();
		if (rop in relationalOps && RRelType(_) := rType) return makeBoolType();

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();

	}

	if (RMapType(_,_) := lType) {
	        if (RPlus() := rop && RMapType(_,_) := rType)
		        return makeMapTypeFromTuple(lub(getMapFieldsAsTuple(lType),getMapFieldsAsTuple(rType)));

		if (RMinus() := rop && RMapType(_,_) := rType && comparable(lType,rType)) 
		        return makeMapTypeFromTuple(getMapFieldsAsTuple(lType));

		if (RInter() := rop && RMapType(_,_) := rType && comparable(lType,rType)) 
		        return makeMapTypeFromTuple(getMapFieldsAsTuple(lType));

		if (rop in relationalOps && RMapType(_,_) := rType) return makeBoolType();

		if (rop in { RIn(), RNotIn() } && RListType(_) := rType && subtypeOf(lType,getListElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RSetType(_) := rType && subtypeOf(lType,getSetElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RBagType(_) := rType && subtypeOf(lType,getBagElementType(rType))) return makeBoolType();
		if (rop in { RIn(), RNotIn() } && RMapType(_,_) := rType && subtypeOf(lType,getMapDomainType(rType))) return makeBoolType();
	}

	return notSupported();
}

// TODO: Guessing at type of children in loc, not implemented yet
private map[RType,map[str,RType]] fieldMap =
	( RLocType() :
		( "scheme" : RStrType(), "authority" : RStrType(), "host" : RStrType(), "path" : RStrType(), "parent" : RStrType(),
                  "file" : RStrType(), "children" : makeListType(makeLocType()), "extension" : RStrType(), 
                  "fragment" : RStrType(), "query" : RStrType(), "user" : RStrType(), "port" : RIntType(), "length" : RIntType(), 
                  "offset" : RIntType(), "begin" : makeTupleType([RIntType(),RIntType()]),
		  "end" : makeTupleType([RIntType(),RIntType()]), "uri" : RStrType()),
	  RDateTimeType() :
		( "year" : RIntType(), "month" : RIntType(), "day" : RIntType(), "hour" : RIntType(), "minute" : RIntType(), "second" : RIntType(),
                  "millisecond" : RIntType(), "timezoneOffsetHours" : RIntType(), "timezoneOffsetMinutes" : RIntType(), "century" : RIntType(),
		  "isDate" : RBoolType(), "isTime" : RBoolType(), "isDateTime" : RBoolType(), "justDate" : RDateTimeType(), "justTime" : RDateTimeType())
	);

public RType typeForField(RType source, str fieldName) {
	if (source in fieldMap) {
		if (fieldName in fieldMap[source])
			return fieldMap[source][fieldName];
	}
	throw "Invalid looking: field <fieldName> for type <prettyPrintType(source)> not in field type map.";
}

public bool dateTimeHasField(RName fieldName) {
	str fn = prettyPrintName(fieldName);
	return (fn in fieldMap[RDateTimeType()]);
}

public bool locHasField(RName fieldName) {
	str fn = prettyPrintName(fieldName);
	return (fn in fieldMap[RLocType()]);
}

public bool typeAllowsFields(RType rt) {
	return (isADTType(rt) || isTupleType(rt) || isRelType(rt) || isLocType(rt) || isDateTimeType(rt) || isMapType(rt));
}

public bool typeHasField(RType rt, RName fn, STBuilder stBuilder) {
	if (isADTType(rt)) return adtHasField(rt, fn, stBuilder);
	if (isTupleType(rt)) return tupleHasField(rt, fn);
	if (isRelType(rt)) return relHasField(rt, fn);
	if (isLocType(rt)) return locHasField(fn);
	if (isDateTimeType(rt)) return dateTimeHasField(fn);
	if (isMapType(rt)) return mapHasField(rt, fn);

	throw "Type <prettyPrintType(rt)> does not allow fields.";
}

public RType getFieldType(RType rt, RName fn, STBuilder stBuilder, loc l) {
	if (isADTType(rt) && typeHasField(rt,fn,stBuilder)) return getADTFieldType(rt, fn, stBuilder);
	if (isADTType(rt)) return makeFailType("ADT <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

	if (isTupleType(rt) && typeHasField(rt,fn,stBuilder)) return getTupleFieldType(rt, fn);
	if (isTupleType(rt)) return makeFailType("Tuple <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

	if (isRelType(rt) && typeHasField(rt,fn,stBuilder)) return getRelFieldType(rt, fn);
	if (isRelType(rt)) return makeFailType("Relation <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

	if (isMapType(rt) && typeHasField(rt,fn,stBuilder)) return getMapFieldType(rt, fn);
	if (isMapType(rt)) return makeFailType("Map <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

	if (isLocType(rt) && typeHasField(rt,fn,stBuilder)) return typeForField(rt, prettyPrintName(fn));
	if (isLocType(rt)) return makeFailType("Location <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);

	if (isDateTimeType(rt) && typeHasField(rt,fn,stBuilder)) return typeForField(rt, prettyPrintName(fn));
	if (isDateTimeType(rt)) return makeFailType("DateTime <prettyPrintType(rt)> does not define field <prettyPrintName(fn)>", l);
	
	return makeFailType("Type <prettyType(rt)> does not have fields", l);
}

@doc{Check to see if a relation defines a field.}
public bool relHasField(RType t, RName fn) {
        if (isRelType(t)) {
	        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
		for (ta <- tas) {
			if (RNamedType(_,fn) := ta) return true;	
		}
		return false;
	}
	throw "Cannot check for relation field on type <prettyPrintType(t)>";	
}

@doc{Return the type of a field defined on a relation.}
public RType getRelFieldType(RType t, RName fn) {
        if (isRelType(t)) {
	        list[RNamedType] tas = getTupleFieldsWithNames(getRelElementType(t));
		for (ta <- tas) {
			if (RNamedType(ft,fn) := ta) return ft;	
		}
	        throw "Relation <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
	}
	throw "Cannot get relation field type from type <prettyPrintType(t)>";	
}

public list[RType] getRelFields(RType t) {
        if (isRelType(t)) return getTupleFields(getRelElementType(t));
	throw "Cannot get relation fields from type <prettyPrintType(t)>";	
}

public list[RNamedType] getRelFieldsWithNames(RType t) {
        if (isRelType(t)) return getTupleFieldsWithNames(getRelElementType(t));
	throw "Cannot get relation fields from type <prettyPrintType(t)>";	
}

@doc{Check to see if an ADT defines a field.}
public bool adtHasField(RType t, RName fn, STBuilder stBuilder) {
       if (isADTType(t)) {
		for (ci <- stBuilder.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := stBuilder.scopeItemMap[ci]) {
			for (ta <- cts) {
				if (RNamedType(_,fn) := ta) return true;
			}	
		}
	        return false;
	}
	throw "adtHasField: given unexpected type <prettyPrintType(t)>";
}

//
// Look up the type of field fn on ADT t. Note that fields have a unique type in a given ADT, even if
// they appear on multiple constructors, so we can always use the first occurrence of the field we
// find on a constructor.
//
@doc{Return the type of a field on an ADT.}
public RType getADTFieldType(RType t, RName fn, STBuilder stBuilder) {
	if (isADTType(t)) {
		for (ci <- stBuilder.adtMap[getADTName(t)].consItems, ConstructorItem(_,cts,_,_) := stBuilder.scopeItemMap[ci]) {
			for (ta <- cts) {
				// See if we have a match on the field name
				if (RNamedType(ft,fn) := ta) {
					return markUserTypes(ft,stBuilder,stBuilder.scopeItemMap[ci].parentId);
				}
			}	
		}
		throw "ADT <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
	}	
	throw "adtHasField: given unexpected type <prettyPrintType(t)>";
}
