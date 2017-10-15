module lang::rascal::check::AType

//import Type;
import Node;
extend analysis::typepal::AType;
import lang::rascal::check::ATypeUtils;
import lang::rascal::\syntax::Rascal;

alias Key = loc;
alias Keyword = tuple[AType fieldType, str fieldName, Expression defaultExp];
alias NamedField   = tuple[AType fieldType, str fieldName];

data QName = qualName(str qualifier, str name);

data AType (str label = "")
    =  aint()
     | abool()
     | areal()
     | arat()
     | astr()
     | anum()
     | anode()
     | avoid()
     | avalue()
     | aloc()
     | adatetime()
     | alist(AType elmType)
     | aset(AType elmType)
     | atuple(AType elemType)
     | amap(AType keyType, AType valType)
     | arel(AType elemType)
     | alrel(AType elemType)
     | afunc(AType ret, AType formals, list[Keyword] kwFormals, bool varArgs=false)
     | auser(QName uname, list[AType] parameters) 
     | aalias(str aname, list[AType] parameters, AType aliased)
     | aadt(str adtName, list[AType] parameters, list[Keyword] common)
     | acons(AType adt, str consName, list[NamedField] fields, list[Keyword] kwFields)
     | amodule(str mname)
     | aparameter(str pname, AType bound) 
     | areified(AType atype)
     ;

AType overloadedAType(rel[Key, AType] overloads){
    if(all(<Key k, AType t> <- overloads, aadt(adtName, params, common) := t)){
      cm = [];
      str adtName;
      list[AType] adtParams;
      for(<Key k, AType t> <- overloads, aadt(adtName1, params1, common1) := t){
        adtName = adtName1;
        adtParams = params1;
        cm += common1;
      }
      return aadt(adtName, adtParams, cm);
    }
    fail;
}

@doc{Extension to add a production type.}
public data AType = \prod(AType \sort, str sname, list[AType] parameters, set[Attr] attributes);


data Production
     = \cons(AType def, list[AType] symbols, list[AType] kwTypes, set[Attr] attributes)
     | \func(AType def, list[AType] symbols, list[AType] kwTypes, set[Attr] attributes)
     | \choice(AType def, set[Production] alternatives)
     | \composition(Production lhs, Production rhs)
     ;

@doc{
.Synopsis
Attributes register additional semantics annotations of a definition. 
}
data Attr 
     = \tag(value \tag) 
     ;

// ---- Parse Tree


data Tree 
     = appl(Production prod, list[Tree] args) // <1>
     | cycle(AType symbol, int cycleLength)  // <2>
     | amb(set[Tree] alternatives) // <3> 
     | char(int character) // <4>
     ;

@doc{
.Synopsis
Production in ParseTrees 

.Description

The type `Production` is introduced in <<Prelude-Type>>, see <<Type-Production>>. Here we extend it with the symbols
that can occur in a ParseTree. We also extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

<1> A `prod` is a rule of a grammar, with a defined non-terminal, a list
    of terminal and/or non-terminal symbols and a possibly empty set of attributes.
  
<2> A `regular` is a regular expression, i.e. a repeated construct.

<3> A `error` represents a parse error.

<4> A `skipped` represents skipped input during error recovery.

<5> `priority` means ordered choice, where alternatives are tried from left to right;
<6> `assoc`  means all alternatives are acceptable, but nested on the declared side;
<7> `others` means '...', which is substituted for a choice among the other definitions;
<8> `reference` means a reference to another production rule which should be substituted there,
    for extending priority chains and such.
} 
data Production 
     = prod(AType def, list[AType] symbols, set[Attr] attributes) // <1>
     | regular(AType def) // <2>
     | error(Production prod, int dot) // <3>
     | skipped() // <4>
     ;
     
data Production 
     = \priority(AType def, list[Production] choices) // <5>
     | \associativity(AType def, Associativity \assoc, set[Production] alternatives) // <6>
     | \others(AType def) // <7>
     | \reference(AType def, str cons) // <8>
     ;

