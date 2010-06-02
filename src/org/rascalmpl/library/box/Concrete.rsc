module box::Concrete
import ParseTree;
import  ValueIO;
import  IO;
import List;
import String;
import box::Box;
import box::Box2Text;

alias pairs = list[tuple[Symbol, Tree]] ;

Tree getTree(ParseTree t) {
     switch (t) {
          case parsetree(Tree top, _): {
              switch(top) {
                  case appl(prod(_ , _, _), list[Tree] t): { 
                      Tree q = t[1];
                      // rawPrintln(q);
                      return q;
                      }
                    }
                }
           }
          return t;    
      }

bool isTerminal(Symbol s) {
     return ((\lit(_):= s)) ||  (\char-class(_):=s);
     }

bool _isIndented(pairs u) {
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
     
bool isIndented(pairs u) {
     while (size(u)>=4 && !_isIndented(u)) {
          u=tail(u);
          }
     return size(u)>=4;
     }
     

Box ppDefault(pairs u) {
       list[Box] bl = walkThroughSymbols(u, false);
       if (size(bl)==0) return H([]);
       Box r =  (size(bl)==1)?bl[0]:HV(bl);
       if (size(bl)>1)  r@hs = 1;
       return r;
       }


Box visitParseTree(Tree q) {
   //  rawPrintln(q);
    switch(q) {
       case appl(prod(list[Symbol] s, _, _), list[Tree] t): {
                      pairs u =[<s[i], t[i]>| int i<-[0,2..size(t)-1]];
                      return _isIndented(u)?V(walkThroughSymbols(u, true)):ppDefault(u);                                      
                     }
       case appl(\list(\cf(\iter-star-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..size(t)-1]];
                      return V(walkThroughList(u, sep));
                     }
        case appl(\list(\cf(\iter-sep(Symbol s, Symbol sep) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..size(t)-1]];
                      Box r = HV(walkThroughList(u, sep));
                      r@hs = 0;
                      return r;
                     }
        case appl(\list(\cf(\iter-star(Symbol s) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..size(t)-1]];
                      Box r = HV(walkThroughList(u));
                      r@hs = 0;
                      return r;
                     }
        case appl(\list(\cf(\iter(Symbol s) )), list[Tree] t): {
                      pairs u =[<s, t[i]>| int i<-[0,2..size(t)-1]];
                      Box r =  HV(walkThroughList(u));
                      r@hs = 0;
                      return r;
                     }
       // case appl(Production p , list[Tree] t): {println("<p>:<t>");}
       }
       return H([]);
}

list[Box] walkThroughList(pairs u, Symbol sep) {
  list[Box] out = [];
  list[Box] r = [];
   for  (<Symbol a, Tree t><-u) {
       //  println(a);
       switch(a) {
          case \lex(Symbol s): {
             out+=walkThroughList([<s, t>], sep);
             }
        case \opt(Symbol s): {
             return walkThroughList([<s, t>]);
             }
          case \iter(Symbol s): {
             return walkThroughList([<s, t>]);
             }
         case \lex(\sort(str c)): {
             out+=L("<t>");
             }
        case \lit(str c): {
             out+=L("<t>");
            }
       case \char-class(_): {
            out+=L("<t>");
            }   
       default: {
                Box  b = visitParseTree(t);
                out+= defaultBox(b, false);
                }    
        }
         if (size(out)==2) {
                       Box b = H([out[0], out[1]]);
                       b@hs = 0;
                       r+=  b;
                       out=[];
                      }
         // println("Klaar:<out>");
         } 
         if (size(out)>0) {
              Box b = H(out);
              r+=  b;
              }
   return r;
}

 list[Box] walkThroughList(pairs u) {
    list[Box] out = [];
   list[Box] r = [];
   for  (<Symbol a, Tree t><-u) {
       switch(a) {
        case \opt(Symbol s): {
             return walkThroughList([<s, t>]);
             }
          case \iter(Symbol s): {
             return walkThroughList([<s, t>]);
             }
         case \lex(\sort(str c)): {
             if (size(t)>0) out+=L("<t>");
             }
        case \lit(str c): {
             if (size(t)>0) out+=L("<t>");
            }
       case \char-class(_): {
            out+=L("<t>");
            }   
       default: {
                Box  b = visitParseTree(t);
                out+= defaultBox(b, false);
                }    
        }
         // println("Klaar:<out>");
         } 
        if (size(out)>0) {
              Box b = V(out);
               r+=  b;
              }
   return r;
}

list[Box] defaultBox(Box b, bool indent) {
               list[Box] out = [];
               if (H(list[Box] c):=b) {
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
   
list[Box] compact(list[Box] out, str c) {
                 if (size(out)>0 && (c=="." || c==";" || c=="(" || c==")") ) {
                          Box b = out[size(out)-1];
                          out = head(out, size(out)-1);
                           b = H([b, L(c)]);
                           b@hs = 0;
                           out+=b;  
                          } 
                  else out+=L(c);
                  return out;
                 }

list[Box] walkThroughSymbols(pairs u, bool indent) {
  list[Box] out = [];
   for  (<Symbol a, Tree t><-u) {
       // println(a);
       switch(a) {
        case \lex(\sort(str c)) : {
           out += (indent?I([L("<t>")]):L("<t>"));
          }
        case \lit(str c): {
             out+= L("<t>");
             // out=compact(out,"<t>");
            }
       case \char-class(_): {
            out+= L("<t>");
           }   
       default: {
             Box  b = visitParseTree(t);
              out+= defaultBox(b, indent);
              }
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
     // ParseTree a = readBinaryValueFile(#ParseTree, |file:///ufs/bertl/asfix/pico/big.asf|);
     // ParseTree a = readBinaryValueFile(#ParseTree, |file:///ufs/bertl/asfix/java/ViewAction.asf|);
     ParseTree a = readBinaryValueFile(#ParseTree, asf);
     Box out = visitParseTree(getTree(a));
     // println(out);
     return box2text(out);
     }

