
@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@contributor{Anastasia Izmaylova - Anastasia.Izmaylova@cwi.nl (CWI)}
module lang::rascal::check::ATypeUtils

import List;
import Set;
import String;
extend ParseTree;

extend lang::rascal::check::AType;

import lang::rascal::\syntax::Rascal;



Symbol normalizeType(Symbol s){
   return
    visit(s){
        case AType::alist(AType::atuple(list[Symbol] tls)) => AType::alrel(tls)
        case AType::aset(AType::atuple(list[Symbol] tls))  => AType::arel(tls)
   }
}

// ---- print atypes ----------------------------------------------------------

@doc{Pretty printer for Rascal abstract types.}
str prettyPrintAType(aint()) = "int";
str prettyPrintAType(abool()) = "bool";
str prettyPrintAType(areal()) = "real";
str prettyPrintAType(arat()) = "rat";
str prettyPrintAType(astr()) = "str";
str prettyPrintAType(anum()) = "num";
str prettyPrintAType(anode()) = "node";
str prettyPrintAType(avoid()) = "void";
str prettyPrintAType(avalue()) = "value";
str prettyPrintAType(aloc()) = "loc";
str prettyPrintAType(adatetime()) = "datetime";
str prettyPrintAType(aparameter(str pn, AType t)) = t == avalue() ? "&<pn>" : "&<pn> \<: <prettyPrintAType(t)>";
str prettyPrintAType(aset(AType t)) = "set[<prettyPrintAType(t)>]";
str prettyPrintAType(arel(AType ts)) = "rel[<prettyPrintAType(ts)>]";
str prettyPrintAType(alrel(AType ts)) = "lrel[<prettyPrintAType(ts)>]";
str prettyPrintAType(atuple(AType ts)) = "tuple[<prettyPrintAType(ts)>]";
str prettyPrintAType(alist(AType t)) = "list[<prettyPrintAType(t)>]";
str prettyPrintAType(amap(AType d, AType r)) = "map[<prettyPrintAType(d)>, <prettyPrintAType(r)>]";
str prettyPrintAType(abag(AType t)) = "bag[<prettyPrintAType(t)>]";
str prettyPrintAType(aadt(str s, [], _)) = s;
str prettyPrintAType(aadt(str s, ps, _)) = "<s>[<prettyPrintAType(ps)>]" when size(ps) > 0;
str prettyPrintAType(acons(AType adt, str consName, AType fs, list[Keyword] kwFormals)) = "<prettyPrintAType(adt)> <consName> : (<prettyPrintAType(fs)>)";

str prettyPrintAType(auser(qualName(str qual, str s), [])) = isEmpty(qual) ? s : "<qual>::<s>";
str prettyPrintAType(auser(qualName(str qual, str s), ps)) ="<isEmpty(qual) ? s : "<qual>::<s>">[<prettyPrintAType(ps)>]" when size(ps) > 0;

str prettyPrintAType(afunc(AType rt, AType formals, list[Keyword] kwFormals)) = "fun <prettyPrintAType(rt)>(<prettyPrintAType(formals)><isEmpty(kwFormals) ? "" : ", "><intercalate(", ", [ prettyPrintAType(p) | p <- kwFormals])>)";
str prettyPrintAType(\var-func(AType rt, atypeList(ps), AType va)) = "fun <prettyPrintAType(rt)>(prettyPrintAType(ps+val))>...)";
str prettyPrintAType(areified(AType t)) = "type[<prettyPrintAType(t)>]";

str prettyPrintAType(acons(str adtName, str consName, 
                 lrel[AType fieldType, str fieldName] fields, 
                 lrel[AType fieldType, str fieldName, Expression defaultExp] kwFields))
                 = "<adtName>:<consName>(<intercalate(",", ["<prettyPrintAType(ft)> <fn>" | <ft, fn> <- fields])>,<intercalate(",", ["<prettyPrintAType(ft)> <fn>=..." | <ft, fn, de> <- kwFields])>)";

str prettyPrintAType(afunc(AType ret, list[AType] formals, lrel[AType fieldType, str fieldName, Expression defaultExp] kwFormals))
                = "<prettyPrintAType(ret)>(<intercalate(",", [prettyPrintAType(f) | f <- formals])>,<intercalate(",", ["<prettyPrintAType(ft)> <fn>=..." | <ft, fn, de> <- kwFormals])>)";
str prettyPrintAType(amodule(str mname)) = "module <mname>";               
str prettyPrintAType(overloadedAType(rel[Key, AType] overloads))
                = "overloaded(" + intercalate(", ", [prettyPrintAType(t) | <k, t> <- overloads]) + ")";

str prettyPrintAType(list[AType] atypes) = intercalate(", ", [prettyPrintAType(t) | t <- atypes]);

