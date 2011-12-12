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
module lang::rascal::types::Types

import List;
import Set;
import IO;
import ParseTree;
import String;
import Map;

import lang::rascal::checker::ListUtils;
import lang::rascal::syntax::RascalRascal;


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
			return RCompoundName(nameParts)[@at = qn@\loc];
		} else {
			return RSimpleName(head(nameParts))[@at = qn@\loc];
		} 
	}
	throw "Unexpected syntax for qualified name: <qn>";
}

public RName convertName(Name n) {
	if (startsWith("<n>","\\"))
		return RSimpleName(substring("<n>",1))[@at = n@\loc];
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
// Abstract syntax for tag kinds
//
data RKind = FunctionKind() | VariableKind() | AllKind() | AnnoKind() | DataKind() |
             ViewKind() | RuleKind() | AliasKind() | ModuleKind() | TagKind();
             
public RKind convertKind(Kind k) {
    switch(k) {
        case (Kind) `function` : return FunctionKind();
        case (Kind) `variable` : return VariableKind();
        case (Kind) `all` : return AllKind();
        case (Kind) `anno` : return AnnoKind();
        case (Kind) `data` : return DataKind();
        case (Kind) `view` : return ViewKind();
        case (Kind) `rule` : return RuleKind();
        case (Kind) `alias` : return AliasKind();
        case (Kind) `module` : return ModuleKind();
        case (Kind) `tag` : return TagKind();
    }
    throw "convertKind, error, no match for kind <k>";
}

public str prettyPrintKind(RKind rk) {
    switch(rk) {
        case FunctionKind() : return "function";
        case VariableKind() : return "variable";
        case AllKind() : return "all";
        case AnnoKind() : return "anno";
        case DataKind() : return "data";
        case ViewKind() : return "view";
        case RuleKind() : return "rule";
        case AliasKind() : return "alias";
        case ModuleKind() : return "module";
        case TagKind() : return "tag";
    }
    throw "prettyPrintKind, error, no match for rkind <rk>";
}

//
// Abstract syntax for types
//
// An abstract representation of Rascal types. These are given in
// the same order as the BasicType nonterminal in RascalRascal.
//
data RType =
      RValueType()
    | RLocType()
    | RNodeType()
    | RNumType()
    | RReifiedType(RType baseType)
    | RBagType(RType elementType)
    | RIntType()
    | RRatType()
    | RRelType(list[RNamedType] elementTypes) 
    | RTypeVar(RName varName, RType varTypeBound)
    | RRealType()
    | RFunctionType(RType returnType, list[RNamedType] parameterTypes, bool isVarArgs)
    | RTupleType(list[RNamedType] elementTypes) 
    | RStrType()
    | RBoolType()
    | RReifiedReifiedType(RType baseType)
    | RVoidType()
    | RNonTerminalType()
    | RDateTimeType()
    | RSetType(RType elementType)
    | RMapType(RNamedType domainType, RNamedType rangeType)
    | RConstructorType(RName constructorName, RType adtType, list[RNamedType] elementTypes) 
    | RListType(RType elementType)
    | RADTType(RName adtName, list[RType] adtParameters)
    ;
  
//
// Next, we add the types that are constructed from the above, including
// user types and aliases.
//
data RType =
      RUserType(RName typeName,list[RType] typeParams)
    | RAliasType(RName aliasName, list[RType] typeParams, RType aliasedType)
    | RDataTypeSelector(RName source, RName target)
    ;  
//
// Now, we define additional types used internally by the checker.
//
// - RFailType represents a failure detected during name resolution or type checking
// - RInferredType represents inferred types, which will eventually be bound to a real type
// - ROverloadedType represents sets of types, for instance the type of a function name when
//   that name is overloaded
//
data RType =
      RFailType(set[tuple[str failMsg, loc failLoc]])
    | RInferredType(int tnum)
    | ROverloadedType(set[RType] possibleTypes)
    ;

//
// The Abstract version of the TypeArg nonterminal, allowing types
// with optional names.	
//
data RNamedType =
      RUnnamedType(RType typeArg)
    | RNamedType(RType typeArg, RName typeName)
    ;

//
// Annotation for adding types to trees
//
anno RType Tree@rtype; 

//
// Annotation for adding locations to types and names
//
anno loc RType@at;
anno loc RName@at;

//
// Annotations for adding error and warning information to types
//
anno tuple[str msg, loc at] RType@errinfo;
anno tuple[str msg, loc at] RType@warninfo;

//
// Convert basic types into their Rascal analogs. Add error information
// if needed -- for instance, if we just see list that is an error, since
// just list isn't a type, we need list[something]. The cases are handled
// in two batches, first the non-error cases, then the error cases.
//
public RType convertBasicType(BasicType t) {
    switch(t) {
        case (BasicType)`bool` : return makeBoolType();
        case (BasicType)`int` : return makeIntType();
        case (BasicType)`rat` : return makeRatType();
        case (BasicType)`real` : return makeRealType();
        case (BasicType)`num` : return makeNumType();
        case (BasicType)`str` : return makeStrType();
        case (BasicType)`value` : return makeValueType();
        case (BasicType)`node` : return makeNodeType();
        case (BasicType)`void` : return makeVoidType();
        case (BasicType)`loc` : return makeLocType();
        case (BasicType)`datetime` : return makeDateTimeType();
        case (BasicType)`non-terminal` : return makeNonTerminalType();

        case (BasicType)`list` : 
            return makeListType(makeVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
        case (BasicType)`set` : 
            return makeSetType(makeVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
        case (BasicType)`bag` : 
            return makeBagType(makeVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
        case (BasicType)`map` : 
            return makeMapType(makeVoidType(),makeVoidType())[@errinfo = <"Non-well-formed type, type should have two type arguments", t@\loc>];
        case (BasicType)`rel` : 
            return makeRelType()[@errinfo = <"Non-well-formed type, type should have one or more type arguments", t@\loc>];
        case (BasicType)`tuple` : 
            return makeTupleType()[@errinfo = <"Non-well-formed type, type should have one or more type arguments", t@\loc>];
        case (BasicType)`type` : 
            return makeReifiedType(makeVoidType())[@errinfo = <"Non-well-formed type, type should have one type argument", t@\loc>];
        case (BasicType)`adt` : 
            return makeADTType(RSimpleName("unnamedADT"))[@errinfo = <"Non-well-formed type", t@\loc>];
        case (BasicType)`parameter` : 
            return makeTypeVar(RSimpleName("unnamedVar"))[@errinfo = <"Non-well-formed type", t@\loc>];
        case (BasicType)`constructor` : 
            return makeConstructorType(RSimpleName("unnamedConstructor"),makeADTType(RSimpleName("unnamedADT")),[])[@errinfo = <"Non-well-formed type", t@\loc>];
        case (BasicType)`fun` : 
            return makeFunctionType(makeVoidType(),[],false)[@errinfo = <"Non-well-formed type", t@\loc>];
        case (BasicType)`reified` : 
            return makeReifiedReifiedType(makeVoidType())[@errinfo = <"Non-well-formed type", t@\loc>];
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
// a given type; lists require exactly one type argument; etc.
//
public RType convertStructuredType(StructuredType st) {
    switch(st) {
        case (StructuredType) `list [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1 && RUnnamedType(_) := l[0])
                return makeListType(getElementType(l[0]));
            else if (size(l) == 1 && RNamedType(_,_) := l[0])
                return makeListType(getElementType(l[0]));
            else 
                return makeListType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a list",st@\loc>];
        }

        case (StructuredType) `set [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1 && RUnnamedType(_) := l[0])
                return makeSetType(getElementType(l[0]));
            else if (size(l) == 1 && RNamedType(_,_) := l[0])
                return makeSetType(getElementType(l[0]));
            else 
                return makeSetType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a set",st@\loc>];
        }

        case (StructuredType) `bag [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1 && RUnnamedType(_) := l[0])
                return makeBagType(getElementType(l[0]));
            else if (size(l) == 1 && RNamedType(_,_) := l[0])
                return makeBagType(getElementType(l[0]));
            else 
                return makeBagType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a bag",st@\loc>];
        }

        case (StructuredType) `map [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 2 && RUnnamedType(_) := l[0] && RUnnamedType(_) := l[1])
                return makeMapTypeWithNames(l[0],l[1]);
            else if (size(l) == 2 && RNamedType(_,n1) := l[0] && RNamedType(_,n2) := l[1] && n1 != n2)
                return makeMapTypeWithNames(l[0],l[1]);
            else if (size(l) == 2 && RNamedType(_,n1) := l[0] && RNamedType(_,n2) := l[1] && n1 == n2)
                return makeMapTypeWithNames(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"The names given to the type arguments must be distinct",st@\loc>];
            else if (size(l) == 2)
                return makeMapTypeWithNames(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"Names must either be given to both the domain and range or to neither",st@\loc>];
            else if (size(l) > 2)
                return makeMapTypeWithNames(RUnnamedType(getElementType(l[0])),RUnnamedType(getElementType(l[0])))[@errinfo=<"Only two type arguments should be given",st@\loc>];
            else 
                return makeMapTypeWithNames(RUnnamedType(RVoidType()), RUnnamedType(RVoidType()))[@errinfo=<"Two type arguments must be given for a map",st@\loc>];
        }

        case (StructuredType) `rel [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]) == size(l))
                return makeRelTypeFromTuple(makeTupleTypeWithNames(l));
            else if (size({tn | n <- [0..size(l)-1], RNamedType(_,tn) := l[n]}) == size(l))
                return makeRelTypeFromTuple(makeTupleTypeWithNames(l));
            else if (size([n | n <- [0..size(l)-1], RNamedType(_,_) := l[n]]) == size(l))
                return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(li)) | li <- l]))[@errinfo=<"If names are given to type arguments they must all be distinct.",st@\loc>];
            else
                return makeRelTypeFromTuple(makeTupleTypeWithNames([RUnnamedType(getElementType(li)) | li <- l]))[@errinfo=<"Names must be given either to all type arguments or to none.",st@\loc>];
        }

        case (StructuredType) `tuple [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]) == size(l))
                return makeTupleTypeWithNames(l);
            else if (size({tn | n <- [0..size(l)-1], RNamedType(_,tn) := l[n]}) == size(l))
                return makeTupleTypeWithNames(l);
            else if (size([n | n <- [0..size(l)-1], RNamedType(_,_) := l[n]]) == size(l))
                return makeTupleTypeWithNames([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"If names are given to type arguments they must all be distinct.",st@\loc>];
            else
                return makeTupleTypeWithNames([RUnnamedType(getElementType(li)) | li <- l])[@errinfo=<"Names must be given either to all type arguments or to none.",st@\loc>];
        }

        case (StructuredType) `type [ < {TypeArg ","}+ tas > ]` : {
            l = convertTypeArgList(tas);
            if (size(l) == 1 && RUnnamedType(_) := l[0])
                return makeReifiedType(getElementType(l[0]));
            else if (size(l) == 1 && RNamedType(_,_) := l[0])
                return makeReifiedType(getElementType(l[0]));
            else 
                return makeReifiedType(getElementType(l[0]))[@errinfo=<"Exactly one element type must be given for a type",st@\loc>];
        }

		case (StructuredType) `<BasicType bt> [ < {TypeArg ","}+ tas > ]` : {
		        return makeVoidType()[@errinfo=<"Type <bt> does not accept type parameters",st@\loc>];
		}
		
		// TODO: Add support for reified reified
	}
}

public RType convertFunctionType(FunctionType ft) {
    switch(ft) {
        case (FunctionType) `<Type t> ( <{TypeArg ","}* tas> )` : {
            l = convertTypeArgList(tas);
            if (size(l) == 0)
                return makeFunctionType(convertType(t), [ ], false);
            else if (size(l) != size([n | n <- [0..size(l)-1], RUnnamedType(_) := l[n]]))
                return makeFunctionTypeWithNames(convertType(t),[RUnnamedType(getElementType(li)) | li <- l],false)[@warninfo=<"Names are ignored on the arguments to a function type",ft@\loc>];
            else
                return makeFunctionTypeWithNames(convertType(t),l,false);
        }
    }
}

public RType convertUserType(UserType ut) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return RUserType(convertName(n),[]);
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : return RUserType(convertName(n),[convertType(ti) | ti <- ts]);
    }
}