@doc{
.Synopsis
Attributes in productions.

.Description

An `Attr` (attribute) documents additional semantics of a production rule. Neither tags nor
brackets are processed by the parser generator. Rather downstream processors are
activated by these. Associativity is a parser generator feature though. 
}
data Attr 
     = \assoc(Associativity \assoc)
     | \bracket()
     ;

@doc{
.Synopsis
Associativity attribute. 
 
.Description

Associativity defines the various kinds of associativity of a specific production.
}  
data Associativity 
     = \left()
     | \right() 
     | \assoc() 
     | \non-assoc()
     ;

@doc{
.Synopsis
Character ranges and character class
.Description

*  `CharRange` defines a range of characters.
*  A `CharClass` consists of a list of characters ranges.
}
data CharRange = range(int begin, int end);

alias CharClass = list[CharRange];

@doc{
.Synopsis
Symbols that can occur in a ParseTree

.Description

The type `Symbol` is introduced in <<Prelude-Type>>, see <<Type-Symbol>>, to represent the basic Rascal types,
e.g., `int`, `list`, and `rel`. Here we extend it with the symbols that may occur in a ParseTree.

<1>  The `start` symbol wraps any symbol to indicate that it is a start symbol of the grammar and
        may occur at the root of a parse tree.
<2>  Context-free non-terminal
<3>  Lexical non-terminal
<4>  Layout symbols
<5>  Terminal symbols that are keywords
<6>  Parameterized context-free non-terminal
<7> Parameterized lexical non-terminal
<8>  Terminal.
<9>  Case-insensitive terminal.
<10> Character class
<11> Empty symbol
<12> Optional symbol
<13> List of one or more symbols without separators
<14> List of zero or more symbols without separators
<15> List of one or more symbols with separators
<16> List of zero or more symbols with separators
<17> Alternative of symbols
<18> Sequence of symbols
<19> Conditional occurrence of a symbol.

}
data AType // <1>
     = \start(AType symbol);

// These symbols are the named non-terminals.
data AType 
     = \nonterminal(str sname) // <2> 
     | \lex(str sname)  // <3>
     | \layouts(str sname)  // <4>
     | \keywords(str sname) // <5>
     | \parameterized-sort(str sname, list[AType] parameters) // <6>
     | \parameterized-lex(str sname, list[AType] parameters)  // <7>
     ; 

// These are the terminal symbols.
data AType 
     = \lit(str string)   // <8>
     | \cilit(str string) // <9>
     | \char-class(list[CharRange] ranges) // <10>
     ;
    
// These are the regular expressions.
data AType
     = \empty() // <11>
     | \opt(AType symbol)  // <12>
     | \iter(AType symbol) // <13>
     | \iter-star(AType symbol)  // <14>
     | \iter-seps(AType symbol, list[AType] separators)      // <15> 
     | \iter-star-seps(AType symbol, list[AType] separators) // <16>
     | \alt(set[AType] alternatives) // <17>
     | \seq(list[AType] symbols)     // <18>
     ;
  
data AType // <19>
     = \conditional(AType symbol, set[Condition] conditions);

public bool subtype(AType::\sort(_), AType::\adt("Tree", _)) = true;

@doc{
.Synopsis
Datatype for declaring preconditions and postconditions on symbols

.Description

A `Condition` can be attached to a symbol; it restricts the applicability
of that symbol while parsing input text. For instance, `follow` requires that it
is followed by another symbol and `at-column` requires that it occurs 
at a certain position in the current line of the input text.
}
data Condition
     = \follow(AType symbol)
     | \not-follow(AType symbol)
     | \precede(AType symbol)
     | \not-precede(AType symbol)
     | \delete(AType symbol)
     | \at-column(int column) 
     | \begin-of-line()  
     | \end-of-line()  
     | \except(str label)
     ;

// ---- end ParseTree

