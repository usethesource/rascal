@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bas Basten - Bas.Basten@cwi.nl (CWI)}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@contributor{Arnold Lankamp - Arnold.Lankamp@cwi.nl}

@synopsis{Library functions for parse trees.}
@description{
A _concrete syntax tree_ or [parse tree](http://en.wikipedia.org/wiki/Parse_tree) is an ordered, rooted tree that 
represents the syntactic structure of a string according to some formal grammar. 

Most Rascal users will encounter parse trees in the form of concrete values.
Expert users may find the detailed description here useful when writing generic functions on parse trees. 

In Rascal parse trees, the interior nodes are labeled by rules of the grammar, 
while the leaf nodes are labeled by terminals (characters) of the grammar. 

`Tree` is the universal parse tree data type in Rascal and can be used to represent parse trees for any language.

*  `Tree` is a subtype of the type `node`
*  All types (non-terminals) declared in `syntax`, `lexical`, `layout` and `keyword`  definitions are sub-types of `Tree`.
*  All concrete syntax expressions produce parse trees with a type corresponding to a non-terminal.
*  Trees can be annotated in various ways.  Most importantly the `\loc` annotation always points to the source location of any (sub) parse tree.

_Advanced users_ may want to create tools that analyze any parse tree, regardless of the 
syntax definition that generated it, you can manipulate them on the abstract level.

A parse tree is of type ((ParseTree-Tree)) using the auxiliary types 
((ParseTree-Production)), ((ParseTree-Symbol)), ((ParseTree-Condition)),
((ParseTree-Attr)), ((ParseTree-Associativity)), ((ParseTree-CharRange)).
Effectively, a parse tree is a nested tree structure of type `Tree`. 

*  Most internal nodes are applications (`appl`) of a `Production` to a list of children `Tree` nodes. 
   `Production` is the abstract representation of a rule in a
   syntax definition.
   which consists of a definition of an alternative for a `Symbol` by a list of `Symbols`.
*  The leaves of a parse tree are always
characters (`char`), which have an integer index in the UTF8 table. 

*  Some internal nodes encode ambiguity (`amb`) by pointing to a set of 
alternative `Tree` nodes.

The `Production` and `Symbol` types are an abstract notation for rules in syntax definitions,
while the `Tree` type is the actual notation for parse trees. 

Parse trees are called parse forests when they contain `amb` nodes.

You can analyze and manipulate parse trees in three ways:

*  Directly on the `Tree` level, just like any other algebraic data type.
*  Using concrete syntax expressions and concrete syntax patterns.
*  Using disambiguation actions (parameters of the `parse` function)

The type of a parse tree is the symbol that it's production produces, i.e. `appl(prod(sort("A"),[],{}),[])` has type `A`. Ambiguity nodes 
Each such a non-terminal type has `Tree` as its immediate super-type.
}
@examples{
```rascal-shell
import ParseTree;
syntax A = "a";
// will make the following succeed:
t = parse(#A,"a");
t := appl(
  prod(
    sort("A"),
    [lit("a")],
    {}),
  [appl(
      prod(
        lit("a"),
        [\char-class([range(97,97)])],
        {}),
      [char(97)])]);
```
You see that the defined non-terminal A ends up as the production for the outermost node. 
As the only child is the tree for recognizing the literal a, which is defined to be a single a from the character-class `[ a ]`.

When we use labels in the definitions, they also end up in the trees.
The following definition
```rascal-shell
import ParseTree;
lexical B= myB:"b";
lexical C = myC:"c" B bLabel;
// Will make the following succeed:
t = parse(#C,"cb");
t := appl(
  prod(
    label(
      "myC",
      lex("C")),
    [
      lit("c"),
      label(
        "bLabel",
        lex("B"))
    ],
    {}),
  [appl(
      prod(
        lit("c"),
        [\char-class([range(99,99)])],
        {}),
      [char(99)]),appl(
      prod(
        label(
          "myB",
          lex("B")),
        [lit("b")],
        {}),
      [appl(
          prod(
            lit("b"),
            [\char-class([range(98,98)])],
            {}),
          [char(98)])])]);
```

Here you see that the alternative name is a label around the first argument of `prod` while argument labels become 
labels in the list of children of a `prod`.
}
@benefits{
* Parse trees have all the necessary information in them for high-fidelity source code analysis and transformation
* Parse trees contain full definitions of the grammar rules that it applies
* Parse trees can always be "unparsed" to source text again
}
@pitfalls{
* For historical reasons the name of the annotation is "loc" and this interferes with the Rascal keyword `loc`
for the type of source locations. Therefore the annotation name has to be escaped as `\loc` when it is declared or used.
* We are in transition from deprecating the annotation `@\loc` with the keyword field `.src=|unknown:///|`. Currently the
run-time already uses `.src` while the source code still uses `@\loc`.
}

module ParseTree

extend Type;
extend Message;
extend List;

@synopsis{The Tree data type as produced by the parser.}
@description{
A `Tree` defines the trees normally found after parsing; additional constructors exist for execptional cases:

<1> Parse tree constructor when parse succeeded.
<2> Cyclic parsetree.
<3> Ambiguous subtree.
<4> A single character.
}

data Tree //(loc src = |unknown:///|(0,0,<0,0>,<0,0>))
     = appl(Production prod, list[Tree] args) // <1>
     | cycle(Symbol symbol, int cycleLength)  // <2>
     | amb(set[Tree] alternatives) // <3> 
     | char(int character) // <4>
     ;


@synopsis{Production in ParseTrees}
@description{
The type `Production` is introduced in ((Library:module:Type)), see ((Type-Production)). Here we extend it with the symbols
that can occur in a ParseTree. We also extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

<1> A `prod` is a rule of a grammar, with a defined non-terminal, a list
    of terminal and/or non-terminal symbols and a possibly empty set of attributes.
  
<2> A `regular` is a regular expression, i.e. a repeated construct.

<3> `priority` means operator precedence, where the order of the list indicates the binding strength of each rule;
<4> `assoc`  means all alternatives are acceptable, but nested on the declared side;
<5> `reference` means a reference to another production rule which should be substituted there,
    for extending priority chains and such.
<6> `error` means a node produced by error recovery.
<7> `skipped` means characters skipped during error recovery, always the last child of an `appl` with a `error` production.
} 
data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) // <1>
     | regular(Symbol def) // <2>
     ;
     
