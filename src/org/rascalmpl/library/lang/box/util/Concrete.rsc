@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module lang::box::util::Concrete
import ParseTree;
import ValueIO;
import IO;
import List;
import String;
import lang::box::util::Box;
import lang::box::util::Box2Text;


alias pairs = list[tuple[Symbol,Tree]] ;

alias segment = tuple[int,int] ;

// UserDefined

Box defaultUserDefined(Tree t) {
     return NULL();
     }

Box ( Tree ) userDefined=defaultUserDefined ;

public void setUserDefined(Box ( Tree ) userDef) {
     userDefined=userDef;
     }
     
//  startEndBlock
tuple[str, str] defaultStartEndBlock = <"{", "}">;
tuple[str, str] startEndBlock = defaultStartEndBlock;
public void setStartEndBlock(str startSym, str endSym) {
    startEndBlock = <startSym, endSym>;
    }
    
//   ISINDENT 

list[int] defaultIndent(list[Symbol] p) {
     return [];
     }

list[int] ( list[Symbol]) isIndent=defaultIndent ;

public void setIndent(list[int] ( list[Symbol]) isIn) {
     isIndent=isIn;
     }

//   ISBLOK 

list[int] defaultBlok(list[Symbol] p,list[Tree] q) {
     return [];
     }

list[int] ( list[Symbol],list[Tree] ) isBlok=defaultBlok ;

public void setBlock(list[int] ( list[Symbol],list[Tree] ) isBl) {
     isBlok=isBl;
     }

//   ISCOMPACT

list[segment] defaultCompact(list[Symbol] p) {
     return [];
     }

list[segment] ( list[Symbol] ) isCompact=defaultCompact ;

public void setCompact(list[segment] ( list[Symbol] ) isCompac) {
     isCompact=isCompac;
     }

//   ISSEPARATED

bool defaultSeparated(list[Symbol] o) {
     return false;
     }

bool ( list[Symbol] ) isSeparated=defaultSeparated ;

public void setSeparated(bool ( list[Symbol] ) isSepar) {
     isSeparated=isSepar;
     }

//   ISKEYWORD

bool defaultKeyword(Symbol o) {
     return false;
     }

bool defaultString(Symbol o) {
     return false;
     }

bool ( Symbol s ) isKeyword=defaultKeyword ;

bool ( Symbol s ) isString=defaultString ;

public void setKeyword(bool ( Symbol ) isKeywor) {
     isKeyword=isKeywor;
     }

public void setString(bool ( Symbol ) isStrin) {
     isString=isStrin;
     }

// End Setting User Defined Filters

bool isTerminal(Symbol s) {
     return ((\lit(_):=s))||(\char-class(_):=s);
     }

str getName(list[Symbol] b) {
     list[str] r = [q| \lit(str q) <- b];
     return "<for (x <- r) {><x><}>";
     }

bool isTerminal(Symbol s,str c) {
     if (\lit(str a):=s) {
          if (a==c) return true;
          }
     return false;
     }
     
bool isNonTerminal(Symbol s,str c) {
     if (\sort(str a):=s) {
          if (a==c) return true;
          }
     return false;
     }

bool isComment(Attributes a) {
     if (\attrs(list[Attr] attrs):=a) {
         for (Attr attr<-attrs) {
             if (term(category(str c)):=attr) {
                  if (c=="Comment") return true;
                  }
         }
      }
     return false;
     }
     
str getComment(Tree t) {
     if (appl(prod(_,_,Attributes a), list[Tree] ps):=t) {
          if (isComment(a)) return "<t>"; 
          else {
            str r = "";
             for (Tree p<-ps) {
                str g = getComment(p);
                r += g;
                }
            return r;
          }
        }
     return "";
     }
        
public bool isScheme(list[Symbol] q,list[str] b) {
     // println("<size(b)>==<(size(q)+1)/2>"); 
     list[Symbol] f = [p|Symbol p<-q, layouts(_)!:=p];
     // println("b=<b>  f=<f>");
     if (size(b)!=size(f)) return false;
     list[tuple[Symbol,str]] r=[<f[i],b[i]>|int i<-[0..size(b)-1]];
     for (<Symbol s,str z><-r) {
          if (!isTerminal(s)) {
               if (z!="N"&&!isNonTerminal(s,z)) return false;
               }
          else {
               if (z!="T"&&!isTerminal(s,z)) return false;
               }
          }
     return true;
     }

bool isBlock(list[Symbol] q) {
     return isScheme(q,["N",startEndBlock[0],"N",startEndBlock[1]]);
     }

bool isBody(list[Symbol] q) {
     return isScheme(q,[startEndBlock[0],"N",startEndBlock[1]]);
     }

public list[int] isBody(list[Symbol] q, list[Tree] t,int idx) {
     list[Tree] b = [t[i]|int i<-[0,1..size(t)-1], layouts(_)!:=q[i]];
     Tree g=b[idx];
     return (isBody(g)&&userDefined(g)==NULL())?[idx]:[];
     }

public bool isBlock(Tree c) {
     if (appl(prod(list[Symbol] s,_,Attributes att),_):=c) {
          r=isBlock(s);
          return r;
          }
     return false;
     }

