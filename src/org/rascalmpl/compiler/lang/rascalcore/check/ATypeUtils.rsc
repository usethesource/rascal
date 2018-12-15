
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
@bootstrapParser
module lang::rascalcore::check::ATypeUtils

import List;
import Set;
import String;
import Node;
import IO;
extend ParseTree;

//extend analysis::typepal::TypePal;
extend lang::rascalcore::check::AType;

import lang::rascalcore::check::ATypeInstantiation;

import lang::rascalcore::check::TypePalConfig;
import lang::rascalcore::check::ATypeExceptions;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::format::Escape;

str unescape(str s) = replaceAll(s, "\\", "");

AType removeLabels(AType t) = unsetRec(t, "label");

// ---- print atypes ----------------------------------------------------------

@doc{Pretty printer for Rascal abstract types.}
str prettyAType(aint()) = "int";
str prettyAType(abool()) = "bool";
str prettyAType(areal()) = "real";
str prettyAType(arat()) = "rat";
str prettyAType(astr()) = "str";
str prettyAType(anum()) = "num";
str prettyAType(anode(list[AType fieldType] fields)) = isEmpty(fields) ? "node" : "node(<intercalate(", ", ["<prettyAType(ft)> <ft.label> = ..." | ft <- fields])>)";
str prettyAType(avoid()) = "void";
str prettyAType(avalue()) = "value";
str prettyAType(aloc()) = "loc";
str prettyAType(adatetime()) = "datetime";
str prettyAType(alist(AType t)) = "list[<prettyAType(t)>]";
str prettyAType(aset(AType t)) = "set[<prettyAType(t)>]";
str prettyAType(atuple(AType ts)) = "tuple[<prettyAType(ts)>]";
str prettyAType(amap(AType d, AType r)) = "map[<prettyAType(d)>, <prettyAType(r)>]";
str prettyAType(arel(AType ts)) = "rel[<prettyAType(ts)>]";
str prettyAType(alrel(AType ts)) = "lrel[<prettyAType(ts)>]";

str prettyAType(afunc(AType ret, list[AType] formals, lrel[AType fieldType, Expression defaultExp] kwFormals))
                = "<prettyAType(ret)>(<intercalate(",", [prettyAType(f) | f <- formals])><isEmpty(kwFormals) ? "" : ", "><intercalate(",", ["<prettyAType(ft)> <ft.label>=..." | <ft, de> <- kwFormals])>)";

str prettyAType(aalias(str aname, [], AType aliased)) = "alias <aname> = <prettyAType(aliased)>";
str prettyAType(aalias(str aname, ps, AType aliased)) = "alias <aname>[<prettyAType(ps)>] = <prettyAType(aliased)>" when size(ps) > 0;

str prettyAType(aanno(str aname, AType onType, AType annoType)) = "anno <prettyAType(annoType)> <prettyAType(onType)>@<aname>";

str prettyAType(aadt(str s, [], SyntaxRole _)) = s;
str prettyAType(aadt(str s, ps, SyntaxRole _)) = "<s>[<prettyAType(ps)>]" when size(ps) > 0;

