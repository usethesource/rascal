@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@synopsis{This is an implementation of "From Box to Tex:An algebraic approach to the construction of documentation tools" by Mark van den Brand and Eelco Visser (June 30, 1994)

The main function `format` maps a box tree (which describes 2-dimensional layout constraints for a linear text) to a string
which satisfies these constraints.}
module lang::box::util::Box2Text

import List;
import String;
import IO;
import lang::box::util::Box;

int maxWidth = 80;
int hv2h_crit = 70;

alias options = map [str, int];
options  oDefault = ("h":1,"v":0, "i":2, "t":10);

@synopsis{Print boxes}    
public void fprint(Box b) {
  print(format(b));
}

@synopsis{Print boxes followed by newline}
public void fprintln(Box b) {
  println(format(b));
}

@synopsis{Converts boxes into a string} 
public str format(Box b) {
  box2textmap=();
  text t = box2text(b);
  return "<for (l <- t) {><l>\n<}>";
}


@synopsis{Converts boxes into latex}       
public text box2latex(Box b) {
    // println("Start box2latex");
    text q = [];
    if (box2textmap[b]?) q = box2textmap[b];
    else {
        q = box2data(b);
        box2textmap+=(b:q);
        }
    text t = readFileLines(|std:///lang/box/util/Start.tex|)+text2latex(q)+readFileLines(|std:///lang/box/util/End.tex|);    
    // println("End box2latex");
    return t;
    }

@synopsis{Converts boxes into html}       
public text box2html(Box b) {
    //println("Start box2html");
    text q = [];
    if (box2textmap[b]?) q = box2textmap[b];
    else {
        q = box2data(b);
        box2textmap+=(b:q);
        }
    text t = readFileLines(|std:///lang/box/util/Start.html|)+text2html(q)+readFileLines(|std:///lang/box/util/End.html|);    
    //println("End box2html");
    return t;
    }

@synopsis{Converts boxes into list of lines (ASCII)}      
 public text box2text(Box b) {
    text q = [];
    if (box2textmap[b]?) q = box2textmap[b];
    else {
        q = box2data(b);
        box2textmap+=(b:q);
        }
    text t = text2txt(q);
    return t;
    }
    
//-------------------------------------------------------------------------------------------------

alias   foptions = map[str, list[str]];


map[Box, text] box2textmap=();

data Box(list[str] format=[]);

@synopsis{simple vertical concatenation (every list element is a line)}
text vv(text a, text b) = [*a, *b];

str blank(str a) = right("", width(a));

@synopsis{Computes a white line with the length of the last line of a}
text wd([])             = [];
text wd([*_, str x])    = wd([x]);

@synopsis{Computes the length of unescaped string s}
int width(str s) = size(s); // replaceAll(s,"\r...",""); ??
     
@synopsis{Computes the maximum width of text t}
int twidth(text t) = max([width(line) | line <- t]);
     
@synopsis{Computes the length of the last line of t}
int hwidth([])             = 0;
int hwidth([*_, str last]) = width(last);

@synopsis{Prepends str a before text b, all lines of b will be shifted}
text bar(str a, [])                = [a];
text bar(str a, [str bh, *str bt]) = vv([a+bh], prepend(blank(a), bt));

@synopsis{Produce text consisting of a white line of length  n}
text hskip(int n) = [right("", n)];
    
@synopsis{Produces text consisting of n white lines at length 0}
text vskip(int n) = ([] | vv(it, [""]) | _ <- [0..n]);


bool isBlank(str a) {return a==blank(a);}

text prepend(str a, text b) {
   return [(a+c)|str c <- b];
}

text hh([], text b)  = b;
text hh(text a, [])  = a;
text hh([a], text b) = bar(a, b);

default text hh(text a, text b) = vv(a[0..-1], bar(a[-1], b));
        

text lhh([], text _) = [];
default text lhh(a, b) = hh(a, b);

text lvv(text a, text b) {
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

/*
text HH(list[Box] b, Box c, options opts, int m) {
    if (isEmpty(b)) return [];
    int h = opts["h"];
    text t = O(b[0], H([]), opts, m);
    int s = hwidth(t);
    return hh(t, hh_(hskip(h), HH(tail(b), H([]), opts, m-s-h)));
   }
*/
   
text HH(list[Box] b, Box _, options opts, int m) {
    if (isEmpty(b)) return [];
    int h = opts["h"];
    text r = [];
    b = reverse(b);
    for (a<-b) {
         text t = O(a, H([]), opts, m);
         int s = hwidth(t); 
         r = hh(t, hh_(hskip(h), r));
         m  = m - s - h;
   }
   return r;
   }

text VV(list[Box] b, Box c, options opts, int m) {
    if (isEmpty(b)) return [];
    int v = opts["v"];
    text r = [];
    b = reverse(b);
    for (a<-b) {
        if (V(_)!:=c || L("")!:=a)
            {
            text t = O(a, V([]), opts, m);
            r = vv(t, vv_(vskip(v), r));
            }
    }
    return r;
   }

/*
text VV(list[Box] b, Box c, options opts, int m) {
    if (isEmpty(b)) return [];
    int v = opts["v"];
    return vv(O(b[0], c , opts, m), vv_(vskip(v), VV(tail(b), V([]), opts, m)));
   }
*/

text II(list[Box] b, Box c, options opts, int m) {
 if (isEmpty(b)) return [];
    int i = opts["i"];
    switch(c) {
        case  H(list[Box] _):{
            return HH(b, c, opts, m);
        }
       case  V(list[Box] _):{
          int m1 = m-i;
           text t = O(b[0], c, opts, m1);
           int s = hwidth(t);
           text r =  hh_(hskip(i),  hh(t, II(tail(b), c, opts, m1-s)));
           return r;
        }
     }
     return [];
}

text WDWD(list[Box] b, Box c ,options opts, int m) {
    if (isEmpty(b)) return [];
    int h= b[0].hs?opts["h"];
    text t = O(b[0], c, opts, m);
    int s = hwidth(t);
    return  hh(t , hh_(hskip(h) , WDWD(tail(b), c, opts, m-s-h)));
    }



text ifHOV(text t, Box b,  Box c, options opts, int m) {
     if (isEmpty(t)) return [];
     if (size(t)==1) {
          if (width(t[0])<=m)  return t;
          else 
           return O(b, c, opts, m);
           }
      return O(b, c, opts, m);
     }

 text HOVHOV(list[Box] b, Box c, options opts, int m) {
      return ifHOV(HH(b, c, opts, m), V(b), c, opts, m);
     }


/* Gets complicated HVHV */
text HVHV(text T, int s, text a, Box A, list[Box] B, options opts, int m) {
      int h= opts["h"];
      int v = opts["v"];
      int i= opts["i"];
      int n = h + hwidth(a);
      if (size(a)>1) { // Multiple lines 
           text T1 = O(A, V([]), opts, m-i);
           return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
          }
      if (n <= s) {  // Box A fits in current line
           return HVHV(hh(lhh(T, hskip(h)), a), s-n, B, opts, m, H([]));
           }
      else {
        n -= h; // n == width(a)
         if  ((i+n)<m) { // Fits in the next line, not in current line
                 text T1 =O(A, V([]), opts, m-i);
                 return vv(T, vv_(vskip(v), HVHV(T1, m-n-i, B, opts, m, H([]))));
                 }
         else { // Doesn't fit in both lines
                 text T1 =O(A, V([]), opts, m-i);
                 return vv(T, vv_(vskip(v), HVHV(T1, m-hwidth(T1), B, opts, m, H([]))));
                 }
          }
}

text HVHV(text T, int s, list[Box] b, options opts,  int m, Box c) {
      if (isEmpty(b))  return T;
      text T1 = O(b[0], c  , opts, s);  // Was H([])
      return HVHV(T, s, T1 , b[0],  tail(b), opts, m);
      }

 text HVHV(list[Box] b, Box _, options opts, int m) {
       // println("HVHV:<h>");
       if (isEmpty(b))  return [];
       text T =  O(b[0], V([]), opts, m);  // Was H([])
       if (size(b)==1) return T;
      return HVHV(T, m-hwidth(T), tail(b), opts, m, H([]));
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
   for (int i <-[1, 2..n]) {
       r+=t[i];
      }
   r+=(t[n]+"\r}<tg>");
   return r;
  }

text QQ(Box b, Box c, options opts, foptions f, int m) {
      // println("QQ:<getName(b)> <opts>");
      switch(b) {
         case L(str s): {return LL(s);}
         case  H(list[Box] bl): {return HH(bl, c, opts, m); }
         case  V(list[Box] bl): {return VV(bl, c, opts, m);}
         case  I(list[Box] bl):{return II(bl, c, opts, m);}
         case  WD(list[Box] bl):{return WDWD(bl, c, opts, m);}
         case  HOV(list[Box] bl):{return HOVHOV(bl, c, opts, m);}
         case  HV(list[Box] bl):{return  HVHV(bl, c, opts, m);}
         case  SPACE(int n):{return  hskip(n);}
         case  A(list[Box] bl):{return AA(bl, c, opts, f, m);}
         //case  R(list[Box] bl):{return RR(bl, c, opts, m);} // TODO: Return type should be subtype of `list[str]`, found `list[list[Box]]`
         case KW(Box a):{return font(O(a, c, opts, m),"KW");}
         case VAR(Box a):{return  font(O( a, c, opts, m),"VR");}
         case NM(Box a):{return font(O( a, c, opts, m),"NM");}
         case STRING(Box a):{return font(O( a, c, opts, m),"SG");}
         case COMM(Box a):{return font(O( a, c, opts, m),"CT");}
         case MATH(Box a):{return font(O( a, c, opts, m),"MT");}
         case ESC(Box a):{return font(O( a, c, opts, m),"SC");}
     }
return [];
}

text O(Box b, Box c, options opts, int m) {
    int h = opts["h"];
    int v = opts["v"];
    int i = opts["i"];
    // if ((b.vs)?) println("Start:<getName(b)> <b.vs>");
     if ((b.hs)?) {opts["h"] = b.hs;}
     if ((b.vs)?) {opts["v"] = b.vs;}
     if ((b.is)?) {opts["i"] = b.is;}
     foptions f =();
     if ((b.format)?) {f["f"] = b.format;}
     text t = QQ(b, c, opts, f, m);
     opts["h"]=h;
     opts["v"]=v;
     opts["i"]=i;
     // println("End:<getName(b)>");
     return t;
}

/* ------------------------------- Alignment ------------------------------------------------------------*/

Box boxSize(Box b, Box c, options opts, int m) {
       text s = O(b, c, opts, m);
       b.width = twidth(s);
       b.height = size(s);
       return b;
       }

list[list[Box]] RR(list[Box] bl, Box c, options opts, int m) {
     list[list[Box]] g = [ b |R(list[Box]  b)<-bl];
     // println(g);
     return [ [ boxSize(z, c, opts, m) | Box z <- b ] | list[Box] b<- g];
}

int getMaxWidth(list[Box] b) {
   return max([c.width| Box c <- b]);
}

list[int] Awidth(list[list[Box]] a) {
     if (isEmpty(a)) return [];
     int m = size(head(a));  // Rows have the same length
     list[int] r = [];
     for (int k<-[0..m]) {
           r+=[max([b[k].width|b<-a])];
           }
     return r;
     }

text AA(list[Box] bl, Box c ,options opts, foptions f, int m) {
     // println(bl);
     list[list[Box]]  r=RR(bl, c, opts, m);
     list[int] mw0 = Awidth(r);
     list[str] format0 = ((f["f"]?)?f["f"]:[]);
     list[Box] vargs = [];
     for (list[Box] bl2 <- r) {
         list[int]  mw = mw0;
         list[str] format =format0;
         list[Box] hargs = [];
         for (Box b<- bl2) {
                int width = b.width;
                str f_str = !isEmpty(format)?head(format):"l";
                if (!isEmpty(format)) format = tail(format);
                max_width = head(mw);
                mw=tail(mw);
                int h= opts["h"];
                switch(f_str) {
                    case "l": {
                     // b.hs=max_width - width+h; /*left alignment */  
                         hargs+=b;
                         hargs += SPACE(max_width - width);
                         }
                    case "r": {
                     // b.hs=max_width - width+h; /*left alignment */
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
     return O(V(vargs), c, opts, m);
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
                     int h = (t.hs)?(-1);
                     int i =   (t.is)?(-1);
                     int v =   (t.vs)?(-1);
                     Box r = H(hv);
                     if (h>=0) r.hs = h;
                     if (i>=0)  r.is = i;
                     if (v>=0) r.vs = v;
                     r;                
                     }
                  when changeHV2H(hv)
      };
}

Box removeHOV(Box b) {
return innermost visit(b) {
     case t:HOV(list[Box] hov) => {
                     int h = (t.hs)?(-1);
                     int i =   (t.is)?(-1);
                     int v =   (t.vs)?(-1);
                     Box r = changeHV2H(hov)?H(hov):V(hov);
                     if (h>=0) r.hs = h;
                     if (i>=0)  r.is = i;
                     if (v>=0) r.vs = v;
                     // println("changed2");
                     r;
                     }
                  // when changeHV2H(hov)
      };
}





public text box2data(Box b) {
    //println("BEGIN box2data");
    // println(b);
    b = removeHV(b);
    b = removeHOV(b);
    text t = O(b, V([]), oDefault, maxWidth);
    // println(t);
    //println("END box2data");
    return t;
    }
    
public str convert2latex(str s) {
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
   
public str convert2html(str s) {
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
       case /^\r\}<tg:..>/ => selectEndTag(tg)
       }
    }
    
public str text2txt(str t) {
    return visit(t) {
       case /^\r\{../ => ""
       case /^\r\}../ => ""
       }
    }
    
text text2latex(text t) {
    return [text2latex(s)|s<-t];
    }
    
text text2html(text t) {
    return ["\<NOBR\><text2html(s)>\</NOBR\>\<BR\>"|s<-t];
    }
    
public text text2txt(text t) {
    return [text2txt(s)|s<-t];
    } 
       


// JV TODO: these functions do not compile because writeData does not exist
//public value toText(Box b, loc src, loc dest) {
//     text t = box2text(b);
//     writeData(src, dest, t, ".txt");
//     return t;
//     }
//     
//public value toLatex(Box b, loc src, loc dest) {
//     text t = box2latex(b);
//     writeData(src, dest, t, ".tex");
//     return t;
//     }
//
//  
//public value toHtml(Box b, loc src, loc dest) {
//     text t = box2html(b);
//     writeData(src, dest, t, ".html");
//     return t;
//     } 
        
void tst() {
  Box  b1 = R([L("ab"), L("c")]);
  Box  b2 = R([L("def"), L("hg")]);
  Box  b3 = R([L("ijkl"), L("m")]);
  Box b = A([b1, b2, b3]);
  b.format=["c","c"];
} 

public str baseName(str input) {
     str s = input;
     str find = "/";
     if (/^<pre1:.*>\..*?$/:=input) {
          s = "<pre1>";
           if(/^.*<find><post2:.*?>$/ := s) {	
               s = post2;
               }
          }
     return s;
     }

public void toExport(loc src,loc dest, text r,str suffix) {
     str s=baseName(src.path);
     loc g=|file://<dest.path>/<s><suffix>|;
     println("Written <suffix> content in file:\"<g>\"");
     writeFile(g);
     for (str q<-r) appendToFile(g,"<q>\n");
     }
 
 public void main(Box b) {
  fprintln(b);
  }    