public list[int] isBlock(list[Symbol] q, list[Tree] t,int idx) {
     list[Tree] b = [t[i]|int i<-[0,1..size(t)-1], layouts(_)!:=q[i]];
     list[int] r =  (isBlock(g)&&userDefined(g)==NULL())?[idx]:[];
     return r;
     }

public bool isBody(Tree c) {
     if (appl(prod(list[Symbol] s,_,Attributes att),_):=c) {
          return isBody(s);
          }
     return false;
     }

str toString(Attributes att) {
     if (\attrs(list[Attr] a):=att) {
          return (""|it+"_<a[i]>"|i<-[0..size(a)-1]);
          }
     return "<att>";
     }

public list[Tree] getA(Tree q) {
     if (appl(_,list[Tree] z):=q) return z;
     return [];
     }

public Tree getLast(Tree q) {
     list[Tree] a=getA(q);
     return a[size(a)-1];
     }
     
public Tree getFirst(Tree q) {
     list[Tree] a=getA(q);
     if (size(a)==0) return amb({}); // Nothing
     return a[0];
     }
     
public Box evPt(Tree q) {
     return evPt(q,false);
     }

bool isLayout(Tree q) {
     if (appl(prod(_,Symbol s,_),_):=q) {
          return layouts(_):=s;
          }
     return false;
     }
     
Box makeString(Symbol a, Tree t, Attributes att) {
      Box b = NULL();
      if (isString(a)) {
               b=STRING(L("<t>"));
               }
      else if (/\lex():=att || /literal():=att) {
                    str s="<t>";
                    if (endsWith(s,"\n")) s=replaceLast(s,"\n","");
                    b=(isKeyword(a)?KW(L(s)):L(s));
                }
      return b;
      }

public Box C(Tree t, int idx) {
      if (idx<0) return NULL();
      Tree g = getA(t)[2*idx+1];
      Box b = evLayout(getA(g)[0]);
      if (COMM(Box d):=b) {
           if (d!=NULL()) {
             return b;
             }
           }
      return NULL();
      }     
      
public Box evLayout(Tree g) {
      if (appl (\regular(\iter-star(Symbol s), Attributes att),list[Tree] t ):=g) {
      str r = "";
      for (Tree q<-t) {
             r += getComment(q);
             }
       return (size(r)>0?COMM(L("<r>")):COMM(NULL()));
       }
       return COMM(NULL());
      }
      
      

public Box evPt(Tree q,bool doIndent) {
     Box b=userDefined(q);
     if (b!=NULL()) return b;
     // rawPrintln(q); println(q);
     switch (q) {
          case appl ( prod(list[Symbol] s, Symbol r, Attributes att),list[Tree] t ) : {  
                       // println(q);
                       if (layouts(_):=r) return evLayout(t[0]);          
                       // println("r=<r>");
                       Box b = makeString(r, q, att);
                       if (b!=NULL()) {
                             return b;
                             }
                       return walkThroughSymbols(s, t,true,doIndent,-1);        
                    }
          case appl ( \regular(\iter-star-seps(Symbol s,list[Symbol] sep), Attributes att),list[Tree] t) : {
                    return HV(1,getArgsSep(q));
                    }
          case appl ( \regular(\iter-seps(Symbol s, list[Symbol] sep), Attributes att),list[Tree] t) : {
                    return HV(1,getArgsSep(q));
                    }
          case appl ( \regular(\iter-star(Symbol s), Attributes att),list[Tree] t) : {
                    // return HV(0,getArgs(q));
                    return walkThroughSymbols([], t,false,doIndent,-1);
                    }
          case appl ( \regular(\iter(Symbol s), Attributes att),list[Tree] t) : {
                    // return HV(0,getArgs(q));
                    return walkThroughSymbols([], t,false,doIndent,-1);
                    }
          case appl (\regular(Production p, _),list[Tree] t) : {
                    // return HV(0,getArgs(q));
                    println("Exception"); println(p);
                    return walkThroughSymbols([], t,false,doIndent,-1);
                    }
             
          }
     return NULL();
     }

list[Box] addTree(list[Box] out,Tree t) {
     str s="<t>";
     if (size(s)>0) out+=s;
     return out;
     }

Box defaultBox(Box b) {
     if (b==NULL()) return NULL();
     if (H(list[Box] c):=b) {
          if (size(c)>0) return b;
          }
     else if (V(list[Box] c):=b) {
               if (size(c)>0) return b;
               }
          else if (HV(list[Box] c):=b) {
                    if (size(c)>0) return b;
                    }
               else if (HOV(list[Box] c):=b) {
                         if (size(c)>0) return b;
                         }
                    else if (L(str c):=b) {
                              if (size(c)>0&&!startsWith(c," ")) return b;
                              }
                         else if (I(list[Box] c):=b) {
                                   if (size(c)>0) return b;
                                   }
                              else {
                              return b;
                              }
     return NULL(); 
     } 

tuple[Box,Box] compactStyle(Box b) {
     if (V(list[Box] v):=b) {
          return <v[0],V(tail(v))>;
          }
     return <NULL(),b>;
     }