//str prettyPrintAType(\user(RName rn, list[Symbol] ps)) = "<prettyPrintName(rn)>[<intercalate(", ", [ prettyPrintAType(p) | p <- ps ])>]";
//str prettyPrintAType(failure(set[Message] ms)) = "fail"; // TODO: Add more detail?
//str prettyPrintAType(\inferred(int n)) = "inferred(<n>)";
//str prettyPrintAType(\overloaded(set[Symbol] os, set[Symbol] defaults)) = "overloaded:\n\t\t<intercalate("\n\t\t",[prettyPrintAType(\o) | \o <- os + defaults])>";
//str prettyPrintAType(deferred(Symbol givenType)) = "deferred(<prettyPrintAType(givenType)>)";
// named non-terminal symbols
//str prettyPrintAType(\sort(str name)) = name;
//str prettyPrintAType(\start(Symbol s)) = "start[<prettyPrintAType(s)>]";
//str prettyPrintAType(\prod(Symbol s, str name, list[Symbol] fs, set[Attr] atrs)) = "<prettyPrintAType(s)> <name> : (<intercalate(", ", [ prettyPrintAType(f) | f <- fs ])>)";
//str prettyPrintAType(\lex(str name)) = name;
//str prettyPrintAType(\layouts(str name)) = name;
//str prettyPrintAType(\keywords(str name)) = name;
//str prettyPrintAType(aparameterized-sort(str name, list[Symbol] parameters)) = name when size(parameters) == 0;
//str prettyPrintAType(aparameterized-sort(str name, list[Symbol] parameters)) = "<name>[<intercalate(", ", [ prettyPrintAType(p) | p <- parameters ])>]" when size(parameters) > 0;
//str prettyPrintAType(aparameterized-lex(str name, list[Symbol] parameters)) = name when size(parameters) == 0;
//str prettyPrintAType(aparameterized-lex(str name, list[Symbol] parameters)) = "<name>[<intercalate(", ", [ prettyPrintAType(p) | p <- parameters ])>]" when size(parameters) > 0;
//// terminal symbols
//str prettyPrintAType(\lit(str string)) = string;
//str prettyPrintAType(\cilit(str string)) = string;
//str prettyPrintAType(\char-class(list[CharRange] ranges)) = "[<intercalate(" ", [ "<r.begin>-<r.end>" | r <- ranges ])>]";
//// regular symbols
//str prettyPrintAType(\empty()) = "()";
//str prettyPrintAType(\opt(Symbol symbol)) = "<prettyPrintAType(symbol)>?";
//str prettyPrintAType(\iter(Symbol symbol)) = "<prettyPrintAType(symbol)>+";
//str prettyPrintAType(\iter-star(Symbol symbol)) = "<prettyPrintAType(symbol)>*";
//str prettyPrintAType(\iter-seps(Symbol symbol, list[Symbol] separators)) = "{<prettyPrintAType(symbol)> <intercalate(" ", [ prettyPrintAType(sep) | sep <- separators ])>}+";
//str prettyPrintAType(\iter-star-seps(Symbol symbol, list[Symbol] separators)) = "{<prettyPrintAType(symbol)> <intercalate(" ", [ prettyPrintAType(sep) | sep <- separators ])>}*";
//str prettyPrintAType(\alt(set[Symbol] alternatives)) = "( <intercalate(" | ", [ prettyPrintAType(a) | a <- alternatives ])> )" when size(alternatives) > 1;
//str prettyPrintAType(\seq(list[Symbol] sequence)) = "( <intercalate(" ", [ prettyPrintAType(a) | a <- sequence ])> )" when size(sequence) > 1;
//str prettyPrintAType(\conditional(Symbol symbol, set[Condition] conditions)) = "<prettyPrintAType(symbol)> { <intercalate(" ", [ prettyPrintCond(cond) | cond <- conditions ])> }";
//default str prettyPrintAType(Symbol s) = "<type(s,())>";

//private str prettyPrintCond(Condition::\follow(Symbol symbol)) = "\>\> <prettyPrintAType(symbol)>";
//private str prettyPrintCond(Condition::\not-follow(Symbol symbol)) = "!\>\> <prettyPrintAType(symbol)>";
//private str prettyPrintCond(Condition::\precede(Symbol symbol)) = "<prettyPrintAType(symbol)> \<\<";
//private str prettyPrintCond(Condition::\not-precede(Symbol symbol)) = "<prettyPrintAType(symbol)> !\<\<";
//private str prettyPrintCond(Condition::\delete(Symbol symbol)) = "???";
//private str prettyPrintCond(Condition::\at-column(int column)) = "@<column>";
//private str prettyPrintCond(Condition::\begin-of-line()) = "^";
//private str prettyPrintCond(Condition::\end-of-line()) = "$";
//private str prettyPrintCond(Condition::\except(str label)) = "!<label>";

// ---- create atypes ---------------------------------------------------------

@doc{Create a new int type.}
AType makeIntType() = aint();

@doc{Create a new bool type.}
AType makeBoolType() = abool();

@doc{Create a new real type.}
AType makeRealType() = areal();

@doc{Create a new rat type.}
AType makeRatType() = arat();

@doc{Create a new str type.}
AType makeStrType() = astr();

@doc{Create a new num type.}
AType makeNumType() = anum();

@doc{Create a new node type.}
AType makeNodeType() = anode();

@doc{Create a new void type.}
AType makeVoidType() = avoid();

@doc{Create a new value type.}
AType makeValueType() = avalue();

@doc{Create a new loc type.}
AType makeLocType() = aloc();

@doc{Create a new datetime type.}
AType makeDateTimeType() = adatetime();

@doc{Create a new set type, given the element type of the set.}
AType makeSetType(AType elementType) {
    return isTupleType(elementType) ? makeRelTypeFromTuple(elementType) : aset(elementType);
}

@doc{Create a new list type, given the element type of the list.}
AType makeListType(AType elementType) {
    return isTupleType(elementType) ? makeListRelTypeFromTuple(elementType) : alist(elementType);
}       

