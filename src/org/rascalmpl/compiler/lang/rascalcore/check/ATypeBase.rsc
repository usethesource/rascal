@license{
Copyright (c) 2018-2025, NWO-I CWI and Swat.engineering
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
@bootstrapParser
module lang::rascalcore::check::ATypeBase

/*
    Basic declarations for the ATypes used in the checker. They extend the datat type AType introduced in TypePal.
 */

extend analysis::typepal::TypePal;

import lang::rascal::\syntax::Rascal;

//import IO;
import List;
import Set;
//import Node;
import String;

data Keyword
    = kwField(AType fieldType, str fieldName, str definingModule, Expression defaultExp)   // Only used during compilation of the current module
    | kwField(AType fieldType, str fieldName, str definingModule)      // When compilation is complete, this reduced version is saved in the TModel
    ;

data AType (str alabel = "")
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
             str deprecationMessage="",     // Only used during compilation of the current module
             bool isConcreteArg=false,      // Only used during compilation of the current module
             bool isDefault=false,          // Only used during compilation of the current module
             bool isTest=false,             // Only used during compilation of the current module
             bool returnsViaAllPath = false,// Only used during compilation of the current module
             int abstractFingerprint=0,     // Only used during compilation of the current module
             int concreteFingerprint=0)     // Only used during compilation of the current module
     | aalias(str aname, list[AType] parameters, AType aliased)
     | aanno(str aname, AType onType, AType annoType)

     | anode(list[AType] fields)
     | aadt(str adtName, list[AType] parameters, SyntaxRole syntaxRole)
     | acons(AType adt, list[AType] fields, list[Keyword] kwFields)
     | aprod(AProduction production)

     | amodule(str mname, str deprecationMessage="")
     | aparameter(str pname, AType bound, bool closed=false)
     | areified(AType atype)
     | avalue()
     ;

