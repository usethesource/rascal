@bootstrapParser
module lang::rascalcore::check::AType

extend analysis::typepal::AType;
import analysis::typepal::Exception;

import lang::rascal::\syntax::Rascal;
import lang::rascalcore::grammar::definition::Characters;

import List;
import Set;
import Node;
import String;
import IO;

import util::Memo;

alias Keyword     = tuple[AType fieldType, Expression defaultExp];
   
data AType (str label = "")
    =  
       avoid()
     | abool()
     | aint()
     | areal()
     | arat()
     | anum()
     | astr()
     | aloc()
     | adatetime()
     | alist(AType elmType)
     | abag(AType elmType)
     | aset(AType elmType)
     | arel(AType elemType)  
     | alrel(AType elemType)
   
     | atuple(AType elemType)
     | amap(AType keyType, AType valType)
  
     | afunc(AType ret, list[AType] formals, list[Keyword] kwFormals,  
             bool varArgs=false, 
             str deprecationMessage="", 
             bool isConcreteArg=false, 
             bool isDefault=false, 
             bool isTest=false, 
             bool returnsViaAllPath = false,
             int abstractFingerprint=0, 
             int concreteFingerprint=0)
     | aalias(str aname, list[AType] parameters, AType aliased)
     | aanno(str aname, AType onType, AType annoType)
     
     | anode(list[AType] fields)
     | aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)
     | acons(AType adt, list[AType] fields, list[Keyword] kwFields)
     | aprod(AProduction production)
     
     | amodule(str mname, str deprecationMessage="")
     | aparameter(str pname, AType bound) 
     | areified(AType atype)
     | avalue()
     ;
     
