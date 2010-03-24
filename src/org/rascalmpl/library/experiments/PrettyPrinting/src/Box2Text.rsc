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
import viz::Basic;
int maxWidth = 60+1;
int hv2h_crit = 40;
int  lmargin = 5;
bool decorated = false;
alias text = list[str];

data Box
    =  H (list[Box] h)
    |  V(list[Box] v)
    |  HOV (list[Box] hov)
    |  HV (list[Box] hv)
    |  I(list[Box] i)
    |  WD(list[Box] wd)
    | R(list[Box] r)
    |  A(list[Box] a)
    | SPACE(int space)
    |  L(str l)
    |  KW(Box kw)
    |  VAR(Box  var)
    | NM(Box  nm)
    ;

alias options = map [str, int];
alias   foptions = map[str, list[str]];
options  oDefault = ("h":1,"v":1);

anno int Box@hs;
anno int Box@vs;
anno int Box@is;
anno int Box@width;
anno int Box@height;
anno list[str] Box@format;


text vv(text a, text b) {
if (!isEmpty(a) && isEmpty(a[0])) return b;
if (!isEmpty(b) && isEmpty(b[0])) return a;
return a+b;}

str blank(str a) {return right("", width(a));}

/* Computes a white line with the length of the last line of a */
text wd(text a) {
   if (isEmpty(a)) return [];
   if (size(a)==1) return blank(a[0]);
   return wd(tail(a));
}

/* Computes the length of unescaped string s */
int width(str s) {
     s = replaceAll(s,"\t...",""); 
    return size(s);
     }

/* Computes the maximum width of text t */
int twidth(text t) {
     return max([width(r)|str r <-t]);
     }

/* Computes the length of the last line of t */
int hwidth(text t) {
     if (isEmpty(t)) return 0;
     return width(t[size(t)-1]);
     }

/* Prepends str a before text b, all lines of b will be shifted  */
text bar(str a, text b) {
    if (isEmpty(b)) return [a];
    return  vv ([a+b[0]], prepend(blank(a), tail(b)));
   }

/* produce text consisting of a white line of length  n */
text hskip(int n) {
     return [right("", n)];
    }

/* produces a text consisting of n white lines at length 0 */
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
   return [s];
   }

text HH(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int h = o["h"];
    text t = O(b[0], c, o, m);
    int s = hwidth(t);
    return hh(t, hh_(hskip(h), HH(tail(b), H([]), o, m-s-h)));
   }

text VV(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int v = o["v"];
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
           text T1 = O(A, V([]), o, m-i);
           return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, o, m, H([]))));
          }
      if (n <= s) {  // Box A fits in current line
           return HVHV(hh(_hh(T, hskip(h)), a), s-n, B, o, m, H([]));
           }
      else {
        n -= h; // n == width(a)
         if  ((i+n)<m) { // Fits in the next line, not in current line
                 text T1 =O(A, V([]), o, m-i);
                 return vv(T, vv_(vskip(v), HVHV(T1, m-n-i, B, o, m, H([]))));
                 }
         else { // Doesn't fit in both lines
                 text T1 =O(A, V([]), o, m-i);
                 return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, o, m, H([]))));
                 }
          }
     return [];
}

text HVHV(text T, int s, list[Box] b, options o,  int m, Box c) {
      if (isEmpty(b))  return T;
      text T1 = O(b[0], c  , o, s);  // Was H([])
      return HVHV(T, s, T1 , b[0],  tail(b), o, m);
      }

 text HVHV(list[Box] b, Box c, options o, int m) {
       if (isEmpty(b))  return [];
       text T =  O(b[0], V([]), o, m);  // Was H([])
       if (size(b)==1) return T;
      return HVHV(T, m-hwidth(T), tail(b), o, m, H([]));
      }

text font(text t, str tg) {
   if (isEmpty(t)) return t;
   str h = "\t{<tg>"+t[0];
   int n = size(t)-1;
   if (n==0) {
       h +="\t}12";
       return [h];
       }
   text r = [];
   r+=h;
   for (int i <-[1, 1..(n-1)]) {
       r+=t[i];
      }
   r+=(t[n]+"}");
   return r;
  }

