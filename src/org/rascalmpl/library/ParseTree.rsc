@license{
  Copyright (c) 2009-2011 CWI
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

@doc{
Synopsis: Library functions for parse trees.

Description:

A _concrete syntax tree_ or [parse tree](http://en.wikipedia.org/wiki/Parse_tree) is an ordered, rooted tree that 
represents the syntactic structure of a string according to some formal grammar. In Rascal parse trees, the interior nodes are labeled by rules of the grammar, while the leaf nodes are labeled by terminals (characters) of the grammar. 

`Tree` is the universal parse tree data type in Rascal and can be used to represent parse trees for any language. 
* `Tree` is a subtype of the type [$Values/Node].
* All [SyntaxDefinition] types (non-terminals) are sub-types of `Tree`
* All [ConcreteSyntax] expressions produce parse trees the types of which are non-terminals
* Trees can be annotated in various ways, see [IDEConstruction] features. Most importantly the `\loc` annotation always points to the source location of any (sub) parse tree.


Parse trees are usually analyzed and constructed using [ConcreteSyntax] expressions and patterns.
 
_Advanced users_ may want to create tools that analyze any parse tree, regardless of the [SyntaxDefinition] that generated it, you can manipulate them on the abstract level.

In [$ParseTree/Tree] is the full definition of `Tree`, `Production` and `Symbol`. A parse tree is a nested tree structure of type `Tree`. 
   * Most internal nodes are applications (`appl`) of a `Production` to a list of children `Tree` nodes. `Production` is the abstract representation of a [SyntaxDefinition] rule, which consists of a definition of an alternative for a `Symbol` by a list of `Symbols`.
   * The leaves of a parse tree are always
characters (`char`), which have an integer index in the UTF8 table. 
   * Some internal nodes encode ambiguity (`amb`) by pointing to a set of 
alternative `Tree` nodes.


The `Production` and `Symbol` types are an abstract notation for rules in [SyntaxDefinition]s, while the `Tree` type is the actual notation
for parse trees. 

Parse trees are called parse forests when they contain `amb` nodes.

You can analyze and manipulate parse trees in three ways:
   * Directly on the `Tree` level, just like any other [AlgebraicDataType]
   * Using [ConcreteSyntax]
   * Using [Action]s


The type of a parse tree is the symbol that it's production produces, i.e. `appl(prod(sort("A"),[],{}),[])` has type `A`. Ambiguity nodes 
Each such a non-terminal type has `Tree` as its immediate super-type.

The `ParseTree` library provides:
<toc Rascal/Libraries/Prelude/ParseTree 1>

Examples:

<listing>
// the following definition
syntax A = "a";
// would make the following [Test] succeed:
test a() = parse(#A,"a") == appl(prod(sort("A"),[lit("a")],{}),[appl(prod(lit("a"),[\char-class([range(97,97)]),[char(97)])]);
// you see that the defined non-terminal A ends up as the production for the outermost node. As the only child is the tree for recognizing the literal a, which is defined to be a single a from the character-class [ a ].
</listing>

<listing>
// when we use labels in the definitions, they also end up in the trees:
// the following definition
lexical A = myA:"a" B bLabel;
lexical B = myB:"b";
// would make the following [Test] succeed:
test a() = parse(#A,"ab") == appl(prod(label("myA",lex("A")),[lit("a"),sort("bLabel",lex("B"))],{}),[appl(prod(lit("a"),[\char-class([range(97,97)]),[char(97)]),appl(prod(label("myB", lex("B"),[lit("b")],{}),[appl(prod(lit("b"),[\char-class([range(98,98)]),[char(98)])]) ]);
// here you see that the alternative name is a label around the first argument of `prod` while argument labels become labels in the list of children of a `prod`.
</listing>

Examples:

Benefits:

Pitfalls:
For historical reasons the name of the annotation is "loc" and this interferes with the Rascal keyword `loc`
for the type of [Location]s.
Therefore the annotation name has to be escaped as `\loc` when it is declared or used.

Questions:


}

module ParseTree

extend Type;
import Message;

@doc{
Synopsis: The Tree data type as produced by the parser.
Description:
* A `Tree` defines the trees normally found after parsing (/*1*/) and additional constructors (/*2*/) that are used in error trees.
* A `Production` (/*3*/) is a rule of a grammar, with a defined non-terminal, a list
        of terminal and non-terminal symbols and a possibly empty set of attributes.
* An `Attr` (attribute, /*4*/) documents additional semantics of a production rule. Neither tags nor
        brackets are processed by the parser generator. Rather downstream processors are
        activated by these. Associativity is a parser generator feature though. 
        
* `Associativity` (/*5*/) defines the kinds of associativity.
* `CharRange` (/*6*/) defines a range of characters.
* A `CharClass` (/*7*/) consists of a list of characters ranges.
* The `start` symbol (/*8*/) wraps any symbol to indicate that it is a start symbol of the grammar and
        may occur at the root of a parse tree.
* `Symbol` defines non-terminals (/*9*/), terminals (/*10*/) and regular expressions (/*11*/).
* The `Conditional` wrapper (/*12*/) adds conditions to the existence of an instance of a symbol.
* A `Condition` (/*13*/) on a symbol gives rise to a disambiguation filter.
}

data Tree 
     = appl(Production prod, list[Tree] args) /*1*/
     | cycle(Symbol symbol, int cycleLength) 
     | amb(set[Tree] alternatives)  
     | char(int character)
     ;

data Production 
     = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) /*3*/
     | regular(Symbol def)
     | error(Production prod, int dot)
     | skipped()
     ;
     
data Attr 
     = \assoc(Associativity \assoc) /*4*/
     | \bracket() 
     ;
data Associativity 
     = \left() /*5*/
     | \right() 
     | \assoc() 
     | \non-assoc()
     ;

data CharRange = range(int begin, int end) /*6*/;

alias CharClass = list[CharRange] /*7*/;


data Symbol = \start(Symbol symbol) /*8*/;

// These symbols are the named non-terminals.
data Symbol 
     = \sort(str name) /*9*/  
     | \lex(str name) 
     | \layouts(str name) 
     | \keywords(str name)
     | \parameterized-sort(str name, list[Symbol] parameters)  
     | \parameter(str name)
     ; 

// These are the terminal symbols.
data Symbol 
     = \lit(str string) /*10*/
     | \cilit(str string)
     | \char-class(list[CharRange] ranges)
     ;
    
// These are the regular expressions.
data Symbol
     = \empty() /*11*/
     | \opt(Symbol symbol)  
     | \iter(Symbol symbol)   
     | \iter-star(Symbol symbol)   
     | \iter-seps(Symbol symbol, list[Symbol] separators)   
     | \iter-star-seps(Symbol symbol, list[Symbol] separators) 
     | \alt(set[Symbol] alternatives)
     | \seq(list[Symbol] sequence)
     ;
  

data Symbol = \conditional(Symbol symbol, set[Condition] conditions) /*12*/;


data Condition
     = \follow(Symbol symbol) /*13*/
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
Here we extend productions with basic combinators allowing to
construct ordered and un-ordered compositions, and associativity groups.

The intended semantics are that 
  * 'choice' means unordered choice (defined in |Type|)
  * 'priority'  means ordered choice, where alternatives are tried from left to right,
  * 'assoc'  means all alternatives are acceptable, but nested on the declared side
  * 'others' means '...', which is substituted for a choice among the other definitions
  * 'reference' means a reference to another production rule which should be substituted there,
                for extending priority chains and such.
} 
data Production 
  = \priority(Symbol def, list[Production] choices)
  | \associativity(Symbol def, Associativity \assoc, set[Production] alternatives)
  | \others(Symbol def)
  | \reference(Symbol def, str cons)
  ;

@doc{Nested priority is flattened}
public Production priority(Symbol s, [list[Production] a, priority(Symbol t, list[Production] b),list[Production] c])
  = priority(s,a+b+c);
   
@doc{Choice under associativity is flattened}
public Production associativity(Symbol s, Associativity as, {set[Production] a, choice(Symbol t, set[Production] b)}) 
  = associativity(s, as, a+b); 
  
@doc{Nested (equal) associativity is flattened}             
public Production associativity(Symbol rhs, Associativity a, {associativity(Symbol rhs2, Associativity b, set[Production] alts), set[Production] rest}) {
  if (a == b)  
    return associativity(rhs, a, rest + alts) ;
  else
    fail;
}

public Production associativity(Symbol rhs, Associativity a, {prod(Symbol rhs, list[Symbol] lhs, set[Attr] as), set[Production] rest}) {
  if (!(\assoc(_) <- as)) 
    return \associativity(rhs, a, rest + {prod(rhs, lhs, as + {\assoc(a)})});
  else fail;
}

@doc{Priority under an associativity group defaults to choice}
public Production associativity(Symbol s, Associativity as, {set[Production] a, priority(Symbol t, list[Production] b)}) 
  = associativity(s, as, a + { e | e <- b}); 
             
@doc{
Synopsis: Annotate a parse tree node with a source location.
}
anno loc Tree@\loc;

@doc{
Synopsis: Parse input text (from a string or a location) and return a parse tree.

Description:
# Parse a string and return a parse tree.
# Parse a string and return a parse tree, `origin` defines the original location of the input.
# Parse the contents of resource input and return a parse tree.

Examples:
<screen errors>
import demo::lang::Exp::Concrete::NoLayout::Syntax;
import ParseTree;
// Seeing that `parse` returns a parse tree:
parse(#Exp, "2+3");
// Catching a parse error:
import IO;
try {
  Exp e = parse(#Exp, "2@3");
}
catch ParseError(loc l): {
  println("I found a parse error at line <l.begin.line>, column <l.begin.column>");
}
</screen>
}
@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input);

@experimental
@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, map[Production robust, CharClass lookaheads] recovery, str input, loc origin);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, loc input);

@doc{
Synopsis: Yield the string of characters that form the leafs of the given parse tree.

Description:
`unparse` is the inverse function of [parse], i.e., for every syntactically correct string $TXT$ of
type `S`, the following holds:
<listing>
unparse(parse(#S, $TXT$)) == $TXT$
</listing>

Examples:
<screen>
import demo::lang::Exp::Concrete::NoLayout::Syntax;
import ParseTree;
// First parse an expression, this results in a parse tree. Then unparse this parse tree:
unparse(parse(#Exp, "2+3"));

}
@javaClass{org.rascalmpl.library.Prelude}
public java str unparse(Tree tree);

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses Evaluator to create constructors in the caller scope (to fire rewrite rules).}
@doc{
Synopsis: Implode a parse tree according to a given ADT.

Description:

Given a grammar for a language, its sentences can be parsed and the result is a parse tree
(or more precisely a value of type `Tree`). For many applications this is sufficient
and the results are achieved by traversing and matching them using concrete patterns.

In other cases, the further processing of parse trees is better done in a more abstract form.
The [abstract syntax](http://en.wikipedia.org/wiki/Abstract_syntax) for a language is a
data type that is used to represent programs in the language in an _abstract_ form.
Abstract syntax has the following properties:
* It is "abstract" in the sense that it does not contain textual details such as parentheses,
  layout, and the like.
* While a language has one grammar (also known as, _concrete syntax_) it may have several abstract syntaxes
  for different purposes: type analysis, code generation, etc.


The function `implode` bridges the gap between parse tree and abstract syntax tree.
Given a parse tree and the type of an ADT it traverses them simultaneously and constructs
an abstract syntax tree (a value of the given ADT type) as follows:

# Literals, layout and empty (i.e. ()) nodes are skipped.

# Regular */+ lists are imploded to `list`s or `set`s depending on what is 
  expected in the ADT.

# Ambiguities are imploded to `set`s.

# If a tree's production has no label and a single AST (i.e. non-layout, non-literal) argument
  (for instance, an injection), the tree node is skipped, and implosion continues 
  with the lone argument. The same applies to bracket productions, even if they
  are labeled.

# If a tree's production has no label, but more than one argument, the tree is imploded 
  to a tuple (provided this conforms to the ADT).

# Optionals are imploded to booleans if this is expected in the ADT.
  This also works for optional literals, as shown in the example below.

# An optional is imploded to a list with zero or one argument, iff a list
  type is expected.

# If the argument of an optional tree has a production with no label, containing
  a single list, then this list is spliced into the optional list.

# For trees with (cons-)labeled productions, the corresponding constructor
  in the ADT corresponding to the non-terminal of the production is found in
  order to make the AST.

# Unlabeled lexicals are imploded to str, int, real, bool depending on the expected type in
  the ADT. To implode lexical into types other than str, the PDB parse functions for 
  integers and doubles are used. Boolean lexicals should match "true" or "false". 
  NB: lexicals are imploded this way, even if they are ambiguous.

# If a lexical tree has a cons label, the tree imploded to a constructor with that name
  and a single string-valued argument containing the tree's yield.


An `IllegalArgument` exception is thrown if during implosion a tree is encountered that cannot be
imploded to the expected type in the ADT. As explained above, this function assumes that the
ADT type names correspond to syntax non-terminal names, and constructor names correspond 
to production labels. Labels of production arguments do not have to match with labels
 in ADT constructors.

Finally, source location annotations are propagated as annotations on constructor ASTs. 
To access them, the user is required to explicitly declare a location annotation on all
ADTs used in implosion. In other words, for every ADT type `T`, add:

<listing>
anno loc T@location;
</listing>

Examples:

==Example for rule 5==
Given the grammar
<listing>
syntax IDTYPE = Id ":" Type;
syntax Decls = decls: "declare" {IDTYPE ","}* ";";
</listing>
    
`Decls` will be imploded as:
<listing>
data Decls = decls(list[tuple[str,Type]]);
</listing>
(assuming Id is a lexical non-terminal).   

==Example for rule 6==
Given the grammar
<listing>
syntax Formal = formal: "VAR"? {Id ","}+ ":" Type;
</listing>
The corresponding ADT could be:
<listing>
data Formal = formal(bool, list[str], Type);
</listing>

==Example for rule 8==
Given the grammar
<listing>
syntax Tag = "[" {Modifier ","}* "]";
syntax Decl = decl: Tag? Signature Body;
</listing>
In this case, a `Decl` is imploded into the following ADT:
<listing>
data Decl = decl(list[Modifier], Signature, Body);  
</listing>

==Example for rule 9==
Given the grammar
<listing>
syntax Exp = left add: Exp "+" Exp;
</listing>
Can be imploded into:
<listing>
data Exp = add(Exp, Exp);
</listing>
}
public java &T<:node implode(type[&T<:node] t, Tree tree);

@doc{
Synopsis: Annotate a parse tree node with an (error) message.
}
public anno Message Tree@message;

@doc{
Synopsis: Annotate a parse tree node with a list of (error) messages.
}
public anno set[Message] Tree@messages;

@doc{
Synopsis: Annotate a parse tree node with a documentation string.
}
anno str Tree@doc;

@doc{
Synopsis: Annotate a parse tree node with documentation strings for several locations.

}
anno map[loc,str] Tree@docs;


@doc{
Synopsis: Annotate a parse tree node with the target of a reference.
}
anno loc Tree@link;

@doc{
Synopsis: Annotate a parse tree node with multiple targets for a reference.
}
anno set[loc] Tree@links;

@doc{
Synopsis: Tree search result type for [treeAt].
}
public data TreeSearchResult[&T<:Tree] = treeFound(&T tree) | treeNotFound();


@doc{
Synopsis: Select the innermost Tree of a given type which is enclosed by a given location.

Description: Select the innermost Tree of type `t` which is enclosed by location `l`.
}
public TreeSearchResult[&T<:Tree] treeAt(type[&T<:Tree] t, loc l, a:appl(_, _)) {
	if ((a@\loc)?, al := a@\loc, al.offset <= l.offset, al.offset + al.length >= l.offset + l.length) {
		for (arg <- a.args, r:treeFound(_) := treeAt(t, l, arg)) {
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