bool myIsSubType(AType t1, AType t2) = asubtype(t1, t2);
//{ res = asubtype(t1, t2); println("asubtype(<t1>, <t2>) ==\> <res>"); return res;}

AType myLUB(AType t1, AType t2) = alub(t1, t2);
//{ println("myLUB: <t1>, <t2>"); return alub(t1, t2); }

AType myATypeMin() = avoid();
AType myATypeMax() = avalue();

public set[AType] numericTypes = { aint(), areal(), arat(), anum() };

public bool comparableOrNum(AType l, AType r) {
    leftAsNum = visit(l) {
        case aint() => anum()
        case areal() => anum()
        case arat() => anum()
    };
    
    rightAsNum = visit(r) {
        case aint() => anum()
        case areal() => anum()
        case arat() => anum()
    };
    
    return comparable(l, r) || comparable(leftAsNum,rightAsNum);
}

@doc{
.Synopsis
Subtype on types.
}       
bool subtype(type[&T] t, type[&U] u) = subtype(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system. 
}

bool asubtype(AType s, s) = true;

default bool asubtype(AType s, AType t) = unsetRec(s) == unsetRec(t);

bool asubtype(overloadedAType(overloads), AType r) = all(<k, tp> <- overloads, asubtype(tp, r));
bool asubtype(AType l, overloadedAType(overloads)) = all(<k, tp> <- overloads, asubtype(l, tp));

bool asubtype(AType _, avalue()) = true;

bool asubtype(avoid(), AType _) = true;

bool asubtype(acons(AType a, _, list[NamedField] _, list[Keyword] _), a) = true;
bool asubtype(acons(AType a, str name, list[NamedField] ap, list[Keyword] _), acons(a,name,list[NamedField] bp, list[Keyword] _)) = asubtype(ap,bp);

bool asubtype(aadt(str _, list[AType] _, list[Keyword] _), anode()) = true;
bool asubtype(aadt(str n, list[AType] l, list[Keyword] _), aadt(n, list[AType] r, list[Keyword] _)) = asubtype(l, r);

bool asubtype(aadt(str n, list[AType] l,  _), auser(qualName(_, n), list[AType] r)) = asubtype(l,r);
bool asubtype(auser(qualName(_, str n), list[AType] l), aadt(n, list[AType] r, _)) = asubtype(l,r);

bool asubtype(auser(qualName(_, str n), list[AType] l), auser(qualName(_, str n), list[AType] r)) = asubtype(l,r);

//bool asubtype(aalias(str _, list[AType] _, AType aliased), AType r) = asubtype(aliased, r);
//bool asubtype(AType l, aalias(str _, list[AType] _, AType aliased)) = asubtype(l, aliased);

bool asubtype(aint(), anum()) = true;
bool asubtype(arat(), anum()) = true;
bool asubtype(areal(), anum()) = true;

bool asubtype(atuple(AType l), atuple(AType r)) = asubtype(l, r);

// list and lrel
bool asubtype(alist(AType s), alist(AType t)) = asubtype(s, t);
bool asubtype(alrel(AType l), alrel(AType r)) = asubtype(l, r);

bool asubtype(alist(AType s), alrel(AType r)) = asubtype(s, atuple(r));
bool asubtype(alrel(AType l), alist(AType r)) = asubtype(atuple(l), r);

// set and rel
bool asubtype(aset(AType s), aset(AType t)) = asubtype(s, t);
bool asubtype(arel(AType l), arel(AType r)) = asubtype(l, r);

bool asubtype(aset(AType s), arel(AType r)) = asubtype(s, atuple(r));
bool asubtype(arel(AType l), aset(AType r)) = asubtype(atuple(l), r);

bool asubtype(abag(AType s), abag(AType t)) = asubtype(s, t);  

bool asubtype(amap(AType from1, AType to1), amap(AType from2, AType to2)) 
    { return  asubtype(from1, from2) && asubtype(to1, to2);}

