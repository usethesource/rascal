module lang::rascalcore::check::AType

import Node;
import Set;
import Relation;
import String;
extend analysis::typepal::AType;
import lang::rascalcore::check::ATypeUtils;
import lang::rascal::\syntax::Rascal;
import lang::rascalcore::check::ATypeExceptions;

//alias Key         = loc;
alias Keyword     = tuple[str fieldName, AType fieldType, Expression defaultExp];
alias NamedField  = tuple[str fieldName, AType fieldType] ;

data QName        = qualName(str qualifier, str name);
   
data AType (str label = "")
    =  aint()
     | abool()
     | areal()
     | arat()
     | astr()
     | anum()
     | anode(list[NamedField] fields)
     | avoid()
     | avalue()
     | aloc()
     | adatetime()
     | alist(AType elmType)
     | aset(AType elmType)
     | abag(AType elmType)
     | atuple(AType elemType)
     | amap(AType keyType, AType valType)
     | arel(AType elemType)  
     | alrel(AType elemType)
     | afunc(AType ret, AType formals, list[Keyword] kwFormals, bool varArgs=false, str deprecationMessage="")
     | auser(str uname, list[AType] parameters) 
     | aalias(str aname, list[AType] parameters, AType aliased)
     | aanno(str aname, AType onType, AType annoType)
     
     | aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)
     | acons(AType adt, str consName, list[NamedField] fields, list[Keyword] kwFields)
     
     | amodule(str mname, str deprecationMessage="")
     | aparameter(str pname, AType bound) 
     | areified(AType atype)
     ;
     
@memo
AType overloadedAType(rel[Key, IdRole, AType] overloads){
    topIf:
    if(all(<Key k, IdRole idr, AType t> <- overloads, aadt(adtName, params, syntaxRole) := t)){
      str adtName = "";
      list[AType] adtParams = [];
      syntaxRoles = {};
      for(<Key k, IdRole idr, AType t> <- overloads, aadt(adtName1, params1, syntaxRole1) := t){
        if(!isEmpty(adtName) && adtName != adtName1) fail overloadedAType; // overloading of different ADTs.
        adtName = adtName1;
        adtParams = params1;    // TODO take care of different parameter names
        syntaxRoles += syntaxRole1;
      }
      syntaxRole = overloadSyntaxRole(syntaxRoles);
      if(syntaxRole == illegalSyntax()) fail overloadedAType;
      
      return aadt(adtName, adtParams, syntaxRole);
    } else {
        otypes = overloads<2>;
        if({AType tp} := otypes) return tp;
    }
    fail;
}

data AProduction
     = \choice(AType def, set[AProduction] alternatives)
     ;
 
@doc{
.Synopsis
Attributes register additional semantics annotations of a definition. 
}
data Attr 
     = \tag(value \tag) 
     ;

@doc{
.Synopsis
Normalize the choice between alternative productions.

.Description
Nested choice is flattened.
}
public AProduction choice(AType s, set[AProduction] choices){
    if(!any(choice(AType t, set[AProduction] b)  <- choices)){
       fail;
    } else {   
        // TODO: this does not work in interpreter and typechecker crashes on it (both related to the splicing)
        //return choice(s, { *(choice(Symbol t, set[AProduction] b) := ch ? b : {ch}) | ch <- choices });
        changed = false;
        new_choices = {};
        for(ch <- choices){
            if(choice(AType t, set[AProduction] b) := ch){
                changed = true;
                new_choices += b;
            } else {
                new_choices += ch;
            }
        }
        if(changed){
            return choice(s, new_choices);
        } else {
            fail;
        }
   }
}

// ---- Parse Tree

