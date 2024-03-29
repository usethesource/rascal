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
The input to Box2Text is a hierarchy of "Boxes" represented by the ((Box)) algebraic data-type.
These boxes put hard and soft relative positioning constraints on the embedded text fragments, and
there is the global soft constraints of the width of the screen (or the paper). Box2Text can also
add markup for syntax highlighting in either ANSI plaintext encoding, HTML font tags or LaTex macros.

This implementation is a port from ASF+SDF to Rascal. The ASF+SDF implementation was published as 
"From Box to Tex:An algebraic approach to the construction of documentation tools" by Mark van den Brand 
and Eelco Visser (June 30, 1994). The original Box concept was introduced by Joel Coutaz as this technical report:
"The Box, A Layout Abstraction for User Interface Toolkits" (1984) Pittsburgh, PA: Carnegie Mellon University.

The main function `format` maps a ((Box)) tree to a `str`:
* To obtain ((Box)) terms people typically transform ASTs or ((ParseTree))s to ((Box)) using pattern matching in Rascal.
* ((Options)) encode global default options for constraint parameters that only override local parameters if they were elided.
* ((Markup)) configures which markup language to use for syntax highlighting purposes.
}
@examples{
This demonstrates the semantics of the main hard constraints:
* `H` for horizontal;
* `V` for vertical;
* `I` for indentation.

```rascal-shell
import lang::box::util::Box2Text;
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
format(V([H([L("if"), H([L("("), L("true"), L(")")], hs=0)]), I([V([L("W<i>") | i <- [0..30]])])]))
```
}
module lang::box::util::Box2Text

import List;
import String;
import IO;
import lang::box::util::Box;

