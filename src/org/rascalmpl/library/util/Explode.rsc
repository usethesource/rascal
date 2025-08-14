@licence{
Copyright (c) 2023, NWO-I Centrum Wiskunde & Informatica (CWI) 
All rights reserved. 
  
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met: 
  
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
  
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
  
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.     
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl}
@synopsis{Explode lifts abstract syntax trees to parse trees}
@description{
The explode function is used to lift abstract syntax trees to concrete syntax trees.
The main difference is that all whitespace and comments are retrieved from the original
files and placed inside of the parse tree.    
}
@benefits{
* when analyzing a parse tree all information including layout and comments is preservation
* when transforming parse trees, the original layout and comments are transformed along; moroever things that remain the same, remain the same.
* unparsing a parse tree returns the exact original input file
* using the `explode` function we can reuse an external parser that produces ASTs, and still have Hi-fidelity source code analyses and transformations.
* the explode function is "type name preserving", such that a data-type named "Expression" becomes a concrete syntax tree type named "Expression"
}
@pitfalls{
* exploding only works correctly if the AST implements the AST specification from analysis::m3::AST.
* exploding takes about as much time as parsing a file
}
module util::Explode

extend ParseTree;
import IO;
import Node;
import List;
import Location;

@synopsis{Turn an AST into a ParseTree, while preserving the name of the type.}
syntax[&T] explode(data[&T] ast) {
   assert ast.src?;
   assert readFile(ast.src.top) == readFile(ast.src);
   assert astNodeSpecification(ast);

   if (syntax[&T] r := explode(ast, readFile(ast.src.top), ast.src.offset, ast.src.length)) {
      return r;
   }

   throw "unexpected problem while exploding <ast>";
}r

// singleton str nodes are lexicals (identifiers and constants)
Tree explode(data[&T] ast:str label(str identifier), str contents, int offset, int length) {
   return appl(prod(lex("*identifiers*"),[\iter-star(\char-class([range(1,1114111)]))],{}),
      [
         appl(regular(\iter-star(\char-class([range(1,1114111)]))),
            [char(ch) | ch <- chars(contents[offset..offset+length])])
      ]);
}

// lists get separator too. pretty sure the first and last separators will always be empty...
list[Tree] explodeList(list[data[&T]] lst, Symbol s, str contents, int offset, int length) {
   children = [
      *[
         separatorTree(contents, offset, c.src.offset),
         explode(c, contents, c.src.offset, c.src.length) | c <- children
      ],
      separatorTree(contents, last.src.offset + last.src.length, offset + length) | last <- children[-1..]
   ];

   return appl(regular(s), children);
}

// we do not further explode parse trees
Tree explode(Tree t, str _, int _, int _) = t;

// this is the main workhorse
default Tree explode(data[&T] ast, str contents, int offset, int length) {
   children = getChildren(ast);
   pox      = positions(ast.src, children);
   cons     = getConstructor(ast);
   symbols  = cons.symbols;
  
   // Here we generate a quasi syntax rule on-the-fly that has the structure and the types
   // of the exploded children. Each rule starts with separators, has separators
   // in between every child, and ends with separators. Each child node is modified
   // to a syntax node. Lists become iter-star symbols.
   rule = prod(\syntax(cons.def), [
         layouts("*separators*"), 
         *[\syntax(c), layouts("*separators*") | Symbol c <- symbols]
      ], 
      {});

   children = [
      *[
         separatorTree(contents, offset, c.src.offset),
         // there are 3 cases, mutually exclusive:
         *[explode(c, contents, c.src.offset, c.src.length)[src=p] | node _ := c], // a node
         *[emptyList(s, p)                                         | []     := c], // an empty list
         *[explodeList(c, \syntax(s), contents, c.src.offset, c.src.length)[src=p] | [_,*_] := c]  // a non-empty list
      | <c, s, p> <- zip3(children, symbols, pox)
      ],
      separatorTree(contents, last.src.offset + last.src.length, offset + length) | last <- children[-1..]
   ];

   return appl(rule, children, src=ast.src);
}

Tree emptyList(Symbol s, loc src) = appl(regular(s), [], src=src);

