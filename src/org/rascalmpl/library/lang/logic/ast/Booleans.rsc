@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module lang::logic::ast::Booleans

data Formula 
  = \true()
  | \false()
  | \not(Formula arg)
  | \and(Formula lhs, Formula rhs)
  | \and(set[Formula] args)
  | \or(Formula lhs, Formula rhs)
  | \or(set[Formula] args)
  | \if(Formula lhs, Formula rhs)
  | \fi(Formula lhs, Formula rhs)
  | \iff(Formula lhs, Formula rhs)
  ;

Formula or({Formula x}) = x;
Formula and({Formula x}) = x;
Formula and(Formula a, Formula b) = and({a,b});
Formula or(Formula a, Formula b) = or({a,b});
Formula and({*Formula a, and(set[Formula] b)}) = and(a + b);
Formula or({*Formula a, or(set[Formula] b)}) = or(a + b);

Formula simplify(or({\true(), *Formula _}))   = \true();
Formula simplify(and({\false(), *Formula _})) = \false();
Formula simplify(not(not(Formula g))) = g;
Formula simplify(not(\true()))        = \false();
Formula simplify(not(\false()))       = \true();
Formula simplify(\if(Formula l, Formula r))           = or(not(l),r);
Formula simplify(\fi(Formula l, Formula r))           = \if(r, l);
Formula simplify(\iff(Formula l, Formula r))          = and(\if(l,r),\fi(l,r));
Formula simplify(and({Formula g,\not(g),*Formula r})) = \false();

default Formula simplify(Formula f) = f;
