module box::Sdf
import box::Concrete;
import box::Box;
import IO;
import List;
import String;
import box::Aux;

import languages::sdf3::syntax::Modules;
// import languages::sdf3::syntax::Symbols;
// import languages::sdf3::syntax::Kernel;
// import languages::sdf3::syntax::Sorts;
// import languages::sdf3::syntax::Basic;
// import languages::sdf3::syntax::Literals;
import languages::sdf3::syntax::Labels;
import basic::StrCon;



public Box extraRules(Tree q) {   
   /*   
   if (Import a:=q) {
      if  (` import <ImportedModule m>  ; `  := a )  
            return cmd("import",  m, ";");
       }
   if (Header a:=q) {
         if  ( `  module  <QualifiedName n>  <Import* imp> `  := a )  {
             list[Box ] h = [H( [L("module"), evPt(n)])];
             return V(h+getArgs(imp, #Import));
         }
   }
   */
   return NULL();
   }

  
/*
public str getSort(Symbol u) {
   if (\cf(\sort(str s)):=u) {return s;}
   return "";
   }
*/

public list[Sort]  getSorts(Tree t) {
   list[Sort] r = [];
   for (/Grammar s <- t) {
       if ( `  sorts <Sym* a> ` := s ) {
          r+=[c | /Sort c <- a];
       }
   }
   return r;
}


bool isSort(Prod p, Sort sr) {
    list[Sort] srt = [s | /Sort s <- p];
    return srt[size(srt)-1]==sr;
   }


public list[Prod] getProductions(Tree t, Sort srt) {
     list[Prod] g = [p| /Prod p <- t, isSort(p, srt) ];
     return g;
     }

public str  makeProgram(Tree t) {
     str u = "";
     list[Sort] srts = getSorts(t);
     for (Sort srt<-srts) {
        println("QQ:<srt>");
         list[Prod] r= getProductions(t, srt);
         if (size(r)>0) u+="if (`<srt> a`:=q) \nswitch(a) {\n";
         for (\Prod p<- r) {
            u+="\tcase `";
            list[Tree] ps = getArgs(p);
            for (Tree z<-ps) {
            if (Sym q:=z) {
               switch (q) {
                  case  `<Label a> : <Sort b>*`:  u+="\<<b>* <a>\> ";
                  case  `<Label a> : <Sort b>`:  u+="\<<b> <a>\> ";
                  case `<Label c> : { <Sym a> <Sym b> } * `: u+="\<{<a> <b>}*  c \> ";
                  case  `<Label c>  : { <Sym a> <Sym b>  }+`:  u+="\<{<a> <b>}+  c \> ";
                  default: if (StrCon s := getA(z)[0]) {
                        str w = "<s>";
                        u+= substring(w, 1, size(w)-1)+" ";
                       }

                }
            }    
        }
       u+="`: return NULL();\n";
      }
      if (size(r)>0)  u+="}\n";
      }
      return u;
}

list[Tree] getArgs(Tree q) {
      switch(q) {
       case appl(prod(list[Symbol] s, _, Attributes att), list[Tree] t): {
                      pairs u =[<s[i], t[i]>| int i<-[0,2..(size(t)-1)]]; 
                      // println("MATCH1:<t[0]>");
                      switch (t[0])  {
                             case appl(prod(list[Symbol] x, _, Attributes att), list[Tree] y): {
                                 pairs v =[<x[i], y[i]>| int i<-[0,2..(size(y)-1)]];
                                 // println("MATCH2:<y[0]>");
                                 switch (y[0])  {
                                     case appl(\list(\cf(\iter-star(Symbol a) )), list[Tree] b): {
                                      return [b[i]|int i<-[0,2 .. (size(b)-1)]];
                                      }
                              }
                     }
          }
      }
     }
      return [];
}
   
public text toList(loc asf){
     Module a = readFromLoc (asf);
     println(makeProgram(a));
     return returnText(a, extraRules);
     }

/*
public void main(){
    Tree a = parse(#Module, |file:///ufs/bertl/asfix/A.rsc|);
    concrete(a);
    }
*/