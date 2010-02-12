module org::rascalmpl::checker::Types

import List;

import org::rascalmpl::checker::ListUtils;

import languages::rascal::syntax::Rascal;

//
// Abstract syntax for names
//
data RName =
	  RSimpleName(str name)
	| RCompoundName(list[str] nameParts)
;

//
// Abstract syntax for types
//
data RBasicType =
  	  RBoolType()
  	| RIntType()
  	| RRealType()
  	| RStrType()
  	| RValueType()
  	| RNodeType()
    	| RVoidType()
  	| RLocType()
  	| RListType()
  	| RSetType()
  	| RBagType()
  	| RMapType()
  	| RRelType() 
  	| RTupleType() 
  	| RLexType()
  	| RTypeType() 
  	| RADTType()
  	| RConstructorType() 
  	| RFunType()
  	| RNonTerminalType()
  	| RReifiedType()
  	| RDateTimeType()
;

data RTypeArg =
	  RTypeArg(RType typeArg)
	| RNamedTypeArg(RType typeArg, RName typeName)
;

data RStructuredType = 
	  RStructuredType(RBasicType basicType, list[RTypeArg] typeArgs)
;

data RFunctionType = 
	  RFunctionType(RType retType, list[RTypeArg] argTypes)
;

data RTypeVar =
	  RFreeTypeVar(RName varName)
	| RBoundTypeVar(RName varName, RType varTypeBound)
;

data RUserType =
	  RTypeName(RName typeName)
	| RParametricType(RName typeName, list[RType] typeParams)
;

data RDataTypeSelector = 
	  RDataTypeSelector(RName sortName, RName productionName)
;

data RType =
	  RTypeBasic(RBasicType bt)
	| RTypeStructured(RStructuredType st)
	| RTypeFunction(RFunctionType ft)
	| RTypeTVar(RTypeVar tv)
	| RTypeUser(RUserType ut)
	| RTypeSelector(RDataTypeSelector dts)
	| RTypeParen(RType parenType)
	| RFailType(str failMsg, loc failLoc)
;

//
// Annotation for adding types to expressions
//
anno RType Expression@rtype;

//
// Printing routines for names and types
//
public str prettyPrintNameList(list[str] nameList) {
	return joinList(nameList,str(str s) { return s; },"::","");
}
	
public str prettyPrintName(RName n) {
	switch(n) {
		case RSimpleName(s) : return s;
		case RCompoundName(sl) : return prettyPrintNameList(sl);
	}
}

public str prettyPrintBasicType(RBasicType bt) {
	switch(bt) {
		case RBoolType() : return "bool";
		case RIntType() : return "int";
		case RRealType() : return "real";
		case RStrType() : return "str";
		case RValueType() : return "value";
		case RNodeType() : return "node";
		case RVoidType() : return "void";
		case RLocType() : return "loc";
		case RListType() : return "list";
		case RSetType() : return "set";
		case RBagType() : return "bag";
		case RMapType() : return "map";
		case RRelType() : return "rel";
		case RTupleType() : return "tuple";
		case RLexType() : return "lex";
		case RTypeType() : return "type";
		case RADTType() : return "adt";
		case RConstructorType() : return "constructor";
		case RFunType() : return "fun";
		case RNonTerminalType() : return "non-terminal";
		case RReifiedType() : return "reified";
		case RDateTimeType() : return "datetime";
	}
}

public str prettyPrintTA(RTypeArg ta) {
	switch(ta) {
		case RTypeArg(rt) : return prettyPrintType(rt);
		case RNamedTypeArg(rt,tn) : return prettyPrintType(rt) + " " + prettyPrintName(tn);
	}
}

public str prettyPrintTAList(list[RTypeArg] taList) {
	return joinList(taList, prettyPrintTA, ", ", "");
}

public str prettyPrintStructuredType(RStructuredType st) {
	switch(st) {
		case RStructuredType(bt,tas) : return prettyPrintBasicType(bt) + "[" + prettyPrintTAList(tas) + "]";
	}
}

public str prettyPrintFunctionType(RFunctionType ft) {
	switch(st) {
		case RFunctionType(rt,tas) : return prettyPrintType(rt) + "[" + prettyPrintTAList(tas) + "]";
	}
}

