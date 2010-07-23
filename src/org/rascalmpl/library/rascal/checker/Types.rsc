module rascal::checker::Types

import List;
import Set;
import IO;
import ParseTree;

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
	if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
		nameParts = [ "<n>" | n <- nl];
		if (size(nameParts) > 1) {
			return RCompoundName(nameParts);
		} else {
			return RSimpleName(head(nameParts));
		} 
	}
	throw "Unexpected syntax for qualified name: <qn>";
}

public RName convertName(Name n) {
	return RSimpleName("<n>");
}

private Name getLastName(QualifiedName qn) {
	if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
		nameParts = [ n | n <- nl];
		return head(tail(nameParts,1));
	}
	throw "Unexpected syntax for qualified name: <qn>";
}

public RName appendName(RName n1, RName n2) {
	if (RSimpleName(s1) := n1  && RSimpleName(s2) := n2) return RCompoundName([s1,s2]);
	if (RSimpleName(s1) := n1 && RCompoundName(ss2) := n2) return RCompoundName([s1] + ss2);
	if (RCompoundName(ss1) := n1 && RSimpleName(s2) := n2) return RCompoundName(ss1 + s2);
	if (RCompoundName(ss1) := n1 && RCompoundName(ss2) := n2) return RCompoundName(ss1 + ss2);
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
  	| RNumType()
  	| RStrType()
  	| RValueType()
  	| RNodeType()
   	| RVoidType()
  	| RLocType()
  	| RListType(RType elementType)
  	| RSetType(RType elementType)
  	| RBagType(RType elementType)
	| RContainerType(RType elementType)
  	| RMapType(RNamedType domainType, RNamedType rangeType)
  	| RRelType(list[RNamedType] elementTypes) 
  	| RTupleType(list[RNamedType] elementTypes) 
  	| RLexType()
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
	| RAssignableType(RType wholeType, RType partType)
	| RUnknownType(RType wrappedType)
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
		case `num` : return RNumType();
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
		case `type` : return RReifiedType(RVoidType());
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

public list[RNamedType] convertTypeArgList({TypeArg ","}* tas) {
	return [convertTypeArg(ta) | ta <- tas];
}

public RType convertStructuredType(StructuredType st) {
	RType buildMapType(StructuredType st, list[RNamedType] mapTypes) {
		if (size(mapTypes) != 2) {
			throw "Invalid arguments provided for building map type: <st>";
		}
		return RMapType(mapTypes[0],mapTypes[1]);
	}
	
	switch(st) {
		case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : return RListType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : return RSetType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : return RBagType(getElementType(head(convertTypeArgList(tas)))); 
		case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : return buildMapType(st,convertTypeArgList(tas)); 
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
		case (UserType) `<QualifiedName n>` : return RUserType(convertName(n));
		case (UserType) `<QualifiedName n> [ <{Type ","}+ ts> ]` : return RParameterizedUserType(convertName(n),[convertType(ti) | ti <- ts]);
	}
}

public Name getUserTypeRawName(UserType ut) {
	switch(ut) {
		case (UserType) `<QualifiedName n>` : return getLastName(n);
		case (UserType) `<QualifiedName n> [ <{Type ","}+ ts> ]` : return getLastName(n);
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
		case (DataTypeSelector) `<QualifiedName n1> . <Name n2>` : return RDataTypeSelector(convertName(n1),convertName(n2));
	}
}

public RType convertType(Type t) {
	switch(t) {
		case (Type) `<BasicType bt>` : return convertBasicType(bt);
		case (Type) `<StructuredType st>` : return convertStructuredType(st);
		case (Type) `<FunctionType ft>` : return convertFunctionType(ft);
		case (Type) `<TypeVar tv>` : return RTypeVar(convertTypeVar(tv));
		case `<UserType ut>` : return convertUserType(ut);
		case (Type) `<UserType ut>` : return convertUserType(ut);
		case (Type) `<DataTypeSelector dts>` : return convertDataTypeSelector(dts);
		case (Type) `( <Type tp> )` : return convertType(tp);
		default : { println(t); throw "Error in convertType, unexpected type syntax: <t>"; }
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
		case RNumType() : return "num";
		case RStrType() : return "str";
		case RValueType() : return "value";
		case RNodeType() : return "node";
		case RVoidType() : return "void";
		case RLocType() : return "loc";
		case RListType(et) : return "list[<prettyPrintType(et)>]";
		case RSetType(et) : return "set[<prettyPrintType(et)>]";
		case RContainerType(et) : return "container[<prettyPrintType(et)>]";
		case RBagType(et) : return "bag[<prettyPrintType(et)>]";
		case RMapType(dt,rt) : return "map[<prettyPrintNamedType(dt)>,<prettyPrintNamdType(rt)>]";
		case RRelType(nts) : return "rel[<prettyPrintNamedTypeList(nts)>]";
		case RTupleType(nts) : return "tuple[<prettyPrintNamedTypeList(nts)>]";
		case RLexType() : return "lex";
		case RADTType(n,cs) : return "adt <prettyPrintType(n)>: <prettyPrintTypeList(cs)>"; // TODO: Add more detail on the pretty printer
		case RConstructorType(cn, ets, at) : return "Constructor for type <prettyPrintType(at)>: <prettyPrintName(cn)>(<prettyPrintNamedTypeList(ets)>)";
		case RFunctionType(rt, pts) : return "<prettyPrintType(rt)> (<prettyPrintNamedTypeList(pts)>)";
		case RNonTerminalType() : return "non-terminal";
		case RReifiedType(rt) : return "type(<prettyPrintType(t)>)";
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
		case RAssignableType(wt,pt) : return "Assignable type, whole <prettyPrintType(wt)>, part <prettyPrintType(pt)>";
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

public bool isNumType(RType t) {
	return RNumType() := t;
}

public bool isStrType(RType t) {
	return RStrType() := t;
}

public bool isValueType(RType t) {
	return RValueType() := t;
}

public bool isNodeType(RType t) {
	return RNodeType() := t;
}

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

public bool isContainerType(RType t) {
	return RContainerType(_) := t;
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

public bool isReifiedType(RType t) {
	return RReifiedType(_) := t;
}

// TODO: Add other is...

public bool isFunctionType(RType t) {
	return RFunctionType(_,_)  := t;
}

public bool isADTType(RType t) {
	return RADTType(_,_) := t;
}

public bool isConstructorType(RType t) {
	return RConstructorType(_,_,_) := t;
}
	
public bool isDateTimeType(RType t) {
	return RDateTimeType() := t;
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

public RType getVarArgsType(RType t) {
	if (RVarArgsType(vt) := t) return vt;
	throw "Cannot return var args type for type <prettyPrintType(t)>";
}

public bool isOverloadedType(RType t) {
	return ROverloadedType(_) := t;
}

public bool isVarArgsFun(RType t) {
	if (RFunctionType(_,ps) := t) {
		if (size(ps) > 0) {
			if (isVarArgsType(getElementType(head(tail(ps,1))))) {
				return true;
			}
		}
	}
	return false;
}

public bool isInferredType(RType t) {
	return RInferredType(_) := t;
}

public bool isAssignableType(RType t) {
	return RAssignableType(_,_) := t;
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

public RType getContainerElementType(RType t) {
	if (RContainerType(et) := t) return et;
	throw "Error: Cannot get container element type from type <prettyPrintType(t)>";
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

public list[RNamedType] getTupleFieldsWithNames(RType t) {
	if (RTupleType(tas) := t) {
		return [ ta | ta <- tas ];
	}
	throw "Cannot get tuple fields from type <prettyPrintType(t)>";	
}

public int getTupleFieldCount(RType t) {
	if (RTupleType(tas) := t) {
		return size(tas);
	}
	throw "Cannot get tuple field count from type <prettyPrintType(t)>";	
}

@doc{Check to see if a map defines a field.}
public bool mapHasField(RType t, RName fn) {
	if (RMapType(tl,tr) := t) {
		if (RNamedType(_,fn) := tl) return true;	
		if (RNamedType(_,fn) := tr) return true;	
	}
	return false;
}

@doc{Return the type of a field defined on a map.}
public RType getMapFieldType(RType t, RName fn) {
	if (RMapType(tl,tr) := t) {
		if (RNamedType(ft,fn) := tl) return ft;	
		if (RNamedType(ft,fn) := tr) return ft;	
	}
	throw "Map <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public list[RType] getMapFields(RType t) {
	if (RMapType(tl, tr) := t) {
		return [ getElementType(tl), getElementType(tr) ];
	}
	throw "Cannot get map fields from type <prettyPrintType(t)>";	
}

public list[RNamedType] getMapFieldsWithNames(RType t) {
	if (RMapType(tl, tr) := t) {
		return [ tl, tr ];
	}
	throw "Cannot get map fields from type <prettyPrintType(t)>";	
}

public RType getMapDomainType(RType t) {
	if (RMapType(tl,_) := t) return getElementType(tl);
	throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

public RType getMapRangeType(RType t) {
	if (RMapType(_,tr) := t) return getElementType(tr);
	throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

@doc{Check to see if a relation defines a field.}
public bool relHasField(RType t, RName fn) {
	if (RRelType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(_,fn) := ta) return true;	
		}
	}
	return false;
}

@doc{Return the type of a field defined on a relation.}
public RType getRelFieldType(RType t, RName fn) {
	if (RRelType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(ft,fn) := ta) return ft;	
		}
	}
	throw "Relation <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public list[RType] getRelFields(RType t) {
	if (RRelType(tas) := t) {
		return [ getElementType(ta) | ta <- tas ];
	}
	throw "Cannot get relation fields from type <prettyPrintType(t)>";	
}

public list[RNamedType] getRelFieldsWithNames(RType t) {
	if (RRelType(tas) := t) {
		return [ ta | ta <- tas ];
	}
	throw "Cannot get relation fields from type <prettyPrintType(t)>";	
}

@doc{Check to see if an ADT defines a field.}
public bool adtHasField(RType t, RName fn) {
	if (RADTType(n,cs) := t) {
		for (RConstructorType(cn,cts,at) <- cs) {
			for (ta <- cts) {
				if (RNamedType(_,fn) := ta) return true;
			}	
		}
	}
	return false;
}

//
// Look up the type of field fn on ADT t. Note that fields have a unique type in a given ADT, even if
// they appear on multiple constructors, so we can always use the first occurrence of the field we
// find on a constructor.
//
@doc{Return the type of a field on an ADT.}
public RType getADTFieldType(RType t, RName fn) {
	if (RADTType(n,cs) := t) {
		for (RConstructorType(cn,cts,at) <- cs) {
			for (ta <- cts) {
				if (RNamedType(ft,fn) := ta) return ft;
			}	
		}
	}
	throw "ADT <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public RName getADTName(RType t) {
	if (RADTType(n,_) := t) return getUserTypeName(n);
	throw "getADTName, invalid type given: <prettyPrintType(t)>";
}

public RType getWholeType(RType rt) {
	if (RAssignableType(wt,_) := rt) return wt;
	throw "Expected assignable type, got <prettyPrintType(rt)> instead.";
}

public RType getPartType(RType rt) {
	if (RAssignableType(_,pt) := rt) return pt;
	throw "Expected assignable type, got <prettyPrintType(rt)> instead.";
}
	
//
// Functions to build various types
//
public RType makeIntType() { return RIntType(); }

public RType makeRealType() { return RRealType(); }

public RType makeNumType() { return RNumType(); }

public RType makeBoolType() { return RBoolType(); }

public RType makeStrType() { return RStrType(); }

public RType makeVoidType() { return RVoidType(); }

public RType makeValueType() { return RValueType(); }

public RType makeLocType() { return RLocType(); }

public RType makeDateTimeType() { return RDateTimeType(); }

public RType makeListType(RType itemType) { return RListType(itemType); }

public RType makeSetType(RType itemType) { return RSetType(itemType); }

public RType makeContainerType(RType itemType) { return RContainerType(itemType); }

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

public RType makeAssignableType(RType wt, RType pt) { return RAssignableType(wt,pt); } 

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
	if (RConstructorType(cn, cts, pt) := ct) return [ getElementType(argType) | argType <- cts ]; 
	throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public RType getConstructorResultType(RType ct) {
	if (RConstructorType(cn, cts, pt) := ct) return pt;
	throw "Cannot get constructor ADT type from non-constructor type <prettyPrintType(ft)>";
}

public RName getUserTypeName(RType ut) {
	switch(ut) {
		case RUserType(x) : return x;
		case RParameterizedUserType(x,_) : return x;
		default: throw "Cannot get user type name from non user type <prettyPrintType(ut)>";
	}
} 
