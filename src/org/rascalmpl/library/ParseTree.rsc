module ParseTree

import Message;

data Tree =
     appl(Production prod, list[Tree] args) |
	 cycle(Symbol symbol, int cycleLength) |
	 amb(set[Tree] alternatives) | 
	 char(int character);

data Production =
     prod(list[Symbol] lhs, Symbol rhs, Attributes attributes) | 
     regular(Symbol rhs, Attributes attributes);

data Attributes = \no-attrs() | \attrs(list[Attr] attrs);

data Term = \lex() | \literal() | \cons(value \constructor);

data Attr =
     \assoc(Associativity \assoc) | 
     \term(value \term) |  
     \bracket() | \reject();

data Associativity =
     \left() | \right() | \assoc() | \non-assoc();

data CharRange = range(int start, int end);

// data Constructor = cons(str name);

alias CharClass = list[CharRange];

data Symbol =
     \START() |
     \start(Symbol symbol) |
     \label(str name, Symbol symbol) |
     \lit(str string) |
     \cilit(str string) | 
     \empty()  |
     \opt(Symbol symbol)  |
     \sort (str string)  | 
     \layouts(str name) |
     \iter(Symbol symbol)  | 
     \iter-star(Symbol symbol)  | 
     \iter-seps(Symbol symbol, list[Symbol] separators)  | 
     \iter-star-seps(Symbol symbol, list[Symbol] separators) |
     \parameterized-sort(str sort, list[Symbol] parameters)  |
     \parameter(str name) |
     \char-class(list[CharRange] ranges) |
     \at-column(int column) |
     \start-of-line() |
     \end-of-line();
     
@deprecated{Used in SDF2, but not used in Rascal anymore}
data Symbol =
     \layout()  | 
     \alt(Symbol lhs, Symbol rhs)  |
     \tuple(Symbol head, list[Symbol] rest)  |
     \seq(list[Symbol] symbols);
     
@doc{provides access to the source location of a parse tree node}
anno loc Tree@\loc;

@doc{Parse the contents of a resource pointed to by the input parameter and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about imported SDF modules at call site}
public &T<:Tree java parse(type[&T<:Tree] start, loc input);

@doc{Parse a string and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about imported SDF modules at call site}
public &T<:Tree java parse(type[&T<:Tree] start, str input);

@doc{Yields the string of characters that form the leafs of the given parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
public str java unparse(Tree tree);

@doc{
Parsetree Implosion
===================

This function implodes a parsetree by simulataneously traversing the 
reified ADT and the parse tree. Meanwhile, an AST is constructed as follows:

- Literals and layout nodes are skipped.

- Regular */+ lists are imploded to list[]s or set[]s depending on what is 
  expected in the ADT.

- Ambiguities are imploded to set[]s.

- If a tree's production has no label and a single AST (i.e. non-layout, non-literal) argument
  (for instance, and injection), the tree node is skipped, and implosion continues 
  with the lone argument. The same applies to bracket productions, even if they
  are labeled.

- If a tree's production has no label, but more than one argument, the tree is imploded 
  to a tuple (provided this conforms to the ADT).
  
  Example
  
    syntax IDTYPE = Id ":" Type;
    
    syntax Decls = decls: "declare" \{IDTYPE ","\}* ";";
    
  Hence, Decls will be imploded as:
    
    data Decls = decls(list[tuple[str,Type]]);
    
  (assuming Id is a lexical non-terminal).   

- Optionals are imploded to booleans if this is expected in the ADT.
  This also works for optional literals, as shown in the following
  example:
  
    syntax Formal = formal: "VAR"? \{Id ","\}+ ":" Type;
  
  The corresponding ADT could be:
  
    data Formal = formal(bool, list[str], Type);
    

- An optional is imploded to a list with zero or one argument, iff a list
  type is expected.

- If the argument of an optional tree has a production with no label, containing
  a single list, then this list is spliced into the optional list.
  
  Example:
  
    syntax Tag = "[" \{Modifier ","\}* "]";
    syntax Decl = decl: Tag? Signature Body;
  
  In this case, a Decl is imploded into the following ADT:
  
    data Decl = decl(list[Modifier], Signature, Body);  
  
- For trees with (cons-)labeled productions, the corresponding constructor
  in the ADT corresponding to the non-terminal of the production is found in
  order to make the AST.
  
  Typical example:
  
    syntax Exp = left add: Exp "+" Exp;
  
  Can be imploded into:
    data Exp = add(Exp, Exp);
  
- Lexicals are imploded to str, int, real, bool depending on the expected type in
  the ADT. To implode lexical into types other than str, the standard Java parse
  functions Integer.parseInt and Double.parseDouble are used. Boolean lexicals should match
  "true" or "false". NB: lexicals are imploded this way, even if they are ambiguous.
  

IllegalArgument is thrown if during implosion a tree is encountered that cannot be
imploded to the expected type in the ADT. As explained above, this routine assumes the
ADT type names correspond to syntax non-terminal names, and constructor names correspond 
to production labels. Labels of production arguments do not have to match with labels
 in ADT constructors.

Finally, source location annotations are propagated as annotations on constructor ASTs. 
To access them, the user is required to explicitly declare a location annotation on all
ADTs used in implosion. In other words, for every ADT type T, add:

anno loc T@location;

}
@javaClass{org.rascalmpl.library.ParseTree}
public &T<:node java implode(type[&T<:node] t, Tree tree);

@doc{introduces a (error) message related to a certain sub-tree}
public anno Message Tree@message;

@doc{lists all (error) messages relevant for a certain sub-tree}
public anno set[Message] Tree@messages;

@doc{provides a documentation string for this parse tree node}
anno str Tree@doc;

@doc{provides the target of a link}
anno loc Tree@link;