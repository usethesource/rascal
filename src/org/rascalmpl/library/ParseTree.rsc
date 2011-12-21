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
//START
module ParseTree

import Message;

@doc{These are the trees normally found after parsing}
data Tree 
  = appl(Production prod, list[Tree] args) 
  | cycle(Symbol symbol, int cycleLength) 
  | amb(set[Tree] alternatives)  
  | char(int character)
  ;
  
@doc{These trees constructors are used additionally in error trees}
data Tree 
  = error(Production prod, list[Tree] args, list[Tree] rest)
  | expected(Symbol symbol)
  | erroramb(set[Tree] alternatives)
  | errorcycle(Symbol symbol, int cycleLength)
  ;

@doc{
  Productions are the rules of a grammar, with a defined non-terminal, a list
  of terminal and non-terminal symbols and a possibly empty set of attributes.
}
data Production 
  = prod(Symbol def, list[Symbol] symbols, set[Attr] attributes) 
  | regular(Symbol def)
  ;

@doc{
  Attributes document additional semantics of a production rule. Neither tags nor
  brackets are processed by the parser generator. Rather downstream processors are
  activated by these. Associativity is a parser generator feature though. 
}
data Attr 
  = \assoc(Associativity \assoc)  
  | \tag(value \tag) 
  | \bracket() 
  ;

@doc{These are the kinds of associativity}
data Associativity 
  = \left() 
  | \right() 
  | \assoc() 
  | \non-assoc()
  ;

data CharRange = range(int begin, int end);

alias CharClass = list[CharRange];

@doc{The start symbol wraps any symbol to indicate it will occur at the top}
data Symbol = \start(Symbol symbol);

@doc{These symbols are the named non-terminals} 
data Symbol 
  = \sort(str name)  
  | \lex(str name) 
  | \layouts(str name) 
  | \keywords(str name)
  | \parameterized-sort(str name, list[Symbol] parameters)  
  | \parameter(str name)
  | \label(str name, Symbol symbol)
  ; 

@doc{These are the terminal symbols}
data Symbol 
  = \lit(str string) 
  | \cilit(str string)
  | \char-class(list[CharRange] ranges)
  ;
    
@doc{These are the regular expressions}
data Symbol
  = \empty()  
  | \opt(Symbol symbol)  
  | \iter(Symbol symbol)   
  | \iter-star(Symbol symbol)   
  | \iter-seps(Symbol symbol, list[Symbol] separators)   
  | \iter-star-seps(Symbol symbol, list[Symbol] separators) 
  | \alt(set[Symbol] alternatives)
  | \seq(list[Symbol] sequence)
  ;
  
@doc{The conditional wrapper adds conditions to the existance of an instance of a symbol}
data Symbol = \conditional(Symbol symbol, set[Condition] conditions);

@doc{Conditions on symbols give rise to disambiguation filters.}    
data Condition
  = \follow(Symbol symbol)
  | \not-follow(Symbol symbol)
  | \precede(Symbol symbol)
  | \not-precede(Symbol symbol)
  | \delete(Symbol symbol)
  | \at-column(int column) 
  | \begin-of-line()  
  | \end-of-line()  
  ;
         
@doc{provides access to the source location of a parse tree node}
anno loc Tree@\loc;

@doc{Parse the contents of a resource pointed to by the input parameter and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] \begin, loc input);

@doc{Parse the contents of a resource pointed to by the input parameter and return a parse tree which can contain error nodes.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parseWithErrorTree(type[&T<:Tree] begin, loc input);

@doc{Parse a string and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input);

@doc{Parse a string and return a parse tree, which can contain error nodes.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parseWithErrorTree(type[&T<:Tree] begin, str input);

@doc{Parse a string and return a parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parse(type[&T<:Tree] begin, str input, loc origin);

@doc{Parse a string and return a parse tree, which can contain error nodes.}
@javaClass{org.rascalmpl.library.ParseTree}
@reflect{uses information about syntax definitions at call site}
public java &T<:Tree parseWithErrorTree(type[&T<:Tree] begin, str input, loc origin);

@doc{Yields the string of characters that form the leafs of the given parse tree.}
@javaClass{org.rascalmpl.library.ParseTree}
public java str unparse(Tree tree);

@doc{
Parsetree Implosion
===================

This function implodes a parsetree by simulataneously traversing the 
reified ADT and the parse tree. Meanwhile, an AST is constructed as follows:

- Literals, layout and empty (i.e. ()) nodes are skipped.

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
  
- Unlabeled lexicals are imploded to str, int, real, bool depending on the expected type in
  the ADT. To implode lexical into types other than str, the PDB parse functions for 
  integers and doubles are used. Boolean lexicals should match "true" or "false". 
  NB: lexicals are imploded this way, even if they are ambiguous.

- If a lexical tree has a cons label, the tree imploded to a constructor with that name
  and a single string-valued argument containing the tree's yield.

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
public java &T<:node implode(type[&T<:node] t, Tree tree);

@doc{introduces a (error) message related to a certain sub-tree}
public anno Message Tree@message;

@doc{lists all (error) messages relevant for a certain sub-tree}
public anno set[Message] Tree@messages;

@doc{provides a documentation string for this parse tree node}
anno str Tree@doc;

@doc{provides a documentation string for certain locations}
anno map[loc,str] Tree@docs;

@doc{provides the target of a reference}
anno loc Tree@link;

@doc{provides multiple targets of a references}
anno set[loc] Tree@links;

@doc{result type for treeAt()}
public data TreeSearchResult[&T<:Tree] = treeFound(&T tree) | treeNotFound();

@doc{selects the innermost Tree of type t which location encloses l}
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
