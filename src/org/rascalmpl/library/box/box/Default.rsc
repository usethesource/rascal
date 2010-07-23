module box::box::Default
import box::box::Input;
import box::box::Basic;
import ParseTree;
import box::Concrete;
import box::Box;
import IO;

alias UserDefinedFilter = Box(Tree t) ;

list[UserDefinedFilter] userDefinedFilters = [ 
       getBasic
       ];
 
public text toText(loc asf){
     Tree a = inPut(asf);
     setUserDefined(extraRules);
     return toText(a);
     }

public text toLatex(loc asf){
     Tree a = inPut(asf);
     setUserDefined(extraRules);
     text r = toLatex(a);
     writeLatex(asf, r, ".box");
     return r;
     } 
    
// Don't change this part 

public Box extraRules(Tree q) {  
   for (UserDefinedFilter userDefinedFilter<-userDefinedFilters) {
           Box b = userDefinedFilter(q);
           if (b!=NULL()) return b;
           }
   return NULL();
   }