bool asubtype(afunc(AType r1, AType p1, list[Keyword] _), afunc(AType r2, AType p2, list[Keyword] _)) = asubtype(r1, r2) && asubtype(p2, p1); // note the contra-variance of the argument types

bool asubtype(aparameter(str _, AType bound), AType r) = asubtype(bound, r);
bool asubtype(AType l, aparameter(str _, AType bound)) = asubtype(l, bound);

bool asubtype(areified(AType s), areified(AType t)) = asubtype(s,t);
bool asubtype(areified(AType s), anode()) = true;

bool asubtype(atypeList(list[AType] l), atypeList(list[AType] r)) = asubtype(l, r);

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool asubtype(list[NamedField] l, list[NamedField] r) = all(i <- index(l), asubtype(l[i].fieldType, r[i].fieldType)) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[NamedField] l, list[NamedField] r) = size(l) == 0 && size(r) == 0;

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
bool comparable(AType s, AType t) = asubtype(s,t) || asubtype(t,s);

@doc{
.Synopsis
Check if two types are equivalent.
}
bool equivalent(AType s, AType t) = asubtype(s,t) && asubtype(t,s);


@doc{
.Synopsis
Structural equality between values. 

.Description
The difference is that no implicit coercions are done between values of incomparable types, such as == does for
int, real and rat.

.Examples

[source,rascal-shell]
----
import Type;
1 == 1.0
eq(1,1.0)
----
}
@javaClass{org.rascalmpl.library.Type}
public java bool eq(value x, value y);

int size(atypeList(list[AType] l)) = size(l);

@doc{
.Synopsis
The least-upperbound (lub) between two types.

.Description
This function documents and implements the lub operation in Rascal's type system. 
}
AType alub(AType s, s) = s;
default AType alub(AType s, AType t) = (s.label? || t.label?) ? alub(unset(s, "label") , unset(t, "label")) : avalue();

AType alub(avalue(), AType t) = avalue();
AType alub(AType s, avalue()) = avalue();
AType alub(avoid(), AType t) = t;
AType alub(AType s, avoid()) = s;
AType alub(aint(), anum()) = anum();
AType alub(aint(), areal()) = anum();
AType alub(aint(), arat()) = anum();
AType alub(arat(), anum()) = anum();
AType alub(arat(), areal()) = anum();
AType alub(arat(), aint()) = anum();
AType alub(areal(), anum()) = anum();
AType alub(areal(), aint()) = anum();
AType alub(areal(), arat()) = anum();
AType alub(anum(), aint()) = anum();
AType alub(anum(), areal()) = anum();
AType alub(anum(), arat()) = anum();

AType alub(aset(AType s), aset(AType t)) = aset(alub(s, t)); 
 
AType alub(aset(AType s), arel(AType ts)) = aset(alub(s,atuple(ts)));  
AType alub(arel(AType ts), aset(AType s)) = aset(alub(s,atuple(ts)));

AType alub(r1: arel(AType l), r2:arel(AType r))  = size(l) == size(r) ? arel(alub(l, r)) : aset(avalue());

AType alub(alist(AType s), alist(AType t)) = alist(alub(s, t));  
AType alub(alist(AType s), alrel(AType ts)) = alist(alub(s,atuple(ts)));  
AType alub(alrel(AType ts), alist(AType s)) = alist(alub(s,atuple(ts)));

AType alub(lr1: alrel(AType l), lr2:alrel(AType r))  = size(l) == size(r) ? alrel(alub(l, r)) : alist(avalue());

Type alub(t1: atuple(AType l), t2:atuple(AType r)) = size(l) == size(r) ? atuple(alub(l, r)) : atuple(avalue());

AType alub(m1: amap(ld, lr), m2: amap(rd, rr)) = amap(alub(ld, rd), alub(lr, rr));

AType alub(abag(AType s), abag(AType t)) = abag(alub(s, t));

