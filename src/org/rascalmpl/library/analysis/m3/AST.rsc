@doc{
#### Synopsis

a symbolic representation for abstract syntax trees of programming languages.

#### Description

We provide a general set of data types for the syntactic constructs of programming languages: `Expression`, `Statement`, `Declaration` and `Type`.
Also, very common syntactic constructs are added to this, such as `if`, `while`, etc.

The idea is that parsers for different languages will map to common abstract syntax elements, when this can be done meaningfully.
If not, then these front-ends will extend the existing types with new constructor definitions, or even new kinds of types will
be added. The shared representation limits the element of surprise when working with different languages, and perhaps may
make some downstream analyses reusable.

The concept of a source ((Location)) is important for abstract syntax trees. The annotation `src` will always point to value of type `loc`,
pointing to the physical location of the construct in the source code.

The concept of _declaration_ is also relevant. A `decl` annotation points from a use of a concept to its definition, but always
via an indirection (i.e. fully qualified name). The `decl` annotation is also of type `loc`, where each ((Expressions-Values-Location)) is a fully qualified name of the
definition that is used. 

Finally, the concept of a _type_ is relevant for ASTs. In particular an `Expression` may have a `typ` annotation, or
a variable declaration, etc.

#### Benefits

*  Symbolic abstract syntax trees can be analyzed and transformed easily using Rascal primitives such as ((Patterns)), ((Expressions-Comprehensions)), and ((Statements-Visit)).

#### Pitfalls

*  Even though different languages may map to the same syntactic construct, this does not mean that the semantics is the same. Downstream
metrics or other analysis tools should still take semantic differences between programming languages into account. 
}
module analysis::m3::AST

import Message;
import Node;
import analysis::m3::TypeSymbol;

data \AST(loc file = |unknown:///|)
  = declaration(Declaration declaration)
  | lines(list[str] contents)
  | noAST(Message msg)
  ;
  
loc unknownSource = |unknown:///|;
loc unresolvedDecl = |unresolved:///|;
loc unresolvedType = |unresolved:///|;  

data Declaration(
	loc src = |unknown:///|,
	loc decl = |unresolved:///|, //unresolvedDecl
	TypeSymbol typ = \any(),
	list[Modifier] modifiers = [],
	list[Message] messages = []
);

data Statement(
	loc src = |unknown:///|,
	loc decl = |unresolved:///| //unresolvedDecl
);

data Expression(
	loc src = |unknown:///|,
	loc decl = |unresolved:///|, //unresolvedDecl,
	TypeSymbol typ = \any()
);

data Type(
	loc name = |unresolved:///|, //unresolvedType,              
	TypeSymbol typ = \any()
);

data Modifier;

@synopsis{Test for the consistency characteristics of an M3 annotated abstract syntax tree}
bool astNodeSpecification(node n, str language = "java", bool checkNameResolution=false, bool checkSourceLocation=true) {
	// get a loc from any node if there is any.
	loc  pos(node y) = (loc f := (y.src?|unknown:///|(0,0))) ? f : |unknown:///|(0,0);
	loc  decl(node y) = (loc d := y.decl?|unresolved:///|) ? d : |unresolved:///|;
	int  begin(node y) = begin(pos(y));
	int  end(node y) = end(pos(y));
	int  begin(loc l) = l.offset;
	int  end(loc l) = l.offset + l.length;
	bool leftToRight(loc l, loc r) = end(l) <= begin(r);
	bool leftToRight(node a, node b) = leftToRight(pos(a), pos(b));

	if (checkSourceLocation) {
		// all nodes have src annotations
		assert all(/node x := n, x.src?);

		// siblings are sorted in the input, even if some of them are lists
		assert all(/node x := n, [*_, node a, node b, *_] := getChildren(x), leftToRight(a,b));
		assert all(/node x := n, [*_, node a, [node b, *_], *_] := getChildren(x), leftToRight(a,b));
		assert all(/node x := n, [*_, [*_, node a], node b, *_] := getChildren(x), leftToRight(a,b));
		assert all(/node x := n, [*_, [*_, node a], [node b, *_], *_] := getChildren(x), leftToRight(a,b));
		assert all(/[*_, node a, node b, *_] := n, leftToRight(a,b));

		// children positions are included in the parent input scope
		assert all(/node parent := n, /node child := parent, begin(parent) <= begin(child), end(child) <= end(parent));
	}
	
	if (checkNameResolution) {
		// all resolved names have the language as schema prefix
		assert all(/node m := n, m.decl?, /^<language>/ := decl(m).scheme);
	}
	
	return true;
}