public str prettyPrintTVar(RTypeVar tv) {
	switch(tv) {
		case RFreeTypeVar(tn) : return "&" + prettyPrintName(tn);
		case RBoundTypeVar(vn,vtb) : return "&" + prettyPrintName(vn) + " \<: " + prettyPrintType(vtb);
	}
}

public str prettyPrintUserType(RUserType ut) {
	switch(ut) {
		case RTypeName(tn) : return prettyPrintName(tn);
		case RParametricType(tn,ltp) : return prettyPrintName(tn) + " [ " + prettyPrintTypeList(ltp) + " ]";
	}
}

public str prettyPrintTypeList(list[RType] tList) {
	return joinList(tList,prettyPrintType,", ","");
}

public str prettyPrintSelector(RDataTypeSelector dts) {
	switch(dts) {
		case RDataType(sn,pn) : return prettyPrintName(sn) + "." + prettyPrintName(pn);
	}
}

public str prettyPrintType(RType t) {
	switch(t) {
		case RTypeBasic(bt) : return prettyPrintBasicType(bt);
		case RTypeStructured(st) : return prettyPrintStructuredType(st);
		case RTypeFunction(ft) : return prettyPrintFunctionType(ft);
		case RTypeTVar(tv) : return prettyPrintTVar(tv);
		case RTypeUser(ut) : return prettyPrintUserType(ut);
		case RTypeSelector(dts) : return prettyPrintSelector(dts);
		case RTypeParen(pt) :  return "(" + prettyPrintType(pt) + ")";
		case RFailType(s,l) :  return "Failure: " + s;
	}
}

//
// Convert Rascal concrete syntax representations of names into our internal AST-like representation
//
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

//
// Convert Rascal concrete syntax representations of types into our internal AST-like representation
//
// TODO: Add missing conversion functions
//
public RBasicType convertBasicType(BasicType t) {
	switch(t) {
		case `bool` : return RBoolType();

		case `int` : return RIntType();

		case `real` : return RRealType();

		case `str` : return RStrType();

		case `value` : return RValueType();

		case `node` : return RNodeType();

		case `void` : return RVoidType();

		case `loc` : return RLocType();

		case `list` : return RListType();

		case `set` : return RSetType();

		case `bag` : return RBagType();

		case `map` : return RMapType();

		case `rel` : return RRelType();

		case `tuple` : return RTupleType();

		case `lex` : return RLexType();

		case `type` : return RTypeType();

		case `adt` : return RADTType();

		case `constructor` : return RConstructorType();

		case `fun` : return RFunType();

		case `non-terminal` : return RNonTerminalType();

		case `reified` : return RReifiedType();

		case `datetime` : return RDateTimeType();
	}
}

public RTypeArg convertTypeArg(TypeArg ta) {
	switch(ta) {
		case (TypeArg) `<Type t>` : return RTypeArg(convertType(t));

		case (TypeArg) `<Type t> <Name n>` : return RNamedTypeArg(convertType(t),convertName(n));
	}
}

public list[RTypeArg] convertTypeArgList({TypeArg ","}+ tas) {
	list[RTypeArg] tal = [];
	for (ta <- tas) {
		tal += [ convertTypeArg(ta) ];
	}
	return tal;
}

public RStructuredType convertStructuredType(StructuredType st) {
	switch(st) {
		case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : 
			return RStructuredType(convertBasicType(bt), convertTypeArgList(tas));
	}
}

public RType convertType(Type t) {
	switch(t) {
		case (Type) `<BasicType bt>` : return RTypeBasic(convertBasicType(bt));
		case (Type) `<StructuredType st>` : return RTypeStructured(convertStructuredType(st));
	}
}

//
// Helper routines for querying/building/etc types
//
public bool isIntType(RType t) {
	switch(t) {
		case RTypeBasic(tb) : return isIntTypeBT(tb);

		default : return false;
	}
}

public bool isIntTypeBT(RBasicType t) {
	switch(t) {
		case RIntType() : return true;

		default : return false;
	}
}

public bool isRealType(RType t) {
	switch(t) {
		case RTypeBasic(tb) : return isRealTypeBT(tb);

		default : return false;
	}
}

public bool isRealTypeBT(RBasicType t) {
	switch(t) {
		case RRealType() : return true;
		
		default : return false;
	}
}

