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
     
bool isKeyword(Symbol a) {
     if (\lit(str s):=a) {
         if (s=="begin" || s == "end" || s == "declare" || s == "while" || s == "if"
            || s == "then" || s == "do" || s == "od" || s == "fi") return true;
         }
     return false;
     }
     
void setUserRules() {
    setIndented(isIndented);
    setCompact(isCompact);
    setKeyword(isKeyword);
    }  
     
public text toText(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
     setUserRules();
     text r = toText(a);
     writeData(asf, r, ".txt");
     return r;
     }
   
public text toLatex(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
     // rawPrintln(a);
     setUserRules();
     text r = toLatex(a);
     writeData(asf, r, ".tex");
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
    
