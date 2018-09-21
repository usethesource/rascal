module lang::rascalcore::check::Test6

extend Type;
extend Message;
//extend List;

@doc{
.Synopsis
The Tree data type as produced by the parser.

.Description

A `Tree` defines the trees normally found after parsing; additional constructors exist for execptional cases:

<1> Parse tree constructor when parse succeeded.
<2> Cyclic parsetree.
<3> Ambiguous subtree.
<4> A single character. 
}

data Tree 
     = appl(Production prod, list[Tree] args) // <1>
     | cycle(Symbol symbol, int cycleLength)  // <2>
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
<7> `reference` means a reference to another production rule which should be substituted there,
    for extending priority chains and such.
} 
data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) // <1>
     | regular(Symbol def) // <2>
     | error(Production prod, int dot) // <3>
     | skipped() // <4>
     ;
     
data Production 
     = \priority(Symbol def, list[Production] choices) // <5>
     | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives) // <6>
     | \reference(Symbol def, str cons) // <7>
     ;

@doc{
.Synopsis
Attributes in productions.

.Description

An `Attr` (attribute) documents additional semantics of a production rule. Neither tags nor
brackets are processed by the parser generator. Rather downstream processors are
activated by these. Associativity is a parser generator feature though. 
}
data Attr = \bracket() | /*deprecated*/ \assoc(Associativity \assoc);

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

public bool subtype(Symbol::\sort(_), Symbol::\adt("Tree", _)) = true;

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

@doc{
.Synopsis
Nested priority is flattened.
}
public Production priority(Symbol s, [*Production a, priority(Symbol t, list[Production] b), *Production c])
  = priority(s,a+b+c);
   
