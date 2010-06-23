module box::Rsc
import ParseTree;
import box::Concrete;
import box::Box;
import IO;
import templates::Constructors;

import languages::rascal::syntax::RascalForConcreteSyntax;
import languages::rascal::syntax::Modules;
import languages::rascal::syntax::Names;


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
   return getConstructors(q);
   }

 
   
public text toList(loc asf){
     Module a = parse(#Module, asf);
     return returnText(a, extraRules);
     }

/*
public void main(){
    Tree a = parse(#Module, |file:///ufs/bertl/asfix/A.rsc|);
    concrete(a);
    }
*/
