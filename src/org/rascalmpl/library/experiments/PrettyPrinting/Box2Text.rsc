module Box2Text
/* Conform definitions in "From Box to Tex:An algebraic approach to the construction of documentation
tools".

     Mark van den Brand
     Eelco Visser

     June 30, 1994

*/

import List;
import String;
import IO;
import ValueIO;
int maxWidth = 40;
int  lmargin = 5;
alias text = list[str];

data Box
    =  H (list[Box] b)
    |  V(list[Box] b)
    |  HOV (list[Box] b)
    |  HV (list[Box] b)
    |  I(list[Box] b)
    |  WD(list[Box] b)
    |  L(str)
    |  KW(list[Box] b)
    |  VAR(list[Box] b)
    | NUM(list[Box] b)
    ;

alias options = map [str, int];
options  oDefault = ("h":1,"v":1);

anno int Box@hs;
anno int Box@vs;
anno int Box@is;


public Box a = H([L("aap"),L("noot"), L("mies")])[@hs=3];

/* Second part   */
text vv(text a, text b) {
return a+b;}

str blank(str a) {return right("", size(a));}

text wd(text a) {
   if (isEmpty(a)) return [];
   if (size(a)==1) return blank(a[0]);
   return wd(tail(a));
}

int width(str s) {
     return size(s);
     }

int twidth(text t) {
     return max([width(r)|str r <-t]);
     }

int hwidth(text t) {
     if (isEmpty(t)) return 0;
     return width(t[size(t)-1]);
     }

list[Box] expand(Box b) {
     switch (b) {
     case KW(list[Box] bl):return bl;
     case VAR(list[Box] bl):return bl;
     case NUM(list[Box] bl):return bl;
     case MATH(list[Box] bl):return bl;
    }  
    return [b];
}

list[Box] expand(list[Box]  bl) {
    return  [expand(b)|Box b <- bl];
}



text bar(str a, text b) {
    if (isEmpty(b)) return [a];
    return  vv ([a+b[0]], prepend(blank(a), tail(b)));
   }

text hskip(int n) {
     return [right("", n)];
    }

text vskip(int n) {
    text r = [];
    for (int i<-[0,n-1])
         r=vv(r, [""]);
   return r;
}

 bool isBlank(str a) {return a==blank(a);}

text prepend(str a, text b) {
   return [(a+c)|str c <- b];
}

text hh(text a, text b) {
          if (isEmpty(a)) return b;
          if (isEmpty(b))  return a;
          if (size(a)==1) return bar(a[0], b);
          str last = a[size(a)-1];
          list[str] first =  slice(a, 0, size(a)-1);
          return vv(first, bar(last, b));
   }

text _hh(text a, text b) {
     if (isEmpty(a)) return [];
     return hh(a, b);
}

text _vv(text a, text b) {
     if (isEmpty(a)) return [];
     return vv(a, b);
}

text hh_(text a, text b) {
     if (isEmpty(b)) return [];
     return hh(a, b);
}

text vv_(text a, text b) {
     if (isEmpty(b)) return [];
     return vv(a, b);
}

text LL(str s ) { 
   //  if (/L(<s:\w>)/:=b) return s;
   return [s];
   }

text HH(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int h= b[0]@hs?o["h"];
    text t = O(b[0], c, o, m);
    int s = hwidth(t);
    return hh(t, hh_(hskip(h), HH(tail(b), H([]), o, m-s-h)));
   }

text VV(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int v = b[0]@vs?o["v"];
    return vv(O(b[0], c, o, m), vv_(vskip(v), VV(tail(b), c, o, m)));
   }

text II(list[Box] b, Box c, options o, int m) {
 if (isEmpty(b)) return [];
    if (!(o["i"])?) o["i"]= lmargin;
    int i = o["i"];
    switch(c) {
        case  H(list[Box] bl):{
            return HH(b, c, o, m);
        }
       case  V(list[Box] bl):{
          int m1 = m-i;
           text t = O(b[0], c, o, m1);
           int s = hwidth(t);
           text r =  hh_(hskip(i),  hh(t, II(tail(b), c, o, m1-s)));
           return r;
        }
     }
     return [];
}