data Tree 
     = appl(AProduction aprod, list[Tree] args, loc src=|unknown:///|) // <1>
     | cycle(AType asymbol, int cycleLength)  // <2>
     | amb(set[Tree] alternatives) // <3> 
     | char(int character) // <4>
     ;

@doc{
.Synopsis
Annotate a parse tree node with a source location.
}
anno loc Tree@\loc; // TODO: weg

data SyntaxRole
    = dataSyntax()
    | contextFreeSyntax()
    | lexicalSyntax()
    | keywordSyntax()
    | layoutSyntax()
   // | startSyntax()
    | illegalSyntax()
    ;
    
SyntaxRole overloadSyntaxRole(set[SyntaxRole] syntaxRoles) {
    if(size(syntaxRoles) == 1) return getFirstFrom(syntaxRoles);
    //if(syntaxRoles <= {dataSyntax(), contextFreeSyntax()}){
    //   return startSyntax() in syntaxRoles ? startSyntax() : contextFreeSyntax();
    //}
    return illegalSyntax();
}

bool isConcreteSyntaxRole(SyntaxRole sr) = sr in {lexicalSyntax(), contextFreeSyntax()};

bool isLayoutSyntax(aadt(name, parameters, layoutSyntax())) = true;
default bool isLayoutSyntax(AType t) = false;

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
data AProduction 
     = prod(AType def, list[AType] asymbols, set[Attr] attributes={}, loc src=|unknown:///|) // <1>
     | regular(AType def) // <2>
     | error(AProduction prod, int dot) // <3>
     | skipped() // <4>
     ;
     
data AProduction 
     = \priority(AType def, list[AProduction] choices) // <5>
     | \associativity(AType def, Associativity \assoc, set[AProduction] alternatives) // <6>
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
data ACharRange = range(int begin, int end);

alias ACharClass = list[ACharRange];

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



//data AType // <1>
//     = \start(AType symbol);

// These symbols are the named non-terminals.
//data AType 
//     = 
//     \sort(str sname) // <2> 
//     | \lex(str sname)  // <3>
//     | \layouts(str sname)  // <4>
//     | \keywords(str sname) // <5>
//       \parameterized-sort(str sname, list[AType] parameters) // <6>
//     | \parameterized-lex(str sname, list[AType] parameters)  // <7>
//     ; 

// For convenience in transition period
//AType \start(AType symbol)  = symbol[syntaxRole=startSyntax()];
AType \sort(str sname)      = aadt(sname, [], contextFreeSyntax());
AType \lex(str sname)       = aadt(sname, [], lexicalSyntax());
AType \layouts(str sname)   = aadt(sname, [], layoutSyntax());
AType \keywords(str sname)  = aadt(sname, [], keywordSyntax());
AType \parameterized-sort(str sname, list[AType] parameters) 
                            = aadt(sname, parameters, contextFreeSyntax());
AType \parameterized-lex(str sname, list[AType] parameters) 
                            = aadt(sname, parameters, lexicalSyntax());


// These are the terminal symbols.
data AType 
     = \lit(str string)   // <8>
     | \cilit(str string) // <9>
     | \char-class(list[ACharRange] ranges) // <10>
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
     | \start(AType symbol)
     ;
  
data AType // <19>
     = \conditional(AType symbol, set[ACondition] conditions);

@doc{
.Synopsis
Datatype for declaring preconditions and postconditions on symbols

.Description

A `Condition` can be attached to a symbol; it restricts the applicability
of that symbol while parsing input text. For instance, `follow` requires that it
is followed by another symbol and `at-column` requires that it occurs 
at a certain position in the current line of the input text.
}
data ACondition
     = \follow(AType atype)
     | \not-follow(AType atype)
     | \precede(AType atype)
     | \not-precede(AType atype)
     | \delete(AType atype)
     | \at-column(int column) 
     | \begin-of-line()  
     | \end-of-line()  
     | \except(str label)
     ;

// ---- end ParseTree

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

// ---- asubtype --------------------------------------------------------------

@doc{
.Synopsis
Subtype on types.
}       
bool asubtype(type[&T] t, type[&U] u) { throw "asubtype not yet implemented on <t> and <u>"; } //asubtype(t.symbol, u.symbol);

@doc{
.Synopsis
This function documents and implements the subtype relation of Rascal's type system. 
}

bool asubtype(AType s, s) = true;

default bool asubtype(AType s, AType t) = (s.label? || t.label?) ? asubtype(unset(s, "label") , unset(t, "label")) : s == t;

bool asubtype(tvar(s), AType r) { /*println("asubtype(tvar(<s>), <r>)");*/ throw TypeUnavailable(); }
bool asubtype(AType l, tvar(s)) { /*println("asubtype(<l> tvar(<s>))");*/ throw TypeUnavailable(); }


bool asubtype(overloadedAType(overloads), AType r) = any(<k, idr, tp> <- overloads, asubtype(tp, r));
//{   for(<k, idr, tp> <- overloads){
//        println("asubtype(tp,r):");
//        iprintln(tp);
//        iprintln(r);
//        if(asubtype(tp, r)) return true;
//    }
//    return false;
//}
bool asubtype(AType l, overloadedAType(overloads)) = any(<k, idr, tp> <- overloads, asubtype(l, tp));

bool asubtype(AType _, avalue()) = true;

bool asubtype(avoid(), AType _) = true;

bool asubtype(x:acons(AType a, _, list[NamedField] _, list[Keyword] _), AType b){
    res = asubtype(a, b);
    //println("asubtype(acons(<a>,_,_,_), <b>) ==\> <res>");
    return res;
}
bool asubtype(AType a, x:acons(AType b, _, list[NamedField] _, list[Keyword] _)){
    res = asubtype(a, b);
    //println("asubtype(<a>, acons(<b>,_,_,_) ==\> <res>");
    return res;
}
bool asubtype(acons(AType a, str name, list[NamedField] ap, list[Keyword] _), acons(a,name,list[NamedField] bp, list[Keyword] _)) = asubtype(ap,bp);

bool asubtype(aadt(str _, list[AType] _, _), anode(_)) = true;
bool asubtype(aadt(str n, list[AType] l, _), aadt(n, list[AType] r, _)) = asubtype(l, r);
bool asubtype(aadt(_, _, sr), aadt("Tree", _, _)) = true when isConcreteSyntaxRole(sr);
bool asubtype(aadt(str _, list[AType] _, sr), AType::\auser("Tree", _))  = true when isConcreteSyntaxRole(sr);

bool asubtype(\start(AType a), AType b) = asubtype(a, b);
bool asubtype(AType a, \start(AType b)) = asubtype(a, b);


bool asubtype(AType::\iter-seps(AType s, list[AType] seps), AType::\iter-star-seps(AType t, list[AType] seps2)) = asubtype(s,t) && asubtype(seps, seps2);
bool asubtype(AType::\iter(AType s), AType::\iter-star(AType t)) = asubtype(s, t);

bool asubtype(AType::\iter(AType s), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter(AType s), anode(_)) = true;

bool asubtype(AType::\iter-star(AType s), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-star(AType s), anode(_)) = true;

bool asubtype(AType::\iter-seps(AType s, list[AType] seps), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-seps(AType s, list[AType] seps), anode(_)) = true;

bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), aadt("Tree", [], _)) = true;
bool asubtype(AType::\iter-star-seps(AType s, list[AType] seps), anode(_)) = true;

