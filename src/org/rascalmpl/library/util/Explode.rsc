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
import String;

@synopsis{Turn an AST into a ParseTree, while preserving the name of the type, and the entire input string}
syntax[&T] explode(data[&T] ast) {
   assert readFile(ast.src.top) == readFile(ast.src);
   Production cons = getConstructor(ast);

   if (syntax[&T] r := explode(ast, unlabel(cons.def), readFile(ast.src.top), ast.src.top, ast.src.offset, ast.src.length)) {
      return r[src=ast.src];
   }

   throw "unexpected problem while exploding <ast>";
}

@synopsis{singleton str nodes are lexicals (identifiers and constants)}
Tree explode(data[&T] ast:str name(str _), Symbol def, str contents, loc _top, int offset, int length) {
   Symbol allChars = \char-class([range(1,1114111)]);
   Symbol allCharsStar = \iter-star(allChars);
   
   return appl(prod(label(name, \syntax(def)),[allCharsStar],{}), 
      [appl(regular(allCharsStar), [char(ch) | ch <- chars(contents[offset..offset+length])])]);
}

@synopsis{Special case for empty lists}
Tree explode([], Symbol def, str contents, loc top, int offset, int length) 
   = appl(regular(\syntax(def), []));

@synopsis{Special case for singleton lists (can never be a list of lists)}
Tree explode([data[&T] elem], Symbol def, str contents, loc top, int offset, int length) 
   = appl(regular(\syntax(def)), [explode(elem, def.symbol, contents, offset, length)]);

@synopsis{Abstract lists become concrete lists}
Tree explode(list[value] children, Symbol def, str contents, loc top, int offset, int length) {
   list[loc]    pox      = positions(top(offset, length), children);
   Symbol elem = def.symbol;

   rule = regular(\syntax(def));

   work = zipi(zip2(children, pox));
   count = size(work);

   children = [
      *[separatorTree(contents, offset, pos.offset) | count > 0, <_, <_, _, loc pos>> := work[0]],
      *[ 
         explode(c, elem, contents, top, pos.offset, pos.length)[src=pos], // element
         *[separatorTree(contents, pos.offset + pos.length, next.offset) | i + 1 < count, <_, loc next> := work[i + 1]], // middle
         *[separatorTree(contents, pos.offset + pos.length, offset + length) | i == count, <_, loc lp> := work[i]]   // last
      | <int i, <value c, loc pos>> <- work
      ]
   ];

   return appl(rule, children);
}

@synopsis{do not further explode parse trees}
Tree explode(Tree t, Symbol _, str _, int _, int _) = t;

@synopsis{Null constructor}
Tree explode(data[&T] ast: _(), Symbol def, str contents, loc _pos, int offset, int length) {
   rule = prod(\syntax(def), [layouts("*seps*")],  {});
   
   return appl(rule, [separatorTree(contents, offset, offset + length)]);
}

@synopsis{AST nodes with a single child}
Tree explode(data[&T] ast:str label(value child), Symbol _def, str contents, loc pos, int offset, int length) {
   list[value]  children = [child];
   list[loc]    pox      = positions(ast.src, children);
   Production   cons     = getConstructor(ast);
   list[Symbol] symbols  = cons.symbols;

   rule = prod(\syntax(cons.def), [layouts("*seps*")],  {});
   
   return appl(rule, [
      separatorTree(contents, offset, pox[0].offset),
      explode(child, unlabel(cons.symbols[0]), contents, pos, pox[0].offset, pox[0].length)[src=pox[0]],
      separatorTree(contents, pox[0].offset + pox[0].length, offset + length)
   ]);
}


@synopsis{main workhorse for AST nodes with more than one children}
default Tree explode(data[&T] ast, Symbol _def, str contents, loc top, int offset, int length) {
   list[value]  children = getChildren(ast);
   list[loc]    pox      = positions(ast.src, children);
   Production   cons     = getConstructor(ast);
   list[Symbol] symbols  = cons.symbols;
   
   rule = prod(\syntax(cons.def), [layouts("*seps*"),  *[\syntax(c), layouts("*seps*") | Symbol c <- symbols]],  {});
   
   work = zipi(zip3(children, symbols, pox));
   count = size(work);
   
   children = [
      *[separatorTree(contents, offset, pos.offset) | count > 0, <_, <_, _, loc pos>> := work[0]],
      *[ 
         explode(c, s, contents, top, pos.offset, pos.length)[src=pos], // element
         *[separatorTree(contents, pos.offset + pos.length, next.offset) | i + 1 < count, <_, <_, _, loc next>> := work[i + 1]], // middle
         *[separatorTree(contents, pos.offset + pos.length, offset + length) | i == count - 1] // last
      | <int i, <value c, Symbol s, loc pos>> <- work
      ]
   ];

   return appl(rule, children);
}

@synopsis{Generate a layout tree with the separator content}
Tree separatorTree(str contents, int \start, int end)
   = appl(prod(layouts("*seps*"),[\iter-star(\char-class([range(1,1114111)]))],{}),
      [appl(regular(\iter-star(\char-class([range(1,1114111)]))),
            [char(ch) | int ch <- chars(contents[\start..end])])]);

@synopsis{Helper function to convert AST notions to their ParseTree equivalent.}
@description{
* argument labels are kept for field access purposes later
* string constants represent (flat) lexical syntax
* abstract lists become concrete layout-separated nullable lists.
}
Symbol \syntax(label(str x, Symbol s)) = label(x, \syntax(s));
Symbol \syntax(\str())                 = \lex("*lexical*");
Symbol \syntax(\list(Symbol s))        = \iter-star-seps(\syntax(s),[layouts("*seps*")]);

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

@synsopsis{Take the src field or infer the position from context}
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

@synopsis{Infer positions of separators}
private list[loc] sepPos([], loc ctx) = [];

private list[loc] sepPos([loc single], loc ctx) 
   = [ctx.top[length=endFirst], single, single.top[offset=startLast][length=endLength]]
   when int lengthFirst := single.offset - ctx.offset,
        int startLast := single.offset+single.length,
        int endLength := ctx.offset + ctx.length - single.offset;

private list[loc] sepPos([loc first, *loc rest], loc ctx)
   = [ctx.top[length=endFirst], first, *sepPos(rest, first.top[offset=ctxStart][length=ctxLength])] 
   when int lengthFirst := single.offset - ctx.offset,
        int ctxStart := single.offset+single.length,
        int ctxLength := ctx.length-first.length;