str prettyAType(t: acons(AType adt, /*str consName,*/ 
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "<prettyAType(adt)>::<t.label>(<intercalate(", ", ["<prettyAType(ft)><ft.label? ? " <ft.label>" : "">" | ft <- fields])><isEmpty(kwFields) ? "" : ", "><intercalate(",", ["<prettyAType(ft)> <ft.label>=..." | <ft, de> <- kwFields])>)";

str prettyAType(amodule(str mname)) = "module <mname>";         
str prettyAType(aparameter(str pn, AType t)) = t == avalue() ? "&<pn>" : "&<pn> \<: <prettyAType(t)>";
str prettyAType(areified(AType t)) = "type[<prettyAType(t)>]";

// utilities
str prettyAType(overloadedAType(rel[loc, IdRole, AType] overloads))
                = intercalateOr([prettyAType(t1) | t1 <- {t | <k, idr, t> <- overloads} ]);

str prettyAType(list[AType] atypes) = intercalate(", ", [prettyAType(t) | t <- atypes]);

str prettyAType(Keyword kw) = "<prettyAType(kw.fieldType) <kw.fieldType.label/*fieldName*/> = <kw.defaultExp>";

// non-terminal symbols
str prettyAType(\prod(AType s, list[AType] fs/*, SyntaxRole _*/)) = "<prettyAType(s)> : (<intercalate(", ", [ prettyAType(f) | f <- fs ])>)"; //TODO others

// terminal symbols
str prettyAType(AType::\lit(str string)) = string;
str prettyAType(AType::\cilit(str string)) = string;
str prettyAType(\char-class(list[ACharRange] ranges)) = "[<intercalate(" ", [ "<r.begin>-<r.end>" | r <- ranges ])>]";

str prettyAType(\start(AType symbol)) = "start[<prettyAType(symbol)>]";

// regular symbols
str prettyAType(AType::\empty()) = "()";
str prettyAType(\opt(AType symbol)) = "<prettyAType(symbol)>?";
str prettyAType(\iter(AType symbol)) = "<prettyAType(symbol)>+";
str prettyAType(\iter-star(AType symbol)) = "<prettyAType(symbol)>*";
str prettyAType(\iter-seps(AType symbol, list[AType] separators)) = "{<prettyAType(symbol)> <intercalate(" ", [ prettyAType(sep) | sep <- separators ])>}+";
str prettyAType(\iter-star-seps(AType symbol, list[AType] separators)) = "{<prettyAType(symbol)> <intercalate(" ", [ prettyAType(sep) | sep <- separators ])>}*";
str prettyAType(\alt(set[AType] alternatives)) = "( <intercalate(" | ", [ prettyAType(a) | a <- alternatives ])> )" when size(alternatives) > 1;
str prettyAType(\seq(list[AType] sequence)) = "( <intercalate(" ", [ prettyAType(a) | a <- sequence ])> )" when size(sequence) > 1;
str prettyAType(\conditional(AType symbol, set[ACondition] conditions)) = "<prettyAType(symbol)> { <intercalate(" ", [ prettyPrintCond(cond) | cond <- conditions ])> }";
default str prettyAType(AType s) = "<s>"; //"<type(s,())>";

private str prettyPrintCond(ACondition::\follow(AType symbol)) = "\>\> <prettyAType(symbol)>";
private str prettyPrintCond(ACondition::\not-follow(AType symbol)) = "!\>\> <prettyAType(symbol)>";
private str prettyPrintCond(ACondition::\precede(AType symbol)) = "<prettyAType(symbol)> \<\<";
private str prettyPrintCond(ACondition::\not-precede(AType symbol)) = "<prettyAType(symbol)> !\<\<";
private str prettyPrintCond(ACondition::\delete(AType symbol)) = "???";
private str prettyPrintCond(ACondition::\at-column(int column)) = "@<column>";
private str prettyPrintCond(ACondition::\begin-of-line()) = "^";
private str prettyPrintCond(ACondition::\end-of-line()) = "$";
private str prettyPrintCond(ACondition::\except(str label)) = "!<label>";

@doc{Rascal abstract types to classic Symbols}
Symbol atype2symbol(aint()) = \int();
Symbol atype2symbol(abool()) = \bool();
Symbol atype2symbol(areal()) = \real();
Symbol atype2symbol(arat()) = \rat();
Symbol atype2symbol(astr()) = \str();
Symbol atype2symbol(anum()) = \num();
Symbol atype2symbol(anode( list[AType fieldType] fields)) = \node();
Symbol atype2symbol(avoid()) = \void();
Symbol atype2symbol(avalue()) = \value();
Symbol atype2symbol(aloc()) = \loc();
Symbol atype2symbol(adatetime()) = \datetime();
Symbol atype2symbol(alist(AType t)) = \list(atype2symbol(t));
Symbol atype2symbol(aset(AType t)) = \set(atype2symbol(t));
Symbol atype2symbol(atuple(atypeList(list[AType] ts))) = \tuple([atype2symbol(t) | t <- ts]);
Symbol atype2symbol(amap(AType d, AType r)) = \map(atype2symbol(d), atype2symbol(r));
Symbol atype2symbol(arel(atypeList(list[AType] ts))) = \rel([atype2symbol(t) | t <- ts]);
Symbol atype2symbol(alrel(atypeList(list[AType] ts))) = \lrel([atype2symbol(t) | t <- ts]);

// TODO complete conversion from str to Symbol

Symbol atype2symbol(afunc(AType ret, list[AType] formals, lrel[AType fieldType, Expression defaultExp] kwFormals))
                = "<atype2symbol(ret)>(<intercalate(",", [atype2symbol(f) | f <- formals])>)";

Symbol atype2symbol(aalias(str aname, [], AType aliased)) = \alias(aname,[],atype2symbol(aliased));
Symbol atype2symbol(aalias(str aname, ps, AType aliased)) = \alias(aname,[atype2symbol(p) | p<-ps], atype2symbol(aliased)) when size(ps) > 0;

Symbol atype2symbol(aanno(str aname, AType onType, AType annoType)) = \anno(aname,atype2symbol(annoType), atype2symbol(onType));

Symbol atype2symbol(aadt(str s, [], contextFreeSyntax()))  = Symbol::\sort(s);
Symbol atype2symbol(aadt(str s, [], lexicalSyntax()))      = Symbol::\lex(s);
Symbol atype2symbol(aadt(str s, [], keywordSyntax()))      = Symbol::\keywords(s);
Symbol atype2symbol(aadt(str s, [], layoutSyntax()))       = Symbol::\layouts(s);

Symbol atype2symbol(aadt(str s, ps, contextFreeSyntax)) = \parameterized-sort(s, [atype2symbol(p) | p <- ps]) when size(ps) > 0;
Symbol atype2symbol(aadt(str s, ps, lexicalSyntax())) = \parameterized-lex(s, [atype2symbol(p) | p <- ps]) when size(ps) > 0;

Symbol atype2symbol(t: acons(AType adt,
                list[AType fieldType] fields,
                lrel[AType fieldType, Expression defaultExp] kwFields))
                 = "<atype2symbol(adt)>::<t.label>(<intercalate(", ", ["<atype2symbol(ft)> <ft.label>" | ft <- fields])><isEmpty(kwFields) ? "" : ", "><intercalate(",", ["<atype2symbol(ft)> <ft.label>=..." | <ft, de> <- kwFields])>)";

Symbol atype2symbol(amodule(str mname)) = "\\module(\"<mname>\")";         
Symbol atype2symbol(aparameter(str pn, AType t)) = "\\parameter(\"<pn>\", <atype2symbol(t)>)";
Symbol atype2symbol(areified(AType t)) = "\\reified(<atype2symbol(t)>)";

// utilities
Symbol atype2symbol(overloadedAType(rel[loc, IdRole, AType] overloads))
                = intercalateOr([atype2symbol(t1) | t1 <- {t | <k, idr, t> <- overloads} ]);

Symbol atype2symbol(list[AType] atypes) = intercalate(", ", [atype2symbol(t) | t <- atypes]);

Symbol atype2symbol(Keyword kw) = "<atype2symbol(kw.fieldType) <kw.fieldType.label/*fieldName*/> = <kw.defaultExp>";

// non-terminal symbols
Symbol atype2symbol(\prod(AType s, list[AType] fs, attributes=ats)) = prod(atype2symbol(s), [ atype2symbol(f) | f <- fs ], ats); //TODO others

// terminal symbols
Symbol atype2symbol(AType::\lit(str string)) = Symbol::\lit(string);
Symbol atype2symbol(AType::\cilit(str string)) = Symbol::\cilit(string);
Symbol atype2symbol(\char-class(list[ACharRange] ranges)) = Symbol::\char-class([range(<r.begin>,<r.end>) | r <- ranges ]);

Symbol atype2symbol(\start(AType symbol)) = "\\start(<atype2symbol(symbol)>)";

// regular symbols
Symbol atype2symbol(AType::\empty()) = Symbol::\empty();
Symbol atype2symbol(\opt(AType symbol)) = Symbol::\opt(atype2symbol(symbol));
Symbol atype2symbol(\iter(AType symbol)) = Symbol::\iter(atype2symbol(symbol));
Symbol atype2symbol(\iter-star(AType symbol)) = Symbol::\iter-star(atype2symbol(symbol));
Symbol atype2symbol(\iter-seps(AType symbol, list[AType] separators)) = "\\iter-seps(<atype2symbol(symbol)>, [<intercalate(",", [ atype2symbol(sep) | sep <- separators ])>])";
Symbol atype2symbol(\iter-star-seps(AType symbol, list[AType] separators)) = "\\iter-star-seps(<atype2symbol(symbol)>, [<intercalate(",", [ atype2symbol(sep) | sep <- separators ])>])";
Symbol atype2symbol(\alt(set[AType] alternatives)) = "\\alt({<intercalate(", ", [ atype2symbol(a) | a <- alternatives ])>})" when size(alternatives) > 1;
Symbol atype2symbol(\seq(list[AType] sequence)) = "\\seq([<intercalate(",", [ atype2symbol(a) | a <- sequence ])>])" when size(sequence) > 1;
Symbol atype2symbol(\conditional(AType symbol, set[ACondition] conditions)) = "\\conditional(<atype2symbol(symbol)>, {<intercalate(",", [ acond2cond(cond) | cond <- conditions ])>} )";
default Symbol atype2symbol(AType s)  { throw "<s>"; } //"<type(s,())>";

private Symbol acond2cond(ACondition::\follow(AType symbol)) = \follow(atype2symbol(symbol));
private Symbol acond2cond(ACondition::\not-follow(AType symbol)) = \not-follow(atype2symbol(symbol));
private Symbol acond2cond(ACondition::\precede(AType symbol)) = \precede(atype2symbol(symbol));
private Symbol acond2cond(ACondition::\not-precede(AType symbol)) = \not-precede(atype2symbol(symbol));
private Symbol acond2cond(ACondition::\delete(AType symbol)) = \delete(atype2symbol(symbol));
private Symbol acond2cond(ACondition::\at-column(int column)) = \at-column(column);
private Symbol acond2cond(ACondition::\begin-of-line()) = \begin-of-line();
private Symbol acond2cond(ACondition::\end-of-line()) = \end-of-line();
private Symbol acond2cond(ACondition::\except(str label)) = \except(label);

Symbol atype2symbol(regular(AType def)) = \regular(atype2symbol(def));

// ---- Predicates, selectors and constructors --------------------------------

// ---- utilities

@doc{Unwraps parameters and conditionals from a type.}
AType unwrapType(p: aparameter(_,tvb)) = p.label? ? unwrapType(tvb)[label=p.label] : unwrapType(tvb);
AType unwrapType(\conditional(AType sym,  set[ACondition] _)) = unwrapType(sym);
default AType unwrapType(AType t) = t;

bool allLabelled(list[AType] tls) = size(tls) == size([tp | tp <- tls, !isEmpty(tp.label)]);

// ---- int

@doc{
.Synopsis
Determine if the given type is an int.
}
bool isIntType(aparameter(_,AType tvb)) = isIntType(tvb);
bool isIntType(aint()) = true;
default bool isIntType(AType _) = false;

@doc{Create a new int type.}
AType makeIntType() = aint();

// ---- bool 
@doc{
.Synopsis
Determine if the given type is a bool.
}
bool isBoolType(aparameter(_,AType tvb)) = isBoolType(tvb);
bool isBoolType(abool()) = true;
default bool isBoolType(AType _) = false;

@doc{Create a new bool type.}
AType makeBoolType() = abool();

// ---- real

@doc{
.Synopsis
Determine if the given type is a real.
}
bool isRealType(aparameter(_,AType tvb)) = isRealType(tvb);
bool isRealType(areal()) = true;
default bool isRealType(AType _) = false;

@doc{Create a new real type.}
AType makeRealType() = areal();

// ---- rat

@doc{
.Synopsis
Determine if the given type is a rational.
}
bool isRatType(aparameter(_,AType tvb)) = isRatType(tvb);
bool isRatType(arat()) = true;
default bool isRatType(AType _) = false;

@doc{Create a new rat type.}
AType makeRatType() = arat();

// ---- str

@doc{
.Synopsis
Determine if the given type is a string.
}
bool isStrType(aparameter(_,AType tvb)) = isStrType(tvb);
bool isStrType(astr()) = true;
default bool isStrType(AType _) = false;

@doc{Create a new str type.}
AType makeStrType() = astr();

// ---- num

@doc{
.Synopsis
Determine if the given type is a num.
}
bool isNumType(aparameter(_,AType tvb)) = isNumType(tvb);
bool isNumType(anum()) = true;
default bool isNumType(AType _) = false;

bool isNumericType(AType t) = isIntType(t) || isRealType(t) || isRatType(t) || isNumType(t);

@doc{Create a new num type.}
AType makeNumType() = anum();

// ---- node

@doc{
.Synopsis
Determine if the given type is a node.
}
bool isNodeType(aparameter(_,AType tvb)) = isNodeType(tvb);
bool isNodeType(anode(_)) = true;
bool isNodeType(aadt(_,_,_)) = true;
default bool isNodeType(AType _) = false;

@doc{Create a new node type.}
AType makeNodeType() = anode([]);

// ---- hasKeywordParameters 

bool hasKeywordParameters(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals))
    = !isEmpty(kwFormals);
    
bool hasKeywordParameters(acons(AType adt, list[AType] fields, list[Keyword] kwFields))
    = !isEmpty(kwFields);
    
bool hasKeywordParameters(overloadedAType(overloads))
    = any(<loc k, IdRole idr, AType t> <- overloads, hasKeywordParameters(t));

default bool hasKeywordParameters(AType t) = false;


// ---- void

@doc{
.Synopsis
Determine if the given type is a void.
}
bool isVoidType(aparameter(_,AType tvb)) = isVoidType(tvb);
bool isVoidType(avoid()) = true;
default bool isVoidType(AType _) = false;

@doc{Create a new void type.}
AType makeVoidType() = avoid();

// ---- value

@doc{
.Synopsis
Determine if the given type is a value.
}
bool isValueType(aparameter(_,AType tvb)) = isValueType(tvb);
bool isValueType(avalue()) = true;
default bool isValueType(AType _) = false;

@doc{Create a new value type.}
AType makeValueType() = avalue();

// ---- loc

@doc{
.Synopsis
Determine if the given type is a loc.
}
bool isLocType(aparameter(_,AType tvb)) = isLocType(tvb);
bool isLocType(aloc()) = true;
default bool isLocType(AType _) = false;

@doc{Create a new loc type.}
AType makeLocType() = aloc();

// ---- datetime

@doc{
.Synopsis
Determine if the given type is a `datetime`.
}
bool isDateTimeType(aparameter(_,AType tvb)) = isDateTimeType(tvb);
bool isDateTimeType(adatetime()) = true;
default bool isDateTimeType(AType _) = false;

@doc{Create a new datetime type.}
AType makeDateTimeType() = adatetime();

// ---- set

@doc{
.Synopsis
Determine if the given type is a set.
}
bool isSetType(aparameter(_,AType tvb)) = isSetType(tvb);
bool isSetType(aset(_)) = true;
bool isSetType(arel(_)) = true;
default bool isSetType(AType _) = false;

@doc{Create a new set type, given the element type of the set.}
AType makeSetType(AType elementType) {
    return isTupleType(elementType) ? makeRelTypeFromTuple(elementType) : aset(elementType);
}

@doc{Get the element type of a set.}
AType getSetElementType(AType t) {
    if (aset(et) := unwrapType(t)) return et;
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    throw rascalCheckerInternalError("Error: Cannot get set element type from type <prettyAType(t)>");
}

// ---- rel

@doc{
.Synopsis
Determine if the given type is a relation.
}
bool isRelType(aparameter(_,AType tvb)) = isRelType(tvb);
bool isRelType(arel(_)) = true;
bool isRelType(aset(AType tp)) = true when isTupleType(tp);
default bool isRelType(AType _) = false;

@doc{Ensure that sets of tuples are treated as relations.}
AType aset(AType t) = arel(atypeList(getTupleFields(t))) when isTupleType(t);

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

@doc{Get the type of the relation fields as a tuple.}
AType getRelElementType(AType t) {
    if (arel(ets) := unwrapType(t)) return atuple(ets);
    if (aset(tup) := unwrapType(t)) return tup;
    throw rascalCheckerInternalError("Cannot get relation element type from type <prettyAType(t)>");
}

@doc{Get whether the rel has field names or not.}
bool relHasFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return size(tls) == size([tp | tp <- tls, !isEmpty(tp.label)]);
    throw rascalCheckerInternalError("relHasFieldNames given non-Relation type <prettyAType(t)>");
}