// TODO: add subtype for elements under optional and alternative, but that would also require auto-wrapping/unwrapping in the run-time
// bool subtype(AType s, \opt(AType t)) = subtype(s,t);
// bool subtype(AType s, \alt({AType t, *_}) = true when subtype(s, t); // backtracks over the alternatives

bool asubtype(aadt(str n, list[AType] l, _), auser(n, list[AType] r)) = asubtype(l,r); //{throw "Illegal use of auser <n>"; } //= asubtype(l,r);

bool asubtype(auser(str n, list[AType] l), aadt(n, list[AType] r, _)) = asubtype(l,r); //{throw "Illegal use of auser <n>"; } //= asubtype(l,r);
bool asubtype(auser(str n, list[AType] l), auser(n, list[AType] r)) = asubtype(l,r); //{throw "Illegal use of auser <n>"; } //= asubtype(l,r);

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
bool asubtype(areified(AType s), anode(_)) = true;

bool asubtype(anode(list[NamedField] l), anode(list[NamedField] r)) = l <= r;

// Utilities

bool asubtype(atypeList(list[AType] l), atypeList(list[AType] r)) = asubtype(l, r);

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool asubtype(list[NamedField] l, list[NamedField] r) = all(i <- index(l), asubtype(l[i].fieldType, r[i].fieldType)) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[NamedField] l, list[NamedField] r) = size(l) == 0 && size(r) == 0;

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
bool comparable(AType s, AType t)
    = s == t || asubtype(s,t) || asubtype(t,s);

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

