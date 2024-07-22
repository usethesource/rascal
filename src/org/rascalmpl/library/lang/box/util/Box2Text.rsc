@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@synopsis{Two-dimensional text layout algorithm}
@description{
The input to Box2Text is a hierarchy of "Boxes" represented by the Box algebraic data-type.
These boxes put hard and soft relative positioning constraints on the embedded text fragments, and
there is the global soft constraints of the width of the screen (or the paper). Box2Text can also
add markup for syntax highlighting in either ANSI plaintext encoding, HTML font tags or LaTex macros.

This implementation is a port from ASF+SDF to Rascal. The ASF+SDF implementation was published as 
"From Box to Tex:An algebraic approach to the construction of documentation tools" by Mark van den Brand 
and Eelco Visser (June 30, 1994). The original Box concept was introduced by Joel Coutaz as this technical report:
"The Box, A Layout Abstraction for User Interface Toolkits" (1984) Pittsburgh, PA: Carnegie Mellon University.

The main function `format` maps a Box tree to a `str`:
* To obtain Box terms people typically transform ASTs or ((ParseTree))s to Box using pattern matching in Rascal.
* ((Options)) encode global default options for constraint parameters that only override local parameters if they were elided.
}
@examples{
This demonstrates the semantics of the main hard constraints:
* `H` for horizontal;
* `V` for vertical;
* `I` for indentation.

```rascal-shell
import lang::box::util::Box2Text;
import lang::box::\syntax::Box;
format(H([L("A"), L("B"), L("C")], hs=2))
format(H([L("A"), L("B"), L("C")], hs=1))
format(H([L("A"), L("B"), L("C")], hs=0))
format(V([L("A"), L("B"), L("C")], vs=2))
format(V([L("A"), L("B"), L("C")], vs=1))
format(V([L("A"), L("B"), L("C")], vs=0))
format(H([L("A"), V([L("B"), L("C")])]))
format(H([L("A"), I([L("B")]), L("C")]))
format(H([L("A"), V([L("B"), H([L("C"), L("D")])])]))
```

The "soft" constraints change their behavior based on available horizontal room:
```rascal-shell,continue
format(HV([L("W<i>") | i <- [0..10]]));
format(HV([L("W<i>") | i <- [0..20]]));
format(HV([L("W<i>") | i <- [0..40]]));
format(HV([L("W<i>") | i <- [0..80]]));
format(HV([L("W<i>") | i <- [0..100]]));
format(HOV([L("W<i>") | i <- [0..10]]));
format(HOV([L("W<i>") | i <- [0..20]]));
format(HOV([L("W<i>") | i <- [0..30]]));
```

By cleverly combining constraints, a specifically desired behavior is easy to achieve:
```rascal-shell,continue
format(H([L("if"), H([L("("), L("true"), L(")")], hs=0), HOV([L("doSomething")])]))
format(H([L("if"), H([L("("), L("true"), L(")")], hs=0), HOV([L("W<i>") | i <- [0..30]])]))
format(H([L("if"), H([L("("), L("true"), L(")")], hs=0), HV([L("W<i>") | i <- [0..30]])]))
```
}
module lang::box::util::Box2Text

import util::Math;
import List;
import String;
import lang::box::\syntax::Box;

@synopsis{Converts boxes into a string by finding an "optimal" two-dimensional layout}
@description{
* This algorithm never changes the left-to-right order of the Boxes constituents, such that
syntactical correctness is maintained
* This algorithm tries to never over-run the `maxWidth` parameter, but if it must to maintain 
text order, and the specified nesting of boxes, it will anyway. For example, if a table column doesn't
fit it will still be printed. We say `maxWidth` is a _soft_ constraint.
* Separator options like `i`, `h` and `v` options are _hard_ constraints, they may lead to overriding `maxWidth`.
* H, V and I boxes represent hard constraints too.
* HV and HOV are the soft constraints that allow for better solutions, so use them where you can to allow for 
flexible layout that can handle deeply nested expressions and statements.
} 
public str format(Box b, int maxWidth=80, int wrapAfter=70)
    = "<for (line <- box2text(b, maxWidth=maxWidth, wrapAfter=wrapAfter)) {><line>
      '<}>";

@synopsis{Box2text uses list[str] as intermediate representation of the output during formatting}
@benefits{
* Helps with fast concatenation
* Allows for measuring (max) width and height of intermediate results very quickly
}
@pitfalls{
* Because of this representation, box2text does not recognize that unprintable characters have width 0. So,
ANSI escape codes, and characters like \r and \n in `L` boxes _will break_ the accuracy of the algorithm.
}
alias Text = list[str];