@doc{Get the field names of the rel fields.}
list[str] getRelFieldNames(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)){
        return [tp.label | tp <- tls];
    }
    throw rascalCheckerInternalError("getRelFieldNames given non-Relation type <prettyAType(t)>");
}

@doc{Get the fields of a relation.}
list[AType] getRelFields(AType t) {
    if (arel(atypeList(tls)) := unwrapType(t)) return tls;
    if (aset(atuple(atypeList(tls))) := unwrapType(t)) return tls;
    throw rascalCheckerInternalError("getRelFields given non-Relation type <prettyAType(t)>");
}

// ---- lrel

@doc{
.Synopsis
Determine if the given type is a list relation.
}
bool isListRelType(aparameter(_,AType tvb)) = isListRelType(tvb);
bool isListRelType(alrel(_)) = true;
bool isListRelType(alist(AType tp)) = true when isTupleType(tp);
default bool isListRelType(AType _) = false;

@doc{Ensure that lists of tuples are treated as list relations.}
AType alist(AType t) = alrel(atypeList(getTupleFields(t))) when isTupleType(t);

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

@doc{Get the type of the list relation fields as a tuple.}
AType getListRelElementType(AType t) {
    if (alrel(ets) := unwrapType(t)) return atuple(ets);
    if (alist(tup) := unwrapType(t)) return tup;
    throw rascalCheckerInternalError("Cannot get list relation element type from type <prettyAType(t)>");
}

