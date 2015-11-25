@doc{Utility functions for use in string formatting templates. Use `extend \format::Box;` because the Box functions
will recursively call your `format` functions.}
module \format::Box

import String;
import List;
import IO;

private int stdWidth=100;

@doc{indent the formatted value x a given number of levels}
str I(value x, int level = 1) = level <= 0 ? "~x" : "    ~(I(x, level=level-1))";

@doc{format all elems next to each other}
str H(list[value] elems, str sep="")
  = "~for (e <- elems) {~e~} sep {~sep~}";
  
@doc{format all elems on top of each other}  
str V(list[value] elems) 
  = "~for (e <- elems) {~e~} sep {
    '~}";

@doc{format all elems next to each other and move to a newline if an element would go over the width margin.}
str HV(list[value] elems, int width=stdWidth) 
  = "~{oldWidth = width;}~for (e <- elems, s := "~e", W := Width(s)) {~if (W < width) {~~s~} else {
    '~{width=oldWidth;}~~s~}~{width -= W;}~}";

@doc{format all elements horizontally if it fits in the width margin, otherwise vertically}
str HOV(list[value] elems, str sep="", int width=stdWidth) 
  = hor := H(l,hsep=hsep) && size(hor) <= width ? hor : V(elems);

private int Width(str s) = max([size(e) | e <- split("\n", s)]);
