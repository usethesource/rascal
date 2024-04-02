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

import List;

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
* `L` produces A literal word. This word may only contain printable characters and no spaces; this is a required property that the formatting algorithm depends on for correctness.
* `U` splices its contents in the surrounding box, for automatic flattening of overly nested structures in syntax trees.
* `G` is an additional group-by feature that reduces tot the above core features
* `SL` is a convenience box for separated syntax lists based on `G`
* `NULL()` is the group that will dissappear from its context, useful for skipping content. It is based on the `U` box.
}
@benefits{
* Box expressions are a declarative mechanism to express formatting rules that are flexible enough to deal
with limited horizontal space, and satisfy the typical alignment and indentation principles found in 
the coding standards for programming languages.
* The parameters of Box expressions allow for full configuration. It is up to the code that produces Box
expressions to present these parameters to the user or not. For example, indentation level `is` should be
set on every `I` Box according to the current preferences of the user. 
}
@pitfalls{
* `U(boxes)` is rendered as `H(boxes)` if it's the outermost Box.
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
    | G(list[Box] boxes, Box(list[Box]) op = H, int gs=2)
    | NULL()
    ;

@synopsis{A row is a list of boxes that go into an `A` array/table.}
@description{
Rows do not have parameters. These are set on the `A` level instead,
or per cell Box. 
}
data Row = R(list[Box] cells);

data Alignment = l() | r() | c();  

@synopsis{NULL can be used to return a Box that will completely dissappear in the surrounding context.}
@description{
Consider `NULL()`` as an alternative to producing `H([])` when you see unexpected
additional spaces generated.

Typical applications for NULL would be to produce it for layout nodes that contain
only whitespace. If you encounter source code comments you can produce the appropriate `L` content,
but if you want the position ignored, use `NULL`.

`NULL`` depends on the splicing semantics of `U` to dissappear completely before the layout
algorithm starts counting boxes and widths. 
}
@examples{
* `H([L("A"), H([]),L("B")])` will produce `"A  B"` with two spaces;
* `H([L("A"), NULL(),L("B")])` will produce `"A B"` with only one space.
}
@pitfalls{
* Do not use `NULL` for empty Row cells, unless you do want your cells aligned to the left and filled up to the right with empty H boxes.
* NULL will be formatted as `H([])` if it's the outermost Box.
}
Box NULL() = U([]);