@doc{Get the field names of the list rel fields.}
list[str] getListRelFieldNames(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)){
        return [tp.label | tp <- tls];
    }
    throw rascalCheckerInternalError("getListRelFieldNames given non-List-Relation type <prettyAType(t)>");
}

@doc{Get the fields of a list relation.}
list[AType] getListRelFields(AType t) {
    if (alrel(atypeList(tls)) := unwrapType(t)) return tls;
    if (alist(atuple(atypeList(tls))) := unwrapType(t)) return tls;
    throw rascalCheckerInternalError("getListRelFields given non-List-Relation type <prettyAType(t)>");
}

// ---- tuple

@doc{
.Synopsis
Determine if the given type is a tuple.
}
bool isTupleType(aparameter(_,AType tvb)) = isTupleType(tvb);
bool isTupleType(atuple(_)) = true;
default bool isTupleType(AType _) = false;

@doc{Create a new tuple type, given the element types of the fields. Check any given labels for consistency.}
AType makeTupleType(AType elementTypes...) {
    set[str] labels = { tp.label | tp <- elementTypes, !isEmpty(tp.label) };
    if(size(labels) > 0 && size(labels) != size(elementTypes))
        elementTypes = [unset(e, "label") | e <- elementTypes];
    return atuple(atypeList(elementTypes));
    //else
    //    throw rascalCheckerInternalError("For tuple types, either all fields much be given a distinct label or no fields should be labeled."); 
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
AType getTupleFieldType(AType t, str fn) {
    if (atuple(atypeList(tas)) := unwrapType(t)) {
        for(tp <- tas){
            if(tp.label == fn) return tp;
        }
        throw rascalCheckerInternalError("Tuple <prettyAType(t)> does not have field <fn>");
    }
    throw rascalCheckerInternalError("getTupleFieldType given unexpected type <prettyAType(t)>");
}

@doc{Get the type of the tuple field at the given offset.}
AType getTupleFieldType(AType t, int fn) {
    if (atuple(atypeList(tas)) := t) {
        if (0 <= fn && fn < size(tas)) return unwrapType(tas[fn]);
        throw rascalCheckerInternalError("Tuple <prettyAType(t)> does not have field <fn>");
    }
    throw rascalCheckerInternalError("getTupleFieldType given unexpected type <prettyAType(t)>");
}

@doc{Get the types of the tuple fields, with labels removed}
list[AType] getTupleFieldTypes(AType t) {
    if (atuple(atypeList(tas)) := t)
        return tas;
    throw rascalCheckerInternalError("Cannot get tuple field types from type <prettyAType(t)>"); 
}

@doc{Get the fields of a tuple as a list.}
list[AType] getTupleFields(AType t) {
    if (atuple(atypeList(tas)) := unwrapType(t)) return tas;
    throw rascalCheckerInternalError("Cannot get tuple fields from type <prettyAType(t)>"); 
}

@doc{Get the number of fields in a tuple.}
int getTupleFieldCount(AType t) = size(getTupleFields(t));

@doc{Does this tuple have field names?}
bool tupleHasFieldNames(AType t) {
    if (tup: atuple(atypeList(tas)) := unwrapType(t)) return size(tas) == size([tp | tp <- tas, !isEmpty(tp.label)]);
    throw rascalCheckerInternalError("tupleHasFieldNames given non-Tuple type <prettyAType(t)>");
}

@doc{Get the names of the tuple fields.}
list[str] getTupleFieldNames(AType t) {
    if (atuple(atypeList(tls)) := unwrapType(t)) {
        if (allLabelled(tls)) {
            return [tp.label | tp <- tls];
        }
        throw rascalCheckerInternalError("getTupleFieldNames given tuple type without field names: <prettyAType(t)>");        
    }
    throw rascalCheckerInternalError("getTupleFieldNames given non-Tuple type <prettyAType(t)>");
}

@doc{Get the name of the tuple field at the given offset.}
str getTupleFieldName(AType t, int idx) {
    list[str] names = getTupleFieldNames(t);
    if (0 <= idx && idx < size(names)) return names[idx];
    throw rascalCheckerInternalError("getTupleFieldName given invalid index <idx>");
}

// ---- list

@doc{
.Synopsis
Determine if the given type is a list.
}
bool isListType(aparameter(_,AType tvb)) = isListType(tvb);
bool isListType(alist(_)) = true;
bool isListType(alrel(_)) = true;
default bool isListType(AType _) = false;

@doc{Create a new list type, given the element type of the list.}
AType makeListType(AType elementType) {
    return isTupleType(elementType) ? makeListRelTypeFromTuple(elementType) : alist(elementType);
} 

@doc{Get the element type of a list.}
AType getListElementType(AType t) {
    if (alist(et) := unwrapType(t)) return et;
    if (alrel(ets) := unwrapType(t)) return atuple(ets);    
    throw rascalCheckerInternalError("Cannot get list element type from type <prettyAType(t)>");
}  

// ---- map

@doc{
.Synopsis
Determine if the given type is a map.
}
bool isMapType(x:aparameter(_,AType tvb)) = isMapType(tvb);
bool isMapType(x:amap(_,_)) = true;
default bool isMapType(AType x) = false;

@doc{Create a new map type, given the types of the domain and range. Check to make sure field names are used consistently.}
AType makeMapType(AType domain, AType range) {
    if(!isEmpty(domain.label) && !isEmpty(range.label)){
        if(domain.label != range.label) return amap(domain, range);
        throw rascalCheckerInternalError("The field names of the map domain and range must be distinct; found `<domain.label>`");
    }
    else if(!isEmpty(domain.label)) return amap(unset(domain,"label"),range);
    else if(!isEmpty(range.label)) return amap(domain,unset(range, "label"));
    return amap(domain, range);
}

@doc{Get the domain and range of the map as a tuple.}
AType getMapFieldsAsTuple(AType t) {
    if (amap(dt,rt) := unwrapType(t)) return atuple(atypeList([dt,rt]));
    throw rascalCheckerInternalError("getMapFieldsAsTuple called with unexpected type <prettyAType(t)>");
}       

@doc{Check to see if a map defines a field (by name).}
bool mapHasField(AType t, str fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Check to see if a map defines a field (by index).}
bool mapHasField(AType t, int fn) = tupleHasField(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by name).}
AType getMapFieldType(AType t, str fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Return the type of a field defined on a map (by index).}
AType getMapFieldType(AType t, int fn) = getTupleFieldType(getMapFieldsAsTuple(t),fn);

