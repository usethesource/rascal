@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
module lang::rascal::checker::Lift

import languages::lang::rascal::syntax::Rascal;
import List;
import ParseTree; 

// This module will contain functionality to lift 
// embedded concrete syntax fragments to expressions
// and patterns over the UPTR data-type
 
public Expression lift(Tree e) {
  l = e@\loc;

  if (appl(prod, args) := e) {
    return `appl(<lift(prod,l)>,<lift(args)>)`;
    1;
  }  

  throw "missed a case: <e>";
}

public Expression lift(Production p, loc l) {
  if (prod(lhs, rhs, attrs) := p) {  
     return `prod(<lift(lhs,l)>,<lift(rhs,l)>,<lift(attrs,l)>)`;
  }

  throw "missed a case: <p>";
}

public Expression lift(Attributes a, loc l) {
  switch(a) {
    case \no-attrs(): return (Expression) `no-attrs()`[@\loc=l];
    case attrs(as): return (Expression) `attrs(<lift(as, l)>)`[@\loc=l];
  }
   throw "missed a case: <a>";
}

public Expression lift(Attr a) {
  switch(a) {
     case assoc(left()): return (Expression) `assoc(left())`[@\loc=l];
     case assoc(right()): return (Expression) `assoc(right())`[@\loc=l];
     case assoc(assoc()): return (Expression) `assoc(assoc())`[@\loc=l];
     case assoc(\non-assoc()): return (Expression) `assoc(\non-assoc())`[@\loc=l];
     case term(v): return (Expression) `term(<[Expression] "<v>">)`[@\loc=l];
     case id(i): return (Expression) `id(<[Literal] i>)`[@\loc=l]; 
     case bracket(): return (Expression) `bracket()`;
     case prefer(): return (Expression) `prefer()`;
     case avoid(): return (Expression) `avoid()`;
     case reject(): return (Expression) `reject()`;
  }
   
  throw "missed a case: <a>";
}

public Expression lift(list[Symbol] syms, loc l) {
   if (syms == []) {
      return `[]`[@\loc=l];
   }
   
   if (`[ <{Expression ","}* tail> ]` := lift(tail(syms), l)) {
      head = lift(head(syms));
      return `[<Expression head>,<{Expression ","}* tail>]`[@\loc=l];
   }
}

public Expression lift(list[Attr] attrs, loc l) {
   if (attrs == []) {
      return `[]`[@\loc=l];
   }
   
   if (`[ <{Expression ","}* tail> ]` := lift(tail(attrs), l)) {
      head = lift(head(syms));
      return `[<Expression head>,<{Expression ","}* tail>]`[@\loc=l];
   }
}

public Expression lift(list[CharRange] ranges, loc l) {
   if (ranges == []) {
      return `[]`[@\loc=l];
   }
   
   if (`[ <{Expression ","}* tail> ]` := lift(tail(ranges), l)) {
      head = lift(head(syms));
      return `[<Expression head>,<{Expression ","}* tail>]`[@\loc=l];
   }
}

public str escape(str s) {
  return "\"<s>\"";
}

public Expression lift(Symbol s, loc l) {
   switch(s) {
     case sort(t): return (Expression) `sort(<[Literal] escape(t)>)`[@\loc=l];
     case cf(t): return (Expression) `cf(<lift(t,l)>)`[@\loc=l];
     case lex(t): return (Expression) `lex(<lift(t,l)>)`[@\loc=l];
     case iter(t): return (Expression) `iter(<lift(t,l)>)`[@\loc=l];
     case \iter-sep(t,p): return (Expression) `\iter-sep(<lift(t,l)>,<lift(p,l)>)`[@\loc=l];
     case \iter-star(t): return (Expression) `\iter-star(<lift(t,l)>)`[@\loc=l];
     case \iter-star-sep(t,p):  return (Expression) `\iter-star-sep(<lift(t,l)>,<lift(p,l)>)`[@\loc=l];
     case opt(t): return (Expression) `opt(<lift(t,l)>)`[@\loc=l];
     case lit(t): return (Expression) `lit(<[Literal] escape(t)>)`[@\loc=l];
     case empty(): return (Expression) `empty()`[@\loc=l];
     case layout(): return (Expression) `layout()`[@\loc=l];
     case \char-class(ranges): return lift(ranges,l); 
     case  \parameterized-sort(t, ps): return `\parameterized-sort(<[Literal] escape(t)>, lift(ps, l))`[@\loc=l];
     case \var-sym(t) : return `\var-sym(<lift(t,l)>)`[@\loc=l];
     case \alt(t, u) : return `\alt(<lift(t,l)>,<lift(u,l)>)`[@\loc=l];
     case \seq(syms) : return `\seq(<lift(syms,l)>)`[@\loc=l];
     case \tuple(h, t) : return `\tuple(<lift(h,l)>, <lift(t, l)>)`[@\loc=l];
   }
  
   throw "missed the case for <s>";
}

public Expression lift(CharRange r) {
  switch(r) {
     case single(i) : return (Expression) `single(<[Literal] "<i>">)`;
     case range(i,j): return (Expression) `range(<[Literal] "<i>">,<[Literal] "<j>">)`; 
  }
  throw "missed the case for <r>";
}
public Pattern lift(Pattern e) {

  return e;
}