public Name getUserTypeRawName(UserType ut) {
    switch(ut) {
        case (UserType) `<QualifiedName n>` : return getLastName(n);
        case (UserType) `<QualifiedName n>[ <{Type ","}+ ts> ]` : return getLastName(n);
    }
}

public RType convertTypeVar(TypeVar tv) {
    switch(tv) {
        case (TypeVar) `& <Name n>` : return RTypeVar(convertName(n),RValueType());
        case (TypeVar) `& <Name n> <: <Type tb>` : return RTypeVar(convertName(n),convertType(tb));
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
        case (Type) `<TypeVar tv>` : return convertTypeVar(tv);
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
        case RValueType() : return "value";
        case RLocType() : return "loc";
        case RNodeType() : return "node";
        case RNumType() : return "num";
        case RReifiedType(rt) : return "type[#<prettyPrintType(rt)>]";
        case RBagType(et) : return "bag[<prettyPrintType(et)>]";
        case RIntType() : return "int";
        case RRelType(nts) : return "rel[<prettyPrintNamedTypeList(nts)>]";
        case RTypeVar(tvn,tvb) : return "&<prettyPrintName(tvn)> \<: <prettyPrintType(tvb)>";
        case RRealType() : return "real";
        case RFunctionType(rt,pts,isva) : return "<prettyPrintType(rt)> (<prettyPrintNamedTypeList(pts)>" + (isva ? "..." : "") + ")";
        case RTupleType(nts) : return "tuple[<prettyPrintNamedTypeList(nts)>]";
        case RStrType() : return "str";
        case RBoolType() : return "bool";
        case RReifiedReifiedType(rt) : return "reified(<prettyPrintType(t)>)";
        case RVoidType() : return "void";
        case RNonTerminalType() : return "non-terminal";
        case RDateTimeType() : return "datetime";
        case RSetType(et) : return "set[<prettyPrintType(et)>]";
        case RMapType(dt,rt) : return "map[<prettyPrintNamedType(dt)>,<prettyPrintNamedType(rt)>]";
        case RConstructorType(cn, an, ets) : return "<prettyPrintType(an)>: <prettyPrintName(cn)>(<prettyPrintNamedTypeList(ets)>)";
        case RListType(et) : return "list[<prettyPrintType(et)>]";
        case RADTType(n,ps) : return "<prettyPrintName(n)>" + (size(ps) > 0 ? "[ <prettyPrintTypeList(ps)> ]" : "");
        case RUserType(tn, tps) : return "<prettyPrintName(tn)>" + (size(tps)>0 ? "[<prettyPrintTypeList(tps)>]" : "");
        case RAliasType(an,ps,at) : return "alias <prettyPrintName(an)>" + (size(ps) > 0 ? "[<prettyPrintTypeList(ps)>]" : "") + " = <prettyPrintType(at)>";
        case RDataTypeSelector(s,t) : return "Selector <s>.<t>";
        case RFailType(sls) :  return "failure " + joinList(toList(sls),printLocMsgPair,", ","");
        case RInferredType(n) : return "Inferred Type: <n>";
        case ROverloadedType(pts) : return "Overloaded type, could be: " + prettyPrintTypeList([p | p <- pts]);
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

//
// Routines for dealing with value types
//
public bool isValueType(RType t) {
    if (RAliasType(_,_,at) := t) return isValueType(at);
    if (RTypeVar(_,tvb) := t) return isValueType(tvb);
    return RValueType() := t;
}

public RType makeValueType() { return RValueType(); }

//
// Routines for dealing with loc types
//
public bool isLocType(RType t) {
    if (RAliasType(_,_,at) := t) return isLocType(at);
    if (RTypeVar(_,tvb) := t) return isLocType(tvb);
    return RLocType() := t;
}

public RType makeLocType() { return RLocType(); }

//
// Routines for dealing with node types
//
public bool isNodeType(RType t) {
    if (RAliasType(_,_,at) := t) return isNodeType(at);
    if (RTypeVar(_,tvb) := t) return isNodeType(tvb);
    return (RNodeType() := t || RADTType(_,_) := t);
}

public RType makeNodeType() { return RNodeType(); }

//
// Routines for dealing with num types
//
public bool isNumType(RType t) {
    if (RAliasType(_,_,at) := t) return isNumType(at);
    if (RTypeVar(_,tvb) := t) return isNumType(tvb);
    return RNumType() := t;
}

public RType makeNumType() { return RNumType(); }

//
// Routines for dealing with type type
//
public bool isReifiedType(RType t) {
    if (RAliasType(_,_,at) := t) return isReifiedType(at);
    if (RTypeVar(_,tvb) := t) return isReifiedType(tvb);
    return RReifiedType(_) := t;
}

public RType getReifiedType(RType t) {
    if (RAliasType(_,_,at) := t) return getReifiedType(at);
    if (RTypeVar(_,tvb) := t) return getReifiedType(tvb);
    if (RReifiedType(rt) := t) return rt;
    throw "getReifiedType given unexpected type: <prettyPrintType(t)>";
}

public RType makeReifiedType(RType mainType) { return RReifiedType(mainType); }

//
// Routines for dealing with bag types
//
// TODO: Add additional support once bags are supported in the language
//
public bool isBagType(RType t) {
    if (RAliasType(_,_,at) := t) return isBagType(at);
    if (RTypeVar(_,tvb) := t) return isBagType(tvb);
    return RBagType(_) := t;
}

//
// Routines for dealing with int types
//
public bool isIntType(RType t) {
    if (RAliasType(_,_,at) := t) return isIntType(at);
    if (RTypeVar(_,tvb) := t) return isIntType(tvb);
    return RIntType() := t;
}

public RType makeIntType() { return RIntType(); }

//
// Routines for dealing with rat types
//
public bool isRatType(RType t) {
    if (RAliasType(_,_,at) := t) return isRatType(at);
    if (RTypeVar(_,tvb) := t) return isRatType(tvb);
    return RRatType() := t;
}

public RType makeRatType() { return RRatType(); }

//
// Routines for dealing with rel types
//
public bool isRelType(RType t) {
    if (RAliasType(_,_,at) := t) return isRelType(at);
    if (RTypeVar(_,tvb) := t) return isRelType(tvb);
    if (RRelType(_) := t) return true;
    return false;
}

public RType makeRelType(RType its...) { return RRelType([ RUnnamedType( t ) | t <- its ]); }

public RType makeRelTypeFromTuple(RType t) { return RRelType(getTupleFieldsWithNames(t)); }

public RType getRelElementType(RType t) {
    if (RAliasType(_,_,at) := t) return getRelElementType(at);
    if (RTypeVar(_,tvb) := t) return getRelElementType(tvb);
    if (RRelType(ets) := t) return RTupleType(ets);
    throw "Error: Cannot get relation element type from type <prettyPrintType(t)>";
}

public bool relHasFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return relHasFieldNames(at);
    if (RTypeVar(_,tvb) := t) return relHasFieldNames(tvb);
    if (RRelType(tls) := t)
        return size(tls) == size([n | n <- [0..size(tls)-1], namedTypeHasName(tls[n])]);
    throw "relHasFieldNames given non-Relation type <prettyPrintType(t)>";
}

public list[RName] getRelFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return getRelFieldNames(at);
    if (RTypeVar(_,tvb) := t) return getRelFieldNames(tvb);
    if (RRelType(tls) := t) {
        if (relHasFieldNames(t)) {
            return [ getTypeName(tli) | tli <- tls ];
        }
        throw "getRelFieldNames given rel type without field names: <prettyPrintType(t)>";        
    }
    throw "getRelFieldNames given non-Relation type <prettyPrintType(t)>";
}