public bool isBoolType(RType t) {
	switch(t) {
		case RTypeBasic(tb) : return isBoolTypeBT(tb);

		default : return false;
	}
}

public bool isBoolTypeBT(RBasicType t) {
	switch(t) {
		case RBoolType() : return true;
		
		default : return false;
	}
}

public bool isStrType(RType t) {
	switch(t) {
		case RTypeBasic(tb) : return isStrTypeBT(tb);

		default : return false;
	}
}

public bool isStrTypeBT(RBasicType t) {
	switch(t) {
		case RStrType() : return true;
		
		default : return false;
	}
}

public bool isSetType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return isSetTypeBT(st);

		default : return false;
	}
}

public bool isSetTypeBT(RStructuredType t) {
	switch(t) {
		case RStructuredType(RSetType(), tas) : return size(tas) == 1;

		default: return false;
	}
}

public RType getSetElementType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return getSetElementTypeST(st);

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

public RType getSetElementTypeST(RStructuredType t) {
	switch(t) {
		case RStructuredType(RSetType(), tas) : return getElementType(head(tas));

		case RStructuredType(RRelType(), tas) : return getElementType(head(tas));

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

public RType getElementType(RTypeArg t) {
	switch(t) {
		case RTypeArg(rt) : return rt;

		case RNamedTypeArg(rt,rtn) : return rt;
	}
}

public bool isRelType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return isRelTypeST(st);

		default : return false;
	}
}

public bool isRelTypeST(RStructuredType t) {
	switch(t) {
		case RStructuredType(RRelType(), tas) : return size(tas) == 1;

		default: return false;
	}
}

public RType getRelElementType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return getRelElementTypeST(st);

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

public RType getRelElementTypeST(RStructuredType t) {
	switch(t) {
		case RStructuredType(RRelType(), tas) : return getElementType(head(tas));

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

public bool isListType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return isListTypeST(st);

		default : return false;
	}
}

public bool isListTypeST(RStructuredType t) {
	switch(t) {
		case RStructuredType(RListType(), tas) : return size(tas) == 1;

		default: return false;
	}
}

public RType getListElementType(RType t) {
	switch(t) {
		case RTypeStructured(st) : return getListElementTypeST(st);

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

public RType getListElementTypeST(RStructuredType t) {
	switch(t) {
		case RStructuredType(RListType(), tas) : return getElementType(head(tas));

		default : return RTypeBasic(RVoidType()); // Should throw exception
	}
}

private bool isFailType(RType t) {
	switch(t) {
		case RFailType(s,l) : return true;
		default: return false;
	}
}

// Stubs for comparison functions -- not sure if I'll need these yet...
public bool compareTypes(RType t1, RType t2) {
	return false;
}

public bool compareDataTypeSelectors(RDataTypeSelector t1, RDataTypeSelector t2) {
	return false;
}

public bool compareUserTypes(RUserType t1, RUserType t2) {
	return false;
}

public bool compareTypeVars(RTypeVar t1, RTypeVar t2) {
	return false;
}

public bool compareFunctionTypes(RFunctionType t1, RFunctionType t2) {
	return false;
}

public bool compareTypeArgs(RTypeArg t1, RTypeArg t2) {
	return false;
}

public bool compareBasicTypes(RBasicType t1, RBasicType t2) {
	return false;
}

//
// Functions to build various types
//
public RType makeIntType() { return RTypeBasic(RIntType()); }

public RType makeRealType() { return RTypeBasic(RRealType()); }

public RType makeBoolType() { return RTypeBasic(RBoolType()); }

public RType makeStrType() { return RTypeBasic(RStrType()); }

public RType makeVoidType() { return RTypeBasic(RVoidType()); }

public RType makeValueType() { return RTypeBasic(RValueType()); }

public RType makeListType(RType itemType) { return RTypeStructured(RStructuredType(RListType(),[RTypeArg(itemType)])); }

public RType makeSetType(RType itemType) { return RTypeStructured(RStructuredType(RSetType(), [RTypeArg(itemType)])); }

public RType makeTupleType(list[RType] itemTypes) { 
	return RTypeStructured(RStructuredType(RTupleType(), [ RTypeArg( x ) | x <- itemTypes ]));
}

public RType makeFailType(str s, loc l) { return RFailType(s,l); }

