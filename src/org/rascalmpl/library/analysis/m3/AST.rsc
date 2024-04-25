
@synopsis{a symbolic representation for abstract syntax trees of programming languages.}
@description{
We provide a general set of data types for the syntactic constructs of programming languages: `Expression`, `Statement`, `Declaration` and `Type`.
Also, very common syntactic constructs are added to this, such as `if`, `while`, etc.

The idea is that parsers for different languages will map to common abstract syntax elements, when this can be done meaningfully.
If not, then these front-ends will extend the existing types with new constructor definitions, or even new kinds of types will
be added. The shared representation limits the element of surprise when working with different languages, and perhaps may
make some downstream analyses reusable.

The concept of a source location is important for abstract syntax trees. The annotation `src` will always point to value of type `loc`, pointing to the physical location of the construct in the source code.

The concept of _declaration_ is also relevant. A `decl` annotation points from a use of a concept to its definition, but always via an indirection (i.e. fully qualified name). The `decl` annotation is also of type `loc`, where each location is a fully qualified name of the definition that is used. 

Finally, the concept of a _type_ is relevant for ASTs. In particular an `Expression` may have a `typ` annotation, or a variable declaration, etc.
}
@benefits{
* Symbolic abstract syntax trees can be analyzed and transformed easily using Rascal primitives such as patterns, comprehensions and visit.
* By re-using recognizable names for different programming languages, it's easier to switch between languages to analyze.
* Some algorithms made be reusable on different programming languages, but please be aware of the _pitfalls_.
}
@pitfalls{
*  Even though different languages may map to the same syntactic construct, this does not mean that the semantics is the same. Downstream
metrics or other analysis tools should still take semantic differences between programming languages into account.
}
module analysis::m3::AST

import Message;
import Node;
import IO;
import Set;
import util::Monitor;
import analysis::m3::TypeSymbol;

