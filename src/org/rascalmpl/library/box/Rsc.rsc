module box::Rsc
import ParseTree;
import box::Concrete;
import box::Box;
import IO;
import box::rascal::Modules;
import box::rascal::Declarations;
import box::rascal::Constructors;
import box::rascal::Expressions;
import box::rascal::Rascal;

import rascal::\old-syntax::Rascal;
import rascal::\old-syntax::Modules;
import rascal::\old-syntax::Names;

list[int] isIndented(pairs u) {
        list[Symbol] q = [s|<Symbol s, _><-u];
        list[Tree] z  = [a |<_, Tree a><-u];
        if (isScheme(q , ["N","T", "T", "N", "T", "N"])) return isBlock(z, 5);  // for
        if (isScheme(q , ["N","T", "T", "N", "T", "N", "N"])) return isBlock(z, 5);  // if then
        if (isScheme(q , ["N", "N", "N", "N"])) return isBody(z,3); // Visibility Signature FunctionBody
        if (isScheme(q , ["N","T", "T", "N", "T", "N", "T", "N"])) return isBlock(z,5)+isBlock(z,7); // If then else
        if (isScheme(q , ["T", "N", "N"])) return isBlock(z, 1);  // try
        if (isScheme(q , ["T", "T", "N"])) return isBlock(z, 2);  // catch
        if (isScheme(q , ["T", "N", "T", "N"])) return  isBlock(z, 3);  // catch
        if (isScheme(q , ["N", "T", "N"])) return isBlock(z, 2);  // pattern with action
     return [];
     }


public Box extraRules(Tree q) {  
   Box b = NULL();
   b=getExpressions(q);
   if (b!=NULL()) return b;
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
     return returnText(a, extraRules, isIndented);
     }

/*
public void main(){
    Tree a = parse(#Module, |file:///ufs/bertl/asfix/A.rsc|);
    concrete(a);
    }
*/
