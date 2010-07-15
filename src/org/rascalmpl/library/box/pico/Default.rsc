module box::pico::Default
import ParseTree;
import box::Concrete;
import box::Box;
import IO;

import languages::pico::syntax::Pico;

alias UserDefinedFilter = Box(Tree t) ;

list[UserDefinedFilter] userDefinedFilters = [ 
       ];

list[int] isIndented(list[Symbol] q, list[Tree] z) {
     if (isScheme(q , ["begin", "N", "N", "end"])) return [1, 2];
     if (isScheme(q , ["if", "N", "then", "N", "else",  "N", "fi"])) return [3, 5];
     if (isScheme(q , ["while", "N", "do", "N", "od"])) return [3];
     return [];
     }

list[segment] isCompact(list[Symbol] q) {
     if (isScheme(q , ["if", "N", "then", "N", "else",  "N", "fi"])) return [<1,1>];
     if (isScheme(q , ["while", "N", "do", "N", "od"])) return [<1,1>];
     return [];
     }

bool isSeperated(list[Symbol] q) {
     return false;
     }
     
public text toList(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
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