@memo{expireAfter(minutes=15)}
AType overloadedAType(rel[loc, IdRole, AType] overloads){
    if(all(<loc k, IdRole idr, AType t> <- overloads, aadt(adtName, params, syntaxRole) := t)){
      str adtName = "";
      list[AType] adtParams = [];
      synRoles = {};
      nformals = -1;
      for(<loc k, IdRole idr, AType t> <- overloads, aadt(adtName1, params1, syntaxRole1) := t){
        if(!isEmpty(adtName) && adtName != adtName1) fail overloadedAType; // overloading of different ADTs.
        if(nformals >= 0 && size(params1) != nformals) fail overloadedAType; else nformals = size(params1);  // different type parameter arities
        
        adtName = adtName1;
        adtParams = params1;    // TODO take care of different parameter names
        synRoles += syntaxRole1;
      }
      syntaxRole = overloadSyntaxRole(synRoles);
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
    //if(size(choices) == 1) return getFirstFrom(choices);
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
     | cycle(AType atype, int cycleLength)  // <2>
     | amb(set[Tree] alternatives) // <3> 
     | char(int character) // <4>
     ;
     
public /*const*/ AType treeType = aadt("Tree", [], dataSyntax());

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
    | illegalSyntax()
    ;
    
SyntaxRole overloadSyntaxRole(set[SyntaxRole] syntaxRoles) {
   if({SyntaxRole sr} := syntaxRoles) return sr;
   if({SyntaxRole sr, dataSyntax()} := syntaxRoles) return sr; 
   return illegalSyntax();
}

bool isConcreteSyntaxRole(SyntaxRole sr) = sr in {lexicalSyntax(), contextFreeSyntax(), layoutSyntax(), keywordSyntax()};

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
     = prod(AType def, list[AType] atypes, set[Attr] attributes={}, loc src=|unknown:///|, str label = "") // <1>
     | regular(AType def) // <2>
     ;
     
data AProduction 
     = \priority(AType def, list[AProduction] choices) // <5>
     | \associativity(AType def, Associativity \assoc, set[AProduction] alternatives) // <6>
     | \reference(AType def, str cons) // <7>
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
data ACharRange = arange(int begin, int end);

alias ACharClass = list[ACharRange];

public /*const*/ int minUniCode = 1;
public /*const*/int maxUniCode = 0x10FFFF;
public /*const*/ AType anyCharType = \char-class([arange(minUniCode, maxUniCode)]);

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

// For convenience in transition period

//AType \sort(str sname)      = aadt(sname, [], contextFreeSyntax());
//AType \lex(str sname)       = aadt(sname, [], lexicalSyntax());
AType \layouts(str sname)   = aadt(sname, [], layoutSyntax());
//AType \keywords(str sname)  = aadt(sname, [], keywordSyntax());
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
     | \opt(AType atype)  // <12>
     | \iter(AType atype) // <13>
     | \iter-star(AType atype)  // <14>
     | \iter-seps(AType atype, list[AType] separators)      // <15> 
     | \iter-star-seps(AType atype, list[AType] separators) // <16>
     | \alt(set[AType] alternatives) // <17>
     | \seq(list[AType] atypes)     // <18>
     | \start(AType atype)
     ;
  
data AType // <19>
     = \conditional(AType atype, set[ACondition] conditions);

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

// ---- Grammar 


@doc{
.Synopsis
The Grammar datatype

.Description
Grammar is the internal representation (AST) of syntax definitions used in Rascal.
A grammar is a set of productions and set of start symbols. The productions are 
stored in a map for efficient access.
}
data AGrammar 
  = \grammar(set[AType] starts, map[AType sort, AProduction def] rules)
  ;
 
public AGrammar grammar(set[AType] starts, set[AProduction] prods) {
  rules = ();

  for (p <- prods) {
    t = (p.def is label) ? p.def.symbol : p.def;
    rules[t] = t in rules ? choice(t, {p, *rules[t]}) : choice(t, {p});
  } 
  return grammar(starts, rules);
} 
           
AGrammar grammar(type[&T <: Tree] sym)
    = grammar({sym.symbol}, sym.definitions);

  
@doc{
.Synopsis
An item is an index into the symbol list of a production rule.
}  
data Item = item(AProduction production, int index);

@doc{
.Synopsis
Compose two grammars.

.Description
Compose two grammars by adding the rules of g2 to the rules of g1.
The start symbols of g1 will be the start symbols of the resulting grammar.
}
public AGrammar compose(AGrammar g1, AGrammar g2) {
  for (s <- g2.rules)
    if (g1.rules[s]?)
      g1.rules[s] = choice(s, {g1.rules[s], g2.rules[s]});
    else
      g1.rules[s] = g2.rules[s];
  g1.starts += g2.starts;

  reduced_rules = ();
  for(s <- g1.rules){
      c = g1.rules[s];
      c.alternatives -= { *choices | priority(_, choices) <- c.alternatives } +
                        { *alts | associativity(_, _, alts) <- c.alternatives};
      reduced_rules[s] = c;
  }
  
  return grammar(g1.starts, reduced_rules);
}    


//public set[AType] numericTypes = { aint(), areal(), arat(), anum() };

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

bool asubtype(tvar(s), AType r) { 
    //println("asubtype(tvar(<s>), <r>)");
    throw TypeUnavailable(); 
}
bool asubtype(AType l, tvar(s)) {
    //println("asubtype(<l> tvar(<s>))");
    throw TypeUnavailable(); 
}


bool asubtype(overloadedAType(overloads), AType r) = !isEmpty(overloads) && any(<k, idr, tp> <- overloads, asubtype(tp, r));

bool asubtype(AType l, overloadedAType(overloads)) = !isEmpty(overloads) && any(<k, idr, tp> <- overloads, asubtype(l, tp));

bool asubtype(AType _, avalue()) = true;

bool asubtype(avoid(), AType _) = true;

bool asubtype(anode(_), anode(_)) = true;
  
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), acons(a,list[AType] bp, list[Keyword] _)) = asubtype(ap,bp);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), adt: aadt(str _, list[AType] _, _)) = asubtype(a,adt);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), afunc(a,list[AType] bp, list[Keyword] _)) = asubtype(ap,bp);
bool asubtype(acons(AType a, list[AType] ap, list[Keyword] _), anode(_)) = true;

bool asubtype(acons(a,list[AType] ap, list[Keyword] _), afunc(AType b, list[AType] bp, list[Keyword] _)) = asubtype(a, b) && comparable(ap, bp);
bool asubtype(afunc(AType a, list[AType] ap, list[Keyword] _), acons(b,list[AType] bp, list[Keyword] _)) = asubtype(a, b) && comparable(ap, bp);


bool asubtype(aadt(str _, list[AType] _, _), anode(_)) = true;
bool asubtype(adt: aadt(str _, list[AType] _, _), acons(AType a, list[AType] ap, list[Keyword] _)) = asubtype(adt, a);
bool asubtype(aadt(str n, list[AType] l, _), aadt(n, list[AType] r, _)) = asubtype(l, r);
bool asubtype(aadt(_, _, sr), aadt("Tree", _, _)) = true when isConcreteSyntaxRole(sr);

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

bool asubtype(AType::\lit(_), aadt("Tree", [], _)) = true;
bool asubtype(AType::\cilit(_), aadt("Tree", [], _)) = true;
bool asubtype(AType::\char-class(_), aadt("Tree", [], _)) = true;

// TODO: add subtype for elements under optional and alternative, but that would also require auto-wrapping/unwrapping in the run-time
// bool subtype(AType s, \opt(AType t)) = subtype(s,t);
// bool subtype(AType s, \alt({AType t, *_}) = true when subtype(s, t); // backtracks over the alternatives

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
    
