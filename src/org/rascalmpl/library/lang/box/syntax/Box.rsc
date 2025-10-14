@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@synopsis{An abstract declarative language for two dimensional text layout}
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
* `G` is an additional group-by feature for `list[Box]` that reduces tot the above core features. You can use it to wrap another
box around every `gs` elements.
* `AG` is an additional group-by feature for array `Row`s that reduces to the above core features. You can use it to wrap a `R` row
around every `gs` elements and then construct an `A` around those rows.
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
data Box(int hs=1, int vs=0, int is=4)
    = _H(list[Box] boxes)
    | _V(list[Box] boxes)
    | _HOV(list[Box] boxes)
    | _HV(list[Box] boxes)
    | _I(list[Box] boxes) 
    | _WD(list[Box] boxes)
    | _A(list[Row] rows, Box rs=NULL(), list[Alignment] columns=[])
    | _AG(list[Box] boxes, int gs=2, list[Alignment] columns=[], Box rs=NULL())
    | SPACE(int space)
    | L(str word)
    | _U(list[Box] boxes)
    | _G(list[Box] boxes, bool backwards=false, int gs=2, Box op = H([]))
    | NULL()
    ;

Box H(Box boxes..., int hs=1) = _H(boxes, hs=hs);
Box V(Box boxes..., int vs=0) = _V(boxes, vs=vs);
Box HOV(Box boxes..., int hs=1, int vs=0) = _HOV(boxes, hs=hs, vs=vs);
Box HV(Box boxes..., int hs=1, int vs=0) = _HV(boxes, hs=hs, vs=vs);
Box I(Box boxes...) = _I(boxes);
Box WD(Box boxes...) = _WD(boxes);
Box A(Row rows..., Box rs=NULL(), list[Alignment] columns=[])
    = _A(rows, rs=rs, columns=columns);
Box AG(Box boxes..., int gs=2, list[Alignment] columns=[], Box rs=NULL())
    = _AG(boxes, gs=gs, columns=columns, rs=rs);
Box U(Box boxes...) = _U(boxes);
Box G(Box boxes..., bool backwards=false, int gs=2, Box op = H([]))
    = _G(boxes, backwards=backwards, gs=gs, op=op);

@synopsis{A row is a list of boxes that go into an `A` array/table.}
@description{
Rows do not have parameters. These are set on the `A` level instead,
or per cell Box. 
}
data Row = R(list[Box] cells);

// Row R(Box cells...) = _R(cells);

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

@synopsis{Convenience box for adding separators to an existing box list}
@description{
Each element is wrapped by the `op` operator together with the next separator.
The resulting list is wrapped by a G box, of which the elements will be spliced
into their context. 
}
Box SL(list[Box] boxes, Box sep, Box op = H([], hs=0))
  = G([b, sep | b <- boxes][..-1], op=op, gs=2);

@synopsis{Flatten and fold U and G boxes to simplify the Box structure}
@description{
U and G and AG boxes greatly simplify the Box tree before it is formatted. This
happens "just-in-time" for efficiency reasons. However, from a Box tree
with many U and G boxes it can become hard to see what the actual formatting
constraints are going to be. 

This function applies the semantics of G and U and returns a Box that renders
exactly the same output, but with a lot less nested structure. 
}
@benefits{
* useful to debug complex `toBox` mappings
* formatting semantics preserving transformation
}
@pitfalls{
* only useful for debugging purposes, because it becomes a pipeline bottleneck otherwise.
}
Box debUG(Box b) {
    list[Box] groupBy([], int _gs, Box _op) = [];
    list[Box] groupBy(list[Box] boxes:[Box _, *_], int gs, Box op)
        = [op[boxes=boxes[..gs]], *groupBy(boxes[gs..], gs, op)];

    list[Box] groupByBackward([], int _gs, Box _op) = [];
    list[Box] groupByBackward(list[Box] boxes:[Box _, *_], int gs, Box op)
        = [op[boxes=boxes[..size(boxes) mod gs]], *groupBy(boxes[size(boxes) mod gs..], gs, op)];
        
    list[Row] groupRows([], int _gs) = [];
    list[Row] groupRows(list[Box] boxes:[Box _, *_], int gs)
        = [R(boxes[..gs]), *groupRows(boxes[gs..], gs)];

    return innermost visit(b) {
        case [*Box pre, _U([*Box mid]), *Box post]           => [*pre, *mid, *post]
        case _G(list[Box] boxes, gs=gs, op=op, backwards=bw) => _U(bw ? groupByBackward(boxes, gs, op) : groupBy(boxes, gs, op))
        case _AG(list[Box] boxes, gs=gs, columns=cs, rs=rs)  => A(groupRows(boxes, gs), columns=cs, rs=rs)
    }
}