@doc{Get the fields in a map as a list of fields.}
list[AType] getMapFields(AType t) = getTupleFields(getMapFieldsAsTuple(t));

@doc{Check to see if the map has field names.}
bool mapHasFieldNames(AType t) = tupleHasFieldNames(getMapFieldsAsTuple(t));

@doc{Get the field names from the map fields.}
list[str] getMapFieldNames(AType t) {
    if ([dm, rng] := getMapFields(t)) {
        return [ dm.label, rng.label ];
    }
    throw rascalCheckerInternalError("getMapFieldNames given map type without field names: <prettyAType(t)>");        
}

@doc{Get the field name for the field at a specific index.}
str getMapFieldName(AType t, int idx) = getMapFieldNames(t)[idx];

@doc{Get the domain type of the map.}    
AType getMapDomainType(AType t) = unwrapType(getMapFields(t)[0]);

@doc{Get the range type of the map.}
AType getMapRangeType(AType t) = unwrapType(getMapFields(t)[1]);

// ---- bag

@doc{
.Synopsis
Determine if the given type is a bag (bags are not yet implemented).
}
bool isBagType(aparameter(_,AType tvb)) = isBagType(tvb);
bool isBagType(abag(_)) = true;
default bool isBagType(AType _) = false;

@doc{Create a new bag type, given the element type of the bag.}
AType makeBagType(AType elementType) = abag(elementType);