text QQ(Box b, Box c, options o, int m) {
 text t=[];
      switch(b) {
         case L(str s): {t= LL(s);}
         case  H(list[Box] bl): {t =  HH(bl, c, o, m); }
         case  V(list[Box] bl): {t = VV(bl, c, o, m);}
         case  I(list[Box] bl):{t = II(bl, c, o, m);}
         case  WD(list[Box] bl):{t = WDWD(bl, c, o, m);}
         case  HOV(list[Box] bl):{t = HOVHOV(bl, c, o, m);}
         case  HV(list[Box] bl):{t= HVHV(bl, c, o, m);}
         case  SPACE(int n):{t= hskip(n);}
         case   A(list[Box] bl):{t = AA(bl, c, o, f, m);}
         case KW(Box a):{t =  decorated?font(O(a, c, o, m),"bf"):O(a,c,o,m);}
         case VAR(Box a):{t = decorated?font(O( a, c, o, m),"it"):O( a, c, o, m);}
         case NM(Box a):{t = decorated?font(O( a, c, o, m),"nm"):O( a, c, o, m);}
     }
return t;
}

text O(Box b, Box c, options o, int m) {
    int h = o["h"];
     if ((b@hs)?) {o["h"] = b@hs;}
     if ((b@vs)?) {o["v"] = b@vs;}
     if ((b@is)?) {o["i"] = b@is;}
     foptions f =();
     if ((b@format)?) {f["f"] = b@format;}
     text t = QQ(b, c, o, m);
     o["h"]=h;
     return t;
}
/* ------------------------------- Alignment ------------------------------------------------------------*/

Box boxSize(Box b, Box c, options o, int m) {
       text s = O(b, c, o, m);
       b@width = twidth(s);
       b@height = size(s);
       return b;
       }

list[list[Box]] RR(list[Box] bl, Box c, options o, int m) {
     list[list[Box]] g = [[b]|R(list[Box]  b)<-bl];
     println(g);
     return [[[boxSize(z, c, o, m)|Box z<-[b]] ]|list[Box] b<- g];
}

int maxWidth(list[Box] b) {
     return max([c@width| Box c <- b]);
     }

list[int] Awidth(list[list[Box]] a) {
     int m = size(head(a));  // Rows have the same length
     list[int] r = [];
     for (int k<-[0..m-1]) {
           r+=[max([b[k]@width|b<-a])];
           }
     return r;
     }

text AA(list[Box] bl, Box c ,options o, foptions f, int m) {
     println(bl);
     list[list[Box]]  r=RR(expand(bl), c, o, m);
     list[int] mw0 = Awidth(r);
     list[str] format0 = f["f"];
     list[Box] vargs = [];
     for (list[Box] bl <- r) {
         list[int]  mw = mw0;
         list[str] format =format0;
         list[Box] hargs = [];
         for (Box b<- bl) {
                int width = b@width;
                str f = head(format);
                format = tail(format);
                max_width = head(mw);
                mw=tail(mw);
                int h= o["h"];
                switch(f) {
                    case "l": {
                     // b@hs=max_width - width+h; /*left alignment */  
                         hargs+=b;
                         if (!isEmpty(format)) hargs += SPACE(max_width - width);
                         }
                    case "r": {
                     // b@hs=max_width - width+h; /*left alignment */
                         hargs += SPACE(max_width - width);
                         hargs+=b;
                         }
                    case "c": {
                         hargs += SPACE((max_width - width)/2);
                         hargs+=b;
                         hargs += SPACE((max_width - width)/2);
                        }
                }
}
         vargs += H(hargs);
         }
     return O(V(vargs), c, o, m);
}

bool changeHV2H(list[Box] hv) {
   int n = 0;
    visit(hv) {
         case L(str s): {n+=size(s);}
         }
    return n<hv2h_crit;
    }


Box removeHV(Box b) {
return visit(b) {
     case t:HV(list[Box] hv) => {
                     int h = t@hs?-1;
                     int i =   t@is?-1;
                     int v =   t@is?-1;
                     Box r = H(hv);
                     if (h>=0) r@hs = h;
                     if (i>=0)  r@hs = i;
                     if (v>=0) r@hs = v;
                     r;
                     }
                  when changeHV2H(hv)
      };
}

public void main(Box b) {
  b = removeHV(b);
  // str s = box2text(b, 0);
  // println(s);
  decorated = true;
  text t = O(b, V([]), oDefault, maxWidth);
  boxView(t); 
/*
  for (str r<-t) {
      if  (!isBlank(r)) {
          println(r);
         //  appendToFile(|file:///ufs/bertl/box/big.txt|, r);
        }
   } 
*/
}

void tst() {
  Box  b1 = R([L("ab"), L("c")]);
  Box  b2 = R([L("def"), L("hg")]);
  Box  b3 = R([L("ijkl"), L("m")]);
  Box b = A([b1, b2, b3]);
  b@format=["c","c"];
} 