@synopsis{Converts boxes into list of lines (Unicode)}      
public Text box2text(Box b, int maxWidth=80, int wrapAfter=70) 
    = box2data(b, options(maxWidth=maxWidth, wrapAfter=wrapAfter));

////////// private functions below implement the intermediate data-structures
////////// and the constraint solver

@synopsis{Configuration options for a single formatting run.}
@description{
This is used during the algorithm, not for external usage.

* `hs` is the current separation between every horizontal element in H, HV and HOV boxes
* `vs` is the current separation between vertical elements in V, HV and HOV boxes
* `is` is the default (additional) indentation for indented boxes
* `maxWidth` is the number of columns (characters) of a single line on screen or on paper
* `wrapAfter` is the threshold criterium for line fullness, to go to the next line in a HV box and to switching 
between horizontal and vertical for HOV boxes.
}
data Options = options(
    int hs = 1, 
    int vs = 0, 
    int is = 2, 
    int maxWidth=80, 
    int wrapAfter=70
);

@synopsis{Quickly splice in any nested U boxes}
list[Box] u(list[Box] boxes) {
    return [*((U(list[Box] nested) := b) ? u(nested) : [b]) | b <- boxes];
}

@synopsis{simple vertical concatenation (every list element is a line)}
private Text vv(Text a, Text b) = [*a, *b];

@synopsis{Create a string of spaces just as wide as the parameter a}
private str blank(str a) = right("", width(a));

@synopsis{Computes a white line with the length of the last line of a}
Text wd([])             = [];
Text wd([*_, str x])    = [blank(x)];

@synopsis{Computes the length of unescaped string s}
private int width(str s) = size(s); 
     
@synopsis{Computes the maximum width of text t}
private int twidth([]) = 0;
private default int twidth(Text t) = max([width(line) | line <- t]);
     
@synopsis{Computes the length of the last line of t}
private int hwidth([])             = 0;
private int hwidth([*_, str last]) = width(last);

@synopsis{Prepends str a before text b, all lines of b will be shifted}
private Text bar(str a, [])                = [a];
private Text bar(str a, [str bh, *str bt]) = vv(["<a><bh>"], prepend(blank(a), bt));

@synopsis{Produce text consisting of a white line of length  n}
Text hskip(int n) = [right("", n)];
    
@synopsis{Produces text consisting of n white lines at length 0}
private Text vskip(int n) = ["" | _ <- [0..n]];

@synopsis{Prepend Every line in b with `a`}
private Text prepend(str a, Text b) = ["<a><line>" | line <- b];

@synopsis{Implements horizontal concatenation, also for multiple lines}
private Text hh([], Text b)  = b;
private Text hh(Text a, [])  = a;
private Text hh([a], Text b) = bar(a, b);

private default Text hh(Text a, Text b) = vv(a[0..-1], bar(a[-1], b));
        
@synsopsis{Horizontal concatenation, but if the left text is empty return nothing.}
private Text lhh([], Text _) = [];
private default Text lhh(a, b) = hh(a, b);

@synsopsis{Horizontal concatenation, but if the right text is empty return nothing.}
private Text rhh(Text _, []) = [];
private Text rhh(Text a, Text b) = hh(a, b);

@synsopsis{Vertical concatenation, but if the right text is empty return nothing.}
private Text rvv(Text _, []) = [];
private default Text rvv(Text a, Text b) = vv(a,b);
    
private Text LL(str s ) = [s]; 
   
private Text HH([], Box _, Options _opts, int _m) = [];

private Text HH(list[Box] b:[_, *_], Box _, Options opts, int m) {
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        Text t = \continue(a, H([]), opts, m);
        int s = hwidth(t); 
        r = hh(t, rhh(hskip(opts.hs), r));
        m  = m - s - opts.hs;
    }
   
    return r;
}

private Text VV([], Box _c, Options _opts, int _m) = [];

private Text VV(list[Box] b:[_, *_], Box c, Options opts, int m) {
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        if (V(_) !:= c || L("") !:= a) {
            Text t = \continue(a, V([]), opts, m);
            r = vv(t, rvv(vskip(opts.vs), r));
        }
    }
    return r;
   }

private Text II([], Box _c, Options _opts, int _m) = [];

