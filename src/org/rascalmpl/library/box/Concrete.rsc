module box::Concrete
import ParseTree;
import  ValueIO;
import  IO;
import List;
import String;
import box::Box;
import box::Box2Text;

alias pairs = list[tuple[Symbol, Tree]] ;

alias segment = tuple[int, int];

Box fNULL(Tree t) {return NULL();}

list[int]  pNULL(list[Symbol] p, list[Tree] q) {return [];}

list[segment]  cNULL(list[Symbol] p) {return [];}

bool bNULL(list[Symbol] o) {return false;}

Box(Tree) userDefined = fNULL; 

list[int](list[Symbol] , list[Tree])  isIndented = pNULL;

list[segment](list[Symbol])  isCompact = cNULL;

bool(list[Symbol]) isSeperated = bNULL;

bool isTerminal(Symbol s) {
     return ((\lit(_):= s)) ||  (\char-class(_):=s);
     }

str getName(Symbol s) {
     if (\lit(str q):= s) return q;
     return "";
     } 
        
bool isTerminal(Symbol s, str c) {
     if (\lit(str a):= s) { 
           if (a==c) return true;
           }
     return false;
     }

bool classic = false;

public bool isScheme(list[Symbol] q, list[str] b) {
  if (size(b)!=size(q)) return false;
  list[tuple[Symbol, str]] r = [<q[i], b[i]> |  int i <-[0  .. size(q)-1]];
  for (<Symbol s, str z>  <- r) {
       if (!isTerminal(s)) {
                   if (z!="N") return false;
                   }
      else {
           if (z!="T" &&  !isTerminal(s, z)) return false;
       }
  }
  return true;
}

bool isBlock(list[Symbol] q) {
   return isScheme(q, ["N", "{","N", "}"]);
   }

bool isBody(list[Symbol] q) {
   return isScheme(q, ["{","N", "}"]);
   }

public list[int] isBody(list[Tree] t, int idx) {
             Tree g = t[idx];
             return (isBody(g) && userDefined(g)==NULL())?[idx]:[];
             }

public bool isBlock(Tree c) {
         if (appl(prod(list[Symbol] s, _, Attributes att), _):=c) {
                    list[Symbol]  q =[s[i]  | int i<-[0,2..(size(s)-1)]];
                    r = isBlock(q);
                    // if (r) println("Match2:<t>");
                    return r;
                  }
              return false;
             }
             
public list[int] isBlock(list[Tree] t, int idx) {
            Tree g = t[idx];
             return (isBlock(g)&&userDefined(g)==NULL()) ?[idx]:[];
             }

public bool isBody(Tree c) {
         if (appl(prod(list[Symbol] s, _, Attributes att), _):=c) {
                    list[Symbol]  q =[s[i]  | int i<-[0,2..(size(s)-1)]];
                    r = isBody(q);
                    // if (r) println("Match2:<t>");
                    return r;
                  }
              return false;
             }