data Production 
     = \priority(Symbol def, list[Production] choices) // <3>
     | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives) // <4>
     | \reference(Symbol def, str cons) // <5>
     ;

data Production
     = \error(Symbol def, Production prod, int dot)
     | \skipped(Symbol def);

@synopsis{Attributes in productions.}
@description{
An `Attr` (attribute) documents additional semantics of a production rule. Neither tags nor
brackets are processed by the parser generator. Rather downstream processors are
activated by these. Associativity is a parser generator feature though.
}
data Attr 
  = \bracket() 
  | \assoc(Associativity \assoc)
  ;


@synopsis{Associativity attribute.}
@description{
Associativity defines the various kinds of associativity of a specific production.
}  
data Associativity 
    = \left()
    | \right() 
    | \assoc() 
    | \non-assoc()
    ;


@synopsis{Character ranges and character class}
@description{
*  `CharRange` defines a range of characters.
*  A `CharClass` consists of a list of characters ranges.
}
data CharRange = range(int begin, int end);

alias CharClass = list[CharRange];


@synopsis{Symbols that can occur in a ParseTree}
@description{
The type `Symbol` is introduced in ((Library:module:Type)), see ((Type-Symbol)), to represent the basic Rascal types,
e.g., `int`, `list`, and `rel`. Here we extend it with the symbols that may occur in a ParseTree.

<1>  The `start` symbol wraps any symbol to indicate that it is a start symbol of the grammar and
        may occur at the root of a parse tree.
<2>  Context-free non-terminal
<3>  Lexical non-terminal
<4>  Layout symbols
<5>  Terminal symbols that are keywords
<6>  Parameterized context-free non-terminal
<7>  Parameterized lexical non-terminal
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
data Symbol // <1>
     = \start(Symbol symbol);

// These symbols are the named non-terminals.
data Symbol 
     = \sort(str name) // <2> 
     | \lex(str name)  // <3>
     | \layouts(str name)  // <4>
     | \keywords(str name) // <5>
     | \parameterized-sort(str name, list[Symbol] parameters) // <6>
     | \parameterized-lex(str name, list[Symbol] parameters)  // <7>
     ; 

// These are the terminal symbols.
data Symbol 
     = \lit(str string)   // <8>
     | \cilit(str string) // <9>
     | \char-class(list[CharRange] ranges) // <10>
     ;
    
// These are the regular expressions.
data Symbol
     = \empty() // <11>
     | \opt(Symbol symbol)  // <12>
     | \iter(Symbol symbol) // <13>
     | \iter-star(Symbol symbol)  // <14>
     | \iter-seps(Symbol symbol, list[Symbol] separators)      // <15> 
     | \iter-star-seps(Symbol symbol, list[Symbol] separators) // <16>
     | \alt(set[Symbol] alternatives) // <17>
     | \seq(list[Symbol] symbols)     // <18>
     ;
  
data Symbol // <19>
     = \conditional(Symbol symbol, set[Condition] conditions);

bool subtype(Symbol::\sort(_), Symbol::\adt("Tree", _)) = true;


@synopsis{Datatype for declaring preconditions and postconditions on symbols}
@description{
A `Condition` can be attached to a symbol; it restricts the applicability
of that symbol while parsing input text. For instance, `follow` requires that it
is followed by another symbol and `at-column` requires that it occurs 
at a certain position in the current line of the input text.
}
data Condition
     = \follow(Symbol symbol)
     | \not-follow(Symbol symbol)
     | \precede(Symbol symbol)
     | \not-precede(Symbol symbol)
     | \delete(Symbol symbol)
     | \at-column(int column) 
     | \begin-of-line()  
     | \end-of-line()  
     | \except(str label)
     ;


@synopsis{Nested priority is flattened.}
Production priority(Symbol s, [*Production a, priority(Symbol _, list[Production] b), *Production c])
  = priority(s,a+b+c);
   

@synopsis{Normalization of associativity.}
@description{
* The ((Type-choice)) constructor under associativity is flattened.
* Nested (equal) associativity is flattened.
* ((ParseTree-priority)) under an associativity group defaults to choice.
}
Production associativity(Symbol s, Associativity as, {*Production a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
            
Production associativity(Symbol rhs, Associativity a, {associativity(rhs, Associativity b, set[Production] alts), *Production rest})  
  = associativity(rhs, a, rest + alts); // the nested associativity, even if contradictory, is lost

Production associativity(Symbol s, Associativity as, {*Production a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, {*a, *b}); 
        

@synopsis{Annotate a parse tree node with a source location.}
anno loc Tree@\loc;


@synopsis{Parse input text (from a string or a location) and return a parse tree.}
@description{
*  Parse a string and return a parse tree.
*  Parse a string and return a parse tree, `origin` defines the original location of the input.
*  Parse the contents of resource input and return a parse tree.

The parse either throws ParseError exceptions or returns parse trees of type ((Tree)).

The `allowAmbiguity` flag dictates the behavior of the parser in the case of ambiguity. When `allowAmbiguity=true` 
the parser will construct ambiguity clusters (local sets of parse trees where the input string is ambiguous). If it is `false`
the parser will throw an `Ambiguous` exception instead. An `Ambiguous` exception is comparable to a ParseError exception then.
The latter option terminates much faster, i.e. always in cubic time, and always linear in the size of the intermediate parse graph, 
while constructing ambiguous parse forests may grow to O(n^p+1), where p is the length of the longest production rule and n 
is the length of the input.

The `allowRecovery` can be set to `true` to enable error recovery. This is an experimental feature.
When error recovery is enabled, the parser will attempt to recover from parse errors and continue parsing.
If successful, a parse tree with error and skipped productions is returned (see the definition of `Production` above).
The `util::ErrorRecovery` module contains a number of functions to analyze trees with errors, for example `hasErrors`, `getSkipped`, and `getErrorText`.
Note that the resulting parse forest can contain a lot of ambiguities. Any code that processes error trees must be aware of this,
for instance a simple traversal of all subtrees will be too expensive in most cases. `disambiguateErrors` can be used to 
efficiently prune the forest and leave a tree with a single (or even zero) errors based on simple heuristics, but these heuristics
are somewhat arbitrary so the usability of this function is limited.

The `filters` set contains functions which may be called optionally after the parse algorithm has finished and just before
the Tree representation is built. The set of functions contain alternative functions, only on of them is successfully applied
to each node in a tree. If such a function fails to apply, the other ones are tried. There is no fixed-point computation, so
composed filters must be added to the set of filters programmatically. Post-parse filtering is best done at this stage and
not later on the Tree representation for efficiency reasons. Namely, the size of the parse graph before Tree construction
is still cubic due to "binarized" sharing of intermediate nodes, while after Tree construction the forest may obtain
a size in O(n^p+1) where n is the length of the input and p is the length of the longest syntax rule. Filtering using
the `filters` parameter, on the other hand, may very well cut the forest quickly down to even a linear size and result in 
an efficient overall parsing algorithm.

The `hasSideEffects` flag is normally set to false. When the `filters` functions have side-effects to
remove ambiguity, this option must be set to `true` to ensure correct behavior. A side-effect of filter functions is
typically the construction of a symbol table and the removal (see [[Statements/Filter]]) of syntax trees which refer to 
undefined symbols. In such a case `hasSideEffects` must be set to `true` for correctness' sake. If its set to `false`
then the algorithm assumes tree construction is context-free and it can memoize the results of shared intermediate graph nodes.
The tree construction algorithm is effectively always worst case
polynomial in O(n^p+1) --p being the length of the longest syntax rule-- when `hasSideEffects` is true, but may be linear when set 
to false. So this is quite an important flag to consider.
}
@examples{
```rascal-shell
lexical Number = [0-9]+;
syntax Exp
    = Number
    | left Exp "+" Exp
    ;

import ParseTree;
```
Seeing that `parse` returns a parse tree:
```rascal-shell,continue
parse(#Exp, "2+3");
```
Catching a parse error:
```rascal-shell,continue
import IO;
try {
  Exp e = parse(#Exp, "2@3");
}
catch ParseError(loc l): {
  println("I found a parse error at line <l.begin.line>, column <l.begin.column>");
}
```
}

&T<:Tree parse(type[&T<:Tree] begin, str input, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
  = parser(begin, allowAmbiguity=allowAmbiguity, allowRecovery=allowRecovery, hasSideEffects=hasSideEffects, filters=filters)(input, |unknown:///|);

&T<:Tree parse(type[&T<:Tree] begin, str input, loc origin, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
  = parser(begin, allowAmbiguity=allowAmbiguity, allowRecovery=allowRecovery, hasSideEffects=hasSideEffects, filters=filters)(input, origin);
  
&T<:Tree parse(type[&T<:Tree] begin, loc input, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={})
  = parser(begin, allowAmbiguity=allowAmbiguity, allowRecovery=allowRecovery, hasSideEffects=hasSideEffects, filters=filters)(input, input);


@synopsis{Generates a parser from an input grammar.}
@description{
This builtin function wraps the Rascal parser generator by transforming a grammar into a parsing function.

The resulting parsing function has the following overloaded signature:

   * Tree parse(str input, loc origin);
   * Tree parse(loc input, loc origin);

So the parse function reads either directly from a str or via the contents of a loc. It also takes a `origin` parameter
which leads to the prefix of the `src` fields of the resulting tree.

The parse function behaves differently depending of the given keyword parameters:
     * `allowAmbiguity`: if true then no exception is thrown in case of ambiguity and a parse forest is returned. if false,
                         the parser throws an exception during tree building and produces only the first ambiguous subtree in its message.
                         if set to `false`, the parse constructs trees in linear time. if set to `true` the parser constructs trees in polynomial time.
     * 'allowRecovery`: ***experimental*** if true, the parser tries to recover when it encounters a parse error. if a parse error is encountered that can be recovered from,
                         special `error` and `skipped` productions are included in the resulting parse tree. More documentation will be added here when this feature matures.
                         Note that if `allowRecovery` is set to true, the resulting tree can still contain ambiguity nodes related to recovered parse errors, even if `allowAmbiguity`
                         is set to false. When a 'regular` (non-error) ambiguity is found an exception is still thrown in this case.
     *  `hasSideEffects`: if false then the parser is a lot faster when constructing trees, since it does not execute the parse _actions_ in an
                         interpreted environment to make side effects (like a symbol table) and it can share more intermediate results as a result.
}
@javaClass{org.rascalmpl.library.Prelude}
java &T (value input, loc origin) parser(type[&T] grammar, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Generates a parser function that can be used to find the left-most deepest ambiguous sub-sentence.}
@benefits{
* Instead of trying to build a polynomially sized parse forest, this function only builds the smallest part of
the tree that exhibits ambiguity. This can be done very quickly, while the whole forest could take minutes to hours to construct.
* Use this function for ambiguity diagnostics and regression testing for ambiguity.
}
@pitfalls{
* The returned sub-tree usually has a different type than the parameter of the type[] symbol that was passed in. 
The reason is that sub-trees typically have a different non-terminal than the start non-terminal of a grammar.
}
java Tree (value input, loc origin) firstAmbiguityFinder(type[Tree] grammar, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={}); 

@synopsis{Generates parsers from a grammar (reified type), where all non-terminals in the grammar can be used as start-symbol.}
@description{
This parser generator behaves the same as the `parser` function, but it produces parser functions which have an additional
nonterminal parameter. This can be used to select a specific non-terminal from the grammar to use as start-symbol for parsing.
}
@javaClass{org.rascalmpl.library.Prelude}
java &U (type[&U] nonterminal, value input, loc origin) parsers(type[&T] grammar, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false,  set[Tree(Tree)] filters={}); 

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Generates a parser function that can be used to find the left-most deepest ambiguous sub-sentence.}
@benefits{
* Instead of trying to build a polynomially sized parse forest, this function only builds the smallest part of
the tree that exhibits ambiguity. This can be done very quickly, while the whole forest could take minutes to hours to construct.
* Use this function for ambiguity diagnostics and regression testing for ambiguity.
}
@pitfalls{
* The returned sub-tree usually has a different type than the parameter of the type[] symbol that was passed in. 
The reason is that sub-trees typically have a different non-terminal than the start non-terminal of a grammar.
}
java Tree (type[Tree] nonterminal, value input, loc origin) firstAmbiguityFinders(type[Tree] grammar, bool allowRecovery=false, bool hasSideEffects=false,  set[Tree(Tree)] filters={}); 

@synopsis{Parse the input but instead of returning the entire tree, return the trees for the first ambiguous substring.}
@description{
This function is similar to the ((parse)) function in its functionality. However, in case of serious ambiguity parse
could be very slow. This function is much faster, because it does not try to construct an entire forest and thus avoids
the cost of constructing nested ambiguity clusters. 

If the input sentence is not ambiguous after all, simply the entire tree is returned.
}
Tree firstAmbiguity(type[Tree] begin, str input)
  = firstAmbiguityFinder(begin)(input, |unknown:///|);

Tree firstAmbiguity(type[Tree] begin, loc input)
  = firstAmbiguityFinder(begin)(input, input);

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Generate a parser and store it in serialized form for later reuse.}
@description{
The stored parsers would be able to be recovered later using ((loadParsers)).

For any concrete grammar, a.k.a. reified syntax type, `g` it holds that:
* after `storeParsers(g, file);`
* then `g = loadParsers(file);`
* and given `h = parsers(g);`
* then for all valid `nt`, `input` and `origin`: `g(nt, input, origin) == h(nt, input, origin)`

In other words, a loaded parser function behaves exactly as a freshly generated parser
for the same grammar, if (and only if) it was stored for the same grammar value.
}
@benefits{
* storing parsers is to cache the work of reifing a grammar, and generating a parser from that grammar.
* stored parsers are nice for deployment scenerios where the language is fixed and efficiency is appreciated.
}
@pitfalls{
* caching parsers with `storeParsers` is your responsibility; the cache is not cleaned automatically when the grammar changes.
}
java void storeParsers(type[Tree] grammar, loc saveLocation);

@javaClass{org.rascalmpl.library.Prelude}
@synopsis{Load a previously serialized parser from disk for usage}
@description{
For any concrete grammar, a.k.a. reified syntax type, `g` it holds that:
* after `storeParsers(g, file);`
* then `g = loadParsers(file);`
* and given `h = parsers(g);`
* then for all valid `nt`, `input` and `origin`: `g(nt, input, origin) == h(nt, input, origin)`

In other words, a loaded parser function behaves exactly as a freshly generated parser
for the same grammar, if (and only if) it was stored for the same grammar value.
}
@examples{
First we store a parser:
```rascal-shell
import ParseTree;
syntax E = E "+" E | "e";
storeParsers(#E, |memory://test-tmp/E.parsers|)
```

Here we show a new shell does not even know about the grammar:
```rascal-shell,errors
#E
```

Then in a next run, we load the parser and use it:
```rascal-shell
import ParseTree;
p = loadParsers(|memory://test-tmp/E.parsers|);
p(type(sort("E"), ()), "e+e", |src:///|);
```
}
@benefits{
* loaded parsers can be used immediately without the need of loadig and executing a parser generator.
}
@pitfalls{
* reifiying types (use of `#`) will trigger the loading of a parser generator anyway. You have to use
this notation for types to avoid that: `type(\start(sort("MySort")), ())` to avoid the computation for `#start[A]`
}
java &U (type[&U] nonterminal, value input, loc origin) loadParsers(loc savedParsers, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={});

@synopsis{Load a previously serialized parser, for a specific non-terminal, from disk for usage}
@description{
This loader behaves just like ((loadParsers)), except that the resulting parser function is already
bound to a specific non-terminal. 
}
@javaClass{org.rascalmpl.library.Prelude}
java &U (value input, loc origin) loadParser(type[&U] nonterminal, loc savedParsers, bool allowAmbiguity=false, bool allowRecovery=false, bool hasSideEffects=false, set[Tree(Tree)] filters={});

@synopsis{Yield the string of characters that form the leafs of the given parse tree.}
@description{
`unparse` is the inverse function of ((ParseTree-parse)), i.e., for every syntactically correct string `TXT` of
type `S`, the following holds:
```rascal
unparse(parse(#S, TXT)) == TXT
```
}
@examples{
```rascal-shell
syntax Exp = Exp "+" Exp | Number;
lexical Number = [0-9]+;
import ParseTree;
```
First parse an expression, this results in a parse tree. Then unparse this parse tree:
```rascal-shell,continue
unparse(parse(#Exp, "2+3"));
```
}
str unparse(Tree tree) = "<tree>";

@javaClass{org.rascalmpl.library.Prelude}
java str printSymbol(Symbol sym, bool withLayout);

@javaClass{org.rascalmpl.library.Prelude}

@synopsis{Implode a parse tree according to a given (ADT) type.}
@description{
Given a grammar for a language, its sentences can be parsed and the result is a parse tree
(or more precisely a value of type `Tree`). For many applications this is sufficient
and the results are achieved by traversing and matching them using concrete patterns.

In other cases, the further processing of parse trees is better done in a more abstract form.
The [abstract syntax](http://en.wikipedia.org/wiki/Abstract_syntax) for a language is a
data type that is used to represent programs in the language in an _abstract_ form.
Abstract syntax has the following properties:

*  It is "abstract" in the sense that it does not contain textual details such as parentheses,
  layout, and the like.
*  While a language has one grammar (also known as, _concrete syntax_) it may have several abstract syntaxes
  for different purposes: type analysis, code generation, etc.


The function `implode` bridges the gap between parse tree and abstract syntax tree.
Given a parse tree and a Rascal type it traverses them simultaneously and constructs
an abstract syntax tree (a value of the given type) as follows:

*  Literals, layout and empty (i.e. ()) nodes are skipped.

*  Regular */+ lists are imploded to `list`s or `set`s depending on what is 
  expected in the ADT.

*  Ambiguities are imploded to `set`s.

*  If the expected type is `str` the tree is unparsed into a string. This happens for both 
  lexical and context-free parse trees.

*  If a tree's production has no label and a single AST (i.e. non-layout, non-literal) argument
  (for instance, an injection), the tree node is skipped, and implosion continues 
  with the lone argument. The same applies to bracket productions, even if they
  are labeled.

*  If a tree's production has no label, but more than one argument, the tree is imploded 
  to a tuple (provided this conforms to the ADT).

*  Optionals are imploded to booleans if this is expected in the ADT.
  This also works for optional literals, as shown in the example below.

*  An optional is imploded to a list with zero or one argument, iff a list
  type is expected.

*  If the argument of an optional tree has a production with no label, containing
  a single list, then this list is spliced into the optional list.

*  For trees with (cons-)labeled productions, the corresponding constructor
  in the ADT corresponding to the non-terminal of the production is found in
  order to make the AST.
  
*  If the provided type is `node`, (cons-)labeled trees will be imploded to untyped `node`s.
  This means that any subtrees below it will be untyped nodes (if there is a label), tuples of 
  nodes (if a label is absent), and strings for lexicals. 

*  Unlabeled lexicals are imploded to str, int, real, bool depending on the expected type in
  the ADT. To implode lexical into types other than str, the PDB parse functions for 
  integers and doubles are used. Boolean lexicals should match "true" or "false". 
  NB: lexicals are imploded this way, even if they are ambiguous.

*  If a lexical tree has a cons label, the tree imploded to a constructor with that name
  and a single string-valued argument containing the tree's yield.


An `IllegalArgument` exception is thrown if during implosion a tree is encountered that cannot be
imploded to the expected type in the ADT. As explained above, this function assumes that the
ADT type names correspond to syntax non-terminal names, and constructor names correspond 
to production labels. Labels of production arguments do not have to match with labels
 in ADT constructors.

Finally, source location fields are propagated as keyword fields on constructor ASTs. 
To access them, the user is required to explicitly declare a keyword field on all
ADTs used in implosion. In other words, for every ADT type `T`, add:

```rascal-commands
data T(loc location=|unknown:///|);
```
}
@examples{
Here are some examples for the above rules.

* Example for rule 5. Given the grammar
```rascal
syntax IDTYPE = Id ":" Type;
syntax Decls = decls: "declare" {IDTYPE ","}* ";";
```
`Decls` will be imploded as:
```rascal
data Decls = decls(list[tuple[str,Type]]);
```
(assuming Id is a lexical non-terminal).   
* Example for rule 6. Given the grammar
```rascal
syntax Formal = formal: "VAR"? {Id ","}+ ":" Type;
```
The corresponding ADT could be:
```rascal
data Formal = formal(bool, list[str], Type);
```
* Example for rule 8. Given the grammar
```rascal
syntax Tag = "[" {Modifier ","}* "]";
syntax Decl = decl: Tag? Signature Body;
```
In this case, a `Decl` is imploded into the following ADT:
```rascal
data Decl = decl(list[Modifier], Signature, Body);  
```
* Example for rule 9. Given the grammar
```rascal
syntax Exp = left add: Exp "+" Exp;
```
Can be imploded into:
```rascal
data Exp = add(Exp, Exp);
```
}
java &T<:value implode(type[&T<:value] t, Tree tree);


@synopsis{Annotate a parse tree node with an (error) message.}
anno Message Tree@message;


@synopsis{Annotate a parse tree node with a list of (error) messages.}
anno set[Message] Tree@messages;


@synopsis{Annotate a parse tree node with a documentation string.}
anno str Tree@doc;


@synopsis{Annotate a parse tree node with documentation strings for several locations.}
anno map[loc,str] Tree@docs;



@synopsis{Annotate a parse tree node with the target of a reference.}
anno loc Tree@link;


@synopsis{Annotate a parse tree node with multiple targets for a reference.}
anno set[loc] Tree@links;


@synopsis{Annotate the top of the tree with hyperlinks between entities in the tree (or other trees)

This is similar to link and links annotations, except that you can put it as one set at the top of the tree.}
anno rel[loc,loc] Tree@hyperlinks;


@synopsis{Tree search result type for ((treeAt)).}
data TreeSearchResult[&T<:Tree] = treeFound(&T tree) | treeNotFound();



@synopsis{Select the innermost Tree of a given type which is enclosed by a given location.}
@description{

}
TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, Tree a:appl(_, _)) {
	if ((a@\loc)?, al := a@\loc, al.offset <= l.offset, al.offset + al.length >= l.offset + l.length) {
		for (arg <- a.args, TreeSearchResult[&T<:Tree] r:treeFound(&T<:Tree _) := treeAt(t, l, arg)) {
			return r;
		}
		
		if (&T<:Tree tree := a) {
			return treeFound(tree);
		}
	}
	return treeNotFound();
}

default TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, Tree root) = treeNotFound();

bool sameType(label(_,Symbol s),Symbol t) = sameType(s,t);
bool sameType(Symbol s,label(_,Symbol t)) = sameType(s,t);
bool sameType(Symbol s,conditional(Symbol t,_)) = sameType(s,t);
bool sameType(conditional(Symbol s,_), Symbol t) = sameType(s,t);
bool sameType(Symbol s, s) = true;
default bool sameType(Symbol s, Symbol t) = false;


@synopsis{Determine if the given type is a non-terminal type.}
bool isNonTerminalType(Symbol::\sort(str _)) = true;
bool isNonTerminalType(Symbol::\lex(str _)) = true;
bool isNonTerminalType(Symbol::\layouts(str _)) = true;
bool isNonTerminalType(Symbol::\keywords(str _)) = true;
bool isNonTerminalType(Symbol::\parameterized-sort(str _, list[Symbol] _)) = true;
bool isNonTerminalType(Symbol::\parameterized-lex(str _, list[Symbol] _)) = true;
bool isNonTerminalType(Symbol::\start(Symbol s)) = isNonTerminalType(s);
default bool isNonTerminalType(Symbol s) = false;
