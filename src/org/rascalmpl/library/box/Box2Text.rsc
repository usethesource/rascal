module box::Box2Text

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
import Node;
import SystemAPI;
import box::Box;
import box::Latex;
int maxWidth = 80;
int hv2h_crit = 70;
bool decorated = false;


alias options = map [str, int];
alias   foptions = map[str, list[str]];
options  oDefault = ("h":1,"v":0, "i":5);
map[int, str] toBlank =  ();


anno list[str] Box@format;


text vv(text a, text b) {
// if (!isEmpty(a) && isEmpty(a[0])) return b;
// if (!isEmpty(b) && isEmpty(b[0])) return a;
return a+b;}

str blank(str a) {
       /* int n = width(a);
        if (n notin toBlank)  {
          toBlank[n] = right("", n);
         }
       return toBlank[n]; */
       return right("", width(a));
       }

/* Computes a white line with the length of the last line of a */
text wd(text a) {
   if (isEmpty(a)) return [];
   if (size(a)==1) return blank(a[0]);
   return wd(tail(a));
}

/* Computes the length of unescaped string s */
int width(str s) {
     s = replaceAll(s,"\r...",""); 
     int b = size(s); 
     return b;
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
    // println("OK<n>");
   for (int i<-[0, 1..n-1])  r=vv(r,[""]);
   // println(size(r));
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
   // println(s);
   if (startsWith(s,"\"<backquote>") && endsWith(s,"<backquote>\"")) {
       s = substring(s, 1, size(s)-1);
       s = replaceAll(s, "\\\\\"", "\"");
       }
   return [s];
   }

/*
text HH(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int h = o["h"];
    text t = O(b[0], H([]), o, m);
    int s = hwidth(t);
    return hh(t, hh_(hskip(h), HH(tail(b), H([]), o, m-s-h)));
   }
*/
   
text HH(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int h = o["h"];
    text r = [];
    b = reverse(b);
    for (a<-b) {
         text t = O(a, H([]), o, m);
         int s = hwidth(t); 
         r = hh(t, hh_(hskip(h), r));
         m  = m - s - h;
   }
   return r;
   }

text VV(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int v = o["v"];
    text r = [];
    b = reverse(b);
    for (a<-b) {
          text t = O(a, V([]), o, m);
          r = vv(t, vv_(vskip(v), r));
    }
    return r;
   }

/*
text VV(list[Box] b, Box c, options o, int m) {
    if (isEmpty(b)) return [];
    int v = o["v"];
    return vv(O(b[0], c , o, m), vv_(vskip(v), VV(tail(b), V([]), o, m)));
   }
*/

text II(list[Box] b, Box c, options o, int m) {
 if (isEmpty(b)) return [];
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
      int i= o["i"];
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
       int h = o["h"];
       // println("HVHV:<h>");
       if (isEmpty(b))  return [];
       text T =  O(b[0], V([]), o, m);  // Was H([])
       if (size(b)==1) return T;
      return HVHV(T, m-hwidth(T), tail(b), o, m, H([]));
      }

text font(text t, str tg) {
   if (isEmpty(t)) return t;
   str h = "\r{<tg>"+t[0];
   int n = size(t)-1;
   if (n==0) {
       h += "\r}12";
       return [h];
       }
   text r = [];
   r+=h;
   for (int i <-[1, 2..(n-1)]) {
       r+=t[i];
      }
   r+=(t[n]+"\r}<tg>");
   return r;
  }

text QQ(Box b, Box c, options o, int m) {
      switch(b) {
         case L(str s): {return LL(s);}
         case  H(list[Box] bl): {return HH(bl, c, o, m); }
         case  V(list[Box] bl): {return VV(bl, c, o, m);}
         case  I(list[Box] bl):{return II(bl, c, o, m);}
         case  WD(list[Box] bl):{return WDWD(bl, c, o, m);}
         case  HOV(list[Box] bl):{return HOVHOV(bl, c, o, m);}
         case  HV(list[Box] bl):{return  HVHV(bl, c, o, m);}
         case  SPACE(int n):{return  hskip(n);}
         case  A(list[Box] bl):{return AA(bl, c, o, f, m);}
         case KW(Box a):{return decorated?font(O(a, c, o, m),"KW"):O(a,c,o,m);}
         case VAR(Box a):{return  decorated?font(O( a, c, o, m),"VR"):O( a, c, o, m);}
         case NM(Box a):{return decorated?font(O( a, c, o, m),"NM"):O( a, c, o, m);}
         case STRING(Box a):{return decorated?font(O( a, c, o, m),"SG"):O( a, c, o, m);}
         case COMM(Box a):{return decorated?font(O( a, c, o, m),"CT"):O( a, c, o, m);}
         case MATH(Box a):{return decorated?font(O( a, c, o, m),"MT"):O( a, c, o, m);}
         case ESC(Box a):{return decorated?font(O( a, c, o, m),"SC"):O( a, c, o, m);}
     }
return [];
}