list[int] isDefaultIndented(pairs u) {
     for (int i <- [0,2 .. size(u)-2])  {
          if ((<Symbol s1, _> := u[i]) && (<Symbol s2, _> := u[i+1])) {
              if (!(isTerminal(s1)&&!isTerminal(s2))) return [];
              }
       }
     if (size(u)%2==0) return size(u)>2?[0]:[];
     if (<Symbol q, _> := u[size(u)-1])
                     return isTerminal(q)?[0]:[];
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
 
Box boxArgs(pairs u, bool hv, list[int] indent, list[segment] compact, bool seperated, bool doIndent, int space) {
     list[Box] bl = walkThroughSymbols(u, indent, compact, seperated, doIndent);
       if (size(bl)==0) return NULL();
       if (size(bl)==1) {
             return bl[0];
             }
       else {
            Box r =  ((hv && !doIndent &&isEmpty(indent))?HV(1, bl):V(0, bl));
           if (space>=0)  r@hs = space;
            return r;
            }
     }

Box boxArgs(pairs u, bool hv, bool doIndent, int space) {
    return  boxArgs(u, hv, [],  [], false, doIndent, space);
    }

public void initConcrete(Box(Tree) userDef, list[int] (list[Symbol], list[Tree]) g , list[segment] (list[Symbol]) h, bool(list[Symbol]) q ) {
    userDefined = userDef;
    isIndented = g;
    isCompact = h;
    isSeperated = q;
    }

public list[Tree] getA(Tree q) {
    if (appl(_, list[Tree] z):=q) return z;
    return[];
    }

public Tree getLast(Tree q) {
   list[Tree] a = getA(q);
   return  a[size(a)-1];
   }

public Box evPt(Tree q) {
   return evPt(q, false);
   }

public Box evPt(Tree q, bool doIndent) {
   //  rawPrintln(q);
    Box b = userDefined(q); 
    if (b!=NULL()) return b;
    switch(q) {
       case appl(prod(list[Symbol] s, _, Attributes att), list[Tree] t): {
                      pairs u =[<s[i], t[i]>| int i<-[0,2..(size(t)-1)]];
                      list[Symbol] q = [s|<Symbol s, _><-u];
                      list[Tree] z  = [a |<_, Tree a><-u];
                      Box r = boxArgs(u, true, isIndented(q, z), isCompact(q), isSeperated(q), doIndent, -1);  
                      return r;                                   
                     }
        case appl(\list(\cf(\iter-star-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                     pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                      list[Box] q = [H(0, [ evPt(t[i]), (i+2<size(t)?L(getName(sep)):NULL())])|int i<-[0,4..(size(t)-1)]];
                      return (getName(sep)==";")?V(0, q):HV(0, q);
                     // return boxArgs(u, true, doIndent, 0); 
                     }
        case appl(\list(\cf(\iter-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                      list[Box] q = [H(0, [ evPt(t[i]),  (i+2<size(t)?L(getName(sep)):NULL())])|int i<-[0,4..(size(t)-1)]];
                      return (getName(sep)==";")?V(0, q):HV(0, q);
                      // return boxArgs(u, true, doIndent, 0); 
                     }
        case appl(\list(\cf(\iter-star(Symbol s) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                      return boxArgs(u, false, doIndent, -1); 
                     }
        case appl(\list(\cf(\iter(Symbol s) )), list[Tree] t): {
                     pairs u =[<s, t[i]>| int i<-[0,2..(size(t)-1)]];
                     return boxArgs(u, false, doIndent, -1); 
                     }
       }
       return NULL();
}

list[Box] addTree(list[Box] out, Tree t) {
       str s = "<t>";
       if (size(s)>0) out+=s;
       return out;
       }

Box defaultBox(Box b) {
               if (H(list[Box] c):=b ) {
                           if (size(c)>0)  return  b; 
                          }
                      else
                      if (V(list[Box] c):=b) {
                           if (size(c)>0)  return  b; 
                          }
                      else
                      if (HV(list[Box] c):=b) {
                           if (size(c)>0) return  b; 
                          }
                      else
                      if (HOV(list[Box] c):=b) {
                           if (size(c)>0) return  b; 
                          }
                     else 
                     if (L(str c):=b) {
                           if (size(c)>0 && !startsWith(c, " ")) return  b; 
                          }
                     else
                     if (I(list[Box] c):=b) {
                           if (size(c)>0) return  b; 
                          }
                      else 
                           return  b;
                    return NULL();
              } 
                    

list[Box] walkThroughSymbols(pairs u, list[int] indent, list[segment] compact, bool seperated, bool doIndent) {
   list[Box] out = [];
   segment q = isEmpty(compact)?<1000,1000>:head(compact);
   if (!isEmpty(compact)) compact = tail(compact);
   if (classic) {
      for  (<Symbol a, Tree t><-u) {
       // println(a);
      if ( \lex(\sort(_)):=a || \lit(_):=a ||  \char-class(_):=a)  out+= L("<t>") ;
      else {
             Box  b = evPt(t);
             b = defaultBox(b);
             if (b!=NULL()) out+= b;
             }   
      } 
   } else  {
        bool first = true;
        list[Box] c = [];
        for   (int i<-[0, 1 .. (size(u)-1)]) 
        if (<Symbol a, Tree t>:=u[i]) {
            if (first && (i in indent)) {
                 Box r = H(1, out);
                 out=[r];
                 first = false;
                 }
            Box b = ( \lex(\sort(_)):=a || \lit(_):=a ||  \char-class(_):=a)? L("<t>"):
                       defaultBox(evPt(t,  (i in indent)));
              if (b!=NULL()) 
              if ( i>=q[0] && i<=q[1]) {
                           if (b!=NULL())  c+= b;   
                           if (i==q[1]) {
                                   out += H(0, c);
                                   c=[];
                                   if (!isEmpty(compact)) {
                                              q = head(compact);
                                              compact = tail(compact);
                                              }
                                         } 
                              }
                             else 
                                   out+=(i in indent?I([b]):b);       
               }
   }
   return seperated?[H(1, out)]: (doIndent?[V(0, out)]:out);
}

public text  returnText(Tree a, Box(Tree) userDef, list[int](list[Symbol], list[Tree]) isIndented, list[segment](list[Symbol]) isCompact, 
   bool(list[Symbol]) isSeperated ) {
     initConcrete(userDef, isIndented, isCompact, isSeperated);
     Box out = evPt(a);
     // println(out);
     return box2text(out);
    }

public void concrete(Tree a) {
    Box out = evPt(a);
    text t = box2text(out);
    for (str r<-t) {
          println(r);
         }
    println(out);
   }


public list[Box] getArgs(Tree g, type[&T<:Tree] filter) {
   list[Tree] tl = getA(g);
   list[Box] r = [evPt(t) | Tree t <- tl, &T a := t];
   return r;
   }

public Box getConstructor(Tree g,  type[&T<:Tree] filter, str h1, str h2) {
          Box r =  HV([L(h1)]+getArgs(g, filter)+L(h2));
           r@hs = 0;
           return r;
           }
 
   
public Box cmd(str name, Tree expr, str sep) {
   Box h = H([evPt(expr), L(sep)]);
   h@hs = 0;
   return H([L(name), h]);
   }
  
public Box HV(int space, list[Box] bs) {
   Box r = HV([b| Box b <- bs, b!=NULL()]);
   if (space>=0) r@hs = space;
   r@vs = 0;
   return r;
   }

public Box H(int space, list[Box] bs) {
   Box r = H([b| Box b <- bs, b!=NULL()]);
   if (space>=0) r@hs = space;
   return r;
   }
   
 public Box V(int space, list[Box] bs) {
   Box r = V([b| Box b <- bs, b!=NULL()]);
   if (space>=0) r@vs = space;
   return r;
   }
/*
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
     CompilationUnit a = parse(#CompilationUnit, asf);
     Box out = visitParseTree(a);
     // println(out);
     return box2text(out);
     }
 */

