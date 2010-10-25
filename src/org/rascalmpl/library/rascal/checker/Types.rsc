module rascal::checker::Types

import List;
import Set;
import IO;
import ParseTree;
import String;
import Map;

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
		nameParts = [ (startsWith("<n>","\\") ? substring("<n>",1) : "<n>") | n <- nl];
		if (size(nameParts) > 1) {
			return RCompoundName(nameParts);
		} else {
			return RSimpleName(head(nameParts));
		} 
	}
	throw "Unexpected syntax for qualified name: <qn>";
}

public RName convertName(Name n) {
	if (startsWith("<n>","\\"))
		return RSimpleName(substring("<n>",1));
	else
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
  	| RADTType(RType adtName)
  	| RConstructorType(RName constructorName, RType adtType, list[RNamedType] elementTypes) 
  	| RFunctionType(RType returnType, list[RNamedType] parameterTypes)
  	| RNonTerminalType()
  	| RReifiedType(RType baseType)
  	| RDateTimeType()

	| RFailType(set[tuple[str failMsg, loc failLoc]])
	| RInferredType(int tnum)
	| ROverloadedType(set[ROverloadedType] possibleTypes)
	| RVarArgsType(RType vt)

        | RStatementType(RType internalType)

	| RAliasType(RType aliasName, RType aliasedType)

	| RDataTypeSelector(RName source, RName target)
	| RUserType(RName typeName)
	| RParameterizedUserType(RName typeName, list[RType] typeParams)
	| RTypeVar(RTypeVar tv)
	| RAssignableType(RType wholeType, RType partType)
	| RUnknownType(RType wrappedType)
	| RLubType(list[RType] lubOptions)
;

data ROverloadedType =
	  ROverloadedType(RType overloadType)
	| ROverloadedTypeWithLoc(RType overloadType, loc overloadLoc)
	;
	
data RNamedType =
	  RUnnamedType(RType typeArg)
	| RNamedType(RType typeArg, RName typeName)
;

data RTypeVar =
	  RFreeTypeVar(RName varName)
	| RBoundTypeVar(RName varName, RType varTypeBound)
;

//
// Convert basic types into their Rascal analogs. Add error information
// if needed -- for instance, if we just see list that is an error, since
// just list isn't a type, we need list[something].
//
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
		case `lex` : return RLexType();
		case `datetime` : return RDateTimeType();
		case `non-terminal` : return RNonTerminalType();

		case `list` : 
                        return RListType(RVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
		case `set` : 
                        return RSetType(RVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
		case `bag` : 
                        return RBagType(RVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
		case `map` : 
                        return RMapType(RUnnamedType(RVoidType()),RUnnamedType(RVoidType()))[@errinfo = <"Non-well-formed type, type should have two type arguments", t@\loc>];
		case `rel` : 
                        return RRelType([])[@errinfo = <"Non-well-formed type, type should have one or more type arguments", t@\loc>];
		case `tuple` : 
                        return RTupleType([])[@errinfo = <"Non-well-formed type, type should have one or more type arguments", t@\loc>];
		case `type` : 
                        return RReifiedType(RVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
		case `adt` : 
                        return RADTType(RUserType(RSimpleName("unnamedADT")))[@errinfo = <"Non-well-formed type", t@\loc>];
		case `parameter` : 
                        return RTypeVar(RFreeTypeVar(RSimpleName("unnamedVar")))[@errinfo = <"Non-well-formed type", t@\loc>];
		case `constructor` : 
                        return RConstructorType(RSimpleName("unnamedConstructor"),RUserType(RSimpleName("unnamedADT")),[])[@errinfo = <"Non-well-formed type", t@\loc>];
		case `fun` : 
                        return RFunctionType(RVoidType,[])[@errinfo = <"Non-well-formed type", t@\loc>];
		case `reified` : 
                        return RReifiedType(RVoidType())[@errinfo = <"Non-well-formed type", t@\loc>];
	}
}

//
// Convert type arguments into Rascal named types, which are types with an optional name.
//
public RNamedType convertTypeArg(TypeArg ta) {
	switch(ta) {
		case (TypeArg) `<Type t>` : return RUnnamedType(convertType(t));
		case (TypeArg) `<Type t> <Name n>` : return RNamedType(convertType(t),convertName(n));
	}
}

//
// Convenience function to convert an entire list of named type args.
//
public list[RNamedType] convertTypeArgList({TypeArg ","}* tas) {
	return [convertTypeArg(ta) | ta <- tas];
}

//
// Convert structured types, such as list[int]. Check here for certain
// syntactical conditions, such as: all field names must be distinct in
// a given type; lists require exactly one type argument.
//
public RType convertStructuredType(StructuredType st) {
	switch(st) {
		case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 1 && RUnnamedType(_) := l[0])
		                return RListType(getElementType(l[0]));
			else if (size(l) == 1 && RNamedType(_,_) := l[0])
		                return RListType(getElementType(l[0]))[@errinfo=<"A name cannot be given to the list element type",st@\loc>];
			else 
		                return RListType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a list",st@\loc>];
				
		}

		case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 1 && RUnnamedType(_) := l[0])
		                return RSetType(getElementType(l[0]));
			else if (size(l) == 1 && RNamedType(_,_) := l[0])
		                return RSetType(getElementType(l[0]))[@errinfo=<"A name cannot be given to the set element type",st@\loc>];
			else 
		                return RSetType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a set",st@\loc>];
		}

		case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 1 && RUnnamedType(_) := l[0])
		                return RBagType(getElementType(l[0]));
			else if (size(l) == 1 && RNamedType(_,_) := l[0])
		                return RBagType(getElementType(l[0]))[@errinfo=<"A name cannot be given to the bag element type",st@\loc>];
			else 
		                return RBagType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a bag",st@\loc>];
		}

		case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 2 && RUnnamedType(_) := l[0] && RUnnamedType(_) := l[1])
		                return RMapType(l[0],l[1]);
			else if (size(l) == 2 && RNamedType(_,n1) := l[0] && RNamedType(_,n2) := l[1] && n1 != n2)
		                return RMapType(l[0],l[1]);
			else if (size(l) == 2 && RNamedType(_,n1) := l[0] && RNamedType(_,n2) := l[1] && n1 == n2)
			        return RMapType(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"The names given to the type arguments must be distinct",st@\loc>];
			else if (size(l) == 2)
			        return RMapType(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"Names must either be given to both the domain and range or to neither",st@\loc>];
			else if (size(l) > 2)
			        return RMapType(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"Only two type arguments should be given",st@\loc>];
			else 
		                return RMapType(RUnnamedType(RVoidType()), RUnnamedType(RVoidType()))[@errinfo=<"Two type arguments must be given for a map",st@\loc>];
		}

		case (StructuredType) `rel [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]) == size(l))
			        return RRelType(l);
			else if (size({tn | n <- [0..size(l)-1], RNamedType(_,tn) := l[n]}) == size(l))
			        return RRelType(l);
			else if (size([n | n <- [0..size(l)-1], RNamedType(_,_) := l[n]]) == size(l))
			        return RRelType([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"If names are given to type arguments they must all be distinct.",st@\loc>];
			else
			        return RRelType([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"Named must be given either to all type arguments or to none.",st@\loc>];
		}

		case (StructuredType) `tuple [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]) == size(l))
			        return RTupleType(l);
			else if (size({tn | n <- [0..size(l)-1], RNamedType(_,tn) := l[n]}) == size(l))
			        return RTupleType(l);
			else if (size([n | n <- [0..size(l)-1], RNamedType(_,_) := l[n]]) == size(l))
			        return RTupleType([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"If names are given to type arguments they must all be distinct.",st@\loc>];
			else
			        return RTupleType([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"Named must be given either to all type arguments or to none.",st@\loc>];
		}

		case (StructuredType) `type [ < {TypeArg ","}+ tas > ]` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 1 && RUnnamedType(_) := l[0])
		                return RReifiedType(getElementType(l[0]));
			else if (size(l) == 1 && RNamedType(_,_) := l[0])
		                return RReifiedType(getElementType(l[0]))[@errinfo=<"A name cannot be given to the type element type",st@\loc>];
			else 
		                return RReifiedType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a type",st@\loc>];
		}

		case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : {
		        return RVoidType()[@errinfo=<"Type <bt> does not accept type parameters",st@\loc>];
		}
	}
}