// ---- lub: least-upper-bound ------------------------------------------------

int size(atypeList(list[AType] l)) = size(l);

@doc{
.Synopsis
The least-upperbound (lub) between two types.

.Description
This function documents and implements the lub operation in Rascal's type system. 
}
AType alub(tvar(s), AType r) { /*println("alub(tvar(<s>), <r>)");*/ throw TypeUnavailable(); } 
AType alub(AType l, tvar(s)) { /*println("alub(<l>, tvar(<s>))");*/ throw TypeUnavailable(); }

AType alub(AType s, s) = s;
default AType alub(AType s, AType t) = (s.label? || t.label?) ? alub(unset(s, "label") , unset(t, "label")) : avalue();

AType alub(avalue(), AType t) = avalue();
AType alub(AType s, avalue()) = avalue();
AType alub(avoid(), AType t) = t;
AType alub(AType s, avoid()) = s;
AType alub(aint(), anum()) = anum();
AType alub(aint(), areal()) = anum();   // why not areal();
AType alub(aint(), arat()) = anum();      // why not arat();
AType alub(arat(), anum()) = anum();
AType alub(arat(), areal()) = anum();
AType alub(arat(), aint()) = anum();    // why not arat();
AType alub(areal(), anum()) = anum();
AType alub(areal(), aint()) = anum();   // why not areal();
AType alub(areal(), arat()) = anum();
AType alub(anum(), aint()) = anum();
AType alub(anum(), areal()) = anum();
AType alub(anum(), arat()) = anum();

AType alub(aset(AType s), aset(AType t)) = aset(alub(s, t)); 
 
AType alub(aset(AType s), arel(atypeList(list[AType] ts))) = aset(alub(s,atuple(atypeList(ts))));  
AType alub(arel(atypeList(list[AType] ts)), aset(AType s)) = aset(alub(s,atuple(atypeList(ts))));

AType alub(r1: arel(atypeList(list[AType] l)), r2:arel(atypeList(list[AType] r)))  = size(l) == size(r) ? arel(atypeList(alubList(l, r))) : aset(avalue());

AType alub(alist(AType s), alist(AType t)) = alist(alub(s, t));  
AType alub(alist(AType s), alrel(atypeList(list[AType] ts))) = alist(alub(s,atuple(atypeList(ts))));  
AType alub(alrel(atypeList(list[AType] ts)), alist(AType s)) = alist(alub(s,atuple(atypeList(ts))));

AType alub(lr1: alrel(atypeList(list[AType] l)), lr2:alrel(atypeList(list[AType] r)))  = size(l) == size(r) ? alrel(atypeList(alubList(l, r))) : alist(avalue());

AType alub(t1: atuple(atypeList(list[AType] l)), t2:atuple(atypeList(list[AType] r))) = size(l) == size(r) ? atuple(atypeList(alubList(l, r))) : atuple(avalue());

