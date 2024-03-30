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

data Box(int hs=1, int vs=0, int is=2)
    = H(list[Box] boxes)
    | V(list[Box] boxes)
    | HOV(list[Box] boxes)
    | HV(list[Box] boxes)
    | I(list[Box] boxes)
    | WD(list[Box] boxes)
    | A(list[Row] rows, list[Alignment] columns=[l() | [R(list[Box] cs), *_] := rows, _ <- cs] /* learns the amount of columns from the first row */)
    | SPACE(int space)
    | L(str word)
    ;

data Row = R(list[Box] cells);

data Alignment = l() | r() | c();  
alias Text     = list[str];




