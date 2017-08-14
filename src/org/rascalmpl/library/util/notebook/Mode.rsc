// This module was taken from Salix. The original module name  can be found in: salix::lib::Mode
@license{
  Copyright (c) Tijs van der Storm <Centrum Wiskunde & Informatica>.
  All rights reserved.
  This file is licensed under the BSD 2-Clause License, which accompanies this project
  and is available under https://opensource.org/licenses/BSD-2-Clause.
}
@contributor{Tijs van der Storm - storm@cwi.nl - CWI}

module util::notebook::Mode

import Type;
import String;
import ParseTree;

data Mode
  = mode(str name, list[State] states, map[str, value] meta = ());
  
data State
  = state(str name, list[Rule] rules)
  ;
  
data Rule
  = rule(str regex, list[str] tokens, str next = "", bool indent=false, bool dedent=false)
  ;
  
str cat2token("StringLiteral") = "string";
str cat2token("Comment") = "comment";
str cat2token("Constant") = "atom";
str cat2token("Variable") = "variable";
str cat2token(str _) = "unknown";


Mode grammar2mode(str name, type[&T <: Tree] sym) {
  defs = sym.definitions;
  
  str reEsc(str c) //= c in {"*", "\\", "+", "?", "|"} ? "\\<c>" : c;
    = escape(c, ("*": "\\*", "\\": "\\\\", "+": "\\+", "?": "\\?", "|": "\\|", "^": "\\^", "/": "\\/", "^^": "\\^\\^"));
  
  set[str] lits = { x | /lit(x:/^[a-zA-Z0-9_]*$/) := defs };
  
  set[str] ops 
    = { x | /prod(_, [_, _, lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/), _, _], ts) := defs, !any(\tag("category"("Comment")) <-  ts)}
    + { x | /prod(_, [lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/), _, _], ts) := defs, !any(\tag("category"("Comment")) <-  ts) }
    + { x | /prod(_, [_, _, lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/)], ts) := defs, !any(\tag("category"("Comment")) <-  ts) };

  set[str] commen 
    = { x | /prod(_, [_, _, lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/), _, _], ts) := defs, any(\tag("category"("Comment")) <-  ts)}
    + { x | /prod(_, [lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/), _, _], ts) := defs, any(\tag("category"("Comment")) <-  ts) }
    + { x | /prod(_, [_, _, lit(x:/^[+\-\<\>=!@#%^&*~\/|]*$/)], ts) := defs, any(\tag("category"("Comment")) <-  ts) };
    
  // todo: sort by length, longest first.
  kwRule = rule("(?:<intercalate("|", [ l | l <- lits ])>)\\b", ["keyword"]);   
  opRule = rule("(?:<intercalate("|", [ reEsc(l) | l <- ops ])>)", ["operator"]);
  commRule = rule("<intercalate("|", [ reEsc(l) | l <- commen ])>", ["comment"]);
  // todo: add Variable with word boundaries.
     
  return mode(name, [state("start", [kwRule, opRule, commRule])], meta = ());
}