@doc{
.Synopsis
Normalization of associativity.

.Description

* Choice (see the `choice` constructor in <<Type-ParseTree>>) under associativity is flattened.
* Nested (equal) associativity is flattened.
* Priority under an associativity group defaults to choice.
}
Production associativity(Symbol s, Associativity as, {*Production a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
            
Production associativity(Symbol rhs, Associativity a, {associativity(rhs, Associativity b, set[Production] alts), *Production rest})  
  = associativity(rhs, a, rest + alts); // the nested associativity, even if contradictory, is lost

Production associativity(Symbol s, Associativity as, {*Production a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, {*a, *b}); 

// deprecated; remove after bootstrap
Production associativity(Symbol rhs, Associativity a, set[Production] rest)
  = associativity(rhs, a, withAssoc + withNewAssocs)
  when  withoutAssoc := {p | Production p:prod(_,_,_) <- rest, !(\assoc(_) <- p.attributes)},
        withoutAssoc != {},
        withAssoc := rest - withoutAssoc,
        withNewAssocs := {p[attributes = p.attributes + {\assoc(a)}] | Production p <- withoutAssoc}
        ;
        
@doc{
.Synopsis
Annotate a parse tree node with a source location.
}
anno loc Tree@\loc;

@doc{
.Synopsis
Parse input text (from a string or a location) and return a parse tree.

.Description

*  Parse a string and return a parse tree.
*  Parse a string and return a parse tree, `origin` defines the original location of the input.
*  Parse the contents of resource input and return a parse tree.

The parse either throws ParseError exceptions or returns parse trees of type `Tree`. See [[ParseTree]].

The `allowAmbiguity` flag dictates the behavior of the parser in the case of ambiguity. When `allowAmbiguity=true` 
the parser will construct ambiguity clusters (local sets of parse trees where the input string is ambiguous). If it is `false`
the parser will throw an `Ambiguous` exception instead which is comparable to a ParseError exception. The latter option terminates faster.

The `hasSideEffects` flag is normally set to false. When a [[SyntaxDefinition]] uses side-effects to filter ambiguity, this 
option must be set to `true` to ensure correct behavior. Otherwise the parser employs optimizations which assume the parse tree construction
algorithm is context-free. When filter functions associated with syntax definitions exist that use global variables, for example to store type definitions 
in a symbol table , then this option must be set to `true`. 

.Examples
[source,rascal-shell,error]
----
import demo::lang::Exp::Concrete::NoLayout::Syntax;
import ParseTree;
----
Seeing that `parse` returns a parse tree:
[source,rascal-shell,continue,error]
----
parse(#Exp, "2+3");
----
Catching a parse error:
[source,rascal-shell,continue,error]
----
import IO;
try {
  Exp e = parse(#Exp, "2@3");
}
catch ParseError(loc l): {
  println("I found a parse error at line <l.begin.line>, column <l.begin.column>");
}
----
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input, bool allowAmbiguity=false, bool hasSideEffects=false);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin, bool allowAmbiguity=false, bool hasSideEffects=false);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, loc input, bool allowAmbiguity=false, bool hasSideEffects=false);


@doc{
.Synopsis parse the input but instead of returning the entire tree, return the trees for the first ambiguous substring.

.Description

This function is similar to the [[parse]] function in its functionality. However, in case of serious ambiguity parse
could be very slow. This function is much faster, because it does not try to construct an entire forest and thus avoids
the cost of constructing nested ambiguity clusters. 

If the input sentence is not ambiguous after all, simply the entire tree is returned.
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java Tree firstAmbiguity(type[&T<:Tree] begin, str input);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses information about syntax definitions at call site}
public java Tree firstAmbiguity(type[&T<:Tree] begin, loc input);


@doc{
.Synopsis
Yield the string of characters that form the leafs of the given parse tree.

.Description
`unparse` is the inverse function of <<ParseTree-parse>>, i.e., for every syntactically correct string _TXT_ of
type `S`, the following holds:
[source,rascal,subs="quotes"]
----
unparse(parse(#S, _TXT_)) == _TXT_
----

.Examples
[source,rascal-shell]
----
import demo::lang::Exp::Concrete::NoLayout::Syntax;
import ParseTree;
----
First parse an expression, this results in a parse tree. Then unparse this parse tree:
[source,rascal-shell,continue]
----
unparse(parse(#Exp, "2+3"));
----
}
@javaClass{org.rascalmpl.library.Prelude}
public java str unparse(Tree tree);

@javaClass{org.rascalmpl.library.Prelude}
public java str printSymbol(Symbol sym, bool withLayout);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses Evaluator to create constructors in the caller scope (to fire rewrite rules).}
@doc{
.Synopsis
Implode a parse tree according to a given (ADT) type.

.Description

Given a grammar for a language, its sentences can be parsed and the result is a parse tree
(or more precisely a value of type `Tree`). For many applications this is sufficient
and the results are achieved by traversing and matching them using concrete patterns.

In other cases, the further processing of parse trees is better done in a more abstract form.
The http://en.wikipedia.org/wiki/Abstract_syntax[abstract syntax] for a language is a
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

Finally, source location annotations are propagated as annotations on constructor ASTs. 
To access them, the user is required to explicitly declare a location annotation on all
ADTs used in implosion. In other words, for every ADT type `T`, add:

[source,rascal]
----
anno loc T@location;
----

.Examples
Here are some examples for the above rules.

.Example for rule 5

Given the grammar
[source,rascal]
----
syntax IDTYPE = Id ":" Type;
syntax Decls = decls: "declare" {IDTYPE ","}* ";";
----
    
`Decls` will be imploded as:
[source,rascal]
----
data Decls = decls(list[tuple[str,Type]]);
----
(assuming Id is a lexical non-terminal).   

.Example for rule 6

Given the grammar
[source,rascal]
----
syntax Formal = formal: "VAR"? {Id ","}+ ":" Type;
----
The corresponding ADT could be:
[source,rascal]
----
data Formal = formal(bool, list[str], Type);
----

.Example for rule 8

Given the grammar
[source,rascal]
----
syntax Tag = "[" {Modifier ","}* "]";
syntax Decl = decl: Tag? Signature Body;
----
In this case, a `Decl` is imploded into the following ADT:
[source,rascal]
----
data Decl = decl(list[Modifier], Signature, Body);  
----

.Example for rule 9

Given the grammar
[source,rascal]
----
syntax Exp = left add: Exp "+" Exp;
----
Can be imploded into:
[source,rascal]
----
data Exp = add(Exp, Exp);
----
}
public java &T<:value implode(type[&T<:value] t, Tree tree);

@doc{
.Synopsis
Annotate a parse tree node with an (error) message.
}
public anno Message Tree@message;

@doc{
.Synopsis
Annotate a parse tree node with a list of (error) messages.
}
public anno set[Message] Tree@messages;

@doc{
.Synopsis
Annotate a parse tree node with a documentation string.
}
anno str Tree@doc;

@doc{
.Synopsis
Annotate a parse tree node with documentation strings for several locations.

}
anno map[loc,str] Tree@docs;


@doc{
.Synopsis
Annotate a parse tree node with the target of a reference.
}
anno loc Tree@link;

@doc{
.Synopsis
Annotate a parse tree node with multiple targets for a reference.
}
anno set[loc] Tree@links;

@doc{
.Synopsis
Annotate the top of the tree with hyperlinks between entities in the tree (or other trees)

This is similar to link and links annotations, except that you can put it as one set at the top of the tree.
}
anno rel[loc,loc] Tree@hyperlinks;

@doc{
.Synopsis
Tree search result type for <<treeAt>>.
}
public data TreeSearchResult[&T<:Tree] = treeFound(&T tree) | treeNotFound();


@doc{
.Synopsis
Select the innermost Tree of a given type which is enclosed by a given location.

.Description
}
public TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, Tree a:appl(_, _)) {
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

public default TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, Tree root) = treeNotFound();

public bool sameType(label(_,Symbol s),Symbol t) = sameType(s,t);
public bool sameType(Symbol s,label(_,Symbol t)) = sameType(s,t);
public bool sameType(Symbol s,conditional(Symbol t,_)) = sameType(s,t);
public bool sameType(conditional(Symbol s,_), Symbol t) = sameType(s,t);
public bool sameType(Symbol s, s) = true;
public default bool sameType(Symbol s, Symbol t) = false;

@doc{
.Synopsis
Determine if the given type is a non-terminal type.
}
public bool isNonTerminalType(Symbol::\sort(str _)) = true;
public bool isNonTerminalType(Symbol::\lex(str _)) = true;
public bool isNonTerminalType(Symbol::\layouts(str _)) = true;
public bool isNonTerminalType(Symbol::\keywords(str _)) = true;
public bool isNonTerminalType(Symbol::\parameterized-sort(str _, list[Symbol] _)) = true;
public bool isNonTerminalType(Symbol::\parameterized-lex(str _, list[Symbol] _)) = true;
public bool isNonTerminalType(Symbol::\start(Symbol s)) = isNonTerminalType(s);
public default bool isNonTerminalType(Symbol s) = false;