AType alub(aadt(str n, list[AType] _, list[Keyword] _), anode()) = anode();
AType alub(anode(), aadt(str n, list[AType] _, list[Keyword] _)) = anode();

AType alub(aadt(str n, list[AType] lp, list[Keyword] _), aadt(n, list[AType] rp,list[Keyword] _)) = aadt(n, addParamLabels(alub(lp,rp),getParamLabels(lp)), []) when size(lp) == size(rp) && getParamLabels(lp) == getParamLabels(rp) && size(getParamLabels(lp)) > 0;
AType alub(aadt(str n, list[AType] lp, list[Keyword] _), aadt(n, list[AType] rp, list[Keyword] _)) = aadt(n, alub(lp,rp), []) when size(lp) == size(rp) && size(getParamLabels(lp)) == 0;
AType alub(aadt(str n, list[AType] lp, list[Keyword] _), aadt(str m, list[AType] rp, list[Keyword] _)) = anode() when n != m;
AType alub(aadt(str ln, list[AType] lp, list[Keyword] _), acons(AType b, _, list[AType] _)) = alub(aadt(ln,lp,[]),b);

AType alub(acons(AType la, _, list[AType] _), acons(AType ra, _, list[AType] _)) = alub(la,ra);
AType alub(acons(AType a, _, list[AType] lp), aadt(str n, list[AType] rp)) = alub(a,aadt(n,rp, []));
AType alub(acons(AType _, _, list[AType] _), anode()) = anode();

bool keepParams(aparameter(str s1, AType bound1), aparameter(str s2, AType bound2)) = s1 == s2 && equivalent(bound1,bound2);

AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = l when keepParams(l,r);
AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = alub(bound1,bound2) when !keepParams(l,r);
AType alub(aparameter(str _, AType bound), AType r) = alub(bound, r) when !(isRascalTypeParam(r));
AType alub(AType l, aparameter(str _, AType bound)) = alub(l, bound) when !(isRascalTypeParam(l));

AType alub(areified(AType l), areified(AType r)) = areified(alub(l,r));
AType alub(areified(AType l), anode()) = anode();

AType alub(afunc(AType lr, list[AType] lp, list[AType] lkw), afunc(AType rr, list[AType] rp, list[AType] rkw)) {
    lubReturn = alub(lr,rr);
    lubParams = glb(atuple(lp),atuple(rp));
    if (isTupleType(lubParams))
        return afunc(lubReturn, lubParams.ATypes, lkw == rkw ? lkw : []);
    else
        return avalue();
}

public list[AType] alub(list[AType] l, list[AType] r) = [alub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
default list[AType] alub(list[AType] l, list[AType] r) = [avalue()]; 

private list[str] getParamLabels(list[AType] l) = [ s | li <- l, aparameter(s,_) := li ];
private list[AType] addParamLabels(list[AType] l, list[str] s) = [ aparameter(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[AType] addParamLabels(list[AType] l, list[str] s) { throw "Length of AType list and label list much match"; } 


@doc{
.Synopsis
Determine if the given type is an int.
}
bool isIntType(aparameter(_,AType tvb)) = isIntType(tvb);
bool isIntType(aint()) = true;
default bool isIntType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a bool.
}
bool isBoolType(aparameter(_,AType tvb)) = isBoolType(tvb);
bool isBoolType(abool()) = true;
default bool isBoolType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a real.
}
bool isRealType(aparameter(_,AType tvb)) = isRealType(tvb);
bool isRealType(areal()) = true;
default bool isRealType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a rational.
}
bool isRatType(aparameter(_,AType tvb)) = isRatType(tvb);
bool isRatType(arat()) = true;
default bool isRatType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a string.
}
bool isStrType(aparameter(_,AType tvb)) = isStrType(tvb);
bool isStrType(astr()) = true;
default bool isStrType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a num.
}
bool isNumType(aparameter(_,AType tvb)) = isNumType(tvb);
bool isNumType(anum()) = true;
default bool isNumType(AType _) = false;

