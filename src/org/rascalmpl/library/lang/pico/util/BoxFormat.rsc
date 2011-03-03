module lang::pico::util::BoxFormat
import ParseTree;
import lang::box::util::Concrete;
import lang::box::util::Box;
import IO;

import lang::pico::syntax::Main;

/*
alias UserDefinedFilter = Box(Tree t) ;

list[Box(Tree t)] userDefinedFilters = [ 
       ];
*/

list[int] isIndented(list[Symbol] q) {
     if (isScheme(q , ["begin", "N", "N", "end"])) return [1, 2];
     if (isScheme(q , ["declare", "N", ";"])) return [1];
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
            || s == "then" || s == "do" || s == "od" || s == "fi" || s=="else") return true;
         }
     return false;
     }
     
void setUserRules() {
    setIndent(isIndented);
    setCompact(isCompact);
    setKeyword(isKeyword);
    } 

/* --- Interface -- */  
     
public Box toBox(loc src) {
    PROGRAM a = parse(#PROGRAM, src);
    setUserRules();
    return treeToBox(a);
    }