@synopsis{For metric purposes we can use a true AST declaration tree, a simple list of lines for generic metrics, or the reason why we do not have an AST.}
data \AST(loc file = |unknown:///|)
  = declaration(Declaration declaration)
  | lines(list[str] contents)
  | noAST(Message msg)
  ;

@synopsis{Uniform name for everything that is declared in programming languages: variables, functions, classes, etc.}
@description{
Instances of the Declaration type represent the _syntax_ of declarations in programming languages.

| field name | description |
| ---------- | ----------- |
| `src`      | the exact source location of the declaration in a source file |
| `decl`     | the resolved fully qualified name of the artefact that is being declared here |
| `typ`      | a symbolic representation of the static type of the declared artefact here (not the syntax of the type) |
} 
data Declaration(
	loc src = |unknown:///|,
	loc decl = |unresolved:///|,
	TypeSymbol typ = unresolved()
);

@synopsis{Uniform name for everything that is typicall a _statement_ programming languages: assignment, loops, conditionals, jumps}
@description{
Instances of the Statement type represent the _syntax_ of statements in programming languages.

| field name | description |
| ---------- | ----------- |
| `src`      | the exact source location of the statement in a source file |
| `decl`     | if the statement directly represent a usage of a declared artefact, then this points to the fully qualified name of the used artifact.
}
data Statement(
	loc src = |unknown:///|,
	loc decl = |unresolved:///| 
);

@synopsis{Uniform name for everything that is an _expression_ in programming languages: arithmetic, comparisons, function invocations, ...}
@description{
Instances of the Expression type represent the _syntax_ of expressions in programming languages.

| field name | description |
| ---------- | ----------- |
| `src`      | the exact source location of the expression in a source file |
| `decl`     | if this expression represents a usage, decl is the resolved fully qualified name of the artefact that is being used here |
| `typ`      | a symbolic representation of the static type of the _result_ of the expression |
} 
data Expression(
	loc src = |unknown:///|,
	loc decl = |unresolved:///|, 
	TypeSymbol typ = \unresolved()
);

@synopsis{Uniform name for everything that is an _type_ in programming languages syntax: int, void, List<Expression>, ...}
@description{
Instances of the Type type represent the _syntax_ of types in programming languages.

| field name | description |
| ---------- | ----------- |
| `src`      | the exact source location of the expression in a source file |
| `decl`     | the fully qualified name of the type, if resolved and if well-defined |
| `typ`      | a symbolic representation of the static type that is the meaning of this type expression |
} 
data Type(
	loc src = |unknown:///|,
	loc decl = |unresolved:///|, 
	TypeSymbol typ = \unresolved()
);

@synopsis{Uniform name for everything that is a _modifier_ in programming languages syntax: public, static, final, etc.}
@description{
Instances of the Modifer type represent the _syntax_ of modifiers in programming languages.

| field name | description |
| ---------- | ----------- |
| `src`      | the exact source location of the expression in a source file |
} 
data Modifier(
	loc src = |unknown:///|
);

data Bound;

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
	bool included(node parent, node child) = begin(parent) <= begin(child) && end(child) <= end(parent);

	if (checkSourceLocation) {
		// all AST nodes have src annotations
		for (/node x := n, TypeSymbol _ !:= x, Message _ !:= x, Bound _ !:= x) {
			if (!(x.src?)) {
				println("No .src annotation on:
				        '   <x>");
				return false;
			}

			// Note that by removing all the (unannotated) empty lists here, we cover many more complex situations 
			// below in detecting adjacent nodes in syntax trees.
			children = [ e | e <- getChildren(x), e != []]; 

			// Here we collect all the possible ways nodes can be direct siblings in an abstract syntax tree:
			siblings = [
				*[<a,b> | [*_, node a, node b, *_]             := children], // adjacent nodes
				*[<a,b> | [*_, node a, [node b, *_], *_]       := children], // node followed by non-empty list
				*[<a,b> | [*_, [*_, node a], node b, *_]       := children], // non-empty list followed by node
				*[<a,b> | [*_, [*_, node a], [node b, *_], *_] := children], // adjacent non-empty lists
				*[<a,b> | [*_, [*_, node a, node b, *_], *_]   := children]  // nodes inside a list (elements can not be lists again)
			];

			// Note that by induction: if all the pairwise adjacent siblings are in-order, then all siblings are in order

			// siblings are sorted in the input, even if some of them are lists
			for (<a,b> <- siblings) {
				if (!leftToRight(a, b)) {
					println("Siblings are out of order:
							'a     : <a.src> is <a>
							'b     : <b.src> is <b>");
					return false;
				}
				if (ab <- [a,b], !included(n, ab)) {
					println("Child location not is not covered by the parent location:
					        '    parent: <n.src>
							'    child : <ab.src>, is <ab>");
					return false;
				}
			}

			// if ([*_, [*_, [*_], *_], *_] := getChildren(x)) {
			// 	println("Node contains a directly nested list:
			// 		    '   <n.src> : <n>");
			// 	return false;
			// }

			// if ([_, *_, str _, *_] := children || [*_, str _, *_, _] := children) {
			// 	println("Literals and identifiers must be singletons:
			// 	        '   <n>");
			// 	return false;
			// }
		}
	}
	
	if (checkNameResolution) {
		// all resolved names have the language as schema prefix
		//TODO: for the benefit of the compiler, changed
		//    assert all(/node m := n, m.decl?, /^<language>/ := decl(m).scheme);
		//to:
		for (/node m := n, m.decl?) {
		  if (/^<language>/ !:= decl(m).scheme) {
			println("<m.decl> has a strange loc scheme at <m.src>");
			return false;
		  }
		}
	}
	
	return true;
}

@synopsis{Check the AST node specification on a (large) set of ASTs and monitor the progress.}
bool astNodeSpecification(set[node] toCheck, str language = "java", bool checkNameResolution=false, bool checkSourceLocation=true) 
	= job("AST specification checker", bool (void (str, int) step) {
       for (node ast <- toCheck) {
		  step(loc l := ast.src ? l.path : "AST without src location", 1);
		  if (!astNodeSpecification(ast, language=language, checkNameResolution=checkNameResolution, checkSourceLocation=checkSourceLocation)) {
			 return false;
		  }
	   }

	   return true;
	}, totalWork=size(toCheck));