private Text II(list[Box] b:[_, *_], c:H(list[Box] _), Options opts, int m) = HH(b, c, opts, m);

private Text II(list[Box] b:[Box head, *Box tail], c:V(list[Box] _), Options opts, int m) {
    Text t = \continue(head, c, opts, m - opts.is);
    return rhh(hskip(opts.is),  hh(t, II(tail, c, opts, m - opts.is - hwidth(t))));
}

private Text WDWD([], Box _c , Options _opts, int _m) 
    = [];

private Text WDWD([Box head, *Box tail], Box c , Options opts, int m) {
    int h  = head.hs ? opts.hs;
    Text t = \continue(head, c, opts, m);
    int s  = hwidth(t);
    return  hh(wd(t), rhh(hskip(h) , WDWD(tail, c, opts, m - s - h)));
}

private Text ifHOV([], Box b,  Box c, Options opts, int m) = [];

private Text ifHOV(Text t:[str head], Box b,  Box c, Options opts, int m) 
    = width(head) <= m ? t : \continue(b, c, opts, m);

private Text ifHOV(Text t:[str head, str _, *str_], Box b,  Box c, Options opts, int m)
    = \continue(b, c, opts, m);

private Text HOVHOV(list[Box] b, Box c, Options opts, int m) 
    = ifHOV(HH(b, c, opts, m), V(b), c, opts, m);

/* Gets complicated HVHV */
private Text HVHV(Text T, int s, Text a, Box A, list[Box] B, Options opts, int m) {
    int h= opts.hs;
    int v = opts.vs;
    int i= opts.is;
    int n = h + hwidth(a);

    if (size(a) > 1) { // Multiple lines 
        Text T1 = \continue(A, V([]), opts, m-i);
        return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
    }

    if (n <= s) {  // Box A fits in current line
        return HVHV(hh(lhh(T, hskip(h)), a), s-n, B, opts, m, H([]));
    }
    else {
        n -= h; // n == width(a)
        if  (i + n < m) { // Fits in the next line, not in current line
            Text T1 =\continue(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-n-i, B, opts, m, H([]))));
        }
        else { // Doesn't fit in either lines
            Text T1 = \continue(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
        }
    }
}

private Text HVHV(Text T, int _s, [], Options _opts,  int _m, Box _c) = T;
    
private Text HVHV(Text T, int s, [Box head, *Box tail], Options opts,  int m, Box c) {
    Text T1 = \continue(head, c  , opts, s);  
    return HVHV(T, s, T1 , head,  tail, opts, m);
}

private Text HVHV([], Box _, Options opts, int m) 
    = [];

private Text HVHV(list[Box] b:[Box head], Box _, Options opts, int m) 
    = \continue(head, V([]), opts, m);

private Text HVHV(list[Box] b:[Box head, Box next, *Box tail], Box _, Options opts, int m) {
    Text T =  \continue(head, V([]), opts, m);  
    return HVHV(T, m - hwidth(T), [next, *tail], opts, m, H([]));
}

// empty lists do not need grouping
private Text GG([], Box(list[Box]) op, int gs, Box c, Options opts, int m)
    = \continue(U([]), c, opts, m);

// the last elements are smaller than the group size, just wrap them up and finish
private Text GG([*Box last], Box(list[Box]) op, int gs, Box c, Options opts, int m) 
    = \continue(op(u(last))[hs=opts.hs][vs=opts.vs][is=opts.is], c, opts, m)
    when size(last) < gs;

// we pick the head of (size group size) and then continue with the rest
private Text GG([*Box heads, *Box tail], Box(list[Box]) op, int gs, Box c, Options opts, int m) 
    = \continue(op(heads)[hs=opts.hs][vs=opts.vs][is=opts.is], NULL(), opts, m)
    + \continue(G(tail, op=op, hs=opts.hs, vs=opts.vs, is=opts.is, gs=gs), c, opts, m)
    when size(heads) == gs;

