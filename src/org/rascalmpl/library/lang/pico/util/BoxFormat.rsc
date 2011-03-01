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
    

     
public text toText(loc src, loc dest){
     PROGRAM a = parse(#PROGRAM, src);
     setUserRules();
     text r = toText(a);
     toExport(src, dest, r, ".txt");
     return r;
     }
     
 public text toHtml(loc src, loc dest){
     PROGRAM a = parse(#PROGRAM, src);
     setUserRules();
     text r = toHtml(a);
     toExport(src, dest, r, ".html");
     return r;
     }  
     
public text toLatex(loc src, loc dest){
     PROGRAM a = parse(#PROGRAM, src);
     // rawPrintln(a);
     setUserRules();
     text r = toLatex(a);
     toExport(src, dest, r, ".tex");
     return r;
     } 

public text toRichText(loc asf){
     PROGRAM a = parse(#PROGRAM, asf);
     setUserRules();
     text r = toRichText(a);
     return r;
     } 
          
public Box toBox(loc asf) {
     Tree a = parse(#PROGRAM, asf);
     setUserRules();
     return toBox(a);
     }  

// Don't change this part 

public Box extraRules(Tree q) {  
   /*
   for (Box(Tree t) userDefinedFilter<-userDefinedFilters) {
           Box b = userDefinedFilter(q);
           if (b!=NULL()) return b;
           }
     */
    return NULL();
    }
    