@doc{Create a new rel type, given the element types of the fields. Check any given labels for consistency.}
AType makeRelType(AType elementTypes...) {
    set[str] labels = { tp.label | tp <- elementTypes, !isEmpty(tp.label) };
    if (size(labels) == 0 || size(labels) == size(elementTypes))
        return arel(atypeList(elementTypes));
    else
        throw rascalCheckerInternalError("For rel types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Create a new rel type based on a given tuple type.}
AType makeRelTypeFromTuple(AType t) = arel(atypeList(getTupleFields(t)));

@doc{Create a new list rel type, given the element types of the fields. Check any given labels for consistency.}
AType makeListRelType(AType elementTypes...) {
    set[str] labels = { tp.label | tp <- elementTypes, !isEmpty(tp.label) };
    if (size(labels) == 0 || size(labels) == size(elementTypes)) 
        return alrel(atypeList(elementTypes));
    else
        throw rascalCheckerInternalError("For lrel types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Create a new lrel type based on a given tuple type.}
AType makeListRelTypeFromTuple(AType t) = alrel(atypeList(getTupleFields(t)));

@doc{Create a new tuple type, given the element types of the fields. Check any given labels for consistency.}
AType makeTupleType(AType elementTypes...) {
    set[str] labels = { tp.label | tp <- elementTypes, !isEmpty(tp.label) };
    if (size(labels) == 0 || size(labels) == size(elementTypes)) 
        return atuple(atypeList(elementTypes));
    else
        throw rascalCheckerInternalError("For tuple types, either all fields much be given a distinct label or no fields should be labeled."); 
}

@doc{Create a new map type, given the types of the domain and range. Check to make sure field names are used consistently.}
AType makeMapType(AType domain, AType range) {
    if(!isEmpty(domain.label) && !isEmpty(range.label)){
        if(domain.label != range.label) return amap(domain, range);
        throw "The field names of the map domain and range must be distinct.";
    }
    else if(!isEmpty(domain.label)) return amap(unset(domain,"label"),range);
    else if(!isEmpty(range.label)) return amap(domain,unset(range, "label"));
    return amap(domain, range);
}

//@doc{Create a new map type based on the given tuple.}
//AType makeMapTypeFromTuple(AType t) {
//    list[AType] tf = getTupleFields(t);
//    if (size(tf) != 2)
//        throw rascalCheckerInternalError("The provided tuple must have exactly 2 fields, one for the map domain and one for the range.");
//    return makeMapType(tf[0],tf[1], fieldNames=t.fieldNames);
//}

@doc{Create a new bag type, given the element type of the bag.}
AType makeBagType(AType elementType) = abag(elementType);

@doc{Create a new ADT type with the given name.}
AType makeADTType(str n) = aadt(n,[],[]);

@doc{Create a new parameterized ADT type with the given type parameters}
AType makeParameterizedADTType(str n, AType p...) = aadt(n,p);

@doc{Create a new constructor type.}
AType makeConstructorType(AType adtType, str name, AType consArgs...) { 
    set[str] labels = { tp.label | tp <- consArgs, !isEmpty(tp.label) };   
    if (size(labels) == 0 || size(labels) == size(consArgs)) 
        return acons(adtType, name, consArgs);
    else
        throw rascalCheckerInternalError("For constructor types, either all arguments much be given a distinct label or no parameters should be labeled."); 
}

//@doc{Create a new constructor type based on the contents of a tuple.}
//AType makeConstructorTypeFromTuple(AType adtType, str name, AType consArgs) {    
//    return makeConstructorType(adtType, name, getTupleFields(consArgs)); 
//}

@doc{Create a new alias type with the given name and aliased type.}
AType makeAliasType(str n, AType t) = aalias(n,[],t);

@doc{Create a new parameterized alias type with the given name, aliased type, and parameters.}
AType makeParameterizedAliasType(str n, AType t, list[AType] params) = aalias(n,params,t);

@doc{Marks if a function is a var-args function.}
public anno bool AType@isVarArgs;

@doc{Create a new function type with the given return and parameter types.}
AType makeFunctionType(AType retType, bool isVarArgs, AType paramTypes...) {
    set[str] labels = { tp.label |tp <- paramTypes, !isEmpty(tp.label) };
    if (size(labels) == 0 || size(labels) == size(paramTypes))
        //if (isVarArgs) { 
        //  return \var-func(retType, head(paramTypes,size(paramTypes)-1), last(paramTypes));
        //} else {
            return afunc(retType, paramTypes, [])[@isVarArgs=isVarArgs];
        //}
    else
        throw "For function types, either all parameters much be given a distinct label or no parameters should be labeled."; 
}

//@doc{Create a new function type with parameters based on the given tuple.}
//AType makeFunctionTypeFromTuple(AType retType, bool isVarArgs, AType paramTypeTuple) { 
//    return makeFunctionType(retType, isVarArgs, getTupleFields(paramTypeTuple));
//}

@doc{Create a type representing the reified form of the given type.}
AType makeReifiedType(AType mainType) = areified(mainType);

@doc{Create a type representing a type parameter (type variable).}
AType makeTypeVar(str varName) = aparameter(varName, avalue())[@boundGiven=false];

@doc{Create a type representing a type parameter (type variable) and bound.}
AType makeTypeVarWithBound(str varName, AType varBound) = aparameter(varName, varBound)[@boundGiven=true];


// ---- Deconstruct atypes ----------------------------------------------------

@doc{Unwraps parameters, and labels from around a type.}
//AType unwrapType(aalias(_,_,at)) = unwrapType(at);
AType unwrapType(p: aparameter(_,tvb)) = p.label? ? unwrapType(tvb)[label=p.label] : unwrapType(tvb);
//AType unwrapType(alabel(_,ltype)) = unwrapType(ltype);
AType unwrapType(\conditional(sym,_)) = unwrapType(sym);
public default AType unwrapType(AType t) = t;

@doc{Get the type that has been reified and stored in the reified type.}
public AType getReifiedType(AType t) {
    if (areified(rt) := unwrapType(t)) return rt;
    throw "getReifiedType given unexpected type: <prettyPrintAType(t)>";
}

@doc{Get the type of the relation fields as a tuple.}
public AType getRelElementType(AType t) {
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    if (aset(tup) := unwrapType(t)) return tup;
    throw "Error: Cannot get relation element type from type <prettyPrintAType(t)>";
}

@doc{Get whether the rel has field names or not.}
bool relHasFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return size(tls) == size([tp | tp <- tls, !isEmpty(tp.label)]);
    throw "relHasFieldNames given non-Relation type <prettyPrintAType(t)>";
}

bool allLabelled(list[AType] tls) = size(tls) == size([tp | tp <- tls, !isEmpty(tp.label)]);

@doc{Get the field names of the rel fields.}
public list[str] getRelFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)){
        return [tp.label | tp <- tls];
    }
    throw "getRelFieldNames given non-Relation type <prettyPrintAType(t)>";
}