// note that comparability is enough for function argument sub-typing due to pattern matching semantics
bool asubtype(afunc(AType r1, list[AType] p1, list[Keyword] _), afunc(AType r2, list[AType] p2, list[Keyword] _))
    = asubtype(r1, r2) && comparable(p1, p2);

// aparameter
bool asubtype(aparameter(str pname1, AType bound1), AType r) =
     aparameter(str _, AType bound2) := r ? asubtype(bound1, bound2) : asubtype(bound1, r)
    when /aparameter(pname1,_) !:= r;
bool asubtype(AType l, r:aparameter(str pname2, AType bound2)) = 
    aparameter(str _, AType bound1) := l ? asubtype(bound1, bound2) : asubtype(l, bound2)
    when /aparameter(pname2,_) !:= l;

// areified
bool asubtype(areified(AType s), areified(AType t)) = asubtype(s,t);
bool asubtype(areified(AType s), anode(_)) = true;

bool asubtype(anode(list[AType] l), anode(list[AType] r)) = l <= r;

// Character classes and char
bool asubtype(l:\char-class(_), r:\char-class(_)) {
    return  l.ranges == r.ranges || (difference(r, l) != \char-class([]));
}
bool asubtype(l:\char-class(_), aadt("Tree", _, _)) = true; // characters are Tree instances 

bool asubtype(\char(int c), \char-class(list[ACharRange] ranges)) {
    res = difference(ranges, [arange(c,c)]) == [arange(c,c)];
    return res;
}
bool asubtype(l:\char-class(list[ACharRange] ranges), \char(int c)) = l == \char-class([arange(c,c)]);

// Utilities