list[Symbol] proj0(list[tuple[Symbol,Tree]] t) {
     return [q[0]|tuple[Symbol, Tree] q <- t];
     }

list[Tree] proj1(list[tuple[Symbol,Tree]] t) {
     return [q[1]|tuple[Symbol, Tree] q <- t];
     }



Box walkThroughSymbols(list[Symbol] y, list[Tree] z,bool hv,bool doIndent,int space) {
     list[Box] out=[],collectList=[],compactList=[];
     // println(y);
     list[int] block = isEmpty(y)?[]:isBlok(y,z);
     // if (size(block)>0) println(block);
     list[int] indent = isIndent(y);
     list[segment] compact= isCompact(y);
     bool collect=false;
     segment q=isEmpty(compact)?<1000,1000>:head(compact);
     if (!isEmpty(compact)) compact=tail(compact);
     bool first=true;
     int i = 0;
     for (Tree t <- z) {
          Box b=defaultBox(evPt(t,(i in block)));
          if (COMM(Box a):=b) {
               if (a!=NULL()) {
                   out+=a;
                   }
               }
          else {
          if (i>=q[0]&&i<=q[1]) {
               compactList+=b;
               if (i==q[1]) {
                    out+=H(0,compactList);
                    compactList=[];
                    if (!isEmpty(compact)) {
                         q=head(compact);
                         compact=tail(compact);
                         }
                    }
               }
          else if (i in block) {
                         // println("Block:<i> <b>");
                         if (isEmpty(out)) out+=I([b]);
                         else {
                         tuple[Box,Box] c=compactStyle(b);
                         if (c[0]==NULL()) out+=c[1]; else {
                              out+=c[0];
                              if (first) {
                                   out = [H(1,out)];
                                   first=false;
                                   }
                              else {
                                   Box last1=out[size(out)-1],last2=out[size(out)-2];
                                   out=slice(out,0,size(out)-2);
                                   out+=H([last2,last1]);
                                   }
                              out+=I([c[1]]);
                              }
                             }
                         }
               else if (i in indent) {
                      if (first) {
                          out = [H(1,out)];
                          first=false;
                          }
                       collectList+= b;
                       }
                    else {
                      if (!isEmpty(collectList)) {
                          out += I([V(collectList)]);
                          out+= b;
                          collectList=[];
                          }
                      else out+=b;
                    }
           // println("i=<i>");
           i=i+1;
           }
          }
     if (!isEmpty(collectList)) {
          out += I([V(collectList)]);
          }
     if (hv && isSeparated(y)) out = [H(1,out)];
     if (size(out)==0) return NULL();
     if (size(out)==1) {
          return out[0];
          }
     else {
          Box r=(hv&&!doIndent &&isEmpty(block) && isEmpty(indent))?HV(-1,out):V(0,out);
          if (space>=0) r@hs=space;
          return r;
          }
     }

map[Tree,Box] aux=() ;


     
public void concrete(Tree a) {
     Box out=evPt(a);
     text t=box2text(out);
     for (str r<-t) {
          println(r);
          }
     println(out);
     }
     

public list[Box] getArgs(Tree g) {
     list[Tree] tl=getA(g);
     if (isEmpty(tl)) return [];
     list[Box] r=[evPt(t)|Tree t<-tl];
     r = [b|Box b<-r, COMM(_)!:=b || (COMM(L(str s)):=b && (size(s)>0))];
     return r; 
     }
 
 public list[Box] getArgsSep(Tree g) {
     list[Box] r = getArgs(g);
     r = [b|Box b<-r, COMM(_)!:=b]; 
     if (isEmpty(r)) return r;
     list[Box] q = [];
     if (size(r)%2==0)
     for (int i<-[0,2..size(r)-1]) {
          q += H(0, [r[i], r[i+1]]);
          }
     else {
     for (int i<-[0,2..size(r)-3]) {
          q += H(0, [r[i], r[i+1]]);
          }
     q+=H(0, [r[size(r)-1]]);
     }
     return q;
     }

     
public Box getConstructor(Tree g,str h1,str h2) {
     list[Box] bs=getArgs(g);
     Box r=HV(0,[L(h1)]+bs+L(h2));
     return r;
     }

public Box cmd(str name,Tree expr,str sep) {
     Box h=H([evPt(expr),L(sep)]);
     h@hs=0;
     return H([KW(L(name)),h]);
     }

public Box HV(int space,list[Box] bs) {
     if (isEmpty(bs)) return NULL();
     Box r=HV([b|Box b<-bs,b!=NULL()]);
     if (space>=0) r@hs=space;
     r@vs=0;
     return r;
     }

public Box H(int space,list[Box] bs) {
     if (isEmpty(bs)) return NULL();
     Box r=H([b|Box b<-bs,b!=NULL()]);
     if (space>=0) r@hs=space;
     return r;
     }

public Box V(int space,list[Box] bs) {
     if (isEmpty(bs)) return NULL();
     Box r=V([b|Box b<-bs,b!=NULL()]);
     if (space>=0) r@vs=space;
     return r;
     }
          
public Box treeToBox(Tree a) {
      return evPt(a);
      }