@doc{Get the fields of a relation.}
public list[AType] getRelFields(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return tls;
    if (aset(atuple(tls)) := unwrapType(t)) return tls;
    throw "getRelFields given non-Relation type <prettyPrintAType(t)>";
}

@doc{Get the type of the list relation fields as a tuple.}
public AType getListRelElementType(AType t) {
    if (alrel(ets) := unwrapType(t)) return atuple(ets);
    if (alist(tup) := unwrapType(t)) return tup;
    throw "Error: Cannot get list relation element type from type <prettyPrintAType(t)>";
}

//@doc{Get whether the list rel has field names or not.}
//bool listRelHasFieldNames(AType t) {
//    if (alrel(tls) := \unwrapType(t)) return size(tls) == size([ti | ti:alabel(_,_) <- tls]);
//    throw "listRelHasFieldNames given non-List-Relation type <prettyPrintAType(t)>";
//}
//
@doc{Get the field names of the list rel fields.}
public list[str] getListRelFieldNames(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)){
        return [tp.label | tp <- tls];
    }
    throw "getListRelFieldNames given non-List-Relation type <prettyPrintAType(t)>";
}

@doc{Get the fields of a list relation.}
public list[AType] getListRelFields(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)) return tls;
    if (alist(atuple(tls)) := unwrapType(t)) return tls;
    throw "getListRelFields given non-List-Relation type <prettyPrintAType(t)>";
}

@doc{Get the name of a Rascal type parameter.}
str getRascalTypeParamName(AType t) {
    //if (aalias(_,_,AType at) := t) return getRascalTypeParamName(at);
    //if (alabel(_,AType lt) := t) return getRascalTypeParamName(lt);
    if (aparameter(tvn,_) := t) return tvn;
    throw "getRascalTypeParamName given unexpected type: <prettyPrintAType(t)>";
}

@doc{Get the bound of a type parameter.}
public AType getRascalTypeParamBound(AType t) {
    if (aparameter(_,tvb) := t) return tvb;
    throw "getRascalTypeParamBound given unexpected type: <prettyPrintAType(t)>";
}

@doc{Get all the type parameters inside a given type.}
public set[AType] collectRascalTypeParams(AType t) {
    return { unset(rt, "label") | / AType rt : aparameter(_,_) := t }; // TODO: "label" is unset to enable subset check later, reconsider
}

@doc{Provide an initial type map from the type parameters in the type to void.}
public map[str,AType] initializeRascalTypeParamMap(AType t) {
    set[AType] rt = collectRascalTypeParams(t);
    return ( getRascalTypeParamName(tv) : makeVoidType() | tv <- rt );
}

@doc{See if a type contains any type parameters.}
bool typeContainsRascalTypeParams(AType t) = size(collectRascalTypeParams(t)) > 0;