@memo{expireAfter(minutes=5),maximumSize(1000)}
AType overloadedAType(rel[loc, IdRole, AType] overloads){
    if(all(<loc _, IdRole _, AType t> <- overloads, aadt(_, _, _) := t)){
      str adtName = "";
      list[AType] adtParams = [];
      synRoles = {};
      nformals = -1;
      for(<loc _, IdRole _, AType t> <- overloads, aadt(adtName1, params1, syntaxRole1) := t){
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
     = \achoice(AType def, set[AProduction] alternatives)
     ;

@doc{
.Synopsis
Attributes register additional semantics annotations of a definition.
}
data AAttr
     = atag(value \tag)
     ;

@doc{
.Synopsis
Normalize the choice between alternative productions.

.Description
Nested achoice is flattened.
}
public AProduction achoice(AType s, set[AProduction] achoices){
    if(any(achoice(AType _, set[AProduction] _)  <- achoices)){
        // TODO: this does not work in interpreter and typechecker crashes on it (both related to the splicing)
        //return choice(s, { *(choice(Symbol t, set[AProduction] b) := ch ? b : {ch}) | ch <- choices });
        bool changed = false;
        new_achoices = {};
        for(ch <- achoices){
            if(achoice(AType _, set[AProduction] b) := ch){
                changed = true;
                new_achoices += b;
            } else {
                new_achoices += ch;
            }
        }
        if(changed){
            return achoice(s, new_achoices);
        }
   }
   fail;
}

// ---- Parse Tree

data ATree
     = appl(AProduction aprod, list[ATree] args/*, loc src=|unknown:///|*/) // <1>
     | cycle(AType atype, int cycleLength)  // <2>
     | aamb(set[ATree] alternatives) // <3>
     | achar(int character) // <4>
     ;

public /*const*/ AType treeType = aadt("Tree", [], dataSyntax());

public bool isTreeType(AType t) = treeType := t;

//@doc{
//.Synopsis
//Annotate a parse tree node with a source location.
//}
//anno loc Tree@\loc; // TODO: weg

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
     = prod(AType def, list[AType] atypes, set[AAttr] attributes={}, loc src=|unknown:///|, str alabel = "") // <1>
     | regular(AType def) // <2>
     ;

data AProduction
     = \priority(AType def, list[AProduction] choices) // <5>
     | \associativity(AType def, AAssociativity \assoc, set[AProduction] alternatives) // <6>
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
data AAttr
     = \aassoc(AAssociativity \assoc)
     | \abracket()
     ;

@doc{
.Synopsis
Associativity attribute.

.Description

Associativity defines the various kinds of associativity of a specific production.
}
data AAssociativity
     = aleft()
     | aright()
     | aassoc()
     | \a-non-assoc()
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
public /*const*/ AType anyCharType = \achar-class([arange(minUniCode, maxUniCode)]);

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
AType \layouts(str sname)   = aadt(sname, [], layoutSyntax());
AType \parameterized-sort(str sname, list[AType] parameters)
                            = aadt(sname, parameters, contextFreeSyntax());
AType \parameterized-lex(str sname, list[AType] parameters)
                            = aadt(sname, parameters, lexicalSyntax());

// These are the terminal symbols (isTerminalType)
data AType
     = alit(str string)   // <8>
     | acilit(str string) // <9>
     | \achar-class(list[ACharRange] ranges) // <10>
     ;

// These are the regular expressions.
data AType
     = \aempty() // <11>
     | \opt(AType atype)  // <12>
     | \iter(AType atype, bool isLexical = false) // <13>
     | \iter-star(AType atype, bool isLexical = false)  // <14>
     | \iter-seps(AType atype, list[AType] separators, bool isLexical = false)      // <15>
     | \iter-star-seps(AType atype, list[AType] separators, bool isLexical = false) // <16>
     | \alt(set[AType] alternatives) // <17>
     | \seq(list[AType] atypes)     // <18>
     | \start(AType atype)
     ;

//public AType \iter-seps(AType atype, [])  = \iter(atype);
//public AType \iter-star-seps(AType atype, [])  = \iter-star(atype);

// flattening rules
public AType seq([*AType a, seq(list[AType] b), *AType c]) = seq(a + b + c);

public AType alt({*AType a, alt(set[AType] b)}) = alt(a + b);

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
     | \a-at-column(int column)
     | \a-begin-of-line()
     | \a-end-of-line()
     | \a-except(str label)
     ;

AType \conditional(AType s, {*ACondition a, \conditional(AType _, set[ACondition] b), *ACondition c})
    = \conditional(s, a+b+c);

AType \conditional(\conditional(AType s, set[ACondition] a), set[ACondition] b)
    = \conditional(s, a+b);

AType \follow(\conditional(AType s, set[ACondition] a))
    = \conditional(s, a + {\follow(s)});

AType \not-follow(\conditional(AType s, set[ACondition] a))
    = \conditional(s, a + {\not-follow(s)});

AType \precede(\conditional(AType s, set[ACondition] a))
    = \conditional(s, a + {precede(s)});

AType \not-precede(\conditional(AType s, set[ACondition] a))
    = \conditional(s, a + {\not-precede(s)});



@doc{
.Synopsis
Nested priority is flattened.
}
public AProduction priority(AType s, [*AProduction a, priority(AType _, list[AProduction] b), *AProduction c])
  = priority(s,a+b+c);

@doc{
.Synopsis
Normalization of associativity.

.Description

* Choice (see the `choice` constructor in <<Type-ParseTree>>) under associativity is flattened.
* Nested (equal) associativity is flattened.
* Priority under an associativity group defaults to choice.
}
AProduction associativity(AType s, AAssociativity as, {*AProduction a, achoice(AType t, set[AProduction] b)})
  = associativity(s, as, a+b);

AProduction associativity(AType rhs, AAssociativity a, {associativity(rhs, AAssociativity b, set[AProduction] alts), *AProduction rest})
  = associativity(rhs, a, rest + alts); // the nested associativity, even if contradictory, is lost

AProduction associativity(AType s, AAssociativity as, {AProduction a, priority(AType t, list[AProduction] b)})
  = associativity(s, as, {a, *b});

// deprecated; remove after bootstrap
AProduction associativity(AType rhs, AAssociativity a, set[AProduction] rest)
  = associativity(rhs, a, withAssoc + withNewAssocs)
  when  withoutAssoc := {p | AProduction p:prod(_,_) <- rest, !(\aassoc(_) <- p.attributes)},
        withoutAssoc != {},
        withAssoc := rest - withoutAssoc,
        withNewAssocs := {p[attributes = p.attributes + {\aassoc(a)}] | AProduction p <- withoutAssoc}
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
  map[AType sort, AProduction def]rules = ();

  for (p <- prods) {
    t = /*(p.def is label) ? p.def.symbol : */ p.def;
    rules[t] = t in rules ? achoice(t, {p, rules[t]}) : achoice(t, {p});
  }
  return grammar(starts, rules);
}

//AGrammar grammar(type[&T <: Tree] sym)
//    = grammar({sym.symbol}, sym.definitions);


@doc{
.Synopsis
An item is an index into the symbol list of a production rule.
}
data Item = item(AProduction aproduction, int index);

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
      g1.rules[s] = achoice(s, {g1.rules[s], g2.rules[s]});
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