Tree separatorTree(str contents, int \start, int end)
   = appl(prod(layouts("*separators*"),[\iter-star(\char-class([range(1,1114111)]))],{}),
      [
         appl(regular(\iter-star(\char-class([range(1,1114111)]))),
            [char(ch) | int ch <- chars(contents[\start..end])])
      ]);

@synopsis{Helper function to convert AST notions to their ParseTree equivalent.}
@description{
* argument labels are kept for field access purposes later
* string constants represent (flat) lexical syntax
* abstract lists become concrete layout-separated nullable lists.
}
Symbol \syntax(label(str x, Symbol s)) = label(x, \syntax(s));
Symbol \syntax(\str())                 = \lex("*lexical*");
Symbol \syntax(\list(Symbol s))        = \iter-star-seps(\syntax(s),[layouts("*separators*")]);

private Symbol unlabel(label(str _, Symbol s))                  = unlabel(s);
private Symbol unlabel(conditional(Symbol s, set[Condition] _)) = unlabel(s);
private default Symbol unlabel(Symbol s)                        = s;

@synopsis{Give every element an exact and true location for later processing.}
@description{
For every AST element in a list, the function returns a list of the same length,
with each inferred fully-specified location in the place of the respective AST element.

There are strings, nodes, empty lists and non-empty lists to consider. Only nodes have
a `.src` field. For the other values a `loc` value is computed from the surrounding
siblings and the parent span. 

This algorithm runs in 2 steps:
1. `pos` first positions every type of possible abstract element
   * for lexical strings it becomes the entire span
   * empty lists are not resolvable in this stage, deferred with `empty:///`
   * nodes with src annotations; that is used
   * non-empty lists take the cover of the first and last element.
2. The second step is a fixed-point computation that incrementally replaces `empty:///` instances
by using the information of the already resolved siblings, until all `empty:///` spots have been resolved.
   * `empty:///` at the start means we can use the parent span for the left border.
   * `empty:///` at the end means we can use the parent span for the right border.
   * `empty:///` after a resolved location can take over the right border of that sibling.
   * `empty:///` before a resolved location can take over the left border of that sibling.

Due to the semantics of list matching, the algorithm typically replaces `empty:///` in the list
going from left to right to find instances of the above cases. 
}
private list[loc] positions(loc span, list[value] l) = infer(span, [pos(span, x) | x <- l]);

@synopsis{Replaces all |empty:///| with a correct loc inferred from the surroundings}
private list[loc] infer(loc span, [loc l, *loc rest])                       = infer(span, [span[length=0], *rest]) when l == |empty:///|;
private list[loc] infer(loc span, [*loc rest, loc l])                       = infer(span, [*rest, span[offset=span.offset+span.length-1][length=0]]) when l == |empty:///|;
private list[loc] infer(loc span, [*loc pre, loc before, loc l, *loc post]) = infer(span, [*pre, before, before[offset=before.offset+before.length][length = 0], *post]) when l == |empty:///|;
private list[loc] infer(loc span, [*loc pre, loc l, loc after, *loc post])  = infer(span, [*pre, after[offset=after.offset][length = 0], after, *post]) when l == |empty:///|;
private default list[loc] infer(loc _span, list[loc] done)                  = done;

@synsopsis{An element either knows its position, or it does not.}
@description{
This function applies the `span` and any directly available `.src` fields
to do a first estimate at solving the location of an AST element.
In particular it fails to do so for empty lists `[]`, which is left for 
the later `infer` stage. 
}
@pitfalls{
* This is where we have to assume that `str` fields are always singletons, otherwise we could not 
put the entire `span` around them.
}
private loc pos(loc span, str _)                 = span;
private loc pos(loc _span, [])                   = |empty:///|;
private loc pos(loc _span, node n)               = \loc(n);
private loc pos(loc _span, [node n])             = \loc(n);
private loc pos(loc _span, [node a, *_, node b]) = cover([\loc(a), \loc(b)]);

@synopsis{Waiting for `node.src` to be available in Rascal for good...}
private loc \loc(node n) = l when loc l := n.src;