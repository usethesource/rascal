module box::Sdf
import box::Concrete;
import box::Box;
import IO;
import List;
import String;
import languages::sdf22::syntax::Modules;
import languages::sdf22::syntax::Labels;
import basic::StrCon;
import box::Aux;


bool debug = false;

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

public set[Sort]  getSorts(Tree t) {
   set[Sort] r = {getSort(p)|/Prod p <- t};
   return r;
}

Sort getSort(Prod p) {
    list[Sort] srt = [s | /Sort s <- p];
    return srt[size(srt)-1];
   }

bool isSort(Prod p, Sort sr) {
    list[Sort] srt = [s | /Sort s <- p];
    return getSort(p)==sr;
   }


public list[Prod] getProductions(Tree t, Sort srt) {
     list[Prod] g = [p| /Prod p <- t, isSort(p, srt) ];
     return g;
     }

public str  makeProgram(Tree t, str moduleName) { 
      str u = "module templates::<moduleName>\n";
      /*
      if (Module m := t) {
           if (`module <ModuleName n> <ImpSection* v> <Sections g>`:=m) {
                str s = replaceAll("<n>","/","::");
                s = replaceAll(s,"old-syntax", "\\old-syntax");
                u+="import <s>;\n";
                }
           }
       */
    u+="import rascal::\\old-syntax::Rascal;\n";     
    u+="import box::Box;\n";
    u+="import box::Concrete;\n";
    /*
    for (/Import m <- t) {
           if (`<Import n>`:=m) {
                str s = replaceAll("<n>","/","::");
                s = replaceAll(s,"old-syntax", "\\old-syntax");
                u+="import <s>;\n";
                }
           }
     */
     u += "public Box get<moduleName>(Tree q) {\n";
     set[Sort] srts = getSorts(t);
     for (Sort srt<-srts) {
        // println("QQ:<srt>");
         list[Prod] r= getProductions(t, srt);
         if (size(r)>0) u+="if (<srt> a:=q) \nswitch(a) {\n";
         for (\Prod p<- r) {
            if (debug) {
                              println(`<Symbols a> -> <Sym b> <Attributes at>`:=p);
                              println(p);
                              }
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
      u+="return NULL();\n";
      u+="}\n";
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
   
public str toStr(loc asf, str moduleName){
     Module a = readFromLoc (asf);
     println(asf.path);
     return makeProgram(a, moduleName);
     }

/*
public void main(){
    Tree a = parse(#Module, |file:///ufs/bertl/asfix/A.rsc|);
    concrete(a);
    }
*/