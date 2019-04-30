module lang::rascalcore::compile::Examples::Tst2
 
import Type;
//import ParseTree;
//  
//type[value] main(){
//   return #int;
//}      
                      
syntax S = "abc";
data D = d1(str s, bool b = false) | d2(int n);
      
                                                            
value main() = #S.symbol; // == \str();  