private Text continueWith(Box b:L(str s)         , Box c, Options opts, int m) = LL(s);
private Text continueWith(Box b:H(list[Box] bl)  , Box c, Options opts, int m) = HH(u(bl), c, opts, m); 
private Text continueWith(Box b:V(list[Box] bl)  , Box c, Options opts, int m) = VV(u(bl), c, opts, m);
private Text continueWith(Box b:I(list[Box] bl)  , Box c, Options opts, int m) = II(u(bl), c, opts, m);
private Text continueWith(Box b:WD(list[Box] bl) , Box c, Options opts, int m) = WDWD(u(bl), c, opts, m);
private Text continueWith(Box b:HOV(list[Box] bl), Box c, Options opts, int m) = HOVHOV(u(bl), c, opts, m);
private Text continueWith(Box b:HV(list[Box] bl) , Box c, Options opts, int m) = HVHV(u(bl), c, opts, m);
private Text continueWith(Box b:SPACE(int n)     , Box c, Options opts, int m) = hskip(n);

// This is a degenerate case, an outermost U-Box without a wrapper around it.
private Text continueWith(Box b:U(list[Box] bl)  , Box c, Options opts, int m) = HH(u(bl), c, opts, m);

private Text continueWith(Box b:A(list[Row] rows), Box c, Options opts, int m) 
    = AA(rows, c, b.columns, opts, m);

private Text continueWith(Box b:G(list[Box] bl), Box c, Options opts, int m) = GG(u(bl), b.op, b.gs, c, opts, m);

@synopsis{General shape of a Box operator, as a parameter to `G`}
private alias BoxOp = Box(list[Box]);

@synopsis{Option inheritance layer; then continue with the next box.}
@description{
The next box is either configured by itself. Options are transferred from the
box to the opts parameter for easy passing on to recursive calls.
}
private Text \continue(Box b, Box c, Options opts, int m)
    = continueWith(b, c, opts[hs=b.hs][vs=b.vs][is=b.is], m);

/* ------------------------------- Alignment ------------------------------------------------------------*/

@synopsis{This is to store the result of the first pass of the algorithm over all the cells in an array/table}
data Box(int width=0, int height=1);

@synopsis{Completely layout a box and then measure its width and height, and annotate the result into the Box}
private Box boxSize(Box b, Box c, Options opts, int m) {
    Text s = \continue(b, c, opts, m);
    b.width = twidth(s);
    b.height = size(s);
    return b;
}

private list[list[Box]] RR(list[Row] bl, Box c, Options opts, int m) {
    list[list[Box]] g = [b | R(list[Box] b) <- bl]; 
    return [ [ boxSize(z, c, opts, m) | Box z <- b ] | list[Box] b <- g];
}

@synopsis{Compute the maximum number of columns of the rows in a table}
private int Acolumns(list[Row] rows) = (0 | max(it, size(row.cells)) | row <- rows);

@synopsis{Compute the maximum cell width for each column in an array}
private list[int] Awidth(list[list[Box]] rows) 
    = [(0 | max(it, row[col].width) | row <- rows ) | int col <- [0..size(head(rows))]];

@synopsis{Adds empty cells to every row until every row has the same amount of columns.}
list[Row] AcompleteRows(list[Row] rows, int columns=Acolumns(rows))
    = [ R(u([*row.cells, *[H([]) | _ <- [0..columns - size(row.cells)]]])) | row <- rows];

@synopsis{Helper function for aligning Text inside an array cell}
private Box align(l(), Box cell, int maxWidth) = maxWidth - cell.width > 0 
    ? H([cell, SPACE(maxWidth - cell.width)], hs=0)
    : cell;

private Box align(r(), Box cell, int maxWidth) = maxWidth - cell.width > 0 
    ? H([SPACE(maxWidth - cell.width), cell], hs=0)
    : cell;

private Box align(c(), Box cell, int maxWidth) = maxWidth - cell.width > 1 
    ? H([SPACE((maxWidth - cell.width) / 2),  cell, SPACE((maxWidth - cell.width) / 2)], hs=0)
    : maxWidth - cell.width == 1 ?
        align(l(), cell, maxWidth)
        : cell;

private Text AA(list[Row] table, Box c, list[Alignment] alignments, Options opts, int m) {
    list[list[Box]] rows = RR(AcompleteRows(table), c, opts, m);
    list[int] maxWidths  = Awidth(rows);
    
    return \continue(V([
        H([align(al, cell, mw) | <cell, al, mw> <- zip3(row, alignments, maxWidths)]) 
    | row <- rows
    ]),c, opts, m);
}

@synopsis{Check soft limit for HV and HOV boxes}
// TODO this seems to ignore SPACE boxes?
private bool noWidthOverflow(list[Box] hv, Options opts) 
    = (0 | it + size(s) | /L(s) := hv) < opts.wrapAfter;

