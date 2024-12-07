@license{
  Copyright (c) 2022 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm - CWI}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@synopsis{Visualizing values using "ASCII art".}
@synopsis{This module provides functions that map values to strings using ASCII Art pretty printing.

The words [ASCII Art](https://en.wikipedia.org/wiki/ASCII_art) refers to the technique of 
constructing images from text characters that are in the ASCII set. However, in this case
we may use any Unicode character for visual representation purposes.}
@examples{
```rascal-shell
syntax E = "e" | E "+" E;
import IO;
import vis::Text;
ex = prettyTree([E] "e+e+e");
println(ex);
```
}
module vis::Text

import Node;
import List;
import ListRelation;
import ParseTree;
import Type;

@synopsis{Pretty prints parse trees using ASCII art lines for edges.}
str prettyTree(Tree t, bool src=false, bool characters=true, bool \layout=false, bool literals=\layout) {
  bool include(appl(prod(lit(_),_,_),_))                 = literals;
  bool include(appl(prod(cilit(_),_,_),_))               = literals;
  bool include(appl(prod(\layouts(_),_,_),_))            = \layout;
  bool include(amb({*_, appl(prod(\layouts(_),_,_),_)})) = \layout;
  bool include(char(_))                                  = characters;
  default bool include(Tree _)                           = true;

  str nodeLabel(appl(prod(label(str l, Symbol nt), _, _), _)) = "<type(nt,())> = <l>: ";
  str nodeLabel(appl(prod(Symbol nt, as, _), _))              = "<type(nt,())> = <for (a <- as) {><type(a,())> <}>";
  str nodeLabel(appl(error(Symbol nt, Production p, int dot), _)) = "!error dot=<dot>: <nodeLabel(appl(p,[]))>";
  str nodeLabel(appl(skipped(Symbol s), chars))               = "skipped";
  str nodeLabel(appl(regular(Symbol nt), _))                  = "<type(nt,())>";
  str nodeLabel(char(32))                                     = "⎵";
  str nodeLabel(char(10))                                     = "\\r";
  str nodeLabel(char(13))                                     = "\\n"; 
  str nodeLabel(char(9))                                      = "\\t";
  str nodeLabel(amb(_) )                                      = "❖";
  str nodeLabel(loc src)                                      = "<src>";
  str nodeLabel(cycle(Symbol nt, int len))                    = "cycle(<type(nt,())>, <len>)";
  default str nodeLabel(Tree v)                               = "<v>";

  lrel[str,value] edges(Tree t:appl(_,  list[Tree] args)) = [<"src", t@\loc> | src, t@\loc?] + [<"", k> | Tree k <- args, include(k)];
  lrel[str,value] edges(amb(set[Tree] alts))              = [<"", a> | Tree a <- alts];
  lrel[str,value] edges(loc _)                            = [];
  default lrel[str,value] edges(Tree _)                   = [];
    
  return ppvalue(t, nodeLabel, edges);
}

@synopsis{Pretty prints nodes and ADTs using ASCII art for the edges.}
str prettyNode(node n, bool keywords=true) {
  str nodeLabel(list[value] _)       = "[…]";
  str nodeLabel(set[value] _)        = "{…}";
  str nodeLabel(map[value, value] _) = "(…)";
  str nodeLabel(value t)             = "\<…\>" when typeOf(t) is \tuple;
  str nodeLabel(node k)              = getName(k);
  default str nodeLabel(value v)     = "<v>";
  
  lrel[str,value] edges(list[value] l)       = [<"", x> | value x <- l];
  lrel[str,value] edges(value t)             = [<"", x> | value x <- carrier([t])] when typeOf(t) is \tuple;
  lrel[str,value] edges(set[value] s)        = [<"", x> | value x <- s];
  lrel[str,value] edges(map[str, value] m)   = [<"<x>", m[x]> | value x <- m];  
  lrel[str,value] edges(map[num, value] m)   = [<"<x>", m[x]> | value x <- m];  
  lrel[str,value] edges(map[loc, value] m)   = [<"<x>", m[x]> | value x <- m];  
  lrel[str,value] edges(map[node, value] m)  = [<"key", x>, <"value", m[x]> | value x <- m];  
  lrel[str,value] edges(node k)              = [<"", kid> | value kid <- getChildren(k)] + [<l, m[l]> | keywords, map[str,value] m := getKeywordParameters(k), str l <- m];
  default lrel[str,value] edges(value _)     = [];
    
  return ppvalue(n, nodeLabel, edges);
}

private str ppvalue(value e, str(value) nodeLabel, lrel[str,value](value) edges) 
  = " <nodeLabel(e)>
    '<ppvalue_(e, nodeLabel, edges)>";

private str ppvalue_(value e, str(value) nodeLabel, lrel[str,value](value) edges, str indent = "") {
  lrel[str, value] kids = edges(e);
  int i = 0;

  str indented(str last, str other, bool doSpace) 
    = "<indent> <if (i == size(kids) - 1) {><last><} else {><other><}><if (doSpace) {> <}>";
    
  return "<for (<str l, value sub> <- kids) {><indented("└─", "├─", l == "")><if (l != "") {>─<l>─→<}><nodeLabel(sub)>
         '<ppvalue_(sub, nodeLabel, edges, indent = indented(" ", "│", true))><i +=1; }>";
}
