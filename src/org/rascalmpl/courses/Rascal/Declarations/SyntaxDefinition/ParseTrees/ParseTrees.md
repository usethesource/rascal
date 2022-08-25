# Parse Trees

.Synopsis
An algebraic data-type for parse trees; produced by all parsers generated from syntax definitions.

.Syntax

.Types

.Function

.Details

.Description

Below is the full definition of `Tree` and `Production` and `Symbol`. A parse tree is a nested tree structure of type `Tree`. 

*  Most internal nodes are applications (`appl`) of a `Production` to a list of children `Tree` nodes. `Production` is the abstract representation of a [SyntaxDefinition] rule, which consists of a definition of an alternative for a `Symbol` by a list of `Symbols`.
*  The leaves of a parse tree are always
characters (`char`), which have an integer index in the UTF8 table. 

*  Some internal nodes encode ambiguity (`amb`) by pointing to a set of 
alternative `Tree` nodes.


The `Production` and `Symbol` types are an abstract notation for rules in ((Syntax Definition))s, while the `Tree` type is the actual notation
for parse trees. 

Parse trees are called parse forests when they contain `amb` nodes.

You can analyze and manipulate parse trees in three ways:

*  Directly on the `Tree` level, just like any other ((Algebraic Data Type))
*  Using ((Concrete Syntax))
*  Using ((Action))s


The type of a parse tree is the symbol that it's production produces, i.e. `appl(prod(sort("A"),[],{}),[])` has type `A`. Ambiguity nodes 
Each such a non-terminal type has `Tree` as its immediate super-type.
                
.Examples

[source,rascal]
----
// the following definition
syntax A = "a";
// would make the following [Test] succeed:
test a() = parse(#A,"a") ==  
appl(prod(
    sort("A"), 
    [lit("a")], 
    {}),
  [appl(
      prod(
        lit("a"),
        [\char-class([range(97,97)])],
        {}),
      [char(97)])]);
// you see that the defined non-terminal A ends up as the production for the outermost node. As the only child is the tree for recognizing the literal a, which is defined to be a single a from the character-class [ a ].
----

[source,rascal]
----
// when we use labels in the definitions, they also end up in the trees:
// the following definition
lexical A = myA:"a" B bLabel;
lexical B = myB:"b";
// would make the following [Test] succeed:
test a() = parse(#A,"ab") == appl(prod(label("myA",lex("A")),[lit("a"),sort("bLabel",lex("B"))],{}),[appl(prod(lit("a"),[\char-class([range(97,97)]),[char(97)]),appl(prod(label("myB", lex("B"),[lit("b")],{}),[appl(prod(lit("b"),[\char-class([range(98,98)]),[char(98)])]) ]);
// here you see that the alternative name is a label around the first argument of `prod` while argument labels become labels in the list of children of a `prod`.
----
.Benefits

.Pitfalls

