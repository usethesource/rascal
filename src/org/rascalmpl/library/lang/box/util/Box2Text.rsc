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
The input to Box2Text is a hierarchy of "Boxes" represented by the ((lang::box::\syntax::Box)) algebraic data-type.
These boxes put hard and soft relative positioning constraints on the embedded text fragments, and
there is the global soft constraints of the width of the screen (or the paper). 


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
format(H(L("A"), L("B"), L("C"), hs=2))
format(H(L("A"), L("B"), L("C"), hs=1))
format(H(L("A"), L("B"), L("C"), hs=0))
format(V(L("A"), L("B"), L("C"), vs=2))
format(V(L("A"), L("B"), L("C"), vs=1))
format(V(L("A"), L("B"), L("C"), vs=0))
format(H(L("A"), V(L("B"), L("C"))))
format(H(L("A"), I(L("B")]), L("C")]))
format(H(L("A"), V(L("B"), H(L("C"), L("D")))))
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
format(H(L("if"), H(L("("), L("true"), L(")"), hs=0), HOV(L("doSomething"))))
format(H(L("if"), H(L("("), L("true"), L(")"), hs=0), HOV([L("W<i>") | i <- [0..30]])))
format(H(L("if"), H(L("("), L("true"), L(")"), hs=0), HV([L("W<i>") | i <- [0..30]])))
```
}
@pitfalls{
* Box2text does not have highlighting features anymore; you can use ((util::Highlight)) for this instead.
}
module lang::box::util::Box2Text

import util::Math;
import List;
import String;
import lang::box::\syntax::Box;
import IO;

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
    int is = 4, 
    int maxWidth = 80, 
    int wrapAfter = 70
);

@synopsis{Quickly splice in any nested U boxes, and empty H, V, HV, I or HOV boxes}
list[Box] u(list[Box] boxes) {
    return [*((U_(list[Box] nested) := b) ? u(nested) : [b]) | b <- boxes, !isDegenerate(b)];
}

@synopsis{Empty H, V, HOV, HV, I boxes should not lead to accidental extra separators in their context}
private bool isDegenerate(Box b) = b has boxes && b.boxes == [];

@synopsis{simple vertical concatenation (every list element is a line)}
private Text vv(Text a, Text b) = [*a, *b];

@synopsis{Create a string of spaces just as wide as the parameter a}
private str blank(str a) = right("", size(a));

@synopsis{Computes a white line with the length of the last line of a}
Text wd([])             = [];
Text wd([*_, str x])    = [blank(x)];
     
@synopsis{Computes the maximum width of text t}
private int twidth([]) = 0;
private default int twidth(Text t) = max([size(line) | line <- t]);
     
@synopsis{Computes the length of the last line of t}
private int hwidth([])             = 0;
private int hwidth([*_, str last]) = size(last);

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
    
private Text LL(str s) {
    assert s != "" : "literal strings must never be empty for Box2Text to work correctly.";
    return [s];
}

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

private Text GG(list[Box] boxes, Box c, Options opts, int m, int gs, Box op, bool backwards)
    = \continue(c[boxes=groupBy(boxes, gs, op, backwards)], c, opts, m);

public list[Box] groupBy(list[Box] boxes, int gs, Box op, false) = groupBy(boxes, gs, op);

@synopsis{simulates grouping as-if done from the back, by starting to peel off the rest instead of grouping the rest at the end}
public list[Box] groupBy(list[Box] boxes, int gs, Box op, true) 
    = [op[boxes=boxes[..size(boxes) mod gs]], *groupBy(boxes[size(boxes) mod gs..], gs, op)];

public list[Box] groupBy([], int _gs, Box _op) = [];

public list[Box] groupBy(list[Box] boxes:[Box _, *_], int gs, Box op)
    = [op[boxes=boxes[..gs]], *groupBy(boxes[gs..], gs, op)];

private Text VV([], Box _c, Options _opts, int _m) = [];

private Text VV(list[Box] b:[_, *_], Box c, Options opts, int m) {
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        if (V_(_) !:= c || L("") !:= a) {
            Text t = \continue(a, V([]), opts, m);
            r = vv(t, rvv(vskip(opts.vs), r));
        }
    }
    return r;
   }

private Text II([], Box _c, Options _opts, int _m) = [];

private Text II(list[Box] b:[_, *_]              , c:H_(list[Box] _), Options opts, int m) 
    = HH(b, c, opts, m);

private Text II(list[Box] b:[Box _, *Box _], c:V_(list[Box] _), Options opts, int m) 
    = rhh(hskip(opts.is), \continue(V(b, vs=opts.vs), c, opts, m - opts.is));

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
    = size(head) <= m ? t : \continue(b, c, opts, m);

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
        return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H_([]))));
    }

    if (n <= s) {  // Box A fits in current line
        return HVHV(hh(lhh(T, hskip(h)), a), s-n, B, opts, m, H_([]));
    }
    else {
        n -= h; // n == size(a)
        if  (i + n < m) { // Fits in the next line, not in current line
            Text T1 =\continue(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-n-i, B, opts, m, H_([]))));
        }
        else { // Doesn't fit in either lines
            Text T1 = \continue(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H_([]))));
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
    = \continue(head, V_([]), opts, m);

private Text HVHV(list[Box] b:[Box head, Box next, *Box tail], Box _, Options opts, int m) {
    Text T =  \continue(head, V_([]), opts, m);  
    return HVHV(T, m - hwidth(T), [next, *tail], opts, m, H_([]));
}

private Text continueWith(Box b:L(str s)         , Box c, Options opts, int m) = LL(s);
private Text continueWith(Box b:H_(list[Box] bl)  , Box c, Options opts, int m) = HH(u(bl), c, opts, m); 
private Text continueWith(Box b:V_(list[Box] bl)  , Box c, Options opts, int m) = VV(u(bl), c, opts, m);
private Text continueWith(Box b:I_(list[Box] bl)  , Box c, Options opts, int m) = II(u(bl), c, opts, m);
private Text continueWith(Box b:WD_(list[Box] bl) , Box c, Options opts, int m) = WDWD(u(bl), c, opts, m);
private Text continueWith(Box b:HOV_(list[Box] bl), Box c, Options opts, int m) = HOVHOV(u(bl), c, opts, m);
private Text continueWith(Box b:HV_(list[Box] bl) , Box c, Options opts, int m) = HVHV(u(bl), c, opts, m);
private Text continueWith(Box b:SPACE(int n)     , Box c, Options opts, int m) = hskip(n);

// This is a degenerate case, an outermost U-Box without a wrapper around it.
private Text continueWith(Box b:U_(list[Box] bl)  , Box c, Options opts, int m) = HH(u(bl), c, opts, m);

private Text continueWith(Box b:G_(list[Box] bl), Box c, Options opts, int m)
    = GG(u(bl), c, opts, m,  b.gs, b.op, b.backwards);

private Text continueWith(Box b:A_(list[Row] rows), Box c, Options opts, int m) 
    = AA(rows, c, b.columns, b.rs, opts, m);

private Text continueWith(Box b:AG_(list[Box] boxes), Box c, Options opts, int m) 
    = AAG(u(boxes), b.gs, b.columns, b.rs, c, opts, m);

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
    = [(0 | max(it, row[col].width) | row <- rows, col < size(row) ) | int col <- [0..size(head(rows))]];

@synopsis{Adds empty cells to every row until every row has the same amount of columns.}
list[Row] AcompleteRows(list[Row] rows, int columns=Acolumns(rows), Box rs=NULL())
    = [ R(u([*row.cells[..-1], H_([row.cells[-1], rs],hs=0), *[SPACE(1) | _ <- [0..columns - size(row.cells)]]])) | row <- rows[..-1]]
    + [ R(u([*rows[-1].cells, *[SPACE(1) | _ <- [0..columns - size(rows[-1].cells)]]]))] ;

@synopsis{Helper function for aligning Text inside an array cell}
private Box align(l(), Box cell, int maxWidth) = maxWidth - cell.width > 0 
    ? H_([cell, SPACE(maxWidth - cell.width)], hs=0)
    : cell;

private Box align(r(), Box cell, int maxWidth) = maxWidth - cell.width > 0 
    ? H_([SPACE(maxWidth - cell.width), cell], hs=0)
    : cell;

private Box align(c(), Box cell, int maxWidth) = maxWidth - cell.width > 1 
    ? H_([SPACE((maxWidth - cell.width) / 2),  cell, SPACE((maxWidth - cell.width) / 2)], hs=0)
    : maxWidth - cell.width == 1 ?
        align(l(), cell, maxWidth)
        : cell;

private Text AA(list[Row] table, Box c, list[Alignment] alignments, Box rs, Options opts, int m) {
    if (table == []) {
        return [];
    }

    // first flatten any nested U cell lists into the Rows
    table = [R(u(r.cells)) | Row r <- table];

    // we remove any H-V backtracking because table cells are too small anyway, generally.
    // so we prefer the less wide V over HOV and HV. This boosts efficiency radically, because
    // later, ever cell will be formatted individually to an optimal width, and measured, before we even start
    // to format the table. Then the same cells will be formatted again from scratch. By removing the
    // backtracking, larger tables (like reified grammars) become doable.
    table = visit (table) {
        case Box b:HOV_(list[Box] boxes) => V_(boxes, vs=b.vs)
        case Box b:HV_(list[Box] boxes)  => V_(boxes, vs=b.vs)
    }

    // then we can know the number of columns
    int maxColumns = Acolumns(table);

    // then we fill each row up to the maximum of columns
    list[list[Box]] rows = RR(AcompleteRows(table, columns=maxColumns, rs=rs), c, opts, m);

    // and we infer alignments where not provided
    alignments = AcompleteAlignments(alignments, maxColumns);

    // finally we compute alignment information
    list[int] maxWidths  = Awidth(rows);

    try {
        // A row is simply an H box where each cell is filled with enough spaces to align for the next column
        return \continue(V_([ 
            H_([align(al, cell, mw) | <cell, al, mw> <- zip3(row, alignments, maxWidths)]) | row <- rows]), c, opts, m);
    }
    catch IllegalArgument(_, "List size mismatch"): {
        throw IllegalArgument("Array alignments size is <size(alignments)> while there are <size(rows[0])> columns.");
    }
}

private Text AAG([], int _gs, list[Alignment] _columns, Box _rs, Box _c, Options _opts, int _m) = [];

private Text AAG(list[Box] boxes:[Box _, *_], int gs, list[Alignment] columns, Box rs, Box c, Options opts, int m)
    = \continue(A(groupRows(boxes, gs), columns=columns, rs=rs), c, opts, m);

private list[Row] groupRows([], int _gs) = [];

private list[Row] groupRows(list[Box] boxes:[Box _, *_], int gs)
    = [R(boxes[..gs]), *groupRows(boxes[gs..], gs)];

@synopsis{Cuts off and extends the alignment spec to the width of the table}
@description{
* if too few columns are specified: `l()`'s are added accordingly
* if too many columns are specified: they are cut off from the right
}
private list[Alignment] AcompleteAlignments(list[Alignment] alignments, int maxColumns) 
    = [*alignments[..maxColumns], *[l() | _ <- [0..maxColumns - size(alignments)]]];

@synopsis{Check soft limit for HV and HOV boxes}
// TODO this seems to ignore SPACE boxes?
private bool noWidthOverflow(list[Box] hv, Options opts) 
    = (0 | it + size(s) | /L(s) := hv) < opts.wrapAfter;

@synopsis{Changes all HV boxes that do fit horizontally into hard H boxes.}
private Box applyHVconstraints(Box b, Options opts) = innermost visit(b) {
    case HV_(list[Box] boxes, hs=h, is=i, vs=v) => H_(boxes, hs=h, is=i, vs=v) 
        when noWidthOverflow(boxes, opts)
};

@synopsis{Changes all HOV boxes that do fit horizontally into hard H boxes,
and the others into hard V boxes.}
private Box applyHOVconstraints(Box b, Options opts) = innermost visit(b) {
    case HOV_(list[Box] boxes, hs=h, is=i, vs=v) => noWidthOverflow(boxes, opts) 
        ? H_(boxes, hs=h, is=i, vs=v)
        : V_(boxes, hs=h, is=i, vs=v)      
};

@synopsis{Workhorse, that first applies hard HV and HOV limits and then starts the general algorithm}
private Text box2data(Box b, Options opts) {
    b = applyHVconstraints(b, opts);
    b = applyHOVconstraints(b, opts);
    return \continue(b, V_([]), options(), opts.maxWidth);
}
    
///////////////// regression tests ////////////////////////////////

test bool horizontalPlacement2()
    = format(H(L("A"), L("B"), L("C"), hs=2))
    == "A  B  C
       '";

test bool horizontalPlacement3()
    = format(H(L("A"), L("B"), L("C"), hs=3))
    == "A   B   C
       '";

test bool horizontalIndentIsNoop1() 
    = format(H(L("A"), I(L("B"))))
    == "A B
       '";

test bool horizontalIndentIsNoop2() 
    = format(HV(L("A"), I(L("B"))))
    == "A B
       '";

test bool horizontalIndentIsNoop3() 
    = format(HOV(L("A"), I(L("B"))))
    == "A B
       '";

test bool emptyBoxesNoExtraSpacing1()
    = format(H(L("A"), H(), L("B")))
    == "A B
       '";

test bool emptyBoxesNoExtraSpacing2()
    = format(H(L("A"), V(), L("B")))
    == "A B
       '";

test bool emptyBoxesNoExtraSpacing3()
    = format(H(L("A"), I(), L("B")))
    == "A B
       '";

test bool emptyBoxesNoExtraSpacing4()
    = format(V(L("A"), H(), L("B")))
    == "A
       'B
       '";

test bool emptyBoxesNoExtraSpacing5()
    = format(V(L("A"), V(), L("B")))
    == "A
       'B
       '";

test bool verticalPlacement0()
    = format(V(L("A"), L("B"), L("C"), vs=0))
    == "A
       'B
       'C
       '";

test bool verticalPlacement1()
    = format(V(L("A"), L("B"), L("C"), vs=1))
    == "A
       '
       'B
       '
       'C
       '";

test bool verticalIndentation2()
    = format(V(L("A"), I(L("B")), L("C")))
    == "A
       '    B
       'C
       '";

test bool blockIndent()
    = format(V(L("A"), I(V(L("B"), L("C"))), L("D")))
    == "A
       '    B
       '    C
       'D
       '";

test bool wrappingIgnoreIndent()
    = format(HV(L("A"), I(L("B")), L("C"), hs=0), maxWidth=2, wrapAfter=2)
    == "AB
       'C
       '";

test bool wrappingWithIndent()
    = format(HV(L("A"), I(L("B")), I(L("C")), hs=0), maxWidth=2, wrapAfter=2)
    == "AB
       '    C
       '";

test bool multiBoxIndentIsVertical()
    = format(I(L("A"), L("B")))
    == "    A
       '    B
       '";

test bool flipping1NoIndent()
    = format(HOV(L("A"), L("B"), L("C"), hs=0, vs=0), maxWidth=2, wrapAfter=2)
    == "A
       'B
       'C
       '";

test bool horizontalOfOneVertical()
    = format(H(L("A"), V(L("B"), L("C"))))
    == "A B
       '  C
       '";

test bool stairCase()
    = format(H(L("A"), V(L("B"), H(L("C"), V(L("D"), H(L("E"), L("F")))))))
    == "A B
       '  C D
       '    E F
       '";

test bool simpleTable() 
    = format(A(R([L("1"),L("2"),L("3")]),R([L("4"), L("5"), L("6")]),R([L("7"), L("8"), L("9")])))
    == "1 2 3
       '4 5 6
       '7 8 9
       '";

test bool simpleAlignedTable() 
    = format(A(R([L("1"),L("2"),L("3")]),R([L("44"), L("55"), L("66")]),R([L("777"), L("888"), L("999")]), 
                columns=[l(),c(),r()]))
    == "1    2    3
       '44  55   66
       '777 888 999
       '";

test bool simpleAlignedTableDifferentAlignment() 
    = format(A(R([L("1"),L("2"),L("3")]),R([L("44"), L("55"), L("66")]),R([L("777"), L("888"), L("999")]), 
                columns=[r(),c(),l()]))
    == "  1  2  3  
       ' 44 55  66 
       '777 888 999
       '";

test bool WDtest() {
    L1 = H(L("aap")         , hs=0);
    L2 = H(WD(L1), L("noot"), hs=0);
    L3 = H(WD(L2), L("mies"), hs=0);

    return format(V(L1, L2, L3))
        == "aap
           '   noot
           '       mies
           '";
}

test bool groupByTest() {
    lst  = [L("<i>") | i <- [0..10]];
    g1   = G(lst, op=H(), gs=3);
    lst2 = [H(L("<i>"), L("<i+1>"), L("<i+2>")) | i <- [0,3..7]] + [H(L("9"))];

    return format(V(g1)) == format(V(lst2));
}

test bool groupByBackwardsTest() {
    lst  = [L("<i>") | i <- [0..10]];
    g1   = G(lst, op=H(), gs=3, backwards=true);
    lst2 = [H(L("0"))] + [H(L("<i>"), L("<i+1>"), L("<i+2>")) | i <- [1, 4..10]];

    return format(V([g1])) == format(V(lst2));
}

test bool noDegenerateHVSeparators1()
    = format(HV(L("a"),V(),L("b"))) 
    == "a b
       '";

test bool noDegenerateHVSeparators2()
    = format(HV(L("a"),V(),L("b")), maxWidth=1, wrapAfter=1) 
    == "a
       'b
       '";

test bool noDegenerateHOVSeparators1()
    = format(HOV(L("a"),V(),L("b"))) 
    == "a b
       '";

test bool noDegenerateHOVSeparators2()
    = format(HOV(L("a"),V(),L("b")), maxWidth=1, wrapAfter=1) 
    == "a
       'b
       '";