@doc{Return the names of all type variables in the given type.}
public set[str] typeParamNames(AType t) {
    return { tvn | aparameter(tvn,_) <- collectRascalTypeParams(t) };
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

@doc{Get a list of arguments for the function.}
public list[AType] getFunctionArgumentTypes(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return ats;
    if (afunc(_, ats) := unwrapType(ft)) return ats;
    throw "Cannot get function arguments from non-function type <prettyPrintAType(ft)>";
}

@doc{Get the arguments for a function in the form of a tuple.}
public AType getFunctionArgumentTypesAsTuple(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return atuple(ats);
     if (afunc(_, ats) := unwrapType(ft)) return atuple(ats);
    throw "Cannot get function arguments from non-function type <prettyPrintAType(ft)>";
}

@doc{Get the return type for a function.}
public AType getFunctionReturnType(AType ft) {
    if (afunc(rt, _, _) := unwrapType(ft)) return rt;
    if (afunc(rt, _) := unwrapType(ft)) return rt; 
    throw "Cannot get function return type from non-function type <prettyPrintAType(ft)>";
}

@doc{Indicate if the given tuple has a field of the given name.}
bool tupleHasField(AType t, str fn) {
    return atuple(atypeList(tas)) := unwrapType(t) && fn in { tp.label | tp <- tas, tp.label? } ;
}

@doc{Indicate if the given tuple has a field with the given field offset.}
bool tupleHasField(AType t, int fn) {
    return atuple(atypeList(tas)) := unwrapType(t) && 0 <= fn && fn < size(tas);
}

@doc{Get the type of the tuple field with the given name.}
public AType getTupleFieldType(AType t, str fn) {
    if (atuple(atypeList(tas)) := unwrapType(t)) {
        for(tp <- tas){
            if(tp.label == fn) return tp;
        }
        throw "Tuple <prettyPrintAType(t)> does not have field <fn>";
    }
    throw "getTupleFieldType given unexpected type <prettyPrintAType(t)>";
}

@doc{Get the type of the tuple field at the given offset.}
public AType getTupleFieldType(AType t, int fn) {
    if (atuple(atypeList(tas)) := t) {
        if (0 <= fn && fn < size(tas)) return unwrapType(tas[fn]);
        throw "Tuple <prettyPrintAType(t)> does not have field <fn>";
    }
    throw "getTupleFieldType given unexpected type <prettyPrintAType(t)>";
}

@doc{Get the types of the tuple fields, with labels removed}
public list[AType] getTupleFieldTypes(AType t) {
    if (atuple(atypeList(tas)) := t)
        return tas;
    throw "Cannot get tuple field types from type <prettyPrintAType(t)>"; 
}

@doc{Get the fields of a tuple as a list.}
public list[AType] getTupleFields(AType t) {
    if (atuple(atypeList(tas)) := unwrapType(t)) return tas;
    throw "Cannot get tuple fields from type <prettyPrintAType(t)>"; 
}

@doc{Get the number of fields in a tuple.}
public int getTupleFieldCount(AType t) = size(getTupleFields(t));

@doc{Does this tuple have field names?}
bool tupleHasFieldNames(AType t) {
    if (tup: atuple(atypeList(tas)) := unwrapType(t)) return size(tas) == [tp | tp <- tas, !isEmpty(tp.label)];
    throw "tupleHasFieldNames given non-Tuple type <prettyPrintAType(t)>";
}

@doc{Get the names of the tuple fields.}
public list[str] getTupleFieldNames(AType t) {
    if (atuple(atypeList(tls)) := unwrapType(t)) {
        if (allLabelled(tls)) {
            return [tp.label | tp <- tls];
        }
        throw "getTupleFieldNames given tuple type without field names: <prettyPrintAType(t)>";        
    }
    throw "getTupleFieldNames given non-Tuple type <prettyPrintAType(t)>";
}

@doc{Get the name of the tuple field at the given offset.}
str getTupleFieldName(AType t, int idx) {
    list[str] names = getTupleFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw "getTupleFieldName given invalid index <idx>";
}

@doc{Get the element type of a set.}
public AType getSetElementType(AType t) {
    if (aset(et) := unwrapType(t)) return et;
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    throw "Error: Cannot get set element type from type <prettyPrintAType(t)>";
}

@doc{Get the element type of a bag.}
public AType getBagElementType(AType t) {
    if (abag(et) := unwrapType(t)) return et;
    throw "Error: Cannot get set element type from type <prettyPrintAType(t)>";
}

@doc{Get the domain and range of the map as a tuple.}
public AType getMapFieldsAsTuple(AType t) {
    if (amap(dt,rt) := unwrapType(t)) return atuple(atypeList([dt,rt]));
    throw "getMapFieldsAsTuple called with unexpected type <prettyPrintAType(t)>";
}       

@doc{Check to see if a map defines a field (by name).}
bool mapHasField(AType t, str fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Check to see if a map defines a field (by index).}
bool mapHasField(AType t, int fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by name).}
public AType getMapFieldType(AType t, str fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by index).}
public AType getMapFieldType(AType t, int fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Get the fields in a map as a list of fields.}
public list[AType] getMapFields(AType t) = getTupleFields(getMapFieldsAsTuple(t));

@doc{Check to see if the map has field names.}
bool mapHasFieldNames(AType t) = tupleHasFieldNames(getMapFieldsAsTuple(t));

@doc{Get the field names from the map fields.}
public list[str] getMapFieldNames(AType t) {
    if ([domain, range] := getMapFields(t)) {
        return [ domain.label, range.label ];
    }
    throw "getMapFieldNames given map type without field names: <prettyPrintAType(t)>";        
}

@doc{Get the field name for the field at a specific index.}
str getMapFieldName(AType t, int idx) = getMapFieldNames(t)[idx];

@doc{Get the domain type of the map.}    
public AType getMapDomainType(AType t) = unwrapType(getMapFields(t)[0]);

@doc{Get the range type of the map.}
public AType getMapRangeType(AType t) = unwrapType(getMapFields(t)[1]);

@doc{Get a list of the argument types in a constructor.}
public list[AType] getConstructorArgumentTypes(AType ct) {
    if (acons(_,_,cts) := unwrapType(ct)) return cts;
    throw "Cannot get constructor arguments from non-constructor type <prettyPrintAType(ct)>";
}

@doc{Get a tuple with the argument types as the fields.}
public AType getConstructorArgumentTypesAsTuple(AType ct) {
    return atuple(getConstructorArgumentTypes(ct));
}

@doc{Get the ADT type of the constructor.}
public AType getConstructorResultType(AType ct) {
    if (acons(a,_,_) := unwrapType(ct)) return a;
    throw "Cannot get constructor ADT type from non-constructor type <prettyPrintAType(ct)>";
}

@doc{Get the element type of a list.}
public AType getListElementType(AType t) {
    if (alist(et) := unwrapType(t)) return et;
    if (alrel(ets) := unwrapType(t)) return atuple(ets);    
    throw "Error: Cannot get list element type from type <prettyPrintAType(t)>";
}

@doc{Get the name of the ADT.}
str getADTName(AType t) {
    if (aadt(n,_,_) := unwrapType(t)) return n;
    if (acons(a,_,_) := unwrapType(t)) return getADTName(a);
    if (areified(_) := unwrapType(t)) return "type";
    throw "getADTName, invalid type given: <prettyPrintAType(t)>";
}

