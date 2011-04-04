@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::C
import ParseTree;


import  IO;
import List;
import String;
import box::Box;
import box::Box2Text;
import languages::c::syntax::C;

alias pairs = list[tuple[Symbol, Tree]] ;

bool isTerminal(Symbol s) {
     return ((\lit(_):= s)) ||  (\char-class(_):=s);
     }

bool isIndented(pairs u) {
     for (int i <- [0,2 .. size(u)-2])  {
          if ((<Symbol s1, _> := u[i]) && (<Symbol s2, _> := u[i+1])) {
              if (!(isTerminal(s1)&&!isTerminal(s2))) return false;
              }
       }
     if (size(u)%2==0) return size(u)>2;
     if (<Symbol q, _> := u[size(u)-1])
                     return isTerminal(q);
     return false;
     }
     
     
str toString(Attributes att) {
       if (\attrs(list[Attr] a):=att) {
          return ("" | it + "_<a[i]>" | i <- [0..size(a)-1]);
          }
       return "<att>";
       }
       
Box toValue(Attributes att) {
       if (\attrs(list[Attr] a):=att) {
          list[value] vs = [v| \term(\box(value v))  <- a];
          if (size(vs)>0) {
              // println("QQ:<vs>");
              Box r = readTextValueString(#Box, "<vs[0]>");
              return r;
              }
          }
       return NULL();
     }
     
Box boxArgs(pairs u, bool hv, bool indent, int space) {
     list[Box] bl = walkThroughSymbols(u, indent);
       if (size(bl)==0) return H([]);
       if (size(bl)==1) {
             return bl[0];
             }
       else {
            Box r =  (hv&&!indent)?HV(bl):V(bl);
            r@hs = space;
            return r;
            }
     }


Box visitParseTree(Tree q) {
   //  rawPrintln(q);
    switch(q) {
       case appl(prod(list[Symbol] s, _, Attributes att), list[Tree] t): {
                      pairs u =[<s[i], t[i]>| int i<-[0,2..(size(t)-1)]];
                      return boxArgs(u, true, isIndented(u), 1);                                     
                     }
        case appl(\list(\cf(\iter-star-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                     pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                     return boxArgs(u, false, isIndented(u), 0); 
                     }
        case appl(\list(\cf(\iter-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                      return boxArgs(u, true, false, 0); 
                     }
        case appl(\list(\cf(\iter-star(Symbol s) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                      return boxArgs(u, true, false, 1); 
                     }
        case appl(\list(\cf(\iter(Symbol s) )), list[Tree] t): {
                     pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                     return boxArgs(u, true, false, 1); 
                     }
       // case appl(Production p , list[Tree] t): {println("<p>:<t>");}
       }
       return H([]);
}

list[Box] addTree(list[Box] out, Tree t) {
       str s = "<t>";
       if (size(s)>0) out+=s;
       return out;
       }


 list[Box] walkThroughList(pairs u) {
    list[Box] out = [];
   for  (<Symbol a, Tree t><-u) {
       switch(a) {
        case \opt(Symbol s):  return walkThroughList([<s, t>]);
        case \iter(Symbol s): return walkThroughList([<s, t>]);
        case \lex(\sort(_)): out= addTree(out, t);
        case \lit(_):  out= addTree(out, t);
        case \char-class(_): out= addTree(out, t);
        default: {
                Box  b = visitParseTree(t);
                out+= defaultBox(b, false);
                }    
        }
         // println("Klaar:<out>");
         } 
   return out;
}

list[Box] defaultBox(Box b, bool indent) {
               list[Box] out = [];
               if (H(list[Box] c):=b ) {
                           if (size(c)>0) out+=  (indent?I([b]):b); 
                          }
                      else
                      if (V(list[Box] c):=b) {
                           if (size(c)>0) out+=   (indent?I([b]):b); 
                          }
                      else
                      if (HV(list[Box] c):=b) {
                           if (size(c)>0) out+=  (indent?I([b]):b); 
                          }
                      else
                      if (HOV(list[Box] c):=b) {
                           if (size(c)>0) out+=  (indent?I([b]):b); 
                          }
                     else 
                     if (L(str c):=b) {
                           if (size(c)>0) out+=  (indent?I([b]):b); 
                          }
                     else
                          out+=  (indent?I([b]):b);
                    return out;
              } 
                    

list[Box] walkThroughSymbols(pairs u, bool indent) {
  list[Box] out = [];
   for  (<Symbol a, Tree t><-u) {
       // println(a);
      if ( \lex(\sort(_)):=a || \lit(_):=a ||  \char-class(_):=a)  out+= L("<t>");
     else {
             Box  b = visitParseTree(t);
              out+= defaultBox(b, indent);
              }   
   }
   return out;
}

public void main(){
     // ParseTree a = readBinaryValueFile(#ParseTree, |file:///ufs/bertl/asfix/pico/big.asf|);
    ParseTree a = readBinaryValueFile(#ParseTree, |file:///ufs/bertl/asfix/java/ViewAction.asf|);
    Box out = visitParseTree(getTree(a));
    text t = box2text(out);
    for (str r<-t) {
          println(r);
         }
    println(out);
    println(getTree(a));
    }
     
public text toList(loc asf){
     TranslationUnit a = parse(#TranslationUnit, asf);
     Box out = visitParseTree(a);
     // println(out);
     return box2text(out);
     }