//
// Routines for dealing with type var types
//
public bool isTypeVar(RType t) {
    return RTypeVar(_,_) := t;
}

public RType makeTypeVar(RName varName) {
    return RTypeVar(varName, RValueType());
}

public RType makeTypeVarWithBound(RName varName, RType varBound) {
    return RTypeVar(varName, varBound);
}

public RName getTypeVarName(RType t) {
    if (RTypeVar(tvn,_) := t) return tvn;
    throw "getTypeVarName given unexpected type: <prettyPrintType(t)>";
}

public RType getTypeVarBound(RType t) {
    if (RTypeVar(_,tvb) := t) return tvb;
    throw "getTypeVarBound given unexpected type: <prettyPrintType(t)>";
}

public set[RType] collectTypeVars(RType t) {
    return { rt | / RType rt : RTypeVar(_,_) <- t };
}

public map[RName,RType] initializeTypeVarMap(RType t) {
    set[RType] rt = collectTypeVars(t);
    return ( getTypeVarName(tv) : makeVoidType() | tv <- rt );
}

public bool typeContainsTypeVars(RType t) {
    return size(collectTypeVars(t)) > 0;
}

public set[RName] typeVarNames(RType t) {
    return { tvn | RTypeVar(tvn,_) <- collectTypeVars(t) };
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
//  return visit(rt) {
//      case RTypeVar(RFreeTypeVar(n)) : if (n in varMappings) insert(varMappings[n]);
//      case RTypeVar(RBoundTypeVar(n,_)) : if (n in varMappings) insert(varMappings[n]);   
//  };
//}

//
// Routines for dealing with real types
//
public bool isRealType(RType t) {
    if (RAliasType(_,_,at) := t) return isRealType(at);
    if (RTypeVar(_,tvb) := t) return isRealType(tvb);
    return RRealType() := t;
}

public RType makeRealType() { return RRealType(); }

//
// Routines for dealing with function types
//
public bool isFunctionType(RType t) {
    if (RAliasType(_,_,at) := t) return isFunctionType(at);
    if (RTypeVar(_,tvb) := t) return isFunctionType(tvb);
    return RFunctionType(_,_,_)  := t;
}

public RType makeFunctionType(RType retType, list[RType] paramTypes, bool isVarArgs) { 
    return RFunctionType(retType, [ RUnnamedType( x ) | x <- paramTypes ], isVarArgs); 
}

public RType makeFunctionTypeWithNames(RType retType, list[RNamedType] paramTypes, bool isVarArgs) { 
    return RFunctionType(retType, paramTypes, isVarArgs); 
}

public RType makeFunctionTypeFromTuple(RType retType, RType paramTypeTuple, bool isVarArgs) { 
    return RFunctionType(retType, getTupleFieldsWithNames(paramTypeTuple), isVarArgs); 
}

public list[RType] getFunctionArgumentTypes(RType ft) {
    if (RAliasType(_,_,at) := ft) return getFunctionArgumentTypes(at);
    if (RTypeVar(_,tvb) := ft) return getFunctionArgumentTypes(tvb);
    if (RFunctionType(_, ats, _) := ft) return [ getElementType(argType) | argType <- ats ];
    throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public list[RNamedType] getFunctionArgumentTypesWithNames(RType ft) {
    if (RAliasType(_,_,at) := ft) return getFunctionArgumentTypesWithNames(at);
    if (RTypeVar(_,tvb) := ft) return getFunctionArgumentTypesWithNames(tvb);
    if (RFunctionType(_, ats, _) := ft) return ats;
    throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public RType getFunctionArgumentTypesAsTuple(RType ft) {
    if (RAliasType(_,_,at) := ft) return getFunctionArgumentTypesAsTuple(at);
    if (RTypeVar(_,tvb) := ft) return getFunctionArgumentTypesAsTuple(tvb);
    if (RFunctionType(_,ats,_) := ft) return RTupleType(ats);
    throw "Cannot get function arguments from non-function type <prettyPrintType(ft)>";
}

public RType getFunctionReturnType(RType ft) {
    if (RAliasType(_,_,at) := ft) return getFunctionReturnType(at);
    if (RTypeVar(_,tvb) := ft) return getFunctionReturnType(tvb);
    if (RFunctionType(retType, _, _) := ft) return retType; 
    throw "Cannot get function return type from non-function type <prettyPrintType(ft)>";
}

public bool isVarArgsFun(RType t) {
    if (RAliasType(_,_,at) := t) return isVarArgsFun(at);
    if (RTypeVar(_,tvb) := t) return isVarArgsFun(tvb);
    if (RFunctionType(_,_,isva) := t) return isva;
    throw "isVarArgsFun: given non-function type <prettyPrintType(t)>";
}

//
// Routines for dealing with tuple types
//
public bool isTupleType(RType t) {
    if (RAliasType(_,_,at) := t) return isTupleType(at);
    if (RTypeVar(_,tvb) := t) return isTupleType(tvb);
    return RTupleType(_) := t;
}

public RType makeTupleType(RType its...) {   return RTupleType([ RUnnamedType( t ) | t <- its ]); }

public RType makeTupleTypeWithNames(RNamedType its...) { return RTupleType(its); }

public bool tupleHasField(RType t, RName fn) {
    if (RAliasType(_,_,at) := t) return tupleHasField(at,fn);
    if (RTypeVar(_,tvb) := t) return tupleHasField(tvb,fn);
    if (RTupleType(tas) := t) {
        for (ta <- tas) {
            if (RNamedType(_,fn) := ta) return true;    
        }
    }
    return false;
}

public bool tupleHasField(RType t, int fn) {
    if (RAliasType(_,_,at) := t) return tupleHasField(at,fn);
    if (RTypeVar(_,tvb) := t) return tupleHasField(tvb,fn);
    if (RTupleType(tas) := t) {
        return (0 <= fn) && (fn < size(tas));
    }
    return false;
}

public RType getTupleFieldType(RType t, RName fn) {
    if (RAliasType(_,_,at) := t) return getTupleFieldType(at,fn);
    if (RTypeVar(_,tvb) := t) return getTupleFieldType(tvb,fn);
    if (RTupleType(tas) := t) {
        for (ta <- tas) {
            if (RNamedType(ft,fn) := ta) return ft; 
        }
        throw "Tuple <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }
    throw "getTupleFieldType given unexpected type <prettyPrintType(t)>";
}

public RType getTupleFieldType(RType t, int fn) {
    if (RAliasType(_,_,at) := t) return getTupleFieldType(at,fn);
    if (RTypeVar(_,tvb) := t) return getTupleFieldType(tvb,fn);
    if (RTupleType(tas) := t) {
        if (0 <= fn && fn < size(tas)) return getElementType(tas[fn]);
        throw "Tuple <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }
    throw "getTupleFieldType given unexpected type <prettyPrintType(t)>";
}

public list[RType] getTupleFields(RType t) {
    if (RAliasType(_,_,at) := t) return getTupleFields(at);
    if (RTypeVar(_,tvb) := t) return getTupleFields(tvb);
    if (RTupleType(tas) := t) {
        return [ getElementType(ta) | ta <- tas ];
    }
    throw "Cannot get tuple fields from type <prettyPrintType(t)>"; 
}

public list[RNamedType] getTupleFieldsWithNames(RType t) {
    if (RAliasType(_,_,at) := t) return getTupleFieldsWithNames(at);
    if (RTypeVar(_,tvb) := t) return getTupleFieldsWithNames(tvb);
    if (RTupleType(tas) := t) {
        return tas;
    }
    throw "Cannot get tuple fields from type <prettyPrintType(t)>"; 
}

public int getTupleFieldCount(RType t) {
    if (RAliasType(_,_,at) := t) return getTupleFieldCount(at);
    if (RTypeVar(_,tvb) := t) return getTupleFieldCount(tvb);
    if (RTupleType(tas) := t) {
        return size(tas);
    }
    throw "Cannot get tuple field count from type <prettyPrintType(t)>";    
}

public bool tupleHasFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return tupleHasFieldNames(at);
    if (RTypeVar(_,tvb) := t) return tupleHasFieldNames(tvb);
    if (RTupleType(tls) := t) {
        return size(tls) == size([n | n <- [0..size(tls)-1], namedTypeHasName(tls[n])]);
    }
    throw "tupleHasFieldNames given non-Tuple type <prettyPrintType(t)>";
}

public list[RName] getTupleFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return getTupleFieldNames(at);
    if (RTypeVar(_,tvb) := t) return getTupleFieldNames(tvb);
    if (RTupleType(tls) := t) {
        if (tupleHasFieldNames(t)) {
            return [ getTypeName(tli) | tli <- tls ];
        }
        throw "getTupleFieldNames given tuple type without field names: <prettyPrintType(t)>";        
    }
    throw "getTupleFieldNames given non-Tuple type <prettyPrintType(t)>";
}

public RName getTupleFieldName(RType t, int idx) {
    list[RName] names = getTupleFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw "getTupleFieldName given invalid index <idx>";
}

//
// Routines for dealing with str types
//
public bool isStrType(RType t) {
    if (RAliasType(_,_,at) := t) return isStrType(at);
    if (RTypeVar(_,tvb) := t) return isStrType(tvb);
    return RStrType() := t;
}

public RType makeStrType() { return RStrType(); }

//
// Routines for dealing with bool types
//
public bool isBoolType(RType t) {
    if (RAliasType(_,_,at) := t) return isBoolType(at);
    if (RTypeVar(_,tvb) := t) return isBoolType(tvb);
	return RBoolType() := t;
}

public RType makeBoolType() { return RBoolType(); }

//
// Routines for dealing with type reified
//
public bool isReifiedReifiedType(RType t) {
    if (RAliasType(_,_,at) := t) return isReifiedReifiedType(at);
    if (RTypeVar(_,tvb) := t) return isReifiedReifiedType(tvb);
    return RReifiedReified(_) := t;
}

public RType getReifiedReifiedType(RType t) {
    if (RAliasType(_,_,at) := t) return getReifiedReifiedType(at);
    if (RTypeVar(_,tvb) := t) return getReifiedReifiedType(tvb);
    if (RReifiedReified(rt) := t) return rt;
    throw "getReifiedReifiedType given unexpected type: <prettyPrintType(t)>";
}

public RType makeReifiedReifiedType(RType mainType) { return RReifiedReifiedType(mainType); }

//
// Routines for dealing with void types
//
public bool isVoidType(RType t) {
    if (RAliasType(_,_,at) := t) return isVoidType(at);
    if (RTypeVar(_,tvb) := t) return isVoidType(tvb);
    return RVoidType() := t;
}

public RType makeVoidType() { return RVoidType(); }

//
// Routines for dealing with nonterminal types
//
public bool isNonTerminalType(RType t) {
    return RNonTerminalType(_) := t;
}

//
// Routines for dealing with datetime types
//
public bool isDateTimeType(RType t) {
    if (RAliasType(_,_,at) := t) return isDateTimeType(at);
    if (RTypeVar(_,tvb) := t) return isDateTimeType(tvb);
    return RDateTimeType() := t;
}

public RType makeDateTimeType() { return RDateTimeType(); }

//
// Routines for dealing with set types
//
public bool isSetType(RType t) {
    if (RAliasType(_,_,at) := t) return isSetType(at);
    if (RTypeVar(_,tvb) := t) return isSetType(tvb);
    return RSetType(_) := t || RRelType(_) := t;
}

public RType makeSetType(RType itemType) { 
    return isTupleType(itemType) ? makeRelTypeFromTuple(itemType) : RSetType(itemType); 
}

public RType getSetElementType(RType t) {
    if (RAliasType(_,_,at) := t) return getSetElementType(at);
    if (RTypeVar(_,tvb) := t) return getSetElementType(tvb);
    if (RSetType(et) := t) return et;
    if (RRelType(ets) := t) return RTupleType(ets);
    throw "Error: Cannot get set element type from type <prettyPrintType(t)>";
}

//
// Routines for dealing with map types
//
public bool isMapType(RType t) {
    if (RAliasType(_,_,at) := t) return isMapType(at);
    if (RTypeVar(_,tvb) := t) return isMapType(tvb);
    return RMapType(_,_) := t;
}

public RType makeMapType(RType domainType, RType rangeType) { return RMapType(RUnnamedType(domainType), RUnnamedType(rangeType)); }

public RType makeMapTypeWithNames(RNamedType domainType, RNamedType rangeType) { return RMapType(domainType, rangeType); }

public RType makeMapTypeFromTuple(RType t) {
    if (RAliasType(_,_,at) := t) return makeMapTypeFromTuple(at);
    if (RTypeVar(_,tvb) := t) return makeMapTypeFromTuple(tvb);
    if (RTupleType([lt,rt]) := t) return RMapType(lt,rt);
    throw "makeMapTypeFromTuple called with unexpected type <prettyPrintType(t)>";
}

public RType getMapFieldsAsTuple(RType t) {
    if (RAliasType(_,_,at) := t) return getMapFieldsAsTuple(at);
    if (RTypeVar(_,tvb) := t) return getMapFieldsAsTuple(tvb);
    if (RMapType(lt,rt) := t) return RTupleType([lt,rt]);
    throw "getMapFieldsAsTuple called with unexpected type <prettyPrintType(t)>";
}       

@doc{Check to see if a map defines a field (by name).}
public bool mapHasField(RType t, RName fn) {
    if (RAliasType(_,_,at) := t) return mapHasField(at,fn);
    if (RTypeVar(_,tvb) := t) return mapHasField(tvb,fn);
    if (RMapType(tl,tr) := t) {
        if (RNamedType(_,fn) := tl) return true;    
        if (RNamedType(_,fn) := tr) return true;    
    }
    return false;
}

@doc{Check to see if a map defines a field (by index).}
public bool mapHasField(RType t, int fn) {
    if (RAliasType(_,_,at) := t) return mapHasField(at,fn);
    if (RTypeVar(_,tvb) := t) return mapHasField(tvb,fn);
    if (RMapType(tl,tr) := t) {
        return (0 <= fn) && (fn <= 1);    
    }
    return false;
}

@doc{Return the type of a field defined on a map (by name).}
public RType getMapFieldType(RType t, RName fn) {
    if (RAliasType(_,_,at) := t) return getMapFieldType(at,fn);
    if (RTypeVar(_,tvb) := t) return getMapFieldType(tvb,fn);
    if (RMapType(tl,tr) := t) {
        if (RNamedType(ft,fn) := tl) return ft; 
        if (RNamedType(ft,fn) := tr) return ft; 
        throw "Map <prettyPrintType(t)> does not have field <prettyPrintName(fn)>";
    }
    throw "getMapFieldType given type <prettyPrintType(t)>";
}

@doc{Return the type of a field defined on a map (by index).}
public RType getMapFieldType(RType t, int fn) {
    if (RAliasType(_,_,at) := t) return getMapFieldType(at,fn);
    if (RTypeVar(_,tvb) := t) return getMapFieldType(tvb,fn);
    if (RMapType(tl,tr) := t) {
        if (fn == 0) return getElementType(tl);
        if (fn == 1) return getElementType(tr);
        throw "Map <prettyPrintType(t)> does not have field at index <fn>";
    }
    throw "getMapFieldType given type <prettyPrintType(t)>";
}

public list[RType] getMapFields(RType t) {
    if (RAliasType(_,_,at) := t) return getMapFields(at);
    if (RTypeVar(_,tvb) := t) return getMapFields(tvb);
    if (RMapType(tl, tr) := t) {
        return [ getElementType(tl), getElementType(tr) ];
    }
    throw "Cannot get map fields from type <prettyPrintType(t)>";   
}

public list[RNamedType] getMapFieldsWithNames(RType t) {
    if (RAliasType(_,_,at) := t) return getMapFieldsWithNames(at);
    if (RTypeVar(_,tvb) := t) return getMapFieldsWithNames(tvb);
    if (RMapType(tl, tr) := t) {
        return [ tl, tr ];
    }
    throw "Cannot get map fields from type <prettyPrintType(t)>";   
}

public bool mapHasFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return mapHasFieldNames(at);
    if (RTypeVar(_,tvb) := t) return mapHasFieldNames(tvb);
    if (RMapType(tl, tr) := t) {
        return (namedTypeHasName(tl) && namedTypeHasName(tr));
    }
    throw "mapHasFieldNames given non-Map type <prettyPrintType(t)>";
}

public tuple[RName domainName, RName rangeName] getMapFieldNames(RType t) {
    if (RAliasType(_,_,at) := t) return getMapFieldNames(at);
    if (RTypeVar(_,tvb) := t) return getMapFieldNames(tvb);
    if (RMapType(tl, tr) := t) {
        if (mapHasFieldNames(t)) {
            return < getTypeName(tl), getTypeName(tr) >;
        }
        throw "getMapFieldNames given map type without field names: <prettyPrintType(t)>";        
    }
    throw "getMapFieldNames given non-Map type <prettyPrintType(t)>";
}

public RName getMapFieldName(RType t, int idx) {
    tuple[RName domainName, RName rangeName] fns = getMapFieldNames(t);
    if (idx == 0) return fns.domainName;
    if (idx == 1) return fns.rangeName;
    throw "getMapFieldName given invalid index <idx>";
}
    
public RType getMapDomainType(RType t) {
    if (RAliasType(_,_,at) := t) return getMapDomainType(at);
    if (RTypeVar(_,tvb) := t) return getMapDomainType(tvb);
    if (RMapType(tl,_) := t) return getElementType(tl);
    throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

public RType getMapRangeType(RType t) {
    if (RAliasType(_,_,at) := t) return getMapRangeType(at);
    if (RTypeVar(_,tvb) := t) return getMapRangeType(tvb);
    if (RMapType(_,tr) := t) return getElementType(tr);
    throw "Cannot get domain of non-map type <prettyPrintType(t)>";
}

//
// Routines for dealing with constructor types
//
public bool isConstructorType(RType t) {
    if (RAliasType(_,_,at) := t) return isConstructorType(at);
    if (RTypeVar(_,tvb) := t) return isConstructorType(tvb);
    return RConstructorType(_,_,_) := t;
}

public RType makeConstructorType(RName consName, RType adtType, list[RNamedType] consArgs) {    
    return RConstructorType(consName, adtType, consArgs); 
}

public RType makeConstructorTypeFromTuple(RName consName, RType adtType, RType consArgs) {    
    return RConstructorType(consName, adtType, getTupleFieldsWithNames(consArgs)); 
}

public list[RType] getConstructorArgumentTypes(RType ct) {
    if (RAliasType(_,_,at) := ct) return getConstructorArgumentTypes(at);
    if (RTypeVar(_,tvb) := ct) return getConstructorArgumentTypes(tvb);
    if (RConstructorType(_,_,cts) := ct) return [ getElementType(cti) | cti <- cts ]; 
    throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public list[RNamedType] getConstructorArgumentTypesWithNames(RType ct) {
    if (RAliasType(_,_,at) := ct) return getConstructorArgumentTypesWithNames(at);
    if (RTypeVar(_,tvb) := ct) return getConstructorArgumentTypesWithNames(tvb);
    if (RConstructorType(_,_,cts) := ct) return cts;
    throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public RType getConstructorArgumentTypesAsTuple(RType ct) {
    if (RAliasType(_,_,at) := ct) return getConstructorArgumentTypesAsTuple(at);
    if (RTypeVar(_,tvb) := ct) return getConstructorArgumentTypesAsTuple(tvb);
    if (RConstructorType(_,_,cts) := ct) return RTupleType(cts); 
    throw "Cannot get constructor arguments from non-constructor type <prettyPrintType(ct)>";
}

public RType getConstructorResultType(RType ct) {
    if (RAliasType(_,_,at) := ct) return getConstructorResultType(at);
    if (RTypeVar(_,tvb) := ct) return getConstructorResultType(tvb);
    if (RConstructorType(cn, an, cts) := ct) return an;
    throw "Cannot get constructor ADT type from non-constructor type <prettyPrintType(ct)>";
}

public RName getConstructorName(RType ct) {
    if (RAliasType(_,_,at) := ct) return getConstructorName(at);
    if (RTypeVar(_,tvb) := ct) return getConstructorName(tvb);
    if (RConstructorType(n,_,_) := ct) return n;
    throw "Cannot get constructor name from non-constructor type <prettyPrintType(ct)>";
}

//
// Routines for dealing with list types
//
public bool isListType(RType t) {
    if (RAliasType(_,_,at) := t) return isListType(at);
    if (RTypeVar(_,tvb) := t) return isListType(tvb);
	return RListType(_) := t;
}

public RType makeListType(RType itemType) { return RListType(itemType); }

public RType getListElementType(RType t) {
    if (RAliasType(_,_,at) := t) return getListElementType(at);
    if (RTypeVar(_,tvb) := t) return getListElementType(tvb);
	if (RListType(et) := t) return et;
	throw "Error: Cannot get list element type from type <prettyPrintType(t)>";
}

//
// Routines for dealing with adt types
//
public bool isADTType(RType t) {
    if (RAliasType(_,_,at) := t) return isADTType(at);
    if (RTypeVar(_,tvb) := t) return isADTType(tvb);
    if (RReifiedType(_) := t) return true;
    return RADTType(_,_) := t;
}

public RType makeADTType(RName n) {
    return RADTType(n,[]);
}

public RType makeParameterizedADTType(RName n, RType p...) {
    return RADTType(n,p);
}

public RName getADTName(RType t) {
    if (RAliasType(_,_,at) := t) return getADTName(at);
    if (RTypeVar(_,tvb) := t) return getADTName(tvb);
    if (RADTType(n,_) := t) return n;
    if (RConstructorType(_,RADTType(n,_),_) := t) return n;
    if (RReifiedType(_) := t) return RSimpleName("type");
    throw "getADTName, invalid type given: <prettyPrintType(t)>";
}

public bool adtHasTypeParameters(RType t) {
    if (RAliasType(_,_,at) := t) return adtHasTypeParameters(at);
    if (RTypeVar(_,tvb) := t) return adtHasTypeParameters(tvb);
    if (RADTType(_,ps) := t) return size(ps) > 0;
    if (RConstructorType(_,RADTType(_,ps),_) := t) return size(ps) > 0;
    if (RReifiedType(_) := t) return false;
    throw "ADTHasTypeParameters given non-ADT type <prettyPrintType(t)>";
}

public list[RType] getADTTypeParameters(RType t) {
    if (RAliasType(_,_,at) := t) return getADTTypeParameters(at);
    if (RTypeVar(_,tvb) := t) return getADTTypeParameters(tvb);
    if (RADTType(_,ps) := t) return ps;
    if (RConstructorType(_,RADTType(_,ps),_) := t) return ps;
    if (RReifiedType(_) := t) return [ ];
    throw "getADTTypeParameters given non-ADT type <prettyPrintType(t)>";
}

//
// Routines for dealing with user types
//
public RName getUserTypeName(RType ut) {
    if (RUserType(x,_) := ut) return x;
    throw "Cannot get user type name from non user type <ut>";
} 

public bool userTypeHasParameters(RType ut) {
    return (RUserType(_,ps) := ut && size(ps) > 0);
}

public list[RType] getUserTypeParameters(RType ut) {
    if (RUserType(_,ps) := ut) return ps;
    throw "error in getUserTypeParameters: given unparameterized type <prettyPrintType(ut)>";
}

//
// Routines for dealing with alias types
//
public bool isAliasType(RType t) {
    return RAliasType(_,_,_) := t;
}

public RName getAliasName(RType t) {
    if (RAliasType(n,_,_) := t) return n;
    throw "getAliasName, invalid type given: <prettyPrintType(t)>";
}

public RType getAliasedType(RType t) {
    if (RAliasType(_,_,at) := t) return at;
    throw "getAliasedType, invalid type given: <prettyPrintType(t)>";
}

public RType makeAliasType(RName n, RType t) {
    return RAliasType(n,[],t);
}

public RType makeParameterizedAliasType(RName n, RType t, list[RType] p) {
    return RAliasType(n,p,t);
}

public bool aliasHasTypeParameters(RType t) {
    if (RAliasType(n,ps,_) := t) return size(ps) > 0;
    throw "AliasHasTypeParameters given non-alias type <prettyPrintType(t)>";
}

public list[RType] getAliasTypeParameters(RType t) {
    if (RAliasType(n,ps,_) := t) return ps;
    throw "getAliasTypeParameters given non-alias type <prettyPrintType(t)>";
}

public RType unwindAliases(RType t) {
    return visit(t) { case RAliasType(tl,ps,tr) => tr };
}

//
// Routines for dealing with data type selector types
//
public bool isDataTypeSelectorType(RType t) {
    return RDataTypeSelector(_,_) := t;
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

//
// Routines for dealing with overloaded types
//
public bool isOverloadedType(RType t) {
	return ROverloadedType(_) := t;
}

public set[RType] getOverloadOptions(RType t) {
	if (ROverloadedType(s) := t) return s;
	throw "Error: Cannot get overloaded options from non-overloaded type <prettyPrintType(t)>";
}

public RType makeOverloadedType(set[RType] options) {
    return ROverloadedType(options);
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

//
// Given a list of (possibly named) field types, and a list of fields, return the mapping from the field to the
// offset in the field types list. This is used for (for instance) subscript resolution.
//
// TODO: Would it be better to return a map?
//
// TODO: Is this the best place for this? It was moved out of the type checker module, but there may
// be a better place for it than here.
//
private list[tuple[Field field, int offset]] getFieldOffsets(list[RNamedType] fieldTypes, list[Field] fields) {
	list[tuple[Field field, int offset]] result = [ ];
	map[RName, int] namedFields = size(fieldTypes) > 0 ? ( n : i | i <-  [0..size(fieldTypes)-1], RNamedType(_,n) := fieldTypes[i]) : ( );
	
	for (f <- fields) {
		switch(f) {
			case (Field) `<Name n>` : {
				RName fName = convertName(n);
				if (fName in namedFields) 
					result += < f, namedFields[fName] >;
				else
					result += < f, -1 >;				
			}

			case (Field) `<IntegerLiteral il>` : {
				int ival = toInt("<il>");
				if (ival >= size(fieldTypes))
					result += <f, -1 >;
				else
					result += <f, ival >;
			} 
		}
	}

	return result;
}

