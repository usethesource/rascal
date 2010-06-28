module box::Rsc
import ParseTree;
import box::Concrete;
import box::Box;
import IO;
import box::rascal::Modules;
import box::rascal::Declarations;
import box::rascal::Constructors;
import box::rascal::Rascal;

import rascal::\old-syntax::Rascal;
import rascal::\old-syntax::Modules;
import rascal::\old-syntax::Names;


public Box extraRules(Tree q) {  
   Box b = NULL();
   b=getModules(q);
   if (b!=NULL()) return b;
   b=getDeclarations(q);
   if (b!=NULL()) return b;
   b=getConstructors(q);
   if (b!=NULL()) return b;
   b=getRascal(q);
   if (b!=NULL()) return b;
   return b;
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