public RType convertFunctionType(FunctionType ft) {
	switch(ft) {
		case (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )` : {
		        l = convertTypeArgList(tas);
			if (size(l) == 0)
			        return RFunctionType(convertType(t), [ ]);
		        else if (size(l) != size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]))
                                return RFunctionType(convertType(t),[RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"Names cannot be given to the arguments in a function type",ft@\loc>];
			else
                                return RFunctionType(convertType(t),convertTypeArgList(tas));
		}
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
	return joinList(tList, prettyPrintType, ", ", "");
}

public str prettyPrintTypeListWLoc(list[RType] tList) {
	return joinList(tList,str (RType t) { if ( (t@at)? ) { return "<prettyPrintType(t)> at <t@at>"; } else { return prettyPrintType(t); } }, ", ", "");
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
		case RMapType(dt,rt) : return "map[<prettyPrintNamedType(dt)>,<prettyPrintNamedType(rt)>]";
		case RRelType(nts) : return "rel[<prettyPrintNamedTypeList(nts)>]";
		case RTupleType(nts) : return "tuple[<prettyPrintNamedTypeList(nts)>]";
		case RLexType() : return "lex";
		case RADTType(n) : return "<prettyPrintType(n)>"; // TODO: Add more detail on the pretty printer
		case RConstructorType(cn, an, ets) : return "<prettyPrintType(an)>: <prettyPrintName(cn)>(<prettyPrintNamedTypeList(ets)>)";
		case RFunctionType(rt, pts) : return "<prettyPrintType(rt)> (<prettyPrintNamedTypeList(pts)>)";
		case RNonTerminalType() : return "non-terminal";
		case RReifiedType(rt) : return "type(<prettyPrintType(t)>)";
		case RDateTimeType() : return "datetime";
		case RFailType(sls) :  return "Failure: " + joinList(toList(sls),printLocMsgPair,", ","");
		case RInferredType(n) : return "Inferred Type: <n>";
		case ROverloadedType(pts) : return "Overloaded type, could be: " + prettyPrintTypeList([p.overloadType | p <- pts]);
		case RVarArgsType(vt) : return "<prettyPrintType(vt)>...";
		case RStatementType(rt) : return "Statement: <prettyPrintType(rt)>";
		case RAliasType(an,at) : return "Alias: <prettyPrintType(an)> = <prettyPrintType(at)>";
		case RDataTypeSelector(s,t) : return "Selector <s>.<t>";
		case RUserType(tn) : return "<prettyPrintName(tn)>";
		case RParameterizedUserType(tn, tps) : return "<prettyPrintName(tn)>[<prettyPrintTypeList(tps)>]";
		case RTypeVar(tv) : return prettyPrintTypeVar(tv);
		case RAssignableType(wt,pt) : return "Assignable type, whole <prettyPrintType(wt)>, part <prettyPrintType(pt)>";
		case RLocatedType(rlt,l) : return "Located type <prettyPrintType(rlt)> at location <l>";
		case RLubType(ll) : return "Unresolved LUB type: <prettyPrintTypeList(ll)>";
		default : return "Unhandled type <t>";
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
// Annotation for adding an alternate type to expressions
anno RType Tree@fctype;

//
// Annotation for adding locations to types
//
anno loc RType@at;

//
// Annotation for adding error information to types
//
anno tuple[str msg, loc at] RType@errinfo;

//
// Routines for dealing with bool types
//
public bool isBoolType(RType t) {
        if (RAliasType(_,at) := t) return isBoolType(at);
        if (RTypeVar(_) := t) return isBoolType(getTypeVarBound(t));
	return RBoolType() := t;
}

public RType makeBoolType() { return RBoolType(); }

//
// Routines for dealing with int types
//
public bool isIntType(RType t) {
        if (RAliasType(_,at) := t) return isIntType(at);
        if (RTypeVar(_) := t) return isIntType(getTypeVarBound(t));
	return RIntType() := t;
}

public RType makeIntType() { return RIntType(); }

//
// Routines for dealing with real types
//
public bool isRealType(RType t) {
        if (RAliasType(_,at) := t) return isRealType(at);
        if (RTypeVar(_) := t) return isRealType(getTypeVarBound(t));
	return RRealType() := t;
}

public RType makeRealType() { return RRealType(); }

//
// Routines for dealing with num types
//
public bool isNumType(RType t) {
        if (RAliasType(_,at) := t) return isNumType(at);
        if (RTypeVar(_) := t) return isNumType(getTypeVarBound(t));
	return RNumType() := t;
}

public RType makeNumType() { return RNumType(); }

//
// Routines for dealing with str types
//
public bool isStrType(RType t) {
        if (RAliasType(_,at) := t) return isStrType(at);
        if (RTypeVar(_) := t) return isStrType(getTypeVarBound(t));
	return RStrType() := t;
}

public RType makeStrType() { return RStrType(); }

//
// Routines for dealing with value types
//
public bool isValueType(RType t) {
        if (RAliasType(_,at) := t) return isValueType(at);
        if (RTypeVar(_) := t) return isValueType(getTypeVarBound(t));
	return RValueType() := t;
}

public RType makeValueType() { return RValueType(); }

//
// Routines for dealing with node types
//
public bool isNodeType(RType t) {
        if (RAliasType(_,at) := t) return isNodeType(at);
        if (RTypeVar(_) := t) return isNodeType(getTypeVarBound(t));
	return (RNodeType() := t || RADTType(_) := t);
}

public RType makeNodeType() { return RNodeType(); }
//
// Routines for dealing with void types
//
public bool isVoidType(RType t) {
        if (RAliasType(_,at) := t) return isVoidType(at);
        if (RTypeVar(_) := t) return isVoidType(getTypeVarBound(t));
	return RVoidType() := t;
}

public RType makeVoidType() { return RVoidType(); }

//
// Routines for dealing with loc types
//
public bool isLocType(RType t) {
        if (RAliasType(_,at) := t) return isLocType(at);
        if (RTypeVar(_) := t) return isLocType(getTypeVarBound(t));
	return RLocType() := t;
}

public RType makeLocType() { return RLocType(); }

//
// Routines for dealing with list types
//
public bool isListType(RType t) {
        if (RAliasType(_,at) := t) return isListType(at);
        if (RTypeVar(_) := t) return isListType(getTypeVarBound(t));
	return RListType(_) := t;
}

public RType makeListType(RType itemType) { return RListType(itemType); }

public RType getListElementType(RType t) {
        if (RAliasType(_,at) := t) return getListElementType(at);
        if (RTypeVar(_) := t) return getListElementType(getTypeVarBound(t));
	if (RListType(et) := t) return et;
	throw "Error: Cannot get list element type from type <prettyPrintType(t)>";
}

//
// Routines for dealing with set types
//
public bool isSetType(RType t) {
        if (RAliasType(_,at) := t) return isSetType(at);
        if (RTypeVar(_) := t) return isSetType(getTypeVarBound(t));
	return RSetType(_) := t || RRelType(_) := t;
}

public RType makeSetType(RType itemType) { return RSetType(itemType); }

public RType getSetElementType(RType t) {
        if (RAliasType(_,at) := t) return getSetElementType(at);
        if (RTypeVar(_) := t) return getSetElementType(getTypeVarBound(t));
	if (RSetType(et) := t) return et;
	if (RRelType(ets) := t) return RTupleType(ets);
	throw "Error: Cannot get set element type from type <prettyPrintType(t)>";
}

//
// Routines for dealing with bag types
//
public bool isBagType(RType t) {
        if (RAliasType(_,at) := t) return isBagType(at);
        if (RTypeVar(_) := t) return isBagType(getTypeVarBound(t));
	return RBagType(_) := t;
}

//
// Routines for dealing with map types
//
public bool isMapType(RType t) {
        if (RAliasType(_,at) := t) return isMapType(at);
        if (RTypeVar(_) := t) return isMapType(getTypeVarBound(t));
	return RMapType(_,_) := t;
}

public RType makeMapType(RType domainType, RType rangeType) { return RMapType(RUnnamedType(domainType), RUnnamedType(rangeType)); }

public RType makeMapTypeWithNames(RNamedType domainType, RNamedType rangeType) { return RMapType(domainType, rangeType); }

public RType makeMapTypeFromTuple(RType t) {
        if (RAliasType(_,at) := t) return makeMapTypeFromTuple(at);
        if (RTypeVar(_) := t) return makeMapTypeFromTuple(getTypeVarBound(t));
        if (RTupleType([lt,rt]) := t) return RMapType(lt,rt);
        throw "makeMapTypeFromTuple called with unexpected type <prettyPrintType(t)>";
}

public RType getMapFieldsAsTuple(RType t) {
        if (RAliasType(_,at) := t) return getMapFieldsAsTuple(at);
        if (RTypeVar(_) := t) return getMapFieldsAsTuple(getTypeVarBound(t));
	if (RMapType(lt,rt) := t) return RTupleType([lt,rt]);
        throw "getMapFieldsAsTuple called with unexpected type <prettyPrintType(t)>";
}       

@doc{Check to see if a map defines a field.}
public bool mapHasField(RType t, RName fn) {
        if (RAliasType(_,at) := t) return mapHasField(at,fn);
        if (RTypeVar(_) := t) return mapHasField(getTypeVarBound(t),fn);
	if (RMapType(tl,tr) := t) {
		if (RNamedType(_,fn) := tl) return true;	
		if (RNamedType(_,fn) := tr) return true;	
	}
	return false;
}

@doc{Return the type of a field defined on a map.}
public RType getMapFieldType(RType t, RName fn) {
        if (RAliasType(_,at) := t) return getMapFieldType(at,fn);
        if (RTypeVar(_) := t) return getMapFieldType(getTypeVarBound(t),fn);
	if (RMapType(tl,tr) := t) {
		if (RNamedType(ft,fn) := tl) return ft;	
		if (RNamedType(ft,fn) := tr) return ft;	
	}
	throw "Map <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public list[RType] getMapFields(RType t) {
        if (RAliasType(_,at) := t) return getMapFields(at);
        if (RTypeVar(_) := t) return getMapFields(getTypeVarBound(t));
	if (RMapType(tl, tr) := t) {
		return [ getElementType(tl), getElementType(tr) ];
	}
	throw "Cannot get map fields from type <prettyPrintType(t)>";	
}

public list[RNamedType] getMapFieldsWithNames(RType t) {
        if (RAliasType(_,at) := t) return getMapFieldsWithNames(at);
        if (RTypeVar(_) := t) return getMapFieldsWithNames(getTypeVarBound(t));
	if (RMapType(tl, tr) := t) {
		return [ tl, tr ];
	}
	throw "Cannot get map fields from type <prettyPrintType(t)>";	
}

public bool mapHasFieldNames(RType t) {
        if (RAliasType(_,at) := t) return mapHasFieldNames(at);
        if (RTypeVar(_) := t) return mapHasFieldNames(getTypeVarBound(t));
        if (RMapType(tl, tr) := t) {
	        return (namedTypeHasName(tl) && namedTypeHasName(tr));
	}
	throw "mapHasFieldNames given non-Map type <prettyPrintType(t)>";
}

public tuple[RName domainName, RName rangeName] getMapFieldNames(RType t) {
        if (RAliasType(_,at) := t) return getMapFieldNames(at);
        if (RTypeVar(_) := t) return getMapFieldNames(getTypeVarBound(t));
        if (RMapType(tl, tr) := t) {
	        if (mapHasFieldNames(t)) {
		        return < getTypeName(tl), getTypeName(tr) >;
		}
		throw "getMapFieldNames given map type without field names: <prettyPrintType(t)>";        
        }
        throw "getMapFieldNames given non-Map type <prettyPrintType(t)>";
}

public RType getMapDomainType(RType t) {
        if (RAliasType(_,at) := t) return getMapDomainType(at);
        if (RTypeVar(_) := t) return getMapDomainType(getTypeVarBound(t));
	if (RMapType(tl,_) := t) return getElementType(tl);
	throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

public RType getMapRangeType(RType t) {
        if (RAliasType(_,at) := t) return getMapRangeType(at);
        if (RTypeVar(_) := t) return getMapRangeType(getTypeVarBound(t));
	if (RMapType(_,tr) := t) return getElementType(tr);
	throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

//
// Routines for dealing with rel types
//
public bool isRelType(RType t) {
        if (RAliasType(_,at) := t) return isRelType(at);
        if (RTypeVar(_) := t) return isRelType(getTypeVarBound(t));
        if (RRelType(_) := t) return true;
        if (RSetType(RTupleType(_)) := t) return true;
        return false;
}

public RType makeRelType(list[RType] its) { return RRelType([ RUnnamedType( t ) | t <- its ]); }

public RType makeRelTypeFromTuple(RType t) { return RRelType(getTupleFieldsWithNames(t)); }

public RType getRelElementType(RType t) {
        if (RAliasType(_,at) := t) return getRelElementType(at);
        if (RTypeVar(_) := t) return getRelElementType(getTypeVarBound(t));
	if (RRelType(ets) := t)
		return RTupleType(ets);
	if (RSetType(RTupleType(ets)) := t)
		return RTupleType(ets);
	throw "Error: Cannot get relation element type from type <prettyPrintType(t)>";
}

public bool relHasFieldNames(RType t) {
        if (RAliasType(_,at) := t) return relHasFieldNames(at);
        if (RTypeVar(_) := t) return relHasFieldNames(getTypeVarBound(t));
        if (RRelType(tls) := t || RSetType(RTupleType(tls)) := t) {
                return size(tls) == size([n | n <- [0..size(tls)-1], namedTypeHasName(tls[n])]);
        }
        throw "relHasFieldNames given non-Relation type <prettyPrintType(t)>";
}

public list[RName] getRelFieldNames(RType t) {
        if (RAliasType(_,at) := t) return getRelFieldNames(at);
        if (RTypeVar(_) := t) return getRelFieldNames(getTypeVarBound(t));
        if (RRelType(tls) := t || RSetType(RTupleType(tls)) := t) {
	        if (relHasFieldNames(t)) {
		        return [ getTypeName(tli) | tli <- tls ];
		}
		throw "getRelFieldNames given rel type without field names: <prettyPrintType(t)>";        
        }
        throw "getRelFieldNames given non-Relation type <prettyPrintType(t)>";
}

//
// Routines for dealing with tuple types
//
public bool isTupleType(RType t) {
        if (RAliasType(_,at) := t) return isTupleType(at);
        if (RTypeVar(_) := t) return isTupleType(getTypeVarBound(t));
	return RTupleType(_) := t;
}

public RType makeTupleType(list[RType] its) { 	return RTupleType([ RUnnamedType( t ) | t <- its ]); }

public RType makeTupleTypeWithNames(list[RNamedType] its) { return RTupleType(its); }

public bool tupleHasField(RType t, RName fn) {
        if (RAliasType(_,at) := t) return tupleHasField(at,fn);
        if (RTypeVar(_) := t) return tupleHasField(getTypeVarBound(t),fn);
	if (RTupleType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(_,fn) := ta) return true;	
		}
	}
	return false;
}

public RType getTupleFieldType(RType t, RName fn) {
        if (RAliasType(_,at) := t) return getTupleFieldName(at,fn);
        if (RTypeVar(_) := t) return getTupleFieldName(getTypeVarBound(t),fn);
	if (RTupleType(tas) := t) {
		for (ta <- tas) {
			if (RNamedType(ft,fn) := ta) return ft;	
		}
	}
	throw "Tuple <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
}

public list[RType] getTupleFields(RType t) {
        if (RAliasType(_,at) := t) return getTupleFields(at);
        if (RTypeVar(_) := t) return getTupleFields(getTypeVarBound(t));
	if (RTupleType(tas) := t) {
		return [ getElementType(ta) | ta <- tas ];
	}
	throw "Cannot get tuple fields from type <prettyPrintType(t)>";	
}

public list[RNamedType] getTupleFieldsWithNames(RType t) {
        if (RAliasType(_,at) := t) return getTupleFieldsWithNames(at);
        if (RTypeVar(_) := t) return getTupleFieldsWithNames(getTypeVarBound(t));
	if (RTupleType(tas) := t) {
		return tas;
	}
	throw "Cannot get tuple fields from type <prettyPrintType(t)>";	
}

public int getTupleFieldCount(RType t) {
        if (RAliasType(_,at) := t) return getTupleFieldCount(at);
        if (RTypeVar(_) := t) return getTupleFieldCount(getTypeVarBound(t));
	if (RTupleType(tas) := t) {
		return size(tas);
	}
	throw "Cannot get tuple field count from type <prettyPrintType(t)>";	
}

public bool tupleHasFieldNames(RType t) {
        if (RAliasType(_,at) := t) return tupleHasFieldNames(at);
        if (RTypeVar(_) := t) return tupleHasFieldNames(getTypeVarBound(t));
        if (RTupleType(tls) := t) {
                return size(tls) == size([n | n <- [0..size(tls)-1], namedTypeHasName(tls[n])]);
        }
        throw "tupleHasFieldNames given non-Tuple type <prettyPrintType(t)>";
}

public list[RName] getTupleFieldNames(RType t) {
        if (RAliasType(_,at) := t) return getTupleFieldNames(at);
        if (RTypeVar(_) := t) return getTupleFieldNames(getTypeVarBound(t));
        if (RTupleType(tls) := t) {
	        if (tupleHasFieldNames(t)) {
		        return [ getTypeName(tli) | tli <- tls ];
		}
		throw "getTupleFieldNames given tuple type without field names: <prettyPrintType(t)>";        
        }
        throw "getTupleFieldNames given non-Tuple type <prettyPrintType(t)>";
}

//
// Routines for dealing with lex types
//
public bool isLexType(RType t) {
        if (RAliasType(_,at) := t) return isLexType(at);
        if (RTypeVar(_) := t) return isLexType(getTypeVarBound(t));
	return RLexType() := t;
}

//
// Routines for dealing with adt types
//
public bool isADTType(RType t) {
        if (RAliasType(_,at) := t) return isADTType(at);
        if (RTypeVar(_) := t) return isADTType(getTypeVarBound(t));
	return RADTType(_) := t;
}

public RType makeADTType(RName n) {
       return RADTType(RUserType(n));
}

public RType makeParameterizedADTType(RName n, list[RType] p) {
       return RADTType(RParameterizedUserType(n,p));
}

public RName getADTName(RType t) {
        if (RAliasType(_,at) := t) return getADTName(at);
        if (RTypeVar(_) := t) return getADTName(getTypeVarBound(t));
	if (RADTType(ut) := t) return getUserTypeName(ut);
	if (RConstructorType(_,RADTType(ut),_) := t) return getUserTypeName(ut);
	throw "getADTName, invalid type given: <prettyPrintType(t)>";
}

public bool adtHasTypeParameters(RType t) {
        if (RAliasType(_,at) := t) return adtHasTypeParameters(at);
        if (RTypeVar(_) := t) return adtHasTypeParameters(getTypeVarBound(t));
        if (RADTType(ut) := t) return userTypeHasParameters(ut);
        if (RConstructorType(_,RADTType(ut),_)) return userTypeHasParameters(ut);
        throw "ADTHasTypeParameters given non-ADT type <prettyPrintType(t)>";
}

public list[RType] getADTTypeParameters(RType t) {
        if (RAliasType(_,at) := t) return getADTTypeParameters(at);
        if (RTypeVar(_) := t) return getADTTypeParameters(getTypeVarBound(t));
        if (RADTType(ut) := t) return getUserTypeParameters(ut);
        if (RConstructorType(_,RADTType(ut),_)) return getUserTypeParameters(ut);
        throw "getADTTypeParameters given non-ADT type <prettyPrintType(t)>";
}

//
// Routines for dealing with constructor types
//
public bool isConstructorType(RType t) {
        if (RAliasType(_,at) := t) return isConstructorType(at);
        if (RTypeVar(_) := t) return isConstructorType(getTypeVarBound(t));
	return RConstructorType(_,_,_) := t;
}

public RType makeConstructorType(RName consName, RType adtType, list[RNamedType] consArgs) { 	
	return RConstructorType(consName, adtType, consArgs); 
}

public list[RType] getConstructorArgumentTypes(RType ct) {
        if (RAliasType(_,at) := ct) return getConstructorArgumentTypes(at);
        if (RTypeVar(_) := ct) return getConstructorArgumentTypes(getTypeVarBound(ct));
	if (RConstructorType(_,_,cts) := ct) return [ getElementType(argType) | argType <- cts ]; 
	throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public list[RNamedType] getConstructorArgumentTypesWithNames(RType ct) {
        if (RAliasType(_,at) := ct) return getConstructorArgumentTypesWithNames(at);
        if (RTypeVar(_) := ct) return getConstructorArgumentTypesWithNames(getTypeVarBound(ct));
	if (RConstructorType(_,_,cts) := ct) return [ argType | argType <- cts ]; 
	throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public RType getConstructorResultType(RType ct) {
        if (RAliasType(_,at) := ct) return getConstructorResultType(at);
        if (RTypeVar(_) := ct) return getConstructorResultType(getTypeVarBound(ct));
	if (RConstructorType(cn, an, cts) := ct) return an;
	throw "Cannot get constructor ADT type from non-constructor type <prettyPrintType(ct)>";
}

public RName getConstructorName(RType ct) {
        if (RAliasType(_,at) := ct) return getConstructorName(at);
        if (RTypeVar(_) := ct) return getConstructorName(getTypeVarBound(ct));
	if (RConstructorType(n,_,_) := ct) return n;
	throw "Cannot get constructor name from non-constructor type <prettyPrintType(ct)>";
}

//
// Routines for dealing with function types
//
public bool isFunctionType(RType t) {
        if (RAliasType(_,at) := t) return isFunctionType(at);
        if (RTypeVar(_) := t) return isFunctionType(getTypeVarBound(t));
	return RFunctionType(_,_)  := t;
}

public RType makeFunctionType(RType retType, list[RType] paramTypes) { return RFunctionType(retType, [ RUnnamedType( x ) | x <- paramTypes ]); }

public RType makeFunctionTypeWithNames(RType retType, list[RNamedType] paramTypes) { return RFunctionType(retType, paramTypes); }

public list[RType] getFunctionArgumentTypes(RType ft) {
        if (RAliasType(_,at) := ft) return getFunctionArgumentTypes(at);
        if (RTypeVar(_) := ft) return getFunctionArgumentTypes(getTypeVarBound(ft));
	if (RFunctionType(_, ats) := ft) return [ getElementType(argType) | argType <- ats ];
	throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public list[RNamedType] getFunctionArgumentTypesWithNames(RType ft) {
        if (RAliasType(_,at) := ft) return getFunctionArgumentTypesWithNames(at);
        if (RTypeVar(_) := ft) return getFunctionArgumentTypesWithNames(getTypeVarBound(ft));
	if (RFunctionType(_, ats) := ft) return ats;
	throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public RType getFunctionReturnType(RType ft) {
        if (RAliasType(_,at) := ft) return getFunctionReturnType(at);
        if (RTypeVar(_) := ft) return getFunctionReturnType(getTypeVarBound(ft));
	if (RFunctionType(retType, _) := ft) return retType; 
	throw "Cannot get function return type from non-function type <prettyPrintType(ft)>";
}

public bool isVarArgsFun(RType t) {
        if (RAliasType(_,at) := t) return isVarArgsFun(at);
        if (RTypeVar(_) := t) return isVarArgsFun(getTypeVarBound(t));
	if (RFunctionType(_,ps) := t) {
		if (size(ps) > 0) {
			if (isVarArgsType(getElementType(head(tail(ps,1))))) {
				return true;
			}
		}
	}
	return false;
}

//
// Routines for dealing with nonterminal types
//
public bool isNonTerminalType(RType t) {
       return RNonTerminalType(_) := t;
}

//
// Routines for dealing with reified types
//
public bool isReifiedType(RType t) {
        if (RAliasType(_,at) := t) return isReifiedType(at);
        if (RTypeVar(_) := t) return isReifiedType(getTypeVarBound(t));
	return RReifiedType(_) := t;
}

public RType getReifiedType(RType t) {
       if (RAliasType(_,at) := t) return getReifiedType(at);
       if (RTypeVar(_) := t) return getReifiedType(getTypeVarBound(t));
       if (RReifiedType(rt) := t) return rt;
       throw "getReifiedType given unexpected type: <prettyPrintType(t)>";
}

public RType makeReifiedType(RType mainType) { return RReifiedType(mainType); }

//
// Routines for dealing with datetime types
//
public bool isDateTimeType(RType t) {
        if (RAliasType(_,at) := t) return isDateTimeType(at);
        if (RTypeVar(_) := t) return isDateTimeType(getTypeVarBound(t));
	return RDateTimeType() := t;
}

public RType makeDateTimeType() { return RDateTimeType(); }

//
// Routines for dealing with container types
//
public bool isContainerType(RType t) {
	return RContainerType(_) := t;
}

public RType makeContainerType(RType itemType) { return RContainerType(itemType); }

public RType getContainerElementType(RType t) {
	if (RContainerType(et) := t) return et;
	throw "Error: Cannot get container element type from type <prettyPrintType(t)>";
}

//
// Routines for dealing with fail types
//
public bool isFailType(RType t) {
	return RFailType(_) := t; 
}

public RType makeFailType(str s, loc l) { return RFailType({<s,l>}); }

public RType extendFailType(RType ft, set[tuple[str s, loc l]] sls) {
	if (RFailType(sls2) := ft) {
		return RFailType(sls2 + { < e.s, e.l > | e <- sls });
	}
	throw "Cannot extend a non-failure type with failure information, type <prettyPrintType(ft)>";
}
 
public RType collapseFailTypes(set[RType] rt) { 
	return RFailType({ s | RFailType(ss) <- rt, s <- ss }); 
}

// TODO: Come up with a less stupid name for this
public RType makeBiggerFailType(RType ft, set[tuple[str s, loc l]] sls) { return RFailType({ < e.s, e.l > | e <- sls }); }

//
// Routines for dealing with inferred types
//
public bool isInferredType(RType t) {
	return RInferredType(_) := t;
}

public RType makeInferredType(int n) { return RInferredType(n); }

public int getInferredTypeIndex(RType t) {
	if (RInferredType(n) := t) return n;
	throw "Error: Cannot get inferred type index from non-inferred type <prettyPrintType(t)>";
}

public bool hasDeferredTypes(RType rt) {
	return (size ( [ tt | tt:/RLubType(_) <- rt ] + [ tt | tt:/RInferredType(_) <- rt] ) > 0);
}

//
// Routines for dealing with overloaded types
//
public bool isOverloadedType(RType t) {
	return ROverloadedType(_) := t;
}

public set[ROverloadedType] getOverloadOptions(RType t) {
	if (ROverloadedType(s) := t) return s;
	throw "Error: Cannot get overloaded options from non-overloaded type <prettyPrintType(t)>";
}

//
// Routines for dealing with varargs types
//
public bool isVarArgsType(RType t) {
	return RVarArgsType(_) := t;
}

public RType makeVarArgsType(RType t) { return RVarArgsType(t); }

public RType getVarArgsType(RType t) {
	if (RVarArgsType(vt) := t) return vt;
	throw "Cannot return var args type for type <prettyPrintType(t)>";
}

//
// Routines for dealing with statement types
//
public bool isStatementType(RType t) {
	return RStatementType(_) := t;
}

public RType makeStatementType(RType rt) { return RStatementType(rt); }

public RType getInternalStatementType(RType st) {
	if (RStatementType(rt) := st) return rt;
	throw "Cannot get internal statement type from type <prettyPrintType(st)>";
}

//
// Routines for dealing with alias types
//
public bool isAliasType(RType t) {
       return RAliasType(_,_) := t;
}

public RName getAliasName(RType t) {
	if (RAliasType(ut,_) := t) return getUserTypeName(ut);
	throw "getAliasName, invalid type given: <prettyPrintType(t)>";
}

public RType getAliasedType(RType t) {
       if (RAliasType(_,at) := t) return at;
       throw "getAliasedType, invalid type given: <prettyPrintType(t)>";
}

public RType makeAliasType(RName n, RType t) {
       return RAliasType(RUserType(n),t);
}

public RType makeParameterizedAliasType(RName n, RType t, list[RType] p) {
       return RAliasType(RParameterizedUserType(n,p),t);
}

public bool aliasHasTypeParameters(RType t) {
       if (RAliasType(ut,_) := t) return userTypeHasParameters(ut);
       throw "AliasHasTypeParameters given non-alias type <prettyPrintType(t)>";
}

public list[RType] getAliasTypeParameters(RType t) {
       if (RAliasType(ut,_) := t) return getUserTypeParameters(ut);
       throw "getAliasTypeParameters given non-alias type <prettyPrintType(t)>";
}

public RType unwindAliases(RType t) {
	return visit(t) { case RAliasType(tl,tr) => tr };
}

//
// Routines for dealing with data type selector types
//
public bool isDataTypeSelectorType(RType t) {
       return RDataTypeSelector(_,_) := t;
}

//
// Routines for dealing with user types
//
public RName getUserTypeName(RType ut) {
        if (RUserType(x) := ut || RParameterizedUserType(x,_) := ut) return x;
        throw "Cannot get user type name from non user type <prettyPrintType(ut)>";
} 

public bool userTypeHasParameters(RType ut) {
        return (RParameterizedUserType(_,ps) := ut && size(ps) > 0);
}

public list[RType] getUserTypeParameters(RType ut) {
       if (RParameterizedUserType(_,ps) := ut) return ps;
       throw "error in getUserTypeParameters: given unparameterized type <prettyPrintType(ut)>";
}

//
// Routines for dealing with parameterized user types
//

//
// Routines for dealing with type var types
//
public bool isTypeVar(RType t) {
	return RTypeVar(_) := t;
}

public RType makeTypeVar(RName varName) {
       return RTypeVar(RFreeTypeVar(varName));
}

public RType makeTypeVarWithBound(RName varName, RType varBound) {
       return RTypeVar(RBoundTypeVar(varName, varBound));
}

public RName getTypeVarName(RType t) {
        if (RTypeVar(RFreeTypeVar(n)) := t || RTypeVar(RBoundTypeVar(n,_)) := t) return n;
	throw "getTypeVarName given unexpected type: <prettyPrintType(t)>";
}

public bool typeVarHasBound(RType t) {
        if (RTypeVar(RBoundTypeVar(_,_)) := t) return true;
	if (RTypeVar(RFreeTypeVar(_)) := t) return false;
	throw "typeVarHasBound given unexpected type: <prettyPrintType(t)>";
}

public RType getTypeVarBound(RType t) {
	if (RTypeVar(RFreeTypeVar(_)) := t) return makeValueType();
	if (RTypeVar(RBoundTypeVar(_,bt)) := t) return bt;
	throw "getTypeVarBound given unexpected type: <prettyPrintType(t)>";
}

public set[RType] collectTypeVars(RType t) {
	return { rt | / RType rt : RTypeVar(RTypeVar v) <- t };
}

public bool typeContainsTypeVars(RType t) {
	return size(collectTypeVars(t)) > 0;
}

public set[RName] typeVarNames(RType t) {
        return { n | tv <- collectTypeVars(t), RTypeVar(RFreeTypeVar(n)) := tv || RTypeVar(RBoundTypeVar(n,_)) := tv };
}

//
// Instantiate type variables based on a var name to type mapping.
//
// NOTE: We assume that bounds have already been checked, so this should not violate the bounds
// given on bounded type variables.
//
// NOTE: Commented out for now. Unfortunately, the visit could change some things that we normally
// would not change, so we need to instead do this using standard recursion. It is now in SubTypes
// along with the functionality which finds the mappings.
//public RType instantiateVars(map[RName,RType] varMappings, RType rt) {
//	return visit(rt) {
//		case RTypeVar(RFreeTypeVar(n)) : if (n in varMappings) insert(varMappings[n]);
//		case RTypeVar(RBoundTypeVar(n,_)) : if (n in varMappings) insert(varMappings[n]);	
//	};
//}

//
// Routines for dealing with assignable types
//
public bool isAssignableType(RType t) {
	return RAssignableType(_,_) := t;
}

public RType makeAssignableType(RType wt, RType pt) { return RAssignableType(wt,pt); } 

public RType getWholeType(RType rt) {
	if (RAssignableType(wt,_) := rt) return wt;
	throw "Expected assignable type, got <prettyPrintType(rt)> instead.";
}

public RType getPartType(RType rt) {
	if (RAssignableType(_,pt) := rt) return pt;
	throw "Expected assignable type, got <prettyPrintType(rt)> instead.";
}

//
// Routines for dealing with unknown types
//
public bool isUnknownType(RType rt) {
       return RUnknownType(_) := rt;
}

public RType getUnknownType(RType rt) {
       if (RUnknownType(t) := rt) return t;
       throw "getUnknownType given unexpected type: <prettyPrintType(rt)>";
}

//
// Routines for dealing with lub types
//
public bool isLubType(RType t) {
       return RLubType(_) := t;
}


public RType makeLubType(list[RType] tl) {
       return RLubType(tl);
}

//
// Routines for dealing with named types
//
public RType getElementType(RNamedType t) {
       if (RUnnamedType(rt) := t || RNamedType(rt,_) := t) return rt;
       throw "getElementType given unexpected type <t>";
}

public bool namedTypeHasName(RNamedType t) {
       return RNamedType(_,_) := t;
}

public RName getTypeName(RNamedType t) {
       if (RNamedType(_,n) := t) return n;
       throw "Cannot get type name on an unnamed type";
}
