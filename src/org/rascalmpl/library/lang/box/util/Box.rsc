@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module lang::box::util::Box

data Box(int hs=-1, int vs=-1, int is=-1, int ts=-1, int width=-1, int height=-1)
    = H (list[Box] h)
    | V(list[Box] v)
    | HOV (list[Box] hov)
    | HV (list[Box] hv)
    | I(list[Box] i)
    | WD(list[Box] wd)
    | R(list[Box] r)
    | A(list[Box] a)
    | SPACE(int space)
    | L(str l)
    | KW(Box kw)
    | VAR(Box  var)
    | NM(Box nm)
    | STRING(Box  string)
    | COMM(Box  comm)
    | MATH(Box math)
    | ESC(Box esc)
    | REF(int ref)
    | NULL()
    ;
    
alias Text = list[str];