text WDWD(list[Box] b, Box c ,options o, int m) {
    if (isEmpty(b)) return [];
    int h= b[0]@hs?o["h"];
    text t = O(b[0], c, o, m);
    int s = hwidth(t);
    return  hh(t , hh_(hskip(h) , WDWD(tail(b), c, o, m-s-h)));
    }



text ifHOV(text t, Box b,  Box c, options o, int m) {
     if (isEmpty(t)) return [];
     if (size(t)==1) {
          if (width(t[0])<=m)  return t;
          else 
           return O(b, c, o, m);
           }
      return O(b, c, o, m);
     }

 text HOVHOV(list[Box] b, Box c, options o, int m) {
      return ifHOV(HH(b, c, o, m), V(b), c, o, m);
     }


/* Gets complicated HVHV */
text HVHV(text T, int s, text a, Box A, list[Box] B, options o, int m) {
      int h= o["h"];
      int v = o["v"];
      int i= o["i"]?0;
      int n = h + hwidth(a);
      if (size(a)>1) { // Multiple lines
           
           text T1 =hh(hskip(i), O(A, V([]), o, m-i));
  // println("QQ0:i=<i>  <T1>");
           return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, o, m)));
          }
      if (n <= s) {  // Box A fits in current line
          // println("QQ1:  i=<i>");
           return HVHV(hh(_hh(T, hskip(h)), a), s-n, B, o, m);
           }
      else {
        n -= h; // n == width(a)
         if  ((i+n)<m) { // Fits in the next line, not in current line
                 text T1 =hh(hskip(i), a);
                 // println("QQ2:i=<i> <T1>");
                 return vv(T, vv_(vskip(v), HVHV(T1, m-n-i, B, o, m)));
                 }
         else { // Doesn't fit in both lines
                 text T1 =hh(hskip(i), O(A, V([]), o, m-i));
                 // println("QQ3:i=<i> <T1>");
                 return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, o, m)));
                 }
          }
    // println("QQ3");
     return [];
}

text HVHV(text T, int s, list[Box] B, options o,  int m) {
      if (isEmpty(B))  return T;
      text T1 = O(B[0], H([]), o, s);
      return HVHV(T, s, T1 , B[0],  tail(B), o, m);
      }

 text HVHV(list[Box] b, Box c, options o, int m) {
       if (isEmpty(b))  return [];
       text T =  O(b[0], H([]), o, m);
       if (size(b)==1) return T;
      return HVHV(T, m-hwidth(T), tail(b), o, m);
      }

text O(Box b, Box c, options o, int m) {
      // println(b);
     if ((b@hs)?) {o["h"] = b@hs;}
     if ((b@vs)?) {o["v"] = b@vs;}
     if ((b@is)?) {o["i"] = b@is;}
      switch(b) {
         case L(str s): {return LL(s);}
         case  H(list[Box] bl): {return HH(expand(bl), c, o, m);}
         case  V(list[Box] bl): {return VV(expand(bl), c, o, m);}
         case  I(list[Box] bl):{return II(expand(bl), c, o, m);}
         case  WD(list[Box] bl):{return WDWD(expand(bl), c, o, m);}
         case  HOV(list[Box] bl):{return HOVHOV(expand(bl), c, o, m);}
         case  HV(list[Box] bl):{return HVHV(expand(bl), c, o, m);}
     }
return [];
}

/* text O(Box b, Box c, options o, int m) {
     int h0 = o["h"];
     int v0 = o["v"];
     int i0 = o["i"];
     if ((b@hs)?) {o["h"] = b@hs;}
     if ((b@vs)?) {o["h"] = b@vs;}
     if ((b@is)?) {o["i"] = b@is;}
     text r = _O(b, c, o, m);
     o["h"] =  h0;
     o["v"] =  v0;
     o["i"] =   i0;
     return r;
     }
*/
public void main() {
   Box b = readBinaryValueFile(#Box, |file:///ufs/bertl/box/big.box|);
   println(b);
  //  str s = box2text(b, 0);
  //  println(s);
  text t = O(b, V([]), oDefault, maxWidth);
  for (str r<-t) {
     if (!isBlank(r)) {
         println(r);
         appendToFile(|file:///ufs/bertl/box/big.txt|, r);
          }
     }
}