@doc{Get the type parameters of an ADT.}
public list[AType] getADTTypeParameters(AType t) {
    if (aadt(n,ps,_) := unwrapType(t)) return ps;
    if (acons(a,_,_) := unwrapType(t)) return getADTTypeParameters(a);
    if (areified(_) := unwrapType(t)) return [];
    throw "getADTTypeParameters given non-ADT type <prettyPrintAType(t)>";
}

@doc{Return whether the ADT has type parameters.}
bool adtHasTypeParameters(AType t) = size(getADTTypeParameters(t)) > 0;

//@doc{Get the name of a user type.}
//str getUserTypeName(AType ut) {
//    if (\user(x,_) := unwrapType(ut)) return prettyPrintName(x);
//    throw "Cannot get user type name from non user type <prettyPrintAType(ut)>";
//} 

//@doc{Get the type parameters from a user type.}
//public list[AType] getUserTypeParameters(AType ut) {
//    if (\user(_,ps) := unwrapType(ut)) return ps;
//    throw "Cannot get type parameters from non user type <prettyPrintAType(ut)>";
//}

//@doc{Does this user type have type parameters?}
//bool userTypeHasParameters(AType ut) = size(getUserTypeParameters(ut)) > 0;

//@doc{Get the name of the type alias.}
//str getAliasName(AType t) {
//    if (aalias(x,_,_) := t) return x;
//    throw "Cannot get the alias name from non alias type <prettyPrintAType(t)>";
//}

//@doc{Get the aliased type of the type alias.}
//public AType getAliasedType(AType t) {
//    if (aalias(_,_,at) := t) return at;
//    throw "Cannot get the aliased type from non alias type <prettyPrintAType(t)>";
//}

//@doc{Get the type parameters for the alias.}
//public list[AType] getAliasTypeParameters(AType t) {
//    if (aalias(_,ps,_) := t) return ps;
//    throw "getAliasTypeParameters given non-alias type <prettyPrintAType(t)>";
//}
//
//@doc{Does this alias have type parameters?}
//bool aliasHasTypeParameters(AType t) = size(getAliasTypeParameters(t)) > 0;
//
//@doc{Unwind any aliases inside a type.}
//public AType unwindAliases(AType t) {
//    solve(t) {
//        t = visit(t) { case aalias(tl,ps,tr) => tr };
//    }
//    return t;
//}

@doc{Is this type an overloaded type?}
bool isOverloadedType(\overloaded(_,_)) = true;
public default bool isOverloadedType(AType _) = false;

@doc{Get the non-default overloads stored inside the overloaded type.}
public set[AType] getNonDefaultOverloadOptions(AType t) {
    if (\overloaded(s,_) := t) return s;
    throw "Error: Cannot get non-default overloaded options from non-overloaded type <prettyPrintAType(t)>";
}

@doc{Get the default overloads stored inside the overloaded type.}
public set[AType] getDefaultOverloadOptions(AType t) {
    if (\overloaded(_,defaults) := t) return defaults;
    throw "Error: Cannot get default overloaded options from non-overloaded type <prettyPrintAType(t)>";
}

@doc{Construct a new overloaded type.}
public AType makeOverloadedType(set[AType] options, set[AType] defaults) {
    options  = { *( (\overloaded(opts,_) := optItem) ? opts : { optItem } ) | optItem <- options };
    defaults = defaults + { *( (\overloaded(_,opts) := optItem) ? opts : {} ) | optItem <- options };
    return \overloaded(options,defaults);
}

@doc{Ensure that sets of tuples are treated as relations.}
public AType aset(AType t) = arel(atypeList(getTupleFields(t))) when isTupleType(t);

@doc{Ensure that lists of tuples are treated as list relations.}
public AType alist(AType t) = alrel(atypeList(getTupleFields(t))) when isTupleType(t);

@doc{Calculate the lub of a list of types.}
public AType lubList(list[AType] ts) {
    AType theLub = avoid();
    for (t <- ts) theLub = lub(theLub,t);
    return theLub;
}

@doc{Is this type a non-container type?}
bool isElementType(AType t) = 
    isIntType(t) || isBoolType(t) || isRealType(t) || isRatType(t) || isStrType(t) || 
    isNumType(t) || isNodeType(t) || isVoidType(t) || isValueType(t) || isLocType(t) || 
    isDateTimeType(t) || isTupleType(t) || isADTType(t) || isConstructorType(t) ||
    isFunctionType(t) || isReifiedType(t) || isNonTerminalType(t);

@doc{Is this type a container type?}
bool isContainerType(AType t) =
    isSetType(t) || isListType(t) || isMapType(t) || isBagType(t);
    
@doc{Synopsis: Determine if the given type is a nonterminal.}
bool isNonTerminalType(aalias(_,_,AType at)) = isNonTerminalType(at);
bool isNonTerminalType(aparameter(_,AType tvb)) = isNonTerminalType(tvb);
bool isNonTerminalType(alabel(_,AType lt)) = isNonTerminalType(lt);
bool isNonTerminalType(AType::\start(AType ss)) = isNonTerminalType(ss);
bool isNonTerminalType(AType::\conditional(AType ss,_)) = isNonTerminalType(ss);
bool isNonTerminalType(AType::\sort(_)) = true;
bool isNonTerminalType(AType::\lex(_)) = true;
bool isNonTerminalType(AType::\layouts(_)) = true;
bool isNonTerminalType(AType::\keywords(_)) = true;
bool isNonTerminalType(AType::\parameterized-sort(_,_)) = true;
bool isNonTerminalType(AType::\parameterized-lex(_,_)) = true;
bool isNonTerminalType(AType::\iter(_)) = true;
bool isNonTerminalType(AType::\iter-star(_)) = true;
bool isNonTerminalType(AType::\iter-seps(_,_)) = true;
bool isNonTerminalType(AType::\iter-star-seps(_,_)) = true;
bool isNonTerminalType(AType::\empty()) = true;
bool isNonTerminalType(AType::\opt(_)) = true;
bool isNonTerminalType(AType::\alt(_)) = true;
bool isNonTerminalType(AType::\seq(_)) = true;