bool isNumericType(AType t) = isIntType(t) || isRealType(t) || isRatType(t) || isNumType(t);

@doc{
.Synopsis
Determine if the given type is a node.
}
bool isNodeType(aparameter(_,AType tvb)) = isNodeType(tvb);
bool isNodeType(anode()) = true;
bool isNodeType(aadt(_,_)) = true;
default bool isNodeType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a void.
}
bool isVoidType(aparameter(_,AType tvb)) = isVoidType(tvb);
bool isVoidType(avoid()) = true;
default bool isVoidType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a value.
}
bool isValueType(aparameter(_,AType tvb)) = isValueType(tvb);
bool isValueType(avalue()) = true;
default bool isValueType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a loc.
}
bool isLocType(aparameter(_,AType tvb)) = isLocType(tvb);
bool isLocType(aloc()) = true;
default bool isLocType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a `datetime`.
}
bool isDateTimeType(aparameter(_,AType tvb)) = isDateTimeType(tvb);
bool isDateTimeType(adatetime()) = true;
default bool isDateTimeType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a set.
}
bool isSetType(aparameter(_,AType tvb)) = isSetType(tvb);
bool isSetType(aset(_)) = true;
bool isSetType(arel(_)) = true;
default bool isSetType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a relation.
}
bool isRelType(aparameter(_,AType tvb)) = isRelType(tvb);
bool isRelType(arel(_)) = true;
bool isRelType(aset(AType tp)) = true when isTupleType(tp);
default bool isRelType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a list relation.
}
bool isListRelType(aparameter(_,AType tvb)) = isListRelType(tvb);
bool isListRelType(alrel(_)) = true;
bool isListRelType(alist(AType tp)) = true when isTupleType(tp);
default bool isListRelType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a tuple.
}
bool isTupleType(aparameter(_,AType tvb)) = isTupleType(tvb);
bool isTupleType(atuple(_)) = true;
default bool isTupleType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a list.
}
bool isListType(aparameter(_,AType tvb)) = isListType(tvb);
bool isListType(alist(_)) = true;
bool isListType(alrel(_)) = true;
default bool isListType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a list relation.
}
bool isListRelType(aparameter(_,AType tvb)) = isListRelType(tvb);
bool isListRelType(alrel(_)) = true;
default bool isListRelType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a map.
}
bool isMapType(x:aparameter(_,AType tvb)) {
    return  isMapType(tvb);
    }
bool isMapType(x:amap(_,_)) {
    return true;
}
default bool isMapType(AType x) {
    return false;
}

@doc{
.Synopsis
Determine if the given type is a bag (bags are not yet implemented).
}
bool isBagType(aparameter(_,AType tvb)) = isBagType(tvb);
bool isBagType(abag(_)) = true;
default bool isBagType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is an Abstract Data Type (ADT).
}
bool isADTType(aparameter(_,AType tvb)) = isADTType(tvb);
bool isADTType(aadt(_,_,_)) = true;
bool isADTType(areified(_)) = true;
default bool isADTType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a constructor.
}
bool isConstructorType(aparameter(_,AType tvb)) = isConstructorType(tvb);
bool isConstructorType(acons(AType _,str _,list[AType] _)) = true;
default bool isConstructorType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a function.
}
bool isFunctionType(aparameter(_,AType tvb)) = isFunctionType(tvb);
bool isFunctionType(afunc(_,_,_)) = true;
bool isFunctionType(afunc(_,_)) = true;
//bool isFunctionType(\var-func(_,_,_)) = true;
default bool isFunctionType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is a reified type.
}
bool isReifiedType(aparameter(_,AType tvb)) = isReifiedType(tvb);
bool isReifiedType(areified(_)) = true;
default bool isReifiedType(AType _) = false;

@doc{
.Synopsis
Determine if the given type is an type variable (parameter).
}
bool isRascalTypeParam(aparameter(_,_)) = true;
default bool isRascalTypeParam(AType _) = false;

