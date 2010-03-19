module rascal::checker::Types

import List;
import Set;
import IO;

import rascal::checker::ListUtils;

import rascal::\old-syntax::Rascal;

//
// Abstract syntax for names
//
data RName =
	  RSimpleName(str name)
	| RCompoundName(list[str] nameParts)
;

public RName convertName(QualifiedName qn) {
	list[str] nameParts = [];
	for (/Name n <- qn) {
		nameParts = nameParts + [ "<n>" ];
	}
	if (size(nameParts) == 1) {
		RName rn = RSimpleName(head(nameParts));
		return rn;
	} else {
		RName rn = RCompoundName(nameParts);
		return rn;
	}
}

public str prettyPrintNameList(list[str] nameList) {
	return joinList(nameList,str(str s) { return s; },"::","");
}
	
public str prettyPrintName(RName n) {
	switch(n) {
		case RSimpleName(s) : return s;
		case RCompoundName(sl) : return prettyPrintNameList(sl);
	}
}

//
// NEW Abstract syntax for types
//
data RType =
  	  RBoolType()
  	| RIntType()
  	| RRealType()
  	| RStrType()
  	| RValueType()
  	| RNodeType()
   	| RVoidType()
  	| RLocType()
  	| RListType(RType elementType)
  	| RSetType(RType elementType)
  	| RBagType(RType elementType)
  	| RMapType(RNamedType domainType, RNamedType rangeType)
  	| RRelType(list[RNamedType] elementTypes) 
  	| RTupleType(list[RNamedType] elementTypes) 
  	| RLexType()
  	| RTypeType() 
  	| RADTType(RType adtName, list[RType] constructors)
  	| RConstructorType(RName cname, list[RNamedType] elementTypes, RType adtType) 
  	| RFunctionType(RType returnType, list[RNamedType] parameterTypes)
  	| RNonTerminalType()
  	| RReifiedType(RType baseType)
  	| RDateTimeType()

	| RFailType(set[tuple[str failMsg, loc failLoc]])
	| RInferredType(int tnum)
	| ROverloadedType(set[RType] possibleTypes)
	| RVarArgsType(RType vt)

    | RStatementType(RType internalType)

	| RAliasType(RType aliasName, RType aliasedType)

	| RDataTypeSelector(RName source, RName target)
	| RUserType(RName typeName)
	| RParameterizedUserType(RName typeName, list[RType] typeParams)
	| RTypeVar(RTypeVar tv)
;

data RNamedType =
	  RUnnamedType(RType typeArg)
	| RNamedType(RType typeArg, RName typeName)
;

data RTypeVar =
	  RFreeTypeVar(RName varName)
	| RBoundTypeVar(RName varName, RType varTypeBound)
;

public RType convertBasicType(BasicType t) {
	switch(t) {
		case `bool` : return RBoolType();
		case `int` : return RIntType();
		case `real` : return RRealType();
		case `str` : return RStrType();
		case `value` : return RValueType();
		case `node` : return RNodeType();
		case `void` : return RVoidType();
		case `loc` : return RLocType();
		case `list` : return RListType(RVoidType());
		case `set` : return RSetType(RVoidType());
		case `bag` : return RBagType(RVoidType());
		case `map` : return RMapType(RUnnamedType(RVoidType()),RUnnamedType(RVoidType()));
		case `rel` : return RRelType([]);
		case `tuple` : return RTupleType([]);
		case `lex` : return RLexType();
		case `type` : return RTypeType();
		case `adt` : return RADTType(RUserType(RSimpleName("unnamed")),[]);
		case `constructor` : return RConstructorType(RSimpleName("unnamed"),[],RVoidType());
		case `fun` : return RFunctionType(RVoidType,[]);
		case `non-terminal` : return RNonTerminalType();
		case `reified` : return RReifiedType(RVoidType());
		case `datetime` : return RDateTimeType();
	}
}

public RNamedType convertTypeArg(TypeArg ta) {
	switch(ta) {
		case (TypeArg) `<Type t>` : return RUnnamedType(convertType(t));
		case (TypeArg) `<Type t> <Name n>` : return RNamedType(convertType(t),convertName(n));
	}
}

public list[RNamedType] convertTypeArgList({TypeArg ","}+ tas) {
	return [convertTypeArg(ta) | ta <- tas];
}