@synopsis{Global options for a single formatting run.}
@description{
* `hs` is the default separation between every horizontal element in H, HV and HOV boxes
* `vs` is the default separation between vertical elements in V, HV and HOV boxes
* `is` is the default (additional) indentation for indented boxes
* `maxWidth` is the number of columns (characters) of a single line on screen or on paper
* `hv2hCrit` is the threshold criterium for line fullness, to go to the next line in a HV box and to switching 
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
    int h = 1, 
    int v = 0, 
    int i = 2, 
    int maxWidth=80, 
    int hv2hCrit=70,
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

@synopsis{Converts boxes into latex}   
// TODO: avoid file IO and read directly from constants    
public Text box2latex(Box b, Options opts=options()) 
    = [
    *readFileLines(|std:///lang/box/util/Start.tex|),
    *text2latex(box2text(b, opts=opts)),
    *readFileLines(|std:///lang/box/util/End.tex|)
    ];

@synopsis{Converts Box to HTML}
@description{
This produces a <code>...</code> markup that wraps the formatted
output of ((box2text)). Next to this all the FONT boxes are
translated to their HTML markup representation to provide the
proper highlighting.
}       
public Text box2html(Box b, Options opts=options()) 
    = [
    *readFileLines(|std:///lang/box/util/Start.html|),
    *text2html(q, opts),
    *readFileLines(|std:///lang/box/util/End.html|)
]   ;

@synopsis{Converts boxes into list of lines (ASCII)}      
public Text box2text(Box b, Options opts=options()) = text2txt(box2data(b, opts));
    
alias   fOptions = map[str, list[str]];

map[Box, Text] box2textmap=();

data Box(list[str] format=[]);

@synopsis{simple vertical concatenation (every list element is a line)}
Text vv(Text a, Text b) = [*a, *b];

str blank(str a) = right("", width(a));

@synopsis{Computes a white line with the length of the last line of a}
Text wd([])             = [];
Text wd([*_, str x])    = wd([x]);

@synopsis{Computes the length of unescaped string s}
int width(str s) = size(s); // replaceAll(s,"\r...",""); ??
     
@synopsis{Computes the maximum width of text t}
int twidth(Text t) = max([width(line) | line <- t]);
     
@synopsis{Computes the length of the last line of t}
int hwidth([])             = 0;
int hwidth([*_, str last]) = width(last);

@synopsis{Prepends str a before text b, all lines of b will be shifted}
Text bar(str a, [])                = [a];
Text bar(str a, [str bh, *str bt]) = vv(["<a><bh>"], prepend(blank(a), bt));

@synopsis{Produce text consisting of a white line of length  n}
Text hskip(int n) = [right("", n)];
    
@synopsis{Produces text consisting of n white lines at length 0}
Text vskip(int n) = ["" | _ <- [0..n]];

@synopsis{Check if a string already consists of only blanks.}
bool isBlank(str a) = (a == blank(a));

@synopsis{Prepend Every line in b with `a`}
Text prepend(str a, Text b) = ["<a><line>" | line <- b];

@synopsis{Implements horizontal concatenation, also for multiple lines}
Text hh([], Text b)  = b;
Text hh(Text a, [])  = a;
Text hh([a], Text b) = bar(a, b);

default Text hh(Text a, Text b) = vv(a[0..-1], bar(a[-1], b));
        
@synsopsis{Horizontal concatenation, but if the left text is empty return nothing.}
Text lhh([], Text _) = [];
default Text lhh(a, b) = hh(a, b);

@synsopsis{Vertical concatenation, but if the left text is empty return nothing.}
Text lvv([], Text _) = [];
default Text lvv(Text a, Text b) = vv(a,b);

@synsopsis{Horizontal concatenation, but if the right text is empty return nothing.}
Text rhh(Text _, []) = [];
Text rhh(Text a, Text b) = hh(a, b);

@synsopsis{Vertical concatenation, but if the right text is empty return nothing.}
Text rvv(Text _, []) = [];
default Text rvv(Text a, Text b) = vv(a,b);
    
Text LL(str s ) = [s]; 
   
Text HH([], Box _, Options opts, int m) = [];

Text HH(list[Box] b:[_, *_], Box _, Options opts, int m) {
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        Text t = O(a, H([]), opts, m);
        int s = hwidth(t); 
        r = hh(t, rhh(hskip(opts.h), r));
        m  = m - s - opts.h;
    }
   
    return r;
}

Text VV(list[Box] b, Box c, Options opts, int m) {
    if (isEmpty(b)) return [];
    Text r = [];
    b = reverse(b);
    for (a <- b) {
        if (V(_)!:=c || L("")!:=a) {
            Text t = O(a, V([]), opts, m);
            r = vv(t, rvv(vskip(opts.v), r));
        }
    }
    return r;
   }

Text II([], Box c, Options opts, int m) = [];

Text II(list[Box] b:[_,*_], c:H(list[Box] _), Options opts, int m) = HH(b, c, opts, m);

Text II(list[Box] b:[head,*tail], c:V(list[Box] _), Options opts, int m) {
    Text t = O(head, c, opts, m - opts.i);
    return rhh(hskip(opts.i),  hh(tail, II(t, c, opts, m - opts.i - hwidth(t))));
}

Text WDWD(list[Box] b, Box c , Options opts, int m) {
    if (isEmpty(b)) {
        return [];
    }
    int h  = b[0].hs?opts.h;
    Text t = O(b[0], c, opts, m);
    int s  = hwidth(t);
    return  hh(t , rhh(hskip(h) , WDWD(tail(b), c, opts, m - s - h)));
}

Text ifHOV(Text t, Box b,  Box c, Options opts, int m) {
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

Text HOVHOV(list[Box] b, Box c, Options opts, int m) 
    = ifHOV(HH(b, c, opts, m), V(b), c, opts, m);

/* Gets complicated HVHV */
Text HVHV(Text T, int s, Text a, Box A, list[Box] B, Options opts, int m) {
    int h= opts.h;
    int v = opts.v;
    int i= opts.i;
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

Text HVHV(Text T, int s, list[Box] b, Options opts,  int m, Box c) {
    if (isEmpty(b)) {
        return T;
    }
    Text T1 = O(b[0], c  , opts, s);  // Was H([])
    return HVHV(T, s, T1 , b[0],  tail(b), opts, m);
}

Text HVHV(list[Box] b, Box _, Options opts, int m) {
    if (isEmpty(b)) {
        return [];
    }
    Text T =  O(b[0], V([]), opts, m);  // Was H([])
    if (size(b)==1) {
        return T;
    }

    return HVHV(T, m - hwidth(T), tail(b), opts, m, H([]));
}

// TODO: use real ANSI escape codes here instead?
Text font(Text t, str tg) {
    if (isEmpty(t)) return t;
    str h = "\r{<tg>"+t[0];
    int n = size(t)-1;
    if (n==0) {
        h += "\r}12";
        return [h];
    }
    Text r = [];
    r+=h;
    for (int i <-[1, 2..n]) {
        r+=t[i];
    }
    r+=(t[n]+"\r}<tg>");
    return r;
}

Text QQ(Box b:L(str s)         , Box c, Options opts, int m) = LL(s);
Text QQ(Box b:H(list[Box] bl)  , Box c, Options opts, int m) = HH(bl, c, opts, m); 
Text QQ(Box b:V(list[Box] bl)  , Box c, Options opts, int m) = VV(bl, c, opts, m);
Text QQ(Box b:I(list[Box] bl)  , Box c, Options opts, int m) = II(bl, c, opts, m);
Text QQ(Box b:WD(list[Box] bl) , Box c, Options opts, int m) = WDWD(bl, c, opts, m);
Text QQ(Box b:HOV(list[Box] bl), Box c, Options opts, int m) = HOVHOV(bl, c, opts, m);
Text QQ(Box b:HV(list[Box] bl) , Box c, Options opts, int m) = HVHV(bl, c, opts, m);
Text QQ(Box b:SPACE(int n)     , Box c, Options opts, int m) = hskip(n);

Text QQ(Box b:A(list[Box] bl)  , Box c, Options opts, int m) 
    = AA([b[align=a] | <b, a> <- zip2(bl, b.columns)], c, opts, m);

Text QQ(Box b:KW(Box a)        , Box c, Options opts, int m) = font(O(a, c, opts, m), "KW");
Text QQ(Box b:VAR(Box a)       , Box c, Options opts, int m) = font(O(a, c, opts, m), "VR");
Text QQ(Box b:NM(Box a)        , Box c, Options opts, int m) = font(O(a, c, opts, m), "NM");
Text QQ(Box b:STRING(Box a)    , Box c, Options opts, int m) = font(O(a, c, opts, m), "SG");
Text QQ(Box b:COMM(Box a)      , Box c, Options opts, int m) = font(O(a, c, opts, m), "CT");
Text QQ(Box b:MATH(Box a)      , Box c, Options opts, int m) = font(O(a, c, opts, m), "MT");
Text QQ(Box b:ESC(Box a)       , Box c, Options opts, int m) = font(O(a, c, opts, m), "SC");
     
Text O(Box b, Box c, Options opts, int m) {
    int h = opts.h;
    int v = opts.v;
    int i = opts.i;
    // if ((b.vs)?) println("Start:<getName(b)> <b.vs>");
    if ((b.hs)?) {opts.h = b.hs;}
    if ((b.vs)?) {opts.v = b.vs;}
    if ((b.is)?) {opts.i = b.is;}
    
    return QQ(b, c, opts, m);
}

/* ------------------------------- Alignment ------------------------------------------------------------*/

Box boxSize(Box b, Box c, Options opts, int m) {
    Text s = O(b, c, opts, m);
    b.width = twidth(s);
    b.height = size(s);
    return b;
}

list[list[Box]] RR(list[Box] bl, Box c, Options opts, int m) {
    list[list[Box]] g = [ b |R(list[Box]  b)<-bl];
    return [ [ boxSize(z, c, opts, m) | Box z <- b ] | list[Box] b<- g];
}

int getMaxWidth(list[Box] b) = max([c.width| Box c <- b]);

list[int] Awidth(list[list[Box]] a) {
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

Text AA(list[Box] bl, Box c , Options opts, int m) {
    list[list[Box]] r = RR(bl, c, opts, m);
    list[int] mw0 = Awidth(r);
    list[Box] vargs = [];

    for (list[Box] bl2 <- r) {
        list[int]  mw = mw0;
        list[Box] hargs = [];
        for (Box b <- bl2) {
            int width = b.width;
        
            max_width = head(mw);
            mw=tail(mw);
            int h= opts.h;
            switch(b.align) {
                case l(): {
                    // b.hs=max_width - width+h; /*left alignment */  
                    hargs+=b;
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
bool noWidthOverflow(list[Box] hv, Options opts) 
    = (0 | it + size(s) | /L(s) := hv) < opts.hv2hCrit;

@synopsis{Changes all HV boxes that do fit horizontally into hard H boxes.}
Box applyHVconstraints(Box b, Options opts) = innermost visit(b) {
    case HV(boxes, hs=h, is=i, vs=v) => H(boxes, hs=h, is=i, vs=v) 
        when noWidthOverflow(boxes, opts)
};

@synopsis{Changes all HOV boxes that do fit horizontally into hard H boxes,
and the others into hard V boxes.}
Box applyHOVconstraints(Box b, Options opts) = innermost visit(b) {
    case HOV(boxes, hs=h, is=i, vs=v) => noWidthOverflow(boxes, opts) 
        ? H(boxes, hs=h, is=i, vs=v)
        : V(boxes, hs=h, is=i, vs=v)      
};

@synopsis{Workhorse, that first applies hard HV and HOV limits and then starts the general algorithm}
Text box2data(Box b, Options opts) {
    b = applyHVconstraints(b, opts);
    b = applyHOVconstraints(b, opts);
    return O(b, V([]), options(), opts.maxWidth);
}
    
str convert2latex(str s) {
	return visit (s) { 
	  case /^\r\{/ => "\r{"
	  case /^\r\}/ => "\r}"
	  case /^`/ => "{\\textasciigrave}"
	  case /^\"/ => "{\\textacutedbl}"
	  case /^\{/ => "\\{"
	  case /^\}/ => "\\}"
	  case /^\\/ => "{\\textbackslash}"
	  case /^\</ => "{\\textless}"
	  case /^\>/ => "{\\textgreater}"
	  case /^\|/ => "{\\textbar}"
	  case /^%/ => "\\%"
	  // case /^-/ => "{\\textendash}"
	}	
}

str text2latex(str t) {
    t = convert2latex(t);
    return visit(t) {
       // case /^\r\{<tg:..><key:[^\r]*>\r\}../ => "\\<tg>{<text2latex(key)>}"
       case /^\r\{<tg:..><key:[^\r]*>/ => "\\<tg>{<key>"
       case /^\r\}../ => "}"
    }
}

str selectBeginTag(str tg, str key) {
   if (tg=="KW") return "\<B\><key>";
   if (tg=="CT") return "\<I\><key>";
   if (tg=="SG") return "\<FONT color=\"blue\"\><key>";
   if (tg=="NM") return "\<FONT color=\"blue\"\><key>";
   if (tg=="SC") return "\<I\><key>";
   return key;
}

str selectEndTag(str tg) {
   if (tg=="KW") return "\</B\>";
   if (tg=="CT") return "\</I\>";
   if (tg=="SG") return "\</FONT\>";
   if (tg=="NM") return "\</FONT\>";
   if (tg=="SC") return "\</I\>";
   return "";
}
   
str convert2html(str s) {
	return visit (s) { 
	  case /^\r\{/ => "\r{"
	  case /^\r\}/ => "\r}"
	  case /^ / => "&nbsp;"
	  case /^\"/ => "&quot;"
	  case /^&/ => "&amp;"
	  case /^\</ => "&lt;"
	  case /^\>/ => "&gt;"
	  case /^%/ => "\\%"
	}	
}

str text2html(str t) {
    t = convert2html(t);
    return visit(t) {
       case /^\r\{<tg:..><key:[^\r]*>/ =>  selectBeginTag(tg, key)
       case /^\r\}<tg:..>/             => selectEndTag(tg)
    }
}
    
str text2txt(str t) {
    return visit(t) {
       case /^\r\{../ => ""
       case /^\r\}../ => ""
    }
}
    
Text text2latex(Text t) = [text2latex(s)| s <- t];
    
Text text2html(Text t) = ["\<NOBR\><text2html(s)>\</NOBR\>\<BR\>" | s <- t];
    
Text text2txt(Text t) = [text2txt(s) | s <- t];

       