@synopsis{Changes all HV boxes that do fit horizontally into hard H boxes.}
private Box applyHVconstraints(Box b, Options opts) = innermost visit(b) {
    case HV(boxes, hs=h, is=i, vs=v) => H(boxes, hs=h, is=i, vs=v) 
        when noWidthOverflow(boxes, opts)
};

@synopsis{Changes all HOV boxes that do fit horizontally into hard H boxes,
and the others into hard V boxes.}
private Box applyHOVconstraints(Box b, Options opts) = innermost visit(b) {
    case HOV(boxes, hs=h, is=i, vs=v) => noWidthOverflow(boxes, opts) 
        ? H(boxes, hs=h, is=i, vs=v)
        : V(boxes, hs=h, is=i, vs=v)      
};

@synopsis{Workhorse, that first applies hard HV and HOV limits and then starts the general algorithm}
private Text box2data(Box b, Options opts) {
    b = applyHVconstraints(b, opts);
    b = applyHOVconstraints(b, opts);
    return \continue(b, V([]), options(), opts.maxWidth);
}
    
///////////////// regression tests ////////////////////////////////

test bool horizontalPlacement2()
    = format(H([L("A"), L("B"), L("C")], hs=2))
    == "A  B  C
       '";

test bool horizontalPlacement3()
    = format(H([L("A"), L("B"), L("C")], hs=3))
    == "A   B   C
       '";

test bool verticalPlacement0()
    = format(V([L("A"), L("B"), L("C")], vs=0))
    == "A
       'B
       'C
       '";

test bool verticalPlacement1()
    = format(V([L("A"), L("B"), L("C")], vs=1))
    == "A
       '
       'B
       '
       'C
       '";

test bool verticalIndentation2()
    = format(V([L("A"), I([L("B")]), L("C")]))
    == "A
       '  B
       'C
       '";

test bool blockIndent()
    = format(V([L("A"), I([V([L("B"), L("C")])]), L("D")]))
    == "A
       '  B
       '  C
       'D
       '";

test bool wrappingIgnoreIndent()
    = format(HV([L("A"), I([L("B")]), L("C")], hs=0), maxWidth=2, wrapAfter=2)
    == "AB
       'C
       '";

test bool wrappingWithIndent()
    = format(HV([L("A"), I([L("B")]), I([L("C")])], hs=0), maxWidth=2, wrapAfter=2)
    == "AB
       '  C
       '";

test bool flipping1NoIndent()
    = format(HOV([L("A"), L("B"), L("C")], hs=0, vs=0), maxWidth=2, wrapAfter=2)
    == "A
       'B
       'C
       '";

test bool horizontalOfOneVertical()
    = format(H([L("A"), V([L("B"), L("C")])]))
    == "A B
       '  C
       '";

test bool stairCase()
    = format(H([L("A"), V([L("B"), H([L("C"), V([L("D"), H([L("E"), L("F")])])])])]))
    == "A B
       '  C D
       '    E F
       '";

test bool simpleTable() 
    = format(A([R([L("1"),L("2"),L("3")]),R([L("4"), L("5"), L("6")]),R([L("7"), L("8"), L("9")])]))
    == "1 2 3
       '4 5 6
       '7 8 9
       '";

test bool simpleAlignedTable() 
    = format(A([R([L("1"),L("2"),L("3")]),R([L("44"), L("55"), L("66")]),R([L("777"), L("888"), L("999")])], 
                columns=[l(),c(),r()]))
    == "1    2    3
       '44  55   66
       '777 888 999
       '";

test bool simpleAlignedTableDifferentAlignment() 
    = format(A([R([L("1"),L("2"),L("3")]),R([L("44"), L("55"), L("66")]),R([L("777"), L("888"), L("999")])], 
                columns=[r(),c(),l()]))
    == "  1  2  3  
       ' 44 55  66 
       '777 888 999
       '";

test bool WDtest() {
    L1 = H([L("aap")]           , hs=0);
    L2 = H([WD([L1]), L("noot")], hs=0);
    L3 = H([WD([L2]), L("mies")], hs=0);

    return format(V([L1, L2, L3]))
        == "aap
           '   noot
           '       mies
           '";
}

test bool groupBy() {
    lst  = [L("<i>") | i <- [0..10]];
    g1   = G(lst, op=H, gs=3);
    lst2 = [H([L("<i>"), L("<i+1>"), L("<i+2>")]) | i <- [0,3..7]] + [H([L("9")])];

    return format(V([g1])) == format(V(lst2));
}