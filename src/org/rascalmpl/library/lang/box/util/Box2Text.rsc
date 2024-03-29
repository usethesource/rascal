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
The input to Box2Text is a hierarchy of "Boxes" represented by the ((data:Box)) algebraic data-type.
These boxes put hard and soft relative positioning constraints on the embedded text fragments, and
there is the global soft constraints of the width of the screen (or the paper). Box2Text can also
add markup for syntax highlighting in either ANSI plaintext encoding, HTML font tags or LaTex macros.

This implementation is a port from ASF+SDF to Rascal. The ASF+SDF implementation was published as 
"From Box to Tex:An algebraic approach to the construction of documentation tools" by Mark van den Brand 
and Eelco Visser (June 30, 1994). The original Box concept was introduced by Joel Coutaz as this technical report:
"The Box, A Layout Abstraction for User Interface Toolkits" (1984) Pittsburgh, PA: Carnegie Mellon University.

The main function `format` maps a ((data:Box)) tree to a `str`:
* To obtain ((data:Box)) terms people typically transform ASTs or ((ParseTree))s to ((data:Box)) using pattern matching in Rascal.
* ((Options)) encode global default options for constraint parameters that only override local parameters if they were elided.
* ((MarkupLanguage)) configures which markup language to use for syntax highlighting purposes.
}
@examples{
This demonstrates the semantics of the main hard constraints:
* `H` for horizontal;
* `V` for vertical;
* `I` for indentation.

```rascal-shell
import lang::box::util::Box2Text;
import lang::box::util::Box;
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

import List;
import String;
import lang::box::util::Box;

@synopsis{Global options for a single formatting run.}
@description{
* `hs` is the default separation between every horizontal element in H, HV and HOV boxes
* `vs` is the default separation between vertical elements in V, HV and HOV boxes
* `is` is the default (additional) indentation for indented boxes
* `maxWidth` is the number of columns (characters) of a single line on screen or on paper
* `wrapAfter` is the threshold criterium for line fullness, to go to the next line in a HV box and to switching 
between horizontal and vertical for HOV boxes.
}
@benefits{
* if any of these options, with the same name and intent, are set on a Box constructor, the latter take precedence.
}
@pitfalls{
* default options are also set on the Box constructors; they are not equal to these and set to `-1` to avoid
any confusion. 
}
data Options = options(
    int hs = 1, 
    int vs = 0, 
    int is = 2, 
    int maxWidth=80, 
    int wrapAfter=70,
    MarkupLanguage markup=ansi()
);

@synopsis{Option values for the highlighting markup features of the FONT and KW boxes, etc.}
@description{
(((TODO))) 
}
data MarkupLanguage 
    = html()
    | ansi()
    | none()
    ;

@synopsis{Converts boxes into a string by finding an "optimal" two-dimensional layout}
@description{
* This algorithm never changes the left-to-right order of the Boxes constituents, such that
syntactical correctness is maintained
* This algorithm tries not never over-run the maxWidth parameter, but if it must to maintain 
text order, and the specified nesting of boxes, it will anyway. For example, if a table column doesn't
fit it will still be printed. We say `maxWidth` is a _soft_ constraint.
* Separator options like `i`, `h` and `v` options are _hard_ constraints, they may lead to overriding `maxWidth`.
* H, V and I boxes represent hard constraints too.
* HV and HOV are the soft constraints that allow for better solutions, so use them where you can to allow for 
flexible layout that can handle deeply nested expressions and statements.
} 
public str format(Box b, Options opts = options())
    = "<for (line <- box2text(b, opts=opts)) {><line>
      '<}>";

@synopsis{Converts boxes into list of lines (ASCII)}      
public Text box2text(Box b, Options opts=options()) = box2data(b, opts);
    
@synopsis{simple vertical concatenation (every list element is a line)}
private Text vv(Text a, Text b) = [*a, *b];

@synopsis{Create a string of spaces just as wide as the parameter a}
private str blank(str a) = right("", width(a));

@synopsis{Computes a white line with the length of the last line of a}
private Text wd([])             = [];
private Text wd([*_, str x])    = wd([x]);

@synopsis{Computes the length of unescaped string s}
private int width(str s) = size(s); 
     
@synopsis{Computes the maximum width of text t}
private int twidth(Text t) = max([width(line) | line <- t]);
     
@synopsis{Computes the length of the last line of t}
private int hwidth([])             = 0;
private int hwidth([*_, str last]) = width(last);

@synopsis{Prepends str a before text b, all lines of b will be shifted}
private Text bar(str a, [])                = [a];
private Text bar(str a, [str bh, *str bt]) = vv(["<a><bh>"], prepend(blank(a), bt));

@synopsis{Produce text consisting of a white line of length  n}
private Text hskip(int n) = [right("", n)];
    
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
   
private Text HH([], Box _, Options opts, int m) = [];

private Text HH(list[Box] b:[_, *_], Box _, Options opts, int m) {
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        Text t = O(a, H([]), opts, m);
        int s = hwidth(t); 
        r = hh(t, rhh(hskip(opts.hs), r));
        m  = m - s - opts.hs;
    }
   
    return r;
}

private Text VV(list[Box] b, Box c, Options opts, int m) {
    if (isEmpty(b)) return [];
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        if (V(_)!:=c || L("")!:=a) {
            Text t = O(a, V([]), opts, m);
            r = vv(t, rvv(vskip(opts.vs), r));
        }
    }
    return r;
   }

private Text II([], Box c, Options opts, int m) = [];

private Text II(list[Box] b:[_, *_], c:H(list[Box] _), Options opts, int m) = HH(b, c, opts, m);

private Text II(list[Box] b:[Box head, *Box tail], c:V(list[Box] _), Options opts, int m) {
    Text t = O(head, c, opts, m - opts.is);
    return rhh(hskip(opts.is),  hh(t, II(tail, c, opts, m - opts.is - hwidth(t))));
}

private Text WDWD(list[Box] b, Box c , Options opts, int m) {
    if (isEmpty(b)) {
        return [];
    }
    int h  = b[0].hs?opts.hs;
    Text t = O(b[0], c, opts, m);
    int s  = hwidth(t);
    return  hh(t , rhh(hskip(h) , WDWD(tail(b), c, opts, m - s - h)));
}

private Text ifHOV(Text t, Box b,  Box c, Options opts, int m) {
    if (isEmpty(t)) {
        return [];
    }
    if (size(t)==1) {
        if (width(t[0])<=m) {
            return t;
        }
        else {
           return O(b, c, opts, m);
        }
    }
    return O(b, c, opts, m);
}

private Text HOVHOV(list[Box] b, Box c, Options opts, int m) 
    = ifHOV(HH(b, c, opts, m), V(b), c, opts, m);

/* Gets complicated HVHV */
private Text HVHV(Text T, int s, Text a, Box A, list[Box] B, Options opts, int m) {
    int h= opts.hs;
    int v = opts.vs;
    int i= opts.is;
    int n = h + hwidth(a);
    if (size(a)>1) { // Multiple lines 
        Text T1 = O(A, V([]), opts, m-i);
        return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
    }
    if (n <= s) {  // Box A fits in current line
        return HVHV(hh(lhh(T, hskip(h)), a), s-n, B, opts, m, H([]));
    }
    else {
        n -= h; // n == width(a)
        if  ((i+n)<m) { // Fits in the next line, not in current line
            Text T1 =O(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-n-i, B, opts, m, H([]))));
        }
        else { // Doesn't fit in both lines
            Text T1 =O(A, V([]), opts, m-i);
            return vv(T, rvv(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
        }
    }
}

private Text HVHV(Text T, int s, list[Box] b, Options opts,  int m, Box c) {
    if (isEmpty(b)) {
        return T;
    }
    Text T1 = O(b[0], c  , opts, s);  // Was H([])
    return HVHV(T, s, T1 , b[0],  tail(b), opts, m);
}

private Text HVHV(list[Box] b, Box _, Options opts, int m) {
    if (isEmpty(b)) {
        return [];
    }
    Text T =  O(b[0], V([]), opts, m);  // Was H([])
    if (size(b)==1) {
        return T;
    }

    return HVHV(T, m - hwidth(T), tail(b), opts, m, H([]));
}

private Text QQ(Box b:L(str s)         , Box c, Options opts, int m) = LL(s);
private Text QQ(Box b:H(list[Box] bl)  , Box c, Options opts, int m) = HH(bl, c, opts, m); 
private Text QQ(Box b:V(list[Box] bl)  , Box c, Options opts, int m) = VV(bl, c, opts, m);
private Text QQ(Box b:I(list[Box] bl)  , Box c, Options opts, int m) = II(bl, c, opts, m);
private Text QQ(Box b:WD(list[Box] bl) , Box c, Options opts, int m) = WDWD(bl, c, opts, m);
private Text QQ(Box b:HOV(list[Box] bl), Box c, Options opts, int m) = HOVHOV(bl, c, opts, m);
private Text QQ(Box b:HV(list[Box] bl) , Box c, Options opts, int m) = HVHV(bl, c, opts, m);
private Text QQ(Box b:SPACE(int n)     , Box c, Options opts, int m) = hskip(n);

private Text QQ(Box b:A(list[Box] rows), Box c, Options opts, int m) 
    = AA(rows, c, b.columns, opts, m);
     
@synopsis{Option inheritance layer.}
@description{
The next box is either configured by itself, or it inherits the options from the context.
Every recursive call to a nested box must go through this layer.
}
private Text O(Box b, Box c, Options opts, int m)
    = QQ(b, c, opts[
        hs=b.hs? ? b.hs : opts.hs][
        vs=b.vs? ? b.vs : opts.vs][
        is=b.is? ? b.is : opts.is], 
        m
    );


/* ------------------------------- Alignment ------------------------------------------------------------*/

private Box boxSize(Box b, Box c, Options opts, int m) {
    Text s = O(b, c, opts, m);
    b.width = twidth(s);
    b.height = size(s);
    return b;
}

private list[list[Box]] RR(list[Box] bl, Box c, Options opts, int m) {
    list[list[Box]] g = [b | R(list[Box] b) <- bl];
    return [ [ boxSize(z, c, opts, m) | Box z <- b ] | list[Box] b <- g];
}

private list[int] Awidth(list[list[Box]] a) {
    if (isEmpty(a)) {
        return [];
    }
    
    int m = size(head(a));  // Rows have the same length

    list[int] r = [];

    for (int k<-[0..m]) {
        r+=[max([b[k].width|b<-a])];
    }
    
    return r;
}

private Text AA(list[Box] bl, Box c, list[Alignment] columns, Options opts, int m) {
    list[list[Box]] r = RR(bl, c, opts, m);
    list[int] mw0 = Awidth(r);
    list[Box] vargs = [];

    for (list[Box] bl2 <- r) {
        list[int]  mw = mw0;
        list[Box] hargs = [];
        for (<Box b, Alignment a> <- zip2(bl2, columns)) {
            int width = b.width;
        
            max_width = head(mw);
            mw        = tail(mw);
            // int h= opts.hs;
            switch(a) {
                case l(): {
                    // b.hs=max_width - width+h; /*left alignment */  
                    hargs +=b;
                    hargs += SPACE(max_width - width);
                }
                case r(): {
                    // b.hs=max_width - width+h; /*left alignment */
                    hargs += SPACE(max_width - width);
                    hargs +=b;
                }
                case c(): {
                    hargs += SPACE((max_width - width)/2);
                    hargs +=b;
                    hargs += SPACE((max_width - width)/2);
                }
            }
        }   
        
        vargs += H(hargs);
    }
    
    return O(V(vargs), c, opts, m);
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
    return O(b, V([]), options(), opts.maxWidth);
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

test bool wrapping1IgnoreIndent()
    = format(HV([L("A"), I([L("B")]), L("C")], hs=0), opts=options(maxWidth=2, wrapAfter=2))
    == "AB
       'C
       '";

test bool flipping1NoIndent()
    = format(HOV([L("A"), L("B"), L("C")], hs=0, vs=0), opts=options(maxWidth=2, wrapAfter=2))
    == "A
       'B
       'C
       '";