AType alub(m1: amap(ld, lr), m2: amap(rd, rr)) = amap(alub(ld, rd), alub(lr, rr));

AType alub(abag(AType s), abag(AType t)) = abag(alub(s, t));

AType alub(aadt(str n, list[AType] _, SyntaxRole_), anode(_))  = anode([]);
AType alub(anode(_), aadt(str n, list[AType] _, SyntaxRole _)) = anode([]);

AType alub(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr)) 
                                                = addADTLabel(a1, a2, aadt(n, addParamLabels(alubList(lp,rp),getParamLabels(lp)), sr))
                                                  when size(lp) == size(rp) && getParamLabels(lp) == getParamLabels(rp) && size(getParamLabels(lp)) > 0 &&
                                                       sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr)) 
                                                = addADTLabel(a1, a2, aadt(n, alubList(lp,rp), sr))
                                                  when size(lp) == size(rp) && size(getParamLabels(lp)) == 0 && sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(aadt(str n, list[AType] lp, SyntaxRole _), aadt(str m, list[AType] rp,SyntaxRole _)) = anode([]) when n != m;
AType alub(a1: aadt(str ln, list[AType] lp,SyntaxRole  _), acons(AType b, _, _, _)) = alub(a1,b);

AType addADTLabel(AType a1, AType a2, AType adt){
  if(a1.label? && a1.label == a2.label) adt = adt[label=a1.label];
  return adt;
}

AType alub(acons(AType la, _, list[NamedField] _,  list[Keyword] _), acons(AType ra, _, list[NamedField] _, list[Keyword] _)) = alub(la,ra);
AType alub(acons(AType a,  _, list[NamedField] lp, list[Keyword] _), a2:aadt(str n, list[AType] rp, SyntaxRole _)) = alub(a,a2);
AType alub(acons(AType _,  _, list[NamedField] _,  list[Keyword] _), anode(_)) = anode([]);

AType alub(anode(list[NamedField] l), anode(list[NamedField] r)) = anode(l & r);

bool keepParams(aparameter(str s1, AType bound1), aparameter(str s2, AType bound2)) = s1 == s2 && equivalent(bound1,bound2);

AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = l when keepParams(l,r);
AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = alub(bound1,bound2) when !keepParams(l,r);
AType alub(aparameter(str _, AType bound), AType r) = alub(bound, r) when aparameter(_,_) !:= r; //!(isRascalTypeParam(r));
AType alub(AType l, aparameter(str _, AType bound)) = alub(l, bound) when aparameter(_,_) !:= l; //!(isRascalTypeParam(l));

AType alub(areified(AType l), areified(AType r)) = areified(alub(l,r));
AType alub(areified(AType l), anode(_)) = anode([]);

AType alub(afunc(AType lr, AType lp, list[Keyword] lkw), afunc(AType rr, AType rp, list[Keyword] rkw)) {
    lubReturn = alub(lr,rr);
    lubParams = alub(lp,rp);    // TODO was glb, check this
    if (atypeList(_) := lubParams)
        return afunc(lubReturn, lubParams, lkw == rkw ? lkw : []);
    else
        return avalue();
}

public list[AType] alubList(list[AType] l, list[AType] r) = [alub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
default list[AType] alubList(list[AType] l, list[AType] r) = [avalue()]; 

private list[str] getParamLabels(list[AType] l) = [ s | li <- l, aparameter(s,_) := li ];
private list[AType] addParamLabels(list[AType] l, list[str] s) = [ aparameter(s[idx],l[idx]) | idx <- index(l) ] when size(l) == size(s);
private default list[AType] addParamLabels(list[AType] l, list[str] s) { throw "Length of AType list and label list much match"; } 

@doc{Calculate the lub of a list of types.}
public AType lubList(list[AType] ts) {
    AType theLub = avoid();
    for (t <- ts) theLub = alub(theLub,t);
    return theLub;
}