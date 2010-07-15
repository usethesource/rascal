module box::box::Default
import box::box::Input;
import ParseTree;
import box::Concrete;
import box::Box;
import IO;
import box::box::Basic;


alias UserDefinedFilter = Box(Tree t) ;

list[UserDefinedFilter] userDefinedFilters = [ 
       getBasic
       ];

list[int] isIndented(list[Symbol] q, list[Tree] z) {
     return [];
     }

list[segment] isCompact(list[Symbol] q) {
     return [];
     }

bool isSeperated(list[Symbol] q) {
     return false;
     }
     
public text toList(loc asf){
     Tree a = inPut(asf);
     return returnText(a, extraRules, isIndented, isCompact, isSeperated);
     }

// Don't change this part 

public Box extraRules(Tree q) {  
   for (UserDefinedFilter userDefinedFilter<-userDefinedFilters) {
           Box b = userDefinedFilter(q);
           if (b!=NULL()) return b;
           }
    return NULL();
    }
    

/*
public void main(){
    Tree a = parse(#Module, |file:///ufs/bertl/asfix/A.rsc|);
    concrete(a);
    }
*/