public default bool isNonTerminalType(AType _) = false;    

bool isNonTerminalIterType(aalias(_,_,AType at)) = isNonTerminalIterType(at);
bool isNonTerminalIterType(aparameter(_,Symbol tvb)) = isNonTerminalIterType(tvb);
bool isNonTerminalIterType(alabel(_,Symbol lt)) = isNonTerminalIterType(lt);
bool isNonTerminalIterType(AType::\iter(_)) = true;
bool isNonTerminalIterType(AType::\iter-star(_)) = true;
bool isNonTerminalIterType(AType::\iter-seps(_,_)) = true;
bool isNonTerminalIterType(AType::\iter-star-seps(_,_)) = true;
public default bool isNonTerminalIterType(Symbol _) = false;    

public Symbol getNonTerminalIterElement(aalias(_,_,Symbol at)) = getNonTerminalIterElement(at);
public Symbol getNonTerminalIterElement(aparameter(_,Symbol tvb)) = getNonTerminalIterElement(tvb);
public Symbol getNonTerminalIterElement(alabel(_,Symbol lt)) = getNonTerminalIterElement(lt);
public Symbol getNonTerminalIterElement(AType::\iter(Symbol i)) = i;
public Symbol getNonTerminalIterElement(AType::\iter-star(Symbol i)) = i;
public Symbol getNonTerminalIterElement(AType::\iter-seps(Symbol i,_)) = i;
public Symbol getNonTerminalIterElement(AType::\iter-star-seps(Symbol i,_)) = i;
public default Symbol getNonTerminalIterElement(Symbol i) {
    throw "<prettyPrintAType(i)> is not an iterable non-terminal type";
}   

bool isNonTerminalOptType(aalias(_,_,Symbol at)) = isNonTerminalOptType(at);
bool isNonTerminalOptType(aparameter(_,Symbol tvb)) = isNonTerminalOptType(tvb);
bool isNonTerminalOptType(alabel(_,Symbol lt)) = isNonTerminalOptType(lt);
bool isNonTerminalOptType(AType::\opt(Symbol ot)) = true;
public default bool isNonTerminalOptType(Symbol _) = false;

public Symbol getNonTerminalOptType(aalias(_,_,Symbol at)) = getNonTerminalOptType(at);
public Symbol getNonTerminalOptType(aparameter(_,Symbol tvb)) = getNonTerminalOptType(tvb);
public Symbol getNonTerminalOptType(alabel(_,Symbol lt)) = getNonTerminalOptType(lt);
public Symbol getNonTerminalOptType(AType::\opt(Symbol ot)) = ot;
public default Symbol getNonTerminalOptType(Symbol ot) {
    throw "<prettyPrintAType(ot)> is not an optional non-terminal type";
}

bool isStartNonTerminalType(aalias(_,_,Symbol at)) = isNonTerminalType(at);
bool isStartNonTerminalType(aparameter(_,Symbol tvb)) = isNonTerminalType(tvb);
bool isStartNonTerminalType(alabel(_,Symbol lt)) = isNonTerminalType(lt);
bool isStartNonTerminalType(AType::\start(_)) = true;
public default bool isStartNonTerminalType(Symbol _) = false;    

public Symbol getStartNonTerminalType(aalias(_,_,Symbol at)) = getStartNonTerminalType(at);
public Symbol getStartNonTerminalType(aparameter(_,Symbol tvb)) = getStartNonTerminalType(tvb);
public Symbol getStartNonTerminalType(alabel(_,Symbol lt)) = getStartNonTerminalType(lt);
public Symbol getStartNonTerminalType(AType::\start(Symbol s)) = s;
public default Symbol getStartNonTerminalType(Symbol s) {
    throw "<prettyPrintAType(s)> is not a start non-terminal type";
}

@doc{Get the name of the nonterminal.}
str getNonTerminalName(aalias(_,_,Symbol at)) = getNonTerminalName(at);
str getNonTerminalName(aparameter(_,Symbol tvb)) = getNonTerminalName(tvb);
str getNonTerminalName(alabel(_,Symbol lt)) = getNonTerminalName(lt);
str getNonTerminalName(AType::\start(Symbol ss)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\sort(str n)) = n;
str getNonTerminalName(AType::\lex(str n)) = n;
str getNonTerminalName(AType::\layouts(str n)) = n;
str getNonTerminalName(AType::\keywords(str n)) = n;
str getNonTerminalName(AType::\aparameterized-sort(str n,_)) = n;
str getNonTerminalName(AType::\aparameterized-lex(str n,_)) = n;
str getNonTerminalName(AType::\iter(Symbol ss)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\iter-star(Symbol ss)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\iter-seps(Symbol ss,_)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\iter-star-seps(Symbol ss,_)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\opt(Symbol ss)) = getNonTerminalName(ss);
str getNonTerminalName(AType::\conditional(Symbol ss,_)) = getNonTerminalName(ss);
public default str getNonTerminalName(Symbol s) { throw "Invalid nonterminal passed to getNonTerminalName: <s>"; }