text O(Box b, Box c, options o, int m) {
    int h = o["h"];
    int v = o["v"];
    int i = o["i"];
     // println("Start:<getName(b)> <h>");
     if ((b@hs)?) {o["h"] = b@hs;}
     if ((b@vs)?) {o["v"] = b@vs;}
     if ((b@is)?) {o["i"] = b@is;}
     foptions f =();
     if ((b@format)?) {f["f"] = b@format;}
     text t = QQ(b, c, o, m);
     o["h"]=h;
     o["v"]=v;
     o["i"]=i;
     // println("End:<getName(b)>");
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
return innermost visit(b) {
     case t:HV(list[Box] hv) => {
                     int h = t@hs?-1;
                     int i =   t@is?-1;
                     int v =   t@vs?-1;
                     Box r = H(hv);
                     if (h>=0) r@hs = h;
                     if (i>=0)  r@is = i;
                     if (v>=0) r@vs = v;
                     r;                
                     }
                  when changeHV2H(hv)
      };
}

Box removeHOV(Box b) {
return innermost visit(b) {
     case t:HOV(list[Box] hov) => {
                     int h = t@hs?-1;
                     int i =   t@is?-1;
                     int v =   t@vs?-1;
                     Box r = changeHV2H(hov)?H(hov):V(hov);
                     if (h>=0) r@hs = h;
                     if (i>=0)  r@is = i;
                     if (v>=0) r@vs = v;
                     // println("changed2");
                     r;
                     }
                  // when changeHV2H(hov)
      };
}

public void main(Box b) {
  // str s = box2text(b, 0);
  value t = toList(b);
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

public void fprint(Box b) {
  print(format(b));
}

public void fprintln(Box b) {
  println(format(b));
}

public str format(Box b) {
  text t = box2text(b);
  return "<for (l <- t) {><l>\n<}>";
}



public text box2data(Box b) {
    println("BEGIN box2data");
    b = removeHV(b);
    b = removeHOV(b);
    text t = O(b, V([]), oDefault, maxWidth);
    println("END box2data");
    return t;
    }
    
public str convert2latex(str s) {
	return visit (s) { 
	  case /^\r\{/ => "\r{"
	  case /^\r\}/ => "\r}"
	  case /^<backquote>/ => "{\\textasciigrave}"
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
       case /^\r\{<tg:..><key:[^\r]*>\r\}../ => "\\<tg>{<key>}"
       case /^\r\{<tg:..><key:[^\r]*>/ => "\\<tg>{<key>"
       case /^\r\}<tg:..>/ => "}"
       }
    }

str selectTag(str tg, str key) {
   if (tg=="KW") return "\<B\><key>\</B\>";
   if (tg=="CT") return "\<I\><key>\</I\>";
   if (tg=="SG") return "\<FONT color=\"blue\"\><key>\</FONT\>";
   if (tg=="NM") return "\<FONT color=\"blue\"\><key>\</FONT\>";
   if (tg=="SC") return "\<I\><key>\</I\>";
   return key;
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
   
public str convert2html(str s) {
	return visit (s) { 
	  case /^\r\{/ => "\r{"
	  case /^\r\}/ => "\r}"
	  case /^ / => "&nbsp;"
	  case /^\"/ => "&quot;"
	  case /^&/ => "&amp;"
	  // case /^\{/ => "\\{"
	  // case /^\}/ => "\\}"
	  case /^\</ => "&lt;"
	  case /^\>/ => "&gt;"
	  case /^%/ => "\\%"
	}	
}

str text2html(str t) {
    t = convert2html(t);
    return visit(t) {
       case /^\r\{<tg:..><key:[^\r]*>\r\}../ => selectTag(tg, key)
       case /^\r\{<tg:..><key:[^\r]*>/ =>  selectBeginTag(tg, key)
       case /^\r\}<tg:..>/ => selectEndTag(tg)
       }
    }
    
str text2txt(str t) {
    return visit(t) {
       case /^\r\{<tg:..><key:[^\r]*>\r\}../ => "<key>"
       }
    }
    
text text2latex(text t) {
    return [text2latex(s)|s<-t];
    }
    
text text2html(text t) {
    return ["\<NOBR\><text2html(s)>\</NOBR\>\<BR\>"|s<-t];
    }
    
text text2txt(text t) {
    return [text2txt(s)|s<-t];
    } 
       
map[Box, text] aux=();
     
public text box2latex(Box b) {
    // println("Start box2latex");
    decorated =  true;
    text q = [];
    if (aux[b]?) q = aux[b];
    else {
        q = box2data(b);
        aux+=(b:q);
        }
    text t = getFileContent("box/Start.tex")+text2latex(q)+getFileContent("box/End.tex");    
    // println("End box2latex");
    return t;
    }
    
public text box2html(Box b) {
    println("Start box2latex");
    decorated =  true;
    text q = [];
    if (aux[b]?) q = aux[b];
    else {
        q = box2data(b);
        aux+=(b:q);
        }
    text t = getFileContent("box/Start.html")+text2html(q)+getFileContent("box/End.html");    
    println("End box2latex");
    return t;
    }
    
 public text box2text(Box b) {
    decorated =  true;
    text q = [];
    if (aux[b]?) q = aux[b];
    else {
        q = box2data(b);
        aux+=(b:q);
        }
    text t = text2txt(q);
    return t;
    }

public value toList(Box b) {
  // println("Hallo");
  b = removeHV(b);
  b = removeHOV(b);
  // println("Reduce finished");
  decorated =  true;
  text t = O(b, V([]), oDefault, maxWidth);
  return t;
}

void tst() {
  Box  b1 = R([L("ab"), L("c")]);
  Box  b2 = R([L("def"), L("hg")]);
  Box  b3 = R([L("ijkl"), L("m")]);
  Box b = A([b1, b2, b3]);
  b@format=["c","c"];
} 