bool asubtype(atypeList(list[AType] l), atypeList(list[AType] r)) = asubtype(l, r);

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool asubtype(list[AType] l, list[AType] r) = all(i <- index(l), asubtype(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool asubtype(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

@doc{
.Synopsis
Check if two types are comparable, i.e., have a common supertype.
}
bool comparable(AType s, AType t)
    = s == t || asubtype(s,t) || asubtype(t,s);

bool comparable(list[AType] l, list[AType] r) = all(i <- index(l), comparable(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool comparable(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;

bool outerComparable(AType l, AType r){
    return outerComparable1(l, r);
}

bool outerComparable1(AType l, l) = true;
bool outerComparable1(alist(_), alist(_)) = true;
bool outerComparable1(aset(_), aset(_)) = true;
bool outerComparable1(abag(_), abag(_)) = true;
bool outerComparable1(arel(atypeList(list[AType] ts1)), arel(atypeList(list[AType] ts2))) = size(ts1) == size(ts2);
bool outerComparable1(arel(_), aset(_)) = true;
bool outerComparable1(aset(_), arel(_)) = true;
bool outerComparable1(alrel(atypeList(list[AType] ts1)), alrel(atypeList(list[AType] ts2))) = size(ts1) == size(ts2);
bool outerComparable1(alrel(_), alist(_)) = true;
bool outerComparable1(atuple(atypeList(ts1)), atuple(atypeList(ts2))) = size(ts1) == size(ts2);
bool outerComparable1(amap(_,_), amap(_,_)) = true;

bool outerComparable1(f1:afunc(AType r1, list[AType] p1, list[Keyword] _), f2:afunc(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && (f1.varArgs ? (f2.varArgs ? outerComparable(p1, p2)
                                                           : outerComparable(p1[0..-1], p2))
                                             : (f2.varArgs ? outerComparable(p1, p2[0..-1])
                                                           : outerComparable(p1, p2)));              
    
bool outerComparable1(afunc(AType r1, list[AType] p1, list[Keyword] _), acons(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && outerComparable(p1, p2);
bool outerComparable1(acons(AType r1, list[AType] p1, list[Keyword] _), afunc(AType r2, list[AType] p2, list[Keyword] _))
    = outerComparable(r1, r2) && outerComparable(p1, p2);

bool outerComparable1(aparameter(str pname1, AType bound1), aparameter(str pname2, AType bound2)) 
    = outerComparable(bound1, bound2);

bool outerComparable1(aadt(str adtName1, list[AType] parameters1, SyntaxRole syntaxRole1),  areified(_)) = true;


default bool outerComparable1(AType l, AType r) {
    return comparable(l, r);
}

bool outerComparable(list[AType] l, list[AType] r) = all(i <- index(l), outerComparable(l[i], r[i])) when size(l) == size(r) && size(l) > 0;
default bool outerComparable(list[AType] l, list[AType] r) = size(l) == 0 && size(r) == 0;


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
AType alub(tvar(s), AType r) { 
    //println("alub(tvar(<s>), <r>)"); 
    throw TypeUnavailable(); 
} 
AType alub(AType l, tvar(s)) { 
    //println("alub(<l>, tvar(<s>))"); 
    throw TypeUnavailable(); 
}

AType alub(AType s, s) = s;
default AType alub(AType s, AType t)
    = (s.label? || t.label?) ? (s.label == t.label)  ? alub(unset(s, "label") , unset(t, "label"))[label=s.label]
                                                     : alub(unset(s, "label"), unset(t, "label"))
                             : avalue();

AType alub(atypeList(ts1), atypeList(ts2)) = atypeList(alubList(ts1, ts2));
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
                                                = addADTLabel(a1, a2, aadt(n, alubList(lp,rp), sr))
                                                  when size(lp) == size(rp) && getTypeParamNames(lp) == getTypeParamNames(rp) && size(getTypeParamNames(lp)) > 0 &&
                                                       sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(a1:aadt(str n, list[AType] lp, SyntaxRole lsr), a2:aadt(n, list[AType] rp, SyntaxRole rsr)) 
                                                = addADTLabel(a1, a2, aadt(n, alubList(lp,rp), sr))
                                                  when size(lp) == size(rp) && size(getTypeParamNames(lp)) == 0 && sr := overloadSyntaxRole({lsr, rsr}) && sr != illegalSyntax();
                                                                         
AType alub(aadt(str n, list[AType] lp, SyntaxRole _), aadt(str m, list[AType] rp,SyntaxRole _)) = anode([]) when n != m;
AType alub(a1: aadt(str ln, list[AType] lp,SyntaxRole  _), acons(AType b, _, _)) = alub(a1,b);

AType addADTLabel(AType a1, AType a2, AType adt){
  if(a1.label? && a1.label == a2.label) adt = adt[label=a1.label];
  return adt;
}

//AType alub(acons(AType la, list[AType] _,  list[Keyword] _), acons(AType ra, list[AType] _, list[Keyword] _)) = alub(la,ra);
AType alub(acons(AType lr, list[AType] lp, list[Keyword] lkw), acons(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(/*lr == rr && */size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp) && size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), acons(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp) && size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

AType alub(acons(AType a,  list[AType] lp, list[Keyword] _), a2:aadt(str n, list[AType] rp, SyntaxRole _)) = alub(a,a2);
AType alub(acons(AType _,  list[AType] _,  list[Keyword] _), anode(_)) = anode([]);

AType alub(anode(list[AType] l), anode(list[AType] r)) = anode(l & r);

bool keepParams(aparameter(str s1, AType bound1), aparameter(str s2, AType bound2)) = s1 == s2 && equivalent(bound1,bound2);

AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = l when keepParams(l,r);
AType alub(AType l:aparameter(str s1, AType bound1), AType r:aparameter(str s2, AType bound2)) = alub(bound1,bound2) when !keepParams(l,r);
AType alub(aparameter(str _, AType bound), AType r) = alub(bound, r) when aparameter(_,_) !:= r; //!(isRascalTypeParam(r));
AType alub(AType l, aparameter(str _, AType bound)) = alub(l, bound) when aparameter(_,_) !:= l; //!(isRascalTypeParam(l));

AType alub(areified(AType l), areified(AType r)) = areified(alub(l,r));
AType alub(areified(AType l), anode(_)) = anode([]);

AType alub(l:\char-class(_), r:\char-class(_)) = union(l, r);
AType alub(l:aadt("Tree", _, _), \char-class(_)) = l;
AType alub(\char-class(_), r:aadt("Tree", _, _)) = r;
 
// TODO: missing lub of iter/iter-plus relation here.
// TODO: missing lub of aadt("Tree", _, _) with all non-terminal types such as seq, opt, iter

// because functions _match_ their parameters, parameter types may be comparable (co- and contra-variant) and not
// only contra-variant. We choose the lub here over glb (both would be correct), to
// indicate to the programmer the intuition that rather _more_ than fewer functions are substitutable.
AType alub(afunc(AType lr, list[AType] lp, list[Keyword] lkw), afunc(AType rr, list[AType] rp, list[Keyword] rkw)) {
    if(size(lp) == size(rp)){
        return afunc(alub(lr,rr), alubList(lp, rp), lkw + (rkw - lkw)); // TODO how do we want to propagate the keyword parameters?
    } else
        return avalue();
}

public list[AType] alubList(list[AType] l, list[AType] r) = [alub(l[idx],r[idx]) | idx <- index(l)] when size(l) == size(r); 
default list[AType] alubList(list[AType] l, list[AType] r) = [avalue()]; 

private list[str] getTypeParamNames(list[AType] l) = [ s | li <- l, aparameter(s,_) := li ];

@doc{Calculate the lub of a list of types.}
public AType lubList(list[AType] ts) {
    AType theLub = avoid();
    for (t <- ts) theLub = alub(theLub,t);
    return theLub;
}