public RType convertStructuredType(StructuredType st) {
	switch(st) {
		case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : return RListType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : return RSetType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : return RBagType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : return RMapType(convertTypeArgList(tas)); 
		case (StructuredType) `rel [ < {TypeArg ","}+ tas > ]` : return RRelType(convertTypeArgList(tas)); 
		case (StructuredType) `tuple [ < {TypeArg ","}+ tas > ]` : return RTupleType(convertTypeArgList(tas));
		case (StructuredType) `type [ < {TypeArg ","}+ tas > ]` : return RReifiedType(convertTypeArgList(tas));
		case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : throw "Invalid basic type <bt> in definition of structured type <st>";  
	}
}

public RType convertFunctionType(FunctionType ft) {
	switch(ft) {
		case (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )` : return RFunctionType(convertType(t),convertTypeArgList(tas));
	}
}

public RType convertUserType(UserType ut) {
	switch(ut) {
		case (UserType) `<Name n>` : return RUserType(convertName(n));
		case (UserType) `<Name n> [ <{Type ","}+ ts> ]` : return RParameterizedUserType(convertName(n),[convertType(ti) | ti <- ts]);
	}
}

public Name getUserTypeRawName(UserType ut) {
	switch(ut) {
		case (UserType) `<Name n>` : return n;
		case (UserType) `<Name n> [ <{Type ","}+ ts> ]` : return n;
	}
}

public RTypeVar convertTypeVar(TypeVar tv) {
	switch(tv) {
		case (TypeVar) `& <Name n>` : return RFreeTypeVar(convertName(n));
		case (TypeVar) `& <Name n> <: <Type tb>` : return RBoundTypeVar(convertName(n),convertType(tb));
	}
}

public RType convertDataTypeSelector(DataTypeSelector dts) {
	switch(dts) {
		case (DataTypeSelector) `<Name n1> . <Name n2>` : return RDataTypeSelector(convertName(n1),convertName(n2));
	}
}

public RType convertType(Type t) {
	switch(t) {
		case (Type) `<BasicType bt>` : return convertBasicType(bt);
		case (Type) `<StructuredType st>` : return convertStructuredType(st);
		case (Type) `<FunctionType ft>` : return convertFunctionType(ft);
		case (Type) `<TypeVar tv>` : return RTypeVar(convertTypeVar(tv));
		case (Type) `<UserType ut>` : return convertUserType(ut);
		case (Type) `<DataTypeSelector dts>` : return convertDataTypeSelector(dts);
		case (Type) `( <Type tp> )` : return convertType(tp);
	}
}

public str prettyPrintTypeList(list[RType] tList) {
	return joinList(tList,prettyPrintType,", ","");
}

public str printLocMsgPair(tuple[str failMsg, loc failLoc] lmp) {
	return "Error at location <lmp.failLoc>: <lmp.failMsg>";
}

public str prettyPrintType(RType t) {
	switch(t) {
		case RBoolType() : return "bool";
		case RIntType() : return "int";
		case RRealType() : return "real";
		case RStrType() : return "str";
		case RValueType() : return "value";
		case RNodeType() : return "node";
		case RVoidType() : return "void";
		case RLocType() : return "loc";
		case RListType(et) : return "list[<prettyPrintType(et)>]";
		case RSetType(et) : return "set[<prettyPrintType(et)>]";
		case RBagType(et) : return "bag[<prettyPrintType(et)>]";
		case RMapType(dt,rt) : return "map[<prettyPrintNamedType(dt)>,<prettyPrintNamdType(rt)>]";
		case RRelType(nts) : return "rel[<prettyPrintNamedTypeList(nts)>]";
		case RTupleType(nts) : return "tuple[<prettyPrintNamedTypeList(nts)>]";
		case RLexType() : return "lex";
		case RTypeType() : return "type";
		case RADTType(n,cs) : return "adt <prettyPrintType(n)>: <prettyPrintTypeList(cs)>"; // TODO: Add more detail on the pretty printer
		case RConstructorType(cn, ets, at) : return "Constructor for type <prettyPrintType(at)>: <prettyPrintName(cn)>(<prettyPrintNamedTypeList(ets)>)";
		case RFunctionType(rt, pts) : return "<prettyPrintType(rt)> (<prettyPrintNamedTypeList(pts)>)";
		case RNonTerminalType() : return "non-terminal";
		case RReifiedType(rt) : return "#<prettyPrintType(t)>";
		case RDateTimeType() : return "datetime";
		case RFailType(sls) :  return "Failure: " + joinList(toList(sls),printLocMsgPair,", ","");
		case RInferredType(n) : return "Inferred Type: <n>";
		case ROverloadedType(pts) : return "Overloaded type, could be: " + prettyPrintTypeList([p | p <- pts]);
		case RVarArgsType(vt) : return "<prettyPrintType(vt)>...";
		case RStatementType(rt) : return "Statement: <prettyPrintType(rt)>";
		case RAliasType(an,at) : return "Alias <prettyPrintType(an)> = <prettyPrintType(at)>";
		case RDataTypeSelector(s,t) : return "Selector <s>.<t>";
		case RUserType(tn) : return "<prettyPrintName(tn)>";
		case RParameterizedUserType(tn, tps) : return "<prettyPrintName(tn)>(<prettyPrintTypeList(tps)>)";
		case RTypeVar(tv) : return prettyPrintTypeVar(tv);
	}
}

public str prettyPrintNamedType(RNamedType nt) {
	switch(nt) {
		case RUnnamedType(rt) : return prettyPrintType(rt);
		case RNamedType(rt,tn) : return prettyPrintType(rt) + " " + prettyPrintName(tn);
	}
}

public str prettyPrintNamedTypeList(list[RNamedType] ntList) {
	return joinList(ntList, prettyPrintNamedType, ", ", "");
}

public str prettyPrintTypeVar(RTypeVar tv) {
	switch(tv) {
		case RFreeTypeVar(tn) : return "&" + prettyPrintName(tn);
		case RBoundTypeVar(vn,vtb) : return "&" + prettyPrintName(vn) + " \<: " + prettyPrintType(vtb);
	}
}

//
// Annotation for adding types to expressions
//
anno RType Tree@rtype; 

//
// Helper routines for querying/building/etc types
//
public bool isBoolType(RType t) {
	return RBoolType() := t;
}

public bool isIntType(RType t) {
	return RIntType() := t;
}

public bool isRealType(RType t) {
	return RRealType() := t;
}

public bool isStrType(RType t) {
	return RStrType() := t;
}

public bool isValueType(RType t) {
	return RValueType() := t;
}

// isNodeType

public bool isVoidType(RType t) {
	return RVoidType() := t;
}

public bool isLocType(RType t) {
	return RLocType() := t;
}

public bool isListType(RType t) {
	return RListType(_) := t;
}

public bool isSetType(RType t) {
	return RSetType(_) := t;
}

public bool isBagType(RType t) {
	return RBagType(_) := t;
}

public bool isMapType(RType t) {
	return RMapType(_,_) := t;
}

public bool isRelType(RType t) {
	return RRelType(_) := t;
}

public bool isTupleType(RType t) {
	return RTupleType(_) := t;
}

// TODO: Add other is...

public bool isFunctionType(RType t) {
	return RFunctionType(_,_)  := t;
}

public bool isConstructorType(RType t) {
	return RConstructorType(_,_,_) := t;
}
	
public bool isFailType(RType t) {
	return RFailType(_) := t; 
}

public bool isStatementType(RType t) {
	return RStatementType(_) := t;
}

public bool isVarArgsType(RType t) {
	return RVarArgsType(_) := t;
}

public bool isOverloadedType(RType t) {
	return ROverloadedType(_) := t;
}

public bool isInferredType(RType t) {
	return RInferredType(_) := t;
}

public RType getSetElementType(RType t) {
	if (RSetType(et) := t)
		return et;
	if (RRelType(ets) := t)
		return RTupleType(ets);
	throw "Error: Cannot get set element type from type <prettyPrintType(t)>";
}

public RType getElementType(RNamedType t) {
	switch(t) {
		case RUnnamedType(rt) : return rt;

		case RNamedType(rt,_) : return rt;
	}
}

public RType getRelElementType(RType t) {
	if (RRelType(ets) := t)
		return RTupleType(ets);
	throw "Error: Cannot get relation element type from type <prettyPrintType(t)>";
}

public RType getListElementType(RType t) {
	if (RListType(et) := t) return et;
	throw "Error: Cannot get list element type from type <prettyPrintType(t)>";
}

public int getInferredTypeIndex(RType t) {
	if (RInferredType(n) := t) return n;
	throw "Error: Cannot get inferred type index from non-inferred type <prettyPrintType(t)>";
}

public set[RType] getOverloadOptions(RType t) {
	if (ROverloadedType(s) := t) return s;
	throw "Error: Cannot get overloaded options from non-overloaded type <prettyPrintType(t)>";
}

public bool tupleHasField(RType t, RName fn) {
	if (RTupleType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(_,fn) := ta) return true;	
		}
	}
	return false;
}

public RType getTupleFieldType(RType t, RName fn) {
	if (RTupleType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(ft,fn) := ta) return ft;	
		}
	}
	throw "Tuple <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public list[RType] getTupleFields(RType t) {
	if (RTupleType(tas) := t) {
		return [ getElementType(ta) | ta <- tas ];
	}
	throw "Cannot get tuple fields from type <prettyPrintType(t)>";	
}

public int getTupleFieldCount(RType t) {
	if (RTupleType(tas) := t) {
		return size(tas);
	}
	throw "Cannot get tuple field count from type <prettyPrintType(t)>";	
}

//
// Functions to build various types
//
public RType makeIntType() { return RIntType(); }

public RType makeRealType() { return RRealType(); }

public RType makeBoolType() { return RBoolType(); }

public RType makeStrType() { return RStrType(); }

public RType makeVoidType() { return RVoidType(); }

public RType makeValueType() { return RValueType(); }

public RType makeLocType() { return RLocType(); }

public RType makeDateTimeType() { return RDateTimeType(); }

public RType makeListType(RType itemType) { return RListType(itemType); }

public RType makeSetType(RType itemType) { return RSetType(itemType); }

public RType makeMapType(RType domainType, RType rangeType) { return RMapType(RUnnamedType(domainType), RUnnamedType(rangeType)); }

public RType makeTupleType(list[RType] its) { 	return RTupleType([ RUnnamedType( t ) | t <- its ]); }

public RType makeFunctionType(RType retType, list[RType] paramTypes) { return RFunctionType(retType, [ RUnnamedType( x ) | x <- paramTypes ]); }

public RType makeReifiedType(RType mainType) { return RReifiedType(mainType); }

public RType makeVarArgsType(RType t) { return RVarArgsType(t); }
	
public RType makeFailType(str s, loc l) { return RFailType({<s,l>}); }

// TODO: Come up with a less stupid name for this
public RType makeBiggerFailType(RType ft, set[tuple[str s, loc l]] sls) { return RFailType({ < e.s, e.l > | e <- sls }); }

public RType makeInferredType(int n) { return RInferredType(n); }

public RType makeStatementType(RType rt) { return RStatementType(rt); }

public RType getInternalStatementType(RType st) {
	if (RStatementType(rt) := st) return rt;
	throw "Cannot get internal statement type from type <prettyPrintType(st)>";
}

public RType extendFailType(RType ft, set[tuple[str s, loc l]] sls) {
	if (RFailType(sls2) := ft) {
		return RFailType(sls2 + { < e.s, e.l > | e <- sls });
	}
	throw "Cannot extend a non-failure type with failure information, type <prettyPrintType(ft)>";
}
 
public RType collapseFailTypes(set[RType] rt) { return RFailType({ s | RFailType(ss) <- rt, s <- ss }); }

public RType makeConstructorType(RName cname, list[RNamedType] tas, RType tn) { 	return RConstructorType(cname, tas, tn); }

public list[RType] getFunctionArgumentTypes(RType ft) {
	if (RFunctionType(_, ats) := ft) return [ getElementType(argType) | argType <- ats ];
	throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public RType getFunctionReturnType(RType ft) {
	if (RFunctionType(retType, _) := ft) return retType; 
	throw "Cannot get function return type from non-function type <prettyPrintType(ft)>";
}

public list[RType] getConstructorArgumentTypes(RType ct) {
	if (RTypeConstructor(cn, cts, pt) := ct) return [ getElementType(argType) | argType <- cts ]; 
	throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ft)>";
}

public RType getConstructorResultType(RType ct) {
	if (RTypeConstructor(cn, cts, pt) := ct) return pt;
	throw "Cannot get constructor ADT type from non-constructor type <prettyPrintType(ft)>";
}

public RName getUserTypeName(RType ut) {
	switch(ut) {
		case RUserType(x) : return x;
		case RParameterizedUserType(x,_) : return x;
		default: throw "Cannot get user type name from non user type <prettyPrintType(ut)>";
	}
} 