@doc{Check to see if the type allows fields.}
bool nonTerminalAllowsFields(aalias(_,_,Symbol at)) = nonTerminalAllowsFields(at);
bool nonTerminalAllowsFields(aparameter(_,Symbol tvb)) = nonTerminalAllowsFields(tvb);
bool nonTerminalAllowsFields(alabel(_,Symbol lt)) = nonTerminalAllowsFields(lt);
bool nonTerminalAllowsFields(AType::\start(Symbol ss)) = true;
bool nonTerminalAllowsFields(AType::\sort(str n)) = true;
bool nonTerminalAllowsFields(AType::\lex(str n)) = true;
bool nonTerminalAllowsFields(AType::\parameterized-sort(str n,_)) = true;
bool nonTerminalAllowsFields(AType::\parameterized-lex(str n,_)) = true;
bool nonTerminalAllowsFields(AType::\opt(Symbol ss)) = true;
bool nonTerminalAllowsFields(AType::\conditional(Symbol ss,_)) = nonTerminalAllowsFields(ss);
public default bool nonTerminalAllowsFields(Symbol s) = false;

@doc{Get the type parameters of a nonterminal.}
//public list[Symbol] getNonTerminalTypeParameters(Symbol t) = [ rt | / Symbol rt : aparameter(_,_) := t ];
public list[Symbol] getNonTerminalTypeParameters(Symbol t) {
    t = unwrapType(t);
    if (AType::aparameterized-sort(n,ps) := t) return ps;
    if (AType::aparameterized-lex(n,ps) := t) return ps;
    if (AType::\start(s) := t) return getNonTerminalTypeParameters(s);
    if (AType::\iter(s) := t) return getNonTerminalTypeParameters(s);
    if (AType::\iter-star(s) := t) return getNonTerminalTypeParameters(s);
    if (AType::\iter-seps(s,_) := t) return getNonTerminalTypeParameters(s);
    if (AType::\iter-star-seps(s,_) := t) return getNonTerminalTypeParameters(s);
    if (AType::\opt(s) := t) return getNonTerminalTypeParameters(s);
    if (AType::\conditional(s,_) := t) return getNonTerminalTypeParameters(s);
    if (AType::\prod(s,_,_,_) := t) return getNonTerminalTypeParameters(s);
    return [ ];
}

public Symbol provideNonTerminalTypeParameters(Symbol t, list[Symbol] ps) {
    // Note: this function assumes the length is proper -- that we are replacing
    // a list of params with a list of types that is the same length. The caller
    // needs to check this.
    
    t = unwrapType(t);
    
    if (AType::aparameterized-sort(n,ts) := t) return t[parameters=ps];
    if (AType::aparameterized-lex(n,ts) := t) return t[parameters=ps];
    if (AType::\start(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\iter(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\iter-star(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\iter-seps(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\iter-star-seps(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\opt(s) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\conditional(s,_) := t) return t[symbol=provideNonTerminalTypeParameters(s,ps)];
    if (AType::\prod(s,_,_,_) := t) return t[\sort=provideNonTerminalTypeParameters(s,ps)];
    return t;
}

@doc{Synopsis: Determine if the given type is a production.}
bool isProductionType(aalias(_,_,Symbol at)) = isProductionType(at);
bool isProductionType(aparameter(_,Symbol tvb)) = isProductionType(tvb);
bool isProductionType(alabel(_,Symbol lt)) = isProductionType(lt);
bool isProductionType(AType::\prod(_,_,_,_)) = true;
public default bool isProductionType(Symbol _) = false; 

public Symbol removeConditional(conditional(Symbol s, set[Condition] _)) = s;
public Symbol removeConditional(label(str lab, conditional(Symbol s, set[Condition] _)))
  = label(lab, s);
public default Symbol removeConditional(Symbol s) = s;

@doc{Get a list of the argument types in a production.}
public list[Symbol] getProductionArgumentTypes(Symbol pr) {
    if (AType::\prod(_,_,ps,_) := unwrapType(pr)) {
        return [ removeConditional(psi) | psi <- ps, isNonTerminalType(psi) ] ;
    }
    throw "Cannot get production arguments from non-production type <prettyPrintAType(pr)>";
}

@doc{Get a tuple with the argument types as the fields.}
public Symbol getProductionArgumentTypesAsTuple(Symbol pr) {
    return atuple(getProductionArgumentTypes(pr));
}

@doc{Get the sort type of the production.}
public Symbol getProductionSortType(Symbol pr) {
    if (AType::\prod(s,_,_,_) := unwrapType(pr)) return s;
    throw "Cannot get production sort type from non-production type <prettyPrintAType(pr)>";
}

bool hasDeferredTypes(Symbol t) = size({d | /d:deferred(_) := t}) > 0;

bool subtype(AType::deferred(Symbol t), Symbol s) = subtype(t,s);
bool subtype(Symbol t, AType::deferred(Symbol s)) = subtype(t,s); 
bool subtype(Symbol t, AType::aadt("Tree",[])) = true when isNonTerminalType(t);
bool subtype(Symbol t, AType::anode()) = true when isNonTerminalType(t);
bool subtype(AType::\iter-seps(Symbol s, list[Symbol] seps), AType::\iter-star-seps(Symbol t, list[Symbol] seps2)) = subtype(s,t) && subtype(seps, seps2);
bool subtype(AType::\iter(Symbol s), AType::\iter-star(Symbol t)) = subtype(s, t);
// TODO: add subtype for elements under optional and alternative, but that would also require auto-wrapping/unwrapping in the run-time
// bool subtype(Symbol s, \opt(Symbol t)) = subtype(s,t);
// bool subtype(Symbol s, \alt({Symbol t, *_}) = true when subtype(s, t); // backtracks over the alternatives

