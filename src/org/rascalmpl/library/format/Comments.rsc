module \format::Comments

import ParseTree;
import String;

@doc{this rule matches layout nodes and filters out all source code comments, neatly separating them by a newline if there are more than one and returning
the empty string otherwise. This allows one to splice in any layout position and the existing whitespace will be removed but the existing comments will
be retained.}
default format(Tree t:appl(prod(layouts(str _), list[Symbol] _, set[Attr] _),list[Tree] _))
  = "~for (/Tree c:appl(prod(_,_,{*_, \tag("category"("Comment"))}),_) := t) {
    '~c~}";

default format(appl(prod(Symbol _, [lit(str _),*Symbol _, lit("\n")],{*_, \tag("category"("Comment"))}),
               [Tree prefix, *Tree content, Tree newline]))
  = "// ~~content
    '";             

default format(appl(prod(Symbol _, [lit("/*"),*Symbol _, lit("*/")],{*_, \tag("category"("Comment"))}),
               [Tree prefix, *Tree content, Tree newline]), int width=100) {
  lines = split("\n", "~~content");
  lines = [ /^\W**/ := e ? e[2..] : e | e <- c];
  
  if ([line] := lines) {
    return "/* ~(trim(line)) */";
  } else {
    return "/*
           '~for (line <- lines) { * ~(trim(line))
           '~}*/";
  }               
}
