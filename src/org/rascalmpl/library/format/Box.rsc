@doc{Utility functions for use in string formatting templates. Use `extend \format::Box;` because the Box functions
will recursively call your `format` functions.}
module \format::Box

import String;
import List;

private int stdWidth=100;

str I(value x, int level = 1) = "~x" when level <= 0;
str I(value x, int level = 1) = "    ~(I(x, level=level-1))" when level > 0;

@doc{format all elems next to each other}
str H(list[value] elems, str hsep="")
  = "~for (e <- elems) {~e~} sep {~hsep~}";
  
str V(list[value] elems) 
  = "~for (e <- elems) {~e
    '~}";

str HV([]) = ""; 
str HV([value elem]) = "~elem";
str HV([value elem1, value elem2, *value rest], int width=stdWidth, int originalWidth = width, str hsep = "") {
  s1 = "~elem1";
  w1 = Width(s1);
  
  if (w1 < width) {
    return "~~s1~hsep~(HV([elem2, *rest], width=width-w1, hsep=hsep, originalWidth=originalWidth))";
  }
  else {
    return "~~s1
           '~(HV([elem2, *rest], width=originalWidth, hsep=hsep))";
  }
}



str HOV([]) = "";
str HOV([value elem]) = "~elem";
str HOV(list[value] l:[value _, value _, *value _], int width=stdWidth, str hsep="") {
  hor = H(l,hsep=hsep);
  
  if (size(hor) <= width) {
    return hor;
  }
  else {
    return V(l);
  }
}

private int Width(str s) = max([size(e) | e <- split("\n", s)]);
