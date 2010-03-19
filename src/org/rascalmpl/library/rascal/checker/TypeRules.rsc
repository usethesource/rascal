module rascal::checker::TypeRules

import rascal::checker::SubTypes;
import rascal::checker::Types;
import rascal::checker::ListUtils;

import List;
import Set;
import IO;

import rascal::\old-syntax::Rascal;

data RAssignmentOp = RADefault() | RAAddition() | RASubtraction() | RAProduct() | RADivision() | RAIntersection() | RAIfDefined() ;

public RAssignmentOp convertAssignmentOp(Assignment a) {
	switch(a) {
		case `=` : return RADefault();
		case `+=` : return RAAddition();
		case `-=` : return RASubtraction();
		case `*=` : return RAProduct();
		case `/=` : return RADivision();
		case `&=` : return RAIntersection();
		case `?=` : return RAIfDefined();
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

data ROp = RProduct() | RDiv() | RMod() | RPlus() | RMinus() | RNotIn() | RIn() | RLt() | RLtEq() | RGt() | RGtEq() | REq() | RNEq() | RInter();

private map[ROp,str] opStr = ( RProduct() : "*", RDiv() : "/", RMod() : "%", RPlus() : "+", RMinus() : "-", RNotIn() : "notin" , RIn() : "in",
											   RLt() : "\<", RLtEq() : "\<=", RGt() : "\>", RGtEq() : "\>=", REq() : "=", RNEq() : "!=", RInter() : "&"); 

public str prettyPrintOp(ROp ro) {
	if (ro in opStr) return opStr[ro];
	throw "Operator <ro> is not in the operator/string map";
}

private map[RType,map[str,RType]] fieldMap =
	( RLocType() :
		( "scheme" : RStrType(), "authority" : RStrType(), "host" : RStrType(), "path" : RStrType(), "extension" : RStrType(), "fragment" : RStrType(),
		  "query" : RStrType(), "user" : RStrType(), "port" : RIntType(), "length" : RIntType(), "offset" : RIntType(), "begin" : makeTupleType([RIntType(),RIntType()]),
		  "end" : makeTupleType([RIntType(),RIntType()]), "uri" : RStrType()),
	  RDateTimeType() :
		( "year" : RIntType(), "month" : RIntType(), "day" : RIntType(), "hour" : RIntType(), "minute" : RIntType(), "second" : RIntType(),
          "millisecond" : RIntType(), "timezoneOffsetHours" : RIntType(), "timezoneOffsetMinutes" : RIntType(), "century" : RIntType(),
		  "isDate" : RBoolType(), "isTime" : RBoolType(), "isDateTime" : RBoolType(), "justDate" : RDateTimeType(), "justTime" : RDateTimeType())
	);

public RType typeForField(RType source, str fieldName) {
	if (source in fieldMap)
		if (fieldName in fieldMap[source])
			return fieldMap[source][fieldName];
	throw "Invalid looking: field <fieldName> for type <prettyPrintType(source)> not in field type map.";
}

// TODO: For documentation purposes, I'm not sure if it makes the most sense to organize this
// by types (the way it is now) or by operators. Should decide which is easier to follow.
//
// NOTE: There are no cases for types void, lex, type, adt, non-terminal, reified. This is not an oversight.
public RType expressionType(RType lType, RType rType, ROp rop, loc l) {
	switch(<lType, rop, rType>) {
		// BOOL CASES
		case <RBoolType(), RLt(), RBoolType()> : return RBoolType();
		case <RBoolType(), RLtEq(), RBoolType()> : return RBoolType();
		case <RBoolType(), RGt(), RBoolType()> : return RBoolType();
		case <RBoolType(), RGtEq(), RBoolType()> : return RBoolType();
		case <RBoolType(), REq(), RBoolType()> : return RBoolType();
		case <RBoolType(), RNEq(), RBoolType()> : return RBoolType();

		// INT CASES
		case <RIntType(), RProduct(), RIntType()> : return RIntType();
		case <RIntType(), RDiv(), RIntType()> : return RIntType();
		case <RIntType(), RMod(), RIntType()> : return RIntType();
		case <RIntType(), RPlus(), RIntType()> : return RIntType();
		case <RIntType(), RMinus(), RIntType()> : return RIntType();
		case <RIntType(), RLt(), RIntType()> : return RBoolType();
		case <RIntType(), RLtEq(), RIntType()> : return RBoolType();
		case <RIntType(), RGt(), RIntType()> : return RBoolType();
		case <RIntType(), RGtEq(), RIntType()> : return RBoolType();
		case <RIntType(), REq(), RIntType()> : return RBoolType();
		case <RIntType(), RNEq(), RIntType()> : return RBoolType();

		// REAL CASES
		case <RRealType(), RProduct(), RRealType()> : return RRealType();
		case <RRealType(), RDiv(), RRealType()> : return RRealType();
		case <RRealType(), RPlus(), RRealType()> : return RRealType();
		case <RRealType(), RMinus(), RRealType()> : return RRealType();
		case <RRealType(), RLt(), RRealType()> : return RBoolType();
		case <RRealType(), RLtEq(), RRealType()> : return RBoolType();
		case <RRealType(), RGt(), RRealType()> : return RBoolType();
		case <RRealType(), RGtEq(), RRealType()> : return RBoolType();
		case <RRealType(), REq(), RRealType()> : return RBoolType();
		case <RRealType(), RNEq(), RRealType()> : return RBoolType();

		// MIXED INT/REAL CASES
		case <RIntType(), RProduct(), RRealType()> : return RRealType();
		case <RIntType(), RDiv(), RRealType()> : return RRealType();
		case <RIntType(), RPlus(), RRealType()> : return RRealType();
		case <RIntType(), RMinus(), RRealType()> : return RRealType();
		case <RIntType(), RLt(), RRealType()> : return RBoolType();
		case <RIntType(), RLtEq(), RRealType()> : return RBoolType();
		case <RIntType(), RGt(), RRealType()> : return RBoolType();
		case <RIntType(), RGtEq(), RRealType()> : return RBoolType();
		case <RIntType(), REq(), RRealType()> : return RBoolType();
		case <RIntType(), RNEq(), RRealType()> : return RBoolType();

		// MIXED REAL/INT CASES
		case <RRealType(), RProduct(), RIntType()> : return RRealType();
		case <RRealType(), RDiv(), RIntType()> : return RRealType();
		case <RRealType(), RPlus(), RIntType()> : return RRealType();
		case <RRealType(), RMinus(), RIntType()> : return RRealType();
		case <RRealType(), RLt(), RIntType()> : return RBoolType();
		case <RRealType(), RLtEq(), RIntType()> : return RBoolType();
		case <RRealType(), RGt(), RIntType()> : return RBoolType();
		case <RRealType(), RGtEq(), RIntType()> : return RBoolType();
		case <RRealType(), REq(), RIntType()> : return RBoolType();
		case <RRealType(), RNEq(), RIntType()> : return RBoolType();

		// STR CASES
		case <RStrType(), RPlus(), RStrType()> : return RStrType();
		case <RStrType(), RLt(), RStrType()> : return RBoolType();
		case <RStrType(), RLtEq(), RStrType()> : return RBoolType();
		case <RStrType(), RGt(), RStrType()> : return RBoolType();
		case <RStrType(), RGtEq(), RStrType()> : return RBoolType();
		case <RStrType(), REq(), RStrType()> : return RBoolType();
		case <RStrType(), RNEq(), RStrType()> : return RBoolType();

		// MIXED VALUE/OTHER CASES
		case <RValueType(), REq(), RIntType()> : return RBoolType();
		case <RValueType(), REq(), RRealType()> : return RBoolType();
		case <RValueType(), REq(), RStrType()> : return RBoolType();
		case <RValueType(), REq(), RListType(_)> : return RBoolType();
		case <RValueType(), REq(), RSetType(_)> : return RBoolType();
		case <RValueType(), REq(), RMapType(_,_)> : return RBoolType();
		case <RValueType(), REq(), RNodeType()> : return RBoolType();
		case <RValueType(), REq(), RLocType()> : return RBoolType();
		case <RValueType(), REq(), RRelType(_)> : return RBoolType();
		case <RValueType(), REq(), RTupleType(_)> : return RBoolType();
		case <RValueType(), REq(), RBoolType()> : return RBoolType();
		case <RValueType(), REq(), RValueType()> : return RBoolType();
		case <RValueType(), REq(), RDateTimeType()> : return RBoolType();
		case <RValueType(), RNEq(), RIntType()> : return RBoolType();
		case <RValueType(), RNEq(), RRealType()> : return RBoolType();
		case <RValueType(), RNEq(), RStrType()> : return RBoolType();
		case <RValueType(), RNEq(), RListType(_)> : return RBoolType();
		case <RValueType(), RNEq(), RSetType(_)> : return RBoolType();
		case <RValueType(), RNEq(), RMapType(_,_)> : return RBoolType();
		case <RValueType(), RNEq(), RNodeType()> : return RBoolType();
		case <RValueType(), RNEq(), RLocType()> : return RBoolType();
		case <RValueType(), RNEq(), RRelType(_)> : return RBoolType();
		case <RValueType(), RNEq(), RTupleType(_)> : return RBoolType();
		case <RValueType(), RNEq(), RBoolType()> : return RBoolType();
		case <RValueType(), RNEq(), RValueType()> : return RBoolType();
		case <RValueType(), RNEq(), RDateTimeType()> : return RBoolType();

		// MIXED OTHER/VALUE CASES
		case <RIntType(), REq(), RValueType()> : return RBoolType();
		case <RRealType(), REq(), RValueType()> : return RBoolType();
		case <RStrType(), REq(), RValueType()> : return RBoolType();
		case <RListType(_), REq(), RValueType()> : return RBoolType();
		case <RSetType(_), REq(), RValueType()> : return RBoolType();
		case <RMapType(_,_), REq(), RValueType()> : return RBoolType();
		case <RNodeType(), REq(), RValueType()> : return RBoolType();
		case <RLocType(), REq(), RValueType()> : return RBoolType();
		case <RRelType(_), REq(), RValueType()> : return RBoolType();
		case <RTupleType(_), REq(), RValueType()> : return RBoolType();
		case <RBoolType(), REq(), RValueType()> : return RBoolType();
		case <RValueType(), REq(), RValueType()> : return RBoolType();
		case <RDateTimeType(), REq(), RValueType()> : return RBoolType();
		case <RIntType(), RNEq(), RValueType()> : return RBoolType();
		case <RRealType(), RNEq(), RValueType()> : return RBoolType();
		case <RStrType(), RNEq(), RValueType()> : return RBoolType();
		case <RListType(_), RNEq(), RValueType()> : return RBoolType();
		case <RSetType(_), RNEq(), RValueType()> : return RBoolType();
		case <RMapType(_,_), RNEq(), RValueType()> : return RBoolType();
		case <RNodeType(), RNEq(), RValueType()> : return RBoolType();
		case <RLocType(), RNEq(), RValueType()> : return RBoolType();
		case <RRelType(_), RNEq(), RValueType()> : return RBoolType();
		case <RTupleType(_), RNEq(), RValueType()> : return RBoolType();
		case <RBoolType(), RNEq(), RValueType()> : return RBoolType();
		case <RValueType(), RNEq(), RValueType()> : return RBoolType();
		case <RDateTimeType(), RNEq(), RValueType()> : return RBoolType();

		// NODE CASES
		// TODO: Should take subtypes into account
		case <RNodeType(), RLt(), RNodeType()> : return RBoolType();
		case <RNodeType(), RLtEq(), RNodeType()> : return RBoolType();
		case <RNodeType(), RGt(), RNodeType()> : return RBoolType();
		case <RNodeType(), RGtEq(), RNodeType()> : return RBoolType();
		case <RNodeType(), REq(), RNodeType()> : return RBoolType();
		case <RNodeType(), RNEq(), RNodeType()> : return RBoolType();

		// LOC CASES
		case <RLocType(), RPlus(), RStrType()> : return RLocType();
		case <RLocType(), REq(), RLocType()> : return RBoolType();
		
		// LIST CASES
		case <lt, RNotIn(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <lt, RIn(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), RProduct(), RListType(rt)> : return makeListType(makeTupleType([lt,rt]));
		case <RListType(lt), RPlus(), RListType(rt)> : return makeListType(lub(lt,rt));
		case <RListType(lt), RPlus(), rt> : return makeListType(lub(lt,rt));
		case <lt, RPlus(), RListType(rt)> : return makeListType(lub(lt,rt));
		case <RListType(lt), RMinus(), RListType(rt)> : return subtypeOf(rt,lt) ? lType : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RListType(lt), RMinus(), rt> : return subtypeOf(rt,lt) ? lType : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RListType(lt), RLt(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), RLtEq(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), RGt(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), RGtEq(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), REq(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RListType(lt), RNEq(), RListType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);

		// SET CASES
		case <lt, RNotIn(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <lt, RIn(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RProduct(), RSetType(rt)> : return makeRelType(makeTupleType([lt,rt]));
		case <RSetType(lt), RPlus(), RSetType(rt)> : return makeSetType(lub(lt,rt));
		case <RSetType(lt), RPlus(), rt> : return makeSetType(lub(lt,rt));
		case <lt, RPlus(), RSetType(rt)> : return makeSetType(lub(lt,rt));
		case <RSetType(lt), RMinus(), RSetType(rt)> : return subtypeOf(rt,lt) ? lType : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RSetType(lt), RMinus(), rt> : return subtypeOf(rt,lt) ? lType : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RSetType(lt), RLt(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RLtEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RGt(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RGtEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), REq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RNEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RInter(), RSetType(rt)> : return ltype;

		// BAG CASES TODO: Bags are not implemented yet

		// MAP CASES

		// REL CASES
		case <lt, RNotIn(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <lt, RIn(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RProduct(), RSetType(rt)> : return makeRelType(makeTupleType([lt,rt]));
		case <RRelType(lt), RProduct(), RSetType(rt)> : return makeRelType(makeTupleType([lt,rt]));
		case <RSetType(lt), RProduct(), RRelType(rt)> : return makeRelType(makeTupleType([lt,rt]));
		case <RRelType(lt), RProduct(), RRelType(rt)> : return makeRelType(makeTupleType([lt,rt]));
		case <RRelType(lt), RPlus(), RRelType(rt)> : return makeRelType(lub(lt,rt));
		case <RSetType(lt), RPlus(), RRelType(rt)> : return makeRelType(lub(lt,rt));
		case <RRelType(lt), RPlus(), RSetType(rt)> : return makeRelType(lub(lt,rt));
		case <RRelType(lt), RPlus(), rt> : return makeRelType(lub(lt,rt));
		case <lt, RPlus(), RRelType(rt)> : return makeRelType(lub(lt,rt));
		case <RRelType(lt), RMinus(), RRelType(rt)> : return subtypeOf(rt,lt) ? RRelType(lt) : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RRelType(lt), RMinus(), RSetType(rt)> : return subtypeOf(rt,lt) ? RRelType(lt) : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RSetType(lt), RMinus(), RRelType(rt)> : return subtypeOf(rt,lt) ? RRelType(lt) : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RRelType(lt), RMinus(), rt> : return subtypeOf(rt,lt) ? RRelType(lt) : makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rt)> must be a subtype of <prettyPrintType(lt)>",l);
		case <RRelType(lt), RLt(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RLt(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RLt(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RLtEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RLtEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RLtEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RGt(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RGt(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RGt(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RGtEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RGtEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RGtEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), REq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), REq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), REq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RNEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RSetType(lt), RNEq(), RRelType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RNEq(), RSetType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RRelType(lt), RInter(), RRelType(rt)> : return ltype;
		case <RSetType(lt), RInter(), RRelType(rt)> : return ltype;
		case <RRelType(lt), RInter(), RSetType(rt)> : return ltype;

		// TUPLE CASES
		case <RTupleType(lt), RPlus(), RTupleType(rt)> : return RTupleType(lt+rt);
		case <RTupleType(lt), RLt(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RTupleType(lt), RLtEq(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RTupleType(lt), RGt(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RTupleType(lt), RGtEq(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RTupleType(lt), REq(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		case <RTupleType(lt), RNEq(), RTupleType(rt)> : return subtypeOf(lt,rt) ? RBoolType() : makeFailType("In comparison operation <prettyPrintOp(rop)> type <prettyPrintType(lt)> must be a subtype of <prettyPrintType(rt)>",l);
		
		// CONSTRUCTOR CASES
		// TODO: Should take ADT type into account
		case <RConstructorType(_,_,_), REq(), RConstructorType(_,_,_)> : return RBoolType();
		case <RConstructorType(_,_,_), RNEq(), RConstructorType(_,_,_)> : return RBoolType();

		// FUN CASES
		case <RFunctionType(_,_), REq(), RFunctionType(_,_)> : return RBoolType();

		// DATETIME CASES
		case <RDateTimeType(), RLt(), RDateTimeType()> : return RBoolType();
		case <RDateTimeType(), RLtEq(), RDateTimeType()> : return RBoolType();
		case <RDateTimeType(), RGt(), RDateTimeType()> : return RBoolType();
		case <RDateTimeType(), RGtEq(), RDateTimeType()> : return RBoolType();
		case <RDateTimeType(), REq(), RDateTimeType()> : return RBoolType();
		case <RDateTimeType(), RNEq(), RDateTimeType()> : return RBoolType();
	}
	return makeFailType("In operation <prettyPrintOp(rop)> type <prettyPrintType(rType)> must be a subtype of <prettyPrintType(lType)>",l);
}
