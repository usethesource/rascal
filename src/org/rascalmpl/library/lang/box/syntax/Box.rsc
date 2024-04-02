@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module lang::box::\syntax::Box

@synopsis{Every kind of boxes encodes one or more parameterized two-dimensional text constraints.}
@description{
* `H` puts their elements next to each other one the same line separated by `hs` spaces.
* `V` puts their elements below each other on their own line, separated by `vs` empty lines.
* `HOV` acts like `H` as long as the elements all fit on one line, otherwise it becomes a `V`
* `HV` acts like H until the line is full, and then continues on the next line like `V`.
* `I` is a `V` box that indents every line in the output with `is` spaces
* `WD` produces a number of spaces exactly as wide as the wides line of the constituent boxes
* `A` is a table formatter. The list of Rows is formatted with `H` but each cell is aligned vertically with the rows above and below.
* `SPACE` produces `space` spaces
* `L` produces the literal word. This word may only contain printable characters and no spaces; this is a required property that the formatting algorithm depends on for correctness.
* `U` splices its contents in the surrounding box, for automatic flattening of overly nested structures in syntax trees.
}
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
    | U(list[Box] boxes)
    ;

@synopsis{A row is a list of boxes that go into an `A` array/table.}
@description{
Rows do not have parameters. These are set on the `A` level instead,
or per cell Box. 
}
data Row = R(list[Box] cells);

data Alignment = l() | r() | c();  

@synopsis{U flattens by splicing itself into its context.}
@description{
For efficiency's sake the flattening of U into the other boxes
is done lazily and on demand during the layout algorithm.
}
Box U([*Box front, U([*Box middle]), *Box end])
  = U([*front, *middle, *end]);
 