@doc{Get the element type of a bag.}
AType getBagElementType(AType t) {
    if (abag(et) := unwrapType(t)) return et;
    throw rascalCheckerInternalError("Cannot get set element type from type <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is an Abstract Data Type (ADT).
}
bool isADTType(aparameter(_,AType tvb)) = isADTType(tvb);
bool isADTType(aadt(_,_,_)) = true;
bool isADTType(areified(_)) = true;
bool isADTType(\start(AType s)) = isADTType(s);
default bool isADTType(AType _) = false;

@doc{Create a new parameterized ADT type with the given type parameters}
AType makeParameterizedADTType(str n, AType p...) = aadt(n,p, dataSyntax());

@doc{Create a new ADT type with the given name.}
AType makeADTType(str n) = aadt(n,[], dataSyntax());

@doc{Get the name of the ADT.}
str getADTName(AType t) {
    if (aadt(n,_,_) := unwrapType(t)) return n;
    if (acons(a,_,_) := unwrapType(t)) return getADTName(a);
    if (\start(ss) := unwrapType(t)) return getADTName(ss);
    if (areified(_) := unwrapType(t)) return "type";
    throw rascalCheckerInternalError("getADTName, invalid type given: <prettyAType(t)>");
}

@doc{Get the type parameters of an ADT.}
list[AType] getADTTypeParameters(AType t) {
    if (aadt(n,ps,_) := unwrapType(t)) return ps;
    if (acons(a,_,_) := unwrapType(t)) return getADTTypeParameters(a);
    if (\start(ss) := unwrapType(t)) return getADTTypeParameters(ss);
    if (areified(_) := unwrapType(t)) return [];
    throw rascalCheckerInternalError("getADTTypeParameters given non-ADT type <prettyAType(t)>");
}

@doc{Return whether the ADT has type parameters.}
bool adtHasTypeParameters(AType t) = size(getADTTypeParameters(t)) > 0;


@doc{
.Synopsis
Determine if the given type is a constructor.
}
bool isConstructorType(aparameter(_,AType tvb)) = isConstructorType(tvb);
bool isConstructorType(acons(AType _, /*str _,*/ _, _)) = true;
default bool isConstructorType(AType _) = false;

@doc{Get the ADT type of the constructor.}
AType getConstructorResultType(AType ct) {
    if (acons(a,/*_,*/_,_) := unwrapType(ct)) return a;
    throw rascalCheckerInternalError("Cannot get constructor ADT type from non-constructor type <prettyAType(ct)>");
}

@doc{Get a list of the argument types in a constructor.}
list[AType] getConstructorArgumentTypes(AType ct) {
    if (acons(_,/*_,*/list[AType/*NamedField*/] cts,_) := unwrapType(ct)) return cts/*<1>*/;
    throw rascalCheckerInternalError("Cannot get constructor arguments from non-constructor type <prettyAType(ct)>");
}

@doc{Get a tuple with the argument types as the fields.}
AType getConstructorArgumentTypesAsTuple(AType ct) {
    return atuple(atypeList(getConstructorArgumentTypes(ct)));
}

@doc{
.Synopsis
Determine if the given type is a function.
}
bool isFunctionType(aparameter(_,AType tvb)) = isFunctionType(tvb);
bool isFunctionType(afunc(_,_,_)) = true;
default bool isFunctionType(AType _) = false;

@doc{Get a list of arguments for the function.}
list[AType] getFunctionArgumentTypes(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return ats;
    throw rascalCheckerInternalError("Cannot get function arguments from non-function type <prettyAType(ft)>");
}

@doc{Get the arguments for a function in the form of a tuple.}
AType getFunctionArgumentTypesAsTuple(AType ft) {
    if (afunc(_, ats, _) := unwrapType(ft)) return atuple(atypeList(ats));
    throw rascalCheckerInternalError("Cannot get function arguments from non-function type <prettyAType(ft)>");
}

@doc{Get the return type for a function.}
AType getFunctionReturnType(AType ft) {
    if (afunc(rt, _, _) := unwrapType(ft)) return rt;
    throw rascalCheckerInternalError("Cannot get function return type from non-function type <prettyAType(ft)>");
}

int getArity(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = size(formals);
int getArity(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = size(fields);
default int getArity(AType t) {
    throw rascalCheckerInternalError("Can only get arity from function or constructor type <prettyAType(t)>");
}

list[AType] getFormals(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = formals;
list[AType] getFormals(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = fields;
default list[AType] getFormals(AType t){
    throw rascalCheckerInternalError("Can only get formals from function or constructor type <prettyAType(t)>");
}

AType getResult(afunc(AType ret, list[AType] formals, list[Keyword] kwFormals)) = ret;
AType getResult(acons(AType adt, list[AType] fields, list[Keyword] kwFields)) = adt;
AType getResult(AType t){
    throw rascalCheckerInternalError("Can only get result type from function or constructor type <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is a reified type.
}
bool isReifiedType(aparameter(_,AType tvb)) = isReifiedType(tvb);
bool isReifiedType(areified(_)) = true;
default bool isReifiedType(AType _) = false;

@doc{Create a type representing the reified form of the given type.}
AType makeReifiedType(AType mainType) = areified(mainType);

@doc{Get the type that has been reified and stored in the reified type.}
AType getReifiedType(AType t) {
    if (areified(rt) := unwrapType(t)) return rt;
    throw rascalCheckerInternalError("getReifiedType given unexpected type: <prettyAType(t)>");
}

@doc{
.Synopsis
Determine if the given type is an type variable (parameter).
}
bool isRascalTypeParam(aparameter(_,_)) = true;
default bool isRascalTypeParam(AType _) = false;

@doc{Create a type representing a type parameter (type variable).}
AType makeTypeVar(str varName) = aparameter(varName, avalue());

@doc{Create a type representing a type parameter (type variable) and bound.}
AType makeTypeVarWithBound(str varName, AType varBound) = aparameter(varName, varBound);

@doc{Get the name of a Rascal type parameter.}
str getRascalTypeParamName(AType t) {
    if (aparameter(tvn,_) := t) return tvn;
    throw rascalCheckerInternalError("getRascalTypeParamName given unexpected type: <prettyAType(t)>");
}

@doc{Get the bound of a type parameter.}
AType getRascalTypeParamBound(AType t) {
    if (aparameter(_,tvb) := t) return tvb;
    throw rascalCheckerInternalError("getRascalTypeParamBound given unexpected type: <prettyAType(t)>");
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectRascalTypeParams(AType t) {
    
    return { rt | / AType rt : aparameter(_,_) := t };
    //return { unset(rt, "label") | / AType rt : aparameter(_,_) := t }; // TODO: "label" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given type.}
set[AType] collectAndUnlabelRascalTypeParams(AType t) {
   return { unset(rt, "label") | / AType rt : aparameter(_,_) := t }; // TODO: "label" is unset to enable subset check later, reconsider
}

@doc{Get all the type parameters inside a given set of productions.}
set[AType] collectAndUnlabelRascalTypeParams(set[AProduction] prods) {
   return { unset(rt, "label") | / AType rt : aparameter(_,_) := prods }; // TODO: "label" is unset to enable subset check later, reconsider
}

@doc{Provide an initial type map from the type parameters in the type to void.}
map[str,AType] initializeRascalTypeParamMap(AType t) {
    set[AType] rt = collectRascalTypeParams(t);
    return ( getRascalTypeParamName(tv) : makeVoidType() | tv <- rt );
}

@doc{See if a type contains any type parameters.}
bool typeContainsRascalTypeParams(AType t) = size(collectRascalTypeParams(t)) > 0;

@doc{Return the names of all type variables in the given type.}
set[str] typeParamNames(AType t) {
    return { tvn | aparameter(tvn,_) <- collectRascalTypeParams(t) };
}

// ---- element & container types

@doc{Is this type a non-container type?}
bool isElementType(AType t) = 
    isIntType(t) || isBoolType(t) || isRealType(t) || isRatType(t) || isStrType(t) || 
    isNumType(t) || isNodeType(t) || isVoidType(t) || isValueType(t) || isLocType(t) || 
    isDateTimeType(t) || isTupleType(t) || isADTType(t) || isConstructorType(t) ||
    isFunctionType(t) || isReifiedType(t) || isNonTerminalType(t);

@doc{Is this type a container type?}
bool isContainerType(AType t) =
    isSetType(t) || isListType(t) || isMapType(t) || isBagType(t);
    
bool isEnumeratorType(AType t) =
    isSetType(t) || isListType(t) || isMapType(t) || isADTType(t) || isTupleType(t) || isNodeType(t) ||
    isNonTerminalIterType(t) || isNonTerminalOptType(t);

// ---- literal
@doc{Synopsis: Determine if the given type is a literal.}
bool isLiteralType(aparameter(_,AType tvb)) = isLiteralType(tvb);
bool isLiteralType(lit(_)) = true;
bool isLiteralType(cilit(_)) = true;
default bool isLiteralType(AType _) = false;

// ---- layout
@doc{Synopsis: Determine if the given type is a layout type.}
bool isLayoutType(aparameter(_,AType tvb)) = isLayoutType(tvb);

bool isLayoutType(AType::\conditional(AType ss,_)) = isLayoutType(ss);
bool isLayoutType(t:aadt(adtName,_,SyntaxRole sr)) = sr == layoutSyntax();
bool isLayoutType(AType::\start(AType ss)) = isLayoutType(ss);
bool isLayoutType(AType::\iter(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\iter-seps(AType s,_)) = isLayoutType(s);
bool isLayoutType(AType::\iter-star-seps(AType s,_)) = isLayoutType(s);

bool isLayoutType(AType::\opt(AType s)) = isLayoutType(s);
bool isLayoutType(AType::\alt(set[AType] alts)) = any(a <- alts, isLayoutType(a));
bool isLayoutType(AType::\seq(list[AType] symbols)) = all(s <- symbols, isLayoutType(s));
default bool isLayoutType(AType _) = false;

// ---- isConcretePattern

bool isConcretePattern(Pattern p, AType tp) {
    return isNonTerminalType(tp) && !(p is callOrTree) /*&& Symbol::sort(_) := tp*/;
} 
// ---- nonterminal

@doc{Synopsis: Determine if the given type is a nonterminal.}
bool isNonTerminalType(aparameter(_,AType tvb)) = isNonTerminalType(tvb);
bool isNonTerminalType(AType::\conditional(AType ss,_)) = isNonTerminalType(ss);
bool isNonTerminalType(t:aadt(adtName,_,SyntaxRole sr)) = isConcreteSyntaxRole(sr) || adtName == "Tree";
bool isNonTerminalType(AType::\start(AType ss)) = isNonTerminalType(ss);
bool isNonTerminalType(AType::\iter(_)) = true;
bool isNonTerminalType(AType::\iter-star(_)) = true;
bool isNonTerminalType(AType::\iter-seps(_,_)) = true;
bool isNonTerminalType(AType::\iter-star-seps(_,_)) = true;
bool isNonTerminalType(AType::\empty()) = true;
bool isNonTerminalType(AType::\opt(_)) = true;
bool isNonTerminalType(AType::\alt(_)) = true;
bool isNonTerminalType(AType::\seq(_)) = true;
default bool isNonTerminalType(AType _) = false;   

bool isParameterizedNonTerminalType(AType t) = isNonTerminalType(t) && t has parameters;

bool isNonParameterizedNonTerminalType(AType t) = isNonTerminalType(t) && (t has parameters ==> isEmpty(t.parameters));

bool isNonTerminalIterType(aparameter(_,AType tvb)) = isNonTerminalIterType(tvb);
bool isNonTerminalIterType(AType::\iter(_)) = true;
bool isNonTerminalIterType(AType::\iter-star(_)) = true;
bool isNonTerminalIterType(AType::\iter-seps(_,_)) = true;
bool isNonTerminalIterType(AType::\iter-star-seps(_,_)) = true;
default bool isNonTerminalIterType(AType _) = false;    

AType getNonTerminalIterElement(aparameter(_,AType tvb)) = getNonTerminalIterElement(tvb);
AType getNonTerminalIterElement(AType::\iter(AType i)) = i;
AType getNonTerminalIterElement(AType::\iter-star(AType i)) = i;
AType getNonTerminalIterElement(AType::\iter-seps(AType i,_)) = i;
AType getNonTerminalIterElement(AType::\iter-star-seps(AType i,_)) = i;
default AType getNonTerminalIterElement(AType i) {
    throw rascalCheckerInternalError("<prettyAType(i)> is not an iterable non-terminal type");
}   

bool isNonTerminalOptType(aparameter(_,AType tvb)) = isNonTerminalOptType(tvb);
bool isNonTerminalOptType(AType::\opt(AType ot)) = true;
default bool isNonTerminalOptType(AType _) = false;

AType getNonTerminalOptType(aparameter(_,AType tvb)) = getNonTerminalOptType(tvb);
AType getNonTerminalOptType(AType::\opt(AType ot)) = ot;
default AType getNonTerminalOptType(AType ot) {
    throw rascalCheckerInternalError("<prettyAType(ot)> is not an optional non-terminal type");
}

bool isStartNonTerminalType(aparameter(_,AType tvb)) = isStartNonTerminalType(tvb);
bool isStartNonTerminalType(AType::\start(_)) = true;
default bool isStartNonTerminalType(AType s) = false;    

AType getStartNonTerminalType(aparameter(_,AType tvb)) = getStartNonTerminalType(tvb);
AType getStartNonTerminalType(AType::\start(AType s)) = s;
default AType getStartNonTerminalType(AType s) {
    throw rascalCheckerInternalError("<prettyAType(s)> is not a start non-terminal type");
}

AType removeConditional(cnd:conditional(AType s, set[ACondition] _)) = cnd.label? ? s[label=cnd.label] : s;
default AType removeConditional(AType s) = s;